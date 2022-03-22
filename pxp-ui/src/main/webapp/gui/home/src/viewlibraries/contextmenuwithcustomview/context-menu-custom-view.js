import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as CustomPopoverView } from '../../viewlibraries/customPopoverView/custom-popover-view';
import { view as ContextMenuItemView } from './context-menu-custom-item-view.js';
import ContextMenuViewModel from './../../viewlibraries/contextmenuwithcustomview/model/context-menu-custom-view-model';

const oEvents = {};

let aPopoverPositions = ["left", "right", "top", "bottom"];

const oPropTypes = {
  contextMenuViewModel: ReactPropTypes.oneOfType([
    ReactPropTypes.arrayOf(ReactPropTypes.instanceOf(ContextMenuViewModel)),
    ReactPropTypes.arrayOf(ReactPropTypes.object),
  ]).isRequired,
  context: ReactPropTypes.string,
  onClickHandler: ReactPropTypes.func,
  onApplyHandler: ReactPropTypes.func,
  searchHandler: ReactPropTypes.func,
  searchText: ReactPropTypes.string,
  loadMoreHandler: ReactPropTypes.func,
  style: ReactPropTypes.object,
  showSelectedItems: ReactPropTypes.bool,
  selectedItems: ReactPropTypes.array,
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
 * @class ContextMenuCustomView
 * @memberOf Views
 * @property {custom} contextMenuViewModel - Contains array of items.
 * @property {string} [context] - Used to differentiate which operation will be performed or describes the screen(for example: userProfilePopover)
 * @property {func} [onClickHandler] - Executes when dropdown item clicked.
 * @property {func} [onApplyHandler] - Executes when apply button clicked.(apply button are used to select multiple items from drop down).
 * @property {func} [searchHandler] - Executes when text is searched.
 * @property {string} [searchText] - Contains text which have to be searched.
 * @property {func} [loadMoreHandler] - Executes after load more option is clicked.
 * @property {object} [style] - Style for drop down view.
 * @property {bool} [showSelectedItems] - To show selected items in dropdown list.
 * @property {array} [selectedItems] - Contains selected items.
 * @property {object} [children] - Passing view which is used to display selected items.
 * @property {string} [className] - Class name for ContextMenuCustomView.
 * @property {bool} [isMultiselect] - To select multiple items from dropdowm list flag is set to true and vice versa.
 * @property {bool} [showColor] - To change the style of item, if true(ex.display = 'block'; backgroundColor = sColor).
 * @property {bool} [showCustomIcon] - To show custom icons for items in dropdown list.
 * @property {bool} [useAnchorElementWidth] - If true we use Anchor Element Width, else not.
 * @property {bool} [disabled] - If true ContextMenuCustomView disabled .
 * @property {func} [onPopOverOpenedHandler] - Executes when clicking on ContextMenuCustomView.
 * @property {bool} [isMovable] - Used for making draggable dropdown list.
 * @property {func} [onCreateHandler] - Executes when create button is clicked.
 * @property {string} [menuListHeight] - Height for menu list.
 * @property {func} [onItemsChecked] - Executes when item checkbox is clicked.
 * @property {bool} [showCreateButton] - To show create button on dropdown list.
 * @property {bool} [showPopover] - Used for toggle(show/hide) dropdown.
 * @property {func} [showHidePopoverHandler] - Executes when dropdown is closed.
 * @property {bool} [showSearch] - To show search bar on dropdown list.
 * @property {bool} [showArrowHead] - If true - Showing arrow head for dropdown list
 * @property {bool} [clearSearchOnPopoverClose] - Used to clear the search text after closing popover.
 * @property {custom} [anchorOrigin] - This is the point on the anchor where the popover's anchorEl will attach to.
 * This is not used when the anchorReference is 'anchorPosition'.
 * Options: vertical: [top, center, bottom]; horizontal: [left, center, right].
 * @property {custom} [targetOrigin] -
 */

// @CS.SafeComponent
class ContextMenuCustomView extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      showDropdown: false
    }

    this.triggerElement = React.createRef();
  }
  closeDropdown =()=> {
    window.dontRaise = true; //Do not remove
    if (CS.isFunction(this.props.showHidePopoverHandler)) {
      this.props.showHidePopoverHandler(false, this.props.context)
    } else {
      if(this.props.clearSearchOnPopoverClose && CS.isFunction(this.props.searchHandler)){
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

  handleContextMenuOnClick = () => {
    window.dontRaise = true; //Do not remove
  }

  render() {
    var sClassName = (this.state.showDropdown) ? "contextMenuViewNewContainer popoverVisible " : "contextMenuViewNewContainer ";
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
      backgroundColor: "transparent"
    } : null;


    let sWrapperClassName = !this.props.context ? "contextMenuItemViewWrapper" : "contextMenuItemViewWrapper " + this.props.context;
    return (
        <div className={sClassName}>
          <CustomPopoverView
              className={"popover-root"}
              open={CS.isFunction(this.props.showHidePopoverHandler) ? this.props.showPopover : this.state.showDropdown}
              anchorEl={this.state.anchorElement}
              anchorOrigin={oAnchorOrigin}
              transformOrigin={oTargetOrigin}
              style={ oPopoverStyle}
              onClose={this.closeDropdown}>
            <div className={this.props.showArrowHead == true ? "contextMenuPopoverArrowHeadWrapper" : ""} onClick={this.handleContextMenuOnClick}>
              {this.props.showArrowHead == true ? <div className="arrowSection"></div> : null}
              <div className={sWrapperClassName}>
                <ContextMenuItemView contextMenuViewModel={this.props.contextMenuViewModel}
                                     isMultiselect={this.props.isMultiselect}
                                     onClickHandler={this.onClickHandler}
                                     onApplyHandler={this.onApplyHandler}
                                     onCreateHandler={this.onCreateHandler}
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
                                     onItemsChecked={this.props.onItemsChecked}
                                     showSearch={this.props.showSearch}>
                </ContextMenuItemView>
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

ContextMenuCustomView.propTypes = oPropTypes;


export const view = ContextMenuCustomView;
export const events = oEvents;
export const propTypes = oPropTypes;
