import CS from '../../../../../libraries/cs';
import React, {useRef} from 'react';
import ReactPropTypes from 'prop-types';
import {view as CustomPopoverView} from '../../../../../viewlibraries/customPopoverView/custom-popover-view';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import TooltipView from '../../../../../viewlibraries/tooltipview/tooltip-view';
import {view as TreeView} from "../../../../../viewlibraries/treeviewnew/tree-view";
import {view as CustomMaterialButtonView} from '../../../../../viewlibraries/custommaterialbuttonview/custom-material-button-view';
import AllCategoriesTaxonomiesData
  from '../../../../../commonmodule/tack/mock-data-for-all-categories-taxonomy-selector';
import {getTranslations as getTranslation} from '../../../../../commonmodule/store/helper/translation-manager';
import {view as SimpleSearchBarView} from "../../../../../viewlibraries/simplesearchbarview/simple-search-bar-view";
import AllCategoriesSummaryView from '../view/all-categories-taxonomies-selector-summary-view';
import AllCategoriesSearchView from '../view/all-categories-search-view';
import CategoriesConstantDictionary from "../../../../../commonmodule/tack/categories-constant-dictionary";

const oEvents = {
  HANDLE_ALL_CATEGORIES_SELECTOR_APPLY_BUTTON_CLICKED: "handle_all_categories_selector_apply_button_clicked",
  HANDLE_ALL_CATEGORIES_SELECTOR_CANCEL_BUTTON_CLICKED: "handle_cancel_button_clicked",
  HANDLE_ALL_CATEGORIES_SELECTOR_SEARCH_TEXT_CHANGED: "handle_taxonomy_search_text_changed",
  HANDLE_ALL_CATEGORIES_SELECTOR_ORGANISE_BUTTON_CLICKED: "handle_organise_taxonomy_button_clicked",
  HANDLE_ALL_CATEGORIES_BUTTON_CLICKED: "handle_categories_button_clicked",
  HANDLE_ALL_CATEGORIES_TREE_NODE_CHECKBOX_CLICKED: "handle_all_categories_tree_node_checkbox_clicked",
  HANDLE_ALL_CATEGORIES_TAXONOMY_TREE_NODE_CLICKED: "handle_all_categories_taxonomy_tree_node_clicked",
  ALL_CATEGORIES_SELECTOR_VIEW_LOAD_MORE_CLICKED: "ALL_CATEGORIES_SELECTOR_VIEW_LOAD_MORE_CLICKED",
};

const oPropTypes = {
  filterContext: ReactPropTypes.object.isRequired,
  allCategoriesPopOverData: ReactPropTypes.object,
  taxonomyTreeRequestData: ReactPropTypes.object,
};

