import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import ClickTooltipView from './click-tooltip-view';
import ClickAwayListener from '@material-ui/core/ClickAwayListener';

/** -------------------------------------------------------------------------------------------------
*  ClickTooltipWrapperView is designed as a Wrapper to ClickTooltipView,
*
*  Intention:
*  1. To show tooltip window when a click is performed on a particular
*     DOM element.
*  2. On clicking again, hide the tooltip
*  3. Also clicking anywhere else,hide the tooltip
*
*
*  Props:
*
*  1. couplingLabel     |   Mandatory   | DOM element, which should be displayed on tooltip
*  2. clickHandler      |   Optional    | If some additional function if required to execute
* ----------------------------------------------------------------------------------------------- **/

const oPropTypes = {
  clickHandler: ReactPropTypes.func,
  couplingLabel: ReactPropTypes.string
};

// @CS.SafeComponent
class ClickTooltipWrapperView extends React.Component{

  constructor(props) {
    super(props);
    this.state = {
      openTooltip: false
    };
  };

  toggleTooltipState(){
    this.state.openTooltip = !this.state.openTooltip
  };

  handleClickAway = () => {
    this.setState({
      openTooltip: false
    })
  };

  clickHandler = () => {
    this.toggleTooltipState();
    if(CS.isFunction(this.props.onClickHandler)){
      this.props.onClickHandler();
    }
  };

  render(){
    return(
      <ClickAwayListener onClickAway={this.handleClickAway}>
        <ClickTooltipView label={this.props.couplingLabel} placement="left"
                          open={this.state.openTooltip}
        >
          <span onClick={this.clickHandler}>
            {this.props.children}
          </span>
        </ClickTooltipView>
      </ClickAwayListener>
    )
  };
}

ClickTooltipWrapperView.propTypes = oPropTypes;

export const view = ClickTooltipWrapperView;
