
import RequestMapping from './content-screen-request-mapping';

var ArticleRequestMapping = function () {
  this.inheritsFrom(RequestMapping);
  this.className = "ArticleRequestMapping";

  this.CreateEntity = 'runtime/klassinstances?lang=<%=lang%>';
  this.GetEntityById = 'runtime/klassinstances/<%=tab%>/<%=id%>?isLoadMore=<%=isLoadMore%>&getAll=<%=getAll%>';
  this.GetEntityWithTypeChanged = 'runtime/klassinstances/typeswitch';
  this.SaveEntity = 'runtime/klassinstances/tabs?isRollback=<%=isRollback%>';
  this.SaveEntityForOverviewTab = 'runtime/klassinstances/tabs/overviewtab';
  this.ChildrenQuicklist = 'runtime/klassinstance/quicklist';
  this.RelationshipQuickList = 'runtime/relationship/quicklist/get';
  this.CreateEntityVariant = 'runtime/variants/article?lang=<%=lang%>';
  this.DeleteEntityVariant = 'runtime/articlevariantinstances';
  this.saveRelationship = "runtime/klassinstance/relationshipinstances";
  this.GetRelationshipElements = 'runtime/articleinstancesrelationships/<%=id%>';

  //Version URL's
  this.DeleteContentVersions = 'runtime/articleinstances/versionsarchive/<%=id%>';
  this.GetVersionsForComparisons = 'runtime/articleinstances/versions/comparison/<%=id%>';
  this.RestoreContentVersions = 'runtime/articleinstances/versionsrestore';
  this.getArchiveTimelineData = 'runtime/archivedklassinstances/timelinetab/<%=id%>?isLoadMore=<%=isLoadMore%>&getAll=<%=getAll%>';

  this.GetVariantTableViewData = "runtime/articleinstance/variant/tableview";

  this.GetPropertyTableViewData = "runtime/articleinstance/properties/variant/tableview";
  this.RollbackContentVersion = "runtime/articleinstance/versionrollback/<%=id%>";

  //To restore Contents
  this.restoreContents = "runtime/articleinstances/restore";

  this.GetDataLanguage = "runtime/getdatalanguage";
  this.CreateTranslatableArticleInstance = "runtime/createtranslatablearticleinstance";
  this.DeleteArticleTranslation = "runtime/article/translations";
  this.GetEntityForContentLanguageComparison = "runtime/articleinstance/language/comparison";
  this.SaveEntityInLanguageComparisonMode = "runtime/klassinstances/languagecomparison";
};

export default new ArticleRequestMapping();
