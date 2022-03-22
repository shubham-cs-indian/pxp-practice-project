import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import Popover from '@material-ui/core/Popover';


const oEvents = {};

const oPropTypes = {
  className: ReactPropTypes.string,
  open: ReactPropTypes.bool,
  anchorEl: ReactPropTypes.object,
  anchorOrigin: ReactPropTypes.object,
  transformOrigin: ReactPropTypes.object,
  style: ReactPropTypes.object,
  onClose: ReactPropTypes.func,
  updatePosition: ReactPropTypes.func,
  animated: ReactPropTypes.bool,      //unused
  useLayerForClickAway: ReactPropTypes.bool //unused
};


// @CS.SafeComponent
class CustomPopoverView extends React.Component {


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
        <Popover
            selected={this.props.selected}
            classes={{}}
            action={this.action}
            className= {this.props.className}
            open={this.props.open}
            anchorEl={this.props.anchorEl}
            anchorOrigin={this.props.anchorOrigin}
            transformOrigin={this.props.transformOrigin}
            PaperProps={{style: oStyle}}
            onClose={this.handlePopoverClose}
        >
          {this.props.children}
        </Popover>
    );
  }
}


CustomPopoverView.propTypes = oPropTypes;

export const view = CustomPopoverView;
export const events = oEvents;
