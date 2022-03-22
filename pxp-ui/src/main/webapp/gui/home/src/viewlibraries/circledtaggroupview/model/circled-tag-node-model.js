/**
 *
 * @param sId
 * @param sLabel
 * @param sIcon
 * @param sColor (in Hex)
 * @param sType (refer to tag-type-constants)
 * @param iRelevance
 * @param aChildrenModels
 * @param oProperties
 * @constructor
 */


var CircledTagNodeModel = function (sId, sLabel, sIcon, sColor, sType, iRelevance, aChildrenModels, oProperties) {
  this.id = sId;
  this.label = sLabel;
  this.iconKey = sIcon;
  this.color = sColor;
  this.type = sType;
  this.relevance = iRelevance;
  this.childrenModels = aChildrenModels;
  this.properties = oProperties

};

export default CircledTagNodeModel;