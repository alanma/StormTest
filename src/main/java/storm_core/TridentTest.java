package storm_core;

import storm.trident.TridentState;
import storm.trident.TridentTopology;
import storm.trident.operation.builtin.Count;
import storm.trident.operation.builtin.MapGet;
import storm.trident.testing.FixedBatchSpout;
import storm.trident.testing.MemoryMapState;
import storm.trident.testing.Split;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.DRPCClient;

public class TridentTest {
	static TridentState state;
	public static void main(String[] args) {
		@SuppressWarnings("unchecked")
		FixedBatchSpout spout = new FixedBatchSpout(new Fields("sentence"), 3,
				new Values("the cow jumped over the moon"), new Values(
						"the man went to the store and bought some candy"),
				new Values("four score and seven years ago"), new Values(
						"how many apples can you eat"));
		TridentTopology topology = new TridentTopology();
		state = topology
				.newStream("spout1", spout)
				.each(new Fields("sentence"), new Split(), new Fields("words"))
				.groupBy(new Fields("words"))
				.persistentAggregate(new MemoryMapState.Factory(), new Count(),
						new Fields("count")).parallelismHint(6);
		DRPCClient client = new DRPCClient("cdckvm253", 3772);

	}

	public static void reachTopology() {
		TridentState url2Tweeters = state;
		TridentState tweeter2Followers = state;
		
	}
}
