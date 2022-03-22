/**
 * Created by CS31 on 22-11-2016.
 */

import CS from '../../libraries/cs';

import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { view as ThumbnailView } from '../thumbnailview2/thumbnail-view.js';
import TooltipView from './../tooltipview/tooltip-view';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import ViewUtils from '../utils/view-library-utils';
const oEvents = {
  THUMBNAIL_SCROLLER_LOAD_MORE: "thumbnail_scroller_load_more",
};

const oPropTypes = {
  thumbnailModels: ReactPropTypes.array,
  elementId: ReactPropTypes.string,
  relationshipProps: ReactPropTypes.object,
  activeIndex: ReactPropTypes.number,
  onChangeActiveItem: ReactPropTypes.func
};
/**
 * @class ThumbnailScroller
 * @memberOf Views
 * @property {array} [thumbnailModels]
 * @property {string} [elementId]
 * @property {object} [relationshipProps]
 * @property {number} [activeIndex]
 * @property {func} [onChangeActiveItem]
 */

// @CS.SafeComponent
class ThumbnailScroller extends React.Component {

  static propTypes = oPropTypes;

  constructor(props) {
    super(props)

    this.state = {
      activeIndex: 0
    }

    this.setRef = ( sRef, element) => {
      this[sRef] = element;
    };
  }

  componentDidMount() {
    this.repositionContainer();
  }

  componentDidUpdate() {
    this.repositionContainer();
  }

  static getDerivedStateFromProps (oNextProps, oState) {
    let iActiveIndex = oNextProps.activeIndex;
    return {activeIndex: iActiveIndex};
  }

  /*componentWillReceiveProps(oNextProps) {
    var iActiveIndex = oNextProps.activeIndex;
    this.setState({activeIndex: iActiveIndex});
  }*/

  repositionContainer =()=> {
    var _this = this;
    var aThumbnailModels = _this.props.thumbnailModels;
    var iTotalCount = aThumbnailModels.length;
    var iActiveIndex = _this.state.activeIndex || 0;
    iActiveIndex = iActiveIndex >= iTotalCount ? (iTotalCount - 1) : iActiveIndex;

    var oParentDOM = _this["thumbnailContainer"];
    var sActiveItemRef = "scrollerItem" + ViewUtils.getSplitter() + iActiveIndex;
    var oActiveDOM = _this[sActiveItemRef];

    var oGrandParentDOM = _this["thumbnailScrollerContainer"];
    var oGrandParentDimension = oGrandParentDOM.getBoundingClientRect();
    var iGrandParentWidth = oGrandParentDimension.width;

    CS.forEach(aThumbnailModels, function (oModel, iIndex){
      var sItemRef = "scrollerItem" + ViewUtils.getSplitter() + iIndex;
      var oDOM = _this[sItemRef];
      oDOM.style.width = iGrandParentWidth + "px";
    });

    if(iTotalCount) {
      var oItemDimension = oActiveDOM.getBoundingClientRect();
      var iHeightOfActiveDOM = oItemDimension.height;
      var iWidthOfActiveDOM = oItemDimension.width;

      oParentDOM.style.height = iHeightOfActiveDOM + "px";
      oParentDOM.style.width = (iWidthOfActiveDOM * iTotalCount)+ "px";

      var iLeftOfActiveDOM = oActiveDOM.offsetLeft;
      oParentDOM.style.transform =  "translateZ(0px) translateX(-" + iLeftOfActiveDOM + "px)";
    }
  }

  moveNext =()=> {
    var aThumbnailModels = this.props.thumbnailModels;
    var sElementId = this.props.elementId;
    var iTotalCount = aThumbnailModels.length;
    var iActiveIndex = this.state.activeIndex;

    if(iActiveIndex >= iTotalCount - 1) {
      EventBus.dispatch(oEvents.THUMBNAIL_SCROLLER_LOAD_MORE, sElementId, "NEXT");
      iActiveIndex = 0;

      if(this.props.onChangeActiveItem) {
        this.props.onChangeActiveItem(iActiveIndex);
      }
      return;
    } else {
      iActiveIndex++;
    }

    if(this.props.onChangeActiveItem) {
      this.props.onChangeActiveItem(iActiveIndex);
    }

    this.setState({
      activeIndex: iActiveIndex
    });
  }

