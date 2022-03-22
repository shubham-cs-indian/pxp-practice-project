import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import Button from '@material-ui/core/Button';

const oEvents = {};

const oPropTypes = {
  label: ReactPropTypes.string,
  isRaisedButton: ReactPropTypes.bool,
  isContainedButton: ReactPropTypes.bool,
  isDisabled: ReactPropTypes.bool,
  onButtonClick: ReactPropTypes.func,
  style: ReactPropTypes.object,
  labelStyle: ReactPropTypes.object,
  children: ReactPropTypes.node,
  bUncoloredRaisedButton: ReactPropTypes.bool,
  classes: ReactPropTypes.object,
  color: ReactPropTypes.string,
  className: ReactPropTypes.string,
  variant: ReactPropTypes.string
};
/**
 * @class CustomMaterialButtonView - use for display button view.
 * @memberOf Views
 * @property {string} [label] -  label of buttons.
 * @property {bool} [isRaisedButton] -  boolean value for isRaisedButton or not.
 * @property {bool} [isDisabled] -  boolean value for isDisabled or not.
 * @property {func} [onButtonClick] - pass function button on click.
 * @property {object} [style] - pass CSS for styling the buttons.
 * @property {object} [labelStyle] - pass CSS for label style.
 * @property {undefined} children - pass chilndren of button view.
 * @property {bool} [bUncoloredRaisedButton] -  boolean value for bUncoloredRaisedButton or not.
 */

// @CS.SafeComponent
class CustomMaterialButtonView extends React.Component {

  constructor (props) {
    super(props);
  }

  getButton () {
    let oApplyButtonStyles = !CS.isEmpty(this.props.style) ? CS.clone(this.props.style) : {
      height: "28px",
      lineHeight: "28px",
      margin: "0 5px",
      padding: '0 10px',
      minWidth: '64px',
      minHeight: '28px',
      boxShadow: 'none',
      outline: 'none'
    };

    let sClassName = (this.props.className || "") + " customMaterialButton ";
    let fOnClickHandler = this.props.onButtonClick;
    let sLabel = this.props.label || this.props.children;
    let sVariant = this.props.variant || 'flat';
    if (this.props.isRaisedButton) {
      // oApplyButtonStyles.backgroundColor = '#3248A7';
      oApplyButtonStyles.color = oApplyButtonStyles.color ? oApplyButtonStyles.color : '#FFF';
      sVariant = 'contained';
      sClassName += 'raisedButton';
    }

    if (this.props.isContainedButton) {
      oApplyButtonStyles.color = '#FFF';
      sVariant = 'contained';
      sClassName += 'raisedButton';
    }
    return (<Button
        className={sClassName}
        onClick={fOnClickHandler}
        style={oApplyButtonStyles}
        variant={sVariant}
        children={this.props.children}
        disabled={this.props.isDisabled}
        classes={this.props.classes}
        color={this.props.color}
    >
      {sLabel}
    </Button>);
  };

  render () {
    return (
        this.getButton()
    );
  }
}


CustomMaterialButtonView.propTypes = oPropTypes;

export const view = CustomMaterialButtonView;
export const events = oEvents;
