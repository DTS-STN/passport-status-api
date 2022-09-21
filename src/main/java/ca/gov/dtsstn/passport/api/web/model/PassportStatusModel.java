package ca.gov.dtsstn.passport.api.web.model;

import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import ca.gov.dtsstn.passport.api.service.domain.PassportStatus;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@SuppressWarnings({ "immutables", "serial" })
@Style(passAnnotations = { Relation.class })
@Relation(collectionRelation = "passportStatuses", itemRelation = "passportStatus")
public abstract class PassportStatusModel extends RepresentationModel<PassportStatusModel> implements PassportStatus {}
