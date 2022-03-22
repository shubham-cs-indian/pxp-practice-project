import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactDOM from 'react-dom';
import ReactPropTypes from 'prop-types';
import ResizeSensor from 'css-element-queries/src/ResizeSensor';
import { view as CustomPopoverView } from '../../../../../viewlibraries/customPopoverView/custom-popover-view';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';

import { view as DragNDropView } from './../../../../../viewlibraries/dragndropview/drag-n-drop-view';
import DragNDropViewModel from './../../../../../viewlibraries/dragndropview/model/drag-n-drop-view-model';
import ViewUtils from './utils/view-utils';
import {view as ImageFitToContainerView} from "../../../../../viewlibraries/imagefittocontainerview/image-fit-to-container-view";
var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
  HANDLE_SORT_BY_ITEM_CLICKED: "handle_sort_by_item_clicked",
  HANDLE_SORT_ORDER_TOGGLED: "handle_sort_order_toggled",
  HANDLE_SORT_REORDERED: "handle_sort_reordered",
  FILTER_SORT_BY_ACTIVATED_ITEM_CLICKED: "filter_sort_by_activated_item_clicked",
  FILTER_SORT_BY_DEACTIVATED_ITEM_CLICKED: "filter_sort_by_deactivated_item_clicked",
};

const oPropTypes = {
  availableSortData: ReactPropTypes.array,
  activeSortDetails: ReactPropTypes.object,
  filterContext: ReactPropTypes.object.isRequired,
  selectedHierarchyContext: ReactPropTypes.string,
  handleSortDeactivatedItemClicked: ReactPropTypes.func,
  handleSortActivatedItemClicked: ReactPropTypes.func,
  handleSortByItemClicked: ReactPropTypes.func,
  handleMoreSectionSelectedItemClicked: ReactPropTypes.func
};

// @CS.SafeComponent
class SortByView extends React.Component {
  static propTypes = oPropTypes;

  constructor (props) {
    super(props);

    this.setRef =( sRef, element) => {
      this[sRef] = element;
    };
  }

  state = {
    showMore: false,
    availableSortData: this.props.availableSortData
  };

  componentDidMount() {
    setTimeout(this.calculateSortItemCount);
  }

  componentDidUpdate() {
    setTimeout(this.calculateSortItemCount);
  }

  static getDerivedStateFromProps (oNextProps, oState) {
    return{
      availableSortData: oNextProps.availableSortData
    }
  }

  handleMoreButtonClicked = (oEvent) => {
    var bCurrentState = this.state.showMore;
    this.setState({
      showMore: !bCurrentState,
      moreView: oEvent.currentTarget
    });
  };

  handleClosePopoverButtonClicked = () => {
    this.setState({
      showMore: false
    });
  };

  handleSortByItemClicked = (sId, oEvent) => {
    if (this.props.handleSortDeactivatedItemClicked) {
      this.props.handleSortByItemClicked(sId, this.props.filterContext);
    } else {
      EventBus.dispatch(oEvents.HANDLE_SORT_ORDER_TOGGLED, sId, this.props.filterContext);
    }
  };

  handleSortDeactivatedItemClicked = (sId, oEvent) => {
    let sSelectedHierarchyContext = this.props.selectedHierarchyContext;
    var bMultiSelect = (oEvent.ctrlKey || oEvent.metaKey);

    if (this.props.handleSortDeactivatedItemClicked) {
      this.props.handleSortDeactivatedItemClicked(sId, bMultiSelect, sSelectedHierarchyContext, this.props.filterContext);
    } else {
      EventBus.dispatch(oEvents.FILTER_SORT_BY_DEACTIVATED_ITEM_CLICKED, sId, bMultiSelect, sSelectedHierarchyContext, this.props.filterContext);
    }
  };

  handleSortActivatedItemClicked = (sId, oEvent) => {
    if (this.props.handleSortActivatedItemClicked) {
      this.props.handleSortActivatedItemClicked(sId, this.props.filterContext);
    } else {
      EventBus.dispatch(oEvents.FILTER_SORT_BY_ACTIVATED_ITEM_CLICKED, sId, this.props.filterContext);
    }
  };

  handleMoreSectionSelectedItemClicked = (oEvent) => {
    var sId = oEvent.target.dataset.id;

    if (this.props.handleMoreSectionSelectedItemClicked) {
      this.props.handleMoreSectionSelectedItemClicked(sId, this.props.filterContext)
    } else {
      EventBus.dispatch(oEvents.HANDLE_SORT_ORDER_TOGGLED, sId, this.props.filterContext);
    }
  };

  handleSortByItemFromPopoverClicked = (sId, oEvent) => {
    this.handleClosePopoverButtonClicked();
    var bMultiSelect = (oEvent.ctrlKey || oEvent.metaKey);
    EventBus.dispatch(oEvents.HANDLE_SORT_BY_ITEM_CLICKED, sId, bMultiSelect, this.props.filterContext);
  };

