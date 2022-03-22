import CS from '../../../../../../libraries/cs';
import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import SettingUtils from './../helper/setting-utils';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import LanguageTreeProps from './../model/language-tree-config-view-props';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import { LanguageTaxonomyRequestMapping as oLanguageTaxonomyRequestMapping } from '../../tack/setting-screen-request-mapping';
import LocaleData from '../../../../../../commonmodule/tack/mock-data-for-locale-language';
import ConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import { communicator as HomeScreenCommunicator } from '../../../../store/home-screen-communicator';
import SessionStorageManager from '../../../../../../libraries/sessionstoragemanager/session-storage-manager';
import SessionStorageConstants from '../../../../../../commonmodule/tack/session-storage-constants';
import CommonUtils from '../../../../../../commonmodule/util/common-utils';
import FileImportTypes from '../../tack/coverflow-asset-type-list';
import ManageEntityStore from "./config-manage-entity-store";
import SettingScreenModuleDictionary from "../../../../../../commonmodule/tack/setting-screen-module-dictionary";

let LanguageTreeStore = (function () {

  let _triggerChange = function () {
    LanguageTreeStore.trigger('language-tree-changed');
  };

  let _generateDummyLanguage = function (sParentId, sLabel) {
    return {
      id: null,
      label: sLabel || UniqueIdentifierGenerator.generateUntitledName(),
      parentId: sParentId,
      abbreviation: '',
      localeId: '',
      isNewlyCreated: true,
      code: ""
    };
  };

  let _cancelLanguageTreeDialogClick = function () {
    let oLanguage = LanguageTreeProps.getActiveLanguage();
    if(oLanguage.parentId != -1){
      _fetchLanguage(oLanguage.parentId);
    } else {
      _fetchLanguageTree();
    }
    _triggerChange();
  };

  let _createLanguageTreeDialogClick = function () {
    let oActiveLanguage = LanguageTreeProps.getActiveLanguage();
    if(oActiveLanguage.languageClone){
      oActiveLanguage = oActiveLanguage.languageClone;
    }

    let sCode = oActiveLanguage.code;
    if(!sCode) {
      alertify.error(getTranslation().CODE_SHOULD_NOT_BE_EMPTY);
      return;
    }

    if(sCode && !SettingUtils.isValidEntityCode(sCode)) {
      alertify.error(getTranslation().PLEASE_ENTER_VALID_CODE_FOR_LANGUAGE_TAXONOMY);
      return;
    }

    let oLocale = LocaleData[oActiveLanguage.localeId[0]];
    if(!oActiveLanguage.localeId){
      alertify.error(getTranslation().PLEASE_SELECT_LOCALE);
      return;
    }

    if(!oActiveLanguage.abbreviation){
      alertify.error(getTranslation().PLEASE_ENTER_ABBREVIATION);
      return;
    }
    oActiveLanguage.dateFormat = oLocale.dateFormat;
    oActiveLanguage.numberFormat= oLocale.numberFormat;

    var oCodeToVerifyUniqueness = {
      id: oActiveLanguage.code,
      entityType: ConfigEntityTypeDictionary.ENTITY_TYPE_LANGUAGETAXONOMY
    };

    var sURL = oLanguageTaxonomyRequestMapping.CheckEntityCode;

    var oCallbackDataForUniqueness = {};
    oCallbackDataForUniqueness.functionToExecute = function(){
      delete oActiveLanguage.isCreated;
      delete oActiveLanguage['isNewlyCreated'];
      _createLanguageTaxonomyCall(oActiveLanguage);
    };

    SettingUtils.checkForUniquenessOfCodePropertyAndSendAjaxCall(oCodeToVerifyUniqueness, sURL, oCallbackDataForUniqueness);
  };

  let _createLanguageTaxonomyCall = function (oActiveLanguage) {
    SettingUtils.csPutRequest(oLanguageTaxonomyRequestMapping.CreateLanguages, {}, oActiveLanguage, successCallbackForCreateLanguage.bind(this, oActiveLanguage.parentId), failureCallBackForCreateLanguage);
  };

  let successCallbackForCreateLanguage = function (sParentId, oResponse) {
    let oCreatedLanguage = oResponse.success.entity;
    let oLanguageTree = LanguageTreeProps.getLanguageTree();
    let oParentLanguage = _getLanguageById(oLanguageTree, sParentId);

    oParentLanguage.children.push(oCreatedLanguage);
    let oLanguageValueList = LanguageTreeProps.getLanguageValueListByTypeGeneric();
    SettingUtils.addNewTreeNodesToValueList(oLanguageValueList, oParentLanguage.children , {'canDelete': true, 'canCreate': true});

    _checkAllValueDataOfLanguages(oCreatedLanguage.id, 'isActive', false, true);
    _checkAllValueDataOfLanguages(oCreatedLanguage.id, 'isChecked', false, true);
    _checkAllValueDataOfLanguages(oCreatedLanguage.id, 'isLoading', false, true);
    let oParentNode = SettingUtils.getParentNodeByChildId({children: oLanguageTree}, oCreatedLanguage.id);

    let aChildrenKeyValueToReset = [{ key: "isExpanded", value: false }, { key: "isSelected", value: false }];

    SettingUtils.applyToAllNodesBelow(aChildrenKeyValueToReset, oParentNode, oLanguageValueList, true);
    oLanguageValueList[oCreatedLanguage.id].isSelected = true;
    oLanguageValueList[oCreatedLanguage.id].isExpanded = true;

    LanguageTreeProps.setActiveLanguage(oCreatedLanguage);
    _triggerChange();
  };

  let failureCallBackForCreateLanguage = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureAttributionTaxonomyListCallback", getTranslation());
  };

  let _createLanguage = function (sNodeId) {
    let isLanguageDirty = _getIsLanguageDataDirty();
    if(isLanguageDirty){
      let oCallBackData = {};
      oCallBackData.functionToExecute = _createLanguage.bind(this, sNodeId);
      CustomActionDialogStore.showTriActionDialog(getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
          _saveLanguage.bind(this, oCallBackData),
          _discardLanguage.bind(this, oCallBackData), function () {
          });
    } else {
      let oDummyActiveLanguage = _generateDummyLanguage(sNodeId);
      LanguageTreeProps.setActiveLanguage(oDummyActiveLanguage);
      _triggerChange();
    }
  };

  let _deleteLanguage = function (sNodeId) {
    let sType =SettingScreenModuleDictionary.TRANSLATION_TREE;
    // let oCallback = {functionToExecute: deleteUnusedLanguage.bind(this, sNodeId)}
    // ManageEntityStore.handleManageEntityDialogOpenButtonClicked(sNodeId, sType, oCallback);
    deleteUnusedLanguage(sNodeId, {functionToExecute:  ManageEntityStore.handleManageEntityDialogOpenButtonClicked.bind(this, sNodeId, sType)});
  };

  let deleteUnusedLanguage = function (sNodeId, oCallback){
    //let bCanDeleteEntity = ManageEntityConfigProps.getDataForDeleteEntity();
    let sSelectedDataLanguageCode = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);
    let sSelectedUILanguageCode = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE);
    let oDeleteData = {
      ids: [sNodeId],
      dataLanguage: sSelectedDataLanguageCode,
      uiLanguage: sSelectedUILanguageCode
    };
    let OLanguageTree = LanguageTreeProps.getLanguageTree();
    let oLanguage = _getLanguageById(OLanguageTree, sNodeId);
    let sLanguageLabel = CS.getLabelOrCode(oLanguage);

    if (oLanguage.isStandard) {
      CommonUtils.showMessage(getTranslation().STANDARD_LANGUAGE_DELETION);
      return;
    }

    CustomActionDialogStore.showConfirmDialog(sLanguageLabel,
        getTranslation().DELETE_CONFIRMATION,
        function () {
          SettingUtils.csDeleteRequest(oLanguageTaxonomyRequestMapping.DeleteLanguages, {}, oDeleteData, successDeleteLanguage, failureDeleteLanguage.bind(this, oCallback));
        }, function (oEvent) {
        }, "", true);
  };

  let successDeleteLanguage = function (oResponse) {
    let aDeletedLanguages = oResponse.success;
    let oLanguageTree = LanguageTreeProps.getLanguageTree();
    let  oSelectedLanguage =LanguageTreeProps.getActiveLanguage();
    let oNewActiveNode = SettingUtils.getDefaultActiveNodeToSetOnTreeNodeDelete([oLanguageTree], oSelectedLanguage.id, aDeletedLanguages[0]);
    _removeDeletedLanguages(oLanguageTree, aDeletedLanguages[0]);
    CS.isNotEmpty(oNewActiveNode) && _fetchLanguage(oNewActiveNode.id);

    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_DELETED_SUCCESSFULLY,{entity : getTranslation().LANGUAGE}));
    _triggerChange();
  };

  let _removeDeletedLanguages =function(oLanguageTree, sId){
    SettingUtils.removeNodeById([oLanguageTree], sId);
  };

  let failureDeleteLanguage = function (oCallback, oResponse) {
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
    SettingUtils.failureCallback(oResponse, "failureAttributionTaxonomyListCallback", getTranslation());
  };

  let _makeLanguageDirty = function (oActiveLanguage) {
    if(!oActiveLanguage.languageClone && !oActiveLanguage.isNewlyCreated){
      oActiveLanguage.isDirty = true;
      oActiveLanguage.languageClone = CS.cloneDeep(oActiveLanguage);
    }
    return oActiveLanguage.languageClone || oActiveLanguage;
  };

  let _handleCommonConfigDialogValueChanged = function (sKey, sVal) {
    let oActiveLanguage = LanguageTreeProps.getActiveLanguage();
    let bIsActiveDataLanguage = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE) === oActiveLanguage.code;
    let bIsActiveUILanguageCode = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE) === oActiveLanguage.code;
    if ((bIsActiveUILanguageCode && sKey === 'isUserInterfaceLanguage') || (bIsActiveDataLanguage && sKey === 'isDataLanguage')) {
      alertify.error(getTranslation().ERROR_CANNOT_DESELECT_ACTIVE_LANGUAGE);
      return;
    }
    oActiveLanguage = _makeLanguageDirty(oActiveLanguage);

    oActiveLanguage[sKey] =  sVal;
    if(sKey === 'icon'){
      oActiveLanguage.showSelectIconDialog = false;
    }
    _triggerChange();
  };
  let _handleLocaleIdChanged = function (sKey, aSelectedItems) {
    let oActiveLanguage = LanguageTreeProps.getActiveLanguage();
    oActiveLanguage = _makeLanguageDirty(oActiveLanguage);
    if(sKey === "localeId"){
      let oLocale = CS.find(LocaleData,{id: aSelectedItems[0]});
      oActiveLanguage.numberFormat = oLocale.numberFormat;
      oActiveLanguage.dateFormat = oLocale.dateFormat;

      let sLocalForCode = oLocale.locale;
      sLocalForCode = CS.replace(sLocalForCode, '-', '_');
      oActiveLanguage.code = sLocalForCode;
    }
    oActiveLanguage[sKey] =  aSelectedItems[0];
    _triggerChange();
  };

  let _fetchLanguageTree =  function (sId) {
    let oParameters = {};
    oParameters.id = SettingUtils.getTreeRootId();
    SettingUtils.csGetRequest(oLanguageTaxonomyRequestMapping.GetLanguages, oParameters, successCallbackForFetchLanguageTree, failureCallbackForFetchLanguageTree);
  };

  let _checkAllValueDataOfLanguages = function (sId, sKey, defaultValue, value) {
    let oLanguageValueList = LanguageTreeProps.getLanguageValueListByTypeGeneric();
    CS.forEach(oLanguageValueList, function (oLanguageValue) {
      if(oLanguageValue.id != -1){
        oLanguageValue[sKey] = defaultValue;
      }
    });
    oLanguageValueList[sId][sKey] = value;
  };

  let successCallbackForFetchLanguageTree = function (oResponse) {
    let oLanguagesTree = oResponse.success.entity;
    oLanguagesTree.id = -1;
    oLanguagesTree.label = getTranslation().LANGUAGE_TREE;

    let oLanguageValueList = LanguageTreeProps.getLanguageValueListByTypeGeneric();
    SettingUtils.addNewTreeNodesToValueList(oLanguageValueList, oLanguagesTree.children, {'canDelete': true, 'canCreate': true});
    LanguageTreeProps.setLanguageTree(oLanguagesTree);
    oLanguagesTree.children[0].children = [];
    _fetchLanguage(oLanguagesTree.children[0].id);
  };

  let failureCallbackForFetchLanguageTree= function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureAttributionTaxonomyListCallback", getTranslation());
  };

  let _fetchLanguage= function (sId) {
    let isLanguageDirty = _getIsLanguageDataDirty();
    let oParameters = {};
    oParameters.id = sId;
    let oCallBackData = {};
    oCallBackData.functionToExecute = _fetchLanguage.bind(this, sId);
    if(isLanguageDirty){
      CustomActionDialogStore.showTriActionDialog(getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
          _saveLanguage.bind(this, oCallBackData),
          _discardLanguage.bind(this, oCallBackData), function () {
          });
    } else {
      SettingUtils.csGetRequest(oLanguageTaxonomyRequestMapping.GetLanguages, oParameters, successCallbackForFetchLanguage.bind(this, sId), failureCallbackForFetchLanguage);
    }
  };

  let _getLanguageById =function(oLanguageTree, sId){
    if(sId === oLanguageTree.id){
      return oLanguageTree;
    } else {
      let oFoundLanguage= null;
      CS.forEach(oLanguageTree.children, function(oChildLanguage){
        oFoundLanguage = _getLanguageById(oChildLanguage, sId);
        if(oFoundLanguage) {
          return false;
        }
      });
      return oFoundLanguage;
    }
  };

  let successCallbackForFetchLanguage = function (sParentId, oResponse) {
    let oLanguage = oResponse.success.entity;
    let oLanguageTree = LanguageTreeProps.getLanguageTree();
    let oParentLanguage = _getLanguageById(oLanguageTree, sParentId);
    oParentLanguage.children = oLanguage.children;
    let oLanguageValueList = LanguageTreeProps.getLanguageValueListByTypeGeneric();
    SettingUtils.addNewTreeNodesToValueList(oLanguageValueList, oLanguage.children, {'canDelete': true, 'canCreate': true});

    _checkAllValueDataOfLanguages(sParentId, 'isActive', false, true);
    _checkAllValueDataOfLanguages(sParentId, 'isChecked', false, true);
    _checkAllValueDataOfLanguages(sParentId, 'isLoading', false, true);
    let oParentNode = SettingUtils.getParentNodeByChildId({children: oLanguageTree}, oLanguage.id);

    let aChildrenKeyValueToReset = [{ key: "isExpanded", value: false }, { key: "isSelected", value: false }];

    SettingUtils.applyToAllNodesBelow(aChildrenKeyValueToReset, oParentNode, oLanguageValueList, true);
    oLanguageValueList[oLanguage.id].isSelected = true;
    oLanguageValueList[oLanguage.id].isExpanded = true;

    LanguageTreeProps.setActiveLanguage(oLanguage);
    _triggerChange();
  };

  let failureCallbackForFetchLanguage = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureAttributionTaxonomyListCallback", getTranslation());
  };

  let failureCallbackForGetCurrentDefaultLanguage = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureCallbackForGetCurrentDefaultLanguage", getTranslation());
  };

  let _handleLanguageTreeSnackBarButtonClicked = function (sButton) {
    if(sButton == "save"){
      _saveLanguage();
    }else {
      _discardLanguage();
    }
  };

  let _getIsLanguageDataDirty = function () {
    let oLanguage = LanguageTreeProps.getActiveLanguage();
    return !!oLanguage.languageClone;
  };

  let _saveLanguage= function(oCallBackData){
    let oActiveLanguage = LanguageTreeProps.getActiveLanguage();
    oActiveLanguage = oActiveLanguage.languageClone;
    if(!oActiveLanguage.label) {
      alertify.error(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return;
    }

    if(!oActiveLanguage.localeId){
      alertify.error(getTranslation().PLEASE_SELECT_LOCALE_ID);
      return;
    }

    if(!oActiveLanguage.abbreviation){
      alertify.error(getTranslation().PLEASE_ENTER_ABBREVIATION);
      return;
    }

    //This method remove keys from request data.
    SettingUtils.removeKeysFromRequestData(oActiveLanguage, ['iconKey']);
    SettingUtils.csPostRequest(oLanguageTaxonomyRequestMapping.SaveLanguages, {}, oActiveLanguage, successSaveLanguage.bind(this, oCallBackData), failureSaveLanguage);
  };

  let successSaveLanguage = function (oCallBackData, oResponse) {
    let oLanguage = oResponse.success.entity;

    /** update value list **/
    let oLanguageValueList = LanguageTreeProps.getLanguageValueListByTypeGeneric();
    oLanguageValueList[oLanguage.id].label = oLanguage.label;

    /** update tree **/
    let oLanguageTree = LanguageTreeProps.getLanguageTree();
    let oNode = SettingUtils.getNodeFromTreeListById(oLanguageTree.children, oLanguage.id);
    CS.assign(oNode, oLanguage);
    let oActiveLanguage = LanguageTreeProps.getActiveLanguage();
    if (oActiveLanguage.iconKey !== oLanguage.iconKey || oActiveLanguage.dateFormat !== oLanguage.dateFormat ||
        oActiveLanguage.numberFormat !== oLanguage.numberFormat) {
      HomeScreenCommunicator.handleLanguageInfoChanged(oLanguage);
    }
    LanguageTreeProps.setActiveLanguage(oLanguage);
    alertify.success(getTranslation().SUCCESSFULLY_SAVED);

    if(!CS.isEmpty(oCallBackData) && oCallBackData.functionToExecute){
      oCallBackData.functionToExecute();
    }
    _triggerChange();
  };

  let _discardLanguage = function (oCallBackData) {
    let oActiveLanguage = LanguageTreeProps.getActiveLanguage();
    delete oActiveLanguage['languageClone'];

    alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
    if(!CS.isEmpty(oCallBackData)){
      oCallBackData.functionToExecute();
    }
    _triggerChange();
  };

  let failureSaveLanguage = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureAttributionTaxonomyListCallback", getTranslation());
  };

  let _getIsValidFileTypes = function (oFile) {
    // var aValidTypes = AllowedMediaTypeDictionary[sAssetType];
    var sTypeRegex = FileImportTypes.map(function (sType) {
      return '\\' + sType + '$';
    }).join('|');
    return oFile.name.match(new RegExp(sTypeRegex, 'i'));
  };

  let uploadFileImport = function (oImportExcel, oCallback) {
    return SettingUtils.csCustomPostRequest(oImportExcel.sUrl, oImportExcel.data,
        successUploadFileImport.bind(this, oCallback), failureUploadFileImport, false);
  };

  let successUploadFileImport = function (oResponse) {
    alertify.success(getTranslation().IMPORT_IN_PROGRESS);
    return true;
  };

  let failureUploadFileImport = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureUploadFileImport", getTranslation());
    return false;
  };

  let _handleLanguageTreeConfigImportButtonClicked = function (aFiles, oImportExcel) {
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
              data.append("importType", "entity");
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

  let _handleLanguageTreeConfigExportButtonClicked = function (oSelectiveExport) {
    return SettingUtils.csPostRequest(oSelectiveExport.sUrl, {}, oSelectiveExport.oPostRequest, successExportFile, failureExportFile);
  };

  let successExportFile = function (oResponse) {
    alertify.success(getTranslation().EXPORT_IN_PROGRESS);
    SettingUtils.downloadFromByteStream(oResponse.success.fileStream, oResponse.success.fileName);
    return true;
  };

  let failureExportFile = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureExportFile", getTranslation());
    return false;
  };

  let _handleLanguageManageEntityButtonClicked = function () {
    let oActiveLanguage = LanguageTreeProps.getActiveLanguage();
    let aSelectedIds = oActiveLanguage.id;
    let sType =SettingScreenModuleDictionary.TRANSLATION_TREE;

    if (CS.isEmpty(aSelectedIds)) {
      alertify.message(getTranslation().NOTHING_SELECTED_TO_DELETE);
      return
    }
    else {
      ManageEntityStore.handleManageEntityDialogOpenButtonClicked(aSelectedIds, sType);
    }
  };

  return {
    createLanguage: function (sNodeId) {
      _createLanguage(sNodeId);
    },

    deleteLanguage: function (sNodeId) {
      _deleteLanguage(sNodeId);
    },

    cancelLanguageTreeDialogClick: function () {
      _cancelLanguageTreeDialogClick();
    },

    handleLanguageManageEntityButtonClicked: function () {
      _handleLanguageManageEntityButtonClicked();
    },

    createLanguageTreeDialogClick: function () {
      _createLanguageTreeDialogClick();
    },

    getDefaultLanguageConfirmationLabel: function (oCurrentDefaultLanguage) {
      let oActiveLanguage = LanguageTreeProps.getActiveLanguage();

      return (
          CommonUtils.getParsedString(
              getTranslation().CHANGE_DEFAULT_LANGUAGE_CONFORMATION,
              {
                fromLanguage: CS.getLabelOrCode(oCurrentDefaultLanguage),
                toLanguage: CS.getLabelOrCode(oActiveLanguage)
              }
          )
      );
    },

    handleCommonConfigDialogValueChanged : function(sKey, sVal){
      if (sKey === "isDefaultLanguage" && sVal) {
        let fSuccess = (response) => {
          CustomActionDialogStore.showConfirmDialog(this.getDefaultLanguageConfirmationLabel(response.success), "",
            function () {
              _handleCommonConfigDialogValueChanged(sKey, sVal);
              _saveLanguage();
            },
            function (oEvent) {
            });
        }

        SettingUtils.csGetRequest(oLanguageTaxonomyRequestMapping.GetCurrentDefaultLanguage, null, fSuccess, failureCallbackForGetCurrentDefaultLanguage);
      } else {
        _handleCommonConfigDialogValueChanged(sKey, sVal);
      }
    },

    handleLocaleIdChanged : function(sKey, sVal){
      _handleLocaleIdChanged(sKey, sVal);
    },

    fetchLanguageTree: function (sId) {
      _fetchLanguageTree(sId);
    },

    handleLanguageTreeSnackBarButtonClicked: function (sButton) {
      _handleLanguageTreeSnackBarButtonClicked(sButton);
    },

    fetchLanguage: function (sId) {
      _fetchLanguage(sId);
    },

    getIsLanguageDataDirty: function () {
      return _getIsLanguageDataDirty();
    },

    saveLanguage: function (oCallbackData) {
      _saveLanguage(oCallbackData);
    },

    discardLanguage: function (oCallbackData) {
      _discardLanguage(oCallbackData);
    },

    handleLanguageTreeConfigImportButtonClicked: function (aFiles, oImportExcel) {
      _handleLanguageTreeConfigImportButtonClicked(aFiles, oImportExcel);
    },

    handleLanguageTreeConfigExportButtonClicked: function (oSelectiveExport) {
      _handleLanguageTreeConfigExportButtonClicked(oSelectiveExport);
    },
  }
})();

MicroEvent.mixin(LanguageTreeStore);
export default LanguageTreeStore;
