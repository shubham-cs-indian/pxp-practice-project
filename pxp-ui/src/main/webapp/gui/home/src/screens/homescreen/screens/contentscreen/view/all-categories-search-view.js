import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import {view as TreeView} from "../../../../../viewlibraries/treeviewnew/tree-view";
import { getTranslations as getTranslations } from '../../../../../commonmodule/store/helper/translation-manager';

const oPropTypes = {
  searchData: ReactPropTypes.object,
  treeLoadMoreHandler: ReactPropTypes.func,
  searchButtonClickHandler: ReactPropTypes.func
};

const AllCategoriesSearchView = (oProps) => {

  const getTreeViewDataForSummaryView = (oTreeViewData, aLevelNames, sContext) => {
    return <TreeView
        treeData={oTreeViewData}
        searchText={oProps.searchData.searchText}
        treeLoadMoreMap={oProps.searchData.treeLoadMoreMap}
        context={sContext}
        levelNames={aLevelNames}
        nodeCheckClickHandler={oProps.searchButtonClickHandler.bind(this, sContext)}
        loadChildNodesHandler={CS.noop}
        showLevelInHeader={true}
        hideSearchbar={true}
        hideNothingFoundView={true}
        showCount={true}
        loadMoreHandler={oProps.treeLoadMoreHandler}
        suppressUnmount={true}
    />;
  };

  const getNothingFoundViewWithHeader = (sHeader) => {
    return (
        <div className={"headerWrapper"}>
          <div className={"header"}>{sHeader}</div>
          <div className={"nothingFoundMsg"}>{getTranslations().NO_MATCH_FOUND}</div>
        </div>
    );
  };

  let aNatureClassesData = oProps.searchData.treeData["natureClasses"];
  let oTreeDataForNatureClasses = CS.isNotEmpty(aNatureClassesData) && getTreeViewDataForSummaryView(aNatureClassesData, [getTranslations().NATURE_CLASSES], "natureClasses") || getNothingFoundViewWithHeader(getTranslations().NATURE_CLASSES);

  let aAttributionData = oProps.searchData.treeData["attributionClasses"];
  let oTreeDataForNonNatureClasses = CS.isNotEmpty(aAttributionData) && getTreeViewDataForSummaryView(aAttributionData, [getTranslations().ATTRIBUTION_CLASSES], "attributionClasses") || getNothingFoundViewWithHeader(getTranslations().ATTRIBUTION_CLASSES);

  let aTaxonomiesData = oProps.searchData.treeData["taxonomies"];
  let oTreeDataForTaxonomies = CS.isNotEmpty(aTaxonomiesData) && getTreeViewDataForSummaryView(aTaxonomiesData, [getTranslations().TAXONOMIES], "taxonomies") || getNothingFoundViewWithHeader(getTranslations().TAXONOMIES);

  return (
      <div className="allCategoriesSearchViewWrapper">
        <div className="searchViewLeftSide">
          {oTreeDataForNatureClasses}
          {oTreeDataForNonNatureClasses}
        </div>
        <div className="searchViewRightSide">
          {oTreeDataForTaxonomies}
        </div>
      </div>
  );
};

AllCategoriesSearchView.propTypes = oPropTypes;

export default AllCategoriesSearchView;

