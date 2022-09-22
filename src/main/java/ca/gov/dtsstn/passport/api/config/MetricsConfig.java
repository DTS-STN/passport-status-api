package ca.gov.dtsstn.passport.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;

/**
 * @author Greg Baker <gregory.j.baker@hrsdc-rhdcc.gc.ca>
 */
@Configuration
public class MetricsConfig {

	private static final Logger log = LoggerFactory.getLogger(MetricsConfig.class);

	@Bean CountedAspect countedAspect(MeterRegistry meterRegstry) {
		log.info("Creating 'countedAspect' bean");
		return new CountedAspect(meterRegstry);
	}

	@Bean TimedAspect timedAspect(MeterRegistry meterRegstry) {
		log.info("Creating 'timedAspect' bean");
		return new TimedAspect(meterRegstry);
	}

}
