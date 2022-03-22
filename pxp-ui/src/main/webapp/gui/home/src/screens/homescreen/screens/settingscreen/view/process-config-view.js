import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as CustomDialogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import { view as CommonConfigSectionView } from '../../../../../viewlibraries/commonconfigsectionview/common-config-section-view.js';
import ViewUtils from './utils/view-utils';
import CreateDialogLayoutData from '../tack/mock/create-dialog-layout-data';
import MockDataForProcessEventTypes from '../../../../../commonmodule/tack/mock-data-for-mdm-process-event-types';
import SectionLayout from '../tack/process-config-layout-data';
import { view as ContextualGridView } from '../../../../../viewlibraries/contextualgridview/contextual-grid-view';
import { view as GridView } from '../../../../../viewlibraries/gridview/grid-view';
import { view as AdvancedSearchPanelView } from '../../contentscreen/view/advanced-search-panel-view';
import SaveAsDialogGridViewSkeleton from "../tack/save-as-dialog-grid-view-skeleton";
import MockDataForProcessWorkflowTypes from '../tack/mock/mock-data-for-process-workflow-types';
import ProcessEventTypeDictionary from '../../../../../commonmodule/tack/process-event-type-dictionary';
import MockDataForProcessTriggeringEvent from "../tack/mock/mock-data-for-process-triggering-event";
import MockDataForWorkflowTypesDictionary from '../tack/mock/mock-data-for-workflow-types-dictionary';
import MockDataForProcessActionTypesDictionary from "../tack/mock/mock-data-for-action-subtypes-dictionary";
import {view as FilterTaxonomySelectorView} from "../../contentscreen/view/fltr-taxonomy-selector-view";


const oEvents = {
  PROCESS_CONFIG_DIALOG_BUTTON_CLICKED: "process_config_dialog_button_clicked",
  PROCESS_CONFIG_SAVEAS_DIALOG_VALUE_CHANGED: "process_config_saveas_dialog_value_changed",
  PROCESS_CONFIG_WORKFLOW_TYPE_CLICKED: "process_config_workflow_type_clicked",
  PROCESS_CONFIG_ENTITY_TYPE_CLICKED: "process_config_entity_type_clicked",
};

const oPropTypes = {
  actionItemModel: ReactPropTypes.object.isRequired,
  processListModel: ReactPropTypes.array.isRequired,
  activeProcess: ReactPropTypes.object,
  availableComponents: ReactPropTypes.array,
  allComponents: ReactPropTypes.array,
  selectedComponent: ReactPropTypes.object,
  ruleList: ReactPropTypes.object,
  relationshipList: ReactPropTypes.object,
  contextList: ReactPropTypes.array,
  attributeList: ReactPropTypes.array,
  lazyMSSReqResInfo: ReactPropTypes.object,
  orgConfigListModel: ReactPropTypes.array,
  destinationCatalogModel: ReactPropTypes.array,
  allTalendJobList: ReactPropTypes.array,
  componentList: ReactPropTypes.array,
  isProcessDirty: ReactPropTypes.bool,
  isFullScreenMode: ReactPropTypes.bool,
  isSearchFilterComponentDialogOpen:ReactPropTypes.bool,
  searchFilterComponentData:ReactPropTypes.object,
  isSaveAsDialogOpen:ReactPropTypes.bool,
  enableCopyButton:ReactPropTypes.bool ,
  duplicateCodes: ReactPropTypes.array,
  dataForSaveAsDialog: ReactPropTypes.array,
  workflowFilterData: ReactPropTypes.object,
  searchCriteriaData: ReactPropTypes.object,
  isSearchCriteriaDialogOpen: ReactPropTypes.bool,
};

// @CS.SafeComponent
class ProcessConfigView extends React.Component {
  static propTypes = oPropTypes;

  handleDialogButtonClicked = (sButtonId) => {
    EventBus.dispatch(oEvents.PROCESS_CONFIG_DIALOG_BUTTON_CLICKED, sButtonId);
  };

