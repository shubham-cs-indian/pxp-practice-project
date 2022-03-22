import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import ReactDOM from 'react-dom';
import { view as CustomPopoverView } from '../../../../../viewlibraries/customPopoverView/custom-popover-view';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';
import TooltipView from '../../../../../viewlibraries/tooltipview/tooltip-view';
import { view as HorizontalTreeView } from './fltr-horizontal-tree-view';
import { view as SimpleSearchBarView } from '../../../../../viewlibraries/simplesearchbarview/simple-search-bar-view';
import { view as CustomMaterialButtonView } from '../../../../../viewlibraries/custommaterialbuttonview/custom-material-button-view';
var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
  FLTR_TAXONOMY_SELECTOR_APPLY_BUTTON_CLICKED: "fltr_taxonomy_selector_apply_button_clicked",
  HANDLE_TREE_NODE_TOGGLE_CLICKED: "handle_tree_node_toggle_clicked",
  HANDLE_CANCEL_BUTTON_CLICKED: "handle_cancel_button_clicked",
  HANDLE_TAXONOMY_SEARCH_TEXT_CHANGED: "handle_taxonomy_search_text_changed",
  HANDLE_SELECTED_TAXONOMIES_BUTTON_CLICKED : "handle_selected_taxonomies_button_clicked",
  HANDLE_ORGANISE_TAXONOMY_BUTTON_CLICKED: "handle_organise_taxonomy_button_clicked",
  FLTR_TAXONOMY_SELECTOR_CLEAR_ALL_CLICKED : "fltr_taxonomy_selector_clear_all_clicked",
  HANDLE_SHOW_TAXONOMIES_POP_OVER_STATE: "handle_show_taxonomies_pop_over_state",
  FLTR_TAXONOMY_SELECTOR_HIDE_EMPTY_TOGGLE_CLICKED: "fltr_taxonomy_selector_hide_empty_toggle_clicked"
};

const oPropTypes = {
  taxonomyTree: ReactPropTypes.array,
  taxonomyVisualProps: ReactPropTypes.object,
  getFilterSectionWidth: ReactPropTypes.func,
  matchingTaxonomyIds: ReactPropTypes.array,
  taxonomyTreeSearchText: ReactPropTypes.string,
  isFilterAndSearchViewDisabled: ReactPropTypes.bool,
  parentTaxonomyId: ReactPropTypes.string,
  affectedTreeNodeIds: ReactPropTypes.array,
  isSelectTaxonomyPopOverVisible: ReactPropTypes.bool,
  viewMode: ReactPropTypes.string,
  collectionData: ReactPropTypes.object,
  isOrganizeButtonDisabled: ReactPropTypes.bool,
  filterContext: ReactPropTypes.object.isRequired,
  selectedHierarchyContext: ReactPropTypes.string,
  taxonomyTreeRequestData: ReactPropTypes.object,
  hideEmpty: ReactPropTypes.bool,
};

// @CS.SafeComponent
class FltrTaxonomySelectorView extends React.Component {
  constructor(props) {
    super(props);
    this.taxonomyTreeSearchInput = React.createRef();
  }

  state = {
    searchText: "",
    showFullTree: false
  };

  componentDidUpdate() {
    this.adjustPopOverDOMStyle();
    if(this.taxonomyTreeSearchInput.current) {
      this.taxonomyTreeSearchInput.current.value = this.props.taxonomyTreeSearchText;
    }
  }

  adjustPopOverDOMStyle = () => {
  };

  popOverDOMMounted = (oView) => {
    var _this = this;
    if (oView) {
      _this.popOverDOM = oView;
      this.adjustPopOverDOMStyle();
    }
  };

  handleSelectTaxonomiesButtonClicked = () => {
    var oDOM = ReactDOM.findDOMNode(this);
    this.setState({
      anchorElement: oDOM.parentNode
    });
    EventBus.dispatch(oEvents.HANDLE_SELECTED_TAXONOMIES_BUTTON_CLICKED, this.props.filterContext, this.props.taxonomyTreeRequestData);
  };

  handleTaxonomySearchTextChanged = (sSearchText) => {
    EventBus.dispatch(oEvents.HANDLE_TAXONOMY_SEARCH_TEXT_CHANGED, this, sSearchText, this.props.filterContext);
  };

  handleHideEmptyToggleClicked = () => {
    EventBus.dispatch(oEvents.FLTR_TAXONOMY_SELECTOR_HIDE_EMPTY_TOGGLE_CLICKED, !this.props.hideEmpty, this.props.filterContext);
  };

  handleClearAllClicked = () => {
    EventBus.dispatch(oEvents.FLTR_TAXONOMY_SELECTOR_CLEAR_ALL_CLICKED, this.props.filterContext);
  };

  handleTaxonomyApplyButtonClicked = () => {
    EventBus.dispatch(oEvents.FLTR_TAXONOMY_SELECTOR_APPLY_BUTTON_CLICKED, this.props.filterContext);
    EventBus.dispatch(oEvents.HANDLE_SHOW_TAXONOMIES_POP_OVER_STATE, false, this.props.filterContext);
  };

  handleCancelButtonClicked = () => {
    EventBus.dispatch(oEvents.HANDLE_CANCEL_BUTTON_CLICKED, this.props.filterContext);
    EventBus.dispatch(oEvents.HANDLE_SHOW_TAXONOMIES_POP_OVER_STATE, false, this.props.filterContext);
    this.setState({
      searchText: ""
    });
  };

  handleOrganiseTaxonomyButtonClicked = () => {
    EventBus.dispatch(oEvents.HANDLE_ORGANISE_TAXONOMY_BUTTON_CLICKED, this.props.filterContext);
    EventBus.dispatch(oEvents.HANDLE_SHOW_TAXONOMIES_POP_OVER_STATE, false, this.props.filterContext);
    this.setState({
      searchText: ""
    });
  };

