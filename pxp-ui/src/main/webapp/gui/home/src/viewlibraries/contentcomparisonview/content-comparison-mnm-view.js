import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { view as ContentComparisonMatchAndMergePropertyView } from './content-comparison-mnm-property-view';
import ContentComparisonMatchAndMergePropertyDetailView from './content-comparison-mnm-property-detailed-view';
import {view as ImageFitToContainerView} from "../imagefittocontainerview/image-fit-to-container-view";
import ViewLibraryUtils from "../utils/view-library-utils";

var oEvents = {
  CONTENT_COMPARISON_MATCH_AND_MERGE_VIEW_TABLE_ROW_CLICKED: "content_comparison_match_and_merge_view_table_row_clicked",
  CONTENT_COMPARISON_MATCH_AND_MERGE_PROPERTY_VALUE_CHANGED: "content_comparison_match_and_merge_property_value_changed"
};

const oPropTypes = {
  context: ReactPropTypes.string,
  activePropertyDetailedData: ReactPropTypes.object,
  contentComparisonMatchAndMergeData: ReactPropTypes.object,
  containerStyle: ReactPropTypes.object
};

// @CS.SafeComponent
class ContentComparisonMnmView extends React.Component {
  constructor (props) {
    super(props);
  }

  static propTypes = oPropTypes;

  handleTableRowClicked = function (sPropertyId, sTableId, oEvent) {
    let sContext = this.props.context;
    let oMatchAndMergeData = this.props.contentComparisonMatchAndMergeData;
    let sSelectedRowId = oMatchAndMergeData.selectedRowId;
    if (sSelectedRowId !== sPropertyId && sTableId !== "fixedTable") {
      EventBus.dispatch(oEvents.CONTENT_COMPARISON_MATCH_AND_MERGE_VIEW_TABLE_ROW_CLICKED, sContext, sPropertyId, sTableId);
    }
  };

  handleMatchAndMergeViewPropertyValueChanged = (sTableId, sPropertyId, oValue) => {
    let sContext = this.props.context;
    EventBus.dispatch(oEvents.CONTENT_COMPARISON_MATCH_AND_MERGE_PROPERTY_VALUE_CHANGED, sContext, sPropertyId, sTableId, oValue);
  };


  getRightSectionView = function () {
    let oMatchAndMergeData = this.props.contentComparisonMatchAndMergeData;
    let sSelectedRowId = oMatchAndMergeData.selectedRowId;
    let sSelectedTableId = oMatchAndMergeData.selectedTableId;
    let oContentComparisonMatchAndMergeActivePropertyDetailedData = this.props.activePropertyDetailedData;

    if (CS.isEmpty(oContentComparisonMatchAndMergeActivePropertyDetailedData) || CS.isEmpty(sSelectedRowId)) {
      return null;
    }

    let _this = this;
    return (
        <div className="mnmRightSection">
          <div className="mnmRightSectionColumnFlexParent">
            <div className="mnmRightSectionColumnFlexChild">
             {/* {_this.getSectionHeaderView("Detailed")}*/}
              <ContentComparisonMatchAndMergePropertyDetailView
                  propertyDetailedData={oContentComparisonMatchAndMergeActivePropertyDetailedData}
                  selectedTableId={sSelectedTableId}
                  selectedPropertyId={sSelectedRowId}
                  emptyElementMsgKey={"RELATIONSHIP_INSTANCE_NOT_FOUND"}
                  context={this.props.context}
                  onChangeHandler={_this.handleMatchAndMergeViewPropertyValueChanged.bind(this, sSelectedTableId, sSelectedRowId)}
              />
            </div>
          </div>
        </div>
    );
  };

