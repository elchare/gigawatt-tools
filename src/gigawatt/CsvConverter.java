package gigawatt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * CsvConverter allows converting the CSV file exported from the Gigawatt
 * dashboard into a CSV file that is actually separated by commas and that
 * can be opened in Microsoft Excel.
 * 
 * @author Elias Chavarria
 *
 */
public class CsvConverter {

	private static void 
	convertSemicolonToComma(BufferedReader in, BufferedWriter out) {
		String line;
		int count = 0;
		
		try {
			while ((line = in.readLine()) != null) {
				line = line.replace(';', ',');
				
				if (count++ == 0) {
					line = line.replaceAll("ID", "\"ID\"");
				}
				out.write(line);
				out.newLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Converts a CSV file exported from the Gigawatt dashboard into a CSV file
	 * that is actually separated by commas and that can be opened in 
	 * Microsoft Excel.
	 *  
	 * @param args if no arguments is specified, the CSV file is read from 
	 * stdin and the output is sent to stdout. If a single argument is  
	 * provided, such argument should represent the path of the input CSV file;
	 * the output is sent to stdout. If two arguments are provided, the first 
	 * and second arguments should represent the path of the input and output
	 * CSV files, respectively. If more arguments are provided, nothing is
	 * done.  
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File   inFile;
		String inputName;
		FileReader inFileReader = null;
		BufferedReader in = null;
		BufferedWriter out = null;
		
		System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
		
		switch (args.length) {
		case 0:
			in = new BufferedReader(new InputStreamReader(System.in));
			out = new BufferedWriter(new OutputStreamWriter(System.out));
			
			break;
		case 1:
			inputName = args[0];
			try {
				inFileReader = new FileReader(inputName);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			in = new BufferedReader(inFileReader);
			
			out = new BufferedWriter(new OutputStreamWriter(System.out));
			break;
		case 2:
			String outputName;
			
			inputName  = args[0];
			outputName = args[1];
			
			inFile = new File(inputName);
			try {
				inFileReader = new FileReader(inFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			in = new BufferedReader(inFileReader);
			
			try {
				out = new BufferedWriter(new FileWriter(outputName));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			return;
		}
		
		// Do conversion
		convertSemicolonToComma(in,out);
		
		// Close the files
		try {
			in.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
