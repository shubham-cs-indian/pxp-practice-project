-- Table: staging.baseentity

DROP TABLE IF EXISTS staging.baseentity;

CREATE TABLE staging.baseentity
(
    baseentityiid bigint NOT NULL DEFAULT nextval('pxp.seqiid'::regclass),
    id character varying COLLATE pg_catalog."default" NOT NULL,
    basetype character varying COLLATE pg_catalog."default",
    branchof character varying COLLATE pg_catalog."default",
    contextiid bigint,
    createdby character varying COLLATE pg_catalog."default",
    createdon bigint,
    creationlanguage character varying COLLATE pg_catalog."default",
    defaultassetinstanceid character varying COLLATE pg_catalog."default",
    endpointid character varying COLLATE pg_catalog."default",
    hiddensummary jsonb,
    isembedded boolean,
    ismerged boolean,
    jobid character varying COLLATE pg_catalog."default",
    klassinstanceid character varying COLLATE pg_catalog."default",
    languagecodes text[] COLLATE pg_catalog."default",
    languageinstances jsonb,
    lastmodified bigint,
    lastmodifiedby character varying COLLATE pg_catalog."default",
    logicalcatalogid character varying COLLATE pg_catalog."default",
    messages jsonb,
    name character varying COLLATE pg_catalog."default",
    organizationid character varying COLLATE pg_catalog."default",
    originalinstanceid character varying COLLATE pg_catalog."default",
    owner character varying COLLATE pg_catalog."default",
    parentid character varying COLLATE pg_catalog."default",
    partnersources jsonb,
    path text[] COLLATE pg_catalog."default",
    physicalcatalogid character varying COLLATE pg_catalog."default",
    referenceconflictingvalues jsonb,
    relationshipconflictingvalues jsonb,
    roles jsonb,
    ruleviolation jsonb,
    selectedtaxonomyids text[] COLLATE pg_catalog."default",
    savecomment character varying COLLATE pg_catalog."default",
    summary jsonb,
    systemid character varying COLLATE pg_catalog."default",
    taxonomyconflictingvalues jsonb,
    taxonomyids text[] COLLATE pg_catalog."default",
    types text[] COLLATE pg_catalog."default",
    variantid character varying COLLATE pg_catalog."default",
    variants text[] COLLATE pg_catalog."default",
    versionid character varying COLLATE pg_catalog."default",
    versionof character varying COLLATE pg_catalog."default",
    versiontimestamp bigint,
    attributevariants jsonb,
    assetinformation jsonb,
    copyof character varying COLLATE pg_catalog."default",
    CONSTRAINT baseentity_pkey PRIMARY KEY (baseentityiid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE staging.baseentity
    OWNER to postgres;