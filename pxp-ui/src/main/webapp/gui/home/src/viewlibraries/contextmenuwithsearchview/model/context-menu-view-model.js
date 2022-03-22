/***
 *
 * @param sId
 * @param sName
 * @param bIsActive
 * @param sIcon
 * @param oProperties
 * @constructor
 */

var ContextMenuModel = function (sId, sName, bIsActive, sIconKey, oProperties) {

  this.id = sId;
  this.label = sName;
  this.isActive = bIsActive;
  this.iconKey = sIconKey;
  this.properties = oProperties;

  /**
   * @property properties can include following info
   * customIconClassName
   * code
   * color
   * disabled
   */

};

export default ContextMenuModel;