import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as CustomPopoverView } from '../../../../../viewlibraries/customPopoverView/custom-popover-view';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { view as XRaySettingsView } from './x-ray-settings-view';
import TooltipView from './../../../../../viewlibraries/tooltipview/tooltip-view';
import ContentScreenConstants from '../store/model/content-screen-constants';

const oEvents = {
  HANDLE_X_RAY_BUTTON_CLICKED: "handle_x_ray_button_clicked",
  HANDLE_X_RAY_SETTINGS_BUTTON_CLICKED: "handle_x_ray_settings_button_clicked"
};

const oPropTypes = {
  thumbnailMode: ReactPropTypes.string,
  xRaySettingsData: ReactPropTypes.object,
  viewMode: ReactPropTypes.string,
  filterContext: ReactPropTypes.object.isRequired,
};

// @CS.SafeComponent
class XRayButtonView extends React.Component {

  constructor(props) {
    super(props);
    this.settingsButton = React.createRef();
  }
  static propTypes = oPropTypes;

  static defaultProps = function() {}();

  state = {
    showSettingsPopover: false
  };

  closePopover = () => {
    this.setState({
      showSettingsPopover: false
    });
  };

  handleXRayButtonClicked = (sRelationshipId, sRelationshipIdToFetchData) => {
    EventBus.dispatch(oEvents.HANDLE_X_RAY_BUTTON_CLICKED,sRelationshipId, sRelationshipIdToFetchData, this.props.filterContext);
  };

  handleSettingsButtonClicked = () => {
    var oDOM = this.settingsButton.current;
    this.setState({
      showSettingsPopover: true,
      anchorElement: oDOM
    });
    EventBus.dispatch(oEvents.HANDLE_X_RAY_SETTINGS_BUTTON_CLICKED);
  };

  getXRaySettingsView = () => {
    let oXRaySettingsData = this.props.xRaySettingsData;
    return (
        <XRaySettingsView attributeMap={oXRaySettingsData.attributeMap}
                          tagMap={oXRaySettingsData.tagMap}
                          searchText={oXRaySettingsData.searchText}
                          propertyGroupList={oXRaySettingsData.propertyGroupList}
                          activeXRayPropertyGroup={oXRaySettingsData.activeXRayPropertyGroup}
                          viewMode={oXRaySettingsData.viewMode}
                          onClose={this.closePopover}
                          relationshipId={oXRaySettingsData.relationshipId}
                          relationshipIdToFetchData={oXRaySettingsData.relationshipIdForFetchingXRAYData}
                          filterContext={this.props.filterContext}
                          groupData={this.props.groupData}/>
    );
  };

  render() {

    var sThumbnailMode = this.props.thumbnailMode;
    var oSettingsButtonView = null;
    var sXRayButtonClass = "xRayButton ";
    var oPopoverStyle = {
      // boxShadow: "none",
      backgroundColor: "transparent",
      maxWidth: '250px',
      maxHeight: '500px'
    };
    var oXRaySettingsView = this.getXRaySettingsView();

    var sSettingsButtonClass = "settingsButton ";
    if (sThumbnailMode == ContentScreenConstants.thumbnailModes.XRAY) {
      //x-ray mode is active
      sXRayButtonClass += "isActive ";
    }
    var sSettingActiveClass = "";
    if (this.state.showSettingsPopover) {
      sSettingsButtonClass += "isActive ";
      sSettingActiveClass += "isActive ";
    }

    oSettingsButtonView = (
        <div className={sSettingsButtonClass} ref={this.settingsButton}>
          <div className="settingsIcon" onClick={this.handleSettingsButtonClicked}></div>
        </div>
    );
    var oXRaySettingsData = this.props.xRaySettingsData;
    let sXrayViewBtnLabel = " X-Ray View";

    let sRelationshipIdToFetchData = oXRaySettingsData.relationshipIdForFetchingXRAYData || "";
    return (
        <div className="xRaySection customButton">
          <TooltipView placement="top" label={sXrayViewBtnLabel}>
          <div className={"xRayButtonContainer "+sSettingActiveClass}>
              <div className={sXRayButtonClass} onClick={this.handleXRayButtonClicked.bind(this,oXRaySettingsData.relationshipId, sRelationshipIdToFetchData)}></div>
            {oSettingsButtonView}
          </div>
          </TooltipView>
          <CustomPopoverView
              className="popover-root"
              open={this.state.showSettingsPopover}
              anchorEl={this.state.anchorElement}
              anchorOrigin={{horizontal: 'right', vertical: 'bottom'}}
              transformOrigin={{horizontal: 'right', vertical: 'top'}}
              style={oPopoverStyle}
              onClose={this.closePopover}
          >
            {oXRaySettingsView}
          </CustomPopoverView>
        </div>
    );
  }
}

export const view = XRayButtonView;
export const events = oEvents;
