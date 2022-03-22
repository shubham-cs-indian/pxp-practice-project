import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as GridPropertyView } from './grid-property-view';
import TooltipView from './../tooltipview/tooltip-view';
import { view as RadioButtonView } from '../radiobuttonview/radio-button-view';
import { view as BulkDownloadDialogView} from '../../screens/homescreen/screens/contentscreen/view/bulk-download-dialog-view';

const oEvents = {};

const oPropTypes = {
  className: ReactPropTypes.string,
  onRowClick: ReactPropTypes.func,
  context: ReactPropTypes.string,
  gridPropertyViewHandlers: ReactPropTypes.object,

  //For Row
  rowNumber: ReactPropTypes.number,
  rowType: ReactPropTypes.oneOf(["leftFixed", "scrollable", "action"]),
  rowData: ReactPropTypes.object,
  columnData: ReactPropTypes.array,
  activeContentId: ReactPropTypes.string,
  showCheckboxColumn: ReactPropTypes.bool,

  selectedContentIds: ReactPropTypes.array,
  totalDataCount: ReactPropTypes.number,
  totalNestedItems: ReactPropTypes.number,

  rowVisualData: ReactPropTypes.object,
  activeCellDetails: ReactPropTypes.object,
  hierarchical: ReactPropTypes.bool,
  isGridDataDirty: ReactPropTypes.bool,
  showIsRowDisabled: ReactPropTypes.bool,
  actionItems: ReactPropTypes.array,

  showContentCheckBox: ReactPropTypes.bool,
  showContentRadioButton: ReactPropTypes.bool,

  //Functions
  handleEmptyCellClicked: ReactPropTypes.func,
  makeGridCellActiveFromActiveCellDetails: ReactPropTypes.func,
  handleSelectClicked: ReactPropTypes.func,
  handleSelectAllClicked: ReactPropTypes.func,
  handleGridCellClicked: ReactPropTypes.func,
  getDividedWidth: ReactPropTypes.func,
  getActionItemCellWidth: ReactPropTypes.func,
  setActionItemCellWidth: ReactPropTypes.func,
  getActionItemWidth: ReactPropTypes.func,
  handleActionItemClicked: ReactPropTypes.func,
  duplicateCode: ReactPropTypes.array,
  duplicateLabel: ReactPropTypes.array,
  onCellMouseUp: ReactPropTypes.func,
  resizableWidth: ReactPropTypes.string,
  tableContextId: ReactPropTypes.string
};
/**
 * @class GridRowView
 * @memberOf Views
 * @property {string} [className] - To apply CSS className for row.
 * @property {func} [onRowClick]  - Executes when row is clicked.
 * @property {number} [rowNumber] - Contains row number.
 * @property {custom} [rowType]   - There are three row types("leftFixed", "rightFixed", "scrollable").
 * @property {object} [rowData]   - (id, pathToRoot, properties etc).
 * @property {array} [columnData] - Contains column data.
 * @property {string} [activeContentId] - Active content id.
 * @property {bool} [showCheckboxColumn] - To show checkbox for row.
 * @property {array} [selectedContentIds] - Contains selected row ids.
 * @property {number} [totalDataCount] - Total count of rows(we do not store nested rows count in totalDataCount).
 * @property {number} [totalNestedItems] - Total rows count(we store nested rows count in totalNestedItems).
 * @property {object} [rowVisualData] - Contains isExpandable property of row.
 * @property {object} [activeCellDetails] - Contains active cell details.
 * @property {bool} [hierarchical] - Contains true if row has nested row.
 * @property {bool} [isGridDataDirty] - Indicating grid data is dirty or not.
 * @property {array} [actionItems] - Actions details(ex. delete tag group, delete tag value, create tag value, move up, move down).
 * @property {func} [handleEmptyCellClicked] - Executes after empty cell is clicked.
 * @property {func} [makeGridCellActiveFromActiveCellDetails] -
 * @property {func} [handleSelectClicked] - Executes when the checkbox is clicked to select the row.
 * @property {func} [handleSelectAllClicked] - Executes after select all button is clicked.
 * @property {func} [handleGridCellClicked] - Executes after cell is clicked.
 * @property {func} [getDividedWidth] - To gets width to divide rows.
 * @property {func} [getActionItemCellWidth] - To get action item cell width.
 * @property {func} [setActionItemCellWidth] - To set action item cell width.
 * @property {func} [getActionItemWidth] - To get action item width.
 * @property {func} [handleActionItemClicked] - Executes when action item is clicked(actions eg. delete tag group, delete tag value, create tag value, move up, move down).
 */

