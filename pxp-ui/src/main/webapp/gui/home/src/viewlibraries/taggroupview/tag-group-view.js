import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { view as TagNodeWrapperView } from './tag-node-wrapper-view.js';
import { view as EntityTagView } from './../entitytagsummaryview/entity-tags-summary-view.js';
import { view as UniqueSelectionView } from '../uniqueSelectionView/unique-selection-view.js';
import ViewUtils from '../utils/view-library-utils';
import { view as GridYesNoPropertyView } from '../gridview/grid-yes-no-property-view';
import { view as TagMSSView } from './tag-mss-view';
import LazyTagGroupViewStore from './store/lazy-tag-group-view-store';
import TagGroupModel from './model/tag-group-model';
import TooltipView from "./../tooltipview/tooltip-view";

const oEvents = {
  TAG_GROUP_TAG_VALUE_CHANGED: "tag_group_tag_value_changed",
};

const oPropTypes = {
  tagGroupModel: ReactPropTypes.instanceOf(TagGroupModel).isRequired,
  tagRanges: ReactPropTypes.object,
  tagValues: ReactPropTypes.array,
  disabled: ReactPropTypes.bool,
  singleSelect: ReactPropTypes.bool,
  hideTooltip: ReactPropTypes.bool,
  displayInGrid: ReactPropTypes.bool,
  showLabel: ReactPropTypes.bool,
  isDoubleSlider: ReactPropTypes.bool,
  extraData: ReactPropTypes.object,
  masterTagList: ReactPropTypes.oneOfType([ReactPropTypes.object, ReactPropTypes.array]),
  customPlaceholder: ReactPropTypes.string,
  onApply: ReactPropTypes.func,
  customRequestObject: ReactPropTypes.object,
  customDOMWidth: ReactPropTypes.number,
  showMoreButton: ReactPropTypes.bool
  //disableLazyLoading: ReactPropTypes.bool,
  //disableCrossIcon: ReactPropTypes.bool
};
/**
 * @class TagGroupView - use to dispaly Tags for adding tags in Application.
 * @memberOf Views
 * @property {custom} tagGroupModel - tagGroupModel.
 * @property {object} tagRanges  -  object which contain tagRanges.
 * @property {array} [tagValues] -  an array of tagValues.
 * @property {bool} [disabled] -  boolean value for disabled tagView or not.
 * @property {bool} [singleSelect] -  boolean value for singleSelect or multiple.
 * @property {bool} [hideTooltip] -  boolean value for hideTooltip or not.
 * @property {bool} [displayInGrid] -  boolean value for displayInGrid or not.
 * @property {bool} [showLabel] -  boolean value for showLabel or not.
 * @property {bool} [isDoubleSlider] -  boolean value for isDoubleSlider or not.
 * @property {object} [extraData] -  object which contain extraData.
 * @property {custom} [masterTagList] -  array of masterTagList.
 * @property {string} [customPlaceholder] -  string which contain customPlaceholder.
 * @property {func} [onApply] -  function which used onApply event.
 * @property {object} [customRequestObject] -  object which contain customRequestObject.
 */

// @CS.SafeComponent
class TagGroupView extends React.Component{

  constructor(props) {
    super(props);
    this.state = {
      store: this.initializeStore()
    }
  }

  shouldComponentUpdate =(oNextProps, oNextState)=> {
    return true; //DO something of TagGroupModel Model
  };

  initializeStore = (oNextProps) => {
    let oProps = oNextProps;
    if (!oProps) {
      oProps = this.props;
    }
    let oReferencedTags = {};
    if (CS.isArray(oProps.masterTagList)) {
      CS.forEach(oProps.masterTagList, function (oMasterTag) {
        oReferencedTags[oMasterTag.id] = oMasterTag;
      })
    } else {
      oReferencedTags = oProps.masterTagList;
    }
    return new LazyTagGroupViewStore(oReferencedTags, this.props.customRequestObject, this.stateChanged);
  };

