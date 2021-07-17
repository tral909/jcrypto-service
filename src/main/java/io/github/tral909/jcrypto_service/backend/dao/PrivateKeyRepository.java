package io.github.tral909.jcrypto_service.backend.dao;

import io.github.tral909.jcrypto_service.backend.model.PrivateKey;
import org.springframework.data.repository.CrudRepository;

public interface PrivateKeyRepository extends CrudRepository<PrivateKey, Long> {
}
