import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as CustomPopoverView } from '../../viewlibraries/customPopoverView/custom-popover-view';
import { view as ContextMenuItemView } from './context-menu-item-view.js';

const oEvents = {};

let aPopoverPositions = ["left", "right", "top", "bottom"];

const oPropTypes = {
  contextMenuViewModel: ReactPropTypes.array.isRequired,
  context: ReactPropTypes.string,
  onClickHandler: ReactPropTypes.func,
  onApplyHandler: ReactPropTypes.func,
  searchHandler: ReactPropTypes.func,
  searchText: ReactPropTypes.string,
  loadMoreHandler: ReactPropTypes.func,
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
  onPopOverOpenedHandler: ReactPropTypes.func,
  isMovable: ReactPropTypes.bool,
  onCreateHandler: ReactPropTypes.func,
  onItemsChecked: ReactPropTypes.func,
  showCreateButton: ReactPropTypes.bool,
  showPopover: ReactPropTypes.bool,
  showHidePopoverHandler: ReactPropTypes.func,
  menuListHeight: ReactPropTypes.string,
  showSearch: ReactPropTypes.bool,
  showArrowHead: ReactPropTypes.bool,
  clearSearchOnPopoverClose: ReactPropTypes.bool,
  onDeleteHandler: ReactPropTypes.func,
  showDeleteIcon: ReactPropTypes.bool,
  showDefaultIcon: ReactPropTypes.bool,
  trigger: ReactPropTypes.array, // Array of events(React events e.g onMouseOver) on which context menu should get trigger.

  //------------------------------- optional props to set popover position: ------------------------------------------
  anchorOrigin: ReactPropTypes.shape({
    horizontal: ReactPropTypes.oneOf(aPopoverPositions),
    vertical: ReactPropTypes.oneOf(aPopoverPositions)}),
  targetOrigin: ReactPropTypes.shape({
    horizontal: ReactPropTypes.oneOf(aPopoverPositions),
    vertical: ReactPropTypes.oneOf(aPopoverPositions)}),
  // -----------------------------------------------------------------------------------------------------------------

};
/**
 * @class - ContextMenuViewNew - use for context menu view dropdown button.
 * @memberOf Views
 * @property {array} contextMenuViewModel - array of context menu model.
 * @property {string} [context] - context.
 * @property {func} [onClickHandler] -  function which is used for onclick button event.
 * @property {func} [onApplyHandler] -   function which is used for on apply changes event.
 * @property {func} [searchHandler] -  function which is used for search handle event.
 * @property {string} [searchText] -  string of search text.
 * @property {func} [loadMoreHandler] -  function which is used for load more event.
 * @property {object} [style] -  object for style the context.
 * @property {bool} [showSelectedItems] -  boolean which is used for show or hide selected items.
 * @property {array} [selectedItems] -  array for selected item.
 * @property {object} [children] -  object of children of button.
 * @property {string} [className] string of className
 * @property {bool} [isMultiselect] -  boolean which is used for multiselect context menu or not.
 * @property {bool} [showColor] -  boolean which is used for show or hide color.
 * @property {bool} [showCustomIcon] -  boolean which is used for show or hide custom icon.
 * @property {bool} [useAnchorElementWidth] -  boolean which is used for useAnchorElementWidth.
 * @property {bool} [disabled] -  boolean which is used for disabled button or not.
 * @property {func} [onPopOverOpenedHandler] -  function which is used for onPopOverOpenedHandler event.
 * @property {bool} [isMovable] -  boolean which is used for movable or not.
 * @property {func} [onCreateHandler] -  function which is used for onCreate context handler event.
 * @property {func} [onItemsChecked] -  function which is used for on item checked context handler event.
 * @property {bool} [showCreateButton] -  boolean which is used for show create button or not.
 * @property {bool} [showPopover] -  boolean which is used for showPopover.
 * @property {func} [showHidePopoverHandler] -  function which is used for showHidePopoverHandler event.
 * @property {string} [menuListHeight] -  string of menu list height.
 * @property {bool} [showSearch] -  boolean which is used for show search or not.
 * @property {bool} [showArrowHead] -  boolean which is used for show or hide ArrowHead.
 * @property {bool} [clearSearchOnPopoverClose] -  boolean which is used for clearSearchOnPopoverClose or not.
 * @property {func} [onDeleteHandler] -  function which is used for onDeleteHandler event.
 * @property {bool} [showDeleteIcon] -  boolean which is used for show or hide DeleteIcon.
 * @property {array} [trigger] -  array of trigger change element.
 * @property {custom} [anchorOrigin] - HTML div element.
 * @property {custom} [targetOrigin] - HTML div element.
 */

