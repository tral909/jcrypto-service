package io.github.tral909.jcrypto_service.backend.service;

import io.github.tral909.jcrypto_service.backend.dao.PrivateKeyRepository;
import io.github.tral909.jcrypto_service.backend.dto.PrivateKeyOutDto;
import io.github.tral909.jcrypto_service.backend.model.PrivateKey;
import io.github.tral909.jcrypto_service.backend.model.PublicKey;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class KeysService {
	@Autowired
	private PrivateKeyRepository privateKeyRepository;

	public List<PrivateKeyOutDto> getPrivateKeys() {
		List<PrivateKeyOutDto> keys = new ArrayList<>();
		privateKeyRepository.findAll().forEach(k -> keys.add(new PrivateKeyOutDto(k.getId(), k.getName())));
		return keys;
	}

	@SneakyThrows
	public Long generatePrivateKey(String name) {
		SecureRandom secureRandom = new SecureRandom();
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA");
		keyPairGen.initialize(1024, secureRandom);
		KeyPair keyPair = keyPairGen.generateKeyPair();

		java.security.PrivateKey generatedPrKey = keyPair.getPrivate();
		String generatedPrKeyBase64 = Base64.getEncoder().encodeToString(generatedPrKey.getEncoded());

		java.security.PublicKey generatedPubKey = keyPair.getPublic();
		String generatedPubKeyBase64 = Base64.getEncoder().encodeToString(generatedPubKey.getEncoded());

		PrivateKey prKey = new PrivateKey();
		prKey.setName(name);
		prKey.setKey(generatedPrKeyBase64);

		PublicKey pubKey = new PublicKey();
		pubKey.setKey(generatedPubKeyBase64);

		prKey.setPublicKey(pubKey);
		return privateKeyRepository.save(prKey).getId();
	}
}
