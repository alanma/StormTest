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

	// �����tuple����������field list
	Fields outputFields;

	// �õ�����join fields
	Fields joinFields;

	// output fields�е�ÿ��fields�����Ե�stream
	Map<String, GlobalStreamId> field2Stream;

	// ����join fields value������stream�е�tuple
	Map<List<Object>, Map<GlobalStreamId, Tuple>> joinFields2Tuple;

	// ���bolt��������source�ĸ���
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

		// �����component��ÿһ��source���õ�join fields
		for (GlobalStreamId source : context.getThisSources().keySet()) {
			Fields tupleFields = context.getComponentOutputFields(
					source.get_componentId(), source.get_streamId());
			Set<String> tmpFields = new HashSet<String>(tupleFields.toList());
			if (joinFieldsSet == null) {
				joinFieldsSet = tmpFields;
			} else {
				joinFieldsSet.retainAll(tmpFields);
			}

			// �õ�ÿ��outputfields�����Ե�stream
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

		// �Ѿ�������join value����Ӧ��stream��Ҳ�������join value�ж��tuple,����Ϊ�Ǵ���
		if (stream2Tuple.containsKey(streamId)) {
			throw new RuntimeException(joinValuesList + " has been existed.");
		}
		stream2Tuple.put(streamId, input);

		// ���join value����Ӧ������source�������ˣ��Ϳ��Բ��������
		ArrayList<Object> values = new ArrayList<Object>();
		if (stream2Tuple.size() == numSources) {
			joinFields2Tuple.remove(joinValuesList);

			// �õ�����output fields��value�������
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

			// ��Ҫ������anchor��input����һ��ack����ʱ��XOR�Ľ�����ݸ�anchor task���Ա�֤��ȷ��
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
