package cn.chain33.javasdk.client;

import cn.chain33.javasdk.model.enums.Execer;
import cn.chain33.javasdk.model.enums.RpcMethod;
import cn.chain33.javasdk.model.enums.SignType;
import cn.chain33.javasdk.model.exception.Chain33Exception;
import cn.chain33.javasdk.model.rpcresult.AccountAccResult;
import cn.chain33.javasdk.model.rpcresult.AccountResult;
import cn.chain33.javasdk.model.rpcresult.BlockItemsResult;
import cn.chain33.javasdk.model.rpcresult.BlockResult;
import cn.chain33.javasdk.model.rpcresult.BlocksResult;
import cn.chain33.javasdk.model.rpcresult.BooleanResult;
import cn.chain33.javasdk.model.rpcresult.QueryTransactionResult;
import cn.chain33.javasdk.model.rpcresult.QueryTxsResult;
import cn.chain33.javasdk.model.rpcresult.RpcResult;
import cn.chain33.javasdk.model.rpcresult.TxResult;
import cn.chain33.javasdk.model.rpcresult.WalletStatusResult;
import cn.chain33.javasdk.model.token.Playload;
import cn.chain33.javasdk.model.token.Token;
import cn.chain33.javasdk.model.token.TokenResult;
import cn.chain33.javasdk.utils.HexUtil;
import cn.chain33.javasdk.utils.HttpUtil;
import cn.chain33.javasdk.utils.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by apa7 on 2019/8/13.
 * Maintainer:
 */
public class JsonRpcClient {

    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    private static Gson gson = new Gson();

    // 通过配置文件或者其他方式设置URL
    private String BASE_URL;

    public JsonRpcClient() {
    }

    public JsonRpcClient(String url) {
        this.BASE_URL = url;
    }

    public JsonRpcClient(String host, Integer port) {
        this.BASE_URL = "http://" + host + ":" + port;
    }

    public void setBASE_URL(String bASE_URL) {
        BASE_URL = bASE_URL;
    }

    public void setUrl(String host, Integer port) {
        this.BASE_URL = "http://" + host + ":" + port;
    }

    public void setUrl(String url) {
        this.BASE_URL = url;
    }

    public String getUrl() {
        return BASE_URL;
    }

    private String postData(String method, Pair... pairs) {
        List<Map> list = new ArrayList<>();
        Map map = new HashMap();
        if (pairs != null) {
            for (Pair pair : pairs) {
                map.put(pair.getKey(), pair.getValue());
            }
        }
        list.add(map);
        return postData(method, list);
    }

    private String postData(String method, List params) {
        Map<String, Object> postData = new HashMap<>();
        postData.put("jsonrpc", "2.0");
        postData.put("id", 2);
        postData.put("method", method);
        if (params != null && params.size() > 0) {
            postData.put("params", params);
        }
        return HttpUtil.httpPostBody(getUrl(), gson.toJson(postData));
    }

    public Object invoke(String method, Type type, Pair... pairs) throws Chain33Exception {
        String httpPostResult = postData(method, pairs);
        if (StringUtil.isNotEmpty(httpPostResult)) {
            RpcResult result = gson.fromJson(httpPostResult, type);
            //logger.info("RESPONSE:" + httpPostResult);
            if (result != null && result.isValid()) {
                return result.getResult();
            }
            logger.error("RPC请求失败，错误信息：" + result == null ? "" : result.getError() + " , 请求参数：" + pairs);
            throw new Chain33Exception(result.getError());
        }
        logger.error("RPC请求失败，请求参数：" + pairs);
        return null;
    }

    public Object invoke(RpcMethod rpcMethod, Type type, Pair... pairs) throws Chain33Exception {
        return invoke(rpcMethod.getMethod(), type, pairs);
    }

    public Object invokeList(String method, Pair... pairs) {
        String httpPostResult = postData(method, pairs);
        if (StringUtil.isNotEmpty(httpPostResult)) {
            RpcResult result = gson.fromJson(httpPostResult, new TypeToken<RpcResult>() {
            }.getType());
            return result.getResult();
        }
        return null;
    }

