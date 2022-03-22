import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import UniqueIdentifierGenerator from '../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import TooltipView from './../tooltipview/tooltip-view';
import { view as MatchAndMergeAttributeCellView } from './match-and-merge-attribute-cell-view';
import { view as MatchAndMergeCommonCellView } from './match-and-merge-common-cell-view';
import ViewUtils from "../../screens/homescreen/screens/contentscreen/view/utils/view-utils";
import {view as ImageFitToContainerView} from "../imagefittocontainerview/image-fit-to-container-view";

const oEvents = {
  MATCH_AND_MERGE_COLUMN_HEADER_CLICKED: "match_and_merge_column_header_clicked",
  MATCH_AND_MERGE_VERSION_ROLLBACK_BUTTON_CLICKED: "match_and_merge_version_rollback_button_clicked"
};

const oPropTypes = {
  rowData: ReactPropTypes.array,
  columnData: ReactPropTypes.array,
  firstColumnWidth: ReactPropTypes.number,
  tableId: ReactPropTypes.string,
  tableGroupName: ReactPropTypes.string,
  referencedAssetsData: ReactPropTypes.object,
  isInstancesComparisonView: ReactPropTypes.bool,
  showHeader: ReactPropTypes.bool,
  actionItems: ReactPropTypes.object,
  hideEqualElements: ReactPropTypes.bool,
  isGoldenRecordComparison: ReactPropTypes.bool
};
/**
 * @class MatchAndMergeView -  Use for Comparision between products in Application.
 * @memberOf Views
 * @property {array} [rowData] -  an array of rowData.
 * @property {array} [columnData] -  an array of columnData.
 * @property {number} [firstColumnWidth] -  a number for firstColumnWidth.
 * @property {string} [tableId] -  string of tableId.
 * @property {string} [tableGroupName] -  string of tableGroupName.
 * @property {object} [referencedAssetsData] -  style object which is used referencedAssetsData.
 * @property {bool} [isInstancesComparisonView] -  boolean which is used for isInstancesComparisonView or not.
 * @property {bool} [showHeader] -  boolean which is used for showHeader or not.
 * @property {object} [actionItems] -  style object which is used actionItems.
 * @property {bool} [hideEqualElements] -  boolean which is used for hideEqualElements or not.
 * @property {bool} [isGoldenRecordComparison] -  boolean which is used for isGoldenRecordComparison or not.
 */

const iColumnWidth = 250;
// @CS.SafeComponent
class MatchAndMergeView extends React.Component {

  constructor(props) {
    super(props);

    let sViewContextUUID = UniqueIdentifierGenerator.generateUUID();
    this.state = {
      viewContext: sViewContextUUID
    }

    this.setRef = ( sRef, element) => {
      this[sRef] = element;
    };

    this.leftFixedSection = React.createRef();
  }

  componentDidMount() {
    this.equaliseRowHeights();
    //this.calculateScrollbarVisibility();
    //this.rescaleScrollableColumnsWidthIfRequired();
  }

 /* componentWillUpdate() {
    this.setAllRowHeightsToAuto();
  }*/

  componentDidUpdate(oPreProps) {
    this.setAllRowHeightsToAuto(oPreProps);
    this.equaliseRowHeights();
    //this.calculateScrollbarVisibility();
    //this.rescaleScrollableColumnsWidthIfRequired();
  }

 /* handleColumnHeaderClicked =(sColumnId)=> {
    EventBus.dispatch(oEvents.MATCH_AND_MERGE_COLUMN_HEADER_CLICKED, sColumnId);
  }*/

  handleRollbackVersionButtonClicked =(sActionItem , sVersionId)=>{
    EventBus.dispatch(oEvents.MATCH_AND_MERGE_VERSION_ROLLBACK_BUTTON_CLICKED, sActionItem, sVersionId);
  };

