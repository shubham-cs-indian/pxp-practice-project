import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import Radio from '@material-ui/core/Radio';
import { withStyles as withStyle } from '@material-ui/core/styles';

const styles = {
  root: {
    '&$checked': {
      color: '#3248A7',
    },
  },
  checked: {},
};

const CustomRadioButton = withStyle(styles)(Radio);

const oPropTypes = {
  context: ReactPropTypes.string,
  selected: ReactPropTypes.bool,
  clickHanlder: ReactPropTypes.func,
};

// @CS.SafeComponent
class RadioButtonView extends React.Component {

  constructor (props) {
    super(props);
  }

  render () {
    let sContext = this.props.context || "";
    let bIsSelected = this.props.selected;
    let fOnCheckHandler = this.props.clickHanlder;
    return (
        <div className={"radioButtonViewContainer " + sContext}>
          <CustomRadioButton checked={bIsSelected} onClick={fOnCheckHandler}/>
        </div>
    );
  }
}

RadioButtonView.propTypes = oPropTypes;

export const view = RadioButtonView;
