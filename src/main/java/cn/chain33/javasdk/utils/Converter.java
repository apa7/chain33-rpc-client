package cn.chain33.javasdk.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by apa7 on 2019/8/14.
 * Maintainer:
 */
public class Converter {

    public static final int DEFAULT_FACTOR = 8;

    public static BigDecimal getFactor(int decimals) {
        return BigDecimal.TEN.pow(decimals);
    }

    public static BigDecimal getDefaultFactor(){
        return getFactor(DEFAULT_FACTOR);
    }

    public static BigDecimal fromNum(BigInteger num, int decimals) {
        BigDecimal value = new BigDecimal(num.toString());
        return value.divide(getFactor(decimals));
    }

    public static BigDecimal fromNum(String numStr, int decimals) {
        BigDecimal value = new BigDecimal(numStr);
        return value.divide(getFactor(decimals));
    }

    public static BigInteger toNum(BigDecimal value, int decimals) {
        BigDecimal result = value.multiply(getFactor(decimals));
        return result.toBigInteger();
    }

    /**
     * 转正常小数.
     */
    public static BigDecimal from(BigInteger wei) {
        BigDecimal value = new BigDecimal(wei.toString());
        return value.divide(getDefaultFactor());
    }

    /**
     * 转为整数Number, 8位.
     */
    public static BigInteger to(BigDecimal value) {
        return value.multiply(getDefaultFactor()).toBigInteger();
    }
}
