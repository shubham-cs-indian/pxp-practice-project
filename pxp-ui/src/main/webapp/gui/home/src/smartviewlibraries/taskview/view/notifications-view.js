import CS from '../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../commonmodule/store/helper/translation-manager.js';
import TooltipView from './../../../viewlibraries/tooltipview/tooltip-view';
import { view as ImageSimpleView } from '../../../viewlibraries/imagesimpleview/image-simple-view';
import { view as DisconnectedHTMLView } from '../../../viewlibraries/disconnectedhtmlview/disconnected-html-view.js';
import CommonUtils from '../../../commonmodule/util/common-utils';
import ContentUtils from '../../../screens/homescreen/screens/contentscreen/view/utils/view-utils';
import NotificationDictionary from '../../../commonmodule/tack/notifications-dictionary';
import NotificationAction from '../../../commonmodule/tack/notification-action';
import TaskDictionary from '../../../commonmodule/tack/task-dictionary';

var oEvents = {
  NOTIFICATION_CLICKED: "handle_notification_clicked",
  NOTIFICATIONS_LOAD_MORE_CLICKED: "handle_notifications_load_more_clicked",
  NOTIFICATIONS_CLEAR_NOTIFICATION_CLICKED: "handle_clear_notification_clicked",
  NOTIFICATIONS_CLEAR_ALL_NOTIFICATIONS_CLICKED: "handle_clear_all_notifications_clicked",
  NOTIFICATIONS_REFRESH_CLICKED: "handle_notifications_refresh_clicked"
};

const oPropTypes = {
  notifications: ReactPropTypes.array,
  showLoadMore: ReactPropTypes.bool,
  isRefreshing: ReactPropTypes.bool
};
/**
 * @class NotificationsView
 * @description - To show task notifications to users.
 * @memberOf Views
 * @property {array} [notifications] - Notifications data.
 * @property {bool} [showLoadMore] - To show load more option.
 * @property {bool} [isRefreshing] - Deprecated
 */

// @CS.SafeComponent
class NotificationsView extends React.Component {
  static propTypes = oPropTypes;

  handleLoadMoreNotificationsClicked = () => {
    EventBus.dispatch(oEvents.NOTIFICATIONS_LOAD_MORE_CLICKED);
  };

  handleClearNotification = (sId, oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    EventBus.dispatch(oEvents.NOTIFICATIONS_CLEAR_NOTIFICATION_CLICKED, sId);
  };

  handleClearAllNotificationsClicked = (oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    EventBus.dispatch(oEvents.NOTIFICATIONS_CLEAR_ALL_NOTIFICATIONS_CLICKED);
  };

  handleRefreshNotificationsClicked = () => {
    EventBus.dispatch(oEvents.NOTIFICATIONS_REFRESH_CLICKED);
  };

  handleNotificationClicked = (oNotification, oEvent) => {
    if (!oEvent.nativeEvent.dontRaise) {
      EventBus.dispatch(oEvents.NOTIFICATION_CLICKED, oNotification.id);
    }
  };

  getUserIconView = (sIconKey) => {
    if (sIconKey) {
      var sIconUrl = ContentUtils.getIconUrl(sIconKey);
      return (<div className="userIcon"><ImageSimpleView classLabel="image" imageSrc={sIconUrl}/></div>);
    } else {
      return (<div className="defaultUserIcon"></div>);
    }
  };

  getNotificationIconClassName = (oNotification) => {
    var sAction = oNotification.action;
    var sClassName = "notificationIcon ";

    switch (sAction) {
      case NotificationAction.comment:
      case NotificationAction.commentWithAttachment:
        sClassName += "comment";
        break;

      case NotificationAction.attachmentUploaded:
      case NotificationAction.attachmentDeleted:
        sClassName += "attachment";
        break;

      case NotificationAction.usersAdded:
      case NotificationAction.assignedYou:
        sClassName += "userAdded";
        break;

      case NotificationAction.usersDeleted:
      case NotificationAction.removedYou:
        sClassName += "userRemoved";
        break;

      case NotificationAction.dueDateChanged:
      case NotificationAction.startDateChanged:
      case NotificationAction.overDueDateChanged:
        sClassName += "dateChanged";
        break;

      case NotificationAction.statusChanged:
        sClassName += "statusChanged";
        break;

      case NotificationAction.priorityChanged:
        sClassName += "priorityChanged";
        break;

      default:
        sClassName += "task";
    }

    return sClassName;
  };

  getCommentMessageDOM = (oNotification) => {
    if ((!CS.isEmpty(oNotification.description) && (oNotification.action === NotificationAction.comment))) {
      return (
          <div className="descriptionOfNotification">
            <DisconnectedHTMLView
                className="description"
                content={oNotification.description}
                dataId={oNotification.id}
            />
          </div>
      );
    } else {
      return null;
    }
  };

  /** Function to get time format **/
  getTimeInHHMM = (iTimeStamp) => {
    let sDate = new Date(iTimeStamp);
    let HH = sDate.getHours();
    let MM = sDate.getMinutes();
    let SS = sDate.getSeconds();
    if (MM < 10) {
      MM = "0" + MM;
    }
    if (SS < 10) {
      SS = "0" + SS;
    }
    return HH + ":" + MM + ":" + SS;
  }

