package labRetiAssignments.ex08;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainClass {

	public static void main(String[] args) throws IOException, InterruptedException
	{
		if(args.length != 1)
			throw new RuntimeException();
		
		byte[] accounts_data = Files.readAllBytes(Paths.get(args[0] + "/bankAccounts.json"));
		BankAccount[] accounts = SerializerWrapper.deserialize(accounts_data, BankAccount[].class);

		Map<BankMovementReason, Integer> totalReasons = Collections.synchronizedMap(new TreeMap<BankMovementReason, Integer>());
		ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
		for(BankAccount account : accounts)
		{
			pool.execute(new BankAccountTask(account, totalReasons));
		}
		
		pool.shutdown();		
		pool.awaitTermination(10000, TimeUnit.DAYS);
		
		System.out.println(totalReasons);
	}
}
