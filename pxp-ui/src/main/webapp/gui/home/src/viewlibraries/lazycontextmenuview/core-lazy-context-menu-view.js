import React from 'react';
import CS from '../../libraries/cs';
import ReactPropTypes from 'prop-types';
import { view as CustomPopoverView } from '../customPopoverView/custom-popover-view';
import { view as LazyContextMenuItemView } from './lazy-context-menu-item-view.js';
import LazyContextMenuViewStore from './store/lazy-context-menu-view-store';
import AjaxMethodContext from '../../commonmodule/HOC/ajax-method-context'

const oEvents = {};

let aPopoverPositions = ["left", "right", "top", "bottom"];

const oPropTypes = {
  context: ReactPropTypes.string,
  onClickHandler: ReactPropTypes.func,
  onApplyHandler: ReactPropTypes.func,
  style: ReactPropTypes.object,
  showSelectedItems: ReactPropTypes.bool,
  selectedItems: ReactPropTypes.array,
  excludedItems: ReactPropTypes.array,
  children: ReactPropTypes.object,
  className: ReactPropTypes.string,
  isMultiselect: ReactPropTypes.bool,
  showColor: ReactPropTypes.bool,
  showCustomIcon: ReactPropTypes.bool,
  useAnchorElementWidth: ReactPropTypes.bool, //if true, anchor element's (children view's) width will be used for popover
  disabled: ReactPropTypes.bool,
  key: ReactPropTypes.string,
  onPopOverOpenedHandler: ReactPropTypes.func,
  isMovable: ReactPropTypes.bool,
  onCreateHandler: ReactPropTypes.func,
  showCreateButton: ReactPropTypes.bool,
  hideApplyButton: ReactPropTypes.bool,
  showPopover: ReactPropTypes.bool,
  showHidePopoverHandler: ReactPropTypes.func,
  menuListHeight: ReactPropTypes.string,
  showSearch: ReactPropTypes.bool,
  showArrowHead: ReactPropTypes.bool,
  popoverStyle: ReactPropTypes.object,
  postMethodToCall: ReactPropTypes.func,
  showClearButton: ReactPropTypes.bool,
  onClearHandler: ReactPropTypes.func,
  showDefaultIcon: ReactPropTypes.bool,

  //------------------------------- optional props to set popover position: ------------------------------------------
  anchorOrigin: ReactPropTypes.shape({
    horizontal: ReactPropTypes.oneOf(aPopoverPositions),
    vertical: ReactPropTypes.oneOf(aPopoverPositions)
  }),
  targetOrigin: ReactPropTypes.shape({
    horizontal: ReactPropTypes.oneOf(aPopoverPositions),
    vertical: ReactPropTypes.oneOf(aPopoverPositions)
  }),
  // -----------------------------------------------------------------------------------------------------------------

  updateStore: ReactPropTypes.func,
  requestResponseInfo: ReactPropTypes.object
};
/**
 * @class LazyContextMenuView
 * @memberOf Views
 * @property {string} [context] - Used to differentiate which operation will be performed.
 * @property {func} [onClickHandler] - Executes when item is clicked.
 * @property {func} [onApplyHandler] - Executes when apply button is clicked(apply button are used to select multiple items from drop down).
 * @property {object} [style] - Passing CSS style for LazyContextMenuView.
 * @property {bool} [showSelectedItems] - To show selected items in dropdown list.
 * @property {array} [selectedItems] - Contains selected items.
 * @property {array} [excludedItems] - Contains items which are excluded from dropdown list.
 * @property {object} [children] - Contains DOM.
 * @property {string} [className] - Contains class name for LazyContextMenuView.
 * @property {bool} [isMultiselect] - To select multiple items from dropdowm list flag is set to true and vice versa.
 * @property {bool} [showColor] - To change the style of item, if true(ex.display = 'block'; backgroundColor = sColor).
 * @property {bool} [showCustomIcon] - To show custom icons for items in dropdown list.
 * @property {bool} [useAnchorElementWidth] - To use Anchor Element width or not.
 * @property {bool} [disabled] - Used for disabling LazyContextMenuView.
 * @property {func} [onPopOverOpenedHandler] - Executes when LazyContextMenuView is clicked.
 * @property {bool} [isMovable] - Used for making draggable dropdown list.
 * @property {func} [onCreateHandler] - Executes when create button is clicked.
 * @property {bool} [showCreateButton] - To show create button on dropdown list.
 * @property {bool} [hideApplyButton] - To hide the apply button from dropdown (when apply button is hidden Items will be appliied on click).
 * @property {bool} [showPopover] - Used for toggle(show/hide) dropdown.
 * @property {func} [showHidePopoverHandler] - Executes when dropdown is closed.
 * @property {string} [menuListHeight] - Height for menu list.
 * @property {bool} [showSearch] - To show search bar on dropdown list.
 * @property {bool} [showArrowHead] - If true showing arrow head for dropdown.
 * @property {custom} [anchorOrigin] - This is the point on the anchor where the popover's anchorEl will attach to.
 * This is not used when the anchorReference is 'anchorPosition'.
 * Options: vertical: [top, center, bottom]; horizontal: [left, center, right].
 * @property {custom} [targetOrigin]
 * @property {func} [updateStore] -
 * @property {object} [requestResponseInfo] - Request data for getting items from backend.
 */

