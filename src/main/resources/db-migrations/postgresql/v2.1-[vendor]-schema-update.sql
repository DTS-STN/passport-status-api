DROP INDEX ix_given_name;
CREATE INDEX ix_given_name ON passport_status(lower(cast(remove_non_alpha_numeric(remove_diacritics(given_name)) as text)));
REINDEX INDEX ix_given_name;

DROP INDEX ix_surname;
CREATE INDEX ix_surname ON passport_status(lower(cast(remove_non_alpha_numeric(remove_diacritics(surname)) as text)));
REINDEX INDEX ix_surname;