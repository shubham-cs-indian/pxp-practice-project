import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';

const oEvents = {
};

const oPropTypes = {
  value: ReactPropTypes.bool,
  onChange: ReactPropTypes.func,
  isDisabled: ReactPropTypes.bool
};
/**
 * @class GridYesNoPropertyView - use to display toggle button in gridView.
 * @memberOf Views
 * @property {bool} [value] -  boolean for grid have value or not.
 * @property {func} [onChange] -  function which is used for onChange onChange event
 * @property {bool} [isDisabled] -  boolean value for isDisabled or not.
 */

// @CS.SafeComponent
class GridYesNoPropertyView extends React.Component {

  constructor(props) {
    super(props);
  }

  shouldComponentUpdate(oNextProps, oNextState) {
    return !CS.isEqual(oNextProps, this.props) || !CS.isEqual(oNextState, this.state);
  }

  handleValueChanged =(oEvent)=> {
    this.props.onChange(!this.props.value, oEvent);
    oEvent.nativeEvent.dontRaise = true;
  }

  render() {
    // var sStringToDisplay;
    var sButtonClass = "gridYesNoSwitchButton ";

    if (this.props.value) {
      sButtonClass += "yes";
      // sStringToDisplay = "yes";
    } else {
      sButtonClass += "no";
      // sStringToDisplay = "no";
    }

    let fHandleValueChanged = null;
    if (this.props.isDisabled) {
      sButtonClass += " disabled";
    } else {
      fHandleValueChanged = this.handleValueChanged;
    }

    return (
        <div className="gridYesNoPropertyView">
          <div className="gridYesNoSwitch" onClick={fHandleValueChanged}>
            <div className={sButtonClass}>
              <div className="textSection yes">I</div>
              <div className="circleSection"></div>
              <div className="textSection no ">O</div>
            </div>
          </div>
          {/*<div className="stringToDisplay">{sStringToDisplay}</div>*/}
        </div>
    );
  }

}

GridYesNoPropertyView.propTypes = oPropTypes;
GridYesNoPropertyView.defaultProps = {
  isDisabled: false
};

export const view = GridYesNoPropertyView;
export const events = oEvents;
