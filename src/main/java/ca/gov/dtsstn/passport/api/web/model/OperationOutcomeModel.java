package ca.gov.dtsstn.passport.api.web.model;

import java.io.Serializable;
import java.util.List;

import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@Schema(name = "OperationOutcome")
public interface OperationOutcomeModel extends Serializable {

	@JsonProperty("Issue")
	@Schema(required = true)
	List<IssueModel> getIssues();

	@Default
	@Schema(required = true)
	@JsonProperty("OperationOutcome")
	default OperationOutcomeDateModel getOperationOutcomeDate() {
		return ImmutableOperationOutcomeDateModel.builder().build();
	}

	@Schema(required = true)
	@JsonProperty("OperationOutcomeStatus")
	OperationOutcomeStatusModel getOperationOutcomeStatus();

}
