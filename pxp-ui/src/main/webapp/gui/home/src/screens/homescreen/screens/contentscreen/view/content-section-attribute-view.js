import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import ScreenModeUtils from './../store/helper/screen-mode-utils';

import { view as ContentAttributeElementView } from '../../../../../viewlibraries/attributeelementview/content-attribute-element-view';
import ContentAttributeElementViewModel from '../../../../../viewlibraries/attributeelementview/model/content-attribute-element-view-model';
import ViewUtils from './utils/view-utils';
const getTranslation = ScreenModeUtils.getTranslationDictionary;

const oEvents = {
  CONTENT_SECTION_VIEW_ATTRIBUTE_TEXT_VAL_CHANGED: "content_section_view_attribute_text_val_changed",
  CONTENT_SECTION_VIEW_ATTRIBUTE_MEASUREMENT_METRIC_VAL_CHANGED: "content_section_view_attribute_measurement_metric_val_changed",
  CONTENT_SECTION_VIEW_ATTRIBUTE_CONCATENATED_EXPRESSION_CHANGED: "content_section_view_attribute_concatenated_expression_changed"
};

const oPropTypes = {
  sectionElement: ReactPropTypes.object,
  sectionId: ReactPropTypes.string,
  sectionContext: ReactPropTypes.string,
  dataId: ReactPropTypes.string,
  onAttributeClick: ReactPropTypes.func,
  variantVersionProps: ReactPropTypes.object,
  versionView: ReactPropTypes.object,
  masterTags: ReactPropTypes.array,
  collapsed: ReactPropTypes.bool,
  filterContext: ReactPropTypes.object,
  selectedContext: ReactPropTypes.object
};

// @CS.SafeComponent
class ContentSectionAttributeView extends React.Component {

  constructor (props) {
    super(props);

    this.state = ContentSectionAttributeView.getNewState(props);
  }

  static propTypes = oPropTypes;

  static getDerivedStateFromProps (oNextProps, oNextState) {
    return ContentSectionAttributeView.getNewState(oNextProps);
  }

  static getNewState (oProps) {
    let oElement = oProps.sectionElement;
    let oMaster = oElement.attribute;
    let oAttributeInstance = oElement.contentAttributes[0] || {};
    let sValue = CS.isEmpty(oMaster.attributeTags) ? oAttributeInstance.value : oAttributeInstance.contextualCalculatedValue;

    return {
      elementId: oElement.id,
      attributeInstance: oAttributeInstance,
      value: sValue
    }
  };

  handleTextFieldChanged = (oData) => {
    let {sectionContext: sContext, sectionId: sSectionId} = this.props;
    let {elementId: sElementId, attributeInstance: {id: sAttributeId}, value: sOldValue} = this.state;
    let sValue = oData.value;

    if(sOldValue != sValue) {
      EventBus.dispatch(oEvents.CONTENT_SECTION_VIEW_ATTRIBUTE_TEXT_VAL_CHANGED, this, sContext, sElementId, sAttributeId, sValue, sSectionId, this.props.filterContext);
    }
  };

  handleNumberFieldChanged = (oData) => {
    let {sectionContext: sContext, sectionId: sSectionId} = this.props;
    let {elementId: sElementId, attributeInstance: {id: sAttributeId}, value: sOldValue} = this.state;
    let oMasterAttribute = this.props.sectionElement.attribute;
    let sSplitter = null;
    let sValue = oData.value;
    if (sValue.includes(".")) {
      sSplitter = ".";
    } else if (sValue.includes(",")) {
      sSplitter = ",";
    }
    if (sSplitter != null) {
      let aSplits = sValue.split(sSplitter);

      let iPrecision = oData.precision ? oData.precision : oMasterAttribute.precision;
      sValue = iPrecision == 0 ? aSplits[0] : sValue;
      if (aSplits[1].length <= iPrecision) {
          if (sOldValue !== sValue) {
            EventBus.dispatch(oEvents.CONTENT_SECTION_VIEW_ATTRIBUTE_TEXT_VAL_CHANGED, this, sContext, sElementId, sAttributeId, sValue, sSectionId, this.props.filterContext);
          }
      }
    }
    if (sOldValue != sValue) {
      EventBus.dispatch(oEvents.CONTENT_SECTION_VIEW_ATTRIBUTE_TEXT_VAL_CHANGED, this, sContext, sElementId, sAttributeId, sValue, sSectionId, this.props.filterContext);
    }
  };

