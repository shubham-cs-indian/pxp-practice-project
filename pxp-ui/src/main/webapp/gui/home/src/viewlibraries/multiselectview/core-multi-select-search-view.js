import React from 'react';
import CS from '../../libraries/cs';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import { view as CustomPopoverView } from '../../viewlibraries/customPopoverView/custom-popover-view';
import ViewUtils from '../../viewlibraries/utils/view-library-utils';
import { view as ImageFitToContainerView } from '../imagefittocontainerview/image-fit-to-container-view';
import TooltipView from './../tooltipview/tooltip-view';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import ExceptionLogger from '../../libraries/exceptionhandling/exception-logger';
import { view as ContextMenuViewNew } from '../contextmenuwithsearchview/context-menu-view-new';
import ContextMenuViewModel from '../../viewlibraries/contextmenuwithsearchview/model/context-menu-view-model';
import ClassNameFromBaseTypeDictionary from'../../commonmodule/tack/class-name-base-types-dictionary';

const oEvents = {
  MULTI_SELECT_POPOVER_CLOSED: "multi_select_popover_closed",
  MULTI_SEARCH_BAR_CROSS_ICON_CLICKED: "multi_search_bar_cross_icon_clicked",
  MULTI_SELECT_SEARCH_HANDLE: "multi_select_search_handle",
  MULTI_SELECT_SEARCH_LOAD_MORE_HANDLE: "multi_select_search_load_more_handle",
  MULTI_SELECT_POPOVER_OPENED: "multi_select_popover_opened",
  MULTI_SELECT_CREATE_HANDLE: "multi_select_create_handle"
};

const oPropTypes = {
  isMultiSelect: ReactPropTypes.bool,
  bShowIcon: ReactPropTypes.bool,
  items: ReactPropTypes.array,
  context: ReactPropTypes.string,
  disabled: ReactPropTypes.bool,
  selectedItems: ReactPropTypes.array,
  onApply: ReactPropTypes.func,
  showColor: ReactPropTypes.bool,
  cannotRemove: ReactPropTypes.bool,
  customPlaceholder: ReactPropTypes.string,
  searchHandler: ReactPropTypes.func,
  loadMoreHandler: ReactPropTypes.func,
  isLoadMoreEnabled: ReactPropTypes.bool,
  label: ReactPropTypes.string,
  showLabel: ReactPropTypes.bool,
  hideTooltip: ReactPropTypes.bool,
  showSelectedInDropdown: ReactPropTypes.bool,
  searchText: ReactPropTypes.string,
  onPopOverOpen: ReactPropTypes.func,
  onCreateHandler: ReactPropTypes.func,
  showCreateButton: ReactPropTypes.bool,
  selectedObjects: ReactPropTypes.array,
  clearSearchOnPopoverClose: ReactPropTypes.bool,
  showSearch: ReactPropTypes.bool,
  anchorOrigin: ReactPropTypes.object,
  targetOrigin: ReactPropTypes.object,
  showCustomIcon: ReactPropTypes.bool,
  onDeleteHandler: ReactPropTypes.func,
  showHidePopoverHandler: ReactPropTypes.func,
  showPopover: ReactPropTypes.bool,
  className: ReactPropTypes.string,
  showDeleteIcon: ReactPropTypes.bool
  /*** NOTE: SelectedItems are referred to SelectedObjects when not found in aItems
   * Use it when aItems does not have selectedItems i.e For Dynamically updated aItems***/
};
/**
 * @class CoreMultiSelectSearchView
 * @memberOf Views
 * @property {bool} [isMultiSelect] - To select multiple items from dropdowm list flag is set to true and vice versa.
 * @property {bool} [bShowIcon] - If true then showing icon for list items.
 * @property {array} [items] - Contains all items data;
 * @property {string} [context] - Used to differentiate which operation will be perform.
 * @property {bool} [disabled] - If true disabling CoreMultiSelectSearchView.
 * @property {array} [selectedItems] - Contains selected Items data.
 * @property {func} [onApply] - Execute when apply button is clicked.
 * @property {bool} [showColor] - To change the style of item, if true(ex.display = 'block'; backgroundColor = sColor).
 * @property {bool} [cannotRemove] - If true hiding cross icon from selected items for restrict remove operation.
 * @property {string} [customPlaceholder] - To Show text if nothing is selected from list.
 * @property {func} [searchHandler] - Executes when text is searched.
 * @property {func} [loadMoreHandler] - Execute after loadMore option is clicked.
 * @property {bool} [isLoadMoreEnabled] -
 * @property {string} [label] -
 * @property {bool} [showLabel] - To show label.
 * @property {bool} [hideTooltip] - To hide tooltip of label.
 * @property {bool} [showSelectedInDropdown] - To Show selected items in dropdown list.
 * @property {string} [searchText] - Contains text which you have searched.
 * @property {func} [onPopOverOpen] - Executes when CoreMultiSelectSearchView is clicked.
 * @property {func} [onCreateHandler] - Executes when create button is clicked.
 * @property {bool} [showCreateButton] - To show create button on dropdown list.
 * @property {array} [selectedObjects] - Contains selected items data.
 * @property {bool} [clearSearchOnPopoverClose] - Execute after closing popover for clear the search text.
 * @property {bool} [showSearch] - To show search bar on dropdown list.
 * @property {object} [anchorOrigin] - This is the point on the anchor where the popover's anchorEl will attach to.
 * This is not used when the anchorReference is 'anchorPosition'.
 * Options: vertical: [top, center, bottom]; horizontal: [left, center, right].
 * @property {object} [targetOrigin] -
 * @property {bool} [showCustomIcon] - To show custom icons for items in dropdown list.
 * showing custom icon for nodes).
 * @property {func} [onDeleteHandler] - Executes when delete icon of node is clicked.
 * @property {bool} [showDeleteIcon] - To show delete icon for node.
 * @property {func} [showHidePopoverHandler] - Executes after dropdown is closed.
 * @property {bool} [showPopover] - Used for toggle(show/hide) dropdown.
 * @property {string} [className] - To apply CSS className for ContextMenuViewNew.
 */

