import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import CS from '../../../../../../libraries/cs';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import ContentScreenProps from './../model/content-screen-props';
import ContentUtils from './content-utils';
import ScreenModeUtils from './screen-mode-utils';
import TableHeaderIdEntityMap from '../../tack/table-header-id-entity-map';
import ContentScreenViewContextConstants from '../../tack/content-screen-view-context-constants';
import CommonModuleRequestMapping from '../../../../../../commonmodule/tack/common-module-request-mapping';
import ModuleDictionary from '../../../../../../commonmodule/tack/module-dictionary';
import ThumbnailModeConstants from '../../../../../../commonmodule/tack/thumbnail-mode-constants';
import ContentScreenConstants from './../model/content-screen-constants';
import HierarchyTypesDictionary from '../../../../../../commonmodule/tack/hierarchy-types-dictionary';
import MockDataForEntityBaseTypesDictionary from './../../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import AvailableEntityStore from './available-entity-store';
import FilterStoreFactory from './filter-store-factory';
import TaxonomyBaseTypeDictionary from '../../../../../../commonmodule/tack/mock-data-for-taxonomy-base-types-dictionary';
import CollectionRequestMapping from '../../tack/collection-request-mapping'
const getRequestMapping = ScreenModeUtils.getScreenRequestMapping;
const getTranslation = ScreenModeUtils.getTranslationDictionary;

