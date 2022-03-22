
import CS from '../../../../../../libraries/cs';
import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import ScreenModeUtils from './screen-mode-utils';
import SessionStorageManager from './../../../../../../libraries/sessionstoragemanager/session-storage-manager';
import ContentUtils from './content-utils';
import NumberUtils from '../../../../../../commonmodule/util/number-util';
import AttributeUtils from '../../../../../../commonmodule/util/attribute-utils';
import NumberFormatDictionary from '../../../../../../commonmodule/tack/number-format-dictionary';
import TimelineProps from './../model/timeline-props';
import RequestMapping from '../../../../../../libraries/requestmappingparser/request-mapping-parser.js';
import BaseTypesDictionary from '../../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import TemplateTabsDictionary from '../../../../../../commonmodule/tack/template-tabs-dictionary';
import TagTypeConstants from '../../../../../../commonmodule/tack/tag-type-constants';
import RelationshipTypeDictionary from '../../../../../../commonmodule/tack/relationship-type-dictionary';
import CouplingConstants from '../../../../../../commonmodule/tack/coupling-constans';
import UniqueIdentifierGenerator from './../../../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import ContentScreenConstants from './../model/content-screen-constants';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import ExceptionLogger from '../../../../../../libraries/exceptionhandling/exception-logger';
import AttributeTypeDictionary from '../../../../../../commonmodule/tack/attribute-type-dictionary-new';
import SessionProps from '../../../../../../commonmodule/props/session-props';
import SessionStorageConstants from '../../../../../../commonmodule/tack/session-storage-constants';
const oNumberFormatDictionary = new NumberFormatDictionary();
var getTranslation = ScreenModeUtils.getTranslationDictionary;
var getRequestMapping = ScreenModeUtils.getScreenRequestMapping;


