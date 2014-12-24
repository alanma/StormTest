package mavenArtifact;

import java.util.Properties;

import kafka.producer.ProducerConfig;

public class TestMavenJar {
	public static void main(String[] args) {
		testProperties();
	}

	/**
	 * 使用Properties类来设定属性的值，并且将Properties中设置的属性值用在配置文件中
	 */
	public static void testProperties() {
		Properties prop = new Properties();
		prop.put("meta", "value1");
		prop.put("prop2", 2);
		ProducerConfig conf = new ProducerConfig(prop);
		System.out.println(prop.get("prop1"));
	}
}
