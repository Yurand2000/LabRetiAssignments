package labRetiAssignments.ex08;

import java.io.File;
import java.io.IOException;

public class MainClass
{
	private static String bank_account_file_name = "bankAccounts.json";
	
	public static void main(String[] args) throws IOException, InterruptedException
	{
		if(!areArgumentsValid(args))
		{
			return;
		}
		
		Reader reader = new Reader(getBankAccountFilePath(args));
		reader.readBankAccounts();
		reader.printTotalReasons();
	}
	
	private static boolean areArgumentsValid(String[] args)
	{
		if(args.length != 1)
		{
			System.out.println("Expected only one argument: working directory.");
			return false;
		}

		File directory = new File(args[0]);
		if(!directory.exists())
		{
			System.out.println("Given directory does not exist.");
			return false;
		}
		if(!directory.isDirectory())
		{
			System.out.println("Given path is not a directory.");
			return false;
		}
		
		File file = new File(getBankAccountFilePath(args));
		if(!file.exists())
		{
			System.out.println("Bank account file: \"" + bank_account_file_name + "\" does not exist.");
			return false;
		}
		
		return true;
	}
	
	private static String getBankAccountFilePath(String[] args)
	{
		return args[0] + '/' + bank_account_file_name;
	}
}
