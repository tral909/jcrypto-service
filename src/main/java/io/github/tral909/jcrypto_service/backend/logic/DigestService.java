package io.github.tral909.jcrypto_service.backend.logic;

import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;

@Service
public class DigestService {

    @SneakyThrows
    public String digest(String payload, String algorithm) {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(payload.getBytes());
        return Hex.encodeHexString(md.digest()).toUpperCase();
    }
}