// @AjaxMethodContext
class LazyContextMenuView extends React.Component {

  constructor (props) {
    super(props);
    this.state = {
      showDropdown: false,
      store: {},
      context: props.context
    };

    this.updatePosition = CS.noop;
    this.triggerElement = React.createRef();
    this.initializeStore();
  }

  static getDerivedStateFromProps (oNextProps, oState) {
    if (oNextProps.context !== oState.context) {
      let oStore = oState.store;
      oStore && oStore.resetStoreProps(oNextProps.requestResponseInfo);
      if (CS.isFunction(oNextProps.updateStore)) {
        oNextProps.updateStore(oStore);
      }
      return {
        store: oStore,
        context: oNextProps.context
      }
    }
    return null;
  }

  /*componentWillReceiveProps = (oNextProps) => {
    if(oNextProps.context != this.props.context) {
      this.initializeStore(oNextProps);
    }
  }*/

  initializeStore = (oNextProps) => {
    var oProps = oNextProps;
    if(!oProps) {
      oProps = this.props;
    }

    this.state.store = new LazyContextMenuViewStore(oProps.requestResponseInfo, this.stateChanged, oProps.postMethodToCall);
    if (CS.isFunction(oProps.updateStore)) {
      oProps.updateStore(this.state.store);
    }
  }

  stateChanged = () => {
    this.setState({});
    this.updatePosition();
  }

  componentWillUnmount() {
  }

  closeDropdown = () => {
    if (CS.isFunction(this.props.showHidePopoverHandler)) {
      this.props.showHidePopoverHandler(false, this.props.context)
    } else {
      this.setState({
        showDropdown: false
      });
    }
    //Reset props when pop-over close.
    this.state.store.resetPropsOnDropdownClose();
  }

  showDropdown = (oEvent) => {
    if (!oEvent.nativeEvent.dontRaise) {
      var oDOM = this.triggerElement.current;
      var oProps = this.props;
      if (oProps.onPopOverOpenedHandler) {
        oProps.onPopOverOpenedHandler(oProps.context);
      }
      this.setState({
        showDropdown: true,
        anchorElement: oDOM
      });
      this.state.store.handleSearchClicked("");
    }
  }

  addToReferencedData = (aSelectedItems, aItems) => {
    if (!CS.isEmpty(this.state.store) && CS.isFunction(this.state.store.addToReferencedData)) {
      this.state.store.addToReferencedData(aSelectedItems, aItems)
    }
  };

  getItemLabel = (oItem) => {
    return CS.getLabelOrCode(oItem);
  };

  onApplyHandler = (aSelectedIds) => {
    let __props = this.props;
    let aItems = this.state.store.getItemsData();
    //this.addToReferencedData(aSelectedIds, aItems);
    let oReferencedData = this.state.store.getReferencedData();

    if (__props.onApplyHandler) {
      /** raise apply event only if any changes are made **/
      let aAddedItems = CS.difference(aSelectedIds, __props.selectedItems);
      let aRemovedItems = CS.difference(__props.selectedItems, aSelectedIds);
      (CS.isNotEmpty(aAddedItems) || CS.isNotEmpty(aRemovedItems)) && __props.onApplyHandler(aSelectedIds, oReferencedData, __props.context);
    }
    this.closeDropdown();
  }
  addReferencedData = (aSelectedIds) => {
    let aItems = this.state.store.getItemsData();
    this.addToReferencedData(aSelectedIds, aItems);
  }

  onClickHandler = (oItem) => {
    let __props = this.props;
    let aItems = this.state.store.getItemsData();
    let sItemId = oItem.id;
    this.addToReferencedData([sItemId], aItems);
    let oReferencedData = this.state.store.getReferencedData();

    if (__props.onClickHandler) {
      __props.onClickHandler(oItem, oReferencedData, __props.context);
    }
    // If applyButton is hidden then the onClickHandler function is called on click of item and drop down is not to be closed in that case.
    !this.props.hideApplyButton && this.closeDropdown();
  }

  onCreateHandler = (sSearchValue) => {
    if (this.props.onCreateHandler) {
      this.props.onCreateHandler(sSearchValue);
    }
    this.closeDropdown();
  }

  onClearButtonHandler = () => {
    if (this.props.onClearHandler) {
      this.props.onClearHandler();
    }
    this.closeDropdown();
  }

