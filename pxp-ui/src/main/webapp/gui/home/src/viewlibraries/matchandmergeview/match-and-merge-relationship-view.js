import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import UniqueIdentifierGenerator from '../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import ViewUtils from './../utils/view-library-utils';
import { view as ImageSimpleView } from './../imagesimpleview/image-simple-view';
import {view as CustomEntityTagsSummaryView} from './../entitytagsummaryview/custom-entity-tags-summary-view';
import { view as MediaPreviewDialogView } from '../mediapreviewdialogview/media-preview-dialog-view';
import TooltipView from './../tooltipview/tooltip-view';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';


const oEvents = {
  MATCH_MERGE_RELATIONSHIP_VIEW_CELL_CLICKED: "match_merge_relationship_view_cell_clicked",
  MATCH_MERGE_RELATIONSHIP_VIEW_CELL_REMOVE_CLICKED: "match_merge_relationship_view_cell_remove_clicked",
  MATCH_AND_MERGE_RELATIONSHIP_VERSION_ROLLBACK_BUTTON_CLICKED: "match_and_merge_relationship_version_rollback_button_clicked"
};
const oPropTypes = {
  rowData: ReactPropTypes.array,
  columnData: ReactPropTypes.array,
  firstColumnWidth: ReactPropTypes.number,
  referencedAssetsData: ReactPropTypes.object,
  tableId: ReactPropTypes.string,
  context: ReactPropTypes.string,
  isInstancesComparisonView: ReactPropTypes.bool,
  hideEqualElements: ReactPropTypes.bool,
  isGoldenRecordComparison: ReactPropTypes.bool
};
/**
 * @class MatchAndMergeRelationshipView -  Use to Display Match and merge relationship view in comparison table view.
 * @memberOf Views
 * @property {array} [rowData] - Pass array of id, label, type, isDisabled, rendererType.
 * @property {array} [columnData] - Pass array of id, label, type, isDisabled, rendererType.
 * @property {number} [firstColumnWidth] - Pass 250 width default for first column.
 * @property {string} [tableId] - Pass table id.
 * @property {object} [referencedAssetsData] - Pass referencedAsset data like image.
 * @property {string} [context] - Pass context name.
 * @property {bool} [isInstancesComparisonView] - Pass bool value for instance comparison view.
 * @property {bool} [hideEqualElements] - Pass bool value for hide equal element or not.
 * @property {bool} [isGoldenRecordComparison] - Pass boolean for is this golden record comparison or not.
 */

const iColumnWidth = 250;
// @CS.SafeComponent
class MatchAndMergeRelationshipView extends React.Component {

  constructor(props) {
    super(props);

    let sViewContextUUID = UniqueIdentifierGenerator.generateUUID();
    this.state = {
      isPopupOpen: false,
      imageData: {},
      viewContext: sViewContextUUID
    }

    this.leftFixedSection = React.createRef();
  }

  componentDidMount(){
  }

  componentDidUpdate() {
  }

  handleRollbackVersionButtonClicked = (sActionItem , sVersionId)=>{
    EventBus.dispatch(oEvents.MATCH_AND_MERGE_RELATIONSHIP_VERSION_ROLLBACK_BUTTON_CLICKED, sActionItem, sVersionId);
  };

  handleCellOnClick =(oImageData, oEvent)=> {
    let bCanViewImage = oImageData.assetObjectKey || oImageData.thumbKeySrc || oImageData.imageSrc;
    if (bCanViewImage) {
      this.setState({
        isPopupOpen: true,
        imageData: oImageData
      });
    }
  }

  handleCheckOnClick =(sRowId, oProperty, bShouldRemove, sColumnId)=> {
    let sTableId = this.props.tableId;
    let sContext = this.props.context || "";

    if(this.props.isInstancesComparisonView) {
      return;
    }

    if(bShouldRemove){
      EventBus.dispatch(oEvents.MATCH_MERGE_RELATIONSHIP_VIEW_CELL_REMOVE_CLICKED, sRowId, sTableId, sContext);
    }else{
      EventBus.dispatch(oEvents.MATCH_MERGE_RELATIONSHIP_VIEW_CELL_CLICKED, sRowId, oProperty, sTableId, sContext, sColumnId);
    }
  }

  closeMediaPreviewPopView =()=> {
    this.setState({
      isPopupOpen: false,
      imageData: {}
    });
  }

  getContextTagView = (aTags) => {
    return (
        <div className={"contextTagsContainer"}>
          <CustomEntityTagsSummaryView tags={aTags}
                                       showMoreButton={true}
                                       isCrossIconEnabled={false}
          />
        </div>
    )
  };

