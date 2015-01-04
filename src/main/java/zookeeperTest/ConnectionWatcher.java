package zookeeperTest;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class ConnectionWatcher implements Watcher {

	CountDownLatch countDownLatch = new CountDownLatch(1);
	public ZooKeeper zk;

	/**
	 * 创建一个客户端到zookeeperhost的连接，并且保证，只有等完全连接上服务器之后，才
	 * 能将connect函数返回，这个实现是通过设置一个时间栅栏来实现的
	 * 
	 * @param hosts
	 * @throws Exception
	 */
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
