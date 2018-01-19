package gigawatt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * BitcointaxIncomeConverter allows converting the CSV file exported from the 
 * Gigawatt dashboard into a CSV file that can be imported to the Income tab of
 * Bitcoin.tax
 * 
 * @author Elias Chavarria
 *
 */
public class BitcointaxConverter {

	static void
	convertToBitcointaxIncomeFormat(BufferedReader in, BufferedWriter outIncome,
			BufferedWriter outSpending, BufferedWriter outTrading) 
			throws Exception {
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
		
		// Create the output header for the Income file
		BitcointaxIncomeSpendingHeader btisHeader = new BitcointaxIncomeSpendingHeader();
		try {
			outIncome.write(btisHeader.toString());
			outIncome.newLine();
			outSpending.write(btisHeader.toString());
			outSpending.newLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Create the output header for the Spending file
		// FIXME: Spending
		
		// Parse the entries
		try {
			while ((line = in.readLine()) != null) {
				entry = new GwEntry();
				entry.parseEntry(line);
				
				switch (entry.getType()) {
				case gwtHosting:
					outSpending.write(entry.toBitcointaxIncomeSpendingEntry().toString());
					outSpending.newLine();
					break;
				case gwtReward:
					outIncome.write(entry.toBitcointaxIncomeSpendingEntry().toString());
					outIncome.newLine();
					break;
				case gwtWithdrawal:
					// FIXME: Pending
					break;
				case gwtWttRent:
					outIncome.write(entry.toBitcointaxIncomeSpendingEntry().toString());
					outIncome.newLine();
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Converts a CSV file exported from the Gigawatt dashboard into the CSV
	 * files that can be imported to Bitcoin.tax
	 *  
	 * @param args four arguments are need. The first one is the input
	 * CSV filename. The second, third, and fourth represent the CSV outputs 
	 * corresponding to the income CSV, spending CSV, and trading CSV, 
	 * respectively.
	 * Note that the timezone used in the Gigawatt CSV is in UTC.
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		File   inFile;
		String inputName;
		FileReader inFileReader = null;
		BufferedReader in = null;
		BufferedWriter outIncome = null;
		BufferedWriter outSpending = null;
		BufferedWriter outTrading = null;
		String outIncomeName;
		String outSpendingName;
		String outTradingName;
		
		switch (args.length) {
		case 4:
			inputName       = args[0];
			outIncomeName   = args[1];
			outSpendingName = args[2];
			outTradingName  = args[3];
			
			inFile = new File(inputName);
			try {
				inFileReader = new FileReader(inFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			in = new BufferedReader(inFileReader);
			
			try {
				outIncome   = new BufferedWriter(new FileWriter(outIncomeName));
				outSpending = new BufferedWriter(new FileWriter(outSpendingName));
				outTrading  = new BufferedWriter(new FileWriter(outTradingName));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			return;
		}
		
		try {
			convertToBitcointaxIncomeFormat(in, outIncome, outSpending, outTrading);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Close the files
		try {
			in.close();
			outIncome.close();
			outSpending.close();
			outTrading.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
