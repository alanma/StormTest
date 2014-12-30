package ConfigurationZookeeper;

import java.util.Random;

import org.apache.zookeeper.KeeperException;

/**
 * 调用store中的set方法来实现不断地对配置属性的值进行修改； 这个修改操作会触发Watcher中的EventType.NodeDataChanged
 * 
 * @author wangjj
 * 
 *         Dec 30, 2014
 */
public class ConfigureUpdate {

	ConfigureStore store;
	public static String path = "/config";

	public ConfigureUpdate(String hosts) throws Exception {
		store = new ConfigureStore();
		store.connect(hosts);
	}

	public void run() throws KeeperException, InterruptedException, Exception {
		while (true) {
			Random r = new Random();
			String data = r.nextInt(100) + "";
			store.setConfigure(path, data);
		}
	}

	public static void main(String[] args) throws Exception {
		ConfigureUpdate update = new ConfigureUpdate(args[0]);
		update.run();
	}
}
