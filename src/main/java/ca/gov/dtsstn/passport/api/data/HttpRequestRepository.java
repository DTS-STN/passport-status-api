package ca.gov.dtsstn.passport.api.data;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.gov.dtsstn.passport.api.data.entity.HttpRequestEntity;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
public interface HttpRequestRepository extends JpaRepository<HttpRequestEntity, String> {}
