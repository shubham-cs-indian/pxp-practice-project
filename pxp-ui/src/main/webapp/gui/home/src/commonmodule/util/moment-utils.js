import {find, isEmpty, isNaN} from '../../libraries/cs/cs-lodash';
import SessionStorageManager from './../../libraries/sessionstoragemanager/session-storage-manager';
import SessionProps from './../props/session-props';
import MicroEvent from '../../libraries/microevent/MicroEvent.js';
import Moment from 'moment';
import DateFormatForDataLanguageDictionary from '../../commonmodule/tack/date-format-for-data-language-dictionary';
import MomentProps from '../props/moment-props';
import MockDataForLocales from '../../commonmodule/tack/mock-data-for-locale-language';
import oLocalesDictionary from '../tack/locales-dictionary';
import SessionStorageConstants from '../tack/session-storage-constants';
const oDateFormatDictionary = new DateFormatForDataLanguageDictionary();

var MomentUtils = (function () {

  let _triggerChange = function () {
    MomentUtils.trigger('moment-data-changed');
  };

  let _getSelectedDataLanguage = () => {
    let sDataLanguageId = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);
    let oLanguageInfoData = SessionProps.getLanguageInfoData();
    let oDataLanguages = oLanguageInfoData.dataLanguages;
    return find(oDataLanguages, {code: sDataLanguageId});
  };

  let _getSelectedUILanguage = () => {
    let sUILanguageId = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE);
    let oLanguageInfoData = SessionProps.getLanguageInfoData();
    let aUILanguages = oLanguageInfoData.userInterfaceLanguages;
    return find(aUILanguages, {code: sUILanguageId});
  };

  let _setMomentLocale = () => {
    let oSelectedDataLanguage = _getSelectedUILanguage();
    let oLocale = find(MockDataForLocales, {id: oSelectedDataLanguage.localeId});
    let sLocale = (oLocale && oLocale.locale) || oLocalesDictionary.THREE_DIGIT_GROUPING_WITH_GROUP_SEPARATOR_COMMA_AND_DECIMAL_SEPARATOR_DOT;
    Moment.locale(sLocale);
  };

  let _setCurrentDateFormat = function (oSelectedDataLanguage) {
    if (isEmpty(oSelectedDataLanguage)) {
      oSelectedDataLanguage = _getSelectedDataLanguage();
    }

    let oCurrentDateFormat = oDateFormatDictionary[oSelectedDataLanguage.dateFormat];
    MomentProps.setCurrentDateFormat(oCurrentDateFormat);
    _triggerChange();
  };

  let _getStandardDateTimeFormat = function () {
    let oCurrentDateFormat = MomentProps.getCurrentDateFormat();
    if(isEmpty(oCurrentDateFormat)) {
      _setCurrentDateFormat();
      oCurrentDateFormat = MomentProps.getCurrentDateFormat();
    }

    return oCurrentDateFormat;
  };

  let _getDateAttributeInTimeFormat = function (sValue, oDateFormat) {
    oDateFormat = !isEmpty(oDateFormat) ? oDateFormat : _getStandardDateTimeFormat();
    let sDateFormat = oDateFormat.dateFormat;

    //_setMomentLocale();
    sValue =  +sValue ? +sValue : "";
    let oDate = new Date(sValue);
    if (isNaN(Date.parse(oDate))) {
      return '';
    }
    let oCurrentDateMoment = Moment(oDate);
    return oCurrentDateMoment.format(sDateFormat);
  };

  let _getDateInSpecifiedDateTimeFormat = function (sValue, oDateFormat) {
    let sDateFormat = oDateFormat.dateFormat;
    let sTimeFormat = oDateFormat.timeFormat;

    //_setMomentLocale();
    let oDate = new Date(+sValue);
    if (isNaN(Date.parse(oDate))) {
      return '';
    }
    let oCurrentDateMoment = Moment(oDate);
    let sDate = oCurrentDateMoment.format(sDateFormat);
    let sTime = oCurrentDateMoment.format(sTimeFormat);

    return ({date: sDate, time: sTime});
  }

  let _getDateAttributeInDateTimeFormat = function (sValue) {
    let oDateFormat = _getStandardDateTimeFormat();
    return _getDateInSpecifiedDateTimeFormat(sValue, oDateFormat);
  };

  let _getShortDate = function (iTimeStamp) {
    //_setMomentLocale();
    var oDate = new Date(iTimeStamp);
    if (isNaN(Date.parse(oDate))) {
      return '';
    }

    var oCurrentDateMoment = Moment(oDate);
    let oDateFormat = _getStandardDateTimeFormat();
    let sDateFormat = oDateFormat.dateFormat;
    return oCurrentDateMoment.format(sDateFormat);
  };

  /*let _getFullTime = function (iTimeStamp) {
    //_setMomentLocale();
    var oDate = new Date(iTimeStamp);
    if (isNaN(Date.parse(oDate))) {
      return '';
    }

    var oCurrentDateMoment = Moment(oDate);
    let oDateFormat = _getStandardDateTimeFormat();
    let sTimeFormat = oDateFormat.dateFormat;
    return oCurrentDateMoment.format(sTimeFormat);
  };*/

  let _getMomentOfDate = function (sDate) {
    if(!sDate) {
      return Moment();
    }
    return Moment(sDate);
  };

  let _isDateLessThanGivenDays = function (iTimeStamp, sDuration) {
    let oGivenDate = Moment(iTimeStamp);
    let oCurrentDate = Moment();
    return (oCurrentDate.diff(oGivenDate, sDuration, true));
  };

  let _getCurrentLocale = function () {
    return Moment.locale();
  };

  let _getTimeDifference = function (sStartTime, sEndTime, sType = "seconds") {
    return Moment.duration(Moment(sEndTime).diff(Moment(sStartTime))).as(sType);
  }

  return {
    getDateAttributeInTimeFormat: function (sValue, oDateFormat) {
      return _getDateAttributeInTimeFormat(sValue, oDateFormat);
    },

    getDateAttributeInDateTimeFormat: function (sValue) {
      return _getDateAttributeInDateTimeFormat(sValue)
    },

    getDateInSpecifiedDateTimeFormat: function (sValue, oDateFormat) {
      return _getDateInSpecifiedDateTimeFormat(sValue, oDateFormat)
    },

    getShortDate: function (iTimeStamp) {
      return _getShortDate(iTimeStamp);
    },

    getMomentOfDate: function (sDate) {
      return _getMomentOfDate(sDate);
    },

    getCurrentLocale: function () {
      return _getCurrentLocale();
    },

    setMomentLocale: function () {
     return _setMomentLocale();
    },

    isDateLessThanGivenDays: function (iTimeStamp, sDuration) {
      return _isDateLessThanGivenDays(iTimeStamp, sDuration);
    },

    setCurrentDateFormat: function (oSelectedDataLanguage) {
      _setCurrentDateFormat(oSelectedDataLanguage);
    },

    getStandardDateTimeFormat: function() {
      return _getStandardDateTimeFormat();
    },

    getTimeDifference: function (sStartTime, sEndTime, sType) {
      return _getTimeDifference(sStartTime, sEndTime, sType);
    }

  }
})();

MicroEvent.mixin(MomentUtils);

export default MomentUtils;