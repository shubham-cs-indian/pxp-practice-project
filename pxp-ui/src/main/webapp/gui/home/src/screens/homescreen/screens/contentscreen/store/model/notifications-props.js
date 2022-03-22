var NotificationsProps = (function () {
  var Props = function () {
    return {
      aNotifications: [],
      aNotificationViewModel: {},
      iUnreadNotificationsCount: null,
      oPaginationData: {
        from: 0,
        size: 20
      },
      iTotalNotificationsCount: null,
      aTaskInfo: [],
      aUserList: [],
      oRoleMap: {},
      iTimeInterval: 5000,
      isRefreshing: false,
      oReferencedTags: {},
      oReferencedTasks: {}
    }
  };

  var oProperties = new Props();

  return {

    getTotalNotificationsCount: function () {
      return oProperties.iTotalNotificationsCount;
    },

    setTotalNotificationsCount: function (_iTotalNotificationsCount) {
      oProperties.iTotalNotificationsCount = _iTotalNotificationsCount;
    },

    getReferencedTags: function () {
      return oProperties.oReferencedTags;
    },

    setReferencedTags: function (_oReferencedTags) {
      oProperties.oReferencedTags = _oReferencedTags;
    },

    getReferencedTasks: function () {
      return oProperties.oReferencedTasks;
    },

    setReferencedTasks: function (_oReferencedTasks) {
      oProperties.oReferencedTasks = _oReferencedTasks;
    },

    getRefreshingState: function () {
      return oProperties.isRefreshing;
    },

    setRefreshingState: function (_bIsRefreshing) {
      oProperties.isRefreshing = _bIsRefreshing;
    },

    getTimeInterval: function () {
      return oProperties.iTimeInterval;
    },

    getPaginationDataForNotifications: function () {
      return oProperties.oPaginationData;
    },

    setPaginationDataForNotification: function (_oPaginationData) {
      oProperties.oPaginationData = _oPaginationData;
    },

    getUnreadNotificationsCount: function () {
      return oProperties.iUnreadNotificationsCount;
    },

    setUnreadNotificationsCount: function (_iUnreadNotificationsCount) {
      oProperties.iUnreadNotificationsCount = _iUnreadNotificationsCount;
    },

    getTasksDetails: function () {
      return oProperties.aTaskInfo;
    },

    setTaskDetails: function (_aTaskInfo) {
      oProperties.aTaskInfo = _aTaskInfo;
    },

    getRoleDetails: function () {
      return oProperties.oRoleMap;
    },

    setRoleDetails: function (_oRoleMap) {
      oProperties.oRoleMap = _oRoleMap;
    },

    getUsersList: function () {
      return oProperties.aUserList;
    },

    setUsersList: function (_aUsersList) {
      oProperties.aUserList = _aUsersList;
    },

    getNotificationModel: function () {
      return oProperties.aNotificationViewModel;
    },

    setNotificationModel: function (_aNotificationViewModel) {
      oProperties.aNotificationViewModel = _aNotificationViewModel;
    },

    getNotificationsList: function () {
      return oProperties.aNotifications;
    },

    setNotificationsList: function (_aNotifications) {
      oProperties.aNotifications = _aNotifications;
    },

    toJSON: function () {
      return {}
    },

    reset: function () {
      oProperties = new Props();
    }
  };
})();
export default NotificationsProps;
