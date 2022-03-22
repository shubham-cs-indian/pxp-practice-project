/**
 * Author:  vallee
 * Created: Mar 16, 2019
 *
 * Notice : upper case must be reserved to gcc constants and macros 
 */

/* Column and variable types alignement */
#ifdef PGSQL11
    #define _VARCHAR    varchar
    #define _LOCALEID   varchar
    #define _STRING     varchar /* used for function parameters */
    #define _TEXT       text
    #define _IID        bigint
    #define _UUID       varchar
    #define _TIMESTAMP  timestamptz
    #define _SHORT      smallint
    #define _INT        integer
    #define _LONG       bigint
    #define _BOOL(col)  boolean
    #define _BOOLEAN    boolean
    #define _FLOAT      numeric
    #define _JSON       json
    #define _JSONB      jsonb
    #define _BLOB       bytea
    #define _JSONCOL(colname)       json default null
    #define _JSONBCOL(colname)      jsonb default null
    #define _VARARRAY   varchar[]
    #define _IIDARRAY   bigint[]
    #define _INTARRAY   integer[]
    #define _HSTORE     hstore default null
    #define _INT8RANGE  int8range default null

#elif ORACLE12C
    #define _VARCHAR    varchar2(3000)  /* max previous to Oracle 12c */
    #define _LOCALEID   varchar(5)
    #define _STRING     varchar2 /* used for function parameters */
    /*#define _TEXT       varchar2(32767) /* max for Oracle 12c - with extension */
    #define _TEXT       clob
    #define _IID        number
    #define _UUID       varchar2(36)
    #define _TIMESTAMP  timestamp(3) with time zone
    #define _SHORT      number
    #define _INT        number
    #define _LONG       number
    #define _BOOL(col)  char(1) constraint check_ ## col check (col IN ('t', 'f'))
    #define _BOOLEAN    char
    #define _FLOAT      number
    #define _JSON       clob
    #define _JSONB      clob
    #define _BLOB       blob
    #define _JSONCOL(colname)       clob default null check ( colname is JSON strict )
    #define _JSONBCOL(colname)      clob default null check ( colname is JSON strict )
    #define _VARARRAY   pxp.varcharArray
    #define _IIDARRAY   pxp.iidArray
    #define _INTARRAY   pxp.iidArray
#endif

/* Declare a record and a cursor type for function returns */
#ifdef PGSQL11
    #define _RECORD( tableName) tableName%rowtype
    #define _RECORD_CURSOR(tableName) tableName%rowtype
    #define _CURSOR(tableName) setof tableName
#elif ORACLE12C
    #define _RECORD( tableName) tableName%rowtype 
    #define _RECORD_CURSOR(tableName) sys_refcursor
    #define _CURSOR(tableName)    sys_refcursor
#endif

/* Count for looping through an array and array element access */
#ifdef PGSQL11
    #define _COUNT( array)      coalesce( array_upper(array, 1), 0) 
    #define _ARRAY( array,idx)    array[idx]
    #define _ARRAY_CONCAT(array1, array2)   array1 || array2
    #define _ARRAY_INCLUDE(array, elt)      elt = any(array)
    #define _ARRAY_TABLE(array)    ( select * from unnest(array) )
#elif ORACLE12C
    #define _COUNT( array)      array.count 
    #define _ARRAY( array,idx)    array(idx)
    #define _ARRAY_CONCAT(array1, array2)   array1 multiset union array2
    #define _ARRAY_INCLUDE(array, elt)      elt member of array
    #define _ARRAY_TABLE(array)    ( select * from table(array) )
#endif


/* Declare a table */
#ifdef PGSQL11
    #define _TABLE( tableName) drop table if exists tableName cascade; create table tableName 
#elif ORACLE12C
    #define _TABLE( tableName) __NL__ create table tableName 
#endif

/* Declare a view */
#ifdef PGSQL11
    #define _VIEW( viewName) drop view if exists viewName cascade; create view viewName 
#elif ORACLE12C
    #define _VIEW( viewName) __NL__ create view viewName 
#endif

#include "./sql-partitions.sql" /* drive partition creation */
#ifndef WITH_PARTITIONS
    #define _PARTITION_BY_HASH(...)
    #define _CREATE_PARTITIONS_BY_HASH_TABLES( tablename ) --  
#endif
#ifdef WITH_PARTITIONS
#ifdef PGSQL11
    #define _PARTITION_BY_HASH(...)  partition by hash(__VA_ARGS__)
    #define _CREATE_PARTITIONS_BY_HASH_TABLES( tablename)    call pxp.CreateHashPartitions( #tablename, _NB_PARTITIONS)
#elif ORACLE12C
    #define _PARTITION_BY_HASH(...)  partition by hash(__VA_ARGS__) partitions _NB_PARTITIONS
    #define _CREATE_PARTITIONS_BY_HASH_TABLES( tablename) --   
