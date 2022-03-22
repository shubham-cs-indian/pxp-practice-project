import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher';
import { view as MultiSelectSearchView } from '../multiselectview/multi-select-search-view';
import ViewUtils from '../utils/view-library-utils';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import { view as LazyContextMenuView } from '../lazycontextmenuview/lazy-context-menu-view';
import TooltipView from '../tooltipview/tooltip-view';

const oEvents = {
  TAG_SELECTOR_VIEW_REMOVE_SELECTED_TAG_GROUP: 'tag_selector_view_remove_selected_tag_group',
  TAG_SELECTOR_VIEW_TAG_LIST_LOAD_MORE_CLICKED: 'tag_selector_view_tag_list_load_more_clicked',
  TAG_SELECTOR_VIEW_TAG_LIST_SEARCH_CLICKED: 'tag_selector_view_tag_list_search_clicked',
  TAG_SELECTOR_VIEW_TAG_LIST_APPLY_BUTTON_CLICKED: 'tag_selector_view_tag_list_apply_button_clicked',
  TAG_SELECTOR_VIEW_TAG_VALUE_LIST_APPLY_BUTTON_CLICKED: 'tag_selector_view_tag_value_list_apply_button_clicked'
};

const oPropTypes = {
  context: ReactPropTypes.string,
  selectedTagsData: ReactPropTypes.array,
  tagMap: ReactPropTypes.object,
  searchText: ReactPropTypes.string,
  paginationData: ReactPropTypes.object,
  isTagValueListSearchEnabled: ReactPropTypes.bool,
  isTagValueListLoadMoreEnabled: ReactPropTypes.bool,
  referencedData: ReactPropTypes.object,
  requestResponseInfo: ReactPropTypes.object,
  menuListHeight: ReactPropTypes.string,
};
/**
 * @class TagSelectorView
 * @memberOf Views
 * @property {string} [context] - Used to differentiate which operation will be perform.
 * @property {array} [selectedTagsData] - Contains selected tags.
 * @property {object} [tagMap] - Deprecated.
 * @property {string} [searchText] - Contains text which you have searched.
 * @property {object} [paginationData] - Contains pagination data.
 * @property {bool} [isTagValueListSearchEnabled] - Deprecated.
 * @property {bool} [isTagValueListLoadMoreEnabled] - Deprecated.
 * @property {object} [referencedData] - Deprecated.
 * @property {object} [requestResponseInfo] - Request data for lazy context menu view.
 * @property {string} [menuListHeight] - Height for menu list.
 */

// @CS.SafeComponent
class TagSelectorView extends React.Component {

  constructor(props) {
    super(props);
  }

  handleRemoveSelectedTagButtonClicked =(sTagGroupId)=> {
    let sContext = this.props.context;
    EventBus.dispatch(oEvents.TAG_SELECTOR_VIEW_REMOVE_SELECTED_TAG_GROUP, sContext, sTagGroupId);
  }

  handleTagListLoadMoreClicked =()=>{
    let oTagMap = this.props.tagMap;
    let sContext = this.props.context;
    let oPaginationData = this.props.paginationData;
    let oTagsPaginationData = oPaginationData.tags;
    oTagsPaginationData.from = CS.size(oTagMap);
    EventBus.dispatch(oEvents.TAG_SELECTOR_VIEW_TAG_LIST_LOAD_MORE_CLICKED, sContext);
  }

  handleTagListSearchTextClicked = (sSearchText) => {
    let sContext = this.props.context;
    EventBus.dispatch(oEvents.TAG_SELECTOR_VIEW_TAG_LIST_SEARCH_CLICKED, sContext, sSearchText);
  }

  handleTagListApplyButtonClicked =(oModel)=>{
    let sContext = this.props.context;
    let aSelectedItems = [oModel.id];
    EventBus.dispatch(oEvents.TAG_SELECTOR_VIEW_TAG_LIST_APPLY_BUTTON_CLICKED, sContext, aSelectedItems);
  }

  handleTagValueListApplyButtonClicked =(sChildContext, aSelectedItems)=>{
    EventBus.dispatch(oEvents.TAG_SELECTOR_VIEW_TAG_VALUE_LIST_APPLY_BUTTON_CLICKED, sChildContext, aSelectedItems);
  }

  getTagValuesDataForMSS =(aTagValues)=>{
    let aSelectedTags = [];
    let aReferencedTags = [];

    CS.forEach(aTagValues, function(oTagValue){

      if(oTagValue.isSelected){
        aSelectedTags.push(oTagValue.tagValueId);
      }

      aReferencedTags.push({
        id: oTagValue.tagValueId,
        label: CS.getLabelOrCode(oTagValue)
      });

    });

    return({
      selectedItems: aSelectedTags,
      items: aReferencedTags
    });
  }

