import React from 'react';
import ReactPropTypes from 'prop-types';
import CS from '../../libraries/cs';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import ViewUtils from '../utils/view-library-utils';
import TooltipView from '../tooltipview/tooltip-view';
import { view as ImageFitToContainerView } from '../imagefittocontainerview/image-fit-to-container-view';
import { view as CustomDialogView } from '../customdialogview/custom-dialog-view';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import { view as NothingFoundView } from '../nothingfoundview/nothing-found-view';

const oEvents = {
  UI_AND_DATA_LANGUAGE_INFO_VIEW_HANDLE_LANGUAGE_CHANGED: "ui_and_data_language_info_view_handle_language_changed",
  UI_AND_DATA_LANGUAGE_INFO_VIEW_GET_LANGUAGE_INFO: "UI_AND_DATA_LANGUAGE_INFO_VIEW_GET_LANGUAGE_INFO"
};

const oPropTypes = {
  isDialogOpen: ReactPropTypes.bool,
  selectedDataLanguageId: ReactPropTypes.string,
  selectedUILanguageId: ReactPropTypes.string,
  dataLanguages: ReactPropTypes.array,
  uiLanguages: ReactPropTypes.array,
  isShowUILanguage: ReactPropTypes.bool,
  isShowDataLanguage: ReactPropTypes.bool
};
/**
 * @class UiAndDataLanguageInfoView - Use for Display Data Language info view.
 * @memberOf Views
 * @property {bool} [isDialogOpen] - Pass true value for open Data language view dialog open and false for close the Dialog box.
 * @property {string} [selectedDataLanguageId] - Pass ID of currently selected Data language.
 * @property {string} [selectedUILanguageId] - Pass ID of currently selected UI language.
 * @property {array} [dataLanguages] - Pass Different Data languages Name.
 * @property {array} [uiLanguages] - Pass Different UI languages name.
 * @property {bool} [isShowUILanguage] - Pass default true boolean to show UI language in View.
 * @property {bool} [isShowDataLanguage] - Pass default true boolean to show Data language in View.
 */

class UiAndDataLanguageInfoView extends React.Component {
  static propTypes = oPropTypes;
  static isChangeFromUI = false;

  constructor (props) {
    super(props);

    this.state = {
      searchText: "",
      dataLanguages: this.props.dataLanguages,
      uiLanguages: this.props.uiLanguages,
      selectedUILanguageId: this.props.selectedUILanguageId,
      selectedDataLanguageId: this.props.selectedDataLanguageId
    }
  }

  static getDerivedStateFromProps (oNextProps, oNextState) {
    let oUpdatedData = UiAndDataLanguageInfoView.isChangeFromUI ? oNextState : oNextProps;
    return {
      dataLanguages: oUpdatedData.dataLanguages,
      uiLanguages: oUpdatedData.uiLanguages,
      selectedUILanguageId: oUpdatedData.selectedUILanguageId,
      selectedDataLanguageId: oUpdatedData.selectedDataLanguageId,
      searchText: oUpdatedData.searchText || ""
    }
  }

  /*componentWillReceiveProps (nextProps) {
    this.setState({
      dataLanguages: nextProps.dataLanguages,
      uiLanguages: nextProps.uiLanguages,
      selectedUILanguageId: nextProps.selectedUILanguageId,
      selectedDataLanguageId: nextProps.selectedDataLanguageId,
      searchText: "",
    });
  };*/

  handleDialogButtonClicked = (sButtonId) => {
    let sSelectedUILanguageId = "";
    let sSelectedDataLanguageId = "";

    switch (sButtonId) {
      case 'ok' :
        sSelectedUILanguageId = (this.props.selectedUILanguageId !== this.state.selectedUILanguageId) && this.state.selectedUILanguageId || "";
        sSelectedDataLanguageId = (this.props.selectedDataLanguageId !== this.state.selectedDataLanguageId) && this.state.selectedDataLanguageId || "";
        break;
    }

    UiAndDataLanguageInfoView.isChangeFromUI = false;
    EventBus.dispatch(oEvents.UI_AND_DATA_LANGUAGE_INFO_VIEW_HANDLE_LANGUAGE_CHANGED, sSelectedUILanguageId, sSelectedDataLanguageId);
  };