#endif
#endif

/* Declare a Stored Procedure */
#define _PROCEDURE( functionName) create or replace procedure functionName 

/* Declare a Function*/
#define _FUNCTION( functionName) create or replace function functionName


/* Declare a foreign key*/
#define _FOREIGN_KEY(referencingTable, referencedTable, id, field) alter table pxp.referencingTable add constraint referencingTable ## _ ## id ##_fk foreign key(id) references pxp.referencedTable (field)
#define _CREATE_INDEX1(tableName, field1) create index tableName ## _ ## field1 ## _ ## idx on pxp.tableName(field1)
#define _CREATE_INDEX2(tableName, field1, field2) create index tableName ## _ ## field1 ## _ ## field2 ## _ ## idx on pxp.tableName(field1, field2)
#define _CREATE_INDEX3(tableName, field1, field2, field3) create index tableName ## _ ## field1 ## _ ## field2 ## _ ## field3 ## _## idx on pxp.tableName(field1, field2, field3)
#define _CREATE_UNIQUE_INDEX1(tableName, field1) create unique index tableName ## _ ## field1 ## _ ## idx on pxp.tableName(field1)
#define _CREATE_UNIQUE_INDEX2(tableName, field1, field2) create unique index tableName ## _ ## field1 ## _ ## field2 ## _ ## idx on pxp.tableName(field1, field2)

/* Declare covering index */
#define _CREATE_INDEX1_INCLUDE(tableName, field1, field2) create index tableName ## _ ## field1 ## _include_ ## field2 ## _idx on pxp.tableName(field1) include (field2)
#define _CREATE_INDEX2_INCLUDE(tableName, field1, field2, field3) create index tableName ## _ ## field1 ## _ ## field2 ## _include_ ## field3 ## _idx on pxp.tableName(field1, field2) include (field3)

#define _CREATE_JSON_INDEX(tableName, field1) create index tableName ## _ ## field1 ## _ ## idx on pxp.tableName using gin(field1)
#define _CREATE_HSTORE_INDEX(tableName, field1) create index tableName ## _ ## field1 ## _ ## idx on pxp.tableName using gin(field1)
#define _CREATE_GIN_INDEX(tableName, field1) create index tableName ## _ ## field1 ## _ ## idx on pxp.tableName using gin(field1)

/* Start definition of a function, exception and null statement of return */
#ifdef PGSQL11
    #define _RETURN     returns 
    #define _NULL(x)    null
    #define _RAISE(format, param) raise format, param
    #define _CALL       call
    #define _SPLIT(field, column)          split_part(field, ':', column)
    #define _ARRAY_APPEND(array, value)    array = array_append(array, value)

#elif ORACLE12C
    #define _RETURN     return
    #define _NULL(x)    x
    #define _RAISE(format, param) raise_application_error( -20001, replace(format, '%', param) ) 
    #define _CALL
    #define _SPLIT(field, column)          regexp_substr(field, '[^:]+', 1, column)
    #define _ARRAY_APPEND(array, value)    select (array MULTISET UNION cast(value as  pxp.iidArray)) into array from dual
#endif

/* Start definition of a Stored Procedure or Function */
#ifdef PGSQL11
    #define _IMPLEMENT_AS as $body$ declare 
    #define _IMPLEMENT_END $body$ language plpgsql
    #define _IMPLEMENT_CURSOR( cursorName, ...) for cursorName in (__VA_ARGS__) \
                                                loop \
                                                    return next cursorName; \
                                                end loop
	#define _RECURSIVE recursive
#elif ORACLE12C
    #define _IMPLEMENT_AS is 
    #define _IMPLEMENT_END 
    #define _IMPLEMENT_CURSOR( cursorName, ...) open cursorName for __VA_ARGS__; \
                                                return cursorName
	#define _RECURSIVE
#endif

/* Declare everything related to triggers */
#ifdef PGSQL11
    #define _TRIGGER( triggerName) create or replace function pxp.triggerName() returns trigger
    #define _TRIGGER_BODY_IS( action, table)    as $body$ declare
    #define _TRIGGER_DECLARE( triggerName, triggerProc, action, table)  drop trigger if exists triggerName on table; \
                                                           create trigger triggerName action on table \
                                                           for each row execute procedure pxp.triggerProc()
    #define _TRIGGER_BODY_END( triggerName, action, table) $body$ language plpgsql; \
                                                           _TRIGGER_DECLARE( triggerName, triggerName, action, table);
    #define _NEW new
    #define _OLD old
    #define _RETURN_TRIGGER_NEW return new
    #define _RETURN_TRIGGER_OLD return old
