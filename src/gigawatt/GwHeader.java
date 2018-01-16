package gigawatt;

/**
 * GwHeader is the class used to represent the header of the CSV file exported
 * from the Gigawatt dashboard.
 * 
 * @author    Elias Chavarria
 * 
 */
public class GwHeader {
	private static final String idTxt = "ID";
	private static int idIdx;
	private static final String emailTxt = "E-mail";
	private static int emailIdx;
	private static final String statusTxt = "Status";
	private static int statusIdx;
	private static final String typeTxt = "Type";
	private static int typeIdx;
	private static final String amountTxt = "Amount";
	private static int amountIdx;
	private static final String autoTxt = "Auto";
	private static int autoIdx;
	private static final String currencyTxt = "Currency";
	private static int currencyIdx;
	private static final String createdAtTxt = "Created At";
	private static int createdAtIdx;
	private static final String updatedAtTxt = "Updated At";
	private static int updatedAtIdx;
	private static int maxIdx;
	
	/**
	 * Parses the header of the CSV export from the Gigawatt Dashboard.
	 * Internally, the function identifies in what column each field is located
	 * 
	 * @param str String contining the header of the CSV export
	 * 
	 * @throws Exception when an invalid header is read.
	 */
	public static void parseHeader(String str) throws Exception {
		String[] parts = str.split(";");
		maxIdx = parts.length - 1;
		
		for (int i=0; i < parts.length; i++) {
			switch (parts[i]) {
			case idTxt:
				idIdx = i;
				break;
			case emailTxt:
				emailIdx = i;
				break;
			case statusTxt:
				statusIdx = i;
				break;
			case typeTxt:
				typeIdx = i;
				break;
			case amountTxt:
				amountIdx = i;
				break;
			case autoTxt:
				autoIdx = i;
				break;
			case currencyTxt:
				currencyIdx = i;
				break;
			case createdAtTxt:
				createdAtIdx = i;
				break;
			case updatedAtTxt:
				updatedAtIdx = i;
				break;
			default:
				System.err.println("Invalid header entry for GW Header.");
				throw new Exception("Invalid header entry for GW Header.");
			}
		}
	}
	
	/**
	 * Gets the column index of the "ID" field
	 * 
	 * @return index of the "ID" field
	 * 
	 */
	static int getIdIdx() { return idIdx;}
	
	/**
	 * Gets the column index of the "E-mail" field
	 * 
	 * @return index of the "E-mail" field
	 * 
	 */
	static int getEmailIdx() { return emailIdx; }
	
	/**
	 * Gets the column index of the "Status" field
	 * 
	 * @return index of the "Status" field
	 * 
	 */
	static int getStatusIdx() { return statusIdx; }
	
	/**
	 * Gets the column index of the Type field
	 * 
	 * @return index of the "Type" field
	 * 
	 */
	static int getTypeIdx() { return typeIdx; }
	
	/**
	 * Gets the column index of the "Amount" field
	 * 
	 * @return index of the "Amount" field
	 * 
	 */
	static int getAmountIdx() { return amountIdx; }
	
	/**
	 * Gets the column index of the "Auto" field
	 * 
	 * @return index of the "Auto" field
	 * 
	 */
	static int getAutoIdx() { return autoIdx; }
	
	/**
	 * Gets the column index of the "Currency" field
	 * 
	 * @return index of the "Currency" field
	 * 
	 */
	static int getCurrencyIdx() { return currencyIdx; }
	
	/**
	 * Gets the column index of the "Created At" field
	 * 
	 * @return index of the "Created At" field
	 * 
	 */
	static int getCreatedAtIdx() { return createdAtIdx; }
	
	/**
	 * Gets the column index of the "Updated At" field
	 * 
	 * @return index of the "Updated At" field
	 * 
	 */
	static int getUpdatedAtIdx() { return updatedAtIdx; }
	
	/**
	 * Gets the maximum index of CSV fields
	 * 
	 * @return Max index for CSV fields.
	 * 
	 */
	public int getMaxIdx() {
		return maxIdx;
	}
}
