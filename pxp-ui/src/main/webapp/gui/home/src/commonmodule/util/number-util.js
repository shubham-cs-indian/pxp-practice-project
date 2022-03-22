import SessionProps from './../props/session-props';
import NumberFormatDictionary from '../../commonmodule/tack/number-format-dictionary';
import {numberFormatIds} from "../../commonmodule/tack/number-and-date-format-ids-dictionary";
import {isEmpty, toString, find, isNumber, isNaN} from '../../libraries/cs/cs-lodash';
import MicroEvent from '../../libraries/microevent/MicroEvent.js';
import NumberProps from '../props/number-props';
import MetricCheckConstants from '../../commonmodule/tack/measurement-metrics-constants';
import SessionStorageManager from '../../libraries/sessionstoragemanager/session-storage-manager';
import MomentUtils from './moment-utils';
import SessionStorageConstants from '../tack/session-storage-constants';
const oNumberFormatDictionary = new NumberFormatDictionary();
const oNumberFormatIdDictionary = numberFormatIds();

var NumberUtils = (function () {

  let _triggerChange = function () {
    NumberUtils.trigger('number-data-changed');
  };

  let _setCurrentNumberFormat = (oSelectedDataLanguage) => {
    if (isEmpty(oSelectedDataLanguage)) {
      let sDataLanguageId = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);
      let oLanguageInfoData = SessionProps.getLanguageInfoData();
      let oDataLanguages = oLanguageInfoData.dataLanguages;
      oSelectedDataLanguage = find(oDataLanguages, {code: sDataLanguageId});
    }

    let oCurrentNumberFormat = oNumberFormatDictionary[oSelectedDataLanguage.numberFormat];
    NumberProps.setCurrentNumberFormat(oCurrentNumberFormat);
    _triggerChange();
  };

  let _getSelectedNumberFormatByDataLanguage = () => {
    let oCurrentNumberFormat = NumberProps.getCurrentNumberFormat();

    if(isEmpty(oCurrentNumberFormat)) {
      _setCurrentNumberFormat();
      oCurrentNumberFormat = NumberProps.getCurrentNumberFormat();
    }

    return oCurrentNumberFormat;
  };

  let _getNumberAccordingToPrecision =(sValue, iPrecision = 0, sDecimalSeparator, oNumberFormat)=> {
    oNumberFormat = !isEmpty(oNumberFormat) ? oNumberFormat : _getSelectedNumberFormatByDataLanguage();
    let sSplitter = !isEmpty(sDecimalSeparator) ? sDecimalSeparator : oNumberFormat.decimalSeparator;

    if (sSplitter != null && (isNumber(iPrecision) || iPrecision === 0)) {
      sValue = toString(sValue);
      var aSplits = sValue.split(sSplitter);
      if (aSplits[1] && (aSplits[1].length > iPrecision)) {
        if(iPrecision === 0) {
          sValue = aSplits[0];
        } else {
          let sTruncatedValue = aSplits[1].substring(0, iPrecision);
          sValue = aSplits[0] + sSplitter + sTruncatedValue;
        }
      }
    }

    return sValue;
  };

  let _getValueToShowAccordingToNumberFormat = (sValue, iPrecision, oNumberFormat, bDisableFormatByLocale) => {
    iPrecision = isNaN(iPrecision) ? 0 : iPrecision;
    oNumberFormat = !isEmpty(oNumberFormat) ? oNumberFormat : _getSelectedNumberFormatByDataLanguage();
    let sLocale = oNumberFormat.locale;
    let sSelectedNumberFormat = oNumberFormat.numberFormat;

    sValue = _getNumberAccordingToPrecision(sValue, iPrecision, MetricCheckConstants.DEFAULT_DECIMAL_SEPARATOR, oNumberFormat);
    sValue = bDisableFormatByLocale ? sValue :
             _getFormattedNumberByLocale(sValue, iPrecision, oNumberFormat.useGrouping, sLocale);

    return _processNumberForCustomisedNumberFormat(sValue, sSelectedNumberFormat);
  };

  /** For expected number format, if locale is not present, then used other locale for grouping &
   * replaced decimal separator to get expected format */
  let _processNumberForCustomisedNumberFormat = function (sValue, sSelectedNumberFormat) {
    switch (sSelectedNumberFormat) {
      case oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_SPACE_DOT:
        return sValue.replace(/,/g, '.');

      case oNumberFormatIdDictionary.NUMBER_FORMAT_NO_GROUPING_WITH_COMMA:
        return sValue.replace(/\./g, ',');

      case oNumberFormatIdDictionary.NUMBER_FORMAT_THREE_DIGIT_GROUPING_WITH_COMMA_SLASH:
        return sValue.replace(/\./g, '/');
    }

    return sValue;
  };

  let _getFormattedNumberByLocale = function (sValue, iPrecision, bUseGrouping, sLocale) {
    sValue = toString(sValue);
    let oNumberFormatOptions = {
      maximumFractionDigits: isNumber(iPrecision) ? iPrecision : 0,
      useGrouping: bUseGrouping
    };

    if(!isEmpty(sValue)) {
      return new Intl.NumberFormat(sLocale, oNumberFormatOptions).format(sValue);
    } else {
      return sValue;
    }
  };

  let _getNumberAccordingToLocale = (iNumber) => {
    let sCurrentLocale = MomentUtils.getCurrentLocale();
    return new Intl.NumberFormat(sCurrentLocale).format(iNumber) || iNumber;
  };

  return {
    getNumberAccordingToLocale: function (iNumber) {
      return _getNumberAccordingToLocale(iNumber);
    },

    getNumberAccordingToPrecision: function (sValue, iPrecision, sDecimalSeparator) {
      return _getNumberAccordingToPrecision(sValue, iPrecision, sDecimalSeparator);
    },

    getSelectedNumberFormatByDataLanguage: function () {
      return _getSelectedNumberFormatByDataLanguage();
    },

    getValueToShowAccordingToNumberFormat: function (sValue, iPrecision, oNumberFormat, bDisableFormatByLocale) {
      return _getValueToShowAccordingToNumberFormat(sValue, iPrecision, oNumberFormat, bDisableFormatByLocale);
    },

    setCurrentNumberFormat: function (oSelectedDataLanguage) {
      _setCurrentNumberFormat(oSelectedDataLanguage);
    }
  }
})();

MicroEvent.mixin(NumberUtils);

export default NumberUtils;