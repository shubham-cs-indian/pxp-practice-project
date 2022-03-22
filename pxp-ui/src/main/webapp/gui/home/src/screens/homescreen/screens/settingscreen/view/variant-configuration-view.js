import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import {getTranslations as getTranslation} from '../../../../../commonmodule/store/helper/translation-manager.js';
import {view as CustomMaterialButton} from "../../../../../viewlibraries/custommaterialbuttonview/custom-material-button-view";
import {view as GridYesNoPropertyView} from "../../../../../viewlibraries/gridview/grid-yes-no-property-view";

const oEvents = {
  VARIANT_CONFIGURATION_ACTION_BUTTON_CLICKED: "variant_configuration_action_button_clicked",
  VARIANT_CONFIGURATION_TOGGLE_BUTTON_CLICKED: "variant_configuration_toggle_button_clicked",
};

let oPropTypes = {
  isVariantConfigurationEnable: ReactPropTypes.bool
};

// @CS.SafeComponent
class VariantConfigurationView extends React.Component {

  constructor(props) {
    super(props);
  }

  static propTypes = oPropTypes;

  handleVariantActionButtonClicked = (sId) => {
    EventBus.dispatch(oEvents.VARIANT_CONFIGURATION_ACTION_BUTTON_CLICKED, sId);
  };

  handleVariantConfigurationToggleButtonClicked = () => {
    EventBus.dispatch(oEvents.VARIANT_CONFIGURATION_TOGGLE_BUTTON_CLICKED);
  };

  getSnackBarView = () => {
    if (this.props.isDirty) {
      let aButtonIds = ["save", "discard"];
      let aButtonView = [];
      let fButtonHandler = this.handleVariantActionButtonClicked;

      CS.forEach(aButtonIds, (sButtonId) => {
        aButtonView.push(<CustomMaterialButton
            label={getTranslation()[CS.toUpper(sButtonId)]}
            isRaisedButton={true}
            keyboardFocused={true}
            onButtonClick={fButtonHandler.bind(this, sButtonId)}
        />)
      });
      return (<div className={"snackBarView"}>{aButtonView}</div>);
    }
    return null;
  };

  render() {
    let bIsVariantConfigurationEnable = this.props.isVariantConfigurationEnable;
    let oSnackBarView = this.getSnackBarView();

    return (<div className="variantConfigurationContainer">
      <div className="variantWrapper">
        <div className="variantConfigurationBodyContainer">
          <div className="variantLabel">{getTranslation().SELECT_VARIANT}</div>
          <div className="variantConfigurationButtonView">
            <div className="buttonLabel">{getTranslation().SELECT_VARIANT_OPTION}</div>
            <GridYesNoPropertyView value={bIsVariantConfigurationEnable}
                                   onChange={this.handleVariantConfigurationToggleButtonClicked}/>
          </div>
        </div>
      </div>
      {oSnackBarView}
    </div>)
  }
};

VariantConfigurationView.propTypes = oPropTypes;
export const view = VariantConfigurationView;
export const events = oEvents;
