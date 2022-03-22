/**
 * Created by CS62 on 21-12-2016.
 */
import RequestMapping from './content-screen-request-mapping';

var TextAssetRequestMapping = function () {
  this.inheritsFrom(RequestMapping);
  this.className = "TextAssetRequestMapping";

  this.CreateEntity = 'runtime/textassetinstances?lang=<%=lang%>';
  this.GetEntityById = 'runtime/textassetinstances/<%=tab%>/<%=id%>?isLoadMore=<%=isLoadMore%>&getAll=<%=getAll%>';
  this.GetEntityWithTypeChanged = 'runtime/textassetinstances/typeswitch';
  this.GetEntityTree = 'runtime/textassetinstances/expand/<%=id%>';
  this.GetAllEntityInstancePropertyVersions = 'runtime/textassetinstances/propertyversions/<%=id%>';
  this.SaveEntity = 'runtime/textassetinstances/tabs?isRollback=<%=isRollback%>';
  this.SaveEntityForOverviewTab = 'runtime/textassetinstances/tabs/overviewtab';
  this.BranchOffEntity = 'runtime/textassetinstances/clonebranch';
  this.GetEntityForComparison = 'runtime/textassetinstances/comparison';
  this.RollbackEntity = 'runtime/textassetinstances?isRollback=<%=isRollback%>&versionClassId=<%=versionClassId%>';
  this.RemoveEntity = 'runtime/textassetinstances';
  this.ChildrenQuicklist = 'runtime/textassetinstance/quicklist';
  this.RelationshipQuickList = 'runtime/textassetinstance/relationship/quicklist';
  this.GetAssetList = 'runtime/textassetinstance/asset/quicklist';

  this.CreateEntityVariant = 'runtime/variants/textasset?lang=<%=lang%>';
  this.CreatePromoVersion = 'runtime/promoversion';
  this.GetAllEntityVariants = 'runtime/textassetinstances/getallentityvariants/<%=id%>';
  this.DeleteEntityVariant = "runtime/textassetvariantinstances";
  this.saveRelationship = "runtime/textassetinstance/relationshipinstances";

  this.GetRelationshipElements = 'runtime/textassetinstancesrelationships/<%=id%>';
  this.GetReferenceElements = 'runtime/referenceinstances/get/<%=id%>';

  //Bulk save
  this.BulkSaveEntity = 'runtime/textassetinstances/bulksave?isRollback=<%=isRollback%>';

  //Version URL's
  this.DeleteContentVersions = 'runtime/textassetinstances/versionsarchive/<%=id%>';
  this.GetVersionsForComparisons = 'runtime/textassetinstances/versions/comparison/<%=id%>';
  this.RestoreContentVersions = 'runtime/textassetinstances/versionsrestore';
  this.getArchiveTimelineData = 'runtime/archivedtextassetinstances/timelinetab/<%=id%>?isLoadMore=<%=isLoadMore%>&getAll=<%=getAll%>';

  this.GetPropertyTableViewData = "runtime/textassetinstance/properties/variant/tableview";
  this.RollbackContentVersion = "runtime/textassetinstance/versionrollback/<%=id%>";
  this.GetVariantTableViewData = "runtime/textassetinstance/variant/tableview";
  this.CreateLimitedObject = "runtime/textasset/variant/limitedobject?lang=<%=lang%>";
  this.BulkSaveVariantsTableData = "runtime/textassetinstance/variants/bulksave";

  //To restore Contents
  this.restoreContents = "runtime/textassetinstances/restore";
  this.CreateTranslatableArticleInstance = "runtime/createtranslatabletextassetinstances";
  this.DeleteArticleTranslation = "runtime/textasset/translations";
  this.GetEntityForContentLanguageComparison = "runtime/textassetinstance/language/comparison";
  this.SaveEntityInLanguageComparisonMode = "runtime/textassetinstances/languagecomparison";

};

export default new TextAssetRequestMapping();