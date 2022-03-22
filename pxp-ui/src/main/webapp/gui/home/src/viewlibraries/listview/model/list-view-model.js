/**
 *
 * @param sId
 * @param sName
 * @param bIsChecked
 * @param bIsSelected
 * @param bIsEditable
 * @param sIcon
 * @param bCheckboxVisibility
 * @param bRightIconVisibility
 * @param sRightIconClass
 * @param oProperties
 * @constructor
 */
var ListViewModel = function (sId, sName, bIsChecked, bIsSelected, bIsEditable, sIcon, bCheckboxVisibility,
                              bRightIconVisibility, sRightIconClass, oProperties) {

  this.id = sId;
  this.label = sName;
  this.isChecked = bIsChecked;
  this.isSelected = bIsSelected;
  this.isEditable = bIsEditable;
  this.icon = sIcon;
  this.checkboxVisibility = bCheckboxVisibility;
  this.rightIconVisibility = bRightIconVisibility;
  this.rightIconClass = sRightIconClass;
  this.properties = oProperties;

};

export default ListViewModel;