  generateRows =(aRowData, aColumnData, oTableData)=> {
    //generate rows
    //iterate over rowData
    //first cell in each row will be rowData obj label
    //iterate over columnData and add cell for each row
    let _this = this;
    let oReferencedAssets = this.props.referencedAssetsData;
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

        //first column with all the labels
        aLeftFixedSectionRowCells.push(
            <div className="mnmrTableCell mnmrFirstColumnCell"
                 key={iKey++}
                 style={{width: _this.props.firstColumnWidth + "px"}}>
              <span className="mnmrTableCellLabel" title={CS.getLabelOrCode(oRow)}>{CS.getLabelOrCode(oRow)}</span>
            </div>
        );

        let oComparisonColumn = CS.find(aColumnData, {forComparison : true});

        CS.forEach(aColumnData, function (oColumn) {
          let oCellStyle = {};
          let iColumnWidth = (oColumn.width ? oColumn.width : iColumnWidth);
          oCellStyle.width = iColumnWidth + "px";
          let oProperty = oColumn.properties[sRowId] || {};
          let oImageView = null;
          let sCheckIconClassName = "mnmrCellIcon";
          let oCell = null;
          let oComparisonProperty = null;

          if(oComparisonColumn){
            oComparisonProperty = oComparisonColumn.properties[sRowId];
          }

          let sCellClassName = "mnmrTableCell";

          if(!oColumn.isFixed){
            sCellClassName = sCellClassName + " mnmrScrollableCell";
          }

          if(_this.props.isInstancesComparisonView) {
            oColumn.forComparison && (sCellClassName += " forComparison");
          }

          if (_this.props.isGoldenRecordComparison && oProperty.isRecommended) {
            sCellClassName += " isRecommended";
          }

          if (!CS.isEmpty(oProperty.value)) {

            let sValue = oProperty.value;
            let oImageData = oReferencedAssets[sValue];
            let sComparisonValue = null;
            let bShouldRemove = false;

            if(oComparisonProperty){
              sComparisonValue = oComparisonProperty.value;
            }

            let oRemoveIconDOM = null;
            let oCellClickHandler = null;

            let bCanViewImage = oImageData.assetObjectKey || oImageData.thumbKeySrc || oImageData.imageSrc;
            let oTimelineImagePreviewButton = bCanViewImage ? (
                <TooltipView label={getTranslation().VIEW} placement="top">
                  <div className="timelinePreviewButton" onClick={_this.handleCellOnClick.bind(_this, oImageData)}/>
                </TooltipView>
            ) : null;

            let bValuesAreSame = (sComparisonValue == sValue) && CS.isEqual(oComparisonProperty.tags, oProperty.tags); //check for tags required in case of relationships with context
            if (!_this.props.isGoldenRecordComparison && (oColumn.isFixed) && !CS.isEmpty(sValue) && oRow.canDelete) {
              sCheckIconClassName = sCheckIconClassName + " mnmrShowRemoveIcon";
              bShouldRemove = true;
              oRemoveIconDOM = (
                  <div className={sCheckIconClassName}
                       onClick={_this.handleCheckOnClick.bind(_this, sRowId, oProperty, bShouldRemove, oColumn.id)}>
                  </div>
              );
            } else if(oColumn.isFixed || (!_this.props.isGoldenRecordComparison && (CS.isEmpty(oComparisonColumn) ||(bValuesAreSame) || !oRow.canAdd))){//TODO: Refactor this check
              //sCheckIconClassName = sCheckIconClassName + " mnmrHideIcon";
              oRemoveIconDOM = null;
              //oTimelineImagePreviewButton = null;
            } else {
              oCellClickHandler = _this.handleCheckOnClick.bind(_this, sRowId, oProperty, bShouldRemove, oColumn.id);
            }

            oImageView = CS.isEmpty(oImageData) ?
                         (<div className="mnmrBlankImageContainer">
                           <div className="mnmrBlankImage"></div>
                         </div>) :
                         (<div className="mnmrImageContainer" onClick={oCellClickHandler}>
                           <ImageSimpleView imageSrc={oImageData.thumbKeySrc} classLabel="mnmrIcon"/>
                         </div>);

            let oContextTagView = null;
            if (!CS.isEmpty(oProperty.tags)) {
              oContextTagView = _this.getContextTagView(oProperty.tags);
                 // <CircularTagListView tags={oProperty.tags}/>
            }

            let oTimeRangeView = null;
            let oTimeRange = oProperty.timeRange;
            if (!CS.isEmpty(oTimeRange)) {
              let sFrom = oTimeRange.from;
              let sTo = oTimeRange.to;
              if (sFrom && sTo) {
                let sTimeRange = ViewUtils.getShortDate(sFrom) + " - " + ViewUtils.getShortDate(sTo);
                oTimeRangeView = (
                    <div className="contextTimeRange" title={sTimeRange}>{sTimeRange}</div>
                );
              }
            }

            let oActionItemDOM = (
                <div className="mnmRelationshipActionItemContainer">
                  {oRemoveIconDOM}
                  {oTimelineImagePreviewButton}
                </div>
            );

            oCell = (
                <div className={sCellClassName} style={oCellStyle} key={iKey++}>
                  {oActionItemDOM}
                  <div className="mnmrCellContentContainer">
                    {oImageView}
                    {oTimeRangeView}
                    {oContextTagView}
                  </div>
                </div>
            );

          } else {

            if (oColumn.isFixed) {

              var sClassName = _this.props.isInstancesComparisonView ? "instanceComparisonNoImage" : "mnmrBlankImage timelineComparison";
              oCell = (<div className={sCellClassName} key={iKey++} style={oCellStyle}>
                <div className="mnmrCellContentContainer">
                  <div className="mnmrCellCheckIcon mnmrHideCheckIcon"></div>
                  <div className={sClassName}></div>
                </div>
              </div>);

            }else{
              oCell = (<div className={sCellClassName} key={iKey++} style={oCellStyle}></div>);
            }

          }


          if (oColumn.isFixed) {
            aLeftFixedSectionRowCells.push(oCell);
          } else {
            aScrollableSectionRowCells.push(oCell);
          }

        });

        oTableData.leftFixedSectionRows.push(
            <div className="mnmrTableRow"
                 key={iKey++}
                 style={oRowStyle}>
              {aLeftFixedSectionRowCells}
            </div>
        );

        oTableData.scrollableSectionRows.push(
            <div className="mnmrTableRow mnmrScrollableRow"
                 key={iKey++}
                 style={oRowStyle}>
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
          <div className="mnmrTableCell mnmrHeaderCell" style={{width: this.props.firstColumnWidth + "px"}}></div>
      );
    }

