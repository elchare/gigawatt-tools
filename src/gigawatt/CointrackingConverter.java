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
 * CointrackingConverter allows converting the CSV file exported from the 
 * Gigawatt dashboard into a CSV file that can be imported to
 * Cointracking.info
 * 
 * @author Elias Chavarria
 *
 */
public class CointrackingConverter {
	
	static void
	convertToCointrackingFormat(BufferedReader in, BufferedWriter out) throws Exception {
		String line = null;
		GwEntry entry;
		
		// Parse the header
		try {
			line = in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (line == null) return;
		
		GwHeader.parseHeader(line);
		
		// Create the output header
		CointrackingHeader ctHeader = new CointrackingHeader();
		try {
			out.write(ctHeader.toString());
			out.newLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Parse the entries
		try {
			while ((line = in.readLine()) != null) {
				entry = new GwEntry();
				entry.parseEntry(line);
				out.write(entry.toCointrackingEntry().toString());
				out.newLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Converts a CSV file exported from the Gigawatt dashboard into a CSV file
	 * that can be imported to Cointracking.info
	 *  
	 * @param args if no argument is specified, the CSV file is read from 
	 * stdin and the output is sent to stdout. If a single argument is  
	 * provided, such argument should represent the path of the input CSV file;
	 * the output is sent to stdout. If two arguments are provided, the first 
	 * and second arguments should represent the path of the input and output
	 * CSV files, respectively. If more arguments are provided, nothing is
	 * done. 
	 * Note that the timezone used in the Gigawatt CSV is in UTC. The converter
	 * takes this field and transforms it to the corresponding timezone of your
	 * system. The converted value is the one written in the output CSV. If your
	 * cointracking.info account is also using your system timezone, the
	 * aforementioned approach should be correct. Otherwise, adjust 
	 * accordingly (see function convertToCtTimezone in 
	 * CointrackingEntry.java). 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		File   inFile;
		String inputName;
		FileReader inFileReader = null;
		BufferedReader in = null;
		BufferedWriter out = null;
		
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
		try {
			convertToCointrackingFormat(in,out);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
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