  handleGridPropertyValueChanged = (sContentId, sPropertyId, sValue, sPathToRoot, oReferencedData) => {
    EventBus.dispatch(oEvents.PROCESS_CONFIG_SAVEAS_DIALOG_VALUE_CHANGED, sContentId, sPropertyId, sValue, sPathToRoot, oReferencedData);
  };

  handleWorkflowTypeClicked = (aSelectedItem) => {
    EventBus.dispatch(oEvents.PROCESS_CONFIG_WORKFLOW_TYPE_CLICKED, aSelectedItem);
  };

  handleEntityTypeClicked = (aSelectedItem) => {
    EventBus.dispatch(oEvents.PROCESS_CONFIG_ENTITY_TYPE_CLICKED, aSelectedItem);
  };

  /** To create the Mss model */
  getProcessTypeMssModel = (aItems, sSelectedItem, sSubContext) => {
    let sSplitter = ViewUtils.getSplitter();
    return {
      items: aItems,
      selectedItems: [sSelectedItem],
      cannotRemove: true,
      context: "process" + sSplitter + sSubContext,
      disabled: false,
      label: "",
      isMultiSelect: false,
    }
  };

  getCreateDialog = () => {
    let oActiveProcess = this.props.activeProcess;
    if (oActiveProcess.isCreated) {

      var bIsDisableCreate = true;
      var sErrorText = "";
      if (CS.isEmpty(oActiveProcess.code.trim())) {
        sErrorText = getTranslation().CODE_SHOULD_NOT_BE_EMPTY;
      } else if (!ViewUtils.isValidEntityCode(oActiveProcess.code)) {
        sErrorText = getTranslation().PLEASE_ENTER_VALID_CODE;
      } else {
        bIsDisableCreate = false;
      }
      let oModel = {
        label: oActiveProcess.label || "",
        processType: {},
        code: oActiveProcess.code || "",
      };
      let aButtonData = [
        {
          id: "process_creation_discard",
          label: getTranslation().CANCEL,
          isDisabled: false,
          isFlat: true,
        },
        {
          id: "process_creation_create",
          label: getTranslation().CREATE,
          isDisabled: bIsDisableCreate,
          isFlat: false,
        }
      ];

      let fButtonHandler = this.handleDialogButtonClicked;
      let aItems = MockDataForProcessEventTypes();
      let aWorkflowItems = MockDataForProcessWorkflowTypes();
      let aActionTypes = MockDataForProcessTriggeringEvent();
      if (oActiveProcess.eventType === ProcessEventTypeDictionary.BUSINESS_PROCESS_EVENT) {
        aActionTypes = CS.filter(aActionTypes, function (oItem) {
          return (oItem.id === "afterSave" || oItem.id === "afterCreate" );
        })
      } else if(oActiveProcess.eventType === ProcessEventTypeDictionary.INTEGRATION){
        aActionTypes = CS.filter(aActionTypes, function (oItem) {
          return (oItem.id === "import" || oItem.id === "export" );
        })
      }

      if (oActiveProcess.workflowType !== MockDataForWorkflowTypesDictionary.SCHEDULED_WORKFLOW &&
          oActiveProcess.workflowType !== MockDataForWorkflowTypesDictionary.JMS_WORKFLOW) {
        oModel.eventType = this.getProcessTypeMssModel(aItems, oActiveProcess.eventType, "eventType");
        oModel.triggeringEventType = this.getProcessTypeMssModel(aActionTypes, oActiveProcess.triggeringType, "triggeringEventType");
      }

      oModel.workflowType = this.getProcessTypeMssModel(aWorkflowItems, oActiveProcess.workflowType, "workflowType");

      let oCreateDialogLayoutData = new CreateDialogLayoutData();
      return (<CustomDialogView modal={true} open={true}
                                title={getTranslation().CREATE}
                                bodyClassName=""
                                className={"processConfigCreateDialog"}
                                buttonData={aButtonData}
                                onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
                                buttonClickHandler={fButtonHandler}>
        <CommonConfigSectionView context={"process"} sectionLayout={oCreateDialogLayoutData} data={oModel}
                                 errorTextForCodeEntity={sErrorText}/>
      </CustomDialogView>);
    } else {
      return null;
    }
  };

