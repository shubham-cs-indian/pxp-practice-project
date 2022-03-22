import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { view as HorizontalTreeView } from '../../../../../viewlibraries/horizontaltreeview/horizontal-tree-view';
import { view as CustomDialogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import { view as CommonConfigSectionView } from '../../../../../viewlibraries/commonconfigsectionview/common-config-section-view';
import CreateLanguageLayoutData from '../tack/mock/create-language-tree-layout-data-for-dialog';
import LanguageLayoutData from '../tack/mock/language-tree-layout-data';
import { view as CustomMaterialButton } from '../../../../../viewlibraries/custommaterialbuttonview/custom-material-button-view';
import ViewUtils from '../view/utils/view-utils';
import { view as ConfigHeaderView } from '../../../../../viewlibraries/configheaderview/config-header-view';
import ManageEntityConfigProps from "../store/model/manage-entity-config-props";
import {view as ManageEntityDialogView} from "./entity-usage-dialog-view";

const oEvents = {
  HANDLE_LANGUAGE_TREE_DIALOG_CANCEL_CLICKED: "handle_language_tree_dialog_cancel_clicked",
  HANDLE_LANGUAGE_TREE_DIALOG_CREATE_CLICKED: "handle_language_tree_dialog_create_clicked",
  HANDLE_LANGUAGE_TREE_SNACK_BAR_BUTTON_CLICKED: "handle_language_tree_snack_bar_button_clicked",
  LANGUAGE_TREE_CONFIG_IMPORT_BUTTON_CLICKED: "language_tree_config_import_button_clicked"
};

class LanguageTreeConfigView extends React.Component {
  static propTypes = {
    contentHierarchyData: ReactPropTypes.object,
    commonConfigSectionViewModel: ReactPropTypes.object,
    activeLanguage: ReactPropTypes.object,
    commonConfigLanguageViewModel: ReactPropTypes.object,
    isLanguageDirty: ReactPropTypes.bool
  };

  handleDialogButtonClicked = (sButtonId) => {

    if(sButtonId === "create"){
      EventBus.dispatch(oEvents.HANDLE_LANGUAGE_TREE_DIALOG_CREATE_CLICKED);
    } else{
      EventBus.dispatch(oEvents.HANDLE_LANGUAGE_TREE_DIALOG_CANCEL_CLICKED);
    }
  };

  getHorizantalTreeView = () => {
    return <HorizontalTreeView contentHierarchyData={this.props.contentHierarchyData}/>;
  };

  handleLanguageTreeDialogClose = (sButton) =>{
    EventBus.dispatch(oEvents.HANDLE_LANGUAGE_TREE_SNACK_BAR_BUTTON_CLICKED, sButton);
  };

  handleFilesSelectedToUpload = (aFiles) => {
    EventBus.dispatch(oEvents.LANGUAGE_TREE_CONFIG_IMPORT_BUTTON_CLICKED, aFiles);
  };

  getCreateLanguageDialog = (oActiveLanguage) => {
    if (!oActiveLanguage || !oActiveLanguage.isNewlyCreated) {
      return null;
    }
    let oModel = this.props.commonConfigSectionViewModel;

    var oBodyStyle = {
      padding: '0 10px 20px 10px'
    };

    var bIsDisableCreate = true;

    var aButtonData = [
      {
        id: "cancel",
        label: getTranslation().CANCEL,
        isDisabled: false,
        isFlat: true,
      },
      {
        id: "create",
        label: getTranslation().CREATE,
        isDisabled: bIsDisableCreate,
        isFlat: false,
      }
    ];
    let fButtonHandler = this.handleDialogButtonClicked
    let oCreateDialogLayoutData = new CreateLanguageLayoutData();
    return (<CustomDialogView modal={true} open={true}
                              title={getTranslation().CREATE}
                              bodyStyle={oBodyStyle}
                              bodyClassName=""
                              contentClassName="createClassModalDialog"
                              buttonData={aButtonData}
                              onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
                              buttonClickHandler={fButtonHandler}>
      <CommonConfigSectionView context={"languageTree"} sectionLayout={oCreateDialogLayoutData} data={oModel}  disabledFields = {["code"]}/>
    </CustomDialogView>);
  };

  getSaveBarButtons = () => {
    return (
        <div className={"saveBarButtonsWrapper"}>
          <CustomMaterialButton
              label={getTranslation().DISCARD}
              labelColor="#FFF"
              backgroundColor="#555"
              isRaisedButton={true}
              onButtonClick={this.handleLanguageTreeDialogClose.bind(this, "discard")}
          />
          <CustomMaterialButton
              label={getTranslation().SAVE}
              labelColor="#FFF"
              backgroundColor="#555"
              keyboardFocused={true}
              isRaisedButton={true}
              onButtonClick={this.handleLanguageTreeDialogClose.bind(this, "save")}
          />
        </div>
    );
  };

  getEntityDialog = () => {
    let oEntityDatList = ManageEntityConfigProps.getDataForDialog();
    let bIsDelete = ManageEntityConfigProps.getIsDelete();
    return (
        <ManageEntityDialogView
            oEntityDatList = {oEntityDatList}
            bIsDelete={bIsDelete}
        />
    );
  };


  render() {
    let oActiveLanguge = this.props.activeLanguage;
    let oModel = this.props.commonConfigLanguageViewModel;
    var sErrorText = "";

    let sCommonConfigSectionViewContainer = "commonConfigSectionViewContainer";
    if (this.props.isLanguageDirty) {
      sCommonConfigSectionViewContainer += " dirty"
    }

    let oLanguageLayoutData = new LanguageLayoutData();
    let aDisabledFields = ["code"];
    let oCommonConfigSectionView = !CS.isEmpty(oModel)?
        (<CommonConfigSectionView context={"languageTree"} sectionLayout={oLanguageLayoutData} data={oModel} disabledFields={aDisabledFields} errorTextForCodeEntity={sErrorText}/>) : null
    let snackBarView = this.props.isLanguageDirty? (<div className="snackBar">{this.getSaveBarButtons()}</div>): null;


    let aLanguageTreeActionGroup = this.props.actionItemModel;
    let sLanguageTreeViewContainerClassName = "languageTaxonomyConfigContainer ";
    let oHeaderView = null;
    if (!CS.isEmpty(aLanguageTreeActionGroup)) {
      oHeaderView = (<ConfigHeaderView actionButtonList={aLanguageTreeActionGroup}
                                       showSaveDiscardButtons={false}
                                       hideSearchBar={true}
                                       context={"class"}
                                       filesUploadHandler={this.handleFilesSelectedToUpload}
      />);
      sLanguageTreeViewContainerClassName += "withHeader"
    }

    let bIsEntityDialogOpen = ManageEntityConfigProps.getIsEntityDialogOpen();
    let oManageEntityDialog = bIsEntityDialogOpen ? this.getEntityDialog() : null;

    return (
        <div className={"languageTaxonomyConfigContainerWrapper"}>
          {oHeaderView}
          {oManageEntityDialog}
          <div className={sLanguageTreeViewContainerClassName}>

            <div className={"languageListContainer"}>
              {this.getHorizantalTreeView()}
            </div>
            <div className="languagesDetailViewContainer">
              <div className={sCommonConfigSectionViewContainer}>
                {oCommonConfigSectionView}
              </div>
              {snackBarView}
            </div>
            {this.getCreateLanguageDialog(oActiveLanguge)}
          </div>
        </div>
    );
  }
}

export const view = React.getSafeComponent(LanguageTreeConfigView);
export const events = oEvents;
