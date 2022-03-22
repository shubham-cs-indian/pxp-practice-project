import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import CS from '../../../../../libraries/cs';
import SettingUtil from './utils/view-utils';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as GridView } from '../../../../../viewlibraries/gridview/grid-view';
import { view as ContextMenuViewNew } from '../../../../../viewlibraries/contextmenuwithsearchview/context-menu-view-new';
import ContextMenuViewModel from '../../../../../viewlibraries/contextmenuwithsearchview/model/context-menu-view-model';
import { view as ImageFitToContainerView } from '../../../../../viewlibraries/imagefittocontainerview/image-fit-to-container-view';

const oEvents = {
  TAG_CONFIG_VIEW_DATA_LANGUAGE_CHANGED: "tag_config_view_data_language_changed"
};

/** need to change with // @CS.SafeComponent **/
// @React.getSafeComponent
class TagConfigView extends React.Component {
  static propTypes = {
    gridData: ReactPropTypes.object,
    dataLanguageData: ReactPropTypes.object
  }

  constructor (props) {
    super(props);
  }

  handleDataLanguageChanged = (oSelectedItem) => {
    EventBus.dispatch(oEvents.TAG_CONFIG_VIEW_DATA_LANGUAGE_CHANGED, oSelectedItem.id);
  };

  getContextMenuModelList = (aLanguageSelectorData, oSelectedLanguage) => {
    let aContextModelList = [];
    CS.forEach(aLanguageSelectorData, function (oItem) {
      if(oItem.code !== oSelectedLanguage.code)
        aContextModelList.push(new ContextMenuViewModel(
            oItem.id,
            CS.getLabelOrCode(oItem),
            false,
            oItem.icon,
            {}
        ));
    });

    return aContextModelList;
  };

  getDLContextView = () => {
    let oDataLanguageData = this.props.dataLanguageData;
    let oSelectedLanguage = oDataLanguageData.selectedDataLanguage;
    let sIconURL = SettingUtil.getIconUrl(oSelectedLanguage.icon);
    return (<div className="languageForTagContainer">
      <ContextMenuViewNew
          contextMenuViewModel={this.getContextMenuModelList(oDataLanguageData.dataLanguages, oSelectedLanguage)}
          onClickHandler={this.handleDataLanguageChanged}
          anchorOrigin={{horizontal: 'left', vertical: 'bottom'}}
          targetOrigin={{horizontal: 'left', vertical: 'top'}}
          showSearch={false}>
        <div className={"languageSelectorContainer"}>
          <div className="dataLanguageLabel">{getTranslation().DATA_LANGUAGE + " :"}</div>
          <div className="languageSelectorIcon">
            <ImageFitToContainerView imageSrc={sIconURL}/>
          </div>
          <div className="languageSelectorLabel">{oSelectedLanguage.label}</div>
          <div className="buttonIcon"></div>
        </div>
      </ContextMenuViewNew>
    </div>)
  };

  render () {
    let oActionItems = this.getDLContextView();
    let oGridProps = this.props.gridData;
    oGridProps.customActionView = oActionItems;

    return (
        <div className="tagConfigViewContainer">
          <GridView
              {...this.props.gridData}
          />
        </div>

    );
  }
}

export const view = TagConfigView;
export const events = oEvents;
