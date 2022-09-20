package ca.gov.dtsstn.passport.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@SpringBootTest
class ApplicationTests {

	@Test void contextLoads(ApplicationContext applicationContext) {
		assertThat(applicationContext).isNotNull();
	}

}