  handleShowDialogButtonClicked = () => {
    EventBus.dispatch(oEvents.UI_AND_DATA_LANGUAGE_INFO_VIEW_GET_LANGUAGE_INFO);
  };

  handleSearchOperation = (e) => {
    // console.log(e.target.value);
    let aUILanguageSearchList = [];
    let aDataLanguageSearchList = [];
    let sValue = e.target.value;
    let sSearchText = sValue.toLocaleLowerCase();
    if (!CS.isEmpty(sSearchText)) {
      CS.forEach(this.props.uiLanguages, function (item) {
        let sUILanguageLabel = CS.getLabelOrCode(item);
        if (sUILanguageLabel.toLocaleLowerCase().indexOf(sSearchText) != -1) {
          aUILanguageSearchList.push(item);
        }
      });

      CS.forEach(this.props.dataLanguages, function (item) {
        let sDataLanguageLabel = CS.getLabelOrCode(item);
        if (sDataLanguageLabel.toLocaleLowerCase().indexOf(sSearchText) != -1) {
          aDataLanguageSearchList.push(item);
        }
      });

      this.setState({
        searchText: sValue,
        uiLanguages: aUILanguageSearchList,
        dataLanguages: aDataLanguageSearchList
      })
      UiAndDataLanguageInfoView.isChangeFromUI = true;
    } else {
      this.setState({
        searchText: "",
        uiLanguages: this.props.uiLanguages,
        dataLanguages: this.props.dataLanguages

      })
    }
  };


  clearSearchInput = () => {
    this.setState({
      searchText: "",
      dataLanguages: this.props.dataLanguages,
      uiLanguages: this.props.uiLanguages,
    })
  };

  handleItemClicked = (sContext, sSelectedId) => {
    switch (sContext) {
      case "uiLanguage":
        this.setState({
          selectedUILanguageId: sSelectedId
        });
        break;
      case "dataLanguage":
        this.setState({
          selectedDataLanguageId: sSelectedId
        });
        break;
    }
    UiAndDataLanguageInfoView.isChangeFromUI = true
  };

  getLanguageInfoView = () => {
    let sSelectedDataLanguageId = this.state.selectedDataLanguageId;
    let sSelectedUILanguageId = this.state.selectedUILanguageId;
    let oSelectedDataLanguage = CS.find(this.props.dataLanguages, {code: sSelectedDataLanguageId});
    let oSelectedUILanguage = CS.find(this.props.uiLanguages, {code: sSelectedUILanguageId});
    let oUILanguageDOM = null;
    let oDataLanguageDOM = null;

    if (this.props.isShowUILanguage) {
      let oUILanguageIconDom = null;
      if (oSelectedUILanguage.iconKey) {
        let sUILanguageIcon = ViewUtils.getIconUrl(oSelectedUILanguage.iconKey);
        oUILanguageIconDom = (
            <div className="selectedItemIcon">
              <ImageFitToContainerView imageSrc={sUILanguageIcon}/>
            </div>
        );
      } else {
        let sLabel = oSelectedUILanguage.label ? oSelectedUILanguage.label.substring(0, 2) : oSelectedUILanguage.code.substring(0, 2);
        oUILanguageIconDom = (
            <div className="selectedItemIcon">
              <div className="selectedLanguageLabel">
                {sLabel}
              </div>
            </div>
        );
      }
      let sUILanguageLabel = getTranslation().UI_LANGUAGE + ": " + CS.getLabelOrCode(oSelectedUILanguage);
      oUILanguageDOM = (
          <div className="uiLanguageWrapper">
            <TooltipView label={sUILanguageLabel}>
              <div className="uiLanguageButton">
                <div className="uiLanguageImageWrapper">
                  <div className="uiLanguageImage">

                  </div>
                </div>
                {oUILanguageIconDom}
              </div>
            </TooltipView>
          </div>
      );
    }

    if (this.props.isShowDataLanguage) {
      let oDataLanguageIconDom = null;
      if (oSelectedDataLanguage.iconKey) {
        let sDataLanguageIcon = ViewUtils.getIconUrl(oSelectedDataLanguage.iconKey);
        oDataLanguageIconDom = (
            <div className="selectedItemIcon">
              <ImageFitToContainerView imageSrc={sDataLanguageIcon}/>
            </div>
        );
      } else {
        let sLabel = oSelectedDataLanguage.label ? oSelectedDataLanguage.label.substring(0, 2) : oSelectedDataLanguage.code.substring(0, 2);
        oDataLanguageIconDom = (
            <div className="selectedItemIcon">
              <div className="selectedLanguageLabel">
                {sLabel}
              </div>
            </div>
        );
      }

      let sUILanguageLabel = getTranslation().DATA_LANGUAGE + ": " + CS.getLabelOrCode(oSelectedDataLanguage);

      oDataLanguageDOM = (
          <div className="dataLanguageWrapper">
            <TooltipView label={sUILanguageLabel}>
              <div className="dataLanguageButton">
                <div className="dataLanguageImageWrapper">
                  <div className="dataLanguageImage">

                  </div>
                </div>
                {oDataLanguageIconDom}
              </div>
            </TooltipView>
          </div>
      );
    }

    return (
        <div className="uiAndDataLanguageInfoContainer" onClick={this.handleShowDialogButtonClicked}>
          {oUILanguageDOM}
          {oDataLanguageDOM}
        </div>
    );
  };

