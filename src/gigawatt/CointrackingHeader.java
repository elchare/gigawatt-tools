package gigawatt;

/**
 * CointrackingHeader is the class used to represent the header of the CSV file
 * to be imported to Cointracking.
 * 
 * @author Elias Chavarria
 *
 */
public class CointrackingHeader {
	private static final String typeTxt = "Type";
	static final int typeIdx = 0;
	private static final String buyAmntTxt = "Buy";
	static final int buyAmntIdx = 1;
	private static final String buyCurTxt = "Cur.";
	static final int buyCurIdx = 2;
	private static final String sellAmntTxt = "Sell";
	static final int sellAmntIdx = 3;
	private static final String sellCurTxt = "Cur.";
	static final int sellCurIdx = 4;
	private static final String feeAmntTxt = "Fee";
	static final int feeAmntIdx = 5;
	private static final String feeCurTxt = "Cur.";
	static final int feeCurIdx = 6;
	private static final String exchangeTxt = "Exchange";
	static final int exchangeIdx = 7;
	private static final String groupTxt = "Group";
	static final int groupIdx = 8;
	private static final String commentTxt = "Comment";
	static final int commentIdx = 9;
	private static final String dateTxt = "Date";
	static final int dateIdx = 10;
	
	/**
	 * Gets the maximum index of CSV fields
	 * 
	 * @return Max index used for the fields
	 * 
	 */
	public static int getMaxIdx() {
		return 10;
	}
	
	/**
	 * Provides the string representation of the header.
	 * 
	 * @return string representing the header line.
	 */
	public String toString() { 
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < 11; i++) {
			sb.append("\"");
			switch (i) {
			case typeIdx:
				sb.append(typeTxt);
				break;
			case buyAmntIdx:
				sb.append(buyAmntTxt);
				break;
			case buyCurIdx:
				sb.append(buyCurTxt);
				break;
			case sellAmntIdx:
				sb.append(sellAmntTxt);
				break;
			case sellCurIdx:
				sb.append(sellCurTxt);
				break;
			case feeAmntIdx:
				sb.append(feeAmntTxt);
				break;
			case feeCurIdx:
				sb.append(feeCurTxt);
				break;
			case exchangeIdx:
				sb.append(exchangeTxt);
				break;
			case groupIdx:
				sb.append(groupTxt);
				break;
			case commentIdx:
				sb.append(commentTxt);
				break;
			case dateIdx:
				sb.append(dateTxt);
				break;
			}
			sb.append("\"");
			if (i < 10) sb.append(',');
		}
		
		return sb.toString();
	}
}
