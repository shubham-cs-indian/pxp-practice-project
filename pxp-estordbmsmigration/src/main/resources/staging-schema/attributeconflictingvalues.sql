-- Table: staging.attributeconflictingvalues

DROP TABLE IF EXISTS staging.attributeconflictingvalues;

CREATE TABLE staging.attributeconflictingvalues
(
    id character varying COLLATE pg_catalog."default",
    attributeid character varying COLLATE pg_catalog."default",
    couplingtype character varying COLLATE pg_catalog."default",
    source jsonb,
    klassinstanceid character varying COLLATE pg_catalog."default",
    ismandatory boolean,
    isshould boolean,
    value character varying COLLATE pg_catalog."default",
    language character varying COLLATE pg_catalog."default",
    valueashtml character varying COLLATE pg_catalog."default",
    versiontimestamp bigint,
    lastmodifiedby character varying COLLATE pg_catalog."default",
    versionid character varying COLLATE pg_catalog."default"
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE staging.attributeconflictingvalues
    OWNER to postgres;