  stateChanged = () => {
    this.setState({});
  };

  static getDerivedStateFromProps (oNextProps, oState) {
    let oMasterTagsList = {};
    let oNextPropsMasterTagList = oNextProps.masterTagList;
    if (CS.isArray(oNextPropsMasterTagList)) {
      CS.forEach(oNextPropsMasterTagList, function (oMasterTag) {
        oMasterTagsList[oMasterTag.id] = oMasterTag;
      })
    } else {
      oMasterTagsList = oNextPropsMasterTagList;
    }
    oState.store.updateChildrenInReferencedTagFromMasterReferencedTag(oMasterTagsList);
    return null;
  }

  handleTagGroupTagValueChanged = (aTagValueRelevanceData, oExtraData) => {
    let oTagGroupModel = this.props.tagGroupModel;
    let oEntityTag = oTagGroupModel.entityTag;
    let sTagGroupId = oTagGroupModel.tagId;
    let aChildrenModels = oEntityTag.tagValues;

    let aTagValueRelevanceDataClone = CS.cloneDeep(aTagValueRelevanceData);

    CS.forEach(aTagValueRelevanceData, function (oTagData) {
      let oTagValueFromModel = CS.find(aChildrenModels, {tagId: oTagData.tagId});
      if (oTagValueFromModel && oTagValueFromModel.relevance === oTagData.relevance) {
        CS.remove(aTagValueRelevanceDataClone, {tagId: oTagData.tagId});
      }
    });

    let aOldTagsIds = CS.map(aChildrenModels, function (oChild) {
      if (oChild.relevance !== 0) {
        return oChild.tagId;
      } else {
        return "";
      }
    });
    CS.pull(aOldTagsIds, "");
    let aNewTagIds = CS.map(aTagValueRelevanceData, "tagId");
    if (CS.isEmpty(aTagValueRelevanceDataClone) && CS.isEmpty(CS.difference(aOldTagsIds, aNewTagIds))) {
      return;
    }

    this.state.store.updateChildrenInReferencedTag(sTagGroupId, aTagValueRelevanceData);

    if (CS.isFunction(this.props.onApply)) {
      this.props.onApply(sTagGroupId, aTagValueRelevanceData, oExtraData);
    } else {
      EventBus.dispatch(oEvents.TAG_GROUP_TAG_VALUE_CHANGED, sTagGroupId, aTagValueRelevanceData, oExtraData);
    }
  }

  handleTagGroupMSSValueChanged =(aCheckedItems)=> {
    var _props = this.props;
    var oTagGroupModel = _props.tagGroupModel;
    var oExtraData = _props.extraData || {};
    var aTagValueRelevanceData = [];

    CS.forEach(aCheckedItems, function (sItem) {
      aTagValueRelevanceData.push({
        tagId: sItem,
        relevance: 100
      });
    });

    this.setContextDataInExtraData(oTagGroupModel, oExtraData);
    this.handleTagGroupTagValueChanged(aTagValueRelevanceData, oExtraData);
  }

  handleTagSummaryViewCrossIconClicked =(sTagId)=> {
    let oTagValueObj = {
      tagId: sTagId,
      relevance: 0
    };
    this.handleTagNodeValueChanged(oTagValueObj);
  }

