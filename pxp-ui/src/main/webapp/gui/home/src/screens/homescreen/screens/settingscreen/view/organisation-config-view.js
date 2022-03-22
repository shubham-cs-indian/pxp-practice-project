import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as ListView } from '../../../../../viewlibraries/listviewnew/list-view-new.js';
import { view as NothingFoundView } from '../../../../../viewlibraries/nothingfoundview/nothing-found-view';
import { view as ConfigHeaderView } from '../../../../../viewlibraries/configheaderview/config-header-view';
import { view as OrganisationConfigDetailView } from './organisation-config-detail-view';
import { view as ConfigEntityCreationDialogView } from './config-entity-creation-dialog-view';
import ManageEntityConfigProps from "../store/model/manage-entity-config-props";
import {view as ManageEntityDialogView} from "./entity-usage-dialog-view";

const oEvents = {
  HANDLE_ORGANISATION_IMPORT_BUTTON_CLICKED : "handle_organisation_import_button_clicked"
};

const oPropTypes = {
  activeOrganisation: ReactPropTypes.object.isRequired,
  actionItemModel: ReactPropTypes.object,
  ruleConfigView: ReactPropTypes.object,
  listItemCreated: ReactPropTypes.bool,
  organisationConfigListModel: ReactPropTypes.array,
  activeTabId: ReactPropTypes.string,
  context: ReactPropTypes.string,
  isPermissionVisible: ReactPropTypes.bool,
  systemList: ReactPropTypes.array,
  organisationBasicInformationData: ReactPropTypes.object,
  hideRuleConfigPermissionToggle: ReactPropTypes.bool,
  systemsSelectionViewModel: ReactPropTypes.object,
  isDirty: ReactPropTypes.bool,
  ssoSettingView: ReactPropTypes.object,
  showLoadMore: ReactPropTypes.bool,
  iconLibraryData: ReactPropTypes.object,
  actionItemOfListNodeView: ReactPropTypes.array
};

// @CS.SafeComponent
class OrganisationConfigView extends React.Component{
  static propTypes = oPropTypes


  handleFilesSelectedToUpload = (aFiles) => {
    EventBus.dispatch(oEvents.HANDLE_ORGANISATION_IMPORT_BUTTON_CLICKED, aFiles);
  };

  getListView = () => {
    let aClassListModelGeneric = this.props.organisationConfigListModel;
    let oStyle = {
      height: "calc(100% - 52px)"
    };
    return (
        <ListView
            model={aClassListModelGeneric}
            bListItemCreated={this.props.listItemCreated}
            bEnableLoadMore={this.props.showLoadMore}
            context={"organization"}
            hideSearchBar={false}
            searchText={this.props.searchText}
            style={oStyle}
        />
    )
  };

  getDetailedView =()=> {
    let oProps = this.props;
    if (CS.isEmpty(this.props.activeOrganisation)) {
      return (<NothingFoundView message={getTranslations().PLEASE_SELECT_ORGANISATION_CONFIG}/>);
    } else {
      return (<OrganisationConfigDetailView activeTabId={oProps.activeTabId}
                                            activeOrganisation={oProps.activeOrganisation}
                                            context={oProps.context}
                                            isPermissionVisible={this.props.isPermissionVisible}
                                            systemList={this.props.systemList}
                                            organisationBasicInformationData={oProps.organisationBasicInformationData}
                                            ruleConfigView={this.props.ruleConfigView}
                                            ssoSettingView={this.props.ssoSettingView}
                                            hideRuleConfigPermissionToggle={this.props.hideRuleConfigPermissionToggle}
                                            systemsSelectionViewModel={this.props.systemsSelectionViewModel}
                                            iconLibraryData={this.props.iconLibraryData}

      />)
    }
  }

  getCreateOrganisationDialog =(oActiveEntity,sEntityType)=> {
    return (
        <ConfigEntityCreationDialogView
            activeEntity={oActiveEntity}
            entityType={sEntityType}
            extraFieldsToShow = {this.props.organisationCreateDialogModel}
        />
    )
  }

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


  render() {
    var aOrgConfigListActionGroup = this.props.actionItemModel.organisationConfigListView;
    var oOrgConfigListView = this.getListView();
    var oDetailedView = this.getDetailedView();
    var oActiveOrgConfig = this.props.activeOrganisation;
    var oCreateOrganisationDialog = !CS.isEmpty(oActiveOrgConfig) ? this.getCreateOrganisationDialog(oActiveOrgConfig,"organization") : null;
    let bIsEntityDialogOpen = ManageEntityConfigProps.getIsEntityDialogOpen();
    let oManageEntityDialog = bIsEntityDialogOpen ? this.getEntityDialog() : null;
    let oSearchBarProps = {searchText: this.props.searchText};

    return (
        <div className="organisationConfigViewContainer">
          <ConfigHeaderView actionButtonList={aOrgConfigListActionGroup}
                            showSaveDiscardButtons={this.props.isDirty}
                            context={"organization"}
                            searchBarProps={oSearchBarProps}
                            filesUploadHandler={this.handleFilesSelectedToUpload}
                            hideSearchBar={true}/>
          <div className="organisationConfigListViewContainer">
            {oOrgConfigListView}
          </div>
          <div className="organisationDetailedViewContainer">
            <div className="organisationDetailedViewBody">
              {oDetailedView}
              {oCreateOrganisationDialog}
              {oManageEntityDialog}
            </div>
          </div>
        </div>
    );
  }
}

export const view = OrganisationConfigView;
export const events = oEvents;
