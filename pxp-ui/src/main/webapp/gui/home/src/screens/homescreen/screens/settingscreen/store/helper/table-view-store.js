
import CS from '../../../../../../libraries/cs';
import MicroEvent from '../../../../../../libraries/microevent/MicroEvent.js';
import {oTableRenderTypeConstant} from '../../tack/table-render-type';
import ContextualTableViewProps from "../model/contextual-table-view-props";

var TableViewStore = (function () {

  let _prepareTableViewHeaderData = (aSkeleton, oData, sContext, bIsReadOnly, bHasDisabledField) => {
    let oProcessedProperties = {};
    let oRow = [];
    let bIsVisible = oData.isVisible;
    CS.forEach(aSkeleton, function (oSkeleton) {
      switch (oSkeleton.id) {
        case "label":
          oProcessedProperties = {
            renderType: oSkeleton.renderType,
            value: oData.label,
            width: oSkeleton.width,
            iconClass: "propertyCollection ",
            isCollapsed: oSkeleton.isCollapsed,
            id:"label",
            label:"Property Collection",
            labelClassName: oSkeleton.labelClassName
          };
          break;

        case "visible":
          oProcessedProperties = {
            renderType: oSkeleton.renderType,
            value: oData.isVisible,
            width: oSkeleton.width,
            iconClass: oSkeleton.iconClass,
            id: "visible",
            label: oSkeleton.label,
            labelClassName: oSkeleton.labelClassName,
            isPartiallySelected: false
          };
          break;

        case "canEdit":
          oProcessedProperties = {
            renderType: oSkeleton.renderType,
            value: (bIsReadOnly || !bIsVisible) ? false : oData.canEdit,
            width: oSkeleton.width,
            iconClass: oSkeleton.iconClass,
            id: "canEdit",
            label:oSkeleton.label,
            labelClassName: oSkeleton.labelClassName,
            isDisabled: bHasDisabledField || bIsReadOnly,
            isPartiallySelected: false
          };
          break;
      }
      oRow.push(oProcessedProperties);
    });

    let oTableViewPropsForPropertyCollection = ContextualTableViewProps.createTableViewPropsByContext(sContext);
    oTableViewPropsForPropertyCollection.setTableViewHeaderData(oRow);
  };

  let _getCanEditValue = (sContext, oDataFromServer, oPropertyCollectionAttributePermission, sRowId, oExtraData) => {
    let bIsReadOnly = oExtraData["isReadOnly"];
    if (bIsReadOnly) {
      return false;
    }
    let oTableViewPropsForPropertyCollection = ContextualTableViewProps.getTableViewPropsByContext(sContext);
    let aTableViewHeaderData = oTableViewPropsForPropertyCollection.getTableViewHeaderData();
    let oHeaderCell = CS.find(aTableViewHeaderData, {id: "canEdit"});
    let bValue = null;
    if (aTableViewHeaderData.isDirty && !oHeaderCell.isPartiallySelected) {
      bValue = oHeaderCell.value;
    }
    else {
      bValue = (sContext === "relationship") ? oDataFromServer.canEdit : oPropertyCollectionAttributePermission.canEdit;
    }
    oExtraData.updatePropertyInAnotherTable && oExtraData.updatePropertyInAnotherTable(bValue, sContext, sRowId, "canEdit");
    return bValue;
  };

  let _getIsVisibleValue = (sContext, oDataFromServer, oPropertyCollectionAttributePermission, sRowId, oExtraData) => {
    let oTableViewPropsForPropertyCollection = ContextualTableViewProps.getTableViewPropsByContext(sContext);
    let aTableViewHeaderData = oTableViewPropsForPropertyCollection.getTableViewHeaderData();
    let oHeaderCell = CS.find(aTableViewHeaderData, {id: "visible"});
    let bValue = null;
    if (aTableViewHeaderData.isDirty && !oHeaderCell.isPartiallySelected) {
      bValue = oHeaderCell.value;
    }
    else {
      bValue = (sContext === "relationship") ? oDataFromServer.isVisible : oPropertyCollectionAttributePermission.isVisible;
    }
    oExtraData.updatePropertyInAnotherTable && oExtraData.updatePropertyInAnotherTable(bValue, sContext, sRowId, "visible");
    return bValue;
  };

  let _prepareTableViewRowData = (oSkeleton, aData, sContext, oExtraData = {}) => {
    let oProcessedProperties = {};
    let aRowData = [];
    let bIsReadOnly = oExtraData["isReadOnly"];
    let oTableViewPropsForPropertyCollection = ContextualTableViewProps.getTableViewPropsByContext(sContext);

    CS.forEach(aData, function (oDataFromServer, key) {
      let sRowId = oDataFromServer.id && oDataFromServer.id || key;
      let oRow = [];
      let oPropertyCollectionAttributePermission = oExtraData.propertyPermission && oExtraData.propertyPermission[oDataFromServer.id];
      CS.forEach(oSkeleton, function (oData) {
        let bIsVisible = _getIsVisibleValue(sContext, oDataFromServer, oPropertyCollectionAttributePermission, sRowId, oExtraData);
        let bIsDisabled = bIsReadOnly ? bIsReadOnly : !bIsVisible;
        switch (oData.id) {
          case "label":
            oProcessedProperties = {
              renderType: oTableRenderTypeConstant.LABEL,
              value: oDataFromServer.label,
              width: oData.width,
              iconClass: (sContext === "relationship") ? null  : oPropertyCollectionAttributePermission.type,
              id: "label",
              labelClassName: "",
              code: oDataFromServer.code
            };
            break;

          case "visible":
            oProcessedProperties = {
              renderType: (sContext === "relationship") ? oTableRenderTypeConstant.BOOLEAN : oTableRenderTypeConstant.CHECKBOX,
              value: bIsVisible,
              width: oData.width,
              id: "visible",
              labelClassName:""
            };
            break;

          case "canEdit":
            CS.isNull(oDataFromServer.canEdit) ?
            oProcessedProperties = {
              renderType: null,
              width: oData.width,
              id: "canEdit"
            } :
            oProcessedProperties = {
              renderType: (sContext === "relationship") ? oTableRenderTypeConstant.BOOLEAN : oTableRenderTypeConstant.CHECKBOX,
              value: (bIsReadOnly || !bIsVisible) ? false : _getCanEditValue(sContext, oDataFromServer, oPropertyCollectionAttributePermission, sRowId, oExtraData),
              width: oData.width,
              id: "canEdit",
              labelClassName: "",
              isDisabled: bIsDisabled
            };
            break;

          case "canAdd":
            oProcessedProperties = {
              renderType: oTableRenderTypeConstant.BOOLEAN,
              value:  (bIsReadOnly || !bIsVisible) ? false : oDataFromServer.canAdd,
              width: oData.width,
              id: "canAdd",
              labelClassName: "",
              isDisabled: bIsDisabled
            };
            break;

          case "canRemove":
            oProcessedProperties = {
              renderType:  oTableRenderTypeConstant.BOOLEAN,
              value: (bIsReadOnly || !bIsVisible) ? false : oDataFromServer.canDelete,
              width: oData.width,
              id: "canRemove",
              labelClassName: "",
              isDisabled: bIsDisabled
            };
            break;
        }
        oRow.push(oProcessedProperties);
      });
      oRow.rowId = sRowId;
      aRowData.push(oRow);
    });

    oTableViewPropsForPropertyCollection.setTableViewRowData(aRowData);
  };

  /*****************
   * PUBLIC API's
   * **************/
  return {

    prepareTableViewHeaderData: function (aSkeleton, oData, sContext, bIsReadOnly, bHasDisabledField) {
      return _prepareTableViewHeaderData(aSkeleton, oData, sContext, bIsReadOnly, bHasDisabledField);
    },

    prepareTableViewRowData: function (oSkeleton, aData, sContext, oExtraData) {
      return _prepareTableViewRowData(oSkeleton, aData, sContext, oExtraData);
    }

  }
})();

MicroEvent.mixin(TableViewStore);

export default TableViewStore;
