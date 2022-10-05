package ca.gov.dtsstn.passport.api.data;

import ca.gov.dtsstn.passport.api.data.document.HttpRequestDocument;

/**
 * MongoDB repository to persist {@link HttpRequestDocument} instances.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
public interface HttpRequestRepository extends ExtendedMongoRepository<HttpRequestDocument, String> {}
