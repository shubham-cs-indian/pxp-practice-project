import CS from '../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as ImageSimpleView } from '../../../viewlibraries/imagesimpleview/image-simple-view';
import { view as LoginScreenFormView } from './../view/login-screen-form-view.jsx';
import { getTranslations } from '../../../commonmodule/store/helper/translation-manager.js';

const oPropTypes = {
  store: ReactPropTypes.object,
  action: ReactPropTypes.object
};

// @CS.SafeComponent
class LoginScreenController extends React.Component {
  static propTypes = oPropTypes;

  constructor(props, context) {
    super(props, context);
    var initialState = {
      data: this.getStore().getData()
    };
    this.state = initialState;
  }

  //@Bind: Store with state
  componentDidMount() {
    this.getStore().bind('change', this.loginScreenStateChanged);
    this.props.action.registerEvent();
  }

  //@UnBind: store with state
  componentWillUnmount() {
    this.getStore().unbind('change', this.loginScreenStateChanged);
    this.props.action.deRegisterEvent();
  }

  //@set: state
  loginScreenStateChanged = () => {
    var changedState = {
      data: this.getStore().getData()
    };
    this.setState(changedState);
  };

  getStore = () => {
    return this.props.store;
  };

  getLogoView = (oThemeConfig) => {
    let sClassName = "loginScreenLogo";
    let oLogoDom = null;
    if (CS.isNotEmpty(oThemeConfig.primaryLogoId)) {
      oLogoDom = (<ImageSimpleView classLabel="userDefinedLogo" imageSrc={CS.getIconUrl(oThemeConfig.primaryLogoId)}/>);
    } else {
      sClassName += " defaultLogo";
    }
    return (<div className={sClassName}>{oLogoDom}</div>);
  };

  getScreenTitle = (oThemeConfig) => {
    let oTitleDom = null;
    let oStyle = {color: oThemeConfig.loginScreenFontColor};
    if (CS.isNotEmpty(oThemeConfig.welcomeMessage)) {
      oTitleDom = (<span className="loginScreenSubTitle" style={oStyle}>{oThemeConfig.welcomeMessage}</span>)
    } else {
      oTitleDom = (
          <React.Fragment>
            <p style={oStyle}>{getTranslations().WELCOME_TO}</p>
            <span style={oStyle}>{getTranslations().PRODUCT_EXPERIENCE_PLATFORM}</span>
          </React.Fragment>
      );
    }
    return (<div className="welcomeMessage">{oTitleDom}</div>)
  };

  getRightSectionView = (oThemeConfig) => {
    let oStyle = null;
    if (CS.isNotEmpty(oThemeConfig.loginScreenBackgroundImageKey)) {
      oStyle = {
        backgroundImage: `url(${CS.getIconUrl(oThemeConfig.loginScreenBackgroundImageKey)})`,
      };
    }
    return (
        <div className="loginRightImage" style={oStyle} />
    );
  };

  render() {
    let oThemeConfig = this.getStore().getLogoConfig();
     let oStyle = {background: `${oThemeConfig.generalThemeColor}`};
    return (
        <div>
          <div className="loginScreenContainer" style={oStyle}>
            {this.getLogoView(oThemeConfig)}
            {this.getScreenTitle(oThemeConfig)}
            <LoginScreenFormView data={this.state.data} themeConfig={oThemeConfig}/>
          </div>
          {this.getRightSectionView(oThemeConfig)}
        </div>
    );
  }
}

export default LoginScreenController;