  getSearchBarDOM = () => {
    let oCrossIconDOM = null;
    if (!CS.isEmpty(this.state.searchText)) {
      oCrossIconDOM = (
          <TooltipView label={getTranslation().CLEAR} key="clear">
            <div className="crossIcon" onClick={this.clearSearchInput}></div>
          </TooltipView>
      );
    }
    return (
        <div className="searchInputFieldBar">
          <div className="searchIcon"></div>
          <input className="searchInput" value={this.state.searchText} placeholder={getTranslation().SEARCH}
                 onChange={this.handleSearchOperation}></input>
          {oCrossIconDOM}
        </div>
    );
  };

  getUILanguageItems = () => {
    let __this = this;
    let aUILanguages = this.state.uiLanguages;
    let sSelectedUILanguageId = this.state.selectedUILanguageId;
    let aUILanguageItemsView = [];
    CS.forEach(aUILanguages, function (oUILanguage) {

      let sUILanguageClass = "languageItemWrapper";
      if (sSelectedUILanguageId === oUILanguage.code) {
        sUILanguageClass += " selected";
      }
      let sTitle = oUILanguage.code ? oUILanguage.label + ' (' + oUILanguage.code + ')' : oUILanguage.label;
      let sLabel= CS.getLabelOrCode(oUILanguage);
      let oIconDom = null;
      if (oUILanguage.iconKey) {
        let sUILanguageIcon = ViewUtils.getIconUrl(oUILanguage.iconKey);
        oIconDom = (
            <div className="selectedItemIcon">
              <ImageFitToContainerView imageSrc={sUILanguageIcon}/>
            </div>
        );
      } else {
        oIconDom = (
            <div className="defaultIcon">
            </div>
        );
      }
      aUILanguageItemsView.push(
          <div className={sUILanguageClass} key={oUILanguage.id}
               onClick={__this.handleItemClicked.bind(__this, "uiLanguage", oUILanguage.code)}>
            <TooltipView label={sTitle}>
              <div className="languageItem">
                {oIconDom}
                <div className="languageData">{sLabel}</div>
              </div>
            </TooltipView>
          </div>
      );
    });
    if (CS.isEmpty(aUILanguageItemsView)) {
      aUILanguageItemsView.push(<NothingFoundView message={getTranslation().NOTHING_FOUND} key={"NOTHING_FOUND"}/>)
    }
    return (aUILanguageItemsView);
  };

