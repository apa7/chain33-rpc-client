package cn.chain33.javasdk.model.token;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by apa7 on 2019/8/15.
 * Maintainer:
 */
public class Token implements Serializable {

    private String name;

    private String symbol;

    private String introduction;

    private String owner;

    private BigInteger total;

    private BigInteger price;

    public Token() {
    }

    public Token(String name, String symbol, String introduction, String owner, BigInteger total, BigInteger price) {
        this.name = name;
        this.symbol = symbol;
        this.introduction = introduction;
        this.owner = owner;
        this.total = total;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public BigInteger getTotal() {
        return total;
    }

    public void setTotal(BigInteger total) {
        this.total = total;
    }

    public BigInteger getPrice() {
        return price;
    }

    public void setPrice(BigInteger price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Token {" +
               "name='" + name + '\'' +
               ", symbol='" + symbol + '\'' +
               ", introduction='" + introduction + '\'' +
               ", owner='" + owner + '\'' +
               ", total=" + total +
               ", price=" + price +
               '}';
    }
}