  getSaveAsDialog = () => {
    let _props = this.props;
    let bIsSaveAsDialogOpen = _props.isSaveAsDialogOpen;
    if (bIsSaveAsDialogOpen) {

      let aButtonData = [
        {
          id: "process_saveAs_discard",
          label: getTranslation().CANCEL,
          isDisabled: false,
          isFlat: true,
        },
        {
          id: "process_saveAs_save",
          label: getTranslation().SAVE,
          isDisabled: false,
          isFlat: false,
        }
      ];

      let oSaveAsDialogView =  this.getSaveAsDialogDOM();
      return (<CustomDialogView modal={true} open={true}
                                title={getTranslation().COPY}
                                bodyClassName=""
                                className={"processConfigSaveAsDialog"}
                                buttonData={aButtonData}
                                onRequestClose={this.handleDialogButtonClicked.bind(this, aButtonData[0].id)}
                                buttonClickHandler={this.handleDialogButtonClicked}>
        {oSaveAsDialogView}
      </CustomDialogView>);
    } else {
      return null;
    }
  };

  getSaveAsDialogDOM = () => {
    let aDataForSaveAsDialog = this.props.dataForSaveAsDialog;
    let oSkeleton = new SaveAsDialogGridViewSkeleton();
    let oGridPropertyEventHandlers = {
      gridPropertyValueChangedHandler: this.handleGridPropertyValueChanged
    }

    let aListOfDuplicateCodes = this.props.duplicateCodes;
    let aDuplicateCode = CS.isEmpty(aListOfDuplicateCodes) ? [] : aListOfDuplicateCodes;
    let oColumnOrganizerData = this.props.columnOrganizerData;

    return (
        <div className="processSaveAsDialog">
          <GridView
              context="saveAsDialog"
              skeleton={oSkeleton}
              data={aDataForSaveAsDialog}
              showCheckboxColumn={false}
              hideLowerSection={true}
              hideUpperSection={true}
              gridPropertyViewHandlers={oGridPropertyEventHandlers}
              duplicateCode={aDuplicateCode}
              selectedColumns={oColumnOrganizerData.selectedColumns}
              isColumnOrganizerDialogOpen={oColumnOrganizerData.isColumnOrganizerDialogOpen}
          />
        </div>
    );
  };

  getAdvancedSearchPanelView = () => {
    let oSearchFilterComponentData = this.props.searchFilterComponentData;
    let oAdvancedSearchPanelData = oSearchFilterComponentData.advancedSearchPanelData;
    let sSelectedHierarchyContext = "";
    let oAdvancedSearchPanelView = null;
    let sContext = "processSearchFilter";
    if (this.props.isSearchFilterComponentDialogOpen) {
      oAdvancedSearchPanelData.showAdvancedSearchPanel = this.props.isSearchFilterComponentDialogOpen;
      oAdvancedSearchPanelView = (<AdvancedSearchPanelView advancedSearchPanelData={oAdvancedSearchPanelData}
                                                           appliedFilterDataClone={oSearchFilterComponentData.appliedFilterDataClone}
                                                           appliedFilterData={oSearchFilterComponentData.appliedFilterData}
                                                           masterAttributeList={oSearchFilterComponentData.masterAttributeList}
                                                           selectedHierarchyContext={sSelectedHierarchyContext}
                                                           context = {sContext}
                                                           />)
    }

    return oAdvancedSearchPanelView;
  };

