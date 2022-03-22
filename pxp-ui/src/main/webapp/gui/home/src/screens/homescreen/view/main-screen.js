import React from 'react';
import ReactPropTypes from 'prop-types';

const oEvents = {};

// @CS.SafeComponent
class MainScreen extends React.Component {

  static propTypes = {
    notificationCountChanged: ReactPropTypes.bool
  };

  constructor (props) {
    super(props);
  }

  shouldComponentUpdate (oNextProps) {
    return !oNextProps.notificationCountChanged;
  }

  render () {
    return <React.Fragment>{this.props.children}</React.Fragment>
  };

}

export const view = MainScreen;
export const events = oEvents;
