
var GoldenRecordProps = (function () {

  var Props = function () {
    return {
      bShowGoldenRecordBuckets: false,
      oGoldenRecordBucketsDataMap: {},
      oGoldenRecordBucketsConfigDetails: {},
      oGoldenRecordBucketsPaginationData: {
        from: 0,
        pageSize: 20,
        totalBuckets: null,
        currentPageCount: null
      },
      oGoldenRecordRule: {},
      sCurrentBucketId: "",
      aSelectedKlassIds: [],
      sGoldenRecordId: "",
      activeGoldenRecordInstance: {},
      oPropertyRecommendations: {},
      oGoldenRecordComparisonConfigDetails: {},
      aBucketIdsContainingGoldenRecord: [],
      oGoldenRecordComparisionSkeleton: {},
      aGoldenRecordLanguageSelectionGridViewData: [],
      aReferencedLanguages: [],
      aSelectedLanguageList: [],
      sDefaultLanguage: '',
      stepperViewActiveStep: 0,
      stepperViewStepsList: [],
      sMNMSelectedLanguageId: "",
      aMNMMergedLanguageId: [],
      oMNMActivePropertyDetailedData: {},
      contentComparisonMatchAndMergePropertiesDetailedData: {},
      contentComparisonMatchAndMergeData : {},
      dummyKlassInstance: {},
      klassInstanceList: [],
      activeBucketData: {},
      klassBasicInformationData: {
        klassIds: [],
        taxonomyIds: []
      },
      matchPropertiesData: {},
      relationshipPagination: {
        from: 0,
        size: 10
      },
      isGoldenRecordRemergeSourcesTabClicked: false,
      isFullScreenMode: false,
      isMatchAndMergeViewOpen: false,
      isGoldenRecordViewSourcesDialogOpen: false,
      goldenRecordLanguageInstances: []
    }
  };

  var Properties = new Props();

  return {

    getShowGoldenRecordBuckets: function () {
      return Properties.bShowGoldenRecordBuckets;
    },

    setShowGoldenRecordBuckets: function (_bShowGoldenRecordBuckets) {
      Properties.bShowGoldenRecordBuckets = _bShowGoldenRecordBuckets;
    },

    getGoldenRecordBucketsDataMap: function () {
      return Properties.oGoldenRecordBucketsDataMap;
    },

    setGoldenRecordBucketsDataMap: function (_oGoldenRecordBucketsDataMap) {
      Properties.oGoldenRecordBucketsDataMap = _oGoldenRecordBucketsDataMap;
    },

    getGoldenRecordBucketsConfigDetails: function () {
      return Properties.oGoldenRecordBucketsConfigDetails;
    },

    setGoldenRecordBucketsConfigDetails: function (_oGoldenRecordBucketsConfigDetails) {
      Properties.oGoldenRecordBucketsConfigDetails = _oGoldenRecordBucketsConfigDetails;
    },

    getGoldenRecordBucketsPaginationData: function () {
      return Properties.oGoldenRecordBucketsPaginationData;
    },

    getMatchAndMergeComparisonData: function () {
      return Properties.contentComparisonMatchAndMergeData;
    },

    setMatchAndMergeComparisonData: function (_oContentComparisonMatchAndMergeData) {
      Properties.contentComparisonMatchAndMergeData = _oContentComparisonMatchAndMergeData;
    },

    setGoldenRecordRule: function (_oGoldenRecordRule) {
      Properties.oGoldenRecordRule = _oGoldenRecordRule;
    },

    getGoldenRecordRule: function () {
      return Properties.oGoldenRecordRule;
    },

    setActiveBucketId: function (_sBucketId) {
      Properties.sCurrentBucketId = _sBucketId;
    },

    getActiveBucketId: function () {
      return Properties.sCurrentBucketId;
    },

    setSelectedKlassInstanceIds: function (_aSelectedKlassIds) {
      Properties.aSelectedKlassIds = _aSelectedKlassIds;
    },

    getSelectedKlassInstanceIds: function () {
      return Properties.aSelectedKlassIds;
    },

    setActiveGoldenRecordId: function (_sGoldenRecordId) {
      Properties.sGoldenRecordId = _sGoldenRecordId;
    },

    getActiveGoldenRecordId: function () {
      return Properties.sGoldenRecordId;
    },

    setPropertyRecommendations: function (_oPropertyRecommendations) {
      Properties.oPropertyRecommendations = _oPropertyRecommendations;
    },

    getPropertyRecommendations: function () {
      return Properties.oPropertyRecommendations;
    },

    setGoldenRecordComparisonConfigDetails: function (_oGoldenRecordComparisonConfigDetails) {
      Properties.oGoldenRecordComparisonConfigDetails = _oGoldenRecordComparisonConfigDetails;
    },

    getGoldenRecordComparisonConfigDetails: function () {
      return Properties.oGoldenRecordComparisonConfigDetails;
    },

    setBucketIdsContainingGoldenRecord: function (aBucketIdsContainingGoldenRecord) {
      Properties.aBucketIdsContainingGoldenRecord = aBucketIdsContainingGoldenRecord;
    },

    getBucketIdsContainingGoldenRecord: function () {
      return  Properties.aBucketIdsContainingGoldenRecord;
    },

    getGoldenRecordComparisionSkeleton () {
      return Properties.oGoldenRecordComparisionSkeleton;
    },

    setGoldenRecordComparisionSkeleton (oSkeleton) {
      Properties.oGoldenRecordComparisionSkeleton = oSkeleton;
    },

    getGoldenRecordLanguageSelectionGridViewData () {
      return Properties.aGoldenRecordLanguageSelectionGridViewData;
    },

    setGoldenRecordLanguageSelectionGridViewData (_aGoldenRecordLanguageSelectionGridViewData) {
      Properties.aGoldenRecordLanguageSelectionGridViewData = _aGoldenRecordLanguageSelectionGridViewData;
    },

    resetGoldenRecordLanguageSelectionGridViewData () {
      Properties.aGoldenRecordLanguageSelectionGridViewData = [];
    },

    getReferencedLanguages () {
      return Properties.aReferencedLanguages;
    },

    setReferencedLanguages (_aReferencedLanguages) {
      Properties.aReferencedLanguages = _aReferencedLanguages;
    },

    getSelectedLanguageList () {
      return Properties.aSelectedLanguageList;
    },

    setSelectedLanguageList (_aSelectedLanguageList) {
      Properties.aSelectedLanguageList = _aSelectedLanguageList;
    },

    getDefaultLanguage() {
      return Properties.sDefaultLanguage;
    },

    setDefaultLanguage(_sDefaultLanguage) {
      Properties.sDefaultLanguage = _sDefaultLanguage;
    },

    getStepperViewActiveStep () {
      return Properties.stepperViewActiveStep;
    },

    setStepperViewActiveStep (_sActiveStep) {
      Properties.stepperViewActiveStep = _sActiveStep;
    },

    getStepperViewSteps () {
      return Properties.stepperViewStepsList;
    },

    setStepperViewSteps (_aStepsList) {
      Properties.stepperViewStepsList = _aStepsList;
    },

    getMNMActivePropertyDetailedData (){
      return Properties.oMNMActivePropertyDetailedData;
    },

    setMNMActivePropertyDetailedData(oData) {
      Properties.oMNMActivePropertyDetailedData = oData
    },

    getDummyKlassInstance () {
      return Properties.dummyKlassInstance;
    },

    setDummyKlassInstance (_oDummyKlassInstance) {
      Properties.dummyKlassInstance = _oDummyKlassInstance;
    },

    getMNMSelectedLanguageId () {
      return Properties.sMNMSelectedLanguageId;
    },

    setMNMSelectedLanguageId (_sMNMSelectedLanguageId) {
      Properties.sMNMSelectedLanguageId = _sMNMSelectedLanguageId;
    },

    getMNMMergedLanguageList () {
      return Properties.aMNMMergedLanguageId;
    },

    setMNMMergedLanguageList (_aMNMMergedLanguageId) {
      Properties.aMNMMergedLanguageId = _aMNMMergedLanguageId;
    },

    getKlassInstanceList () {
      return Properties.klassInstanceList;
    },

    setKlassInstanceList (_aklassInstanceList) {
      Properties.klassInstanceList = _aklassInstanceList;
    },

    getActiveBucketData () {
      return Properties.activeBucketData;
    },

    setActiveBucketData (_oActiveBucketData) {
      Properties.activeBucketData = _oActiveBucketData;
    },

    getSelectedKlassBasicInformationData () {
      return Properties.klassBasicInformationData;
    },

    setSelectedKlassBasicInformationData (_oKlassBasicInformationData) {
      Properties.klassBasicInformationData = _oKlassBasicInformationData;
    },

    getGoldenRecordMatchPropertiesData () {
      return Properties.matchPropertiesData;
    },

    setGoldenRecordMatchPropertiesData (_oMatchPropertiesData) {
      Properties.matchPropertiesData = _oMatchPropertiesData;
    },

    getRelationshipPaginationData () {
       return Properties.relationshipPagination;
    },

    setRelationshipPaginationData (_oPaginationData){
       Properties.relationshipPagination = _oPaginationData;
    },

    getActiveGoldenRecordInstance () {
      return  Properties.activeGoldenRecordInstance;
    },

    setActiveGoldenRecordInstance (_oInstance) {
      Properties.activeGoldenRecordInstance = _oInstance;
    },

    getIsGoldenRecordRemergeSourcesTabClicked () {
      return  Properties.isGoldenRecordRemergeSourcesTabClicked;
    },

    setIsGoldenRecordRemergeSourcesTabClicked (_bIsGoldenRecordRemergeSourcesTabClicked) {
      Properties.isGoldenRecordRemergeSourcesTabClicked = _bIsGoldenRecordRemergeSourcesTabClicked;
    },

    getIsFullScreenMode () {
      return  Properties.isFullScreenMode;
    },

    setIsFullScreenMode (_bIsFullScreenMode) {
      Properties.isFullScreenMode = _bIsFullScreenMode;
    },

    getIsMatchAndMergeViewOpen () {
      return  Properties.isMatchAndMergeViewOpen;
    },

    setIsMatchAndMergeViewOpen (_bIsMatchAndMergeViewOpen) {
      Properties.isMatchAndMergeViewOpen = _bIsMatchAndMergeViewOpen;
    },

    getIsGoldenRecordViewSourcesDialogOpen () {
      return  Properties.isGoldenRecordViewSourcesDialogOpen;
    },

    setIsGoldenRecordViewSourcesDialogOpen (_bIsGoldenRecordViewSourcesDialogOpen) {
      Properties.isGoldenRecordViewSourcesDialogOpen = _bIsGoldenRecordViewSourcesDialogOpen;
    },

    getGoldenRecordLanguageInstances () {
      return Properties.goldenRecordLanguageInstances;
    },

    setGoldenRecordLanguageInstances (_aGoldenRecordLanguageInstances) {
      Properties.goldenRecordLanguageInstances = _aGoldenRecordLanguageInstances;
    },

    resetMNMData () {
      Properties.aMNMMergedLanguageId = [];
      Properties.dummyKlassInstance = {};
      Properties.contentComparisonMatchAndMergeData = {};
      Properties.sMNMSelectedLanguageId = "";
      Properties.oMNMActivePropertyDetailedData = {};
    },

    resetGoldenRecordComparisonProps: function () {
      Properties.sCurrentBucketId = "";
      Properties.sGoldenRecordId = "";
      Properties.oGoldenRecordRule = {};
      Properties.aSelectedKlassIds = [];
      Properties.contentComparisonMatchAndMergeData = {};
      Properties.aMNMMergedLanguageId = [];
      Properties.dummyKlassInstance = {};
      Properties.sMNMSelectedLanguageId = "";
      Properties.oMNMActivePropertyDetailedData = {};
      Properties.aSelectedLanguageList = [];
      Properties.isGoldenRecordRemergeSourcesTabClicked = false;
      Properties.isFullScreenMode = false;
      Properties.sDefaultLanguage = '';
      Properties.isMatchAndMergeViewOpen = false;
    },

    reset: () => {
      Properties = new Props();
    },

    toJSON: function () {
      return {
      };
    }

  };

})();

export default GoldenRecordProps;
