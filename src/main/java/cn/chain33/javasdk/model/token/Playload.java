package cn.chain33.javasdk.model.token;

import java.io.Serializable;

/**
 * Created by apa7 on 2019/8/15.
 * Maintainer:
 */
public class Playload implements Serializable {

    public static final int STATUS_NO = 0;  //预创建
    public static final int STATUS_OK = 1;  //已完成

    private Integer status;

    private Boolean queryAll;

    private Boolean symbolOnly;

    private String data;

    public Playload() {
    }

    public Playload(String data) {
        this.data = data;
    }

    public Playload(Integer status, Boolean queryAll, Boolean symbolOnly) {
        this.status = status;
        this.queryAll = queryAll;
        this.symbolOnly = symbolOnly;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getQueryAll() {
        return queryAll;
    }

    public void setQueryAll(Boolean queryAll) {
        this.queryAll = queryAll;
    }

    public Boolean getSymbolOnly() {
        return symbolOnly;
    }

    public void setSymbolOnly(Boolean symbolOnly) {
        this.symbolOnly = symbolOnly;
    }

    public String getData() {
        return data;
    }

    public Playload setData(String data) {
        this.data = data;
        return this;
    }
}