import CS from '../../../../../../libraries/cs';

import alertify from '../../../../../../commonmodule/store/custom-alertify-store';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import ScreenModeUtils from './screen-mode-utils';
import ContentUtils from './content-utils';
import MNMUtils from './match-and-merge-utils';
import GoldenRecordProps from './../model/golden-record-props';
import GoldenRecordBucketTabs from '../../../../../../commonmodule/tack/golden-record-bucket-tabs';
import ContentScreenRequestMapping from '../../tack/content-screen-request-mapping';
import ContentScreenProps from './../model/content-screen-props';
import ContentScreenConstants from './../model/content-screen-constants';
import DashboardScreenProps from './../../screens/dashboardscreen/store/model/dashboard-screen-props';
import BreadCrumbModuleAndHelpScreenIdDictionary from '../../../../../../commonmodule/tack/breadcrumb-module-and-help-screen-id-dictionary';
import SessionProps from './../../../../../../commonmodule/props/session-props';
import GoldenRecordLanguageSelectionSkeleton from '../../tack/golden-record-language-selection-skeleton';
import { gridViewPropertyTypes as oGridViewPropertyTypes } from './../../../../../../viewlibraries/tack/view-library-constants';
import ExceptionLogger from '../../../../../../libraries/exceptionhandling/exception-logger';
import CustomActionDialogStore from '../../../../../../commonmodule/store/custom-action-dialog-store';
import GRConstants from '../../tack/golden-record-view-constants';
import BreadcrumbStore from '../../../../../../commonmodule/store/helper/breadcrumb-store';
import oFilterPropType from '../../tack/filter-prop-type-constants';
import FilterStoreFactory from './filter-store-factory';
import ClassNameFromBaseTypeDictionary from '../../../../../../commonmodule/tack/class-name-base-types-dictionary';
import DashboardTabDictionary from './../../screens/dashboardscreen/tack/dashboard-tab-dictionary';
import HierarchyTypesDictionary from "../../../../../../commonmodule/tack/hierarchy-types-dictionary";
import SessionStorageManager from "../../../../../../libraries/sessionstoragemanager/session-storage-manager";
import SessionStorageConstants from "../../../../../../commonmodule/tack/session-storage-constants";
import MomentUtils from "../../../../../../commonmodule/util/moment-utils";
import NumberUtils from "../../../../../../commonmodule/util/number-util";
import TagTypeConstants from "../../../../../../commonmodule/tack/tag-type-constants";
import BaseTypesDictionary from "../../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary";
var getRequestMapping = ScreenModeUtils.getScreenRequestMapping;
var getTranslation = ScreenModeUtils.getTranslationDictionary;

