import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import TooltipView from './../tooltipview/tooltip-view';
import { view as CustomMaterialButtonView } from '../custommaterialbuttonview/custom-material-button-view';

const oEvents = {
  /***Warning: plz do not add any event here.
   * try to it from parent view as a handler if needed */
};

const oPropTypes = {
  bodyView: ReactPropTypes.object.isRequired,
  header: ReactPropTypes.string.isRequired,
  showHeader: ReactPropTypes.bool,
  isDefaultFullScreen: ReactPropTypes.bool,
  isFullScreenMode: ReactPropTypes.bool,
  fullScreenHandler: ReactPropTypes.func,
  actionButtonsData: ReactPropTypes.array
};
/**
 * @class FullScreenView - Use in Workflow setting for full screen view mode.
 * @memberOf Views
 * @property {object} bodyView - Pass full body view.
 * @property {string} header - Pass graph in header.
 * @property {bool} [showHeader] - when true show header.
 * @property {bool} [isDefaultFullScreen] - when true enable full screen.
 * @property {bool} [isFullScreenMode] - when true enable full screen mode.
 * @property {func} [fullScreenHandler] - handle function for full screen mode.
 */

// @CS.SafeComponent
class FullScreenView extends React.Component {

  constructor (props) {
    super(props);

    this.state = {
      isFullScreenMode: this.props.isDefaultFullScreen || this.props.isFullScreenMode || false
    }
  }

  static getDerivedStateFromProps (oNextProps, oState) {
    if(CS.isBoolean(oNextProps.isFullScreenMode)){
      return{
        isFullScreenMode: oNextProps.isFullScreenMode
      };
    }
    return null;
  }

  handleFullScreenEnabled = () => {
    if(this.props.fullScreenHandler){
      this.props.fullScreenHandler();
    }else {
      let bIsFullScreenMode = this.state.isFullScreenMode;
      this.setState({
        isFullScreenMode: !bIsFullScreenMode
      });
    }
  };

  getFullScreenButtonClassName = () => {
    let bIsFullScreen = this.state.isFullScreenMode;

    let sClassName = bIsFullScreen ? "fullScreenButtonContainer fullScreen " : "fullScreenButtonContainer ";
    if(this.props.className) {
      sClassName += this.props.className;
    }

    return sClassName;
  };

  getFullScreenButtonView = () => {
    let sClassName = this.getFullScreenButtonClassName();
    let sLabel = this.state.isFullScreenMode ? null : getTranslation().FULLSCREEN;

    return (
        <div className={sClassName}>
          <TooltipView placement="bottom" label={sLabel}>
          <div className="fullScreenButton"
               onClick={this.handleFullScreenEnabled}>
          </div>
          </TooltipView>
        </div>
    )
  };

  getHeaderLabel = () => {
    let bIsFullScreen = this.state.isFullScreenMode;
    let sHeader = this.props.header;
    let bShowHeader = this.props.showHeader;

    if(bIsFullScreen) {
      return <div className="fullScreenHeaderLabel">{sHeader}</div>;
    } else {
      return bShowHeader ? <div className="fullScreenHeaderLabel">{sHeader}</div> : null;
    }
  };

  getFooterView = () => {
    let _this = this;
    let aActionButtonsData = _this.props.actionButtonsData;
    if(CS.isEmpty(aActionButtonsData)) {
      return null;
    }

    let aFooterButtonView = [];
    CS.forEach(aActionButtonsData, function (oButton) {
      aFooterButtonView.push(
          <CustomMaterialButtonView
              key={oButton.id}
              label={oButton.label}
              isRaisedButton={!oButton.isFlat}
              isDisabled={false}
              variant={oButton.variant || ""}
              color={oButton.color || ""}
              onButtonClick={oButton.actionHandler.bind(_this, oButton.id)}/>
      );
    });

    return (
        <div className="fullScreenFooter">
          {aFooterButtonView}
        </div>
    )
  };

  getHeaderView = () => {
    let oFullScreenButtonView = this.getFullScreenButtonView();
    let oHeaderLabelView = this.getHeaderLabel();
    let bIsFullScreen = this.state.isFullScreenMode;
    let bShowHeader = this.props.showHeader;

    if(!bShowHeader && !bIsFullScreen) {
      /** When full screen is off, and show only icon without header **/
      return oFullScreenButtonView;
    } else {
      return (
          <div className="fullScreenHeader">
            {oHeaderLabelView}
            {oFullScreenButtonView}
          </div>
      );
    }
  };

  getBodyView = () => {
    let oProps = this.props;
    let oBodyView = oProps.bodyView;

    return (
        <div className={"fullScreenBody"}>
          {oBodyView}
        </div>
    )
  };

  render () {
    let oProps = this.props;
    let aActionButtonsData = oProps.actionButtonsData;
    let bShowFooter = CS.isNotEmpty(aActionButtonsData);
    let bIsFullScreen = this.state.isFullScreenMode;
    let sClassName = "fullScreenView ";

    if(bIsFullScreen) {
      sClassName += "fullScreen ";
      if(bShowFooter) {
        sClassName += "withFooter";
      }
    }

    return(
        <div className={sClassName}>
          {this.getHeaderView()}
          {this.getBodyView()}
          {this.getFooterView()}
        </div>
    );
  }
}

FullScreenView.propTypes = oPropTypes;

export default {
  view: FullScreenView,
  events: oEvents
}
