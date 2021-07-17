package io.github.tral909.jcrypto_service.backend.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "private_key")
public class PrivateKey {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	/** Ключ в base64 формате */
	@Column(length = 2048, nullable = false)
	private String key;

	@OneToOne(cascade = CascadeType.PERSIST)
	private PublicKey publicKey;
}
