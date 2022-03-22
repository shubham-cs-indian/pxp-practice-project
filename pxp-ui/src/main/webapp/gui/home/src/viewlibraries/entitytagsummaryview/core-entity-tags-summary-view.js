import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import TooltipView from '../tooltipview/tooltip-view';
import ViewUtils from '../utils/view-library-utils';
import { view as ImageFitToContainerView } from '../imagefittocontainerview/image-fit-to-container-view';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import { view as GridYesNoPropertyView } from '../gridview/grid-yes-no-property-view';
import TagTypeConstants from '../../commonmodule/tack/tag-type-constants';
import { view as CustomPopoverView } from '../../viewlibraries/customPopoverView/custom-popover-view';

const oEvents = {};

const oPropTypes = {
  tags: ReactPropTypes.array,
  elaborated: ReactPropTypes.bool,
  visibleItemCount: ReactPropTypes.number,
  masterTagList: ReactPropTypes.oneOfType([ReactPropTypes.object, ReactPropTypes.array]),
  showEmptyTagGroupLabel: ReactPropTypes.bool,
  disabled: ReactPropTypes.bool,
  isCrossIconEnabled: ReactPropTypes.bool,
  onCrossIconClicked: ReactPropTypes.func,
  showMoreButton: ReactPropTypes.bool,
  isDoubleSlider: ReactPropTypes.bool,
  customPlaceholder: ReactPropTypes.string,
  customDOMWidth: ReactPropTypes.number
};
/**
 * @class CoreEntityTagsSummaryView - Use to Display status tag in property collection.
 * @memberOf Views
 * @property {array} [tags] -  string of tags.
 * @property {bool} [elaborated] -  boolean value for elaborated.
 * @property {number} [visibleItemCount] - this prop is pass number of visibleItemCount.
 * @property {custom} [masterTagList] - pass multiple array like lifestatustag, listingstatustag, statustag.
 * @property {bool} [showEmptyTagGroupLabel] -  boolean value for showEmptyTagGroupLabel.
 * @property {bool} [disabled] -  boolean value for disabled true or false.
 * @property {bool} [isCrossIconEnabled] -  boolean value for isCrossIconEnabled true or false.
 * @property {func} [onCrossIconClicked] -  function which is used for onCrossIconClicked button event.
 * @property {bool} [showMoreButton] -  boolean value for showMoreButton true or not.
 * @property {bool} [isDoubleSlider] -  boolean value for isDoubleSlider true or not.
 * @property {string} [customPlaceholder] -  string of customPlaceholder.
 */

let noTagsToDisplay;
let aRefs;


class CoreEntityTagsSummaryView extends React.Component {

  constructor(props) {
    super(props);
    noTagsToDisplay = false;
    aRefs = [];

    this.state = {
      showMore: false
    }

    this.setRef =( sRef, element) => {
      this[sRef] = element;
      aRefs.push(element)
    };
  }

  componentDidMount() {
    this.calculateTagsMoreButtonCount();
  }

  componentDidUpdate() {
    this.calculateTagsMoreButtonCount();
  }

  calculateTagsMoreButtonCount() {
    let _this = this;
    let aTags = _this.props.tags;
   /* CS.forEach(aTags, function (oTag) {
      _this.calculateTagValuesCount(oTag.tagValues);
    });*/
    let aTagValues = [];
    CS.forEach(aTags, function (oTag) {
      oTag.tagValues = oTag.tagValues || [];
      aTagValues = CS.concat(aTagValues, oTag.tagValues);
    });

    _this.calculateTagValuesCount(aTagValues);
  }

  handleMoreItemsOnClick = ()=>{
    window.dontRaise = true; //Do not remove
  }

  handleMoreButtonClicked =(oEvent)=> {
    oEvent.nativeEvent.dontRaise = true;
    var bCurrentState = this.state.showMore;
    this.setState({
      showMore: !bCurrentState,
      moreView: oEvent.currentTarget
    });
  }

  handleCloseMorePopoverButtonClicked =()=> {
    var bCurrentState = this.state.showMore;
    this.setState({
      showMore: !bCurrentState
    });

  }