  handleTagNodeValueChanged =(oTagValueObj)=> {
    let __props = this.props;
    let oExtraData = this.props.extraData || {};
    let oTagGroupModel = __props.tagGroupModel;
    let aTagValueRelevanceData = [];
    // To check if new tag value has been added
    let bShouldCreateNewTagValueObj = true;
    let oEntityTag = oTagGroupModel.entityTag;
    let sTagViewType = oTagGroupModel.properties['tagViewType'];

    CS.forEach(oEntityTag.tagValues, function (oTagValue) {
      let sTagId = oTagValue.tagId;
      let iTagRelevance = (sTagViewType != "doubleSlider") ? oTagValue.relevance : ViewUtils.getTagRelevanceForDoubleSlider(oTagValue);

      if(sTagId === oTagValueObj.tagId) {
        iTagRelevance = oTagValueObj.relevance;
        // If tag value was already existing and only its relevance was modified, then no need to create new tag values object
        bShouldCreateNewTagValueObj = false;
      }

      if (iTagRelevance) {
        let oRelevanceData = {
          tagId: sTagId,
          relevance: iTagRelevance
        };

        //In case of Double Slider, 'iTagRelevance' is an object containing 'to' & 'from' key-values
        if (CS.isObject(iTagRelevance)) {
          oRelevanceData.to = iTagRelevance.to || 0;
          oRelevanceData.from = iTagRelevance.from || 0;
          oRelevanceData.relevance = 0;
        }

        if (!CS.isEmpty(oTagValueObj.key) && sTagId === oTagValueObj.tagId) {
          oRelevanceData[oTagValueObj.key] = iTagRelevance;
        }

        aTagValueRelevanceData.push(oRelevanceData);
      }
    });

    // If below boolean value is still true, then push a new object of this newly added tag value
    if (bShouldCreateNewTagValueObj) {
      aTagValueRelevanceData.push(
          {
            tagId: oTagValueObj.tagId,
            relevance: oTagValueObj.relevance
          }
      );
    }

    this.setContextDataInExtraData(oTagGroupModel, oExtraData);
    this.handleTagGroupTagValueChanged(aTagValueRelevanceData, oExtraData);
  }

  handleGroupContainerClicked =(oEvent)=> {
    /** Avoid event bubbling[parent container clicked]..
     * On parent container clicked, container collapsed to show selected tag.
     * */
    if (!(this.props.disabled || (oEvent && oEvent.nativeEvent.dontRaise))) {
      this.handleTagValuesSearchClicked("", true);
    }
  }

  handleRulerTagValueChanged =(sTagValueId)=> {
    let aTagValueRelevanceData = [];
    aTagValueRelevanceData.push({
      tagId: sTagValueId,
      relevance: 100
    });

    let oTagGroupModel = this.props.tagGroupModel;
    let oExtraData = this.props.extraData || {};
    this.setContextDataInExtraData(oTagGroupModel, oExtraData);
    this.handleTagGroupTagValueChanged(aTagValueRelevanceData, oExtraData);
  }

  handleBooleanTagValueChanged =(oTagValue, oTagGroupModel, bValue)=> {
    let aTagValueRelevanceData = [];
    let oExtraData = this.props.extraData || {};

    aTagValueRelevanceData.push({
      tagId: oTagValue.tagId,
      relevance: bValue ? 100 : 0
    });

    this.setContextDataInExtraData(oTagGroupModel, oExtraData);
    this.handleTagGroupTagValueChanged(aTagValueRelevanceData, oExtraData);
  }

  setContextDataInExtraData =(oTagGroupModel, oExtraData)=> {
    oExtraData.modelContext = oTagGroupModel.properties["context"];
    if (CS.isEmpty(oExtraData.outerContext)) {
      let sSplitter = ViewUtils.getSplitter();
      let aContextKey = oExtraData.modelContext.split(sSplitter);
      oExtraData.outerContext = aContextKey[0];
      oExtraData.elementId = aContextKey[2];
    }
  }

  getDataForMultiSelect =(aTagValuesList)=> {
    var __this = this;
    var __props = __this.props;
    var oTagGroupModel = __props.tagGroupModel;
    var aSelectedItems = [];

    let oEntityTag = oTagGroupModel.entityTag;
    CS.forEach(oEntityTag.tagValues, function (oTagValue) {
      if(oTagValue.relevance !== 0){
        aSelectedItems.push(oTagValue.tagId);
      }
    });

    return {
      items: aTagValuesList,
      selectedItems: aSelectedItems
    }
  }

