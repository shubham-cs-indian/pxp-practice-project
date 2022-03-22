/**
 * @param sId
 * @param bIsDroppable
 * @param aValidItems
 * @param oProperties
 * @constructor
 */
var DropViewModel = function (sId, bIsDroppable, aValidItems, oProperties) {

  this.id = sId;
  this.isDroppable = bIsDroppable;
  this.validItems = aValidItems;
  this.properties = oProperties;


};

export default DropViewModel;