  handleDateFieldChanged = (oData) => {
    let {sectionContext: sContext, sectionId: sSectionId} = this.props;
    let {elementId: sElementId, attributeInstance: {id: sAttributeId}} = this.state;
    let newDate = oData.value;
    EventBus.dispatch(oEvents.CONTENT_SECTION_VIEW_ATTRIBUTE_TEXT_VAL_CHANGED, this, sContext, sElementId, sAttributeId, newDate, sSectionId, this.props.filterContext);
  };

  getAttributeElementView = (
    sLabel,
    sValue,
    sRef,
    sErrorText,
    bDisabled,
    oHandler,
    oMasterAttribute,
    oExtraData,
  ) => {
    let sAttributeId = oExtraData.attributeId ? oExtraData.attributeId : oMasterAttribute.attributeId;

    let oProperties = {
      showDisconnected: oExtraData.showDisconnected,
      isMultiLine: oExtraData.isMultiLine,
      sectionId: oExtraData.sectionId,
      context: oExtraData.context,
      elementId: oExtraData.elementId,
      structureId: oExtraData.structureId,
      expressionList: oExtraData.expressionList,
      imageCoverflowModel: oExtraData.imageCoverflowModel,
      numberOfItemsAllowed: oExtraData.numberOfItemsAllowed,
      selectedContext: oExtraData.selectedContext,
      mssModel: oExtraData.mssModel,
      contextualAttributeTextFieldValueList: oExtraData.contextualAttributeTextFieldValueList || null,
      attributeVariantContext: oExtraData.attributeVariantContext,
      variantInstanceId: oExtraData.variantInstanceId,
      attributeContextViewData: oExtraData.attributeContextViewData,
      attributeVariantsStats: oExtraData.attributeVariantsStats,
      precision: oExtraData.precision,
      defaultUnit: oExtraData.defaultUnit
    };

    let oAttributeElementModel = new ContentAttributeElementViewModel(
        sAttributeId,
        sLabel,
        sValue,
        bDisabled,
        sErrorText,
        sRef,
        oMasterAttribute,
        oProperties
    );

    return (<ContentAttributeElementView key={sAttributeId + sRef}
                                         data-id={sAttributeId + sRef}
                                         model={oAttributeElementModel}
                                         filterContext={this.props.filterContext}
                                         onBlurHandler={oHandler}/>);

  };

  handleMeasurementMetricInuputFieldChanged = (oData) => {
    let {sectionContext: sContext, sectionId: sSectionId} = this.props;
    let {elementId: sElementId, attributeInstance: {id: sAttributeId}, value: sOldValue} = this.state;
    let sValue = oData.value;

    if(sOldValue !== sValue) {
      EventBus.dispatch(oEvents.CONTENT_SECTION_VIEW_ATTRIBUTE_MEASUREMENT_METRIC_VAL_CHANGED, this,
        sContext, sElementId, sAttributeId, sValue, sSectionId, this.props.filterContext);
    }
  };

  getMeasurementMetricView = (oElement, oAttributeVariant, oHandler) => {

    let oMasterAttribute = oElement.attribute;
    let sSplitter = ViewUtils.getSplitter();

    // let sValue = oAttributeVariant.value;
    let sValue = CS.isEmpty(oMasterAttribute.attributeTags) ? oAttributeVariant.value : oAttributeVariant.contextualCalculatedValue;
    let sLabel = CS.getLabelOrCode(oMasterAttribute);
    let sAttributeId = oAttributeVariant.id;
    let sRef = "attr" + sSplitter + oElement.id + sSplitter + sAttributeId;
    let bDisabled = oElement.isDisabled || false;
    let oContextualAttributeTextFieldValueList = this.getContextualAttributeValueList(oMasterAttribute, oAttributeVariant);
    let oExtraData = {
      attributeId: sAttributeId,
      contextualAttributeTextFieldValueList: oContextualAttributeTextFieldValueList,
      attributeVariantsStats: oElement.attributeVariantsStats
    };
    let iPrecision = oElement.precision;
    if (CS.isNumber(iPrecision)) {
      oExtraData.precision = iPrecision;
    }
    let sDefaultUnit = oElement.defaultUnit;
    if (!CS.isEmpty(sDefaultUnit)) {
      oExtraData.defaultUnit = sDefaultUnit;
    }
    oExtraData.attributeVariantContext = oElement.attributeVariantContext;
    oExtraData.variantInstanceId = oElement.attributeVariantContext ? oElement.variantInstanceId : "";
    oExtraData.attributeContextViewData = oElement.attributeContextViewData;
    oExtraData.showDisconnected = oElement.showDisconnected || true;
    return this.getAttributeElementView(sLabel, sValue, sRef, "", bDisabled, oHandler, oMasterAttribute, oExtraData);
  };

