import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import ContentScreenStore from '../store/content-screen-store';
import ContentScreenAction from '../action/content-screen-action';
import RequestMapping from '../../../../../libraries/requestmappingparser/request-mapping-parser.js';
import TagTypeConstants from '../../../../../commonmodule/tack/tag-type-constants';
import ModuleDictionary from './../../../../../commonmodule/tack/module-dictionary';
import ClassNameDictionary from '../../../../../commonmodule/tack/dictionary-for-class-name';
import RelationshipConstants from '../../../../../commonmodule/tack/relationship-constants';
import ContextTypeDictionary from '../../../../../commonmodule/tack/context-type-dictionary';
import PhysicalCatalogDictionary from '../../../../../commonmodule/tack/physical-catalog-dictionary';
import ConfigEntityTypeDictionary from '../../../../../commonmodule/tack/config-entity-type-dictionary';
import AssetUploadContextDictionary from '../../../../../commonmodule/tack/asset-upload-context-dictionary';
import HierarchyTypesDictionary from '../../../../../commonmodule/tack/hierarchy-types-dictionary';
import PortalTypeDictionary from '../../../../../commonmodule/tack/portal-type-dictionary';
import ImageCoverflowItemModel from '../../../../../viewlibraries/imagecoverflowview/model/image-coverflow-item-model';
import GlobalStore from '../../../store/global-store';
import { view as XRayButtonView } from '../view/x-ray-button-view';
import { view as ButtonWithContextMenuView } from '../../../../../viewlibraries/buttonwithcontextmenuview/button-with-context-menu-view';
import { view as BulkUploadButtonView } from '../view/bulk-upload-button-view';
import { view as AssetLinkSharingView } from '../view/bulk-asset-link-sharing-view';
import { view as OnboardingFileUploadButtonView } from '../view/onboarding-file-upload-button-view';
import { view as FilterSearchView } from '../view/filter-search-view';
import { view as ContentTileListView } from '../view/content-tile-list-view';
import ContextMenuViewModel from '../../../../../viewlibraries/contextmenuwithsearchview/model/context-menu-view-model';
import { view as UOMView } from '../view/uom-view';
import { view as ContentScreenWrapperView } from '../view/content-screen-wrapper-view';
import { view as OnBoardingFileMappingView } from '../view/onboarding-file-mapping-view';
import { view as CustomDialogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import { view as BulkDownloadDialogView } from '../view/bulk-download-dialog-view';
import ViewUtils from '../view/utils/view-utils';
import ContentUtils from '../store/helper/content-utils';
import ScreenModeUtils from '../store/helper/screen-mode-utils';
import ImageCoverflowUtils from '../store/helper/image-coverflow-utils';
import CoverflowStore from '../store/helper/image-coverflow-store';
import ExceptionLogger from '../../../../../libraries/exceptionhandling/exception-logger';
import MockDataForKlassTypes from '../../../../../commonmodule/tack/mock-data-for-class-types';
import ContentScreenConstants from '../store/model/content-screen-constants';
import NatureTypeDictionary from '../../../../../commonmodule/tack/nature-type-dictionary';
import ContentToolbarItemList from '../tack/content-toolbar-item-list';
import ContentToolbarItemLayoutData from '../tack/content-toolbar-item-layout-data';
import EntityBaseTypeDictionary from '../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import HorizontalToolbarItemList from '../tack/horizontal-toolbar-item-list';
import ContentHierarchyContextMenuList from '../tack/content-hierarchy-context-menu-list';
import ContentEditFilterItemsDictionary from '../tack/content-edit-filter-items-dictionary';
import CommonUtils from '../../../../../commonmodule/util/common-utils';
import ThumbnailModeConstants from '../../../../../commonmodule/tack/thumbnail-mode-constants';
import ContentEditToolbarData from '../tack/mock/mock-data-for-content-edit-toolbar';
import SessionProps from '../../../../../commonmodule/props/session-props';
import ContentScreenViewContextConstants from './../tack/content-screen-view-context-constants';
import TemplateTabsDictionary from '../../../../../commonmodule/tack/template-tabs-dictionary';
import FilterItemsList from '../tack/content-edit-filter-items-list';
import DashboardTabDictionary from '../screens/dashboardscreen/tack/dashboard-tab-dictionary';
import SessionStorageManager from '../../../../../libraries/sessionstoragemanager/session-storage-manager';
import SessionStorageConstants from '../../../../../commonmodule/tack/session-storage-constants';
import { ContextList } from '../../../../../commonmodule/contexts/context-creator';
import { view as FlatCreateButtonView } from './../../../../../viewlibraries/createbuttonview/flat-create-button-view';
import {view as UploadAssetDialogView} from '../view/upload-asset-dialog-view';
import oFilterPropType from './../tack/filter-prop-type-constants';
import CouplingConstant from './../../../../../commonmodule/tack/coupling-constans';
import TreeViewProps from '../../../../../viewlibraries/treeviewnew/store/model/tree-view-props';
import MomentUtils from "../../../../../commonmodule/util/moment-utils";
import { Logger } from "../../../../../libraries/logger";
import ConfigDataEntitiesDictionary from "../../../../../commonmodule/tack/config-data-entities-dictionary";
import ContentScreenProps from "../store/model/content-screen-props";
import MultiClassificationDialogToolbarLayoutData from "../tack/multiclassification-dialog-toolbar-layout-data";
import {bulkEditToolbarData as BulkEditToolBarData, bulkEditTabTypesConstants as BulkEditTabTypesConstants} from "../tack/bulk-edit-layout-data";
import BreadCrumbModuleAndHelpScreenIdDictionary from "../../../../../commonmodule/tack/breadcrumb-module-and-help-screen-id-dictionary";
import DashboardScreenProps from "../screens/dashboardscreen/store/model/dashboard-screen-props";
import JobScreenProps from "../store/model/job-screen-view-props";
import EndpointTypeDictionary from "../../../../../commonmodule/tack/endpoint-type-dictionary";
import { communicator as HomeScreenCommunicator } from "../../../store/home-screen-communicator";
import AllCategoriesTaxonomiesData
  from "../../../../../commonmodule/tack/mock-data-for-all-categories-taxonomy-selector";

const getTranslation = ScreenModeUtils.getTranslationDictionary;
const getRequestMapping = ScreenModeUtils.getScreenRequestMapping;

const oPropTypes = {
  mode: ReactPropTypes.string,
  quickViewData: ReactPropTypes.object,
  screenMasterData: ReactPropTypes.object,
  isInsideDataIntegration: ReactPropTypes.bool
};

// @CS.SafeComponent
class ContentScreenController extends React.Component {

  //@required: Props
  static propTypes = oPropTypes;
  static childContextTypes = {
    currentZoomValue: ReactPropTypes.number,
      currentModuleId: ReactPropTypes.string,
      masterTagList: ReactPropTypes.object,
      masterAttributeList: ReactPropTypes.array,
      masterUserList: ReactPropTypes.array,
      staticCollectionList: ReactPropTypes.array
  };
  constructor(props, context) {
    super(props, context);

    const oStore = this.getStore();
    this.refreshAll(props.mode || "");

    this.state = {
      appData: oStore.getData().appData,
      componentProps: oStore.getData().componentProps,
      headerText: getTranslation().ARTICLES_LABEL,
      mode: props.mode
    };

  }

  static getDerivedStateFromProps (oNextProps, oState) {
    if(oNextProps.mode !== oState.mode) {
      let oStore = ContentScreenStore;
      oStore.resetAll();
      CommonUtils.setHistoryStateToNull(oStore.refreshAll.bind(this, oNextProps.mode || ""));
      return {
        mode: oNextProps.mode
      };
    }
    return null;
  }

  //@Bind: Store with state
  componentDidMount() {
    HomeScreenCommunicator.setContentScreenLoaded(true);
    this.debouncedStateChanged = CS.debounce(this.contentStateChanged, 10);
    this.getStore().bind('change', this.debouncedStateChanged);
    this.getAction().registerEvent();
  }

  componentDidUpdate() {
    this.getStore().stopSCUUpdate();
    if (global.startTime) {
      let sEndTime = new Date().toISOString();
      Logger.log(`${MomentUtils.getTimeDifference(global.startTime, sEndTime)}s was taken for the render all the affected components.`);
      Logger.log("Metadata", {
        startTime: global.startTime,
        endTime: sEndTime,
        component: "ContentScreenController",
        timeTaken: `${MomentUtils.getTimeDifference(global.startTime, sEndTime)}s`
      });
      global.startTime = "";
    }
  }

  //@UnBind: store with state
  componentWillUnmount() {
    ContentUtils.resetContentScreenState();
    this.getStore().unbind('change', this.debouncedStateChanged);
    this.getAction().deRegisterEvent();
  }

  //@set: state
  contentStateChanged = () => {
    var changedState = {
      appData: this.getStore().getData().appData,
      componentProps: this.getStore().getData().componentProps,
      headerText: getTranslation().ARTICLES_LABEL
    };

    this.setState(changedState);
  };

  getReferencedTags = () => {
    let oComponentProps = this.getComponentProps();

    let oReferencedTags = oComponentProps.screen.getReferencedTags();
    return CS.assign({}, oReferencedTags);
  };

  //Do NOT remove below function
  getChildContext() {
    let oAppData = this.getAppData();
    let oActiveCollection = this.getActiveCollection();
    let sActiveCollectionId = oActiveCollection.id;
    let aStaticCollectionList = oAppData.getStaticCollectionList();
    if(oActiveCollection.type === "staticCollection"){
      CS.remove(aStaticCollectionList,{id: sActiveCollectionId});
    }

    return {
      currentZoomValue: this.getCurrentZoomValue(),
      currentModuleId: ContentUtils.getSelectedModuleId(),
      masterTagList: this.getReferencedTags(),
      masterAttributeList: this.getMasterAttributeList(),
      masterUserList: oAppData.getUserList(),
      staticCollectionList: aStaticCollectionList
    };
  }

  refreshAll = (sMode) => {
    let oStore = this.getStore();
    oStore.resetAll();
    CommonUtils.setHistoryStateToNull(oStore.refreshAll.bind(this, sMode));
  };

  getStore = () => {
    return ContentScreenStore;
  };

  getAction = () => {
    return ContentScreenAction;
  };

  getComponentProps = () => {

    return this.state.componentProps;
  };

  getAppData = () => {

    return this.state.appData;
  };

  getActiveContent = () => {
    var oActiveContent = this.getComponentProps().screen.getActiveContent();

    return oActiveContent.contentClone ? oActiveContent.contentClone : oActiveContent;
  };

  getActiveEntity = () => {
    var oActiveEntity = ContentUtils.getActiveEntity();

    return oActiveEntity.contentClone ? oActiveEntity.contentClone : oActiveEntity;
  };

  getIsContentDisabled = () => {
    return !ContentUtils.isContentAvailableInSelectedDataLanguage() ||
        ContentUtils.getIsCurrentUserReadOnly();
  };

  getDefaultVerticalMenuItems = (oVerticalMenu, sChildClassName) => {
    var oAppData = this.getAppData();
    var aDefaultTypes = CS.cloneDeep(oAppData.getDefaultTypes());

    let aChildTypes = CS.map(aDefaultTypes, function (oType) {
      if (!oType.icon) {
        oType.className = sChildClassName;
      }
      oType.isFolder = false;
      return oType;
    });
    return aChildTypes;
  };

  getVerticalMenu = () => {
    var oVerticalMenu = {};
    oVerticalMenu.list = this.getDefaultVerticalMenuItems(oVerticalMenu, "articleMenuItem");
    oVerticalMenu.context = "contentTile";
    let sSearchText = ContentScreenProps.screen.getDefaultTypesSearchText();
    oVerticalMenu.searchText = sSearchText;
    oVerticalMenu.isHideCreateButton = ContentUtils.isHideCreateButton() && CS.isEmpty(sSearchText);
    return oVerticalMenu;
  };

  getApplicationType = () => {
    var oGlobalModulesData = GlobalStore.getGlobalModulesData();
    return oGlobalModulesData.type;
  };

  isOnboarding = () => {
    return this.getApplicationType() == "onboarding";
  };

  getContentOwnerMultiSelectModel = (bIsDisabled) => {
    var oActiveContent = this.getActiveContent();
    var aMasterUserList = this.getAppData().getUserList();
    var sOwnerId = oActiveContent.owner;
    var sTypeLang = getTranslation().OWNER;
    var aList = [];

    CS.forEach(aMasterUserList, function(oOwner){
      aList.push({
        id: oOwner.id,
        label: CS.trim(oOwner.lastName || "" + " " + oOwner.firstName || "")
      })
    });

    return {
      disabled: bIsDisabled,
      label: sTypeLang,
      items: aList,
      selectedItems: [sOwnerId],
      singleSelect: true,
      context: 'owner',
      disableCross: true
    }
  };

  getNatureTypeClassData = () => {
    const oComponentProps = this.getComponentProps();
    const oActiveContent = this.getActiveEntity();
    const sSelectedTypeId = ViewUtils.getEntityClassType(oActiveContent);
    const oReferencedClasses = oComponentProps.screen.getReferencedClasses();
    try {
      return CS.getLabelOrCode(oReferencedClasses[sSelectedTypeId]);
    }
    catch(oException){
      ExceptionLogger.error("Referenced Class Not Found : ");
      ExceptionLogger.error(oException);
    }
  };

  checkVideoAssetStatus = (oImage, oProperty, oCoverflowProps, sCoverflowContext, sContext) => {
    if ((oImage.properties.mp4).indexOf('#') == -1) {
      oProperty.mp4 = (RequestMapping.getRequestUrl(getRequestMapping().GetAssetImage, {
        type: oImage.type, id: oImage.properties.mp4 }) + "/");
    } else {
      oProperty.mp4 = oImage.properties.mp4;
      var aCurrentVideoStatusCalls = ImageCoverflowUtils.getActiveVideoStatusRequests();

      var bIsCoverflowDetailsOpen = this.getCoverflowDialogStatus(sCoverflowContext, sContext);
      if (bIsCoverflowDetailsOpen && (aCurrentVideoStatusCalls.indexOf(oImage.assetObjectKey) == -1)) {
        aCurrentVideoStatusCalls.push(oImage.assetObjectKey);
        CoverflowStore.getAssetStatus(oImage);
      }
    }
    oProperty.status = oImage.properties.status;
  };

  getCoverflowDialogStatus = (sAttributeId, sContext) => {
    switch (sContext) {
      case "contentHeader":
        return ImageCoverflowUtils.getIsHeaderImageDialogOpen();
      case "attribute":
        return ImageCoverflowUtils.getIsAttributeImageDialogOpen();
      case "editAsset":
        return ImageCoverflowUtils.getIsEditImageDialogOpen();
    }
  };

  getImageCoverFlowModelList = (sAttributeId, sContext, oElement) => {
    var oComponentProps = this.getComponentProps();
    var iCoverFlowActiveIndex = ImageCoverflowUtils.getImageCoverflowActiveIndex();
    var aCoverFlowSelectedImages = ImageCoverflowUtils.getSelectedImages();
    var oActiveEntity = ContentUtils.getDirtyActiveEntity();
    oActiveEntity = oActiveEntity.contentClone || oActiveEntity;

    var aUploadedImageCoverFlowModelList = [];
    var aImageCoverFlowModelList = [];
    var isUpload = false;
    let bIsImageEditable = false;
    var bIsImageVariant = oElement && oElement.isImageVariant;
    if (oActiveEntity.baseType == EntityBaseTypeDictionary.assetBaseType) {
      let aCoverflowAttributes = bIsImageVariant ? (CS.isNull(oElement.assetInformation) ? [] : [oElement.assetInformation]) : [oActiveEntity.assetInformation];
      let _this = this;
      CS.forEach(aCoverflowAttributes, function (oImage, iIndex) {
              var oProperty = {
                  tags: oImage.tags,
                  extension: oImage.properties ? oImage.properties.extension : ""
              };
              var oCoverflowProps = _this.getComponentProps().imageCoverflowViewProps;
              if ((oImage.properties) && (oImage.type == 'Video')) {
                  _this.checkVideoAssetStatus(oImage, oProperty, oCoverflowProps, sAttributeId, sContext);
              }
              var sImageSrc = '';
              var sImageOriginalSrc = '';
              var sPreviewImageSrc = '';
              var sType = '';
              var bImageIsActive = (iIndex == iCoverFlowActiveIndex);
              var bImageIsSelected = false;
              if (aCoverFlowSelectedImages[oImage.assetObjectKey]) {
                  bImageIsSelected = true;
              }
              if (oImage.copyOf) {
                  sImageSrc = (oImage.byteStream) ? oImage.byteStream : "contentImage/getByteStream/" + oImage.copyOf;
              } else {
                  sImageSrc = (oImage.byteStream) ? oImage.byteStream : (RequestMapping.getRequestUrl(getRequestMapping().GetAssetImage, {
                      type: oImage.type,
                      id: oImage.thumbKey
                  }) + "/");
                  if (oImage.assetObjectKey && oImage.assetObjectKey.indexOf("#") == -1) {
                      sImageOriginalSrc = (oImage.byteStream) ? oImage.byteStream : (RequestMapping.getRequestUrl(getRequestMapping().GetAssetImage, {
                          type: oImage.type,
                          id: oImage.assetObjectKey
                      }) + "/");
                  } else {
                      sImageOriginalSrc = sImageSrc;
                  }
                  if (oImage.previewImageKey) {
                      sPreviewImageSrc = RequestMapping.getRequestUrl(getRequestMapping().GetAssetImage, {
                          type: oImage.type,
                          id: oImage.previewImageKey
                      }) + "/";
                  }
                  sType = oImage.type;
                  if (!oImage.thumbKey) {
                      sImageSrc = oImage.thumbKey;
                  }
              }
        let oImageProperties = oImage.properties;
        bIsImageEditable = ContentUtils.getSelectedModuleId() === ModuleDictionary.MAM && (oImageProperties && (oImageProperties.extension === "jpeg" || oImageProperties.extension === "png" || oImageProperties.extension === "gif" || oImageProperties.extension === "jpg"));
        var imageCoverflowModel = new ImageCoverflowItemModel(oImage.assetObjectKey, oImage.id, sImageSrc, bImageIsActive, bImageIsSelected, oImage.isDefault,
          sImageOriginalSrc, sPreviewImageSrc, sType, oImage.fileName, oImage.description, oProperty);
        aImageCoverFlowModelList.push(imageCoverflowModel);
        if (oImage.isNewlyUploaded) {
          isUpload = true;
          aUploadedImageCoverFlowModelList.push(imageCoverflowModel);
        }
      });
    }
    var oImageCoverFlowModelList = {};
    oImageCoverFlowModelList.activeIndex = iCoverFlowActiveIndex;
    oImageCoverFlowModelList.coverflowItems = aImageCoverFlowModelList;
    oImageCoverFlowModelList.uploadedCoverflowItems = aUploadedImageCoverFlowModelList.length > 0? aUploadedImageCoverFlowModelList: aImageCoverFlowModelList ;
    oImageCoverFlowModelList.isUpload = isUpload;
    oImageCoverFlowModelList.context = sContext;
    oImageCoverFlowModelList.isDialogOpen = this.getCoverflowDialogStatus(sAttributeId, sContext);
    oImageCoverFlowModelList.isImageEditDialogOpen = this.getCoverflowDialogStatus(sAttributeId, "editAsset");
    oImageCoverFlowModelList.isImageEditable = bIsImageEditable;
    if (bIsImageVariant) {
      /** Add button keys in hideToolBarButtons which are not needed to be rendered */
      oImageCoverFlowModelList.hideToolBarButtons = ["addImage", "downloadButton"];
      oImageCoverFlowModelList.imageVariantData = {
        isImageVariant: true,
        contextId: oElement.contextId,
        elementId: oElement.elementId,
        elementLabel: oElement.elementLabel,
        canDelete: oElement.canDelete,
      }
    }

    /**
     * annotationData : X,Y, isAnnotationOpen
     * showAnnotation
     * **/
    let oTaskData = oComponentProps.taskProps.getTaskData();
    oTaskData.showAnnotation = oComponentProps.taskProps.getShowAnnotations();
    oImageCoverFlowModelList.imageAnnotationData = oTaskData;

    //changes for assetDownloadData
    if (ContentUtils.getSelectedTabId() === TemplateTabsDictionary.PROPERTY_COLLECTION_TAB) {
      let oAssetDownloadData = ContentUtils.getDownloadDialogData();
      oImageCoverFlowModelList.assetDownloadData = oAssetDownloadData;
    }

    return oImageCoverFlowModelList;
  };

  getTaxonomyTypeMultiSelectModel = (oElement) => {
    var oAppData = this.getAppData();
    var oActiveEntity = this.getActiveEntity();

    var aList = [];

    var aTaxonomyList = oAppData.getTaxonomyList();

    if (aTaxonomyList.length) {
      CS.forEach(aTaxonomyList, function (oTaxonomy) {
        if (!CS.isEmpty(oTaxonomy)) {
          aList.push({
            id: oTaxonomy.id,
            label: CS.getLabelOrCode(oTaxonomy)
          });
        }
      });
    }

    return {
      disabled: oElement.isDisabled,
      label: oElement.label || oElement.attribute.label,
      items: aList,
      selectedItems: oActiveEntity.selectedTaxonomyIds,
      singleSelect: false,
      context: 'taxonomyViewType',
      disableCross: false
    };
  };

  getTaskDialogData = () => {
    var oComponentProps = this.getComponentProps();
    var oScreenProps = oComponentProps.screen;
    var oTaskProps = oComponentProps.taskProps;
    var oTaskData = oTaskProps.getTaskData();
    let oActiveContent = ContentUtils.getActiveContent();

    if(oTaskProps.getIsTaskDialogActive()) {
      let oData = {
        isDialogOpen: oTaskProps.getIsTaskDialogActive(),
        templateId: ContentUtils.getTemplateIdForServer(oScreenProps.getSelectedTemplateId()),
        screenMode: ContentUtils.getScreenModeBasedOnEntityBaseType(oActiveContent.baseType),
        propertyIdsHavingTask: oTaskProps.getPropertyIdsHavingTask()
      };

      return CS.combine(oTaskData, oData);
    }
  };

  fillSectionAttributeWithModel = (oElement, oEntity) => {

    var _this = this;
    var oComponentProps = this.getComponentProps();
    var oScreenProps = oComponentProps.screen;
    var oMasterAttribute = oElement.attribute;
    var aEntityAttributes = oEntity.attributes;
    var oVariantVersionMap = oComponentProps.contentSectionViewProps.getSectionElementVariantVersionMap();
    oElement.variantVersionProps = oVariantVersionMap[oElement.id];
    oElement.attributeVariantsStats = oScreenProps.getAttributeVariantsStats()[oElement.id] || {};
    var oFilterObject = {attributeId: oMasterAttribute.id};
    var oContentTypeModel = {};
    var aFilteredEntityAttributes = [];

    //for task dialog
    var oAttribute = CS.find(aEntityAttributes, {attributeId: oElement.id}) || {};

    oElement.taskDialogData = this.getTaskDialogData(oAttribute);

    var aPropertyIdsHavingTasks = oComponentProps.taskProps.getPropertyIdsHavingTask();
    oElement.isTasksExists = CS.includes(aPropertyIdsHavingTasks, oAttribute.attributeId);

    //when attribute is contextual -
    if (!CS.isEmpty(oMasterAttribute.attributeTags)) {
      oElement.contextualAttributeData = this.getContextualAttributeData(oMasterAttribute.id, aEntityAttributes);
      oFilterObject = function (oAttribute) {
        return ((oAttribute.attributeId == oMasterAttribute.id) && (CS.isEmpty(oAttribute.tags))); //original attribute instance will not have any tags set
      }
    }

    if (ViewUtils.isAttributeTypeTaxonomy(oMasterAttribute.type)) {
      oContentTypeModel = _this.getTaxonomyTypeMultiSelectModel(oElement);
      oElement.model = oContentTypeModel;
      oElement.contentAttributes = CS.filter(oEntity.attributes, oFilterObject);
    }
    else if (ViewUtils.isAttributeTypeCoverflow(oMasterAttribute.type)) {
        if(oEntity.baseType != EntityBaseTypeDictionary.assetBaseType) {
          aEntityAttributes = oEntity.referencedAssets;
        }
        //will not be set when no image is added to the coverflow
        if(!ImageCoverflowUtils.getPropsForContext()) {
          ImageCoverflowUtils.setDefaultPropsForContext();
        }
        var oImageCoverflowViewModel = {
          imageCoverflowActiveIndex: ImageCoverflowUtils.getImageCoverflowActiveIndex(),
          coverflowSelectedImages: ImageCoverflowUtils.getSelectedImages(),
          coverflowImageModels: _this.getImageCoverFlowModelList(oMasterAttribute.id, "attribute", oElement),
          isDialogOpen: ImageCoverflowUtils.getIsAttributeImageDialogOpen(),
          mamMode: true
        };

        var iMaxCoverflowItemAllowed = 1;
        var bIsAddButtonEnable  = false;
        var bIsRemoveButtonEnable  = false;
        if(oImageCoverflowViewModel.coverflowImageModels && oImageCoverflowViewModel.coverflowImageModels.coverflowItems.length > 0) {
          bIsRemoveButtonEnable = true;
        }
        if((oImageCoverflowViewModel.coverflowImageModels
            && oImageCoverflowViewModel.coverflowImageModels.coverflowItems.length < iMaxCoverflowItemAllowed)
            || iMaxCoverflowItemAllowed == -1) {
          bIsAddButtonEnable = true;
        }
        oImageCoverflowViewModel.isAddButtonEnable = bIsAddButtonEnable;
        oImageCoverflowViewModel.isRemoveButtonEnable = bIsRemoveButtonEnable;
        oElement.model = oImageCoverflowViewModel;

        aFilteredEntityAttributes = CS.filter(aEntityAttributes, oFilterObject);
        oElement.contentAttributes = CS.isEmpty(aFilteredEntityAttributes) ? [] : aFilteredEntityAttributes;
    }
    else if (ViewUtils.isRoleTypeOwner(oMasterAttribute.type)) {
      oElement.model = _this.getContentOwnerMultiSelectModel(oElement.isDisabled);
    }
    else if (ViewUtils.isAttributeTypeName(oMasterAttribute.type)) {
        aFilteredEntityAttributes = CS.filter(aEntityAttributes, oFilterObject);
        oElement.contentAttributes = aFilteredEntityAttributes;

    }
    else if (ViewUtils.isAttributeTypeDueDate(oMasterAttribute.type)) {
      aFilteredEntityAttributes = CS.filter(aEntityAttributes, oFilterObject);
      if (!CS.isEmpty(aFilteredEntityAttributes)) {
        oElement.contentAttributes = aFilteredEntityAttributes;
      }
    }
    else if (ViewUtils.isAttributeTypeDescription(oMasterAttribute.type)) {
      aFilteredEntityAttributes = CS.filter(aEntityAttributes, oFilterObject);
      if (!CS.isEmpty(aFilteredEntityAttributes)) {
        oElement.contentAttributes = aFilteredEntityAttributes;
      }
    }
    else if (ViewUtils.isAttributeTypeCreatedOn(oMasterAttribute.type)) {
      aFilteredEntityAttributes = CS.filter(aEntityAttributes, oFilterObject);
      if (!CS.isEmpty(aFilteredEntityAttributes)) {
        oElement.contentAttributes = aFilteredEntityAttributes;
      }
    }
    else if (ViewUtils.isAttributeTypeUser(oMasterAttribute.type)) {
        oElement.isUserDeleted = false;
        aFilteredEntityAttributes = CS.filter(aEntityAttributes, oFilterObject);
        if (!CS.isEmpty(aFilteredEntityAttributes)) {
          CS.forEach(aFilteredEntityAttributes, function (oAttribute) {
            if (!ViewUtils.getUserById(oAttribute.value)) {
              oElement.isUserDeleted = true;
            }
          });
          oElement.contentAttributes = aFilteredEntityAttributes;
        }
    }
    else if (ViewUtils.isConcatenatedAttribute(oMasterAttribute.type)) {
      var aReferencedAttributeList = _this.getComponentProps().screen.getReferencedAttributes();

      var oMaster = oElement.attribute;
      var oEntityAttribute = CS.find(aEntityAttributes, {attributeId: oMaster.id});

      var aExpressionList = oEntityAttribute && !CS.isEmpty(oEntityAttribute.valueAsExpression) ? oEntityAttribute.valueAsExpression : oMaster.attributeConcatenatedList;
      var aTemp = [];
      CS.forEach(aExpressionList, function (oExpression) {
        var sBaseType = "";
        var sType = oExpression.type;
        var sValue = oExpression.value || "";
        var sValueAsHtml = oExpression.valueAsHtml || "";
        var sEntityId = oExpression.attributeId;
        if( sType === "attribute"){
          var oEntityAttribute = CS.find(aEntityAttributes, {attributeId: sEntityId});
          sValue = oEntityAttribute ? oEntityAttribute.value : ContentUtils.getDecodedTranslation( getTranslation().ENTITY_NOT_AVAILABLE, {entity : getTranslation().ATTRIBUTE} );

          var oMasterAttribute = CS.find(aReferencedAttributeList, {id: sEntityId});
          if(CS.isEmpty(oMasterAttribute)){
            sBaseType = "";
            sValue = ContentUtils.getDecodedTranslation( getTranslation().ENTITY_NOT_AVAILABLE, {entity : getTranslation().ATTRIBUTE} );
          }else {
            sBaseType = oMasterAttribute.type;
          }

          if(ViewUtils.isAttributeTypeMeasurement(sBaseType)){
            var sBaseUnit = ViewUtils.getBaseUnitFromType(sBaseType);
            sValue = ViewUtils.getMeasurementAttributeValueToShow(sValue, sBaseUnit, oMasterAttribute.defaultUnit, oMasterAttribute.precision);
            sValue += "";
            if(!CS.isEmpty(sValue)){
              var sUnitToDisplay = ContentUtils.getDisplayUnitFromDefaultUnit(oMasterAttribute.defaultUnit, sBaseType);
              sValue = ContentUtils.getCurrentLocaleNumberValue(sValue, oMasterAttribute.precision) + " " + sUnitToDisplay;
            }
          }
          else if(ViewUtils.isAttributeTypeDate(sBaseType)){
            sValue = ViewUtils.formatDate(sValue);
          }
          else if(ViewUtils.isAttributeTypeRole(sBaseType) || ViewUtils.isAttributeTypeUser(sBaseType)){
            sValue = ViewUtils.getUserNameById(sValue);
          }
          else if(ViewUtils.isAttributeTypeHtml(sBaseType) && !CS.isEmpty(oEntityAttribute)){
              sValue = ViewUtils.getDecodedHtmlContent(oEntityAttribute.valueAsHtml || "");
              sValueAsHtml = oEntityAttribute.valueAsHtml;
          }
          else if(ViewUtils.isAttributeTypeNumber(sBaseType) && !CS.isEmpty(oEntityAttribute)){
            sValue = ContentUtils.getCurrentLocaleNumberValue(oEntityAttribute.valueAsNumber, oMasterAttribute.precision);
          }
        } else if (sType === "tag") {
          /** To set visual tag value label from referenced tag **/
          sEntityId = oExpression.tagId;
          let oReferencedTags = _this.getComponentProps().screen.getReferencedTags();
          let oMasterTag = oReferencedTags[sEntityId] || {};
          if(CS.isEmpty(oMasterTag)){
            sValue = ContentUtils.getDecodedTranslation( getTranslation().ENTITY_NOT_AVAILABLE, {entity : getTranslation().TAG});
          }else {
            let aChildren = oMasterTag.children;
            let aTagValueSequence = oMasterTag.tagValuesSequence;
            let aContentTags = oEntity.tags;
            let oTag = CS.find(aContentTags, {tagId: sEntityId});
            if (!CS.isEmpty(oTag)) {
              let aTagValues = oTag.tagValues;

              /** Show only first selected tag value according to tag value sequences **/
              let aSelectedTagValues = CS.filter(aTagValues, {relevance: 100});
              let aSelectedTagValuesIds = !CS.isEmpty(aSelectedTagValues) ? CS.map(aSelectedTagValues, "tagId") : [];
              let oFirstSelectedMasterTagValueBySequence = {};
              if (!CS.isEmpty(aSelectedTagValuesIds)) {
                let sFirstSelectedTagValueIdBySequence = CS.find(aTagValueSequence, function (sTagId) {
                  return CS.includes(aSelectedTagValuesIds, sTagId);
                });
                oFirstSelectedMasterTagValueBySequence = CS.find(aChildren, {id: sFirstSelectedTagValueIdBySequence});
              }

              if(!CS.isEmpty(oFirstSelectedMasterTagValueBySequence)) {
                sValue = oMaster.isCodeVisible ? oFirstSelectedMasterTagValueBySequence.code : oFirstSelectedMasterTagValueBySequence.label;
              }
            }
          }
        }
        else if(sType === "html"){
          sValueAsHtml = oExpression.valueAsHtml;
          sValue = oExpression.value;
        }

        aTemp.push({
          baseType: sBaseType,
          type: sType,
          value: sValue || "",
          valueAsHtml: sValueAsHtml || "",
          entityId: sEntityId,
          id: oExpression.id
        });
      });
      oElement.expressionList = aTemp;

      aFilteredEntityAttributes = CS.filter(aEntityAttributes, oFilterObject);
      if (!CS.isEmpty(aFilteredEntityAttributes)) {
        oElement.contentAttributes = aFilteredEntityAttributes;
      }
    }

    if(ViewUtils.isAttributeTypeCalculated(oMasterAttribute.type)){
      oElement.isDisabled = true;
      aFilteredEntityAttributes = CS.filter(aEntityAttributes, oFilterObject);
      if (!CS.isEmpty(aFilteredEntityAttributes)) {
        oElement.contentAttributes = aFilteredEntityAttributes;
      }
    }
    else {
        //DATAMIGRATION: change to mappingId
        aFilteredEntityAttributes = CS.filter(aEntityAttributes, oFilterObject);
        if (!CS.isEmpty(aFilteredEntityAttributes)) {
          oElement.contentAttributes = aFilteredEntityAttributes;
        }
    }
  };

  getAddedClassificationListForTaxonomies = (oParent, aSelectedClassification, sRootNodeId) => {
      if (oParent.id == sRootNodeId) {
          return;
      }
      !CS.isEmpty(oParent.parent) && this.getAddedClassificationListForTaxonomies(oParent.parent, aSelectedClassification, sRootNodeId);
      aSelectedClassification.push(oParent);
  };

  fillSectionTaxonomyWithModel = (oElement) => {
    var _this = this;
    var oComponentProps =  this.getComponentProps();
    var aSelectedTaxonomyIds = oComponentProps.screen.getActiveTaxonomyIds();
    var aRemainingTaxonomyList = [];
    var aTaxonomyTree = oComponentProps.screen.getAllowedTaxonomies();
    let oAllowedTaxonomyHierarchyList = oComponentProps.screen.getAllowedTaxonomyHierarchyList();

    CS.forEach(aTaxonomyTree, function (oTaxonomy){
        if(oTaxonomy.id != -1) {
            if(!CS.includes(aSelectedTaxonomyIds, oTaxonomy.id)) {
                aRemainingTaxonomyList.push(oTaxonomy);
            }
        }
    });
    var aSelectedTaxonomy = [];
    var oReferencedTaxonomy = oComponentProps.screen.getReferencedTaxonomies();
    var oMinorTaxonomy = oReferencedTaxonomy[oElement.id];
    if (oMinorTaxonomy) {
        oElement.label = CS.getLabelOrCode(oMinorTaxonomy.rootNodeInfo);
    }
    CS.forEach(aSelectedTaxonomyIds, function (sTaxonomyId) {
        var oRTaxonomy = oReferencedTaxonomy[sTaxonomyId];
        var aSelectedClassification = [];
        if(!CS.isEmpty(oRTaxonomy) && oRTaxonomy.rootNodeInfo.id == oElement.id) {
            oElement.label = CS.getLabelOrCode(oRTaxonomy.rootNodeInfo);
            if (oRTaxonomy.id != oRTaxonomy.rootNodeInfo.id) {
                _this.getAddedClassificationListForTaxonomies(oRTaxonomy, aSelectedClassification, oRTaxonomy.rootNodeInfo.id);
                var oSelectedTaxonomy = {};
                oSelectedTaxonomy.id = sTaxonomyId;
                oSelectedTaxonomy.addedClassificationList = aSelectedClassification;
                oSelectedTaxonomy.furtherClassificationList = aRemainingTaxonomyList;
                aSelectedTaxonomy.push(oSelectedTaxonomy);
            }
        }
    });

    oElement.model = {selected: aSelectedTaxonomy, notSelected: aRemainingTaxonomyList, activeEntitySelectedTabId: ContentUtils.getSelectedTabId()};
    oElement.paginationData = oComponentProps.screen.getAllowedTaxonomiesPaginationData();
    oElement.allowedTaxonomyHierarchyList = oAllowedTaxonomyHierarchyList;
  };

  fillSectionTagWithModel = (oElement, aElementsToRemove, oEntity, sContext, oHierarchyData, oFilterContext) => {
    var oComponentProps = this.getComponentProps();
    var oReferencedTag = oComponentProps.screen.getReferencedTags()[oElement.id];
    var oMasterTag = !CS.isEmpty(oReferencedTag) ? oReferencedTag : oElement.tag;

    var oVariantVersionMap = oComponentProps.contentSectionViewProps.getSectionElementVariantVersionMap();
    oElement.variantVersionProps = oVariantVersionMap[oElement.id];
    var aEntityTag = oEntity.tags;
    var oTag = CS.find(aEntityTag, {tagId: oElement.id}) || {};

    oElement.taskDialogData = this.getTaskDialogData(oTag);
    var aPropertyIdsHavingTasks = oComponentProps.taskProps.getPropertyIdsHavingTask();
    oElement.isTasksExists = CS.includes(aPropertyIdsHavingTasks, oTag.id);
    if (CS.isEmpty(oMasterTag)) {
      aElementsToRemove.push(oElement);
    } else {
      oElement.tagType = oMasterTag.tagType;
      oElement.model = CommonUtils.getTagGroupModels(oMasterTag, oEntity, oElement, sContext, oHierarchyData, oFilterContext);
      oElement.contentTags = CS.filter(oEntity.tags, {tagId: oMasterTag.id});
    }
  };

  removeElementsHavingNoReadPermission = (oElements) => {
    var aElementsToRemove = [];
    CS.forEach(oElements, function (oElement, sKey) {
      oElement.type !== "divider" && !oElement.canRead && aElementsToRemove.push(sKey);
    });
    CS.forEach(aElementsToRemove, function (sKey) {
      delete oElements[sKey];
    });
  };

  getXRaySettingsData = (sRelationshipId) => {
    var oComponentProps = this.getComponentProps();
    var oAppData = this.getAppData();
    var aXRayPropertyGroupList = oComponentProps.screen.getXRayPropertyGroupList();
    var oActiveXRayPropertyGroup = ContentUtils.getActiveXRayPropertyGroup(sRelationshipId);
    return {
      attributeMap: oAppData.getAttributeListMap(),
      tagMap: oAppData.getTagListMap(),
      searchText: oComponentProps.screen.getEntitySearchText(),
      propertyGroupList: aXRayPropertyGroupList,
      activeXRayPropertyGroup: CS.cloneDeep(oActiveXRayPropertyGroup),
    };
  };

  getSectionViewObjectFilledWithModels = (sContext, aActiveSections, oActiveContent) => {
  var oComponentProps = this.getComponentProps();
  var oContentSectionViewProps = oComponentProps.contentSectionViewProps;
  var aSectionsToUpdate = oContentSectionViewProps.getSectionsToUpdate();

    if(CS.isEmpty(oActiveContent) || CS.isEmpty(aActiveSections)) {
      return [];
    }

     if(sContext == "variantSections"){
        aActiveSections = oComponentProps.screen.getActiveVariantSections();
    } else {
      if (oActiveContent.variantInstanceId) {
        aActiveSections = oComponentProps.screen.getActiveVariantSections();
      }
      else {
        //don't reset active sections if context is for Relationship Object
        if (sContext != 'relationshipObject') {
          aActiveSections = CS.filter(aActiveSections, function (oActiveSection) {
            return !!oActiveSection.fromRelationshipTab;
          });
        }
      }
    }


  var aSectionData = this.getSectionsWithElementsMap(aActiveSections);

  var oSelectionObject = oComponentProps.screen.getSetSectionSelectionStatus();
  var bRelationshipSelectionStatus =  oSelectionObject["relationshipContainerSelectionStatus"];
  var oSelectedRelationshipElements =  oComponentProps.relationshipView.getSelectedRelationshipElements();
  var oRelationshipToolbarProps = oComponentProps.relationshipView.getRelationshipToolbarProps();
  var oRelationshipViewMode = oComponentProps.screen.getRelationshipViewMode();

  var _this = this;
  var aSectionModel = [];
  var oSectionVisualProps = oComponentProps.contentSectionViewProps.getSectionVisualProps();
  if(!CS.isEmpty(oActiveContent)) {
    var bIsVariant = !!oActiveContent.variantInstanceId;
    CS.forEach(aSectionData, function (oSection) {
      if (oSectionVisualProps[oSection.id] && oSectionVisualProps[oSection.id].isHidden) {
        return;
      }
      var oRelationshipSectionElement = {};
      var aRelationshipContentElementList = [];

      var oElements = oSection.elements;
      _this.removeElementsHavingNoReadPermission(oElements);
      if(!CS.isEmpty(oElements) || oSection.type != ClassNameDictionary.ENTITY_SECTION){

        var aElementsToRemove = [];
        var sSectionLabel = CS.getLabelOrCode(oSection);

        CS.forEach(oElements, function (oElement) {

          if (oElement.type.toLocaleLowerCase() == ConfigEntityTypeDictionary.ENTITY_TYPE_ATTRIBUTE) {
            _this.fillSectionAttributeWithModel(oElement, oActiveContent);
            _this.addMustShouldDataInRuleViolationObject(oElement);
            _this.addUniqueDataInRuleViolationObject(oElement);
          }
          else if (oElement.type.toLocaleLowerCase() == "relationship") {
            var oRelationship = oElement.relationship;
            oElement.tags = oActiveContent.tags;
            oRelationshipSectionElement = oElement;
            var oRelationshipSide = oElement.relationshipSide || {};
            let sKlassRelationshipId = oRelationshipSide.relationshipMappingId || "";
            aRelationshipContentElementList = _this.getRelationshipElementsThumbnailViewModel(oRelationshipToolbarProps,sKlassRelationshipId);
            let aSelectedRelationshipList = oSelectedRelationshipElements[sKlassRelationshipId] || [];
            oElement.activeElement = "article";
            oElement.defaultAssetInstanceId = oActiveContent.defaultAssetInstanceId;
            oElement.activeEntityId = oActiveContent.id;
            oElement.relationshipElement = oRelationshipSectionElement;
            oElement.relationshipContentElementList = aRelationshipContentElementList;
            oElement.selectedElementsOfRelationship = aSelectedRelationshipList;
            oElement.relationshipToolbarProps = oRelationshipToolbarProps[oElement.id];
            oElement.activeXRayPropertyGroup = ContentUtils.getActiveXRayPropertyGroup(oElement.id);

            var oXRaySettingsData = _this.getXRaySettingsData(oElement.id);
            oXRaySettingsData.relationshipId = oElement.id;

            var oRelationshipToolbar = CS.cloneDeep(ContentToolbarItemList.RelationshipToolbar);
            if(CS.isEmpty(aSelectedRelationshipList)){
              oRelationshipToolbar.leftItemList = CS.reject(oRelationshipToolbar.leftItemList, {id:  "delete_relationship_entities"});
            }
            else{
              var oDeleteOption = CS.find(oRelationshipToolbar.leftItemList, {id:  "delete_relationship_entities"});
              if(oDeleteOption) {
                oDeleteOption.className = "toolDelete ";
                if(CS.isEmpty(oElement.selectedElementsOfRelationship)) {
                  oDeleteOption.className += "hideVisibility ";
                }
              }
            }

            var oToolbarItem = CS.find(oRelationshipToolbar.leftItemList, {id: 'selectAll_relationship_entities'});
            if(aSelectedRelationshipList.length) {
              oToolbarItem && (oToolbarItem.label = "DESELECT_ALL") &&
              (oToolbarItem.className = aRelationshipContentElementList.length === aSelectedRelationshipList.length ? "toolUnCheckAll " : "toolHalfChecked");
            } else {
              oToolbarItem && (oToolbarItem.label = "SELECT_ALL") && (oToolbarItem.className = "toolCheckAll");
            }
            oToolbarItem.selectedCount = aSelectedRelationshipList.length;
            var sThumbnailMode = ThumbnailModeConstants.BASIC;
            if (oRelationshipToolbarProps[oElement.id].isXRayEnabled && oRelationshipViewMode[oElement.id] !== ContentScreenConstants.viewModes.LIST_MODE) {
              sThumbnailMode = ThumbnailModeConstants.XRAY;
              oRelationshipToolbar.rightItemList = CS.reject(oRelationshipToolbar.rightItemList, {id: 'zoomin_relationship_entities'});
              oRelationshipToolbar.rightItemList = CS.reject(oRelationshipToolbar.rightItemList, {id: 'zoomout_relationship_entities'});
            }
            var oRelationshipVariantXRaybutton = CS.find(oRelationshipToolbar.rightItemList, {id: "view_relationship_variants"});
            if (!CS.isEmpty(oRelationshipVariantXRaybutton)) {
              let aGroupsData = _this.getStore().getXRayProperties();
              oRelationshipVariantXRaybutton.view = (
                  <XRayButtonView key={oRelationshipVariantXRaybutton.id} thumbnailMode={sThumbnailMode}
                                  viewMode={ViewUtils.getViewMode()} xRaySettingsData={oXRaySettingsData} groupData={aGroupsData}/>);
            }

            if (!oElement.isDisabled) { //proceed only if element is NOT disabled
              /** relationshipType will be present only in case of nature relationship */
              let bIsNR = !!oRelationship.relationshipType;
              var bIsNotVisible = !oRelationshipSide.isOppositeVisible;
              oElement.isDisabled = bIsNR || bIsNotVisible;
            }

            if(!oElement.canDelete || oElement.isDisabled || CS.isEmpty(aRelationshipContentElementList)){
              oRelationshipToolbar.leftItemList = CS.reject(oRelationshipToolbar.leftItemList, {id:  "selectAll_relationship_entities"});
              oRelationshipToolbar.leftItemList = CS.reject(oRelationshipToolbar.leftItemList, {id:  "delete_relationship_entities"});
            }

            if(oRelationshipViewMode[oElement.id] == ContentScreenConstants.viewModes.LIST_MODE){
              oRelationshipToolbar.rightItemList = CS.reject(oRelationshipToolbar.rightItemList, {id:  "view_relationship_variants"});
            }

            oElement.relationshipToolbar = oRelationshipToolbar;
            if(bRelationshipSelectionStatus){
              var sRectangleSelectionObjectId = oSelectionObject["selectedRelationship"].id;
              if(oElement.id == sRectangleSelectionObjectId){
                oElement.isSelected = true;
              }else{
                oElement.isSelected = false;
              }
            }
          }
          else if (oElement.type.toLocaleLowerCase() == ConfigEntityTypeDictionary.ENTITY_TYPE_TAG) {
            _this.fillSectionTagWithModel(oElement, aElementsToRemove, oActiveContent, 'content');
            _this.addMustShouldDataInRuleViolationObject(oElement);
          }
          else if (oElement.type.toLocaleLowerCase() == ConfigEntityTypeDictionary.ENTITY_TYPE_TAXONOMY) {
            _this.fillSectionTaxonomyWithModel(oElement, oActiveContent);
          }
        });

        //Temporary fix need to discuss: when we delete attribute, attribute not get deleted from Class Section
        CS.forEach(aElementsToRemove, function (oElement) {
          delete oElements[oElement.id];
        });

          let sSectionContext = oSection.isContextSection ? "context" : "content";
          var oProperties = {};
          oProperties.selectedRoleId = '';
          oProperties.dragDetails = {};
          oProperties.sectionContext = sSectionContext;
          oProperties.sectionsToUpdate = aSectionsToUpdate;
          oProperties.sectionSCU = "section";
          oProperties.type = oSection.type;
          oProperties.fromRelationshipTab = oSection.fromRelationshipTab;
          if (oSection.isContextSection) {
            let bIsSectionExpanded = oSectionVisualProps[oSection.id] && oSectionVisualProps[oSection.id].isExpanded;
            oProperties.uomData = _this.getUOMData(oSection.id, CS.getLabelOrCode(oSection), bIsSectionExpanded, null);
          }
          var sIcon = oSection.icon ? ContentUtils.getIconUrl(oSection.icon) : "";

          let oSectionData = {};
          oSectionData.id = oSection.id;
          oSectionData.type = oSection.type || "";
          oSectionData.rows = oSection.rows;
          oSectionData.label = sSectionLabel;
          oSectionData.columns = oSection.columns;
          oSectionData.icon = sIcon;
          oSectionData.elements = oElements;
          oSectionData.properties = oProperties;
          aSectionModel.push(oSectionData);
        }
      });
    }

  return aSectionModel;
};

  removePropertiesAndSectionsAccordingToSelectedFilterOption = (oElements) => {
    let sSelectedFilterOption = ContentUtils.getActiveEntitySelectedFilterId();
    let sProperty = "";
    let bValue = false;
    let _this = this;
    switch (sSelectedFilterOption) {
      case ContentEditFilterItemsDictionary.ALL:
        return;
      case ContentEditFilterItemsDictionary.COMPLETE:
        sProperty = "isComplete";
        bValue = true;
        break;
      case ContentEditFilterItemsDictionary.INCOMPLETE:
        sProperty = "isComplete";
        bValue = false;
        break;
      case ContentEditFilterItemsDictionary.VIOLATED:
        sProperty = "isViolated";
        bValue = true;
        break;
      case ContentEditFilterItemsDictionary.MANDATORY:
        sProperty = "isMandatory";
        bValue = true;
        break;
      case ContentEditFilterItemsDictionary.DEPENDENT:
        sProperty = "isDependent";
        bValue = true;
        break;
    }

    let aIdsToRemove = [];

    CS.forEach(oElements, function (oElement, sKey) {
      if (oElement[sProperty] !== bValue || !CS.isEmpty(oElement.attributeVariantContext)) {
        aIdsToRemove.push(sKey);
      }
      if(!CS.isEmpty(oElement.elements)) {
        _this.removePropertiesAndSectionsAccordingToSelectedFilterOption(oElement.elements)
      }
    });

    CS.forEach(aIdsToRemove, function (sId) {
      delete oElements[sId];
    });
  };

  getSectionViewData = (sContext, aActiveSections, oActiveContent) => {

    if (CS.isEmpty(oActiveContent) || CS.isEmpty(aActiveSections)) {
      return [];
    }

    var oComponentProps = this.getComponentProps();
    var oContentSectionViewProps = oComponentProps.contentSectionViewProps;
    var aSectionsToUpdate = oContentSectionViewProps.getSectionsToUpdate();

    var aSectionData = this.getSectionsWithElementsMap(aActiveSections);

    var oContextToUpdateForSCU = oComponentProps.screen.getSelectedContext();

    var _this = this;
    var aSectionModel = [];
    var oSectionVisualProps = oComponentProps.contentSectionViewProps.getSectionVisualProps();
    let oElementsHavingConflictingValue = oComponentProps.screen.getElementsHavingConflictingValues();
    let oViolatingElement = {};
    let aBreadCrumbData = ContentScreenProps.breadCrumbProps.getBreadCrumbData();
    let oActiveBreadcrumbNode = aBreadCrumbData[aBreadCrumbData.length - 1];
    let bIsQuickList = ContentUtils.isQuickListNode(oActiveBreadcrumbNode);

    CS.forEach(aSectionData, (oSection) => {

      let bIsRelationshipSection = oSection.type === ContentScreenConstants.sectionTypes.SECTION_TYPE_NATURE_RELATIONSHIP
          || oSection.type === ContentScreenConstants.sectionTypes.SECTION_TYPE_RELATIONSHIP;
      let bIsContextSection = oSection.type === ContentScreenConstants.sectionTypes.SECTION_TYPE_CONTEXT || oSection.type === ContentScreenConstants.sectionTypes.SECTION_TYPE_CONTEXT_SELECTION;
      let sSelectedFilterOption = ContentUtils.getActiveEntitySelectedFilterId();
      let sSelectedTabId = ContentUtils.getSelectedTabId();
      if (!bIsContextSection && bIsRelationshipSection && sSelectedTabId !== ContentScreenConstants.tabItems.TAB_OVERVIEW && !bIsQuickList) {
        if ((sSelectedFilterOption === ContentEditFilterItemsDictionary.VIOLATED) ||
            (sSelectedFilterOption === ContentEditFilterItemsDictionary.COMPLETE && oSection.isEmpty) ||
            (sSelectedFilterOption === ContentEditFilterItemsDictionary.INCOMPLETE && !oSection.isEmpty)) {
          return;
        }
      }
      var oSectionData = {};
      if (oSectionVisualProps[oSection.id] && oSectionVisualProps[oSection.id].isHidden) {
        return;
      }

      let oElements = CS.cloneDeep(oSection.elements);
      _this.removeElementsHavingNoReadPermission(oElements);

      if (!bIsContextSection && !bIsRelationshipSection && sSelectedTabId !== ContentScreenConstants.tabItems.TAB_OVERVIEW) {
        _this.removePropertiesAndSectionsAccordingToSelectedFilterOption(oElements);
        if (sSelectedFilterOption !== ContentEditFilterItemsDictionary.ALL && CS.isEmpty(oElements)) {
          return;
        }
      }

      if (!CS.isEmpty(oElements) || oSection.type != ClassNameDictionary.ENTITY_SECTION) {
          aSectionsToUpdate = _this.processSectionElementsData(oElements, oActiveContent, oViolatingElement, oElementsHavingConflictingValue, aSectionsToUpdate);


        let sSectionContext = oSection.isContextSection ? "context" : "content";
        let sNatureType = oSection.natureType || "";
        var oProperties = {};
        oProperties.selectedRoleId = '';
        oProperties.dragDetails = {};
        oProperties.sectionContext = sSectionContext;
        oProperties.sectionsToUpdate = aSectionsToUpdate;
        oProperties.contextToUpdateForSCU = oContextToUpdateForSCU;
        oProperties.sectionSCU = "section";
        oProperties.type = oSection.type;
        oProperties.fromRelationshipTab = oSection.fromRelationshipTab;
        oProperties.natureType = sNatureType;
        oProperties.viewContext = oSection.viewContext;
        oProperties.canBeEmpty = oSection.canBeEmpty;
        oProperties.viewModel = oSection.viewModel;
        oProperties.hideSectionLabel = oSection.hideSectionLabel;

        /**For upload asset from relationship*/
        if (oElements[oSection.id] && oElements[oSection.id].showBulkAssetUploadInRelationship) {
          let oRelationshipViewProps = ContentScreenProps.relationshipView;
          let sContext = oRelationshipViewProps.getContext();
          let bShowBulkUploadDialog = oRelationshipViewProps.getIsBulkUploadDialogOpen() && sContext === AssetUploadContextDictionary.RELATIONSHIP_SECTION_DROP;
          oProperties.showBulkUploadDialog = bShowBulkUploadDialog;
          oProperties.assetClassList = oRelationshipViewProps.getAssetClassList();
        }

        if (oSection.isContextSection) {
          let bIsSectionExpanded = oSectionVisualProps[oSection.id] && oSectionVisualProps[oSection.id].isExpanded;
          let oReferencedContext = oSection.referencedContext || {};
          oProperties.uomData = _this.getUOMData(oSection.id, CS.getLabelOrCode(oSection), bIsSectionExpanded, null);
        }

        var sIconKey = oSection.iconKey ? oSection.iconKey : "";

        oSectionData.id = oSection.id;
        oSectionData.type = oSection.type || "";
        oSectionData.rows = oSection.rows;
        oSectionData.label = oSection.label;
        oSectionData.code = oSection.code;
        oSectionData.columns = oSection.columns;
        oSectionData.iconKey = sIconKey;
        oSectionData.elements = oElements;
        oSectionData.properties = oProperties;
        oSectionData.relationshipType = oSection.relationshipType;
        oSectionData.viewModel = oSection.viewModel;
        oSectionData.relationshipId = oSection.relationshipId;
        oSectionData.selectedContext = oSection.selectedContext;
        oSectionData.contextModelList = oSection.contextModelList;
        oSectionData.showDisconnected = oSection.showDisconnected;
        oSectionData.isDisabled = false;
        aSectionModel.push(oSectionData);
      }
    });

    return aSectionModel;
  };

  processSectionElementsData (oElements, oActiveContent, oViolatingElement, oElementsHavingConflictingValue, aSectionsToUpdate) {
    var aElementsToRemove = [];
    let _this = this;
    var oComponentProps = this.getComponentProps();
    var oRelationshipViewProps = oComponentProps.relationshipView;
    var oRelationshipToolbarProps = oRelationshipViewProps.getRelationshipToolbarProps();
    var oSelectedRelationshipElements = oRelationshipViewProps.getSelectedRelationshipElements();
    var oSelectionObject = oComponentProps.screen.getSetSectionSelectionStatus();
    var bRelationshipSelectionStatus = oSelectionObject["relationshipContainerSelectionStatus"];
    var oContextToUpdateForSCU = oComponentProps.screen.getSelectedContext();

    CS.forEach(oElements, function (oElement) {

      /** -------------------------------------------  SCU Handling -------------------------------------------------*/
      if (oContextToUpdateForSCU.elementId === oElement.id) {
        aSectionsToUpdate = oElement.sections;
      }

      /** -------------------------------------------  ATTRIBUTE -------------------------------------------------*/
      if (oElement.type.toLocaleLowerCase() == "attribute") {
        _this.fillSectionAttributeWithModel(oElement, oActiveContent);
        _this.addMustShouldDataInRuleViolationObject(oElement);
        _this.addUniqueDataInRuleViolationObject(oElement);

        oViolatingElement = oElementsHavingConflictingValue[oElement.id];
        if (!CS.isEmpty(oViolatingElement)) {
          oElement.violationExpanded = oViolatingElement.isExpanded;
        }
      }
      /** -------------------------------------------  RELATIONSHIP  ---------------------------------------------*/
      else if (oElement.type.toLocaleLowerCase() == "relationship" ) {
        //todo: CT, separate function if possible
        let oAddEntityInRelationship = oComponentProps.screen.getAddEntityInRelationshipScreenData();
        var oRelationship = oElement.relationship;
        oElement.tags = oActiveContent.tags;
        let oRelationshipSectionElement = oElement;
        var oRelationshipSide = oElement.relationshipSide || {};
        let sKlassRelationshipId = oRelationshipSide.relationshipMappingId || "";
        let aRelationshipContentElementList = _this.getRelationshipElementsThumbnailViewModel(oRelationshipToolbarProps, sKlassRelationshipId);
        let sRelationshipType = oRelationship.relationshipType;
        let bIsNatureRelationship = ContentUtils.isNatureRelationship(sRelationshipType) ||
            ContentUtils.isVariantRelationship(sRelationshipType);
        let aSelectedRelationshipList = !bIsNatureRelationship ? oSelectedRelationshipElements[sKlassRelationshipId] || [] : oSelectedRelationshipElements[sKlassRelationshipId] || [];
        oElement.activeElement = "article";
        oElement.defaultAssetInstanceId = oActiveContent.defaultAssetInstanceId;
        oElement.activeEntityId = oActiveContent.id;
        oElement.relationshipElement = oRelationshipSectionElement;
        oElement.relationshipContentElementList = aRelationshipContentElementList;
        oElement.selectedElementsOfRelationship = aSelectedRelationshipList;
        oElement.relationshipToolbarProps = oRelationshipToolbarProps[oElement.id];
        oElement.activeXRayPropertyGroup = ContentUtils.getActiveXRayPropertyGroup(oElement.id);
        /*----------------For Maintain dragging state in props----------------*/
        oElement.isDragging = oRelationshipViewProps.getIsDragging();
        oElement.canEditRelationshipContext = _this.getCanEditRelationshipContext(oRelationship.id);

        let oMasterRelationship = oComponentProps.screen.getReferencedRelationships()[oElement.propertyId]
        oMasterRelationship && (oElement.contentRelationships = CS.filter(oActiveContent.contentRelationships, {relationshipId: oMasterRelationship.id}));


        if (!oElement.isDisabled) { //proceed only if element is NOT disabled
          /** relationshipType will be present only in case of nature relationship */
          /**Check proper editable scenario in case of nature relationship **/
          var bIsNotVisible = !oElement.relationshipSide.isOppositeVisible;
          oElement.isDisabled = /*bIsNatureRelationship || */bIsNotVisible;
        }
        var oFilterContext = {filterType: oFilterPropType.MODULE, screenContext: oElement.id};
        oElement.relationshipToolbar = _this.getRelationshipSectionToolbarData(oElement, aRelationshipContentElementList, aSelectedRelationshipList, oFilterContext);

        if (bRelationshipSelectionStatus) {
          var sRectangleSelectionObjectId = oSelectionObject["selectedRelationship"].id;
          if (oElement.id == sRectangleSelectionObjectId) {
            oElement.isSelected = true;
          } else {
            oElement.isSelected = false;
          }
        }
        oViolatingElement = oElementsHavingConflictingValue[oElement.id];
        if (!CS.isEmpty(oViolatingElement)) {
          oElement.violationExpanded = oViolatingElement.isExpanded;
        }
      }
      /** ----------------------------------------------  TAG  ---------------------------------------------------*/
      else if (oElement.type.toLocaleLowerCase() == "tag") {
        _this.fillSectionTagWithModel(oElement, aElementsToRemove, oActiveContent, 'content');
        _this.addMustShouldDataInRuleViolationObject(oElement);
        oElement.model.customRequestObject = {
          klassInstanceId: oActiveContent.id,
          baseType: oActiveContent.baseType
        };

        oViolatingElement = oElementsHavingConflictingValue[oElement.id];
        if (!CS.isEmpty(oViolatingElement)) {
          oElement.violationExpanded = oViolatingElement.isExpanded;
        }
      }

      /** --------------------------------------------  TAXONOMY  ------------------------------------------------*/
      else if (oElement.type.toLocaleLowerCase() == "taxonomy") {
        _this.fillSectionTaxonomyWithModel(oElement, oActiveContent);
        if (oElement.elements) {
          let aSectionData = _this.getSectionsWithElementsMap([oElement]);
          CS.forEach(aSectionData, function (oSection) {
            oElement.sections = _this.processSectionElementsData(oElement.elements, oActiveContent, oViolatingElement, oElementsHavingConflictingValue, aSectionsToUpdate)
          });
        }

      }

      else if (oElement.type.toLowerCase() == "filler") {
        oElement.sections =  _this.processSectionElementsData(oElement.elements, oActiveContent, oViolatingElement, oElementsHavingConflictingValue, aSectionsToUpdate)
      }
    });

    //Temporary fix need to discuss: when we delete attribute, attribute not get deleted from Class Section
    CS.forEach(aElementsToRemove, function (oElement) {
      delete oElements[oElement.id];
    });
    return aSectionsToUpdate;
  }

  getHierarchySectionModels = (aSections, sHierarchyContext, oFilterContext) => {
    var _this = this;
    var aSectionModel = [];

    CS.forEach(aSections, function (oSection) {
      var aElements = oSection.elements;
      CS.remove(aElements, function (oElement) {
        return (oElement.type.toLocaleLowerCase() === "relationship" || oElement.type.toLocaleLowerCase() === "role");
      })
    });

    var aSectionData = this.getSectionsWithElementsMap(aSections);
    CS.forEach(aSectionData, function (oSection) {
      var sIcon = oSection.icon ? oSection.icon : "";
      var aElementsToRemove = [];
      var oActiveContent = {};
      var oElements = oSection.elements;
      CS.forEach(oElements, function (oElement, sKey) {
        let sAttributeType = oElement.attribute && oElement.attribute.type || "";
        if(oElement.attribute && (ContentUtils.isAttributeTypeCoverflow(sAttributeType) || oElement.attributeVariantContext != null)){
          delete oElements[sKey]; /** Hide coverflow attribute and attribute context elements in Hierarchy*/
          return;
        }
        if (oElement.type.toLocaleLowerCase() == "attribute") {
          _this.fillSectionAttributeWithModel(oElement, oActiveContent);
          if (oElement.attribute && (ViewUtils.isAttributeTypeConcatenated(sAttributeType)
              || ViewUtils.isAttributeTypeCalculated(sAttributeType))) {
            oElement.isDisabled = true;
            if (oElement.hasOwnProperty('expressionList')) {
              oElement.expressionList = [];
            }
          }
          var oClonedElement = CS.cloneDeep(oElement);
          delete oClonedElement.contentAttributes;

          if (ViewUtils.isAttributeTypeHtml(sAttributeType)) {
            /** swap value & valueAsHtml here because froala will read required value in value (Taxonomy hierarchies - Properties tab)**/
            [oClonedElement.value, oClonedElement.valueAsHtml] = [oClonedElement.valueAsHtml, oClonedElement.defaultValue];
          }
          else {
            oClonedElement.value = oClonedElement.defaultValue;
          }
          oElement.contentAttributes = [oClonedElement];

        }
        else if (oElement.type.toLocaleLowerCase() == "relationship") {
          /** *Do nothing. Relationship is not allowed*/
        }
        else if (oElement.type.toLocaleLowerCase() == "tag") {
          if(oElement.tagType == TagTypeConstants.LIFECYCLE_STATUS_TAG_TYPE ||
              oElement.tagType == TagTypeConstants.STATUS_TAG_TYPE){
            /** Do not show lifeCycle and status tag types*/
            return;
          }
          var sSectionId = oSection.id;
          var oHierarchyData = {
            hierarchyContext: sHierarchyContext,
            elementId: oElement.id,
            sectionId: sSectionId
          };
          _this.fillSectionTagWithModel(oElement, aElementsToRemove, oActiveContent, 'hierarchy', oHierarchyData, oFilterContext);
          if(sHierarchyContext === HierarchyTypesDictionary.TAXONOMY_HIERARCHY){
            let oCustomRequestObject = {};
              oCustomRequestObject.elementId = oElement.id;
            oElement.model.customRequestObject = oCustomRequestObject;
          }
          oClonedElement = CS.cloneDeep(oElement);
          delete oClonedElement.contentTags;
          oClonedElement.tagValues = oClonedElement.defaultValue;
          oElement.contentTags = [oClonedElement];
        }
      });

      var oProperties = {};
      oProperties.sectionContext = sHierarchyContext;
      oProperties.isInherited = oSection.isInherited;
      oProperties.isSkipped = oSection.isSkipped;
      oProperties.isCollapsed = oSection.isCollapsedUI;

      aSectionModel.push({
          id: oSection.id,
          label: CS.getLabelOrCode(oSection),
          rows: oSection.rows,
          columns: oSection.columns,
          icon: sIcon,
          elements: oElements,
          properties: oProperties
          });
    });

    return aSectionModel;
  };

  getRelationshipElementsThumbnailViewModel = (oActiveContentRelationships, sKlassRelationshipId) => {
    var oCurrentRelationship = oActiveContentRelationships[sKlassRelationshipId];
    if(!CS.isEmpty(oCurrentRelationship)){
      return oCurrentRelationship.elements;
    }
    return [];
  };

  getCanEditRelationshipContext = (sRelationshipId) => {
    let oReferencedPermissions = this.getComponentProps().screen.getReferencedPermissions();
    let aCanEditContextRelationshipIds = oReferencedPermissions["canEditContextRelationshipIds"];
    return CS.includes(aCanEditContextRelationshipIds, sRelationshipId);
  };

  getSectionsWithElementsMap = (aSections) => {
    var aClonedSections = CS.cloneDeep(aSections);
    let _this = this;

    CS.forEach(aClonedSections, function (oSection) {
      var oElements = {};
      CS.forEach(oSection.elements, function (oElement) {
        if(!CS.isEmpty(oElement.elements)) {
          let oNestedElements = _this.getSectionsWithElementsMap([oElement]);
          oElement = oNestedElements[0];
        }
        oElements[oElement.id] = oElement;
      });
      oSection.elements = oElements;
    });

    return aClonedSections;
  };

  getTabSpecificContentTileListView = (sSelectedTabId) => {
    let sViewMode = ContentUtils.getViewMode();
    let oAppData = this.getAppData();
    let oComponentProps = ContentUtils.getComponentProps();
    let oScreenProps = oComponentProps.screen;
    let oScreenViewsMasterData = this.getScreenViewsMasterData();
    let oContentThumbContainerViewMasterData = oScreenViewsMasterData.contentThumbContainerViewMasterData;

    /**Filter and Navigation Handling**/
    let oFilterContext = {
      filterType: oFilterPropType.PAGINATION,
      screenContext: sSelectedTabId
    };
    let oFilterViewData = {filterContext: oFilterContext, isFilterAndSearchViewDisabled: true};
    let oNavigationData = this.getNavigationData(oFilterContext);

    let oToolbarData = this.getCustomToolbarData(this.getToolbarData(false, oFilterContext.screenContext, oFilterContext));
    let iCurrentZoom = oScreenProps.getSectionInnerZoom();
    let sThumbnailMode = oScreenProps.getSectionInnerThumbnailMode();
    let bHideAddButton = (sSelectedTabId === ContentScreenConstants.tabItems.TAB_DUPLICATE_ASSETS);

    /**Entity View Data Handling**/
    let oEntityViewData = {
      sortByViewData: this.getDefaultSortData(),
      viewMode: oScreenProps.getViewMode(),
      isDragNotAllowed: true,
      activeEntity: this.getActiveEntity(),
      entityList: oAppData.getAvailableEntities(),
      verticalMenu: bHideAddButton ? {isHideCreateButton: true} : [],
    };

    let oCustomView = (
        <ContentTileListView
            dragDropContext={sSelectedTabId}
            entityViewData={oEntityViewData}
            currentZoom={iCurrentZoom}
            filterViewData={oFilterViewData}
            screenMode={ContentUtils.getContentScreenMode()}
            viewMode={sViewMode}
            isHierarchyMode={this.isHierarchyModeSelected()}
            toolbarData={oToolbarData}
            navigationData={oNavigationData}
            thumbnailMode={sThumbnailMode}
            collectionData={{}} //Todo: need to be removed if not required (not handled empty check inside view)
            viewMasterData={oContentThumbContainerViewMasterData.contentTileListViewMasterData}
        />
    );

    return {
      customViewForTab: oCustomView
    };
  };

  getAvailableEntityViewForTaxonomyHierarchy = (sContext, bHideAddButton, bIsInnerHierarchyViewMode, oFilterContext) => {
    var oComponentProps = ContentUtils.getComponentProps();
    var oScreenProps = oComponentProps.screen;
    var sViewMode = ContentUtils.getViewMode();

    var aThumbContextMenuList = [];
    /** To stop drag functionality in case of Class Taxonomy Hierarchy*/
    var bIsDragNotAllowed = false;
    if(sContext == HierarchyTypesDictionary.TAXONOMY_HIERARCHY){
      var sOuterParentTaxonomyId = oScreenProps.getSelectedOuterParentContentHierarchyTaxonomyId();
      if(sOuterParentTaxonomyId == "-1" || sOuterParentTaxonomyId == ContentUtils.getHierarchyDummyNodeId()){
        bIsDragNotAllowed = true;
      }
      aThumbContextMenuList = ContentHierarchyContextMenuList.TaxonomyThumbnailContextMenuList;
    }else if(sContext == HierarchyTypesDictionary.COLLECTION_HIERARCHY){
      aThumbContextMenuList = ContentHierarchyContextMenuList.CollectionThumbnailContextMenuList;
    }

    var oScreenViewsMasterData = this.getScreenViewsMasterData();
    var oContentThumbContainerViewMasterData = oScreenViewsMasterData.contentThumbContainerViewMasterData;

    let {selectedEntityList: aSelectedEntityList, entityList: aEntityList} = this.getEntityListAndSelectedEntityList(sContext);

    let bIsProductVariantQuickList = sContext === "productVariantQuickList";

    var oEntityViewData = {
      sortByViewData: this.getDefaultSortData(),
      viewMode: oComponentProps.screen.getViewMode(),
      isDragNotAllowed: bIsDragNotAllowed,
      contextMenuList: aThumbContextMenuList,
      verticalMenu: bHideAddButton ? [] : (sContext == "relationshipEntity" ? this.getVerticalMenu() : {list: []}),
      side2NatureClassOfProductVariant: bIsProductVariantQuickList ? oComponentProps.variantSectionViewProps.getSide2NatureClassOfProductVariant() : null,
      userList: this.getAppData().getUserList(),
      activeEntity: this.getActiveEntity(),
      selectedEntityList: aSelectedEntityList,
      entityList: aEntityList,
      selectedRelationshipElements: oComponentProps.relationshipView.getSelectedRelationshipElements(),
  };

    if(sContext == HierarchyTypesDictionary.COLLECTION_HIERARCHY || sContext == HierarchyTypesDictionary.TAXONOMY_HIERARCHY){
      var oSelectedCutEntitiesData = oScreenProps.getHierarchyEntitiesToCopyOrCutData();
      var sActiveHierarchyNodeId = oScreenProps.getActiveHierarchyNodeId();
      if(oSelectedCutEntitiesData.action == "cut" && sActiveHierarchyNodeId == oSelectedCutEntitiesData.hierarchyNodeId){
        oEntityViewData.cutEntityList = oSelectedCutEntitiesData.entityList;
      }
      var oClickedThumbModel = oScreenProps.getRightClickedThumbnailModel();
      oEntityViewData.rightClickedThumbnailId = !CS.isEmpty(oClickedThumbModel) ? oClickedThumbModel.id : "";
    }

    var oCollectionData = {};
    let oFilterViewData, oNavigationData = {};
    let oFilterView = null;
    let bIsDuplicateAssetsTab = ContentUtils.getSelectedTabId() === TemplateTabsDictionary.DUPLICATE_TAB;

    if(bIsDuplicateAssetsTab) {
      oFilterViewData = {filterContext: oFilterContext, isFilterAndSearchViewDisabled: true};
      oNavigationData = this.getNavigationData(oFilterContext);
    } else {
      oFilterViewData = this.getFilterViewData(oFilterContext);
      oFilterView = (CS.isNotEmpty(ContentUtils.getSelectedHierarchyContext())) ? null : this.getFilterView(oFilterViewData);
      oNavigationData = this.getNavigationData(oFilterContext);
    }

    var oToolbarData = this.getToolbarData(bIsInnerHierarchyViewMode, sContext, oFilterContext);
    oToolbarData = this.getCustomToolbarData(oToolbarData);
    var iCurrentZoom = oComponentProps.screen.getSectionInnerZoom();
    var sThumbnailMode = oComponentProps.screen.getSectionInnerThumbnailMode();
    var oActiveXRayPropertyGroup = ContentUtils.getActiveXRayPropertyGroup();
    if(!CS.isEmpty(oToolbarData.leftItemList) && ContentUtils.getAvailableEntityViewStatus()) {
      oToolbarData.leftItemList = CS.reject(oToolbarData.leftItemList, {id: 'assetBulkUpload'});
    }

    /** For Relationship Quick list ***/
    var oToolbarItem = CS.find(oToolbarData.leftItemList, {id: 'selectAll'});
    if (aSelectedEntityList.length && aEntityList.length == aSelectedEntityList.length) {
      oToolbarItem && (oToolbarItem.label = "DESELECT_ALL");
    } else {
      oToolbarItem && (oToolbarItem.label = "SELECT_ALL");
    }
    var oXRayData = this.getXRayData();

    let aBreadcrumbPath = oComponentProps.breadCrumbProps.getBreadCrumbData();

    return (<ContentTileListView
        filterView={oFilterView}
        entityViewData={oEntityViewData}
        currentZoom={iCurrentZoom}
        filterViewData={oFilterViewData}
        screenMode={ContentUtils.getContentScreenMode()}
        viewMode={sViewMode}
        innerHierarchyViewMode={bIsInnerHierarchyViewMode}
        isHierarchyMode={this.isHierarchyModeSelected()}
        toolbarData={oToolbarData}
        navigationData={oNavigationData}
        isForAvailableEntity={true}
        dragDropContext={sContext}
        thumbnailMode={sThumbnailMode}
        xRayData={oXRayData}
        collectionData = {oCollectionData}
        activeXRayPropertyGroup = {oActiveXRayPropertyGroup}
        viewMasterData={oContentThumbContainerViewMasterData.contentTileListViewMasterData}
        breadcrumbPath={aBreadcrumbPath}
        hideAddEntityButton={bHideAddButton || bIsProductVariantQuickList}
    />);
  };

  getAvailableEntityData = (sContext, bHideAddButton, bIsInnerHierarchyViewMode, oFilterContext, oOverridingViewProps, oActiveRelationship, oExtraData) => {
    let oComponentProps = ContentUtils.getComponentProps();
    let sViewMode = ContentUtils.getViewMode();
    let aThumbContextMenuList = [];
    let oScreenViewsMasterData = this.getScreenViewsMasterData();
    let oContentThumbContainerViewMasterData = oScreenViewsMasterData.contentThumbContainerViewMasterData;
    let {selectedEntityList: aSelectedEntityList, entityList: aEntityList} = this.getEntityListAndSelectedEntityList(sContext);
    let bIsProductVariantQuickList = sContext === "productVariantQuickList";

    let oEntityViewData = {
      sortByViewData: this.getDefaultSortData(),
      viewMode: oComponentProps.screen.getViewMode(),
      isDragNotAllowed: false,
      contextMenuList: aThumbContextMenuList,
      verticalMenu: bHideAddButton ? {isHideCreateButton: true} : (sContext == "relationshipEntity" ? this.getVerticalMenu() : {list: []}),
      side2NatureClassOfProductVariant: bIsProductVariantQuickList ? oComponentProps.variantSectionViewProps.getSide2NatureClassOfProductVariant() : null,
      userList: this.getAppData().getUserList(),
      activeEntity: this.getActiveEntity(),
      selectedEntityList: aSelectedEntityList,
      entityList: aEntityList,
      selectedRelationshipElements: oComponentProps.relationshipView.getSelectedRelationshipElements(),
      referencedAssets: oComponentProps.screen.getReferencedAssetList()
  };

    let oCollectionData = {};
    let oFilterViewData, oNavigationData = {};
    let oFilterView = null;
    let bIsDuplicateAssetsTab = ContentUtils.getSelectedTabId() === TemplateTabsDictionary.DUPLICATE_TAB;

    if(bIsDuplicateAssetsTab) {
      oFilterViewData = {filterContext: oFilterContext, isFilterAndSearchViewDisabled: true};
      oNavigationData = this.getNavigationData(oFilterContext);
    } else {
      oFilterViewData = this.getFilterViewData(oFilterContext);
      oFilterView = (CS.isNotEmpty(ContentUtils.getSelectedHierarchyContext())) ? null : this.getFilterView(oFilterViewData, oOverridingViewProps, oExtraData);
      oNavigationData = this.getNavigationData(oFilterContext);
    }

    let oToolbarData = this.getToolbarData(bIsInnerHierarchyViewMode, sContext, oFilterContext, oActiveRelationship);
    oToolbarData = this.getCustomToolbarData(oToolbarData);
    let iCurrentZoom = oComponentProps.screen.getSectionInnerZoom();
    let sThumbnailMode = oComponentProps.screen.getSectionInnerThumbnailMode();
    let oActiveXRayPropertyGroup = ContentUtils.getActiveXRayPropertyGroup();
    if(!CS.isEmpty(oToolbarData.leftItemList) && ContentUtils.getAvailableEntityViewStatus()) {
      oToolbarData.leftItemList = CS.reject(oToolbarData.leftItemList, {id: 'assetBulkUpload'});
    }

    /** For Relationship Quick list ***/
    let oToolbarItem = CS.find(oToolbarData.leftItemList, {id: 'selectAll'});
    if (aSelectedEntityList.length && aEntityList.length == aSelectedEntityList.length) {
      oToolbarItem && (oToolbarItem.label = "DESELECT_ALL");
    } else {
      oToolbarItem && (oToolbarItem.label = "SELECT_ALL");
    }
    let oXRayData = this.getXRayData();

    let aBreadcrumbPath = oComponentProps.breadCrumbProps.getBreadCrumbData();

    return ({
      filterView: oFilterView,
      entityViewData: oEntityViewData,
      currentZoom: iCurrentZoom,
      filterViewData: oFilterViewData,
      screenMode: ContentUtils.getContentScreenMode(),
      viewMode: sViewMode,
      innerHierarchyViewMode: bIsInnerHierarchyViewMode,
      isHierarchyMode: this.isHierarchyModeSelected(),
      toolbarData: oToolbarData,
      navigationData: oNavigationData,
      isForAvailableEntity: true,
      dragDropContext: sContext,
      thumbnailMode: sThumbnailMode,
      xRayData: oXRayData,
      collectionData: oCollectionData,
      activeXRayPropertyGroup: oActiveXRayPropertyGroup,
      viewMasterData: oContentThumbContainerViewMasterData.contentTileListViewMasterData,
      breadcrumbPath: aBreadcrumbPath,
      hideAddEntityButton: bHideAddButton || bIsProductVariantQuickList
    });
  };

  getCollectionHierarchyRightsideViewSpecificData = (sContext, bHideAddButton, bIsInnerHierarchyViewMode, oFilterContext) => {
    let oComponentProps = ContentUtils.getComponentProps();
    let oScreenProps = oComponentProps.screen;

    /** To stop drag functionality in case of Class Taxonomy Hierarchy*/
    let bIsDragNotAllowed = false;
    let aThumbContextMenuList = ContentHierarchyContextMenuList.CollectionThumbnailContextMenuList || [];

    let oScreenViewsMasterData = this.getScreenViewsMasterData();
    let oContentThumbContainerViewMasterData = oScreenViewsMasterData.contentThumbContainerViewMasterData;

    let aHorizontalToolbarItemList = this.getEntityHorizontalToolbarItemList();
    let { selectedEntityList: aSelectedEntityList, entityList: aEntityList } = this.getEntityListAndSelectedEntityList(sContext);

    let oEntityViewData = {
      horizontalToolbarList: aHorizontalToolbarItemList,
      sortByViewData: this.getDefaultSortData(),
      viewMode: oComponentProps.screen.getViewMode(),
      isDragNotAllowed: bIsDragNotAllowed,
      contextMenuList: aThumbContextMenuList,
      verticalMenu: bHideAddButton ? [] : { list: [] },
      side2NatureClassOfProductVariant: null,
      userList: this.getAppData().getUserList(),
      activeEntity: this.getActiveEntity(),
      selectedEntityList: aSelectedEntityList,
      entityList: aEntityList,
      selectedRelationshipElements: oComponentProps.relationshipView.getSelectedRelationshipElements(),
      referencedAssets: oScreenProps.getReferencedAssetList(),
    };

    let oSelectedCutEntitiesData = oScreenProps.getHierarchyEntitiesToCopyOrCutData();
    let sActiveHierarchyNodeId = oScreenProps.getActiveHierarchyNodeId();
    if (oSelectedCutEntitiesData.action == "cut" && sActiveHierarchyNodeId == oSelectedCutEntitiesData.hierarchyNodeId) {
      oEntityViewData.cutEntityList = oSelectedCutEntitiesData.entityList;
    }
    let oClickedThumbModel = oScreenProps.getRightClickedThumbnailModel();
    oEntityViewData.rightClickedThumbnailId = !CS.isEmpty(oClickedThumbModel) ? oClickedThumbModel.id : "";

    let oCollectionData = {};
    let oFilterViewData, oNavigationData = {};
    let oFilterView = null;
    let bIsDuplicateAssetsTab = ContentUtils.getSelectedTabId() === TemplateTabsDictionary.DUPLICATE_TAB;

    if (bIsDuplicateAssetsTab) {
      oFilterViewData = { filterContext: oFilterContext, isFilterAndSearchViewDisabled: true };
      oNavigationData = this.getNavigationData(oFilterContext);
    } else {
      oFilterViewData = this.getFilterViewData(oFilterContext);
      oFilterView = (CS.isNotEmpty(ContentUtils.getSelectedHierarchyContext())) ? null : this.getFilterView(oFilterViewData);
      oNavigationData = this.getNavigationData(oFilterContext);
    }

    let oToolbarData = this.getToolbarData(bIsInnerHierarchyViewMode, sContext, oFilterContext);
    oToolbarData = this.getCustomToolbarData(oToolbarData);
    this.getSelectedViewTypeItem(oToolbarData.viewTypesItems, oEntityViewData.viewMode);
    let iCurrentZoom = oComponentProps.screen.getSectionInnerZoom();
    let sThumbnailMode = oComponentProps.screen.getSectionInnerThumbnailMode();
    let oActiveXRayPropertyGroup = ContentUtils.getActiveXRayPropertyGroup();
    if (!CS.isEmpty(oToolbarData.leftItemList) && ContentUtils.getAvailableEntityViewStatus()) {
      oToolbarData.leftItemList = CS.reject(oToolbarData.leftItemList, { id: 'assetBulkUpload' });
    }

    /** For Relationship Quick list ***/
    let oToolbarItem = CS.find(oToolbarData.leftItemList, { id: 'selectAll' });
    if (aSelectedEntityList.length && aEntityList.length == aSelectedEntityList.length) {
      oToolbarItem && (oToolbarItem.label = "DESELECT_ALL");
    } else {
      oToolbarItem && (oToolbarItem.label = "SELECT_ALL");
    }
    let oXRayData = this.getXRayData();

    let aBreadcrumbPath = oComponentProps.breadCrumbProps.getBreadCrumbData();

    return {
      filterView: oFilterView,
      entityViewData: oEntityViewData,
      currentZoom: iCurrentZoom,
      filterViewData: oFilterViewData,
      screenMode: ContentUtils.getContentScreenMode(),
      innerHierarchyViewMode: bIsInnerHierarchyViewMode,
      toolbarData: oToolbarData,
      navigationData: oNavigationData,
      isForAvailableEntity: true,
      dragDropContext: sContext,
      thumbnailMode: sThumbnailMode,
      xRayData: oXRayData,
      collectionData: oCollectionData,
      activeXRayPropertyGroup: oActiveXRayPropertyGroup,
      viewMasterData: oContentThumbContainerViewMasterData.contentTileListViewMasterData,
      breadcrumbPath: aBreadcrumbPath,
      hideAddEntityButton: bHideAddButton
    }
  }

  getImageSrcForThumbnail = (oContent) => {
    var aList = oContent.referencedAssets;
    if(oContent.baseType == EntityBaseTypeDictionary.assetBaseType) {
      aList = oContent.attributes;
    }

    var oImage = CS.find(aList, {isDefault: true});

    return ViewUtils.getImageSrcUrlFromImageObject(oImage);
  };

  removeIndicatorFromGroupList = (aGroupList) => {
    var aClonedGroupList = CS.cloneDeep(aGroupList);
      CS.remove(aClonedGroupList, {id: 'group4'});
    return aClonedGroupList;
  };

  getEntityHorizontalToolbarItemList = () => {
      return this.removeIndicatorFromGroupList(HorizontalToolbarItemList.ArticleTile);
  };

  isMajorTaxonomy = (oTaxonomy) => {
    switch (oTaxonomy.taxonomyType) {
        case "majorTaxonomy":
          return true;
        case "minorTaxonomy":
          return false;
        default:
          if (oTaxonomy.parent.id == -1) {
            return false;
          }
          return this.isMajorTaxonomy(oTaxonomy.parent);
    }
  };

  getAddedClassificationList = (oParent, aSelectedClassification) => {
    if(oParent.id == -1) {
      return;
    }
    !CS.isEmpty(oParent.parent) && this.getAddedClassificationList(oParent.parent, aSelectedClassification);
    aSelectedClassification.push(oParent);
  };

  getRemainingAllowedTaxonomyList = (aMainTaxonomyList) => {
    var oComponentProps =  this.getComponentProps();
    var oArticleViewProps = oComponentProps.articleViewProps;
    var aAllowedTypes = oArticleViewProps.getAllowedTypes();

    var aRemainingAllowedTaxonomyList = [];
    CS.forEach(aAllowedTypes, function (sAllowedId) {
      var oFoundNode = CS.find(aMainTaxonomyList, {id: sAllowedId});
      if(!CS.isEmpty(oFoundNode)){
        aRemainingAllowedTaxonomyList.push(oFoundNode);
      }
    });

    return aRemainingAllowedTaxonomyList;
  };

  getMultiTaxonomyList = (aSelectedTaxonomyIds) => {
    var _this = this;
    var oComponentProps =  this.getComponentProps();
    var oReferencedPermissions = oComponentProps.screen.getReferencedPermissions();
    var aTaxonomyIdsHavingRP = oReferencedPermissions.allTaxonomyIdsHavingRP || [];
    var aRemainingTaxonomyList = [];
    let aTaxonomyTree = oComponentProps.screen.getAllowedTaxonomies();
    CS.forEach(aTaxonomyTree, function (oTaxonomy){
      if(oTaxonomy.id != -1) {
        if(!CS.includes(aSelectedTaxonomyIds, oTaxonomy.id)) {
          aRemainingTaxonomyList.push(oTaxonomy);
        }
      }
    });
    var aSelectedTaxonomy = [];
    var oReferencedTaxonomy = oComponentProps.screen.getReferencedTaxonomies();
    CS.forEach(aSelectedTaxonomyIds, function (sTaxonomyId) {
      var oRTaxonomy = oReferencedTaxonomy[sTaxonomyId];
      if (CS.includes(aTaxonomyIdsHavingRP, sTaxonomyId)) {
        var aSelectedClassification = [];
        if (!CS.isEmpty(oRTaxonomy) && _this.isMajorTaxonomy(oRTaxonomy)) {
          _this.getAddedClassificationList(oRTaxonomy, aSelectedClassification);
          var oSelectedTaxonomy = {};
          oSelectedTaxonomy.id = sTaxonomyId;
          oSelectedTaxonomy.addedClassificationList = aSelectedClassification;
          oSelectedTaxonomy.furtherClassificationList = aRemainingTaxonomyList;
          aSelectedTaxonomy.push(oSelectedTaxonomy);
        }
      }
    });

    var aRemainingAllowedTaxonomyList = this.getRemainingAllowedTaxonomyList(aRemainingTaxonomyList);
    return {selected: aSelectedTaxonomy, notSelected: aRemainingAllowedTaxonomyList};
  };

  getComparisonHeaderLabel = (oComparisonLayoutData) => {
    var oComponentProps =  this.getComponentProps();
    var TimelineProps = oComponentProps.timelineProps;
    let oHeaderInformationTable = oComparisonLayoutData["headerInformationTable"];
    let oHeaderInformation = oHeaderInformationTable && oHeaderInformationTable["headerInformation"] || {};
    let aColumnData = oHeaderInformation && oHeaderInformation.columnData || {};
    let oForComparisonColumn = CS.find(aColumnData, {forComparison: true});
    let sContentToCompare = "";
    let sHeader = "";
    let iContentsCount = null;
    let sEntityTypeLabel = null;

    if(CS.isEmpty(oForComparisonColumn)) {
      sHeader = getTranslation().VERSION_COMPARISON_WITHOUT_CURRENT_VERSION_HEADER_LABEL;
      iContentsCount = aColumnData.length;
    } else {
      let bIsVersionComaprison = TimelineProps.getIsComparisonMode();
      sContentToCompare = CS.getLabelOrCode(oForComparisonColumn);
      sHeader = bIsVersionComaprison ? getTranslation().VERSION_COMPARISON_HEADER_LABEL : getTranslation().INSTANCE_COMPARISON_HEADER_LABEL;
      iContentsCount = aColumnData.length - 1;
      let sSelectedModuleId = ContentUtils.getSelectedModuleId();
      sEntityTypeLabel = getTranslation()[sSelectedModuleId + "_type_label"];
    }

    return (
        ViewUtils.getDecodedTranslation(
            sHeader,
            {
              count: iContentsCount,
              label: sContentToCompare,
              entityTypeName: sEntityTypeLabel
            })
    );
  }

  getTimelineData = () => {
    var oActiveContent = this.getActiveEntity();
    var oComponentProps =  this.getComponentProps();
    let oScreenProps = oComponentProps.screen;
    var TimelineProps = oComponentProps.timelineProps;
    var oReferencedPermissions = oScreenProps.getReferencedPermissions() || {};
    let oComparisonLayoutData = TimelineProps.getComparisonLayoutData();
    let sHeaderLabel = this.getComparisonHeaderLabel(oComparisonLayoutData);
    let bIsContentDisabled = this.getIsContentDisabled();
    let bIsComparisonMode = TimelineProps.getIsComparisonMode();
    let oTimeLineToolbarData = this.getTimeLineToolbarData(oComparisonLayoutData, bIsComparisonMode);
    let sSelectedDataLanguageCode = TimelineProps.getSelectedLanguageForComparison();
    let oLanguageSelectorData = this.getLanguageDataForVersionComparison(sSelectedDataLanguageCode);
    let oReferencedLanguages = oScreenProps.getReferencedLanguages();
    let sSelectedHeaderButtonId = TimelineProps.getSelectedHeaderButtonId();

    return {
      zoomLevel: TimelineProps.getZoomLevel(),
      versions: TimelineProps.getVersions(),
      selectedVersionIds: TimelineProps.getSelectedVersionIds(),
      maxSelectedVersionCount: TimelineProps.getMaxSelectedVersionCount(),
      isComparisonMode: bIsComparisonMode,
      comparisonLayoutData: oComparisonLayoutData,
      currentVersion: !CS.isEmpty(oActiveContent) ? oActiveContent.versionId : '',
      isCreatedOnVisible: TimelineProps.getIsCreatedOnVisible(),
      createdOn: oActiveContent.createdOn,
      globalPermission: oReferencedPermissions.globalPermission,
      isArchiveVisible: TimelineProps.getIsArchiveVisible(),
      numberOfAllowedVersions : TimelineProps.getNumberOfAllowedVersions(),
      headerLabel: sHeaderLabel,
      hideRollback: oActiveContent.isMdmInstanceDeleted,
      isContentDisabled: bIsContentDisabled,
      timeLineToolbarData: oTimeLineToolbarData,
      referencedLanguages: oReferencedLanguages,
      languageSelectorData: oLanguageSelectorData,
      selectedHeaderButtonId: sSelectedHeaderButtonId,
      isActiveEntityDirty: ContentUtils.isActiveContentDirty(),
    }
  };

  getLanguageDataForVersionComparison = (sSelectedDataLanguageCode) => {
    let aLanguagesInfo = SessionProps.getLanguageInfoData();
    let aDataLanguages = aLanguagesInfo.dataLanguages;
    let oComponentProps =  this.getComponentProps();
    let aAvailableDLsOfContent = oComponentProps.timelineProps.getVersionComparisionLanguages();
    let oSelectedLanguageForComparison = null;

    if (CS.isEmpty(sSelectedDataLanguageCode)) {
      sSelectedDataLanguageCode = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);
    }

    let aContentDataLanguages = CS.filter(aDataLanguages, function (oDataLanguage) {
      if(oDataLanguage.code === sSelectedDataLanguageCode) {
        oSelectedLanguageForComparison = oDataLanguage;
      }
      return CS.includes(aAvailableDLsOfContent, oDataLanguage.code);
    });

    return {
      list: aContentDataLanguages,
      selectedItemCode: sSelectedDataLanguageCode,
      selectedItem: oSelectedLanguageForComparison
    }
  };

  getLanguageDataForContentComparison = (sSelectedDataLanguageCode, aLanguageList) => {
    if(CS.isEmpty(sSelectedDataLanguageCode)) {
      sSelectedDataLanguageCode = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);
    }
    let oSelectedLanguageForComparison = CS.find(aLanguageList, {code: sSelectedDataLanguageCode});

    return {
      list: aLanguageList,
      selectedItemCode: sSelectedDataLanguageCode,
      selectedItem: oSelectedLanguageForComparison
    }
  };

  getContentComparisonData = () => {
    let oComponentProps =  this.getComponentProps();
    let oScreenProps = oComponentProps.screen;
    let oGoldenRecordProps = this.getComponentProps().goldenRecordProps;
    let bIsComparisonMode = oScreenProps.getIsContentComparisonMode();
    let oComparisonLayoutData = oScreenProps.getContentComparisonLayoutData();
    let sHeaderLabel = this.getComparisonHeaderLabel(oComparisonLayoutData);
    let sSelectedDataLanguageCode = oScreenProps.getSelectedLanguageForComparison();
    let aGoldenRecordLanguageInstances = oGoldenRecordProps.getGoldenRecordLanguageInstances();
    let bIsGoldenRecordViewSourcesDialogOpen = oGoldenRecordProps.getIsGoldenRecordViewSourcesDialogOpen();
    let aLanguagesInfo = SessionProps.getLanguageInfoData();
    let aLanguageList = bIsGoldenRecordViewSourcesDialogOpen ? aGoldenRecordLanguageInstances : aLanguagesInfo.dataLanguages;
    let oLanguageSelectorData = this.getLanguageDataForContentComparison(sSelectedDataLanguageCode, aLanguageList);


    let oRequestResponseInfo = {
      requestType: "customType",
      requestURL: RequestMapping.getRequestUrl(getRequestMapping(ContentScreenConstants.entityModes.ARTICLE_MODE).GetDataLanguage),
      customRequestModel: {},
      responsePath: ["success"]
    };

    return {
      zoomLevel: "",
      versions: ContentUtils.getSelectedContents(),
      selectedVersionIds: ContentUtils.getSelectedContentIds(),
      maxSelectedVersionCount: 60,
      isContentComparisonMode: bIsComparisonMode,
      comparisonLayoutData: oComparisonLayoutData,
      currentVersion: "",
      isCreatedOnVisible: false,
      createdOn: {},
      globalPermission: {},
      isArchiveVisible: false,
      numberOfAllowedVersions : 60,
      headerLabel: sHeaderLabel,
      languageSelectorData: oLanguageSelectorData,
      requestResponseInfo : oRequestResponseInfo,
      isGoldenRecordViewSourcesDialogOpen: bIsGoldenRecordViewSourcesDialogOpen
    };
  };

  getFilteredGridViewData = () => {
    let oContentGridProps = this.getComponentProps().contentGridProps;
    let aGridViewData = oContentGridProps.getGridViewData();
    let aFilteredRowIds = oContentGridProps.getFilteredRowIds();
    let sSearchText = oContentGridProps.getGridViewSearchText();

    if(CS.isEmpty(sSearchText)) {
      return aGridViewData;
    }

    let aFilteredGridViewData = CS.filter(aGridViewData, function (oRow) {
      return !CS.includes(aFilteredRowIds, oRow.id);
    });

    return aFilteredGridViewData;
  };

  getGoldenRecordRemergeSourcesViewData = () => {
    let oComponentProps = this.getComponentProps();
    let oAppData =  this.getAppData();
    let {goldenRecordProps} = oComponentProps;
    let sActiveGoldenRecordId = goldenRecordProps.getActiveGoldenRecordId();
    let oGoldenRecordRule = goldenRecordProps.getGoldenRecordRule();
    let sHeaderLabel = !CS.isEmpty(oGoldenRecordRule) ? getTranslation().MATCH_AND_MERGE + " : " + CS.getLabelOrCode(oGoldenRecordRule)
                                                     : getTranslation().MATCH_AND_MERGE;
    let oGoldenRecordLanguageSelectionSkeleton = goldenRecordProps.getGoldenRecordComparisionSkeleton();
    let oGoldenRecordLanguageSelectionGridData = goldenRecordProps.getGoldenRecordLanguageSelectionGridViewData();
    let oScreenRecordStepperViewActiveStep = goldenRecordProps.getStepperViewActiveStep();
    let oScreenRecordStepperViewSteps = goldenRecordProps.getStepperViewSteps();
    let oGoldenRecordMatchAndMergePropertyDetailedData = goldenRecordProps.getMNMActivePropertyDetailedData();
    let oGoldenRecordComparisonMatchAndMergeData = goldenRecordProps.getMatchAndMergeComparisonData();
    let aSelectedLanguageData = goldenRecordProps.getSelectedLanguageList();
    let oActiveBucketData = goldenRecordProps.getActiveBucketData();
    let oConfigDetails = goldenRecordProps.getGoldenRecordBucketsConfigDetails();
    let aUserList = oAppData.getUserList();
    let oActiveEntity = this.getActiveEntity();

    return {
      contentComparisonMatchAndMergeData: oGoldenRecordComparisonMatchAndMergeData,
      headerLabel: sHeaderLabel,
      isGoldenRecordComparison: true,
      activeGoldenRecordId: sActiveGoldenRecordId,
      contentComparisonSkeleton: oGoldenRecordLanguageSelectionSkeleton,
      contentComparisonSelectionGridData: oGoldenRecordLanguageSelectionGridData,
      stepperViewActiveStep: oScreenRecordStepperViewActiveStep,
      stepperViewSteps: oScreenRecordStepperViewSteps,
      contentComparisonSelectedLanguageData: aSelectedLanguageData,
      contentComparisonActivePropertyDetailedData: oGoldenRecordMatchAndMergePropertyDetailedData,
      activeBucketData: oActiveBucketData,
      configDetails: oConfigDetails,
      userList: aUserList,
      isGoldenRecordRemergeSourcesTabClicked: goldenRecordProps.getIsGoldenRecordRemergeSourcesTabClicked(),
      isActiveContentDirty: oActiveEntity.isDirty,
      isFullScreenMode: goldenRecordProps.getIsFullScreenMode(),
      isGoldenRecordMatchAndMergeViewOpen: goldenRecordProps.getIsMatchAndMergeViewOpen()
    }
  };

  getContentGridActionItemsData = () => {
    let oContentGridProps = this.getComponentProps().contentGridProps;
    let oGridEditViewProps = this.getComponentProps().gridEditViewProps;
    let oActionItemsData = {};
    let aPropertiesList = oGridEditViewProps.getProperties();
    let aPropertySequenceList = oGridEditViewProps.getPropertiesSequenceList().clonedObject || oGridEditViewProps.getPropertiesSequenceList();

    oActionItemsData.editablePropertiesData = {
      propertyList: aPropertiesList,
      sequenceList: aPropertySequenceList,
      isListDirty: oGridEditViewProps.getPropertiesSequenceList().isDirty,
      searchText: oGridEditViewProps.getSearchText(),
      enableLoadMore: true,
      isMultiSelect: true,
      showSelectedItems: true,
      disabled: false,
      isDialogOpen: oGridEditViewProps.getIsDialogOpen(),
      sequenceListLimit: oContentGridProps.getSequenceListLimit()
    };

    return oActionItemsData;
  }

  getColumnOrganizerData = () => {
    let oGridEditViewProps = this.getComponentProps().gridEditViewProps;
    let oContentGridProps = this.getComponentProps().contentGridProps;
    let aTotalColumns = oGridEditViewProps.getProperties().concat(oGridEditViewProps.getPropertiesSequenceList());
    return {
      isColumnOrganizerDialogOpen: oGridEditViewProps.getIsDialogOpen(),
      selectedColumns: oGridEditViewProps.getPropertiesSequenceList(),
      availableColumns: aTotalColumns,
      searchText: oGridEditViewProps.getSearchText(),
      selectedColumnsLimit: oContentGridProps.getSequenceListLimit(),
      bIsLoadMore: oGridEditViewProps.getIsLoadMore()
    }
  };

  getContentGridEditData = () => {
    let oContentGridProps = this.getComponentProps().contentGridProps;
    let aGridViewData = this.getFilteredGridViewData();

    let oContentGridEditViewData = {
      gridViewSkeleton: oContentGridProps.getGridViewSkeleton(),
      gridViewData: aGridViewData,
      gridViewVisualData: oContentGridProps.getGridViewVisualData(),
      paginationData: oContentGridProps.getGridViewPaginationData(),
      searchText: oContentGridProps.getGridViewSearchText(),
      sortBy: oContentGridProps.getGridViewSortBy(),
      sortOrder: oContentGridProps.getGridViewSortOrder(),
      isGridDataDirty: oContentGridProps.getIsGridDataDirty(),
      showCheckboxColumn: oContentGridProps.getShowCheckboxColumn(),
      disableCreate: oContentGridProps.getDisableCreate(),
      disableDelete: oContentGridProps.getDisableDelete(),
      actionItemsData: this.getContentGridActionItemsData(),
      disableRefresh: true,
      isContentGridEditViewOpen: oContentGridProps.getIsContentGridEditViewOpen(),
      columnOrganizerData: this.getColumnOrganizerData(),
      resizedColumnId: oContentGridProps.getResizedColumnId()
    };

    return {
      isContentGridEditMode: (oContentGridProps.getIsContentGridEditViewOpen()),
      contentGridEditViewData: oContentGridEditViewData
    }
  };

  getUOMData = (sContextId, sContextLabel, bIsSectionExpanded, sAttributeId, oExtraData = {}) => {
    let oComponentProps =  this.getComponentProps();
    let oUOMProps = oComponentProps.uomProps;
    let sIdForTableProps = ContentUtils.getIdForTableViewPropsSimple(sContextId, sAttributeId);
    let oActiveContent = ContentUtils.getActiveContent();
    let sKlassInstanceId = oExtraData.klassInstanceId || oActiveContent.id;
    let oTableViewProps = oComponentProps.tableViewProps.getTableViewPropsByContext(sIdForTableProps, sKlassInstanceId);
    let oTableFilterViewProps = oComponentProps.tableViewProps.getTableFilterViewPropsByContext(sIdForTableProps, sKlassInstanceId);
    let oColumnOrganizerProps = oComponentProps.tableViewProps.getColumnOrganizerPropsByContext(sIdForTableProps, sKlassInstanceId);
    let oContextVariantProps = oComponentProps.tableViewProps.getVariantContextPropsByContext(sIdForTableProps, sKlassInstanceId);

    let bShowCreateButton, bShowDateRangeSelector;
    let bIsTableContentDirty;
    let bShowColumnOrganiser;
    let bShowFilterView;
    let oTableData = {};
    let oFilterData = {};
    let bIsSectionLoading = false;
    let sViewMode =  "";
    if (!CS.isEmpty(oTableViewProps)) {
      sViewMode =  oTableViewProps.getViewMode();
      let oTableOrganiserConfig = oTableViewProps.getTableOrganiserConfig();
      bShowCreateButton = oTableOrganiserConfig.showCreateButton;
      bIsTableContentDirty = oTableOrganiserConfig.isTableContentDirty;
      bShowDateRangeSelector = oTableOrganiserConfig.showDateRangeSelector;
      bShowColumnOrganiser = oTableOrganiserConfig.showColumnOrganiser;
      bShowFilterView = oTableOrganiserConfig.showFilterView;
      bIsSectionLoading = oTableViewProps.getIsSectionLoading();
      let oTableSettings = CS.clone(oTableViewProps.getSettings());
      oTableSettings.sortData = oTableFilterViewProps.getSortData();
      oTableSettings.paginationData = oTableFilterViewProps.getPaginationData();
      oTableSettings.timeRangeData = oTableFilterViewProps.getTimeRangeData();
      oTableData = {
        tableHeaderData: oTableViewProps.getHeaderData(),
        tableBodyData: oTableViewProps.getBodyData(),
        tableSettings: oTableSettings,
        tableSelectedHeaders: oTableViewProps.getSelectedHeaders(),
        columnOrganiserSelectedHeaders: oTableViewProps.getSelectedHeaders(),
        columnOrganiserMasterList: oTableViewProps.getColumnOrganiserHeaderData()
      };
    }
    if (!CS.isEmpty(oTableFilterViewProps)) {
      oFilterData = {
        availableFilterData: oTableFilterViewProps.getAvailableFilters(),
        appliedFilterData: oTableFilterViewProps.getAppliedFilters(),
        appliedFilterDataClone: oTableFilterViewProps.getAppliedFiltersClone(),
        searchText: oTableFilterViewProps.getAllSearchText(),
        masterAttributeList: oContextVariantProps.getReferencedAttributes()
      };
    }

    if (sViewMode == ContentScreenConstants.UOM_BODY_GRID_MODE) {
      let oContentGridProps = oComponentProps.contentGridProps;
      let oViewModeData = oExtraData.viewModeData;
      let sKey = oExtraData.gridComponentKey || "";
      oTableData.data = oTableViewProps.getBodyData();
      oTableData.skeleton = ContentUtils.getSelectedTabId() === ContentScreenConstants.tabItems.TAB_RENDITION ? this.getUOMTableGridDataForRenditionTab(oTableViewProps.getGridSkeleton()) : oTableViewProps.getGridSkeleton();
      oTableData.isGridDataDirty = oContentGridProps.getIsGridDataDirty();
      oTableData.resizedColumnId = oTableViewProps.getResizedColumnId();
      if (CS.isNotEmpty(oViewModeData)) {
        let oViewModeMenuModel = {};
        oViewModeMenuModel.items = oViewModeData.items;
        oViewModeMenuModel.selectedList = [oViewModeData.value];
        oViewModeMenuModel.excludedItems = [oViewModeData.value];
        oViewModeMenuModel.valueLabel = oViewModeData.valueLabel;
        oTableData.viewModeMenuModel = oViewModeMenuModel;
      }
      oTableData.key = sKey;
    }
    let oUOMVariantViewData = this.getUOMVariantViewData(sContextId, oExtraData);
    let bIsFullScreenViewMode = (sContextId === oUOMProps.getFullScreenTableContextId());
    let bIsColumnOrganizerDialogOpen = oColumnOrganizerProps.getIsDialogOpen();
    let aSelectedColumns = oColumnOrganizerProps.getSelectedOrganizedColumns();

    return {
      contextId: sContextId,
      contextLabel: sContextLabel,
      isSectionExpanded: bIsSectionExpanded,
      isSectionLoading: bIsSectionLoading,
      viewMode: sViewMode,
      tableData: oTableData,
      filterData: oFilterData,
      UOMVariantViewData:oUOMVariantViewData,
      showCreateButton: bShowCreateButton,
      isTableContentDirty: bIsTableContentDirty,
      showDateRangeSelector: bShowDateRangeSelector,
      showColumnOrganiser: bShowColumnOrganiser,
      showFilterView: bShowFilterView,
      isFullScreenMode: bIsFullScreenViewMode,
      isColumnOrganizerDialogOpen: bIsColumnOrganizerDialogOpen,
      selectedColumns: aSelectedColumns,
    }
  };

  getUOMTableGridDataForRenditionTab = (oSkeleton) => {
    let oComponentProps = this.getComponentProps();
    let oBulkDownloadProps = oComponentProps.bulkDownloadAssetProps;
    let oActiveEntity = this.getActiveEntity();
    let oDownloadImageData = CS.find(oSkeleton.actionItems, {id: "downloadImage"});
    let bIsShowDownloadDialog = false;
    if (oBulkDownloadProps.getShowDownloadDialog() && CS.isNotEmpty(oDownloadImageData)) {
      bIsShowDownloadDialog = oBulkDownloadProps.getShowDownloadDialog();
      let sSelectedContextId = oBulkDownloadProps.getSelectedContextId();
      let sSelectedContentId = oBulkDownloadProps.getSelectedContentId();
      let sDownloadFileName = oBulkDownloadProps.getDownloadFileName();
      let bShouldDownloadAssetWithOriginalFileName = oBulkDownloadProps.getShouldDownloadAssetWithOriginalFilename();
      if (CS.isNotEmpty(sSelectedContextId) && CS.isEmpty(sDownloadFileName)) {
        let oTableViewProps = oComponentProps.tableViewProps.getTableViewPropsByContext(sSelectedContextId, oActiveEntity.id);
        let aRowData = oTableViewProps.getOriginalRowData();
        let oTIVRowData = CS.find(aRowData, {id: sSelectedContentId});
        if (oTIVRowData) {
          const sFileNameRegex = /[\\\/:*?"<>|“.]/;
          let oTIVProperties = oTIVRowData.properties;
          let oAssetInformation = oTIVRowData.assetInformation;
          let sTIVInstanceName = oTIVProperties.nameattribute.value;
          let sTIVFileName = oAssetInformation.fileName;
          sTIVFileName = CS.isEmpty(sTIVFileName) ? sTIVInstanceName : sTIVFileName.substr(0, sTIVFileName.lastIndexOf('.'));
          let sDownloadFileName = oTIVProperties && bShouldDownloadAssetWithOriginalFileName ? sTIVFileName : sTIVInstanceName;
          if (sDownloadFileName.match(sFileNameRegex)) {
            let oDownloadAsExtraData = {};
            let aInvalidRowIds = oBulkDownloadProps.getInvalidRowIds();
            aInvalidRowIds.push("downloadAs");
            oDownloadAsExtraData.downloadFileNameErrorMsg = getTranslation().INVALID_CHARACTERS_IN_FILE_NAME;
            oDownloadAsExtraData.isDownloadAsError = true;
            oBulkDownloadProps.setDownloadAsExtraData(oDownloadAsExtraData);
            oBulkDownloadProps.setInvalidRowIds(aInvalidRowIds);
          }
          oBulkDownloadProps.setDownloadFileName(sDownloadFileName);
        }
      }
      let oAssetDownloadDialogData = ContentUtils.getDownloadDialogData(false, true);
      oDownloadImageData.selectedContentId = sSelectedContentId;
      oDownloadImageData.downloadDialogData = oAssetDownloadDialogData;
    }
    oDownloadImageData.showDownloadDialog = bIsShowDownloadDialog;

    return oSkeleton;
  };

  getUOMView = (sContextId, sAttributeId, oOpenedDialogAttributeData={}) => {
    let oComponentProps = this.getComponentProps();
    let oReferencedVariantContexts = oComponentProps.screen.getReferencedVariantContexts();
    let bIsRowSelectionMode = false;
    let oRowSelectionData = {};
    let sContext = "";
    let oEmbeddedVariantContexts = oReferencedVariantContexts.embeddedVariantContexts;
    let oReferencedContext = oEmbeddedVariantContexts[sContextId];
    let oUOMView = null;

    if (oReferencedContext) {
      let oUOMData = this.getUOMData(sContextId, CS.getLabelOrCode(oReferencedContext), true, sAttributeId, oOpenedDialogAttributeData);
      oUOMData.hidePaper = true;
      oUOMData.isRowSelectionMode = bIsRowSelectionMode;
      oUOMData.rowSelectionData = oRowSelectionData;
      oUOMData.context = sContext;
      oUOMData.filterContext = {
        filterType: oFilterPropType.MODULE,
        screenContext: sContextId
      }

      oUOMView = (
          <UOMView {...oUOMData}/>
      );
    }

    return oUOMView;
  };

  getAttributeContextDialogViewData = () => {
    let oComponentProps = this.getComponentProps();
    let oOpenedDialogAttributeData = oComponentProps.uomProps.getOpenedDialogAttributeData();
    let sOpenedDialogAttributeContextId = oOpenedDialogAttributeData.contextId;
    let sAttributeId = oOpenedDialogAttributeData.attributeId;
    let oAttributeContextDialogViewData = {};
    if (sOpenedDialogAttributeContextId) {
      oAttributeContextDialogViewData.attributeContextId = sOpenedDialogAttributeContextId;
      oAttributeContextDialogViewData.uomView = this.getUOMView(sOpenedDialogAttributeContextId, sAttributeId, oOpenedDialogAttributeData);
      oAttributeContextDialogViewData.isDirty = oComponentProps.contentGridProps.getIsGridDataDirty();
      oAttributeContextDialogViewData.label = oOpenedDialogAttributeData.label;
    }
    return oAttributeContextDialogViewData;
  };

  getPromotionCampaignTagViewData = () => {
    var oComponentProps = this.getComponentProps();
    var oActiveEntity = this.getActiveEntity();
    var oTargetRelationship = {};
    var aContextTags = (oTargetRelationship && oTargetRelationship.contextTags) ? oTargetRelationship.contextTags : [];
    var aReferencedTags = oComponentProps.screen.getReferencedTags();

    var aMasterTagGroup = [];
    CS.forEach(aContextTags, function (sTagId) {
      aMasterTagGroup.push(aReferencedTags[sTagId]);
    });

    var aTagInstances = [];
    CS.forEach(aContextTags, function (sTagId) {
      var oTagInstance = CS.find(oActiveEntity.tags, {tagId: sTagId});
      if(!CS.isEmpty(oTagInstance)){
        aTagInstances.push(oTagInstance);
      }
    });

    return ({
      masterTagGroups: aMasterTagGroup,
      tagInstances: aTagInstances,
      disabled: false //TODO : Need to handle according to permissions
    })
  };

  getMinDateObjectForEventSchedule = () => {
    //Currently ignoring Seconds/Milliseconds as they are not selected in the view hence they cause comparison issue
    // wrt to min time
    let oCreatedOn = new Date();
    oCreatedOn.setMilliseconds(0);
    oCreatedOn.setSeconds(0);
    return oCreatedOn;
  };

  getEventScheduleViewData = () => {
    var oActiveEntity = this.getActiveEntity();
    var oEventSchedule = oActiveEntity.eventSchedule || {};

    var oSchedulerViewData = {
      id: oActiveEntity.id,
      label: CS.getLabelOrCode(oActiveEntity),
      isDirty: oActiveEntity.isDirty,
      startTime: oEventSchedule.startTime ? new Date(oEventSchedule.startTime) : null,
      hideRemoveButton: false,
      createdOn: this.getMinDateObjectForEventSchedule()
    };

    oSchedulerViewData.endTime = oEventSchedule.endTime ? new Date(oEventSchedule.endTime) : null;

    return oSchedulerViewData;
  };

  getUOMDialogVariant = (sOpenedContextId) => {
    let oComponentProps = this.getComponentProps();
    let oVariantToReturn = {};
    let sIdForProps = ContentUtils.getIdForTableViewProps(sOpenedContextId);
    let oActiveContent = ContentUtils.getActiveContent();
    let oOpenedDialogAttributeData = oComponentProps.uomProps.getOpenedDialogAttributeData();
    let sActiveContentId = oOpenedDialogAttributeData.klassInstanceId || oActiveContent.id;
    var oUOMVariantViewProps = oComponentProps.tableViewProps.getVariantContextPropsByContext(sIdForProps, sActiveContentId);
    if(!CS.isEmpty(oUOMVariantViewProps)){
      var oDummyVariant = oUOMVariantViewProps.getDummyVariant();
      oDummyVariant = oDummyVariant.contentClone ? oDummyVariant.contentClone : oDummyVariant;
      var oVariant = oUOMVariantViewProps.getActiveVariantForEditing();
      oVariant = oVariant.contentClone ? oVariant.contentClone : oVariant;
      oVariantToReturn = CS.isEmpty(oVariant) ? oDummyVariant : oVariant;
    }
    return oVariantToReturn;
  };

  getTaxonomyType = (oTaxonomy) => {
    if (CS.isEmpty(oTaxonomy.taxonomyType)) {
      return this.getTaxonomyType(oTaxonomy.parent);
    } else {
      return oTaxonomy.taxonomyType;
    }
  };

  getTableActivePopOverContextEntityData = () => {
    let oComponentProps = this.getComponentProps();
    let oActivePopOverContextData = oComponentProps.uomProps.getActivePopOverContextData();
    let oActiveContent = ContentUtils.getActiveContent();
    let oPopOverContextTableViewProps = oComponentProps.tableViewProps.getTableViewPropsByContext(oActivePopOverContextData.idForProps || oActivePopOverContextData.contextId, oActiveContent.id);
    let aBodyData = oPopOverContextTableViewProps.getBodyData();
    let oRow;
    let oActivePopOverContextEntityData;
    if (oActivePopOverContextData.isTranspose) {
      oRow = CS.find(aBodyData, {id: oActivePopOverContextData.entity});
      oActivePopOverContextEntityData = oRow.properties[oActivePopOverContextData.contextInstanceId];
    } else {
      oRow = CS.find(aBodyData, {id: oActivePopOverContextData.contextInstanceId});
      oActivePopOverContextEntityData = oRow.properties[oActivePopOverContextData.entity];
    }
    return oActivePopOverContextEntityData;
  };

  getAvailableEntityDataForQuickList = (oAddEntityInRelationshipScreenData, oExtraData) => {
    let oComponentProps =  this.getComponentProps();
    let oActiveEntity = this.getActiveEntity();
    let oActiveRelationshipScreenData = oAddEntityInRelationshipScreenData[oActiveEntity.id];
    let oActiveRelationship = oComponentProps.screen.getReferencedElements()[oActiveRelationshipScreenData.id];
    let bHideAddButton = false;

    if (oActiveRelationship) {
      let oRelationshipSide = oActiveRelationship.relationshipSide || {};
      let aElements = oComponentProps.relationshipView.getReferenceRelationshipInstanceElements()[oActiveRelationshipScreenData.id] || [];
      bHideAddButton = oRelationshipSide.cardinality == RelationshipConstants.ONE && aElements.length > 0;
    }
    let oFilterContext = {
      filterType: oFilterPropType.QUICKLIST,
      screenContext: oFilterPropType.QUICKLIST
    };

    let sContext = "relationshipEntity";
    if(oActiveRelationshipScreenData.context === ContextTypeDictionary.PRODUCT_VARIANT) {
      sContext = "productVariantQuickList";
    }

    return this.getAvailableEntityData(sContext, bHideAddButton, false, oFilterContext, {}, oActiveRelationship, oExtraData);
  };

  getSwitchTypeData = () => {
    let _this = this;
    var oComponentProps =  this.getComponentProps();
    let oScreenProps = oComponentProps.screen;
    var oActiveEntity = this.getActiveEntity();
    var oReferencedClasses = oComponentProps.screen.getReferencedClasses();
    var aSelectedTypes = oActiveEntity.types;

    var aTempClassListForTemplateDropDown = [];
    CS.forEach(aSelectedTypes, function (sId) {
      let oClass = oReferencedClasses[sId];
      if (CS.isEmpty(oClass)) {
        return;
      }
      aTempClassListForTemplateDropDown.push({
        id: oClass.id,
        label: CS.getLabelOrCode(oClass),
        type: "klassTemplate",
        icon: oClass.icon,
        baseType: oClass.type
      })
    });

    var oReferencedTaxonomies = oScreenProps.getReferencedTaxonomies();
    var aSelectedTaxonomyIds = oActiveEntity.selectedTaxonomyIds;
    var aTempTaxonomyListForTemplateDropDown = [];
    let sTaxonomyType = "";
    CS.forEach(aSelectedTaxonomyIds, function (sId) {
      try{
        let oTaxonomy = oReferencedTaxonomies[sId];
        sTaxonomyType = _this.getTaxonomyType(oTaxonomy);
        if(sTaxonomyType === "majorTaxonomy") {
          aTempTaxonomyListForTemplateDropDown.push({
            id: oTaxonomy.id,
            label: CS.getLabelOrCode(oTaxonomy),
            type: "taxonomyTemplate",
            icon: oTaxonomy.icon,
            baseType: oTaxonomy.baseType
          })
        }
      }
      catch (oException){
        ExceptionLogger.error("Referenced Taxonomy Not Found : ");
        ExceptionLogger.error(oException);
      }
    });
    var aTemplateList = oScreenProps.getReferencedTemplates() || [];
    var aAllTemplateDropDownList = aTemplateList.concat(aTempClassListForTemplateDropDown);
    return aAllTemplateDropDownList.concat(aTempTaxonomyListForTemplateDropDown);
  };

  modifyAndGetReferencedPermissions = (sSelectedTabId) => {
    var oComponentProps =  this.getComponentProps();
    var oReferencedPermissions = oComponentProps.screen.getReferencedPermissions();
    var bIsOverviewTab = (sSelectedTabId === ContentScreenConstants.tabItems.TAB_OVERVIEW);

    oReferencedPermissions.headerPermission && (oReferencedPermissions.headerPermission.showClassTaxonomyInHeader = !bIsOverviewTab);

    return oReferencedPermissions;
  };

  getTabSpecificDataForEntityEditView = (sSelectedTabId) => {
    switch (sSelectedTabId) {
      case ContentScreenConstants.tabItems.TAB_TIMELINE:
        return this.getTimelineData();

      case ContentScreenConstants.tabItems.TAB_TASKS:
        return this.getComponentProps().taskProps.getTaskData();

      case ContentScreenConstants.tabItems.TAB_RENDITION:
        return this.getComponentProps().screen.getImageVariantData();

      case ContentScreenConstants.tabItems.TAB_DUPLICATE_ASSETS:
        return this.getTabSpecificContentTileListView(sSelectedTabId);
    }
  };

  getEntityEditViewData = () => {
    let oComponentProps =  this.getComponentProps();
    let oActiveEntity = this.getActiveEntity();
    let oSelectedContext = CS.cloneDeep(oComponentProps.screen.getSelectedContext());
    let oGlobalPermission = oActiveEntity.globalPermission || {};
    let sSelectedTabId = ContentUtils.getSelectedTabId();

    if (CS.isEmpty(oActiveEntity)) {
      return null;
    }

    let oScreenProps = oComponentProps.screen;
    let bIsContentDisabled = this.getIsContentDisabled();
    let bIsDisableHeader = ContentUtils.isOnHeaderDisabledTab() || bIsContentDisabled;

    let sBranchOfLabel = oScreenProps.getBranchOfLabel() || "";
    let sVariantOfLabel = oScreenProps.getVariantOfLabel() || "";
    let bEntityLabelEditable = oComponentProps.screen.getEntityHeaderLabelInEditMode();

    let oActiveTemplate = oComponentProps.screen.getReferencedTemplate() || {};

    let oEventScheduleSectionViewData = {
      eventData: this.getEventScheduleViewData(),
      tagData: this.getPromotionCampaignTagViewData(),
      validityMessage: oActiveEntity.messages.validityMessage
    };

    let bIsOverviewTab = (sSelectedTabId === ContentScreenConstants.tabItems.TAB_OVERVIEW);

    let oEntityViewData = {
      screen: BreadCrumbModuleAndHelpScreenIdDictionary.CONTENT,
      isAcrolinxSidebarVisible: oComponentProps.screen.getIsAcrolinxSidebarVisible(),
      userList: this.getAppData().getUserList(),
      activeEntity: oActiveEntity,
      isTaskForceUpdate: oComponentProps.taskProps.getIsTaskForceUpdate(),

      requestURLToGetDefaultTypesForRelationhsip : getRequestMapping().GetDefaultTypes,

      /**TODO: THINK TO REMOVE IT**/
      violatingMandatoryElements: oComponentProps.contentSectionViewProps.getViolatingMandatoryElements(),

      /** Selected section and property info in contentEditView **/
      selectedContext: oSelectedContext,

      /** Content Classes **/
      natureClassLabel: this.getNatureTypeClassData(),

      /** @type Object - overview tab data **/
      overviewTabData: bIsOverviewTab ? this.getOverViewTabData() : {},

      /** @type Object - Content Open View Header Data**/
      contentOpenViewLeftSideData: !bIsOverviewTab ? this.getContentOpenViewLeftSideData() : {},

      /** Global & Referenced Permission **/
      globalPermission: oGlobalPermission,
      referencedPermissions: this.modifyAndGetReferencedPermissions(sSelectedTabId),

      /** @type Array - Content Tabular Data**/
      entityTabData: this.getEntityTabData(),

      /** @type Array - Nature Relationship Data Preparation **/
      natureSectionViewData: this.getNatureSectionViewData(),

      /** @type Boolean - Content name edit mode **/
      entityLabelEditable: bEntityLabelEditable,

      /** @type String - Label of the parent content from where it got branched **/
      branchOfLabel: sBranchOfLabel,

      /** @type Array - Available list of templates to show on header **/
      templateList: this.getSwitchTypeData(),

      /** @type Object - To show label of Active Template and hide from available template list **/
      activeTemplate: oActiveTemplate,

      /** @type Array - To display tags on header of variant (Circular Tags) **/
      activeVariantTagDisplayData: this.getActiveVariantTagDisplayData(),

      /** @type Object - Tab specific data (Golden Record source tab | duplicate assets) **/
      tabSpecificData: this.getTabSpecificDataForEntityEditView(sSelectedTabId),

      /** @type Object - To show Schedule of Campaign and Promotion **/
      eventScheduleSectionViewData: oEventScheduleSectionViewData,

      /** @type String - To show Linked Variant Version Of Label**/
      variantOfLabel: sVariantOfLabel,

      /** @type Object - To show task data **/
      referencedTasks: oComponentProps.taskProps.getReferencedTasks(),

      /** @type Boolean - Disable entity header data**/
      isDisableHeader: bIsDisableHeader,

      /** @type Boolean - Used at schedule Information & timeline to disable editing**/
      isContentDisabled: bIsContentDisabled,

      isEditMode: oScreenProps.getIsEditMode(),

      isRelationshipContextDialogOpen: oScreenProps.getIsRelationshipContextDialogOpen(),
      side2NatureClassOfProductVariant: oScreenProps.getIsRelationshipContextDialogOpen() ?
          oComponentProps.variantSectionViewProps.getSide2NatureClassOfProductVariant() : null,
      assetShareButtonContext : oComponentProps.bulkAssetLinkSharingProps.getAssetShareContext(),
    };

    CS.assign(oEntityViewData, this.getArticleViewData());

    /** To prepare dialog data. */
    this.prepareDialogDataForProductOpenView(oEntityViewData);

    return {oEntityViewData};
  };

  getTaxonomyInheritanceData = () => {
    let oComponentProps = this.getComponentProps();
    let oMultiClassificationProps =  oComponentProps.multiClassificationViewProps;
    let bOpenDialog = oMultiClassificationProps.getTaxonomyInheritanceDialog();
    return {
      bOpenDialog: bOpenDialog,
      contentIdVsTypesTaxonomies: oMultiClassificationProps.getContentIdVsTypesTaxonomies(),
      referencedTaxonomies: oMultiClassificationProps.getReferencedTaxonomyMap(),
      parentContentId: oMultiClassificationProps.getEntityParentContentId(),
      articleId: this.getActiveEntity().id
    }
  }

  getContentOpenViewLeftSideData = ()=> {
    let oComponentProps =  this.getComponentProps();
    let oScreenProps = oComponentProps.screen;
    let oActiveEntity = this.getActiveEntity();
    let oBulkDownloadProps = oComponentProps.bulkDownloadAssetProps;
    let oBulkAssetLinkSharingProps = oComponentProps.bulkAssetLinkSharingProps;
    let bIsShowDownloadDialog = oBulkDownloadProps.getShowDownloadDialog();
    let bShowLinkSharingDialog = oBulkAssetLinkSharingProps.getShowAssetLinkSharingDialog();
    let sFileName = "";
    let oAssetDownloadData = {};
    let oAssetLinkSharingDialogData = {};

    if (bIsShowDownloadDialog && CS.isEmpty(oBulkDownloadProps.getSelectedContentId())) {
      if (CS.isNotEmpty(oActiveEntity)) {
        let bShouldDownloadAssetWithOriginalFilename = oBulkDownloadProps.getShouldDownloadAssetWithOriginalFilename();
        if (CS.isEmpty(oBulkDownloadProps.getDownloadFileName())) {
          sFileName = oActiveEntity.assetInformation.fileName;
          sFileName = sFileName && sFileName.substr(0, sFileName.lastIndexOf('.'));
          const sFileNameRegex = /[\\\/:*?"<>|“.]/;
          let sDownloadFileName = bShouldDownloadAssetWithOriginalFilename ? sFileName : oActiveEntity.name;
          if (sDownloadFileName.match(sFileNameRegex)) {
            let oDownloadAsExtraData = {};
            let aInvalidRowIds = oBulkDownloadProps.getInvalidRowIds();
            aInvalidRowIds.push("downloadAs");
            oDownloadAsExtraData.downloadFileNameErrorMsg = getTranslation().INVALID_CHARACTERS_IN_FILE_NAME;
            oDownloadAsExtraData.isDownloadAsError = true;
            oBulkDownloadProps.setDownloadAsExtraData(oDownloadAsExtraData);
            oBulkDownloadProps.setInvalidRowIds(aInvalidRowIds);
          }
          oBulkDownloadProps.setDownloadFileName(sDownloadFileName);
        }
      }
      let bisHideDownloadAsExtension = CS.isNotEmpty(oActiveEntity.context);
      oAssetDownloadData = ContentUtils.getDownloadDialogData(false, bisHideDownloadAsExtension);
    }
    if (bShowLinkSharingDialog) {
      let oAssetDownloadGridData = {
        gridViewSkeleton: oBulkAssetLinkSharingProps.getGridViewSkeleton(),
        gridViewVisualData: oBulkAssetLinkSharingProps.getGridViewVisualData(),
        gridViewData: oBulkAssetLinkSharingProps.getGridViewData(),
        totalNestedItems: oBulkAssetLinkSharingProps.getTotalNestedItems()
      };

      oAssetLinkSharingDialogData = {
        assetLinkSharingGridViewData: oAssetDownloadGridData,
      }
    }

    let oShareAssetData = {
      assetSharedUrl: oScreenProps.getAssetSharedUrl(),
      bIsEmbeddedAsset : (ContentUtils.getNatureTypeFromContent(oActiveEntity) === NatureTypeDictionary.EMBEDDED)
    };

    return {
      multiClassificationData: this.getMultiClassificationDataAccordingToPermissions(oComponentProps.multiClassificationViewProps.getMultiClassificationData()),
      lifeCycleTagGroupModels: this.getLifeCycleTagGroupModels(oActiveEntity, oScreenProps),
      kpiData: this.getKPIData(oActiveEntity, oScreenProps),
      /** asset download and shareUrl data**/
      assetDownloadData: oAssetDownloadData,
      assetLinkSharingDialogData: oAssetLinkSharingDialogData,
      shareAssetData: oShareAssetData
    };
  };

  getMultiClassificationDataAccordingToPermissions = (oMultiClassificationData) => {
    let oUpdatedMultiClassificationData = CS.cloneDeep(oMultiClassificationData);
    let oComponentProps =  this.getComponentProps();
    let oScreenProps = oComponentProps.screen;
    let oReferencedPermissions = oScreenProps.getReferencedPermissions();
    let aActiveClassIds = oScreenProps.getActiveClassIds();
    let MultiClassificationViewTypesIds = new MultiClassificationDialogToolbarLayoutData().multiClassificationViewTypesIds;
    let oReferencedClasses = oUpdatedMultiClassificationData[MultiClassificationViewTypesIds.CLASSES];
    let oReferencedTaxonomy = oUpdatedMultiClassificationData[MultiClassificationViewTypesIds.TAXONOMIES];
    let aKlassIdsHavingRP = oReferencedPermissions.klassIdsHavingRP || [];
    let aTaxonomyIdsHavingRP = oReferencedPermissions.allTaxonomyIdsHavingRP || [];
    let aSelectedTaxonomyIds = oScreenProps.getActiveTaxonomyIds();

    if(aKlassIdsHavingRP.length){
      CS.forEach(aActiveClassIds, function (sClassId) {
        if (!CS.includes(aKlassIdsHavingRP, sClassId)) {
          delete oReferencedClasses[sClassId];

          return;
        }
      });
    }

    CS.forEach(aSelectedTaxonomyIds, function (sTaxonomyId) {
      if (!CS.includes(aTaxonomyIdsHavingRP, sTaxonomyId)) {
        delete oReferencedTaxonomy[sTaxonomyId];

        return;
      }
    });

    return oUpdatedMultiClassificationData;
  };

  getMultiClassificationDialogData = () => {
    let oComponentProps = this.getComponentProps();
    let oMultiClassificationViewProps = oComponentProps.multiClassificationViewProps;
    let aClassificationDialogToolbarData = new MultiClassificationDialogToolbarLayoutData().multiClassificationToolbar;
    let sSelectedTabInClassificationDialog = oMultiClassificationViewProps.getSelectedTabInClassificationDialog();

    CS.forEach(aClassificationDialogToolbarData, function (oData) {
      oData.isActive = oData.id === sSelectedTabInClassificationDialog;
    });

    let oMultiClassificationData = oMultiClassificationViewProps.getMultiClassificationData();
    let oMultiClassificationClonedObject = oMultiClassificationData.clonedObject;
    let bIsDirty = CS.isNotEmpty(oMultiClassificationClonedObject);
    let oData = bIsDirty ? oMultiClassificationClonedObject : oMultiClassificationData;
    oData = this.getMultiClassificationDataAccordingToPermissions(oData);

    return {
      classificationDialogToolbarData: {multiClassificationToolbar: aClassificationDialogToolbarData},
      selectedTabIdInClassificationDialog: sSelectedTabInClassificationDialog,
      multiClassificationData: oData,
      isDirty: bIsDirty,
      multiClassificationTreeData: {
        treeData: TreeViewProps.getTreeData(),
        searchText: oMultiClassificationViewProps.getMultiClassificationTreeSearchText(),
        treeLoadMoreMap: TreeViewProps.getTreeLoadMoreMap(),
        context: sSelectedTabInClassificationDialog
      }
    }
  };

  getAssetDownloadTrackerData = (oOverViewTabData) => {
    let oActiveContent = ContentUtils.getActiveContent();
    if (oActiveContent.baseType !== EntityBaseTypeDictionary.assetBaseType) {
      return oOverViewTabData;
    }
    let oAssetDownloadTrackerProps = this.getComponentProps().assetDownloadTrackerProps;
    oOverViewTabData.assetDownloadInfo = {
      downloadRange: oAssetDownloadTrackerProps.getDownloadRange(),
      downloadCountWithinRange: oAssetDownloadTrackerProps.getDownloadCountWithinRange(),
      totalDownloadCount: oAssetDownloadTrackerProps.getTotalDownloadCount(),
      context: ContentScreenConstants.sectionTypes.SECTION_TYPE_DOWNLOAD_INFO
    };
    return oOverViewTabData;
  };

  getOverViewTabData = ()=> {
    let oComponentProps =  this.getComponentProps();
    let bShowClassificationDialog =  oComponentProps.multiClassificationViewProps.getIsShowClassificationDialog();
    let oOverViewTabData = this.getContentOpenViewLeftSideData();
    oOverViewTabData = this.getAssetDownloadTrackerData(oOverViewTabData);
    let oReferencedPermissions = ContentScreenProps.screen.getReferencedPermissions();
    let oHeaderPermission = oReferencedPermissions.headerPermission;
    let bCanEdit = oHeaderPermission.canAddClasses || oHeaderPermission.canDeleteClasses
        || oHeaderPermission.canAddTaxonomy || oHeaderPermission.canDeleteTaxonomy;

    oOverViewTabData.showClassificationDialog = bShowClassificationDialog;
    oOverViewTabData.showEditClassificationIcon = bCanEdit && !this.getIsContentDisabled();

    oOverViewTabData.taxonomyInheritanceData = this.getTaxonomyInheritanceData();
    oOverViewTabData.taxonomyConflictingValues = this.getActiveEntity().taxonomyConflictingValues || [];

    if(oOverViewTabData.showClassificationDialog && bCanEdit) {
      oOverViewTabData.classificationDialogData = this.getMultiClassificationDialogData();
    }

    return oOverViewTabData;
  };

  getKPIData = (oActiveEntity, oScreenProps)=> {
    let bShowClassTaxonomyInHeader = (ContentUtils.getSelectedTabId() !== ContentScreenConstants.tabItems.TAB_OVERVIEW);
    let oActiveEntitySelectedTabIdMap = oScreenProps.getActiveEntitySelectedTabIdMap();
    let oActiveContentMap = oActiveEntitySelectedTabIdMap[oActiveEntity.id] || {};
    let oKpiSummaryViewData = {
      kpiList: oScreenProps.getKpiListForSummary(),
      selectedKPIId: oActiveContentMap.selectedKPIId,
      showSummaryDataInHeader: bShowClassTaxonomyInHeader,
      selectedKpiChartType: oScreenProps.getSelectedKPIChartType()
    };

    return {
      kpiSummaryViewData: oKpiSummaryViewData,
      contentKPIDataMap: oScreenProps.getProcessedContentKPIDataMap(),
      activeKPIData : oScreenProps.getActiveKPIObject(),
    };
  }

  getMultiTaxonomyData = (oActiveEntity, oFilterContext) => {
    let oComponentProps =  this.getComponentProps();
    let oScreenProps = oComponentProps.screen;
    let oAllowedTaxonomyHierarchyList = oScreenProps.getAllowedTaxonomyHierarchyList();
    let aSelectedTaxonomyIds = oScreenProps.getActiveTaxonomyIds();

    if(oComponentProps.multiClassificationViewProps.getIsShowClassificationDialog()) {
      aSelectedTaxonomyIds = oScreenProps.getActiveClonedTaxonomyIds();
    }

    let oMultiTaxonomyDataList = this.getMultiTaxonomyList(aSelectedTaxonomyIds, oFilterContext);
    let oMultiTaxonomyData = {};
    oMultiTaxonomyData.selectedMultiTaxonomyList = oMultiTaxonomyDataList.selected;
    oMultiTaxonomyData.multiTaxonomyListToAdd = oMultiTaxonomyDataList.notSelected;
    oMultiTaxonomyData.paginationData = oScreenProps.getAllowedTaxonomiesPaginationData();
    oMultiTaxonomyData.allowedTaxonomyHierarchyList = oAllowedTaxonomyHierarchyList;
    return oMultiTaxonomyData;
  };

  getLifeCycleTagGroupModels = (oActiveEntity, oScreenProps) => {
    let oReferencedTags = oScreenProps.getReferencedTags();
    let oReferencedPermissions = oScreenProps.getReferencedPermissions();
    let bCanEditStatusTag = oReferencedPermissions.headerPermission && oReferencedPermissions.headerPermission.canEditStatusTag;
    let bIsContentDisabled = this.getIsContentDisabled();
    let bIsVariantSelected = !!oActiveEntity.variantInstanceId;
    let aSelectedLifeCycleData = oScreenProps.getReferencedLifeCycleTags();
    let aLifeCycleTagGroupModels = [];

    if(!bIsVariantSelected){
      CS.forEach(aSelectedLifeCycleData, function (sTagId){
        let oFoundReferencedTag = oReferencedTags[sTagId];
        let oElement = {
          isDisabled: bIsVariantSelected || !bCanEditStatusTag || bIsContentDisabled,
          isMultiSelect: false
        };
        /**Required to add dummy tag values into Lifecycle tag instances */
        let oTagInstance = CS.find(oActiveEntity.tags, {tagId: oFoundReferencedTag.id});
        CS.isNotEmpty(oTagInstance) && ContentUtils.addTagsInContentBasedOnTagType(oFoundReferencedTag, oTagInstance);

        let oTagModel = CommonUtils.getTagGroupModels(oFoundReferencedTag, oActiveEntity, oElement, "content");
        if(oTagModel) {
          aLifeCycleTagGroupModels.push(oTagModel);
        }
      });
    }

    return aLifeCycleTagGroupModels;
  }

  getActiveVariantTagDisplayData = () => {
    var oComponentProps =  this.getComponentProps();
    var oActiveEntity = this.getActiveEntity();
    var oVariantSectionProps = oComponentProps.variantSectionViewProps;
    var oActiveContext = oVariantSectionProps.getSelectedContext();
    var aTags = [];
    var aContentTags = oActiveEntity.tags;

    if((oActiveEntity.variantInstanceId || !CS.isEmpty(oActiveEntity.context)) && oActiveContext){
      if(!oActiveEntity.variantInstanceId){
        oActiveEntity.variantInstanceId = oActiveEntity.context.id;
      }
      var aContextTags = oActiveContext.tags;
      CS.forEach(aContextTags, function (oTag) {
        var oContentTag = CS.find(aContentTags, {tagId: oTag.id});
        if(oContentTag) {
          aTags.push(oContentTag);
        }
      });
    }

    return aTags;
  };

  prepareFileMappingViewData = () => {
    var oComponentProps =  this.getComponentProps();
    let oEndPointMappingViewProps = oComponentProps.endPointMappingViewProps;
    var bOnboardingFileMappingViewVisible = oComponentProps.screen.getOnboardingFileMappingViewVisibilityStatus();

    if(!bOnboardingFileMappingViewVisible) {
      return null;
    }

    var oActiveEndpoint = oEndPointMappingViewProps.getActiveEndpoint() || {};
    var oUnmappedColumnValuesList = oEndPointMappingViewProps.getUnmappedElementValuesList();
    var sActiveTabId = oEndPointMappingViewProps.getActiveTabId();
    let sSelectedMappingFilterId = oEndPointMappingViewProps.getSelectedMappingFilterId();
    let oMappingFilterProps = oEndPointMappingViewProps.getMappingFilterProps();
    let aEndPointMappingList = CS.concat(oActiveEndpoint.attributeMappings, oActiveEndpoint.tagMappings) || [];

    let aMockDataForKlassTypes = new MockDataForKlassTypes();
    let oEndPointReqResInfo = {
      classes: {
        requestType: "customType",
        responsePath: ["success", "list"],
        requestURL:  RequestMapping.getRequestUrl(getRequestMapping().GetKlassesListByBaseType),
        customRequestModel: {
          types: CS.map(aMockDataForKlassTypes, "value"),
        }
      },
      taxonomies: {
        requestType: "configData",
        entityName: "taxonomies",
        customRequestModel: {}
      },
      tagValues: {
        requestType: "configData",
        entityName: "tagValues",
      }
    };

    return {
      endPointMappingList: aEndPointMappingList,
      onBoardingFileMappingViewVisibilityStatus: bOnboardingFileMappingViewVisible,
      activeEndpoint: oActiveEndpoint,
      endPointReferencedData: oComponentProps.endPointMappingViewProps.getConfigData(),
      propertyRowTypeData: oComponentProps.endPointMappingViewProps.getPropertyRowTypeData(),
      endPointReqResInfo: oEndPointReqResInfo,
      unmappedColumnValuesList: oUnmappedColumnValuesList,
      selectedMappingFilterId : sSelectedMappingFilterId,
      mappingFilterProps : oMappingFilterProps,
      activeTabId: sActiveTabId,
    }
  };

  getEntityListAndSelectedEntityList = (sContext) => {
    var oComponentProps =  this.getComponentProps();
    var oAppData =  this.getAppData();
    var aSelectedContents = ContentUtils.getSelectedEntityList();
    var aMasterContentList = ContentUtils.getEntityList();

    if (sContext === "productVariantQuickList" ||
        sContext == "relationshipEntity" ||
        sContext == "staticCollection" ||
        sContext == "contextEntity" ||
        sContext == "manageVariantLinkedInstances" ||
        sContext == HierarchyTypesDictionary.COLLECTION_HIERARCHY ||
        sContext == ContentScreenViewContextConstants.TABLE_CONTEXT_AVAILABLE_ENTITY_VIEW ||
        sContext == HierarchyTypesDictionary.TAXONOMY_HIERARCHY ||
        CS.includes(CS.values(ContentScreenViewContextConstants.availableEntityViewContexts), sContext)) {
      aSelectedContents = oComponentProps.availableEntityViewProps.getSelectedEntities();
      aMasterContentList = oAppData.getAvailableEntities();
    }

    return {
      selectedEntityList: aSelectedContents,
      entityList: aMasterContentList,
    };
  };

  getDefaultSortData = () => {
    var oComponentProps = this.getComponentProps();
    var oAppData = this.getAppData();
    var aSortMenuList = oAppData.getSortMenuList();
    var aAttributeList = oAppData.getAttributeList();
    var sActiveSortingField = oComponentProps.screen.getActiveSortingField();
    var sActiveSortingOrder = oComponentProps.screen.getActiveSortingOrder();
    return {
      sortMenuList : aSortMenuList,
      attributeList : aAttributeList,
      activeSortingField : sActiveSortingField,
      activeSortingOrder : sActiveSortingOrder,
      isActive: oComponentProps.screen.getIsSortActive()
    }
  };

  getContentEditDataLanguageDropDownInfo =()=>{
    var oComponentProps = this.getComponentProps();
    var oActiveContent = this.getActiveEntity();
    let oScreenProps = oComponentProps.screen;
    let oReferencedLanguages = oScreenProps.getReferencedLanguages();
    let aLanguageTaxonomyIds = oActiveContent.languageCodes;
    let aReferencedDataLanguages = CS.reject(oReferencedLanguages, function (oLanguage) {
      return !CS.includes(aLanguageTaxonomyIds, oLanguage.code);
    });
    let sSelectedDataLanguageCode = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);
    let sActiveContentId = oActiveContent.id;
    let oActiveEntitySelectedTabIdMap = oScreenProps.getActiveEntitySelectedTabIdMap();
    let oActiveContentMap = oActiveEntitySelectedTabIdMap[sActiveContentId];
    let bLanguageComparisonModeOn = oActiveContentMap.languageComparisonModeOn;
    if (bLanguageComparisonModeOn) {
      let sHelperLanguageCode = oActiveContentMap.languageCodeToCompare;
      CS.remove(aReferencedDataLanguages, {code: sHelperLanguageCode});
    }

    let oRequestInfoData = {
      requestType: "customType",
      requestURL: RequestMapping.getRequestUrl(getRequestMapping(ContentScreenConstants.entityModes.ARTICLE_MODE).GetDataLanguage),
      customRequestModel: {
        id: oActiveContent.id
      },
      responsePath: ["success"]
    };

    let bIsUserReadOnly = ContentUtils.getIsCurrentUserReadOnly();
    let oSwitchContentDataLanguageDropdownData = {
      isPopoverVisible: oScreenProps.getIsDataLanguagePopoverVisible(),
      isDisabled: oScreenProps.getIsSwitchDataLanguageDisabled(),
      isDeleteTranslationButtonDisabled: bIsUserReadOnly
    };

    let oCreateTranslationDropdownData = {
      requestInfoData: oRequestInfoData,
      isPopoverVisible: oScreenProps.getIsCreateTranslationPopoverVisible(),
      isPopoverDisabled: ContentUtils.isActiveContentDirty(),
      isButtonDisabled: oScreenProps.getIsSwitchDataLanguageDisabled() || bIsUserReadOnly,
    };

    let oHelperLanguageDropdownData = {
      isSelected: bLanguageComparisonModeOn,
      isPopoverVisible: oScreenProps.getIsHelperLanguagePopoverVisible(),
      isDisabled: oScreenProps.getIsHelperLanguageDisabled(),
      requestInfoData: oRequestInfoData,
    };

    return {
      switchContentDataLanguageDropdownData: oSwitchContentDataLanguageDropdownData,
      createTranslationDropdownData: oCreateTranslationDropdownData,
      helperLanguageDropdownData: oHelperLanguageDropdownData,
      referencedLanguages: CS.keyBy(aReferencedDataLanguages, "id") ,
      selectedDataLanguageCode: sSelectedDataLanguageCode,
    }
  };

  getViewSpecificLanguageData = (oActiveContent) => {
    let oComponentProps = this.getComponentProps();
    let oScreenProps = oComponentProps.screen;
    let sActiveContentId = oActiveContent.id;
    let oActiveEntitySelectedTabIdMap = oScreenProps.getActiveEntitySelectedTabIdMap();
    let oActiveContentMap = oActiveEntitySelectedTabIdMap[sActiveContentId];
    let bLanguageComparisonModeOn = oActiveContentMap.languageComparisonModeOn && !oScreenProps.getIsHelperLanguageDisabled();
    let aHelperLanguageInstances = oScreenProps.getHelperLanguageInstances();
    let aLanguagesInfo = SessionProps.getLanguageInfoData();
    let aDataLanguages = aLanguagesInfo.dataLanguages;
    let sSelectedDataLanguageCode = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);

    let aHelperLanguages = [];
    aHelperLanguages.push(CS.find(aDataLanguages, {code: sSelectedDataLanguageCode}));
    CS.forEach(aHelperLanguageInstances, function (oInstance) {
      aHelperLanguages.push(CS.find(aDataLanguages, {code: oInstance.language}));
    });

    return {
      languageComparisonModeOn: bLanguageComparisonModeOn,
      helperLanguages: aHelperLanguages,
      helperLanguageInstances: aHelperLanguageInstances
    }
  }

  getArticleViewData = () => {
    var oComponentProps = this.getComponentProps();
    var sThumbnailType = ViewUtils.getThumbnailType();
    var oActiveContent = this.getActiveEntity();
    let oScreenProps = oComponentProps.screen;
    var aActiveSections = oComponentProps.screen.getActiveSections();

    let oImageAttribute = oActiveContent.assetInformation;
    let oCoverflowImageModels = {};
    if(oImageAttribute){
      oCoverflowImageModels = this.getImageCoverFlowModelList(oImageAttribute.attributeId, "contentHeader");
      oCoverflowImageModels.attributeId = oImageAttribute.attributeId;
    }

    var aSectionViewData = this.getSectionViewData('',aActiveSections,oActiveContent);
    var oSortByViewData = this.getDefaultSortData();

    var oConflictingValues = oScreenProps.getConflictingValues();
    let oConflictingSources = oScreenProps.getConflictingSources();
    let oAllPossibleConflictingSources = oScreenProps.getAllPossibleConflictingSources();
    var oReferencedClasses = oScreenProps.getReferencedClasses();
    var oReferencedTaxonomies = oScreenProps.getReferencedTaxonomies();
    var aReferencedCollections = oScreenProps.getReferencedCollections();
    var oReferencedRelationships = oScreenProps.getReferencedRelationshipProperties();
    var oReferencedContents = oScreenProps.getReferencedContents();
    var oReferencedTags = oScreenProps.getReferencedTags();
    let oReferencedContexts = oScreenProps.getReferencedVariantContexts();
    let oReferencedAttributes = oScreenProps.getReferencedAttributes();
    let {languageComparisonModeOn : bLanguageComparisonModeOn, helperLanguages:aHelperLanguages, helperLanguageInstances: aHelperLanguageInstances}= this.getViewSpecificLanguageData(oActiveContent);

    return {
      sectionViewModel: aSectionViewData,
      conflictingValues: oConflictingValues,
      conflictingSources: oConflictingSources,
      allPossibleConflictingSources: oAllPossibleConflictingSources,
      referencedClasses: oReferencedClasses,
      referencedTaxonomies: oReferencedTaxonomies,
      referencedCollections: aReferencedCollections,
      referencedRelationships: oReferencedRelationships,
      referencedContents: oReferencedContents,
      referencedTags: oReferencedTags,
      referencedAttributes: oReferencedAttributes,
      referencedContexts: oReferencedContexts,
      structureViewModel: {},
      childrenToolbarItemList: this.removeIndicatorFromGroupList(HorizontalToolbarItemList.ArticleChildrenSection),
      sortByViewData: oSortByViewData,
      thumbnailType: sThumbnailType,
      coverflowimagemodels: oCoverflowImageModels,
      isHelperLanguageSelected: bLanguageComparisonModeOn,
      helperLanguageInstances: aHelperLanguageInstances,
      helperLanguages: aHelperLanguages,
      sectionHeaderData : this.getSectionHeaderData(ContentUtils.getSelectedTabId()),
    };
  };

  isFromExcludedAttributeTypes = (sAttrType) => {
    return (
        ContentUtils.isImageCoverflowAttribute(sAttrType) ||
        ContentUtils.isAttributeTypeType(sAttrType) ||
        ContentUtils.isAttributeTypeFile(sAttrType) ||
        ContentUtils.isAttributeTypeSecondaryClasses(sAttrType) ||
        ContentUtils.isAttributeTypeTaxonomy(sAttrType)
    );
  };

  getAdvancedSearchPanelData = (oFilterContext) => {
    var _this = this;
    var oAppData = this.getAppData();
    var oComponentProps = this.getComponentProps();
    var oScreenProps = oComponentProps.screen;
    var oFilterProps = ContentUtils.getFilterProps(oFilterContext);

    var aAppliedFilterDataClone = oFilterProps.getAppliedFiltersClone();
    var aAppliedFilterData =  (aAppliedFilterDataClone != null) ? aAppliedFilterDataClone : oFilterProps.getAppliedFilters();

    //Attribute data
    var aMasterAttributeList = CS.cloneDeep(oAppData.getAttributeList());
    var aNonSelectedAttributeList = [];
    CS.forEach(aMasterAttributeList, function (oMasterAttribute) {
      var oFound = CS.find(aAppliedFilterData, {id: oMasterAttribute.id});
      if(CS.isEmpty(oFound) && !_this.isFromExcludedAttributeTypes(oMasterAttribute.type)){
        aNonSelectedAttributeList.push(oMasterAttribute);
      }
    });

    //Tag Data
    var aMasterTagList = CS.cloneDeep(oAppData.getTagList());
    var aNonSelectedTagList = [];
    CS.forEach(aMasterTagList, function (oMasterTag) {
      var oFound = CS.find(aAppliedFilterData, {id: oMasterTag.id});
      if(CS.isEmpty(oFound)){
        aNonSelectedTagList.push(oMasterTag);
      }
    });

    //Role Data
    var aMasterRolesList = CS.cloneDeep(oAppData.getRoleList());
    var aNonSelectedRolesList = [];
    CS.forEach(aMasterRolesList, function (oMasterRole) {
      var oFound = CS.find(aAppliedFilterData, {id: oMasterRole.id});
      if(CS.isEmpty(oFound)){
        aNonSelectedRolesList.push(oMasterRole);
      }
    });

    var oAppliedFilterCollapseStatusMap = oFilterProps.getAppliedFilterCollapseStatusMap();
    var sSearchInput = oScreenProps.getEntitySearchText();

    var oLoadedAttributes = oScreenProps.getLoadedAttributes();
    var oLoadedTags = oScreenProps.getLoadedTags();
    var oLoadedRoles = oScreenProps.getLoadedRoles();

    return {
      showAdvancedSearchPanel: oFilterProps.getAdvancedSearchPanelShowStatus(),
      searchInput: sSearchInput,
      loadedAttributes: oLoadedAttributes,
      loadedTags: oLoadedTags,
      loadedRoles: oLoadedRoles,
      attributeList: aNonSelectedAttributeList,
      tagList: aNonSelectedTagList,
      roleList: aNonSelectedRolesList,
      masterUserList: oAppData.getUserList(),
      isFilterDirty: oFilterProps.getIsFilterDirty(),
      appliedFilterCollapseStatusMap: oAppliedFilterCollapseStatusMap
    };
  };

  getAvailableFilterData = (oFilterContext = {}) => {
    let oFilterProps = ContentUtils.getFilterProps(oFilterContext);
    let oAvailableFilters = oFilterProps.getAvailableFilters();
    let aAvailableFilterData = [];

    /** Note: If Advanced Filters Is Applied Then No Need To Pass Available Filter Data **/
    if (!oFilterProps.getAdvancedFilterAppliedStatus()) {
      aAvailableFilterData = CS.sortBy(oAvailableFilters, [function (oAvailableFilterData) {
        return CS.getLabelOrCode(oAvailableFilterData).toLowerCase();
      }]);
    }

    let addInAvailableFilters = (aFilters) => {
      aFilters.forEach((oFilter) => {
        CS.isNotEmpty(oFilter) && (aAvailableFilterData.unshift(oFilter))
      });
    }

    /** Note: Below Filters can work independently with respect to advance filters **/
    addInAvailableFilters([
      oFilterProps.getFiltersForRecommendedAssets(),
      oFilterProps.getFiltersForNewProducts()
    ]);

    addInAvailableFilters(oFilterProps.getAdditionalFiltersForAsset());

    return aAvailableFilterData;
  }

  getFilterViewData = (oFilterContext) => {
    var oAppData = this.getAppData();
    var oComponentProps = this.getComponentProps();
    var oScreenProps = oComponentProps.screen;
    var oFilterProps = ContentUtils.getFilterProps(oFilterContext);
    var aAvailableSortData = oFilterProps.getAvailableSortData();
    var aAvailableFilterData = this.getAvailableFilterData(oFilterContext);
    let bIsAdvancedFilterApplied = oFilterProps.getAdvancedFilterAppliedStatus();
    var oAdvancedSearchPanelData = this.getAdvancedSearchPanelData(oFilterContext);
    var sParentTaxonomyId = oFilterProps.getSelectedOuterParentId();

    var oTaxonomySections = oFilterProps.getTaxonomyNodeSections();
    var aTaxonomySections = oTaxonomySections.clonedObject ? oTaxonomySections.clonedObject.sections : oTaxonomySections.sections;
    var bIsSelectTaxonomyPopOverVisible = oFilterProps.getIsSelectTaxonomyPopOverVisible();

    let oActiveCollection = oComponentProps.collectionViewProps.getActiveCollection();
    let bShowGroupByView = true;

    let sSelectedHierarchyContext = ContentUtils.getSelectedHierarchyContext();
    if(sSelectedHierarchyContext === HierarchyTypesDictionary.COLLECTION_HIERARCHY ||
        sSelectedHierarchyContext === HierarchyTypesDictionary.TAXONOMY_HIERARCHY ||
        !CS.isEmpty(oActiveCollection)) {
      bShowGroupByView = false;
    }

    let oActiveEntity = oScreenProps.getActiveContent();
    if(!CS.isEmpty(oActiveCollection) && CS.isEmpty(oActiveEntity)) {
      sSelectedHierarchyContext = "staticCollection";
    }


    let aAppliedTaxonomyIds = [];
    let aAppliedClassIds = [];
    let aReferencedTaxonomies = {};
    let aReferencedClasses = {};
    let aSelectedClassOptions = [];
    let aSelectedTaxonomyOptions = [];
    let bIsShowDetails = oFilterProps.getShowDetails();
    if (bIsShowDetails){
      aAppliedClassIds = oFilterProps.getSelectedTypes();
      aAppliedTaxonomyIds = oFilterProps.getSelectedTaxonomyIds();
    }
    let oReferencedTaxonomies = oFilterProps.getReferencedTaxonomies();
    CS.forEach(aAppliedTaxonomyIds, (sId) => {
      aSelectedTaxonomyOptions.push({
        id: sId
      });
      let oElement = oReferencedTaxonomies[sId];
      if (CS.isEmpty(oElement.toolTip)) {
        oElement.toolTip = ContentUtils.getTaxonomyPath(oElement.label, oElement.parentId, oReferencedTaxonomies);
      }
      oElement.customIconClassName = "noImage";
      aReferencedTaxonomies[sId] = oElement;
    });

    let oReferencedClasses = oFilterProps.getReferencedClasses();
    CS.forEach(aAppliedClassIds, (sId) => {
      aSelectedClassOptions.push({
        id: sId
      });
      let oElement = oReferencedClasses[sId];
      oElement.toolTip = CS.getLabelOrCode(oElement);
      oElement.customIconClassName = "noImage";
      aReferencedClasses[sId] = oElement;
    });

    return {
      categoryCount: {},
      taxonomyTree: oFilterProps.getTaxonomyTree(),
      taxonomyVisualProps: oFilterProps.getTaxonomyVisualProps(),
      availableSortData: aAvailableSortData,
      availableFilterData: aAvailableFilterData,
      appliedFilterData: oFilterProps.getAppliedFilters(),
      appliedFilterDataClone: oFilterProps.getAppliedFiltersClone(),
      showDetails: oFilterProps.getShowDetails(),
      searchText: oFilterProps.getSearchText(),
      activeSortDetails: oFilterProps.getActiveSortDetails(),
      taxonomyTreeSearchText: oFilterProps.getTaxonomySearchText(),
      matchingTaxonomyIds: oFilterProps.getMatchingTaxonomyIds(),
      advancedSearchPanelData: oAdvancedSearchPanelData,
      masterAttributeList: oAppData.getAttributeList(),
      isAdvancedFilterApplied: bIsAdvancedFilterApplied,
      isFilterAndSearchViewDisabled: oScreenProps.getFilterAndSearchViewDisabledStatus(),
      parentTaxonomyId: sParentTaxonomyId,
      affectedTreeNodeIds: oFilterProps.getAllAffectedTreeNodeIds(),
      taxonomySettingIconClickedNode: oFilterProps.getTaxonomySettingIconClickedNode(),
      taxonomyNodeSections: aTaxonomySections || [],
      isSelectTaxonomyPopOverVisible: bIsSelectTaxonomyPopOverVisible,
      selectedHierarchyContext: sSelectedHierarchyContext,
      selectedFilterHierarchyFilterGroups: oScreenProps.getFilterHierarchySelectedFilters(),
      availableEntityViewStatus: ContentUtils.getAvailableEntityViewStatus(),
      showFilter: true,
      bIsChooseTaxonomyVisible: oScreenProps.getIsChooseTaxonomyVisible(),
      filterContext: oFilterContext,
      showGroupBy: bShowGroupByView,
      appliedTaxonomies: aReferencedTaxonomies,
      appliedClasses: aReferencedClasses,
      selectedClassOptions: aSelectedClassOptions,
      selectedTaxonomyOptions: aSelectedTaxonomyOptions,
    }
  };

  getUpperSectionToolbarData = () => {
    let oComponentProps = this.getComponentProps();
    let oContent = ContentUtils.getActiveContent();
    let oActiveCollection = oComponentProps.collectionViewProps.getActiveCollection();
    let bIsTaxonomyHierarchy = ContentUtils.getSelectedHierarchyContext() === HierarchyTypesDictionary.TAXONOMY_HIERARCHY;
    if(!CS.isEmpty(oContent)) {
      return this.getContentEditToolbarData();
    } else if (!CS.isEmpty(oActiveCollection)) {
      return this.getCollectionToolbarData();
    } else if (bIsTaxonomyHierarchy) {
      return this.getTaxonomySectionToolbarData();
    }
   return null;
  };

  getCollectionToolbarData = () => {
    //TODO: getCollectionDirectly
    let bIsUserReadOnly = ContentUtils.getIsCurrentUserReadOnly();
    let oCollectionData = this.getCollectionData();
    let oActiveCollection = oCollectionData.activeCollection;
    let bContentEditToolbarViewStatus = !!oActiveCollection.isDirty;
    let aMoreButtons = [];
    let aTextButtons = [];
    let aExtraButtons = [];
    let sToolTipLabel = oActiveCollection.type === 'staticCollection' ? getTranslation().EDIT_COLLECTION :getTranslation().EDIT_BOOKMARKS;
    if (bContentEditToolbarViewStatus && bIsUserReadOnly) {
      aTextButtons.push({id: 'discardCollection', label: "Discard Changes"});
    } else if (bContentEditToolbarViewStatus) {
      aMoreButtons.push(new ContextMenuViewModel("discardCollection", "Discard Changes", false, "", {}));
      aTextButtons.push({id: 'saveCollection', label: "Save"});
    }
    if (CS.isEmpty(aTextButtons)) {
      aMoreButtons = [];
      aTextButtons = [];
    }

    let oComponentProps = this.getComponentProps();
    var bAddEntityInCollectionViewStatus = oComponentProps.collectionViewProps.getAddEntityInCollectionViewStatus();
    if (!bAddEntityInCollectionViewStatus && !bIsUserReadOnly) {
      aExtraButtons.push({id: "editCollection", tooltip: sToolTipLabel, className: "toolEditCollection"});
    }

    return {
      moreButtons: aMoreButtons,
      textButtons: aTextButtons,
      extraButtons: aExtraButtons
    };
  };

  getTaxonomySectionToolbarData = () => {
    let aMoreButtons = [];
    let aTextButtons = [];

    let oFilterContext = {
      filterType: oFilterPropType.HIERARCHY,
      screenContext: HierarchyTypesDictionary.TAXONOMY_HIERARCHY
    };

    let oFilterProps = ContentUtils.getFilterProps(oFilterContext);
    let oTaxonomySections = oFilterProps.getTaxonomyNodeSections();
    if (oTaxonomySections && oTaxonomySections.clonedObject) {
      aMoreButtons.push(new ContextMenuViewModel("discard", "Discard Changes", false, "", {customIconClassName: "discard"}));
      aTextButtons.push({id: 'save', label: "Save"});

      return {
        moreButtons: aMoreButtons,
        textButtons: aTextButtons,
        extraButtons: []
      };
    } else {
      return null;
    }
  };

  getCompleteButtonForTask = () => {
    let oComponentProps = this.getComponentProps();
    let oTaskProps = oComponentProps.taskProps;
    let oActiveTask = oTaskProps.getActiveTask();
    let aCheckedTaskList = oTaskProps.getSelectedTaskIds();
    let oCurrentUser = CommonUtils.getCurrentUser();
    let oData = oTaskProps.getTaskData();
    let aTaskList = oData.taskInstanceList;
    let aListOfDoneTasks = [];
    let bCurrentRoleIsSignOff = (CS.isNotEmpty(oActiveTask) && CS.isNotEmpty(oActiveTask.signoff)) ? oActiveTask.signoff.roleIds.includes(oCurrentUser.roleId) : false;
    let bCurrentUserIsSignOff = (CS.isNotEmpty(oActiveTask) && CS.isNotEmpty(oActiveTask.signoff)) ? oActiveTask.signoff.userIds.includes(oCurrentUser.id) : false;
    CS.forEach(aTaskList, (oTask) => {
      if (oTask.isCamundaCreated && (oTask.tagForList && oTask.tagForList.id != "taskdone" && aCheckedTaskList.includes(oTask.id))) {
        aListOfDoneTasks.push(oTask.id);
      }
    });

    if ((!CS.isEmpty(aListOfDoneTasks) && !CS.isEmpty(aCheckedTaskList)) ||
        (
            oActiveTask && oActiveTask.isCamundaCreated &&
            (oActiveTask.status && oActiveTask.status.tagValues[0] && oActiveTask.status.tagValues[0].tagId == "tasksignedoff")
        ) && (bCurrentRoleIsSignOff || bCurrentUserIsSignOff)
    ) {
      return {
        id: 'complete',
        label: getTranslation().COMPLETE
      };
    } else {
      return null;
    }
  };

  getContentEditToolbarData = () => {
    let oComponentProps = this.getComponentProps();
    if (ContentUtils.getRelationshipViewStatus() || ContentUtils.getAvailableEntityViewStatus()) {
      return null;
    }

    var oDummyVariant = ContentUtils.getActiveContentOrVariant();
    let bIsRelationshipQuickListDirty = !!(oDummyVariant && oDummyVariant.contentClone);

    let aContentEditToolbarData = new ContentEditToolbarData();
    let oContent = ContentUtils.getActiveContent();
    let aMaterialButtons = [];
    let aMoreOptionsButtons = [];
    let aExtraButtons = [];
    let oContentSectionViewProps = oComponentProps.contentSectionViewProps;
    let bIsActiveContentDirty = oContent.hasOwnProperty('contentClone');
    let bIsViolatingMandatoryElement = !CS.isEmpty(oContentSectionViewProps.getViolatingMandatoryElements());

    let sSelectedTab = ContentUtils.getSelectedTabId();
    let TaskProps = oComponentProps.taskProps;

    let bIsTaskDirty = sSelectedTab === TemplateTabsDictionary.TASKS_TAB ? TaskProps.getIsTaskDirty() : false;
    let bIsTableContentDirty = ContentUtils.getIsTableContentDirty();
    let bIsKlassInstanceFlatPropertyUpdated = ContentUtils.getIsKlassInstanceFlatPropertyUpdated();
    let bIsContentEditable = !this.getIsContentDisabled();

    // hide more-options (create clone, refresh), acronyx and ContentEditFilterView buttons from quicklist

    // Handling condition for create context variant
    let oVariantSectionViewProps = oComponentProps.variantSectionViewProps;
    let bIsContextQuickListOpen = oVariantSectionViewProps.getAvailableEntityChildVariantViewVisibilityStatus();
    let oFunctionPermission = oComponentProps.screen.getFunctionalPermission();

    if(!oFunctionPermission.canClone || ContentUtils.getIsCurrentUserReadOnly()){
      aContentEditToolbarData = CS.reject(aContentEditToolbarData, {id: "createClone"});
    }
    // Handling condition for variant creatiom from uom (table)
    let bIsVariantQuickListOpen = oComponentProps.availableEntityViewProps.getAvailableEntityViewContext() === ContentScreenViewContextConstants.TABLE_CONTEXT_AVAILABLE_ENTITY_VIEW;
    let bIsAnyQuickListOpen = bIsContextQuickListOpen || bIsVariantQuickListOpen;

    if (oContent.baseType !== EntityBaseTypeDictionary['contentBaseType']
    && oContent.baseType !== EntityBaseTypeDictionary.marketBaseType
    && oContent.baseType !== EntityBaseTypeDictionary.textAssetBaseType) {
      aContentEditToolbarData = CS.reject(aContentEditToolbarData, {id: "createClone"});
    } else if(!CS.isEmpty(oContent.context)){
      aContentEditToolbarData = CS.reject(aContentEditToolbarData, {id: "createClone"});
    }
    if (bIsContextQuickListOpen || bIsVariantQuickListOpen) {
      aContentEditToolbarData = CS.reject(aContentEditToolbarData, {id: "createClone"});
      aContentEditToolbarData = CS.reject(aContentEditToolbarData, {id: "refresh"});
    }

    if(!bIsContentEditable  || bIsContextQuickListOpen) {
      aContentEditToolbarData = CS.reject(aContentEditToolbarData, {id: "deleteLanguage"});
    }

    if (bIsContextQuickListOpen || bIsVariantQuickListOpen) {
      aContentEditToolbarData = CS.reject(aContentEditToolbarData, {id: "createClone"});
      aContentEditToolbarData = CS.reject(aContentEditToolbarData, {id: "refresh"});
    }

    /**Disable Clone button visibility for Data Integration **/
    if (ContentUtils.getSelectedPhysicalCatalogId() === PhysicalCatalogDictionary.DATAINTEGRATION) {
      aContentEditToolbarData = CS.reject(aContentEditToolbarData, {id: "createClone"});
    }

    if (!bIsActiveContentDirty && !bIsTaskDirty && !bIsTableContentDirty
        && !bIsKlassInstanceFlatPropertyUpdated && !(bIsRelationshipQuickListDirty && bIsAnyQuickListOpen)) {
      aContentEditToolbarData = CS.reject(aContentEditToolbarData, function (oToolbarItem) {
        return (oToolbarItem.id === 'save' || oToolbarItem.id === 'saveAndPublish');
      });
      aContentEditToolbarData = CS.reject(aContentEditToolbarData, function (oToolbarItem) {
        return (oToolbarItem.id === 'saveWithWarning');
      });
      aContentEditToolbarData = CS.reject(aContentEditToolbarData, function (oToolbarItem) {
        return (oToolbarItem.id === 'discard');
      });
    } else {
      if(bIsViolatingMandatoryElement) {
        //if content is dirty & violating any mandatory element
        aContentEditToolbarData = CS.reject(aContentEditToolbarData, function (oToolbarItem) {
          return (oToolbarItem.id === 'save');
        });
      } else {
        //if content is dirty & not violating any mandatory element
        aContentEditToolbarData = CS.reject(aContentEditToolbarData, function (oToolbarItem) {
          return (oToolbarItem.id === 'saveWithWarning');
        });
      }
    }

    /** To hide "save And Publish" button if content is not dirty*/
    if ((!bIsActiveContentDirty && !bIsTableContentDirty) || bIsKlassInstanceFlatPropertyUpdated) {
      aContentEditToolbarData = CS.reject(aContentEditToolbarData, function (oToolbarItem) {
        return (oToolbarItem.id === 'saveAndPublish');
      });
    }
    /** To hide "save" button in content, if versionable attribute is changed*/
    else if (oComponentProps.screen.getIsVersionableAttributeValueChanged()) {
      aContentEditToolbarData = CS.reject(aContentEditToolbarData, function (oToolbarItem) {
        return (oToolbarItem.id === 'save');
      });
    }

    let bIsActiveClassGoldenRecord = oComponentProps.screen.getIsActiveClassGoldenRecord();
    if (!bIsActiveClassGoldenRecord) {
      aContentEditToolbarData = CS.reject(aContentEditToolbarData, (oToolbarItem) => {
        return (oToolbarItem.id === 'viewSources' || oToolbarItem.id === 'remergeSources');
      });
    }

    CS.forEach(aContentEditToolbarData, function (oContentEditToolbarData) {
      if (oContentEditToolbarData.id === "save" || oContentEditToolbarData.id === "saveWithWarning" ||
         oContentEditToolbarData.id === "saveAndPublish") {
        aMaterialButtons.push(oContentEditToolbarData);
      } else {
        aMoreOptionsButtons.push(new ContextMenuViewModel(oContentEditToolbarData.id, CS.getLabelOrCode(oContentEditToolbarData), false, "", {customIconClassName: oContentEditToolbarData.id}));
      }
    });

    if(sSelectedTab === TemplateTabsDictionary.TASKS_TAB) {
      let oCompleteButton = this.getCompleteButtonForTask();
      oCompleteButton && aMaterialButtons.push(oCompleteButton);
    }

    let aTabsToIgnore = [TemplateTabsDictionary.TASKS_TAB,
                         TemplateTabsDictionary.TIMELINE_TAB, TemplateTabsDictionary.OVERVIEW_TAB,
                         TemplateTabsDictionary.RENDITION_TAB, TemplateTabsDictionary.DUPLICATE_TAB];
    let sActiveTabId = ContentUtils.getSelectedTabId();
    let bIsDisabled = CS.includes(aTabsToIgnore, sActiveTabId);

    if(!bIsContextQuickListOpen){
      aExtraButtons.push(
        {
          id: "languageInfoButtons",
          className: "",
          extraData: this.getContentEditDataLanguageDropDownInfo()
        }
      );
    }

    if (!bIsAnyQuickListOpen) {
      aExtraButtons.push(
          {
            id: "ContentEditFilterView",
            className: "filterViewWrapper",
            items: new FilterItemsList(),
            selectedItems: ContentUtils.getActiveEntitySelectedFilterId(),
            isDisabled: bIsDisabled
          }
      );
      aExtraButtons.push(
          {
            id: "acrolinxToggleButtonView",
            className: "acrolinxToggleButton",
            isVisible: oComponentProps.screen.getIsAcrolinxSidebarVisible()
          }
      );
    }
    return {
      textButtons: aMaterialButtons,
      moreButtons: aMoreOptionsButtons,
      extraButtons: aExtraButtons
    };
  };

  /**
   *
   * @param aLeftToolbarItemList
   * @param aRightToolbarItemList
   * @description Gives toolbar Items layout data
   * @returns {{leftItemList: Array, rightItemList: Array}}
   */
  getToolbarItemLayoutData = (aLeftToolbarItemList, aRightToolbarItemList) => {
    let oContentToolbarItemLayoutData = new ContentToolbarItemLayoutData();
    let oContentToolbarItemData = oContentToolbarItemLayoutData.toolbarItems;
    let aLeftData = [];
    let aRightData = [];

    CS.forEach(aLeftToolbarItemList, function (sItem) {
      aLeftData.push(oContentToolbarItemData[sItem]);
    });
    CS.forEach(aRightToolbarItemList, function (sItem) {
      aRightData.push(oContentToolbarItemData[sItem]);
    });

    return {
      leftItemList: aLeftData,
      rightItemList: aRightData
    };
  };

  getTimelineComparisonDialogToolbarData = (oComparisonLayoutData) => {
    let oComponentProps = this.getComponentProps();
    let TimeLineProps = oComponentProps.timelineProps;
    let oTimelineComparisonDialogToolbarData = ContentToolbarItemList.TimelineComparisonDialogToolbarData;
    let aRightItemToolbarList = oTimelineComparisonDialogToolbarData.rightItemList();
    let aToolbarRejectList = [];

    if(!oComparisonLayoutData.showPropertiesTab) {
      aToolbarRejectList.push("timeline_comparison_properties");
    }
    if(!oComparisonLayoutData.showRelationshipsTab) {
      aToolbarRejectList.push("timeline_comparison_relationship");
    }

    CS.remove(aRightItemToolbarList, function (sItem) {
      return CS.includes(aToolbarRejectList, sItem);
    });

    let oTimelineComparisonDialogToolbarLayoutData = this.getToolbarItemLayoutData([], aRightItemToolbarList);
    let aRightItemList =  oTimelineComparisonDialogToolbarLayoutData.rightItemList;
    let sSelectedButtonId = TimeLineProps.getSelectedHeaderButtonId();
    CS.forEach(aRightItemList, function(oButtonData) {
        oButtonData.isActive = (oButtonData.id === sSelectedButtonId);
    });

    let oComparisonDialogToolbarData = {
      comparisonViewTypes: aRightItemList
    };

    return oComparisonDialogToolbarData;
  };

  /**
   * @description Process Content open view timeline tab toolbar data
   */
  getTimeLineToolbarData = (oComparisonLayoutData, bIsComparisonMode) => {
    let oComponentProps = this.getComponentProps();
    let TimeLineProps = oComponentProps.timelineProps;
    let oTimeLineToolbar = ContentToolbarItemList.TimeLineToolbarData;

    let aSelectedVersions = TimeLineProps.getSelectedVersionIds();
    let aAvailableVersions = TimeLineProps.getVersions();
    if(aAvailableVersions.length < 1) {
      /**If Available Versions Are Less Than One Then It Should Return Empty*/
      return {};
    }

    let aLeftToolbarItemList = oTimeLineToolbar.leftItemList();
    let aRightToolbarItemList = oTimeLineToolbar.rightItemList();
    let aToolbarRejectList = [];

    if (aSelectedVersions.length < 2) {
      aToolbarRejectList.push("compare_timeline_entities");
    }

    if (aSelectedVersions.length < 1) {
      aToolbarRejectList.push("compare_timeline_entities");
      aToolbarRejectList.push("archive_timeline_entities");
      aToolbarRejectList.push("restore_timeline_entities");
      aToolbarRejectList.push("delete_timeline_entities");
    }

    if(TimeLineProps.getIsArchiveVisible()) {
      aToolbarRejectList.push("compare_timeline_entities");
      aToolbarRejectList.push("archive_timeline_entities");
    } else {
      aToolbarRejectList.push("restore_timeline_entities");
      aToolbarRejectList.push("delete_timeline_entities");
    }

    if(this.getIsContentDisabled()) {
      aToolbarRejectList.push("archive_timeline_entities");
      aToolbarRejectList.push("restore_timeline_entities");
      aToolbarRejectList.push("delete_timeline_entities");
    }

    CS.remove(aLeftToolbarItemList, function (sItem) {
      return CS.includes(aToolbarRejectList, sItem);
    });

    CS.remove(aRightToolbarItemList, function (sItem) {
      return CS.includes(aToolbarRejectList, sItem);
    });

    let oToolbarLayoutData = this.getToolbarItemLayoutData(aLeftToolbarItemList, aRightToolbarItemList);
    let oToolbarItem = CS.find(oToolbarLayoutData.leftItemList, {id: 'selectAll_timeline_entities'});
    if (oToolbarItem && aSelectedVersions.length) {
      oToolbarItem.selectedCount = aSelectedVersions.length;
      oToolbarItem.className = aSelectedVersions.length === aAvailableVersions.length ? ' toolUnCheckAll' : " toolHalfChecked";
      oToolbarItem.label = "DESELECT_ALL";
    }

    let oTimelineComparisonDialogToolbarData = bIsComparisonMode ? this.getTimelineComparisonDialogToolbarData(oComparisonLayoutData) : {};

    return {
      timelineTabToolbarData: this.getCustomToolbarData(oToolbarLayoutData),
      timelineComparisonDialogToolbarData: oTimelineComparisonDialogToolbarData
  }
  };

  /**
   *
   * @param oElement: Relation section Data
   * @param aRelationshipContentElementList: Relationship entities list
   * @param aSelectedRelationshipList: Selected Relationship entities list
   * @description Processed Content Open view relationship sections toolbar data and return clubbed toolbar data
   */
  getRelationshipSectionToolbarData (oElement, aRelationshipContentElementList, aSelectedRelationshipList, oFilterContext) {
    let _this = this;
    let oComponentProps = this.getComponentProps();
    let oRelationshipViewMode = oComponentProps.screen.getRelationshipViewMode();
    let oRelationshipToolbarProps = oComponentProps.relationshipView.getRelationshipToolbarProps();

    let oRelationshipToolbar = ContentToolbarItemList.RelationshipToolbar;
    let aLeftToolbarItemList = oRelationshipToolbar.leftItemList();
    let aRightToolbarItemList = oRelationshipToolbar.rightItemList();
    let aToolbarItemRejectList = [];

    if (CS.isEmpty(aRelationshipContentElementList)) {
      aRightToolbarItemList = [];
    }
    if (CS.isEmpty(aSelectedRelationshipList)) {
      aToolbarItemRejectList.push("delete_relationship_entities");
    }

    let oActiveEntitySelectedTabIdMap = oComponentProps.screen.getActiveEntitySelectedTabIdMap();
    if (oActiveEntitySelectedTabIdMap[oElement.activeEntityId].selectedTabId === ContentScreenViewContextConstants.USAGE_TAB) {
      aToolbarItemRejectList.push("add_relationship_entities");
      aToolbarItemRejectList.push("selectAll_relationship_entities");
      aToolbarItemRejectList.push("delete_relationship_entities");
    }

    let sThumbnailMode = ThumbnailModeConstants.BASIC;
    if (oRelationshipToolbarProps[oElement.id].isXRayEnabled && oRelationshipViewMode[oElement.id] !== ContentScreenConstants.viewModes.LIST_MODE) {
      sThumbnailMode = ThumbnailModeConstants.XRAY;
      aToolbarItemRejectList.push('zoomin_relationship_entities');
      aToolbarItemRejectList.push('zoomout_relationship_entities');
    }

    if (!oElement.canDelete || oElement.isDisabled || CS.isEmpty(aRelationshipContentElementList)) {
      aToolbarItemRejectList.push("selectAll_relationship_entities");
      aToolbarItemRejectList.push("delete_relationship_entities");
    }

    if (oRelationshipViewMode[oElement.id] === ContentScreenConstants.viewModes.LIST_MODE) {
      aToolbarItemRejectList.push("view_relationship_variants");
    }

    if(oElement.isDisabled || !oElement.canAdd || (oElement.relationshipSide.cardinality === RelationshipConstants.ONE && aRelationshipContentElementList.length > 0)) {
      aToolbarItemRejectList.push("add_relationship_entities");
    }

    if (oElement.couplingType == CouplingConstant.DYNAMIC_COUPLED) {
      aToolbarItemRejectList.push("add_relationship_entities");
      aToolbarItemRejectList.push("selectAll_relationship_entities");
      aToolbarItemRejectList.push("upload_relationship_entities");
    }

    if (!oElement.showBulkAssetUploadInRelationship || ContentUtils.getIsCurrentUserReadOnly()) {
      aToolbarItemRejectList.push("upload_relationship_entities");
    }

    CS.remove(aLeftToolbarItemList, function (sItem) {
      return CS.includes(aToolbarItemRejectList, sItem);
    });

    CS.remove(aRightToolbarItemList, function (sItem) {
      return CS.includes(aToolbarItemRejectList, sItem);
    });

    let oToolbarLayoutData = this.getToolbarItemLayoutData(aLeftToolbarItemList, aRightToolbarItemList);
    let aLeftData = oToolbarLayoutData.leftItemList;
    let aRightData = oToolbarLayoutData.rightItemList;

    let oDeleteOption = CS.find(aLeftData, {id: "delete_relationship_entities"});
    if (oDeleteOption) {
      oDeleteOption.className = "toolRemove ";
      if (CS.isEmpty(oElement.selectedElementsOfRelationship)) {
        oDeleteOption.className += "hideVisibility ";
      }
    }

    let oToolbarItem = CS.find(aLeftData, {id: 'selectAll_relationship_entities'});
    if(oToolbarItem) {
      if (aSelectedRelationshipList.length) {
        oToolbarItem && (oToolbarItem.label = "DESELECT_ALL") &&
        (oToolbarItem.className = aRelationshipContentElementList.length === aSelectedRelationshipList.length ? "toolUnCheckAll" : "toolHalfChecked");
      } else {
        oToolbarItem && (oToolbarItem.label = "SELECT_ALL") && (oToolbarItem.className = "toolCheckAll");
      }
      oToolbarItem.selectedCount = aSelectedRelationshipList.length;
    }

    let oXRaySettingsData = _this.getXRaySettingsData(oElement.id);
    oXRaySettingsData.relationshipId = oElement.id;
    let oRelationshipVariantXRaybutton = CS.find(aRightData, {id: "view_relationship_variants"});
    if (!CS.isEmpty(oRelationshipVariantXRaybutton)) {
      let aGroupsData = _this.getStore().getXRayProperties();
      oRelationshipVariantXRaybutton.view = (
          <XRayButtonView key={oRelationshipVariantXRaybutton.id} thumbnailMode={sThumbnailMode}
                          viewMode={ViewUtils.getViewMode()} xRaySettingsData={oXRaySettingsData} groupData={aGroupsData} filterContext={oFilterContext}/>);
    }

    let oBulkUploadItem = CS.find(aLeftData, {id: "upload_relationship_entities"});
    if (CS.isNotEmpty(oBulkUploadItem)) {
      let sContext = oComponentProps.relationshipView.getContext();
      let bShowBulkUploadDialog = oComponentProps.relationshipView.getIsBulkUploadDialogOpen() && sContext === AssetUploadContextDictionary.RELATIONSHIP_SECTION;
      let aAllowedKlassList = oComponentProps.relationshipView.getAssetClassList();
      let oAssetExtensions = oComponentProps.screen.getAssetExtensions();

      oBulkUploadItem.view = (
          <UploadAssetDialogView assetClassList={aAllowedKlassList}
                                 assetExtensions={oAssetExtensions}
                                 relationshipSideId={oElement.id}
                                 context={AssetUploadContextDictionary.RELATIONSHIP_SECTION}
                                 filterContext={oFilterContext}
                                 showUploadButton={true}
                                 showBulkUploadDialog={bShowBulkUploadDialog}
                                 requestURL ={getRequestMapping().GetDefaultTypes}/>)
    }

    let oRelationshipToolbarWithGroups = {};
    CS.forEach(aLeftData, function (oListItem) {
      oRelationshipToolbarWithGroups[oListItem.id] = [oListItem];
    });
    let aItemsToRejectForXRay = ['zoomin_relationship_entities', 'zoomout_relationship_entities', 'tile_relationship_entities', 'list_relationship_entities'];
    let aItemsToRejectForViewTypes = ['zoomin_relationship_entities', 'zoomout_relationship_entities', 'view_relationship_variants'];
    let aItemsToRejectForZoomToolbarItemList = ['view_relationship_variants', 'tile_relationship_entities', 'list_relationship_entities'];

    let aItemsForXRay = CS.clone(aRightData);
    let aItemsForViewTypes = CS.clone(aRightData);
    let aItemsForZoomToolbarItemList = CS.clone(aRightData);

    aItemsForXRay = _this.getFilteredToolbarButtons(aItemsForXRay, aItemsToRejectForXRay);
    aItemsForViewTypes = _this.getFilteredToolbarButtons(aItemsForViewTypes, aItemsToRejectForViewTypes);
    aItemsForZoomToolbarItemList = _this.getFilteredToolbarButtons(aItemsForZoomToolbarItemList, aItemsToRejectForZoomToolbarItemList);

    oRelationshipToolbarWithGroups.viewTypesItems = aItemsForViewTypes;
    oRelationshipToolbarWithGroups.zoomToolbarItemsList = aItemsForZoomToolbarItemList;
    oRelationshipToolbarWithGroups.XRayView = aItemsForXRay;
    return oRelationshipToolbarWithGroups;
  };

  /**
   * @description Process hierarchies toolbar data and returns rejected item list
   * @returns {Array}
   */
  processToolbarDataForHierarchies = () => {
    let oComponentProps = this.getComponentProps();
    let oScreenProps = this.getComponentProps().screen;
    let aSelectedAvailableEntities = oComponentProps.availableEntityViewProps.getSelectedEntities();
    let aRightToolbarItemRejectList = [];

    let sSelectedOuterParentContentHierarchyTaxonomyId = oScreenProps.getSelectedOuterParentContentHierarchyTaxonomyId();
    let oCutOrCopyData = oScreenProps.getHierarchyEntitiesToCopyOrCutData();
    let sActiveHierarchyNodeId = oScreenProps.getActiveHierarchyNodeId();
    let sCutOrCopiedFromNodeId = !CS.isEmpty(oCutOrCopyData) ? oCutOrCopyData.hierarchyNodeId : "";
    if (CS.isEmpty(oCutOrCopyData) ||
        sActiveHierarchyNodeId === sCutOrCopiedFromNodeId ||
        sSelectedOuterParentContentHierarchyTaxonomyId === "-1") {
      aRightToolbarItemRejectList.push('paste_hierarchy');
    }

    if (!CS.isEmpty(aSelectedAvailableEntities) && sSelectedOuterParentContentHierarchyTaxonomyId === "-1") {
      aRightToolbarItemRejectList.push('cut_hierarchy');
    }

    if (sSelectedOuterParentContentHierarchyTaxonomyId === ContentUtils.getHierarchyDummyNodeId()) {
      aRightToolbarItemRejectList.push("cut_hierarchy", "paste_hierarchy",);
    }

    if (CS.isEmpty(aSelectedAvailableEntities)) {
      aRightToolbarItemRejectList.push('cut_hierarchy', 'copy_hierarchy');
    }

    if (oScreenProps.getViewMode() === ContentScreenConstants.viewModes.LIST_MODE) {
      aRightToolbarItemRejectList.push("toggleMode");
    }

    return aRightToolbarItemRejectList;
  };

  /**
   *
   * @param aSelectedEntitiesList
   * @param sSelectedModuleId
   * @description Process toolbar data and returns rejected item list of export and transfer button.
   * @returns {Array}
   */
  processToolbarDataForExportAndTransferButtonVisibility = (aSelectedEntitiesList, sSelectedModuleId) => {
    let aToolbarItemRejectList = [];
    let oCurrentUser =  GlobalStore.getCurrentUser();
    let sOrganizationId = oCurrentUser.organizationId;
    let bRejectExportBtn = false;
    let bRejectExportToExcel = false;
    let bRejectTransferToSupplierStaging = true;
    let oCurrentUserDetails = ViewUtils.getCurrentUser();
    let bIsAdmin = oCurrentUserDetails && oCurrentUserDetails.id === "admin";
    let sPhysicalCatalogId = SessionProps.getSessionPhysicalCatalogId();
    if (sPhysicalCatalogId === PhysicalCatalogDictionary.OFFBOARDING) {
      aToolbarItemRejectList.push('onboardingFileUpload');
      if (sSelectedModuleId !== ModuleDictionary.FILES && CS.isEmpty(aSelectedEntitiesList)) {
        bRejectExportToExcel = true;
      }
      bRejectExportBtn = true;
      if (bIsAdmin) {
        bRejectExportToExcel = true;
        bRejectTransferToSupplierStaging = false;
      }

    }
    else if (sPhysicalCatalogId === PhysicalCatalogDictionary.ONBOARDING) {
      if (sSelectedModuleId === ModuleDictionary.FILES) {
        bRejectExportBtn = true;
      }
      bRejectExportToExcel = true;
    }
    else {
      if (sSelectedModuleId !== ModuleDictionary.PIM) {
        bRejectExportToExcel = true;
      }
      if (bIsAdmin) {
        bRejectExportBtn = true;
      }
    }
    if (bRejectExportBtn || CS.isEmpty(aSelectedEntitiesList) || sSelectedModuleId === ModuleDictionary.ALL) {
      aToolbarItemRejectList.push('exportToMDM');
    }
    if (bRejectExportToExcel) {
      aToolbarItemRejectList.push('exportToExcel');
    }
    if (bRejectTransferToSupplierStaging) {
      aToolbarItemRejectList.push('transferToSupplierStaging');
    }
    return aToolbarItemRejectList;
  };
  /**
   *
   * @param oToolbarData: Toolbar items ids
   * @param aEntitiesList: Current screen entities list
   * @param aSelectedEntitiesList: Current screen selected entities list
   * @description Get toolbar items layout data and add new properties if required
   * @returns {{leftItemList: (*|Array), rightItemList: Array}}
   */
  modularizeToolbarData = (oToolbarData, aEntitiesList, aSelectedEntitiesList, oFilterContext, oActiveRelationship) => {
    let oComponentProps = this.getComponentProps();
    let oAppData = this.getAppData();
    let oScreenProps = oComponentProps.screen;
    let bIsAvailableEntityViewStatus = ContentUtils.getAvailableEntityViewStatus();
    let bIsHierarchyViewModeExceptFilterHierarchy = ContentUtils.getIsAnyHierarchySelectedExceptFilterHierarchy();
    let sThumbnailMode = (bIsAvailableEntityViewStatus || bIsHierarchyViewModeExceptFilterHierarchy) ? oScreenProps.getSectionInnerThumbnailMode() : oScreenProps.getThumbnailMode();
    let sPhysicalCatalogId = SessionProps.getSessionPhysicalCatalogId();
    let aLeftToolbarItems = oToolbarData.leftItemList;
    let aRightToolbarItems = oToolbarData.rightItemList;
    let oToolbarLayoutData = this.getToolbarItemLayoutData(aLeftToolbarItems, aRightToolbarItems);
    let aLeftData = oToolbarLayoutData.leftItemList;
    let aRightData = oToolbarLayoutData.rightItemList;

    /** For Tile List View ( at -1 call) ***/
    let oToolbarItem = CS.find(aLeftData, {id: 'selectAll'});
    if (oToolbarItem) {
      if (!CS.isEmpty(aEntitiesList) && oToolbarItem) {
        if (aSelectedEntitiesList.length) {
          oToolbarItem.label = "DESELECT_ALL";
          oToolbarItem.className = aEntitiesList.length === aSelectedEntitiesList.length ? "toolUnCheckAll" : "toolHalfChecked";
        } else {
          oToolbarItem.label = "SELECT_ALL";
          oToolbarItem.className = "toolCheckAll";
        }
        oToolbarItem.selectedCount = aSelectedEntitiesList.length;
      }
    }

    let oExportToMDM = CS.find(aLeftData, {id: 'exportToMDM'});
    if (oExportToMDM) {
      let oGlobalModulesData = GlobalStore.getGlobalModulesData();
      if (sPhysicalCatalogId === PhysicalCatalogDictionary.ONBOARDING) {
        if (oGlobalModulesData.forCentralStaging) {
          oExportToMDM.label = "ONBOARDING_CENTRAL_STAGING_EXPORT_FILE"
        }
        else {
          oExportToMDM.label = "ONBOARDING_EXPORT_FILE"
        }
      } else {
        oExportToMDM.label = "MDM_EXPORT_FILE"
      }
    }

    let oBulkUploadItem = CS.find(aLeftData, {id: "assetBulkUpload"});
    if (!CS.isEmpty(oBulkUploadItem)) {
      if (oActiveRelationship && oActiveRelationship.propertyId == "standardArticleAssetRelationship" &&
          oActiveRelationship.relationshipSide.klassId == "asset_asset" && oFilterContext.screenContext == "quickList") {
        // CHECK FOR ASSET SIDE

        let sContext = oComponentProps.relationshipView.getContext();
        let bShowBulkUploadDialog = oComponentProps.relationshipView.getIsBulkUploadDialogOpen() && sContext === AssetUploadContextDictionary.RELATIONSHIP_QUICKLIST;
        // sideId
        let aAllowedKlassList = oComponentProps.relationshipView.getAssetClassList();
        let oAssetExtensions = oScreenProps.getAssetExtensions();
        oBulkUploadItem.view = (
            <UploadAssetDialogView assetClassList={aAllowedKlassList}
                                   assetExtensions={oAssetExtensions}
                                   relationshipSideId={oActiveRelationship.id}
                                   context={AssetUploadContextDictionary.RELATIONSHIP_QUICKLIST}
                                   filterContext={oFilterContext}
                                   showUploadButton={true}
                                   showBulkUploadDialog={bShowBulkUploadDialog}
                                   requestURL={getRequestMapping().GetDefaultTypes}/>)
      } else {
        let aCollectionList = oAppData.getStaticCollectionList();
        let bShowCollectionDialog = oScreenProps.getBulkUploadCollectionView();
        let oActiveCollection = this.getActiveCollection() || {};
        let sActiveCollectionId = oActiveCollection.id || "";
        let oRequestResponseData = {
          responsePath: ["success"],
          requestType: "customType",
          requestURL: getRequestMapping().GetDefaultTypes,
          customRequestModel: {
            moduleId: ViewUtils.getSelectedModuleId()
          }
        };

        oBulkUploadItem.view = (
            <BulkUploadButtonView collectionList={aCollectionList}
                                  showCollectionDialog={bShowCollectionDialog}
                                  activeCollectionId={sActiveCollectionId}

                                  filterContext={oFilterContext}
                                  assetExtensions={oScreenProps.getAssetExtensions()}
                                  requestResponseData={oRequestResponseData}/>)
      }
    }

    let oBulkDownloadItem = CS.find(aLeftData, {id: "assetBulkDownload"});
    let oLinkSharingItem = CS.find(aLeftData, {id: "assetBulkShare"});
    let oBulkDownloadProps = oComponentProps.bulkDownloadAssetProps;
    let oBulkAssetLinkSharingProps = oComponentProps.bulkAssetLinkSharingProps;
    let bShowDownloadDialog = oBulkDownloadProps.getShowDownloadDialog();
    let bShowLinkSharingDialog = oBulkAssetLinkSharingProps.getShowAssetLinkSharingDialog();

    if (bShowLinkSharingDialog && !CS.isEmpty(oLinkSharingItem)) {
      let oAssetDownloadGridData = {
        gridViewSkeleton: oBulkAssetLinkSharingProps.getGridViewSkeleton(),
        gridViewVisualData: oBulkAssetLinkSharingProps.getGridViewVisualData(),
        gridViewData: oBulkAssetLinkSharingProps.getGridViewData(),
        totalNestedItems: oBulkAssetLinkSharingProps.getTotalNestedItems()
      };

      oLinkSharingItem.view = (
          <AssetLinkSharingView assetLinkSharingGridViewData={oAssetDownloadGridData}/>
      )
    }

    if (bShowDownloadDialog && CS.isNotEmpty(oBulkDownloadItem)) {
      let oAssetDownloadData = ContentUtils.getDownloadDialogData();
      oBulkDownloadItem.view = (
          <BulkDownloadDialogView isFolderByAsset={oAssetDownloadData.isFolderByAsset}
                                  toggleButtonData={oAssetDownloadData.toggleButtonData}
                                  downloadData={oAssetDownloadData.downloadData}
                                  fixedSectionData={oAssetDownloadData.fixedSectionData}
                                  downloadAsExtraData={oAssetDownloadData.downloadAsExtraData}
                                  buttonExtraData={oAssetDownloadData.buttonExtraData}
                                  invalidRowIds={oAssetDownloadData.invalidRowIds}/>
      );
    }

    //add x-ray vision button view to the item
    let oXRaySettingsData = this.getXRaySettingsData();
    let oXRayItem = CS.find(aRightData, {id: 'toggleMode'});
    if (!CS.isEmpty(oXRayItem)) {
      let aGroupsData = this.getStore().getXRayProperties();
      oXRayItem.view = (
          <XRayButtonView
              key={oXRayItem.id}
              thumbnailMode={sThumbnailMode}
              viewMode={ViewUtils.getViewMode()}
              xRaySettingsData={oXRaySettingsData}
              groupData={aGroupsData}
              filterContext={oFilterContext}
          />
      );
    }

    let sSelectedModuleId = ContentUtils.getSelectedModuleId();
    if (sSelectedModuleId === ModuleDictionary.FILES) {
      let oOnboardingFileUploadButton = CS.find(aLeftData, {id: 'onboardingFileUpload'});
      if(oOnboardingFileUploadButton) {
        oOnboardingFileUploadButton.view = (
            <OnboardingFileUploadButtonView key={"onboardingFileUploadButtonView"}
                                            context={"runtimeProductImport"}
                                            label={getTranslation().IMPORT}/>);
      }
    }

    aLeftData = this.modifyToolbarItemsForDataIntegration(aLeftData, oFilterContext);
    aLeftData = this.modifyToolbarItemsForSmartDocumentPreset(aLeftData);


    return {
      leftItemList: aLeftData,
      rightItemList: aRightData
    };
  };

  /**
   *
   * @param oFilterProps: To fetch the filter , taxonomies and search data
   * @param aSelectedEntitiesList: Current screen selected entities list
   * @description Enable and disable of export button for Collection
   * @returns {boolean}
   */
  processExportButtonVisibilityForCollectionScreen = (oFilterProps, aSelectedEntitiesList) => {
    let oActiveCollection = this.getActiveCollection() || {};
    let sSelectedParentId = oFilterProps.getSelectedOuterParentId();
    let sAllSearch = oFilterProps.getSearchText();
    let aAppliedFilters = oFilterProps.getAppliedFilters();
    let bShowExportButton = CS.isEmpty(aSelectedEntitiesList);
    let sRootNodeId = ContentUtils.getTreeRootNodeId();
    let oTaxonomyVisualProps = oFilterProps.getTaxonomyVisualProps();

    if (ContentUtils.getIsStaticCollectionScreen()) {
      if (CS.isNotEmpty(sAllSearch) || CS.isNotEmpty(sSelectedParentId) || CS.isNotEmpty(aAppliedFilters)
          || (CS.isEmpty(sSelectedParentId) && oTaxonomyVisualProps[sRootNodeId]
              && oTaxonomyVisualProps[sRootNodeId].isChecked == 1)) {
        bShowExportButton = false;
      }
    } else if (oActiveCollection.isDirty) {
      bShowExportButton = false;
    }

    return bShowExportButton;
  };

  /**
   * @function Remove toolbar items based on specific condition
   * @param oToolbarItems: Toolbar items ids
   * @param sContext: Eg: "relationshipEntity", "collectionHierarchy" etc. Context of current screen
   * @param bIsInnerHierarchyViewMode: Boolean value if view mode is inner hierarchy
   * @param aEntitiesList: Current screen entities list
   * @param aSelectedEntitiesList: Current screen selected entities list
   * @returns {{leftItemList: *, rightItemList: *}}
   */
  processToolbarData = (oToolbarItems, sContext, bIsInnerHierarchyViewMode, aEntitiesList, aSelectedEntitiesList, oFilterContext, oActiveRelationship) => {
    let oScreenProps = this.getComponentProps().screen;
    let bIsAvailableEntityViewStatus = ContentUtils.getAvailableEntityViewStatus();
    let bIsHierarchyViewModeExceptFilterHierarchy = ContentUtils.getIsAnyHierarchySelectedExceptFilterHierarchy();
    let sThumbnailMode = (bIsAvailableEntityViewStatus || bIsHierarchyViewModeExceptFilterHierarchy) ? oScreenProps.getSectionInnerThumbnailMode() : oScreenProps.getThumbnailMode();
    let sSelectedModuleId = ContentUtils.getSelectedModuleId();
    let sViewMode = ViewUtils.getViewMode();
    let aLeftToolbarItemList = oToolbarItems.leftItemList();
    let aRightToolbarItemList = oToolbarItems.rightItemList();
    let aToolbarItemRejectList = [];
    let oFilterProps = (CS.isNotEmpty(oFilterContext)) ? ContentUtils.getFilterProps(oFilterContext) : {};
    let bIsFilterTypePagination = CS.isNotEmpty(oFilterContext) ? oFilterContext.filterType == oFilterPropType.PAGINATION : false;
    let oFunctionalPermission = oScreenProps.getFunctionalPermission();

    if(ContentUtils.getIsCurrentUserReadOnly()) {
      aToolbarItemRejectList.push("delete", "cloneContent", "bulkEdit", "gridEditContents",
          "transferContents", "assetBulkUpload", "smartDocumentPresets",
          "restoreContents", "remove", "importContents");
    }

    if (!oFunctionalPermission.canTransfer) {
      aToolbarItemRejectList.push("transferContents");
    }
    if (!oFunctionalPermission.canExport) {
      aToolbarItemRejectList.push("exportContents");
    }

    if (!oFunctionalPermission.canImport) {
      aToolbarItemRejectList.push("onboardingFileUpload");
      aToolbarItemRejectList.push("importContents");
    }

    if (!oFunctionalPermission.canBulkEdit) {
      aToolbarItemRejectList.push('bulkEdit');
    }
    if (!oFunctionalPermission.canGridEdit) {
      aToolbarItemRejectList.push('gridEditContents');
    }
    if (!oFunctionalPermission.canShare) {
      aToolbarItemRejectList.push('assetBulkShare');
    }

    /**Need to change**/
    if (oActiveRelationship && oActiveRelationship.propertyId == "standardArticleAssetRelationship" && oActiveRelationship.relationshipSide.klassId == "asset_asset") {
      aLeftToolbarItemList.push("assetBulkUpload");
    }

    /**Based on thumbnail mode and view mode */
    if (sViewMode === ContentScreenConstants.viewModes.LIST_MODE) {
      aToolbarItemRejectList.push("toggleMode");
    }

    /** Reject zoomIn zoomOut when viewMode is TileMode and XRAY is selected **/
    if(sThumbnailMode === ContentScreenConstants.thumbnailModes.XRAY && (sViewMode === ContentScreenConstants.viewModes.TILE_MODE)){
      aToolbarItemRejectList.push("zoomin", "zoomout")
    }

    /**Based on Entity List */
    if (CS.isEmpty(aEntitiesList)) {
      CS.remove(aRightToolbarItemList, function(sItem) {
        return sItem !== "paste_hierarchy";
      });
    }

    /**Enable and disable of export button for Collection*/
    if (ContentUtils.isCollectionScreen() && !bIsFilterTypePagination && !this.processExportButtonVisibilityForCollectionScreen(oFilterProps, aSelectedEntitiesList)) {
      aToolbarItemRejectList.push('exportContents');
    }

    /**Enable clone icon if selected entities contains only articles in dynamic collection**/
    if(ContentUtils.getIsDynamicCollectionScreen()){
      let bShowCloneContent = oScreenProps.getFunctionalPermission().canClone;
      CS.forEach(aSelectedEntitiesList, function (oEntity) {
        if(!ViewUtils.isBaseTypeArticle(oEntity.baseType)) {
          bShowCloneContent = false;
          return false;
        }
      });
      !bShowCloneContent && aToolbarItemRejectList.push('cloneContent');
    }
    /**  In case of normal product view **/
    if(!oScreenProps.getFunctionalPermission().canClone){
      aToolbarItemRejectList.push("cloneContent");
    }

    if (aSelectedEntitiesList.length === 0) {
      aToolbarItemRejectList.push('bulkEdit', 'transferToSupplierStaging', 'restoreContents', 'gridEditContents', 'delete', 'assetBulkDownload', 'transferContents', 'remove', 'addAll', "cloneContent", 'assetBulkShare');
      if (sSelectedModuleId !== ModuleDictionary.PIM && sSelectedModuleId !== ModuleDictionary.MAM && sSelectedModuleId !== ModuleDictionary.DASHBOARD && sSelectedModuleId !== ModuleDictionary.SUPPLIER && sSelectedModuleId !== ModuleDictionary.TARGET && sSelectedModuleId !== ModuleDictionary.TEXT_ASSET) {
        aToolbarItemRejectList.push('exportContents');
      }
      if (sSelectedModuleId === ModuleDictionary.MAM) {
        aToolbarItemRejectList.push('assetBulkDownload');
        aToolbarItemRejectList.push('assetBulkShare');
      }
      if (sSelectedModuleId === ModuleDictionary.PIM) {
        aToolbarItemRejectList.push('smartDocumentPresets');
      }
    }
    if(sSelectedModuleId !== ModuleDictionary.PIM){
      aToolbarItemRejectList.push('smartDocumentPresets');
    }
    if (aSelectedEntitiesList.length < 2 || sSelectedModuleId === ModuleDictionary.ALL) {
      //do not show compare icon in 'allModule' screen.
      aToolbarItemRejectList.push('comparison');
    }
    if (sSelectedModuleId === ModuleDictionary.ALL) {
      //do not show compare icon in 'allModule' screen.
      aToolbarItemRejectList.push('gridEditContents');
    }
    if (aEntitiesList && aEntitiesList.length === 0) {
      aToolbarItemRejectList.push('selectAll');
      aToolbarItemRejectList.push('exportContents');
    }
    if (!sSelectedModuleId === ModuleDictionary.TEXT_ASSET
        || !sSelectedModuleId === ModuleDictionary.TARGET) {
      aToolbarItemRejectList.push('cloneContent');
    }

    /**In target module*/
    if(sSelectedModuleId == ModuleDictionary.TARGET && !ContentUtils.isCollectionScreen()){
      aToolbarItemRejectList.push('exportContents');
    }

    /**In supplier module*/
    let oSupplierEntity = CS.find(aEntitiesList, {baseType: EntityBaseTypeDictionary["supplierBaseType"]});
    let bIsStaticCollectionActive = ContentUtils.getIsStaticCollectionScreen();
    if (!CS.isEmpty(oSupplierEntity) && !bIsStaticCollectionActive) {
        aToolbarItemRejectList.push('delete');
    }

    /**In case of linked variant, if the allowed duplicate is off then user cannot select all contents*/
    let oRelationshipContextData = oScreenProps.getRelationshipContextData();
    let oRelationshipContext = oRelationshipContextData.context;
    if (!CS.isEmpty(oRelationshipContext) && oRelationshipContext.type === ContextTypeDictionary.PRODUCT_VARIANT && !oRelationshipContext.isDuplicateVariantAllowed) {
      aToolbarItemRejectList.push("selectAll");
    }

    /**Hierarchy mode*/
    if(ContentUtils.getSelectedHierarchyContext()) {
      if (bIsInnerHierarchyViewMode) {
        aToolbarItemRejectList = CS.concat(aToolbarItemRejectList, this.processToolbarDataForHierarchies());
      }
    }

    /**Export and Transfer button visibility in onboarding and offboarding*/
    aToolbarItemRejectList = CS.concat(aToolbarItemRejectList, this.processToolbarDataForExportAndTransferButtonVisibility(aSelectedEntitiesList, sSelectedModuleId));


    CS.remove(aLeftToolbarItemList, function (sItem) {
      return CS.includes(aToolbarItemRejectList, sItem);
    });

    CS.remove(aRightToolbarItemList, function (sItem) {
      return CS.includes(aToolbarItemRejectList, sItem);
    });

    return {
      leftItemList: aLeftToolbarItemList,
      rightItemList: aRightToolbarItemList
    };
  };

  /**
   *
   * @param bIsInnerHierarchyViewMode: Boolean value if view mode is inner hierarchy
   * @param sContext: Eg: "relationshipEntity", "collectionHierarchy" etc. Context of given screen
   * @description: Used to get toolbar data keys based on screen
   */
  getScreenBasedToolbarData= (bIsInnerHierarchyViewMode, sContext) => {
    let oComponentProps = this.getComponentProps();
    let oScreenProps = oComponentProps.screen;
    let bIsAvailableEntityViewStatus = ContentUtils.getAvailableEntityViewStatus();
    let sViewMode = ViewUtils.getViewMode();
    let sSelectedModuleId = ContentUtils.getSelectedModuleId();
    let oActiveCollectionObj = oComponentProps.collectionViewProps.getActiveCollection();
    let bIsArchive = ContentUtils.getIsArchive();
    let bShowGoldenRecordBuckets = oComponentProps.goldenRecordProps.getShowGoldenRecordBuckets();
    let bIsKpiContentExplorerOpen = oScreenProps.getIsKpiContentExplorerOpen();
    let bIsInformationTabRuleViolationTileClicked = ContentUtils.getIsRuleViolatedContentsScreen();
    let bIsDamInformationTabRuleViolationTileClicked = ContentUtils.getIsDamRuleViolatedContentsScreen();
    let bIsDuplicateAssetsTab = sContext == ContentScreenConstants.tabItems.TAB_DUPLICATE_ASSETS;

    if (ContentUtils.getSelectedHierarchyContext()) {
      return bIsInnerHierarchyViewMode ? ContentToolbarItemList.hierarchyToolbarItemList : {};
    }
    else if(bIsDuplicateAssetsTab){
      return ContentToolbarItemList.DuplicateAssetsTabMode;
    }
    else if (bIsAvailableEntityViewStatus) {
      return ContentToolbarItemList.AvailableEntityTileView;
    }
    else if (!CS.isEmpty(oActiveCollectionObj) && oActiveCollectionObj.type === "staticCollection") {
      return ContentToolbarItemList.StaticCollectionScreen;
    }
    else if (!CS.isEmpty(oActiveCollectionObj) && oActiveCollectionObj.type === "dynamicCollection") {
      return ContentToolbarItemList.DynamicCollectionScreen;
    }
    else if (bIsArchive) {
      return ContentToolbarItemList.PimArchivalToolbarItems;
    }
    else if (bShowGoldenRecordBuckets && (sViewMode === ContentScreenConstants.viewModes.TILE_MODE)) {
      return ContentToolbarItemList.GoldenRecordToolbarItems;
    }
    else if (bIsKpiContentExplorerOpen) {
      return ContentToolbarItemList.KPIExplorerScreen;
    }
    else if (bIsInformationTabRuleViolationTileClicked) {
      return ContentToolbarItemList.InformationTabRuleViolationScreen;
    }
    else if (bIsDamInformationTabRuleViolationTileClicked) {
      return ContentToolbarItemList.InformationTabRuleViolationScreen;
    }
    else if (sSelectedModuleId === ModuleDictionary.FILES) {
      return ContentToolbarItemList.FileModule;
    }
    else if (sSelectedModuleId === ModuleDictionary.PIM) {
      return ContentToolbarItemList.PimModule;
    }
    else if (sSelectedModuleId === ModuleDictionary.MAM) {
      return ContentToolbarItemList.DamModule;
    } else if (sSelectedModuleId === ModuleDictionary.TEXT_ASSET) {
      return ContentToolbarItemList.TextAssetData;
    }
    else if (sSelectedModuleId === ModuleDictionary.TARGET) {
      return ContentToolbarItemList.TargetData;
    }
    else {
      return ContentToolbarItemList.BasicToolbarItems;
    }
  };

  /**
   * @function Toolbar data base function
   * @param bIsLinkedVariant: Boolean value if it is linked variant
   * @param bIsInnerHierarchyViewMode: Boolean value if view mode is inner hierarchy
   * @param sContext: Eg: "relationshipEntity", "collectionHierarchy" etc. Context of given screen
   * @returns {*}Returns modified screen based Toolbar data
   */
  getToolbarData = (bIsInnerHierarchyViewMode, sContext, oFilterContext, oActiveRelationship) => {
    let oComponentProps = this.getComponentProps();
    let oAppData = this.getAppData();
    let aEntityList = oAppData.getContentList();
    let aAvailableEntities = oAppData.getAvailableEntities();
    let aSelectedEntityList = ContentUtils.getSelectedEntityList();
    let aSelectedAvailableEntities = oComponentProps.availableEntityViewProps.getSelectedEntities();
    let bIsAvailableEntityViewStatus = ContentUtils.getAvailableEntityViewStatus();
    let aViewedEntities = bIsAvailableEntityViewStatus ? aAvailableEntities : aEntityList;
    let aViewedSelectedEntitiesList = bIsAvailableEntityViewStatus ? aSelectedAvailableEntities : aSelectedEntityList;
    if (ContentUtils.getSelectedHierarchyContext()) {
      aViewedEntities = aAvailableEntities;
      aViewedSelectedEntitiesList = aSelectedAvailableEntities;
    }

    let oToolbarData = this.getScreenBasedToolbarData(bIsInnerHierarchyViewMode, sContext);
    if(!CS.isEmpty(oToolbarData)) {
      oToolbarData = this.processToolbarData(oToolbarData, sContext, bIsInnerHierarchyViewMode, aViewedEntities, aViewedSelectedEntitiesList, oFilterContext, oActiveRelationship);
      oToolbarData = this.modularizeToolbarData(oToolbarData, aViewedEntities, aViewedSelectedEntitiesList, oFilterContext, oActiveRelationship);
    }
    return oToolbarData;
  };

  getSelectedViewTypeItem (aViewTypeItems, sViewMode) {

    CS.forEach(aViewTypeItems, function (oViewTypeItem) {
      if ((sViewMode === ContentScreenConstants.viewModes.TILE_MODE && oViewTypeItem.className === "toolView1") ||
          (sViewMode === ContentScreenConstants.viewModes.LIST_MODE && oViewTypeItem.className === "toolView2") ||
          (sViewMode === ContentScreenConstants.viewModes.GRID_MODE && oViewTypeItem.className === "toolView3")
          ) {
        oViewTypeItem.isSelected = true;
      } else {
        oViewTypeItem.isSelected = false;
      }
    });
  }

  getCustomToolbarData (oToolbarData) {
    let oNewToolbarData = {};
    let aXRayToolItems = CS.clone(oToolbarData.rightItemList);
    let aZoomToolbarItems = CS.clone(oToolbarData.rightItemList);
    let aViewTypeItems = CS.clone(oToolbarData.rightItemList);
    let aCutCopyPasteLists = CS.clone(oToolbarData.rightItemList);
    let aToolbarItemsToRemoveForXRay = ["tile", "list", "zoomin", "zoomout", "copy_hierarchy", "cut_hierarchy",
                                        "paste_hierarchy", "zoomin_timeline_entities", "zoomout_timeline_entities"];

    let aToolbarItemsToRemoveForZoom = ["tile", "list", "toggleMode", "copy_hierarchy", "cut_hierarchy",
                                        "paste_hierarchy"];

    let aToolbarItemsToRemoveForViewType = ["zoomin", "zoomout", "toggleMode", "copy_hierarchy", "cut_hierarchy",
                                            "paste_hierarchy", "zoomin_timeline_entities", "zoomout_timeline_entities"];

    let aToolbarItemsToRemoveForCutCopyPaste = ["tile", "list", "toggleMode", "zoomin", "zoomout", "toggleMode",
                                                "zoomin_timeline_entities", "zoomout_timeline_entities"];

    aXRayToolItems = this.getFilteredToolbarButtons(aXRayToolItems, aToolbarItemsToRemoveForXRay);
    aZoomToolbarItems = this.getFilteredToolbarButtons(aZoomToolbarItems, aToolbarItemsToRemoveForZoom);

    aViewTypeItems = this.getFilteredToolbarButtons(aViewTypeItems, aToolbarItemsToRemoveForViewType);
    aCutCopyPasteLists = this.getFilteredToolbarButtons(aCutCopyPasteLists, aToolbarItemsToRemoveForCutCopyPaste);

    CS.forEach(oToolbarData.leftItemList, function (oLeftToolbarItems) {
      oNewToolbarData[oLeftToolbarItems.id] = [oLeftToolbarItems]
    });


    if(!CS.isEmpty(aViewTypeItems)){
      /**Below Function Is Used To Set Current View Mode Type In Toolbar View*/
      let sViewMode = ViewUtils.getViewMode();
      this.getSelectedViewTypeItem(aViewTypeItems, sViewMode);
    }

    oNewToolbarData.viewTypesItems = aViewTypeItems;
    oNewToolbarData.XRayView = aXRayToolItems;
    oNewToolbarData.zoomToolbarItemsList = aZoomToolbarItems;
    oNewToolbarData.cutCopyPasteList = aCutCopyPasteLists;
    return oNewToolbarData;
  };

  getFilteredToolbarButtons = (oButtonGroup, aButtonsToRemove) =>{
    CS.forEach(aButtonsToRemove, function (sButtonId, iButtonIndex) {
      oButtonGroup = CS.reject(oButtonGroup, {id: sButtonId})
    });
    return oButtonGroup;
  };

  getContextMenuModel = (aList) => {
    let aMasterListModels = [];
    CS.forEach(aList, function (oListItem) {
      aMasterListModels.push(new ContextMenuViewModel(
          oListItem.id,
          CS.getLabelOrCode(oListItem),
          false,
          "",
          {context: ""}
      ));
    });
    return aMasterListModels;
  };

  modifyToolbarItemsForDataIntegration = (aLeftToolbarItems, oFilterContext) => {
    var oDataIntegrationInfo = ContentUtils.getDataIntegrationInfo();
    let oScreenProps = this.getComponentProps().screen;
    let oCurrentUser =  GlobalStore.getCurrentUser();
    let aAllowedPhysicalCatalogIds = oCurrentUser.allowedPhysicalCatalogIds;//oScreenProps.getAllowedPhysicalCatalogs();
    let sOrganizationId = oCurrentUser.organizationId;
    let bIsPermissionGiven = false;
    let oContextMenuModelForTransferButton = {};
    let oContextMenuModelForExportButton = {};
    let aListForOffboardingExportButton = [];
    let sUseCaseForTransfer = 'showList';
    let sToolTipForTransfer = getTranslation().TRANSFER ;
    let sUseCaseForImportExport = "showDynamicList";
    let sToolTipForExport = getTranslation().EXPORT;
    let sTransferTo = getTranslation().TRANSFER + ' ' + getTranslation().TO + ' ';
    let aListForPIMTransferOptions = [
      {
        id: "offboarding",
        label: sTransferTo + ' ' + getTranslation()["OFFBOARDING"]
      },
      {
        id: "retailer",
        label: sTransferTo + getTranslation()["RETAILER"]
      }
    ];

    let aListForOnBoardingTransferOptions = [
      {
        id: "pim",
        label: sTransferTo + getTranslation()["PIM"]
      },
      {
        id: "retailer",
        label: sTransferTo + getTranslation()["RETAILER"]
      }
    ];

    let aListForTransferOptionsInOffBoarding = [
      {
        id: "retailer",
        label: sTransferTo + getTranslation()["RETAILER"]
      }
    ];

    if (sOrganizationId === "-1") {
      aListForPIMTransferOptions = CS.reject(aListForPIMTransferOptions, {id: 'retailer'});
      aListForOnBoardingTransferOptions = CS.reject(aListForOnBoardingTransferOptions, {id: 'retailer'});
    }

    let aListForDataIntegrationTransferOptions = [
      {
        id: "pim",
        label: sTransferTo + getTranslation()["PIM"]
      },
      {
        id: "onboarding",
        label: sTransferTo + getTranslation()["ONBOARDING"]
      }
    ];
    switch (oDataIntegrationInfo.physicalCatalogId) {
      case PhysicalCatalogDictionary.ONBOARDING:
        aLeftToolbarItems = CS.reject(aLeftToolbarItems, {id: 'exportContents'});
        bIsPermissionGiven = CS.includes(aAllowedPhysicalCatalogIds, PhysicalCatalogDictionary.PIM);
        if (!bIsPermissionGiven) {
          aLeftToolbarItems = CS.reject(aLeftToolbarItems, {id: 'transferContents'});
        }
        else {
          if (aListForOnBoardingTransferOptions.length < 2) {
            sUseCaseForTransfer = 'directClick';
            sToolTipForTransfer = CS.getLabelOrCode(aListForOnBoardingTransferOptions[0]);
          }
          oContextMenuModelForTransferButton = this.getContextMenuModel(aListForOnBoardingTransferOptions);
        }
        break;

      case PhysicalCatalogDictionary.PIM:
        bIsPermissionGiven = CS.includes(aAllowedPhysicalCatalogIds, PhysicalCatalogDictionary.OFFBOARDING);
          if (aListForPIMTransferOptions.length < 2) {
            sUseCaseForTransfer = 'directClick';
            sToolTipForTransfer = CS.getLabelOrCode(aListForPIMTransferOptions[0]);
          }
          oContextMenuModelForTransferButton = this.getContextMenuModel(aListForPIMTransferOptions);
        aListForOffboardingExportButton = oScreenProps.getEndpointsListAccordingToUser();
        oContextMenuModelForExportButton = this.getContextMenuModel(aListForOffboardingExportButton);
        break;

      case PhysicalCatalogDictionary.OFFBOARDING:
        if (aListForTransferOptionsInOffBoarding.length < 2) {
          sUseCaseForTransfer = 'directClick';
          sToolTipForTransfer = CS.getLabelOrCode(aListForTransferOptionsInOffBoarding[0]);
        }
        oContextMenuModelForTransferButton = this.getContextMenuModel(aListForTransferOptionsInOffBoarding);
        aListForOffboardingExportButton = oScreenProps.getEndpointsListAccordingToUser();
        oContextMenuModelForExportButton = this.getContextMenuModel(aListForOffboardingExportButton);
        break;

      case PhysicalCatalogDictionary.DATAINTEGRATION:
        let bIsPimPermissionGiven = CS.includes(aAllowedPhysicalCatalogIds,PhysicalCatalogDictionary.PIM);
        let sEndpointId = oDataIntegrationInfo.endPoint;
        let oDataForEndPoint = {
          id: sEndpointId
        };
        oContextMenuModelForExportButton = this.getContextMenuModel(oDataForEndPoint);
        aLeftToolbarItems = CS.reject(aLeftToolbarItems, {id: 'cloneContent'});
        if(!bIsPimPermissionGiven){
          aListForDataIntegrationTransferOptions = CS.reject(aListForDataIntegrationTransferOptions, {id: 'pim'})
        }
        if(oDataIntegrationInfo.endpointType === "onboardingendpoint"){
          aLeftToolbarItems = CS.reject(aLeftToolbarItems, {id: 'exportContents'});
        }
        if(oDataIntegrationInfo.endpointType === "offboardingendpoint"){
          aLeftToolbarItems = CS.reject(aLeftToolbarItems, {id: 'importContents'});
          aLeftToolbarItems = CS.reject(aLeftToolbarItems, {id: 'onboardingFileUpload'});
          }
        if(aListForDataIntegrationTransferOptions.length && (aListForDataIntegrationTransferOptions.length < 2)){
          sUseCaseForTransfer = 'directClick';
          sToolTipForTransfer = CS.getLabelOrCode(aListForDataIntegrationTransferOptions[0]);
        }
        oContextMenuModelForTransferButton = this.getContextMenuModel(aListForDataIntegrationTransferOptions);
        break;
    }

    if (!CommonUtils.isDataIntegrationAllowedForCurrentUser()) {
      aLeftToolbarItems = CS.reject(aLeftToolbarItems, {id: "exportContents"});
    }

    var oExportContents = CS.find(aLeftToolbarItems, {id: 'exportContents'});

    if (!CS.isEmpty(oExportContents)) {
      oExportContents.className = "exportContentsButton";
      oExportContents.tooltip = sToolTipForExport;

      if(oDataIntegrationInfo.physicalCatalogId !== PhysicalCatalogDictionary.DATAINTEGRATION){
        let aResponsePath = ["success", "endpoints", "list"];
        let sRequestURL = RequestMapping.getRequestUrl("config/permitted/endpoint");
        let oRequestResponseInfoData = {
          requestType: "customImpExp",
          entityName: "endpoints",
          responsePath: aResponsePath,
          requestURL: sRequestURL,
          types: ["offboardingendpoint"],
          customRequestModel: {
            physicalCatalogId: oDataIntegrationInfo.physicalCatalogId,
            organizationId: oDataIntegrationInfo.organizationId,
            actionType: "export"
          }
        };

        let oContextMenuViewData = {
          isMultiSelect: false,
          showPopover: oScreenProps.getExportContentsPopoverVisibility(),
          showSearch: false,
          showArrowHead: true,
          contextMenuViewModel: oContextMenuModelForExportButton,
          requestResponseInfoData: oRequestResponseInfoData
        };
        oExportContents.view = (<ButtonWithContextMenuView
            key={"exportContentsButton"}
            context={"exportContents"}
            className={"exportContentsButton"}
            tooltip={sToolTipForExport}
            useCase={sUseCaseForImportExport}
            contextMenuData={oContextMenuViewData}
            filterContext={oFilterContext}
            isClickedNodeItemReturn={true}
        />);
      }

    };

    let oImportContents = CS.find(aLeftToolbarItems, {id: 'importContents'});
    if (!CS.isEmpty(oImportContents)) {
      oImportContents.className = "toolImportContents";

      if(oDataIntegrationInfo.physicalCatalogId !== PhysicalCatalogDictionary.DATAINTEGRATION){
        let bImportEndpointSelected = oScreenProps.getImportEndpointSelected();
        oImportContents.tooltip = getTranslation().IMPORT;
        let aResponsePath = ["success", "endpoints", "list"];
        let sRequestURL = RequestMapping.getRequestUrl("config/permitted/endpoint");
        let oRequestResponseInfoData = {
          requestType: "customImpExp",
          entityName: "endpoints",
          responsePath: aResponsePath,
          requestURL: sRequestURL,
          types: ["onboardingendpoint"],
          customRequestModel: {
            physicalCatalogId: oDataIntegrationInfo.physicalCatalogId,
            organizationId: oDataIntegrationInfo.organizationId,
            actionType: "import"
          }
        };

        let oContextMenuViewData = {
          isMultiSelect: false,
          showPopover: oScreenProps.getImportContentsPopoverVisibility(),
          showSearch: false,
          showArrowHead: true,
          contextMenuViewModel: oContextMenuModelForExportButton,
          requestResponseInfoData: oRequestResponseInfoData
        };
        oImportContents.view = (<ButtonWithContextMenuView
            key={"importContentsButton"}
            context={"importContents"}
            className={"toolImportContents"}
            tooltip={getTranslation().IMPORT}
            useCase={sUseCaseForImportExport}
            contextMenuData={oContextMenuViewData}
            filterContext={oFilterContext}
            isClickedNodeItemReturn={true}
        >
          <OnboardingFileUploadButtonView context={"runtimeProductImport"} endpointSelected={bImportEndpointSelected} dropdown={true}/>
        </ButtonWithContextMenuView>);
      }
      else{
        oImportContents.view =
            (<OnboardingFileUploadButtonView className={"toolImportContents"} context={"runtimeProductImport"}
                                             label={getTranslation().IMPORT}/>
            );
      }
    }

    var oTransferContents = CS.find(aLeftToolbarItems, {id: 'transferContents'});
    let oContextMenuViewData = null;
    let oCustomObject = null;
    if (!CS.isEmpty(oTransferContents)) {
      if ((oDataIntegrationInfo.physicalCatalogId == PhysicalCatalogDictionary.PIM ||
          oDataIntegrationInfo.physicalCatalogId == PhysicalCatalogDictionary.DATAINTEGRATION ||
          oDataIntegrationInfo.physicalCatalogId == PhysicalCatalogDictionary.ONBOARDING ||
          oDataIntegrationInfo.physicalCatalogId == PhysicalCatalogDictionary.OFFBOARDING)) {
        sUseCaseForTransfer = "showDynamicList";
        sToolTipForTransfer = getTranslation().TRANSFER ;
        let oRequestResponseInfoData = {
          requestType: "customType",
          requestURL: "config/organizations/getall",
          responsePath: ["success", "list"]
        };
        oContextMenuViewData = {
          isMultiSelect: false,
          showPopover: oScreenProps.getExportDialogViewOpened(),
          showSearch: false,
          showArrowHead: true,
          requestResponseInfoData: oRequestResponseInfoData,
          organizationList : oScreenProps.getOrganizationListForExportDialog(),
          authorizationMappingList : oScreenProps.getAuthorizationMappingListForExportDialog(),
          inboundEndpointList: oScreenProps.getInboundEndpointListForExportDialog(),
          outboundEndpointList: oScreenProps.getOutboundEndpointListForExportDialog(),
          disableAuthMapping : oScreenProps.getIsAuthMappingDisabled(),
          physicalCatalog : oDataIntegrationInfo.physicalCatalogId,
          currentOrganizationId : sOrganizationId,
          selectedAuthorizationMappingId: oScreenProps.getSelectedAuthorizationMappingId(),
          isTransferBetweenStagesEnabled: oScreenProps.getIsTransferBetweenStagesEnabled(),
          endpointType: oDataIntegrationInfo.endpointType,
          allowedPhysicalCatalogIds: aAllowedPhysicalCatalogIds,
          isRevisionableTransfer: oScreenProps.getIsRevisionableTransfer()
        };

        oCustomObject = {
          '-1': {
            'label': getTranslation().SELF
          }

        }
      }

      oTransferContents.view = (
          <ButtonWithContextMenuView
                  key={"transferContentsButton"}
                  context={"transferContents"}
                  className={"transferContentsButton"}
                  tooltip={sToolTipForTransfer}
                  useCase={sUseCaseForTransfer}
                  contextMenuData={oContextMenuViewData}
                  customObject={oCustomObject}
                                />);
    }

    return aLeftToolbarItems;
  };

  modifyToolbarItemsForSmartDocumentPreset = (aLeftData) => {
    let oScreenProps = this.getComponentProps().screen;
    let oSmartDocumentPresetContent = CS.find(aLeftData, {id: 'smartDocumentPresets'});
    let bShowGenerateSmartDocumentButton = oScreenProps.getIsGenerateSmartDocuemntButtonVisible();
    if(!CS.isEmpty(oSmartDocumentPresetContent) && bShowGenerateSmartDocumentButton){
      let oRequestResponseInfoData = {
        requestType: "customType",
        requestURL: getRequestMapping().GetSmartDocumentPresetList,
        responsePath: ["success","list"]
      };
      let oContextMenuViewData = {
        isMultiSelect: false,
        showPopover: oScreenProps.getSmartDocumentPresetsPopoverVisibility(),
        showSearch: true,
        showArrowHead: true,
        contextMenuViewModel: [],
        requestResponseInfoData: oRequestResponseInfoData
      };
      oSmartDocumentPresetContent.view = (<ButtonWithContextMenuView
          key={"smartDocumentPresetButton"}
          context={"smartDocumentPresetContent"}
          className={"smartDocumentPresetButton"}
          tooltip={getTranslation().GENERATE_SMART_DOCUMENT}
          useCase={"showDynamicList"}
          contextMenuData={oContextMenuViewData}
      />);
    } else {
      CS.remove(aLeftData, {id: "smartDocumentPresets"});
    }

    return aLeftData;
  };

  getNavigationDataForRelationshipSectionLoadMore = (oRelationshipToolbarProps) => {
    var aContentList = oRelationshipToolbarProps.elements;
    var iFrom = oRelationshipToolbarProps.startIndex;
    var iTotalContents = oRelationshipToolbarProps.totalCount;
    var iContentCount = aContentList.length;
    var iTo = iFrom + iContentCount;

    oRelationshipToolbarProps.from = iFrom;
    oRelationshipToolbarProps.to = iTo;
    oRelationshipToolbarProps.totalContents = iTotalContents;
    oRelationshipToolbarProps.currentPageItems = iContentCount;
    return oRelationshipToolbarProps;
  };

  getGoldenRecordPaginationData = () => {
    let oGoldenRecordProps = this.getComponentProps().goldenRecordProps;
    let oGoldenRecordBucketsPaginationData = oGoldenRecordProps.getGoldenRecordBucketsPaginationData();

    let iFrom = oGoldenRecordBucketsPaginationData.from;
    let iContentCount = oGoldenRecordBucketsPaginationData.currentPageCount;
    let iTo = iFrom + iContentCount;

    return {
      from: iFrom,
      to: iTo,
      totalContents: oGoldenRecordBucketsPaginationData.totalBuckets,
      currentPageItems: iContentCount,
      pageSize: oGoldenRecordBucketsPaginationData.pageSize
    };
  };

  getNavigationData = (oFilterContext) => {
    let oFilterProps = ContentUtils.getFilterProps(oFilterContext);
    let iFrom = oFilterProps.getFromValue();
    let iTotalItemsCount = oFilterProps.getTotalItemCount();
    let iPageSize = oFilterProps.getPaginationSize();
    let iCurrentItemsCount = oFilterProps.getCurrentPageItems();
    let iTo = iFrom + iCurrentItemsCount;

    return {
      from: iFrom,
      to: iTo,
      totalContents: iTotalItemsCount,
      currentPageItems: iCurrentItemsCount,
      pageSize: iPageSize
    };
  };

  getCurrentZoomValue = () => {
    var oScreenProps = this.getComponentProps().screen;
    var bIsTaxonomyHierarchySelected = oScreenProps.getIsTaxonomyHierarchySelected();
    var bIsCollectionHierarchySelected = oScreenProps.getIsCollectionHierarchySelected();
    var bRelationshipStatus = ContentUtils.getAvailableEntityViewStatus();
    return bRelationshipStatus  || bIsTaxonomyHierarchySelected || bIsCollectionHierarchySelected
          ? oScreenProps.getSectionInnerZoom() : oScreenProps.getCurrentZoom();
  };

  getCollectionData = (oFilterContext) => {
    var oCollectionData = {};
    var oAppData = this.getAppData();
    var oComponentProps = this.getComponentProps();
    var oScreenProps = oComponentProps.screen;
    var oActiveEntity = this.getActiveEntity();
    var aDynamicCollectionList = oAppData.getDynamicCollectionList();
    var aStaticCollectionList = oAppData.getStaticCollectionList();
    var oActiveCollection = this.getActiveCollection();
    var bIsCollectionHierarchySelected = oScreenProps.getIsCollectionHierarchySelected();
    var oBreadCrumb = oComponentProps.collectionViewProps.getBreadCrumb();
    var oStaticCollectionMap = oAppData.getStaticCollectionMap();
    var iActiveStaticCollectionLevel = oAppData.getActiveStaticCollectionLevel();
    var bAddEntityInCollectionViewStatus = oComponentProps.collectionViewProps.getAddEntityInCollectionViewStatus();
    var bShowDynamicCollectionOption = true;
    var bShowStaticCollectionOption = true;
    var sSelectedModuleId = ContentUtils.getSelectedModuleId();

    let bIsBucketTab = sSelectedModuleId === ModuleDictionary.DASHBOARD && oScreenProps.getSelectedDashboardTabId() === DashboardTabDictionary.BUCKETS_TAB;
    if (!CS.isEmpty(oActiveEntity) || bAddEntityInCollectionViewStatus || bIsCollectionHierarchySelected
        || ContentUtils.getIsArchive() || bIsBucketTab
    ) {
      bShowDynamicCollectionOption = false;
      bShowStaticCollectionOption = false;
    }

    var bIsShakingEnabled = oScreenProps.getShakingStatus();

    oCollectionData.dynamicCollectionList = aDynamicCollectionList;
    oCollectionData.staticCollectionList = aStaticCollectionList;
    oCollectionData.addEntityInCollectionViewStatus = bAddEntityInCollectionViewStatus;
    oCollectionData.activeCollection = oActiveCollection;
    oCollectionData.isShakingEnable = bIsShakingEnabled;
    oCollectionData.showDynamicCollectionOption = bShowDynamicCollectionOption;
    oCollectionData.showStaticCollectionOption = bShowStaticCollectionOption;
    oCollectionData.breadCrumbPath = oBreadCrumb.treePath;
    oCollectionData.staticCollection = oStaticCollectionMap;
    oCollectionData.activeLevel = iActiveStaticCollectionLevel;
    oCollectionData.allHierarchySafe = ContentUtils.allHierarchyNonDirty(oFilterContext);
    oCollectionData.selectedContentIds = ContentUtils.getSelectedContentIds();
    oCollectionData.isEditCollectionScreen = oComponentProps.collectionViewProps.getIsEditCollectionScreen();

    return oCollectionData;
  };

  getActiveCollection = () => {
    var oComponentProps = this.getComponentProps();
    var oActiveCollection = oComponentProps.collectionViewProps.getActiveCollection();
    return oActiveCollection.clonedObject || oActiveCollection;
  };

  getMasterAttributeList = () => {
    var oAppData = this.getAppData();
    return oAppData.getAttributeList();
  };

  getEntityHeaderTagsEditData = () => {
    var oComponentProps = this.getComponentProps();
    var oVariantSectionViewProps = oComponentProps.variantSectionViewProps;
    var oSelectedContext = oVariantSectionViewProps.getSelectedContext();
    var aMasterTags = oSelectedContext ? oSelectedContext.tags || [] : [];
    var aEntityTags = this.getActiveVariantTagDisplayData();
    var oActiveEntity = this.getActiveEntity();
    var oGlobalPermission = oActiveEntity && oActiveEntity.globalPermission || {};
    let bIsContentEditable = !this.getIsContentDisabled();
    let bCanEdit = bIsContentEditable;
    if (oSelectedContext.type === ContextTypeDictionary.IMAGE_VARIANT && oSelectedContext.isAutoCreate) {
      bCanEdit = false;
    }
    let bCanDelete = bIsContentEditable && oGlobalPermission.canDelete;
    if (!CS.isEmpty(oActiveEntity.mdmKlassInstanceId)) {
      bCanEdit = false;
      bCanDelete = false;
    }

    return {
      masterTagGroups: aMasterTags,
      tagInstances: aEntityTags,
      canEdit: bCanEdit ,//oGlobalPermission.canEdit removed
      canDelete: bCanDelete
    }
  };

  getUOMVariantViewData = (sContextId, oExtraData) => {
    let oComponentProps = this.getComponentProps();
    let sIdForTableProps = ContentUtils.getIdForTableViewProps(sContextId);
    let oActiveContent = ContentUtils.getActiveContent();
    let sKlassInstanceId = oExtraData.klassInstanceId || oActiveContent.id;
    let oUOMVariantViewProps = oComponentProps.tableViewProps.getVariantContextPropsByContext(sIdForTableProps, sKlassInstanceId);
    if(CS.isEmpty(oUOMVariantViewProps)){
      return {};
    }

    var oSelectedContext = oUOMVariantViewProps.getVariantContext();
    var sActiveVariantContextId = oSelectedContext.id;
    var oSelectedVisibleContext = oUOMVariantViewProps.getSelectedVisibleContext();
    var sVariantDialogOpenContext = oUOMVariantViewProps.getVariantDialogOpenContext();

    var oDummyVariant = oUOMVariantViewProps.getDummyVariant();
    oDummyVariant = oDummyVariant.contentClone ? oDummyVariant.contentClone : oDummyVariant;
    var oVariant = oUOMVariantViewProps.getActiveVariantEntity();

    var bIsVariantDialogOpened = oUOMVariantViewProps.getIsVariantDialogOpen();
    var aSelectedContextSections = [];
    var aSectionViewModel = [];
    var oActiveEditableVariant = null;
    if(bIsVariantDialogOpened) {
      if(CS.includes(sVariantDialogOpenContext, "edit")){
        oActiveEditableVariant =  oUOMVariantViewProps.getActiveVariantForEditing();
        oActiveEditableVariant = oActiveEditableVariant.contentClone ? oActiveEditableVariant.contentClone : oActiveEditableVariant;
      } else {
        oActiveEditableVariant = oDummyVariant;
      }

      aSelectedContextSections = oUOMVariantViewProps.getSelectedContextVariantSections();

      //remove disabled elements
      var aSectionToRemoveIds = [];
      var aSelectedContextSectionsClone = CS.cloneDeep(aSelectedContextSections);
      CS.forEach(aSelectedContextSectionsClone, function (oSection) {
        var aElements = oSection.elements;
        var aNewSectionElements = [];
        CS.forEach(aElements, function (oElement) {
          if(!oElement.isDisabled){
            aNewSectionElements.push(oElement);
          }
        });
        oSection.elements = aNewSectionElements;
        if(CS.isEmpty(aNewSectionElements)){
          aSectionToRemoveIds.push(oSection.id);
        }
      });

      CS.forEach(aSectionToRemoveIds, function (sId) {
        CS.remove(aSelectedContextSectionsClone, {id: sId});
      });
      aSectionViewModel = this.getSectionViewObjectFilledWithModels('variantSections', aSelectedContextSectionsClone, oActiveEditableVariant);
    }

    let oOpenedDialogAttributeData = oComponentProps.uomProps.getOpenedDialogAttributeData();
    let bIsVariatingAttributeDialogOpen = !CS.isEmpty(oOpenedDialogAttributeData.contextId);
    let oReferencedAttributes = oComponentProps.screen.getReferencedAttributes();
    let sAttributeId = oOpenedDialogAttributeData.attributeId;
    let oReferencedMasterAttribute = oReferencedAttributes[sAttributeId];

    return {
      isVariantSectionVisible: true,
      selectedContext: oSelectedContext,
      selectedVisibleContext: oSelectedVisibleContext,
      dummyVariant: oDummyVariant,
      activeVariant: oVariant,
      activeVariantContextId: sActiveVariantContextId,
      editVariantTags: oUOMVariantViewProps.getEditVariantTags(),
      isVariantDialogOpen: bIsVariantDialogOpened,
      editableVariant: oActiveEditableVariant,
      sectionViewModels: aSectionViewModel,
      variantDialogOpenContext: sVariantDialogOpenContext,
      isVariantingAttributeDialogOpen: bIsVariatingAttributeDialogOpen,
      masterAttribute: oReferencedMasterAttribute,
      referencedClasses: oComponentProps.screen.getReferencedClasses(),
      referencedAttributes: oUOMVariantViewProps.getReferencedAttributes(),
      referencedTags: oUOMVariantViewProps.getReferencedTags(),
      assetRelationshipEntities: oComponentProps.screen.getReferencedAssetList()
    }
  };

  getReferencedAssetListForVariantSection = () => {
    var oComponentProps = this.getComponentProps();
    let aScreenReferencedAssetList = oComponentProps.screen.getReferencedAssetList() || [];
    if (oComponentProps.availableEntityViewProps.getAvailableEntityViewContext() === ContentScreenViewContextConstants.TABLE_CONTEXT_AVAILABLE_ENTITY_VIEW) {
      let oActivePopOverContextEntityData = this.getTableActivePopOverContextEntityData();
      let aLinkedInstancesWithAssets = oActivePopOverContextEntityData.assetRelationshipEntities || [];
      return CS.combine(aScreenReferencedAssetList, aLinkedInstancesWithAssets)
    }
    return aScreenReferencedAssetList;
  };

  getSectionHeaderData = (sContext) => {
    var oComponentProps = this.getComponentProps();
    var oScreenProps = oComponentProps.screen;
    var aHeaderButtonsData = [];
    var oSectionLabel = null;
    var sLabel = null;
    var bShowHeaderView = false;
    let bIsContentEditable = !this.getIsContentDisabled();

    if (sContext === ContentScreenConstants.tabItems.TAB_RENDITION) {
      var oSelectedRenditionTabContext = oScreenProps.getSelectedImageVariantContext();
      if (CS.isNotEmpty(oSelectedRenditionTabContext) && oSelectedRenditionTabContext.canCreate) {
        bShowHeaderView = true;
        sLabel = getTranslation().CONTEXT_TYPES_IMAGE_VARIANT;
        if (bIsContentEditable && !oSelectedRenditionTabContext.isAutoCreate) {
          aHeaderButtonsData.push(<FlatCreateButtonView
              context={"renditionTab"}
              contextId={oSelectedRenditionTabContext.id}
              placeholder={getTranslation().CREATE}
          />);
        }
      }
    }
    if (CS.isNotEmpty(sLabel)) {
      oSectionLabel = (<div className={"sectionHeaderLabel"}>{sLabel}</div>);
    }
    return {
      sectionLabel: oSectionLabel,
      headerButtonsData: aHeaderButtonsData,
      showHeaderView: bShowHeaderView,
    }
  }

  getVariantSectionViewData = () => {

    if(CS.isNotEmpty(this.promisedData.variantSectionViewData)) {
      return this.promisedData.variantSectionViewData;
    }

    var oComponentProps = this.getComponentProps();
    var oVariantSectionViewProps = oComponentProps.variantSectionViewProps;
    var oScreenProps = oComponentProps.screen;
    var oReferencedTags = oScreenProps.getReferencedTags();
    var oSelectedContext = oVariantSectionViewProps.getSelectedContext();
    var oSelectedVisibleContext = oVariantSectionViewProps.getSelectedVisibleContext();
    var aSelectedContentIds = ContentUtils.getSelectedContentIds();
    var aReferencedAssetList =  this.getReferencedAssetListForVariantSection();
    var aAllAssetList = oScreenProps.getAssetList();

    var oRelationshipToolbarProps = oComponentProps.relationshipView.getRelationshipToolbarProps();
    var sActiveRelationshipVariantId = oScreenProps.getVariantRelationshipId();
    var oReferenceNatureRelationshipInstanceElements = oComponentProps.relationshipView.getReferenceNatureRelationshipInstanceElements();

    var oActiveEntity = this.getActiveEntity();
    var oSelectedNatureRelationship = oComponentProps.relationshipView.getSelectedRelationshipElements();
    var aSelectedNatureElementsIds = oSelectedNatureRelationship[sActiveRelationshipVariantId] || [];

    var sVariantInstanceId = oActiveEntity.variantInstanceId;
    var aVariantTags = oActiveEntity.tags;
    var oVariant = oActiveEntity.variantInstanceId ? oActiveEntity : {};

    //To handle search in variant view
    var aNatureRelationshipInstanceElements = oReferenceNatureRelationshipInstanceElements[sActiveRelationshipVariantId] || [];

    var aSelectedStatusData = oScreenProps.getReferencedStatusTags();
    var aStatusTagGroupModels = [];
    if (!CS.isEmpty(oActiveEntity)) {
      CS.forEach(aSelectedStatusData, function (sTagId) {
        var oFoundReferencedTag = oReferencedTags[sTagId];
        var oElement = {isDisabled: false, isMultiSelect: false};
        aStatusTagGroupModels.push(CommonUtils.getTagGroupModels(oFoundReferencedTag, oActiveEntity, oElement, "variant"));
      });
    }
    var sActiveEntitySelectedTabId = !CS.isEmpty(oActiveEntity) ? ContentUtils.getSelectedTabId() : null;
    var bIsLinkVariant = false;
    var bIsProductVariant = false;

    var oCurrentRelationshipToolbarProps = {};
    if(!CS.isEmpty(oRelationshipToolbarProps[sActiveRelationshipVariantId])){
      oCurrentRelationshipToolbarProps = this.getNavigationDataForRelationshipSectionLoadMore(oRelationshipToolbarProps[sActiveRelationshipVariantId]);
    }

    var sActiveEntityType = "";
    if (oActiveEntity && !CS.isEmpty(oActiveEntity.types)) {
      sActiveEntityType = ContentUtils.getEntityClassType(oActiveEntity);
    }

    var oDummyVariant = oVariantSectionViewProps.getDummyVariant();
    var sVariantDialogOpenContext = oVariantSectionViewProps.getVariantDialogOpenContext();
    var bIsVariantDialogOpened = oVariantSectionViewProps.getIsVariantDialogOpen();
    var aSelectedContextSections = [];
    var aSectionViewModel = [];
    var oActiveEditableVariant = null;
    if(bIsVariantDialogOpened) {
      oActiveEditableVariant = ContentUtils.getEditableVariant();
      oActiveEditableVariant = oActiveEditableVariant.contentClone ? oActiveEditableVariant.contentClone : oActiveEditableVariant;
      oDummyVariant = oActiveEditableVariant;
      aSelectedContextSections = oComponentProps.screen.getSelectedContextVariantSections();

      //remove disabled elements
      var aSectionToRemoveIds = [];
      var aSelectedContextSectionsClone = CS.cloneDeep(aSelectedContextSections);
      CS.forEach(aSelectedContextSectionsClone, function (oSection) {
        var aElements = oSection.elements;
        var aNewSectionElements = [];
        CS.forEach(aElements, function (oElement) {
          if(!oElement.isDisabled){
            aNewSectionElements.push(oElement);
          }
        });
        oSection.elements = aNewSectionElements;
        if(CS.isEmpty(aNewSectionElements)){
          aSectionToRemoveIds.push(oSection.id);
        }
      });

      CS.forEach(aSectionToRemoveIds, function (sId) {
        CS.remove(aSelectedContextSectionsClone, {id: sId});
      });
      aSectionViewModel = this.getSectionViewObjectFilledWithModels('variantSections', aSelectedContextSectionsClone, oActiveEditableVariant);
    }

    var oDateRangeSelectorData = {
      userDateRangeStartDate: oVariantSectionViewProps.getUserDateRangeStartDate(),
      userDateRangeEndDate: oVariantSectionViewProps.getUserDateRangeEndDate(),
      isUserDateRangeApplied: oVariantSectionViewProps.getIsUserDateRangeApplied(),
      isCurrentDate: oVariantSectionViewProps.getIsCurrentDateSelected(),
      showCurrentDate: true,
      context: "priceVariant"
    };

    this.promisedData.variantSectionViewData =  {
      selectedContext: oSelectedContext,
      selectedVisibleContext: oSelectedVisibleContext,
      dummyVariant: oDummyVariant,
      variantInstanceId: sVariantInstanceId,
      variantTags: aVariantTags, //todo-variant //no
      activeVariant: oVariant,
      selectedVariantIds: aSelectedContentIds,
      assetRelationshipEntities: aReferencedAssetList,
      allAssetList: aAllAssetList,
      variantImageUploadDialogStatus: oScreenProps.getVariantUploadImageDialogStatus(),
      activeEntitySelectedTabId: sActiveEntitySelectedTabId,
      relationshipToolbarProps: oCurrentRelationshipToolbarProps,
      activeEntityType: sActiveEntityType,
      natureRelationshipInstanceElements: aNatureRelationshipInstanceElements,
      selectedNatureElements: aSelectedNatureElementsIds,
      isVariantRelationshipAddViewVisible: oVariantSectionViewProps.getAvailableEntityChildVariantViewVisibilityStatus(),
      isLinkedVariant: bIsLinkVariant,
      isProductVariant: bIsProductVariant,
      isVariantDialogOpen: bIsVariantDialogOpened,
      editableVariant: oActiveEditableVariant,
      sectionViewModels: aSectionViewModel,
      dateRangeSelectorData: oDateRangeSelectorData,
      variantDialogOpenContext: sVariantDialogOpenContext,
    }

    return this.promisedData.variantSectionViewData;
  };

  getContextualAttributeData = (sAttributeId, aEntityAttributes) => {
    var oContextualAttributeData = {};
    oContextualAttributeData.attributes = CS.filter(aEntityAttributes, function (oAttribute) {
      return ((oAttribute.attributeId === sAttributeId) && (!CS.isEmpty(oAttribute.tags))); //tags check to exclude original attribute instance
    });

    return oContextualAttributeData;
  };

  getEntityTabData = () => {

    if(CS.isNotEmpty(this.promisedData.entityTabData)) {
      return this.promisedData.entityTabData;
    }
    var oComponentProps = this.getComponentProps();

    let oTemplate = oComponentProps.screen.getReferencedTemplate();
    let aTabs = oTemplate.tabs || [];

    let oFoundTaskTab = CS.find(aTabs,{id: 'task_tab'});
    if(oFoundTaskTab){
      oFoundTaskTab.count = oComponentProps.taskProps.getTasksCount();
    }

    this.promisedData.entityTabData = {
      entityTabItemList: aTabs,
      activeEntitySelectedTabId: ContentUtils.getSelectedTabId()
    };

    return this.promisedData.entityTabData;
  };

  getNatureSectionViewData = () => {
    var oComponentProps = this.getComponentProps();
    var _this = this;
    var aNatureSectionViewData = [];

    if (!CS.isEmpty(oComponentProps.screen.getNatureRelationshipIds())) {
      var oActiveEntity = this.getActiveEntity();
      let bIsContentEditable = !this.getIsContentDisabled();
      var aNatureRelationships = oActiveEntity.natureRelationships;
      var oReferencedNatureRelationships = oComponentProps.screen.getReferencedNatureRelationships();
      var oReferencedElements = oComponentProps.screen.getReferencedElements();
      var oReferenceNatureRelationshipInstanceElements = oComponentProps.relationshipView.getReferenceNatureRelationshipInstanceElements();
      var oRelationshipToolbarProps = oComponentProps.relationshipView.getRelationshipToolbarProps();

      CS.forEach(oComponentProps.screen.getNatureRelationshipIds(), function (sRelationshipId) {
        var oNatureRelationship = CS.find(aNatureRelationships, {relationshipId: sRelationshipId});
        let sSideId = oNatureRelationship.sideId;
        let aNaturePropertyCollectionSectionModel = [];
        /***uncomment when nature PC are needed**/
        var bNatureRelationshipAddViewStatus = ContentUtils.getNatureRelationshipViewStatus();
        var oSelectedNatureRelationship = oComponentProps.relationshipView.getSelectedRelationshipElements();
        var aSelectedNatureElementsIds = oSelectedNatureRelationship[sSideId] || [];

        var oReferencedNatureRelationship = oReferencedNatureRelationships[sRelationshipId];
        var oCurrentRelationshipToolbarProps = {};
        if (!CS.isEmpty(oRelationshipToolbarProps[sSideId])) {
          oCurrentRelationshipToolbarProps = _this.getNavigationDataForRelationshipSectionLoadMore(oRelationshipToolbarProps[sSideId]);
        }
        var aReferencedAssetList = oComponentProps.screen.getReferencedAssetList();
        var oGlobalPermission = oActiveEntity.globalPermission || {};
        let bIsDisabled = oReferencedElements[sSideId] && oReferencedElements[sSideId].isDisabled;
        let oNatureThumbnailProperties = {
          disableCheck: !bIsContentEditable || bIsDisabled,
          disableDelete: !bIsContentEditable || bIsDisabled
        };


        let oBundleSectionToolbarData = CS.cloneDeep(ContentToolbarItemList.BundleSectionToolbarData);
        if(bIsDisabled) {
          delete oBundleSectionToolbarData.selectAll;
        }else{
          oBundleSectionToolbarData.selectAll[0].selectedCount = aSelectedNatureElementsIds.length;
        }
        let aNatureRelationshipInstanceElements = oReferenceNatureRelationshipInstanceElements[sSideId] || [];
        oNatureRelationship.elementIds = aNatureRelationshipInstanceElements ? aNatureRelationshipInstanceElements.map(obj => obj.id): oNatureRelationship.elementIds;

        let bCanDelete = !CS.isEmpty(oNatureRelationship) ? oNatureRelationship.canDelete : true;

        if (aSelectedNatureElementsIds.length < 1 && !bCanDelete) {
          delete oBundleSectionToolbarData.remove;
        }

        if (aSelectedNatureElementsIds.length !== 1) {
          delete oBundleSectionToolbarData.transfer;
        }

        if (aSelectedNatureElementsIds.length) {
          let oSelectAllElement = oBundleSectionToolbarData.selectAll[0];
          oSelectAllElement.selectedCount = aSelectedNatureElementsIds.length;
          oSelectAllElement.label = "DESELECT_ALL";
          oSelectAllElement.className = aNatureRelationshipInstanceElements.length === aSelectedNatureElementsIds.length ? "toolUnCheckAll" : "toolHalfChecked"
        }

        var oSectionViewData = {
          referencedNatureRelationship: oReferencedNatureRelationship,
          natureRelationshipElement: oReferencedElements[sSideId],
          natureRelationship: oNatureRelationship,
          isReadOnly: !bIsContentEditable  || bIsDisabled,
          showToolbar: true,
          natureRelationshipInstanceElements: aNatureRelationshipInstanceElements,
          naturePropertyCollectionSectionModel: aNaturePropertyCollectionSectionModel,
          relationshipToolbarProps: oCurrentRelationshipToolbarProps,
          natureRelationshipAddViewStatus: bNatureRelationshipAddViewStatus,
          selectedNatureElements: aSelectedNatureElementsIds,
          assetRelationshipEntities: aReferencedAssetList,
          globalPermission: oGlobalPermission,
          relationshipId: sRelationshipId,
          sideId: sSideId,
          viewContext: ContentScreenViewContextConstants.NATURE_RELATIONSHIP + ContentUtils.getSplitter() + ContentScreenViewContextConstants.NATURE_TYPE_RELATIONSHIP,
          natureThumbnailProperties:oNatureThumbnailProperties,
          isNatureThumbConnectorHidden: false,
          bundleSectionToolbarData: oBundleSectionToolbarData
        };

        aNatureSectionViewData.unshift(oSectionViewData);
      });
    }

    return aNatureSectionViewData;
  };

  getScreenViewsMasterData = () => {

    if(CS.isNotEmpty(this.promisedData.screenViewMasterData)) {
      return this.promisedData.screenViewMasterData;
    }

    var oComponentProps = this.getComponentProps();
    var oScreenMasterData = this.props.screenMasterData;
    var oViewMasterData = {};
    let sSelectedHierarchyContext = ContentUtils.getSelectedHierarchyContext();

    /****** FilterSearchView Master Data ****/
    oViewMasterData.filterSearchViewMasterData = {};
    var oFilterSearchViewMasterData = oViewMasterData.filterSearchViewMasterData;
    oFilterSearchViewMasterData.canFilterTaxonomy  =  oScreenMasterData.canFilterTaxonomy;

    /****** CollectionOptionsView Master Data (Inside FilterSearchView)****/
    oFilterSearchViewMasterData.collectionOptionsViewMasterData = {};
    var oCollectionOptionsViewMasterData = oFilterSearchViewMasterData.collectionOptionsViewMasterData;
    oCollectionOptionsViewMasterData.isBookmarkEnabled  =  oScreenMasterData.isBookmarkEnabled;
    oCollectionOptionsViewMasterData.isStaicCollectionEnabled  =  oScreenMasterData.isStaicCollectionEnabled;

    /****** Do not show collection and bookmark option for any Hierarchy Mode****/
    if (ContentUtils.getSelectedHierarchyContext()) {
      oCollectionOptionsViewMasterData.isBookmarkEnabled = false;
      oCollectionOptionsViewMasterData.isStaicCollectionEnabled = false;
    }

    /****** ContentThumbContainerView Master Data ****/
    oViewMasterData.contentThumbContainerViewMasterData = {};
    var oContentThumbContainerViewMasterData = oViewMasterData.contentThumbContainerViewMasterData;

    /****** ContentTileListView Master Data (Inside ContentThumbContainerView) ****/
    oContentThumbContainerViewMasterData.contentTileListViewMasterData = {};
    var oContentTileListViewMasterData = oContentThumbContainerViewMasterData.contentTileListViewMasterData;

    /****** FilterView Master Data (Inside ContentTileListView)****/
    oContentTileListViewMasterData.filterViewMasterData = {};
    var oFilterViewMasterData = oContentTileListViewMasterData.filterViewMasterData;
    /* Filter hierarchy dose not need to apply filter from filter view. */
    oFilterViewMasterData.canFilter  =  oScreenMasterData.canFilter;
    oFilterViewMasterData.canSort  =  oScreenMasterData.canSort;
    oFilterViewMasterData.canFilterTaxonomy  =  oScreenMasterData.canFilterTaxonomy;

    /****** FilterView Master Data (Inside ContentTileListView)****/
    var aModuleList = oComponentProps.screen.getAllModuleList();
    var oSelectedModule = CS.find(aModuleList, {isSelected: true}) || {};
    oContentTileListViewMasterData.toolbarViewMasterData = {};
    var oToolbarViewMasterData = oContentTileListViewMasterData.toolbarViewMasterData;
    oToolbarViewMasterData.allowedViews = oSelectedModule.allowedViews || [];

    this.promisedData.screenViewMasterData = oViewMasterData;

    return oViewMasterData;
  };

  getFilterView = (oFilterViewData, oOverridingViewProps, oExtraData = {}) => {
    var oComponentProps = this.getComponentProps();
    var sViewMode = ContentUtils.getViewMode();
    let bIsEditMode = oComponentProps.screen.getIsEditMode();
    let bIsHierarchyMode = CS.isNotEmpty(ContentUtils.getSelectedHierarchyContext());
    let oScreenViewsMasterData = this.getScreenViewsMasterData();
    let oCollectionData = this.getCollectionData(oFilterViewData.filterContext);
    var bIsFilterAndSearchViewDisabled = oComponentProps.screen.getFilterAndSearchViewDisabledStatus();
    var oFilterSearchViewMasterData = oScreenViewsMasterData.filterSearchViewMasterData;
    let bIsArchive = ContentUtils.getIsArchive();
    let bShowGoldenRecordBuckets = oComponentProps.goldenRecordProps.getShowGoldenRecordBuckets();
    oOverridingViewProps = oOverridingViewProps || {};
    let bisOrganizeButtonDisabled = oOverridingViewProps.isOrganizeButtonDisabled || bIsArchive;
    let bShowTaxonomySelectoreView = !bShowGoldenRecordBuckets;
    let oFilterProps = ContentUtils.getFilterProps(oFilterViewData.filterContext);
    let bHideEmpty = oFilterProps.getHideEmpty();
    let contextualAllCategoriesViewProps = oComponentProps.contextualAllCategoriesSelectorViewProps.getAllCategoriesSelectorViewPropsByContext(oFilterViewData.filterContext.screenContext);
    let aAllCategoriesTaxonomies =  new AllCategoriesTaxonomiesData();
    let sSelectedCategoriesId = contextualAllCategoriesViewProps.getSelectedCategoryID();
    let oSelectedCategories = CS.find(aAllCategoriesTaxonomies, {id: sSelectedCategoriesId});
    let aLevelNames = oSelectedCategories && (oSelectedCategories.label !== "Taxonomies") ? [oSelectedCategories.label]
                                                                                          : [];
    let aSummaryTreeData = contextualAllCategoriesViewProps.getSummaryTreeData().clonedObject ||
        contextualAllCategoriesViewProps.getSummaryTreeData();
    let sSearchText = contextualAllCategoriesViewProps.getTreeSearchText();
    let oSearchViewTreeData = contextualAllCategoriesViewProps.getSearchViewTreeData();
    let oTreeViewData = TreeViewProps.getTreeData();
    let oTreeLoadMoreMap = TreeViewProps.getTreeLoadMoreMap();
    if (sSearchText) {
      let {loadMoreMap: oLoadMoreMap, ...oTreeData} = oSearchViewTreeData;
      oTreeViewData = oTreeData;
      oTreeLoadMoreMap = oLoadMoreMap;
    }
    let oAllCategoriesTaxonomiesSelectorTreeData = {
      treeData: oTreeViewData,
      searchText: sSearchText,
      treeLoadMoreMap: oTreeLoadMoreMap,
      context: sSelectedCategoriesId,
      levelNames: aLevelNames
    };

    let bIsShowOrganizeButton = !bIsEditMode && !bisOrganizeButtonDisabled && CS.isEmpty(oCollectionData.activeCollection) && !bIsHierarchyMode;
    let oAllCategoriesPopOverData = {
      isSelectAllCategoriesPopOverVisible: contextualAllCategoriesViewProps.getIsAllCategoriesTaxonomiesPopOverVisible(),
      selectedCategoriesId: sSelectedCategoriesId,
      allCategoriesTaxonomyTreeData: oAllCategoriesTaxonomiesSelectorTreeData,
      summaryTreeData: aSummaryTreeData,
      isAllCategoriesPopOverDirty: contextualAllCategoriesViewProps.getIsAllCategoriesTaxonomySelectorPopDirty(),
      isShowOrganizeButton: bIsShowOrganizeButton,
    };

    return (
        <FilterSearchView
            taxonomyTree={oFilterViewData.taxonomyTree}
            taxonomyTreeRequestData={oExtraData.taxonomyTreeRequestData}
            taxonomyVisualProps={oFilterViewData.taxonomyVisualProps}
            searchText={oFilterViewData.searchText}
            collectionData = {oCollectionData}
            matchingTaxonomyIds={oFilterViewData.matchingTaxonomyIds}
            taxonomyTreeSearchText={oFilterViewData.taxonomyTreeSearchText}
            parentTaxonomyId={oFilterViewData.parentTaxonomyId}
            isFilterAndSearchViewDisabled={bIsFilterAndSearchViewDisabled}
            viewMasterData={oFilterSearchViewMasterData}
            affectedTreeNodeIds={oFilterViewData.affectedTreeNodeIds}
            taxonomySettingIconClickedNode={oFilterViewData.taxonomySettingIconClickedNode}
            taxonomyNodeSections={oFilterViewData.taxonomyNodeSections}
            isSelectTaxonomyPopOverVisible={oFilterViewData.isSelectTaxonomyPopOverVisible}
            selectedHierarchyContext={oFilterViewData.selectedHierarchyContext}
            viewMode={sViewMode}
            isChooseTaxonomyVisible={oFilterViewData.bIsChooseTaxonomyVisible}
            isOrganizeButtonDisabled={bisOrganizeButtonDisabled}
            showTaxonomySelectorView={bShowTaxonomySelectoreView}
            filterContext={oFilterViewData.filterContext}
            isEditMode={bIsEditMode}
            isHierarchyMode={bIsHierarchyMode}
            hideEmpty={bHideEmpty}
            allCategoriesPopOverData={oAllCategoriesPopOverData}
            />
    );
  };

  getJobViewData = () => {
    let oAppData = this.getAppData();
    let oComponentProps = this.getComponentProps();
    let JobProps = oComponentProps.jobProps;

    let oEntityViewData = {
      screen: "products",
      jobViewData: {
        jobList: oAppData.getJobList(),
        activeJob: JobProps.getActiveJob(),
        activeJobGraphData: JobProps.getActiveJobGraphData()
      }
    };

    return {oEntityViewData};
  };

  getPropertyCollectionModelList = (oCurrentEntity, sContext) => {
    var oComponentProps = this.getComponentProps();
    var aMasterPropertyCollectionList = oComponentProps.screen.getPropertyCollectionsList();
    var aSectionModels = [];
    CS.forEach(aMasterPropertyCollectionList, function (oSection) {
      if (CS.find(oCurrentEntity.sections, {propertyCollectionId: oSection.id}) || oSection.isForXRay) {
        return;
      }
      aSectionModels.push(new ContextMenuViewModel(
          oSection.id,
          CS.getLabelOrCode(oSection),
          false,
          "",
          {context: sContext}
      ));
    });
    return aSectionModels;
  };

  getCustomContextMenuModelList = (aItemList, sContext) => {
    var aContextModels = [];
    CS.forEach(aItemList, function (oItem) {
      aContextModels.push(new ContextMenuViewModel(
          oItem.id,
          CS.getLabelOrCode(oItem),
          false,
          "",
          {context: sContext}
      ));
    });
    return aContextModels;
  };

  getContentHierarchyData = (oFilterContext) => {
    var oComponentProps = this.getComponentProps();
    var oScreenProps = oComponentProps.screen;
    var oFilterProps = ContentUtils.getFilterProps(oFilterContext);

    var sSelectedHierarchyContext = ContentUtils.getSelectedHierarchyContext();

    let bIsScrollAutomatically = oScreenProps.getIsHierarchyTreeScrollAutomaticallyEnabled();
    let oClickedThumbModel = oScreenProps.getRightClickedThumbnailModel();
    let oPaginationData = {};
    let bRightClickedThumbnailId = !CS.isEmpty(oClickedThumbModel) ? oClickedThumbModel.id : "";

    let oHierarchyTreeRightClickedNodeData = oScreenProps.getRightClickedHierarchyTreeNodeData();
    let sRightClickedHierarchyNodeId = oHierarchyTreeRightClickedNodeData.clickedNodeId;

    var oCopyOrCutData = oScreenProps.getHierarchyEntitiesToCopyOrCutData();
    var bIsEmptyCopyCutList = CS.isEmpty(oCopyOrCutData) || CS.isEmpty(oCopyOrCutData.entityList);
    let oSelectedContext = CS.cloneDeep(oComponentProps.screen.getSelectedContext());
    oSelectedContext.updateAll = true;

    var aContextMenuItemList = [];
    var sSelectedContext = "";
    var oTaxonomySectionData = {};
    var oCollectionSectionsData = {};
    if(sSelectedHierarchyContext === HierarchyTypesDictionary.TAXONOMY_HIERARCHY){
      sSelectedContext = HierarchyTypesDictionary.TAXONOMY_HIERARCHY;
      var oTaxonomySections = oFilterProps.getTaxonomyNodeSections();
      var bSectionDirty = false;
      var aTaxonomySections = [];
      if(oTaxonomySections.clonedObject){
        aTaxonomySections = oTaxonomySections.clonedObject.sections;
        bSectionDirty = true;
      }else {
        aTaxonomySections = oTaxonomySections.sections;
      }
      var sTaxonomyViewMode = oScreenProps.getHierarchyDetailViewMode();

      let bIsInnerHierarchyViewMode = true;
      let oAddedEntitiesView = sTaxonomyViewMode == "thumbnailViewMode" ?
          this.getAvailableEntityViewForTaxonomyHierarchy(sSelectedContext, true, bIsInnerHierarchyViewMode, oFilterContext) : null;
      aContextMenuItemList = ContentHierarchyContextMenuList.TaxonomyHierarchyNodeContextMenuList;
      if(bIsEmptyCopyCutList){
        aContextMenuItemList = CS.reject(aContextMenuItemList, {id: "paste"})
      }

      oTaxonomySectionData = {
        outerSelectedParentTaxonomyId: oScreenProps.getSelectedOuterParentContentHierarchyTaxonomyId(),
        taxonomyClickedNode: oFilterProps.getTaxonomySettingIconClickedNode(),
        taxonomyNodeSections: aTaxonomySections,
        isShakingEnable: oScreenProps.getShakingStatus(),
        isActiveSectionsDirty: bSectionDirty,
        taxonomyViewMode: sTaxonomyViewMode,
        addedEntitiesView: oAddedEntitiesView,
        taxonomySectionModels: this.getHierarchySectionModels(aTaxonomySections, HierarchyTypesDictionary.TAXONOMY_HIERARCHY, oFilterContext),
        selectedContext: oSelectedContext,
      };
    }
    else if(sSelectedHierarchyContext === HierarchyTypesDictionary.COLLECTION_HIERARCHY){
      sSelectedContext = HierarchyTypesDictionary.COLLECTION_HIERARCHY;
      var oActiveHierarchyCollection = oScreenProps.getActiveHierarchyCollection();
      var bIsActiveCollectionDirty = false;
      if(oActiveHierarchyCollection.clonedObject){
        bIsActiveCollectionDirty = true;
        oActiveHierarchyCollection = oActiveHierarchyCollection.clonedObject;
      }
      var aPropertyCollectionModels = this.getPropertyCollectionModelList(oActiveHierarchyCollection, sSelectedContext);
      var sCollectionViewMode = oScreenProps.getHierarchyDetailViewMode();
      let bIsInnerHierarchyViewMode = true;

      aContextMenuItemList = ContentHierarchyContextMenuList.CollectionHierarchyNodeContextMenuList;
      if(bIsEmptyCopyCutList){
        aContextMenuItemList = CS.reject(aContextMenuItemList, {id: "paste"})
      }
      var oReferencedTags = oComponentProps.screen.getReferencedTags();
      let bShowPublicPrivateModeButton = oComponentProps.collectionViewProps.getPublicPrivateModeButtonVisibility();

      oCollectionSectionsData = {
        activeHierarchyCollection: oActiveHierarchyCollection,
        propertyCollectionModels: aPropertyCollectionModels,
        context: sSelectedContext,
        isShakingEnable: oScreenProps.getShakingStatus(),
        isActiveCollectionDirty: bIsActiveCollectionDirty,
        collectionViewMode: sCollectionViewMode,
        collectionSectionModels: this.getHierarchySectionModels(oActiveHierarchyCollection.sections, HierarchyTypesDictionary.COLLECTION_HIERARCHY),
        rightClickedThumbnailId: bRightClickedThumbnailId,
        isMasterCollectionListOrganiseClicked: oScreenProps.getIsMasterCollectionListOrganiseClicked(),
        selectedContext: oSelectedContext,
        referencedTags: oReferencedTags,
        showPublicPrivateModeButton: bShowPublicPrivateModeButton,
        collectionHierarchyRightsideViewSpecificData: this.getCollectionHierarchyRightsideViewSpecificData(sSelectedContext, true, bIsInnerHierarchyViewMode, oFilterContext)
      }
    }

    return {
      selectedContext: sSelectedContext,
      oHierarchyTree: oScreenProps.getContentHierarchyTree(),
      oHierarchyTreeVisualProps: oScreenProps.getContentHierarchyVisualProps(),
      taxonomySectionData: oTaxonomySectionData,
      collectionSectionsData: oCollectionSectionsData,
      rightClickedHierarchyNodeId: sRightClickedHierarchyNodeId,
      hierarchyNodeContextMenuItemList: aContextMenuItemList,
      paginationData: oPaginationData,
      scrollAutomatically: bIsScrollAutomatically,
    }
  };

  getXRayData = () => {
    var oComponentProps = this.getComponentProps();
    var oScreenProps = oComponentProps.screen;

    return {
      referencedAttributes: oScreenProps.getReferencedAttributes(),
      referencedTags: oScreenProps.getReferencedTags()
    }
  };

  addMustShouldDataInRuleViolationObject = (oElement) => {
    var oComponentProps = this.getComponentProps();
    var oScreenProps = oComponentProps.screen;
    var oRuleViolationObject = oScreenProps.getRuleViolationObject();
    var aInstance = [];
    var sType = oElement.type;
    if (oElement.type === "attribute") {
      aInstance = oElement.contentAttributes;
    }
    else if (oElement.type === "tag") {
      aInstance = oElement.contentTags;
    }
    var oNewRuleObject = {};
    if(oElement.isMandatory) {
      if (!CS.isEmpty(aInstance) && aInstance[0].isMandatoryViolated) {
        oNewRuleObject = {
          calculatedAttributeUnit: null,
          calculatedAttributeUnitAsHTML: null,
          color: "red",
          description: getTranslation().MANDATORY_FIELD_EMPTY ,
          entityId: oElement.id,
          id: "",
          lastModifiedBy: null,
          ruleId: "",
          ruleLabel: "",
          type: sType,
          versionId: null
        };
      }
    }
      else if(oElement.isShould) {
      if(!CS.isEmpty(aInstance) && aInstance[0].isShouldViolated) {
        oNewRuleObject = {
          calculatedAttributeUnit: null,
          calculatedAttributeUnitAsHTML: null,
          color: "orange",
          description: getTranslation().SHOULD_FIELD_EMPTY,
          entityId: oElement.id,
          id: "",
          lastModifiedBy: null,
          ruleId: "",
          ruleLabel: "",
          type: sType,
          versionId: null
        };
      }
    }
    if (!CS.isEmpty(oNewRuleObject)) {
      if (!oRuleViolationObject[oElement.id]) {
        oRuleViolationObject[oElement.id] = [oNewRuleObject];
      }
      else {
        let isPresent = false;
        CS.forEach(oRuleViolationObject[oElement.id], function (oObject) {
          if (oObject.description === oNewRuleObject.description) {
            isPresent = true;
            return false;
          }
        });
        if (!isPresent) {
          oRuleViolationObject[oElement.id].push(oNewRuleObject);
        }
      }
    }
  };

  addUniqueDataInRuleViolationObject = (oElement) => {
    var oComponentProps = this.getComponentProps();
    var oScreenProps = oComponentProps.screen;
    var oRuleViolationObject = oScreenProps.getRuleViolationObject();
    var aInstance = [];
    var sType = oElement.type;
    if (oElement.type === "attribute") {
      aInstance = oElement.contentAttributes;
    }
    var oNewRuleObject = {};
      if (!CS.isEmpty(aInstance) && aInstance[0].isUnique === 0) {
        oNewRuleObject = {
          calculatedAttributeUnit: null,
          calculatedAttributeUnitAsHTML: null,
          color: "red",
          description:  getTranslation().FIELD_NOT_UNIQUE,
          entityId: oElement.id,
          id: "",
          lastModifiedBy: null,
          ruleId: "",
          ruleLabel: "",
          type: sType,
          versionId: null
        };
    }
    if (!CS.isEmpty(oNewRuleObject)) {
      if (!oRuleViolationObject[oElement.id]) {
        oRuleViolationObject[oElement.id] = [oNewRuleObject];
      }
      else {
        let bIsPresent = false;
        CS.forEach(oRuleViolationObject[oElement.id], function (oObject) {
          if (oObject.description === oNewRuleObject.description) {
            bIsPresent = true;
            return false;
          }
        });
        if (!bIsPresent) {
          oRuleViolationObject[oElement.id].push(oNewRuleObject);
        }
      }
    }
  };

  getGoldenRecordBucketsData = () => {
    let oComponentProps = this.getComponentProps();
    let oGoldenRecordProps = oComponentProps.goldenRecordProps;
    let oGoldenRecordBucketsDataMap = oGoldenRecordProps.getGoldenRecordBucketsDataMap();
    let oConfigDetails = oGoldenRecordProps.getGoldenRecordBucketsConfigDetails();
    return {
      buckets: oGoldenRecordBucketsDataMap,
      configDetails: oConfigDetails
    }
  };

  isHideUpperSearchBar = () => {
    let aModuleList = this.getModulesList();
    return CS.isEmpty(aModuleList);
  };

  isHierarchyModeSelected = () => {
    let bIsHierarchyMode = false;
    var sSelectedHierarchyContext = ContentUtils.getSelectedHierarchyContext();

    if(ContentUtils.isCollectionScreen() || sSelectedHierarchyContext === HierarchyTypesDictionary.COLLECTION_HIERARCHY
        || sSelectedHierarchyContext === HierarchyTypesDictionary.TAXONOMY_HIERARCHY){
      bIsHierarchyMode = true;
    }

    return bIsHierarchyMode;
  };

  fillEntityViewDataForGoldenRecords = () => {
    return {
      showGoldenRecordBuckets: true,
      goldenRecordBucketsData: this.getGoldenRecordBucketsData(),
      goldenRecordBucketsPaginationData: this.getGoldenRecordPaginationData()
    }
  };

  /**
   * @function getCloningWizardViewData
   * @param bShowCloneCountField: Boolean value if there is need to show clone count field in view
   * @returns {*}Returns object with cloning wizard data
   */
  getCloningWizardViewData = () => {
    let oVariantSectionViewProps = this.getComponentProps().variantSectionViewProps;
    let bShowCloneCountField = false;
    let bIsCloningWizardDialogOpen = oVariantSectionViewProps.getIsCloningWizardOpen();
    let aAllowedTypesToCreateLinkedVariant = oVariantSectionViewProps.getAllowedTypesToCreateLinkedVariant();

    if(!bIsCloningWizardDialogOpen){
      return null;
    }

    let oActiveEntity = ContentUtils.getActiveEntity();

    return {
      isCloningWizardOpen: bIsCloningWizardDialogOpen,
      cloningWizardViewData: oVariantSectionViewProps.getCloningWizardViewData(),
      selectedIdsForCloningWizardData: oVariantSectionViewProps.getSelectedEntityIdsForCloning(),
      context: oVariantSectionViewProps.getCloneWizardDialogContext(),
      cloneCount: oVariantSectionViewProps.getCloningWizardCloneCount(),
      showCloneCountField: bShowCloneCountField,
      isExactCloneSelected: oVariantSectionViewProps.getIsExactCloneSelected(),
      productLabelToBeCloned: oActiveEntity.name,
      allowedTypesToCreateLinkedVariant: aAllowedTypesToCreateLinkedVariant
    }
  };

  prepareDataForDashboardFilesModule = () => {
    let oEntityViewData = this.prepareEntityViewData();
    oEntityViewData.screen = "products";
    let oFilterContext = {
      filterType: oFilterPropType.MODULE,
      screenContext: oFilterPropType.MODULE
    };

    let oFilterViewData = {filterContext: oFilterContext, isFilterAndSearchViewDisabled: true};
    let oFilterView = null;
    let oNavigationData = this.getNavigationData(oFilterContext);

    return {oEntityViewData, oFilterViewData, oFilterView, oNavigationData};
  };

  getFilterContext = () => {
    let oComponentProps = this.getComponentProps();
    let oActiveCollection = this.getActiveCollection();
    let bShowGoldenRecordBuckets = oComponentProps.goldenRecordProps.getShowGoldenRecordBuckets();

    let bIsFilterViewVisible = !this.isHideUpperSearchBar();
    let oFilterContext = {};
    if (bIsFilterViewVisible) {
      if (!CS.isEmpty(oActiveCollection)) {
        oFilterContext = {
          filterType: oFilterPropType.COLLECTION,
          screenContext: oActiveCollection.id,
        };
      }
      else if (bShowGoldenRecordBuckets) {
        oFilterContext = {
          filterType: oFilterPropType.MODULE,
          screenContext: "matchAndMerge"
        }
      }
      else {
        oFilterContext = {
          filterType: oFilterPropType.MODULE,
          screenContext: oFilterPropType.MODULE
        }
      }
      return oFilterContext;
    }
  };

  prepareDataForListOrTileMode = (oExtraData) => {
    let bIsFilterViewVisible = !this.isHideUpperSearchBar();
    let oFilterContext = this.getFilterContext();
    let oEntityViewData = this.prepareEntityViewData();
    oEntityViewData.screen = "products";
    /**To prepare dialog data. */
    this.prepareDialogDataForProductsView(oEntityViewData);

    if (bIsFilterViewVisible) {
      let {oFilterViewData = {}, oFilterView = null, oNavigationData = {}} = this.getFilterDataByFilterContext(oFilterContext, oExtraData);
      return {oEntityViewData, oFilterViewData, oFilterView, oNavigationData};
    }
    return {oEntityViewData};
  };

  prepareEntityViewData = () => {
    let oComponentProps = this.getComponentProps();
    let oScreenProps = oComponentProps.screen;
    let oCollectionViewProps = oComponentProps.collectionViewProps;
    let bIsKpiContentExplorerOpen = oComponentProps.screen.getIsKpiContentExplorerOpen();
    let bHideAddButton = bIsKpiContentExplorerOpen || oComponentProps.informationTabProps.getIsRuleViolatedContentsScreen();
    let bShowGoldenRecordBuckets = oComponentProps.goldenRecordProps.getShowGoldenRecordBuckets();
    let {selectedEntityList: aSelectedEntityList, entityList: aEntityList} = this.getEntityListAndSelectedEntityList();
    let oActiveCollection = this.getActiveCollection();
    let oCollectionData = {
      isEditCollectionScreen: oCollectionViewProps.getIsEditCollectionScreen(),
      activeCollection: oActiveCollection,
      addEntityInCollectionViewStatus: oCollectionViewProps.getAddEntityInCollectionViewStatus()
    };
    let sEndpointId = SessionProps.getSessionEndpointId();
    let sOrganizationId = ContentUtils.getCurrentUser().organizationId;

    let oEntityViewData = {
      sortByViewData: this.getDefaultSortData(),
      referencedTags: oScreenProps.getReferencedTags(),
      verticalMenu: bHideAddButton ? {isHideCreateButton: true} : this.getVerticalMenu(),
      isKPIContentExplore: bIsKpiContentExplorerOpen,
      userList: this.getAppData().getUserList(),
      activeEntity: this.getActiveEntity(),
      selectedEntityList: aSelectedEntityList,
      entityList: aEntityList,
      selectedRelationshipElements: oComponentProps.relationshipView.getSelectedRelationshipElements(),
      fileMappingViewData: this.prepareFileMappingViewData(),//TODO: Refactor - prepare file mapping data according to screen
      collectionData: oCollectionData,
      referencedAssets: oScreenProps.getReferencedAssetList(),
      endpointId: sEndpointId,
      organizationId: sOrganizationId
    };

    bShowGoldenRecordBuckets && (CS.assign(oEntityViewData, this.fillEntityViewDataForGoldenRecords()));

    return oEntityViewData;
  };

  getFilterDataByFilterContext = (oFilterContext, oExtraData) => {
    let oFilterViewData = this.getFilterViewData(oFilterContext);
    let oFilterView = this.getFilterView(oFilterViewData, {}, oExtraData);
    let oNavigationData = this.getNavigationData(oFilterContext);

    return {oFilterViewData, oFilterView, oNavigationData};
  };

  getSelectedPropertiesForBulkEdit = () => {
    let oComponentProps = this.getComponentProps();
    let oBulkEditProps = oComponentProps.bulkEditProps;
    let oReferencedAttributes = oBulkEditProps.getReferencedAttributes();
    let aAppliedAttributes = oBulkEditProps.getAppliedAttributes();
    let oReferencedTags = oBulkEditProps.getReferencedTags();
    let oAppliedTags = oBulkEditProps.getAppliedTags();
    let aSelectedProperties = [];

    CS.forEach(aAppliedAttributes, (oAttribute) => {
      let oReferencedAttribute = oReferencedAttributes[oAttribute.attributeId];
      aSelectedProperties.push({
        id: oReferencedAttribute.id,
        label: CS.getLabelOrCode(oReferencedAttribute),
        groupType: "attributes"
      });
    });

    CS.forEach(oAppliedTags, (oTag) => {
      let oReferencedTag = oReferencedTags[oTag.tagId];
      aSelectedProperties.push({
        id: oReferencedTag.id,
        label: CS.getLabelOrCode(oReferencedTag),
        groupType: "tags"
      });
    });

    return aSelectedProperties;
  };

  getBulkEditSummaryViewModel = () => {
    let oComponentProps = this.getComponentProps();
    let oBulkEditProps = oComponentProps.bulkEditProps;
    let oTaxonomySummary = oBulkEditProps.getTaxonomySummary();
    let oClassSummary = oBulkEditProps.getClassSummary();
    let oElements = {};

    let aAttributes = CS.sortBy(oBulkEditProps.getAppliedAttributes(), "label");
    let oReferencedAttributes = oBulkEditProps.getReferencedAttributes();
    CS.forEach(aAttributes, (oAttribute) => {
        oElements[oAttribute.attributeId] = {
          header: CS.getLabelOrCode(oAttribute),
          type: "attribute",
          data: oAttribute,
          referencedData: oReferencedAttributes[oAttribute.attributeId]
        }
    });

    let aTags = CS.sortBy(oBulkEditProps.getAppliedTags(), "label");
    let oReferencedTags = oBulkEditProps.getReferencedTags();
    CS.forEach(aTags, (oTag) => {
      oElements[oTag.tagId] = {
        header: CS.getLabelOrCode(oTag),
        type: "tag",
        data: oTag,
        referencedData: oReferencedTags[oTag.tagId]
      }
    });

    if(CS.isNotEmpty(oTaxonomySummary.taxonomiesToAdd)) {
      oElements["addedTaxonomies"] = {
        header: getTranslation().ADDED_TAXONOMIES,
        type: "taxonomy",
        data: oTaxonomySummary.taxonomiesToAdd
      };
    }

    if(CS.isNotEmpty(oTaxonomySummary.taxonomiesToDelete)) {
      oElements["removedTaxonomies"] = {
        header: getTranslation().REMOVED_TAXONOMIES,
        type: "taxonomy",
        data: oTaxonomySummary.taxonomiesToDelete
      };
    }

    if(CS.isNotEmpty(oClassSummary.classesToAdd)) {
      oElements["addedClasses"] = {
        header: getTranslation().ADDED_CLASSES,
        type: "class",
        data: oClassSummary.classesToAdd
      };
    }

    if(CS.isNotEmpty(oClassSummary.classesToDelete)) {
      oElements["removedClasses"] = {
        header: getTranslation().REMOVED_CLASSES,
        type: "class",
        data: oClassSummary.classesToDelete
      };
    }

    return oElements;
  };

  getBulkEditViewModel = () => {
    let oComponentProps = this.getComponentProps();
    let oBulkEditProps = oComponentProps.bulkEditProps;
    let oBulkEditToolbarData = new BulkEditToolBarData();
    let oFunctionPermission = oComponentProps.screen.getFunctionalPermission();

    this.handleBulkEditToolbarData(oBulkEditToolbarData);

    let oPropertiesTabData = oFunctionPermission.canBulkEditProperties ? this.prepareBulkEditPropertiesTabData() : {};
    let oTaxonomiesTabData = oFunctionPermission.canBulkEditTaxonomies ? this.prepareBulkEditTaxonomyTabData() : {};
    let oClassesTabData = oFunctionPermission.canBulkEditClasses ? this.prepareBulkEditClassesTabData() : {};

    return {
      isBulkEditViewOpen: oBulkEditProps.getIsBulkEditViewOpen(),
      selectedTabId: oBulkEditProps.getSelectedTabForBulkEdit(),
      toolbarData: oBulkEditToolbarData,
      propertiesTabData: oPropertiesTabData,
      taxonomiesTabData: oTaxonomiesTabData,
      classesTabData: oClassesTabData,
      bulkEditSummary: this.getBulkEditSummaryViewModel()
    }
  };

  /**
   * Handle bulk edit toolbar data according to function permissions of tabs.
   * @param oBulkEditToolbarData
   */
  handleBulkEditToolbarData = (oBulkEditToolbarData) => {
    let oComponentProps = this.getComponentProps();
    let oBulkEditProps = oComponentProps.bulkEditProps;
    let oFunctionPermission = oComponentProps.screen.getFunctionalPermission();
    let bCanBulkEditProperties = oFunctionPermission.canBulkEditProperties;
    let bCanBulkEditTaxonomies = oFunctionPermission.canBulkEditTaxonomies;
    let bCanBulkEditClasses = oFunctionPermission.canBulkEditClasses;
    let oBulkEditToolbar = oBulkEditToolbarData.bulkEditToolbar;

    /**Default selected Tab in bulkEditProps is PROPERTIES. If properties tab does not have permission, default tab should be Taxonomies,
     * if it has permission, otherwise Classes. If Bulk Edit dialog is open, then At least 1 of the tab will always have permission. */
    let sSelectedTabForBulkEdit = oBulkEditProps.getSelectedTabForBulkEdit();
    if (sSelectedTabForBulkEdit === BulkEditTabTypesConstants.PROPERTIES && !bCanBulkEditProperties) {
      if (bCanBulkEditTaxonomies) {
        oBulkEditProps.setSelectedTabForBulkEdit(BulkEditTabTypesConstants.TAXONOMIES);
      }
      else {
        oBulkEditProps.setSelectedTabForBulkEdit(BulkEditTabTypesConstants.CLASSES);
      }
    }

    CS.remove(oBulkEditToolbar, function(oToolbarItem) {
      switch (oToolbarItem.id) {
        case BulkEditTabTypesConstants.PROPERTIES :
          if (!bCanBulkEditProperties) {
            return oToolbarItem.id;
          }
          break;
        case BulkEditTabTypesConstants.TAXONOMIES:
          if (!bCanBulkEditTaxonomies) {
            return oToolbarItem.id;
          }
          break;
        case BulkEditTabTypesConstants.CLASSES:
          if (!bCanBulkEditClasses) {
            return oToolbarItem.id;
          }
          break;
      }
    });

    CS.forEach(oBulkEditToolbar, function (oData) {
      oData.isActive = oData.id === oBulkEditProps.getSelectedTabForBulkEdit();
    });
  };

  /**
   * Prepare data for Properties Tab of bulk edit dialog.
   * @returns {{leftPanelData: {properties: *[], selectedProperties: Array}, rightPanelData: {referencedAttributes: *, referencedTags: *, attributes, tags}}}
   */
  prepareBulkEditPropertiesTabData = () => {
    let oBulkEditProps = this.getComponentProps().bulkEditProps;
    return {
      leftPanelData: {
        properties: [
          {
            id: "attributes",
            label: "attributes",
            requestInfo: {
              entities: "attributes",
              requestType: "configData",
              requestURL: "config/configdata",
              responsePath: ["success", "attributes"],
              typesToExclude: ContentUtils.getAllExcludedAttributeTypeForBulkEdit(),
              isDisabled: false,
              entitiesToExclude: ['assetdownloadcountattribute', 'scheduleendattribute', 'schedulestartattribute']
            },
            customIconClassName: "attribute"
          },
          {
            id: "tags",
            label: "tags",
            requestInfo: {
              entities: "tags",
              requestType: "configData",
              requestURL: "config/configdata",
              responsePath: ["success", "tags"],
              typesToExclude: [],
              isDisabled: false
            },
            customIconClassName: "tag"
          }
        ],
        selectedProperties: this.getSelectedPropertiesForBulkEdit()
      },
      rightPanelData: {
        referencedAttributes: oBulkEditProps.getReferencedAttributes(),
        referencedTags: oBulkEditProps.getReferencedTags(),
        attributes: CS.sortBy(oBulkEditProps.getAppliedAttributes(), "label"),
        tags: CS.sortBy(oBulkEditProps.getAppliedTags(), "label")
      }
    }
  };

  /**
   * Prepare data for Taxonomies Tab of bulk edit dialog.
   * @returns {{referencedTaxonomies: *, taxonomySummary: *, treeViewModel: {treeData: *, searchText: *, treeLoadMoreMap: *, context: string, showLevelInHeader: boolean, showCustomTooltip: boolean}}}
   */
  prepareBulkEditTaxonomyTabData = () => {
    let oBulkEditProps = this.getComponentProps().bulkEditProps;
    return {
      referencedTaxonomies: oBulkEditProps.getReferencedTaxonomies(),
      taxonomySummary: oBulkEditProps.getTaxonomySummary(),
      treeViewModel: {
        treeData: TreeViewProps.getTreeData(),
        searchText: oBulkEditProps.getTreeSearchText(),
        treeLoadMoreMap: TreeViewProps.getTreeLoadMoreMap(),
        context: BulkEditTabTypesConstants.TAXONOMIES,
        showLevelInHeader: CS.isEmpty(oBulkEditProps.getTreeSearchText()),
        showCustomTooltip: true
      }
    }
  };

  /**
   * Prepare data for Classes Tab of bulk edit dialog.
   * @returns {{referencedTaxonomies: *, classSummary: *, treeViewModel: {treeData: *, searchText: *, treeLoadMoreMap: *, context: string, showLevelInHeader: boolean, showCustomTooltip: boolean}}}
   */
  prepareBulkEditClassesTabData = () => {
    let oBulkEditProps = this.getComponentProps().bulkEditProps;
    return {
      referencedTaxonomies: oBulkEditProps.getReferencedClasses(),
      classSummary: oBulkEditProps.getClassSummary(),
      treeViewModel: {
        treeData: TreeViewProps.getTreeData(),
        searchText: oBulkEditProps.getClassesTreeSearchText(),
        treeLoadMoreMap: TreeViewProps.getTreeLoadMoreMap(),
        context: BulkEditTabTypesConstants.CLASSES,
        showLevelInHeader: CS.isEmpty(oBulkEditProps.getClassesTreeSearchText()),
        showCustomTooltip: true
      }
    }
  };

  prepareDialogDataForProductsView = (oEntityViewData) => {
    let oComponentProps = this.getComponentProps();
    let oScreenProps = oComponentProps.screen;
    let oBulkEditProps = oComponentProps.bulkEditProps;
    let oContentGridProps = oComponentProps.contentGridProps;
    let oGoldenRecordProps = oComponentProps.goldenRecordProps;

    if (oScreenProps.getIsContentComparisonMode()) {
      oEntityViewData.comparisonViewData = this.getContentComparisonData();
    }
    else if (oBulkEditProps.getIsBulkEditViewOpen()) {
      oEntityViewData.bulkEditViewData = this.getBulkEditViewModel();
    }
    else  if (oContentGridProps.getIsContentGridEditViewOpen()) {
      oEntityViewData.contentGridEditData = this.getContentGridEditData();
    }
    else if(oGoldenRecordProps.getIsMatchAndMergeViewOpen()){
      oEntityViewData.comparisonViewData = this.getGoldenRecordRemergeSourcesViewData();
      oEntityViewData.isGoldenRecordMatchAndMergeViewOpen = ContentScreenProps.goldenRecordProps.getIsMatchAndMergeViewOpen();
    }
  };

  prepareDialogDataForProductOpenView = (oEntityViewData) => {
    let oComponentProps = this.getComponentProps();
    let oOpenedDialogAttributeData = oComponentProps.uomProps.getOpenedDialogAttributeData();
    let sOpenedDialogAttributeContextId = oOpenedDialogAttributeData.contextId;
    let oVariantSectionViewProps = oComponentProps.variantSectionViewProps;
    let oGoldenRecordProps = oComponentProps.goldenRecordProps;
    let bIsGoldenRecordViewSourcesDialogOpen = oGoldenRecordProps.getIsGoldenRecordViewSourcesDialogOpen();
    let bIsGoldenRecordRemergeSourcesTabClicked = oGoldenRecordProps.getIsGoldenRecordRemergeSourcesTabClicked();
    if (oVariantSectionViewProps.getIsCloningWizardOpen()) {
      /** @type Object - Cloning wizard data to show cloning view in edit view**/
      oEntityViewData.cloningWizardData = this.getCloningWizardViewData();
    }
    else if (sOpenedDialogAttributeContextId) {
      /** @type String - To show attribute variant information **/
      oEntityViewData.attributeContextDialogViewData = this.getAttributeContextDialogViewData();
    }
    else if (bIsGoldenRecordViewSourcesDialogOpen) {
      oEntityViewData.comparisonViewData = this.getContentComparisonData();
    }
    else if(bIsGoldenRecordRemergeSourcesTabClicked){
      oEntityViewData.comparisonViewData = this.getGoldenRecordRemergeSourcesViewData();
    }
  };

  prepareDialogDataForQuickList = (oEntityViewData) => {
    let oComponentProps = this.getComponentProps();
    let oVariantSectionViewProps = oComponentProps.variantSectionViewProps;
    if (oVariantSectionViewProps.getIsCloningWizardOpen()) {
      /** @type Object - Cloning wizard data to show cloning view in Quick list view**/
      oEntityViewData.cloningWizardData = this.getCloningWizardViewData();
    }
  };

  prepareHierarchyModeViewData = () => {
    let sSelectedHierarchyMode = ContentUtils.getSelectedHierarchyContext();
    let oComponentProps = this.getComponentProps();
    let oScreenProps = oComponentProps.screen;
    let oFilterContext = null;
    let oEntityViewData = {};
    let oExtraData = {};

    /** To prepare dialog data. */
    this.prepareDialogDataForProductsView(oEntityViewData);

    switch (sSelectedHierarchyMode) {
      case HierarchyTypesDictionary.TAXONOMY_HIERARCHY:
      case HierarchyTypesDictionary.COLLECTION_HIERARCHY:
        oFilterContext = {
          filterType: oFilterPropType.HIERARCHY,
          screenContext: sSelectedHierarchyMode,
        };
        break;
    }

    oEntityViewData.contentHierarchyData = this.getContentHierarchyData(oFilterContext);
    oEntityViewData.sortByViewData = this.getDefaultSortData();
    let oFilterProps = ContentUtils.getFilterProps(oFilterContext);
    oEntityViewData.violatingMandatoryElements = oFilterProps.getViolatingMandatoryElements();
    oEntityViewData.referencedTags = this.getReferencedTags();

    let {selectedEntityList: aSelectedEntityList, entityList: aEntityList} = this.getEntityListAndSelectedEntityList(sSelectedHierarchyMode);

    let {oFilterViewData, oFilterView, oNavigationData} = this.getFilterDataByFilterContext(oFilterContext, oExtraData);
    oEntityViewData.selectedEntityList = aSelectedEntityList;
    oEntityViewData.entityList = aEntityList;
    oEntityViewData.screen = "hierarchy";
    oEntityViewData.referencedAssets = oScreenProps.getReferencedAssetList();

    return {oEntityViewData, oFilterViewData, oFilterView, oNavigationData};
  };

  prepareDataForDashboardTabs = () => {
    let oComponentProps = this.getComponentProps();
    let oScreenProps = oComponentProps.screen;
    let sSelectedDashboardTabId = oScreenProps.getSelectedDashboardTabId();
    let sSessionPortalId = CommonUtils.getSelectedPortalId();

    switch (sSelectedDashboardTabId) {
      case DashboardTabDictionary.INFORMATION_TAB:
        return this.getInformationTabViewData();

      case DashboardTabDictionary.DAM_TAB:
        return sSessionPortalId === PortalTypeDictionary.PIM ? this.getDamInformationTabViewData() : null;

      case DashboardTabDictionary.BUCKETS_TAB:
        let {oEntityViewData = {}, oFilterViewData = {}, oFilterView = null, oNavigationData = {}} = this.prepareDataForProductsView();
        oEntityViewData.selectedDashboardTabId = oScreenProps.getSelectedDashboardTabId();
        return {oEntityViewData, oFilterViewData, oFilterView, oNavigationData};
    }
    return {};
  };

  prepareDataForModuleScreen = (oActiveBreadcrumbNode) => {
    if (oActiveBreadcrumbNode.id === BreadCrumbModuleAndHelpScreenIdDictionary.DASHBOARD) {
      return this.prepareDataForDashboardTabs();
    }
    let oTaxonomyTreeRequestData = {
      url: getRequestMapping().GetAllKlassTaxonomies
    };
    return this.prepareDataForProductsView({taxonomyTreeRequestData: oTaxonomyTreeRequestData});
  };

  prepareDataForProductsView = (oExtraData) => {
    let sViewMode = ContentUtils.getViewMode();

    switch (sViewMode) {
      case ContentScreenConstants.viewModes.LIST_MODE:
      case ContentScreenConstants.viewModes.TILE_MODE:
        return this.prepareDataForListOrTileMode(oExtraData)
    }
  };

  /**
   * Prepare data for content quick list
   * @return {{oEntityViewData: {screen: string, userList: *, activeEntity: *, selectedEntityList: *, entityList: *, selectedRelationshipElements: *, addEntityInRelationshipScreenData: *, availableEntityView: *, availableEntityViewVisibilityStatus: (boolean|*), availableEntityParentViewVisibilityStatus: *, availableEntityViewContext: *, natureSectionViewData: Array, contextSelectedEntities: Array, selectedVariantIds: Array}}}
   */
  getContentQuickListViewData = () => {
    let oComponentProps = this.getComponentProps();
    let oScreenProps = oComponentProps.screen;
    let aContextSelectedEntities = [];
    let aSelectedVariantIds = [];
    let oActiveEntity = this.getActiveEntity();
    let sAvailableEntityViewContext = oComponentProps.availableEntityViewProps.getAvailableEntityViewContext();
    let oAddEntityInRelationshipScreenData = oScreenProps.getAddEntityInRelationshipScreenData();
    let oVariantSectionViewProps = oComponentProps.variantSectionViewProps;
    let bAvailableEntityChildViewVisibilityStatus = oVariantSectionViewProps.getAvailableEntityChildVariantViewVisibilityStatus();
    let bAvailableEntityParentViewVisibilityStatus = oVariantSectionViewProps.getAvailableEntityParentVariantViewVisibilityStatus();
    let oAvailableEntityView = null;
    let sContext = "";
    let bAvailableEntityViewForTableContext = false;
    let oAvailableEntityData = {};


    let {selectedEntityList: aSelectedEntityList, entityList: aEntityList} = this.getEntityListAndSelectedEntityList(sContext);

    /** TODO: Refactor - prepare quickList data only*/

    let oEntityViewData = {
      userList: this.getAppData().getUserList(),
      activeEntity: oActiveEntity,
      selectedEntityList: aSelectedEntityList,
      entityList: aEntityList,
      selectedRelationshipElements: oComponentProps.relationshipView.getSelectedRelationshipElements(),

      /**TODO: THINK TO REMOVE IT**/
      addEntityInRelationshipScreenData: oAddEntityInRelationshipScreenData,

      /** @type Boolean - To show available entity list quick list **/
      availableEntityViewVisibilityStatus: bAvailableEntityViewForTableContext || bAvailableEntityChildViewVisibilityStatus,
      availableEntityParentViewVisibilityStatus: bAvailableEntityParentViewVisibilityStatus,

      /** @type Array - Nature Relationship Data Preparation **/
      natureSectionViewData: this.getNatureSectionViewData(),

      /** @type Array - Linked entities info to show (e.g Market, Article, Asset etc.) in Context**/
      contextSelectedEntities: aContextSelectedEntities,
      /** @type Array - Selected Linked entities info to show (e.g Market, Article, Asset etc.) in Context**/
      selectedVariantIds: aSelectedVariantIds
    };

    CS.assign(oEntityViewData, this.getArticleViewData());

    let screen = "";
    let oExtraData = {
      taxonomyTreeRequestData: this.getTaxonomyTreeRequestDataForRelationshipQuickList()
    };
    if (oAddEntityInRelationshipScreenData[oActiveEntity.id] && oAddEntityInRelationshipScreenData[oActiveEntity.id].status) {
      oAvailableEntityData = this.getAvailableEntityDataForQuickList(oAddEntityInRelationshipScreenData, oExtraData);
      screen = BreadCrumbModuleAndHelpScreenIdDictionary.QUICKLIST;
      if (oAddEntityInRelationshipScreenData[oActiveEntity.id].context === ContextTypeDictionary.PRODUCT_VARIANT) {
        sAvailableEntityViewContext = "productVariantQuickList";
      }
    }
    oEntityViewData.availableEntityData = oAvailableEntityData;
    oEntityViewData.availableEntityViewContext = sAvailableEntityViewContext;
    oEntityViewData.screen = screen;

    /**To prepare dialog data */
    this.prepareDialogDataForQuickList(oEntityViewData);

    return {oEntityViewData};
  };

  getVariantQuickListViewData = () => {
    let oComponentProps = this.getComponentProps();
    let oScreenProps = oComponentProps.screen;
    let aContextSelectedEntities = [];
    let aSelectedVariantIds = [];
    let oActiveEntity = this.getActiveEntity();
    let sAvailableEntityViewContext = oComponentProps.availableEntityViewProps.getAvailableEntityViewContext();
    let oAddEntityInRelationshipScreenData = oScreenProps.getAddEntityInRelationshipScreenData();
    let oVariantSectionViewProps = oComponentProps.variantSectionViewProps;
    let bAvailableEntityChildViewVisibilityStatus = oVariantSectionViewProps.getAvailableEntityChildVariantViewVisibilityStatus();
    let bAvailableEntityParentViewVisibilityStatus = oVariantSectionViewProps.getAvailableEntityParentVariantViewVisibilityStatus();
    let oUOMProps = oComponentProps.uomProps;
    let sOpenedDialogTableContextId = oUOMProps.getOpenedDialogTableContextId();
    let oAvailableEntityView = null;
    let sContext = "";
    let sSelectedEntity = oVariantSectionViewProps.getSelectedEntity();
    let bAvailableEntityViewForTableContext = false;
    let oAvailableEntityData = {};
    let oExtraData = {
      taxonomyTreeRequestData: {
        url: getRequestMapping().GetAllTaxonomiesByLeafIdsForVariantQuicklist,
        moduleEntities: [oVariantSectionViewProps.getSelectedEntity()]
      }
    };

    if (!CS.isEmpty(sOpenedDialogTableContextId)) {
      /** Executes on variant create **/
      let oDialogVariant = this.getUOMDialogVariant(sOpenedDialogTableContextId);
      let oLinkedInstances = ViewUtils.getLinkedInstancesFromVariant(oDialogVariant);
      aContextSelectedEntities = oLinkedInstances[sSelectedEntity] || [];
      sContext = "contextEntity";
      aSelectedVariantIds = oVariantSectionViewProps.getSelectedVariantInstances();
      // required for Article/Asset page pagination
      let oFilterContext = {
        filterType: oFilterPropType.QUICKLIST,
        screenContext: oFilterPropType.QUICKLIST
      };
      oAvailableEntityData = this.getAvailableEntityData(sContext, true, false, oFilterContext, {}, {}, oExtraData);
    }
    else if (oComponentProps.availableEntityViewProps.getAvailableEntityViewContext() === ContentScreenViewContextConstants.TABLE_CONTEXT_AVAILABLE_ENTITY_VIEW) {
      /** for limited object (add entities in table cell) **/
      /** Executes on variant update **/
      let oActivePopOverContextEntityData = this.getTableActivePopOverContextEntityData();
      aContextSelectedEntities = oActivePopOverContextEntityData.referencedLinkedEntites;
      aSelectedVariantIds = oComponentProps.availableEntityViewProps.getSelectedRightPanelEntities();
      bAvailableEntityViewForTableContext = true;
      // required for Article/Asset page pagination for inline editing
      let oFilterContext = {
        filterType: oFilterPropType.QUICKLIST,
        screenContext: oFilterPropType.QUICKLIST
      };
      oAvailableEntityData = this.getAvailableEntityData(oComponentProps.availableEntityViewProps.getAvailableEntityViewContext(), true, false, oFilterContext, {}, {}, oExtraData);
    }
    else if (bAvailableEntityChildViewVisibilityStatus) {
      /** context without limited object **/
      let oDummyVariant = ContentUtils.getActiveContentOrVariant();
      oDummyVariant = oDummyVariant && oDummyVariant.contentClone || oDummyVariant;
      let oLinkedInstances = ViewUtils.getLinkedInstancesFromVariant(oDummyVariant);
      aContextSelectedEntities = oLinkedInstances[sSelectedEntity] || [];
      sContext = "contextEntity";
      aSelectedVariantIds = oVariantSectionViewProps.getSelectedVariantInstances();
      // required for Article/Asset page pagination
      let oFilterContext = {
        filterType: oFilterPropType.QUICKLIST,
        screenContext: oFilterPropType.QUICKLIST
      };
      oAvailableEntityData = this.getAvailableEntityData(sContext, true, false, oFilterContext);
    }
    let {selectedEntityList: aSelectedEntityList, entityList: aEntityList} = this.getEntityListAndSelectedEntityList(sContext);

    let oPresentEntityViewData = {
      filterContext: {
        filterType: oFilterPropType.QUICKLIST,
        screenContext: oFilterPropType.QUICKLIST
      }
    };
    let oEntityViewData = {
      presentEntityViewData: oPresentEntityViewData,
      screen: BreadCrumbModuleAndHelpScreenIdDictionary.VARIANT_QUICKLIST,
      userList: this.getAppData().getUserList(),
      activeEntity: oActiveEntity,
      selectedEntityList: aSelectedEntityList,
      entityList: aEntityList,
      selectedRelationshipElements: oComponentProps.relationshipView.getSelectedRelationshipElements(),

      /**TODO: THINK TO REMOVE IT**/
      addEntityInRelationshipScreenData: oAddEntityInRelationshipScreenData,

      /** @type Object - Quicklist View **/
      availableEntityData: oAvailableEntityData,
      /** @type Boolean - To show available entity list quick list **/
      availableEntityViewVisibilityStatus: bAvailableEntityViewForTableContext || bAvailableEntityChildViewVisibilityStatus,
      availableEntityParentViewVisibilityStatus: bAvailableEntityParentViewVisibilityStatus,
      availableEntityViewContext: sAvailableEntityViewContext,

      /** @type Array - Nature Relationship Data Preparation **/
      natureSectionViewData: this.getNatureSectionViewData(),

      /** @type Array - Linked entities info to show (e.g Market, Article, Asset etc.) in Context**/
      contextSelectedEntities: aContextSelectedEntities,
      /** @type Array - Selected Linked entities info to show (e.g Market, Article, Asset etc.) in Context**/
      selectedVariantIds: aSelectedVariantIds,
    };

    CS.assign(oEntityViewData, this.getArticleViewData());
    return {oEntityViewData};
  };

  getCollectionQuickListViewData = () => {
    let oActiveCollection = this.getActiveCollection();
    let bIsFilterViewVisible = !this.isHideUpperSearchBar();
    let oFilterContext = {
      filterType: oFilterPropType.QUICKLIST,
      screenContext: oFilterPropType.QUICKLIST
    };

    let oEntityViewData = this.prepareEntityViewData();
    oEntityViewData.screen = BreadCrumbModuleAndHelpScreenIdDictionary.STATIC_COLLECTION_QUICKLIST;
    if (bIsFilterViewVisible) {
      let oTaxonomyTreeRequestData = this.getTaxonomyTreeRequestDataForStaticCollection();
      oTaxonomyTreeRequestData.isQuicklist = true;
      let oExtraData = {
        taxonomyTreeRequestData: oTaxonomyTreeRequestData
      };
      oEntityViewData.availableEntityData = this.getAvailableEntityData("staticCollection", true, false, oFilterContext, {}, {}, oExtraData);
      /** for pagination in present entity view **/
      let oFilterContextForPresentEntity = {
        filterType: oFilterPropType.COLLECTION,
        screenContext: oActiveCollection.id
      };
      let oNavigationData = this.getNavigationData(oFilterContextForPresentEntity);
      return {
        oEntityViewData,
        oFilterViewData: {filterContext: oFilterContextForPresentEntity},
        oFilterView: {},
        oNavigationData
      };
    }
    return {oEntityViewData};
  };

  prepareDataForDIModules = (oActiveBreadcrumbNode) => {
    let bIsDashboardFilesModule = ContentUtils.getSelectedModuleId() === ModuleDictionary.FILES;
    if(bIsDashboardFilesModule) {
      return this.prepareDataForDashboardFilesModule();
    }
    else if (oActiveBreadcrumbNode.id === ModuleDictionary.JOSSTATUS) {
      return this.getJobViewData();
    }
    else {
      let oTaxonomyTreeRequestData = {
        url: getRequestMapping().GetAllKlassTaxonomies
      };
      return this.prepareDataForProductsView({taxonomyTreeRequestData: oTaxonomyTreeRequestData});
    }
  };

  getTaxonomyTreeRequestDataForStaticCollection = () => {
    let oActiveCollection = this.getActiveCollection();
    return {
      collectionId: oActiveCollection.id,
      url: getRequestMapping().GetAllTaxonomiesForStaticCollection
    }
  };

  getTaxonomyTreeRequestDataForRelationshipQuickList = () => {
    let oComponentProps = this.getComponentProps();
    let oTaxonomyTreeRequestData = {};
    let oFilterContext = {
      filterType: oFilterPropType.QUICKLIST,
      screenContext: oFilterPropType.QUICKLIST
    };
    let oFilterProps = ContentUtils.getFilterProps(oFilterContext);
    let sSelectedParentId = oFilterProps.getSelectedOuterParentId();
    let sSelectedOuterParentTaxonomyId = sSelectedParentId == "-1" ? "" : sSelectedParentId;
    let sClickedTaxonomyId = CS.isEmpty(sSelectedOuterParentTaxonomyId) ? null : sSelectedOuterParentTaxonomyId;
    let oSectionSelectionStatus = oComponentProps.screen.getSetSectionSelectionStatus();
    let sRelationshipSectionId = oSectionSelectionStatus['selectedRelationship'].id;
    ContentUtils.getAdditionalDataForRelationshipCalls(oTaxonomyTreeRequestData);
    oTaxonomyTreeRequestData.url = ContentUtils.getURLForTaxonomiesForRelationship(sClickedTaxonomyId, sRelationshipSectionId);
    return oTaxonomyTreeRequestData
  };

  /**
   * To prepare data according to screen eg. Module screen, Content open screen, QuickList, Collection screen etc.
   * Breadcrumb define which screen is opened.
   * @return {*}
   */
  prepareDataAccordingToScreen = () => {
    let oBreadCrumbProps = ContentScreenProps.breadCrumbProps;
    let aBreadCrumbData = oBreadCrumbProps.getBreadCrumbData();
    let oActiveBreadcrumbNode = aBreadCrumbData[aBreadCrumbData.length - 1];
    let oTaxonomyTreeRequestData = {};

    switch ( oActiveBreadcrumbNode && oActiveBreadcrumbNode.type) {
      case  BreadCrumbModuleAndHelpScreenIdDictionary.MODULE:
        return this.prepareDataForModuleScreen(oActiveBreadcrumbNode);
      case BreadCrumbModuleAndHelpScreenIdDictionary.CONTENT:
        return this.getEntityEditViewData() || {};
      case BreadCrumbModuleAndHelpScreenIdDictionary.QUICKLIST:
        return this.getContentQuickListViewData();
      case BreadCrumbModuleAndHelpScreenIdDictionary.VARIANT_QUICKLIST:
        return this.getVariantQuickListViewData();
      case BreadCrumbModuleAndHelpScreenIdDictionary.STATIC_COLLECTION:
        oTaxonomyTreeRequestData = this.getTaxonomyTreeRequestDataForStaticCollection();
        return this.prepareDataForProductsView({taxonomyTreeRequestData: oTaxonomyTreeRequestData});
      case BreadCrumbModuleAndHelpScreenIdDictionary.DYNAMIC_COLLECTION:
        oTaxonomyTreeRequestData = {
          url: getRequestMapping().GetAllKlassTaxonomies
        };
        return this.prepareDataForProductsView({taxonomyTreeRequestData: oTaxonomyTreeRequestData});
      case BreadCrumbModuleAndHelpScreenIdDictionary.STATIC_COLLECTION_QUICKLIST:
        return this.getCollectionQuickListViewData();
      case BreadCrumbModuleAndHelpScreenIdDictionary.COLLECTION_HIERARCHY:
      case BreadCrumbModuleAndHelpScreenIdDictionary.TAXONOMY_HIERARCHY:
        return this.prepareHierarchyModeViewData();
        // Dashboard screens
      case BreadCrumbModuleAndHelpScreenIdDictionary.DASHBOARD_RULE_VIOLATION:
      case BreadCrumbModuleAndHelpScreenIdDictionary.EXPLORER_KPI_TILE:
      case BreadCrumbModuleAndHelpScreenIdDictionary.ASSET_RULE_VIOLATION:
        oTaxonomyTreeRequestData = {
          url: getRequestMapping().GetAllKlassTaxonomies
        };
        return this.prepareDataForProductsView({taxonomyTreeRequestData: oTaxonomyTreeRequestData});
      case BreadCrumbModuleAndHelpScreenIdDictionary.DIMODULE:
        return this.prepareDataForDIModules(oActiveBreadcrumbNode);
    }
    return null;
  };

  getInformationTabViewData = () => {
    let oComponentProps = this.getComponentProps();
    let oScreenProps = oComponentProps.screen;
    let oInformationTabProps = oComponentProps.informationTabProps;
    let oFilterProps = ContentUtils.getFilterProps({screenContext: oFilterPropType.MODULE, filterType: oFilterPropType.MODULE});
    let aLastModifiedContents = oInformationTabProps.getLastModifiedContentsList();
    let aLatestCreatedContents = oInformationTabProps.getLatestCreatedContentsList();
    let oDashBoardInformationTabData = {};
    oDashBoardInformationTabData.dashboardTabData = {
      lastModifiedContentsList: aLastModifiedContents,
      latestCreatedContentsList: aLatestCreatedContents,
      isLastModifiedListLoading: oInformationTabProps.getLastModifiedListLoading(),
      isLatestCreatedListLoading: oInformationTabProps.getLatestCreatedListLoading(),
    };
    let aAvailableFilterData = CS.sortBy(oFilterProps.getAvailableFilters(), [function (oAvailableFilterData) {
      return CS.getLabelOrCode(oAvailableFilterData).toLowerCase();
    }]);
    oDashBoardInformationTabData.dataRuleFilterData = CS.find(aAvailableFilterData, {id: "colorVoilation"});
    oDashBoardInformationTabData.dashboardRuleViolationTileData = oInformationTabProps.getDashboardRuleViolationTileData();
    oDashBoardInformationTabData.showBulkUploadDialog = oInformationTabProps.getShowBulkUploadDialog();
    oDashBoardInformationTabData.assetClassList = oInformationTabProps.getAssetClassList();
    oDashBoardInformationTabData.context = DashboardTabDictionary.INFORMATION_TAB;
    oDashBoardInformationTabData.requestURLForAssetDefaultTypes = getRequestMapping().GetDefaultTypes;

    let oEntityViewData = {
      selectedDashboardTabId: oScreenProps.getSelectedDashboardTabId(),
      dashboardTabData: oDashBoardInformationTabData,
    };
    return {oEntityViewData};
  };

  getDamInformationTabViewData = () => {
    let oComponentProps = this.getComponentProps();
    let oScreenProps = oComponentProps.screen;
    let oDamInformationTabProps = oComponentProps.damInformationTabProps;
    let oFilterProps = ContentUtils.getFilterProps({screenContext: oFilterPropType.MODULE, filterType: oFilterPropType.MODULE});
    let aLastModifiedAssets = oDamInformationTabProps.getLastModifiedAssetList();
    let aLatestCreatedAssets = oDamInformationTabProps.getLatestCreatedAssetList();
    let aExpiredAssetList = oDamInformationTabProps.getExpiredAssetList();
    let aAssetAboutToExpireList = oDamInformationTabProps.getAssetAboutToExpireList();
    let aDuplicateAssetsList = oDamInformationTabProps.getDuplicateAssetsList();
    let oDashBoardDamTabData = {};
    oDashBoardDamTabData.dashboardTabData = {
      lastModifiedAssetList: aLastModifiedAssets,
      latestCreatedAssetList: aLatestCreatedAssets,
      expiredAssetList: aExpiredAssetList,
      assetAboutToExpire: aAssetAboutToExpireList,
      duplicateAssetsList: aDuplicateAssetsList,
    };
    let aAvailableFilterData = CS.sortBy(oFilterProps.getAvailableFilters(), [function (oAvailableFilterData) {
      return CS.getLabelOrCode(oAvailableFilterData).toLowerCase();
    }]);
    oDashBoardDamTabData.dataRuleFilterData = CS.find(aAvailableFilterData, {id: "colorVoilation"});
    oDashBoardDamTabData.dashboardRuleViolationTileData = oDamInformationTabProps.getDashboardRuleViolationTileData();
    oDashBoardDamTabData.showBulkUploadDialog = oDamInformationTabProps.getShowBulkUploadDialog();
    oDashBoardDamTabData.assetClassList = oDamInformationTabProps.getAssetClassList();
    oDashBoardDamTabData.context = DashboardTabDictionary.DAM_TAB;
    oDashBoardDamTabData.requestURLForAssetDefaultTypes = getRequestMapping().GetDefaultTypes;

    let oEntityViewData = {
      selectedDashboardTabId: oScreenProps.getSelectedDashboardTabId(),
      dashboardTabData: oDashBoardDamTabData
    };
    return {oEntityViewData};
  };

  getRuntimeMappingView = () => {
    let oMappingDialogData = this.prepareFileMappingViewData();
    return (
        <OnBoardingFileMappingView
            unmappedColumnValuesList={oMappingDialogData.unmappedColumnValuesList}
            activeTabId={oMappingDialogData.activeTabId}
            selectedMappingFilterId={oMappingDialogData.selectedMappingFilterId}
            mappingFilterProps={oMappingDialogData.mappingFilterProps}
            activeEndpoint={oMappingDialogData.activeEndpoint}
            endPointReqResInfo={oMappingDialogData.endPointReqResInfo}
            propertyRowTypeData={oMappingDialogData.propertyRowTypeData}
            endPointReferencedData={oMappingDialogData.endPointReferencedData}
            endPointMappingList={oMappingDialogData.endPointMappingList}
        />
    );
  };

  getRuntimeMappingDialogView = () => {
    let oComponentProps = this.getComponentProps();
    if (SessionProps.getSessionPhysicalCatalogId() === PhysicalCatalogDictionary.PIM &&
        oComponentProps.screen.getOnboardingFileMappingViewVisibilityStatus()) {
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
      return (
          <CustomDialogView
              open={true}
              bodyStyle={oBodyStyle}
              contentStyle={oContentStyle}
          >
            {this.getRuntimeMappingView()}
          </CustomDialogView>
      );
    } else {
      return null;
    }
  };

  getModulesList = () => {
    let aSelectedModules = GlobalStore.getAllMenus();
    let oComponentProps = this.getComponentProps();
    let bIsInsideDataIntegration = this.props.isInsideDataIntegration;
    if (bIsInsideDataIntegration) {
      let oDataIntegrationInfo = ContentUtils.getDataIntegrationInfo();
      let sSelectedCatalogId = oComponentProps.endPointMappingViewProps.getSelectedPhysicalCatalogId();
      let aCommonEndpointModule = [ModuleDictionary.PIM, ModuleDictionary.MAM,
                                   ModuleDictionary.TARGET, ModuleDictionary.SUPPLIER, ModuleDictionary.TEXT_ASSET];
      let aEndpointModule = [];
      if (oDataIntegrationInfo.endpointType === EndpointTypeDictionary.INBOUND_ENDPOINT) {
        aEndpointModule = [ModuleDictionary.FILES, ModuleDictionary.JOSSTATUS];
        if (sSelectedCatalogId === PhysicalCatalogDictionary.DATAINTEGRATION) {
          aEndpointModule = CS.concat(aEndpointModule, aCommonEndpointModule);
        }
      } else {
        aEndpointModule = [ModuleDictionary.JOSSTATUS];
        if (sSelectedCatalogId === PhysicalCatalogDictionary.DATAINTEGRATION) {
          aEndpointModule = CS.concat(aEndpointModule, aCommonEndpointModule);
        }
      }

      aSelectedModules = CS.filter(aSelectedModules, oSelectedModule => {
        return CS.includes(aEndpointModule, oSelectedModule.id);
      })
    }

    return aSelectedModules;
  };

  getVisibilityRelatedProps = () => {
    let oComponentProps = this.getComponentProps();
    let oScreenProps = oComponentProps.screen;
    let bIsKpiContentExplorerOpen = oScreenProps.getIsKpiContentExplorerOpen();
    let bIsInformationTabRuleViolationTileClicked = ContentUtils.getIsRuleViolatedContentsScreen();
    let bIsDamInformationTabRuleViolationTileClicked = ContentUtils.getIsDamRuleViolatedContentsScreen();
    let oActiveEntity = ContentUtils.getActiveContent();
    let bIsHierarchyViewMode = CS.isNotEmpty(ContentUtils.getSelectedHierarchyContext());
    let oActiveCollection = this.getActiveCollection();

    let bShowModuleSidebar = !(bIsKpiContentExplorerOpen || bIsInformationTabRuleViolationTileClicked ||
        bIsDamInformationTabRuleViolationTileClicked || bIsHierarchyViewMode ||
        !CS.isEmpty(oActiveCollection));

    let bHideHeader = !CS.isEmpty(oActiveEntity) || ContentUtils.getIsHierarchyViewMode() || ContentUtils.isCollectionScreen();

    /** show runtime view even if the currently selected module is dashboard **/
    let bIsInsideDataIntegration = this.props.isInsideDataIntegration;
    let bForceShowRuntimeView = (
        bIsInsideDataIntegration
        || ContentUtils.getSelectedModuleId() !== ModuleDictionary.DASHBOARD
        || bIsKpiContentExplorerOpen
        || bIsInformationTabRuleViolationTileClicked
        || bIsDamInformationTabRuleViolationTileClicked
        || !CS.isEmpty(oActiveEntity)
        || !CS.isEmpty(oActiveCollection)
    );

    return {
      showModuleSidebar: bShowModuleSidebar,
      showBreadCrumb: true,
      hideHeader: bHideHeader,
      forceShowRuntimeView: bForceShowRuntimeView
    }
  };

  getWorkflowWorkbenchData = () => {
    let oComponentProps = this.getComponentProps();
    let oTaskData = oComponentProps.taskProps.getTaskData();
    let sSelectedModuleId = ContentUtils.getSelectedModuleId();
    oTaskData.currentUser = ContentUtils.getCurrentUser();
    oTaskData.usersList = ContentUtils.getUserList();
    oTaskData.selectedModuleId = sSelectedModuleId;

    let aEntityList = [ConfigDataEntitiesDictionary.USERS, ConfigDataEntitiesDictionary.ROLES];
    let aGroups = [];
    CS.forEach(aEntityList, entity => {
      let [id, label, resPath, paginationData] = ["", "", [], {}];
      switch (entity) {
        case ConfigDataEntitiesDictionary.USERS:
          id = "users";
          label = getTranslation().USERS;
          resPath = ["success", entity];
          paginationData = {
            sortBy: 'firstName',
            searchColumn: 'firstName',
          };
          break;
        case ConfigDataEntitiesDictionary.ROLES:
          id = "roles";
          label = getTranslation().ROLES
          resPath = ["success", entity];
          break;
      }
      var oRequestData = {
        id: id,
        label: label,
        requestInfo: {
          requestType: "configData",
          responsePath: resPath,
          entities: entity,
          requestURL:"config/rolesconfigdata",
          paginationData: paginationData,
          customRequestModel: {},
          customRequestInfoModel: {
            organizationId : SessionProps.getSessionOrganizationId()
          },
        }
      }
      aGroups.push(oRequestData);
    });

    oTaskData.taskRolesData = aGroups;

    return oTaskData;
  };

  prepareRuntimeData = () => {
    let oComponentProps = this.getComponentProps();
    let oScreenProps = oComponentProps.screen;
    let sSelectedDashboardTabId = oScreenProps.getSelectedDashboardTabId();
    let oVisibilityProps = this.getVisibilityRelatedProps();
    let sSelectedModuleId = ContentUtils.getSelectedModuleId();
    let aBreadcrumbPath = oVisibilityProps.showBreadCrumb ? oComponentProps.breadCrumbProps.getBreadCrumbData() : [];
    let bPreventDashboardDataReset = oScreenProps.getPreventDashboardDataReset();
    let bIsInsideDataIntegration = this.props.isInsideDataIntegration;
    let oTaskProps = oComponentProps.taskProps;
    let oActiveTask = oTaskProps.getActiveTask();

    let {oEntityViewData = {}, oFilterViewData = {}, oFilterView = null, oNavigationData = {}} = this.prepareDataAccordingToScreen() || {};
    let bCreateToolbarData = !oScreenProps.getIsEditMode();
    let oToolbarData = {};
    if (bCreateToolbarData) {
      oToolbarData =  this.getToolbarData(false, "", oFilterViewData.filterContext);
      oToolbarData = !CS.isEmpty(oToolbarData) && this.getCustomToolbarData(oToolbarData);
    }
    let sThumbNailMode = oScreenProps.getThumbnailMode();
    let oRuleViolation = oScreenProps.getRuleViolationObject();
    let oScreenViewsMasterData = this.getScreenViewsMasterData();
    let oContentThumbContainerViewMasterData = oScreenViewsMasterData.contentThumbContainerViewMasterData;

    let sSelectedEntityTabId = ContentUtils.getSelectedTabId();
    let oViewConfigData = this.props.viewConfigData;
    let oActiveEntity = this.getActiveContent();
    let bIsSidePanelExpanded = CS.isEmpty(oActiveEntity) ? oViewConfigData.isLandingPageExpanded : oViewConfigData.isProductInfoPageExpanded;
    let sKey = CS.isEmpty(oActiveEntity) ? 'landingPage' : 'productInfoiPage';
    let oDataIntegrationLogsViewData = DashboardScreenProps.getDataIntegrationLogsViewData();
    oDataIntegrationLogsViewData.activeJob = JobScreenProps.getActiveJob();
    oDataIntegrationLogsViewData.activeJobGraphData = JobScreenProps.getActiveJobGraphData();

    return {
      breadCrumbPath: aBreadcrumbPath,
      upperSectionToolbarData: this.getUpperSectionToolbarData(),
      moduleList: this.getModulesList(),
      selectedModuleId: sSelectedModuleId,
      forceShowRuntimeView: oVisibilityProps.forceShowRuntimeView,
      showModuleSidebar: oVisibilityProps.showModuleSidebar,
      preventDashboardDataReset: bPreventDashboardDataReset,
      isInsideDataIntegration: bIsInsideDataIntegration,
      runtimeMappingDialogView: this.getRuntimeMappingDialogView(),
      taskData: sSelectedDashboardTabId === DashboardTabDictionary.WORKFLOW_WORKBENCH_TAB ? this.getWorkflowWorkbenchData() : null,
      relationshipViewMode: oScreenProps.getRelationshipViewMode(),
      activeXRayPropertyGroup: ContentUtils.getActiveXRayPropertyGroup(),
      xRayData: this.getXRayData(),
      variantSectionViewData: oScreenProps.getIsEditMode() ? this.getVariantSectionViewData() : {},
      relationshipContextData: oComponentProps.screen.getRelationshipContextData(),
      isHierarchyMode: this.isHierarchyModeSelected(),
      selectedImageId: oScreenProps.getSelectedImageId(),
      showGoldenRecordBuckets: oComponentProps.goldenRecordProps.getShowGoldenRecordBuckets(),
      entityHeaderTagsEditData: this.getEntityHeaderTagsEditData(),
      toolbarData: oToolbarData,
      viewMode: ContentUtils.getViewMode(),
      currentZoom: oScreenProps.getCurrentZoom(),
      ruleViolation: oRuleViolation,
      thumbnailMode: sThumbNailMode,
      viewMasterData: oContentThumbContainerViewMasterData,
      entityViewData: oEntityViewData,
      filterViewData: oFilterViewData,
      filterView: oFilterView,
      navigationData: oNavigationData,
      selectedEntityTabId: sSelectedEntityTabId,
      isSidePanelExpanded: bIsSidePanelExpanded,
      activeTask: oActiveTask,
      key: sKey,
      canClone: oScreenProps.getFunctionalPermission().canClone,
      dataIntegrationLogsViewData:oDataIntegrationLogsViewData
    }
  };

  render = () => {

    /**
     * promisedData includes multiple processed data in controller through various function calls
     *
     * e.g {@link this.getEntityTabData} method gets called multiple times in controller doing some processing
     * and producing the same result again and again.
     * So storing the result once produces in promised data and then referring the old produced result for
     * the next function call in the controller life span for each render
     *
     * @type {Object}
     */
    this.promisedData = {};
    let oData = this.prepareRuntimeData();

    return (
        <ContextList.Provider value={this.getChildContext()}>
          <ContentScreenWrapperView {...oData}/>
        </ContextList.Provider>
    );
  }
}

export default ContentScreenController;
