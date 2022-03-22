import CS from '../../libraries/cs';
import AttributeUtils from './attribute-utils';
import CommonUtils from './common-utils';
import ViewUtils from '../../screens/homescreen/screens/contentscreen/view/utils/view-utils';
import FilterAllTypeDictionary from '../tack/filter-type-dictionary';
const FilterAllTypeDictionaryAll = FilterAllTypeDictionary.ALL

var FilterViewUtils = (function () {

  var _getLabelByAttributeType = function (sType, oChild, sDefaultUnit, sPrecision, oFilterContext) {
    var sAttributeVisualType = AttributeUtils.getAttributeTypeForVisual(sType);
    var sFilterLabel = "";
    var sChildType = oChild.type;
    var aTypes = ["contains", "exact", "lt", "gt", "start", "end", "empty", "notempty"];
    var aValueAsNumberTypes = ["measurementMetrics", "number", "calculated"];

    if(sAttributeVisualType == "date") {
      if (CS.includes(aTypes, sChildType)) {
        sFilterLabel = CommonUtils.getDateAttributeInTimeFormat(oChild.value);
      }else {
        sFilterLabel = CommonUtils.getDateAttributeInTimeFormat(oChild.from);
        sFilterLabel += " - "  + CommonUtils.getDateAttributeInTimeFormat(oChild.to);
      }
    }
    else if (CS.includes(aValueAsNumberTypes, sAttributeVisualType)) {
      if (CS.includes(aTypes, sChildType)) {
        sFilterLabel = AttributeUtils.getLabelByAttributeType(sType, oChild.value, sDefaultUnit, sPrecision,
            oChild.hideSeparator || oChild.disableNumberLocaleFormatting);
      }else {
        let sDefaultFromUnit = CS.isNotEmpty(sDefaultUnit) && sDefaultUnit.from ? sDefaultUnit.from : sDefaultUnit;
        let sDefaultToUnit = CS.isNotEmpty(sDefaultUnit) && sDefaultUnit.to ? sDefaultUnit.to : sDefaultUnit;
        sFilterLabel = AttributeUtils.getLabelByAttributeType(sType, oChild.from, sDefaultFromUnit, sPrecision,
            oChild.hideSeparator || oChild.disableNumberLocaleFormatting);
        sFilterLabel += " - "  + AttributeUtils.getLabelByAttributeType(sType, oChild.to, sDefaultToUnit, sPrecision,
            oChild.hideSeparator || oChild.disableNumberLocaleFormatting);
      }
    } else {
      var sValue = oChild.advancedSearchFilter ? oChild.value : CS.getLabelOrCode(oChild);
      sFilterLabel = AttributeUtils.getLabelByAttributeType(sType, sValue, sDefaultUnit, sPrecision,
          oChild.hideSeparator || oChild.disableNumberLocaleFormatting);
    }

    var bIsAdvancedSearchApplied = ViewUtils.getFilterProps(oFilterContext).getAdvancedFilterAppliedStatus();
    let oFilterAllTypeDictionary = new FilterAllTypeDictionaryAll();
    if(bIsAdvancedSearchApplied && !CS.isEmpty(oFilterAllTypeDictionary[sChildType])){
      sFilterLabel = sChildType=="empty" || sChildType=="notempty" ? "" : sFilterLabel;
      sFilterLabel = sFilterLabel + "(" + oFilterAllTypeDictionary[sChildType] +")";
    }

    return sFilterLabel;
  };

  return {
    getLabelByAttributeType: function (sType, oChild, sDefaultUnit, sPrecision, oFilterContext) {
      return _getLabelByAttributeType(sType, oChild, sDefaultUnit, sPrecision, oFilterContext);
    },

    getDateAttributeInTimeFormat: function (sDate) {
      return CommonUtils.getDateAttributeInTimeFormat(sDate);
    }

  };

})();

export default FilterViewUtils;
