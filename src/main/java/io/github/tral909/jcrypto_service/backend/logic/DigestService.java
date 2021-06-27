package io.github.tral909.jcrypto_service.backend.logic;

import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.security.MessageDigest;

@Service
public class DigestService {

    @SneakyThrows
    public String digest(String payload, String algorithm) {
        Assert.notNull(payload, "payload is null");
        Assert.notNull(algorithm, "algorithm is null");

        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(payload.getBytes());
        return Hex.encodeHexString(md.digest()).toUpperCase();
    }
}
