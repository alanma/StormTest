package storm_trident;

import java.util.Map;

import backtype.storm.task.IMetricsContext;
import storm.trident.state.State;
import storm.trident.state.StateFactory;

public class LocationDBFactory implements StateFactory {

	@Override
	public State makeState(Map arg0, IMetricsContext arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return new LocationDB();
	}

}
