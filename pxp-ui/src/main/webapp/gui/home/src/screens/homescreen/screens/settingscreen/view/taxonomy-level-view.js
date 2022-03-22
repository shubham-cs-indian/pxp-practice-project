import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import TooltipView from './../../../../../viewlibraries/tooltipview/tooltip-view';
import { view as TaxonomyLevelChildItemView } from './taxonomy-level-child-item-view';
import { view as LazyContextMenuView } from './../../../../../viewlibraries/lazycontextmenuview/lazy-context-menu-view';
import { view as ContextMenuViewNew } from './../../../../../viewlibraries/contextmenuwithsearchview/context-menu-view-new';
import ContextMenuViewModel from '../../../../../viewlibraries/contextmenuwithsearchview/model/context-menu-view-model';

const oEvents = {
  TAXONOMY_LEVEL_ACTION_ITEM_CLICKED: "taxonomy_level_action_item_clicked",
  TAXONOMY_LEVEL_MASTER_LIST_CHILDREN_ADDED: "taxonomy_level_master_list_children_added",
  TAXONOMY_ADD_CHILD_BUTTON_CLICKED: "taxonomy_add_child_button_clicked"
};

const oPropTypes = {
  selectedTaxonomy: ReactPropTypes.object,
  selectedMasterList: ReactPropTypes.object,
  selectedChild: ReactPropTypes.string,
  levelIndex: ReactPropTypes.number,
  levelActionItems: ReactPropTypes.array,
  childActionItems: ReactPropTypes.array,
  context: ReactPropTypes.string,
  showPopover: ReactPropTypes.bool,
  allowedTagValues: ReactPropTypes.array,
  masterTagValuesRequestInfo: ReactPropTypes.object,
  activeDetailedTaxonomy: ReactPropTypes.object
};

// @CS.SafeComponent
class TaxonomyLevelView extends React.Component {
  static propTypes = oPropTypes;

  state = {
    showPopover: false
  };

  closePopover = () => {
    this.setState({
      showPopover: false
    });
  };

  handleAddChildButtonClicked = () => {
    var sTagGroupId = this.props.selectedMasterList.id;
    var sTaxonomyId = this.props.selectedTaxonomy.id;
    EventBus.dispatch(oEvents.TAXONOMY_ADD_CHILD_BUTTON_CLICKED, true, sTagGroupId, sTaxonomyId, this.props.context);
  };

  handleAddChildPopoverVisibility = (bPopoverStatus, sContext) => {
    var sTagGroupId = this.props.selectedMasterList.id;
    var sTaxonomyId = this.props.selectedTaxonomy.id;
    EventBus.dispatch(oEvents.TAXONOMY_ADD_CHILD_BUTTON_CLICKED, bPopoverStatus, sTagGroupId, sTaxonomyId, sContext);
  };

  handleMasterListChildrenAdded = (aCheckedItems, oReferencedData) => {
    EventBus.dispatch(oEvents.TAXONOMY_LEVEL_MASTER_LIST_CHILDREN_ADDED, this.props.levelIndex, aCheckedItems, this.props.context, "", oReferencedData);
    this.closePopover();
  };

  handleMasterListChildCreated = (sSearchedValue) => {
    EventBus.dispatch(oEvents.TAXONOMY_LEVEL_MASTER_LIST_CHILDREN_ADDED, this.props.levelIndex, [], this.props.context, sSearchedValue);
    this.closePopover();
  };

  handleLevelActionItemClicked = (sItemId) => {
    var sContext = this.props.context;
    EventBus.dispatch(oEvents.TAXONOMY_LEVEL_ACTION_ITEM_CLICKED, this.props.levelIndex, sItemId, sContext);
  };

  getContextMenuModelsForLevel = () => {
    var aMasterListModels = [];
    var oSelectedMasterListChildren = this.props.allowedTagValues;

    CS.forEach(oSelectedMasterListChildren, function (oMasterItem) {
        aMasterListModels.push(new ContextMenuViewModel(
            oMasterItem.id,
            CS.getLabelOrCode(oMasterItem),
            false,
            "",
            {context: "taxonomyLevelsMasterList"}
        ));
    });

    return aMasterListModels;
  };

