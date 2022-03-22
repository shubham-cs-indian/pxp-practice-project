import React, {useRef, useState} from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import {view as CustomMaterialButtonView} from '../../../../../viewlibraries/custommaterialbuttonview/custom-material-button-view';
import {view as TreeView} from "../../../../../viewlibraries/treeviewnew/tree-view";
import CS from "../../../../../libraries/cs";
import {view as NothingFoundView} from "../../../../../viewlibraries/nothingfoundview/nothing-found-view";
import {getTranslations as getTranslation} from '../../../../../commonmodule/store/helper/translation-manager';

const oEvents = {
  HANDLE_SUMMARY_CLEAR_SELECTION_BUTTON_CLICKED: "handle_summary_clear_selection_button_clicked",
  HANDLE_SUMMARY_CROSS_BUTTON_CLICKED: "handle_summary_cross_button_clicked"
};

const oPropTypes = {
  summaryTreeData: ReactPropTypes.object,
  selectedCategoriesId: ReactPropTypes.string,
};

const AllCategoriesTaxonomySelectorSummaryView = (oProps) => {

  const handleSummaryClearSelectionButtonClicked = () => {
    EventBus.dispatch(oEvents.HANDLE_SUMMARY_CLEAR_SELECTION_BUTTON_CLICKED, oProps.filterContext)
  };

  const handleSummaryViewCrossIconCLicked = (context, sButtonId, sSelectedId) => {
    EventBus.dispatch(oEvents.HANDLE_SUMMARY_CROSS_BUTTON_CLICKED, context, sButtonId, sSelectedId, oProps.filterContext)
  };

  const getTreeViewDataForSummaryView = (oTreeViewData, aLevelNames, sContext) => {
    return <TreeView
        treeData={oTreeViewData}
        searchText=""
        treeLoadMoreMap=""
        context={sContext}
        levelNames={aLevelNames}
        nodeCheckClickHandler={CS.noop}
        rightSideActionButtonsHandler={handleSummaryViewCrossIconCLicked}
        loadChildNodesHandler={CS.noop}
        showLevelInHeader={true}
        hideSearchbar={true}
        showCount={false}
        suppressUnmount={true}
    />;
  };

  const getSummaryDataAsPerCategories = () => {
    let aSummaryData = oProps.summaryTreeData;
    let oTreeDataForNatureClasses = aSummaryData["natureClasses"] && getTreeViewDataForSummaryView(aSummaryData["natureClasses"], ["Nature Classes"], "natureClasses") || null;
    let oTreeDataForNonNatureClasses = aSummaryData["attributionClasses"] && getTreeViewDataForSummaryView(aSummaryData["attributionClasses"], ["Attribution Classes"], "attributionClasses") || null;
    let oTreeDataForTaxonomies = aSummaryData["taxonomies"] && getTreeViewDataForSummaryView(aSummaryData["taxonomies"], ["Taxonomies"], "taxonomies") || null;

    let oStyle = {
      height: "28px",
      lineHeight: "28px",
      margin: 0,
      padding: '0 10px',
      minWidth: '64px',
      minHeight: '28px',
      boxShadow: 'none',
      outline: 'none'
    };
    return (
        <React.Fragment>
          <div className="treeViewSummary">
            {oTreeDataForNatureClasses}
            {oTreeDataForNonNatureClasses}
            {oTreeDataForTaxonomies}
          </div>
          <CustomMaterialButtonView
              label={getTranslation().CLEAR_SELECTION}
              isContainedButton={true}
              onButtonClick={handleSummaryClearSelectionButtonClicked}
              className={"clearSelection"}
              style={oStyle}
          />
        </React.Fragment>
    )
  };

  return (
      <div className="treeWrapperSummaryView">
        {CS.isNotEmpty(oProps.summaryTreeData) ? getSummaryDataAsPerCategories() :
         <NothingFoundView message={getTranslation().NOTHING_SELECTED_PLEASE_SELECT_FROM_THE_LIST}/>}
      </div>

  );
};

AllCategoriesTaxonomySelectorSummaryView.propTypes = oPropTypes;

export default AllCategoriesTaxonomySelectorSummaryView;
export const events = oEvents;

