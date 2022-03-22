-- Table: staging.relationships

DROP TABLE IF EXISTS staging.relationships;

CREATE TABLE staging.relationships
(
    relationshipsiid bigint NOT NULL DEFAULT nextval('pxp.seqiid'::regclass),
    id character varying COLLATE pg_catalog."default" NOT NULL,
    commonrelationshipinstanceid character varying COLLATE pg_catalog."default",
    contextiid bigint,
    count bigint,
    isfatherarticle boolean,
    originalinstanceid character varying COLLATE pg_catalog."default",
    parentid character varying COLLATE pg_catalog."default",
    parentversionid character varying COLLATE pg_catalog."default",
    relationshipid character varying COLLATE pg_catalog."default",
    relationshipobjectid character varying COLLATE pg_catalog."default",
    savecomment character varying COLLATE pg_catalog."default",
    side1basetype character varying COLLATE pg_catalog."default",
    side2basetype character varying COLLATE pg_catalog."default",
    side1instanceid character varying COLLATE pg_catalog."default",
    side2instanceid character varying COLLATE pg_catalog."default",
    side1instanceversionid character varying COLLATE pg_catalog."default",
    side2instanceversionid character varying COLLATE pg_catalog."default",
    sideid character varying COLLATE pg_catalog."default",
    tags jsonb,
    versionid character varying COLLATE pg_catalog."default",
    versiontimestamp bigint,
    variant1instanceid character varying COLLATE pg_catalog."default",
    variant2instanceid character varying COLLATE pg_catalog."default",
    CONSTRAINT relationships_pkey PRIMARY KEY (relationshipsiid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE staging.relationships
    OWNER to postgres;