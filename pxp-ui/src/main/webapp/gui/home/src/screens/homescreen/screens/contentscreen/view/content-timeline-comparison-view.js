import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactDom from 'react-dom';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';
import { view as ExpandableView } from '../../../../../viewlibraries/expandableview/expandable-view';
import { view as MatchAndMergeView } from './../../../../../viewlibraries/matchandmergeviewnew/match-and-merge-view';
import { view as MatchAndMergeHeaderView } from './../../../../../viewlibraries/matchandmergeviewnew/match-and-merge-header-view';
import { view as MatchAndMergeRelationshipView } from './../../../../../viewlibraries/matchandmergeview/match-and-merge-relationship-view';
import { view as MatchAndMergeTagView } from './../../../../../viewlibraries/matchandmergeview/match-and-merge-tag-view';
import TooltipView from './../../../../../viewlibraries/tooltipview/tooltip-view';
import ComparisonViewConstants from './../../../../../commonmodule/tack/comparison-view-table-constants';
import { view as ImageFitToContainerView } from '../../../../../viewlibraries/imagefittocontainerview/image-fit-to-container-view';
import { view as ToolbarView } from '../../../../../viewlibraries/toolbarview/toolbar-view';
import { view as CustomMaterialButtonView } from '../../../../../viewlibraries/custommaterialbuttonview/custom-material-button-view';
import ViewUtils from './utils/view-utils';
import { view as ContextMenuViewNew } from '../../../../../viewlibraries/contextmenuwithsearchview/context-menu-view-new';
import ContextMenuViewModel from '../../../../../viewlibraries/contextmenuwithsearchview/model/context-menu-view-model';

var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
  COMPARISON_VIEW_LANGUAGE_FOR_COMPARISON_CHANGED: "comparison_view_language_for_comparison_changed",
  CONTENT_TIMELINE_COMPARISON_BUTTON_CLICKED: "content_timeline_comparison_button_clicked",
};

const oPropTypes = {
  timelineData: ReactPropTypes.object,
  showSaveDiscardButtons: ReactPropTypes.bool
};
const iColumnWidth = 250;
// @CS.SafeComponent
class ContentTimelineComparisonView extends React.Component {
  constructor(props) {
    super(props);

    this.scrollIconLeft = React.createRef();
    this.scrollIconRight = React.createRef();
    this.comparisonBody = React.createRef();
  }

  static propTypes = oPropTypes;

  state = {
    showScrollTopButton: false,
    hideEqualElements: false,
  };

  componentDidMount() {
    this.handleScrollIconsVisibility();
  }

  componentDidUpdate() {
    this.handleScrollIconsVisibility();
  }

  handleContentTimelineComparisonButtonClicked =(sButtonId)=> {
   EventBus.dispatch(oEvents.CONTENT_TIMELINE_COMPARISON_BUTTON_CLICKED, sButtonId);
  };

  handleScrollIconsVisibility = () => {
    var bIsScrollable = this.isScrollable();
    var oScrollIconDOMLeft = this.scrollIconLeft.current;
    var oScrollIconDOMRight = this.scrollIconRight.current;
    if(oScrollIconDOMLeft && oScrollIconDOMRight) {
      if(bIsScrollable) {
        oScrollIconDOMLeft.classList.remove('hideScrollIcon');
        oScrollIconDOMRight.classList.remove('hideScrollIcon');
      } else {
        oScrollIconDOMLeft.classList.add('hideScrollIcon');
        oScrollIconDOMRight.classList.add('hideScrollIcon');
      }
    }
  };

  getDOMsFromThisView = () => {
      let oChildDOM = ReactDom.findDOMNode(this);
      let oScrollableDOM = oChildDOM.getElementsByClassName("mnmScroll");
      let oScrollableContainer = oChildDOM.getElementsByClassName("mnmChildTableSection");
      return {oChildDOM, oScrollableDOM, oScrollableContainer}
  };