const AllCategoriesTaxonomySelectorView = (oProps) => {
  const taxonomySelectorContainer = useRef(null);

  const handleTaxonomyApplyButtonClicked = () => {
    EventBus.dispatch(oEvents.HANDLE_ALL_CATEGORIES_SELECTOR_APPLY_BUTTON_CLICKED, oProps.filterContext);
  };

  const handleCancelButtonClicked = () => {
    EventBus.dispatch(oEvents.HANDLE_ALL_CATEGORIES_SELECTOR_CANCEL_BUTTON_CLICKED, oProps.filterContext);
  };

  const handleOrganiseTaxonomyButtonClicked = () => {
    EventBus.dispatch(oEvents.HANDLE_ALL_CATEGORIES_SELECTOR_ORGANISE_BUTTON_CLICKED, oProps.filterContext);
  };

  const handleCategoriesButtonClicked = (sId) => {
    EventBus.dispatch(oEvents.HANDLE_ALL_CATEGORIES_BUTTON_CLICKED, sId, oProps.filterContext,
        oProps.taxonomyTreeRequestData);
  };

  const handleTreeNodeCheckBoxClicked = (sContext, sTreeNodeId) => {
    EventBus.dispatch(oEvents.HANDLE_ALL_CATEGORIES_TREE_NODE_CHECKBOX_CLICKED, sContext, sTreeNodeId, oProps.filterContext);
  };

  const handleTreeSearched = (sSearchText) => {
    EventBus.dispatch(oEvents.HANDLE_ALL_CATEGORIES_SELECTOR_SEARCH_TEXT_CHANGED, oProps.filterContext, sSearchText,
        oProps.taxonomyTreeRequestData);
  };

  const handleGetSelectedTaxonomyChildrenList = (sTreeNodeId) => {
    if (oProps.allCategoriesPopOverData.selectedCategoriesId === CategoriesConstantDictionary.TAXONOMIES) {
      EventBus.dispatch(oEvents.HANDLE_ALL_CATEGORIES_TAXONOMY_TREE_NODE_CLICKED, sTreeNodeId, oProps.filterContext, oProps.taxonomyTreeRequestData);
    }
  };

  const handleLoadMoreClicked = (sContext, sParentId) => {
    EventBus.dispatch(oEvents.ALL_CATEGORIES_SELECTOR_VIEW_LOAD_MORE_CLICKED, sContext, sParentId,
        oProps.taxonomyTreeRequestData, oProps.filterContext);
  };

  const getCustomMaterialButtonView = () => {
    let aButtonDOM = [];
    let aButtonData = [];

    if (oProps.allCategoriesPopOverData.isAllCategoriesPopOverDirty) {
      aButtonData.push(
          {
            id: "apply",
            isContainedButton: true,
            className: "apply",
            label: getTranslation().APPLY,
            clickHandler: handleTaxonomyApplyButtonClicked
          },
          {
            id: "cancel",
            isContainedButton: false,
            className: "cancel",
            label: getTranslation().CANCEL,
            clickHandler: handleCancelButtonClicked
          }
      )
    } else {
      aButtonData.push({
        id: "ok",
        isContainedButton: true,
        className: "okBtn",
        label: getTranslation().OK,
        clickHandler: handleCancelButtonClicked
      })
    }

    if (oProps.allCategoriesPopOverData.isShowOrganizeButton) {
        aButtonData.push({
          id: "organise",
          className: "organiseBtn",
          isContainedButton: false,
          label: getTranslation().ORGANISE,
          clickHandler: handleOrganiseTaxonomyButtonClicked
        })
      }

    CS.forEach(aButtonData, function (oButton) {
      aButtonDOM.push(<CustomMaterialButtonView
          label={oButton.label}
          isContainedButton={oButton.isContainedButton}
          onButtonClick={oButton.clickHandler}
          className={oButton.className}
      />);
    });
    return aButtonDOM;
  };

  const getAllCategoriesSelectorView = () => {
    let aAllCategoriesTaxonomies = new AllCategoriesTaxonomiesData();
    let aCategoriesDOM = [];
    CS.forEach(aAllCategoriesTaxonomies, function (obj) {
      let sListCategoriesClassName = "listCategories";
      sListCategoriesClassName += oProps.allCategoriesPopOverData.selectedCategoriesId === obj.id ? " selected" : "";
      aCategoriesDOM.push(
          <div className={sListCategoriesClassName}
               onClick={handleCategoriesButtonClicked.bind(this, obj.id)}>{obj.label}</div>
      );
    });

    let sTreeContext = oProps.allCategoriesPopOverData.allCategoriesTaxonomyTreeData.context;
    let oTreeView = <TreeView
        {...oProps.allCategoriesPopOverData.allCategoriesTaxonomyTreeData}
        nodeCheckClickHandler={handleTreeNodeCheckBoxClicked.bind(this, sTreeContext)}
        loadChildNodesHandler={handleGetSelectedTaxonomyChildrenList}
        loadMoreHandler={handleLoadMoreClicked}
        showLevelInHeader={true}
        hideSearchbar={true}
        showCount={true}
        showEndNodeIndicator={true}
    />;

    let oSearchBar = <SimpleSearchBarView onBlur={handleTreeSearched}/>;
    let sTreeViewWrapperClassName = "allCategoriesTreeViewWrapper " + oProps.allCategoriesPopOverData.selectedCategoriesId;

    return (
        <div className="allCategoriesTaxonomiesWrapper">
          <div className="allCategoriesUpperView">
            <div className="allCategoriesInnerWrapper">
              <div className="treeSearchBarContainer">
                {oSearchBar}
              </div>
              {CS.isEmpty(oProps.allCategoriesPopOverData.allCategoriesTaxonomyTreeData.searchText) ?
               <div className={sTreeViewWrapperClassName}>
                 <div className={"allCategoriesScroll"}>
                   <div className="allCategories">
                     {aCategoriesDOM}
                   </div>
                   {oTreeView}
                 </div>
               </div> :
               <AllCategoriesSearchView
                   searchData={oProps.allCategoriesPopOverData.allCategoriesTaxonomyTreeData}
                   filterContext={oProps.filterContext}
                   searchButtonClickHandler={handleTreeNodeCheckBoxClicked}
                   treeLoadMoreHandler={handleLoadMoreClicked}
               />}
            </div>
            <div className="summaryViewWrapper">
              <div className="summaryTitle">{getTranslation().SELECTED_CATEGORIES}</div>
              <AllCategoriesSummaryView
                  filterContext={oProps.filterContext}
                  summaryTreeData={oProps.allCategoriesPopOverData.summaryTreeData}
                  selectedCategoriesId={oProps.allCategoriesPopOverData.selectedCategoriesId}
              />
            </div>
          </div>
          <div className="actionButtonView">
            {getCustomMaterialButtonView()}
          </div>
        </div>
    )
  };

  var oChooseTaxonomyHandler = null;
  var oChooseTaxonomyClass = "selectTaxonomyButtonDisabled";

  if (!oProps.isFilterAndSearchViewDisabled) {
    oChooseTaxonomyHandler = handleCategoriesButtonClicked.bind(this, CategoriesConstantDictionary.NATURE_CLASSES);
    oChooseTaxonomyClass = "selectTaxonomyButton";
  }

  let oPopoverStyle = {
    width: '70%',
    maxWidth: '70%',
    height: '490px',
    maxHeight: '490px',
    overflow: "hidden",
  };

  let fCancel2ButtonHandler = handleCancelButtonClicked;
  let anchorE1 = taxonomySelectorContainer.current && taxonomySelectorContainer.current.parentNode;

  return (
      <div className="taxonomySelectorContainer" ref={taxonomySelectorContainer}>
        <TooltipView label={"All Categories"}>
          <div className={oChooseTaxonomyClass} onClick={oChooseTaxonomyHandler}>
            <div className="selectTaxonomyButtonLabel">{getTranslation().ALL_CATEGORIES}</div>
            <div className="downArrowIcon"></div>
          </div>
        </TooltipView>
        <CustomPopoverView
            className="popover-root"
            style={oPopoverStyle}
            open={oProps.allCategoriesPopOverData.isSelectAllCategoriesPopOverVisible}
            anchorEl={anchorE1}
            anchorOrigin={{horizontal: 'center', vertical: 'bottom'}}
            transformOrigin={{horizontal: 'center', vertical: 'top'}}
            onClose={fCancel2ButtonHandler}
        >
          {getAllCategoriesSelectorView()}
        </CustomPopoverView>
      </div>
  );
};

AllCategoriesTaxonomySelectorView.propTypes = oPropTypes;

export default AllCategoriesTaxonomySelectorView;
export const events = oEvents;

