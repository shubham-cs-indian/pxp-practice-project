#!/bin/sh
# Script to run the all PGSQL script file
# Please set below params as per your system env.

DATABASE_NAME=pxp
DB_PORT=5433

for file in $SCRIPTS_DIR/*.sql
do
	echo "executing the SQL file $file"
	$psql -h localhost -p $DB_PORT -d $DATABASE_NAME -U pxp -f $file
done