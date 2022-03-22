import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import TooltipView from '../tooltipview/tooltip-view';
import { view as ImageFitToContainerView } from './../imagefittocontainerview/image-fit-to-container-view';
import ExceptionLogger from '../../libraries/exceptionhandling/exception-logger';
import ViewLibraryUtils from '../utils/view-library-utils';
import CommonUtils from "../../commonmodule/util/common-utils";

const oEvents = {
  HANDLE_TAB_LAYOUT_VIEW_TAB_CLICKED: "handle_tab_layout_view_tab_clicked"
};

const oPropTypes = {
  tabList: ReactPropTypes.arrayOf(
      ReactPropTypes.shape({
        id: ReactPropTypes.string,
        label: ReactPropTypes.string,
        count: ReactPropTypes.number,
        className: ReactPropTypes.string,
        icon: ReactPropTypes.string
      })
  ),
  activeTabId: ReactPropTypes.string,
  onChange: ReactPropTypes.func,

  //if you need a border around the body area :
  addBorderToBody: ReactPropTypes.bool,
  context: ReactPropTypes.string
};
/**
 * @class TabLayoutView - use for tab layout in Application.
 * @memberOf Views
 * @property {array} [tabList] -  an array of tabList data.
 * @property {string} [activeTabId] -  string of activeTabId.
 * @property {func} [onChange] -  a function which used for onChange event.
 * @property {bool} [addBorderToBody] -  boolean which is used for addBorderToBody or not.
 * @property {string} [context] -  string of context.
 */

// @CS.SafeComponent
class TabLayoutView extends React.Component{

  constructor(props) {
    super(props);

    this.iWidthDifference = 0; /** difference in width of tabSection and tabList */

    this.tabsSection = React.createRef();
    this.scrollContainer = React.createRef();
    this.tabsList = React.createRef();
    this.leftScrollButton =  React.createRef();
    this.rightScrollButton = React.createRef();
  }

  componentDidMount = () => {
    !CS.isEmpty(this.props.tabList) && this.handleScrollButtonVisibility();
  };

  componentDidUpdate = () => {
    !CS.isEmpty(this.props.tabList) && this.handleScrollButtonVisibility();
  };

  /*getDefaultProps: function () {
    return ({
      tabList: [
        {
          id: "1",
          label: "First Tab"
        },
        {
          id: "2",
          label: "Second Tab"
        },
        {
          id: "3",
          label: "Third Tab"
        },
        {
          id: "4",
          label: "Fourth Tab"
        }
      ],
      activeTabId: "2",
      addBorderToBody: true
    });
  },*/

  handleScrollButtonVisibility = () => {
    try {
      //todo: may have to be called on window resize!
      let oTabsContainerDOM = this.scrollContainer.current;
      /** The Container */
      let iTabsContainerWidth = Math.ceil(oTabsContainerDOM.offsetWidth) || 0;
      let oTabsListDOM = this.tabsList.current;
      /** The inner section */
      let iTabsListWidth = Math.ceil(oTabsListDOM.offsetWidth) || 0;

      let oLeftScrollButton = this.leftScrollButton.current;
      let oRightScrollButton = this.rightScrollButton.current;
      oLeftScrollButton.style.visibility = "hidden";
      oRightScrollButton.style.visibility = "hidden";

      if (iTabsListWidth > iTabsContainerWidth) {
        // this.iWidthDifference = iTabsListWidth - iTabsContainerWidth;
        /** all possible 'left' values of the oTabsListDOM will lie between 0px and -(iWidthDifference)px */
        let iTabListLeft = Math.ceil(oTabsContainerDOM.scrollLeft) || 0;
        if (iTabListLeft) {
          //left button visible
          oLeftScrollButton.style.visibility = "visible";
        }
        if ((iTabListLeft + iTabsContainerWidth) < iTabsListWidth) {
          //right button visible
          oRightScrollButton.style.visibility = "visible";
        }
      }
    } catch (oException) {
      ExceptionLogger.error("Something went wrong in 'handleScrollButtonVisibility' (File - tab-layout-view) : ");
      ExceptionLogger.error(oException);
    }
  };

