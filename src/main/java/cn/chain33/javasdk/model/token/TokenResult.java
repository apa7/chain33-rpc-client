package cn.chain33.javasdk.model.token;

import java.io.Serializable;
import java.util.List;

/**
 * Created by apa7 on 2019/8/15.
 * Maintainer:
 */
public class TokenResult implements Serializable {

    private List<Token> tokens;

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }
}