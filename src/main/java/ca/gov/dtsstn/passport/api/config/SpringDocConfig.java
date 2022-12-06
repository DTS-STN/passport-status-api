package ca.gov.dtsstn.passport.api.config;

import java.time.LocalDate;
import java.time.ZoneOffset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.gov.dtsstn.passport.api.config.properties.SwaggerUiProperties;
import ca.gov.dtsstn.passport.api.web.model.ImmutableErrorResponseModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutableIssueModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutableOperationOutcomeDateModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutableOperationOutcomeModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutableOperationOutcomeStatusModel;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Configuration
public class SpringDocConfig {

	public final class ExampleRefs {

		private ExampleRefs() { /* constants class */ }

		public static final String ACCESS_DENIED_ERROR = "AccessDeniedError";
		public static final String AUTHENTICATION_ERROR = "AuthenticationError";
		public static final String BAD_REQUEST_ERROR = "BadRequestError";
		public static final String INTERNAL_SERVER_ERROR = "InternalServerError";
		public static final String RESOURCE_NOT_FOUND_ERROR = "ResourceNotFoundError";
		public static final String UNPROCESSABLE_ENTITY_ERROR = "UnprocessableEntityError";
	}

	private static final Logger log = LoggerFactory.getLogger(SpringDocConfig.class);

	public static final String HTTP = "JSON-Web-Token";

	public static final String OAUTH = "Azure-Active-Directory";

	@Autowired ObjectMapper objectMapper;

	@Bean OpenApiCustomizer openApiCustomizer(Environment environment, GitProperties gitProperties, SwaggerUiProperties swaggerUiProperties) {
		log.info("Creating 'openApiCustomizer' bean");

		return openApi -> {
			openApi.getInfo()
				.title(swaggerUiProperties.applicationName())
				.contact(new Contact()
					.name(swaggerUiProperties.contactName())
					.url(swaggerUiProperties.contactUrl()))
				.description(swaggerUiProperties.description())
				.termsOfService(swaggerUiProperties.tosUrl())
				.version(getApplicationVersion(gitProperties));

			openApi.getComponents()
				.addSecuritySchemes(HTTP, new SecurityScheme()
					.type(Type.HTTP)
					.description(swaggerUiProperties.authentication().http().description())
					.scheme("bearer")
					.bearerFormat("JWT"));

			openApi.getComponents()
				.addSecuritySchemes(OAUTH, new SecurityScheme()
					.type(Type.OAUTH2)
					.description(swaggerUiProperties.authentication().oauth().description())
					.flows(new OAuthFlows()
						.authorizationCode(new OAuthFlow()
							.authorizationUrl(swaggerUiProperties.authentication().oauth().authorizationUrl())
							.refreshUrl(swaggerUiProperties.authentication().oauth().tokenUrl())
							.scopes(new Scopes().addString(swaggerUiProperties.authentication().oauth().clientId() + "/.default", "Default scope"))
							.tokenUrl(swaggerUiProperties.authentication().oauth().tokenUrl()))));

			/*
			 * Customize the error responses to match what the API will actually return...
			 */

			openApi.getComponents().addExamples(ExampleRefs.ACCESS_DENIED_ERROR, generateExample(null, "API-0403", "The server understands the request but refuses to authorize it.", null, "403", "Forbidden"));
			openApi.getComponents().addExamples(ExampleRefs.AUTHENTICATION_ERROR, generateExample(null, "API-0401", "The request lacks valid authentication credentials for the requested resource.", null, "401", "Unauthorized"));
			openApi.getComponents().addExamples(ExampleRefs.BAD_REQUEST_ERROR, generateExample(null, "API-0400", "The the server cannot or will not process the request due to something that is perceived to be a client error.", "$.CertificateApplication.CertificateApplicationApplicant.PersonName.PersonGivenName[0]", "400", "Bad request"));
			openApi.getComponents().addExamples(ExampleRefs.INTERNAL_SERVER_ERROR, generateExample("00000000-0000-0000-0000-000000000000", "API-0500", "An unexpected error has occurred.", null, "500", "Internal server error"));
			openApi.getComponents().addExamples(ExampleRefs.RESOURCE_NOT_FOUND_ERROR, generateExample(null, "API-0404", "The requested resource was not found or the user does not have access to the resource.", null, "404", "Not found"));
			openApi.getComponents().addExamples(ExampleRefs.UNPROCESSABLE_ENTITY_ERROR, generateExample(null, "API-0422", "The server understands the request, but is unable to process it.", null, "422", "Unprocessable entity"));
		};
	}

	protected Example generateExample(String correlationId, String issueCode, String issueDetails, @Nullable String issueReferenceExpression, String statusCode, String statusDescriptionText) {
		try {
			final var dateTime = LocalDate.of(2000, 01, 01).atStartOfDay().toInstant(ZoneOffset.UTC);
			final var issueSeverityCode = "error";

			return new Example().value(objectMapper.writeValueAsString(ImmutableErrorResponseModel.builder()
				.correlationId(correlationId)
				.operationOutcome(ImmutableOperationOutcomeModel.builder()
					.addIssues(ImmutableIssueModel.builder()
						.issueCode(issueCode)
						.issueDetails(issueDetails)
						.issueReferenceExpression(issueReferenceExpression)
						.issueSeverityCode(issueSeverityCode)
						.build())
					.operationOutcomeDate(ImmutableOperationOutcomeDateModel.builder()
						.dateTime(dateTime)
						.build())
					.operationOutcomeStatus(ImmutableOperationOutcomeStatusModel.builder()
						.statusCode(statusCode)
						.statusDescriptionText(statusDescriptionText)
						.build())
					.build())
				.build()));
		}
		catch (final JsonProcessingException jsonProcessingException) {
			throw new RuntimeException(jsonProcessingException); // NOSONAR (use custom exception)
		}
	}

	protected String getApplicationVersion(GitProperties gitProperties) {
		return "v%s+%s".formatted(gitProperties.get("build.version"), gitProperties.getShortCommitId());
	}

}
