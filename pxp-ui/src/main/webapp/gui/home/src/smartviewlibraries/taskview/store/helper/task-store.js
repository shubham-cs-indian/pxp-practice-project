import CS from '../../../../libraries/cs';
import MicroEvent from '../../../../libraries/microevent/MicroEvent.js';

/** @HackWork : View library should not access content screen think for proper solution **/
import ScreenModeUtils from './../../../../screens/homescreen/screens/contentscreen/store/helper/screen-mode-utils';

import RequestMapping from '../../../../libraries/requestmappingparser/request-mapping-parser.js';
import {TaskRequestMapping, WorkflowRequestMapping, UploadRequestMapping} from './../../../../viewlibraries/tack/view-library-request-mapping';
import { getTranslations as getTranslation } from '../../../../commonmodule/store/helper/translation-manager.js';
import EventBus from '../../../../libraries/eventdispatcher/EventDispatcher';
import TaskProps from './../model/task-props';
import alertify from '../../../../commonmodule/store/custom-alertify-store';
import ViewUtils from '../../../../viewlibraries/utils/view-library-utils';
import CommonModuleRequestMapping from '../../../../commonmodule/tack/common-module-request-mapping';
import UniqueIdentifierGenerator from '../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import TasksGroupingConstants from '../../tack/tasks-grouping-constants';
import BaseTypesDictionary from '../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import RoleIdDictionary from '../../../../commonmodule/tack/role-id-dictionary';
import TaskDictionary from '../../../../commonmodule/tack/task-dictionary';
import TagIdDictionary from '../../../../commonmodule/tack/tag-id-dictionary';
import TaskViewContextConstants from '../../tack/task-view-context-constants';
import TaskTypeDictionary from '../../../../commonmodule/tack/task-type-dictionary';
import EntityTasksFormViewTypes from '../../../../commonmodule/tack/entity-tasks-form-view-types';
import MockDataForTaskWorkflow from './../../tack/mock/mock-data-for-task-workflow';
import MockDataForTasksEditableRoles from './../../tack/mock/mock-data-for-task-editable-roles';
import CustomActionDialogStore from '../../../../commonmodule/store/custom-action-dialog-store';
import ExceptionLogger from '../../../../libraries/exceptionhandling/exception-logger';
import TaskDashBoardModelList from '../../tack/task-dashboard-model-list';
import WorkflowWorkbenchList from '../../tack/workflow-work-bench-list';
import ContentTaskModelList from './../../tack/content-tasks-model-list';
import ContentTaskViewListNodeContexts from './../../tack/task-view-context-constants';
import Task from './task';
import NotificationProps from './../model/notifications-props';
import NotificationStore from './notifications-store';
import CommonUtils from '../../../../commonmodule/util/common-utils';
import CommonModuleNotificationProps from '../../../../commonmodule/props/notification-props';
import SharableURLStore from '../../../../commonmodule/store/helper/sharable-url-store';
import { communicator as HomeScreenCommunicator } from '../../../../screens/homescreen/store/home-screen-communicator';
import BreadcrumbStore from '../../../../commonmodule/store/helper/breadcrumb-store';
import ViewLibraryUtils from '../../../../viewlibraries/utils/view-library-utils';
import ContentTaskProps from './../../../../screens/homescreen/screens/contentscreen/store/model/content-task-props';
import MockDataForFrequencyTypesDictionary
  from "../../../../screens/homescreen/screens/settingscreen/tack/mock/mock-data-for-frequency-dictionary";
import ContentUtils from "../../../../screens/homescreen/screens/contentscreen/store/helper/content-utils";
import ConfigEntityTypeDictionary from "../../../../commonmodule/tack/config-entity-type-dictionary";

var getRequestMapping = ScreenModeUtils.getScreenRequestMapping;

var Events = {
  TASK_FILE_ATTACHMENT_VIEW_FILE_UPLOAD_CLICKED: "task_file_attachment_view_file_upload_clicked",
  RESET_TASK_DATA_FROM_PARENT: "reset_task_data_from_parent",
  TASK_DATA_CHANGED: "task_data_changed",
  OPEN_PRODUCT_FROM_DASHBOARD: "open_product_from_dashboard",
  TASK_FILE_ATTACHMENT_VIEW_GET_ALL_ASSET_EXTENSIONS: "task_file_attachment_view_get_all_asset_extensions",
  HANDLE_TASK_LIST_BY_HEADER_CLICKED: "handle_task_list_by_header_clicked",
};

