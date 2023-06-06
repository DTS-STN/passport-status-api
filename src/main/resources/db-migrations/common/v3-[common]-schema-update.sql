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
