import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import ExceptionLogger from '../../libraries/exceptionhandling/exception-logger';

const oPropTypes = {
  elementKey: ReactPropTypes.string,
  poster: ReactPropTypes.string,
  videoSrc: ReactPropTypes.string,
};
/**
 * @class VideoView - use for Display Video view.
 * @memberOf Views
 * @property {string} [elementKey] -  element key.
 * @property {string} [poster] poster of video.
 * @property {string} [videoSrc] -  a path of video.
 */

// @CS.SafeComponent
class VideoView extends React.Component {

  constructor (props) {
    super(props);

    this.state = {showError: false};
  }

  onVideoError = (oEvent) => {
    ExceptionLogger.error('Error in video source');
    ExceptionLogger.log(oEvent);
    this.setState({showError: true});
  };

  render () {
    let _this = this;
    let _props = _this.props;
    let _state = _this.state;

    let oVideoView = (
        <video className="" autoPlay loop controls key={_props.elementKey} poster={_props.poster} preload="auto">
          <source src={_props.videoSrc} onError={_this.onVideoError}/>
        </video>
    );

    let oErrorView = (
        <div className="videoErrorMessage">{getTranslation().VIDEO_ERROR}</div>
    );

    return _state.showError ? oErrorView : oVideoView;
  }
}

VideoView.propTypes = oPropTypes;

export const view = VideoView;
export const propTypes = oPropTypes;
