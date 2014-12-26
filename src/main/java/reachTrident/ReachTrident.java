package reachTrident;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import storm.trident.TridentState;
import storm.trident.TridentTopology;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.CombinerAggregator;
import storm.trident.operation.TridentCollector;
import storm.trident.operation.builtin.MapGet;
import storm.trident.operation.builtin.Sum;
import storm.trident.state.ReadOnlyState;
import storm.trident.state.State;
import storm.trident.state.StateFactory;
import storm.trident.state.map.ReadOnlyMapState;
import storm.trident.tuple.TridentTuple;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.generated.StormTopology;
import backtype.storm.task.IMetricsContext;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class ReachTrident {
	public static Map<String, List<String>> TWEETERS_DB = new HashMap<String, List<String>>() {
		{
			put("foo.com/blog/1",
					Arrays.asList("sally", "bob", "tim", "george", "nathan"));
			put("engineering.twitter.com/blog/5",
					Arrays.asList("adam", "david", "sally", "nathan"));
			put("tech.backtype.com/blog/123",
					Arrays.asList("tim", "mike", "john"));
		}
	};

	public static Map<String, List<String>> FOLLOWERS_DB = new HashMap<String, List<String>>() {
		{
			put("sally", Arrays.asList("bob", "tim", "alice", "adam", "jim",
					"chris", "jai"));
			put("bob", Arrays.asList("sally", "nathan", "jim", "mary", "david",
					"vivian"));
			put("tim", Arrays.asList("alex"));
			put("nathan", Arrays.asList("sally", "bob", "adam", "harry",
					"chris", "vivian", "emily", "jordan"));
			put("adam", Arrays.asList("david", "carissa"));
			put("mike", Arrays.asList("john", "bob"));
			put("john", Arrays.asList("alice", "nathan", "jim", "mike", "bob"));
		}
	};

	public static class MyStaticState extends ReadOnlyState implements
			ReadOnlyMapState<Object> {

		public static class SingleKeymapFactory implements StateFactory {

			Map map;

			public SingleKeymapFactory(Map map) {
				this.map = map;
			}

			@Override
			public State makeState(Map conf, IMetricsContext metrics,
					int partitionIndex, int numPartitions) {
				return new MyStaticState(map);
			}

		}

		Map map;

		public MyStaticState(Map map) {
			this.map = map;
		}

		// 此时的List<List<Object>>中包含的是多个key，而每个key的形式是List<Object>，这是grouping
		// fields，而此时的key只有一个元素，也就是第一个元素

		// 0 key value is foo.com/blog/1
		// list value is [foo.com/blog/1]
		@Override
		public List<Object> multiGet(List<List<Object>> keys) {
			List<Object> retList = new ArrayList<Object>();
			for (List<Object> key : keys) {
				retList.add(map.get(key.get(0)));
				System.out.println("0 key value is " + key.get(0));
				System.out.println("list value is " + key);
			}
			return retList;
		}

	}

	/**
	 * 此时出入的是一个list
	 * 
	 * @author ibm
	 * 
	 */
	public static class ExpandList extends BaseFunction {

		@Override
		public void execute(TridentTuple tuple, TridentCollector collector) {
			// 此时的tuple的形式是new Values(list)，即只有一个field，这个field的类型是list。
			// 如果使用tuple.get(0)，则返回的是第1个元素的值，如果使用getValue(0),那么就会返回field1
			// 所对应的value的值
			List<Object> list = (List<Object>) tuple.getValue(0);
			if (list != null) {
				for (Object o : list) {
					System.out.println("Person is :" + o);
					collector.emit(new Values(o));
				}
			}
		}

	}

	public static class CombineOne implements CombinerAggregator<Integer> {

		@Override
		public Integer init(TridentTuple tuple) {
			return 1;
		}

		@Override
		public Integer combine(Integer val1, Integer val2) {
			return 1;
		}

		@Override
		public Integer zero() {
			return 1;
		}

	}

	public static StormTopology builde(LocalDRPC drpc) {
		TridentTopology topology = new TridentTopology();
		TridentState url2Tweeters = topology
				.newStaticState(new MyStaticState.SingleKeymapFactory(
						TWEETERS_DB));
		TridentState tweeter2Followers = topology
				.newStaticState(new MyStaticState.SingleKeymapFactory(
						FOLLOWERS_DB));
		topology.newDRPCStream("reach", drpc)
				.stateQuery(url2Tweeters, new Fields("args"), new MapGet(),
						new Fields("tweeters"))
				.each(new Fields("tweeters"), new ExpandList(),
						new Fields("tweeter"))
				.shuffle()
				.stateQuery(tweeter2Followers, new Fields("tweeter"),
						new MapGet(), new Fields("followers"))
				.each(new Fields("followers"), new ExpandList(),
						new Fields("follower"))
				.groupBy(new Fields("follower"))
				.aggregate(new Fields("follower"), new CombineOne(),
						new Fields("one"))
				.aggregate(new Fields("one"), new Sum(), new Fields("sum"));
		return topology.build();
	}

	public static void main(String[] args) {
		LocalDRPC drpc = new LocalDRPC();
		LocalCluster cluster = new LocalCluster();
		Config conf = new Config();
		cluster.submitTopology("reachTopology", conf, builde(drpc));
		System.out.println("Result for foo.com/blog/1 is :"
				+ drpc.execute("reach", "foo.com/blog/1"));
		System.out
				.println("Result for aaa is :" + drpc.execute("reach", "aaa"));
		cluster.shutdown();
		drpc.shutdown();
	}
}
