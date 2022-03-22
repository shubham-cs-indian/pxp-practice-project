import CS from '../../../../../../libraries/cs';

import ScreenModeUtils from './screen-mode-utils';
import ContentUtils from './content-utils';
import CouplingConstants from '../../../../../../commonmodule/tack/coupling-constans';
import GRConstants from '../../tack/golden-record-view-constants';
import ContextMenuViewModel from '../../../../../../viewlibraries/contextmenuwithsearchview/model/context-menu-view-model';
import MarkerClassTypeDictionary from '../../../../../../commonmodule/tack/marker-class-type-dictionary';
import UniqueIdentifierGenerator from './../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import TagTypeConstants from '../../../../../../commonmodule/tack/tag-type-constants';
import ClassNameFromBaseTypeDictionary from '../../../../../../commonmodule/tack/class-name-base-types-dictionary';
var getTranslation = ScreenModeUtils.getTranslationDictionary;

let MatchAndMergeUtils = (function () {
  const
      ATTRIBUTE = "attribute",
      TAG = "tag",
      RELATIONSHIP = "relationship",
      NATURE_RELATIONSHIP = "natureRelationship",
      BASIC_INFORMATION_TABLE = "basicInformationTable",
      TYPES = "types",
      TAXONOMIES = "taxonomies",
      BASIC_INFO = "basicInfo";

  /** Skeleton of Match and Merge Data */
  const _getLayoutSkeleton = function () {
    return {
      languageData: {},
      tableIds: [],
      tableData: {},
      selectedTableId: "",
      selectedRowId: "",
    };
  };

  /**==================================Dummy KI Processing========================**/

  /**
   * @description - Dummy klass instance attribute model
   * @returns {{id: *, value: string, valueAsHtml: string}}
   * @private
   */
  let _getAttributeInstanceModel = function (sElementId, sElementType, oPropertyRecommendation, aMatchAttributeList, aGoldenRecordInstanceAttributes) {
    let oProperty = CS.find(aGoldenRecordInstanceAttributes, {attributeId: sElementId}) || oPropertyRecommendation[sElementId];
    if (CS.isEmpty(oProperty)) {
      oProperty = CS.find(aMatchAttributeList, {attributeId: sElementId});
    }
    const {value = "", valueAsHtml = ""} = oProperty || {};
    return {
      id: sElementId,
      value,
      valueAsHtml
    }
  };


  /**
   * @description - Dummy klass instance Tag Model
   * @returns {{id: *, tagId: *, tagValues: Array}}
   * @private
   */
  let _getTagInstanceModel = function (sElementId, sElementType, oPropertyRecommendation, aMatchTagList, aGoldenRecordInstanceTags, oReferencedTag) {
    let oProperty = CS.find(aGoldenRecordInstanceTags, {tagId: sElementId}) || oPropertyRecommendation[sElementId];
    if (CS.isEmpty(oProperty)) {
      oProperty = CS.find(aMatchTagList, {tagId: sElementId});
    }
    const {tagValues = []} = oProperty || {};

    let oTag = {
      id: sElementId,
      tagId: sElementId,
      tagValues
    };
    /**Adding tag values into entity tag from tag references*/
    ContentUtils.addTagsInContentBasedOnTagType(oReferencedTag, oTag);
    delete oTag.isValueChanged;//Not required in model :Neha
    return oTag;
  };

  /**
   * @description - Prepares attribute and dependent attribute data for dummy klass instance
   * @private
   */
  let _fillDummyKlassInstanceWithAttribute = function (oDummyKlassInstance, oReferencedAttributes, oReferencedElements, oPropertyRecommendations, aMatchAttributeList,
                                                       aGoldenRecordInstanceAttributes, sSelectedLanguageId) {
    let oKlassInstanceAttributes = oDummyKlassInstance.attributes;
    let oKlassInstanceDependentAttributes = oDummyKlassInstance.dependentAttributes;
    let aExistingElementInstance = [];
    let oElementInstance = {};
    /***COMMENT***/
    CS.forEach(oReferencedAttributes, function (oReferencedAttribute, sPropertyId) {
      aExistingElementInstance = [];
      let oSelectedLanguageAttributesList = {};
      if (oReferencedElements[sPropertyId]) {
        if (oReferencedAttribute.isTranslatable) {
          oSelectedLanguageAttributesList = oKlassInstanceDependentAttributes[sSelectedLanguageId] || {};
          aExistingElementInstance = (oSelectedLanguageAttributesList && oSelectedLanguageAttributesList[sPropertyId]);
          if (CS.isEmpty(aExistingElementInstance)) {
            oElementInstance = _getAttributeInstanceModel(sPropertyId, ATTRIBUTE, oPropertyRecommendations, aMatchAttributeList, aGoldenRecordInstanceAttributes);
            oSelectedLanguageAttributesList[sPropertyId] = [oElementInstance];
            oKlassInstanceDependentAttributes[sSelectedLanguageId] = oSelectedLanguageAttributesList;
          }
        } else {
          aExistingElementInstance = oKlassInstanceAttributes[sPropertyId];
          if (CS.isEmpty(aExistingElementInstance)) {
            oElementInstance = _getAttributeInstanceModel(sPropertyId, ATTRIBUTE, oPropertyRecommendations, aMatchAttributeList, aGoldenRecordInstanceAttributes);
            oKlassInstanceAttributes[sPropertyId] = [oElementInstance];
          }
        }
      }
    });
  };

  /**
   * @description - Prepares tag data for dummy klass instance
   * @private
   */
  let _fillDummyKlassInstanceWithTag = function (oDummyKlassInstance, oReferencedTags, oReferencedElements, oPropertyRecommendations,
                                                 aMatchTagList, aGoldenRecordInstanceTags) {
    let oKlassInstanceTags = oDummyKlassInstance.tags;
    let aExistingElementInstance = [];
    let oElementInstance = {};
    CS.forEach(oReferencedTags, function (oReferencedTag, sTagGroupId) {
      if (oReferencedElements[sTagGroupId]) {
        aExistingElementInstance = oKlassInstanceTags[sTagGroupId];
        if (CS.isEmpty(aExistingElementInstance)) {
          oElementInstance = _getTagInstanceModel(sTagGroupId, TAG, oPropertyRecommendations, aMatchTagList, aGoldenRecordInstanceTags, oReferencedTag);
          oKlassInstanceTags[sTagGroupId] = [oElementInstance];
        }
      }
    });
  };


  /**
   * @description - Wrapper function which prepares attribute and tag data for dummy klass instance
   * @private
   */
  let _fillDummyKlassInstanceWithElements = function (oDummyKlassInstance, oConfigDetails, oPropertyRecommendations, oGoldenRecordInstance, oMatchPropertiesData, sSelectedLanguageId) {
    let oReferencedAttributes = oConfigDetails.referencedAttributes;
    let oReferencedTags = oConfigDetails.referencedTags;
    let oReferencedElements = oConfigDetails.referencedElements;

    /**If Golden record present in bucket*/
    let aGoldenRecordInstanceAttributes = [];
    let aGoldenRecordInstanceTags = [];
    if (!CS.isEmpty(oGoldenRecordInstance)) {
      aGoldenRecordInstanceAttributes = oGoldenRecordInstance.attributes;
      aGoldenRecordInstanceTags = oGoldenRecordInstance.tags;
    }

    _fillDummyKlassInstanceWithAttribute(oDummyKlassInstance, oReferencedAttributes, oReferencedElements, oPropertyRecommendations,
        oMatchPropertiesData.attributes, aGoldenRecordInstanceAttributes, sSelectedLanguageId);
    _fillDummyKlassInstanceWithTag(oDummyKlassInstance, oReferencedTags, oReferencedElements, oPropertyRecommendations,
        oMatchPropertiesData.tags, aGoldenRecordInstanceTags);
  };


  /**
   * @returns {*} Dummy klass instance
   * @description Creates golden record dummy klass instance
   * @private
   */
  let _createDummyKlassInstance = function (oDummyKlassInstance) {
    if (CS.isEmpty(oDummyKlassInstance)) {
      oDummyKlassInstance = {
        attributes: {},
        dependentAttributes: {},
        tags: {},
        relationships: [],
        natureRelationships: []
      }
    }
    return oDummyKlassInstance;
  };

  /**===============================Lang Selection Data Processing=================================**/

  /**
   *
   * @param oLayoutData Match and merge layout data
   * @param aSelectedLanguageList
   * @param sSelectedLanguageId
   * @description Prepare MNM data for selected languages
   * @private
   */
  let _makeInstanceComparisonRowDataForLanguageTable = function (oLayoutData, aSelectedLanguageList, sSelectedLanguageId) {

    let aContextModelList = [];
    let sIconURL = "";
    let oSelectedItem = {};

    CS.forEach(aSelectedLanguageList, function (oItem) {
      if (oItem.code !== sSelectedLanguageId) {
        aContextModelList.push(new ContextMenuViewModel(
            oItem.code,
            CS.getLabelOrCode(oItem),
            false,
            oItem.iconKey,
            {}
        ));
      } else {
        sIconURL = ContentUtils.getIconUrl(oItem.icon);
        oSelectedItem = oItem;
      }
    });

    let oLanguageData = {
      contextMenuViewModel: aContextModelList,
      showSearch: false,
    };

    oLayoutData.languageData = {
      tableHeaderLabel: getTranslation().CHOOSE_LANGUAGE,
      rowData: oLanguageData,
      selectedItem: oSelectedItem,
      selectedItemURL: sIconURL
    }
  };

  /**===============================Basic Details Data Processing=================================**/

  /**
   *
   * @param aSelectedKlassIDs
   * @param oReferencedKlasses
   * @returns {Array} List of selected Klasses
   * @description - Prepares Map of Selected klasses
   * @private
   */
  let _prepareReferencedKlassesMap = function (aSelectedKlassIDs, oReferencedKlasses) {
    let aKlassIds = [];
    CS.forEach(aSelectedKlassIDs, function (sKlassId) {
      if (sKlassId !== MarkerClassTypeDictionary.GOLDEN_RECORD && sKlassId !== MarkerClassTypeDictionary.MARKER) {
        const oKlass = oReferencedKlasses[sKlassId];
        if (oKlass) {
          let oKlassData = {
            id: oKlass.id,
            label: oKlass.label,
            code: oKlass.code,
            icon: oKlass.icon,
            customIconClassName: ClassNameFromBaseTypeDictionary[oKlass.type] || ""
          };
          aKlassIds.push(oKlassData);
        }
      }
    });
    return aKlassIds;
  };

  /**
   *
   * @param aSelectedTaxonomyIds
   * @param oReferencedTaxonomies
   * @returns {Array} List of Selected taxonomies
   * @description - Prepares Map of selected taxonomies
   * @private
   */
  let _prepareReferencedTaxonomiesMap = function (aSelectedTaxonomyIds, oReferencedTaxonomies) {
    let aValues = [];
    let oTaxonomyMap = {};
    CS.forEach(oReferencedTaxonomies, function (oReferencedTaxonomy) {
      ContentUtils.prepareReferencedTaxonomyMap(oReferencedTaxonomy, oTaxonomyMap);
    });

    CS.forEach(aSelectedTaxonomyIds, function (sTaxonomyId) {
      var aTaxonomyIds = [];
      ContentUtils.prepareSelectedTaxonomiesFromMap(sTaxonomyId, aTaxonomyIds, oTaxonomyMap);
      if (!CS.isEmpty(aTaxonomyIds)) {
        aValues.push(CS.reverse(aTaxonomyIds));
      }
    });
    return aValues;
  };


  /**
   * @description Layout Data for Klasses and taxonomies
   * @returns {{id: *, label: *, values: *, isDisabled: *, rendererType: *, propertyType: *}}
   * @private
   */
  let _getFixedTableInformationLayoutData = function (sId, sLabel, aValues, bIsDisabled, sRendererType, sPropertyType) {
    return {
      id: sId,
      label: sLabel,
      values: aValues,
      isDisabled: bIsDisabled,
      rendererType: sRendererType,
      propertyType: sPropertyType
    }
  };


  /**
   *
   * @param oLayoutData
   * @param oConfigDetails
   * @param aSelectedKlassIDs
   * @param aSelectedTaxonomyIds
   * @description - Prepares comparison data for Types and taxonomies
   * @private
   */
  let _makeInstancesComparisonRowDataForFixedInformation = function (oLayoutData, oConfigDetails, aSelectedKlassIDs, aSelectedTaxonomyIds, oKlassBasicInformationData) {
    let aRowData = [];

    let oReferencedKlasses = oConfigDetails.referencedKlasses;
    let oReferencedTaxonomies = oConfigDetails.referencedTaxonomies;
    let aSelectedKlassesList = _prepareReferencedKlassesMap(aSelectedKlassIDs, oReferencedKlasses);
    aRowData.push(_getFixedTableInformationLayoutData(TYPES, getTranslation().TYPES, [aSelectedKlassesList], false, "chip", BASIC_INFO));

    let aSelectedTaxonomiesList = _prepareReferencedTaxonomiesMap(aSelectedTaxonomyIds, oReferencedTaxonomies);
    if (!CS.isEmpty(aSelectedTaxonomiesList)) {
      aRowData.push(_getFixedTableInformationLayoutData(TAXONOMIES, getTranslation().TAXONOMIES, aSelectedTaxonomiesList, false, "chip", BASIC_INFO));
    }

    /**TODO: Event schedule information in fixed information table*/

    if (!CS.isEmpty(aRowData)) {
      oLayoutData.tableData[BASIC_INFORMATION_TABLE] = {
        tableHeaderLabel: getTranslation().BASIC_INFORMATION,
        hideEditRowButton: true,
        rowData: aRowData
      };
      oLayoutData.tableIds.push(BASIC_INFORMATION_TABLE);
    }
    oKlassBasicInformationData.klassIds = aSelectedKlassIDs;
    oKlassBasicInformationData.taxonomyIds = aSelectedTaxonomyIds;
  };

  /**===============================PC Data Processing=================================**/

  let _prepareRowDataForAttribute = function (oDummyKlassInstance, oReferencedElement, oMasterAttribute, aMatchAttributeList, sSelectedLanguageId) {
    let oKlassInstanceAttributes = oDummyKlassInstance.attributes;
    let oKlassInstanceDependentAttributes = oDummyKlassInstance.dependentAttributes;
    let sAttributeId = oReferencedElement.id;

    if (
        oReferencedElement
        && !(
            oReferencedElement.isSkipped
            || ContentUtils.isAttributeTypeCalculated(oMasterAttribute.type)
            || ContentUtils.isAttributeTypeConcatenated(oMasterAttribute.type)
            || ContentUtils.isAttributeTypeCoverflow(oMasterAttribute.type)
            || ContentUtils.isAttributeTypeCreatedOn(oMasterAttribute.type)
            || ContentUtils.isAttributeTypeLastModified(oMasterAttribute.type)
            || ContentUtils.isAttributeTypeUser(oMasterAttribute.type)
        )) {

      let aAttribute = [];
      if (oMasterAttribute.isTranslatable) {
        aAttribute = oKlassInstanceDependentAttributes[sSelectedLanguageId][sAttributeId];
      } else {
        aAttribute = oKlassInstanceAttributes[sAttributeId]
      }
      let oInstanceAttribute = aAttribute[0] || {};

      let bIsDisableRow = (oReferencedElement.isDisabled
          || oMasterAttribute.isDisabled
          || oReferencedElement.couplingType === CouplingConstants.DYNAMIC_COUPLED
          || oReferencedElement.couplingType === CouplingConstants.READ_ONLY_COUPLED);
      bIsDisableRow = bIsDisableRow || !CS.isEmpty(CS.find(aMatchAttributeList, {attributeId: sAttributeId}));

      let bIsHTMLAttribute = ContentUtils.isAttributeTypeHtml(oMasterAttribute.type);
      let sValue = bIsHTMLAttribute ? oInstanceAttribute.valueAsHtml : oInstanceAttribute.value;
      let sValueAsHtml = bIsHTMLAttribute ? oInstanceAttribute.value : "";

      return {
        id: oMasterAttribute.id,
        label: CS.getLabelOrCode(oMasterAttribute),
        type: oMasterAttribute.type,
        propertyType: ATTRIBUTE,
        masterAttribute: oMasterAttribute,
        isDisabled: bIsDisableRow,
        rendererType: ContentUtils.getAttributeTypeForVisual(oMasterAttribute.type),
        value: sValue,
        valueAsHtml: sValueAsHtml,
        valueAsExpression: oInstanceAttribute.valueAsExpression,
        isDependent: oMasterAttribute.isTranslatable,
        properties: {},
        iconKey: oMasterAttribute.iconKey
      };
    }
  };

  let _prepareRowDataForTag = function (oDummyKlassInstance, oReferencedElement, oReferencedTags, aMatchTagList, sSelectedLanguageId) {
    let oKlassInstanceTags = oDummyKlassInstance.tags;
    let sTagId = oReferencedElement.id;
    let oMasterTag = oReferencedTags[sTagId];

    if ((oReferencedElement && !oReferencedElement.isSkipped)) {
      /**Required to Prepared tag group model according to updated tag values*/
      let oInstanceTag = oKlassInstanceTags[sTagId][0] || {};
      let oTagGroupModel = ContentUtils.getTagGroupModels(oMasterTag, {tags: [oInstanceTag]}, oReferencedElement, GRConstants.GOLDEN_RECORD_COMPARISON, {});
      oTagGroupModel.masterTagList = oReferencedTags;
      oTagGroupModel.extraData = {
        dataLanguage: sSelectedLanguageId
      };

      let bIsDisableRow = (oReferencedElement.isDisabled
          || oMasterTag.isDisabled
          || oReferencedElement.couplingType === CouplingConstants.DYNAMIC_COUPLED
          || oReferencedElement.couplingType === CouplingConstants.READ_ONLY_COUPLED);

      bIsDisableRow = bIsDisableRow || !CS.isEmpty(CS.find(aMatchTagList, {tagId: sTagId}));

      return {
        id: oMasterTag.id,
        label: CS.getLabelOrCode(oMasterTag),
        type: oMasterTag.type,
        tagGroupModel: oTagGroupModel,
        propertyType: TAG,
        isDisabled: bIsDisableRow,
        properties: {},
        iconKey: oMasterTag.iconKey
      };
    }
  };


  let _prepareRowDataForProperties = function (oDummyKlassInstance, oReferencedElement, oConfigDetails, oMatchPropertiesData, sSelectedLanguageId) {
    let oReferencedAttributes = oConfigDetails.referencedAttributes;
    let oReferencedTags = oConfigDetails.referencedTags;
    let oReferencedData = {};
    let sElementId = oReferencedElement.id;

    switch (oReferencedElement.type) {
      case ATTRIBUTE:
        oReferencedData = oReferencedAttributes[sElementId];
        return _prepareRowDataForAttribute(oDummyKlassInstance, oReferencedElement, oReferencedData, oMatchPropertiesData.attributes, sSelectedLanguageId);

      case TAG:
        return _prepareRowDataForTag(oDummyKlassInstance, oReferencedElement, oReferencedTags, oMatchPropertiesData.tags, sSelectedLanguageId);

        //TODO: Taxonomy Handling
    }
  };

  /**
   *
   * @param oDummyKlassInstance
   * @param oLayoutData - Match and merge layout data
   * @param oConfigDetails
   * @param oMatchPropertiesData
   * @param sSelectedLanguageId
   * @description - Prepare MNM data for PropertyCollection
   * @private
   */

  let _prepareDataForPropertyCollection = function (oDummyKlassInstance, oLayoutData, oConfigDetails, oMatchPropertiesData, sSelectedLanguageId) {
    let oReferencedPropertyCollection = oConfigDetails.referencedPropertyCollections;
    let oReferencedElements = oConfigDetails.referencedElements;

    CS.forEach(oReferencedPropertyCollection, function (oPropertyCollection, sPropertyCollectionId) {
      let aRowData = [];
      let aElements = oPropertyCollection.elements;
      CS.forEach(aElements, function (oElement) {
        let oReferencedElement = oReferencedElements[oElement.id];
        if (!oReferencedElement) {
          return;
        }
        let oRowData = _prepareRowDataForProperties(oDummyKlassInstance, oReferencedElement, oConfigDetails, oMatchPropertiesData, sSelectedLanguageId);
        !CS.isEmpty(oRowData) && aRowData.push(oRowData);
      });

      if (!CS.isEmpty(aRowData)) {
        oLayoutData.tableData[sPropertyCollectionId] = {
          tableHeaderLabel: oPropertyCollection.label,
          rowData: aRowData
        };
        oLayoutData.tableIds.push(sPropertyCollectionId);
      }
    });
  };

  let _isPropertyValueEmpty = function (sPropertyType, oElementInstance) {
    switch (sPropertyType) {
      case ATTRIBUTE:
        if (ContentUtils.isAttributeTypeHtml(oElementInstance.type)) {
          return CS.isEmpty(CS.toString(oElementInstance.valueAsHtml));
        }
        return CS.isEmpty(CS.toString(oElementInstance.value));

      case TAG:
        let oInstanceTagGroupModel = oElementInstance.tagGroupModel.tagGroupModel;
        let aInstanceTagValues = oInstanceTagGroupModel.entityTag.tagValues;
        return CS.isEmpty(aInstanceTagValues);

      case RELATIONSHIP:
      case NATURE_RELATIONSHIP:
        return false;

      case TYPES:
      case TAXONOMIES:
        return CS.isEmpty(oElementInstance.values);
    }
  };

  /**
   * @param aInstanceTagValues
   * @param aSelectedTagValues
   * @returns {boolean}
   * @private
   */
  let _isTagValuesAreEqual = function (aInstanceTagValues = [], aSelectedTagValues = []) {
    let bIsEqual = true;

    aInstanceTagValues = aInstanceTagValues.filter(function (oTagValue) {
      return oTagValue.relevance !== 0;
    });
    aSelectedTagValues = aSelectedTagValues.filter(function (oTagValue) {
      return oTagValue.relevance !== 0;
    });

    if (aInstanceTagValues.length !== aSelectedTagValues.length) {
      return false;
    }
    CS.forEach(aInstanceTagValues, function (oTagValue) {
      let oSelectedTagValue = CS.find(aSelectedTagValues, {tagId: oTagValue.tagId});
      if (CS.isEmpty(oSelectedTagValue)) {
        bIsEqual = false;
      } else {
        bIsEqual = oSelectedTagValue.relevance === oTagValue.relevance;
      }
      if (!bIsEqual) return false;
    });
    return bIsEqual;
  };

  /**
   * @param sPropertyType
   * @param oInstanceValue
   * @param oSelectedValue
   * @returns {boolean}
   * @private
   */
  let _isPropertyValueEqual = function (sPropertyType, oInstanceValue, oSelectedValue) {
    switch (sPropertyType) {
      case ATTRIBUTE:
        return oInstanceValue.value == oSelectedValue.value;

      case TAG:
        let oInstanceTagGroupModel = oInstanceValue.tagGroupModel.tagGroupModel;
        let oSelectedTagGroupModel = oSelectedValue.tagGroupModel.tagGroupModel;
        let aInstanceTagValues = oInstanceTagGroupModel.entityTag.tagValues;
        let aSelectedTagValues = oSelectedTagGroupModel.entityTag.tagValues;
        return _isTagValuesAreEqual(aInstanceTagValues, aSelectedTagValues);

      case RELATIONSHIP:
      case NATURE_RELATIONSHIP:
        return oInstanceValue.sourceKlassInstanceId === oSelectedValue.sourceKlassInstanceId;
    }
  };

  let _getComparisonDataFirstRowInfo = (oMatchAndMergeComparisonData) => {
    let aTableIds = oMatchAndMergeComparisonData.tableIds;
    let oTableData = oMatchAndMergeComparisonData.tableData;

    let sFirstTableId = aTableIds[0];
    let oFirstTableData = oTableData[sFirstTableId];
    let aRowData = oFirstTableData.rowData;
    let sPropertyId = aRowData[0].id;
    return {sPropertyId, sFirstTableId};
  };

  /**
   * @param sTagId
   * @param aTagValueRelevanceData
   * @param oTagGroup
   * @returns {Array}
   * @private
   */
  let _updateGoldenRecordComparisonTagValues = (sTagId, aTagValueRelevanceData, oTagGroup, oReferencedTags) => {
    let aTagValues = [];
    let oMasterTag = CS.find(oReferencedTags, {id: oTagGroup.tagId});

    CS.forEach(aTagValueRelevanceData, function (oData) {
      let oEntityTag = CS.find(oTagGroup.tagValues, {tagId: oData.tagId});
      if (CS.isEmpty(oEntityTag)) {
        oEntityTag = {
          id: UniqueIdentifierGenerator.generateUUID(),
          tagId: oData.tagId,
          relevance: oData.relevance,
        };
      } else {
        oEntityTag.relevance = oData.relevance;
      }

      aTagValues.push(oEntityTag);
    });

    /**Required to remove all tag values, when clicking on MNMRightSection "No value" radio button*/
    if (CS.isEmpty(aTagValueRelevanceData)) {
     ContentUtils.updateUnselectedTagValuesInTagEntity(oTagGroup, []);
     return oTagGroup.tagValues;
    }

    /** Updating tagValues instead of setting new selected values
     *  Required in case of tag type is Ruler, Lifecycle, Listing etc.
     **/
    let aTagTypesToUpdateTagValues = [
      TagTypeConstants.RULER_TAG_TYPE,
      TagTypeConstants.LIFECYCLE_STATUS_TAG_TYPE,
      TagTypeConstants.LISTING_STATUS_TAG_TYPE
    ];
    if (CS.includes(aTagTypesToUpdateTagValues, oMasterTag.tagType)) {
      ContentUtils.updateUnselectedTagValuesInTagEntity(oTagGroup, aTagValues);
      return oTagGroup.tagValues
    }
    /**Set selected values only **/
    return aTagValues;
  };

  /**
   * @param sPropertyId
   * @param oPropertyData
   * @private
   */
  let _validatePropertyDetailDataOnValueChange = function (sPropertyId, oPropertyData, oActivePropertyDetailedData) {

    let oPropertyDetailedData = oActivePropertyDetailedData[sPropertyId];
    if (CS.isEmpty(oPropertyDetailedData)) {
      return;
    }
    let aElements = oPropertyDetailedData.elements;
    CS.forEach(aElements, function (oElement) {
      let oActiveProperty = oElement.property;
      oElement.isChecked = _isPropertyValueEqual(oActiveProperty.propertyType, oPropertyData, oActiveProperty);
    });
  };

  /**
   * @param sPropertyId
   * @param sTableId
   * @param oNewVal
   * @private
   */
  let _processContentComparisonMatchAndMergePropertyValueChanged = function (sPropertyId, sTableId, oData, oNewVal, oReferencedTags) {
    let {
      comparisonData: oComparisionData,
      activeGoldenRecordId: sActiveGoldenRecordId,
      dummyKlassInstance: oDummyKlassInstance,
      selectedLanguageId: sSelectedLanguageId,
      activePropertyDetailedData: oActivePropertyDetailedData
    } = oData;

    let oTableData = oComparisionData.tableData[sTableId];
    let oPropertyData = CS.find(oTableData.rowData, {id: sPropertyId});
    let sPropertyType = oPropertyData.propertyType;

    let oInstancePropertyData = {};
    let oElementInstance = {};

    switch (sPropertyType) {
      case ATTRIBUTE:
        if (oPropertyData.isDependent) {

          oInstancePropertyData = oDummyKlassInstance.dependentAttributes[sSelectedLanguageId][sPropertyId];
        } else {
          oInstancePropertyData = oDummyKlassInstance.attributes[sPropertyId]
        }
        oElementInstance = oInstancePropertyData[0];

        oPropertyData.value = oNewVal.value;
        oPropertyData.valueAsHtml = oNewVal.valueAsHtml;

        let bIsHTMLAttribute = ContentUtils.isAttributeTypeHtml(oPropertyData.type);
        let sValue = bIsHTMLAttribute ? oNewVal.valueAsHtml : oNewVal.value;
        let sValueAsHtml = bIsHTMLAttribute ? oNewVal.value : "";
        oElementInstance.value = sValue;
        oElementInstance.valueAsHtml = sValueAsHtml;

        /**Set Dirty Property in instance when Bucket has golden record*/
        !CS.isEmpty(sActiveGoldenRecordId) && (oElementInstance.isDirty = true);
        break;

      case TAG:
        let oTagGroupModel = oPropertyData.tagGroupModel;
        let oTagGroup = oTagGroupModel.tagGroupModel.entityTag;
        if (!oTagGroup.hasOwnProperty('previousSelectedValues')) {
          oTagGroup.previousSelectedValues = CS.cloneDeep(
              _getAllSelectedTagValues(oTagGroup.tagValues));
        }
        let aTagValues = _updateGoldenRecordComparisonTagValues(oNewVal.tagId, oNewVal.tagValueRelevanceData, oTagGroup, oReferencedTags);
        oTagGroupModel.tagGroupModel.entityTag.tagValues = aTagValues;
        oInstancePropertyData = oDummyKlassInstance.tags[oNewVal.tagId];
        oElementInstance = oInstancePropertyData[0];
        oElementInstance.tagValues = aTagValues;

        /**Set Dirty Property in instance when Bucket has golden record*/
        !CS.isEmpty(sActiveGoldenRecordId) && (oElementInstance.isDirty = true);
        break;

      case RELATIONSHIP:
      case NATURE_RELATIONSHIP:
        //TODO: Optimise Relationship finding
        oInstancePropertyData = CS.find(oDummyKlassInstance.relationships, {sideId: sPropertyId}) ||
            CS.find(oDummyKlassInstance.natureRelationships, {sideId: sPropertyId});
        oInstancePropertyData.sourceKlassInstanceId = oNewVal.sourceKlassInstanceId;
        oInstancePropertyData.referencedAssetsData = oNewVal.referencedAssetsData;
        oInstancePropertyData.paginationData = oNewVal.paginationData;

        oPropertyData.sourceKlassInstanceId = oNewVal.sourceKlassInstanceId;
        oPropertyData.referencedAssetsData = oNewVal.referencedAssetsData;
        oPropertyData.paginationData = oNewVal.paginationData;

        /**Set Dirty Property in instance when Bucket has golden record*/
        !CS.isEmpty(sActiveGoldenRecordId) && (oInstancePropertyData.isDirty = true);
        break;
    }

    /**
     * To equate value of the detail view (Show checked on the same value at detail view)
     */
    _validatePropertyDetailDataOnValueChange(sPropertyId, oPropertyData, oActivePropertyDetailedData);
  };

  let _postProcessDummyKlassInstanceForSave = function (oKlassInstance) {
    let oKlassInstanceAttributes = oKlassInstance.attributes;
    let oKlassInstanceDependentAttributes = oKlassInstance.dependentAttributes;
    let oKlassInstanceTags = oKlassInstance.tags;
    let aKlassInstanceRelationship = oKlassInstance.relationships;
    let aKlassInstanceNatureRelationships = oKlassInstance.natureRelationships;

    let oNewAttributeInstances = {};
    let oNewDependentAttributeInstances = {};
    let oNewTagInstances = {};

    /**
     * Filter to get attributes with isDirty Flag
     */
    CS.forEach(oKlassInstanceAttributes, function (aAttributeList, sKey) {
      let oAttribute = CS.get(aAttributeList, 0);
      if (oAttribute.isDirty) {
        delete oAttribute.isDirty;
        oNewAttributeInstances[sKey] = [oAttribute];
      }
    });

    /**
     * Filter to get dependent attributes with isDirty Flag
     */
    CS.forEach(oKlassInstanceDependentAttributes, function (oLanguageAttributeMap, sLanguageId) {
      let oDependentNewAttributeInstances = {};
      CS.forEach(oLanguageAttributeMap, function (aAttributeList, sKey) {
        let oAttribute = CS.get(aAttributeList, 0);
        if (oAttribute.isDirty) {
          delete oAttribute.isDirty;
          oDependentNewAttributeInstances[sKey] = [oAttribute];
        }
      });
      if (!CS.isEmpty(oDependentNewAttributeInstances)) {
        oNewDependentAttributeInstances[sLanguageId] = oDependentNewAttributeInstances;
      }
    });

    /**
     * Filter to get tag with non-zero relevance and isDirtyFlag
     */
    CS.forEach(oKlassInstanceTags, function (aTagList, sKey) {
      let oTag = CS.get(aTagList, 0);
      if (oTag.isDirty) {
        delete oTag.isDirty;
        delete oTag.tagId;
        let aTagValues = oTag.tagValues;
        if (!CS.isEmpty(aTagValues)) {
          let aAllSelectedValues = _getAllSelectedTagValues(aTagValues);
          let aPreviousSelectedValues = oTag.previousSelectedValues;
          let aDeletedValues = CS.differenceBy(CS.cloneDeep(aPreviousSelectedValues),
              aAllSelectedValues, "tagId");
          aDeletedValues.map((oValue) => {
            oValue.relevance = 0;
          });
          let aAddedValues = CS.differenceBy(aAllSelectedValues, aPreviousSelectedValues, "tagId");
          let aModifiedValues = aAllSelectedValues.filter((oValue) => {
            let oOldValue = CS.find(aPreviousSelectedValues, {tagId: oValue.tagId});
            return oOldValue && oValue.relevance != oOldValue.relevance;
        });

          let [...aADMValues] = aDeletedValues;
          aADMValues.push.apply(aADMValues, aAddedValues);
          aADMValues.push.apply(aADMValues, aModifiedValues);
          delete oTag.previousSelectedValues;
          oTag.tagValues = aADMValues;
        }
        CS.isNotEmpty(oTag.tagValues) && (oNewTagInstances[sKey] = [oTag]);
      }
    });

    /**
     * Filter to get relationship with isDirty Flag
     */
    CS.remove(aKlassInstanceRelationship, function (oRelationshipInstance) {
      delete oRelationshipInstance.referencedAssetsData;
      delete oRelationshipInstance.paginationData;
      let bIsDirty = oRelationshipInstance.isDirty;
      oRelationshipInstance.hasOwnProperty("isDirty") && (delete oRelationshipInstance.isDirty);
      return !bIsDirty;
    });

    /**
     * Filter to get nature relationship with isDirty Flag
     */
    CS.remove(aKlassInstanceNatureRelationships, function (oRelationshipInstance) {
      delete oRelationshipInstance.referencedAssetsData;
      delete oRelationshipInstance.paginationData;
      let bIsDirty = oRelationshipInstance.isDirty;
      oRelationshipInstance.hasOwnProperty("isDirty") && (delete oRelationshipInstance.isDirty);
      return !bIsDirty;
    });

    /**
     * Re-assigning klassInstanceValues
     */
    oKlassInstance.attributes = oNewAttributeInstances;
    oKlassInstance.dependentAttributes = oNewDependentAttributeInstances;
    oKlassInstance.tags = oNewTagInstances;
    oKlassInstance.relationships = CS.concat(aKlassInstanceRelationship, aKlassInstanceNatureRelationships);
    delete oKlassInstance.natureRelationships;
  };

  let _getAllSelectedTagValues = (aTagValues) => {
    let aUpdatedTagValues = [];
    CS.forEach(aTagValues, function (oTagValue) {
      if(oTagValue.relevance != 0){
        let oTag = {
          id: oTagValue.id,
          tagId: oTagValue.tagId,
          relevance: oTagValue.relevance,
          code: oTagValue.code
        };
        aUpdatedTagValues.push(oTag);
      }
    });

    return aUpdatedTagValues;
  };

  return {

    getLayoutSkeleton: function () {
      return _getLayoutSkeleton();
    },

    fillDummyKlassInstanceWithElements: function (oDummyKlassInstance, oConfigDetails, oPropertyRecommendations, oGoldenRecordInstance, oMatchPropertiesData, sSelectedLanguageId) {
      _fillDummyKlassInstanceWithElements(oDummyKlassInstance, oConfigDetails, oPropertyRecommendations, oGoldenRecordInstance, oMatchPropertiesData, sSelectedLanguageId);
    },

    createDummyKlassInstance: function (oDummyKlassInstance) {
      return _createDummyKlassInstance(oDummyKlassInstance);
    },

    makeInstanceComparisonRowDataForLanguageTable: function (oLayoutData, aSelectedLanguageList, sLanguageId) {
      _makeInstanceComparisonRowDataForLanguageTable(oLayoutData, aSelectedLanguageList, sLanguageId);
    },

    makeInstancesComparisonRowDataForFixedInformation: function (oLayoutData, oConfigDetails, aSelectedKlassIDs, aSelectedTaxonomyIds, oKlassBasicInformationData) {
      _makeInstancesComparisonRowDataForFixedInformation(oLayoutData, oConfigDetails, aSelectedKlassIDs, aSelectedTaxonomyIds, oKlassBasicInformationData)
    },

    prepareReferencedKlassesMap: function (aSelectedKlassIDs, oReferencedKlasses) {
      return _prepareReferencedKlassesMap(aSelectedKlassIDs, oReferencedKlasses);
    },

    prepareReferencedTaxonomiesMap: function (aSelectedTaxonomyIds, oReferencedTaxonomies) {
      return _prepareReferencedTaxonomiesMap(aSelectedTaxonomyIds, oReferencedTaxonomies);
    },

    prepareDataForPropertyCollection: function (oDummyKlassInstance, oLayoutData, oConfigDetails, oMatchPropertiesData, sSelectedLanguageId) {
      _prepareDataForPropertyCollection(oDummyKlassInstance, oLayoutData, oConfigDetails, oMatchPropertiesData, sSelectedLanguageId);
    },

    isPropertyValueEmpty: function (sPropertyType, oElementInstance) {
      return _isPropertyValueEmpty(sPropertyType, oElementInstance);
    },

    isPropertyValueEqual: function (sPropertyType, oInstanceValue, oSelectedValue) {
      return _isPropertyValueEqual(sPropertyType, oInstanceValue, oSelectedValue);
    },

    getComparisonDataFirstRowInfo: function (oComparisonData) {
      return _getComparisonDataFirstRowInfo(oComparisonData);
    },

    processContentComparisonMatchAndMergePropertyValueChanged: function (sPropertyId, sTableId, oData, oNewVal, oReferencedTags) {
      _processContentComparisonMatchAndMergePropertyValueChanged(sPropertyId, sTableId, oData, oNewVal, oReferencedTags);
    },

    postProcessDummyKlassInstanceForSave: function (oKlassInstance) {
      _postProcessDummyKlassInstanceForSave(oKlassInstance);
    },

  }


})();


export default MatchAndMergeUtils;
