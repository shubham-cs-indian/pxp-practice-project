import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { Button } from '@material-ui/core';
import TooltipView from '../../../../../viewlibraries/tooltipview/tooltip-view';
import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';
import {view as CommonConfigSectionView} from "../../../../../viewlibraries/commonconfigsectionview/common-config-section-view";
import IndesignServerLayoutData from "../tack/settinglayouttack/config-indesign-server-layout-data";

const oEvents = {
  INDESIGN_SERVER_CONFIGURATION_REMOVE_BUTTON_CLICKED: "indesign_server_configuration_remove_button_clicked",
  INDESIGN_SERVER_CONFIGURATION_CHECK_STATUS_BUTTON_CLICKED: "indesign_server_configuration_check_status_button_clicked",
};

let oPropTypes = {
  serverConfig: ReactPropTypes.object,
};

/**
 * @class IndesignServerView - This view is used to render InDesign Server configuration which includes server's
 *                             "HOST NAME", "PORT" and "STATUS" along with buttons to save and delete configuration.
 * @memberOf Views
 * @property {object} [serverConfig]   - serverConfig is a configuration object will consist "HOST NAME", "PORT",
 *                                       "STATUS" and "ID" of configured InDesign server instance.
 */

// @CS.SafeComponent
class IndesignServerView extends React.Component {
  constructor (props) {
    super(props);
    this.state = { port: this.props.serverConfig.port};
  }

  static propTypes = oPropTypes;

  handleRemoveServerConfigurationButtonClicked = (sServerId) => {
    EventBus.dispatch(oEvents.INDESIGN_SERVER_CONFIGURATION_REMOVE_BUTTON_CLICKED, sServerId);
  };

  handleCheckStatusButtonClicked = (oServerDetails) => {
    EventBus.dispatch(oEvents.INDESIGN_SERVER_CONFIGURATION_CHECK_STATUS_BUTTON_CLICKED, oServerDetails);
  };

  render () {
    let sServerId = this.props.serverConfig.id;
    let sServerStatus = this.props.serverConfig.status == "inds_active" ? "Active" : "Disabled";
    let sPort = this.props.serverConfig.isModified ? this.props.serverConfig.updatedPort : this.props.serverConfig.port;
    let oServerDetails = {id: sServerId, hostName: this.props.serverConfig.hostName, port: sPort};
    let aSectionLayout = IndesignServerLayoutData.indesignServerConfiguration(sServerId);
    let oPortStausView = (<div className="configurationEntry">
      <TooltipView placement="top" key="check_server_status" label={oTranslations().CHECK_INDESIGN_SERVER_STATUS}>
        <Button size="small" variant="outlined"
                className="checkStatusButton"
                onClick={this.handleCheckStatusButtonClicked.bind(this, oServerDetails)}>
          {oTranslations().CHECK_STATUS}
        </Button>
      </TooltipView>
      <label> STATUS: </label> <span> {sServerStatus} </span>
    </div>);
    let loadBalancerData = {[sServerId]: sPort, "portStatus": oPortStausView};

    return (
        <div>
          <TooltipView placement="top" key="delete_server" label={oTranslations().DELETE_CONFIGURATION}>
            <div className={"removeConfigurationButton"}
                 onClick={this.handleRemoveServerConfigurationButtonClicked.bind(this, sServerId)}/>
          </TooltipView>
          <CommonConfigSectionView context="indesignServerConfiguration" sectionLayout={aSectionLayout}
                                   data={loadBalancerData}/>
        </div>
    )
  }
}



export const view = IndesignServerView;
export const events = oEvents;
