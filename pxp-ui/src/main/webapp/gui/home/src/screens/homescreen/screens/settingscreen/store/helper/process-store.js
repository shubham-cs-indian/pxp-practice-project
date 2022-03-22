import CS from '../../../../../../libraries/cs';
import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import { TranslationsRequestMapping, ProcessRequestMapping } from '../../tack/setting-screen-request-mapping';
import ProcessConfigViewProps from './../model/process-config-view-props';
import SettingUtils from './../helper/setting-utils';
import Constants from './../../../../../../commonmodule/tack/constants';
import ProcessTypeDictionary from './../../../../../../commonmodule/tack/process-type-dictionary';
import ConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import ProcessEventTypeDictionary from './../../../../../../commonmodule/tack/process-event-type-dictionary';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import MockDataForProcessInputParametersFactory from '../../tack/mock-data-for-process-input-parameters';
import MockDataForCustomUserTasks from '../../tack/mock-data-for-custom-user-tasks';
import ProcessConstants from './../../tack/process-constants';
import GridViewStore from '../../../../../../viewlibraries/contextualgridview/store/grid-view-store';
import Moment from 'moment';
import ElementHelper from 'bpmn-js-properties-panel/lib/helper/ElementHelper';
import oReplaceOptions from 'bpmn-js/lib/features/replace/ReplaceOptions';
import BpmnModeler from 'bpmn-js/lib/Modeler';
import ExceptionLogger from '../../../../../../libraries/exceptionhandling/exception-logger';
import GridViewContexts from '../.././../../../../commonmodule/tack/grid-view-contexts';
import ProcessGridViewSkeleton from '../../tack/process-config-grid-view-skeleton';
import SettingScreenProps from './../model/setting-screen-props';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import { SettingsRequestMapping as oSettingScreenRequestMapping } from '../../tack/setting-screen-request-mapping';
import MockDataXMLDataForCamunda from '../../tack/mock/mock-xml-data-for-camunda';
import ConfigDataEntitiesDictionary from '../../../../../../commonmodule/tack/config-data-entities-dictionary';
import ProcessFilterStore from '../../store/helper/process-filter-store';
import MeasurementMetricBaseTypeDictionary from '../../../../../../commonmodule/tack/measurement-metric-base-type-dictionary';
import TagTypeConstants from './../../../../../../commonmodule/tack/tag-type-constants';
import ProcessFilterProps from '../../store/model/process-filter-props';
import SaveAsDialogGridViewSkeleton from "../../tack/save-as-dialog-grid-view-skeleton";
import MockDataForWorkflowTypesDictionary from '../../tack/mock/mock-data-for-workflow-types-dictionary';
import CustomMockDataForWorkflowReplaceOptions from "../../tack/mock/mock-data-for-process-replace-options";
import CommonUtils from "../../../../../../commonmodule/util/common-utils";
import ProcessConfigCallActivityDictionary from '../../../../../../commonmodule/tack/process-config-call-activity-dictionary';
import MockDataForFrequencyTypesDictionary from '../../tack/mock/mock-data-for-frequency-dictionary';
const MockDataForProcessInputParameters = MockDataForProcessInputParametersFactory();

