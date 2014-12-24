package storm_trident;

import storm.trident.TridentState;
import storm.trident.TridentTopology;
import storm.trident.operation.builtin.Count;
import storm.trident.testing.FixedBatchSpout;
import storm.trident.testing.MemoryMapState;
import storm.trident.testing.Split;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class TridentAPITest {
	public static void main(String[] args) throws InterruptedException {
		FixedBatchSpout spout = new FixedBatchSpout(new Fields("sentence"), 3,
				new Values("hello world"), new Values("hello wang jing jing"),
				new Values("hello Shanghai"));

		TridentTopology topology = new TridentTopology();
		TridentState state = topology.newStream("mystream", spout)
				.each(new Fields("sentence"), new Split(), new Fields("words"))
				// words fields append to sentence fields
				.groupBy(new Fields("words"))
				.persistentAggregate(new MemoryMapState.Factory(),
						new Fields("words"), new Count(), new Fields("count"));
		// System.out.println(state.newValuesStream().toString());

		// ʵ���Զ����Filter��
		// Stream steam = topology.newStream("stream1", spout).each(
		// new Fields("sentence"), new LengthFilter());

		// �Զ���Split��,CombinerAggregator��
		topology.newStream("stream2", spout)
				.each(new Fields("sentence"), new MySplit(),
						new Fields("words"))
				.groupBy(new Fields("words"))
				.aggregate(new Fields("words"), new MyCount(),
						new Fields("count"));
		StormTopology stormTopology = topology.build();
		//
		// // �Զ����reducerAggregation��
		// topology.newStream("stream2", spout)
		// .each(new Fields("sentence"), new MySplit(),
		// new Fields("words"))
		// .groupBy(new Fields("words"))
		// .aggregate(new Fields("words"), new MyReducerAgg(),
		// new Fields("count"));
		//
		// // �Զ����BaseAggregation��
		// topology.newStream("stream2", spout)
		// .each(new Fields("sentence"), new MySplit(),
		// new Fields("words"))
		// .groupBy(new Fields("words"))
		// .aggregate(new Fields("words"), new MyAggregation(),
		// new Fields("count"));
		//
		// // ͬʱ��source dataִ�ж��aggregation���������յõ��Ľ����ͬʱ������Щaggregation��fields���
		// topology.newStream("stream2", spout)
		// .each(new Fields("sentence"), new MySplit(),
		// new Fields("words"))
		// .groupBy(new Fields("words"))
		// .chainedAgg()
		// .aggregate(new MyCount(), new Fields("count"))
		// .aggregate(new Fields("word"), new MyAggregation(),
		// new Fields("count1")).chainEnd();
		Config conf = new Config();
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("wordCounter", conf, stormTopology);
		Thread.sleep(60 * 1000);
		cluster.killTopology("wordCounter");
		cluster.shutdown();
		System.exit(0);
	}

}