  /** Function to display edit button and the choose Taxonomy dialog box **/
  getSearchCriteriaMapView = () => {
    let bIsSearchCriteriaDialogOpen = this.props.isSearchCriteriaDialogOpen;
    let oSearchCriteriaData = this.props.searchCriteriaData;
    return (
        <FilterTaxonomySelectorView
            taxonomyTree={oSearchCriteriaData.taxonomyTree}
            taxonomyVisualProps={oSearchCriteriaData.taxonomyVisualProps}
            matchingTaxonomyIds={[]}
            taxonomyTreeSearchText={""}
            isFilterAndSearchViewDisabled={false}
            parentTaxonomyId={""}
            affectedTreeNodeIds={[]}
            isSelectTaxonomyPopOverVisible={bIsSearchCriteriaDialogOpen}
            viewMode={"tile_mode"}
            collectionData={null}
            isOrganizeButtonDisabled={true}
            selectedHierarchyContext={""}
            filterContext={oSearchCriteriaData.filterContext}
            isEditMode={false}
            isHierarchyMode={false}
            taxonomyTreeRequestData={oSearchCriteriaData.taxonomyTreeRequestData}
        />)
  };

  /** Modification of Skeleton **/
  skeletonModification = (aTemp, aSkeletonInfo) => {
    let aTempProcessInfo = [];
    CS.forEach(aSkeletonInfo, function (oSkeletonInfo) {
      if(!aTemp.includes(oSkeletonInfo.key)){
        aTempProcessInfo.push(oSkeletonInfo);
      }
    });
   return aTempProcessInfo;
  };

  getProcessDetailedView = () => {
    let _props = this.props;
    var oActiveProcess = _props.activeProcess;
    if (CS.isEmpty(oActiveProcess) || oActiveProcess.isCreated) {
      return null;
    }
    var aDisabledFields = ["code"];
    let oSectionLayout = new SectionLayout();
    let aProcessBasicInformation = oSectionLayout.processBasicInformation;

    if (oActiveProcess.eventType === ProcessEventTypeDictionary.BUSINESS_PROCESS_EVENT &&
        oActiveProcess.workflowType === MockDataForWorkflowTypesDictionary.STANDARD_WORKFLOW ||
        oActiveProcess.workflowType === MockDataForWorkflowTypesDictionary.TASKS_WORKFLOW) {
      if (oActiveProcess.triggeringType === "afterCreate") {
        let aTemp = ["attributes", "tags", "taxonomy", "actionSubType", "nonNatureklassTypes"];
        let aBasicInfoProcess = aProcessBasicInformation[0].elements;
        aProcessBasicInformation[0].elements = this.skeletonModification(aTemp, aBasicInfoProcess);
      } else {
        if (!CS.includes(oActiveProcess.actionSubType, MockDataForProcessActionTypesDictionary.AFTER_PROPERTIES_SAVE)) {
          if (CS.includes(oActiveProcess.actionSubType, MockDataForProcessActionTypesDictionary.AFTER_CLASSIFICATION_SAVE)) {
            let aTemp = ["attributes", "tags"];
            let aBasicInfoProcess = aProcessBasicInformation[0].elements;
            aProcessBasicInformation[0].elements = this.skeletonModification(aTemp, aBasicInfoProcess);
          } else if (CS.includes(oActiveProcess.actionSubType, MockDataForProcessActionTypesDictionary.AFTER_RELATIONSHIP_SAVE)) {
            let aTemp = ["attributes", "tags", "taxonomy", "nonNatureklassTypes"];
            let aBasicInfoProcess = aProcessBasicInformation[0].elements;
            aProcessBasicInformation[0].elements = this.skeletonModification(aTemp, aBasicInfoProcess);
          }
        }
      }
    }

    if (oActiveProcess.eventType == ProcessEventTypeDictionary.INTEGRATION) {
      let aTemp = ["actionSubType", "taxonomy", "klassTypes", "attributes", "tags", "nonNatureklassTypes", "usecases"];
      let aBasicInfoProcess =  aProcessBasicInformation[0].elements;
      aProcessBasicInformation[0].elements = this.skeletonModification(aTemp,aBasicInfoProcess);
    }
    if(oActiveProcess.workflowType !== MockDataForWorkflowTypesDictionary.SCHEDULED_WORKFLOW){
      let aTemp = ["isTemplate","physicalCatalogIds"];
      let aBasicInfoProcess = aProcessBasicInformation[0].elements;
      aProcessBasicInformation[0].elements = this.skeletonModification(aTemp, aBasicInfoProcess);
      aProcessBasicInformation.splice(1, 1);// disable frequency
    }
    if(oActiveProcess.workflowType === MockDataForWorkflowTypesDictionary.SCHEDULED_WORKFLOW || oActiveProcess.workflowType === MockDataForWorkflowTypesDictionary.JMS_WORKFLOW) {
      let aTemp = ["eventType","triggeringEventType", "actionSubType", "taxonomy", "klassTypes", "attributes", "tags", "nonNatureklassTypes"];
      let aBasicInfoProcess =  aProcessBasicInformation[0].elements;
      aProcessBasicInformation[0].elements = this.skeletonModification(aTemp,aBasicInfoProcess);
    }
    let aButtonData = [];

    if (_props.isProcessDirty) {
      aButtonData = [
        {
          id: "detailed_process_discard",
          label: getTranslation().DISCARD,
          isDisabled: false,
          isFlat: false,
        },
        {
          id: "detailed_process_save",
          label: getTranslation().SAVE,
          isDisabled: false,
          isFlat: false,
        }];
    } else {
      aButtonData = [{
        id: "detailed_process_close",
        label: getTranslation().OK,
        isFlat: false,
        isDisabled: false
      }];
    }

    var oBodyStyle = {
      padding: 0,
      maxHeight: 'none'
    };
    var oContentStyle = {
      transform: 'none',
      width: '94%',
      maxWidth: 'none',
      overflowY: 'unset'
    };

    return (
        <CustomDialogView modal={true} open={true}
                          bodyClassName="processDetailDialogContainer"
                          contentClassName="processDetailDialog"
                          buttonData={aButtonData}
                          contentStyle={oContentStyle}
                          bodyStyle={oBodyStyle}
                          title={getTranslation().WORKFLOW_DETAILS}
                          onRequestClose={this.handleDialogButtonClicked.bind(this, aButtonData[0].id)}
                          buttonClickHandler={this.handleDialogButtonClicked}>

          <div className="processDetailView">
            <CommonConfigSectionView context="process"
                                     sectionLayout={oSectionLayout.processBasicInformation}
                                     data={_props.sectionLayoutModel}
                                     disabledFields={aDisabledFields}/>

          </div>
        </CustomDialogView>)
  };

