
import CS from '../../../../../../libraries/cs';

import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import SettingUtils from './../helper/setting-utils';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import { AttributeRequestMapping as oAttributeRequestMapping } from '../../tack/setting-screen-request-mapping';
import SettingScreenProps from './../model/setting-screen-props';
import CalculatedAttributeConfigViewProps from './../model/calculated-attribute-config-view-props.js';
import ConfigModulePropertyGroupTypeDictionary from '../../tack/settinglayouttack/config-module-data-model-property-group-type-dictionary';
import ExceptionLogger from '../../../../../../libraries/exceptionhandling/exception-logger';
import GridViewContexts from "../../../../../../commonmodule/tack/grid-view-contexts";
import GridViewStore from "../../../../../../viewlibraries/contextualgridview/store/grid-view-store";

let CalculatedAttributeStore = (function () {

  let _triggerChange = function () {
    CalculatedAttributeStore.trigger('calculated-attribute-changed');
  };

  let _getAllowedAttributesForCalculatedAttribute = function (oPaginationData, sCalculatedAttributeType, sCalculatedAttributeUnit, sAttributeId, bIsForReload) {
    if (bIsForReload && sAttributeId == CalculatedAttributeConfigViewProps.getAttributeId()) {
      return;
    }
    let oPostData = CS.cloneDeep(oPaginationData);
    oPostData.calculatedAttributeType = sCalculatedAttributeType;
    oPostData.calculatedAttributeUnit = sCalculatedAttributeUnit;
    oPostData.attributeId = sAttributeId;
    SettingUtils.csPostRequest(oAttributeRequestMapping.getAllowedAttributesForCalculatedAttribute, {}, oPostData,
        successGetAllowedAttributesForCalculatedAttributeCallback.bind(this, sAttributeId, oPaginationData), failureAllowedAttributesForCalculatedAttributeCallback);
  };

  let successGetAllowedAttributesForCalculatedAttributeCallback = function (sAttributeId, oPaginationData, oResponse) {
    if (oPaginationData.isFirstCall) {
      CalculatedAttributeConfigViewProps.setIsFirstCall(false);
    }
    CalculatedAttributeConfigViewProps.setFrom(oPaginationData.from);
    CalculatedAttributeConfigViewProps.setSearchText(oPaginationData.searchText);
    CalculatedAttributeConfigViewProps.setTotalCount(oResponse.success.totalCount);
    CalculatedAttributeConfigViewProps.setAttributeId(sAttributeId);
    if (oPaginationData.from == 0) {
      CalculatedAttributeConfigViewProps.setAllowedAttributes(oResponse.success.list);
    } else {
      var aAttributesList = CalculatedAttributeConfigViewProps.getAllowedAttributes();
      CalculatedAttributeConfigViewProps.setAllowedAttributes(aAttributesList.concat(oResponse.success.list));
    }

    let sConfigScreenViewName = SettingScreenProps.screen.getSelectedLeftNavigationTreeItem();

    if (sConfigScreenViewName === ConfigModulePropertyGroupTypeDictionary.CALCULATED) {
      let oGridViewProps = GridViewStore.getGridViewPropsByContext(GridViewContexts.ATTRIBUTE);
      try {
        let gridViewData = oGridViewProps.getGridViewData();
        let oSelectedAttribute = CS.find(gridViewData, {"id": sAttributeId});
        let aAttributesList = oSelectedAttribute.properties.calculatedAttributeFormula.allowedAttributes;
        if (CS.isEmpty(aAttributesList)) {
          aAttributesList = [];
        }

        let oSkeleton = oGridViewProps.getGridViewSkeleton();
        var aScrollableColumns = oSkeleton.scrollableColumns;
        var oCalculatedAttrFormula = CS.find(aScrollableColumns, {id: "calculatedAttributeFormula"});
        var oReferencedAttributes = oCalculatedAttrFormula.extraData.referencedAttributes;
        CS.forEach(oResponse.success.list, function (oListItem) {
          oReferencedAttributes[oListItem.id] = oListItem;
        });

        if (oPaginationData.from == 0) {
          CS.set(oSelectedAttribute, 'properties.calculatedAttributeFormula.allowedAttributes', oResponse.success.list);
        } else {
          CS.set(oSelectedAttribute, 'properties.calculatedAttributeFormula.allowedAttributes', aAttributesList.concat(oResponse.success.list));
        }
        CS.set(oSelectedAttribute, 'properties.calculatedAttributeFormula.paginationData', oPaginationData);
      }
      catch (oException) {
        ExceptionLogger.error(oException);
      }

    }
    _triggerChange();
  };

  let failureAllowedAttributesForCalculatedAttributeCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureAllowedAttributesForCalculatedAttributeCallback", getTranslation());
  };

  return {
    getAllowedAttributesForCalculatedAttribute: function (oPaginationData, sCalculatedAttributeType, sCalculatedAttributeUnit, sCalculatedAttributeId, bIsForReload) {
      _getAllowedAttributesForCalculatedAttribute(oPaginationData, sCalculatedAttributeType, sCalculatedAttributeUnit, sCalculatedAttributeId, bIsForReload);
    }
  }

})();

MicroEvent.mixin(CalculatedAttributeStore);

export default CalculatedAttributeStore;
