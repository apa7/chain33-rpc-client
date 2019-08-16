package cn.chain33.javasdk.model.rpcresult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Receipt implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer ty;

    private String tyName;

    private List<ReceiptLogs> logs = new ArrayList<>();

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

    public List<ReceiptLogs> getLogs() {
        return logs;
    }

    public void setLogs(List<ReceiptLogs> logs) {
        this.logs = logs;
    }
}