let TaskStore = (function () {

  let _triggerChange = () => {
    TaskStore.trigger('task-change');
  };

  let _activeTaskCommentSafetyCheck = () => {
    var sTemporaryCommentText =  TaskProps.getTemporaryCommentText();
    var aTemporaryCommentAttachmentData = TaskProps.getTemporaryCommentAttachmentData();
    if(!CS.isEmpty(sTemporaryCommentText) || !CS.isEmpty(aTemporaryCommentAttachmentData)){
      alertify.message(getTranslation().COMMENTING_IN_PROGRESS);
      return false;
    }
    else{
      return true;
    }
  };

  let _activeTaskSafetyCheck = () => {
    var oActiveTask = TaskProps.getActiveTask();
    if(oActiveTask && oActiveTask.pureObject && oActiveTask.isDirty) {
      alertify.message(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
      _triggerChange();
    } else {
      return true;
    }
  };

  let _updateTaskProps = (oData) => {
    TaskProps.setTaskList(oData.taskInstanceList);
    TaskProps.setActiveProperty(oData.activeProperty);
    TaskProps.setTemplateId(oData.templateId);
    TaskProps.setPropertyIdsHavingTask(oData.propertyIdsHavingTask);
    TaskProps.setShowAnnotations(oData.showAnnotation);
    TaskProps.setTaskData(oData);
    let oConfigDetails = oData.configDetails;
    _setReferencedDataForTask(oConfigDetails, oData);
  };

  let _handleTaskDialogCloseClicked = () => {
    let oExtraData = {
      isDialogOpen: false
    };

    let fFunctionToExecute = () => {
      TaskProps.setActiveProperty({});
      TaskStore.clearAllTaskProps();
    };

    if(_activeTaskSafetyCheck()){
      fFunctionToExecute();
      EventBus.dispatch(Events.TASK_DATA_CHANGED, oExtraData);
    } else {
      TaskStore.saveOrDiscardTask(fFunctionToExecute, oExtraData);
    }
  };

  let _handleContentEditToolbarButtonClicked = (sId) => {
    switch (sId) {
      case 'save' :
      case "saveWithWarning":
        _saveTask();
        break;
      case "complete":
        _completeTask();
        break;
      case 'discard' :
        _discardTask();
        break;
    }
  };

  let _handleFileAttachmentUploadClicked = (sContext, aFiles) => {
    switch (sContext){
      case 'taskFileAttachment':
        _handleTaskFileAttachmentUpload(aFiles, sContext);
        break;

      case 'commentFileAttachment':
        _handleCommentTaskFileAttachmentUpload(aFiles, sContext);
        break;

      default:
        break;
    }
  };

  var _getTasksScreenLockStatus = function () {
    return TaskProps.getTasksScreenLockStatus();
  };

  var _setTasksScreenLockStatus = function (bLockStatus) {
    TaskProps.setTasksScreenLockStatus(bLockStatus);
  };

  var _makeActiveTaskDirty = function () {
    var oActiveTask = TaskProps.getActiveTask();

    // TODO : Change to Cloned Object
    if (!oActiveTask.pureObject) {
      oActiveTask.pureObject = CS.cloneDeep(oActiveTask);
      oActiveTask.isDirty = true;
    }
    _setTasksScreenLockStatus(true);
    return oActiveTask;
  };

  var _clearAllTaskProps = function () {
    let sTaskMode = TaskProps.getTaskMode();
    if(sTaskMode!== TaskViewContextConstants.IMAGE_ANNOTATION_TASK){
      EventBus.dispatch(Events.RESET_TASK_DATA_FROM_PARENT);
    }
    TaskProps.reset();
  };

  var successFetchAllTasksForProperty = function (oResponse) {

    var oSuccess = oResponse.success;
    var oConfigDetails = oSuccess.configDetails;
    var oReferencedPermissions = oConfigDetails.referencedPermissions;
    _setReferencedDataForTask(oConfigDetails, oSuccess);

    // TaskProps.setTaskLevelsList([ContentTaskModelList()]);
    TaskProps.setTaskList(oSuccess.taskInstanceList);

    CS.forEach(oSuccess.taskInstanceList, function (oTask) {
      _updateTagForTask(oTask);
      CS.forEach(oTask.subTasks, function (oSubTask) {
        _updateTagForTask(oSubTask);
      });
    });

    TaskProps.setEditableTaskId("");
    TaskProps.setActiveTaskLevel({});
    TaskProps.setActiveTask({});
    TaskProps.setActiveSubTask({});
    TaskProps.setCheckedTaskList([]);

    TaskProps.setShouldUsePropPermissions(true);
    TaskProps.setTaskCanCreate(oReferencedPermissions.tabPermission.canCreate);
    TaskProps.setTaskCanEdit(oReferencedPermissions.tabPermission.canEdit);
    TaskProps.setTaskCanDelete(oReferencedPermissions.tabPermission.canDelete);

    _updateTaskMap();
    var oGroupByTypeMap = TaskProps.getTaskListGroupByType();
    var bIsShowImageAnnotation = TaskProps.getShowAnnotations();
    if(!bIsShowImageAnnotation) {
      setDefaultActiveTask(oGroupByTypeMap);
    }
    _triggerChange();
  };

  var _getTagForTaskList = function  (oMasterTagChild) {
    return {
      id: oMasterTagChild.id,
      code: oMasterTagChild.code,
      label: oMasterTagChild.label,
      color: oMasterTagChild.color,
      icon: oMasterTagChild.icon
    }
  };

  var _updateTagForTask = function (oTask) {
    var oReferencedData = TaskProps.getReferencedData();
    var oReferencedTags = oReferencedData.referencedTags;
    var oReferencedTasks = oReferencedData.referencedTasks;
    var oMasterTag = {};
    var oChildTag = {};

    //set status tag details in task instance
    var oMasterTask = oTask && oReferencedTasks[oTask.types[0]];
    var oStatus = oTask.status;
    if (oStatus && oMasterTask) {
      var oSelectedStatus = CS.find(oStatus.tagValues, {relevance: 100});
      var sStatusTagId = oMasterTask.statusTag;
      try {
        oMasterTag = oReferencedTags[sStatusTagId];
        oChildTag = oSelectedStatus && CS.find(oMasterTag.children, {id: oSelectedStatus.tagId});

        oChildTag && (oTask.tagForList = _getTagForTaskList(oChildTag));
      } catch (oException) {
        ExceptionLogger.error(oException);
      }
    }

    //set priority tag details in task instance
    var oPriority = oTask.priority;
    if(oPriority){
      var oSelectedPriority = CS.find(oPriority.tagValues, {relevance: 100});
      if(oSelectedPriority){
        try {
          oMasterTag = oReferencedTags[oPriority.tagId];
          oChildTag = CS.find(oMasterTag.children, {id: oSelectedPriority.tagId});

          oChildTag && (oTask.priorityTagForList = _getTagForTaskList(oChildTag));
        } catch (oException) {
          ExceptionLogger.error(oException);
        }
      }
    }
  };

  var _makeContentsAndPropertiesListHavingTasks = function () {
    let oReferencedData = TaskProps.getReferencedData();
    let oTaskData = TaskProps.getTaskData();
    var oReferencedElements = oReferencedData.referencedElements;
    var oReferencedVariants =oReferencedData.referencedVariants;
    var oReferencedTasks = oReferencedData.referencedTasks;
    var oReferencedElementsMapping = oReferencedData.referencedElementsMapping;
    var oActiveContent = oTaskData.activeContent;
    var aTaskLevelsList = TaskProps.getTaskLevelsList();

    if(CS.isEmpty(aTaskLevelsList)) {
      return;
    }
    //content side has just one level
    let aTaskLevel = aTaskLevelsList[0];

    var aTaskList = TaskProps.getTaskList();
    CS.forEach(aTaskList, function (oTask) {
      _updateTagForTask(oTask);
      CS.forEach(oTask.subTasks, function (oSubTask) {
        _updateTagForTask(oSubTask);
      });
    });

    var oCountMap = {};
    var oContentLevelTask = CS.find(aTaskLevel, {id: "content-level-task"});
    oContentLevelTask.children = [];
    var aContentLevelTaskList = oContentLevelTask.children;

    var oPropertyLevelTask = CS.find(aTaskLevel, {id: "property-level-task"});
    oPropertyLevelTask.children = [];
    var aPropertyLevelTaskList = oPropertyLevelTask.children;

    var oContentDetails = {};

    oContentDetails = {
      "id": oActiveContent.id,
      "label": CommonUtils.getContentName(oActiveContent),
      "context": ContentTaskViewListNodeContexts.CONTENT_TASK,
      "listItemKey": "content",
      "tasksCount": 0,
      "children": []
    };
    aContentLevelTaskList.push(oContentDetails);
    oCountMap[oActiveContent.id] = oCountMap[oActiveContent.id] || 0;
    oCountMap["all"] = oCountMap["all"] || 0;
    oCountMap["content-level-task"] = oCountMap["content-level-task"] || 0;
    oCountMap["property-level-task"] = oCountMap["property-level-task"] || 0;

    if (!CS.isEmpty(oReferencedVariants)) {
      CS.forEach(oReferencedVariants, function (oVariant) {
        oContentDetails = {
          "id": oVariant.id,
          "label": oVariant.label,
          "context": ContentTaskViewListNodeContexts.CONTENT_TASK,
          "listItemKey": "variant",
          "tasksCount": 0,
          "children": []
        };
        aContentLevelTaskList.push(oContentDetails);
        oCountMap[oVariant.id] = oCountMap[oVariant.id] || 0;
      });
    }

    CS.forEach(aTaskList, function (oTask) {
      if (oReferencedTasks[oTask.types[0]] || CS.isEmpty(oTask.types)) {
        var aLinkedEntities = oTask.linkedEntities;
        var oLinkedEntity = CS.find(aLinkedEntities, {contentId: oActiveContent.id}) || {};
        if (oLinkedEntity.elementId) {
          if (oReferencedElementsMapping[oLinkedEntity.elementId]) {
            var oReferencedElementFromMapping = oReferencedElementsMapping[oLinkedEntity.elementId];
            var sElementLabel = oReferencedElements[oReferencedElementFromMapping.id];
            var oChild = CS.find(aPropertyLevelTaskList, {id: oLinkedEntity.elementId});
            if (CS.isEmpty(oChild)) {
              var oPropertyDetails = {
                "id": oLinkedEntity.elementId,
                "label": sElementLabel,
                "context": ContentTaskViewListNodeContexts.CONTENT_TASK,
                "listItemKey": "property",
                "tasksCount": 0,
                "children": []
              };
              oCountMap[oLinkedEntity.elementId] = oCountMap[oLinkedEntity.elementId] || 1;
              aPropertyLevelTaskList.push(oPropertyDetails);
            } else {
              oCountMap[oLinkedEntity.elementId]++;
            }
            oCountMap["all"]++;
            oCountMap["property-level-task"]++;
          }else{
            oCountMap["all"]++;
          }
        } else if (oLinkedEntity.variantId || oLinkedEntity.contentId) {
          if (oLinkedEntity.variantId) {
            oCountMap[oLinkedEntity.variantId]++;
          } else {
            oCountMap[oActiveContent.id]++;
          }
          oCountMap["all"]++;
          oCountMap["content-level-task"]++;
        }
      }
    });

    CS.forEach(aTaskLevel, function (oLevel) {
      oLevel.tasksCount = oCountMap[oLevel.id] || 0;
      if (oLevel.children) {
        CS.forEach(oLevel.children, function (oChild) {
          oChild.tasksCount = oCountMap[oChild.id] || 0;
        });
      }
    });

  };

  var _setTaskViewForContent = function (sId, sContext) {
    _makeContentsAndPropertiesListHavingTasks();
    TaskProps.setEditableTaskId("");
    TaskProps.setActiveGroupByType("type");
    TaskProps.setActiveSubTask({});
    _handleTaskListLevelClicked(sId, sContext);
  };

  var _handleCommentTextChanged = function (sValue) {
    _setTasksScreenLockStatus(true);
    TaskProps.setTemporaryCommentText(sValue);
    let oActiveTask = _makeActiveTaskDirty();
    EventBus.dispatch(Events.TASK_DATA_CHANGED, {isTaskDirty: true});
    _triggerChange();
  };

  var _createTaskComment = function () {
    _setTasksScreenLockStatus(true);
    var sCommentText = TaskProps.getTemporaryCommentText();
    let aAttachmentData = TaskProps.getTemporaryCommentAttachmentData();

    if (!(/\S/.test(sCommentText)) && CS.isEmpty(aAttachmentData)) {
      CommonUtils.showError(getTranslation().CANNOT_CREATE_EMPTY_COMMENT);
      return;
    }

    var oActiveTask = _makeActiveTaskDirty();
    var oActiveSubTask = TaskProps.getActiveSubTask();
    var oCurrentUser = CommonUtils.getCurrentUser();
    var oCommentData = {};
    var aAttachmentsIdList = [];
    CS.forEach(aAttachmentData, function (oAttachmentData) {
      aAttachmentsIdList.push(oAttachmentData.id);
    });
    oCommentData.id = UniqueIdentifierGenerator.generateUUID();
    oCommentData.attachments = aAttachmentsIdList;
    oCommentData.postedBy = oCurrentUser.id;
    oCommentData.text = sCommentText;
    oCommentData.timestamp = new Date().getTime();
    if(!CS.isEmpty(oActiveSubTask)){
      oActiveSubTask.comments.push(oCommentData);
    } else {
      oActiveTask.comments.push(oCommentData);
    }
    TaskProps.setTemporaryCommentText("");
    TaskProps.setTemporaryCommentAttachmentData([]);
    // _saveTask();
  };

  var failureFetchAllTasks = function (oResponse) {
    CommonUtils.failureCallback(oResponse, 'failureFetchAllTasks', getTranslation());
    _triggerChange();
  };

  var _fetchAllTaskForProperty = function (oActiveContent, sScreenMode) {
    TaskProps.setActiveGroupByType("type");
    var oPostData = {
      templateId : TaskProps.getTemplateId()
    };

    var oParameters = {};
    oParameters.getAll = true;
    oParameters.id = oActiveContent.id;
    oParameters.isLoadMore = false;
    oParameters.tab = "tasktab";

    if(TaskProps.getShowAnnotations()){
      oPostData.isForTaskAnnotation = true;
    }

    CS.postRequest(getRequestMapping(sScreenMode).GetEntityById, oParameters, oPostData, successFetchAllTasksForProperty, failureFetchAllTasks);
  };
  var successSaveBulkTaskCallback = function (oCallBack, oResponse) {
    let oDataToUpdateInParent = {};
    if (CS.isNotEmpty(oResponse.success) && CS.isEmpty(oResponse.failure.exceptionDetails)) {
      var aTasksFromServer = oResponse.success;
      TaskProps.setTasksScreenLockStatus(false);

      CS.forEach(aTasksFromServer, (oTaskFromServer) => {
        var aTaskList = TaskProps.getTaskList();
        var oTaskFromList = CS.find(aTaskList, {id: oTaskFromServer.id});
        oTaskFromList.name = oTaskFromServer.name;
        oTaskFromList.priority = oTaskFromServer.priority;
        oTaskFromList.status = oTaskFromServer.status;
        oTaskFromList.overDueDate = oTaskFromServer.overDueDate;
        _updateTagForTask(oTaskFromList);
        CS.forEach(oTaskFromServer.subTasks, function (oSubTask) {
          _updateTagForTask(oSubTask);
          if (!CS.isEmpty(oSubTask)) {
            _setSubTasksAttachmentsData();
          } else {
            _setAttachmentsData();
          }
        });
        _updateTaskMap();
        _setRoleDetails(oTaskFromServer);
        _setTaskFormViewModels(oTaskFromServer);
      });

      alertify.success(getTranslation().SUCCESSFULLY_SAVED);
      if (oCallBack && oCallBack.functionToExecute) {
        oCallBack.functionToExecute();
      }

      let oActiveTask = TaskProps.getActiveTask();
      if (!CS.isEmpty(oActiveTask) && oActiveTask.status.tagValues[0].tagId !== "taskdone") {
        oActiveTask = CS.find(aTasksFromServer, {id: oActiveTask.id});
        TaskProps.setActiveTask(oActiveTask);
      }
      TaskProps.setCheckedTaskList([]);
       oDataToUpdateInParent = {
        activeTask: oActiveTask,
        checkedTaskList: []
      };
    } else if(CS.isNotEmpty(oResponse.failure.exceptionDetails) && CS.isEmpty(oResponse.success)){
      failureSaveBulkTaskCallback(oResponse);
    }else if(CS.isNotEmpty(oResponse.failure.exceptionDetails) && CS.isNotEmpty(oResponse.success)){
      failureSaveBulkTaskCallback(oResponse);
    }

    EventBus.dispatch(Events.TASK_DATA_CHANGED, oDataToUpdateInParent);
    setTimeout(HomeScreenCommunicator.handleNotificationRefresh, 2000);

  };

  var failureSaveBulkTaskCallback = function (oResponse) {
    if (CS.isNotEmpty(oResponse.failure.exceptionDetails) && CS.isNotEmpty(oResponse.success)) {
      alertify.warning(getTranslation().BULK_COMPLETE_PARTIAL_EXCEPTION);
    } else if (!CS.isEmpty(oResponse.failure)) {
      TaskProps.setTasksScreenLockStatus(false);
      CommonUtils.failureCallback(oResponse, "failureSaveBulkTaskCallback", getTranslation())
    } else{
      ExceptionLogger.error("failureSaveTaskDataCallback: Something went wrong" + oResponse);
    }
    _triggerChange();
  };

  var successSaveTaskCallback = function (oCallBack, oResponse) {
    var oActiveTask = TaskProps.getActiveTask();
    var oActiveSubTask = TaskProps.getActiveSubTask();
    var oTaskFromServer = oResponse.success;
    TaskProps.setTasksScreenLockStatus(false);

    if (oActiveTask.isDirty || oActiveTask.pureObject) {
      delete oActiveTask.isDirty;
      delete oActiveTask.pureObject;
    }

    CS.assign(oActiveTask, oTaskFromServer);
    var aTaskList = TaskProps.getTaskList();
    var oTaskFromList = CS.find(aTaskList, {id: oActiveTask.id});
    oTaskFromList.name = oActiveTask.name;
    oTaskFromList.priority = oActiveTask.priority;
    oTaskFromList.status = oActiveTask.status;
    oTaskFromList.overDueDate = oActiveTask.overDueDate;
    _updateTagForTask(oTaskFromList);
    CS.forEach(oTaskFromServer.subTasks, function (oSubTask) {
      _updateTagForTask(oSubTask);
    });

    if(!CS.isEmpty(oActiveSubTask)){
      _setSubTasksAttachmentsData();
    } else {
      _setAttachmentsData();
    }
    alertify.success(getTranslation().SUCCESSFULLY_SAVED);
    if (oCallBack && oCallBack.functionToExecute) {
      oCallBack.functionToExecute();
    }
    _updateTaskMap();
    _setRoleDetails(oTaskFromServer);
    _setTaskFormViewModels(oTaskFromServer);
    let oDataToUpdateInParent = {
      isTaskDirty: false,
      isTaskAdded: true,
    };

    let oExtraData = oCallBack && oCallBack.extraData;
    EventBus.dispatch(Events.TASK_DATA_CHANGED, CS.assign(oDataToUpdateInParent, oExtraData));
  };

  var failureSaveTaskCallback = function (oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      TaskProps.setTasksScreenLockStatus(false);
      let oException = oResponse.failure.exceptionDetails[0];
      let sKey =  oException.key;
      switch (sKey) {
          case "TaskCannotBeDoneException" :
              alertify.error(getTranslation().TaskCannotBeDoneException);
            break;
          case "TaskCannotBeSignedOffException" :
              alertify.error(getTranslation().TaskCannotBeSignedOffException);
              break;
      }
    } else {
      ExceptionLogger.error("failureSaveTaskDataCallback: Something went wrong" + oResponse);
    }
    _triggerChange();
  };

  var _generateADMForAttachments = function (aOldAttachments, aNewAttachments) {
    var oADM = {
      added: [],
      deleted: []
    };

    oADM.added = CS.difference(aNewAttachments, aOldAttachments);
    oADM.deleted = CS.difference(aOldAttachments, aNewAttachments);

    return oADM;
  };

  var _changeVariantOfIdOfNewlyAddedElement = function (aNewIds, aAddedElements) {
    var sSplitter = CommonUtils.getSplitter();
    CS.forEach(aNewIds, function (sNewId) {
      var oVariant = CS.find(aAddedElements, {variantOf: sNewId});
      if(oVariant) {
        oVariant.variantOf = sNewId.split(sSplitter)[0];
      }
    });
  };

  var _generateADMForRoleCandidates = function (aOldCadidates, aClonedCandidates) {
    var oADM = {
      added: [],
      modified: [],
      deleted: []
    };

    var sSplitter = CommonUtils.getSplitter();
    var aFoundIdsInNew = [];

    CS.forEach(aOldCadidates, function (oOldCandidate) {
      var oClonedCandidate = CS.find(aClonedCandidates, {id: oOldCandidate.id});
      if(CS.isEmpty(oClonedCandidate)) {
        oADM.deleted.push(oOldCandidate.id);
      }
      else {
        aFoundIdsInNew.push(oOldCandidate.id)
      }
    });

    CS.forEach(aClonedCandidates, function(oClonedCan){
      if(CS.indexOf(aFoundIdsInNew, oClonedCan.id) == -1){
        oClonedCan.id = oClonedCan.id.split(sSplitter)[0];
        oADM.added.push(oClonedCan);
      }
    });

    return oADM;
  };

  let _generateADMForRoles = function (oOldTask, oNewTask) {
    let aRoleIdsList = ["accountable", "responsible", "consulted", "informed", "verify", "signoff"];

    CS.forEach(aRoleIdsList, function (sRoleId) {
      let oADM = {
        addedUserIds: [],
        deletedUserIds: [],
        addedRoleIds: [],
        deletedRoleIds: [],
      };

      let oOldRole = oOldTask[sRoleId];
      let oClonedRole = oNewTask[sRoleId];

      let oTaskData = TaskProps.getTaskData();
      let aUsersList = oTaskData.usersList;
      let aRolesList = oTaskData.rolesList;

      CS.forEach(oClonedRole.userIds, function (sId) {
        // if (!CS.includes(oOldRole.userIds, sId) && CS.find(aUsersList, {id: sId})) {
        if (!CS.includes(oOldRole.userIds, sId)) {
          oADM.addedUserIds.push(sId);
        }
      });
      CS.forEach(oOldRole.userIds, function (sId) {
        // if (!CS.includes(oClonedRole.userIds, sId) && CS.find(aUsersList, {id: sId})) {
        if (!CS.includes(oClonedRole.userIds, sId)) {
          oADM.deletedUserIds.push(sId);
        }
      });

      CS.forEach(oClonedRole.roleIds, function (sId) {
        // if (!CS.includes(oOldRole.roleIds, sId) && CS.find(aRolesList, {id: sId})) {
        if (!CS.includes(oOldRole.roleIds, sId)) {
          oADM.addedRoleIds.push(sId);
        }
      });
      CS.forEach(oOldRole.roleIds, function (sId) {
        // if (!CS.includes(oClonedRole.roleIds, sId) && CS.find(aRolesList, {id: sId})) {
        if (!CS.includes(oClonedRole.roleIds, sId)) {
          oADM.deletedRoleIds.push(sId);
        }
      });
      oNewTask[sRoleId] = oADM;
    });

   /* var aNewlyAddedIds = [];
    var aFoundIdsInNew = [];
    var sSplitter = CommonUtils.getSplitter();
    var sNewId = CommonUtils.getNewSuffix();
    CS.forEach(aOldRoles, function (oOldRole) {
      var oClonedRole = CS.find(aClonedRoles, {id: oOldRole.id});
      if(CS.isEmpty(oClonedRole)) {
        oADM.deleted.push(oOldRole.id);
      }/!*else if(oOldRole.roleMappingId != oClonedRole.roleMappingId){
       oADM.modified.push(oClonedRole);
       }*!/
      else {
        oClonedRole.id = oClonedRole.id.split(sSplitter)[0];
        aFoundIdsInNew.push(oClonedRole.id);
        if(oOldRole.id.indexOf(sNewId) >= 0) {
          aNewlyAddedIds.push(oOldRole.id);
          oADM.added.push(oClonedRole);

        } else {
          var oRoleCandidatesADM = _generateADMForRoleCandidates(oOldRole.candidates, oClonedRole.candidates);
          var oActivePropertyForTask = TaskProps.getActiveTask();
          if (oRoleCandidatesADM.added.length
              || oRoleCandidatesADM.deleted.length
              || oRoleCandidatesADM.modified.length
              || (!CS.isEmpty(oOldRole.notification) && CS.isEmpty(oClonedRole.notification)) ||
              oActivePropertyForTask && oActivePropertyForTask.instanceId == oOldRole.id) {
            oClonedRole.addedCandidates = oRoleCandidatesADM.added;
            oClonedRole.modifiedCandidates = oRoleCandidatesADM.modified;
            oClonedRole.deletedCandidates = oRoleCandidatesADM.deleted;
            delete oClonedRole.candidates;
            oADM.modified.push(oClonedRole);
          }
        }
      }
    });

    CS.forEach(aClonedRoles, function(oClonedRole){
      if(CS.indexOf(aFoundIdsInNew, oClonedRole.id) == -1){
        aNewlyAddedIds.push(oClonedRole.id);
        oClonedRole.id = oClonedRole.id.split(sSplitter) [0];
        oADM.added.push(oClonedRole);
      }
    });

    _changeVariantOfIdOfNewlyAddedElement(aNewlyAddedIds, oADM.added);

    return oADM;*/
  };

  var _getADMForModifiedSubtask = function (oOldSubtask, oNewSubtask) {
    oNewSubtask.addedComments = _getAddedComments(oOldSubtask.comments, oNewSubtask.comments);
    oNewSubtask.comments = [];
    oNewSubtask.addedInclusions = [];
    oNewSubtask.deletedInclusions = [];
    oNewSubtask.modifiedInclusions = [];
    delete oNewSubtask.inclusions;

    oNewSubtask.addedExclusions = [];
    oNewSubtask.deletedExclusions = [];
    oNewSubtask.modifiedExclusions = [];
    delete oNewSubtask.exclusions;

    var oADMForLinkedEntities = _generateADMForLinkedEntities(oOldSubtask.linkedEntities, oNewSubtask.linkedEntities);
    oNewSubtask.addedLinkedEntities = oADMForLinkedEntities.added;
    oNewSubtask.deletedLinkedEntities = oADMForLinkedEntities.deleted;
    oNewSubtask.modifiedLinkedEntities = oADMForLinkedEntities.modified;
    delete oNewSubtask.linkedEntities;

    var oADMForAttachments = _generateADMForAttachments(oOldSubtask.attachments, oNewSubtask.attachments);
    oNewSubtask.addedAttachments = oADMForAttachments.added;
    oNewSubtask.deletedAttachments = oADMForAttachments.deleted;
    delete oNewSubtask.attachments;

   /* var aOldRoles = oOldSubtask.roles;
    var aNewRoles = oNewSubtask.roles;
    var oADMForRoles = _generateADMForRoles(aOldRoles, aNewRoles);
    oNewSubtask.addedRoles = oADMForRoles.added;
    oNewSubtask.deletedRoles = oADMForRoles.deleted;
    oNewSubtask.modifiedRoles = oADMForRoles.modified;
    delete oNewSubtask.roles;*/
    _generateADMForRoles(oOldSubtask, oNewSubtask);


    var aOldTags = [];
    var aNewTags = [];
    oOldSubtask.status && aOldTags.push(oOldSubtask.status);
    oOldSubtask.priority && aOldTags.push(oOldSubtask.priority);
    oNewSubtask.status && aNewTags.push(oNewSubtask.status);
    oNewSubtask.priority && aNewTags.push(oNewSubtask.priority);

    var oADMForTags = _generateADMForTags(aOldTags, aNewTags);
    oNewSubtask.addedTags = oADMForTags.added;
    oNewSubtask.deletedTags = oADMForTags.deleted;
    oNewSubtask.modifiedTags = oADMForTags.modified;

    oNewSubtask.status = null;
    oNewSubtask.priority = null;

    return oNewSubtask;
  };

  var _generateADMForSubTasks = function (aOldSubTasks, aNewSubTasks) {
    var oADM = {
      added: [],
      deleted: [],
      modified: []
    };

    CS.forEach(aNewSubTasks, function (oNewSubTask) {
      var oOldSubTask = CS.find(aOldSubTasks, {id: oNewSubTask.id});
      if(CS.isEmpty(oOldSubTask)){
        oADM.added.push(oNewSubTask);
      } else {
        if(!CS.isEqual(oOldSubTask, oNewSubTask)){
          oADM.modified.push(_getADMForModifiedSubtask(oOldSubTask, oNewSubTask));
        }
      }
    });

    CS.forEach(aOldSubTasks, function (oOldSubTask) {
      var oNewSubTask = CS.find(aNewSubTasks, {id: oOldSubTask.id});
      if(CS.isEmpty(oNewSubTask)){
        oADM.deleted.push(oOldSubTask.id);
      }
    });

    return oADM;
  };

  var _getAddedComments = function (aOldComments, aNewComments) {
    var addedComments = [];
    CS.forEach(aNewComments, function (oNewComment) {
      var oOldComment = CS.find(aOldComments, {id: oNewComment.id});
      if(CS.isEmpty(oOldComment)){
        addedComments.push(oNewComment);
      }
    });
    return addedComments;
  };

  var _generateADMForLinkedEntities = function (aOldLinkedEntities, aNewLinkedEntities) {
    var oADM = {
      added: [],
      deleted: [],
      modified: []
    };

    CS.forEach(aNewLinkedEntities, function (oNewLinkedEntity) {
      var oOldLinkedEntity = CS.find(aOldLinkedEntities, {id: oNewLinkedEntity.id});
      if(CS.isEmpty(oOldLinkedEntity)){
        oADM.added.push(oNewLinkedEntity);
      } else {
        if(!CS.isEqual(oOldLinkedEntity, oNewLinkedEntity)){
          oADM.modified.push(oNewLinkedEntity)
        }
      }
    });

    CS.forEach(aOldLinkedEntities, function (oOldLinkedEntity) {
      var oNewLinkedEntity = CS.find(aNewLinkedEntities, {id: oOldLinkedEntity.id});
      if(CS.isEmpty(oNewLinkedEntity)){
        oADM.deleted.push(oOldLinkedEntity.id);
      }
    });

    return oADM;
  };

  var _generateADMForTagValues = function (aOldTagValues, aNewTagValues) {
    var oADM = {
      added: [],
      modified: [],
      deleted: []
    };

    var aFoundIds = [];

    CS.forEach(aOldTagValues, function (oOldTagValue) {
      var oNewTagValue = CS.find(aNewTagValues, {id: oOldTagValue.id});
      if(CS.isEmpty(oNewTagValue)) {
        oADM.deleted.push(oOldTagValue.id);
      } else {
        aFoundIds.push(oOldTagValue.id);
      }
    });

    CS.forEach(aNewTagValues, function(oNewTag){
      if(CS.indexOf(aFoundIds, oNewTag.id) === -1){
        if(!CS.find(oADM.added, {id: oNewTag.id})) {
          oADM.added.push(oNewTag);
        }
      }
    });

    return oADM;
  };

  var _generateADMForTags = function (aOldTags, aNewTags) {
    var oADM = {
      added: [],
      modified: [],
      deleted: []
    };

    var aFoundIds = [];

    CS.forEach(aOldTags, function (oOldTag) {
      var oNewTag = CS.find(aNewTags, {id: oOldTag.id});
      if(CS.isEmpty(oNewTag)) {
        oADM.deleted.push(oOldTag.id);
      }
      else {
        aFoundIds.push(oOldTag.id);
        var bIsModified = false;
        var oTagValuesADM = _generateADMForTagValues(oOldTag.tagValues, oNewTag.tagValues);
        if (oTagValuesADM.added.length || oTagValuesADM.deleted.length || oTagValuesADM.modified.length) {
          bIsModified = true;
        }

        if(bIsModified) {
          delete oNewTag.tagValues;
          oNewTag.addedTagValues = (oTagValuesADM) ? oTagValuesADM.added : [];
          oNewTag.modifiedTagValues = (oTagValuesADM) ? oTagValuesADM.modified : [];
          oNewTag.deletedTagValues = (oTagValuesADM) ? oTagValuesADM.deleted : [];
          oADM.modified.push(oNewTag);
        }
      }
    });

    CS.forEach(aNewTags, function(oNewTag){
      if(CS.indexOf(aFoundIds, oNewTag.id) === -1){
        if(!CS.find(oADM.added, {id: oNewTag.id}) && !CS.isEmpty(oNewTag.tagValues)) {
          oADM.added.push(oNewTag);
        }
      }
    });

    return oADM;
  };

  let _fillTaskFormFieldsFromModels = (oTaskToSave) => {
    let aModels = TaskProps.getActiveTaskFormViewModels();
    CS.forEach(oTaskToSave.formFields, function (oFormField) {
      let oModel = CS.find(aModels, {id: oFormField.id});
      let sValue = oModel.value;
      if (oModel.type == EntityTasksFormViewTypes.NUMBER_FIELD) {
        sValue = parseInt(sValue);
      }
      oFormField.value.value = sValue;
    })
  };

  var _generateADMForTask = function (oActiveTask) {
    var oOriginalTask = oActiveTask.pureObject;
    delete  oActiveTask.pureObject;
    delete  oActiveTask.isDirty;
    var oTaskToSave = CS.cloneDeep(oActiveTask);

    oTaskToSave.addedComments = _getAddedComments(oOriginalTask.comments, oTaskToSave.comments);
    oTaskToSave.comments = [];
    oTaskToSave.addedInclusions = [];
    oTaskToSave.deletedInclusions = [];
    oTaskToSave.modifiedInclusions = [];
    delete oTaskToSave.inclusions;

    oTaskToSave.addedExclusions = [];
    oTaskToSave.deletedExclusions = [];
    oTaskToSave.modifiedExclusions = [];
    delete oTaskToSave.exclusions;

    var oADMForLinkedEntities = _generateADMForLinkedEntities(oOriginalTask.linkedEntities, oTaskToSave.linkedEntities);
    oTaskToSave.addedLinkedEntities = oADMForLinkedEntities.added;
    oTaskToSave.deletedLinkedEntities = oADMForLinkedEntities.deleted;
    oTaskToSave.modifiedLinkedEntities = oADMForLinkedEntities.modified;
    delete oTaskToSave.linkedEntities;

    var oADMForSubTasks = _generateADMForSubTasks(oOriginalTask.subTasks, oTaskToSave.subTasks);
    oTaskToSave.addedSubtasks = oADMForSubTasks.added;
    oTaskToSave.deletedSubtasks = oADMForSubTasks.deleted;
    oTaskToSave.modifiedSubtasks = oADMForSubTasks.modified;
    delete oTaskToSave.subTasks;

    var oADMForAttachments = _generateADMForAttachments(oOriginalTask.attachments, oTaskToSave.attachments);
    oTaskToSave.addedAttachments = oADMForAttachments.added;
    oTaskToSave.deletedAttachments = oADMForAttachments.deleted;
    delete oTaskToSave.attachments;

    /*if(oOriginalTask.roles && oTaskToSave.roles) {
      var aOldRoles = oOriginalTask.roles;
      var aNewRoles = oTaskToSave.roles;
      var oADMForRoles = _generateADMForRoles(aOldRoles, aNewRoles);
      oTaskToSave.addedRoles = oADMForRoles.added;
      oTaskToSave.deletedRoles = oADMForRoles.deleted;
      oTaskToSave.modifiedRoles = oADMForRoles.modified;
      delete oTaskToSave.roles;
    }*/
    _generateADMForRoles(oOriginalTask, oTaskToSave);


    var aOldTags = [];
    var aNewTags = [];
    oOriginalTask.status && aOldTags.push(oOriginalTask.status);
    oOriginalTask.priority && aOldTags.push(oOriginalTask.priority);
    oTaskToSave.status && aNewTags.push(oTaskToSave.status);
    oTaskToSave.priority && aNewTags.push(oTaskToSave.priority);

    var oADMForTags = _generateADMForTags(aOldTags, aNewTags);
    oTaskToSave.addedTags = oADMForTags.added;
    oTaskToSave.deletedTags = oADMForTags.deleted;
    oTaskToSave.modifiedTags = oADMForTags.modified;

    oTaskToSave.status = null;
    oTaskToSave.priority = null;
    _fillTaskFormFieldsFromModels(oTaskToSave);

    return oTaskToSave;
  };

  /****** Update Due Date and Overdue date to all subtask having overduedate more than its parent
   * overdue date *******/
  var _updateDueDateAndOverDueDateToAllSubTask = function (oActiveTask) {
    var aSubTasks = oActiveTask.subTasks;
    CS.forEach(aSubTasks, function (oTask) {
      (oTask.overDueDate > oActiveTask.overDueDate) && (oTask.overDueDate = oActiveTask.overDueDate);
      (oTask.dueDate > oActiveTask.dueDate) && (oTask.dueDate = oActiveTask.dueDate);
    });
  };

  let _completeTask = function () {
    var oActiveTask = TaskProps.getActiveTask();
    if(!!oActiveTask.isDirty){
      alertify.message(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
      return;
    }

    let aCheckedTaskList = TaskProps.getCheckedTaskList();
    let aTaskList = TaskProps.getTaskList();
    let aListOfIncompleteTasks = [];
    CS.forEach(aTaskList, (oTask) => {
      if (oTask.isCamundaCreated && (oTask.tagForList && oTask.tagForList.id != "taskdone" && aCheckedTaskList.includes(oTask.id))) {
        aListOfIncompleteTasks.push(oTask.id);
      }
    });

    let aListOfIds = {};
    if (CS.isEmpty(aListOfIncompleteTasks)) {
      aListOfIds.ids = [oActiveTask.id];
    } else {
      aListOfIds.ids = aListOfIncompleteTasks;
    }
    CS.postRequest(TaskRequestMapping.CompleteTaskBulk, {}, aListOfIds, successSaveBulkTaskCallback.bind(this, {}), failureSaveBulkTaskCallback);

    // _makeActiveTaskDirty();
    // _saveTask({}, true)
  };

  let prepareRequestModelForTaskValidation = function (oTaskToSave) {

    CS.forEach(oTaskToSave.modifiedSubtasks, (oSubTask) => {
      oTaskToSave.responsible.addedUserIds = CS.union(oTaskToSave.responsible.addedUserIds, oSubTask.responsible.addedUserIds);
      oTaskToSave.responsible.addedRoleIds = CS.union(oTaskToSave.responsible.addedRoleIds, oSubTask.responsible.addedRoleIds);

      oTaskToSave.accountable.addedUserIds = CS.union(oTaskToSave.accountable.addedUserIds, oSubTask.accountable.addedUserIds);
      oTaskToSave.accountable.addedRoleIds = CS.union(oTaskToSave.accountable.addedRoleIds, oSubTask.accountable.addedRoleIds);

      oTaskToSave.consulted.addedUserIds = CS.union(oTaskToSave.consulted.addedUserIds, oSubTask.consulted.addedUserIds);
      oTaskToSave.consulted.addedRoleIds = CS.union(oTaskToSave.consulted.addedRoleIds, oSubTask.consulted.addedRoleIds);

      oTaskToSave.informed.addedUserIds = CS.union(oTaskToSave.informed.addedUserIds, oSubTask.informed.addedUserIds);
      oTaskToSave.informed.addedRoleIds = CS.union(oTaskToSave.informed.addedRoleIds, oSubTask.informed.addedRoleIds);

      oTaskToSave.verify.addedUserIds = CS.union(oTaskToSave.verify.addedUserIds, oSubTask.verify.addedUserIds);
      oTaskToSave.verify.addedRoleIds = CS.union(oTaskToSave.verify.addedRoleIds, oSubTask.verify.addedRoleIds);

      oTaskToSave.signoff.addedUserIds = CS.union(oTaskToSave.signoff.addedUserIds, oSubTask.signoff.addedUserIds);
      oTaskToSave.signoff.addedRoleIds = CS.union(oTaskToSave.signoff.addedRoleIds, oSubTask.signoff.addedRoleIds);

    });

  };

  let _saveTask = function (oCallBack = {}, bIsTaskCompleteClicked) {
    var oActiveTask = TaskProps.getActiveTask();
    let oReferencedData = TaskProps.getReferencedData();
    var oReferencedTask = oReferencedData.referencedTasks;
    var oMasterTask = oActiveTask && oReferencedTask[oActiveTask.types[0]];
    let oTaskTypeDictionary = new TaskTypeDictionary();
    if (CS.isEmpty(oActiveTask.name)) {
      CommonUtils.showError(getTranslation().TASK_NAME_EMPTY);
      return;
    } else {
      var sLabel = CS.trim(oActiveTask.name);
      if (sLabel) {
        oActiveTask.name = sLabel;
      }
    }

    if (!oActiveTask.isDirty && !oActiveTask.isCreated) {
      alertify.message(getTranslation().SCREEN_STORE_SAVE_NOTHING_CHANGED);
      return;
    }

    if(oActiveTask.roles) {
      var oResponsibleRole = CS.find(oActiveTask.roles, {roleId: RoleIdDictionary.ResponsibleRoleId});
      if (oResponsibleRole && oMasterTask.type === oTaskTypeDictionary.SHARED) {
        var aCandidates = oResponsibleRole.candidates;
        if (!aCandidates.length) {
          alertify.message(getTranslation().SELECT_AT_LEAST_ONE_RESPONSIBLE_FOR_TASK);
          return;
        }
      }
    }

    var oActiveSubTask = TaskProps.getActiveSubTask();
    if (!CS.isEmpty(oActiveSubTask)) {
      var oSubTask = CS.find(oActiveTask.subTasks, {id: oActiveSubTask.id});
      CS.assign(oSubTask, oActiveSubTask);
    }

    // _updateDueDateAndOverDueDateToAllSubTask(oActiveTask);

    if (oActiveTask.isCreated) {
      if (CS.isEmpty(oActiveTask.types)) {
        alertify.message(getTranslation().PLEASE_SELECT_TASK_TYPE);
        return;
      }

      delete oActiveTask.isCreated;
      CS.putRequest(TaskRequestMapping.CreateTask, {}, oActiveTask, successCreateTaskCallback, failureCreateTaskCallback);
    } else {
      let oCommentAttachmentData = TaskProps.getTemporaryCommentAttachmentData();
      let sCommentText =  TaskProps.getTemporaryCommentText();
      if(CS.isNotEmpty(sCommentText) || CS.isNotEmpty(oCommentAttachmentData)){
        _createTaskComment();
      }
      let oTaskToSave = _generateADMForTask(CS.cloneDeep(oActiveTask));
      oCallBack.taskToSave = CS.cloneDeep(oTaskToSave);
      prepareRequestModelForTaskValidation(oTaskToSave);
      CS.postRequest(TaskRequestMapping.ValidateUserAndRolesForTask, {}, oTaskToSave, successValidateUserAndRolesForTaskCallback.bind(this, oCallBack), failureValidateUserAndRolesForTaskCallback);
    }
  };

  let successValidateUserAndRolesForTaskCallback = function (oCallBack, oResponse) {
    let aUnAuthorizedUsersOrRoles = oResponse.success.unAuthorizedUsersOrRoles;
    if (CS.isEmpty(aUnAuthorizedUsersOrRoles)) {
      CS.postRequest(TaskRequestMapping.SaveTask, {}, oCallBack.taskToSave, successSaveTaskCallback.bind(this, oCallBack), failureSaveTaskCallback);
    } else {
      alertify.error(`${getTranslation().PLEASE_CHECK_PERMISSIONS_FOR} ${aUnAuthorizedUsersOrRoles.join(', ')}`);
    }
  };

  let failureValidateUserAndRolesForTaskCallback = function (oResponse) {
    CommonUtils.failureCallback(oResponse, "failureValidateUserAndRolesForTaskCallback", getTranslation())
  };

  var _handleTaskFileAttachmentUpload = function (aFiles, sContext) {
    let oCallback = {};
    oCallback.functionToExecute = _addAssetToAttachments;
    oCallback.context = sContext;
    EventBus.dispatch(Events.TASK_FILE_ATTACHMENT_VIEW_FILE_UPLOAD_CLICKED, 'taskFileAttachment', aFiles, oCallback);
  };

  var _handleCommentTaskFileAttachmentUpload = function (aFiles, sContext) {
    let oCallback = {};
    oCallback.functionToExecute = _addAssetToCommentAttachments;
    oCallback.context = sContext;
    EventBus.dispatch(Events.TASK_FILE_ATTACHMENT_VIEW_FILE_UPLOAD_CLICKED, 'commentFileAttachment', aFiles, oCallback);
  };

  var _openProductFromDashboard = function (sId, sBaseType) {
    let sPortalId = "pim";
    window.open(SharableURLStore.getSharableURL(sId, sBaseType, sPortalId));
  };

  var _handleCommmentTaskFileAttachmentRemoveClicked = function (sId) {

    var oTaskViewProps = TaskProps.getTaskViewProps();

    CS.remove(oTaskViewProps.commentTemporaryData.commentAttachmentData, function (oCommentData) {
      return oCommentData.id == sId;
    });

    _triggerChange();
  };

  var _handleFileAttachmentDetailViewOpen = function (sAttachmentId) {
    TaskProps.setOpenAttachmentId(sAttachmentId);

    _triggerChange();
  };

  var _handleFileAttachmentDetailViewClose = function () {
    TaskProps.setOpenAttachmentId('');

    _triggerChange();
  };

  let _handleFileAttachmentGetAllAssetExtensions = function (oRefDom) {
    EventBus.dispatch(Events.TASK_FILE_ATTACHMENT_VIEW_GET_ALL_ASSET_EXTENSIONS, oRefDom, "allAssets");
  };



  var _handleTaskFileAttachmentRemoveClicked = function (sId) {
    _setTasksScreenLockStatus(true);
    var oActiveTask = _makeActiveTaskDirty();
    var oActiveSubTask = TaskProps.getActiveSubTask();
    var oTaskViewProps = TaskProps.getTaskViewProps();

    var oTask = (!CS.isEmpty(oActiveSubTask)) ? oActiveSubTask : oActiveTask;
    CS.remove(oTask.attachments, function (sKey) {
      if (CS.includes(sId, sKey)) {
        return true;
      }
    });

    CS.remove(oTaskViewProps.attachmentData, function (oData) {
      return oData.id == sId;
    });

    let oDataToUpdateInParent = {
      isTaskDirty: true
    };
    EventBus.dispatch(Events.TASK_DATA_CHANGED, oDataToUpdateInParent);
  };

  var _addAssetToCommentAttachments = function (oUploadResponse, aData) {

    var index = 0; //todo:temporary fix

    CS.forEach(aData, function (oData) {

      var oInstance = oData;
      let oImage = oInstance.assetInformation;
      var sMp4Src = "";

      var sImageSrc = RequestMapping.getRequestUrl(CommonModuleRequestMapping.GetAssetImage, {
        type: oImage.type,
        id: oImage.assetObjectKey
      });

      var sThumbnailSrc = RequestMapping.getRequestUrl(CommonModuleRequestMapping.GetAssetImage, {
        type: oImage.type,
        id: oImage.thumbKey
      });

      var sPreviewSrc = RequestMapping.getRequestUrl(CommonModuleRequestMapping.GetAssetImage, {
        type: oImage.type,
        id: oImage.previewImageKey
      });

      if(oImage.properties.mp4){
        sMp4Src = RequestMapping.getRequestUrl(CommonModuleRequestMapping.GetAssetImage, {
          type: oImage.type,
          id: oImage.properties.mp4
        });
      }

      var oCommentAttachmentObject = {
        id: oUploadResponse.success[index].id, //todo: temporary fix
        type: oImage.type,
        imageKey: oImage.assetObjectKey,
        thumbKey: oImage.thumbKey,
        previewKey: oImage.previewImageKey,
        imageSrc: sImageSrc,
        thumbKeySrc: sThumbnailSrc,
        previewSrc: sPreviewSrc,
        mp4Src: sMp4Src,
        extension: oImage.properties.extension
      };

      index++;
      TaskProps.addToTemporaryCommmentAttachmentData(oCommentAttachmentObject);
    });
    let oActiveTask = _makeActiveTaskDirty();
    EventBus.dispatch(Events.TASK_DATA_CHANGED, {isTaskDirty: true});
    _triggerChange();
  };


  var _addAssetToAttachments = function (oResponse, aAssetsList) {
    _setTasksScreenLockStatus(true);
    var oActiveTask = _makeActiveTaskDirty();
    var aSuccessResponses = oResponse.success;
    var oActiveSubTask = TaskProps.getActiveSubTask();

    CS.forEach(aSuccessResponses, function (oSuccessResponse) {
      if(!CS.isEmpty(oActiveSubTask)){
        oActiveSubTask.attachments.push(oSuccessResponse.id);
      } else {
        oActiveTask.attachments.push(oSuccessResponse.id);
      }
    });
    _saveTask();
  };

  var _setAttachmentsData = function () {
    var oActiveTask = TaskProps.getActiveTask();
    var oCommentAttachmentDataMap = {};
    TaskProps.setAttachmentData([]);
    TaskProps.setCommentAttachmentDataMap({});
    TaskProps.setAttachmentData(_getAttachmentDataArray(oActiveTask.attachments, oActiveTask.referencedAssets));
    CS.forEach(oActiveTask.comments, function (oComment) {
      var aCommentAttachmentData = _getAttachmentDataArray(oComment.attachments, oActiveTask.referencedAssets);
      oCommentAttachmentDataMap[oComment.id] = aCommentAttachmentData;
    });
    TaskProps.setCommentAttachmentDataMap(oCommentAttachmentDataMap);
  };

  var _setSubTasksAttachmentsData = function () {
    var oActiveTask = TaskProps.getActiveTask();
    var oActiveSubTask = TaskProps.getActiveSubTask();
    var oCommentAttachmentDataMap = {};
    TaskProps.setAttachmentData([]);
    TaskProps.setCommentAttachmentDataMap({});
    TaskProps.setAttachmentData(_getAttachmentDataArray(oActiveSubTask.attachments, oActiveTask.referencedAssets));
    CS.forEach(oActiveSubTask.comments, function (oComment) {
      var aCommentAttachmentData = _getAttachmentDataArray(oComment.attachments, oActiveTask.referencedAssets);
      oCommentAttachmentDataMap[oComment.id] = aCommentAttachmentData;
    });
    TaskProps.setCommentAttachmentDataMap(oCommentAttachmentDataMap);
  };

  var _getAttachmentDataArray = function(aAttachments, oReferencedAssets){
    var aAttachmentData = [];
    CS.forEach(aAttachments, function (sId) {
      var oReferencedAsset = oReferencedAssets[sId];
      if(!oReferencedAsset){
        return;
      }
      var sMp4src = "";

      var sImageSrc = RequestMapping.getRequestUrl(UploadRequestMapping.GetAssetImage, {
        type: oReferencedAsset.type,
        id: oReferencedAsset.assetObjectKey
      });

      var sThumbnailSrc = RequestMapping.getRequestUrl(UploadRequestMapping.GetAssetImage, {
        type: oReferencedAsset.type,
        id: oReferencedAsset.thumbKey
      });

      var sPreviewSrc = RequestMapping.getRequestUrl(UploadRequestMapping.GetAssetImage, {
        type: oReferencedAsset.type,
        id: oReferencedAsset.previewKey
      });

      if(oReferencedAsset.properties.mp4){
        sMp4src = RequestMapping.getRequestUrl(UploadRequestMapping.GetAssetImage, {
          type: oReferencedAsset.type,
          id: oReferencedAsset.properties.mp4
        });
      }

      var oAttachmentObject = {
        id: sId,
        type: oReferencedAsset.type,
        imageKey: oReferencedAsset.assetObjectKey,
        thumbKey: oReferencedAsset.thumbKey,
        previewKey: oReferencedAsset.previewKey,
        imageSrc: sImageSrc,
        thumbKeySrc: sThumbnailSrc,
        previewSrc: sPreviewSrc,
        mp4Src: sMp4src,
        extension: oReferencedAsset.properties.extension
      };
      aAttachmentData.push(oAttachmentObject);
    });
    return aAttachmentData;
  };

  var _getFilteredListByLevel = function (sId, sContext) {
    var aTaskList = TaskProps.getTaskList();
    let oTaskData = TaskProps.getTaskData();
    var oActiveContent = oTaskData.activeContent;
    var aFilteredList = [];
    var oGroupByRoleTaskListMap = [];

    switch (sContext) {
      case "taskDashboardRole":
      case "all":
      case RoleIdDictionary.AccountableRoleId:
      case RoleIdDictionary.ResponsibleRoleId:
      case RoleIdDictionary.InformedRoleId:
      case RoleIdDictionary.VerifyRoleId:
      case RoleIdDictionary.SignOffRoleId:
      case RoleIdDictionary.ConsultedRoleId:
        if (sId === TaskDictionary.otherTasks) {
          oGroupByRoleTaskListMap = _getGroupByRoleTaskListForDashboard(aTaskList, sId);
          aFilteredList = oGroupByRoleTaskListMap[sId].tasks;
        } else {
          aFilteredList = aTaskList;
        }
        break;

      case "contentAll":
        aFilteredList = CS.filter(aTaskList, function (oTask) {
          var aLinkedEntities = oTask.linkedEntities;
          var oLinkedEntity = CS.find(aLinkedEntities, {contentId: oActiveContent.id});
          return CS.isEmpty(oLinkedEntity.elementId);
        });
        break;

      case "propertyAll":
        aFilteredList = CS.filter(aTaskList, function (oTask) {
          var aLinkedEntities = oTask.linkedEntities;
          var oLinkedEntity = CS.find(aLinkedEntities, {contentId: oActiveContent.id});
          return !CS.isEmpty(oLinkedEntity.elementId);
        });
        break;

      case "variant":
        aFilteredList = CS.filter(aTaskList, function (oTask) {
          var aLinkedEntities = oTask.linkedEntities;
          var oLinkedEntity = CS.find(aLinkedEntities, {contentId: oActiveContent.id});
          return (CS.isEmpty(oLinkedEntity.elementId) && oLinkedEntity.variantId == sId);
        });
        break;

      case "property":
        aFilteredList = CS.filter(aTaskList, function (oTask) {
          var aLinkedEntities = oTask.linkedEntities;
          var oLinkedEntity = CS.find(aLinkedEntities, {contentId: oActiveContent.id});
          return oLinkedEntity.elementId == sId;
        });
        break;

      case "content":
        aFilteredList = CS.filter(aTaskList, function (oTask) {
          var aLinkedEntities = oTask.linkedEntities;
          var oLinkedEntity = CS.find(aLinkedEntities, {contentId: oActiveContent.id});
          return oLinkedEntity.contentId == sId && CS.isEmpty(oLinkedEntity.variantId) && CS.isEmpty(oLinkedEntity.elementId);
        });
        break;
    }

    return aFilteredList;
  };

  var _getGroupByTypeTaskList = function (aFilteredList, sContext) {
    let oReferencedData = TaskProps.getReferencedData();
    var aMasterTasks = oReferencedData.referencedTasks;
    var oReferencedPermission = oReferencedData.referencedPermissions;

    var oGroupByTypeTaskListMap = {};
    var aList = [];
    var bShowAddNewTask = false;
    if (TaskProps.getShouldUsePropPermissions()) {
      bShowAddNewTask = TaskProps.getTaskCanCreate();
    } else {
      bShowAddNewTask = oReferencedPermission.tabPermission && oReferencedPermission.tabPermission.canCreate;
    }
    var bIsFirstLevelContext = (sContext == "all" || sContext == "contentAll" || sContext == "propertyAll");

    if (bIsFirstLevelContext) {
      bShowAddNewTask = false;
      if (CS.isEmpty(aFilteredList)) {
        return oGroupByTypeTaskListMap;
      }
    }

    CS.forEach(aMasterTasks, function (oMasterTask) {
      aList = CS.filter(aFilteredList, function (oEvent) {
        var sType = oEvent.types[0];
        return sType === oMasterTask.id;
      });

      if (bIsFirstLevelContext) {
        if (!CS.isEmpty(aList)) {
          oGroupByTypeTaskListMap[oMasterTask.id] = {
            "bShowAddNewTask": bShowAddNewTask,
            "type": oMasterTask.id,
            "label": oMasterTask.label,
            "code": oMasterTask.code,
            "tasks": aList,
            "isCollapsed": false
          };
        }
      } else {
        if (TaskProps.getTaskMode() === TaskViewContextConstants.DASHBOARD_WORKFLOW_WORKBENCH_TASK && !CS.isEmpty(aList)) {
          oGroupByTypeTaskListMap[oMasterTask.id] = {
            "bShowAddNewTask": bShowAddNewTask,
            "type": oMasterTask.id,
            "label": oMasterTask.label,
            "code": oMasterTask.code,
            "tasks": aList,
            "isCollapsed": false
          };
        }
        else {
          oGroupByTypeTaskListMap[oMasterTask.id] = {
            "bShowAddNewTask": bShowAddNewTask,
            "type": oMasterTask.id,
            "label": oMasterTask.label,
            "code": oMasterTask.code,
            "tasks": aList,
            "isCollapsed": false
          };
        }
      }
    });

    return oGroupByTypeTaskListMap;
  };

  var _getGroupByRoleTaskListForDashboard = function (aFilteredList, sContext) {
    var aFirstLevelContext = ["all","contentAll","propertyAll","taskDashboardAll",RoleIdDictionary.AccountableRoleId,
                              RoleIdDictionary.ConsultedRoleId, RoleIdDictionary.InformedRoleId, RoleIdDictionary.SignOffRoleId,
                              RoleIdDictionary.VerifyRoleId, RoleIdDictionary.ResponsibleRoleId,TaskDictionary.otherTasks];
    let oReferencedData = TaskProps.getReferencedData();
    var aTaskTypes = oReferencedData.referencedTasks;
    var oReferencedRole = oReferencedData.referencedRoles;
    var oReferencedPermission = oReferencedData.referencedPermissions;
    var oGroupByTypeTaskListMap = {};
    var aList = [];
    var bShowAddNewTask = false;
    if(TaskProps.getShouldUsePropPermissions()) {
      bShowAddNewTask = TaskProps.getTaskCanCreate();
    } else {
      bShowAddNewTask = oReferencedPermission.tabPermission && oReferencedPermission.tabPermission.canCreate;
    }
    var bIsFirstLevelContext = CS.includes(aFirstLevelContext, sContext);

    if (bIsFirstLevelContext) {
      bShowAddNewTask = false;
      if(CS.isEmpty(aFilteredList)){
        return oGroupByTypeTaskListMap;
      }
    }
    var oTaskItemViewByRolesId = {};
    CS.forEach(aTaskTypes, function (oTaskType) {
      aList = CS.filter(aFilteredList, {types: [oTaskType.id]}) || [];
      CS.forEach(aList, function (oTask) {
        var aRoles = oTask.roles;
        if(CS.isEmpty(aRoles)) {
          if (!oTaskItemViewByRolesId[TaskDictionary.otherTasks]) {
            oTaskItemViewByRolesId[TaskDictionary.otherTasks] = [];
          }
          oTaskItemViewByRolesId[TaskDictionary.otherTasks].push(oTask);
        }
        else {
          CS.forEach(aRoles, function (oRole) {
            if (!oTaskItemViewByRolesId[oRole.roleId]) {
              oTaskItemViewByRolesId[oRole.roleId] = [];
            }
            oTaskItemViewByRolesId[oRole.roleId].push(oTask);
          });
        }
      });
    });

    CS.forEach(oTaskItemViewByRolesId, function (aTaskList, sKey) {
      var sLabel = oReferencedRole[sKey] && oReferencedRole[sKey].label || "Others";
      if(bIsFirstLevelContext) {
        if(!CS.isEmpty(aTaskList)){
          oGroupByTypeTaskListMap[sKey] = {
            "bShowAddNewTask": bShowAddNewTask,
            "type": "",
            "label": sLabel,
            "tasks": aTaskList,
            "isCollapsed": false
          };
        }
      } else {
        oGroupByTypeTaskListMap[sKey] = {
          "bShowAddNewTask": bShowAddNewTask,
          "type": "",
          "label": sLabel,
          "tasks": aTaskList,
          "isCollapsed": false
        };
      }
    });

    return oGroupByTypeTaskListMap;
  };

  var _handleListCollapsedStateChanged = function (sTypeId) {
    var oGroupByTypeMap = TaskProps.getTaskListGroupByType();
    var oGroup = oGroupByTypeMap[sTypeId];
    oGroup.isCollapsed = !oGroup.isCollapsed;
    _triggerChange();
  };

  var setDefaultActiveTask = function (oGroupByTypeTaskListMap) {
    var oActiveTask = TaskProps.getActiveTask();
    var oDefaultTask = {};
    var bTaskFound = false;

    CS.forEach(oGroupByTypeTaskListMap, function (oGroup) {
      if(!CS.isEmpty(oGroup.tasks) && CS.isEmpty(oActiveTask) && !bTaskFound) {
        bTaskFound = true;
        oDefaultTask = oGroup.tasks[0];
        _getTaskDetails(oDefaultTask.id);
      }
    });
  };

  var _updateGroupByModel = function (aFilteredList, sContext) {
    var oGroupByTypeTaskListMap = {};

    var sActiveGroupByType = TaskProps.getActiveGroupByType();
    if(sActiveGroupByType == "type") {
      oGroupByTypeTaskListMap = _getGroupByTypeTaskList(aFilteredList, sContext);
    } else if (sActiveGroupByType == "dueDate"){
      oGroupByTypeTaskListMap = _getGroupByDueDateTaskList(aFilteredList);
    } else if(sActiveGroupByType == "priority"){
      oGroupByTypeTaskListMap = _getGroupByPriorityTaskList(aFilteredList);
    }else if(sActiveGroupByType == "status"){
      oGroupByTypeTaskListMap = _getGroupByStatusTaskList(aFilteredList);
    }

    return oGroupByTypeTaskListMap;
  };

  var _handleTaskListLevelClicked = function (sId, sContext) {
    var aFilteredList = _getFilteredListByLevel(sId, sContext);
    var oGroupByTypeTaskListMap = _updateGroupByModel(aFilteredList, sContext);

    TaskProps.setActiveTask({});
    setDefaultActiveTask(oGroupByTypeTaskListMap);
    TaskProps.setCheckedTaskList([]);
    var oActiveTaskLevel = _getTaskLevelListItemById(sId, 0);
    TaskProps.setActiveTaskLevel(oActiveTaskLevel);
    TaskProps.setTaskListGroupByType(oGroupByTypeTaskListMap);
    TaskProps.setFilteredList(aFilteredList);
    EventBus.dispatch(Events.TASK_DATA_CHANGED, {activeTask: {}, checkedTaskList: []});
  };

  var _handleTaskListLevelClickedForDashboard = function (aTaskInstanceList, sRoleId, sContext, oDataToUpdate, oCallBack) {
    var aFilteredList = [];
    var oGroupByRoleTaskListMap = {};

    if (sRoleId === TaskDictionary.otherTasks) {
      oGroupByRoleTaskListMap = _getGroupByRoleTaskListForDashboard(aTaskInstanceList, sContext);
      CS.forEach(oGroupByRoleTaskListMap, function (oType, sKey) {
        if (sKey !== sRoleId) {
          delete oGroupByRoleTaskListMap[sKey];
        }
      });
      aFilteredList = oGroupByRoleTaskListMap[sRoleId];
      if (aFilteredList) {
        aFilteredList = aFilteredList.tasks;
      }
      else {
        aFilteredList = [];
      }
    } else {
      aFilteredList = aTaskInstanceList;
    }
    var oGroupByTaskListMap = _updateGroupByModel(aFilteredList, sContext);
    TaskProps.setTaskListGroupByType(oGroupByTaskListMap);
    TaskProps.setFilteredList(aFilteredList);

    let oDataToUpdateInParent = {
      tasksCount: aFilteredList.length
    };
    EventBus.dispatch(Events.TASK_DATA_CHANGED, CS.assign(oDataToUpdateInParent, oDataToUpdate));
    oCallBack && oCallBack.functionToExecute && oCallBack.functionToExecute();
  };

  var _getFilteredListForProperty = function (sPropertyId, sVariantId) {
    var aFilteredList = [];
    var aTaskList = TaskProps.getTaskList();
    let oTaskData = TaskProps.getTaskData();
    var oActiveContent = oTaskData.activeContent;
    var sSplitter = ViewUtils.getSplitter();
    var sNewId = CommonUtils.getNewSuffix();
    var sId = sPropertyId;
    if(sId && sId.indexOf(sNewId) >= 0) {
      sId = sId.split(sSplitter)[0];
    }
    if(sVariantId) {
      aFilteredList = CS.filter(aTaskList, function (oTask) {
        var aLinkedEntities = oTask.linkedEntities;
        var oLinkedEntity = CS.find(aLinkedEntities, {contentId: oActiveContent.id});
        return oLinkedEntity.elementId == sId && oLinkedEntity.variantId == sVariantId;
      });
    } else {
      aFilteredList = CS.filter(aTaskList, function (oTask) {
        var aLinkedEntities = oTask.linkedEntities;
        var oLinkedEntity = CS.find(aLinkedEntities, {contentId: oActiveContent.id});
        return oLinkedEntity.elementId == sId && CS.isEmpty(oLinkedEntity.variantId);
      });
    }

    return aFilteredList;
  };

  var _updateTaskMap = function () {
    var oActiveTaskLevel = TaskProps.getActiveTaskLevel();
    var aTasksList = TaskProps.getTaskList();

    var oProperty = TaskProps.getActiveProperty();
    var sPropertyId = oProperty && oProperty.id || "";
    var sContext = (oActiveTaskLevel && oActiveTaskLevel.listItemKey) ?  oActiveTaskLevel.listItemKey : "";
    var aFilteredList = [];
    let oTaskData = TaskProps.getTaskData();
    var oActiveContent = oTaskData.activeContent;
    var sVariantId = (oActiveContent && oActiveContent.variantInstanceId) ? oActiveContent.variantInstanceId : "";

    if(sPropertyId){
      aFilteredList = _getFilteredListForProperty(sPropertyId, sVariantId);
    } else {
      aFilteredList = _getFilteredListByLevel(oActiveTaskLevel.id, sContext);
    }
    //To update Count
    if (TaskProps.getTaskMode() !== TaskViewContextConstants.DASHBOARD_WORKFLOW_WORKBENCH_TASK) {
      _makeContentsAndPropertiesListHavingTasks();
    }

    var oGroupByTypeTaskListMap = _updateGroupByModel(aFilteredList, sContext);
    TaskProps.setTaskListGroupByType(oGroupByTypeTaskListMap);
    TaskProps.setFilteredList(aFilteredList);
  };

  var successCreateTaskCallback = function (oResponse) {
    var aTaskList = TaskProps.getTaskList();
    var oCreatedTask = oResponse.success;
    let oReferencedData = TaskProps.getReferencedData();
    var oReferencedTasks = oReferencedData.referencedTasks;
    var oActiveTaskType = oReferencedTasks[oCreatedTask.types[0]];
    let oConfigDetails = oCreatedTask.configDetails;

    aTaskList.push(oCreatedTask);
    TaskProps.setActiveTask(oCreatedTask);
    _setRoleDetails(oCreatedTask);
    _updateTagForTask(oCreatedTask);
    _setTagDetailsForTask(oActiveTaskType);
    TaskProps.setActiveSubTask({});
    TaskProps.setIsTaskDetailViewOpen(true);
    TaskProps.setEditableTaskId(oCreatedTask.id);
    TaskProps.setCommentAttachmentDataMap({});
    TaskProps.setAttachmentData([]);
    TaskProps.setTemporaryCommentText("");
    TaskProps.setTemporaryCommentAttachmentData([]);
    TaskProps.getFilteredTaskList().push(oCreatedTask.id);
    if(TaskProps.getIsAllTaskChecked()){
      TaskProps.addToCheckedTaskList(oCreatedTask.id);
    }
    _updateTaskMap();

    var oActiveProperty = TaskProps.getActiveProperty();
    var sElementId = oActiveProperty && oActiveProperty.instanceId || "";
    var sSplitter = CommonUtils.getSplitter();
    var sNewId = CommonUtils.getNewSuffix();

    let oDataToUpdateInParent = {}
    if(sElementId && sElementId.indexOf(sNewId) >= 0){
      sElementId = sElementId.split(sSplitter)[0];
      oDataToUpdateInParent.isTaskAdded = true;
    }
    var aPropertyIdsHavingTasks = TaskProps.getPropertyIdsHavingTask();
    if (aPropertyIdsHavingTasks && !CS.includes(aPropertyIdsHavingTasks, sElementId)) {
      aPropertyIdsHavingTasks.push(oActiveProperty.instanceId);
      oDataToUpdateInParent.propertyIdsHavingTasks = aPropertyIdsHavingTasks;
    }
    alertify.success(getTranslation().SUCCESSFULLY_SAVED);
    oDataToUpdateInParent.tasksCount = aTaskList.length;
    oDataToUpdateInParent.isTaskDirty = false;
    oDataToUpdateInParent.activeTask = oCreatedTask;
    let oTaskData = TaskProps.getTaskData();
    oTaskData.referencedData = CS.merge(oConfigDetails.referencedRoles, oConfigDetails.referencedUsers);
    EventBus.dispatch(Events.TASK_DATA_CHANGED, oDataToUpdateInParent);
  };

  var failureCreateTaskCallback = function (oResponse) {
    var aExceptionDetails = oResponse.failure ? oResponse.failure.exceptionDetails : [];
    if (!CS.isEmpty(aExceptionDetails)  && aExceptionDetails[0].key == 'UserNotHaveCreatePermission') {
      alertify.error(getTranslation().UserNotHaveCreatePermission);
      return;
    }
    CommonUtils.failureCallback(oResponse, "failureCreateTaskCallback", "");
    _triggerChange();
  };

  var _handleAddNewTaskClicked = function (sTypeId, oContent) {
    var oActiveTask = TaskProps.getActiveTask();
    if (oActiveTask && oActiveTask.isDirty) {
      var oProperty = TaskProps.getActiveProperty();

      if(!CS.isEmpty(oProperty)){
        _saveOrDiscardTask(_createTask.bind(this, sTypeId, oContent));
      } else {
        alertify.message(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
      }
    } else if(_activeTaskCommentSafetyCheck()) {
      _createTask(sTypeId, oContent);
    }
  };

  var _createTask = function (sTypeId, oContent) {
    var oCreatedTaskMaster = _createDefaultTaskMasterObject(sTypeId, "", "", oContent);

    CS.putRequest(TaskRequestMapping.CreateTask, {}, oCreatedTaskMaster, successCreateTaskCallback, failureCreateTaskCallback);
  };

  var _getNewTagValue = function (sTagId, iRelevance) {
    const iDefaultRelevance = 0;
    return  {
      id: UniqueIdentifierGenerator.generateUUID(),
      tagId: sTagId,
      relevance: iRelevance || iDefaultRelevance,
      timestamp: new Date().getTime()
    };
  };

  var _addDefaultTagToTask = function (sTagId, oMasterTag) {
    var oTag = {};

    oTag.id = UniqueIdentifierGenerator.generateUUID();
    oTag.tagValues = [];
    oTag.tagId = oMasterTag.id;
    oTag.baseType = BaseTypesDictionary.tagInstanceBaseType;

    if(sTagId){
      oTag.tagValues.push(_getNewTagValue(sTagId, 100));
    }
    oTag.isValueChanged = true;

    return oTag;
  };

  var _updateTagValuesInTag = function (sChildTagId, oTagGroup) {
    var aTagValues = [];
    if(sChildTagId){
      aTagValues.push(_getNewTagValue(sChildTagId, 100));
    }
    oTagGroup.tagValues = aTagValues;
    oTagGroup.isValueChanged = true;

    return oTagGroup;
  };

  var _setTagDetailsForTask = function (oActiveTaskType) {
    let oReferencedData = TaskProps.getReferencedData();
    var oReferencedTags = oReferencedData.referencedTags;
    var oActiveTask = TaskProps.getActiveTask();
    let oTaskTypeDictionary = new TaskTypeDictionary();
    //set priority tag details
    if (!CS.isEmpty(oActiveTaskType)) {
      var oMasterPriority = oReferencedTags[oActiveTaskType.priorityTag] || null;
      TaskProps.setPriorityTagDetails(oMasterPriority);
    } else {
      TaskProps.setPriorityTagDetails({});
    }

    //set status tag details
    var oMasterStatus = null;
    if(oActiveTaskType && oActiveTaskType.type === oTaskTypeDictionary.PERSONAL) {
      oMasterStatus = oReferencedTags[oActiveTaskType.statusTag] || null;
    } else {
      oMasterStatus = oReferencedTags[TagIdDictionary.TaskStatusId];
    }
    TaskProps.setStatusTagDetails(oMasterStatus);
  };

  var _getRoleIdsHavingUserFromInstances = function (aRoleInstances, sUserId, sCurrentRoleId) {

    let aUpdatedRoleIds = [];

    CS.forEach(aRoleInstances, function (oRoleInstance, sRoleId) {
      let aUserIds = CS.isNotEmpty(oRoleInstance) ? oRoleInstance.userIds : [];
      let aRoleIds = CS.isNotEmpty(oRoleInstance) ? oRoleInstance.roleIds : [];
      let bIsUserInRole = CS.includes(aUserIds, sUserId);
      let bIsRoleInRole = CS.includes(aRoleIds, sCurrentRoleId);

      if(bIsUserInRole || bIsRoleInRole){
          aUpdatedRoleIds.push(sRoleId);
      }
    });

    return aUpdatedRoleIds;
  };

  var _setTaskWorkflowDetails = function (oTaskInstance) {
    var oCurrentUser = CommonUtils.getCurrentUser();
    let oRolesFromTask = _getRolesForTaskInstance(oTaskInstance);
    let aRoleIds = _getRoleIdsHavingUserFromInstances(oRolesFromTask, oCurrentUser.id, oCurrentUser.roleId );
    var oRes = {};

    CS.forEach(MockDataForTaskWorkflow, function (oSettings, sTagId) {
      var aCombinedTagIds = [];
      CS.forEach(oSettings, function (aTagIds, sRoleId) {
        if(CS.includes(aRoleIds, sRoleId)){
          aCombinedTagIds = aCombinedTagIds.concat(aTagIds);
        }
      });

      oRes[sTagId] = CS.uniq(aCombinedTagIds);
    });

    TaskProps.setWorkFlowStatusPossibleTagValues(oRes);
  };

  let _getRolesForTaskInstance = function (oTaskInstance) {
    let oRole = {};
    oRole[RoleIdDictionary.AccountableRoleId] = oTaskInstance.accountable;
    oRole[RoleIdDictionary.ResponsibleRoleId] = oTaskInstance.responsible;
    oRole[RoleIdDictionary.ConsultedRoleId] = oTaskInstance.consulted;
    oRole[RoleIdDictionary.InformedRoleId] = oTaskInstance.informed;
    oRole[RoleIdDictionary.SignOffRoleId] = oTaskInstance.signoff;
    oRole[RoleIdDictionary.VerifyRoleId] = oTaskInstance.verify;
    return oRole;
  };

  var _setEditableRoles = function (oTaskInstance) {
    let oCurrentUser = CommonUtils.getCurrentUser();
    let oRolesFromTask = _getRolesForTaskInstance(oTaskInstance);
    let aRoleIds = _getRoleIdsHavingUserFromInstances(oRolesFromTask, oCurrentUser.id, oCurrentUser.roleId );
    let aEditableRoles = [];

    CS.forEach(MockDataForTasksEditableRoles, function (aValidRoles, sRoleId) {
      if(CS.includes(aRoleIds, sRoleId)){
        aEditableRoles = CS.union(aEditableRoles, aValidRoles);
      }
    });

    TaskProps.setEditableRoles(aEditableRoles);
  };

  var _setRoleDetails = function (oTask) {
    let oTaskTypeDictionary = new TaskTypeDictionary();
    let oReferencedData = TaskProps.getReferencedData();
    var oReferencedTask = oReferencedData.referencedTasks;
    var oMasterTask = oTask && oReferencedTask[oTask.types[0]];
    var sType = oMasterTask && oMasterTask.type;
    if(sType === oTaskTypeDictionary.SHARED) {
      _setTaskWorkflowDetails(oTask);
      _setEditableRoles(oTask);
    }
  };

  let _createIdLabelArray = (oObj) => {
    let aArrayToReturn = [];
    CS.forEach(oObj, function (sVal, sKey) {
      aArrayToReturn.push({id: sKey, label: sVal})
    });
    return aArrayToReturn;
  };

  let _setTaskFormViewModels = (oTask) => {
    if (oTask.isCamundaCreated) {
      let aTaskFormModels = [];
      let oMapToSwapContext = {
        "enum": EntityTasksFormViewTypes.MSS,
        "string": EntityTasksFormViewTypes.TEXT_FIELD,
        "long": EntityTasksFormViewTypes.NUMBER_FIELD,
        "boolean": EntityTasksFormViewTypes.BOOLEAN,
        "cutype": EntityTasksFormViewTypes.MSS,
        "date": EntityTasksFormViewTypes.DATE_FIELD,
      };

      let aTasksFormData = oTask.formFields;
      let aPromises = [];
      CS.forEach(aTasksFormData, function (oTaskFormData) {
        let oModelToPush = {};
        //put all view related properties in this object
        let oViewProperties = {};
        oModelToPush.id = oTaskFormData.id;
        oModelToPush.label = oTaskFormData.label;
        let oTaskFormValueData = oTaskFormData.value;
        let oTaskFormTypeData = oTaskFormData.type;
        let sType = oMapToSwapContext[oTaskFormTypeData.name];
        let oCallBackData = {};
        let functionToExecute = function (aUsers) {
          switch (sType) {
            case EntityTasksFormViewTypes.MSS:
              oViewProperties.context = "taskFormView";
              if(oTaskFormTypeData.name == "cutype"){
                oModelToPush.items = aUsers;
              }
              break;
          }
          // oModelToPush.type = sType;
          // oModelToPush.value = oTaskFormValueData.value;
          oModelToPush.viewProperties = oViewProperties;
          // aTaskFormModels.push(oModelToPush);
        };
        oCallBackData.functionToExecute = functionToExecute;
        if(oTaskFormTypeData.name == "cutype"){
          aPromises.push(_getUserByRoleId(oTaskFormData.properties.RoleId, oCallBackData));
        }else{
          if (oTaskFormTypeData.name == "enum") {
            oModelToPush.items = _createIdLabelArray(oTaskFormTypeData.values);
          }
          oModelToPush.viewProperties = oViewProperties;
        }
        oModelToPush.type = sType;
        oModelToPush.value = oTaskFormValueData.value;
        aTaskFormModels.push(oModelToPush);
      });
      Promise.all(aPromises).then(TaskProps.setActiveTaskFormViewModels(aTaskFormModels));
    }
  };

  let _getUserByRoleId = function (sSelectedRoleId, oCallBackData) {
    if(CS.isEmpty(sSelectedRoleId)){
      return CS.getRequest(CommonModuleRequestMapping.getAll,{}, successGetAllUsers.bind(this,oCallBackData), failureGetAllUsers.bind(this,sSelectedRoleId));
    }else{
      return CS.getRequest(CommonModuleRequestMapping.getUsersByRole, {id: sSelectedRoleId}, successGetRoleDetailsByIdCallback.bind(this, sSelectedRoleId, oCallBackData), failureGetRoleDetailsByIdCallback.bind(this,sSelectedRoleId));
    }
  };

  let successGetAllUsers = function (oCallBackData, oResponse) {
    let aUsersList = oResponse.success;
    let aUsers = [];
    CS.forEach(aUsersList,(oUser)=>{
      let oTempObj={}
      oTempObj.id = oUser.id;
      oTempObj.label = oUser.firstName + " " + oUser.lastName;
      oTempObj.code = oUser.code;
      aUsers.push(oTempObj);
    });
    if(oCallBackData){
      oCallBackData.functionToExecute(aUsers);
    }
    _triggerChange();
  };

  let failureGetAllUsers = function (oResponse) {
    CommonUtils.failureCallback(oResponse, "failureGetAllUsers", getTranslation())
  };

  let successGetRoleDetailsByIdCallback = function (sSelectedRoleId, oCallBackData, oResponse) {
    let oRoleFromServer = oResponse.success;
    let aUserList = oRoleFromServer.usersList;
    let aUsers = [];
    CS.forEach(aUserList,(oUser)=>{
      let oTempObj={}
      oTempObj.id = oUser.id;
      oTempObj.name = oUser.userName;
      aUsers.push(oTempObj);
    });
    if(oCallBackData){
      oCallBackData.functionToExecute(aUsers);
    }
    _triggerChange();
  };

  let failureGetRoleDetailsByIdCallback = function (oResponse) {
    CommonUtils.failureCallback(oResponse, "failureGetRoleDetailsByIdCallback", getTranslation())
  };

  let _resetToggleCollapsedState = function () {
    var oTaskViewProps = TaskProps.getTaskViewProps();
    var oSectionVisualState = oTaskViewProps.sectionVisualState;

    CS.forEach(oSectionVisualState , function (obj) {
      obj.isCollapsed = true;
    });
  };

  var successGetTaskDetails = function (oCallBack, oResponse) {
    var oSuccess = oResponse.success;

    let oReferencedInstances = oSuccess.referencedInstances;
    let oConfigDetails = oSuccess.configDetails;
    if (!CS.isEmpty(oReferencedInstances)) {
      let oLinkedEntity = oSuccess.linkedEntities[0];
      let oInstance = oReferencedInstances[oLinkedEntity.contentId];
      oSuccess.linkedInstanceData = {
        contentId: oInstance.id,
        baseType: oLinkedEntity.type,
        label: oInstance.label,
        types: oInstance.types,
        assetInstance: oInstance.assetInstance,
        referencedKlasses: oConfigDetails.referencedKlasses
      };
    }

    TaskProps.setActiveTask(oSuccess);
    TaskProps.setActiveSubTask({});
    let oReferencedData = TaskProps.getReferencedData();
    var oReferencedTasks = oReferencedData.referencedTasks;
    var oActiveTaskType = oReferencedTasks[oSuccess.types[0]];
    _setTagDetailsForTask(oActiveTaskType);
    _setRoleDetails(oSuccess);
    TaskProps.setIsTaskDetailViewOpen(true);
    _setAttachmentsData();
    _setTaskFormViewModels(oSuccess);
    _resetToggleCollapsedState();

    let oTaskData = TaskProps.getTaskData();
    oTaskData.referencedData = CS.merge(oConfigDetails.referencedRoles, oConfigDetails.referencedUsers);


    CS.forEach(oSuccess.subTasks, function (oSubTask) {
      _updateTagForTask(oSubTask);
    });
    if (oCallBack && oCallBack.functionToExecute) {
      oCallBack.functionToExecute();
    }

    EventBus.dispatch(Events.TASK_DATA_CHANGED, {activeTask: oSuccess, checkedTaskList: []});
  };

  var failureGetTaskDetails = function (sId, oResponse) {
    var aExceptionDetails = oResponse.failure ? oResponse.failure.exceptionDetails : [];
    if (!CS.isEmpty(aExceptionDetails)) {
      if(aExceptionDetails[0].key == 'UserNotHaveReadPermission') {
        alertify.error(getTranslation().UserNotHaveReadPermissionForTask);
        return;
      } else if (aExceptionDetails[0].key == 'TaskInstanceNotFoundException') {
        var aTaskList = TaskProps.getTaskList();
        CS.remove(aTaskList, {id: sId});
        _makeContentsAndPropertiesListHavingTasks();
        _updateTaskMap();
        TaskProps.setActiveTask({});
        alertify.error(getTranslation().TaskInstanceNotFoundException);
        EventBus.dispatch(Events.TASK_DATA_CHANGED, {activeTask: {}});
        return;
      }
    }
    CommonUtils.failureCallback(oResponse, 'failureGetTaskDetails', getTranslation());
  };

  var _getTaskDetails = function (sId, oCallBack) {
    CS.getRequest(TaskRequestMapping.GetTask, {id: sId}, successGetTaskDetails.bind(this, oCallBack), failureGetTaskDetails.bind(this, sId));
  };

  var _handleTaskListClicked = function (sId, oCallBack) {
    var oActiveTask = TaskProps.getActiveTask();
    var oActiveProperty = TaskProps.getActiveProperty();
    var isTaskDialogOpen = !CS.isEmpty(oActiveProperty);
    if(oActiveTask && oActiveTask.pureObject){
      if(isTaskDialogOpen){
        _saveOrDiscardTask(_getTaskDetails.bind(this, sId));
      } else {
        alertify.message(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
        _triggerChange();
      }
    }
    else if(_activeTaskCommentSafetyCheck()){
      _getTaskDetails(sId, oCallBack);
    }
  };

  var _handleSubTaskClicked = function (sId) {
    let oActiveTask = TaskProps.getActiveTask();
    let isOldSubTask = true;
    if(CS.isNotEmpty(oActiveTask.pureObject)){
      isOldSubTask = CS.findIndex(oActiveTask.pureObject.subTasks, {id: sId}) != -1;
    }

    if (isOldSubTask) {
      let aSubTasks = oActiveTask.subTasks || [];
      let oSubTask = CS.find(aSubTasks, {id: sId});
      TaskProps.setActiveSubTask(oSubTask);
      TaskProps.setResetScrollStatus(true);

      //set workflow details for subtask
      _setRoleDetails(oSubTask);
      _setSubTasksAttachmentsData();
      _triggerChange();
    }
  };

  var _handleResetScrollState = function () {
    TaskProps.setResetScrollStatus(false);
  };

  var _handleSubtaskBackButtonClicked = function () {
    //set workflow details for task & attachments data
    var oActiveTask = TaskProps.getActiveTask();
    _setRoleDetails(oActiveTask);
    _setAttachmentsData();
    TaskProps.setActiveSubTask({});
    _triggerChange();
  };

  var _handleTaskListCheckClicked = function (bIsChecked, sTaskId) {
    if(bIsChecked){
      var aCheckedTaskList = TaskProps.getCheckedTaskList();
      if(aCheckedTaskList) {
        aCheckedTaskList.push(sTaskId);
      }
      // TaskProps.addToCheckedTaskList(sTaskId);
    }else{
      var aCheckedTaskList = TaskProps.getCheckedTaskList();
      CS.remove(aCheckedTaskList, function (sCheckedTaskId) {
        return sCheckedTaskId == sTaskId;
      });
    }

    _triggerChange();
  };

  let escapeRegexCharacters = function (sString) {
    return sString.replace(new RegExp('[.\\\\+*?\\[\\^\\]$(){}=!<>|:\\-]', 'g'), '\\$&');
  };

  var _handleAllTaskListCheckClicked = function (sSearchString) {
    var aTaskListGroupByType = TaskProps.getTaskListGroupByType();
    var aAllGroupsTasks = [];
    var aCheckedTasks = [];
    sSearchString = escapeRegexCharacters(sSearchString);
    var rPattern = sSearchString ? new RegExp(sSearchString, "i") : null;
    let aFilteredTaskList = [];

    CS.forEach(aTaskListGroupByType, function (oTaskGroup) {
      aAllGroupsTasks = aAllGroupsTasks.concat(oTaskGroup.tasks);
    });

    if (TaskProps.getIsAllTaskChecked()) {
      TaskProps.setCheckedTaskList(aCheckedTasks);
      TaskProps.setFilteredTaskList(aCheckedTasks);
    } else {
      CS.forEach(aAllGroupsTasks, function (oTask) {
        if (rPattern) {
          if (rPattern.test(oTask.name)) {
            aCheckedTasks.push(oTask.id);
            aFilteredTaskList.push(oTask.id);
          }
        } else {
          aCheckedTasks.push(oTask.id);
          aFilteredTaskList.push(oTask.id);
        }
      });
      TaskProps.setCheckedTaskList(aCheckedTasks);
      TaskProps.setFilteredTaskList(aFilteredTaskList);
    }

    EventBus.dispatch(Events.TASK_DATA_CHANGED, {checkedTaskList: aCheckedTasks});
  };

  var _handleSubTaskEditButtonClicked = function (sSubTaskId){
    TaskProps.setEditableSubTaskId(sSubTaskId);
    _triggerChange();
  };

  var _handleImageSimpleAnnotationViewCloseTaskDetail = function (sTaskId) {
    TaskProps.setActiveTask({});
    TaskProps.setActiveSubTask({});
    TaskProps.setTaskListGroupByType({});
    TaskProps.setIsTaskDetailViewOpen(false);
    TaskProps.setEditableTaskId("");
    TaskProps.setTemporaryCommentText("");
    TaskProps.setTemporaryCommentAttachmentData([]);
    TaskProps.setIsTaskAnnotationDetailViewOpen(false);

    EventBus.dispatch(Events.TASK_DATA_CHANGED, {isDialogOpen: false, activeTask: {}});
  };

  var _handleImageSimpleAnnotationViewOpenTaskDetail = function (sTaskId) {
    var aTaskList = TaskProps.getTaskList();
    var oActiveTask = CS.find(aTaskList, {id: sTaskId});
    var oProperty = TaskProps.getActiveProperty();
    var aFilteredList = _getFilteredListByLevel(oProperty.instanceId, "property");
    var oGroupByTypeTaskListMap = _getGroupByTypeTaskList(aFilteredList, "");
    TaskProps.setTaskListGroupByType(oGroupByTypeTaskListMap);
    TaskProps.setIsTaskDetailViewOpen(true);
    TaskProps.setEditableTaskId("");
    TaskProps.setFilteredList(aFilteredList);
    TaskProps.setIsTaskAnnotationDetailViewOpen(true);


    CS.getRequest(TaskRequestMapping.GetTask, {id: sTaskId}, successGetTaskDetails.bind(this, {}), failureGetTaskDetails.bind(this, {}));
  };

  var _handleImageSimpleAnnotationViewCreateAnnotationClicked = function (iXPosition, iYPosition, oContent){
    var oTask = _createDefaultTaskMasterObject("", iXPosition, iYPosition, oContent);
    oTask.isCreated = true;

    TaskProps.setIsTaskAnnotationDetailViewOpen(true);
    TaskProps.setActiveTask(oTask);
    TaskProps.setIsTaskDetailViewOpen(true);
    let oReferencedData = TaskProps.getReferencedData();
    var oReferencedTasks = oReferencedData.referencedTasks;
    var oActiveTaskType = oReferencedTasks[oTask.types[0]] || null;
    _setRoleDetails(oTask);
    _setTagDetailsForTask(oActiveTaskType);
    TaskProps.setCommentAttachmentDataMap({});
    TaskProps.setAttachmentData([]);
    TaskProps.setTemporaryCommentText("");
    TaskProps.setTemporaryCommentAttachmentData([]);
    EventBus.dispatch(Events.TASK_DATA_CHANGED, {activeTask: oTask});
  };

  var _handleCoverflowDetailViewShowAnnotationButtonClicked = function () {
    var bShowAnnotations = TaskProps.getShowAnnotations();
    TaskProps.setShowAnnotations(!bShowAnnotations);
    if(!bShowAnnotations){
      _fetchAllTaskForProperty();
    }else{
      TaskProps.reset();
      _triggerChange();
    }
  };

  var _handleSubTaskDeleteButtonClicked = function (sSubTaskId) {
    var oActiveTask = _makeActiveTaskDirty();
    var aCheckedSubTasks = TaskProps.getCheckedSubTaskList();
    var sEditableSubTaskId = TaskProps.getEditableSubTaskId();
    var aSelectedSubTasks = !CS.isEmpty(sSubTaskId) ? [sSubTaskId] : aCheckedSubTasks;
    var aDeleteLabels = [];

    CS.forEach(aSelectedSubTasks, function (sSelectedTaskId){
      var oSubTask = CS.find(oActiveTask.subTasks, {id: sSelectedTaskId});
      aDeleteLabels.push(oSubTask.name);
    });

    CommonUtils.listModeConfirmation(getTranslation().DELETE_CONFIRMATION, aDeleteLabels,
        function () {
          _deleteSubTasks(oActiveTask.subTasks, aSelectedSubTasks, sEditableSubTaskId, aCheckedSubTasks, sSubTaskId);
        }, function (oEvent) {
        });

  };

  var _deleteSubTasks = function (aSubTasks, aSelectedSubTasks, sEditableSubTaskId, aCheckedSubTasks, sSubTaskId) {
    CS.forEach(aSelectedSubTasks, function (sSelectedTaskId){
      if(sEditableSubTaskId == sSelectedTaskId){
        TaskProps.setEditableSubTaskId("");
      }
      CS.remove(aSubTasks, function (oSubTask) {
        return oSubTask.id == sSelectedTaskId;
      });
    });

    if(CS.isEmpty(sSubTaskId)){
      TaskProps.setCheckedSubTaskList([]);
    }else{
      CS.remove(aCheckedSubTasks, function (sCheckedSubTaskId) {
        return sCheckedSubTaskId == sSubTaskId;
      });
    }

    EventBus.dispatch(Events.TASK_DATA_CHANGED, {isTaskDirty: true});
  };

  var _handleNewSubTaskLabelValueChanged = function (sLabel) {
    _setTasksScreenLockStatus(true);
    var oActiveTask = _makeActiveTaskDirty();
    var sEditableSubTaskId = TaskProps.getEditableSubTaskId();
    var oSubTask = CS.find(oActiveTask.subTasks, {"id": sEditableSubTaskId});
    oSubTask.name = sLabel;
    EventBus.dispatch(Events.TASK_DATA_CHANGED, {isTaskDirty: true});
  };

  var _handleTaskLabelValueChanged = function (sId, sValue) {
    _setTasksScreenLockStatus(true);
    var oActiveTask = _makeActiveTaskDirty();
    var oActiveSubTask = TaskProps.getActiveSubTask();

    if(!CS.isEmpty(oActiveSubTask)){
      oActiveSubTask.name = sValue;
    } else {
      if(oActiveTask.id == sId) {
        if(oActiveTask.isCreated){
          oActiveTask.name = sValue;
        } else {
          var aTaskList = TaskProps.getTaskList();
          var oTask = CS.find(aTaskList, {id: sId});
          oActiveTask.name = sValue;
          oTask.name = sValue;
        }
      }
    }
    EventBus.dispatch(Events.TASK_DATA_CHANGED, {isTaskDirty: true});
  };

  var _handleTaskVisibilityModeChanged=function (sId,sIsPublic) {
    var oActiveTask = _makeActiveTaskDirty();
    var oActiveSubTask = TaskProps.getActiveSubTask();
    if(!CS.isEmpty(oActiveSubTask)){
      oActiveSubTask.isPublic = !sIsPublic;
    }
    if(oActiveTask.id == sId) {
      if(oActiveTask.isCreated){
        oActiveTask.isPublic = !sIsPublic;
      } else {
        var aTaskList = TaskProps.getTaskList();
        var oTask = CS.find(aTaskList, {id: sId});
        oActiveTask.isPublic = !sIsPublic;
        oTask.isPublic = !sIsPublic;
      }
    }
    EventBus.dispatch(Events.TASK_DATA_CHANGED, {isTaskDirty: true});
  };

  var _handleTaskNameOnBlur = function (sId) {
    _setTasksScreenLockStatus(true);
    TaskProps.setEditableTaskId("");
    _triggerChange();
  };

  var _handleSaveSubTaskValue = function() {
    _setTasksScreenLockStatus(true);
    var oActiveTask = _makeActiveTaskDirty();

    var sEditableSubTaskId = TaskProps.getEditableSubTaskId();
    var oSubTask = CS.find(oActiveTask.subTasks, {"id": sEditableSubTaskId});
    if(CS.isEmpty(oSubTask.name)){
      oSubTask.name = UniqueIdentifierGenerator.generateUntitledName();
    }
    TaskProps.setEditableSubTaskId("");
    EventBus.dispatch(Events.TASK_DATA_CHANGED, {isTaskDirty: true});
  };

  var _setRolesForSubTaskFromTask = function (oSubTask) {
    var oActiveTask = TaskProps.getActiveTask();
    var oTaskConsultedRole = CS.find(oActiveTask.roles, {roleId: RoleIdDictionary.ConsultedRoleId});
    var oConsultedRole = CS.find(oSubTask.roles, {roleId: RoleIdDictionary.ConsultedRoleId});
    CS.assign(oConsultedRole, oTaskConsultedRole);

    var oTaskInformedRole = CS.find(oActiveTask.roles, {roleId: RoleIdDictionary.InformedRoleId});
    var oInformedRole = CS.find(oSubTask.roles, {roleId: RoleIdDictionary.InformedRoleId});
    CS.assign(oInformedRole, oTaskInformedRole);

    var oTaskVerifyRole = CS.find(oActiveTask.roles, {roleId: RoleIdDictionary.VerifyRoleId});
    var oVerifyRole = CS.find(oSubTask.roles, {roleId: RoleIdDictionary.VerifyRoleId});
    CS.assign(oVerifyRole, oTaskVerifyRole);
  };

  var _handleAddNewSubTaskClicked = function (oActiveContent) {
    if(!_activeTaskCommentSafetyCheck()) {
      return;
    }
    let oReferencedData = TaskProps.getReferencedData();
    var oReferencedTag = oReferencedData.referencedTags;
    var oReferencedTasks = oReferencedData.referencedTasks;
    var oActiveTask = _makeActiveTaskDirty();
    let oLinkedInstanceData = oActiveTask.linkedInstanceData;
    let sBaseType = oLinkedInstanceData ?  oLinkedInstanceData.baseType : oActiveContent.baseType;
    let oTaskTypeDictionary = new TaskTypeDictionary();
    var oDate = _getDatesForTask(oActiveTask);

    var oSubTask = new Task(oActiveTask.types, oDate.createdOn , oDate.startDate , oDate.dueDate , oDate.overDueDate,
        "", "", "", -1, -1, BaseTypesDictionary.taskInstanceBaseType, sBaseType);
    let oCurrentUser = CommonUtils.getCurrentUser();
    let oDummyRole = {"roleIds": [], "userIds": [oCurrentUser.id]};
    oSubTask.accountable = oSubTask.signoff = oDummyRole;
    oSubTask.dueDate = oActiveTask.dueDate;
    oSubTask.overDueDate = oActiveTask.overDueDate;

    //add default status tag to task
    var oMasterTask = oReferencedTasks[oSubTask.types[0]];
    if(oMasterTask && oMasterTask.type === oTaskTypeDictionary.SHARED) {
      var oStatusMaster = oReferencedTag[TagIdDictionary.TaskStatusId];
      oSubTask.status = _addDefaultTagToTask(TagIdDictionary.TaskPlannedId, oStatusMaster);
    }

    if(oMasterTask && oMasterTask.type === oTaskTypeDictionary.PERSONAL) {
      oStatusMaster = oReferencedTag[oMasterTask.statusTag];
      oStatusMaster && (oSubTask.status = _addDefaultTagToTask("", oStatusMaster));
    }

    var oMasterPriority = oReferencedTag[oMasterTask.priorityTag];
    oMasterPriority && (oSubTask.priority = _addDefaultTagToTask("", oMasterPriority));

    oActiveTask.subTasks.push(oSubTask);
    TaskProps.setEditableSubTaskId(oSubTask.id);
    _updateTagForTask(oSubTask);
    EventBus.dispatch(Events.TASK_DATA_CHANGED, {isTaskDirty: true});
  };

  var _handleSubTaskListCheckBoxClicked = function (sSubTaskId, bSubTaskChecked) {
    if(bSubTaskChecked){
      TaskProps.addToCheckedSubTaskList(sSubTaskId);
    }else{
      var aCheckedSubTaskList = TaskProps.getCheckedSubTaskList();
      CS.remove(aCheckedSubTaskList, function (sCheckedSubTaskId) {
        return sCheckedSubTaskId == sSubTaskId;
      });
    }
    _triggerChange();
  };

  var _clearActiveTaskAndCloseDetailedView = function () {
    var oActiveTask = TaskProps.getActiveTask();
    var aTaskList = TaskProps.getTaskList();
    var oTask = CS.find(aTaskList, {id: oActiveTask.id});
    if(oActiveTask && oActiveTask.isDirty) {
      var oOriginalObject = oActiveTask.pureObject;
      CS.forOwn(oTask, function (value, sKey) {
        oTask[sKey] = oOriginalObject[sKey]
      });
      _updateTagForTask(oTask);
      CS.forEach(oTask.subTasks, function (oSubTask) {
        _updateTagForTask(oSubTask);
      });
    }

    TaskProps.setTemporaryCommentText("");
    TaskProps.setTemporaryCommentAttachmentData([]);
    TaskProps.setIsTaskDetailViewOpen(false);
    TaskProps.setActiveTask({});
    TaskProps.setActiveSubTask({});
    EventBus.dispatch(Events.TASK_DATA_CHANGED, {activeTask: {}});
  };

  var _saveOrDiscardTask = function (fFunctionToExecute, oExtraData) {
    var oActiveTask = TaskProps.getActiveTask();
    if(oActiveTask && oActiveTask.pureObject){
      CustomActionDialogStore.showConfirmDialog(getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE, "",
          _saveTask.bind(this, {functionToExecute: fFunctionToExecute, extraData: oExtraData}),
          _discardTask.bind(this, {functionToExecute: fFunctionToExecute, extraData: oExtraData}));
    } else {
      fFunctionToExecute();
    }
  };

  var _handleContentTaskClose = function () {
    var oActiveTask = TaskProps.getActiveTask();
    if(oActiveTask && oActiveTask.isDirty){
      let oActiveProperty = TaskProps.getActiveProperty();
      var isTaskDialogOpen = !CS.isEmpty(oActiveProperty);
      if(isTaskDialogOpen){
        _saveOrDiscardTask(_clearActiveTaskAndCloseDetailedView);
      } else {
        alertify.message(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
        _triggerChange();
      }
    } else if(_activeTaskCommentSafetyCheck()){
      _clearActiveTaskAndCloseDetailedView();
      _triggerChange();
    }
  };

  var handleTaskValueChanged = function (aNewValue, sContext) {
    _setTasksScreenLockStatus(true);
    var oActiveTask = _makeActiveTaskDirty();
    var oActiveSubTask = TaskProps.getActiveSubTask();
    let oTaskTypeDictionary = new TaskTypeDictionary();
    switch (sContext) {
      case "status":
        var sSelectedStatusTagId =  CS.toString(aNewValue);
        if(!CS.isEmpty(oActiveSubTask)){
          oActiveSubTask.status = _updateTagValuesInTag(sSelectedStatusTagId, oActiveSubTask.status);
        } else {
          oActiveTask.status = _updateTagValuesInTag(sSelectedStatusTagId, oActiveTask.status);
        }
        EventBus.dispatch(Events.TASK_DATA_CHANGED, {isTaskDirty: true});
        break;

      case "priority":
        var sSelectedPriorityTagId =  CS.toString(aNewValue);
        if(!CS.isEmpty(oActiveSubTask)){
          oActiveSubTask.priority = _updateTagValuesInTag(sSelectedPriorityTagId, oActiveSubTask.priority);
        } else {
          oActiveTask.priority = _updateTagValuesInTag(sSelectedPriorityTagId, oActiveTask.priority);
        }
        EventBus.dispatch(Events.TASK_DATA_CHANGED, {isTaskDirty: true});
        break;

      case "taskType":
        var sSelectedType = CS.toString(aNewValue);
        let oReferencedData = TaskProps.getReferencedData();
        var oReferencedTasks = oReferencedData.referencedTasks;
        var oReferencedTag = oReferencedData.referencedTags;

        oActiveTask.types = [sSelectedType];
        var oMasterTask = oReferencedTasks[sSelectedType];
        _setTagDetailsForTask(oMasterTask);

        //add default status tag to task
        var oStatusMaster = null;
        if(oMasterTask && oMasterTask.type === oTaskTypeDictionary.SHARED) {
          oStatusMaster = oReferencedTag[TagIdDictionary.TaskStatusId];
          oActiveTask.status = _addDefaultTagToTask(TagIdDictionary.TaskPlannedId, oStatusMaster);
        }

        if(oMasterTask && oMasterTask.type === oTaskTypeDictionary.PERSONAL) {
          oStatusMaster = oReferencedTag[oMasterTask.statusTag];
          oStatusMaster && (oActiveTask.status = _addDefaultTagToTask("", oStatusMaster));
        }

        var oMasterPriority = oMasterTask && oReferencedTag[oMasterTask.priorityTag];
        oMasterPriority && (oActiveTask.priority = _addDefaultTagToTask("", oMasterPriority));
        _saveTask();
        break;
    }
  };

  var _handleTasksRoleMSSValueChanged = function (sContext, sAction, aSelectedRoles) {
    var oActiveTask = _makeActiveTaskDirty();
    var oActiveSubTask = TaskProps.getActiveSubTask();
    var oRole = (!CS.isEmpty(oActiveSubTask)) ? oActiveSubTask[sContext] : oActiveTask[sContext];
    //var oRole = {};

      if(sAction === "add"){
        _updateCandidatesOfRole(aSelectedRoles, oRole , sAction);
      }else{
        _updateCandidatesOfRole([aSelectedRoles], oRole ,sAction);
      }

    /*switch (sContext) {
      case "responsible":
        oRole = CS.find(aRoles, {roleId: RoleIdDictionary.ResponsibleRoleId});
        _updateCandidatesOfRole(aSelectedRoles, oRole);
        break;

      case "accountable":
        oRole = CS.find(aRoles, {roleId: RoleIdDictionary.AccountableRoleId});
        _updateCandidatesOfRole(aSelectedRoles, oRole);
        break;

      case "consulted":
        oRole = CS.find(aRoles, {roleId: RoleIdDictionary.ConsultedRoleId});
        _updateCandidatesOfRole(aSelectedRoles, oRole);
        break;

      case "informed":
        oRole = CS.find(aRoles, {roleId: RoleIdDictionary.InformedRoleId});
        _updateCandidatesOfRole(aSelectedRoles, oRole);
        break;

      case "signoff":
        oRole = CS.find(aRoles, {roleId: RoleIdDictionary.SignOffRoleId});
        _updateCandidatesOfRole(aSelectedRoles, oRole);
        break;

      case "verify":
        oRole = CS.find(aRoles, {roleId: RoleIdDictionary.VerifyRoleId});
        _updateCandidatesOfRole(aSelectedRoles, oRole);
        break;
    }*/
    EventBus.dispatch(Events.TASK_DATA_CHANGED, {isTaskDirty: true});
  };

  let _updateCandidatesOfRole = function (aNewCandidates, oRole, sAction) {
    let aUsersList = [];
    let aRolesList = [];
    if (!CS.isEmpty(aNewCandidates)) {
      let oTaskData = TaskProps.getTaskData();
      CS.forEach(aNewCandidates, function (oElement) {
        let aUserIds = CS.find(oRole.userIds, oElement.id);
        let aRoleIds = CS.find(oRole.roleIds, oElement.id);
        if (CS.find(oTaskData.usersList, {id: oElement.id}) && !aUserIds) {
          if(sAction === "add") {
            aUsersList.push(oElement.id);
          }else{
            let iIndex = CS.indexOf(oRole.userIds, oElement.id);
            oRole.userIds.splice(iIndex,1);
            aUsersList  = oRole.userIds;
            if(!CS.isEmpty(oRole.roleIds)){
              aRolesList = oRole.roleIds;
            }
          }
        } else if (oTaskData.rolesList, {id: oElement.id} && !aRoleIds) {
          if(sAction === "add"){
            aRolesList.push(oElement.id);
          }else{
            let iIndex = CS.indexOf(oRole.roleIds, oElement.id);
            oRole.roleIds.splice(iIndex,1);
            aRolesList = oRole.roleIds;
            if(!CS.isEmpty(oRole.userIds)){
              aUsersList = oRole.userIds;
            }
          }
        }
      });
    }
    oRole.userIds = aUsersList;
    oRole.roleIds = aRolesList;
  };

  var _handleTaskDescriptionChanged = function (sValue) {
    _setTasksScreenLockStatus(true);
    var oActiveTask = _makeActiveTaskDirty();
    var oActiveSubTask = TaskProps.getActiveSubTask();
    if(!CS.isEmpty(oActiveSubTask)){
      oActiveSubTask.longDescription = sValue;
    } else {
      oActiveTask.longDescription = sValue;
    }
    EventBus.dispatch(Events.TASK_DATA_CHANGED, {isTaskDirty: true});
  };

  var _getDatesForTask = function (oActiveTask) {
    var oCurrentDate = CommonUtils.getMomentOfDate();
    var iCurrentDate = oCurrentDate.valueOf();
    var iStartDate = iCurrentDate;
    var iDueDate = oCurrentDate.endOf("day").valueOf();
    var iOverDueDate = "";
    if (!oActiveTask) {
      var oTomorrowDate = CommonUtils.getMomentOfDate();
      oTomorrowDate.add(1, "day");
      iOverDueDate = oTomorrowDate.endOf("day").valueOf();
    }
    else {
      iStartDate = oCurrentDate.valueOf() <= oActiveTask.startDate ? oActiveTask.startDate : oCurrentDate.valueOf();
      iDueDate = oCurrentDate.endOf("day").valueOf() <= oActiveTask.startDate ? oActiveTask.startDate : oCurrentDate.endOf("day").valueOf();
      iOverDueDate = oActiveTask.overDueDate;
    }
    return {
      createdOn: iCurrentDate,
      startDate: iStartDate,
      dueDate: iDueDate,
      overDueDate: iOverDueDate
    };

  };

  var _createDefaultTaskMasterObject = function (sTypeId, iXPosition, iYPosition, oActiveContent) {
    let oReferencedData = TaskProps.getReferencedData();
    var oReferencedTag = oReferencedData.referencedTags;
    var oReferencedTasks = oReferencedData.referencedTasks;
    var oActiveTaskLevel = TaskProps.getActiveTaskLevel();
    var sContext = (oActiveTaskLevel && oActiveTaskLevel.listItemKey) ?  oActiveTaskLevel.listItemKey : "";
    var sVariantId = (oActiveContent.variantInstanceId) ? (oActiveContent.variantInstanceId) : "";
    var sActiveElementId = null;
    var oCreatedTask = {};
    let oTaskTypeDictionary = new TaskTypeDictionary();
    var oDate = _getDatesForTask();
    let bMakeContentDirty = false;

    //create dummy Task for Property from dialog
    if(!CS.isEmpty(TaskProps.getActiveProperty())){
      var oProperty = TaskProps.getActiveProperty();
      sActiveElementId = oProperty.id;
      var sSplitter = CommonUtils.getSplitter();
      var sNewId = CommonUtils.getNewSuffix();
      if(sActiveElementId && sActiveElementId.indexOf(sNewId) >= 0){
        sActiveElementId = sActiveElementId.split(sSplitter)[0];
        // ContentUtils.makeActiveContentDirty();
        bMakeContentDirty = true;
      }
      var iXPos = iXPosition || -1;
      var iYPos = iYPosition || -1;
      var aTypes = CS.isEmpty(sTypeId) ? [] : [sTypeId];
      oCreatedTask = new Task(aTypes, oDate.createdOn , oDate.startDate , oDate.dueDate , oDate.overDueDate ,
          sActiveElementId, sVariantId, oActiveContent.id, iXPos, iYPos,
          BaseTypesDictionary.taskInstanceBaseType, oActiveContent.baseType);
    }

    //create dummy task all content, variant & property from tasks tab
    if(sContext == "content"){
      oCreatedTask = new Task([sTypeId], oDate.createdOn , oDate.startDate , oDate.dueDate , oDate.overDueDate ,
          "", "", oActiveContent.id, -1, -1, BaseTypesDictionary.taskInstanceBaseType, oActiveContent.baseType);
    } else if(sContext == "variant") {
      oCreatedTask = new Task([sTypeId], oDate.createdOn , oDate.startDate , oDate.dueDate , oDate.overDueDate ,
          "", oActiveTaskLevel.id, oActiveContent.id, -1, -1, BaseTypesDictionary.taskInstanceBaseType, oActiveContent.baseType);
    } else if (sContext == "property") {
      var sElementId = oActiveTaskLevel.id;
      oCreatedTask = new Task([sTypeId], oDate.createdOn , oDate.startDate , oDate.dueDate , oDate.overDueDate ,
          sElementId, "", oActiveContent.id, -1, -1, BaseTypesDictionary.taskInstanceBaseType, oActiveContent.baseType);
    }

    //add default status tag to task
    var oMasterTask = oReferencedTasks[oCreatedTask.types[0]];
    var oStatusMaster = null;
    if(oMasterTask && oMasterTask.type === oTaskTypeDictionary.SHARED) {
      oStatusMaster = oReferencedTag[TagIdDictionary.TaskStatusId];
      oCreatedTask.status = _addDefaultTagToTask(TagIdDictionary.TaskPlannedId, oStatusMaster);
    }

    if(oMasterTask && oMasterTask.type === oTaskTypeDictionary.PERSONAL) {
      oStatusMaster = oReferencedTag[oMasterTask.statusTag];
      oStatusMaster && (oCreatedTask.status = _addDefaultTagToTask("", oStatusMaster));
    }

    var oMasterPriority = oMasterTask && oReferencedTag[oMasterTask.priorityTag];
    oMasterPriority && (oCreatedTask.priority = _addDefaultTagToTask("", oMasterPriority));

    if(bMakeContentDirty) {
      EventBus.dispatch(Events.TASK_DATA_CHANGED, {makeActiveContentDirty: true});
    }

    return oCreatedTask;
  };

  var _updateTaskOverDueDate = function (sValue) {
    var oActiveTask = _makeActiveTaskDirty();
    var oActiveSubTask = TaskProps.getActiveSubTask();
    if(!CS.isEmpty(oActiveSubTask)){
      oActiveSubTask.overDueDate = sValue;
      oActiveSubTask.eventSchedule.endTime = sValue;
    } else {
      if(oActiveTask.isCreated){
        oActiveTask.overDueDate = sValue;
      } else {
        var aTaskList = TaskProps.getTaskList();
        var oTask = CS.find(aTaskList, {id: oActiveTask.id});
        if(!oActiveTask.dueDate){
          oActiveTask.dueDate = sValue;
        }
        oActiveTask.overDueDate = sValue;
        oActiveTask.eventSchedule.endTime = sValue;
        oTask.overDueDate = sValue;
      }
    }
    EventBus.dispatch(Events.TASK_DATA_CHANGED, {isTaskDirty: true});
  };

  var _updateTaskDateByKey = function (sValue, sKey) {
    var oActiveTask = _makeActiveTaskDirty();
    var oActiveSubTask = TaskProps.getActiveSubTask();
    if(!CS.isEmpty(oActiveSubTask)){
      oActiveSubTask[sKey] = sValue;
      sKey === "startDate" && (oActiveSubTask.eventSchedule.startTime = sValue);
    } else {
      if(oActiveTask.isCreated){
        oActiveTask[sKey] = sValue;
      } else {
        var aTaskList = TaskProps.getTaskList();
        var oTask = CS.find(aTaskList, {id: oActiveTask.id});
        oActiveTask[sKey] = sValue;
        oTask[sKey] = sValue;
        if(sKey === "dueDate" ){
          if(!oActiveTask.overDueDate){
            oActiveTask.overDueDate = sValue;
            oActiveTask.eventSchedule.endTime = sValue;
          }
        }
        sKey === "startDate" && (oActiveTask.eventSchedule.startTime = sValue);
      }
    }
    EventBus.dispatch(Events.TASK_DATA_CHANGED, {isTaskDirty: true});
  };

  var _updateSubTaskOnParentTaskStartDateChanged = function () {
    var oParentTask = _makeActiveTaskDirty();
    var aSubTasks = oParentTask.subTasks;
    CS.forEach(aSubTasks, function (oSubTask) {

      /** If Parent task start date is greater than sub-task start date then sub-task start date set to parent task
       *  start date. **/
      if (oSubTask.startDate < oParentTask.startDate) {
        oSubTask.startDate = oParentTask.startDate;
      }

      /** If Parent task due date is less than sub-task start date then sub-task start date is set to parent task
       *  start date.*/
      if (oParentTask.dueDate < oSubTask.startDate) {
        oSubTask.startDate = oParentTask.startDate;
      }

      /** If Parent task due date is less than sub-task due date then sub-task due set to parent task due date.*/
      if (oParentTask.dueDate < oSubTask.dueDate) {
        oSubTask.dueDate = oParentTask.dueDate;
      }

      /** If parent task over due date is less than sub-task over due date then sub-task over due date set to parent
       *  task over due date. */
      if (oParentTask.overDueDate < oSubTask.overDueDate) {
        oSubTask.overDueDate = oParentTask.overDueDate;
      }

      /** If Sub-task due date is less than sub-task start date then sub task due date set to parent task due date.*/
      if (oSubTask.dueDate < oSubTask.startDate) {
        oSubTask.dueDate = oParentTask.dueDate;
      }

      /** If sub-task over due date is less than sub-task start date then sub-task over due date is set to parent
       *  task over due date.*/
      if (oSubTask.overDueDate < oSubTask.startDate) {
        oSubTask.overDueDate = oParentTask.overDueDate;
      }

    });
    EventBus.dispatch(Events.TASK_DATA_CHANGED, {isTaskDirty: true});
  };

  var _handleTaskDueDateChanged = function (sValue, sKey) {
    _setTasksScreenLockStatus(true);
    if(sKey === "startDate") {
      _updateTaskDateByKey(sValue, sKey);
    } else if(sKey === "dueDate") {
      _updateTaskDateByKey(sValue, sKey);
    } else {
      _updateTaskOverDueDate(sValue);
    }
    _updateSubTaskOnParentTaskStartDateChanged();
    _triggerChange();
  };

  var _getGroupByPriorityTaskList = function (aFilteredList) {
    var oTaskListMap =  {};
    var oGroupByPriorityTaskListMap = {};
    var sSplitter = ViewUtils.getSplitter();
    let oReferencedData = TaskProps.getReferencedData();
    var oReferencedTags = oReferencedData.referencedTags;

    oTaskListMap = CS.groupBy(aFilteredList, function (oTask) {
      var oPriority = oTask.priority;
      var aTagValues = (oPriority) ? oPriority.tagValues : [];
      if(aTagValues.length && aTagValues[0].tagId){
        var aChildren = oReferencedTags[oPriority.tagId] ? oReferencedTags[oPriority.tagId].children : [];
        var oChildren = CS.find(aChildren, {id: aTagValues[0].tagId});
        if(!CS.isEmpty(oChildren)) {
          return oPriority.tagId + sSplitter + aTagValues[0].tagId;
        } else {
          return "others";
        }
      } else {
        return "others";
      }
    });

    CS.forEach(oTaskListMap, function (value, key) {
      if(key == "others"){
        oGroupByPriorityTaskListMap[key] = {
          "bShowAddNewTask": false,
          "label": getTranslation().OTHERS_TASKS,
          "tasks": value,
          "isCollapsed": false
        }
      } else {
        var aTagId = CS.split(key, sSplitter);
        var sMasterPriorityId = aTagId[0];
        var sPriority = aTagId[1];
        var oMasterPriority = oReferencedTags[sMasterPriorityId];
        var oPriority = CS.find(oMasterPriority.children, {"id": sPriority});
        oGroupByPriorityTaskListMap[key] = {
          "bShowAddNewTask": false,
          "label": oPriority.label,
          "tasks": value,
          "isCollapsed": false,
          "code": oPriority.code
        }
      }
    });

    oGroupByPriorityTaskListMap = CS.toPlainObject(CS.sortBy(oGroupByPriorityTaskListMap, function (oGroup) {
      return oGroup.label;
    }));

    return oGroupByPriorityTaskListMap;
  };

  var _getGroupByStatusTaskList = function (aFilteredList) {
    var oTaskListMap =  {};
    var oGroupByStatusTaskListMap = {};
    var sSplitter = ViewUtils.getSplitter();
    let oReferencedData = TaskProps.getReferencedData();
    var oReferencedTags = oReferencedData.referencedTags;

    oTaskListMap = CS.groupBy(aFilteredList, function (oTask) {
      var oStatus = oTask.status;
      var aTagValues = oStatus ? oStatus.tagValues : [];
      if(aTagValues.length && aTagValues[0].tagId){
        var aChildren = oReferencedTags[oStatus.tagId] ? oReferencedTags[oStatus.tagId].children : [];
        var oChildren = CS.find(aChildren, {id: aTagValues[0].tagId});
        if(!CS.isEmpty(oChildren)){
          return oStatus.tagId + sSplitter + aTagValues[0].tagId;
        } else {
          return "others";
        }
      } else {
        return "others";
      }
    });

    CS.forEach(oTaskListMap, function (value, key) {
      if(key == "others"){
        oGroupByStatusTaskListMap[key] = {
          "bShowAddNewTask": false,
          "label": getTranslation().OTHERS_TASKS,
          "tasks": value,
          "isCollapsed": false
        }
      } else {
        var aTagId = CS.split(key, sSplitter);
        var sMasterStatusId = aTagId[0];
        var sStatus = aTagId[1];
        var oMasterStatus = oReferencedTags[sMasterStatusId];
        var oStatus = CS.find(oMasterStatus.children, {"id": sStatus});
        oGroupByStatusTaskListMap[key] = {
          "bShowAddNewTask": false,
          "label": CS.getLabelOrCode(oStatus),
          "tasks": value,
          "status":sStatus,
          "isCollapsed": false
        }
      }
    });

    let aSortedMap = CS.sortBy(oGroupByStatusTaskListMap, function (oGroup) {
      return oGroup.label;
    });
    let aPlannedItems = CS.remove(aSortedMap, {status: "taskplanned"});
    if (!CS.isEmpty(aPlannedItems)) {
      aSortedMap.unshift(aPlannedItems[0]);
    }
    oGroupByStatusTaskListMap = CS.toPlainObject(aSortedMap);

    return oGroupByStatusTaskListMap;
  };


  var _getGroupByDueDateTaskList = function (aFilteredList) {
    //var oTaskListMap =  CS.groupBy(aFilteredList, 'overDueDate');
    var oTaskListMap =  CS.groupBy(aFilteredList, function (oTask) {
      if(oTask.dueDate) {
        return oTask.dueDate;
      } else {
        return "others";
      }
    });

    var oGroupByDueDateTaskListMap = {};
    var sDate;

    CS.forEach(oTaskListMap, function (value, sKey) {
      if(sKey === "others") {
        oGroupByDueDateTaskListMap["others"] = {
          "bShowAddNewTask": false,
          "label": getTranslation().OTHERS_TASKS,
          "tasks": value,
          "isCollapsed": false
        }
      } else {
        sDate = CommonUtils.getDateAttributeInTimeFormat (sKey);
        oGroupByDueDateTaskListMap[sKey] = {
          "bShowAddNewTask": false,
          "label": sDate,
          "tasks": value,
          "isCollapsed": false
        }
      }
    });

    oGroupByDueDateTaskListMap = CS.toPlainObject(CS.sortBy(oGroupByDueDateTaskListMap, function (oGroup, sKey) {
      return sKey;
    }));

    return oGroupByDueDateTaskListMap;
  };

  var _handleGroupByDropDownClicked = function (sId) {
    let oTaskViewData = new TasksGroupingConstants();
    var aGroupByList = oTaskViewData.taskGroupByOptions;
    var oGroup = CS.find(aGroupByList, {"id": sId});
    var oActiveLevel = TaskProps.getActiveTaskLevel() || {};
    //var aFilteredList = _getFilteredListByLevel(oActiveLevel.id, oActiveLevel.listItemKey);
    var aFilteredList = TaskProps.getFilteredList();
    var oGroupByTaskListMap = {};

    if(oGroup.id == "dueDate") {
      oGroupByTaskListMap = _getGroupByDueDateTaskList(aFilteredList);
    } else if(oGroup.id == "priority") {
      oGroupByTaskListMap = _getGroupByPriorityTaskList(aFilteredList);
    } else if(oGroup.id == "status"){
      oGroupByTaskListMap = _getGroupByStatusTaskList(aFilteredList);
    }
    else {
      oGroupByTaskListMap = _getGroupByTypeTaskList(aFilteredList, oActiveLevel.listItemKey);
    }
    TaskProps.setActiveGroupByType(oGroup.id);
    TaskProps.setTaskListGroupByType(oGroupByTaskListMap);
    TaskProps.setFilteredList(aFilteredList);
    _triggerChange();
  };

  var _handleBeforeDeleteTask = function (aTaskIds) {
    var sSelectedTaskId = TaskProps.getActiveTask().id;
    if (CS.indexOf(aTaskIds, sSelectedTaskId) >= 0) {
      TaskProps.setActiveTask({});
      TaskProps.setActiveSubTask({});
      EventBus.dispatch(Events.TASK_DATA_CHANGED, {activeTask: {}});
    }
  };

  var successDeleteTaskCallback = function (oResponse) {
    var aTaskList = TaskProps.getTaskList();
    var aSelectedTasks = TaskProps.getCheckedTaskList();
    var aSuccessIds = oResponse.success.ids;
    ContentTaskProps.setTasksCount(oResponse.success.taskCount);
    var aExceptionDetails = oResponse.failure ? oResponse.failure.exceptionDetails : [];
    if(!CS.isEmpty(aExceptionDetails)){
      var sExceptionKey = aExceptionDetails[0].key;
      switch(sExceptionKey){
        case "UserNothaveDeletePermissionForTask" :
          alertify.error(getTranslation().UserNotHaveDeletePermissionForTask);
          break;
        case "TaskIsInProgressException":
          alertify.error(getTranslation().TaskIsInProgressException);
          break;
      }
      if(CS.isEmpty(aSuccessIds)) {
        return;
      }
    }

    // if (!CS.isEmpty(aExceptionDetails) && aExceptionDetails[0].key == 'UserNothaveDeletePermissionForTask') {
    //   alertify.error(getTranslation().UserNotHaveDeletePermissionForTask);
    //   if(CS.isEmpty(aSuccessIds)) {
    //     return;
    //   }
    // }

    _setTasksScreenLockStatus(false);
    _handleBeforeDeleteTask(aSuccessIds);

    CS.forEach(aSuccessIds, function (sId) {
      CS.remove(aTaskList, {id: sId});
    });

    CS.remove(aSelectedTasks, function (sId) {
      return CS.includes(aSuccessIds, sId);
    });

    _updateTaskMap();
    var oGroupByTypeMap = TaskProps.getTaskListGroupByType();
    setDefaultActiveTask(oGroupByTypeMap);
    TaskProps.setCheckedSubTaskList([]);

    var aPropertyIdsHavingTasks = TaskProps.getPropertyIdsHavingTask();
    var oActiveProperty = TaskProps.getActiveProperty();

    var aFilteredList = TaskProps.getFilteredList();
    let oDataToUpdate = {};
    if(CS.isEmpty(aFilteredList)){
      CS.remove(aPropertyIdsHavingTasks, function (sId) {
        return oActiveProperty.instanceId == sId;
      });

      oDataToUpdate.propertyIdsHavingTasks = aPropertyIdsHavingTasks;
    }

    if(!CS.isEmpty(aSuccessIds)) {
      CommonUtils.showSuccess(ViewLibraryUtils.getDecodedTranslation(getTranslation().ENTITY_SUCCESSFULLY_DELETED , { entity : getTranslation().TASKS_LABEL }));
    }

    oDataToUpdate.tasksCount = aTaskList.length;
    EventBus.dispatch(Events.TASK_DATA_CHANGED, oDataToUpdate);
  };

  var failureDeleteTaskCallback = function (oResponse) {
    CommonUtils.failureCallback(oResponse, 'failureDeleteTasks', getTranslation());
  };

  var _handleDeleteTaskClicked = function (aSelectedTasks, oActiveContent) {
    var oPostData = {
      ids: aSelectedTasks
    };
    if (TaskProps.getTaskMode() === TaskViewContextConstants.DASHBOARD_WORKFLOW_WORKBENCH_TASK) {
      oPostData.isForDashboard = true;
    } else {
      oPostData.contentId= oActiveContent.id;
      oPostData.isForDashboard = false;
    }

    if (!CS.isEmpty(aSelectedTasks)) {
      CS.deleteRequest(TaskRequestMapping.DeleteTasks, {}, oPostData, successDeleteTaskCallback, failureDeleteTaskCallback);
    }
  };

  var _resetActiveTask = function () {
    TaskProps.setActiveTask({});
    TaskProps.setActiveSubTask({});
    TaskProps.setIsTaskDetailViewOpen(false);
    EventBus.dispatch(Events.TASK_DATA_CHANGED, {activeTask: {}});
  };

  var _handleTasksSearchTextChanged = function (sSearchString) {
    var oActiveTask = TaskProps.getActiveTask();
    let aCheckedTaskList = TaskProps.getCheckedTaskList();
    let aTaskListGroupByType = TaskProps.getTaskListGroupByType();
    let aAllGroupsTasks = [];
    let aTasksToUncheck = [];
    sSearchString = escapeRegexCharacters(sSearchString);
    let rPattern = sSearchString ? new RegExp(sSearchString, "i") : null;
    let bIsAllChecked = TaskProps.getIsAllTaskChecked();
    let aFilteredTaskList = [];

    CS.forEach(aTaskListGroupByType, function (oTaskGroup) {
      aAllGroupsTasks = aAllGroupsTasks.concat(oTaskGroup.tasks);
    });

    CS.forEach(aAllGroupsTasks, function (oTask) {
      if (rPattern) {
        if (!rPattern.test(oTask.name) && bIsAllChecked) {
          aTasksToUncheck.push(oTask.id);
        } else {
          aFilteredTaskList.push(oTask.id);
        }
      } else {
        aFilteredTaskList.push(oTask.id);
      }
    });

    TaskProps.setFilteredTaskList(aFilteredTaskList);

    CS.remove(aCheckedTaskList, function (sTaskId) {
      return CS.includes(aTasksToUncheck, sTaskId);
    });

    if(oActiveTask && oActiveTask.isDirty){
      _saveOrDiscardTask(_resetActiveTask);
    } else {
      _resetActiveTask();
    }
  };

  var _handleToggleCollapsedState = function (sContext) {
    var oTaskViewProps = TaskProps.getTaskViewProps();
    var oSectionVisualState = oTaskViewProps.sectionVisualState;

    if(oSectionVisualState[sContext]){
      oSectionVisualState[sContext].isCollapsed = !oSectionVisualState[sContext].isCollapsed;
    }
    _triggerChange();
  };

  var _discardTask = function (oCallBack) {
    var oActiveTask = TaskProps.getActiveTask();
    var oActiveSubTask = TaskProps.getActiveSubTask();
    var aTaskList = TaskProps.getTaskList();
    var oTask = CS.find(aTaskList, {id: oActiveTask.id});
    if(oActiveTask && oActiveTask.isDirty && !oActiveTask.isCreated) {
      var oOriginalTask = oActiveTask.pureObject;
      CS.forOwn(oTask, function (value, sKey) {
        oTask[sKey] = oOriginalTask[sKey]
      });
      _updateTagForTask(oTask);
      CS.forEach(oTask.subTasks, function (oSubTask) {
        _updateTagForTask(oSubTask);
      });

      TaskProps.setActiveTask(oOriginalTask);
      _setAttachmentsData();
      if(!CS.isEmpty(oActiveSubTask)){
        var oOriginalSubTask = CS.find(oOriginalTask.subTasks, {id: oActiveSubTask.id}) || {};
        TaskProps.setActiveSubTask(oOriginalSubTask);
        !CS.isEmpty(oOriginalSubTask) && _setSubTasksAttachmentsData();
      }

      TaskProps.setCheckedSubTaskList([]);
      _setTaskFormViewModels(oActiveTask);

      delete oActiveTask.isDirty;
      let oExtraData = oCallBack && oCallBack.extraData;
      TaskProps.setTemporaryCommentText("");
      TaskProps.setTemporaryCommentAttachmentData({});
      EventBus.dispatch(Events.TASK_DATA_CHANGED, CS.assign(oExtraData, {isTaskDirty: false, activeTask: oOriginalTask}));
      alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
    } else if(oActiveTask.isCreated){
      TaskProps.setActiveTask({});
      EventBus.dispatch(Events.TASK_DATA_CHANGED, {activeTask: {}});
      alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
    } else {
      alertify.message(getTranslation().NOTHING_CHANGED_TO_DISCARD);
    }
    if (oCallBack && oCallBack.functionToExecute) {
      oCallBack.functionToExecute();
    }
  };

  let successGetUserScheduledWorkflowCallback = function(oTaskListItem, oResponse){
    let aProcessList = oResponse.success.processEventsList;
    TaskProps.setUserScheduledWorkflowList(aProcessList);
    TaskProps.setSelectedLevelListIds([oTaskListItem.listItemKey]);
    TaskProps.setCheckedWorkflowList([]);
    TaskProps.setFilteredWorkflowList([]);
    TaskProps.setIsSearchFilterApplied(false);
    _triggerChange();
    return true;
  };

  let failureGetUserScheduledWorkflowCallback = function(oResponse){
    CommonUtils.failureCallback(oResponse, 'failureGetUserScheduledWorkflowCallback', getTranslation());
    return false;
  };

  let _fetchAllUserScheduledWorkflowList = function (oTaskListItem) {
    let oCurrentUser =  ContentUtils.getCurrentUser();
    let oPostData = {
      from: 0,
      searchColumn: "label",
      searchText: "",
      size: 999,
      sortBy: "label",
      sortOrder: "asc",
      filterData : {
        workflowType : ["USER_SCHEDULED_WORKFLOW"],
        physicalCatalogIds : [ContentUtils.getSelectedPhysicalCatalogId()],
        organizationIds : [oCurrentUser.organizationId]
      }
    };
    return CS.postRequest(TaskRequestMapping.GetAllUserScheduledWorkflow, {}, oPostData, successGetUserScheduledWorkflowCallback.bind(this,oTaskListItem), failureGetUserScheduledWorkflowCallback)
  };

  var _fetchAllLazyTaskDetailsList = function (sRoleId, oCallBack) {
    var oData = {};
    if (sRoleId === TaskDictionary.otherTasks) {
      oData.roleId = "all";
    }
    else {
      oData.roleId = sRoleId;
    }
    return CS.getRequest(TaskRequestMapping.fetchAllLazyTaskDetailsList, oData, successFetchAllLazyTaskDetailsList.bind(this, sRoleId, oCallBack), failureFetchAllLazyTaskDetailsList);
  };

  var _setReferencedDataForTask = function (oConfigDetails, oData) {
    let oReferencedData = {};

    if(!CS.isEmpty(oConfigDetails)) {
      oReferencedData.referencedTasks = oConfigDetails.referencedTasks;
      oReferencedData.referencedPermissions = oConfigDetails.referencedPermissions;
      oReferencedData.referencedRoles = oConfigDetails.referencedRoles;
      oReferencedData.referencedTags = oConfigDetails.referencedTags;
    }

    if(!CS.isEmpty(oData)) {
      oReferencedData.referencedVariants = oData.referencedVariants;
      oReferencedData.referencedElements = oData.referencedElements;
      oReferencedData.referencedElementsMapping = oData.referencedElementsMapping;
    }

    TaskProps.setReferencedData(oReferencedData);
  };

  var successFetchAllLazyTaskDetailsList = function (sRoleId, oCallBack, oResponse) {
    var oResponseData = oResponse.success || {};
    TaskProps.setEditableTaskId("");
    oCallBack && oCallBack.functionToSetTask && oCallBack.functionToSetTask();

    if(CS.isEmpty(TaskProps.getActiveGroupByType())){
      TaskProps.setActiveGroupByType("type");
    }
    oCallBack.notificationClicked ? TaskProps.setActiveGroupByType("status") : TaskProps.setActiveGroupByType("type");
    var oConfigDetails = oResponseData.configDetails;
    var oReferencedPermissions = oConfigDetails.referencedPermissions;
    _setReferencedDataForTask(oConfigDetails, oResponseData);
    NotificationProps.setUnreadNotificationsCount(oResponseData.unreadNotificationsCount);

    var aTaskInstanceList = oResponseData.taskInstanceList || [];
    TaskProps.setTaskList(aTaskInstanceList);
    CS.forEach(aTaskInstanceList, function (oTask) {
      _updateTagForTask(oTask);
      CS.forEach(oTask.subTasks, function (oSubTask) {
        _updateTagForTask(oSubTask);
      });
    });

    TaskProps.setActiveTask({});
    TaskProps.setActiveSubTask({});
    TaskProps.setCheckedTaskList([]);

    TaskProps.setShouldUsePropPermissions(true);
    if(!CS.isEmpty(oReferencedPermissions.globalPermission)) {
      let bCanEdit =  true ;//  can Edit is removed  (oReferencedPermissions.globalPermission.canEdit;)
      TaskProps.setTaskCanCreate(oReferencedPermissions.globalPermission.canCreate);
      TaskProps.setTaskCanEdit(bCanEdit);
      TaskProps.setTaskCanDelete(oReferencedPermissions.globalPermission.canDelete);
    }

    let oDataToUpdate = {activeTask: {}};
    let oBreadcrumbData = oCallBack.breadCrumbData;
    oBreadcrumbData && BreadcrumbStore.addNewBreadCrumbItem(oBreadcrumbData);
    _handleTaskListLevelClickedForDashboard(aTaskInstanceList, sRoleId, "all", oDataToUpdate, oCallBack);
  };

  let _updateCurrentTaskLevelList = function (sListItemId, iNewListLevelIndex) {
    let aTaskLevelList = TaskProps.getTaskLevelsList();
    let aSelectedLevels = TaskProps.getSelectedLevelListIds();
    //remove post task levels and selections
    aTaskLevelList.splice(iNewListLevelIndex + 1);
    aSelectedLevels.splice(iNewListLevelIndex);
    aSelectedLevels.push(sListItemId);
    let oActiveTaskLevelToSet = _getTaskLevelListItemById(sListItemId, iNewListLevelIndex);
    !CS.isEmpty(oActiveTaskLevelToSet) && TaskProps.setActiveTaskLevel(oActiveTaskLevelToSet);
  };

  var failureFetchAllLazyTaskDetailsList = function (oResponse) {
    CommonUtils.failureCallback(oResponse, 'failureFetchLazyTaskDetailsList', getTranslation());
    return false;
  };

  var _setTaskMode = function (sTaskMode) {
    let aTaskLevelsList = [];

    switch (sTaskMode) {
      case TaskViewContextConstants.CONTENT_TASK:
        aTaskLevelsList = [ContentTaskModelList()];
        let iCount = ContentTaskProps.getTasksCount();
        let oAllTask = CS.find(aTaskLevelsList[0],{id: 'all'});
        oAllTask.tasksCount = CS.isNotEmpty(oAllTask) ? iCount : 0;
        break;

      case TaskViewContextConstants.PROPERTY_TASK:
      case TaskViewContextConstants.IMAGE_ANNOTATION_TASK:
        aTaskLevelsList = [];
        break;

      case TaskViewContextConstants.DASHBOARD_WORKFLOW_WORKBENCH_TASK:
        let aTaskList = WorkflowWorkbenchList();
        aTaskList.showSelectTempleteButton = true;
        aTaskLevelsList = [aTaskList];
        break;
    }

    TaskProps.setTaskLevelsList(aTaskLevelsList);
    TaskProps.setTaskMode(sTaskMode);
  };

  var _getTaskMode = function () {
    return TaskProps.getTaskMode();
  };

  let _handleContentTaskDetailFormValueChanged = function (sId, sValue) {
    _makeActiveTaskDirty();
    let aActiveTaskFormModels = TaskProps.getActiveTaskFormViewModels();
    let oChangedTaskModel = CS.find(aActiveTaskFormModels, {id: sId});
    oChangedTaskModel.value = sValue;

    EventBus.dispatch(Events.TASK_DATA_CHANGED, {isTaskDirty: true});
  };

  let _fetchTaskListByProductIdForWorkflow = function (sProductId, iListLevelIndex) {
    let sRoleId = TaskProps.getRoleId();
    let sContentType = "klassinstances";
    let oParameters = {
      id: sProductId,
      getAll: true,
      contentType: sContentType
    };
    var oFilterData = {
      attributes: [],
      tags: [],
      allSearch: "",
      size: 20,
      from: 0,
      sortField: "createdOn",
      sortOrder: "desc",
      getFolders: true,
      getLeaves: true,
      isAttribute: false,
      isNumeric: false,
      selectedRoles: [],
      selectedTypes: [],
      isRed: true,
      isOrange: true,
      isYellow: true,
      isGreen: true,
      roleId: sRoleId
    };
    CS.postRequest(TaskRequestMapping.GetAllTasks, oParameters, oFilterData, successFetchTasksByProductId.bind(this, sProductId, iListLevelIndex), failureFetchAllLazyTaskDetailsList);
  };

  let successFetchTasksByProductId = function (sProductId, iListLevelIndex, oResponse) {
    _updateCurrentTaskLevelList(sProductId, iListLevelIndex);
    successFetchAllLazyTaskDetailsList("all", {}, oResponse);
  };

  let _getTaskLevelListItemById = function (sListItemId, iListLevelIndex) {
    let oTaskToReturn = {};
    let aTaskListLevels = TaskProps.getTaskLevelsList();
    let aCurrentTaskListLevel = aTaskListLevels[iListLevelIndex];
    CS.forEach(aCurrentTaskListLevel, function (oTaskGroup) {
      if (sListItemId === oTaskGroup.id) {
        oTaskToReturn = oTaskGroup;
        return false;
      } else {
        CS.forEach(oTaskGroup.children, function (oListItem) {
          if (sListItemId === oListItem.id) {
            oTaskToReturn = oListItem;
            return false;
          }
        });
      }
    });
    return oTaskToReturn;
  };

  let _toggleTaskLevelListHeaderExpansion = function (sTaskListId, iListLevelIndex, oCallbackData) {
    let oTaskListItem = _getTaskLevelListItemById(sTaskListId, iListLevelIndex);
    let bIsNotificationClicked = CommonModuleNotificationProps.getIsNotificationButtonSelected();
    oTaskListItem.isExpanded = !(oTaskListItem.isExpanded && !bIsNotificationClicked);
    if(oTaskListItem.isExpanded) {
      let aSelectedLevels = TaskProps.getSelectedLevelListIds();
      if(CS.isEmpty(aSelectedLevels) || bIsNotificationClicked) {
        let aChildren = oTaskListItem.children;
        let oFirstChild = aChildren[0];
        oCallbackData = oCallbackData || {};
        oCallbackData.notificationClicked = bIsNotificationClicked;
        return _taskDashboardListItemClicked(oFirstChild.listItemKey, oFirstChild.id, iListLevelIndex, oCallbackData);
      }
    }
    _triggerChange();
    return new Promise((resolve) => { resolve(null); });
  };

  let _handleTaskListByLevelClicked = function (sId, sContext) {
    var oActiveTask = TaskProps.getActiveTask();
    let oActiveProperty = TaskProps.getActiveProperty();
    var isTaskDialogOpen = !CS.isEmpty(oActiveProperty);
    if (oActiveTask && oActiveTask.isDirty) {
      if (isTaskDialogOpen) {
        _saveOrDiscardTask(_handleTaskListLevelClicked(sId, sContext));
      } else {
        alertify.message(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
        _triggerChange();
      }
    } else if (_activeTaskCommentSafetyCheck()) {
      _handleTaskListLevelClicked(sId, sContext);
      _triggerChange();
    }

    //TODO: Returning dummy promise need to wrap above logic in a promise and then return
    return new Promise((resolve) => { resolve(null); });
  };

  let _resetTaskListView = function () {
    _clearActiveTaskAndCloseDetailedView();
    TaskProps.setTaskListGroupByType({});
    TaskProps.setTaskList([]);
    TaskProps.setFilteredList([]);

    EventBus.dispatch(Events.TASK_DATA_CHANGED, {tasksCount: 0});
  };

  let _fetchProductListByRoleId = function (sRoleId, sListItemId, iListLevelIndex) {
    let oTaskData = TaskProps.getTaskData();
    let oCallbackData = {
      roleId: sRoleId,
      listItemId: sListItemId,
      listLevelIndex: iListLevelIndex,
      breadCrumbData: oTaskData.callbackData.breadCrumbData
    };
    let sNewListItem = UniqueIdentifierGenerator.generateUUID();
    //create New Pagination
    TaskProps.resetPaginationDataByListGroupId(sNewListItem);
    _updateCurrentTaskLevelList(sListItemId, iListLevelIndex);
    let fSuccessHandler = successFetchProductList.bind(this, oCallbackData);

    oCallbackData.breadCrumbData.functionToSet = _fetchProductListCall;
    oCallbackData.breadCrumbData.payloadData =[sNewListItem, sRoleId, false, fSuccessHandler];
    _fetchProductListCall(sNewListItem, sRoleId, false, fSuccessHandler);
  };

  let _handleProductListLoadMore = function (oTaskListItem, iListLevelIndex) {
    let fSuccessHandler = successFetchProductPagination.bind(this, iListLevelIndex, true, oTaskListItem.id);
    _fetchProductListCall(oTaskListItem.id, oTaskListItem.listItemKey, true, fSuccessHandler);
  };

  let _handleProductListSearch = function (oTaskListItem, iListLevelIndex, sSearchText) {
    let sListItemId = oTaskListItem.id;
    //reset Current Selection
    _resetTaskListView();
    TaskProps.resetPaginationDataByListGroupId(sListItemId);
    let aSelectedLevels = TaskProps.getSelectedLevelListIds();
    aSelectedLevels.splice(iListLevelIndex);
    //update pagination
    let oPagination = TaskProps.getPaginationDataByListGroups()[sListItemId];
    oPagination.searchText = sSearchText;
    _fetchProductListCall(sListItemId, oTaskListItem.listItemKey, false, successFetchProductPagination.bind(this, iListLevelIndex, false, sListItemId));
  };

  let _fetchProductListCall = function (sListItemId, sRoleId, bIsLoadMore, fSuccessHandler) {
    let oPagination = TaskProps.getPaginationDataByListGroups()[sListItemId];
    let iFrom = bIsLoadMore ? (oPagination.from + oPagination.size) : oPagination.from;
    let oRequest = {
      roleId: sRoleId,
      from: iFrom,
      size: oPagination.size,
      allSearch: oPagination.searchText
    };
    CS.postRequest(WorkflowRequestMapping.GetProductsByRoleId, "", oRequest, fSuccessHandler, failureFetchProductList);
  };


  let successFetchProductList = function (oCallbackData, oResponse) {
    let oSuccess = oResponse.success;
    let aProductList = oSuccess.children;
    TaskProps.setRoleId(oCallbackData.roleId);
    _addProductListLevel(oCallbackData.listItemId, oCallbackData.roleId, aProductList, oCallbackData.listLevelIndex);
    oCallbackData.breadCrumbData && BreadcrumbStore.addNewBreadCrumbItem(oCallbackData.breadCrumbData);
    _triggerChange();
  };

  let successFetchProductPagination = function (iListLevelIndex, bIsLoadMore, sListItemId, oResponse) {
    let oSuccess = oResponse.success;
    let aProductListFromServer = oSuccess.children;
    let aTaskLevelList = TaskProps.getTaskLevelsList();
    let oCurrentLevel = aTaskLevelList[iListLevelIndex];
    //currently only one product list will be active at a time
    let oCurrentGroup = oCurrentLevel[0];
    let aProductList = _prepareProductListItems(aProductListFromServer);
    let oCurrentPagination = TaskProps.getPaginationDataByListGroups()[sListItemId];
    if (bIsLoadMore) {
      oCurrentPagination.from = oCurrentPagination.from + aProductListFromServer.length;
    } else {
      oCurrentGroup.children = []
    }
    oCurrentGroup.children.push(...aProductList);
    _triggerChange();
  };

  let failureFetchProductList = function (oResponse) {
    CommonUtils.failureCallback(oResponse, "failureFetchProductList", getTranslation())
  };

  let _prepareProductListItems = function (aProductListModelsFromServer) {
    let sProductContext = ContentTaskViewListNodeContexts.PRODUCT_LIST_ITEM;
    let aProductList = [];
    CS.forEach(aProductListModelsFromServer, function (oProduct) {
      aProductList.push({
        id: oProduct.id,
        label: oProduct.name,
        context: sProductContext,
        listItemKey: "all"
      });
    });
    return aProductList;
  };

  let _addProductListLevel = function (sListItemId, sRoleId, aProductListFromServer) {
    let aProductList = _prepareProductListItems(aProductListFromServer);
    let aProductListLevels = TaskProps.getTaskLevelsList();
    //convert to id label
    let oListItem = CS.find(TaskDashBoardModelList(), {listItemKey: sRoleId});
    let sLabel = getTranslation()[oListItem.labelKey];
    aProductListLevels[1] =
      [{
        id: sListItemId,
        label: sLabel,
        context: ContentTaskViewListNodeContexts.PRODUCT_LIST_HEADER,
        listItemKey: sRoleId,
        isPaginated: true,
        isExpanded: true,
        isExpandable: false,
        children: aProductList
      }]
  };

  let _toggleBPNMDialogVisibility = function () {
    let oTaskViewProps = TaskProps.getTaskViewProps();
    oTaskViewProps.isBPNMDialogOpen = !oTaskViewProps.isBPNMDialogOpen;
    _triggerChange()
  };

  let _handleTaskLevelListHeaderClicked = function (oTaskListItem, iListLevelIndex, oCallbackData) {
    let sContext = oTaskListItem.context;
    switch (sContext) {
      case ContentTaskViewListNodeContexts.LIST_HEADER:
        return _toggleTaskLevelListHeaderExpansion(oTaskListItem.id, iListLevelIndex, oCallbackData);
      case ContentTaskViewListNodeContexts.TASK_DASHBOARD_LIST_ITEM:
        if(oTaskListItem.id === TaskDictionary.notifications) {
          return _taskDashboardListItemClicked(oTaskListItem.listItemKey, oTaskListItem.id, 0, oCallbackData);
        } else {
          let oCallBack = {
            functionToExecute: _updateCurrentTaskLevelList.bind(this, oTaskListItem.id, iListLevelIndex)
          };
          return _fetchAllLazyTaskDetailsList(oTaskListItem.listItemKey, oCallBack);
        }
      case ContentTaskViewListNodeContexts.CONTENT_TASK:
        return _handleTaskListByLevelClicked(oTaskListItem.id, oTaskListItem.listItemKey);

      case ContentTaskViewListNodeContexts.CONTENT_TASK_LIST_HEADER:
        return _triggerChange();

      case ContentTaskViewListNodeContexts.USER_SCHEDULED_WORKFLOW_LIST:
        return _fetchAllUserScheduledWorkflowList(oTaskListItem);
    }
  };

  let _taskDashboardListItemClicked = function (sListItemKey, sListItemId, iListLevelIndex, oCallbackData) {
    let oTaskData = TaskProps.getTaskData();
    oCallbackData = oCallbackData || {};
    oCallbackData.breadCrumbData = oTaskData.callbackData && oTaskData.callbackData.breadCrumbData;
    oCallbackData.functionToExecute = _updateCurrentTaskLevelList.bind(this, sListItemId, iListLevelIndex);

    switch (sListItemKey) {
      case "notifications":
        return NotificationStore.fetchNotifications(oCallbackData);
      default:
        let oBreadcrumbData = oCallbackData.breadCrumbData;
        oBreadcrumbData.payloadData = [sListItemKey, oCallbackData];
        oBreadcrumbData.functionToSet = _fetchAllLazyTaskDetailsList;
        if(_activeTaskCommentSafetyCheck()){
          return _fetchAllLazyTaskDetailsList(sListItemKey, oCallbackData);
        }
    }
  };

  let _setTaskViewForDashboard = function (oCallbackData) {
    let bIsNotificationClicked = CommonModuleNotificationProps.getIsNotificationButtonSelected();
    let aWorkflowList = WorkflowWorkbenchList();
    let oListItem = bIsNotificationClicked ? CS.find(aWorkflowList, {id: "workFlowWorkBenchTasksHeader"}) : CS.find(aWorkflowList, {id: "notifications"});
    let oReturnvalue = _handleTaskLevelListHeaderClicked(oListItem, 0, oCallbackData);
    if(bIsNotificationClicked){
      CommonModuleNotificationProps.setIsNotificationButtonSelected(false);
    }
    return oReturnvalue;
  };

  /** Generate Cron Expression **/
  let _generateCronExpression = (oActiveData, sActiveFrequencyTab) => {
    let sCronExpression = "";
    let sStar = "*";
    let sSpace = " ";
    switch (sActiveFrequencyTab) {
      case MockDataForFrequencyTypesDictionary.DURATION :
        /** PT02H20M **/
        /*if (oActiveData.mins === 0) {
          if (/^\d$/.test(oActiveData.hours)) {
            sCronExpression = "PT0" + oActiveData.hours + "H";
          } else {
            sCronExpression = "PT" + oActiveData.hours + "H";
          }
        } else if (oActiveData.hours === 0) {
          if (/^\d$/.test(oActiveData.mins)) {
            sCronExpression = "PT0" + oActiveData.mins + "M";
          } else {
            sCronExpression = "PT" + oActiveData.mins + "M";
          }
        }*/
        if( oActiveData.mins === 0 && oActiveData.hours === 0){
          sCronExpression = null;
        }
        else {
          if (/^\d$/.test(oActiveData.hours)) {
            sCronExpression = "PT0" + oActiveData.hours;
          } else {
            sCronExpression = "PT" + oActiveData.hours;
          }
          if (/^\d$/.test(oActiveData.mins)) {
            sCronExpression = sCronExpression + "H0" + oActiveData.mins + "M";
          } else {
            sCronExpression = sCronExpression + "H" + oActiveData.mins + "M";
          }
        }
        break;
      case MockDataForFrequencyTypesDictionary.DATE :
        /** eg: 2011-03-11T12:13:14**/
        let sDateTemp = "";
        if(CS.isEmpty(oActiveData.date) && ((oActiveData.mins === 0) && (oActiveData.hours === 0))) {
          sCronExpression = null;
        }
        else{
          if (CS.isEmpty(oActiveData.date)){
            sDateTemp = new Date();
            sDateTemp = sDateTemp.toLocaleDateString();
            sDateTemp = sDateTemp.split("/");
          }
          sDateTemp = CS.isNotEmpty(sDateTemp) ? sDateTemp : oActiveData.date.split("/");
          let sDate = sDateTemp;
          let sDay = (/^\d$/.test(sDate[1])) ? "0"+sDate[1] : sDate[1];
          let sMonth = (/^\d$/.test(sDate[0])) ? "0"+sDate[0] : sDate[0];
          let sYear = sDate[2];
          let sHours = (/^\d$/.test(oActiveData.hours)) ? "0"+oActiveData.hours : oActiveData.hours;
          let sMins = (/^\d$/.test(oActiveData.mins)) ? "0"+oActiveData.mins : oActiveData.mins;
          sCronExpression = sYear+"-"+sMonth+"-"+sDay+"T"+sHours+":"+sMins+":00";
        }

        break;
      case MockDataForFrequencyTypesDictionary.DAILY :
        /** eg: ( '15 10 1/1 * ? *' ) every day at 10:15 **/
        if( oActiveData.mins === 0 && oActiveData.hours === 0){
          sCronExpression = null;
        }else {
          sCronExpression = "0"+sSpace+oActiveData.mins+sSpace+oActiveData.hours+sSpace+"1/1"+sSpace+sStar+sSpace+"?"+sSpace+sStar;
        }
        break;
      case MockDataForFrequencyTypesDictionary.HOURMIN :
        /** eg: 0 0/3 1/1 * ? *  every 3 hours**/
        if( oActiveData.mins === 0 && oActiveData.hours === 0){
          sCronExpression = null;
        }else{
          sCronExpression = "0"+sSpace+"0/"+oActiveData.mins+sSpace+"0/"+oActiveData.hours+sSpace+"1/1"+sSpace+sSpace+sStar+sSpace+"?"+sSpace+sStar;
        }
        break;
      case MockDataForFrequencyTypesDictionary.WEEKLY :
        /** eg: 0 12 ? * MON,SAT * every mon/sat at 12 **/
        let sDaysOfWeek = oActiveData.daysOfWeeks.toLocaleString();
        if(CS.isEmpty(sDaysOfWeek)){
          sCronExpression = null;
        }else{
          sCronExpression = "0"+sSpace+oActiveData.mins+sSpace+oActiveData.hours+sSpace+"?"+sSpace+sStar+sSpace+sDaysOfWeek+sSpace+sStar+sSpace;
        }
        break;
      case MockDataForFrequencyTypesDictionary.MONTHLY :
        /** eg: 0 12 7 1/2 ? *  Day 7th of every 2 month(s)**/
        sCronExpression = "0"+sSpace+"0"+sSpace+"0"+sSpace+oActiveData.days+sSpace+"1/"+oActiveData.months+sSpace+"?"+sSpace+sStar;
        break;
      case MockDataForFrequencyTypesDictionary.YEARLY :
        /** eg: 0 12 8 6 ? * ' every year 8th of june**/
        sCronExpression = "0"+sSpace+"0"+sSpace+"0"+sSpace+oActiveData.days+sSpace+oActiveData.monthsOfYear+sSpace+"?"+sSpace+sStar;
        break;
    }
    return sCronExpression;
  };

  let _generateTimerData = function(oDataToSave, oActiveData, sActiveFrequencyTab){
    switch (sActiveFrequencyTab) {
      case MockDataForFrequencyTypesDictionary.DURATION :
        oDataToSave.timerDefinitionType = "timeDuration";
        oDataToSave.timerStartExpression = _generateCronExpression(oActiveData, sActiveFrequencyTab);
        break;
      case MockDataForFrequencyTypesDictionary.DATE :
        oDataToSave.timerDefinitionType = "timeDate";
        oDataToSave.timerStartExpression = _generateCronExpression(oActiveData, sActiveFrequencyTab);
        break;
      case MockDataForFrequencyTypesDictionary.DAILY :
        oDataToSave.timerDefinitionType = "timeCycle";
        oDataToSave.timerStartExpression = _generateCronExpression(oActiveData, sActiveFrequencyTab);
        break;
      case MockDataForFrequencyTypesDictionary.HOURMIN :
        oDataToSave.timerDefinitionType = "timeCycle";
        oDataToSave.timerStartExpression = _generateCronExpression(oActiveData, sActiveFrequencyTab);
        break;
      case MockDataForFrequencyTypesDictionary.WEEKLY :
        oDataToSave.timerDefinitionType = "timeCycle";
        oDataToSave.timerStartExpression = _generateCronExpression(oActiveData, sActiveFrequencyTab);
        break;
      case MockDataForFrequencyTypesDictionary.MONTHLY :
        oDataToSave.timerDefinitionType = "timeCycle";
        oDataToSave.timerStartExpression = _generateCronExpression(oActiveData, sActiveFrequencyTab);
        break;
      case MockDataForFrequencyTypesDictionary.YEARLY :
        oDataToSave.timerDefinitionType = "timeCycle";
        oDataToSave.timerStartExpression = _generateCronExpression(oActiveData, sActiveFrequencyTab);
        break;
    }
  };

  let successCreateWorkflowCallback = function (oResponse) {
    alertify.success(getTranslation().WORKFLOW_CREATED_SUCCESSFULLY);
    TaskProps.setIsWorkflowDialogOpen(false);
    let aWorkflowList = WorkflowWorkbenchList();
    let oTaskListItem = CS.filter(aWorkflowList, function (oWorkflowList) {
      return (oWorkflowList.id === "workflow");
    });
    EventBus.dispatch(Events.HANDLE_TASK_LIST_BY_HEADER_CLICKED, oTaskListItem[0],0);
    _triggerChange();
  };

  let failureCreateWorkflowCallback = function (oResponse) {
    CommonUtils.failureCallback(oResponse, "failureCreateWorkflowCallback", getTranslation());
  };

  let failureBulkCodeCheckList = function (oResponse) {
    CommonUtils.failureCallback(oResponse, "failureBulkCodeCheckList", getTranslation());
  };

  let successBulkCodeCheckList = function (oCallBackData, oResponse) {
    let oSuccess = oResponse.success;
    let oNameProcess_Event = oSuccess.nameCheck.Process_Event;
    let aDuplicateNames = [];
    let oPostRequest = oCallBackData.postRequest;

    CS.forEach(oNameProcess_Event, function (bValue, sKey) {
      if (!bValue) {
        aDuplicateNames.push(sKey)
      }
    });
    if (CS.isEmpty(aDuplicateNames)) {
      if(CS.isEmpty(oCallBackData.postRequest.timerStartExpression)){
        alertify.error(getTranslation().EXCEPTION_FOR_TIMERDEFINITIONTYPE_AND_TIMERSTARTEXPRESSION);
      }else{
        CS.postRequest(TaskRequestMapping.CreateWorkflow, {}, [oPostRequest], successCreateWorkflowCallback, failureCreateWorkflowCallback);
      }
    } else {
      if (CS.isNotEmpty(aDuplicateNames)) {
        alertify.error(getTranslation().WORKFLOW_NAME_ALREADY_EXISTS);
      }
    }
    _triggerChange();
  };

  /** Function to handle Workflow Template Dialog Button Clicked**/
  let _handleWorkflowTemplateDialogButtonClicked = function(sButton){
    switch (sButton) {
      case "create":
        let oFrequencyData = TaskProps.getFrequencyData();
        let oWorkflowData = TaskProps.getWorkflowData();
        let sActiveFrequencyTab = TaskProps.getSelectedTabId();
        let oWorkflowDetailsFromDropdown = TaskProps.getSelectedWorkflowId();
        let oActiveData = oFrequencyData[sActiveFrequencyTab];
        let oPostDataToCreate = {};
        oPostDataToCreate.label = oWorkflowData.label;
        oPostDataToCreate.code = "";
        oPostDataToCreate.originalEntityId = oWorkflowDetailsFromDropdown.code;
        oPostDataToCreate.isFromTemplate = true;
        oPostDataToCreate.physicalCatalogIds = [ContentUtils.getSelectedPhysicalCatalogId()];
        let oData = {};
        let oCallbackData = {};
        let oPostDataForBulkCodeCheck = [];
        oData.codes = [];
        oData.entityType = ConfigEntityTypeDictionary.ENTITY_TYPE_PROCESS_EVENT;
        oData.names = [oWorkflowData.label];
        oPostDataForBulkCodeCheck.push(oData);
        oCallbackData.postRequest = oPostDataToCreate;
         _generateTimerData(oPostDataToCreate, oActiveData, sActiveFrequencyTab);
        CS.postRequest(TaskRequestMapping.BulkCodeCheck, {}, oPostDataForBulkCodeCheck, successBulkCodeCheckList.bind(this, oCallbackData), failureBulkCodeCheckList);
        break;
      case "cancel":
        TaskProps.setIsWorkflowDialogOpen(false);
        TaskProps.setSelectedWorkflowId("");
        break;
    }
    _triggerChange();
  };

  /** Function to handle Workflow Template Workflow Selection Button Clicked**/
  let _handleWorkflowTemplateApplyButtonClicked = function (sContext, sSelectedTemplateWorkflow) {
    TaskProps.setIsWorkflowDialogOpen(true);
    TaskProps.setSelectedWorkflowId(sSelectedTemplateWorkflow);
    TaskProps.setSelectedTabId("duration");
    let oWorflowData = {
      label :  UniqueIdentifierGenerator.generateUntitledName(),
      priority : "",
    }
    TaskProps.setWorkflowData(oWorflowData);
    let oFrequencyData = TaskProps.getFrequencyData();
    oFrequencyData["duration"].hours = 0;
    oFrequencyData["duration"].mins = 0;
    TaskProps.setFrequencyData(oFrequencyData);
    _triggerChange();
  };

  /** Function to handle Workflow Template CommonConfig View MSS Value Changed**/
  let _handleMSSValueChanged = function(sContext, sSelectedItem, aContexts){
    let oWorkflowData = TaskProps.getWorkflowData();
    let oFrequencyData = TaskProps.getFrequencyData();
    switch (sContext) {
      case "priority":
        oWorkflowData[sContext] = sSelectedItem[0];
        TaskProps.setWorkflowData(oWorkflowData);
        break;

      case "frequency":
        if(aContexts[3] !== "daysOfWeeks"){
          oFrequencyData[aContexts[2]][aContexts[3]] = sSelectedItem[0];
        }else{
          oFrequencyData[aContexts[2]][aContexts[3]] = sSelectedItem;
        }
        TaskProps.setFrequencyData(oFrequencyData);
        break;
    }
  };

  /** Function to handle Workflow Template CommonConfig View Single Text Value Changed**/
  let _handleSingleTextValueChanged = function(sKey,sVal){
    let oWorkflowData = TaskProps.getWorkflowData();
    switch (sKey) {
      case "label":
        oWorkflowData[sKey] = sVal;
        break;
    }
    TaskProps.setWorkflowData(oWorkflowData);
  };

  /** Function to handle Workflow Template Common Config View MSS Value Changed based on Context**/
  let _handleWorkflowTemplateMSSChanged = function (aSelectedItems, sContext, oReferencedData) {
    let sSplitter = ContentUtils.getSplitter();
    let aContexts = sContext.split(sSplitter);
    sContext = aContexts[0];
    switch (sContext) {
      case "process":
        let sNewContext = aContexts[1];
        _handleMSSValueChanged(sNewContext,aSelectedItems,aContexts);
        break;
    }
    _triggerChange();
  };

  /** Function to handle Workflow Template CommonConfig View Single Text Value Changed based on Context**/
  let _handleWorkflowTemplateCommonConfigSectionSingleTextChanged = function (sContext, sKey, sVal) {
    switch (sContext) {
      case "process":
        _handleSingleTextValueChanged(sKey,sVal);
        break;
    }
    _triggerChange();
  };

  /** Clear data after Frequency Tab Switch**/
  let _tabChangedClearData = (sTabId) => {
    let aItems = [MockDataForFrequencyTypesDictionary.DURATION,MockDataForFrequencyTypesDictionary.DATE, MockDataForFrequencyTypesDictionary.DAILY,
      MockDataForFrequencyTypesDictionary.HOURMIN, MockDataForFrequencyTypesDictionary.WEEKLY,MockDataForFrequencyTypesDictionary.MONTHLY,
      MockDataForFrequencyTypesDictionary.YEARLY];
    let oFrequency = TaskProps.getFrequencyData();
    aItems = CS.filter(aItems, function (sItem) {
      return (sItem !== sTabId);
    });
    CS.forEach(aItems, function (sKey) {
      if((sKey === MockDataForFrequencyTypesDictionary.DURATION) ||
          (sKey === MockDataForFrequencyTypesDictionary.DAILY) ||
          (sKey === MockDataForFrequencyTypesDictionary.HOURMIN) ){
        oFrequency[sKey].hours = 0;
        oFrequency[sKey].mins = 0;
      }else if(sKey === MockDataForFrequencyTypesDictionary.WEEKLY){
        oFrequency[sKey].daysOfWeeks = [];
        oFrequency[sKey].hours = 0;
        oFrequency[sKey].mins = 0;
      }else if(sKey === MockDataForFrequencyTypesDictionary.MONTHLY){
        oFrequency[sKey].days = "";
        oFrequency[sKey].months = 1;
      }else if(sKey === MockDataForFrequencyTypesDictionary.YEARLY){
        oFrequency[sKey].days = 1;
        oFrequency[sKey].monthsOfYear = 1;
      }else if(sKey === MockDataForFrequencyTypesDictionary.DATE){
        oFrequency[sKey].date = "";
        oFrequency[sKey].hours = 0;
        oFrequency[sKey].mins = 0;
      }
    })
  };

  /** Function to handle Workflow Template Tab Layout Switch**/
  let _handleWorkflowTemplateTabLayoutTabChanged = function (sTabId) {
    TaskProps.setSelectedTabId(sTabId);
    _tabChangedClearData(sTabId);
    _triggerChange();
  };

  /** Function to handle Workflow Template Frequency Date value Changed**/
  let _handleProcessFrequencySummaryDateButtonClicked = function (sDate, sContext) {
    let oFrequencyData = TaskProps.getFrequencyData();
    let oData = oFrequencyData[sContext];
    oData.date = sDate;
    TaskProps.setFrequencyData(oFrequencyData);
    _triggerChange();
  };

  /** Function for workflow bulk save success **/
  let successBulkSaveWorkflowList = function (oResponse) {
    alertify.success(getTranslation().WORKFLOW_SCHEDULED_SUCCESSFULLY);
    _triggerChange();
  };

  /** Function for workflow bulk save failure **/
  let failureBulkSaveWorkflowList = function (oResponse) {
    CommonUtils.failureCallback(oResponse, "failureBulkSaveWorkflowList", getTranslation());
  };

  /** Function for UserScheduledWorkflow toggle button value Changed**/
  let _handleScheduledWorkflowToggled = function (oData, sValue) {
    oData.isExecutable = sValue;
    oData.isXMLModified = sValue;
    CS.postRequest(TaskRequestMapping.BulkSave, {}, [oData], successBulkSaveWorkflowList, failureBulkSaveWorkflowList);
    _triggerChange();
  };

  /** Function for workflow delete success **/
  let successDeleteWorkflowList = function (oResponse) {
    let aDeletedId = oResponse.success;
    let aWorkflowList = TaskProps.getUserScheduledWorkflowList();
    if(aDeletedId.length === aWorkflowList.length){
      let bIsAllWorkflowChecked = TaskProps.getIsAllWorkflowChecked();
      if(bIsAllWorkflowChecked){
        TaskProps.setIsAllWorkflowChecked(false);
      }
    }
    CS.forEach(aDeletedId, function (sDeletedId) {
      let iIndex = CS.findIndex(aWorkflowList, {id: sDeletedId});
      aWorkflowList.splice(iIndex, 1);
    });
    TaskProps.setUserScheduledWorkflowList(aWorkflowList);
    TaskProps.setCheckedWorkflowList([]);
    TaskProps.setFilteredWorkflowList([]);
    alertify.success(getTranslation().WORKFLOW_DELETED_SUCCESSFULLY);
    _triggerChange();
  };

  /** Function for workflow delete failure **/
  let failureDeleteWorkflowList = function (oResponse) {
    CommonUtils.failureCallback(oResponse, "failureDeleteWorkflowList", getTranslation());
  };

  /** Function for UserScheduledWorkflow delete button clicked**/
  let _handleScheduledWorkflowDeleteButtonClicked = function (sWorkflowId , sContext) {
    let aWorkflowIds = (sContext === "bulkDelete") ? sWorkflowId : [sWorkflowId];
    CS.deleteRequest(TaskRequestMapping.DeleteUserScheduledWorkflow, {}, {ids: aWorkflowIds}, successDeleteWorkflowList, failureDeleteWorkflowList);
  };

  /** Function to search workflow from list**/
  let _handleWorkflowSearchTextChanged = function (sSearchString) {
    sSearchString = escapeRegexCharacters(sSearchString);
    let rPattern = sSearchString ? new RegExp(sSearchString, "i") : null;
    let aFilteredTaskList = [];
    let aUserScheduledWorkflowList = TaskProps.getUserScheduledWorkflowList();
    if(CS.isEmpty(sSearchString)){
      TaskProps.setUserScheduledWorkflowList(aUserScheduledWorkflowList);
      TaskProps.setFilteredWorkflowList([]);
      TaskProps.setIsSearchFilterApplied(false);
    }else{
      CS.forEach(aUserScheduledWorkflowList, function (oWorkflow) {
        if (rPattern) {
          if (rPattern.test(oWorkflow.label)) {
            aFilteredTaskList.push(oWorkflow);
          }
        }
      });
      TaskProps.setFilteredWorkflowList(aFilteredTaskList);
      TaskProps.setIsSearchFilterApplied(true);
    }
    _triggerChange();
  };

  /** Function to handle select all checkbox clicked in workflow list **/
  let _handleAllWorkflowListCheckClicked = function (sSearchString) {
    let bIsAllWorkflowChecked = TaskProps.getIsAllWorkflowChecked();
    TaskProps.setIsAllWorkflowChecked(!bIsAllWorkflowChecked);
    let aCheckedTasks = [];
    sSearchString = escapeRegexCharacters(sSearchString);
    let rPattern = sSearchString ? new RegExp(sSearchString, "i") : null;
    let aFilteredTaskList = [];
    let aUserScheduledWorkflowList = TaskProps.getUserScheduledWorkflowList();

    if (bIsAllWorkflowChecked) {
      TaskProps.setCheckedWorkflowList(aCheckedTasks);
      TaskProps.setFilteredWorkflowList(aFilteredTaskList);
    } else {
      CS.forEach(aUserScheduledWorkflowList, function (oWorkflow) {
        if (rPattern) {
          if (rPattern.test(oWorkflow.label)) {
            aCheckedTasks.push(oWorkflow.id);
            aFilteredTaskList.push(oWorkflow);
          }
        } else {
          aCheckedTasks.push(oWorkflow.id);
          aFilteredTaskList.push(oWorkflow);
        }
      });
      TaskProps.setCheckedWorkflowList(aCheckedTasks);
      TaskProps.setFilteredWorkflowList(aFilteredTaskList);
    }

   _triggerChange();
  };

  /** Function to handle single checkbox clicked in workflow list **/
  let _handleWorkflowListCheckClicked = function (bIsChecked, sTaskId) {
    let aCheckedTaskList = TaskProps.getCheckedWorkflowList();
    let bIsAllWorkflowChecked = TaskProps.getIsAllWorkflowChecked();
    if(bIsChecked){
      if(aCheckedTaskList) {
        aCheckedTaskList.push(sTaskId);
      }
    }else{
      if(bIsAllWorkflowChecked) {
        TaskProps.setIsAllWorkflowChecked(false);
      }
      CS.remove(aCheckedTaskList, function (sCheckedTaskId) {
        return sCheckedTaskId == sTaskId;
      });
    }
    TaskProps.setCheckedWorkflowList(aCheckedTaskList);
    _triggerChange();
  };

  /** Function to handle bulk delete workflow list **/
  let _handleScheduledWorkflowBulkDeleteButtonClicked = function () {
    let aCheckWorkflowList = TaskProps.getCheckedWorkflowList();
    _handleScheduledWorkflowDeleteButtonClicked(aCheckWorkflowList, "bulkDelete");
  };

  /** Function to handle frequency Days Cross Icon Clicked **/
  let _handleProcessFrequencyDaysCrossIconClicked = function (aContextKey, sId) {
    let oFrequency = TaskProps.getFrequencyData();
    let sFrequency = aContextKey[2];
    let sFrequencyTime = aContextKey[3];
    let aFrequency = oFrequency[sFrequency][sFrequencyTime];
    let iIndexFrequencyIds = CS.indexOf(aFrequency, sId);
    aFrequency.splice(iIndexFrequencyIds, 1);
    oFrequency[sFrequency][sFrequencyTime] = aFrequency;
  };

  let _handleTaskFormValueChanged = function (sDate, sContext, oModel) {
    let oActiveTask = _makeActiveTaskDirty();
    let aFormFields = TaskProps.getActiveTaskFormViewModels();
    switch (sContext) {
      case "formDate" :
        let oFormDate = CS.find(aFormFields,{id : oModel.id});
        let aDateSplit = sDate.split("/");
        let sNewDate = aDateSplit[1] + "/" + aDateSplit[0] + "/" + aDateSplit[2];
        oFormDate.value = sNewDate;
        TaskProps.setActiveTaskFormViewModels(aFormFields);
    }
    EventBus.dispatch(Events.TASK_DATA_CHANGED, {isTaskDirty: true});
    _triggerChange();
  };

  //************************************* Public API's **********************************************//
  return {

    getData: function () {
      return {
        componentProps: TaskProps
      }
    },

    getTasksScreenLockStatus: function () {
      _getTasksScreenLockStatus();
    },

    setTasksScreenLockStatus: function (_bScreenLockStatus) {
      _setTasksScreenLockStatus(_bScreenLockStatus);
    },

    handleListCollapsedStateChanged: function (sTypeId) {
      _handleListCollapsedStateChanged(sTypeId);
    },

    handleTaskListByLevelClicked: function (oTaskListItem, iListLevelIndex, oCallbackData) {
      let sContext = oTaskListItem.context;
      let sListItemId = oTaskListItem.id;
      let sListItemKey = oTaskListItem.listItemKey;
      switch (sContext) {
        case ContentTaskViewListNodeContexts.TASK_DASHBOARD_LIST_ITEM:
          this.taskDashboardListItemClicked(sListItemKey, sListItemId, iListLevelIndex);
          break;
        case ContentTaskViewListNodeContexts.WORKFLOW_LIST_ITEM:
          _resetTaskListView();
          _fetchProductListByRoleId(sListItemKey, sListItemId, iListLevelIndex);
          break;
        case ContentTaskViewListNodeContexts.PRODUCT_LIST_ITEM:
          _fetchTaskListByProductIdForWorkflow(sListItemId, iListLevelIndex);
          break;
        case ContentTaskViewListNodeContexts.CONTENT_TASK:
          //for content side
          _handleTaskListByLevelClicked(sListItemId, sListItemKey);
          break;
      }
    },

    handleTaskListClicked: function (sId, oCallBack) {
      _handleTaskListClicked(sId, oCallBack);
    },

    handleSubTaskClicked: function (sId) {
      _handleSubTaskClicked(sId);
    },

    handleResetScrollState: function () {
      _handleResetScrollState();
    },

    handleSubtaskBackButtonClicked: function () {
      _handleSubtaskBackButtonClicked();
    },

    handleTaskListCheckClicked: function (bIsChecked, sId) {
      _handleTaskListCheckClicked(bIsChecked, sId);
    },

    handleAllTaskListCheckClicked: function (sSearchString) {
      _handleAllTaskListCheckClicked(sSearchString);
    },

    handleCloseTaskDetailClicked: function (oCallbackData) {
      _handleContentTaskClose(oCallbackData);
    },

    handleCommentTextChanged: function (sValue) {
      _handleCommentTextChanged(sValue);
    },

    createTaskComment: function () {
      _createTaskComment();
    },

    handleTaskFileAttachmentUpload: function (aFiles) {
      _handleTaskFileAttachmentUpload(aFiles);
    },

    handleCommentTaskFileAttachmentUpload: function (aFiles) {
      _handleCommentTaskFileAttachmentUpload(aFiles);
    },

    handleTaskFileAttachmentRemoveClicked: function(sId){
      _handleTaskFileAttachmentRemoveClicked(sId);
    },

    handleCommmentTaskFileAttachmentRemoveClicked: function (sId) {
      _handleCommmentTaskFileAttachmentRemoveClicked(sId);
    },

    handleFileAttachmentDetailViewOpen: function (sAttachmentId) {
      _handleFileAttachmentDetailViewOpen(sAttachmentId)
    },

    handleFileAttachmentDetailViewClose: function () {
      _handleFileAttachmentDetailViewClose()
    },

    handleFileAttachmentGetAllAssetExtensions: function (oRefDom) {
      _handleFileAttachmentGetAllAssetExtensions(oRefDom)
    },

    getOpenAttachmentId: function () {
      return TaskProps.getOpenAttachmentId();
    },

    handleSubTaskListCheckBoxClicked: function (sSubTaskId, bSubTaskChecked) {
      _handleSubTaskListCheckBoxClicked(sSubTaskId, bSubTaskChecked);
    },

    handleAddNewSubTaskClicked: function(oContent){
      _handleAddNewSubTaskClicked(oContent);
    },

    handleSaveSubTaskValue: function(){
      _handleSaveSubTaskValue();
    },

    handleNewSubTaskLabelValueChanged: function(sLabel){
      _handleNewSubTaskLabelValueChanged(sLabel);
    },

    handleTaskLabelValueChanged: function (sId, sValue) {
      _handleTaskLabelValueChanged(sId, sValue);
    },

    handleTaskVisibilityModeChanged:function(sId,sIsPublic){
      _handleTaskVisibilityModeChanged(sId,sIsPublic);
    },

    handleTaskDueDateChanged: function (sValue, sKey) {
      _handleTaskDueDateChanged(sValue, sKey);
    },

    handleGroupByDropDownClicked: function (sId) {
      _handleGroupByDropDownClicked(sId);
    },

    openProductFromDashboard: function (sId, sBaseType) {
      _openProductFromDashboard(sId, sBaseType);
    },

    handleDeleteTaskClicked: function (sId, oActiveContent) {

      var aSelectedTasks = !CS.isEmpty(sId) ? [sId] : TaskProps.getCheckedTaskList();
      var oActiveProperty = TaskProps.getActiveProperty();
      var aFilteredList = [];

      if(oActiveProperty && oActiveProperty.instanceId){
        var sVariantId = (oActiveContent.variantInstanceId) ? oActiveContent.variantInstanceId : "";
        aFilteredList = _getFilteredListForProperty(oActiveProperty.instanceId, sVariantId);
      } else {
        var oActiveLevel = TaskProps.getActiveTaskLevel();
        aFilteredList = _getFilteredListByLevel(oActiveLevel.id, oActiveLevel.listItemKey);
      }
      var aDeleteLabels = [];

      CS.forEach(aFilteredList, function (oTask) {
        CS.forEach(aSelectedTasks, function (sId){
          if(oTask.id == sId) {
            aDeleteLabels.push(oTask.name);
          }
        });
      });

      //aDeleteLabels = CS.trimEnd(aDeleteLabels, ', ');
      CommonUtils.listModeConfirmation(getTranslation().DELETE_CONFIRMATION, aDeleteLabels,
          function () {
            _handleDeleteTaskClicked(aSelectedTasks, oActiveContent);
          }, function (oEvent) {
          });
    },

    handleSaveTaskClicked: function () {
      _saveTask();
    },

    completeTask: function () {
      _completeTask();
    },

    handleToggleCollapsedState: function (sContext) {
      _handleToggleCollapsedState(sContext);
    },

    handleTasksRoleMSSValueChanged: function (sContext, sAction, aSelectedRoles) {
      _handleTasksRoleMSSValueChanged(sContext, sAction, aSelectedRoles);
    },

    handleTasksSearchTextChanged: function (sSearchString) {
      _handleTasksSearchTextChanged(sSearchString);
    },

    handleSubTaskEditButtonClicked: function(sSubTaskId){
      _handleSubTaskEditButtonClicked(sSubTaskId);
    },

    handleTaskNameOnBlur: function (sId) {
      _handleTaskNameOnBlur(sId);
    },

    handleSubTaskDeleteButtonClicked: function(sSubTaskId){
      _handleSubTaskDeleteButtonClicked(sSubTaskId);
    },

    handleImageSimpleAnnotationViewCreateAnnotationClicked: function(iXPosition, iYPosition, oContent){
      _handleImageSimpleAnnotationViewCreateAnnotationClicked(iXPosition, iYPosition, oContent);
    },

    handleImageSimpleAnnotationViewOpenTaskDetail: function (sTaskId) {
      _handleImageSimpleAnnotationViewOpenTaskDetail(sTaskId)
    },

    handleImageSimpleAnnotationViewCloseTaskDetail: function (sTaskId) {
      _saveOrDiscardTask(_handleImageSimpleAnnotationViewCloseTaskDetail);
    },

    saveOrDiscardTask: function (fFunctionToExecute, oExtraData) {
      _saveOrDiscardTask(fFunctionToExecute, oExtraData);
    },

    handleTaskValueChanged: function (aNewValue, sContext) {
      handleTaskValueChanged(aNewValue, sContext);
    },

    handleTaskDescriptionChanged: function (sValue) {
      _handleTaskDescriptionChanged(sValue);
    },

    handleAddNewTaskClicked: function (sTypeId, oContent) {
      _handleAddNewTaskClicked(sTypeId, oContent);
    },

    setTaskViewForContent: function (sId, sContext) {
      _setTaskViewForContent(sId, sContext);
    },

    taskDashboardListItemClicked: function (sListItemKey, sListItemId, iListLevelIndex) {
      _taskDashboardListItemClicked(sListItemKey, sListItemId, iListLevelIndex);
    },

    setTaskMode: function(sTaskMode) {
      _setTaskMode(sTaskMode);
    },

    getTaskMode: function() {
      return _getTaskMode();
    },

    clearAllTaskProps: function () {
      _clearAllTaskProps();
    },

    discardTask: function (oCallback) {
      _discardTask(oCallback);
    },

    handleContentTaskDetailFormValueChanged: function (sId, sValue) {
      _handleContentTaskDetailFormValueChanged(sId, sValue);
      _triggerChange();
    },

    handleContentTaskBPNMDialogButtonClicked: function (sId) {
      switch (sId) {
        case "bpnm_toggle_dialog":
          _toggleBPNMDialogVisibility();
          break;
      }
    },

    toggleBPNMDialogVisibility: function () {
      _toggleBPNMDialogVisibility();
    },

    handleTaskDetailSnackbarClicked: function (sKey) {
      switch (sKey) {
        case "save":
          _saveTask();
          break;
        case "complete":
          _completeTask();
          break;
        case "discard":
          _discardTask();
          break
      }
    },

    setTaskViewForDashboard: function (oCallbackData) {
      _setTaskViewForDashboard(oCallbackData);
    },

    handleTaskLevelListHeaderClicked: function (oTaskListItem, iListLevelIndex) {
      _handleTaskLevelListHeaderClicked(oTaskListItem, iListLevelIndex);
    },

    handleTaskListSearch: function (oTaskListItem, iListLevelIndex, sSearchText) {
      let sContext = oTaskListItem.context;
      switch (sContext) {
        case ContentTaskViewListNodeContexts.PRODUCT_LIST_HEADER:
          _handleProductListSearch(oTaskListItem, iListLevelIndex, sSearchText);
          break;
      }
    },

    handleTaskListLoadMore: function (oTaskListItem, iListLevelIndex) {
      let sContext = oTaskListItem.context;
      switch (sContext) {
        case ContentTaskViewListNodeContexts.PRODUCT_LIST_HEADER:
          _handleProductListLoadMore(oTaskListItem, iListLevelIndex);
          break;
      }
    },

    handleTaskDialogCloseClicked: function () {
      _handleTaskDialogCloseClicked();
    },

    handleFileAttachmentUploadClicked: function(sContext, aFiles){
      _handleFileAttachmentUploadClicked(sContext, aFiles);
    },

    handleContentEditToolbarButtonClicked : function (sId) {
      _handleContentEditToolbarButtonClicked(sId);
    },

    updateTaskProps: function (oData) {
      _updateTaskProps(oData);
    },

    fetchAnnotationData: function (oImageAttribute) {
      _handleCoverflowDetailViewShowAnnotationButtonClicked(oImageAttribute);
    },

    activeTaskSafetyCheck: function () {
      return _activeTaskSafetyCheck()
    },

    activeTaskCommentSafetyCheck: function () {
      return _activeTaskCommentSafetyCheck();
    },

    handleWorkflowTemplateDialogButtonClicked: function (sButton) {
      _handleWorkflowTemplateDialogButtonClicked(sButton);
    },

    handleWorkflowTemplateApplyButtonClicked: function (sContext, sSelectedTemplateWorkflow) {
      _handleWorkflowTemplateApplyButtonClicked(sContext, sSelectedTemplateWorkflow);
    },

    handleWorkflowTemplateMSSChanged: function (aSelectedItems, sContext, oReferencedData) {
      _handleWorkflowTemplateMSSChanged(aSelectedItems, sContext, oReferencedData);
    },

    handleWorkflowTemplateCommonConfigSectionSingleTextChanged: function (sContext, sKey, sVal) {
      _handleWorkflowTemplateCommonConfigSectionSingleTextChanged(sContext, sKey, sVal);
    },

    handleWorkflowTemplateTabLayoutTabChanged: function (sTabId) {
      _handleWorkflowTemplateTabLayoutTabChanged(sTabId);
    },

    handleProcessFrequencySummaryDateButtonClicked: function (sDate, sContext) {
      _handleProcessFrequencySummaryDateButtonClicked(sDate, sContext);
    },

    handleScheduledWorkflowToggled: function (oData, sValue) {
      _handleScheduledWorkflowToggled(oData, sValue);
    },

    handleScheduledWorkflowDeleteButtonClicked: function (sWorkflowId) {
      _handleScheduledWorkflowDeleteButtonClicked(sWorkflowId);
    },

    handleWorkflowSearchTextChanged: function (sSearchString) {
      _handleWorkflowSearchTextChanged(sSearchString);
    },

    handleAllWorkflowListCheckClicked: function (sSearchString) {
      _handleAllWorkflowListCheckClicked(sSearchString);
    },

    handleWorkflowListCheckClicked: function (bIsChecked, sId) {
      _handleWorkflowListCheckClicked(bIsChecked, sId);
    },

    handleScheduledWorkflowBulkDeleteButtonClicked: function () {
      _handleScheduledWorkflowBulkDeleteButtonClicked();
    },

    handleProcessFrequencyDaysCrossIconClicked: function (aContextKey, sId) {
      _handleProcessFrequencyDaysCrossIconClicked(aContextKey, sId);
    },

    handleTaskFormValueChanged: function(sDate, sContext, oModel){
      _handleTaskFormValueChanged(sDate, sContext, oModel);
    },

    triggerChange: function () {
      _triggerChange();
    }
  }

})();

MicroEvent.mixin(TaskStore);

export const store = TaskStore;
export const events = Events;