    oTableData.leftFixedSectionWidth += this.props.firstColumnWidth;

    CS.forEach(aColumnData, function (oColumn) {
      let oCellStyle = {};
      let iColumnWidth = (oColumn.width ? oColumn.width : iColumnWidth);
      oCellStyle.width = iColumnWidth + "px";

      let sCellClassName = "mnmrTableCell mnmrHeaderCell";

      if(!oColumn.isFixed){
        sCellClassName = sCellClassName + " mnmrScrollableCell";
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
      let sViewContext = _this.state.viewContext || "";
      oTableData.leftFixedSectionRows.push(
          <div className={"mnmrTableRow mnmrHeaderRow " + sViewContext}>
            {aLeftFixedSectionRowCells}
          </div>
      );

      oTableData.scrollableSectionRows.push(
          <div className={"mnmrTableRow mnmrHeaderRow mnmrScrollableRow " + sViewContext}
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

    let sTitle = this.props.title;
    let oTitleView = null;
    if (sTitle) {
      oTitleView = (
          <div className="matchAndMergeViewTitle">
            {sTitle + " :"}
          </div>
      );
    }

    let bShowPreview = this.state.isPopupOpen;
    let oImageData = this.state.imageData;

    //todo: need to add oTitleView

    return (
        <div className="matchAndMergeRelationshipView">

          {<MediaPreviewDialogView mediaData={oImageData}
                                   showPreview={bShowPreview}
                                   title={sTitle}
                                   onClose={this.closeMediaPreviewPopView}/>}

          {oTitleView}

          <div className="matchAndMergeRelationshipTableContainer">

            <div className="mnmrLeftFixedSection leftFixedSection" ref={this.leftFixedSection}>
              <div className="mnmrTableSection">
                {oTableData.leftFixedSectionRows}
              </div>
            </div>

            <div className="mnmrScrollableSection mnmScroll"
                 onScroll={this.handleScrollableSectionScrolled}>
              <div className="mnmrTableSection mnmChildTableSection">
                {oTableData.scrollableSectionRows}
              </div>
            </div>

          </div>

        </div>
    );
  }

}

MatchAndMergeRelationshipView.propTypes = oPropTypes;

MatchAndMergeRelationshipView.defaultProps = {
  rowData: [],
  columnData: [],
  referencedAssetsData: {},
  firstColumnWidth: iColumnWidth
}


export const view = MatchAndMergeRelationshipView;
export const events = oEvents;
