import CS from '../../../../../../libraries/cs';

import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import ContentUtils from './content-utils';
import InnerFilterProps from '../model/inner-filter-props';
import TableViewProps from '../model/table-view-props';
import ContextualFilterProps from '../model/contextual-filter-props';
import CommonUtils from "../../../../../../commonmodule/util/common-utils";
import NumberUtils from "../../../../../../commonmodule/util/number-util";

var FilterUtils = (function () {

  let _getFilterProps = (oFilterContext) => {
    if (!oFilterContext) {
      console.log(oFilterContext)
    }
    else if (oFilterContext.contextId) {
      let sIdForProps = ContentUtils.getIdForTableViewProps(oFilterContext.contextId);
      let sActiveKlassInstanceId = oFilterContext.klassInstanceId || ContentUtils.getActiveContent().id;
      return TableViewProps.getTableFilterViewPropsByContext(sIdForProps, sActiveKlassInstanceId);
    }
    else {
      return ContextualFilterProps.getFilterPropsByContext(oFilterContext.screenContext, oFilterContext.filterType);
    }
  };

  let _resetAllFilters = () => {
    ContextualFilterProps.reset();
  };

  var _setOldSearchText = function (oExtraData) {
    var oFilterProps = _getFilterProps(oExtraData);
    var sOldSearchText = oFilterProps.getSearchText();
    oFilterProps.setOldSearchText(sOldSearchText);
  };

  let _getDateRangeLabel = function (oChild) {
    let sFilterLabel = CommonUtils.getDateAttributeInTimeFormat(oChild.from);
    sFilterLabel += " - " + CommonUtils.getDateAttributeInTimeFormat(oChild.to);
    oChild.label = sFilterLabel;
  };

  let _getNumberAccordingToNumberFormat = function (oChild) {
    oChild.label = NumberUtils.getValueToShowAccordingToNumberFormat(oChild.from, {}, {}, oChild.hideSeparator)
        + " - " + NumberUtils.getValueToShowAccordingToNumberFormat(oChild.to, {}, {}, oChild.hideSeparator);
  };

  return {

    getDateRangeLabel: function (oChild) {
      _getDateRangeLabel(oChild);
    },

    getNumberAccordingToNumberFormat: function (oChild) {
      _getNumberAccordingToNumberFormat(oChild);
    },

    getCheckedKlassTreeNodes: function (aNodes, bNotFirst, filterContext) {
      var _this = this;
      var oFilterProps = _getFilterProps(filterContext);
      var oVisualProps = oFilterProps.getTaxonomyVisualProps();
      aNodes = aNodes || oFilterProps.getTaxonomyTree();
      var sParentTaxonomyId = oFilterProps.getSelectedOuterParentId() || "-1";

      var bAllChecked = true;
      var aCheckedNodes = [];
      var bAreAllChildrenChecked = true;

      CS.forEach(aNodes, function (oNode) {
        var oNodeVisualProps = oVisualProps[oNode.id] || {};
        var bIsChecked = oNodeVisualProps.isChecked;
        bAllChecked = bAllChecked && bIsChecked;
        var bChildrenExist = oNode.children ? oNode.children.length : null;

        var oChildInfo = _this.getCheckedKlassTreeNodes(oNode.children || [], null, filterContext);
        bAllChecked = bAllChecked && oChildInfo.allChecked;
        oChildInfo.allChecked || (aCheckedNodes = aCheckedNodes.concat(oChildInfo.checkedNodes));

        if(bChildrenExist){
          (oChildInfo.allChecked || oNode.id == sParentTaxonomyId) && aCheckedNodes.push(oNode);
          bAreAllChildrenChecked = bAreAllChildrenChecked && oChildInfo.allChecked;
        } else {
          (bIsChecked || oNode.id == sParentTaxonomyId) && aCheckedNodes.push(oNode);
          bAreAllChildrenChecked = false;
        }
      });

      if (bAreAllChildrenChecked) {
        bAllChecked = true;
      }

      return {
        allChecked: bAllChecked,
        checkedNodes: (bAllChecked && bNotFirst) ? [] : aCheckedNodes
      }
    },

    getIsAllKlassNodesChecked: function (aNodes) {
      var _this = this;
      var oFilterProps = _getFilterProps();
      aNodes = aNodes || oFilterProps.getTaxonomyTree();
      var bAllChecked = true;

      CS.forEach(aNodes, function (oNode) {
        var bIsChecked = oNode.isChecked;
        bAllChecked = bAllChecked && bIsChecked;

        var bChildrenChecked = _this.getIsAllKlassNodesChecked(oNode.children || []);
        bAllChecked = bAllChecked && bChildrenChecked;
      });

      return bAllChecked
    },

    getFilterProps: function (oFilterContext) {
      return _getFilterProps(oFilterContext);
    },

    getFilterInfo: function () {
      return InnerFilterProps.getFilterInfo();
    },

    getInnerFilterProps: function () {
      return InnerFilterProps;
    },

    setOldSearchText: function (oExtraData) {
      _setOldSearchText(oExtraData)
    },

    resetAllFilters: () => {
      _resetAllFilters();
    }

  }
})();

MicroEvent.mixin(FilterUtils);

export default FilterUtils;
