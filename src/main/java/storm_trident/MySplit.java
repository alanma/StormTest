package storm_trident;

import java.util.Map;

import storm.trident.operation.Function;
import storm.trident.operation.TridentCollector;
import storm.trident.operation.TridentOperationContext;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;

public class MySplit implements Function {

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub

	}

	@Override
	public void prepare(Map arg0, TridentOperationContext arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void execute(TridentTuple sentence, TridentCollector collector) {
		String[] tokens = sentence.getString(0).split("[\\s]+");
		for (String word : tokens) {
			System.out.println(word);
			collector.emit(new Values(word));
		}

	}

}
