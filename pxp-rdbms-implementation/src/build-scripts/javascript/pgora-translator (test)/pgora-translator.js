/* 
 * Take the PGORA sql script and transform them into PGSQL and ORACLE regular sql
 */
const fs = require('fs');
const process = require('process');
const {exec} = require('child_process');

// constants:
const PGORA_SOURCE_DIR = "../../../main/sql";
const DEF_PGSQL = "PGSQL11";
const DEF_ORA = "ORACLE12C";
const SYSTEM_OS = process.platform;
const DEF_LIST = [DEF_PGSQL, DEF_ORA];
const INTERVAL_MS = 100;
const NB_MAX_INTERVAL = 5000 / INTERVAL_MS; // max 5 sec.

// global variables:
var nbSourceFile = 0;
var nbGccRun = 0;
var nbIntervals = 0;

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

// Clean-out any content inserted by gcc and produce the final sql file
function cleanResult(tmpScriptName, defTarget) {
    let sqlContent = "-- Generated on: " + systimeString() + "\n";
    console.log("start cleaning " + tmpScriptName + "/" + defTarget);
    let inputContent = fs.readFileSync(tmpScriptName, 'utf8');
    let inputLines = inputContent.split(/\r?\n/); // split by portable new line
    inputLines.forEach((inputLine) => {
        let line = inputLine.trimRight();
        // remove empty lines
        if (line.length === 0)
            return;
        // remove line starting with a #
        if (line.match(/^[#].*/))
            return;
        // Transform a single ; into ";\n/" for ORACLE scripts
        if (defTarget === DEF_ORA && line === ";") {
            line = ";\n/";
        }
        // Replace all double quotes with single quotes when they are not preceeding by json curly braces
        if (line.match(/["]/) && !line.match(/[{].*[}]/)) {
            line = line.replace(/"/g, "'");
        }
        // Replace sequence __NL__ by a "\n"
        line = line.replace( /__NL__/g, "\n");
        sqlContent += line + "\n";
    });
    let targetScriptName = tmpScriptName.replace(".c", ".sql");
    fs.writeFileSync(targetScriptName, sqlContent, "UTF-8", {'flags': 'w+'});
    // remove the produced C copy that is not necessary anymore
    fs.unlinkSync(tmpScriptName);
}

// Run gcc to take advantage of the preprocessor transformations
function gcc(sourceFileName, targetFileName, defTarget) {
    let gccCmd = "gcc -E '" + sourceFileName + "' -D " + defTarget + " -o '" + targetFileName + "'";
    if (SYSTEM_OS.startsWith("win")) {
        gccCmd = "bash -c \"" + gccCmd + "\"";
    }
    exec(gccCmd, (err, stdout, stderr) => {
        console.log(gccCmd);
        if (err) {
            console.log("GCC exception " + err);
            process.exit(1);
        }
        if (stderr) {
            console.log("GCC errors: " + stderr + "\n");
        }
        if (stdout) {
            console.log("GCC output: " + stdout + "\n");
        }
        cleanResult(targetFileName, defTarget);
        nbGccRun++;
    });
}

// Translate a SQL PGORA file into destination SQL file
function translatePGORA(pgoraScript) {
    let pgoraScriptName = PGORA_SOURCE_DIR + "/" + pgoraScript;
    // Make a c file copy to make it compliant with gcc
    let pgoraTmpScriptName = pgoraScriptName.replace(".sql", ".c");
    fs.copyFile(pgoraScriptName, pgoraTmpScriptName, (error) => {
        if (error) {
            console.log("copy error: " + error);
            process.exit(1);
        }
        DEF_LIST.forEach((defTarget) => {
            let targetDir = PGORA_SOURCE_DIR + "/" + defTarget;
            let pgoraTmpTargetScriptName = (targetDir + "/" + pgoraScript).replace(".sql", ".c");
            gcc(pgoraTmpScriptName, pgoraTmpTargetScriptName, defTarget);
        });
    });
}

function cleanCCopies() {
// Clear all copy C sources:
    fs.readdirSync(PGORA_SOURCE_DIR).forEach((fileName) => {
        if (fileName.endsWith(".c")) {
            fs.unlinkSync(PGORA_SOURCE_DIR + "/" + fileName);
        }
    });
}

// Ensure destination folders are created
DEF_LIST.forEach((def) => {
    try {
        fs.mkdirSync(PGORA_SOURCE_DIR + "/" + def);
    } catch (error) {
        if (error.code !== 'EEXIST') {
            console.log("mkdir error: " + error);
            process.exit(1);
        }
    }
});

// run Java Descriptor and retrieve JSON interface descriptions
process.stdout.write("start translation from " + PGORA_SOURCE_DIR + " / OS: " + SYSTEM_OS + "\n\n");

// Read through the source files and proceed to transform
fs.readdirSync(PGORA_SOURCE_DIR).forEach((fileName) => {
    if (fileName.endsWith(".sql")) {
        nbSourceFile++;
        translatePGORA(fileName);
    }
});

// Wait gcc to have executed all expected runs
var waitGcc = setInterval(function () {
    if (nbGccRun === nbSourceFile * DEF_LIST.length) {
        clearInterval(waitGcc);
        cleanCCopies();
    }
    console.log( "Executed runs: " + nbGccRun + " / " + (nbSourceFile * DEF_LIST.length));
    nbIntervals++;
    if (nbIntervals > NB_MAX_INTERVAL) {
        clearInterval(waitGcc);
        cleanCCopies();
    }
}, INTERVAL_MS);
