package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "OperationOutcome")
@Style(validationMethod = ValidationMethod.NONE)
public interface OperationOutcomeModel extends Serializable {

	@JsonProperty("Issue")
	List<IssueModel> getIssues();

	@Default
	@JsonProperty("OperationOutcome")
	default OperationOutcomeDateModel getOperationOutcomeDate() {
		return ImmutableOperationOutcomeDate.builder().dateTime(Instant.now()).build();
	}

	@JsonProperty("OperationOutcomeStatus")
	OperationOutcomeStatusModel getOperationOutcomeStatus();

}
