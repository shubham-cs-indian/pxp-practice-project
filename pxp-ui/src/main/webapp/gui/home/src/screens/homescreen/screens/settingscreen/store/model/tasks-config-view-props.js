/**
 * Created by CS98 on 30/3/2017.
 */

var TasksConfigViewProps = (function () {
  /** TODO: Remove unused props - Shashank**/

  let Props = function () {
    return {
      aTasksList: {},
      oActiveTask: {},
      aTaskGridData: [],
    };
  };

  let oProperties = new Props();

  return {
    getTaskList: function () {
      return oProperties.aTasksList;
    },

    setTaskList: function (_aTasksList) {
      oProperties.aTasksList = _aTasksList;
    },

    getActiveTask: function () {
      return oProperties.oActiveTask;
    },

    setActiveTask: function (_oActiveTask) {
      oProperties.oActiveTask = _oActiveTask;
    },

    getTaskGridData: function () {
      return oProperties.aTaskGridData;
    },

    setTaskGridData: function (_aTaskGridData) {
      oProperties.aTaskGridData = _aTaskGridData;
    },

    reset: function () {
      oProperties = new Props();
    },

  };
})();

export default TasksConfigViewProps;