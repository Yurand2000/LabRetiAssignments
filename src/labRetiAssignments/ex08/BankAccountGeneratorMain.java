package labRetiAssignments.ex08;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.StandardOpenOption;
import java.nio.file.Paths;

public class BankAccountGeneratorMain
{
	private static int accounts_number = 100;
	private static int movements_min = 10;
	private static int movements_max = 250;
	private static String bank_account_file_name = "bankAccounts.json";
	
	public static void main(String[] args) throws IOException
	{
		if(!areArgumentsValid(args))
		{
			return;
		}		
		
		String bank_account_file_path = getBankAccountFilePath(args);
		NameGenerator name_generator = new NameGenerator(getNamesFilePath(args));		
		BankAccountGenerator generator = new BankAccountGenerator(name_generator, accounts_number, movements_min, movements_max);		
		writeDataToNewFile(bank_account_file_path, generator.generateAndSerialize());
	}
	
	private static void writeDataToNewFile(String path, byte[] data) throws IOException
	{
		try
		{
			FileChannel file = FileChannel.open(Paths.get(path), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
			ByteBuffer buffer = ByteBuffer.wrap(data);
			while(buffer.hasRemaining())
			{
				file.write(buffer);
			}
			file.close();

			System.out.println("Successfully generated the " + bank_account_file_name + " file.");
		}
		catch(FileAlreadyExistsException e)
		{
			e.printStackTrace();
		}
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
		
		File names_file = new File(getNamesFilePath(args));
		if(!names_file.exists())
		{
			System.out.println("Default names file does not exist. The names file name is \"names.json\".");
			return false;
		}
		
		File file = new File(getBankAccountFilePath(args));
		if(file.exists())
		{
			System.out.println("Can't generate the " + bank_account_file_name + " file, file already exists.");
			System.out.println("To overwrite first delete the file manually then run the program again.");
			return false;
		}
		
		return true;
	}
	
	private static String getNamesFilePath(String[] args)
	{
		return args[0] + "/names.json";
	}
	
	private static String getBankAccountFilePath(String[] args)
	{
		return args[0] + '/' + bank_account_file_name;
	}
}
