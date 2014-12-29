package zookeeperTest;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class CreateGroup implements Watcher {
	private ZooKeeper zk;
	CountDownLatch connetedSignal = new CountDownLatch(1);

	public void connect(String hosts) throws IOException, InterruptedException {
		zk = new ZooKeeper(hosts, 5000, this);
		connetedSignal.await();
	}

	@Override
	public void process(WatchedEvent event) {
		if (event.getState() == KeeperState.SyncConnected) {
			connetedSignal.countDown();
		}
	}

	public void create(String groupName) throws KeeperException,
			InterruptedException {
		String pathString = "/" + groupName;
		String createPath = zk.create(pathString, null, Ids.OPEN_ACL_UNSAFE,
				CreateMode.PERSISTENT);
		System.out.println("created " + createPath);
	}
	
	public void close() throws InterruptedException{
		zk.close();
	}
	
	public static void main(String[] args) throws KeeperException, InterruptedException, IOException {
		CreateGroup createGroup = new CreateGroup();
		createGroup.connect(args[0]);
		createGroup.create(args[1]);
		createGroup.close();
	}
}