  /** Function to get Date with time **/
  getDateWithTime = (iTimeStamp) => {
    let date = new Date(iTimeStamp);
    if(!CS.isDate(date)) {
      return '';
    }
    let sDate = date.toLocaleDateString() + "";
    let aDate = sDate.split("/");
    if(aDate[0] < 10){
      aDate[0] = "0" + aDate[0];
    }
    if(aDate[1] < 10){
      aDate[1] = "0" + aDate[1];
    }
    let sNewDate = aDate[1] + "/" + aDate[0] + "/" + aDate[2];
    let sTime = this.getTimeInHHMM(iTimeStamp);
    return sNewDate + " " + sTime;
  };

  getSingleNotificationView = (oNotification) => {
    var oNotifiedBy = oNotification.notifiedBy;
    var oUserIcon = null;
    if(oNotifiedBy){
      oUserIcon = this.getUserIconView(oNotifiedBy.icon)
    }
    else {
      oUserIcon = <div className="dueDateIcon"></div>;
    }
    var oCommentDOM = this.getCommentMessageDOM(oNotification);
    var sNotificationClassName = (oNotification.status === "read") ? "notificationNode read " : "notificationNode ";
    var sNotificationIconClassName = this.getNotificationIconClassName(oNotification);
    let sNotificationDateWithTime = this.getDateWithTime(oNotification.createdOn);
    return (
        <div className={sNotificationClassName} onClick={this.handleNotificationClicked.bind(this, oNotification)}>
          <div className="userIconContainer">{oUserIcon}</div>
          <div className="middleSection">
            {oCommentDOM}
            <div className="notificationTime">
              <div className={sNotificationIconClassName}></div>
              <div className="time">{sNotificationDateWithTime}</div>
            </div>
          </div>
          <TooltipView label={getTranslation().CLEAR} placement="top">
            <div className="clearNotification"
                 onClick={this.handleClearNotification.bind(this, oNotification.id)}></div>
          </TooltipView>
        </div>
    );
  };

  getNotificationNotFoundDOM = () => {
    return (
        <div className="nothingFound">
          <div className="notificationNotFound">{getTranslation().YOU_DO_NOT_HAVE_ANY_NOTIFICATION}</div>
        </div>);
  };

  getNotificationsView = () => {
    var _this = this;
    var aNotificationsGroups = this.props.notifications;
    var aGroupView = [];

    if (CS.isEmpty(aNotificationsGroups)) {
      return this.getNotificationNotFoundDOM();
    }

    var iIndex = 0;
    CS.forEach(aNotificationsGroups, function (oData) {
      var aNotificationGroupView = [];
      if (!CS.isEmpty(oData)) {
        var aNotifications = oData.notifications;

        aNotificationGroupView.push(<div className="groupHeader" key={"groupHeader_" + iIndex++}>{oData.header}</div>);

        var aNotificationListView = [];
        CS.forEach(aNotifications, function (oNotification) {
          aNotificationListView.push(_this.getSingleNotificationView(oNotification));
        });

        aNotificationGroupView.push(<div className="notificationsList">{aNotificationListView}</div>);

        aGroupView.push(
            <div className="groupSection">
              {aNotificationGroupView}
            </div>
        )
      }
    });

    return aGroupView;
  };

  getLoadMoreView = () => {
    var bShowLoadMore = this.props.showLoadMore;
    var fOnClickLoadMore = this.handleLoadMoreNotificationsClicked;

    return (bShowLoadMore) ?
           (<div className="loadMoreNotificationsContainer" onClick={fOnClickLoadMore}>
             <TooltipView label={getTranslation().LOAD_MORE} placement="top">
               <div className="loadMoreNotifications">{getTranslation().LOAD_MORE}</div>
             </TooltipView>
           </div>) : null;
  };

  getNotificationsToolbarView = () => {
    var aNotifications = this.props.notifications;
    var oClearAllDOM = (aNotifications.length) ?
                       (<TooltipView label={getTranslation().CLEAR_ALL} placement="bottom">
                         <div className="clearAll" onClick={this.handleClearAllNotificationsClicked}>{getTranslation().CLEAR_ALL}</div>
                       </TooltipView>) : null;

    var oRefreshDOM = <div className="refresh" onClick={this.handleRefreshNotificationsClicked}></div>;

    return (
        <div className="notificationsToolbar">
          <div className="notificationTitle">{getTranslation().NOTIFICATIONS}</div>
          {oRefreshDOM}
          {oClearAllDOM}
        </div>
    )
  };

  render() {
    var oToolbarView = this.getNotificationsToolbarView();
    var oNotificationsView = this.getNotificationsView();
    var oLoadMoreView = this.getLoadMoreView();

    return (
        <div className="notificationViewContainer">
          {oToolbarView}
          <div className="notificationsView">
            {oNotificationsView}
            {oLoadMoreView}
          </div>
        </div>
    );
  }
}

export const view = NotificationsView;
export const events = oEvents;
