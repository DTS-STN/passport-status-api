CREATE TABLE email_request (
	id VARCHAR(64) NOT NULL,

	date_of_birth DATE NOT NULL,
	email VARCHAR(256) NOT NULL,
	file_number VARCHAR(32) NOT NULL,
	first_name VARCHAR(64) NOT NULL,
	last_name VARCHAR(64) NOT NULL,
	status VARCHAR(32) NOT NULL,

	-- audit fields
	created_by VARCHAR(64) NOT NULL,
	created_date TIMESTAMP NOT NULL,
	last_modified_by VARCHAR(64) NULL,
	last_modified_date TIMESTAMP NULL,

	CONSTRAINT pk_email_request PRIMARY KEY (id)
);

CREATE TABLE http_request (
	id VARCHAR(64) NOT NULL,

	principal_name VARCHAR(64),
	request_headers VARCHAR(65536),
	request_method VARCHAR(8) NOT NULL,
	request_remote_address VARCHAR(64),
	request_uri VARCHAR(4096) NOT NULL,
	response_headers VARCHAR(65536),
	response_status INTEGER NOT NULL,
	session_id VARCHAR(256),
	time_taken_millis BIGINT,
	timestamp TIMESTAMP NOT NULL,

	-- audit fields
	created_by VARCHAR(64) NOT NULL,
	created_date TIMESTAMP NOT NULL,
	last_modified_by VARCHAR(64) NULL,
	last_modified_date TIMESTAMP NULL,

	CONSTRAINT pk_http_request PRIMARY KEY (id)
);

CREATE TABLE passport_status (
	id VARCHAR(64) NOT NULL,

	application_register_sid VARCHAR(256) NOT NULL,
	date_of_birth DATE NOT NULL,
	email VARCHAR(256) NOT NULL,
	file_number VARCHAR(32) NOT NULL,
	first_name VARCHAR(64) NOT NULL,
	last_name VARCHAR(64) NOT NULL,
	status VARCHAR(32) NOT NULL,

	-- audit fields
	created_by VARCHAR(64) NOT NULL,
	created_date TIMESTAMP NOT NULL,
	last_modified_by VARCHAR(64) NULL,
	last_modified_date TIMESTAMP NULL,

	CONSTRAINT pk_passport_status PRIMARY KEY (id)
);

CREATE INDEX ix_date_of_birth ON passport_status(date_of_birth);
CREATE INDEX ix_email ON passport_status(email);
CREATE INDEX ix_file_number ON passport_status(file_number);
CREATE INDEX ix_first_name ON passport_status(first_name);
CREATE INDEX ix_last_name ON passport_status(last_name);