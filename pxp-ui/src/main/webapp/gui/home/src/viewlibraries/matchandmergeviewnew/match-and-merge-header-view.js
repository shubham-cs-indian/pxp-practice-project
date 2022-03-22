import CS from '../../libraries/cs';
import MasterUserListContext from '../../commonmodule/HOC/master-user-list-context';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import UniqueIdentifierGenerator from '../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import ViewLibraryUtils from '../../viewlibraries/utils/view-library-utils';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import TooltipView from './../tooltipview/tooltip-view';
import { view as MatchAndMergeCommonCellView } from './match-and-merge-common-cell-view';

const oEvents = {
  MATCH_AND_MERGE_HEADER_CELL_CLICKED: "match_and_merge_header_cell_clicked",
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
  isGoldenRecordComparison: ReactPropTypes.bool,
  masterUserList: ReactPropTypes.array
};
/**
 * @class MatchAndMergeHeaderView - Use to Display header of comparision product view.
 * @memberOf Views
 * @property {array} [rowData] - Pass array of id, label, type, isDisabled, rendererType.
 * @property {array} [columnData] - Pass array of column data like createdBy, createdOn, id , label, type, width etc.
 * @property {number} [firstColumnWidth] - Pass 250 width default for first column.
 * @property {string} [tableId] - pass table Id.
 * @property {string} [tableGroupName] - Pass table Group Name.
 * @property {object} [referencedAssetsData] - Deprecated.
 * @property {bool} [isInstancesComparisonView] - Pass bool value for instance comparison view.
 * @property {bool} [isGoldenRecordComparison] - Deprecated.
 */

const iColumnWidth = 250;

// @MasterUserListContext
// @CS.SafeComponent
class MatchAndMergeHeaderView extends React.Component {

  constructor(props) {
    super(props);

    let sViewContextUUID = UniqueIdentifierGenerator.generateUUID();
    this.state = {
      viewContext: sViewContextUUID
    }

    this.leftFixedSection = React.createRef();
  }

  handleColumnHeaderClicked =(sColumnId, oEvent)=> {
    if(!oEvent.nativeEvent.dontRaise) {
      EventBus.dispatch(oEvents.MATCH_AND_MERGE_HEADER_CELL_CLICKED, sColumnId);
    }
  };

  handleRollbackVersionButtonClicked =(sActionItem , sVersionId)=>{
    EventBus.dispatch(oEvents.MATCH_AND_MERGE_VERSION_ROLLBACK_BUTTON_CLICKED, sActionItem, sVersionId);
  };

