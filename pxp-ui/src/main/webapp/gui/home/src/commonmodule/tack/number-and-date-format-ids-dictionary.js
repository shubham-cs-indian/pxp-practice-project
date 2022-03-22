const oNumberFormatIdDictionary = function () {
  return {
    NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_COMMA_DOT: "###,###,###.##",
    NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_DOT_COMMA: "###.###.###,##",
    NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_SPACE_COMMA: "### ### ###,##",
    NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_APOSTROPHE_DOT: "###’###’###.##",
    NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_SPACE_DOT: "### ### ###.##",
    NUMBER_FORMAT_NO_GROUPING_WITH_DOT: "#########.##",
    NUMBER_FORMAT_NO_GROUPING_WITH_COMMA: "#########,##",
    NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_COMMA_SLASH: "###,###,###/##",
    NUMBER_FORMAT_TWO_DIGIT_GROUPING_WITH_COMMA_DOT: "##,##,##,###.##"
  }
};

const oDateFormatIdDictionary = function () {
  return {
    "YYYY-MM-DD_24_HOURS": "YYYY-MM-DD HH:mm:ss",
    "DD-MM-YYYY_24_HOURS": "DD-MM-YYYY HH:mm:ss",
    "DD-MM-YYYY_12_HOURS": "DD-MM-YYYY hh:mm:ss",
    "MM/DD/YYYY_24_HOURS": "MM/DD/YYYY HH:mm:ss",
    "MM/DD/YYYY_12_HOURS": "MM/DD/YYYY hh:mm:ss",
    "DD-MM-YY_24_HOURS"   : "DD-MM-YY HH:mm:ss",
    "M/D/YYYY_24_HOURS"   : "M/D/YYYY HH:mm:ss",
    "D-M-YYYY_24_HOURS"   : "D-M-YYYY HH:mm:ss",
    "D.M.YYYY_24_HOURS"   : "D.M.YYYY HH:mm:ss",
    "D.MM.YYYY_24_HOURS"  : "D.MM.YYYY HH:mm:ss",
    "D/M/YY_24_HOURS"     : "D/M/YY HH:mm:ss",
    "D/MM/YYYY_24_HOURS"  : "D/MM/YYYY HH:mm:ss",
    "DD.M.YYYY_24_HOURS"  : "DD.M.YYYY HH:mm:ss",
    "DD.MM.YY_24_HOURS"   : "DD.MM.YY HH:mm:ss",
    "DD/MM/YYYY_24_HOURS" : "DD/MM/YYYY HH:mm:ss",
    "DD/MM/YY_24_HOURS"   : "DD/MM/YY HH:mm:ss",
    "YYYY-M-D_24_HOURS"   : "YYYY-M-D HH:mm:ss",
    "YYYY.MM.DD_24_HOURS" : "YYYY.MM.DD HH:mm:ss",
    "YYYY/M/D_24_HOURS"   : "YYYY/M/D HH:mm:ss",
    "YYYY/MM/DD_24_HOURS" : "YYYY/MM/DD HH:mm:ss",
    "D-MMM YY_24_HOURS": "D-MMM YY HH:mm:ss",
    "D/M/YYYY_24_HOURS": "D/M/YYYY HH:mm:ss",
    "DD.MM.YYYY_24_HOURS": "DD.MM.YYYY HH:mm:ss",
  }
};

export const numberFormatIds = oNumberFormatIdDictionary;
export const dateFormatIds = oDateFormatIdDictionary;