
import CS from '../../../../../../libraries/cs';

import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import { getTranslations as getTranslation } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import UniqueIdentifierGenerator from '../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import Exception from '../../../../../../libraries/exceptionhandling/exception.js';
import {TagRequestMapping as oTagRequestMapping, LanguageTaxonomyRequestMapping as oLanguageTaxonomyRequestMapping} from '../../tack/setting-screen-request-mapping';
import SettingScreenProps from './../model/setting-screen-props';
import TagProps from './../model/tag-config-view-props';
import SettingUtils from './../helper/setting-utils';
import MockTagTypes from './../../../../../../commonmodule/tack/mock-data-for-tag-types';
import GridViewContexts from '../.././../../../../commonmodule/tack/grid-view-contexts';
import MockTagColors from './../../tack/mock/mock-data-for-tag-colors';
import TagConfigGridViewData from './../../tack/mock/mock-data-for-tag-config-grid-view';
import TagTypeConstants from './../../../../../../commonmodule/tack/tag-type-constants';
import ConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import EntityList from '../../../../../../commonmodule/tack/entities-list.js';
import assetTypes from '../../tack/coverflow-asset-type-list';
import ConfigPropertyTypeDictionary from '../../tack/settinglayouttack/config-module-data-model-property-group-type-dictionary';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import ExceptionLogger from '../../../../../../libraries/exceptionhandling/exception-logger';
import GridViewStore from '../../../../../../viewlibraries/contextualgridview/store/grid-view-store';
import SettingScreenAppData from "../model/setting-screen-app-data";
import GridViewColumnIdConstants
  from "../../../../../../viewlibraries/contextualgridview/tack/grid-view-column-id-constants";

