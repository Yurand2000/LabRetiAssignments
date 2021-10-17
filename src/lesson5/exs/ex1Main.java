package lesson5.exs;

import java.io.*;

public class ex1Main {

	public static void main(String[] args) {
		//File arg_file = new File(args[0]);
		File arg_file = new File(".");
		StringBuffer dirs = new StringBuffer();
		StringBuffer files = new StringBuffer();
		if(!arg_file.exists())
		{
			
			System.out.println("Il file dato non esiste.");
			return;
		}
		
		getDirectoryContents(arg_file, dirs, files);
		
		try(FileOutputStream dirs_file = new FileOutputStream("directories.txt");
			FileOutputStream files_file = new FileOutputStream("files.txt"))
		{
			dirs_file.write(dirs.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8));
			files_file.write(files.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static void getDirectoryContents(File file, StringBuffer directories, StringBuffer files)
	{
		if(file.isDirectory())
		{
			directories.append(file.getName() + '\n');
			for(File sub_file : file.listFiles())
			{
				getDirectoryContents(sub_file, directories, files);
			}
		}
		else if(file.isFile())
		{
			files.append(file.getName() + '\n');
		}
	}
}
