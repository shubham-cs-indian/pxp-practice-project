import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import { view as CustomPopoverView } from '../../viewlibraries/customPopoverView/custom-popover-view';
import ViewUtils from '../../viewlibraries/utils/view-library-utils';
import { view as ImageFitToContainerView } from '../imagefittocontainerview/image-fit-to-container-view';
import TooltipView from './../tooltipview/tooltip-view';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { view as LazyContextMenuView } from '../lazycontextmenuview/lazy-context-menu-view';
import ExceptionLogger from '../../libraries/exceptionhandling/exception-logger';
import ClassNameFromBaseTypeDictionary from '../../commonmodule/tack/class-name-base-types-dictionary';

const oEvents = {
  MULTI_SELECT_POPOVER_CLOSED: "multi_select_popover_closed",
  MULTI_SEARCH_BAR_CROSS_ICON_CLICKED: "multi_search_bar_cross_icon_clicked",
  MULTI_SELECT_POPOVER_OPENED: "multi_select_popover_opened",
  MULTI_SELECT_CREATE_HANDLE: "multi_select_create_handle",
  MULTI_SELECT_UPDATE_VIEW_PROPS: "multi_select_update_view_props"
};

let aPopoverPositions = ["left", "right", "top", "bottom"];

const oPropTypes = {
  isMultiSelect: ReactPropTypes.bool,
  bShowIcon: ReactPropTypes.bool,
  context: ReactPropTypes.string,
  disabled: ReactPropTypes.bool,
  selectedItems: ReactPropTypes.array,
  excludedItems: ReactPropTypes.array,
  onApply: ReactPropTypes.func,
  showColor: ReactPropTypes.bool,
  cannotRemove: ReactPropTypes.bool,
  customPlaceholder: ReactPropTypes.string,
  label: ReactPropTypes.string,
  showLabel: ReactPropTypes.bool,
  hideTooltip: ReactPropTypes.bool,
  showSelectedInDropdown: ReactPropTypes.bool,
  onPopOverOpen: ReactPropTypes.func,
  onCreateHandler: ReactPropTypes.func,
  showCreateButton: ReactPropTypes.bool,
  referencedData: ReactPropTypes.object,
  requestResponseInfo: ReactPropTypes.object,
  menuListHeight: ReactPropTypes.string,
  popoverStyle: ReactPropTypes.object,
  onClearHandler: ReactPropTypes.func,
  showClearButton: ReactPropTypes.bool,

  //------------------------------- optional props to set popover position: ------------------------------------------
  anchorOrigin: ReactPropTypes.shape({
    horizontal: ReactPropTypes.oneOf(aPopoverPositions),
    vertical: ReactPropTypes.oneOf(aPopoverPositions)
  }),
  targetOrigin: ReactPropTypes.shape({
    horizontal: ReactPropTypes.oneOf(aPopoverPositions),
    vertical: ReactPropTypes.oneOf(aPopoverPositions)
  }),
};
/**
 * @class LazyMSSView - use for multiple select search view.
 * @memberOf Views
 * @property {bool} [isMultiSelect] -  boolean which is used for multiselect context menu or not.
 * @property {bool} [bShowIcon] -  boolean which is used for bShowIcon or not.
 * @property {string} [context] - pass context name.
 * @property {bool} [disabled] -  boolean which is used for disabled or not.
 * @property {array} [selectedItems] -  array for selected item.
 * @property {array} [excludedItems] -  an array which contain excleded items.
 * @property {func} [onApply] -   function which is used for on apply changes event.
 * @property {bool} [showColor] -  boolean which is used for show or hide color.
 * @property {bool} [cannotRemove] -  boolean which is used for cannotRemove or not.
 * @property {string} [customPlaceholder] . pass customPlaceholder name.
 * @property {string} [label] - pass label name.
 * @property {bool} [showLabel] -  boolean which is used for show or hide label.
 * @property {bool} [hideTooltip] -  boolean which is used for hideTooltip or not.
 * @property {bool} [showSelectedInDropdown] -  boolean which is used for show or hide selected items in drop down.
 * @property {func} [onPopOverOpen] -  function which is used for onPopOverOpen event.
 * @property {func} [onCreateHandler] -  function which is used for onCreate context handler event.
 * @property {bool} [showCreateButton] -  boolean which is used for show create button or not.
 * @property {object} [referencedData] - pass ref data as a object.
 * @property {object} [requestResponseInfo] - pass object of requestResponseInfo.
 * @property {string} [menuListHeight] -  string of menu list height.
 * @property {custom} [anchorOrigin] - pass HTML elements.
 * @property {custom} [targetOrigin] - pass HTML elements.
 */

