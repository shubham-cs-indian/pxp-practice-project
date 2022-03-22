import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager';
import { view as TabLayoutView } from '../../../../../viewlibraries/tablayoutview/tab-layout-view';
import { view as ContentInformationSidebarView } from './content-information-sidebar-view';
import { view as ContentLinkedSectionsView } from './content-linked-sections-view';
import ContentScreenConstants from '../store/model/content-screen-constants';
import { view as AcrolinxSidebarView } from '../../../../../viewlibraries/acrolinxsidebarview/acrolinx-sidebar-view';
import TabsDictionary from '../../../../../commonmodule/tack/template-tabs-dictionary';

const oEvents = {
  HANDLE_CONTENT_TAB_CLICKED: 'handle_content_tab_clicked',
  CONTENT_EDIT_VIEW_ACROLINX_CHECK_RESULT: "content_edit_view_acrolinx_check_result"
};

const oPropTypes = {
  linkItemsData: ReactPropTypes.array,
  contentInfoData: ReactPropTypes.object,
  tabData: ReactPropTypes.shape({
    tabsList: ReactPropTypes.arrayOf(
        ReactPropTypes.shape({
          id: ReactPropTypes.string,
          label: ReactPropTypes.string
        })
    ),
    activeTabId: ReactPropTypes.string
  }),
  sectionComponents: ReactPropTypes.array,
  selectedFilterOption: ReactPropTypes.string,
  selectedImageId: ReactPropTypes.string,
  isDefaultIconVisible: ReactPropTypes.bool,
  isAcrolinxSidebarVisible: ReactPropTypes.bool,
};

// @CS.SafeComponent
class ContentEditView extends React.Component {

  constructor (props) {
    super(props);

    this.handleTabClicked = this.handleTabClicked.bind(this);
    this.getContentInformationSidebarView = this.getContentInformationSidebarView.bind(this);
    this.getContentDetailView = this.getContentDetailView.bind(this);
  };

  handleTabClicked =(sTabId)=> {
    EventBus.dispatch(oEvents.HANDLE_CONTENT_TAB_CLICKED, sTabId);
  };

  handleAcrolinxCheckResult =(sScoreValue, sScoreCardUrl)=> {
    EventBus.dispatch(oEvents.CONTENT_EDIT_VIEW_ACROLINX_CHECK_RESULT, sScoreValue, sScoreCardUrl);
  };

  getCustomView () {
    let sActiveTabId = this.props.tabData.activeTabId;
    if (sActiveTabId !== TabsDictionary.TIMELINE_TAB) {
      return (
        this.props.customView
     )
    } else {
      return (this.props.customView)
    }
  }

  getContentInformationSidebarView () {
    let __props = this.props;
    let bCanDownload = __props.contentInfoData.referencedPermissions.globalPermission.canDownload;
    let oFunctionalPermission = __props.contentInfoData.referencedPermissions.functionPermission;
    let bCanShare = oFunctionalPermission && oFunctionalPermission.canShare;
    var oHeaderPermissions = __props.contentInfoData.referencedPermissions.headerPermission;
    return (
        <ContentInformationSidebarView
            contentInfoData={__props.contentInfoData}
            headerPermission={oHeaderPermissions}
            selectedImageId={this.props.selectedImageId}
            isDefaultIconVisible={this.props.isDefaultIconVisible}
            isDownloadIconVisible={bCanDownload}
            isShareIconVisible={bCanShare}
        />
    );
  };

  getContentDetailView () {
    if (this.props.customView) {
      let sActiveTabId = this.props.tabData.activeTabId || "";
      return (
          <div className={"customViewContainer " + sActiveTabId}>
            {this.getCustomView()}
          </div>);
    }

    let aLinkItemsData = this.props.linkItemsData;
    let aSectionComponent = this.props.sectionComponents;
    let oTabItemConstants = ContentScreenConstants.tabItems;
    let oTabsData = this.props.tabData;
    let bShowExpandedMenuList = !(oTabsData.activeTabId === oTabItemConstants.TAB_OVERVIEW);
    let oContentInfoData = this.props.contentInfoData;

    if(!CS.isEmpty(aLinkItemsData)) {
      return (
          <ContentLinkedSectionsView
              linkItemsData={aLinkItemsData}
              selectedElementInformation={this.props.selectedElementInformation}
              sectionComponents={aSectionComponent}
              isHelperLanguageSelected={oContentInfoData.isHelperLanguageSelected}
              helperLanguages={oContentInfoData.helperLanguages}
              showExpandedMenuList={bShowExpandedMenuList}
              sectionHeaderData={oContentInfoData.sectionHeaderData}
          />
      );
    } else {
      return (
          <div className="nothingToDisplayContainer">
            <div className="nothingToDisplay">{getTranslation().NOTHING_TO_DISPLAY}</div>
          </div>
      );
    }

  };

  render () {

    let __props = this.props;
    let oTabsData = __props.tabData;
    let sLinkedContentAndPanelWrapperClass = "linkedContentsAndPanelWrapper ";
    let oContentInformationSidebarView = this.getContentInformationSidebarView();
    let oContentDetailView = this.getContentDetailView();
    return (
        <div className="contentEditViewContainer">
          <div className={sLinkedContentAndPanelWrapperClass}>
            <div className="contentDetailsWrapper">
              {oContentInformationSidebarView}
              <div className="contentDetailViewContainer">
                <TabLayoutView
                    key={__props.activeEntityId}
                    tabList={oTabsData.tabsList}
                    activeTabId={oTabsData.activeTabId}
                    addBorderToBody={false}
                    onChange={this.handleTabClicked}>
                  {oContentDetailView}
                </TabLayoutView>
              </div>
              <AcrolinxSidebarView isVisible={this.props.isAcrolinxSidebarVisible} onCheckHandler={this.handleAcrolinxCheckResult}/>
            </div>
          </div>
        </div>
    );
  };
}

ContentEditView.propTypes = oPropTypes;


export const view = ContentEditView;
export const events = oEvents;