  getContextMenuItemsList = (aItems) => {
    let aItemDataToReturn = [];
    let sContext = this.props.context;
    let bShowSelectedInDropdown = this.props.showSelectedItems;
    let bIsMultiSelect = this.props.isMultiselect;
    let aSelectedItems = this.props.selectedItems;
    let aExcludedItems = this.props.excludedItems;

    //Do not show selected items for Single Select && (Multi-Select & don't show selectedItem)
    let bRejectSelectedItemFromList = !(bIsMultiSelect && bShowSelectedInDropdown);

    CS.forEach(aItems, function (oItem) {
      if(bRejectSelectedItemFromList && CS.includes(aSelectedItems,oItem.id)){
        return;
      }

      if(!CS.isEmpty(aExcludedItems) && CS.includes(aExcludedItems,oItem.id)){
        return;
      }

      let oItemDataToReturn = CS.cloneDeep(oItem);
      oItemDataToReturn.context = sContext;
      oItemDataToReturn.label = CS.getLabelOrCode(oItem);
      aItemDataToReturn.push(oItemDataToReturn);

    });

    return aItemDataToReturn;
  }

  /**
   *
   * To update Popover position for lazy loading of data
   *
   * Need to change this if performance issues occurs - Instead of calling it in componentDidUpdate need to think
   * about a strategy when actually the repositioning should happen
   *
   * @param fUpdatePosition - It contains updatePosition function which can be called to reposition Popover position whenever needed
   *
   */
  updatePopoverPosition = (fUpdatePosition) => {
    if(fUpdatePosition) {
      this.updatePosition = fUpdatePosition;
    }
  };

  render () {
    var sClassName = (this.props.selected && this.state.showDropdown) ? "contextMenuViewNewContainer popoverVisible selected " : (this.state.showDropdown) ? "contextMenuViewNewContainer popoverVisible " : "contextMenuViewNewContainer ";
    if (this.props.className) {
      sClassName += this.props.className;
    }

    //NOTE : If changes in AnchorOrigin OR TargetOrigin are needed, pass them through props. Do not edit here.
    var oAnchorOrigin = this.props.anchorOrigin || {horizontal: 'right', vertical: 'bottom'};
    var oTargetOrigin = this.props.targetOrigin || {horizontal: 'right', vertical: 'top'};

    var oStyle = this.props.style || {};
    if (this.props.useAnchorElementWidth && this.state.anchorElement) {
      oStyle.width = this.state.anchorElement.clientWidth + "px";
    }

    var fShowDropdownHandler = (this.props.disabled) ? null : this.showDropdown;
    let oPopoverStyle = this.props.popoverStyle || {};

    if(this.props.showArrowHead) {
      oPopoverStyle.boxShadow = "none";
      oPopoverStyle.backgroundColor = "transparent";
    }

    let oStore = this.state.store;

    return (
        <div className={sClassName} key={this.props.key}>
          <CustomPopoverView
              className="popover-root"
              open={CS.isFunction(this.props.showHidePopoverHandler) ? this.props.showPopover : this.state.showDropdown}
              updatePosition={this.updatePopoverPosition}
              anchorEl={this.state.anchorElement}
              anchorOrigin={oAnchorOrigin}
              transformOrigin={oTargetOrigin}
              style={ oPopoverStyle}
              onClose={this.closeDropdown}>
            <div className={this.props.showArrowHead == true ? "contextMenuPopoverArrowHeadWrapper" : ""}>
              {this.props.showArrowHead == true ? <div className="arrowSection"></div> : null}
              <div className="contextMenuItemViewWrapper">
                <LazyContextMenuItemView
                    selected={this.props.selected}
                    contextMenuItems={this.getContextMenuItemsList(oStore.getItemsData())}
                    isMultiselect={this.props.isMultiselect}
                    onClickHandler={this.onClickHandler}
                    onApplyHandler={this.onApplyHandler}
                    getItemLabel={this.getItemLabel}
                    onCreateHandler={this.onCreateHandler}
                    showCreateButton={this.props.showCreateButton}
                    hideApplyButton={this.props.hideApplyButton}
                    searchHandler={oStore.handleSearchClicked}
                    searchText={oStore.getSearchText()}
                    loadMoreHandler={oStore.handleLoadMoreClicked}
                    addReferencedData={this.addReferencedData}
                    style={oStyle}
                    showSelectedItems={true}
                    showColor={this.props.showColor}
                    showCustomIcon={this.props.showCustomIcon}
                    isMovable={this.props.isMovable}
                    menuListHeight={this.props.menuListHeight}
                    selectedItems={this.props.selectedItems}
                    showSearch={this.props.showSearch}
                    showClearButton={this.props.showClearButton}
                    onClearHandler={this.onClearButtonHandler}
                    updatePosition={this.updatePosition}
                    showDefaultIcon={this.props.showDefaultIcon}>
                </LazyContextMenuItemView>
              </div>
            </div>
          </CustomPopoverView>
          <div className="triggerElement" ref={this.triggerElement} onClick={fShowDropdownHandler}>
            {this.props.children}
          </div>
        </div>
    );
  }
}

LazyContextMenuView.propTypes = oPropTypes;


export default {
  view: AjaxMethodContext(LazyContextMenuView),
  events: oEvents,
  propTypes: oPropTypes
}