// @CS.SafeComponent
class LazyMSSView extends React.Component {

  constructor (props) {
    super(props);

    //this.store = null;

    this.state = {
      showMore: false,
      showDetails: false,
      searchString: "",
      isLoadMoreClicked: false,
      selectedItems: LazyMSSView.getClonedSelectedItems(props, null, props.context),
      context: props.context,
      store: null
    }

    this.setRef =( sRef, element) => {
      this[sRef] = element;
    };
  }

  static getDerivedStateFromProps(oNextProps, oState){
    let oReferencedData = oNextProps.referencedData || {};
    LazyMSSView.updateReferencedData(oReferencedData, oState);

    if (oState.isLoadMoreClicked) {
      return{
        isLoadMoreClicked: false,
        context: oNextProps.context
      }
    } else {
      return {
        selectedItems: LazyMSSView.getClonedSelectedItems(oNextProps, oState.store, oState.context),
        context: oNextProps.context
      };
    }
  }

  /*componentWillReceiveProps = (oNextProps) => {
    let oReferencedData = oNextProps.referencedData || {};
    this.updateReferencedData(oReferencedData);

    if (this.state.isLoadMoreClicked) {
      this.setState({
        isLoadMoreClicked: false
      });
    } else {
      this.setState({
        selectedItems: this.getClonedSelectedItems(oNextProps),
      });
    }
  }*/

  componentDidMount = () => {
    /** setTimeout() is used in componentDidMount() of Popover.js,
     * which causes delay in rendering Popover view,
     * hence it is used here for delay.
     */
    setTimeout(this.calculateSelectedItemCount);
  }

  componentDidUpdate = () => {
    /** setTimeout() is used in componentDidUpdate() of Popover.js,
     * which causes delay in updating Popover view,
     * hence it is used here for delay.
     */
    setTimeout(this.calculateSelectedItemCount);
  }

  shouldComponentUpdate = (oNextProps, oNextState) => {
    return !CS.isEqual(oNextProps, this.props) || !CS.isEqual(oNextState, this.state);
  }

  static updateReferencedData (oReferencedData, oState) {
    if (!CS.isEmpty(oState.store) && CS.isFunction(oState.store.updateReferencedData)) {
      oState.store.updateReferencedData(oReferencedData);
      let oUpdatedReferencedData = oState.store.getUpdatedReferencedData();
      let sContext = oState.context;
      EventBus.dispatch(oEvents.MULTI_SELECT_UPDATE_VIEW_PROPS, sContext, oUpdatedReferencedData);
    }
  }

  static getReferencedData (oProps, oStore) {
    if (!CS.isEmpty(oStore) && CS.isFunction(oStore.getReferencedData)) {
      return oStore.getReferencedData()
    }
    return oProps.referencedData;
  }

  updateStore = (oStore) => {
    this.setState({
      store: oStore
    })
    let oReferencedData = this.props.referencedData;
    LazyMSSView.updateReferencedData(oReferencedData, oStore);
  }

  handleMoreButtonClicked = (oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    let bCurrentState = this.state.showMore;
    this.setState({
      showMore: !bCurrentState,
      moreView: oEvent.currentTarget
    });
  }

  handleCloseMorePopoverButtonClicked = () => {
    let bCurrentState = this.state.showMore;
    this.setState({
      showMore: !bCurrentState
    });
  }