var TimelineStore = (function () {

  var _triggerChange = function () {
    TimelineStore.trigger('timeline-change');
  };

  /******************* SERVER CALLBACKS *******************/
  var successFetchTimelineComparisonData = function (oCallback, oResponse) {
    oResponse = oResponse.success;
    var oApData = ContentUtils.getAppData();
    var oContentStore = ContentUtils.getContentStore();
    var oActiveContent = oResponse.klassInstance;
    var oComponentProps = ContentUtils.getComponentProps();
    var oScreenProps = oComponentProps.screen;
    var oConfigDetails = oResponse.configDetails;
    if(oCallback && oCallback.preFunctionToExecute) {
      oCallback.preFunctionToExecute();
    }
    oContentStore.setContentOpenReferencedData(oResponse, oActiveContent);

    var sActiveVersionId = oActiveContent.versionId;
    var oActiveVersion = CS.find(oResponse.versions, {versionId: sActiveVersionId});
    if(oActiveVersion) {
      if(!CS.isEmpty(oActiveContent.context)){
        oActiveVersion.context = oActiveContent.context;
        oActiveVersion.timeRange = oActiveContent.timeRange;
        oActiveVersion.linkedInstances = oActiveContent.linkedInstances;
        oActiveVersion.variantInstanceId = oActiveVersion.id;
        oActiveVersion.variantsTimeRange = oActiveContent.variantsTimeRange;
      }

      let oContentRelationships = oResponse.contentRelationships;
      oActiveVersion.contentRelationships =
          !CS.isEmpty(oContentRelationships[sActiveVersionId]) && !CS.isEmpty(oContentRelationships[sActiveVersionId].relationships)
              ? oContentRelationships[sActiveVersionId].relationships : [];

      let oNatureRelationships = oResponse.natureRelationships;
      oActiveVersion.natureRelationships =
          !CS.isEmpty(oNatureRelationships[sActiveVersionId]) && !CS.isEmpty(oNatureRelationships[sActiveVersionId].relationships)
              ? oNatureRelationships[sActiveVersionId].relationships : [];

      oActiveVersion.eventSchedule = oActiveContent.eventSchedule;
      if (CS.isEmpty(oActiveVersion.eventSchedule) && oActiveVersion.baseType === BaseTypesDictionary["assetBaseType"]) {
        oActiveVersion.eventSchedule = {
          "startTime": null,
          "endTime": null,
        };
      }
      oScreenProps.setReferencedNatureRelationships(oConfigDetails.referencedNatureRelationships);
      ContentUtils.setRelationshipElements(oActiveVersion);
      ContentUtils.setActiveContent(oActiveVersion);

      CS.forEach(oConfigDetails.referencedElements, (oElement) => {
        let sRelationshipTypeRelationship = ContentUtils.getRelationshipTypeById(oElement.propertyId);
        let bIsNatureRelationship = ContentUtils.isNatureRelationship(sRelationshipTypeRelationship) ||
            ContentUtils.isVariantRelationship(sRelationshipTypeRelationship);
        if(oElement.type === "relationship") {
          ContentUtils.addRelationshipDummyEntity(oElement.propertyId, oActiveVersion, oElement.id, bIsNatureRelationship);
        }
      });
    }

    _makeDataForComparison(oResponse);

    try {
      //Set available entities for relationship to send added elements with their version number
      var oReferenceRelationshipInstanceElements = oResponse.referenceRelationshipInstanceElements;
      oApData.setAvailableEntities(CS.flatMap(oReferenceRelationshipInstanceElements));
    }
    catch (oException) {
      ExceptionLogger.error("Error in setting available list for comparison");
      ExceptionLogger.error(oException);
    }

    TimelineProps.setVersionComparisionLanguages(oResponse.versionComparisionLanguages);
    TimelineProps.setIsComparisonMode(true);

    if(oCallback && oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
    _triggerChange();
  };

  var failureFetchTimelineComparisonData = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureFetchTimelineComparisonData', getTranslation());
  };

  var successArchiveVersionsCallback = function (oResponse) {
    var aVersions = TimelineProps.getVersions();
    var aSelectedVersions = TimelineProps.getSelectedVersionIds();

    CS.forEach(oResponse.versionNumbers, function (sId) {
      //TODO: Instead we should send get call with pagination to fetch latest version lists
      CS.remove(aVersions, function (oVersion) {
         return oVersion.versionNumber == sId;
      });

      CS.remove(aSelectedVersions, function (sVersionId) { return sVersionId == sId});
    });

    ContentUtils.showSuccess(ContentUtils.getDecodedTranslation(getTranslation().ENTITY_SUCCESSFULLY_DELETED , { entity : getTranslation().REVISIONS } ));
    _triggerChange();
  };

  var failureArchiveVersionsCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureArchiveVersionsCallback', getTranslation());
  };

  /******************* PRIVATE API  *******************/
  var _getTableSkeletonData = function () {
    return {
      rowData: [],
      columnData: [],
      firstColumnWidth: 250,
      tableName: "",
      tableId: ""
    }
  };

  var _getLayoutSkeleton = function () {
    return {
      attributeTable: {},
      tagTable: {},
      relationshipTable: {},
      headerInformationTable: {},
      fixedHeaderTable: {},
      showPropertiesTab: false,
      showRelationshipsTab: false
    };
  };

  let _makeComparisonRowDataForPropertyCollections = function (oLayoutData, sPropertyId, oPropertyCollection, oConfigDetails, oUsedElement) {
    var oReferencedElements = oConfigDetails.referencedElements;
    var oReferencedAttributes = oConfigDetails.referencedAttributes;
    var oReferencedTags = oConfigDetails.referencedTags;
    let bIsContentEditable = !ContentUtils.getIsContentDisabled();

    try {

      CS.forEach(oPropertyCollection.elements, function (oElement) {
        var sElementId = oElement.id;
        if (oUsedElement[sElementId]) {
          return true; // Skip if already data is processed for the same element
        }

        try {

          oElement = oReferencedElements[sElementId];
          var oRowObject = {};
          var sElementType = oElement.type;

          switch (sElementType) {
            case "attribute" :
              var oMasterAttribute = oReferencedAttributes[sElementId];
              if(oMasterAttribute.id === "nameattribute") {
                break;
              }
              var bIsAttributeDisabled =
                  !bIsContentEditable ||
                  oElement.isDisabled ||
                  oElement.couplingType === CouplingConstants.DYNAMIC_COUPLED ||
                  oElement.couplingType === CouplingConstants.READ_ONLY_COUPLED ||
                  (oMasterAttribute.type === AttributeTypeDictionary.CONCATENATED)||
                  (oMasterAttribute.type === AttributeTypeDictionary.CALCULATED);

              oRowObject = {
                id: oMasterAttribute.id,
                label: CS.getLabelOrCode(oMasterAttribute),
                type: sElementType,
                masterAttribute: oMasterAttribute,
                isDisabled: bIsAttributeDisabled,
                rendererType: ContentUtils.getAttributeTypeForVisual(oMasterAttribute.type)
              };

              if(!oLayoutData.attributeTable['attribute']) { //TODO: Harcoded Code
                var oTableSkeletonData = _getTableSkeletonData();
                oTableSkeletonData.tableId = 'attribute';
                oTableSkeletonData.tableName = getTranslation().ATTRIBUTES;

                oLayoutData.attributeTable['attribute'] = oTableSkeletonData;
              }
              oLayoutData.attributeTable['attribute'].rowData.push(oRowObject);
              break;

            case "tag":
              var oMasterTag = oReferencedTags[sElementId];
              var bIsTagDisabled =
                  !bIsContentEditable ||
                  oElement.isDisabled ||
                  oElement.couplingType == CouplingConstants.DYNAMIC_COUPLED ||
                  oElement.couplingType == CouplingConstants.READ_ONLY_COUPLED;

              CS.forEach(oMasterTag.children, function (oChildMaster) {
                let sChildLabel = CS.getLabelOrCode(oChildMaster);
                if(oMasterTag.tagType === TagTypeConstants.TAG_TYPE_BOOLEAN && CS.isEmpty(oChildMaster.label)) {
                  sChildLabel = CS.getLabelOrCode(oMasterTag);
                }
                oRowObject = {
                  id: oChildMaster.id,
                  label: sChildLabel,
                  type: sElementType,
                  isDisabled: bIsTagDisabled,
                  rendererType: 'CHECK',
                  iconKey: oChildMaster.iconKey
                };

                if(!oLayoutData.tagTable[oMasterTag.id]) {
                  var oTableSkeletonData = _getTableSkeletonData();
                  oTableSkeletonData.tableId = oMasterTag.id;
                  oTableSkeletonData.tableName = CS.getLabelOrCode(oMasterTag);
                  oTableSkeletonData.iconKey = oMasterTag.iconKey;
                  oLayoutData.tagTable[oMasterTag.id] = oTableSkeletonData;
                }

                oLayoutData.tagTable[oMasterTag.id].rowData.push(oRowObject);
              });

              break;

              /**We can add Minor taxonomy in property collection **/
            case "taxonomy":
              let oReferencedMinorTaxonomy = oConfigDetails.referencedTaxonomies;
              let oMinorTaxonomy = oReferencedMinorTaxonomy[oElement.id];
              let aMinorTaxonomiesChildren = oMinorTaxonomy.children;
              CS.forEach(aMinorTaxonomiesChildren, function (oChildren) {
                let oReferencedTaxonomy = oReferencedMinorTaxonomy[oChildren.id];

                let aPropertyCollections = (oReferencedTaxonomy && oReferencedTaxonomy.propertyCollections) || [];
                CS.forEach(aPropertyCollections, function (sPropertyCollectionId) {
                  let aReferencedPropertyCollection = oConfigDetails.referencedPropertyCollections;
                  let oPropertyCollection = aReferencedPropertyCollection[sPropertyCollectionId];
                  _makeComparisonRowDataForPropertyCollections(oLayoutData, "", oPropertyCollection, oConfigDetails, oUsedElement);
                });
              });
              break;
          }

          oUsedElement[sElementId] = true; //Flag so that it won't get processed again
        }
        catch (oException) {
          ExceptionLogger.error(oException);
        }
      });
    }
    catch (oException) {
      ExceptionLogger.error(oException);
    }
  };

  var _makeComparisonRowDataForCustomTab = function (oLayoutData, oReferencedTemplates, oConfigDetails, oResponse) {
    try {
      let oReferencedDetails = {
        referencedPropertyCollections: oConfigDetails.referencedPropertyCollections,
        referencedRelationships: oConfigDetails.referencedRelationships || {},
        referencedNatureRelationships: oConfigDetails.referencedNatureRelationships || {},
      };
      let oSectionTypes = ContentScreenConstants.sectionTypes;
      let oUsedElement = {};

      CS.forEach(oReferencedTemplates.tabs, function (oTab) {
        if(oTab.baseType === TemplateTabsDictionary.CUSTOM_TAB) {
          CS.forEach(oTab.propertySequenceList, function (sectionData) {
            let sSectionId = "";
            let sIdForFetching = "";
            if(sectionData.relationshipId){
              sIdForFetching = sectionData.relationshipId;
              sSectionId = sectionData.sideId;
            }else {
              sSectionId = sIdForFetching = sectionData;
            }

            //determine the type of section
            let {oReferencedData, sSectionType} = ContentUtils.getDesiredReferencedDataAndTypeBySectionId(sIdForFetching, oReferencedDetails);

            switch (sSectionType) {
              case oSectionTypes.SECTION_TYPE_PROPERTY_COLLECTION:
                _makeComparisonRowDataForPropertyCollections(oLayoutData, sSectionId, oReferencedData, oConfigDetails, oUsedElement);
                break;

              case oSectionTypes.SECTION_TYPE_RELATIONSHIP:
                break;

              case oSectionTypes.SECTION_TYPE_NATURE_RELATIONSHIP:
                break;
            }
          });
        }
      });

      _makeComparisonRowDataForRelationshipTab(oLayoutData, oResponse);
      _makeComparisonRowDataForNatureRelationships(oLayoutData, oResponse);

    }
    catch (oException) {
      ExceptionLogger.error(oException);
    }
  };

  var _getAssetDataObject = function (oReferencedAsset) {
    var oAssetData = {};
    try {
      var sExtention = '';
      var sThumbKeySRC = '';
      var sImageSrc = '';
      var sPreviewSrc = '';
      let sMp4Src = '';

      if(!CS.isEmpty(oReferencedAsset)) {
        if(oReferencedAsset.thumbKey) {
          sThumbKeySRC = RequestMapping.getRequestUrl(getRequestMapping().GetAssetImage, {
            type: oReferencedAsset.type,
            id: oReferencedAsset.thumbKey
          });
        }

        if(oReferencedAsset.assetObjectKey) {
          sImageSrc = RequestMapping.getRequestUrl(getRequestMapping().GetAssetImage, {
            type: oReferencedAsset.type,
            id: oReferencedAsset.assetObjectKey
          });
        }

        if(oReferencedAsset.previewImageKey) {
          sPreviewSrc = RequestMapping.getRequestUrl(getRequestMapping().GetAssetImage, {
            type: oReferencedAsset.type,
            id: oReferencedAsset.previewImageKey
          });
        }

        if(!CS.isEmpty(oReferencedAsset.properties.mp4)){
          sMp4Src = RequestMapping.getRequestUrl(getRequestMapping().GetAssetImage, {
            type: oReferencedAsset.type,
            id: oReferencedAsset.properties.mp4
          });
        }

        sExtention = oReferencedAsset.properties && oReferencedAsset.properties.extension || '';
      }

      oAssetData = {
        fileName: oReferencedAsset && oReferencedAsset.fileName || "",
        thumbKeySrc: sThumbKeySRC,
        imageSrc: sImageSrc,
        extension: sExtention,
        assetObjectKey: oReferencedAsset && oReferencedAsset.assetObjectKey || "",
        previewSrc: sPreviewSrc,
        mp4Src: sMp4Src
      };
    }
    catch(oException) {
      ExceptionLogger.error(oException);
    }

    return oAssetData;
  };

  var _getElementAssetData = function (oElement) {
    var oElementAssetData = {};
    try {
      var oReferencedAsset = !CS.isEmpty(oElement.referencedAssets) && CS.find(oElement.referencedAssets, {isDefault: true}) || {};
      if (oElement && oElement.baseType == BaseTypesDictionary.assetBaseType) {
        var oAssetAttribute = oElement.assetInformation;
        if (oAssetAttribute) {
          oReferencedAsset = {
            previewImageKey: oAssetAttribute.previewImageKey,
            fileName: oAssetAttribute.fileName,
            assetObjectKey: oAssetAttribute.assetObjectKey,
            assetInstanceId: oElement.id,
            isDefault: true,
            label: oElement.name,
            properties: oAssetAttribute.properties,
            thumbKey: oAssetAttribute.thumbKey,
            type: oAssetAttribute.type
          }
        }
      }

      oElementAssetData = _getAssetDataObject(oReferencedAsset);
    }
    catch(oException) {
      ExceptionLogger.error(oException);
    }

    return oElementAssetData;
  };

  var _makeComparisonRowDataForNatureRelationships = function (oLayoutData, oResponse) {
    var oConfigDetails = oResponse.configDetails;
    var oReferencedNatureRelationships = oConfigDetails.referencedNatureRelationships;
    var oReferencedElements = oConfigDetails.referencedElements;
    var aVersions = oResponse.versions;
    var oReferencedNatureRelationshipInstances = oResponse.referenceNatureRelationshipInstanceElements;
    var aRelationshipIds = [];
    CS.forEach(oReferencedNatureRelationships, function (oRelationship) {
      aRelationshipIds.push(oRelationship.id);
    });
    var oNatureRelationships = oResponse.natureRelationships;
    _makeRelationshipComparisonData(aRelationshipIds, oReferencedNatureRelationships, "natureRelationships", oReferencedElements,
        oReferencedNatureRelationshipInstances, oLayoutData, aVersions, oNatureRelationships);
  };

  var _makeComparisonRowDataForRelationshipTab = function (oLayoutData, oResponse) {
    var oConfigDetails = oResponse.configDetails;
    var oReferencedRelationships = oConfigDetails.referencedRelationships;
    var oReferencedElements = oConfigDetails.referencedElements;
    var aVersions = oResponse.versions;
    var oReferencedRelationshipInstances = oResponse.referenceRelationshipInstanceElements;
    let aRelationshipConflictingValues = oResponse.klassInstance.relationshipConflictingValues;
    var aRelationshipIds = [];
    CS.forEach(oReferencedRelationships, function (oRelationship) {
      aRelationshipIds.push(oRelationship.id);
    });
    var oContentRelationships = oResponse.contentRelationships;
    _makeRelationshipComparisonData(aRelationshipIds, oReferencedRelationships, "contentRelationships",
        oReferencedElements, oReferencedRelationshipInstances, oLayoutData, aVersions, oContentRelationships, aRelationshipConflictingValues);
  };

  var _makeLifeCycleStatusTagData = function (oLayoutData, oResponse, oHeaderPermission) {
    let oConfigDetails = oResponse.configDetails;
    let aReferencedLifeCycleStatusTags = oConfigDetails.referencedLifeCycleStatusTags;
    let oReferencedTags = oConfigDetails.referencedTags;

    CS.forEach(aReferencedLifeCycleStatusTags, function (sTagId) {
      let oTag = oReferencedTags[sTagId];
      let oRowObject = {
        id: oTag.id,
        label: CS.getLabelOrCode(oTag),
        type: "lifeCycleStatusTag",
        rendererType: "lifeCycleStatusTag",
        isDisabled: !oHeaderPermission.canEditStatusTag
      };

      oLayoutData.headerInformationTable["headerInformation"].rowData.push(oRowObject);
    });

  };

  var _getTaxonomyRowData = function () {
    return {
      id: "taxonomy",
      label: getTranslation().TAXONOMIES,
      type: "taxonomy",
      rendererType: "taxonomy",
      isDisabled: true, //TODO: 'bIsDisabled' is hardcoded for now, later pass it as props
    };
  };

  var _getKlassRowData = function () {
    return {
      id: "type",
      label: getTranslation().CLASSES,
      type: "type",
      rendererType: "type",
      isDisabled: true, //TODO: 'bIsDisabled' is hardcoded for now, later pass it as props
    };
  };

  var _getNameRowData = function (oHeaderPermission, oReferencedAttributes) {
    return {
      id: "name",
      label: getTranslation().NAME,
      type: "name",
      rendererType: "name",
      masterAttribute: oReferencedAttributes["nameattribute"],
      isDisabled: !oHeaderPermission.canEditName
    };
  };

  var _getDefaultImageRowData = function (oHeaderPermission) {
    return {
      id: "image",
      label: getTranslation().DEFAULT_IMAGE,
      type: "image",
      isDisabled: oHeaderPermission ? !oHeaderPermission.canChangeIcon : true,
      rendererType: "image",
    }
  };

  var _getEventScheduleRowData = function () {
    return {
      id: "eventSchedule",
      label: getTranslation().VALIDITY_INFO,
      type: "eventSchedule",
      isDisabled: true,
      rendererType: "eventSchedule"
    };
  };

  var _makeComparisonRowDataForHeaderInformation = function (oLayoutData, oResponse) {
    let oConfigDetails = oResponse.configDetails;
    let oReferencedPermissions = oConfigDetails.referencedPermissions;
    let oReferencedAttributes = oConfigDetails.referencedAttributes;
    let oHeaderPermission = oReferencedPermissions["headerPermission"];

    try {
      if (!oLayoutData.headerInformationTable['headerInformation']) {
        let oTableSkeletonData = _getTableSkeletonData();
        oTableSkeletonData.tableId = 'headerInformation';
        oTableSkeletonData.tableName = getTranslation().HEADER_INFORMATION;
        oLayoutData.headerInformationTable['headerInformation'] = oTableSkeletonData;
      }

      let oRowObject = _getTaxonomyRowData(oHeaderPermission);
      oLayoutData.headerInformationTable['headerInformation'].rowData.push(oRowObject);

      oRowObject = _getKlassRowData(oHeaderPermission);
      oLayoutData.headerInformationTable['headerInformation'].rowData.push(oRowObject);

      oRowObject = _getNameRowData(oHeaderPermission, oReferencedAttributes);
      oLayoutData.headerInformationTable['headerInformation'].rowData.push(oRowObject);

      oRowObject = _getDefaultImageRowData(oHeaderPermission);
      oLayoutData.headerInformationTable['headerInformation'].rowData.push(oRowObject);

      oRowObject = _getEventScheduleRowData();
      oLayoutData.headerInformationTable['headerInformation'].rowData.push(oRowObject);

      _makeLifeCycleStatusTagData(oLayoutData, oResponse, oHeaderPermission);

    } catch (oException) {
      ExceptionLogger.error(oException);
    }
  };

  var _makeRelationshipComparisonData = function (aRelationshipIds, oReferencedRelationships, sKlassInstanceRelationshipKey,
                                                  oReferencedElements, oReferencedRelationshipInstances, oLayoutData,
                                                  aVersions, oRelationships, aRelationshipConflictingValues) {
    try {

      let bIsContentEditable = !ContentUtils.getIsContentDisabled();
      CS.forEach(aRelationshipIds, function (sRelationshipId) {
        try {

          //TODO: Need to get coupling info from referencedElements in 19.1-SR1
          /** Todo : Temp fix : coupling type for relationship should get into referencedElements but currently it has been handled through relationshipConflictingValues **/
          let oRelationshipConflict = {};
          let oRelationship = CS.find(aRelationshipConflictingValues, function (oRelationship) {
            return oRelationship.propagableRelationshipId === sRelationshipId
          })
          oRelationshipConflict = CS.size(oRelationship) !== 0 ? oRelationship.conflicts[0] : {};

          var oMasterRelationship = oReferencedRelationships[sRelationshipId];
          var aReferencedElements = CS.filter(oReferencedElements, {propertyId: oMasterRelationship.id});
          CS.forEach(aReferencedElements, function (oReferencedElement) {
            if (!oLayoutData.relationshipTable[oReferencedElement.id]) {
              var oTableSkeletonData = _getTableSkeletonData();
              oTableSkeletonData.tableId = oReferencedElement.id;

              let aMasterRelSides = [oMasterRelationship.side1, oMasterRelationship.side2];
              let oDesiredRelationshipSideMasterData = CS.find(aMasterRelSides, {id: oReferencedElement.relationshipSide.id});
              oTableSkeletonData.tableName = CS.getLabelOrCode(oDesiredRelationshipSideMasterData);

              oLayoutData.relationshipTable[oReferencedElement.id] = oTableSkeletonData;
            }

            if(CS.isEmpty(oReferencedElement)) {
              return;
            }

            var aElementIds = [];
            if (!CS.isEmpty(oRelationships)) {
              CS.forEach(aVersions, function (oVersion) {
                var oVersionRelationship = oRelationships[oVersion.versionId];
                var aRelationships = oVersionRelationship.relationships;
                CS.forEach(aRelationships, function (oRelationship) {
                  if (oRelationship.sideId === oReferencedElement.id) {
                    aElementIds = CS.union(aElementIds, oRelationship.elementIds);
                  }
                })
              });
            }

            var aReferencedRelationshipElements = oReferencedRelationshipInstances[oReferencedElement.id];
            CS.forEach(aElementIds, function (sElementId) {
              var oElement = CS.find(aReferencedRelationshipElements, {id: sElementId});

              if (CS.isEmpty(oElement)) {
                /** If your control is coming inside this block then, there is issue from backend - Shashank
                 * this check is added for safety purpose.
                 * */
                return;
              }
              var oElementReferenced = _getElementAssetData(oElement);
              let bIsDynamicCoupled = oRelationshipConflict.couplingType === "dynamicCoupled";

              var oRowObject = {
                id: sElementId,
                label: CS.getLabelOrCode(oElement),
                type: 'relationship',
                canAdd: bIsContentEditable && oReferencedElement.canAdd && !bIsDynamicCoupled ,
                canDelete: bIsContentEditable && oReferencedElement.canDelete && !bIsDynamicCoupled ,
                rendererType: 'ELEMENT',
              };

              if(!oLayoutData.relationshipTable[oReferencedElement.id].referencedAssetsData) {
                oLayoutData.relationshipTable[oReferencedElement.id].referencedAssetsData = {};
              }
              oLayoutData.relationshipTable[oReferencedElement.id].referencedAssetsData[oElement.id] = oElementReferenced;


              oLayoutData.relationshipTable[oReferencedElement.id].rowData.push(oRowObject);
            });
          });

        }
        catch (oException) {
          ExceptionLogger.error(oException);
        }
      });
    }
    catch (oException) {
      ExceptionLogger.error(oException);
    }
  };

  var _prepareReferencedTaxonomyMap = function (oReferencedTaxonomy, oTaxonomyMap) {
    if(!oTaxonomyMap[oReferencedTaxonomy.id]) {
      var oNewObj = {
        id: oReferencedTaxonomy.id,
        label: CS.getLabelOrCode(oReferencedTaxonomy),
        parentId: null
      };
      var oParent = oReferencedTaxonomy.parent;
      if(oParent) {
        oNewObj.parentId = oParent.id;
        _prepareReferencedTaxonomyMap(oParent, oTaxonomyMap);
      }
      oTaxonomyMap[oReferencedTaxonomy.id] = oNewObj;
    }
  };

  var _prepareSelectedTaxonomiesFromMap = function (sTaxonomyId, aSelectedTaxonomies, oTaxonomyMap) {
    if(oTaxonomyMap[sTaxonomyId]) {
      var oTaxonomy = oTaxonomyMap[sTaxonomyId];
      aSelectedTaxonomies.push(oTaxonomy);
      var sParentId = oTaxonomy.parentId;

      if(!CS.isEmpty(sParentId) && sParentId != -1) {
        _prepareSelectedTaxonomiesFromMap(sParentId, aSelectedTaxonomies, oTaxonomyMap);
      }
    }
  };

  let _getTableVisibility = function (aRows) {
    return CS.isEmpty(CS.find(aRows, {isSkipped: false}));
  };

  let _getSelectedNumberFormatForVersionComparison = function () {
    let sSelectedDLCodeForComparison = TimelineProps.getSelectedLanguageForComparison();
    let oLanguageInfoData = SessionProps.getLanguageInfoData();
    let oDataLanguages = oLanguageInfoData.dataLanguages;
    let oSelectedDataLanguage = CS.find(oDataLanguages, {code: sSelectedDLCodeForComparison});

    return oNumberFormatDictionary[oSelectedDataLanguage.numberFormat];
  };

  var _makeComparisonColumnData = function (oLayoutData, oResponse, aVersions) {
    var oConfigDetails = oResponse.configDetails;
    var oReferencedAttributes = oConfigDetails.referencedAttributes;
    var oReferencedTags = oConfigDetails.referencedTags;
    let oReferencedElements = oConfigDetails.referencedElements;
    var oAllReferencedRelationships = CS.clone(oConfigDetails.referencedRelationships);
    CS.assign(oAllReferencedRelationships, oConfigDetails.referencedNatureRelationships);
    var oReferencedContexts = oConfigDetails.referencedVariantContexts;
    var oActiveEntity = ContentUtils.getActiveEntity();
    var oReferencedTaxonomiesInfo = oResponse.referencedTaxonomiesInfo;
    var oReferencedKlassesInfo = oResponse.referencedKlassesInfo;
    var oReferencedAssets = oResponse.referencedAssets;

    let bIsContentEditable = !ContentUtils.getIsContentDisabled();
    let aActionItems = [];
    let aConcatenatedExpresionList = []

    if(bIsContentEditable) {
      aActionItems.push({
        id: "rollback",
        label: "Rollback",
        className: "rollback"
      });
    }
      let iColumnWidth = 250;


      CS.forEach(oLayoutData.attributeTable, function (oAttributeTableData) {

      CS.remove(oAttributeTableData.rowData, function (oRowData) {
        var sAttributeId = oRowData.id;
        var bFoundProperty = false;
        let bAllColumnsAreEqual = true;
        let sValueToCompareWith = "";
        let bIsTranslatable = oRowData.masterAttribute.isTranslatable;

        CS.forEach(aVersions, function (oVersion, iIndex) {
          var sVersionId = oVersion.versionId;
          let bIsEntityAvailableInDL = ContentUtils.isContentAvailableInSelectedDataLanguage(oVersion);

          var oAttribute = CS.find(oVersion.attributes, {attributeId: sAttributeId});

          var oVersionColumn = CS.find(oAttributeTableData.columnData, {id: sVersionId});
          if (!oVersionColumn) {
            oVersionColumn = {
              id: sVersionId,
              label: getTranslation().VERSION + sVersionId,
              isFixed: oActiveEntity.versionId == sVersionId,
              forComparison: oActiveEntity.versionId == sVersionId,
              width: iColumnWidth,
              properties: {},
              actionItems: oActiveEntity.versionId === sVersionId ? [] : aActionItems
            };

            oAttributeTableData.columnData.push(oVersionColumn);
          }

          var sValue = "";
          let sOriginalValue = "";
          var oReferencedAssetsData = null;
          var oCoverflowAttribute = null;
          if (oAttribute) {
            bFoundProperty = true;
            var oMasterAttribute = oReferencedAttributes[sAttributeId];
            let sAttributeType = oMasterAttribute.type;
            if (ContentUtils.isAttributeTypeHtml(sAttributeType)) {
              //when HTML value is modified by Rule, valueAsHTML found to be empty.
              sValue = oAttribute.valueAsHtml || oAttribute.value;
            }
            else if (ContentUtils.isAttributeTypeNumber(sAttributeType) || ContentUtils.isAttributeTypeCalculated(sAttributeType)) {
              let oNumberFormat = _getSelectedNumberFormatForVersionComparison();
              sValue = NumberUtils.getValueToShowAccordingToNumberFormat(oAttribute.value, oMasterAttribute.precision,
                  oNumberFormat, oMasterAttribute.hideSeparator);
              sOriginalValue = oAttribute.value;
            }
            else if (ContentUtils.isAttributeTypeMeasurement(sAttributeType)) {
              sValue = AttributeUtils.getLabelByAttributeType(sAttributeType, oAttribute.value, oMasterAttribute.defaultUnit,
                  oMasterAttribute.precision, oMasterAttribute.hideSeparator);
              sOriginalValue = oAttribute.value;
            }
            else if (ContentUtils.isAttributeTypeCoverflow(sAttributeType)) {
              oCoverflowAttribute = CS.find(oVersion.attributes, {baseType: BaseTypesDictionary.imageAttributeInstanceBaseType});
              if(oCoverflowAttribute.isDefault) {
                sValue = oCoverflowAttribute.thumbKey;
                oReferencedAssetsData =  _getAssetDataObject(oCoverflowAttribute);
              }
            }
            else if (ContentUtils.isAttributeTypeConcatenated(sAttributeType)) {
              let oAttribute = CS.find(oVersion.attributes, { attributeId: oMasterAttribute.id });

              aConcatenatedExpresionList = ContentUtils.getConcatenatedAttributeExpressionList(oAttribute, oVersion.attributes, oVersion.tags,
                  oReferencedAttributes, oReferencedTags, oReferencedElements);

              CS.forEach(aConcatenatedExpresionList, (attribute) => {
                sValue = sValue.concat(attribute.value);
              });
            }
            else {
              sValue = oAttribute.value;
            }
            if(!iIndex) {
              sValueToCompareWith = sValue;
            }
            else if(!CS.isEqual(sValueToCompareWith, sValue) && bAllColumnsAreEqual){
              bAllColumnsAreEqual = false;
            }
          } else {
            bAllColumnsAreEqual = false;
          }
          oVersionColumn.properties[sAttributeId] = {
            value: sValue,
            originalValue: sOriginalValue,
            isDisabled: !oVersionColumn.forComparison,
            isNotApplicable: bIsTranslatable && !bIsEntityAvailableInDL,
            referencedAssetsData: oReferencedAssetsData,
            coverflowAttribute: oCoverflowAttribute
          };
        });

        oRowData.isSkipped = bAllColumnsAreEqual;

        return !bFoundProperty;
      });

        oAttributeTableData.allRowsAreEqual = _getTableVisibility(oAttributeTableData.rowData);
        _removeUnchangedAttributes(oAttributeTableData.rowData);
      });

    if (!CS.isEmpty(oLayoutData.attributeTable) && CS.isEmpty(oLayoutData.attributeTable["attribute"].rowData)) {
      delete oLayoutData.attributeTable["attribute"];
    }

    var aTagIdsToDelete = [];
    CS.forEach(oLayoutData.tagTable, function (oTagTableData, sTagId) {
      var oTagGroup = oReferencedTags[sTagId];

      CS.forEach(oTagTableData.rowData, function (oRowData) {
        var bFoundProperty = false;
        let bAllColumnsAreEqual = true;
        let sValueToCompareWith = "";

        CS.forEach(aVersions, function (oVersion, iIndex) {
          var sVersionId = oVersion.versionId;
          var oTag = CS.find(oVersion.tags, {tagId: sTagId});
          if(oTag) {
            bFoundProperty = true;
          }

          var sChildTagId = oRowData.id;
          var oChildTag = {relevance: 0};
          if (oTag) {
            oChildTag = CS.find(oTag.tagValues, {tagId: sChildTagId});
          }

          var bIsForRange = false;
          if(oTag){
            bIsForRange = TagTypeConstants.RANGE_TAG_TYPE == oTagGroup.tagType;
          }

          var oVersionColumn = CS.find(oTagTableData.columnData, {id: sVersionId});
          if (!oVersionColumn) {
            oVersionColumn = {
              id: sVersionId,
              label: getTranslation().VERSION + sVersionId,
              isFixed: oActiveEntity.versionId == sVersionId,
              forComparison: oActiveEntity.versionId == sVersionId,
              isForRange: bIsForRange,
              width: iColumnWidth,
              properties: {},
              actionItems: oActiveEntity.versionId === sVersionId ? [] : aActionItems
            };

            oTagTableData.columnData.push(oVersionColumn);
          }

          var sValue = 0;
          if (oChildTag) {
            sValue = oChildTag.relevance;
          }
          oVersionColumn.properties[sChildTagId] = {value: sValue};
          if(!iIndex) {
            sValueToCompareWith = sValue;
          } else if(!CS.isEqual(sValueToCompareWith, sValue) && bAllColumnsAreEqual){
            bAllColumnsAreEqual = false;
          }

        });

        if(!bFoundProperty) {
          aTagIdsToDelete.push(sTagId);
        }

        oRowData.isSkipped = bAllColumnsAreEqual;

      });

      oTagTableData.allRowsAreEqual = _getTableVisibility(oTagTableData.rowData);

    });

    CS.forEach(aTagIdsToDelete, function (sTagId){
      delete oLayoutData.tagTable[sTagId];
    });

    let aRelationshipIdsToDelete = [];
    CS.forEach(oLayoutData.relationshipTable, function (oRelationshipTableData, sRelationshipSideId) {
      if (CS.isEmpty(oRelationshipTableData.rowData)) {
        aRelationshipIdsToDelete.push(sRelationshipSideId);
      }
      CS.forEach(oRelationshipTableData.rowData, function (oRowData) {
        var sElementId = oRowData.id;
        let bAllColumnsAreEqual = true;

        CS.forEach(aVersions, function (oVersion) {
          var sVersionId = oVersion.versionId;

          var oContentRelationships = oResponse.contentRelationships;
          var oNatureRelationships = oResponse.natureRelationships;
          var oVersionContentRelationships = oContentRelationships[oVersion.versionId];
          var oVersionNatureRelationships = oNatureRelationships[oVersion.versionId];

          var aAllRelationships = CS.concat(oVersionContentRelationships.relationships, oVersionNatureRelationships.relationships);
          var oContentRelationship = CS.find(aAllRelationships, {sideId: sRelationshipSideId});

          var oVersionColumn = CS.find(oRelationshipTableData.columnData, {id: sVersionId});
          if (!oVersionColumn) {
            oVersionColumn = {
              id: sVersionId,
              label: getTranslation().VERSION + sVersionId,
              isFixed: oActiveEntity.versionId == sVersionId,
              forComparison: oActiveEntity.versionId == sVersionId,
              width: iColumnWidth,
              properties: {},
              actionItems: oActiveEntity.versionId === sVersionId ? [] : aActionItems
            };

            oRelationshipTableData.columnData.push(oVersionColumn);
          }

          var sValue = '';
          var sContextId = null;
          if (oContentRelationship && CS.includes(oContentRelationship.elementIds, sElementId)) {
            sValue = sElementId;
          } else {
            bAllColumnsAreEqual = false;
          }
          if(!CS.isEmpty(oContentRelationship)) {
            var oReferencedRelationship = oAllReferencedRelationships[oContentRelationship.relationshipId];
            if (oReferencedRelationship.relationshipType) {
              //get context id from
              switch (oReferencedRelationship.relationshipType) {

                case RelationshipTypeDictionary.PRODUCT_VARIANT:
                  sContextId = CS.keys(oReferencedContexts.productVariantContexts)[0];
                  break;
              }
            } else {
              sContextId = oReferencedRelationship.side1.contextId;
            }
          }

          oVersionColumn.properties[sElementId] = {
            value: sValue,
            tags: oContentRelationship ? oContentRelationship.elementTagMapping[sElementId] : [],
            timeRange: oContentRelationship ? oContentRelationship.elementTimeRangeMapping[sElementId] : {},
            contextId: sContextId,
          };
        });

        oRowData.isSkipped = bAllColumnsAreEqual;
      });

      oRelationshipTableData.allRowsAreEqual = _getTableVisibility(oRelationshipTableData.rowData);

    });

    CS.forEach(aRelationshipIdsToDelete, function (sRelationshipId) {
      delete oLayoutData.relationshipTable[sRelationshipId];
    });

      CS.forEach(oLayoutData.headerInformationTable, function (oHeaderInformationTableData) {

      CS.remove(oHeaderInformationTableData.rowData, function (oRowData) {
        var sType = oRowData.type;
        var sRowId = oRowData.id;
        var bFoundProperty = false;
        var valueToCheck = null;
        let bAllColumnsAreEqual = true;
        let aValueToCompareWith = [];

        CS.forEach(aVersions, function (oVersion, iIndex) {
          let bIsEntityAvailableInDL = ContentUtils.isContentAvailableInSelectedDataLanguage(oVersion);
          var sVersionId = oVersion.versionId;
          var oVersionColumn = CS.find(oHeaderInformationTableData.columnData, {id: sVersionId});
          if (!oVersionColumn) {
            let sLabel = sVersionId === -1? getTranslation().LIVE_COPY : getTranslation().VERSION + sVersionId;
            oVersionColumn = {
              id: sVersionId,
              label: sLabel,
              isFixed: oActiveEntity.versionId === sVersionId,
              forComparison: oActiveEntity.versionId === sVersionId,
              width: iColumnWidth,
              properties: {},
              actionItems: oActiveEntity.versionId === sVersionId ? [] : aActionItems
            };
            oHeaderInformationTableData.columnData.push(oVersionColumn);
          }

          var oProperty = {};
          //fill up the property object according the type of row (taxonomy, class, name etc.) :
          switch (sType) {

            case "taxonomy":
              try {
                var aSelectedTaxonomyIds = oVersion.selectedTaxonomyIds;

                if(iIndex === 0) {
                  valueToCheck = aSelectedTaxonomyIds;
                } else {
                  var aTaxonomies = CS.xor(aSelectedTaxonomyIds, valueToCheck);
                  if (!CS.isEmpty(aTaxonomies)) {
                    bFoundProperty = true;
                  }
                }
                oProperty.value = aSelectedTaxonomyIds;
                oProperty.visibleValues = [];
                oProperty.isDisabled = !oVersionColumn.forComparison;

                var oTaxonomyMap = {};

                CS.forEach(oReferencedTaxonomiesInfo, function (oReferencedTaxonomy) {
                  _prepareReferencedTaxonomyMap(oReferencedTaxonomy, oTaxonomyMap);
                });

                CS.forEach(aSelectedTaxonomyIds, function (sTaxonomyId) {
                  var aTaxonomyIds = [];
                  _prepareSelectedTaxonomiesFromMap(sTaxonomyId, aTaxonomyIds, oTaxonomyMap);
                  if(!CS.isEmpty(aTaxonomyIds)) {
                    oProperty.visibleValues.push(CS.reverse(aTaxonomyIds));
                  }
                });

                if(!iIndex) {
                  aValueToCompareWith = aSelectedTaxonomyIds;
                } else if(!CS.isEqual(aValueToCompareWith, aSelectedTaxonomyIds) && bAllColumnsAreEqual){
                  bAllColumnsAreEqual = false;
                }

              } catch (oException) {
                ExceptionLogger.error(oException);
              }
              break;

            case "type":
              try {
                oProperty.visibleValues = [];
                oProperty.isDisabled = !oVersionColumn.forComparison;
                var aSelectedKlassIds = oVersion.types;
                oProperty.value = aSelectedKlassIds;
                var aKlassIds = [];

                if(iIndex === 0) {
                  valueToCheck = aSelectedKlassIds;
                } else {
                  var aTypes = CS.xor(aSelectedKlassIds, valueToCheck);
                  if (!CS.isEmpty(aTypes)) {
                    bFoundProperty = true;
                  }
                }

                CS.forEach(aSelectedKlassIds, function (sKlassId) {
                  var oKlass = oReferencedKlassesInfo[sKlassId];
                  if (oKlass) {
                    aKlassIds.push({
                      id: oKlass.id,
                      label: CS.getLabelOrCode(oKlass),
                    });
                  }
                });

                oProperty.visibleValues.push(aKlassIds);


                if(!iIndex) {
                  aValueToCompareWith = aSelectedKlassIds;
                } else if(!CS.isEqual(aValueToCompareWith, aSelectedKlassIds) && bAllColumnsAreEqual){
                  bAllColumnsAreEqual = false;
                }

              } catch (oException) {
                ExceptionLogger.error(oException);
              }

              break;

            case "name":
              try {
                var aAttributes = oVersion.attributes;
                var oAttribute = CS.find(aAttributes, {attributeId: "nameattribute"});
                var sName = oAttribute && oAttribute.value || "";

                if(iIndex === 0) {
                  valueToCheck = sName;
                } else {
                  if (!CS.isEqual(sName, valueToCheck)) {
                    bFoundProperty = true;
                  }
                }

                if(oAttribute) {
                  oProperty.nameAttribute = {
                    id: oAttribute.id,
                    label: oAttribute.value
                  }
                }
                oProperty.value = sName;
                oProperty.isDisabled = !oVersionColumn.forComparison;

                if(!iIndex) {
                  aValueToCompareWith = sName;
                } else if(!CS.isEqual(aValueToCompareWith, sName) && bAllColumnsAreEqual){
                  bAllColumnsAreEqual = false;
                }

                let oReferencedAttribute = oRowData.masterAttribute;
                let bIsTranslatable = oReferencedAttribute.isTranslatable;
                oProperty.isNotApplicable = bIsTranslatable && !bIsEntityAvailableInDL;

              } catch (oException) {
                ExceptionLogger.error(oException);
              }
              break;

            case "image":
              try {
                var sBaseType = oVersion.baseType;
                if(sBaseType !== BaseTypesDictionary.assetBaseType) {
                  var sDefaultAssetInstanceId = oVersion.defaultAssetInstanceId;
                  if(sDefaultAssetInstanceId) {
                    if(iIndex === 0) {
                      valueToCheck = sDefaultAssetInstanceId;
                    } else {
                      if (!CS.isEqual(sDefaultAssetInstanceId, valueToCheck)) {
                        bFoundProperty = true;
                      }
                    }
                    oProperty.value = sDefaultAssetInstanceId;
                    var oReferencedAssetsData = {};
                    oReferencedAssetsData[sDefaultAssetInstanceId] = _getAssetDataObject(oReferencedAssets[sDefaultAssetInstanceId]);
                    oProperty.referencedAssetsData = oReferencedAssetsData;
                  } else {
                    bAllColumnsAreEqual = false;
                  }
                }
              } catch (oException) {
                ExceptionLogger.error(oException);
              }
              break;

            case "lifeCycleStatusTag":

              try {
                var oTag = CS.find(oVersion.tags, {tagId: sRowId});
                var oReferencedTag = oReferencedTags[sRowId];
                var aValues = [];

                if(iIndex === 0) {
                  valueToCheck = _getSelectedTagIdsFromTag(oTag);
                } else {
                  var aSelectedTagIds = _getSelectedTagIdsFromTag(oTag);
                  var aTags = CS.xor(aSelectedTagIds, valueToCheck);
                  if (!CS.isEmpty(aTags)) {
                    bFoundProperty = true;
                  }
                }

                if (!CS.isEmpty(oTag)) {

                  CS.forEach(oTag.tagValues, function (oTagValue) {
                    if (oTagValue.relevance == 100) {
                      var oReferencedTagValue = CS.find(oReferencedTag.children, {id: oTagValue.tagId});
                      aValues.push({
                        id: oTagValue.id,
                        tagId: oTagValue.tagId,
                        label: oReferencedTagValue.label,
                        code: oReferencedTagValue.code
                      });
                    }
                  });
                  oProperty.selectedStatusTagIds = _getSelectedTagIdsFromTag(oTag);
                  oProperty.values = aValues;
                }

                if(!iIndex) {
                  aValueToCompareWith = aValues;
                } else if(!CS.isEqual(aValueToCompareWith, aValues) && bAllColumnsAreEqual){
                  bAllColumnsAreEqual = false;
                }

              } catch (oException) {
                ExceptionLogger.error(oException);
              }

              break;

            case "eventSchedule":
              try {
                oProperty.visibleValues = [];
                oProperty.isDisabled = true;
                oProperty.value = null;
                let oEventScheduleInfo = oVersion.eventSchedule;
                if (oEventScheduleInfo && oEventScheduleInfo.startTime && oEventScheduleInfo.endTime) {
                  let oEventStartTime = ContentUtils.getDateAttributeInDateTimeFormat(oEventScheduleInfo.startTime);
                  let oEventEndTime = ContentUtils.getDateAttributeInDateTimeFormat(oEventScheduleInfo.endTime);
                  let sFrom = oEventStartTime.date + " " + oEventStartTime.time;
                  let sTo = oEventEndTime.date + " " + oEventEndTime.time;
                  let sRepeat = "";

                  let oRepeat = oEventScheduleInfo.repeat;
                  if (oRepeat && !CS.isEmpty(oRepeat.repeatType)) {
                    sRepeat = getTranslation()[oRepeat.repeatType];

                    let sRepeatType = oRepeat.repeatType;
                    if (sRepeatType !== "WEEKLY") {
                      sRepeat = sRepeat + " " + oRepeat.repeatEvery + " " + CS.toLower(getTranslation().TIMES);
                    }

                    let oDaysOfWeek = oRepeat.daysOfWeek;
                    if (!CS.isEmpty() && sRepeatType !== "DAILY" && sRepeatType !== "MONTHLY") {
                      let aRepeatDaysOfWeek = [];
                      CS.forEach(oDaysOfWeek, function (sNewDay, sNewDayIndex) {
                        if (sNewDay === true) {
                          let sNewDay;
                          switch (sNewDayIndex) {
                            case "MON":
                              sNewDay = getTranslation().MONDAY_SHORT;
                              break;
                            case "TUE":
                              sNewDay = getTranslation().TUESDAY_SHORT;
                              break;
                            case "WED":
                              sNewDay = getTranslation().WEDNESDAY_SHORT;
                              break;
                            case "THU":
                              sNewDay = getTranslation().THURSDAY_SHORT;
                              break;
                            case "FRI":
                              sNewDay = getTranslation().FRIDAY_SHORT;
                              break;
                            case "SAT":
                              sNewDay = getTranslation().SATURDAY_SHORT;
                              break;
                            case "SUN":
                              sNewDay = getTranslation().SUNDAY_SHORT;
                              break;
                          }
                          aRepeatDaysOfWeek.push(sNewDay);
                        }
                      });
                      sRepeat = sRepeat + " " + CS.toLower(getTranslation().ON) + " " + aRepeatDaysOfWeek.join();
                    }
                  }
                  oProperty.value = {
                    [getTranslation().FROM]: sFrom,
                    [getTranslation().TO]: sTo,
                  };
                }

                if (oEventScheduleInfo && oEventScheduleInfo.startTime && oEventScheduleInfo.endTime && !bFoundProperty) {
                  bFoundProperty = true;
                }

                if (!iIndex && oEventScheduleInfo) {
                  aValueToCompareWith = oEventScheduleInfo.startTime && oEventScheduleInfo.endTime ? oEventScheduleInfo : null;
                } else if ((!CS.isEqual(aValueToCompareWith, oEventScheduleInfo) && bAllColumnsAreEqual)) {
                  bAllColumnsAreEqual = false;
                }
              } catch (oException) {
                ExceptionLogger.error(oException);
              }
              break;
          }
          oVersionColumn.properties[sRowId] = oProperty;
        });

        oRowData.isSkipped = bAllColumnsAreEqual;
        return !bFoundProperty;
      });
      oHeaderInformationTableData.allRowsAreEqual = _getTableVisibility(oHeaderInformationTableData.rowData);
    });

    if (!CS.isEmpty(oLayoutData.headerInformationTable) && CS.isEmpty(oLayoutData.headerInformationTable["headerInformation"].rowData)) {
      delete oLayoutData.headerInformationTable["headerInformation"];
    }

    CS.forEach(oLayoutData.fixedHeaderTable, function (oFixedHeaderData) {

      CS.remove(oFixedHeaderData.rowData, function (oRowData) {
        var sType = oRowData.type;
        var sRowId = oRowData.id;
        var bFoundProperty = false;

        CS.forEach(aVersions, function (oVersion, iIndex) {
          let bIsEntityAvailableInDL = ContentUtils.isContentAvailableInSelectedDataLanguage(oVersion);
          var sVersionId = oVersion.versionId;
          var oVersionColumn = CS.find(oFixedHeaderData.columnData, {id: sVersionId});
          let sLabel = (sVersionId == -1) ? getTranslation().LIVE_COPY : getTranslation().VERSION + sVersionId;
          if (!oVersionColumn) {
            oVersionColumn = {
              id: sVersionId,
              label: sLabel,
              createdBy: oVersion.lastModifiedBy,  //For version comparision, last modified details displays as
              createdOn: oVersion.lastModified,    // creation details
              isFixed: oActiveEntity.versionId === sVersionId,
              forComparison: oActiveEntity.versionId === sVersionId,
              width: iColumnWidth,
              isEntityAvailableInDL: bIsEntityAvailableInDL,
              properties: {},
              actionItems: oActiveEntity.versionId === sVersionId ? [] : aActionItems
            };
            oFixedHeaderData.columnData.push(oVersionColumn);
          }

          var oProperty = {};
          switch (sType) {
            case "image":
              try {
                var sBaseType = oVersion.baseType;
                var sDefaultAssetInstanceId = oVersion.defaultAssetInstanceId;
                bFoundProperty = true;
                var oReferencedAssetsData = {};
                if(sBaseType !== BaseTypesDictionary.assetBaseType && sDefaultAssetInstanceId) {
                  oProperty.value = sDefaultAssetInstanceId;
                  oReferencedAssetsData[sDefaultAssetInstanceId] = _getAssetDataObject(oReferencedAssets[sDefaultAssetInstanceId]);
                } else {
                  oProperty.value = oVersion.id;
                  var oCoverflowAttribute = oVersion.assetInformation;
                  oReferencedAssetsData[oVersion.id] = _getAssetDataObject(oCoverflowAttribute);
                }
                oProperty.referencedAssetsData = oReferencedAssetsData;
                oProperty.name = oVersion.name;
              } catch (oException) {
                ExceptionLogger.error(oException);
              }
              break;
          }

          oVersionColumn.properties[sRowId] = oProperty;
        });
        return !bFoundProperty;
      });
    });

    if (!CS.isEmpty(oLayoutData.fixedHeaderTable) && CS.isEmpty(oLayoutData.fixedHeaderTable["fixedHeader"].rowData)) {
      delete oLayoutData.fixedHeaderTable["fixedHeader"];
    }

  };

  let _removeUnchangedAttributes = function (aAttributesRowData) {
    CS.remove(aAttributesRowData, function (oRowData) {
      return oRowData.isSkipped;
    });
  };

  var _getSelectedTagIdsFromTag = function (oTag) {
    var aTagValues = oTag && oTag.tagValues;
    var aSelectedTags = [];
    CS.forEach(aTagValues, function (oTag) {
      if(oTag.relevance !== 0) {
        aSelectedTags.push(oTag.tagId);
      }
    });

    return aSelectedTags;
  };

  var _makeInstancesComparisonRowDataForFixedHeader = function (oLayoutData) {
    try {
      if (!oLayoutData.fixedHeaderTable['fixedHeader']) {
        let oTableSkeletonData = _getTableSkeletonData();
        oTableSkeletonData.tableId = 'fixedHeader';
        oLayoutData.fixedHeaderTable['fixedHeader'] = oTableSkeletonData;
      }

      let oRowObject = _getDefaultImageRowData();
      oLayoutData.fixedHeaderTable['fixedHeader'].rowData.push(oRowObject);

    } catch (oException) {
      ExceptionLogger.error(oException);
    }
  };

  let _preProcessLayoutDataToShowHeaderButtons = function (oLayoutData) {
    let sButtonId = "";
    let sPropertiesTabId = "timeline_comparison_properties";
    let sRelationshipTabId = "timeline_comparison_relationship";
    CS.forEach(oLayoutData, function (oTableData, sTableId) {
      let aTableIds = ["attributeTable", "tagTable", "headerInformationTable"];
      if (CS.includes(aTableIds, sTableId) && !CS.isEmpty(oTableData)) {
        oLayoutData.showPropertiesTab = true;
        sButtonId = sPropertiesTabId;
      }
      if (sTableId === "relationshipTable" && !CS.isEmpty(oTableData)) {
        oLayoutData.showRelationshipsTab = true;
        sButtonId = CS.isEmpty(sButtonId) ? sRelationshipTabId : sButtonId;
      }
    });

    /**To show properties tab and relationship tab on timeline version comparison based on condition*/
    let sSelectedHeaderButtonId = TimelineProps.getSelectedHeaderButtonId();
    let sHeaderButtonId = sSelectedHeaderButtonId;
    if (sSelectedHeaderButtonId === sPropertiesTabId && !oLayoutData.showPropertiesTab) {
      sHeaderButtonId = sRelationshipTabId;
    } else if (sSelectedHeaderButtonId === sRelationshipTabId && !oLayoutData.showRelationshipsTab) {
      sHeaderButtonId = sPropertiesTabId;
    } else if (CS.isEmpty(sSelectedHeaderButtonId)) {
      sHeaderButtonId = oLayoutData.showPropertiesTab ? sPropertiesTabId : sRelationshipTabId;
    }
    TimelineProps.setSelectedHeaderButtonId(sHeaderButtonId);

    /**Don't show any buttons when version properties or relationships are empty*/
    if(!oLayoutData.showPropertiesTab || !oLayoutData.showRelationshipsTab) {
      oLayoutData.showPropertiesTab = oLayoutData.showRelationshipsTab = false;
    }
  };

  var _makeDataForComparison = function (oResponse) {
    var oConfigDetails = oResponse.configDetails;
    var oReferencedTemplates = oConfigDetails.referencedTemplate;
    var aVersions = oResponse.versions;
    var oLayoutData = _getLayoutSkeleton();
    _makeComparisonRowDataForCustomTab(oLayoutData, oReferencedTemplates, oConfigDetails, oResponse);
    _makeComparisonRowDataForHeaderInformation(oLayoutData, oResponse);
    _makeInstancesComparisonRowDataForFixedHeader(oLayoutData);

    _makeComparisonColumnData(oLayoutData, oResponse, aVersions);

    /**Processing to show properties and relationship tab**/
    _preProcessLayoutDataToShowHeaderButtons(oLayoutData);

    TimelineProps.setComparisonLayoutData(oLayoutData);
  };

  var _changeZoomLevel = function (bIncrement) {
    var iCurrentZoomLevel = TimelineProps.getZoomLevel();
    if(bIncrement) {
      iCurrentZoomLevel < 3 && iCurrentZoomLevel++;
    } else {
      iCurrentZoomLevel > 1 && iCurrentZoomLevel--;
    }
    TimelineProps.setZoomLevel(iCurrentZoomLevel);

  };

  var _archiveVersions = function (aVersionIds) {
    var oActiveContent = ContentUtils.getActiveEntity();
    var sArticleId = oActiveContent.id;
    var sEntityBaseType = oActiveContent.baseType;
    var sScreenMode = ContentUtils.getScreenModeBasedOnEntityBaseType(sEntityBaseType);
    let oPostData = {
      versionNumbers: aVersionIds,
      baseType: sEntityBaseType
    };

    let bIsDeleteFromArchival = TimelineProps.getIsArchiveVisible();
    bIsDeleteFromArchival && (oPostData.isDeleteFromArchival = bIsDeleteFromArchival);

    var sUrl = getRequestMapping(sScreenMode).DeleteContentVersions;

    CS.postRequest(sUrl, {id: sArticleId}, oPostData, successArchiveVersionsCallback, failureArchiveVersionsCallback);

  };

  var _archiveVersionButtonClicked = function (aVersionIds) {
    //Are you sure you want to archive following Versions?
    let CustomActionDialogStore = require('../../../../../../commonmodule/store/custom-action-dialog-store').default;
    CustomActionDialogStore.showConfirmDialog(getTranslation().STORE_ALERT_CONFIRM_DELETE_VERSIONS, '',
        function () {
          _archiveVersions(aVersionIds);
        }, function (oEvent) {
        });
  };

  var _handleLanguageForComparisonChanged = function (sLanguageForComparison, oFilterContext) {
    if (ContentUtils.isActiveContentDirty(oFilterContext)) {
      ContentUtils.showMessage(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
      return;
    }
    let aLanguagesInfo = SessionProps.getLanguageInfoData();
    let aDataLanguages = aLanguagesInfo.dataLanguages;
    let oSelectedLanguageForComparison = CS.find(aDataLanguages, {id: sLanguageForComparison});
    let sLanguageForComparisonCode = oSelectedLanguageForComparison.code;
    let oExtraData = {
      dataLanguage: sLanguageForComparisonCode
    };

    let oCallbackData = {
      preFunctionToExecute: function () {
        TimelineProps.setSelectedLanguageForComparison(sLanguageForComparisonCode);
      }
    };

    let aVersionIdsForCompare = TimelineProps.getSelectedVersionIds();
    _fetchComparisonData(oExtraData, aVersionIdsForCompare, oCallbackData);
  };

  var _fetchComparisonData = function (oExtraData, aNewSelectedVersions, oCallbackData) {
    var oComponentProps = ContentUtils.getComponentProps();
    var oScreenProps = oComponentProps.screen;

    var oActiveContent = ContentUtils.getActiveEntity();
    var sArticleId = oActiveContent.id;
    var sEntityBaseType = oActiveContent.baseType;

    var oData = {
      id: sArticleId,
    };

    var sTemplateId = oScreenProps.getSelectedTemplateId();
    var aSelectedVersions=[];
    if (!(CS.isEmpty(aNewSelectedVersions))) {
      aSelectedVersions = aNewSelectedVersions;
    }
    else {
      aSelectedVersions = TimelineProps.getSelectedVersionIds();
    }

    var oFilterPostData = {
      from: 0,
      templateId: ContentUtils.getTemplateIdForServer(sTemplateId),
      versionIds: aSelectedVersions
    };

    if(!CS.isEmpty(oExtraData)) {
      CS.assign(oFilterPostData, oExtraData);
    }

    var sScreenMode = ContentUtils.getScreenModeBasedOnEntityBaseType(sEntityBaseType);
    var sUrl = getRequestMapping(sScreenMode).GetVersionsForComparisons;
    let fSuccess = successFetchTimelineComparisonData.bind(this, oCallbackData);

    CS.postRequest(sUrl, oData, oFilterPostData, fSuccess, failureFetchTimelineComparisonData,  false, oExtraData);

  };

  var _compareVersionButtonClicked = function (bIsComparisonMode, aSelectedVersions) {
    if (bIsComparisonMode) {
      /** To set current data language as language for comparison for the first time when comparison window is opened **/
      let oCallbackData = {
        preFunctionToExecute: function () {
          let sSelectedDataLanguageCode = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);
          TimelineProps.setSelectedLanguageForComparison(sSelectedDataLanguageCode);
        }
      };

      aSelectedVersions && TimelineProps.setSelectedVersionIds(aSelectedVersions);
      _fetchComparisonData({}, aSelectedVersions, oCallbackData);
    } else {
      TimelineProps.setIsComparisonMode(bIsComparisonMode);
    }
  };

  var _deleteSelectedVersionIds = function () {
    var aSelectedVersionIds = TimelineProps.getSelectedVersionIds();
    var oActiveContent = ContentUtils.getActiveContent();
    var sActiveContentVersionId = oActiveContent.versionId;

    if(CS.includes(aSelectedVersionIds, sActiveContentVersionId)){
      ContentUtils.showMessage(getTranslation().CANNOT_DELETE_ACTIVE_VERSION);
      return;
    }

    _archiveVersionButtonClicked(aSelectedVersionIds);
  };

  var _loadMoreButtonClicked = function (oFilterContext) {
    _handleTimelineVersionShowArchiveButtonClicked(TimelineProps.getIsArchiveVisible(), true, oFilterContext);
  };

  var _selectVersionButtonClicked = function (sVersionId) {
    var aSelectedVersionIds = TimelineProps.getSelectedVersionIds();
    var iMaxSelectedVersionCount = TimelineProps.getMaxSelectedVersionCount();

    var aRemovedItems = CS.remove(aSelectedVersionIds, function (sId) {return sId === sVersionId});
    if(aSelectedVersionIds.length == iMaxSelectedVersionCount){
      ContentUtils.showMessage(getTranslation().MAXIMUM_NO_OF_VERSIONS_SELECTED);
      return;
    }

    if(CS.isEmpty(aRemovedItems)) {
      aSelectedVersionIds.push(sVersionId);
    }
  };

  var _setVersions = function (aVersionList, iFrom) {
    if(aVersionList) {
      var oContentScreenProps = ContentUtils.getComponentProps();
      var iSizeLimit = oContentScreenProps.screen.getDefaultPaginationLimit();
      if(aVersionList.length < iSizeLimit) {
        TimelineProps.setIsCreatedOnVisible(true);
      }
      else {
        TimelineProps.setIsCreatedOnVisible(false);
      }
      var aVersions = [];
      if (iFrom == 0) {
        aVersions = aVersionList;
      }
      else {
        aVersions = TimelineProps.getVersions();
        aVersions = aVersions.concat(aVersionList);
      }
      TimelineProps.setVersions(aVersions);
    }

  };

  var _shouldEmptyOtherTagPropertyValues = function (sTagGroupId) {
    var oComponentProps = ContentUtils.getComponentProps();
    var oReferencedTags = oComponentProps.screen.getReferencedTags();
    var oTagGroup = oReferencedTags[sTagGroupId];
    return (!oTagGroup.isMultiselect) && _checkTagTypeToEmtpyOtherTagPropertyValue(oTagGroup.tagType);
  };

  var _checkTagTypeToEmtpyOtherTagPropertyValue = function (sTagType) {
    switch (sTagType) {
      case TagTypeConstants.YES_NEUTRAL_TAG_TYPE:
      case TagTypeConstants.RULER_TAG_TYPE:
      case TagTypeConstants.STATUS_TAG_TYPE:
      case TagTypeConstants.LISTING_STATUS_TAG_TYPE:
        return true;
      default:
        return false;
    }
  }

  var _handleMatchMergeCellRemoveClicked = function(sRowId, sTableId, sTableGroupName){
    var oActiveContent = ContentUtils.getActiveContent();
    var iContentVersionId = oActiveContent.versionId;
    var oComparisonLayoutData = TimelineProps.getComparisonLayoutData();
    var oTableGroup = oComparisonLayoutData[sTableGroupName];
    var oTable = oTableGroup[sTableId];
    var aColumnData = oTable.columnData;
    var oColumn = CS.find(aColumnData, {id: iContentVersionId});
    var oProperty = oColumn.properties[sRowId];
    oProperty.isDirty = true;
    oProperty.value = null;

    ContentUtils.makeActiveContentDirty();
  };

  var _handleMatchMergeCellClicked = function(sRowId, oSourceProperty, sTableId, sTableGroupName){
    let oComponentProps = ContentUtils.getComponentProps();
    let oReferencedAttributes = oComponentProps.screen.getReferencedAttributes();
    let oReferencedAttribute = oReferencedAttributes[sRowId];
    var oActiveContent = ContentUtils.getActiveContent();
    var iContentVersionId = oActiveContent.versionId;
    var oComparisonLayoutData = TimelineProps.getComparisonLayoutData();
    var oTableGroup = oComparisonLayoutData[sTableGroupName];
    var oTable = oTableGroup[sTableId];
    var aColumnData = oTable.columnData;
    var oColumn = CS.find(aColumnData, {id: iContentVersionId});

    if(CS.isEmpty(oColumn)){
      return;
    }

    var oTargetProperty = oColumn.properties[sRowId] || {};
    oTargetProperty.isDirty = true;

    if(sTableGroupName == "tagTable"){ //only for Yes-Neutral tags
      var bShouldEmpty = _shouldEmptyOtherTagPropertyValues(sTableId);
      if(bShouldEmpty){
       CS.forEach(oColumn.properties, function (oProperty) {
         oProperty.isDirty = true;
         oProperty.value = 0;
       });
      }
    }

    if(sTableGroupName == "attributeTable") {
      if(oSourceProperty.referencedAssetsData) {
        oTargetProperty.referencedAssetsData = oSourceProperty.referencedAssetsData;
        oTargetProperty.coverflowAttribute = oSourceProperty.coverflowAttribute;
      }
    }

    if (sTableGroupName === "relationshipTable") {
      oTargetProperty.tags = oSourceProperty.tags;
      oTargetProperty.timeRange = oSourceProperty.timeRange;
      oTargetProperty.contextId = oSourceProperty.contextId;
    }

    if(sTableGroupName === "headerInformationTable") {
      if(oSourceProperty.nameAttribute) {
        oTargetProperty.nameAttribute = oSourceProperty.nameAttribute;
      }
      if(oSourceProperty.referencedAssetsData) {
        oTargetProperty.referencedAssetsData = oSourceProperty.referencedAssetsData;
      }
      oTargetProperty.visibleValues = oSourceProperty.visibleValues;
    }

    if(oReferencedAttribute && (ContentUtils.isAttributeTypeNumber(oReferencedAttribute.type) || ContentUtils.isAttributeTypeMeasurement(oReferencedAttribute.type))) {
      oTargetProperty.value = oSourceProperty.value;
      oTargetProperty.originalValue = oSourceProperty.originalValue;
    }
    else {
      oTargetProperty.value = oSourceProperty.value;
    }

    ContentUtils.makeActiveContentDirty();
  };

  var _postProcessVersionMerge = function () {
    var oComponentProps = ContentUtils.getComponentProps();
    var oComparisonLayoutData = TimelineProps.getComparisonLayoutData();
    var oActiveContent = ContentUtils.getActiveContent();
    var iContentVersionId = oActiveContent.versionId;
    var oContentClone = ContentUtils.makeActiveContentDirty();
    var oReferencedAttributes = oComponentProps.screen.getReferencedAttributes();

    var oAttributeTable = oComparisonLayoutData.attributeTable;
    var oRelationshipTable = oComparisonLayoutData.relationshipTable;
    var oTagTable = oComparisonLayoutData.tagTable;
    var oHeaderInformationTable = oComparisonLayoutData.headerInformationTable;

    TimelineProps.setIsArchiveVisible(false);
    //for attributes
    var aAttributes = oContentClone.attributes;
    CS.forEach(oAttributeTable, function (oTableData) {
      try {
        var oActiveContentAttributeColumn = CS.find(oTableData.columnData, {id: iContentVersionId});
        if(oActiveContentAttributeColumn) {
          CS.forEach(oActiveContentAttributeColumn.properties, function (oProperty, sId) {
            try {
              if (oProperty.isDirty) {
                var oAttribute = CS.find(aAttributes, {attributeId: sId});
                var oMasterAttribute = oReferencedAttributes[sId];
                if(ContentUtils.isAttributeTypeHtml(oMasterAttribute.type)) {
                  oAttribute.valueAsHtml = ContentUtils.getDecodedHtmlContent(oProperty.value);
                  oAttribute.value = oProperty.value;
                }
                else if (ContentUtils.isAttributeTypeNumber(oMasterAttribute.type) || ContentUtils.isAttributeTypeMeasurement(oMasterAttribute.type)) {
                  oAttribute.value = oProperty.originalValue;
                }
                else if (ContentUtils.isAttributeTypeCoverflow(oMasterAttribute.type)) {
                  var oCoverflowAttribute = oProperty.coverflowAttribute;
                  var oCoverflowAttributeFromContent = CS.find(aAttributes, {attributeId: "assetcoverflowattribute"});
                  if(oCoverflowAttributeFromContent) {
                    CS.remove(aAttributes, {id: oCoverflowAttributeFromContent.id});
                  }
                  oCoverflowAttribute.id = UniqueIdentifierGenerator.generateUUID();
                  aAttributes.push(oCoverflowAttribute);
                } else if (ContentUtils.isAttributeTypeName(oMasterAttribute.type)) {
                  oContentClone.name = oProperty.value;
                  oAttribute.value = oProperty.value;
                }
                else {
                  oAttribute.value = oProperty.value;
                }
                oAttribute.isValueChanged = true;
              }
            }
            catch (oException) {
              ExceptionLogger.error(oException);
            }
          });
        }
      }
      catch (oException) {
        ExceptionLogger.error(oException);
      }
    });

    //for relationships
    var aContentRelationships = oContentClone.contentRelationships;
    var aNatureRelationships = oContentClone.natureRelationships;
    var aAllRelationships = CS.concat(aContentRelationships, aNatureRelationships);
    CS.forEach(oRelationshipTable, function (oTableData) {
      try {
        var oRelationship = CS.find(aAllRelationships, {sideId: oTableData.tableId});
        var oActiveContentRelationshipColumn = CS.find(oTableData.columnData, {id: iContentVersionId});
        if(oActiveContentRelationshipColumn) {
          CS.forEach(oActiveContentRelationshipColumn.properties, function (oProperty, sId) {
            try {
              if (oProperty.isDirty) {
                if (CS.isEmpty(oProperty.value)) {
                  CS.remove(oRelationship.elementIds, function (sElementId) {
                    return sElementId == sId;
                  });
                } else {
                  if (!CS.includes(oRelationship.elementIds, sId)) {
                    oRelationship.elementIds.push(sId);
                  }
                  oRelationship.elementTagMapping[sId] = oProperty.tags;
                  oRelationship.elementTimeRangeMapping[sId] = oProperty.timeRange;
                  oRelationship.contextId = oProperty.contextId;
                }
              }
            }
            catch(oException) {
              ExceptionLogger.error(oException);
            }
          });
        }
      }
      catch(oException) {
        ExceptionLogger.error(oException);
      }
    });

    //for tags
    var aTags = oContentClone.tags;
    CS.forEach(oTagTable, function (oTableData) {
      try {
        var oTagGroup = CS.find(aTags, {tagId: oTableData.tableId});
        var oActiveContentTagColumn = CS.find(oTableData.columnData, {id: iContentVersionId});
        CS.forEach(oActiveContentTagColumn.properties, function (oProperty, sId) {
          try {
            if (oProperty.isDirty) {
              var oTagValue = CS.find(oTagGroup.tagValues, {tagId: sId});
              if (CS.isEmpty(oTagValue) && oProperty.value) {
                oTagValue = ContentUtils.getNewTagValue(sId);
                oTagGroup.tagValues.push(oTagValue);
              }

              if(oTagValue) {
                oTagValue.relevance = oProperty.value;
                oTagGroup.isValueChanged = true;
              }
            }
          }
          catch (oException) {
            ExceptionLogger.error(oException);
          }
        });
      }
      catch (oException) {
        ExceptionLogger.error(oException);
      }
    });


    //for tags
    CS.forEach(oHeaderInformationTable, function (oHeaderInformationData) {
      try {

        let sTableId = oHeaderInformationData.tableId;
        switch (sTableId) {

          case "headerInformation":
            let oActiveContentTagColumn = CS.find(oHeaderInformationData.columnData, {id: iContentVersionId});
            let oProperties = oActiveContentTagColumn.properties;
            let oTaxonomy = oProperties["taxonomy"];
            let oType = oProperties["type"];
            let oName = oProperties["name"];
            let oImage = oProperties["image"];

            if (oTaxonomy && oTaxonomy.isDirty) {
              oContentClone.selectedTaxonomyIds = oTaxonomy.value;
            }
            if(oType && oType.isDirty) {
              oContentClone.types = oType.value;
            }
            if(oName && oName.isDirty) {
              oContentClone.name = oName.value;
              var oAttribute = CS.find(oContentClone.attributes, {attributeId: "nameattribute"});
              var oNameAttribute = oName.nameAttribute;
              oAttribute.value = oNameAttribute.label;
            }
            if(oImage && oImage.isDirty) {
              oContentClone.defaultAssetInstanceId = oImage.value;
            }
            break;

        }

      }
      catch (oException) {
        ExceptionLogger.error(oException);
      }
    });


  };

  var _isComparisonMode = function () {
    return TimelineProps.getIsComparisonMode();
  };

  var successArchiveVersionsShowCallback = function (bArchiveFlag,oResponse) {
    let oResponseData = oResponse.success;
    TimelineStore.setVersions(oResponseData.versions, oResponseData.from);
    TimelineProps.setIsArchiveVisible(bArchiveFlag);
    TimelineProps.setSelectedVersionIds([]);
    let ContentStore = ContentUtils.getContentStore();
    ContentStore.setContentOpenReferencedData(oResponseData, oResponseData.klassInstance);
    _triggerChange();
  };

  var failureArchiveVersionsShowCallback = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureArchiveVersionsShowCallback', getTranslation());
  };

  var _handleTimelineVersionShowArchiveButtonClicked = function(bArchiveFlag, bIsLoadMore, oFilterContext) {
    var oActiveContent = ContentUtils.getActiveEntity();
    var sArticleId = oActiveContent.id;
    var sSelectedTabId = ContentUtils.getSelectedTabId(sArticleId);
    var sCurrentTab = ContentUtils.getTabUrlFromTabId(sSelectedTabId);
    var sEntityBaseType = oActiveContent.baseType;
    var sScreenMode = ContentUtils.getScreenModeBasedOnEntityBaseType(sEntityBaseType);
    var sUrl = null;
    if(bArchiveFlag) {
      sUrl = getRequestMapping(sScreenMode).getArchiveTimelineData;
    }
    else
    {
      sUrl = getRequestMapping(sScreenMode).GetEntityById;
    }
      var oData = {
        id: sArticleId,
        tab: sCurrentTab,
        isLoadMore: false,
        getAll: true
      };
      var oFilterPostData = ContentUtils.createFilterPostData(null, oFilterContext);

    if (bIsLoadMore) {
      var aVersions = TimelineProps.getVersions();
      oFilterPostData.from = aVersions.length;
      oData.isLoadMore = true;
    }
    CS.postRequest(sUrl,oData,oFilterPostData,successArchiveVersionsShowCallback.bind(this,bArchiveFlag), failureArchiveVersionsShowCallback);
  };

  var successArchiveVersionsRestoreCallback = function (oResponse) {
    var aVersions = TimelineProps.getVersions();
    var aSelectedVersions = TimelineProps.getSelectedVersionIds();
    CS.forEach(oResponse.versionNumbers, function (sId) {
      //TODO: Instead we should send get call with pagination to fetch latest version lists
      CS.remove(aVersions, function (oVersion) {
        return oVersion.versionNumber == sId;
      });

      CS.remove(aSelectedVersions, function (sVersionId) { return sVersionId == sId});
    });

    ContentUtils.showSuccess(getTranslation().VERSION_SUCCESSFULLY_RESTORED);
    // oContentStore.fetchArticleById(oActiveEntity.id, oCallbackData, oExtraData);
    _triggerChange();
  };

  var failureArchiveVersionsRestoreCallback = function (oResponse) {
    var aExceptionDetails = oResponse.failure ? oResponse.failure.exceptionDetails : [];
    if (!CS.isEmpty(aExceptionDetails)  && aExceptionDetails[0].key === 'MaxVersionCountViolateException') {
      alertify.error(getTranslation().CANNOT_RESTORE_ARCHIVED_VERSIONS);
      return;
    }
    ContentUtils.failureCallback(oResponse, 'failureArchiveVersionsRestoreCallback', getTranslation());
  };

  var _restoreArchiveVersions= function (aVersionIds) {
    var oActiveContent = ContentUtils.getActiveEntity();
    var sArticleId = oActiveContent.id;
    var sEntityBaseType = oActiveContent.baseType;
    var sScreenMode = ContentUtils.getScreenModeBasedOnEntityBaseType(sEntityBaseType);
    var sUrl = getRequestMapping(sScreenMode).RestoreContentVersions;
    CS.postRequest(sUrl, {}, {versionNumbers: aVersionIds,instanceId: sArticleId},successArchiveVersionsRestoreCallback, failureArchiveVersionsRestoreCallback);
  };

  var _timelineRestoreArchivedVersion = function (aVersionIds) {
    CustomActionDialogStore.showConfirmDialog(getTranslation().STORE_ALERT_CONFIRM_RESTORE_VERSIONS, '',
        function () {
          _restoreArchiveVersions(aVersionIds);
        }, function (oEvent) {
        });
  };

  var _handleTimelineRestoreArchiveVersionsButtonClicked = function(){
    var aSelectedVersionIds = TimelineProps.getSelectedVersionIds();
    _timelineRestoreArchivedVersion(aSelectedVersionIds);
  };

  var _handleTimelineVersionSelectAllButtonClicked = function (){
    var aVersions = TimelineProps.getVersions();
    var aVersionIds = [];
    var aShowSelectedVersion = CS.cloneDeep(aVersions);
    var iMaxSelectedVersionCount = TimelineProps.getMaxSelectedVersionCount();
    if (TimelineProps.getSelectedVersionIds().length) {
      TimelineProps.setSelectedVersionIds([]);
    }
    else {
      if (aShowSelectedVersion.length > iMaxSelectedVersionCount) {
        aShowSelectedVersion = aShowSelectedVersion.splice(0, iMaxSelectedVersionCount);
      }
      CS.forEach(aShowSelectedVersion, function (oVersion) {
        aVersionIds.push(oVersion.versionNumber)
      });
      TimelineProps.setSelectedVersionIds(aVersionIds);
    }
    _triggerChange();
  };

  let _timelineRollbackArchivedVersion = function (aVersionId) {
    CustomActionDialogStore.showConfirmDialog(getTranslation().STORE_ALERT_CONFIRM_ROLLBACK_VERSIONS, '',
        function () {
          _rollbackArchiveVersion(aVersionId);
        }, function (oEvent) {
        });
  };

  let successRollbackVersionsRestoreCallback = function (oCallback, oResponse) {
    if(oCallback && oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
    let aWarningData = oResponse.success.warnings;
    let oAssetAlreadyUploadedException = CS.find(aWarningData.exceptionDetails, {"key": "AssetAlreadyUploadedException"});
    CS.isNotEmpty(oAssetAlreadyUploadedException) && alertify.warning(`${getTranslation().AssetAlreadyUploadedException} ${oAssetAlreadyUploadedException.itemName}`);
    let ContentStore = ContentUtils.getContentStore();
    ContentStore.successFetchArticleById(oCallback, oResponse);
    _triggerChange();
  };

  let failureRollbackVersionsRestoreCallback = function (oResponse) {
    let aException = oResponse.failure.exceptionDetails;
    let oAssetFileTypeNotSupportedRollbackException = CS.find(aException, {"key": "AssetFileTypeNotSupportedRollbackException"});
    if (CS.isNotEmpty(oAssetFileTypeNotSupportedRollbackException)) {
      alertify.error(`${ContentUtils.getDecodedTranslation(
          getTranslation().AssetFileTypeNotSupportedRollbackException,
          {assetName: oAssetFileTypeNotSupportedRollbackException.itemName})}`);
    } else {
      ContentUtils.failureCallback(oResponse, 'failureArchiveVersionsRestoreCallback', getTranslation());
    }
  };

  let _rollbackArchiveVersion = function (sVersionId) {
    let oActiveContent = ContentUtils.getActiveEntity();
    let sArticleId = oActiveContent.id;
    let sEntityBaseType = oActiveContent.baseType;
    let sScreenMode = ContentUtils.getScreenModeBasedOnEntityBaseType(sEntityBaseType);
    let sUrl = getRequestMapping(sScreenMode).RollbackContentVersion;
    let oCallbackData = {};
    // Below condition is added temporary. Will be removed once DTP entity rollback is supported.
    CS.postRequest(sUrl, {id: sArticleId}, {versionId: sVersionId},
        successRollbackVersionsRestoreCallback.bind(this,oCallbackData),
        failureRollbackVersionsRestoreCallback);
  };

  let _handleTimelineComparisonDialogViewHeaderButtonClicked = function (sButtonId, oFilterContext) {
    if(ContentUtils.isActiveContentDirty(oFilterContext)) {
      ContentUtils.showMessage(getTranslation().THERE_ARE_UNSAVED_CHANGES_PLEASE_SAVE);
      return;
    }
    TimelineProps.setSelectedHeaderButtonId(sButtonId);
    _triggerChange();
  };

  /** prepare comparison data Only after save in comparison dialog **/
  let _setUpComparisonModeTimeLineData = function (oCallbackData) {
    let bIsComparisonMode = TimelineProps.getIsComparisonMode();

    if(bIsComparisonMode) {
      let aSelectedVersionIds = TimelineProps.getSelectedVersionIds();
      /**
       * For Finding previous active version Id,
       * from all selected version ids, Maximum value is the previous active version id.
       * So CS.max used to find previous active version id.
       */

      _fetchComparisonData({dataLanguage: oCallbackData.dataLanguage}, aSelectedVersionIds, {});
    }
  };

  /*******************  PUBLIC API  *******************/
  return {

    handleZoomClicked: function (bIncrement) {
      _changeZoomLevel(bIncrement);
      _triggerChange();
    },

    handleTimelineDeleteVersionZoomButtonClicked: function (sVersionId) {
      _archiveVersionButtonClicked([sVersionId]);
    },

    handleTimelineCompareVersionButtonClicked: function (bIsComparisonMode, aSelectedVersions) {
      _compareVersionButtonClicked(bIsComparisonMode, aSelectedVersions);
    },

    handleLanguageForComparisonChanged: function (sLanguageForComparison, oFilterContext) {
      _handleLanguageForComparisonChanged(sLanguageForComparison, oFilterContext);
    },

    handleTimelineVersionLoadMoreButtonClicked: function (oFilterContext) {
      _loadMoreButtonClicked(oFilterContext);
      _triggerChange();
    },

    handleTimelineVersionDeselectAllButtonClicked: function () {
      TimelineProps.setSelectedVersionIds([]);
      _triggerChange();
    },

    handleTimelineVersionSelectAllButtonClicked : function () {
      _handleTimelineVersionSelectAllButtonClicked();
    },

    handleTimelineVersionDeleteSelectedButtonClicked: function(){
      _deleteSelectedVersionIds();
    },

    handleTimelineSelectVersionButtonClicked: function (sVersionId) {
      _selectVersionButtonClicked(sVersionId);
      _triggerChange();
    },

    handleTimelineVersionShowArchiveButtonClicked : function (bArchiveFlag, bIsLoadMore, oFilterContext) {
      _handleTimelineVersionShowArchiveButtonClicked(bArchiveFlag, bIsLoadMore, oFilterContext);
    },

    handleTimelineRestoreArchiveVersionsButtonClicked : function () {
      _handleTimelineRestoreArchiveVersionsButtonClicked();
    },

    handleTimelineRestoreVersionButtonClicked: function (sVersionId) {
      _timelineRestoreArchivedVersion([sVersionId]);
    },

    handleTimelineRollbackVersionButtonClicked: function (sVersionId) {
      _timelineRollbackArchivedVersion(sVersionId);
    },

    setVersions: function (aVersionList, iFrom) {
      _setVersions(aVersionList, iFrom);
    },

    handleMatchMergeCellClicked: function (sRowId, oProperty, sTableId, sTableGroupName) {
      _handleMatchMergeCellClicked(sRowId, oProperty, sTableId, sTableGroupName);
      _triggerChange();
    },

    handleMatchMergeCellRemoveClicked: function (sRowId, sTableId, sTableGroupName) {
      _handleMatchMergeCellRemoveClicked(sRowId, sTableId, sTableGroupName);
      _triggerChange();
    },

    postProcessVersionMerge: function () {
      _postProcessVersionMerge();
    },

    handleTimelineCompareVersionCloseButtonClicked: function () {
      if (ContentUtils.activeContentSafetyCheck()) {
        var oActiveContent = ContentUtils.getActiveContent();
        var oExtraData = {
          from: 0
        };
        var oCallbackData = {};
        oCallbackData.functionToExecute =  _compareVersionButtonClicked.bind(this, false);

        ContentUtils.getContentStore().fetchArticleById(oActiveContent.id, oCallbackData, oExtraData);
      }
    },

    isComparisonMode: function () {
      return _isComparisonMode();
    },

    handleTimelineComparisonDialogViewHeaderButtonClicked: function (sButtonId, oFilterContext) {
      _handleTimelineComparisonDialogViewHeaderButtonClicked(sButtonId, oFilterContext);
    },

    setUpComparisonModeTimeLineData: function (oCallbackData) {
      _setUpComparisonModeTimeLineData(oCallbackData);
    },

  }
})();

MicroEvent.mixin(TimelineStore);

export default TimelineStore;
