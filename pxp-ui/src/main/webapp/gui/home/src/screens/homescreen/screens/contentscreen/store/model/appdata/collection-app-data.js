/**
 * Created by CS56 on 9/30/2016.
 */
import EntityAppData from './entity-app-data';

var CollectionAppData = function () {
  this.inheritsFrom(EntityAppData);

  this.className = "CollectionAppData";

  var aDynamicCollections = [];
  var aStaticCollections = [];

  this.getDynamicCollectionList = function () {
    return aDynamicCollections;
  };

  this.setDynamicCollectionList = function (_aDynamicCollections) {
    aDynamicCollections = _aDynamicCollections;
  };

  this.getStaticCollectionList = function () {
    return aStaticCollections;
  };

  this.setStaticCollectionList = function (_aStaticCollections) {
    aStaticCollections = _aStaticCollections;
  };

};

export default new CollectionAppData();