  static getClonedSelectedItems (oNextProps, oStore, sContext) {
    let aSelectedItems = [];
    if (oNextProps) {
      let oStoredReferencedData = LazyMSSView.getReferencedData(oNextProps, oStore);
      let oReferencedData = CS.isEmpty(oStoredReferencedData) ? oNextProps.referencedData : oStoredReferencedData;
      CS.forEach(oNextProps.selectedItems, function (sId) {
        try {
          let oItem = oReferencedData[sId];
          if (oItem) {
            aSelectedItems.push(oItem.id);
          }
        }
        catch (oException) {
          ExceptionLogger.error(oException);
          ExceptionLogger.error(sContext);
        }
      });

    }
    return aSelectedItems;
  }

  onPopoverClosed = (aSelectedItems) => {
    let aPreviousSelectedItems = this.props.selectedItems;
    if (!CS.isEqual(aSelectedItems, aPreviousSelectedItems)) {
      /*
      let aItems = this.store.getItemsData();
      this.addToReferencedData(aSelectedItems, aItems);
      */
      let oReferencedData = LazyMSSView.getReferencedData(this.props, this.state.store);
      if (CS.isFunction(this.props.onApply)) {
        this.props.onApply(aSelectedItems, oReferencedData);
      } else {
        EventBus.dispatch(oEvents.MULTI_SELECT_POPOVER_CLOSED, aSelectedItems, this.props.context, oReferencedData);
      }
    }
  }

  onPopOverOpen = (sContext) => {
    let oProps = this.props;
    if (CS.isFunction(oProps.onPopOverOpen)) {
      oProps.onPopOverOpen(sContext);
    } else {
      EventBus.dispatch(oEvents.MULTI_SELECT_POPOVER_OPENED, oProps.context);
    }
  }

  handleClearButtonClicked = (sContext) => {
    let oProps = this.props;
    oProps.onClearHandler(sContext);
  }

  handleCrossIconClicked = (sId, oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    let sContext = this.props.context;
    let aSelectedItems = CS.cloneDeep(this.state.selectedItems);
    if (CS.includes(aSelectedItems, sId)) {
      CS.remove(aSelectedItems, function (sItemId) {
        return sItemId == sId;
      });

      let oReferencedData = LazyMSSView.getReferencedData(this.props, this.state.store);
      if (CS.isFunction(this.props.onApply)) {
        this.props.onApply(aSelectedItems, oReferencedData);
      } else {
        EventBus.dispatch(oEvents.MULTI_SEARCH_BAR_CROSS_ICON_CLICKED, this, sContext, sId);
      }
    }
  }

  getNothingSelectedDom = () => {
    let bIsActive = this.state.showDetails;
    let aSelectedItems = this.state.selectedItems || [];
    let sPlaceholder = (this.props.customPlaceholder) ? this.props.customPlaceholder : getTranslation().NOTHING_IS_SELECTED;

    if (this.props.disabled && !aSelectedItems.length) {
      return null;
    } else if (!bIsActive && !aSelectedItems.length) {
      return (<div className="multiSelectNothingSelected">{sPlaceholder}</div>);
    }
    return null;
  }

