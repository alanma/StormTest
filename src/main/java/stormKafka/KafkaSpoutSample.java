package stormKafka;

import java.util.ArrayList;
import java.util.Map;

import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class KafkaSpoutSample {

	// public static final Logger logger = LoggerFactory
	// .getLogger(KafkaSpoutSample.class);

	public static class PrintBolt extends BaseRichBolt {
		ArrayList<String> sentence = new ArrayList<String>();

		@Override
		public void prepare(Map stormConf, TopologyContext context,
				OutputCollector collector) {

		}

		@Override
		public void execute(Tuple input) {
			// logger.info(input.toString()); // 这个log的存放路径，log的配置？
			System.out.println(input.getValue(0) + ":" + input.getValue(1));
		}

		@Override
		public void declareOutputFields(OutputFieldsDeclarer declarer) {
		}

		@Override
		public void cleanup() {
			System.out.println("==============");
			System.out.println("Sentence output is:");
			for (String sent : sentence) {
				System.out.println(sent);
			}
			System.out.println("End of Sentence.");
			System.out.println("==============");
		}
	}

	BrokerHosts hosts;

	public KafkaSpoutSample(String zkHosts) {
		hosts = new ZkHosts(zkHosts);
	}

	/**
	 * 把kafka中的某个topic作为spout的输入
	 * 
	 * @return
	 */
	public StormTopology buildTopology() {
		SpoutConfig spoutConf = new SpoutConfig(hosts, "storm-sentence", "",
				"storm");
		spoutConf.scheme = new SchemeAsMultiScheme(new MyTestScheme());
		spoutConf.forceFromStart = true; // 强制从头开始读，否则读取新增的数据，如果没有产生新的数据则无法从kafka中拉取数据
		KafkaSpout spout = new KafkaSpout(spoutConf);
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("myspout", spout);
		builder.setBolt("printBolt", new PrintBolt())
				.shuffleGrouping("myspout");
		return builder.createTopology();
	}

	public static void main(String[] args) throws InterruptedException {
		String zookeeperHost = args[0];
		KafkaSpoutSample sample = new KafkaSpoutSample(zookeeperHost);
		Config conf = new Config();
		conf.put(Config.TOPOLOGY_TRIDENT_BATCH_EMIT_INTERVAL_MILLIS, 2000);
		if (args != null && args.length == 1) {
			conf.setNumWorkers(2);
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("printTopology", conf,
					sample.buildTopology());
		}
		Thread.sleep(1000);
	}
}
