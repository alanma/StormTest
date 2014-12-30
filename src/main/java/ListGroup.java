

import java.util.List;

import org.apache.zookeeper.KeeperException;

/**
 * 先建立一个从client到zk server的connection
 * 再根据path对znode进行增删改
 * @author ibm
 *
 */
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