var ProcessStore = (function () {

  var _triggerChange = function () {
    ProcessStore.trigger('process-changed');
  };

  var failureFetchProcessList = function (oResponse) {
    SettingUtils.failureCallback(oResponse, 'failureFetchProcessList', getTranslation());
  };

  let _getCustomReplaceOptionObject = function (oCustomServiceTask,sDelegateExp, sCustomElementType, sActionName, sIconClassName, sTargetType) {
    let sId = oCustomServiceTask.id;
    return {
      customElementID: sId,
      label: oCustomServiceTask.label,
      customElementType: sCustomElementType,
      delegateExpression: sDelegateExp,
      actionName: sActionName + SettingUtils.getSplitter() + sId,
      className: sIconClassName,
      target: {
        type: sTargetType
      }
    }
  };

  let _getCustomReplaceOptionObjectForCallActivity = function (oCustomServiceTask, sCalledElement, sCustomElementType, sActionName, sIconClassName, sTargetType) {
    let sId = oCustomServiceTask.id;
    return {
      customElementID: sId,
      label: oCustomServiceTask.label,
      customElementType: sCustomElementType,
      calledElement: sCalledElement,
      actionName: sActionName + SettingUtils.getSplitter() + sId,
      className: sIconClassName,
      target: {
        type: sTargetType
      }
    }
  };

  let _setUpBPNMModlerCustomReplaceOptions = function (aCustomServiceTasks) {
    //put all other replace menu options in here only
    let oCustomReplaceMenuOptions = {TASK: []};
    let aTaskReplaceMenuOptions = [];

    //remove previously added custom elements
    CS.forEach(oReplaceOptions, function (aReplaceOptions) {
      CS.remove(aReplaceOptions, function (oReplaceOption) {
        return !CS.isEmpty(oReplaceOption.customElementType)
      });
    });

    ///adding service tasks and call activity
    CS.forEach(aCustomServiceTasks, function (oCustomServiceTask) {
      let sDelegateExp = '${camundaTask}'; /** Default Delegate Expression **/
      if (oCustomServiceTask.iconClassName == "CALL_ACTIVIY_TASK") {
        aTaskReplaceMenuOptions.push(_getCustomReplaceOptionObjectForCallActivity(oCustomServiceTask, ProcessConfigCallActivityDictionary[oCustomServiceTask.id], ProcessConstants.CUSTOM_ELEMENT_TYPE_CALL_ACTIVITY, ProcessConstants.REPLACE_ACTION_CALL_ACTIVITY, ProcessConstants.CALL_ACTIVITY, ProcessConstants.TARGET_CALL_ACTIVITY));
      } else {
        aTaskReplaceMenuOptions.push(_getCustomReplaceOptionObject(oCustomServiceTask, sDelegateExp, ProcessConstants.CUSTOM_ELEMENT_TYPE_SERVICE_TASK, ProcessConstants.REPLACE_ACTION_SERVICE_TASK, ProcessConstants[oCustomServiceTask.iconClassName], ProcessConstants.TARGET_SERVICE_TASK));
      }
    });

    //adding user tasks
    CS.forEach(MockDataForCustomUserTasks(), function (oCustomUserTask) {
      aTaskReplaceMenuOptions.push(_getCustomReplaceOptionObject(oCustomUserTask, null, ProcessConstants.CUSTOM_ELEMENT_TYPE_USER_TASK, ProcessConstants.REPLACE_ACTION_USER_TASK, ProcessConstants.ICON_CLASS_USER_TASK, ProcessConstants.TARGET_USER_TASK));
    });
    oCustomReplaceMenuOptions.TASK = aTaskReplaceMenuOptions;

    //Sorting the Custom components list.
    oCustomReplaceMenuOptions.TASK = CS.sortBy(aTaskReplaceMenuOptions , 'label') || [];

    //assigning replace options
    CS.forEach(oCustomReplaceMenuOptions, function (aValuesToReplace, sKey) {
      oReplaceOptions[sKey] && oReplaceOptions[sKey].push(...aValuesToReplace);
    });
  };

  var successGetProcessCallback = function (oCallback,oResponse) {
    var oActiveProcess = oResponse.success;
    ProcessConfigViewProps.setValidationInfo(oActiveProcess.validationInfo);
    if(CS.isEmpty(oActiveProcess.validationInfo) || oActiveProcess.validationInfo.isWorkflowValid) {
      ProcessConfigViewProps.setActiveProcess(oActiveProcess);
      let oConfigDetails = oActiveProcess.configDetails;
      if (oActiveProcess.workflowType === MockDataForWorkflowTypesDictionary.SCHEDULED_WORKFLOW) {
        let sFrequencyActiveTabId = CS.isEmpty(oActiveProcess.frequencyActiveTabId) ? MockDataForFrequencyTypesDictionary.DURATION : oActiveProcess.frequencyActiveTabId;
        ProcessConfigViewProps.setSelectedTabId(sFrequencyActiveTabId);
        CS.isNotEmpty(oActiveProcess.frequencyActiveTabId) && _fillFrequencyTabData(oActiveProcess);
      }
      ProcessConfigViewProps.setLoadedAttributes(oConfigDetails.referencedAttributes);
      ProcessConfigViewProps.setLoadedTags(oConfigDetails.referencedTags);
      let aProcessedGridViewData = ProcessConfigViewProps.getProcessGridViewData();
      /** Active process pushed in grid data**/
      let iIndex = CS.findIndex(aProcessedGridViewData, {id: oActiveProcess.id});
      if (iIndex != -1) {
        aProcessedGridViewData[iIndex] = oActiveProcess;
      } else {
        aProcessedGridViewData.push(oActiveProcess);
      }

      let oGridConfigDetailsData = ProcessConfigViewProps.getConfigDetailsData();
      ProcessConfigViewProps.setReferencedTaxonomiesForComponent(oActiveProcess.configDetails.referencedTaxonomies);
      let oGridTaxonomies = oGridConfigDetailsData.referencedTaxonomies;
      let oGridKlasses = oGridConfigDetailsData.referencedKlasses;
      let oGridAttributes = oGridConfigDetailsData.referencedAttributes;
      let oGridTags = oGridConfigDetailsData.referencedTags;
      CS.assign(oGridTaxonomies, oActiveProcess.configDetails.referencedTaxonomies);
      CS.assign(oGridKlasses, oActiveProcess.configDetails.referencedKlasses);
      CS.assign(oGridAttributes, oActiveProcess.configDetails.referencedAttributes);
      CS.assign(oGridTags, oActiveProcess.configDetails.referencedTags);
      let oModelerInstance = ProcessConfigViewProps.getActiveBPMNModelerInstance();
      if (!CS.isEmpty(oModelerInstance)) {
        oModelerInstance.getDefinitions && ProcessConfigViewProps.setPureProcessDefinitionJSON(CS.cloneDeep(oModelerInstance.getDefinitions()))
      }
      //Master object processing
      var oAppData = SettingUtils.getAppData();
      var oProcessList = oAppData.getProcessList();
      oProcessList[oActiveProcess.id] = oActiveProcess;

      //Value List Processing
      var oProcessValueList = ProcessConfigViewProps.getProcessValueList();
      var oProcessValueObj = _createProcessValueObject(oActiveProcess.id, oProcessValueList[oActiveProcess.id]);
      SettingUtils.applyValueToAllTreeNodes(oProcessValueList, 'isSelected', false);
      oProcessValueObj.isSelected = true;
      oProcessValueList[oActiveProcess.id] = oProcessValueObj;


      let sContext = GridViewContexts.PROCESS;
      let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(sContext);

      if (oCallback.isCreated) {
        let aGridViewData = oGridViewPropsByContext.getGridViewData();
        let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(sContext, [oActiveProcess], oActiveProcess.configDetails)[0];
        aGridViewData.unshift(aProcessedGridViewData);
        oGridViewPropsByContext.setGridViewTotalItems(oGridViewPropsByContext.getGridViewTotalItems() + 1);
        SettingUtils.getComponentProps().screen.setListItemCreationStatus(true);
        ProcessConfigViewProps.setSelectedComponent({});
        /** To open edit view immediately after create */
        ProcessConfigViewProps.setIsProcessDialogActive(true);
      } else {
        let aProcessList = CS.values(oProcessList);
        let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(sContext, aProcessList, oActiveProcess.configDetails);
        oGridViewPropsByContext.setGridViewData(aProcessedGridViewData);
      }
      _setProcessScreenLockStatus(false);

      if (oCallback && oCallback.alertifyFunction) {
        oCallback.alertifyFunction();
      }

      if (oCallback.isSaved) {
        if (oActiveProcess.isExecutable) {
          SettingUtils.showSuccess(getTranslation().WORKFLOW_IS_VALID_AND_SAVED_SUCCESSFULLY);
        } else {
          SettingUtils.showSuccess(getTranslation().WORKFLOW_SUCCESSFULLY_SAVED);
        }
      }

      if (oCallback && oCallback.functionToExecute) {
        oCallback.functionToExecute();
      }
      // _setAllProcessComponents();

    } else if (CS.isNotEmpty(oActiveProcess.validationInfo.validationDetails)) {
      SettingUtils.showError(getTranslation().WORKFLOW_IS_INVALID_AND_COULD_NOT_BE_SAVED);
    }
    ProcessConfigViewProps.setShouldImportXML(true); //Import XML to reload the Workflow css everytime.
    if(oActiveProcess.eventType === ProcessEventTypeDictionary.INTEGRATION && oActiveProcess.workflowType === MockDataForWorkflowTypesDictionary.SCHEDULED_WORKFLOW){
      ProcessConfigViewProps.setSelectedTabId(MockDataForFrequencyTypesDictionary.DURATION);
    }
    _fetchProcessList();
  };

  let _setSearchCriteria = (oModelerInstance, sContext) =>{
    let oActiveBPMNElement = ProcessConfigViewProps.getActiveBPMNElement();
    if(CS.isNotEmpty(oActiveBPMNElement)) {
      let oBusinessObject = oActiveBPMNElement.businessObject;
      if (CS.isNotEmpty(oBusinessObject) && oBusinessObject.hasOwnProperty("calledElement") && oBusinessObject.calledElement === "STD_PXON_EXPORT") {
        let oExtensionElements = oBusinessObject.extensionElements;
        let oValues = oExtensionElements.values[0];
        let oInputParameters = oValues.inputParameters;
        let oFoundElement = CS.find(oInputParameters, {name: "MANUAL_SEARCH_CRITERIA"});
        if (CS.isNotEmpty(oFoundElement) && oFoundElement.value === "true") {
          let oSearchCrtieria = CS.find(oInputParameters, {name: "SEARCH_CRITERIA"});
          oSearchCrtieria = JSON.parse(oSearchCrtieria.value);
          let oExportSubType = CS.find(oInputParameters, {name: "EXPORT_SUB_TYPE"});
          oSearchCrtieria.exportSubType = oExportSubType.value;
          let oSelectedBaseTypes = CS.find(oInputParameters, {name: "SELECTED_BASETYPES"});
          let aItems = oSelectedBaseTypes.definition.items;
          let aList = [];
          CS.forEach(aItems, function (oItem) {
            aList.push(oItem.value);
          });
          oSearchCrtieria.selectedBaseTypes = aList;
          ProcessFilterProps.setSearchCriteria(oSearchCrtieria);
          if(sContext){
            _handleBPMNCustomElementSingleValueChanged("SEARCH_CRITERIA",JSON.stringify(oSearchCrtieria));
          }
        }
      }
    }
  };

  var successSaveProcessCallback = function (oCallback,oResponse) {
    var oActiveProcess = oResponse.success;
    ProcessConfigViewProps.setValidationInfo(oActiveProcess.validationInfo);
    if(CS.isEmpty(oActiveProcess.validationInfo) || oActiveProcess.validationInfo.isWorkflowValid) {
      ProcessConfigViewProps.setActiveProcess(oActiveProcess);
      let oConfigDetails = oActiveProcess.configDetails;
      ProcessConfigViewProps.setLoadedAttributes(oConfigDetails.referencedAttributes);
      ProcessConfigViewProps.setLoadedTags(oConfigDetails.referencedTags);
      if (oActiveProcess.workflowType === MockDataForWorkflowTypesDictionary.SCHEDULED_WORKFLOW) {
        ProcessConfigViewProps.setSelectedTabId(oActiveProcess.frequencyActiveTabId);
        _fillFrequencyTabData(oActiveProcess);
      }
      let aProcessedGridViewData = ProcessConfigViewProps.getProcessGridViewData();
      /** Active process pushed in grid data**/
      let iIndex = CS.findIndex(aProcessedGridViewData, {id: oActiveProcess.id});
      if (iIndex != -1) {
        aProcessedGridViewData[iIndex] = oActiveProcess;
      } else {
        aProcessedGridViewData.push(oActiveProcess);
      }

      let oGridConfigDetailsData = ProcessConfigViewProps.getConfigDetailsData();
      ProcessConfigViewProps.setReferencedTaxonomiesForComponent(oActiveProcess.configDetails.referencedTaxonomies);
      let oGridTaxonomies = oGridConfigDetailsData.referencedTaxonomies;
      let oGridKlasses = oGridConfigDetailsData.referencedKlasses;
      let oGridAttributes = oGridConfigDetailsData.referencedAttributes;
      let oGridTags = oGridConfigDetailsData.referencedTags;
      CS.assign(oGridTaxonomies, oActiveProcess.configDetails.referencedTaxonomies);
      CS.assign(oGridKlasses, oActiveProcess.configDetails.referencedKlasses);
      CS.assign(oGridAttributes, oActiveProcess.configDetails.referencedAttributes);
      CS.assign(oGridTags, oActiveProcess.configDetails.referencedTags);
      let oModelerInstance = ProcessConfigViewProps.getActiveBPMNModelerInstance();
      if (!CS.isEmpty(oModelerInstance)) {
        oModelerInstance.getDefinitions && ProcessConfigViewProps.setPureProcessDefinitionJSON(CS.cloneDeep(oModelerInstance.getDefinitions()))
        _setSearchCriteria(oModelerInstance);
      }
      //Master object processing
      var oAppData = SettingUtils.getAppData();
      var oProcessList = oAppData.getProcessList();
      oProcessList[oActiveProcess.id] = oActiveProcess;

      //Value List Processing
      var oProcessValueList = ProcessConfigViewProps.getProcessValueList();
      var oProcessValueObj = _createProcessValueObject(oActiveProcess.id, oProcessValueList[oActiveProcess.id]);
      SettingUtils.applyValueToAllTreeNodes(oProcessValueList, 'isSelected', false);
      oProcessValueObj.isSelected = true;
      oProcessValueList[oActiveProcess.id] = oProcessValueObj;


      let sContext = GridViewContexts.PROCESS;
      let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(sContext);

      if (oCallback.isCreated) {
        let aGridViewData = oGridViewPropsByContext.getGridViewData();
        let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(sContext, [oActiveProcess], oActiveProcess.configDetails)[0];
        aGridViewData.unshift(aProcessedGridViewData);
        oGridViewPropsByContext.setGridViewTotalItems(oGridViewPropsByContext.getGridViewTotalItems() + 1);
        SettingUtils.getComponentProps().screen.setListItemCreationStatus(true);
        ProcessConfigViewProps.setSelectedComponent({});
        /** To open edit view immediately after create */
        ProcessConfigViewProps.setIsProcessDialogActive(true);
      } else {
        let aProcessList = CS.values(oProcessList);
        let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(sContext, aProcessList, oActiveProcess.configDetails);
        oGridViewPropsByContext.setGridViewData(aProcessedGridViewData);
      }
      _setProcessScreenLockStatus(false);

      if (oCallback && oCallback.alertifyFunction) {
        oCallback.alertifyFunction();
      }

      if (oCallback.isSaved) {
        if (oActiveProcess.isExecutable) {
          SettingUtils.showSuccess(getTranslation().WORKFLOW_IS_VALID_AND_SAVED_SUCCESSFULLY);
        } else {
          SettingUtils.showSuccess(getTranslation().WORKFLOW_SUCCESSFULLY_SAVED);
        }
      }

      if (oCallback && oCallback.functionToExecute) {
        oCallback.functionToExecute();
      }
      // _setAllProcessComponents();

    } else if (CS.isNotEmpty(oActiveProcess.validationInfo.validationDetails)) {
      SettingUtils.showError(getTranslation().WORKFLOW_IS_INVALID_AND_COULD_NOT_BE_SAVED);
    }
    ProcessConfigViewProps.setShouldImportXML(true); //Import XML to reload the Workflow css everytime.
    _fetchProcessList();
  };

  var failureSaveProcessCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, 'failureSaveProcessCallback', getTranslation());
  };

  var successDeleteProcessCallback = function (oCallbackData, oResponse) {
    var aSuccessIds = oResponse.success;
    //Handle Failure
    var aFailureIds = oResponse.failure ? oResponse.failure.exceptionDetails : [];

    /** In case backEnd fails to send the deleted IDs at all then following code will work (Hack) - Shashank*/
    /*if(!aSuccessIds.length){
      var aTotalSentToDeleteIds = oCallbackData.deletedIds;
      aSuccessIds = CS.difference(aTotalSentToDeleteIds, aFailureIds);
    }*/

    var oAppData = SettingUtils.getAppData();
    var oProcessList = oAppData.getProcessList();
    var oProcessValueList = ProcessConfigViewProps.getProcessValueList();
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.PROCESS);
    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    let oGridViewSkeleton = oGridViewPropsByContext.getGridViewSkeleton();

    CS.forEach(aSuccessIds, function (sId) {
      CS.remove(aGridViewData, {id: sId});
      CS.pull(oGridViewSkeleton.selectedContentIds, sId);
      delete oProcessList[sId];
      delete oProcessValueList[sId];
    });

    let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.PROCESS, oProcessList);
    oGridViewPropsByContext.setGridViewData(aProcessedGridViewData);
    oGridViewPropsByContext.setGridViewTotalItems(oGridViewPropsByContext.getGridViewTotalItems() - aSuccessIds.length);

    ProcessConfigViewProps.setActiveProcess({});
    alertify.success(getTranslation().WORKFLOW_DELETE_SUCCESSFUL);
    _setProcessScreenLockStatus(false);

  };

  var failureDeleteProcessCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, 'failureDeleteProcessCallback', getTranslation());
  };

  var _setProcessValueList = function (aProcessList) {
    var oProcessAttributeList = {};
    CS.forEach(aProcessList, function (oListNode) {
      oProcessAttributeList[oListNode.id] = _createProcessValueObject(oListNode.id);
    });

    ProcessConfigViewProps.setProcessValueList(oProcessAttributeList);
  };

  let _fetchProcessList = function (bLoadMore) {
    let oPostData = GridViewStore.getPostDataToFetchList(GridViewContexts.PROCESS);
    oPostData.filterData = _getFilterData();
    SettingUtils.csPostRequest(ProcessRequestMapping.GetProcessListForGrid, {}, oPostData, successFetchProcessListForGridView, failureFetchProcessList);
  };

  let _fetAllProcessComponents = function (oPostData) {
    SettingUtils.csPostRequest(ProcessRequestMapping.GetAllProcessComponents, {}, oPostData, successFetchAllProcessComponents, failureFetchAllProcessComponents)
  };

  let successFetchAllProcessComponents = function (oResponse) {
    let oComponentMap = oResponse.success.tasksMap;
    let aComponentWithTranslations = _getTranslationsForComponentIds(oComponentMap);
    _setUpBPNMModlerCustomReplaceOptions(aComponentWithTranslations);
    _triggerChange();
  };

  let _getTranslationsForComponentIds = function (oComponentMap) {
    let aUpdatedComponents = [];
    CS.forEach(oComponentMap, function (aList, skey) {
      if (skey != ProcessConstants.BGP_TASK && skey != ProcessConstants.EVENT_TASK && skey != ProcessConstants.DRAFT_TASK) {
        CS.forEach(aList, function (sId) {
          /** discard **/
          if(getTranslation()[sId]) {
            let oTemp = {};
            oTemp.id = sId;
            oTemp.label = getTranslation()[sId];
            oTemp.iconClassName = skey;
            aUpdatedComponents.push(oTemp);
          }
        });
      }
    });
    return aUpdatedComponents;
  };

  let failureFetchAllProcessComponents = function (oResponse) {
    SettingUtils.failureCallback(oResponse,"failureFetchAllProcessComponents",getTranslation());
  };

  var _getProcessScreenLockStatus = function () {
    return ProcessConfigViewProps.getProcessScreenLockStatus();
  };

  var _createProcessValueObject = function (sId, oVisualItem) {
    return {
      id: sId || "",
      isChecked: oVisualItem && oVisualItem.isChecked || false,
      isEditable: oVisualItem && oVisualItem.isEditable || false,
      isSelected: oVisualItem && oVisualItem.isSelected || false
    };
  };

  var _createUntitledProcessMasterObject = function () {
    let bIsCreated = true;
    let sEvent = ProcessEventTypeDictionary.BUSINESS_PROCESS_EVENT;
    let sWorkflowType = MockDataForWorkflowTypesDictionary.STANDARD_WORKFLOW;
    let sProcessType = ProcessTypeDictionary.ONBOARDING_PROCESS;
    let sActionType = "";
    if (SettingUtils.isOffboarding()) {
      sEvent = Constants.OFFBOARDING_EXPORT_EVENT;
      sProcessType = ProcessTypeDictionary.OFFBOARDING_PROCESS;
    } else if (!SettingUtils.isOnboarding()) {
      bIsCreated = true;
    }
    if(sEvent === ProcessEventTypeDictionary.BUSINESS_PROCESS_EVENT){
      sActionType = "afterSave";
    }
    if(sEvent === ProcessEventTypeDictionary.INTEGRATION){
      sActionType = "import";
    }

    return {
      id: UniqueIdentifierGenerator.generateUUID(),
      label: UniqueIdentifierGenerator.generateUntitledName(),
      eventType: sEvent,
      processType: sProcessType,
      isCreated: bIsCreated,
      code: "",
      workflowType: sWorkflowType,
      triggeringType: sActionType,
      isTemplate: false
    }
  };

  var _setProcessScreenLockStatus = function (bStatus) {
    ProcessConfigViewProps.setProcessScreenLockStatus(bStatus);
  };

  var _cancelProcessCreation = function () {
    var oProcessValueMap = ProcessConfigViewProps.getProcessValueList();
    var oProcessMap = SettingUtils.getAppData().getProcessList();

    var oNewProcessToCreate = ProcessConfigViewProps.getActiveProcess();

    delete oProcessValueMap[oNewProcessToCreate.id];
    delete oProcessMap[oNewProcessToCreate.id];
    ProcessConfigViewProps.setActiveProcess({});
    ProcessConfigViewProps.setProcessScreenLockStatus(false);
    _triggerChange();
  };

  let _closeProcessDialog = function () {
    var oProcessValueMap = ProcessConfigViewProps.getProcessValueList();
    var oActiveProcess = ProcessConfigViewProps.getActiveProcess();
    let oListObject = SettingUtils.getDefaultListProps();
    oListObject.id = oActiveProcess.id;
    oProcessValueMap[oActiveProcess.id] = oListObject;
    ProcessConfigViewProps.setActiveProcess({});
    ProcessConfigViewProps.resetBPMNProperties();
    _resetFrequencyTabData();
    _triggerChange();
  };

  let _handleSaveAsDialogCloseButtonClicked = function () {
    let oGridViewProps = GridViewStore.getGridViewPropsByContext(GridViewContexts.PROCESS);
    let oGridSkeleton = oGridViewProps.getGridViewSkeleton();
    oGridSkeleton.selectedContentIds = [];
    ProcessConfigViewProps.setIsSaveAsDialogActive(false);
    ProcessConfigViewProps.setCodeDuplicates([]);
    _triggerChange();
  };

  let _handleSaveAsDialogSaveButtonClicked = function () {
    let aGridViewData = ProcessConfigViewProps.getDataForSaveAsDialog();
    let aDataToSave = prepareDataForSaveAs(aGridViewData);
    if (CS.isEmpty(aDataToSave)) {
      alertify.error(getTranslation().NAME_IS_EMPTY);
    } else {
      let oCallbackData = {};
      let aListOfCodesToCheck = [];
      let aListOfNamesToCheck = [];
      let oPostData = [];
      oCallbackData.isSaveAsDialogActive = true;
      oCallbackData.functionToExecuteAfterCodeCheck = _saveAsProcessCall.bind(this, aDataToSave, oCallbackData);
      CS.forEach(aDataToSave, function (oElement) {
        let bIsValidEntityCode = SettingUtils.isValidEntityCode(oElement.code);
        if (bIsValidEntityCode) {
          aListOfCodesToCheck.push(oElement.code);
        }
        let bIsValidEntityName = CS.isNotEmpty(CS.trim(oElement.label)) ? true : false;
        if (bIsValidEntityName) {
          aListOfNamesToCheck.push(oElement.label);
        }
      });

      let oData = {};
      oData.codes = aListOfCodesToCheck;
      oData.entityType = ConfigEntityTypeDictionary.ENTITY_TYPE_PROCESS_EVENT;
      oData.names = aListOfNamesToCheck;
      oPostData.push(oData);
      if (CS.isNotEmpty(aListOfNamesToCheck) && CS.isNotEmpty(aListOfCodesToCheck)) {
        SettingUtils.csPostRequest(ProcessRequestMapping.BulkCodeCheck, {}, oPostData, successBulkCodeCheckList.bind(this, oCallbackData), failureBulkCodeCheckList);
      } else {
        if (CS.isEmpty(aListOfNamesToCheck)) {
          alertify.error(getTranslation().NAME_IS_EMPTY);
        }
        if (CS.isEmpty(aListOfCodesToCheck)) {
          ProcessConfigViewProps.setCodeDuplicates(aListOfCodesToCheck);
          alertify.error(getTranslation().PLEASE_ENTER_VALID_CODE);
        }
      }
    }
    _triggerChange();
  };

  let _saveAsProcessCall = function(aDataToSave,oCallbackData){
    oCallbackData.isSaveAsCall = true;
    SettingUtils.csPostRequest(ProcessRequestMapping.SaveAs, {}, aDataToSave, successBulkSaveProcessList.bind(this, oCallbackData), failureSaveAs);
    ProcessConfigViewProps.setIsSaveAsDialogActive(false);
  };

  /**Function to check the timer Start Expression**/
  let _checkTimerStartExpression = function(aProcessToSave){
    let bIsTimerStartExpressionIsNull = false;
    CS.forEach(aProcessToSave, function (oProcessToSave) {
      if(CS.isEmpty(oProcessToSave.timerStartExpression)){
        bIsTimerStartExpressionIsNull = true;
      }
    });
    return bIsTimerStartExpressionIsNull;
  };

  let failureBulkCodeCheckList = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureBulkCodeCheckList", getTranslation());
  };

  let successBulkCodeCheckList = function (oCallBackData, oResponse) {
    let oSuccess = oResponse.success;
    let oCodeProcess_Event = oSuccess.codeCheck.Process_Event;
    let oNameProcess_Event = oSuccess.nameCheck.Process_Event;
    let aDuplicateCodes = [];
    let aDuplicateNames = [];
    CS.forEach(oCodeProcess_Event, function (bValue, sKey) {
      if (!bValue) {
        aDuplicateCodes.push(sKey)
      }
    });

    CS.forEach(oNameProcess_Event, function (bValue, sKey) {
      if (!bValue) {
        aDuplicateNames.push(sKey)
      }
    });
    ProcessConfigViewProps.setCodeDuplicates(aDuplicateCodes);
    if (CS.isEmpty(aDuplicateCodes) && CS.isEmpty(aDuplicateNames)) {
      oCallBackData && oCallBackData.functionToExecuteAfterCodeCheck && oCallBackData.functionToExecuteAfterCodeCheck();
      if (oCallBackData.serverCallback) {
        let oServerCallback = oCallBackData.serverCallback;
        if (oServerCallback.isBulkSave) {
          if (!oServerCallback.isJMSProp) {
            let bCheckTimerStartExpression = _checkTimerStartExpression(oServerCallback.processToSave);
            if((oServerCallback.processToSave.workflowType === MockDataForWorkflowTypesDictionary.SCHEDULED_WORKFLOW) && bCheckTimerStartExpression) {
              alertify.error(getTranslation().EXCEPTION_FOR_TIMERDEFINITIONTYPE_AND_TIMERSTARTEXPRESSION);
            }else {
              SettingUtils.csPostRequest(oServerCallback.requestURL, {}, oServerCallback.processToSave, successBulkSaveProcessList.bind(this, oServerCallback), failureBulkSaveProcessList);
            }
          } else {
            alertify.error(getTranslation().JMS_WORKFLOW_MANDATORY_MESSAGE);
          }
        } else if (oServerCallback.isCreated) {
          oServerCallback.functionToExecute && oServerCallback.functionToExecute();
        }
        else {
          if (!oCallBackData.isJMSProp) {
            if(CS.isEmpty(oServerCallback.processToSave.timerStartExpression) && (oServerCallback.processToSave.workflowType === MockDataForWorkflowTypesDictionary.SCHEDULED_WORKFLOW)) {
              alertify.error(getTranslation().EXCEPTION_FOR_TIMERDEFINITIONTYPE_AND_TIMERSTARTEXPRESSION);
            }else{
              SettingUtils.csPostRequest(oServerCallback.requestURL, {}, oServerCallback.processToSave, successSaveProcessCallback.bind(this, oCallBackData), failureSaveProcessCallback);
            }
          } else {
            alertify.error(getTranslation().JMS_WORKFLOW_MANDATORY_MESSAGE);
          }
        }
      }
    } else {
      if (CS.isNotEmpty(aDuplicateCodes)) {
        alertify.error(getTranslation().WORKFLOW_CODE_ALREADY_EXISTS);
      }
      if (CS.isNotEmpty(aDuplicateNames)) {
        alertify.error(getTranslation().WORKFLOW_NAME_ALREADY_EXISTS);
      }
    }
    _triggerChange();
  };

  let _handleSaveAsDialogValueChanged = function (sContentId, sPropertyId, sValue) {
    let aGridViewData = ProcessConfigViewProps.getDataForSaveAsDialog();
    let oGridRow = CS.find(aGridViewData, {id: sContentId});
    let oProperty = oGridRow.properties[sPropertyId];
    if (!CS.isEmpty(oProperty) && !CS.isEqual(oProperty.value, sValue)) {
      oProperty.value = sValue;
    }
  };

  /** Function to set Default Value for WorkflowType & EntityType when Workflow tab button is clicked **/
  let _handleWorkflowTabClicked = function (bIsTreeItemClicked) {
    /** Set up Grid View Data **/
    let oActiveProcess = ProcessConfigViewProps.getActiveProcess();
    _setUpGridDataAndStartEventsData(oActiveProcess.workflowType);
  };

  /** Function to set EntityType **/
  let _handleEntityTypeClicked = function (aSelectedItem) {
    ProcessConfigViewProps.setEntityType(aSelectedItem[0]);
    _handleShowWorkflowButtonClicked();
  };

  /** Function to update the grid view based on WorkflowType & EntityType on Show Workflow button clicked **/
  let _handleShowWorkflowButtonClicked = function () {
    let sWorkflowType = ProcessConfigViewProps.getWorkflowType();
    _setUpGridDataAndStartEventsData(sWorkflowType);
    _triggerChange();
  };

  let _setUpGridDataAndStartEventsData = function(sWorkflowType){
    _setUpProcessConfigGridView();
    _setStartEvents(sWorkflowType);
  };

  /** Function to set START_EVENTS based on WorkflowType **/
  let _setStartEvents = function(sWorkflowType){
    let aStartEvent = CustomMockDataForWorkflowReplaceOptions;
    if (sWorkflowType === MockDataForWorkflowTypesDictionary.SCHEDULED_WORKFLOW) {
      oReplaceOptions["START_EVENT"] = CS.filter(aStartEvent, function (oStartEvent) {
        return (oStartEvent.className === "bpmn-icon-start-event-timer");
      });
    } else if (sWorkflowType === MockDataForWorkflowTypesDictionary.STANDARD_WORKFLOW || sWorkflowType === MockDataForWorkflowTypesDictionary.TASKS_WORKFLOW) {
      oReplaceOptions["START_EVENT"] = CS.filter(aStartEvent, function (oStartEvent) {
        return !(oStartEvent.className === "bpmn-icon-start-event-timer");
      });
    }
  };

  let prepareDataForSaveAs = function (aGridViewData) {
    let aDataToSave = [];
    let sLabelEmptyCheck = false;
    CS.forEach(aGridViewData, function (oData) {
      let oNewDataToSave = {};
      let oProperties = oData.properties;
      if (CS.isEmpty(oProperties["newLabel"].value)) {
        sLabelEmptyCheck = true;
      } else {
        oNewDataToSave.id = UniqueIdentifierGenerator.generateUUID();
        oNewDataToSave.label = oProperties["newLabel"].value;
        oNewDataToSave.code = oProperties["newCode"].value;
        oNewDataToSave.originalEntityId = oData.id;
        oNewDataToSave.isFromTemplate = false;
        aDataToSave.push(oNewDataToSave);
      }

    });
    if(sLabelEmptyCheck){
      return [];
    }else {
      return aDataToSave
    }
  };

  var _createProcess = function (bIsMDMCreate) {
    if (bIsMDMCreate) {
      let oActiveProcess = ProcessConfigViewProps.getActiveProcess();
      oActiveProcess = oActiveProcess.clonedObject || oActiveProcess;
      _createProcessCall(oActiveProcess);
    } else {
      var oUntitledProcessMasterObj = _createUntitledProcessMasterObject();
      if (!oUntitledProcessMasterObj.isCreated) {
        _createProcessCall(oUntitledProcessMasterObj);
      } else {
        ProcessConfigViewProps.setActiveProcess(oUntitledProcessMasterObj);
        _triggerChange();
      }
    }
  };

  let _preProcessSaveAsDialogForGridView = function (aSaveAsList) {
    let oGridSkeleton = new SaveAsDialogGridViewSkeleton();
    let aGridViewData = [];

    CS.forEach(aSaveAsList, function (oProcess) {
      let oProcessedData = {};
      oProcessedData.id = oProcess.id;
      oProcessedData.isExpanded = false;
      oProcessedData.children = [];
      oProcessedData.actionItemsToShow = [];
      oProcessedData.properties = {};

      CS.forEach(oGridSkeleton.scrollableColumns, function (oColumn) {
        switch (oColumn.id) {

          case "label":
            if (oProcess.hasOwnProperty(oColumn.id)) {
              oProcessedData.properties[oColumn.id] = {
                value: oProcess[oColumn.id],
                placeholder: oProcess.code,
                isDisabled: true,
                bIsMultiLine: false
              };
            }
            break;

          case "code":
            if (oProcess.hasOwnProperty(oColumn.id)) {
              oProcessedData.properties[oColumn.id] = {
                value: oProcess[oColumn.id],
                isDisabled: true,
                bIsMultiLine: false
              };
            }
            break;

          case "newLabel":
            let sLabelValue = _preprocessLabelAndCode(oProcess["label"]);
            oProcessedData.properties[oColumn.id] = {
              value: sLabelValue,
              placeholder: oProcess.code,
              isDisabled: false,
              bIsMultiLine: false
            };

            break;

          case "newCode":
            let sCodeValue = _preprocessLabelAndCode(oProcess["code"]);
            oProcessedData.properties[oColumn.id] = {
              value: sCodeValue,
              isDisabled: false,
              bIsMultiLine: false
            };

            break;
        }
      });

      aGridViewData.push(oProcessedData);
    });

    return aGridViewData;
  };

  let _preprocessLabelAndCode = function(sValue){
    let aSplitedValue = sValue.split("_");
    let bIsCopyIncluded = aSplitedValue[0].includes("Copy");
    let aCopyIncluded = bIsCopyIncluded ? aSplitedValue.splice(0,1) : aSplitedValue;
    let sContainsNumber = /\d/;
    let bIsNumberIncluded = sContainsNumber.test(aCopyIncluded[0]);
    let iCount = bIsNumberIncluded ? parseInt(aCopyIncluded[0].slice(4)) : parseInt(0);
    iCount = iCount+1 ;
    let sUpdatedValue = bIsCopyIncluded ? "Copy"+ iCount +"_" + aSplitedValue.join("_") : "Copy"+ "_" + aSplitedValue.join("_");

    return sUpdatedValue;
  }

  let _saveAsProcess = function () {
    let oGridViewProps = GridViewStore.getGridViewPropsByContext(GridViewContexts.PROCESS);
    let oGridSkeleton = oGridViewProps.getGridViewSkeleton();
    let aSelectedContentIds = oGridSkeleton.selectedContentIds;
    let aProcessData = ProcessConfigViewProps.getProcessGridViewData();
    let aDataForSaveAsDialog = [];
    if (CS.isEmpty(aSelectedContentIds)) {
      alertify.error(getTranslation().PLEASE_SELECT_ATLEAST_1_ROW);
    } else if (aSelectedContentIds.length === 1) {
      CS.forEach(aSelectedContentIds, function (sSelectedId) {
        let oProcess = CS.find(aProcessData, {id: sSelectedId});
        if (!CS.isEmpty(oProcess)) {
          let oNewProcessDataForCopyDialog = {};
          oNewProcessDataForCopyDialog.id = oProcess.id;
          oNewProcessDataForCopyDialog.label = oProcess.label;
          oNewProcessDataForCopyDialog.code = oProcess.code;
          aDataForSaveAsDialog.push(oNewProcessDataForCopyDialog);
        }
      });
      let oProcessedSaveAsDataForGrid = _preProcessSaveAsDialogForGridView(aDataForSaveAsDialog);
      ProcessConfigViewProps.setDataForSaveAsDialog(oProcessedSaveAsDataForGrid);
      ProcessConfigViewProps.setIsSaveAsDialogActive(true);
    } else {
      alertify.error(getTranslation().SELECT_ONLY_ONE_ENTITY);
    }
    _triggerChange();
  };

  var _createProcessCall = function (oWorkflowToCreate) {
    if (CS.isEmpty(oWorkflowToCreate.label.trim())) {
      alertify.error(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return;
    }
    if (!SettingUtils.isValidEntityCode(oWorkflowToCreate.code)) {
      alertify.error(getTranslation().PLEASE_ENTER_VALID_CODE);
      return;
    }
    var oCallback = {};
    oCallback.isCreated = true;
    oCallback.alertifyFunction = function () {
      SettingUtils.showSuccess(getTranslation().WORKFLOW_CREATED_SUCCESSFULLY);
    };
    var oSuccessCreateCallback = {};
    oSuccessCreateCallback.functionToExecute = _createProcessServerCall.bind(this, oWorkflowToCreate, oCallback);

    let oPostRequest = [];
    let oData = {};
    oData.codes = [oWorkflowToCreate.code];
    oData.entityType = ConfigEntityTypeDictionary.ENTITY_TYPE_PROCESS_EVENT;
    oData.names = [oWorkflowToCreate.label];
    oPostRequest.push(oData);
    oSuccessCreateCallback.serverCallback = {
      workflowToCreate: oWorkflowToCreate,
      isCreated: true,
      functionToExecute: oSuccessCreateCallback.functionToExecute,
    }
    SettingUtils.csPostRequest(ProcessRequestMapping.BulkCodeCheck, {}, oPostRequest, successBulkCodeCheckList.bind(this, oSuccessCreateCallback), failureBulkCodeCheckList);
  };

  var _createProcessServerCall = function (oProcessToCreate, oCallback) {

    let aMockXMLData = MockDataXMLDataForCamunda();
    let bIsScheduledWorkflow = (oProcessToCreate.workflowType === MockDataForWorkflowTypesDictionary.SCHEDULED_WORKFLOW) ? true : false;
    let sXMLKey = bIsScheduledWorkflow ? "forScheduledWorkflows" : "forCreate";
    let oXmlData = CS.find(aMockXMLData, {id: sXMLKey});
    oProcessToCreate.processDefinition = oXmlData.xmlData;

    /** To fetch all process components after create call **/
    let oPostData = {};
    oPostData.workflowType = oProcessToCreate.workflowType;
    oPostData.eventType = oProcessToCreate.eventType;
    oCallback.functionToExecute =  _fetAllProcessComponents(oPostData);
    SettingUtils.csPutRequest(ProcessRequestMapping.CreateProcess, {}, oProcessToCreate,
        successGetProcessCallback.bind(this, oCallback), failureSaveProcessCallback);

  };

  let _fillSelectedValueMap = function (sRequestSelectedElementKey, oSelectedListValuesMap, aSelectedItems) {
    !oSelectedListValuesMap[sRequestSelectedElementKey] && (oSelectedListValuesMap[sRequestSelectedElementKey] = []);
    oSelectedListValuesMap[sRequestSelectedElementKey] = CS.union(oSelectedListValuesMap[sRequestSelectedElementKey], aSelectedItems);
  };

  let _checkAndFillSelectedListValues = function (aInputParameters, oSelectedListValuesMap) {
    CS.forEach(aInputParameters, function (oInputParameter) {
      let oInputParameterTemplate = MockDataForProcessInputParameters[oInputParameter.name];
      let sRequestSelectedElementKey = oInputParameterTemplate && oInputParameterTemplate.requestSelectedElementKey;
      let sType = CS.isEmpty(oInputParameterTemplate) ? "" : oInputParameterTemplate.type;
      switch (sType) {
        case "tagGroup":
          CS.forEach(oInputParameter.definition.entries, function (oEntry) {
            let oKeyTemplate = MockDataForProcessInputParameters["tagGroup"];
            let aSelectedEntryItems = (CS.isEmpty(oEntry.value) ? [] : [oEntry.value]);
            _fillSelectedValueMap(oKeyTemplate.requestSelectedElementKey, oSelectedListValuesMap, aSelectedEntryItems);
          });
          break;
        case "list":
          let aSelectedItems = oInputParameter.definition ? CS.map(oInputParameter.definition.items, 'value') : (CS.isEmpty(oInputParameter.value) ? [] : [oInputParameter.value]);
          _fillSelectedValueMap(sRequestSelectedElementKey, oSelectedListValuesMap, aSelectedItems);
          break;
        case "tagsMap":
          CS.forEach(oInputParameter.definition.entries, function (oEntry) {
            let oKeyTemplate = MockDataForProcessInputParameters["TAG_VALUES_MAP"];
            let aSelectedEntryItems = [];
            if (!CS.isEmpty(oEntry.key)) {
              aSelectedEntryItems.push(oEntry.key);
              if (!CS.isEmpty(oEntry.value)) {
                aSelectedEntryItems = CS.concat(aSelectedEntryItems, oEntry.value);
              }
            }
            _fillSelectedValueMap(oKeyTemplate.requestSelectedElementKey, oSelectedListValuesMap, aSelectedEntryItems);
          });
          break;
        case "attributesTypeMap":
          CS.forEach(oInputParameter.definition.entries, function (oEntry) {
            let oKeyTemplate = MockDataForProcessInputParameters["ATTRIBUTES_TYPES_MAP"];
            let aSelectedEntryItems = [];
            if (!CS.isEmpty(oEntry.key)) {
              aSelectedEntryItems.push(oEntry.key);
            }
            _fillSelectedValueMap(oKeyTemplate.requestSelectedElementKey, oSelectedListValuesMap, aSelectedEntryItems);
          });
          break;
        case "taxonomiesList":
          let aSelectedItemsList = oInputParameter.definition ? CS.map(oInputParameter.definition.items, 'value') : (CS.isEmpty(oInputParameter.value) ? [] : [oInputParameter.value]);
          _fillSelectedValueMap(sRequestSelectedElementKey, oSelectedListValuesMap, aSelectedItemsList);
          break;
      }
    });
  };

  let _getSelectedListInputParametersFromDefinition = function (oDefinition) {
    let oSelectedListValuesMap = {};
    let oRootElement = oDefinition.rootElements[0];
    let aFlowElements = oRootElement && oRootElement.flowElements;
    CS.forEach(aFlowElements, function (oBusinessObject) {
      if ((oBusinessObject.$type == "bpmn:ServiceTask" || oBusinessObject.$type == "bpmn:UserTask" || oBusinessObject.$type == "bpmn:CallActivity") &&
          !CS.isEmpty(SettingUtils.getBPMNCustomElementIDFromBusinessObject(oBusinessObject)) &&
          ((SettingUtils.getBPMNCustomElementIDFromBusinessObject(oBusinessObject) !== "relationshipInstanceReadTask") &&
              (SettingUtils.getBPMNCustomElementIDFromBusinessObject(oBusinessObject) !== "relationshipInstanceCreateTask") &&
              (SettingUtils.getBPMNCustomElementIDFromBusinessObject(oBusinessObject) !== "relationshipInstanceUpdateTask") &&
              (SettingUtils.getBPMNCustomElementIDFromBusinessObject(oBusinessObject) !== "relationshipInstanceDeleteTask"))) {
        let oExtensionElements = oBusinessObject.extensionElements;
        let oInputOutput = oExtensionElements.values[0];
        _checkAndFillSelectedListValues(oInputOutput.inputParameters, oSelectedListValuesMap);
      }
    });
    return oSelectedListValuesMap;
  };

  let _fillSelectedElementADMByKey = function (oDataToSave, sKey, oOldData, oNewData) {
    let aNewElements = oNewData[sKey] || [];
    let aOldElements = oOldData[sKey] || [];
    oDataToSave[`added${sKey}`] = CS.difference(aNewElements, aOldElements);
    oDataToSave[`deleted${sKey}`] = CS.difference(aOldElements, aNewElements);
  };

  let _fillADMForSelectedElements = function (oActiveProcess, oDataToSave) {
    let oModelerInstance = ProcessConfigViewProps.getActiveBPMNModelerInstance();
    if(CS.isNotEmpty(oModelerInstance)){
      let aKeysForSelectedElements = ["Users", "KlassIds", "KlassIdsForComponent", "DestinationOrganizations", "Tasks", "AttributeIdsForComponent",
        "TagIdsForComponent", "TagValues", "Contexts", "Relationships", "TaxonomyIdsForComponent", "EndpointIdsForComponent",
        "AuthorizationMappings", "nonNatureKlassIds"];
      let oNewSelectedListInputItems = _getSelectedListInputParametersFromDefinition(oModelerInstance.getDefinitions());
      let oOldSelectedListInputItems = {};
      if(!ProcessConfigViewProps.getIsUploadedWF()) {
        oOldSelectedListInputItems = _getSelectedListInputParametersFromDefinition(ProcessConfigViewProps.getPureProcessDefinitionJSON());
      }else{
        ProcessConfigViewProps.setIsUploadedWF(false);
      }
      CS.forEach(aKeysForSelectedElements, function (sKey) {
        _fillSelectedElementADMByKey(oDataToSave, sKey, oOldSelectedListInputItems, oNewSelectedListInputItems);
      })
    }
  };

  /** Generate Cron Expression **/
  let _generateCronExpression = (oActiveData, sActiveFrequencyTab) => {
    let sCronExpression = "";
    let sStar = "*";
    let sSpace = " ";
    switch (sActiveFrequencyTab) {
      case MockDataForFrequencyTypesDictionary.DURATION :
        /** PT02H20M **/
        /*if (oActiveData.mins === 0) {
          if (/^\d$/.test(oActiveData.hours)) {
            sCronExpression = "PT0" + oActiveData.hours + "H";
          } else {
            sCronExpression = "PT" + oActiveData.hours + "H";
          }
        } else if (oActiveData.hours === 0) {
          if (/^\d$/.test(oActiveData.mins)) {
            sCronExpression = "PT0" + oActiveData.mins + "M";
          } else {
            sCronExpression = "PT" + oActiveData.mins + "M";
          }
        }*/
        if( oActiveData.mins === 0 && oActiveData.hours === 0){
          sCronExpression = null;
        }
        else {
          if (/^\d$/.test(oActiveData.hours)) {
            sCronExpression = "PT0" + oActiveData.hours;
          } else {
            sCronExpression = "PT" + oActiveData.hours;
          }
          if (/^\d$/.test(oActiveData.mins)) {
            sCronExpression = sCronExpression + "H0" + oActiveData.mins + "M";
          } else {
            sCronExpression = sCronExpression + "H" + oActiveData.mins + "M";
          }
        }
        break;
      case MockDataForFrequencyTypesDictionary.DATE :
        /** eg: 2011-03-11T12:13:14**/
        let sDateTemp = "";
        if(CS.isEmpty(oActiveData.date) && ((oActiveData.mins === 0) && (oActiveData.hours === 0))) {
          sCronExpression = null;
        }
        else{
          if (CS.isEmpty(oActiveData.date)){
            sDateTemp = new Date();
            sDateTemp = sDateTemp.toLocaleDateString();
            sDateTemp = sDateTemp.split("/");
          }
          sDateTemp = CS.isNotEmpty(sDateTemp) ? sDateTemp : oActiveData.date.split("/");
          let sDate = sDateTemp;
          let sDay = (/^\d$/.test(sDate[1])) ? "0"+sDate[1] : sDate[1];
          let sMonth = (/^\d$/.test(sDate[0])) ? "0"+sDate[0] : sDate[0];
          let sYear = sDate[2];
          let sHours = (/^\d$/.test(oActiveData.hours)) ? "0"+oActiveData.hours : oActiveData.hours;
          let sMins = (/^\d$/.test(oActiveData.mins)) ? "0"+oActiveData.mins : oActiveData.mins;
          sCronExpression = sYear+"-"+sMonth+"-"+sDay+"T"+sHours+":"+sMins+":00";
        }

        break;
      case MockDataForFrequencyTypesDictionary.DAILY :
        /** eg: ( '15 10 1/1 * ? *' ) every day at 10:15 **/
        if( oActiveData.mins === 0 && oActiveData.hours === 0){
          sCronExpression = null;
        }else {
          sCronExpression = "0"+sSpace+oActiveData.mins+sSpace+oActiveData.hours+sSpace+"1/1"+sSpace+sStar+sSpace+"?"+sSpace+sStar;
        }
        break;
      case MockDataForFrequencyTypesDictionary.HOURMIN :
        /** eg: 0 0/3 1/1 * ? *  every 3 hours**/
        if( oActiveData.mins === 0 && oActiveData.hours === 0){
          sCronExpression = null;
        }else{
          sCronExpression = "0"+sSpace+"0/"+oActiveData.mins+sSpace+"0/"+oActiveData.hours+sSpace+"1/1"+sSpace+sSpace+sStar+sSpace+"?"+sSpace+sStar;
        }
        break;
      case MockDataForFrequencyTypesDictionary.WEEKLY :
        /** eg: 0 12 ? * MON,SAT * every mon/sat at 12 **/
        let sDaysOfWeek = oActiveData.daysOfWeeks.toLocaleString();
        if(CS.isEmpty(sDaysOfWeek)){
          sCronExpression = null;
        }else{
          sCronExpression = "0"+sSpace+oActiveData.mins+sSpace+oActiveData.hours+sSpace+"?"+sSpace+sStar+sSpace+sDaysOfWeek+sSpace+sStar+sSpace;
        }
        break;
      case MockDataForFrequencyTypesDictionary.MONTHLY :
        /** eg: 0 12 7 1/2 ? *  Day 7th of every 2 month(s)**/
        sCronExpression = "0"+sSpace+"0"+sSpace+"0"+sSpace+oActiveData.days+sSpace+"1/"+oActiveData.months+sSpace+"?"+sSpace+sStar;
        break;
      case MockDataForFrequencyTypesDictionary.YEARLY :
        /** eg: 0 12 8 6 ? * ' every year 8th of june**/
        sCronExpression = "0"+sSpace+"0"+sSpace+"0"+sSpace+oActiveData.days+sSpace+oActiveData.monthsOfYear+sSpace+"?"+sSpace+sStar;
        break;
    }
    return sCronExpression;
  };

  var _processDataToSave = function (oActiveProcess) {
    var oDataToSave = CS.cloneDeep(oActiveProcess.clonedObject);
    _fillADMForSelectedElements(oActiveProcess, oDataToSave);
    let oNewDataToSave = _generateADM(oActiveProcess);
    let oDataToSaveForRoles = _generateADMForRoles(oActiveProcess);
    CS.assign(oDataToSave, oDataToSaveForRoles);
    CS.assign(oDataToSave, oNewDataToSave);
    let oModelerInstance = ProcessConfigViewProps.getActiveBPMNModelerInstance();
    if (CS.isNotEmpty(oModelerInstance)) {
      let oDefinitions = oModelerInstance.getDefinitions();
      let aRootElements = oDefinitions.rootElements[0];
      let aFlowElements = aRootElements.flowElements;
      oDataToSave.isExecutable = aRootElements.isExecutable;
      CS.forEach(aFlowElements, function (oFlowElements) {
        if (!CS.includes(oFlowElements.id, "SequenceFlow")) {
          let oBPMNFactory = ProcessConfigViewProps.getActiveBPMNFactoryInstance();
          let oActiveModelingInstance = ProcessConfigViewProps.getActiveBPMNModelingInstance();
          let sDelegateExpression = "${camundaTask}";
          if (CS.includes(oFlowElements.id, "Task")) {
            oFlowElements.delegateExpression = sDelegateExpression;
          } else {
            if (!oFlowElements.extensionElements) {
              let sEvent = CS.includes(oFlowElements.id, "StartEvent") ? "start" : "end";
              let oExtensionElement = ElementHelper.createElement(ProcessConstants.BO_ELEMENT_EXTENSION_ELEMENTS, {values: []}, oFlowElements, oBPMNFactory);
              oFlowElements.extensionElements = oExtensionElement;
              let oTaskListener = ElementHelper.createElement(ProcessConstants.EXTENSION_EXECUTION_LISTENER, {
                delegateExpression: sDelegateExpression, event: sEvent
              }, oExtensionElement, oBPMNFactory);
              oExtensionElement.values.push(oTaskListener);
            } else {
              let oExtensionElements = oFlowElements.extensionElements;
              let oExecutionListener = oExtensionElements.values[0];
              oExecutionListener.delegateExpression = sDelegateExpression;
              if (CS.isNotEmpty(oActiveModelingInstance)) {
                oActiveModelingInstance.updateProperties(oFlowElements, {});
              }
            }
          }
          if (CS.isEmpty(oFlowElements.incoming) && oFlowElements.eventDefinitions) {
            let oFrequencyData = ProcessConfigViewProps.getFrequencyData();
            let sActiveFrequencyTab = ProcessConfigViewProps.getSelectedTabId();
            let oTimerInfo = oFlowElements.eventDefinitions[0];
            oDataToSave.frequencyActiveTabId = sActiveFrequencyTab;
            let oActiveData = oFrequencyData[sActiveFrequencyTab];
            if(CS.isNotEmpty(oActiveData)){
              oDataToSave.isXMLModified = true;
            }
            switch (sActiveFrequencyTab) {
              case MockDataForFrequencyTypesDictionary.DURATION :
                /** Tried to setup values in BPMN directly, but could not found out the way**/
                /*let oActiveBPMNModelerInstance = ProcessConfigViewProps.getActiveBPMNModelerInstance();
                let oFactory = oActiveBPMNModelerInstance._moddle.factory.model;
                let oFormalExpression = ElementHelper.createElement(ProcessConstants.FORMAL_EXPRESSION, {body: oActiveData.hours}, oTimerInfo, oFactory);
                oTimerInfo.timeDuration = oFormalExpression;
                oFactory.updateProperties(oFlowElements, {});*/
                oDataToSave.timerDefinitionType = "timeDuration";
                oDataToSave.timerStartExpression = _generateCronExpression(oActiveData, sActiveFrequencyTab)
                break;
              case MockDataForFrequencyTypesDictionary.DATE :
                oDataToSave.timerDefinitionType = "timeDate";
                oDataToSave.timerStartExpression = _generateCronExpression(oActiveData, sActiveFrequencyTab)
                break;
              case MockDataForFrequencyTypesDictionary.DAILY :
                oDataToSave.timerDefinitionType = "timeCycle";
                oDataToSave.timerStartExpression = _generateCronExpression(oActiveData, sActiveFrequencyTab)
                break;
              case MockDataForFrequencyTypesDictionary.HOURMIN :
                oDataToSave.timerDefinitionType = "timeCycle";
                oDataToSave.timerStartExpression = _generateCronExpression(oActiveData, sActiveFrequencyTab)
                break;
              case MockDataForFrequencyTypesDictionary.WEEKLY :
                oDataToSave.timerDefinitionType = "timeCycle";
                oDataToSave.timerStartExpression = _generateCronExpression(oActiveData, sActiveFrequencyTab)
                break;
              case MockDataForFrequencyTypesDictionary.MONTHLY :
                oDataToSave.timerDefinitionType = "timeCycle";
                oDataToSave.timerStartExpression = _generateCronExpression(oActiveData, sActiveFrequencyTab)
                break;
              case MockDataForFrequencyTypesDictionary.YEARLY :
                oDataToSave.timerDefinitionType = "timeCycle";
                oDataToSave.timerStartExpression = _generateCronExpression(oActiveData, sActiveFrequencyTab)
                break;
            }
            /*if (oTimerInfo.timeDuration) {
              oDataToSave.timerDefinitionType = "timeDuration";
              oDataToSave.timerStartExpression = (oTimerInfo.timeDuration.body) ? oTimerInfo.timeDuration.body : null;
            } else if (oTimerInfo.timeDate) {
              oDataToSave.timerDefinitionType = "timeDate";
              oDataToSave.timerStartExpression = (oTimerInfo.timeDate.body) ? oTimerInfo.timeDate.body : null;
            } else if (oTimerInfo.timeCycle) {
              oDataToSave.timerDefinitionType = "timeCycle";
              oDataToSave.timerStartExpression = (oTimerInfo.timeCycle.body) ? oTimerInfo.timeCycle.body : null;
            }*/
          }
        }
      });
    }
    return oDataToSave;
  };

  let _checkOrganizationEmptyForJMSAndSchedule = function (oDataToSave) {
    return ((oDataToSave.workflowType === MockDataForWorkflowTypesDictionary.JMS_WORKFLOW || oDataToSave.workflowType === MockDataForWorkflowTypesDictionary.SCHEDULED_WORKFLOW)
        && CS.isEmpty(oDataToSave.organizationsIds));
  };

  var _checkAnyTaxonomyColumnNameEmpty = function () {
    let bIsTaxonomyColumnEmpty = false;
    //todo handle for bpmn
    return bIsTaxonomyColumnEmpty;
  };

  var _saveProcess = function (oCallback) {
    var oActiveProcess = ProcessConfigViewProps.getActiveProcess();
    if (oActiveProcess.clonedObject) {
      if (CS.isEmpty(oActiveProcess.clonedObject.label.trim())) {
        alertify.error(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      } else if (_checkAnyTaxonomyColumnNameEmpty()) {
        alertify.error(getTranslation().TAXONOMY_COLUMN_NAME_SHOULD_NOT_BE_EMPTY);
        return;
      }
      else {
        let oDataToSave = _processDataToSave(oActiveProcess);
        let aWorkflowGridData = ProcessConfigViewProps.getProcessGridViewData()
        let oServerCallback = {};

        delete oDataToSave.klassTypes;
        delete oDataToSave.klassIds;
        delete oDataToSave.referencedTaxonomies;
        delete oDataToSave.configDetails;
        delete oDataToSave.taxonomyIds;
        delete oDataToSave.attributeIds;
        delete oDataToSave.tagIds;
        delete oDataToSave.taxonomyIdsForComponent;
        delete oDataToSave.nonNatureklassTypes;
        delete oDataToSave.nonNatureKlassIds;
        // delete oDataToSave.organizationsIds;

        //delete oDataToSave.selectedTaxonomyIds;

        var oCallbackData = {};
        CS.assign(oCallbackData, oCallback);
        oCallbackData.isSaved = true;

        oServerCallback.processToSave = oDataToSave;
        if (oDataToSave.workflowType === MockDataForWorkflowTypesDictionary.JMS_WORKFLOW) {
          if ((CS.isEmpty(oDataToSave.ip) || CS.isEmpty(oDataToSave.port) || CS.isEmpty(oDataToSave.queue))) {
            oCallbackData.isJMSProp = true;
          }
        }
        oServerCallback.requestURL = ProcessRequestMapping.SaveProcess;
        oCallbackData.serverCallback = oServerCallback;
        let oPostRequest = [];
        let oData = {};
        oData.codes = [];
        oData.entityType = ConfigEntityTypeDictionary.ENTITY_TYPE_PROCESS_EVENT;
        oData.names = [];
        oPostRequest.push(oData);

        let oWorkFlowGridData = CS.find(aWorkflowGridData, {id: oDataToSave.id});
        if(CS.isNotEmpty(oWorkFlowGridData)){
          if (oWorkFlowGridData.label !== oDataToSave.label) {
            oData.names.push(oDataToSave.label);
          }
        }
        if (_checkOrganizationEmptyForJMSAndSchedule(oDataToSave)) {
          alertify.error(getTranslation().ORGANISATION_IS_MANDATORY_FOR_JMS_AND_SCHEDULED_WORKFLOW);
        } else {
          SettingUtils.csPostRequest(ProcessRequestMapping.BulkCodeCheck, {}, oPostRequest, successBulkCodeCheckList.bind(this, oCallbackData), failureBulkCodeCheckList);
        }
      }
    }
  };

  let _generateADM = function (oActiveProcess) {
    let oOldProcess = oActiveProcess;
    let oNewProcess = oActiveProcess.clonedObject;

    let oDataToSave = {
      id: oNewProcess.id,
      label: oNewProcess.label,
    };

    oDataToSave.isXMLModified = oOldProcess.processDefinition.localeCompare(oNewProcess.processDefinition) ? true : false;

    oDataToSave.addedKlassIds = CS.difference(oNewProcess.klassIds, oOldProcess.klassIds);
    oDataToSave.deletedKlassIds = CS.difference(oOldProcess.klassIds, oNewProcess.klassIds);

    oDataToSave.addedTagIds = CS.difference(oNewProcess.tagIds, oOldProcess.tagIds);
    oDataToSave.deletedTagIds = CS.difference(oOldProcess.tagIds, oNewProcess.tagIds);

    oDataToSave.addedAttributeIds = CS.difference(oNewProcess.attributeIds , oOldProcess.attributeIds);
    oDataToSave.deletedAttributeIds  = CS.difference(oOldProcess.attributeIds , oNewProcess.attributeIds);

    oDataToSave.addedTaxonomyIds = CS.difference(oNewProcess.taxonomyIds, oOldProcess.taxonomyIds);
    oDataToSave.deletedTaxonomyIds = CS.difference(oOldProcess.taxonomyIds, oNewProcess.taxonomyIds);

    oDataToSave.addedNonNatureKlassIds = CS.difference(oNewProcess.nonNatureKlassIds, oOldProcess.nonNatureKlassIds);
    oDataToSave.deletedNonNatureKlassIds = CS.difference(oOldProcess.nonNatureKlassIds, oNewProcess.nonNatureKlassIds);

    oDataToSave.addedOrganizations = CS.difference(oNewProcess.organizationsIds, oOldProcess.organizationsIds);
    oDataToSave.deletedOrganizations = CS.difference(oOldProcess.organizationsIds, oNewProcess.organizationsIds);

    return oDataToSave;
  };

  let _generateADMForRoles = function (oActiveProcess) {
    let oNewProcess = oActiveProcess.clonedObject;
    let aRoleList = ["accountable", "responsible", "consulted", "informed", "verify", "signoff"];

    let oDataToSave = {
      id: oNewProcess.id,
      label: oNewProcess.label,
      addedRoles:[],
      addedUsers:[]
    };


    CS.forEach(aRoleList, function (sRole) {
      let oRoles = oNewProcess[sRole];
      if(!CS.isEmpty(oRoles)){
        CS.forEach(oRoles.roleIds, function (role) {
          if(!oDataToSave.addedUsers.includes(role)) {
            oDataToSave.addedRoles.push(role);
          }
        });
        CS.forEach(oRoles.userIds, function (user) {
          if(!oDataToSave.addedUsers.includes(user)){
            oDataToSave.addedUsers.push(user);
          }
        });

        delete oNewProcess[sRole];
      }
    });

    return oDataToSave;
  }

  var _discardUnsavedProcess = function (oCallbackData) {
    var oActiveProcess = ProcessConfigViewProps.getActiveProcess();
    var bScreenLockStatus = _getProcessScreenLockStatus();
    if(!bScreenLockStatus) {
      alertify.message(getTranslation().NO_CHANGE_FOUND_TO_DISCARD);
      return;
    }

    if (!CS.isEmpty(oActiveProcess)) {
      if (oActiveProcess.clonedObject) {

        delete oActiveProcess.clonedObject;
        delete oActiveProcess.isDirty;
        ProcessConfigViewProps.setSelectedComponent({});
        ProcessConfigViewProps.setShouldImportXML(true);
        ProcessConfigViewProps.setValidationInfo({});
        alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
      }
      _setProcessScreenLockStatus(false);
      if (oCallbackData.functionToExecute) {
        oCallbackData.functionToExecute();
      }
      _triggerChange();

    } else {
      alertify.message(getTranslation().NO_CHANGE_FOUND_TO_DISCARD);
    }
  };

  var _deleteProcessAjax = function (aToDeleteIds) {
    if(aToDeleteIds.includes("housekeeping_activity_workflow")){
      _setProcessScreenLockStatus(false);
      alertify.message(getTranslation().STANDARD_WORKFLOW_CANNOT_BE_DELETED);
      _triggerChange();
    } else if (!CS.isEmpty(aToDeleteIds)) {
      var oCallbackData = {
        deletedIds: aToDeleteIds
      };
      return SettingUtils.csDeleteRequest(ProcessRequestMapping.DeleteProcess, {}, {ids: aToDeleteIds}, successDeleteProcessCallback.bind(this, oCallbackData), failureDeleteProcessCallback);
    } else {
      _setProcessScreenLockStatus(false);
      alertify.success(getTranslation().WORKFLOW_DELETE_SUCCESSFUL);
      _triggerChange();
    }
  };

  var _deleteProcess = function (aSelectedIds) {

    let aProcessIdsToDelete = aSelectedIds;
    var oMasterProcessList = SettingUtils.getAppData().getProcessList();
    CS.remove(oMasterProcessList, function (sProcessId) {
      let oMasterProcess = oMasterProcessList[sProcessId];
      if (sProcessId === "admin" || oMasterProcess.isDefault) {
        return true;
      }
    })
    if (!CS.isEmpty(aProcessIdsToDelete)) {
      var aBulkDeleteProcess = [];
      CS.forEach(aProcessIdsToDelete, function (sProcessId) {
        let sSelectedWorkflowLabel = CS.getLabelOrCode(oMasterProcessList[sProcessId]);
        aBulkDeleteProcess.push(sSelectedWorkflowLabel);
      });

      //var sBulkDeleteProcessLastNames = aBulkDeleteProcess.join(', ');
      CustomActionDialogStore.showListModeConfirmDialog(getTranslation().DELETE_CONFIRMATION, aBulkDeleteProcess,
          function () {
            _deleteProcessAjax(aProcessIdsToDelete)
            .then(_fetchProcessListForGridView);
          }, function (oEvent) {
          });
    } else {
      alertify.message(getTranslation().NOTHING_SELECTED_TO_DELETE);
    }
  };

  var _getProcessDetails = function (sSelectedProcessId, oCallBackData) {
    oCallBackData = {};
    let oPostData = {};
    oCallBackData.isCreated = false;
    /** Updated the API call as per requirement of Workflow Screen Changes **/
    let aGridData = ProcessConfigViewProps.getProcessGridViewData();
    let oProcessData = CS.find(aGridData, {id: sSelectedProcessId});
    if (CS.isNotEmpty(oProcessData)) {
      oPostData.workflowType = oProcessData.workflowType;
      oPostData.eventType = oProcessData.eventType;
    }
    oCallBackData.functionToExecute = _fetAllProcessComponents(oPostData);

    SettingUtils.csGetRequest(ProcessRequestMapping.GetProcess, {id: sSelectedProcessId}, successGetProcessCallback.bind(this, oCallBackData), failureSaveProcessCallback);

  };

  var _handleProcessListNodeClicked = function (oModel) {
    var bProcessScreenLockStatus = _getProcessScreenLockStatus();
    var oActiveProcess = ProcessConfigViewProps.getActiveProcess();
    var oProcessValueList = ProcessConfigViewProps.getProcessValueList();
    if (!(oActiveProcess.id === oModel.id)) {

      if (bProcessScreenLockStatus) {
        var oCallbackData = {};
        oCallbackData.functionToExecute = _getProcessDetails.bind(this, oModel.id);

        CustomActionDialogStore.showTriActionDialog( getTranslation().THERE_ARE_UNSAVED_CHANGES_DO_YOU_WANT_TO_SAVE,
            _saveProcess.bind(this, oCallbackData),
            _discardUnsavedProcess.bind(this, oCallbackData)).set({transition: 'fade'});

      } else {
        _getProcessDetails(oModel.id);
      }
    } else {

      //To un-check all if clicked on the selected node
      SettingUtils.applyValueToAllTreeNodes(oProcessValueList, 'isChecked', false);
      _triggerChange();
    }
  };

  var _makeActiveProcessDirty = function () {
    var oActiveProcess = ProcessConfigViewProps.getActiveProcess();
    SettingUtils.makeObjectDirty(oActiveProcess);
    _setProcessScreenLockStatus(true);
    return oActiveProcess.clonedObject;
  };

  let _handleProcessSingleValueChanged = function (sKey, sValue, aSplitContext) {
    var oActiveProcess = _makeActiveProcessDirty();
    if(sKey === "processType"){
      if (sValue === ProcessTypeDictionary.ONBOARDING_PROCESS) {
        oActiveProcess.eventType = ProcessEventTypeDictionary.BUSINESS_PROCESS_EVENT;
      } else {
        oActiveProcess.eventType = ProcessEventTypeDictionary.EXPORT_EVENT;
      }
    }else if(sKey === "eventType" && sValue === ProcessEventTypeDictionary.INTEGRATION){
      oActiveProcess["triggeringType"] = "import";
    }else if(sKey === "eventType" && sValue === ProcessEventTypeDictionary.BUSINESS_PROCESS_EVENT){
      oActiveProcess["triggeringType"] = "afterSave";
    } else if (sKey === "workflowType") {
      if (sValue === MockDataForWorkflowTypesDictionary.STANDARD_WORKFLOW || sValue === MockDataForWorkflowTypesDictionary.TASKS_WORKFLOW) {
        if (CS.isEmpty(oActiveProcess["eventType"])) {
          oActiveProcess["eventType"] = ProcessEventTypeDictionary.BUSINESS_PROCESS_EVENT;
          oActiveProcess["triggeringType"] = "afterSave";
        }
      } else {
        oActiveProcess["eventType"] = "";
        oActiveProcess["triggeringType"] = "";
      }
    }else if(CS.isNotEmpty(aSplitContext)){
      let oFrequencyData = ProcessConfigViewProps.getFrequencyData();
      if(aSplitContext[3] !== "daysOfWeeks"){
        oFrequencyData[sKey][aSplitContext[3]] = sValue[0];
      }else{
        oFrequencyData[sKey][aSplitContext[3]] = sValue;
      }
      oActiveProcess["frequency"] = oFrequencyData;
      ProcessConfigViewProps.setFrequencyData(oFrequencyData);
    }
    if(CS.isEmpty(aSplitContext)){
      oActiveProcess[sKey] = sValue;
    }
    _triggerChange();
  };

  let _handleProcessLazyMSSValueChanged = function (sKey, aSelectedValue, oReferencedData) {
    var oActiveProcess = _makeActiveProcessDirty();
    let oConfigDetails = oActiveProcess.configDetails;

    switch (sKey) {
      case "klassTypes" :
        oConfigDetails.referencedKlassesIds = aSelectedValue;
        oConfigDetails.referencedKlasses = oReferencedData;
        oActiveProcess["klassIds"] = aSelectedValue;
        break;

      case "attributes" :
        oConfigDetails.referencedAttributesIds = aSelectedValue;
        oConfigDetails.referencedAttributes = oReferencedData;
        oActiveProcess["attributeIds"] = aSelectedValue;
        break;

      case "tags" :
        oConfigDetails.referencedTagsIds = aSelectedValue;
        oConfigDetails.referencedTags = oReferencedData;
        oActiveProcess["tagIds"] = aSelectedValue;
        break;

      case "nonNatureklassTypes" :
        oConfigDetails.referencedKlassesIds = aSelectedValue;
        oConfigDetails.referencedKlasses = oReferencedData;
        oActiveProcess["nonNatureKlassIds"] = aSelectedValue;
        break;

      case "organizations" :
        oConfigDetails.referencedOrganizationsIds = aSelectedValue;
        oConfigDetails.referencedOrganizations = oReferencedData;
        oActiveProcess["organizationsIds"] = aSelectedValue;
        break;

    }
    _triggerChange();
  };

  var _validateComponentProperty = function (oSelectedComponent, sKey, sValue) {
    var oDataSource = oSelectedComponent.dataSources[0];
    var oClassInfo = oDataSource.classInfo;
    switch (sKey) {
      case "secondaryClassType":
        if(sValue == "multiClass") {
          oClassInfo.secondaryClassColumnName = "";
        }
        else {
          oClassInfo.secondaryClasses = [];
        }
        break;
      case "type":
        if (sValue == "singleClass") {
          oClassInfo.klassColumn = "";
        }
        else {
          oClassInfo.klassId = "";
        }
        break;
      case "variantType":
        if(sValue == "variant") {
          oClassInfo.parentContextId = "";
          oClassInfo.masterVariantInstanceColumn = "";
        }
        break;
      case "isTaxonomyEnabled":
        if(sValue == false) {
          oClassInfo.taxonomyColumn = "";
        }
        break;
      case "isMultiClassificationEnabled":
        if(sValue == false) {
          oClassInfo.secondaryClassColumnName = "";
          oClassInfo.secondaryClasses = [];
          oClassInfo.secondaryClassType = "multiClass";
        }
        break;
      case "taxonomyType":
        break;

    }
  };

  var _handleProcessComponentDataSourceValueChanged = function (sKey, sValue) {
    _makeActiveProcessDirty();
    var oSelectedComponent = ProcessConfigViewProps.getSelectedComponent();
    var oDataSource = oSelectedComponent.dataSources[0];
    _validateComponentProperty(oSelectedComponent, sKey, sValue);
    oDataSource[sKey] = sValue;
  };

  var _handleProcessComponentClassInfoValueChanged = function (sKey, sValue, oReferencedData) {
    let oActiveProcess = _makeActiveProcessDirty();

    if(sKey === "klassId") {
      oActiveProcess.configDetails.referencedKlasses = oReferencedData;
    }
    var oSelectedComponent = ProcessConfigViewProps.getSelectedComponent();
    var oDataSource = oSelectedComponent.dataSources[0];
    var oClassInfo = oDataSource.classInfo;
    _validateComponentProperty(oSelectedComponent, sKey, sValue);
    oClassInfo[sKey] = sValue;
  };

  var _handleProcessDesignChanged = function (xml) {
    let oActiveBPMNModelerInstance = ProcessConfigViewProps.getActiveBPMNModelerInstance();
    ProcessConfigViewProps.setActiveBPMNFactoryInstance(oActiveBPMNModelerInstance._moddle);
      let oActiveProcess = _makeActiveProcessDirty();
      oActiveProcess.processDefinition = xml;
  };

  let _handleBPMNUploadDialogStatusChanged = function (bStatus) {
    ProcessConfigViewProps.setIsUploadWindowActive(bStatus);
    _triggerChange();
  };

  let _handleBPMNXMLUpload = function (sXml) {
    let oModelerInstance = new BpmnModeler();
    /**Creating new Modeler instance in order to check wheather uploaded file is valid***/
    oModelerInstance.importXML(sXml, function (err) {
      if (err) {
        alertify.error(getTranslation().INVALID_BPMN_FILE);
        ExceptionLogger.error(err)
      } else {
        let oActiveProcess = _makeActiveProcessDirty();
        oActiveProcess.processDefinition = sXml;
        ProcessConfigViewProps.setIsUploadWindowActive(false);
        ProcessConfigViewProps.setShouldImportXML(true);
        ProcessConfigViewProps.setIsUploadedWF(true);
        _getRequiredConfigDetails(oModelerInstance);
        alertify.success(getTranslation().BPMN_FILE_UPLOADED_SUCCESSFULLY);
        _triggerChange();
      }
    });
  };

  /* This function fetch required config information while uploading bpmn file to resolve issue mentioned in ticket */
  let _getRequiredConfigDetails = (sXml) => {
    let oDefinition = sXml.getDefinitions();
    let oRootElement = oDefinition.rootElements[0];
    let aFlowElements = oRootElement && oRootElement.flowElements;
    let sRequestUrl = "";
    let oReqData = {};
    let oEntities = {};
    CS.forEach(aFlowElements, function (oElement) {
      switch (oElement.name) {
        case "Set Attribute And Tags Task" :
          oReqData = {
            dataLanguage: "",
            isGetDataLanguages: true,
            isGetUILanguages: false,
            uiLanguage: ""
          };
          sRequestUrl = ProcessRequestMapping.GetDataLanguage;
          //call for getting Languages
          SettingUtils.csPostRequest(sRequestUrl, {}, oReqData,
              successGetRequiredConfigDetails.bind(this, ProcessRequestMapping.GetDataLanguage), failureGetRequiredConfigDetails);
          let aTags = [];
          let oValue = oElement.extensionElements.values[0];
          let oInputParameter = CS.find(oValue.$children, {name: "TAG_VALUES_MAP"});
          let aEntries = oInputParameter.$children[0];
          if (aEntries.$children) {
            aEntries.$children.forEach(entry => {
              aTags.push(entry.key);
            });
          }
          oReqData = {};
          oReqData.ids = aTags;
          SettingUtils.csPostRequest(ProcessRequestMapping.GetTagValues, {}, oReqData,
              successGetRequiredConfigDetails.bind(this, ProcessRequestMapping.GetTagValues), failureGetRequiredConfigDetails);
        case "Get Attributes And Tags Task":
          if (oElement.name !== "Set Attribute And Tags Task") {
            oEntities.tags = {
              from: 0,
              size: 9999,
              sortBy: "label",
              sortOrder: "asc",
              types: [],
              typesToExclude: [],
            };
          }
          oEntities.attributes = {
            from: 0,
            size: 9999,
            sortBy: "label",
            sortOrder: "asc",
            types: [],
            typesToExclude: []
          };
          oReqData = {
            searchColumn: "label",
            searchText: "",
            entities: oEntities
          };
          sRequestUrl = ProcessRequestMapping.GetConfigData;

          break;
        case "Execute and Update Asset Validity":
          oReqData = {
            dataLanguage: "",
            isGetDataLanguages: true,
            isGetUILanguages: false,
            uiLanguage: ""
          };
          sRequestUrl = ProcessRequestMapping.GetDataLanguage;
          break;
        case "Transfer Task":
          oReqData = {
            from: 0,
            size: 99999,
            sortBy: "label",
            sortOrder: "asc",
            types: [],
            searchColumn: "label",
            searchText: "",
          };
          sRequestUrl = ProcessRequestMapping.GetAllOrganisation;

          break;
        case "Custom User Task":
          oEntities.tasks = {
            from: 0,
            size: 9999,
            sortBy: "label",
            sortOrder: "asc",
            types: [],
            typesToExclude: ["personal"],
          };
          oReqData = {
            searchColumn: "label",
            searchText: "",
            entities: oEntities
          };
          sRequestUrl = ProcessRequestMapping.GetConfigData;
          break;
        default:
          break;

      }
      if (sRequestUrl !== "") {
        SettingUtils.csPostRequest(sRequestUrl, {}, oReqData,
            successGetRequiredConfigDetails.bind(this, oElement.name), failureGetRequiredConfigDetails)
      };
    });
  };

  let successGetRequiredConfigDetails = function (sName, oResponse) {
    let oActiveProcess = ProcessConfigViewProps.getActiveProcess();

    switch (sName) {
      case "Set Attribute And Tags Task":
        _setReferencedAttributes(oActiveProcess, oResponse);
        break;
      case "Get Attributes And Tags Task":
        _setReferencedAttributes(oActiveProcess, oResponse);
        let aTags = oResponse.success.tags.list;
        let oTags = oActiveProcess.configDetails.referencedTags;
        aTags.forEach(oTag => {
          oTags[oTag.id] = oTag;
        });
        oActiveProcess.configDetails.referencedTags = oTags;
        break;
      case "Execute and Update Asset Validity":
        _setReferencedLanguages(oActiveProcess, oResponse);
        break;
      case "Transfer Task":
        let aOrg = oResponse.success.list;
        let oOrganization = {};
        aOrg.forEach(oOrg => {
          oOrganization[oOrg.id] = oOrg;
        });
        oActiveProcess.configDetails.referencedDestinationOrganizations = oOrganization;
        oActiveProcess.configDetails.referencedOraganizations = oOrganization;

        break;
      case "Custom User Task":
        let aTasks = oResponse.success.tasks.list;
        let oTasks = {};
        aTasks.forEach(oTask => {
          oTasks[oTask.id] = oTask;
        });
        oActiveProcess.configDetails.referencedTasks = oTasks;
        break;
      case ProcessRequestMapping.GetDataLanguage:
        _setReferencedLanguages(oActiveProcess, oResponse);
        break;
      case ProcessRequestMapping.GetTagValues:
        let aTagValues = oResponse.success;
        let oTagValues = {};
        aTagValues.forEach(oTagValue => {
          oTagValues[oTagValue.id] = oTagValue;
        });
        oActiveProcess.configDetails.referencedTags = oTagValues;
        break;
      default:
        break;
    }
  };
  let _setReferencedAttributes = (oActiveProcess, oResponse) =>{
    let aAttributes = oResponse.success.attributes.list;
    let oAttributes = {};
    aAttributes.forEach(oAttribute => {
      oAttributes[oAttribute.id] = oAttribute;
    });
    oActiveProcess.configDetails.referencedAttributes = oAttributes;
  };

  let _setReferencedLanguages = (oActiveProcess, oResponse)=>{
    let aLanguage = oResponse.success.dataLanguages;
    let oLanguages = {};
    aLanguage.forEach(oLanguage => {
      oLanguages[oLanguage.id] = oLanguage;
    });
    oActiveProcess.configDetails.referencedLanguages = oLanguages;
  };

  let failureGetRequiredConfigDetails = function (oResponse) {
    SettingUtils.failureCallback(oResponse, 'failureFetchAllCustomComponents', getTranslation());
  };

  let _handleSetProcessDefinitions = function (oModeler) {
    ProcessConfigViewProps.setPureProcessDefinitionJSON(CS.cloneDeep(oModeler.getDefinitions()));
    ProcessConfigViewProps.setActiveBPMNModelerInstance(oModeler);
    ProcessConfigViewProps.setShouldImportXML(false);
    let oDefinition = oModeler.getDefinitions();
    let oRootElement = oDefinition.rootElements[0];
    let aFlowElements = oRootElement && oRootElement.flowElements;
    CS.forEach(aFlowElements, function (oBusinessObject) {
      if ((oBusinessObject.$type == "bpmn:ServiceTask" || oBusinessObject.$type == "bpmn:UserTask") && !CS.isEmpty(SettingUtils.getBPMNCustomElementIDFromBusinessObject(oBusinessObject))) {
        _prepareAdvancedFilterData(oBusinessObject);
      }
    });

  };

  var _handleSelectionToggleButtonClicked = function (sKey, sId, bSingleSelect) {
    _makeActiveProcessDirty();
    var oSelectedComponent = ProcessConfigViewProps.getSelectedComponent();
    var aRules = oSelectedComponent.dataRules || [];
    var aRemovedIds = CS.remove(aRules, function (sRuleId) { return sRuleId === sId});
    if(CS.isEmpty(aRemovedIds)) {
      aRules.push(sId);
    }
    oSelectedComponent.dataRules = aRules;
  };

  var successFetchAllCustomComponents = function (oResponse) {
    var oAppData = SettingUtils.getAppData();
    var aAllCustomComponents = oResponse.success;
    oAppData.setAllCustomComponentList(aAllCustomComponents);
    _triggerChange();
  };

  var failureFetchAllCustomComponents = function (oResponse) {
    SettingUtils.failureCallback(oResponse, 'failureFetchAllCustomComponents', getTranslation());
  };

  var _fetchAllCustomComponents = function () {
    SettingUtils.csGetRequest(ProcessRequestMapping.GetAllCustomCompnents, {}, successFetchAllCustomComponents, failureFetchAllCustomComponents);
  };

  let _createInputOutputParameter = function (sParameterId, oParameterTemplate, oInputOutput, BPMNFactory, sCustomElementID, parameterType = "input") {
    let oInputOutputParameter = ElementHelper.createElement(ProcessConstants.EXTENSION_INPUT_PARAMETER, {name: sParameterId, parameterType: parameterType}, oInputOutput, BPMNFactory);
    if (oParameterTemplate.selectionContext == "multiSelectList" || oParameterTemplate.selectionContext == "singleSelectList") {
      let oDefinition = ElementHelper.createElement(ProcessConstants.BO_ELEMENT_LIST, {items: []}, oInputOutputParameter, BPMNFactory);
      oDefinition.items = [];
      oInputOutputParameter.definition = oDefinition;
    }
    else if (oParameterTemplate.type == "tagGroup") {
      let oDefinition = ElementHelper.createElement(ProcessConstants.BO_ELEMENT_MAP, {entries: []}, oInputOutputParameter, BPMNFactory);
      CS.forEach(oParameterTemplate.entries, function (sEntry) {
        let oEntryParameter = ElementHelper.createElement(ProcessConstants.EXTENSION_INPUT_ENTRY, {key: sEntry}, oDefinition, BPMNFactory);
        oDefinition.entries.push(oEntryParameter);
      });
      oInputOutputParameter.definition = oDefinition;
    }
    else if (oParameterTemplate.type == "tagsMap") {
      let oDefinition = ElementHelper.createElement(ProcessConstants.BO_ELEMENT_MAP, {entries: []}, oInputOutputParameter, BPMNFactory);
      // let oEntryParameter = ElementHelper.createElement(ProcessConstants.EXTENSION_INPUT_ENTRY, {key: "" ,value: ""}, oDefinition, BPMNFactory);
      // oDefinition.entries.push(oEntryParameter);
      oInputOutputParameter.definition = oDefinition;
    }
    else if (oParameterTemplate.type == "attributesTypeMap") {
      let oDefinition = ElementHelper.createElement(ProcessConstants.BO_ELEMENT_MAP, {entries: []}, oInputOutputParameter, BPMNFactory);
      // let oEntryParameter = ElementHelper.createElement(ProcessConstants.EXTENSION_INPUT_ENTRY, {key: "" ,value: ""}, oDefinition, BPMNFactory);
      // oDefinition.entries.push(oEntryParameter);
      oInputOutputParameter.definition = oDefinition;
    }

    else if (oParameterTemplate.type == "attributesValueMap") {
      let oDefinition = ElementHelper.createElement(ProcessConstants.BO_ELEMENT_MAP, {entries: []}, oInputOutputParameter, BPMNFactory);
      // let oEntryParameter = ElementHelper.createElement(ProcessConstants.EXTENSION_INPUT_ENTRY, {key: "" ,value: ""}, oDefinition, BPMNFactory);
      // oDefinition.entries.push(oEntryParameter);
      oInputOutputParameter.definition = oDefinition;
    }
    else if (oParameterTemplate.type == "checkbox") {
      oInputOutputParameter.value = oParameterTemplate.defaultValue;
    }else if (oParameterTemplate.type == "list") {
      oInputOutputParameter.value = oParameterTemplate.defaultValue;
    } else if (oParameterTemplate.type == "variableMap") {
      let oDefinition = ElementHelper.createElement(ProcessConstants.BO_ELEMENT_MAP, {entries: []}, oInputOutputParameter, BPMNFactory);
      oInputOutputParameter.definition = oDefinition;
    }
    else if (oParameterTemplate.type == "groupMss") {
      let oDummyData = {
        userIds : [],
        roleIds : []
      };
      oInputOutputParameter.value = JSON.stringify(oDummyData);
    }
    if(oParameterTemplate.type == "text" && !CS.isEmpty(oParameterTemplate.defaultValue)){
      if(oParameterTemplate.id === "taskId"){
        oInputOutputParameter.value = sCustomElementID;
      }else {
        oInputOutputParameter.value = oParameterTemplate.defaultValue;
      }
    }
    return oInputOutputParameter;
  };

  let _addInputOutputExtension = function (oCustomDefinition, oExtensionElement, oParamtersToSet, BPMNFactory) {
    /*****For Input parameters*****/
    let oInputOutput = ElementHelper.createElement(ProcessConstants.BO_ELEMENT_INPUT_OUTPUT,
        {inputParameters: [], outputParameters: []}, oExtensionElement, BPMNFactory);
    let aInputParameters = oInputOutput.inputParameters;
    let aOutputParameters = oInputOutput.outputParameters;


    CS.forEach(oParamtersToSet.inputParameters, function (sParameterId) {
      try {
        let oParameterTemplate = MockDataForProcessInputParameters[sParameterId];
        let oCreatedParameter = _createInputOutputParameter(sParameterId, oParameterTemplate, oInputOutput, BPMNFactory, oCustomDefinition.customElementID, "input");
        if (!CS.isEmpty(oParameterTemplate.dependentInputParameters)) {
          /**Add Default Dependent Input Parameters***/
          let sDefaultValue = oParameterTemplate.defaultValue;
          oCreatedParameter.value = sDefaultValue;
          aInputParameters.push(oCreatedParameter);
          _fillDependentInputParameters(oParameterTemplate, oParameterTemplate.defaultValue, aInputParameters, oInputOutput);
        } else {
          aInputParameters.push(oCreatedParameter);
        }

      } catch (Exception) {
        ExceptionLogger.error("Something went wrong parameter  " + sParameterId);
      }
    });

    /** Separation of Output Parameters **/
    CS.forEach(oParamtersToSet.outputParameters, function (sParameterId) {
      try {
        let oParameterTemplate = MockDataForProcessInputParameters[sParameterId];
        let oCreatedParameter = _createInputOutputParameter(sParameterId, oParameterTemplate, oInputOutput, BPMNFactory, oCustomDefinition.customElementID , "output");
        if (!CS.isEmpty(oParameterTemplate.dependentInputParameters)) {
          /**Add Default Dependent Input Parameters***/
          let sDefaultValue = oParameterTemplate.defaultValue;
          oCreatedParameter.value = sDefaultValue;
          aOutputParameters.push(oCreatedParameter);
          _fillDependentInputParameters(oParameterTemplate, oParameterTemplate.defaultValue, aOutputParameters, oInputOutput);
        } else {
          aOutputParameters.push(oCreatedParameter);
        }

      } catch (Exception) {
        ExceptionLogger.error("Something went wrong parameter  " + sParameterId);
      }
    });

    //adding extra parameters for custom element identification
    aInputParameters.push(ElementHelper.createElement(ProcessConstants.EXTENSION_INPUT_PARAMETER, {name: "customElementID", value: oCustomDefinition.customElementID}, oInputOutput, BPMNFactory));
    aInputParameters.push(ElementHelper.createElement(ProcessConstants.EXTENSION_INPUT_PARAMETER, {name: "customElementType", value: oCustomDefinition.customElementType}, oInputOutput, BPMNFactory));

    oExtensionElement.values.push(oInputOutput);
  };

  let _addListenerExtension = function (oExtensionElement, sListenerType, sDelegateExpression, sEvent, BPMNFactory) {
    let oTaskListener = ElementHelper.createElement(sListenerType, {
      delegateExpression: sDelegateExpression, event: sEvent
    }, oExtensionElement, BPMNFactory);
    oExtensionElement.values.push(oTaskListener);
  };

  let _addOtherExtensionsByCustomElementType = function (oCustomDefinition, oBusinessObject, oBPMNFactory) {
    let oExtensionElement = oBusinessObject.extensionElements;
    let sExtensionType;
    let sDelegateExpression;
    let sEvent = "start";
    switch (oCustomDefinition.customElementType) {
      case ProcessConstants.CUSTOM_ELEMENT_TYPE_USER_TASK:
        sExtensionType = ProcessConstants.EXTENSION_TASK_LISTENER;
        sDelegateExpression = "${camundaUserTask}";
        oBusinessObject.assignee = "admin";
        sEvent = "create";
        break;
      case ProcessConstants.CUSTOM_ELEMENT_TYPE_START_EVENT:
        sExtensionType = ProcessConstants.EXTENSION_EXECUTION_LISTENER;
        sDelegateExpression = "${createProcessInstanceController}";
        let aDocumentation = [];
        aDocumentation.push(ElementHelper.createElement("bpmn:Documentation", {
          text: "customElementID" + SettingUtils.getSplitter() + oCustomDefinition.customElementID
        }, oBusinessObject, oBPMNFactory));
        aDocumentation.push(ElementHelper.createElement("bpmn:Documentation", {
          text: "customElementType"
        }, oBusinessObject, oBPMNFactory));
        oBusinessObject.asyncAfter = true;
        oBusinessObject.documentation = aDocumentation;
        break;
      case ProcessConstants.CUSTOM_ELEMENT_TYPE_END_EVENT:
        /** Adding end event for Process_End **/
        let aEvent = ["start","end"];
        CS.forEach(aEvent , function (sEventName) {
          if(sEventName === "start"){
            sDelegateExpression = "${saveFileInstanceController}";
          }else{
            sDelegateExpression = "${triggerSingleSaveOrCreateAfterImport}";
          }
          _addListenerExtension(oExtensionElement, ProcessConstants.EXTENSION_EXECUTION_LISTENER, sDelegateExpression, sEventName, oBPMNFactory)
        });
        break;

      case ProcessConstants.CUSTOM_ELEMENT_TYPE_SERVICE_TASK:
        oBusinessObject.delegateExpression = oCustomDefinition.delegateExpression;
        break;

      case ProcessConstants.CUSTOM_ELEMENT_TYPE_CALL_ACTIVITY:
        oBusinessObject.calledElement = oCustomDefinition.calledElement;
        let oExtensionElementVariableIn = ElementHelper.createElement(ProcessConstants.BO_ELEMENT_IN, {variables: "all"}, oBusinessObject, oBPMNFactory);
        let oExtensionElementVariableOut = ElementHelper.createElement(ProcessConstants.BO_ELEMENT_OUT, {variables: "all"}, oBusinessObject, oBPMNFactory);
        oBusinessObject.extensionElements.values.push(oExtensionElementVariableIn);
        oBusinessObject.extensionElements.values.push(oExtensionElementVariableOut);
        break;


    }
    !CS.isEmpty(sExtensionType) && _addListenerExtension(oExtensionElement, sExtensionType, sDelegateExpression, sEvent, oBPMNFactory);
  };

  let _addPropertiesToSetInBusinessObject = function (oNewElement, oCustomDefinition, oElementsToSet) {
    //adding properties to input parameters
    let oBusinessObject = oNewElement.businessObject;
    oBusinessObject.name = oCustomDefinition.label; /** To set the Default Name for the Components **/
    let oBPMNFactory = ProcessConfigViewProps.getActiveBPMNFactoryInstance();
    let oExtensionElement = ElementHelper.createElement(ProcessConstants.BO_ELEMENT_EXTENSION_ELEMENTS, {values: []}, oBusinessObject, oBPMNFactory);
    oBusinessObject.extensionElements = oExtensionElement;
    //adding Extensions
    oCustomDefinition.customElementType != ProcessConstants.CUSTOM_ELEMENT_TYPE_START_EVENT && _addInputOutputExtension(oCustomDefinition, oBusinessObject.extensionElements, oElementsToSet, oBPMNFactory);
    _addOtherExtensionsByCustomElementType(oCustomDefinition, oBusinessObject, oBPMNFactory);

  };

  let _processAndSetElements = function (oNewElement, oCustomDefinition, oElementsToSet) {
    let oBusinessObject = oNewElement.businessObject;
    CS.isEmpty(oBusinessObject.extensionElements) && _addPropertiesToSetInBusinessObject(oNewElement, oCustomDefinition, oElementsToSet);
    ProcessConfigViewProps.setActiveBPMNElement(oNewElement);
    let oActiveModelingInstance = ProcessConfigViewProps.getActiveBPMNModelingInstance();
    oActiveModelingInstance.updateProperties(ProcessConfigViewProps.getActiveBPMNElement(), {});
  };

  let _handleProcessAndSetNewElement = function (oNewElement) {
    let oActiveModelingInstance = ProcessConfigViewProps.getActiveBPMNModelingInstance();
    oActiveModelingInstance.updateProperties(oNewElement, {});
  };

  let _handleProcessAndSetNewCustomElement = function (oNewElement, oCustomDefinition, bpmnFactory, modeling, sTaskId, oInputOutputParameters) {
    SettingUtils.csGetRequest(ProcessRequestMapping.GetComponentDetails, {id: oCustomDefinition.customElementID}, successGetComponentsDetailsCallback.bind(this, oNewElement, oCustomDefinition, bpmnFactory, modeling, sTaskId, oInputOutputParameters), failureGetComponentsDetailsCallback)
  }

  let _handleProcessAndSetProperties = function (oNewElement, oCustomDefinition, bpmnFactory, modeling, oResponse) {
    let aInputList = oResponse.success.inputList;
    let aOutputList = oResponse.success.outputList;
    let oElementsToSet = {};
     switch (oCustomDefinition.customElementID) {
        case "receiverTask":
        let aInputListUpdated = ["INPUT_METHOD", "taskId"];
        oElementsToSet.inputParameters = aInputListUpdated;
        oElementsToSet.outputParameters = aOutputList;
        break;

       case "JSONToPXONTask":
       case "relationshipInstanceDeleteTask":
       case "relationshipInstanceUpdateTask":
       case "relationshipInstanceCreateTask":
       case "relationshipInstanceReadTask":
       case "PXONToJSONTask":
       case "PXONToExcelTask":
       case "excelToPXONTask":
       case "PXONImportTask":
       case "PXONExportTask":
       case "configPXONToExcelTask":
       case "upsertAssetToPXONTask":
       case "storageTask":
       case "zipExtractionTask":
       case "PXONImportActivity":
       case "listExtendTask":
       case "listSearchIndexTask":
       case "listInsertPositionTask":
       case "listRemovePositionTask":
       case "listRemoveFirstOccurrenceTask":
       case "listRemoveAllOccurrencesTask":
       case "assetExpirationTask":
       case "booleanCheckTask":
       case "executionStatusCheckTask":
       case "listReverseTask":
       case "listSortTask":
       case "assignVariablesTask":
       case "listAppendTask":
       case "listClearTask":
       case "listCountElementTask":
       case "configAPITask":
       case "customUserTask":
       case "getAttributesAndTagsTask":
       case "talendTask":
       case "JMSAcknowledgementTask":
       case "configPXONToJSONTask":
	   case "assetPurgingTask":
	   case "readJMSRequestParams":
	   case "mapCodesToIIDs":
         aInputList.push("taskId");
         oElementsToSet.inputParameters = aInputList;
         oElementsToSet.outputParameters = aOutputList;
         break;

       case "deliveryTask":
         oElementsToSet.inputParameters = ["OUTPUT_METHOD", "taskId"];
         oElementsToSet.outputParameters = aOutputList;
         break;

       case "relationshipInstanceTask":
         oElementsToSet.inputParameters = ["ACTION", "taskId"];
         oElementsToSet.outputParameters = aOutputList;
         break;

       case "PXONExportActivity":
         oElementsToSet.inputParameters = ["PRIORITY", "EXPORT_TYPE", "taskId"];
         oElementsToSet.outputParameters = aOutputList;
         break;

       case "setAttributesAndTagsTask":
         oElementsToSet.inputParameters = ["ENTITY_TYPE","KLASS_INSTANCE_ID","TAG_VALUES_MAP","ATTRIBUTES_VALUES_MAP","ATTRIBUTES_TYPES_MAP","IS_TRIGGERED_THROUGH_SCHEDULER", "taskId"];
         oElementsToSet.outputParameters = aOutputList;
         break;

       case "transferActivity":
         oElementsToSet.inputParameters = ["BASE_ENTITY_IIDS", "DESTINATION_CATALOG_ID", "DESTINATION_ORGANIZATION_ID", "PARTNER_AUTHORIZATION_ID", "IS_REVISIONABLE_TRANSFER", "taskId"];
         oElementsToSet.outputParameters = aOutputList;
         break;

       case "configToPXONTask":
         oElementsToSet.inputParameters = ["RECEIVED_DATA", "taskId", "IS_CONFIG_TYPE_PERMISSION"];
         oElementsToSet.outputParameters = aOutputList;
         break;

       case "notificationTask":
         oElementsToSet.inputParameters = ["NOTIFICATION_SUBJECT", "NOTIFICATION_BODY", "NOTIFICATION", "taskId"];
         oElementsToSet.outputParameters = aOutputList;
         break;
    }
    ProcessConfigViewProps.setActiveBPMNModelingInstance(modeling);
    ProcessConfigViewProps.setActiveBPMNFactoryInstance(bpmnFactory);
    _processAndSetElements(oNewElement, oCustomDefinition, oElementsToSet);
  };

  let successGetComponentsDetailsCallback = function (oNewElement, oCustomDefinition, bpmnFactory, modeling, sTaskId, oInputOutputParameters, oResponse){
    if(CS.isNotEmpty(sTaskId)){
      let oOldElement = ProcessConfigViewProps.getActiveBPMNElement();
      let aInputList = oResponse.success.inputList;
      let aOutputList = oResponse.success.outputList;
      let aInputParameters = oInputOutputParameters.inputParameters;
      let aOutputParameters = oInputOutputParameters.outputParameters;
      CS.forEach(aOutputList, function (sOutputParameter) {
        let oFoundParameter = CS.find(aInputParameters, {name : sOutputParameter});
        if(CS.isNotEmpty(oFoundParameter)){
          aOutputParameters.push(oFoundParameter);
          CS.remove(aInputParameters,{name : sOutputParameter});
        }
      });
      if (oNewElement == oOldElement) return; /** updateProperties is going in infinite loop so breaking the loop by comparing newElement with oldElement**/
      ProcessConfigViewProps.setActiveBPMNElement(oNewElement);
      ProcessConfigViewProps.setActiveBPMNFactoryInstance(bpmnFactory);
      ProcessConfigViewProps.setActiveBPMNModelingInstance(modeling);
      _prepareAdvancedFilterData(oNewElement);
      _setSearchCriteria(oNewElement,"getComponent");
      // modeling.updateProperties(oNewElement, {}); /** Update properties **/
      _triggerChange();
    }else{
      _handleProcessAndSetProperties(oNewElement, oCustomDefinition, bpmnFactory, modeling, oResponse);
    }
  };

  let failureGetComponentsDetailsCallback = function(oResponse){
    SettingUtils.failureCallback(oResponse, 'failureGetComponentsDetailsCallback', getTranslation());
  };

  let _getInputParameterFromActiveElementByName = function (sName) {
    let oElement = ProcessConfigViewProps.getActiveBPMNElement();
    return _getInputParameterFromElementByName(oElement, sName);
  };

  let _getInputParameterFromElementByName = function (oElement, sName) {
    let oExtensionElements = (oElement.businessObject && oElement.businessObject.extensionElements) || oElement.extensionElements;
    let oValue = oExtensionElements.values[0];
    let aInputParameters = oValue.inputParameters;
    return CS.find(aInputParameters, {name: sName});
  };

  let _getInputOutputFromActiveElement = function () {
    let oElement = ProcessConfigViewProps.getActiveBPMNElement();
    let oBusinessObject = oElement.businessObject;
    let oExtensionElements = oBusinessObject.extensionElements;
    return oExtensionElements.values[0];
  };

  let _fillDependentInputParameters = function (oParameterTemplate, values, aInputParameters, oInputOutput) {
    let aDependentInputParameters = oParameterTemplate.dependentInputParameters;
    let oActiveBPMNFactory = ProcessConfigViewProps.getActiveBPMNFactoryInstance();
    let aInputParameterIndices;
    if (CS.isArray(values)) {
      aInputParameterIndices = values;
    } else {
      aInputParameterIndices = oParameterTemplate.inputParameterToShowIndicesByValueMap[values] || [];
    }
    CS.forEach(aInputParameterIndices, (sParameterIndex) => {
      let sDependentIPParameterId = aDependentInputParameters[sParameterIndex];
      let oDependentParameterTemplate = MockDataForProcessInputParameters[sDependentIPParameterId];
      let oCreatedParameter = _createInputOutputParameter(sDependentIPParameterId, oDependentParameterTemplate, oInputOutput, oActiveBPMNFactory);
      if (!CS.isEmpty(oDependentParameterTemplate.dependentInputParameters)) {
        let sDefaultValue = oDependentParameterTemplate.defaultValue;
        oCreatedParameter.value = sDefaultValue;
        aInputParameters.push(oCreatedParameter);
        _fillDependentInputParameters(oDependentParameterTemplate, sDefaultValue, aInputParameters, oInputOutput);
      } else {
        aInputParameters.push(oCreatedParameter);
      }
    });
  };

  let _fillDependentParametersToRemove = function (aDependentInputParameters, aDependentParametersToRemove) {
    CS.forEach(aDependentInputParameters, function (sParameterId) {
      aDependentParametersToRemove.push({name: sParameterId});
      let oDependentParameterTemplate = MockDataForProcessInputParameters[sParameterId];
      if (!CS.isEmpty(oDependentParameterTemplate.dependentInputParameters)) {
        _fillDependentParametersToRemove(oDependentParameterTemplate.dependentInputParameters, aDependentParametersToRemove);
      }
    });
  };

  let _handleIsAdvancedOptionsEnabledToggle = function () {
    ProcessConfigViewProps.setIsAdvancedOptionsEnabled(!ProcessConfigViewProps.getIsAdvancedOptionsEnabled());
    _triggerChange();
  };

  let _handleBPMNCustomElementSingleValueChanged = function (sName, sNewVal) {
    let oInputOutput = _getInputOutputFromActiveElement();
    let aInputParameters = oInputOutput.inputParameters;
    let aOutputParameters = oInputOutput.outputParameters;
    let iCurrentParameterIndex = CS.findIndex(aInputParameters, {name: sName});
    let iCurrentParameterIndexOutput = CS.findIndex(aOutputParameters, {name: sName});
    let oActiveModelingInstance = ProcessConfigViewProps.getActiveBPMNModelingInstance();
    if (iCurrentParameterIndex > -1) {
      let oFoundIPParam = aInputParameters[iCurrentParameterIndex];
      oFoundIPParam.value = sNewVal;
      let oParameterTemplate = MockDataForProcessInputParameters[sName];
      if (!CS.isEmpty(oParameterTemplate.dependentInputParameters)) {
        let aDependentInputParameters = oParameterTemplate.dependentInputParameters;
        let aItemsToRemove = [];
        _fillDependentParametersToRemove(aDependentInputParameters, aItemsToRemove);
        CS.forEach(aItemsToRemove, function (oItemToRemove) {
          CS.remove(aInputParameters, oItemToRemove);
        });
        let aSplicedInputParameters = aInputParameters.splice(iCurrentParameterIndex + 1);

        _fillDependentInputParameters(oParameterTemplate, sNewVal, aInputParameters, oInputOutput);
        aInputParameters.push(...aSplicedInputParameters);
      }

      /** this check is to hide Multiclassification & TaxonomyColumns for Article Variant Import/Export **/
      if (sName === "isAttributeVariant") {
        let aParameters = oParameterTemplate.parametersToHide;
        if (sNewVal === "true") {
          CS.forEach(aParameters, function (sName) {
            let iCurrentParameterIndex = CS.findIndex(aInputParameters, {name: sName});
            if (iCurrentParameterIndex !== -1) {
              let oFoundIPParam = aInputParameters[iCurrentParameterIndex];
              CS.remove(aInputParameters, oFoundIPParam);
            }
          })
        } else if (sNewVal === "false") {
          let oActiveBPMNFactory = ProcessConfigViewProps.getActiveBPMNFactoryInstance();
          CS.forEach(aParameters, function (sParameterId) {
            let oTemplate = MockDataForProcessInputParameters[sParameterId];
            let oCreatedParameter = _createInputOutputParameter(sParameterId, oTemplate, oInputOutput, oActiveBPMNFactory);
            let sDefaultValue = oTemplate.defaultValue;
            oCreatedParameter.value = sDefaultValue;
            aInputParameters.push(oCreatedParameter);
          })
        }
      }
      if(sName === "IS_CONFIG_TYPE_PERMISSION"){
        let iNodeTypeIndex = CS.findIndex(aInputParameters, {name: "NODE_TYPE"});
        let oFoundIPParamNodeType = aInputParameters[iNodeTypeIndex];
        if (sNewVal === "true") {
          oFoundIPParamNodeType.value = "PERMISSION";
        }else{
          oFoundIPParamNodeType.value = "";
        }
      }
      if(sName === "EXPORT_SUB_TYPE"){
        let oSearchCriteria = ProcessFilterProps.getSearchCriteria();
        oSearchCriteria.exportSubType = sNewVal;
        oSearchCriteria.collectionId = "";
        oSearchCriteria.bookmarkId = "";
        switch (sNewVal){
          case "TAXONOMY_BASED_EXPORT":
            oSearchCriteria.baseEntityIIds = [];
            break;
          case "EXPORT_SELECTED":
            oSearchCriteria.selectedTypes = [];
            oSearchCriteria.selectedTaxonomyIds = [];
            break;
          case "EXPORT_ALL":
          case "WITHOUT_TAXONOMY_EXPORT":
          case "COLLECTION_EXPORT_WITH_CHILD":
          case "COLLECTION_EXPORT_WITHOUT_CHILD":
          case "BOOKMARK_EXPORT":
            oSearchCriteria.selectedTypes = [];
            oSearchCriteria.selectedTaxonomyIds = [];
            oSearchCriteria.baseEntityIIds = [];
            break;
        }
        ProcessFilterProps.setSearchCriteria(oSearchCriteria);
        _handleBPMNCustomElementSingleValueChanged("SEARCH_CRITERIA",JSON.stringify(oSearchCriteria));
      }
      else if (sName === "BASE_ENTITY_IIDS"){
        let oSearchCriteria = ProcessFilterProps.getSearchCriteria();
        oSearchCriteria.baseEntityIIds = sNewVal;
        ProcessFilterProps.setSearchCriteria(oSearchCriteria);
        _handleBPMNCustomElementSingleValueChanged("SEARCH_CRITERIA",JSON.stringify(oSearchCriteria));
      }
      else if(sName === "MANUAL_SEARCH_CRITERIA"){
        if(sNewVal === "false"){
          ProcessFilterProps.setSearchCriteria({});
          _handleBPMNCustomElementSingleValueChanged("SEARCH_CRITERIA",JSON.stringify({}));
        }
      }
      else if(sName === "COLLECTION_ID"){
        let oSearchCriteria = ProcessFilterProps.getSearchCriteria();
        oSearchCriteria.collectionId = JSON.parse(sNewVal);
        ProcessFilterProps.setSearchCriteria(oSearchCriteria);
        _handleBPMNCustomElementSingleValueChanged("SEARCH_CRITERIA",JSON.stringify(oSearchCriteria));
      }
      else if(sName === "BOOKMARK_ID"){
        let oSearchCriteria = ProcessFilterProps.getSearchCriteria();
        oSearchCriteria.bookmarkId = JSON.parse(sNewVal);
        ProcessFilterProps.setSearchCriteria(oSearchCriteria);
        _handleBPMNCustomElementSingleValueChanged("SEARCH_CRITERIA",JSON.stringify(oSearchCriteria));
      }
    }else {
      let oFoundIPParam = aOutputParameters[iCurrentParameterIndexOutput];
      oFoundIPParam.value = sNewVal;
      let oParameterTemplate = MockDataForProcessInputParameters[sName];
      if (!CS.isEmpty(oParameterTemplate.dependentInputParameters)) {
        let aDependentInputParameters = oParameterTemplate.dependentInputParameters;
        let aItemsToRemove = [];
        _fillDependentParametersToRemove(aDependentInputParameters, aItemsToRemove);
        CS.forEach(aItemsToRemove, function (oItemToRemove) {
          CS.remove(aOutputParameters, oItemToRemove);
        });
        let aSplicedInputParameters = aOutputParameters.splice(iCurrentParameterIndexOutput + 1);

        _fillDependentInputParameters(oParameterTemplate, sNewVal, aOutputParameters, oInputOutput);
        aOutputParameters.push(...aSplicedInputParameters);
      }
    }
    oActiveModelingInstance.updateProperties(ProcessConfigViewProps.getActiveBPMNElement(), {});
  };

  let _handleBPMNTextValueChanged = function (sName, sSelectedAttributeId, sNewVal) {
    let oInputOutput = _getInputOutputFromActiveElement();
    let aInputParameters = oInputOutput.inputParameters;
    let iCurrentParameterIndex = CS.findIndex(aInputParameters, {name: sName});
    let oFoundIPParam = aInputParameters[iCurrentParameterIndex];
    let oActiveModelingInstance = ProcessConfigViewProps.getActiveBPMNModelingInstance();
    let oDefinition = oFoundIPParam.definition;
    let aEntries = oDefinition.entries;
    let oEntry = CS.find(aEntries,{key:sSelectedAttributeId});
    oEntry.value = sNewVal;
    oActiveModelingInstance.updateProperties(ProcessConfigViewProps.getActiveBPMNElement(), {});
  };

  let _handleBPMNCustomElementMSSChanged = function (oActiveProcess, sName, aSelectedItems) {
    let oFoundIPParam = _getInputParameterFromActiveElementByName(sName);
    let oActiveModelingInstance = ProcessConfigViewProps.getActiveBPMNModelingInstance();
    let oDefinition = oFoundIPParam.definition;

    oDefinition.items = [];
    let oBPMNFactory = ProcessConfigViewProps.getActiveBPMNFactoryInstance();
    let aNewItems = [];
    CS.forEach(aSelectedItems, function (sSelectedElement) {
      aNewItems.push(ElementHelper.createElement("camunda:Value", {value: sSelectedElement}, oDefinition, oBPMNFactory));
    });
    if(sName === "SELECTED_BASETYPES"){
      let oSearchCriteria = ProcessFilterProps.getSearchCriteria();
      oSearchCriteria.selectedBaseTypes = aSelectedItems;
      ProcessFilterProps.setSearchCriteria(oSearchCriteria);
      _handleBPMNCustomElementSingleValueChanged("SEARCH_CRITERIA",JSON.stringify(oSearchCriteria));
    }

    let oInputOutput = _getInputOutputFromActiveElement();
    let aInputParameters = oInputOutput.inputParameters;
    let oParameterTemplate = MockDataForProcessInputParameters[sName];
    let aDependentInputParameters = oParameterTemplate.dependentInputParameters;
    let iCurrentParameterIndex = CS.findIndex(aInputParameters, {name: sName});

    if (iCurrentParameterIndex > -1 && !CS.isEmpty(aDependentInputParameters)) {
      let aInputParameterList = CS.map(aInputParameters,"name");
      let aSelectedDependentParameter = _combineSelectedDependentParameter(aSelectedItems, oParameterTemplate);
      let aParametersToRemove = CS.difference(aDependentInputParameters, aSelectedDependentParameter);
      let aItemsToRemove = [];
      _fillDependentParametersToRemove(aParametersToRemove, aItemsToRemove);
      CS.forEach(aItemsToRemove, function (oItemToRemove) {
        CS.remove(aInputParameters, oItemToRemove);
      });
      let aSplicedInputParameters = aInputParameters.splice(iCurrentParameterIndex + 1);
      let aParametersToFill = CS.difference(aSelectedDependentParameter, aInputParameterList);
      let aParameterIndicesToFill = CS.map(aParametersToFill, sItem => CS.indexOf(aDependentInputParameters, sItem));
      _fillDependentInputParameters(oParameterTemplate, aParameterIndicesToFill, aInputParameters, oInputOutput);
      aInputParameters.push(...aSplicedInputParameters);
    }
    oDefinition.items = aNewItems;
    oActiveModelingInstance.updateProperties(ProcessConfigViewProps.getActiveBPMNElement(), {});
  };

  /**Function to combine selected dependent parameters */
  let _combineSelectedDependentParameter = function (aValues, oParameterTemplate) {
    let aInputParameterIndices = [];
    CS.forEach(aValues, (sValue) => {
      let aSingleInputParameterIndices = oParameterTemplate.inputParameterToShowIndicesByValueMap[sValue] || [];
      aInputParameterIndices = CS.union(aInputParameterIndices, aSingleInputParameterIndices);
    });
    return CS.map(aInputParameterIndices, (index) => oParameterTemplate.dependentInputParameters[index]);
  }

  let _handleBPMNElementsAddRowButtonClicked = function (sName) {
    let oActiveModelingInstance = ProcessConfigViewProps.getActiveBPMNModelingInstance();
    let oFoundIPParam = _getInputParameterFromActiveElementByName(sName);
    let oDefinition = oFoundIPParam.definition;
    let oBPMNFactory = ProcessConfigViewProps.getActiveBPMNFactoryInstance();
    (!oDefinition.items) && (oDefinition.items = []);

    oDefinition.items.push(ElementHelper.createElement("camunda:Value", {value: ""}, oDefinition, oBPMNFactory));
    oActiveModelingInstance.updateProperties(ProcessConfigViewProps.getActiveBPMNElement(), {});
  };

  var _handleProcessComponentClassInfoAddTagGroup = function (sName) {
    let oActiveModelingInstance = ProcessConfigViewProps.getActiveBPMNModelingInstance();
    let oFoundIPParam = _getInputParameterFromActiveElementByName(sName);
    let oDefinition = oFoundIPParam.definition;
    let oBPMNFactory = ProcessConfigViewProps.getActiveBPMNFactoryInstance();
    (!oDefinition.entries) && (oDefinition.entries= []);
    oDefinition.entries.push(ElementHelper.createElement(ProcessConstants.EXTENSION_INPUT_ENTRY, {key: "",value:""}, oDefinition, oBPMNFactory));
    oActiveModelingInstance.updateProperties(ProcessConfigViewProps.getActiveBPMNElement(), {});
  };

  var _handleProcessComponentClassInfoRemoveTagGroup = function (sName, iIndex) {
    let oActiveModelingInstance = ProcessConfigViewProps.getActiveBPMNModelingInstance();
    let oFoundIPParam = _getInputParameterFromActiveElementByName(sName);
    let oDefinition = oFoundIPParam.definition;
    let aEntries = oDefinition.entries;
    aEntries.splice(iIndex,1);
    oActiveModelingInstance.updateProperties(ProcessConfigViewProps.getActiveBPMNElement(), {});
  }

  var _handleProcessComponentClassInfoAddAttributeGroup = function (sName) {
    let oActiveModelingInstance = ProcessConfigViewProps.getActiveBPMNModelingInstance();
    _handleProcessComponentClassInfoAddAttributeValueGroup("ATTRIBUTES_TYPES_MAP");
    _handleProcessComponentClassInfoAddAttributeValueGroup("ATTRIBUTES_VALUES_MAP");
    oActiveModelingInstance.updateProperties(ProcessConfigViewProps.getActiveBPMNElement(), {});
  };

  var _handleProcessComponentClassInfoAddAttributeValueGroup = function (sName) {
    let oFoundIPParam = _getInputParameterFromActiveElementByName(sName);
    let oDefinition = oFoundIPParam.definition;
    let oBPMNFactory = ProcessConfigViewProps.getActiveBPMNFactoryInstance();
    (!oDefinition.entries) && (oDefinition.entries= []);
    oDefinition.entries.push(ElementHelper.createElement(ProcessConstants.EXTENSION_INPUT_ENTRY, {key: "",value:""}, oDefinition, oBPMNFactory));
  };

  var _handleProcessComponentClassInfoRemoveAttributeGroup = function (sName, iIndex) {
    let oActiveModelingInstance = ProcessConfigViewProps.getActiveBPMNModelingInstance();
    _handleProcessComponentClassInfoRemoveAttributeFromMap("ATTRIBUTES_TYPES_MAP",iIndex);
    _handleProcessComponentClassInfoRemoveAttributeFromMap("ATTRIBUTES_VALUES_MAP",iIndex);
    oActiveModelingInstance.updateProperties(ProcessConfigViewProps.getActiveBPMNElement(), {});

  }

  var _handleProcessComponentClassInfoRemoveAttributeFromMap = function (sName, iIndex) {
    let oFoundIPParam = _getInputParameterFromActiveElementByName(sName);
    let oDefinition = oFoundIPParam.definition;
    let aEntries = oDefinition.entries;
    aEntries.splice(iIndex,1);
  }

  let _handleBPMNPropertiesAttributeDateChangedCustom =function (sName, sSelectedAttributeId, sNewVal) {
    let oActiveModelingInstance = ProcessConfigViewProps.getActiveBPMNModelingInstance();
    let oInputOutput = _getInputOutputFromActiveElement();
    let aInputParameters = oInputOutput.inputParameters;
    let iCurrentParameterIndex = CS.findIndex(aInputParameters, {name: sName});
    let oFoundIPParam = aInputParameters[iCurrentParameterIndex];
    let oDefinition = oFoundIPParam.definition;
    let aEntries = oDefinition.entries;
    let oEntry = CS.find(aEntries, {key: sSelectedAttributeId});
    oEntry.value = sNewVal.toString();
    oActiveModelingInstance.updateProperties(ProcessConfigViewProps.getActiveBPMNElement(), {});
  };

  var _handleBPMNPropertiesTagMSSChangedCustom = async function (sKey, sStatus, sSelectedTagGroup, aSelectedItems, oReferencedData) {
    let oActiveModelingInstance = ProcessConfigViewProps.getActiveBPMNModelingInstance();
    let oFoundIPParam = _getInputParameterFromActiveElementByName("TAG_VALUES_MAP");
    let oActiveProcess = ProcessConfigViewProps.getActiveProcess();
    if(!oActiveProcess.configDetails){
      oActiveProcess.configDetails = {
        referencedTags: {}
      };
    }
    let oConfigDetails = oActiveProcess.configDetails;
    CS.assign(oConfigDetails.referencedTags, oReferencedData);
    let oDefinition = oFoundIPParam.definition;
    let aEntries = oDefinition.entries;
    if(sStatus == "tagKey"){
      let oFoundKeyEntry = CS.find(aEntries, {key: sKey});
      let sNewKey = CS.isEmpty(aSelectedItems[0]) ? "" : aSelectedItems[0];
      oFoundKeyEntry.key = sNewKey;
      oFoundKeyEntry.value = "";
      /** Only for boolean tag **/
      if(oConfigDetails.referencedTags[sNewKey].tagType === TagTypeConstants.TAG_TYPE_BOOLEAN){
        let oFoundValueEntry = CS.find(aEntries, {key: sNewKey});
        let oTagValuesData = {
          searchColumn: "label",
          searchText: "",
          entities: {
            tagValues:
                {
                  from: 0,
                  size:20,
                  sortBy:"label",
                  sortOrder:"asc",
                  types:[],
                  typesToExclude:[],
                  tagGroupId: sNewKey
                }
          }
        }
        await SettingUtils.csPostRequest(ProcessRequestMapping.GetConfigData, {}, oTagValuesData, successTagValuesList, failureTagValuesList).then((sSelectedTagValue) => {
          oFoundValueEntry.value = sSelectedTagValue;
        });
      }
    } else if(sStatus == "tagValue"){
      let oFoundValueEntry = CS.find(aEntries, {key: sSelectedTagGroup});
      let sNewValue = CS.isEmpty(aSelectedItems) ? "" : aSelectedItems;
      oFoundValueEntry.value = sNewValue.toString();
    }
    oActiveModelingInstance.updateProperties(ProcessConfigViewProps.getActiveBPMNElement(), {});
  };

  let successTagValuesList = function(oResponse){
    let aTagValueList = oResponse.success['tagValues'].list;
    let sSelectedTagValue = aTagValueList[0].id;
    return sSelectedTagValue;
  };

  let failureTagValuesList = function(oResponse){
    SettingUtils.failureCallback(oResponse, 'failureTagValuesList', getTranslation());
  };

  let _removeZeroValuesFromFilterTags = function(aTags){
    var aRes = CS.cloneDeep(aTags) || [];
    var aObjToRemove = [];

    CS.forEach(aRes, function(oTag){
      if(oTag.type === TagTypeConstants.YES_NEUTRAL_TAG_TYPE || oTag.type === TagTypeConstants.RULER_TAG_TYPE || oTag.type === TagTypeConstants.TAG_TYPE_MASTER || oTag.type === TagTypeConstants.LIFECYCLE_STATUS_TAG_TYPE){
        CS.remove(oTag.children, {from:0, to:0});
        if(oTag.children && CS.isEmpty(oTag.children)){
          aObjToRemove.push(oTag);
        }
        CS.remove(oTag.mandatory, {from:0, to:0});
        CS.remove(oTag.should, {from:0, to:0});
      }else{
        if(oTag.users && CS.isEmpty(oTag.users)){
          aObjToRemove.push(oTag);
        }
      }
    });

    CS.forEach(aObjToRemove, function (oObjToRemove) {
      var iIndex = CS.findIndex(aRes, {id:oObjToRemove.id});
      aRes.splice(iIndex, 1);
    });

    return aRes;
  };

  let _getDefaultAttributeChildData = function (bIsAdvancedFilterClicked, oAttr) {
    var sValue = oAttr.label;
    var sType = "exact";
    var bIsAdvancedFilter = false;

    if (oAttr.advancedSearchFilter) {
      sValue = oAttr.value;
      sType = oAttr.type;
      bIsAdvancedFilter = true;
    } else if (bIsAdvancedFilterClicked) {
      sType = oAttr.type;
      bIsAdvancedFilter = true;
    }
    return {
      "id": oAttr.id,
      "type": sType,
      "baseType": "com.cs.core.runtime.interactor.model.filter.FilterValueMatchModel",
      "value": sValue,
      "defaultUnit": "",
      "advancedSearchFilter": bIsAdvancedFilter
    }
  };

  let _getDefaultNumberAttributeChildData = function (bIsAdvancedFilterClicked, oAttr) {
    var sType = "range";
    var bIsAdvancedFilter = false;

    if (oAttr.advancedSearchFilter || bIsAdvancedFilterClicked) {
      sType = oAttr.type;
      bIsAdvancedFilter = true;
    }
    return {
      "id": oAttr.id,
      "type": sType,
      "baseType": "com.cs.core.runtime.interactor.model.filter.FilterValueRangeModel",
      "to": oAttr.to,
      "from": oAttr.from,
      "defaultUnit": "",
      "advancedSearchFilter": bIsAdvancedFilter
    }
  };

  let _makeDefaultAttributeFilterData = function (oAppliedFilter, bIsAdvancedFilterClicked) {
    var _this = this;
    var aChildrenModel = [];
    var aRangeTypeAttributes = SettingUtils.getAllNumericTypeAttributes();

    if (oAppliedFilter.advancedSearchFilter || bIsAdvancedFilterClicked) {
      CS.forEach(oAppliedFilter.children, function (oChildFilter) {
        if (oChildFilter.type === "range") {
          aChildrenModel.push(_getDefaultNumberAttributeChildData(bIsAdvancedFilterClicked, oChildFilter));
        } else {
          aChildrenModel.push(_getDefaultAttributeChildData(bIsAdvancedFilterClicked, oChildFilter));
        }
      });
    } else {
      if (CS.includes(aRangeTypeAttributes, oAppliedFilter.type)) {
        aChildrenModel = CS.map(oAppliedFilter.children, _getDefaultNumberAttributeChildData.bind(_this, bIsAdvancedFilterClicked))
      } else {
        aChildrenModel = CS.map(oAppliedFilter.children, _getDefaultAttributeChildData.bind(_this, bIsAdvancedFilterClicked))
      }
    }

    var bIsAdvancedFilter = oAppliedFilter.advancedSearchFilter || bIsAdvancedFilterClicked || false;
    return {
      "id": oAppliedFilter.id,
      "type": oAppliedFilter.type,
      "mandatory": aChildrenModel,
      "should": [],
      "label": oAppliedFilter.label,
      "advancedSearchFilter": bIsAdvancedFilter
    };

  };

  let _makeDataForAppliedAttributeAndTags = function (aAppliedFilterData, bIsAdvancedFilterClicked) {
    let aFilterDataToProcess = _removeZeroValuesFromFilterTags(aAppliedFilterData);
    var aAppliedTags = [];
    var aAppliedAttributes = [];
    CS.forEach(aFilterDataToProcess, function (oProperty) {
      if (SettingUtils.isTag(oProperty.type)) {
        aAppliedTags.push(oProperty);
        oProperty.mandatory = oProperty.children;
      }
      else if(oProperty.type && CS.includes(oProperty.type.toLowerCase(), "attribute")){
        aAppliedAttributes.push(_makeDefaultAttributeFilterData(oProperty, bIsAdvancedFilterClicked));
      }
      delete oProperty.children;
    });

    return {
      tags: aAppliedTags,
      attributes: aAppliedAttributes
    }
  };

  let _handleBPMNPropertiesAdvancedFilterChangedCustom = function () {
    let oAppliedFilter = ProcessFilterProps.getAppliedFiltersClone();
    let oFilterData = _makeDataForAppliedAttributeAndTags(oAppliedFilter);
    let oActiveModelingInstance = ProcessConfigViewProps.getActiveBPMNModelingInstance();
    let oFoundIPParam = _getInputParameterFromActiveElementByName("Input_filterModel");
    oFoundIPParam.value = JSON.stringify(oFilterData);
    delete oFoundIPParam.originalValue;
    oActiveModelingInstance.updateProperties(ProcessConfigViewProps.getActiveBPMNElement(), {});
  };

  var _getDefaultAppliedFilterData = function (oParent) {

    return {
      "id": oParent.id,
      "children": [],
      "type": oParent.type,
      "label": oParent.label,
      "isHiddenInAdvancedFilters": oParent.isHiddenInAdvancedFilters
    }
  };

  let _setAppliedFilterData = function(oAppliedFilterData) {
    let aAttributes = oAppliedFilterData.attributes;
    let aTags = oAppliedFilterData.tags;
    let oLoadedAttributes = ProcessConfigViewProps.getLoadedAttributes();
    let oLoadedTags = ProcessConfigViewProps.getLoadedTags();

    /*//added for bookmark handling.
    CS.forEach(oConfigDetails.referencedAttributes, function (oAttribute, sAttributeId) {
      oLoadedAttributes[sAttributeId] = oAttribute;
    });
    CS.forEach(oConfigDetails.referencedTags, function (oTag, sTagId) {
      oLoadedTags[sTagId] = oTag;
    });
    */


    //attributes
    var aAppliedFilters = [];
    CS.forEach(aAttributes, function (oAttribute) {
      var oAppliedAttribute = _getDefaultAppliedFilterData(oAttribute);
      if(oAttribute.advancedSearchFilter){
        oAppliedAttribute.advancedSearchFilter = true;
      }

      var oLoadedAttr = oLoadedAttributes[oAttribute.id];
      oAppliedAttribute.label = oLoadedAttr.label;

      CS.forEach(oAttribute.mandatory, function (oChild) {
        oChild.label = oChild.value; //label not in response data
        oAppliedAttribute.children.push(oChild);
      });
      aAppliedFilters.push(oAppliedAttribute);
    });


    //tags
    let aMasterTagList = [];
    CS.forEach(aTags, function (oTag) {
      let oMasterTag = oLoadedTags[oTag.id] || SettingUtils.getNodeFromTreeListById(aMasterTagList, oTag.id);
      /*if (CS.isEmpty(oMasterTag)) {
        oMasterTag = CS.find(oFilterInfo.defaultFilterTags, {id: oTag.id}) || CS.find(oFilterInfo.filterData, {id: oTag.id}) || {};
      }*/
      let oAppliedTag = _getDefaultAppliedFilterData(oTag);
      oAppliedTag.label = oMasterTag.label;
      if(oTag.advancedSearchFilter){
        ProcessFilterProps.setAdvancedFilterAppliedStatus(true);
      }
      CS.forEach(oTag.mandatory, function (oChild) {
        let oMasterChild = oLoadedTags[oChild.id] || {};
        oChild.label = oMasterChild.label;
        oAppliedTag.children.push(oChild);

      });
/*
      if (oAppliedTag.children.length !== CS.size(oMasterTag)) {
        let aRemainingTagChildren = CS.differenceBy(oMasterTag, oAppliedTag.children, 'id');
        CS.forEach(aRemainingTagChildren, function (oTagChild) {
          oAppliedTag.children.push(_getDefaultTagChild(oTagChild));
        });
      }*/
      aAppliedFilters.push(oAppliedTag);
    });

    ProcessFilterProps.setAppliedFilters(aAppliedFilters);
  };

  let _createAppliedFilterCollapseStatusMap = function () {
    var aAppliedFilters = ProcessFilterProps.getAppliedFilters();
    var oAppliedFilterCollapseStatusMap = ProcessFilterProps.getAppliedFilterCollapseStatusMap();
    CS.forEach(aAppliedFilters, function (oAppliedFilter) {
      if(!CS.isEmpty(oAppliedFilterCollapseStatusMap[oAppliedFilter.id])){
        oAppliedFilterCollapseStatusMap[oAppliedFilter.id].isCollapsed = false;
      }else {
        oAppliedFilterCollapseStatusMap[oAppliedFilter.id] = {isCollapsed: false};
      }
    });
  };

  /**
   * @function _prepareAdvancedFilterData
   * @description convert xml data into required format to update filter data for tag/attributes
   * @param oElement
   * @private
   */
  let _prepareAdvancedFilterData = function (oElement) {
    ProcessFilterProps.reset();
    let oFoundIPParam = _getInputParameterFromElementByName(oElement,"Input_filterModel");
    if(oFoundIPParam){
      if(!CS.isEmpty(oFoundIPParam.value)) {
        let oAppliedFilter = JSON.parse(oFoundIPParam.value);
        _setAppliedFilterData(oAppliedFilter);
        _createAppliedFilterCollapseStatusMap();
        oFoundIPParam.originalValue = JSON.stringify(ProcessFilterProps.getAppliedFilters());
      }
    }
  };

  var _handleBPMNPropertiesAttributeMSSChangedCustom = function (sKey, sStatus, sSelectedTagGroup, aSelectedItems, oReferencedData) {
    let oActiveModelingInstance = ProcessConfigViewProps.getActiveBPMNModelingInstance();
    let oFoundIPParam = _getInputParameterFromActiveElementByName("ATTRIBUTES_TYPES_MAP");
    let oActiveProcess = ProcessConfigViewProps.getActiveProcess();
    if(!oActiveProcess.configDetails){
      oActiveProcess.configDetails = {
        referencedAttributes: {}
      };
    }
    let oConfigDetails = oActiveProcess.configDetails;
    CS.assign(oConfigDetails.referencedAttributes, oReferencedData);
    let oDefinition = oFoundIPParam.definition;
    let aEntries = oDefinition.entries;
    if(sStatus == "attributeKey"){
      let oFoundKeyEntry = CS.find(aEntries, {key: sKey});
      let sNewKey = CS.isEmpty(aSelectedItems[0]) ? "" : aSelectedItems[0];
      oFoundKeyEntry.key = sNewKey;
      let oReferencedAttributeObject = CS.find(oReferencedData , {id : aSelectedItems[0]});
      oFoundKeyEntry.value = oReferencedAttributeObject.type;

      let oFoundIPParamValueMap = _getInputParameterFromActiveElementByName("ATTRIBUTES_VALUES_MAP");
      let oDefinitionValueMap = oFoundIPParamValueMap.definition;
      let aEntriesValueMap = oDefinitionValueMap.entries;
      let oFoundKeyEntryValueMap = CS.isEmpty(sKey) ? CS.find(aEntriesValueMap, {key: sSelectedTagGroup}) : CS.find(aEntriesValueMap, {key: sKey});
      oFoundKeyEntryValueMap.key = sNewKey;
      if(!CS.isEmpty(sKey)){
        oFoundKeyEntryValueMap.value = "";
      }
    }
    oActiveModelingInstance.updateProperties(ProcessConfigViewProps.getActiveBPMNElement(), {});
  };

  let _handleSearchFilterEditButtonClicked = function(sName) {
    let ProcessFilterProps = SettingScreenProps.processFilterProps;
    ProcessFilterProps.setAdvancedSearchPanelShowStatus(true);
    // _triggerChange();
  };

  let _handleBPMNPropertiesTagMSSChanged = function (sKey, aSelectedItems, oReferencedData) {
    let oActiveModelingInstance = ProcessConfigViewProps.getActiveBPMNModelingInstance();
    let oFoundIPParam = _getInputParameterFromActiveElementByName("tagGroup");
    let oActiveProcess = ProcessConfigViewProps.getActiveProcess();
    if(!oActiveProcess.configDetails){
      oActiveProcess.configDetails = {
        referencedTags: {}
      };
    }
    let oConfigDetails = oActiveProcess.configDetails;
    CS.assign(oConfigDetails.referencedTags, oReferencedData);
    let oDefinition = oFoundIPParam.definition;
    let aEntries = oDefinition.entries;
    let oFoundEntry = CS.find(aEntries, {key: sKey});
    let sNewValue = CS.isEmpty(aSelectedItems[0]) ? "" : aSelectedItems[0];
    oFoundEntry.value = sNewValue;
    (sKey == "tagId" && CS.isEmpty(sNewValue)) && (aEntries[1].value = "");
    oActiveModelingInstance.updateProperties(ProcessConfigViewProps.getActiveBPMNElement(), {});
  };

  var _handleProcessComponentClassInfoTaxonomyValueChanged = function (sName, iIndex, sValue) {
    let oFoundIPParam = _getInputParameterFromActiveElementByName(sName);
    let oActiveModelingInstance = ProcessConfigViewProps.getActiveBPMNModelingInstance();

    let oDefinition = oFoundIPParam.definition;
    oDefinition.items[iIndex].value = sValue;
    oActiveModelingInstance.updateProperties(ProcessConfigViewProps.getActiveBPMNElement(), {});
  };

  let _handleBPMNElementsRemoveRowButtonClicked = function (sName, iIndex) {
    let oFoundIPParam = _getInputParameterFromActiveElementByName(sName);
    let oActiveModelingInstance = ProcessConfigViewProps.getActiveBPMNModelingInstance();

    let oDefinition = oFoundIPParam.definition;
    oDefinition.items.splice(iIndex, 1);
    oActiveModelingInstance.updateProperties(ProcessConfigViewProps.getActiveBPMNElement(), {});
  };

  let _setUpProcessConfigGridView = function () {
    let oWorkflowConfigGridViewSkeleton = new ProcessGridViewSkeleton();
    GridViewStore.createGridViewPropsByContext(GridViewContexts.PROCESS, {skeleton: oWorkflowConfigGridViewSkeleton});
    _fetchProcessListForGridView();
  };

  let _fetchProcessListForGridView = function(){
    let oPostData = GridViewStore.getPostDataToFetchList(GridViewContexts.PROCESS);
    oPostData.filterData = _getFilterData();
    SettingUtils.csPostRequest(ProcessRequestMapping.GetProcessListForGrid,{},oPostData,successFetchProcessListForGridView, failureFetchProcessListForGridView);

  };

  let successFetchProcessListForGridView = function (oResponse) {
    let aProcessList = oResponse.success.processEventsList;
    let oConfigDetails = oResponse.success.configDetails;
    ProcessConfigViewProps.setConfigDetailsData(oConfigDetails);
    SettingUtils.getAppData().setProcessList(aProcessList);
    _setProcessValueList(aProcessList);
    GridViewStore.preProcessDataForGridView(GridViewContexts.PROCESS, aProcessList, oResponse.success.count, oConfigDetails);
    ProcessConfigViewProps.setProcessGridViewData(aProcessList);
    _triggerChange();
  };

  let failureFetchProcessListForGridView = function (oResponse) {
      SettingUtils.failureCallback(oResponse, 'failureFetchProcessListForGridView', getTranslation());
  };

  /** Function to Prepare Filter Data **/
  let _getFilterData = function () {
    let aAppliedFilterData = ProcessConfigViewProps.getAppliedFilterData();
    let aSearchFilterData = ProcessConfigViewProps.getSearchFilterData();
    let aCustomWorkflowType = [MockDataForWorkflowTypesDictionary.SCHEDULED_WORKFLOW, MockDataForWorkflowTypesDictionary.JMS_WORKFLOW, MockDataForWorkflowTypesDictionary.TASKS_WORKFLOW, MockDataForWorkflowTypesDictionary.STANDARD_WORKFLOW];
    let oFilterData;

    if (CS.isEmpty(aAppliedFilterData) && CS.isEmpty(aSearchFilterData)) {
      oFilterData = {
        workflowType: aCustomWorkflowType
      };
    } else {
      oFilterData = {
        eventType: null,
        workflowType: null,
        triggeringType: null,
        timerDefinitionType: null,
        activation: null,
        physicalCatalogIds: null,
        timerStartExpression: null
      };

      CS.forEach(aSearchFilterData, function (oSearchData) {
        oFilterData[oSearchData.id] = oSearchData.value;
      });

      CS.forEach(aAppliedFilterData, function (oAppliedFilter) {
        if (oAppliedFilter.id == "activation") {
          oFilterData[oAppliedFilter.id] = oAppliedFilter.children.map(oChild => {
            if (oChild.id === "activate") {
              return true;
            } else {
              return false;
            }
          });
        } else {
          oFilterData[oAppliedFilter.id] = oAppliedFilter.children.map(oChild => oChild.id);
        }
      });
      oFilterData.workflowType = CS.isEmpty(oFilterData.workflowType) ? aCustomWorkflowType : oFilterData.workflowType;
    }

    return oFilterData;
  };

  let _fetchTaxonomyById = function (sContext, sTaxonomyId,sOtherTaxonomyId) {
    let oParameters = {};
    if (sContext == "majorTaxonomy") {
      if (sTaxonomyId == "addItemHandlerforMultiTaxonomy") {
        sTaxonomyId = SettingUtils.getTreeRootId();
      } else {
        sTaxonomyId = sTaxonomyId;
      }
    }
    oParameters.id = sTaxonomyId;

    let oTaxonomyPaginationData = {};
    if(sOtherTaxonomyId){
      oTaxonomyPaginationData = ProcessConfigViewProps.getBPMNTaxonomyPaginationData();
      oTaxonomyPaginationData.taxonomyTypes = ["majorTaxonomy", "minorTaxonomy"];
    }else{
      oTaxonomyPaginationData = ProcessConfigViewProps.getTaxonomyPaginationData();
      oTaxonomyPaginationData.taxonomyId = sTaxonomyId;
    }

    let oPostData = CS.assign({taxonomyId: sTaxonomyId}, oTaxonomyPaginationData);

    SettingUtils.csPostRequest(oSettingScreenRequestMapping.GetAllTaxonomy,{},oPostData,successFetchTaxonomy.bind(this,sOtherTaxonomyId), failureFetchClassListCallback);

  };

  let successFetchTaxonomy = function (sOtherTaxonomyId,oResponse) {
    let aTaxonomyListFromServer = oResponse.success.list;
    let oConfigDetails = oResponse.success.configDetails;
    let oAllowedTaxonomyHierarchyListData;
    if(sOtherTaxonomyId){
      ProcessConfigViewProps.setParentTaxonomyListProcessConfig(aTaxonomyListFromServer);
      let aTaxonomyList = ProcessConfigViewProps.getAllowedTaxonomiesInProcessConfig();
      ProcessConfigViewProps.setAllowedTaxonomiesInProcessConfig(CS.concat(aTaxonomyList, aTaxonomyListFromServer));
      oAllowedTaxonomyHierarchyListData = CommonUtils.prepareTaxonomyHierarchyData(ProcessConfigViewProps.getAllowedTaxonomiesInProcessConfig(), oConfigDetails);
      ProcessConfigViewProps.setAllowedTaxonomyHierarchyList(oAllowedTaxonomyHierarchyListData);
    }else{
      ProcessConfigViewProps.setParentTaxonomyList(aTaxonomyListFromServer);
      let aTaxonomyList = ProcessConfigViewProps.getAllowedTaxonomies();
      ProcessConfigViewProps.setAllowedTaxonomies(CS.concat(aTaxonomyList, aTaxonomyListFromServer));
      oAllowedTaxonomyHierarchyListData = CommonUtils.prepareTaxonomyHierarchyData(ProcessConfigViewProps.getAllowedTaxonomies(), oConfigDetails);
      ProcessConfigViewProps.setAllowedTaxonomyHierarchyList(oAllowedTaxonomyHierarchyListData);
    }
    _triggerChange();
  };

  let failureFetchClassListCallback = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchOrganisationConfigCallBack", getTranslation);
  };

  let _addSelectedTaxonomyToXML = function (oModel,sParentTaxonomyId,sViewContext) {
    _makeActiveProcessDirty();
    let oActiveModelingInstance = ProcessConfigViewProps.getActiveBPMNModelingInstance();
    let oFoundIPParam = _getInputParameterFromActiveElementByName("taxonomiesList");
    let oDefinition = oFoundIPParam.definition;
    !oDefinition.items && (oDefinition.items = []);
    let aSelectedTaxonomies = oDefinition.items;
    let oBPMNFactory = ProcessConfigViewProps.getActiveBPMNFactoryInstance();
    let aParentTaxonomyList = ProcessConfigViewProps.getParentTaxonomyListProcessConfig();
    let bIsRootTaxonomySelected = false;
    CS.forEach(aParentTaxonomyList, function (oParentTaxonomy) {
      if (oParentTaxonomy.id == oModel.id && (oParentTaxonomy.type == "majorTaxonomy" || oParentTaxonomy.type == "minorTaxonomy")) {
        bIsRootTaxonomySelected = true;
      }
    });
    let oReferencedTaxonomiesForComponents = ProcessConfigViewProps.getReferencedTaxonomiesForComponent();
    if (CS.isEmpty(oReferencedTaxonomiesForComponents) || bIsRootTaxonomySelected) {
      let temp = {};
      temp.id = oModel.id;
      temp.parent = {};
      temp.parent.id = "-1";
      temp.parent.label = null;
      temp.parent.parent = null;
      temp.label = oModel.label;
      oReferencedTaxonomiesForComponents[oModel.id] = temp;
      ProcessConfigViewProps.setReferencedTaxonomiesForComponent(oReferencedTaxonomiesForComponents);
    } else {
      let temp = {};
      temp.id = oModel.id;
      temp.label = oModel.label;
      temp.parent = oReferencedTaxonomiesForComponents[sParentTaxonomyId];
      oReferencedTaxonomiesForComponents[oModel.id] = temp;
      delete oReferencedTaxonomiesForComponents[sParentTaxonomyId];
      CS.remove(aSelectedTaxonomies, {value: temp.parent.id});
      ProcessConfigViewProps.setReferencedTaxonomiesForComponent(oReferencedTaxonomiesForComponents);
    }
    aSelectedTaxonomies.push(ElementHelper.createElement("camunda:Value", {value: oModel.id}, oDefinition, oBPMNFactory));
    oActiveModelingInstance.updateProperties(ProcessConfigViewProps.getActiveBPMNElement(), {});
  };

  let _handleTaxonomyAdded = function (oModel, sTaxonomyId, sParentTaxonomyId, sViewContext) {
    SettingUtils.getTaxonomyHierarchyForSelectedTaxonomy(sTaxonomyId, sParentTaxonomyId, {fSuccessHandler: _successGetTaxonomies});
    if (sViewContext == "processComponentTaxonomy") {
        _addSelectedTaxonomyToXML(oModel,sParentTaxonomyId,sViewContext);
    }else {
      let oActiveProcess = _makeActiveProcessDirty();
      let oReferencedTaxonomies = oActiveProcess.configDetails.referencedTaxonomies;
      let aSelectedTaxonomies = oActiveProcess.taxonomyIds;
      let bIsParentTaxonomyPresent = CS.includes(aSelectedTaxonomies,sParentTaxonomyId);
      aSelectedTaxonomies.push(oModel.id);
      if (!bIsParentTaxonomyPresent) {
        let temp = {};
        temp.id = oModel.id;
        temp.parent = {};
        temp.parent.id = "-1";
        temp.parent.label = null;
        temp.parent.parent = null;
        temp.label = oModel.label;
        temp.icon = oModel.icon;
        oReferencedTaxonomies[oModel.id] = temp;
        ProcessConfigViewProps.setReferencedTaxonomies(oReferencedTaxonomies);
      } else {
        let temp = {};
        temp.id = oModel.id;
        temp.label = oModel.label;
        temp.parent = oReferencedTaxonomies[sParentTaxonomyId];
        temp.icon = oModel.icon;
        oReferencedTaxonomies[oModel.id] = temp;
        delete oReferencedTaxonomies[sParentTaxonomyId];
        let iParentIndex = aSelectedTaxonomies.indexOf(temp.parent.id);
        aSelectedTaxonomies.splice(iParentIndex, 1);
        ProcessConfigViewProps.setReferencedTaxonomies(oReferencedTaxonomies);
      }
    }
  };

  let _successGetTaxonomies = function (sTaxonomyId, sParentTaxonomyId, oResponse) {
    oResponse = oResponse.success;
    let oReferencedTaxonomy = oResponse.referencedTaxonomies;

    let oActiveProcess = _makeActiveProcessDirty();
    oActiveProcess.configDetails = oActiveProcess.configDetails || {referencedTaxonomies: {}};
    let oReferencedTaxonomies = oActiveProcess.configDetails.referencedTaxonomies;

    oReferencedTaxonomies[sTaxonomyId] = oReferencedTaxonomy[sTaxonomyId];
    _triggerChange();
  };

  let _handleLazyMSSValueRemoved = function (sOldContextKey,sId) {
    let sKey = sOldContextKey[1];
    let oActiveProcess = _makeActiveProcessDirty();
    switch (sKey) {
      case "klassTypes" :
        let aNatureClassType = oActiveProcess['klassIds'];
        let iIndex = CS.indexOf(aNatureClassType, sId);
        aNatureClassType.splice(iIndex, 1);
        break;

      case "attributes" :
        let aAttributes = oActiveProcess['attributeIds'];
        let iIndexForAttribute = CS.indexOf(aAttributes, sId);
        aAttributes.splice(iIndexForAttribute, 1);
        break;

      case "tags" :
        let aTags = oActiveProcess['tagIds'];
        let iIndexForTag = CS.indexOf(aTags, sId);
        aTags.splice(iIndexForTag, 1);
        break;

      case "actionSubType" :
        let aActionSubType = oActiveProcess['actionSubType'];
        let iIndexForActionSubType = CS.indexOf(aActionSubType, sId);
        aActionSubType.splice(iIndexForActionSubType, 1);
        break;

      case "nonNatureklassTypes" :
        let aNonNatureKlassIds = oActiveProcess['nonNatureKlassIds'];
        let iIndexNonNatureKlassIds = CS.indexOf(aNonNatureKlassIds, sId);
        aNonNatureKlassIds.splice(iIndexNonNatureKlassIds, 1);
        break;

      case "frequency" :
        let oFrequency = oActiveProcess['frequency'];
        if(CS.isEmpty(oFrequency)){
          oFrequency = ProcessConfigViewProps.getFrequencyData();
        }
        let sFrequency = sOldContextKey[2];
        let sFrequencyTime = sOldContextKey[3];
        let aFrequency = oFrequency[sFrequency][sFrequencyTime];
        let iIndexFrequencyIds = CS.indexOf(aFrequency, sId);
        aFrequency.splice(iIndexFrequencyIds, 1);
        oFrequency[sFrequency][sFrequencyTime] = aFrequency;
        break;

      case "physicalCatalogIds" :
        let aPhysicalCatalogIds = oActiveProcess['physicalCatalogIds'];
        let iIndexPhysicalCatalogIds = CS.indexOf(aPhysicalCatalogIds, sId);
        aPhysicalCatalogIds.splice(iIndexPhysicalCatalogIds, 1);
        break;

      case "organizations" :
        let aOrganizations = oActiveProcess['organizationsIds'];
        let iIndexForaOrganization = CS.indexOf(aOrganizations, sId);
        aOrganizations.splice(iIndexForaOrganization, 1);
        break;

      case "usecases" :
        let aUsecases = oActiveProcess['usecases'];
        let iIndexForUsecases = CS.indexOf(aUsecases, sId);
        aUsecases.splice(iIndexForUsecases, 1);
        break;
    }
    _triggerChange();

  };

  let _deleteSelectedTaxonomyToXML = function(oTaxonomy, sParentTaxonomyId, sViewContext){
    _makeActiveProcessDirty();
    let oActiveModelingInstance = ProcessConfigViewProps.getActiveBPMNModelingInstance();
    let oFoundIPParam = _getInputParameterFromActiveElementByName("taxonomiesList");
    let oDefinition = oFoundIPParam.definition;
    !oDefinition.items && (oDefinition.items = []);
    let aSelectedTaxonomies = oDefinition.items;
    let oBPMNFactory = ProcessConfigViewProps.getActiveBPMNFactoryInstance();
    let oReferencedTaxonomiesForComponents = ProcessConfigViewProps.getReferencedTaxonomiesForComponent();
    CS.remove(aSelectedTaxonomies,{value:sParentTaxonomyId});
    if (oTaxonomy.parent.id != "-1") {
      let aSelectedTaxonomiesList = CS.map(aSelectedTaxonomies,'value');
      if (!CS.includes(aSelectedTaxonomiesList, oTaxonomy.parent.id)) {
        aSelectedTaxonomies.push(ElementHelper.createElement("camunda:Value", {value: oTaxonomy.parent.id}, oDefinition, oBPMNFactory));
        oReferencedTaxonomiesForComponents[oTaxonomy.parent.id] = oTaxonomy.parent;
      }
    }
    oActiveModelingInstance.updateProperties(ProcessConfigViewProps.getActiveBPMNElement(), {});
  };

  let _handleMultiSelectSmallTaxonomyViewCrossIconClicked = function (oTaxonomy, sParentTaxonomyId, sViewContext) {

    if (sViewContext == "processComponentTaxonomy") {
      _deleteSelectedTaxonomyToXML(oTaxonomy, sParentTaxonomyId, sViewContext);
    } else {
      let oActiveProcess = _makeActiveProcessDirty();
      let oReferencedTaxonomies = oActiveProcess.configDetails.referencedTaxonomies;
      let aSelectedTaxonomies = oActiveProcess.taxonomyIds;
      let iActiveTaxonomyIndex = aSelectedTaxonomies.indexOf(sParentTaxonomyId);
      aSelectedTaxonomies.splice(iActiveTaxonomyIndex, 1);
      let sParentId = oTaxonomy.parent.id;
      if (sParentId !== "-1") {
        if (!CS.includes(aSelectedTaxonomies, sParentId)) {
          aSelectedTaxonomies.push(sParentId);
          oReferencedTaxonomies[sParentId] = oTaxonomy.parent;
        }
      }
      delete oReferencedTaxonomies[sParentTaxonomyId];
    }
    _triggerChange();
  };

  let _discardProcessGridViewChanges = function (oCallbackData) {
    var aProcessGridData = ProcessConfigViewProps.getProcessGridViewData();
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.PROCESS);
    var aGridViewData = oGridViewPropsByContext.getGridViewData();
    var bShowDiscardMessage = false;
    ProcessConfigViewProps.setGridWFValidationErrorList([]);

    CS.forEach(aGridViewData, function (oOldProcess, iIndex) {
      if (oOldProcess.isDirty) {
        var oProcess = CS.find(aProcessGridData, {id: oOldProcess.id});
        aGridViewData[iIndex] = GridViewStore.getProcessedGridViewData(GridViewContexts.PROCESS, [oProcess])[0];
        bShowDiscardMessage = true;
      }
    });

    bShowDiscardMessage && alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
    oGridViewPropsByContext.setIsGridDataDirty(false);
    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
    _triggerChange();
  };

  let generateADMForGrid = function (oNewProcess , oOldProcess) {
    let oDataToSave = {
      id : oNewProcess.id
    };

    if (oOldProcess.workflowType != MockDataForWorkflowTypesDictionary.SCHEDULED_WORKFLOW) {
      if (CS.isNotEmpty(oNewProcess.properties.natureType)) {
        oDataToSave.addedKlassIds = CS.difference(oNewProcess.properties.natureType.selectedItems, oOldProcess.klassIds);
        oDataToSave.deletedKlassIds = CS.difference(oOldProcess.klassIds, oNewProcess.properties.natureType.selectedItems);
      }
      if (CS.isNotEmpty(oNewProcess.properties.attributes)) {
        oDataToSave.addedAttributeIds = CS.difference(oNewProcess.properties.attributes.selectedItems, oOldProcess.attributeIds);
        oDataToSave.deletedAttributeIds = CS.difference(oOldProcess.attributeIds, oNewProcess.properties.attributes.selectedItems);
      }
      if (CS.isNotEmpty(oNewProcess.properties.tags)) {
        oDataToSave.addedTagIds = CS.difference(oNewProcess.properties.tags.selectedItems, oOldProcess.tagIds);
        oDataToSave.deletedTagIds = CS.difference(oOldProcess.tagIds, oNewProcess.properties.tags.selectedItems);
      }
    }

    if (CS.isNotEmpty(oNewProcess.properties.organizations)) {
      oDataToSave.addedOrganizations = CS.difference(oNewProcess.properties.organizations.selectedItems, oOldProcess.organizationsIds);
      oDataToSave.deletedOrganizations = CS.difference(oOldProcess.organizationsIds, oNewProcess.properties.organizations.selectedItems);
    }

    return oDataToSave;
  };

  /** To get the flag when XML is modified **/
  let _getIsXMLModified = function (oMappedGridData, oProcessedProcess, oProcessToSave) {
    if ((oMappedGridData[oProcessedProcess.id].isExecutable !== oProcessToSave.isExecutable)) {
      return true;
    } else {
      return false;
    }
  };

  let _processListBulkSave = function (oCallbackData) {
    let aProcessToSave = [];
    let bFlagToCheckValidation = false;
    let oGridViewProps = GridViewStore.getGridViewPropsByContext(GridViewContexts.PROCESS);
    let aGridData = oGridViewProps.getGridViewData();
    let aProcessGridData = ProcessConfigViewProps.getProcessGridViewData();
    let oMappedGridData = CS.keyBy(aProcessGridData,'id');
    let bSafeToSave = GridViewStore.processGridDataToSave(aProcessToSave, GridViewContexts.PROCESS, aProcessGridData);

    CS.forEach(aProcessToSave, (oProcessToSave) => {
      let oProcessedProcess = CS.find(aGridData, {id: oProcessToSave.id});
      let oOldProcessData = oMappedGridData[oProcessToSave.id];
      let oADMProcessToSave = generateADMForGrid(oProcessedProcess, oOldProcessData);
      CS.assign(oProcessToSave, oADMProcessToSave);


      oProcessToSave.isXMLModified = _getIsXMLModified(oMappedGridData, oProcessedProcess, oProcessToSave);
    });

     if (bSafeToSave) {
      let oPostRequest = [];
      let oData = {};
      oData.codes = [];
      oData.entityType = ConfigEntityTypeDictionary.ENTITY_TYPE_PROCESS_EVENT;
      oData.names = [];
      let iCount = 0;
      let bisJmsAndScheduleInvalid = false;
      CS.forEach(aProcessToSave, function (oProcess) {
        let oWorkflowGridData = CS.find(aProcessGridData, {id: oProcess.id});
        if (oWorkflowGridData.label !== oProcess.label) {
          oData.names.push(oProcess.label);
        }
        if (oProcess.workflowType === MockDataForWorkflowTypesDictionary.JMS_WORKFLOW) {
          if ((CS.isEmpty(oProcess.ip) || CS.isEmpty(oProcess.port) || CS.isEmpty(oProcess.queue))) {
            iCount++;
          }
        }
        if (_checkOrganizationEmptyForJMSAndSchedule(oProcess)) {
          bisJmsAndScheduleInvalid = true;
        }
      });
      oPostRequest.push(oData);
      let bIsJMSPropValid = (iCount > 0) ? true : false;
      oCallbackData.serverCallback = {
        requestURL: ProcessRequestMapping.BulkSave,
        processToSave: aProcessToSave,
        isBulkSave: true,
        isJMSProp : bIsJMSPropValid
      }
      let aUniqueNameList = CS.uniq(oData.names);
      if (aUniqueNameList.length !== oData.names.length) {
        alertify.error(getTranslation().WORKFLOW_NAME_ALREADY_EXISTS);
      } else if (bisJmsAndScheduleInvalid) {
        alertify.error(getTranslation().ORGANISATION_IS_MANDATORY_FOR_JMS_AND_SCHEDULED_WORKFLOW);
      } else {
        SettingUtils.csPostRequest(ProcessRequestMapping.BulkCodeCheck, {}, oPostRequest, successBulkCodeCheckList.bind(this, oCallbackData), failureBulkCodeCheckList);
      }
    }
  };

  let successBulkSaveProcessList = function (oCallBackData, oResponse) {

    //let oScreenProps = SettingScreenProps.screen;
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.PROCESS);
    let aGridData = oGridViewPropsByContext.getGridViewData();
    if (oResponse.success && CS.isNotEmpty(oResponse.success)) {
      let aProcessList = oResponse.success.processEventsList;
      let aProcessGridData = ProcessConfigViewProps.getProcessGridViewData();
      let oMasterProcessList = SettingUtils.getAppData().getProcessList();
      CS.forEach(aGridData, function (oData) {
        oData.isDirty = false;
      });

    CS.forEach(aProcessList, function (oProcessData) {
      let iIndex = CS.findIndex(aProcessGridData, { 'id': oProcessData.id});
      if(iIndex != -1){
        aProcessGridData[iIndex] = oProcessData;
      } else if(oCallBackData.isSaveAsDialogActive && (iIndex === -1)){
        aProcessGridData.unshift(oProcessData);
        oMasterProcessList[oProcessData.id] = oProcessData;
        let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.PROCESS, [oProcessData], oResponse.configDetails)[0];
        aGridData.unshift(aProcessedGridViewData);
      }
    });
    if(oCallBackData && oCallBackData.isSaveAsDialogActive){
      let oGridSkeleton = oGridViewPropsByContext.getGridViewSkeleton();
      oGridSkeleton.selectedContentIds = [];
    }
      if (CS.isEmpty(oResponse.failure.exceptionDetails)) {
        if (oCallBackData && oCallBackData.isSaveAsCall) {
          let oGridViewTotalCount = oGridViewPropsByContext.getGridViewTotalItems() + oResponse.success.count;
          let oPostData = GridViewStore.getPostDataToFetchList(GridViewContexts.PROCESS);
          if(oGridViewTotalCount > oPostData.size){
            _fetchProcessListForGridView();
          }else{
            oGridViewPropsByContext.setGridViewTotalItems(oGridViewTotalCount);
          }
          alertify.success(getTranslation().PROCESS_CLONED_SUCCESSFULLY);
        } else {
          alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY , { entity :getTranslation().WORKFLOW } ) );
        }
        oGridViewPropsByContext.setIsGridDataDirty(false);
      }
    }
    if (CS.isNotEmpty(oResponse.failure.exceptionDetails) && CS.isNotEmpty(oResponse.success)) {
      let sWorkflowType = oResponse.success.processEventsList[0].workflowType;
      let aExceptionDetails = oResponse.failure.exceptionDetails;
      CS.forEach(aExceptionDetails, function (oExceptionDetail) {
        let oFoundData = CS.find(aGridData, {id: oExceptionDetail.itemId});
        oFoundData.isDirty = true;
      });
      if (sWorkflowType === MockDataForWorkflowTypesDictionary.SCHEDULED_WORKFLOW) {
        alertify.warning(getTranslation().BULK_SAVE_FAILED_PARTIALLY);
      }
      else {
        failureBulkSaveProcessList(oResponse);
      }

    }
    else if (CS.isNotEmpty(oResponse.failure.exceptionDetails)) {
      failureBulkSaveProcessList(oResponse);
    }

    oCallBackData && oCallBackData.functionToExecute && oCallBackData.functionToExecute();
    _triggerChange();
  };

  let failureSaveAs = function (oResponse) {
    alertify.error(getTranslation().ERROR_DURING_COPY_OPERATION);
  };

  let failureBulkSaveProcessList = function (oResponse) {
    let aGridWFValidationErrorList = [];
    let aExceptionDetails = oResponse.failure.exceptionDetails;
    CS.forEach(aExceptionDetails, function (oProcess) {
      if(oProcess.key === "InvalidWorkflowException"){
        aGridWFValidationErrorList.push(oProcess.itemId);
      }
    });
    ProcessConfigViewProps.setGridWFValidationErrorList(aGridWFValidationErrorList);
    oResponse.failure.exceptionDetails = CS.remove(aExceptionDetails ,function(object){object.key == "InvalidWorkflowException"}); // eslint-disable-line
    if(oResponse.failure.exceptionDetails.length == 0 && CS.isNotEmpty(aGridWFValidationErrorList)){
      alertify.error(getTranslation().VALIDATION_FAILED_FOR_SOME_WORKFLOWS);
    }
    else{
      SettingUtils.failureCallback(oResponse,"failureBulkSaveContextList",getTranslation());
    }
    _triggerChange();
  };

  let _handleGridPropertyValueChangeDependencies = function(){
    let oGridViewProps = GridViewStore.getGridViewPropsByContext(GridViewContexts.PROCESS);
    let aGridData = oGridViewProps.getGridViewData();
    CS.forEach(aGridData, function (oGridProcess) {
      let oProperties = oGridProcess.properties;
      let oEventType = oProperties.eventType;
      let sEventValue = oEventType.value[0];
      let oTriggeringType = oProperties.triggeringType;
      let sTriggeringValue = CS.isNotEmpty(oTriggeringType) ? oTriggeringType.value[0] : "";

    /*  if(sEventValue === ProcessEventTypeDictionary.BUSINESS_PROCESS_EVENT && sTriggeringValue === "afterSave") {
        processProperties(oProperties,false,false,true,false,false);
      }else if(sEventValue === ProcessEventTypeDictionary.BUSINESS_PROCESS_EVENT && sTriggeringValue === "afterCreate") {
        processProperties(oProperties,false,false,true,true,true);
      }else if(sEventValue === ProcessEventTypeDictionary.BUSINESS_PROCESS_EVENT && sTriggeringValue === "transfer") {
        processProperties(oProperties,false,false,true,true,true);
      }else if(sEventValue === ProcessEventTypeDictionary.BUSINESS_PROCESS_EVENT && sTriggeringValue === "afterImport") {
        processProperties(oProperties,false,false,true,true,true);
      }else if(sEventValue === ProcessEventTypeDictionary.BUSINESS_PROCESS_EVENT && sTriggeringValue === "afterSwitchType") {
        processProperties(oProperties,false,true,true,true,true);
      }*/
      /*else if(sEventValue === ProcessEventTypeDictionary.INTEGRATION) {
        processProperties(oProperties,false,true,true,true,true);
      }else {
        processProperties(oProperties,true,true,true,true,true);
      }*/

      // if(CS.isNotEmpty(oTriggeringType)){
      //   oProperties.triggeringType.items = updatePropertyTriggeringType(sEventValue);
      // }

    });
    _triggerChange();
  };

  let _handleBPMNCustomTabsUpdate = function () {
    let oActiveModelingInstance = ProcessConfigViewProps.getActiveBPMNModelingInstance();
    oActiveModelingInstance._eventBus && oActiveModelingInstance._eventBus.fire('selection.changed', {
      oldSelection: [],
      newSelection: [ProcessConfigViewProps.getActiveBPMNElement()]
    });
  };

  let _handleTaxonomySearchClicked = function(sContext, sTaxonomyId, sSearchText){
    ProcessConfigViewProps.setAllowedTaxonomies([]);
    let oTaxonomyPaginationData = ProcessConfigViewProps.getTaxonomyPaginationData();
    oTaxonomyPaginationData.from = 0;
    oTaxonomyPaginationData.searchText = sSearchText;
    _fetchTaxonomyById(sContext, sTaxonomyId);
  }

  let _handleTaxonomyLoadMoreClicked = function(sContext, sTaxonomyId){
    let oTaxonomyPaginationData = ProcessConfigViewProps.getTaxonomyPaginationData();
    let aTaxonomyList = ProcessConfigViewProps.getAllowedTaxonomies();
    oTaxonomyPaginationData.from = aTaxonomyList.length;
    _fetchTaxonomyById(sContext, sTaxonomyId);
  };

  let _handleBPMNTaxonomySearchClicked = function(sContext, sTaxonomyId, sSearchText){
    ProcessConfigViewProps.setAllowedTaxonomiesInProcessConfig([]);
    let oSmallTaxonomyPaginationData = ProcessConfigViewProps.getBPMNTaxonomyPaginationData();
    oSmallTaxonomyPaginationData.from = 0;
    oSmallTaxonomyPaginationData.searchText = sSearchText;
    _fetchTaxonomyById(sContext, sTaxonomyId, "processComponentTaxonomy");
  }

  let _handleBPMNTaxonomyLoadMoreClicked = function(sContext, sTaxonomyId){
    let oSmallTaxonomyPaginationData = ProcessConfigViewProps.getBPMNTaxonomyPaginationData();
    let aTaxonomyList = ProcessConfigViewProps.getAllowedTaxonomiesInProcessConfig();
    oSmallTaxonomyPaginationData.from = aTaxonomyList.length;
    _fetchTaxonomyById(sContext, sTaxonomyId, "processComponentTaxonomy");
  };

  /** Function to handle Filter clear **/
  let _handleAppliedFilterClearClicked = function() {
    let oProcessConfigViewProps = ProcessConfigViewProps;
    oProcessConfigViewProps.setAppliedFilterData([]);
    oProcessConfigViewProps.setAppliedFilterClonedData([]);
    oProcessConfigViewProps.setSearchFilterData([]);
    oProcessConfigViewProps.setIsFilterDirty(false);
    _fetchProcessListForGridView();
/*    _handleAdvancedSearchPanelClearClicked();
    _handleBPMNPropertiesAdvancedFilterChangedCustom();
    _makeActiveProcessDirty();*/
  };

  let _handleMSSValueClicked = function(){
    let oPostData = {
        dataLanguage: "",
        isGetDataLanguages: true,
        isGetUILanguages: false,
        uiLanguage: ""
    };

    SettingUtils.csPostRequest(TranslationsRequestMapping.GetDataLanguage, {}, oPostData, successLanguageInfo, failureLanguageInfo);

  };

  let successLanguageInfo = function(oResponse){
    let aLanguageInfo = oResponse.success;
    let aDataLanguages = aLanguageInfo.dataLanguages;
    let oActiveModelingInstance = ProcessConfigViewProps.getActiveBPMNModelingInstance();
    let oFoundIPParam = _getInputParameterFromActiveElementByName("DATA_LANGUAGE");

    oFoundIPParam.items = aDataLanguages;
    oActiveModelingInstance.updateProperties(ProcessConfigViewProps.getActiveBPMNElement(), {});
  };

  let failureLanguageInfo = function(oResponse){
    SettingUtils.failureCallback(oResponse, 'failureLanguageInfo', getTranslation());
  };

  let _handleBPMNElementsDataLanguageMSSChanged = function(sName, aSelectedItems, oReferencedData){
    let oActiveModelingInstance = ProcessConfigViewProps.getActiveBPMNModelingInstance();
    let oFoundIPParam = _getInputParameterFromActiveElementByName(sName);
    let oLanguage = CS.find(oFoundIPParam.items,{id : aSelectedItems[0]});
    oFoundIPParam.value = CS.isEmpty(aSelectedItems) ? "" : oLanguage.code;
    oActiveModelingInstance.updateProperties(ProcessConfigViewProps.getActiveBPMNElement(), {});
  };

  let _handleBPMNElementsGroupMSSChanged = function(sContext, sAction, aSelectedRoles){
    let oActiveProcess = _makeActiveProcessDirty();
    let oActiveModelingInstance = ProcessConfigViewProps.getActiveBPMNModelingInstance();
    let oFoundIPParam = _getInputParameterFromActiveElementByName(sContext);
    let oParsedValue = JSON.parse(oFoundIPParam.value);
    let oConfigDetails = oActiveProcess.configDetails;
    if(sAction === "add"){
      CS.forEach(aSelectedRoles, function (oElement) {
        if(oElement.groupType === "users"){
          oConfigDetails.referencedUsers[oElement.id]  = oElement;
          if(!oParsedValue.userIds.includes(oElement.id)){
            oParsedValue.userIds.push(oElement.id);
          }
        }else if(oElement.groupType === "roles"){
          oConfigDetails.referencedRoles[oElement.id]  = oElement;
          if(!oParsedValue.roleIds.includes(oElement.id)){
            oParsedValue.roleIds.push(oElement.id);
          }
        }
      });
    } else {
        if(oParsedValue.userIds.includes(aSelectedRoles.id)){
          let iIndex = CS.indexOf(oParsedValue.userIds, aSelectedRoles.id);
          if(iIndex !== -1){
            oParsedValue.userIds.splice(iIndex,1);
          }
        }
        if(oParsedValue.roleIds.includes(aSelectedRoles.id)){
          let iIndex = CS.indexOf(oParsedValue.roleIds, aSelectedRoles.id);
          if(iIndex !== -1){
            oParsedValue.roleIds.splice(iIndex,1);
          }
        }
    }
    oActiveProcess[sContext] = oParsedValue;
    oFoundIPParam.value = JSON.stringify(oParsedValue);
    oActiveModelingInstance.updateProperties(ProcessConfigViewProps.getActiveBPMNElement(), {});
  };

  let _handleBPMNElementsVariableMapChanged = function(sName, iIndex, sType, sValue){
    let oActiveModelingInstance = ProcessConfigViewProps.getActiveBPMNModelingInstance();
    let oFoundIPParam = _getInputParameterFromActiveElementByName(sName);
    let oDefinition = oFoundIPParam.definition;
    let aEntries = oDefinition.entries;
    aEntries[iIndex][sType] = sValue;
    oActiveModelingInstance.updateProperties(ProcessConfigViewProps.getActiveBPMNElement(), {});
  };

  let _handleBPMNElementsAddVariableMap = function(sName){
    let oActiveModelingInstance = ProcessConfigViewProps.getActiveBPMNModelingInstance();
    let oFoundIPParam = _getInputParameterFromActiveElementByName(sName);
    let oDefinition = oFoundIPParam.definition;
    let oBPMNFactory = ProcessConfigViewProps.getActiveBPMNFactoryInstance();
    (!oDefinition.entries) && (oDefinition.entries = []);

    oDefinition.entries.push(ElementHelper.createElement(ProcessConstants.EXTENSION_INPUT_ENTRY, {key: "", value: ""}, oDefinition, oBPMNFactory));
    oActiveModelingInstance.updateProperties(ProcessConfigViewProps.getActiveBPMNElement(), {});
  };

  let _handleBPMNElementsRemoveVariableMap = function(sName, iIndex){
    let oActiveModelingInstance = ProcessConfigViewProps.getActiveBPMNModelingInstance();
    let oFoundIPParam = _getInputParameterFromActiveElementByName(sName);
    let oDefinition = oFoundIPParam.definition;
    let aEntries = oDefinition.entries;
    aEntries.splice(iIndex,1);
    oActiveModelingInstance.updateProperties(ProcessConfigViewProps.getActiveBPMNElement(), {});

  };

  let _handleAdvancedSearchPanelClearClicked = function () {
    ProcessFilterProps.setAppliedFilters([]);
    ProcessFilterProps.setAppliedFiltersClone([]);
    ProcessFilterProps.setIsFilterDirty(true);
  };

  var _getAttributeObjectToPushIntoFilterProps = function (sAttributeId, oMasterAttribute) {

    var aMasterAttributeList =  SettingUtils.getAppData().getAttributeList();
    oMasterAttribute = oMasterAttribute || CS.find(aMasterAttributeList, {id: sAttributeId});

    var sDefaultUnit = oMasterAttribute.defaultUnit || "";
    var sVisualType = SettingUtils.getAttributeTypeForVisual(oMasterAttribute.type);

    var sType = "contains";
    var sValue = "";
    var iFrom = "";
    var iTo = "";
    if(sVisualType == "date"){
      sType = "exact";
    }

    if(sVisualType == "measurementMetrics" || sVisualType == "number" || sVisualType == "calculated"){
      sValue = 0;
      iFrom = 0;
      iTo = 0;
    }

    let oAppliedFilterCollapseStatusMap = SettingScreenProps.processFilterProps.getAppliedFilterCollapseStatusMap();
    oAppliedFilterCollapseStatusMap[sAttributeId] = {isCollapsed: false};

    return {
      id: sAttributeId,
      label: oMasterAttribute.label,
      isCollapsed: false,
      type: oMasterAttribute.type,
      advancedSearchFilter: true,
      children: [
        {
          id: UniqueIdentifierGenerator.generateUUID(),
          type: sType,
          baseType: "com.cs.core.runtime.interactor.model.filter.FilterValueMatchModel",
          label: "",
          value: sValue,
          defaultUnit: sDefaultUnit,
          unitToShow: MeasurementMetricBaseTypeDictionary[oMasterAttribute.type],
          from: iFrom,
          to: iTo,
          advancedSearchFilter: true
        }
      ],
    };
  };

  let _getTagObjectToPushIntoFilterProps = function (sTagId, oMasterTag) {

    // var aMasterTagList = ContentUtils.getAppData().getTagList();
    oMasterTag = oMasterTag || ProcessFilterStore.getMasterTagById(sTagId);
    let aLeafNodesFlattened = ProcessFilterStore.getLeafNodeSubbedWithParentInfo(oMasterTag.children, '', 'children');

    let oAppliedFilterCollapseStatusMap = SettingScreenProps.processFilterProps.getAppliedFilterCollapseStatusMap();
    oAppliedFilterCollapseStatusMap[sTagId] = {isCollapsed: false};

    return {
      id: sTagId,
      label: oMasterTag.label,
      isCollapsed: false,
      type: oMasterTag.tagType || oMasterTag.type,
      children: _getTagDummyInstancesForFilters(aLeafNodesFlattened, oMasterTag),
      advancedSearchFilter: true
    }
  };

  let _getTagDummyInstancesForFilters = function(aLeafNodes, oMasterTag){

    let sTagType = oMasterTag.tagType;
    let aRes = [];
    let iMin = 0;
    let iMax = 0;
    let bIsRange = sTagType == TagTypeConstants['RANGE_TAG_TYPE'] || sTagType == TagTypeConstants['CUSTOM_TAG_TYPE'];

    if(bIsRange){
      iMin = ProcessFilterStore.getMinValueFromListByPropertyName(oMasterTag.tagValues, 'relevance');
      iMax = ProcessFilterStore.getMaxValueFromListByPropertyName(oMasterTag.tagValues, 'relevance');
    }

    CS.forEach(aLeafNodes, function(oNode){

      if(bIsRange){

        aRes.push({
          id: oNode.id,
          label: oNode.label,
          type: "range",
          min: iMin,
          max: iMax,
          from: 0,
          to: iMax,
          baseType: "com.cs.core.runtime.interactor.model.filter.FilterValueRangeModel",
          advancedSearchFilter: true
        })
      } else {
        aRes.push({
          id: oNode.id,
          label: oNode.label,
          type: "range",
          from: 0,
          to: 0,
          baseType: "com.cs.core.runtime.interactor.model.filter.FilterValueRangeModel",
          advancedSearchFilter: true
        })
      }
    });

    return aRes;
  };

  let _fetchAttribute = function (sAttributeId, sContext) {
    var oParameters = {};
    oParameters.id = sAttributeId;

    SettingUtils.csGetRequest(ProcessRequestMapping.GetAttribute, oParameters, successFetchAttributeDetailsCallback.bind(this, sContext), failureFetchAttributeDetailsCallback);
  };

  let successFetchAttributeDetailsCallback = function(sContext , oResponse){
    let oAttribute = oResponse.success;
    let oLoadedAttributes = ProcessConfigViewProps.getLoadedAttributes();
    if (oAttribute && oAttribute.id) {
      oLoadedAttributes[oAttribute.id] = oAttribute;
      if (sContext == "advancedSearch") {
        let aAppliedFilters = ProcessFilterStore.getAppliedFiltersClone();
        let oObjToPush = _getAttributeObjectToPushIntoFilterProps(oAttribute.id, oAttribute);
        let oFound = CS.find(aAppliedFilters, {id: oObjToPush.id});
        if (oFound) {
          oFound.children = oFound.children.concat(oObjToPush.children);
          oFound.label = oObjToPush.label;
          oFound.isCollapsed = false;
        } else {
          aAppliedFilters.push(oObjToPush);
        }
      } else if (sContext == "bulkEdit") {
        // let oBulkEditProcessFilterProps = SettingScreenProps.bulkEditProcessFilterProps;
        // let aAppliedOjects = oBulkEditProcessFilterProps.getAppliedDataObjects();
        // let aPropertySequence = oBulkEditProcessFilterProps.getPropertySequence();
        // let oDummyObjectToPush = {
        //   entityId: oAttribute.id,
        //   value: "",
        //   valueAsHtml: "",
        //   type: "attribute"
        // };
        //
        // let oSequenceObject = {
        //   id: oAttribute.id,
        //   type: "attribute",
        // };
        //
        // aPropertySequence.push(oSequenceObject);
        // aAppliedOjects.attributes.push(oDummyObjectToPush);
        //
        // let aAppliedAttributeList = oBulkEditProcessFilterProps.getAppliedAttributeList();
        // aAppliedAttributeList.push(oAttribute);
        // oBulkEditProcessFilterProps.setIsDirty(true);
      }

      _triggerChange();
    }
  };

  let failureFetchAttributeDetailsCallback = function(sContext , oResponse){
    SettingUtils.failureCallback(oResponse, 'failureFetchAttributeDetailsCallback', getTranslation());
  };

  let _fetchTags = function (sTagId, sContext) {
    var oParameters = {};
    oParameters.id = sTagId;
    oParameters.mode = "all";

    SettingUtils.csGetRequest(ProcessRequestMapping.GetTag, oParameters, successFetchTagDetailsCallback.bind(this, sContext), failureFetchTagDetailsCallback);
  };

  let successFetchTagDetailsCallback = function(sContext , oResponse){
    let oTag = oResponse.success;
    let oLoadedTags = ProcessConfigViewProps.getLoadedTags();
    if (oTag && oTag.id) {
      oLoadedTags[oTag.id] = oTag;
      CS.assign(oLoadedTags, CS.keyBy(oTag.children, 'id'));
      if (sContext === "advancedSearch") {
        let aAppliedFilters = ProcessFilterStore.getAppliedFiltersClone();
        let oObjToPush = _getTagObjectToPushIntoFilterProps(oTag.id, oTag);
        aAppliedFilters.push(oObjToPush);
        let oAppliedFilterCollapseStatusMap = SettingScreenProps.processFilterProps.getAppliedFilterCollapseStatusMap();
        oAppliedFilterCollapseStatusMap[oTag.id] = {isCollapsed: false};

      } else if (sContext == "bulkEdit") {
        // var oBulkEditProps = ContentScreenProps.bulkEditProps;
        // var oAppliedObjects = oBulkEditProps.getAppliedDataObjects();
        // var aPropertySequence = oBulkEditProps.getPropertySequence();
        //
        // var oDummyObjectToPush = {
        //   entityId: oTag.id,
        //   type: "tag",
        //   value: []
        // };
        //
        // //add new tagValue instances in the dummy tag. - Back end need 0 value instances also in modified tags.
        // _addNewBulkEditInstanceOfTagValuesInTag(oDummyObjectToPush, oTag);
        //
        // var oSequenceObject = {
        //   id: oTag.id,
        //   type: "tag",
        // };
        //
        // oAppliedObjects.tags.push(oDummyObjectToPush);
        // aPropertySequence.push(oSequenceObject);
        //
        // var aAppliedTagList = oBulkEditProps.getAppliedTagList();
        // aAppliedTagList.push(oTag);
        // oBulkEditProps.setIsDirty(true);
      }
      _triggerChange();
    }
  };

  let failureFetchTagDetailsCallback = function(sContext , oResponse){
    SettingUtils.failureCallback(oResponse, 'failureFetchTagDetailsCallback', getTranslation());
  };

  let handleAdvancedFilterAttributeAdded = function(sAttributeId, sContext){
    _fetchAttribute(sAttributeId, sContext);
  };

  let handleAdvancedFilterTagAdded = function(sTagId, sContext){
    _fetchTags(sTagId, sContext);
  };

  let _handleAdvancedSearchListItemNodeClicked = function (sItemId, sType, sContext) {
    if (sType === ConfigDataEntitiesDictionary.ATTRIBUTES) {
      handleAdvancedFilterAttributeAdded(sItemId, sContext);
    } else if (sType === ConfigDataEntitiesDictionary.TAGS) {
      handleAdvancedFilterTagAdded(sItemId, sContext);
    }
  };

  /** Function to handle Filter remove **/
  let _handleRemoveAppliedFilterClicked  = function(sElementId) {
    let aAppliedFilterData = ProcessConfigViewProps.getAppliedFilterData();
    CS.remove(aAppliedFilterData, {id: sElementId});
    _fetchProcessListForGridView();

/*    _handleFilterElementDeleteClicked(sElementId);
    _handleBPMNPropertiesAdvancedFilterChangedCustom();
    _makeActiveProcessDirty();*/
  };

  let _handleFilterElementDeleteClicked = function (sElementId) {
    let aAppliedFilters = ProcessFilterStore.getAppliedFiltersClone();
    let iFoundIndex = CS.findIndex(aAppliedFilters, {id: sElementId});
    if(iFoundIndex >= 0){
      aAppliedFilters.splice(iFoundIndex, 1);
    }
    _triggerChange();
  };

  let _handleFilterElementExpandClicked = function (sElementId) {
    let oAppliedFilterCollapseStatusMap = SettingScreenProps.processFilterProps.getAppliedFilterCollapseStatusMap();
    let bPrevStatus = oAppliedFilterCollapseStatusMap[sElementId].isCollapsed;
    oAppliedFilterCollapseStatusMap[sElementId].isCollapsed = !bPrevStatus;
    _triggerChange();
  };

  let _handleAddAttributeClicked = function (sAttributeId) {
    let aAppliedFilters = ProcessFilterStore.getAppliedFiltersClone();
    let oAppliedAttribute = CS.find(aAppliedFilters, {id: sAttributeId});

    let sType = "contains";
    let sVisualType = SettingUtils.getAttributeTypeForVisual(oAppliedAttribute.type);
    if (sVisualType == "date") {
      sType = "exact";
    }

    let aValueAsNumberTypes = ["measurementMetrics", "number", "calculated"];
    let sValue = CS.includes(aValueAsNumberTypes, sVisualType) ? "0" : "";

    let oObjToPush = {
      id: UniqueIdentifierGenerator.generateUUID(),
      type: sType,
      baseType: "com.cs.core.runtime.interactor.model.filter.FilterValueMatchModel",
      value: sValue,
      unitToShow: MeasurementMetricBaseTypeDictionary[oAppliedAttribute.type],
      advancedSearchFilter: true
    };

    oAppliedAttribute.children.push(oObjToPush);

    let oCollapseExpandMap = SettingScreenProps.processFilterProps.getAppliedFilterCollapseStatusMap();
    oCollapseExpandMap[sAttributeId].isCollapsed = false;

    _triggerChange();
  };

  let _handleFilterAttributeValueDeleteClicked = function (sAttributeId, sValId) {
    let aAppliedFilters = ProcessFilterStore.getAppliedFiltersClone();
    let oAppliedAttribute = CS.find(aAppliedFilters, {id: sAttributeId});
    CS.remove(oAppliedAttribute.children, {id: sValId});

    _triggerChange();
  };

  let _handleFilterAttributeValueTypeChanged = function (sAttributeId, sValId, sTypeId) {
    let aAppliedFilters = ProcessFilterStore.getAppliedFiltersClone();
    let oAppliedAttribute = CS.find(aAppliedFilters, {id: sAttributeId});

    let sVisualType = SettingUtils.getAttributeTypeForVisual(oAppliedAttribute.type);
    let aNumberTypes = ["number", "measurementMetrics", "calculated"];

    let oValue = CS.find(oAppliedAttribute.children, {id: sValId});
    oValue.type = sTypeId;
    if(sTypeId == "range"){
      oValue.baseType = "com.cs.core.runtime.interactor.model.filter.FilterValueRangeModel";
      oValue.to = (CS.includes(aNumberTypes, sVisualType)) ? 0 : "";
      oValue.from = (CS.includes(aNumberTypes, sVisualType)) ? 0 : "";
    } else {
      oValue.baseType = "com.cs.core.runtime.interactor.model.filter.FilterValueMatchModel"
    }

    if(sTypeId == "empty" || sTypeId == "notempty"){
      oValue.value = (CS.includes(aNumberTypes, sVisualType)) ? 0 : "";
    }
    _triggerChange();
  };

  let _validateNewValue = function (sVal, sAttributeBaseType) {
    let sAttributeVisualType = SettingUtils.getAttributeTypeForVisual(sAttributeBaseType);
    let aValueAsNumberTypes = ["measurementMetrics", "number", "calculated"];
    let bIsValidValue = true;

    if(CS.includes(aValueAsNumberTypes, sAttributeVisualType)){
      bIsValidValue = SettingUtils.testNumber(sVal);
    }
    return bIsValidValue ? sVal : 0;
  };

  let _handleFilterAttributeValueChanged = function (sAttributeId, sValId, sVal) {
    let aAppliedFilters = ProcessFilterStore.getAppliedFiltersClone();
    let oAppliedAttribute = CS.find(aAppliedFilters, {id: sAttributeId});

    let sVisualType = SettingUtils.getAttributeTypeForVisual(oAppliedAttribute.type);
    let aNumberTypes = ["number", "measurementMetrics", "calculated"];
    if(CS.includes(aNumberTypes, sVisualType)){
      sVal = Number(sVal).toString(); // remove trailing zeros.
    }

    let oValue = CS.find(oAppliedAttribute.children, {id: sValId});
    if(oValue.advancedSearchFilter){
      sVal = _validateNewValue(sVal, oAppliedAttribute.type);
      oValue.value = sVal;
    }else {
      oValue.label = sVal;
    }

    _triggerChange();
  };

  let _handleFilterAttributeValueChangedForRange = function (sAttributeId, sValId, sVal, sRange) {
    let aAppliedFilters = ProcessFilterStore.getAppliedFiltersClone();
    let oAppliedAttribute = CS.find(aAppliedFilters, {id: sAttributeId});

    sVal = _validateNewValue(sVal, oAppliedAttribute.type);
    let oValue = CS.find(oAppliedAttribute.children, {id: sValId});
    oValue[sRange] = sVal;

    _triggerChange();
  };

  let _addNewFilterInstanceOfSelectedTagValuesInTag = function (oAppliedFilterTag, oMasterTag, aTagValueRelevanceData) {
    if(!aTagValueRelevanceData) {
      return;
    }

    let aNewFilterInstanceReqData = [];
    CS.forEach(aTagValueRelevanceData, function (oData) {
      let oChild = CS.find(oAppliedFilterTag.children, {id: oData.tagId});
      if(!oChild) {
        let oMasterTagValue = CS.find(oMasterTag.children, {id: oData.tagId});
        aNewFilterInstanceReqData.push({
          id: oData.tagId,
          label: oMasterTagValue.label
        });
      }
    });

    if(aNewFilterInstanceReqData) {
      let aNewFilterInstances = _getTagDummyInstancesForFilters(aNewFilterInstanceReqData, oMasterTag);
      CS.forEach(aNewFilterInstances, function (oFilterInstance) {
        oAppliedFilterTag.children.push(oFilterInstance);
      })
    }
  };

  let _handleFilterTagValueChanged = function (sTagGroupId, aTagValueRelevanceData) {
    let aAppliedFilters = ProcessFilterStore.getAppliedFiltersClone();
    let oLoadedTags = ProcessConfigViewProps.getLoadedTags();
    let oMasterTagGroup = oLoadedTags[sTagGroupId] || SettingUtils.getMasterTagById(sTagGroupId);

    let oTagGroupFromFilter = CS.find(aAppliedFilters, {id: sTagGroupId});
    let aTagValuesFromFilter = oTagGroupFromFilter.children;

    _addNewFilterInstanceOfSelectedTagValuesInTag(oTagGroupFromFilter, oMasterTagGroup, aTagValueRelevanceData);

    CS.forEach(aTagValuesFromFilter, function (oTagValueFromFilter) {
      let sTagValueId = oTagValueFromFilter.id;
      let oMasterTag = CS.find(oMasterTagGroup.children, {id: sTagValueId}) || SettingUtils.getMasterTagById(sTagValueId);
      let oData = CS.find(aTagValueRelevanceData, {tagId: sTagValueId});

      SettingUtils.updateTagValueRelevanceOrRange(oTagValueFromFilter, oData, true);

      oTagValueFromFilter.label = oMasterTag.label;
      oTagValueFromFilter.baseType = "com.cs.core.runtime.interactor.model.filter.FilterValueRangeModel";
      oTagValueFromFilter.type = "range";
    });

    _triggerChange();
  };

  let _handleAdvancedSearchDataSave = function () {
    _makeActiveProcessDirty();

  };

  /** Function to add values to EntityMap **/
  let _handleBPMNElementsEntityMapChanged = function(sName, iIndex, sItemid, sNewVal){
    let oActiveModelingInstance = ProcessConfigViewProps.getActiveBPMNModelingInstance();
    let oFoundIPParam = _getInputParameterFromActiveElementByName(sName);
    let oDefinition = oFoundIPParam.definition;
    let aEntries = oDefinition.entries;
    aEntries[iIndex].value = sNewVal;
    oActiveModelingInstance.updateProperties(ProcessConfigViewProps.getActiveBPMNElement(), {});
  };

  let _getInputOutputParametersSeparation = function (oNewElement, oBPMNFactory, oModelling) {
    let oCustomDefinition = {};
    let oBusinessObject = oNewElement.businessObject;
    let sTaskId = oBusinessObject.id;
    let aExtensionElements = oBusinessObject.extensionElements;
    let oInputOutputParameters = aExtensionElements.values[0];
    let aInputParameters = oInputOutputParameters.inputParameters;
    let aOutputParameters = oInputOutputParameters.outputParameters;
    let oFoundIPParam = CS.find(aInputParameters, {name: "customElementID"});
    if(CS.isNotEmpty(oFoundIPParam)){
      oCustomDefinition.customElementID = oFoundIPParam.value;
    }
    _handleProcessAndSetNewCustomElement(oNewElement, oCustomDefinition, oBPMNFactory, oModelling, sTaskId, oInputOutputParameters);
  };

  /** Function to handle Filter child value toggle **/
  let _handleFilterChildToggled = function (sParentId, sChildId, sContext, oExtraData) {
    let oProcessConfigViewProps = ProcessConfigViewProps;
    let aAppliedFilterData = oProcessConfigViewProps.getAppliedFilterClonedData();
    aAppliedFilterData = CS.isEmpty(aAppliedFilterData) ? CS.cloneDeep(oProcessConfigViewProps.getAppliedFilterData()) : aAppliedFilterData;
    let oFilter = CS.find(aAppliedFilterData, {id: sParentId});

    if (CS.isEmpty(oFilter)) {
      oFilter = {
        id: sParentId,
        children: [{id: sChildId}],
      };
      aAppliedFilterData.push(oFilter);
    } else {
      CS.find(oFilter.children, {id: sChildId}) ? CS.remove(oFilter.children, oChild => sChildId == oChild.id) : oFilter.children.push({id: sChildId});
    }
    oProcessConfigViewProps.setAppliedFilterClonedData(aAppliedFilterData);
    oProcessConfigViewProps.setIsFilterDirty(true);
  };

  /** Function to handle Filter Collapse **/
  let _handleCollapseFilterClicked = function (bExpanded) {
    ProcessConfigViewProps.setIsFilterExpanded(bExpanded);
  };

  /** Function to handle Filter Apply **/
  let _handleApplyFilterButtonClicked = function () {
    let oProcessConfigViewProps = ProcessConfigViewProps;
    if (oProcessConfigViewProps.getIsFilterDirty()) {
      CS.isNotEmpty(oProcessConfigViewProps.getAppliedFilterClonedData()) && oProcessConfigViewProps.setAppliedFilterData(oProcessConfigViewProps.getAppliedFilterClonedData());
      oProcessConfigViewProps.setAppliedFilterClonedData([]);
      oProcessConfigViewProps.setIsFilterDirty(false);
      _fetchProcessListForGridView();
    }
  };

  /** Function to handle Filter Item Popover closed **/
  let _handleFilterItemPopoverClosed = function () {
    ProcessConfigViewProps.setAvailableFilterSearchData({});
  };

  /** Function to handle Filter Item Search **/
  let _handleFilterItemSearchTextChanged = function (sId, sSearchedText) {
    let oAvailableFilterSearchData = ProcessConfigViewProps.getAvailableFilterSearchData();
    oAvailableFilterSearchData[sId] = sSearchedText;
  };

  /** Function to handle Seacrh Filter **/
  let _handleFilterSummarySearchTextChanged = function (sFilterId, sSearchedText) {
    let oProcessConfigViewProps = ProcessConfigViewProps;
    let aSearchFilterData = oProcessConfigViewProps.getSearchFilterData();

    let oFilter = CS.find(aSearchFilterData, {id: sFilterId});

    if (CS.isEmpty(oFilter) && CS.isNotEmpty(sSearchedText)) {
      oProcessConfigViewProps.setIsFilterDirty(true);
      oFilter = {
        id: sFilterId,
        value: sSearchedText
      };
      aSearchFilterData.push(oFilter);
    } else if (oFilter.value !== sSearchedText) {
      oProcessConfigViewProps.setIsFilterDirty(true);
      oFilter.value = sSearchedText;
    }
  };

  /** Clear data after Frequency Tab Switch**/
  let _resetFrequencyTabData = () => {
    let aItems = [MockDataForFrequencyTypesDictionary.DURATION,MockDataForFrequencyTypesDictionary.DATE, MockDataForFrequencyTypesDictionary.DAILY,
      MockDataForFrequencyTypesDictionary.HOURMIN, MockDataForFrequencyTypesDictionary.WEEKLY,MockDataForFrequencyTypesDictionary.MONTHLY,
      MockDataForFrequencyTypesDictionary.YEARLY];
    let oFrequency = ProcessConfigViewProps.getFrequencyData();

    CS.forEach(aItems, function (sKey) {
      if((sKey === MockDataForFrequencyTypesDictionary.DURATION) ||
          (sKey === MockDataForFrequencyTypesDictionary.DAILY) ||
          (sKey === MockDataForFrequencyTypesDictionary.HOURMIN) ){
        oFrequency[sKey].hours = 0;
        oFrequency[sKey].mins = 0;
      }else if(sKey === MockDataForFrequencyTypesDictionary.WEEKLY){
        oFrequency[sKey].daysOfWeeks = [];
        oFrequency[sKey].hours = 0;
        oFrequency[sKey].mins = 0;
      }else if(sKey === MockDataForFrequencyTypesDictionary.MONTHLY){
        oFrequency[sKey].days = 1;
        oFrequency[sKey].months = 1;
      }else if(sKey === MockDataForFrequencyTypesDictionary.YEARLY){
        oFrequency[sKey].days = 1;
        oFrequency[sKey].monthsOfYear = 1;
      }else if(sKey === MockDataForFrequencyTypesDictionary.DATE){
        oFrequency[sKey].date = "";
        oFrequency[sKey].hours = 0;
        oFrequency[sKey].mins = 0;
      }
    })
  };

  let _handleTabLayoutTabChanged = function (sTabId) {
    ProcessConfigViewProps.setSelectedTabId(sTabId);
    _triggerChange();
  };

  let _handleProcessFrequencySummaryDateButtonClicked = function (sDate, sContext) {
    let oActiveProcess = _makeActiveProcessDirty();
    let oFrequencyData = ProcessConfigViewProps.getFrequencyData();
    let oData = oFrequencyData[sContext];
    oData.date = sDate;
    ProcessConfigViewProps.setFrequencyData(oFrequencyData);
    oActiveProcess.frequency = oFrequencyData;
    _triggerChange();
  };

  let _fillFrequencyTabData = function (oActiveProcess) {
    let sActiveFrequencyTab = oActiveProcess.frequencyActiveTabId;
    let sTimerStartExp = oActiveProcess.timerStartExpression;
    let oFrequencyData = ProcessConfigViewProps.getFrequencyData();
    let aCronExpElements = [];

    _resetFrequencyTabData();
    if(CS.isNotEmpty(sTimerStartExp)){
      switch (sActiveFrequencyTab) {
        case MockDataForFrequencyTypesDictionary.DURATION :
          /** PT02H20M  PT12H20M **/
          if (/[H]/.test(sTimerStartExp) && /[M]/.test(sTimerStartExp)) {
            aCronExpElements = sTimerStartExp.split('H');
            /** s[0]= PT02**/
            if (/^PT0/.test(aCronExpElements[0])) {
              oFrequencyData[sActiveFrequencyTab].hours = parseInt(aCronExpElements[0].substr(3));
            } else {
              oFrequencyData[sActiveFrequencyTab].hours = parseInt(aCronExpElements[0].substr(2));
            }
            /** s[1]= 20M**/
            if (/^0/.test(aCronExpElements[1])) {
              oFrequencyData[sActiveFrequencyTab].mins = parseInt(aCronExpElements[1].substr(1, 2));
            } else {
              oFrequencyData[sActiveFrequencyTab].mins = parseInt(aCronExpElements[1].substr(0, 2));
            }

          } else if (/[H]/.test(sTimerStartExp)) {
            /**PT02H        PT12H**/
            if (/^PT0/.test(sTimerStartExp)) {
              oFrequencyData[sActiveFrequencyTab].hours = parseInt(sTimerStartExp.substr(3, 4));
            } else {
              oFrequencyData[sActiveFrequencyTab].hours = parseInt(sTimerStartExp.substr(2, 4));
            }
          } else {
            /**PT02M        PT12M **/
            if (/^PT0/.test(sTimerStartExp)) {
              oFrequencyData[sActiveFrequencyTab].mins = parseInt(sTimerStartExp.substr(3, 4));
            } else {
              oFrequencyData[sActiveFrequencyTab].mins = parseInt(sTimerStartExp.substr(2, 4));
            }
          }

          break;

        case MockDataForFrequencyTypesDictionary.DATE :
          /** eg: 2011-03-11T12:13:14**/
          aCronExpElements = sTimerStartExp.split('T');
          let sFormatedDate = Moment(aCronExpElements[0], "YYYY-MM-DD").format("MM/DD/YYYY");
          oFrequencyData[sActiveFrequencyTab].date = sFormatedDate;
          let aTime = aCronExpElements[1].split(':');
          oFrequencyData[sActiveFrequencyTab].hours = (/^0/.test(aTime[0])) ? parseInt(aTime[0].substr(1)) : parseInt(aTime[0]);
          oFrequencyData[sActiveFrequencyTab].mins = (/^0/.test(aTime[1])) ? parseInt(aTime[1].substr(1)) : parseInt(aTime[1]);
          break;
        case MockDataForFrequencyTypesDictionary.DAILY :
          /** eg: ( '0 15 10 1/1 * ? *' ) every day at 10:15 **/
          aCronExpElements = sTimerStartExp.split(' ');
          oFrequencyData[sActiveFrequencyTab].hours = parseInt(aCronExpElements[2]);
          oFrequencyData[sActiveFrequencyTab].mins = parseInt(aCronExpElements[1]);
          break;
        case MockDataForFrequencyTypesDictionary.HOURMIN :
          /** eg: 0 0/0 0/3 1/1 * ? *  every 3 hours 0 mins**/
          aCronExpElements = sTimerStartExp.split(' ');
          oFrequencyData[sActiveFrequencyTab].hours = parseInt(aCronExpElements[2].substr(2));
          oFrequencyData[sActiveFrequencyTab].mins = parseInt(aCronExpElements[1].substr(2));
          break;
        case MockDataForFrequencyTypesDictionary.WEEKLY :
          /** eg: 0 0 12 ? * MON,SAT *  every mon/sat at 12 **/
          aCronExpElements = sTimerStartExp.split(' ');
          oFrequencyData[sActiveFrequencyTab].daysOfWeeks = aCronExpElements[5].split(',');
          oFrequencyData[sActiveFrequencyTab].hours = parseInt(aCronExpElements[2]);
          oFrequencyData[sActiveFrequencyTab].mins = parseInt(aCronExpElements[1]);

          break;
        case MockDataForFrequencyTypesDictionary.MONTHLY :
          /** eg: 0 0 0 7 1/2 ? *  Day 7th of every 2 month(s)**/
          aCronExpElements = sTimerStartExp.split(' ');
          oFrequencyData[sActiveFrequencyTab].days = parseInt(aCronExpElements[3]);
          oFrequencyData[sActiveFrequencyTab].months = parseInt(aCronExpElements[4].substr(2));
          break;
        case MockDataForFrequencyTypesDictionary.YEARLY :
          /** eg: 0 0 0 8 6 ? * ' every year 8th of june**/
          aCronExpElements = sTimerStartExp.split(' ');
          oFrequencyData[sActiveFrequencyTab].monthsOfYear = parseInt(aCronExpElements[4]);
          oFrequencyData[sActiveFrequencyTab].days = parseInt(aCronExpElements[3]);
          break;
      }
    }
  };

  let successSearchCriteriaData = (sPTaxoId, oResponse) => {
    let aTaxonomyTree = oResponse.success.klassTaxonomyInfo;
    let ProcessFilterProps = SettingScreenProps.processFilterProps;
    if(sPTaxoId){
      ProcessFilterProps.setIsTaxonomySelected(true);
    }
    ProcessFilterProps.setTaxonomyTreeData(aTaxonomyTree);
    ProcessFilterProps.setTaxonomyTree(aTaxonomyTree);
    ProcessFilterProps.setSearchCriteriaDialogShowStatus(true);
    let oNewTreeProps = addExpandAndCollapsePropertyToTaxonomyTree(aTaxonomyTree, null, {});
    ProcessFilterProps.setTaxonomyVisualProps(oNewTreeProps);
    _triggerChange();
  };

  let failureSearchCriteriaData = (oResponse) => {
    SettingUtils.failureCallback(oResponse, 'failureSearchCriteriaData', getTranslation());
  };

  let _handleBPMNSearchCriteriaEditButtonClicked = function (sName, sId) {
    let ProcessFilterProps = SettingScreenProps.processFilterProps;
    let oData = ProcessFilterProps.getSearchCriteriaData();
    let oTaxonomyTreeRequestData = oData.taxonomyTreeRequestData;
    let sTaxoId = CS.isEmpty(sId) ? null : sId;
    let sPTaxoId = CS.isEmpty(sId) ? "" : sId;
    let oPostData = {
      "attributes": [],
      "tags": [],
      "allSearch": "",
      "size": 20,
      "from": 0,
      "sortOptions": [{"sortField": "lastmodifiedattribute", "sortOrder": "desc", "isNumeric": false}],
      "selectedTypes": [],
      "selectedTaxonomyIds": [],
      "moduleId": "pimmodule",
      "clickedTaxonomyId": sTaxoId,
      "parentTaxonomyId": sPTaxoId,
      "isArchivePortal": false
    }
    CS.postRequest(oTaxonomyTreeRequestData.url,{},oPostData,successSearchCriteriaData.bind(this, sPTaxoId),failureSearchCriteriaData);
  };

  let assignToAllNodesBelow = function (oObjToAssign, oNode, oVisualProps, aAllChildNodeIds) {
    let oNodeVisualProp = oVisualProps[oNode.id] || {};
    CS.assign(oNodeVisualProp, oObjToAssign);
    oVisualProps[oNode.id] = oNodeVisualProp;

    CS.forEach(oNode.children, function (oChild) {
      if(aAllChildNodeIds){
        aAllChildNodeIds.push(oChild.id);
      }
      assignToAllNodesBelow(oObjToAssign, oChild, oVisualProps, aAllChildNodeIds)
    });
  };

  let getToggledNodeState = function (iState) {
    if (iState >= 1 ) {
      return 0;
    } else {
      return 2;
    }
  };

  let updateNodeStateByChildInfo = function (oTreeNodeVisualProp, oChildInfo ) {
    if (oChildInfo.isAllChildChecked) {
      oTreeNodeVisualProp.isChecked = 2;
    }
    else if (oChildInfo.isAnyChildChecked) {
      oTreeNodeVisualProp.isChecked = 1;
    }
    else {
      oTreeNodeVisualProp.isChecked = 0
    }
  };

  let addExpandAndCollapsePropertyToTaxonomyTree = function (aTaxonomyTree, iLevel, oNewTreeProps, iCheckedStatus, bUseParentStatus) {
    let oTaxonomyVisualProps = ProcessFilterProps.getTaxonomyVisualProps();
    iLevel = iLevel ? ++iLevel : 1;

    CS.forEach(aTaxonomyTree, function (oTaxonomy) {
      if (!oTaxonomy.children) {
        oTaxonomy.children = [];
      }
      if (!CS.isEmpty(oTaxonomy.children)) {
        addExpandAndCollapsePropertyToTaxonomyTree(oTaxonomy.children, iLevel, oNewTreeProps, iCheckedStatus, bUseParentStatus);
      }
      oNewTreeProps[oTaxonomy.id] = oTaxonomyVisualProps[oTaxonomy.id];
      if (!oTaxonomyVisualProps[oTaxonomy.id]) {
        oNewTreeProps[oTaxonomy.id] = {
          isChecked: (bUseParentStatus && iCheckedStatus === 2 && 2) || 0,
          isExpanded: false,
          isHidden: false
        };
      } else {
        if(bUseParentStatus) {
          oNewTreeProps[oTaxonomy.id].isChecked = iCheckedStatus > 0 ? 2 : 0;
        }
      }

      if(iLevel == 1) {
        oNewTreeProps[oTaxonomy.id].isHidden = false;
      }
    });

    return oNewTreeProps;
  };

  let toggleHeaderNodeStateRecursively = function (aNodes, sNodeId, oVisualProps, aAllAffectedNodeIds, bCrossClicked) {

    let bNodeFound = false;
    let bAllChildChecked = true;
    let bAnyChildChecked = false;
    oVisualProps = oVisualProps || ProcessFilterProps.getTaxonomyVisualProps();
    let sSelectedParentId = ProcessFilterProps.getSelectedOuterParentId();
    let _this = this;

    CS.forEach(aNodes, function (oNode) {
      let oNodeVisualProp = oVisualProps[oNode.id] || {};
      if (oNode.id == sNodeId) {
        bNodeFound = true;
        /**** Fixes for cross icon click not working while click on parent node cross icon****/
        let iCheckedState = oNodeVisualProp.isChecked;
        if(oNode.id == sSelectedParentId && bCrossClicked) {
          ProcessFilterProps.setSelectedOuterParentId("-1");
          let oTaxonomyTreeFlatMap = ProcessFilterProps.getTaxonomyTreeFlatMap();
          let aDefaultTaxonomyTree = ProcessFilterProps.getDefaultTaxonomyTree();
          oTaxonomyTreeFlatMap["-1"] = oTaxonomyTreeFlatMap["-1"] || aDefaultTaxonomyTree[0];
          iCheckedState = 2;
        }
        /********************************************* ***************************************/
        let iNewState = getToggledNodeState(iCheckedState);
        oNodeVisualProp.isChecked = iNewState;
        let bNodeHiddenState = oNodeVisualProp.isHidden;
        assignToAllNodesBelow({isChecked: iNewState, isHidden: !iNewState}, oNode, oVisualProps, aAllAffectedNodeIds);
        oNodeVisualProp.isHidden = bNodeHiddenState;
        aAllAffectedNodeIds.push(oNode.id);
      }
      else if (!bNodeFound && !CS.isEmpty(oNode.children)) {
        let oChildInfo = toggleHeaderNodeStateRecursively(oNode.children, sNodeId, oVisualProps, aAllAffectedNodeIds);
        updateNodeStateByChildInfo(oNodeVisualProp, oChildInfo);
        if(oChildInfo.isNodeFound) {
          bNodeFound = true;
          aAllAffectedNodeIds.push(oNode.id);
        }
      } else if (bNodeFound) {
        aAllAffectedNodeIds.push(oNode.id);
      }

      bAllChildChecked = bAllChildChecked && oNodeVisualProp.isChecked == 2;
      bAnyChildChecked = bAnyChildChecked || !!oNodeVisualProp.isChecked;
    });

    return {
      isNodeFound: bNodeFound,
      isAnyChildChecked: bAnyChildChecked,
      isAllChildChecked: bAllChildChecked
    }

  };

  let _handleTreeCheckClicked = (iId, iLevel) => {
    let aTreeList = ProcessFilterProps.getTaxonomyTree();
    if (iLevel === 1) {
      let sParentId = iId == "-1" ? "" : iId;
      _handleBPMNSearchCriteriaEditButtonClicked("",sParentId);
      if(CS.isEmpty(sParentId)){
        ProcessFilterProps.setIsTaxonomySelected(false);
      }else{
        ProcessFilterProps.setIsTaxonomySelected(true);
      }
      ProcessFilterProps.setSelectedOuterParentId(sParentId);
      ProcessFilterProps.setAllAffectedTreeNodeIds([]);
      return;
    } else {
      // added 999 in array as first level dom in taxonomy hierarchy has id 999 in FltrHorizontalTreeView
      let aAllAffectedNodeIds = [999];
      toggleHeaderNodeStateRecursively(aTreeList, iId, null, aAllAffectedNodeIds, false);
      ProcessFilterProps.setAllAffectedTreeNodeIds(aAllAffectedNodeIds);
    }
    _triggerChange();
  };

  let _handleTaxonomyClearAllClicked = () => {
    let oVisualProps = ProcessFilterProps.getTaxonomyVisualProps();
    CS.forEach(oVisualProps, function (oProp) {
      oProp.isChecked = 0;
      oProp.isHidden = false;
    });
    ProcessFilterProps.setAllAffectedTreeNodeIds([]);
    _triggerChange();
  };

  /**********************************  PUBLIC API's   ************************************/
  return {

    fetchProcessList: function () {
      _fetchProcessList();
    },

    getProcessScreenLockStatus: function () {
      return _getProcessScreenLockStatus();
    },

    createProcess: function (bIsMDMCreate) {
      _createProcess(bIsMDMCreate);
    },

    saveAsProcess: function () {
      _saveAsProcess();
    },

    saveProcess: function (oCallback) {
      _saveProcess(oCallback);
    },

    discardUnsavedProcess: function (oCallback) {
      _discardUnsavedProcess(oCallback);
    },

    deleteProcess: function (aSelectedIds) {
      _deleteProcess(aSelectedIds);
    },

    getProcessDetails: function (_sSelectedProcessId) {
      var oActiveProcess = ProcessConfigViewProps.getActiveProcess();
      var sSelectedProcessId = _sSelectedProcessId || oActiveProcess.id;
      _getProcessDetails(sSelectedProcessId);
    },

    handleProcessRefreshMenuClicked: function () {
      var oActiveProcess = ProcessConfigViewProps.getActiveProcess();
      var sSelectedProcessId = oActiveProcess.id;
      if(sSelectedProcessId) {
        _getProcessDetails(sSelectedProcessId);
      } else {
        _fetchProcessList();
      }
    },

    handleProcessListNodeClicked: function (oModel) {
      _handleProcessListNodeClicked(oModel);
    },

    handleProcessComponentDataSourceValueChanged: function (sKey, sValue) {
      _handleProcessComponentDataSourceValueChanged(sKey, sValue);
      _triggerChange();
    },

    handleProcessComponentClassInfoValueChanged: function (sKey, sValue, oReferencedData) {
      _handleProcessComponentClassInfoValueChanged(sKey, sValue, oReferencedData);
      _triggerChange();
    },

    handleProcessComponentClassInfoTaxonomyValueChanged: function (sName, iIndex, sValue) {
      _handleProcessComponentClassInfoTaxonomyValueChanged(sName, iIndex, sValue);
    },

    handleProcessComponentClassInfoAddTaxonomy: function (sName) {
      _handleBPMNElementsAddRowButtonClicked(sName);
    },

    handleProcessComponentClassInfoRemoveTaxonomy: function (sName, iIndex) {
      _handleBPMNElementsRemoveRowButtonClicked(sName, iIndex);
    },

    handleProcessComponentClassInfoAddTagGroup: function (sName) {
      _handleProcessComponentClassInfoAddTagGroup(sName);
    },

    handleProcessComponentClassInfoRemoveTagGroup: function (sName, iIndex) {
      _handleProcessComponentClassInfoRemoveTagGroup(sName, iIndex);
    },

    handleProcessComponentClassInfoAddAttributeGroup: function (sName) {
      _handleProcessComponentClassInfoAddAttributeGroup(sName);
    },

    handleProcessComponentClassInfoRemoveAttributeGroup: function (sName,iIndex) {
      _handleProcessComponentClassInfoRemoveAttributeGroup(sName,iIndex);
    },

    handleProcessDesignChanged: function (xml) {
      _handleProcessDesignChanged(xml);
      _triggerChange();
    },

    handleSetProcessDefinitions: function (oModeler) {
      _handleSetProcessDefinitions(oModeler)
    },

    handleBPMNUploadDialogStatusChanged: function (bStatus) {
      _handleBPMNUploadDialogStatusChanged(bStatus);
    },

    handleBPMNXMLUpload: function (sXML) {
      _handleBPMNXMLUpload(sXML);
    },

    handleSelectionToggleButtonClicked: function (sKey, sId, bSingleSelect) {
      _handleSelectionToggleButtonClicked(sKey, sId, bSingleSelect);
      _triggerChange();
    },

    handleProcessSingleValueChanged: function (sKey, sVal, aSplitContext) {
      _handleProcessSingleValueChanged(sKey, sVal, aSplitContext);
    },

    handleProcessLazyMSSValueChanged: function (sKey, aVal, oReferencedData) {
      _handleProcessLazyMSSValueChanged(sKey,aVal, oReferencedData);
    },

    fetchAllCustomComponents: function () {
      _fetchAllCustomComponents();
    },

    reset: function (bIsTreeItemClicked) {
      if (bIsTreeItemClicked) {
        SettingUtils.getAppData().setProcessList([]);
        ProcessConfigViewProps.reset();
      }
    },

    handleListViewSearchOrLoadMoreClicked: function (sSearchText, bLoadMore) {
      let oProcessList = SettingUtils.getAppData().getProcessList();
      SettingUtils.handleListViewSearchOrLoadMoreClicked(sSearchText, bLoadMore, oProcessList, ProcessConfigViewProps, _fetchProcessList);
    },

    cancelProcessCreation: function () {
      _cancelProcessCreation();
      _triggerChange();
    },

    closeProcessDialog: function () {
      _closeProcessDialog()
    },

    handleSaveAsDialogCloseButtonClicked: function () {
      _handleSaveAsDialogCloseButtonClicked()
    },

    handleSaveAsDialogSaveButtonClicked: function () {
      _handleSaveAsDialogSaveButtonClicked()
    },

    handleSaveAsDialogValueChanged: function (sContentId, sPropertyId, sValue) {
      _handleSaveAsDialogValueChanged(sContentId, sPropertyId, sValue);
    },

    handleWorkflowTypeClicked: function (aSelectedItem) {
      // _handleWorkflowTypeClicked(aSelectedItem);
      _triggerChange();
    },

    handleEntityTypeClicked: function (aSelectedItem) {
      _handleEntityTypeClicked(aSelectedItem);
      _triggerChange();
    },

    handleWorkflowTabClicked: function (bIsTreeItemClicked) {
      _handleWorkflowTabClicked(bIsTreeItemClicked);
      _triggerChange();
    },

    handleProcessAndSetNewCustomElement: function (oNewElement, oCustomDefinition, bpmnFactory, modeling) {
      _handleProcessAndSetNewCustomElement(oNewElement, oCustomDefinition, bpmnFactory, modeling);
    },

    handleProcessAndSetNewElement: function (oNewElement) {
      _handleProcessAndSetNewElement(oNewElement);
    },

    handleSetActiveBPMNInstances : function (oNewElement, oBPMNFactory, oModelling) {
      _getInputOutputParametersSeparation(oNewElement,oBPMNFactory, oModelling);
      // ProcessConfigViewProps.setActiveBPMNElement(oNewElement);
      // ProcessConfigViewProps.setActiveBPMNFactoryInstance(oBPMNFactory);
      // ProcessConfigViewProps.setActiveBPMNModelingInstance(oModelling);
      // _prepareAdvancedFilterData(oNewElement);
      // _triggerChange();
    },

    handleBPMNCustomTabsUpdate: function () {
      _handleBPMNCustomTabsUpdate();

    },

    handleBPMNCustomElementTextChanged: function (sName, sSelectedAttributeId,sNewVal) {
      switch (sName){
        case "isAdvancedOptionsEnabled":
          _handleIsAdvancedOptionsEnabledToggle();
          break;

        case "ATTRIBUTES_VALUES_MAP":
          _handleBPMNTextValueChanged(sName,sSelectedAttributeId, sNewVal );
          break;

        default:
          _makeActiveProcessDirty();
          _handleBPMNCustomElementSingleValueChanged(sName, sNewVal);
          break;
      }
    },

    handleBPMNPropertiesAttributeDateChangedCustom: function (sName, sSelectedAttributeId, sNewVal) {
      _handleBPMNPropertiesAttributeDateChangedCustom(sName, sSelectedAttributeId, sNewVal);
    },

    handleBPMNPropertiesTagMSSChanged: function (sName, aSelectedItems, oReferencedData) {
      _handleBPMNPropertiesTagMSSChanged(sName, aSelectedItems, oReferencedData);
    },

    handleBPMNPropertiesTagMSSChangedCustom: function (sName, sStatus, sSelectedTagGroup, aSelectedItems, oReferencedData) {
      _handleBPMNPropertiesTagMSSChangedCustom(sName, sStatus, sSelectedTagGroup, aSelectedItems, oReferencedData);
    },

    handleBPMNPropertiesAdvancedFilterChangedCustom: function () {
      _handleBPMNPropertiesAdvancedFilterChangedCustom();
    },

    handleBPMNPropertiesAttributeMSSChangedCustom: function (sName, sStatus, sSelectedTagGroup, aSelectedItems, oReferencedData) {
      _handleBPMNPropertiesAttributeMSSChangedCustom(sName, sStatus, sSelectedTagGroup, aSelectedItems, oReferencedData);
    },

    handleSearchFilterEditButtonClicked: function (sName) {
      _handleSearchFilterEditButtonClicked(sName);
    },

    handleBPMNCustomElementMSSChanged: function (sName, aSelectedItems, oReferencedData) {
      _makeActiveProcessDirty();
      let oActiveProcess = ProcessConfigViewProps.getActiveProcess();
      let oProcessInputElementSkeleton = MockDataForProcessInputParameters[sName];
      let sRequestSelectedElementKey = oProcessInputElementSkeleton.requestSelectedElementKey;
      let sConfigDetailsKey = oProcessInputElementSkeleton.referencedDataKey;
      (!oActiveProcess.configDetails) && (oActiveProcess.configDetails = {});
      let oConfigDetails = oActiveProcess.configDetails;
      (!oConfigDetails[sRequestSelectedElementKey]) && (oConfigDetails[sRequestSelectedElementKey] = []);
      oConfigDetails[sRequestSelectedElementKey] = CS.union(oConfigDetails[sRequestSelectedElementKey], aSelectedItems);
      (oConfigDetails[sConfigDetailsKey]) && (CS.assign(oConfigDetails[sConfigDetailsKey], oReferencedData));
      if (oProcessInputElementSkeleton.selectionContext == "multiSelectList" || oProcessInputElementSkeleton.selectionContext == "singleSelectList") {
        _handleBPMNCustomElementMSSChanged(oActiveProcess, sName, aSelectedItems)
      } else {
        _handleBPMNCustomElementSingleValueChanged(sName, aSelectedItems[0]);
      }
    },

    handleAllowedTypesDropdownOpened: function (sContext, sTaxonomyId , sOtherTaxonomyId) {
      if(sOtherTaxonomyId){
        ProcessConfigViewProps.setAllowedTaxonomiesInProcessConfig([]);
      }else{
        ProcessConfigViewProps.setAllowedTaxonomies([]);
      }

      let oTaxonomyPaginationData = ProcessConfigViewProps.getTaxonomyPaginationData();
      oTaxonomyPaginationData.searchText = "";
      oTaxonomyPaginationData.from = 0;
      _fetchTaxonomyById(sContext, sTaxonomyId,sOtherTaxonomyId);
    },

    editButtonClicked: function (sEntityId) {
      ProcessConfigViewProps.setIsProcessDialogActive(true);
      ProcessConfigViewProps.setIsFullScreenMode(false);
      _getProcessDetails(sEntityId);
    },

    fetchProcessListForGridView : function () {
      _fetchProcessListForGridView();
    },

    handleTaxonomyAdded: function (oModel, sTaxonomyId, sParentTaxonomyId, sViewContext) {
      _handleTaxonomyAdded(oModel, sTaxonomyId, sParentTaxonomyId, sViewContext);
      _triggerChange();
    },

    handleLazyMSSValueRemoved: function (sOldContextKey,sId) {
      _handleLazyMSSValueRemoved(sOldContextKey,sId);
    },

    handleMultiSelectSmallTaxonomyViewCrossIconClicked: function (oTaxonomy, sParentTaxonomyId, sViewContext) {
      _handleMultiSelectSmallTaxonomyViewCrossIconClicked(oTaxonomy, sParentTaxonomyId, sViewContext);
    },

    discardProcessGridViewChanges: function (oCallbackData) {
      _discardProcessGridViewChanges(oCallbackData);
    },

    processListBulkSave:function (oCallbackData) {
      _processListBulkSave(oCallbackData);
    },

    handleAdvancedSearchListItemNodeClicked:function (sItemId, sType, sContext) {
      _handleAdvancedSearchListItemNodeClicked(sItemId, sType, sContext);
    },

    handleAdvancedSearchListSearched:function (sSearchText, sContext) {
      // _handleAdvancedSearchListSearched(sSearchText, sContext);
    },

    handleAdvancedSearchDataSave:function () {
      _handleAdvancedSearchDataSave();
    },

    handleFilterElementDeleteClicked:function (sElementId) {
      _handleFilterElementDeleteClicked(sElementId);
    },

    handleRemoveAppliedFilterClicked: function (sElementId) {
      _handleRemoveAppliedFilterClicked(sElementId);
    },

    handleFilterElementExpandClicked:function (sElementId) {
      _handleFilterElementExpandClicked(sElementId);
    },

    handleAddAttributeClicked:function (sElementId) {
      _handleAddAttributeClicked(sElementId);
    },

    handleFilterAttributeValueDeleteClicked:function (sAttributeId, sValId) {
      _handleFilterAttributeValueDeleteClicked(sAttributeId, sValId);
    },

    handleFilterAttributeValueTypeChanged:function (sAttributeId, sValId, sTypeId) {
      _handleFilterAttributeValueTypeChanged(sAttributeId, sValId, sTypeId);
    },

    handleFilterAttributeValueChanged:function (sAttributeId, sValId, sVal) {
      _handleFilterAttributeValueChanged(sAttributeId, sValId, sVal);
    },

    handleFilterAttributeValueChangedForRange:function (sAttributeId, sValId, sVal, sRange) {
      _handleFilterAttributeValueChangedForRange(sAttributeId, sValId, sVal, sRange);
    },

    handleFilterTagValueChanged:function (sTagId, aTagValueRelevanceData) {
      _handleFilterTagValueChanged(sTagId, aTagValueRelevanceData);
    },

    prepareAdvancedFilterData: function() {
      let oElement = ProcessConfigViewProps.getActiveBPMNElement();
      _prepareAdvancedFilterData(oElement);
    },

    handleGridPropertyValueChangeDependencies:function () {
      _handleGridPropertyValueChangeDependencies();
    },

    handleTaxonomySearchClicked: function (sContext, sTaxonomyId, sSearchText) {
      _handleTaxonomySearchClicked(sContext, sTaxonomyId, sSearchText);
    },

    handleTaxonomyLoadMoreClicked: function (sContext, sTaxonomyId) {
      _handleTaxonomyLoadMoreClicked(sContext, sTaxonomyId);
    },

    handleBPMNTaxonomySearchClicked: function (sContext, sTaxonomyId, sSearchText) {
      _handleBPMNTaxonomySearchClicked(sContext, sTaxonomyId, sSearchText);
    },

    handleBPMNTaxonomyLoadMoreClicked: function (sContext, sTaxonomyId) {
      _handleBPMNTaxonomyLoadMoreClicked(sContext, sTaxonomyId);
    },

    handleAdvancedSearchPanelClearClicked: function () {
      _handleAdvancedSearchPanelClearClicked();
    },

    handleAppliedFilterClearClicked: function () {
      _handleAppliedFilterClearClicked();
    },

    handleMSSValueClicked: function () {
      _handleMSSValueClicked();
    },

    handleBPMNElementsDataLanguageMSSChanged: function (sName, aSelectedItems, oReferencedData) {
      _handleBPMNElementsDataLanguageMSSChanged(sName, aSelectedItems, oReferencedData);
    },

    handleBPMNElementsGroupMSSChanged: function (sContext, sAction, aSelectedRoles) {
      _handleBPMNElementsGroupMSSChanged(sContext, sAction, aSelectedRoles);
    },

    handleBPMNElementsVariableMapChanged: function (sName, iIndex, sType, sValue) {
      _handleBPMNElementsVariableMapChanged(sName, iIndex, sType, sValue);
    },

    handleBPMNElementsAddVariableMap: function (sName) {
      _handleBPMNElementsAddVariableMap(sName);
    },

    handleBPMNElementsRemoveVariableMap: function (sName, iIndex) {
      _handleBPMNElementsRemoveVariableMap(sName, iIndex);
    },

    handleBPMNElementsAddRowButtonClicked: function (sName) {
      _handleBPMNElementsAddRowButtonClicked(sName);
    },

    handleBPMNElementsRemoveRowButtonClicked: function (sName, iIndex) {
      _handleBPMNElementsRemoveRowButtonClicked(sName, iIndex);
    },

    handleBPMNElementsEntityMapChanged: function (sName, iIndex, sItemid, sNewVal) {
      _handleBPMNElementsEntityMapChanged(sName, iIndex, sItemid, sNewVal);
    },

    handleFilterChildToggled: function (sParentId, sChildId, sContext, oExtraData) {
      _handleFilterChildToggled(sParentId, sChildId, sContext, oExtraData);
      _triggerChange();
    },

    handleCollapseFilterClicked : function (bExpanded) {
      _handleCollapseFilterClicked(bExpanded);
      _triggerChange();
    },

    handleApplyFilterButtonClicked : function () {
      _handleApplyFilterButtonClicked();
    },

    handleFilterItemPopoverClosed : function () {
      _handleFilterItemPopoverClosed();
      _triggerChange();
    },

    handleFilterItemSearchTextChanged : function (sId, sSearchedText) {
      _handleFilterItemSearchTextChanged(sId, sSearchedText);
      _triggerChange();
    },

    handleFilterSummarySearchTextChanged : function (sFilterId, sSearchedText) {
      _handleFilterSummarySearchTextChanged(sFilterId, sSearchedText);
      _triggerChange();
    },

    handleTabLayoutTabChanged: function (sTabId) {
      _handleTabLayoutTabChanged(sTabId);
    },

    handleProcessFrequencySummaryDateButtonClicked: function (sDate, sContext) {
      _handleProcessFrequencySummaryDateButtonClicked(sDate, sContext);
    },

    handleBPMNSearchCriteriaEditButtonClicked: function (sName) {
      _handleBPMNSearchCriteriaEditButtonClicked(sName);
    },

    handleTreeCheckClicked: function (iId, iLevel) {
      _handleTreeCheckClicked(iId, iLevel);
    },

    handleTaxonomyClearAllClicked: function () {
      _handleTaxonomyClearAllClicked();
    },
  }
})();

MicroEvent.mixin(ProcessStore);

export default ProcessStore;
