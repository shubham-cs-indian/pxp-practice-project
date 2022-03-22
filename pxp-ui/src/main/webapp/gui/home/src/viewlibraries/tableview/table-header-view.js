import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import TooltipView from './../tooltipview/tooltip-view';
import { view as SimpleImageView } from '../imagesimpleview/image-simple-view';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import ViewUtils from '../utils/view-library-utils';

const oEvents = {
  TABLE_EDIT_BUTTON_CLICKED: "table_edit_button_clicked",
  TABLE_DELETE_BUTTON_CLICKED: "table_delete_button_clicked",
  TABLE_OPEN_BUTTON_CLICKED: "table_open_button_clicked"
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

  settings: ReactPropTypes.shape({
    showCheckbox: ReactPropTypes.bool,
    showEditButton: ReactPropTypes.bool,
    showDeleteButton: ReactPropTypes.bool,
    showOpenButton: ReactPropTypes.bool,
    toolbar: ReactPropTypes.arrayOf(
        ReactPropTypes.shape({
          id: ReactPropTypes.string,
          tooltip: ReactPropTypes.string,
          className: ReactPropTypes.string
        })
    ),
    sortData: ReactPropTypes.shape({
      sortBy: ReactPropTypes.string,
      sortOrder: ReactPropTypes.string
    }),
  }),

  onSortChange: ReactPropTypes.func,
  isRowSelectionMode: ReactPropTypes.bool,
  filterContext: ReactPropTypes.string.isRequired
};
/**
 * @class TableHeaderView - Use for Display Header of Table view.
 * @memberOf Views
 * @property {string} [tableContextId] - Pass id of table.
 * @property {array} [headerData] - Pass data of Header in Table.
 * @property {custom} [settings] - Pass model contain showCheckbox, showEditButton, showDeleteButton, showOpenButton, toolbar, id, tooltip, className, sortData, sortBy, sortOrder.
 * @property {func} [onSortChange] - Pass sorted data function on base of text, calculated, date, price etc.
 */

// @CS.SafeComponent
class TableHeaderView extends React.Component {

  constructor(props) {
    super(props);
  }

  shouldComponentUpdate(oNextProps) {
    var oOldProps = this.props;
    return !CS.isEqual(oNextProps.headerData, oOldProps.headerData) || !CS.isEqual(oNextProps.settings.sortData, oOldProps.settings.sortData);
  }

  handleColumnHeaderClicked = (sHeaderId) => {
    let oSortData = this.props.settings.sortData;
    //check if header id is the sort id
    let oSortDataToSend = ViewUtils.getUpdatedSortData(oSortData, sHeaderId);

    if(CS.isFunction(this.props.onSortChange)) {
      this.props.onSortChange(oSortDataToSend);
    }

  }

  handleEditButtonClicked = (sDataId) => {
    var oSettings = this.props.settings;
    var sTableContextId = this.props.tableContextId;

    EventBus.dispatch(oEvents.TABLE_EDIT_BUTTON_CLICKED, sDataId, oSettings.context, sTableContextId);
  };

  handleDeleteButtonClicked = (sDataId) => {
    var oSettings = this.props.settings;
    var sTableContextId = this.props.tableContextId;
    EventBus.dispatch(oEvents.TABLE_DELETE_BUTTON_CLICKED, sDataId, oSettings.context, sTableContextId, this.props.filterContext);
  };

  handleOpenButtonClicked = (sDataId) => {
    var oSettings = this.props.settings;
    EventBus.dispatch(oEvents.TABLE_OPEN_BUTTON_CLICKED, sDataId, oSettings.context);
  };

