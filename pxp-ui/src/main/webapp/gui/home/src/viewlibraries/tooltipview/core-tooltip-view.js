
import React from 'react';

import ReactPropTypes from 'prop-types';
import BaseTooltipView from './base-tooltip-view';

const oPropTypes = {
  placement: ReactPropTypes.string,
  label: ReactPropTypes.oneOfType([ReactPropTypes.string, ReactPropTypes.object, ReactPropTypes.array]),
  delay: ReactPropTypes.number
};
/**
 * @class CoreTooltipView - Use for display tooltips.
 * @memberOf Views
 * @property {string} [placement] - Pass place for where to display tooltip.
 * @property {custom} label - Pass label of tooltip.
 * @property {number} [delay] - Pass delay time for display tooltip.
 */

class CoreTooltipView extends React.Component{

  constructor(props) {
    super(props);
  }

  getTriggerEvent () {
    return {disableHoverListener: false, disableTouchListener: true};
  }

  render() {
    return (this.props.label) ? (<BaseTooltipView {...this.props} getTriggerEvent={this.getTriggerEvent}/>) : this.props.children;
  }
}

CoreTooltipView.propTypes = oPropTypes;

export default {
  view: CoreTooltipView,
  propTypes: oPropTypes
}
