/**
 * Created by CS56 on 10/4/2016.
 */

import IViewProps from './i-view-props';

var DefaultTreeProps = function () {
  this.inheritsFrom(IViewProps);
  this.className = "DefaultTreeProps";

  var TREE_ROOT_ID = this.TREE_ROOT_ID;

  this.treeProps = {};
  this.treeProps[TREE_ROOT_ID] = {
    id: TREE_ROOT_ID,
    isChecked: false,
    isChildLoaded: true,
    isEditable: false,
    isExpanded: true,
    isLoading: false,
    isSelected: true
  };
};

export default DefaultTreeProps;