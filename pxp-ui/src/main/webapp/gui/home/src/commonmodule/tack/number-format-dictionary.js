import {numberFormatIds} from "./number-and-date-format-ids-dictionary";
import oLocalesDictionary from './locales-dictionary';
const oNumberFormatIdDictionary = numberFormatIds();

const oNumberFormatDictionary = function () {
  return {
    [oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_COMMA_DOT]:
        {
          id: oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_COMMA_DOT,
          label: oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_COMMA_DOT,
          numberFormat: oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_COMMA_DOT,
          locale: oLocalesDictionary.THREE_DIGIT_GROUPING_WITH_GROUP_SEPARATOR_COMMA_AND_DECIMAL_SEPARATOR_DOT,
          decimalSeparator: ".",
          negativeNumberRegex: /^-?[0-9]\d*(\.\d*)?$/,
          positiveNumberRegex: /^[0-9]\d*(\.\d*)?$/,
          useGrouping: true
        },
    [oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_DOT_COMMA]:
        {
          id: oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_DOT_COMMA,
          label: oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_DOT_COMMA,
          numberFormat: oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_DOT_COMMA,
          locale: oLocalesDictionary.THREE_DIGIT_GROUPING_WITH_GROUP_SEPARATOR_DOT_AND_DECIMAL_SEPARATOR_COMMA,
          decimalSeparator: ",",
          negativeNumberRegex: /^-?[0-9]\d*(\,\d*)?$/,
          positiveNumberRegex: /^[0-9]\d*(\,\d*)?$/,
          useGrouping: true
        }
    ,
    [oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_SPACE_COMMA]:
        {
          id: oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_SPACE_COMMA,
          label: oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_SPACE_COMMA,
          numberFormat: oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_SPACE_COMMA,
          locale: oLocalesDictionary.THREE_DIGIT_GROUPING_WITH_GROUP_SEPARATOR_SPACE_AND_DECIMAL_SEPARATOR_COMMA,
          decimalSeparator: ",",
          negativeNumberRegex: /^-?[0-9]\d*(\,\d*)?$/,
          positiveNumberRegex: /^[0-9]\d*(\,\d*)?$/,
          useGrouping: true
        }
    ,
    [oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_APOSTROPHE_DOT]:
        {
          id: oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_APOSTROPHE_DOT,
          label: oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_APOSTROPHE_DOT,
          numberFormat: oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_APOSTROPHE_DOT,
          locale: oLocalesDictionary.THREE_DIGIT_GROUPING_WITH_GROUP_SEPARATOR_APOSTROPHE_AND_DECIMAL_SEPARATOR_DOT, //it-CH
          decimalSeparator: ".",
          negativeNumberRegex: /^-?[0-9]\d*(\.\d*)?$/,
          positiveNumberRegex: /^[0-9]\d*(\.\d*)?$/,
          useGrouping: true
        }
    ,
    [oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_SPACE_DOT]:
        {
          id: oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_SPACE_DOT,
          label: oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_SPACE_DOT,
          numberFormat: oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_SPACE_DOT,
          locale: oLocalesDictionary.THREE_DIGIT_GROUPING_WITH_GROUP_SEPARATOR_SPACE_AND_DECIMAL_SEPARATOR_COMMA,
          decimalSeparator: ".",
          negativeNumberRegex: /^-?[0-9]\d*(\.\d*)?$/,
          positiveNumberRegex: /^[0-9]\d*(\.\d*)?$/,
          useGrouping: true
        }
    ,
    [oNumberFormatIdDictionary.NUMBER_FORMAT_NO_GROUPING_WITH_DOT]:
        {
          id: oNumberFormatIdDictionary.NUMBER_FORMAT_NO_GROUPING_WITH_DOT,
          label: oNumberFormatIdDictionary.NUMBER_FORMAT_NO_GROUPING_WITH_DOT,
          numberFormat: oNumberFormatIdDictionary.NUMBER_FORMAT_NO_GROUPING_WITH_DOT,
          locale: oLocalesDictionary.THREE_DIGIT_GROUPING_WITH_GROUP_SEPARATOR_COMMA_AND_DECIMAL_SEPARATOR_DOT,
          decimalSeparator: ".",
          negativeNumberRegex: /^-?[0-9]\d*(\.\d*)?$/,
          positiveNumberRegex: /^[0-9]\d*(\.\d*)?$/,
          useGrouping: false
        }
    ,
    [oNumberFormatIdDictionary.NUMBER_FORMAT_NO_GROUPING_WITH_COMMA]:
        {
          id: oNumberFormatIdDictionary.NUMBER_FORMAT_NO_GROUPING_WITH_COMMA,
          label: oNumberFormatIdDictionary.NUMBER_FORMAT_NO_GROUPING_WITH_COMMA,
          numberFormat: oNumberFormatIdDictionary.NUMBER_FORMAT_NO_GROUPING_WITH_COMMA,
          locale: oLocalesDictionary.THREE_DIGIT_GROUPING_WITH_GROUP_SEPARATOR_COMMA_AND_DECIMAL_SEPARATOR_DOT,
          decimalSeparator: ",",
          negativeNumberRegex: /^-?[0-9]\d*(\,\d*)?$/,
          positiveNumberRegex: /^[0-9]\d*(\,\d*)?$/,
          useGrouping: false
        }
    ,
    [oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_COMMA_SLASH]:
        {
          id: oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_COMMA_SLASH,
          label: oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_COMMA_SLASH,
          numberFormat: oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_COMMA_SLASH,
          locale: oLocalesDictionary.THREE_DIGIT_GROUPING_WITH_GROUP_SEPARATOR_COMMA_AND_DECIMAL_SEPARATOR_DOT,
          decimalSeparator: "/",
          negativeNumberRegex: /^-?[0-9]\d*(\/\d*)?$/,
          positiveNumberRegex: /^[0-9]\d*(\/\d*)?$/,
          useGrouping: true
        }
    ,
    [oNumberFormatIdDictionary.NUMBER_FORMAT_TWO_DIGIT_GROUPING_WITH_COMMA_DOT]:
        {
          id: oNumberFormatIdDictionary.NUMBER_FORMAT_TWO_DIGIT_GROUPING_WITH_COMMA_DOT,
          label: oNumberFormatIdDictionary.NUMBER_FORMAT_TWO_DIGIT_GROUPING_WITH_COMMA_DOT,
          numberFormat: oNumberFormatIdDictionary.NUMBER_FORMAT_TWO_DIGIT_GROUPING_WITH_COMMA_DOT,
          locale: oLocalesDictionary.TWO_DIGIT_GROUPING_WITH_GROUP_SEPARATOR_COMMA_AND_DECIMAL_SEPARATOR_DOT,
          decimalSeparator: ".",
          negativeNumberRegex: /^-?[0-9]\d*(\.\d*)?$/,
          positiveNumberRegex: /^[0-9]\d*(\.\d*)?$/,
          useGrouping: true
        }
  }
};

export default oNumberFormatDictionary;