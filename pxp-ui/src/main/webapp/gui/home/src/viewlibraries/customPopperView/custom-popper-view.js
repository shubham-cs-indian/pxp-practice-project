import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import Popper from '@material-ui/core/Popper';

const oEvents = {};

const oPropTypes = {
  className: ReactPropTypes.string,
  open: ReactPropTypes.bool,
  style: ReactPropTypes.object,
  onClose: ReactPropTypes.func,
  placement: ReactPropTypes.object,
  disablePortal: ReactPropTypes.bool
};


// @CS.SafeComponent
class CustomPopperView extends React.Component {


  constructor (props) {
    super(props);
  }


  /**
   * Popover accepts a function action as props in which it returns updatePosition function in order to
   * update the Popover Position.
   *
   * Internally that accesses DOM's dimensions to recalculate height & width so accordingly it
   * repositions Popover starting point if needed
   *
   * @param obj - It contains updatePosition function which can be called to reposition Popover position whenever needed
   *
   */
  action = (obj) => {
    if(obj && obj.updatePosition && this.props.updatePosition) {
      this.props.updatePosition(obj.updatePosition);
    }
  };

  handlePopoverClose = (oEvent) => {
    if(oEvent.nativeEvent === undefined) {
      oEvent.nativeEvent = {};
      oEvent.dontRaise = true;
    }
    oEvent.nativeEvent.dontRaise = true;
    this.props.onClose(oEvent);
  };

  render () {

    let oStyle = this.props.style || {};
    oStyle.maxWidth = oStyle.maxWidth ? oStyle.maxWidth : '90%';
    oStyle.maxHeight = oStyle.maxHeight ? oStyle.maxHeight : '350px';


    return (
        <Popper
            className={this.props.className}
            style={this.props.style}
            placement={this.props.placement}
            open={this.props.open}
            onClose={this.handlePopoverClose}
            disablePortal={this.props.disablePortal}
        >
          {this.props.children}
        </Popper>
    );
  }
}


CustomPopperView.propTypes = oPropTypes;

export const view = CustomPopperView;
export const events = oEvents;
