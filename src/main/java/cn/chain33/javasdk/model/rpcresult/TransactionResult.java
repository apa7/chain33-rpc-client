package cn.chain33.javasdk.model.rpcresult;

import java.io.Serializable;


public class TransactionResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private PayLoad payload;

    private String execer;

    //private String rawpayload;

    private long fee;

    private String feefmt;

    private long expire;

    private long nonce;

    private String from;

    private String to;

    private String hash;

    //private Signature signature;

    public String getExecer() {
        return execer;
    }

    public void setExecer(String execer) {
        this.execer = execer;
    }

    public PayLoad getPayload() {
        return payload;
    }

    public void setPayload(PayLoad payload) {
        this.payload = payload;
    }

    public long getFee() {
        return fee;
    }

    public void setFee(long fee) {
        this.fee = fee;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public long getNonce() {
        return nonce;
    }

    public void setNonce(long nonce) {
        this.nonce = nonce;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFeefmt() {
        return feefmt;
    }

    public void setFeefmt(String feefmt) {
        this.feefmt = feefmt;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return "TransactionResult [payload=" + payload + ", execer=" + execer + ", fee="
               + fee + ", expire=" + expire + ", nonce=" + nonce + ", from=" + from + ", hash=" + hash + ", to=" + to + "]";
    }


}
