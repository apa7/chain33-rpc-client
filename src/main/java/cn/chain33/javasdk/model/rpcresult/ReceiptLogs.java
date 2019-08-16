package cn.chain33.javasdk.model.rpcresult;

import java.io.Serializable;

/**
 * Created by apa7 on 2019/8/16.
 * Maintainer:
 */
public class ReceiptLogs implements Serializable {

    private Integer ty;

    private String tyName;

    private ReceiptLog log;

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

    public ReceiptLog getLog() {
        return log;
    }

    public void setLog(ReceiptLog log) {
        this.log = log;
    }
}