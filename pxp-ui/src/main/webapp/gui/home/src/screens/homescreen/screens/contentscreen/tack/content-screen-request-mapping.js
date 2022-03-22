
var RequestMapping = function () {
  this.className = "RequestMapping";

  this.GetKlassesListByBaseType = 'config/klasseslistbybasetype';
  this.GetConfigData = 'config/configdata';
  this.GetAttributes = 'config/attributes?mode=<%=mode%>';
  this.GetAttribute = 'config/attributes/<%=id%>';
  this.GetBasicAttributes = 'config/attributelist?mode=all';
  this.RuntimeTransfer = 'runtime/manualtransfer';
  this.GetEndpointsAccordingToUser = 'config/user/offboardingendpoint/<%=userid%>';
  this.RuntimeExport = 'runtime/startexport';

  this.GetAllKlassess = 'config/klassesForModule/<%=id%>';
  this.GetAllArticleKlassess = 'config/klassesByCategory/<%=id%>';
  this.GetMasterKlass = 'config/masterklasses/<%=id%>';

  this.GetAllUsers = 'config/users';
  this.GetUser = 'config/user/<%=id%>';
  this.GetTaxonomies = 'config/articletaxonomylist';

  this.AllowedTypes = 'runtime/allowedtypes';
  this.AllowedTypesByTypes = 'runtime/allowedtypesbytypes';
  this.AllowedTypesTaxonomySearch = 'runtime/allowedTypesTaxonomySearch';

  //URL for tags
  this.GetAllTags = 'config/tags/<%=id%>?mode=<%=mode%>';

  //URL for roles
  this.GetAllRoles = 'config/roles';

  //URL for Image
  this.GetAssetImage = 'asset/<%=type%>/<%=id%>';
  this.GetAssetStatus = 'asset/upload/status/<%=type%>/<%=id%>';
  this.UploadImage = 'asset/upload/';
  this.AssetBulkUpload = 'assets/upload?mode=<%=mode%>&isUploadedFromInstance=<%=isUploadedFromInstance%>&klassId=<%=klassId%>';
  this.AssetsForegroundUpload = 'assets/uploadinforeground?mode=<%=mode%>&klassId=<%=klassId%>';
  this.OnboardingFileUpload = 'runtime/onboardingUploadFile/';
  this.SaveEndpoint = 'config/endpoints';
  this.SaveMappings = 'config/mappings';
  this.OnboardingStartImport = 'runtime/startimport/<%=id%>';
  this.TagValuesFromColumn = 'runtime/gettagvaluesfromcolumn';
  this.ExportToMDM = 'runtime/exporttomdm';
  this.ExportToOffboardStaging = 'runtime/exporfrommdm';
  this.ExportToFile = 'runtime/startexport';
  this.ExportBaseEntities = 'runtime/export/baseentity';
  this.TransferToSupplierStaging = 'runtime/exporfromcentral';
  this.ExportToCentral = 'runtime/exporttocentral';
  this.ExportToExcel = 'runtime/exporttoexcel';
  this.DeleteNFSFiles = 'nfsfiles';

  //Klass List
  this.GetKlassesByIds = 'config/klassesbyids';

  // Relationship
  this.GetRelationshipElements = 'runtime/klassinstancesrelationships/<%=id%>';
  this.GetReferenceElements = 'runtime/referenceinstances/get/<%=id%>';
  this.RelationshipQuickList = 'runtime/relationship/quicklist/get';
  this.CreateAsset = 'runtime/assetinstances';
  this.BulkCreateAsset = 'runtime/bulkcreateassetinstances';
  this.BulkCreateAttachment = 'runtime/bulkcreateattachmentinstances';
  this.GetAssetList = 'runtime/assetinstance/asset/quicklist';

  this.ArticlePropertySearch = 'runtime/article/propertysearch';
  this.GetAllDashboardEntities = 'runtime/dashboardtile/info';
  this.GetAssetsAboutToExpire = 'runtime/dashboardtile/assetsabouttoexpire/info';
  this.GetDashboardDuplicateAssetsInfo = 'runtime/dashboardtile/duplicateasset/info';
  this.GetAllEntities = 'runtime/instancetree/<%=id%>';
  this.GetAllInstances = 'runtime/instancetree/get';
  this.GetRuleViolationInformation = 'runtime/dashboardtile/ruleviolationinfo';
  this.GetAllKlassTaxonomies = 'runtime/klasstaxonomytree/get';
  this.GetOrganizeScreenTree = 'runtime/organizescreentree/get';
  this.GetAllTaxonomiesForStaticCollection = 'runtime/collection/klasstaxonomytree';
  this.GetTaxonomyHierarchyForRelationship = 'runtime/relationship/quicklist/klasstaxonomytree';
  this.GetAllTaxonomiesByLeafIdsForVariantQuicklist = 'runtime/linkedinstance/quicklist/klasstaxonomytree';
  this.DeleteInstances = 'runtime/instances';
  this.GetFilterChildren = "runtime/filterchildren/get";
  this.GetFilterChildrenForRelationshipQuickList = "runtime/relationship/quicklist/filterchildren";
  this.GetFilterChildrenForCollection = "runtime/collection/filterchildren";
  this.GetFileInstance = "getuploadedfilesforendpointinfo";
  this.Deleteimportedfile = "deleteimportedfile";
  //Assets
  this.DeleteAssetInstances = 'runtime/assetinstances';

  //Tasks
  this.GetTask = 'runtime/taskinstances/<%=id%>';
  this.CreateTask = 'runtime/taskinstances';
  this.SaveTask = 'runtime/taskinstances';
  this.CompleteTask = 'runtime/taskinstancescomplete';
  this.CompleteTaskBulk = 'runtime/taskinstancescomplete/bulk';
  this.DeleteTasks = 'runtime/taskinstances';
  this.fetchAllLazyTaskDetailsList = 'runtime/taskinstances/getall/<%=roleId%>';

  //XRayPropertyGroups i.e. Property Collections -
  this.GetAllPropertyCollections = 'config/propertycollections/getall?isForXRay=<%=isForXRay%>';
  this.GetPropertyCollection = 'config/propertycollections/<%=id%>';

  //Bulk Download
  this.BulkDownload = 'assetinstances/getFileId';
  this.ShareDownload = 'runtime/assetinstance/createlink';

  //Asset Share DialogInfo
  this.BulkAssetSharedDialogInfo = 'runtime/assetinstances/sharedialoginfo';

  //Bulk Asset Download
  this.BulkAssetDownload = 'runtime/assetinstances/getTechnicalTypeId';

  this.BulkDownloadSrc = 'assetinstances/getFileById/<%=id%>';

  //GetAssetInstance
  this.GetAllAssets = 'runtime/klassinstance/assets';


  /*** JobInstances File Mapping Urls ***/
  this.GetJob = 'runtime/procesinstances/task';
  this.GetExecutionStatus = 'runtime/procesinstances/getRequestedProcessInstanceLog';

  //variants
  this.GetAllVariantEntitiesQuicklist = 'runtime/linkedinstance/quicklist';
  this.GetSortAndFilterDataForVariantQuicklist = 'runtime/linkedinstance/quicklist/sortandfilter';
  this.GetFilterChildrenForVariantQuicklist = 'runtime/linkedinstance/quicklist/filterchildren';

  this.GetSortAndFilterData = "runtime/sortandfilter/get";
  this.GetFilterAndSortForStaticCollection = "runtime/collection/sortandfilter";
  this.GetSortAndFilterDataForRelationshipQuicklist = "runtime/relationship/quicklist/sortandfilter";


  //clone context wizard
  this.GetPropertiesToClone = "runtime/getPropertiesToClone";
  this.GetPropertiesToCloneTarget = "runtime/getMarketInstancePropertiesToClone";
  this.GetPropertiesToCloneTextAsset = "runtime/getTextAssetPropertiesToClone";

  this.CreateLimitedObject = "runtime/article/variant/limitedobject?lang=<%=lang%>";
  this.GetContext = "config/variantcontexts/<%=id%>";
  this.BulkSaveVariantsTableData = "runtime/articleinstance/variants/bulksave";
  this.GetVariantConfigData = "runtime/variantinstances/getconfigdata/<%=id%>";

  this.GetEntityById = 'runtime/klassinstances/<%=tab%>/<%=id%>?isLoadMore=<%=isLoadMore%>&getAll=<%=getAll%>';
  this.GetInstanceComparisionData = 'runtime/klassinstancescomparison';

  this.BulkEdit = 'runtime/bulkapplyvalues';
  //Create Article Clone
  this.CreateSingleClone = "runtime/articleinstance/singleclone";
  this.CreateCloneForLinkedVariant = "runtime/articleinstance/cloneforlinkedvariant";
  this.CreateBulkClone = "runtime/articleinstance/bulkclone";
  this.CreateSingleCloneForTarget = "runtime/marketinstance/singleclone";
  this.CreateSingleCloneForTextAsset = "runtime/textassetinstances/singleclone";
  this.CreateBulkCloneForTarget = "runtime/marketinstance/bulkclone";
  this.CreateBulkCloneForTextAsset = "runtime/textassetinstance/bulkclone";

  //Content Grid
  this.GetContentGridData = "runtime/contentgrid";
  this.GetAllGridEditablePropertyList = "config/grideditpropertylists";
  this.GetGridEditableProperties = "config/grideditproperties";

  //KPI summary information
  this.GetKpiSummaryData = "runtime/getstatisticsforcontent";

  //To get possible conflicting sources
  this.GetPossibleConflictingSources = "runtime/getconflictsourcesinfo";

  //Golden Record Requests
  this.GetGoldenRecordBuckets = "runtime/goldenrecordbucketinstances/get";
  this.GetGoldenRecordBucketMatches = "runtime/getgoldenrecordmatchedinstances";
  this.GetSortAndFilterDataForGoldenRecord = "runtime/goldenrecord/sortandfilter";
  this.GetDataForGoldenRecordMergeLanugageSelection = "runtime/getsourceinfoforbucket";
  this.GetFilterChildrenForGoldenRecord = "runtime/goldenrecord/filterchildren";
  this.CreateGoldenRecord = "runtime/goldenrecord";
  this.AutoCreateGoldenRecord = "runtime/goldenrecord/auto";
  this.SaveGoldenRecord = "runtime/goldenrecord";

  this.GetDataForGoldenRecordPropertiesMerge = "runtime/goldenrecord/property/mergeview";
  this.GetDataForGoldenRecordRelationshipsMerge = "runtime/goldenrecord/relationship/mergeview";

  this.GetValuesFromSourcesForProperties = "runtime/getvaluefromsources";
  this.GetValuesFromSourcesForRelationship = "runtime/getrelationshipdatafromsources";
  this.GetValuesFromSourcesForBasicInfo = "runtime/gettypeinfoforsources";

  this.LoadRelationshipElementsBySourceId = "runtime/getrelationshipdatafromsources/<%=sourceId%>";

  //To restore Contents
  this.restoreContents = "runtime/articleinstances/restore";

  this.GetDuplicateAssets = "runtime/assetinstances/duplicateassettab/<%=id%>";
  this.GetAssetDownloadCount = "runtime/downloadcount";
  this.GetAssetSharedUrl = "runtime/assetinstance/getlink";
  this.DeleteAssetSharedUrl = "runtime/assetobject/shareurl";

  //Smart Document
  this.GetSmartDocumentPresetList = "config/smartdocumentpresets";
  this.GetSmartDocuemnt = "config/smartdocument/smartdocument";
  this.GenerateSmartDocument = "runtime/generatesmartdocument"

  this.ResolveRelationshipinheritanceconflict = "runtime/resolve/relationshipinheritanceconflict";
  //Get asset extensions
  this.GetAllAssetExtensions = "config/getExtensions";
  this.GenerateSmartDocument = "runtime/generatesmartdocument";
  this.GetAllSide2NatureKlassFromNatureRelationship = 'config/getAllSide2NatureKlassFromNatureRelationship/<%=id%>';
  this.GetSide2NatureKlassFromNatureRelationship = 'config/side2NatureKlassFromNatureRelationship/<%=id%>';

  //Get taxonomy hierarchy for selected taxonomy
  this.GetTaxonomyConflicts = "runtime/fetchtaxonomiesinfo";
  this.GetTaxonomyHierarchyForSelectedTaxonomy = "runtime/taxonomyhierarchy/multiclassification";
  this.GetTypesAndTaxonomiesOfContent = "runtime/typesandtaxonomiesofcontent";

  /** Selective Export in Runtime View **/
  this.SelectiveExport = "export/excel";
  this.RuntimeProductImport ="config/import";
  this.GetDefaultTypes = "config/defaulttypes/get";

  this.CheckContextDuplication = "runtime/checkduplicatelinkedvariant";
};

export default new RequestMapping();

