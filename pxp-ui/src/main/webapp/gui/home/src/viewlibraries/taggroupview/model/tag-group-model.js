/**
 *
 * @param sId
 * @param sTagId //To store Tag Group Id
 * @param sName
 * @param iDepth
 * @param iRelevance
 * @param aChildrenModels
 * @param oProperties
 * @constructor
 */

var TagGroupModel = function (sId, sTagId, sName, oEntityTag, iDepth, iRelevance, oProperties, sIconKey) {

  this.id =  sId;
  this.tagId =  sTagId;
  this.label =  sName;
  this.entityTag = oEntityTag;
  this.depth =  iDepth;
  this.relevance =  iRelevance;
  this.properties =  oProperties;
  this.iconKey =  sIconKey

};

export default TagGroupModel;