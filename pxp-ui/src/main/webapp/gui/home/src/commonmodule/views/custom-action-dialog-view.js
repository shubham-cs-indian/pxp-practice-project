import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import { view as CustomMaterialButtonView } from '../../viewlibraries/custommaterialbuttonview/custom-material-button-view';
import ExceptionLogger from '../../libraries/exceptionhandling/exception-logger';
import Icon from '@material-ui/core/Icon';
import { getTranslations as getTranslation } from '../store/helper/translation-manager';
import ErrorOutlineOutlinedIcon from '@material-ui/icons/ErrorOutlineOutlined';

const Events = {};

const oPropTypes = {
  header: ReactPropTypes.string,
  message: ReactPropTypes.string,
  buttonData: ReactPropTypes.array,
  isTriActionDialog: ReactPropTypes.bool,
  titleStyle: ReactPropTypes.object,
  deleteDOMCallback: ReactPropTypes.func,
  style: ReactPropTypes.object,
};


// @CS.SafeComponent
class CustomActionDialogView extends React.Component {

  constructor (props) {
    super(props);

    this.state = {
      isOpen : true
    };
  }

  handleCustomMaterialButtonClick = (fButtonContextHandler) => {
    setTimeout(this.props.deleteDOMCallback, 500);
    this.setState({
      isOpen: false
    });
    if (CS.isFunction(fButtonContextHandler)) {
      fButtonContextHandler();
    } else {
      ExceptionLogger.info("No handler Passed");
    }

  };

  getDialogButtons = () => {
    let aButtonData = this.props.buttonData;
    let aButtonView = [];
    let _this = this;

    let oButtonStyle = {
      height: '30px',
      minWidth: '75px',
      lineHeight: "28px",
      margin: "0 5px",
      padding: '0',
      minHeight: '30px',
      boxShadow: 'none'
    };

    CS.forEach(aButtonData, function (oButton) {
      oButtonStyle = !CS.isEmpty(oButton.style) ? oButton.style : oButtonStyle
      aButtonView.push(<CustomMaterialButtonView
          label={oButton.label}
          isRaisedButton={!oButton.isFlat}
          isDisabled={false}
          style={oButtonStyle}
          onButtonClick={_this.handleCustomMaterialButtonClick.bind(_this, oButton.handler)}/>);
    });

    return aButtonView;
  };

  getDialogTitleView = () => {
    if (CS.isNotEmpty(this.props.header)) {
      let oDialogTitleStyle = {
        fontSize: "18px",
        height: '50px',
        paddingTop: '14px',
        whiteSpace: 'nowrap',
        overflow: 'hidden',
        textOverflow: 'ellipsis',
      };

      return (
          <DialogTitle
              className="customDialogTitleView"
              style={oDialogTitleStyle}
              disableTypography={true}>
            {this.props.header}
          </DialogTitle>);
    }
    return null
  };

  getDialogContentView = () => {
    if (CS.isNotEmpty(this.props.message)) {
      let oMessageStyle = {
        "color": "rgba(0, 0, 0, 0.87)",
        "fontSize": "16px",
        "paddingBottom": "10px"
      };
      return (<DialogContent style={{padding: this.props.isManageConfig ?  "0px 24px" : 24, 'white-space': 'pre-line', 'scrollbarWidth': 'thin'}}>
        <div style={oMessageStyle}>{this.props.message}</div>
      </DialogContent>)
    }
    return null;
  };

  getManageConfigurationWarning() {
    let { bIsDelete } = this.props;
    return <div className="alert alert-warning" role="alert" style={{ margin: 15 }}>
      <div className="row" style={{
        display: "flex",
        padding: "0px 10px"
      }}>
        <div className="" style={{
          paddingTop: bIsDelete ? 4 : 0
        }}>
          <ErrorOutlineOutlinedIcon
   			 fontSize="inherit"
    		 style={{ fontSize: "25px" }}
  		/>
        </div>
        <div className="" style={{
          paddingLeft: 8,
          paddingTop: bIsDelete ? 0 : 5
        }}>
          {this.props.count == 1
            ? getTranslation().ManageConfigEntityDeleteWarning
            : getTranslation().ManageConfigEntityDeleteWarningPlural}
        </div>
      </div>
    </div>
  }

  render () {
    let aDialogButtons = this.getDialogButtons();

    let oActionContainerStyle = {
      "padding": "7px 8px",
      "height": "46px",
      "borderTop": "1px solid #ddd",
      "backgroundColor": "#eee",
      "margin": "0"
    };

    let oStyle = CS.isNotEmpty(this.props.style) ? this.props.style : {
      width: '75%',
      maxWidth: '768px',
      borderRadius: '3px',
    };

    return (
        <Dialog modal={'true'}
                open={this.state.isOpen}
                /*title={this.props.header}*/
                actions={aDialogButtons}
                /*actionsContainerStyle={oActionContainerStyle}*/
                className="actionDialog"
                titleStyle={this.props.titleStyle}
                /*bodyClassName="actionDialogBody"*/
                PaperProps={{style : oStyle}}>
          {this.getDialogTitleView()}
          {this.props.isManageConfig && this.getManageConfigurationWarning()}
          {this.getDialogContentView()}
          <DialogActions style={oActionContainerStyle}>{aDialogButtons}</DialogActions>
        </Dialog>
    );
  }
}

CustomActionDialogView.propTypes = oPropTypes;

export const view = CustomActionDialogView;
export const events = Events;
export const propTypes = oPropTypes;
