package wordCountTrident;

import storm.trident.TridentState;
import storm.trident.TridentTopology;
import storm.trident.operation.builtin.Count;
import storm.trident.operation.builtin.FilterNull;
import storm.trident.operation.builtin.MapGet;
import storm.trident.operation.builtin.Sum;
import storm.trident.testing.FixedBatchSpout;
import storm.trident.testing.MemoryMapState;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class WordCountTrident {

	public static StormTopology buildTopology(LocalDRPC drpc) {
		FixedBatchSpout spout = new FixedBatchSpout(new Fields("sentence"), 3,
				new Values("one"), new Values("two"), new Values("three"),
				new Values("four"));
		spout.setCycle(true);

		TridentTopology topology = new TridentTopology();
		TridentState state = topology
				.newStream("myspout", spout)
				.each(new Fields("sentence"), new MySentenceSplit(),
						new Fields("words"))
				.groupBy(new Fields("words"))
				.persistentAggregate(new MemoryMapState.Factory(), new Count(),
						new Fields("count"));

		topology.newDRPCStream("wordCount", drpc)
				// drpc会接收到client发送的function name和arguments
				.each(new Fields("args"), new MySentenceSplit(),
						new Fields("words"))
				// words stream
				.groupBy(new Fields("words"))
				.stateQuery(state, new Fields("words"), new MapGet(),
						new Fields("count"))
				// group by方法提供批量查询？
				.each(new Fields("count"), new FilterNull())  //对空的查询结果要进行filter，否则会导致Null exception
				.aggregate(new Fields("count"), new Sum(), new Fields("sum")); // 对get到的结果进行combine

		// 将这个结果进行返回
		return topology.build();
	}

	public static void main(String[] args) throws Exception {
		
		Config conf = new Config();
//		conf.setDebug(true);
		if (args.length == 0) {
			LocalDRPC drpc = new LocalDRPC();
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("wordCountT", conf, buildTopology(drpc));
			for (int i = 0; i < 10; i++) {
				System.out.println("The result is "
						+ drpc.execute("wordCount", "one two three"));
				Thread.sleep(1000);
			}
		} else {
			StormSubmitter.submitTopology("wordCountT", conf,
					buildTopology(null));
		}

		// cluster.shutdown();
	}
}
