package storm_trident;

import java.util.Map;

import storm.trident.operation.Filter;
import storm.trident.operation.TridentOperationContext;
import storm.trident.tuple.TridentTuple;

public class LengthFilter implements Filter {

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub

	}

	@Override
	public void prepare(Map arg0, TridentOperationContext arg1) {
		// TODO Auto-generated method stub
		System.out.println("In Lengthfilter");
	}

	@Override
	public boolean isKeep(TridentTuple sentence) {
		// TODO Auto-generated method stub
		String[] tokens = sentence.getString(0).split("[\\s]+");
		System.out.println(sentence.getString(0));
		return tokens.length >= 3;
	}

}
