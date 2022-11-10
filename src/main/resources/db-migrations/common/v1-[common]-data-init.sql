INSERT INTO status_code
  (id, code, cdo_code, description, is_active, created_by, created_date, last_modified_by, last_modified_date)
VALUES
  ('57fe687e-50a6-411f-af63-2a659622127d', 'FILE_BEING_PROCESSED', '1', 'File being processed', true, 'flyway-community-edition', CURRENT_TIMESTAMP, 'flyway-community-edition', CURRENT_TIMESTAMP),
  ('c4c3d083-39f6-4cae-bb18-1e65dd2f60a3', 'PASSPORT_ISSUED_READY_FOR_PICKUP', '2', 'Passport Issued and Ready for Pickup', true, 'flyway-community-edition', CURRENT_TIMESTAMP, 'flyway-community-edition', CURRENT_TIMESTAMP),
  ('20edcb59-b217-4ed8-8378-f78184f634f2', 'PASSPORT_ISSUED_SHIPPING_CANADA_POST', '3', 'Passport Issued and Shipping Canada Post', true, 'flyway-community-edition', CURRENT_TIMESTAMP, 'flyway-community-edition', CURRENT_TIMESTAMP),
  ('af0857c3-467a-4770-9b2b-964f1d252107', 'PASSPORT_ISSUED_SHIPPING_FEDEX', '4', 'Passport Issued and Shipping Fedex', true, 'flyway-community-edition', CURRENT_TIMESTAMP, 'flyway-community-edition', CURRENT_TIMESTAMP),
  ('933113a5-e219-4a86-9753-fac20deeb85b', 'NOT_ACCEPTABLE_FOR_PROCESSING', '5', 'Not acceptable for processing', true, 'flyway-community-edition', CURRENT_TIMESTAMP, 'flyway-community-edition', CURRENT_TIMESTAMP),
  ('637c1ebd-cf97-4ed4-83d3-fd0370f62b5e', 'APPLICATION_NO_LONGER_MEETS_CRITERIA', '99', 'Application No Longer Meets Criteria for Use in DTS', true, 'flyway-community-edition', CURRENT_TIMESTAMP, 'flyway-community-edition', CURRENT_TIMESTAMP);