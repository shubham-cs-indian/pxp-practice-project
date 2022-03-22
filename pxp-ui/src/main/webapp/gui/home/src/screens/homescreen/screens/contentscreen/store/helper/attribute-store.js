import CS from '../../../../../../libraries/cs';

import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import AttributeProps from './../model/attributes-screen-view-props';
import ContentUtils from './content-utils';
import GlobalStore from './../../../../store/global-store';
import MethodTracker from '../../../../../../libraries/methodtracker/method-tracker';
import LogFactory from '../../../../../../libraries/logger/log-factory';
import TagTypeConstants from '../../../../../../commonmodule/tack/tag-type-constants';

import StandardAttributeIdDictionary from '../../tack/mock/standard-attribute-id-dictionary';
import BaseTypesDictionary from '../../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import ExceptionLogger from '../../../../../../libraries/exceptionhandling/exception-logger';
import SystemLevelIdDictionary from '../../../../../../commonmodule/tack/system-level-id-dictionary';

var logger = LogFactory.getLogger('attribute-store');
var trackMe = MethodTracker.getTracker('attribute-store');


var AttributeStore = (function () {

  //************************************* Private API's ************-**********************************//
  var _triggerChange = function () {
    AttributeStore.trigger('attribute-change');
  };

  var _updateTagRelevance = function (sAttributeId, sTagGroupId, sTagId, iNewRelevance) {
    var oActiveEntity = ContentUtils.makeCurrentEntityDirty();
    var oAttribute = CS.find(oActiveEntity.attributes, {id: sAttributeId});
    var oContentTagGroup = CS.find(oAttribute.tags, {tagId: sTagGroupId});
    var oMasterTag = ContentUtils.getMasterTagById(sTagGroupId);

    if(CS.isEmpty(oContentTagGroup)) {
      oContentTagGroup = ContentUtils.addTagInEntity(oAttribute, {}, oMasterTag)
    }

    var oTag = CS.find(oContentTagGroup.tagValues, {tagId: sTagId});
    if(CS.isEmpty(oTag)) {
      oTag = ContentUtils.getNewTagValue(sTagId);
      oContentTagGroup.tagValues.push(oTag);
    }

    if(!oMasterTag.isMultiselect && oMasterTag.tagType == TagTypeConstants.YES_NEUTRAL_TAG_TYPE) {
      oContentTagGroup.tagValues = [oTag];
    }

    oTag.relevance = iNewRelevance;
    oAttribute.isValueChanged = true;
  };

  var _updateTagRelevanceUsingModel = function (oModel) {
    var sTagId = oModel.id;
    var iNewRelevance = oModel.properties['iNewValue'];
    var sTagGroupId = oModel.properties['tagGroupId'];
    var sAttributeId = oModel.properties['entityId'];
    _updateTagRelevance(sAttributeId, sTagGroupId, sTagId, iNewRelevance);
  };

  var _updateTagRelevanceInAsset = function (sAttributeId, sTagGroupId, sTagId, iNewRelevance) {
    var oActiveEntity = ContentUtils.makeCurrentEntityDirty();
    var aList = oActiveEntity.referencedAssets;
    if(oActiveEntity.baseType == BaseTypesDictionary.assetBaseType) {
      aList = oActiveEntity.attributes;
    }
    var oAttribute = CS.find(aList, {id: sAttributeId});
    var oContentTagGroup = CS.find(oAttribute.tags, {tagId: sTagGroupId});
    var oMasterTag = ContentUtils.getMasterTagById(sTagGroupId);

    if(CS.isEmpty(oContentTagGroup)) {
      oContentTagGroup = ContentUtils.addTagInEntity(oAttribute, {}, oMasterTag)
    }

    var oTag = CS.find(oContentTagGroup.tagValues, {tagId: sTagId});
    if(CS.isEmpty(oTag)) {
      oTag = ContentUtils.getNewTagValue(sTagId);
      oContentTagGroup.tagValues.push(oTag);
    }

    if(!oMasterTag.isMultiselect && oMasterTag.tagType == TagTypeConstants.YES_NEUTRAL_TAG_TYPE) {
      oContentTagGroup.tagValues = [oTag];
    }

    oTag.relevance = iNewRelevance;
    oAttribute.isValueChanged = true;
  };

  /** To show only saveAndPublish button*/
  let _checkIsVersionableAttributeChanged = function (sAttributeId) {
    let oComponentProps = ContentUtils.getComponentProps();
    let oScreenProps = oComponentProps.screen;
    let oReferencedElements = oComponentProps.screen.getReferencedElements();
    let oCurrentElement = oReferencedElements[sAttributeId];
    let oReferencedAttributes = oScreenProps.getReferencedAttributes();
    let oMasterAttribute = oReferencedAttributes[sAttributeId] || ContentUtils.getMasterAttributeById(sAttributeId);

    let bIsVersionable = (oCurrentElement && !CS.isNull(oCurrentElement.isVersionable)) ? oCurrentElement.isVersionable : oMasterAttribute.isVersionable;
    if (bIsVersionable) {
      oScreenProps.setIsVersionableAttributeValueChanged(true);
    }
  };

  var _handleConcatenatedAttributeExpressionChanged = function (sAttributeId, sValue, sSectionId, sExpressionId) {
    var oComponentProps = ContentUtils.getComponentProps();
    var oScreenProps = oComponentProps.screen;
    let aReferencedAttributes = oScreenProps.getReferencedAttributes();
    let oActiveEntityClone = ContentUtils.makeActiveContentOrVariantDirty();
    let oOriginalActiveEntity = ContentUtils.getActiveContentOrVariant();

    var oFoundAttribute = CS.find(oActiveEntityClone.attributes, {"id": sAttributeId});

    var oFoundAttributeFromOriginalContent = CS.find(oOriginalActiveEntity.attributes, {"id": sAttributeId});

    if (oFoundAttribute) {
      if (CS.isEmpty(oFoundAttribute.valueAsExpression)) {
        var oFoundReferencedAttribute = CS.find(aReferencedAttributes, {"id": oFoundAttribute.attributeId});
        oFoundAttribute.valueAsExpression = CS.cloneDeep(oFoundReferencedAttribute.attributeConcatenatedList);
      }

      var oFoundExpression = CS.find(oFoundAttribute.valueAsExpression, {id: sExpressionId});
      if(oFoundExpression.type === "html"){
        oFoundExpression.value = sValue.value;
        oFoundExpression.valueAsHtml = sValue.valueAsHtml;
      }else {
        oFoundExpression.value = sValue;
      }

      if (oFoundAttributeFromOriginalContent) {
        oFoundAttribute.isValueChanged = oFoundAttributeFromOriginalContent.value != sValue;
      } else {
        oFoundAttribute.isValueChanged = true;
      }

      /** To show only saveAndPublish button*/
      _checkIsVersionableAttributeChanged(oFoundAttribute.attributeId);

    } else {
      ExceptionLogger.log(sAttributeId + 'attribute not found (handleAttributeVariantInputTextChanged)');
    }
    oComponentProps.contentSectionViewProps.setSectionsToUpdate(sSectionId);
  };

  //************************************* Public API's **********************************************//
  return {
    handleAttributeVariantInputTextChanged: function (sAttributeId, sValue, sSectionId, oSource) {
      var oComponentProps = ContentUtils.getComponentProps();
      var oScreenProps = oComponentProps.screen;
      var oReferencedAttributes = oScreenProps.getReferencedAttributes();
      var oActiveEntityClone = ContentUtils.makeActiveContentOrVariantDirty();
      var oOriginalActiveEntity = ContentUtils.getActiveContentOrVariant();
      var oFoundAttribute = null;
      var oFoundAttributeFromOriginalContent = null;
      if (ContentUtils.isAttributeTypeDueDate(sAttributeId) || ContentUtils.isAttributeTypeCreatedOn(sAttributeId)) {
        var sMasterAttributeId = StandardAttributeIdDictionary[sAttributeId];
        oFoundAttribute = CS.find(oActiveEntityClone.attributes, {"attributeId": sMasterAttributeId});
        oFoundAttributeFromOriginalContent = CS.find(oOriginalActiveEntity.attributes, {"attributeId": sMasterAttributeId});
      } else {
        oFoundAttribute = CS.find(oActiveEntityClone.attributes, {"id": sAttributeId});
        var oMasterAttribute = oReferencedAttributes[oFoundAttribute.attributeId] || ContentUtils.getMasterAttributeById(oFoundAttribute.attributeId);
        if (((!CS.isEmpty(oMasterAttribute) && ContentUtils.isAttributeTypeName(oMasterAttribute.type)) || oFoundAttribute.attributeId === SystemLevelIdDictionary.NameAttributeId)
            && CS.isEmpty(oFoundAttribute.variantOf)) {
          oActiveEntityClone.name = sValue;
          var sBreadCrumbId = (oActiveEntityClone.variantInstanceId) ? oActiveEntityClone.variantInstanceId : oActiveEntityClone.id;
        }
        oFoundAttributeFromOriginalContent = CS.find(oOriginalActiveEntity.attributes, {"id": sAttributeId});
      }

      if (oFoundAttribute) {
        if (sValue && CS.has(sValue, 'htmlValue')) {
          oFoundAttribute.valueAsHtml = sValue.textValue;
          sValue = sValue.htmlValue;
        }
        //check for change mandatory html field
        else if (oMasterAttribute && ContentUtils.isAttributeTypeHtml(oMasterAttribute.type)) {
          oFoundAttribute.valueAsHtml = sValue;
          sValue = ContentUtils.getDecodedHtmlContent(sValue);
        }
        oFoundAttribute.value = sValue;

        /** To update breadcrumb label for name attribute when changes are made **/
        if(oFoundAttribute.attributeId === SystemLevelIdDictionary.NameAttributeId) {
          ContentUtils.updateBreadCrumbInfo(sBreadCrumbId, oActiveEntityClone);
        }

        if (oFoundAttributeFromOriginalContent) {
          oFoundAttribute.isValueChanged = oFoundAttributeFromOriginalContent.value != sValue;
        } else {
          oFoundAttribute.isValueChanged = true;
        }

        //Below flag is added for only UI purpose.
        var bIsResolved = false;
        CS.forEach(oFoundAttribute.conflictingValues, function (oValue) {
          bIsResolved = true;
          oValue.isResolved = bIsResolved;
        });
        oFoundAttribute.isResolved = bIsResolved;
        oFoundAttribute.isConflictResolved = bIsResolved;

        if (oSource) {
          oFoundAttribute.source = oSource;
        } else {
          delete oFoundAttribute.source;
        }
      } else {
        ExceptionLogger.log(sAttributeId + 'attribute not found (handleAttributeVariantInputTextChanged)');
      }

      var oReferencedElements = oComponentProps.screen.getReferencedElements();
      var sCurrentAttributeId = oFoundAttribute.attributeId;
      var oCurrentElement = oReferencedElements[sCurrentAttributeId];

      /** To show only saveAndPublish button*/
      _checkIsVersionableAttributeChanged(sCurrentAttributeId);

      if (oCurrentElement && oCurrentElement.isMandatory) {
        var oSectionProps = oComponentProps.contentSectionViewProps;
        var aViolatingMandatoryElements = oSectionProps.getViolatingMandatoryElements();
        if (CS.includes(aViolatingMandatoryElements, sCurrentAttributeId) && oFoundAttribute.value != "") {
          CS.remove(aViolatingMandatoryElements, function (n) {
            return n == sCurrentAttributeId;
          });
        } else if (oFoundAttribute.value == "") {
          aViolatingMandatoryElements.push(sCurrentAttributeId);
        }
      }
      oComponentProps.contentSectionViewProps.setSectionsToUpdate(sSectionId);
    },

    populateTagInContentAttributeVariants: function () {
      trackMe('populateTagInContentAttributeVariants');
      var oActiveContentClone = ContentUtils.makeActiveContentDirty();
      var aSelectedAttributeVariants = AttributeProps.getSelectedAttributeVariants();

      var aTagData = CS.cloneDeep(CS.values(GlobalStore.getTagValuesList()));

      logger.info('_populateTagInContentAttributeVariants: Applying tag on selected attributes',
          {'selectedAttributeVariants': aSelectedAttributeVariants, 'tagData': aTagData});

      CS.forEach(aSelectedAttributeVariants, function (oAttributeVariant) {
        var oAttributeToFind = {};
        if (oAttributeVariant.uuid) {
          oAttributeToFind.uuid = oAttributeVariant.uuid;
        }
        if (oAttributeVariant.id) {
          oAttributeToFind.id = oAttributeVariant.id;
        }

        var oAttributeVariantToBeTagged = CS.find(oActiveContentClone.attributes, oAttributeToFind);

        oAttributeVariantToBeTagged.attributeContext.tags = aTagData;
      });
    },

    updateTagRelevance: function (oModel) {
      _updateTagRelevanceUsingModel(oModel);
      _triggerChange();
    },

    addTagInAttributeFromMultiSearch: function (sAttributeId, sTagGroupId, sTagId) {
      _updateTagRelevance(sAttributeId, sTagGroupId, sTagId, 100);
      _triggerChange();
    },

    removeTagInAttributeFromMultiSearch: function (sAttributeId, sTagGroupId, sTagId) {
      _updateTagRelevance(sAttributeId, sTagGroupId, sTagId, 0);
    },

    addTagInAssetFromMultiSearch: function (sAttributeId, sTagGroupId, sTagId) {
      _updateTagRelevanceInAsset(sAttributeId, sTagGroupId, sTagId, 100);
      _triggerChange();
    },

    removeTagInAssetFromMultiSearch: function (sAttributeId, sTagGroupId, sTagId) {
      _updateTagRelevanceInAsset(sAttributeId, sTagGroupId, sTagId, 0);
    },

    handleConcatenatedAttributeExpressionChanged: function (sAttributeId, sValue, sSectionId, sExpressionId) {
      _handleConcatenatedAttributeExpressionChanged(sAttributeId, sValue, sSectionId, sExpressionId);
      _triggerChange();
    }
  }
})();

MicroEvent.mixin(AttributeStore);

export default AttributeStore;
