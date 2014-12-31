package kafkaSample;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class MyPartitioner implements Partitioner {
	public MyPartitioner(VerifiableProperties props) {

	}

	@Override
	public int partition(Object key, int numPartition) {
		int partId = 0;
		String stringKey = (String) key;
		int lastIndex = stringKey.lastIndexOf(".");
		if (lastIndex > 0) {
			partId = Integer.parseInt(stringKey.substring(lastIndex + 1))
					% numPartition;
		}
		return partId;
	}

}