  calculateTagValuesCount =(aTagValues)=> {
    var _this = this;
    let iCustomDOMWidth = this.props.customDOMWidth;
    if (!CS.isEmpty(_this["tagsSummaryItemsContainer"]) && _this.props.showMoreButton) {
      var sSplitter = ViewUtils.getSplitter();

      if (_this["moreContainer"]) {
        _this["moreContainer"].classList.remove('hideMe');
      }
      var iWidthToSubtract = _this["moreContainer"].offsetWidth + 1;
      let oTagsSummaryItemsContainer = _this["tagsSummaryItemsContainer"];
      let iOffsetWidth = iCustomDOMWidth ? iCustomDOMWidth : Math.ceil(_this["tagsSummaryItemsContainer"].offsetWidth);
      let iTotalWidth = CS.isEmpty(oTagsSummaryItemsContainer) ? 0 : iOffsetWidth - iWidthToSubtract;
      var iSum = 0;
      var iMoreCounter = 0;
      var aAdjustableRefIds = [];

      CS.forEach(aTagValues, function (oTagValue, iIndex) {
        var oSelectedLabelDOM = _this[oTagValue.tagId + sSplitter + "selectedLabel"];
        var oSelectedIconDOM = _this[oTagValue.tagId + sSplitter + "tagIcon"];
        var oSelectedDOM = _this[oTagValue.tagId + sSplitter + "selected"];
        var oMoreDOM = _this[oTagValue.tagId + sSplitter + "more"];

        if (!CS.isEmpty(oSelectedDOM)) {
          oSelectedDOM.classList.remove('hideMe');
          //iTotalWidth = CS.isEmpty(oTagsSummaryItemsContainer) ? 0 :
          // Math.ceil(oTagsSummaryItemsContainer.offsetWidth) - iWidthToSubtract;
          var iSelectedIconWidth = oSelectedIconDOM && oSelectedIconDOM.offsetWidth ? oSelectedIconDOM.offsetWidth : 0;
          //selectedItemLabel margin(2tagValue0) + crossIcon width(14) + crossIcon margin(5) = 20 + 14 + 5 = 39
          iSum = iSum + oSelectedDOM.offsetWidth + 5;
        }

        if (iSum >= iTotalWidth && iIndex >= 0) {
          iMoreCounter++;
          oMoreDOM && oMoreDOM.classList.remove('hideMe');
          oSelectedDOM && oSelectedDOM.classList.add('hideMe');
          if (iSum < (iTotalWidth + Math.ceil(_this.moreContainer.offsetWidth + 1))) {
            aAdjustableRefIds.push(oTagValue.tagId + sSplitter);
          } else {
            aAdjustableRefIds = [];
          }
        } else if (iSum >= iTotalWidth && iIndex == 0) {
          oMoreDOM && oMoreDOM.classList.add('hideMe');
          if (iSum < (iTotalWidth + Math.ceil(_this.moreContainer.offsetWidth + 1)) && !iMoreCounter) {
            oSelectedDOM && oSelectedDOM.classList.remove('hideMe');
          } else {
            if (!_this.props.isCrossIconEnabled) {
              //selectedItemMargin(3) + labelMargin(20) = 24
              iWidthToSubtract = iSelectedIconWidth + 24;
            } else {
              //selectedItemMargin(3) + labelMargin(20) + crossIcon(14) + crossIconMargin(5) = 42
              iWidthToSubtract = iSelectedIconWidth + 42;
            }
            var iNewSelectedLabelDOMWidth = iTotalWidth - iWidthToSubtract;
            oSelectedLabelDOM.style.width = iNewSelectedLabelDOMWidth + "px";
            //iSum = iSum - (oSelectedLabelDOM.offsetWidth - iNewSelectedLabelDOMWidth);
          }
        } else {
          oMoreDOM && oMoreDOM.classList.add('hideMe');
        }
      });

      if (_this.moreContainer && _this.moreCounter) {
        if (!iMoreCounter) {
          _this.moreContainer.classList.add('hideMe');

          //To hide un-mount more count popover if it is already mounted and there is nothing to show
          if (_this.state.showMore) {
            _this.setState({showMore: false});
          }

        } else if (!CS.isEmpty(aAdjustableRefIds)) {
          CS.forEach(aAdjustableRefIds, function (sRefId) {
            var oSelectedDOM = _this[sRefId + "selected"];
            var oMoreDOM = _this[sRefId + "more"];
            oSelectedDOM && oSelectedDOM.classList.remove('hideMe');
            oMoreDOM && oMoreDOM.classList.add('hideMe');
          });
          //To hide un-mount more count popover if it is already mounted and there is nothing to show
          if (_this.state.showMore) {
            _this.setState({showMore: false});
          }
          _this.moreContainer.classList.add('hideMe');
        } else {
          _this.moreContainer.classList.remove('hideMe');
          _this.moreCounter.innerHTML = iMoreCounter;
        }
      }

      _this.updateMoreCounterValue(aTagValues);
      this.props.hasOwnProperty("adjustRowHeight") && this.props.adjustRowHeight();
    }
  }

