package ca.gov.dtsstn.passport.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import ca.gov.dtsstn.passport.api.config.WebMvcConfig;
import ca.gov.dtsstn.passport.api.config.WebSecurityConfig;
import ca.gov.dtsstn.passport.api.web.AuthenticationErrorController;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@ActiveProfiles("test")
@WebMvcTest({ WebMvcConfig.class, WebSecurityConfig.class })
class WebMvcIT {

	@Autowired MockMvc mvc;

	@MockBean AuthenticationErrorController authenticationErrorController;

	@Test void testSwaggerRedirect() throws Exception {
		mvc.perform(get("/"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/swagger-ui.html"));
	}

}