  getImageView =(oRow, oColumn, oProperty)=> {
    let sRowId = oRow.id;
    let sTableId = this.props.tableId;
    let sRendererType  = oRow.rendererType;
    let sTableGroupName = this.props.tableGroupName;
    let oComparisonProperty = null;
    if(this.comparisonColumn && this.comparisonColumn.id !== oColumn.id) {
      oComparisonProperty = this.comparisonColumn.properties[sRowId] || {};
    }

    switch (oRow.type) {

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
                hideComparison={true}
                isInstancesComparisonView={this.props.isInstancesComparisonView}
            />
        );
    }
  }

  generateRows =(aRowData, aColumnData, oTableData)=> {
    var aUserList = this.props.masterUserList;

    //generate rows
    //iterate over rowData
    //iterate over columnData and add cell for each row
    let _this = this;
    let iKey = 0;

    CS.forEach(aRowData, function (oRow, iRowIndex) {
      let sRowId = oRow.id;
      let sRendererType = oRow.rendererType;
      let aLeftFixedSectionRowCells = [];
      let aScrollableSectionRowCells = [];

      //first empty column
      aLeftFixedSectionRowCells.push(
          <div className="mnmTableCell firstColumnCell" key={iKey++} title={CS.getLabelOrCode(oRow)}>
          </div>
      );

      CS.forEach(aColumnData, function (oColumn) {
        let oCellStyle = {};
        let aVersionInfo = [];
        let sCellClassName = "mnmTableCell";
        let fOnColumnHeaderClick = null;
        let aActionButtonDom = [];
        let oProperty = {};
        let sType = oColumn.type;

        if (!oColumn.isGoldenRecord || !CS.isEmpty(oColumn.id)) {


          let oCreatedBy = oColumn.createdBy ? ViewLibraryUtils.getUserByUsername(oColumn.createdBy, aUserList) : null;
          let sCreatedBy = !CS.isEmpty(oCreatedBy) ? (oCreatedBy.firstName + " " + oCreatedBy.lastName) : "";
          let oCreatedOn = oColumn.createdOn ? ViewLibraryUtils.getDateAttributeInDateTimeFormat(oColumn.createdOn) : null;
          let oLastModifiedBy = oColumn.lastModifiedBy ? ViewLibraryUtils.getUserById(oColumn.lastModifiedBy,aUserList): null;
          let sLastModifiedBy = !CS.isEmpty(oLastModifiedBy) ? (oLastModifiedBy.firstName + " " + oLastModifiedBy.lastName) : "";
          let oLastModifiedOn = oColumn.lastModified ? ViewLibraryUtils.getDateAttributeInDateTimeFormat(oColumn.lastModified) : null;

          let iColumnWidth = (oColumn.width ? oColumn.width : iColumnWidth);
          oCellStyle.width =  iColumnWidth + "px";
          oProperty = oColumn.properties[sRowId] || {};


          if(sRendererType === "image" || oRow.rendererType === "coverflow")  {
            sCellClassName += " image"
          }
          if(!oColumn.isFixed){
            sCellClassName = sCellClassName + " mnmScrollableCell";
          }

          /*if(_this.props.isInstancesComparisonView) {
            oColumn.forComparison && (sCellClassName += " forComparison");
          }*/
          oColumn.forComparison && (sCellClassName += " forComparison");

          CS.forEach(oColumn.actionItems, function (oActionItems) {
            aActionButtonDom.push(
                <TooltipView label={CS.getLabelOrCode(oActionItems)} key={oActionItems.id}>
                  <div className={oActionItems.className}
                       onClick={_this.handleRollbackVersionButtonClicked.bind(_this, oActionItems.id, oColumn.id)}>
                  </div>
                </TooltipView>
            );
          });

          fOnColumnHeaderClick = (_this.props.isInstancesComparisonView && !oColumn.isFixed) ?
              _this.handleColumnHeaderClicked.bind(_this, oColumn.id) : null;
          let oLabelTooltipDom = {};

          if(sCreatedBy && oCreatedOn) {
            oLabelTooltipDom = (<div style={{maxWidth: 210}} className="maxWidthTooltip">{sCreatedBy}</div>);
            aVersionInfo.push(
                <div className="createdByContainer">
                  <TooltipView label={oLabelTooltipDom}>
                    <div className="createdByInformation">
                      <div className="normal">{getTranslation().CREATED_BY}</div>
                      <div className="highlighted">{sCreatedBy}</div>
                    </div>
                  </TooltipView>
                  <div className="createdOnInformation">
                    {/*<span className="normal on">{getTranslation().ON}</span>*/}
                    <div className="highlighted">{oCreatedOn.date}</div>
                    {/*<span className="normal">{getTranslation().AT}</span>*/}
                    <div className="highlighted">{oCreatedOn.time}</div></div>
                </div>
            );
          }
          if(sLastModifiedBy && oLastModifiedOn) {
            oLabelTooltipDom = (<div style={{maxWidth: 210}} className="maxWidthTooltip">{sLastModifiedBy}</div>);
            aVersionInfo.push(
                <div className="createdOnContainer">
                  <TooltipView label={oLabelTooltipDom}>
                    <div className="lastModifiedByInformation">
                      <div className="normal">{getTranslation().LAST_MODIFIED_BY}</div>
                      <div className="highlighted">{sLastModifiedBy}</div>
                    </div>
                  </TooltipView>
                  <div className="lastModifiedOnInformation">
                    {/*<span className="normal on">{getTranslation().ON}</span>*/}
                    <div className="highlighted">{oLastModifiedOn.date}</div>
                    {/*<span className="normal">{getTranslation().AT}</span>*/}
                    <div className="highlighted">{oLastModifiedOn.time}</div>
                  </div>
                </div>
            );
          }
        }

        let sGoldebRecordLabel, sVariantOfLabel, sCloneOfLabel, sGoldebRecordClassName, sVariantOfClassName, sCloneOfClassName;
        sGoldebRecordClassName = sVariantOfClassName = sCloneOfClassName = "columnIconType ";

        if(CS.includes(sType, "goldenRecordIndicator")){
          sGoldebRecordLabel = getTranslation().GOLDEN_RECORD;
          sGoldebRecordClassName += "goldenRecordIndicator";
        }
        if (CS.includes(sType, "variantOfContainer")) {
          sVariantOfLabel = getTranslation().VARIANT;
          sVariantOfClassName += "variantOfContainer";
        }
        if (CS.includes(sType, "cloneOfContainer")) {
          sCloneOfLabel = getTranslation().CLONE;
          sCloneOfClassName += "cloneOfContainer";
        }

        let oExclamationView = !oColumn.isEntityAvailableInDL ? (
            <div className="languageUnavailableContainer">
              <div className="exclamationMark"></div>
              <div className="languageUnavailableLabel">{getTranslation().LANGUAGE_UNAVAILABLE}</div>
            </div>
        ) : null;

        let oCell = (
            <div className={sCellClassName}
                 key={iKey++}
                 onClick={fOnColumnHeaderClick}
                 style={oCellStyle}>
              {aActionButtonDom}
              <div className="columnImage">
                {oColumn.properties && oColumn.properties[sRendererType].value ? _this.getImageView(oRow, oColumn, oProperty) : null}
              </div>
              <div className="columnHeader">
                <TooltipView label={CS.getLabelOrCode(oColumn)} placement={"bottom"}>
                  <div className="headerLabel">{CS.getLabelOrCode(oColumn)}</div>
                </TooltipView>
                {aActionButtonDom}
                <div className="columnIcons">
                  {sGoldebRecordLabel && <TooltipView label={sGoldebRecordLabel}><div className={sGoldebRecordClassName}></div></TooltipView>}
                  {sVariantOfLabel && <TooltipView label={sVariantOfLabel}><div className={sVariantOfClassName}></div></TooltipView>}
                  {sCloneOfLabel && <TooltipView label={sCloneOfLabel}><div className={sCloneOfClassName}></div></TooltipView>}
                </div>
              </div>
              {aVersionInfo}
              {oExclamationView}
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
               key={iKey++}>
            {aLeftFixedSectionRowCells}
          </div>
      );

      oTableData.scrollableSectionRows.push(
          <div className="mnmTableRow mnmScrollableRow"
               key={iKey++}>
            {aScrollableSectionRowCells}
          </div>
      );

    });
  }

  generateHeaderRow =(aRowData, aColumnData, oTableData)=> {
    let _this = this;

    oTableData.leftFixedSectionWidth += this.props.firstColumnWidth;

    CS.forEach(aColumnData, function (oColumn) {
      let iColumnWidth = (oColumn.width ? oColumn.width : iColumnWidth);

      if(!oColumn.isFixed){
        _this.scrollableColumn++;
      }

      if(oColumn.forComparison) {
        _this.comparisonColumn = oColumn;
      }

      if (oColumn.isFixed) {
        oTableData.leftFixedSectionWidth += iColumnWidth;
      } else {
        oTableData.scrollableSheetWidth += iColumnWidth;
      }
    });
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
        <div className="matchAndMergeHeaderView">

          <div className="mnmLeftFixedSection leftFixedSection" ref={this.leftFixedSection}>
            <div className="mnmTableSection">
              {oTableData.leftFixedSectionRows}
            </div>
          </div>

          <div className="mnmScrollableSection mnmScroll mnmHeader" onScroll={this.handleScrollableSectionScrolled}>
            <div className="mnmTableSection mnmChildTableSection">
              {oTableData.scrollableSectionRows}
            </div>
          </div>

        </div>
    );
  }

}

MatchAndMergeHeaderView.propTypes = oPropTypes;



MatchAndMergeHeaderView.defaultProps = {
  rowData: [],
  columnData: [],
  firstColumnWidth: iColumnWidth
};

export const view = MasterUserListContext(MatchAndMergeHeaderView);
export const events = oEvents;
