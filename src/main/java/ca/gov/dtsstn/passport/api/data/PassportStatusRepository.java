package ca.gov.dtsstn.passport.api.data;

import ca.gov.dtsstn.passport.api.data.document.PassportStatusDocument;

/**
 * MongoDB repository to persist {@link HttpTraceDocuPassportStatusDocumentment} instances.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
public interface PassportStatusRepository extends ExtendedMongoRepository<PassportStatusDocument, String> {}
