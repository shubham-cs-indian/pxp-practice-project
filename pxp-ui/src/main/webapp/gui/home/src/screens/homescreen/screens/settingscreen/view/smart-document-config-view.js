import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import CreateSmartDocumentLayoutData from '../tack/mock/create-smart-document-layout-data';
import SmartDocumentLayoutData from '../tack/mock/smart-document-layout-data';
import { view as HorizontalTreeView } from '../../../../../viewlibraries/horizontaltreeview/horizontal-tree-view';
import { view as CustomDialogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import { view as CommonConfigSectionView } from '../../../../../viewlibraries/commonconfigsectionview/common-config-section-view';
import { view as CustomMaterialButton } from '../../../../../viewlibraries/custommaterialbuttonview/custom-material-button-view';

const oEvents = {
  HANDLE_SMART_DOCUMENT_DAILOG_BUTTON_CLICKED: "handle_smart_document_dailog_create_clicked",
  HANDLE_SMART_DOCUMENT_SNACK_BAR_BUTTON_CLICKED: "handle_smart_document_snack_bar_button_clicked",
  HANDLE_SMART_DOCUMENT_PRESET_DROP_DOWN_CLICKED: "handle_smart_document_preset_drop_down_clicked",
};

const oPropTypes = {
  isSectionDirty: ReactPropTypes.bool,
  context: ReactPropTypes.string,
  activeSection: ReactPropTypes.object,
  sectionViewModel: ReactPropTypes.object,
  createViewModel: ReactPropTypes.object,
  hierarchyData: ReactPropTypes.object,
  showSmartDocumentIcon: ReactPropTypes.bool,
};


// @CS.SafeComponent
class SmartDocumentConfigView extends React.Component {

  constructor (props) {
    super(props);
  }

  handleDialogButtonClicked = (sButtonId) => {
    EventBus.dispatch(oEvents.HANDLE_SMART_DOCUMENT_DAILOG_BUTTON_CLICKED, sButtonId);
  };

  handleSmartDocumentSnackBarClicked = (sButtonId, oEvent) => {
    EventBus.dispatch(oEvents.HANDLE_SMART_DOCUMENT_SNACK_BAR_BUTTON_CLICKED, sButtonId);
    oEvent.stopPropagation();
  };

  getSnackBarView = () => {
    return (
        <div className={"saveBarButtonsWrapper"}>
          <CustomMaterialButton
              label={getTranslation().DISCARD}
              labelColor="#FFF"
              backgroundColor="#555"
              isRaisedButton={true}
              onButtonClick={this.handleSmartDocumentSnackBarClicked.bind(this, "discard")}
          />
          <CustomMaterialButton
              label={getTranslation().SAVE}
              labelColor="#FFF"
              backgroundColor="#555"
              keyboardFocused={true}
              isRaisedButton={true}
              onButtonClick={this.handleSmartDocumentSnackBarClicked.bind(this, "save")}
          />
        </div>
    );
  };

  getCreateDialogView = (oCreateSection, oModel) => {
    if (oCreateSection && oCreateSection.isNewlyCreated) {

      var oBodyStyle = {
        padding: '0 10px 20px 10px'
      };

      let sErrorText = "";
      if (CS.isEmpty(CS.trim(oCreateSection.code))) {
        sErrorText = getTranslation().CODE_SHOULD_NOT_BE_EMPTY;
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
          isDisabled: false,
          isFlat: false,
        }
      ];

      let oCreateSmartDocumentLayoutData = new CreateSmartDocumentLayoutData();
      return (<CustomDialogView modal={true} open={true}
                                title={getTranslation().CREATE}
                                bodyStyle={oBodyStyle}
                                bodyClassName=""
                                contentClassName="createClassModalDialog"
                                buttonData={aButtonData}
                                buttonClickHandler={this.handleDialogButtonClicked.bind(oCreateSection.type)}>
        <CommonConfigSectionView context={"smartDocument"} sectionLayout={oCreateSmartDocumentLayoutData}
                                 data={oModel}
                                 errorTextForCodeEntity={sErrorText}/>
      </CustomDialogView>);
    }
    return null;
  };

  getPDFConfigurationSectionView = (oSmartDocumentLayoutData, oModel) => {
    return (<CommonConfigSectionView
        context={"smartDocument"}
        sectionLayout={oSmartDocumentLayoutData['smartDocumentPresetPdfConfiguration']}
        data={oModel.smartDocumentPresetPdfConfiguration}/>)
  }

  getMSSModel = (
      aSelectedItems,
      aItems,
      sContext,
      sInnerContext,
      bIsMultiSelect,
      bCannotRemove,
      fOnPopOverOpen,
      fOnApply
  ) => {

    sInnerContext = sInnerContext || "";
    let sMSSContext = sInnerContext;
    return {
      items: aItems,
      selectedItems: aSelectedItems,
      context: sMSSContext,
      isMultiSelect: bIsMultiSelect || false,
      cannotRemove: bCannotRemove || false,
      onPopOverOpen: fOnPopOverOpen,
      onApply: fOnApply
    }
  };

  handleMSSValueChanged = (sKey, aSelectedItems, oReferencedData) => {
    EventBus.dispatch(oEvents.HANDLE_SMART_DOCUMENT_PRESET_DROP_DOWN_CLICKED, sKey, aSelectedItems, oReferencedData)
  }

  getAttributesMSS = (aAttributes, oReferencedAttributes) => {
    let oMSSProcessRequestResponseObj = {
      requestType: "configData",
      entityName: "attributes",
      types: [],
      typesToExclude: [],
    };
    let aAttributesList = [];
    let aSelectedEntityIds = aAttributes;
    let fOnApply = this.handleMSSValueChanged.bind(this, "attributes");
    let oMSSModel = this.getMSSModel(aSelectedEntityIds, aAttributesList, "selectionConfigurations", "attributes", true, false, null, fOnApply);
    oMSSModel.requestResponseInfo = oMSSProcessRequestResponseObj;
    oMSSModel.referencedData = oReferencedAttributes;
    return oMSSModel;
  }

  getTagsMSS = (aTags, oReferencedTags) => {
    let oMSSProcessRequestResponseObj = {
      requestType: "configData",
      entityName: "tags",
      types: []
    };
    let aTagListForMSS = [];
    let aSelectedEntityIds = aTags;
    let fOnApply = this.handleMSSValueChanged.bind(this, "tags");
    let oMSSModel = this.getMSSModel(aSelectedEntityIds, aTagListForMSS, "selectionConfigurations", "tags", true, false, null, fOnApply);
    oMSSModel.requestResponseInfo = oMSSProcessRequestResponseObj;
    oMSSModel.referencedData = oReferencedTags;
    return oMSSModel;
  }

  getSelectionConfigurationView = (oSmartDocumentLayoutData, oModel, oReferencedData) => {
    let oAttributesMSS = this.getAttributesMSS(oModel.attributes, oReferencedData.referencedAttributes);
    let oTagsMSS = this.getTagsMSS(oModel.tags, oReferencedData.referencedTags);

    oModel.attributes = oAttributesMSS;
    oModel.tags = oTagsMSS;
    return (<CommonConfigSectionView
        context={"smartDocument"}
        sectionLayout={oSmartDocumentLayoutData['smartDocumentPresetSelectionConfiguration']}
        data={oModel}/>)
  }

  getFilterConfigurationSectionView = (oSmartDocumentLayoutData, oModel) => {
    return (<CommonConfigSectionView
        context={"smartDocument"}
        sectionLayout={oSmartDocumentLayoutData['smartDocumentPresetFilterConfiguration']}
        data={oModel.smartDocumentPresetFilterConfiguration}/>)
  };

  getCommonConfigSectionView = (oActiveSection, oSmartDocumentLayoutData, oModel, oReferencedData) => {
    let aDisabledFields = ['code'];
    if (CS.isEmpty(oModel)) {
      return null;
    } else {
      if (oActiveSection.type === "smartDocumentPreset") {
        oModel.smartDocumentPresetPdfConfiguration = this.getPDFConfigurationSectionView(oSmartDocumentLayoutData, oModel);
        oModel.smartDocumentPresetSelectionConfiguration = this.getSelectionConfigurationView(oSmartDocumentLayoutData, oModel.smartDocumentPresetSelectionConfiguration, oReferencedData);
        oModel.smartDocumentPresetFilterConfiguration = this.getFilterConfigurationSectionView(oSmartDocumentLayoutData, oModel);
      }
      return <CommonConfigSectionView
          context={"smartDocument"}
          sectionLayout={oSmartDocumentLayoutData[oActiveSection.type]}
          data={oModel}
          disabledFields={aDisabledFields}/>
    }
  };

  getHorizontalTreeView = (oContentHierarchyData) => {
    oContentHierarchyData.oHierarchyTree.label = getTranslation().TEMPLATES;
    return <HorizontalTreeView contentHierarchyData={oContentHierarchyData} showIcon={this.props.showSmartDocumentIcon}/>;
  };

  render () {
    let oCommonConfigSectionView = null;
    let oCreateDialogView = null;
    let oSmartDocumentLayoutData = new SmartDocumentLayoutData();
    let oActiveSection = this.props.activeSection;
    let oCreateSectionModel = this.props.createViewModel;
    let oContentHierarchyData = this.props.hierarchyData;
    let bisSectionDirty = this.props.isSectionDirty;
    let oCommonConfigSectionModel = this.props.sectionViewModel;
    let oReferencedData = {
      referencedAttributes : oActiveSection.referencedAttributes,
      referencedTags : oActiveSection.referencedTags
    };
    let sSmartDocumentCommonConfigSectionClass = "smartDocumentCommonConfigSection";
    let sSmartDocumentSnackBarView = "snackBarView";

    if (oActiveSection.isNewlyCreated) {
      oCreateDialogView = this.getCreateDialogView(oActiveSection, oCreateSectionModel)
    } else {
      oCommonConfigSectionView = this.getCommonConfigSectionView(oActiveSection, oSmartDocumentLayoutData, oCommonConfigSectionModel, oReferencedData);
    }
    if (bisSectionDirty && CS.isNotEmpty(oCommonConfigSectionView)) {
      sSmartDocumentCommonConfigSectionClass += " dirty";
      sSmartDocumentSnackBarView += " dirty";
    }
    return (
        <div className="smartDocumentConfigViewContainer">
          <div className={"smartDocumentListContainer"}>
            {this.getHorizontalTreeView(CS.cloneDeep(oContentHierarchyData))}
          </div>
          <div className="smartDocumentDetailViewContainer">
            <div className={sSmartDocumentCommonConfigSectionClass}>
              {oCommonConfigSectionView}
              {/*
                {oCommonConfigSectionView}
             */}
            </div>
            <div className={sSmartDocumentSnackBarView}>{this.getSnackBarView()}</div>
          </div>
          {oCreateDialogView}
        </div>
    )
  }
}

SmartDocumentConfigView.propTypes = oPropTypes;


export const view = SmartDocumentConfigView;
export const events = oEvents;
export const propTypes = SmartDocumentConfigView.propTypes;
