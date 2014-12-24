package storm_trident;

import java.util.Map;

import storm.trident.operation.Aggregator;
import storm.trident.operation.TridentCollector;
import storm.trident.operation.TridentOperationContext;
import storm.trident.tuple.TridentTuple;

public class MyAggregation implements Aggregator {

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub

	}

	@Override
	public void prepare(Map arg0, TridentOperationContext arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void aggregate(Object arg0, TridentTuple arg1, TridentCollector arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void complete(Object arg0, TridentCollector arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object init(Object arg0, TridentCollector arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}
