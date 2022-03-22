import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import TooltipView from './../tooltipview/tooltip-view';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import { view as TableCellView } from './table-cell-view';

const oEvents = {
  TABLE_EDIT_BUTTON_CLICKED: "table_edit_button_clicked",
  TABLE_DELETE_BUTTON_CLICKED: "table_delete_button_clicked",
  TABLE_OPEN_BUTTON_CLICKED: "table_open_button_clicked",
  TABLE_ROW_SELECTION_CHANGED: "table_row_selection_changed"
};

const oPropTypes = {
  tableContextId:ReactPropTypes.string,

  headerData: ReactPropTypes.arrayOf(
      ReactPropTypes.shape({
        id: ReactPropTypes.string,
        value: ReactPropTypes.string,
        width: ReactPropTypes.number
      })
  ),

  data: ReactPropTypes.arrayOf(
      ReactPropTypes.shape({
        id: ReactPropTypes.string,
        properties: ReactPropTypes.shape({
          id: ReactPropTypes.string,
          value: ReactPropTypes.string,
          rowSpan: ReactPropTypes.number,
          colSpan: ReactPropTypes.number
        }),
        isRowDisabled: ReactPropTypes.bool
      })
  ),

  settings: ReactPropTypes.shape({
    isCellEditable: ReactPropTypes.bool,
    showCheckbox: ReactPropTypes.bool,
    showEditButton: ReactPropTypes.bool,
    showDeleteButton: ReactPropTypes.bool,
    showOpenButton: ReactPropTypes.bool,
    context: ReactPropTypes.string,
    toolbar: ReactPropTypes.arrayOf(
        ReactPropTypes.shape({
          id: ReactPropTypes.string,
          tooltip: ReactPropTypes.string,
          className: ReactPropTypes.string
        })
    ),
    paginationData: ReactPropTypes.shape({
      from: ReactPropTypes.number,
      pageSize: ReactPropTypes.number,
      totalItems: ReactPropTypes.number
    }),
    showPagination: ReactPropTypes.bool
  }),

  masterEntity: ReactPropTypes.object,
  isRowSelectionMode: ReactPropTypes.bool,
  rowSelectionData: ReactPropTypes.object,
  context: ReactPropTypes.string,
  filterContext: ReactPropTypes.object
};
/**
 * @class TableBodyView - Use for Display Tbale Body view.
 * @memberOf Views
 * @property {string} [tableContextId] -  Pass Table context Id.
 * @property {array} [headerData] - Pass data of Header in Table.
 * @property {array} [data] - Pass data of table like id, mdmKlassInstanceId, originalInstanceId, Properties, versionId.
 * @property {custom} [settings] - Pass model contain showCheckbox, showEditButton, showDeleteButton, showOpenButton, toolbar, id, tooltip, className, sortData, sortBy, sortOrder.
 * @property {object} [masterEntity] - Pass properties like allowedTags, availability, code, children, klass etc.
 */

// @CS.SafeComponent
class TableBodyView extends React.Component{

  constructor(props) {
    super(props);
  }

  /*shouldComponentUpdate: function (oNextProps) {
    return !CS.isEqual(oNextProps, this.props);
  },
*/
  handleEditButtonClicked =(sDataId)=> {
    var oSettings = this.props.settings;
    var sTableContextId = this.props.tableContextId;

    EventBus.dispatch(oEvents.TABLE_EDIT_BUTTON_CLICKED, sDataId, oSettings.context, sTableContextId);
  }

  handleDeleteButtonClicked =(sDataId)=> {
    var oSettings = this.props.settings;
    var sTableContextId = this.props.tableContextId;
    EventBus.dispatch(oEvents.TABLE_DELETE_BUTTON_CLICKED, sDataId, oSettings.context, sTableContextId, this.props.filterContext);
  }

  handleOpenButtonClicked =(sDataId)=> {
    var oSettings = this.props.settings;
    EventBus.dispatch(oEvents.TABLE_OPEN_BUTTON_CLICKED, sDataId, oSettings.context);
  }

  handleRowSelectionChanged (sAttributeId, sRowId, iIndex) {
    let oData = this.props.data[iIndex] || {};
    let sValue = oData.properties.value && oData.properties.value.value || "";
    EventBus.dispatch(oEvents.TABLE_ROW_SELECTION_CHANGED, sAttributeId, sRowId, this.props.context, sValue);
  };

  getMasterEntity = (sEntityType, sEntityId) => {
    let _props = this.props;
    let oReferencedAttributes = _props.referencedAttributes;
    let oReferencedTags = _props.referencedTags;
    let oMasterEntity = {};

    if (sEntityType === "attribute") {
      oMasterEntity = oReferencedAttributes[sEntityId];
    } else if (sEntityType === "tag") {
      oMasterEntity = oReferencedTags[sEntityId];
    } else if(sEntityType === "custom"){
      oMasterEntity = oReferencedAttributes[sEntityId] ? oReferencedAttributes[sEntityId] : null;
    }

    return oMasterEntity;
  };

