package kafkaSample;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

public class ConsumerTest implements Runnable {
	KafkaStream stream;
	int threadNum;

	public ConsumerTest(KafkaStream stream, int threadNumber) {
		this.stream = stream;
		this.threadNum = threadNumber;
	}

	@Override
	public void run() {
		ConsumerIterator<byte[], byte[]> it = stream.iterator();
		while (it.hasNext()) {
			System.out.println("Thread " + threadNum + " : "
					+ new String(it.next().message()));
		}
		System.out.println("Shutting down " + threadNum);
	}

}
