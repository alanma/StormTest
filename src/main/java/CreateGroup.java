import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs.Ids;

/**
 * 先建立一个从client到zk server的connection 再根据path对znode进行增删改
 * 
 * @author wangjj
 * 
 * Dec 30, 2014
 */
public class CreateGroup extends ConnectionWatcher {

	public void create(String groupName) throws KeeperException,
			InterruptedException {
		String pathString = "/" + groupName;
		String createPath = zk.create(pathString, null, Ids.OPEN_ACL_UNSAFE,
				CreateMode.PERSISTENT);
		System.out.println("created " + createPath);
	}

	public void close() throws InterruptedException {
		zk.close();
	}

	public static void main(String[] args) throws Exception {
		CreateGroup createGroup = new CreateGroup();
		createGroup.connect(args[0]);
		createGroup.create(args[1]);
		createGroup.close();
	}
}
