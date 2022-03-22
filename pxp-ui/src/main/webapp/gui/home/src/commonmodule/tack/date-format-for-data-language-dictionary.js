import {dateFormatIds} from "./number-and-date-format-ids-dictionary";
const DateFormatIdDictionary = dateFormatIds();

const oDateFormatDictionary = function () {
  const TIME_HR_MIN_SEC_24_Hours = 'HH:mm:ss';
  const TIME_HR_MIN_SEC_12_Hours = 'hh:mm:ss';

  return {
    [DateFormatIdDictionary["YYYY-MM-DD_24_HOURS"]]:
        {
          id: DateFormatIdDictionary["YYYY-MM-DD_24_HOURS"],
          label: DateFormatIdDictionary["YYYY-MM-DD_24_HOURS"], //TODO : replace with translations
          dateTimeFormat: DateFormatIdDictionary["YYYY-MM-DD_24_HOURS"],
          dateFormat: "YYYY-MM-DD",
          timeFormat: TIME_HR_MIN_SEC_24_Hours
        },
    [DateFormatIdDictionary["DD-MM-YYYY_24_HOURS"]]:
        {
          id: DateFormatIdDictionary["DD-MM-YYYY_24_HOURS"],
          label: DateFormatIdDictionary["DD-MM-YYYY_24_HOURS"],
          dateTimeFormat: DateFormatIdDictionary["DD-MM-YYYY_24_HOURS"],
          dateFormat: "DD-MM-YYYY",
          timeFormat: TIME_HR_MIN_SEC_24_Hours
        },
    [DateFormatIdDictionary["DD-MM-YYYY_12_HOURS"]]:
        {
          id: DateFormatIdDictionary["DD-MM-YYYY_12_HOURS"],
          label: DateFormatIdDictionary["DD-MM-YYYY_12_HOURS"],
          dateTimeFormat: DateFormatIdDictionary["DD-MM-YYYY_12_HOURS"],
          dateFormat: "DD-MM-YYYY",
          timeFormat: TIME_HR_MIN_SEC_12_Hours
        },
    [DateFormatIdDictionary["MM/DD/YYYY_24_HOURS"]]:
        {
          id: DateFormatIdDictionary["MM/DD/YYYY_24_HOURS"],
          label: DateFormatIdDictionary["MM/DD/YYYY_24_HOURS"],
          dateTimeFormat: DateFormatIdDictionary["MM/DD/YYYY_24_HOURS"],
          dateFormat: "MM/DD/YYYY",
          timeFormat: TIME_HR_MIN_SEC_24_Hours
        },
    [DateFormatIdDictionary["MM/DD/YYYY_12_HOURS"]]:
        {
          id: DateFormatIdDictionary["MM/DD/YYYY_12_HOURS"],
          label: DateFormatIdDictionary["MM/DD/YYYY_12_HOURS"],
          dateTimeFormat: DateFormatIdDictionary["MM/DD/YYYY_12_HOURS"],
          dateFormat: "MM/DD/YYYY",
          timeFormat: TIME_HR_MIN_SEC_12_Hours
        },
    [DateFormatIdDictionary["M/D/YYYY_24_HOURS"]]:
        {
          id: DateFormatIdDictionary["M/D/YYYY_24_HOURS"],
          label: DateFormatIdDictionary["M/D/YYYY_24_HOURS"],
          dateTimeFormat: DateFormatIdDictionary["M/D/YYYY_24_HOURS"],
          dateFormat: "M/D/YYYY",
          timeFormat: TIME_HR_MIN_SEC_24_Hours
        },
    [DateFormatIdDictionary["D-M-YYYY_24_HOURS"]]:
        {
          id: DateFormatIdDictionary["D-M-YYYY_24_HOURS"],
          label: DateFormatIdDictionary["D-M-YYYY_24_HOURS"],
          dateTimeFormat: DateFormatIdDictionary["D-M-YYYY_24_HOURS"],
          dateFormat: "D-M-YYYY",
          timeFormat: TIME_HR_MIN_SEC_24_Hours
        },
    [DateFormatIdDictionary["D.M.YYYY_24_HOURS"]]:
        {
          id: DateFormatIdDictionary["D.M.YYYY_24_HOURS"],
          label: DateFormatIdDictionary["D.M.YYYY_24_HOURS"],
          dateTimeFormat: DateFormatIdDictionary["D.M.YYYY_24_HOURS"],
          dateFormat: "D.M.YYYY",
          timeFormat: TIME_HR_MIN_SEC_24_Hours
        },
    [DateFormatIdDictionary["D.MM.YYYY_24_HOURS"]]:
        {
          id: DateFormatIdDictionary["D.MM.YYYY_24_HOURS"],
          label: DateFormatIdDictionary["D.MM.YYYY_24_HOURS"],
          dateTimeFormat: DateFormatIdDictionary["D.MM.YYYY_24_HOURS"],
          dateFormat: "D.MM.YYYY",
          timeFormat: TIME_HR_MIN_SEC_24_Hours
        },
    [DateFormatIdDictionary["D/M/YY_24_HOURS"]]:
        {
          id: DateFormatIdDictionary["D/M/YY_24_HOURS"],
          label: DateFormatIdDictionary["D/M/YY_24_HOURS"],
          dateTimeFormat: DateFormatIdDictionary["D/M/YY_24_HOURS"],
          dateFormat: "D/M/YY",
          timeFormat: TIME_HR_MIN_SEC_24_Hours
        },
    [DateFormatIdDictionary["D/MM/YYYY_24_HOURS"]]:
        {
          id: DateFormatIdDictionary["D/MM/YYYY_24_HOURS"],
          label: DateFormatIdDictionary["D/MM/YYYY_24_HOURS"],
          dateTimeFormat: DateFormatIdDictionary["D/MM/YYYY_24_HOURS"],
          dateFormat: "D/MM/YYYY",
          timeFormat: TIME_HR_MIN_SEC_24_Hours
        },
    [DateFormatIdDictionary["DD.M.YYYY_24_HOURS"]]:
        {
          id: DateFormatIdDictionary["DD.M.YYYY_24_HOURS"],
          label: DateFormatIdDictionary["DD.M.YYYY_24_HOURS"],
          dateTimeFormat: DateFormatIdDictionary["DD.M.YYYY_24_HOURS"],
          dateFormat: "DD.M.YYYY",
          timeFormat: TIME_HR_MIN_SEC_24_Hours
        },
    [DateFormatIdDictionary["DD.MM.YY_24_HOURS"]]:
        {
          id: DateFormatIdDictionary["DD.MM.YY_24_HOURS"],
          label: DateFormatIdDictionary["DD.MM.YY_24_HOURS"],
          dateTimeFormat: DateFormatIdDictionary["DD.MM.YY_24_HOURS"],
          dateFormat: "DD.MM.YY",
          timeFormat: TIME_HR_MIN_SEC_24_Hours
        },
    [DateFormatIdDictionary["DD/MM/YYYY_24_HOURS"]]:
        {
          id: DateFormatIdDictionary["DD/MM/YYYY_24_HOURS"],
          label: DateFormatIdDictionary["DD/MM/YYYY_24_HOURS"],
          dateTimeFormat: DateFormatIdDictionary["DD/MM/YYYY_24_HOURS"],
          dateFormat: "DD/MM/YYYY",
          timeFormat: TIME_HR_MIN_SEC_24_Hours
        },
    [DateFormatIdDictionary["DD/MM/YY_24_HOURS"]]:
        {
          id: DateFormatIdDictionary["DD/MM/YY_24_HOURS"],
          label: DateFormatIdDictionary["DD/MM/YY_24_HOURS"],
          dateTimeFormat: DateFormatIdDictionary["DD/MM/YY_24_HOURS"],
          dateFormat: "DD/MM/YY",
          timeFormat: TIME_HR_MIN_SEC_24_Hours
        },
    [DateFormatIdDictionary["YYYY-M-D_24_HOURS"]]:
        {
          id: DateFormatIdDictionary["YYYY-M-D_24_HOURS"],
          label: DateFormatIdDictionary["YYYY-M-D_24_HOURS"],
          dateTimeFormat: DateFormatIdDictionary["YYYY-M-D_24_HOURS"],
          dateFormat: "YYYY-M-D",
          timeFormat: TIME_HR_MIN_SEC_24_Hours
        },
    [DateFormatIdDictionary["YYYY.MM.DD_24_HOURS"]]:
        {
          id: DateFormatIdDictionary["YYYY.MM.DD_24_HOURS"],
          label: DateFormatIdDictionary["YYYY.MM.DD_24_HOURS"],
          dateTimeFormat: DateFormatIdDictionary["YYYY.MM.DD_24_HOURS"],
          dateFormat: "YYYY.MM.DD",
          timeFormat: TIME_HR_MIN_SEC_24_Hours
        },
    [DateFormatIdDictionary["YYYY/M/D_24_HOURS"]]:
        {
          id: DateFormatIdDictionary["YYYY/M/D_24_HOURS"],
          label: DateFormatIdDictionary["YYYY/M/D_24_HOURS"],
          dateTimeFormat: DateFormatIdDictionary["YYYY/M/D_24_HOURS"],
          dateFormat: "YYYY/M/D",
          timeFormat: TIME_HR_MIN_SEC_24_Hours
        },
    [DateFormatIdDictionary["YYYY/MM/DD_24_HOURS"]]:
        {
          id: DateFormatIdDictionary["YYYY/MM/DD_24_HOURS"],
          label: DateFormatIdDictionary["YYYY/MM/DD_24_HOURS"],
          dateTimeFormat: DateFormatIdDictionary["YYYY/MM/DD_24_HOURS"],
          dateFormat: "YYYY/MM/DD",
          timeFormat: TIME_HR_MIN_SEC_24_Hours
        },
    [DateFormatIdDictionary["D-MMM YY_24_HOURS"]]:
        {
          id: DateFormatIdDictionary["D-MMM YY_24_HOURS"],
          label: DateFormatIdDictionary["D-MMM YY_24_HOURS"],
          dateTimeFormat: DateFormatIdDictionary["D-MMM YY_24_HOURS"],
          dateFormat: "D-MMM YY",
          timeFormat: TIME_HR_MIN_SEC_24_Hours
        },
    [DateFormatIdDictionary["D/M/YYYY_24_HOURS"]]:
        {
          id: DateFormatIdDictionary["D/M/YYYY_24_HOURS"],
          label: DateFormatIdDictionary["D/M/YYYY_24_HOURS"],
          dateTimeFormat: DateFormatIdDictionary["D/M/YYYY_24_HOURS"],
          dateFormat: "D/M/YYYY",
          timeFormat: TIME_HR_MIN_SEC_24_Hours
        },
    [DateFormatIdDictionary["DD.MM.YYYY_24_HOURS"]]:
        {
          id: DateFormatIdDictionary["DD.MM.YYYY_24_HOURS"],
          label: DateFormatIdDictionary["DD.MM.YYYY_24_HOURS"],
          dateTimeFormat: DateFormatIdDictionary["DD.MM.YYYY_24_HOURS"],
          dateFormat: "DD.MM.YYYY",
          timeFormat: TIME_HR_MIN_SEC_24_Hours
        },
    [DateFormatIdDictionary["DD-MM-YY_24_HOURS"]]:
        {
          id: DateFormatIdDictionary["DD-MM-YY_24_HOURS"],
          label: DateFormatIdDictionary["DD-MM-YY_24_HOURS"],
          dateTimeFormat: DateFormatIdDictionary["DD-MM-YY_24_HOURS"],
          dateFormat: "DD-MM-YY",
          timeFormat: TIME_HR_MIN_SEC_24_Hours
        }
  }
};

export default oDateFormatDictionary;