class CoreMultiSelectSearchView extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      showMore: false,
      showDetails: false,
      searchString: "",
      isLoadMoreClicked: false,
      selectedItems: CoreMultiSelectSearchView.getClonedSelectedItems(props, props.context),
      context: props.context
    }

    this.setRef =( sRef, element) => {
      this[sRef] = element;
    };
  }

  static getDerivedStateFromProps(oNextProps, oState) {
    if(oState.isLoadMoreClicked) {
      return {
        isLoadMoreClicked: false
      };
    } else {
      return {
        selectedItems: CoreMultiSelectSearchView.getClonedSelectedItems(oNextProps, oState.context),
        context: oNextProps.context
      };
    }
  }

  /*componentWillReceiveProps =(oNextProps)=> {
    if(this.state.isLoadMoreClicked) {
      this.setState({
        isLoadMoreClicked: false
      });
    } else {
      this.setState({
        selectedItems: this.getClonedSelectedItems(oNextProps),
      });
    }
  }*/

  componentDidMount =()=> {
    /** setTimeout() is used in componentDidMount() of Popover.js,
     * which causes delay in rendering Popover view,
     * hence it is used here for delay.
     */
    setTimeout(this.calculateSelectedItemCount);
  }

  componentDidUpdate =()=> {
    /** setTimeout() is used in componentDidUpdate() of Popover.js,
     * which causes delay in updating Popover view,
     * hence it is used here for delay.
     */
    setTimeout(this.calculateSelectedItemCount);
  }

  shouldComponentUpdate =(oNextProps, oNextState)=> {
    return !CS.isEqual(oNextProps, this.props) || !CS.isEqual(oNextState, this.state);
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

  static getClonedSelectedItems =(oNextProps, context)=> {
    let aSelectedItems = [];
    if(oNextProps) {
      let aItems = oNextProps.items;
      let aSelectedObjects = oNextProps.selectedObjects;
      CS.forEach(oNextProps.selectedItems, function (sId) {
        try {
          let oItem = CS.find(aItems, {id: sId});
          if (CS.isEmpty(oItem)) {
            oItem = CS.find(aSelectedObjects, {id: sId});
          }
          aSelectedItems.push(oItem.id);
        }
        catch (oException) {
          ExceptionLogger.error(oException);
          ExceptionLogger.error(context);
        }
      });

    }
    return aSelectedItems;
  };


  onPopoverClosed =(aSelectedItems)=> {
    var aPreviousSelectedItems = this.props.selectedItems;
    if(!CS.isEqual(aSelectedItems, aPreviousSelectedItems)) {
      if(CS.isFunction(this.props.onApply)) {
        this.props.onApply(aSelectedItems);
      } else {
        EventBus.dispatch(oEvents.MULTI_SELECT_POPOVER_CLOSED, aSelectedItems, this.props.context);
      }
    }
  }

  onPopOverOpen =(sContext)=> {
    var oProps = this.props;
    if (CS.isFunction(oProps.onPopOverOpen)) {
      oProps.onPopOverOpen(sContext, oProps.items);
    } else {
      EventBus.dispatch(oEvents.MULTI_SELECT_POPOVER_OPENED, oProps.context);
    }
  }

  handleCrossIconClicked =(sId, oEvent)=> {
    oEvent.nativeEvent.dontRaise = true;
    var isDisabled = this.props.disabled;
    var sContext = this.props.context;
    var aSelectedItems = CS.cloneDeep(this.state.selectedItems);
    if(CS.includes(aSelectedItems, sId)) {
      CS.remove(aSelectedItems, function (sItemId) {
        return sItemId == sId;
      });

      if (CS.isFunction(this.props.onApply)) {
        this.props.onApply(aSelectedItems);
      } else {
        EventBus.dispatch(oEvents.MULTI_SEARCH_BAR_CROSS_ICON_CLICKED, this, sContext, sId);
      }
    }
  }

  getNothingSelectedDom =()=> {
    var bIsActive = this.state.showDetails;
    var aSelectedItems = this.state.selectedItems || [];
    var sPlaceholder = (this.props.customPlaceholder) ? this.props.customPlaceholder : getTranslation().NOTHING_IS_SELECTED;

    if(this.props.disabled && !aSelectedItems.length){
      return null;
    } else if (!bIsActive && !aSelectedItems.length) {
      return (<div className="multiSelectNothingSelected">{sPlaceholder}</div>);
    }
    return null;
  }

  handleChildFilterClicked =(oModel)=> {
    var sId = oModel.id;
    var bIsMultiSelect = this.props.isMultiSelect;
    var aSelectedItems = this.state.selectedItems || [];
    if(!bIsMultiSelect) {
      this.onPopoverClosed([sId]);
      this.setState({
        selectedItems: [sId]
      });
    } else {
      var aRemovedItems = CS.remove(aSelectedItems, function (sItemId) {
        return sItemId == sId
      });
      if (CS.isEmpty(aRemovedItems)) {
        aSelectedItems.push(sId);
      }

      this.setState({
        selectedItems: aSelectedItems,
      });
    }
  }

  calculateSelectedItemCount =()=> {
    var _this = this;
    if (!CS.isEmpty(_this["multiSelectSearchView"])) {
      var aSelectedItems = _this.state.selectedItems;
      var sSplitter = ViewUtils.getSplitter();
      let oMultiSelectSearchViewDOM = _this["multiSelectSearchView"];

      if (_this["moreContainer"]) {
        _this["moreContainer"].classList.remove('hideMe');
      }
      var iWidthToSubtract = (_this.props.isMultiSelect) ? (_this["moreContainer"].offsetWidth + 1) : 0;
      var iTotalWidth = Math.ceil(oMultiSelectSearchViewDOM.offsetWidth) - iWidthToSubtract;
      var iSum = 0;
      var iMoreCounter = 0;
      var aAdjustableRefIds = [];

      if (!_this.props.isMultiSelect) {
        oMultiSelectSearchViewDOM.classList.add('singleSelect');
        _this["moreContainer"].classList.add('hideMe');
        return;
      }

      CS.forEach(aSelectedItems, function (sId, iIndex) {
        var oSelectedLabelDOM = _this[sId + sSplitter + "selectedLabel"];
        var oSelectedIconDOM = _this[sId + sSplitter + "selectedIcon"];
        var oSelectedDOM = _this[sId + sSplitter + "selected"];
        var oMoreDOM = _this[sId + sSplitter + "more"];
        var oCrossIcon = _this[sId + sSplitter + "crossIcon"]

        oSelectedDOM.classList.remove('hideMe');
        var iSelectedIconWidth = oSelectedIconDOM && oSelectedIconDOM.offsetWidth ? oSelectedIconDOM.offsetWidth : 0;
        let oCrossIconStyle = oCrossIcon ? getComputedStyle(oCrossIcon) : undefined;
        let iCrossIconWidth = oCrossIconStyle ? parseFloat(oCrossIconStyle.marginRight) + parseFloat(oCrossIconStyle.marginLeft) + parseFloat(oCrossIconStyle.width) : 0;
        iSum = iSum + oSelectedLabelDOM.offsetWidth + iSelectedIconWidth + iCrossIconWidth;

        if (iSum >= iTotalWidth && iIndex >= 0) {
          iMoreCounter++;
          oMoreDOM && oMoreDOM.classList.remove('hideMe');
          oSelectedDOM.classList.add('hideMe');
          if (iSum < (iTotalWidth + Math.ceil(_this["moreContainer"].offsetWidth + 1))) {
            aAdjustableRefIds.push(sId + sSplitter);
          } else {
            aAdjustableRefIds = [];
          }
        } else if (iSum >= iTotalWidth && iIndex == 0){
          oMoreDOM && oMoreDOM.classList.add('hideMe');
          if (iSum < (iTotalWidth + Math.ceil(_this["moreContainer"].offsetWidth + 1)) && !iMoreCounter) {
            oSelectedDOM.classList.remove('hideMe');
          } else {
            if(_this.props.cannotRemove) {
              //selectedItemMargin(7) + labelMargin(17) = 24
              iWidthToSubtract = iSelectedIconWidth + 10;
            } else {
              //selectedItemMargin(7) + labelMargin(17) + crossIcon(14) + crossIconMargin(4) = 42
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

      if (_this["moreContainer"] && _this["moreCounter"]) {
        if (!iMoreCounter) {
          _this["moreContainer"].classList.add('hideMe');

          //To hide un-mount more count popover if it is already mounted and there is nothing to show
          if (_this.state.showMore) {
            _this.setState({showMore: false});
          }

        } else if (!CS.isEmpty(aAdjustableRefIds)) {
          CS.forEach(aAdjustableRefIds, function (sRefId) {
            var oSelectedDOM = _this[sRefId + "selected"];
            var oMoreDOM = _this[sRefId + "more"];
            oSelectedDOM.classList.remove('hideMe');
            oMoreDOM && oMoreDOM.classList.add('hideMe');
          });
          //To hide un-mount more count popover if it is already mounted and there is nothing to show
          if (_this.state.showMore) {
            _this.setState({showMore: false});
          }
          _this["moreContainer"].classList.add('hideMe');
        } else {
          _this["moreContainer"].classList.remove('hideMe');
          _this.moreCounter.innerHTML = iMoreCounter;
        }
      }
    }
    this.props.hasOwnProperty("adjustRowHeight") && this.props.adjustRowHeight();
  }

  getMoreSectionView =(aMoreFilterItems)=> {
    var sMoreFiltersSectionClassName = this.state.showMore ? "moreFilters" : "moreFilters invisible";
    return (
          <div className="moreSection" ref={this.setRef.bind(this, "moreContainer")}
               onClick={this.handleMoreButtonClicked}>
            <div className="moreLabel" ref={this.setRef.bind(this, "moreCounter")}></div>
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

  getContextMenuModelList =(aItems)=> {
    var aItemModels = [];
    var sContext = this.props.context;
    let bShowSelectedInDropdown = this.props.showSelectedInDropdown;
    let bIsMultiSelect = this.props.isMultiSelect;
    let aSelectedItems = this.state.selectedItems;
    let sSelectedItemId = aSelectedItems[0];

    CS.forEach(aItems, function (oItem) {
      // NOTE: showSelectedInDropdown(=false) is used to hide the selected item in dropdown (only for singleSelect)
      if (!bIsMultiSelect && !bShowSelectedInDropdown && sSelectedItemId === oItem.id) {
        return;
      }

      let sIcon = oItem.iconKey || "";
      let sColor = oItem.color || "";
      let oProperties = {
        context: sContext,
        color: sColor,
        code: oItem.code || ""
      };
      var sLabel = oItem.label;
      if (!CS.isEmpty(oItem.iconClassName)) {
        oProperties.customIconClassName = oItem.iconClassName;
      } else {
        let sType = CS.isEmpty(oItem.type) ? oItem.value : oItem.type;
        oProperties.customIconClassName = ClassNameFromBaseTypeDictionary[sType] || "";
      }
      aItemModels.push(new ContextMenuViewModel(
          oItem.id,
          sLabel,
          false, //bIsActive
          sIcon, //sIcon
          oProperties
      ));

    });
    return aItemModels;
  }

  handleMSSSearch =(sSearchText)=> {
    if (this.props.searchHandler) {
      this.props.searchHandler(sSearchText);
    } else {
      var sContext = this.props.context;
      EventBus.dispatch(oEvents.MULTI_SELECT_SEARCH_HANDLE, sContext, sSearchText);
    }
  }

  handleMSSCreate =(sSearchText)=> {
    if (this.props.onCreateHandler) {
      this.props.onCreateHandler(sSearchText);
    } else {
      var sContext = this.props.context;
      EventBus.dispatch(oEvents.MULTI_SELECT_CREATE_HANDLE, sContext, sSearchText);
    }
  }

  handleMSSLoadMore =()=> {
    this.setState({
      isLoadMoreClicked: true
    });
    if (this.props.loadMoreHandler) {
      this.props.loadMoreHandler();
    } else {
      var sContext = this.props.context;
      EventBus.dispatch(oEvents.MULTI_SELECT_SEARCH_LOAD_MORE_HANDLE, sContext);
    }
  }

  handleClearButtonClicked = (sContext) => {
    let oProps = this.props;
    oProps.onClearHandler(sContext);
  }

  getSelectedItemView = (sId, sLabel, sLabelClass, oIconOrColorView, oCrossIconView) => {
    let sSplitter = ViewUtils.getSplitter();
    let sClassName = "selectedItems" + (this.props.disabled ? " disabled" : "");
    return (
        <div className={sClassName} ref={this.setRef.bind(this,sId + sSplitter + "selected")} key={"selectedItems" + sId}>
          {oIconOrColorView}
          <TooltipView label={sLabel} placement="bottom">
            <div className={sLabelClass} ref={this.setRef.bind(this,sId + sSplitter + "selectedLabel")}>{sLabel}</div>
          </TooltipView>
          {oCrossIconView}
        </div>
    );
  };

  getSummaryView =()=> {
    var _this = this;
    var aSummaryView = [];
    var aItems = this.props.items;
    var aSelectedItems = this.state.selectedItems;
    var aMoreFilterItems = [];
    var sSplitter = ViewUtils.getSplitter();
    let aSelectedObjects = this.props.selectedObjects || [];

    CS.forEach(aSelectedItems, function (sId) {
      var oItem = CS.find(aItems, {"id": sId});
      if (CS.isEmpty(oItem)) {
        oItem = CS.find(aSelectedObjects, {"id": sId});
      }

      var oIconOrColorView = null;
      if (_this.props.bShowIcon && oItem.hasOwnProperty('iconKey') && oItem.iconKey) {
        //oIconOrColorView = <div className="selectedItemIcon" ref={sId + sSplitter + "selectedIcon"}></div>;
        var sIcon = ViewUtils.getIconUrl(oItem.iconKey);
        oIconOrColorView = (
            <div className="selectedItemIcon">
              <ImageFitToContainerView imageSrc={sIcon}/>
            </div>
        );
      } else if (_this.props.showColor && oItem.color) {
        var oStyle = {
          backgroundColor: oItem.color
        };
        oIconOrColorView = <div className="selectedItemColor" style={oStyle}></div>;
      }
      else if(_this.props.bShowIcon){
        oIconOrColorView = (<div className={"selectedItemIcon defaultIcon " + ClassNameFromBaseTypeDictionary[oItem.value]}></div>);
      }

      var bCanNotRemove = _this.props.cannotRemove;
      var oCrossIconView = !(_this.props.disabled || bCanNotRemove) ? <div className="crossIcon" ref={_this.setRef.bind(_this,sId + sSplitter + "crossIcon")} onClick={_this.handleCrossIconClicked.bind(_this, sId)}></div> : null;
      var sLabelClass = "selectedItemLabel ";
      (_this.props.disabled) && (sLabelClass += "disabled");
      var sLabel = CS.getLabelOrCode(oItem);
      aSummaryView.push(_this.getSelectedItemView(sId, sLabel, sLabelClass, oIconOrColorView, oCrossIconView));
      /* aSummaryView.push(
           <div className="selectedItems" ref={sId + sSplitter + "selected"} key={"selectedItems" + sId}>
             {oIconOrColorView}
             <TooltipView label={sLabel} placement="bottom">
               <div className={sLabelClass} ref={sId + sSplitter + "selectedLabel"}>{sLabel}</div>
             </TooltipView>
             {oCrossIconView}
           </div>
       );*/
      aMoreFilterItems.push(
          <div className="moreFiltersItemContainer"
               key={sId}
               ref={_this.setRef.bind(_this,sId + sSplitter + "more")}>
            <TooltipView label={sLabel} placement="bottom">
              <div className="moreFiltersItemLabel">{sLabel}</div>
            </TooltipView>
            {oCrossIconView}
          </div>
      );
    });
    return {
      summaryView: aSummaryView,
      moreFilterItems: aMoreFilterItems
    }
  };


  getMainMultiselectSearchView = () => {
    let {summaryView, moreFilterItems} = this.getSummaryView();
    return (
        <div className="multiSelectSearchView" ref={this.setRef.bind(this,"multiSelectSearchView")}
             onClick={null}>
          <div className="multiSelectSummary">
            {summaryView}
          </div>
          {this.getMoreSectionView(moreFilterItems)}
          {this.getNothingSelectedDom()}
        </div>
    );
  };

  /*
  getChildrenView = () => {
    let aChildrenViews = [];
    aChildrenViews.push (this.getMainMultiselectSearchView());
    return aChildrenViews;
  };
  */

  render() {
    var aItems = this.props.items;
    var aSelectedItems = this.state.selectedItems;
    var oAnchorOrigin = {horizontal: 'left', vertical: 'bottom'};
    var oTargetOrigin = {horizontal: 'left', vertical: 'top'};
    if (this.props.anchorOrigin) {
      oAnchorOrigin = this.props.anchorOrigin;
    }
    if (this.props.targetOrigin) {
      oTargetOrigin = this.props.targetOrigin;
    }
    var oMSSLabelView = null;
    var sLabel = this.props.label;

    if (!CS.isEmpty(sLabel) && this.props.showLabel) {
      var bHideTooltip = this.props.hideTooltip;
      oMSSLabelView = !bHideTooltip ?
                      <TooltipView placement="bottom" label={sLabel}>
                        <div className="mssLabel">{sLabel}</div>
                      </TooltipView> :
                      <div className="mssLabel">{sLabel}</div>;
    }

    var oSearchHandler = null;
    var oLoadMoreHandler = null;
    if (this.props.isLoadMoreEnabled) {
      oSearchHandler = this.handleMSSSearch;
      oLoadMoreHandler = this.handleMSSLoadMore;
    }

    let sClassName = "multiSelectSearchViewWrapper" + (this.props.disabled ? " disabled" : "");

    return (
        <div ref={this.domMounted} className={sClassName}>
          {oMSSLabelView}
          <ContextMenuViewNew
              selected={this.props.selected}
              contextMenuViewModel={this.getContextMenuModelList(aItems)}
              context={this.props.context}
              selectedItems={aSelectedItems}
              isMultiselect={this.props.isMultiSelect}
              onApplyHandler={this.onPopoverClosed}
              onClickHandler={CS.throttle(this.handleChildFilterClicked, 400, {'leading': false})}
              onDeleteHandler={this.props.onDeleteHandler}
              showDeleteIcon={this.props.showDeleteIcon}
              showSelectedItems={this.props.isMultiSelect}
              showColor={this.props.showColor}
              anchorOrigin={oAnchorOrigin}
              targetOrigin={oTargetOrigin}
              useAnchorElementWidth={true}
              searchHandler={oSearchHandler}
              loadMoreHandler={oLoadMoreHandler}
              disabled={this.props.disabled}
              searchText={this.props.searchText}
              onPopOverOpenedHandler={this.onPopOverOpen}
              onCreateHandler={this.handleMSSCreate}
              showCreateButton={this.props.showCreateButton}
              clearSearchOnPopoverClose={this.props.clearSearchOnPopoverClose}
              showSearch={this.props.showSearch}
              showHidePopoverHandler={this.props.showHidePopoverHandler}
              showPopover={this.props.showPopover}
              className={this.props.className}
              showCustomIcon={this.props.bShowIcon}
              showClearButton={this.props.showClearButton}
              onClearHandler={this.handleClearButtonClicked}
          >
            {this.getMainMultiselectSearchView()}
          </ContextMenuViewNew>
        </div>
    );
  }
}

export const view = CoreMultiSelectSearchView;
export const propTypes = oPropTypes;
export const events = oEvents;
