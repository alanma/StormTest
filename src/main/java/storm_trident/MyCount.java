package storm_trident;

import storm.trident.operation.CombinerAggregator;
import storm.trident.tuple.TridentTuple;


/**
 * �Ե�һ��tupleִ��init
 * �Եڶ���tupleֱ�����һ��tuple����ִ��init����ִ��combine����init�Ľ������һ�εĽ�����кϲ�
 * ���û��tuple��ʱ��ִ��zero������ֵ
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