  handleConcatenatedExpressionChanged = (oData) => {
    let sExpressionId = oData.expressionId;
    let sSectionId = this.props.sectionId;
    let {attributeInstance: {id: sAttributeId}} = this.state;
    EventBus.dispatch(oEvents.CONTENT_SECTION_VIEW_ATTRIBUTE_CONCATENATED_EXPRESSION_CHANGED, sAttributeId, oData, sSectionId, sExpressionId, this.props.filterContext);
  };

  getMultiSelectSearchView = (oModel, oElement) => {
    let oMaster = oElement.attribute;
    let oExtraData = {
      mssModel: oModel,
      attributeVariantContext: oElement.attributeVariantContext,
      attributeContextViewData: oElement.attributeContextViewData
  };
    return this.getAttributeElementView("", "", "", "", "", null, oMaster, oExtraData);
  };

  getAttributeView = () => {
    const sSplitter = ViewUtils.getSplitter();
    let {sectionElement: oElement} = this.props;
    let sType = oElement.attribute.type;

    let sElementId = oElement.id;
    let oMaster = oElement.attribute;
    let sTitle = CS.getLabelOrCode(oMaster);
    let oAttributeInstance = this.state.attributeInstance;
    let sRef = "attr" + sSplitter + oElement.id + sSplitter + oAttributeInstance.id;
    let bDisabled = oElement.isDisabled || false;

    let sValue = CS.isEmpty(oMaster.attributeTags) ? oAttributeInstance.value : oAttributeInstance.contextualCalculatedValue;
    let sAttributeInstanceId = oAttributeInstance.id;
    let oHandler = null;

    if (ViewUtils.isAttributeTypeHtml(sType)) {
      return this.getHTMLFieldView(sValue, sElementId, sAttributeInstanceId, '', CS.getLabelOrCode(oMaster), oElement);

    } else if (ViewUtils.isAttributeTypeText(sType) || ViewUtils.isAttributeTypeUser(sType)) {
      return this.getTextFieldView(true, oElement, oAttributeInstance, this.handleTextFieldChanged);

    } else if (ViewUtils.isAttributeTypeNumber(sType)) {
      return this.getTextFieldView(true, oElement, oAttributeInstance, this.handleNumberFieldChanged);

    } else if (ViewUtils.isAttributeTypeDate(sType)) {
      return this.getDateFieldView(sTitle, sValue, sRef, "", bDisabled, this.handleDateFieldChanged, oAttributeInstance, oElement);

    } else if (ViewUtils.isAttributeTypeMeasurement(sType)) {
      return this.getMeasurementMetricView(oElement, oAttributeInstance, this.handleMeasurementMetricInuputFieldChanged);

    } else if (ViewUtils.isAttributeTypeTelephone(sType)) {
      return this.getTextFieldView(false, oElement, oAttributeInstance, this.handleNumberFieldChanged);

    } else if (ViewUtils.isCalculatedAttribute(sType)) {
      if (oElement.attribute.calculatedAttributeType) {
        return this.getMeasurementMetricView(oElement, oAttributeInstance, oHandler);
      } else {
        return this.getTextFieldView(false, oElement, oAttributeInstance, oHandler);
      }

    } else if (ViewUtils.isConcatenatedAttribute(sType)) {
      return this.getTextFieldView(false, oElement, oAttributeInstance, this.handleConcatenatedExpressionChanged);

    } else if (ViewUtils.isAttributeTypeType(sType) || ViewUtils.isAttributeTypeRole(sType)
        || ViewUtils.isAttributeTypeTaxonomy(sType) || ViewUtils.isAttributeTypeSecondaryClasses(sType)) {
      let oModel = oElement.model;
      return this.getMultiSelectSearchView(oModel, oElement);

    } else if (ViewUtils.isAttributeTypeCoverflow(sType)) {
      return this.getImageGallery(oElement);
    }

    return null;
  };

