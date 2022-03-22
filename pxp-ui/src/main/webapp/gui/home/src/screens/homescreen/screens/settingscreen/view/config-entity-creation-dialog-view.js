
import CS from '../../../../../libraries/cs';

import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as CommonConfigSectionView } from '../../../../../viewlibraries/commonconfigsectionview/common-config-section-view.js';
import { view as CustomDialogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import CreateDialogLayoutData from '../tack/mock/create-dialog-layout-data';
import ViewUtils from '../view/utils/view-utils';

const oEvents = {
  HANDLE_ENTITY_CONFIG_DIALOG_BUTTON_CLICKED: "handle_entity_config_dialog_button_clicked"
};

const oPropTypes = {
  activeEntity : ReactPropTypes.object,
  entityType : ReactPropTypes.string,
  model: ReactPropTypes.object,
  extraFieldsToShow: ReactPropTypes.object
};

// @CS.SafeComponent
class ConfigEntityCreationDialogView extends React.Component {
  static propTypes = oPropTypes;

  handleEntityDialogButtonClicked = (sButtonId) => {
    var sEntityType = this.props.entityType;
    EventBus.dispatch(oEvents.HANDLE_ENTITY_CONFIG_DIALOG_BUTTON_CLICKED, sButtonId, sEntityType);
  };

  getDialogView = () => {
    var oActiveEntity = this.props.activeEntity;
    var sEntityType = this.props.entityType;
    var oExtraFieldsToShow = this.props.extraFieldsToShow;
    if (oActiveEntity.isCreated || oActiveEntity.isNewlyCreated) {

      var sErrorText = "";
      var bIsDisableCreate = true;

      if (CS.isEmpty(oActiveEntity.code.trim())) {
        sErrorText = getTranslations().CODE_SHOULD_NOT_BE_EMPTY;
      } else if (!ViewUtils.isValidEntityCode(oActiveEntity.code)) {
        sErrorText = getTranslations().PLEASE_ENTER_VALID_CODE;
      } else {
        bIsDisableCreate = false;
      }

      let oModel = this.props.model ? this.props.model : {
        label:  oActiveEntity.label || "",
        code:  oActiveEntity.code || ""
      };

      CS.assign(oModel, oExtraFieldsToShow);

      var oBodyStyle = {
        padding: '0 10px 20px 10px'
      };
      var aButtonData = [
        {
          id: "cancel",
          label: getTranslations().CANCEL,
          isDisabled: false,
          isFlat: true,
        },
        {
          id: "create",
          label: getTranslations().CREATE,
          isDisabled: bIsDisableCreate,
          isFlat: false,
        }

      ];
      let fButtonHandler = this.handleEntityDialogButtonClicked;
      let aSectionLayoutData = CreateDialogLayoutData();

      return (<CustomDialogView modal={true} open={true}
                                title={getTranslations().CREATE}
                                bodyStyle={oBodyStyle}
                                bodyClassName=""
                                contentClassName="createEntityModalDialog"
                                buttonData={aButtonData}
                                onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
                                buttonClickHandler={fButtonHandler}>
        <CommonConfigSectionView context={sEntityType} sectionLayout={aSectionLayoutData} data={oModel} errorTextForCodeEntity={sErrorText}/>
      </CustomDialogView>);

    }
    else {
      return null;
    }
  };

  render() {
    return (this.getDialogView());
  }
}

export const view = ConfigEntityCreationDialogView;
export const events = oEvents;
