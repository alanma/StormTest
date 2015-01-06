package stormKafka;

import java.util.Properties;

import storm.kafka.trident.TridentKafkaState;
import storm.kafka.trident.TridentKafkaStateFactory;
import storm.kafka.trident.TridentKafkaUpdater;
import storm.kafka.trident.mapper.FieldNameBasedTupleToKafkaMapper;
import storm.kafka.trident.selector.DefaultTopicSelector;
import storm.trident.Stream;
import storm.trident.TridentTopology;
import storm.trident.testing.FixedBatchSpout;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

/**
 * 将State中包含的值写入到Kafka中去，主要是通过TridentStateFactory，
 * 再利用partitionPersist将state中的内容存放到kafka中去
 * 
 * @author wangjj
 * 
 *         Jan 6, 2015
 */
public class KafkaTridentBoltSample {
	public StormTopology build() {
		Fields fields = new Fields("word", "count");
		FixedBatchSpout spout = new FixedBatchSpout(fields, 3, new Values(
				"trident", "trident1"), new Values("bolt", "bolt"), new Values(
				"bolt", "bolt"), new Values("sample", "sample"));
		spout.setCycle(true);
		TridentTopology topology = new TridentTopology();
		Stream stream = topology.newStream("myspout", spout);
		TridentKafkaStateFactory stateFactory = new TridentKafkaStateFactory()
				.withKafkaTopicSelector(new DefaultTopicSelector("TridentBolt"))
				.withTridentTupleToKafkaMapper(
						new FieldNameBasedTupleToKafkaMapper("word", "count"));
		stream.partitionPersist(stateFactory, fields,
				new TridentKafkaUpdater(), new Fields());
		return topology.build();
	}

	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put("metadata.broker.list", "localhost:9092,localhost:9093");
		properties.put("request.required.acks", "1");
		properties.put("serializer.class", "kafka.serializer.StringEncoder");
		Config config = new Config();
		config.put(TridentKafkaState.KAFKA_BROKER_PROPERTIES, properties);
		LocalCluster cluster = new LocalCluster();
		KafkaTridentBoltSample sample = new KafkaTridentBoltSample();
		cluster.submitTopology("TridentBoltTopology", config, sample.build());
	}
}