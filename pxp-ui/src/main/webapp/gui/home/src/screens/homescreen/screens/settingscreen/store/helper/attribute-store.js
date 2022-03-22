
import CS from '../../../../../../libraries/cs';

import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import Exception from '../../../../../../libraries/exceptionhandling/exception.js';
import { AttributeRequestMapping as oAttributeRequestMapping } from '../../tack/setting-screen-request-mapping';
import SettingScreenProps from './../model/setting-screen-props';
import AttributeProps from './../model/attribute-config-view-props';
import SettingUtils from './../helper/setting-utils';
import MeasurementMetricBaseTypeDictionary from '../../../../../../commonmodule/tack/measurement-metric-base-type-dictionary';
import AttributeDictionary from './../../../../../../commonmodule/tack/attribute-type-dictionary-new';
import MockAttributeTypes from './../../../../../../commonmodule/tack/mock-data-for-attribute-types';
import MockDataForRTEIcons from '../../tack/mock/mock-data-for-rich-text-editor-plugins.js';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import MockAttributeUnits from '../../../../../../commonmodule/tack/mock-data-for-measurement-metrics-and-imperial';
import MetricCheckConstants from '../../../../../../commonmodule/tack/measurement-metrics-constants';
import AttributeUtils from '../.././../../../../commonmodule/util/attribute-utils';
import GridViewContexts from '../.././../../../../commonmodule/tack/grid-view-contexts';
import ConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import ConfigModulePropertyGroupTypeDictionary from '../../tack/settinglayouttack/config-module-data-model-property-group-type-dictionary';
import assetTypes from '../../tack/coverflow-asset-type-list';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import ExceptionLogger from '../../../../../../libraries/exceptionhandling/exception-logger';
import GridViewStore from '../../../../../../viewlibraries/contextualgridview/store/grid-view-store';
import CommonUtils from "../../../../../../commonmodule/util/common-utils";

