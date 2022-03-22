import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as ClassConfigBasicInformationView } from './class-config-basic-information-view';
import { view as ClassConfigSectionsView } from './class-config-sections-view';
import { view as CustomDialogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import { view as CommonConfigSectionView } from '../../../../../viewlibraries/commonconfigsectionview/common-config-section-view';
import { view as CusromMaterialButtonView } from '../../../../../viewlibraries/custommaterialbuttonview/custom-material-button-view';
import { view as HorizontalTreeView } from '../../../../../viewlibraries/horizontaltreeview/horizontal-tree-view';
import { view as ConfigHeaderView } from '../../../../../viewlibraries/configheaderview/config-header-view';
import SystemLevelIdDictionary from '../../../../../commonmodule/tack/system-level-id-dictionary';
import NatureTypeDictionary from '../../../../../commonmodule/tack/nature-type-dictionary.js';
import CreateDialogLayoutData from '../tack/mock/create-dialog-layout-data';
import ViewUtils from './utils/view-utils';
import {view as AssetUploadConfigurationView} from  '../view/asset-upload-configuration-view';
import {view as ManageEntityDialogView} from "./entity-usage-dialog-view";
import ManageEntityConfigProps from "../store/model/manage-entity-config-props";
import ExportSide2RelationshipNatureTypeDictionary from '../../../../../commonmodule/tack/export-relationship-side-nature-type-dictionary';
var DefaultNatureType = NatureTypeDictionary.SINGLE_ARTICLE;

const oEvents = {
  CLASS_CONFIG_CREATE_DIALOG_BUTTON_CLICKED: "class_config_create_dialog_button_clicked",
  CLASS_SAVE_DIALOG_CLOSE: "class_save_dialog_close",
  CLASS_CONFIG_IMPORT_BUTTON_CLICKED: "class_config_import_button_clicked"
};

const oPropTypes = {
  actionItemModel: ReactPropTypes.array,
  classTreeModel: ReactPropTypes.array,
  availableAttributeListModel: ReactPropTypes.array,
  contextMenuList: ReactPropTypes.object,
  isAttributeTableVisible: ReactPropTypes.bool.isRequired,
  activeClass: ReactPropTypes.object,
  sectionLayoutModel: ReactPropTypes.object,
  disabledFields: ReactPropTypes.array,
  masterEntitiesForSection: ReactPropTypes.object,
  relationshipModel: ReactPropTypes.object,
  propertyCollectionModels: ReactPropTypes.array,
  dragDetails: ReactPropTypes.object,
  referencedContexts: ReactPropTypes.object,
  contentHierarchyData: ReactPropTypes.object,
  isActiveClassDirty: ReactPropTypes.bool,
  commonConfigSectionViewModel: ReactPropTypes.object,
  assetUploadConfigurationModel: ReactPropTypes.object
};

// @CS.SafeComponent
class ClassConfigViewClone extends React.Component {

  static propTypes = oPropTypes;

  constructor (props) {
    super(props)
    this.state = {
      isNature: false,
      natureType: DefaultNatureType,
      classLabel: "",
      id: props.activeClass.id
    };
  }

  static getDerivedStateFromProps (oNextProps, oState) {
    if (oNextProps.activeClass.id !== oState.id) {
      let oActiveClass = oNextProps.activeClass;
      if (!CS.isEmpty(oActiveClass) && oActiveClass.isCreated) {
        return ({
          id: oActiveClass.id,
          isNature: oActiveClass.isNature,
          classLabel: oActiveClass.label
        });
      }
      return {
        id: oActiveClass.id
      }
    }
    return null;
  }

  getHierarchyTreeView = () =>  {
    return <HorizontalTreeView contentHierarchyData={this.props.contentHierarchyData}/>;
  }

  handleClassSaveDialogClose = (sId) => {
    EventBus.dispatch(oEvents.CLASS_SAVE_DIALOG_CLOSE, sId);
  }

  handleFilesSelectedToUpload = (aFiles) => {
    EventBus.dispatch(oEvents.CLASS_CONFIG_IMPORT_BUTTON_CLICKED, aFiles);
  };

  getClassDetailedView = () => {
    var __props = this.props;
    var oActiveClass = __props.activeClass;
    var aScrollLinkedItems = [];
    let oExportPropertiesSection = {
      side2RelationshipData: __props.side2RelationshipData,
      side2CollapseEnabled: __props.side2CollapseEnabled
    };

    aScrollLinkedItems.push(<div className="classConfigLinkScrollElementWrapper" key="classConfigLinkScrollElementWrapper">
          <ClassConfigBasicInformationView
              sectionLayoutModel={__props.sectionLayoutModel}
              disabledFields={__props.disabledFields}
          />
        </div>);
    var aDisabledFields = ["code"];

    let aNativeAssetClasses = ["image_asset" , "document_asset", "video_asset"];

    if(CS.includes(aNativeAssetClasses, oActiveClass.id)){
      aScrollLinkedItems.push(<AssetUploadConfigurationView
          {...__props.assetUploadConfigurationModel}
      />)
    }

    /** To enable export Side2 Relationship in Class Config **/
    let aExportSide2RelationshipNatureTypeDictionary = new ExportSide2RelationshipNatureTypeDictionary().natureTypeDictionary;
    let bShowExportSide2RelationshipView = CS.includes(aExportSide2RelationshipNatureTypeDictionary, oActiveClass.natureType);

    if (oActiveClass.id !== SystemLevelIdDictionary.MarkerKlassId && oActiveClass.id !== SystemLevelIdDictionary.GoldenArticleKlassId) {
      aScrollLinkedItems.push(
          <ClassConfigSectionsView
              key="ClassConfigSectionsView"
              sections={oActiveClass.sections}
              exportPropertiesSection={oExportPropertiesSection}
              propertyCollectionModels={__props.availableAttributeListModel}
              dragDetails={__props.dragDetails}
              masterEntitiesForSection={__props.masterEntitiesForSection}
              referencedContexts={__props.referencedContexts}
              disabledFields={aDisabledFields}
              showExportSide2RelationshipView={bShowExportSide2RelationshipView}
          />
      );
    }
    return aScrollLinkedItems;
  };

  handleDialogButtonClicked = (sContext) => {
    let oState = this.state;
    var bIsNature = oState.isNature;
    let sLabel = oState.classLabel;
    let sNatureType = oState.natureType;

    EventBus.dispatch(oEvents.CLASS_CONFIG_CREATE_DIALOG_BUTTON_CLICKED, sContext, sLabel, bIsNature, sNatureType);
  };

  getIsNatureDialog = (oActiveClass) => {
    if (!oActiveClass || !oActiveClass.isCreated /*|| oActiveClass.type != [oClassTypeDictionary["2"]]*/) {
      return null;
    }
    let oModel = this.props.commonConfigSectionViewModel;

    var oBodyStyle = {
      padding: '0 10px 20px 10px'
    };

    var sErrorText = "";
    var bIsDisableCreate = true;

    if (CS.isEmpty(oActiveClass.code.trim())) {
      sErrorText = getTranslation().CODE_SHOULD_NOT_BE_EMPTY;
    } else if (!ViewUtils.isValidEntityCode(oActiveClass.code)) {
      sErrorText = getTranslation().PLEASE_ENTER_VALID_CODE;
    } else {
      bIsDisableCreate = false;
    }

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
    let fButtonHandler = this.handleDialogButtonClicked;
    let oCreateDialogLayoutData = new CreateDialogLayoutData();
    return (<CustomDialogView modal={true} open={true}
                              title={getTranslation().CREATE}
                              bodyStyle={oBodyStyle}
                              bodyClassName=""
                              contentClassName="createClassModalDialog"
                              buttonData={aButtonData}
                              onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
                              buttonClickHandler={fButtonHandler}>
      <CommonConfigSectionView context={"class"} sectionLayout={oCreateDialogLayoutData} data={oModel} errorTextForCodeEntity={sErrorText}/>
    </CustomDialogView>);
  };

  getSaveBarButtons = () => {
    let oButtonLabelStyle = {
      fontSize: 12,
    };

    return (
        <div className={"saveBarButtonsWrapper"}>
          <CusromMaterialButtonView
              isRaisedButton={true}
              label={getTranslation().DISCARD}
              onButtonClick={this.handleClassSaveDialogClose.bind(this, "discard")}
          />
          <CusromMaterialButtonView
              isRaisedButton={true}
              label={getTranslation().SAVE}
              labelStyle={oButtonLabelStyle}
              keyboardFocused={true}
              onButtonClick={this.handleClassSaveDialogClose.bind(this, "save")}
          />
        </div>
    );
  }

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
    let aClassListActionGroup = this.props.actionItemModel;
    var oClassDetailedView = this.props.isAttributeTableVisible ? this.getClassDetailedView() : '';
    var oActiveClass = this.props.activeClass;
    var oIsNatureDialog = this.getIsNatureDialog(oActiveClass);

    let sClassDetailedViewWrapper = "classConfigDetailViewWrapper";
    let sSaveBarClass = "saveBar";

    if (this.props.isActiveClassDirty) {
      sSaveBarClass += " dirty";
      sClassDetailedViewWrapper += " dirty"
    }

    let sClassConfigViewContainerClassName = "classConfigViewContainer ";
    let oHeaderView = null;
    if (!CS.isEmpty(aClassListActionGroup)) {
      oHeaderView = (<ConfigHeaderView actionButtonList={aClassListActionGroup}
                                       showSaveDiscardButtons={false}
                                       hideSearchBar={true}
                                       context={"class"}
                                       filesUploadHandler={this.handleFilesSelectedToUpload}
      />);
      sClassConfigViewContainerClassName += "withHeader"
    }

    let bIsEntityDialogOpen = ManageEntityConfigProps.getIsEntityDialogOpen();
    let oManageEntityDialog = bIsEntityDialogOpen ? this.getEntityDialog() : null;

    return (
        <div className="classConfigViewContainerWrapper">
          {oHeaderView}
          {oManageEntityDialog}
          <div className={sClassConfigViewContainerClassName}>
            <div className="classListViewContainer">
              {this.getHierarchyTreeView()}
            </div>
            <div className="classDetailedViewContainer">
              <div className={sClassDetailedViewWrapper}>
              {oClassDetailedView}
              </div>
              <div className={sSaveBarClass}>{this.getSaveBarButtons()}</div>
              {oIsNatureDialog}
            </div>
          </div>
        </div>
    );
  }
}

export const view = ClassConfigViewClone;
export const events = oEvents;
