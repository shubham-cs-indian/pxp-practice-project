import CS from '../../../../../../libraries/cs';
import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import Exception from '../../../../../../libraries/exceptionhandling/exception.js';
import { DataGovernanceTasksRequestMapping as oDataGovernanceTasksRequestMapping } from '../../tack/setting-screen-request-mapping';
import DataGovernanceTasksConfigViewProps from './../model/data-governance-tasks-config-view-props';
import SettingUtils from './../helper/setting-utils';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import TaskTypeDictionary from '../../../../../../commonmodule/tack/task-type-dictionary';
import ConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import GridViewContexts from '../.././../../../../commonmodule/tack/grid-view-contexts';
import DataGovernanceTaskConfigGridViewSkeleton from '../../tack/data-governance-tasks-config-grid-view-skeleton';
import SettingScreenProps from './../model/setting-screen-props';
import MockColors from '../../tack/mock/mock-data-for-tag-colors';
import { gridViewPropertyTypes as oGridViewPropertyTypes } from '../../../../../../viewlibraries/tack/view-library-constants';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';

let DataGovernanceTasksStore = (function () {

  let _triggerChange = function () {
    DataGovernanceTasksStore.trigger('data-governance-tasks-changed');
  };

  let _getDataGovernanceTasksScreenLockStatus = function () {
    return DataGovernanceTasksConfigViewProps.getDataGovernanceTasksScreenLockStatus();
  };

  let _setDataGovernanceTasksScreenLockStatus = function (bLockStatus) {
    return DataGovernanceTasksConfigViewProps.setDataGovernanceTasksScreenLockStatus(bLockStatus);
  };

  let _setValueList = function (aTasksList, oNodeInTasksList) {
    CS.forEach(aTasksList, function (oListNode) {
      let oListItem = {};
      oListItem.id = oListNode.id;
      oListItem.name = oListNode.label;
      oListItem.isChecked = false;
      oListItem.isEditable = false;
      oListItem.isSelected = false;

      oNodeInTasksList[oListNode.id] = oListItem;
    });
  };

  let successFetchDataGovernanceTasksList = function (oResponse) {
    let oAppData = SettingUtils.getAppData();
    oAppData.setDataGovernanceTasksList(oResponse.success);
    _setValueList(oResponse.success, DataGovernanceTasksConfigViewProps.getDataGovernanceTasksValuesList());
    _triggerChange();
  };

  let failureFetchDataGovernanceTasksList = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchTasksList", getTranslation());
  };

  let _fetchDataGovernanceTasksList = function () {

    SettingUtils.csGetRequest(oDataGovernanceTasksRequestMapping.GetAll, {}, successFetchDataGovernanceTasksList, failureFetchDataGovernanceTasksList);
  };

  let _getActiveDataGovernanceTask = function () {
    return DataGovernanceTasksConfigViewProps.getActiveDataGovernanceTask();
  };

  let _setActiveDataGovernanceTask = function (oDataGovernanceTask) {
    DataGovernanceTasksConfigViewProps.setActiveDataGovernanceTask(oDataGovernanceTask);
  };

  let _makeActiveDataGovernanceTaskDirty = function () {
    let sActiveTaskId = _getActiveDataGovernanceTask().id;
    let oTasksMap = SettingUtils.getAppData().getDataGovernanceTasksList();
    let oActiveTasks = oTasksMap[sActiveTaskId];
    SettingUtils.makeObjectDirty(oActiveTasks);
    _setDataGovernanceTasksScreenLockStatus(true);
    return oActiveTasks.clonedObject;
  };

  let _createDefaultTaskMasterObject = function () {
    return {
      id: UniqueIdentifierGenerator.generateUUID(),
      label: UniqueIdentifierGenerator.generateUntitledName(),
      isCreated: true,
      icon: "",
      color: null,
      type: TaskTypeDictionary.SHARED,
      code: ""
    }
  };

  let _createDefaultTaskValueObject = function (sId) {
    return {
      id: sId || "",
      isChecked: false,
      isEditable: false,
      isSelected: false
    };
  };

  let _applyTaskValueToAllListNodes = function (sId, oValue) {
    let oListValuesInList = DataGovernanceTasksConfigViewProps.getDataGovernanceTasksValuesList();
    CS.map(oListValuesInList, function (oListValue) {
      oListValue[sId] = oValue;
    });
  };

  let successSaveTaskCallback = function (oCallbackData, oResponse) {
    var oSavedDataGovernanceTask = oResponse.success;
    var aDataGovernaceTaskGridData = DataGovernanceTasksConfigViewProps.getDataGovernanceTaskGridData();
    aDataGovernaceTaskGridData.push(oSavedDataGovernanceTask);
    var oProcessedTask = _preProcessDataGovernanceTaskDataForGridView([oSavedDataGovernanceTask])[0];
    var aGridViewData = SettingScreenProps.screen.getGridViewData();
    aGridViewData.unshift(oProcessedTask);

    var oMasterTasksList = SettingUtils.getAppData().getDataGovernanceTasksList();
    oMasterTasksList[oSavedDataGovernanceTask.id] = oSavedDataGovernanceTask;

    SettingScreenProps.screen.setGridViewTotalItems(SettingScreenProps.screen.getGridViewTotalItems() + 1);

    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }

    if (oCallbackData.isCreated) {
      alertify.success(getTranslation().TASK_CREATED_SUCCESSFULLY);
    } else {
      alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY , { entity :getTranslation().TASK}));
    }
    _setDataGovernanceTasksScreenLockStatus(false)

    _triggerChange();
  };

  let failureSaveTaskCallback = function (oCallback, oResponse) {
    SettingUtils.failureCallback(oResponse, "failureSaveTaskCallback",getTranslation());
  };

  let _createDataGovernanceTaskCall = function (oCreatedTaskMaster, oCallbackData) {
    SettingUtils.csPutRequest(oDataGovernanceTasksRequestMapping.CreateTask, {}, oCreatedTaskMaster, successSaveTaskCallback.bind(this, oCallbackData),
        failureSaveTaskCallback.bind(this, oCallbackData));
  };

  let _setTaskListValue = function (iId, oListItemValue) {
    let oListValuesInList = DataGovernanceTasksConfigViewProps.getDataGovernanceTasksValuesList();
    CS.assign(oListValuesInList[iId], oListItemValue);
  };

  let successGetTaskDetailsCallback = function (sTaskId, oResponse) {
    let oListItemValue = {};
    let oTaskFromServer = oResponse.success;

    let oTasksList = SettingUtils.getAppData().getDataGovernanceTasksList();
    let oTaskFromList = oTasksList[sTaskId];

    CS.assign(oTaskFromList, oTaskFromServer);

    delete oTaskFromList.clonedObject;
    delete oTaskFromList.isDirty;

    let oTaskValueList = DataGovernanceTasksConfigViewProps.getDataGovernanceTasksValuesList();
    delete oTaskValueList[sTaskId];

    oTaskValueList[oTaskFromServer.id] = _createDefaultTaskValueObject(oTaskFromServer.id);

    _applyTaskValueToAllListNodes('isEditable', false);
    _applyTaskValueToAllListNodes('isSelected', false);
    _applyTaskValueToAllListNodes('isChecked', false);
    oListItemValue.isSelected = true;
    oListItemValue.isChecked = true;
    _setTaskListValue(oTaskFromServer.id, oListItemValue);
    _setActiveDataGovernanceTask(oTaskFromList);

    _setDataGovernanceTasksScreenLockStatus(false);

    _triggerChange();
  };

  let _checkAndDeleteTasksFromList = function (sTaskId) {
    let oMasterTasks = SettingUtils.getAppData().getTasksList();
    let oTasksValues = DataGovernanceTasksConfigViewProps.getDataGovernanceTasksValuesList();
    let oSelectedTask = DataGovernanceTasksConfigViewProps.getActiveDataGovernanceTask();

    if (oSelectedTask || sTaskId) {
      let sTaskName = oMasterTasks[sTaskId || oSelectedTask.id].label;
      delete oTasksValues[sTaskId || oSelectedTask.id];
      delete oMasterTasks[sTaskId || oSelectedTask.id];
      DataGovernanceTasksConfigViewProps.setActiveDataGovernanceTask({});
      return sTaskName;
    }
    return null;
  };

  let failureGetTaskDetailsCallback = function (sTaskId, oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      let sTaskName = _checkAndDeleteTasksFromList(sTaskId);
      alertify.error("[" + sTaskName + "] " + getTranslation().ERROR_ALREADY_DELETED, 0);
      _setDataGovernanceTasksScreenLockStatus(false);
    } else {
      SettingUtils.failureCallback(oResponse, "failureGetTaskDetailsCallback" , getTranslation());
    }
    _triggerChange();
  };

  let _getTaskDetails = function (sSelectedTaskId) {
    SettingUtils.csGetRequest(oDataGovernanceTasksRequestMapping.GetTask, {id: sSelectedTaskId}, successGetTaskDetailsCallback.bind(this, sSelectedTaskId), failureGetTaskDetailsCallback.bind(this, sSelectedTaskId));
  };

  let _saveDataGovernanceTask  = function (oCallbackData) {
    let sActiveTaskId = _getActiveDataGovernanceTask().id;
    let oTaskMap = SettingUtils.getAppData().getDataGovernanceTasksList();
    let oActiveTask = oTaskMap[sActiveTaskId];

    if (!(oActiveTask.isDirty || oActiveTask.isCreated)) {
      if (oCallbackData.functionToExecute) {
        //unreachable code
        oCallbackData.functionToExecute();
      }
      alertify.message(getTranslation().NOTHING_CHANGED_TO_SAVE);
      return;
    }
    let oCurrentTask = {};
    if (!CS.isEmpty(oActiveTask)) {
      oCurrentTask = oActiveTask.isDirty ? oActiveTask.clonedObject : oActiveTask;
    }
    oCurrentTask.label = oCurrentTask.label.trim();
    if (CS.isEmpty(oCurrentTask.label)) {
      alertify.message(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return;
    }

    let oTaskValueObject = DataGovernanceTasksConfigViewProps.getDataGovernanceTasksValuesList()[sActiveTaskId];
    oTaskValueObject.isEditable = false;

    _setDataGovernanceTasksScreenLockStatus(false);
    let oServerCallback = {};
    oServerCallback.functionToExecute = oCallbackData.functionToExecute;

    SettingUtils.csPostRequest(oDataGovernanceTasksRequestMapping.SaveTask,{}, oCurrentTask, successSaveTaskCallback.bind(this, oServerCallback), failureSaveTaskCallback.bind(this, oServerCallback));

  };

  let _handleDataGovernanceTasksListNodeClicked = function (oModel) {
    let bTasksScreenLockStatus = _getDataGovernanceTasksScreenLockStatus();
    let oSelectedTask = _getActiveDataGovernanceTask();
    if (!(oSelectedTask.id == oModel.id)) {
      if (bTasksScreenLockStatus) {
        let oCallbackData = {};
        oCallbackData.functionToExecute = _getTaskDetails.bind(this, oModel.id);
        _saveDataGovernanceTask(oCallbackData);
      } else {
        _getTaskDetails(oModel.id);
      }
    } else {
      _applyTaskValueToAllListNodes('isChecked', false);
      _triggerChange();
    }
  };

  var _handleDeleteTaskFailure = function (aFailureIds) {
    let aTaskAlreadyDeleted = [];
    let aUnhandledTask = [];
    let aTaskGridData = DataGovernanceTasksConfigViewProps.getDataGovernanceTaskGridData();
    CS.forEach(aFailureIds, function (oItem) {
      let oTask = CS.find(aTaskGridData, {id: oItem.itemId});
      if (oItem.key == "DataGovernanceTaskNotFoundException") {
        aTaskAlreadyDeleted.push(oTask.label);
      } else {
        aUnhandledTask.push(oTask.label);
      }
    });
    if (aTaskAlreadyDeleted.length > 0) {
      let sTaskAlreadyDeleted = aTaskAlreadyDeleted.join(',');
      alertify.error(Exception.getCustomMessage("Task_already_deleted", getTranslation(), sTaskAlreadyDeleted), 0);
    }
    if (aUnhandledTask.length > 0) {
      let sUnhandledTask = aUnhandledTask.join(',');
      alertify.error(Exception.getCustomMessage("Unhandled_Task", getTranslation(), sUnhandledTask), 0);
    }
  };

  let failureDeleteTaskCallback = function (oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      _handleDeleteTaskFailure([]);
      _setDataGovernanceTasksScreenLockStatus(false);
    } else {
      SettingUtils.failureCallback(oResponse, "failureDeleteTaskCallback" , getTranslation());
    }
    _triggerChange();
  };

  let successDeleteTaskCallback = function (oResponse) {
    let aSuccessIds = oResponse.success;
    let aFailureIds = oResponse.failure ? oResponse.failure.exceptionDetails : [];
    let aGridViewData = SettingScreenProps.screen.getGridViewData();
    let oMasterTaskList = SettingUtils.getAppData().getDataGovernanceTasksList();
    let oGridViewSkeleton = SettingScreenProps.screen.getGridViewSkeleton();

    CS.forEach(aSuccessIds, function (sId) {
      CS.remove(aGridViewData, {id: sId});
      CS.pull(oGridViewSkeleton.selectedContentIds, sId);
      delete oMasterTaskList[sId];
    });

    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_DELETED_SUCCESSFULLY,{entity : getTranslation().TASK}));

    if (aFailureIds && aFailureIds.length > 0) {
      var oSelectedTask = DataGovernanceTasksConfigViewProps.getActiveDataGovernanceTask();
      _handleDeleteTaskFailure(aFailureIds, oMasterTaskList, oSelectedTask);
    }
    _setDataGovernanceTasksScreenLockStatus(false);

    _triggerChange();
  };

  let _deleteTasks = function (aBulkDeleteList) {
    if (!CS.isEmpty(aBulkDeleteList)) {
      SettingUtils.csDeleteRequest(oDataGovernanceTasksRequestMapping.DeleteTask, {}, {ids: aBulkDeleteList}, successDeleteTaskCallback, failureDeleteTaskCallback);
    } else {
      _setDataGovernanceTasksScreenLockStatus(false);
      alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_DELETED_SUCCESSFULLY,{entity : getTranslation().TASK}));
    }
    _triggerChange();
  };

  let _deleteDataGovernanceTask = function (aSeletedIds) {
    let oMasterTasks = SettingUtils.getAppData().getDataGovernanceTasksList();

    if (!CS.isEmpty(aSeletedIds)) {
      let aBulkDeleteTasks = '';
      CS.forEach(aSeletedIds, function (iId) {
        let oMasterTask = oMasterTasks[iId];
        aBulkDeleteTasks.push(oMasterTask.label);
      });
      //sBulkDeleteTasks = CS.trimEnd(sBulkDeleteTasks, ', ');
      CustomActionDialogStore.showListModeConfirmDialog(getTranslation().DELETE_CONFIRMATION, aBulkDeleteTasks,
          function () {
            _deleteTasks(aSeletedIds);
          }, function (oTask) {
          });
    }
  };

  let  _createDataGovernanceTaskDialogClick = function () {
    let oActiveTask = DataGovernanceTasksConfigViewProps.getActiveDataGovernanceTask();
    var oCodeToVerifyUniqueness = {
      id: oActiveTask.code,
      entityType: ConfigEntityTypeDictionary.ENTITY_TYPE_DATAGOVERNANCE
    };

    var oCallbackDataForUniqueness = {};
    oCallbackDataForUniqueness.functionToExecute = function(){
      delete oActiveTask.isCreated;
      _createDataGovernanceTaskCall(oActiveTask, {});
    };
    var sURL = oDataGovernanceTasksRequestMapping.CheckEntityCode;
    SettingUtils.checkForUniquenessOfCodePropertyAndSendAjaxCall(oCodeToVerifyUniqueness, sURL, oCallbackDataForUniqueness);

  };

  let _cancelDataGovernanceTaskDialogClicked = function () {
    let oActiveTask = DataGovernanceTasksConfigViewProps.getActiveDataGovernanceTask();
    let oDataGovernanceTasksMap = SettingUtils.getAppData().getDataGovernanceTasksList();
    delete oDataGovernanceTasksMap[oActiveTask.id];
    DataGovernanceTasksConfigViewProps.setActiveDataGovernanceTask({});
    DataGovernanceTasksConfigViewProps.setDataGovernanceTasksScreenLockStatus(false);
    _triggerChange();
  };

  let _handleDataGovernanceTaskConfigDialogButtonClicked = function (sButtonId) {
    if (sButtonId == "create") {
      _createDataGovernanceTaskDialogClick();
    } else {
      _cancelDataGovernanceTaskDialogClicked();
    }
  };

  let _getTaskColorTagMSSObject = function (oSelectedTask) {
    let oMockColors = new MockColors();
    let aSelectedColors = CS.isEmpty(oSelectedTask.color) || !oMockColors[oSelectedTask.color] ? [] : [oSelectedTask.color];
    let aColorList = CS.map(oMockColors, function (sVal, sKey) {
      return {
        id: sKey,
        label: sVal,
        color: sVal
      };
    });
    return{
      disabled: false,
      label: "",
      items: aColorList,
      selectedItems: aSelectedColors,
      singleSelect: true,
      context: "dataGovernanceTasksConfigColor"
    };
  };

  let _getTaskPriorityTagMSSObject = function (oSelectedTask, oRegResponseInfo) {
    let aTagList = SettingUtils.getAppData().getTagMap();
    let aSelectedList = [];
    let aPriorityTagMSSList = [];

    CS.forEach(aTagList, function (oTag) {
      if (oSelectedTask.priorityTag == oTag.id) {
        aSelectedList.push(oSelectedTask.priorityTag);
      }
      aPriorityTagMSSList.push({
        id: oTag.id,
        label: oTag.label
      })
    });

    return {
      disabled: false,
      label: "",
      items: aPriorityTagMSSList,
      selectedItems: aSelectedList,
      singleSelect: true,
      context: "taskPriorityTag",
      referencedData: aTagList,
      requestResponseInfo: oRegResponseInfo
    }
  };

  let _preProcessDataGovernanceTaskDataForGridView = function (aGovernanceTaskList) {
    var oGridSkeleton = SettingScreenProps.screen.getGridViewSkeleton();
    var aGridViewData = [];

    CS.forEach(aGovernanceTaskList, function (oDataGovernanceTask) {
      let oProcessedDataGovernanceTask = {};
      oProcessedDataGovernanceTask.id = oDataGovernanceTask.id;
      oProcessedDataGovernanceTask.isExpanded = false;
      oProcessedDataGovernanceTask.children = [];
      oProcessedDataGovernanceTask.actionItemsToShow = ["delete"];
      oProcessedDataGovernanceTask.properties = {};

      CS.forEach(oGridSkeleton.fixedColumns, function (oColumn) {
        switch (oColumn.id) {

          case "label":
            if (oDataGovernanceTask.hasOwnProperty(oColumn.id)) {
              oProcessedDataGovernanceTask.properties[oColumn.id] = {
                value: oDataGovernanceTask[oColumn.id],
                bIsMultiLine: false
              };
            }
            break;
          case "code":

            if(oDataGovernanceTask.hasOwnProperty(oColumn.id)){
              oProcessedDataGovernanceTask.properties[oColumn.id] = {
                value: oDataGovernanceTask[oColumn.id],
                isDisabled: true,
                bIsMultiLine: false
              };
            }
            break;

          default:
            if (oDataGovernanceTask.hasOwnProperty(oColumn.id)) {
              oProcessedDataGovernanceTask.properties[oColumn.id] = {
                value: oDataGovernanceTask[oColumn.id]
              };
            }
        }
      });

      CS.forEach(oGridSkeleton.scrollableColumns, function (oColumn) {
        switch (oColumn.id) {

          case "color":
            if (oDataGovernanceTask.hasOwnProperty(oColumn.id)) {
              oProcessedDataGovernanceTask.properties[oColumn.id] = _getTaskColorTagMSSObject(oDataGovernanceTask);
              oProcessedDataGovernanceTask.properties[oColumn.id].value = oProcessedDataGovernanceTask.properties[oColumn.id].selectedItems;
              oProcessedDataGovernanceTask.properties[oColumn.id].rendererType = oGridViewPropertyTypes.DROP_DOWN;
            }
            break;

          case "priorityTag":
            if (oDataGovernanceTask.hasOwnProperty(oColumn.id)) {
              let oMSSRequestResponseObj = {
                requestType: "configData",
                entityName: "tags",
              };
              oProcessedDataGovernanceTask.properties[oColumn.id] = _getTaskPriorityTagMSSObject(oDataGovernanceTask, oMSSRequestResponseObj);
              oProcessedDataGovernanceTask.properties[oColumn.id].value = oProcessedDataGovernanceTask.properties[oColumn.id].selectedItems;
            }
            break;
        }
      });

      aGridViewData.push(oProcessedDataGovernanceTask);
    });

    return aGridViewData;
  };

  let _fetchDataGovernanceTaskListForGridView = function () {
    let oGridPaginationData = SettingScreenProps.screen.getGridViewPaginationData();
    let sGridViewSearchText = SettingScreenProps.screen.getGridViewSearchText();
    let sGridViewSortBy = SettingScreenProps.screen.getGridViewSortBy();
    let sGridViewSearchBy = SettingScreenProps.screen.getGridViewSearchBy();
    let sGridViewSortOrder = SettingScreenProps.screen.getGridViewSortOrder();

    var oPostData = {
      from: oGridPaginationData.from,
      size: oGridPaginationData.pageSize,
      sortBy: sGridViewSortBy,
      sortOrder: sGridViewSortOrder,
      searchText: sGridViewSearchText,
      searchColumn: sGridViewSearchBy
    };

    SettingUtils.csPostRequest(oDataGovernanceTasksRequestMapping.Grid, {}, oPostData, successFetchDataGovernanceTaskListForGridView, failureFetchDataGovernanceTaskListForGridView);
  };

  let successFetchDataGovernanceTaskListForGridView = function (oResponse) {
    let aGovernanceTaskList = oResponse.success.tasksList;
    SettingUtils.getAppData().setTagMap(oResponse.success.referencedTags);
    var aProcessedGridViewData = _preProcessDataGovernanceTaskDataForGridView(aGovernanceTaskList);
    SettingUtils.getAppData().setDataGovernanceTasksList(aGovernanceTaskList);
    DataGovernanceTasksConfigViewProps.setDataGovernanceTaskGridData(aGovernanceTaskList);
    SettingScreenProps.screen.setGridViewData(aProcessedGridViewData);
    SettingScreenProps.screen.setGridViewTotalItems(oResponse.success.count);
    SettingScreenProps.screen.setIsGridDataDirty(false);
    SettingScreenProps.screen.setGridViewVisualData({});
    SettingScreenProps.screen.setGridViewContext(GridViewContexts.DATA_GOVERNANCE_TASK);

    _triggerChange();
  };

  let failureFetchDataGovernanceTaskListForGridView = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchDataGovernanceTaskListForGridView", getTranslation());
  };

  let _setUpDataGovernanceTaskConfigGridView = function (bIsTreeItemClicked) {
    let oTaskConfigGridViewSkeleton = new DataGovernanceTaskConfigGridViewSkeleton();
    SettingScreenProps.screen.setGridViewContext(GridViewContexts.DATA_GOVERNANCE_TASK);
    SettingScreenProps.screen.setGridViewSkeleton(oTaskConfigGridViewSkeleton);

    if(bIsTreeItemClicked) {
      SettingScreenProps.screen.setGridViewPaginationData(
          {
            from: 0,
            pageSize: 20
          }
      );
      SettingScreenProps.screen.setGridViewSortBy('label');
      SettingScreenProps.screen.setGridViewSearchBy('label');
      SettingScreenProps.screen.setGridViewSortOrder('asc');
      SettingScreenProps.screen.setGridViewSearchText('');
    }
    _fetchDataGovernanceTaskListForGridView();
  };

  let _postProcessDataGovernanceTask = function (oProcessedTask) {
    var aDataGovernanceTaskGridData = DataGovernanceTasksConfigViewProps.getDataGovernanceTaskGridData();
    if (oProcessedTask.isDirty) {
      let oOriginalTask = CS.find(aDataGovernanceTaskGridData, {id: oProcessedTask.id});
      let oTaskToSave = CS.cloneDeep(oOriginalTask);
      if (oTaskToSave) {
        CS.forOwn(oProcessedTask.properties, function (oProperty, sPropertyId) {
          switch (sPropertyId) {

            case "priorityTag":
            case "color":
              if (!CS.isEmpty(oProperty)) {
                oTaskToSave[sPropertyId] = oProperty.value[0];
              }
              break;

            default:
              oTaskToSave[sPropertyId] = oProperty.value;
              break;
          }
        });
        return oTaskToSave;
      }
    }
    return null;
  };

  let _safeToSave = function (aTaskListToSave) {
    var bSafeToSave = true;

    if (CS.isEmpty(aTaskListToSave)) {
      alertify.message(getTranslation().NOTHING_CHANGED_TO_SAVE);
      return false;
    }

    CS.forEach(aTaskListToSave, function (oTask) {
      if(oTask && CS.trim(oTask.label) == "") {
        bSafeToSave = false;
        alertify.message(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
        return false;
      }
    });

    return bSafeToSave;
  };

  let _saveDataGovernanceTaskInBulk = function (aTaskListToSave, oCallbackData) {
    if(_safeToSave(aTaskListToSave)) {
      SettingUtils.csPostRequest(oDataGovernanceTasksRequestMapping.SaveTask, {}, aTaskListToSave, successSaveDataGovernanceTasksInBulk.bind(this, oCallbackData), failureSaveDataGovernanceTasksInBulk);
    }

  };

  let successSaveDataGovernanceTasksInBulk = function (oCallbackData, oResponse) {
    let aDataGovernanceTasksList = oResponse.success.tasksList;
    SettingUtils.getAppData().setTagMap(oResponse.success.referencedTags);
    var aProcessedGridViewData = _preProcessDataGovernanceTaskDataForGridView(aDataGovernanceTasksList);
    var aTaskGridData = DataGovernanceTasksConfigViewProps.getDataGovernanceTaskGridData();
    var aGridViewData = SettingScreenProps.screen.getGridViewData();
    var oMasterTasksMap = SettingUtils.getAppData().getTasksList();

    CS.forEach(aDataGovernanceTasksList, function (oTask) {
      var sTaskId = oTask.id;
      var iIndex = CS.findIndex(aTaskGridData, {id: sTaskId});
      aTaskGridData[iIndex] = oTask;
      oMasterTasksMap[sTaskId] = oTask;
    });

    CS.forEach(aProcessedGridViewData, function (oProcessedTask) {
      var iIndex = CS.findIndex(aGridViewData, {id: oProcessedTask.id});
      aGridViewData[iIndex] = oProcessedTask;
    });

    let bIsAnyTaskDirty = false;
    CS.forEach(aGridViewData, function (oTask) {
      if(oTask.isDirty){
        bIsAnyTaskDirty= true;
      }
    });
    !bIsAnyTaskDirty && SettingScreenProps.screen.setIsGridDataDirty(false);

    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY , { entity :getTranslation().TASK}));
    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
    _triggerChange();
  };

  let failureSaveDataGovernanceTasksInBulk = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureSaveDataGovernanceTasksInBulk", getTranslation());
  };

  let _postProcessDataGovernanceTaskListAndSave = function (oCallbackData) {
    let aGridData = SettingScreenProps.screen.getGridViewData();
    let aTaskListToSave = [];
    let oTaskToSave = {};

    CS.forEach(aGridData, function (oProcessedTask) {
      oTaskToSave = _postProcessDataGovernanceTask(oProcessedTask);
      oTaskToSave && aTaskListToSave.push(oTaskToSave);
    });

    _saveDataGovernanceTaskInBulk(aTaskListToSave, oCallbackData);
  };

  let _discardDataGovernanceGridViewChanges = function (oCallbackData) {
    var aTaskGridData = DataGovernanceTasksConfigViewProps.getDataGovernanceTaskGridData();
    var aGridViewData = SettingScreenProps.screen.getGridViewData();
    var bShowDiscardMessage = false;

    CS.forEach(aGridViewData, function (oOldProcessedTask, iIndex) {
      if (oOldProcessedTask.isDirty) {
        var oTask = CS.find(aTaskGridData, {id: oOldProcessedTask.id});
        aGridViewData[iIndex] = _preProcessDataGovernanceTaskDataForGridView([oTask])[0];
        bShowDiscardMessage = true;
      }
    });

    bShowDiscardMessage && alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
    SettingScreenProps.screen.setIsGridDataDirty(false);
    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
    _triggerChange();
  };

  let _createDataGovernaceTask = function () {
    let oCreatedTaskMaster = _createDefaultTaskMasterObject();
    let sCreatedTaskId = oCreatedTaskMaster.id;

    let oTasksList = SettingUtils.getAppData().getTasksList();
    oTasksList[sCreatedTaskId] = oCreatedTaskMaster;

    _setDataGovernanceTasksScreenLockStatus(true);
    _setActiveDataGovernanceTask(oCreatedTaskMaster);
    _triggerChange();
  };

  /***************** PUBLIC API's **************/
  return {
    getDataGovernanceTasksScreenLockStatus: function () {
      return _getDataGovernanceTasksScreenLockStatus();
    },

    handleDataGovernanceTasksListNodeClicked: function (oModel) {
      _handleDataGovernanceTasksListNodeClicked(oModel);
    },

    getDataGovernanceTaskDetails: function (sActiveTaskId) {
      return _getTaskDetails(sActiveTaskId);
    },

    getActiveDataGovernanceTask: function () {
      return _getActiveDataGovernanceTask();
    },

    handleDataGovernanceTasksRefreshMenuClicked: function () {
      let sActiveTaskId = _getActiveDataGovernanceTask().id;
      if (sActiveTaskId) {
        _getTaskDetails(sActiveTaskId);
      } else {
        _fetchDataGovernanceTasksList();
      }
    },

    saveDataGovernanceTask: function (oCallback) {
      _saveDataGovernanceTask(oCallback);
    },

    discardUnsavedDataGovernanceTask: function (oCallbackData) {
      let oActiveTask = _getActiveDataGovernanceTask();
      let oMasterTaskList = SettingUtils.getAppData().getDataGovernanceTasksList();
      let oSelectedTaskInMaster = oMasterTaskList[oActiveTask.id];
      let bScreenLockStatus = _getDataGovernanceTasksScreenLockStatus();
      if (!bScreenLockStatus) {
        alertify.message(getTranslation().NO_CHANGE_FOUND_TO_DISCARD);
        return;
      }
      if (!CS.isEmpty(oSelectedTaskInMaster)) {
        if (oSelectedTaskInMaster.isDirty) {
          delete oSelectedTaskInMaster.clonedObject;
          delete oSelectedTaskInMaster.isDirty;
          alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
        }
        if (oSelectedTaskInMaster.isCreated) {
          delete oMasterTaskList[oActiveTask.id];
          delete DataGovernanceTasksConfigViewProps.getDataGovernanceTasksValuesList()[oActiveTask.id];
          _getActiveDataGovernanceTask({});
          alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
        }
        _setDataGovernanceTasksScreenLockStatus(false);
        if (oCallbackData.functionToExecute) {
          oCallbackData.functionToExecute();
        }
        _triggerChange();

      } else {
        alertify.message(getTranslation().NO_CHANGE_FOUND_TO_DISCARD);
      }
    },

    handleDataGovernanceTaskConfigFieldValueChanged: function (sKey, sVal) {
      let oActiveTask = DataGovernanceTasksStore.getActiveDataGovernanceTask();
      oActiveTask[sKey] = sVal;
      _triggerChange();
    },

    handleDataGovernanceTaskUploadIconChangeEvent: function (sIconKey) {
      _setDataGovernanceTasksScreenLockStatus(true);
      let oActiveTask = _makeActiveDataGovernanceTaskDirty();
      oActiveTask.icon = sIconKey;
    },

    deleteDataGovernanceTask: function (aSeletedIds) {
      _deleteDataGovernanceTask(aSeletedIds);
    },

    handleDataGovernanceTaskConfigDialogButtonClicked: function (sButtonId) {
      _handleDataGovernanceTaskConfigDialogButtonClicked(sButtonId);
    },

    setUpDataGovernanceTaskConfigGridView: function (bIsTreeItemClicked) {
      _setUpDataGovernanceTaskConfigGridView(bIsTreeItemClicked);
    },

    postProcessDataGovernanceTaskListAndSave: function (oCallbackData) {
      _postProcessDataGovernanceTaskListAndSave(oCallbackData);
    },

    discardDataGovernanceGridViewChanges: function (oCallbackData) {
      _discardDataGovernanceGridViewChanges(oCallbackData);
    },

    createDataGovernanceTask: function () {
      _createDataGovernaceTask();
    },

    fetchDataGovernanceTasksForGridView: function () {
      _fetchDataGovernanceTaskListForGridView();
    }
  }
})();

MicroEvent.mixin(DataGovernanceTasksStore);

export default DataGovernanceTasksStore;
