package storm_trident;

import storm.trident.operation.ReducerAggregator;
import storm.trident.tuple.TridentTuple;

public class MyReducerAgg implements ReducerAggregator<Long> {

	/* ��batch����֮ǰ������init���г�ʼ���������ʼ���Ľ���ᴫ��reduce��ȥ */
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