  handleScrollIconClicked = (sScrollPosition) => {
      let {oScrollableDOM, oScrollableContainer} = this.getDOMsFromThisView();
      let oContainerWidth = oScrollableContainer[0].offsetWidth;
      CS.forEach(oScrollableDOM, (oDOM) => {
          let iNewScrollLeft = oDOM.scrollLeft;
          let bIsScrollable = false;
          let iVisibleWidthCount = oDOM.clientWidth;
          if (sScrollPosition === "moveLeft" && iNewScrollLeft != 0) {
              iNewScrollLeft = -iColumnWidth;
              bIsScrollable = true;
          } else if (sScrollPosition === "moveRight" && (iNewScrollLeft + iVisibleWidthCount) <= oContainerWidth) {
              iNewScrollLeft = iColumnWidth;
              bIsScrollable = true;
          }

          if (bIsScrollable) {
              oDOM.scrollBy({
                  left: iNewScrollLeft,
                  behavior: 'smooth'
              });
          }
      });
  };

  scrollSectionOnDOMMount = (oDOM) => {
    //To manipulate DOM only when it is mounted, not when it is unmounted
    if(!CS.isEmpty(oDOM)) {
      let oParentDOM = ReactDom.findDOMNode(this);
      let oScrollableHeader = oParentDOM.getElementsByClassName("mnmHeader");
      let iScrollLeft = oScrollableHeader[0].scrollLeft;

      let oChildDOM = ReactDom.findDOMNode(oDOM);
      let DOMToScroll = oChildDOM.getElementsByClassName("mnmScroll");

      if(iScrollLeft > 0 && DOMToScroll) {
        DOMToScroll[0].scrollBy({
          left: iScrollLeft,
        })
      }
    }
  };

  isScrollable = () => {
    let {oScrollableDOM, oScrollableContainer} = this.getDOMsFromThisView();
    if (oScrollableDOM[0] && oScrollableContainer[0]) {
      var oScrollableParentClientRect = oScrollableDOM[0].getBoundingClientRect();
      var oScrollableChildClientRect = oScrollableContainer[0].getBoundingClientRect();

      return Math.ceil(oScrollableChildClientRect.width) > Math.ceil(oScrollableParentClientRect.width);
    }
  };

  getFixedHeaderView = () => {
    var oComparisonLayoutData = this.props.timelineData.comparisonLayoutData || {};
    var oTimelineData = this.props.timelineData;
    var bIsInstancesComparisonView = oTimelineData && oTimelineData.isContentComparisonMode;
    var aTables = [];
    var iCount = 0;

    CS.forEach(oComparisonLayoutData.fixedHeaderTable, function (oTableData) {
      if (!CS.isEmpty(oTableData.rowData)) {
        aTables.push(
            <div className="tableWrapper fixedHeaderTable" key={iCount++}>
              <MatchAndMergeHeaderView
                  rowData={oTableData.rowData}
                  columnData={oTableData.columnData}
                  firstColumnWidth={oTableData.firstColumnWidth}
                  tableId={oTableData.tableId}
                  tableGroupName={"fixedHeaderTable"}
                  isInstancesComparisonView={bIsInstancesComparisonView}
                  isGoldenRecordComparison={oTimelineData.isGoldenRecordComparison}
              />
            </div>
        );
      }
    });

    return (
        <div className="comparisonFixedHeader">
          {aTables}
        </div>
    );
  };

  getMatchAndMergeView = (oTableData, sTableGroupName) => {
    let _this = this;
    var oComparisonData = this.props.timelineData;
    var bIsInstancesComparisonView = oComparisonData && oComparisonData.isContentComparisonMode;
    let bHideEqualElements = _this.state.hideEqualElements;

    return (
        <MatchAndMergeView
            ref = {this.scrollSectionOnDOMMount}
            rowData={oTableData.rowData}
            columnData={oTableData.columnData}
            firstColumnWidth={oTableData.firstColumnWidth}
            tableId={oTableData.tableId}
            tableGroupName={sTableGroupName}
            isInstancesComparisonView={bIsInstancesComparisonView}
            showHeader={false}
            hideEqualElements={bHideEqualElements}
            isGoldenRecordComparison={oComparisonData.isGoldenRecordComparison || false}
        />
    )
  };

