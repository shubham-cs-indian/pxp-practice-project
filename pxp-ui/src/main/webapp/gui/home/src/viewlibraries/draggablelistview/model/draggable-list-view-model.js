/**
 * @param sId
 * @param sName
 * @param bIsChecked
 * @param bIsSelected
 * @param bIsEditable
 * @param sIcon
 * @param bCheckboxVisibility
 * @param bRightIconVisibility
 * @param sRightIconClass
 * @param bIsDraggable
 * @param sContext
 * @param sType
 * @param oProperties
 * @constructor
 */
var DraggableListViewModel = function (sId, sName, bIsChecked, bIsSelected, bIsEditable, sIcon, bCheckboxVisibility,
                                       bRightIconVisibility, sRightIconClass, bIsDraggable, sContext, sType, oProperties) {
  this.id = sId;
  this.label = sName;
  this.isChecked = bIsChecked;
  this.isSelected = bIsSelected;
  this.isEditable = bIsEditable;
  this.icon = sIcon;
  this.checkboxVisibility = bCheckboxVisibility;
  this.rightIconVisibility = bRightIconVisibility;
  this.rightIconClass = sRightIconClass;
  this.isDraggable = bIsDraggable;
  this.context = sContext;
  this.type = sType;
  this.properties = oProperties;
};

export default DraggableListViewModel;

