import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import TooltipView from '../../../../../viewlibraries/tooltipview/tooltip-view';
import {view as IndesignServerView} from '../view/indesign-server-view';
import {view as CommonConfigSectionView} from '../../../../../viewlibraries/commonconfigsectionview/common-config-section-view.js';
import {view as CustomMaterialButtonView} from "../../../../../viewlibraries/custommaterialbuttonview/custom-material-button-view";
import IndesignServerLayoutData from "../tack/settinglayouttack/config-indesign-server-layout-data";

const oEvents = {
  INDESIGN_SERVER_CONFIGURATION_ADD_BUTTON_clicked: "indesign_server_configuration_add_button_clicked",
  INDESIGN_SERVER_CONFIGURATION_HEADER_SAVE_BUTTON_CLICKED: "indesign_server_configuration_header_save_button_clicked",
  INDESIGN_SERVER_CONFIGURATION_HEADER_CANCEL_BUTTON_CLICKED: "indesign_server_configuration_header_cancel_button_clicked",
};

let oPropTypes = {
  showSaveSaveDiscardButton: ReactPropTypes.bool,
  loadBalancerConfiguration: ReactPropTypes.object,
  indesignServerList: ReactPropTypes.array,
  sequenceNumber: ReactPropTypes.number
};

/**
 * @class IndesignServerConfigurationView - This view is used to render InDesign Server configuration which includes load balancer server's
 *                             "HOST NAME", "PORT" and "STATUS" along with buttons to save and delete configuration.
 * @memberOf Views
 * @property {object} [serverConfig]   - serverConfig is a configuration object will consist "HOST NAME", "PORT",
 *                                       "STATUS" and "ID" of configured InDesign server instance.
 * @property {number} [sequenceNumber] - Number specifying index for server.
 */

// @CS.SafeComponent
class IndesignServerConfigurationView extends React.Component {

  constructor (props) {
    super(props);
    this.state = {
      showSaveButton: false
    };
  }

  static propTypes = oPropTypes;

  handleHeaderSaveButtonClicked = () => {
    EventBus.dispatch(oEvents.INDESIGN_SERVER_CONFIGURATION_HEADER_SAVE_BUTTON_CLICKED, null, "");
  };

  handleHeaderCancelButtonClicked = () => {
    EventBus.dispatch(oEvents.INDESIGN_SERVER_CONFIGURATION_HEADER_CANCEL_BUTTON_CLICKED);
  };

  handleAddConfigurationButtonClicked = () => {
    EventBus.dispatch(oEvents.INDESIGN_SERVER_CONFIGURATION_ADD_BUTTON_clicked);
  };

  render () {
    let sPort = !CS.isEmpty(this.props.loadBalancerConfiguration) ? this.props.loadBalancerConfiguration.port : "";
    let sHostName = !CS.isEmpty(this.props.loadBalancerConfiguration) ? this.props.loadBalancerConfiguration.hostName : "";
    let aIndesignServerList = this.props.indesignServerList;
    aIndesignServerList = aIndesignServerList.map((oServerConfig, iIndex) =>
        <IndesignServerView key={oServerConfig.id} serverConfig={oServerConfig}
                            loadBalancerConfiguration={this.props.loadBalancerConfiguration}/>);
    let aSectionLayout = IndesignServerLayoutData.loadBalancerConfiguration();
    let oLoadBalancerData = {"hostName": sHostName, "serverPort": sPort};

    return (<div className="indesignConfigurationContainer">
      <div className="indesignServerConfigurationSection">
        <fieldset>
          <div className="indesignServerConfigurationHeaderContainer">
            <div className="indesignServerConfigurationHeader">InDesign Server Configuration</div>
            <div className="cancelButton" key="discard">
              <CustomMaterialButtonView
                  label={getTranslation().CANCEL}
                  isRaisedButton={false}
                  isDisabled={false}
                  onButtonClick={this.handleHeaderCancelButtonClicked}
              />
            </div>
            <div className="saveButton save" key="save">
              <CustomMaterialButtonView
                  label={getTranslation().SAVE}
                  isRaisedButton={true}
                  isDisabled={false}
                  onButtonClick={this.handleHeaderSaveButtonClicked}/>
            </div>
          </div>
          <div className="indesignLoadBalancerConfigurationsContainer">
            <fieldset>
              <legend> Load Balancer Configuration</legend>
              <div className="loadBalancerConfiguration">
                <CommonConfigSectionView context="loadBalancerServerConfiguration" sectionLayout={aSectionLayout}
                                         data={oLoadBalancerData}/>
                <div className="addIndsignServerButtonConfigContainer">
                  <legend> InDesign Server
                    <TooltipView placement="top" key="add_server" label={getTranslation().ADD_INDESIGN_SERVER}>
                    <div className="addIndesignServerButton"
                         onClick={this.handleAddConfigurationButtonClicked}>
                      +
                    </div>
                  </TooltipView>
                  </legend>

                </div>
              </div>
              <div className="indesignServerConfigurationsContainer">
                  <div className="indesignServerConfigurationWrapper">
                    <div className="indesignServerConfiguration">
                    {aIndesignServerList}
                    </div>
                  </div>
              </div>
            </fieldset>
          </div>
        </fieldset>
      </div>
    </div>)
  }
};

IndesignServerConfigurationView.propTypes = oPropTypes;
export const view = IndesignServerConfigurationView;
export const events = oEvents;