  getMatchAndMergeTagView = (oTableData) => {
    let _this = this;
    var oComparisonData = _this.props.timelineData;
    var bIsInstancesComparisonView = oComparisonData && oComparisonData.isContentComparisonMode;
    let bHideEqualElements = _this.state.hideEqualElements;

    return (
        <MatchAndMergeTagView
            ref = {this.scrollSectionOnDOMMount}
            rowData={oTableData.rowData}
            columnData={oTableData.columnData}
            firstColumnWidth={oTableData.firstColumnWidth}
            tableId={oTableData.tableId}
            isInstancesComparisonView={bIsInstancesComparisonView}
            showHeader={false}
            hideEqualElements={bHideEqualElements}
            isGoldenRecordComparison={oComparisonData.isGoldenRecordComparison || false}
        />
    )
  };

  getMatchAndMergeRelationshipView = (oTableData) => {
    let _this = this;
    var oComparisonData = _this.props.timelineData;
    var bIsInstancesComparisonView = oComparisonData && oComparisonData.isContentComparisonMode;
    let bHideEqualElements = _this.state.hideEqualElements;

    return (
        <MatchAndMergeRelationshipView
            ref = {this.scrollSectionOnDOMMount}
            rowData={oTableData.rowData}
            columnData={oTableData.columnData}
            firstColumnWidth={oTableData.firstColumnWidth}
            referencedAssetsData={oTableData.referencedAssetsData}
            tableId={oTableData.tableId}
            isInstancesComparisonView={bIsInstancesComparisonView}
            showHeader={false}
            hideEqualElements={bHideEqualElements}
            context={oComparisonData.isGoldenRecordComparison ? "goldenRecord" : ""}
            isGoldenRecordComparison={oComparisonData.isGoldenRecordComparison || false}
        />
    )
  };

  getExpandableView = (sHeader, oChildrenView, sDOMUniqueIdentifier, bIDefaultExpanded, sIconKey, bShowIcon) => {
    let oHeaderData = {
      header: sHeader,
      iconKey: sIconKey,
      showHeaderIcon: bShowIcon
    };
    return (
        <ExpandableView
            key={sDOMUniqueIdentifier}
            isDefaultExpanded={bIDefaultExpanded}
            headerData={oHeaderData}>
          {oChildrenView}
        </ExpandableView>
    )
  };

  getView = () => {
    let _this = this;
    let oTimeLineData = this.props.timelineData;
    var oComparisonLayoutData = oTimeLineData.comparisonLayoutData || {};
    let bIsInstancesComparisonView = oTimeLineData && oTimeLineData.isContentComparisonMode;

    var aTables = [];
    let bHideEqualElements = _this.state.hideEqualElements;
    let bHideTable = false;
    let sSelectedHeaderButtonId = oTimeLineData.selectedHeaderButtonId || "";

    if(sSelectedHeaderButtonId === "timeline_comparison_properties") {
      this.getViewForProperties(oComparisonLayoutData, bHideTable, bIsInstancesComparisonView, bHideEqualElements, _this, aTables);
    } else if(sSelectedHeaderButtonId === "timeline_comparison_relationship") {
      this.getViewForRelationship(oComparisonLayoutData, bHideTable, bIsInstancesComparisonView, bHideEqualElements, _this, aTables);
    } else if(CS.isEmpty(sSelectedHeaderButtonId)) {
      this.getViewForProperties(oComparisonLayoutData, bHideTable, bIsInstancesComparisonView, bHideEqualElements, _this, aTables);
      this.getViewForRelationship(oComparisonLayoutData, bHideTable, bIsInstancesComparisonView, bHideEqualElements, _this, aTables);
    }

    return aTables;
  };

  getViewForRelationship = (oComparisonLayoutData, bHideTable, bIsInstancesComparisonView, bHideEqualElements, _this, aTables) => {
    CS.forEach(oComparisonLayoutData.relationshipTable, function (oTableData, iIndex) {
      bHideTable = bIsInstancesComparisonView ? oTableData.allRowsAreEqual && bHideEqualElements : oTableData.allRowsAreEqual;
      if (!CS.isEmpty(oTableData.rowData) && !bHideTable) {
        let oChildrenView = _this.getMatchAndMergeRelationshipView(oTableData, ComparisonViewConstants.TAG_TABLE);
        let oExpandableView = _this.getExpandableView(oTableData.tableName, oChildrenView, iIndex, false);
        aTables.push(oExpandableView);
      }
    });
  }