#elif ORACLE12C
    #define _TRIGGER( triggerName) create or replace trigger pxp.triggerName
    #define _TRIGGER_BODY_IS( action, table)   action on table for each row enable declare
    #define _TRIGGER_DECLARE( triggerName, triggerProc, action, table)
    #define _TRIGGER_BODY_END( triggerName, action, table)
    #define _NEW :new
    #define _OLD :old
    #define _RETURN_TRIGGER_NEW return
    #define _RETURN_TRIGGER_OLD return
#endif

/* Create sequence command */
#ifdef PGSQL11
    #define _CREATE_SEQUENCE( seqName, seqStart) drop sequence if exists seqName cascade; create sequence seqName start seqStart
    #define _NEXTVAL( seqName) nextval(#seqName)
    #define _CURRVAL( seqName) currval(#seqName)
#elif ORACLE12C
    #define _CREATE_SEQUENCE( seqName, seqStart) __NL__ create sequence seqName start with seqStart increment by 1 nocache nocycle
    #define _NEXTVAL( seqName) seqName.nextval
    #define _CURRVAL( seqName) seqName.currval
#endif

/* Select system time into a variable */
#ifdef PGSQL11
    #define _SYSTIMESTAMP   localtimestamp
    #define _SYSTIMESTAMP_INTO( tsVar)  tsVar := (select _SYSTIMESTAMP) /* notice: now() returns with TZ */
    #define _SYSTIMESTAMP_ISO( tsVar)   tsVar := (select to_char(_SYSTIMESTAMP, 'YYYY-MM-DD"T"HH24:MI:SS'))
    #define _INTERVAL( numberOf, range) (numberOf || ' ' || #range)::interval /* example _INTERVAL( 3, hour) */
	#define _NUMTIMESTAMP_INTO( tsVar)  tsVar := (select extract(epoch from _SYSTIMESTAMP))::bigint
#elif ORACLE12C
    #define _SYSTIMESTAMP   systimestamp
    #define _SYSTIMESTAMP_INTO( tsVar)  select _SYSTIMESTAMP into tsVar from dual
    #define _SYSTIMESTAMP_ISO( tsVar)   select to_char(_SYSTIMESTAMP, 'YYYY-MM-DD"T"HH24:MI:SS') into tsVar from dual
    #define _INTERVAL( numberOf, range) numtodsinterval( numberOf, #range)
	#define _NUMTIMESTAMP_INTO( tsVar)  \
		select extract(day from (systimestamp - timestamp '1970-01-01 00:00:00')) * 86400000 \
		+ extract(hour from (systimestamp - timestamp '1970-01-01 00:00:00')) * 3600000 \
		+ extract(minute from (systimestamp - timestamp '1970-01-01 00:00:00')) * 60000 \
		+ extract(second from (systimestamp - timestamp '1970-01-01 00:00:00')) * 1000 into tsVar from dual /* to be tested */
#endif

/* Found function for upsert operations to be used as: if _FOUND then ... */
#ifdef PGSQL11
    #define _FOUND      found
    #define _NOT_FOUND  not found
#elif ORACLE12C
    #define _FOUND  ( sql%rowcount > 0 )
    #define _NOT_FOUND  ( sql%rowcount = 0 )
#endif

/* LIMIT clause differences in pgsql and Oracle.. */
#ifdef PGSQL11
    #define _LIMIT   LIMIT
    #define _ROWID   ctid
#elif ORACLE12C
    #define _LIMIT  and rownum <
    #define _ROWID	rowid 
#endif

/* Like clause with regexp */
#ifdef PGSQL11
    #define _SIMILAR( col, pattern)     (col similar to pattern)
#elif ORACLE12C
    #define _SIMILAR( col, pattern)     regexp_like( col, pattern)
#endif

/* Transtyping */
#ifdef PGSQL11
    #define _TO_VARCHAR(n)  n::varchar
    #define _TO_LONG(n)     n::bigint
    #define _TO_NUMERIC(n)  n::numeric
    #define _TO_JSON(n)     n::json
    #define _TO_PXONCODE(n)  case when n ~ '^[0-9\-]' then '' || n || '' else n end
    #define _TO_VARARRAY(...)  array[__VA_ARGS__ ]
#elif ORACLE12C
    #define _TO_VARCHAR(n) to_char(n)
    #define _TO_LONG(n)     n
    #define _TO_NUMERIC(n)  n
    #define _TO_JSON(n)     n
    #define _TO_PXONCODE(n)  case when regexp_like( n, '^[0-9\-]') then '''' || n || '''' else n end
    #define _TO_VARARRAY(...)  _VARARRAY(__VA_ARGS__)
#endif

/* insert a title in introduction of generated files */ 
#ifdef PGSQL11
--  GENERATED SCRIPT for PGSQL 11
#define _EOF    -- end of file __FILE__ (PGSQL 11)
#elif ORACLE12C
--  GENERATED SCRIPT for ORACLE 12c
#define _EOF    -- end of file __FILE__ (ORACLE 12c)
#endif
