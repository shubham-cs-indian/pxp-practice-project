import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';

import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as ListView } from '../../../../../viewlibraries/listviewnew/list-view-new';
import DraggableListViewModel from '../../../../../viewlibraries/draggablelistview/model/draggable-list-view-model';
import ListViewModel from '../../../../../viewlibraries/listviewnew/model/list-view-new-model';
import { view as CommonConfigSectionView } from '../../../../../viewlibraries/commonconfigsectionview/common-config-section-view.js';
import { view as ConfigEntityCreationDialogView } from './config-entity-creation-dialog-view';
import { view as ConfigHeaderView } from '../../../../../viewlibraries/configheaderview/config-header-view';
import SectionLayout from '../tack/property-collection-layout-data';
import ManageEntityConfigProps from "../store/model/manage-entity-config-props";
import {view as ManageEntityDialogView} from "./entity-usage-dialog-view";
import {view as NothingFoundView} from "./../../../../../viewlibraries/nothingfoundview/nothing-found-view";
import {view as TabLayoutView} from "../../../../../viewlibraries/tablayoutview/tab-layout-view";
import {view as PropertyCollectionDraggableListView} from "./property-collection-draggable-list-view";

const oEvents = {
  HANDLE_PROPERTY_COLLECTION_IMPORT_BUTTON_CLICKED : "handle_property_collection_import_button_clicked",
  HANDLE_PROPERTY_COLLECTION_TAB_CHANGED : "handle_property_collection_tab_clicked"
};

const oPropTypes = {
  actionItemModel: ReactPropTypes.object.isRequired,
  classListModel: ReactPropTypes.arrayOf(ReactPropTypes.instanceOf(DraggableListViewModel).isRequired),
  availableActiveListModel: ReactPropTypes.arrayOf(ReactPropTypes.instanceOf(ListViewModel)).isRequired,
  contextMenuList: ReactPropTypes.object,
  isAttributeTableVisible: ReactPropTypes.bool.isRequired,
  selectedPropertiesList: ReactPropTypes.object,
  availableRoleListModel:ReactPropTypes.arrayOf(ReactPropTypes.instanceOf(ListViewModel)).isRequired,
  context: ReactPropTypes.string,
  bListItemCreated: ReactPropTypes.bool,
  activeItemModel: ReactPropTypes.object,
  propertyCollapsedStatusMap: ReactPropTypes.object,
  isPropertyCollectionDirty: ReactPropTypes.bool,
  selectedTabId: ReactPropTypes.string,
  propertyCollectionListTabData: ReactPropTypes.array,
  searchText: ReactPropTypes.string,
  propertyCollectionDraggableListTabsData: ReactPropTypes.object,
  propertyCollectionListSearchText: ReactPropTypes.string,
  showPropertyLoadMore: ReactPropTypes.bool
};

// @CS.SafeComponent
class PropertyCollectionConfigView extends React.Component {
  constructor (oProps) {
    super(oProps);

    this.iconUpload = React.createRef();
  }
  static propTypes = oPropTypes;

  componentDidMount() {
    this.setValues();
  }

  componentDidUpdate() {
    this.setValues();
  }

  setValues = () => {

    if(this.iconUpload.current) {
      this.iconUpload.current.value = '';
    }
  };

  handleFilesSelectedToUpload = (aFiles) => {
    EventBus.dispatch(oEvents.HANDLE_PROPERTY_COLLECTION_IMPORT_BUTTON_CLICKED, aFiles);
  };

  handleTabClicked = (sTabId) => {
    if (sTabId === this.props.selectedTabId) {
      return;
    }
    EventBus.dispatch(oEvents.HANDLE_PROPERTY_COLLECTION_TAB_CHANGED, sTabId);
  };

  getTabLayoutView = () => {
    let oTabData = this.props.propertyCollectionListTabData;

    return (
      <TabLayoutView
        tabList={oTabData.tabList}
        activeTabId={oTabData.selectedTabId}
        addBorderToBody={false}
        onChange={this.handleTabClicked}>
      </TabLayoutView>);
  };