    public Object invokeList(String method, Type type, List params) {
        String httpPostResult = postData(method, params);
        if (StringUtil.isNotEmpty(httpPostResult)) {
            RpcResult result = gson.fromJson(httpPostResult, type);
            //logger.info("RESPONSE:" + httpPostResult);
            if (result != null && result.isValid()) {
                return result.getResult();
            }
            logger.error("RPC请求失败，错误信息：" + result == null ? "" : result.getError() + " , 请求参数：" + params);
            return result.getResult();
        }
        logger.error("RPC请求失败，请求参数：" + params);
        return null;
    }

    /**
     * 查询同步状态
     *
     * @return Boolean
     */
    public Boolean isSync() throws Chain33Exception {
        return (Boolean) invoke(RpcMethod.BLOCKCHAIN_IS_SYNC, new TypeToken<RpcResult<Boolean>>() {
        }.getType());
    }

    /**
     * 8.1 获取钱包状态.
     */
    public WalletStatusResult getWalletStatus() throws Chain33Exception {
        return (WalletStatusResult) invoke(RpcMethod.GET_WALLET_STUATUS,
                                           new TypeToken<RpcResult<WalletStatusResult>>() {
                                           }.getType());
    }

    /**
     * 2.1 上锁 Lock
     *
     * @return BooleanResult
     */
    public BooleanResult lock() throws Chain33Exception {
        return (BooleanResult) invoke(RpcMethod.LOCK_WALLET,
                                      new TypeToken<RpcResult<BooleanResult>>() {
                                      }.getType());
    }

    /**
     * 2.2 解锁 Unlock.
     *
     * @param passwd
     * @param walletorticket true，只解锁ticket买票功能，false：解锁整个钱包。
     * @param timeout        解锁时间，默认 0，表示永远解锁；非 0 值，表示超时之后继续锁住钱包，单位：秒。
     * @return BooleanResult
     */
    public BooleanResult unlock(String passwd, boolean walletorticket, int timeout) throws Chain33Exception {
        return (BooleanResult) invoke(RpcMethod.UNLOCK_WALLET,
                                      new TypeToken<RpcResult<BooleanResult>>() {
                                      }.getType(),
                                      new Pair<String, Object>("passwd", passwd),
                                      new Pair<String, Object>("walletorticket", walletorticket),
                                      new Pair<String, Object>("timeout", timeout));
    }


    /**
     * 2.5 创建账户 NewAccount
     *
     * @param label
     */
    public String newAccount(String label) throws Chain33Exception {
        AccountResult ar = (AccountResult) invoke(RpcMethod.NEW_ACCOUNT,
                                                  new TypeToken<RpcResult<AccountResult>>() {
                                                  }.getType(),
                                                  new Pair<String, Object>("label", label));
        return ar != null && ar.getAcc() != null ? ar.getAcc().getAddr() : null;
    }


    /**
     * Chain33.GetBalance
     *
     * @param execer    执行器名称,coins查询可用的主代币，ticket查询正在挖矿的 主代币
     * @param addresses 地址
     * @return List<AccountAccResult>
     */
    public List<AccountAccResult> getBalance(Execer execer, String... addresses) throws Chain33Exception {
        return getBalance(execer, Arrays.asList(addresses));
    }

    public BigInteger getBalance(String addr) throws Chain33Exception {
        List<AccountAccResult> list = getBalance(Execer.COINS, addr);
        if (list == null || list.isEmpty()) {
            return BigInteger.ZERO;
        }
        return list.get(0).getBalance();
    }

    @SuppressWarnings("unchecked")
    public List<AccountAccResult> getBalance(Execer execer, List<String> addressList) throws Chain33Exception {
        return (List<AccountAccResult>) invoke(RpcMethod.GET_ACCOUNT_BALANCE,
                                               new TypeToken<RpcResult<List<AccountAccResult>>>() {
                                               }.getType(),
                                               new Pair<String, Object>("execer", execer.getName()),
                                               new Pair<String, Object>("addresses", addressList));
    }

    public BigInteger getTokenBalance(String tokenSymbol, String addr) throws Chain33Exception {
        List<String> addrs = new ArrayList<>();
        addrs.add(addr);
        List<AccountAccResult> list = getTokenBalance(tokenSymbol, addrs);
        if (list == null || list.isEmpty()) {
            return BigInteger.ZERO;
        }
        return list.get(0).getBalance();
    }

