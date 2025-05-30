package ca.gov.dtsstn.passport.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import ca.gov.dtsstn.passport.api.config.WebMvcConfig;
import ca.gov.dtsstn.passport.api.config.WebSecurityConfig;
import ca.gov.dtsstn.passport.api.web.AuthenticationErrorHandler;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@ActiveProfiles("test")
@WebMvcTest({ WebMvcConfig.class, WebSecurityConfig.class })
class WebMvcIT {

	@Autowired MockMvc mvc;

	@MockitoBean AuthenticationErrorHandler authenticationErrorController;

	@Test void testSwaggerRedirect() throws Exception {
		mvc.perform(get("/"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/swagger-ui/index.html"));
	}

}
