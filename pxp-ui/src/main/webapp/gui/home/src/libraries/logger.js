let ConsoleLogger = (function () {
    let _bIsDeveloperMode = false;
    let logger = {
        log: () => {},
        debug: () => {},
        error: () => {}
    }

    let _getQueryVariable = function (variable) {
        let query = window.location.search.substring(1);
        let vars = query.split("&");
        for (let i = 0; i < vars.length; i++) {
            let pair = vars[i].split("=");
            if (pair[0] == variable) {
                return pair[1];
            }
        }
    };

    let bIsDeveloperMode = _getQueryVariable("debugMode");
    _bIsDeveloperMode = bIsDeveloperMode === "true" || bIsDeveloperMode === "TRUE";

    if (_bIsDeveloperMode) {
        logger = {
            log: console.log,
            debug: console.debug,
            error: console.error
        }
    }
    return logger;
})()

export let Logger = ConsoleLogger;