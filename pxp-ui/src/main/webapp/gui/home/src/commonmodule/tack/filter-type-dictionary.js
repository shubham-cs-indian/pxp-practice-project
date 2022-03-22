import { getTranslations as getTranslation } from '../store/helper/translation-manager.js';

let fnFILTER_TYPES = function () {
 return {
   exact: getTranslation().EXACT,
   contains: getTranslation().CONTAINS,
   start: getTranslation().STARTS_WITH,
   end: getTranslation().ENDS_WITH,
   empty: getTranslation().IS_EMPTY,
   notempty: getTranslation().IS_NOT_EMPTY
 }
};

let fnFILTER_TYPES_FOR_NUMBER = function () {
 return {
   exact: getTranslation().EXACT,
   lt: getTranslation().LESS_THAN,
   gt: getTranslation().GREATER_THAN,
   contains: getTranslation().CONTAINS,
   start: getTranslation().STARTS_WITH,
   end: getTranslation().ENDS_WITH,
   empty: getTranslation().IS_EMPTY,
   notempty: getTranslation().IS_NOT_EMPTY,
   range: getTranslation().RANGE
 }
};

let fnFILTER_TYPES_FOR_DATE = function () {
 return {
   exact: getTranslation().EXACT,
   lt: getTranslation().LESS_THAN,
   gt: getTranslation().GREATER_THAN,
   empty: getTranslation().IS_EMPTY,
   notempty: getTranslation().IS_NOT_EMPTY,
   range: getTranslation().RANGE
 }
};

let fnFILTER_TYPES_FOR_USER_ATTRS = function () {
 return {
   exact: getTranslation().EXACT,
   empty: getTranslation().IS_EMPTY,
   notempty: getTranslation().IS_NOT_EMPTY,
 }
};

let fnALL = function () {
 return {
   exact: getTranslation().EXACT,
   contains: getTranslation().CONTAINS,
   start: getTranslation().STARTS_WITH,
   end: getTranslation().ENDS_WITH,
   empty: getTranslation().IS_EMPTY,
   notempty: getTranslation().IS_NOT_EMPTY,
   lt: getTranslation().LESS_THAN,
   gt: getTranslation().GREATER_THAN,
   range: getTranslation().RANGE,
 }
};

let  fnFILTER_TYPES_FOR_HTML_AND_TEXT = function () {
  return {
    exact: getTranslation().EXACT,
    contains: getTranslation().CONTAINS,
    start: getTranslation().STARTS_WITH,
    end: getTranslation().ENDS_WITH,
    empty: getTranslation().IS_EMPTY,
    notempty: getTranslation().IS_NOT_EMPTY,
    length_lt : getTranslation().CHARACTER_LESS_THAN,
    length_gt : getTranslation().CHARACTER_GREATER_THAN,
    length_equal : getTranslation().CHARACTER_EQUAL_TO,
    length_range : getTranslation().CHARACTER_RANGE,
    regex: getTranslation().REGEX
  }
};

let  fnFILTER_TYPES_WITH_ISDUPLICATE = function() {
  return {
    exact: getTranslation().EXACT,
    contains: getTranslation().CONTAINS,
    start: getTranslation().STARTS_WITH,
    end: getTranslation().ENDS_WITH,
    empty: getTranslation().IS_EMPTY,
    notempty: getTranslation().IS_NOT_EMPTY
  }
};

let fnFILTER_TYPES_FOR_MEASUREMENT_METRICS = function () {
  return {
    exact: getTranslation().EXACT,
    lt: getTranslation().LESS_THAN,
    gt: getTranslation().GREATER_THAN,
    empty: getTranslation().IS_EMPTY,
    notempty: getTranslation().IS_NOT_EMPTY,
    range: getTranslation().RANGE
  }
};

let fnFILTER_TYPES_FOR_DATE_WITH_ISDUPLICATE = function () {
  return {
    exact: getTranslation().EXACT,
    lt: getTranslation().LESS_THAN,
    gt: getTranslation().GREATER_THAN,
    empty: getTranslation().IS_EMPTY,
    notempty: getTranslation().IS_NOT_EMPTY,
    range: getTranslation().RANGE
  }
};

