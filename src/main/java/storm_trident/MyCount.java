package storm_trident;

import storm.trident.operation.CombinerAggregator;
import storm.trident.tuple.TridentTuple;


/**
 * 对第一个tuple执行init
 * 对第二个tuple直到最后一个tuple，先执行init，再执行combine，将init的结果与上一次的结果进行合并
 * 如果没有tuple的时候，执行zero，返回值
 * @author ibm
 *
 */
public class MyCount implements CombinerAggregator<Long> {

	@Override
	public Long combine(Long value1, Long value2) {
		// TODO Auto-generated method stub
		System.out.println(value1+value2);
		return value1 + value2;
	}

	@Override
	public Long init(TridentTuple tuple) {
		// TODO Auto-generated method stub
		return 1l;
	}

	@Override
	public Long zero() {
		// TODO Auto-generated method stub
		return 0l;
	}

}