var GoldenRecordStore = (function () {

  const
      ATTRIBUTE                 = "attribute",
      TAG                       = "tag",
      RELATIONSHIP              = "relationship",
      NATURE_RELATIONSHIP       = "natureRelationship",
      RELATIONSHIP_TABLE        = "relationshipTable",
      NATURE_RELATIONSHIP_TABLE = "natureRelationshipTable",
      TYPES                     = "types",
      TAXONOMIES                = "taxonomies",
      LANGUAGE                  = "language",
      DEFAULT_LANGUAGE          = "defaultLanguage",
      BASIC_INFO                = "basicInfo";

  var _triggerChange = function () {
    GoldenRecordStore.trigger('golden-record-change');
  };

  /**
   *
   * @param oCallbackData
   * @returns {*}
   * @private
   * @description To fetch golden record bucket in Dashboard's Match and Merge Tab
   */
  let _fetchGoldenRecordBuckets = function (oCallbackData) {
    let oScreenProps = ContentScreenProps.screen;
    let oData = _createRequestData(oCallbackData.filterContext);
    let sUrl = ContentScreenRequestMapping.GetGoldenRecordBuckets;

    let oExtraData = {};
    oExtraData.URL = sUrl;
    oExtraData.postData = oData.requestData;
    oExtraData.requestData = oData.queryString;

    let sBreadcrumbId, sBreadcrumbLabel, sType, sHelpScreenId;
    let aDashboardTabsList = DashboardScreenProps.getDashboardTabsList();
    let oSelectedTab = CS.find(aDashboardTabsList, {id: DashboardTabDictionary.BUCKETS_TAB});
    let oSelectedModule = ContentUtils.getSelectedModule();
    sBreadcrumbId = BreadCrumbModuleAndHelpScreenIdDictionary.DASHBOARD;
    sBreadcrumbLabel = getTranslation().DASHBOARD_TITLE + " : " + oSelectedTab.label;
    sType = "module";
    sHelpScreenId = oSelectedModule.id;
    let aPayloadData = [oCallbackData, oExtraData];
    oCallbackData.breadCrumbData = BreadcrumbStore.createBreadcrumbItem(sBreadcrumbId, sBreadcrumbLabel, sType, sHelpScreenId, oCallbackData.filterContext, oExtraData, "", aPayloadData, _fetchGoldenRecordBucketsCall);

    return _fetchGoldenRecordBucketsCall(oCallbackData, oExtraData)
  };

  let _createRequestData = function (oFilterContext) {
    let oPaginationData = GoldenRecordProps.getGoldenRecordBucketsPaginationData();
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    let oPostDataForFilter = oFilterStore.createGetAllInstancesData();
    let AvailableEntityStore = ContentUtils.getAvailableEntityStore();
    let oSpecialUsecaseFilterData = AvailableEntityStore.handleSpecialUsecaseFilters({filterContext: oFilterContext});
    CS.isNotEmpty(oSpecialUsecaseFilterData) && CS.assign(oPostDataForFilter, oSpecialUsecaseFilterData);
    if (CS.isEmpty(oPostDataForFilter.moduleId)) {
      return;
    }

    oPostDataForFilter.selectedTypes = [];
    oPostDataForFilter.selectedTaxonomyIds = [];

    if (ContentScreenProps.screen.getIsKpiContentExplorerOpen()) {
      let oKpiData = ContentScreenProps.screen.getKpiContentExplorerData();
      oPostDataForFilter.kpiId = oKpiData.kpiId;
    }

    var sContentId = ContentUtils.getTreeRootNodeId();
    var oData = {
      id: sContentId,
    };

    oPostDataForFilter.from = oPaginationData.from;
    oPostDataForFilter.size = oPaginationData.pageSize;
    oPostDataForFilter.isArchivePortal = ContentUtils.getIsArchive();

    return {requestData: oPostDataForFilter, queryString: oData};
  };

  let _fetchGoldenRecordBucketsCall = function (oCallbackData, oAjaxExtraData) {
    let fSuccess = successFetchGoldenRecordBuckets.bind(this, oCallbackData);
    let fFailure = failureFetchGoldenRecordBuckets;
    return CS.postRequest(oAjaxExtraData.URL, oAjaxExtraData.requestData, oAjaxExtraData.postData, fSuccess, fFailure);
  };

  let successFetchGoldenRecordBuckets = function (oCallbackData, oResponseData) {
    let oResponse = oResponseData.success;
    let oFilterContext = oCallbackData.filterContext;
    let aBuckets = oResponse.bucketInstances;
    let oReferencedGoldenRecordRules = oResponse.referencedGoldenRecordRules;
    let oReferencedTaxonomies = oResponse.referencedTaxonomies;
    let oReferencedKlasses = oResponse.referencedKlasses;
    let oGoldenRecordBucketsMap = {};
    let aBucketIdsContainingGoldenRecord = oResponse.bucketIdsContainingGoldenRecord;
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    let oFilterProps = ContentUtils.getFilterProps(oFilterContext);
    let bIsFilterInfoRequired = oFilterProps.getIsFilterInformationRequired();
    let oPaginationData = GoldenRecordProps.getGoldenRecordBucketsPaginationData();

    oPaginationData.from = oResponse.from;
    oPaginationData.totalBuckets = oResponse.totalBuckets;
    oPaginationData.currentPageCount = aBuckets.length;

    if (bIsFilterInfoRequired) {
      oFilterStore.prepareAndUpdateFilterAndSortModel(oResponse, oCallbackData, true);
    } else {
      let oFilterInfo = {
        sortData: oResponse.appliedSortData,
        filterData: oFilterProps.getAvailableFilters()
      };
      oFilterStore.setFilterInfo(oFilterInfo);
    }
    oFilterStore.updateTagDataFromAppliedFilters(oResponse.referencedTags);
    oFilterStore.clearTaxonomyTreeBackup();
    ContentUtils.setFilterProps(oFilterStore.getFilterInfo(), false, oFilterContext);

    CS.forEach(aBuckets, function (oBucket) {
      let oReferencedRule = oReferencedGoldenRecordRules[oBucket.ruleId] || {};
      oBucket.activeTabId = GoldenRecordBucketTabs.PROPERTIES;
      oBucket.label = oReferencedRule.label;
      oBucket.code = oReferencedRule.code;
      oBucket.multiTaxonomyData = ContentUtils.getMultiTaxonomyData(oReferencedRule.taxonomyIds, oReferencedTaxonomies, []);
      oBucket.klassesData = [];
      CS.forEach(oReferencedRule.klassIds, function (sKlassId) {
        let oReferencedKlass = oReferencedKlasses[sKlassId] || {};
        let oKlassData = {
          id: sKlassId,
          label: oReferencedKlass.label,
          code: oReferencedKlass.code,
          icon: oReferencedKlass.icon,
          customIconClassName: ClassNameFromBaseTypeDictionary[oReferencedKlass.type] || ""
        };
        oBucket.klassesData.push(oKlassData);

        // Handling boolean tag value instance with FALSE value to display on UI,
        // as in back-end we does not save false instance. - Dhanashree
        addBooleanFalseValueTagInstance(oBucket.tags, oResponse.referencedTags, oReferencedRule.tags);
      });

      oGoldenRecordBucketsMap[oBucket.id] = oBucket;
    });

    let oConfigDetails = {
      referencedAttributes: oResponse.referencedAttributes,
      referencedTags: oResponse.referencedTags,
      referencedGoldenRecordRules: oReferencedGoldenRecordRules
    };

    let oScreenProps = ContentScreenProps.screen;
    if (!ContentUtils.getSelectedHierarchyContext()) {
      oScreenProps.setViewMode(ContentScreenConstants.viewModes.TILE_MODE);
    }
    GoldenRecordProps.setGoldenRecordBucketsDataMap(oGoldenRecordBucketsMap);
    GoldenRecordProps.setGoldenRecordBucketsConfigDetails(oConfigDetails);
    GoldenRecordProps.setBucketIdsContainingGoldenRecord(aBucketIdsContainingGoldenRecord);
    ContentUtils.setLoadedPropertiesFromConfigDetails(oConfigDetails);
    ContentUtils.setActiveEntity({});
    if(!CS.isEmpty(oCallbackData) && !CS.isEmpty(oCallbackData.breadCrumbData)) {
      BreadcrumbStore.addNewBreadCrumbItem(oCallbackData.breadCrumbData);
    }

    GoldenRecordProps.setShowGoldenRecordBuckets(true);

    _triggerChange();
  };

  let failureFetchGoldenRecordBuckets = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureFetchGoldenRecordBuckets', getTranslation());
  };

  /**
   *
   * @param sBucketId - Active bucket id
   * @param sTabId - Tab id of respective bucket
   * @private
   * @description Golden record bucket tab change handler
   */
  let _handleGoldenRecordBucketTabChanged = function (sBucketId, sTabId) {
    let oGoldenRecordBucketDataMap = GoldenRecordProps.getGoldenRecordBucketsDataMap();
    if (sTabId === GoldenRecordBucketTabs.PROPERTIES) {
      oGoldenRecordBucketDataMap[sBucketId].activeTabId = sTabId;
      _triggerChange();
    } else {
      oGoldenRecordBucketDataMap[sBucketId].activeTabId = sTabId;
      _fetchGoldenRecordBucketMatches(sBucketId);
    }
  };

  let _fetchGoldenRecordBucketMatches = function (sBucketId) {
    let sUrl = ContentScreenRequestMapping.GetGoldenRecordBucketMatches;
    let oQueryString = {};
    let oRequestData = {
      id: sBucketId,
      from: 0,
      size: 999,
      searchText: ""
    };
    let fSuccess = successFetchGoldenRecordBucketMatches.bind(this, sBucketId);
    let fFailure = failureFetchGoldenRecordBucketMatches;
    CS.postRequest(sUrl, oQueryString, oRequestData, fSuccess, fFailure);
  };

  let successFetchGoldenRecordBucketMatches = function (sBucketId, oResponseData) {
    let oResponse = oResponseData.success;
    let aMatches = oResponse.klassInstancesList;
    let oReferencedAssets = oResponse.referencedAssets || {};
    let oReferencedKlasses = oResponse.referencedKlasses;
    let oReferencedTaxonomies = oResponse.referencedTaxonomies;
    let oReferencedOrganizations = oResponse.referencedOrganizations;

    CS.forEach(aMatches, function (oMatchingContent) {
      let oReferencedAsset = oReferencedAssets[oMatchingContent.defaultAssetInstanceId];
      if (oReferencedAsset) {
        oMatchingContent.assetObject = ContentUtils.getAssetDataObject(oReferencedAsset);
      }

      let sKlassesAndTaxonomies = "";

      CS.forEach(oMatchingContent.types, function (sKlassId) {
        let oReferencedKlass = oReferencedKlasses[sKlassId] || {};
        let sKlassName = CS.getLabelOrCode(oReferencedKlass);
        if (sKlassName) {
          if (sKlassesAndTaxonomies) {
            sKlassName = ", " + sKlassName;
          }
          sKlassesAndTaxonomies += sKlassName;
        }
      });

      CS.forEach(oMatchingContent.taxonomyIds, function (sTaxonomyId) {
        let oReferencedTaxonomy = oReferencedTaxonomies[sTaxonomyId] || {};
        let sTaxonomyName = CS.getLabelOrCode(oReferencedTaxonomy);
        if (sTaxonomyName) {
          if (sKlassesAndTaxonomies) {
            sTaxonomyName = ", " + sTaxonomyName;
          }
          sKlassesAndTaxonomies += sTaxonomyName;
        }
      });

      oMatchingContent.klassesAndTaxonomies = sKlassesAndTaxonomies;

      let oPartnerSource = oMatchingContent.partnerSource;
      let oOrganization = oReferencedOrganizations[oPartnerSource.organizationId];
      oMatchingContent.supplier = oOrganization || {};
    });

    let oGoldenRecordBucketsMap = GoldenRecordProps.getGoldenRecordBucketsDataMap();
    oGoldenRecordBucketsMap[sBucketId].matches = aMatches; //todo GR: handle pagination
    oGoldenRecordBucketsMap[sBucketId].klassInstanceCount = oResponse.count;
    _triggerChange();
  };

  let failureFetchGoldenRecordBucketMatches = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureFetchGoldenRecordBucketMatches', getTranslation());
  };


  /**
   *
   * @param sId - Active bucketId
   * @param bIsSourceTabClicked - True if Content open view source tab clicked
   * @private
   * @description Golden record bucket comparison button handler
   */
  let _fetchDataForGoldenRecordMatchAndMergeWrapper = function (sId, bIsSourceTabClicked) {
    let bIsForAutoCreate = false;
    let sBucketRuleId = "";

    /**Decider  logic */
    let aBucketIdsContainingGoldenRecord = GoldenRecordProps.getBucketIdsContainingGoldenRecord();
    if (!CS.includes(aBucketIdsContainingGoldenRecord, sId) && !bIsSourceTabClicked) {
      let oGoldenRecordBucketsDataMap = GoldenRecordProps.getGoldenRecordBucketsDataMap();
      let oBucket = oGoldenRecordBucketsDataMap[sId];
      let oGoldenRecordBucketsConfigDetails = GoldenRecordProps.getGoldenRecordBucketsConfigDetails();
      let oReferencedGoldenRecordRules = oGoldenRecordBucketsConfigDetails.referencedGoldenRecordRules;
      let oRule = oReferencedGoldenRecordRules[oBucket.ruleId];
      if (oRule.isAutoCreate) {
        bIsForAutoCreate = true;
        sBucketRuleId = oBucket.ruleId;
      }
    }

    if (bIsForAutoCreate) {
      _autoCreateGoldenRecord(sId, sBucketRuleId);
    } else {
      _fetchDataForGoldenRecordMatchAndMerge(sId, bIsSourceTabClicked);
    }
  };

  /**
   *
   * @param sId - Active bucketId
   * @param bIsSourceTabClicked - True if Content open view source tab clicked
   * @private
   * @description - Fetch data for comparison view lanugage selection
   */
  let _fetchDataForGoldenRecordMatchAndMerge = function (sId, bIsSourceTabClicked) {
      let oData = {};
      if (bIsSourceTabClicked) {
        oData.goldenRecordId = sId;
      } else {
        oData.bucketId = sId;
      }
      let sUrl = getRequestMapping().GetDataForGoldenRecordMergeLanugageSelection;
      let fSuccess = successFetchDataForMNMLanguageSelection.bind(this, sId, bIsSourceTabClicked);
      let fFailure = failureFetchDataForMNMLanguageSelection.bind(this);
      CS.postRequest(sUrl, {}, oData, fSuccess, fFailure);
  };

  let successFetchDataForMNMLanguageSelection = function (sId, bIsSourceTabClicked, oResponse) {
    oResponse = oResponse.success;
    let oConfigDetails = oResponse.configDetails;
    let aSourceInstances = oResponse.sourceInstances;
    let aGoldenRecordLanguages = [];

    if (bIsSourceTabClicked) {
      ContentScreenProps.screen.setIsEditMode(true);
      GoldenRecordProps.setIsGoldenRecordRemergeSourcesTabClicked(true);
      GoldenRecordProps.setActiveGoldenRecordId(sId);
    } else {
      GoldenRecordProps.setIsMatchAndMergeViewOpen(true);
      GoldenRecordProps.setActiveBucketId(sId);
    }
    let oGoldenRecordBucketsDataMap = GoldenRecordProps.getGoldenRecordBucketsDataMap();
    let oBucket = oGoldenRecordBucketsDataMap[sId];
    GoldenRecordProps.setActiveBucketData(oBucket);

    if(oResponse.goldenRecordId) {
      GoldenRecordProps.setActiveGoldenRecordId(oResponse.goldenRecordId);
    }
    if (oConfigDetails.goldenRecordRule) {
      GoldenRecordProps.setGoldenRecordRule(oConfigDetails.goldenRecordRule);
    }
    aGoldenRecordLanguages = oResponse.goldenRecordLanguages || [];

    GoldenRecordProps.setIsFullScreenMode(true);
    _preProcessGoldenRecordLanguageSelectionDataForStepperView();
    _preProcessSkeletonDataForGoldenRecordLanguageSelection(oConfigDetails, aSourceInstances, aGoldenRecordLanguages, oResponse.goldenRecordId, oResponse.creationLanguage);
    _triggerChange();
  };

  let failureFetchDataForMNMLanguageSelection = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureFetchDataForMNMLanguageSelection', getTranslation());
  };

  /**
   *
   * @private
   * @description - Process comaprison view language selection data for stepper view
   */
  let _preProcessGoldenRecordLanguageSelectionDataForStepperView = () => {
    GoldenRecordProps.setStepperViewActiveStep(0);
    GoldenRecordProps.setStepperViewSteps([
      GRConstants.SELECT_LANGUAGE,
      GRConstants.MATCH_PROPERTIES,
      GRConstants.MATCH_RELATIONSHIPS]);
  };

  let _prepareSourceKlassInstanceList = (oConfigDetails, aSourceInstances) => {
    let oReferencedOrganizations = oConfigDetails.referencedOrganizations;
    CS.forEach(aSourceInstances, function (oInstance) {
      let sOrganizationId = oInstance.organizationId;
      let oOrganization = oReferencedOrganizations[sOrganizationId];
      oInstance.organizationName = CS.getLabelOrCode(oOrganization);
    });
  };

  /**
   *
   * @param oConfigDetails
   * @param aSourceInstances
   * @param aGoldenRecordLanguages - List of language codes presenet in golden record if golden record present in bucket
   * @private
   * @description - prepare skeleton data for language selection screen grid view
   * */
  let _preProcessSkeletonDataForGoldenRecordLanguageSelection = function (oConfigDetails, aSourceInstances, aGoldenRecordLanguages, sGoldenRecordId, sCreationLanguage) {
    let oGRSkeleton = GoldenRecordLanguageSelectionSkeleton();
    let sDefaultLanguage = GoldenRecordProps.getDefaultLanguage();
    let bGoldenRecordPresent = false;

    _prepareSourceKlassInstanceList(oConfigDetails, aSourceInstances);
    GoldenRecordProps.setKlassInstanceList(aSourceInstances);

    oGRSkeleton.scrollableColumns = aSourceInstances.map(oInstance => ({
      id: oInstance.id,
      label: oInstance.organizationName,
      subLabel: oInstance.name,
      languageCodes: oInstance.languageCodes,
      type: oGridViewPropertyTypes.TICK,
      isVisible: true,
      width: 200,
    }));
    GoldenRecordProps.setGoldenRecordComparisionSkeleton(oGRSkeleton);

    let oReferencedLanguages = oConfigDetails.referencedLanguages;
    GoldenRecordProps.setReferencedLanguages(oReferencedLanguages);

    let aGridViewData = [];
    CS.forEach(oReferencedLanguages, function (oLanguageData, sLanguageCode) {
      let oGRData = {properties: {}};
      oGRData.id = sLanguageCode;
      oGRData.isDisabled = CS.includes(aGoldenRecordLanguages, sLanguageCode);

      oGRData.properties[LANGUAGE] = {
        value: CS.getLabelOrCode(oLanguageData),
        isDisabled: CS.includes(aGoldenRecordLanguages, sLanguageCode)
      };

      CS.forEach(oGRSkeleton.scrollableColumns, oColumn => {
        oGRData.properties[oColumn.id] = {
          value: CS.includes(oColumn.languageCodes, sLanguageCode)
        }
      });
      aGridViewData.push(oGRData);
    });

    let aSelectedLanguageList = GoldenRecordProps.getSelectedLanguageList();
    /**
     * If golden record present in bucket then set languages as selected.
     */
    let aSelectedLanguagesIds = CS.map(aSelectedLanguageList, "code");

    if (!CS.isEmpty(aGoldenRecordLanguages)) {
      _handleGoldenRecordGridViewLanguageSelectButtonClicked(CS.union(aSelectedLanguagesIds, aGoldenRecordLanguages));
    } else if (CS.isEmpty(aSelectedLanguagesIds)) {
      aSelectedLanguagesIds = CS.map(oReferencedLanguages, "code");
      _handleGoldenRecordGridViewLanguageSelectButtonClicked(aSelectedLanguagesIds);

      sDefaultLanguage = aSelectedLanguagesIds[0];
      GoldenRecordProps.setDefaultLanguage(sDefaultLanguage);
    }

    /***When back from Merge properties screen,to show selected languages in select language screen*/
    CS.remove(aSelectedLanguageList, function (oLanguage) {
      let sCode = oLanguage.code;
      if(oReferencedLanguages[sCode] && !CS.includes(oGRSkeleton.selectedContentIds, sCode)) {
        oGRSkeleton.selectedContentIds.push(sCode);
      }
      return CS.isEmpty(oReferencedLanguages[sCode])
    });

    if(!CS.isEmpty(sGoldenRecordId)) {
      sDefaultLanguage = sCreationLanguage;
      GoldenRecordProps.setDefaultLanguage(sCreationLanguage);
      bGoldenRecordPresent = true;
    }

    CS.forEach(aGridViewData, (data) => {
      data.properties[DEFAULT_LANGUAGE] = {
        value: CS.isEqual(data.id, sDefaultLanguage),
        isDisabled: (bGoldenRecordPresent) ? true : !CS.includes(oGRSkeleton.selectedContentIds, data.id)
      };
    });

    _setDefaultLanguageChipViewIndicator();

    GoldenRecordProps.setGoldenRecordLanguageSelectionGridViewData(aGridViewData);
  };

  /**
   *
   * @param aSelectedContentIds - List of selected language codes
   * @param bSelectAllClicked - True if SelectAll button clicked in grid view
   * @private
   * @description - Select language button clicked in content language comparison Grid view
   */
  let _handleGoldenRecordGridViewLanguageSelectButtonClicked = (aSelectedContentIds, bSelectAllClicked) => {
    let oSkeleton = GoldenRecordProps.getGoldenRecordComparisionSkeleton();
    let oGridViewData = GoldenRecordProps.getGoldenRecordLanguageSelectionGridViewData();

    if (bSelectAllClicked) {
      oSkeleton.selectedContentIds = aSelectedContentIds;
    } else {
      oSkeleton.selectedContentIds = CS.xor(oSkeleton.selectedContentIds, aSelectedContentIds);
    }

    let oReferencedLanguages = GoldenRecordProps.getReferencedLanguages();
    let aSelectedLanguageList = [];
    CS.forEach(oSkeleton.selectedContentIds, function (sLanguageCode){
      aSelectedLanguageList.push(oReferencedLanguages[sLanguageCode]);
    });

    GoldenRecordProps.setSelectedLanguageList(aSelectedLanguageList);

    if (CS.isEmpty(GoldenRecordProps.getActiveGoldenRecordId())) {
      CS.forEach(oGridViewData, (data) => {
        if (CS.includes(oSkeleton.selectedContentIds, data.id) === true) {
          data.properties[DEFAULT_LANGUAGE].isDisabled = false;
        } else {
          data.properties[DEFAULT_LANGUAGE].isDisabled = true;
          data.properties[DEFAULT_LANGUAGE].value = false;

          if (GoldenRecordProps.getDefaultLanguage() === data.id) {
            GoldenRecordProps.setDefaultLanguage('');
          }
        }
      });

      _setDefaultLanguageChipViewIndicator();
    }
  };

  let _setDefaultLanguageChipViewIndicator = () => {
    let aSelectedLanguageList = GoldenRecordProps.getSelectedLanguageList();
    let sDefaultLanguage = GoldenRecordProps.getDefaultLanguage();

    CS.forEach(aSelectedLanguageList, function (language) {
      language.showIndicator = (language.code === sDefaultLanguage);
    });
  }

  let _handleGoldenRecordGridViewDefaultLanguageSelectButtonClicked = (sSelectedContentId, sPropertyId, bValue) => {
    let oGridViewData = GoldenRecordProps.getGoldenRecordLanguageSelectionGridViewData();

    CS.forEach(oGridViewData, (data) => {
      data.properties[sPropertyId].value = false;

      if (data.id === sSelectedContentId) {
        data.properties[sPropertyId].value = bValue;
      }
    });

    if (bValue === true) {
      GoldenRecordProps.setDefaultLanguage(sSelectedContentId);
    } else {
      GoldenRecordProps.setDefaultLanguage('');
    }

    _setDefaultLanguageChipViewIndicator();
  };

  /**
   *
   * @param sContext - Stepper button context
   * @description - Handles stepper step change event
   *                NEXT - When we click next button in stepper
   *                BACK - When we click back button in stepper
   *                FINISH (Create/Save) - When we click Last button in stepper
   * @private
   */
  let _handleStepperViewActiveStepChanged = (sContext) => {
    let iActiveStep = GoldenRecordProps.getStepperViewActiveStep() || 0;
    let aGoldenRecordSteps  = GoldenRecordProps.getStepperViewSteps();
    let sDefaultLanguage = GoldenRecordProps.getDefaultLanguage();

    switch (sContext) {
      case GRConstants.BACK:
        if(iActiveStep > 0) {
          iActiveStep -= 1;
        }
        break;

      case GRConstants.NEXT:
        if (CS.isEmpty(sDefaultLanguage)) {
          alertify.error(getTranslation().DEFAULT_LANGUAGE_NEEDED);

          return;
        } else {
          if (iActiveStep < aGoldenRecordSteps.length) {
            iActiveStep += 1;
          }
        }

        break;

      case GRConstants.CREATE:
        _createGoldenRecord();
        return;

      case GRConstants.SAVE:
        _saveGoldenRecord();
        return;
    }

    GoldenRecordProps.setStepperViewActiveStep(iActiveStep);
    _setActiveStepData(iActiveStep);
  };

  /**
   *
   * @param iActiveStep - Id of active step
   * @description - Prepare active step data when step change event occures
   * @private
   */
  let _setActiveStepData = (iActiveStep) => {
    let aStepperViewSteps = GoldenRecordProps.getStepperViewSteps();
    let sActiveStepContext = aStepperViewSteps[iActiveStep];
    let oSkeleton = GoldenRecordProps.getGoldenRecordComparisionSkeleton();
    let sSelectedLanguageId = oSkeleton.selectedContentIds[0];
    GoldenRecordProps.setMNMSelectedLanguageId(sSelectedLanguageId);
    let bIsGoldenRecordRemergeSourcesTabClicked = GoldenRecordProps.getIsGoldenRecordRemergeSourcesTabClicked();

    switch (sActiveStepContext) {
      case GRConstants.SELECT_LANGUAGE:
        CustomActionDialogStore.showConfirmDialog(getTranslation().WARNING_MESSAGE_CHANGES_WILL_BE_LOST,
            "",
            _handleMNMBackButtonClicked.bind(this, bIsGoldenRecordRemergeSourcesTabClicked),
            function () {
              let iActiveStep = GoldenRecordProps.getStepperViewActiveStep();
              GoldenRecordProps.setStepperViewActiveStep(iActiveStep + 1);
              _triggerChange();
            });
        break;

      case GRConstants.MATCH_PROPERTIES:
        _fetchMatchAndMergeDataForProperties(sSelectedLanguageId);
        break;

      case GRConstants.MATCH_RELATIONSHIPS:
        _fetchMatchAndMergeDataForRelationships(sSelectedLanguageId);
        break;
    }
  };

  /**
   *
   * @param sSelectedLanguageId - Language id selected in Match and merge view
   * @private
   * @description - fetch Merge properties screen data
   */

  let _fetchMatchAndMergeDataForProperties = function (sSelectedLanguageId) {
    let sActiveBucketId = GoldenRecordProps.getActiveBucketId();

    let sUrl = ContentScreenRequestMapping.GetDataForGoldenRecordPropertiesMerge;
    let oRequestData = {
      languageCode: sSelectedLanguageId,
      goldenRecordId: GoldenRecordProps.getActiveGoldenRecordId() || null
    };

    let bIsGoldenRecordRemergeSourcesTabClicked = GoldenRecordProps.getIsGoldenRecordRemergeSourcesTabClicked();
    if(!bIsGoldenRecordRemergeSourcesTabClicked) {
      oRequestData.bucketInstanceId = sActiveBucketId;
    }

    let oExtraData = {
      dataLanguage: GoldenRecordProps.getMNMSelectedLanguageId()
    };
    let fSuccess = successFetchMatchAndMergeDataForProperties;
    let fFailure = failureFetchMatchAndMergeDataForProperties;
    CS.postRequest(sUrl, {}, oRequestData, fSuccess, fFailure, false, oExtraData);
  };

  let successFetchMatchAndMergeDataForProperties = function (oResponse) {
    let oResponseData = oResponse.success;
    let oConfigDetails = oResponseData.configDetails;
    let oPropertyRecommendations = oResponseData.recommendation;

    let oGoldenRecordInstance = {};
    if (oConfigDetails.goldenRecordRule) {
      oGoldenRecordInstance = oResponseData.goldenRecordInstance;
    }

    GoldenRecordProps.setActiveGoldenRecordInstance(oGoldenRecordInstance);
    GoldenRecordProps.setSelectedKlassInstanceIds(oResponseData.klassInstanceIds);
    if (oPropertyRecommendations) {
      GoldenRecordProps.setPropertyRecommendations(oPropertyRecommendations);
    }

    GoldenRecordProps.setGoldenRecordMatchPropertiesData(oResponseData.matchPropertiesModel);
    GoldenRecordProps.setGoldenRecordComparisonConfigDetails(oConfigDetails);
    _preProcessPropertiesDataForComparison(oConfigDetails, oResponseData.klassIds, oResponseData.taxonomyIds, oGoldenRecordInstance);

    /**To show First row selected in comparison view*/
    _handleComparisonDataFirstRowClicked();
  };

  let failureFetchMatchAndMergeDataForProperties = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureFetchMatchAndMergeDataForProperties', getTranslation());
  };

  /**
   *
   * @param sSelectedLanguageId - Language id selected in Match and merge view
   * @private
   * @description fetch Merge relationships screen data
   */
  let _fetchMatchAndMergeDataForRelationships = function (sSelectedLanguageId) {
    let sActiveBucketId = GoldenRecordProps.getActiveBucketId();
    let oPaginationData = GoldenRecordProps.getRelationshipPaginationData();

    let sUrl = ContentScreenRequestMapping.GetDataForGoldenRecordRelationshipsMerge;
    let oRequestData = {
      languageCode: sSelectedLanguageId,
      goldenRecordId: GoldenRecordProps.getActiveGoldenRecordId() || null,
      from: oPaginationData.from,
      size: oPaginationData.size
    };

    let bIsGoldenRecordRemergeSourcesTabClicked = GoldenRecordProps.getIsGoldenRecordRemergeSourcesTabClicked();
    if(!bIsGoldenRecordRemergeSourcesTabClicked) {
      oRequestData.bucketInstanceId = sActiveBucketId;
    }

    let fSuccess = successFetchMatchAndMergeDataForRelationships.bind(this, sActiveBucketId);
    let fFailure = failureFetchMatchAndMergeDataForRelationships;
    CS.postRequest(sUrl, {}, oRequestData, fSuccess, fFailure);
  };

  let successFetchMatchAndMergeDataForRelationships = function (sId, oResponse) {
    oResponse = oResponse.success;
    let oConfigDetails = oResponse.configDetails;
    let oLayoutData = MNMUtils.getLayoutSkeleton();
    let oDummyKlassInstance = GoldenRecordProps.getDummyKlassInstance();

    let oReferencedRelationships = oConfigDetails.referencedRelationships;
    let oReferencedNatureRelationships = oConfigDetails.referencedNatureRelationships;
    let oReferencedElements = oConfigDetails.referencedElements;
    let oPropertyRecommendations = oResponse.recommendation;

    /**If Golden record present in bucket*/
    let sGoldenRecordId = GoldenRecordProps.getActiveGoldenRecordId();
    let oGoldenRecordRelationshipInstance = {};
    if (oConfigDetails.goldenRecordRule) {
      oGoldenRecordRelationshipInstance = oResponse.goldenRecordRelationshipInstances;
      CS.forEach(oGoldenRecordRelationshipInstance, function (oRelationship, sSideId) {
        oRelationship.contentId = sGoldenRecordId;
      });
    }

    _fillDummyKlassInstanceWithRelationship(oDummyKlassInstance, oReferencedRelationships, oReferencedElements,
        oPropertyRecommendations, oGoldenRecordRelationshipInstance);
    _fillDummyKlassInstanceWithNatureRelationship(oDummyKlassInstance, oReferencedNatureRelationships, oReferencedElements,
        oPropertyRecommendations, oGoldenRecordRelationshipInstance);

    _makeComparisonDataForRelationshipsTable(oDummyKlassInstance, oLayoutData, oResponse);
    _makeComparisonDataForNatureRelationshipsTable(oDummyKlassInstance, oLayoutData, oResponse);
    GoldenRecordProps.setMatchAndMergeComparisonData(oLayoutData);
    /**To show First row selected in comparison view*/
    _handleComparisonDataFirstRowClicked();
  };

  let failureFetchMatchAndMergeDataForRelationships = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureFetchMatchAndMergeDataForRelationships', getTranslation());
  };

  /**
   *
   * @param sPropertyId - Id of active row (types/ taxonomies/ attribute/ tag/ relationship/ natureRelationship)
   * @param sTableId - Id of table in which propertyId belongs to
   * @description Match and merge view property row click handler
   * @private
   */
  let _handleGoldenRecordMatchAndMergeViewTableRowClicked = function (sPropertyId, sTableId) {
    let oComparisionData = GoldenRecordProps.getMatchAndMergeComparisonData();
    let oTableData = oComparisionData.tableData[sTableId];
    let oPropertyData = CS.find(oTableData.rowData, {id: sPropertyId});

    let sSelectedLanguageId = GoldenRecordProps.getMNMSelectedLanguageId();
    let sBucketId = GoldenRecordProps.getActiveBucketId();
    let sGoldenRecordId = GoldenRecordProps.getActiveGoldenRecordId();

    let oRequestData = {
      languageCode: sSelectedLanguageId
    };

    let bIsGoldenRecordRemergeSourcesTabClicked = GoldenRecordProps.getIsGoldenRecordRemergeSourcesTabClicked();
    if(bIsGoldenRecordRemergeSourcesTabClicked) {
      oRequestData.goldenRecordId = sGoldenRecordId;
    } else {
      oRequestData.bucketId = sBucketId;
    }

    let {sURL, fSuccess} = _getURLAndRequestDataToGetDetailedInfo(oPropertyData, oRequestData, sPropertyId);
    fSuccess = fSuccess.bind(this, sPropertyId, sTableId, oPropertyData);
    return CS.postRequest(sURL, "", oRequestData, fSuccess, failureFetchGoldenRecordMNMPropertyDetailData)
        .then(() => {
          oComparisionData.selectedTableId = sTableId;
          oComparisionData.selectedRowId = sPropertyId;
          _triggerChange();
        });
  };

  /**
   *
   * @param oPropertyData - Active Property Comparison data
   * @param oRequestData - Request Data for detailed view
   * @param sPropertyId - Active propety Id
   * @returns {{sURL: URL based on propertyType, fSuccess: Success handler}}
   * @private
   * @description Prepare URL and request data of properties for detailed view
   */
  let _getURLAndRequestDataToGetDetailedInfo = function (oPropertyData, oRequestData, sPropertyId) {
    let sURL = "";
    let fSuccess = CS.noop;
    if (oPropertyData.propertyType === ATTRIBUTE) {
      oRequestData.propertyInfo = {
        propertyId: sPropertyId,
        type: oPropertyData.propertyType,
        isTranslatable: oPropertyData.isDependent
      };
      sURL = ContentScreenRequestMapping.GetValuesFromSourcesForProperties;
      fSuccess = successFetchGoldenRecordMNMAttributeDetailData;
    }
    else if (oPropertyData.propertyType === TAG) {
      oRequestData.propertyInfo = {
        propertyId: sPropertyId,
        type: oPropertyData.propertyType,
      };
      sURL = ContentScreenRequestMapping.GetValuesFromSourcesForProperties;
      fSuccess = successFetchGoldenRecordMNMTagDetailData;
    }
    else if (oPropertyData.propertyType === RELATIONSHIP || oPropertyData.propertyType === NATURE_RELATIONSHIP) {
      oRequestData.relationshipId = oPropertyData.relationshipId;
      oRequestData.isNatureRelationship = (oPropertyData.propertyType === NATURE_RELATIONSHIP);
      oRequestData.sideId = sPropertyId;
      oRequestData.from = 0;
      oRequestData.size = 10;
      sURL = ContentScreenRequestMapping.GetValuesFromSourcesForRelationship;
      fSuccess = successFetchGoldenRecordMNMRelationshipDetailData;
    }
    else if (oPropertyData.propertyType === BASIC_INFO) {
      sURL = ContentScreenRequestMapping.GetValuesFromSourcesForBasicInfo;
      oRequestData.type = oPropertyData.id;
      fSuccess = successFetchGoldenRecordMNMBasicInfoDetailData;
    }
    return {sURL, fSuccess};
  };

  let _getRecordLabel = function (oKlassInstance) {
    return {
      label: oKlassInstance.organizationName,
      subLabel: oKlassInstance.name
    }
  };

  let successFetchGoldenRecordMNMAttributeDetailData = function (sPropertyId, sTableId, oPropertyData, oResponse) {
    oResponse = oResponse.success;
    let aSourceIds = oResponse.sourceIds;
    let oSourceIdInstancesMap = oResponse.sourceIdValueMap;
    let aPropertyInstances = [];
    let oPropertyDetailedData = {};
    let sPropertyType = oPropertyData.propertyType;
    let aKlassInstanceList = GoldenRecordProps.getKlassInstanceList();

    let bIsValid = _validateKlassInstances(aKlassInstanceList, aSourceIds);
    if(!bIsValid) {
      return;
    }

    CS.forEach(aSourceIds, function (sSourceId) {
      let oKlassInstance = CS.find(aKlassInstanceList, {id: sSourceId});
      let oAttributeInstance = oSourceIdInstancesMap[sSourceId];
      if(CS.isEmpty(oAttributeInstance)) {
        return;
      }

      let bIsHTMLAttribute = ContentUtils.isAttributeTypeHtml(oPropertyData.type);
      let sValue = bIsHTMLAttribute ? oAttributeInstance.valueAsHtml : oAttributeInstance.value;
      let sValueAsHtml = bIsHTMLAttribute ? oAttributeInstance.value : "";

      let oElement = {
        id: oAttributeInstance.attributeId,
        type: oPropertyData.type,
        value: sValue,
        valueAsHtml: sValueAsHtml,
        valueAsExpression: oAttributeInstance.valueAsExpression,
        isDisabled: true,
        masterAttribute: oPropertyData.masterAttribute,
        properties: {},
        propertyType: ATTRIBUTE,
      };

      let bValueExist = false;
      CS.forEach(aPropertyInstances, function (oPropertyInstance) {
        if(MNMUtils.isPropertyValueEqual(sPropertyType, oPropertyInstance.property, oElement)) {
          oPropertyInstance.recordLabels.push(_getRecordLabel(oKlassInstance));
          bValueExist = true;
          return false;
        }
      });

      if(!bValueExist) {
        aPropertyInstances.push({
          property: oElement,
          recordLabels: [_getRecordLabel(oKlassInstance)],
          isChecked: MNMUtils.isPropertyValueEqual(sPropertyType, oPropertyData, oElement),
          isValueEmpty: MNMUtils.isPropertyValueEmpty(sPropertyType, oElement),
          suppressSelection: oPropertyData.isDisabled
        });
      }
    });

    oPropertyDetailedData[sPropertyId] = {
      id: sPropertyId,
      label: oPropertyData.label,
      elements: aPropertyInstances
    };
    GoldenRecordProps.setMNMActivePropertyDetailedData(oPropertyDetailedData);
  };

  let successFetchGoldenRecordMNMTagDetailData = function (sPropertyId, sTableId, oPropertyData,  oResponse) {
    oResponse = oResponse.success;
    let aSourceIds = oResponse.sourceIds;
    let oSourceIdInstancesMap = oResponse.sourceIdValueMap;
    let oGRConfigDetails = GoldenRecordProps.getGoldenRecordComparisonConfigDetails();
    let oReferencedTags = oGRConfigDetails.referencedTags;
    let oReferencedElements = oGRConfigDetails.referencedElements;
    let aKlassInstanceList = GoldenRecordProps.getKlassInstanceList();

    let bIsValid = _validateKlassInstances(aKlassInstanceList, aSourceIds);
    if(!bIsValid) {
      return;
    }

    let aPropertyInstances = [];
    let oPropertyDetailedData = {};
    let sPropertyType = oPropertyData.propertyType;

    CS.forEach(aSourceIds, function (sSourceId) {
      let oKlassInstance = CS.find(aKlassInstanceList, {id: sSourceId});
      let oTagInstance = oSourceIdInstancesMap[sSourceId];
      let oTagGroupModel = ContentUtils.getTagGroupModels(oReferencedTags[sPropertyId], {tags: [oTagInstance]}, oReferencedElements[sPropertyId], GRConstants.GOLDEN_RECORD_COMPARISON, {});
      oTagGroupModel.disabled = true;
      oTagGroupModel.masterTagList = oReferencedTags;

      let oElement = {
        tagGroupModel: oTagGroupModel,
        propertyType: TAG,
      };

      let bValueExist = false;
      CS.forEach(aPropertyInstances, function (oPropertyInstance) {
        if(MNMUtils.isPropertyValueEqual(sPropertyType, oPropertyInstance.property, oElement)) {
          oPropertyInstance.recordLabels.push(_getRecordLabel(oKlassInstance));
          bValueExist = true;
          return false;
        }
      });

      if(!bValueExist) {
        aPropertyInstances.push({
          property: oElement,
          recordLabels: [_getRecordLabel(oKlassInstance)],
          isChecked: MNMUtils.isPropertyValueEqual(sPropertyType, oPropertyData, oElement),
          isValueEmpty: MNMUtils.isPropertyValueEmpty(sPropertyType, oElement),
          suppressSelection: oPropertyData.isDisabled
        });
      }
    });

    oPropertyDetailedData[sPropertyId] = {
      id: sPropertyId,
      label: oPropertyData.label,
      elements: aPropertyInstances
    };
    GoldenRecordProps.setMNMActivePropertyDetailedData(oPropertyDetailedData);
  };

  let _getPaginationData = function (sFilterScreenContext) {
    let oFilterContext = {
      filterType: oFilterPropType.PAGINATION,
      screenContext: sFilterScreenContext
    };
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    let oFilterProps = oFilterStore.getFilterProps();

    return {
      currentPageItems: oFilterProps.getCurrentPageItems(),
      from: oFilterProps.getFromValue(),
      pageSize: oFilterProps.getPaginationSize(),
      totalItems: oFilterProps.getTotalItemCount(),
      isMiniPaginationView: true
    }
  };

  let _setPaginationData = function (sFilterScreenContext, iTotalItemCount, iFrom, iSize, iCurrentPageItems) {
    let oFilterContext = {
      filterType: oFilterPropType.PAGINATION,
      screenContext: sFilterScreenContext
    };
    let oFilterStore = FilterStoreFactory.getFilterStore(oFilterContext);
    let oFilterProps = oFilterStore.getFilterProps();

    oFilterProps.setFromValue(iFrom);
    oFilterProps.setPaginationSize(iSize);
    oFilterProps.setCurrentPageItems(iCurrentPageItems);
    oFilterProps.setTotalItemCount(iTotalItemCount);
  };

  let successFetchGoldenRecordMNMRelationshipDetailData = function (sPropertyId, sTableId, oPropertyData,  oResponse) {
    oResponse = oResponse.success;
    let oConfigDetails = oResponse.configDetails || {};
    let aSourceIds = oResponse.sourceIds;
    let oSourceIdInstancesMap = oResponse.sourceIdRelationshipInstancesMap;
    let oKlassInstancesMap = oResponse.klassInstances;
    let oReferencedAssets = oResponse.referencedAssets;
    let oReferencedTags = oConfigDetails.referencedTags;

    let aKlassInstanceList = GoldenRecordProps.getKlassInstanceList();
    let oPaginationData = GoldenRecordProps.getRelationshipPaginationData();

    let bIsValid = _validateKlassInstances(aKlassInstanceList, aSourceIds);
    if(!bIsValid) {
      return;
    }

    let oPropertyInstances = {};
    let oPropertyDetailedData = {};
    let sPropertyType = oPropertyData.propertyType;

    CS.forEach(aSourceIds, function (sSourceId) {
      let oKlassInstance = CS.find(aKlassInstanceList, {id: sSourceId});

      let oRelationshipInstances = oSourceIdInstancesMap[sSourceId];
      let aRelationshipInstances = oRelationshipInstances ? oRelationshipInstances.relationshipInstances : [];
      if(CS.isEmpty(aRelationshipInstances)) return;

      let aReferencedAssetData = _getReferencedAssetDataForRelationship(aRelationshipInstances, oKlassInstancesMap, oReferencedTags, oReferencedAssets);

      _setPaginationData(sSourceId, oRelationshipInstances.totalCount, oPaginationData.from, oPaginationData.size, aReferencedAssetData.length);
      let oElement = {
        id:  sPropertyId,
        sideId: oPropertyData.id,
        referencedAssetsData: aReferencedAssetData,
        sourceKlassInstanceId: sSourceId,
        propertyType: RELATIONSHIP,
        paginationData: _getPaginationData(sSourceId)
      };

      oPropertyInstances[sSourceId] = {
        property: oElement,
        recordLabels: [_getRecordLabel(oKlassInstance)],
        isChecked: MNMUtils.isPropertyValueEqual(sPropertyType, oPropertyData, oElement),
        isValueEmpty: MNMUtils.isPropertyValueEmpty(sPropertyType, oElement),
      };
    });

    oPropertyDetailedData[sPropertyId] = {
      id: sPropertyId,
      label: oPropertyData.label,
      elements: oPropertyInstances
    };
    GoldenRecordProps.setMNMActivePropertyDetailedData(oPropertyDetailedData);
  };

  let successFetchGoldenRecordMNMBasicInfoDetailData = function (sPropertyId, sTableId, oPropertyData,  oResponse) {
    oResponse = oResponse.success;
    let aSourceIds = oResponse.sourceIds;
    let oSourceIdInstancesMap = oResponse.sourceIdTypeInfoMap;
    let oConfigDetails = oResponse.configDetails;
    let aKlassInstanceList = GoldenRecordProps.getKlassInstanceList();

    let bIsValid = _validateKlassInstances(aKlassInstanceList, aSourceIds);
    if(!bIsValid) {
      return;
    }

    let aPropertyInstances = [];
    let oPropertyDetailedData = {};
    let oReferencedData = {};
    let sPropertyKey = "";
    let fFunctionToPrepareValues = CS.noop;

    if(sPropertyId === TYPES) {
      oReferencedData = oConfigDetails.referencedKlasses;
      sPropertyKey = "klassIds";
      fFunctionToPrepareValues = MNMUtils.prepareReferencedKlassesMap;
    } else if (sPropertyId === TAXONOMIES) {
      oReferencedData = oConfigDetails.referencedTaxonomies;
      sPropertyKey = "taxonomyIds";
      fFunctionToPrepareValues = MNMUtils.prepareReferencedTaxonomiesMap
    }
    CS.forEach(aSourceIds, function (sSourceId) {
      let oKlassInstance = CS.find(aKlassInstanceList, {id: sSourceId});
      let oInstance = oSourceIdInstancesMap[sSourceId];
      let aValues = fFunctionToPrepareValues(oInstance[sPropertyKey], oReferencedData);

      let oElement = {
        values : sPropertyId === TYPES ? [aValues] : aValues,
        propertyType: oPropertyData.propertyType,
        rendererType: "chip"
      };
      aPropertyInstances.push({
        property: oElement,
        recordLabels: [_getRecordLabel(oKlassInstance)],
        suppressSelection: true,
        isValueEmpty: MNMUtils.isPropertyValueEmpty(sPropertyId, oElement)
      });
    });
    oPropertyDetailedData[sPropertyId] = {
      id: sPropertyId,
      label: oPropertyData.label,
      elements: aPropertyInstances,
    };
    GoldenRecordProps.setMNMActivePropertyDetailedData(oPropertyDetailedData);
  };

  let failureFetchGoldenRecordMNMPropertyDetailData = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureFetchGoldenRecordMNMPropertyDetailData', getTranslation());
  };

  let _autoCreateGoldenRecord = function (sBucketId, sRuleId) {
    let oRequestData = {
      bucketId: sBucketId,
      ruleId: sRuleId,
      klassInstance: {
        attributes: [],
        tags: [],
        defaultAssetInstanceId: null,
        organizationId: SessionProps.getSessionOrganizationId(),
        endpointId: SessionProps.getSessionEndpointId() || '-1',
        physicalCatalogId: SessionProps.getSessionPhysicalCatalogId(),
        portalId: ContentUtils.getSelectedPortalId(),
      },
      relationships: []
    };

    let oCallback = {
      updateBreadcrumbRootNodeLabel: _updateBreadcrumbRootNodeLabel,
      breadCrumbData: {}
    };

    let sUrl = getRequestMapping().AutoCreateGoldenRecord;
    let fSuccess = _successAutoCreateGoldenRecord.bind(this, oCallback);
    let fFailure = _failureAutoCreateGoldenRecord.bind(this);
    CS.putRequest(sUrl, {}, oRequestData, fSuccess, fFailure);
  };

  let _successAutoCreateGoldenRecord = function (oCallback, oResponse) {
    _successCreateGoldenRecord(oCallback, oResponse);
  };

  let _failureAutoCreateGoldenRecord = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureAutoCreateGoldenRecord', getTranslation());
  };

  let _createGoldenRecord = function () {
    let sDefaultLanguage = GoldenRecordProps.getDefaultLanguage();
    let oKlassInstance = GoldenRecordProps.getDummyKlassInstance();
    _postProcessDummyKlassInstanceForCreate(oKlassInstance);

    let aSelectedLanguages = GoldenRecordProps.getSelectedLanguageList();
    let aSelectedLanguageCodes = aSelectedLanguages.map(oLanguage => oLanguage.code);
    let sBucketId = GoldenRecordProps.getActiveBucketId();
    let oGoldenRecordRule = GoldenRecordProps.getGoldenRecordRule();
    let oKlassBasicInformationData = GoldenRecordProps.getSelectedKlassBasicInformationData();
    let aKlassInstanceIds = GoldenRecordProps.getSelectedKlassInstanceIds();

    let sDefualtAssetInstanceId = "";
    let oRequestData = {
      bucketId: sBucketId,
      ruleId: oGoldenRecordRule.id,
      goldenRecordId: null,
      attributes: oKlassInstance.attributes,
      dependentAttributes: oKlassInstance.dependentAttributes,
      tags: oKlassInstance.tags,
      relationships: oKlassInstance.relationships,
      defaultAssetInstanceId: sDefualtAssetInstanceId,
      selectedLanguageCodes: aSelectedLanguageCodes,
      klassIds: oKlassBasicInformationData.klassIds,
      taxonomyIds: oKlassBasicInformationData.taxonomyIds,
      creationLanguage: sDefaultLanguage,
      sourceIds: aKlassInstanceIds
    };

    let oCallback = {
      updateBreadcrumbRootNodeLabel: _updateBreadcrumbRootNodeLabel
    }
    let sUrl = getRequestMapping().CreateGoldenRecord;
    let oAjaxExtraData = {
      URL: sUrl,
      postData: oRequestData
    };
    oCallback.breadCrumbData = {
      extraData: oAjaxExtraData
    };

    _createGoldenRecordCall(oCallback, oAjaxExtraData);
  };

  let _createGoldenRecordCall = function (oCallbackData, oAjaxExtraData) {
    let fSuccess = _successCreateGoldenRecord.bind(this, oCallbackData);
    let fFailure = _failureCreateGoldenRecord.bind(this);
    CS.putRequest(oAjaxExtraData.URL, {}, oAjaxExtraData.postData, fSuccess, fFailure);
  };

  let _successCreateGoldenRecord = function (oCallback, oResponse) {
    var oContentStore = ContentUtils.getContentStore();
    oResponse = oResponse.success;
    GoldenRecordProps.resetGoldenRecordComparisonProps();
    alertify.success(getTranslation().GOLDEN_RECORD_CREATED_SUCCESSFULLY);
    oContentStore.processGetArticleResponse(oResponse);
    var oCreatedEntity = oResponse.klassInstance;
    let sEntityName = ContentUtils.getContentName(oCreatedEntity);

    let sHelpScreenId;
    let sType = sHelpScreenId = BreadCrumbModuleAndHelpScreenIdDictionary.CONTENT;
    let oBreadcrumbItem = BreadcrumbStore.createBreadcrumbItem(oCreatedEntity.id, sEntityName, sType, sHelpScreenId);

    GoldenRecordProps.setShowGoldenRecordBuckets(false);

    CS.assign(oCallback.breadCrumbData, oBreadcrumbItem);

    BreadcrumbStore.addNewBreadCrumbItem(oCallback.breadCrumbData);

    if(oCallback && oCallback.updateBreadcrumbRootNodeLabel){
      oCallback.updateBreadcrumbRootNodeLabel();
    }
    GoldenRecordProps.setShowGoldenRecordBuckets(false);

    _triggerChange();
  };

  let _failureCreateGoldenRecord = function (oResponse) {
    /**Show alert message when sources are modified while creation of golden record */
    let oFailure = oResponse.failure;
    if (!CS.isEmpty(oFailure) && oFailure.exceptionDetails[0].key === "SourceRecordsUpdatedForBucketException") {
      _handleSourcesModifiedByAnotherUser();
      return;
    }
    ContentUtils.failureCallback(oResponse, 'failureCreateGoldenRecord', getTranslation());
  };

  let _saveGoldenRecord = function () {
    let oKlassInstance = GoldenRecordProps.getDummyKlassInstance();
    MNMUtils.postProcessDummyKlassInstanceForSave(oKlassInstance);

    let aSelectedLanguages = GoldenRecordProps.getSelectedLanguageList();
    let aSelectedLanguageCodes = aSelectedLanguages.map(oLanguage => oLanguage.code);
    let sBucketId = GoldenRecordProps.getActiveBucketId();
    let oGoldenRecordRule = GoldenRecordProps.getGoldenRecordRule();
    let oKlassBasicInformationData = GoldenRecordProps.getSelectedKlassBasicInformationData();
    let sActiveGoldenRecordId = GoldenRecordProps.getActiveGoldenRecordId();
    let oActiveGoldenRecordInstance = GoldenRecordProps.getActiveGoldenRecordInstance();
    let sGoldenArticleBaseType = oActiveGoldenRecordInstance.baseType;
    let bIsGoldenRecordRemergeSourcesTabClicked = GoldenRecordProps.getIsGoldenRecordRemergeSourcesTabClicked();
    let aKlassInstanceIds = GoldenRecordProps.getSelectedKlassInstanceIds();

    let sDefualtAssetInstanceId = "";

    let oRequestData = {
      ruleId: oGoldenRecordRule.id,
      goldenRecordId: sActiveGoldenRecordId,
      attributes: oKlassInstance.attributes,
      dependentAttributes: oKlassInstance.dependentAttributes,
      tags: oKlassInstance.tags,
      relationships: oKlassInstance.relationships,
      defaultAssetInstanceId: sDefualtAssetInstanceId,
      selectedLanguageCodes: aSelectedLanguageCodes,
      klassIds: oKlassBasicInformationData.klassIds,
      taxonomyIds: oKlassBasicInformationData.taxonomyIds,
    };

    if(!bIsGoldenRecordRemergeSourcesTabClicked) {
      oRequestData.bucketId = sBucketId;
      oRequestData.sourceIds = aKlassInstanceIds;
    }

    let oCallback = {
      updateBreadcrumbRootNodeLabel: _updateBreadcrumbRootNodeLabel,
      functionToExecute: function () {
        let ContentStore = ContentUtils.getContentStore();
        let oCallback = {entityType: sGoldenArticleBaseType};
        ContentStore.fetchArticleById(sActiveGoldenRecordId, oCallback);
      }
    };

    let sUrl = getRequestMapping().SaveGoldenRecord;

    let oAjaxExtraData = {
      URL: sUrl,
      postData: oRequestData
    };
    oCallback.breadCrumbData = {
      extraData: oAjaxExtraData
    };
    _saveGoldenRecordCall(oCallback, oAjaxExtraData)
  };

  let _saveGoldenRecordCall = function (oCallback, oAjaxExtraData) {
    let fSuccess = _successSaveGoldenRecord.bind(this, oCallback);
    let fFailure = _failureSaveGoldenRecord.bind(this);
    CS.postRequest(oAjaxExtraData.URL, {}, oAjaxExtraData.postData, fSuccess, fFailure);
  };

  let _successSaveGoldenRecord = function (oCallback, oResponse) {
    GoldenRecordProps.resetGoldenRecordComparisonProps();
    ContentUtils.makeActiveEntityClean();
    alertify.success(ContentUtils.getDecodedTranslation(getTranslation().ENTITY_SAVED_SUCCESSFULLY,{ entity : getTranslation().GOLDEN_RECORD}));

    GoldenRecordProps.setShowGoldenRecordBuckets(false);

    if (oCallback && oCallback.functionToExecute) {
      oCallback.functionToExecute();
    }
  };

  let _failureSaveGoldenRecord = function (oResponse) {
    /**Show alert message when sources are modified while saving golden record */
    let oFailure = oResponse.failure;
    if (!CS.isEmpty(oFailure) && oFailure.exceptionDetails[0].key === "SourceRecordsUpdatedForBucketException") {
      _handleSourcesModifiedByAnotherUser();
      return;
    }
    ContentUtils.failureCallback(oResponse, 'failureSaveGoldenRecord', getTranslation());
  };

  let _postProcessDummyKlassInstanceForCreate = function (oKlassInstance) {
    let oKlassInstanceAttributes = oKlassInstance.attributes;
    let oKlassInstanceDependentAttributes = oKlassInstance.dependentAttributes;
    let oKlassInstanceTags = oKlassInstance.tags;
    let aKlassInstanceRelationship = oKlassInstance.relationships;
    let aKlassInstanceNatureRelationships = oKlassInstance.natureRelationships;

    let oNewAttributeInstances = {};
    let oNewDependentAttributeInstances = {};
    let oNewTagInstances = {};

    /**
     * Filter to get attributes with non-empty value
     */
    CS.forEach(oKlassInstanceAttributes, function (aAttributeList, sKey) {
      let oAttribute = CS.get(aAttributeList, 0);
      if (CS.isNumber(oAttribute.value)) {
        oNewAttributeInstances[sKey] = [oAttribute];
      } else if (!CS.isEmpty(oAttribute.value)) {
        oNewAttributeInstances[sKey] = [oAttribute];
      }
    });

    /**
     * Filter to get dependent attributes with non-empty value
     */
    CS.forEach(oKlassInstanceDependentAttributes, function (oLanguageAttributeMap, sLanguageId) {
      let oDependentNewAttributeInstances = {};
      CS.forEach(oLanguageAttributeMap, function (aAttributeList, sKey) {
        let oAttribute = CS.get(aAttributeList, 0);
        if(!CS.isEmpty(oAttribute.value)) {
          oDependentNewAttributeInstances[sKey] = [oAttribute];
        }
      });
      if(!CS.isEmpty(oDependentNewAttributeInstances)) {
        oNewDependentAttributeInstances[sLanguageId] = oDependentNewAttributeInstances;
      }
    });

    /**
     * Filter to get tag with non-zero relevance
     */
    CS.forEach(oKlassInstanceTags, function (aTagList, sKey) {
      let oTag = CS.get(aTagList, 0);
      let aTagValuesWithoutZeroRelevance =[];
      CS.forEach(oTag.tagValues, function (oTagValue) {
        if(oTagValue.relevance) {
          delete oTag.tagId;
          aTagValuesWithoutZeroRelevance.push({
            id: oTagValue.id,
            tagId: oTagValue.tagId,
            relevance: oTagValue.relevance
          });
        }
      });

      if(!CS.isEmpty(aTagValuesWithoutZeroRelevance)) {
        oTag.tagValues = aTagValuesWithoutZeroRelevance;
        delete oTag.previousSelectedValues;
        oNewTagInstances[sKey] = [oTag];
      }

    });

    /**
     * Filter to get relationship with not-empty sourceKlassInstanceId
     */
    CS.remove(aKlassInstanceRelationship, function (oRelationshipInstance) {
      delete oRelationshipInstance.referencedAssetsData;
      delete oRelationshipInstance.paginationData;
      return CS.isEmpty(oRelationshipInstance.sourceKlassInstanceId);
    });

    /**
     * Filter to get nature relationship with not-empty sourceKlassInstanceId
     */
    CS.remove(aKlassInstanceNatureRelationships, function (oRelationshipInstance) {
      delete oRelationshipInstance.referencedAssetsData;
      delete oRelationshipInstance.paginationData;
      return CS.isEmpty(oRelationshipInstance.sourceKlassInstanceId);
    });

    /**
     * Re-assigning klassInstanceValues
     */
    oKlassInstance.attributes = oNewAttributeInstances;
    oKlassInstance.dependentAttributes = oNewDependentAttributeInstances;
    oKlassInstance.tags = oNewTagInstances;
    oKlassInstance.relationships = CS.concat(aKlassInstanceRelationship, aKlassInstanceNatureRelationships);
    delete oKlassInstance.natureRelationships;
  };

  let _handleGoldenRecordBucketsPaginationChanged = function (oPaginationData, oFilterContext) {
    let oGoldenRecordBucketsPaginationData = GoldenRecordProps.getGoldenRecordBucketsPaginationData();
    oGoldenRecordBucketsPaginationData.from = oPaginationData.from;
    oGoldenRecordBucketsPaginationData.pageSize = oPaginationData.pageSize;

    let oCallbackData = {
      filterContext: oFilterContext
    };

    _fetchGoldenRecordBuckets(oCallbackData);
  };

  let _hadleGoldenRecordDialogAbortButtonClicked = function () {
    let bIsGoldenRecordRemergeSourcesTabClicked = GoldenRecordProps.getIsGoldenRecordRemergeSourcesTabClicked();
    if(!bIsGoldenRecordRemergeSourcesTabClicked) {
      let oScreenProps = ContentScreenProps.screen;
      let sDefaultViewMode = oScreenProps.getDefaultViewMode();
      oScreenProps.setViewMode(sDefaultViewMode);
    }
    GoldenRecordProps.resetGoldenRecordComparisonProps();
    _triggerChange();
  };

  let _handleGoldenRecordDialogActionButtonClicked = function (sButtonId) {
    switch (sButtonId) {
      case "cancel":
        _hadleGoldenRecordDialogAbortButtonClicked();
        break;
    }
  };

  let _handleFilterButtonClicked = function (oCallbackData) {
    _fetchGoldenRecordBuckets(oCallbackData);
  };

  let _handleDashboardBucketsTabClicked = function (oCallbackData) {
    GoldenRecordProps.setShowGoldenRecordBuckets(true);
    return _fetchGoldenRecordBuckets(oCallbackData);
  };

  let _updateBreadcrumbRootNodeLabel = function () {
    let aDashboardTabList = DashboardScreenProps.getDashboardTabsList();
    let sSelectedTabId = DashboardScreenProps.getSelectedDashboardTabId();
    let oSelectedTab = CS.find(aDashboardTabList, {id: sSelectedTabId});
    let aBreadcrumbPath = ContentScreenProps.breadCrumbProps.getBreadCrumbData();
    aBreadcrumbPath[0].label = oSelectedTab.label;
  };

  let _handleContentComparisonMatchAndMergeLanguageSelectionClick = (sSelectedLanguageId) => {
    GoldenRecordProps.setMNMSelectedLanguageId(sSelectedLanguageId);
    let aMergedLanguageList = GoldenRecordProps.getMNMMergedLanguageList();
    !CS.includes(aMergedLanguageList, sSelectedLanguageId) && aMergedLanguageList.push(sSelectedLanguageId);
    _fetchMatchAndMergeDataForProperties(sSelectedLanguageId);
  };

  let _handleComparisonDataFirstRowClicked = () => {
    let {sPropertyId, sFirstTableId} = MNMUtils.getComparisonDataFirstRowInfo(GoldenRecordProps.getMatchAndMergeComparisonData());
    _handleGoldenRecordMatchAndMergeViewTableRowClicked(sPropertyId, sFirstTableId);
  };

  let _handleMNMBackButtonClicked = (bIsSourceTabClicked) => {
    GoldenRecordProps.resetMNMData();
    let sId = GoldenRecordProps.getActiveBucketId();
    if(bIsSourceTabClicked) {
      sId = GoldenRecordProps.getActiveGoldenRecordId();
    }
    _fetchDataForGoldenRecordMatchAndMerge(sId, bIsSourceTabClicked);
  };

  let _handleContentComparisonMatchAndMergePropertyValueChanged = function (sPropertyId, sTableId, oNewVal) {
    let oComparisionData = GoldenRecordProps.getMatchAndMergeComparisonData();
    let sActiveGoldenRecordId = GoldenRecordProps.getActiveGoldenRecordId();
    let oDummyKlassInstance = GoldenRecordProps.getDummyKlassInstance();
    let sSelectedLanguageId = GoldenRecordProps.getMNMSelectedLanguageId();
    let oActivePropertyDetailedData = GoldenRecordProps.getMNMActivePropertyDetailedData();
    let oConfigDetails = GoldenRecordProps.getGoldenRecordComparisonConfigDetails();

    let oData = {
      comparisonData:oComparisionData,
      activeGoldenRecordId:sActiveGoldenRecordId,
      dummyKlassInstance:oDummyKlassInstance,
      selectedLanguageId : sSelectedLanguageId,
      activePropertyDetailedData:oActivePropertyDetailedData
    };
    MNMUtils.processContentComparisonMatchAndMergePropertyValueChanged(sPropertyId, sTableId, oData, oNewVal, oConfigDetails.referencedTags);
  };

  let _setUpDummyKlassInstanceForMatchAndMerge = function (oConfigDetails, oPropertyRecommendations, oGoldenRecordInstance) {
    let oMatchPropertiesData = GoldenRecordProps.getGoldenRecordMatchPropertiesData();
    let sSelectedLanguageId = GoldenRecordProps.getMNMSelectedLanguageId();
    let oDummyKlassInstance = MNMUtils.createDummyKlassInstance(GoldenRecordProps.getDummyKlassInstance());
    MNMUtils.fillDummyKlassInstanceWithElements(oDummyKlassInstance, oConfigDetails, oPropertyRecommendations, oGoldenRecordInstance, oMatchPropertiesData, sSelectedLanguageId);
    GoldenRecordProps.setDummyKlassInstance(oDummyKlassInstance);
  };

  /**
   *
   * @param oConfigDetails
   * @param aKlassIds - Selected klass Ids
   * @param aTaxonomyIds - Selected taxanomy ids
   * @param oGoldenRecordInstance - Golden record instance if golden record present in bucket
   * @description - Prepare Match and merge data for properties
   * @private
   */
  let _preProcessPropertiesDataForComparison = function (oConfigDetails, aKlassIds, aTaxonomyIds, oGoldenRecordInstance) {
    let oLayoutData = MNMUtils.getLayoutSkeleton();
    // let bIsGoldenRecordComparison = true;
    let oPropertyRecommendations = GoldenRecordProps.getPropertyRecommendations();
    let sLanguageId  = GoldenRecordProps.getMNMSelectedLanguageId();

    /*******************Dummy Instance Processing ********************/
    _setUpDummyKlassInstanceForMatchAndMerge(oConfigDetails, oPropertyRecommendations, oGoldenRecordInstance);

    /*****************Language Selection Processing****************/
    let aSelectedLanguageList = GoldenRecordProps.getSelectedLanguageList();
    MNMUtils.makeInstanceComparisonRowDataForLanguageTable(oLayoutData, aSelectedLanguageList, sLanguageId);

    /*****************Basic Info Section Data Processing****************/
    MNMUtils.makeInstancesComparisonRowDataForFixedInformation(oLayoutData, oConfigDetails, aKlassIds, aTaxonomyIds, GoldenRecordProps.getSelectedKlassBasicInformationData());

    /*****************PC Sections Data Processing****************/
    MNMUtils.prepareDataForPropertyCollection(GoldenRecordProps.getDummyKlassInstance(), oLayoutData, oConfigDetails, GoldenRecordProps.getGoldenRecordMatchPropertiesData(),sLanguageId);

    GoldenRecordProps.setMatchAndMergeComparisonData(oLayoutData);
  };


  /**
   *
   * @param oDummyKlassInstance
   * @param oLayoutData
   * @param oResponse
   * @description - Prepares Comparison data for relationship
   * @private
   */
  let _makeComparisonDataForRelationshipsTable = function (oDummyKlassInstance, oLayoutData, oResponse) {
    let oConfigDetails = oResponse.configDetails;
    let oReferencedRelationships = oConfigDetails.referencedRelationships;

    let oFunctionParameters = {
      relationshipKlassInstances : oDummyKlassInstance.relationships,
      referencedRelationships : oReferencedRelationships,
      propertyType: RELATIONSHIP,
      referencedElements: oConfigDetails.referencedElements,
      recommendation: oResponse.recommendation,
      klassInstances: oResponse.klassInstances,
      referencedAssets: oResponse.referencedAssets,
      referencedTags: oResponse.referencedTags,
      goldenRecordRelationshipInstances: oResponse.goldenRecordRelationshipInstances
    };
    let aRowData = _makeRelationshipComparisonData(oFunctionParameters);
    if (!CS.isEmpty(aRowData)) {
      oLayoutData.tableData[RELATIONSHIP_TABLE] = {
        tableHeaderLabel: getTranslation().RELATIONSHIPS,
        hideEditRowButton: true,
        rowData: aRowData
      };
      oLayoutData.tableIds.push(RELATIONSHIP_TABLE);
    }
  };

  /**
   *
   * @param oDummyKlassInstance
   * @param oLayoutData
   * @param oResponse
   * @description - Prepares Comparison data for Nature relationship
   * @private
   */
  let _makeComparisonDataForNatureRelationshipsTable = function (oDummyKlassInstance, oLayoutData, oResponse) {
    let oConfigDetails = oResponse.configDetails;
    let oReferencedRelationships = oConfigDetails.referencedNatureRelationships;


    let oFunctionParameters = {
      relationshipKlassInstances : oDummyKlassInstance.natureRelationships,
      referencedRelationships : oReferencedRelationships,
      propertyType: NATURE_RELATIONSHIP,
      referencedElements: oConfigDetails.referencedElements,
      recommendation: oResponse.recommendation,
      klassInstances: oResponse.klassInstances,
      referencedAssets: oResponse.referencedAssets,
      referencedTags: oResponse.referencedTags,
      goldenRecordRelationshipInstances: oResponse.goldenRecordRelationshipInstances
    };
    let aRowData = _makeRelationshipComparisonData(oFunctionParameters);
    if (!CS.isEmpty(aRowData)) {
      oLayoutData.tableData[NATURE_RELATIONSHIP_TABLE] = {
        tableHeaderLabel: getTranslation().NATURE_RELATIONSHIPS,
        hideEditRowButton: true,
        rowData: aRowData
      };
      oLayoutData.tableIds.push(NATURE_RELATIONSHIP_TABLE);
    }
  };

  /**
   *
   * @param oFunctionParameters
   * @returns {Array}
   * @private
   * @description Common function for relationship and nature relationship for preparing relationship instances data
   */
  let _makeRelationshipComparisonData = function (oFunctionParameters) {
    let aRowData = [];
    let aRelationshipKlassInstance = oFunctionParameters.relationshipKlassInstances;
    let oReferencedRelationships = oFunctionParameters.referencedRelationships;
    let sPropertyType = oFunctionParameters.propertyType;
    let oReferencedElements = oFunctionParameters.referencedElements;
    let oRecommendation = oFunctionParameters.recommendation;
    let oKlassInstances = oFunctionParameters.klassInstances;
    let oReferencedAssets = oFunctionParameters.referencedAssets;
    let oReferencedTags = oFunctionParameters.referencedTags;
    let oGoldenRecordRelationshipInstances = oFunctionParameters.goldenRecordRelationshipInstances;
    let oRelationshipPaginationData = GoldenRecordProps.getRelationshipPaginationData();

    try {
      CS.forEach(aRelationshipKlassInstance, function (oRelationshipInstance) {
        try {
          let sRelationshipId = oRelationshipInstance.relationshipId;
          let sSideId = oRelationshipInstance.sideId;
          let oMasterRelationship = oReferencedRelationships[sRelationshipId];
          let oReferencedElement = oReferencedElements[sSideId];
          let oSide1 = oMasterRelationship.side1 || {};
          let oSide2 = oMasterRelationship.side2 || {};
          let oOppositeSide = oReferencedElement.id === oSide1.elementId ? oSide2 : oSide1;

          let aAssetsData = [];
          let oPaginationData = {};

          if(!CS.isEmpty(oRelationshipInstance.sourceKlassInstanceId)) {
            let oPropertyRecommendation = oRecommendation[sSideId] || {};
            let oGRRelationshipInstance = oGoldenRecordRelationshipInstances[sSideId] || {};
            let aRelationshipInstances = [];
            let iTotalPaginationCount = 0;

            if(!CS.isEmpty(oPropertyRecommendation)) {
              /** Process Recommondation Data */
              let oTargetRelationshipInstances = oPropertyRecommendation.targetRelationshipInstances;
              aRelationshipInstances = oTargetRelationshipInstances.relationshipInstances;
              iTotalPaginationCount = oTargetRelationshipInstances.totalCount;
            } else if(!CS.isEmpty(oGRRelationshipInstance)) {
              /** Process Golden record relationship instance if golden record is present **/
              aRelationshipInstances = oGRRelationshipInstance.relationshipInstances;
              iTotalPaginationCount = oGRRelationshipInstance.totalCount;
            }

            aAssetsData = _getReferencedAssetDataForRelationship(aRelationshipInstances, oKlassInstances, oReferencedTags, oReferencedAssets);
            _setPaginationData(sSideId, iTotalPaginationCount, oRelationshipPaginationData.from, oRelationshipPaginationData.size, aAssetsData.length);
            oPaginationData = _getPaginationData(sSideId);
            oRelationshipInstance.referencedAssetsData = aAssetsData;
            oRelationshipInstance.paginationData = oPaginationData;
          }

          aRowData.push({
            relationshipId: sRelationshipId,
            id: sSideId,
            label: CS.getLabelOrCode(oOppositeSide),
            propertyType: sPropertyType,
            canAdd: !!oReferencedElement.canAdd,
            canDelete: !!oReferencedElement.canDelete,
            rendererType: 'ELEMENT',
            referencedAssetsData: oRelationshipInstance.referencedAssetsData,
            paginationData: oRelationshipInstance.paginationData,
            sourceKlassInstanceId: oRelationshipInstance.sourceKlassInstanceId || "",
            isDisabled: false,
          });
        }
        catch (oException) {
          ExceptionLogger.error(oException);
        }
      });
      return aRowData;
    }
    catch (oException) {
      ExceptionLogger.error(oException);
    }
  };

  /**
   * @description - Dummy klass instance relationship/nature relationship model
   * @returns {{relationshipId: *, sourceKlassInstanceId: (*|string), sideId: *, referencedAssetsData: {}, paginationData: {}}}
   * @private
   */
  let _getRelationshipInstanceModel = function (sElementId, sSideId, oPropertyRecommendation, oGoldenRecordInstanceRelationships = {}) {
    let oRelationship = oGoldenRecordInstanceRelationships[sSideId] || oPropertyRecommendation[sSideId] || {};
    return {
      relationshipId: sElementId,
      sourceKlassInstanceId: oRelationship.contentId || "",
      sideId: sSideId,
      referencedAssetsData: {},
      paginationData: {},
    }
  };


  /**
   * @description - Prepares relationship data for dummy klass instance
   * @private
   */
  let _fillDummyKlassInstanceWithRelationship = function (oDummyKlassInstance, oReferencedRelationships, oReferencedElements, oPropertyRecommendations,
                                                          oGoldenRecordInstanceRelationships) {
    let aKlassInstanceRelationship = oDummyKlassInstance.relationships;
    let aExistingElementInstance = [];
    let oElementInstance = {};
    CS.forEach(oReferencedRelationships, function (oReferencedRelationship) {
      if (oReferencedRelationship.id === "standardArticleGoldenArticleRelationship") {
        return;
      }
      let aRelationshipElements = CS.filter(oReferencedElements, {propertyId: oReferencedRelationship.id});

      CS.forEach(aRelationshipElements, function (oElement) {
        aExistingElementInstance = CS.find(aKlassInstanceRelationship, {sideId: oElement.id});
        if (CS.isEmpty(aExistingElementInstance) && !oElement.isNature) {
          oElementInstance = _getRelationshipInstanceModel(oElement.propertyId, oElement.id, oPropertyRecommendations, oGoldenRecordInstanceRelationships);
          aKlassInstanceRelationship.push(oElementInstance);
        }
      });

    });
  };

  /**
   * @description - Prepares nature relationship data for dummy klass instance
   * @private
   */
  let _fillDummyKlassInstanceWithNatureRelationship = function (oDummyKlassInstance, oReferencedNatureRelationships, oReferencedElements, oPropertyRecommendations,
                                                                oGoldenRecordInstanceNatureRelationships) {
    let oKlassInstanceNatureRelationship = oDummyKlassInstance.natureRelationships;
    let aExistingElementInstance = [];
    let oElementInstance = {};
    CS.forEach(oReferencedNatureRelationships, function (oReferencedNatureRelationship) {
      let aNatureRelationshipElement = CS.filter(oReferencedElements, {propertyId: oReferencedNatureRelationship.id});

      CS.forEach(aNatureRelationshipElement, function (oElement) {
        aExistingElementInstance = CS.find(oKlassInstanceNatureRelationship, {sideId: oElement.id});
        if (CS.isEmpty(aExistingElementInstance) && oElement.isNature) {
          oElementInstance = _getRelationshipInstanceModel(oElement.propertyId, oElement.id, oPropertyRecommendations,
              oGoldenRecordInstanceNatureRelationships);
          oKlassInstanceNatureRelationship.push(oElementInstance);
        }
      });

    });
  };



  /**
   *
   * @param sViewContext - Context from where pagination is changed ("propertyView" or "propertyDetailView")
   * @param sPropertyId - Property Id of which pagination changed
   * @param sTableId - Table id of respective propertyId
   * @param oPaginationData - Updated pagination data
   * @param sSourceKlassInstanceId - Id of source whose pagination is changed
   * @private
   */
  let _handleMnmRelationshipPaginationChanged = function (sViewContext, sPropertyId, sTableId, oPaginationData, sSourceKlassInstanceId) {
    let sURL = ContentScreenRequestMapping.LoadRelationshipElementsBySourceId;
    let oComparisionData = GoldenRecordProps.getMatchAndMergeComparisonData();
    let oTableData = oComparisionData.tableData[sTableId];
    let oPropertyData = CS.find(oTableData.rowData, {id: sPropertyId});

    let sSelectedLanguageId = GoldenRecordProps.getMNMSelectedLanguageId();
    let sBucketId = GoldenRecordProps.getActiveBucketId();
    let oQueryString = {sourceId: sSourceKlassInstanceId};

    let oRequestData = {
      goldenRecordId: GoldenRecordProps.getActiveGoldenRecordId() || null,
      languageCode: sSelectedLanguageId,
      relationshipId: oPropertyData.relationshipId,
      isNatureRelationship: (oPropertyData.propertyType === NATURE_RELATIONSHIP),
      from: oPaginationData.from,
      size: oPaginationData.pageSize,
      sourceId: sSourceKlassInstanceId,
      sideId: sPropertyId
    };

    let bIsGoldenRecordRemergeSourcesTabClicked = GoldenRecordProps.getIsGoldenRecordRemergeSourcesTabClicked();
    if(!bIsGoldenRecordRemergeSourcesTabClicked) {
      oRequestData.bucketId = sBucketId;
    }

    let fSuccess = successRelationshipPaginationChanged.bind(this, sViewContext, sPropertyId, sTableId, oPaginationData, sSourceKlassInstanceId);
    let fFailure = failureRelationshipPaginationChanged;
    CS.postRequest(sURL, oQueryString, oRequestData, fSuccess, fFailure);
  };

  let successRelationshipPaginationChanged = function (sViewContext, sPropertyId, sTableId, oPaginationData, sSourceKlassInstanceId, oResponse) {
    oResponse = oResponse.success;
    let oSourceIdInstancesMap = oResponse.sourceIdRelationshipInstancesMap;
    let oKlassInstancesMap = oResponse.klassInstances;
    let oConfigDetails = oResponse.configDetails || {};
    let oReferencedTags = oConfigDetails.referencedTags;
    let oReferencedAssets = oResponse.referencedAssets;

    let oRelationshipInstances = oSourceIdInstancesMap[sSourceKlassInstanceId];
    let aRelationshipInstances = oRelationshipInstances ? oRelationshipInstances.relationshipInstances : [];

    let oDataToUpdate = {};
    let sPaginationContext = "";

    switch(sViewContext) {
      case GRConstants.PROPERTY_VIEW:
        oDataToUpdate = _getPropertyData(sPropertyId, sTableId);
        sPaginationContext = sPropertyId;
        break;

      case GRConstants.PROPERTY_DETAIL_VIEW:
        oDataToUpdate = _getPropertyDetailData(sPropertyId, sSourceKlassInstanceId);
        sPaginationContext = sSourceKlassInstanceId;
        break;
    }

    oDataToUpdate.referencedAssetsData = _getReferencedAssetDataForRelationship(aRelationshipInstances, oKlassInstancesMap, oReferencedTags, oReferencedAssets);
    _setPaginationData(sPaginationContext, oRelationshipInstances.totalCount, oPaginationData.from, oPaginationData.pageSize, (oDataToUpdate.referencedAssetsData).length);
    oDataToUpdate.paginationData = _getPaginationData(sPaginationContext);

    _triggerChange();
  };

  let failureRelationshipPaginationChanged = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureRelationshipPaginationChanged', getTranslation());
  };

  /**
   * @param aRelationshipInstances
   * @param oKlassInstances
   * @param oReferencedTags
   * @param oReferencedAssets
   * @returns {Array}
   * @description - Prepares referenced Assets data for relationships Instances
   * @private
   */
  let _getReferencedAssetDataForRelationship = function (aRelationshipInstances, oKlassInstances, oReferencedTags, oReferencedAssets) {
    let aReferencedAssetData = [];
    CS.forEach(aRelationshipInstances, function (oRelationshipInstance) {
      let sSide2 = oRelationshipInstance.side2InstanceId;
      let oKlassInstance =  oKlassInstances[sSide2];
      aReferencedAssetData.push(_prepareAssetData(oKlassInstance, oRelationshipInstance, oReferencedAssets, oReferencedTags));
    });
    return aReferencedAssetData;
  };

  /**
   *
   * @param oKlassInstance
   * @param oRelationshipInstance
   * @param oReferencedAssets
   * @param oReferencedTags
   * @returns {{instanceName: *, context: Array, assetData: *}}
   * @description - Prepares Data for one relationship
   * @private
   */
  let _prepareAssetData = function (oKlassInstance, oRelationshipInstance, oReferencedAssets, oReferencedTags) {
    let sDefualtAssetInstanceId = oKlassInstance.defaultAssetInstanceId;
    let oReferencedAsset = sDefualtAssetInstanceId ? oReferencedAssets[sDefualtAssetInstanceId] : oKlassInstance;
    let sContext = oRelationshipInstance.context;
    return ({
      instanceName: oReferencedAsset.name,
      context: !CS.isEmpty(sContext) ? _getRelationshipContextData(sContext, oRelationshipInstance.tags, oReferencedTags) : [],
      assetData: ContentUtils.getAssetDataObject(oReferencedAsset)
    });
  };

  let _getRelationshipContextData = function (oContext, aTags, oReferencedTags) {
    let aTagInstanceIds = oContext.tagInstanceIds;
    let aTagLabelList = [];

    CS.forEach(aTagInstanceIds, function (sTagId) {
      let sTagLabel = "";
      let oTagGroup = CS.find(aTags, {id: sTagId});
      var aTagValues = oTagGroup.tagValues;
      var oMasterTagGroup = CS.find(oReferencedTags, {id: oTagGroup.tagId});

      if(!CS.isEmpty(oMasterTagGroup)){
        sTagLabel = CS.getLabelOrCode(oMasterTagGroup) + ": ";
        CS.forEach(aTagValues, function (oTagValue) {

          if (oTagValue.relevance !== 0) {
            var oTagMaster = CS.find(oMasterTagGroup.children, {id: oTagValue.tagId});

            if (!CS.isEmpty(oTagMaster)) {
              sTagLabel += CS.getLabelOrCode(oTagMaster) + " ";
            }
          }
        });
      }
      aTagLabelList.push(sTagLabel);
    });
    return aTagLabelList;
  };

  let _getPropertyData = function (sPropertyId, sTableId) {
    let oComparisionData = GoldenRecordProps.getMatchAndMergeComparisonData();
    let oTableData = oComparisionData.tableData[sTableId];
    return CS.find(oTableData.rowData, {id: sPropertyId});
  };

  let _getPropertyDetailData = function (sPropertyId, sSourceKlassInstanceId) {
    let oMNMActivePropertyDetailedData = GoldenRecordProps.getMNMActivePropertyDetailedData();
    let oPropertyDetailedData = oMNMActivePropertyDetailedData[sPropertyId];
    let oElements = oPropertyDetailedData.elements;
    let oRelationshipInstance = oElements[sSourceKlassInstanceId];
    return oRelationshipInstance.property;
  };

  let _handleSourcesModifiedByAnotherUser = function () {
    alertify.error(getTranslation().ERROR_RECORD_MODIFIED_BY_ANOTHER_USER);
    _hadleGoldenRecordDialogAbortButtonClicked();
  };

  let _validateKlassInstances = function (aPreviousKlassInstanceList, aCurrentKlassInstanceIds) {
    let bIsValid = false;

    let aPreviousKlassInstanceIds = CS.map(aPreviousKlassInstanceList, 'id');
    if(aPreviousKlassInstanceIds.length === aCurrentKlassInstanceIds.length) {
      bIsValid = CS.isEmpty(CS.xor(aCurrentKlassInstanceIds, aPreviousKlassInstanceIds));
    }
    if (!bIsValid) {
      _handleSourcesModifiedByAnotherUser();
      return false;
    }
    return bIsValid;
  };

  let _handleContentComparisonMnmRemoveRelationshipButtonClicked = function (sPropertyId, sTableId, oPropertyToSave) {
    _handleContentComparisonMatchAndMergePropertyValueChanged(sPropertyId, sTableId, oPropertyToSave);
  };

  let _handleContentComparisonFullScreenButtonClicked = function () {
    CustomActionDialogStore.showConfirmDialog(getTranslation().WARNING_MESSAGE_CHANGES_WILL_BE_LOST,
        "",
        function () {
          let bIsFullScreenMode = GoldenRecordProps.getIsFullScreenMode();
          GoldenRecordProps.setIsFullScreenMode(!bIsFullScreenMode);
          _hadleGoldenRecordDialogAbortButtonClicked();
        },
        function (oEvent) {});
  };

  /**
   * @description - As response does not contains tag value instance for boolean with 0 relevance.
   * This method will update aInstanceTags, with it if configured in aRuleTags
   *
   * @param aInstanceTags - Existing tags
   * @param oReferencedTags - Referenced tags to get compare boolean tag type
   * @param aRuleTags - Configured tags in golden record rule
   */
  let addBooleanFalseValueTagInstance = function (aInstanceTags, oReferencedTags, aRuleTags) {
    let aExistingArticleTagCodes = [];
    CS.forEach(aInstanceTags, function (oTagInstance) {
      aExistingArticleTagCodes.push(oTagInstance.tagId);
    });

    CS.forEach(oReferencedTags, function (oTagMap, sTagCode) {
      if (aRuleTags && aRuleTags.includes(sTagCode)
          && oTagMap.tagType === TagTypeConstants.TAG_TYPE_BOOLEAN
          && !aExistingArticleTagCodes.includes(sTagCode)) {
        let oTagInstance = {
          baseType: BaseTypesDictionary.tagInstanceBaseType,
          id: sTagCode,
          tagId: sTagCode,
          tagValues: [{
            code: oTagMap.children[0].code,
            id: oTagMap.children[0].id,
            tagId: oTagMap.children[0].id,
            relevance: 0
          }]
        };
        aInstanceTags.push(oTagInstance);
      }
    });
  };


  let _handleGoldenRecordComparisonLanguageChanged = function (sLanguageForComparison) {
    let aLanguagesInfo = SessionProps.getLanguageInfoData();
    let aDataLanguages = aLanguagesInfo.dataLanguages;
    let oSelectedLanguageForComparison = CS.find(aDataLanguages, {id: sLanguageForComparison});
    let sLanguageForComparisonCode = oSelectedLanguageForComparison.code;
    let oExtraData = {
      dataLanguage: sLanguageForComparisonCode
    };

    let oCallbackData = {
      preFunctionToExecute: function () {
        ContentScreenProps.screen.setSelectedLanguageForComparison(sLanguageForComparisonCode);

        if (!CS.isEmpty(oSelectedLanguageForComparison)) {
          MomentUtils.setCurrentDateFormat(oSelectedLanguageForComparison);
          NumberUtils.setCurrentNumberFormat(oSelectedLanguageForComparison);
        }
      }
    };

    _getViewSourcesButtonClicked(oCallbackData, oExtraData);
  };

  let _getViewSourcesButtonClicked = (oCallbackData, oExtraData) => {
    let oActiveEntity = ContentUtils.getActiveContent();
    let oData = {
      goldenRecordId: oActiveEntity.id
    };
    GoldenRecordProps.setIsGoldenRecordViewSourcesDialogOpen(true);
    let sUrl = getRequestMapping().GetDataForGoldenRecordMergeLanugageSelection;
    let fSuccess = successFetchDataForViewSources.bind(this, oCallbackData);
    let fFailure = failureFetchDataForViewSources;
    CS.postRequest(sUrl, {}, oData, fSuccess, fFailure, oExtraData);
  };

  let successFetchDataForViewSources = (oCallbackData, oResponse) => {
    oResponse = oResponse.success;
    let aSourceInstances = oResponse.sourceInstances;
    let aSelectedIds = [];
    let aSelectedIdsForSuccess = [];

    GoldenRecordProps.setGoldenRecordLanguageInstances(oResponse.configDetails.referencedLanguages);
    CS.forEach(aSourceInstances, function (oSources) {
      aSelectedIds.push({"id": oSources.id});
      aSelectedIdsForSuccess.push(oSources.id)
    });
    var oListForComparision = {"productListsToCompare": aSelectedIds};
    var sUrl = getRequestMapping().GetInstanceComparisionData;
    var oData = {};
    var fSuccess = successGetGoldenRecordComparisonData.bind(this, aSelectedIdsForSuccess, oCallbackData);
    var fFailure = failureGetGoldenRecordComparisonData;
    CS.postRequest(sUrl, oData, oListForComparision, fSuccess, fFailure, false, {});
  };

  let failureFetchDataForViewSources = (oResponse) => {
    ContentUtils.failureCallback(oResponse, 'failureFetchDataForMNMLanguageSelection', getTranslation());
  };

  var _getLayoutSkeleton = function () {
    return {
      attributeTable: {},
      tagTable: {},
      relationshipTable: {},
      headerInformationTable: {},
      fixedHeaderTable: {}
    };
  };

  var successGetGoldenRecordComparisonData = function (aSelectedInstanceIds, oCallbackData, oResponse) {
    var oResponseData = oResponse.success;
    var oConfigDetails = oResponseData.configDetails;
    var aKlassInstances = oResponseData.klassInstancesDetails;
    var oLayoutData = _getLayoutSkeleton();

    ContentScreenProps.screen.setMasterKlassInstanceListForComparison(aKlassInstances);
    if (oCallbackData && oCallbackData.preFunctionToExecute) {
      oCallbackData.preFunctionToExecute();
    } else{
      let sSelectedDataLanguageCode = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_DATA_LANGUAGE_CODE);
      ContentScreenProps.screen.setSelectedLanguageForComparison(sSelectedDataLanguageCode);
    }
    var aSelectedKlassInstances = ContentUtils.getOnlySelectedKlassInstances(aKlassInstances, aSelectedInstanceIds);
    let oKlassInstance = aSelectedKlassInstances && aSelectedKlassInstances[0] && aSelectedKlassInstances[0].klassInstance;
    let sScreenMode = ContentUtils.getScreenModeBasedOnEntityBaseType(oKlassInstance.baseType);

    ContentUtils.makeInstancesComparisonRowDataForTags(oLayoutData, oConfigDetails, false);
    ContentUtils.makeInstancesComparisonRowDataForAttributes(oLayoutData, oConfigDetails, false);
    ContentUtils.makeInstancesComparisonRowDataForRelationshipTab(oLayoutData, oConfigDetails, aSelectedKlassInstances, false);
    ContentUtils.makeInstancesComparisonRowDataForNatureRelationships(oLayoutData, oConfigDetails, aSelectedKlassInstances);
    ContentUtils.makeInstancesComparisonRowDataForHeaderInformation(oLayoutData, sScreenMode);
    ContentUtils.makeInstancesComparisonRowDataForFixedHeader(oLayoutData);
    ContentUtils.makeInstancesComparisonColumnData(oLayoutData, oConfigDetails, aSelectedKlassInstances);

    ContentScreenProps.screen.setContentComparisonLayoutData(oLayoutData);
    _triggerChange();
  };

  var failureGetGoldenRecordComparisonData = function (oResponse) {
    ContentUtils.failureCallback(oResponse, 'failureGetInstanceComparisionData', getTranslation());
  };

  /*******************  PUBLIC API  *******************/
  return {

    handleGoldenRecordBucketTabChanged: function (sBucketId, sTabId) {
      _handleGoldenRecordBucketTabChanged(sBucketId, sTabId);
    },

    fetchGoldenRecordBuckets: function (oCallbackData) {
      _fetchGoldenRecordBuckets(oCallbackData);
    },

    handleGoldenRecordBucketsPaginationChanged: function (oPaginationData, oFilterContext) {
      _handleGoldenRecordBucketsPaginationChanged(oPaginationData, oFilterContext)
    },

    fetchDataForGoldenRecordMatchAndMergeWrapper: function (sId, bIsSourceTabClicked) {
      _fetchDataForGoldenRecordMatchAndMergeWrapper(sId, bIsSourceTabClicked);
    },

    handleMatchMergeCellClicked: function () {
      _triggerChange();
    },

    handleGoldenRecordDialogActionButtonClicked: function (sButtonId) {
      _handleGoldenRecordDialogActionButtonClicked(sButtonId);
    },

    handleMatchMergeCellRemoveClicked: function () {
      _triggerChange();
    },

    handleFilterButtonClicked: function (oFilterContext) {
      _handleFilterButtonClicked({filterContext: oFilterContext});
    },

    handleDashboardBucketsTabClicked: function (oCallbackData) {
      return _handleDashboardBucketsTabClicked(oCallbackData);
    },

    handleGoldenRecordGridViewLanguageSelectButtonClicked(aSelectedContentIds, bSelectAllClicked){
      _handleGoldenRecordGridViewLanguageSelectButtonClicked(aSelectedContentIds, bSelectAllClicked);
      _triggerChange();
    },

    handleGoldenRecordGridViewDefaultLanguageSelectButtonClicked(sContentId, sPropertyId, sValue) {
      _handleGoldenRecordGridViewDefaultLanguageSelectButtonClicked(sContentId, sPropertyId, sValue);
      _triggerChange();
    },

    handleGoldenRecordMatchAndMergeViewTableRowClicked: function (sPropertyId, sTableId) {
      _handleGoldenRecordMatchAndMergeViewTableRowClicked(sPropertyId, sTableId);
    },

    handleContentComparisonMatchAndMergePropertyValueChanged: function (sPropertyId, sTableId, oNewVal) {
      _handleContentComparisonMatchAndMergePropertyValueChanged(sPropertyId, sTableId, oNewVal);
    },

    handleContentComparisonMatchAndMergeLanguageSelectionClick(sSelectedLanguageId) {
      _handleContentComparisonMatchAndMergeLanguageSelectionClick(sSelectedLanguageId);
    },

    handleStepperViewActiveStepChanged: function (sContext) {
      _handleStepperViewActiveStepChanged(sContext);
    },

    handleMnmRelationshipPaginationChanged: function (sViewContext, sPropertyId, sTableId, oPaginationData, sSourceKlassInstanceId) {
      _handleMnmRelationshipPaginationChanged(sViewContext, sPropertyId, sTableId, oPaginationData, sSourceKlassInstanceId);
    },

    handleContentComparisonMnmRemoveRelationshipButtonClicked: function (sPropertyId, sTableId, oPropertyToSave) {
      _handleContentComparisonMnmRemoveRelationshipButtonClicked(sPropertyId, sTableId, oPropertyToSave);
      _triggerChange();
    },

    handleContentComparisonFullScreenButtonClicked: function () {
      _handleContentComparisonFullScreenButtonClicked();
    },

    getViewMergeSourcesButtonView: () => {
      _getViewSourcesButtonClicked();
    },

    handleGoldenRecordComparisonLanguageChanged: (sLanguageForComparison) => {
      _handleGoldenRecordComparisonLanguageChanged(sLanguageForComparison);
    }
  }
})();

MicroEvent.mixin(GoldenRecordStore);

export default GoldenRecordStore;
