/**
 * Created by CS31 on 14-11-2016.
 */
import RequestMapping from './content-screen-request-mapping';

var AssetRequestMapping = function () {
  this.inheritsFrom(RequestMapping);
  this.className = "AssetRequestMapping";

  this.CreateEntity = 'runtime/assetinstances?lang=<%=lang%>';
  this.GetEntityById = 'runtime/assetinstances/<%=tab%>/<%=id%>?isLoadMore=<%=isLoadMore%>&getAll=<%=getAll%>';
  this.GetEntityWithTypeChanged = 'runtime/assetinstances/typeswitch';
  this.GetEntityTree = 'runtime/assetinstances/expand/<%=id%>';
  this.GetAllEntityInstancePropertyVersions = 'runtime/assetinstances/propertyversions/<%=id%>';
  this.SaveEntity = 'runtime/assetinstances/tabs?isRollback=<%=isRollback%>';
  this.SaveEntityForOverviewTab = 'runtime/assetinstances/tabs/overviewtab';
  this.BranchOffEntity = 'runtime/klassinstanceassets/clonebranch';
  this.GetEntityForComparison = 'runtime/assetinstances/comparison';
  this.RollbackEntity = 'runtime/assetinstances?isRollback=<%=isRollback%>&versionClassId=<%=versionClassId%>';
  this.RemoveEntity = 'runtime/assetinstances';
  this.ChildrenQuicklist = 'runtime/assetinstance/quicklist';
  this.RelationshipQuickList = 'runtime/assetinstance/relationship/quicklist';
  this.saveRelationship = "runtime/assetinstance/relationshipinstances";

  this.CreateEntityVariant = 'runtime/variants/asset?lang=<%=lang%>';
  this.CreatePromoVersion = 'runtime/promoversion';
  this.GetAllEntityVariants = 'runtime/klassinstanceassets/getallentityvariants/<%=id%>';
  this.DeleteEntityVariant = "runtime/assetvariantinstances";

  this.GetAllassetinstancestructureVersions = 'runtime/assetinstances/structureversions/<%=id%>';

  //PropertyInstances
  this.GetPropertyInstances = 'runtime/assetinstances/propertyinstances';

  //this.GetAvailableContents = 'runtime/relatedassetinstances';//'klass/getcontents/<%=id%>'
  this.GetRelationshipElements = 'runtime/assetinstancesrelationships/<%=id%>';
  this.GetReferenceElements = 'runtime/referenceinstances/get/<%=id%>';

  //Variants
  this.DeleteVariants = 'runtime/assetvariantinstances';

  //Version URL's
  this.DeleteContentVersions = 'runtime/assetinstances/versionsarchive/<%=id%>';
  this.GetVersionsForComparisons = 'runtime/assetinstances/versions/comparison/<%=id%>';
  this.BulkSaveEntity = 'runtime/assetinstances/bulksave?isRollback=<%=isRollback%>';
  this.RestoreContentVersions = 'runtime/assetinstances/versionsrestore';
  this.getArchiveTimelineData = 'runtime/archivedassetinstances/timelinetab/<%=id%>?isLoadMore=<%=isLoadMore%>&getAll=<%=getAll%>';

  this.GetVariantTableViewData = "runtime/assetinstance/variant/tableview";
  this.GetVariantConfigData = "runtime/assetvariantinstances/getconfigdata/<%=id%>";

  this.GetPropertyTableViewData = "runtime/assetinstance/properties/variant/tableview";
  this.RollbackContentVersion = "runtime/assetinstance/versionrollback/<%=id%>";
  this.CreateLimitedObject = "runtime/asset/variant/limitedobject?lang=<%=lang%>";
  this.BulkSaveVariantsTableData = "runtime/assetinstance/variants/bulksave";

  //To restore Contents
  this.restoreContents = "runtime/assetinstances/restore";

  this.CreateTranslatableArticleInstance = "runtime/createtranslatableassetinstances";
  this.DeleteArticleTranslation = "runtime/asset/translations";
  this.GetEntityForContentLanguageComparison = "runtime/assetinstance/language/comparison";
  this.SaveEntityInLanguageComparisonMode = "runtime/assetinstances/languagecomparison";

};

export default new AssetRequestMapping();
