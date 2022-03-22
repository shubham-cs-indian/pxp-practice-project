import React from 'react';
import ReactPropTypes from 'prop-types';
import CS from '../../libraries/cs';
import { view as CustomPopoverView } from '../customPopoverView/custom-popover-view';
import { view as GroupMssContextMenuView } from './group-mss-context-menu-view';
const Fragment = React.Fragment;

const oPropTypes = {
  disabled: ReactPropTypes.bool,
  closePopover: ReactPropTypes.func,
  activeOptions: ReactPropTypes.array,
  store: ReactPropTypes.object,
  handleOptionClicked: ReactPropTypes.func,
  groupsData: ReactPropTypes.object,
  styleCss: ReactPropTypes.object,
  isDirty: ReactPropTypes.bool
};

/**
 * @class GroupMssContextMenuPopupView
 * @memberOf Views
 * @property {bool} [disabled] - Used for disabling LazyContextMenuView.
 * @property {func} [closePopover] - Function to call when popup is closed.
 * @property {object} [activeOptions] - Options of groups which is already selected.
 * @property {object} [store] - Store instance.
 * @property {func} [handleOptionClicked] - Used to handle checkbox click functionality.
 * @property {object} [groupsData] - Group Json data to show on context menu.
 * @property {array} [styleCss] - Custom style to apply to popup.
 */

class GroupMssContextMenuPopupView extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      showPopover: props.showPopover || false,
      anchorEl: props.anchorEl || undefined
    };
    this.triggerElement = React.createRef();
    this.updatePosition = CS.noop;
  }

  componentDidUpdate (prevProps, prevState, snapshot) {
    /** Update popover position only if popover is opened **/
    let bIsPopOverOpen = this.props.showPopover ? this.props.showPopover : this.state.showPopover;
    bIsPopOverOpen && this.updatePosition();
  }

  updatePopoverPosition = (fUpdatePosition) => {
    if (fUpdatePosition) {
      this.updatePosition = fUpdatePosition;
    }
  };
  /**
   * Function to handle dropdown visibility
   * @param {object} oEvent Click event
   */
  showDropdown = (oEvent) => {
    this.setState({
      showPopover: true,
      anchorEl: oEvent.currentTarget
    })
  }

  /**
     * Function to handle closing of context menu
     */
    closePopover = () => {
      this.setState({
        showPopover: false
      })
      this.props.closePopover && this.props.closePopover();
    }

  render() {
    var fShowDropdownHandler = (this.props.disabled) ? null : this.showDropdown;
    let sWidth = this.props.anchorEl ? this.props.anchorEl.offsetWidth + 'px' : "300px";
    let oStyle = this.props.styleCss ? CS.cloneDeep(this.props.styleCss) : {}
    oStyle.maxWidth = oStyle.maxWidth ?  oStyle.maxWidth : sWidth;
    oStyle.width = oStyle.width ? oStyle.width : sWidth;
    oStyle.maxHeight = oStyle.maxHeight ? oStyle.maxHeight : "350px";
    return (
      <Fragment>
        <CustomPopoverView
          className="groupWrapper"
          open={this.state.showPopover}
          anchorEl={this.state.anchorEl}
          anchorOrigin={{ horizontal: 'left', vertical: 'bottom' }}
          transformOrigin={{ horizontal: 'left', vertical: 'top' }}
          onClose={this.closePopover}
          style={oStyle}
          updatePosition={this.updatePopoverPosition}
        >
          <GroupMssContextMenuView
            handleOptionClicked={this.props.handleOptionClicked}
            groupsData={this.props.groupsData}
            activeOptions={this.props.activeOptions}
            store={this.props.store}
            handleApplyButton={this.props.handleApplyButton}
            showApply={this.props.showApply}
            closePopover={this.closePopover}
            isMultiSelect={this.props.isMultiSelect}
            isDirty={this.props.isDirty}
          >
          </GroupMssContextMenuView>
        </CustomPopoverView>
        <div className="triggerElement" ref={this.triggerElement} onClick={fShowDropdownHandler}>
          {this.props.children}
        </div>
      </Fragment>
    )
  }
}

GroupMssContextMenuPopupView.propTypes = oPropTypes;

export const view = GroupMssContextMenuPopupView;
export const propTypes = oPropTypes;
