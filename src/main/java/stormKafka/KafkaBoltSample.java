package stormKafka;

import java.util.Properties;

import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;
import storm.kafka.bolt.KafkaBolt;
import storm.kafka.bolt.mapper.FieldNameBasedTupleToKafkaMapper;
import storm.kafka.bolt.selector.DefaultTopicSelector;
import storm.kafka.trident.TridentKafkaState;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;

/**
 * 读取kafka中的topic到storm中，再将storm中的tuple发送到对应的kafka中的topic下
 * 
 * @author wangjj
 * 
 *         Jan 6, 2015
 */
public class KafkaBoltSample {

	ZkHosts hosts;

	public KafkaBoltSample(String zkHosts) {
		hosts = new ZkHosts(zkHosts);
	}

	public StormTopology build() {
		TopologyBuilder builder = new TopologyBuilder();
		SpoutConfig config = new SpoutConfig(hosts, "storm-sentence", "",
				"storm");
		config.scheme = new SchemeAsMultiScheme(new MyTestScheme());
		config.forceFromStart = true;
		KafkaSpout spout = new KafkaSpout(config);
		builder.setSpout("kafkaSpout", spout);

		// 设置bolt的topic selector和tupleToKafkaMapper
		KafkaBolt bolt = new KafkaBolt().withTopicSelector(
				new DefaultTopicSelector("BoltTopic")).withTupleToKafkaMapper(
				new FieldNameBasedTupleToKafkaMapper("key", "message"));

		builder.setBolt("bolt", bolt).shuffleGrouping("kafkaSpout");
		return builder.createTopology();
	}

	public static void main(String[] args) throws InterruptedException {
		String zookeeper = args[0];
		KafkaBoltSample sample = new KafkaBoltSample(zookeeper);
		Config conf = new Config();
		Properties props = new Properties();

		// 此时的bolt相当于是kafka
		// producer，因此需要指定的是broker-list，以及从producer端接收到的数据序列化成为kafka中的存储形式
		props.put("metadata.broker.list", "localhost:9092,localhost:9093");
		props.put("request.required.acks", "1");
		props.put("serializer.class", "kafka.serializer.StringEncoder");

		conf.put(TridentKafkaState.KAFKA_BROKER_PROPERTIES, props);
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("kafkaBoltTopology", conf, sample.build());
		Thread.sleep(1000);
	}
}