  calculateSortItemCount = () => {
    var _this = this;
    var __props = _this.props;
    if (!CS.isEmpty(_this["sortByViewContainer"])) {

      var oActiveSortDetails = __props.activeSortDetails;

      var sSplitter = ViewUtils.getSplitter();
      var bSelectionFromMoreContainer = false;

      _this["moreContainer"].classList.remove('hideMe'); //making more visible initially so we can use its width
      _this["selectedOption"].classList.remove('ascending');
      _this["selectedOption"].classList.remove('descending');
      var iWidthToSubtract = _this["sortByViewLabel"].offsetWidth + _this["moreContainer"].offsetWidth + 80; //80px for the less more toggle
      var iTotalWidth = _this["sortByViewContainer"].offsetWidth - iWidthToSubtract;
      var iSum = 0;
      var iMoreCounter =  0;
      var aAdjustableRefIds = [];
      CS.forEach(_this.state.availableSortData, function (oFilterData) {

        var oActiveSortData = oActiveSortDetails[oFilterData.sortField];


        var sRefId = oFilterData.sortField + sSplitter;
        var oAvailDOM = ReactDOM.findDOMNode(_this[sRefId + "avail"]);
        var oMoreDOM = ReactDOM.findDOMNode(_this[sRefId + "more"]);

        oAvailDOM.classList.remove('hideMe');
        iSum = iSum + oAvailDOM.offsetWidth;

        if (iSum >= iTotalWidth) {
          iMoreCounter++;
          oAvailDOM.classList.add('hideMe');
          oMoreDOM && oMoreDOM.classList.remove('hideMe');
          if (oActiveSortData.sortOrder) {
            var sSortOrderClass = oActiveSortData.sortOrder === "asc" ? "ascending" : "descending";
            _this["selectedOption"].dataset.id = oFilterData.sortField;
            _this["selectedOption"].innerText = CS.getLabelOrCode(oFilterData);
            _this["selectedOption"].title = CS.getLabelOrCode(oFilterData);
            _this["selectedOption"].classList.add('activeSortItem');
            _this["selectedOption"].classList.add(sSortOrderClass);
          }
          if (iSum < (iTotalWidth + _this["moreContainer"].offsetWidth)) {
            aAdjustableRefIds.push(sRefId);
          } else {
            aAdjustableRefIds = [];
          }
        }
        else {
          oMoreDOM && oMoreDOM.classList.add('hideMe');
          if (oActiveSortData.sortOrder) {
            bSelectionFromMoreContainer = true;

          }
        }
      });

      if(_this["moreContainer"]) {
        if (!iMoreCounter) {
          _this["moreContainer"].classList.add('hideMe');
        } else if (!CS.isEmpty(aAdjustableRefIds)) {
          CS.forEach(aAdjustableRefIds, function (sRefId) {
            var oAvailDOM = ReactDOM.findDOMNode(_this[sRefId + "avail"]);
            var oMoreDOM = ReactDOM.findDOMNode(_this[sRefId + "more"]);
            oAvailDOM.classList.remove('hideMe');
            oMoreDOM && oMoreDOM.classList.add('hideMe');
          });
          _this["moreContainer"].classList.add('hideMe');
        } else {
          _this["moreContainer"].classList.remove('hideMe');
          var oMoreSelectDOM = _this["selectedOption"];
          if(bSelectionFromMoreContainer && oMoreSelectDOM) {
            oMoreSelectDOM.title = iMoreCounter + " " + getTranslation().MORE;
            oMoreSelectDOM.dataset.id = "more";
            oMoreSelectDOM.classList.remove('activeSortItem');
            oMoreSelectDOM.innerHTML = iMoreCounter + " " + getTranslation().MORE;
          }
        }
      }

      if(typeof __props.onViewUpdate === 'function'){
        __props.onViewUpdate();
      }
    }
  };

  handleDragEnterEvent = (oDropModel, oDraggedModel) => {
    var aAvailableSortData = this.state.availableSortData;
    var sDestinationId = oDropModel.id;
    var sDraggedElementId = oDraggedModel.id;

    var iIndex = CS.findIndex(aAvailableSortData, {sortField: sDestinationId});
    var aDraggedItems = CS.remove(aAvailableSortData, {sortField: sDraggedElementId});

    aAvailableSortData.splice(iIndex, 0, aDraggedItems[0]);

    this.setState({
      availableSortData: aAvailableSortData
    });
  };

  handleDragEndEvent = () => {
    EventBus.dispatch(oEvents.HANDLE_SORT_REORDERED, this.state.availableSortData, this.props.filterContext)
  };

