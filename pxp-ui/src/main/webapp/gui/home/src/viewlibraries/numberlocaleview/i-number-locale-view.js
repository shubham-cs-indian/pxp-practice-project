import React from 'react';
import INumberLocaleStore from './i-number-locale-store';
import {uniqueId} from './../../libraries/cs/cs-lodash';

/**
 * @class INumberLocaleView - Use for numberlocale view.
 */
class INumberLocaleView extends React.Component {

  constructor (props) {
    super(props);
    INumberLocaleStore.prototype.bind('i-number-changed', this.stateChanged)
  }

  componentWillUnmount() {
    INumberLocaleStore.prototype.unbind('i-number-changed', this.stateChanged)
  }

  stateChanged = () => {
    this.setState({uniqueId: uniqueId('number_')});
  }

}

export default INumberLocaleView;