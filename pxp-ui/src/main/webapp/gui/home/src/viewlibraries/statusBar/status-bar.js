import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';

const oEvents = {};

const oPropTypes = {
  completeness: ReactPropTypes.number
};

// @CS.SafeComponent
class StatusBar extends React.Component {

  constructor (props) {
    super(props);
  }


  render () {
    let oStatusBarIndicatorStyle = {width: this.props.completeness + '%'};
    return (
        <div className={"statusBarContainer"}>
          <div className={"statusBar"}></div>
          <div className={"statusBarIndicator"} style={oStatusBarIndicatorStyle}></div>
        </div>
    )
  }
}

StatusBar.propTypes = oPropTypes;

export const view = StatusBar;
export const events = oEvents;
