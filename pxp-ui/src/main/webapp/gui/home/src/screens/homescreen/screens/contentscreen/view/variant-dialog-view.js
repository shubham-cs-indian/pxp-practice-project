import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';

import { view as ContentSectionViewNew } from './content-section-view-new';
import { view as VariantTagGroupView } from './../../../../../viewlibraries/varianttagsummaryview/variant-tag-group-view';
import { view as ContentAttributeElementView } from './../../../../../viewlibraries/attributeelementview/content-attribute-element-view';
import ContentAttributeElementViewModel from './../../../../../viewlibraries/attributeelementview/model/content-attribute-element-view-model';
import { view as PaperView } from '../../../../../viewlibraries/paperview/paper-view';
import PaperViewModel from './../../../../../viewlibraries/paperview/model/paper-model';
import { view as CustomDialogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import ViewUtils from './utils/view-utils';
var getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
  VARIANT_DIALOG_SAVE_BUTTON_CLICKED: "handle_variant_dialog_save_clicked",
  VARIANT_DIALOG_DISCARD_BUTTON_CLICKED: "handle_variant_dialog_discard_clicked",
  VARIANT_DIALOG_ON_CHANGE_ATTRIBUTE_VALUE: "handle_variant_dialog_change_attribute_value"
};

const oPropTypes = {
  isDialogOpen: ReactPropTypes.bool,
  variantSectionViewData: ReactPropTypes.object,
  canEdit: ReactPropTypes.bool,
  canDelete: ReactPropTypes.bool,
  sectionViewModels: ReactPropTypes.array
};

// @CS.SafeComponent
class VariantDialogView extends React.Component {
  static propTypes = oPropTypes;

  getVariantContext = () => {
    var oVariantSectionViewData = this.props.variantSectionViewData;
    var sVariantDialogOpenContext = oVariantSectionViewData.variantDialogOpenContext;
    var aVariantDialogOpenContext = sVariantDialogOpenContext.split(ViewUtils.getSplitter());
    return aVariantDialogOpenContext && aVariantDialogOpenContext[1] ? aVariantDialogOpenContext[1] : "";
  };

  handleSaveButtonClicked = () => {
    var sContext = this.getVariantContext();
    EventBus.dispatch(oEvents.VARIANT_DIALOG_SAVE_BUTTON_CLICKED, sContext, this.props.filterContext);
  };

  handleDiscardButtonClicked = () => {
    var sContext = this.getVariantContext();
    EventBus.dispatch(oEvents.VARIANT_DIALOG_DISCARD_BUTTON_CLICKED, sContext);
  };

  handleAttributeValueOnChange = (oData) => {
    var sContext = this.getVariantContext();
    EventBus.dispatch(oEvents.VARIANT_DIALOG_ON_CHANGE_ATTRIBUTE_VALUE, sContext, oData);
  };

  handleButtonClick = (sButtonId) => {
    if(sButtonId === "save") {
      this.handleSaveButtonClicked();
    }
    else{
      this.handleDiscardButtonClicked();
    }
  };

  //TODO: Check If any scenario exists to show sections in variant dialog otherwise remove it
  getSectionsView = () => {
    var oVariantSectionViewData = this.props.variantSectionViewData;
    var aModels = oVariantSectionViewData.sectionViewModels;
    var aSections = [];

    CS.forEach(aModels, function (oModel) {
      var oSection = <ContentSectionViewNew  key={oModel.id}
                                          model={oModel}
                                          referencedClasses={oVariantSectionViewData.referencedClasses}
                                          selectedContext={oVariantSectionViewData.selectedContext}/>;
      var oPaperViewModel = new PaperViewModel(oModel.id, null, null, null, CS.getLabelOrCode(oModel), "", {context: ""});
      aSections.push(<PaperView model={oPaperViewModel} children = {oSection}/>);
    });

    return aSections;
  };

  getVariantTagsView = () => {
    var oVariantSectionViewData = this.props.variantSectionViewData;

    return (<VariantTagGroupView variantSectionViewData={oVariantSectionViewData}
                                 canEdit={this.props.canEdit}
                                 canDelete={this.props.canDelete}
                                 filterContext={this.props.filterContext}/>);
  };

