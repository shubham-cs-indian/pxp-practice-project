import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import IconButton from '@material-ui/core/IconButton';
import Radio from '@material-ui/core/Radio';
import { withStyles as withStyle } from '@material-ui/core/styles';

const oEvents = {};

const styles = {
  root: {
    '&$checked': {
      color: '#3248A7',
    },
  },
  checked: {},
};

const ColoredRadioButton = withStyle(styles)(Radio);


const oPropTypes = {
  radioGroupId: ReactPropTypes.string,
  disabled: ReactPropTypes.bool,
  dataList: ReactPropTypes.arrayOf(
      ReactPropTypes.shape
      ({
        id: ReactPropTypes.string,
        label: ReactPropTypes.string
      })),
  selectedId: ReactPropTypes.string,
  containerModel: ReactPropTypes.object,
  onApply: ReactPropTypes.func,
};

// @CS.SafeComponent
class RadioButtonGroupView extends React.Component{

  constructor(props) {
    super(props);
  }

  handleRadioButtonClicked =(sDataId)=> {
    var oContainerModel = this.props.containerModel;
    if (CS.isFunction(this.props.onApply)) {
      oContainerModel.properties['iNewValue'] = sDataId;
      this.props.onApply(oContainerModel);
    }
  }

  getRadioButtonView =(oData)=> {
    var __this = this;
    var __props = __this.props;
    var sSelectedId = __props.selectedId;
    var bIsDisabled = __props.disabled;
    var bIsSelected = sSelectedId == oData.id;

    var oView = null;
    if(bIsDisabled) {
      oView = <Radio key={oData.id}  checked = {bIsSelected} />;
    } else {
      oView = (
          <IconButton key={oData.id} onClick={__this.handleRadioButtonClicked.bind(__this, oData.id)} style={{padding: "1px 0px", background: "transparent"}}>
            <ColoredRadioButton
            checked = {bIsSelected} style={{padding: "1px 0px", background: "transparent"}}/>
          </IconButton>
      );
    }
    return (<div className="radioButton">{oView}</div>);
  }

  getRadioButtonGroup =()=> {
    var __this = this;
    var __props = __this.props;
    return CS.map(__props.dataList, __this.getRadioButtonView);
  }

  render() {
    return (
        <div className="radioButtonGroupViewContainer">
          {this.getRadioButtonGroup()}
        </div>
    );
  }
}

RadioButtonGroupView.propTypes = oPropTypes;

export const view = RadioButtonGroupView;
export const events = oEvents;
