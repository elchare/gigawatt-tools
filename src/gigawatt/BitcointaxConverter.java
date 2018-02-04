package gigawatt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * BitcointaxIncomeConverter allows converting the CSV file exported from the 
 * Gigawatt dashboard into a CSV files that can be imported to the income and
 * spending tabs of Bitcoin.tax
 * 
 * @author Elias Chavarria
 *
 */
public class BitcointaxConverter {
	private static boolean aggHostingFee = false;
	private static BigDecimal totalHostingFee;
	private static GwEntry lastHostingEntry;
	
	static void
	setHostFeeAggrMode(boolean mode) {
		aggHostingFee = mode;
	}

	static void
	convertToBitcointaxIncomeFormat(BufferedReader in, BufferedWriter outIncome,
			BufferedWriter outSpending) 
			throws Exception {
		String line = null;
		GwEntry entry;
		
		if (aggHostingFee) {
			totalHostingFee = new BigDecimal(0);
		}
		
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
		
		try {
			while ((line = in.readLine()) != null) {
				entry = new GwEntry();
				entry.parseEntry(line);
				
				if (aggHostingFee && entry.getType() == GwTranType.gwtHosting) {
					totalHostingFee = totalHostingFee.add(entry.getAmount());
					lastHostingEntry = entry;
					continue;
				}
				
				BitcointaxIncomeSpendingEntry btEntry = entry.toBitcointaxIncomeSpendingEntry();
				
				// Experimental: Adjust the mining amount to account for mining
				// fees
				if (aggHostingFee && entry.getType() == GwTranType.gwtReward ) {
					BigDecimal rewardAmnt = btEntry.getVolume();
					
					if (rewardAmnt.compareTo(totalHostingFee.abs()) > 0) {
						// The mined amount can offset the hosting fees

						// Set the fees
						if (totalHostingFee.compareTo(new BigDecimal(0)) != 0) {
							btEntry.setFee(totalHostingFee.abs());
							btEntry.setFeeCurrency(btEntry.getSymbol());
						}
					
						// Adjust the buy amount
						rewardAmnt = rewardAmnt.add(totalHostingFee);
						btEntry.setVolume(rewardAmnt);
						
						// Reset the hosting fees
						totalHostingFee = new BigDecimal(0);
					} else {
						// The mined amount cannot fully offset the hosting costs
						totalHostingFee = totalHostingFee.add(rewardAmnt);
						// Do not write any entry
						continue;
					}
				}
				
				switch (entry.getType()) {
				case gwtHosting:
					outSpending.write(btEntry.toString());
					outSpending.newLine();
					break;
				case gwtReward:
					outIncome.write(btEntry.toString());
					outIncome.newLine();
					break;
				case gwtWithdrawal:
					outSpending.write(btEntry.toString());
					outSpending.newLine();
					outSpending.write(btEntry.toTransferFeeString());
					outSpending.newLine();
					break;
				case gwtWttRent:
					outIncome.write(btEntry.toString());
					outIncome.newLine();
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// If we are aggregating the hosting fees, and there are hosting fees  
		// that have not been matched to a mining reward, add them as a
		// spending entry
		if (aggHostingFee && totalHostingFee.compareTo(new BigDecimal(0)) < 0) {
			BitcointaxIncomeSpendingEntry btEntry = lastHostingEntry.toBitcointaxIncomeSpendingEntry();
			btEntry.setVolume(totalHostingFee.abs());
			
			outSpending.write(btEntry.toString());
			outSpending.newLine();
		}
	}
	
	/**
	 * Converts a CSV file exported from the Gigawatt dashboard into the CSV
	 * files that can be imported to Bitcoin.tax
	 *  
	 * @param args three arguments are need. The first one is the input
	 * CSV filename. The second and third represent the CSV outputs 
	 * corresponding to the income and spending CSVs, respectively.
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
		String outIncomeName;
		String outSpendingName;
		
		switch (args.length) {
		case 3:
			inputName       = args[0];
			outIncomeName   = args[1];
			outSpendingName = args[2];
			
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default:
			return;
		}
		
		try {
			convertToBitcointaxIncomeFormat(in, outIncome, outSpending);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Close the files
		try {
			in.close();
			outIncome.close();
			outSpending.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
