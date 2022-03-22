
import CS from '../../../../../../libraries/cs';

import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import Exception from '../../../../../../libraries/exceptionhandling/exception.js';
import MethodTracker from '../../../../../../libraries/methodtracker/method-tracker';
import { RuleListRequestMapping } from '../../tack/setting-screen-request-mapping';
import ConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import RuleListProps from './../model/rule-list-config-view-props';
import SettingUtils from './../helper/setting-utils';
import ExceptionLogger from '../../../../../../libraries/exceptionhandling/exception-logger';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import {getTranslations as getTranslation} from '../../../../../../commonmodule/store/helper/translation-manager.js';
import ManageEntityStore from "./config-manage-entity-store";

var trackMe = MethodTracker.getTracker('role-store');

var RuleListStore = (function () {

  var _triggerChange = function () {
    RuleListStore.trigger('rule-list-changed');
  };

  var successCreateRuleListCallback = function (oResponse) {
    var oAppData = SettingUtils.getAppData();
    var aRuleList = oAppData.getListOfRuleList();
    var oRuleListValueProps = RuleListProps.getRuleListValuesList();
    var oNewRuleList = oResponse.success;
    oRuleListValueProps[oNewRuleList.id] = _getDefaultListProps(oNewRuleList);
    aRuleList.push(oNewRuleList);

    _selectRuleListById(oNewRuleList.id);
    RuleListProps.setActiveRuleList(oNewRuleList);
    SettingUtils.getComponentProps().screen.setListItemCreationStatus(true);
    RuleListProps.setRuleListScreenLockStatus(false);
    alertify.success(getTranslation().RULE_LIST_CREATED_SUCCESSFULLY);
    _triggerChange();

  };

  var failureCreateRuleListCallback = function (oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      alertify.error(Exception.getMessage(oResponse, getTranslation()), 0);
    } else {
      ExceptionLogger.error("successCreateRuleListCallback: Something went wrong");
    }
    ExceptionLogger.log(oResponse);
  };

  var successFetchListOfRuleListCallback = function (bLoadMore, oResponse) {
    oResponse = oResponse.success;
    let aListOfRuleList = oResponse.list;
    let iCount = oResponse.count;
    let oAppData = SettingUtils.getAppData();

    if (bLoadMore) {
      var oRuleListMap = oAppData.getListOfRuleList();
      aListOfRuleList = oRuleListMap.concat(aListOfRuleList);
    }


    RuleListProps.setRuleListValuesList({});
    oAppData.setListOfRuleList([]);
    var oRuleValueList = RuleListProps.getRuleListValuesList();
    oAppData.setListOfRuleList(aListOfRuleList);
    CS.forEach(aListOfRuleList, function (oRuleList) {
      oRuleValueList[oRuleList.id] = _getDefaultListProps(oRuleList);
    });
    RuleListProps.setShowLoadMore(CS.size(aListOfRuleList) !== iCount);
    if (!CS.isEmpty(aListOfRuleList)) {
      _fetchRuleListById(aListOfRuleList[0].id);
    }
    else {
      RuleListProps.setActiveRuleList({});
      _triggerChange();
    }
  };

  var checkAndDeleteRuleListFromList = function (oResponse, sRuleListId) {
    var oRuleListValueList = RuleListProps.getRuleListValuesList();
    var oRuleListList = SettingUtils.getAppData().getListOfRuleList();
    var oSelectedRuleList = RuleListProps.getActiveRuleList();

    if(oResponse.success.message.indexOf('NOT_FOUND') >= 0 && (oSelectedRuleList || sRuleListId)){
      var sRuleListName = oRuleListList[sRuleListId || oSelectedRuleList.id].label;
      delete oRuleListValueList[sRuleListId || oSelectedRuleList.id];
      delete oRuleListList[sRuleListId || oSelectedRuleList.id];
      RuleListProps.setActiveRuleList({});
      return sRuleListName;
    }
    return null;

  };

  var failureFetchListOfRuleListCallback = function (oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      alertify.error(Exception.getMessage(oResponse, getTranslation()), 0);
    } else {
      ExceptionLogger.error("successFetchListOfRuleListCallback: Something went wrong");
    }
    ExceptionLogger.log(oResponse);
  };

  var successFetchRuleListByIdCallback = function (oCallbackData, oResponse) {
    var oAppData = SettingUtils.getAppData();
    var aRuleList = oAppData.getListOfRuleList();

    var oRuleList = oResponse.success;
    var sRuleListId = oRuleList.id;
    _selectRuleListById(sRuleListId);
    RuleListProps.setActiveRuleList(oRuleList);

    let oFoundRuleList = CS.find(aRuleList, {id: sRuleListId});
    oFoundRuleList.label = oRuleList.label;

    if(oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }

    _triggerChange();
  };

  var failureFetchRuleListByIdCallback = function (sRuleListId,oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      var sRuleName = checkAndDeleteRuleListFromList(oResponse, sRuleListId);
      alertify.error("[" + sRuleName +"] "+getTranslation().ERROR_ALREADY_DELETED, 0);
    } else {
      ExceptionLogger.error("successFetchRuleListByIdCallback: Something went wrong");
    }
    ExceptionLogger.log(oResponse);
    _triggerChange();
  };

  var successDeleteRuleListButtonClicked = function (oResponse) {
    oResponse = oResponse.success;
    if (!CS.isEmpty(oResponse.failure)) {
      CS.forEach(oResponse.failure, function (oFailedItem) {
        if (oFailedItem.message == "LIST_EXISTS_IN_RULE") {
          alertify.error(getTranslation().RULE_LIST_EXISTS_IN_RULE + ": " + oFailedItem.linkedItemLabels.join(", "), 0);
        }
      });

      return;
    }
    var aRuleListMap = SettingUtils.getAppData().getListOfRuleList();
    var oRuleListValueList = RuleListProps.getRuleListValuesList();
    let oActiveRuleList = RuleListProps.getActiveRuleList();
    let oNewActiveNode = SettingUtils.getDefaultActiveNodeToSetOnListNodeDelete(aRuleListMap, oRuleListValueList, oActiveRuleList.id, oResponse);

    CS.isNotEmpty(oNewActiveNode) && _fetchRuleListById(oNewActiveNode.id);

    RuleListProps.setActiveRuleList({});
    RuleListProps.setRuleListScreenLockStatus(false);
    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SUCCESSFULLY_DELETED , { entity : getTranslation().RULE_LIST}));
    _triggerChange();
  };

  var failureDeleteRuleListButtonClicked = function (aSelectedAndCheckedIds, oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      let configError = CS.reduce(oResponse.failure.exceptionDetails, (isConfigError, error) => {
        if (error.key === "EntityConfigurationDependencyException") {
          isConfigError = true;
        }
        return isConfigError;
      }, false);
      if (configError) {
        ManageEntityStore.handleManageEntityDialogOpenButtonClicked(aSelectedAndCheckedIds, "rulelist");
      }
    }
    else {
      ExceptionLogger.error("successDeleteRuleListButtonClicked: Something went wrong");
    }
    ExceptionLogger.log(oResponse);
  };

  var successSaveRuleListCallback = function (oCallback, oResponse) {
    var oAppData = SettingUtils.getAppData();
    var aRuleList = oAppData.getListOfRuleList();

    var oRuleList = oResponse.success;
    var sRuleListId = oRuleList.id;
    RuleListProps.setActiveRuleList(oRuleList);
    RuleListProps.setRuleListScreenLockStatus(false);

    let oFoundRuleList = CS.find(aRuleList, {id: sRuleListId});
    oFoundRuleList.label = oRuleList.label;

    if(oCallback && oCallback.functionToExecute){
      oCallback.functionToExecute();
    }

    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY,{ entity : getTranslation().RULE_LIST}));

    _triggerChange();
  };

  var failureSaveRuleListCallback = function (oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      var sRuleName = checkAndDeleteRuleListFromList(oResponse);
      alertify.error("[" + sRuleName +"] "+getTranslation().ERROR_ALREADY_DELETED, 0);
    } else {
      ExceptionLogger.error("successSaveRuleListCallback: Something went wrong");
    }
    ExceptionLogger.log(oResponse);
    _triggerChange();
  };

  var _makeActiveRuleListDirty = function () {
    var oActiveRoleList = RuleListProps.getActiveRuleList();
    if(!oActiveRoleList.clonedObject) {
      SettingUtils.makeObjectDirty(oActiveRoleList);
    }
    RuleListProps.setRuleListScreenLockStatus(true);
    return oActiveRoleList.clonedObject;
  };

  var _cancelRuleListCreation = function () {
    var oRuleListValueMap = RuleListProps.getRuleListValuesList();
    var oRuleListMap = SettingUtils.getAppData().getListOfRuleList();

    var oNewRuleListToCreate = RuleListProps.getActiveRuleList();

    delete oRuleListValueMap[oNewRuleListToCreate.id];
    delete oRuleListMap[oNewRuleListToCreate.id];
    RuleListProps.setActiveRuleList({});
    RuleListProps.setRuleListScreenLockStatus(false);
    _triggerChange();
  };

  var _createDefaultRuleListObject = function () {
    var oObj = {
      label: UniqueIdentifierGenerator.generateUntitledName(),
      code: "",
      isCreated: true,
      list:[]
    };

    RuleListProps.setActiveRuleList(oObj);
    _triggerChange();
  };

  var _createNewRuleList = function () {
    var oRuleToCreate = RuleListProps.getActiveRuleList();
    oRuleToCreate = oRuleToCreate.clonedObject || oRuleToCreate;

    if(CS.isEmpty(oRuleToCreate.label.trim())) {
      alertify.error(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return;
    }

    var oCodeToVerifyUniqueness = {
      id: oRuleToCreate.code,
      entityType: ConfigEntityTypeDictionary.ENTITY_TYPE_RULELIST
    };

    var oCallbackData = {};
    oCallbackData.functionToExecute = _createRuleListCall.bind(this, oRuleToCreate);
    var sURL = RuleListRequestMapping.CheckEntityCode;
    SettingUtils.checkForUniquenessOfCodePropertyAndSendAjaxCall(oCodeToVerifyUniqueness, sURL, oCallbackData);
  };

  var _createRuleListCall = function (oRuleToCreate) {
    SettingUtils.csPutRequest(RuleListRequestMapping.CreateRuleList, {}, oRuleToCreate, successCreateRuleListCallback, failureCreateRuleListCallback);
  };

  var _isActiveRuleListDirty = function () {
    var oActiveRuleList = RuleListProps.getActiveRuleList();
    if(!CS.isEmpty(oActiveRuleList)) {
      return !!oActiveRuleList.clonedObject;
    }
    return false;
  };

  var _getDefaultListProps = function (oRuleList) {
    return {
      isSelected: false,
      isChecked: false,
      isEditable: false
    }
  };

  var _selectRuleListById = function (sRuleListId) {
    var oRuleListValueMap = RuleListProps.getRuleListValuesList();
    CS.forEach(oRuleListValueMap, function (oValue, sId) {
      oValue.isChecked = false;
      oValue.isSelected = (sId == sRuleListId);
    })
  };

  var _fetchListOfRuleList = function (bLoadMore) {
    bLoadMore = bLoadMore || false;
    let oPostData = SettingUtils.getEntityListViewLoadMorePostData(RuleListProps, bLoadMore);
    SettingUtils.csPostRequest(RuleListRequestMapping.GetAll, {}, oPostData, successFetchListOfRuleListCallback.bind(this, bLoadMore), failureFetchListOfRuleListCallback);
  };

  var _handleListOfRuleListNodeClicked = function (sRuleListId) {
    if(_isActiveRuleListDirty()) {
      var oCallbackData = {};
      oCallbackData.functionToExecute = _fetchRuleListById.bind(this, sRuleListId);
      _saveRuleList(oCallbackData);
    } else {
      _fetchRuleListById(sRuleListId);
    }
  };

  var _fetchRuleListById = function (sRuleListId, oCallbackData) {
    var oCallback = {};
    if(oCallbackData) {
      oCallback.functionToExecute = oCallbackData.functionToExecute
    }
    SettingUtils.csGetRequest(RuleListRequestMapping.GetRuleList, {id: sRuleListId}, successFetchRuleListByIdCallback.bind(this, oCallback), failureFetchRuleListByIdCallback.bind(this,sRuleListId));
  };

  var _getRuleListScreenLockStatus = function () {
    return RuleListProps.getRuleListScreenLockStatus();
  };

  var _getSelectedAndCheckedIds = function () {
    var oRuleListValueList = RuleListProps.getRuleListValuesList();
    var aSelectedList = [];
    CS.forEach(oRuleListValueList, function (oRuleListValue, sRuleId) {
      if(oRuleListValue.isSelected || oRuleListValue.isChecked) {
        aSelectedList.push(sRuleId);
      }
    });

    return aSelectedList;
  };

  var _deleteRuleList = function () {
    var oRuleList = SettingUtils.getAppData().getListOfRuleList();
    var aSelectedAndCheckedIds = _getSelectedAndCheckedIds();
    if(CS.isEmpty(aSelectedAndCheckedIds)) {
      alertify.message(getTranslation().NOTHING_SELECTED_TO_DELETE);
      return;
    }

    var aRuleNames = [];
    CS.forEach(aSelectedAndCheckedIds, function (sRuleId) {
      let sRuleListLabel = CS.getLabelOrCode(CS.find(oRuleList, {id: sRuleId}));
      aRuleNames.push(sRuleListLabel);
    });

    CustomActionDialogStore.showListModeConfirmDialog(getTranslation().DELETE_CONFIRMATION
        , aRuleNames,
        function () {

          var oObj = {
            ids: aSelectedAndCheckedIds
          };
          SettingUtils.csDeleteRequest(RuleListRequestMapping.DeleteRuleList, {}, oObj,
              successDeleteRuleListButtonClicked,
              failureDeleteRuleListButtonClicked.bind(this, aSelectedAndCheckedIds));

        }, function (oEvent) {
        }, true);

  };

  var _handleRuleListRefreshMenuClicked = function () {
    var oActiveRuleList = RuleListProps.getActiveRuleList();
    if(_isActiveRuleListDirty()) {
      var oCallbackData = {};
      oCallbackData.functionToExecute = _fetchRuleListById.bind(this, oActiveRuleList.id);

      CustomActionDialogStore.showTriActionDialog( getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
          _saveRuleList.bind(this, oCallbackData),
          _discardUnsavedRuleList.bind(this, oCallbackData),
          function () {
          }
      );
    } else {
      if(CS.isEmpty(oActiveRuleList)) {
        _fetchListOfRuleList();
      } else {
        _fetchRuleListById(oActiveRuleList.id);
      }
    }
  };

  var _saveRuleList = function (oCallback) {
    if(_isActiveRuleListDirty()) {
      var oCurrentRuleList = RuleListProps.getCurrentRuleList();
      var oEntityValidation = _validateEntities([oCurrentRuleList]);

      if (!oEntityValidation.nameValidation) {
        alertify.error(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME, 0);
        return;
      }

      var oCallbackData = {};
      CS.assign(oCallbackData, oCallback);

      SettingUtils.csPostRequest(RuleListRequestMapping.SaveRuleList, {}, oCurrentRuleList, successSaveRuleListCallback.bind(this, oCallbackData), failureSaveRuleListCallback);

    }else {
      alertify.message(getTranslation().NOTHING_CHANGED_TO_SAVE);
    }
  };

  var _discardUnsavedRuleList = function (oCallbackData) {
    var bScreenLockStatus = _getRuleListScreenLockStatus();
    if(!bScreenLockStatus) {
      alertify.message(getTranslation().NO_CHANGE_FOUND_TO_DISCARD);
      return;
    }

    var oActiveRuleList = RuleListProps.getActiveRuleList();
    if (!CS.isEmpty(oActiveRuleList)) {
      if (oActiveRuleList.isDirty || oActiveRuleList.clonedObject) {
        delete oActiveRuleList.clonedObject;
        delete oActiveRuleList.isDirty;
        alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
      }
      RuleListProps.setRuleListScreenLockStatus(false);
      if (oCallbackData.functionToExecute) {
        oCallbackData.functionToExecute();
      }
      _triggerChange();

    } else {
      alertify.message(getTranslation().NO_CHANGE_FOUND_TO_DISCARD);
    }
  };

  var _getCurrentRuleList = function () {
    let oActiveRuleList = RuleListProps.getActiveRuleList();
    return (oActiveRuleList.clonedObject) ? oActiveRuleList.clonedObject : oActiveRuleList;
  }

  var _handleEditBlackListItem = function (iIndex, sValue) {
    let oCurrentRuleList = _getCurrentRuleList();
    let aBlackListWords = oCurrentRuleList.list;
    if (aBlackListWords[iIndex] === sValue) {
      return false;
    }
    if (CS.includes(aBlackListWords, sValue)) {
      alertify.error(getTranslation().WORD_ALREADY_EXIST);
      return false;
    }
    if (CS.isEmpty(sValue)) {
      alertify.error(getTranslation().WORD_CANNOT_BE_EMPTY);
      return false;
    }

    var oActiveRuleList = _makeActiveRuleListDirty();
    var aBlackList = oActiveRuleList.list;
    aBlackList[iIndex] = sValue;
  };

  var _handleAddNewBlackListItem = function (sValue) {
    var oActiveRuleList = _makeActiveRuleListDirty();
    var aBlackList = oActiveRuleList.list;
    if (CS.includes(aBlackList, sValue)) {
      alertify.error(getTranslation().WORD_ALREADY_EXIST);
      return false;
    }
    aBlackList.push(sValue);
    oActiveRuleList.list = aBlackList;
    return true;
  };

  var _handleRemoveBlackListItem = function (iIndex) {
    var oActiveRuleList = _makeActiveRuleListDirty();
    var aBlackList = oActiveRuleList.list;
    aBlackList.splice(iIndex, 1);
  };

  var _handleRuleListLabelChanged = function(sNewValue){
    var oActiveRuleList = _makeActiveRuleListDirty();
    oActiveRuleList.label = sNewValue;
  };

  var _validateEntity = function (oEntity) {
    trackMe('_validateEntity');
    var oAppData = SettingUtils.getAppData();
    var oMapOfMasterRuleList = oAppData.getListOfRuleList();
    var aEntityList = [];
    CS.forEach(oMapOfMasterRuleList, function (oMasterRule) {
      aEntityList.push(oMasterRule);
    });

    var sEntityName = oEntity.label;

    var bNameValidation = sEntityName.trim() != "";
    var bDuplicateNameValidation = true;

    var sContentId = oEntity.id;
    var aEntityWithSameName = CS.filter(aEntityList, {'label': oEntity.label});

    CS.forEach(aEntityWithSameName, function (oContentWithSameName) {
      if (oContentWithSameName.id != sContentId) {
        bDuplicateNameValidation = false;
      }
    });

    return {
      nameValidation: bNameValidation,
      duplicateNameValidation: bDuplicateNameValidation
    };
  };

  var _validateEntities = function (aEntity) {
    trackMe('_validateContents');

    var oRes = {
      entityWithBlankNames: [],
      entityWithDuplicateNames: [],
      nameValidation: true,
      duplicateNameValidation: true
    };

    CS.forEach(aEntity, function (oEntity) {
      var sContentId = oEntity.id;
      var oValidation = oEntity.clonedObject ? _validateEntity(oEntity.clonedObject) : _validateEntity(oEntity);
      oRes.nameValidation = oRes.nameValidation && oValidation.nameValidation;
      oRes.duplicateNameValidation = oRes.duplicateNameValidation && oValidation.duplicateNameValidation;

      if (!oValidation.nameValidation) {
        oRes.entityWithBlankNames.push(sContentId);
      }

    });

    return oRes;
  };

  let _handleRuleListManageEntityButtonClicked = function () {
    let aSelectedIds = _getSelectedAndCheckedIds();

    if (CS.isEmpty(aSelectedIds)) {
      alertify.message(getTranslation().NOTHING_SELECTED);
      return;
    }
    else {
      ManageEntityStore.handleManageEntityDialogOpenButtonClicked(aSelectedIds, "rulelist");
    }
  };

  var _getRuleListDetails = function () {
    var oActiveRuleList = RuleListProps.getActiveRuleList();
    _fetchRuleListById(oActiveRuleList.id, {});
  };

  let _handleListViewSearchOrLoadMoreClicked = function (sSearchText, bLoadMore) {
    var oAppData = SettingUtils.getAppData();
    let oRuleList = oAppData.getListOfRuleList();
    SettingUtils.handleListViewSearchOrLoadMoreClicked(sSearchText, bLoadMore, oRuleList, RuleListProps, _fetchListOfRuleList);
  };

  /*****************
   * PUBLIC API's
   * **************/
  return {
    getRuleListScreenLockStatus: function () {
      return _getRuleListScreenLockStatus();
    },

    createRuleList: function () {
      _createNewRuleList();
    },

    cancelRuleListCreation: function () {
      _cancelRuleListCreation();
    },

    createDefaultRuleListObject: function () {
      _createDefaultRuleListObject();
    },

    fetchListOfRuleList: function () {
      _fetchListOfRuleList();
    },

    handleListOfRuleListNodeClicked: function (oModel) {
      _handleListOfRuleListNodeClicked(oModel.id);
    },

    deleteRuleList: function () {
      _deleteRuleList();
    },

    handleRuleListRefreshMenuClicked: function () {
      _handleRuleListRefreshMenuClicked();
    },

    saveRuleList: function (oCallbackData) {
      _saveRuleList(oCallbackData);
    },

    discardUnsavedRuleList: function (oCallbackData) {
      _discardUnsavedRuleList(oCallbackData);
    },

    handleEditBlackListItem: function (iIndex, sValue) {
      _handleEditBlackListItem(iIndex, sValue);
      _triggerChange();
    },

    handleAddNewBlackListItem: function (sValue) {
      if (_handleAddNewBlackListItem(sValue)) {
        _triggerChange();
      }
    },

    handleRemoveBlackListItem: function (sIndex) {
      _handleRemoveBlackListItem(sIndex);
      _triggerChange();
    },

    handleRuleListLabelChanged: function (sNewValue) {
      _handleRuleListLabelChanged(sNewValue);
      _triggerChange();
    },

    handleRuleListManageEntityButtonClicked: function () {
      _handleRuleListManageEntityButtonClicked();
    },

    handleRuleListValueChanged: function (sKey, sValue) {
      var oActiveRuleList = _makeActiveRuleListDirty();
      oActiveRuleList[sKey] = sValue;
      _triggerChange();
    },

    getRuleListDetails: function () {
      _getRuleListDetails();
    },

    handleRuleListDialogButtonClicked: function (sButtonId) {
      if(sButtonId === "create") {
        RuleListStore.createRuleList();
      } else {
        RuleListStore.cancelRuleListCreation();
      }
    },

    handleListViewSearchOrLoadMoreClicked: function (sSearchText, bLoadMore) {
      _handleListViewSearchOrLoadMoreClicked(sSearchText, bLoadMore);
    },

    reset: function () {
      RuleListProps.resetPaginationData();
      RuleListProps.setActiveRuleList({});
    },

  }
})();

MicroEvent.mixin(RuleListStore);

export default RuleListStore;
