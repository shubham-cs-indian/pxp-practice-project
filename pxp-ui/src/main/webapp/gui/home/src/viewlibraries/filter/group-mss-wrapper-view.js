import React from 'react';
import ReactPropTypes from 'prop-types';
import CS from '../../libraries/cs';
import GroupMssContextMenuStore from './store/groups-mss-context-menu-store';
import { view as GroupMssChipsView } from './group-mss-chips-view';
import { view as GroupMssContextMenuView } from './group-mss-context-menu-view';
import { view as GroupMssContextMenuPopupView } from './group-mss-context-menu-popup-view';

const [TRIGGEREVENT] = ["group-mss-view-data-changed"];


const oPropTypes = {
  activeOptions: ReactPropTypes.array,
  handleOptionClicked: ReactPropTypes.func,
  hideChips: ReactPropTypes.bool,
  showApply: ReactPropTypes.bool,
  removeOption: ReactPropTypes.func,
  disabled: ReactPropTypes.bool,
  isMultiSelect: ReactPropTypes.bool,
  groupsData: ReactPropTypes.arrayOf(
    ReactPropTypes.shape({
      id: ReactPropTypes.string,
      icon: ReactPropTypes.string,
      label: ReactPropTypes.string,
      list: ReactPropTypes.array,
      requestInfo: ReactPropTypes.shape({
        requestType: ReactPropTypes.string,
        entities: ReactPropTypes.oneOf([ReactPropTypes.array, ReactPropTypes.string]),
        responsePath: ReactPropTypes.array,
        requestURL: ReactPropTypes.string
      })
    })
  ),
  referencedData: ReactPropTypes.object,
  hideSelectedCount: ReactPropTypes.bool,
};

/**
 * @class GroupMssWrapper
 * @memberOf Views
 * @property {array} [activeOptions] - PreSelected Options of groups.
 * @property {func} [handleOptionClicked] - Used to handle checkbox click functionality.
 * @property {array} [groupsData] - Request data for getting items from backend.
 * @property {bool} [hideChips] - Boolean value to show/hide chps
 * @property {func} [removeOption] - Function to call at time of remove chips
 * @property {bool} [disabled] - Boolean value to disable editing the options
 * @property {bool} [isMultiSelect] - Boolean value to multi/single select options
 */

// @CS.SafeComponent
class GroupMssWrapper extends React.Component {

  constructor(props) {
    super(props);
    GroupMssContextMenuStore.prototype.bind(TRIGGEREVENT, this.stateChanged);
    this.state = {
      groupsData: undefined,
      store: {},
      activeOptions: [],
      showPopover: false
    };
    this.initializeStore();
  }

  componentWillUnmount() {
    GroupMssContextMenuStore.prototype.unbind(TRIGGEREVENT, this.stateChanged);
  }

  /**
   * Function to handle if selectedOption should be read from state or props
   */
  static getDerivedStateFromProps(oNextProps, oState) {
    if (!oNextProps.isDirty) {
      let aActiveChips = oNextProps.activeOptions || [];
      CS.each(aActiveChips, oOpt => {
        let sGroupType = oOpt.groupType || oOpt.type;
        oOpt.groupType = sGroupType;
      })
      return ({ activeOptions: aActiveChips});
    }
    return ({ activeOptions: oState.activeOptions});
  }

  /**
   * Function to update state groupData
   */
  stateChanged = () => {
    let aGroupsData = this.state.store.getGroupsData();
    if (!CS.isEmpty(aGroupsData)) {
      this.setState({
        groupsData: aGroupsData
      });
    }
  }

  /**
   * Function to initialize store and assign to state
   */
  initializeStore = () => {
    var oProps = this.props;
    this.state.store = new GroupMssContextMenuStore(oProps.groupsData);
    let aGroupsData = {}
    CS.forEach(oProps.groupsData, oGroup => {
      aGroupsData[oGroup.id] = {
        id: oGroup.id,
        icon: oGroup.icon,
        label: oGroup.label,
        showLoadMore: oGroup.showLoadMore,
        list: oGroup.list ? oGroup.list : [],
        customIconClassName: oGroup.customIconClassName
      };
    })
    // aGroupsData = Object.assign(aGroupsData, oProps.groupsData);
    this.state.store.setGroupsData(aGroupsData);
    this.state.groupsData = aGroupsData;
  }

  /**
   * Function to remove active option
   * @param {object} oClickedOption Selected Option Json Object
   */
  removeActiveChips = (oClickedOption) => {
    let aActiveOptions = CS.remove(this.state.activeOptions, opt => {
      return opt.id !== oClickedOption.id
    });
    this.setState({
      activeOptions: aActiveOptions,
    });
    if (typeof this.props.removeOption == 'function') this.props.removeOption(oClickedOption);
  }

  /**
   * Function to handle isDirty check if any option is selected
   * @param {object} oOption Selected option
   */
  handleOptionClicked = (oOption) => {
    this.setState({})
    if (typeof this.props.handleOptionClicked == 'function') this.props.handleOptionClicked(oOption);
  }

  handleApplyButton = (aActiveOptions) => {
    this.props.handleApplyButton(aActiveOptions)
  }

  /**
   * Function generate context menu dom based on props as popup or plain
   */
  getContextMenuView = () => {
    if (this.props.showPopup) {
      let oPopupStyle = this.props.styles || { width: '250px', overFlow: "hidden" };
      return (<GroupMssContextMenuPopupView
        styleCss={oPopupStyle}
        groupsData={this.state.groupsData}
        handleOptionClicked={this.handleOptionClicked}
        store={this.state.store}
        activeOptions={this.state.activeOptions}
        handleApplyButton={this.handleApplyButton}
        showApply={this.props.showApply}
        isMultiSelect={this.props.isMultiSelect}
        isDirty={this.props.isDirty}
      />)
    } else {
      return (<div className="groupList"><GroupMssContextMenuView
        handleOptionClicked={this.handleOptionClicked}
        groupsData={this.state.groupsData}
        store={this.state.store}
        activeOptions={this.state.activeOptions}
        handleApplyButton={this.handleApplyButton}
        showApply={this.props.showApply}
        isMultiSelect={this.props.isMultiSelect}
        hideSelectedCount={this.props.hideSelectedCount}
        isDirty={this.props.isDirty}
      /></div>)
    }
  }

  render() {
    let oContextView = this.props.disabled ? null : this.getContextMenuView();
    return (
      <div className="groupWrapper">
        <GroupMssChipsView
          selectedOptions={this.state.activeOptions}
          store={this.state.store}
          handleRemove={this.removeActiveChips}
          hideChips={this.props.hideChips}
          disabled={this.props.disabled}
          referencedData={this.props.referencedData}
        />
        {oContextView}
      </div>
    )
  }
}

GroupMssWrapper.propTypes = oPropTypes;

export const view = GroupMssWrapper;
export const propTypes = oPropTypes;