  getSettingsView = (oData) => {
    let _this = this;
    let __props = _this.props;
    let oSettings = __props.settings;
    let bEditEnable, bDeleteEnable, bOpenEnable = false;
    if (oSettings) {
      bEditEnable = oSettings.showEditButton;
      bDeleteEnable = oSettings.showDeleteButton;
      bOpenEnable = oSettings.showOpenButton;
    }

    var oEditButtonView, oDeleteButtonView, oViewButtonView = null;

    if (bEditEnable) {
      oEditButtonView = (
          <TooltipView placement="bottom" label={getTranslation().EDIT}>
            <div className="relativeCell settingIcon editButton"
                 onClick={_this.handleEditButtonClicked.bind(_this, oData.id)}>
              <div className="edit"/>
            </div>
          </TooltipView>
      );
    }

    if (bDeleteEnable) {
      oDeleteButtonView = (
          <TooltipView placement="bottom" label={getTranslation().DELETE}>
            <div className="relativeCell settingIcon deleteButton"
                 onClick={_this.handleDeleteButtonClicked.bind(_this, oData.id)}>
              <div className="delete"/>
            </div>
          </TooltipView>
      );
    }

    if (bOpenEnable) {
      oViewButtonView = (
          <TooltipView placement="bottom" label={getTranslation().VIEW_CONTENT}>
            <div className="relativeCell settingIcon openButton"
                 onClick={_this.handleOpenButtonClicked.bind(_this, oData.id)}>
              <div className="open"/>
            </div>
          </TooltipView>
      );
    }

    return (
        <div className="settingsCellContainer">
          {oViewButtonView}
          {oEditButtonView}
          {oDeleteButtonView}
        </div>
    );
  };

