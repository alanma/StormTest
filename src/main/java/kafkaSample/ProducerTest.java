package kafkaSample;

import java.util.Date;
import java.util.Properties;
import java.util.Random;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

/**
 * 先设置producer的属性；再根据这些配置属性创建producer；再利用这个producer向broker发送message
 * 
 * @author wangjj
 * 
 *         Dec 31, 2014
 */
public class ProducerTest {
	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put("metadata.broker.list", "localhost:9092,localhost:9093");
		properties.put("serializer.class", "kafka.serializer.StringEncoder");
		properties.put("partitioner.class", "kafkaSample.MyPartitioner");

		ProducerConfig config = new ProducerConfig(properties);

		// 指定producer的key和message的类型
		Producer<String, String> producer = new Producer<String, String>(config);
		int msgNum = Integer.parseInt(args[0]);
		Random r = new Random();
		String ip;
		String message;
		for (int i = 0; i < msgNum; i++) {
			ip = "192.168.2." + r.nextInt(255);
			message = new Date().getTime() + ",www.baidu.com," + ip;

			// 指定message所属于的topic
			KeyedMessage<String, String> key2Msg = new KeyedMessage<String, String>(
					"visitMsg", ip, message);
			producer.send(key2Msg);
		}
	}

}
