package cn.chain33.javasdk.model.enums;

/**
 * Created by apa7 on 2019/8/13.
 * Maintainer:
 */
public enum Execer {

    /*查询可用的主代币.*/
    COINS("coins"),

    /*ticket 查询正在挖矿的 主代币.*/
    TICKET("ticket"),

    /*查询可用的Token*/
    TOKEN("token"),

    /*查询正在交易合约里的Token*/
    TRADE("trade"),

    /*如果是查询平行链上余额，则需要指定具体平行链的执行器execer,例如：user.p.xxx.token .*/

    /* 合约-虚拟机 */
    EVM("evm");

    private String name;

    Execer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}