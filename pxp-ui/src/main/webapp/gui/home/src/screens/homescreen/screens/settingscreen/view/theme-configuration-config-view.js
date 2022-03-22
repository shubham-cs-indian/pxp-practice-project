import React from "react";
import ReactPropTypes from "prop-types";
import CS from "../../../../../libraries/cs";
import {view as CommonConfigSectionView} from "../../../../../viewlibraries/commonconfigsectionview/common-config-section-view";
import SectionLayout from "../tack/theme-configuration-layout-data";
import {getTranslations as getTranslation} from '../../../../../commonmodule/store/helper/translation-manager.js';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import {view as CustomMaterialButton} from '../../../../../viewlibraries/custommaterialbuttonview/custom-material-button-view';
import {view as ConfigHeaderView} from '../../../../../viewlibraries/configheaderview/config-header-view';

const oEvents = {
  THEME_CONFIGURATION_SNACK_BAR_BUTTON_CLICKED: "theme_configuration_snack_bar_button_clicked",
};

const oPropTypes = {
  themeConfigurationModelData: ReactPropTypes.object,
  showSaveDiscardButtons: ReactPropTypes.bool,
  headerActionGroup: ReactPropTypes.array,
};

class ThemeConfigurationView extends React.Component {
  static propTypes = oPropTypes;


  handleSnackBarButtonClicked = (sButtonId, oEvent) => {
    EventBus.dispatch(oEvents.THEME_CONFIGURATION_SNACK_BAR_BUTTON_CLICKED, sButtonId);
    oEvent.stopPropagation();
  };

  getSnackBarView = () => {
    if (this.props.showSaveDiscardButtons) {
      let aButtonIds = ["save", "discard"];
      let aButtonView = [];
      CS.forEach(aButtonIds, (sButtonId) => {
        aButtonView.push(<CustomMaterialButton
            label={getTranslation()[CS.toUpper(sButtonId)]}
            isRaisedButton={true}
            keyboardFocused={true}
            onButtonClick={this.handleSnackBarButtonClicked.bind(this, sButtonId)}
        />)
      });
      return (<div className={"snackBarView"}>{aButtonView}</div>);
    }
    return null;
  };

  getConfigHeaderView = () => {
    if (CS.isNotEmpty(this.props.headerActionGroup)) {
      return (<ConfigHeaderView
          actionButtonList={this.props.headerActionGroup}
          hideSearchBar={true}
          context={"themeConfiguration"}/>)
    }
    return null;
  };

  getCommonConfigSections = () => {
    let aCommonConfigSections = [];
    CS.forEach(this.props.themeConfigurationModelData, (oConfiguration, sConfiguration) => {
      let aSectionLayout = new SectionLayout()[sConfiguration];
      aCommonConfigSections.push(
          <div className="themesConfigurationSection">
            <div className="themesConfigurationHeaderLabel">
              {oConfiguration.headerLabel}
            </div>
            <CommonConfigSectionView
                context="ThemeConfigurationConfig"
                sectionLayout={aSectionLayout}
                data={oConfiguration}/>
          </div>
      )
    });
    return aCommonConfigSections;
  };

  render () {
    let oConfigHeaderView = this.getConfigHeaderView();
    let oSnackBarView = this.getSnackBarView();
    let oStyle = {height: `calc(100% - ${CS.isNotEmpty(oSnackBarView) ? 92 : 52}px)`};
    oStyle["overflow"] = "auto";
    oStyle["scrollbarWidth"] = "thin";
    return (
        <div className={"themeConfigurationViewContainer"}>
          {oConfigHeaderView}
          <div style={oStyle}>
            {this.getCommonConfigSections()}
          </div>
          {oSnackBarView}
        </div>);
  }
}

export const view = ThemeConfigurationView;
export const events = oEvents;