  getViewForProperties = (oComparisonLayoutData, bHideTable, bIsInstancesComparisonView, bHideEqualElements, _this, aTables) => {
    CS.forEach(oComparisonLayoutData.headerInformationTable, function (oTableData, iIndex) {
      bHideTable = bIsInstancesComparisonView ? oTableData.allRowsAreEqual && bHideEqualElements : oTableData.allRowsAreEqual;
      if (!CS.isEmpty(oTableData.rowData) && !bHideTable) {
        let oChildrenView = _this.getMatchAndMergeView(oTableData, ComparisonViewConstants.HEADER_INFORMATION_TABLE);
        let oExpandableView = _this.getExpandableView(oTableData.tableName, oChildrenView, iIndex, true);
        aTables.push(oExpandableView);
      }
    });

    CS.forEach(oComparisonLayoutData.attributeTable, function (oTableData, iIndex) {
      bHideTable = bIsInstancesComparisonView ? oTableData.allRowsAreEqual && bHideEqualElements : oTableData.allRowsAreEqual;
      if (!CS.isEmpty(oTableData.rowData) && !bHideTable) {
        let oChildrenView = _this.getMatchAndMergeView(oTableData, ComparisonViewConstants.ATTRIBUTE_TABLE);
        let oExpandableView = _this.getExpandableView(oTableData.tableName, oChildrenView, iIndex, false);
        aTables.push(oExpandableView);
      }
    });

    CS.forEach(oComparisonLayoutData.tagTable, function (oTableData, iIndex) {
      bHideTable = bIsInstancesComparisonView ? oTableData.allRowsAreEqual && bHideEqualElements : oTableData.allRowsAreEqual;
      if (!CS.isEmpty(oTableData.rowData) && !bHideTable) {
        let oChildrenView = _this.getMatchAndMergeTagView(oTableData, ComparisonViewConstants.TAG_TABLE);
        let oExpandableView = _this.getExpandableView(oTableData.tableName, oChildrenView, iIndex, false, oTableData.iconKey, true);
        aTables.push(oExpandableView);
      }
    });
  }

  handleHideEqualElementsButtonClicked = () => {
    let bHideEqualElements = this.state.hideEqualElements;
    this.setState({
      hideEqualElements: !bHideEqualElements
    })
  };

  getScrollButtonsView = () => {
    var aButtonsView = [];

    aButtonsView.push(
        <TooltipView key={'PREVIOUS'} placement="bottom" label={getTranslation().PREVIOUS}>
          <div className="scrollIcon left"  ref={this.scrollIconLeft} onClick={this.handleScrollIconClicked.bind(this, "moveLeft")}></div>
        </TooltipView>
    );

    aButtonsView.push(
        <TooltipView key={'NEXT'} label={getTranslation().NEXT}>
          <div className="scrollIcon right" ref={this.scrollIconRight} onClick={this.handleScrollIconClicked.bind(this, "moveRight")}></div>
        </TooltipView>
    );

    return aButtonsView;
  };

  getComparisonTablesView = () => {
    var oView = this.getView();
    return (
        <div className="comparisonTablesContainer">
          <div className="comparisonTables">
            {oView}
          </div>
        </div>
    );
  };

  getSkipEqualElementsView = () => {
    let bHideEqualElements = this.state.hideEqualElements;
    let sButtonLabel = bHideEqualElements ? getTranslation().SHOW_ALL  : getTranslation().HIDE_IDENTICAL;
    let sClassName = bHideEqualElements ? "skipEqualElementsButtonView isSelected" : "skipEqualElementsButtonView ";

    return (
        <div className={sClassName} onClick={this.handleHideEqualElementsButtonClicked}>
          <div className="skipEqualElementsButtonLabel">{sButtonLabel}</div>
        </div>
    )
  };

  handleLanguageForComparisonChanged = (oModel) => {
    let sId = oModel.id;
    EventBus.dispatch(oEvents.COMPARISON_VIEW_LANGUAGE_FOR_COMPARISON_CHANGED, sId);
  };


  getContextMenuModelList = (oLanguageSelectorData) => {
    let aContextModelList = [];
    let aItemList = oLanguageSelectorData.list;
    let sSelectedItemCode = oLanguageSelectorData.selectedItemCode;

    CS.forEach(aItemList, function (oItem) {
      if(oItem.code !== sSelectedItemCode)
      aContextModelList.push(new ContextMenuViewModel(
          oItem.id,
          CS.getLabelOrCode(oItem),
          false,
          oItem.iconKey,
          {}
      ));
    });

    return aContextModelList;
  };


