import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import UniqueIdentifierGenerator from '../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import TooltipView from './../tooltipview/tooltip-view';
import ViewUtils from "../../screens/homescreen/screens/contentscreen/view/utils/view-utils";
import {view as ImageFitToContainerView} from "../imagefittocontainerview/image-fit-to-container-view";

const oEvents = {
  MATCH_MERGE_TAG_VIEW_CELL_CLICKED: "match_merge_tag_view_cell_clicked",
  MATCH_AND_MERGE_TAG_VERSION_ROLLBACK_BUTTON_CLICKED: "match_and_merge_tag_version_rollback_button_clicked"
};

const oPropTypes = {
  rowData: ReactPropTypes.array,
  columnData: ReactPropTypes.array,
  firstColumnWidth: ReactPropTypes.number,
  tableId: ReactPropTypes.string,
  isInstancesComparisonView: ReactPropTypes.bool,
  hideEqualElements: ReactPropTypes.bool,
  isGoldenRecordComparison: ReactPropTypes.bool
};
/**
 * @class MatchAndMergeTagView - Use to Display Match and merge Tag view in comparison table view.
 * @memberOf Views
 * @property {array} [rowData] - Pass array of id, label, type, isDisabled, rendererType.
 * @property {array} [columnData] - Pass array of id, label, type, isDisabled, rendererType.
 * @property {number} [firstColumnWidth] - Pass 250 width default for first column.
 * @property {string} [tableId] - Pass table id.
 * @property {bool} [isInstancesComparisonView] - Pass bool value for instance comparison view.
 * @property {bool} [hideEqualElements] - Pass bool value for hide equal element or not.
 * @property {bool} [isGoldenRecordComparison] - Pass boolean for is this golden record comparison or not.
 */

const iColumnWidth = 250;
// @CS.SafeComponent
class MatchAndMergeTagView extends React.Component {

  constructor(props) {
    super(props);

    let sViewContextUUID = UniqueIdentifierGenerator.generateUUID();
    this.state = {
      viewContext: sViewContextUUID
    }

    this.leftFixedSection = React.createRef();
  }

  componentDidMount(){
  }

  componentDidUpdate() {
  }

  handleCellOnClick =(bShouldChangeValue, sRowId, oProperty, sColumnId)=>{
    if(bShouldChangeValue){
      let sTableId = this.props.tableId;
      EventBus.dispatch(oEvents.MATCH_MERGE_TAG_VIEW_CELL_CLICKED, sRowId, oProperty, sTableId, this.props.isGoldenRecordComparison, sColumnId);
    }
  }

  handleRollbackVersionButtonClicked =(sActionItem , sVersionId)=>{
    EventBus.dispatch(oEvents.MATCH_AND_MERGE_TAG_VERSION_ROLLBACK_BUTTON_CLICKED, sActionItem, sVersionId);
  };

  getColorRGBFromRelevance =(iRelevance)=> {

    if (iRelevance > 0) {
      return "#008000";
    } else if (iRelevance < 0) {
      return "#FF0000";
    } else {
      return "#FFBC00";
    }

  }

  getClockPercentView =(iPercent)=> {
    let iRelevance = iPercent;
    let sOuterCircleColor = "";
    let sDegree1 = "";
    let sShadowColor = "";
    let sDegree2 = "";
    let iFactor = 180 / 50;

    if (iRelevance > 50 || iRelevance < -50) {
      sShadowColor = this.getColorRGBFromRelevance(iRelevance);
      sOuterCircleColor = "#fff";
      sDegree1 = "90deg";
      sDegree2 = 90 + iFactor * (Math.abs(iRelevance) - 50) + "deg";
    } else {
      sOuterCircleColor = this.getColorRGBFromRelevance(iRelevance); //#CE4B0C
      sShadowColor = "#fff";
      sDegree1 = "270deg";
      sDegree2 = 90 + iFactor * Math.abs(iRelevance) + "deg";
    }

    let sLinearGradient = "linear-gradient(" + sDegree1 + ", transparent 50%, " + sShadowColor +
        " 50%), linear-gradient(" + sDegree2 + ", transparent 50%, " + sShadowColor + " 50%)";

    let oStyle = {
      "backgroundColor": sOuterCircleColor,
      "backgroundImage": sLinearGradient
    };

    let sClockText = Math.floor(iPercent);
    let sSectionTagLetterClassName = "mnmtSectionTagLetter ";

    return (<div className="mnmtSectionTagWrapper" style={oStyle}>
      <div className="mnmtSectionTagInnerCircle">
        <div className={sSectionTagLetterClassName}>{sClockText}</div>
      </div>
    </div>);
  }