// @CS.SafeComponent
class GridRowView extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      className: '',
      style: {},
      onClick: CS.noop
    }
  }

  /*shouldComponentUpdate: function (oNextProps, oNextState) {
    return !CS.isEqual(oNextProps, this.props) || !CS.isEqual(oNextState, this.state);
  },*/

  handleRowClicked =(oEvent)=> {
    let oRowData = this.props.rowData;

    //Do not remove below lines
    oEvent.nativeEvent.activeGridRowId = oRowData.id;

    if(this.props.onRowClick) {
      this.props.onRowClick(this.props.rowNumber, oEvent);
    }
  }

  handleSelectClicked =(oEvent)=> {
    var oRowData = this.props.rowData;
    this.props.handleSelectClicked(oRowData.id, oEvent);
  }

  handleSelectAllClicked =(bSelectAll, oEvent)=> {
    this.props.handleSelectAllClicked(bSelectAll, oEvent);
  }

  handleActionItemClicked =(sId, oEvent)=> {
    var oRowData = this.props.rowData;
    this.props.handleActionItemClicked(sId, oRowData.id, oEvent)
  }

  handleEmptyCellClicked =(oCellData, oEvent)=> {
    this.props.handleEmptyCellClicked(oCellData, oEvent)
  }

  getSelectOptionCell =(bIsHeader, sContentId, bIsDirty, bIsDisabled)=> {
    var __props = this.props;
    var aSelectedContentIds = __props.selectedContentIds;
    var iTotalDataCount = __props.totalDataCount || []; //todo: okay?
    var fOnClickHandler = this.handleSelectClicked;
    let bShowSquareCheckBox = this.props.showContentCheckBox;  //TODO: check and remove(Snehal)
    let bShowRadioButton = this.props.showContentRadioButton && !bIsHeader;
    let oRowData = __props.rowData;
    var sContainerClass = "gridViewCell selectOption ";
    var sIconClass = oRowData.isDisabled ? "selectIcon disabled" : "selectIcon ";
    if (bShowRadioButton) {
      sIconClass = "contentRadioButtonIcon ";
    }
    if(bIsDisabled) {
      sContainerClass += " disabled ";
      fOnClickHandler = CS.noop;
    }
    if (oRowData.isDisabled && oRowData.showDisabled) {
      sContainerClass += " showRowIsDisabled";
    }

    var iTotalLength = __props.totalNestedItems ? __props.totalNestedItems : iTotalDataCount;

    if (bIsHeader) {
      sContainerClass += "header";
      var bSelectAll = true;
      if (iTotalLength && (aSelectedContentIds.length == iTotalLength)) {
        sIconClass += "isSelected";
        bSelectAll = false;
      }
      fOnClickHandler = this.handleSelectAllClicked.bind(this, bSelectAll);
    } else if (aSelectedContentIds.includes(sContentId)) {
      sIconClass += "isSelected";
    }

    var oDirtyIndicator = bIsDirty ? <div className="dirtyIndicator"></div> : null;
    // if (bIsSelected) {
    //   sIconClass += "isSelected";
    // }

    if (bShowRadioButton) {
      return (
          <div className={sContainerClass} key={sContentId + "_select"}>
            {oDirtyIndicator}
            <RadioButtonView context={sIconClass}
                             selected={aSelectedContentIds.includes(sContentId)}
                             clickHanlder={fOnClickHandler}/>
          </div>);
    } else {
      return (
          <div className={sContainerClass} key={sContentId + "_select"} onClick={fOnClickHandler}>
            {oDirtyIndicator}
            <div className={sIconClass}></div>
          </div>);
    }
  };

  getActionItemsCell =(bIsHeader, oContent)=> {
    var _this = this;
    var __props = this.props;
    var aActionItems = __props.actionItems;
    if (CS.isEmpty(aActionItems)) {
      return null;
    }
    var iNumberOfActionItems = aActionItems.length;
    var oActionItemCellStyle = {};

    var iActionItemCellWidth = __props.getActionItemCellWidth();
    if (bIsHeader) {
      iActionItemCellWidth = iNumberOfActionItems * __props.getActionItemWidth() + 10; //10px for
      __props.setActionItemCellWidth(iActionItemCellWidth);
      // padding
      oActionItemCellStyle.width = iActionItemCellWidth + "px";
      return (
          <div key="actionItemsHeader" className="gridViewCell header actionItemsContainer" style={oActionItemCellStyle}></div>
      );
    }

    var aActionItemViews = [];
    let oDownloadImageDialogView = null;
    CS.forEach(oContent.actionItemsToShow, function (sId) {
      let oActionItem = CS.find(aActionItems, { id: sId });
      if (oActionItem.id === "download") {
        let oVisualData = __props.rowData || {};
        aActionItemViews.push(
            <TooltipView placement="left" label={CS.getLabelOrCode(oActionItem)}
                         key={oContent.id + "_actionItem_" + sId}>
              <div>
                <a className={"iconDownloadImage "}
                   href={"asset/Document/" + oVisualData.assetId} target="_blank">
                  <div className={"actionItemIcon " + oActionItem.class}></div>
                </a>
              </div>
            </TooltipView>
        )
      } else if (oActionItem.id === "downloadImage") {
        aActionItemViews.push(
            <TooltipView placement="left" label={CS.getLabelOrCode(oActionItem)} key={oContent.id + "_actionItem_" + sId}>
              <div className={"actionItemIcon " + oActionItem.class} onClick={_this.handleActionItemClicked.bind(_this, sId)}></div>
            </TooltipView>
        );

        if (oActionItem.showDownloadDialog && oActionItem.selectedContentId === oContent.id && CS.isNotEmpty(oActionItem.downloadDialogData)) {
          let oDownloadImageDialogData = oActionItem.downloadDialogData;
          oDownloadImageDialogView = (<BulkDownloadDialogView
              {...oDownloadImageDialogData}
          />)
        }
      }
      else {
        if (oActionItem.id === "dummy") {
          //To show empty space
          aActionItemViews.push(<div className={"actionItemIcon "}/>);
        } else {
          aActionItemViews.push(
              <TooltipView placement="left" label={CS.getLabelOrCode(oActionItem)}
                           key={oContent.id + "_actionItem_" + sId}>
                <div className={"actionItemIcon " + oActionItem.class}
                     onClick={_this.handleActionItemClicked.bind(_this, sId)}></div>
              </TooltipView>
          );
        }
      }
    });

    oActionItemCellStyle.width = iActionItemCellWidth + "px";

    return (
        <div className="gridViewCell actionItemsContainer" style={oActionItemCellStyle} key={oContent.id + "_actionItems"}>
          {aActionItemViews}
          {oDownloadImageDialogView}
        </div>
    );
  }

  getColumns =()=> {
    var _this = this;
    var __props = _this.props;
    var oRowData = __props.rowData;
    var aColumnData = __props.columnData;
    var oProperties = oRowData.properties;
    var aColumnViews = [];

    switch(__props.rowType) {
      case "leftFixed":
        if (oRowData.id === __props.activeContentId) {
          aColumnViews.push(<div key="activeRow" className="activeRow"></div>);
        }

        __props.showCheckboxColumn || __props.showContentRadioButton ? aColumnViews.push(_this.getSelectOptionCell(false, oRowData.id, oRowData.isDirty, // eslint-disable-line
            oRowData.isDisabled)) : null;

        //left fixed
        CS.forEach(aColumnData, function (oColumn) {
          var oProperty = oProperties[oColumn.id];
          if(!oColumn.hideColumn) {
            aColumnViews.push(_this.getCellAccordingToPropertyType(oProperty, oColumn, oRowData, false, true));
          }
        });
        break;


      case "scrollable":
        //scrollable
        CS.forEach(aColumnData, function (oColumn, iIndex) {
          var oProperty = oProperties[oColumn.id];
          let bIsLastScrollableCell = (iIndex === (aColumnData.length - 1));
          aColumnViews.push(_this.getCellAccordingToPropertyType(oProperty, oColumn, oRowData, bIsLastScrollableCell, false));
        });
        break;


      case "action":
        //right fixed
        //decide which icons to show in the action items
        aColumnViews.push(_this.getActionItemsCell(false, oRowData));
        break;
    }

    return aColumnViews;
  }

  // for resizing width of columns : http://jsfiddle.net/T4St6/82/

  getCellAccordingToPropertyType =(oProperty, oColumn, oContent, bIsLastScrollableCell, bIsFixedColumn)=> {
    var __props = this.props;
    var iCellWidth = oColumn.width;
    var sPathToRoot = oContent.pathToRoot;
    var oMetadata = oContent.metadata;
    let oReferencedAttributes = __props.referencedAttributes;
    let oReferencedAttribute = oReferencedAttributes && oReferencedAttributes[oColumn.id];

    if (!bIsFixedColumn) {
      iCellWidth += __props.getDividedWidth(bIsLastScrollableCell);
    }

    var sContentId = oContent.id;
    /** if resizable width is present then set it else take it from skeleton**/
    let oCellStyle = __props.resizedColumnId === oColumn.id ? {width: oColumn.width} : {width: iCellWidth + "px"};

    /*enablePointer Event to display tooltip in disabled column.*/
    if(oColumn.hasOwnProperty("enablePointerEvent") && oColumn.enablePointerEvent) {
      oCellStyle.pointerEvents = "all";
    } else if (oColumn.isDisabled) {
      oCellStyle.pointerEvents = "none";
    }

    if (CS.isEmpty(oProperty)) {
      //show greyed out cell
      oCellStyle.backgroundColor = (oColumn.hasOwnProperty("showDisabledColumn") && oColumn.showDisabledColumn) ? "transparent" : "#f5f5f5";
      let oCellData = {
        property: oProperty,
        column: oColumn,
        contentId: sContentId,
        pathToRoot: sPathToRoot
      };
      return (
          <div className="gridViewCell"
               style={oCellStyle}
               key={sContentId + "_" + oColumn.id}
               onClick={this.handleEmptyCellClicked.bind(this, oCellData)}>
          </div>
      );
    }

    var oRowVisualData = __props.rowVisualData || {};
    var bIsParent = false;
    var bIsExpanded = false;
    if (oColumn.id == "label" || oProperty.isExpandable) { //todo: should not keep this hardcoded! need to change.
      bIsParent = !CS.isEmpty(oContent.children) || oProperty.isExpandable;
      oRowVisualData && (bIsExpanded = oRowVisualData.isExpanded);
    }
    var oActiveCellDetails = __props.activeCellDetails;
    var fRefFunction = null;
    if (oActiveCellDetails && (oActiveCellDetails.dataId === oContent.id) && (oActiveCellDetails.columnId === oColumn.id)) {
      fRefFunction = __props.makeGridCellActiveFromActiveCellDetails;
    }

    return (
      <div className="gridViewCell" style={oCellStyle} key={sContentId + "_" + oColumn.id} onClick={__props.handleGridCellClicked} ref={fRefFunction} onMouseUp={__props.onCellMouseUp}>
          <GridPropertyView contentId={sContentId}
                            context={this.props.context}
                            {...this.props.gridPropertyViewHandlers || {}}
                            property={oProperty}
                            referencedAttribute={oReferencedAttribute}
                            propertyId={oColumn.id}
                            propertyType={oColumn.type}
                            pathToRoot={sPathToRoot}
                            metadata={oMetadata}
                            isParent={bIsParent}
                            isExpanded={bIsExpanded}
                            isExpandable={oProperty.isExpandable}
                            hierarchical={__props.hierarchical}
                            isGridDataDirty={__props.isGridDataDirty}
                            extraData={oColumn.extraData}
                            duplicateCode={this.props.duplicateCode}
                            duplicateLabel={this.props.duplicateLabel}
                            gridWFValidationErrorList={this.props.gridWFValidationErrorList}
                            resizableWidth={this.props.resizableWidth}
                            hideIconUpload={this.props.hideIconUpload}
                            adjustRowHeight={this.props.adjustRowHeight}
                            tableContextId={this.props.tableContextId}
          />
        </div>
    );
  }

  render() {
    var __props = this.props;
    var sClassName = "gridRowContainer ";
    var oRowData = __props.rowData;
    sClassName += __props.className + " ";
    if (oRowData.isDisabled && oRowData.showDisabled) {
      sClassName += " showDisabled";
    }

    return (
        <div className={sClassName} onClick={this.handleRowClicked}>
          {this.getColumns()}
        </div>
    )
  }
}

GridRowView.propTypes = oPropTypes;

export const view = GridRowView;
export const events = oEvents;