  handleTagValuesSearchClicked =(sSearchText, bIsInitialSearch)=> {
    let oTagGroupModel = this.props.tagGroupModel;
    let sTagGroupId = oTagGroupModel.tagId;
    let oExtraData = this.props.extraData || {};
    this.setContextDataInExtraData(oTagGroupModel, oExtraData);
    oExtraData.isInitialSearch = bIsInitialSearch;
    this.state.store.handleSearchClicked(sTagGroupId, sSearchText, oExtraData);
  }

  handleTagValuesLoadMoreClicked =()=> {
    let oTagGroupModel = this.props.tagGroupModel;
    let sTagGroupId = oTagGroupModel.tagId;
    let oExtraData = this.props.extraData || {};
    this.setContextDataInExtraData(oTagGroupModel, oExtraData);
    this.state.store.handleLoadMoreClicked(sTagGroupId, oExtraData);
  }

  handleMSSItemsChecked = (aCheckedItems) => {
    let oTagGroupModel = this.props.tagGroupModel;
    let sTagGroupId = oTagGroupModel.tagId;
    let aTagValueRelevanceData = [];

    CS.forEach(aCheckedItems, function (sCheckedItemId) {
      aTagValueRelevanceData.push({
        tagId: sCheckedItemId,
        relevance: 100
      });
    });

    this.state.store.updateChildrenInReferencedTag(sTagGroupId, aTagValueRelevanceData);
  }