  handleChildFilterClicked = (oItem) => {
    let sId = oItem.id;
    let bIsMultiSelect = this.props.isMultiSelect;
    let aSelectedItems = this.state.selectedItems || [];
    if (!bIsMultiSelect) {
      this.onPopoverClosed([sId]);
      this.setState({
        selectedItems: [sId]
      });
    } else {
      let aRemovedItems = CS.remove(aSelectedItems, function (sItemId) {
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

  calculateSelectedItemCount = () => {
    let _this = this;
    if (!CS.isEmpty(_this["multiSelectSearchView"])) {
      let aSelectedItems = _this.state.selectedItems;
      let sSplitter = ViewUtils.getSplitter();

      if (_this["moreContainer"]) {
        _this["moreContainer"].classList.remove('hideMe');
      }
      let iWidthToSubtract = (_this.props.isMultiSelect) ? (_this["moreContainer"].offsetWidth + 1) : 0;
      let iTotalWidth = Math.ceil(_this["multiSelectSearchView"].offsetWidth) - iWidthToSubtract;
      let iSum = 0;
      let iMoreCounter = 0;
      let aAdjustableRefIds = [];
      let oContainerDiv = this.props.forwardedRef;
      let iWidthOfContainer = oContainerDiv && oContainerDiv.offsetWidth ? oContainerDiv.offsetWidth : 0;

      CS.forEach(aSelectedItems, function (sId, iIndex) {
        let oSelectedLabelDOM = _this[sId + sSplitter + "selectedLabel"];
        let oSelectedIconDOM = _this[sId + sSplitter + "selectedIcon"];
        let oSelectedDOM = _this[sId + sSplitter + "selected"];
        let oMoreDOM = _this[sId + sSplitter + "more"];

        oSelectedDOM.classList.remove('hideMe');
        let iSelectedIconWidth = oSelectedIconDOM && oSelectedIconDOM.offsetWidth ? oSelectedIconDOM.offsetWidth : 0;
        //selectedItemLabel margin 10 + label padding 20 + crossIcon width 14 + crossIcon margin 4 = 30 + 14 + 4
        iSum = iSum + oSelectedLabelDOM.offsetWidth + iSelectedIconWidth + 48;

        if (iSum >= iTotalWidth && iIndex >= 1) {
          iMoreCounter++;
          oMoreDOM && oMoreDOM.classList.remove('hideMe');
          oSelectedDOM.classList.add('hideMe');
          if (iSum < (iTotalWidth + iWidthToSubtract)) {
            aAdjustableRefIds.push(sId + sSplitter);
          } else {
            aAdjustableRefIds = [];
          }
        } else if (iSum >= iTotalWidth && iIndex === 0) {
          oMoreDOM && oMoreDOM.classList.add('hideMe');
          if (iSum < (iTotalWidth + Math.ceil(_this["moreContainer"].offsetWidth + 1)) && !iMoreCounter) {
            oSelectedDOM.classList.remove('hideMe');
          } else {
            if (_this.props.cannotRemove) {
              //selectedItemMargin(7) + labelMargin(17) = 24
                iWidthToSubtract = iSelectedIconWidth + 10;
            } else {
              //selectedItemMargin(7) + labelMargin(17) + crossIcon(14) + crossIconMargin(4) = 42
              iWidthToSubtract = iSelectedIconWidth + 42;
            }
            let iNewSelectedLabelDOMWidth = iTotalWidth - iWidthToSubtract;
            if(iNewSelectedLabelDOMWidth > iWidthOfContainer && iWidthOfContainer != 0){
              iNewSelectedLabelDOMWidth = iWidthOfContainer - 20  ;
            }
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
            let oSelectedDOM = _this[sRefId + "selected"];
            let oMoreDOM = _this[sRefId + "more"];
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
          _this["moreCounter"].innerHTML = iMoreCounter;
        }
      }
    }
  }

  getMoreSectionView = (aMoreFilterItems) => {
    let sMoreFiltersSectionClassName = this.state.showMore ? "moreFilters" : "moreFilters invisible";
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

  handleMSSCreate = (sSearchText) => {
    if (this.props.onCreateHandler) {
      this.props.onCreateHandler(sSearchText);
    } else {
      let sContext = this.props.context;
      EventBus.dispatch(oEvents.MULTI_SELECT_CREATE_HANDLE, sContext, sSearchText);
    }
  };

  getIconColorDom = (oItem) => {
    let oIconOrColorView = null;
    let bShowIcon = this.props.bShowIcon;
    if (bShowIcon && oItem.hasOwnProperty('iconKey') && oItem.iconKey) {
      let sIcon = ViewUtils.getIconUrl(oItem.iconKey);
      oIconOrColorView = (
          <div className="selectedItemIcon">
            <ImageFitToContainerView imageSrc={sIcon}/>
          </div>
      );
    } else if (this.props.showColor && oItem.color) {
      let oStyle = {
        backgroundColor: oItem.color
      };
      oIconOrColorView = <div className="selectedItemColor" style={oStyle}></div>;
    } else if (bShowIcon) {
      let sType = CS.isEmpty(oItem.baseType) ? oItem.type : oItem.baseType;
      let sIconClassName = "selectedItemIcon defaultIcon ";
      if (ClassNameFromBaseTypeDictionary[sType]) {
        sIconClassName += ClassNameFromBaseTypeDictionary[sType];
        oIconOrColorView = <div className={sIconClassName}></div>;
      }
    }
    return oIconOrColorView;
  };

  render () {
    let _this = this;
    let aSummaryView = [];
    let aSelectedItems = this.state.selectedItems;
    let aMoreFilterItems = [];
    let sSplitter = ViewUtils.getSplitter();
    let oReferencedData = LazyMSSView.getReferencedData(this.props, this.state.store);

    CS.forEach(aSelectedItems, function (sId) {
      try {
        let oItem = oReferencedData[sId];
        let oIconOrColorView = _this.getIconColorDom(oItem);

        let bCanNotRemove = _this.props.cannotRemove;
        let oCrossIconView = !(_this.props.disabled || bCanNotRemove) ?
                             <div className="crossIcon" onClick={_this.handleCrossIconClicked.bind(_this, sId)}></div> : null;
        let sLabelClass = "selectedItemLabel ";
        (_this.props.disabled) && (sLabelClass += "disabled");
        let sLabel = CS.getLabelOrCode(oItem);
        aSummaryView.push(
            <div className="selectedItems" ref={_this.setRef.bind(_this,sId + sSplitter + "selected")} key={"selectedItems" + sId}>
              {oIconOrColorView}
              <TooltipView label={sLabel} placement="bottom">
                <div className={sLabelClass} ref={_this.setRef.bind(_this,sId + sSplitter + "selectedLabel")}>{sLabel}</div>
              </TooltipView>
              {oCrossIconView}
            </div>
        );

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
      } catch (oException) {
        ExceptionLogger.error(oException);
      }

    });

    var oAnchorOrigin = this.props.anchorOrigin || {horizontal: 'left', vertical: 'bottom'};
    var oTargetOrigin = this.props.targetOrigin || {horizontal: 'left', vertical: 'top'};
    let oMSSLabelView = null;
    let sLabel = this.props.label;

    if (!CS.isEmpty(sLabel) && this.props.showLabel) {
      let bHideTooltip = this.props.hideTooltip;
      oMSSLabelView = !bHideTooltip ?
          <TooltipView placement="bottom" label={sLabel}>
            <div className="mssLabel">{sLabel}</div>
          </TooltipView> :
          <div className="mssLabel">{sLabel}</div>;
    }

    return (
        <div ref={this.domMounted} className="multiSelectSearchViewWrapper">
          {oMSSLabelView}
          <LazyContextMenuView
              selected={this.props.selected}
              context={this.props.context}
              selectedItems={aSelectedItems}
              excludedItems={this.props.excludedItems}
              isMultiselect={this.props.isMultiSelect}
              onApplyHandler={this.onPopoverClosed}
              onClickHandler={this.handleChildFilterClicked}
              showSelectedItems={this.props.isMultiSelect}
              showColor={this.props.showColor}
              anchorOrigin={oAnchorOrigin}
              targetOrigin={oTargetOrigin}
              useAnchorElementWidth={true}
              disabled={this.props.disabled}
              onPopOverOpenedHandler={this.props.requestResponseInfo ? null : this.onPopOverOpen}
              onCreateHandler={this.handleMSSCreate}
              showCreateButton={this.props.showCreateButton}
              updateStore={this.updateStore}
              requestResponseInfo={this.props.requestResponseInfo}
              menuListHeight={this.props.menuListHeight}
              popoverStyle = {this.props.popoverStyle}
              showCustomIcon = {this.props.bShowIcon}
              showClearButton={this.props.showClearButton}
              onClearHandler={this.handleClearButtonClicked}
          >
            <div className="multiSelectSearchView" ref={this.setRef.bind(this,"multiSelectSearchView")}
                 onClick={null}>
              <div className="multiSelectSummary">
                {aSummaryView}
              </div>
              {this.getMoreSectionView(aMoreFilterItems)}
              {this.getNothingSelectedDom()}

            </div>
          </LazyContextMenuView>
        </div>
    );
  }
}


LazyMSSView.propTypes = oPropTypes;

export const view = LazyMSSView;
export const events = oEvents;