  handleScrollButtonClicked = (bScrollLeft) => {
    let iScrollByWidth = 200; /** HARDCODED ALERT */
    let oScrollContainer = this.scrollContainer.current;
    let oTabsListDOM = this.tabsList.current;
    let iCurrentLeft = Math.ceil(oScrollContainer.scrollLeft);
    let iNewLeft = 0;

    /**
     * As per scrollBy requirement,
     * 1. for scrolling left, -ve value is expected,
     * 2. for scrolling right, +ve value is expected.
     *
     * if the scrolling width exceeds 200, then set the width as 200, else continue with width.
     * */
    if (bScrollLeft) {
      iNewLeft = (iCurrentLeft + Math.ceil(oScrollContainer.offsetWidth)) - Math.ceil(oTabsListDOM.offsetWidth);
      if((Math.abs(iNewLeft) > iScrollByWidth) || Math.abs(iNewLeft) == 0){
        iNewLeft = -iScrollByWidth;
      };
    } else {
      iNewLeft = Math.ceil(oTabsListDOM.offsetWidth) - (iCurrentLeft + Math.ceil(oScrollContainer.offsetWidth));
      iNewLeft = (iNewLeft > iScrollByWidth) ? iScrollByWidth : iNewLeft;
    }
    CommonUtils.scrollBy(oTabsListDOM.parentElement, {left: iNewLeft}, "", this.handleScrollButtonVisibility);
  };

  handleTabClicked = (sTabId) => {
    let fOnChange = this.props.onChange;
    if (CS.isFunction(fOnChange)) {
      fOnChange(sTabId);
    } else {
      EventBus.dispatch(oEvents.HANDLE_TAB_LAYOUT_VIEW_TAB_CLICKED, sTabId, this.props.context);
    }
  };

  getTabs =(aTabList, sActiveTabId)=> {
    let _this = this;
    let aTabs = [];

    CS.forEach(aTabList, function (oTab) {
      let sTabId = oTab.id;
      let sTabLabel = CS.getLabelOrCode(oTab);
      let sTabItemClass = "tabItem ";
      let oTabCount = null;
      let iCount = oTab.count;
      let sActiveTabIconAppender = "";

      if (sTabId === sActiveTabId) {
        sTabItemClass += "isActive ";
        sActiveTabIconAppender += "isActive ";
      }

      if (iCount) {
        oTabCount = <div className='tabCount'>
          {iCount}
        </div>
      }
      let oTabIcon = null;
      if (oTab.iconKey) {
        let sImageURL = ViewLibraryUtils.getIconUrl(oTab.iconKey);
        oTabIcon = (
            <div className="tabIconImage">
              <ImageFitToContainerView imageSrc={sImageURL}></ImageFitToContainerView>
            </div>
        );
      } else if (oTab.className) {
        oTabIcon = (<div className={"tabIcon " + sActiveTabIconAppender + oTab.className}></div>);
      }

      aTabs.push(
          <TooltipView placement="bottom" label={sTabLabel} key={sTabId}>
            <div className={sTabItemClass} key={sTabId} onClick={_this.handleTabClicked.bind(_this, sTabId)}>
              {oTabIcon}
              {oTabCount}
              {sTabLabel}
            </div>
          </TooltipView>
      );
    });

    return aTabs;
  };

  render() {

    let aTabList = this.props.tabList;
    let sActiveTabId = this.props.activeTabId;
    let oTabSectionView = null;
    let aTabs = this.getTabs(aTabList, sActiveTabId);

    if (!CS.isEmpty(aTabList)) {
      oTabSectionView =
          <div className="tabsSection" ref={this.tabsSection}>

            <div className="scrollButton left" ref={this.leftScrollButton}
                 onClick={this.handleScrollButtonClicked.bind(this, true)}></div>

            <div className="parentScrollContainer" ref={this.scrollContainer}>
              <div className="tabsList" ref={this.tabsList}>
                {aTabs}
              </div>
            </div>
            <div className="scrollButton right" ref={this.rightScrollButton}
                 onClick={this.handleScrollButtonClicked.bind(this, false)}></div>

          </div>
    }

    let sBodySectionClass = "bodySection ";
    if (this.props.addBorderToBody) {
      sBodySectionClass += "withBorder ";
    }

    return (
        <div className="tabLayoutView">

          <div className="tabLayoutListContainer">
            {oTabSectionView}
          </div>

          <div className={sBodySectionClass}>
            {this.props.children}
          </div>

        </div>
    );
  }

}

TabLayoutView.propTypes = oPropTypes;

export const view = TabLayoutView;
export const events = oEvents;
export const propTypes = TabLayoutView.propTypes;