  getSortItems = () => {

    var _this = this;
    var _props = this.props;
    var aAvailableSortData = _this.state.availableSortData;
    var aSortDom = [];
    var aMoreSortItems = [];
    var sSplitter = ViewUtils.getSplitter();

    CS.forEach(aAvailableSortData, function (oAvailableSort, iIndex) {
      var sSortOrderClass = "ascending";
      var oActiveSortData = _props.activeSortDetails[oAvailableSort.sortField];
      if (oActiveSortData) {
        sSortOrderClass = oActiveSortData.sortOrder === "asc" ? "ascending " : "descending ";
      }

      var bIsActiveSort = !!oActiveSortData.sortOrder;
      var sClassName = bIsActiveSort ? ("sortItem activeSortItem ") : "sortItem ";
      var sActiveClass = bIsActiveSort? "sortButton activeSortItem ":"";
      var sExtraClassName = bIsActiveSort ? ("extraSortItem activeSortItem " + sSortOrderClass) : "extraSortItem ";
      var oClickSortIcon = _this.handleSortByItemClicked.bind(_this, oAvailableSort.sortField);
      var oClickDeactivatedItem = _this.handleSortDeactivatedItemClicked.bind(_this, oAvailableSort.sortField);
      var oClickActivatedItem = _this.handleSortActivatedItemClicked.bind(_this, oAvailableSort.sortField);
      var oClickOnLabel = bIsActiveSort ? oClickActivatedItem: oClickDeactivatedItem;

      var oClickHandlerForMoreSortItem = _this.handleSortByItemFromPopoverClicked.bind(_this, oAvailableSort.sortField);
      let sLabel = CS.getLabelOrCode(oAvailableSort);

      var oDragNDropModel = new DragNDropViewModel(oAvailableSort.sortField, sLabel, true, true, ["sortItem"], "sortItem", {});
      let sImageSrc = ViewUtils.getIconUrl(oAvailableSort.iconKey);
      aSortDom.push(
          <DragNDropView
              model={oDragNDropModel}
              key={iIndex}
              ref={_this.setRef.bind(_this,oAvailableSort.sortField + sSplitter + "avail")}
              onDragEnter={_this.handleDragEnterEvent}
              onDragEnd={_this.handleDragEndEvent}
          >
            <div className="customIcon">
              <ImageFitToContainerView imageSrc={sImageSrc}/>
            </div>
            <div className={sClassName}
                 onClick={oClickOnLabel}>
              {sLabel}
            </div>
            <div className={sActiveClass + sSortOrderClass} onClick={oClickSortIcon}></div>
          </DragNDropView>
      );

      aMoreSortItems.push(
          <div className={sExtraClassName} key={iIndex} ref={_this.setRef.bind(_this,oAvailableSort.sortField + sSplitter + "more")} title={sLabel}
               onClick={oClickHandlerForMoreSortItem}>{sLabel}</div>
      );
    });
    var oMoreSectionView = (
        <div className="moreSectionContainer" ref={this.setRef.bind(this, "moreContainer")}>
          <div className={"selectedOption"} data-id="more" ref={this.setRef.bind(this, "selectedOption")} onClick={this.handleMoreSectionSelectedItemClicked}>{getTranslation().MORE}</div>
          <div className="showMoreButton" onClick={this.handleMoreButtonClicked}></div>
          <CustomPopoverView
              className="popover-root"
              open={this.state.showMore}
              anchorEl={this.state.moreView}
              anchorOrigin={{horizontal: 'right', vertical: 'bottom'}}
              transformOrigin={{horizontal: 'right', vertical: 'top'}}
              onClose={this.handleClosePopoverButtonClicked}>
            <div className="moreOptions">
              {aMoreSortItems}
            </div>
          </CustomPopoverView>
        </div>
    );

    return {
      sortItems : aSortDom,
      moreSectionView : oMoreSectionView
    };
  };

  domMounted = (oView) => {
    if(oView) {
      var _this = this;
      new ResizeSensor(oView, CS.debounce(_this.calculateSortItemCount, 150));
    }
  };

  render() {

    var oSortSectionDom = this.getSortItems();
    var aSortItemDom = oSortSectionDom.sortItems;
    var oMoreSectionView = oSortSectionDom.moreSectionView;
    if (CS.isEmpty(aSortItemDom)) {
      return null;
    }
    return (
        <div ref={this.domMounted}>
          <div className="sortByViewContainer" ref={this.setRef.bind(this, "sortByViewContainer")}>
            <div ref={this.setRef.bind(this, "sortByViewLabel")} className="sortByViewLabel">{getTranslation().SORT_BY + " : "}</div>
            <div className="sortByViewItems">{aSortItemDom}</div>
            {oMoreSectionView}
          </div>
        </div>
    );
  }
}

export const view = SortByView;
export const events = oEvents;
