-- Table: staging.tagconflictingvalues

DROP TABLE IF EXISTS staging.tagconflictingvalues;

CREATE TABLE staging.tagconflictingvalues
(
    id character varying COLLATE pg_catalog."default",
    tagid character varying COLLATE pg_catalog."default",
    couplingtype character varying COLLATE pg_catalog."default",
    source jsonb,
    klassinstanceid character varying COLLATE pg_catalog."default",
    tagvalues hstore,
    ismandatory boolean,
    isshould boolean,
    versiontimestamp bigint,
    lastmodifiedby character varying COLLATE pg_catalog."default",
    versionid character varying COLLATE pg_catalog."default"
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE staging.tagconflictingvalues
    OWNER to postgres;