/**
 * Created by CS31 on 14-11-2016.
 */
import RequestMapping from './content-screen-request-mapping';

var CollectionRequestMapping = function () {
  this.inheritsFrom(RequestMapping);
  this.className = "CollectionRequestMapping";
  this.GetAllEntityInstancePropertyVersions = 'runtime/klassinstancescollection/propertyversions/<%=id%>';
  this.ChildrenQuicklist = 'runtime/klassinstancecollection/quicklist';
  this.LinkedQuickList = 'runtime/klassinstancecollection/quicklist';
  this.RelationshipQuickList = 'runtime/klassinstancecollection/relationship/quicklist';
  this.GetAssetList = 'runtime/klassinstancecollection/asset/quicklist';

  this.BranchOffContentOfCollection = 'runtime/klassinstancecollections/clonebranch';
  this.CreateCollectionVariant = 'runtime/klassinstancecollections/createVariant';
  this.GetAllCollectionVariants = 'runtime/klassinstancecollections/getallentityvariants/<%=id%>';
  this.DeleteCollectionVariant = "runtime/klassinstancecollections";

  this.CreateCollection = 'runtime/klassinstancecollections';
  this.SaveCollection = 'runtime/klassinstancecollections?isRollback=<%=isRollback%>';
  this.RollbackCollection = 'runtime/klassinstancecollections?isRollback=<%=isRollback%>&versionClassId=<%=versionClassId%>';
  this.DeleteCollection = 'runtime/klassinstancecollections';
  this.GetAllCollectionKlassess = 'config/klassesByCategory/<%=id%>';
  this.GetCollectionWithTypeChanged = 'runtime/klassinstancecollections/typeswitch';
  this.GetCollectionForComparison = 'runtime/collectionklassinstances/comparison';
  this.GetCollectionTree = 'runtime/klassinstancecollections/expand/<%=id%>';

  //PropertyInstances
  this.GetPropertyInstancesForCollection = 'runtime/klassinstancecollections/propertyinstances';

  //NEW COLLECTION REQUESTS :

  this.GetAllCollections = 'runtime/collections/';

  //Dynamic Collections -
  this.CreateDynamicCollection = 'runtime/dynamiccollections';
  this.GetDynamicCollection = 'runtime/bookmark/get';
  this.SaveDynamicCollection = 'runtime/dynamiccollections';
  this.DeleteDynamicCollection = 'runtime/dynamiccollections';

  //Static Collections -
  this.CreateStaticCollection = 'runtime/staticcollections';
  this.GetStaticCollection = 'runtime/collection/get';
  this.SaveStaticCollection = 'runtime/staticcollections';
  this.DeleteStaticCollection = 'runtime/staticcollections';
  this.GetStaticCollectionQuickList = 'runtime/staticcollectionsquicklist/<%=id%>';
  this.MoveStaticCollection = "runtime/movestaticcollections";
  this.MoveStaticCollectionHierarchyNode = "config/movestaticcollections";

  this.GetStaticCollectionsTree = 'runtime/staticcollectionstree/<%=id%>';
  this.GetStaticCollectionDetails = 'runtime/staticcollectionsdetails/<%=id%>';
  this.SaveStaticCollectionDetails = 'runtime/staticcollectionsdetails';
  this.GetStaticCollections = 'runtime/staticcollections';
  this.GetDynamicCollections = 'runtime/dynamiccollections';
};

export default new CollectionRequestMapping();