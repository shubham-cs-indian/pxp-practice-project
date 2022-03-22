import React from 'react';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';

const oEvents = {};
const oPropTypes = {
  message: ReactPropTypes.string
};
/**
 * @class NothingFoundView - use for display nothing found view.
 * @memberOf Views
 * @property {string} [message] -  string for message display for nothing found view.
 */

// @CS.SafeComponent
class NothingFoundView extends React.Component{

  constructor(props) {
    super(props);
  }

  getView =()=> {

    var sMessage = this.props.message || getTranslation().NOTHING_FOUND;

    return (
        <div className="nothingFoundInnerContainer">
          <div className="nothingFoundIcon"></div>
          <div className="nothingFoundMsg">{sMessage}</div>
        </div>
    );
  }

  render() {
    let oStyle = this.props.style || null;
    return (
        <div className="nothingFoundViewContainer" style={oStyle}>{this.getView()}</div>
    );
  }
}

NothingFoundView.propTypes = oPropTypes;

export const view = NothingFoundView;
export const events = oEvents;
