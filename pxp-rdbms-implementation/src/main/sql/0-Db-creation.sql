/**
 * Commmands to create the PXP RDBMS database
 * Author:  vallee
 * Created: Mar 16, 2019
 */
#ifdef PGSQL11
/*
    PostgreSQL 11 instructions:
    --------------------------
    for automation, the script is run on-line from
    $> psql -p -U postgres --plz <port number>
    
    for manual setup, copy/paste the script into a query tools from PGAdmin4
    and execute on default postgres database
*/
create user pxp;
alter user pxp with encrypted password 'pxp123';
alter user pxp with superuser;

--grant all privileges on database camundadb to pxp;
create database pxp
    with 
    owner = pxp
    encoding = 'UTF8'
    connection limit = -1;

create schema if not exists pxp authorization pxp;
-- TODO defines the tablespaces structure for PGSQL
#endif

#ifdef ORACLE12C
/*
    ORACLE 12c instrutions
    --------------------------
    for automation, the script is run on-line from
    // TODO

    for manual setup, copy/paste the script SQLDeveloper and execute
*/
alter session set "_oracle_script"=true;
create user pxp identified by "pxp123";
grant connect, resource, dba, create view to pxp;

-- create database in ORACLE??
-- TODO defines the tablespaces structure for ORACLE
#endif

