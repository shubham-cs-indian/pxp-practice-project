import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as CommonConfigSectionView } from '../../../../../viewlibraries/commonconfigsectionview/common-config-section-view.js';
import { view as CustomDialogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import { view as ConfigEntityCreationDialogView } from './config-entity-creation-dialog-view';
import { view as ContextualGridView } from '../../../../../viewlibraries/contextualgridview/contextual-grid-view';
import { view as GridView } from '../../../../../viewlibraries/gridview/grid-view';
import SectionLayout from '../tack/mapping-config-layout-data';
import SaveAsDialogGridViewSkeletonForMappings from '../tack/save-as-dialog-grid-view-skeleton-for-mappings';
import MockDataForMappingTypes from "../../../../../commonmodule/tack/mock-data-for-mapping-types";
import ViewUtils from "../../../../../viewlibraries/utils/view-library-utils";

const oEvents = {
  HANDLE_CUSTOM_MAPPING_CONFIG_DIALOG_BUTTON_CLICKED : "handle_custom_mapping_config_dailog_button_clicked",
  MAPPING_CONFIG_DIALOG_BUTTON_CLICKED: "mapping_config_dialog_button_clicked",
  MAPPING_CONFIG_SAVEAS_DIALOG_VALUE_CHANGED: "mapping_config_saveas_dialog_value_changed",
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
  activeMapping: ReactPropTypes.object,
  isMappingDirty: ReactPropTypes.bool,
  isDialogOpen: ReactPropTypes.bool,
  isSaveAsDialogOpen:ReactPropTypes.bool,
  enableCopyButton:ReactPropTypes.bool ,
  duplicateCodes: ReactPropTypes.array,
  dataForSaveAsDialog: ReactPropTypes.array,
};

// @CS.SafeComponent
class MappingConfigView extends React.Component {
  static propTypes = oPropTypes;

  handleMappingConfigDetailDialogButtonClicked = (sContext) => {
    EventBus.dispatch(oEvents.HANDLE_CUSTOM_MAPPING_CONFIG_DIALOG_BUTTON_CLICKED, sContext);
  };

  handleDialogButtonClicked = (sButtonId) => {
    EventBus.dispatch(oEvents.MAPPING_CONFIG_DIALOG_BUTTON_CLICKED, sButtonId);
  };

  handleGridPropertyValueChanged = (sContentId, sPropertyId, sValue, sPathToRoot, oReferencedData) => {
    EventBus.dispatch(oEvents.MAPPING_CONFIG_SAVEAS_DIALOG_VALUE_CHANGED, sContentId, sPropertyId, sValue, sPathToRoot, oReferencedData);
  };

  createMappingDialog = () => {
    let sEntityType = "mapping";
    let oActiveMapping = this.props.activeMapping;
      let sSplitter = ViewUtils.getSplitter();
      let oModel = {
          label: oActiveMapping.label || "",
          code: oActiveMapping.code || "",
          type: {
              items: MockDataForMappingTypes(),
              selectedItems: [oActiveMapping.mappingType],
              cannotRemove: true,
              context: "mapping" + sSplitter + "mappingType",
              disabled: false,
              label: "",
              isMultiSelect: false,
          }
      }
    if (oActiveMapping.isCreated) {
      return (
          <ConfigEntityCreationDialogView
              activeEntity={oActiveMapping}
              entityType={sEntityType}
              model={oModel}
          />
      )
    }
  };

  getCommonConfigView = () => {
    if (CS.isEmpty(this.props.activeMapping)) {
      return null;
    }
    var aDisabledFields = ["code"];
    let oSectionLayout = new SectionLayout();
    let minHeight = {
      minHeight : "65vh"
    }
    return (
        <div style={minHeight}>
        <CommonConfigSectionView context="mapping"
                                 sectionLayout={oSectionLayout.mappingBasicInformation}
                                 data={this.props.sectionLayoutModel}
                                 disabledFields={aDisabledFields}/>
        </div>
    )
  };


  getMappingDetailedView = () => {
    let oActiveMapping = this.props.activeMapping;
    if (CS.isEmpty(oActiveMapping)) {
      return null;
    }

    let oBodyStyle = {
      maxWidth: "1200px",
      minWidth: "800px",
      overflowY: "auto",
    };
    let aButtonData = [];

    if (this.props.isMappingDirty) {
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

    let oCommonConfigViewDOM = this.getCommonConfigView();
    let sTitle = getTranslation().MAPPING_DETAILS;
    let bIsDialogOpen = this.props.isDialogOpen;
    let fButtonHandler = this.handleMappingConfigDetailDialogButtonClicked;

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

  getSaveAsDialogDOM = () => {
    let aDataForSaveAsDialog = this.props.dataForSaveAsDialog;
    let oSkeleton = new SaveAsDialogGridViewSkeletonForMappings();
    let oGridPropertyEventHandlers = {
      gridPropertyValueChangedHandler: this.handleGridPropertyValueChanged
    }

    let aListOfDuplicateCodes = this.props.duplicateCodes;
    let aDuplicateCode = CS.isEmpty(aListOfDuplicateCodes) ? [] : aListOfDuplicateCodes;

    return (
        <div className="mappingSaveAsDialog">
          <GridView
              context="saveAsDialog"
              skeleton={oSkeleton}
              data={aDataForSaveAsDialog}
              showCheckboxColumn={false}
              hideLowerSection={true}
              hideUpperSection={true}
              gridPropertyViewHandlers={oGridPropertyEventHandlers}
              duplicateCode={aDuplicateCode}
          />
        </div>
    );
  };

  getSaveAsDialog = () => {
    let _props = this.props;
    let bIsSaveAsDialogOpen = _props.isSaveAsDialogOpen;
    if (bIsSaveAsDialogOpen) {

      let aButtonData = [
        {
          id: "mapping_saveAs_discard",
          label: getTranslation().CANCEL,
          isDisabled: false,
          isFlat: true,
        },
        {
          id: "mapping_saveAs_save",
          label: getTranslation().SAVE,
          isDisabled: false,
          isFlat: false,
        }
      ];

      let oSaveAsDialogView =  this.getSaveAsDialogDOM();
      return (<CustomDialogView modal={true} open={true}
                                title={getTranslation().COPY}
                                bodyClassName=""
                                className={"mappingConfigSaveAsDialog"}
                                buttonData={aButtonData}
                                onRequestClose={this.handleDialogButtonClicked.bind(this, aButtonData[0].id)}
                                buttonClickHandler={this.handleDialogButtonClicked}>
        {oSaveAsDialogView}
      </CustomDialogView>);
    } else {
      return null;
    }
  };

  render() {

    /**Mapping detail view */
    let oMappingDetailedView = this.getMappingDetailedView();

    /**Create Mapping Dialog */
    let oCreateMappingDialog = this.createMappingDialog();

    /**Copy Mapping Dialog */
    let oSaveAsDialog = this.getSaveAsDialog();

    /**Grid Required Data */
    let sContext =  this.props.context;
    let bShowCheckboxColumn = this.props.showCheckboxColumn;
    let bDisableDeleteButton = this.props.disableDeleteButton;
    let bEnableImportExportButton = this.props.enableImportExportButton;
    let bDisableCreate = this.props.disableCreate;
    let oManageEntityDialog = this.props.oManageEntityDialog;
    let bEnableManageEntityButton = this.props.enableManageEntityButton;
    let bEnableCopyButton = this.props.enableCopyButton;
    let oColumnOrganizerData = this.props.columnOrganizerData;

    return (
        <div className="configGridViewContainer" key="contextConfigGridViewContainer">
          <ContextualGridView
              context={sContext}
              tableContextId={sContext}
              showCheckboxColumn={bShowCheckboxColumn}
              disableDeleteButton={bDisableDeleteButton}
              enableImportExportButton={bEnableImportExportButton}
              disableCreate={bDisableCreate}
              enableManageEntityButton={bEnableManageEntityButton}
              enableCopyButton={bEnableCopyButton}
              selectedColumns={oColumnOrganizerData.selectedColumns}
              isColumnOrganizerDialogOpen={oColumnOrganizerData.isColumnOrganizerDialogOpen}
          />
          {oMappingDetailedView}
          {oCreateMappingDialog}
          {oManageEntityDialog}
          {oSaveAsDialog}
        </div>
    );

  }
}

export const view = MappingConfigView;
export const events = oEvents;
