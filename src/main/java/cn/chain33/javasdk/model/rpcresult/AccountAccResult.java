package cn.chain33.javasdk.model.rpcresult;

import java.math.BigInteger;

public class AccountAccResult {
	
	//coins标识（token里无视这个值）
	private Integer currency;
	
	//帐号的可用余额
	private BigInteger balance;
	
	//帐号中冻结余额
	private BigInteger frozen;
	
	//帐号的地址
	private String addr;

	public Integer getCurrency() {
		return currency;
	}

	public void setCurrency(Integer currency) {
		this.currency = currency;
	}

	public BigInteger getBalance() {
		return balance;
	}

	public void setBalance(BigInteger balance) {
		this.balance = balance;
	}

	public BigInteger getFrozen() {
		return frozen;
	}

	public void setFrozen(BigInteger frozen) {
		this.frozen = frozen;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	@Override
	public String toString() {
		return "NewAccountAccResult [currency=" + currency + ", balance=" + balance + ", frozen=" + frozen + ", addr="
				+ addr + "]";
	}
	
	
	
}