  updateMoreCounterValue =(aTagValues)=> {
    let _this = this;
    let iMoreCounter = 0;
    let sSplitter = ViewUtils.getSplitter();

    CS.forEach(aTagValues, function (oTagValue) {
      let oSelectedDOM = _this[oTagValue.tagId + sSplitter + "selected"];
      if (oSelectedDOM && oSelectedDOM.classList.contains("hideMe")) {
        iMoreCounter++;
      }
    });

    if (iMoreCounter > 0) {
      _this.moreContainer.classList.remove('hideMe');
      _this.moreCounter.innerHTML = iMoreCounter;
    } else {
      if (!_this.moreContainer.classList.contains("hideMe")) {
        _this.moreContainer.classList.add('hideMe');
      }
    }
  }

  getMoreSectionView =(aMoreFilterItems)=> {
    if(!this.props.showMoreButton){
      return null;
    }

    var sMoreFiltersSectionClassName = this.state.showMore ? "moreFilters" : "moreFilters invisible";
    return (
        <div className="moreSection" ref={this.setRef.bind(this,"moreContainer")} onClick={this.handleMoreButtonClicked}>
          <div className="moreLabel" ref={this.setRef.bind(this,"moreCounter")}></div>
          <CustomPopoverView
              className="popover-root"
              open={this.state.showMore}
              anchorEl={this.state.moreView}
              anchorOrigin={{horizontal: 'right', vertical: 'bottom'}}
              transformOrigin={{horizontal: 'right', vertical: 'top'}}
              onClose={this.handleCloseMorePopoverButtonClicked}
          >
            <div className={sMoreFiltersSectionClassName}>{aMoreFilterItems}</div>
          </CustomPopoverView>
        </div>
    );
  }

  handleCrossIconClicked =(sTagId, oEvent)=> {
    if (CS.isFunction(this.props.onCrossIconClicked)) {
      this.props.onCrossIconClicked(sTagId);
      oEvent.nativeEvent.dontRaise = true;
    }
  }

  getTagValuesViewForGroup =(aTagValues, oTagGroupMaster)=> {
    var _this = this;
    var aWithIconViews = [];
    var aWithoutIconViews = [];
    var aMoreFilterItems = [];
    var sSplitter = ViewUtils.getSplitter();
    var aMasterTagList = _this.props.masterTagList || _this.props.masterTagListFromContext;

    let oTagListDOMsWithClassName = aRefs;
    let aNotVisibleItemInChipsIdList = [];
    CS.forEach(oTagListDOMsWithClassName, function (oTagValueDOM, sKey) {
      if (oTagValueDOM && (!CS.includes(oTagValueDOM.classList, "hideMe") && CS.includes(oTagValueDOM.classList, "selectedItems"))) {
        aNotVisibleItemInChipsIdList.push(CS.split(sKey, sSplitter)[0]);
      }
    });

    CS.forEach(aTagValues, function (oTagValue, iIndex) {
      var oTagValueMaster = ViewUtils.getNodeFromTreeListById(aMasterTagList, oTagValue.tagId);
      if (!CS.isEmpty(oTagValueMaster)) {
        if ((!_this.props.isDoubleSlider && oTagValue.relevance != 0 && oTagGroupMaster.tagType != TagTypeConstants.TAG_TYPE_BOOLEAN)
            || (_this.props.isDoubleSlider && (oTagValue.to != 0 || oTagValue.from != 0))) {
          _this.getTagValueView(oTagValue, oTagValueMaster, iIndex, aNotVisibleItemInChipsIdList, aWithoutIconViews,  aMoreFilterItems);

        }
        else if((!_this.props.isDoubleSlider && oTagGroupMaster.tagType === TagTypeConstants.TAG_TYPE_BOOLEAN)
            || (_this.props.isDoubleSlider && (oTagValue.to != 0 || oTagValue.from != 0))){
          _this.getBooleanTagView(oTagValue, aWithoutIconViews, oTagValueMaster, iIndex, aNotVisibleItemInChipsIdList, aMoreFilterItems, oTagGroupMaster);
        }
      }
    });

    if(!CS.isEmpty(aTagValues) && CS.isEmpty(aWithIconViews) &&
        CS.isEmpty(aWithoutIconViews) && _this.props.showEmptyTagGroupLabel) {
      let sPlaceholder = this.props.customPlaceholder || getTranslation().NOTHING_IS_SELECTED;
      aWithIconViews.push(<div className="noTagsGrey showEmptyTagGroupLabel">{sPlaceholder}</div>);
    }

    return {
      withIcons: aWithIconViews,
      withoutIcons: aWithoutIconViews,
      moreFilterItems: aMoreFilterItems
    }
  };

