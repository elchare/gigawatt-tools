package gigawatt;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * GwTranStatus represents the "Status" field in the CSV file.
 * 
 * @author Elias Chavarria
 * 
 */
enum GwTranStatus {
	gwtDone("done");
	
	private final String status;
	
	GwTranStatus(String status) {
		this.status = status;
	}
	
	/**
	 * Returns the string representation of GwTranStatus
	 */
	public String toString() { return status; }
};

/**
 * GwTranType represents the "Type" field in the CSV file.
 * 
 * @author Elias Chavarria
 * 
 */
enum GwTranType {
	gwtHosting("hosting"),
	gwtReward("reward"),
	gwtWithdrawal("withdraw"),
	gwtWttRent("wtt_rent");
	
	private final String type;
	
	GwTranType (String type) {
		this.type = type;
	}
	
	/**
	 * Returns the string representation of GwTranType
	 */
	public String toString() { return type; }
};

/**
 * GwTranCurrency represents the "Currency" field in the CSV file.
 * 
 * @author Elias Chavarria
 *
 */
enum GwTranCurrency {
	gwtBitcoin("btc"),
	gwtBitcoinCash("bch"),
	gwtLitecoin("ltc"),
	gwtEthereum("eth"),
	gwtDash("dash"),
	gwtZec("zec");
	
	private final String currency;
	
	GwTranCurrency(String currency) {
		this.currency = currency;
	}
	
	/**
	 * Returns the string representation of GwTranCurrency
	 */
	public String toString() { return currency; }
	
	/**
	 * Returns an equivalent CtTranCurrency object
	 * 
	 * @return Equivalent CtTranCurrency object
	 * @throws Exception
	 */
	public CtTranCurrency toCtTranCurrency() throws Exception {
		switch (currency) {
		case "btc":
			return CtTranCurrency.cttBitcoin;
		case "bch":
			return CtTranCurrency.cttBitcoinCash;
		case "ltc":
			return CtTranCurrency.cttLitecoin;
		case "eth":
			return CtTranCurrency.cttEthereum;
		case "dash":
			return CtTranCurrency.cttDash;
		case "zec":
			return CtTranCurrency.cttZCash;
		default:
			System.err.println("Cannot convert " + currency 
					+ " to Cointracking currency");
			throw new Exception("Cannot convert " + currency 
					+ " to Cointracking currency");
		}
	}
	
	/**
	 * Returns an equivalent BtisTranSymbol object
	 * 
	 * @return Equivalent BtisTranSymbol object
	 * @throws Exception
	 */
	public BtisTranSymbol toBtTranCurrency() throws Exception {
		switch (currency) {
		case "btc":
			return BtisTranSymbol.btsBitcoin;
		case "bch":
			return BtisTranSymbol.btsBitcoinCash;
		case "ltc":
			return BtisTranSymbol.btsLitecoin;
		case "eth":
			return BtisTranSymbol.btsEthereum;
		case "dash":
			return BtisTranSymbol.btsDash;
		case "zec":
			return BtisTranSymbol.btsZCash;
		default:
			System.err.println("Cannot convert " + currency 
					+ " to Cointracking currency");
			throw new Exception("Cannot convert " + currency 
					+ " to Cointracking currency");
		}
	}
}

/**
 * GwEntry represents a single non-header entry/line in the CSV file exported 
 * from the Gigawatt dashboard.
 * 
 * @author Elias Chavarria
 *
 */
public class GwEntry {
	private String id;
	private String email;
	private GwTranStatus status;
	private GwTranType type;
	private BigDecimal amount;
	private Boolean auto;
	private GwTranCurrency currency;
	private ZonedDateTime createdAt;
	private ZonedDateTime updatedAt;
	private BigDecimal fee;
	
	private static DateTimeFormatter formatter;
	
	public GwEntry() {
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	}
	
	private void setId(String id) {
		this.id = id;
	}
	
	private void setEmail(String email) {
		this.email = email;
	}
	
	private void setStatus(String status) throws Exception {
		boolean match = false;
		for (GwTranStatus p: GwTranStatus.values()) {
			if (p.toString().equals(status)) {
				this.status = p;
				match = true;
				return;
			}
		}
		
		if (!match) {
			System.err.println("No match found for GwEntry with status"
					+ status);
			throw new Exception("No match found for GwEntry with status"
					+ status);
		}
	}
	
	private void setType(String type) throws Exception {
		boolean match = false;
		for (GwTranType p: GwTranType.values()) {
			if (p.toString().equals(type)) {
				this.type = p;
				match = true;
				return;
			}
		}
		
		if (!match) {
			System.err.println("No match found for GwEntry with type"
					+ type);
			throw new Exception("No match found for GwEntry with type"
					+ type);
		}
	}
	
	private void setAmount(String amount) {
		this.amount = new BigDecimal(amount);
	}
	
	private void setAuto(String auto) {
		this.auto = Boolean.parseBoolean(auto);
	}
	
	private void setCurrency(String currency) throws Exception {
		boolean match = false;
		for (GwTranCurrency p: GwTranCurrency.values()) {
			if (p.toString().equals(currency)) {
				this.currency = p;
				match = true;
				return;
			}
		}
		if (!match) {
			System.err.println("No match found for GwEntry with currency"
					+ currency);
			throw new Exception("No match found for GwEntry with currency"
					+ currency);
		}
	}
	
	/**
	 * Returns the transaction currency
	 * @return
	 */
	GwTranCurrency getCurrency() {
		return currency;
	}
	
