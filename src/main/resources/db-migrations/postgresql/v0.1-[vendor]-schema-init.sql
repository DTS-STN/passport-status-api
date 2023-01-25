CREATE EXTENSION unaccent;

CREATE FUNCTION remove_diacritics(text)
  RETURNS text
  LANGUAGE sql
  IMMUTABLE PARALLEL SAFE STRICT
AS $func$
SELECT public.unaccent($1)
$func$;

CREATE FUNCTION remove_non_alpha_numeric(text)
  RETURNS text
  LANGUAGE sql
  IMMUTABLE PARALLEL SAFE STRICT
AS $func$
SELECT regexp_replace($1, '[^a-zA-Z0-9]+', '','g')
$func$;

DROP INDEX ix_email;
CREATE INDEX ix_email ON passport_status(lower(email));

DROP INDEX ix_file_number;
CREATE INDEX ix_file_number ON passport_status(lower(file_number));

DROP INDEX ix_given_name;
CREATE INDEX ix_given_name ON passport_status(lower(remove_non_alpha_numeric(remove_diacritics(given_name))));

DROP INDEX ix_surname;
CREATE INDEX ix_surname ON passport_status(lower(remove_non_alpha_numeric(remove_diacritics(surname))));