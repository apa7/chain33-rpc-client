package cn.chain33.javasdk.model.rpcresult;

import java.io.Serializable;

/**
 * Created by apa7 on 2019/8/15.
 * Maintainer:
 */
public class PayLoad implements Serializable {

    private Transfer transfer;

    public Transfer getTransfer() {
        return transfer;
    }

    public void setTransfer(Transfer transfer) {
        this.transfer = transfer;
    }

    @Override
    public String toString() {
        return "PayLoad{" +
               "transfer=" + transfer +
               '}';
    }
}