const CollectionAndTaxonomyDataNavigationHelperStore = (function () {

  /************************************* Private API's **********************************************/

  let _triggerChange = function () {
    CollectionAndTaxonomyDataNavigationHelperStore.trigger('collection-and-taxonomy-data-navigation-helper-change');
  };

  let _handleCollectionHierarchyNodeClickAfterEffectsForTreeRendering = function (oReqData, oResponse) {
    oResponse = oResponse.success;
    let oScreenProps = ContentScreenProps.screen;
    let oActiveCollection = {
      id: oResponse.id,
      label: oResponse.label,
      children: []
    };
    oScreenProps.setActiveHierarchyCollection(oActiveCollection);

    let oInnerCallback = {
      functionToExecute: _triggerChange
    };

    let oCallback = {};
    CS.assign(oCallback, oReqData);
    oCallback.functionToExecute = _handleAnyNodeInHorizontalTreeClickVisualPropPreparation.bind(this, oReqData, oInnerCallback);

    _getStaticCollectionChildrenHierarchy(oReqData.clickedNodeId, oCallback);
  };

  let _getStaticCollectionChildrenHierarchy = function (sId, oCallback) {
    CS.getRequest(CollectionRequestMapping.GetStaticCollectionsTree, {id: sId},
        successGetStaticCollectionChildrenHierarchy.bind(this, oCallback), failureGetStaticCollectionChildrenHierarchy);
  };

  let successGetStaticCollectionChildrenHierarchy = function (oCallback, oResponse) {
    _commonContentHierarchyNodeGetChildrenCallback(oCallback, oResponse.success);
  };

  let failureGetStaticCollectionChildrenHierarchy = function (oResponse) {
    ContentUtils.failureCallback(oResponse, "failureGetStaticCollectionChildrenHierarchy", getTranslation());
  };

  let _handleAnyNodeInHorizontalTreeClickVisualPropPreparation = function (oReqData, oCallbackData) {
    let sClickedNodeId = oReqData.clickedNodeId;
    let oScreenProps = ContentScreenProps.screen;
    let oVisualProps = oScreenProps.getContentHierarchyVisualProps();

    if (oVisualProps[sClickedNodeId].isActive && !oVisualProps[sClickedNodeId].isCollapsed && !oReqData.isModeToggled) {
      return;
    }

    let aKeyValue = [{key: "isActive", value: false}];
    ContentUtils.applyValuesToAllTreeNodes(oVisualProps, aKeyValue);

    let oHierarchyTree = oScreenProps.getContentHierarchyTree();
    let aHierarchyChildren = oHierarchyTree.children;
    let oParentNode = ContentUtils.getParentNodeByChildId([oHierarchyTree], sClickedNodeId);
    let aChildrenKeyValueToReset = [
      {key: "isActive", value: false},
      {key: "isSelected", value: false},
      {key: "isCollapsed", value: true}
    ];
    ContentUtils.applyToAllNodesBelow(aChildrenKeyValueToReset, oParentNode, oVisualProps, true);

    oVisualProps[sClickedNodeId].isCollapsed = false;
    oVisualProps[sClickedNodeId].isSelected = true;
    oVisualProps[sClickedNodeId].isActive = true;

    let oNode = ContentUtils.getNodeFromTreeListById(aHierarchyChildren, sClickedNodeId);
    if (CS.isEmpty(oNode.children)) {
      oVisualProps[sClickedNodeId].noChildren = true;
    }

    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }

    return oNode;
  };

  let _setReferencedTagsDetailsForOrganizeDataOnTaxonomy = function (aSections) {
    var oScreenProps = ContentScreenProps.screen;
    let oReferencedTags = {};
    CS.forEach(aSections, function (oSection) {
      let aElements = oSection.elements;
      CS.forEach(aElements, function (oElement) {
        var oTag = oElement.tag;
        if (oTag) {
          oReferencedTags[oTag.id] = oTag;
        }
      });
    });
    oScreenProps.setReferencedTags(oReferencedTags);
  };

  let _commonContentHierarchyNodeGetChildrenCallback = function (oCallbackData, aChildren) {
    let sClickedNodeId = oCallbackData.clickedNodeId;
    let oScreenProps = ContentScreenProps.screen;
    let oHierarchyTree = oScreenProps.getContentHierarchyTree();

    let oClickedNode = ContentUtils.getNodeFromTreeListById([oHierarchyTree], sClickedNodeId);
    oClickedNode.children = aChildren;

    let oHierarchyVisualProps = oScreenProps.getContentHierarchyVisualProps();
    let bCanDelete = oCallbackData.selectedContext !== HierarchyTypesDictionary.TAXONOMY_HIERARCHY;
    let aKeyValue = [
      {key: "isCollapsed", value: true},
      {key: "isSelected", value: false},
      {key: "noChildren", value: false},
      {key: "isActive", value: false},
      {key: "canDelete", value: bCanDelete}
    ];
    ContentUtils.addNewTreeNodesToValueList(oHierarchyVisualProps, oClickedNode.children, aKeyValue);

    if (oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    } else {
      _triggerChange();
    }
  };

  let successGetStaticCollectionHorizontalTreeChildrenCallback = function (oCallbackData, oResponse) {
    oResponse = oResponse.success;

    //TODO: Temporary fix : referencedTags are not fetched separately in this call
    //Remove below code when referenced tags are sent from backend
    //Currently not needed for attributes & roles
    _setReferencedTagsDetailsForOrganizeDataOnTaxonomy(oResponse.sections);

    let oScreenProps = ContentScreenProps.screen;
    ContentUtils.initializeSectionExpansionState(oResponse.sections);
    oScreenProps.setActiveHierarchyCollection(oResponse);

    _commonContentHierarchyNodeGetChildrenCallback(oCallbackData, oResponse.children);
  };

  let failureGenericFunction = function (sCallingFailureFunctionName, oResponse) {
    ContentUtils.failureCallback(oResponse, sCallingFailureFunctionName, getTranslation());
    ContentUtils.getContentStore().checkForExtraNecessaryServerCalls(oResponse);
  };

  let _handleContentCollectionHorizontalTreeNodeClicked = function (oReqData, oFilterContext) {
    let sViewMode = "thumbnailViewMode";
    let bIsNodeClickCall = true;
    _handleCollectionHierarchyViewModeToggled(sViewMode, oReqData, bIsNodeClickCall, oFilterContext);

    let oScreenProps = ContentScreenProps.screen;
    oScreenProps.setActiveHierarchyNodeLevel(oReqData.level);
    oScreenProps.setActiveHierarchyNodeId(oReqData.clickedNodeId);
    let oCollectionData = {
      id: oReqData.clickedNodeId
    };

    let oInnerCallback = {
      functionToExecute: _triggerChange
    };

    let oCallbackData = {};
    CS.assign(oCallbackData, oReqData);
    oCallbackData.functionToExecute = _handleAnyNodeInHorizontalTreeClickVisualPropPreparation.bind(this, oReqData, oInnerCallback);

    let fSuccess = successGetStaticCollectionHorizontalTreeChildrenCallback.bind(this, oCallbackData);
    let fFailure = failureGenericFunction.bind(this, "failureGetStaticCollectionHorizontalTreeChildrenCallback");

    let sCollectionScreenMode = ContentScreenConstants.entityModes.STATIC_COLLECTION_MODE;
    let sUrl = getRequestMapping(sCollectionScreenMode).GetStaticCollectionDetails;

    CS.getRequest(sUrl, oCollectionData, fSuccess, fFailure);
  };

  let _handleCollectionHierarchyViewModeToggled = function (sViewMode, oReqData, bIsNodeClickCall, oFilterContext) {
    let oScreenProps = ContentScreenProps.screen;
    let oActiveHierarchyCollection = oScreenProps.getActiveHierarchyCollection();
    if (sViewMode === "thumbnailViewMode" && ContentUtils.activeCollectionHierarchySafetyCheck()) {
      oScreenProps.setHierarchyDetailViewMode(sViewMode);
      oReqData = oReqData || {clickedNodeId: oActiveHierarchyCollection.id};
      oReqData.isModeToggled = true;

      let sActiveHierarchyNodeId = oScreenProps.getActiveHierarchyNodeId();
      if (CS.isEmpty(sActiveHierarchyNodeId) || bIsNodeClickCall) {
        oScreenProps.setActiveHierarchyNodeId(oReqData.clickedNodeId);
      }

      let oCallbackData = {};
      oCallbackData.functionToExecuteForCollectionHierarchy = _handleCollectionHierarchyNodeClickAfterEffectsForTreeRendering.bind(this, oReqData);
      oCallbackData.toFetchCollectionId = oReqData.clickedNodeId;
      oCallbackData.filterContext = oFilterContext;

      let oExtraData = {};
      oExtraData.moduleId = ModuleDictionary.ALL;
      oExtraData.breadCrumbNodeLabel = oReqData.breadCrumbNodeLabel;
      delete oReqData.breadCrumbNodeLabel;
      oCallbackData.selectedContext = oReqData.selectedContext;

      AvailableEntityStore.fetchAvailableEntities(oCallbackData, oExtraData);

    }
    else if (sViewMode === "sectionViewMode") {
      oScreenProps.setHierarchyDetailViewMode(sViewMode);
      oReqData = oReqData || {clickedNodeId: oActiveHierarchyCollection.id};
      oReqData.isModeToggled = true;
      ContentUtils.resetToUpdateAllSCU();
      _handleContentCollectionHorizontalTreeNodeClicked(oReqData);
    }
  };

  let _updateHierarchyTreeAfterAddingCollectionNode = function (sParentId, oFilterContext, oResponse) {
    oResponse = oResponse.success;
    oResponse.parentId = sParentId;
    let oScreenProps = ContentScreenProps.screen;
    let oHierarchyTree = oScreenProps.getContentHierarchyTree();

    let oParentNode = ContentUtils.getNodeFromTreeListById([oHierarchyTree], sParentId);
    oParentNode.children.push(oResponse);

    let oVisualProps = oScreenProps.getContentHierarchyVisualProps();
    let aKeyValue = [
      {key: "isCollapsed", value: true},
      {key: "isSelected", value: false},
      {key: "noChildren", value: false},
      {key: "isActive", value: false},
      {key: "canDelete", value: true}
    ];
    ContentUtils.addNewTreeNodesToValueList(oVisualProps, [oResponse], aKeyValue);

    let sViewMode = "thumbnailViewMode";
    let bIsNodeCLickCall = true;
    let oReqData = {clickedNodeId: oResponse.id, selectedContext: "collectionHierarchy"};
    _handleCollectionHierarchyViewModeToggled(sViewMode, oReqData, bIsNodeCLickCall, oFilterContext);
  };

  let _handleCollectionHierarchyNodeDeleteAfterEffects = function (sDeletedId) {
    let oScreenProps = ContentScreenProps.screen;
    let oVisualProps = oScreenProps.getContentHierarchyVisualProps();
    let oHierarchyTree = oScreenProps.getContentHierarchyTree();

    let oParentNode = ContentUtils.getParentNodeByChildId([oHierarchyTree], sDeletedId);
    let oDeletedNode = CS.find(oParentNode.children, {id: sDeletedId});

    oVisualProps[sDeletedId].isSelected && oScreenProps.setActiveHierarchyCollection({});

    let aAllDeletedChildNodes = ContentUtils.getAllChildrenNodes(oDeletedNode.children);
    CS.forEach(aAllDeletedChildNodes, function (oNode) {
      delete oVisualProps[oNode.id];
    });
    delete oVisualProps[sDeletedId];

    CS.remove(oParentNode.children, {id: sDeletedId});
  };

  var _makeActiveHierarchyCollectionDirty = function () {
    var oScreenProps = ContentScreenProps.screen;
    var oActiveHierarchyCollection = oScreenProps.getActiveHierarchyCollection();
    ContentUtils.makeObjectDirty(oActiveHierarchyCollection);
    return oActiveHierarchyCollection.clonedObject;
  };

  let _generateADMForCollectionHierarchy = function () {
    let oScreenProps = ContentScreenProps.screen;
    let oActiveHierarchyCollection = oScreenProps.getActiveHierarchyCollection();
    let oActiveHierarchyCollectionCloneDeep = CS.cloneDeep(oActiveHierarchyCollection);
    if (!oActiveHierarchyCollectionCloneDeep.clonedObject) {
      return null;
    }

    let oADM = {};

    let sSplitter = ContentUtils.getSplitter();

    let aOldSections = oActiveHierarchyCollectionCloneDeep.sections;
    let aNewSections = oActiveHierarchyCollectionCloneDeep.clonedObject.sections;

    oADM.modifiedElements = [];
    let oSectionADMObject = {
      added: [],
      deleted: [],
      modified: []
    };

    CS.forEach(aNewSections, function (oNewSection) {
      let oOldSection = CS.remove(aOldSections, function (oSection) {
        return oSection.id === oNewSection.id
      });
      oOldSection = oOldSection[0];

      //if section found in old version
      if (oOldSection) {
        let oElementADMObject = {
          added: [],
          deleted: [],
          modified: []
        };
        let aOldSectionElements = oOldSection.elements;
        let aNewSectionElements = oNewSection.elements;

        let bIsSectionModified = (!CS.isEqual(oNewSection.sequence, oOldSection.sequence) || !CS.isEqual(oNewSection.isSkipped, oOldSection.isSkipped));
        oNewSection.isModified = bIsSectionModified;

        //iterating on new section elements
        CS.forEach(aNewSectionElements, function (oNewSectionElement) {
          let oOldSectionElement = CS.remove(aOldSectionElements, {id: oNewSectionElement.id});

          //if element found in old version
          if (oOldSectionElement.length > 0) {

            //if element is not modified
            let bIsElementModified = !CS.isEqual(oOldSectionElement[0], oNewSectionElement);
            if (bIsElementModified) {
              if (oNewSectionElement.type === "tag") {
                oNewSectionElement.addedDefaultValues = CS.differenceBy(oNewSectionElement.defaultValue, oOldSectionElement[0].defaultValue, "tagId");
                oNewSectionElement.deletedDefaultValues = CS.map(CS.differenceBy(oOldSectionElement[0].defaultValue, oNewSectionElement.defaultValue, "tagId"), "tagId");
                oNewSectionElement.modifiedDefaultValues = CS.filter(
                    CS.intersectionBy(oNewSectionElement.defaultValue, oOldSectionElement[0].defaultValue, "tagId"),
                    function (oDefaultValue) {
                      var oOldDefaultValue = CS.find(oOldSectionElement[0].defaultValue, {tagId: oDefaultValue.tagId});
                      return oOldDefaultValue && oOldDefaultValue.relevance !== oDefaultValue.relevance;
                    }
                );
                delete oNewSectionElement.defaultValue;
              }
              else if (oNewSectionElement.type === "attribute") {
                let sAttributeType = oNewSectionElement.attribute.type;
                if (ContentUtils.isAttributeTypeHtml(sAttributeType)) {
                  oNewSectionElement.defaultValue = oNewSectionElement.defaultValue.hasOwnProperty("htmlValue") ?
                      oNewSectionElement.defaultValue.htmlValue : oNewSectionElement.defaultValue;
                }
              }

              oElementADMObject.modified.push(oNewSectionElement);
              oADM.modifiedElements.push(oNewSectionElement);
              oNewSectionElement.isModified = bIsElementModified;
            }
          } else {
            //not found in old so add to added list
            oNewSectionElement.id = oNewSectionElement.id.split(sSplitter)[0];
            oElementADMObject.added.push(oNewSectionElement);
          }
        });

        let aDeletedElements = CS.map(aOldSectionElements, 'id');
        CS.merge(oElementADMObject.deleted, aDeletedElements);

        oNewSection.addedElements = oElementADMObject.added;
        oNewSection.deletedElements = oElementADMObject.deleted;
        oNewSection.modifiedElements = oElementADMObject.modified;
        delete oNewSection.elements;

        if (bIsSectionModified) {
          oSectionADMObject.modified.push(oNewSection);
        }

      } else {
        oSectionADMObject.added.push(oNewSection);
      }
    });

    let aDeletedSections = CS.map(aOldSections, 'id');
    CS.merge(oSectionADMObject.deleted, aDeletedSections);

    oADM.addedSections = oSectionADMObject.added;
    oADM.deletedSections = oSectionADMObject.deleted;
    oADM.modifiedSections = [];
    CS.forEach(oSectionADMObject.modified, function (oModifiedSection) {
      oADM.modifiedSections.push({
        id: oModifiedSection.id,
        sequence: oModifiedSection.sequence,
        isSkipped: oModifiedSection.isSkipped,
        isModified: oModifiedSection.isModified
      });
      oADM.modifiedElements = oADM.modifiedElements.concat(oModifiedSection.modifiedElements);
    });

    oADM.id = oActiveHierarchyCollection.id;
    oADM.label = oActiveHierarchyCollection.label;
    oADM.parentId = oActiveHierarchyCollection.parentId;
    oADM.children = oActiveHierarchyCollection.children;

    return oADM;
  };

  let successSaveHierarchyCollectionCallback = function (oResponse) {
    oResponse = oResponse.success;

    //TODO: Temporary fix : referencedTags are not fetched separately in this call
    //Remove below code when referenced tags are sent from backend
    //Currently not needed for attributes & roles
    _setReferencedTagsDetailsForOrganizeDataOnTaxonomy(oResponse.sections);
    ContentUtils.initializeSectionExpansionState(oResponse.sections);

    let oScreenProps = ContentScreenProps.screen;
    oScreenProps.setActiveHierarchyCollection(oResponse);

    alertify.success(getTranslation().SUCCESSFULLY_SAVED);
    _triggerChange();
  };

  let _saveHierarchyCollection = function (oCallbackData) {
    let oCallBack = {};
    CS.assign(oCallBack, oCallbackData);

    let oScreenProps = ContentScreenProps.screen;
    let oActiveHierarchyCollection = oScreenProps.getActiveHierarchyCollection();

    if (!oActiveHierarchyCollection.clonedObject) {
      alertify.message(getTranslation().SCREEN_STORE_SAVE_NOTHING_CHANGED);
      return;
    }

    let oADMToSave = _generateADMForCollectionHierarchy();

    let sCollectionScreenMode = ContentScreenConstants.entityModes.STATIC_COLLECTION_MODE;
    let sUrl = getRequestMapping(sCollectionScreenMode).SaveStaticCollectionDetails;
    let fSuccess = successSaveHierarchyCollectionCallback;
    let fFailure = failureGenericFunction.bind(this, "failureSaveHierarchyCollectionCallback");

    CS.postRequest(sUrl, {}, oADMToSave, fSuccess, fFailure);
  };

  let _copyOrCutSelectedEntitiesInHierarchy = function (sMenuContext) {
    let aEntitiesToCopy = ContentScreenProps.availableEntityViewProps.getSelectedEntities();

    let oScreenProps = ContentScreenProps.screen;
    let sActiveHierarchyNodeId = oScreenProps.getActiveHierarchyNodeId();
    let oHierarchyEntitiesToCopyData = {
      hierarchyNodeId: sActiveHierarchyNodeId,
      entityList: aEntitiesToCopy,
      action: sMenuContext
    };
    oScreenProps.setHierarchyEntitiesToCopyOrCutData(oHierarchyEntitiesToCopyData);
    oScreenProps.setRightClickedThumbnailModel({});

    if (sMenuContext === "copy") {
      ContentUtils.showMessage(getTranslation().ENTITIES_COPIED_SUCCESSFULLY);
    }
    _triggerChange();
  };

  /**
   * @function _handleTaxonomyHierarchyViewModeToggled
   * @param sViewMode
   * @param oClickedNode
   * @param oExtraData - Contains filterContext.
   * @private
   */
  let _handleTaxonomyHierarchyViewModeToggled = function (sViewMode, oClickedNode, oExtraData) {
    let oScreenProps = ContentScreenProps.screen;
    let oFilterStore = FilterStoreFactory.getFilterStore(oExtraData.filterContext);
    let oFilterProps = oFilterStore.getFilterProps();
    let oActiveHierarchyTaxonomy = oFilterProps.getTaxonomySettingIconClickedNode();
    let sSelectedContext = (oExtraData && oExtraData.selectedContext) || HierarchyTypesDictionary.TAXONOMY_HIERARCHY;

    if (sViewMode === "thumbnailViewMode") {
      if (ContentUtils.activeTaxonomyOrganiseSafetyCheck(oExtraData.filterContext)) {

        let oCallbackData = {};
        oCallbackData.selectedContext = sSelectedContext;
        oCallbackData.filterContext = oExtraData.filterContext;
        oScreenProps.setHierarchyDetailViewMode(sViewMode);
        if (!CS.isEmpty(oClickedNode)) {
          oFilterProps.setTaxonomySettingIconClickedNode(oClickedNode);
        }
        return AvailableEntityStore.fetchAvailableEntities(oCallbackData, oExtraData);

      } else {
        alertify.message(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
      }
    } else if (sViewMode === "sectionViewMode") {
      oScreenProps.setHierarchyDetailViewMode(sViewMode);
      ContentUtils.resetToUpdateAllSCU();
      let iLevel = oScreenProps.getActiveHierarchyNodeLevel();
      let oReqData = {
        level: iLevel,
        clickedNodeId: oActiveHierarchyTaxonomy.id,
        isModeToggled: true,
        viewMode: sViewMode
      };
      return _handleContentTaxonomyHorizontalTreeNodeClicked(oReqData, oExtraData.filterContext);
    }
  };

  let _handleClassTaxonomyHorizontalTreeOuterNodeClicked = function (oReqData, oFilterContext) {
    if (oReqData.clickedNodeId === ContentUtils.getHierarchyDummyNodeId()) {
      _commonContentHierarchyNodeGetChildrenCallback({}, []);
      _handleAnyTaxonomyHorizontalTreeNodeClicked(oReqData, oFilterContext);
    }
    else {
      let oCallback = {};
      CS.assign(oCallback, oReqData);
      oCallback.isContentTaxonomyHierarchy = true;
      oCallback.functionToExecute = _getFunctionToExecuteOnTaxonomyNodeClicked.bind(this, oReqData, oFilterContext);
      oCallback.filterContext = oFilterContext;
      let oExtraData = {"parentTaxonomyId": "", "selectedTaxonomyIds": [], "clickedTaxonomyId": ""};
      _handleParentTaxonomyClicked(oCallback, oExtraData);
    }
  };

  let _addStaticCollectionIdIfExists = function (oPostDataForFilter) {
    let oComponentProps = ContentUtils.getComponentProps();
    let oActiveCollection = oComponentProps.collectionViewProps.getActiveCollection();
    if (ContentUtils.getIsStaticCollectionScreen() && !CS.isEmpty(oActiveCollection) &&
        oActiveCollection.type === "staticCollection" && !ContentUtils.getAvailableEntityViewStatus()) {
      oPostDataForFilter.collectionId = oActiveCollection.id;
    }
  };

  let successFetchContentHierarchyTaxonomyCallBack = function (oCallbackData, oExtraData, oResponse) {
    oResponse = oResponse.success;

    let oHierarchyTree = ContentScreenProps.screen.getContentHierarchyTree();
    CS.forEach(oResponse.klassTaxonomyInfo, function (oTaxonomyInfo) {
      _updateVisibleHierarchyTreeNodeCount(oTaxonomyInfo, [oHierarchyTree]);

      let aChildren = oTaxonomyInfo.children;
      _commonContentHierarchyNodeGetChildrenCallback(oCallbackData, aChildren);
    });

  };

  let _handleParentTaxonomyClicked = function (oCallbackData, oExtraData) {
    let oPostDataForFilter = ContentUtils.getAllInstancesRequestData(oCallbackData.filterContext);
    delete oPostDataForFilter.isFilterDataRequired;
    let oURLData = {};

    if (!CS.isEmpty(oExtraData)) {
      CS.assign(oPostDataForFilter, oExtraData);
    }

    let oXRayData = ContentUtils.getXRayPostData();
    if (!CS.isEmpty(oXRayData)) {
      CS.assign(oPostDataForFilter, oXRayData);
    }

    let sContentId = ContentUtils.getTreeRootNodeId();
    oCallbackData = oCallbackData || {};
    let oActiveCollection = ContentScreenProps.collectionViewProps.getActiveCollection();
    if (!CS.isEmpty(oActiveCollection) && oActiveCollection.type === "staticCollection") {
      sContentId = oActiveCollection.id;
      oCallbackData.isCollectionCallback = true;
    }
    let oData = {
      id: sContentId,
    };

    let oFilterStore = FilterStoreFactory.getFilterStore(oCallbackData.filterContext);
    let aTreeList = oFilterStore.getTaxonomyTree();
    let oOldProps = {};
    CS.forEach(aTreeList, function (oTree) {
      var oVisualProps = oFilterStore.getTaxonomyVisualProps();
      oOldProps[oTree.id] = oVisualProps[oTree.id];
      oOldProps[oTree.id].isChecked = 0;

    });
    oFilterStore.setTaxonomyVisualProps(oOldProps);

    if (oFilterStore.getTaxonomySearchText()) {
      oFilterStore.setTaxonomySearchText('');
      oFilterStore.setMatchingTaxonomyIds([]);
    }

    let oComponentProps = ContentUtils.getComponentProps();
    let oVariantSectionViewProps = oComponentProps.variantSectionViewProps;
    let bAvailableEntityChildVariantViewVisibilityStatus = oVariantSectionViewProps.getAvailableEntityChildVariantViewVisibilityStatus();
    let bAvailableEntityParentVariantViewVisibilityStatus = oVariantSectionViewProps.getAvailableEntityParentVariantViewVisibilityStatus();
    let bIsForTablePopOverContext = oComponentProps.availableEntityViewProps.getAvailableEntityViewContext() === ContentScreenViewContextConstants.TABLE_CONTEXT_AVAILABLE_ENTITY_VIEW;
    let bIsForVariantQuicklist = bAvailableEntityChildVariantViewVisibilityStatus || bAvailableEntityParentVariantViewVisibilityStatus || bIsForTablePopOverContext;

    oPostDataForFilter.isArchivePortal = ContentUtils.getIsArchive();

    let sUrl = "";
    let sSelectedEntity;
    if (bIsForVariantQuicklist) {
      if (bIsForTablePopOverContext) {
        let oActivePopOverContextData = oComponentProps.uomProps.getActivePopOverContextData();
        sSelectedEntity = TableHeaderIdEntityMap[oActivePopOverContextData.entity];
      } else {
        sSelectedEntity = oVariantSectionViewProps.getSelectedEntity();
      }
      oFilterStore.fillInstanceIdAndBaseTypeForContext(oPostDataForFilter);
      oPostDataForFilter.moduleEntities = [sSelectedEntity];
      sUrl = getRequestMapping().GetAllTaxonomiesByLeafIdsForVariantQuicklist;
    } else if (ContentUtils.isCollectionScreen() && ContentUtils.getIsStaticCollectionScreen()) {
      let oActiveCollection = oComponentProps.collectionViewProps.getActiveCollection();
      sUrl = getRequestMapping().GetAllTaxonomiesForStaticCollection;
      oPostDataForFilter.collectionId = oActiveCollection.id;
      if (ContentUtils.getAvailableEntityViewStatus()) {
        oPostDataForFilter.isQuicklist = true;
      }
    } else if (ContentUtils.getAvailableEntityViewStatus()) {
        let oSectionSelectionStatus = ContentScreenProps.screen.getSetSectionSelectionStatus();
        let sRelationshipSectionId = oSectionSelectionStatus['selectedRelationship'].id;
        ContentUtils.getAdditionalDataForRelationshipCalls(oPostDataForFilter);
        sUrl = ContentUtils.getURLForTaxonomiesForRelationship(oPostDataForFilter.clickedTaxonomyId, sRelationshipSectionId);
    } else {
      sUrl = getRequestMapping().GetAllKlassTaxonomies;
      delete oPostDataForFilter.selectedTaxonomyIds;
      _addStaticCollectionIdIfExists(oPostDataForFilter);
    }

    oCallbackData.clickedTaxonomyId = oPostDataForFilter.clickedTaxonomyId === "" || CS.isNull(oPostDataForFilter.clickedTaxonomyId) ? "-1" : oPostDataForFilter.clickedTaxonomyId;
    let fSuccess = oCallbackData.isContentTaxonomyHierarchy ?
        successFetchContentHierarchyTaxonomyCallBack.bind(this, oCallbackData, oExtraData) :
        successFetchTaxonomyCallBack.bind(this, oCallbackData, oExtraData);
    let fFailure = ContentUtils.getContentStore().failureFetchContentListCallBack.bind(this, oCallbackData);
    CS.postRequest(sUrl, oData, oPostDataForFilter, fSuccess, fFailure, null, oURLData);
  };

  let _updateTaxonomyTreeBackupCountAndLabel = function (oResponse, oFilterContext) {
    let sTaxonomyId = oResponse.id;
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    let oTaxonomyTreeBackup = oFilterStore.getTaxonomyTreeBackup();
    let aTaxonomyTree = oTaxonomyTreeBackup.taxonomyTree;
    let oTaxonomy = CS.find(aTaxonomyTree, {id: sTaxonomyId});
    if (oTaxonomy && !CS.isEmpty(oTaxonomy.children)) {
      oFilterStore.updateTaxonomyCountAndLabel(oResponse.children, oTaxonomy.children);
    }
  };

  let successFetchTaxonomyCallBack = function (oCallbackData, oExtraData, oResponse) {
    oResponse = CS.find(oResponse.success.klassTaxonomyInfo, {id: oCallbackData.clickedTaxonomyId});
    let oFilterContext = oCallbackData.filterContext;
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext)
    oFilterStore.resetAllTaxonomyRootNodeOtherThanSelectedTaxonomy();
    oFilterStore.setAllAffectedTreeNodeIds([]);

    if (oResponse.id === oFilterStore.getOldSelectedOuterParentId() && !oExtraData.isSearchResult) {
      var oCallback = {};
      oCallback.functionToExecute = oFilterStore.handleTaxonomySearchTextChanged.bind(oFilterStore);
      _updateTaxonomyTreeBackupCountAndLabel(oResponse, oFilterContext);
      oFilterStore.restoreTaxonomyTreeBackup(oCallback);
    }
    else {
      let oTaxonomy = oFilterStore.getTaxonomyNodeById(oResponse.id);
      oTaxonomy.count = oResponse.count || 0;
      oTaxonomy.children = oResponse.children || [];
      var oTaxonomyTreeFlatMap = oFilterStore.getTaxonomyTreeFlatMap();

      let bUseParentStatus = !oExtraData.isPropsCreated;
      /** Fixes Bug No. 2546 ***/
      let oTaxonomyTreeVisualProps = oFilterStore.getTaxonomyVisualProps();
      let oParentVisualProps = oTaxonomyTreeVisualProps[oTaxonomy.id];
      let iCheckedStatus = oParentVisualProps && oParentVisualProps.isChecked;

      oFilterStore.addExpandAndCollapsePropertyToTaxonomyTree([oTaxonomy], null, oTaxonomyTreeVisualProps, iCheckedStatus, bUseParentStatus);

      oFilterStore.putParentNodeIdInAllChildNodesAndCreateFlatNodeMap(oTaxonomy.children, oTaxonomy.id, oTaxonomyTreeFlatMap);
      oFilterStore.resetParentCheckedStateBasedOnChildrenState();
    }

    if (oCallbackData && oCallbackData.functionToExecute) {
      oCallbackData.functionToExecute();
    }

    _triggerChange();
  };

  let _handleTaxonomyChildrenLazyData = function (oCallbackData, oExtraData) {
    let oPostDataForFilter = ContentUtils.getAllInstancesRequestData(oCallbackData.filterContext);
    delete oPostDataForFilter.isFilterDataRequired;

    //Backend sometimes sends 0 count if there is any selectedTaxonomyId.
    oPostDataForFilter.selectedTaxonomyIds = [];
    oPostDataForFilter.selectedTypes = [];

    if (!CS.isEmpty(oExtraData)) {
      CS.assign(oPostDataForFilter, oExtraData);
    }

    let oXRayData = ContentUtils.getXRayPostData();
    if (!CS.isEmpty(oXRayData)) {
      CS.assign(oPostDataForFilter, oXRayData);
    }

    let sContentId = ContentUtils.getTreeRootNodeId();
    oCallbackData = oCallbackData || {};

    let oData = {
      id: sContentId,
    };

    /*** Below code is added because count only get updated for those which are selected inside class taxonomy.**/
    if (!oPostDataForFilter.clickedTaxonomyId) {
      oPostDataForFilter.selectedTypes = [];
    }

    let oComponentProps = ContentUtils.getComponentProps();
    let oVariantSectionViewProps = oComponentProps.variantSectionViewProps;
    let bAvailableEntityChildVariantViewVisibilityStatus = oVariantSectionViewProps.getAvailableEntityChildVariantViewVisibilityStatus();
    let bAvailableEntityParentVariantViewVisibilityStatus = oVariantSectionViewProps.getAvailableEntityParentVariantViewVisibilityStatus();
    let bIsForVariantQuicklist = bAvailableEntityChildVariantViewVisibilityStatus || bAvailableEntityParentVariantViewVisibilityStatus;
    let bIsForTableContext = oComponentProps.availableEntityViewProps.getAvailableEntityViewContext() === ContentScreenViewContextConstants.TABLE_CONTEXT_AVAILABLE_ENTITY_VIEW;
    oPostDataForFilter.isArchivePortal = ContentUtils.getIsArchive();

    let sUrl = "";
    let oURLData = {};
    if (bIsForVariantQuicklist || bIsForTableContext) {
      let sSelectedEntity;
      if (bIsForTableContext) {
        let oActivePopOverContextData = oComponentProps.uomProps.getActivePopOverContextData();
        sSelectedEntity = TableHeaderIdEntityMap[oActivePopOverContextData.entity];
      } else {
        sSelectedEntity = oVariantSectionViewProps.getSelectedEntity();
      }
      oPostDataForFilter.entityId = sSelectedEntity;
      if (!oPostDataForFilter.clickedTaxonomyId) {
        oPostDataForFilter.leafIds = [];
      }
      sUrl = getRequestMapping().GetAllTaxonomiesByLeafIdsForVariantQuicklist;

    } else if (ContentUtils.getAvailableEntityViewStatus()) {
      let oSectionSelectionStatus = ContentScreenProps.screen.getSetSectionSelectionStatus();
      let sRelationshipSectionId = oSectionSelectionStatus['selectedRelationship'].id;
      ContentUtils.getAdditionalDataForRelationshipCalls(oPostDataForFilter);
      sUrl = ContentUtils.getURLForTaxonomiesForRelationship(oPostDataForFilter.clickedTaxonomyId, sRelationshipSectionId);
    } else {
      sUrl = getRequestMapping().GetOrganizeScreenTree;
      _addStaticCollectionIdIfExists(oPostDataForFilter);
    }

    oCallbackData.clickedTaxonomyId = CS.isNull(oPostDataForFilter.clickedTaxonomyId) || oPostDataForFilter.clickedTaxonomyId === " " ? "-1" : oPostDataForFilter.clickedTaxonomyId;
    let fSuccess = oCallbackData.isContentTaxonomyHierarchy ?
        successFetchContentHierarchyTaxonomyCallBack.bind(this, oCallbackData, oExtraData) :
        successFetchTaxonomyCallBack.bind(this, oCallbackData, oExtraData);
    let fFailure = ContentUtils.getContentStore().failureFetchContentListCallBack.bind(this, oExtraData);
    return CS.postRequest(sUrl, oData, oPostDataForFilter, fSuccess, fFailure, null, oURLData);
  };

  let _getFunctionToExecuteOnTaxonomyNodeClicked = async function (oRequestData, oFilterContext) {
    await _handleAnyTaxonomyHorizontalTreeNodeClicked(oRequestData, oFilterContext);
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    let oFilterProps = oFilterStore.getFilterProps();
    let aAppliedFilterData = oFilterProps.getAppliedFilters();
    if (!CS.isEmpty(aAppliedFilterData)) {
      alertify.message(getTranslation().SOME_CONTENTS_MIGHT_NOT_BE_VISIBLE_DUE_TO_APPLIED_FILTERS);
    }
  };

  let successFetchOrganisedDataOnTaxonomyHierarchyNodeClickCallback = function (oCallbackData, oResponse) {
    let oSuccess = oResponse.success;
    let oEntityFromServer = oSuccess.entity;
    let oConfigDetails = oSuccess.configDetails;
    let oKlass = {};
    let aSections = [];
    let oClickedNode = oCallbackData.clickedNode;
    let oFilterContext = oCallbackData.filterContext;
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);

    if (!CS.isEmpty(oClickedNode.type)) {
      oKlass = oEntityFromServer;
      aSections = oKlass.sections;
    } else {
      oKlass = oEntityFromServer;
      aSections = oKlass.sections;
    }
    ContentUtils.initializeSectionExpansionState(aSections);

    let oFilterProps = oFilterStore.getFilterProps();

    ContentScreenProps.screen.setReferencedTags(oConfigDetails.referencedTags);

    oFilterProps.setTaxonomyNodeSections({sections: aSections});
    oFilterProps.setTaxonomySettingIconClickedNode(oClickedNode);
    oFilterProps.setTaxonomyActiveClass(oKlass);

    _triggerChange();
  };

  let _fetchOrganisedDataOnTaxonomyHierarchyNodeClick = function (oClickedNode, oFilterContext) {

    let sClickedNodeId = oClickedNode.id;
    let sClassType = oClickedNode.type;
    let sUrl = "";

    if (!CS.isEmpty(sClassType)) {
      sUrl = CommonModuleRequestMapping.GetArticleWithoutPC;
      if (sClassType === MockDataForEntityBaseTypesDictionary.taskKlassBaseType) {
        sUrl = CommonModuleRequestMapping.GetTaskWithGlobalPermission;
      }
      else if (sClassType === MockDataForEntityBaseTypesDictionary.assetKlassBaseType) {
        sUrl = CommonModuleRequestMapping.GetAssetWithoutPC;
      }
      else if (sClassType === MockDataForEntityBaseTypesDictionary.marketKlassBaseType) {
        sUrl = CommonModuleRequestMapping.GetTargetWithoutPC;
      }
      else if (sClassType === MockDataForEntityBaseTypesDictionary.textAssetKlassBaseType) {
        sUrl = CommonModuleRequestMapping.GetTextAssetWithoutPC;
      }
      else if (sClassType === MockDataForEntityBaseTypesDictionary.supplierKlassBaseType) {
        sUrl = CommonModuleRequestMapping.GetSupplierWithoutPC;
      }
      else if (oClickedNode.type === TaxonomyBaseTypeDictionary.masterTaxonomy) {
        sUrl = CommonModuleRequestMapping.GetKlassAndAttributionTaxonomy;
      }
      else {
        sUrl = CommonModuleRequestMapping.GetArticleTaxonomy;
      }
    }

    let oParameters = {
      id: sClickedNodeId
    };

    let oCallbackData = {
      clickedNode: oClickedNode,
      filterContext: oFilterContext
    };
    let fSuccess = successFetchOrganisedDataOnTaxonomyHierarchyNodeClickCallback.bind(this, oCallbackData);
    let fFailure = failureGenericFunction.bind(this, "failureTreeHeaderSettingIconClickedCallback");

    CS.getRequest(sUrl, oParameters, fSuccess, fFailure);
  };

  let _handleAnyTaxonomyHorizontalTreeNodeClicked = function (oReqData, oFilterContext) {
    let oNode = _handleAnyNodeInHorizontalTreeClickVisualPropPreparation(oReqData);
    if (oReqData.viewMode === "sectionViewMode") {
      _fetchOrganisedDataOnTaxonomyHierarchyNodeClick(oNode, oFilterContext);
    } else {
      let oExtraData = {};
      oExtraData.breadCrumbNodeLabel = oReqData.breadCrumbNodeLabel;
      delete oReqData.breadCrumbNodeLabel;
      oExtraData.selectedContext = oReqData.selectedContext;
      oExtraData.filterContext = oFilterContext;
      return _handleTaxonomyHierarchyViewModeToggled("thumbnailViewMode", oNode, oExtraData);
    }
  };

  let _handleCustomTaxonomyHorizontalTreeNodeClicked = function (oReqData, oFilterContext) {
    let sClickedNodeId = oReqData.clickedNodeId;
    let oExtraData = {};
    let oScreenProps = ContentScreenProps.screen;
    oExtraData.parentTaxonomyId = oScreenProps.getSelectedOuterParentContentHierarchyTaxonomyId();
    oExtraData.clickedTaxonomyId = sClickedNodeId;
    let oCallBackData = {
      isContentTaxonomyHierarchy: true,
      level: oReqData.level,
      clickedNodeId: sClickedNodeId,
      functionToExecute: _getFunctionToExecuteOnTaxonomyNodeClicked.bind(this, oReqData, oFilterContext),
      filterContext: oFilterContext,
      selectedContext: oReqData.selectedContext
    };
    _handleTaxonomyChildrenLazyData(oCallBackData, oExtraData);
  };

  let _handleContentTaxonomyHorizontalTreeNodeClicked = function (oReqData, oFilterContext) {
    let iLevel = oReqData.level;
    let oScreenProps = ContentScreenProps.screen;
    oScreenProps.setActiveHierarchyNodeLevel(iLevel);
    oScreenProps.setActiveHierarchyNodeId(oReqData.clickedNodeId);
    oScreenProps.setHierarchyDetailViewMode("sectionViewMode");
    if (iLevel === 0) {
      oScreenProps.setSelectedOuterParentContentHierarchyTaxonomyId(oReqData.clickedNodeId);
    }

    let oFilterProps = ContentUtils.getFilterProps(oFilterContext);
    oFilterProps.resetPaginationData();

    let sOuterSelectedNode = oScreenProps.getSelectedOuterParentContentHierarchyTaxonomyId();
    if (sOuterSelectedNode === ContentUtils.getHierarchyDummyNodeId()) { //ClassTaxonomy node clicked.
      return _handleClassTaxonomyHorizontalTreeOuterNodeClicked(oReqData, oFilterContext);
    } else if (sOuterSelectedNode === "-1" && iLevel !== 0) {
      return _handleAnyTaxonomyHorizontalTreeNodeClicked(oReqData, oFilterContext);
    } else { //custom taxonomy node clicked.
      return _handleCustomTaxonomyHorizontalTreeNodeClicked(oReqData, oFilterContext);
    }
  };

  let _updateVisibleHierarchyTreeNodeCount = function (oResponseTree, aHierarchyTree) {
    let sSelfId = oResponseTree.id;
    let oHierarchyNode = ContentUtils.getNodeFromTreeListById(aHierarchyTree, sSelfId);
    oHierarchyNode.count = oResponseTree.count;
    oHierarchyNode.type = oResponseTree.type;
    CS.forEach(oResponseTree.children, function (oResponseChild) {
      _updateVisibleHierarchyTreeNodeCount(oResponseChild, aHierarchyTree);
    });
  };

  let successFetchTaxonomyTreeDetailsCallBack = function (oResponse) {
    oResponse = oResponse.success;
    let oScreenProps = ContentScreenProps.screen;
    let oHierarchyTree = oScreenProps.getContentHierarchyTree();
    CS.forEach(oResponse.klassTaxonomyInfo, function (oTaxonomyInfo) {
      _updateVisibleHierarchyTreeNodeCount(oTaxonomyInfo, [oHierarchyTree]);
    });
    _triggerChange();
  };

  let _fetchVisibleHierarchyTreeNodeCount = function (oFilterContext) {
    let oScreenProps = ContentScreenProps.screen;
    let oVisualProps = oScreenProps.getContentHierarchyVisualProps();
    let oHierarchyTree = oScreenProps.getContentHierarchyTree();
    let oClassTaxonomyNode = oHierarchyTree.children[1];
    let aTempClassTaxonomyTree = [oClassTaxonomyNode];

    let sOuterParentTaxonomyId = oScreenProps.getSelectedOuterParentContentHierarchyTaxonomyId();
    if (sOuterParentTaxonomyId === ContentUtils.getHierarchyDummyNodeId() || sOuterParentTaxonomyId === 999 || sOuterParentTaxonomyId === "-1") {
      return;
    }

    let aVisibleNodeIds = CS.keys(oVisualProps) || [];
    CS.remove(aVisibleNodeIds, function (sId) {
      let bRemoveId = false;
      bRemoveId = sId === ContentUtils.getHierarchyDummyNodeId() || sId === 999 || sId === -1;
      if (!bRemoveId) {
        let oFound = ContentUtils.getNodeFromTreeListById(aTempClassTaxonomyTree, sId);
        bRemoveId = !CS.isEmpty(oFound);
      }
      return bRemoveId;
    });

    let oPostDataForFilter = ContentUtils.getAllInstancesRequestData(oFilterContext);
    if (CS.isEmpty(oPostDataForFilter.moduleId)) {
      return;
    }
    delete oPostDataForFilter.selectedTaxonomyIds;
    oPostDataForFilter.parentTaxonomyId = oScreenProps.getSelectedOuterParentContentHierarchyTaxonomyId();
    oPostDataForFilter.clickedTaxonomyId = oScreenProps.getActiveHierarchyNodeId();
    let sContentId = ContentUtils.getTreeRootNodeId();
    let oData = {
      id: sContentId,
    };
    oPostDataForFilter.isArchivePortal = ContentUtils.getIsArchive();
    _addStaticCollectionIdIfExists(oPostDataForFilter);
    let sUrl = getRequestMapping().GetOrganizeScreenTree;
    let oURLData = {};
    let fSuccess = successFetchTaxonomyTreeDetailsCallBack;
    let fFailure = failureGenericFunction.bind(this, "failureFetchTaxonomyTreeDetailsCallBack");
    return CS.postRequest(sUrl, oData, oPostDataForFilter, fSuccess, fFailure, null, oURLData);
  };

  let getUpdatedCountAndData = (oFilterContext) => {
    let oExtraData = {
      selectedContext: ContentUtils.getSelectedHierarchyContext(),
      filterContext: oFilterContext
    };
    _fetchVisibleHierarchyTreeNodeCount(oFilterContext)// To get updated count
        .then(_handleTaxonomyHierarchyViewModeToggled.bind(this, "thumbnailViewMode", {}, oExtraData)); // To get
    // updated Data
  };

  let _pasteSelectedEntityInTaxonomyHierarchy = function (sClickedHierarchyNodeId, oFilterContext) {
    let oScreenProps = ContentScreenProps.screen;
    let oCopiedEntityListData = CS.cloneDeep(oScreenProps.getHierarchyEntitiesToCopyOrCutData());//Clone is necessary
    let sCopiedFromNodeId = oCopiedEntityListData.hierarchyNodeId;
    if (sClickedHierarchyNodeId === sCopiedFromNodeId) {
      ContentUtils.showMessage(getTranslation().ENTITIES_ALREADY_EXISTS);
      return;
    }

    if (sClickedHierarchyNodeId === "-1") {
      ContentUtils.showMessage(getTranslation().PASTE_NOT_ALLOWED);
      return;
    }

    let aCopiedEntityList = oCopiedEntityListData.entityList;
    if (CS.isEmpty(aCopiedEntityList)) {
      ContentUtils.showMessage(getTranslation().NOTHING_SELECTED_TO_PASTE);
      return;
    }

    let fTaxonomyPasteCallbackFunction = function (oFilterContext) {
      let iResponseCount = oScreenProps.getMultipleTaxonomyHierarchyTypeChangeCallCounter();
      if (iResponseCount === 0) {
        oScreenProps.setHierarchyEntitiesToCopyOrCutData({});
        setTimeout(getUpdatedCountAndData.bind(this,oFilterContext), 1000);
      }
    };

    oScreenProps.setMultipleTaxonomyHierarchyTypeChangeCallCounter(CS.size(aCopiedEntityList));

    CS.forEach(aCopiedEntityList, function (oCopiedEntity) {
      var oADM = {
        addedTaxonomyIds: [sClickedHierarchyNodeId]
      };

      var oCallbackData = {
        droppedEntity: oCopiedEntity,
        isPaste: true,
        successMessage: getTranslation().COLLECTION_CONTENT_SUCCESSFULLY_ADDED,
        functionToExecute: fTaxonomyPasteCallbackFunction.bind(this, oFilterContext)
      };

      ContentUtils.getContentStore().handleApplyClassification(oADM, oCallbackData);
    })
  };

  let _handleTaxonomyTreeSearchData = function (oCallbackData, oExtraData) {
    let oFilterContext = oCallbackData.filterContext;
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    let oPostDataForFilter = ContentUtils.getAllInstancesRequestData(oCallbackData.filterContext)
    delete oPostDataForFilter.selectedTaxonomyIds;
    if (CS.isEmpty(oPostDataForFilter.moduleId)) {
      return;
    }
    let oURLData = {};
    oPostDataForFilter.isArchivePortal  = ContentUtils.getIsArchive();
    if (!CS.isEmpty(oExtraData)) {
      CS.assign(oPostDataForFilter, oExtraData);
    }

    let sContentId = "-1";
    let oData = {
      id: sContentId,
    };
    oPostDataForFilter.isArchivePortal = ContentUtils.getIsArchive();

    let aTreeList = oFilterStore.getTaxonomyTree();
    let oOldProps = {};
    CS.forEach(aTreeList, function (oTree) {
      var oVisualProps = oFilterStore.getTaxonomyVisualProps();
      oOldProps[oTree.id] = oVisualProps[oTree.id];
      oOldProps[oTree.id].isChecked = 0;
    });
    oFilterStore.setTaxonomyVisualProps(oOldProps);
    let sUrl = getRequestMapping().GetAllKlassTaxonomies;
    oExtraData.searchText = oExtraData.searchText + "";
    oPostDataForFilter.allSearch = oExtraData.searchText;
    if (oExtraData.searchText.length === 0) {
      _addStaticCollectionIdIfExists(oPostDataForFilter);
    }
    oExtraData.isSearchResult = true;
    oCallbackData.isSearchTaxonomy = true;
    oCallbackData.clickedTaxonomyId = oPostDataForFilter.clickedTaxonomyId === "" ? "-1" : oPostDataForFilter.clickedTaxonomyId;
    let fSuccess = successFetchTaxonomyCallBack.bind(this, oCallbackData, oExtraData);
    let fFailure = ContentUtils.getContentStore().failureFetchContentListCallBack.bind(this, oCallbackData);
    CS.postRequest(sUrl, oData, oPostDataForFilter, fSuccess, fFailure, oURLData);
  };

  let _handleStaticCollectionOrganiseButtonClicked = function (sClickedNodeId, oFilterContext) {
    _clearContentHierarchyRelatedData();
    let oAppData = ContentUtils.getAppData();
    let oScreenProps = ContentScreenProps.screen;
    const oCollectionViewProps = ContentScreenProps.collectionViewProps;

    let iLevel = oAppData.getActiveStaticCollectionLevel();
    let oStaticCollectionMap = oAppData.getStaticCollectionMap();
    let oCollectionList = oStaticCollectionMap[iLevel];
    let aCollectionList = oCollectionList.collectionList;

    let sBreadCrumbNodeLabel = "";

    let aListToSet = [];
    if (sClickedNodeId === "noId") {
      aListToSet = aCollectionList;
      sClickedNodeId = aCollectionList[0].id;
      oScreenProps.setIsMasterCollectionListOrganiseClicked(true);
      sBreadCrumbNodeLabel = ContentUtils.getDecodedTranslation(getTranslation().ENTITY_HIERARCHY,{ entity : getTranslation().COLLECTION });
    } else {
      let oClickedCollection = CS.find(aCollectionList, {id: sClickedNodeId});
      aListToSet = [oClickedCollection];
      oScreenProps.setIsMasterCollectionListOrganiseClicked(false);
      sBreadCrumbNodeLabel = ContentUtils.getDecodedTranslation(getTranslation().ENTITY_HIERARCHY,{ entity : getTranslation().COLLECTION }) + " : " + oClickedCollection.label;
    }

    let oTreeNodeToSet = {
      id: 999,
      label: getTranslation().COLLECTION_LABEL,
      children: aListToSet
    };

    oScreenProps.setIsCollectionHierarchySelected(true);
    oCollectionViewProps.setBreadCrumbPath([]);
    _prepareContentHierarchyDataProps(oTreeNodeToSet);

    let oRequiredData = {
      clickedNodeId: sClickedNodeId,
      level: 0,
      selectedContext: HierarchyTypesDictionary.COLLECTION_HIERARCHY,
      breadCrumbNodeLabel: sBreadCrumbNodeLabel,
    };
    let iActiveStaticCollectionLevel = oAppData.getActiveStaticCollectionLevel();
    let bShowPublicPrivateModeButton = iActiveStaticCollectionLevel === 0;
    oCollectionViewProps.setPublicPrivateModeButtonVisibility(bShowPublicPrivateModeButton);
    _handleContentCollectionHorizontalTreeNodeClicked(oRequiredData, oFilterContext);
  };

  let _clearContentHierarchyRelatedData = function () {
    let oScreenProps = ContentScreenProps.screen;
    oScreenProps.setContentHierarchyVisualProps({});
    oScreenProps.setContentHierarchyTree({});

    //TODO: Clear XRay Properties
    ContentUtils.clearXRayData();
    ContentUtils.clearXRayDataFromBreadCrumbPayLoad();

    let bIsFilterHierarchySelected = oScreenProps.getIsFilterHierarchySelected();
    if (ContentUtils.getSelectedHierarchyContext() && !bIsFilterHierarchySelected) {
      let sDefaultViewMode = oScreenProps.getDefaultViewMode();
      oScreenProps.setViewMode(sDefaultViewMode);
    }
    if (oScreenProps.getThumbnailMode() === ThumbnailModeConstants.XRAY) {
      oScreenProps.setThumbnailMode(ThumbnailModeConstants.BASIC);
    }
    oScreenProps.setIsTaxonomyHierarchySelected(false);
    oScreenProps.setIsCollectionHierarchySelected(false);
    oScreenProps.setActiveHierarchyCollection({});
    oScreenProps.setActiveHierarchyNodeLevel(0);
    oScreenProps.setActiveHierarchyNodeId("");
    oScreenProps.setHierarchyDetailViewMode("sectionViewMode");
    oScreenProps.setSectionInnerZoom(ContentUtils.getDefaultZoom());
    oScreenProps.setHierarchyEntitiesToCopyOrCutData({});
    oScreenProps.setRightClickedThumbnailModel({});
    oScreenProps.setRightClickedHierarchyTreeNodeData({});
    oScreenProps.setSelectedOuterParentContentHierarchyTaxonomyId("");
  };

  let _prepareContentHierarchyDataProps = function (oTreeNodeToSet) {
    let oScreenProps = ContentScreenProps.screen;
    oScreenProps.setContentHierarchyTree(oTreeNodeToSet);

    let oHierarchyVisualProps = {};
    oHierarchyVisualProps[oTreeNodeToSet.id] = {isCollapsed: false};
    let aKeyValue = [
      {key: "isCollapsed", value: true},
      {key: "isSelected", value: false},
      {key: "noChildren", value: false},
      {key: "isActive", value: false},
      {key: "canDelete", value: false}
    ];
    ContentUtils.addNewTreeNodesToValueList(oHierarchyVisualProps, oTreeNodeToSet.children, aKeyValue);
    oScreenProps.setContentHierarchyVisualProps(oHierarchyVisualProps);
  };

  let _handleOrganiseTaxonomyButtonClicked = function (oFilterContext) {
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    let oFilterProps = oFilterStore.getFilterProps();
    let aTaxonomyTree = CS.cloneDeep(oFilterProps.getTaxonomyTree());

    let aAllEntityDummyNode = {
      id: ContentUtils.getHierarchyDummyNodeId(),
      label: getTranslation().ALL,
      count: aTaxonomyTree[0].count,
      children: []
    };

    aTaxonomyTree.unshift(aAllEntityDummyNode);

    let oTreeNodeToSet = {
      id: 999,
      label: getTranslation().TAXONOMIES,
      children: aTaxonomyTree
    };

    ContentScreenProps.screen.setIsTaxonomyHierarchySelected(true);
    _prepareContentHierarchyDataProps(oTreeNodeToSet);
    ContentUtils.setActiveContent({});
    ContentUtils.clearXRayData();
    let sBreadCrumbNodeLabel = ContentUtils.getDecodedTranslation(getTranslation().ENTITY_HIERARCHY,{ entity : getTranslation().TAXONOMY });

    /** on organise button click, it should redirect to 'Class Taxonomy' node */

    let oReqData = {
      level: 0,
      clickedNodeId: ContentUtils.getHierarchyDummyNodeId(),
      selectedContext: HierarchyTypesDictionary.TAXONOMY_HIERARCHY,
      breadCrumbNodeLabel: sBreadCrumbNodeLabel,
    };
    return _handleContentTaxonomyHorizontalTreeNodeClicked(oReqData, oFilterContext);
  };


  let _handleCollectionPCDeleted = function (sSectionId) {
    let oActiveHierarchyCollection = _makeActiveHierarchyCollectionDirty();
    ContentUtils.handleSectionDeleted(oActiveHierarchyCollection, sSectionId);
    _saveHierarchyCollection({});
  };

  let _handleSectionSkipToggled = function (sSectionId) {
    let oActiveHierarchyCollection = _makeActiveHierarchyCollectionDirty();
    let oSection = CS.find(oActiveHierarchyCollection.sections, {id: sSectionId});
    if (oSection && oSection.isInherited) {
      oSection.isSkipped = !oSection.isSkipped;
    }
    _saveHierarchyCollection({});
  };

  let _handleCollectionPCElementInputChanged = function (sSectionId, sElementId, sProperty, sNewValue) {
    let oScreenProps = ContentScreenProps.screen;
    let oActiveHierarchyCollection = oScreenProps.getActiveHierarchyCollection();
    oActiveHierarchyCollection = oActiveHierarchyCollection.clonedObject ? oActiveHierarchyCollection.clonedObject : oActiveHierarchyCollection;
    let oSection = CS.find(oActiveHierarchyCollection.sections, {id: sSectionId});
    let oElement = CS.find(oSection.elements, {id: sElementId});
    if (sProperty === "propertyType" && oElement && oElement.type === "tag") {
      sProperty = "tagType";
    }

    if (oElement[sProperty] !== sNewValue) {
      let oDirtyActiveHierarchyCollection = _makeActiveHierarchyCollectionDirty();
      let oDirtySection = CS.find(oDirtyActiveHierarchyCollection.sections, {id: sSectionId});
      let oDirtyElement = CS.find(oDirtySection.elements, {id: sElementId});
      oDirtyElement[sProperty] = sNewValue;
    }

    _triggerChange();
  };

  let _discardHierarchyCollection = function () {
    let oScreenProps = ContentScreenProps.screen;
    let oActiveHierarchyCollection = oScreenProps.getActiveHierarchyCollection();
    if (!oActiveHierarchyCollection.clonedObject) {
      alertify.message(getTranslation().NOTHING_CHANGED_TO_DISCARD);
    } else {
      delete oActiveHierarchyCollection.clonedObject;
      alertify.success(getTranslation().DISCARDED_SUCCESSFULLY);
    }
    _triggerChange();
  };

  let _handleCollectionHierarchySnackbarButtonClicked = function (sButtonContext) {
    if (sButtonContext === "save") {
      _saveHierarchyCollection({});
    } else {
      _discardHierarchyCollection();
    }
  };

  /************************************* Public API's **********************************************/
  return {

    handleCollectionHierarchyViewModeToggled: function (sViewMode, oReqData, bIsNodeClickCall, oFilterContext) {
      _handleCollectionHierarchyViewModeToggled(sViewMode, oReqData, bIsNodeClickCall, oFilterContext);
    },

    handleCollectionHierarchyNodeDeleteAfterEffects: function (sDeletedId) {
      _handleCollectionHierarchyNodeDeleteAfterEffects(sDeletedId);
    },

    updateHierarchyTreeAfterAddingCollectionNode: function (sParentId, oFilterContext, oResponse) {
      _updateHierarchyTreeAfterAddingCollectionNode(sParentId, oFilterContext, oResponse);
    },

    copyOrCutSelectedEntitiesInHierarchy: function (sMenuContext) {
      _copyOrCutSelectedEntitiesInHierarchy(sMenuContext);
    },

    handleTaxonomyHierarchyViewModeToggled: function (sViewMode, oClickedNode, oExtraData) {
      return _handleTaxonomyHierarchyViewModeToggled(sViewMode, oClickedNode, oExtraData);
    },

    handleContentTaxonomyHorizontalTreeNodeClicked: function (oReqData, oFilterContext) {
      _handleContentTaxonomyHorizontalTreeNodeClicked(oReqData, oFilterContext);
    },

    fetchVisibleHierarchyTreeNodeCount: function (oFilterContext) {
      return _fetchVisibleHierarchyTreeNodeCount(oFilterContext);
    },

    pasteCopiedSelectedEntityInTaxonomyHierarchy: function (sClickedHierarchyNodeId, oFilterContext) {
      _pasteSelectedEntityInTaxonomyHierarchy(sClickedHierarchyNodeId, oFilterContext);
    },

    handleParentTaxonomyClicked: function (oCallBackData, oExtraData) {
      _handleParentTaxonomyClicked(oCallBackData, oExtraData);
    },

    handleTaxonomyTreeSearchData: function (oCallbackData, oExtraData) {
      _handleTaxonomyTreeSearchData(oCallbackData, oExtraData);
    },

    handleTaxonomyChildrenLazyData: function (oCallBackData, oExtraData) {
      _handleTaxonomyChildrenLazyData(oCallBackData, oExtraData);
    },

    handleOrganiseTaxonomyButtonClicked: function (oFilterContext) {
      return _handleOrganiseTaxonomyButtonClicked(oFilterContext);
    },

    clearContentHierarchyRelatedData: function () {
      _clearContentHierarchyRelatedData();
    },

    handleStaticCollectionOrganiseButtonClicked: function (sClickedNodeId, oFilterContext) {
      _handleStaticCollectionOrganiseButtonClicked(sClickedNodeId, oFilterContext);
    },

    makeActiveHierarchyCollectionDirty: function () {
      return _makeActiveHierarchyCollectionDirty();
    },

    handleCollectionPCDeleted: function (sSectionId) {
      _handleCollectionPCDeleted(sSectionId);
    },

    handleSectionSkipToggled: function (sSectionId) {
      _handleSectionSkipToggled(sSectionId);
    },

    handleCollectionPCElementInputChanged: function (sSectionId, sElementId, sProperty, sNewValue) {
      _handleCollectionPCElementInputChanged(sSectionId, sElementId, sProperty, sNewValue);
    },

    handleCollectionHierarchySnackbarButtonClicked: function (sButtonContext) {
      _handleCollectionHierarchySnackbarButtonClicked(sButtonContext);
    },

  }
})();

MicroEvent.mixin(CollectionAndTaxonomyDataNavigationHelperStore);

export default CollectionAndTaxonomyDataNavigationHelperStore;
