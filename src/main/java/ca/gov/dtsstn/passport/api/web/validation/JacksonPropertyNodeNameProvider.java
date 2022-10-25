package ca.gov.dtsstn.passport.api.web.validation;

import org.hibernate.validator.spi.nodenameprovider.JavaBeanProperty;
import org.hibernate.validator.spi.nodenameprovider.Property;
import org.hibernate.validator.spi.nodenameprovider.PropertyNodeNameProvider;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;

/**
 * A {@link PropertyNodeNameProvider} that can resolve names via Jackson's {@code @JsonProperty} annotations.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 * @see https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#section-property-node-name-provider
 */
@Component
public class JacksonPropertyNodeNameProvider implements PropertyNodeNameProvider {

	private final ObjectMapper objectMapper;

	public JacksonPropertyNodeNameProvider(ObjectMapper objectMapper) {
		Assert.notNull(objectMapper, "objectMapper is required; it must not be null");
		this.objectMapper = objectMapper;
	}

	@Override
	public String getName(Property property) {
		return ClassUtils.isAssignableValue(JavaBeanProperty.class, property)
			? getJavaBeanPropertyName((JavaBeanProperty) property) : property.getName();
	}

	protected String getJavaBeanPropertyName(JavaBeanProperty javaBeanProperty) {
		final var javaType = objectMapper.constructType(javaBeanProperty.getDeclaringClass());
		final var beanDescription = objectMapper.getSerializationConfig().introspect(javaType);

		return beanDescription.findProperties().stream()
			.filter(beanPropertyDefinition -> beanPropertyDefinition.getInternalName().equals(javaBeanProperty.getName()))
			.map(BeanPropertyDefinition::getName)
			.findFirst().orElse(javaBeanProperty.getName());
	}

}