import alertify from '../../../../commonmodule/store/custom-alertify-store';
import CS from '../../../../libraries/cs';
import MicroEvent from '../../../../libraries/microevent/MicroEvent.js';
import { NotificationRequestMapping } from './../../../../viewlibraries/tack/view-library-request-mapping';
import { getTranslations as getTranslation } from '../../../../commonmodule/store/helper/translation-manager.js';
import CommonUtils from '../../../../commonmodule/util/common-utils';
import NotificationProps from './../model/notifications-props';
import NotificationAction from '../../../../commonmodule/tack/notification-action';
import { communicator as HomeScreenCommunicator } from '../../../../screens/homescreen/store/home-screen-communicator';
import BreadCrumbStore from '../../../../commonmodule/store/helper/breadcrumb-store';
import ViewLibraryUtils from '../../../../viewlibraries/utils/view-library-utils';

var NotificationsStore = (function () {

  var _triggerChange = function () {
    NotificationsStore.trigger('notification-change');
  };

  var _preProcessNotificationsList = function (aNotificationList) {
    var aUsersList = NotificationProps.getUsersList();
    var oRoleMap = NotificationProps.getRoleDetails();

    CS.forEach(aNotificationList, function (oNotification) {
      var sActedBy = oNotification.actedBy;
      var oEntity = oNotification.entityInfo;
      var oUser = CS.find(aUsersList, {id: sActedBy});
      var oDate = new Date(oNotification.createdOn);
      var sDescription = oNotification.description;
      var aRoleIds = CS.split(sDescription, ",");
      var aRoleLabels = [];

      CS.forEach(aRoleIds, function (sRoleId) {
        var sLabel = oRoleMap[sRoleId];
        aRoleLabels.push(sLabel);
      });

      var sSubtaskId = oEntity.subEntityId;
      oNotification.isForSubtask = !!sSubtaskId;

      //To group notifications by day
      oNotification.daystamp = oDate.setHours(0, 0, 0, 0);
      oNotification.notifiedBy = oUser;
      oNotification.time = CommonUtils.getDateAttributeInTimeFormat(oNotification.createdOn);

      if(oNotification.action === NotificationAction.usersAdded || oNotification.action === NotificationAction.usersDeleted) {
        oNotification.roleName = oRoleMap[sDescription];
      } else {
        oNotification.roleName = CS.join(aRoleLabels, ", ");
      }

      if(oNotification.action === NotificationAction.statusChanged || oNotification.action === NotificationAction.priorityChanged) {
        oNotification.newTagValue = _getUpdatedTagValue(oNotification);
      }
    });
  };

  var _getUpdatedTagValue = function (oNotification) {
    var aTasksList = NotificationProps.getTasksDetails();
    var oReferencedTasks = NotificationProps.getReferencedTasks();
    var oReferencedTags = NotificationProps.getReferencedTags();
    var oEntityInfo = oNotification.entityInfo;
    var oTask = CS.find(aTasksList, {id: oEntityInfo.id});
    var oMasterTask = oReferencedTasks[oTask.type];
    var sAction = oNotification.action;
    var oTag = null;

    switch (sAction) {
      case NotificationAction.statusChanged:
        var sStatusTag = oMasterTask.statusTag;
        oTag = oReferencedTags[sStatusTag];
        break;

      case NotificationAction.priorityChanged:
        var sPriorityTag = oMasterTask.priorityTag;
        oTag = oReferencedTags[sPriorityTag];
        break;
    }

    var oTagValue = CS.find(oTag.children, {id: oNotification.description});

    if(oTagValue) {
      return oTagValue.label;
    } else {
      return null;
    }
  };

  var _groupNotificationsByDate = function () {
    var aNotificationList = NotificationProps.getNotificationsList();
    var oNotificationsGroupByDate = CS.groupBy(aNotificationList, "daystamp");

    var oNotificationViewModel = {};
    CS.forEach(oNotificationsGroupByDate, function (value, sKey) {

      value = CS.sortBy(value, "createdOn");
      CS.reverse(value);

      oNotificationViewModel[sKey] = {
        "header": CommonUtils.getDateAttributeInTimeFormat(+sKey),
        "sortKey": sKey,
        "notifications": value
      }
    });

    var aNotificationViewModel = CS.reverse(CS.sortBy(oNotificationViewModel, function (oGroup) {
      return oGroup.sortKey;
    }));

    NotificationProps.setNotificationModel(aNotificationViewModel);
  };

  var failureFetchAllNotifications = function (oResponse) {
    CommonUtils.failureCallback(oResponse, 'failureFetchAllNotifications', getTranslation());
    _triggerChange();
    return false;
  };

  var successFetchAllNotifications = function (bIsLoadMore, oCallBack, oResponse) {
    var oSuccess = oResponse.success;
    var aNewlyLoadedNotifications = oSuccess.notifications;

    /* 1.Reset task list view.
     * 2.set task mode
     * 3.task levels before processing
     */
    oCallBack && oCallBack.functionToSetTask && oCallBack.functionToSetTask();

    NotificationProps.setUsersList(oSuccess.userInfoList);
    NotificationProps.setTaskDetails(oSuccess.taskInfo);
    NotificationProps.setRoleDetails(oSuccess.roleIdLabelMap);
    NotificationProps.setUnreadNotificationsCount(oSuccess.unreadNotificationsCount);
    NotificationProps.setReferencedTags(oSuccess.referencedTags);
    NotificationProps.setReferencedTasks(oSuccess.referencedTasks);
    var aNotificationsList = NotificationProps.getNotificationsList();

    //set or append notifications
    if(bIsLoadMore) {
      if (!CS.isEmpty(aNotificationsList)) {
        aNotificationsList = CS.concat(aNotificationsList, aNewlyLoadedNotifications);
      }
    } else {
      aNotificationsList = aNewlyLoadedNotifications;
    }
    _preProcessNotificationsList(aNotificationsList);

    var oPaginationData = NotificationProps.getPaginationDataForNotifications();
    oPaginationData.from = CS.size(aNotificationsList);
    NotificationProps.setTotalNotificationsCount(oSuccess.size);
    NotificationProps.setNotificationsList(aNotificationsList);
    _groupNotificationsByDate();
    NotificationProps.setRefreshingState(false);

    if(oCallBack && oCallBack.breadCrumbData) {
      BreadCrumbStore.addNewBreadCrumbItem(oCallBack.breadCrumbData, true);
    }
    if(oCallBack) {
      oCallBack.functionToExecute && oCallBack.functionToExecute();
    }
    HomeScreenCommunicator.disablePhysicalCatalog(false);
    _triggerChange();
    return true;
  };

  var _fetchNotifications = function (bIsLoadMore, oCallBack) {
    var oPaginationData = NotificationProps.getPaginationDataForNotifications();
    var oPostData = {
      from: oPaginationData.from,
      size: oPaginationData.size
    };

    let oAjaxExtraData = {};
    if (oCallBack && oCallBack.breadCrumbData) {
      let oBreadcrumbData = oCallBack.breadCrumbData;
      oAjaxExtraData = oBreadcrumbData.extraData;
      /* oCallBack.breadCrumbFunction = function (oCallbackData) {
       ContentUtils.getBreadCrumbCallback(oCallbackData)();
       };*/
      oBreadcrumbData.payloadData = [oCallBack, oAjaxExtraData, bIsLoadMore];
      oBreadcrumbData.functionToSet = _fetchNotificationsCall;
    }
    oAjaxExtraData.postData = oPostData;
    oAjaxExtraData.requestData = {};
    oAjaxExtraData.url = NotificationRequestMapping.GetAllNotifications;
    _fetchNotificationsCall(oCallBack, oAjaxExtraData, bIsLoadMore);
  };

  let _fetchNotificationsCall = function (oCallBack, oAjaxExtraData, bIsLoadMore) {
    return CS.postRequest(oAjaxExtraData.url, {}, oAjaxExtraData.postData, successFetchAllNotifications.bind(this, bIsLoadMore, oCallBack), failureFetchAllNotifications);
  };

  var successChangeNotificationStatus = function (oResponse) {
    var oSuccess = oResponse.success;
    var aReadNotificationIds = oSuccess.ids;
    var aNotificationsList = NotificationProps.getNotificationsList();
    var aUnreadNotificationsCount = NotificationProps.getUnreadNotificationsCount();
    aUnreadNotificationsCount = aUnreadNotificationsCount - aReadNotificationIds.length;
    NotificationProps.setUnreadNotificationsCount(aUnreadNotificationsCount);

    CS.forEach(aReadNotificationIds, function (sId) {
      var oReadNotification = CS.find(aNotificationsList, {id: sId});
      oReadNotification.status = "read";
    });
    _groupNotificationsByDate();
    _triggerChange();
  };

  var failureChangeNotificationStatus = function (oResponse) {
    CommonUtils.failureCallback(oResponse, 'failureChangeNotificationStatus', getTranslation());
  };

  var _handleNotificationClicked = function (sNotificationId) {
    CS.postRequest(NotificationRequestMapping.ChangeStatus, {}, {ids: [sNotificationId]}, successChangeNotificationStatus, failureChangeNotificationStatus);
  };

  var _handleNotificationLoadMoreClicked = function () {
    var oPaginationData = NotificationProps.getPaginationDataForNotifications();
    var aNotifications = NotificationProps.getNotificationsList();
    oPaginationData.from = CS.size(aNotifications);
    _fetchNotifications(true, {});
  };

  var successClearNotificationsClicked = function ( sDeletedId, oResponse) {
    var aNotificationsList = NotificationProps.getNotificationsList();
    var oClearedNotification = CS.find(aNotificationsList, {id: sDeletedId});
    if(oClearedNotification.status === "unread") {
      var iUnreadNotificationsCount = NotificationProps.getUnreadNotificationsCount();
      NotificationProps.setUnreadNotificationsCount(--iUnreadNotificationsCount);
    }
    CS.remove(aNotificationsList, {id: sDeletedId});
    _groupNotificationsByDate();
    _triggerChange();
  };

  var failureClearNotificationsClicked = function (oResponse) {
    CommonUtils.failureCallback(oResponse, 'failureClearNotificationsClicked', getTranslation());
    _triggerChange();
  };

  var _handleClearNotificationsClicked = function (sId) {
    if (CS.isEmpty(sId)) {
      return;
    }
    CS.deleteRequest(NotificationRequestMapping.ClearNotification, {}, {id: sId}, successClearNotificationsClicked.bind(this,sId), failureClearNotificationsClicked);
  };

  var successClearAllNotificationsClicked = function (oResponse) {
    NotificationProps.setNotificationsList([]);
    alertify.success( ViewLibraryUtils.getDecodedTranslation(getTranslation().ENTITY_SUCCESSFULLY_DELETED , { entity : getTranslation().NOTIFICATIONS }));
    _fetchNotifications(false,{});
    _triggerChange();
  };

  var failureClearAllNotificationsClicked = function (oResponse) {
    CommonUtils.failureCallback(oResponse, 'failureClearNotificationsClicked', getTranslation());
    _triggerChange();
  };

  var _handleClearAllNotificationsClicked = function () {
    CS.deleteRequest(NotificationRequestMapping.ClearAllNotification, {}, {}, successClearAllNotificationsClicked, failureClearAllNotificationsClicked);
  };

  return {
    fetchNotifications: function (oCallBack) {
      var oPaginationData = NotificationProps.getPaginationDataForNotifications();
      oPaginationData.from = 0;
      return _fetchNotifications(false, oCallBack);
    },

    handleNotificationClicked: function (sNotificationId) {
      _handleNotificationClicked(sNotificationId);
    },

    handleNotificationLoadMoreClicked: function () {
      _handleNotificationLoadMoreClicked();
    },

    handleClearNotificationsClicked: function (sId) {
      _handleClearNotificationsClicked(sId);
    },

    handleClearAllNotificationsClicked: function () {
      _handleClearAllNotificationsClicked();
    }
  }
})();

MicroEvent.mixin(NotificationsStore);

export default NotificationsStore;