package appendWordTopology;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import backtype.storm.Config;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class AppendWordSpout extends BaseRichSpout {
	SpoutOutputCollector collector;
	boolean isDistributed;

	public AppendWordSpout() {
		this(true);
	}

	public AppendWordSpout(boolean isDistributed) {
		this.isDistributed = isDistributed;
	}

	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void nextTuple() {
		final String[] words = { "word1", "word2", "word3", "word4" };
		final Random random = new Random();
		final String word = words[random.nextInt(words.length)];
		collector.emit(new Values(word+"!",word+"!!",word+"!!!"));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("field1","field2","field3"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		Map<String, Object> confMap = new HashMap<String, Object>();
		if (!isDistributed) {
			confMap.put(Config.TOPOLOGY_MAX_TASK_PARALLELISM, 1);
			return confMap;
		} else {
			return null;
		}
	}

}
