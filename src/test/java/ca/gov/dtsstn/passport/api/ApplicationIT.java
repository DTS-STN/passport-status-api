package ca.gov.dtsstn.passport.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApplicationIT {

	@Autowired MockMvc mvc;

	@Test void contextLoads(ApplicationContext applicationContext) {
		assertThat(applicationContext).isNotNull();
	}

	@Test void healthEndpointReturnsStatusAndComponents() throws Exception {
		mvc.perform(get("/actuator/health"))
			.andExpect(jsonPath("$.status").exists())
			.andExpect(jsonPath("$.components").exists());
	}

	@Test void actuatorLinksIncludeHealthHref() throws Exception {
		mvc.perform(get("/actuator"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$._links.health.href").isNotEmpty());
	}

	@Test void openApiDocumentIsGenerated() throws Exception {
		mvc.perform(get("/v3/api-docs"))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith("application/json"))
			.andExpect(jsonPath("$.info.title").value("Passport Status API – OpenAPI 3.0"))
			.andExpect(jsonPath("$.paths['/api/v1/passport-statuses']").exists());
	}

}
