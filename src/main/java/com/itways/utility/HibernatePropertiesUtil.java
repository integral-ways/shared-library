package com.itways.utility;

import java.util.Properties;

/**
 * @author ssatwa
 * @date 08-04-2025
 */
public class HibernatePropertiesUtil {

	public static Properties defaultJpaProperties() {
		var props = new Properties();
		props.put("hibernate.physical_naming_strategy",
				"org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
		props.put("hibernate.implicit_naming_strategy",
				"org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy");
		return props;
	}
}
