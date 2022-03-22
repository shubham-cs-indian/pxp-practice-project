import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher';
import TooltipView from './../tooltipview/tooltip-view';
import SessionStorageManager from '../../libraries/sessionstoragemanager/session-storage-manager';
import { view as ImageFitToContainerView } from './../imagefittocontainerview/image-fit-to-container-view';
import ViewUtils from './../utils/view-library-utils';
import { getTranslations as oTranslations } from '../../commonmodule/store/helper/translation-manager.js';
import SessionStorageConstants from '../../commonmodule/tack/session-storage-constants';

var oEvents = {
  LANGUAGE_SELECTION_VIEW_HANDLE_LANGUAGE_CHANGED: "language_selection_view_handle_language_changed",
};

const oPropTypes = {
  languageSelectionData: ReactPropTypes.array,
};
/**
 * @class LanguageSelectionView
 * @memberOf Views
 * @property {array} [languageSelectionData] - Contains languages data.
 */

// @CS.SafeComponent
class LanguageSelectionView extends React.Component {
  constructor (props) {
    super(props);

    this.state = {
      selectedLanguage: SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE),
      isLanguageChanged: false
    }
  }

  handleChangeLanguageRequest = (sSelectedLanguageId) => {
    EventBus.dispatch(oEvents.LANGUAGE_SELECTION_VIEW_HANDLE_LANGUAGE_CHANGED, sSelectedLanguageId);
  };

/*  handleLanguageSelection = (sSelectedLanguageId) => {
    this.setState({
      selectedLanguage: sSelectedLanguageId,
      isLanguageChanged: true
    });
  };

  getDialogButtons = () => {
    let aButtonView = [];
    let _this = this;
    let aButtonData = [{
      id: "cancel",
      label: oTranslations().ALERTIFY_CANCEL,
      isFlat: true,
    },
      {
        id: "ok",
        label: oTranslations().ALERTIFY_OK,
        isFlat: false,
      }];

    CS.forEach(aButtonData, function (oButton) {
      aButtonView.push(<CustomMaterialButtonView
          label={oButton.label}
          isRaisedButton={!oButton.isFlat}
          isDisabled={false}
          onButtonClick={_this.handleChangeLanguageRequest.bind(_this, oButton.id)}/>);
    });
    return (
        <div className="languageDialogButtonContainer">
          {aButtonView}
        </div>
    );
  };*/

  getLanguageSelectionView = () => {
    let aLanguageElementViews = this.getLanguageElementViews(this.props.languageInfo);

    return (
        <div className="languagePreferencesView">
          <div className="languagePreferencesViewMessageSection">
            <div className="languageWarningMessageView">
              {oTranslations().CHANGE_OF_THE_LANGUAGE_WARNING_MESSAGE}
            </div>
            {aLanguageElementViews}
          </div>
          {/*{bIsLanguageChanged ? this.getDialogButtons() : null}*/}
        </div>
    );
  };

  getLanguageElementViews = (oLanguageSelectionData) => {
    let _this = this;
    let sSelectedLanguage = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE);
    return CS.map(oLanguageSelectionData, function (oLanguageNode) {
      let sLanguageNodeClassName = "languageNode ";
      if (oLanguageNode.id === sSelectedLanguage) {
        sLanguageNodeClassName += "isSelected ";
      }
      let sImageSrc = (oLanguageNode.icon) ? ViewUtils.getIconUrl(oLanguageNode.icon) : "";

      return (
          <TooltipView label={CS.getLabelOrCode(oLanguageNode)} key={oLanguageNode.id}>
            <div className={sLanguageNodeClassName}
                 onClick={_this.handleChangeLanguageRequest.bind(_this, oLanguageNode.id)}>
              <span className="languageIcon">
                <div className='languageIconImage '>
                  <ImageFitToContainerView imageSrc={sImageSrc}/>
                </div>
              </span>
              <div className="languageLabel">{CS.getLabelOrCode(oLanguageNode)}</div>
            </div>
          </TooltipView>
      );
    });
  };

  render () {
    let aLanguageSelectorView = this.getLanguageSelectionView();
    return (
        <div className="languagePreferencesContainer">
          {aLanguageSelectorView}
        </div>
    );
  }
}

LanguageSelectionView.propTypes = oPropTypes;

export const view = LanguageSelectionView;
export const events = oEvents;