	private void setCreatedAt(String createdAt) {
		LocalDateTime ldt = LocalDateTime.parse(createdAt,formatter);
		this.createdAt = ldt.atZone(ZoneId.of("UTC"));
	}
	
	private void setUpdatedAt(String updatedAt) {
		LocalDateTime ldt = LocalDateTime.parse(updatedAt,formatter);
		this.updatedAt = ldt.atZone(ZoneId.of("UTC"));
	}
	
	private void setFee(String fee) {
		this.fee = new BigDecimal(fee);
	}
	
	/**
	 * Parses a single non-header line from the CSV file
	 * 
	 * @param str single non-header line from the CSV file
	 * @throws Exception
	 */
	void parseEntry(String str) throws Exception {
		String[] parts = str.split(";");
		
		for (int i=0; i < parts.length; i++) {
			if (i == GwHeader.getIdIdx()) {
				setId(parts[i]);
			} else if (i == GwHeader.getEmailIdx()) {
				setEmail(parts[i]);
			} else if (i == GwHeader.getStatusIdx()) {
				setStatus(parts[i]);
			} else if (i == GwHeader.getTypeIdx()) {
				setType(parts[i]);
			} else if (i == GwHeader.getAmountIdx()) {
				setAmount(parts[i]);
			} else if (i == GwHeader.getAutoIdx()) {
				setAuto(parts[i]);
			} else if (i == GwHeader.getCurrencyIdx()) {
				setCurrency(parts[i]);
			} else if (i == GwHeader.getCreatedAtIdx()) {
				setCreatedAt(parts[i]);
			} else if (i == GwHeader.getUpdatedAtIdx()) {
				setUpdatedAt(parts[i]);
			} else if (i == GwHeader.getFeeIdx()) {
				setFee(parts[i]);
			} else {
				System.err.println("Invalid number of parts in GwEntry: "
						+ parts.length);
				throw new Exception("Invalid number of parts in GwEntry: "
						+ parts.length);
			}
		}
	}
	
	/**
	 * Uses the fields from the GwEntry to create a CoinTrackingEntry object.
	 * 
	 * @return CointrackingEntry object with equivalent fields to those of the
	 * current object.
	 * @throws Exception
	 */
	CointrackingEntry toCointrackingEntry() throws Exception {
		if (status != GwTranStatus.gwtDone) return null;
		
		CointrackingEntry entry = new CointrackingEntry();
		entry.setExchange("Gigawatt");
		
		switch (type) {
		case gwtHosting:
			entry.setType(CtTranType.cttSpend);
			entry.setSellAmnt(amount.abs());
			entry.setSellCur(currency.toCtTranCurrency());
			entry.setGroup(GwTranType.gwtHosting.toString());
			break;
		case gwtReward:
			entry.setType(CtTranType.cttMining);
			entry.setBuyAmnt(amount);
			entry.setBuyCur(currency.toCtTranCurrency());
			entry.setGroup(GwTranType.gwtReward.toString());
			break;
		case gwtWithdrawal:
			entry.setType(CtTranType.cttWithdrawal);
			entry.setSellAmnt(amount.abs().add(fee.abs()));
			entry.setSellCur(currency.toCtTranCurrency());
			entry.setFeeAmnt(fee.abs());
			entry.setFeeCur(currency.toCtTranCurrency());
			entry.setGroup(GwTranType.gwtWithdrawal.toString());
			break;
		case gwtWttRent:
			entry.setType(CtTranType.cttMining);
			entry.setBuyAmnt(amount);
			entry.setBuyCur(currency.toCtTranCurrency());
			entry.setGroup(GwTranType.gwtWttRent.toString());
			break;
		default:
			System.err.println("Unsupported GW Tran Type for conversion: " 
					+ type);
			throw new Exception("Unsupported GW Tran Type for conversion: " 
					+ type);
		}
		
		entry.setDate(createdAt);
		entry.setComment(id);
		
		return entry;
	}
	
	/**
	 * Gets the transaction type.
	 * @return
	 */
	public GwTranType getType() {
		return type;
	}
	
	/**
	 * Uses the fields from the GwEntry to create a BitcointaxIncomeSpendingEntry
	 * object.
	 * 
	 * @return BitcointaxIncomeSpendingEntry object with equivalent fields to
	 * those of the current object.
	 * @throws Exception
	 */
	BitcointaxIncomeSpendingEntry toBitcointaxIncomeSpendingEntry() throws Exception {
		if (status != GwTranStatus.gwtDone) return null;
		
		BitcointaxIncomeSpendingEntry entry = 
				new BitcointaxIncomeSpendingEntry();
		
		switch (type) {
		case gwtHosting:
			entry.setAction(BtisTranAction.btaSpend);
			entry.setVolume(amount.abs());
			break;
		case gwtReward:
			entry.setAction(BtisTranAction.btaMining);
			entry.setVolume(amount);
			break;
		case gwtWithdrawal:
			entry.setAction(BtisTranAction.btaTransfer);
			entry.setVolume(amount.abs());
			entry.setFee(fee.abs());
			entry.setFeeCurrency(currency.toBtTranCurrency());
			break;
		case gwtWttRent:
			entry.setAction(BtisTranAction.btaMining);
			entry.setVolume(amount);
			break;
		default:
			System.err.println("Unsupported GW Tran Type for conversion: " 
					+ type);
			throw new Exception("Unsupported GW Tran Type for conversion: " 
					+ type);
		}
		
		entry.setSource("Gigawatt");
		entry.setSymbol(currency.toBtTranCurrency());
		entry.setDate(createdAt);
		entry.setMemo(type.toString());
		
		return entry;
	}
	
	
	BigDecimal getAmount() {
		return amount;
	}
}
