package ca.gov.dtsstn.passport.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import ca.gov.dtsstn.passport.api.config.WebMvcConfig;
import ca.gov.dtsstn.passport.api.config.WebSecurityConfig;
import ca.gov.dtsstn.passport.api.service.PassportStatusJmsService;
import ca.gov.dtsstn.passport.api.service.PassportStatusService;
import ca.gov.dtsstn.passport.api.web.AuthenticationErrorHandler;
import ca.gov.dtsstn.passport.api.web.PassportStatusController;
import ca.gov.dtsstn.passport.api.web.model.assembler.GetCertificateApplicationRepresentationModelAssembler;
import ca.gov.dtsstn.passport.api.web.model.mapper.CertificateApplicationModelMapper;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@ActiveProfiles("test")
@WebMvcTest({ WebMvcConfig.class, WebSecurityConfig.class, PassportStatusController.class })
class WebMvcIT {

	@Autowired MockMvc mvc;

	@MockitoBean AuthenticationErrorHandler authenticationErrorController;
	@MockitoBean GetCertificateApplicationRepresentationModelAssembler assembler;
	@MockitoBean CertificateApplicationModelMapper mapper;
	@MockitoBean PassportStatusJmsService passportStatusJmsService;
	@MockitoBean PassportStatusService passportStatusService;
	@MockitoSpyBean SpringValidatorAdapter validator;

	@Test void testSwaggerRedirect() throws Exception {
		mvc.perform(get("/"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/swagger-ui/index.html"));
	}

	@Test
	@WithMockUser(authorities = "PassportStatus.Write.All")
	void testPassportStatusPostDeserializesRequest() throws Exception {
		doReturn(Set.of()).when(validator).validate(any());

		mvc.perform(post("/api/v1/passport-statuses")
			.contentType(MediaType.APPLICATION_JSON)
			.content("""
				{"CertificateApplication": {}}
				"""))
			.andExpect(status().isAccepted());

		verify(mapper).toDomain(any());
		verify(passportStatusJmsService).send(null);
	}

}
