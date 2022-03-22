
import CS from '../../../../../../libraries/cs';

import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import ContentUtils from './content-utils';
import GlobalStore from './../../../../store/global-store';
import UniqueIdentifierGenerator from './../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import RequestMapping from '../../../../../../libraries/requestmappingparser/request-mapping-parser.js';
import ScreenModeUtils from './screen-mode-utils';
import assetTypes from '../../tack/coverflow-asset-type-list';
import PortalTypeDictionary from '../../../../../../commonmodule/tack/portal-type-dictionary';
import ModuleTypeDictionary from '../../../../../../commonmodule/tack/module-dictionary';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import ExceptionLogger from '../../../../../../libraries/exceptionhandling/exception-logger';
import oFilterPropType from "../../tack/filter-prop-type-constants";
import FilterStoreFactory from "./filter-store-factory";
import CommonUtils from "../../../../../../commonmodule/util/common-utils";
import ContentScreenProps from "../model/content-screen-props";
import SessionProps from "../../../../../../commonmodule/props/session-props";
import PhysicalCatalogDictionary from "../../../../../../commonmodule/tack/physical-catalog-dictionary";
import ModuleDictionaryExport from '../../../../../../commonmodule/tack/module-dictionary-export';
import SessionStorageManager
  from "../../../../../../libraries/sessionstoragemanager/session-storage-manager";
import SessionStorageConstants from "../../../../../../commonmodule/tack/session-storage-constants";
var getTranslation = ScreenModeUtils.getTranslationDictionary;
var getRequestMapping = ScreenModeUtils.getScreenRequestMapping;
const OnBoardingImportTypes = assetTypes.onBoardingImportTypes;

