package labRetiAssignments.ex08;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

public class BankAccountGenerator
{
	private NameGenerator name_generator;
	private int accounts_number;
	private int movements_min;
	private int movements_diff;
	
	public BankAccountGenerator(NameGenerator name_generator, int accounts_number, int movements_min, int movements_max)
	{
		if(movements_min > movements_max)
		{
			throw new IllegalArgumentException();
		}
		
		this.name_generator = name_generator;
		this.accounts_number = accounts_number;
		this.movements_min = movements_min;
		this.movements_diff = movements_max - movements_min;
	}
	
	public byte[] generateAndSerialize() throws JsonProcessingException
	{
		return SerializerWrapper.serialize(generateRandomAccountList());
	}
	
	private List<BankAccount> generateRandomAccountList()
	{
		List<BankAccount> random_accounts = new ArrayList<BankAccount>();
		for(int i = 0; i < accounts_number; i++)
		{
			random_accounts.add(generateRandomAccount());
		}
		return random_accounts;
	}
	
	private BankAccount generateRandomAccount()
	{
		List<BankMovement> random_movements = new ArrayList<BankMovement>();
		int movements_number = (int)((Math.random() * movements_diff) + movements_min);
		for(int j = 0; j < movements_number; j++)
		{
			random_movements.add(generateRandomMovement());
		}
		
		return new BankAccount(name_generator.getRandomName(), random_movements);
	}
	
	private BankMovement generateRandomMovement()
	{
		return new BankMovement(
			getRandomReason(),
			getRandomPointInTime());
	}
	
	private BankMovementReason getRandomReason()
	{
		BankMovementReason[] values = BankMovementReason.values();	
		
		BankMovementReason reason = null;
		do { reason = values[(int)(Math.random() * values.length)]; }
		while(reason == BankMovementReason.InvalidReason);
		return reason;
	}
	
	private Instant getRandomPointInTime()
	{
		//random point in time between now and two years ago circa.
		return Instant.now()
			.minus((long) (Math.random() * 1000), ChronoUnit.MILLIS)
			.minus((long) (Math.random() * 60), ChronoUnit.SECONDS)
			.minus((long) (Math.random() * 60), ChronoUnit.MINUTES)
			.minus((long) (Math.random() * 24), ChronoUnit.HOURS)
			.minus((long) (Math.random() * 31), ChronoUnit.DAYS)
			.minus((long) (Math.random() * 24) * 31, ChronoUnit.DAYS);
	}
}
