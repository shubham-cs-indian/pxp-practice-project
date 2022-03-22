import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher';
import TooltipView from '../tooltipview/tooltip-view';
import { view as ContextMenuViewNew } from '../contextmenuwithsearchview/context-menu-view-new';

var oEvents = {
  TAXONOMY_DROPDOWN_OPENED: 'taxonomy_dropdown_opened',
  ADD_TAXONOMY_POPOVER_ITEM_CLICKED: 'add_taxonomy_popover_item_clicked',
  TAXONOMY_DROPDOWN_SEARCH_HANDLER: 'taxonomy_dropdown_search_handler',
  TAXONOMY_DROPDOWN_LOAD_MORE_HANDLER: 'taxonomy_dropdown_load_more_handler'
};

const oPropTypes = {
  model: ReactPropTypes.shape({
    id: ReactPropTypes.string,
    label: ReactPropTypes.string,
    contextMenuModelList: ReactPropTypes.array,
    caretVisibility: ReactPropTypes.bool,
    context: ReactPropTypes.string,
    properties: ReactPropTypes.object
  }).isRequired,
  context: ReactPropTypes.string,
  taxonomyId: ReactPropTypes.oneOfType([
    ReactPropTypes.string,
    ReactPropTypes.number//for taxonomyId = -1
  ]),
  paginationData: ReactPropTypes.object,
  localSearch: ReactPropTypes.bool
};
/**
 * @class SmallTaxonomyView - use for add taxonomy in product overview.
 * @memberOf Views
 * @property {custom} model -  id, label, contextMenuModelList, caretVisibility, context, properties
 * @property {string} [context] -  string which contain context.
 * @property {custom} [taxonomyId] - id of taxonomy.
 * @property {object} [paginationData] -  object containing pagination data.
 * @property {bool} [localSearch] -  used for localsearch is done or not.
 */

// @CS.SafeComponent
class SmallTaxonomyView extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      showPopover: false
    }
  }

  handlePopoverVisibility = (sTaxonomyId) => {
    this.setState({
      showPopover: true
    });
    if (this.props.context == "property_collection_taxonomy") {
        let sTaxonomyID = this.props.taxonomyId? this.props.taxonomyId : sTaxonomyId;
        EventBus.dispatch(oEvents.TAXONOMY_DROPDOWN_OPENED, "minorTaxonomy", sTaxonomyID);
    } else {
        EventBus.dispatch(oEvents.TAXONOMY_DROPDOWN_OPENED, "majorTaxonomy", sTaxonomyId, this.props.context);
    }
  }

  handlePopoverItemClicked = (oModel) => {
    var oCurrentContextModel = this.props.model;
    var sTaxonomyId = oCurrentContextModel.context;
    var sContext = oCurrentContextModel.properties['context'];
    let sTaxonomyType = "majorTaxonomy";
    if (this.props.context === "property_collection_taxonomy") {
      sTaxonomyType = "minorTaxonomy"
    }
    EventBus.dispatch(oEvents.ADD_TAXONOMY_POPOVER_ITEM_CLICKED, this, oModel, sTaxonomyId, sContext, sTaxonomyType);
    this.setState({
      showPopover: false
    });
  }

  handlePopoverClose = () => {
    this.setState({
      showPopover: false
    });
  }

  searchHandler = (sSearchText="") => {
    let sTaxonomyType = this.props.context === "property_collection_taxonomy" ? "minorTaxonomy" : "majorTaxonomy";
    EventBus.dispatch(oEvents.TAXONOMY_DROPDOWN_SEARCH_HANDLER, sTaxonomyType, sSearchText, this.props.taxonomyId, this.props.context);
  }

  loadMoreHandler = () => {
    if (this.props.context === "property_collection_taxonomy") {
      EventBus.dispatch(oEvents.TAXONOMY_DROPDOWN_LOAD_MORE_HANDLER, "minorTaxonomy", this.props.taxonomyId);
    } else {
      EventBus.dispatch(oEvents.TAXONOMY_DROPDOWN_LOAD_MORE_HANDLER, "majorTaxonomy", this.props.taxonomyId, this.props.context);
    }
  }

  render() {
    let _this = this;
    var oModel = this.props.model;
    var aContextMenuList = oModel.contextMenuModelList;
    var sLabel = CS.getLabelOrCode(oModel);
    var sContext = oModel.context;
    var fSearchHandler = null;
    var fLoadMoreHandler = null;
    if (!this.props.localSearch) {
      fSearchHandler = this.searchHandler;
      fLoadMoreHandler = this.loadMoreHandler;
    }

    return (<div className="taxonomyHandlerWrapper">
      <ContextMenuViewNew
          contextMenuViewModel={aContextMenuList}
          onClickHandler={this.handlePopoverItemClicked}
          searchText={_this.props.paginationData ? _this.props.paginationData.searchText : ""}
          searchHandler={fSearchHandler}
          loadMoreHandler={fLoadMoreHandler}
          clearSearchOnPopoverClose={true}
          anchorOrigin = {{horizontal: 'left', vertical: 'bottom'}}
          targetOrigin= {{horizontal: 'left', vertical: 'top'}}
          showCustomIcon={_this.props.showCustomIcon}
      >
        <TooltipView placement="top" label={sLabel}>
          <div className="addItemHandler"
               onClick={this.handlePopoverVisibility.bind(this, sContext)}>{sLabel}</div>
        </TooltipView>
      </ContextMenuViewNew>
    </div>);
  }

}

SmallTaxonomyView.propTypes = oPropTypes;

export const view = SmallTaxonomyView;
export const events = oEvents;
