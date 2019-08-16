package cn.chain33.javasdk.model.rpcresult;

import java.io.Serializable;

/**
 * Created by apa7 on 2019/8/16.
 * Maintainer:
 */
public class ReceiptLog implements Serializable {

    private Integer ty;

    private String tyName;

    private TransferLog current;

    private TransferLog prev;

    public Integer getTy() {
        return ty;
    }

    public void setTy(Integer ty) {
        this.ty = ty;
    }

    public String getTyName() {
        return tyName;
    }

    public void setTyName(String tyName) {
        this.tyName = tyName;
    }

    public TransferLog getCurrent() {
        return current;
    }

    public void setCurrent(TransferLog current) {
        this.current = current;
    }

    public TransferLog getPrev() {
        return prev;
    }

    public void setPrev(TransferLog prev) {
        this.prev = prev;
    }
}