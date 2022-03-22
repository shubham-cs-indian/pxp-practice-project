
/**
 * @param sId
 * @param sLabel
 * @param sDescription
 * @param sDefaultValue
 * @param sToolTip
 * @param sPlaceholder
 * @param bIsMultiselect
 * @param oGlobalPermission
 * @param aKlasses
 * @param aAvailableUsers
 * @param aAddedUsers
 * @param bIsStandard
 * @param aModules
 * @param oProperties
 * @constructor
 */

var RoleDetailModel = function (sId, sLabel, sDescription, sDefaultValue, sToolTip, sPlaceholder,
                                bIsMultiselect, oGlobalPermission, bIsSettingAllowed, aKlasses, aAvailableUsers, aAddedUsers, bIsStandard,
                                aModules, oProperties) {

  this.id = sId;
  this.label = sLabel;
  this.description = sDescription;
  this.defaultValue = sDefaultValue;
  this.tooltip = sToolTip;
  this.placeholder = sPlaceholder;
  this.properties = oProperties;
  this.isMultiselect = bIsMultiselect;
  this.klasses = aKlasses;
  this.availableUsers = aAvailableUsers;
  this.addedUsers = aAddedUsers;
  this.isStandard = bIsStandard;
  this.modules = aModules;
  this.isSettingAllowed = bIsSettingAllowed;
  this.globalPermission = oGlobalPermission;

};

export default RoleDetailModel;

