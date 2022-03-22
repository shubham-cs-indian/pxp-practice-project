-- Table: staging."references"

DROP TABLE IF EXISTS staging."references";

CREATE TABLE staging."references"
(
    referenceiid bigint NOT NULL DEFAULT nextval('pxp.seqiid'::regclass),
    id character varying COLLATE pg_catalog."default" NOT NULL,
    originalinstanceid character varying COLLATE pg_catalog."default",
    side1instanceid character varying COLLATE pg_catalog."default",
    side2instanceid character varying COLLATE pg_catalog."default",
    referenceid character varying COLLATE pg_catalog."default",
    CONSTRAINT references_pkey PRIMARY KEY (referenceiid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE staging."references"
    OWNER to postgres;