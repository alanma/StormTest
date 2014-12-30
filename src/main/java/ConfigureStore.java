

import java.nio.charset.Charset;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;


public class ConfigureStore extends ConnectionWatcher {
	public static Charset CHARSET = Charset.forName("UTF-8");

	public void setConfigure(String path, String data) throws KeeperException,
			InterruptedException, Exception {
		Stat stat = zk.exists(path, false);
		if (stat == null) {
			zk.create(path, data.getBytes(CHARSET), Ids.OPEN_ACL_UNSAFE,
					CreateMode.EPHEMERAL);
		} else {
			zk.setData(path, data.getBytes(CHARSET), -1);
		}
	}

	public String readConfigure(String path, Watcher wather)
			throws KeeperException, InterruptedException {

		// getdata方法会在watcher观测到节点的变化时（删除节点或者数值发生变化），调用process函数之后再调用这个函数，即回调函数
		byte[] data = zk.getData(path, wather, null);
		return new String(data, CHARSET);
	}
}
