package stormKafkaTest;

import storm.kafka.Broker;
import storm.kafka.StaticHosts;
import storm.kafka.ZkHosts;
import storm.kafka.trident.GlobalPartitionInformation;



public class StormKafkaTest {
	public static void main(String[] args) {
		
	}
	
	public  static StaticHosts getStaticHosts(){
		Broker broker4P0 = new Broker("localhost", 9092);
		Broker broker4P1 = new Broker("localhost:9092");
		Broker broker4P2 = new Broker("localhost");
		GlobalPartitionInformation info = new GlobalPartitionInformation();
		info.addPartition(0, broker4P0);
		info.addPartition(1, broker4P1);
		info.addPartition(2, broker4P2);
		StaticHosts hosts = new StaticHosts(info);
		return hosts;
	}
	
	public ZkHosts getDynamicHost(){
		ZkHosts hosts = new ZkHosts("localhost:9092");
		return hosts;
	}
}
