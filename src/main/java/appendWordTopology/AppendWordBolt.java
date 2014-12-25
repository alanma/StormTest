package appendWordTopology;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class AppendWordBolt extends BaseRichBolt {
	static Logger log = LoggerFactory.getLogger(AppendWordBolt.class);
	OutputCollector collector;
	StringBuffer sBuffer = new StringBuffer();

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		Fields fields = input.getFields();
		sBuffer.append("============");
		sBuffer.append("fields size is " + fields.size());
		sBuffer.append("============");
		System.out.println(sBuffer.toString());
		collector.emit(input, new Values(input.getString(0), input.getString(1)
				+ "~~", input.getString(2)));
//		collector.emit(input, new Values("one", "two"));
		collector.ack(input);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("output1", "output2", "output3"));
//		declarer.declare(new Fields("one", "two"));
	}

}