  getBooleanTagView = (oTagValue, aWithoutIconViews) => {
    let bIsTagOn = oTagValue.relevance != 0;
    let oBooleanTagView = (<GridYesNoPropertyView
        isDisabled={true}
        value={bIsTagOn}
    />);
    aWithoutIconViews.push(oBooleanTagView);
  }

  getTagValueView = (oTagValue, oTagValueMaster, iIndex, aNotVisibleItemInChipsIdList, aWithoutIconViews, aMoreFilterItems) => {
    let _this = this;
    let sSplitter = ViewUtils.getSplitter();
    let sColorClass = (oTagValue.relevance > 0) ? "greenBorder" : "redBorder";
    let oRangeValueDOM = null;
    let sToLabel = getTranslation().TO;
    sToLabel = sToLabel.toLowerCase();

    if (_this.props.isDoubleSlider) {
      sColorClass = "";
      let oFromStyle = (oTagValue.from < 0) ? {"color": "red"} : ((oTagValue.from == 0) ? {"color": "black"} : {"color": "green"});
      let oToStyle = (oTagValue.to < 0) ? {"color": "red"} : ((oTagValue.to == 0) ? {"color": "black"} : {"color": "green"});
      oRangeValueDOM = (<span>
              {": "}<span style={oFromStyle}>{oTagValue.from}</span>
        {" " + sToLabel + " "}<span style={oToStyle}>{oTagValue.to}</span>
            </span>);
    }

    var sLabel = /*oTagGroupMaster.label + ": " + */CS.getLabelOrCode(oTagValueMaster);
    if (!CS.isEmpty(oTagValueMaster.klass)) {
      sLabel += " (" + oTagValueMaster.klass + ")";
    }
    // var sTagName = CS.getLabelOrCode(oTagMaster);

    var aTagDetailView = [];
    if (oTagValueMaster.iconKey || this.props.showDefaultIcon) {
      var sIcon = ViewUtils.getIconUrl(oTagValueMaster.iconKey);
      aTagDetailView.push(<div className={"tagIconWrapper "} title={sLabel}
                               key={oTagValueMaster.id + "_overlay"}>
        <div className="tagIcon" ref={_this.setRef.bind(_this, oTagValue.tagId + sSplitter + "tagIcon")}>
          <ImageFitToContainerView imageSrc={sIcon}/>
        </div>
        <div className="tagText"
             ref={_this.setRef.bind(_this, oTagValue.tagId + sSplitter + "selectedLabel")}>{CS.getLabelOrCode(oTagValueMaster)}{oRangeValueDOM}</div>
      </div>);
    } else if (oTagValueMaster.color) {
      var oStyle = {};
      oStyle.backgroundColor = oTagValueMaster.color;
      aTagDetailView.push(<div className={"tagIconWrapper "} title={sLabel}
                               key={oTagValueMaster.id + "_overlay"}>
        <div className={"tagColor "} style={oStyle}></div>
        <div className="tagText"
             ref={_this.setRef.bind(_this, oTagValue.tagId + sSplitter + "selectedLabel")}>{CS.getLabelOrCode(oTagValueMaster)}{oRangeValueDOM}</div>
      </div>);
    } else if (oTagValueMaster.klass) {
      aTagDetailView.push(<div className={"tagValue"} title={sLabel}
                               ref={_this.setRef.bind(_this, oTagValue.tagId + sSplitter + "selectedLabel")}
                               key={oTagValueMaster.id + "_overlay"}>{oTagValueMaster.klass}{oRangeValueDOM}</div>);
    } else {
      aTagDetailView.push(<div className={"tagValue"} title={sLabel}
                               ref={_this.setRef.bind(_this, oTagValue.tagId + sSplitter + "selectedLabel")}
                               key={oTagValueMaster.id + "_overlay"}>{CS.getLabelOrCode(oTagValueMaster)}{oRangeValueDOM}</div>);
    }

    var oCrossIconView = (!_this.props.disabled && _this.props.isCrossIconEnabled) ?
                         (<div className="crossIcon"
                               onClick={_this.handleCrossIconClicked.bind(_this, oTagValue.tagId)}></div>) : null;

    var oColorIndicatorView = !_this.props.isDoubleSlider ? (
        <div className={"coloredIndicator " + sColorClass}><span className="relevanceFlag"></span>
        </div>) : null;

    aWithoutIconViews.push(
        <div className="selectedItems" ref={_this.setRef.bind(_this, oTagValue.tagId + sSplitter + "selected")}
             key={"selectedItems" + oTagValue.tagId}>
          <div className="tagContainer" key={iIndex}>
            {oColorIndicatorView}
            {aTagDetailView}
            {oCrossIconView}
          </div>
        </div>
    );

    if (_this.props.showMoreButton && !(CS.includes(aNotVisibleItemInChipsIdList, oTagValueMaster.id))) {
      let oCrossButtonDOM = this.props.isCrossIconEnabled ?
                            (<div className="crossIcon" onClick={_this.handleCrossIconClicked.bind(_this, oTagValue.tagId)}>
                            </div>) : null;
      aMoreFilterItems.push(
          <div className="moreFiltersItemContainer"
               key={oTagValue.tagId}
               ref={_this.setRef.bind(_this, oTagValue.tagId + sSplitter + "more")}
               onClick={_this.handleMoreItemsOnClick}>
            <TooltipView label={sLabel} placement="bottom">
              <div className="moreFiltersItemLabel">{sLabel}</div>
            </TooltipView>
            {oCrossButtonDOM}
          </div>
      );
    }
  }

