package ConfigurationZookeeper;

import java.util.Random;

import org.apache.zookeeper.KeeperException;

/**
 * ����store�е�set������ʵ�ֲ��ϵض��������Ե�ֵ�����޸ģ� ����޸Ĳ����ᴥ��Watcher�е�EventType.NodeDataChanged
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
