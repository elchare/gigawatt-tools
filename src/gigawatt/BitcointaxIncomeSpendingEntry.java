package gigawatt;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents the "Action" field in the CSV file.
 * 
 * @author Elias Chavarria
 *
 */
enum BtisTranAction {
	btaIncome("INCOME"),
	btaGift("GIFT"),
	btaMining("MINING"),
	btaSpend("SPEND"),
	btaDonation("DONATION"),
	btaTransfer("TRANSFER");
	
	private final String type;
	
	BtisTranAction(String type) {
		this.type = type;
	}
	
	public String toString() { return type; }
}

/**
 * Represents the "Symbol" field in the CSV file.
 * 
 * @author Elias Chavarria
 *
 */
enum BtisTranSymbol {
	btsBitcoin("BTC"),
	btsBitcoinCash("BCH"),
	btsEthereum("ETH"),
	btsLitecoin("LTC"),
	btsDash("DASH"),
	btsZCash("ZEC");
	
	private final String symbol;
	
	BtisTranSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public String toString() { return symbol; }
}

/**
 * BitcointaxIncomeEntry represents a single non-header entry/line in the CSV 
 * file to be imported as Income to Cointracking.
 * 
 * @author Elias Chavarria
 *
 */
public class BitcointaxIncomeSpendingEntry {
	private ZonedDateTime  date;
	private BtisTranAction action;
	private String         memo;
	private String         source;
	private BtisTranSymbol symbol;
	private BigDecimal     volume;
	private BigDecimal     fee;
	private BtisTranSymbol feeCurrency;
	
	private static DateTimeFormatter formatter;
	
	public BitcointaxIncomeSpendingEntry() {
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
	}
	
	/**
	 * Sets the value of the transaction date
	 * 
	 * @param date
	 */
	void setDate(ZonedDateTime date) {
		this.date = date;
	}
	
	/**
	 * Sets the value of the transaction action
	 * 
	 * @param action
	 */
	void setAction(BtisTranAction action) {
		this.action = action;
	}
	
	/**
	 * Sets the value of the transaction memo
	 * 
	 * @param memo
	 */
	void setMemo(String memo) {
		this.memo = memo;
	}
	
	/**
	 * Sets the value of the transaction source
	 * 
	 * @param source
	 */
	void setSource(String source) {
		this.source = source;
	}
	
	/**
	 * Sets the value of the transaction symbol
	 * 
	 * @param symbol
	 */
	void setSymbol(BtisTranSymbol symbol) {
		this.symbol = symbol;
	}
	
	/**
	 * Sets the value of the transaction volume
	 * 
	 * @param volume
	 */
	void setVolume(BigDecimal volume) {
		this.volume = volume;
	}
	
	/**
	 * Sets the value of the transaction fee
	 * 
	 * @param fee
	 */
	void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	
	/**
	 * Sets the value of the currency for the transaction fee
	 * 
	 * @param feeCurrency
	 */
	void setFeeCurrency(BtisTranSymbol feeCurrency) {
		this.feeCurrency = feeCurrency;
	}
	
	/**
	 * Converts the object to a String
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		int maxIdx = BitcointaxIncomeSpendingHeader.getMaxIdx();
	
		for (int i = 0; i <= maxIdx; i++) {
			switch (i) {
			case BitcointaxIncomeSpendingHeader.dateIdx:
				sb.append(formatter.format(date));
				break;
			case BitcointaxIncomeSpendingHeader.actionIdx:
				sb.append(action);
				break;
			case BitcointaxIncomeSpendingHeader.memoIdx:
				sb.append(memo);
				break;
			case BitcointaxIncomeSpendingHeader.sourceIdx:
				sb.append(source);
				break;
			case BitcointaxIncomeSpendingHeader.symbolIdx:
				sb.append(symbol);
				break;
			case BitcointaxIncomeSpendingHeader.volumeIdx:
				sb.append(volume);
				break;
			}
			
			if (i < maxIdx) {
				sb.append(',');
			}
		}
		
		return sb.toString();
	}
	
	public String toTransferFeeString() throws Exception {
		StringBuilder sb = new StringBuilder();
		
		if (action != BtisTranAction.btaTransfer) {
			throw new Exception("Tried to assign fee to " + action
					+ "transaction.");
		}
		
		int maxIdx = BitcointaxIncomeSpendingHeader.getMaxIdx();
		
		for (int i = 0; i <= maxIdx; i++) {
			switch (i) {
			case BitcointaxIncomeSpendingHeader.dateIdx:
				sb.append(formatter.format(date));
				break;
			case BitcointaxIncomeSpendingHeader.actionIdx:
				sb.append(BtisTranAction.btaSpend);
				break;
			case BitcointaxIncomeSpendingHeader.memoIdx:
				sb.append(memo + " - transfer fee");
				break;
			case BitcointaxIncomeSpendingHeader.sourceIdx:
				sb.append(source);
				break;
			case BitcointaxIncomeSpendingHeader.symbolIdx:
				sb.append(feeCurrency);
				break;
			case BitcointaxIncomeSpendingHeader.volumeIdx:
				sb.append(fee);
				break;
			}
			
			if (i < maxIdx) {
				sb.append(',');
			}
		}
		
		return sb.toString();
	}
}
