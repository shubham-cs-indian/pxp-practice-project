import React from 'react';
import ReactDOM from 'react-dom';
import ReactPropTypes from 'prop-types';
import AllCategoriesTaxonomySelectorView from './all-categories-selector-view';
import { view as SearchBarView } from './fltr-search-bar-view';
import { view as CollectionOptionsView } from './fltr-collection-options-view';
import ViewUtils from './utils/view-utils';
import HierarchyTypesDictionary from '../../../../../commonmodule/tack/hierarchy-types-dictionary';

const oEvents = {};
const oPropTypes = {
  taxonomyTree: ReactPropTypes.array,
  taxonomyVisualProps: ReactPropTypes.object,
  searchText: ReactPropTypes.string,
  collectionData : ReactPropTypes.object,
  matchingTaxonomyIds: ReactPropTypes.array,
  taxonomyTreeSearchText: ReactPropTypes.string,
  isFilterAndSearchViewDisabled: ReactPropTypes.bool,
  parentTaxonomyId: ReactPropTypes.string,
  viewMasterData: ReactPropTypes.object,
  affectedTreeNodeIds: ReactPropTypes.array,
  isSelectTaxonomyPopOverVisible: ReactPropTypes.bool,
  selectedHierarchyContext: ReactPropTypes.string,
  viewMode: ReactPropTypes.string,
  isChooseTaxonomyVisible: ReactPropTypes.bool,
  isOrganizeButtonDisabled: ReactPropTypes.bool,
  filterContext: ReactPropTypes.object.isRequired,
  showTaxonomySelectorView: ReactPropTypes.bool,
  taxonomyTreeRequestData: ReactPropTypes.object,
  hideEmpty: ReactPropTypes.bool,
  allCategoriesPopOverData: ReactPropTypes.object
};
const iMinWidth = 420;

// @CS.SafeComponent
class FilterSearchView extends React.Component {
  static propTypes = oPropTypes;

  constructor (props){
    super(props);
    this.collectionPopOverWidth = 0;

    this.filterSearchViewContainer = React.createRef();
    this.searchBarView = React.createRef();
  }

  getFilterSectionWidth = () => {
    return this.filterSearchViewContainer.current.offsetWidth;
  };

  getSearchInputSize = (oCollectionData) => {
    var bShowDynamicCollectionOption = oCollectionData.showDynamicCollectionOption;
    var bShowStaticCollectionOption = oCollectionData.showStaticCollectionOption;
    if (bShowDynamicCollectionOption && bShowStaticCollectionOption) {
      return 0;
    } else if (bShowDynamicCollectionOption || bShowStaticCollectionOption) {
      return 1;
    } else {
      return 2;
    }
  };

  componentDidMount () {
    let oSearchBarViewRef = this.searchBarView.current;
    if (oSearchBarViewRef) {
      let oSearchBarDOM = ReactDOM.findDOMNode(oSearchBarViewRef);
      this.collectionPopOverWidth = (oSearchBarDOM && oSearchBarDOM.clientWidth < iMinWidth) ? iMinWidth : oSearchBarDOM.clientWidth;
    }
  }

  render() {
    var __props = this.props;
    var aTaxonomyTree = __props.taxonomyTree;
    var oTaxonomyVisualProps = __props.taxonomyVisualProps;
    var sSearchText = __props.searchText;
    var oCollectionData = __props.collectionData;
    var aMatchingTaxonomyIds = __props.matchingTaxonomyIds;
    var sTaxonomyTreeSearchText = __props.taxonomyTreeSearchText;
    var iSearchInputSize = this.getSearchInputSize(oCollectionData);
    var oViewMasterData = this.props.viewMasterData;
    var oCollectionOptionsViewMasterData =  oViewMasterData.collectionOptionsViewMasterData;
    var sSelectedHierarchyContext = __props.selectedHierarchyContext;

    var bDisableFilterAndSearch = __props.isFilterAndSearchViewDisabled;
    var sParentClassSuffix = "";
    var oFilterTaxonomySelectorView = null;
    var oCollectionOptionsView = null;
    var bIsChooseTaxonomyVisible = __props.isChooseTaxonomyVisible;
    if(
        (ViewUtils.getIsHierarchyViewMode(__props.viewMode)&& (sSelectedHierarchyContext == HierarchyTypesDictionary.TAXONOMY_HIERARCHY || sSelectedHierarchyContext == HierarchyTypesDictionary.COLLECTION_HIERARCHY))
        || !oViewMasterData.canFilterTaxonomy || !bIsChooseTaxonomyVisible){
      sParentClassSuffix = " taxonomySelectorHidden";
    }

    else {
      var sViewMode = __props.viewMode;
      if (this.props.showTaxonomySelectorView && oViewMasterData.canFilterTaxonomy) {
        oFilterTaxonomySelectorView =
            <AllCategoriesTaxonomySelectorView
                filterContext={__props.filterContext}
                allCategoriesPopOverData={__props.allCategoriesPopOverData}
                taxonomyTreeRequestData={__props.taxonomyTreeRequestData}
        />;
      }

      oCollectionOptionsView = !this.props.showTaxonomySelectorView ? null : (
          <CollectionOptionsView collectionData={oCollectionData}
                                 isFilterAndSearchViewDisabled={__props.isFilterAndSearchViewDisabled}
                                 collectionPopOverWidth={this.collectionPopOverWidth}
                                 filterContext={__props.filterContext}
                                 viewMasterData={oCollectionOptionsViewMasterData}/>);
    }

    return (
        <div className={"filterSearchViewContainer" + sParentClassSuffix} ref={this.filterSearchViewContainer}>
          {oFilterTaxonomySelectorView}
            <SearchBarView searchText={sSearchText} ref={this.searchBarView}
                           searchInputSize={iSearchInputSize}
                           isFilterAndSearchViewDisabled={bDisableFilterAndSearch}
                           filterContext={__props.filterContext}
                           selectedHierarchyContext={sSelectedHierarchyContext}

            />
          {oCollectionOptionsView}
        </div>
    );
  }
}

export const view = FilterSearchView;
export const events = oEvents;
