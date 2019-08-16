package cn.chain33.javasdk.utils;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Random;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;

import com.google.protobuf.ByteString;

import cn.chain33.javasdk.model.Address;
import cn.chain33.javasdk.model.Signature;
import cn.chain33.javasdk.model.Transaction;
import cn.chain33.javasdk.model.enums.SignType;
import cn.chain33.javasdk.model.protobuf.TransactionProtoBuf;
import cn.chain33.javasdk.model.sm2.SM2;
import cn.chain33.javasdk.model.sm2.SM2.SM2Signature;
import cn.chain33.javasdk.model.sm2.SM2KeyPair;
import net.vrallev.java.ecc.Ecc25519Helper;

/**
 * 
 * @author logan 2018年5月14日
 */
public class TransactionUtil {
	
	private static final SignType DEFAULT_SIGNTYPE = SignType.SECP256K1;
	
	private static final long DEFAULT_FEE = 1000000;
	
	private static byte[] addrSeed = "address seed bytes for public key".getBytes();
	
	public static String toHexString(byte[] byteArr) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < byteArr.length; i++) {
			int b = byteArr[i] & 0xff;
			String hexString = Integer.toHexString(b);
			sb.append(hexString);
		}
		return sb.toString();
	}

	/**
	 * byte数组合并
	 * 
	 * @param byte_1
	 * @param byte_2
	 * @return
	 */
	public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
		byte[] byte_3 = new byte[byte_1.length + byte_2.length];
		System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
		System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
		return byte_3;
	}

	/**
	 * byte数组截取
	 * 
	 * @param byteArr
	 * @param start
	 * @param end
	 * @return
	 */
	public static byte[] subByteArr(byte[] byteArr, Integer start, Integer end) {
		Integer diff = end - start;
		byte[] byteTarget = new byte[diff];
		if (diff > byteArr.length) {
			diff = byteArr.length;
		}
		for (int i = 0; i < diff; i++) {
			byteTarget[i] = byteArr[i];
		}
		return byteTarget;
	}

	public static byte[] Sha256(byte[] sourceByte) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(sourceByte);
			byte byteData[] = md.digest();
			return byteData;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	public static Long getRandomNonce() {
		Random random = new Random(System.currentTimeMillis());
		return Math.abs(random.nextLong());
	}
	
	public static String createTx(String privateKey, String execer, String payLoad) {
		byte[] privateKeyBytes = HexUtil.fromHexString(privateKey);
		return createTx(privateKeyBytes, execer.getBytes(), payLoad.getBytes(), DEFAULT_SIGNTYPE,DEFAULT_FEE);
	}
	
	public static String createTx(String privateKey, String execer, String payLoad,long fee) {
		byte[] privateKeyBytes = HexUtil.fromHexString(privateKey);
		return createTx(privateKeyBytes, execer.getBytes(), payLoad.getBytes(), DEFAULT_SIGNTYPE,fee);
	}
	
	/**
	 * 
	 * @description create transaction,then @cn.chain33.javasdk.RpcClient.submitTransaction
	 * @param privateKey
	 * @param execer
	 * @param payLoad
	 * @param signType
	 * @param fee	num*100000000
	 * @return
	 *
	 */
	public static String createTx(byte[] privateKey, byte[] execer, byte[] payLoad, SignType signType,long fee) {
		return createTxMain(privateKey, execer, payLoad, signType,fee);
	}
	
	public static String createTxMain(byte[] privateKey, byte[] execer, byte[] payLoad, SignType signType,long fee) {
		if (signType == null)
			signType = DEFAULT_SIGNTYPE;

		// 如果没有私钥，创建私钥 privateKey =
		if (privateKey == null) {
			TransactionUtil.generatorPrivateKey();
		}
		Transaction transation = new Transaction();
		transation.setExecer(execer);
		transation.setPayload(payLoad);
		transation.setFee(fee);
		transation.setNonce(TransactionUtil.getRandomNonce());
		// 计算To
		String toAddress = getToAddress(execer);
		transation.setTo(toAddress);
		// 签名
		byte[] protobufData = encodeProtobuf(transation);

		sign(signType, protobufData, privateKey, transation);
		// 序列化
		byte[] encodeProtobufWithSign = encodeProtobufWithSign(transation);
		String transationHash = HexUtil.toHexString(encodeProtobufWithSign);
		return transationHash;
	}
	
	/**
	 * 计算to
	 * 
	 * @param execer
	 * @return
	 */
	private static String getToAddress(byte[] execer) {
		byte[] mergeredByte = TransactionUtil.byteMerger(addrSeed, execer);
		byte[] sha256_1 = TransactionUtil.Sha256(mergeredByte);
		for (int i = 0; i < sha256_1.length; i++) {
			sha256_1[i] = (byte) (sha256_1[i] & 0xff);
		}
		byte[] sha256_2 = TransactionUtil.Sha256(sha256_1);
		byte[] sha256_3 = TransactionUtil.Sha256(sha256_2);
		byte[] ripemd160 = TransactionUtil.ripemd160(sha256_3);
		Address address = new Address();
		address.setHash160(ripemd160);
		return addressToString(address);
	}
	
	/**
	 * 创建私钥和公钥
	 * 
	 * @return
	 */
	public static byte[] generatorPrivateKey() {
		int length = 0;
		byte[] privateKey;
		do {
			ECKeyPairGenerator gen = new ECKeyPairGenerator();
			SecureRandom secureRandom = new SecureRandom();
			X9ECParameters secnamecurves = SECNamedCurves.getByName("secp256k1");
			ECDomainParameters ecParams = new ECDomainParameters(secnamecurves.getCurve(), secnamecurves.getG(),
					secnamecurves.getN(), secnamecurves.getH());
			ECKeyGenerationParameters keyGenParam = new ECKeyGenerationParameters(ecParams, secureRandom);
			gen.init(keyGenParam);
			AsymmetricCipherKeyPair kp = gen.generateKeyPair();
			ECPrivateKeyParameters privatekey = (ECPrivateKeyParameters) kp.getPrivate();
			privateKey = privatekey.getD().toByteArray();
			length = privatekey.getD().toByteArray().length;
		} while (length != 32);
		return privateKey;
	}

	public static String generatorPrivateKeyString() {
		byte[] generatorPrivateKey = generatorPrivateKey();
		ECKey eckey = ECKey.fromPrivate(generatorPrivateKey);
		return eckey.getPrivateKeyAsHex();
	}
	
	/**
	 */
	public static byte[] encodeProtobuf(Transaction transaction) {
		TransactionProtoBuf.Transaction.Builder builder = TransactionProtoBuf.Transaction.newBuilder();

		builder.setExecer(ByteString.copyFrom(transaction.getExecer()));
		builder.setExpire(transaction.getExpire());
		builder.setFee(transaction.getFee());
		builder.setNonce(transaction.getNonce());
		builder.setPayload(ByteString.copyFrom(transaction.getPayload()));
		builder.setTo(transaction.getTo());
		TransactionProtoBuf.Transaction build = builder.build();
		byte[] byteArray = build.toByteArray();
		return byteArray;
	}

	public static byte[] encodeProtobufWithSign(Transaction transaction) {
		TransactionProtoBuf.Transaction.Builder builder = TransactionProtoBuf.Transaction.newBuilder();

		builder.setExecer(ByteString.copyFrom(transaction.getExecer()));
		builder.setExpire(transaction.getExpire());
		builder.setFee(transaction.getFee());
		builder.setNonce(transaction.getNonce());
		builder.setPayload(ByteString.copyFrom(transaction.getPayload()));
		builder.setTo(transaction.getTo());
		
		TransactionProtoBuf.Signature.Builder signatureBuilder = builder.getSignatureBuilder();
		signatureBuilder.setPubkey(ByteString.copyFrom(transaction.getSignature().getPubkey()));
		signatureBuilder.setTy(transaction.getSignature().getTy());
		signatureBuilder.setSignature(ByteString.copyFrom(transaction.getSignature().getSignature()));
		TransactionProtoBuf.Signature signatureBuild = signatureBuilder.build();
		builder.setSignature(signatureBuild);
		TransactionProtoBuf.Transaction build = builder.build();
		byte[] byteArray = build.toByteArray();
		return byteArray;
	}
	
	/**
	 * 签名
	 * 
	 * @param signType
	 *            签名类型
	 * @param data
	 *            加密数据
	 * @param privateKey
	 *            私钥
	 * @param transaction
	 *            交易
	 */
	private static void sign(SignType signType, byte[] data, byte[] privateKey, Transaction transaction) {
		switch (signType) {
		case SECP256K1: {
			Signature btcCoinSign = btcCoinSign(data, privateKey);
			transaction.setSignature(btcCoinSign);
		}
			break;
		case SM2: {
			SM2KeyPair keyPair = SM2.fromPrivateKey(privateKey);
			SM2Signature sign = SM2.sign(data, null, keyPair);
			try {
				byte[] derSignBytes = SM2.derByteStream(sign.getR(), sign.getS()).toByteArray();
				Signature signature = new Signature();
				signature.setPubkey(keyPair.getPublicKey().getEncoded(false));
				signature.setSignature(derSignBytes);
				signature.setTy(signType.getType());
				transaction.setSignature(signature);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
			break;
		case ED25519: {
			Ecc25519Helper helper1 = new Ecc25519Helper(privateKey);
			byte[] publicKey = helper1.getKeyHolder().getPublicKeySignature();
			byte[] sign = helper1.sign(data);
			Signature signature = new Signature();
			signature.setPubkey(publicKey);
			signature.setSignature(sign);
			signature.setTy(signType.getType());
			transaction.setSignature(signature);
		}
			break;
		default:
			break;
		}
	}
	
	public static String addressToString(Address address) {
		if (StringUtil.isEmpty(address.getEnc58Str())) {
			byte[] ad = new byte[25];
			ad[0] = address.getVersion();
			for (int i = 1; i < 21; i++) {
				ad[i] = address.getHash160()[i - 1];
			}
			byte[] checkSum = getAddressSh(ad);
			address.setCheckSum(checkSum);
			for (int i = 21, j = 0; i < 25; i++, j++) {
				ad[i] = checkSum[j];
			}
			String Enc58Str = Base58Util.encode(ad);
			address.setEnc58Str(Enc58Str);
		}
		return address.getEnc58Str();
	}
	
	/**
	 * 数据处理,sha256Twice
	 * 
	 * @param sourceByte
	 */
	private static byte[] getAddressSh(byte[] sourceByte) {
		byte[] subByteArr = TransactionUtil.subByteArr(sourceByte, 0, 21);
		byte[] sha256_1 = TransactionUtil.Sha256(subByteArr);
		byte[] sha256_2 = TransactionUtil.Sha256(sha256_1);
		return sha256_2;
	}
	
	public static byte[] ripemd160(byte[] sourceByte) {
		byte[] hash = Ripemd160Util.getHash(sourceByte);
		return hash;
	}
	
	private static Signature btcCoinSign(byte[] data, byte[] privateKey) {
		byte[] sha256 = TransactionUtil.Sha256(data);
		Sha256Hash sha256Hash = Sha256Hash.wrap(sha256);
		ECKey ecKey = ECKey.fromPrivate(privateKey);
		ECKey.ECDSASignature ecdsas = ecKey.sign(sha256Hash);
		byte[] signByte = ecdsas.encodeToDER();
		Signature signature = new Signature();
		signature.setPubkey(ecKey.getPubKey());
		signature.setSignature(signByte);
		signature.setTy(SignType.SECP256K1.getType());
		return signature;
	}
	
}
