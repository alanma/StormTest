package kafkaSample;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

/**
 * ָ��һ��topic��localhost��ͬʱ�������topic
 * 
 * @author wangjj
 * 
 *         Jan 4, 2015
 */
public class MyConsumerTest {

	public static String topic;
	public static ConsumerConnector consumer;
	public static int threadNum;
	public static ExecutorService executorService;

	/**
	 * ����һ��consumer�����ӣ���Ҫ�ȵõ�ConsumerConfig
	 * 
	 * @param zookeeper
	 *            consumer��Ҫ���ӵ���zookeeper server
	 * @param groupId
	 *            ���consumer�����ڵ�group
	 * @param topic
	 *            ���group��Ҫ���ѵ�topic
	 */
	public MyConsumerTest(String zookeeper, String groupId, String topic) {
		consumer = kafka.consumer.Consumer
				.createJavaConsumerConnector(createConsumerConfig(zookeeper,
						groupId));
		this.topic = topic;
	}

	private ConsumerConfig createConsumerConfig(String zookeeper, String groupId) {
		Properties properties = new Properties();
		properties.put("zookeeper.connect", zookeeper);
		properties.put("group.id", groupId);

		return new ConsumerConfig(properties);
	}

	public void run(int threadNum) {
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(topic, threadNum);
		Map<String, List<KafkaStream<byte[], byte[]>>> topic2Stream = consumer
				.createMessageStreams(topicCountMap);

		executorService = Executors.newFixedThreadPool(threadNum);
		List<KafkaStream<byte[], byte[]>> streamList = topic2Stream.get(topic);
		int threadId = 0;
		for (KafkaStream stream : streamList) {
			executorService.submit(new ConsumerTest(stream, threadId));
			threadId++;
		}
	}

	private void shutdown() {
		if (consumer != null) {
			consumer.shutdown();
		}
		if (executorService != null) {
			executorService.shutdown();
		}
	}

	public static void main(String[] args) {
		String zookeeper = args[0];
		String group = args[1];
		topic = args[2];
		threadNum = Integer.parseInt(args[3]);
		MyConsumerTest test = new MyConsumerTest(zookeeper, group, topic);
		test.run(threadNum);

		try {
			Thread.sleep(10000);
		} catch (InterruptedException ie) {

		}
		test.shutdown();
	}

}
