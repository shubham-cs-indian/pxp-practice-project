
import React from 'react';
import CoreTooltipView from './core-tooltip-view';

const oPropTypes = CoreTooltipView.propTypes;

class TooltipView extends CoreTooltipView.view{
  constructor(props) {
    super(props);
  }
}

TooltipView.propTypes = oPropTypes;

export default TooltipView;