var TagStore = (function() {

    var _triggerChange = function() {
        TagStore.trigger('tag-changed');
    };

    var successFetchTagListCallback = function(oResponse) {
        var aTagList = SettingUtils.getAppData().getTagList();
        var oRootNode = CS.find(aTagList, {
            'id': SettingUtils.getTreeRootId()
        });
        oRootNode.children = oResponse.success.children;
        SettingUtils.getAppData().createTagMap(oResponse.success.children);
        _setActiveTag(oRootNode);
        _triggerChange();
    };

    var failureFetchTagListCallback = function(oResponse) {
        SettingUtils.failureCallback(oResponse, "failureFetchTagListCallback", getTranslation());
    };

    var successFetchTagTypeListCallback = function(oResponse) {
        var oAppData = SettingUtils.getAppData();
        var aTagTypes = oResponse.success;

        //for translation
        var aCloneTagTypes = CS.cloneDeep(aTagTypes);
        let oMockTagTypes = new MockTagTypes();
        CS.forEach(aCloneTagTypes, function(oTagType) {
            var oCustomType = CS.find(oMockTagTypes, {
                id: oTagType.id
            });
            if (!CS.isEmpty(oCustomType)) {
                oTagType.label = CS.getLabelOrCode(oCustomType);
            }
        });

        CS.remove(aCloneTagTypes, function(oValue) {
            return oValue.id == TagTypeConstants.CUSTOM_TAG_TYPE;
        });

        oAppData.setTagTypeList(aCloneTagTypes);
        _triggerChange();
    };

    var failureFetchTagTypeListCallback = function(oResponse) {
        SettingUtils.failureCallback(oResponse, "failureFetchTagTypeListCallback", getTranslation());
    };

    var successRemoveTagCallback = function(oResponse) {
        if (!CS.isEmpty(oResponse.failure.devExceptionDetails)) {
            SettingUtils.failureCallback(oResponse, "failureRemoveTagCallback", getTranslation());
        }
      let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.TAG);

      let oExtraData = {
        tagTypeList: SettingScreenAppData.getTagTypeList(),
        linkedMasterTagCallback: _processLinkedMasterTagsForGrid
      };
      if (oResponse.success.length) {
            var aDeletedTags = oResponse.success;
            var aTagGridData = TagProps.getTagGridData();
            var aGridViewData = oGridViewPropsByContext.getGridViewData();
            var oSkeleton = oGridViewPropsByContext.getGridViewSkeleton();
            var iDeletedParentTagCount = 0;
            CS.forEach(aDeletedTags, function(sDeletedId) {
                CS.remove(aTagGridData, function(oTag) {
                    let sParentId = oTag.id;
                    if (sParentId === sDeletedId) {
                        iDeletedParentTagCount++;
                        return true;
                    }
                    if (!CS.isEmpty(oTag.children)) {
                        var aRemovedMasterTags = CS.remove(oTag.children, {
                            id: sDeletedId
                        });
                        if (!CS.isEmpty(aRemovedMasterTags)) {
                            /** if tag child is deleted then update parent tag to update dependancy like default value **/
                            let oParentGridDataWithChildren = GridViewStore.getProcessedGridViewData(GridViewContexts.TAG, [oTag],
                                "", {}, oExtraData)[0];
                            let iIndex = CS.findIndex(aGridViewData, {
                                id: sParentId
                            });
                            aGridViewData[iIndex] = oParentGridDataWithChildren;
                            _resetActionItemsForChildren(oParentGridDataWithChildren.children);
                        }
                    }
                });
                CS.remove(aGridViewData, function(oTag) {
                    if (oTag.id == sDeletedId) {
                        return true;
                    }
                    if (!CS.isEmpty(oTag.children)) {
                        _resetActionItemsForChildren(oTag.children);
                    }
                });
                CS.remove(oSkeleton.selectedContentIds, function(oSelectedId) {
                    return oSelectedId == sDeletedId;
                });

            });

            alertify.success(SettingUtils.getDecodedTranslation(getTranslation().ENTITY_DELETED_SUCCESSFULLY,{entity : getTranslation().TAGS}));
            var iTotalNoOfTags = oGridViewPropsByContext.getGridViewTotalItems();
            iTotalNoOfTags = iTotalNoOfTags - iDeletedParentTagCount;
            oGridViewPropsByContext.setGridViewTotalItems(iTotalNoOfTags);
            var iNestedCount = SettingUtils.calculateNestedContentCount(aTagGridData);
            oGridViewPropsByContext.setGridViewTotalNestedItems(iNestedCount);
        oGridViewPropsByContext.setIsGridDataDirty(false);
        }
    };

    var handleDeleteTagFailure = function(List, aTagList, oActiveTag) {
        var aTagAlreadyDeleted = [];
        var aUnhandledTag = [];
        CS.forEach(List, function(oItem) {
            if (oItem.key == "TagNotFoundException" || oItem.key == "ParentTagNotFoundException") {

                if (oActiveTag.id && oActiveTag.id == oItem.itemId) {
                    var oDummyNull = {};
                    TagProps.setActiveTag(oDummyNull);
                }

                SettingUtils.removeNodeById(aTagList, oItem.itemId);
            }
        });

        if (aTagAlreadyDeleted.length > 0) {
            var sTagAlreadyDeleted = aTagAlreadyDeleted.join(',');
            alertify.error(Exception.getCustomMessage("Attribute_already_deleted", getTranslation(), sTagAlreadyDeleted));
        }
        if (aUnhandledTag.length > 0) {
            var sUnhandledTag = aUnhandledTag.join(',');
            alertify.error(Exception.getCustomMessage("Unhandled_Tag", getTranslation(), sUnhandledTag));
        }
    };

    // TODO: Exception not handled
    var failureRemoveTagCallback = function(oCallback, oResponse) {
        if (!CS.isEmpty(oResponse.failure)) {
            let configError = CS.reduce(oResponse.failure.exceptionDetails, (isConfigError, error) => {
                if (error.key === "EntityConfigurationDependencyException") {
                  isConfigError = true;
                }
                return isConfigError;
              }, false);
              if (configError) {
                if (oCallback && oCallback.functionToExecute) {
                  oCallback.functionToExecute();
                  return;
                }
              }
            if (oResponse.errorCode == "com.cs.config.interactor.incoming.InteractorException-com.cs.config.interactor.outgoing.database.NoTagDeletedException") {
                var aTagList = SettingUtils.getAppData().getTagList();
                var oActiveTag = _getActiveTag();

                handleDeleteTagFailure(oResponse.failure.exceptionDetails, aTagList, oActiveTag);
            } else {
                SettingUtils.failureCallback(oResponse, "failureRemoveTagCallback", getTranslation());
            }
        } else {
            SettingUtils.failureCallback(oResponse, "failureRemoveTagCallback", getTranslation());
        }

        _triggerChange();

    };

    var successSaveChangedTagDataCallback = function(oCallback, oResponse) {
        var oActiveTag = _getActiveTag();
        var bIsParentCreated = false;

        var oTagFromServer = oResponse.success;


        if (oActiveTag.isCreated) {
            bIsParentCreated = true;
        }

        delete oActiveTag.isCreated;
        delete oActiveTag.clonedObject;
        delete oActiveTag.isDirty;

        CS.assign(oActiveTag, oTagFromServer);

        if (oCallback.functionToExecute) {
            oCallback.functionToExecute(bIsParentCreated);
        }

        alertify.success(getTranslation().TAG_SUCCESSFULLY_SAVED);

        _triggerChange();
    };

    var failureSaveChangedTagDataCallback = function(oResponse) {
        if (!CS.isEmpty(oResponse.failure)) {

            var sTagName = _removeTagFromMasterOnFailure(oResponse);

            alertify.error("[" + sTagName + "] " + getTranslation().ERROR_ALREADY_DELETED, 0);
        } else {
            SettingUtils.failureCallback(oResponse, "failureSaveChangedTagDataCallback", getTranslation());
        }

        _triggerChange();
    };

    var _removeTagFromMasterOnFailure = function(oResponse, oSelectedTagValue) {
        var aTagList = SettingUtils.getAppData().getTagList();
        var oTagValueList = TagProps.getTagValueList();
        var oActiveTag = oSelectedTagValue || _getActiveTag();

        if (!CS.isEmpty(CS.find(oResponse.failure.exceptionDetails, {
                key: "TagNotFoundException"
            })) && oActiveTag) {

            var sAttributeName = oTagValueList[oActiveTag.id].label;
            SettingUtils.removeNodeById(aTagList, oActiveTag.id);
            delete oTagValueList[oActiveTag.id];
            TagProps.setActiveTag({});
            return sAttributeName;
        }
    };

    var _createDummyTagNode = function(sParentId) {

        var oAppData = SettingUtils.getAppData();
        var sTagTypeId = null;
        var aTagValues = [];
        if (sParentId == SettingUtils.getTreeRootId()) {
            var oTagType = CS.find(oAppData.getTagTypeList(), {
                id: TagTypeConstants.YES_NEUTRAL_TAG_TYPE
            });
            if (!CS.isEmpty(oTagType)) {
                sTagTypeId = oTagType.id;
                aTagValues = CS.cloneDeep(oTagType.tagValues);
            }
        }

        let sSelectedTreeItem = SettingScreenProps.screen.getSelectedLeftNavigationTreeItem();
        let sSelectedTreeItemParent = SettingScreenProps.screen.getSelectedLeftNavigationTreeParentId();

        let oSelectedItem = SettingUtils.getSelectedTreeItemById(sSelectedTreeItem, sSelectedTreeItemParent);
        let aTypes = oSelectedItem.types;
        let bIsMultiSelect = (aTypes[0] === TagTypeConstants.YES_NEUTRAL_TAG_TYPE);

        return {
            id: UniqueIdentifierGenerator.generateUUID(),
            label: UniqueIdentifierGenerator.generateUntitledName(),
            description: "",
            tooltip: "",
            defaultValue: {
                type: "com.cs.core.config.interactor.entity.tag.Tag"
            },
            isMandatory: false,
            placeholder: "",
            icon: "",
            klass: null,
            linkedMasterTagId: null,
            type: "com.cs.core.config.interactor.entity.tag.Tag",
            isCreated: true,
            tagType: aTypes[0],
            tagValues: aTagValues,
            color: "", //Object.keys(oMockTagColors)[0],
            isMultiselect: bIsMultiSelect,
            isDimensional: false,
            code: "",
            parentId: sParentId,
            isFilterable: false,
            availability: [],
            isGridEditable: false,
            isVersionable: true,
        };
    };

    var _discardUnsavedTag = function(oCallback) {
        var oUnsavedTag = _getActiveTag();
        var aTagList = SettingUtils.getAppData().getTagList();
        var oTagValueList = TagProps.getTagValueList();

        if (oUnsavedTag.isCreated) {
            delete oTagValueList[oUnsavedTag.id];
            SettingUtils.removeNodeById(aTagList, oUnsavedTag.id);
            _setActiveTag({});
            alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
        } else if (oUnsavedTag.isDirty) {
            delete oUnsavedTag.isDirty;
            delete oUnsavedTag.clonedObject;
            alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
        } else {
            alertify.message(getTranslation().NO_CHANGE_FOUND_TO_DISCARD);
        }

        if (oCallback.functionToExecute) {
            oCallback.functionToExecute();
        }

    };

    var _putTagNode = function(oTag, sParentId, bAlreadyCreated) {
        if (sParentId == SettingUtils.getTreeRootId()) {
            sParentId = null;
        }
        if (sParentId) {
            oTag.parent = {
                id: sParentId
            };
        }

        var oCodeToVerifyUniqueness = {
            id: oTag.code,
            entityType: ConfigEntityTypeDictionary.ENTITY_TYPE_TAG
        };

        var oCallbackDataForUniqueness = {};
        oCallbackDataForUniqueness.functionToExecute = function() {
            delete oTag.isCreated;
            _createTagCall(oTag, sParentId, bAlreadyCreated);
        };
        var sURL = oTagRequestMapping.CheckEntityCode;
        SettingUtils.checkForUniquenessOfCodePropertyAndSendAjaxCall(oCodeToVerifyUniqueness, sURL, oCallbackDataForUniqueness);
    };

    var _createTagCall = function(oTag, sParentId, bAlreadyCreated) {
        var fSuccessHandler = bAlreadyCreated ? successSaveCreatedTag.bind(this, sParentId) : successCreateTagCallback.bind(this, sParentId);
        SettingUtils.csPutRequest(oTagRequestMapping.CreateTag, {}, oTag, fSuccessHandler, failureSaveTagsInBulk);
    };

    var successSaveCreatedTag = function(sParentId, oResponse) {
        oResponse.success = [oResponse.success];
        _incrementTotalMasterTagCount(sParentId);
      let oGridViewProps = GridViewStore.getGridViewPropsByContext(GridViewContexts.AUTHORIZATION_MAPPING);
      let iNewNestedCount = oGridViewProps.getGridViewTotalNestedItems() + 1;
      oGridViewProps.setGridViewTotalNestedItems(iNewNestedCount);
        successSaveTagsInBulk({}, oResponse);
    };

    var _createTagNode = function(sParentId) {
        var sParentId = sParentId || SettingUtils.getTreeRootId();
        var oDummyTagNode = _createDummyTagNode(sParentId);
        _setActiveTag(oDummyTagNode);
        _triggerChange();
    };

    var _fetchTagList = function(sContext) {
        var oParameters = {};
        oParameters.id = SettingUtils.getTreeRootId();
        oParameters.mode = sContext;
        SettingUtils.csGetRequest(oTagRequestMapping.GetTag, oParameters, successFetchTagListCallback, failureFetchTagListCallback);
    };

    let _setUpTagConfigGridView = function() {
        let oTagConfigGridViewSkeleton = SettingUtils.getGridSkeletonForTag();
        let sContext = GridViewContexts.TAG;
      GridViewStore.createGridViewPropsByContext(sContext, {skeleton: oTagConfigGridViewSkeleton});
      _fetchTagListForGridView(sContext);

    };

    var _fetchTagListForGridView = function(sContext) {
        let oGridDataToFetchList = GridViewStore.getPostDataToFetchList(GridViewContexts.TAG);
        let sSelectedLeftTreeNode = SettingScreenProps.screen.getSelectedLeftNavigationTreeItem();
        let sSelectedParentId = SettingScreenProps.screen.getSelectedLeftNavigationTreeParentId();
        let oSelectedLeftTreeNode = SettingUtils.getSelectedTreeItemById(sSelectedLeftTreeNode, sSelectedParentId);
        oGridDataToFetchList.types = oSelectedLeftTreeNode.types;
       SettingUtils.csPostRequest(oTagRequestMapping.BulkGetTag, {}, oGridDataToFetchList, successFetchTagListForGridView, failureFetchTagListForGridView);
    };

    var successFetchTagListForGridView = function(oResponse) {
        var oResponseData = oResponse.success;
        let aTagList = oResponseData.tagsList;
        TagProps.setReferencedTags(oResponseData.referencedTags);
        TagProps.setTagGridData(aTagList);
      let oExtraData = {
        tagTypeList: SettingScreenAppData.getTagTypeList(),
        linkedMasterTagCallback: _processLinkedMasterTagsForGrid
      };
      GridViewStore.preProcessDataForGridView(GridViewContexts.TAG, oResponseData.tagsList, oResponseData.count, {}, "", oExtraData);
      _triggerChange();
    };

    var failureFetchTagListForGridView = function(oResponse) {
        ExceptionLogger.log(oResponse);
        successFetchTagListForGridView({
            success: {
                tagList: TagConfigGridViewData
            }
        });
        _triggerChange();
    };

    var _getMSSObject = function(aItems, aSelectedItems, sContext, bSingleSelect, bDisabled, sLabel, bDisabledCross) {
        return {
            disabled: bDisabled,
            label: sLabel,
            items: aItems,
            selectedItems: aSelectedItems,
            singleSelect: bSingleSelect,
            context: sContext,
            disableCross: bDisabledCross
        }
    };

    var _processLinkedMasterTagsForGrid = function(sParentId, oProcessedTag, oTag) {
        var bIsDisabled = (oTag.tagType === TagTypeConstants.TAG_TYPE_MASTER || oTag.tagType === TagTypeConstants.TAG_TYPE_BOOLEAN);
        if (bIsDisabled) {
            oProcessedTag.properties["linkedMasterTagId"] = {};
            return;
        }
        var oMasterList = TagProps.getReferencedTags();
        var aTagGridData = TagProps.getTagGridData();
        let oParent = {};
        if (sParentId) {
            oParent = CS.find(aTagGridData, {
                id: sParentId
            }) || {};
            if (!oParent.linkedMasterTagId) {
                return;
            }
            var oParentMasterList = oMasterList[oParent.linkedMasterTagId] || {};
            oMasterList = oParentMasterList.children || {};
        }
        var aMasterListItems = CS.map(oMasterList, function(oItem, sKey) {
            return {
                id: oItem.id,
                label: oItem.label,
                icon: oItem.icon,
                color: oItem.color
            }
        });

        var aSelectedItems = oTag.linkedMasterTagId ? [oTag.linkedMasterTagId] : [];
        let bDontShowMSS = CS.isEmpty(oParent.linkedMasterTagId);
        if (!sParentId) {
            bDontShowMSS = false;
        }

        let aRejectionList = [];
        //TODO: Temporary code lazy list should be fetched from backend with filter data
        try {
            CS.forEach(oParent.children, (oChild) => {
                if (oChild.linkedMasterTagId) {
                    aRejectionList.push(oChild.linkedMasterTagId);
                }
            });
        } catch (oException) {
            ExceptionLogger.error(oException);
        }

        oProcessedTag.properties["linkedMasterTagId"] = _getLinkMasterTagMSSObject(aMasterListItems, aSelectedItems, "gridTagLinkedMasterTag", true, bDontShowMSS, "", false, oParent.linkedMasterTagId);
        oProcessedTag.properties["linkedMasterTagId"].value = aSelectedItems;
        oProcessedTag.properties["linkedMasterTagId"].rejectedList = aRejectionList;
        if (!sParentId) {
            if (oTag.linkedMasterTagId) {
                let aSelectedMasterChildren = [];
                //TODO: Temporary code list should be fetched from backend with filter data
                try {
                    CS.forEach(oTag.children, (oChild) => {
                        if (oChild.linkedMasterTagId) {
                            aSelectedMasterChildren.push(oChild.linkedMasterTagId);
                        }
                    });
                } catch (oException) {
                    ExceptionLogger.error(oException);
                }
                oProcessedTag.properties["linkedMasterTagId"].excludedMasterChildren = aSelectedMasterChildren;
                oProcessedTag.properties["linkedMasterTagId"].additionalBtnReqResInfo = _getReqResModelForMasterTag(oTag.linkedMasterTagId);
            }
        } else {
            //TODO: Temporary code lazy list should be fetched from backend with filter data
            /** This code block is only for create */
            try {
              let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.TAG);
              let aGridViewData = oGridViewPropsByContext.getGridViewData();
                var oParentGridData = CS.find(aGridViewData, {
                    id: sParentId
                });
                if (oTag.linkedMasterTagId) {
                    oParentGridData.properties["linkedMasterTagId"].excludedMasterChildren = oParentGridData.properties["linkedMasterTagId"].excludedMasterChildren || [];
                    oParentGridData.properties["linkedMasterTagId"].excludedMasterChildren.push(oTag.linkedMasterTagId);
                }
            } catch (oException) {
                ExceptionLogger.error(oException);
            }
        }
        oProcessedTag.properties["linkedMasterTagId"].contentId = oTag.id;
    };

    let _getReqResModelForMasterTag = (sParentId) => {
        let oMSSRequestResponseObj = {};
        if (!CS.isEmpty(sParentId)) {
            oMSSRequestResponseObj.requestType = "configData";
            oMSSRequestResponseObj.entityName = "tagValues";
            oMSSRequestResponseObj.customRequestModel = {
                tagGroupId: sParentId
            };
        } else {
            oMSSRequestResponseObj.requestType = "configData";
            oMSSRequestResponseObj.entityName = "tags";
            oMSSRequestResponseObj.types = [TagTypeConstants.TAG_TYPE_MASTER];
        }
        return oMSSRequestResponseObj;
    };

    let _getLinkMasterTagMSSObject = function(aTagList, aSelectedItems, sContext, bSingleSelect, bDisabled, sLabel, bDisabledCross, sParentId) {
        let oMSSRequestResponseObj = _getReqResModelForMasterTag(sParentId);

        var oMasterList = TagProps.getReferencedTags();
        return {
            disabled: bDisabled,
            label: sLabel,
            items: aTagList,
            selectedItems: aSelectedItems,
            singleSelect: bSingleSelect,
            cannotRemove: bDisabledCross,
            context: sContext,
            referencedData: oMasterList,
            requestResponseInfo: oMSSRequestResponseObj
        }
    };

    var _sortTagSequence = function(aChildren, aSequence) {
        var aClonedChildren = CS.clone(aChildren);
        var aSequencedTags = [];
        CS.forEach(aSequence, function(sId) {
            var aTags = CS.remove(aClonedChildren, {
                id: sId
            });
            if (!CS.isEmpty(aTags)) {
                aSequencedTags.push(aTags[0]);
            }
        });

        aSequencedTags.push.apply(aSequencedTags, aClonedChildren);

        return aSequencedTags;
    };

    let _getFilterTagTypeListAccordingToGroup = function(aTagTypeList) {
        let sSelectedItem = SettingScreenProps.screen.getSelectedLeftNavigationTreeItem();

        switch (sSelectedItem) {
            case ConfigPropertyTypeDictionary.LOV:
                aTagTypeList = CS.filter(aTagTypeList, function(oType) {
                    return oType.id === TagTypeConstants.YES_NEUTRAL_NO_TAG_TYPE ||
                        oType.id === TagTypeConstants.YES_NEUTRAL_TAG_TYPE ||
                        oType.id === TagTypeConstants.RULER_TAG_TYPE ||
                        oType.id === TagTypeConstants.RANGE_TAG_TYPE
                });
                break;

            case ConfigPropertyTypeDictionary.STATUS:
                aTagTypeList = CS.filter(aTagTypeList, function(oType) {
                    return oType.id === TagTypeConstants.STATUS_TAG_TYPE ||
                        oType.id === TagTypeConstants.LIFECYCLE_STATUS_TAG_TYPE ||
                        oType.id === TagTypeConstants.LISTING_STATUS_TAG_TYPE
                });
                break;
        }

        return CS.sortBy(aTagTypeList, 'label');
    };

    var _preProcessTagDataForGridView = function(aTagList, sPath) {

        var oGridSkeleton = {};
        var oGridViewVisualData = SettingScreenProps.screen.getGridViewVisualData();
        var aTagGridData = TagProps.getTagGridData();
        var aGridViewData = [];
        var iTagCount = aTagList.length;
        CS.forEach(aTagList, function(oTag, iIndex) {
            var oProcessedTag = {};
            oProcessedTag.id = oTag.id;
            if (oGridViewVisualData[oTag.id]) {
            } else {
                oGridViewVisualData[oTag.id] = {
                    isExpanded: false
                }
            }
            oProcessedTag.children = [];
            var sPathToAdd = sPath;
            if (!sPathToAdd && oTag.parent) {
                sPathToAdd = oTag.parent.id;
            }
            oProcessedTag.pathToRoot = sPathToAdd ? sPathToAdd + SettingUtils.getSplitter() + oTag.id : oTag.id;
            oProcessedTag.metadata = {};
            if (oTag.isCreated) {
                oProcessedTag.metadata.isCreated = true;
            }
            if (oTag.tagType && oTag.tagType === TagTypeConstants.TAG_TYPE_BOOLEAN) {
                oProcessedTag.actionItemsToShow = ["manageEntity", "delete"];
            } else {
                if (!sPathToAdd) {
                    oProcessedTag.actionItemsToShow = ["manageEntity","delete"];
                    oProcessedTag.actionItemsToShow.push("create");
                } else {
                    oProcessedTag.actionItemsToShow = ["deleteValues"];
                    if (iIndex != 0) {
                        oProcessedTag.actionItemsToShow.push("moveUp");
                    } else {
                        //To show empty space
                        oProcessedTag.actionItemsToShow.push("dummy");
                    }

                    if (iIndex < (iTagCount - 1)) {
                        oProcessedTag.actionItemsToShow.push("moveDown");
                    }
                }
            }
            oProcessedTag.properties = {};

            CS.forEach(oGridSkeleton.fixedColumns, function(oColumn) {
                switch (oColumn.id) {
                    case "label":
                        if (oTag.hasOwnProperty(oColumn.id)) {
                            oProcessedTag.properties[oColumn.id] = {
                                value: oTag[oColumn.id],
                                bIsMultiLine: false,
                                showTooltip: true,
                                placeholder: oTag["code"]
                            };
                        }
                        break;
                    case "code":
                        if (oTag.hasOwnProperty(oColumn.id)) {
                            oProcessedTag.properties[oColumn.id] = {
                                value: oTag[oColumn.id],
                                isDisabled: true,
                                bIsMultiLine: false
                            };
                        }
                        break;
                    default:
                        if (oTag.hasOwnProperty(oColumn.id)) {
                            oProcessedTag.properties[oColumn.id] = {
                                value: oTag[oColumn.id]
                            };
                        }
                }
            });

            CS.forEach(oGridSkeleton.scrollableColumns, function(oColumn) {

                if (sPathToAdd && !CS.includes(["label", "klass", "linkedMasterTagId", "icon", "color", "imageResolution", "imageExtension"], oColumn.id)) {
                    return;
                }
                switch (oColumn.id) {

                    /** IMPORTANT : if value is an array or object, make sure to use cloneDeep to avoid passing by reference*/

                    case "tagType":
                        if (oTag.hasOwnProperty(oColumn.id)) {
                            // if column is 'type' then prepare mss object
                            var aTagTypeList = SettingUtils.getAppData().getTagTypeList();
                            var sType = oTag[oColumn.id];
                            var aSelectedList = sType ? [sType] : [];
                            var bIsDisabled = (sType === TagTypeConstants.TAG_TYPE_MASTER || sType === TagTypeConstants.TAG_TYPE_BOOLEAN);
                            let aFilteredTypesList = _getFilterTagTypeListAccordingToGroup(aTagTypeList);
                            oProcessedTag.properties[oColumn.id] = _getMSSObject(aFilteredTypesList, aSelectedList, "gridTagType", true, bIsDisabled, "", true);
                            oProcessedTag.properties[oColumn.id].value = [sType];
                        }
                        break;

                    case "defaultValue":
                        if (oTag.hasOwnProperty(oColumn.id)) {
                            if (oTag.tagType === TagTypeConstants.YES_NEUTRAL_TAG_TYPE) {
                                var aSelectedItems = oTag.defaultValue && oTag.defaultValue.id ? [oTag.defaultValue.id] : [];
                                oProcessedTag.properties[oColumn.id] = _getMSSObject(oTag.children, aSelectedItems, "gridTagDefaultValue", true, false, "", false);
                                oProcessedTag.properties[oColumn.id].value = aSelectedItems;
                            } else {
                                oProcessedTag.properties[oColumn.id] = {}; //default value disabled for all tag types except YN
                            }
                        }
                        break;

                    case "color":
                        if (oTag.hasOwnProperty(oColumn.id)) {
                            var aTagColors = SettingUtils.getAppData().getTagColors();
                            var aTagColorItems = CS.map(aTagColors, function(sColor, sKey) {
                                return {
                                    id: sKey,
                                    label: sColor,
                                    color: sColor
                                }
                            });
                            var aSelectedItems = oTag.color ? [oTag.color] : [];
                            oProcessedTag.properties[oColumn.id] = _getMSSObject(aTagColorItems, aSelectedItems, "gridTagColor", true, false, "", false);
                            oProcessedTag.properties[oColumn.id].value = aSelectedItems;
                            oProcessedTag.properties[oColumn.id].showColor = true;
                        }
                        break;

                    case "linkedMasterTagId":
                        if (oTag.hasOwnProperty(oColumn.id)) {
                            _processLinkedMasterTagsForGrid(sPathToAdd, oProcessedTag, oTag);
                        }
                        break;

                    case "isMultiselect":
                        if (oTag.hasOwnProperty(oColumn.id)) {
                            if (oTag.tagType === TagTypeConstants.YES_NEUTRAL_TAG_TYPE) {
                                oProcessedTag.properties[oColumn.id] = {
                                    value: oTag[oColumn.id]
                                }
                            } else {
                                oProcessedTag.properties[oColumn.id] = {}
                            }
                          if (oTag.id === "resolutiontag" || oTag.id === "imageextensiontag") {
                            oProcessedTag.properties[oColumn.id].isDisabled = true;
                          }
                        }
                        break;

                    case "isGridEditable":
                        if (oTag.hasOwnProperty(oColumn.id)) {
                            oProcessedTag.properties[oColumn.id] = {
                                value: oTag[oColumn.id]
                            }
                        }
                        break;

                    case "klass":
                    case "imageResolution":
                    case "imageExtension":
                        if (oTag.hasOwnProperty(oColumn.id)) {
                            oProcessedTag.properties[oColumn.id] = {
                                value: oTag[oColumn.id],
                                isDisabled: true,
                            };
                        }
                        break;

                    case "isFilterable":
                        if (oTag.hasOwnProperty(oColumn.id)) {
                            oProcessedTag.properties[oColumn.id] = {
                                value: oTag[oColumn.id]
                            }
                        }
                        break;

                    case "availability":
                        if (oTag.hasOwnProperty(oColumn.id)) {
                            oProcessedTag.properties[oColumn.id] = _getAvailableTypeMSSObject(oTag);
                            oProcessedTag.properties[oColumn.id].value = oProcessedTag.properties[oColumn.id].selectedItems;
                        }
                        break;

                  case "isVersionable":
                    if (oTag.hasOwnProperty(oColumn.id)) {
                      oProcessedTag.properties[oColumn.id] = {
                        value: oTag[oColumn.id],
                      };
                    }

                    default:
                        if (oTag.hasOwnProperty(oColumn.id)) {
                            oProcessedTag.properties[oColumn.id] = {
                                value: oTag[oColumn.id],
                                showTooltip: true
                            }
                        }
                }
            });

            if (oTag.tagType !== TagTypeConstants.TAG_TYPE_BOOLEAN) {
                oTag.children = _sortTagSequence(oTag.children, oTag.tagValuesSequence);
                oProcessedTag.children = _preProcessTagDataForGridView(oTag.children, oProcessedTag.pathToRoot);
            }

            aGridViewData.push(oProcessedTag);
        });

        return aGridViewData;
    };

    var _getAvailableTypeMSSObject = function(oSelectedTag) {
        var aSelectedEntityList = oSelectedTag.availability;

            var aTagMSSList = [];
            let aEntityList = EntityList();

        CS.forEach(aEntityList, function(oEntity) {
            aTagMSSList.push({
                id: oEntity.id,
                label: oEntity.label
            });
        });

        return {
            label: "",
            items: aTagMSSList,
            selectedItems: aSelectedEntityList,
            singleSelect: false,
            context: "EntityType",
            disableCross: false,
        }
    };

    var _handleGridPropertyValueChangeDependencies = function(sPropertyId, value, sPathToRoot, oContent) {
        var oTag = _getTagFromPath(sPathToRoot, oContent.id);
        if (oContent.metadata && oContent.metadata.isCreated) {
            if (oTag) {
                oTag[sPropertyId] = value;
            }
        }
        switch (sPropertyId) {
            case "tagType":
                if (value[0] === TagTypeConstants.TAG_TYPE_BOOLEAN || value[0] === TagTypeConstants.TAG_TYPE_MASTER) {
                    oContent.actionItemsToShow = ["delete"];
                    if (value[0] === TagTypeConstants.TAG_TYPE_MASTER) {
                        oContent.actionItemsToShow = ["delete", "create"];
                    }
                    oContent.children = [];
                    oContent.properties["defaultValue"] = {}; //disable default value for all tags except YN
                    oContent.properties["isMultiselect"] = {}; //disable multiselect for all tags except YN
                    oContent.properties["linkedMasterTagId"] = {}; //disable linked master list for Boolean and Master Tag
                } else if (value[0] === TagTypeConstants.YES_NEUTRAL_TAG_TYPE) {
                    if (oTag) {
                        var aSelectedItems = [];
                        oContent.properties["defaultValue"] = _getMSSObject(oTag.children, aSelectedItems, "gridTagDefaultValue", true, false, "", false);
                        oContent.properties["defaultValue"].value = aSelectedItems;

                        if (CS.isEmpty(oContent.properties["linkedMasterTagId"])) {
                            var oMasterList = SettingUtils.getAppData().getMasterList();
                            var aMasterListItems = CS.values(oMasterList);
                            var aSelectedItems = oTag.linkedMasterTagId ? [oTag.linkedMasterTagId] : [];
                            oContent.properties["linkedMasterTagId"] = _getMSSObject(aMasterListItems, aSelectedItems, "gridTagLinkedMasterTag", true, false, "", false);
                            oContent.properties["linkedMasterTagId"].value = aSelectedItems;
                        }
                        oContent.properties["isMultiselect"] = {
                            value: false
                        };
                    }
                    oContent.actionItemsToShow = ["delete", "create"];
                } else {
                    oContent.properties["defaultValue"] = {}; //disable default value for all tags except YN
                    oContent.properties["isMultiselect"] = {}; //disable multiselect for all tags except YN
                    oContent.actionItemsToShow = ["delete", "create"];
                }
                break;

            case GridViewColumnIdConstants.LINKED_MASTER_TAG_ID:
                var sLinkedMasterTag = value[0];
                oTag[sPropertyId] = sLinkedMasterTag;
                if (sLinkedMasterTag) {
                    oTag[sPropertyId] = sLinkedMasterTag;
                    var aPath = CS.split(sPathToRoot, SettingUtils.getSplitter());
                    var sParentId = aPath[1] ? aPath[0] : null;
                    _processLinkedMasterTagsForGrid(sParentId, oContent, oTag);
                }
                break;
        }
    };

    var _getTagFromPath = function(sPathToRoot, sContentId) {
        var aTagGridData = TagProps.getTagGridData();
        var aPath = CS.split(sPathToRoot, SettingUtils.getSplitter());
        if (aPath[0] != sContentId && aPath[1]) {
            var oParent = CS.find(aTagGridData, {
                id: aPath[0]
            }) || {};
            aTagGridData = oParent.children;
        }
        var oContent = CS.find(aTagGridData, {
            id: sContentId
        });
        return oContent;
    };

    var _handleGridMSSAdditionalListItemAdded = function(sContentId, aCheckedItems, sContext, oReferencedData) {
        switch (sContext) {
            case "gridTagLinkedMasterTag":
                try {
                    var aTagNodesToCreate = [];
                    CS.forEach(aCheckedItems, function(sSelectedId) {
                        let oItem = oReferencedData[sSelectedId];
                        var oDummyTagNode = _createDummyTagNode(sContentId);
                        oDummyTagNode.label = oItem.label;
                        oDummyTagNode.linkedMasterTagId = oItem.id;
                        oDummyTagNode.parent = {
                            id: sContentId
                        };
                        aTagNodesToCreate.push(oDummyTagNode);
                        delete oDummyTagNode.isCreated;
                    });
                    _putBulkTagNodes(aTagNodesToCreate, sContentId);
                } catch (oException) {
                    ExceptionLogger.error(oException);
                }
                break;
        }
    };

    var _putBulkTagNodes = function(aTagNodes, sParentId) {
        if (CS.isEmpty(aTagNodes)) {
            return;
        }
        SettingUtils.csPutRequest(oTagRequestMapping.BulkCreateTag, {}, aTagNodes, successBulkCreateTags.bind(this, sParentId), failureSaveTagsInBulk);
    };

    var successBulkCreateTags = function(sParentId, oResponse) {
        let oResponseData = oResponse.success;
        let oReferencedTags = TagProps.getReferencedTags();
        CS.assign(oReferencedTags, oResponseData.referencedTags);
        TagProps.setReferencedTags(oReferencedTags);
        var aCreatedTags = oResponse.success.tags || [];
        var aTagGridData = TagProps.getTagGridData();
      let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.TAG);
      var aGridViewData = oGridViewPropsByContext.getGridViewData();
        var oGridViewVisualData = oGridViewPropsByContext.getGridViewVisualData();
        var oParentMaster = CS.find(aTagGridData, {
            id: sParentId
        });
        CS.forEach(aCreatedTags, function(oCreatedTag) {
            oCreatedTag.parent = {
                id: sParentId
            };
          let oExtraData = {
            tagTypeList: SettingScreenAppData.getTagTypeList(),
            linkedMasterTagCallback: _processLinkedMasterTagsForGrid
          };
            var aProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.TAG, [oCreatedTag],
                "", {}, oExtraData);
            oParentMaster.children.push(oCreatedTag);
            var oParentGridData = CS.find(aGridViewData, {
                id: sParentId
            });
            oParentGridData.children = oParentGridData.children.concat(aProcessedGridViewData);
            oGridViewVisualData[sParentId].isExpanded = true;
        });
        let iNewNestedCount = oGridViewPropsByContext.getGridViewTotalNestedItems() + aCreatedTags.length;
        oGridViewPropsByContext.setGridViewTotalNestedItems(iNewNestedCount);
        _triggerChange();
    };

    var _postProcessTagListAndSave = function(oCallbackData) {
        var aTagGridData = TagProps.getTagGridData();
        var aTagListToSave = [];
        let bSafeToSave = GridViewStore.processGridDataToSave(aTagListToSave, GridViewContexts.TAG, aTagGridData);
      bSafeToSave &&  SettingUtils.csPostRequest(oTagRequestMapping.SaveTag, {}, aTagListToSave, successSaveTagsInBulk.bind(this, oCallbackData), failureSaveTagsInBulk);
    };

    var _safeToSave = function(aTagListToSave) {
        var bSafeToSave = true;

        if (CS.isEmpty(aTagListToSave)) {
            alertify.message(getTranslation().NOTHING_CHANGED_TO_SAVE);
            return false;
        }

        CS.forEach(aTagListToSave, function(oTag) {
            if (CS.trim(oTag.label) == "") {
                bSafeToSave = false;
                alertify.message(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
                return false;
            }
        });

        return bSafeToSave;
    };

    var _saveTagsInBulk = function(aTagListToSave, oCallbackData) {

        if (_safeToSave(aTagListToSave)) {

            SettingUtils.csPostRequest(oTagRequestMapping.SaveTag, {}, aTagListToSave, successSaveTagsInBulk.bind(this, oCallbackData), failureSaveTagsInBulk);
        }

    };

    var successSaveTagsInBulk = function(oCallbackData, oResponse) {
        let aTagsList = oResponse.success.tagsList;
      let oExtraData = {
        tagTypeList: SettingScreenAppData.getTagTypeList(),
        linkedMasterTagCallback: _processLinkedMasterTagsForGrid
      };
        var aProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.TAG, aTagsList,
            "", {}, oExtraData);
        var aTagGridData = TagProps.getTagGridData(); //saved Unprocessed data
      let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.TAG);
      let aGridViewData = oGridViewPropsByContext.getGridViewData(); //saved Processed data

        CS.forEach(aTagsList, function(oTag) { //add the successfully saved attributes into stored data:
            var aTagGridDataForTag = aTagGridData;
            var aGridViewDataForTag = aGridViewData;
            if (oTag.parent) {
                var oParentMaster = CS.find(aTagGridData, {
                    id: oTag.parent.id
                });
                aTagGridDataForTag = oParentMaster.children;
                var oParentViewData = CS.find(aGridViewData, {
                    id: oTag.parent.id
                });
                aGridViewDataForTag = oParentViewData.children;
                _resetActionItemsForChildren(aGridViewDataForTag, aProcessedGridViewData); //This happens in callback also of move up and down
            }
            var iIndex = CS.findIndex(aTagGridDataForTag, {
                id: oTag.id
            });
            aTagGridDataForTag[iIndex] = oTag;

            // Processed grid view data not iterated over separately because we need the parent object from response
            var oProcessedTag = CS.find(aProcessedGridViewData, {
                id: oTag.id
            });
            var iProcessedIndex = CS.findIndex(aGridViewDataForTag, {
                id: oProcessedTag.id
            });
            aGridViewDataForTag[iProcessedIndex] = oProcessedTag;
        });
        alertify.success(getTranslation().TAG_SUCCESSFULLY_SAVED);
      oGridViewPropsByContext.setIsGridDataDirty(false);
        if (oCallbackData && oCallbackData.functionToExecute) {
            oCallbackData.functionToExecute();
        }
        _triggerChange();
    };

    var failureSaveTagsInBulk = function(oResponse) {
        SettingUtils.failureCallback(oResponse, 'failureBulkCreateAssetCallback', getTranslation());
    };

    var _incrementTotalMasterTagCount = function(sParentId) {
        if (!sParentId) {
            let oGridViewProps = GridViewStore.getGridViewPropsByContext(GridViewContexts.TAG);
            let iTotalNoOfTags = oGridViewProps.getGridViewTotalItems();
            oGridViewProps.setGridViewTotalItems(++iTotalNoOfTags);
        }
    };

    var successCreateTagCallback = function(sParentId, oResponse) {
      let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.TAG);
      var oCreatedTag = oResponse.success;
        if (sParentId) {
            oCreatedTag.parent = {
                id: sParentId
            };
        }
        var aTagGridData = TagProps.getTagGridData();
        var aGridViewData = oGridViewPropsByContext.getGridViewData();
        var oGridViewVisualData = oGridViewPropsByContext.getGridViewVisualData();
      let oExtraData = {
        tagTypeList: SettingScreenAppData.getTagTypeList(),
        linkedMasterTagCallback: _processLinkedMasterTagsForGrid
      };
        if (sParentId) {
            var oParentMaster = CS.find(aTagGridData, {
                id: sParentId
            });
            oParentMaster.children.unshift(oCreatedTag);

            let oParentGridDataWithChildren = GridViewStore.getProcessedGridViewData(GridViewContexts.TAG, [oParentMaster],
                "", {}, oExtraData)[0];
            let iIndex = CS.findIndex(aGridViewData, {
                id: sParentId
            });
            aGridViewData[iIndex] = oParentGridDataWithChildren;
            oGridViewVisualData[sParentId].isExpanded = true;
            _resetActionItemsForChildren(oParentGridDataWithChildren.children);
        } else {
            let aProcessedGridViewData = GridViewStore.getProcessedGridViewData(GridViewContexts.TAG, [oCreatedTag], "", {}, oExtraData);
            oGridViewPropsByContext.setGridViewData(aProcessedGridViewData.concat(aGridViewData));
            aTagGridData.unshift(oCreatedTag);
        }
        _incrementTotalMasterTagCount(sParentId);
        var iNestedCount = SettingUtils.calculateNestedContentCount(aTagGridData);
        oGridViewPropsByContext.setGridViewTotalNestedItems(iNestedCount);
        alertify.success(getTranslation().TAG_CREATED_SUCCESSFULLY);
        _triggerChange();
    };

    var _recursivelyDiscardDirtyTags = function (aGridViewData, aTagGridData) {
    var bShowDiscardMessage = false;
    CS.forEach(aGridViewData, function (oOldProcessedTag, iIndex) { //add the successfully saved attributes into stored data:
      var oTag = CS.find(aTagGridData, {id: oOldProcessedTag.id});
      if (oOldProcessedTag.isDirty) {

        let sPathToRoot = "";

        /** To process for tag's children & modify sPathToRoot **/
        if (oOldProcessedTag.pathToRoot && oOldProcessedTag.pathToRoot !== oTag.id) {
          sPathToRoot = oOldProcessedTag.pathToRoot;
          let aPath = CS.split(sPathToRoot, SettingUtils.getSplitter());
          aPath.splice(-1);     /** Removing tagId of the children tag, maintaining rest of the parent tagId's **/
          sPathToRoot = aPath.join(SettingUtils.getSplitter());
        }

        let oExtraData = {
          tagTypeList: SettingScreenAppData.getTagTypeList(),
          linkedMasterTagCallback: _processLinkedMasterTagsForGrid
        };
        aGridViewData[iIndex] = GridViewStore.getProcessedGridViewData(GridViewContexts.TAG, [oTag],sPathToRoot, {}, oExtraData)[0];
        bShowDiscardMessage = true;
      }
      if (!CS.isEmpty(oOldProcessedTag.children)) {
        bShowDiscardMessage = bShowDiscardMessage || _recursivelyDiscardDirtyTags(oOldProcessedTag.children, oTag.children);
      }
    });
    return bShowDiscardMessage;
  };

    var _discardTagGridViewChanges = function(oCallbackData) {
        var aTagGridData = TagProps.getTagGridData(); //saved Original(unprocessed) data
      let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.TAG);
      let aGridViewData = oGridViewPropsByContext.getGridViewData(); //saved Processed data

        var bShowDiscardMessage = _recursivelyDiscardDirtyTags(aGridViewData, aTagGridData);

        bShowDiscardMessage && alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
        oGridViewPropsByContext.setIsGridDataDirty(false);

        if (oCallbackData && oCallbackData.functionToExecute) {
            oCallbackData.functionToExecute();
        }
        _triggerChange();
    };

    var _handleGridPropertyTabPressed = function(sContentId) {
      let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.TAG);
      let aGridViewData = oGridViewPropsByContext.getGridViewData();
        let oGridViewVisualData = oGridViewPropsByContext.getGridViewVisualData();
        var aTagGridData = TagProps.getTagGridData();

        var oMasterTag = CS.find(aTagGridData, {
            id: sContentId
        });
        if (!oMasterTag || (oMasterTag && !oMasterTag.isCreated) || oMasterTag.parent) {
            return;
        }

        var iMasterIndex = CS.findIndex(aTagGridData, {
            id: sContentId
        });
        var iGridViewIndex = CS.findIndex(aGridViewData, {
            id: sContentId
        });

        var iMasterPrevious = iMasterIndex - 1;
        var iGridViewPrevious = iGridViewIndex - 1;

        var oPrevious = aTagGridData[iMasterPrevious];
        if (oPrevious.tagType === "tag_type_boolean") {
            return;
        }

        var aMasterTag = CS.remove(aTagGridData, {
            id: sContentId
        });
        CS.remove(aGridViewData, {
            id: sContentId
        });
        if (oPrevious) {
            aMasterTag[0] && (aMasterTag[0].parent = {
                id: oPrevious.id
            });
            oPrevious.children = oPrevious.children.concat(aMasterTag);
            oGridViewVisualData[oPrevious.id].isExpanded = true;
            var oGridPrevious = aGridViewData[iGridViewPrevious];
          let oExtraData = {
            tagTypeList: SettingScreenAppData.getTagTypeList(),
            linkedMasterTagCallback: _processLinkedMasterTagsForGrid
          };
            if (oGridPrevious) {
                oGridPrevious.children = oGridPrevious.children.concat(GridViewStore.getProcessedGridViewData(GridViewContexts.TAG,
                    aMasterTag, oGridPrevious.id, {}, oExtraData));
                _resetActionItemsForChildren(oGridPrevious.children);
            }
        }
        _triggerChange();
    };

    var _handleGridPropertyShiftTabPressed = function(sContentId, sPathToRoot) {
      let oGridViewPropsByContext = GridViewStore.getGridViewPropsByContext(GridViewContexts.TAG);
      let aGridViewData = oGridViewPropsByContext.getGridViewData();
        var aTagGridData = TagProps.getTagGridData();

        if (!sPathToRoot || (sPathToRoot == sContentId)) {
            return;
        }

        var aPath = CS.split(sPathToRoot, SettingUtils.getSplitter());
        var sParentId = aPath[0];

        var iMasterIndex = CS.findIndex(aTagGridData, {
            id: sParentId
        });
        var iGridViewIndex = CS.findIndex(aGridViewData, {
            id: sParentId
        });

        var iMasterNext = iMasterIndex + 1;
        var iGridViewNext = iGridViewIndex + 1;

        var oParent = aTagGridData[iMasterIndex];
        if (oParent) {
            var oMasterTag = CS.find(oParent.children, {
                id: sContentId
            });
            if (!oMasterTag || (oMasterTag && !oMasterTag.isCreated)) {
                return;
            }

            var aMasterTag = CS.remove(oParent.children, {
                id: sContentId
            });
            CS.remove(aGridViewData[iGridViewIndex].children, {
                id: sContentId
            });

            var oTag = aMasterTag[0];
            if (oTag) {
                delete oTag.parent;
                if (!oTag.tagType) {
                    var oAppData = SettingUtils.getAppData();
                    var oFirstTagType = CS.head(oAppData.getTagTypeList());
                    if (!CS.isEmpty(oFirstTagType)) {
                        oTag.tagType = oFirstTagType.id;
                        oTag.tagValues = CS.cloneDeep(oFirstTagType.tagValues);
                    }
                }
                aTagGridData.splice(iMasterNext, 0, oTag);
                //var oProcessedGridData = _preProcessTagDataForGridView(aMasterTag)[0];
              let oExtraData = {
                tagTypeList: SettingScreenAppData.getTagTypeList(),
                linkedMasterTagCallback: _processLinkedMasterTagsForGrid
              };
                var oProcessedGridData = GridViewStore.getProcessedGridViewData(GridViewContexts.TAG, aMasterTag, "", {}, oExtraData)[0];
                aGridViewData.splice(iGridViewNext, 0, oProcessedGridData);
            }
            _triggerChange();
        }
    };

    var _handleGridPropertyKeyDownEvent = function(sKey, sContentId, sPathToRoot) {
        switch (sKey) {
            case "enter":
                break;

            case "tab":
                _handleGridPropertyTabPressed(sContentId);
                break;

            case "shiftTab":
                _handleGridPropertyShiftTabPressed(sContentId, sPathToRoot);
                break;
        }
    };

    var _fetchTagTypeList = function() {
        SettingUtils.csGetRequest(oTagRequestMapping.GetAllTagTypes, {}, successFetchTagTypeListCallback, failureFetchTagTypeListCallback);
    };

    var _getActiveTag = function() {
        return TagProps.getActiveTag();
    };

    var _setActiveTag = function(oTag) {
        TagProps.setActiveTag(oTag);
    };

    var _deleteMultipleTags = function(aSavedTagIdsToDelete, oCallBack) {
        var oTagData = {};
        oTagData.ids = aSavedTagIdsToDelete;

        if (CS.isEmpty(oTagData.ids)) {
            alertify.success(getTranslation().TAG_SUCCESSFULLY_DELETED);
            _triggerChange();

        } else {
            return SettingUtils.csDeleteRequest(oTagRequestMapping.BulkDelete, {}, oTagData, successRemoveTagCallback, failureRemoveTagCallback.bind(this, oCallBack));
        }
    };

    var _saveChangedTagData = function(oCallbackData) {
        var oActiveTag = _getActiveTag();
        var aTagList = SettingUtils.getAppData().getTagList();
        var oTag = CS.cloneDeep(oActiveTag);

        if (!(oTag.isDirty || oTag.isCreated)) {
            alertify.message(getTranslation().NOTHING_CHANGED_TO_SAVE);
            return;
        }

        if (oTag.isDirty) {
            oTag = oActiveTag.clonedObject;
        }

        oTag.label = oTag.label.trim();
        if (CS.isEmpty(oTag.label)) {
            alertify.error(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
            _triggerChange();
            return;
        }

        if (oTag.id != -1 && oTag.tagType == "tag_type_custom") {
            var aTagValues = oTag.tagValues;
            var oFromTag = CS.find(aTagValues, {
                label: "From"
            });
            var oToTag = CS.find(aTagValues, {
                label: "To"
            });
            if (Number(oFromTag.relevance) >= Number(oToTag.relevance)) {
                alertify.error(getTranslation().ERROR_TAG_FROM_TO_VALIDATION);
                return;
            }
        }

        delete oTag.children;
        var sParentId = SettingUtils.getParentNodeIdByChildId(aTagList, oTag.id, '');
        if (sParentId != SettingUtils.getTreeRootId()) {
            oTag.parent = {
                id: sParentId
            };
        }

        if (oTag.isCreated) {
            delete oTag.isCreated;
            delete oTag.id;
            SettingUtils.csPutRequest(oTagRequestMapping.CreateTag, {}, oTag, successSaveChangedTagDataCallback.bind(this, oCallbackData), failureSaveChangedTagDataCallback);
        } else {
            SettingUtils.csPostRequest(oTagRequestMapping.SaveTag, {}, oTag, successSaveChangedTagDataCallback.bind(this, oCallbackData), failureSaveChangedTagDataCallback);
        }
    };

    var _handleTagDataChanged = function(oModel, sContext, sNewValue) {
        var oActiveTag = _getActiveTag();
        if (!oActiveTag.isCreated && !oActiveTag.isDirty) {
            if (oActiveTag[sContext] == sNewValue) {
                return;
            }
            SettingUtils.makeObjectDirty(oActiveTag);
        }
        var oTag = oActiveTag.clonedObject ? oActiveTag.clonedObject : oActiveTag;
        oTag[sContext] = sNewValue;
    };

    let _removeChildrenFromSelectedIds = function (aSelectedTagIds, aChildTags) {
    /** Exclude children ids from list if tag group is selected**/
    CS.forEach(aChildTags, function (oChild) {
      CS.remove(aSelectedTagIds, (id) => {
        return id === oChild.id;
      });
    });
  };

    var _getRecursivelyTagInfo = function (aTagGridData, oTagsToBeDeleted, aTagIds) {
    CS.forEach(aTagGridData, function (oTag) {
      var bIsTagIdPresent = CS.includes(aTagIds, oTag.id);
      if (bIsTagIdPresent) {
        if (oTag.isStandard) {
          oTagsToBeDeleted.bIsStandardTagSelected = true;
          return false;
        }
        var sTagLabel = CS.getLabelOrCode(oTag);
        oTagsToBeDeleted.aTagsLabelList.push(sTagLabel);
        _removeChildrenFromSelectedIds(aTagIds, oTag.children);
      } else {
        _getRecursivelyTagInfo(oTag.children, oTagsToBeDeleted, aTagIds);
      }
    });
  };

    var _resetActionItemsForChildren = function(aChildren, aProcessedGridViewData) {
        var iChildrenSize = aChildren.length;
        CS.forEach(aChildren, function(oChild, iChildIndex) {
            if (iChildIndex == 0) {
                oChild.actionItemsToShow = ["dummy", "moveDown"];
            } else if (iChildIndex == (iChildrenSize - 1)) {
                oChild.actionItemsToShow = ["moveUp"];
            } else {
                oChild.actionItemsToShow = ["moveUp", "moveDown"];
            }
            oChild.actionItemsToShow.unshift("deleteValues");

            //Hack
            var oProcessedData = CS.find(aProcessedGridViewData, {
                id: oChild.id
            });
            if (oProcessedData) {
                oProcessedData.actionItemsToShow = oChild.actionItemsToShow;
            }
        });
    };

    var _moveTagUp = function(sTagId) {
        var aTagGridData = TagProps.getTagGridData();
        var oTagParentNode = SettingUtils.getParentNodeByChildId(aTagGridData, sTagId);


        var fFunctonToExecute = function() {
            var aTagGridData = TagProps.getTagGridData();
            var oTagParentNode = SettingUtils.getParentNodeByChildId(aTagGridData, sTagId);
            var aTagChildren = oTagParentNode.children;
          let oGridViewProps = GridViewStore.getGridViewPropsByContext(GridViewContexts.TAG);
          let aGridViewData = oGridViewProps.getGridViewData();
            var oParentNode = SettingUtils.getParentNodeByChildId(aGridViewData, sTagId);
            if (oParentNode) {
                var aChildren = oParentNode.children;
                var iIndex = CS.findIndex(aChildren, {
                    id: sTagId
                });
                if (iIndex != -1) {
                    //Reordering Logic
                    aChildren[iIndex] = aChildren.splice(iIndex - 1, 1, aChildren[iIndex])[0];
                    aTagChildren[iIndex] = aTagChildren.splice(iIndex - 1, 1, aTagChildren[iIndex])[0];
                    oTagParentNode.tagValuesSequence = CS.map(aTagChildren, 'id');

                    _resetActionItemsForChildren(aChildren);
                }
            }
        };


        var aTagChildren = [];
        if (oTagParentNode) {
            aTagChildren = oTagParentNode.children;
            var iIndex = CS.findIndex(aTagChildren, {
                id: sTagId
            });
            if (iIndex > 0) {
                var oChild = aTagChildren[iIndex];
                oChild.modifiedSequence = {
                    "id": oChild.id,
                    "sequence": iIndex - 1
                };
                oChild.parent = {
                    id: oTagParentNode.id
                };

                _saveTagsInBulk([oChild], {
                    functionToExecute: fFunctonToExecute
                });
            }
        }
    };

    var _moveTagDown = function(sTagId) {
        var aTagGridData = TagProps.getTagGridData();
        var oTagParentNode = SettingUtils.getParentNodeByChildId(aTagGridData, sTagId);

        var fFunctonToExecute = function() {
            var aTagGridData = TagProps.getTagGridData();
            var oTagParentNode = SettingUtils.getParentNodeByChildId(aTagGridData, sTagId);
            var aTagChildren = oTagParentNode.children;
            let oGridViewProps = GridViewStore.getGridViewPropsByContext(GridViewContexts.TAG);
            let aGridViewData = oGridViewProps.getGridViewData();
            var oParentNode = SettingUtils.getParentNodeByChildId(aGridViewData, sTagId);

            if (oParentNode) {
                var aChildren = oParentNode.children;
                var iIndex = CS.findIndex(aChildren, {
                    id: sTagId
                });
                if (iIndex != -1) {
                    //Reordering Logic
                    aChildren[iIndex] = aChildren.splice(iIndex + 1, 1, aChildren[iIndex])[0];
                    aTagChildren[iIndex] = aTagChildren.splice(iIndex + 1, 1, aTagChildren[iIndex])[0];
                    oTagParentNode.tagValuesSequence = CS.map(aTagChildren, 'id');

                    _resetActionItemsForChildren(aChildren);
                }
            }
        };

        var aTagChildren = [];
        if (oTagParentNode) {
            aTagChildren = oTagParentNode.children;
            var iIndex = CS.findIndex(aTagChildren, {
                id: sTagId
            });
            if (iIndex != -1 && iIndex < aTagChildren.length - 1) {
                var oChild = aTagChildren[iIndex];
                oChild.modifiedSequence = {
                    "id": oChild.id,
                    "sequence": iIndex + 1
                };
                _saveTagsInBulk([oChild], {
                    functionToExecute: fFunctonToExecute
                });
            }
        }
    };

    var failurefetchTagDetailsByIdCallback = function(oResponse) {
        SettingUtils.failureCallback(oResponse, 'failurefetchTagDetailsByIdCallback', getTranslation());
    };

    var successfetchTagDetailsByIdCallback = function(oCallback, oResponse) {

        var oTagFromServer = oResponse.success;
        var oScreenProps = SettingScreenProps.screen;
        var oLoadedTagData = oScreenProps.getLoadedTagsData();
        oLoadedTagData[oTagFromServer.id] = oTagFromServer;
        if (oCallback.functionToExecute) {
            oCallback.functionToExecute();
        }

        _triggerChange();

    };

    var _fetchTagDetailsById = function(sId, oCallbackData) {
        var oParameters = {};
        oParameters.id = sId;
        oParameters.mode = "all";
        SettingUtils.csGetRequest(oTagRequestMapping.GetTag, oParameters, successfetchTagDetailsByIdCallback.bind(this, oCallbackData), failurefetchTagDetailsByIdCallback);
    };

    let _getSelectedLeftNavigationTreeItemType = () => {
        let oScreenProps = SettingScreenProps.screen;
        let sSelectedItem = oScreenProps.getSelectedLeftNavigationTreeItem();
        let sSelectedParentTreeId = oScreenProps.getSelectedLeftNavigationTreeParentId();
        let oSelectedItem = SettingUtils.getSelectedTreeItemById(sSelectedItem, sSelectedParentTreeId);
        return oSelectedItem.id;
    };

    var _fetchMasterTypeTags = function() {
        let sTaxonomyType = _getSelectedLeftNavigationTreeItemType();
        let sURL = sTaxonomyType === ConfigEntityTypeDictionary.ENTITY_TYPE_LANGUAGETAXONOMY ?
            oLanguageTaxonomyRequestMapping.GetAllowedLanguageTags : oTagRequestMapping.AllowedMasterTags;
        SettingUtils.csGetRequest(sURL, {}, successFetchMasterTagList, failureFetchMasterTagList);
    };

    var successFetchMasterTagList = function(oResponse) {
        SettingUtils.getAppData().setMasterTypeTagList(oResponse.success);
        _triggerChange();
    };

    var failureFetchMasterTagList = function(oResponse) {
        SettingUtils.failureCallback(oResponse, "failureFetchMasterTagList", getTranslation());
    };

    var _createTagDialogClick = function() {
        var oActiveTagNode = _getActiveTag();
        var sParentId = oActiveTagNode.parentId;
        if (CS.trim(oActiveTagNode.label) == "") {
            alertify.message(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
            return;
        }
        if(sParentId === "resolutiontag" && CS.isEmpty(oActiveTagNode.imageResolution)) {
          alertify.message(getTranslation().ERROR_PLEASE_ENTER_IMAGE_RESOLUTION);
          return;
        }

      if(sParentId === "resolutiontag" && oActiveTagNode.imageResolution && oActiveTagNode.imageResolution === "0") {
        alertify.message(getTranslation().ENTER_A_VALID_RESOLUTION);
        return;
      }

      _putTagNode(oActiveTagNode, sParentId);
    };

    var _cancelTagDialogClicked = function() {
        var aTagList = SettingUtils.getAppData().getTagList();
        var oActiveTag = _getActiveTag();
        delete aTagList[oActiveTag.id];
        _setActiveTag({});
        _triggerChange();
    };

    var _handleTagConfigDialogButtonClicked = function(sButtonId) {
        if (sButtonId == "create") {
            _createTagDialogClick({});
        } else {
            _cancelTagDialogClicked();
        }
    };

    var _getIsValidFileTypes = function(oFile) {
        var sTypeRegex = assetTypes.map(function(sType) {
            return '\\' + sType + '$';
        }).join('|');
        return oFile.name.match(new RegExp(sTypeRegex, 'i'));
    };

    var _handleExportTag = function(oSelectiveExportDetails) {
        SettingUtils.csPostRequest(oSelectiveExportDetails.sUrl, {}, oSelectiveExportDetails.oPostRequest, successExportFile, failureExportFile);
    };

    var successExportFile = function(oResponse) {
        alertify.success(getTranslation().EXPORT_IN_PROGRESS);
        SettingUtils.downloadFromByteStream(oResponse.success.fileStream, oResponse.success.fileName);
    };

    var failureExportFile = function() {
        SettingUtils.failureCallback(oResponse, "failureExportFile", getTranslation()); // eslint-disable-line
    };

    var uploadFileImport = function(oImportExcel, oCallback) {
        SettingUtils.csCustomPostRequest(oImportExcel.sUrl, oImportExcel.data,
            successUploadFileImport.bind(this, oCallback), failureUploadFileImport, false);
    };

    var successUploadFileImport = function(oResponse) {
      alertify.success(getTranslation().IMPORT_IN_PROGRESS);
    };

    var failureUploadFileImport = function(oResponse) {
        SettingUtils.failureCallback(oResponse, "failureUploadFileImport", getTranslation());
    };

    var _handleTagFileUploaded = function(aFiles,oImportExcel) {
        var bIsAnyValidImage = false;
        var bIsAnyInvalidImage = false;
        if (aFiles.length) {

            var iFilesInProcessCount = 0;
            var count = 0;

            var data = new FormData();
            CS.forEach(aFiles, function(file, index) {
                if (_getIsValidFileTypes(file)) {
                    bIsAnyValidImage = true;
                    var reader = new FileReader();
                    iFilesInProcessCount++;
                    // Closure to capture the file information.
                    reader.onload = (function(theFile) {
                        return function(event) {
                            count += 1;
                            var filekey = UniqueIdentifierGenerator.generateUUID();
                            data.append(filekey, theFile);
                        };
                    })(file);

                    reader.onloadend = function() {
                        iFilesInProcessCount--;
                        if (iFilesInProcessCount == 0) {
                          data.append("entityType", oImportExcel.entityType);
                          oImportExcel.data = data;
                          uploadFileImport(oImportExcel, {});
                        }
                    };

                    reader.readAsDataURL(file);
                } else {
                    bIsAnyInvalidImage = true;
                }
            });

        }
    };

    return {

        setActiveTag: function(oTag) {
            _setActiveTag(oTag);
        },

        fetchTagList: function(sContext) {
          _fetchTagTypeList();
          _fetchTagList(sContext);
        },

        setUpTagConfigGridView: function() {
            this.fetchTagColors();
            _fetchTagTypeList();
            _setUpTagConfigGridView();
        },

        fetchTagListForGridView: function(sContext) {
            _fetchTagTypeList();
            _fetchTagListForGridView(sContext);
        },

        discardUnsavedTag: function(oCallbackData) {
            _discardUnsavedTag(oCallbackData);
            _triggerChange();

        },

        saveChangedTagData: function(oCallbackData) {
            _saveChangedTagData(oCallbackData);
        },

        handleTagDataChanged: function(oModel, sContext, sNewValue) {
            _handleTagDataChanged(oModel, sContext, sNewValue);
        },

        handleTagDefaultValueChanged: function(oModel, sNewValue) {
            var oActiveTag = _getActiveTag();
            var oTag = oActiveTag;
            if (!oActiveTag.isCreated && !oActiveTag.isDirty) {
                if (oTag.defaultValue.id == sNewValue) {
                    return;
                }
                SettingUtils.makeObjectDirty(oActiveTag);
            }
            oTag = oActiveTag.clonedObject ? oActiveTag.clonedObject : oActiveTag;
            if (sNewValue == -1) {
                oTag.defaultValue = {
                    type: "com.cs.core.config.interactor.entity.tag.Tag"
                };
            } else {
                oTag.defaultValue.id = sNewValue;
            }
        },

        handleTagTypeRangeValueChanged: function(sTagValueId, sNewValue) {
            var oActiveTag = _getActiveTag();
            if (!oActiveTag.isCreated && !oActiveTag.isDirty) {
                SettingUtils.makeObjectDirty(oActiveTag);
            }
            var oTag = oActiveTag.clonedObject ? oActiveTag.clonedObject : oActiveTag;
            var oTagValue = CS.find(oTag.tagValues, {
                id: sTagValueId
            });
            oTagValue.relevance = sNewValue;
        },

        handleTagTypeChanged: function(oModel, sChangedTagType) {
            var oAppData = SettingUtils.getAppData();
            var aTagTypeList = oAppData.getTagTypeList();
            var oActiveTag = _getActiveTag();
            var oTag = oActiveTag;
            if (!oActiveTag.isCreated && !oActiveTag.isDirty) {
                if (oTag.tagType == sChangedTagType) {
                    return;
                }
                SettingUtils.makeObjectDirty(oActiveTag);
            }
            oTag = oActiveTag.clonedObject ? oActiveTag.clonedObject : oActiveTag;
            var oTagType = CS.find(aTagTypeList, {
                id: sChangedTagType
            });
            oTag.tagType = sChangedTagType;
            oTag.tagValues = CS.cloneDeep(oTagType.tagValues);

        },

        handleTagIconChanged: function(oModel, sContext, sIcon) {
            _handleTagDataChanged(oModel, sContext, sIcon);
            _triggerChange();
        },

        fetchTagColors: function() {
            let oMockTagColors = new MockTagColors();
            SettingUtils.getAppData().setTagColors(oMockTagColors);
        },

        createTagNode: function(sParentId) {
            _createTagNode(sParentId);
        },

        moveTagUp: function(sTagId) {
            _moveTagUp(sTagId);
        },

        moveTagDown: function(sTagId) {
            _moveTagDown(sTagId);
        },

        deleteMultipleTags: function (aSelectedTags, oCallBack) {
      var oTagData = {};
      if (!CS.isEmpty(aSelectedTags)) {
        oTagData.ids = aSelectedTags;
        var aTagGridData = TagProps.getTagGridData();
        var aTagsToBeDeletedList = [];
        var bIsStandardTagSelected = false;
        var oTagsToBeDeleted = {
          aTagsLabelList: [],
          bIsStandardTagSelected: false
        };
        _getRecursivelyTagInfo(aTagGridData, oTagsToBeDeleted, oTagData.ids);
        bIsStandardTagSelected = oTagsToBeDeleted.bIsStandardTagSelected;
        aTagsToBeDeletedList = oTagsToBeDeleted.aTagsLabelList;

        if (bIsStandardTagSelected) {
          alertify.message(getTranslation().STANDARD_TAG_DELETION);
        } else {
          CustomActionDialogStore.showListModeConfirmDialog(getTranslation().DELETE_CONFIRMATION, aTagsToBeDeletedList,
              function (oEvent) {
                _deleteMultipleTags(oTagData.ids, oCallBack)
                .then(_fetchTagListForGridView);
              }, function (oEvent) {
              }, true);
        }
      } else {
        alertify.message(getTranslation().NOTHING_SELECTED_TO_DELETE);
      }
    },

        handleGridPropertyValueChangeDependencies: function(sPropertyId, value, sPathToRoot, oContent) {
            _handleGridPropertyValueChangeDependencies(sPropertyId, value, sPathToRoot, oContent);
        },

        handleGridMSSAdditionalListItemAdded: function(sContentId, aCheckedItems, sContext, oReferencedData) {
            _handleGridMSSAdditionalListItemAdded(sContentId, aCheckedItems, sContext, oReferencedData);
        },

        postProcessTagListAndSave: function(oCallbackData) {
            _postProcessTagListAndSave(oCallbackData);
        },

        discardTagGridViewChanges: function(oCallbackData) {
            _discardTagGridViewChanges(oCallbackData);
        },

        handleGridPropertyKeyDownEvent: function(sKey, sContentId, sPathToRoot) {
            _handleGridPropertyKeyDownEvent(sKey, sContentId, sPathToRoot);
        },

        fetchTagDetailsById: function(sId, oCallback) {
            _fetchTagDetailsById(sId, oCallback)
        },

        fetchMasterTypeTags: function() {
            _fetchMasterTypeTags();
        },

        handleTagConfigDialogButtonClicked: function(sButtonId) {
            _handleTagConfigDialogButtonClicked(sButtonId);
        },

        handleExportTag: function(oSelectiveExportDetails) {
            _handleExportTag(oSelectiveExportDetails);
        },

        handleTagFileUploaded: function(aFiles,oImportExcel) {
            _handleTagFileUploaded(aFiles,oImportExcel);
        },

    }
})();

MicroEvent.mixin(TagStore);

export default TagStore;
