package zookeeperTest;

import java.util.List;

import org.apache.zookeeper.KeeperException;

public class ListGroup extends ConnectionWatcher {
	public void list(String groupName) throws KeeperException,
			InterruptedException {
		String path = "/" + groupName;
		List<String> childrean = zk.getChildren(path, false);
		if (childrean.isEmpty()) {
			System.out.println("No members in group in " + path);
		} else {
			for (String child : childrean) {
				System.out.println("child : " + child);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		ListGroup listGroup = new ListGroup();
		listGroup.connect(args[0]);
		listGroup.list(args[1]);
		listGroup.close();
	}
}
