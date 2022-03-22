/**
 * Created by CS31 on 28-11-2016.
 */

/**
 * @param sId
 * @param bIsDroppable
 * @param bIsDraggable
 * @param aValidItems
 * @param sContext
 * @param oProperties
 * @constructor
 */

var DropViewModel = function (sId, sLabel, bIsDroppable, bIsDraggable, aValidItems, sContext, oProperties) {

  this.id = sId;
  this.label = sLabel;
  this.isDroppable = bIsDroppable;
  this.validItems = aValidItems;
  this.isDraggable = bIsDraggable;
  this.context = sContext;
  this.properties = oProperties;

};

export default DropViewModel;