  equaliseRowHeights =()=> {
    // let oRefs = this.refs;
    let _this = this;
    let aRowData = this.props.rowData;

    CS.forEach(aRowData, function (oRow) {

      //get left fixed section (LFS) row
      let sLFSRowRef = "LFSRow_" + oRow.id;
      let oLFSRowDOM = _this[sLFSRowRef];

      //get scrollable section (SS) row
      let sSSRowRef = "SSRow_" + oRow.id;
      let oSSRowDOM = _this[sSSRowRef];

      if (oLFSRowDOM && oSSRowDOM) {
        let oLFSRowBoundingClientRectangle = oLFSRowDOM.getBoundingClientRect();
        let iLFSRowHeight = oLFSRowBoundingClientRectangle.height;
        let oSSSRowBoundingClientRectangle = oSSRowDOM.getBoundingClientRect();
        let iSSRowHeight = oSSSRowBoundingClientRectangle.height;
        let iNewHeight = 0;

        //if LFS row height > SS row height, set LFS row height to both
        if (iLFSRowHeight > iSSRowHeight) {
          iNewHeight = iLFSRowHeight;
        }
        //if LFS row height <= SS row height, set SS row height to both
        else {
          iNewHeight = iSSRowHeight;
        }

        oLFSRowDOM.style.height = iNewHeight + "px";
        oSSRowDOM.style.height = iNewHeight + "px";
      }
    });
  }

  setAllRowHeightsToAuto =(oPreProps)=> {
    // let oRefs = this.refs;
    let _this = this;
    let aRowData = oPreProps?  oPreProps.rowData : this.props.rowData;

    CS.forEach(aRowData, function (oRow) {

      //get left fixed section (LFS) row
      let sLFSRowRef = "LFSRow_" + oRow.id;
      let oLFSRowDOM = _this[sLFSRowRef];

      //get scrollable section (SS) row
      let sSSRowRef = "SSRow_" + oRow.id;
      let oSSRowDOM = _this[sSSRowRef];

      if (oLFSRowDOM && oSSRowDOM) {
        oLFSRowDOM.style.height = "auto";
        oSSRowDOM.style.height = "auto";
      }
    });
  };

  getCellViewAccordingToType =(oRow, oColumn, oProperty)=> {
    let sRowId = oRow.id;
    let iColumnId = oColumn.id;
    let sTableId = this.props.tableId;
    let sRendererType  = oRow.rendererType;
    let sTableGroupName = this.props.tableGroupName;
    let oComparisonProperty = null;
    if(this.comparisonColumn && this.comparisonColumn.id !== oColumn.id) {
      oComparisonProperty = this.comparisonColumn.properties[sRowId] || {};
    }

    switch (oRow.type) {

      case "attribute":
        let oMasterAttribute = oRow.masterAttribute;
        let iColumnWidth = (oColumn.width ? oColumn.width : iColumnWidth);

        return (
            <MatchAndMergeAttributeCellView
                property={oProperty}
                comparisonProperty={oComparisonProperty}
                columnWidth={iColumnWidth}
                rendererType={sRendererType}
                masterAttribute={oMasterAttribute}
                rowId={sRowId}
                columnId={iColumnId}
                tableId={sTableId}
                isRowDisabled={oRow.isDisabled}
                tableGroupName={sTableGroupName}
                isInstancesComparisonView={this.props.isInstancesComparisonView}
                isGoldenRecordComparison={this.props.isGoldenRecordComparison}
            />
        );

      case "lifeCycleStatusTag":
      case "taxonomy":
      case "type":
      case "name":
      case "eventSchedule":
      case "image":
        return (
            <MatchAndMergeCommonCellView
                property={oProperty}
                comparisonProperty={oComparisonProperty}
                rendererType={sRendererType}
                tableId={sTableId}
                rowData={oRow}
                isRowDisabled={oRow.isDisabled}
                tableGroupName={sTableGroupName}
                isInstancesComparisonView={this.props.isInstancesComparisonView}
                hideComparison={!this.props.isGoldenRecordComparison}
                isGoldenRecordComparison={this.props.isGoldenRecordComparison}
            />
        );
    }
  }

