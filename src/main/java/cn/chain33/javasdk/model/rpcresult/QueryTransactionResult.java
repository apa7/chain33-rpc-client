package cn.chain33.javasdk.model.rpcresult;

import java.io.Serializable;
import java.math.BigInteger;

public class QueryTransactionResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigInteger amount;

    private TransactionResult tx;

    private Long blockTime;

    private Integer index;

    private String actionName;

    private String fromAddr;

    private Long height;

    private Receipt receipt;

    public TransactionResult getTx() {
        return tx;
    }

    public void setTx(TransactionResult tx) {
        this.tx = tx;
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }


    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public Long getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(Long blockTime) {
        this.blockTime = blockTime;
    }

    public String getFromAddr() {
        return fromAddr;
    }

    public void setFromAddr(String fromAddr) {
        this.fromAddr = fromAddr;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    @Override
    public String toString() {
        return "QueryTransactionResult [amount=" + amount + ", tx=" + tx + ", blockTime=" + blockTime + ", index="
               + index + ", actionName=" + actionName + ", fromAddr=" + fromAddr + ", height=" + height + "]";
    }

}
