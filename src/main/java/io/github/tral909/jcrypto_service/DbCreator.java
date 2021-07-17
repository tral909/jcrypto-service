package io.github.tral909.jcrypto_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

/** Приложение для liquibaseDiff, liquibaseDiffFormat, создает референсную БД по модели */
@Profile("initdb")
@Configuration
public class DbCreator {
	private static final Logger LOGGER = LoggerFactory.getLogger(DbCreator.class);


	public static void main(String[] args) {
		System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "initdb");
		System.setProperty("eureka.client.enabled", "false");
		// отключаем embedded Tomcat
		System.setProperty("spring.main.web_environment", "false");
		System.setProperty("spring.main.web-application-type", "none");
		// отключаем автоконфигурацию redis
		System.setProperty("spring.session.store-type", "none");
		// ускоряем завершение работы дочерних потоков
		System.setProperty("spring.task.execution.pool.keep-alive", "3s");

		PropertySource<?> ps = new SimpleCommandLinePropertySource(args);
		setSystemProperty(ps, "url", "jdbc:postgresql://localhost/jcrypto-ref");
		setSystemProperty(ps, "username", "postgres");
		setSystemProperty(ps, "password", "12345");

		LOGGER.info("url=" + System.getProperty("url"));
		SpringApplication.run(DbCreator.class, args);
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl(System.getProperty("url"));
		dataSource.setUsername(System.getProperty("username"));
		dataSource.setPassword(System.getProperty("password"));
		return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource);
		em.setPackagesToScan("io.github.tral909.jcrypto_service.backend.model");
		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		Properties properties = new Properties();
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL92Dialect");
		properties.setProperty("hibernate.hbm2ddl.auto", "create");
		properties.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");
		properties.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class);
		em.setJpaProperties(properties);
		return em;
	}

	private static void setSystemProperty(PropertySource<?> ps, String key, String defaultValue) {
		String value = (String) ps.getProperty(key);
		if (value == null) {
			System.setProperty(key, defaultValue);
		} else {
			System.setProperty(key, value);
		}
	}
}