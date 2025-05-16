-- New delivery_method codes
CREATE TABLE delivery_method_code
(
	id VARCHAR(64) NOT NULL,

	cdo_code VARCHAR(8) NOT NULL,
	code VARCHAR(64) NOT NULL,
	description VARCHAR(256),
	is_active BOOLEAN NOT NULL,

	-- audit fields
	created_by VARCHAR(64) NOT NULL,
	created_date TIMESTAMP NOT NULL,
	last_modified_by VARCHAR(64),
	last_modified_date TIMESTAMP,

	CONSTRAINT pk_delivery_method_code PRIMARY KEY (id)
);

CREATE INDEX ix_dmc_id on delivery_method_code(id);
CREATE INDEX ix_dmc_code on delivery_method_code(code);
CREATE INDEX ix_dmc_cdo_code on delivery_method_code(cdo_code);

INSERT INTO delivery_method_code
  (id, code, cdo_code, description, is_active, created_by, created_date, last_modified_by, last_modified_date)
VALUES
  ('f45fe40f-bb24-4fd3-8029-d3cdf0ec6cd6', 'NOT_AVAILABLE', '0', 'Old statuses lack delivery method', true, 'flyway-community-edition', CURRENT_TIMESTAMP, 'flyway-community-edition', CURRENT_TIMESTAMP),
  ('8aefdf27-c4f1-43ab-9575-7f760478dd5d', 'MAIL', '1', 'Passport will be mailed', true, 'flyway-community-edition', CURRENT_TIMESTAMP, 'flyway-community-edition', CURRENT_TIMESTAMP),
  ('09540841-757b-4dc4-91e1-a299b8226ef7', 'IN_PERSON', '2', 'Passport will be picked up in person', true, 'flyway-community-edition', CURRENT_TIMESTAMP, 'flyway-community-edition', CURRENT_TIMESTAMP);

-- New service_level codes
CREATE TABLE service_level_code
(
	id VARCHAR(64) NOT NULL,

	cdo_code VARCHAR(8) NOT NULL,
	code VARCHAR(64) NOT NULL,
	description VARCHAR(256),
	is_active BOOLEAN NOT NULL,

	-- audit fields
	created_by VARCHAR(64) NOT NULL,
	created_date TIMESTAMP NOT NULL,
	last_modified_by VARCHAR(64),
	last_modified_date TIMESTAMP,

	CONSTRAINT pk_service_level_code PRIMARY KEY (id)
);

CREATE INDEX ix_slc_id on service_level_code(id);
CREATE INDEX ix_slc_code on service_level_code(code);
CREATE INDEX ix_slc_cdo_code on service_level_code(cdo_code);

INSERT INTO service_level_code
  (id, code, cdo_code, description, is_active, created_by, created_date, last_modified_by, last_modified_date)
VALUES
  ('1f8e5cba-af9b-426e-877e-1a7a6dcca472', 'NOT_AVAILABLE', '0', 'Old statuses lack service level', true, 'flyway-community-edition', CURRENT_TIMESTAMP, 'flyway-community-edition', CURRENT_TIMESTAMP),
  ('e5f40fad-ad83-4fe1-9bdb-beba987045cb', 'TEN_DAYS', '1', 'Applications service level is 10 days', true, 'flyway-community-edition', CURRENT_TIMESTAMP, 'flyway-community-edition', CURRENT_TIMESTAMP),
  ('57abfbb5-f666-4f6d-a084-f2df9208932b', 'TWENTY_DAYS', '2', 'Applications service level is 20 days', true, 'flyway-community-edition', CURRENT_TIMESTAMP, 'flyway-community-edition', CURRENT_TIMESTAMP);

-- New status code for when processing is overdue
INSERT INTO status_code
  (id, code, cdo_code, description, is_active, created_by, created_date, last_modified_by, last_modified_date)
VALUES
  ('080ae70a-d838-4990-8f16-5a79559f1260', 'FILE_BEING_PROCESSED_OVERDUE', '7', 'Passport is being processed, but is passed service standard', true, 'flyway-community-edition', CURRENT_TIMESTAMP, 'flyway-community-edition', CURRENT_TIMESTAMP);

-- Alter status table for new codes + new timeline date fields
ALTER TABLE passport_status
ADD COLUMN delivery_method_code_id VARCHAR(64);

ALTER TABLE passport_status
ADD COLUMN service_level_code_id VARCHAR(64);

ALTER TABLE passport_status
ADD COLUMN app_received_date DATE;

ALTER TABLE passport_status
ADD COLUMN app_reviewed_date DATE;

ALTER TABLE passport_status
ADD COLUMN app_printed_date DATE;

ALTER TABLE passport_status
ADD COLUMN app_completed_date DATE;

-- Add initial values to old applications, then add foreign key.
UPDATE passport_status 
SET delivery_method_code_id = (SELECT id FROM delivery_method_code WHERE code = 'NOT_AVAILABLE' LIMIT 1);

ALTER TABLE passport_status 
ADD CONSTRAINT fk_delivery_method_code
FOREIGN KEY (delivery_method_code_id) REFERENCES delivery_method_code(id);

UPDATE passport_status 
SET service_level_code_id = (SELECT id FROM service_level_code WHERE code = 'NOT_AVAILABLE' LIMIT 1);

ALTER TABLE passport_status 
ADD CONSTRAINT fk_service_level_code
FOREIGN KEY (service_level_code_id) REFERENCES service_level_code(id);

-- Add initial received date values for old applications.
UPDATE passport_status
SET app_received_date = '0001-01-01';

-- Set new required fields to NOT NULL
ALTER TABLE passport_status
ALTER COLUMN delivery_method_code_id SET NOT NULL;

ALTER TABLE passport_status
ALTER COLUMN service_level_code_id SET NOT NULL;

ALTER TABLE passport_status
ALTER COLUMN app_received_date SET NOT NULL;
