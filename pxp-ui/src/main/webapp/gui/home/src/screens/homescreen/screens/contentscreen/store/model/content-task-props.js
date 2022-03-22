var ContentTaskProps = (function () {

  var Props = function () {

    return {
      oTaskData: {},
      bIsTaskDirty: false,
      bShowImageAnnotations: false,
      iTasksCount: 0,
      isTaskDialogActive: false,
      bIsTaskAdded: false,
      aPropertyIdsHavingTasks: [],
      oReferencedTasks: {},
      activeTask: {},
      selectedTaskIds: [],
      isTaskForceUpdate: false
    };
  };

  var Properties = new Props();

  return {

    getSelectedTaskIds: function () {
      return Properties.selectedTaskIds;
    },

    setSeletcedTaskIds: function (_aSelectedTaskIds) {
      Properties.selectedTaskIds = _aSelectedTaskIds;
    },

    getActiveTask: function () {
      return Properties.activeTask;
    },

    setActiveTask: function (_oActiveTask) {
      Properties.activeTask = _oActiveTask;
    },

    getReferencedTasks: function () {
      return Properties.oReferencedTasks;
    },

    setReferencedTasks: function (_oReferencedTasks) {
      Properties.oReferencedTasks = _oReferencedTasks;
    },

    getShowAnnotations: function () {
      return Properties.bShowImageAnnotations;
    },

    setShowAnnotations: function (bFlag) {
      Properties.bShowImageAnnotations = bFlag;
    },

    getIsTaskDirty: function () {
      return Properties.bIsTaskDirty;
    },

    setIsTaskDirty: function (_bIsTaskDirty) {
      Properties.bIsTaskDirty = _bIsTaskDirty;
    },

    getTaskData: function () {
      return Properties.oTaskData;
    },

    setTaskData: function (_oTaskData) {
      Properties.oTaskData = _oTaskData;
    },

    getTasksCount: function () {
      return Properties.iTasksCount;
    },

    setTasksCount: function (_iTasksCount) {
      Properties.iTasksCount = _iTasksCount;
    },

    setIsTaskAddedStatus: function (_bIsTaskAdded) {
      Properties.bIsTaskAdded = _bIsTaskAdded;
    },

    getIsTaskAddedStatus: function () {
      return Properties.bIsTaskAdded;
    },

    setIsTaskDialogActive: function (bIsDialogActive) {
      Properties.isTaskDialogActive = bIsDialogActive
    },

    getIsTaskDialogActive: function () {
      return Properties.isTaskDialogActive;
    },

    getPropertyIdsHavingTask: function () {
      return Properties.aPropertyIdsHavingTasks;
    },

    setPropertyIdsHavingTask: function (_aPropertyIdsHavingTasks) {
      Properties.aPropertyIdsHavingTasks = _aPropertyIdsHavingTasks;
    },

    getIsTaskForceUpdate: function() {
      return Properties.isTaskForceUpdate;
    },

    setIsTaskForceUpdate: function(_isTaskForceUpdate) {
      Properties.isTaskForceUpdate = _isTaskForceUpdate;
    },

    reset: function () {
      Properties = new Props();
    }
  }
})();

export default ContentTaskProps;
