import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as ListView } from '../../../../../viewlibraries/listviewnew/list-view-new.js';
import { view as CustomDialogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import { view as RoleDetailView } from './role-detail-view';
import { view as ConfigEntityCreationDialogView } from './config-entity-creation-dialog-view';
import { view as PermissionDetailView } from './permission-detail-view';

const oEvents = {
  ROLE_CONFIG_PERMISSION_DIALOG_BUTTON_CLICKED: "role_config_permission_dialog_button_clicked"
};

const oPropTypes = {
  actionItemModel: ReactPropTypes.object.isRequired,
  roleListModel: ReactPropTypes.array.isRequired,
  selectedRoleDetailedModel: ReactPropTypes.object.isRequired,
  entitiesList: ReactPropTypes.array.isRequired,
  bRoleCreated: ReactPropTypes.bool,
  isPermissionVisible: ReactPropTypes.bool,
  permissionsData: ReactPropTypes.object,
  modelForRoleCreationDialog: ReactPropTypes.object,
  hideSystemsSelectionView: ReactPropTypes.bool,
  isUserSystemAdmin: ReactPropTypes.bool,
  listViewActionItem: ReactPropTypes.object
};

// @CS.SafeComponent
class RoleConfigView extends React.Component {
  static propTypes = oPropTypes;

  getRoleView = () => {
    let aListViewModels = this.props.roleListModel;
    let oStyle = {
      height: "calc(100% - 52px)"
    };
    return (
        <ListView
            model={aListViewModels}
            bListItemCreated={this.props.bRoleCreated}
            context={"role"}
            hideSearchBar={false}
            searchText={this.props.searchText}
            bEnableLoadMore={false}
            listViewActionItem={this.props.listViewActionItem}
            style={oStyle}
        />
    )
  };

  handlePermissionDialogButtonClick = (sButtonId) => {
    EventBus.dispatch(oEvents.ROLE_CONFIG_PERMISSION_DIALOG_BUTTON_CLICKED, sButtonId);
  };

  getPermissionDetailView = () => {
    let oPermissionsData = this.props.permissionsData;
    let fButtonHandler = this.handlePermissionDialogButtonClick;
    let oPermissionDetailData = oPermissionsData.permissionDetailData;

    let oBodyStyle = {
      padding: 0,
      maxHeight: 'none',
      overflowY: "auto",
    };
    let oContentStyle = {
      height: "90%",
      maxHeight: "none",
      width: '90%',
      maxWidth: 'none'
    };
    let aButtonData = [
      {
        id: "ok",
        label: getTranslation().OK,
        isFlat: false
      }
    ];
    if (oPermissionDetailData.isPermissionDirty) {
      aButtonData = [
        {
          id: "discard",
          label: getTranslation().DISCARD,
          isFlat: true
        },
        {
          id: "save",
          label: getTranslation().SAVE,
          isFlat: false
        }
      ];
    }

    return (
        <CustomDialogView
            open={this.props.isPermissionVisible}
            bodyStyle={oBodyStyle}
            contentStyle={oContentStyle}
            contentClassName="permissionDetail"
            buttonData = {aButtonData}
            onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
            buttonClickHandler= {fButtonHandler}
            title={getTranslation().PERMISSIONS}
        >
          <PermissionDetailView
              permissionsData={oPermissionDetailData}
              leftSectionData={oPermissionsData.permissionViewLeftSectionData}
              treeHierarchyData={oPermissionsData.treeHierarchyData}
          />
        </CustomDialogView>
    );
  };

  getRoleDetailedView = () => {

    if(this.props.isPermissionVisible) {
      return this.getPermissionDetailView();
    }
    else {
      if (!this.props.selectedRoleDetailedModel.properties["isEmpty"]) {
        return (<RoleDetailView
            selectedRoleDetailedModel={this.props.selectedRoleDetailedModel}
            entitiesList={this.props.entitiesList}
            hideSystemsSelectionView={this.props.hideSystemsSelectionView}
            isUserSystemAdmin={this.props.isUserSystemAdmin}
        sContext={"roleDetailView"}/>);
      }

    }
  };

  getCreateRoleDialog = (oSelectedRoleModel, sEntityType) => {
    var oActiveRole ={};
    oActiveRole.label = oSelectedRoleModel.label;
    oActiveRole.isCreated = oSelectedRoleModel.properties['isCreated'];
    oActiveRole.code = oSelectedRoleModel.properties['code'];
    return (
        <ConfigEntityCreationDialogView
            activeEntity={oActiveRole}
            entityType={sEntityType}
            extraFieldsToShow={this.props.modelForRoleCreationDialog}
        />
    )
  };

  render() {

    var oSelectedRoleDetailedModel = this.props.selectedRoleDetailedModel;
    var aRoleListView = this.getRoleView();
    var roleDetailedView = this.getRoleDetailedView();
    var oCreateRoleDialog = !CS.isEmpty(oSelectedRoleDetailedModel) ? this.getCreateRoleDialog(oSelectedRoleDetailedModel, 'role') : null;

    var mainArray = [];
    return (
        <div className="roleConfigViewContainer">
          <div className="roleListViewContainer">
            {aRoleListView}
          </div>
          <div className="roleDetailedViewContainer">
            <div className="roleDetailInfoViewContiner">
              <div className = "roleDetailInfoUpperContainer">
                <div className="roleDetailViewContainer">
                  {roleDetailedView}
                </div>
                {!CS.isEmpty(mainArray) ? (<div className="classPermissionContainer">
                  {mainArray}
                </div>) : null}
              </div>
                {oCreateRoleDialog}
            </div>
          </div>
        </div>
    );
  }
}

export const view = RoleConfigView;
export const events = oEvents;