  getTagView =()=> {
    var __this = this;
    var __props = __this.props;
    var oView = null;
    var oTagGroupModel = __props.tagGroupModel;
    var oExtraData = __props.extraData;
    var sTagViewType = oTagGroupModel.properties['tagViewType'];
    var oStore = this.state.store;
    let bShowMoreButton = __props.hasOwnProperty("showMoreButton") ? this.props.showMoreButton : true;
    let oReferencedTags = oStore.getReferencedData();
    let oMasterTag = CS.find(oReferencedTags, {id: oTagGroupModel.tagId});
    let oToolTip = CS.isEmpty(oTagGroupModel.tooltip) ? null : 
    (<div className="sectionTooltipWrapper"><span>{oTagGroupModel.tooltip}</span></div>);

    if (sTagViewType == "radioGroup" || sTagViewType == "slider" || sTagViewType == "doubleSlider") {
      var oEntityTag = oTagGroupModel.entityTag;
      var aTags = oEntityTag ? [oEntityTag] : [];
      var aTagValuesList = oStore.getItemsData();
      let oReferencedTags = oStore.getReferencedData();

      let oChildView =
        <TooltipView label={oToolTip || ""} placement="bottom">
          <div className="tagsGroupContainer" onClick={__this.handleGroupContainerClicked}>
            <div className="tagsSectionBody">
              <EntityTagView
                  key={this.props.key}
                  tags={aTags}
                  masterTagList={oReferencedTags}
                  disabled={__props.disabled}
                  isCrossIconEnabled={!__props.disabled}
                  onCrossIconClicked={__this.handleTagSummaryViewCrossIconClicked}
                  showMoreButton={bShowMoreButton}
                  isDoubleSlider={__props.isDoubleSlider}
                  customPlaceholder={__props.customPlaceholder}
                  showDefaultIcon={this.props.showDefaultIcon}
              />
            </div>
          </div>
        </TooltipView>;

      oView =
          <TagNodeWrapperView
              tagGroupModel={oTagGroupModel}
              tagRanges={__props.tagRanges}
              tagValues={__props.tagValues}
              extraData={oExtraData}
              childView={oChildView}
              onApply={__this.handleTagNodeValueChanged}
              searchHandler={__this.handleTagValuesSearchClicked}
              loadMoreHandler={__this.handleTagValuesLoadMoreClicked}
              disabled={__props.disabled}
              tagValuesList={aTagValuesList}
              masterTag = {oMasterTag}
              showDefaultIcon={this.props.showDefaultIcon}
          />;
    }
    else if (sTagViewType == "ruler") {
      oView = (
        <TooltipView label={oToolTip || ""} placement="bottom">
          <div className="tagsGroupContainer">
            <UniqueSelectionView
                rulerTagGroupModel={oTagGroupModel}
                masterTagList={oReferencedTags}
                isDisabled={__props.disabled}
                onChange={__this.handleRulerTagValueChanged}
                extraData={oExtraData}
                showDefaultIcon={this.props.showDefaultIcon}
            />
          </div>
        </TooltipView>
      );
    }
    else if (sTagViewType == "switch") {
      var aTagNodeView = [];
      let oEntityTag = oTagGroupModel.entityTag;
      let aTagValues = oEntityTag.tagValues;
      if (CS.isNotEmpty(aTagValues)) {
        var bValue = false;
        CS.forEach(aTagValues, function (oTagValue) {
          if (oTagValue.relevance > 0) {
            bValue = true;
          }
          aTagNodeView.push(
              <GridYesNoPropertyView
                  value={bValue}
                  isDisabled={__this.props.disabled}
                  onChange={__this.handleBooleanTagValueChanged.bind(__this, oTagValue, oTagGroupModel)}
              />
          );
        });
      }
      oView = (
        <TooltipView label={oToolTip || ""} placement="bottom">
          <div className="tagsGroupContainer">
            {aTagNodeView}
          </div>
        </TooltipView>
      );
    }
    else {
      var aTagValuesList = oStore.getItemsData();
      var oMultiSelectItems = __this.getDataForMultiSelect(aTagValuesList);
      var sLabel = CS.getLabelOrCode(oTagGroupModel);
      var aItems = oMultiSelectItems.items;
      var aSelectedItems = oMultiSelectItems.selectedItems;
      var bSingleSelect = __props.singleSelect;
      var bHideTooltip = __props.hideTooltip;
      let oEntityTag = oTagGroupModel.entityTag;
      let aTags = oEntityTag ? [oEntityTag] : [];
      let oReferencedTags = oStore.getReferencedData();
      let oOnClickHandler = __this.handleGroupContainerClicked;
      if(__this.props.disableLazyLoading) {
        oOnClickHandler = CS.noop;
      }

      let oChildView =
        <TooltipView label={oToolTip || ""} placement="bottom">
          <div className="tagsSectionBody" onClick={__this.handleGroupContainerClicked}>
            <EntityTagView
                key={this.props.key}
                tags={aTags}
                masterTagList={oReferencedTags}
                disabled={__props.disabled}
                isCrossIconEnabled={!__props.disabled}
                onCrossIconClicked={__this.handleTagSummaryViewCrossIconClicked}
                showMoreButton={bShowMoreButton}
                isDoubleSlider={__props.isDoubleSlider}
                customPlaceholder={__props.customPlaceholder}
                customDOMWidth={__props.customDOMWidth}
                showDefaultIcon ={this.props.showDefaultIcon}
            />
          </div>
        </TooltipView>;

      oView = (
          <div className="tagsGroupContainer">
            <TagMSSView
                disabled={__props.disabled}
                showLabel={__props.showLabel}
                label={sLabel}
                items={aItems}
                selectedItems={aSelectedItems}
                isMultiSelect={!bSingleSelect}
                onApply={__this.handleTagGroupMSSValueChanged}
                showColor={true}
                bShowIcon={true}
                hideTooltip={bHideTooltip}
                searchHandler={__this.handleTagValuesSearchClicked}
                loadMoreHandler={__this.handleTagValuesLoadMoreClicked}
                childView={oChildView}
                searchText={oStore.getSearchText()}
                onItemsChecked={__this.handleMSSItemsChecked}
                isSearchApplied={oStore.getIsSearchApplied()}
                showDefaultIcon ={this.props.showDefaultIcon}
            />
          </div>
      );
    }

    return oView;
  }

  render() {
    return this.getTagView();
  }
}

TagGroupView.propTypes = oPropTypes;

export const view = TagGroupView;
export const events = oEvents;