  getSelectedTagGroupsListView =()=> {
    let _this = this;
    let aSelectedTagsData = this.props.selectedTagsData;
    let aSelectedTagsDOM = [];
    let sContext = this.props.context;
    let sSplitter = ViewUtils.getSplitter();

    if(CS.isEmpty(aSelectedTagsData)){
      return null;
    }

    CS.forEach(aSelectedTagsData, function (oSelectedTagData) {
      let oTagValuesData = _this.getTagValuesDataForMSS(oSelectedTagData.tagValues);
      let sSelectedTagId = oSelectedTagData.tagId;
      let sChildContext = sContext + sSplitter + sSelectedTagId;
      let sSelectedTagLabel = `${CS.getLabelOrCode(oSelectedTagData)}${oSelectedTagData.tagType !== "tag_type_boolean"? " :": ""}`;
      let oRemoveButton = null;
      if (sContext != 'technicalImageContext') {
        oRemoveButton = <div className="selectedTagRemoveButtonContainer">
          <div className="selectedTagRemoveButton"
               onClick={_this.handleRemoveSelectedTagButtonClicked.bind(_this, oSelectedTagData.tagId)}>
          </div>
        </div>
      }


      aSelectedTagsDOM.push(<div className="selectedTagView" key={sChildContext}>
        <div className="selectedTagLabel">{sSelectedTagLabel}</div>
        <div className="selectedTagMSSViewContainer">
          {oSelectedTagData.tagType !== "tag_type_boolean" && <MultiSelectSearchView
              disabled={false}
              items={oTagValuesData.items}
              selectedItems={oTagValuesData.selectedItems}
              isMultiSelect={true}
              context={sChildContext}
              showSelectedInDropdown={false}
              disableCross={false}
              cannotRemove={false}
              isLoadMoreEnabled={false}
              onApply={_this.handleTagValueListApplyButtonClicked.bind(_this,sChildContext)}
          />}
        </div>
        {oRemoveButton}
      </div>)
    });

    return (
        <div className="selectedTagGroupsContainer">
          {/*<div className="selectedTagContainerHeader">{getTranslation().SELECTED_TAGS}</div>*/}
          <div className="selectedTagGroupsListView">
            {aSelectedTagsDOM}
          </div>
        </div>
    );
  }

  //TODO: find a better solution
  getFilteredTagMapData =()=> {
    let oTagMap = this.props.tagMap;
    let aSelectedTagsData = this.props.selectedTagsData;

    if (CS.isArray(oTagMap)) {
      CS.forEach(aSelectedTagsData, function (oData) {
        CS.remove(oTagMap, {id: oData.tagId});
      });
    } else {
      CS.forEach(aSelectedTagsData, function (oData) {
        delete oTagMap[oData.tagId];
      });
    }

    return oTagMap;
  }

  getContextMenuViewModel =(oTagMap)=> {
    let aNatureClassModels = [];
    let sContext = this.props.context;
    CS.forEach(oTagMap, function (oTag) {
      aNatureClassModels.push({
        id: oTag.id,
        label: CS.getLabelOrCode(oTag),
        isActive: false,
        icon: "",
        properties: {context: sContext}
      });
    });
    return aNatureClassModels;
  }

  getAddTagGroupView =()=> {
    let _props = this.props;
    let sContext = _props.context;
    let oTagMap = this.getFilteredTagMapData();
    // if (CS.isEmpty(oTagMap)) {
    //   return null;
    // } else {
    let aSelectedItems = this.getContextMenuViewModel(oTagMap);
    let sSearchText = _props.searchText;
    let bIsLoadMoreEnabled = _props.isTagValueListLoadMoreEnabled;
    let bIsSearchTextEnabled = _props.isTagValueListSearchEnabled;
    let fSearchHandler = null;
    let fLoadMoreHandler = null;
    if (bIsLoadMoreEnabled) {
      fLoadMoreHandler = this.handleTagListLoadMoreClicked;
    }
    if (bIsSearchTextEnabled) {
      fSearchHandler = this.handleTagListSearchTextClicked
    }
    //hide selected items in add tags
    let aTagIdsToExclude = CS.map(_props.selectedTagsData, 'tagId');

    if (sContext == 'technicalImageContext') {
      return null;
    }
    let oAnchorOrigin = {horizontal: 'right', vertical: 'top'};
    let oTargetOrigin = {horizontal: 'right', vertical: 'bottom'};

    return (
        <div className="addTagGroupView">

          <LazyContextMenuView
              context={this.props.context}
              selectedItems={aSelectedItems}
              isMultiselect={this.props.isMultiSelect}
              onClickHandler={this.handleTagListApplyButtonClicked}
              anchorOrigin={oAnchorOrigin}
              targetOrigin={oTargetOrigin}
              useAnchorElementWidth={true}
              excludedItems={aTagIdsToExclude}
              menuListHeight={this.props.menuListHeight}
              requestResponseInfo={this.props.requestResponseInfo}>
            <TooltipView placement="top" label={getTranslation().ADD_TAGS}>
              <div className="addItemHandler" onClick={this.handlePopoverVisibility}>
                {getTranslation().ADD_TAGS}
              </div>
            </TooltipView>
          </LazyContextMenuView>
        </div>
    );
    // }
  }

  render() {

    return (
        <div className="tagSelectorView">
          {this.getSelectedTagGroupsListView()}
          {this.getAddTagGroupView()}
        </div>
    );
  }

}

TagSelectorView.propTypes = oPropTypes;

export const view = TagSelectorView;
export const events = oEvents;
