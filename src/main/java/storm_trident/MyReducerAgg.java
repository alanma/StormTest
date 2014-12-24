package storm_trident;

import storm.trident.operation.ReducerAggregator;
import storm.trident.tuple.TridentTuple;

public class MyReducerAgg implements ReducerAggregator<Long> {

	/* 在batch处理之前，调用init进行初始化，这个初始化的结果会传到reduce中去 */
	@Override
	public Long init() {
		// TODO Auto-generated method stub
		return 0l;
	}

	@Override
	public Long reduce(Long curr, TridentTuple tuple) {
		// TODO Auto-generated method stub
		return curr += 1;
	}

}
