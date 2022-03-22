import CS from '../../libraries/cs';
import React, { Fragment } from 'react';
import ReactPropTypes from 'prop-types';
import GitInfo from 'react-git-info/macro';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import LogFactory from '../../libraries/logger/log-factory';
import { isDevMode } from "../../libraries/crautils/cra-utils";
import { view as LogoView } from './../../viewlibraries/logoview/logo-view';
import { view as MenuViewNew } from './header-menu-item-view';
import { view as CustomDialogView } from '../../viewlibraries/customdialogview/custom-dialog-view';
import { view as ImageSimpleView } from '../imagesimpleview/image-simple-view';
import { getTranslations as oTranslations } from './../../commonmodule/store/helper/translation-manager.js';

const logger = LogFactory.getLogger('menu-view');
const oEvents = {
  ABOUT_BUTTON_DIALOG_ABOUT_DIALOG_CLOSED: "about_button_dialog_about_dialog_closed",
  HEADER_MENU_VIEW_REFRESH_NOTIFICATION: "header_menu_view_refresh_notification"
};

const oPropTypes = {
  aboutDialogVisible: ReactPropTypes.bool,
  mode: ReactPropTypes.string,
  onClickFun: ReactPropTypes.func,
  oMenuData: ReactPropTypes.object,
};
/**
 * @class HeaderMenuView - use for display Header to the Application.
 * @memberOf Views
 * @property {bool} [aboutDialogVisible] -   boolean value for aboutDialogVisible or not.
 * @property {string} [mode]  -   string of mode like it is Runtime or settings etc.
 * @property {func} [onClickFun] -  function which used onClickFun event.
 * @property {object} [oMenuData]  -  object which contain oMenuData.
 */

let oRawTheme = {
  zIndex: {
    layer: 1500,
  },
};

const gitInfo = GitInfo();

// @CS.SafeComponent
class HeaderMenuView extends React.Component {

  static propTypes = oPropTypes;

  constructor (props) {
    super(props);

    EventBus.dispatch(oEvents.HEADER_MENU_VIEW_REFRESH_NOTIFICATION);
    this.interval = setInterval(() => {
      // TODO: #Refact20
      // let bIsDebugModeEnabled = this.getQueryVariable('debugMode');
      if(!isDevMode()){
        EventBus.dispatch(oEvents.HEADER_MENU_VIEW_REFRESH_NOTIFICATION);
      }
    }, 10000)
  }

  componentWillUnmount () {
    clearInterval(this.interval);
  }

   getQueryVariable = function (variable) {
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
      var pair = vars[i].split("=");
      if (pair[0] == variable) {
        return pair[1];
      }
    }
  };

  handleMenuItemClicked = (oModel, oEvent) => {
    logger.info('handleMenuItemClicked: dispatch with menu object', {'oModel': oModel});
    EventBus.dispatch(oEvents.MENU_SELECTION_CHANGED, this, oEvent, oModel);
  };

  getMenuItemView (oMenuModel, iIndex) {
    return (
        <MenuViewNew oMenuItem={oMenuModel} key={iIndex}/>
    );
  }

  closeCollectionsDialog = () => {
    EventBus.dispatch(oEvents.ABOUT_BUTTON_DIALOG_ABOUT_DIALOG_CLOSED, this);
  };

  createDefaultLogo = () => {
    return(
      <div className="aboutDialogLogo default-logo"></div>
    );
  };

  createCustomBrandLogo = () => {
    return(
      <div className="aboutDialogLogo">
        <ImageSimpleView classLabel="img" imageSrc={this.props.applicationBrandLogo} />
      </div>
    );
  };

  getAboutDialog = () => {
    let sProjectName = process.env.REACT_APP_PXP_NAME;
    let sProjectVersion = process.env.REACT_APP_PXP_VERSION;
    let sGitVersion = gitInfo.commit.hash;
    let sGitBranch = gitInfo.branch;
    let bShowGitVersion = process.env.REACT_APP_SHOW_GIT_VERSION;
    let bShowGitBranch = process.env.REACT_APP_SHOW_GIT_BRANCH;

    let oGitBranchView = bShowGitBranch ? (<div className="aboutDialogInfoStrip">
      <div className="aboutDialogInfoKey">{oTranslations().GIT_BRANCH + ":"}</div>
      <div className="aboutDialogInfoVal">{sGitBranch}</div>
    </div>) : null;

    let oGitVersionView = bShowGitVersion ? (<div className="aboutDialogInfoStrip">
      <div className="aboutDialogInfoKey">{oTranslations().GIT_VERSION + ":"}</div>
      <div className="aboutDialogInfoVal">{sGitVersion}</div>
    </div>) : null;

    let oStyle = {
      padding: "0px"
    };

    let oLogoToDisplay = this.props.applicationBrandLogo ? this.createCustomBrandLogo() : this.createDefaultLogo();

    return (
        <CustomDialogView modal={false}
                          open={true}
                          applicationBrandLogo={this.props.applicationBrandLogo}
                          className={'aboutDialogView'}
                          bodyClassName="aboutDialogViewBody"
                          contentClassName={"aboutDialogViewContent"}
                          bodyStyle={oStyle}
                          contentStyle={{maxWidth: "550px", padding: "0px", width: "75%"}}
                          onRequestClose={this.closeCollectionsDialog}>
          <div className="aboutDialogWrapper">
            <div className="aboutDialogLogoNameContainer">
              {oLogoToDisplay}
              <div className="aboutDialogNameVersion">
                {/*<div className="aboutDialogNameHeader">{sProjectName}</div>*/}
                <div className="aboutDialogVersion">
                  <div className="aboutDialogInfoVal">{sProjectVersion}</div>
                </div>
              </div>
            </div>
            <div className="aboutDialogOtherInfo">
              {oGitBranchView}
              {oGitVersionView}
            </div>
          </div>
        </CustomDialogView>
    );
  };

/*
  getAboutDialogView (oMenu) {
    return (
        <MenuViewNew oMenuItem={oMenu}/>
    );
  }
*/

  getAllMenuView () {
    let _this = this;
    let oMenuData = this.props.MenuData;
    let aAllMenuDom = [];

    CS.forEach(oMenuData, function (aMenu, iMenuIndex) {
      let aMenuDom = [];
      CS.forEach(aMenu, function (oMenu, iIndex) {
        if (oMenu.view) {
          aMenuDom.push(<Fragment key={iIndex}>{oMenu.view}</Fragment>)
        } else {
          aMenuDom.push(_this.getMenuItemView(oMenu, iIndex));
        }
      });
      if (!CS.isEmpty(aMenuDom)) {
        aAllMenuDom.push(<div className={'headerMenuSubContainer'} key={iMenuIndex}>{aMenuDom}</div>)
      }
    });
    return (
        aAllMenuDom
    );
  }

  render () {
    let aAllMenuDom = this.getAllMenuView();
    var oAboutDialog = this.props.aboutDialogVisible ? this.getAboutDialog() : null;

    return (
        <div className="newHeaderMenuContainer" onClick={this.props.onClickFun}>
          <LogoView mode={this.props.mode} applicationIcon={this.props.applicationIcon}/>
          <div className="newHeaderMenuRightSideContainer">
            {aAllMenuDom}
            {oAboutDialog}
          </div>
        </div>
    );
  }
}

HeaderMenuView.propTypes = oPropTypes;

export const view = HeaderMenuView;
export const events = oEvents;