  render() {
    var _this = this;
    var oSelectedTaxonomy = this.props.selectedTaxonomy;
    var oSelectedMasterList = this.props.selectedMasterList;
    var sSelectedChildId = this.props.selectedChild;
    var iLevelIndex = this.props.levelIndex;
    var aChildActionItems = this.props.childActionItems;
    var sLevelLabel = oSelectedMasterList.label || getTranslation().TAXONOMIES;
    var aChildrenViews = [];

    if (oSelectedTaxonomy.label) {
      let bShowCreateOption = this.props.context === "attributionTaxonomy" || this.props.context === "hierarchiesConfiguration";
      let fCreateNodeHandler = bShowCreateOption ? this.handleMasterListChildCreated : CS.noop;

      let oDropDownView = null;
      if (_this.props.masterTagValuesRequestInfo) {
        let oMasterTagValuesRequestInfo = CS.cloneDeep(_this.props.masterTagValuesRequestInfo);
        oMasterTagValuesRequestInfo.customRequestModel = {
          tagId: this.props.selectedMasterList.id,
          taxonomyId: this.props.selectedTaxonomy.id
        };

        oDropDownView = oMasterTagValuesRequestInfo.customRequestModel.tagId && oMasterTagValuesRequestInfo.customRequestModel.taxonomyId && oMasterTagValuesRequestInfo.customRequestModel.taxonomyId != -1 ?
            <LazyContextMenuView
                key={oSelectedTaxonomy.id}
                isMultiselect={true}
                onApplyHandler={this.handleMasterListChildrenAdded}
                onCreateHandler={fCreateNodeHandler}
                showCreateButton={bShowCreateOption}
                context={this.props.levelIndex + ""}
                anchorOrigin={{horizontal: 'left', vertical: 'bottom'}}
                targetOrigin={{horizontal: 'left', vertical: 'top'}}
                requestResponseInfo={oMasterTagValuesRequestInfo}
            >
              <TooltipView label={getTranslation().ADD}>
                <div className="addMasterListChildren" onClick={CS.noop}>
                </div>
              </TooltipView>
            </LazyContextMenuView> : null
      } else {
        let aContextMenuModels = this.getContextMenuModelsForLevel();

        oDropDownView = <ContextMenuViewNew
            key={oSelectedTaxonomy.id}
            contextMenuViewModel={aContextMenuModels}
            isMultiselect={true}
            onApplyHandler={this.handleMasterListChildrenAdded}
            onCreateHandler={fCreateNodeHandler}
            showCreateButton={bShowCreateOption}
            showPopover={this.props.showPopover || this.state.showPopover}
            showHidePopoverHandler={this.handleAddChildPopoverVisibility}
            context={this.props.context}>
          <TooltipView label={getTranslation().ADD}>
            <div className="addMasterListChildren" onClick={this.handleAddChildButtonClicked}>
            </div>
          </TooltipView>
        </ContextMenuViewNew>
      }

      aChildrenViews.push(
          <div className="taxonomyLevelParentContainer" key={oSelectedTaxonomy.id}>
            <TooltipView label={oSelectedTaxonomy.label}>
              <div className="taxonomyLevelParent">
                {oSelectedTaxonomy.label}
              </div>
            </TooltipView>
            {oDropDownView}
          </div>);
    }

    CS.forEach(oSelectedTaxonomy.children, function (oChild) {
      aChildrenViews.push(<TaxonomyLevelChildItemView
          key={oChild.id}
          item={oChild}
          selectedItemId={sSelectedChildId}
          levelIndex={iLevelIndex}
          actionItems={aChildActionItems}
          context={_this.props.context}
          activeDetailedTaxonomy={_this.props.activeDetailedTaxonomy}
          allowedTaxonomyHierarchyList={_this.props.allowedTaxonomyHierarchyList}
      />);
    });
    var aActionItems = [];
    CS.forEach(this.props.levelActionItems, function (oItem) {
      let sActionItemTooltip = "";
      if (oItem.className === "create") {
        sActionItemTooltip = getTranslation().CREATE;
      } else if (oItem.className === "delete") {
        sActionItemTooltip = getTranslation().DELETE;
      }
      aActionItems.push(
          <TooltipView label={sActionItemTooltip} placement={"bottom"} key={oItem.id}>
            <div className={"levelActionItem " + oItem.className}
                 onClick={_this.handleLevelActionItemClicked.bind(_this, oItem.id)}></div>
          </TooltipView>
      )
    });

    return (
        <div className="taxonomyLevelView">
          <div className="levelHeader">
            <TooltipView label={sLevelLabel}>
              <div className="levelLabel">{sLevelLabel}</div>
            </TooltipView>
            <div className="levelActionItems">
              {aActionItems}
            </div>
          </div>
          <div className="levelChildren">
            {aChildrenViews}
          </div>
        </div>
    );
  }
}

export const view = TaxonomyLevelView;
export const events = oEvents;