  getListView = () => {
    var aClassListModel = this.props.classListModel;
    let oStyle = {height: "calc(100% - 95px)"};
    return <ListView
        model={aClassListModel}
        bListItemCreated={this.props.bListItemCreated}
        bEnableLoadMore={this.props.showLoadMore}
        context={"propertyCollection"}
        hideSearchBar={false}
        style={oStyle}
        customHeaderView={this.getTabLayoutView()}
        searchText={this.props.propertyCollectionListSearchText}
    />;
  };

  getPropertyCollectionDetailedView = () => {
    var __props = this.props;
    let oSectionLayout = new SectionLayout();
    var aPropertyCollectionDetailedView = [];
    var aDisabledFields = ["code"];
    aPropertyCollectionDetailedView.push(
        <CommonConfigSectionView context={__props.context} sectionLayout={oSectionLayout} data={__props.activeItemModel} disabledFields={aDisabledFields} key="1"/>);

    aPropertyCollectionDetailedView.push(
        <div className="classConfigLinkScrollElementWrapper" key="2">
          <div className="classSectionContainer">
            <div className="classElementListContainer">
              <PropertyCollectionDraggableListView
                  activeTabId={__props.propertyCollectionDraggableListTabsData.selectedTabId}
                  tabList={__props.propertyCollectionDraggableListTabsData.tabList}
                  leftListData={__props.availableActiveListModel}
                  rightListData={__props.selectedPropertiesList}
                  enableMultiDrag={true}
                  emptyMessage={this.props.emptyMessage}
                  searchText={this.props.searchText}
                  showPropertyLoadMore={this.props.showPropertyLoadMore}
              />
             </div>
          </div>
        </div>
    );
    return aPropertyCollectionDetailedView;
  };

  getCreatePropertyCollectionDialog = (oActiveClass, sEntityType) => {
    return (
        <ConfigEntityCreationDialogView
            activeEntity={oActiveClass}
            entityType={sEntityType}
        />
    )
  };
  getEntityDialog = () => {
    let oEntityDatList = ManageEntityConfigProps.getDataForDialog();
    let bIsDelete = ManageEntityConfigProps.getIsDelete();
    return (
        <ManageEntityDialogView
            oEntityDatList = {oEntityDatList}
            bIsDelete={bIsDelete}
        />
    );
  };

  getPropertyCollectionBody = (oConfigHeaderView) => {
    var aClassListModel = this.props.classListModel;
    var oPropertyCollectionDetailView = this.props.isAttributeTableVisible ? this.getPropertyCollectionDetailedView() : '';
    return (<React.Fragment>
      <div className="classListViewContainer propertyCollection">
        {this.getListView()}
      </div>
      <div className="classDetailedViewContainer propertyCollection">
        {oConfigHeaderView}
        {(aClassListModel.length > 0)?
          <div className="propertyCollectionDetailedView">
          {oPropertyCollectionDetailView}
          </div> :<NothingFoundView message={getTranslation().NO_MATCHES_FOUND} />}
      </div>
    </React.Fragment>)

  }

  render() {

    var aClassListActionGroup = this.props.actionItemModel.ClassListView;
    var oActiveClass = this.props.activeClass;

    var oCreatePropertyCollectionDialog = !CS.isEmpty(oActiveClass) ? this.getCreatePropertyCollectionDialog(oActiveClass, "propertycollection") : null;
    let bIsEntityDialogOpen = ManageEntityConfigProps.getIsEntityDialogOpen();
    let oManageEntityDialog = bIsEntityDialogOpen ? this.getEntityDialog() : null;
    let oSearchBarProps = {
      searchText: this.props.searchText
    };
    let oConfigHeaderView = <ConfigHeaderView actionButtonList={aClassListActionGroup}
                                              showSaveDiscardButtons={this.props.isPropertyCollectionDirty}
                                              context={"propertyCollection"}
                                              searchBarProps={oSearchBarProps}
                                              hideSearchBar={true}
                                              filesUploadHandler={this.handleFilesSelectedToUpload}/>;

    let oPropertyCollectionData = this.getPropertyCollectionBody(oConfigHeaderView);
    return (
        <div className="propertyCollectionViewContainer">
          {oPropertyCollectionData}
          {oCreatePropertyCollectionDialog}
          {oManageEntityDialog}
        </div>
    );
  }
}

export const view = PropertyCollectionConfigView;
export const events = oEvents;
