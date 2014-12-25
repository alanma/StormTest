package SimpleJointTopology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import backtype.storm.generated.GlobalStreamId;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

public class SingleJoinBolt extends BaseRichBolt {

	// 输出的tuple中所包含的field list
	Fields outputFields;

	// 得到所有join fields
	Fields joinFields;

	// output fields中的每个fields所来自的stream
	Map<String, GlobalStreamId> field2Stream;

	// 包含join fields value的所有stream中的tuple
	Map<List<Object>, Map<GlobalStreamId, Tuple>> joinFields2Tuple;

	// 这个bolt所包含的source的个数
	int numSources;

	OutputCollector collector;
	HashSet<String> results = new HashSet<String>();

	public SingleJoinBolt(Fields fields) {
		outputFields = fields;
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
		field2Stream = new HashMap<String, GlobalStreamId>();
		joinFields2Tuple = new HashMap<List<Object>, Map<GlobalStreamId, Tuple>>();
		numSources = context.getThisSources().size();
		Set<String> joinFieldsSet = null;

		// 对这个component的每一个source，得到join fields
		for (GlobalStreamId source : context.getThisSources().keySet()) {
			Fields tupleFields = context.getComponentOutputFields(
					source.get_componentId(), source.get_streamId());
			Set<String> tmpFields = new HashSet<String>(tupleFields.toList());
			if (joinFieldsSet == null) {
				joinFieldsSet = tmpFields;
			} else {
				joinFieldsSet.retainAll(tmpFields);
			}

			// 得到每个outputfields所来自的stream
			for (String outFieldsString : outputFields) {
				for (String sourceField : tmpFields) {
					if (outFieldsString.equals(sourceField)) {
						field2Stream.put(outFieldsString, source);
					}
				}
			}
		}

		joinFields = new Fields(new ArrayList<String>(joinFieldsSet));

	}

	@Override
	public void execute(Tuple input) {
		List<Object> joinValuesList = input.select(joinFields);
		GlobalStreamId streamId = input.getSourceGlobalStreamid();
		if (joinFields2Tuple.get(joinValuesList) == null) {
			joinFields2Tuple.put(joinValuesList,
					new HashMap<GlobalStreamId, Tuple>());
		}
		Map<GlobalStreamId, Tuple> stream2Tuple = joinFields2Tuple
				.get(joinValuesList);

		// 已经存在了join value所对应的stream，也就是这个join value有多个tuple,则认为是错误
		if (stream2Tuple.containsKey(streamId)) {
			throw new RuntimeException(joinValuesList + " has been existed.");
		}
		stream2Tuple.put(streamId, input);

		// 这个join value所对应的所有source都到齐了，就可以产生输出了
		ArrayList<Object> values = new ArrayList<Object>();
		if (stream2Tuple.size() == numSources) {
			joinFields2Tuple.remove(joinValuesList);

			// 得到所有output fields的value，并输出
			String tmpString = "";
			for (String fieldsName : outputFields) {
				GlobalStreamId sourceStreamId = field2Stream.get(fieldsName);
				values.add(stream2Tuple.get(sourceStreamId).getValueByField(
						fieldsName));
				tmpString += stream2Tuple.get(sourceStreamId).getValueByField(
						fieldsName)
						+ "\t";
			}
			collector.emit(new ArrayList<Tuple>(stream2Tuple.values()), values);
			results.add(tmpString);

			// 需要对所有anchor的input产生一个ack，此时将XOR的结果传递给anchor task，以保证正确性
			for (Tuple sourceTuple : stream2Tuple.values()) {
				collector.ack(sourceTuple);
			}
		}
	}

	@Override
	public void cleanup() {
		System.out.println("=====================");
		for (String rst : results) {
			System.out.println(rst);
		}
		System.out.println("=====================");
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(outputFields);
	}
}