    public List<AccountAccResult> getTokenBalance(String tokenSymbol, String... addresses) throws Chain33Exception {
        return getTokenBalance(tokenSymbol, Arrays.asList(addresses));
    }


    /**
     * 查询地址token余额.
     */
    @SuppressWarnings("unchecked")
    public List<AccountAccResult> getTokenBalance(String tokenSymbol, List<String> addressList) throws Chain33Exception {
        return (List<AccountAccResult>) invoke(RpcMethod.GET_TOKEN_BALANCE,
                                               new TypeToken<RpcResult<List<AccountAccResult>>>() {
                                               }.getType(),
                                               new Pair<String, Object>("execer", Execer.TOKEN.getName()),
                                               new Pair<String, Object>("tokenSymbol", tokenSymbol),
                                               new Pair<String, Object>("addresses", addressList));
    }

    /**
     * 转账
     *
     * @param from   来源地址。
     * @param to     发送到地址。
     * @param amount 发送金额。
     * @param note   备注。
     * @return txhash
     */
    public String sendToAddress(String from, String to, BigInteger amount, String note) throws Chain33Exception {
        TxResult txResult = (TxResult) invoke(RpcMethod.SEND_TO_ADDRESS,
                                              new TypeToken<RpcResult<TxResult>>() {
                                              }.getType(),
                                              new Pair<String, Object>("from", from),
                                              new Pair<String, Object>("to", to),
                                              new Pair<String, Object>("amount", amount),
                                              new Pair<String, Object>("note", note),
                                              new Pair<String, Object>("isToken", false),
                                              new Pair<String, Object>("tokenSymbol", ""));

        return txResult == null ? "" : txResult.getHash();
    }

    /**
     * token转账.
     *
     * @param from
     * @param from:        来源地址。
     * @param to:          发送到地址。
     * @param amount:      发送金额。
     * @param note:        备注。
     * @param tokenSymbol: token标记符，最大长度是16个字符，且必须为大写字符。
     * @return txhash
     */
    public String sendToAddress(String from, String to, BigInteger amount, String note, String tokenSymbol) throws Chain33Exception {
        TxResult txResult = (TxResult) invoke(RpcMethod.SEND_TO_ADDRESS,
                                              new TypeToken<RpcResult<TxResult>>() {
                                              }.getType(),
                                              new Pair<String, Object>("from", from),
                                              new Pair<String, Object>("to", to),
                                              new Pair<String, Object>("amount", amount),
                                              new Pair<String, Object>("note", note),
                                              new Pair<String, Object>("isToken", true),
                                              new Pair<String, Object>("tokenSymbol", tokenSymbol));

        return txResult == null ? "" : txResult.getHash();
    }


