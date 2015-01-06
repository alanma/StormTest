package stormKafka;

import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;

public class MySentenceSplit extends BaseFunction {

	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		String[] words = tuple.getString(0).split("[\\s]+");
		for (String word : words) {
			collector.emit(new Values(word));
		}

	}

}
