package storm_trident;

import storm.trident.operation.BaseAggregator;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;
import storm_trident.MyAggregator.CountState;
import backtype.storm.tuple.Values;

/**
 * 实现自定义的BaseAggregator类
 * 
 * @author ibm
 *
 */
public class MyAggregator extends BaseAggregator<CountState> {

	/**
	 * 将需要保存的数据结果存放在State类中，保存State的方法要比Combiner, Reducer的方法更加general
	 * @author ibm
	 *
	 */
	static class CountState {
		int count = 0;
	}

	@Override
	public void aggregate(CountState state, TridentTuple tuple,
			TridentCollector collector) {
		state.count++;
	}

	@Override
	public void complete(CountState state, TridentCollector collector) {
		collector.emit(new Values(state.count));
	}

	@Override
	public CountState init(Object arg0, TridentCollector collector) {
		return new CountState();
	}

}
