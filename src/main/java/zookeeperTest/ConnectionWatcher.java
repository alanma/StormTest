package zookeeperTest;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class ConnectionWatcher implements Watcher {

	CountDownLatch countDownLatch = new CountDownLatch(1);
	public ZooKeeper zk;

	public void connect(String hosts) throws Exception {
		zk = new ZooKeeper(hosts, 5000, this);
		countDownLatch.await();
	}

	@Override
	public void process(WatchedEvent event) {
		if (event.getState() == KeeperState.SyncConnected) {
			countDownLatch.countDown();
		}
	}

	public void close() throws InterruptedException {
		zk.close();
	}

}
