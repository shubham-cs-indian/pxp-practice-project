
import React from 'react';
import CoreTooltipView from './core-tooltip-view';

const oPropTypes = CoreTooltipView.propTypes;

class ClickTooltipView extends CoreTooltipView.view {

  constructor(props) {
    super(props);
  }

  getTriggerEvent () {
    return {disableHoverListener: true, disableTouchListener: false};
  }

}

ClickTooltipView.propTypes = oPropTypes;

export default ClickTooltipView;
