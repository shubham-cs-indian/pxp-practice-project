import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';

import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as CommonConfigSectionView } from '../../../../../viewlibraries/commonconfigsectionview/common-config-section-view.js';
import { view as CustomDialogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import { view as ConfigEntityCreationDialogView } from './config-entity-creation-dialog-view';
import { view as ContextualGridView } from '../../../../../viewlibraries/contextualgridview/contextual-grid-view';
import GridViewContexts from '../../../../../commonmodule/tack/grid-view-contexts';

let SectionLayout = require('../tack/authorization-mapping-config-layout-data');

const oEvents = {
  HANDLE_CUSTOM_AUTHORIZATION_MAPPING_CONFIG_DIALOG_BUTTON_CLICKED: "handle_custom_authorization_mapping_config_dialog_button_clicked",
};

const oPropTypes = {
  sectionLayoutModel: ReactPropTypes.object,
  context: ReactPropTypes.string,
  skeleton: ReactPropTypes.object,
  data: ReactPropTypes.object,
  visualData: ReactPropTypes.object,
  paginationData: ReactPropTypes.object,
  totalItems: ReactPropTypes.number,
  searchText: ReactPropTypes.string,
  sortBy: ReactPropTypes.string,
  sortOrder: ReactPropTypes.string,
  isGridDataDirty: ReactPropTypes.bool,
  showCheckboxColumn: ReactPropTypes.bool,
  disableDeleteButton: ReactPropTypes.bool,
  enableImportExportButton: ReactPropTypes.bool,
  disableCreate: ReactPropTypes.bool,
  activeAuthorizationMapping: ReactPropTypes.object,
  isAuthorizationMappingDirty: ReactPropTypes.bool,
  isDialogOpen: ReactPropTypes.bool,
};

// @CS.SafeComponent
class AuthorizationConfigView extends React.Component {
  static propTypes = oPropTypes;

  handleAuthorizationMappingConfigDetailDialogButtonClicked = (sContext) => {
    EventBus.dispatch(oEvents.HANDLE_CUSTOM_AUTHORIZATION_MAPPING_CONFIG_DIALOG_BUTTON_CLICKED, sContext);
  }

  createAuthorizationMappingDialog = () => {
    let sEntityType = GridViewContexts.AUTHORIZATION_MAPPING;
    let oActiveMapping = this.props.activeAuthorizationMapping;
    if (oActiveMapping.isCreated) {
      return (
          <ConfigEntityCreationDialogView
              activeEntity={oActiveMapping}
              entityType={sEntityType}
          />
      )
    }
  };

  getCommonConfigView = () => {
    if (CS.isEmpty(this.props.activeAuthorizationMapping)) {
      return null;
    }
    let aDisabledFields = ["code"];
    let oSectionLayout = new SectionLayout();
    let minHeight = {
      minHeight: "65vh"
    }

    return (
        <div style={minHeight}>
          <CommonConfigSectionView context={GridViewContexts.AUTHORIZATION_MAPPING}
                                   sectionLayout={oSectionLayout.mappingBasicInformation}
                                   data={this.props.sectionLayoutModel}
                                   disabledFields={aDisabledFields}/>
        </div>
    )
  };


  getAuthorizationMappingDetailedView = () => {
    let oActiveMapping = this.props.activeAuthorizationMapping;
    if (CS.isEmpty(oActiveMapping)) {
      return null;
    }

    let oBodyStyle = {
      maxWidth: "1200px",
      minWidth: "800px",
      overflowY: "auto",
      width: "100%"
    };
    let aButtonData = [];

    if (this.props.isAuthorizationMappingDirty) {
      aButtonData = [
        {
          id: "cancel",
          label: getTranslation().DISCARD,
          isDisabled: false,
          isFlat: false,
        },
        {
          id: "save",
          label: getTranslation().SAVE,
          isDisabled: false,
          isFlat: false,
        }];
    } else {
      aButtonData = [{
        id: "ok",
        label: getTranslation().OK,
        isFlat: false,
        isDisabled: false
      }];
    }

    let fButtonHandler = this.handleAuthorizationMappingConfigDetailDialogButtonClicked;
    let oCommonConfigViewDOM = this.getCommonConfigView();
    let sTitle = getTranslation().DATA_INTEGRATION_AUTHORIZATION;
    let bIsDialogOpen = this.props.isDialogOpen;

    return (<CustomDialogView modal={true}
                              open={bIsDialogOpen}
                              title={sTitle}
                              bodyStyle={oBodyStyle}
                              contentStyle={oBodyStyle}
                              bodyClassName=""
                              contentClassName="mappingConfigDetail"
                              buttonData={aButtonData}
                              onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
                              buttonClickHandler={fButtonHandler}
                              children={oCommonConfigViewDOM}>
    </CustomDialogView>);
  };

  render () {
    /**Authorization Mapping detail view */
    let oAuthorizationMappingDetailedView = this.getAuthorizationMappingDetailedView();

    /**Create Mapping Dialog */
    let oCreateAuthorizationMappingDialog = this.createAuthorizationMappingDialog();

    /**Grid Required Data */
    let sContext = this.props.context;
    let bShowCheckboxColumn = this.props.showCheckboxColumn;
    let bDisableDeleteButton = this.props.disableDeleteButton;
    let bEnableImportExportButton = this.props.enableImportExportButton;
    let bDisableCreate = this.props.disableCreate;

    return (
        <div className="configGridViewContainer" key="contextConfigGridViewContainer">
          <ContextualGridView
              context={sContext}
              tableContextId={sContext}
              showCheckboxColumn={bShowCheckboxColumn}
              disableDeleteButton={bDisableDeleteButton}
              enableImportExportButton={bEnableImportExportButton}
              disableCreate={bDisableCreate}
          />
          {oAuthorizationMappingDetailedView}
          {oCreateAuthorizationMappingDialog}
        </div>
    );

  }
}

export const view = AuthorizationConfigView;
export const events = oEvents;
