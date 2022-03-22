/**
 * Created by CS80 on 20/3/2017.
 */

import CS from '../../../../../../libraries/cs';

import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import { TemplateRequestMapping } from '../../tack/setting-screen-request-mapping';
import TemplateProps from './../model/template-config-view-props';
import SettingUtils from './../helper/setting-utils';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import ConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import TemplateConfigGridSkeleton from './../../tack/template-config-grid-view-skeleton';
import GridViewContexts from '../.././../../../../commonmodule/tack/grid-view-contexts';
import SettingScreenProps from './../model/setting-screen-props';
import Exception from '../../../../../../libraries/exceptionhandling/exception.js';
import GridViewStore from '../../../../../../viewlibraries/contextualgridview/store/grid-view-store';
import CommonUtils from "../../../../../../commonmodule/util/common-utils";

var TemplateStore = (function () {

  var _triggerChange = function () {
    TemplateStore.trigger('template-changed');
  };

  var _getActiveTemplate = function () {
    return TemplateProps.getActiveTemplate();
  };

  var _setActiveTemplate = function (oTemplate) {
    return TemplateProps.setActiveTemplate(oTemplate);
  };

  var _setTemplateReferencedData = function (oRefData) {
    return TemplateProps.setReferencedTemplateData(oRefData);
  };

  var _makeActiveTemplateDirty = function () {
    var oActiveTemplate = _getActiveTemplate();
    if (!oActiveTemplate.clonedObject) {
      SettingUtils.makeObjectDirty(oActiveTemplate);
    }
    return oActiveTemplate.clonedObject;
  };

  var failureGetTemplate = function (sId, oResponse) {
    var oFailure = oResponse && oResponse.failure;
    if(oFailure && oFailure.exceptionDetails) {
      var oException = CS.find(oFailure.exceptionDetails, {key: "TemplateNotFoundException"});
      if(oException) {
        var oAppData = SettingUtils.getAppData();
        var oTemplatesMap = oAppData.getTemplatesList();
        _setActiveTemplate({});
        sId && delete oTemplatesMap[sId];
        _triggerChange();
      }
    }
    SettingUtils.failureCallback(oResponse, "failureGetTemplate", getTranslation());
  };

  var successCreateTemplate = function (oNewTemplate, oResponse) {
    let oScreenProps = SettingScreenProps.screen;
    let oSuccess = oResponse.success;
    let oSavedTemplate = oSuccess.template;
    oSavedTemplate.type = oNewTemplate.type || null;

    let aTemplateGridData = TemplateProps.getTemplateGridData();
    aTemplateGridData.push(oSavedTemplate);
    //let oProcessedTemplate = _preProcessTemplateDataForGridView([oSavedTemplate])[0];
    let oProcessedTemplate = GridViewStore.getProcessedGridViewData(GridViewContexts.TEMPLATE, [oSavedTemplate])[0];
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.TEMPLATE);

    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    aGridViewData.unshift(oProcessedTemplate);

    let oTemplatesMap = SettingUtils.getAppData().getTemplatesList();
    oTemplatesMap[oSavedTemplate.id] = oSavedTemplate;

    oGridViewPropsByContext.setGridViewTotalItems(oGridViewPropsByContext.getGridViewTotalItems() + 1);

    if (oNewTemplate.isCreated) {
      alertify.success(getTranslation().DTP_TEMPLATE_CREATED_SUCCESSFULLY);
    } else {
      alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY,{ entity : getTranslation().TEMPLATE}));
    }

    _setActiveTemplate({});

      /*var oAppData = SettingUtils.getAppData();
      var oTemplatesMap = oAppData.getTemplatesList();
      oTemplatesMap[oNewTemplate.id] = oNewTemplate;
      alertify.success(getTranslation().DTP_TEMPLATE_CREATED_SUCCESSFULLY)
      successGetTemplate(oResponse);*/

    _triggerChange();
  };

  var _createTemplate = function () {
    var oNewTemplate = {};
    oNewTemplate.id = UniqueIdentifierGenerator.generateUUID();
    oNewTemplate.label = UniqueIdentifierGenerator.generateUntitledName();
    oNewTemplate.type = "customTemplate";
    oNewTemplate.code = "";
    oNewTemplate.isCreated = true;
    _setActiveTemplate(oNewTemplate);
    _triggerChange();
  };

  var _createTemplateDialogButtonClicked = function () {
    var oActiveTemplate = _getActiveTemplate();
    oActiveTemplate = oActiveTemplate.clonedObject || oActiveTemplate;
    if(CS.isEmpty(oActiveTemplate.label.trim())) {
      alertify.error(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
      return;
    }
    var oCodeToVerifyUniqueness = {
      id: oActiveTemplate.code,
      entityType: ConfigEntityTypeDictionary.ENTITY_TYPE_TEMPLATE

    };
    var oCallbackDataForUniqueness = {};
    oCallbackDataForUniqueness.functionToExecute = _createTemplateCall.bind(this, oActiveTemplate);
    var sURL = TemplateRequestMapping.CheckEntityCode;
    SettingUtils.checkForUniquenessOfCodePropertyAndSendAjaxCall(oCodeToVerifyUniqueness, sURL, oCallbackDataForUniqueness);
  };

  var _createTemplateCall = function(oActiveTemplate) {
    SettingUtils.csPutRequest(TemplateRequestMapping.CreateTemplate, {}, oActiveTemplate, successCreateTemplate.bind(this, oActiveTemplate), failureGetTemplate);
  };

  let _createADMForTemplateGrid = function () {
    let aTemplateGridData = TemplateProps.getTemplateGridData();        // saved Original(unprocessed) data
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.TEMPLATE);
    let aGridViewData = oGridViewPropsByContext.getGridViewData();    // saved Processed data
    let aADMData = [];                                                   // ADM to return collection of ADM objects per row

    CS.forEach(aGridViewData, function (oNewTemplateData, iIndex) {
      if(oNewTemplateData.isDirty){
        let oTemplate = CS.find(aTemplateGridData, {id: oNewTemplateData.id});
        //let oOldTemplateData = _preProcessTemplateDataForGridView([oTemplate])[0];
        let oOldTemplateData = GridViewStore.getProcessedGridViewData(GridViewContexts.TEMPLATE, [oTemplate])[0];
        let oOldProperties = oOldTemplateData.properties;
        let oNewProperties = oNewTemplateData.properties;

        let oADMData = {
          id: oOldTemplateData.id,
          label: oOldProperties.label.value,
          code: oOldProperties.code.value,
          icon: oOldTemplateData.icon,
        };

        oADMData.label = oNewProperties.label.value;

        oADMData.addedPropertyCollections = CS.difference(oNewProperties.propertyCollectionIds.value, oOldProperties.propertyCollectionIds.value);
        oADMData.deletedPropertyCollections = CS.difference(oOldProperties.propertyCollectionIds.value, oNewProperties.propertyCollectionIds.value);
        oADMData.addedRelationships = CS.difference(oNewProperties.relationshipIds.value, oOldProperties.relationshipIds.value);
        oADMData.deletedRelationships = CS.difference(oOldProperties.relationshipIds.value, oNewProperties.relationshipIds.value);
        oADMData.addedContexts = CS.difference(oNewProperties.contextIds.value, oOldProperties.contextIds.value);
        oADMData.deletedContexts = CS.difference(oOldProperties.contextIds.value, oNewProperties.contextIds.value);
        oADMData.addedNatureRelationships = CS.difference(oNewProperties.natureRelationshipIds.value, oOldProperties.natureRelationshipIds.value);
        oADMData.deletedNatureRelationships = CS.difference(oOldProperties.natureRelationshipIds.value, oNewProperties.natureRelationshipIds.value);

        aADMData.push(oADMData);
      }
    });

    return aADMData;
  };

  var _handleDeleteTemplateFailure = function (aFailureIds) {
    let aTemplateAlreadyDeleted = [];
    let aUnhandledTemplate = [];
    let aTemplateGridData = TemplateProps.getEventGridData();
    CS.forEach(aFailureIds, function (oItem) {
      let oTemplate = CS.find(aTemplateGridData, {id: oItem.itemId});
      if (oItem.key == "EventNotFoundException") {
        aTemplateAlreadyDeleted.push(oTemplate.label);
      } else {
        aUnhandledTemplate.push(oTemplate.label);
      }
    });
    if (aTemplateAlreadyDeleted.length > 0) {
      let sTemplateAlreadyDeleted = aTemplateAlreadyDeleted.join(',');
      alertify.error(Exception.getCustomMessage("Template_already_deleted", getTranslation(), sTemplateAlreadyDeleted), 0);
    }
    if (aUnhandledTemplate.length > 0) {
      let sUnhandledTemplate = aUnhandledTemplate.join(',');
      alertify.error(Exception.getCustomMessage("Unhandled_Template", getTranslation(), sUnhandledTemplate), 0);
    }
  };


  var successDeleteTemplateCallback = function (oResponse) {
    let aSuccessIds = oResponse.success;
    let aFailureIds = oResponse.failure ? oResponse.failure.exceptionDetails : [];
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.TEMPLATE);

    let aGridViewData = oGridViewPropsByContext.getGridViewData();
    let oMasterTemplateList = SettingUtils.getAppData().getTemplatesList();
    let oGridViewSkeleton = oGridViewPropsByContext.getGridViewSkeleton();

    CS.forEach(aSuccessIds, function (sId) {
      CS.remove(aGridViewData, {id: sId});
      CS.pull(oGridViewSkeleton.selectedContentIds, sId);
      delete oMasterTemplateList[sId];
    });

    oGridViewPropsByContext.setGridViewTotalItems(oGridViewPropsByContext.getGridViewTotalItems() - oResponse.success.length);
    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_DELETED_SUCCESSFULLY,{entity : getTranslation().CLASS_CATEGORY_DTP_TEMPLATE_TITLE}));

    if (aFailureIds && aFailureIds.length > 0) {
      _handleDeleteTemplateFailure(aFailureIds);
    }
    oCallback.alertifyToShow = function () { // eslint-disable-line
      alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY,{ entity : getTranslation().TEMPLATE}));
    };
  };

  var failureDeleteTemplateCallback = function (oResponse) {
    if (!CS.isEmpty(oResponse.failure)) {
      _handleDeleteTemplateFailure([]);
    } else {
      SettingUtils.failureCallback(oResponse, 'failureDeleteTemplateCallback', getTranslation());
    }
    _triggerChange();
  };

  var _deleteTemplates = function (aBulkDeleteList) {
    // var aFilteredTemplateIds = _deleteUnSavedTemplate(aBulkDeleteList);
    if (!CS.isEmpty(aBulkDeleteList)) {
      return SettingUtils.csDeleteRequest(TemplateRequestMapping.DeleteTemplate, {}, {ids: aBulkDeleteList}, successDeleteTemplateCallback, failureDeleteTemplateCallback);
    } else {
      alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_DELETED_SUCCESSFULLY,{entity : getTranslation().CLASS_CATEGORY_DTP_TEMPLATE_TITLE}));
      _triggerChange();
    }
  };

  var _deleteTemplate = function (aSelectedIds) {
    // var aTemplateIdsListToDelete = _getCheckedTemplatesAndSelectedIdList();
    var oMasterTemplates = SettingUtils.getAppData().getTemplatesList();
    var bIsStandard = false;

    if (!CS.isEmpty(aSelectedIds)) {
      var aBulkDeleteTemplates = [];
      CS.forEach(aSelectedIds, function (iId) {
        var oMasterTemplate = oMasterTemplates[iId];

        bIsStandard = oMasterTemplate.type != "customTemplate";
        if (bIsStandard) {
          return false;
        }
        let sBulkDeleteTemplateLabel = CS.getLabelOrCode(oMasterTemplate);
        aBulkDeleteTemplates.push(sBulkDeleteTemplateLabel);
      });
      if (bIsStandard) {
        alertify.message(getTranslation().STANDARD_TEMPLATE_DELETION);
        return;
      }
      //sBulkDeleteTemplates = CS.trimEnd(sBulkDeleteTemplates, ', ');
      CustomActionDialogStore.showListModeConfirmDialog(getTranslation().DELETE_CONFIRMATION, aBulkDeleteTemplates,
          function () {
            _deleteTemplates(aSelectedIds)
            .then(_fetchTemplateListForGridView);
          }, function (oTemplate) {
          });
    } else {
      alertify.message(getTranslation().NOTHING_SELECTED_TO_DELETE);
    }
  };

  var _handleTemplateHeaderVisibilityToggled = function (sType, sContext) {
    var oActiveTemplate = _makeActiveTemplateDirty();
    switch (sType) {
      case "headerVisibility":
        var oHeaderVisibility = oActiveTemplate.headerVisibility || {};
        oHeaderVisibility[sContext] = !oHeaderVisibility[sContext];
        if (sContext == "viewCreatedOn") {
          oHeaderVisibility["viewLastModifiedBy"] = oHeaderVisibility[sContext];
        }
        break;
      case "tabs":
        var aTabs = oActiveTemplate.tabs || [];
        var oTab = CS.find(aTabs, {baseType: sContext}) || {};
        oTab.isVisible = !oTab.isVisible;
        break;
    }
    _triggerChange();
  };

  let _handleCustomTemplateSelectedEntitiesChanged = function (aSelectedItems, sContext) {
    let oActiveTemplate = _makeActiveTemplateDirty();

    switch (sContext) {
      case 'propertyCollections':
        oActiveTemplate.propertyCollectionIds = aSelectedItems;

        break;
      case 'relationships':
        oActiveTemplate.relationshipIds = aSelectedItems;
        break;
      case 'contexts':
        oActiveTemplate.contextIds = aSelectedItems;
        break;
      case 'natureRelationships':
        oActiveTemplate.natureRelationshipIds = aSelectedItems;
        break;
    }
   _saveTemplate(); // eslint-disable-line
  };

  let _handleSelectedEntityRemoved = function (sContext, sId) {
    let oActiveTemplate = _makeActiveTemplateDirty();

    switch (sContext) {
      case 'propertyCollections':
        CS.remove(oActiveTemplate.propertyCollectionIds, function (sPCId) {
          return sPCId == sId;
        });
        break;
      case 'relationships':
        CS.remove(oActiveTemplate.relationshipIds, function (sRelationshipId) {
          return sRelationshipId == sId;
        });
        break;
      case 'contexts':
        CS.remove(oActiveTemplate.contextIds, function (sContextId) {
          return sContextId == sId;
        });
        break;
      case 'natureRelationships':
        CS.remove(oActiveTemplate.natureRelationshipIds, function (aNRId) {
          return aNRId == sId;
        });
        break;
    }
    _saveTemplate(); // eslint-disable-line
  };

  var _handleTemplateHeaderTabClicked = function (sTabType) {
    TemplateProps.setCurrentTabType(sTabType);
    _triggerChange();
  };

  var _handleTabIconChanged = function (sTabType, sIcon) {
    var oActiveTemplate = _makeActiveTemplateDirty();
    var oTab = CS.find(oActiveTemplate.tabs, {baseType: sTabType});
    oTab.icon = sIcon;
    _triggerChange();
  };

  var _handleTemplateSectionAdded = function (sSectionId) {
  };

  var _cancelTemplateDialogClick = function () {
    _setActiveTemplate({});
    _triggerChange();
  };

  var _handleTemplateConfigDialogButtonClicked = function (sButtonId) {
    if (sButtonId == "create") {
      _createTemplateDialogButtonClicked();
    } else {
      _cancelTemplateDialogClick();
    }
  };

  /* var _getMSSObject = function (oSelectedTemplate, oRegResponseInfo, context) {
     let oTemplateReferencedData = TemplateProps.getTemplateReferencedData();
     let oList = {};
     let aList = [];
     let aSelectedList = [];
     let sContext = '';

     switch(context){
       case 'propertyCollections':
         oList = oTemplateReferencedData.referencedPropertyCollections;
         aList = CS.values(oList);
         aSelectedList = oSelectedTemplate.propertyCollectionIds;
         sContext = "templatePropertyCollection";
         break;

       case 'contexts':
         oList = oTemplateReferencedData.referencedContexts;
         aList = CS.values(oList);
         aSelectedList = oSelectedTemplate.contextIds;
         sContext = "templateContext";
         break;

       case 'relationships':
         oList = oTemplateReferencedData.referencedRelationships;
         aList = CS.values(oList);
         aSelectedList = oSelectedTemplate.relationshipIds;
         sContext = "templateRelationship";
         break;



       case 'natureRelationships':
         oList = oTemplateReferencedData.referencedNatureRelationships;
         aList = CS.values(oList);
         aSelectedList = oSelectedTemplate.natureRelationshipIds;
         sContext = "templateNatureRelationship";
         break;


       default: break;
     }

     return {
       disabled: false,
       label: "",
       items: aList,
       selectedItems: aSelectedList,
       singleSelect: false,
       context: sContext,
       referencedData: oList,
       requestResponseInfo: oRegResponseInfo
     }
   };



   var _preProcessTemplateDataForGridView = function (aTemplateList) {
     var oGridSkeleton = {} //SettingScreenProps.screen.getGridViewSkeleton();
     var aGridViewData = [];

     CS.forEach(aTemplateList, function (oTemplate) {
       let oProcessedTemplate = {};
       oProcessedTemplate.id = oTemplate.id;
       oProcessedTemplate.isExpanded = false;
       oProcessedTemplate.children = [];
       oProcessedTemplate.actionItemsToShow = ["delete"];
       oProcessedTemplate.properties = {};

       CS.forEach(oGridSkeleton.fixedColumns, function (oColumn) {
         switch (oColumn.id) {

           case "label":
             if (oTemplate.hasOwnProperty(oColumn.id)) {
               oProcessedTemplate.properties[oColumn.id] = {
                 value: oTemplate[oColumn.id],
                 bIsMultiLine: false,
                 showTooltip: true,
                 placeholder: oTemplate["code"]
               };
             }
             break;
           case "code":

             if (oTemplate.hasOwnProperty(oColumn.id)) {
               oProcessedTemplate.properties[oColumn.id] = {
                 value: oTemplate[oColumn.id],
                 isDisabled: true,
                 bIsMultiLine: false
               };
             }
             break;

           default:
             if (oTemplate.hasOwnProperty(oColumn.id)) {
               oProcessedTemplate.properties[oColumn.id] = {
                 value: oTemplate[oColumn.id]
               };
             }
         }
       });

       CS.forEach(oGridSkeleton.scrollableColumns, function (oColumn) {
         let sEntityName = "";
         switch (oColumn.id) {
           case "propertyCollectionIds":
             sEntityName = "propertyCollections";
             break;

           case "relationshipIds":
             sEntityName = "relationships";
             break;


           case "contextIds":
             sEntityName = "contexts";
             break;

           case "natureRelationshipIds":
             sEntityName = "natureRelationships";
             break;


         }

         if (oTemplate.hasOwnProperty(oColumn.id)) {
           let oMSSRequestResponseObj = {
             requestType: "configData",
             entityName: sEntityName,
           };
           oProcessedTemplate.properties[oColumn.id] = _getMSSObject(oTemplate, oMSSRequestResponseObj, sEntityName);
           oProcessedTemplate.properties[oColumn.id].value = oProcessedTemplate.properties[oColumn.id].selectedItems;
         }
       });

       aGridViewData.push(oProcessedTemplate);
     });

     return aGridViewData;
   };
 */

  var successFetchTemplateListForGridView = function (oResponse) {
    let oSuccessResponse = oResponse.success;
    let aTemplateList = oSuccessResponse.list;
    _setTemplateReferencedData(oSuccessResponse.configDetails);

    //var aProcessedGridViewData = _preProcessTemplateDataForGridView(aTemplateList);
    GridViewStore.preProcessDataForGridView(GridViewContexts.TEMPLATE, aTemplateList, oResponse.success.count);
    SettingUtils.getAppData().setTemplatesList(aTemplateList);
    _setActiveTemplate({});

    TemplateProps.setTemplateGridData(aTemplateList);

    let oScreenProps = SettingScreenProps.screen;
    //oScreenProps.setGridViewData(aProcessedGridViewData);
    //oScreenProps.setGridViewTotalItems(oResponse.success.count);
    //oScreenProps.setIsGridDataDirty(false);
    //oScreenProps.setGridViewVisualData({});
    //oScreenProps.setGridViewContext(GridViewContexts.TEMPLATE);

    _triggerChange();
  };

  var failureFetchTemplateListForGridView = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureFetchTemplateListForGridView", getTranslation());
  };


  var _fetchTemplateListForGridView = function () {
    let oPostData = GridViewStore.getPostDataToFetchList(GridViewContexts.TEMPLATE)
    SettingUtils.csPostRequest(TemplateRequestMapping.GetAllTemplatesData, {}, oPostData, successFetchTemplateListForGridView, failureFetchTemplateListForGridView);
  };

  let _setUpTemplateConfigGridView = function () {
    let oTemplateConfigGridViewSkeleton = new TemplateConfigGridSkeleton();
    GridViewStore.createGridViewPropsByContext(GridViewContexts.TEMPLATE, {skeleton: oTemplateConfigGridViewSkeleton});
    //fetch attribute list
    _fetchTemplateListForGridView();
  };


  let successSaveTemplatesInBulk = function (oCallbackData, oResponse) {
    let aTemplatesList = oResponse.success.list;
    var aProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.TEMPLATE, aTemplatesList);
    var aTemplateGridData = TemplateProps.getTemplateGridData();
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.TEMPLATE);

    var aGridViewData = oGridViewPropsByContext.getGridViewData();
    var oMasterTemplatesMap = SettingUtils.getAppData().getTemplatesList();

    CS.forEach(aTemplatesList, function (oTemplate) {
      var sTemplateId = oTemplate.id;
      var iIndex = CS.findIndex(aTemplateGridData, {id: sTemplateId});
      aTemplateGridData[iIndex] = oTemplate;
      oMasterTemplatesMap[sTemplateId] = oTemplate;
    });

    CS.forEach(aProcessedGridViewData, function (oProcessedTemplate) {
      var iIndex = CS.findIndex(aGridViewData, {id: oProcessedTemplate.id});
      aGridViewData[iIndex] = oProcessedTemplate;
    });

    let bIsAnyTemplateDirty = false;
    CS.forEach(aGridViewData, function (oTemplate) {
      if (oTemplate.isDirty) {
        bIsAnyTemplateDirty = true;
      }
    });
    !bIsAnyTemplateDirty && oGridViewPropsByContext.setIsGridDataDirty(false);

    alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY,{ entity : getTranslation().TEMPLATE}));
    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }
  };


  let failureSaveTemplatesInBulk = function (oResponse) {
    SettingUtils.failureCallback(oResponse, "failureSaveTemplatesInBulk", getTranslation());
  };

  let _saveTemplateInBulk = function (aTemplateListToSave, oCallbackData) {
      let oADMToSave = _createADMForTemplateGrid();

      return SettingUtils.csPostRequest(TemplateRequestMapping.BulkSaveTemplates, {}, oADMToSave, successSaveTemplatesInBulk.bind(this, oCallbackData), failureSaveTemplatesInBulk);
  };

  let _postProcessTemplateListAndSave = function (oCallbackData) {
    let aTemplateListToSave = [];

    let bSafeToSave = GridViewStore.processGridDataToSave(aTemplateListToSave, GridViewContexts.TEMPLATE, TemplateProps.getTemplateGridData());

    if (bSafeToSave) {
      return _saveTemplateInBulk(aTemplateListToSave, oCallbackData);
    }
    return CommonUtils.rejectEmptyPromise();
  };

  var _discardTemplateGridViewChanges = function (oCallbackData) {
    var aTemplateGridData = TemplateProps.getTemplateGridData(); //saved Original(unprocessed) data
    let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.TEMPLATE);

    var aGridViewData = oGridViewPropsByContext.getGridViewData(); //saved Processed data
    var bShowDiscardMessage = false;

    CS.forEach(aGridViewData, function (oOldProcessedTemplate, iIndex) { //add the successfully saved attributes into stored data:
      if (oOldProcessedTemplate.isDirty) {
        // get the original attribute object and reprocess it -
        var oTemplate = CS.find(aTemplateGridData, {id: oOldProcessedTemplate.id});
        //aGridViewData[iIndex] = _preProcessTemplateDataForGridView([oTemplate])[0];
        aGridViewData[iIndex] = GridViewStore.getProcessedGridViewData(GridViewContexts.TEMPLATE, [oTemplate])[0];
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

  /*****************
   * PUBLIC API's
   * **************/
  return {
    createTemplate: function () {
      _createTemplate();
    },

    deleteTemplate: function (selectedIds) {
      _deleteTemplate(selectedIds);
    },

    setUpTemplateConfigGridView: function () {
      _setUpTemplateConfigGridView();
    },

    postProcessTemplateAndSave: function (oCallbackData) {
      _postProcessTemplateListAndSave(oCallbackData)
          .then(_triggerChange)
          .catch(CS.noop);
    },

    discardTemplateGridViewChanges: function (oCallbackData) {
      _discardTemplateGridViewChanges(oCallbackData);
    },

    fetchTemplateListForGridView : function () {
      _fetchTemplateListForGridView();
    },

    handleCommonConfigAttributeChanged: function (sKey,sValue) {
      var oActiveTemplate = _makeActiveTemplateDirty();
      oActiveTemplate[sKey] = sValue;
      _triggerChange();
    },

    handleTemplateHeaderVisibilityToggled: function (sType, sContext) {
      _handleTemplateHeaderVisibilityToggled(sType, sContext);
    },

    handleCustomTemplateSelectedEntitiesChanged: function (aSelectedItems, sContext) {
      _handleCustomTemplateSelectedEntitiesChanged(aSelectedItems, sContext);
    },

    handleSelectedEntityRemoved: function (sContext, sId) {
      _handleSelectedEntityRemoved(sContext, sId);
    },

    handleTemplateHeaderTabClicked: function (sTabId) {
      _handleTemplateHeaderTabClicked(sTabId);
    },

    handleTabIconChanged: function (sTabId, sIcon) {
      _handleTabIconChanged(sTabId, sIcon);
    },

    handleTemplateSectionAdded: function (sSectionId) {
      _handleTemplateSectionAdded(sSectionId);
    },

    handleTemplateConfigDialogButtonClicked: function (sButtonId) {
      _handleTemplateConfigDialogButtonClicked(sButtonId);
    },

  }
})();

MicroEvent.mixin(TemplateStore);

export default TemplateStore;
