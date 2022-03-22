import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import { view as CustomMaterialButtonView } from '../custommaterialbuttonview/custom-material-button-view';
import withStyles from '@material-ui/core/styles/withStyles';

const oEvents = {
};
/*To apply custom style instead of default material-ui style on DialogContent*/
const styles = {
  permissionDetail: {
    padding: '0 !important'
  }
};

const oPropTypes = {
  onRequestClose: ReactPropTypes.func,
  title: ReactPropTypes.string,
  children: ReactPropTypes.object,
  buttonData: ReactPropTypes.array,
  buttonClickHandler: ReactPropTypes.func,
  bodyStyle: ReactPropTypes.object,
  contentStyle: ReactPropTypes.object,
  open: ReactPropTypes.bool,
  modal: ReactPropTypes.bool,
  className: ReactPropTypes.string,
  contentClassName: ReactPropTypes.string,
  bodyClassName: ReactPropTypes.string,
  checkboxClickedHandler: ReactPropTypes.func
};
/**
 * @class CustomDialogView - use for custom textfield in Application.
 * @memberOf Views
 * @property {func} [onRequestClose] -  function which used on close button click.
 * @property {string} [title] string of title.
 * @property {object} [children] -  object which contain aaray of children.
 * @property {array} [buttonData] - prop am array of buttonData.
 * @property {func} [buttonClickHandler] -  a function of button click handler event.
 * @property {object} [bodyStyle] -  object of css for bodyStyle.
 * @property {object} [contentStyle] -   object of css for contentStyle.
 * @property {bool} [open]  -  boolean which is used for popup open or not.
 * @property {bool} [modal]  -  boolean which is used for popup modal open or not.
 * @property {string} [className] - string of className.
 * @property {string} [contentClassName] - string for contentClassName.
 * @property {string} [bodyClassName] - string for bodyClassName.
 */

// @CS.SafeComponent
class CustomDialogView extends React.Component{
  constructor(props) {
    super(props);
  }

  handleButtonClick =(sButtonId, oEvent)=> {
    if(CS.isFunction(this.props.buttonClickHandler)){
      this.props.buttonClickHandler(sButtonId, oEvent);
    }
  }

  handleRequestOnClose =(oEvent)=> {
    /**If button data is present then close dialog on esc click and if button data is not present then close dialog
     *  on anywhere click
     */
    if (CS.isFunction(this.props.onRequestClose) && (oEvent.keyCode === 27 || CS.isEmpty(this.props.buttonData))) {
      oEvent.dontRaise = true;
      this.props.onRequestClose(oEvent);
    }
  }

  getCheckboxButtonView = (oButtonData) => {
    return (
        <React.fragment>
          <div className="checkboxButtonDOM" onClick={this.handleButtonClick.bind(this, oButtonData.id)}></div>
          <div>{oButtonData.label}</div>
        </React.fragment>
    );
  };


  getDialogButtons =()=> {
    var aButtonData = this.props.buttonData;
    var aButtonView = [];
    var _this = this;

    let oButtonStyle = {
      height: '30px',
      minWidth: '75px',
      lineHeight: "28px",
      margin: "0 5px",
      padding: '0',
      minHeight: '30px',
      boxShadow: 'none'
    };

    CS.forEach(aButtonData,function (oButton) {
      let bIsDisabled = !oButton.isNotActive ? false : true;
      let bIsCheckbox = oButton.isCheckbox;

      if(bIsCheckbox){
        aButtonView.push(_this.getCheckboxButtonView(oButton));
      }
      aButtonView.push(<CustomMaterialButtonView
          key={oButton.id}
          label={oButton.label}
          isRaisedButton={!oButton.isFlat}
          isDisabled={bIsDisabled}
          style={oButtonStyle}
          className={oButton.className}
          onButtonClick={_this.handleButtonClick.bind(_this, oButton.id)}/>);
    });

    var oActionContainerStyle = {
      "padding": "7px 8px",
      "height": "46px",
      "borderTop": "1px solid #ddd",
      "backgroundColor": "#eee",
      "margin": "0"
    };

    return !CS.isEmpty(aButtonView) ? <DialogActions style={oActionContainerStyle}>{aButtonView}</DialogActions>: null;
  };

  render() {
    var aDialogButtons = this.getDialogButtons();


    let sClassName = 'customDialog ' + this.props.className;

    let oStyle = this.props.contentStyle ? this.props.contentStyle : {
          width: '75%',
          maxWidth: '768px',
        };

    /**Its Dialog Specification Never Change It**/
    CS.assign(oStyle, {borderRadius: '3px',});

    let oDialogTitleStyle = {
      fontSize: "18px",
      height: '50px',
      paddingTop: '14px',
    };

    let oDialogTitle = CS.isEmpty(this.props.title) ? null :
                       (<DialogTitle
                           className="customDialogTitleView"
                           style={oDialogTitleStyle}
                           disableTypography={true}>
                         {this.props.title}
                       </DialogTitle>);

    let {classes} = this.props;
    let oBodyStyle = {
      padding: "24px",
      scrollbarWidth: 'thin'
    };
    let oBodyStyleFromProps = CS.assign(oBodyStyle, this.props.bodyStyle);

    return (
        <Dialog disableBackdropClick={this.props.modal }
                open={this.props.open || false}
                onClose={this.handleRequestOnClose}
                actions={aDialogButtons}
                disableEscapeKeyDown={false}
                className={sClassName}
                contentclassname= {this.props.contentClassName}
                bodyclassname= {this.props.bodyClassName}
                PaperProps={{style : oStyle}}>
          {oDialogTitle}
          <DialogContent className={this.props.contentClassName} style={oBodyStyleFromProps} classes={{root:classes[this.props.contentClassName]}}>{this.props.children}</DialogContent>
          {aDialogButtons}
        </Dialog>
    );
  }
}

CustomDialogView.propTypes = oPropTypes;

export const view = withStyles(styles)(CustomDialogView);
export const events = oEvents;
