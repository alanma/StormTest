package mavenArtifact;

import java.util.Properties;

import kafka.producer.ProducerConfig;

public class TestMavenJar {
	public static void main(String[] args) {
		testProperties();
	}

	/**
	 * ʹ��Properties�����趨���Ե�ֵ�����ҽ�Properties�����õ�����ֵ���������ļ���
	 */
	public static void testProperties() {
		Properties prop = new Properties();
		prop.put("meta", "value1");
		prop.put("prop2", 2);
		ProducerConfig conf = new ProducerConfig(prop);
		System.out.println(prop.get("prop1"));
	}
}