  handleTreeNodeToggleClicked = (iNodeId) => {
    EventBus.dispatch(oEvents.HANDLE_TREE_NODE_TOGGLE_CLICKED, this, iNodeId, this.props.filterContext);
  };

  handleSearchTextChanged = (oEvent) => {
    var sSearchText = oEvent.target.value;
    this.setState({
      searchText: sSearchText
    });
  };

  getNormalTaxonomyView = () => {
    var oProps = this.props;
    var sHideEmptyText = this.props.hideEmpty ? getTranslation().SHOW_ALL : getTranslation().HIDE_EMPTY;
    var sHideEmptyClassName = this.props.hideEmpty ? "hideEmpty " : "showAll ";
    var aMatchingIds = this.props.matchingTaxonomyIds || [];
    var oSearchCountView = null;
    if (this.props.taxonomyTreeSearchText) {
      var sMatchText = getTranslation().MATCHES_FOUND;
      var sResultText = aMatchingIds.length + " " + sMatchText;
      oSearchCountView = <div className="taxonomyTreeSearchResultCount">{sResultText}</div>
    }

    var oHorizontalTreeView = <HorizontalTreeView taxonomyTree={this.props.taxonomyTree}
                                                  taxonomyVisualProps={this.props.taxonomyVisualProps}
                                                  matchingTaxonomyIds={this.props.matchingTaxonomyIds}
                                                  hideEmpty={this.props.hideEmpty}
                                                  parentTaxonomyId={this.props.parentTaxonomyId}
                                                  taxonomyTreeSearchText={this.props.taxonomyTreeSearchText}
                                                  filterContext={this.props.filterContext}
                                                  affectedTreeNodeIds={this.props.affectedTreeNodeIds}/>

    var oOrganiseButton = null;

     let oHideEmptyButton = <div className={"taxonomyTreeToolbarButton " + sHideEmptyClassName} onClick={this.handleHideEmptyToggleClicked}>{sHideEmptyText}</div>;
      let oClearAllButton = <div className="taxonomyTreeToolbarButton clearAll" onClick={this.handleClearAllClicked}>{getTranslation().CLEAR_SELECTION}</div>;
      let oSearchBar = (<div className="taxonomyTreeSearchBarContainer">
           <SimpleSearchBarView
              onBlur={this.handleTaxonomySearchTextChanged}
              searchText={this.props.taxonomyTreeSearchText}
           />
          </div>);
      if (!oProps.isEditMode && !oProps.isOrganizeButtonDisabled) {
        var oActiveCollection = this.props.collectionData.activeCollection;
        oOrganiseButton = CS.isEmpty(oActiveCollection) && !oProps.isHierarchyMode ? (
            <div className="organiseButtonContainer">
              <CustomMaterialButtonView
                  label={getTranslation().ORGANISE}
                  isRaisedButton={false}
                  isDisabled={false}
                  onButtonClick={this.handleOrganiseTaxonomyButtonClicked}/>
            </div>) : null;
      }


    return (
        <div ref={this.popOverDOMMounted}>
          <div className="taxonomyTreeSearchBarWrapper">
            {oSearchBar}
            {oSearchCountView}
            {oHideEmptyButton}
            {oClearAllButton}
          </div>
          {oHorizontalTreeView}
          <div className="taxonomyTreeControlWrapper">
            <div className="applyButtonContainer">
              <CustomMaterialButtonView
                  label={getTranslation().APPLY}
                  isRaisedButton={true}
                  isDisabled={false}
                  onButtonClick={this.handleTaxonomyApplyButtonClicked}/>
            </div>
            <div className="cancelButtonContainer">
              <CustomMaterialButtonView
                  label={getTranslation().CANCEL}
                  isRaisedButton={false}
                  isDisabled={false}
                  onButtonClick={this.handleCancelButtonClicked}/>
            </div>
            {oOrganiseButton}
          </div>
        </div>
    )
  };

  render() {
    var __props = this.props;
    var oChooseTaxonomyHandler = null;
    var oChooseTaxonomyClass = "selectTaxonomyButtonDisabled";

    if(!__props.isFilterAndSearchViewDisabled) {
      oChooseTaxonomyHandler = this.handleSelectTaxonomiesButtonClicked;
      oChooseTaxonomyClass = "selectTaxonomyButton";
    }

    let oPopoverStyle = {width: '70%',
      maxWidth: '1800px',
      maxHeight: '490px'
    };

    var oInsidePopOverDOM = this.getNormalTaxonomyView();
    return (
        <div className="taxonomySelectorContainer">
          <TooltipView label={getTranslation().CHOOSE_TAXONOMIES}>
            <div className={oChooseTaxonomyClass} onClick={oChooseTaxonomyHandler}>
              <div className="selectTaxonomyButtonLabel">{getTranslation().CHOOSE_TAXONOMIES}</div>
              <div className="downArrowIcon"></div>
            </div>
          </TooltipView>
          <CustomPopoverView
              className="popover-root"
              style={oPopoverStyle}
              open={this.props.isSelectTaxonomyPopOverVisible}
              anchorEl={this.state.anchorElement}
              anchorOrigin={{horizontal: 'center', vertical: 'bottom'}}
              transformOrigin={{horizontal: 'center', vertical: 'top'}}
              onClose={this.handleCancelButtonClicked}
          >
            {oInsidePopOverDOM}
          </CustomPopoverView>
        </div>
    );
  }
}

export const view = FltrTaxonomySelectorView;
export const events = oEvents;
