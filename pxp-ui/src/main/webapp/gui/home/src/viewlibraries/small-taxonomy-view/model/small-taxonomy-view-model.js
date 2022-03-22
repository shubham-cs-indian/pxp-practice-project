/***
 *
 * @param sId
 * @param aSelectedTaxonomyList
 * @param aContextMenuModelList
 * @param sHandlerText
 * @param sContext
 * @param oProperties
 * @constructor
 */
var SmallTaxonomyViewModel = function (sId, aSelectedTaxonomyList, aContextMenuModelList, sHandlerText, sContext, sRootTaxonomyId, oProperties) {

  this.id = sId;
  this.selectedTaxonomyList = aSelectedTaxonomyList;
  this.contextMenuModelList = aContextMenuModelList;
  this.handlerText = sHandlerText;
  this.context = sContext;
  this.rootTaxonomyId = sRootTaxonomyId;
  this.properties = oProperties;

};

export default SmallTaxonomyViewModel;