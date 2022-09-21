package ca.gov.dtsstn.passport.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import ca.gov.dtsstn.passport.api.config.WebMvcConfig;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@ActiveProfiles("test")
@WebMvcTest({ WebMvcConfig.class })
class WebMvcIT {

	@Autowired MockMvc mvc;

	@Test void testSwaggerRedirect() throws Exception {
		mvc.perform(get("/"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/swagger-ui.html"));
	}

}