  getDataLanguageItems = () => {
    let __this = this;
    let aDataLanguages = this.state.dataLanguages;
    let sSelectedDataLanguageId = this.state.selectedDataLanguageId;
    let aDataLanguageItemsView = [];
    CS.forEach(aDataLanguages, function (oDataLanguage) {

      let sDataLanguageClass = "languageItemWrapper";
      if (sSelectedDataLanguageId === oDataLanguage.code) {
        sDataLanguageClass += " selected";
      }
      let sTitle = oDataLanguage.code ? oDataLanguage.label + ' (' + oDataLanguage.code + ')' : oDataLanguage.label;
      let sLabel= CS.getLabelOrCode(oDataLanguage);
      let oIconDom = null;
      if (oDataLanguage.iconKey) {
        let sDataLanguageIcon = ViewUtils.getIconUrl(oDataLanguage.iconKey);
        oIconDom = (
            <div className="selectedItemIcon">
              <ImageFitToContainerView imageSrc={sDataLanguageIcon}/>
            </div>
        );
      } else {
        oIconDom = (
            <div className="defaultIcon">
            </div>
        );
      }
      aDataLanguageItemsView.push(
          <div className={sDataLanguageClass}
               key={oDataLanguage.id}
               onClick={__this.handleItemClicked.bind(__this, "dataLanguage", oDataLanguage.code)}>
            <TooltipView label={sTitle}>
              <div className="languageItem">
                {oIconDom}
                <div className="languageData">{sLabel}</div>
              </div>
            </TooltipView>
          </div>
      );
    });

    if (CS.isEmpty(aDataLanguageItemsView)) {
      aDataLanguageItemsView.push(<NothingFoundView message={getTranslation().NOTHING_FOUND} key={"NOTHING_FOUND"}/>)
    }
    return (aDataLanguageItemsView);
  };

  getUILanguageSelectionDOM = () => {
    if (this.props.isShowUILanguage) {
      let sUILanguageListClassName = "UILanguageList";
      sUILanguageListClassName += this.props.isShowDataLanguage === false ? " languageListItems" : "";
      return (
          <div className={sUILanguageListClassName}>
            <div className="languageHeader">
              {getTranslation().UI_LANGUAGE}
            </div>
            <div className="languageItemsContainer">
              {this.getUILanguageItems()}
            </div>
          </div>
      );
    }
    return null;
  };

  getDataLanguageSelectionDOM = () => {
    if (this.props.isShowDataLanguage) {
      let sDataLanguageListClassName = "DataLanguageList";
      sDataLanguageListClassName += this.props.isShowUILanguage === false ? " languageListItems" : "";
      return (
          <div className={sDataLanguageListClassName}>
            <div className="languageHeader">
              {getTranslation().DATA_LANGUAGE}
            </div>
            <div className="languageItemsContainer">
              {this.getDataLanguageItems()}
            </div>
          </div>
      );
    }
    return null;
  };

  getUIAndDataLanguageSelectionView = () => {

    return (
        <div className="uiAndDataLanguageSelectionView">
          {this.getSearchBarDOM()}
          <div className="languageList">
            {this.getUILanguageSelectionDOM()}
            {this.getDataLanguageSelectionDOM()}
          </div>
        </div>
    );

  };

  render () {
    let oLanguageInfoView = this.getLanguageInfoView();

    let oBodyStyle = {
      maxWidth: "1200px",
      minWidth: "800px",
    };

    let aButtonData = [
      {
        id: "cancel",
        label: getTranslation().CANCEL,
        isDisabled: false,
        isFlat: false,
      },
      {
        id: "ok",
        label: getTranslation().OK,
        isFlat: false,
        isDisabled: false
      }];

    let sTitle = getTranslation().LANGUAGES;
    let bIsDialogOpen = this.props.isDialogOpen;
    let fButtonHandler = this.handleDialogButtonClicked;

    return (
        <div className='uiAndDataLanguageView'>
          {oLanguageInfoView}
          <CustomDialogView modal={true}
                            open={bIsDialogOpen}
                            title={sTitle}
                            bodyStyle={oBodyStyle}
                            contentStyle={oBodyStyle}
                            buttonData={aButtonData}
                            onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
                            buttonClickHandler={fButtonHandler}>
            {this.getUIAndDataLanguageSelectionView()}
          </CustomDialogView>
        </div>
    );
  }

}

export const view = React.getSafeComponent(UiAndDataLanguageInfoView);
export const events = oEvents;