let fnRULE_CONSTANTS_TYPES = function () {
  return {
    max: getTranslation().MAX,
    min: getTranslation().MIN,
    exact: getTranslation().EXACT,
    contains: getTranslation().CONTAINS,
    empty: getTranslation().IS_EMPTY,
    notempty: getTranslation().IS_NOT_EMPTY,
    is: getTranslation().IS,
    isnot: getTranslation().IS_NOT,
    notcontains: getTranslation().NOT_CONTAINS
  }
};

let fnFILTER_TYPES_FOR_NUMBER_WITH_ISDUPLICATE = function () {
  return {
    exact: getTranslation().EXACT,
    lt: getTranslation().LESS_THAN,
    gt: getTranslation().GREATER_THAN,
    contains: getTranslation().CONTAINS,
    start: getTranslation().STARTS_WITH,
    end: getTranslation().ENDS_WITH,
    empty: getTranslation().IS_EMPTY,
    notempty: getTranslation().IS_NOT_EMPTY,
    range: getTranslation().RANGE,
    /*regex: getTranslation().REGEX*/
  }
};

let  fnFILTER_TYPES_FOR_HTML_AND_TEXT_IN_EFFECT = function () {
  return {
    value: getTranslation().VALUE,
    uppercase: getTranslation().UPPERCASE,
    lowercase: getTranslation().LOWERCASE,
    trim: getTranslation().TRIM,
    propercase: getTranslation().PROPER_CASE,
    replace: getTranslation().REPLACE,
    substring: getTranslation().SUBSTRING,
    attributeValue: getTranslation().ATTRIBUTE_VALUE,
    concat: getTranslation().CONCAT
  }
};

let fnFILTER_TYPES_FOR_CONCATENATED = function () {
  return {
    exact: getTranslation().EXACT,
    empty: getTranslation().IS_EMPTY,
    notempty: getTranslation().IS_NOT_EMPTY,
    length_lt: getTranslation().CHARACTER_LESS_THAN,
    length_gt: getTranslation().CHARACTER_GREATER_THAN,
    length_equal: getTranslation().CHARACTER_EQUAL_TO,
    length_range: getTranslation().CHARACTER_RANGE,
  }
};

let fnFILTER_TYPES_FOR_EFFECT = function () {
  return {
    value: getTranslation().VALUE,
    attributeValue: getTranslation().ATTRIBUTE_VALUE
  }
};

let fnFILTER_TYPES_FOR_TAGS_EFFECT = function () {
  return {
    add: getTranslation().ADD,
    remove: getTranslation().REMOVE,
    replacewith: getTranslation().REPLACE_WITH
  }
};

export default {
  FILTER_TYPES : fnFILTER_TYPES,
  FILTER_TYPES_FOR_NUMBER : fnFILTER_TYPES_FOR_NUMBER,
  FILTER_TYPES_FOR_DATE : fnFILTER_TYPES_FOR_DATE,
  FILTER_TYPES_FOR_USER_ATTRS : fnFILTER_TYPES_FOR_USER_ATTRS,
  ALL : fnALL,
  FILTER_TYPES_WITH_ISDUPLICATE : fnFILTER_TYPES_WITH_ISDUPLICATE,
  FILTER_TYPES_FOR_HTML_AND_TEXT : fnFILTER_TYPES_FOR_HTML_AND_TEXT,
  FILTER_TYPES_FOR_DATE_WITH_ISDUPLICATE : fnFILTER_TYPES_FOR_DATE_WITH_ISDUPLICATE,
  RULE_CONSTANTS_TYPES : fnRULE_CONSTANTS_TYPES,
  FILTER_TYPES_FOR_MEASUREMENT_METRICS : fnFILTER_TYPES_FOR_MEASUREMENT_METRICS,
  FILTER_TYPES_FOR_NUMBER_WITH_ISDUPLICATE : fnFILTER_TYPES_FOR_NUMBER_WITH_ISDUPLICATE,
  FILTER_TYPES_FOR_HTML_AND_TEXT_IN_EFFECT : fnFILTER_TYPES_FOR_HTML_AND_TEXT_IN_EFFECT,
  FILTER_TYPES_FOR_EFFECT : fnFILTER_TYPES_FOR_EFFECT,
  FILTER_TYPES_FOR_CONCATENATED : fnFILTER_TYPES_FOR_CONCATENATED,
  FILTER_TYPES_FOR_TAGS_EFFECT : fnFILTER_TYPES_FOR_TAGS_EFFECT
};