package stormKafka;

import java.util.UUID;

import backtype.storm.spout.SchemeAsMultiScheme;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import storm.kafka.trident.OpaqueTridentKafkaSpout;
import storm.kafka.trident.TridentKafkaConfig;

public class SpoutConfigSample {
	/**
	 * 创建一个kafkaspout
	 * 
	 * @param zookeeper
	 * @param topic
	 */
	public void createKafkaSpout(String zookeeper, String topic) {
		ZkHosts hosts = new ZkHosts(zookeeper);
		SpoutConfig spoutConfig = new SpoutConfig(hosts, topic, "/" + topic,
				UUID.randomUUID().toString());
		spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
		KafkaSpout spout = new KafkaSpout(spoutConfig);
	}

	/**
	 * 创建一个trident的spout
	 * 
	 * @param zookeeper
	 * @param topic
	 */
	public void createTridentSpout(String zookeeper, String topic) {
		ZkHosts hosts = new ZkHosts(zookeeper);
		TridentKafkaConfig conf = new TridentKafkaConfig(hosts, topic);
		conf.scheme = new SchemeAsMultiScheme(new StringScheme());
		OpaqueTridentKafkaSpout spout = new OpaqueTridentKafkaSpout(conf);
	}
}
