/******************************************************************************
 * Migration file for Passport Application Status Checker release 2.0.
 *
 * PASC v2.0 will include support for multiple data source systems. Prior to
 * v2.0, passport data was sourced from the IRIS passport system. From v2.0
 * onwards, data can be sourced from the IRIS, GCMS, or Tempo passport systems.
 *
 * To add support for the new data source systems, this migration file will
 * perform the following migrations:
 *
 *   - create a new table called `source_code` which represents the source system's CDO code
 *   - populate the new table with the initial data representing IRIS, GCMS, and Tempo
 *   - modify the `passport_status` table to add a column called `source_code_id` that joins to the new `source_code` table
 *
 * Initially, the `source_code_id` column will use a default value that corresponds to the
 * IRIS passport system, which will force all existing rows to be updated to use the IRIS source code.
 * This default value will then be removed to ensure that future rows must have the source code set on insert.
 */

-------------------------------------------------------------------------------
-- create a new table called `source_code` which represents the source system's CDO code
-------------------------------------------------------------------------------

CREATE TABLE source_code
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

	CONSTRAINT pk_source_code PRIMARY KEY (id)
);

CREATE INDEX ix_source_code_cdo_code ON source_code(cdo_code);
CREATE INDEX ix_source_code_code ON source_code(code);

-------------------------------------------------------------------------------
-- populate the new table with the initial data representing IRIS, GCMS, and Tempo
-------------------------------------------------------------------------------

INSERT INTO source_code
	(id, code, cdo_code, description, is_active, created_by, created_date, last_modified_by, last_modified_date)
VALUES
	('c0d1b52d-3107-41d4-bd4b-00d4e99829db', 'TEMPO', '32', 'Tempo Application', true, 'flyway-community-edition', CURRENT_TIMESTAMP, 'flyway-community-edition', CURRENT_TIMESTAMP),
	('61918f07-9dc7-4b84-a2e5-031dcaa0f547', 'GCMS',  '33', 'GCMS Application',  true, 'flyway-community-edition', CURRENT_TIMESTAMP, 'flyway-community-edition', CURRENT_TIMESTAMP),
	('327c25eb-e3f4-492e-bd47-4feb20189e78', 'IRIS',  '36', 'IRIS Application',  true, 'flyway-community-edition', CURRENT_TIMESTAMP, 'flyway-community-edition', CURRENT_TIMESTAMP);

-------------------------------------------------------------------------------
-- modify the `passport_status` table to add a column called `source_code_id` that joins to the new `source_code` table
-------------------------------------------------------------------------------

ALTER TABLE passport_status ADD COLUMN source_code_id VARCHAR(64) NOT NULL DEFAULT '327c25eb-e3f4-492e-bd47-4feb20189e78';
ALTER TABLE passport_status ADD CONSTRAINT fk_passport_status_source_code FOREIGN KEY (source_code_id) REFERENCES source_code(id);
ALTER TABLE passport_status ALTER COLUMN source_code_id DROP DEFAULT;
