/******
 *
 * @param sId
 * @param sHeaderText
 * @param oView
 * @param bIsVisible
 * @param oStyles
 * @param oProperties
 * @constructor
 */
var ContentUserViewModel = function (sId, sHeaderText, oView, bIsVisible, oProperties) {
  this.id = sId;
  this.headerText = sHeaderText;
  this.view = oView;
  this.isVisible = bIsVisible;
  this.properties = oProperties;
};

export default ContentUserViewModel;