var AttributeStore = (function () {

  var _triggerChange = function () {
    AttributeStore.trigger('attribute-changed');
  };

  var _getUnitAttributeTypeList = function () {
    var aAttributeTypeList = SettingUtils.getAppData().getAttributeTypes();
    var aUnitAttributeList = [];
    CS.forEach(aAttributeTypeList, function (oType) {
      if (!oType.isStandard && oType.isUnitAttribute) {
        aUnitAttributeList.push(oType.value);
      }
    });
    return aUnitAttributeList;
  };

  var _makeActiveAttributeDirty = function () {
    var sSelectedAttributeId = _getSelectedAttribute().id;
    var oAttributeMap = SettingUtils.getAppData().getAttributeList();
    var oActiveAttribute = oAttributeMap[sSelectedAttributeId];
    SettingUtils.makeObjectDirty(oActiveAttribute);
    return oActiveAttribute.clonedObject;
  };

  var successDeleteAttributeCallback = function (oResponse) {
    var aSuccessIds = oResponse.success;
    var aFailureIds = oResponse.failure ? oResponse.failure.exceptionDetails : [];
    let oGridViewProps = GridViewStore.getGridViewPropsByContext(GridViewContexts.ATTRIBUTE);
    var aGridViewData = oGridViewProps.getGridViewData();

    var oMasterAttributesMap = SettingUtils.getAppData().getAttributeList(); // get master attribute list

    var oSkeleton = oGridViewProps.getGridViewSkeleton();
    CS.forEach(aSuccessIds, function (sId) {
      CS.remove(aGridViewData, {id: sId});
      delete oMasterAttributesMap[sId];
      CS.remove(oSkeleton.selectedContentIds, function (oSelectedId) {
        return oSelectedId == sId;
      });

    });
    oGridViewProps.setGridViewTotalItems(oGridViewProps.getGridViewTotalItems() - aSuccessIds.length); //remove the number of deleted attributes from total count
    if (aSuccessIds && aSuccessIds.length > 0) {
      alertify.success(getTranslation().ATTRIBUTE_DELETE_SUCCESSFUL);
    }

    if(aFailureIds && aFailureIds.length > 0 ) {
      handleDeleteAttributeFailure(aFailureIds);
    }
  };

  var handleDeleteAttributeFailure = function(List){
    var aAttributeAlreadyDeleted = [];
    var aUnhandledAttribute = [];
    var aAttributeCannotDeleted = [];
    var aAttributeGridData = AttributeProps.getAttributeGridData();
    CS.forEach(List, function (oItem){
      var oAttribute = CS.find(aAttributeGridData, {id: oItem.itemId});
      if(oItem.key == "AttributeNotFoundException"){
        aAttributeAlreadyDeleted.push(oAttribute.label);
      } else if (oItem.key == "InstanceExistsForAttributeException") {
        aAttributeCannotDeleted.push(oAttribute.label);
      } else {
        aUnhandledAttribute.push(oAttribute.label);
      }
    });

    if(aAttributeAlreadyDeleted.length>0){
      var sAttributeAlreadyDeleted = aAttributeAlreadyDeleted.join(',');
      alertify.error(Exception.getCustomMessage("Attribute_already_deleted", getTranslation(), sAttributeAlreadyDeleted), 0);
    }
    if(aUnhandledAttribute.length>0){
      var sUnhandledAttribute = aUnhandledAttribute.join(',');
      alertify.error(Exception.getCustomMessage("Unhandled_Attribute", getTranslation(), sUnhandledAttribute), 0);
    }
    if (aAttributeCannotDeleted.length > 0) {
      alertify.error(Exception.getCustomMessage("InstanceExistsForAttributeException", getTranslation(), aAttributeCannotDeleted.join(',')), 0);
    }
  };

  var failureDeleteAttributeCallback = function (oCallback, oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      let configError = CS.reduce(oResponse.failure.exceptionDetails, (isConfigError, error) => {
        if (error.key === "EntityConfigurationDependencyException") {
          isConfigError = true;
        }
        return isConfigError;
      }, false);
      if (configError) {
        if (oCallback && oCallback.functionToExecute) {
          oCallback.functionToExecute();
          return;
        }
      }
      handleDeleteAttributeFailure(oResponse.failure.exceptionDetails);
    } else {
      SettingUtils.failureCallback(oResponse, "failureDeleteAttributeCallback", getTranslation());
    }
    _triggerChange();
  };

  var successSaveAttributeCallback = function (oCallbackData, oResponse) {
    var oSavedAttribute = oResponse.success;
    var aAttributeGridData = AttributeProps.getAttributeGridData();
    aAttributeGridData.push(oSavedAttribute);
    let oConfigDetails = {
      referencedContexts: SettingScreenProps.screen.getReferencedContexts()
    };
    var oProcessedAttribute = GridViewStore.getProcessedGridViewData(GridViewContexts.ATTRIBUTE, [oSavedAttribute], "", oConfigDetails)[0];
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.ATTRIBUTE);
    var aGridViewData = oGridViewPropsByContext.getGridViewData();
    aGridViewData.unshift(oProcessedAttribute);

    var oMasterAttributesMap = SettingUtils.getAppData().getAttributeList(); // get master attribute list
    oMasterAttributesMap[oSavedAttribute.id] = oSavedAttribute;

    oGridViewPropsByContext.setGridViewTotalItems(oGridViewPropsByContext.getGridViewTotalItems() + 1); //increase total count when new attribute created

    if (oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }

    alertify.success(getTranslation().ATTRIBUTE_CREATED_SUCCESSFULLY);
    _triggerChange();
  };

  var failureSaveAttributeCallback = function (oCallbackData, oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      var sAttribute = checkAndDeleteAtributeFromList(oResponse);
      alertify.error("[" + sAttribute +"] "+getTranslation().ERROR_ALREADY_DELETED, 0);

    } else {
      SettingUtils.failureCallback(oResponse, "failureSaveAttributeCallback", getTranslation());
    }
    _triggerChange();
  };

  var successGetAttributeDetailsByIdCallback = function (sSelectedAttributeId, oCallbackData, oResponse) {
    var oAttributeFromServer = oResponse.success;
    var oScreenProps = SettingUtils.getComponentProps().screen;
    var oLoadedAttributeData = oScreenProps.getLoadedAttributesData();
    oLoadedAttributeData[oAttributeFromServer.id] = oAttributeFromServer;
    if (oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute(oAttributeFromServer);
    }
    _triggerChange();
  };

  var failureGetAttributeDetailsByIdCallback = function (oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      SettingUtils.failureCallback(oResponse, "failureGetAttributeDetailsByIdCallback", getTranslation())
    } else {
      ExceptionLogger.error("failureGetAttributeDetailsByIdCallback: Something went wrong" + oResponse);
    }
    _triggerChange();
  };

  var successGetCalculatedAttributeMappingCallback = function(oResponse) {
    var oMapping = oResponse.success.mapping;
    SettingUtils.getAppData().setCalculatedAttributeMapping(oMapping);

  };

  var failureGetCalculatedAttributeMappingCallback = function(oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchAttributeListCallback", getTranslation());
  };

  var checkAndDeleteAtributeFromList = function (oResponse, sAttributeId) {
    var oMasterAttributes = SettingUtils.getAppData().getAttributeList();

    var oSelectedAttribute = AttributeProps.getSelectedAttribute();
    if (oResponse.success.message.indexOf('NOT_FOUND') >= 0 && (oSelectedAttribute || sAttributeId)) {
      var sAttributeName = oMasterAttributes[sAttributeId || oSelectedAttribute.id].label;
      delete oMasterAttributes[sAttributeId || oSelectedAttribute.id];
      AttributeProps.setSelectedAttribute({});
      return sAttributeName;
    }
    return null;
  };

  var _getHtmlAttributes = function () {
    var aAttributeList = SettingUtils.getAppData().getAttributeList();
    return CS.filter(aAttributeList, function (oAttribute) {
      return SettingUtils.isAttributeTypeHtml(oAttribute.type) && oAttribute.id;
    });
  };

  var _getListForDefaultHTMLStyle = function (aRTEIconsList) {
    var aListToGet = ['bold', 'italic'];
    var aListToReturn = [];

    CS.forEach(aRTEIconsList, function (oIcon) {
      if(CS.includes(aListToGet, oIcon.id)) {
        aListToReturn.push(CS.cloneDeep(oIcon));
      }
    });

    return aListToReturn;
  };

  var _resetDefaultPropertiesForMSSProps = function (oMSSProp) {
    oMSSProp.list = [];
    oMSSProp.filteredList = [];
    oMSSProp.isActive = false;
  };

  var _setMSSPropForAttribute = function (oAttribute) {
    var oMSSProps = AttributeProps.getMSSProps();

    var oMSSPropsForAllowedSiblings = oMSSProps['attributeDetailAllowedFrame'];
    _resetDefaultPropertiesForMSSProps(oMSSPropsForAllowedSiblings);

    var oMSSPropsForAllowedRTEIcons = oMSSProps['attributeRTEIcon'];
    _resetDefaultPropertiesForMSSProps(oMSSPropsForAllowedRTEIcons);

    var oMSSPropsForDefaultStyles = oMSSProps['attributeDefaultStyles'];
    _resetDefaultPropertiesForMSSProps(oMSSPropsForDefaultStyles);

    if(SettingUtils.isAttributeTypeHtml(oAttribute.type)) {
      var aAttributeListOfHTMLAndImageType = _getHtmlAttributes();

      //MSS Props for allowed siblings
      oMSSPropsForAllowedSiblings.list = aAttributeListOfHTMLAndImageType;
      oMSSPropsForAllowedSiblings.filteredList = CS.cloneDeep(aAttributeListOfHTMLAndImageType);
      oMSSPropsForAllowedSiblings.isActive = false;

    }

    if(SettingUtils.isAttributeTypeHtml(oAttribute.type)) {

      //MSS Props for allowed RTE Icons
      var aAttributeIconList = CS.cloneDeep(MockDataForRTEIcons);
      oMSSPropsForAllowedRTEIcons.list = aAttributeIconList;
      oMSSPropsForAllowedRTEIcons.filteredList = CS.cloneDeep(aAttributeIconList);
      oMSSPropsForAllowedRTEIcons.isActive= false;

      //MSS Props for default Styles
      var aDefaultStyleList = _getListForDefaultHTMLStyle(MockDataForRTEIcons);
      oMSSPropsForDefaultStyles.list = aDefaultStyleList;
      oMSSPropsForDefaultStyles.filteredList = CS.cloneDeep(aDefaultStyleList);
      oMSSPropsForDefaultStyles.isActive= false;
    }
  };

  var _setUpAttributeConfigGridView = function () {
    //set skeleton
    let oGridViewSkeleton = SettingUtils.getGridSkeletonForAttribute();
    let sContext = GridViewContexts.ATTRIBUTE;
    GridViewStore.createGridViewPropsByContext(sContext, {skeleton: oGridViewSkeleton});
    //fetch attribute list
    _fetchAttributeListForGridView(sContext);
  };

  var _fetchAttributeListForGridView = function (sContext) {
    let oGridDataToFetchList = GridViewStore.getPostDataToFetchList(GridViewContexts.ATTRIBUTE);
    let sSelectedLeftTreeNode = SettingScreenProps.screen.getSelectedLeftNavigationTreeItem();
    let sSelectedParentId = SettingScreenProps.screen.getSelectedLeftNavigationTreeParentId();
    let oSelectedLeftTreeNode = SettingUtils.getSelectedTreeItemById(sSelectedLeftTreeNode, sSelectedParentId);
    oGridDataToFetchList.types = oSelectedLeftTreeNode.types;
    if(sSelectedLeftTreeNode === ConfigModulePropertyGroupTypeDictionary.STANDARD) {
      oGridDataToFetchList.isStandard = true;
    }
    SettingUtils.csPostRequest(oAttributeRequestMapping.bulkGet, {}, oGridDataToFetchList, successFetchAttributeListForGridView, failureFetchAttributeListForGridView);

  };

  var successFetchAttributeListForGridView = function (oResponse) {
    var oResponseData = oResponse.success;
    AttributeProps.setAttributeGridData(oResponseData.attributeList);
    let oConfigDetails = oResponseData.configDetails;
    GridViewStore.preProcessDataForGridView(GridViewContexts.ATTRIBUTE, oResponseData.attributeList, oResponseData.count,
        oConfigDetails);
    SettingScreenProps.screen.setReferencedContexts(oConfigDetails.referencedContexts);
    _triggerChange();
  };

  var failureFetchAttributeListForGridView = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchAttributeListForGridView", getTranslation());
  };

  var _postProcessAttributeListAndSave = function (oCallbackData) {
    return _saveAttributesInBulk(oCallbackData);

  };

  var _saveAttributesInBulk = function (oCallbackData) {
    let aAttributeListToSave = [];
    let bSafeToSave = GridViewStore.processGridDataToSave(aAttributeListToSave, GridViewContexts.ATTRIBUTE, AttributeProps.getAttributeGridData());
    if(bSafeToSave) {
      return SettingUtils.csPostRequest(oAttributeRequestMapping.save, {}, aAttributeListToSave, successSaveAttributesInBulk.bind(this, oCallbackData), failureSaveAttributesInBulk);
    }
    return CommonUtils.rejectEmptyPromise();
  };

  var successSaveAttributesInBulk = function (oCallbackData, oResponse) {
    let aAttributeList = oResponse.success.attributesList;
    let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.ATTRIBUTE, aAttributeList, "",
        oResponse.success.configDetails);
    var aAttributeGridData = AttributeProps.getAttributeGridData(); //saved Unprocessed data
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.ATTRIBUTE);

    var aGridViewData = oGridViewPropsByContext.getGridViewData(); //saved Processed data
    var oMasterAttributesMap = SettingUtils.getAppData().getAttributeList(); // get master attribute list

    let aGridViewSkeleton = oGridViewPropsByContext.getGridViewSkeleton();
    let aScrollableColumns = aGridViewSkeleton.scrollableColumns;

    let sSelectedTreeItem = SettingScreenProps.screen.getSelectedLeftNavigationTreeItem();

    if(sSelectedTreeItem === ConfigModulePropertyGroupTypeDictionary.CONCATENATED) {
      let concatenatedAttributeListSkeleton = CS.find(aScrollableColumns, {"id" : "attributeConcatenatedList"});
      if(concatenatedAttributeListSkeleton) {
        let oExtraData = concatenatedAttributeListSkeleton.extraData;
        let oConfigDetails = oResponse.success.configDetails;

        if (!CS.isEmpty(oExtraData.referencedAttributes)) {
          oExtraData.referencedAttributes = {};
        }
        if (!CS.isEmpty(oExtraData.referencedTags)) {
          oExtraData.referencedTags = {};
        }
        CS.assign(oExtraData.referencedAttributes, oConfigDetails.referencedAttributes);
        CS.assign(oExtraData.referencedTags, oConfigDetails.referencedTags);
      }
    }

    CS.forEach(aAttributeList, function (oAttribute) { //add the successfully saved attributes into stored data:
      var attributeId = oAttribute.id;
      var iIndex = CS.findIndex(aAttributeGridData, {id: attributeId});
      aAttributeGridData[iIndex] = oAttribute;
      oMasterAttributesMap[attributeId] = oAttribute; // update master attribute list
    });

    CS.forEach(aProcessedGridViewData, function (oProcessedAttribute) { //add the processed attributes into processed data :
      var iIndex = CS.findIndex(aGridViewData, {id: oProcessedAttribute.id});
      aGridViewData[iIndex] = oProcessedAttribute;
    });

    //todo: can merge the above 2 loops into 1, but need to analyse the risk factor

    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY,{ entity : getTranslation().ATTRIBUTE}));
    oGridViewPropsByContext.setIsGridDataDirty(false);
    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
  };

  var failureSaveAttributesInBulk = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureSaveAttributesInBulk", getTranslation());
  };

  let _discardAttributeGridViewChanges = function (oCallbackData) {
    let aAttributeGridData = AttributeProps.getAttributeGridData(); //saved Original(unprocessed) data
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.ATTRIBUTE);
    let aGridViewData = oGridViewPropsByContext.getGridViewData(); //saved Processed data
    let bShowDiscardMessage = false;

    CS.forEach(aGridViewData, function (oOldProcessedAttribute, iIndex) { //add the successfully saved attributes into stored data:
      if (oOldProcessedAttribute.isDirty) {
        // get the original attribute object and reprocess it -
        var oAttribute = CS.find(aAttributeGridData, {id: oOldProcessedAttribute.id});
        let oAttributeConcatenatedList = oOldProcessedAttribute.properties.attributeConcatenatedList;
        let oReferencedData = oAttributeConcatenatedList && oAttributeConcatenatedList.referencedData || {};
        oReferencedData.referencedContexts = SettingScreenProps.screen.getReferencedContexts();

        aGridViewData[iIndex] = GridViewStore.getProcessedGridViewData(GridViewContexts.ATTRIBUTE, [oAttribute], "", oReferencedData)[0];
        bShowDiscardMessage = true;
      }
    });

    bShowDiscardMessage && alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
    oGridViewPropsByContext.setIsGridDataDirty(false);
    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
    _triggerChange();
  };

  var _deleteAttributes = function (aBulkDeletelist, oCallBack) {
    let sURL = oAttributeRequestMapping.bulkDelete;
    var aFilteredAttributeIds = aBulkDeletelist; //todo: temp use
    //todo: pending, partial success/failure handling
    if (!CS.isEmpty(aFilteredAttributeIds)) {
      return SettingUtils.csDeleteRequest(sURL, {}, {ids: aFilteredAttributeIds}, successDeleteAttributeCallback, failureDeleteAttributeCallback.bind(this, oCallBack));
    } else {
      alertify.success(getTranslation().ATTRIBUTE_DELETE_SUCCESSFUL);
      _triggerChange();
    }
  };

  var _getSelectedAttribute = function () {
    return AttributeProps.getSelectedAttribute();
  };

  var _getAttributeDetailsById = function (sSelectedAttributeId, oCallbackData) {

    SettingUtils.csGetRequest(oAttributeRequestMapping.get, {id: sSelectedAttributeId}, successGetAttributeDetailsByIdCallback.bind(this, sSelectedAttributeId, oCallbackData), failureGetAttributeDetailsByIdCallback);

  };

  var _createUntitledAttributeMasterObject = function () {
    let sSelectedAttributeGroupingType = SettingScreenProps.screen.getSelectedLeftNavigationTreeItem();
    let sSelectedAttributeParentGroupingType = SettingScreenProps.screen.getSelectedLeftNavigationTreeParentId();
    let oAttributeGroup = SettingUtils.getSelectedTreeItemById(sSelectedAttributeGroupingType, sSelectedAttributeParentGroupingType);
    let sType = oAttributeGroup.types[0];
    let sDefaultUnit = MeasurementMetricBaseTypeDictionary[sType] || '';
    let bIsTranslatable = AttributeUtils.isAttributeTypeConcatenated(sType);

    return {
      id: "",
      label: UniqueIdentifierGenerator.generateUntitledName(),
      isCreated: true,
      defaultUnit: sDefaultUnit,
      defaultValue: "",
      description: "",
      icon: "",
      isMandatory: false,
      isStandard : false,
      tooltip: "",
      placeholder: "",
      precision: 0,
      type: sType,
      code: "",
      isFilterable: false,
      isSortable: false,
      availability: [],
      isGridEditable: false,
      isTranslatable: bIsTranslatable,
      isVersionable: true,
    }
  };

  var _setSelectedAttribute = function (oAttribute) {
    AttributeProps.setSelectedAttribute(oAttribute);
  };

  var _addOperator = function(sType){
    var oAttributeMaster = _makeActiveAttributeDirty();
    var aAttributeOperatorList = oAttributeMaster["attributeOperatorList"] || [];

    var iAttributeOperatorListLength = aAttributeOperatorList.length;
    var sValue = null;
    var sAttributeId = null;
    var sOperator = null;
    var uuid = UniqueIdentifierGenerator.generateUUID();

    if(sType != "ATTRIBUTE" && sType != "VALUE"){
      sOperator = sType;
    }

    var oAttributeOperator = {
      id: uuid,
      attributeId: sAttributeId,
      operator: sOperator,
      type: sType,
      value: sValue,
      order: iAttributeOperatorListLength
    };

    aAttributeOperatorList.push(oAttributeOperator);
    oAttributeMaster["attributeOperatorList"] = aAttributeOperatorList;

  };

  var _addConcatObject = function (sType) {
    var oAttributeMaster = _makeActiveAttributeDirty();
    var uuid = UniqueIdentifierGenerator.generateUUID();
    var aAttributeConcatenatedList = oAttributeMaster["attributeConcatenatedList"] || [];
    var iAttributeConcatenatedListLength = aAttributeConcatenatedList.length;
    var sValue = null;
    var sAttributeId = null;

    var oAttributeConcat = {
      id: uuid,
      attributeId: sAttributeId,
      type: sType,
      value: sValue,
      order: iAttributeConcatenatedListLength
    };

    aAttributeConcatenatedList.push(oAttributeConcat);
    oAttributeMaster["attributeConcatenatedList"] = aAttributeConcatenatedList;

  };

  var _deleteOperatorAttributeValue = function(sId){
    var oAttributeMaster = _makeActiveAttributeDirty();
    var aAttributeOperatorList = oAttributeMaster["attributeOperatorList"];

    var iCount = 0;
    CS.remove(aAttributeOperatorList, function (oAttributeOperator) {
      if(oAttributeOperator.id == sId) {
        return true;
      }
      oAttributeOperator.order = iCount++;
    });

  };

  var _handleOperatorAttributeValueChanged = function(sId, sType, sValue){
    var oAttributeMaster = _makeActiveAttributeDirty();

    var aAttributeOperatorList = oAttributeMaster["attributeOperatorList"];
    var oAttributeOperator = CS.find(aAttributeOperatorList, {id: sId});
    if(oAttributeOperator) {
      var sOperator = null;
      var sVal = null;
      var sAttributeId = null;

      if(sType == "ATTRIBUTE"){
        sAttributeId = sValue;
      }else if(sType == "VALUE"){
        sVal = sValue;
      }else{
        sOperator = sValue;
        sType = sValue;
      }

      oAttributeOperator.attributeId = sAttributeId;
      oAttributeOperator.operator = sOperator;
      oAttributeOperator.value = sVal;
      oAttributeOperator.type = sType;
    }

  };

  var _attributeSingleTextChanged = function (sKey, sValue) {
    if(sKey == 'numberOfItemsAllowed'&& sValue < 0){
      alertify.message(getTranslation().ERROR_NO_NEGATIVE_NUMBERS);
      return;
    }
    var oAttributeMaster = _makeActiveAttributeDirty();
    if(SettingUtils.isAttributeTypeDate(oAttributeMaster.type) && sKey == "defaultValue" && sValue != ""){
      var oNewDate = new Date(sValue);
      oAttributeMaster[sKey] = oNewDate.getTime();
    }else{
      oAttributeMaster[sKey] = sValue;
    }
  };

  var _attributeFroalaTextChanged = function (sKey, oValue) {
    var oAttributeMaster = _makeActiveAttributeDirty();
    switch (sKey){

      case "customDefaultUnit":
        oAttributeMaster["defaultUnit"] = oValue.value;
        oAttributeMaster["defaultUnitAsHTML"] = oValue.valueAsHtml;
        break;

      case "calculatedAttributeUnitAsHTML":
        oAttributeMaster["calculatedAttributeUnit"] = oValue.value;
        oAttributeMaster["calculatedAttributeUnitAsHTML"] = oValue.valueAsHtml;
        break;

    }

  };

  var _attributeNativeDropdownValueChanged = function (sKey, sVal) {
    var oAttributeMaster = _makeActiveAttributeDirty();

    oAttributeMaster[sKey] = sVal;
  };

  var _handleCommonConfigSectionConcatInputChanged = function (sValue, oConcat) {
    var oAttributeMaster = _makeActiveAttributeDirty();

    var aAttributeConcatenatedList = oAttributeMaster["attributeConcatenatedList"];
    var oCurrentConcatString = CS.find(aAttributeConcatenatedList, {id: oConcat.id});
    if(oConcat.type == "string"){
      oCurrentConcatString.value = sValue;
    } else if (oConcat.type == "html"){
      oCurrentConcatString.value = sValue.value;
      oCurrentConcatString.valueAsHtml = sValue.valueAsHtml;
    }else {
      oCurrentConcatString.attributeId = sValue;
    }

  };

  var _handleCommonConfigSectionConcatObjectRemoved = function (sConcatId) {
    var oAttributeMaster = _makeActiveAttributeDirty();
    var aAttributeConcatenatedList = oAttributeMaster["attributeConcatenatedList"];

    var iCount = 0;
    CS.remove(aAttributeConcatenatedList, function (oConcat) {
      if(oConcat.id == sConcatId) {
        return true;
      }
      oConcat.order = iCount++;
    });

  };

  var _getCalculatedAttributeMapping= function () {
    SettingUtils.csGetRequest(oAttributeRequestMapping.getCalculatedAttributesMapping, {}, successGetCalculatedAttributeMappingCallback, failureGetCalculatedAttributeMappingCallback);
  };

  var _createAttributeDialogClick= function () {
    var oSelectedAttribute = _getSelectedAttribute();
    oSelectedAttribute = oSelectedAttribute.clonedObject || oSelectedAttribute;
    var oCallbackData = {
      functionToExecute: _triggerChange
    };

    if(CS.trim(oSelectedAttribute.label) == "") {
      alertify.message(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return;
    }
    var oCodeToVerifyUniqueness = {
      id: oSelectedAttribute.code,
      entityType: ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTE

    };
    var oCallbackDataForUniqueness = {};
    oCallbackDataForUniqueness.functionToExecute = function(){
      delete oSelectedAttribute.isCreated;
      _createAttributeCall(oSelectedAttribute, oCallbackData);
    };
    var sURL = oAttributeRequestMapping.CheckEntityCode;
    SettingUtils.checkForUniquenessOfCodePropertyAndSendAjaxCall(oCodeToVerifyUniqueness, sURL, oCallbackDataForUniqueness);
  };

  var _createAttributeCall = function(oSelectedAttribute, oCallbackData) {
    SettingUtils.csPutRequest(oAttributeRequestMapping.create, {}, oSelectedAttribute, successSaveAttributeCallback.bind(this, oCallbackData), failureSaveAttributeCallback.bind(this, oCallbackData));
  };

  var _cancelAttributeDialogClicked= function () {
    var aAttributeList = SettingUtils.getAppData().getAttributeList();
    var oActiveAttribute = _getSelectedAttribute();
    delete aAttributeList[oActiveAttribute.id];
    _setSelectedAttribute({});
    _triggerChange();
  };

  var _handleAttributeConfigDialogButtonClicked= function (sButtonId) {
    if (sButtonId == "create") {
      _createAttributeDialogClick({});
    } else {
      _cancelAttributeDialogClicked();
    }
  };

  var _getIsValidFileTypes = function (oFile) {
    // var aValidTypes = AllowedMediaTypeDictionary[sAssetType];
    var sTypeRegex = assetTypes.map(function (sType) {
      return '\\' + sType + '$';
    }).join('|');
    return oFile.name.match(new RegExp(sTypeRegex, 'i'));
  };

  var _handleExportAttribute = function (oSelectiveExportDetails) {
    SettingUtils.csPostRequest(oSelectiveExportDetails.sUrl, {}, oSelectiveExportDetails.oPostRequest, successExportFile, failureExportFile);
  };

  var successExportFile = function (oResponse) {
    alertify.success(getTranslation().EXPORT_IN_PROGRESS);
    SettingUtils.downloadFromByteStream(oResponse.success.fileStream, oResponse.success.fileName);
  };

  var failureExportFile = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureExportFile", getTranslation());
  };

  var uploadFileImport = function (oImportExcel, oCallback) {
    SettingUtils.csCustomPostRequest(oImportExcel.sUrl, oImportExcel.data,
        successUploadFileImport.bind(this, oCallback), failureUploadFileImport, false);
  };

  var successUploadFileImport = function (oResponse) {
    alertify.success(getTranslation().IMPORT_IN_PROGRESS);
  };

  var failureUploadFileImport = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureUploadFileImport", getTranslation());
  };

  var _handleAttributeFileUploaded = function (aFiles,oImportExcel) {
    var bIsAnyValidImage = false;
    var bIsAnyInvalidImage = false;
    if (aFiles.length) {

      var iFilesInProcessCount = 0;
      var count = 0;

      var data = new FormData();
      CS.forEach(aFiles, function (file, index) {
        if (_getIsValidFileTypes(file)) {
          bIsAnyValidImage = true;
          var reader = new FileReader();
          iFilesInProcessCount++;
          // Closure to capture the file information.
          reader.onload = (function (theFile) {
            return function (event) {
              count += 1;
              var filekey = UniqueIdentifierGenerator.generateUUID();
              data.append(filekey, theFile);
            };
          })(file);

          reader.onloadend = function () {
            iFilesInProcessCount--;
            if (iFilesInProcessCount == 0) {
              data.append("entityType", oImportExcel.entityType);
              oImportExcel.data = data;
              uploadFileImport(oImportExcel, {});
            }
          };

          reader.readAsDataURL(file);
        } else {
          bIsAnyInvalidImage = true;
        }
      });

    }
  };

  /************************** PUBLIC ***********************************/

  return {

    fetchAttributeListForGridView: function (sContext) {
      _fetchAttributeListForGridView(sContext);
    },

    setUpAttributeConfigGridView: function () {
      _setUpAttributeConfigGridView();
    },

    postProcessAttributeListAndSave: function (oCallbackData) {
      _postProcessAttributeListAndSave(oCallbackData)
          .then(_triggerChange);
    },

    discardAttributeGridViewChanges: function (oCallbackData) {
      _discardAttributeGridViewChanges(oCallbackData);
    },

    fetchAttributeTypes: function(){
      let oMockAttributeTypes = new MockAttributeTypes();
      SettingUtils.getAppData().setAttributeTypes(oMockAttributeTypes);
    },

    deleteAttribute: function (aAttributeIdsListToDelete, oCallBack) {
      var oMasterAttributes = AttributeProps.getAttributeGridData();

      if (!CS.isEmpty(aAttributeIdsListToDelete)) {
        var aBulkDeleteAttributes = [];
        var isStandardAttributeSelected = false;
        CS.forEach(aAttributeIdsListToDelete, function (AttrId) {
          var oMasterAttribute = CS.find(oMasterAttributes, {id: AttrId});
          var sMasterAttributeLabel = CS.getLabelOrCode(oMasterAttribute);
          aBulkDeleteAttributes.push(sMasterAttributeLabel);
          if(oMasterAttribute.isStandard){
            isStandardAttributeSelected = true;
            return true;
          }
        });
        if(isStandardAttributeSelected){
          alertify.message(getTranslation().STANDARD_ATTRIBUTE_DELETEION);
        } else{
          CustomActionDialogStore.showListModeConfirmDialog(getTranslation().DELETE_CONFIRMATION, aBulkDeleteAttributes,
              function () {
                _deleteAttributes(aAttributeIdsListToDelete, oCallBack)
                .then(_fetchAttributeListForGridView);
              }, function (oEvent) {
              }, true);
        }
      } else {
        alertify.message(getTranslation().NOTHING_SELECTED_TO_DELETE);
      }
    },

    createAttribute: function () {
      var uuid = UniqueIdentifierGenerator.generateUUID();
      var oUntitledAttributeMasterObj = _createUntitledAttributeMasterObject();

      oUntitledAttributeMasterObj.id = uuid;

      if(SettingUtils.isAttributeTypeHtml(oUntitledAttributeMasterObj.type)) {
        var oNewValidator = {
          allowedFrames:[]
        };

          oNewValidator.defaultStyles = [];
          oNewValidator.allowedRTEIcons = [];
        oUntitledAttributeMasterObj.validator = oNewValidator;
      }

      var oAttrWithoutId = CS.cloneDeep(oUntitledAttributeMasterObj);
      oAttrWithoutId.id = "";
      _setSelectedAttribute(oAttrWithoutId);
      var oAttributeMap = SettingUtils.getAppData().getAttributeList();
      oAttributeMap[oAttrWithoutId.id] = oAttrWithoutId;
      _triggerChange();
    },

    handleAttributeSingleTextChanged: function (sKey, sVal) {
      _attributeSingleTextChanged(sKey, sVal);
    },

    handleAttributeMSSValueChanged: function (sKey, aItems, oReferencedData) {
      if (sKey == "contextId") {
        CS.assign(SettingScreenProps.screen.getReferencedContexts(),oReferencedData);
        _attributeSingleTextChanged(sKey, aItems[0]);
      } else {
        _attributeSingleTextChanged(sKey, aItems);
      }
      _triggerChange();
    },

    handleAttributeFroalaTextChanged: function (sKey, oVal) {
      _attributeFroalaTextChanged(sKey, oVal);
    },

    handleCommonConfigSectionAddOperator: function(sOperatorType){
      _addOperator(sOperatorType);
    },

    handleCommonConfigSectionAddConcatObject: function (sType) {
      _addConcatObject(sType);
    },

    handleCommonConfigSectionConcatInputChanged: function (sValue, oConcat) {
      _handleCommonConfigSectionConcatInputChanged(sValue, oConcat);
    },

    handleCommonConfigSectionConcatObjectRemoved: function (sId) {
      _handleCommonConfigSectionConcatObjectRemoved(sId);
    },

    handleOperatorAttributeValueChanged: function(sId, sType, sValue){
      _handleOperatorAttributeValueChanged(sId, sType, sValue);
    },

    deleteOperatorAttributeValue: function(sId){
      _deleteOperatorAttributeValue(sId);
    },

    handleAttributeNativeDropdownValueChanged: function (sKey, sVal) {
      _attributeNativeDropdownValueChanged(sKey, sVal);
    },

    handleAllowedFrameMultiSelectListItemClicked: function (sContext, sItemId) {
      var sSplitter = SettingUtils.getSplitter();
      var oList = SettingUtils.getAppData().getAttributeList();
      var oSelectedAttributeValue = AttributeProps.getSelectedAttribute();
      var oAttributeMaster = oList[oSelectedAttributeValue.id];
      SettingUtils.makeObjectDirty(oAttributeMaster);
      var oValidator = oAttributeMaster.clonedObject.validator;
      var oMSSProps = AttributeProps.getMSSProps();
      oMSSProps = oMSSProps[sContext];

      if(!CS.includes(oValidator.allowedSiblings, "attribute" + sSplitter + sItemId)) {
        oValidator.allowedSiblings.push("attribute" + sSplitter + sItemId);
      }

      CS.remove(oMSSProps.filteredList, function(oObj){
        return oObj.id == sItemId;
      });

    },

    handleAttributeTypeListItemClicked: function(sTypeId){
      var oList = SettingUtils.getAppData().getAttributeList();
      var oSelectedAttributeValue = AttributeProps.getSelectedAttribute();
      var oAttributeMaster = oList[oSelectedAttributeValue.id];
      SettingUtils.makeObjectDirty(oAttributeMaster);
      var oAttributeMasterClone = oAttributeMaster.clonedObject;
      var aUnitAttributeTypeList = [];
      let oMeasurementMetricAndImperial = new MockAttributeUnits();
      oAttributeMasterClone.type = sTypeId;
      if (!oAttributeMasterClone.validator) {
        var oNewValidator = {};
        if (SettingUtils.isAttributeTypeHtml(sTypeId)) {
          oNewValidator.allowedFrames = [];
          oNewValidator.defaultStyles = [];
          oNewValidator.allowedRTEIcons = [];
          _setMSSPropForAttribute(oAttributeMasterClone);
        }
        oAttributeMasterClone.validator = oNewValidator;
      }

      var aAttributeTypes = SettingUtils.getAppData().getAttributeTypes();
      var oAttributeType = CS.find(aAttributeTypes, {value: sTypeId});
      oAttributeMasterClone.defaultValue = "";

      if(oAttributeType.isUnitAttribute) {
        oAttributeMasterClone.defaultUnit = '';
        if(!SettingUtils.isMeasurementAttributeTypeCustom(sTypeId)){
          var aDefaultUnits = oMeasurementMetricAndImperial[sTypeId];
          oAttributeMasterClone.defaultUnit = aDefaultUnits[0].unit;
        }
        oAttributeMasterClone.precision = MetricCheckConstants.DEFAULT_PRECISION;
      } else if(AttributeUtils.isAttributeTypeNumber(oAttributeType.value)) { //oAttributeType.value contains BaseType
        oAttributeMasterClone.precision = MetricCheckConstants.DEFAULT_PRECISION;
      } else if (AttributeUtils.isAttributeTypeCalculated(oAttributeType.value)) {
        oAttributeMasterClone.attributeOperatorList = [];
      } else if (AttributeUtils.isAttributeTypeConcatenated(oAttributeType.value)) {
        oAttributeMasterClone.attributeConcatenatedList = [];
      } else if (oAttributeType.value == AttributeDictionary.CUSTOM_MEASUREMENT) {
        aUnitAttributeTypeList = _getUnitAttributeTypeList();
        oAttributeMasterClone.type = aUnitAttributeTypeList[0];
        var aDefaultUnits = oMeasurementMetricAndImperial[oAttributeMasterClone.type];
        oAttributeMasterClone.defaultUnit = aDefaultUnits[0].unit;
        oAttributeMasterClone.precision = MetricCheckConstants.DEFAULT_PRECISION;
      } else {
        delete oAttributeMasterClone.precision;
      }
    },

    handleCalculatedAttributeUnitListItemClicked: function (sUnitId) {
      var oList = SettingUtils.getAppData().getAttributeList();
      var oSelectedAttributeValue = AttributeProps.getSelectedAttribute();
      var oAttributeMaster = oList[oSelectedAttributeValue.id];
      SettingUtils.makeObjectDirty(oAttributeMaster);
      var oAttributeMasterClone = oAttributeMaster.clonedObject;
      let oMeasurementMetricAndImperial = new MockAttributeUnits();
      var aAttributeUnits = oMeasurementMetricAndImperial[oAttributeMasterClone.calculatedAttributeType];
      CS.each(aAttributeUnits, function (oAttributeUnit) {
        if(oAttributeUnit.id == sUnitId) {
          oAttributeMasterClone.calculatedAttributeUnit = oAttributeUnit.unit;
          oAttributeMasterClone.attributeOperatorList = [];
          return;
        }
      });

    },

    handleCalculatedAttributeTypeListItemClicked: function (sTypeId) {
      var oList = SettingUtils.getAppData().getAttributeList();
      var oSelectedAttributeValue = AttributeProps.getSelectedAttribute();
      var oAttributeMaster = oList[oSelectedAttributeValue.id];
      SettingUtils.makeObjectDirty(oAttributeMaster);
      var oAttributeMasterClone = oAttributeMaster.clonedObject;
      let oMeasurementMetricAndImperial = new MockAttributeUnits();

      oAttributeMasterClone.calculatedAttributeUnit = null;
      oAttributeMasterClone.calculatedAttributeUnitAsHTML = null;

      if (!SettingUtils.isMeasurementAttributeTypeCustom(sTypeId)) {
        var aDefaultUnits = oMeasurementMetricAndImperial[sTypeId];
        oAttributeMasterClone.calculatedAttributeUnit = aDefaultUnits[0].unit;
      }

      oAttributeMasterClone.calculatedAttributeType = sTypeId;
      oAttributeMasterClone.attributeOperatorList = [];

    },

    handleDefaultUnitListItemClicked: function(sUnitId){
      var oList = SettingUtils.getAppData().getAttributeList();
      var oSelectedAttributeValue = AttributeProps.getSelectedAttribute();
      var oAttributeMaster = oList[oSelectedAttributeValue.id];
      SettingUtils.makeObjectDirty(oAttributeMaster);
      var oAttributeClone = oAttributeMaster.clonedObject;
      var sPreviousUnit = oAttributeClone.defaultUnit;
      let oMeasurementMetricAndImperial = new MockAttributeUnits();
      var aAttributeUnits = oMeasurementMetricAndImperial[oAttributeClone.type];
      CS.each(aAttributeUnits, function (oAttributeUnit) {
        if(oAttributeUnit.id == sUnitId) {
          oAttributeClone.defaultUnit = oAttributeUnit.unit;
          return;
        }
      });

    },

    handleAllowedRTEIconsMultiSelectListItemClicked: function (sContext, aItemIds) {
      var oList = SettingUtils.getAppData().getAttributeList();
      var oSelectedAttributeValue = AttributeProps.getSelectedAttribute();
      var oAttributeMaster = oList[oSelectedAttributeValue.id];
      SettingUtils.makeObjectDirty(oAttributeMaster);
      var oValidator = oAttributeMaster.clonedObject.validator;
      var oMSSProps = AttributeProps.getMSSProps();
      oMSSProps = oMSSProps[sContext];

      CS.forEach(aItemIds, function (sItemId) {
        if(!CS.includes(oValidator.allowedRTEIcons,  sItemId)) {
          oValidator.allowedRTEIcons.push(sItemId);
        }

        CS.remove(oMSSProps.filteredList, function(oObj){
          return oObj.id == sItemId;
        });
      });


    },

    handleAllowedDefaultStylesMultiSelectListItemClicked: function (sContext, aItemIds) {
      var oList = SettingUtils.getAppData().getAttributeList();
      var oSelectedAttributeValue = AttributeProps.getSelectedAttribute();
      var oAttributeMaster = oList[oSelectedAttributeValue.id];
      SettingUtils.makeObjectDirty(oAttributeMaster);
      var oValidator = oAttributeMaster.clonedObject.validator;
      var oMSSProps = AttributeProps.getMSSProps();
      oMSSProps = oMSSProps[sContext];

      CS.forEach(aItemIds, function (sItemId) {
        if(!CS.includes(oValidator.defaultStyles, sItemId)) {
          oValidator.defaultStyles.push(sItemId);
        }

        CS.remove(oMSSProps.filteredList, function(oObj){
          return oObj.id == sItemId;
        });
      });

    },

    handleChildAttributesMultiSelectListItemClicked: function (sContext, sItemId) {
      var oList = SettingUtils.getAppData().getAttributeList();
      var oSelectedAttributeValue = AttributeProps.getSelectedAttribute();
      var oAttributeMaster = oList[oSelectedAttributeValue.id];
      SettingUtils.makeObjectDirty(oAttributeMaster);
      oAttributeMaster = oAttributeMaster.clonedObject;
      if (!oAttributeMaster.childAttributes) {
        oAttributeMaster.childAttributes = [];
      }
      if(!CS.includes(oAttributeMaster.childAttributes, sItemId)) {
        oAttributeMaster.childAttributes.push(sItemId);
      }

    },

    getAttributeDetailsById: function (_sSelectedAttributeId, oCallbackData) {
      var sSelectedAttributeId = _sSelectedAttributeId || _getSelectedAttribute().id;
      _getAttributeDetailsById(sSelectedAttributeId, oCallbackData);
    },

    getCalculatedAttributeMapping: function () {
      _getCalculatedAttributeMapping();
    },

    handleAttributeConfigDialogButtonClicked: function (sButtonId) {
      _handleAttributeConfigDialogButtonClicked(sButtonId);
    },

    handleExportAttribute: function (oSelectiveExportDetails) {
      _handleExportAttribute(oSelectiveExportDetails);
    },

    handleAttributeFileUploaded: function (aFiles,oImportExcel) {
      _handleAttributeFileUploaded(aFiles,oImportExcel);
    },
  }
})();

MicroEvent.mixin(AttributeStore);

export default AttributeStore;
