import CS from '../../../../../libraries/cs';
import React from 'react' ;
import ReactPropTypes from 'prop-types';

import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';

import { view as CustomDialogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import { view as CommonConfigSectionView } from '../../../../../viewlibraries/commonconfigsectionview/common-config-section-view.js';

import ViewUtils from './utils/view-utils';
import CreateDialogLayoutData from '../tack/mock/create-dialog-layout-data';
import SectionLayout from '../tack/endpoint-config-layout-data';
import  { view as ContextualGridView } from '../../../../../viewlibraries/contextualgridview/contextual-grid-view';
import  { view as GridView } from '../../../../../viewlibraries/gridview/grid-view';
import MockDataForEndPointTypes from '../../../../../commonmodule/tack/mock-data-for-mdm-endpoint-types';
import GridViewStore from "../../../../../viewlibraries/contextualgridview/store/grid-view-store";
import GridViewContexts from "../../../../../commonmodule/tack/grid-view-contexts";

const oEvents = {
  ENDPOINT_CONFIG_DIALOG_BUTTON_CLICKED: "endpoint_config_dialog_button_clicked",
  ENDPOINT_CONFIG_SAVEAS_DIALOG_VALUE_CHANGED: "endpoint_config_saveas_dialog_value_changed",
};

const oPropTypes = {
  actionItemModel: ReactPropTypes.object.isRequired,
  isEndpointDirty: ReactPropTypes.bool,
  isDialogOpen: ReactPropTypes.bool,
  context:ReactPropTypes.bool,
  data:ReactPropTypes.object,
  activeEndpoint:ReactPropTypes.object,
  sectionLayoutModel:ReactPropTypes.object,
  isSaveAsDialogOpen: ReactPropTypes.bool,
  enableCopyButton: ReactPropTypes.bool,
  duplicateCodes: ReactPropTypes.array,
  duplicateLabels: ReactPropTypes.array,
  duplicateCodesForWorkflows: ReactPropTypes.array,
  duplicateLabelsForWorkflows: ReactPropTypes.array,
  dataForSaveAsDialog: ReactPropTypes.array,
  dataForWorkflowSaveAsDialog: ReactPropTypes.array,
  oManageColumnOrganiserDialog: ReactPropTypes.object,
  selectIconDialog: ReactPropTypes.object,
};

// @CS.SafeComponent
class EndpointConfigView extends React.Component {
  static propTypes = oPropTypes;


  handleDialogButtonClicked = (sButtonId) => {
    EventBus.dispatch(oEvents.ENDPOINT_CONFIG_DIALOG_BUTTON_CLICKED, sButtonId);
  };

  handleGridPropertyValueChanged = (sSubType, sContentId, sPropertyId, sValue, sPathToRoot, oReferencedData) => {
    EventBus.dispatch(oEvents.ENDPOINT_CONFIG_SAVEAS_DIALOG_VALUE_CHANGED, sContentId, sPropertyId, sValue, sPathToRoot, oReferencedData,sSubType);
  };

  getCreateDialog = () => {
    let oActiveEndpoint = this.props.activeEndpoint;
    if (oActiveEndpoint.isCreated) {

      let bIsDisableCreate = true;
      let sErrorText = "";
      if (CS.isEmpty(oActiveEndpoint.code.trim())) {
        sErrorText = getTranslation().CODE_SHOULD_NOT_BE_EMPTY;
      } else if (!ViewUtils.isValidEntityCode(oActiveEndpoint.code)) {
        sErrorText = getTranslation().PLEASE_ENTER_VALID_CODE;
      } else {
        bIsDisableCreate = false;
      }
      let oModel = {
        label: oActiveEndpoint.label || "",
        code: oActiveEndpoint.code || "",
        endpointType: {}
      };
      let aButtonData = [
        {
          id: "endpoint_creation_discard",
          label: getTranslation().CANCEL,
          isDisabled: false,
          isFlat: true,
        },
        {
          id: "endpoint_creation_create",
          label: getTranslation().CREATE,
          isDisabled: bIsDisableCreate,
          isFlat: false,
        }
      ];

      let sSplitter = ViewUtils.getSplitter();
      oModel.endpointType = {
        items: MockDataForEndPointTypes(),
        selectedItems: [oActiveEndpoint.endpointType],
        cannotRemove: true,
        context: "profile" + sSplitter + "endpointType",
        disabled: false,
        label: "",
        isMultiSelect: false,
      };
      let fButtonHandler = this.handleDialogButtonClicked ;
      let oCreateDialogLayoutData = new CreateDialogLayoutData();
      return (<CustomDialogView modal={true} open={true}
                                title={getTranslation().CREATE}
                                bodyClassName=""
                                className={"processConfigCreateDialog"}
                                buttonData={aButtonData}
                                onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
                                buttonClickHandler={fButtonHandler}>
        <CommonConfigSectionView context={"profile"} sectionLayout={oCreateDialogLayoutData} data={oModel}
                                 errorTextForCodeEntity={sErrorText}/>
      </CustomDialogView>);
    } else {
      return null;
    }
  };

  getEndpointDetailedView = () => {
    let _props = this.props;
    let oActiveEndpoint = _props.activeEndpoint;
    if (CS.isEmpty(oActiveEndpoint) || oActiveEndpoint.isCreated) {
      return null;
    }
    let aDisabledFields = ["code"];
    let oSectionLayout = new SectionLayout();
    let aButtonData = [];

    if (_props.isEndpointDirty) {
      aButtonData = [
        {
          id: "detailed_endpoint_discard",
          label: getTranslation().DISCARD,
          isDisabled: false,
          isFlat: false,
        },
        {
          id: "detailed_endpoint_save",
          label: getTranslation().SAVE,
          isDisabled: false,
          isFlat: false,
        }];
    } else {
      aButtonData = [{
        id: "detailed_endpoint_close",
        label: getTranslation().OK,
        isFlat: false,
        isDisabled: false
      }];
    }

    let oBodyStyle = {
      padding: 0,
      maxHeight: 'none'
    };
    let oContentStyle = {
      transform: 'none',
      width: '94%',
      maxWidth: 'none',
      overflowY: 'unset'
    };


    return (
        <CustomDialogView modal={true} open={true}
                          bodyClassName="endpointDetailDialogContainer"
                          contentClassName="endpointDetailDialog"
                          buttonData={aButtonData}
                          contentStyle={oContentStyle}
                          bodyStyle={oBodyStyle}
                          title={getTranslation().ENDPOINTS_DETAILS}
                          onRequestClose={this.handleDialogButtonClicked.bind(this, aButtonData[0].id)}
                          buttonClickHandler={this.handleDialogButtonClicked}>

          <div className="endpointDetailView">
            <CommonConfigSectionView context="profile"
                                     sectionLayout={oSectionLayout.endpointBasicInformation}
                                     data={_props.sectionLayoutModel}
                                     disabledFields={aDisabledFields}/>

          </div>
        </CustomDialogView>)
  };


  getSaveAsDialog = () => {
    let _props = this.props;
    let bIsSaveAsDialogOpen = _props.isSaveAsDialogOpen;
    if (bIsSaveAsDialogOpen) {

      let aButtonData = [
        {
          id: "endpoint_saveAs_discard",
          label: getTranslation().CANCEL,
          isDisabled: false,
          isFlat: true,
        },
        {
          id: "endpoint_saveAs_save",
          label: getTranslation().SAVE,
          isDisabled: false,
          isFlat: false,
        }
      ];

      let oContentStyle = {
        maxHeight: "80%",
        width: '70%',
        maxWidth: 'none'
      };

      let oSaveAsDialogView = this.getSaveAsDialogDOM(GridViewContexts.END_POINT_COPY, false);
      let oSaveAsDialogViewForWorkflow = this.getSaveAsDialogDOM(GridViewContexts.END_POINT_COPY_PROCESS, true);
      return (<CustomDialogView modal={true} open={true}
                                title={getTranslation().COPY}
                                bodyClassName=""
                                contentStyle={oContentStyle}
                                className={"endpointConfigSaveAsDialog"}
                                buttonData={aButtonData}
                                onRequestClose={this.handleDialogButtonClicked.bind(this, aButtonData[0].id)}
                                buttonClickHandler={this.handleDialogButtonClicked}>
        {oSaveAsDialogView}
        {oSaveAsDialogViewForWorkflow}
      </CustomDialogView>);
    } else {
      return null;
    }
  };

  /** To create grid view for Saveas Dialog Dom */
  getSaveAsDialogDOM = (sContext, bShowCheckboxColumn) => {
    let aDataForSaveAsDialog;
    let aDuplicateCode;
    let aDuplicateLabel;
    let sLabel;
    if (sContext == GridViewContexts.END_POINT_COPY) {
      aDataForSaveAsDialog = this.props.dataForSaveAsDialog;
      aDuplicateCode = this.props.duplicateCodes || [];
      aDuplicateLabel = this.props.duplicateLabels || [];
      sLabel = getTranslation().ENDPOINT;
    } else {
      aDataForSaveAsDialog = this.props.dataForWorkflowSaveAsDialog;
      if (CS.isEmpty(aDataForSaveAsDialog)) {
        return null;
      }
      aDuplicateCode = this.props.duplicateCodesForWorkflows || [];
      aDuplicateLabel = this.props.duplicateLabelsForWorkflows || [];
      sLabel = getTranslation().WORKFLOW;
    }
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(sContext);
    let oSkeleton = oGridViewPropsByContext.getGridViewSkeleton();
    let oGridPropertyEventHandlers = {
      gridPropertyValueChangedHandler: this.handleGridPropertyValueChanged.bind(this, sContext)
    }
    let oColumnOrganizerData = this.props.columnOrganizerData;

    return (
        <div className="endpointSaveAsDialog">
          <div className="gridViewLabel">{sLabel}</div>
          <GridView
              context={sContext}
              skeleton={oSkeleton}
              data={aDataForSaveAsDialog}
              showCheckboxColumn={bShowCheckboxColumn}
              hideLowerSection={true}
              hideUpperSection={true}
              gridPropertyViewHandlers={oGridPropertyEventHandlers}
              duplicateCode={aDuplicateCode}
              duplicateLabel={aDuplicateLabel}
              selectedColumns={oColumnOrganizerData.selectedColumns}
              isColumnOrganizerDialogOpen={oColumnOrganizerData.isColumnOrganizerDialogOpen}
          />
        </div>
    );
  };

  render () {


    let oProcessDetailedView = this.getEndpointDetailedView();

    let oActiveEndpoint = this.props.activeEndpoint;
    let oCreateDialog = this.getCreateDialog(oActiveEndpoint, 'endpoint');
    let oSaveAsDialog = this.getSaveAsDialog();

    /**Grid Required Data */
    let sContext = this.props.context;
    let bShowCheckboxColumn = this.props.showCheckboxColumn;
    let bDisableDeleteButton = this.props.disableDeleteButton;
    let bEnableImportExportButton = this.props.enableImportExportButton;
    let bDisableCreate = this.props.disableCreate;
    let bEnableCopyButton = this.props.enableCopyButton;
    let oColumnOrganizerData = this.props.columnOrganizerData;
    let oSelectIconDialog = this.props.selectIconDialog;

    return (
        <div className="configGridViewContainer" key="contextConfigGridViewContainer">
          <ContextualGridView
              context={sContext}
              tableContextId={sContext}
              showCheckboxColumn={bShowCheckboxColumn}
              disableDeleteButton={bDisableDeleteButton}
              enableImportExportButton={bEnableImportExportButton}
              disableCreate={bDisableCreate}
              enableCopyButton={bEnableCopyButton}
              selectedColumns={oColumnOrganizerData.selectedColumns}
              isColumnOrganizerDialogOpen={oColumnOrganizerData.isColumnOrganizerDialogOpen}
          />
          {oProcessDetailedView}
          {oCreateDialog}
          {oSaveAsDialog}
          {oSelectIconDialog}
        </div>
    )
  }
}

export const view = EndpointConfigView;
export const events = oEvents;
