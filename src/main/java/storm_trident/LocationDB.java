package storm_trident;

import storm.trident.state.State;

public class LocationDB implements State {

	@Override
	public void beginCommit(Long arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void commit(Long arg0) {
		// TODO Auto-generated method stub

	}

	public void setLocation(long userId, String location) {
		// TODO
	}

	public String getLocation(long userId) {
		// TODO
		return null;
	}
}
