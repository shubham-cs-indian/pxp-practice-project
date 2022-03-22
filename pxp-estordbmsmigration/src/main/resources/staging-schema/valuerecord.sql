-- Table: staging.valuerecord

DROP TABLE IF EXISTS staging.valuerecord;

CREATE TABLE staging.valuerecord
(
    valueiid bigint NOT NULL DEFAULT nextval('pxp.seqiid'::regclass),
    attributeid character varying COLLATE pg_catalog."default",
    basetype character varying COLLATE pg_catalog."default",
    code character varying COLLATE pg_catalog."default",
    contextiid bigint,
    createdby character varying COLLATE pg_catalog."default",
    createdon bigint,
    duplicatestatus jsonb,
    id character varying COLLATE pg_catalog."default" NOT NULL,
    isconflictresolved boolean,
    ismandatoryviolated boolean,
    ismatchandmerge boolean,
    isshouldviolated boolean,
    isunique integer,
    jobid character varying COLLATE pg_catalog."default",
    klassinstanceid character varying COLLATE pg_catalog."default",
    klassinstanceversion bigint,
    language character varying COLLATE pg_catalog."default",
    lastmodified bigint,
    lastmodifiedby character varying COLLATE pg_catalog."default",
    notification jsonb,
    originalinstanceid character varying COLLATE pg_catalog."default",
    savecomment character varying COLLATE pg_catalog."default",
    tags jsonb,
    value character varying COLLATE pg_catalog."default",
    valueasexpression jsonb,
    valueashtml character varying COLLATE pg_catalog."default",
    valueasnumber numeric,
    variantinstanceid character varying COLLATE pg_catalog."default",
    versionid character varying COLLATE pg_catalog."default",
    versiontimestamp bigint,
    unitsymbol character varying COLLATE pg_catalog."default",
    calculation character varying COLLATE pg_catalog."default",
    CONSTRAINT valuerecord_pkey PRIMARY KEY (valueiid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE staging.valuerecord
    OWNER to postgres;