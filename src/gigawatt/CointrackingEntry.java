package gigawatt;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents the "Type" field in the CSV file.
 * 
 * @author Elias Chavarria
 *
 */
enum CtTranType {
	cttDeposit("Deposit"),
	cttTrade("Trade"),
	cttMining("Mining"),
	cttGift("Gift"),
	cttWithdrawal("Withdrawal"),
	cttStolen("Stolen"),
	cttSpend("Spend"),
	cttGiftTip("Gift/Tip");
	
	private final String type;
	
	CtTranType(String type) {
		this.type = type;
	}
	
	public String toString() { return type; }
}

/**
 * Represents a "Currency" field in the CSV file.
 * 
 * @author Elias Chavarria
 *
 */
enum CtTranCurrency {
	cttBitcoin("BTC"),
	cttBitcoinCash("BCH"),
	cttEthereum("ETH"),
	cttLitecoin("LTC"),
	cttUSD("USD"),
	cttDash("DASH"),
	cttZCash("ZEC"),
	cttNone("");
	
	private final String currency;
	
	CtTranCurrency(String currency) {
		this.currency = currency;
	}
	
	public String toString() { return currency; }
}

/**
 * CointrackingEntry represents a single non-header entry/line in the CSV file
 * to be imported to Cointracking.
 * 
 * @author Elias
 *
 */
public class CointrackingEntry {
	private CtTranType 		type;
	private String     		buyAmnt = "";
	private CtTranCurrency  buyCur = CtTranCurrency.cttNone;
	private String     		sellAmnt = "";
	private CtTranCurrency  sellCur = CtTranCurrency.cttNone;
	private String     		feeAmnt = "";
	private CtTranCurrency  feeCur = CtTranCurrency.cttNone;;
	private String     		exchange = "";
	private String     		group = "";
	private String     		comment = "";
	private ZonedDateTime	date;
	
	private static DateTimeFormatter formatter;
	
	public CointrackingEntry () {
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * Sets the value of the transaction type.
	 * @param type
	 */
	void setType(CtTranType type) {
		this.type = type;
	}
	
	/**
	 * Sets the value of the transaction buy amount.
	 * @param amnt
	 */
	void setBuyAmnt(String amnt) {
		buyAmnt = amnt;
	}
	
	/**
	 * Get Buy Amount
	 * @return
	 */
	String getBuyAmnt() {
		return buyAmnt;
	}
	
	/**
	 * Sets the value of the transaction buy currency.
	 * @param cur
	 */
	void setBuyCur(CtTranCurrency cur) {
		buyCur = cur;
	}
	
	/**
	 * Get the value of the transaction buy currency.
	 * @return
	 */
	CtTranCurrency getBuyCur() {
		return buyCur;
	}
	
	/**
	 * Sets the value of the transaction sell amount.
	 * @param amnt
	 */
	void setSellAmnt(String amnt) {
		sellAmnt = amnt;
	}
	
	/**
	 * Sets the value of the transaction sell currency.
	 * @param cur
	 */
	void setSellCur(CtTranCurrency cur) {
		sellCur = cur;
	}
	
	/**
	 * Sets the value of the transaction fee amount.
	 * @param amnt
	 */
	void setFeeAmnt(String amnt) {
		feeAmnt = amnt;
	}
	
	/**
	 * Sets the value of the transaction fee currency.
	 * @param cur
	 */
	void setFeeCur(CtTranCurrency cur) {
		feeCur = cur;
	}
	
	/**
	 * Sets the value of the transaction exchange.
	 * @param str
	 */
	void setExchange(String str) {
		exchange = str;
	}
	
	/**
	 * Sets the value of the transaction group.
	 * @param str
	 */
	void setGroup(String str) {
		group = str;
	}
	
	/**
	 * Sets the value of the transaction comment.
	 * @param str
	 */
	void setComment(String str) {
		comment = str;
	}
	
	/**
	 * Sets the value of the transaction date.
	 * @param date
	 */
	void setDate (ZonedDateTime date) {
		this.date = date;
	}
	
	/**
	 * Converts the date to the timezone used in cointracking. Right now,
	 * such timezone is assumed to be the same of your system. If you need a 
	 * different timezone, change the ZoneId accordingly.
	 * @param zdt
	 * @return
	 */
	private ZonedDateTime convertToCtTimezone(ZonedDateTime zdt) {
		return zdt.withZoneSameInstant(ZoneId.systemDefault());
		//return zdt.withZoneSameInstant(ZoneId.of("America/Los_Angeles"));
	}
	
	/**
	 * Converts the object to a String
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		int maxIdx = CointrackingHeader.getMaxIdx();
		
		for (int i = 0; i <= maxIdx; i++) {
			sb.append("\"");
			switch (i) {
			case CointrackingHeader.typeIdx:
				sb.append(type);
				break;
			case CointrackingHeader.buyAmntIdx:
				sb.append(buyAmnt);
				break;
			case CointrackingHeader.buyCurIdx:
				sb.append(buyCur);
				break;
			case CointrackingHeader.sellAmntIdx:
				sb.append(sellAmnt);
				break;
			case CointrackingHeader.sellCurIdx:
				sb.append(sellCur);
				break;
			case CointrackingHeader.feeAmntIdx:
				sb.append(feeAmnt);
				break;
			case CointrackingHeader.feeCurIdx:
				sb.append(feeCur);
				break;
			case CointrackingHeader.exchangeIdx:
				sb.append(exchange);
				break;
			case CointrackingHeader.groupIdx:
				sb.append(group);
				break;
			case CointrackingHeader.commentIdx:
				sb.append(comment);
				break;
			case CointrackingHeader.dateIdx:
				sb.append(formatter.format(convertToCtTimezone(date)));
				break;
			}
			sb.append("\"");
			
			if (i < maxIdx) {
				sb.append(',');
			}
		}
		
		return sb.toString();
	}
}
