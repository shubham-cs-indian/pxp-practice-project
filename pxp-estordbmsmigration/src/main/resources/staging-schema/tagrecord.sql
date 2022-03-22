-- Table: staging.tagrecord

DROP TABLE IF EXISTS staging.tagrecord;

CREATE TABLE staging.tagrecord
(
    tagiid bigint NOT NULL DEFAULT nextval('pxp.seqiid'::regclass),
    basetype character varying COLLATE pg_catalog."default",
    contextinstanceid character varying COLLATE pg_catalog."default",
    createdby character varying COLLATE pg_catalog."default",
    createdon bigint,
    id character varying COLLATE pg_catalog."default" NOT NULL,
    isconflictresolved boolean,
    ismandatoryviolated boolean,
    ismatchandmerge boolean,
    isshouldviolated boolean,
    jobid character varying COLLATE pg_catalog."default",
    klassinstanceid character varying COLLATE pg_catalog."default",
    lastmodified bigint,
    lastmodifiedby character varying COLLATE pg_catalog."default",
    notification jsonb,
    tagid character varying COLLATE pg_catalog."default",
    tagvalues hstore,
    variantinstanceid character varying COLLATE pg_catalog."default",
    versionid character varying COLLATE pg_catalog."default",
    versiontimestamp bigint,
    CONSTRAINT tagrecord_pkey PRIMARY KEY (tagiid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE staging.tagrecord
    OWNER to postgres;