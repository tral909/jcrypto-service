package io.github.tral909.jcrypto_service.backend.logic;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class SignatureService {

	private final static String PRIVATE_KEY = "MIIBSwIBADCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoEFgIUH63HIQkL1yQw9FVN+/WjK6BoVyk=";
	private final static String PUBLIC_KEY = "MIIBtzCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoDgYQAAoGAOJS1cyf/XnlshzQhM86ccX34DAa4DbHzIuRbfBbaIHTlToKCJrsE0LiMsSzHLbpBu0DGashOAexLB0B25nMPQG6JHMmPs9g325Y8bQN/IMUTy+lRP5o1NWX1+LOqreRN1nXNUZVKZ9ELM+ZuD8+7aP/UwX2izU5kPv0e+Ll0kN4=";

	@SneakyThrows
	public byte[] sign(InputStream dataStream) {
		Assert.notNull(dataStream, "dataStream is null");

		// init key pair
//		SecureRandom secureRandom2 = new SecureRandom();
//		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA");
//		keyPairGen.initialize(1024);
//		KeyPair keyPair = keyPairGen.generateKeyPair();

		byte[] data = IOUtils.toByteArray(dataStream);
		byte[] prKeyBytes = Base64.getDecoder().decode(PRIVATE_KEY);

		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(prKeyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("DSA");
		PrivateKey prKey = keyFactory.generatePrivate(keySpec);

		Signature signature = Signature.getInstance("SHA256WithDSA");
		signature.initSign(prKey);
		signature.update(data);
		return signature.sign();
	}

	@SneakyThrows
	public boolean verifySign(InputStream dataStream, InputStream signStream) {
		Assert.notNull(dataStream, "dataStream is null");
		Assert.notNull(signStream, "signStream is null");

		// init key pair
//		SecureRandom secureRandom2 = new SecureRandom();
//		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA");
//		keyPairGen.initialize(1024);
//		KeyPair keyPair = keyPairGen.generateKeyPair();

		byte[] data = IOUtils.toByteArray(dataStream);
		byte[] sign = IOUtils.toByteArray(signStream);

		byte[] pubKeyBytes = Base64.getDecoder().decode(PUBLIC_KEY);

		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubKeyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("DSA");
		PublicKey pubKey = keyFactory.generatePublic(keySpec);

		Signature signature = Signature.getInstance("SHA256WithDSA");
		signature.initVerify(pubKey);
		signature.update(data);
		return signature.verify(sign);
	}

}
