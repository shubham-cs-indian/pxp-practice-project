import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import FullScreenViewObj from '../../../../../viewlibraries/fullscreenview/fullscreen-view';
const FullScreenView = FullScreenViewObj.view;

const oEvents = {
  PROCESS_CONFIG_FULLSCREEN_BUTTON_CLICKED : "process_config_fullscreen_button_clicked"
};

const oPropTypes = {
  bodyView: ReactPropTypes.object.isRequired,
  header: ReactPropTypes.string.isRequired,
  showHeader: ReactPropTypes.bool,
  isDefaultFullScreen: ReactPropTypes.bool,
  isFullScreenMode: ReactPropTypes.bool
};

// @CS.SafeComponent
class ProcessConfigFullScreenWrapperView extends React.Component {
  constructor (props) {
    super(props);
  }

  handleFullScreenEnabled = () => {
    EventBus.dispatch(oEvents.PROCESS_CONFIG_FULLSCREEN_BUTTON_CLICKED,)
  };

  render () {

    return(
        <FullScreenView
            header={this.props.header}
            showHeader={false}
            bodyView={this.props.bodyView}
            isFullScreenMode={this.props.isFullScreenMode}
            fullScreenHandler={this.handleFullScreenEnabled}
        />
    );
  }
}

ProcessConfigFullScreenWrapperView.propTypes = oPropTypes;

export const view = ProcessConfigFullScreenWrapperView;
export const events = oEvents;