  getLanguageSelectorView = () => {
    let oTimelineData = this.props.timelineData;
    let oLanguageSelectorData = oTimelineData.languageSelectorData;
    let oSelectedItem = oLanguageSelectorData.selectedItem;
    let sIconURL = ViewUtils.getIconUrl(oSelectedItem.iconKey);

    return (
        <div className="languageForComparisonContainer">
          <ContextMenuViewNew
              contextMenuViewModel={this.getContextMenuModelList(oLanguageSelectorData)}
              onClickHandler={this.handleLanguageForComparisonChanged}
              anchorOrigin={{horizontal: 'left', vertical: 'bottom'}}
              targetOrigin={{horizontal: 'left', vertical: 'top'}}
              showSearch={false}>
            <div className={"languageSelectorContainer"}>
              <div className="languageSelectorIcon">
                <ImageFitToContainerView imageSrc={sIconURL}/>
              </div>
              <div className="languageSelectorLabel">{CS.getLabelOrCode(oSelectedItem)}</div>
              <div className="buttonIcon"></div>
            </div>
          </ContextMenuViewNew>
        </div>);
  };

  getTimeLineToolbarView = () => {
    let oTimeLineData = this.props.timelineData;
    let oItemList = oTimeLineData.timeLineToolbarData.timelineComparisonDialogToolbarData;
    return (<ToolbarView  toolbarData={oItemList}/>);
  };

  getSaveDiscardButtonDOM = () => {
    let aButtonView = [];

    aButtonView.push(
          <div className="saveOrDiscardButton save">
            <CustomMaterialButtonView
                label={getTranslation().SAVE}
                isRaisedButton={true}
                isDisabled={false}
                onButtonClick={this.handleContentTimelineComparisonButtonClicked.bind(this, "save")}/>
          </div>
      );
    aButtonView.push(
          <div className="saveOrDiscardButton discard">
            <CustomMaterialButtonView
                label={getTranslation().DISCARD}
                isRaisedButton={false}
                isDisabled={false}
                onButtonClick={this.handleContentTimelineComparisonButtonClicked.bind(this, "cancel")}
                />
          </div>
      );
    return <div className={"headerSaveDiscardButtonWrapper"}>{aButtonView}</div>;
  };

  getComparisonBodyHeaderView = (oTimelineData) => {
    let oSaveDiscardButtonDOM = this.props.showSaveDiscardButtons ? this.getSaveDiscardButtonDOM() : null;
    let oTimelineToolbarData = oTimelineData.isComparisonMode ? this.getTimeLineToolbarView() : null;
    return (
        <div className="contentTimelineComparisonHeaderViewWrapper">
          {this.getLanguageSelectorView()}
          {oSaveDiscardButtonDOM}
          {oTimelineToolbarData}
        </div>
    );
  };

  render () {
    var oTimelineData = this.props.timelineData;
    var bIsInstancesComparisonView = oTimelineData && oTimelineData.isContentComparisonMode;
    var oFixedHeaderView = this.getFixedHeaderView();
    var oScrollButtonsView = this.getScrollButtonsView();

    var sClassName = bIsInstancesComparisonView ? "contentTimelineComparisonViewContainer instancesComparison" : "contentTimelineComparisonViewContainer";
    let oSkipEqualElementsView = this.getSkipEqualElementsView();
    let oComparisonBodyHeaderView = oTimelineData.isGoldenRecordComparison ? null : this.getComparisonBodyHeaderView(oTimelineData);
    sClassName = oTimelineData && oTimelineData.isComparisonMode && !oTimelineData.isGoldenRecordComparison ? sClassName + " timelineVersionComparison": sClassName;
    return (
        <div className={sClassName}>
          {oScrollButtonsView}
          {oTimelineData.isGoldenRecordComparison || oTimelineData.isComparisonMode ? null : oSkipEqualElementsView}
          {oComparisonBodyHeaderView}
          <div className="comparisonBody"  ref={this.comparisonBody}>
            {oFixedHeaderView}
            {this.getComparisonTablesView()}
          </div>
        </div>
    );
  }
}

export const view = ContentTimelineComparisonView;
export const events = oEvents;
