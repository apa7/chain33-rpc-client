package cn.chain33.javasdk.model.rpcresult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apa7 on 2019/8/15.
 * Maintainer:
 */
public class QueryTxsResult implements Serializable {

    private List<QueryTransactionResult> txs = new ArrayList<>();

    public List<QueryTransactionResult> getTxs() {
        return txs;
    }

    public void setTxs(List<QueryTransactionResult> txs) {
        this.txs = txs;
    }
}
