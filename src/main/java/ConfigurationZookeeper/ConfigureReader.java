package ConfigurationZookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;

/**
 * 利用wather去观察属性值的变化情况，如果发生变化，则调用process，从而触发相关的处理事件
 * 
 * @author wangjj
 * 
 *         Dec 30, 2014
 */
public class ConfigureReader implements Watcher {
	ConfigureStore store;

	public ConfigureReader(String hosts) throws Exception {
		store = new ConfigureStore();
		store.connect(hosts);
	}

	public void display() throws KeeperException, InterruptedException {
		String value = store.readConfigure(ConfigureUpdate.path, this);
		System.out.println(ConfigureUpdate.path + " :" + value);
	}

	@Override
	public void process(WatchedEvent event) {
		if (event.getType() == EventType.NodeDataChanged) {
			try {
				display();
			} catch (KeeperException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		ConfigureReader reader = new ConfigureReader(args[0]);
		reader.display();
		Thread.sleep(Long.MAX_VALUE);
	}

}
