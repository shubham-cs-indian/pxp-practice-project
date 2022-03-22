/*
 Take for entry the result of JAVADescriptor parsing and produce an SQL script
 containing table entry with enum codes
*/
const fs = require('fs');
const process = require('process');
const {exec} = require('child_process');

// constants:
const SOURCE_INTERFACE_DIR = "../../../../../pxp-rdbms-interface/src/main/java/com/cs";
const OUTPUT_SQL_FILE = "../../../main/sql/1ac-Db-infrastructure-DDL.sql";
const OUTPUT_SQL_INC = "../../../main/sql/include/enum-constants.sql";
const JAR_DIR = "../../programs";
const JAVA_DESCRIPTOR_JAR = JAR_DIR + "/JAVADescriptor.jar";
const ENUM_TABLE_ROOTNAME = "enumCode";
const ENUM_TABLE = "pxp."  + ENUM_TABLE_ROOTNAME;
const INSERT_INTO = "insert into " + ENUM_TABLE + " values";
const RUN_JAVA_DESC_CMD = "java -jar " + JAVA_DESCRIPTOR_JAR + " -i " + SOURCE_INTERFACE_DIR;

// format in %02d
function format02d( number) {
    return ( number < 10 ? "0" : "" )  + number;
}

// return current system date
function systimeString() {
    let now = new Date();
    return now.getFullYear() 
            + "-" + format02d(now.getMonth()+1) 
            + "-" + format02d(now.getDate())
            + " " + format02d( now.getHours())
            + ":" + format02d( now.getMinutes())
            + ":" + format02d( now.getSeconds());
 }

// take the interface descriptions and turn them into SQL insert commands
function description2Enums( txtInput) {
    let sqlDeclarations = "";
    let lines = txtInput.split(/\r?\n/); // split by portable new line
    lines.forEach( ( line )=> {
        if ( line.length > 0 ) {
            let interfaceDef = JSON.parse( "{" + line + "}" );
            let interfaceLongName = Object.keys(interfaceDef)[0];
            interfaceDef = interfaceDef[interfaceLongName];
            //process.stdout.write( interfaceDef.interface + ":" + JSON.stringify(interfaceDef) + "\n\n");
            Object.keys( interfaceDef.enums).forEach( ( enumName ) => { 
                process.stdout.write( interfaceDef.interface + "." + enumName + "\n");
                let enumCode = 0;
                interfaceDef.enums[enumName].forEach( function( enumConst) {
                    sqlDeclarations += INSERT_INTO + "( '" + interfaceDef.interface + "', '" +
                            enumName + "', _ENUMTYPE_" + enumName.toUpperCase() + ", '" + enumConst + "', " + (enumCode++) + ");\n";
                });
            });
        }
    });
    return sqlDeclarations;
}

// Wrap up everything from JSON interface descriptions
function writeSQLEnums( enumDeclarations) {
    // SQL Table definition
    var sqlTableDef = "-- GENERATED FROM INTERFACE DEFINITIONS:\n"
                    + "-- on " + systimeString() + " --\n"
                    + '#include "./include/sql-defs.sql"\n'
                    + '#include "./include/enum-constants.sql"\n'
                    + "_TABLE(" + ENUM_TABLE + ") (\n"
                    + "    interface     _VARCHAR,\n"
                    + "    enum          _VARCHAR,\n"
                    + "    type          _SHORT,\n"
                    + "    label         _VARCHAR,\n"
                    + "    code          _SHORT,\n"
                    + "    primary key( type, code)\n"
                    + ");\n"
                    + "_CREATE_INDEX1( " + ENUM_TABLE_ROOTNAME + ", type);\n\n";
    fs.writeFileSync( OUTPUT_SQL_FILE, sqlTableDef + enumDeclarations, "UTF-8",{'flags': 'w+'});
} 

// take the interface descriptions and turn them into a set of precompiler constants
function description2Consts( txtInput) {
    let constDefinitions = "";
    let lines = txtInput.split(/\r?\n/); // split by portable new line
    let typeNo = 0;
    lines.forEach( ( line )=> {
        if ( line.length > 0 ) {
            let interfaceDef = JSON.parse( "{" + line + "}" );
            let interfaceLongName = Object.keys(interfaceDef)[0];
            interfaceDef = interfaceDef[interfaceLongName];
            Object.keys( interfaceDef.enums).forEach( ( enumName ) => { 
                constDefinitions += "\n/* " + interfaceDef.interface + "." + enumName + " */\n";
                constDefinitions += "#define _ENUMTYPE_" + enumName.toUpperCase() + "\t\t" + typeNo + "\n\n";
                let enumCode = 0;
                interfaceDef.enums[enumName].forEach( function( enumConst) {
                    constDefinitions += "#define _" + enumName + "_" + enumConst + 
                                "\t\t" + enumCode + "\n";
                    enumCode++;
                });
                typeNo++;
            });
        }
    });
    return constDefinitions;
}

function writePCCEnums( enumConstants ) {
    // SQL Table definition
    var  header = "/* GENERATED FROM INTERFACE DEFINITIONS:\n"
                    + "-- on " + systimeString() + " -- */\n"
                    + "/* Special property constants */\n"
                    + "#define _PSEUDO_ENTITY_IID   -1\n"
                    + "#define _ADMIN_USER_IID      10\n"
                    + "#define _SINGLE_ARTICLE_IID  20\n"
                    + "#define _UNDEFINED       0\n"
                    + "#define _ARTICLE_NAME_PROPERTY_IID  200 + _StandardProperty_nameattribute\n"
                    + "#define _CREATED_BY_PROPERTY_IID    200 + _StandardProperty_createdbyattribute\n"
                    + "#define _CREATED_ON_PROPERTY_IID    200 + _StandardProperty_createdonattribute\n"
                    + "#define _MODIFIED_BY_PROPERTY_IID   200 + _StandardProperty_lastmodifiedbyattribute\n"
                    + "#define _MODIFIED_ON_PROPERTY_IID   200 + _StandardProperty_lastmodifiedattribute\n"
                    + "\n"
                    + "#define _PRODUCT_IMAGE_PROPERTY_IID   200 + _StandardProperty_standardArticleAssetRelationship\n"
                    + "\n";
    fs.writeFileSync( OUTPUT_SQL_INC, header + enumConstants, "UTF-8",{'flags': 'w+'});
} 

// run Java Descriptor and retrieve JSON interface descriptions
process.stdout.write( ">> Start from root source: " + SOURCE_INTERFACE_DIR + "\n");

exec( RUN_JAVA_DESC_CMD, {maxBuffer : 500 * 1024}, ( err, stdout, stderr) => {
    if ( err ) {
        console.log( "System error " + err);
        process.exit(1);
    }
    let enumDeclarations = description2Enums( stdout.toString('utf8'));
    writeSQLEnums( enumDeclarations);
    let enumConstants = description2Consts( stdout.toString('utf8'));
    writePCCEnums( enumConstants);
});