var OnboardingStore = (function () {

  var _triggerChange = function () {
    OnboardingStore.trigger('onboarding-change');
  };

  //************************************* Private API's **********************************************//

  var _getDefaultUnmappedObject = function() {
    return {
      id: "",
      isIgnored: false,
      mappedElementId: null,
      isAutomapped: false,
      columnNames :[],
    };
  };

  var _preProcessEndpointForUnmappedElements = function (oEndPoint) {
    var oComponentProps = ContentUtils.getComponentProps();
    var oEndPointMappingViewProps = oComponentProps.endPointMappingViewProps;
    var oUnmappedElementValuesList = oEndPointMappingViewProps.getUnmappedElementValuesList();
    var aUnmappedColumns = oEndPoint.unmappedColumns;
    var aUnmappedKlassColumns = oEndPoint.unmappedKlassColumns;
    var aUnmappedTaxonomiesColumns = oEndPoint.unmappedTaxonomyColumns;
    var oUnmappedColumnValues = {};
    var oUnmappedKlassColumnValues = {};
    var oUnmappedTaxonomyColumnValues = {};

    CS.forEach(aUnmappedColumns, function (sColumnId) {
        oUnmappedColumnValues[sColumnId] = _getDefaultUnmappedObject();
    });

    CS.forEach(aUnmappedKlassColumns, function (sColumnId) {
      oUnmappedKlassColumnValues[sColumnId] = _getDefaultUnmappedObject();
    });

    CS.forEach(aUnmappedTaxonomiesColumns, function (sColumnId) {
      oUnmappedTaxonomyColumnValues[sColumnId] = _getDefaultUnmappedObject();
    });

    oUnmappedElementValuesList = {
      unmappedColumns: oUnmappedColumnValues,
      unmappedKlassColumns: oUnmappedKlassColumnValues,
      unmappedTaxonomyColumns: oUnmappedTaxonomyColumnValues
    };

    oEndPointMappingViewProps.setUnmappedElementValuesList(oUnmappedElementValuesList);
  };

  let _setReferencedDataForMapping = (oConfigDetails) => {
    if (CS.isEmpty(oConfigDetails)) {
      return;
    }

    let oComponentProps = ContentUtils.getComponentProps();
    let oConfigData = oComponentProps.endPointMappingViewProps.getConfigData();

    oConfigData.referencedAttributes = oConfigDetails.attributes;
    oConfigData.referencedTags = oConfigDetails.tags;
    oConfigData.referencedKlasses = oConfigDetails.klasses;
    oConfigData.referencedTaxonomies = oConfigDetails.taxonomy;
  };

  let _setPropertyRowTypeData = (oActiveEndpoint) => {
    let oComponentProps = ContentUtils.getComponentProps();
    let oPropertyRowTypeData = {};

    CS.forEach(oActiveEndpoint.attributeMappings, function(oAttributeMapping){
      let sColumnName = oAttributeMapping.columnNames[0];
      oPropertyRowTypeData[sColumnName] = "attribute";
    });

    CS.forEach(oActiveEndpoint.tagMappings, function(oTagMapping){
      let sColumnName = oTagMapping.columnNames[0];
      oPropertyRowTypeData[sColumnName] = "tag";
    });

    oComponentProps.endPointMappingViewProps.setPropertyRowTypeData(oPropertyRowTypeData);
  };

  let _checkIsTagValuesMapped = function (oTagMapping) {
    let bIsTagValuesMapped = false;
    /**It is expected that there should be only one tagValueMapping in tagValueMappings array
     * Need to change impl if number of tagValueMapping increases
     **/
    let oTagValueMapping = CS.get(oTagMapping, 'tagValueMappings[0]') || {};
    let aMappedTagValueIds = [];
    let aUnMappedTagValueIds = [];
    let iUnmappedTagValuesCount = 0;
    CS.forEach(oTagValueMapping.mappings, function (oTag) {
      if (CS.isEmpty(oTag.mappedTagValueId)) {
        iUnmappedTagValuesCount += 1;
        aUnMappedTagValueIds.push(oTag.id);
      } else {
        aMappedTagValueIds.push(oTag.id);
      }

    });
    if (CS.isEmpty(oTagValueMapping.mappings)) {
      bIsTagValuesMapped = true;
    } else if (iUnmappedTagValuesCount === 0) {
      bIsTagValuesMapped = true;
    } else if (iUnmappedTagValuesCount <= oTagValueMapping.mappings.length) {
      bIsTagValuesMapped = false;
    }
    return {
      mappedTagValueIds: aMappedTagValueIds,
      unMappedTagValueIds: aUnMappedTagValueIds,
      isTagValuesMapped: bIsTagValuesMapped
    };
  };

  var createRowProps = function (oResponse) {
    let rowViewPropsObj = {};
    let aAttributeMappings = oResponse.attributeMappings;
    let aTagMappings = oResponse.tagMappings;
    let aClassMappings = oResponse.classMappings;
    let aTaxonomyMappings = oResponse.taxonomyMappings;
    let aUnmappedColumns = oResponse.unmappedColumns;
    let aUnmappedKlassColumns = oResponse.unmappedKlassColumns;
    let aUnmappedTaxonomyColumns= oResponse.unmappedTaxonomyColumns;

    /** For mapped elements*/
    let aTemp = aAttributeMappings.concat(aTagMappings).concat(aTaxonomyMappings).concat(aClassMappings);
    CS.forEach(aTemp, function (oEntityMapping) {
      let smallObj = {};
      smallObj["isIgnored"] = oEntityMapping.isIgnored;
      if (!CS.isEmpty(oEntityMapping.mappedElementId)) {
        smallObj["isMapped"] = true;
      } else {
        smallObj["isMapped"] = false;
      }
      if (oEntityMapping.hasOwnProperty("tagValueMappings")) {
        let oTagValuesObject = _checkIsTagValuesMapped(oEntityMapping);
        smallObj["mappedTagValueIds"] = oTagValuesObject.mappedTagValueIds;
        smallObj["unMappedTagValueIds"] = oTagValuesObject.unMappedTagValueIds;
        smallObj["isTagValuesMapped"] = oTagValuesObject.isTagValuesMapped;
      }
      rowViewPropsObj[oEntityMapping.columnNames] = smallObj;
    });

    /** For unmapped elements*/
    let aUnmappedTemp = aUnmappedColumns.concat(aUnmappedKlassColumns).concat(aUnmappedTaxonomyColumns);
    CS.forEach(aUnmappedTemp, function (sUnmappedColumn) {
      rowViewPropsObj[sUnmappedColumn] = {
        isIgnored: false,
        isMapped: false
      };
    });

    let oComponentProps = ContentUtils.getComponentProps();
    let oEndPointMappingViewProps = oComponentProps.endPointMappingViewProps;
    oEndPointMappingViewProps.setMappingFilterProps(rowViewPropsObj);
  };

  var successUploadOnBoardingBulkImport = function (oCallbackData, oResponse) {
    oResponse = oResponse.success;
    var oComponentProps = ContentUtils.getComponentProps();
    var oEndPointMappingViewProps = oComponentProps.endPointMappingViewProps;
    oEndPointMappingViewProps.setCurrentlyUploadedFileIds([]); //Set Id which you get in response

    oEndPointMappingViewProps.setActiveEndpoint(oResponse);
    _setReferencedDataForMapping(oResponse.configDetails);
    _preProcessEndpointForUnmappedElements(oResponse);
    _setPropertyRowTypeData(oResponse);
    if(oResponse.isRuntimeMappingEnabled || oResponse.isShowMapping){
      oComponentProps.screen.setOnboardingFileMappingViewVisibilityStatus(true);
      createRowProps(oResponse);
      alertify.success("File uploaded successfully. Article creation in progress.");
      if (oCallbackData && oCallbackData.functionToExecute) {
        oCallbackData.functionToExecute();
      }
      _triggerChange();
    }else{
      oComponentProps.screen.setOnboardingFileMappingViewVisibilityStatus(false);
      _saveEndpoint(oCallbackData);
    }
  };

  var failureUploadOnBoardingBulkImport = function (oCallback, oResponse) {
    let oDevException = oResponse.failure.devExceptionDetails;
    if(oDevException[0].key === "FileFormatNotSupportedException"){
      alertify.error(getTranslation().FILE_FORMAT_NOT_SUPPORTED);
    }else{
      ContentUtils.failureCallback(oResponse, 'failureUploadOnBoardingBulkImport', getTranslation());
    }

    var oComponentProps = ContentUtils.getComponentProps();
    var oEndPointMappingViewProps = oComponentProps.endPointMappingViewProps;
    oEndPointMappingViewProps.setCurrentlyUploadedFileIds([]); //Set Id which you get in response
    if (oCallback && oCallback.functionToExecute) {
      oCallback.functionToExecute(true);
    }
    _triggerChange();
  };

  var successExportToMDMCallback = function () {
    ContentUtils.showMessage(getTranslation().EXPORT_IS_INPROGRESS);
  };

  var failureExportToMDMCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureExportToMDMCallback', getTranslation());
    let aExceptionDetails = oResponse.failure.exceptionDetails;
    CS.forEach(aExceptionDetails, function (oData) {
      let sKey = oData.key;
      if (sKey === "UserNotHaveExportPermission") {
        let oScreenProps = ContentScreenProps.screen;
        let oFunctionalPermission = oScreenProps.getFunctionalPermission();
        oFunctionalPermission.canExport = false;
        _triggerChange();
      }
    });
  };

  /******************************* Private ****************************/
  var uploadOnBoardingBulkImport = function (data, oCallbackData) {
    CS.customPostRequest(RequestMapping.getRequestUrl(getRequestMapping().OnboardingFileUpload), data,
        successUploadOnBoardingBulkImport.bind(this, oCallbackData), failureUploadOnBoardingBulkImport.bind(this, oCallbackData), false);
  };

  var _handleExportToMDMButtonClicked = function () {
    var aSelectedContents = ContentUtils.getSelectedContentIds() || [];
    var sSelectedModuleId = ContentUtils.getSelectedModuleId();

    var oGlobalModulesData = GlobalStore.getGlobalModulesData();

    if(!CS.isEmpty(aSelectedContents)) {
      if (oGlobalModulesData.type == PortalTypeDictionary.ONBOARDING) {
          if(oGlobalModulesData.forCentralStaging) {
            CS.postRequest(getRequestMapping().ExportToCentral, {}, {ids: aSelectedContents}, successExportToMDMCallback, failureExportToMDMCallback);
          }
          else {
            CS.postRequest(getRequestMapping().ExportToMDM, {}, {ids: aSelectedContents}, successExportToMDMCallback, failureExportToMDMCallback);
          }
      } else if(oGlobalModulesData.type == PortalTypeDictionary.MAIN_PORTAL) {
        var sModuleId = GlobalStore.getSelectedModule().id;
        CS.postRequest(getRequestMapping().ExportToOffboardStaging, {}, {ids: aSelectedContents, module: sModuleId}, successExportToMDMCallback, failureExportToMDMCallback);
      }

    }

    if (oGlobalModulesData.type == PortalTypeDictionary.OFFBOARDING_PORTAL) {
      if (sSelectedModuleId == ModuleTypeDictionary.FILES) {
        aSelectedContents = [];
      }
      CS.postRequest(getRequestMapping().ExportToFile, {}, {ids: aSelectedContents}, successExportToMDMCallback, failureExportToMDMCallback);
    }
  };

  var _handleExportEntityButtonClicked = function (oSelectiveExport) {
    let sSelectedDataLanguage = ContentUtils.getSelectedDataLanguage();
    oSelectiveExport.oPostRequest.languageCode = sSelectedDataLanguage;
    let sSelectedModuleId = ContentUtils.getSelectedModuleId();
    let oSearchCriteria = oSelectiveExport.oPostRequest.searchCriteria;
    if (CS.isEmpty(oSearchCriteria.collectionId) && CS.isEmpty(oSearchCriteria.bookmarkId)) {
      oSearchCriteria.selectedBaseTypes.push(ModuleDictionaryExport[sSelectedModuleId]);
    }
    oSelectiveExport.oPostRequest.searchCriteria = oSearchCriteria;
    CS.postRequest(oSelectiveExport.sUrl, {}, oSelectiveExport.oPostRequest, successExportToMDMCallback, failureExportToMDMCallback);
  };

  var _handleImportEntityButtonClicked= function(aFiles,oImportExcel){

    var bIsValidFile = false;
    var bIsInvalidFile = false;
    let oScreenProps = ContentScreenProps.screen;
    oScreenProps.setImportEndpointSelected(false);
    if (aFiles.length) {

      var iFilesInProcessCount = 0;
      var count = 0;

      var data = new FormData();
      CS.forEach(aFiles, function (file, index) {
        if (_getIsValidFileTypes(file)) {
          bIsValidFile = true;
          var reader = new FileReader();
          iFilesInProcessCount++;
          // Closure to capture the file information.
          reader.onload = (function (theFile) {
            return function (event) {
              count += 1;
              let filekey = UniqueIdentifierGenerator.generateUUID();
              data.append(filekey, theFile);
            };
          })(file);

          reader.onloadend = function () {
            iFilesInProcessCount--;
            if (iFilesInProcessCount == 0) {
              uploadFileImport(data, {}, oImportExcel);
            }
          };

          reader.readAsDataURL(file);
        } else {
          bIsInvalidFile = true;
        }
      });
    }
    if (bIsInvalidFile) {
      alertify.error(getTranslation().INVALID_FILE_TYPE);
      ContentUtils.hideLoadingScreen();
    }
    _triggerChange();
  };

  let uploadFileImport = function (data, oCallback, oImportExcel) {
    data.append("entityType",null);
    data.append("importType", "product");
    if (SessionProps.getSessionPhysicalCatalogId() !== PhysicalCatalogDictionary.DATAINTEGRATION) {
      let oScreenProps = ContentScreenProps.screen;
      let sEndpointCode = oScreenProps.getSelectedImportEndpointCode();
      data.append("endpointCode", sEndpointCode);
    }else {
      data.append("endpointCode", null);
    }

    const oDataLanguage = {
      dataLanguage: SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE)
    };

    return CS.customPostRequest(oImportExcel.sUrl, data, successUploadFileImport.bind(this, oCallback), failureUploadFileImport,
      false, undefined, oDataLanguage);
  };

  let successUploadFileImport = function () {
    alertify.success(getTranslation().IMPORT_IN_PROGRESS);
    CommonUtils.refreshCurrentBreadcrumbEntity();
    return true;
  };

  let failureUploadFileImport = function (oResponse) {
    CommonUtils.failureCallback(oResponse, "failureUploadFileImport", getTranslation());
    let aExceptionDetails = oResponse.failure.exceptionDetails;
    CS.forEach(aExceptionDetails, function (oData) {
      let sKey = oData.key;
      if (sKey === "UserNotHaveImportPermission") {
        let oScreenProps = ContentScreenProps.screen;
        let oFunctionalPermission = oScreenProps.getFunctionalPermission();
        oFunctionalPermission.canImport = false;
        _triggerChange();
      }
    });
    return false;
  };

  var successTransferToSupplierStagingCallback = function () {
    ContentUtils.showMessage(getTranslation().TRANSFER_IS_INPROGRESS);
  };

  var failureTransferToSupplierStagingCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureExportToMDMCallback', getTranslation());
  };

  var _handleTransferToSupplierStagingButtonClicked = function () {
    var aSelectedContents = ContentUtils.getSelectedContentIds() || [];
    var sModuleId = GlobalStore.getSelectedModule().id;
    CS.postRequest(getRequestMapping().TransferToSupplierStaging, {}, {ids: aSelectedContents,  module: sModuleId}, successTransferToSupplierStagingCallback, failureTransferToSupplierStagingCallback);
  };

  var _handleExportToExcelButtonClicked = function () {
    let oRequest = {};
    let oComponentProps = ContentUtils.getComponentProps();
    let oCollectionProps = oComponentProps.collectionViewProps;
    let sActiveModuleId = GlobalStore.getSelectedModule().id;
    var oGlobalModulesData = GlobalStore.getGlobalModulesData();

    if (oGlobalModulesData.type == PortalTypeDictionary.ONBOARDING_PORTAL || oGlobalModulesData.type == PortalTypeDictionary.OFFBOARDING_PORTAL) {
      _handleExportToMDMButtonClicked();
    } else {
      var sCollectionId = null;
      var sBookmarkId = null;
      var oActiveCollection = oCollectionProps.getActiveCollection();
      if(oActiveCollection && oActiveCollection.type === "staticCollection") {
        sCollectionId = oActiveCollection.id;
      } else {
        sBookmarkId = oActiveCollection.id;
      }
      let oFilterContext = {
        filterType: oFilterPropType.MODULE,
        screenContext: oFilterPropType.MODULE
      };
      let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
      oRequest.module = sActiveModuleId;
      oRequest.ids = ContentUtils.getSelectedContentIds() || [];
      oRequest.filterResultsToSave = oFilterStore.createFilterPostData();
      oRequest.collectionId = sCollectionId;
      oRequest.bookmarkId = sBookmarkId;
      CS.postRequest(getRequestMapping().ExportToExcel, {}, oRequest, successImportToExcel, failureImportToExcel);
    }
  };

  var _base64ToArrayBuffer = function (base64) {
    var sBinaryString = window.atob(base64);
    var iBinaryLen = sBinaryString.length;
    var aBytes = new Uint8Array(iBinaryLen);
    for (var i = 0; i < iBinaryLen; i++) {
      var iAscii = sBinaryString.charCodeAt(i);
      aBytes[i] = iAscii;
    }
    return aBytes;
  };

  var successImportToExcel = function (oResponse) {
    var aBuffer = _base64ToArrayBuffer(oResponse.success.fileStream);
    var oTempElement = window.document.createElement('a');
    document.body.appendChild(oTempElement);
    var oBlob = new Blob([aBuffer], {type: "octet/stream"});
    oTempElement.href = window.URL.createObjectURL(oBlob);
    oTempElement.download = (getTranslation().PRODUCTS_LIST) + " - " + (new Date().getTime()) + ".xlsx";
    oTempElement.click();
    document.body.removeChild(oTempElement);
  };

  var failureImportToExcel = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureImportToExcel', getTranslation());
  };

  var _getIsValidFileTypes = function (oFile) {
    var sTypeRegex = OnBoardingImportTypes.map(function (sType) {
      return '\\' + sType + '$';
    }).join('|');
    return oFile.name.match(new RegExp(sTypeRegex, 'i'));
  };

  var _handleOnboardingFileUploaded = function (aFiles, oCallback) {
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
              uploadOnBoardingBulkImport(data, oCallback);
            }
          };

          reader.readAsDataURL(file);
        } else {
          bIsAnyInvalidImage = true;
        }
      });

    }

    if (bIsAnyInvalidImage) {
      alertify.error(getTranslation().ERROR_INVALID_IMAGE, 0);
      ContentUtils.hideLoadingScreen();
    }

    if (!bIsAnyValidImage) {
      ContentUtils.hideLoadingScreen();
    }
  };

  let _updateReferencedDataForMapping = (sSummaryType, oReferencedData) => {
    let oComponentProps = ContentUtils.getComponentProps();
    let oConfigData = oComponentProps.endPointMappingViewProps.getConfigData();
    switch (sSummaryType) {
      case "attributes":
        CS.assign(oConfigData.referencedAttributes, oReferencedData);
        break;

      case "tags":
        CS.assign(oConfigData.referencedTags, oReferencedData);
        break;

      case "taxonomies":
        CS.assign(oConfigData.referencedTaxonomies, oReferencedData);
        break;

      case "classes":
        CS.assign(oConfigData.referencedKlasses, oReferencedData);
        break;
    }
  };

  let _addToUnmappedMappings = (sId, sMappedElement, sOptionType, sAddMappingKey) => {
    let oComponentProps = ContentUtils.getComponentProps();
    let oEndpointMappingViewProps = oComponentProps.endPointMappingViewProps;
    let oActiveEndpoint = oEndpointMappingViewProps.getActiveEndpoint();
    let sRemoveMappingKey = "";
    let sRowType = "";

    switch (sOptionType) {
      case "attributes":
        sRemoveMappingKey = "tagMappings";
        sRowType = "attribute";
        break;

      case "tags":
        sRemoveMappingKey = "attributeMappings";
        sRowType = "tag";
        break;
    }

    let aRemoveFromRows = {};
    if (!CS.isEmpty(sRemoveMappingKey)) {
      aRemoveFromRows = CS.remove(oActiveEndpoint[sRemoveMappingKey], {id: sId});
    }

    let oRowPropertyData = oEndpointMappingViewProps.getPropertyRowTypeData();
    let oRemovedRow = CS.find(aRemoveFromRows, {id: sId});
    let aRows = oActiveEndpoint[sAddMappingKey];
    CS.forEach(oRemovedRow.columnNames, function (sColumnId) {
      aRows.push(sColumnId);
      oRowPropertyData[sColumnId] = sRowType;
    });

    _preProcessEndpointForUnmappedElements(oActiveEndpoint)

    _triggerChange();
  };

  let _addToMappedMappings = (sId, sMappedElement, sOptionType, oReferencedData, sRemoveMappingKey, sAddMappingKey) => {
    let oComponentProps = ContentUtils.getComponentProps();
    let oEndpointMappingViewProps = oComponentProps.endPointMappingViewProps;
    let oActiveEndpoint = oEndpointMappingViewProps.getActiveEndpoint();
    let aRemoveFromRows = oActiveEndpoint[sRemoveMappingKey];

    let iIndex = aRemoveFromRows.indexOf(sId);
    if (iIndex != -1) {
      aRemoveFromRows.splice(iIndex, 1);
    }

    let aRows = oActiveEndpoint[sAddMappingKey];
    let oRow = CS.find(aRows, {id: sId});
    let sColumnName = oRow.columnNames[0];

    let oRowTypeData = oEndpointMappingViewProps.getPropertyRowTypeData();
    switch (sOptionType){
      case "attributes":
        oRowTypeData[sColumnName] = "attribute";
        break;

      case "tags":
        oRowTypeData[sColumnName] = "tag";
        break;
    }

    //TODO: Hack for sending modified elements in added and deleted instead of modified
    if(oRow){
      oRow.id = UniqueIdentifierGenerator.generateUUID();
    }

    if (!oRow && (sOptionType == "tags" || sOptionType == "attributes")) {
      let sTempAddMappingKey = sOptionType == "tags" ? "attributeMappings" : "tagMappings";
      aRows = oActiveEndpoint[sTempAddMappingKey];
      oRow = CS.find(aRows, {id: sId});

      if(!CS.isEmpty(oRow)){
        if(sAddMappingKey === "attributeMappings"){
          let aRemoveFromTagRows = oActiveEndpoint["tagMappings"];
          CS.remove(aRemoveFromTagRows, {id: sId});

          oRow.id = UniqueIdentifierGenerator.generateUUID();
          oActiveEndpoint["attributeMappings"].push(oRow);
        }else {
          oRow.id = UniqueIdentifierGenerator.generateUUID();
        }
      }
    }

    if(!oRow){
      oRow = {};
      oRow.id = UniqueIdentifierGenerator.generateUUID();
    }

    oRow.tagValueMappings = [];
    oRow.mappedElementId = sMappedElement;

    _updateReferencedDataForMapping(sOptionType, oReferencedData);

    if (sOptionType === "tags") {
      _checkIfTagAndFetchTagColumnNamesFromFile(sMappedElement, oRow.columnNames && oRow.columnNames[0], oRow.id);
    }
    _triggerChange();
  };

  var _handleEndpointMappedElementChanged = function (sId, sMappedElement, sOptionType, oReferencedData) {
    let sAddMappingKey = "";
    let sRemoveMappingKey = "";

    switch (sOptionType) {
      case "attributes":
        sAddMappingKey = "attributeMappings";
        sRemoveMappingKey = "unmappedColumns";
        break;

      case "tags":
        sAddMappingKey = "tagMappings";
        sRemoveMappingKey = "unmappedColumns";
        break;

      case "taxonomies":
        sAddMappingKey = "taxonomyMappings";
        sRemoveMappingKey = "unmappedTaxonomyColumns";
        break;

      case "classes":
        sAddMappingKey = "classMappings";
        sRemoveMappingKey = "unmappedKlassColumns";
        break;
    }

    if(!CS.isEmpty(sMappedElement)){
      _addToMappedMappings(sId, sMappedElement, sOptionType, oReferencedData, sRemoveMappingKey, sAddMappingKey);
    } else if(CS.isEmpty(sMappedElement) && (sOptionType === "attributes" || sOptionType === "tags")){
      _addToUnmappedMappings(sId, sMappedElement, sOptionType, sRemoveMappingKey);
    }
  };

  var _handleProfileUnmappedElementChanged = function (sName, sMappedElement, sOptionType, oReferencedData) {
    let oComponentProps = ContentUtils.getComponentProps();
    let oEndpointMappingViewProps = oComponentProps.endPointMappingViewProps;
    let oRowTypeData = oEndpointMappingViewProps.getPropertyRowTypeData();

    if(CS.isEmpty(sMappedElement)){

      switch (sOptionType) {
        case "attributes":
          oRowTypeData[sName] = "attribute";
          break;

        case "tags":
          oRowTypeData[sName] = "tag";
          break;
      }

      _triggerChange();
      return;
    }

    let oActiveEndpoint = oEndpointMappingViewProps.getActiveEndpoint();
    let sNewId = UniqueIdentifierGenerator.generateUUID();
    let sMappingKeyToPush = "";
    let sMappingKeyToRemove = "";

    switch (sOptionType) {
      case "attributes":
        sMappingKeyToPush = "attributeMappings";
        sMappingKeyToRemove = "unmappedColumns";
        oRowTypeData[sName] = "attribute";
        break;

      case "tags":
        sMappingKeyToPush = "tagMappings";
        sMappingKeyToRemove = "unmappedColumns";
        oRowTypeData[sName] = "tag";
        break;

      case "taxonomies":
        sMappingKeyToPush = "taxonomyMappings";
        sMappingKeyToRemove = "unmappedTaxonomyColumns";
        break;

      case "classes":
        sMappingKeyToPush = "classMappings";
        sMappingKeyToRemove = "unmappedKlassColumns";
        break;
    }


    if(!CS.isEmpty(sMappingKeyToPush)){
      oActiveEndpoint[sMappingKeyToPush].push(
          {
            id: sNewId,
            columnNames: [sName],
            mappedElementId: sMappedElement,
            isIgnored: false,
            tagValueMappings: []
          }
      );
    }

    if(!CS.isEmpty(sMappingKeyToRemove)){
      oActiveEndpoint[sMappingKeyToRemove] && CS.remove(oActiveEndpoint[sMappingKeyToRemove], function (sColumn) {
        return sColumn === sName;
      });
    }

    _updateReferencedDataForMapping(sOptionType, oReferencedData);

    if (sOptionType === "tags") {
      _checkIfTagAndFetchTagColumnNamesFromFile(sMappedElement, sName, sNewId);
    }
    _triggerChange();
  };

  var _handleEndpointUnmappedElementIsIgnoredToggled = function (sId, sTabId) {
    var oComponentProps = ContentUtils.getComponentProps();
    var oEndPointMappingViewProps = oComponentProps.endPointMappingViewProps;
    var oActiveEndpoint = oEndPointMappingViewProps.getActiveEndpoint();
    var sNewId = UniqueIdentifierGenerator.generateUUID();
    var oUnmappedColumnValuesList = oEndPointMappingViewProps.getUnmappedElementValuesList();
    var oUnmappedColumn = {};
    var sMappingKeyToPush = "";
    var sMappingKeyToRemove = "";

    switch (sTabId) {
      case "properties":
        oUnmappedColumn = oUnmappedColumnValuesList["unmappedColumns"];
        sMappingKeyToPush = "attributeMappings";
        sMappingKeyToRemove = "unmappedColumns";
        break;

      case "classes":
        oUnmappedColumn = oUnmappedColumnValuesList["unmappedKlassColumns"];
        sMappingKeyToPush = "classMappings";
        sMappingKeyToRemove = "unmappedKlassColumns";
        break;

      case "taxonomies":
        oUnmappedColumn = oUnmappedColumnValuesList["unmappedTaxonomyColumns"];
        sMappingKeyToPush = "taxonomyMappings";
        sMappingKeyToRemove = "unmappedTaxonomyColumns";
        break;
    }

    oUnmappedColumn[sId].isIgnored = !oUnmappedColumn[sId].isIgnored;
    oUnmappedColumn[sId].id = sNewId;
    oUnmappedColumn[sId].columnNames = [sId];

    oActiveEndpoint[sMappingKeyToPush].push(oUnmappedColumn[sId]);
    oActiveEndpoint[sMappingKeyToRemove] && CS.remove(oActiveEndpoint[sMappingKeyToRemove], function (sColumn) {
      return sColumn === sId;
    });

    _triggerChange();
  };

  var _checkIfTagAndFetchTagColumnNamesFromFile = function (sMappedElement, sColumnName, sId ) {
    var oComponentProps = ContentUtils.getComponentProps();
    var oEndpointMappingViewProps = oComponentProps.endPointMappingViewProps;
    var oActiveEndpoint = oEndpointMappingViewProps.getActiveEndpoint();
    var oPostBody = {};

    oPostBody.fileId = oActiveEndpoint.fileInstanceId;
    oPostBody.columnName = sColumnName;
    oPostBody.tagGroupId = sMappedElement;
    CS.postRequest(getRequestMapping().TagValuesFromColumn, {}, oPostBody, successFetchTagValues.bind(this, sId, sColumnName), failureSaveEndpoint);
  };

  var successFetchTagValues = function (sId, sColumnName, oResponse) {
    oResponse = oResponse.success;
    var oComponentProps = ContentUtils.getComponentProps();
    var oEndpointMappingViewProps = oComponentProps.endPointMappingViewProps;
    var oActiveEndpoint = oEndpointMappingViewProps.getActiveEndpoint();
    var oMapping = CS.find(oActiveEndpoint.tagMappings, {id: sId});
    if(!oMapping){
      oMapping = CS.find(oActiveEndpoint.attributeMappings, {id: sId});
      CS.remove(oActiveEndpoint.attributeMappings, {id: sId});
      oActiveEndpoint.tagMappings.push(oMapping);
    }
    if (oMapping) {
      oMapping.tagValueMappings = [{
        columnName: sColumnName,
        mappings: CS.map(oResponse.mappings || [], function (oTagValue) {
          return {
            id: oTagValue.id,
            isIgnoreCase: oTagValue.isIgnoreCase,
            mappedTagValueId: oTagValue.mappedTagValueId,
            tagValue: oTagValue.tagValue
          };
        })
      }];
    }
    _triggerChange();
  };

  let updateMappedUnmappedTagValuesId = function (sName, sTagValueId, sTagValueIdToReplaceWith) {
    var oComponentProps = ContentUtils.getComponentProps();
    var oEndpointMappingViewProps = oComponentProps.endPointMappingViewProps;
    var oMappingFilterProps = oEndpointMappingViewProps.getMappingFilterProps();
    let oTagMapping = oMappingFilterProps[sName];
    let iMappedIndex = CS.indexOf(oTagMapping.mappedTagValueIds, sTagValueId);
    let iUnmappedIndex = CS.indexOf(oTagMapping.unMappedTagValueIds, sTagValueId);
    if(iMappedIndex !== -1) {
      oTagMapping.mappedTagValueIds[iMappedIndex] = sTagValueIdToReplaceWith;
    } else if (iUnmappedIndex !== -1){
      oTagMapping.unMappedTagValueIds[iUnmappedIndex] = sTagValueIdToReplaceWith;
    }
  };

  var _handleProfileConfigMappedTagValueChanged = function (sId, sMappedTagValueId, sNewValue, sTagGroupId, oReferencedTagValues) {
    var oComponentProps = ContentUtils.getComponentProps();
    var oEndpointMappingViewProps = oComponentProps.endPointMappingViewProps;
    var oActiveEndpoint = oEndpointMappingViewProps.getActiveEndpoint();
    var oMapping = CS.find(oActiveEndpoint.tagMappings, {id: sId}) || {};
    var oTagValueMapping = oMapping.tagValueMappings && oMapping.tagValueMappings[0] || {};
    var aMappings = oTagValueMapping.mappings || [];
    var oSelectedMapping = CS.find(aMappings, {id: sMappedTagValueId});
    oSelectedMapping.mappedTagValueId = sNewValue;
    oSelectedMapping.id = UniqueIdentifierGenerator.generateUUID();
    updateMappedUnmappedTagValuesId(oTagValueMapping.columnName, sMappedTagValueId, oSelectedMapping.id);

    try {
      let oConfigData = oEndpointMappingViewProps.getConfigData();
      let oReferencedTag = oConfigData.referencedTags[sTagGroupId];

      if(CS.isEmpty(oReferencedTag.children)){
        oReferencedTag.children = [];
      }

      CS.forEach(oReferencedTagValues, function (oTagValue) {
        oReferencedTag.children.push(oTagValue);
      });
    } catch (oException) {
      ExceptionLogger.error(oException);
    }

    _triggerChange();
  };

  var _handleEndpointTagValueIgnoreCaseToggled = function (sId, sMappedTagValueId) {
    var oComponentProps = ContentUtils.getComponentProps();
    var oEndpointMappingViewProps = oComponentProps.endPointMappingViewProps;
    var oActiveEndpoint = oEndpointMappingViewProps.getActiveEndpoint();
    var oMapping = CS.find(oActiveEndpoint.tagMappings, {id: sId}) || {};
    var oTagValueMapping = oMapping.tagValueMappings && oMapping.tagValueMappings[0] || {};
    var aMappings = oTagValueMapping.mappings || [];
    var oSelectedMapping = CS.find(aMappings, {id: sMappedTagValueId});
    oSelectedMapping.isIgnoreCase = !oSelectedMapping.isIgnoreCase;
    _triggerChange();
  };

  var _handleEndpointIsIgnoredToggled = function (sId, sTabId) {
    var oComponentProps = ContentUtils.getComponentProps();
    var oEndPointMappingViewProps = oComponentProps.endPointMappingViewProps;
    var oActiveEndpoint = oEndPointMappingViewProps.getActiveEndpoint();
    var aRows = [];

    switch (sTabId) {
      case "properties":
        aRows = oActiveEndpoint.attributeMappings.concat(oActiveEndpoint.tagMappings);
        break;

      case "classes":
        aRows = oActiveEndpoint.classMappings;
        break;

      case "taxonomies":
        aRows = oActiveEndpoint.taxonomyMappings;
        break;
    }

    var oElement = CS.find(aRows, {id: sId}) || {};
    oElement.isIgnored = !oElement.isIgnored;
    _triggerChange();
  };

  var _handleEndPointMappingViewBackButtonClicked = function (oCallbackData) {
    var fOkButtonHandler = function () {
      var oComponentProps = ContentUtils.getComponentProps();
      var oEndPointMappingViewProps = oComponentProps.endPointMappingViewProps;
      oEndPointMappingViewProps.setActiveTabId("properties");
      oEndPointMappingViewProps.resetSelectedMappingFilterId();
      oComponentProps.screen.setOnboardingFileMappingViewVisibilityStatus(false);
      if (oCallbackData && oCallbackData.functionToExecute) {
        oCallbackData.functionToExecute();
      } else {
        _triggerChange();
      }
    }
    CustomActionDialogStore.showConfirmDialog(
        getTranslation().DIALOG_HEADER_FILE_UPLOAD_ABORT_CONFIRMATION,
        '',
        fOkButtonHandler);
  };

  var _handleEndPointMappingTabClicked = function (sTabId) {
    var oComponentProps = ContentUtils.getComponentProps();
    var oEndPointMappingViewProps = oComponentProps.endPointMappingViewProps;
    oEndPointMappingViewProps.setActiveTabId(sTabId);
    _triggerChange();
  };

  var _handleEndPointMappingFilterOptionChanged = function (sFilterId) {
    let oComponentProps = ContentUtils.getComponentProps();
    let oEndPointMappingViewProps = oComponentProps.endPointMappingViewProps;
    oEndPointMappingViewProps.setSelectedMappingFilterId(sFilterId);
    let oActiveEndPoint = oEndPointMappingViewProps.getActiveEndpoint();
    createRowProps(oActiveEndPoint);
    _triggerChange();
  };

  var _isAnyUnmappedElementNotIgnored = function (aMappings) {
    var oElement = CS.find(aMappings, {isIgnored: false, mappedElementId: null}) || {};

    return !CS.isEmpty(oElement);
  };

  var _isAnyUnmappedTagValue = function (aTagMappings) {
    var bIsInValid = false;
    CS.forEach(aTagMappings, function (oTagMapping) {
      if (!oTagMapping.isIgnored) {
        CS.forEach(oTagMapping.tagValueMappings, function (oTagValueMapping) {
          if (CS.find(oTagValueMapping.mappings, {mappedTagValueId: ""})) {
            bIsInValid = true;
            return false;
          }
        });
        return !bIsInValid;
      }
      else {
        oTagMapping.tagValueMappings = [];
      }
    });
    return bIsInValid;
  };

  var _isInvalidateDataToImport = function () {
    var oComponentProps = ContentUtils.getComponentProps();
    var oEndPointMappingViewProps = oComponentProps.endPointMappingViewProps;
    var oActiveEndPoint = oEndPointMappingViewProps.getActiveEndpoint();
    var aAttributeMappings = oActiveEndPoint.attributeMappings;
    var aTagMappings = oActiveEndPoint.tagMappings;
    var aKlassMappings = oActiveEndPoint.classMappings;
    var aTaxonomyMappings = oActiveEndPoint.taxonomyMappings;

    var bIsInvalidDataToImport = _isAnyUnmappedElementNotIgnored(aAttributeMappings) ||
        _isAnyUnmappedElementNotIgnored(aTagMappings) || _isAnyUnmappedTagValue(aTagMappings) ||
        _isAnyUnmappedElementNotIgnored(aKlassMappings) || _isAnyUnmappedElementNotIgnored(aTaxonomyMappings) ||
        !CS.isEmpty(oActiveEndPoint.unmappedColumns) || !CS.isEmpty(oActiveEndPoint.unmappedKlassColumns) ||
        !CS.isEmpty(oActiveEndPoint.unmappedTaxonomyColumns);

    return bIsInvalidDataToImport;
  };

  var _saveEndpoint = function (oCallback) {
    var oComponentProps = ContentUtils.getComponentProps();
    var oEndPointMappingViewProps = oComponentProps.endPointMappingViewProps;
    var oUpdatedEndpoint = oEndPointMappingViewProps.getActiveEndpoint();
    var oOriginalEndpoint = oEndPointMappingViewProps.getActiveEndpointClone();

    if(_isInvalidateDataToImport()) {
      alertify.error(getTranslation().PLEASE_MAP_OR_IGNORE_UNMAPPED_ELEMENT);
      throw new Error("UNMAPPED_FIELDS");
    }

    var oMappingsToSave = _createADMForMappings(oOriginalEndpoint, oUpdatedEndpoint);
    CS.postRequest(getRequestMapping().SaveMappings, {}, oMappingsToSave, successSaveEndpoint.bind(this, oOriginalEndpoint.fileInstanceId, oCallback), failureSaveEndpoint.bind(this, oCallback));
  };

  var successSaveEndpoint = function (sFileInstanceId, oCallback, oResponse) {
    var oComponentProps = ContentUtils.getComponentProps();
    oComponentProps.screen.setOnboardingFileMappingViewVisibilityStatus(false);
    let oEndPointMappingViewProps = oComponentProps.endPointMappingViewProps;
    oEndPointMappingViewProps.resetSelectedMappingFilterId();
    var oPostBody = {};
    CS.postRequest(getRequestMapping().OnboardingStartImport, {id: sFileInstanceId}, oPostBody, sucessOnboardingImport.bind(this, oCallback), failureSaveEndpoint.bind(this, oCallback));

  };

  var sucessOnboardingImport = function (oCallback) {
    alertify.success(getTranslation().SUCCESSFULLY_SAVED);
    if (oCallback && oCallback.functionToExecute) {
      oCallback.functionToExecute(true);
    }
    _triggerChange();
  };

  var failureSaveEndpoint = function (oCallback, oResponse) {
    if (oCallback && oCallback.functionToExecute) {
      oCallback.functionToExecute(true);
    }
    ContentUtils.failureCallback(oResponse, 'failureSaveEndpoint', getTranslation());
  };

  var _isAnyAutoMappedTagValue = function (oTagMapping) {
    let aTagValueMappings = oTagMapping.tagValueMappings;
    let bIsAnyAutoMappedTagValue = false;
    CS.forEach(aTagValueMappings, function (oTagValueMapping) {
      let aMappings = oTagValueMapping.mappings;
      CS.forEach(aMappings, function (oMapping) {
        bIsAnyAutoMappedTagValue = oMapping.isAutomapped;
        return !bIsAnyAutoMappedTagValue;
      });
      return !bIsAnyAutoMappedTagValue;
    });
    return bIsAnyAutoMappedTagValue;
  };

  var _createADMForMappings = function (oActiveProfile, oClonedProfile) {
    var oProfileToSave = CS.cloneDeep(oClonedProfile);
    delete oProfileToSave.attributeMappings;
    delete oProfileToSave.tagMappings;
    delete oProfileToSave.classMappings;
    delete oProfileToSave.taxonomyMappings;

    oProfileToSave.addedAttributeMappings = [];
    oProfileToSave.addedTagMappings = [];
    oProfileToSave.addedTaxonomyMappings = [];
    oProfileToSave.addedClassMappings = [];
    oProfileToSave.modifiedAttributeMappings = [];
    oProfileToSave.modifiedTagMappings = [];
    oProfileToSave.modifiedTaxonomyMappings = [];
    oProfileToSave.modifiedClassMappings = [];
    oProfileToSave.deletedAttributeMappings = [];
    oProfileToSave.deletedTagMappings = [];
    oProfileToSave.deletedTaxonomyMappings = [];
    oProfileToSave.deletedClassMappings = [];

    oProfileToSave.addedProcesses = CS.difference(oClonedProfile.processes, oActiveProfile.processes);
    oProfileToSave.deletedProcesses = CS.difference(oActiveProfile.processes, oClonedProfile.processes);
    delete oProfileToSave.processes;

    var aOldAttributeMappings = oActiveProfile.attributeMappings;
    var aNewAttributeMappings = oClonedProfile.attributeMappings;

    var aOldTagMappings = oActiveProfile.tagMappings;
    var aNewTagMappings = oClonedProfile.tagMappings;

    var aOldClassMappings = oActiveProfile.classMappings;
    var aNewClassMappings = oClonedProfile.classMappings;

    var aOldTaxonomyMappings = oActiveProfile.taxonomyMappings;
    var aNewTaxonomyMappings = oClonedProfile.taxonomyMappings;


    //Tags ADM
    CS.forEach(aOldTagMappings, function (oOldTagMapping) {
      var oNewTagMapping = CS.find(aNewTagMappings, {id: oOldTagMapping.id});

      /** Check for Old mapping exists in new mapping or not..**/
      if (!CS.isEmpty(oNewTagMapping)) {

        /***&&& Is old mapping equal identical to new mapping? if not go inside.
         * OR if there is any Automapped tagvalue then also go inside.
         * ***/
        let bIsAnyAutoMappedTagValue = _isAnyAutoMappedTagValue(oOldTagMapping);
        if(!CS.isEqual(oOldTagMapping, oNewTagMapping) || bIsAnyAutoMappedTagValue){
          if (!oOldTagMapping.isAutomapped && !CS.isEqual(oOldTagMapping.tagValueMappings, oNewTagMapping.tagValueMappings) || (bIsAnyAutoMappedTagValue && !oOldTagMapping.isAutomapped)) {
            var aAddedTagValue = [];
            var aModifiedTagValue = [];
            var aDeletedTagValue = [];
            CS.forEach(oOldTagMapping.tagValueMappings, function (oOldTagValueMap) {
              var oNewTagValueMap = CS.find(oNewTagMapping.tagValueMappings, {columnName: oOldTagValueMap.columnName});
              if (!CS.isEmpty(oNewTagValueMap)) {
                var aOldTagValueMapping = oOldTagValueMap.mappings;
                var aNewTagValueMapping = oNewTagValueMap.mappings;
                CS.forEach(aOldTagValueMapping, function (oTagValueMapOld) {
                  var oTagValueMapNew = CS.find(aNewTagValueMapping, {id: oTagValueMapOld.id});
                  if (CS.isEmpty(oTagValueMapNew)) {
                    if (oTagValueMapOld.mappedTagValueId) {
                      aDeletedTagValue.push(oTagValueMapOld.id);
                    }
                  } else {
                    if(oTagValueMapNew.isAutomapped){
                      oTagValueMapNew.isAutomapped = false;
                      aAddedTagValue.push(oTagValueMapNew);
                    }
                    else if (oTagValueMapOld.tagValue !== oTagValueMapNew.tagValue || oTagValueMapOld.isIgnoreCase !== oTagValueMapNew.isIgnoreCase) {
                      aModifiedTagValue.push(oTagValueMapNew);
                    }
                  }
                });

                CS.forEach(aNewTagValueMapping, function (oTagValueMapNew) {
                  var oTagValueMapOld = CS.find(aOldTagValueMapping, {id: oTagValueMapNew.id});
                  if (CS.isEmpty(oTagValueMapOld)) {
                    aAddedTagValue.push(oTagValueMapNew);
                  }
                });
              }
            });

            /** If tagValueMapping is getting added for the first time **/
            CS.forEach(oNewTagMapping.tagValueMappings, function (oNewTagValueMap) {
              var oOldTagValueMap = CS.find(oOldTagMapping.tagValueMappings, {columnName: oNewTagValueMap.columnName});
              if (CS.isEmpty(oOldTagValueMap)) {
                CS.forEach(oNewTagValueMap.mappings, function (oNewMapping) {
                  aAddedTagValue.push(oNewMapping);
                });
              }
            });

            oNewTagMapping.addedTagValueMappings = aAddedTagValue;
            oNewTagMapping.modifiedTagValueMappings = aModifiedTagValue;
            oNewTagMapping.deletedTagValueMappings = aDeletedTagValue;
            delete oNewTagMapping.tagValueMappings;

            if (oNewTagMapping.isAutomapped) {
              oNewTagMapping.isAutomapped = false;
              oProfileToSave.addedTagMappings.push(oNewTagMapping)
            } else {
              oProfileToSave.modifiedTagMappings.push(oNewTagMapping);
            }
          }
          else if (!oOldTagMapping.isAutomapped && !CS.isEqual(oOldTagMapping.columnNames, oNewTagMapping.columnNames) ||
              (oOldTagMapping.isIgnored != oNewTagMapping.isIgnored)) {
            oProfileToSave.modifiedTagMappings.push(oNewTagMapping);
          }
          else {
            oNewTagMapping.isAutomapped = false;
            oProfileToSave.addedTagMappings.push(oNewTagMapping)
          }
        }

      }
      /**If Old Mapping element not exists in new one, means it get deleted.*/
      else if(CS.isEmpty(oNewTagMapping)){
        if(!oOldTagMapping.isAutomapped){
          oProfileToSave.deletedTagMappings.push(oOldTagMapping.id);
        }
      }
    });

    CS.forEach(aNewTagMappings, function (oNewTagMapping) {
      var oOldTagMapping = CS.find(aOldTagMappings, {id: oNewTagMapping.id});
      if (CS.isEmpty(oOldTagMapping) || oNewTagMapping.isAutomapped) {
        oNewTagMapping.isAutomapped = false;
        oProfileToSave.addedTagMappings.push(oNewTagMapping);
      }
    });


    //Class ADM
    CS.forEach(aOldClassMappings, function (oOldClassMapping) {
      var oNewMapping = CS.find(aNewClassMappings, {id: oOldClassMapping.id});
      if(CS.isEmpty(oNewMapping) && !oOldClassMapping.isAutomapped) {
        oProfileToSave.deletedClassMappings.push(oOldClassMapping.id);
      }
    });

    CS.forEach(aNewClassMappings, function (oNewClassMapping) {
      let bUsed = false;
      var oOldClassMapping = CS.find(aOldClassMappings, {id: oNewClassMapping.id});
      if(CS.isEmpty(oOldClassMapping)) {
        oNewClassMapping.isAutomapped = false;
        oProfileToSave.addedClassMappings.push(oNewClassMapping);
        bUsed = true;
      } else {
        if(!CS.isEqual(oOldClassMapping, oNewClassMapping) && !oOldClassMapping.isAutomapped) {
          oProfileToSave.modifiedClassMappings.push(oNewClassMapping);
          bUsed = true;
        }
        else{
          if(oOldClassMapping.isAutomapped){
            oNewClassMapping.isAutomapped = false;
            oProfileToSave.addedClassMappings.push(oNewClassMapping);
            bUsed = true;
          }
        }
      }

      /**If mapping's isAutomapped is true then that means backEnd does not have that entry.
       * So UI need to send this in added list */
      if(!bUsed && oNewClassMapping.isAutomapped){
        oNewClassMapping.isAutomapped = false;
        oProfileToSave.addedClassMappings.push(oNewClassMapping);
      }
    });

    //Taxonomy ADM
    CS.forEach(aOldTaxonomyMappings, function (oOldTaxonomyMapping) {
      var oNewMapping = CS.find(aNewTaxonomyMappings, {id: oOldTaxonomyMapping.id});
      if(CS.isEmpty(oNewMapping) && !oOldTaxonomyMapping.isAutomapped) {
        oProfileToSave.deletedTaxonomyMappings.push(oOldTaxonomyMapping.id);
      }
    });

    CS.forEach(aNewTaxonomyMappings, function (oNewTaxonomyMapping) {
      let bUsed = false;
      var oOldTaxonomyMapping = CS.find(aOldTaxonomyMappings, {id: oNewTaxonomyMapping.id});
      if(CS.isEmpty(oOldTaxonomyMapping)) {
        oNewTaxonomyMapping.isAutomapped = false;
        oProfileToSave.addedTaxonomyMappings.push(oNewTaxonomyMapping);
        bUsed = true;
      } else {
        if(!CS.isEqual(oOldTaxonomyMapping, oNewTaxonomyMapping)) {
          oProfileToSave.modifiedTaxonomyMappings.push(oNewTaxonomyMapping);
          bUsed = true;
        }
        else{
          if(oOldTaxonomyMapping.isAutomapped){
            oNewTaxonomyMapping.isAutomapped = false;
            oProfileToSave.addedTaxonomyMappings.push(oNewTaxonomyMapping);
            bUsed = true;
          }
        }
      }

      /**If mapping's isAutomapped is true then that means backEnd does not have that entry.
       * So UI need to send this in added list */
      if(!bUsed && oNewTaxonomyMapping.isAutomapped){
        oNewTaxonomyMapping.isAutomapped = false;
        oProfileToSave.addedTaxonomyMappings.push(oNewTaxonomyMapping);
      }
    });

    //Attributes ADM
    CS.forEach(aOldAttributeMappings, function (oOldAttributeMapping) {
      var oNewMapping = CS.find(aNewAttributeMappings, {id: oOldAttributeMapping.id});
      if(CS.isEmpty(oNewMapping) && !oOldAttributeMapping.isAutomapped) {
        oProfileToSave.deletedAttributeMappings.push(oOldAttributeMapping.id);
      }
    });

    CS.forEach(aNewAttributeMappings, function (oNewAttributeMapping) {
      let bUsed = false;
      var oOldAttributeMapping = CS.find(aOldAttributeMappings, {id: oNewAttributeMapping.id});
      if(CS.isEmpty(oOldAttributeMapping)) {
        oNewAttributeMapping.isAutomapped = false;
        oProfileToSave.addedAttributeMappings.push(oNewAttributeMapping);
        bUsed = true;
      } else {
        if(oOldAttributeMapping.isAutomapped){
          oNewAttributeMapping.isAutomapped = false;
          oProfileToSave.addedAttributeMappings.push(oNewAttributeMapping);
          bUsed = true;
        } else if(!CS.isEqual(oOldAttributeMapping, oNewAttributeMapping)) {
            oProfileToSave.modifiedAttributeMappings.push(oNewAttributeMapping);
            bUsed = true;
        }
      }

      /**If mapping's isAutomapped is true then that means backEnd does not have that entry.
       * So UI need to send this in added list */
      if(!bUsed && oNewAttributeMapping.isAutomapped){
        oNewAttributeMapping.isAutomapped = false;
        oProfileToSave.addedAttributeMappings.push(oNewAttributeMapping);
      }
    });

    delete oProfileToSave.unmappedColumns;
    delete oProfileToSave.unmappedKlassColumns;
    delete oProfileToSave.unmappedTaxonomyColumns;

    return oProfileToSave;
  };

  /************************************** Public API's **********************************************/
  return {
    handleOnboardingFileUploaded: function (aFiles, oCallback) {
      _handleOnboardingFileUploaded(aFiles, oCallback);
    },

    handleEndpointMappedElementChanged: function (sId, sMappedElement, sOptionType, oReferencedData) {
      _handleEndpointMappedElementChanged(sId, sMappedElement, sOptionType, oReferencedData)
    },

    handleProfileUnmappedElementChanged: function (sName, sMappedElement, sOptionType, oReferencedData) {
      _handleProfileUnmappedElementChanged(sName, sMappedElement, sOptionType, oReferencedData)
    },

    handleProfileConfigMappedTagValueChanged: function (sId, sMappedTagValueId, sNewValue, sTagGroupId, oReferencedTagValues) {
      _handleProfileConfigMappedTagValueChanged(sId, sMappedTagValueId, sNewValue, sTagGroupId, oReferencedTagValues)
    },

    handleEndpointTagValueIgnoreCaseToggled: function (sId, sMappedTagValueId) {
      _handleEndpointTagValueIgnoreCaseToggled(sId, sMappedTagValueId)
    },

    handleEndpointIsIgnoredToggled: function (sId, sTabId) {
      _handleEndpointIsIgnoredToggled(sId, sTabId);
    },

    handleEndpointUnmappedElementIsIgnoredToggled: function (sId, sTabId) {
      _handleEndpointUnmappedElementIsIgnoredToggled(sId, sTabId);
    },

    handleEndPointMappingViewBackButtonClicked: function (oCallbackData) {
      _handleEndPointMappingViewBackButtonClicked(oCallbackData);
    },

    handleEndPointMappingViewImportButtonClicked: function (oCallback) {
      _saveEndpoint(oCallback);
    },

    handleEndPointMappingTabClicked: function (sTabId) {
      _handleEndPointMappingTabClicked(sTabId);
    },

    handleEndPointMappingFilterOptionChanged : function (sFilterId) {
      _handleEndPointMappingFilterOptionChanged(sFilterId);
    },

    handleExportToMDMButtonClicked: function () {
      _handleExportToMDMButtonClicked();
    },

    handleExportEntityButtonClicked: function (oSelectiveExport) {
      _handleExportEntityButtonClicked(oSelectiveExport);
    },

    handleImportEntityButtonClicked: function (aFiles,oImportExcel) {
      _handleImportEntityButtonClicked(aFiles,oImportExcel);
    },

    handleTransferToSupplierStagingButtonClicked: function () {
      _handleTransferToSupplierStagingButtonClicked();
    },

    handleExportToExcelButtonClicked: function () {
      _handleExportToExcelButtonClicked();
    },
  }
})();

MicroEvent.mixin(OnboardingStore);

export default OnboardingStore;
