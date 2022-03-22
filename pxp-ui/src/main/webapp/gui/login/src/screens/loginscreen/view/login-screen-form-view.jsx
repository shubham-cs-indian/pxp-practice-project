import CS from '../../../libraries/cs';
import React from 'react';
import Paper from '@material-ui/core/Paper';
import TextField from '@material-ui/core/TextField';
import {view as CustomMaterialButtonView} from '../../../viewlibraries/custommaterialbuttonview/custom-material-button-view';
import EventBus from '../../../libraries/eventdispatcher/EventDispatcher.js';
import {getTranslations} from '../../../commonmodule/store/helper/translation-manager.js';
import alertify from "../../../commonmodule/store/custom-alertify-store";
import ReactPropTypes from 'prop-types';
import ColorUtils from '../../../commonmodule/util/color-utils';
import {withStyles} from '@material-ui/core/styles';
import {hexToFilter} from "css-filter-generator";
import ThemeLoader from '../../../libraries/themeloader/theme-loader.js';

function CustomizedTextField (props) {
  const {classes, InputLabelProps : oInputLabelProps, InputProps : oInputProps} = props;
  return (
      <div className={classes.root}>
        <TextField
            id={props.id}
            autoFocus={props.autoFocus}
            className={props.className}
            hintText={props.hintText}
            fullWidth={true}
            label={props.label}
            type={props.type}
            disabled={props.disabled}
            inputRef={props.inputRef}
            onChange={props.onChange}
            onKeyDown={props.onKeyDown}
            InputLabelProps={{
              classes: {
                root: classes.cssLabel,
                focused: classes.cssFocused,
              },
              style: oInputLabelProps.style,
            }}
            InputProps={{
              classes: {
                focused: classes.cssFocused,
                disabled: classes.cssDisabled,
                underline: classes.cssUnderline,
                root: classes.root,
              },
              style: oInputProps.style,
            }}
        />
      </div>
  );
}

var oEvents = {
  USERNAME_CHANGED: "username_changed",
  PASSWORD_CHANGED: "password_changed",
  LOGIN_BUTTON_CLICKED: "login_button_clicked",
  NEXT_BUTTON_CLICKED: "next_button_clicked",
  EDIT_BUTTON_CLICKED: "edit_button_clicked"
};

const oPropTypes = {
  data: ReactPropTypes.object
};

// @CS.SafeComponent
class LoginScreenFormView extends React.Component {

  constructor(props) {
    super(props);
    this.textFieldRef = React.createRef();
  }

  componentDidUpdate () {
  }

  componentWillMount () {
    var oThemeConfig = this.props.themeConfig;
    var sFontColor = oThemeConfig.loginScreenFontColor;
    let styles = theme => ({
      root: {
        display: 'flex',
        flexWrap: 'wrap',
      },
      cssLabel: {
        '&$cssFocused': {
          color: sFontColor,
        },
      },
      cssFocused: {},
      cssDisabled: {},
      cssUnderline: {
        '&:after': {
          borderBottomColor: sFontColor,
        },
        '&:before': {
          borderBottomColor: sFontColor,
        },
        '&:hover:not($disabled):after': {
          borderBottomColor: sFontColor,
        },
        '&:hover:not($disabled):before': {
          borderBottomColor: sFontColor,
        },
        '&:hover:not($disabled):not($error):not($focused):before': {
          borderBottomColor: sFontColor,
        },
      },
    });
    this.customTextField = withStyles(styles)(CustomizedTextField);
  }

  userNameChanged = (oEvent) => {
    var sUserName = oEvent.target.value;
    EventBus.dispatch(oEvents.USERNAME_CHANGED, this, sUserName);
  };

  userPasswordChanged = (oEvent) => {
    var sPassword = oEvent.target.value;
    EventBus.dispatch(oEvents.PASSWORD_CHANGED, this, sPassword);
  };

  handleSubmitButtonClick = (oEvent) => {
    EventBus.dispatch(oEvents.LOGIN_BUTTON_CLICKED);
  };

  handleSubmitButtonOnKeyDown = (oEvent) => {
    if (oEvent.keyCode === 13 && this.props.data.display) {
      this.handleNextButtonClicked();
    }
    else if (oEvent.keyCode === 13) {
      this.handleSubmitButtonClick(oEvent);
    }
  };

  handleNextButtonClicked = () => {
    if (this.textFieldRef && CS.isEmpty(this.textFieldRef.current.value)) {
      let oTranslations = getTranslations('LoginScreenTranslations');
      alertify.message(oTranslations.FILL_USER_NAME);
    } else {
      EventBus.dispatch(oEvents.NEXT_BUTTON_CLICKED);
    }
  };

  onKeyDown = (oEvent) => {
    if (oEvent.keyCode === 13 && this.props.data.display) {
      this.handleSubmitButtonClick();
    }
    else if (oEvent.keyCode === 13 && !this.props.data.display) {
      this.handleNextButtonClicked();
    }
  };

