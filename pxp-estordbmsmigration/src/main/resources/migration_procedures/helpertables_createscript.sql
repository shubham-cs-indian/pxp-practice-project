-- Create Scripts for helper tables

-- Create staging schema
CREATE SCHEMA IF NOT EXISTS staging;

-- Table: staging.baseentityid

DROP TABLE IF EXISTS staging.baseentityid;

CREATE TABLE staging.baseentityid
(
    id character varying COLLATE pg_catalog."default",
    baseentityid character varying COLLATE pg_catalog."default"
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE staging.baseentityid
    OWNER to postgres;
    
    
-- Table: staging.helper_basetype

DROP TABLE IF EXISTS staging.helper_basetype;

CREATE TABLE staging.helper_basetype
(
    basetype character varying COLLATE pg_catalog."default" NOT NULL,
    basetypevsenumvalue integer,
    CONSTRAINT helper_basetype_pkey PRIMARY KEY (basetype)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE staging.helper_basetype
    OWNER to postgres;
    
    
-- Table: staging.helper_config

DROP TABLE IF EXISTS staging.helper_config;

CREATE TABLE staging.helper_config
(
    cid character varying COLLATE pg_catalog."default" NOT NULL,
    code character varying COLLATE pg_catalog."default",
    "isNature" boolean,
    "classifierIId" bigint,
    CONSTRAINT helper_config_pkey PRIMARY KEY (cid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE staging.helper_config
    OWNER to postgres;


-- Table: staging.helper_userconfig

DROP TABLE IF EXISTS staging.helper_userconfig;

CREATE TABLE staging.helper_userconfig
(
    username character varying COLLATE pg_catalog."default",
    userid character varying COLLATE pg_catalog."default" NOT NULL,
    useriid bigint,
    CONSTRAINT helper_userconfig_pkey PRIMARY KEY (userid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE staging.helper_userconfig
    OWNER to postgres;

-- Table: staging.helper_relationshipconfig

DROP TABLE IF EXISTS staging.helper_relationshipconfig;

CREATE TABLE staging.helper_relationshipconfig
(
    relationshipid character varying COLLATE pg_catalog."default" NOT NULL,
    side1elementid character varying COLLATE pg_catalog."default",
    side2elementid character varying COLLATE pg_catalog."default",
    code character varying COLLATE pg_catalog."default",
    iid bigint,
    CONSTRAINT helper_relationshipconfig_pkey PRIMARY KEY (relationshipid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE staging.helper_relationshipconfig
    OWNER to postgres;  

-- Table: staging.helper_endpointconfig

DROP TABLE IF EXISTS staging.helper_endpointconfig;

CREATE TABLE staging.helper_endpointconfig
(
    cid character varying COLLATE pg_catalog."default" NOT NULL,
    code character varying COLLATE pg_catalog."default",
    CONSTRAINT helper_endpointconfig_pkey PRIMARY KEY (cid)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE staging.helper_endpointconfig OWNER to postgres;  

-- Table: staging.successfulbatchcount

-- DROP TABLE staging.successfulbatchcount;

CREATE TABLE staging.successfulbatchcount
(
	index character varying NOT NULL,
    entitycount bigint NOT NULL,
    relationshipbatchcount bigint NOT NULL,
    referencebatchcount bigint NOT NULL,
    taskbatchcount bigint NOT NULL
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE staging.successfulbatchcount
    OWNER to postgres;

-- Table: staging.inprogresstaskinstances

DROP TABLE IF EXISTS staging.inprogresstaskinstances;

CREATE TABLE staging.inprogresstaskinstances
(
    id character varying COLLATE pg_catalog."default"
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE staging.inprogresstaskinstances
    OWNER to postgres;