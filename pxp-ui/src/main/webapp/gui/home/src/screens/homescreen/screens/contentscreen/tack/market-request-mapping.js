/**
 * Created by CS31 on 14-11-2016.
 */
import RequestMapping from './content-screen-request-mapping';

var MarketRequestMapping = function () {
  this.inheritsFrom(RequestMapping);
  this.className = "MarketRequestMapping";

  this.CreateEntity = 'runtime/marketinstances?lang=<%=lang%>';
  this.GetEntityById = 'runtime/marketinstances/<%=tab%>/<%=id%>?isLoadMore=<%=isLoadMore%>&getAll=<%=getAll%>';
  this.GetEntityWithTypeChanged = 'runtime/marketinstances/typeswitch';
  this.GetEntityTree = 'runtime/marketinstances/expand/<%=id%>';
  this.GetAllEntityInstancePropertyVersions = 'runtime/marketinstances/propertyversions/<%=id%>';
  this.SaveEntity = 'runtime/marketinstances/tabs?isRollback=<%=isRollback%>';
  this.SaveEntityForOverviewTab = 'runtime/marketinstances/tabs/overviewtab';
  this.BranchOffEntity = 'runtime/marketinstances/clonebranch';
  this.GetEntityForComparison = 'runtime/marketinstances/comparison';
  this.RollbackEntity = 'runtime/marketinstances?isRollback=<%=isRollback%>&versionClassId=<%=versionClassId%>';
  this.RemoveEntity = 'runtime/marketinstances';
  this.ChildrenQuicklist = 'runtime/marketinstance/quicklist';
  this.RelationshipQuickList = 'runtime/marketinstance/relationship/quicklist';
  this.GetAssetList = 'runtime/marketinstance/asset/quicklist';
  this.saveRelationship = "runtime/marketinstance/relationshipinstances";

  this.CreateEntityVariant = 'runtime/variants/market?lang=<%=lang%>';
  this.CreatePromoVersion = 'runtime/promoversion';
  this.GetAllEntityVariants = 'runtime/marketinstances/getallentityvariants/<%=id%>';
  this.DeleteEntityVariant = "runtime/marketvariantinstances";

  this.GetAllKlassInstanceStructureVersions = 'runtime/marketinstances/structureversions/<%=id%>';

  //PropertyInstances
  this.GetPropertyInstances = 'runtime/marketinstances/propertyinstances';

  //this.GetAvailableContents = 'runtime/relatedmarketinstances';//'klass/getcontents/<%=id%>'
  this.GetRelationshipElements = 'runtime/marketinstancesrelationships/<%=id%>';
  this.GetReferenceElements = 'runtime/referenceinstances/get/<%=id%>';

  //Variants
  this.DeleteVariants = 'runtime/marketvariantinstances';

  //Bulk save
  this.BulkSaveEntity = 'runtime/marketinstances/bulksave?isRollback=<%=isRollback%>';

  //Version URL's
  this.DeleteContentVersions = 'runtime/marketinstances/versionsarchive/<%=id%>';
  this.GetVersionsForComparisons = 'runtime/marketinstances/versions/comparison/<%=id%>';
  this.RestoreContentVersions = 'runtime/marketinstances/versionsrestore';
  this.getArchiveTimelineData = 'runtime/archivedmarketinstances/timelinetab/<%=id%>?isLoadMore=<%=isLoadMore%>&getAll=<%=getAll%>';

  this.GetPropertyTableViewData = "runtime/marketinstance/properties/variant/tableview";
  this.RollbackContentVersion = "runtime/marketinstance/versionrollback/<%=id%>";
  this.GetVariantTableViewData = "runtime/marketinstance/variant/tableview";
  this.CreateLimitedObject = "runtime/market/variant/limitedobject?lang=<%=lang%>";
  this.BulkSaveVariantsTableData = "runtime/marketinstance/variants/bulksave";

  //To restore Contents
  this.restoreContents = "runtime/marketinstances/restore";

  this.CreateTranslatableArticleInstance = "runtime/createtranslatablemarketinstances";
  this.DeleteArticleTranslation = "runtime/market/translations";
  this.GetEntityForContentLanguageComparison = "runtime/marketinstance/language/comparison";
  this.SaveEntityInLanguageComparisonMode = "runtime/marketinstances/languagecomparison";

};

export default new MarketRequestMapping();
