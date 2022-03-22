/**
 * Created by DEV on 30-12-2015.
 */

/**
 *
 * @param sId         sectionId
 * @param sLabel      sectionLabel
 * @param iRow        Number of rows in section
 * @param iColumn     Number of columns in section
 * @param sIcon       Section icon
 * @param oElements   Section's Elements map with its model
 * @param oProperties Extra Properties
 * @constructor
 */
var ContentSectionViewModel = function (sId, sLabel, iRow, iColumn, sIcon, oElements, oProperties) {

  this.id = sId;
  this.label = sLabel;
  this.numberOfRows = iRow;
  this.numberOfColumns = iColumn;
  this.icon = sIcon;
  this.elements = oElements;
  this.properties = oProperties;

}
export default ContentSectionViewModel;