  generateRows =(aRowData, aColumnData, oTableData)=> {
    //generate rows
    //iterate over rowData
    //first cell in each row will be rowData obj label
    //iterate over columnData and add cell for each row
    let _this = this;
    let iKey = 0;

    CS.forEach(aRowData, function (oRow) {
      let bHideEqualElements = _this.props.hideEqualElements;
      if(!(bHideEqualElements && oRow.isSkipped)) {
        let sRowId = oRow.id;
        let aLeftFixedSectionRowCells = [];
        let aScrollableSectionRowCells = [];
        let oRowStyle = {
          height: "auto"
        };
        let sImageSrc = ViewUtils.getIconUrl(oRow.iconKey);

        //first column with all the labels
        aLeftFixedSectionRowCells.push(
            <div className="mnmtTableCell mnmtFirstColumnCell"
                 key={iKey++}
                 style={{width: _this.props.firstColumnWidth + "px"}}>
              <div className="customIcon">
                <ImageFitToContainerView imageSrc={sImageSrc}/>
              </div>
              <span className="mnmtTableCellLabel" title={CS.getLabelOrCode(oRow)} >{CS.getLabelOrCode(oRow)}</span>
            </div>
        );

        let oComparisonColumn = CS.find(aColumnData, {forComparison : true});

        CS.forEach(aColumnData, function (oColumn) {
          let oCellStyle = {};
          let iColumnWidth = (oColumn.width ? oColumn.width : iColumnWidth);
          oCellStyle.width = iColumnWidth + "px";
          let oProperty = oColumn.properties[sRowId] || {};
          let bIsForRange = oColumn.isForRange;
          let sClassName = "mnmtCircularContentContainer";
          let iValue = oProperty.value;
          let oCellContent = null;
          let oComparisonProperty = null;
          let bShouldChangeValue = false;

          if(oComparisonColumn && !_this.props.isInstancesComparisonView){
            oComparisonProperty = oComparisonColumn.properties[sRowId];
            bShouldChangeValue = (oComparisonProperty.value != oProperty.value) && !oColumn.isFixed;
          }

          if (bIsForRange && iValue !== 0) { //for range tag type show three dots instead of '0' value
            oCellContent = _this.getClockPercentView(iValue);
          }
          else {
            if (iValue > 0) {
              sClassName += " mnmtCheckedCircle";
            } else if (iValue < 0) {
              sClassName += " mnmtCrossedCircle";
            } else {
              sClassName += " mnmtNeutral";
            }
            oCellContent = (<div className={sClassName}></div>);
          }

          let sTagCellClassName = "mnmtCellContentContainer";
          let sCellClassName = "mnmtTableCell";

          if(!oColumn.isFixed){
            sTagCellClassName = _this.props.isInstancesComparisonView ? sTagCellClassName : sTagCellClassName + " mnmtClickableTagCell";
            sCellClassName = sCellClassName + " mnmtScrollableCell";
          }

          if(_this.props.isInstancesComparisonView) {
            oColumn.forComparison && (sCellClassName += " forComparison");
          }

          if (_this.props.isGoldenRecordComparison && oColumn.isRecommended) {
            sCellClassName += " isRecommended";
          }

          let fCellOnClick = !oRow.isDisabled ? _this.handleCellOnClick.bind(_this, bShouldChangeValue, sRowId, oProperty, oColumn.id) : null;

          if (_this.props.isGoldenRecordComparison && oColumn.isDisabled) {
            fCellOnClick = null;
            sCellClassName += " isDisabled";
          }

          let oCell = (
              <div className={sCellClassName} style={oCellStyle} key={iKey++}>
                <div className={sTagCellClassName} onClick={fCellOnClick}>
                  {oCellContent}
                </div>
              </div>);

          if (oColumn.isFixed) {
            aLeftFixedSectionRowCells.push(oCell);
          } else {
            aScrollableSectionRowCells.push(oCell);
          }

        });

        oTableData.leftFixedSectionRows.push(
            <div className="mnmtTableRow"
                 style={oRowStyle}
                 key={iKey++}>
              {aLeftFixedSectionRowCells}
            </div>
        );

        oTableData.scrollableSectionRows.push(
            <div className="mnmtTableRow mnmtScrollableRow"
                 style={oRowStyle}
                 key={iKey++}>
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
    let aLeftFixedSectionRowCells = [];
    let aScrollableSectionRowCells = [];
    let _this = this;
    let bShowHeader = this.props.showHeader;

    if(bShowHeader) {
      //first column with all the labels
      aLeftFixedSectionRowCells.push(
          <div className="mnmtTableCell mnmtHeaderCell" style={{width: this.props.firstColumnWidth + "px"}}></div>
      );
    }
    oTableData.leftFixedSectionWidth += this.props.firstColumnWidth;

    CS.forEach(aColumnData, function (oColumn) {
      let oCellStyle = {};
      let iColumnWidth = (oColumn.width ? oColumn.width : iColumnWidth);
      oCellStyle.width = iColumnWidth + "px";

      let sCellClassName = "mnmtTableCell mnmtHeaderCell";

      if(!oColumn.isFixed){
        sCellClassName = sCellClassName + " mnmtScrollableCell";
        _this.scrollableColumn++;
      }

      //let sColumnId = oColumn.id;
      //let fOnColumnHeaderClick = (_this.props.isInstancesComparisonView && !oColumn.isFixed) ? _this.handleColumnHeaderClicked.bind(_this, sColumnId) : null;
      let aActionButtonDom = [];
      CS.forEach(oColumn.actionItems, function (oActionItems) {
        aActionButtonDom.push(<TooltipView label={CS.getLabelOrCode(oActionItems)}>
          <div className={oActionItems.className}
               onClick={_this.handleRollbackVersionButtonClicked.bind(_this, oActionItems.id, oColumn.id)}>
          </div>
        </TooltipView>);
      });

      let oCell = bShowHeader ? (
          <div className={sCellClassName}
               style={oCellStyle}>{CS.getLabelOrCode(oColumn)}
               {aActionButtonDom}
          </div>) : null;

      if (oColumn.isFixed) {
        aLeftFixedSectionRowCells.push(oCell);
        oTableData.leftFixedSectionWidth += iColumnWidth;
      } else {
        aScrollableSectionRowCells.push(oCell);
        oTableData.scrollableSheetWidth += iColumnWidth;
      }
    });

    if(bShowHeader) {
      let sViewContext = _this.state.viewContext;
      oTableData.leftFixedSectionRows.push(
          <div className={"mnmtTableRow mnmtHeaderRow " + sViewContext}>
            {aLeftFixedSectionRowCells}
          </div>
      );

      oTableData.scrollableSectionRows.push(
          <div className={"mnmtTableRow mnmtHeaderRow mnmtScrollableRow " + sViewContext}
               style={{width: oTableData.scrollableSheetWidth + "px"}}>
            {aScrollableSectionRowCells}
          </div>
      );
    }
  }


  render() {

    let aRowData = this.props.rowData;
    let aColumnData = this.props.columnData;

    this.scrollableColumn = 0;

    let oTableData = {
      leftFixedSectionRows: [],
      scrollableSectionRows: [],
      leftFixedSectionWidth: 0,
      scrollableSheetWidth: 0
    };

    this.generateHeaderRow(aRowData, aColumnData, oTableData);
    this.generateRows(aRowData, aColumnData, oTableData);

    return (
        <div className="matchAndMergeTagView">

          <div className="mnmtLeftFixedSection leftFixedSection" ref={this.leftFixedSection}>
            <div className="mnmtTableSection">
              {oTableData.leftFixedSectionRows}
            </div>
          </div>

          <div className="mnmtScrollableSection mnmScroll" onScroll={this.handleScrollableSectionScrolled}>
            <div className="mnmtTableSection mnmChildTableSection">
              {oTableData.scrollableSectionRows}
            </div>
          </div>

        </div>
    );
  }

}

MatchAndMergeTagView.propTypes = oPropTypes;

MatchAndMergeTagView.defaultProps = {
  rowData: [],
  columnData: [],
  firstColumnWidth: iColumnWidth
}

export const view = MatchAndMergeTagView;
export const events = oEvents;
