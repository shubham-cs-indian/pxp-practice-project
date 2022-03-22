/**
 * Created by CS62 on 21-12-2016.
 */
import RequestMapping from './content-screen-request-mapping';

var SupplierRequestMapping = function () {
  this.inheritsFrom(RequestMapping);
  this.className = "SupplierRequestMapping";

  this.CreateEntity = 'runtime/supplierinstances?lang=<%=lang%>';
  this.GetEntityById = 'runtime/supplierinstances/<%=tab%>/<%=id%>?isLoadMore=<%=isLoadMore%>&getAll=<%=getAll%>';
  this.GetEntityWithTypeChanged = 'runtime/supplierinstances/typeswitch';
  this.GetEntityTree = 'runtime/supplierinstances/expand/<%=id%>';
  this.GetAllEntityInstancePropertyVersions = 'runtime/supplierinstances/propertyversions/<%=id%>';
  this.SaveEntity = 'runtime/supplierinstances/tabs?isRollback=<%=isRollback%>';
  this.SaveEntityForOverviewTab = 'runtime/supplierinstances/tabs/overviewtab';
  this.BranchOffEntity = 'runtime/supplierinstances/clonebranch';
  this.GetEntityForComparison = 'runtime/supplierinstances/comparison';
  this.RollbackEntity = 'runtime/supplierinstances?isRollback=<%=isRollback%>&versionClassId=<%=versionClassId%>';
  this.RemoveEntity = 'runtime/supplierinstances';
  this.ChildrenQuicklist = 'runtime/supplierinstance/quicklist';
  this.RelationshipQuickList = 'runtime/supplierinstance/relationship/quicklist';
  this.GetAssetList = 'runtime/supplierinstance/asset/quicklist';
  this.saveRelationship = "runtime/supplierinstance/relationshipinstances";

  this.CreateEntityVariant = 'runtime/variants/supplier?lang=<%=lang%>';
  this.CreatePromoVersion = 'runtime/promoversion';
  this.GetAllEntityVariants = 'runtime/supplierinstances/getallentityvariants/<%=id%>';
  this.DeleteEntityVariant = "runtime/suppliervariantinstances";

  this.GetRelationshipElements = 'runtime/supplierinstancesrelationships/<%=id%>';
  this.GetReferenceElements = 'runtime/referenceinstances/get/<%=id%>';

  //Bulk save
  this.BulkSaveEntity = 'runtime/supplierinstances/bulksave?isRollback=<%=isRollback%>';

  //Version URL's
  this.DeleteContentVersions = 'runtime/supplierinstances/versionsarchive/<%=id%>';
  this.GetVersionsForComparisons = 'runtime/supplierinstances/versions/comparison/<%=id%>';
  this.RestoreContentVersions = 'runtime/supplierinstances/versionsrestore';
  this.getArchiveTimelineData = 'runtime/archivedsupplierinstances/timelinetab/<%=id%>?isLoadMore=<%=isLoadMore%>&getAll=<%=getAll%>';

  this.GetPropertyTableViewData = "runtime/supplierinstance/properties/variant/tableview";
  this.RollbackContentVersion = "runtime/supplierinstance/versionrollback/<%=id%>";
  this.GetVariantTableViewData = "runtime/supplierinstance/variant/tableview";
  this.CreateLimitedObject = "runtime/supplier/variant/limitedobject?lang=<%=lang%>";
  this.BulkSaveVariantsTableData = "runtime/supplierinstance/variants/bulksave";

  //To restore Contents
  this.restoreContents = "runtime/supplierinstances/restore";

  this.CreateTranslatableArticleInstance = "runtime/createtranslatablesupplierinstances";
  this.DeleteArticleTranslation = "runtime/supplier/translations";
  this.GetEntityForContentLanguageComparison = "runtime/supplierinstance/language/comparison";
  this.SaveEntityInLanguageComparisonMode = "runtime/supplierinstances/languagecomparison";

};

export default new SupplierRequestMapping();