  getView =()=> {

    var _this = this;
    var aTagGroups = this.props.tags;
    var aWithIconViews = [];
    var aWithoutIconViews = [];
    var aMoreFilterItems = [];

    CS.forEach(aTagGroups, function(oTagGroup){
      var aTagValues = oTagGroup.tagValues;
      var aMasterTagList = _this.props.masterTagList || _this.props.masterTagListFromContext;
      var oTagGroupMaster = ViewUtils.getNodeFromTreeListById(aMasterTagList, oTagGroup.tagId);

      var oViews = _this.getTagValuesViewForGroup(aTagValues, oTagGroupMaster);
      aWithIconViews = aWithIconViews.concat(oViews.withIcons);
      aWithoutIconViews = aWithoutIconViews.concat(oViews.withoutIcons);
      aMoreFilterItems = aMoreFilterItems.concat(oViews.moreFilterItems);
    });

    if(!aWithIconViews.length && !aWithoutIconViews.length){
      noTagsToDisplay = true;
      let sPlaceholder = this.props.customPlaceholder || getTranslation().NOTHING_IS_SELECTED;
      return (<div className="noTagsGrey">{sPlaceholder}</div>);
    } else {
      noTagsToDisplay = false;
      var aTotalVisibleItems = aWithIconViews.concat(aWithoutIconViews);
      if(this.props.visibleItemCount){
        aTotalVisibleItems = CS.take(aTotalVisibleItems, this.props.visibleItemCount);
      }
      return (<div className="tagsSummaryItemsContainer" ref={this.setRef.bind(this,"tagsSummaryItemsContainer")}>
        {aTotalVisibleItems}
        {this.getMoreSectionView(aMoreFilterItems)}
      </div>);
    }
  }

  getElaborateView =()=> {

    var _this = this;
    var aTagGroups = this.props.tags;
    var aGroupViews = [];
    var sShowEmptyTagGroupLabelClass = this.props.showEmptyTagGroupLabel ? "showEmptyTagGroupLabel" : "";

    CS.forEach(aTagGroups, function(oTagGroup){
      var aTagValues = oTagGroup.tagValues;
      var aMasterTagList = _this.props.masterTagList;
      var oTagGroupMaster = ViewUtils.getNodeFromTreeListById(aMasterTagList, oTagGroup.tagId);
      var oViews = _this.getTagValuesViewForGroup(aTagValues, oTagGroupMaster);

      if(oViews.withIcons.length || oViews.withoutIcons.length) {
        aGroupViews.push(<div className={"tagsSummaryGroupContainer " + sShowEmptyTagGroupLabelClass} >
          <div className="tagsSummaryGroupHeader">{CS.getLabelOrCode(oTagGroupMaster)}</div>
          <div className="tagsSummaryGroupBody">
            {oViews.withIcons}
            {oViews.withoutIcons}
          </div>
        </div>);
      }
    });

    if (!aGroupViews.length){
      noTagsToDisplay = true;
      let sPlaceholder = this.props.customPlaceholder || getTranslation().NOTHING_IS_SELECTED;
      return (<div className="noTagsGrey">{sPlaceholder}</div>);
    } else {
      noTagsToDisplay = false;
      return (<div className="tagsSummaryItemsContainer" ref={this.setRef.bind(this,"tagsSummaryItemsContainer")}>{aGroupViews}</div>);
    }
  }

  render() {

    var oView = this.props.elaborated ? this.getElaborateView() : this.getView();

    return (
        <div className="entityTagsSummaryView">
          {oView}
        </div>);
  }
}



CoreEntityTagsSummaryView.propTypes = oPropTypes;

export const view = CoreEntityTagsSummaryView;
export const events = oEvents;
