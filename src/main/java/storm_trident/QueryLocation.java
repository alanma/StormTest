package storm_trident;

import java.util.ArrayList;
import java.util.List;

import storm.trident.operation.TridentCollector;
import storm.trident.state.BaseQueryFunction;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;

public class QueryLocation extends BaseQueryFunction<LocationDB, String> {

	/**
	 * state即一个DB，对a list of inputs执行query并返回
	 */
	@Override
	public List<String> batchRetrieve(LocationDB state,
			List<TridentTuple> inputs) {
		List<String> retList = new ArrayList<String>();
		for (TridentTuple tuple : inputs) {
			retList.add(state.getLocation(tuple.getLong(0)));
		}
		return retList;
	}

	@Override
	public void execute(TridentTuple tuple, String location,
			TridentCollector collector) {
		collector.emit(new Values(location));
	}

}
