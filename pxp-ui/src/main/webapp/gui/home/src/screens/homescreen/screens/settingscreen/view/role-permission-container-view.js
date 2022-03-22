import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';

const oEvents = {
  ROLE_PERMISSION_VIEW_TOGGLE_BUTTON_CLICKED: "role_permission_view_toggle_button_clicked",
  ROLE_PERMISSION_VIEW_SAVE_ROLE_CLICKED: "role_permission_view_save_role_clicked",
};

const oPropTypes = {
  ruleConfigView: ReactPropTypes.object,
  isPermissionVisible: ReactPropTypes.bool,
  hideRuleConfigPermissionToggle: ReactPropTypes.bool
};

// @CS.SafeComponent
class RolePermissionContainerView extends React.Component {

  constructor(props) {
    super(props);

    this.handleRolePermissionToggleButtonClicked = this.handleRolePermissionToggleButtonClicked.bind(this);
    this.handleRolePermissionSaveButtonClicked = this.handleRolePermissionSaveButtonClicked.bind(this);
  }

  handleRolePermissionToggleButtonClicked () {
    EventBus.dispatch(oEvents.ROLE_PERMISSION_VIEW_TOGGLE_BUTTON_CLICKED);
  };

  handleRolePermissionSaveButtonClicked () {
    EventBus.dispatch(oEvents.ROLE_PERMISSION_VIEW_SAVE_ROLE_CLICKED);
  };


  render() {

    let sPermissionToggleLabel = this.props.isPermissionVisible ? "Show Role" : "Show Permissions";
    let oPermissionToggleDOM = this.props.hideRuleConfigPermissionToggle ? null : (<div className="permissionsToggle"
                                                                                        onClick={this.handleRolePermissionToggleButtonClicked}>{sPermissionToggleLabel}</div>)

    return (
        <div className="rolePermissionContainerView">

          <div className="roleOperationsSection">
            {oPermissionToggleDOM}
          </div>
          {this.props.ruleConfigView}
        </div>
    );
  }
}

RolePermissionContainerView.propTypes = oPropTypes;

export const view = RolePermissionContainerView;
export const events = oEvents;
