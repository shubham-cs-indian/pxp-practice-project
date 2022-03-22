-- Table: staging.context

DROP TABLE IF EXISTS staging.context;

CREATE TABLE staging.context
(
    contextiid bigint NOT NULL DEFAULT nextval('pxp.seqiid'::regclass),
    id character varying COLLATE pg_catalog."default" NOT NULL,
    contextid character varying COLLATE pg_catalog."default",
    lastmodifiedby character varying COLLATE pg_catalog."default",
    linkedinstances jsonb,
    taginstanceids text[] COLLATE pg_catalog."default",
    timerange jsonb,
    versionid character varying COLLATE pg_catalog."default",
    versiontimestamp bigint,
    CONSTRAINT context_pkey PRIMARY KEY (contextiid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE staging.context
    OWNER to postgres;