  getTableRowValueView = (oRowData, sTableId, bHideEditRowButton) => {
    let oMatchAndMergeData = this.props.contentComparisonMatchAndMergeData;
    let sTableRowValueFieldClassName = "mnmTableRowValue ";
    let sSelectedTableId = oMatchAndMergeData.selectedTableId;
    let sSelectedRowId = oMatchAndMergeData.selectedRowId;
    let sEditableRowId = oMatchAndMergeData.editableRowId;
    if (sSelectedTableId === sTableId && sSelectedRowId === oRowData.id) {
      sTableRowValueFieldClassName += "selected";
    }

    return (
        <div className={sTableRowValueFieldClassName}
             onClick={this.handleTableRowClicked.bind(this, oRowData.id, sTableId)}>
          <ContentComparisonMatchAndMergePropertyView
              property={oRowData}
              context={this.props.context}
              tableId={sTableId}
              selectedTableId={sSelectedTableId}
              selectedRowId={sSelectedRowId}
              editableRowId={sEditableRowId}
              selectedLanguageIds={this.props.selectedLanguageIds}
              hideEditRowButton={bHideEditRowButton}
              onChangeHandler={this.handleMatchAndMergeViewPropertyValueChanged.bind(this, sTableId, oRowData.id)}
          />
        </div>
    );
  };

  getTableHeaderView = function (sTableId) {
    return (
        <div className="mnmTableHeader">
          {sTableId}
        </div>
    );
  };

  generateRow = (oRowData, sTableId, bHideEditRowButton) => {
    let aRowView = [];
    if(oRowData.propertyType === "attribute" || oRowData.propertyType === "tag") {
      let sImageSrc = ViewLibraryUtils.getIconUrl(oRowData.iconKey);
      aRowView.push(<div className="customIcon"><ImageFitToContainerView imageSrc={sImageSrc}/></div>);
    }
    aRowView.push(<div className="mnmTableRowLabel">{oRowData.label}</div>);
    aRowView.push(this.getTableRowValueView(oRowData, sTableId, bHideEditRowButton));
    return (
        <div className="mnmTableRow">
          {aRowView}
        </div>);
  };

  generateTables = function () {
    let _this = this;
    let oMatchAndMergeData = this.props.contentComparisonMatchAndMergeData;
    let aTableIds = oMatchAndMergeData.tableIds;
    let aTableData = oMatchAndMergeData.tableData;
    let aTableViews = [];
    CS.forEach(aTableIds, function (sTableId, index) {
      let aTableRowView = [];
      let oTableData = aTableData[sTableId];
      let aRowData = oTableData.rowData;
      let bHideEditRowButton = oTableData.hideEditRowButton;

      CS.forEach(aRowData, function (oData) {
        aTableRowView.push(_this.generateRow(oData, sTableId, bHideEditRowButton));
      });

      aTableViews.push(
          <div className="mnmTableView">
            {_this.getTableHeaderView(oTableData.tableHeaderLabel)}
            <div className="mnmTableRowContainer">{aTableRowView}</div>
          </div>
      );
    });
    let {contentComparisonMatchAndMergeActivePropertyDetailedData} = _this.props;
    let hasValue = contentComparisonMatchAndMergeActivePropertyDetailedData && Object.keys(contentComparisonMatchAndMergeActivePropertyDetailedData).length;


    return (
        <div className={`mnmTablesContainer ${hasValue && "mnmSelectedContainer"}`}>
          {aTableViews}
        </div>
    );
  };

  getLeftSectionView = function () {
    let _this = this;
    let {contentComparisonMatchAndMergeActivePropertyDetailedData} = _this.props;
    let hasValue = contentComparisonMatchAndMergeActivePropertyDetailedData && Object.keys(contentComparisonMatchAndMergeActivePropertyDetailedData).length
    return (
        <div className={`mnmLeftSection ${hasValue ? "mnmLeftConditional" : "mnmLeftConditionalSingle"}`}>
         {/* {_this.getSectionHeaderView(getTranslation().GOLDEN_RECORD)}*/}
          {_this.generateTables()}
        </div>
    )
  };

/*  getSectionHeaderView = (sLabel) => {
    return (
        <div className="mnmSectionHeader">
          <div className="mnmSectionHeaderLabel">
            {sLabel}
          </div>
        </div>
    );
  };*/

  render () {
    return (
          <div className="contentComparisonMNMContainer" style={this.props.containerStyle}>
            {this.getLeftSectionView()}
            {this.getRightSectionView()}
          </div>
    );
  }
}

export const view = ContentComparisonMnmView;
export const events = oEvents;
