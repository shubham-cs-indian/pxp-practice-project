import React from 'react';
import IDateStore from './i-date-store'
import {uniqueId} from './../../libraries/cs/cs-lodash';

class IDatPickerView extends React.Component {

  constructor (props) {
    super(props);
    IDateStore.prototype.bind('i-date-changed', this.stateChanged)
  }

  componentWillUnmount() {
    IDateStore.prototype.unbind('i-date-changed', this.stateChanged)
  }

  stateChanged = () => {
    this.setState({uniqueId: uniqueId('date_')});
  }

}

export default IDatPickerView;