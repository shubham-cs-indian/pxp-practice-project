import CS from '../../../../../../libraries/cs';
import GlobalStore from '../../../../store/global-store.js';
import { ClassRequestMapping as oClassRequestMapping } from '../../tack/setting-screen-request-mapping';
import SettingUtils from './setting-utils';

var ContextUtils = (function () {

  var _handleContextAddTagClicked = function (sTagId, oContext, oCallbackData) {
    let oCallBackDataTemp = oCallbackData || {};
    oCallBackDataTemp.preFunctionToExecute = _addTagDataContext.bind(this, oContext);
    _fetchTagById(sTagId, oCallBackDataTemp);
  };

  var _fetchTagById = function (sTagId, oCallbackData) {
    var oParameters = {};
    oParameters.id = sTagId;
    oParameters.mode = "all";

    SettingUtils.csGetRequest(oClassRequestMapping.GetTag, oParameters, successFetchTagById.bind(this, oCallbackData),
        failureFetchTagById);
  };

  var successFetchTagById = function (oCallbackData, oResponse) {
    if (oCallbackData) {
      oCallbackData.preFunctionToExecute(oResponse.success);
      oCallbackData.functionToExecute && oCallbackData.functionToExecute();
    }
  };

  var failureFetchTagById = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchTagById", GlobalStore.getTranslations('SettingScreenTranslations'))
  };

  var _getTagValuesDataForClassContextTag = function (aTagValues) {
    var aTagValueData = [];

    CS.forEach(aTagValues, function (oTagValue) {
      let sLabel = CS.getLabelOrCode(oTagValue);
      aTagValueData.push({
        color: oTagValue.color,
        isSelected: true,
        label: sLabel,
        tagValueId: oTagValue.id
      });
    });

    return aTagValueData;
  };

  var _addTagDataContext = function (oContextData, oTag) {
    var aTags = oContextData.contextTags || [];
    var sTagId = oTag.id;
    var sTagLabel = CS.getLabelOrCode(oTag);

    CS.forEach(oContextData.uniqueSelectors, function (oSelector) {
      oSelector.selectionValues.push({
        label: sTagLabel,
        tagId: sTagId,
        tagValues: []
      });
    });

    let aTagValues = _getTagValuesDataForClassContextTag(oTag.children);

    if (!CS.find(aTags, { tagId: sTagId })) {
      aTags.push({
        label: sTagLabel,
        tagId: sTagId,
        tagValues: aTagValues,
        isMultiselect: oTag.isMultiselect,
        tagType: oTag.tagType
      });
    }
    oContextData.contextTags = aTags;
  };

  var _handleRemoveSelectedTagGroupClicked = function (sTagGroupId, oContextData) {
    var aTags = oContextData.contextTags;
    CS.remove(oContextData.contextTags, {tagId: sTagGroupId});
    CS.forEach(oContextData.uniqueSelectors, function (oUniqueSelector, iUIndex) {
      CS.forEach(oUniqueSelector.selectionValues, function (oTag, iIndex) {
        if (oTag && oTag.tagId == sTagGroupId) {
          oUniqueSelector.selectionValues.splice(iIndex, 1);
        }
      });
    });
    SettingUtils.removeEmptyTagSelections(oContextData);
  };

  var _handleClassContextAddOrRemoveTagValue = function (sTagGroupId, aCheckedItems, oContextData) {
    var aTags = oContextData.contextTags;
    var oTag = CS.find(aTags, {tagId: sTagGroupId});
    var aTagValues = oTag.tagValues;

    var oUniqueSelectors = oContextData.uniqueSelectors;
    CS.remove(oUniqueSelectors, function (oSelector) {
      var aSelectedTags = oSelector.selectionValues;
      var bRemoveUniqueSelector = true;
      CS.forEach(aSelectedTags, function (oSelectedTags) {
        if (oSelectedTags.tagId == sTagGroupId) {
          var aFilteredTags = [];
          var oTagValueMap = CS.keyBy(oSelectedTags.tagValues, 'tagValueId');
          CS.forEach(aCheckedItems, function (sId) {
            if (oTagValueMap[sId]) {
              aFilteredTags.push(oTagValueMap[sId]);
            }
          });
          oSelectedTags.tagValues = aFilteredTags;
        }

        if (!CS.isEmpty(oSelectedTags.tagValues)) {
          bRemoveUniqueSelector = false;
        }
      });
      if (bRemoveUniqueSelector) {
        return true;
      }
    });

    CS.forEach(aTagValues, function (oTagValue) {
      oTagValue.isSelected = CS.includes(aCheckedItems, oTagValue.tagValueId);
    });
  };


  /***Public APIs ***/
  return ({
    handleContextAddTagClicked: function (sTagId, oContext, oCallbackData) {
      _handleContextAddTagClicked(sTagId, oContext, oCallbackData);
    },

    handleRemoveSelectedTagGroupClicked: function (sTagId, oContext) {
      _handleRemoveSelectedTagGroupClicked(sTagId, oContext);
    },

    handleContextAddOrRemoveTagValue: function (sTagGroupId, aCheckedItems, oContextData) {
      _handleClassContextAddOrRemoveTagValue(sTagGroupId, aCheckedItems, oContextData);
    }
  });

})();
export default ContextUtils;
