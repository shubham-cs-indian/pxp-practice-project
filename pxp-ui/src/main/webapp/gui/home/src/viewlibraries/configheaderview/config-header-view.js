import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import { view as SimpleSearchBarView } from '../simplesearchbarview/simple-search-bar-view';
import { propTypes as SimpleSearchBarViewPropTypes } from '../simplesearchbarview/simple-search-bar-view';
import { view as CustomMaterialButtonView } from '../custommaterialbuttonview/custom-material-button-view';
import { view as ConfigFileUploadButtonView } from '../configfileuploadbuttonview/config-file-upload-button-view';
import TooltipView from '../tooltipview/tooltip-view';
import assetTypes from '../tack/coverflow-asset-type-list';

const oEvents = {
  CONFIG_HEADER_VIEW_ACTION_BUTTON_CLICKED: "config_header_view_action_button_clicked",
  CONFIG_HEADER_VIEW_HANDLE_SEARCH: "config_header_view_handle_search"
};

const oPropTypes = {
  searchBarProps: ReactPropTypes.shape(SimpleSearchBarViewPropTypes),
  actionButtonList: ReactPropTypes.array,
  context: ReactPropTypes.string,
  showSaveDiscardButtons: ReactPropTypes.bool,
  filesUploadHandler: ReactPropTypes.func,
  hideSearchBar: ReactPropTypes.bool,
  eventsActionHandler : ReactPropTypes.func,
  bMultiple:ReactPropTypes.bool,
};
/**
 * @class ConfigHeaderView - use for display header view and header button in views of Application.
 * @memberOf Views
 * @property {custom} [searchBarProps] - searchbar value.
 * @property {array} [actionButtonList] -  array of actionButtonList.
 * @property {string} [context] - context name.
 * @property {bool} [showSaveDiscardButtons] - boolean value of save and discard button or not.
 * @property {func} [filesUploadHandler] - function for file upload.
 * @property {bool} [hideSearchBar] - boolean for hide or show searchBar.
 * @property {func} [eventsActionHandler] - function for eventsActionHandler.
 */

// @CS.SafeComponent
class ConfigHeaderView extends React.Component {

  constructor (props) {
    super(props);
  }

  handleActionButtonClicked = (sButtonId, aFiles) => {
    if (CS.isFunction(this.props.eventsActionHandler)) {
      this.props.eventsActionHandler(sButtonId, aFiles);
    } else {
      EventBus.dispatch(oEvents.CONFIG_HEADER_VIEW_ACTION_BUTTON_CLICKED, sButtonId, aFiles);
    }
  };

  handleSearch = (sSearchText) => {
    let sContext = this.props.context;
    EventBus.dispatch(oEvents.CONFIG_HEADER_VIEW_HANDLE_SEARCH, sContext, sSearchText);
  };

  getActionButtonViews = (aActionButtonList, bShowSaveDiscardButtons) => {
    let _this = this;
    let aActionButtonViews = [];

    //todo: hardcode alert ("save" and "discard")
    if (bShowSaveDiscardButtons) {

      aActionButtonViews.push(
          <div className="saveOrDiscardButton" key="discard">
            <CustomMaterialButtonView
                label={getTranslation().DISCARD}
                isRaisedButton={false}
                isDisabled={false}
                onButtonClick={_this.handleActionButtonClicked.bind(_this, "discard")}
                //
            />
          </div>
      );
      aActionButtonViews.push(
          <div className="saveOrDiscardButton save" key="save">
            <CustomMaterialButtonView
                label={getTranslation().SAVE}
                isRaisedButton={true}
                isDisabled={false}
                onButtonClick={_this.handleActionButtonClicked.bind(_this, "save")}/>
          </div>
      );
    }

    CS.forEach(aActionButtonList, function (oButton) {
      let sId = oButton.id;
      let sIconClassName = oButton.className || "";
      let sTitle = getTranslation()[oButton.title];
      let aImportScreenList = [
        'import_attribution_taxonomy','import_propertyCollection', "import_organisation",'import_class',"import_language_tree",
        "import_taxonomy","upload_icon_library"
      ];

      if (CS.includes(aImportScreenList, sId)) {
        let aFiles = (sId === "upload_icon_library") ? assetTypes.imageTypes : assetTypes.fileImportTypes;
        aActionButtonViews.push(<ConfigFileUploadButtonView label={sTitle} className={sIconClassName}
                                                            aAllowedFiles={aFiles}
                                                            filesUploadHandler={_this.props.filesUploadHandler}
                                                            key={sId} bMultiple={_this.props.bMultiple}
                                                            context={_this.props.context}/>)
      } else if (!CS.isEmpty(oButton.view)) {
        aActionButtonViews.push(oButton.view);
      }
      else {
        aActionButtonViews.push(
            <TooltipView label={sTitle} key={sId}>
              <div className="actionButton" onClick={_this.handleActionButtonClicked.bind(_this, sId)}>
                <div className={"actionButtonIcon " + sIconClassName}></div>
              </div>
            </TooltipView>
        );
      }
    });

    return aActionButtonViews;
  };

  render () {

    let oSearchBarProps = {};
    oSearchBarProps.onBlur = this.handleSearch;
    if (this.props.searchBarProps) {
      CS.assign(oSearchBarProps, this.props.searchBarProps)
    }
    let oCustomHeaderView = this.props.customHeaderView ? <div className="customHeaderView"> {this.props.customHeaderView} </div> : null;
    let aActionButtonList = this.props.actionButtonList || [];
    let bShowSaveDiscardButtons = this.props.showSaveDiscardButtons;
    let aActionButtonViews = this.getActionButtonViews(aActionButtonList, bShowSaveDiscardButtons);
    let oSearchBarDOM = this.props.hideSearchBar ? null :
        <div className="searchBarContainer">
          <SimpleSearchBarView {...oSearchBarProps}/>
        </div>;

    return (
        <div className="configHeaderView" onClick={this.handleBackButtonClicked}>
          {oSearchBarDOM}
          {oCustomHeaderView}
          <div className="actionButtonsContainer">
            {aActionButtonViews}
          </div>

        </div>
    );
  }

}

ConfigHeaderView.propTypes = oPropTypes;

export const view = ConfigHeaderView;
export const events = oEvents;
export const propTypes = ConfigHeaderView.propTypes;
