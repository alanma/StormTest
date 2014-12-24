package storm_trident;

import storm.trident.operation.BaseAggregator;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;
import storm_trident.MyAggregator.CountState;
import backtype.storm.tuple.Values;

/**
 * ʵ���Զ����BaseAggregator��
 * 
 * @author ibm
 *
 */
public class MyAggregator extends BaseAggregator<CountState> {

	/**
	 * ����Ҫ��������ݽ�������State���У�����State�ķ���Ҫ��Combiner, Reducer�ķ�������general
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
