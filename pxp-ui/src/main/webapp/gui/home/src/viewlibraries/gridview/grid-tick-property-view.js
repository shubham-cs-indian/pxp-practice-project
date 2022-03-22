import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
const oEvents = {};

const oPropTypes = {
  value: ReactPropTypes.bool,
  onChange: ReactPropTypes.func,
  isDisabled: ReactPropTypes.bool
};

// @CS.SafeComponent
class GridYesNoPropertyView extends React.Component {

  constructor (props) {
    super(props);
  }

  render () {
    let sButtonClass = "isSelected ";
    if (this.props.isDisabled) {
      sButtonClass += " disabled";
    }
    return (
        <div className="gridTickPropertyView">
          {this.props.value && <div className={sButtonClass}/> || <div/>}
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