  getContentAttributeElementViewModel = () => {
    let oVariantSectionViewData = this.props.variantSectionViewData;
    let sVariantDialogOpenContext = oVariantSectionViewData.variantDialogOpenContext;
    let oMasterAttribute = oVariantSectionViewData.masterAttribute;
    let oDummyVariant = oVariantSectionViewData.dummyVariant;
    let oEditableVariant = oVariantSectionViewData.editableVariant;
    let oVariant = CS.includes(sVariantDialogOpenContext, "create") ? oDummyVariant : oEditableVariant;
    let sValue = !CS.isEmpty(oVariant) ? oVariant.value : "";
    let oContentAttributeElementViewModel = new ContentAttributeElementViewModel(oMasterAttribute.id, "", sValue, false, "", "", oMasterAttribute, {});
    return oContentAttributeElementViewModel;
  };

  getSingleValueDOM = () => {
    var oContentAttributeElementViewModel = this.getContentAttributeElementViewModel();
    var sValueHeader = this.props.valueHeaderLabel || getTranslation().VALUE;
    var fAttributeValueOnChangeHandler = this.handleAttributeValueOnChange;
    return (
        <div className="singleValueAttribute">
        <div className="singleValueContainer">
          <div className="headerLabel">{sValueHeader}</div>
          <div className="userInput">
            <ContentAttributeElementView
                model={oContentAttributeElementViewModel}
                onChangeHandler={fAttributeValueOnChangeHandler}
            />
          </div>
        </div>
        </div>);
  };

  render() {
    var sSplitter = ViewUtils.getSplitter();
    var bIsDialogOpen = this.props.isDialogOpen;
    var aSectionsView = this.getSectionsView();
    var aTagView = this.getVariantTagsView();
    var oSingleValueDOM = null;

    var oVariantSectionViewData = this.props.variantSectionViewData;
    var bIsVariatingAttributeDialogOpen = oVariantSectionViewData.isVariantingAttributeDialogOpen;
    var sVariantDialogOpenContext = oVariantSectionViewData.variantDialogOpenContext;
    var aVariantDialogOpenContext = sVariantDialogOpenContext.split(sSplitter);
    var sVariantDialogOpenMode = aVariantDialogOpenContext[0];
    var oSelectedVisibleContext = oVariantSectionViewData.selectedVisibleContext;

    var sDialogHeader = sVariantDialogOpenMode === "create" ?
        (!oSelectedVisibleContext.isLimitedObject ? getTranslation().CREATE_VARIANT : getTranslation().CONTEXT_CONFIGURATION) :
        getTranslation().EDIT_VARIANT;

    if(oVariantSectionViewData.masterAttribute) {
      sDialogHeader += ' - ' + CS.getLabelOrCode(oVariantSectionViewData.masterAttribute);
    }

    if(bIsVariatingAttributeDialogOpen){
      oSingleValueDOM = this.getSingleValueDOM();
    }
    let fButtonHandler = this.handleButtonClick;
    var aButtonData=[
      {
        id: "cancel",
        label: getTranslation().DISCARD,
        isFlat: true
      },
      {
        id: "save",
        label: !oSelectedVisibleContext.isLimitedObject ? getTranslation().SAVE : getTranslation().CREATE,
        isFlat: false
      }
    ];

    var oBodyStyle = {
      "overflow": "auto"
    };

    return (
        <div className="variantDialogWrapper">
          <CustomDialogView
              open = {bIsDialogOpen}
              buttonData = {aButtonData}
              onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
              buttonClickHandler= {fButtonHandler}
              className="variantDialog"
              title = {sDialogHeader}
              bodyStyle={oBodyStyle}
              contentClassName="variantDialogContent"
              bodyClassName="variantDialogBody">
            <div className="variantDialogContainer">
              <div className="variantDetailDialogBody">
                <div className="variantDetailDialog">
                  {aTagView}
                  {aSectionsView}
                  {oSingleValueDOM}
                </div>
              </div>
            </div>
          </CustomDialogView>
        </div>
    );
  }
}

export const view = VariantDialogView;
export const events = oEvents;
