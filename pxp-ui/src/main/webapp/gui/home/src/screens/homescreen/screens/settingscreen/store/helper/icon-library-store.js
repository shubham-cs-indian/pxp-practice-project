import SettingUtils from "./setting-utils";
import {getTranslations} from '../../../../../../commonmodule/store/helper/translation-manager.js';
import {IconLibraryRequestMapping as oIconLibraryRequestMapping} from '../../tack/setting-screen-request-mapping';
import IconLibraryProps from "../model/icon-library-props";
import MicroEvent from "../../../../../../libraries/microevent/MicroEvent";
import CS from "../../../../../../libraries/cs";
import alertify from "../../../../../../commonmodule/store/custom-alertify-store";
import {UploadRequestMapping as oUploadRequestMapping} from '../../tack/setting-screen-request-mapping';
import CoverflowAssetTypeList from "../../../contentscreen/tack/coverflow-asset-type-list";
import ManageEntityStore from "./config-manage-entity-store";
import ManageEntityConfigProps from "../model/manage-entity-config-props";
import IconLibrarySelectIconDialogProps from "../model/icon-library-select-icon-dialog-props";
import {getTranslations as getTranslation} from "../../../../../../commonmodule/store/helper/translation-manager";
import ConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import AttributeProps from "../model/attribute-config-view-props";
import TagProps from "../model/tag-config-view-props";
import TasksProps from "../model/tasks-config-view-props";

