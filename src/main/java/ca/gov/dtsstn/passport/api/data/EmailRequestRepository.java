package ca.gov.dtsstn.passport.api.data;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gov.dtsstn.passport.api.data.entity.EmailRequestEntity;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
public interface EmailRequestRepository extends JpaRepository<EmailRequestEntity, String> {}
