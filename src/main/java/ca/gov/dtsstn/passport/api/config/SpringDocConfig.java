package ca.gov.dtsstn.passport.api.config;

import java.time.LocalDate;
import java.time.ZoneOffset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.gov.dtsstn.passport.api.config.properties.SwaggerUiProperties;
import ca.gov.dtsstn.passport.api.web.model.ErrorResponseModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutableErrorResponseModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutableIssueModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutableOperationOutcomeDate;
import ca.gov.dtsstn.passport.api.web.model.ImmutableOperationOutcomeModel;
import ca.gov.dtsstn.passport.api.web.model.ImmutableOperationOutcomeStatus;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
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

	public interface SchemaRefs {

		static final String ACCESS_DENIED_ERROR = "AccessDeniedError";
		static final String AUTHENTICATION_ERROR = "AuthenticationError";
		static final String BAD_REQUEST_ERROR = "BadRequestError";
		static final String INTERNAL_SERVER_ERROR = "InternalServerError";
		static final String RESOURCE_NOT_FOUND_ERROR = "ResourceNotFoundError";
		static final String UNPROCESSABLE_ENTITY_ERROR = "UnprocessableEntityError";

	}

	private static final Logger log = LoggerFactory.getLogger(SpringDocConfig.class);

	public static final String HTTP = "JSON-Web-Token";

	public static final String OAUTH = "Azure-Active-Directory";

	@Autowired ObjectMapper objectMapper;

	/**
	 * @param environment
	 * @param gitProperties
	 * @param swaggerUiProperties
	 * @return
	 */
	@Bean OpenApiCustomiser openApiCustomizer(Environment environment, GitProperties gitProperties, SwaggerUiProperties swaggerUiProperties) {
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

			final var accessDeniedErrorSchema = ModelConverters.getInstance().resolveAsResolvedSchema(new AnnotatedType(ErrorResponseModel.class));
			accessDeniedErrorSchema.schema.example(generateErrorExample("API-0403", "The server understands the request but refuses to authorize it.", "403", "Forbidden"));
			final var authenticationErrorSchema = ModelConverters.getInstance().resolveAsResolvedSchema(new AnnotatedType(ErrorResponseModel.class));
			authenticationErrorSchema.schema.example(generateErrorExample("API-0401", "The request lacks valid authentication credentials for the requested resource.", "401", "Unauthorized"));
			final var badRequestErrorSchema = ModelConverters.getInstance().resolveAsResolvedSchema(new AnnotatedType(ErrorResponseModel.class));
			badRequestErrorSchema.schema.example(generateErrorExample("API-0400", "The the server cannot or will not process the request due to something that is perceived to be a client error.", "$.CertificateApplication.CertificateApplicationApplicant.PersonName.PersonGivenName[:1]", "400", "Bad request"));
			final var internalServerErrorSchema = ModelConverters.getInstance().resolveAsResolvedSchema(new AnnotatedType(ErrorResponseModel.class));
			internalServerErrorSchema.schema.example(generateErrorExample("API-0500", "An unexpected error has occurred.", "500", "Internal server error"));
			final var resourceNotFoundErrorSchema = ModelConverters.getInstance().resolveAsResolvedSchema(new AnnotatedType(ErrorResponseModel.class));
			resourceNotFoundErrorSchema.schema.example(generateErrorExample("API-0404", "The requested resource was not found or the user does not have access to the resource.", "404", "Not found"));
			final var unprocessableEntityErrorSchema = ModelConverters.getInstance().resolveAsResolvedSchema(new AnnotatedType(ErrorResponseModel.class));
			unprocessableEntityErrorSchema.schema.example(generateErrorExample("API-0422", "The server understands the request, but is unable to process it.", "422", "Unprocessable entity"));

			openApi.getComponents().getSchemas().put(SchemaRefs.ACCESS_DENIED_ERROR, accessDeniedErrorSchema.schema);
			openApi.getComponents().getSchemas().put(SchemaRefs.AUTHENTICATION_ERROR, authenticationErrorSchema.schema);
			openApi.getComponents().getSchemas().put(SchemaRefs.BAD_REQUEST_ERROR, badRequestErrorSchema.schema);
			openApi.getComponents().getSchemas().put(SchemaRefs.INTERNAL_SERVER_ERROR, internalServerErrorSchema.schema);
			openApi.getComponents().getSchemas().put(SchemaRefs.RESOURCE_NOT_FOUND_ERROR, resourceNotFoundErrorSchema.schema);
			openApi.getComponents().getSchemas().put(SchemaRefs.UNPROCESSABLE_ENTITY_ERROR, unprocessableEntityErrorSchema.schema);
		};
	}

	protected String generateErrorExample(String issueCode, String issueDetails, String statusCode, String statusDescriptionText) {
		return generateErrorExample(issueCode, issueDetails, null, statusCode, statusDescriptionText);
	}

	protected String generateErrorExample(String issueCode, String issueDetails, String issueReferenceExpression, String statusCode, String statusDescriptionText) {
		try {
			final var dateTime = LocalDate.of(2000, 01, 01).atStartOfDay().toInstant(ZoneOffset.UTC);
			final var issueSeverityCode = "error";

			return objectMapper.writeValueAsString(ImmutableErrorResponseModel.builder()
				.operationOutcome(ImmutableOperationOutcomeModel.builder()
					.addIssues(ImmutableIssueModel.builder()
						.issueCode(issueCode)
						.issueDetails(issueDetails)
						.issueReferenceExpression(issueReferenceExpression)
						.issueSeverityCode(issueSeverityCode)
						.build())
					.operationOutcomeDate(ImmutableOperationOutcomeDate.builder()
						.dateTime(dateTime)
						.build())
					.operationOutcomeStatus(ImmutableOperationOutcomeStatus.builder()
						.statusCode(statusCode)
						.statusDescriptionText(statusDescriptionText)
						.build())
					.build())
				.build());
		}
		catch (final JsonProcessingException jsonProcessingException) {
			throw new RuntimeException(jsonProcessingException); // NOSONAR (use custom exception)
		}
	}

	protected String getApplicationVersion(GitProperties gitProperties) {
		return "v%s+%s".formatted(gitProperties.get("build.version"), gitProperties.getShortCommitId());
	}

}
