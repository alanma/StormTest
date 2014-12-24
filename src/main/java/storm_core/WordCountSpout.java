package storm_core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class WordCountSpout implements IRichSpout {
public static final Logger logger = Logger.getLogger(WordCountSpout.class);

	TopologyContext context;
	SpoutOutputCollector collector;
	FileReader fr;
	boolean isCompleted = false;

	private BufferedReader br;

	@Override
	public void ack(Object msgId) {
		// TODO Auto-generated method stub
		System.out.println("OK:" + msgId);
	}

	public void activate() {
		// TODO Auto-generated method stub

	}

	public void close() {
		// TODO Auto-generated method stub

	}

	public void deactivate() {
		// TODO Auto-generated method stub

	}

	public void fail(Object msgId) {
		// TODO Auto-generated method stub
		System.out.println("Failed:" + msgId);
	}

	/**
	 * nextTuple will running all the time, 所以当输入文件读入结束时需要return
	 * spout的执行流程：对每一个文件还是对每一个line执行nextTuple？
	 */
	public void nextTuple() {
		// TODO Auto-generated method stub

		

		String line;
		try {
			if ((line = br.readLine()) != null) {

				// emit一个新的tuple, tuple中个可以包含很多个fields，作为values的参数传入
				collector.emit(new Values(line), line);
			} 
		} catch (IOException e) {
			throw new RuntimeException("Error reading turple", e);
		} finally {
			isCompleted = true;
		}
	}

	/**
	 * 首先执行open
	 */
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		// TODO Auto-generated method stub

		try {
			this.context = context;
//			System.out.println("===================");
			logger.info("===================");
			logger.info(conf.get("wordsFile"));
			logger.info("===================");
//			System.out.println(conf.get("wordsFile"));
//			System.out.println("===================");
			fr = new FileReader(conf.get("wordsFile").toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.collector = collector;

		br = new BufferedReader(fr);

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("line"));
	}

	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
