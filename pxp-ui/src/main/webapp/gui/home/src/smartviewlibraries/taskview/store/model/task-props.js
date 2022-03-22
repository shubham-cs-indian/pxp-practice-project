import CS from '../../../../libraries/cs';
import TabHeaderData
  from "../../../../screens/homescreen/screens/settingscreen/tack/mock/mock-data-for-frequency-dictionary";

let oDefaultPagination = {
  from: 0,
  size: 20,
  searchText: "",
};

var TaskProps = (function () {

  var Props = function () {
    return {
      oTaskViewProps : {
        commentTemporaryData: {
          commentText : "",
          commentAttachmentData : [],
        },
        commentAttachmentDataMap: {},
        checkedTasks : [],
        editableTaskId : "",
        attachmentData : [],
        bIsAllTaskChecked: false,
        resetScrollStatus: false,
        oSubTaskProps : {
          editableSubTaskId : "",
          checkedSubTasks : []
        },
        activePropertyId: "",
        sOpenAttachmentId: "",
        isCollapsedRoles: true,
        isCollapsedSubTasks: true,
        isCollapsedComments: true,
        isBPNMDialogOpen: false,
        sectionVisualState: {
          roles: {
            isCollapsed: true
          },
          subTasks: {
            isCollapsed: true
          },
          comments: {
            isCollapsed: true
          },
          taskForm: {
            isCollapsed: true
          },
          workflowStatus: {
            isCollapsed: true
          },
          linkedEntity: {
            isCollapsed: true
          }
        }
      },
      aReferencedVariants: [],
      oReferencedElements: {},
      aFilteredList: [],
      aFilteredTaskList: [],
      bIsTaskViewOpen: false,
      bScreenLockStatus: false,
      aTaskList: [],
      oActiveTaskLevel: {},
      oActiveTask: {},
      oTaskListGroupByType: {},
      aTaskTypes: [],
      oPriorityTagDetails: {},
      oStatusTagDetails: {},
      sActiveGroupByType: "type",
      canEditTask: false,
      canCreateTask: false,
      canDeleteTask: false,
      usePropPermissions: false,
      oActiveProperty: {},
      oReferencedElementsMapping: {},
      bIsTaskAnnotationDetailViewOpen: false,
      workFlowPossibleTagValues: {},
      oActiveSubTask: {},
      aEditableRoles: [],
      referencedTags: [],
      taskLevelsList: [],
      selectedLevelListIds: [],
      paginationDataByListGroupIds: {},
      sMode: "",
      aTaskFormViewModels: [],
      sRoleId: "",
      referencedData: {},
      sTemplateId: {},
      aPropertyIdsHavingTasks: [],
      bShowImageAnnotations: false,
      oTaskData: {},
      bIsWorkflowDialogOpen: false,
      sSelectedWorkflowId : "",
      aHeaderData: [
        {
          id: TabHeaderData.DURATION,
          label: 'Duration'
        },
        {
          id: TabHeaderData.DATE,
          label: 'Date'
        },
        {
          id: TabHeaderData.DAILY,
          label: 'Daily'
        },
        {
          id: TabHeaderData.HOURMIN,
          label: 'HourMin'
        },
        {
          id: TabHeaderData.WEEKLY,
          label: 'Weekly'
        },
        {
          id: TabHeaderData.MONTHLY,
          label: 'Monthly'
        },
        {
          id: TabHeaderData.YEARLY,
          label: 'Yearly'
        }
      ],
      sSelectedTabId: TabHeaderData.DURATION,
      oFrequencyData : {
        duration : {
          hours : 0,
          mins : 0
        },
        date : {
          date :"",
          hours : 0,
          mins : 0
        },
        daily : {
          hours : 0,
          mins : 0
        },
        hourmin : {
          hours : 0,
          mins : 0
        },
        weekly : {
          daysOfWeeks: [],
          hours : 0,
          mins : 0
        },
        monthly : {
          days: 1,
          months : 1,
        },
        yearly : {
          monthsOfYear : 1,
          days : 1
        },
      },
      workflowData:{
        label : "",
      },
      userScheduledWorkflowList : {},
      bIsSearchFilterApplied: false,
      filteredWorkflowList: [],
      bIsAllWorkflowChecked: false,
      checkedWorkflows: [],
    }
  };

  var oProperties = new Props();

  return {

    getTaskData: function () {
      return oProperties.oTaskData;
    },

    setTaskData: function (_oTaskData) {
      oProperties.oTaskData = _oTaskData;
    },

    getShowAnnotations: function(){
      return oProperties.bShowImageAnnotations;
    },

    setShowAnnotations: function (bFlag) {
      oProperties.bShowImageAnnotations = bFlag;
    },

    getPropertyIdsHavingTask: function () {
      return oProperties.aPropertyIdsHavingTasks;
    },

    setPropertyIdsHavingTask: function (_aPropertyIdsHavingTasks) {
      oProperties.aPropertyIdsHavingTasks = _aPropertyIdsHavingTasks;
    },

    getTemplateId: function () {
      return oProperties.sTemplateId;
    },

    setTemplateId: function (_sTemplateId) {
      oProperties.sTemplateId = _sTemplateId;
    },

    getReferencedData: function () {
      return oProperties.referencedData;
    },

    setReferencedData: function (_oReferencedData) {
      oProperties.referencedData = _oReferencedData;
    },

    getTaskList: function () {
      return oProperties.aTaskList;
    },

    setTaskList: function (_aTaskList) {
      oProperties.aTaskList = _aTaskList;
    },

    getActiveGroupByType: function () {
      return oProperties.sActiveGroupByType;
    },

    setActiveGroupByType: function (_sActiveGroupByType) {
      oProperties.sActiveGroupByType = _sActiveGroupByType;
    },

    getFilteredList: function () {
      return oProperties.aFilteredList;
    },

    setFilteredList: function (_aFilteredList) {
      oProperties.aFilteredList = _aFilteredList;
    },

    getPriorityTagDetails: function () {
      return oProperties.oPriorityTagDetails;
    },

    setPriorityTagDetails: function (_oPriorityTagDetails) {
      oProperties.oPriorityTagDetails = _oPriorityTagDetails;
    },

    getStatusTagDetails: function () {
      return oProperties.oStatusTagDetails;
    },

    setStatusTagDetails: function (_oStatusTagDetails) {
      oProperties.oStatusTagDetails = _oStatusTagDetails;
    },

    getReferencedVariants: function () {
      return oProperties.aReferencedVariants;
    },

    setReferencedVariants: function (_aReferencedVariants) {
      oProperties.aReferencedVariants = _aReferencedVariants;
    },

    getReferencedTasksElements: function () {
      return oProperties.oReferencedElements;
    },

    setReferencedTasksElements: function (_oReferencedElements) {
      oProperties.oReferencedElements = _oReferencedElements;
    },

    getReferencedTasksElementsMapping: function () {
      return oProperties.oReferencedElementsMapping;
    },

    setReferencedTasksElementsMapping: function (_oReferencedElementsMapping) {
      oProperties.oReferencedElementsMapping = _oReferencedElementsMapping;
    },

    getTaskTypes: function () {
      return oProperties.aTaskTypes;
    },

    setTaskTypes: function (_aTaskTypes) {
      oProperties.aTaskTypes = _aTaskTypes;
    },

    getTaskListGroupByType: function () {
      return oProperties.oTaskListGroupByType;
    },

    setTaskListGroupByType: function (_oTaskListGroupByType) {
      oProperties.oTaskListGroupByType = _oTaskListGroupByType;
    },

    getActiveTaskLevel: function () {
      return oProperties.oActiveTaskLevel;
    },

    setActiveTaskLevel: function (_oActiveTaskLevel) {
      oProperties.oActiveTaskLevel = _oActiveTaskLevel;
    },

    getActiveTask: function () {
      return oProperties.oActiveTask;
    },

    setActiveTask: function (_oActiveTask) {
      oProperties.oActiveTask = _oActiveTask;
    },

    getActiveSubTask: function () {
      return oProperties.oActiveSubTask;
    },

    setActiveSubTask: function (_oActiveSubTask) {
      oProperties.oActiveSubTask = _oActiveSubTask;
    },

    getIsTaskDetailViewOpen: function () {
      return oProperties.bIsTaskDetailViewOpen;
    },

    setIsTaskDetailViewOpen: function (_bIsTaskDetailViewOpen) {
      oProperties.bIsTaskDetailViewOpen = _bIsTaskDetailViewOpen;
    },

    getTaskViewProps : function () {
      return oProperties.oTaskViewProps;
    },

    setTaskViewProps : function(oProps){
      oProperties.oTaskViewProps = oProps;
    },

    getEditableSubTaskId : function(){
      return oProperties.oTaskViewProps.oSubTaskProps.editableSubTaskId;
    },

    setEditableSubTaskId : function(sEditableSubTaskId){
      oProperties.oTaskViewProps.oSubTaskProps.editableSubTaskId = sEditableSubTaskId;
    },

    getEditableTaskId: function () {
      return oProperties.oTaskViewProps.editableTaskId;
    },

    setEditableTaskId: function (sActiveTaskId) {
      oProperties.oTaskViewProps.editableTaskId = sActiveTaskId;
    },

    getTasksScreenLockStatus: function () {
      return oProperties.bScreenLockStatus;
    },

    setTasksScreenLockStatus: function (_bScreenLockStatus) {
      oProperties.bScreenLockStatus = _bScreenLockStatus;
    },

    getCheckedSubTaskList : function () {
      return oProperties.oTaskViewProps.oSubTaskProps.checkedSubTasks;
    },

    setCheckedSubTaskList : function (aCheckedSubTaskList){
      oProperties.oTaskViewProps.oSubTaskProps.checkedSubTasks = aCheckedSubTaskList;
    },

    addToCheckedSubTaskList : function (sSubTaskId){
      oProperties.oTaskViewProps.oSubTaskProps.checkedSubTasks.push(sSubTaskId);
    },

    getCheckedTaskList : function (){
      return oProperties.oTaskViewProps.checkedTasks;
    },

    setCheckedTaskList : function (aCheckedTaskList) {
      oProperties.oTaskViewProps.checkedTasks = aCheckedTaskList;
    },

    addToCheckedTaskList : function (sTaskId){
      oProperties.oTaskViewProps.checkedTasks.push(sTaskId);
    },

    getTemporaryCommentText : function () {
      return oProperties.oTaskViewProps.commentTemporaryData.commentText;
    },

    setTemporaryCommentText : function (sValue) {
      oProperties.oTaskViewProps.commentTemporaryData.commentText = sValue;
    },

    getTemporaryCommentAttachmentData : function () {
      return oProperties.oTaskViewProps.commentTemporaryData.commentAttachmentData;
    },

    setTemporaryCommentAttachmentData : function (aData) {
      oProperties.oTaskViewProps.commentTemporaryData.commentAttachmentData = aData;
    },

    addToTemporaryCommmentAttachmentData : function (oAttachmentObject) {
      oProperties.oTaskViewProps.commentTemporaryData.commentAttachmentData.push(oAttachmentObject);
    },

    setAttachmentData: function(aData){
      oProperties.oTaskViewProps.attachmentData = aData;
    },

    setCommentAttachmentDataMap: function (oData) {
      oProperties.oTaskViewProps.commentAttachmentDataMap = oData;
    },

    addToCommentAttachmentDataMap: function (sCommentId, oData) {
      oProperties.oTaskViewProps.commentAttachmentDataMap[sCommentId] = oData;
    },

    getActiveProperty: function () {
      return oProperties.oActiveProperty;
    },

    setActiveProperty: function(_oActiveProperty){
      oProperties.oActiveProperty = _oActiveProperty;
    },

    getTaskCanEdit: function () {
      return oProperties.canEditTask;
    },

    setTaskCanEdit: function(bCanEdit){
      oProperties.canEditTask = bCanEdit;
    },

    getTaskCanCreate: function () {
      return oProperties.canCreateTask;
    },

    setTaskCanCreate: function(bCanCreate){
      oProperties.canCreateTask = bCanCreate;
    },

    getTaskCanDelete: function () {
      return oProperties.canDeleteTask;
    },

    setTaskCanDelete: function(bCanDelete){
      oProperties.canDeleteTask = bCanDelete;
    },


    getShouldUsePropPermissions: function(){
      return oProperties.usePropPermissions;
    },

    setShouldUsePropPermissions: function (bUsePropsPermissions) {
      oProperties.usePropPermissions = bUsePropsPermissions;
    },

    getIsAllTaskChecked : function (){
      return oProperties.oTaskViewProps.bIsAllTaskChecked;
    },

    setIsAllTaskChecked : function (bIsAllTaskChecked) {
      oProperties.oTaskViewProps.bIsAllTaskChecked = bIsAllTaskChecked;
    },

    getIsTaskAnnotationDetailViewOpen: function () {
      return oProperties.bIsTaskAnnotationDetailViewOpen;
    },

    setIsTaskAnnotationDetailViewOpen: function (bIsTaskAnnotationDetailViewOpen) {
      oProperties.bIsTaskAnnotationDetailViewOpen = bIsTaskAnnotationDetailViewOpen;
    },

    getOpenAttachmentId: function(){
      return oProperties.oTaskViewProps.sOpenAttachmentId;
    },

    setOpenAttachmentId: function(sOpenAttachmentId){
      oProperties.oTaskViewProps.sOpenAttachmentId = sOpenAttachmentId;
    },

    getWorkFlowStatusPossibleTagValues: function(){
      return oProperties.workFlowPossibleTagValues;
    },

    setWorkFlowStatusPossibleTagValues: function(oObj){
      oProperties.workFlowPossibleTagValues = oObj;
    },

    getEditableRoles: function () {
      return oProperties.aEditableRoles;
    },

    setEditableRoles: function (_aEditableRoles) {
      oProperties.aEditableRoles = _aEditableRoles;
    },

    getResetScrollStatus: function () {
      return oProperties.oTaskViewProps.resetScrollStatus;
    },

    setResetScrollStatus :function (resetScrollStatus) {
      oProperties.oTaskViewProps.resetScrollStatus = resetScrollStatus;
    },

    getReferencedTagsForTask: function () {
      return oProperties.referencedTags;
    },

    setReferencedTagsForTask: function (_aReferencedTags) {
      oProperties.referencedTags = _aReferencedTags;
    },

    getTaskLevelsList: function () {
      return oProperties.taskLevelsList;
    },

    setTaskLevelsList: function (_aTaskLevelsList) {
      oProperties.taskLevelsList = _aTaskLevelsList;
    },

    getSelectedLevelListIds: function () {
      return oProperties.selectedLevelListIds;
    },

    setSelectedLevelListIds: function (_aSelectedLevelListIds) {
      oProperties.selectedLevelListIds = _aSelectedLevelListIds;
    },

    getPaginationDataByListGroups: function () {
      return oProperties.paginationDataByListGroupIds;
    },

    setPaginationDataByListGroup: function (oPagination) {
      oProperties.paginationDataByListGroupIds = oPagination;
    },

    resetPaginationDataByListGroupId: function (sId) {
      oProperties.paginationDataByListGroupIds[sId] = CS.cloneDeep(oDefaultPagination)
    },

    getTaskMode: function () {
      return oProperties.sMode;
    },

    setTaskMode: function (_sTaskMode) {
      oProperties.sMode = _sTaskMode;
    },

    getFilteredTaskList: function () {
      return oProperties.aFilteredTaskList;
    },

    setFilteredTaskList: function (_aFilteredTaskList) {
      oProperties.aFilteredTaskList = _aFilteredTaskList
    },

    getActiveTaskFormViewModels: function () {
      return oProperties.aTaskFormViewModels;
    },

    setActiveTaskFormViewModels: function (_aTaskFormViewModels) {
      oProperties.aTaskFormViewModels = _aTaskFormViewModels
    },

    getRoleId: function () {
      return oProperties.sRoleId;
    },

    setRoleId: function (_sRoleId) {
      oProperties.sRoleId = _sRoleId
    },

    getIsWorkflowDialogOpen: function (){
      return oProperties.bIsWorkflowDialogOpen;
    },

    setIsWorkflowDialogOpen: function (_bIsWorkflowDialogOpen){
      oProperties.bIsWorkflowDialogOpen = _bIsWorkflowDialogOpen;
    },

    getSelectedWorkflowId: function (){
      return oProperties.sSelectedWorkflowId;
    },

    setSelectedWorkflowId: function (_sSelectedWorkflowId){
      oProperties.sSelectedWorkflowId = _sSelectedWorkflowId;
    },

    getSelectedTabId: function () {
      return oProperties.sSelectedTabId;
    },

    setSelectedTabId: function (_sSelectedId) {
      oProperties.sSelectedTabId = _sSelectedId;
    },

    getTabHeaderData: function () {
      return oProperties.aHeaderData;
    },

    setTabHeaderData: function (_aHeaderData) {
      oProperties.aHeaderData = _aHeaderData;
    },

    getFrequencyData: function () {
      return oProperties.oFrequencyData;
    },

    setFrequencyData: function (_oFrequencyData) {
      oProperties.oFrequencyData = _oFrequencyData;
    },

    getWorkflowData: function () {
      return oProperties.workflowData;
    },

    setWorkflowData: function (_oWorkflowData) {
      oProperties.workflowData = _oWorkflowData;
    },

    getUserScheduledWorkflowList: function () {
      return oProperties.userScheduledWorkflowList;
    },

    setUserScheduledWorkflowList: function (_oUserScheduledWorkflowList) {
      oProperties.userScheduledWorkflowList = _oUserScheduledWorkflowList;
    },

    getIsSearchFilterApplied: function () {
      return oProperties.bIsSearchFilterApplied;
    },

    setIsSearchFilterApplied: function (bIsFilterApplied) {
      oProperties.bIsSearchFilterApplied = bIsFilterApplied;
    },

    getFilteredWorkflowList: function () {
      return oProperties.filteredWorkflowList;
    },

    setFilteredWorkflowList: function (_aFilteredWorkflowList) {
      oProperties.filteredWorkflowList = _aFilteredWorkflowList;
    },

    getIsAllWorkflowChecked : function (){
      return oProperties.bIsAllWorkflowChecked;
    },

    setIsAllWorkflowChecked : function (_bIsAllWorkflowChecked) {
      oProperties.bIsAllWorkflowChecked = _bIsAllWorkflowChecked;
    },

    getCheckedWorkflowList : function (){
      return oProperties.checkedWorkflows;
    },

    setCheckedWorkflowList : function (_aCheckedWorkflows) {
      oProperties.checkedWorkflows = _aCheckedWorkflows;
    },

    toJSON: function () {
      return {}
    },

    reset: function () {
      oProperties = new Props();
    }
  };

})();

export default TaskProps;