    /**
     * 创建token
     *
     * @param token     token信息
     * @param tokenAppr Token审批人地址-配置的tokenApprs
     * @return txhash
     */
    public String createToken(Token token, String tokenAppr) throws Chain33Exception {
        //1.预创建token
        String unsignData = createRawTokenPreCreateTx(token.getName(), token.getSymbol(), token.getIntroduction(), token.getOwner(), token.getTotal(), token.getPrice());
        String sign = signRawTx(token.getOwner(), unsignData, "60s", 2);
        String txhash = sendTransaction(sign);
        logger.info("预创建token,tx:{}, token:{}", txhash, token);
        //2.查询预创建token信息
        List<Token> tokens = getTokens(Playload.STATUS_NO, true, false);
        boolean isExist = false;
        for (Token t : tokens) {
            if (token.getSymbol().equals(t.getSymbol()) && token.getOwner().equals(t.getOwner())) {
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            return null;
        }
        //3.完成创建token
        String unsignData2 = createRawTokenFinishTx(token.getSymbol(), token.getOwner());
        String sign2 = signRawTx(tokenAppr, unsignData2, "60s", 2);
        return sendTransaction(sign2);
    }

    /**
     * token增发
     *
     * @param symbol    token代码
     * @param amount    增发数量
     * @param tokenAppr Token审批人地址-配置的tokenApprs
     * @return txHash
     */
    /*
    public String tokenMint(String symbol, BigInteger amount, String tokenAppr) {
        String txhex = (String) invoke(RpcMethod.TOKEN_CREATE_MINT_TX, new TypeToken<RpcResult<String>>() {
                                       }.getType(),
                                       new Pair<String, Object>("symbol", symbol),
                                       new Pair<String, Object>("amount", amount)
        );
        String sign = signRawTx(tokenAppr, txhex, "60s", 2);

        return sendTransaction(sign);
    }*/

    /**
     * token销毁
     *
     * @param symbol    token代码
     * @param amount    增发数量
     * @param tokenAppr Token审批人地址-配置的tokenApprs
     * @return txHash
     */
    /*public String tokenBurn(String symbol, BigInteger amount, String tokenAppr) {
        String txhex = (String) invoke(RpcMethod.TOKEN_CREATE_BURN_TX, new TypeToken<RpcResult<String>>() {
                                       }.getType(),
                                       new Pair<String, Object>("symbol", symbol),
                                       new Pair<String, Object>("amount", amount)
        );
        String sign = signRawTx(tokenAppr, txhex);
        return sendTransaction(sign);
    }*/

    /**
     * 4.1.3.1 构造交易(平行链上会用到).
     *
     * @param txHex
     * @param txHex:   由上一步的createRawTx生成的交易再传入（比如，CreateRawTokenPreCreateTx：token预创建
     *                 ；CreateRawTokenFinishTx：token完成；CreateRawTransaction：转移token）
     * @param payAddr: 用于付费的地址，这个地址要在主链上存在，并且里面有比特元用于支付手续费。
     * @param Privkey： 对应于payAddr的私钥。如果payAddr已经导入到平行链，那么这个私钥可以不传（建议做法是在平行链上导入地址，
     *                 保证私钥安全）
     * @param expire:  超时时间
     * @return
     */
    public String createRawTransaction(String txHex, String payAddr, String Privkey, String expire) throws Chain33Exception {
        String hash = (String) invoke(RpcMethod.TOKEN_CREATE_RAW_TX,
                                      new TypeToken<RpcResult<String>>() {
                                      }.getType(),
                                      new Pair<String, Object>("txHex", txHex),
                                      new Pair<String, Object>("addr", payAddr),
                                      new Pair<String, Object>("Privkey", Privkey),
                                      new Pair<String, Object>("expire", expire)
        );
        return hash;
    }

    /**
     * 11.9 生成预创建token 的交易
     *
     * @param name         token的全名，最大长度是128个字符。
     * @param symbol       token标记符，最大长度是16个字符，且必须为大写字符。
     * @param introduction token介绍，最大长度为1024个字节。
     * @param ownerAddr    token拥有者地址
     * @param total        发行总量,需要乘以10的8次方，比如要发行100个币，需要100*1e8
     * @param price        发行该token愿意承担的费用
     *                     //@param fee          交易的手续费
     * @return 交易十六进制编码后的字符串
     */
    public String createRawTokenPreCreateTx(String name, String symbol, String introduction, String ownerAddr,
                                            BigInteger total, BigInteger price) throws Chain33Exception {

        String hex = (String) invoke(RpcMethod.TOKEN_CREATE_PRE_CREATE_TX,
                                     new TypeToken<RpcResult<String>>() {
                                     }.getType(),
                                     new Pair<String, Object>("name", name),
                                     new Pair<String, Object>("symbol", symbol),
                                     new Pair<String, Object>("introduction", introduction),
                                     new Pair<String, Object>("owner", ownerAddr),
                                     new Pair<String, Object>("total", total),
                                     new Pair<String, Object>("price", price)
        );
        return hex;
    }

    /**
     * 11.10 生成完成创建token 的交易(未签名).
     *
     * @param symbol:    token标记符，最大长度是16个字符，且必须为大写字符。
     * @param ownerAddr: token拥有者地址
     * @param fee:       交易的手续费
     * @return 交易十六进制编码后的字符串
     */
    public String createRawTokenFinishTx(BigInteger fee, String symbol, String ownerAddr) throws Chain33Exception {
        String txdata = (String) invoke(RpcMethod.TOKEN_CREATE_FINISH_TX,
                                        new TypeToken<RpcResult<String>>() {
                                        }.getType(),
                                        new Pair<String, Object>("fee", fee),
                                        new Pair<String, Object>("symbol", symbol),
                                        new Pair<String, Object>("owner", ownerAddr)
        );
        return txdata;
    }

    public String createRawTokenFinishTx(String symbol, String ownerAddr) throws Chain33Exception {
        String txdata = (String) invoke(RpcMethod.TOKEN_CREATE_FINISH_TX,
                                        new TypeToken<RpcResult<String>>() {
                                        }.getType(),
                                        new Pair<String, Object>("symbol", symbol),
                                        new Pair<String, Object>("owner", ownerAddr)
        );
        return txdata;
    }

    /**
     * 查询token
     *
     * @param status 0=预创建,1=已创建;
     * @return List<Token>
     */
    public List<Token> getTokens(int status, boolean queryAll, boolean symbolOnly) throws Chain33Exception {
        TokenResult tokenResult = (TokenResult) invoke(RpcMethod.QUERY, new TypeToken<RpcResult<TokenResult>>() {
                                                       }.getType(),
                                                       new Pair<String, Object>("execer", Execer.TOKEN.getName()),
                                                       new Pair<String, Object>("funcName", "GetTokens"),
                                                       new Pair<String, Object>("payload", new Playload(status, queryAll, symbolOnly))
        );
        return tokenResult == null ? null : tokenResult.getTokens();
    }

    /**
     * 查询指定token
     *
     * @return
     */
    public Token getTokenInfo(String symbol) throws Chain33Exception {
        return (Token) invoke(RpcMethod.QUERY, new TypeToken<RpcResult<Token>>() {
                              }.getType(),
                              new Pair<String, Object>("execer", Execer.TOKEN.getName()),
                              new Pair<String, Object>("funcName", "GetTokenInfo"),
                              new Pair<String, Object>("payload", new Playload(symbol))
        );
    }


    /**
     * 11.10 签名交易
     *
     * @param addr   与key可以只输入其一
     * @param expire 过期时间可输入如"300ms"，"-1.5h"或者"2h45m"的字符串，有效时间单位为"ns", "us" (or "µs"), "ms", "s", "m", "h"。
     * @param index  若是签名交易组，则为要签名的交易序号，从1开始，小于等于0则为签名组内全部交易
     * @param txhex  上一步CreateNoBalanceTransaction生成的tx
     * @param index  固定填写2(这里是一个交易组，第1笔none的交易已经用pay address签过名了，此处签index=2的交易)
     * @return sign
     */
    public String signRawTx(String addr, String txhex, String expire, int index) throws Chain33Exception {
        String signResult = (String) invoke(RpcMethod.SIGN_RAW_TRANSACTION,
                                            new TypeToken<RpcResult<String>>() {
                                            }.getType(),
                                            new Pair<String, Object>("addr", addr),
//                                            new Pair<String, Object>("privkey", ""),
                                            new Pair<String, Object>("txhex", txhex),
                                            new Pair<String, Object>("expire", expire),
                                            new Pair<String, Object>("index", index)
        );
        return signResult;
    }

    /**
     * 11.10 签名交易
     *
     * @param addr  与key可以只输入其一
     * @param txhex 上一步CreateNoBalanceTransaction生成的tx
     * @return sign
     */
    public String signRawTx(String addr, String txhex) throws Chain33Exception {
        return (String) invoke(RpcMethod.SIGN_RAW_TRANSACTION,
                               new TypeToken<RpcResult<String>>() {
                               }.getType(),
                               new Pair<String, Object>("addr", addr),
                               new Pair<String, Object>("txhex", txhex)
        );
    }

    /**
     * 4.1.2 发送签名后的交易
     *
     * @param unsignTx 未签名的tx
     * @param sign     sign:用私钥对unsigntx签名好的数据
     * @param pubkey   私钥对应的公钥
     * @param signType 签名类型
     * @return txHash
     */
    public String submitRawTransaction(String unsignTx, String sign, String pubkey, SignType signType) throws Chain33Exception {
        return (String) invoke(RpcMethod.SEND_RAW_TRANSACTION,
                               new TypeToken<RpcResult<String>>() {
                               }.getType(),
                               new Pair<String, Object>("unsignTx", unsignTx),
                               new Pair<String, Object>("sign", sign),
                               new Pair<String, Object>("pubkey", pubkey),
                               new Pair<String, Object>("ty", signType.getType())
        );
    }

    /**
     * 发送签名后的交易数据.
     *
     * @param data 交易数据
     * @return txHash
     */
    public String sendTransaction(String data) throws Chain33Exception {
        return (String) invoke(RpcMethod.SEND_TRANSACTION,
                               new TypeToken<RpcResult<String>>() {
                               }.getType(),
                               new Pair<String, Object>("data", data)
        );
    }


    /**
     * 4.2 根据哈希查询交易信息
     *
     * @param hash 交易hash
     */
    public QueryTransactionResult getTxByHash(String hash) throws Chain33Exception {
        if (StringUtil.isNotEmpty(hash) && hash.startsWith("0x")) {
            hash = HexUtil.removeHexHeader(hash);
        }
        return (QueryTransactionResult) invoke(RpcMethod.QUERY_TRANSACTION,
                                               new TypeToken<RpcResult<QueryTransactionResult>>() {
                                               }.getType(),
                                               new Pair<String, Object>("hash", hash));
    }

    /**
     * 4.4 根据哈希数组批量获取交易信息 GetTxByHashes
     *
     * @param hashIdList 交易ID列表
     * @return 交易结果对象列表
     */
    public List<QueryTransactionResult> getTxByHashes(Collection<String> hashIdList) throws Chain33Exception {
        /*if (hashIdList != null && !hashIdList.isEmpty()) {
            for (int i = 0; i < hashIdList.size(); i++) {
                String hash = hashIdList.get(i);
                hash = HexUtil.removeHexHeader(hash);
                hashIdList.set(i, hash);
            }
        }*/
        QueryTxsResult txResult = (QueryTxsResult) invoke(RpcMethod.GET_TX_BY_HASHES, new TypeToken<RpcResult<QueryTxsResult>>() {
        }.getType(), new Pair<String, Object>("hashes", hashIdList));
        return txResult.getTxs();
    }

    /**
     * @param addr      要查询的账户地址
     * @param flag      返回的数据条数
     * @param count     查询的方向；0：正向查询，区块高度从低到高；-1：反向查询；
     * @param direction 交易类型；0：所有涉及到addr的交易； 1：addr作为发送方； 2：addr作为接收方；
     * @param height    交易所在的block高度，-1：表示从最新的开始向后取；大于等于0的值，从具体的高度+具体index开始取
     * @param index     交易所在block中的索引，取值0—100000
     * @return
     */
    /*public List<QueryTransactionResult> getTxByAddr(String addr, int flag, int count, int direction, int height) {
        invoke(RpcMethod.GET_TX_BY_ADDR,
               new TypeToken<RpcResult>() {
               }.getType(),
               new Pair<String, Object>("addr", addr),
               new Pair<String, Object>("flag", flag),
               new Pair<String, Object>("count", count),
               new Pair<String, Object>("direction", direction),
               new Pair<String, Object>("height", height)
               //new Pair<String, Object>("index", index)
        );
        return null;
    }*/

    /**
     * 5.2 获取区间区块 GetBlocks
     *
     * @param start    区块开始高度
     * @param end      区块结束高度
     * @param isDetail 是否获取详情
     */
    public List<BlocksResult> getBlocks(Long start, Long end, boolean isDetail) throws Chain33Exception {
        BlockItemsResult blockItemsResult = (BlockItemsResult) invoke(RpcMethod.GET_BLOCKS,
                                                                      new TypeToken<RpcResult<BlockItemsResult>>() {
                                                                      }.getType(),
                                                                      new Pair<String, Object>("start", start),
                                                                      new Pair<String, Object>("end", end),
                                                                      new Pair<String, Object>("isDetail", isDetail)
        );

        return blockItemsResult.getItems();

    }

    /**
     * 5.3 获取最新的区块头 GetLastHeader
     *
     * @return 最新区块信息
     */
    public BlockResult getLastHeader() throws Chain33Exception {
        return (BlockResult) invoke(RpcMethod.GET_LAST_HEADER,
                                    new TypeToken<RpcResult<BlockResult>>() {
                                    }.getType());
    }

}