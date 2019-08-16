package cn.chain33.javasdk.model.rpcresult;

import java.io.Serializable;

/**
 * Created by apa7 on 2019/8/15.
 * Maintainer:
 */
public class Transfer implements Serializable {

    private String cointoken;

    private String amount;

    private String note;

    private String to;

    public String getCointoken() {
        return cointoken;
    }

    public void setCointoken(String cointoken) {
        this.cointoken = cointoken;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Transfer{" +
               "cointoken='" + cointoken + '\'' +
               ", amount='" + amount + '\'' +
               ", note='" + note + '\'' +
               ", to='" + to + '\'' +
               '}';
    }
}