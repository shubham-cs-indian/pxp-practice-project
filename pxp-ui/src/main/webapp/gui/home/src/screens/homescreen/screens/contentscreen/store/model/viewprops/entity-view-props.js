import CS from '../../../../../../../libraries/cs';

import IViewProps from './i-view-props';
import DefaultTreeProps from './default-tree-props';
import ZoomSettings from '../../../../../../../commonmodule/tack/zoom-toolbar-settings';

var EntityViewProps = function () {
  this.inheritsFrom(IViewProps);
  this.className = "EntityViewProps";

  /**
   * Private properties and methods
   */
  var oTreeProps = {};
  var aSelectedList = [];
  var aAllowedTypes = [];
  var aAllowedNatureTypes = [];
  var aAllowedNonNatureTypes = [];
  let oAllowedNatureTypesPaginationData = {};
  let oAllowedNonNatureTypesPaginationData = {};
  let oDefaultPaginationData = {from: 0, size: ZoomSettings.defaultPaginationLimit, searchColumn: "label", searchText: ""};
  var bSelectAllChildren = false;
  var oAllowedTaxonomiesById = {};

  var initTreeProp = function () {
    oTreeProps = new DefaultTreeProps();
  };

  this._init = function () {
    initTreeProp();
  };

  /**
   * Public methods
   */
  this.getTreeProps = function () {
    return oTreeProps.treeProps;
  };

  this.setSelectedList = function (_aSelectedList) {
    aSelectedList = _aSelectedList;
  };

  this.getAllowedTypes = function () {
    return aAllowedTypes;
  };

  this.setAllowedTypes = function (_aAllowedTypes) {
    aAllowedTypes = _aAllowedTypes;
  };

  this.getAllowedNatureTypes = function () {
    return aAllowedNatureTypes;
  };

  this.setAllowedNatureTypes = function (_aAllowedTypes) {
    aAllowedNatureTypes = _aAllowedTypes;
  };

  this.getAllowedNonNatureTypes = function () {
    return aAllowedNonNatureTypes;
  };

  this.setAllowedNonNatureTypes = function (_aAllowedTypes) {
    aAllowedNonNatureTypes = _aAllowedTypes;
  };

  this.getAllowedNatureTypesPaginationData = function () {
    if (CS.isEmpty(oAllowedNatureTypesPaginationData)) {
      oAllowedNatureTypesPaginationData = CS.cloneDeep(oDefaultPaginationData);
    }
    return oAllowedNatureTypesPaginationData;
  };

  this.resetAllowedNatureTypesPaginationData = function () {
    oAllowedNatureTypesPaginationData = CS.cloneDeep(oDefaultPaginationData);
  };

  this.getAllowedNonNatureTypesPaginationData = function () {
    if (CS.isEmpty(oAllowedNonNatureTypesPaginationData)) {
      oAllowedNonNatureTypesPaginationData = CS.cloneDeep(oDefaultPaginationData);
    }
    return oAllowedNonNatureTypesPaginationData;
  };

  this.resetAllowedNonNatureTypesPaginationData = function () {
    oAllowedNonNatureTypesPaginationData = CS.cloneDeep(oDefaultPaginationData);
  };

  this.getSelectAllChildren = function () {
    return bSelectAllChildren;
  };

  this.setSelectAllChildren = function (_bSelectAllChildren) {
    bSelectAllChildren = _bSelectAllChildren;
  };

  this.getAllowedTaxonomiesById = function () {
    return oAllowedTaxonomiesById;
  };

  this.setAllowedTaxonomiesById = function (_oAllowedTaxonomiesById) {
    oAllowedTaxonomiesById = _oAllowedTaxonomiesById;
  };
};

export default new EntityViewProps();