let IconLibraryStore = (function () {

  let _triggerChange = function () {
    IconLibraryStore.trigger('icon-library-config-changed');
  }

  let _resetIconLibraryProps = function () {
    IconLibraryProps.reset();
  }

  let _fetchIconLibraryScreen = function (sConfigContext) {
    let oPostData = SettingUtils.getEntityListViewLoadMorePostData(IconLibraryProps, false);
    let sConfigScreenViewName = SettingUtils.getConfigScreenViewName();
    let oPaginationData = (sConfigScreenViewName == ConfigEntityTypeDictionary.ENTITY_TYPE_ICON_LIBRARY)
                          ? IconLibraryProps.getPaginationData() : IconLibraryProps.getSelectDialogPaginationData();
    oPostData.from = oPaginationData.from;
    oPostData.size = oPaginationData.size;
    oPostData.idToExclude = _fillIdToExcludeForIconLibrary(sConfigContext);
    return SettingUtils.csPostRequest(oIconLibraryRequestMapping.GetIconLibrary, {}, oPostData, successFetchIconLibraryScreen, failureFetchIconLibraryScreen);
  };

  let _fillIdToExcludeForIconLibrary = function (sConfigContext) {
    let sIdToExclude = "";
    let oComponentProps = SettingUtils.getComponentProps();
    switch (sConfigContext) {
      case 'class':
        let oClassConfigViewProps = oComponentProps.classConfigView;
        let oActiveClass = oClassConfigViewProps.getActiveClass();
        sIdToExclude = CS.isNotEmpty(oActiveClass) && CS.isNotEmpty(oActiveClass.icon) ? oActiveClass.icon : "";
        break;
      case 'propertycollection':
        let oPropertyCollectionConfigView = oComponentProps.propertyCollectionConfigView;
        let oActivePropertyCollection = oPropertyCollectionConfigView.getActivePropertyCollection();
        sIdToExclude = CS.isNotEmpty(oActivePropertyCollection) && CS.isNotEmpty(oActivePropertyCollection.icon)
                       ? oActivePropertyCollection.icon : "";
        break;
      case 'attributionTaxonomyDetail':
        let oAttributionTaxonomyConfigView = oComponentProps.attributionTaxonomyConfigView;
        let oActiveTaxonomy = oAttributionTaxonomyConfigView.getActiveDetailedTaxonomy();
        sIdToExclude = CS.isNotEmpty(oActiveTaxonomy) && CS.isNotEmpty(oActiveTaxonomy.icon)
                       ? oActiveTaxonomy.icon : "";
        break;
      case 'context':
        let oAppData = SettingUtils.getAppData();
        let oContextMasterList = oAppData.getContextList();
        let oContextConfigProps = oComponentProps.contextConfigView;
        let sActiveContextId = oContextConfigProps.getActiveContext().id;
        let oActiveContext = oContextMasterList[sActiveContextId] || {};
        sIdToExclude = CS.isNotEmpty(oActiveContext) && CS.isNotEmpty(oActiveContext.icon)
                       ? oActiveContext.icon : "";
        break;
      case 'organisation':
        let oOrganisationConfigView = oComponentProps.organisationConfigView;
        let oActiveOrganisation = oOrganisationConfigView.getActiveOrganisation();
        sIdToExclude = CS.isNotEmpty(oActiveOrganisation) && CS.isNotEmpty(oActiveOrganisation.icon)
                       ? oActiveOrganisation.icon : "";
        break;
      case 'relationship':
        let oRelationshipView = oComponentProps.relationshipView;
        let aRelationshipGridData = oRelationshipView.getRelationshipGridData();
        sIdToExclude = _getIdToExcludeForGrid(aRelationshipGridData);
        break;
      case 'technicalImageContext':
      case 'embeddedKlassContext':
      case "languageKlassContext":
        let oClassConfigView = oComponentProps.classConfigView;
        let oClassContextDialogProps = oClassConfigView.getClassContextDialogProps();
        sIdToExclude = CS.isNotEmpty(oClassContextDialogProps) && CS.isNotEmpty(oClassContextDialogProps.icon)
                       ? oClassContextDialogProps.icon : "";
        break;
      case 'languageTree':
        let oLanguageTreeProps = oComponentProps.languageTreeConfigView;
        let oActiveLanguage = oLanguageTreeProps.getActiveLanguage();
        sIdToExclude = CS.isNotEmpty(oActiveLanguage) && CS.isNotEmpty(oActiveLanguage.icon)
                       ? oActiveLanguage.icon : "";
        break;
      case 'context_variant' :
        let oContextGridData = SettingUtils.getAppData().getContextList();
        sIdToExclude = _getIdToExcludeForGrid(oContextGridData);
        break;
      case 'attribute' :
        let oAttributeGridData = AttributeProps.getAttributeGridData();
        sIdToExclude = _getIdToExcludeForGrid(oAttributeGridData);
        break;
      case 'tag' :
        let sSplitter = SettingUtils.getSplitter();
        let aTagGridData = TagProps.getTagGridData();
        let sElementId = IconLibrarySelectIconDialogProps.getContentId();
        let sPathToRoot = IconLibrarySelectIconDialogProps.getPathToRoot();
        if (sElementId == sPathToRoot) {
          sIdToExclude = _getIdToExcludeForGrid(aTagGridData);
        } else {
          let sParentId = sPathToRoot.split(sSplitter)[0];
          let oActiveParentElement = CS.find(aTagGridData, {id: sParentId});
          let oActiveElement = CS.find(oActiveParentElement.children, {id: sElementId});
          sIdToExclude = CS.isNotEmpty(oActiveElement) && CS.isNotEmpty(oActiveElement.icon)
                         ? oActiveElement.icon : "";
        }
        break;
      case 'relationship':
        let oRelationshipGridData = oComponentProps.relationshipView.getRelationshipGridData();
        sIdToExclude = _getIdToExcludeForGrid(oRelationshipGridData);
        break;
      case 'end_point':
        let oEndPointGridData = oComponentProps.profileConfigView.getEndpointGridData();
        sIdToExclude = _getIdToExcludeForGrid(oEndPointGridData);
        break;
      case 'tabs':
        let oTabGridData = oComponentProps.tabsConfigView.getTabList();
        sIdToExclude = _getIdToExcludeForGrid(oTabGridData);
        break;
      case 'task':
        let oTaskData = TasksProps.getTaskGridData();
        sIdToExclude = _getIdToExcludeForGrid(oTaskData);
        break;
    }

    return sIdToExclude;
  };

  let _getIdToExcludeForGrid = function (aGridData) {
    let sElementId = IconLibrarySelectIconDialogProps.getContentId();
    let oActiveElement = CS.find(aGridData, {id: sElementId});
    let sIdToExclude = CS.isNotEmpty(oActiveElement) && CS.isNotEmpty(oActiveElement.icon)
                   ? oActiveElement.icon : "";

    return sIdToExclude;
  };

  let successFetchIconLibraryScreen = function (oResponse) {
    oResponse = oResponse.success;
    IconLibraryProps.setIcons(oResponse.icons);
    IconLibraryProps.setTotalCount(oResponse.totalCount);
    _triggerChange();
  }

  let failureFetchIconLibraryScreen = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureIconLibraryCallback", getTranslations())
  }

  let _handleIconElementCheckboxClicked = function (aSelectedIconIds, bIsSelectAllClicked) {
    if (bIsSelectAllClicked) {
      if (CS.isEmpty(IconLibraryProps.getSelectedIconIds())) {
        let aIconsList = IconLibraryProps.getIcons();
        CS.forEach(aIconsList, function (oIcon) {
          IconLibraryProps.getSelectedIconIds().push(oIcon.id);
        });
      } else {
        IconLibraryProps.setSelectedIconIds([]);
      }
    } else {
      IconLibraryProps.setSelectedIconIds(CS.xor(IconLibraryProps.getSelectedIconIds(), aSelectedIconIds));
    }
  };

  let _handleIconLibraryHeaderRefreshActionClicked = function () {
    _resetIconLibraryProps();
    _fetchIconLibraryScreen();
  };

  let _handleIconLibraryHeaderEntityUsageActionClicked = function () {
    let aSelectedIconIds = IconLibraryProps.getSelectedIconIds();
    aSelectedIconIds = CS.isNotEmpty(IconLibraryProps.getActiveIconId()) ? [IconLibraryProps.getActiveIconId()] : aSelectedIconIds;
    ManageEntityStore.handleManageEntityDialogOpenButtonClicked(aSelectedIconIds, "icon");
  };

  let _handleListViewSearchOrLoadMoreClicked = function (sSearchText, bLoadMore) {
    let aIcons = IconLibraryProps.getIcons();
    SettingUtils.handleListViewSearchOrLoadMoreClicked(sSearchText, bLoadMore, aIcons, IconLibraryProps, _fetchIconLibraryScreen);
  };

  let _handleIconLibraryPaginationChanged = function (oNewPaginationData, sContext) {
    if (sContext == "selectDialog") {
      IconLibraryProps.setSelectDialogPaginationData(oNewPaginationData);
      IconLibraryProps.setSelectedIconIds([]);
    } else {
      IconLibraryProps.setPaginationData(oNewPaginationData);
    }
    _fetchIconLibraryScreen();
  };

  let _uploadIconsToServer = function (aIconsToUpload) {
    var oIconDataToUpload = new FormData();
    let oFileKeyVsCodeMap = {};
    let oFileKeyVsNameMap = {};
      CS.forEach(aIconsToUpload, function (oFile) {
        let sFileName = "file" + oFile.id;
        oIconDataToUpload.append(sFileName, oFile);
        oFileKeyVsCodeMap[sFileName] = oFile.codeName;
        oFileKeyVsNameMap[sFileName] = oFile.fileName;
      });

    oIconDataToUpload.append("fileKeyVsCodeMap", JSON.stringify(oFileKeyVsCodeMap));
    oIconDataToUpload.append("fileKeyVsNameMap", JSON.stringify(oFileKeyVsNameMap));

    return CS.customPostRequest(oUploadRequestMapping.UploadIconsToServer,
        oIconDataToUpload, _successCallBackForUploadIcons, _failureCallBackForUploadIcons, false);
  };

  let _successCallBackForUploadIcons = function () {
    IconLibraryProps.setIconLibraryUploadClicked(false);
    alertify.success(getTranslations().ICONS_SUCCESSFULLY_UPLOADED);
    _triggerChange();
  };

  let _failureCallBackForUploadIcons = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchEntitiesList", getTranslations());
  };

  let _validateIconsForDuplicateCheck = function () {
    let aIconsToUpload = IconLibraryProps.getAllActiveIcons();
    let aValidationRequestData = [];
    let aCodeName = [];
    CS.forEach(aIconsToUpload, function (oFile) {
      if (CS.isNotEmpty(oFile.codeName))
        aCodeName.push(oFile.codeName);
    });

    let oValidationRequest = {};
    oValidationRequest.codes = aCodeName;
    oValidationRequest.entityType = "Icon";
    aValidationRequestData.push(oValidationRequest);
    return CS.customPostRequest(oIconLibraryRequestMapping.ValidateIconLibrary,
        aValidationRequestData, _successCallBackForIconsValidation, _failureCallBackForIconsValidation, false);
  };

  let _successCallBackForIconsValidation = function (oResponse) {
    let aDuplicateIcons = [];
    let aFiles = IconLibraryProps.getAllActiveIcons();
    let oResponseData = oResponse.success.codeCheck.Icon;
    for (let sCodekey in oResponseData) {
      if (oResponseData[sCodekey] == false) {
        CS.forEach(aFiles, function (oFile) {
          if (oFile.codeName == sCodekey){
            oFile.status = "invalidImage";
          }
        });
        aDuplicateIcons.push(sCodekey);
      }
    }

    if (aDuplicateIcons.length == 0) {
      _uploadIconsToServer(aFiles);
    } else {
      alertify.error(getTranslations().DuplicateCodeException);
      _triggerChange();
    }
  };

  let _failureCallBackForIconsValidation = function () {
    alertify.error(getTranslations().ICON_UPLOAD_FAILED);
  };

  let _deleteIcons = function () {
    let aSelectedIconIds = IconLibraryProps.getSelectedIconIds();
    aSelectedIconIds = CS.isNotEmpty(IconLibraryProps.getActiveIconId()) ? [IconLibraryProps.getActiveIconId()] : aSelectedIconIds;
    let sURL = "config/icons";
    return SettingUtils.csDeleteRequest(sURL, {}, {ids: aSelectedIconIds}, successDeleteIconCallback, failureDeleteIconCallback);
  };

  let successDeleteIconCallback = function (oResponse) {
    let aDeletedIconIds = oResponse.success;
    let aSelectedIconIds = IconLibraryProps.getSelectedIconIds();
    CS.forEach(aDeletedIconIds, function (sIconId) {
      CS.remove(aSelectedIconIds, function (sSelectedId) {
        return sSelectedId == sIconId;
      });
    });
    _resetDeleteDialogProps();
    alertify.success(getTranslations().ICONS_SUCCESSFULLY_DELETED);
    _fetchIconLibraryScreen();
  };

  let failureDeleteIconCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureDeleteIconCallback", getTranslations());
  };

  let _resetDeleteDialogProps = function () {
    IconLibraryProps.setActiveIconId("");
    IconLibraryProps.setIsUsageDeleteDialog(false);
    ManageEntityConfigProps.setIsEntityDialogOpen(false);
    ManageEntityConfigProps.setIsDelete(false);
  };

  let _deleteIconElement = function (sContentId) {
    IconLibraryProps.setActiveIconId(sContentId);
    IconLibraryProps.setIsUsageDeleteDialog(true);
    ManageEntityConfigProps.setIsDelete(true);
    _handleIconLibraryHeaderEntityUsageActionClicked();
  };

  let _handleDialogListRemoveButtonClicked = function (iIconId) {
    let sDeletedIconCode = "";
    let aAllActiveIcons = IconLibraryProps.getAllActiveIcons();
    CS.remove(aAllActiveIcons, function (oActiveIcon) {
      if (oActiveIcon.id == iIconId) {
        sDeletedIconCode = oActiveIcon.codeName;
      }
      return oActiveIcon.id == iIconId;
    });
    if (CS.isNotEmpty(sDeletedIconCode)) {
      _updateIconsStatus(sDeletedIconCode);
    }
  };

  let _handleGridIconUploadButtonClicked = function (sContentId, sPropertyId, sContext, sPathToRoot) {
    IconLibrarySelectIconDialogProps.setContentId(sContentId);
    IconLibrarySelectIconDialogProps.setShowSelectIconDialog(true);
    IconLibrarySelectIconDialogProps.setPropertyId(sPropertyId);
    IconLibrarySelectIconDialogProps.setContext(sContext);
    IconLibrarySelectIconDialogProps.setPathToRoot(sPathToRoot);
    _fetchIconLibraryScreen(sContext);
  };

  let _handleIconElementActionButtonClicked = function (sButtonId, sId, aFiles) {
    let oActiveIcon = null;
    let aIcons = IconLibraryProps.getIcons();
    switch (sButtonId) {
      case "edit":
        IconLibraryProps.setIconElementEditClicked(true);
        oActiveIcon = CS.cloneDeep(CS.find(aIcons, {id: sId}));
        IconLibraryProps.setActiveIconElement(oActiveIcon);
        break;
      case "replace":
        IconLibraryProps.setAllowedExtensions(CoverflowAssetTypeList.imageTypes);
        oActiveIcon = CS.find(aIcons, {id: sId});
        if (_validateSelectedFileForImport(aFiles[0])) {
          _handleIconElementSaveIconClicked(aFiles[0], oActiveIcon.code);
        }
        break;
      case "delete":
        _deleteIconElement(sId);
        break;

      default:
        break;

    }
    _triggerChange();
  };

  let _validateSelectedFileForImport = function (oFile) {

    if (_getIsValidAssetFileTypes(oFile)) {
      if (oFile.size > 50000) {
        alertify.error(getTranslations().FILE_SIZE_EXCEEDED);
        return false;
      }
    } else {
      alertify.error(getTranslations().FILE_FORMAT_NOT_VALID);
      return false;
    }
    return true;
  };

  let _handleIconElementSaveIconClicked = function (oFile, sCode, sName, isNameEmpty) {
    if(!isNameEmpty)
    {
      var oIconDataToSave = new FormData();
      oIconDataToSave.append("file", oFile);
      oIconDataToSave.append("iconCode", sCode);
      let fSuccess = _successCallBackForReplaceIconElement;
      let fFailure = _failureCallBackForReplaceIconElement;
      if (sName) {
        oIconDataToSave.append("iconName", sName);
        fSuccess = _successCallBackForSavedIconElement;
        fFailure = _failureCallBackForSavedIconElement;
      }

      return CS.customPostRequest(oUploadRequestMapping.SaveEditedIconElement,
          oIconDataToSave, fSuccess, fFailure, false);
    }
    else{
      alertify.message(getTranslations().ERROR_PLEASE_ENTER_VALID_NAME);
    }

  };

  let _successCallBackForSavedIconElement = function () {
    IconLibraryProps.setIconElementEditClicked(false);
    alertify.success(getTranslations().ICON_SUCCESSFULLY_SAVED);
    _fetchIconLibraryScreen();
  };

  let _failureCallBackForSavedIconElement = function () {
    IconLibraryProps.setIconElementEditClicked(false);
    alertify.error(getTranslations().ICON_SAVE_FAILED);
    _triggerChange();
  };

  let _successCallBackForReplaceIconElement = function (oResponse) {
    let oActiveIcon = {
      id: oResponse.id,
      code: oResponse.code,
      label: oResponse.label,
      createdOn: oResponse.createdOn,
      modifiedOn: oResponse.modifiedOn,
      versionId: oResponse.versionId,
      iconKey: oResponse.iconKey
    };

    IconLibraryProps.setActiveIconElement(oActiveIcon);
    alertify.success(getTranslations().ICON_SUCCESSFULLY_REPLACED);
    _fetchIconLibraryScreen();
  };

  let _failureCallBackForReplaceIconElement = function () {
    alertify.error(getTranslations().ICON_REPLACE_FAILED);
  };

  let _handleIconElementReplaceIconClicked = function (oFile, fileName) {
    IconLibraryProps.setAllowedExtensions(CoverflowAssetTypeList.imageTypes);
    if (_validateSelectedFileForImport(oFile)) {
      let oActiveIcon = IconLibraryProps.getActiveIconElement();
      oFile.src = URL.createObjectURL(oFile);
      oFile.code = oActiveIcon.code;
      oFile.label = fileName;
      oFile.id = oActiveIcon.id;
      IconLibraryProps.setActiveIconElement(oFile);
    }
    _triggerChange();
  };

  let _getIsValidAssetFileTypes = function (oFile) {
    let aAssetExtensions = IconLibraryProps.getAllowedExtensions();
    let sTypeRegex = aAssetExtensions.map(function (sType) {
      return '\\' + sType + '$';
    }).join('|');

    return oFile.name.match(new RegExp(sTypeRegex, 'i'));
  };

  let _handleDialogInputChanged = function (oFileData) {
    let bIsFileInvalid = false;
    let sSelectedIconId = oFileData.id;
    let sNewCodeOrFileName = oFileData.value;
    let sChangedField = oFileData.changedField;
    let aAllActiveIcons = IconLibraryProps.getAllActiveIcons();
    let oSelectedIconItem = CS.find(aAllActiveIcons, {id: sSelectedIconId});

    //set file as valid before checking for codeName and fileName
    oSelectedIconItem.status = "validImage";
    oSelectedIconItem.bIsDisabled = false;
    oSelectedIconItem.message = "";

    //Checking for invalid file name and code name
    CS.forEach(aAllActiveIcons, function (oIcon) {
      if (sChangedField == "codeName") {
        let sOldCodeName = oSelectedIconItem.codeName;
        if (/\s/.test(sNewCodeOrFileName)) {
          oSelectedIconItem.status = "invalidImage";
          oSelectedIconItem.message = getTranslation().PLEASE_ENTER_VALID_CODE;
          oSelectedIconItem.bIsDisabled = false;
          bIsFileInvalid = true;
        } else if (CS.isNotEmpty(oIcon.codeName) && oIcon.codeName == sNewCodeOrFileName
            && oIcon.id != sSelectedIconId) {
          oSelectedIconItem.message = getTranslation().CODE_ALREADY_EXISTS;
          oSelectedIconItem.status = "invalidImage";
          oSelectedIconItem.bIsDisabled = false;
          bIsFileInvalid = true;
        }
        oSelectedIconItem.codeName = sNewCodeOrFileName;
        !bIsFileInvalid && CS.isNotEmpty(sOldCodeName) && _updateIconsStatus(sOldCodeName);
      } else if (sChangedField == "fileName") {
        if (!/\S/.test(sNewCodeOrFileName)) {
          oSelectedIconItem.status = "invalidImage";
          oSelectedIconItem.message = getTranslation().FILE_NAME_CANNOT_BE_EMPTY;
          oSelectedIconItem.bIsDisabled = false;
          bIsFileInvalid = true;
        }
        oSelectedIconItem.fileName = sNewCodeOrFileName;
      }
      if (bIsFileInvalid) {
        return false;
      }
    });

    IconLibraryProps.setAllActiveIcons(aAllActiveIcons);
  };

  let _handleIconUploadClicked = function (aFiles) {
    let iId = 0;
    if (aFiles.length > 10) {
      alertify.message(getTranslations().MAXIMUM_ICON_UPLOAD_WARNING);
    } else {
      IconLibraryProps.setAllowedExtensions(CoverflowAssetTypeList.imageTypes);
      CS.forEach(aFiles, function (oFile) {
        let sName = oFile.name;
        oFile.id = iId;
        oFile.codeName = "";
        oFile.fileName = sName.substr(0, sName.lastIndexOf('.'));

        if (_getIsValidAssetFileTypes(oFile)) {
          if (oFile.size > 50000) {
            oFile.status = "invalidImage";
            oFile.message = getTranslation().FILE_SIZE_EXCEEDED;
            oFile.bIsDisabled = true;
          } else {
            oFile.status = "validImage";
            oFile.bIsDisabled = false;
          }
        } else {
          oFile.status = "invalidImage";
          oFile.message = getTranslation().FILE_FORMAT_NOT_VALID;
          oFile.bIsDisabled = true;
        }

        iId++;
      });

      IconLibraryProps.setIconLibraryUploadClicked(true);
      IconLibraryProps.setAllActiveIcons(Array.from(aFiles));
    }
  };

  let _handleIconElementIconNameChanged = function (sFileName) {
    let oActiveIcon = IconLibraryProps.getActiveIconElement();
    oActiveIcon.label = sFileName;
    IconLibraryProps.setActiveIconElement(oActiveIcon);
  };

  let _handleIconElementCancelIconClicked = function (sButtonId) {
    if (sButtonId === "ok") {
      IconLibraryProps.setIconElementEditClicked(false);
    } else {
      let oActiveIcon = IconLibraryProps.getActiveIconElement();
      let oIcon = CS.cloneDeep(IconLibraryProps.getIcons().find(oIcon => oIcon.id === oActiveIcon.id));
      IconLibraryProps.setActiveIconElement(oIcon);
    }
  };

  let _updateIconsStatus = function (sUpdatedIconCode) {
    let oFoundIcon = {};
    let bIsFilled = false;
    let bIsAlreadyValid = false;
    let aAllActiveIcons = IconLibraryProps.getAllActiveIcons();
    CS.forEach(aAllActiveIcons, function (oIcon) {
      if (oIcon.codeName == sUpdatedIconCode) {
        if (oIcon.status == "invalidImage") {
          if (!bIsFilled) {
            oFoundIcon = oIcon;
            bIsFilled = true;
          }
        } else {
          bIsAlreadyValid = true;
        }
      }
      if (bIsAlreadyValid) {
        return false;
      }
    });

    if (!bIsAlreadyValid) {
      oFoundIcon.message = "";
      oFoundIcon.status = "validImage";
      oFoundIcon.bIsDisabled = false;
    }
  };

  return {
      handleIconElementIconNameChanged: function(sFileName) {
        _handleIconElementIconNameChanged(sFileName);
        _triggerChange();
      },

      handleIconUploadClicked: function (aFiles) {
        _handleIconUploadClicked(aFiles);
      },

      handleDialogCancelButtonClicked: function () {
        IconLibraryProps.setIconLibraryUploadClicked(false);
        IconLibraryProps.setAllActiveIcons([]);
        _triggerChange();
      },

      handleDialogInputChanged: function (oFileData) {
        _handleDialogInputChanged(oFileData);
        _triggerChange();
      },

      handleIconElementReplaceIconClicked: function (oFile, fileName) {
        _handleIconElementReplaceIconClicked(oFile, fileName);
      },

      handleIconElementSaveIconClicked: function (oFile, sCode, sName, isNameEmpty) {
        _handleIconElementSaveIconClicked(oFile, sCode, sName, isNameEmpty);
      },

      handleIconElementCancelIconClicked: function (sButtonId) {
        _handleIconElementCancelIconClicked(sButtonId);
        _triggerChange();
      },

      deleteIconElement: function (sContentId) {
        _deleteIconElement(sContentId).then(_triggerChange);
      },

      deleteIcons: function () {
        _deleteIcons().then(_triggerChange);
      },

      resetDeleteDialogProps: function () {
        _resetDeleteDialogProps();
        _triggerChange();
      },

      handleDialogSaveButtonClicked: function () {
        _validateIconsForDuplicateCheck();
      },

      fetchIconLibraryScreen: function (sConfigContext = "") {
        _fetchIconLibraryScreen(sConfigContext);
      },

      resetIconLibraryProps: function () {
        _resetIconLibraryProps();
      },

      handleIconElementCheckboxClicked: function (aSelectedIconIds, bIsSelectAllClicked) {
        _handleIconElementCheckboxClicked(aSelectedIconIds, bIsSelectAllClicked);
      },

      handleIconLibraryHeaderRefreshActionClicked: function () {
        _handleIconLibraryHeaderRefreshActionClicked();
      },

      handleIconLibraryHeaderEntityUsageActionClicked: function () {
        _handleIconLibraryHeaderEntityUsageActionClicked();
      },

      handleListViewSearchOrLoadMoreClicked: function (sSearchText, bLoadMore) {
        _handleListViewSearchOrLoadMoreClicked(sSearchText, bLoadMore);
      },

      handleIconLibraryPaginationChanged: function (oNewPaginationData, sContext) {
        _handleIconLibraryPaginationChanged(oNewPaginationData, sContext);
      },

      handleDialogListRemoveButtonClicked: function (iIconId) {
      _handleDialogListRemoveButtonClicked(iIconId);
      _triggerChange();
    },

      handleGridIconUploadButtonClicked: function (sContentId, sPropertyId, sContext, sPathToRoot) {
        _handleGridIconUploadButtonClicked(sContentId, sPropertyId, sContext, sPathToRoot);
      },

      handleIconElementActionButtonClicked: function (sButtonId, sId, aFiles) {
        _handleIconElementActionButtonClicked(sButtonId, sId, aFiles);
      },
    }
  })
();

MicroEvent.mixin(IconLibraryStore);

export default IconLibraryStore;
