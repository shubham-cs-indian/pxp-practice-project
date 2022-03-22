import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import SettingUtils from './../helper/setting-utils';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import RequestMapping from '../../../../../../libraries/requestmappingparser/request-mapping-parser.js';
import { SmartDocumentRequestMapping as oSmartDocumentRequestMapping } from '../../tack/setting-screen-request-mapping';
import SmartDocumentConfigProps from './../model/smart-document-config-view-props';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import SettingScreenProps from './../model/setting-screen-props';
import CS from "../../../../../../libraries/cs";

let SmartDocumentStore = (function () {

  let _triggerChange = function () {
    SmartDocumentStore.trigger('smart-document-changed');
  };

  let _getIsSmartDocumentConfigDirty = function () {
    return SmartDocumentConfigProps.getIsSmartDocumentConfigDirty();
  }

  let _createAndSetDummySectionModel = function (sParentId) {
    let oDummySectionModel = {
      id: UniqueIdentifierGenerator.generateUUID(),
      label: UniqueIdentifierGenerator.generateUntitledName(),
      parentId: sParentId,
      abbreviation: '',
      localeId: '',
      isNewlyCreated: true,
      code: "",
    };
    if (sParentId === "smartdocument") {
      oDummySectionModel.zipTemplateId = "";
      oDummySectionModel.type = "smartDocumentTemplate"
    } else {
      oDummySectionModel.type = "smartDocumentPreset"
      oDummySectionModel.languageCode = "";
      oDummySectionModel.smartDocumentPresetPdfConfiguration = {}
    }
    SmartDocumentConfigProps.setActiveSection(oDummySectionModel);
    _triggerChange();
  };

  let _makeSmartDocumentDirty = function () {
    SmartDocumentConfigProps.setIsSmartDocumentConfigDirty(true);
    let oActiveSection = SmartDocumentConfigProps.getActiveSection();
    if (!(oActiveSection.clonedObject || oActiveSection.isCreated)) {
      SettingUtils.makeObjectDirty(oActiveSection);
    }
    return oActiveSection.clonedObject ? oActiveSection.clonedObject : oActiveSection;
  }

  let _handleSelectionToggleMultipleValueChanged = function (oActiveSection, sKey, sVal) {
    if (CS.includes(oActiveSection[sKey], sVal)) {
      CS.pull(oActiveSection[sKey], sVal);
    } else {
      oActiveSection[sKey].push(sVal);
    }
  };

  let _handleSmartDocumentPresetMSSValueClicked = function(sKey, aSelectedItems, oReferencedData){
    let oActiveSection = _makeSmartDocumentDirty();
    switch(sKey){
      case "attributes":
        oActiveSection.referencedAttributes = oReferencedData;
        oActiveSection.attributeIds = aSelectedItems;
        break;
      case "tags":
        oActiveSection.referencedTags = oReferencedData;
        oActiveSection.tagIds = aSelectedItems;
        break;
      case "klassIds":
        oActiveSection.referencedKlasses = oReferencedData;
        oActiveSection.klassesIds = aSelectedItems;
        break;
    }
    _triggerChange();
  };

  let _handleCommonConfigValueChanged = function (sKey, sVal) {
    let oActiveSection = _makeSmartDocumentDirty();
    let aPresetConfigKeys = ['pdfAuthor', 'pdfKeywords', 'pdfTitle', 'pdfSubject', 'pdfUserPassword', 'pdfOwnerPassword',
                             'pdfAllowAnnotations', 'pdfAllowCopyContent', 'pdfAllowModifications', 'pdfAllowPrinting', 'pdfColorSpace', 'pdfMarksBleeds'];
    if (CS.includes(aPresetConfigKeys, sKey)) {
      oActiveSection = oActiveSection.smartDocumentPresetPdfConfiguration;
    }
    if (oActiveSection.type === "smartDocumentEntity" && sKey === "physicalCatalogIds") {
      _handleSelectionToggleMultipleValueChanged(oActiveSection, sKey, sVal)
    } else {
      oActiveSection[sKey] = sVal;
    }
    _triggerChange();
  };

  let _handleKlassMSSCrossIconClicked = function (sId, sKey) {
    let oActiveSection = _makeSmartDocumentDirty();
    let oSmartDocumentPresetPdfConfiguration = oActiveSection.smartDocumentPresetPdfConfiguration;
    switch(sKey){
      case "pdfColorSpace":
        oSmartDocumentPresetPdfConfiguration.pdfColorSpace = "";
        break;
      case  "klassIds":
        CS.pull(oActiveSection.klassesIds, sId);
        break;
      case "pdfMarksBleeds":
        oSmartDocumentPresetPdfConfiguration.pdfMarksBleeds = "";
        break;
    }
    _triggerChange();
  };

  let _handleTaxonomySearchClicked = function (sContext, sTaxonomyId, sSearchText) {
    SmartDocumentConfigProps.setAllowedTaxonomies([]);
    let oTaxonomyPaginationData = SmartDocumentConfigProps.getTaxonomyPaginationData();
    oTaxonomyPaginationData.from = 0;
    oTaxonomyPaginationData.searchText = sSearchText;
    _fetchTaxonomyById(sContext, sTaxonomyId);
  };

  let _handleTaxonomyLoadMoreClicked = function (sContext, sTaxonomyId) {
    let oTaxonomyPaginationData = SmartDocumentConfigProps.getTaxonomyPaginationData();
    let aTaxonomyList = SmartDocumentConfigProps.getAllowedTaxonomies();
    oTaxonomyPaginationData.from = aTaxonomyList.length;
    _fetchTaxonomyById(sContext, sTaxonomyId);
  };

  let _handleCommonConfigUploadTemplateActionClicked = function (oElementData, aFiles) {
    var bIsValidZipFile = false;
    if (aFiles.length === 1) {
      var oCurrentFormData = new FormData();
      var oFile = aFiles[0];
      var sFileName = oFile.name;
      var oReader = new FileReader();
      var sFileExtension = sFileName.substring(sFileName.lastIndexOf(".") + 1);
      if (sFileExtension === "zip") {
        bIsValidZipFile = true;
        oReader.onload = (function (oFileNode) {
          return function (oFileEvent) {
            oCurrentFormData.append("smartDocumentTemplate", oFileNode);
          };
        })(oFile);
        oReader.onloadend = function () {
          SettingUtils.csCustomPostRequest(oSmartDocumentRequestMapping.UploadSmartDocumentTemplate, oCurrentFormData,
              successUploadSmartDocumentTemplate, failureUploadSmartDocumentTemplate, false)
        };
        oReader.readAsDataURL(oFile);
      }
    }
    if (!bIsValidZipFile) {
      alertify.error(getTranslation().TEMPLATE_UPLOAD_CANCELLED);
    }
  };

  var successUploadSmartDocumentTemplate = function (oResponse) {
    oResponse = oResponse.success;
    var oActiveSmartDocument = _makeSmartDocumentDirty("smartDocumentTemplate");
    oActiveSmartDocument['zipTemplateId'] = oResponse.id;
    alertify.success(getTranslation().TEMPLATE_UPLOADED_SUCCESSFULLY);
    _triggerChange();
  };

  var failureUploadSmartDocumentTemplate = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureUploadSmartDocumentTemplate", getTranslation());
  };

  let _discardSmartDocumentChanges = function (oCallback) {
    let oActiveSection = SmartDocumentConfigProps.getActiveSection();
    delete oActiveSection.clonedObject;
    SmartDocumentConfigProps.setIsSmartDocumentConfigDirty(false);
    alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
    if (oCallback && oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
    _triggerChange();
    };

  let _checkAllValueDataOfSmartDocuments = function (sId, sKey, defaultValue, value) {
    let oSmartDocumentValueList = SmartDocumentConfigProps.getSmartDocumentValueListByTypeGeneric();
    CS.forEach(oSmartDocumentValueList, function (oDocumentValue) {
      if (oDocumentValue.id != "smartdocument") {
        oDocumentValue[sKey] = defaultValue;
      }
    });
    oSmartDocumentValueList[sId][sKey] = value;
  };

  let _fillPresetSectionValues = function (oSection) {
    let oPDFConfigurations = oSection.smartDocumentPresetPdfConfiguration || {};
    oSection.attributeIds = oSection.attributeIds || [];
    oSection.tagIds = oSection.tagIds || [];
    oSection.referencedAttributes = oSection.referencedAttributes || {};
    oSection.referencedTags = oSection.referencedTags || {};
    oSection.languageCode = oSection.languageCode === "" ?  "cur_lang" : oSection.languageCode;
    oSection.taxonomyIds = oSection.taxonomyIds || [];
    oSection.klassesIds = oSection.klassIds || [];
    oSection.referencedTaxonomies = oSection.referencedTaxonomies || {};
    oSection.referencedKlasses = oSection.referencedKlasses || {};
    oPDFConfigurations.pdfAuthor = oPDFConfigurations.pdfAuthor || "";
    oPDFConfigurations.pdfKeywords = oPDFConfigurations.pdfKeywords || "";
    oPDFConfigurations.pdfTitle = oPDFConfigurations.pdfTitle || "";
    oPDFConfigurations.pdfSubject = oPDFConfigurations.pdfSubject || "";
    oPDFConfigurations.pdfUserPassword = oPDFConfigurations.pdfUserPassword || "";
    oPDFConfigurations.pdfOwnerPassword = oPDFConfigurations.pdfOwnerPassword || "";
    oSection.smartDocumentPresetPdfConfiguration = oPDFConfigurations;
  };

  let _addChildrensToSections = function (oSection) {
    if (oSection.type === "smartDocumentTemplate") {
      oSection.children = oSection.smartDocumentPresets || [];
      delete oSection.smartDocumentPresets;
    } else if (oSection.id === "smartdocument") {
      oSection.children = oSection.smartDocumentTemplates;
      delete oSection.smartDocumentTemplates;
    }
  }

  var successFetchLanguageInfoCallback = function (oResponse) {
    let oSuccess = oResponse.success;
    let aDataLanguages = oSuccess.dataLanguages;
    aDataLanguages.unshift({id: 'cur_lang', label: getTranslation().CURRENT_LANGUAGE, code: 'cur_lang'});
    SmartDocumentConfigProps.setLanguageCodes(aDataLanguages);
    _triggerChange();
  };

  var failureFetchLanguageInfoCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchLanguageInfoCallback", getTranslation());
  };

  var _fetchLanguageInfo = function () {
    return SettingUtils.csPostRequest(oSmartDocumentRequestMapping.GetDataLanguage, {}, {isGetDataLanguages: true},
        successFetchLanguageInfoCallback, failureFetchLanguageInfoCallback);
  };

  let _processSmartDocumentConfigEntityData = function (oSection, aChildren) {
    let oSmartDocumentEntity = SmartDocumentConfigProps.getSmartDocumentEntity();
    let oSmartDocumentValueList = SmartDocumentConfigProps.getSmartDocumentValueListByTypeGeneric();
    SettingUtils.addNewTreeNodesToValueList(oSmartDocumentValueList, aChildren, {'canDelete': true, 'canCreate': true});

    _checkAllValueDataOfSmartDocuments(oSection.id, 'isActive', false, true);
    _checkAllValueDataOfSmartDocuments(oSection.id, 'isChecked', false, true);
    _checkAllValueDataOfSmartDocuments(oSection.id, 'isLoading', false, true);

    let oCurrentNode = SettingUtils.getParentNodeByChildId({children: oSmartDocumentEntity}, oSection.id);
    if (oSection.type === "smartDocumentTemplate") {
      let oCurrentNode = CS.find(oSmartDocumentEntity.children, {id: oSection.id});
      oCurrentNode.label = oSection.label;
      oCurrentNode.children = oSection.children;
    } else if (oSection.type === "smartDocumentPreset") {
      let oPresetNode = CS.find(oCurrentNode.children, {id: oSection.id});
      oPresetNode.label = oSection.label;
      _fetchLanguageInfo();
      _fillPresetSectionValues(oSection);
    }
    let aChildrenKeyValueToReset = [{key: "isExpanded", value: false}, {key: "isSelected", value: false}];

    SettingUtils.applyToAllNodesBelow(aChildrenKeyValueToReset, oCurrentNode, oSmartDocumentValueList, true);
    oSmartDocumentValueList[oSection.id].isSelected = true;
    oSmartDocumentValueList[oSection.id].isExpanded = true;

    SmartDocumentConfigProps.setSmartDocumentEntity(oSmartDocumentEntity);
    SmartDocumentConfigProps.setActiveSection(oSection);
    SmartDocumentConfigProps.setIsSmartDocumentConfigDirty(false);
  }

  let successCreateSmartDocumentSection = function (oResponse) {
    let oSection = oResponse.success;
    let oSmartDocumentEntity = SmartDocumentConfigProps.getSmartDocumentEntity();
    let aHierarchyChildren = {};
    let sMessage = "";
    if (oSection.type === "smartDocumentTemplate") {
      _addChildrensToSections(oSection);
      aHierarchyChildren = oSmartDocumentEntity.children;
      sMessage = SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY,{ entity : getTranslation().TEMPLATE});
    } else if (oSection.type === "smartDocumentPreset") {
      aHierarchyChildren = CS.find(oSmartDocumentEntity.children, {id: oSection.smartDocumentTemplateId}).children;
      sMessage = SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY,{ entity : getTranslation().PRESET});
    }
    aHierarchyChildren.push({id: oSection.id, label: oSection.label, type: oSection.type});
    _processSmartDocumentConfigEntityData(oSection, aHierarchyChildren);
    alertify.success(sMessage);
    _triggerChange();
  };

  let failureCreateSmartDocumentSection = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureCreateSmartDocumentSection", getTranslation());
  };

  let _createSmartDocumentSection = function () {
    let oActiveSection = SmartDocumentConfigProps.getActiveSection();
    oActiveSection = oActiveSection.clonedObject || oActiveSection;
    let sType = oActiveSection.type;
    let sURL = "";
    let oPutData = {
      label: oActiveSection.label,
      code: oActiveSection.code,
      type: oActiveSection.type,
    };
    switch (sType) {
      case "smartDocumentTemplate":
        sURL = oSmartDocumentRequestMapping.CreateSmartDocumentTemplate;
        oPutData.zipTemplateId = oActiveSection.zipTemplateId;
        break;
      case "smartDocumentPreset":
        sURL = oSmartDocumentRequestMapping.CreateSmartDocumentPreset;
        oPutData.smartDocumentTemplateId = oActiveSection.parentId;
        break;
    }
    SettingUtils.csPutRequest(sURL, {}, oPutData, successCreateSmartDocumentSection, failureCreateSmartDocumentSection);
    _triggerChange();
  };

  let _verifyAndCreateSmartDocumentSection = function () {
    let oActiveSection = SmartDocumentConfigProps.getActiveSection();
    oActiveSection = oActiveSection.clonedObject || oActiveSection;
    if (CS.isEmpty(oActiveSection.label.trim())) {
      alertify.error(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return;
    }
    let oCodeToVerifyUniqueness = {
      id: oActiveSection.code,
      entityType: oActiveSection.type
    };
    let oCallbackDataForUniqueness = {};
    oCallbackDataForUniqueness.functionToExecute = _createSmartDocumentSection;

    let urlToVerifyCode = oSmartDocumentRequestMapping.CheckEntityCode;
    SettingUtils.checkForUniquenessOfCodePropertyAndSendAjaxCall(oCodeToVerifyUniqueness, urlToVerifyCode, oCallbackDataForUniqueness);
  };

  var successFetchSmartDocumentSection = function (oResponse) {
    let oSection = oResponse.success;
    _addChildrensToSections(oSection);
    if (oSection.type === "smartDocumentTemplate") {
      _processSmartDocumentConfigEntityData(oSection, oSection.children)
    }
    else if(oSection && oSection.type === "smartDocumentPreset"){
    	_processSmartDocumentConfigEntityData(oSection, oSection.children);
    }
    else {
      var oSmartDocumentValueList = SmartDocumentConfigProps.getSmartDocumentValueListByTypeGeneric();
      SettingUtils.addNewTreeNodesToValueList(oSmartDocumentValueList, oSection.children, {'canDelete': true, 'canCreate': true});
      SmartDocumentConfigProps.setSmartDocumentEntity(oSection);
      SmartDocumentConfigProps.setIsSmartDocumentConfigDirty(false);
      SmartDocumentConfigProps.setActiveSection({
        code: oSection.code,
        label: oSection.label,
        type: "smartDocumentEntity",
        rendererLicenceKey: oSection.rendererLicenceKey || "",
        physicalCatalogIds: oSection.physicalCatalogIds || [],
      });
    }
    _triggerChange();
  };

  var failureFetchSmartDocumentSection = function (oResponse) {
    SettingUtils.failureCallback(oResponse, 'failureFetchSmartDocumentSection', getTranslation());
  };

  let _fetchSmartDocumentConfigEntityDetails = function (sNodeId, sSectionType) {
    let oPutData = {id: sNodeId};
    let sURL = "";
    switch (sSectionType) {
      case "smartDocumentEntity":
        sURL = oSmartDocumentRequestMapping.GetSmartDocument;
        break;
      case "smartDocumentTemplate":
        sURL = oSmartDocumentRequestMapping.GetSmartDocumentTemplate;
        break;
      case "smartDocumentPreset":
        sURL = oSmartDocumentRequestMapping.GetSmartDocumentPreset;
        break;
    }
    SettingUtils.csCustomGetRequest(RequestMapping.getRequestUrl(sURL, oPutData), {},
        successFetchSmartDocumentSection, failureFetchSmartDocumentSection);
  };

  let successSaveSmartDocumentSection = function (sMessage, oCallback, oResponse) {
    successFetchSmartDocumentSection(oResponse)
    alertify.success(sMessage);
    if(oCallback && oCallback.functionToExecute){
      oCallback.functionToExecute();
      _triggerChange();
    }
  };

  let failureSaveSmartDocumentSection = function (sType, oResponse) {
    SettingUtils.failureCallback(oResponse, "failureSaveSmartDocumentSection", getTranslation());
  }

  let _getSmartDocumentPresetADMData = function (oActiveSection) {
    let oPreviouslyActiveSection = SmartDocumentConfigProps.getActiveSection();
    let aPreviouslySelectedAttributeIds = oPreviouslyActiveSection.attributeIds;
    let aRecentlySelectedAttributeIds = oActiveSection.attributeIds;
    let aPreviouslySelectedTagIds = oPreviouslyActiveSection.tagIds;
    let aRecentlySelectedTagIds = oActiveSection.tagIds;
    let aPreviouslySelectedKlassIds = oPreviouslyActiveSection.klassesIds;
    let aRecentlySelectedKlassIds = oActiveSection.klassesIds;
    let aPreviouslySelectedTaxonomyIds = oPreviouslyActiveSection.taxonomyIds;
    let aRecentlySelectedTaxonomyIds = oActiveSection.taxonomyIds;


    let aIntersectionOfAttributes = CS.intersection(aPreviouslySelectedAttributeIds, aRecentlySelectedAttributeIds);
    let aIntersectionOfTags = CS.intersection(aPreviouslySelectedTagIds, aRecentlySelectedTagIds);
    let aIntersectionOfKlassIds = CS.intersection(aPreviouslySelectedKlassIds, aRecentlySelectedKlassIds);
    let aIntersectionOfTaxonomyIds = CS.intersection(aPreviouslySelectedTaxonomyIds, aRecentlySelectedTaxonomyIds);

    let oSmartDocumentPresetADMData = {
      addedAttributeIds: CS.difference(aRecentlySelectedAttributeIds, aIntersectionOfAttributes) || [],
      addedTagIds: CS.difference(aRecentlySelectedTagIds, aIntersectionOfTags) || [],
      deletedAttributeIds: CS.difference(aPreviouslySelectedAttributeIds, aIntersectionOfAttributes) || [],
      deletedTagIds: CS.difference(aPreviouslySelectedTagIds, aIntersectionOfTags) || [],
      addedKlassIds: CS.difference(aRecentlySelectedKlassIds, aIntersectionOfKlassIds) || [],
      deletedKlassIds: CS.difference(aPreviouslySelectedKlassIds, aIntersectionOfKlassIds) || [],
      addedTaxonomyIds: CS.difference(aRecentlySelectedTaxonomyIds, aIntersectionOfTaxonomyIds) || [],
      deletedTaxonomyIds: CS.difference(aPreviouslySelectedTaxonomyIds, aIntersectionOfTaxonomyIds) || [],
    };
      return oSmartDocumentPresetADMData;
  };

  let _saveSmartDocumentSection = function (oCallback) {
    let oActiveSection = SmartDocumentConfigProps.getActiveSection();
    oActiveSection = oActiveSection.clonedObject || oActiveSection;
    let sType = oActiveSection.type;
    let oPostData = {};
    let sURL = "";
    let sMessage = "";
    switch (sType) {
      case "smartDocumentEntity":
        sURL = oSmartDocumentRequestMapping.SaveSmartDocument;
        oPostData.label = oActiveSection.label;
        oPostData.rendererLicenceKey = oActiveSection.rendererLicenceKey;
        oPostData.physicalCatalogIds = oActiveSection.physicalCatalogIds;
        sMessage = SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY , { entity :getTranslation().SMART_DOCUMENT});
        break;
      case "smartDocumentTemplate":
        sURL = oSmartDocumentRequestMapping.SaveSmartDocumentTemplate;
        oPostData.id = oActiveSection.id;
        oPostData.label = oActiveSection.label;
        oPostData.zipTemplateId = oActiveSection.zipTemplateId;
        sMessage = SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY,{ entity : getTranslation().TEMPLATE});
        break;
      case "smartDocumentPreset":
        sURL = oSmartDocumentRequestMapping.SaveSmartDocumentPreset;
        oActiveSection.languageCode = oActiveSection.languageCode === 'cur_lang' ? "" : oActiveSection.languageCode;
        let oSmartDocumentPresetADMData = _getSmartDocumentPresetADMData(oActiveSection);
        oPostData.smartDocumentPreset = {
          code: oActiveSection.code,
          icon: oActiveSection.icon,
          id: oActiveSection.id,
          label: oActiveSection.label,
          languageCode: oActiveSection.languageCode,
          lastModifiedBy: oActiveSection.lastModifiedBy,
          saveDocument: oActiveSection.saveDocument,
          showPreview: oActiveSection.showPreview,
          smartDocumentPresetPdfConfiguration: oActiveSection.smartDocumentPresetPdfConfiguration,
          smartDocumentTemplateId: oActiveSection.smartDocumentTemplateId,
          splitDocument: oActiveSection.splitDocument,
          type: oActiveSection.type,
          versionId: oActiveSection.versionId,
          versionTimestamp: oActiveSection.versionTimestamp,
        };
        oPostData = CS.assign(oPostData, oSmartDocumentPresetADMData);
        sMessage = SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY,{ entity : getTranslation().PRESET});
        break;
    }
    SettingUtils.csPostRequest(sURL, {}, oPostData, successSaveSmartDocumentSection.bind(this, sMessage, oCallback), failureSaveSmartDocumentSection.bind(this, sType));
  };

  let successDeleteSmartDocument = function (oCallbackData, oResponse) {
    let aDeletedNodes = oResponse.success;
    SettingUtils.removeNodeById([SmartDocumentConfigProps.getSmartDocumentEntity()], oCallbackData.id);
    SmartDocumentConfigProps.setActiveSection({});
    let sMessage = oCallbackData.type === "smartDocumentTemplate" ? SettingUtils.getDecodedTranslation(getTranslation().ENTITY_DELETED_SUCCESSFULLY,{entity : getTranslation().CLASS_CATEGORY_DTP_TEMPLATE_TITLE}) : SettingUtils.getDecodedTranslation(getTranslation().ENTITY_REMOVED_SUCCESSFULLY,{entity : getTranslation().PRESET});
    alertify.success(sMessage);
    _triggerChange();
  };

  let failureDeleteSmartDocument = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureDeleteSmartDocument", getTranslation());
  };

  let _deleteSmartDocument = function (sNodeId, iLevel) {
    let sURL = "";
    let oDeleteData = {id: sNodeId};
    if (iLevel === 1) {
      sURL = oSmartDocumentRequestMapping.DeleteSmartDocumentPreset;
      oDeleteData.type = "smartDocumentPreset";
    } else {
      sURL = oSmartDocumentRequestMapping.DeleteSmartDocumentTemplate;
      oDeleteData.type = "smartDocumentTemplate";
    }
    let oSmartDocumentEntity = SmartDocumentConfigProps.getSmartDocumentEntity();
    let oCurrentNode = SettingUtils.getNodeFromTreeListById(oSmartDocumentEntity.children, sNodeId);
    let sCurrentNodeLabel = CS.getLabelOrCode(oCurrentNode);
    CustomActionDialogStore.showConfirmDialog(sCurrentNodeLabel,
        getTranslation().DELETE_CONFIRMATION, function () {
      SettingUtils.csDeleteRequest(sURL, {}, oDeleteData, successDeleteSmartDocument.bind(this, oDeleteData), failureDeleteSmartDocument);
    }, function (oEvent) {
    });
  }

  let successFetchTaxonomy = function (oResponse) {
    let aTaxonomyListFromServer = oResponse.success.list;
    let aTaxonomyList = SmartDocumentConfigProps.getAllowedTaxonomies();
    SmartDocumentConfigProps.setParentTaxonomyList(aTaxonomyListFromServer);
    SmartDocumentConfigProps.setAllowedTaxonomies(CS.concat(aTaxonomyList, aTaxonomyListFromServer));
    _triggerChange();
  };

  let failureFetchFetchTaxonomy = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchFetchTaxonomy", getTranslation());
  };

  let _fetchTaxonomyById = function (sContext, sTaxonomyId) {
    if (sTaxonomyId == "addItemHandlerforMultiTaxonomy") {
      sTaxonomyId = SettingUtils.getTreeRootId();
    }
    let oTaxonomyPaginationData = SmartDocumentConfigProps.getTaxonomyPaginationData();
    let oPostData = CS.assign({taxonomyId: sTaxonomyId}, oTaxonomyPaginationData);
    let oNewEntityVsSearchTextMapping = {
      "taxonomies": oTaxonomyPaginationData.searchText
    };
    let oOldEntityVsSearchTextMapping = SettingScreenProps.screen.getEntityVsSearchTextMapping();
    CS.assign(oOldEntityVsSearchTextMapping, oNewEntityVsSearchTextMapping);

    SettingUtils.csPostRequest(oSmartDocumentRequestMapping.GetTaxonomy, {}, oPostData, successFetchTaxonomy, failureFetchFetchTaxonomy);
  };

  let _handleAllowedTypesDropdownOpened = function (sContext, sTaxonomyId) {
    SmartDocumentConfigProps.setAllowedTaxonomies([]);
    let oTaxonomyPaginationData = SmartDocumentConfigProps.getTaxonomyPaginationData();
    oTaxonomyPaginationData.searchText = "";
    oTaxonomyPaginationData.from = 0;
    _fetchTaxonomyById(sContext, sTaxonomyId);
  }

  let _handleTaxonomyAdded = function (oModel, sParentTaxonomyId, sViewContext) {
    let oActivePresetSection = _makeSmartDocumentDirty();
    let oReferencedTaxonomies = oActivePresetSection.referencedTaxonomies;
    let aSelectedTaxonomies = oActivePresetSection.taxonomyIds;
    let aParentTaxonomyList = SmartDocumentConfigProps.getParentTaxonomyList();
    let bIsParentTaxonomyPresent = false;
    CS.forEach(aParentTaxonomyList, function (oParentTaxonomy) {
      if (oParentTaxonomy.id == oModel.id && oParentTaxonomy.type == "majorTaxonomy") {
        bIsParentTaxonomyPresent = true;
      }
    });
    aSelectedTaxonomies.push(oModel.id);
    if (CS.isEmpty(oReferencedTaxonomies) || bIsParentTaxonomyPresent) {
      let temp = {};
      temp.id = oModel.id;
      temp.parent = {};
      temp.parent.id = "-1";
      temp.parent.label = null;
      temp.parent.parent = null;
      temp.label = oModel.label;
      oReferencedTaxonomies[oModel.id] = temp;
      SmartDocumentConfigProps.setReferencedTaxonomies(oReferencedTaxonomies);
    } else {
      let temp = {};
      temp.id = oModel.id;
      temp.label = oModel.label;
      temp.parent = oReferencedTaxonomies[sParentTaxonomyId];
      oReferencedTaxonomies[oModel.id] = temp;
      delete oReferencedTaxonomies[sParentTaxonomyId];
      let iParentIndex = aSelectedTaxonomies.indexOf(temp.parent.id);
      aSelectedTaxonomies.splice(iParentIndex, 1);
      SmartDocumentConfigProps.setReferencedTaxonomies(oReferencedTaxonomies);
    }

    _saveSmartDocumentSection();
  };

  let _handleMultiSelectSmallTaxonomyViewCrossIconClicked = function (oTaxonomy, sParentTaxonomyId, sViewContext) {
    let oActivePresetSection = _makeSmartDocumentDirty();
    let oReferencedTaxonomies = oActivePresetSection.referencedTaxonomies;
    let aSelectedTaxonomies = oActivePresetSection.taxonomyIds;
    let iActiveTaxonomyIndex = aSelectedTaxonomies.indexOf(sParentTaxonomyId);
    aSelectedTaxonomies.splice(iActiveTaxonomyIndex, 1);
    let sParentId = oTaxonomy.parent.id;
    if (sParentId !== "-1") {
      if (!CS.includes(aSelectedTaxonomies, sParentId)) {
        aSelectedTaxonomies.push(sParentId);
        oReferencedTaxonomies[sParentId] = oTaxonomy.parent;
      }
    }
    delete oReferencedTaxonomies[sParentTaxonomyId];
    _saveSmartDocumentSection();
  };

  return {

    createDummySmartDocumentSection: function (sNodeId) {
      _createAndSetDummySectionModel(sNodeId);
    },

    handleSmartDocumentDialogButtonClicked: function (sButtonId) {
      if (sButtonId === "create") {
        _verifyAndCreateSmartDocumentSection();
      } else {
        let oActiveSection = SmartDocumentConfigProps.getActiveSection();
        if (oActiveSection.type === "smartDocumentPreset")
          _fetchSmartDocumentConfigEntityDetails(oActiveSection.parentId, "smartDocumentTemplate");
        else
          _fetchSmartDocumentConfigEntityDetails("smartdocument", "smartDocumentEntity");
      }
    },

    handleSmartDocumentSnackBarButtonClicked: function (sButtonId) {
      if (sButtonId === "save") {
        _saveSmartDocumentSection();
      }
      else {
        _discardSmartDocumentChanges();
      }
    },

    saveSmartDocumentSection: function (oCallback) {
      _saveSmartDocumentSection(oCallback);
    },

    discardSmartDocumentChanges: function (oCallback) {
      _discardSmartDocumentChanges(oCallback);
    },

    getIsSmartDocumentConfigDirty: function () {
      return _getIsSmartDocumentConfigDirty();
    },

    fetchSmartDocumentConfigDetails: function (oReqData) {
      if (oReqData && oReqData.level === 0) {
        _fetchSmartDocumentConfigEntityDetails(oReqData.clickedNodeId, "smartDocumentTemplate");
      } else if (oReqData && oReqData.level === 1) {
        _fetchSmartDocumentConfigEntityDetails(oReqData.clickedNodeId, "smartDocumentPreset");
      } else {
        _fetchSmartDocumentConfigEntityDetails("smartdocument", "smartDocumentEntity");
      }
    },

    switchSmartDocumentSection: function (oReqData){
      let bIsSmartDocumentDirty = this.getIsSmartDocumentConfigDirty();
      if(!bIsSmartDocumentDirty){
        this.fetchSmartDocumentConfigDetails(oReqData);
      }else{
        let oActiveSection = SmartDocumentConfigProps.getActiveSection();
        if(oReqData.clickedNodeId !== oActiveSection.id){
          let oSwitchCallback = {};
          oSwitchCallback.functionToExecute = this.fetchSmartDocumentConfigDetails.bind(this, oReqData);
          CustomActionDialogStore.showTriActionDialog(getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE, _saveSmartDocumentSection.bind(this, oSwitchCallback), _discardSmartDocumentChanges.bind(this, oSwitchCallback), function () {});
        }
      }
    },

    handleCommonConfigUploadTemplateActionClicked: function (oElementData, aFiles) {
      _handleCommonConfigUploadTemplateActionClicked(oElementData, aFiles);
    },

    handleCommonConfigValueChanged: function (sKey, sVal, sContext) {
      _handleCommonConfigValueChanged(sKey, sVal, sContext)
    },

    handleSmartDocumentPresetMSSValueClicked: function (sKey, aSelectedItems, oReferencedData) {
      _handleSmartDocumentPresetMSSValueClicked(sKey, aSelectedItems, oReferencedData)
    },

    deleteSmartDocument: function (sNodeId, iLevel) {
      _deleteSmartDocument(sNodeId, iLevel);
    },

    handleTaxonomyAdded: function (oModel, sParentTaxonomyId, sViewContext) {
      _handleTaxonomyAdded(oModel, sParentTaxonomyId, sViewContext);
      _triggerChange();
    },

    handleMultiSelectSmallTaxonomyViewCrossIconClicked: function (oTaxonomy, sParentTaxonomyId, sViewContext) {
      _handleMultiSelectSmallTaxonomyViewCrossIconClicked(oTaxonomy, sParentTaxonomyId, sViewContext);
    },

    handleAllowedTypesDropdownOpened: function (sContext, sTaxonomyId) {
      _handleAllowedTypesDropdownOpened(sContext, sTaxonomyId);
    },

    handleKlassMSSCrossIconClicked: function (sId, sKey) {
      _handleKlassMSSCrossIconClicked(sId, sKey);
    },

    handleTaxonomyLoadMoreClicked: function (sContext, sTaxonomyId) {
      _handleTaxonomyLoadMoreClicked(sContext, sTaxonomyId);
    },

    handleTaxonomySearchClicked: function (sContext, sTaxonomyId, sSearchText) {
      _handleTaxonomySearchClicked(sContext, sTaxonomyId, sSearchText);
    }
  }
})();

MicroEvent.mixin(SmartDocumentStore);
export default SmartDocumentStore;