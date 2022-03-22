#!/bin/bash
# Script to run GCC command
# Make a copy of all files with .sql extension to .c extension 
for f in *.sql; do 
    echo cp "$f" "${f%.*}.c"
    cp "$f" "${f%.*}.c"
done

#Mkdir for PGSQL11 and ORACLE12C
mkdir PGSQL11
mkdir ORACLE12C

# GCC command
# Rename all *.txt to *.text
for f in *.c; do 
    echo gcc -E "$f" -D PGSQL11 -o "PGSQL11/${f%.*}.sql"
    gcc -E "$f" -D PGSQL11 -o "PGSQL11/${f%.*}.sql"

    echo gcc -E "$f" -D ORACLE12C -o "ORACLE12C/${f%.*}.sql"
    gcc -E "$f" -D ORACLE12C -o "ORACLE12C/${f%.*}.sql"
done

# Clean SQL files
for f in *.sql;do

   
   awk -f command.awk PGSQL11/$f > PGSQL11/$f.tmp && mv PGSQL11/$f.tmp PGSQL11/$f
   awk -v a="oracle" -f command.awk ORACLE12C/$f > ORACLE12C/$f.tmp && mv ORACLE12C/$f.tmp ORACLE12C/$f

done

# Remove temporary C files created for GCC
rm *.c
