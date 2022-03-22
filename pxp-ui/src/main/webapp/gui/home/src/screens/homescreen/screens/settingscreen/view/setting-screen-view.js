import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { view as ExpandableNestedMenuListItemView } from '../../../../../viewlibraries/expandablenestedmenulistview/expandable-nested-menu-list-view';
import { view as CustomSearchBarPopperView } from '../../../../../viewlibraries/SearchBarView/custom-searchbar-popper-view';
import ViewUtils from './utils/view-utils';

const oEvents = {
  SETTING_SCREEN_LEFT_NAVIGATION_TREE_ITEM_CLICKED: "setting_screen_left_navigation_tree_item_clicked",
  HANDLE_CONFIG_TAB_CLICKED: "handle_config_tab_clicked",
};

const oPropTypes = {
  selectedTabId: ReactPropTypes.string,
  tabItemsList: ReactPropTypes.array,
  leftSectionData: ReactPropTypes.object,
  rightSectionView: ReactPropTypes.object,
  activeScreenName: ReactPropTypes.string,
  breadcrumbView: ReactPropTypes.object,
  searchBarDialogOpen: ReactPropTypes.bool,
  searchListItems: ReactPropTypes.array
};

// @CS.SafeComponent
class SettingScreenView extends React.Component {

  static propTypes = oPropTypes;

  constructor (props) {
    super(props);
  }

  handleTabClicked = (sTabId) => {
    EventBus.dispatch(oEvents.HANDLE_CONFIG_TAB_CLICKED, sTabId);
  };

  listItemClicked = (sItemId, sParentId) => {
    EventBus.dispatch(oEvents.SETTING_SCREEN_LEFT_NAVIGATION_TREE_ITEM_CLICKED, sItemId, sParentId);
  };

  getExpandableMenuListItemView = (aItemData, sSelectedTabId, fOnClickHandler, bIsNested, oItemTreeValueMap, sClassName) => {
    return (
      <div className={sClassName}>
        <ExpandableNestedMenuListItemView
          linkItemsData={aItemData}
          selectedItemId={sSelectedTabId}
          onItemClick={fOnClickHandler}
          selectDefaultItem={fOnClickHandler}
          isNested={bIsNested}
          itemsValuesMap={oItemTreeValueMap}
        />
      </div>
    );
  };

  getTabBodyView = () => {
    let oLeftSectionData = this.props.leftSectionData;
    let oRightSectionView = this.props.rightSectionView;
    let fOnListItemClick = this.listItemClicked.bind(this);

    let sRightSectionClassName = "rightSectionContainer";
    let bIsGridViewScreen = ViewUtils.isGridViewScreen(this.props.activeScreenName);
    sRightSectionClassName += bIsGridViewScreen ? " gridViewInside " : "";

    let oTabView = this.getExpandableMenuListItemView(this.props.tabItemsList, this.props.selectedTabId, this.handleTabClicked, false, {}, "linksContainer");
    let oLeftSectionView = this.getExpandableMenuListItemView(oLeftSectionData.linkItemsData, oLeftSectionData.selectedItemId, fOnListItemClick,
      oLeftSectionData.isNested, oLeftSectionData.leftNavigationTreeValuesMap, "leftSectionContainer");

    return (
      <div className="settingScreenTabBodyViewWrapper">
        {oTabView}
        {oLeftSectionView}
        <div className={sRightSectionClassName}>
          {this.props.breadcrumbView}
          <div className="rightSectionBodyViewWrapper">
            <div className="rightSectionBodyView">
              {oRightSectionView}
            </div>
          </div>
        </div>
      </div>
    );
  };

  getConfigView = () => {
    let oTabBodyView = this.getTabBodyView();
    let oSearchDOM = this.getCustomSearchBarView();

    return (
     <div className="settingScreenBodyViewContainer">
       {oTabBodyView}
       {oSearchDOM}
     </div>
   );
  };

  getCustomSearchBarView = function () {
    let oCustomSearchBarPopperView = <CustomSearchBarPopperView
          items={this.props.searchListItems}
          bDisableAutoFocus={true}
          themeLoaderConfigure={true}
    />;

    return oCustomSearchBarPopperView;
  };

  render () {
    return (
        <div className="settingScreenViewContainer">
          {this.getConfigView()}
        </div>
    );
  }
}

export const view = SettingScreenView;
export const events = oEvents;
