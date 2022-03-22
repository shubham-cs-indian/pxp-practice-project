import CS from '../../../../../../libraries/cs';
import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import Exception from '../../../../../../libraries/exceptionhandling/exception.js';
import { TasksRequestMapping as oTasksRequestMapping } from '../../tack/setting-screen-request-mapping';
import TasksProps from './../model/tasks-config-view-props';
import SettingUtils from './../helper/setting-utils';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import TaskTypeDictionary from '../../../../../../commonmodule/tack/task-type-dictionary';
import ConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import GridViewContexts from '../.././../../../../commonmodule/tack/grid-view-contexts';
import TaskConfigGridViewSkeleton from '../../tack/task-config-grid-view-skeleton';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import assetTypes from '../../tack/coverflow-asset-type-list';
import GridViewStore from '../../../../../../viewlibraries/contextualgridview/store/grid-view-store';
import CommonUtils from "../../../../../../commonmodule/util/common-utils";

var TasksStore = (function () {

  var _triggerChange = function () {
    TasksStore.trigger('tasks-changed');
  };

  var successFetchTasksList = function (oResponse) {
    var oAppData = SettingUtils.getAppData();
    oAppData.setTasksList(oResponse.success);
    _triggerChange();
  };

  var failureFetchTasksList = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchTasksList", getTranslation());
  };

  var _fetchTasksList = function () {

    SettingUtils.csGetRequest(oTasksRequestMapping.GetAll, {}, successFetchTasksList, failureFetchTasksList);
  };

  var _getActiveTask = function () {
    return TasksProps.getActiveTask();
  };

  var _setActiveTask = function (oTask) {
    TasksProps.setActiveTask(oTask);
  };

  let _makeActiveTaskDirty = () => {
    var sActiveTaskId = _getActiveTask().id;
    var oTasksMap = SettingUtils.getAppData().getTasksList();
    var oActiveTasks = oTasksMap[sActiveTaskId];
    SettingUtils.makeObjectDirty(oActiveTasks);
    return oActiveTasks.clonedObject;
  };

  var _createDefaultTaskMasterObject = function () {
    let oTaskTypeDictionary = new TaskTypeDictionary();
    return {
      id: UniqueIdentifierGenerator.generateUUID(),
      label: UniqueIdentifierGenerator.generateUntitledName(),
      isCreated: true,
      icon: "",
      color: null,
      type: oTaskTypeDictionary.SHARED,
      code: ""
    }
  };

  var successSaveTaskCallback = function (oCallbackData, oResponse) {
    let oSuccess = oResponse.success;
    var oSavedTask = oSuccess.task;
    var aTaskGridData = TasksProps.getTaskGridData();
    aTaskGridData.push(oSavedTask);
    SettingUtils.getAppData().setTagMap(oSuccess.referencedTags);
    var oProcessedTask = GridViewStore.getProcessedGridViewData(GridViewContexts.TASK, [oSavedTask])[0];
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.TASK);
    var aGridViewData = oGridViewPropsByContext.getGridViewData();
    aGridViewData.unshift(oProcessedTask);

    var oMasterTasksList = SettingUtils.getAppData().getTasksList();
    oMasterTasksList[oSavedTask.id] = oSavedTask;

    oGridViewPropsByContext.setGridViewTotalItems(oGridViewPropsByContext.getGridViewTotalItems() + 1);

    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }

    if (oCallbackData.isCreated) {
      alertify.success(getTranslation().TASK_CREATED_SUCCESSFULLY);
    } else {
      alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY , { entity :getTranslation().TASK}));
    }
    //SettingUtils.getComponentProps().screen.setListItemCreationStatus(true);

    _triggerChange();
  };

  var failureSaveTaskCallback = function (oCallback, oResponse) {
    SettingUtils.failureCallback(oResponse, "failureSaveTaskCallback", getTranslation());
  };

  var _createTask = function (oCallbackData) {
    var oCreatedTaskMaster = _createDefaultTaskMasterObject();
    var sCreatedTaskId = oCreatedTaskMaster.id;

    var oTasksList = SettingUtils.getAppData().getTasksList();
    oTasksList[sCreatedTaskId] = oCreatedTaskMaster;

    _setActiveTask(oCreatedTaskMaster);
    _triggerChange();
  };

  var _handleDeleteTaskFailure = function (aFailureIds) {
    let aTaskAlreadyDeleted = [];
    let aUnhandledTask = [];
    let aTaskGridData = TasksProps.getEventGridData();
    CS.forEach(aFailureIds, function (oItem) {
      let oTask = CS.find(aTaskGridData, {id: oItem.itemId});
      if (oItem.key == "EventNotFoundException") {
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

  var failureDeleteTaskCallback = function (oCallback, oResponse) {
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
      _handleDeleteTaskFailure([]);
    } else {
      SettingUtils.failureCallback(oResponse, 'failureDeleteTaskCallback', getTranslation());
    }
    _triggerChange();
  };

  var successDeleteTaskCallback = function (oResponse) {
    let aSuccessIds = oResponse.success;
    let aFailureIds = oResponse.failure ? oResponse.failure.exceptionDetails : [];
    let GridViewProps = GridViewStore.getGridViewPropsByContext(sContext); // eslint-disable-line
    let aGridViewData = GridViewProps.getGridViewData();
    let oMasterTaskList = SettingUtils.getAppData().getTasksList();
    let oGridViewSkeleton = GridViewProps.getGridViewSkeleton();

    CS.forEach(aSuccessIds, function (sId) {
      CS.remove(aGridViewData, {id: sId});
      CS.pull(oGridViewSkeleton.selectedContentIds, sId);
      delete oMasterTaskList[sId];
    });

    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_DELETED_SUCCESSFULLY,{entity : getTranslation().TASK}));

    if (aFailureIds && aFailureIds.length > 0) {
      _handleDeleteTaskFailure(aFailureIds);
    }
  };

  var _deleteTasks = function (aBulkDeleteList, oCallBack) {
   // var aFilteredTaskIds = _deleteUnSavedTask(aBulkDeleteList);
    if (!CS.isEmpty(aBulkDeleteList)) {
      return SettingUtils.csDeleteRequest(oTasksRequestMapping.DeleteTask, {}, {ids: aBulkDeleteList}, successDeleteTaskCallback, failureDeleteTaskCallback.bind(this, oCallBack));
    } else {
      alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_DELETED_SUCCESSFULLY,{entity : getTranslation().TASK}));
      _triggerChange();
    }
  };

  var _deleteTask = function (aSelectedIds, oCallBack) {
    //var aTaskIdsListToDelete = _getCheckedTasksAndSelectedIdList();
    var oMasterTasks = SettingUtils.getAppData().getTasksList();

    if (!CS.isEmpty(aSelectedIds)) {
      var aBulkDeleteTasks = [] ;
      CS.forEach(aSelectedIds, function (iId) {
        var oMasterTask = oMasterTasks[iId];
        let sMasterTaskLabel = CS.getLabelOrCode(oMasterTask);
        aBulkDeleteTasks.push(sMasterTaskLabel);
      });
      //sBulkDeleteTasks = CS.trimEnd(sBulkDeleteTasks, ', ');
      CustomActionDialogStore.showListModeConfirmDialog(getTranslation().DELETE_CONFIRMATION, aBulkDeleteTasks,
          function () {
            _deleteTasks(aSelectedIds, oCallBack)
            .then(_fetchTaskListForGridView);
          }, function (oTask) {
          }, true);
    } else {
      alertify.message(getTranslation().NOTHING_SELECTED_TO_DELETE);
    }
  };

  var _cancelTaskDialogClicked = function () {
    var oActiveTask = _getActiveTask();
    var oMasterTaskList = SettingUtils.getAppData().getTasksList();
    delete oMasterTaskList[oActiveTask.id];
    _setActiveTask({});
    _triggerChange();
  };

  var _createTaskDialogClick = function (oCallbackData) {
    var oActiveTask = _getActiveTask();
    var oTaskList = SettingUtils.getAppData().getTasksList();
    var oCreatedTaskMaster = oTaskList[oActiveTask.id];
    oCreatedTaskMaster = oCreatedTaskMaster.clonedObject || oCreatedTaskMaster;

    oCreatedTaskMaster.label = oCreatedTaskMaster.label.trim();
    if (CS.isEmpty(oCreatedTaskMaster.label)) {
      alertify.message(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return;
    }

    oCallbackData.isCreated = true;
    var oCodeToVerifyUniqueness = {
        id: oCreatedTaskMaster.code,
        entityType: ConfigEntityTypeDictionary.ENTITY_TYPE_TASK
    };

    var oCallbackDataForUniqueness = {};
    oCallbackDataForUniqueness.functionToExecute = function(){
      delete oCreatedTaskMaster.isCreated;
      _createTaskCall(oCreatedTaskMaster, oCallbackData);
    }

    var sURL = oTasksRequestMapping.CheckEntityCode;
    SettingUtils.checkForUniquenessOfCodePropertyAndSendAjaxCall(oCodeToVerifyUniqueness, sURL, oCallbackDataForUniqueness);
  };

  var _createTaskCall = function(oCreatedTaskMaster, oCallbackData) {
    SettingUtils.csPutRequest(oTasksRequestMapping.CreateTask, {}, oCreatedTaskMaster, successSaveTaskCallback.bind(this, oCallbackData), failureSaveTaskCallback.bind(this, oCallbackData));
  };

  var _handleTaskConfigDialogButtonClicked = function (sButtonId) {
    if (sButtonId == "create") {
      _createTaskDialogClick({});
    } else {
      _cancelTaskDialogClicked();
    }
  };

  var _getTaskStatusTagMSSObject = function (oSelectedTask, oRegResponseInfo) {
    //let sContext = bIsDataGovernanceTasks ? "dataGovernanceTaskStatusTag" : "taskStatusTag";
    var aTagList = SettingUtils.getAppData().getTagMap();// because the referenced tags were set in tag map
    var aSelectedList = [];
    var aStatusTagMSSList = [];

    CS.forEach(aTagList, function (oTag) {
      if (oSelectedTask.statusTag === oTag.id) {
        aSelectedList.push(oSelectedTask.statusTag);
      }
      aStatusTagMSSList.push({
        id: oTag.id,
        label: oTag.label
      })
    });

    return {
      disabled: oSelectedTask.type === TaskTypeDictionary().SHARED,
      label: "",
      items: aStatusTagMSSList,
      selectedItems: aSelectedList,
      singleSelect: true,
      context: "taskStatusTag",
      referencedData: aTagList,
      requestResponseInfo: oRegResponseInfo
    }
  };

  let successFetchTaskListForGridView= function (oResponse) {
    let aTaskList = oResponse.success.tasksList;
    SettingUtils.getAppData().setTagMap(oResponse.success.referencedTags);
    GridViewStore.preProcessDataForGridView(GridViewContexts.TASK, aTaskList,
        oResponse.success.count);
    SettingUtils.getAppData().setTasksList(aTaskList);
    TasksProps.setTaskGridData(aTaskList);
    _triggerChange();
  };

  var failureFetchTaskListForGridView= function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchTaskListForGridView", getTranslation());
  };

  let _fetchTaskListForGridView = function () {
    let oPostData = GridViewStore.getPostDataToFetchList(GridViewContexts.TASK);
    SettingUtils.csPostRequest(oTasksRequestMapping.bulkGet, {}, oPostData, successFetchTaskListForGridView, failureFetchTaskListForGridView);
  };

  let _setUpTaskConfigGridView = function () {
    let oTaskConfigGridViewSkeleton = new TaskConfigGridViewSkeleton();
    GridViewStore.createGridViewPropsByContext(GridViewContexts.TASK, {skeleton: oTaskConfigGridViewSkeleton});
    _fetchTaskListForGridView();

  };

  let _saveTaskInBulk = function (aTaskListToSave, oCallbackData) {
      return SettingUtils.csPostRequest(oTasksRequestMapping.SaveTask, {}, aTaskListToSave, successSaveTasksInBulk.bind(this, oCallbackData), failureSaveTasksInBulk);
  };

  let successSaveTasksInBulk = function (oCallbackData, oResponse) {
    let aTasksList = oResponse.success.tasksList;
    SettingUtils.getAppData().setTagMap(oResponse.success.referencedTags);
    let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.TASK, aTasksList);
    let aTaskGridData = TasksProps.getTaskGridData();
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.TASK);
    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    let oMasterTasksMap = SettingUtils.getAppData().getTasksList();

    CS.forEach(aTasksList, function (oTask) {
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
    !bIsAnyTaskDirty && oGridViewPropsByContext.setIsGridDataDirty(false);

    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY , { entity :getTranslation().TASK}));
    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
  };

  let failureSaveTasksInBulk = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureSaveTasksInBulk", getTranslation());
  };

  let _postProcessTaskListAndSave = function (oCallbackData) {
    let aTaskListToSave = [];

    let bSafeToSave = GridViewStore.processGridDataToSave(aTaskListToSave, GridViewContexts.TASK, TasksProps.getTaskGridData());
    if (bSafeToSave) {
      return _saveTaskInBulk(aTaskListToSave, oCallbackData);
    }
    return CommonUtils.rejectEmptyPromise();
  };

  var _discardTasksGridViewChanges = function (oCallbackData) {
    let aTaskGridData = TasksProps.getTaskGridData(); //saved Original(unprocessed) data
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.TASK);
    let aGridViewData = oGridViewPropsByContext.getGridViewData(); //saved Processed data
    let bShowDiscardMessage = false;

    CS.forEach(aGridViewData, function (oOldProcessedTask, iIndex) { //add the successfully saved attributes into stored data:
      if (oOldProcessedTask.isDirty) {
        // get the original attribute object and reprocess it -
        var oTask = CS.find(aTaskGridData, {id: oOldProcessedTask.id});
        aGridViewData[iIndex] = GridViewStore.getProcessedGridViewData(GridViewContexts.TASK, [oTask])[0];
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

  var _handleGridPropertyValueChangeDependencies = function (oContent, sPropertyId, value) {
    switch (sPropertyId) {
      case "type":
        let oMSSRequestResponseObj = {
          requestType: "configData",
          entityName: "tags",
        };
        oContent.properties["statusTag"] = _getTaskStatusTagMSSObject(oContent.properties, oMSSRequestResponseObj);
        oContent.properties["statusTag"].value = oContent.properties["statusTag"].selectedItems;
        oContent.isDirty = true;
        let aTaskListToSave = [];
        let bSafeToSave =  GridViewStore.processGridDataToSave(aTaskListToSave, GridViewContexts.TASK, TasksProps.getTaskGridData());
        if (bSafeToSave) {
          return _saveTaskInBulk(aTaskListToSave, {});
        }
        break;
    }
    return CommonUtils.rejectEmptyPromise();
  };

  var _handleTaskTypeSelected = function (aSelected) {
      let oActiveTask = _makeActiveTaskDirty();
      oActiveTask.type = aSelected[0];
  };

  var successUploadFileImport = function (oResponse) {
    alertify.success(getTranslation().IMPORT_IN_PROGRESS);
  };

  var failureUploadFileImport = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureUploadFileImport", getTranslation());
  };

  var uploadFileImport = function (oImportExcel, oCallback) {
    SettingUtils.csCustomPostRequest(oImportExcel.sUrl, oImportExcel.data,
        successUploadFileImport.bind(this, oCallback), failureUploadFileImport, false);
  };

  var _getIsValidFileTypes = function (oFile) {
    // var aValidTypes = AllowedMediaTypeDictionary[sAssetType];
    var sTypeRegex = assetTypes.map(function (sType) {
      return '\\' + sType + '$';
    }).join('|');
    return oFile.name.match(new RegExp(sTypeRegex, 'i'));
  };

  var successExportFile = function (oResponse) {
    alertify.success(getTranslation().EXPORT_IN_PROGRESS);
    SettingUtils.downloadFromByteStream(oResponse.success.fileStream, oResponse.success.fileName);
  };

  var failureExportFile = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureExportFile", getTranslation());
  };

  var _handleExportTask = function (oSelectiveExportDetails) {
    SettingUtils.csPostRequest(oSelectiveExportDetails.sUrl, {},oSelectiveExportDetails.oPostRequest, successExportFile, failureExportFile);
  };

  var _handleTaskFileUploaded = function (aFiles,oImportExcel) {
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

  return {

    fetchTasksList: function () {
      _fetchTasksList();
    },

    createTask: function () {
      _createTask();
    },

    deleteTask: function (aSelectedIds, oCallBack) {
      _deleteTask(aSelectedIds, oCallBack);
    },

    handleTaskConfigDialogButtonClicked: function (sButtonId) {
      _handleTaskConfigDialogButtonClicked(sButtonId);
    },

    setUpTaskConfigGridView: function (bIsTreeItemClicked) {
      _setUpTaskConfigGridView(bIsTreeItemClicked);
    },

    postProcessTaskAndSave: function (oCallbackData) {
      _postProcessTaskListAndSave(oCallbackData)
          .then(_triggerChange)
          .catch(CS.noop);
    },

    discardTasksGridViewChanges: function (oCallbackData) {
      _discardTasksGridViewChanges(oCallbackData);
    },

    fetchTaskListForGridView : function () {
      _fetchTaskListForGridView();
    },

    handleGridPropertyValueChangeDependencies: function (sPropertyId, value, oContent) {
      _handleGridPropertyValueChangeDependencies(oContent, sPropertyId, value)
          .then(_triggerChange)
          .catch(CS.noop);
    },

    handleTaskConfigFieldValueChanged: function (sKey, sVal) {
      var oActiveTask = _makeActiveTaskDirty();
      oActiveTask[sKey] = sVal;
      _triggerChange();
    },

    handleTaskTypeSelected: function (aSelectedItems) {
      _handleTaskTypeSelected(aSelectedItems);
      _triggerChange();
    },

    handleExportTask: function (oSelectiveExportDetails) {
      _handleExportTask(oSelectiveExportDetails)
    },

    handleTaskFileUploaded: function (aFiles,oImportExcel) {
      _handleTaskFileUploaded(aFiles,oImportExcel);
    },

  }
})();

MicroEvent.mixin(TasksStore);

export default TasksStore;
