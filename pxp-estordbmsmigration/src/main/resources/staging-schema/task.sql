-- Table: staging.task

DROP TABLE IF EXISTS staging.task;

CREATE TABLE staging.task
(
    taskiid bigint NOT NULL DEFAULT nextval('pxp.seqiid'::regclass),
    id character varying COLLATE pg_catalog."default" NOT NULL,
    type character varying COLLATE pg_catalog."default",
    name character varying COLLATE pg_catalog."default",
    description character varying COLLATE pg_catalog."default",
    contentId character varying COLLATE pg_catalog."default" NOT NULL,
    createdtime bigint,
    startdate bigint,
    duedate bigint,
    overduedate bigint,
    wfcreated boolean,
    wfprocessid character varying COLLATE pg_catalog."default",    
    wftaskinstanceid character varying COLLATE pg_catalog."default",
    wfprocessinstanceid character varying COLLATE pg_catalog."default",
    attachments bigint[],
    position jsonb,    
    comments jsonb,
    parenttaskid character varying COLLATE pg_catalog."default",
    priority character varying COLLATE pg_catalog."default",    
    status character varying COLLATE pg_catalog."default",    
    propertyid character varying COLLATE pg_catalog."default",    

    CONSTRAINT task_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE staging.task
    OWNER to postgres;


-- Table: staging.taskuserlink

DROP TABLE IF EXISTS staging.taskuserlink;

CREATE TABLE staging.taskuserlink
(
    taskid character varying COLLATE pg_catalog."default" NOT NULL,
    userid character varying COLLATE pg_catalog."default" NOT NULL,
    racivs int,
    CONSTRAINT taskuserlink_pkey PRIMARY KEY (taskid, userid, racivs)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE staging.taskuserlink
    OWNER to postgres;


-- Table: staging.taskrolelink

DROP TABLE IF EXISTS staging.taskrolelink;

CREATE TABLE staging.taskrolelink
(
    taskid character varying COLLATE pg_catalog."default" NOT NULL,
    roleid character varying COLLATE pg_catalog."default" NOT NULL,
    racivs int,
    CONSTRAINT taskrolelink_pkey PRIMARY KEY (taskid, roleid, racivs)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE staging.taskrolelink
    OWNER to postgres;