  getTableBody =()=> {
    var _this = this;
    var __props = _this.props;
    var aTableRows = [];
    var oSettings = __props.settings;
    let bIsTranspose = oSettings && oSettings.isTranspose;
    var bIsCellEditable = oSettings.isCellEditable;

    var bEditEnable, bDeleteEnable, bOpenEnable = false;
    if(!bIsTranspose) { // For a transposed table, don't push the settings buttons at the end (for language variants)
      bEditEnable = oSettings.showEditButton;
      bDeleteEnable = oSettings.showDeleteButton;
      bOpenEnable = oSettings.showOpenButton;
    }
    let oRowSelectionData = this.props.rowSelectionData;


    CS.forEach(__props.data, function (oData, iIndex) {
      if (oData.hideRow) {
        return;
      }

      var aTableCells = [];
      var iColIndex = 0;

      var sRowDataClassName = "";
      sRowDataClassName += !bIsTranspose && oData.highlightRow ? "highlightRow " : "";
      sRowDataClassName += !bIsTranspose && oData.isDirty ? "dirtyRowData" : "";

      let bIsRowSelectionMode = __props.isRowSelectionMode;
      if (bIsRowSelectionMode) {
        let sClassName = oRowSelectionData.attributeInstanceId == oData.id && oRowSelectionData.isDefault == true ? "selectIcon isSelected" : "selectIcon";
        aTableCells.push(
            <th className="settingsCell" key={iColIndex++}>
              <div className={sClassName} onClick={_this.handleRowSelectionChanged.bind(_this, oData.attributeId, oData.id, iIndex)}></div>
            </th>
        );
      }

      CS.forEach(__props.headerData, function (oHeaderData, iIndex) {
        if (oHeaderData.hideColumn) {
          return;
        }
        let sEntityType = bIsTranspose ? oData.type : oHeaderData.type;
        let sEntityId = bIsTranspose ? oData.id : oHeaderData.id;
        var oProperty = oData.properties[oHeaderData.id];

        // In case of attribute context, sEntityRype will be "custom" consider oHeaderData.entityId as it holds the current entityId
        var oMasterEntity = _this.getMasterEntity(sEntityType, oHeaderData.entityId || sEntityId);
        if(oProperty) {
          var iRowSpan = oProperty.rowSpan || 1;
          var iColSpan = oProperty.colSpan || 1;
          let bIsThisCellEditable = iIndex === 0 && bIsTranspose ? false : bIsCellEditable;
          /****
           * Following code is just kept for reference please remove it later
           * isColumnDisabled is transferred into isPropertyDisabled for respective properties for making fields
           * disabled.
           let bIsCellDisabled = bIsTranspose ? oProperty.isPropertyDisabled : oHeaderData.isColumnDisabled;
           ****/
          let bIsCellDisabled = oProperty.isPropertyDisabled || oHeaderData.isColumnDisabled;
          bIsCellDisabled = bIsCellDisabled || oMasterEntity.isDisabled || oData.isRowDisabled;
          var oCellDom = (<TableCellView
                            isCellEditable={bIsThisCellEditable}
                            isCellDisabled={bIsCellDisabled}
                            rowId={oData.id}
                            cellProperty={oProperty}
                            headerData={oHeaderData}
                            masterEntity={oMasterEntity}
                            context={oSettings.context}
                            tableContextId={__props.tableContextId}
                            entityType={sEntityType}
                            isTranspose={bIsTranspose}
                          />);

          let sClassName = iRowSpan > 1 ? "cellMerged" : "";
          aTableCells.push(
              <td key={iColIndex++}
                  className={sClassName}
                  rowSpan={iRowSpan}
                  colSpan={iColSpan}>
                {oCellDom}
              </td>
          );
        }
      });

      var oEditButtonView, oDeleteButtonView, oViewButtonView = null;

      if (bEditEnable) {
        oEditButtonView = (
            <TooltipView placement="bottom" label={getTranslation().EDIT}>
              <div className="relativeCell settingIcon editButton" onClick={_this.handleEditButtonClicked.bind(_this, oData.id)}>
                <div className="edit"></div>
              </div>
            </TooltipView>
        );
      }

      if (bDeleteEnable) {
        oDeleteButtonView = (
            <TooltipView placement="bottom" label={getTranslation().DELETE}>
              <div className="relativeCell settingIcon deleteButton" onClick={_this.handleDeleteButtonClicked.bind(_this, oData.id)}>
                <div className="delete"></div>
              </div>
            </TooltipView>
        );
      }

      if (bOpenEnable) {
        oViewButtonView = (
            <TooltipView placement="bottom" label={getTranslation().VIEW_CONTENT}>
              <div className="relativeCell settingIcon openButton" onClick={_this.handleOpenButtonClicked.bind(_this, oData.id)}>
                <div className="open"></div>
              </div>
            </TooltipView>
        );
      }

      if(bEditEnable || bDeleteEnable || bOpenEnable) {
        aTableCells.push(
            <th className="settingsCell" key={iColIndex}>
              <div className="settingsCellContainer">
                {oViewButtonView}
                {oEditButtonView}
                {oDeleteButtonView}
              </div>
            </th>
        );
      }

      aTableRows.push(<tr className={sRowDataClassName} key={iIndex}>{aTableCells}</tr>);
    });

    return aTableRows;
  }

  render() {
    return (
        <tbody className="tableBodyView">
          {this.getTableBody()}
        </tbody>
    );
  }

}

TableBodyView.propTypes = oPropTypes;

export const view = TableBodyView;
export const events = oEvents;
