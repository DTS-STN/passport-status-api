package ca.gov.dtsstn.passport.api.web.validation;

import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.constraints.PastOrPresent;

import org.hibernate.validator.HibernateValidatorConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.boot.validation.MessageInterpolatorFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * A custom {@link LocalValidatorFactoryBean} that works with {@link JacksonPropertyNodeNameProvider}.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
public class CustomValidatorFactoryBean extends LocalValidatorFactoryBean {

	private static final Logger log = LoggerFactory.getLogger(CustomValidatorFactoryBean.class);

	private final JacksonPropertyNodeNameProvider jacksonPropertyNodeNameProvider;

	public CustomValidatorFactoryBean(ApplicationContext applicationContext, JacksonPropertyNodeNameProvider jacksonPropertyNodeNameProvider) {
		Assert.notNull(applicationContext, "applicationContext is required; it must not be null");
		Assert.notNull(jacksonPropertyNodeNameProvider, "jacksonPropertyNodeNameProvider is required; it must not be null");
		super.setMessageInterpolator(new MessageInterpolatorFactory(applicationContext).getObject());
		this.jacksonPropertyNodeNameProvider = jacksonPropertyNodeNameProvider;
	}

	@Override
	protected void postProcessConfiguration(Configuration<?> configuration) {
		Assert.isTrue(ClassUtils.isAssignableValue(HibernateValidatorConfiguration.class, configuration), "Validation configuration is not an instance of org.hibernate.validator.HibernateValidatorConfiguration");

		log.debug("Registering Jackson based PropertyNodeNameProvider with Hibernate validator");
		final var hibernateValidatorConfiguration = (HibernateValidatorConfiguration) configuration;
		hibernateValidatorConfiguration.propertyNodeNameProvider(jacksonPropertyNodeNameProvider);

		log.debug("Registering String based PastOrPresentValidator with Hibernate validator");
		final var constraintMapping = hibernateValidatorConfiguration.createConstraintMapping();
		constraintMapping.constraintDefinition(PastOrPresent.class).validatedBy(PastOrPresentValidator.class);
		hibernateValidatorConfiguration.addMapping(constraintMapping);
	}

	@Override
	protected Object getRejectedValue(String field, ConstraintViolation<Object> violation, BindingResult bindingResult) {
		try {
			// when a JacksonPropertyNodeNameProvider is configured, Spring can throw here 🤷
			return super.getRejectedValue(field, violation, bindingResult);
		}
		catch (final NotReadablePropertyException ex) {
			return violation.getInvalidValue();
		}
	}

}
