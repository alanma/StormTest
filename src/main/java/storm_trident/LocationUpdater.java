package storm_trident;

import java.util.List;

import storm.trident.operation.TridentCollector;
import storm.trident.state.BaseStateUpdater;
import storm.trident.tuple.TridentTuple;

public class LocationUpdater extends BaseStateUpdater<LocationDB>{

	//如果通过collector收集新生成的tuple，则会生成new values stream
	@Override
	public void updateState(LocationDB state, List<TridentTuple> tuples,
			TridentCollector collector) {
		for(TridentTuple tuple:tuples){
			state.setLocation(tuple.getLong(0), tuple.getString(1));
		}
	}

}
