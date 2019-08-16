package cn.chain33.javasdk.model.rpcresult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apa7 on 2019/8/15.
 * Maintainer:
 */
public class BlockItemsResult implements Serializable {

    private List<BlocksResult> items = new ArrayList<>();

    public List<BlocksResult> getItems() {
        return items;
    }

    public void setItems(List<BlocksResult> items) {
        this.items = items;
    }
}
