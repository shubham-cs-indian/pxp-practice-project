/**
 *
 * @param sId
 * @param iHeight
 * @param iMinHeight
 * @param iMaxHeight
 * @param sName
 * @param sIcon
 * @param oProperties
 * @constructor
 */
var PaperModel = function (sId, iHeight, iMinHeight, iMaxHeight, sName, sIcon, oProperties) {

  this.id = sId;
  this.label = sName;
  this.icon = sIcon;
  this.height = iHeight;
  this.minHeight = iMinHeight;
  this.maxHeight = iMaxHeight;
  this.properties = oProperties;

};

export default PaperModel;