  movePrevious =()=> {
    var sElementId = this.props.elementId;
    var iActiveIndex = this.state.activeIndex;
    if(iActiveIndex <= 0){
      EventBus.dispatch(oEvents.THUMBNAIL_SCROLLER_LOAD_MORE, sElementId, "PREVIOUS");
      var oCurrentRelationshipProps = this.props.relationshipProps;
      iActiveIndex = oCurrentRelationshipProps.paginationLimit - 1;

      if(this.props.onChangeActiveItem) {
        this.props.onChangeActiveItem(iActiveIndex);
      }
      return;
    } else {
      iActiveIndex--;
    }

    if(this.props.onChangeActiveItem) {
      this.props.onChangeActiveItem(iActiveIndex);
    }

    this.setState({
      activeIndex: iActiveIndex
    });
  }

  getThumbnails =()=> {
    var __props = this.props;
    let _this = this;
    var aThumbnailModels = __props.thumbnailModels;
    var iActiveIndex = this.state.activeIndex;
    var aThumbnailViews = [];
    CS.forEach(aThumbnailModels, function (oThumbnailModel, iIndex) {

      var sItemClassName = "scrollerItem ";
      sItemClassName += iIndex == iActiveIndex ? "active" : "";

      aThumbnailViews.push(
          <div className={sItemClassName} key={iIndex} ref={_this.setRef.bind(_this,"scrollerItem" + ViewUtils.getSplitter() + iIndex)}>
            <ThumbnailView model={oThumbnailModel}
                           currentZoom={3}/>
          </div>);
    });
    return aThumbnailViews;
  }

  getLoadMoreView =()=>{
    var aThumbnailModels = this.props.thumbnailModels;
    if(CS.isEmpty(aThumbnailModels)){
      return null;
    }

    var oCurrentRelationshipProps = this.props.relationshipProps;
    var iStartIndex = oCurrentRelationshipProps.startIndex || 0;
    var aThumbModelLength = aThumbnailModels.length;
    var iActiveIndex = this.state.activeIndex;

    var bPreviousDisabled = iStartIndex <= 0 && iActiveIndex <= 0;
    var sPreviousButtonVisibility = bPreviousDisabled ? " disabled" : "";

    /*var oNavigationData = this.props.navigationData;
    var sCurrentItems = oNavigationData.from + "-" + oNavigationData.to;
    var sTotalItems = oNavigationData.totalContents;

    if (sTotalItems === 0) {
      return null;
    }*/

    return (
        <div className="navigationWrapper">
          {/*<div className="navigationInfoContainer">
            <div className="navigationInfo">
              <div className="currentItems">{sCurrentItems}</div>
              <div className="of">&nbsp;{getTranslation().OF}&nbsp;</div>
              <div className="totalItems">{sTotalItems}</div>
            </div>
            <div className="navigationOptions">
              <div className="option"
                   onClick={this.handleNavigationItemClicked.bind(this, "TOP")}>{getTranslation().FIRST_PAGE}</div>
              <div className="option"
                   onClick={this.handleNavigationItemClicked.bind(this, "BOTTOM")}>{getTranslation.LAST_PAGE}</div>
            </div>
          </div>*/}
          <div className="previousNextContainer">
            <TooltipView placement="top" label={getTranslation().PREVIOUS}>
              <div className={"navigationItem previous" + sPreviousButtonVisibility}
                   onClick={bPreviousDisabled ? CS.noop : this.movePrevious}></div>
            </TooltipView>
            <TooltipView placement="top" label={getTranslation().NEXT}>
              <div className="navigationItem next"
                   onClick={this.moveNext}></div>
            </TooltipView>
          </div>
        </div>
    )
  }

  render() {

    return (
        <div className="thumbnailScrollerContainer" ref={this.setRef.bind(this,"thumbnailScrollerContainer")}>
          <div className="thumbnailContainer" ref={this.setRef.bind(this,"thumbnailContainer")}>
            {this.getThumbnails()}
          </div>
          {this.getLoadMoreView()}
        </div>
    );
  }
}

export const view = ThumbnailScroller;
export const events = oEvents;
