package labRetiAssignments.ex08;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class BankAccountGeneratorMain
{
	private static int generated_accounts_number = 5;
	private static int generated_movements_min = 2;
	private static int generated_movements_max = 5;
	private static int generated_movements_diff = generated_movements_max - generated_movements_min;
	
	public static void main(String[] args) throws IOException
	{
		if(args.length != 1)
			throw new RuntimeException();
		
		byte[] name_file_data = Files.readAllBytes(Paths.get(args[0] + "/names.json"));
		NameGenerator generator = new NameGenerator(name_file_data);

		List<BankAccount> random_accounts = generateRandomAccountList(generator);
		byte[] bank_account_data = SerializerWrapper.serialize(random_accounts);
		
		
	}
	
	private static List<BankAccount> generateRandomAccountList(NameGenerator generator)
	{
		List<BankAccount> random_accounts = new ArrayList<BankAccount>();
		for(int i = 0; i < generated_accounts_number; i++)
		{
			random_accounts.add(generateRandomAccount(generator));
		}
		return random_accounts;
	}
	
	private static BankAccount generateRandomAccount(NameGenerator generator)
	{
		List<BankMovement> random_movements = new ArrayList<BankMovement>();
		int movements_number = (int)((Math.random() * generated_movements_diff) + generated_movements_min);
		for(int j = 0; j < movements_number; j++)
		{
			random_movements.add(generateRandomMovement());
		}
		
		return new BankAccount(generator.getRandomName(), random_movements);
	}
	
	private static BankMovement generateRandomMovement()
	{
		return new BankMovement(
			getRandomReason(),
			getRandomPointInTime());
	}
	
	private static BankMovementReason getRandomReason()
	{
		BankMovementReason[] values = BankMovementReason.values();	
		
		BankMovementReason reason = null;
		do { reason = values[(int)(Math.random() * values.length)]; }
		while(reason == BankMovementReason.InvalidReason);
		return reason;
	}
	
	private static Instant getRandomPointInTime()
	{
		return Instant.now()
			.minus((long) (Math.random() * 1000), ChronoUnit.MILLIS)
			.minus((long) (Math.random() * 60), ChronoUnit.SECONDS)
			.minus((long) (Math.random() * 60), ChronoUnit.MINUTES)
			.minus((long) (Math.random() * 24), ChronoUnit.HOURS)
			.minus((long) (Math.random() * 31), ChronoUnit.DAYS)
			.minus((long) (Math.random() * 24) * 31, ChronoUnit.DAYS);
	}
}
