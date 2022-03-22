/**
 * Commmands to cleanup the PXP schema
 * Author:  umesh
 * Created: Oct 25, 2019
 */
#ifdef PGSQL11
/*
    PostgreSQL 11 instructions:
    --------------------------
    for automation, the script is run on-line from 
    $> psql -p -U postgres --plz <port number>

    and through the script postgres-renew-schema.psql
    
    for manual setup, copy/paste the script into a query tools from PGAdmin4
    and execute on default postgres database
*/

drop schema if exists pxp cascade;
create extension hstore;
#endif

#ifdef ORACLE12C
/*
    ORACLE 12c instrutions
    --------------------------
    for automation, the script is run on-line from

    and through the script oracle-renew-schema.psql
    
    for manual setup, copy/paste the script SQLDeveloper and execute
*/
set serveroutput on size unlimited;
begin
   for cur_rec in (select object_name, object_type
                   from user_objects
                   where object_type in ('TABLE', 'VIEW', 'MATERIALIZED VIEW', 'PACKAGE', 'PROCEDURE', 'FUNCTION', 'SEQUENCE', 'SYNONYM', 'PACKAGE BODY')
				   )
   loop
      begin
         if cur_rec.object_type = 'TABLE'
         then
            execute immediate 'drop ' || cur_rec.object_type || ' ' || cur_rec.object_name || ' cascade constraints';
         else
            execute immediate 'drop ' || cur_rec.object_type || ' ' || cur_rec.object_name;
         end if;
     exception
         when others
         then
            dbms_output.put_line ('FAILED: drop ' || cur_rec.object_type || ' ' || cur_rec.object_name);
      end;
   end loop;
   for cur_rec in (select * 
                   from all_synonyms 
                   where table_owner in (select user from dual))
   loop
      begin
         execute immediate 'drop public synonym ' || cur_rec.synonym_name;
      end;
   end loop;
   
   execute immediate 'purge recyclebin';
end;
/
#endif