  getHTMLFieldView = (sValue, sElementId, sAttributeId, sStructureId, sLabel, oElement) => {
    let sSectionId = this.props.sectionId;
    let sContext = this.props.sectionContext;
    let bDisabled = oElement.isDisabled;
    let oAttribute = oElement.attribute;
    let oExtraData = {
      sectionId: sSectionId,
      context: sContext,
      elementId: sElementId,
      structureId: sStructureId,
      attributeId: sAttributeId,
      attributeVariantContext: oElement.attributeVariantContext,
      attributeContextViewData: oElement.attributeContextViewData
    };

    return this.getAttributeElementView(sLabel, sValue, "", "", bDisabled, null, oAttribute, oExtraData);
  };


  getContextualAttributeValueList = (oMasterAttribute, oAttributeVariant) => {
    let aValueList = [];
    if(!CS.isEmpty(oMasterAttribute.attributeTags)) {
      if (oAttributeVariant.contextualCalculatedValue) {
        aValueList.push({label: getTranslation().RANGE + ": ", value: oAttributeVariant.contextualCalculatedValue});
      }

      if (oAttributeVariant.contextualCalculatedAvg) {
        aValueList.push({label: getTranslation().AVERAGE + ": ", value: oAttributeVariant.contextualCalculatedAvg});
      }

      if (oAttributeVariant.showSimpleValue) {
        aValueList = [{label: "", value: oAttributeVariant.contextualCalculatedValue}];
      }
    }
    return aValueList;
  };

  getTextFieldView = (bMultiLine, oElement, oAttributeVariant, oHandler) => {
    const sSplitter = ViewUtils.getSplitter();
    let oMasterAttribute = oElement.attribute;
    let sValue = CS.isEmpty(oMasterAttribute.attributeTags) ? oAttributeVariant.value : oAttributeVariant.contextualCalculatedValue;
    let sLabel = CS.getLabelOrCode(oMasterAttribute);
    let sRef = "attr" + sSplitter + oElement.id + sSplitter + oAttributeVariant.id;
    let bDisabled = oElement.isDisabled || false;
    let oContextualAttributeTextFieldValueList = this.getContextualAttributeValueList(oMasterAttribute, oAttributeVariant);
    let iPrecision = CS.isNumber(oElement.precision) ? oElement.precision : undefined;
    let sVariantInstanceId = oElement.attributeVariantContext ? oElement.variantInstanceId : "";
    let oExtraData = {
      isMultiLine: bMultiLine,
      expressionList: oElement.expressionList,
      contextualAttributeTextFieldValueList: oContextualAttributeTextFieldValueList,
      attributeVariantContext: oElement.attributeVariantContext,
      attributeContextViewData: oElement.attributeContextViewData,
      attributeVariantsStats: oElement.attributeVariantsStats,
      precision: iPrecision,
      variantInstanceId: sVariantInstanceId
    };

    return this.getAttributeElementView(sLabel, sValue, sRef, "", bDisabled, oHandler, oMasterAttribute, oExtraData);
  };

  getDateFieldView = (
    sLabel,
    sValue,
    sRef,
    sErrorText,
    bDisabled,
    oHandler,
    oAttributeVariant,
    oElement,
  ) => {
    let oMasterAttribute = oElement.attribute;
    let aContextualAttributeTextFieldValueList = this.getContextualAttributeValueList(oMasterAttribute, oAttributeVariant);
    let oExtraData = {
      contextualAttributeTextFieldValueList: aContextualAttributeTextFieldValueList,
      attributeVariantContext: oElement.attributeVariantContext,
      attributeContextViewData: oElement.attributeContextViewData
    };
    return this.getAttributeElementView(sLabel, sValue, sRef, sErrorText, bDisabled, oHandler, oMasterAttribute, oExtraData);
  };

  getImageGallery = (oElement) => {
    let oMasterAttribute = oElement.attribute;
    let oExtraData = {
      imageCoverflowModel: oElement.model,
      numberOfItemsAllowed: oElement.numberOfItemsAllowed,
      selectedContext: this.props.selectedContext,
      attributeVariantContext: oElement.attributeVariantContext,
      attributeContextViewData: oElement.attributeContextViewData
    };
    return this.getAttributeElementView("", "", "", "", oElement.isDisabled, null, oMasterAttribute, oExtraData);
  };

  render() {

    return this.getAttributeView();
  }
}

export const events = oEvents;
export const view = ContentSectionAttributeView;
