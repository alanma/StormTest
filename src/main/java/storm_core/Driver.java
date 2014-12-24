package storm_core;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.cluster.StormClusterState;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

public class Driver {
	public static void main(String[] args) throws InterruptedException {
		TopologyBuilder builder = new TopologyBuilder();
		String wordCountSpout = "wordCountSpout";
		String wordSplitBolt = "wordSplitBolt";
		String wordCounter = "wordCounter";

		// 在spout要指定生成的componentId
		builder.setSpout(wordCountSpout, new WordCountSpout());

		// 在bolt中要指定所处理的componentId 和 产生的componentId
		builder.setBolt(wordSplitBolt, new WordSplitBolt()).shuffleGrouping(
				wordCountSpout);
		builder.setBolt(wordCounter, new WordCountBolt(), 1).fieldsGrouping(
				wordSplitBolt, new Fields("word"));

		Config conf = new Config();
		conf.put("wordsFile", args[0]);
		conf.setDebug(true);// ?
		conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);// ?

		String mode = args[1];
		if (mode.equals("local")) {
			LocalCluster localCluster = new LocalCluster();
			localCluster.submitTopology("WordCountTopology", conf,
					builder.createTopology());
			Thread.sleep(10000);
			localCluster.killTopology("WordCountTopology");
			localCluster.shutdown();
		} else {
			try {
				StormSubmitter.submitTopology("WordCountTopology", conf,
						builder.createTopology());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
