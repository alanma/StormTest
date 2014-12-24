package DRPCTopology;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.drpc.LinearDRPCTopologyBuilder;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class DRPCTest {
	public static class RecallBolt extends BaseBasicBolt {

		@Override
		public void execute(Tuple input, BasicOutputCollector collector) {
			String result = input.getString(1);
			collector.emit(new Values(input.getValue(0), result + "!"));
		}

		@Override
		public void declareOutputFields(OutputFieldsDeclarer declarer) {
			declarer.declare(new Fields("id", "result"));
		}
	}

	public static void main(String[] args) {

		// ����һ������rpc��topology�����ӷ����������ؽ���Ȳ���������builder����ɵ�
		// append������ǿͻ��˵ĺ��������������һ��topology�а������function�������
		LinearDRPCTopologyBuilder builder = new LinearDRPCTopologyBuilder(
				"append");
		builder.addBolt(new RecallBolt(), 3);

		Config conf = new Config();
		conf.setDebug(true);

		// ����һ��DRPC��server
		LocalDRPC drpc = new LocalDRPC();
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("drpc-test", conf,
				builder.createLocalTopology(drpc));
		System.out.println("Result for 'hello'"
				+ drpc.execute("append", "hello"));
		cluster.shutdown();
		drpc.shutdown();
	}
}
