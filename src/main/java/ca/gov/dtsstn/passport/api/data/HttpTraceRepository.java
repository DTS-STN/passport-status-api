package ca.gov.dtsstn.passport.api.data;

import ca.gov.dtsstn.passport.api.data.document.HttpTraceDocument;

/**
 * MongoDB repository to persist {@link HttpTraceDocument} instances.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
public interface HttpTraceRepository extends ExtendedMongoRepository<HttpTraceDocument, String> {}