  handleEnableInputField = () => {
    EventBus.dispatch(oEvents.EDIT_BUTTON_CLICKED);
  };

  getCustomMaterialButtonView (sButtonId) {
    var oThemeConfig = this.props.themeConfig;
    let oTranslations = getTranslations('LoginScreenTranslations');
    let sButtonLabel = "";
    let sClassName = "";
    let fButtonClickHandler = {};
    let loginScreenButtonBackgroundColor= oThemeConfig.loginScreenButtonBackgroundColor;
      let oButtonStyle = {
      height: "40px",
      lineHeight: "30px",
      padding: '0 5px',
      margin: '0',
      width: '134px',
      borderRadius: '3px',
      float: 'left',
      marginRight: '5px',
      background: loginScreenButtonBackgroundColor,
      color: oThemeConfig.loginScreenButtonFontColor
        };

    switch (sButtonId) {
      case 'nextButton' :
        sButtonLabel = oTranslations.NEXT;
        sClassName = "nextButton";
        fButtonClickHandler = this.handleNextButtonClicked;
        break;
      case 'formSubmitButton' :
        sButtonLabel = oTranslations.LOGIN_BUTTON_LABEL;
        sClassName = "formSubmitButton";
        fButtonClickHandler = this.handleSubmitButtonClick;
        break;
    }

    return (<CustomMaterialButtonView
        className={sClassName}
        label={sButtonLabel}
        isContainedButton={true}
        isDisabled={false}
        onKeyDown={this.handleSubmitButtonOnKeyDown}
        onButtonClick={fButtonClickHandler}
        style={oButtonStyle}/>);
  };

  getLoginScreenTextField (sTextFieldId, sFontColor = "", bIsDisabled = false) {
    let oTranslations = getTranslations('LoginScreenTranslations');
    let sLabel = "";
    let oInputRef = {};
    let fOnChangeHandler = {};
    let sFieldType = "";

    let sRGBValueOfFontColor = CS.join(ColorUtils.hexToRGBConversion(sFontColor), ", ");
    let oInputStyle = {
      color: sFontColor
    };
    let oInputLabelStyle = {
      transform: "unset",
      fontFamily: "Rubik",
      color: `rgba(${sRGBValueOfFontColor}, 0.54)`
    };

    switch (sTextFieldId) {
      case 'userNameField':
        sLabel = oTranslations.USER_NAME;
        oInputRef = this.textFieldRef;
        fOnChangeHandler = this.userNameChanged;
        break;
      case 'userPasswordField' :
        sLabel = oTranslations.PASSWORD;
        sFieldType = "password";
        fOnChangeHandler = this.userPasswordChanged;
        break;
    }

    let CustomTextField = this.customTextField || TextField;
    return (
        <CustomTextField
            id={sTextFieldId}
            inputRef={oInputRef}
            type={sFieldType}
            autoFocus={true}
            disabled={bIsDisabled}
            hintText={sLabel}
            className={'loginFormTextField'}
            label={sLabel}
            InputLabelProps={{style: oInputLabelStyle}}
            InputProps={{style: oInputStyle}}
            onChange={fOnChangeHandler}
            onKeyDown={this.onKeyDown}/>
    );
  };

  render () {
    var oThemeConfig = this.props.themeConfig;
    var sFontColor = oThemeConfig.loginScreenFontColor;
    let sProjectVersion = process.env.REACT_APP_PXP_VERSION;
    let sClassName = "loginFormElement password";
    let sCurrentButtonId = "nextButton";
    let oPasswordTextFieldView = null;
    let oContextEditNameView = null;
    let oData = this.props.data;
    let bDisplayData = oData && oData.display;
    if (bDisplayData) {
      sClassName = "loginFormElement passwordField";
      sCurrentButtonId = "formSubmitButton";
      oPasswordTextFieldView = this.getLoginScreenTextField('userPasswordField', sFontColor);
      oContextEditNameView = (<div className="contentNameEditButton" onClick={this.handleEnableInputField}/>);
      ThemeLoader.setNewCSSStyleSheetRule(".contentNameEditButton", hexToFilter(sFontColor).filter);
    }
    let oUserNameTextFieldView = this.getLoginScreenTextField('userNameField', sFontColor, bDisplayData);
    let oButtonView = this.getCustomMaterialButtonView(sCurrentButtonId);

    return (
        <div>
          <Paper zDepth={2} className="loginScreenFormContainer">
            <div className="formContainer">
              <div className="loginFormElement">
                {oUserNameTextFieldView}
                {oContextEditNameView}
              </div>
              <div className={sClassName}>
                {oPasswordTextFieldView}
              </div>
              <div className="loginFormElement">
                {oButtonView}
              </div>
            </div>
          </Paper>
          <div className="loginScreenCopyright">
            <p style={{color: sFontColor}}> &copy; Contentserv {sProjectVersion}</p>
          </div>
        </div>
    );
  }
};

export const view = LoginScreenFormView;
export const events = oEvents;
