import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';

import { view as CustomMaterialButtonView } from '../custommaterialbuttonview/custom-material-button-view';
import FilterViewUtils from '../../commonmodule/util/fltr-view-utils';
import KeyboardNavigationForLists, { oCustomEvents} from '../../commonmodule/HOC/keyboard-navigation-for-lists';
import {view as ImageFitToContainerView} from "../imagefittocontainerview/image-fit-to-container-view";
import ViewUtils from "../../screens/homescreen/screens/contentscreen/view/utils/view-utils";

var oEvents = {
  CHILD_FILTER_TOGGLE_CLICKED: "child_filter_toggle_clicked",
  HANDLE_FILTER_SEARCH_TEXT_CHANGED: "handle_filter_search_text_changed"
};

const oPropTypes = {
  filterObject: ReactPropTypes.object,
  appliedFilters: ReactPropTypes.array,
  context: ReactPropTypes.string,
  extraData: ReactPropTypes.object,
  filterContext: ReactPropTypes.object.isRequired,
  showSearch: ReactPropTypes.bool,
  onApplyHandler: ReactPropTypes.func,
  handleChildFilterClicked: ReactPropTypes.func,
  hideButtons: ReactPropTypes.bool,
};

// @KeyboardNavigationForLists
// @CS.SafeComponent
class AvailableFilterItemView extends React.Component {
  static propTypes = oPropTypes;

  constructor (props) {
    super(props);

    this.setRef = (sRef, element) => {
      this[sRef] = element;
    };
  }

  state = {
    showAllItems: false,
    showDetails: false
  };

  componentDidMount () {
    this.props.registerCustomEvent(oCustomEvents.END_OF_LIST, this.handleShowAllItemsClicked);
    this.props.registerCustomEvent(oCustomEvents.SELECTION_OF_ITEM, this.handleChildFilterClicked);
    this.props.registerCustomEvent(oCustomEvents.APPLY_SELECTION, this.handleFilterButtonClicked);
  }

  handleShowAllItemsClicked = (oEvent) => {
    this.setState({
      showAllItems: true
    });
  };

  handleFilterButtonClicked = () => {
    this.props.onApplyHandler();
  };

  handleChildFilterClicked = (iItemIndex) => {
    var __props = this.props;
    var oFilterObj = __props.filterObject;
    let aTotalChildren = oFilterObj.children;
    let oChild = aTotalChildren[iItemIndex];
    if (!oChild.count && !oChild.ignoreCount) {
      return;
    }

    let sChildId = oChild.id;
    this.props.setItemInFocus(iItemIndex);

    if (__props.handleChildFilterClicked) {
      __props.handleChildFilterClicked(oFilterObj.id, sChildId, __props.context, __props.extraData, __props.filterContext)
    } else {
      EventBus.dispatch(oEvents.CHILD_FILTER_TOGGLE_CLICKED, oFilterObj.id, sChildId, __props.context, __props.extraData, __props.filterContext);
    }
  };

  getIconDOM = (sImageSrc) => {
    return (
        <div className="customIcon">
          <ImageFitToContainerView imageSrc={sImageSrc}/>
        </div>
    );
  };

  getChildrenViews = () => {
    var _this = this;
    var __props = _this.props;
    var aAppliedFilters = __props.appliedFilters;
    var oFilterObject = __props.filterObject;
    var aTotalChildren = oFilterObject.children;
    var aChildren = this.state.showAllItems ? aTotalChildren : aTotalChildren.slice(0, 50);
    var aChildrenViews = [];
    let iItemIndex = 0;
    this.props.setIndexMap({});
    let oIndexMap = [];
    CS.forEach(aChildren, function (oChild, iIndex) {
      if (!oChild.isHidden) {
        var sFilterLabel = FilterViewUtils.getLabelByAttributeType(oFilterObject.type, oChild, oFilterObject.defaultUnit, oFilterObject.precision, _this.props.filterContext);
        var oAppliedChild = CS.find(aAppliedFilters, {id: oChild.id});
        var sCheckboxClass = oAppliedChild ? "filterItemChildCheckbox checked" : "filterItemChildCheckbox";
        var sTooltipLabel = sFilterLabel;
        let sCount = "";
        if (!oChild.ignoreCount) {
          sCount = " (" + (oChild.count || 0) + ")";
        }

        var oLabel = (
            <div className="filterItemChildLabel" title={sTooltipLabel} key={oChild.id}>
              <span className="filterItemLabelSpan">{sFilterLabel}</span>
              <span className="countSpan">{sCount}</span>
            </div>
        );

        var sItemClass = "filterItemChildContainer ";
        if (!oChild.count && !oChild.ignoreCount) {
          sItemClass += "disabled";
        }

        if (_this.props.itemInFocus == iItemIndex) {
          sItemClass += "inFocus";
        }

        oIndexMap[iItemIndex] = iIndex;

        let sImageSrc = ViewUtils.getIconUrl(oChild.iconKey);
        let bIsEnableIconDOM = _this.props.showDefaultIcon || CS.isNotEmpty(oChild.iconKey);

        aChildrenViews.push(
            <div className={sItemClass} onClick={_this.handleChildFilterClicked.bind(_this, iIndex)} key={oChild.id}
                 ref={_this.setRef.bind(_this, 'contextMenuItem' + iItemIndex++)}>
              <div className={sCheckboxClass}></div>
              {bIsEnableIconDOM ? _this.getIconDOM(sImageSrc) : null}
              {oLabel}
            </div>
        );
      }
    });

    this.props.setIndexMap(oIndexMap);

    return aChildrenViews;
  };

