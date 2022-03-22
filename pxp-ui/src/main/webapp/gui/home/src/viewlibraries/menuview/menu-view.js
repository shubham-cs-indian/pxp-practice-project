import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import LogFactory from '../../libraries/logger/log-factory';
import ExceptionLogger from '../../libraries/exceptionhandling/exception-logger';
import TooltipView from './../../viewlibraries/tooltipview/tooltip-view';
import { view as UserProfileActionView } from '../userprofileview/user-profile-action-view';
import MenuViewModel from './model/menu-view-model';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';

const logger = LogFactory.getLogger('menu-view');

const oEvents = {
  MENU_SELECTION_CHANGED: "menu_selection_change_event",
};

const oPropTypes = {
  menuModelList: ReactPropTypes.arrayOf(ReactPropTypes.instanceOf(MenuViewModel)).isRequired,
  currentUser: ReactPropTypes.object,
  disableMenuItems: ReactPropTypes.bool,
  changePasswordEnabled: ReactPropTypes.bool,
  userProfileData: ReactPropTypes.object,
};
/**
 * @class MenuView - not in use.
 * @memberOf Views
 * @property {array} menuModelList -  array of menuModeList.
 * @property {object} [currentUser] -  object of currentUser.
 * @property {bool} [disableMenuItems] -  boolean value for disableMenuItems true or not.
 * @property {bool} [changePasswordEnabled] -  boolean value for changePasswordEnabled true or not.
 * @property {object} [userProfileData] -  object of userProfileData.
 */

let oRawTheme = {
  zIndex: {
    layer: 1500,
  },
};

// @CS.SafeComponent
class MenuView extends React.Component {

  constructor(props) {
    super(props);
  }

  handleMenuItemClicked =(oModel, oEvent)=> {
      logger.info('handleMenuItemClicked: dispatch with menu object', {'oModel': oModel});
      EventBus.dispatch(oEvents.MENU_SELECTION_CHANGED, this, oEvent, oModel);
  }

  getHtmlElementsOfMenuItem =(that, oModel, index)=> {
    var sAppliedClass = oModel.className;
    if(CS.isEmpty(sAppliedClass)){
      ExceptionLogger.error(`Empty class name for ${oModel.id} `);
      return null;
    }
    if (oModel.isSelected) {
      sAppliedClass = sAppliedClass + " selectedMenuItem";
    }
    var oMenuClick = null;
    if (this.props.disableMenuItems) {
      sAppliedClass += " disabled";
    } else {
      oMenuClick = that.handleMenuItemClicked.bind(that, oModel);
    }
    return (
        <TooltipView placement="bottom" key={"menu" + index} label={getTranslation()[oModel.title] || ''}>
          <div className={sAppliedClass}
               onClick={oMenuClick}
               data-name={oModel.id}>
          </div>
        </TooltipView>
    );
  }

  getAllMenuItems =()=> {
    var aMenuModels = this.props.menuModelList;
    var that = this;
    return CS.map(aMenuModels, function (oModel, index) {
      let oMenuDOM = that.getHtmlElementsOfMenuItem(that, oModel, index);
      if (!CS.isEmpty(oMenuDOM)) {
        return oMenuDOM;
      }
    });

  }

  render() {
    let aAllMenuItem = this.getAllMenuItems();
    return (
        <div className="menuViewContainer">
          {aAllMenuItem}
          <UserProfileActionView userProfileData={this.props.userProfileData}/>
        </div>
    );
  }
}

MenuView.propTypes = oPropTypes;

export const view = MenuView;
export const events = oEvents;
