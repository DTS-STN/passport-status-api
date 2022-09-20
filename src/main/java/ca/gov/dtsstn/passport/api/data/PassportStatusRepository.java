package ca.gov.dtsstn.passport.api.data;

import org.springframework.data.mongodb.repository.MongoRepository;

import ca.gov.dtsstn.passport.api.data.document.PassportStatusDocument;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
public interface PassportStatusRepository extends MongoRepository<PassportStatusDocument, String> {}