  getIconDOMForAttribute = (oRow) => {
    if (oRow.type === "attribute") {
      let sImageSrc = ViewUtils.getIconUrl(oRow.masterAttribute.iconKey);
      return (
          <div className="customIcon">
            <ImageFitToContainerView imageSrc={sImageSrc}/>
          </div>
      )
    }
    return null;
  };

  generateRows =(aRowData, aColumnData, oTableData)=> {
    //generate rows
    //iterate over rowData
    //first cell in each row will be rowData obj label
    //iterate over columnData and add cell for each row
    let _this = this;
    let iKey = 0;

    CS.forEach(aRowData, function (oRow, iRowIndex) {
      let bHideEqualElements = _this.props.hideEqualElements;
      if(!(bHideEqualElements && oRow.isSkipped)) {
        let sRowId = oRow.id;
        let sRendererType = oRow.rendererType;
        let aLeftFixedSectionRowCells = [];
        let aScrollableSectionRowCells = [];
        let oRowStyle = {
          height: "auto"
        };

        let oMasterAttribute = oRow.masterAttribute;
        let oLanguageIndicatorDOM = oMasterAttribute && oMasterAttribute.isTranslatable ?
                                    <div className="languageDependentIndicator"></div> : null;

        //first column with all the labels
        aLeftFixedSectionRowCells.push(
            <div className="mnmTableCell firstColumnCell" key={iKey++} title={CS.getLabelOrCode(oRow)}>
              {_this.getIconDOMForAttribute(oRow)}
              <span className="mnmTableCellLabel">{CS.getLabelOrCode(oRow)}</span>
              {oLanguageIndicatorDOM}
            </div>
        );

        CS.forEach(aColumnData, function (oColumn) {
          let oCellStyle = {};
          let iColumnWidth = (oColumn.width ? oColumn.width : iColumnWidth);
          oCellStyle.width =  iColumnWidth + "px";
          let oProperty = oColumn.properties[sRowId] || {};
          let sCellClassName = "mnmTableCell";

          if(sRendererType === "image" || oRow.rendererType === "coverflow")  {
            sCellClassName += " image"
          }
          if(!oColumn.isFixed){
            sCellClassName = sCellClassName + " mnmScrollableCell";
          }

          if(_this.props.isInstancesComparisonView || _this.props.isGoldenRecordComparison) {
            oColumn.forComparison && (sCellClassName += " forComparison");
          }

          if (_this.props.isGoldenRecordComparison && oProperty.isRecommended == true) {
            sCellClassName += " isRecommended";
          }
          let oValueView = oProperty.isNotApplicable ? <div className="notApplicableText">{getTranslation().NOT_APPLICABLE}</div> : _this.getCellViewAccordingToType(oRow, oColumn, oProperty);
          let oCell = (
              <div className={sCellClassName}
                   key={iKey++}
                   style={oCellStyle}>
                {oValueView}
              </div>
          );

          if (oColumn.isFixed) {
            aLeftFixedSectionRowCells.push(oCell);
          } else {
            aScrollableSectionRowCells.push(oCell);
          }

        });

        oTableData.leftFixedSectionRows.push(
            <div className="mnmTableRow"
                 key={iKey++}
                 ref={_this.setRef.bind(_this,"LFSRow_" + oRow.id)}>
              {aLeftFixedSectionRowCells}
            </div>
        );

        oTableData.scrollableSectionRows.push(
            <div className="mnmTableRow mnmScrollableRow"
                 key={iKey++}
                 ref={_this.setRef.bind(_this,"SSRow_" + oRow.id)}>
              {aScrollableSectionRowCells}
            </div>
        );
      }
    });
  }