  getGridViewDOM = () => {
    var oProcessDetailedView = this.getProcessDetailedView();
    let oAdvancedSearchPanelDOM = this.getAdvancedSearchPanelView();
    let oSearchCriteriaMapViewDOM = this.getSearchCriteriaMapView();

    var oActiveProcess = this.props.activeProcess;
    let oCreateDialog = this.getCreateDialog(oActiveProcess, 'process');
    let oSaveAsDialog = this.getSaveAsDialog();

    /**Grid Required Data */
    let sContext = this.props.context;
    let bShowCheckboxColumn = this.props.showCheckboxColumn;
    let bDisableDeleteButton = this.props.disableDeleteButton;
    let bEnableImportExportButton = this.props.enableImportExportButton;
    let bDisableCreate = this.props.disableCreate;
    let bEnableCopyButton = this.props.enableCopyButton;
    let oColumnOrganizerData = this.props.columnOrganizerData;
    let aGridWFValidationErrorList = this.props.gridWFValidationErrorList;
    let aFilterData = this.props.filterData;

    return (
        <div className="configGridViewContainer processConfigWrapper" key="contextConfigGridViewContainer">
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
              gridWFValidationErrorList={aGridWFValidationErrorList}
              filterData={aFilterData}
          />
          {oProcessDetailedView}
          {oCreateDialog}
          {oSaveAsDialog}
          {oAdvancedSearchPanelDOM}
          {oSearchCriteriaMapViewDOM}
        </div>
    )
  }

  render () {
    let oGridViewDOM =this.getGridViewDOM();

    return (
        <React.Fragment>
          {oGridViewDOM}
        </React.Fragment>
    )
  }
}

export const view = ProcessConfigView;
export const events = oEvents;
