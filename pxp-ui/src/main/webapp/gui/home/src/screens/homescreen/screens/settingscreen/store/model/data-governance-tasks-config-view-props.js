
let DataGovernanceTasksConfigViewProps = (function () {

  let Props = function () {
    return {
      oDataGovernanceTasksValueList: {},
      aDataGovernanceTasksList: {},
      oActiveDataGovernanceTask: {},
      bDataGovernanceTasksScreenLockStatus: false,
      aDataGovernanceTaskGridData: []
    };
  };

  let oProperties = new Props();

  return {
    getDataGovernanceTasksValuesList: function () {
      return oProperties.oDataGovernanceTasksValueList;
    },

    setDataGovernanceTasksValuesList: function (_oDataGovernanceTasksValueList) {
      oProperties.oDataGovernanceTasksValueList = _oDataGovernanceTasksValueList;
    },

    getDataGovernanceTasksList: function () {
      return oProperties.aDataGovernanceTasksList;
    },

    setDataGovernanceTasksList: function (_aDataGovernanceTasksList) {
      oProperties.aDataGovernanceTasksList = _aDataGovernanceTasksList;
    },

    getDataGovernanceTasksScreenLockStatus: function () {
      return oProperties.bDataGovernanceTasksScreenLockStatus;
    },

    setDataGovernanceTasksScreenLockStatus: function (_status) {
      oProperties.bDataGovernanceTasksScreenLockStatus = _status;
    },

    getActiveDataGovernanceTask: function () {
      return oProperties.oActiveDataGovernanceTask;
    },

    setActiveDataGovernanceTask: function (_oActiveDataGovernanceTask) {
      oProperties.oActiveDataGovernanceTask = _oActiveDataGovernanceTask;
    },

    getDataGovernanceTaskGridData: function () {
      return oProperties.aDataGovernanceTaskGridData;
    },

    setDataGovernanceTaskGridData: function (_aDataGovernanceTaskGridData) {
      oProperties.aDataGovernanceTaskGridData = _aDataGovernanceTaskGridData;
    },

    reset: function () {
      oProperties = new Props();
    }
  };
})();

export default DataGovernanceTasksConfigViewProps;