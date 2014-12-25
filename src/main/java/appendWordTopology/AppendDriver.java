package appendWordTopology;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Time;

public class AppendDriver {
	public static void main(String[] args) throws AlreadyAliveException,
			InvalidTopologyException, InterruptedException {
		TopologyBuilder topology = new TopologyBuilder();
		topology.setSpout("word", new AppendWordSpout(true), 2);
		topology.setBolt("appendWord", new AppendWordBolt(), 2)
				.shuffleGrouping("word");

		Config conf = new Config();
		conf.setDebug(true);

		// remote mode
		if (args != null && args.length > 0) {
			StormSubmitter.submitTopology(args[0], conf,
					topology.createTopology());
		} else {
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("wordAppend", conf,
					topology.createTopology());
			Thread.sleep(5000);
			cluster.killTopology("wordAppend");
			cluster.shutdown();
		}
	}
}
