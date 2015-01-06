package stormKafka;

import java.io.UnsupportedEncodingException;
import java.util.List;

import backtype.storm.spout.Scheme;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class MyTestScheme implements Scheme {

	@Override
	public Fields getOutputFields() {
		return new Fields("key", "message");
	}

	@Override
	public List<Object> deserialize(byte[] ser) {
		String value = "";
		try {
			value = new String(ser, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Values("key", value);
	}
}
