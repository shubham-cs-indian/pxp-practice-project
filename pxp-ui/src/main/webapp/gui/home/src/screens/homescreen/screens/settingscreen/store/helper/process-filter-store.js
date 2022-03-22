import CS from '../../../../../../libraries/cs';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import ProcessFilterProps from '../../store/model/process-filter-props';
import SettingUtils from '../../store/helper/setting-utils';
import CommonUtils from '../../../../../../commonmodule/util/common-utils';

let ProcessFilterStore = (function () {

  let _getAppliedFiltersClone = function () {
    if (ProcessFilterProps.getAppliedFiltersClone() == null) {
      ProcessFilterProps.createAppliedFiltersClone();
      ProcessFilterProps.setIsFilterDirty(true);
    }
    return ProcessFilterProps.getAppliedFiltersClone();
  };

  let _getMasterTagById = function (sTagId) {
    let oAppData = SettingUtils.getAppData();
    let aMasterTagList = oAppData.getTagList();
    return CommonUtils.getNodeFromTreeListById(aMasterTagList, sTagId);
  };

  let _getLeafNodeWithSubbedWithParentInfo = function (aTreeNode, sParentPropertyName, sChildPropName) {
    let sSplitter = SettingUtils.getSplitter();
    let aChildNodes = [];
    let sCustomField = 'custom' + sSplitter + sParentPropertyName;
    CS.forEach(aTreeNode, function (oNode) {
      let aChildren = oNode[sChildPropName];
      if(CS.isEmpty(aChildren)) {
        oNode[sCustomField] = oNode[sParentPropertyName];
        aChildNodes.push(oNode);
      } else {
        let aLeafNodes = _getLeafNodeWithSubbedWithParentInfo(aChildren, sParentPropertyName, sChildPropName, aChildNodes);
        CS.forEach(aLeafNodes, function (oLeafNode) {
          let aCustomFieldValues = oLeafNode[sCustomField].split('/');
          aCustomFieldValues.unshift(oNode[sParentPropertyName]);
          oLeafNode[sCustomField] = aCustomFieldValues.join('/');
          aChildNodes.push(oLeafNode);
        });
      }
    });

    return aChildNodes;
  };

  let _getMinValueFromListByPropertyName = function (aList, sPropertyName) {
    let oMin = CS.minBy(aList, sPropertyName);
    return oMin ? oMin[sPropertyName] : 0;
  };

  let _getMaxValueFromListByPropertyName = function (aList, sPropertyName) {
    let oMax = CS.maxBy(aList, sPropertyName);
    return oMax ? oMax[sPropertyName] : 0;
  };

  let _createAppliedFilterCollapseStatusMap = function () {
    let aAppliedFilters = ProcessFilterProps.getAppliedFilters();
    let oAppliedFilterCollapseStatusMap = ProcessFilterProps.getAppliedFilterCollapseStatusMap();
    CS.forEach(aAppliedFilters, function (oAppliedFilter) {
      if(!CS.isEmpty(oAppliedFilterCollapseStatusMap[oAppliedFilter.id])){
        oAppliedFilterCollapseStatusMap[oAppliedFilter.id].isCollapsed = false;
      }else {
        oAppliedFilterCollapseStatusMap[oAppliedFilter.id] = {isCollapsed: false};
      }
    });
  };

  let _handleAdvancedSearchButtonClicked = function () {
    let bAdvancedSearchStatus = ProcessFilterProps.getAdvancedSearchPanelShowStatus();
    ProcessFilterProps.setAdvancedSearchPanelShowStatus(!bAdvancedSearchStatus);
    if(!bAdvancedSearchStatus){
      _createAppliedFilterCollapseStatusMap();
    }
  };

  return {

    getAppliedFiltersClone: function () {
      return _getAppliedFiltersClone();
    },

    isImageCoverflowAttribute: function (sAttributeType) {
      return AttributeUtils.isImageCoverflowAttribute(sAttributeType); // eslint-disable-line
    },

    isAttributeTypeSecondaryClasses: function (sType) {
      return AttributeUtils.isAttributeTypeSecondaryClasses(sType); // eslint-disable-line
    },

    getMasterTagById: function (sTagId) {
      return _getMasterTagById(sTagId);
    },

    getLeafNodeSubbedWithParentInfo: function (aTreeNode, sParentKey, sChildKey) {
      return _getLeafNodeWithSubbedWithParentInfo(aTreeNode, sParentKey, sChildKey || 'children');
    },

    getMinValueFromListByPropertyName: function (aTagValues, sPropertyName) {
      return _getMinValueFromListByPropertyName(aTagValues, sPropertyName);
    },

    getMaxValueFromListByPropertyName: function (aTagValues, sPropertyName) {
      return _getMaxValueFromListByPropertyName(aTagValues, sPropertyName);
    },

    handleAdvancedSearchButtonClicked: function () {
      _handleAdvancedSearchButtonClicked();
    },

  }
})();

MicroEvent.mixin(ProcessFilterStore);

export default ProcessFilterStore;