  handleFilterSearchTextChanged = (oFilterObject) => {
    var __props = this.props;
    let sRef = "searchBox";
    var oDom = this[sRef];
    if (!CS.isEmpty(oDom)) {
      var sSearchText = oDom.value;
      var oSearchedData = { attributeId: oFilterObject.id, attributeSearchText: sSearchText, type: oFilterObject.type };

      if (this.props.handleFilterSearchTextChanged) {
        this.props.handleFilterSearchTextChanged(this, oSearchedData, __props.context, __props.extraData, __props.filterContext);
      } else {
        EventBus.dispatch(oEvents.HANDLE_FILTER_SEARCH_TEXT_CHANGED, this, oSearchedData, __props.context, __props.extraData, __props.filterContext);
      }
    }
  };

  handleSearchBoxClicked = () => {
    this.props.setItemInFocus(-1);
  };

  getSearchBox = (oFilterObject) => {
    let sRef = "searchBox";
    var oFilterSearchInputDOM = this[sRef];
    var sFilterSearchInputValue = "";
    if (!CS.isEmpty(oFilterSearchInputDOM)) {
      sFilterSearchInputValue = oFilterSearchInputDOM.value;
    }
    var oSearchIconView = sFilterSearchInputValue ? (
        <div className="searchClearIcon" onClick={this.handleClearSearchText.bind(this, oFilterObject)}></div>) : null;
    return (this.props.showSearch ? null : (<div className="filterSearchBoxWrapper">
      <div className="filterSearchBox">
        <input type="text"
               placeholder={getTranslation().SEARCH}
               className="searchBox"
               ref={this.setRef.bind(this, "searchBox")}
               onClick={this.handleSearchBoxClicked}
               onChange={CS.debounce(this.handleFilterSearchTextChanged.bind(this, oFilterObject), 200)}/>
        {oSearchIconView}
      </div>
    </div>));
  };

  handleClearSearchText = (oFilterObject) => {
    var __props = this.props;
    var oSearchedData = {attributeId: oFilterObject.id, attributeSearchText: "", type: oFilterObject.type};
    if (this.props.handleFilterSearchTextChanged) {
      this.props.handleFilterSearchTextChanged(this, oSearchedData, __props.context, __props.extraData, __props.filterContext);
    } else {
      EventBus.dispatch(oEvents.HANDLE_FILTER_SEARCH_TEXT_CHANGED, this, oSearchedData, __props.context, __props.extraData, __props.filterContext);
    }
    let sRef = "searchBox";
    var oFilterSearchInputDOM = this[sRef];
    if (!CS.isEmpty(oFilterSearchInputDOM)) {
      oFilterSearchInputDOM.value = "";
      oFilterSearchInputDOM.focus();
    }
  };

  render () {

    var oFilterObject = this.props.filterObject;
    var aChildrenViews = this.getChildrenViews();
    var oApplyButtonLabelStyles = {
      fontSize: 11,
      fontWeight: 300,
      lineHeight: "17px",
      padding: "0px"
    };

    var aVisibleChildren = CS.filter(oFilterObject.children, function (oChild) {
      return !Boolean(oChild.isHidden);
    });

    var oShowAllItemsView = (aVisibleChildren.length > 50) && !this.state.showAllItems ? (<div className="moreCount"
                                                                                               onClick={this.handleShowAllItemsClicked}>{getTranslation().SHOW_ALL + " (50+)"}</div>) : null;

    let oButtonsView = !this.props.hideButtons ?
      (<div className="filterControlWrapper">
        <div className="applyButtonContainer">
          <CustomMaterialButtonView
            label={getTranslation().APPLY}
            isRaisedButton={true}
            isDisabled={false}
            labelStyle={oApplyButtonLabelStyles}
            onButtonClick={this.handleFilterButtonClicked}/>
        </div>
      </div>) : null;



    return (
        <div className="filterItemWrapper" onKeyDown={this.props.onKeyPressHandler} tabIndex={0}
             ref={this.setRef.bind(this, 'contextMenuView')}>
          {this.getSearchBox(oFilterObject)}
          <div className="filterItemChildrenContainer" ref={this.setRef.bind(this, 'scrollbar')}>
            {aChildrenViews}
            {oShowAllItemsView}
          </div>
          {oButtonsView}
        </div>
    );
  }
}

export const view = KeyboardNavigationForLists(AvailableFilterItemView);
export const events = oEvents;
