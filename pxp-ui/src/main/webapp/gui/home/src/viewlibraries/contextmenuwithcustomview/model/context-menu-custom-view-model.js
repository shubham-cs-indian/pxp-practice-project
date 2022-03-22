/***
 *
 * @param sId
 * @param sName
 * @param bIsActive
 * @param sIcon
 * @param oProperties
 * @constructor
 */

var ContextMenuModel = function (sId, sName, bIsActive, sIcon, oProperties) {

  this.id = sId;
  this.label = sName;
  this.isActive = bIsActive;
  this.icon = sIcon;
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