  generateHeaderRow =(aRowData, aColumnData, oTableData)=> {
    //generate header row
    //empty cell in header row of leftFixedSectionRows
    //iterate through columnData and keep adding cells in leftFixedSectionRows or scrollableSectionRows as required.
    let _this = this;
    let aLeftFixedSectionRowCells = [];
    let aScrollableSectionRowCells = [];
    let bShowHeader = this.props.showHeader;

    if(bShowHeader) {
      //first column with all the labels
      aLeftFixedSectionRowCells.push(
          <div className="mnmTableCell headerCell firstColumnCell"></div>
      );
    }

    oTableData.leftFixedSectionWidth += this.props.firstColumnWidth;

    CS.forEach(aColumnData, function (oColumn) {
      let oCellStyle = {};
      let iColumnWidth = (oColumn.width ? oColumn.width : iColumnWidth);
      oCellStyle.width =  iColumnWidth + "px";

      let sCellClassName = "mnmTableCell headerCell";

      if(!oColumn.isFixed){
        sCellClassName = sCellClassName + " mnmScrollableCell";
        _this.scrollableColumn++;
      }

      let aActionButtonDom = [];
      CS.forEach(oColumn.actionItems, function (oActionItems) {
        aActionButtonDom.push(<TooltipView label={CS.getLabelOrCode(oActionItems)}>
          <div className={oActionItems.className}
               onClick={_this.handleRollbackVersionButtonClicked.bind(_this, oActionItems.id, oColumn.id)}>
          </div>
        </TooltipView>);
      });

      //let sColumnId = oColumn.id;
      //let fOnColumnHeaderClick = (_this.props.isInstancesComparisonView && !oColumn.isFixed) ? _this.handleColumnHeaderClicked.bind(_this, sColumnId) : null;

      let oCell = bShowHeader ? (
          <div className={sCellClassName}>{CS.getLabelOrCode(oColumn)}
            {aActionButtonDom}
          </div>) : null;

      if(oColumn.forComparison) {
        _this.comparisonColumn = oColumn;
      }

      if (oColumn.isFixed) {
        aLeftFixedSectionRowCells.push(oCell);
        oTableData.leftFixedSectionWidth += iColumnWidth;
      } else {
        aScrollableSectionRowCells.push(oCell);
        oTableData.scrollableSheetWidth += iColumnWidth;
      }
    });

    if(bShowHeader) {
      let sViewContext = _this.state.viewContext || "";
      oTableData.leftFixedSectionRows.push(
          <div className={"mnmTableRow headerRow " + sViewContext}>
            {aLeftFixedSectionRowCells}
          </div>
      );

      oTableData.scrollableSectionRows.push(
          <div className={"mnmTableRow headerRow mnmScrollableRow " + sViewContext}>
            {aScrollableSectionRowCells}
          </div>
      );
    }
  }


  render() {

    let _this = this;
    let aRowData = this.props.rowData;
    let aColumnData = this.props.columnData;
    let oTableData = {
      leftFixedSectionRows : [],
      scrollableSectionRows : [],
      leftFixedSectionWidth : 0,
      scrollableSheetWidth : 0
    };
    this.scrollableColumn = 0;

    this.generateHeaderRow(aRowData, aColumnData, oTableData);


    this.generateRows(aRowData, aColumnData, oTableData);



    return (
        <div className="matchAndMergeView">

          <div className="mnmLeftFixedSection leftFixedSection" ref={this.setRef.bind(this,"leftFixedSection")}>
            <div className="mnmTableSection">
              {oTableData.leftFixedSectionRows}
            </div>
          </div>

          <div className="mnmScrollableSection mnmScroll"  ref={this.setRef.bind(this,"scrollableSection")} onScroll={this.handleScrollableSectionScrolled}>
            <div className="mnmTableSection mnmChildTableSection" ref={this.setRef.bind(this,"mnmChildTableSection")}>
              {oTableData.scrollableSectionRows}
            </div>
          </div>

        </div>
    );
  }

}

MatchAndMergeView.propTypes = oPropTypes;
MatchAndMergeView.defaultProps = {
  rowData: [],
  columnData: [],
  firstColumnWidth: iColumnWidth
};

export const view = MatchAndMergeView;
export const events = oEvents;