// @CS.SafeComponent
class ContextMenuViewNew extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      showDropdown: false
    };

    this.triggerElement = React.createRef();

    this.updatePosition = CS.noop;
  }

  componentDidUpdate (prevProps, prevState, snapshot) {
    /** Update popover position only if popover is opened **/
    let bIsPopOverOpen = CS.isFunction(this.props.showHidePopoverHandler) ? this.props.showPopover : this.state.showDropdown;
    bIsPopOverOpen && this.updatePosition();
  }

  updatePopoverPosition = (fUpdatePosition) => {
    if(fUpdatePosition) {
      this.updatePosition = fUpdatePosition;
    }
  };

  closeDropdown =()=> {
    window.dontRaise = true; //Do not remove
    if (CS.isFunction(this.props.showHidePopoverHandler)) {
      this.props.showHidePopoverHandler(false, this.props.context)
    } else {
      if (this.props.searchText && this.props.clearSearchOnPopoverClose && CS.isFunction(this.props.searchHandler)) {
        this.props.searchHandler("")
      }

      this.setState({
        showDropdown: false
      });
    }
  }

  showDropdown =(oEvent)=> {
    if(!oEvent.nativeEvent.dontRaise) {
      var oDOM = this.triggerElement.current;
      var oProps = this.props;
      if (oProps.onPopOverOpenedHandler) {
        oProps.onPopOverOpenedHandler(oProps.context);
      }
      this.setState({
        showDropdown: true,
        anchorElement: oDOM
      });

    }
  }

  onApplyHandler =(aSelectedIds)=> {
    if (this.props.onApplyHandler) {
      this.props.onApplyHandler(aSelectedIds);
    }
    this.closeDropdown();
  }

  onClickHandler =(sSelectedId)=> {
    if (this.props.onClickHandler) {
      this.props.onClickHandler(sSelectedId);
    }
    this.closeDropdown();
  }

  onCreateHandler =(sSearchValue)=> {
    if (this.props.onCreateHandler) {
      this.props.onCreateHandler(sSearchValue);
    }
    this.closeDropdown();
  };

  onDeleteHandler = (oModule) => {
    if (this.props.onDeleteHandler) {
      this.props.onDeleteHandler(oModule);
    }
    this.closeDropdown();
  };

  handleContextMenuOnClick = () => {
    window.dontRaise = true; //Do not remove
  }

  onClearButtonHandler = () => {
    if (this.props.onClearHandler) {
      this.props.onClearHandler();
    }
    this.closeDropdown();
  }

  onItemsChecked = (aCheckedItems, bIsCheckUnCheckAll) => {
    if (this.props.onItemsChecked) {
      this.props.onItemsChecked(aCheckedItems, bIsCheckUnCheckAll);
    }
    else {
      /** Added empty setstate to update the position of popover**/
      this.setState({});
    }
  };

  render() {
    var sClassName = (this.props.selected && this.state.showDropdown) ? "contextMenuViewNewContainer popoverVisible selected " : (this.state.showDropdown) ? "contextMenuViewNewContainer popoverVisible " : "contextMenuViewNewContainer ";
    if(this.props.className) {
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
    let oPopoverStyle = this.props.showArrowHead == true ? {
      boxShadow: "none",
      backgroundColor: "transparent",
      maxWidth: '2300px',
      overflow: 'hidden'
    } : {
      maxWidth: '2300px',
      transform: "none",
      overflow: 'hidden'
    };

    var oTriggerEvents = {"onClick": fShowDropdownHandler};
    var aTrigger = this.props.trigger; // Array of react events.
    if (aTrigger && aTrigger.length) {
      oTriggerEvents = {};
      CS.each(aTrigger, function (sTrigger) {
        oTriggerEvents[sTrigger] = fShowDropdownHandler
      })
    }

    return (
        <div className={sClassName}>
          <CustomPopoverView
              className={"popover-root"}
              open={CS.isFunction(this.props.showHidePopoverHandler) ? this.props.showPopover : this.state.showDropdown}
              anchorEl={this.state.anchorElement}
              anchorOrigin={oAnchorOrigin}
              transformOrigin={oTargetOrigin}
              style={ oPopoverStyle}
              onClose={this.closeDropdown}
              updatePosition={this.updatePopoverPosition}
          >
            <div className={this.props.showArrowHead == true ? "contextMenuPopoverArrowHeadWrapper" : ""} onClick={this.handleContextMenuOnClick}>
              {this.props.showArrowHead == true ? <div className="arrowSection"></div> : null}
              <div className="contextMenuItemViewWrapper">
                <ContextMenuItemView contextMenuViewModel={this.props.contextMenuViewModel}
                                     isMultiselect={this.props.isMultiselect}
                                     onClickHandler={this.onClickHandler}
                                     onApplyHandler={this.onApplyHandler}
                                     onCreateHandler={this.onCreateHandler}
                                     onDeleteHandler={this.onDeleteHandler}
                                     showDeleteIcon={this.props.showDeleteIcon}
                                     showCreateButton={this.props.showCreateButton}
                                     searchHandler={this.props.searchHandler}
                                     searchText={this.props.searchText}
                                     loadMoreHandler={this.props.loadMoreHandler}
                                     style={oStyle}
                                     showSelectedItems={this.props.showSelectedItems}
                                     showColor={this.props.showColor}
                                     showCustomIcon={this.props.showCustomIcon}
                                     isMovable={this.props.isMovable}
                                     menuListHeight={this.props.menuListHeight}
                                     selectedItems={this.props.selectedItems}
                                     excludedItems={this.props.excludedItems}
                                     onItemsChecked={this.onItemsChecked}
                                     showSearch={this.props.showSearch}
                                     showClearButton={this.props.showClearButton}
                                     onClearHandler={this.onClearButtonHandler}
                                     showDefaultIcon ={this.props.showDefaultIcon}>
                </ContextMenuItemView>
              </div>
            </div>
          </CustomPopoverView>
          <div className="triggerElement" ref={this.triggerElement} {...oTriggerEvents}>
            {this.props.children}
          </div>
        </div>
    );
  }
}

ContextMenuViewNew.propTypes = oPropTypes;

export const view = ContextMenuViewNew;
export const events = oEvents;
export const propTypes = oPropTypes;
