
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.KeeperException;

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
			System.out.println(path + " : " + data);
			TimeUnit.SECONDS.sleep(r.nextInt(10));
		}
	}

	public static void main(String[] args) throws Exception {
		ConfigureUpdate update = new ConfigureUpdate(args[0]);
		update.run();
	}
}
