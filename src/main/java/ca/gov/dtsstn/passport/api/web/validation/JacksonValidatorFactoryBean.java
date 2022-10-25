package ca.gov.dtsstn.passport.api.web.validation;

import javax.validation.Configuration;
import javax.validation.ConstraintViolation;

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
public class JacksonValidatorFactoryBean extends LocalValidatorFactoryBean {

	private static final Logger log = LoggerFactory.getLogger(JacksonValidatorFactoryBean.class);

	private final JacksonPropertyNodeNameProvider jacksonPropertyNodeNameProvider;

	public JacksonValidatorFactoryBean(ApplicationContext applicationContext, JacksonPropertyNodeNameProvider jacksonPropertyNodeNameProvider) {
		Assert.notNull(applicationContext, "applicationContext is required; it must not be null");
		Assert.notNull(jacksonPropertyNodeNameProvider, "jacksonPropertyNodeNameProvider is required; it must not be null");
		super.setMessageInterpolator(new MessageInterpolatorFactory(applicationContext).getObject());
		this.jacksonPropertyNodeNameProvider = jacksonPropertyNodeNameProvider;
	}

	@Override
	protected void postProcessConfiguration(Configuration<?> configuration) {
		if (ClassUtils.isAssignableValue(HibernateValidatorConfiguration.class, configuration)) {
			final var hibernateValidatorConfiguration = (HibernateValidatorConfiguration) configuration;
			hibernateValidatorConfiguration.propertyNodeNameProvider(jacksonPropertyNodeNameProvider);
		}
		else {
			log.warn("Bean validator is not an instance of org.hibernate.validator.HibernateValidatorConfiguration");
		}
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