  getHeaders =()=> {
    var _this = this;
    var __props = _this.props;
    let oSettings = __props.settings;
    var aHeaders = [];
    var iIndex = 0;
    var oSortData = __props.settings.sortData || {};
    var sSortBy = oSortData.sortBy;
    var sSortOrder = oSortData.sortOrder;

    if (this.props.isRowSelectionMode) {
      aHeaders.push(
          <th className="settingsCell" key={iIndex}></th>
      );
    }

    CS.forEach(__props.headerData, function (oHeader) {
      if (oHeader.hideColumn) {
        return;
      }
      let oCellStyle = {};
      let fOnClickHandler;
      let oSortIndicator = null;
      if (oHeader.isSortable) {
        let sSortIndicatorClass = "sortIndicator ";
        let sSortIndicatorLabel;
        if (oHeader.id === sSortBy) {
          oCellStyle.backgroundColor = "#d8efff";
          oCellStyle.boxShadow = "inset 0 0 0 2px #5ca4e2";
          if (sSortOrder === "asc") {
            sSortIndicatorLabel = getTranslation().ASCENDING;
            sSortIndicatorClass += "asc";
          } else if (sSortOrder === "desc") {
            sSortIndicatorLabel = getTranslation().DESCENDING;
            sSortIndicatorClass += "desc";
          }
        } else {
          sSortIndicatorClass += "isSortable";
          sSortIndicatorLabel = getTranslation().SORT_BY;
        }
        fOnClickHandler = _this.handleColumnHeaderClicked.bind(_this, oHeader.id);
        oCellStyle.cursor = "pointer";
        oSortIndicator = (
            <TooltipView label={sSortIndicatorLabel}>
              <div className={sSortIndicatorClass} onClick={fOnClickHandler}></div>
            </TooltipView>
        );
      } else {
        fOnClickHandler = null;
      }

      let oIconOrColorView = null;
      /*if (oHeader.icon) {
        let sIconUrl = ViewLibraryUtils.getIconUrl(oHeader.icon);
        oIconOrColorView = (<SimpleImageView imageSrc={sIconUrl} classLabel="icon"/>);
      } else if (oHeader.color) {
        let oStyle = {background: oHeader.color};
        oIconOrColorView = (<div className="color" style={oStyle}/>);
      }*/

      var sCellClassName = "relativeCell headerCell ";
      if (oHeader.isSortable){
        sCellClassName += "isSortable";
      }

      let oSettingsView = null;
      if (oHeader.showSettings) { // For a transposed table, settings will be shown in tabel header (for language variants)
        oSettingsView = _this.getSettingsView(oHeader);
      }

      let sClassName = oHeader.isDirty ? "dirtyColumnData" : "";
      let sTableHeaderWrapperClassName = "thWrapper";
      sTableHeaderWrapperClassName += oSettings.isTranspose ? " isTranspose" : "";
      let oLabelView = [];
      if (oHeader.isMultipleValues && oSettings.isTranspose) {
        let aValues = oHeader.value;
        let aColors = oHeader.color;
        let aIcons = oHeader.icon;
        let aNeutralValues = oHeader.neutralValues;
        let aLabelView = [];

        CS.forEach(aValues, function (sValue, iIndex) {
          if (aIcons[iIndex]) {
            let sIconUrl = ViewUtils.getIconUrl(aIcons[iIndex]);
            oIconOrColorView = (<SimpleImageView imageSrc={sIconUrl} classLabel="icon"/>);
          } else if (aColors[iIndex]) {
            let oStyle = {background: aColors[iIndex]};
            oIconOrColorView = (<div className="color" style={oStyle}/>);
          }
          let sNaturalClass = "isNeutral ";
          if (!CS.isEmpty(aNeutralValues)) {
            sNaturalClass = aNeutralValues[iIndex] ? sNaturalClass + 'positive' : sNaturalClass + 'negative';
          }

          if (iIndex == 3) {
            aLabelView.push(<div className="headerWithIconAndLabel multipleValuesIndication">
              <div className={sCellClassName}>
                {"..."}
              </div>
            </div>);
          }

          aLabelView.push(
              <div className="headerWithIconAndLabel">
                <div className="iconOrColorViewWrapper">{oIconOrColorView}</div>
                <div className={sCellClassName}>
                  {sValue}
                </div>
                <div className={sNaturalClass}></div>
              </div>);
        });

        let oHeaderLabelView = (<div>{aLabelView}</div>);
        oLabelView = (<div className="headerContainer">
          <TooltipView placement="top" label={<div style={{maxWidth: 180}}
                                                   className="tooltipTableHeader maxWidthTooltip">{oHeaderLabelView}</div>}>
            {oHeaderLabelView}
          </TooltipView>
        </div>)

      }
      else {
        if (oSettings.isTranspose) {
          if (oHeader.icon) {
            let sIconUrl = ViewUtils.getIconUrl(oHeader.icon);
            oIconOrColorView = (
                <div className="iconOrColorViewWrapper">
                  <SimpleImageView imageSrc={sIconUrl} classLabel="icon"/>
                </div>);
          } else if (oHeader.color) {
            let oStyle = {background: oHeader.color};
            oIconOrColorView = (
                <div className="iconOrColorViewWrapper">
                  <div className="color" style={oStyle}/>
                </div>);
          }
        }

        oLabelView = (
            <div className="headerLabelWithIcons">
              {oIconOrColorView}
              <TooltipView label={oHeader.value}>
                <div className={sCellClassName}>
                  {oHeader.value}
                </div>
              </TooltipView>
            </div>);
      }

      aHeaders.push(
          <th key={iIndex++} style={oCellStyle} className={sClassName}>
            <div className={sTableHeaderWrapperClassName}>
              {oLabelView}
              {oSettingsView}
              {oSortIndicator}
            </div>
          </th>
      );
    });

    if(oSettings && !oSettings.isTranspose && (oSettings.showEditButton || oSettings.showDeleteButton || oSettings.showOpenButton)) {  // For a transposed table, do not push the last column, i.e., settings column (for language variants)
      aHeaders.push(
          <th className="settingsCell" key={iIndex}></th>
      );
    }

    return (
        <tr>{aHeaders}</tr>
    )
  }

  render() {
    return (
        <thead className="tableHeaderView">
          {this.getHeaders()}
        </thead>
    );
  }

}

TableHeaderView.propTypes = oPropTypes;

export const view = TableHeaderView;
export const events = oEvents;
