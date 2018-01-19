package gigawatt;

/**
 * BitcointaxHeader is the class used to represent the header of the CSV file
 * to be imported as Income to Bitcoin.tax
 * 
 * @author Elias Chavarria
 *
 */
public class BitcointaxIncomeSpendingHeader {
	private static final String dateTxt = "Date";
	static final int dateIdx = 1;
	private static final String actionTxt = "Action";
	static final int actionIdx = 2;
	private static final String memoTxt = "Memo";
	static final int memoIdx = 3;
	private static final String sourceTxt = "Source";
	static final int sourceIdx = 4;
	private static final String symbolTxt = "Symbol";
	static final int symbolIdx = 5;
	private static final String volumeTxt = "Volume";
	static final int volumeIdx = 6;
	
	/**
	 * Gets the maximum index of CSV fields
	 * 
	 * @return Max index used for the fields
	 * 
	 */
	public static int getMaxIdx() {
		return 5;
	}
	
	/**
	 * Provides the string representation of the header.
	 * 
	 * @return string representing the header line.
	 */
	public String toString() { 
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < 6; i++) {
			switch(i) {
			case dateIdx:
				sb.append(dateTxt);
				break;
			case actionIdx:
				sb.append(actionTxt);
				break;
			case memoIdx:
				sb.append(memoTxt);
				break;
			case sourceIdx:
				sb.append(sourceTxt);
				break;
			case symbolIdx:
				sb.append(symbolTxt);
				break;
			case volumeIdx:
				sb.append(volumeTxt);
				break;
			}
			if (i < 5) sb.append(',');
		}
		
		return sb.toString();
	}
}
