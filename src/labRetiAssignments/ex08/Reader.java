package labRetiAssignments.ex08;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Collections;

public class Reader
{
	private static int thread_pool_size = 10;
	private String bank_accounts_file;
	private Map<BankMovementReason, AtomicInteger> reasons_counters;
	
	public Reader(String bank_accounts_file)
	{
		this.bank_accounts_file = bank_accounts_file;
		reasons_counters = generateReadonlyReasonsCounterMap();
	}
	
	public void readBankAccounts() throws InterruptedException, IOException
	{
		byte[] accounts_data = Files.readAllBytes(Paths.get(bank_accounts_file));
		BankAccount[] accounts = SerializerWrapper.deserializeArray(accounts_data, BankAccount[].class);

		ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(thread_pool_size);
		for(BankAccount account : accounts)
		{
			pool.execute(new ReaderTask(account, reasons_counters));
		}
		
		pool.shutdown();		
		while(!pool.awaitTermination(1, TimeUnit.MINUTES));
	}
	
	public void printTotalReasons()
	{
		synchronized(reasons_counters)
		{
			for(Map.Entry<BankMovementReason, AtomicInteger> reason : reasons_counters.entrySet())
			{
				System.out.println(
					String.format("Reason: %-20s Count: %d",
						reason.getKey(), reason.getValue().get()) );
			}
		}
	}
	
	private Map<BankMovementReason, AtomicInteger> generateReadonlyReasonsCounterMap()
	{
		Map<BankMovementReason, AtomicInteger> reasons_map = new TreeMap<BankMovementReason, AtomicInteger>();		
		BankMovementReason[] reasons = BankMovementReason.values();
		for(int i = 0; i < reasons.length; i++)
		{
			reasons_map.put(reasons[i], new AtomicInteger(0));
		}
		
		return Collections.unmodifiableMap(reasons_map);
	}
}
