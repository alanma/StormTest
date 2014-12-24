package storm_core;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

public class WordCountBolt implements IRichBolt{
	public static final Logger logger = Logger.getLogger(WordCountBolt.class);
	OutputCollector collector;
	HashMap<String, Integer> word2Count;
	Map conf;
	String res_String;
	/**
	 * 类似于hadoop中的startup函数，设置一些全局变量的值
	 */
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		// TODO Auto-generated method stub
		this.collector = collector;
		this.conf = stormConf;
		word2Count = new HashMap<String, Integer>();
		res_String = "";
	}

	/**
	 * map函数，都每一条tuple都执行一次
	 */
	public void execute(Tuple input) {
		// TODO Auto-generated method stub
		String word = input.getString(0);
		if(word2Count.containsKey(word)){
			word2Count.put(word, word2Count.get(word)+1);
		}else{
			word2Count.put(word, 1);
		}
		res_String = word2Count.toString();
		logger.info(res_String);
		collector.ack(input);
	}

	/**
	 * 等到所有的tuple都处理结束调用，hadoop中的cleanup
	 */
	public void cleanup() {
		// TODO Auto-generated method stub
		StringBuffer buffer = new StringBuffer();
		logger.info("================================");
		logger.info("Word counting result:");
		
//		System.out.println("================================");
//		System.out.println("Word counting result:");
		for(Map.Entry<String, Integer> entry:word2Count.entrySet()){
//			System.out.println(entry.getKey()+":"+entry.getValue());
			buffer.append(entry.getKey()+":"+entry.getValue()+"\n");
			logger.info(entry.getKey()+":"+entry.getValue());
		}
		logger.info("================================");
//		System.out.println("================================");
		try {
			throw new Exception(buffer.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		
	}

	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
