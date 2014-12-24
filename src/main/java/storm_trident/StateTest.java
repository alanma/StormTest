package storm_trident;

import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import storm.trident.TridentState;
import storm.trident.TridentTopology;
import storm.trident.testing.FixedBatchSpout;

public class StateTest {
	public static void main(String[] args) {
		FixedBatchSpout spout = new FixedBatchSpout(new Fields("sentence"), 3,
				new Values("the cow jumped over the moon"), new Values(
						"the man went to the store and bought some candy"),
				new Values("four score and seven years ago"), new Values(
						"how many apples can you eat"));

		TridentTopology topology = new TridentTopology();

		// TridentState locations = topology.newStaticState(new
		// LocationDBFactory());
		// topology.newStream("mystream", spout)
		// .stateQuery(locations, new Fields("userId"), new QueryLocation(),new
		// Fields("location"));

		//将tuple中的数据插入到state中去，可以在后面的查询中使用
		TridentState locationState = topology
				.newStream("mystream", spout)
				.partitionPersist(new LocationDBFactory(),
						new Fields("userId", "location"), new LocationUpdater());
		
		
	}
}
