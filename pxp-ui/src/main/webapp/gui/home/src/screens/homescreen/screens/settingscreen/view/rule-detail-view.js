import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as FroalaWrapperView } from '../../../../../viewlibraries/customfroalaview/froala-wrapper-view';
import { view as CalculatedAttributeFormulaView } from '../../../../../viewlibraries/calculatedattributeformulaview/calculated-attribute-formula-view';
import { view as CustomDatePicker } from '../../../../../viewlibraries/customdatepickerview/customdatepickerview.js';
import { view as NumberLocaleView } from '../../../../../viewlibraries/numberlocaleview/number-locale-view';
import TooltipView from '../../../../../viewlibraries/tooltipview/tooltip-view';
import { view as DragView } from './../../../../../viewlibraries/dragndropview/drag-view.js';
import DragViewModel from './../../../../../viewlibraries/dragndropview/model/drag-view-model';
import { view as DropView } from '../../../../../viewlibraries/dragndropview/drop-view.js';
import DropViewModel from './../../../../../viewlibraries/dragndropview/model/drop-view-model';
import { view as ContentFilterTagGroupView } from '../../../../../viewlibraries/contentfiltertaggroupview/content-filter-tag-group-view';
import { view as BlackListEditableView } from './black-list-editable-view';
import { view as SmallTaxonomyView } from './../../../../../viewlibraries/small-taxonomy-view/small-taxonomy-view';
import SmallTaxonomyViewModel from './../../../../../viewlibraries/small-taxonomy-view/model/small-taxonomy-view-model';
import { view as TagGroupView } from './../../../../../viewlibraries/taggroupview/tag-group-view.js';
import { view as KlassSelectorView } from './../../../../../viewlibraries/klassselectorview/klass-selector-view';
import { view as LazyContextMenuView } from '../../../../../viewlibraries/lazycontextmenuview/lazy-context-menu-view';
import { view as GridYesNoPropertyView } from '../../../../../viewlibraries/gridview/grid-yes-no-property-view';
import { view as ConcatenatedFormulaView } from '../../../../../viewlibraries/gridview/grid-concatenated-attribute-property-view';
import { view as CommonConfigSectionView } from './../../../../../viewlibraries/commonconfigsectionview/common-config-section-view';
import { view as LazyMSSView } from '../../../../../viewlibraries/lazy-mss-view/lazy-mss-view';
import ColorConstantDictionary from '../tack/mock/color-constants';
import RuleConstantDictionary from '../tack/mock/rule-constants';
import { view as ContentMeasurementMetricsView } from '../../../../../viewlibraries/measurementmetricview/content-measurement-metrics-view.js';
import ContentMeasurementMetricsViewModel from '../../../../../viewlibraries/measurementmetricview/model/content-measurement-metrics-view-model';
import MockDataForMeasurementMetricAndImperial from './../../../../../commonmodule/tack/mock-data-for-measurement-metrics-and-imperial';
import NatureTypeDictionary from '../../../../../commonmodule/tack/nature-type-dictionary.js';
import AttributeTypeDictionary from '../../../../../commonmodule/tack/attribute-type-dictionary-new';
import NonEditableAttributeTypeDictionary from '../../../../../commonmodule/tack/non-editable-attribute-types-dictionary';
import ViewUtils from './utils/view-utils';
import TagTypeConstants from '../../../../../commonmodule/tack/tag-type-constants';
import FilterTypeDictionary from '../../../../../commonmodule/tack/filter-type-dictionary';
import ConcatenatedEntityTypes from '../../../../../commonmodule/tack/concatenated-entity-types';
import RuleTypeDictionary from '../../../../../commonmodule/tack/rule-type-dictionary';
import DataGovernanceRuleConfigLayout from '../tack/data-governance-rules-config-layout';//.DataGovernanceRuleConfigLayoutInfo;
import GoldenRecordRuleConfigLayout from '../tack/golden-record-rules-config-layout';
import ViewContextConstants from '../../../../../commonmodule/tack/view-context-constants';
import ExceptionLogger from '../../../../../libraries/exceptionhandling/exception-logger';
import {arrayMove} from 'react-sortable-hoc';
import AttributesTypeDictionary from '../../../../../commonmodule/tack/attribute-type-dictionary-new';
import ConfigDataEntitiesDictionary from '../../../../../commonmodule/tack/config-data-entities-dictionary';
import MockDataIdsForFroalaView from '../../../../../commonmodule/tack/mock-data-ids-for-froala-view';
import CommonUtils from "../../../../../commonmodule/util/common-utils";
import {view as DragDropContextView} from "../../../../../viewlibraries/draggableDroppableView/drag-drop-context-view";

const oEvents = {
  RULE_ELEMENT_DELETE_CLICKED: "rule_element_delete_clicked",
  RULE_ATTRIBUTE_VALUE_TYPE_CLICKED: "rule_attribute_value_type_clicked",
  RULE_ATTRIBUTE_VALUE_CHANGED: "rule_attribute_value_changed",
  RULE_ADD_ATTRIBUTE_CLICKED: "rule_add_attribute_clicked",
  RULE_ATTRIBUTE_VALUE_DELETE_CLICKED: "rule_attribute_value_delete_clicked",
  RULE_ATTRIBUTE_VALUE_CHANGED_FOR_RANGE: "rule_attribute_value_changed_for_range",
  RULE_NAME_CHANGED: "rule_name_changed",
  RULE_ATTRIBUTE_COLOR_CHANGED: "rule_attribute_color_changed",
  RULE_ATTRIBUTE_DESCRIPTION_CHANGED: "rule_attribute_description_changed",

  CONTENT_FILTER_ATTRIBUTE_EXPAND_CLICKED: "content_filter_attribute_expand_clicked",

  RULE_RIGHT_PANEL_BAR_ICON_CLICKED: "rule_right_panel_bar_icon_clicked",
  REMOVE_BLACKLIST_ICON_CLICKED: "remove_blacklist_icon_clicked",
  ATTRIBUTE_VISIBILITY_BUTTON_CLICKED: "attribute_visibility_button_clicked",
  HANDLE_ATTRIBUTE_VIEW_TYPE_CHANGED: "handle_attribute_view_type_changed",
  RULE_ATTRIBUTE_DESCRIPTION_CHANGED_IN_NORMALIZATION: "rule_attribute_description_changed_in_normalization",
  RULE_ATTRIBUTE_VALUE_CHANGED_IN_NORMALIZATION: "rule_attribute_value_changed_in_normalization",

  RULE_CALC_ATTR_ADD_OPERATOR: "rule_calc_attr_add_operator",
  RULE_CALC_ATTR_OPERATOR_ATTRIBUTE_VALUE_CHANGED: "rule_calc_attr_operator_attribute_value_changed",
  RULE_CALC_ATTR_DELETE_OPERATOR_ATTRIBUTE_VALUE: "rule_calc_attr_delete_operator_attribute_value",
  RULE_CALC_ATTR_CUSTOM_UNIT_CHANGED: "rule_calc_attr_custom_unit_changed",
  RULE_USER_VALUE_CHANGED: "rule_user_value_changed",
  RULE_DETAIL_ROLE_CAUSE_EFFECT_MSS_VALUE_CHANGED: "rule_detail_role_cause_effect_mss_value_changed",
  RULE_DETAIL_ATTRIBUTE_CAUSE_EFFECT_MSS_VALUE_CHANGED: "rule_detail_attribute_cause_effect_mss_value_changed",
  RULE_DETAIL_TAG_CAUSE_EFFECT_MSS_VALUE_CHANGED: "rule_detail_tag_cause_effect_mss_value_changed",
  RULE_DETAIL_USER_VALUE_FOR_NORMALIZATION_CHANGED: "rule_detail_user_value_for_normalization_changed",
  COMPARE_WITH_SYSTEM_DATE_BUTTON_CLICKED: "compare_with_system_date_button_clicked",
  RULE_DETAILS_MSS_LOAD_MORE_CLICKED: "rule_details_mss_load_more_clicked",
  RULE_DETAILS_MSS_SEARCH_CLICKED: "rule_details_mss_search_clicked",
  RULE_DETAIL_KLASS_CAUSE_EFFECT_MSS_VALUE_CHANGED: "rule_detail_klass_cause_effect_mss_value_changed",
  RULE_DETAIL_KLASS_TYPE_CAUSE_MSS_VALUE_CHANGED: "rule_detail_klass_type_cause_effect_mss_value_changed",
  RULE_ATTRIBUTE_VALUE_CHANGED_FOR_RANGE_IN_NORMALIZATION: "rule_attribute_value_changed_for_range_in_normalization",
  RULE_DETAIL_PARTNERS_MSS_VALUE_CHANGED: "rule_detail_partners_mss_value_changed",
  RULE_DETAIL_ENDPOINTS_MSS_VALUE_CHANGED: "rule_detail_endpoints_mss_value_changed",
  RULE_DETAIL_MSS_VALUE_CHANGED: "rule_detail_mss_value_changed",
  RULE_ENTITIES_ADDED_IN_MERGE_SECTION: "rule_entities_added_in_merge_section",
  RULE_LATEST_ENTITY_SELECTION_TOGGLED: "rule_latest_entity_selection_toggled",
  RULE_DETAIL_SELECTED_ENTITY_REMOVED: "rule_detail_selected_entity_removed",
  RULE_DETAIL_SELECTED_SUPPLIER_REMOVED: "rule_detail_selected_supplier_removed",
  RULE_DETAIL_SUPPLIER_SEQUENCE_CHANGED: "rule_detail_supplier_sequence_changed",
  RULE_DETAIL_CONCATENATED_FORMULA_CHANGED: "rule_detail_concatenated_formula_changed"
};

const oPropTypes = {
  context: ReactPropTypes.string,
  activeRule: ReactPropTypes.object,
  ruleListDetailData: ReactPropTypes.array,
  rightPanelData: ReactPropTypes.object,
  screenContext: ReactPropTypes.string,
  ruleEffect: ReactPropTypes.array,
  oCalculatedAttributeMapping: ReactPropTypes.object,
  classList: ReactPropTypes.array,
  dataForCalculatedAttributes: ReactPropTypes.object,
  showOnlyCause: ReactPropTypes.bool,
  selectedKlassType: ReactPropTypes.string,
  multiTaxonomyData: ReactPropTypes.object,
  taxonomyPaginationData: ReactPropTypes.object,
  lazyMSSReqResInfo: ReactPropTypes.object,
  physicalCatalogIdsData: ReactPropTypes.array,
  portalIdsData: ReactPropTypes.array,
  isRuleDirty: ReactPropTypes.bool,
  dataLanguages: ReactPropTypes.array,
};

// @CS.SafeComponent
class RuleDetailView extends React.Component {

  constructor(props) {
    super(props);

    this.ruleDetailLeftViewWrapper = React.createRef();
  }

  static childContextTypes = {
    masterTagList: ReactPropTypes.object,
  };

  static propTypes = oPropTypes;

  state = {
    showAddAttributeDropdown: false,
    showAddTagsDropdown: false
  };

  getChildContext() {
    let _props = this.props;
    let oActiveRule = _props.activeRule;
    let oReferencedTags = {};

    if(!oActiveRule.isCreated && oActiveRule.configDetails) {
      let oConfigDetails = oActiveRule.configDetails;
      CS.forEach(oConfigDetails.referencedTags, function (oReferencedTag) {
        if (oReferencedTag) {
          oReferencedTags[oReferencedTag.id] = oReferencedTag;
        }
      });
    }

    let oLoadedTags = ViewUtils.getLoadedTagsData();
    CS.forEach(oLoadedTags, function (oLoadedTag) {
      if (oLoadedTag) {
        oReferencedTags[oLoadedTag.id] = oLoadedTag;
      }
    });

    return {
      masterTagList: oReferencedTags
    };
  }

  componentDidUpdate(oPreProps, oState) {
    if (this.props.activeRule.id !== oPreProps.activeRule.id) {
      let oLeftFixedSectionDOM = this.ruleDetailLeftViewWrapper.current;
      if(oLeftFixedSectionDOM) {
        oLeftFixedSectionDOM.scrollTop = 0;
      }
    }

    this.updateFields();
  }

  componentDidMount() {
    this.updateFields();
  }

  handleRuleUserValueForNormalizationChanged = (sRoleId, aUsers) => {
    EventBus.dispatch(oEvents.RULE_DETAIL_USER_VALUE_FOR_NORMALIZATION_CHANGED, sRoleId, aUsers);
  };

  handleRuleUserValueChanged = (oExtraData, aNewValue) => {
    EventBus.dispatch(oEvents.RULE_USER_VALUE_CHANGED, oExtraData, aNewValue, this.props.screenContext);
  };

  handleRoleCauseEffectMSSValueChanged = (sContext, aNewValue) => {
    EventBus.dispatch(oEvents.RULE_DETAIL_ROLE_CAUSE_EFFECT_MSS_VALUE_CHANGED, sContext, aNewValue, this.props.screenContext);
  };

  handleAttributeCauseEffectMSSValueChanged = (sContext, aNewValue, oReferencedData) => {
    EventBus.dispatch(oEvents.RULE_DETAIL_ATTRIBUTE_CAUSE_EFFECT_MSS_VALUE_CHANGED, sContext, aNewValue, this.props.screenContext, oReferencedData);
  };

  handleTagCauseEffectMSSValueChanged = (sContext, aNewValue) => {
    EventBus.dispatch(oEvents.RULE_DETAIL_TAG_CAUSE_EFFECT_MSS_VALUE_CHANGED, sContext, aNewValue, this.props.screenContext);
  };

  handleKlassCauseEffectMSSValueChanged = (sContext, aSelectedClasses) => {
    EventBus.dispatch(oEvents.RULE_DETAIL_KLASS_CAUSE_EFFECT_MSS_VALUE_CHANGED, sContext, aSelectedClasses);
  };

  handleKlassTypeCauseMSSValueChanged = (aNewKlassType) => {
    // Always CAUSE
    EventBus.dispatch(oEvents.RULE_DETAIL_KLASS_TYPE_CAUSE_MSS_VALUE_CHANGED, aNewKlassType);
  };

  handlePartnerApplyClicked = (aSelectedList) => {
    EventBus.dispatch(oEvents.RULE_DETAIL_PARTNERS_MSS_VALUE_CHANGED, aSelectedList);
  };

  handleAddAttributeButtonClicked = (sContext) => {
    if(sContext == 'attributes'){
      this.setState({
        showAddAttributeDropdown: true
      });
    }
    else {
      this.setState({
        showAddTagsDropdown: true
      });
    }
  };

  addEntitiesFromDropdown = (sContext, sSelectedEntityType, sSelectedEntity, aSelectedEntities, oReferencedData) => {
    EventBus.dispatch(oEvents.RULE_ENTITIES_ADDED_IN_MERGE_SECTION, sContext, aSelectedEntities, sSelectedEntityType, sSelectedEntity, oReferencedData)
  };

  handleLatestEntityValueSelectionToggled = (sContext ,sSelectedEntity ) => {
    EventBus.dispatch(oEvents.RULE_LATEST_ENTITY_SELECTION_TOGGLED, sContext, sSelectedEntity)
  };

  updateFields = () => {
    if(!this.props.showOnlyCause) {}
  };

  getRoleForMSS = (sContext) => {
    var __props = this.props;
    var oActiveRule = __props.activeRule;
    var oRoleMap = ViewUtils.getRoleList();
    var aSelectedAttributes = [];
    if(sContext == "EFFECT") {
      var aRuleEffect = this.props.ruleEffect;
      aSelectedAttributes = CS.filter(aRuleEffect, {type: 'role'});
    } else {
      aSelectedAttributes = oActiveRule.roles;
    }
    var aRes = [];

    CS.forEach(oRoleMap, function(oAttr){
      if(!CS.find(aSelectedAttributes, {entityId: oAttr.id})){
        aRes.push(oAttr);
      }
    });

    return aRes;
  };

  getAttributeByAttributeId = (sAttrId) => {
    var __props = this.props;
    var oAttributeMap = ViewUtils.getAttributeList();
    var oActiveRule = __props.activeRule;
    var oConfigDetails = oActiveRule.configDetails;
    var oLoadedAttributeData = ViewUtils.getLoadedAttributesData();
    var oAttribute = (oConfigDetails.referencedAttributes && oConfigDetails.referencedAttributes[sAttrId]) ||
        (oLoadedAttributeData && oLoadedAttributeData[sAttrId]) || oAttributeMap[sAttrId] || {};
    return oAttribute;
  };

  getRoleByRoleId = (sRoleId) => {
    var __props = this.props;
    var oRoleMap = ViewUtils.getRoleList();
    var oActiveRule = __props.activeRule;
    var oConfigDetails = oActiveRule.configDetails;
    var oLoadedRoleData = ViewUtils.getLoadedRolesData();
    var oRole = (oConfigDetails.referencedRoles && oConfigDetails.referencedRoles[sRoleId]) ||
        (oLoadedRoleData && oLoadedRoleData[sRoleId]) || oRoleMap[sRoleId] || {};
    return oRole;
  };

  getTagByTagId = (sTagId) => {
    var oActiveRule = this.props.activeRule;
    var oConfigDetails = oActiveRule.configDetails;
    var oReferencedTags = oConfigDetails.referencedTags;
    var oLoadedTags = ViewUtils.getLoadedTagsData();
    return oLoadedTags[sTagId] || oReferencedTags[sTagId];
  };

  getTypeDropdownView = (sSelectedId, sAttrId, sValId, sContext, isCause, sTypeOfRule) => {
    var aOptions = [];
    var oAttribute = sContext === 'tag' ? this.getTagByTagId(sAttrId) : this.getAttributeByAttributeId(sAttrId);
    var sTypeForVisual = ViewUtils.getAttributeTypeForVisual(oAttribute.type, sAttrId);
    var oFilter = new FilterTypeDictionary.FILTER_TYPES_WITH_ISDUPLICATE();
    let oRuleTypeDictionary = new RuleTypeDictionary();
    if(isCause) {
      if (sTypeForVisual == "number") {
        oFilter = new FilterTypeDictionary.FILTER_TYPES_FOR_NUMBER_WITH_ISDUPLICATE();
      }
      else if (sTypeForVisual == "measurementMetrics") { /*Created separate filter types for measurement metrics because contains, starts with, ends with not working due to conversion to base type*/
        oFilter = new FilterTypeDictionary.FILTER_TYPES_FOR_MEASUREMENT_METRICS();
      }
      else if (sTypeForVisual == "date") {
        oFilter = new FilterTypeDictionary.FILTER_TYPES_FOR_DATE_WITH_ISDUPLICATE();
      }
      else if (sTypeForVisual == "text" || sTypeForVisual == "html") {
        oFilter = new FilterTypeDictionary.FILTER_TYPES_FOR_HTML_AND_TEXT();
      }
      else if (sTypeForVisual == "calculated") {
        oFilter = new FilterTypeDictionary.FILTER_TYPES_FOR_MEASUREMENT_METRICS();
      }
      else if (sTypeForVisual == "concatenated") {
        oFilter = new FilterTypeDictionary.FILTER_TYPES_FOR_CONCATENATED();
      }
    }
    else if (sTypeOfRule === oRuleTypeDictionary.STANDARDIZATION_AND_NORMALIZATION_RULE) {

      if(sContext == 'attribute') {
        if (sTypeForVisual == "text" || sTypeForVisual == "html") {
          oFilter = new FilterTypeDictionary.FILTER_TYPES_FOR_HTML_AND_TEXT_IN_EFFECT();
        } else {
          oFilter = new FilterTypeDictionary.FILTER_TYPES_FOR_EFFECT();
        }
      }
      else if (sContext === 'tag' && oAttribute.tagType !== TagTypeConstants.TAG_TYPE_BOOLEAN) {
        oFilter = new FilterTypeDictionary.FILTER_TYPES_FOR_TAGS_EFFECT();
      }
      else {
        return null;
      }
    }

    CS.forEach(oFilter, function(sTypeVal, sKey){
      aOptions.push(<option value={sKey} key={sKey}>{sTypeVal}</option>)
    });

    var bIsDisabled = oAttribute.id == "all";

    return (<select disabled={bIsDisabled} value={sSelectedId} onChange={this.handleAttributeValTypeDropdownChanged.bind(this, sAttrId, sValId,sContext, isCause)}>{aOptions}</select>);
  };

  getMeasurementMetricView = (sContext, sAttrId, oAttrVal, sAttributeType, oHandler, bDisableLocaleFormatting) => {
    var sSplitter = ViewUtils.getSplitter();
    var oMasterAttribute = this.getAttributeByAttributeId(sAttrId);
    var sType = sAttributeType;
    var sDefaultUnit = oMasterAttribute.defaultUnit;
    var bIsCustomAttribute = false;
    var bConverterVisibility = !ViewUtils.isAttributeTypePrice(sType);
    var bIsCalculatedAttribute = ViewUtils.getAttributeTypeForVisual(sType) == 'calculated';
    if (bIsCalculatedAttribute) {
      sType = oMasterAttribute.calculatedAttributeType;
      sDefaultUnit = oMasterAttribute.calculatedAttributeUnit;
      if (!sType) {
        bIsCustomAttribute = true;
        bConverterVisibility = false;
      }
    }
    if (ViewUtils.isMeasurementAttributeTypeCustom(sType)) {
      bIsCustomAttribute = true;
      bConverterVisibility = false;
    }
    var sValue = "";
    if(sContext === "VAL") {
      sValue = (oAttrVal.values && oAttrVal.values[0]) || (oAttrVal.normalization && oAttrVal.normalization.values[0]) || "";
    }else if (sContext === "TO") {
      sValue = oAttrVal.to;
    }else {
      sValue = oAttrVal.from;
    }
    var sLabel = "";
    var sRef = "attr" + sSplitter + oAttrVal.id + sSplitter + sAttrId + sSplitter + sContext;
    var sPlaceholder = "";
    var sDescription = "";
    var bDisabled = oAttrVal.isDisabled || false;
    let oMeasurementMetricAndImperial = new MockDataForMeasurementMetricAndImperial();
    var aConverterList = oMeasurementMetricAndImperial[sType];
    var oBaseUnit = CS.find(aConverterList, {isBase: true});
    var sBaseUnit = oBaseUnit ? oBaseUnit.unit : sDefaultUnit;
    var oProperties = {};
    if (bIsCustomAttribute) {
      oProperties.defaultUnitAsHTML = oMasterAttribute.defaultUnitAsHTML ? oMasterAttribute.defaultUnitAsHTML : "";
      sDefaultUnit = "";
      sBaseUnit = "";
      if (bIsCalculatedAttribute) {
        oProperties.defaultUnitAsHTML = oMasterAttribute.calculatedAttributeUnitAsHTML ? oMasterAttribute.calculatedAttributeUnitAsHTML : "";
      }
    }
    else {
      oProperties.baseUnit = (CS.find(oMeasurementMetricAndImperial[sType], {isBase: true}))["unit"];
    }
    var iPrecision = Number(oMasterAttribute.precision);

    CS.forEach(aConverterList, function (oListItem) {
      oListItem.unitToDisplay = oListItem.unitToDisplay + ' (' + CS.getLabelOrCode(oListItem) + ')'
    });
    oProperties.disableValueChangeByDefaultUnit = ViewUtils.isAttributeTypePrice(sType);

    oProperties.disableNumberLocaleFormatting = bDisableLocaleFormatting;

    var oModel = new ContentMeasurementMetricsViewModel(
        sAttrId,
        sLabel,
        sValue,
        sDefaultUnit,
        sBaseUnit,
        sPlaceholder,
        "",
        sDescription,
        bDisabled,
        bConverterVisibility,
        aConverterList,
        oProperties,
        iPrecision,
        sType
    );
    return (<ContentMeasurementMetricsView ref={sRef}
                                           key={sRef}
                                           onBlur={oHandler}
                                           model={oModel}/>);
  };

  getEffectViewForCalculatedAttribute = (sAttrId, oAttrVal) => {
    var oMasterAttribute = this.getAttributeByAttributeId(sAttrId);
    var sEntityId = oAttrVal.entityId;
    var sId = oAttrVal.id;
    var aNormalizations = this.props.activeRule.normalizations;
    var oNormalization = CS.find(aNormalizations, {entityId: sEntityId, id: sId}) || {};
    var aAttributeOperatorList = oNormalization.attributeOperatorList || [];
    var oFormulaView = (
        <CalculatedAttributeFormulaView
            calculatedAttribute={oMasterAttribute}
            attributeOperatorList={aAttributeOperatorList}
            addOperatorHandler={this.addOperator.bind(this, sEntityId, sId)}
            operatorAttributeValueChangedHandler={this.operatorAttributeValueChanged.bind(this, sEntityId, sId)}
            deleteOperatorAttributeValueHandler={this.deleteOperatorAttributeValue.bind(this, sEntityId, sId)}
            paginationData={this.props.dataForCalculatedAttributes.paginationData}
            extraData={this.props.dataForCalculatedAttributes.extraData}
            allowedAttributes={this.props.dataForCalculatedAttributes.allowedAttributes}
        />
    );

    var oUnitView = null;
    if (ViewUtils.isAttributeTypeCalculated(oMasterAttribute.type) && ViewUtils.isMeasurementAttributeTypeCustom(oMasterAttribute.calculatedAttributeType)) {
      oUnitView = (
          <div className="customUnitWrapper">
            <div className="customUnitLabel">{getTranslation().CUSTOM_DEFAULT_UNIT}</div>
            <FroalaWrapperView dataId={sId}
                               activeFroalaId={sId}
                               content={oNormalization.calculatedAttributeUnitAsHTML || ""}
                               className={""}
                               toolbarIcons={MockDataIdsForFroalaView}
                               showPlaceHolder={false}
                               handler={this.unitChangeHandler.bind(this, sEntityId, sId)}/>
          </div>
      );
    }

    return (
        <div>
          {oFormulaView}
          {oUnitView}
        </div>
    );
  };

  unitChangeHandler = (sAttrId, sId, oEvent) => {
    EventBus.dispatch(oEvents.RULE_CALC_ATTR_CUSTOM_UNIT_CHANGED, sAttrId, sId, oEvent.textValue, oEvent.htmlValue);
  };

  addOperator = (sAttrId, sId, sOperatorType) => {
    EventBus.dispatch(oEvents.RULE_CALC_ATTR_ADD_OPERATOR, sAttrId, sId, sOperatorType);
  };

  operatorAttributeValueChanged = (sAttrId, sId, oAttributeOperator, sValue) => {
    var sType = oAttributeOperator.type;
    var sAttributeOperatorId = oAttributeOperator.id;
    EventBus.dispatch(oEvents.RULE_CALC_ATTR_OPERATOR_ATTRIBUTE_VALUE_CHANGED, sAttrId, sId, sType, sAttributeOperatorId, sValue);
  };

  deleteOperatorAttributeValue = (sAttrId, sId, sAttributeOperatorId) => {
    EventBus.dispatch(oEvents.RULE_CALC_ATTR_DELETE_OPERATOR_ATTRIBUTE_VALUE, sAttrId, sId, sAttributeOperatorId);
  };

  getAttributeInputViewByAttributeType = (
    sType,
    sAttrId,
    oAttrVal,
    sAttributeType,
    sDefaultUnit,
    sContext,
    iPrecision,
    bDisableLocaleFormatting
  ) => {

    var sSplitter = ViewUtils.getSplitter();
    var bContainsType = sType == "contains";
    var sTypeForVisual = ViewUtils.getAttributeTypeForVisual(sAttributeType, sAttrId);
    var sValue = oAttrVal.values[0] || "";
    var aDropContext = ["ruleList"];
    if(this.props.screenContext == "condition") {
      sValue = oAttrVal.value[0] || '';
      var sConditionContext = "ruleList" + sSplitter + "condition";
      aDropContext.push(sConditionContext);
    }
    var oDropViewProperties = {};
    var sDropId = "attributes" + sSplitter + sAttrId + sSplitter + oAttrVal.id;
    var oDropViewModel = new DropViewModel(sDropId, true, aDropContext, oDropViewProperties);
    var oNoDropViewModel = new DropViewModel(sDropId, true, ["noDropAllowed"], oDropViewProperties);

    switch (sTypeForVisual){

      case "html":
      case "text":
      case "concatenated":
      case "all":
        if(oAttrVal.type == "length_lt" || oAttrVal.type == "length_gt" || oAttrVal.type == "length_equal")
        {
          return (
              <NumberLocaleView
                  value={sValue.toString()}
                  isOnlyInteger={true}
                  onChange={this.handleAttributeValueNumberTypeChanged.bind(this, sAttrId, oAttrVal.id,sContext)}
                  label={""}
                  precision={iPrecision}
                  disableNumberLocaleFormatting={bDisableLocaleFormatting}
              />
          );
        }else if(oAttrVal.type == "length_range"){
          if(oAttrVal.from == null) oAttrVal.from = "";
          if(oAttrVal.to == null) oAttrVal.to = "";
          return this.getRangeFieldsView(oAttrVal, sAttrId, iPrecision, true, bDisableLocaleFormatting);
        }else{
        var oLocalDropModel = bContainsType ? oDropViewModel : oNoDropViewModel;
        return (
            <DropView model={oLocalDropModel}>
              <input className="attributeConditionInput"
                     type={"text"}
                     value={sValue}
                     onChange={this.handleAttributeValueChanged.bind(this, sAttrId, oAttrVal.id, sContext)}/>
            </DropView> );
        }
        break;

      case "number":
        if(oAttrVal.type != "range")
        {
          return (
              <NumberLocaleView
                  value={sValue.toString()}
                  onChange={this.handleAttributeValueNumberTypeChanged.bind(this, sAttrId, oAttrVal.id,sContext)}
                  label={""}
                  precision={iPrecision}
                  disableNumberLocaleFormatting={bDisableLocaleFormatting}
              />
          );
        }else {
          if(oAttrVal.from == null) oAttrVal.from = "";
          if(oAttrVal.to == null) oAttrVal.to = "";
          return this.getRangeFieldsView(oAttrVal, sAttrId, iPrecision, false, bDisableLocaleFormatting);
        }

        break;

      case "date":
        if(oAttrVal.type != "range"){
          sValue = sValue ? +sValue ? new Date(+sValue) : null : null;
          if (CS.isNaN(Date.parse(sValue))) {
            sValue = null;
          }

          let oStyle = {
            width: '100px',
            height: '35px'
          };
          return (<CustomDatePicker
              value={sValue}
              className="datePickerCustom"
              onChange={this.handleDateTypeAttributeValueChanged.bind(this, sAttrId, oAttrVal.id,sContext)}
              textFieldStyle={oStyle}
          />);
        }
        else {
          var sTo = oAttrVal.to;
          var sFrom = oAttrVal.from;
          sTo = sTo ? +sTo ? new Date(+sTo) : null : null;
          sFrom = sFrom ? +sFrom ? new Date(+sFrom) : null : null;
          if (CS.isNaN(Date.parse(sTo))) {
            sTo = null;
          }
          if (CS.isNaN(Date.parse(sFrom))) {
            sFrom = null;
          }
          return (<div className="attributeMultipleInputDate">
            <div className="attrDateRuleRangeContainer"><div> {getTranslation().FROM}: </div><CustomDatePicker
                value={sFrom}
                className="datePickerCustom"
                onChange={this.handleDateTypeAttributeValueChangedForRange.bind(this, sAttrId, oAttrVal.id, "from")}/>
            </div>
            <div className="attrDateRuleRangeContainer"><div> {getTranslation().TO}: </div><CustomDatePicker
                value={sTo}
                className="datePickerCustom"
                onChange={this.handleDateTypeAttributeValueChangedForRange.bind(this, sAttrId, oAttrVal.id, "to")}
                endOfDay={true}/>
            </div>
          </div>);
        }

        break;

      case 'measurementMetrics':
      case 'calculated':
        if (oAttrVal.type != "range") {
          var oMeasurementView = this.getMeasurementMetricView("VAL", sAttrId, oAttrVal, sAttributeType,
              this.handleAttributeValueNumberTypeChanged.bind(this, sAttrId, oAttrVal.id, sContext), bDisableLocaleFormatting);
          return (<div className="filterWithMeasurementMetrics">
            {oMeasurementView}
          </div>);
        } else {
          var oMeasurementToView = this.getMeasurementMetricView("TO", sAttrId, oAttrVal, sAttributeType,
              this.handleAttributeValueChangedForRange.bind(this, sAttrId, oAttrVal.id, "to"), bDisableLocaleFormatting);
          var oMeasurementFromView = this.getMeasurementMetricView("FROM", sAttrId, oAttrVal, sAttributeType,
              this.handleAttributeValueChangedForRange.bind(this, sAttrId, oAttrVal.id,  "from"), bDisableLocaleFormatting);
          return (
              <div className="attributeMultipleInput">
                <div className="attributeInputContainer">
                  <div className="attributeInputLabel"> {getTranslation().FROM}:</div>
                  {oMeasurementFromView}
                </div>
                <div className="attributeInputContainer secondContainer">
                  <div className="attributeInputLabel"> {getTranslation().TO}:</div>
                  {oMeasurementToView}
                </div>
              </div>);
        }
        break;

      default:
        return null;
    }

  };

  getRangeFieldsView (oAttrVal, sAttrId, iPrecision, bIsOnlyInteger, bDisableLocaleFormatting) {
    return (
        <div className="attributeMultipleInput">
          <div className="attributeInputContainer">
            <div className="attributeInputLabel"> {getTranslation().FROM}:</div>
            <NumberLocaleView
                value={oAttrVal.from.toString()}
                isOnlyInteger={bIsOnlyInteger}
                onBlur={this.handleAttributeValueChangedForRange.bind(this, sAttrId, oAttrVal.id, "from")}
                label={""}
                precision={iPrecision}
                disableNumberLocaleFormatting={bDisableLocaleFormatting}
            />
          </div>
          <div className="attributeInputContainer secondContainer">
            <div className="attributeInputLabel"> {getTranslation().TO}:</div>
            <NumberLocaleView
                value={oAttrVal.to.toString()}
                isOnlyInteger={bIsOnlyInteger}
                onBlur={this.handleAttributeValueChangedForRange.bind(this, sAttrId, oAttrVal.id, "to")}
                label={""}
                precision={iPrecision}
                disableNumberLocaleFormatting={bDisableLocaleFormatting}
            />
          </div>
        </div>);
  }

  handleAttributeDescriptionValueChangedInNormalization = (sAttrId, oAttrVal, sContext, oEvent) => {
    var sChangedDescriptionValue = oEvent.textValue;
    var sChangedDescriptionValueAsHtml = oEvent.htmlValue;
    EventBus.dispatch(oEvents.RULE_ATTRIBUTE_DESCRIPTION_CHANGED_IN_NORMALIZATION, this, sAttrId, oAttrVal, sContext, sChangedDescriptionValue, sChangedDescriptionValueAsHtml);
  };

  handleAttributeValueChangedInNormalization = (sAttrId, oAttrVal, sContext, oEvent) => {
    var sVal = oEvent.target.value;
    EventBus.dispatch(oEvents.RULE_ATTRIBUTE_VALUE_CHANGED_IN_NORMALIZATION, this, sAttrId, oAttrVal, sContext, sVal);
  };

  handleAttributeValueNumberTypeChangedInNormalization = (sAttrId, oAttrVal, sContext, sVal) => {
    if(sVal == null) {
      sVal = "";
    }
    EventBus.dispatch(oEvents.RULE_ATTRIBUTE_VALUE_CHANGED_IN_NORMALIZATION, this, sAttrId, oAttrVal, sContext, sVal);
  };

  handleDateTypeAttributeValueChangedInNormalization = (sAttrId, oAttrVal, sContext, sNull, sValue) => {
    sValue = new Date(sValue).getTime();
    EventBus.dispatch(oEvents.RULE_ATTRIBUTE_VALUE_CHANGED_IN_NORMALIZATION, this, sAttrId, oAttrVal, sContext, sValue);
  };

  getAttributeInputViewByAttributeTypeForEffect = (
    sType,
    sAttrId,
    oAttrVal,
    sAttributeType,
    sDefaultUnit,
    sContext,
    iPrecision,
  ) => {
    var oNormalization = oAttrVal.normalization;
    var sValue = oNormalization.values[0];

    if(sContext == "attribute") {
      var sTypeForVisual = ViewUtils.getAttributeTypeForVisual(sAttributeType, sAttrId);
      let oMasterAttribute = this.getAttributeByAttributeId(sAttrId);
      let bDisableLocaleFormatting = oMasterAttribute.hideSeparator;

      switch (sTypeForVisual) {
        case "html":
        case "text":
          if(sType == "substring"){
            if(oAttrVal.normalization.rules.from == null) oAttrVal.normalization.rules.from = "";
            if(oAttrVal.normalization.rules.to == null) oAttrVal.normalization.rules.to = "";
            return (
                <div className="attributeMultipleInput">
                  <div className="attributeInputContainer">
                    <div className="attributeInputLabel"> {getTranslation().FROM}: </div>
                    <NumberLocaleView
                        value={oAttrVal.normalization.rules.from}
                        isOnlyInteger={true}
                        onChange={this.handleAttributeValueChangedForRangeInNormalization.bind(this, sAttrId, oAttrVal.id, sType, 'from')}
                        label={""}
                        precision={iPrecision}
                    />
                  </div>
                  <div className="attributeInputContainer secondContainer">
                    <div className="attributeInputLabel"> {getTranslation().TO}: </div>
                    <NumberLocaleView
                        value={oAttrVal.normalization.rules.to}
                        isOnlyInteger={true}
                        onChange={this.handleAttributeValueChangedForRangeInNormalization.bind(this, sAttrId, oAttrVal.id, sType, 'to')}
                        label={""}
                        precision={iPrecision}
                    />
                  </div>
                </div>);
          }else if(sType == "replace"){
            if(oAttrVal.normalization.rules.from == null) oAttrVal.normalization.rules.from = "";
            if(oAttrVal.normalization.rules.to == null) oAttrVal.normalization.rules.to = "";
            return (
                <div className="attributeMultipleInput">
                  <div className="attributeInputContainer effectContainer">
                    <div className="attributeInputLabel"> {getTranslation().FIND}: </div>
                    <input className="attributeConditionInput"
                           type={"text"}
                           value={oAttrVal.normalization.rules.from}
                           onChange={this.handleAttributeValueChangedForRangeInNormalization.bind(this, sAttrId, oAttrVal.id, sType, 'from')}/>
                  </div>
                  <div className="attributeInputContainer effectContainer">
                    <div className="attributeInputLabel"> {getTranslation().REPLACE}: </div>
                    <input className="attributeConditionInput"
                           type={"text"}
                           value={oAttrVal.normalization.rules.to}
                           onChange={this.handleAttributeValueChangedForRangeInNormalization.bind(this, sAttrId, oAttrVal.id, sType, 'to')}/>
                  </div>
                </div>);
          } else{
            if(sTypeForVisual == 'html') {
                let sValueAsHtml = oNormalization.valueAsHTML;
                return (<FroalaWrapperView className="attributeDescriptionTextArea"
                                         dataId={sAttrId}
                                         activeFroalaId={sAttrId}
                                         content={sValueAsHtml}
                                         handler={this.handleAttributeDescriptionValueChangedInNormalization.bind(this, sAttrId, oAttrVal, sContext)}
                  />
              )
            }else {
              return (
                  <input className="attributeConditionInput"
                         type={"text"}
                         value={sValue}
                         onChange={this.handleAttributeValueChangedInNormalization.bind(this, sAttrId, oAttrVal, sContext)}/>);
            }
          }

        case "number":
          sValue = sValue || "";
          return (
              <NumberLocaleView
                  value={sValue.toString()}
                  onChange={this.handleAttributeValueNumberTypeChangedInNormalization.bind(this, sAttrId, oAttrVal, sContext)}
                  label={""}
                  precision={iPrecision}
                  disableNumberLocaleFormatting={bDisableLocaleFormatting}
              />
          );

        case "date":
          sValue = sValue ? +sValue ? new Date(+sValue) : null : null;
          if (CS.isNaN(Date.parse(sValue))) {
            sValue = null;
          }
          return (<CustomDatePicker
              value={sValue}
              className="datePickerCustom"
              onChange={this.handleDateTypeAttributeValueChangedInNormalization.bind(this, sAttrId, oAttrVal, sContext)}/>);

        case 'measurementMetrics':
          return this.getMeasurementMetricView("VAL", sAttrId, oAttrVal, sAttributeType,
              this.handleAttributeValueNumberTypeChangedInNormalization.bind(this, sAttrId, oAttrVal, sContext),
              bDisableLocaleFormatting);

        case 'calculated':
          return this.getEffectViewForCalculatedAttribute(sAttrId, oAttrVal);

        default:
          return null;
      }
    } else if(sContext == "role") {
      return this.getUserValueViewsForNormalization(sAttrId, oAttrVal,sContext);
    } else if(sContext == "tag"){
      return this.getTagGroupViewForNormalization(oAttrVal, sAttrId);
    }
  };

  getRemoveBlackListIcon = (sDropId) => {
    return <div className="removeAttributeClassIcon" onClick={this.handleRemoveBlackListIconClicked.bind(this, sDropId)}></div>
  };

  getAttributeBlackListViewByAttributeType = (sAttrId, oAttrVal, aBlackListWords, sBlackListRuleLabel, bIsDisabled) => {
    var sSplitter = ViewUtils.getSplitter();
    var _this = this;

    var oDropViewProperties = {};
    var sContext = "attributes";
    var sDropId = sContext + sSplitter + sAttrId + sSplitter + oAttrVal.id;
    var oDropViewModel = new DropViewModel(sDropId, true, ["ruleList"], oDropViewProperties);

    return (<DropView model={oDropViewModel}>
      {_this.getBlackListView(aBlackListWords, sBlackListRuleLabel, bIsDisabled)}
      {_this.getRemoveBlackListIcon(sDropId)}
    </DropView>);
  };

  getRelationshipInputView = (sAttrId, oAttrVal, sContext) => {
    var sValue = oAttrVal.values[0];
    if(this.props.screenContext == "condition") {
      sValue = oAttrVal.values[0] || '';
    }
    return (
        <NumberLocaleView
            value={sValue}
            onChange={this.handleAttributeValueNumberTypeChanged.bind(this, sAttrId, oAttrVal.id,sContext)}
            label={""}
            isOnlyInteger= {true}
        />
    );
  };

  getAttributeColorView = (sSelectedColor, sAttrId, oAttrVal, sContext) => {
    var __props = this.props;
    if(__props.screenContext == "condition") {
      return null;
    }

    var aOptions = [];
    var that = this;

    CS.forEach(ColorConstantDictionary, function(sColor, sKey){
      var sColorLabel = getTranslation()[sKey] || sKey;
      var sSelectedClass = "";
      if(sSelectedColor == sColor) {
        sSelectedClass = "selected";
      }
      aOptions.push(<span
                      key={sKey}
                      onClick={that.handleAttributeColorChanged.bind(that, sAttrId, oAttrVal.id, sColor, sContext)}
                      title={sColorLabel}
                      className={sColor+"Color commonColorWrapper "+sSelectedClass}></span>)
    });

    return (<div className="attributeColorDropDown">{aOptions}</div>);
  };

  getRelationshipTypeView = (sType, sAttrId, sValId, sContext) => {

    var aOptions = [];
    var oCurrentTypeList = [];
    if(sContext == "relationship"){
      oCurrentTypeList = RuleConstantDictionary.RELATIONSHIP.TYPE;
    }else if(sContext == "role"){
      oCurrentTypeList = RuleConstantDictionary.ROLE.TYPE;
    }else if(sContext == "type"){
      oCurrentTypeList = RuleConstantDictionary.TYPE.TYPE;
    } else if (sContext == "tag") {
      oCurrentTypeList = RuleConstantDictionary.TAG.TYPE;
    }

    var oRuleConstantTypes = new FilterTypeDictionary.RULE_CONSTANTS_TYPES();
    CS.forEach(oCurrentTypeList, function(sValue, sKey){
      var sLabel = oRuleConstantTypes[sValue] || sKey;
      aOptions.push(<option value={sValue} key={sKey}>{sLabel}</option>)
    });

    return (<select value={sType} onChange={this.handleAttributeValTypeDropdownChanged.bind(this, sAttrId,sValId,sContext, true)}>{aOptions}</select>);
  };

  getAttributeDescriptionView = (sDescription, sAttrId, oAttrVal, sContext) => {
    var __props = this.props;
    if(__props.screenContext == "condition") {
      return null;
    }

    return (<textarea className="attributeDescriptionTextArea"
                      value={sDescription}
                      placeholder={getTranslation().DESCRIPTION}
                      onChange={this.handleAttributeDescriptionValueChanged.bind(this, sAttrId, oAttrVal.id,sContext)}/>);
  };

  handleAttributeDescriptionValueChanged = (sAttrId, oAttrValId, sContext, oEvent) => {
    var sScreenContext = this.props.screenContext;
    var sChangedDescriptionValue = oEvent.target.value;
    EventBus.dispatch(oEvents.RULE_ATTRIBUTE_DESCRIPTION_CHANGED, this, sAttrId,oAttrValId, sChangedDescriptionValue, sContext, sScreenContext);
  };

  handleAttributeColorChanged = (sAttrId, oAttrValId, sChangedColorId, sContext) => {
    var sScreenContext = this.props.screenContext;
    EventBus.dispatch(oEvents.RULE_ATTRIBUTE_COLOR_CHANGED, this, sAttrId,oAttrValId, sChangedColorId,sContext, sScreenContext);
  };

  handleAttributeVisibilityButtonClicked = (sAttrId, oAttrVal) => {
    EventBus.dispatch(oEvents.ATTRIBUTE_VISIBILITY_BUTTON_CLICKED, this, sAttrId, oAttrVal, this.props.screenContext);
  };

  handleCompareWithSystemDateButtonClicked = (sAttrId, oAttrVal) => {
    let sContext = this.props.context;
    let sRuleId = this.props.activeRule.id;
    sContext = CS.isEmpty(sContext) ? this: sContext;
    EventBus.dispatch(oEvents.COMPARE_WITH_SYSTEM_DATE_BUTTON_CLICKED, sContext, sRuleId, sAttrId, oAttrVal);
  };

  handleAttributeViewTypeChanged = (sAttrId, oAttrVal, sContext, sRuleSectionContext, aSelectedValues, oReferencedAttributes) => {
    var sScreenContext = this.props.screenContext;

    /** If transformation type is attribute value & attribute for mapping is not selected then set it to "0" **/
    var sSelectedAttrId = aSelectedValues && aSelectedValues[0] || "0";
    let oExtraData = {
      context: sContext,
      screenContext: sScreenContext,
      ruleSectionContext: sRuleSectionContext
    };
    EventBus.dispatch(oEvents.HANDLE_ATTRIBUTE_VIEW_TYPE_CHANGED, this, sAttrId, oAttrVal, sSelectedAttrId, oReferencedAttributes, oExtraData);
  };

  getDropdownViewForAttributeLink = (sAttrId, oAttrVal, sContext) => {
    let oActiveRule = this.props.activeRule;
    let oConfigDetails = oActiveRule.configDetails;
    let oAttribute = this.getAttributeByAttributeId(sAttrId);
    let aTypes = !CS.isEmpty(oAttribute.type) ? [oAttribute.type] : [];
    let oReqResObj = {
      requestType: "configData",
      entityName: "attributes",
      types: aTypes
    };

    oReqResObj.customRequestModel = this.getCustomRequestModelForAttributes(oActiveRule, oAttribute.isTranslatable, true);

    let oReferencedData = oConfigDetails.referencedAttributes;

    return (<LazyMSSView
        context={sContext}
        referencedData={oReferencedData}
        requestResponseInfo={oReqResObj}
        selectedItems={[oAttrVal.attributeLinkId]}
        excludedItems={[sAttrId]}
        isMultiSelect={false}
        bShowIcon={false}
        onApply={this.handleAttributeViewTypeChanged.bind(this, sAttrId, oAttrVal, sContext, "ruleCause")}
    />);
  };

  getCustomRequestModelForAttributes = (oActiveRule, bIsTranslatableAttribute, bIsForAttributeLink) => {
    let oCustomRequestModel = {};
    if(oActiveRule.isLanguageDependent) {
      /**
       * For dependent rule =>
       * If dependent attribute with toggle condition is selected in cause, both attribute can be linked to it i.e. dependent or independent
       * If independent attribute with toggle condition is selected in cause, only independent attribute can be linked to it
       */
      if(bIsForAttributeLink) {
        !bIsTranslatableAttribute && (oCustomRequestModel.isLanguageIndependent = true);
      } else {
        /** For Language Dependent Rule : To get only language dependent attributes**/
        oCustomRequestModel.isLanguageDependent = true;
      }

    } else {
      /** For Language Independent Rule : To get only language independent**/
      oCustomRequestModel.isLanguageIndependent = true;
    }

    return oCustomRequestModel;
  };

  getDropdownViewForAttributeInRuleEffect = (sAttrId, oAttrVal, sContext) => {
    let oActiveRule = this.props.activeRule;
    let oConfigDetails = oActiveRule.configDetails;
    let oAttribute = this.getAttributeByAttributeId(sAttrId);
    let aTypes = ViewUtils.getAllowedAttributeTypesForRuleEffect(oAttribute);
    let oReqResObj = {
      requestType: "configData",
      entityName: "attributes",
      types: aTypes
    };

    oReqResObj.customRequestModel = this.getCustomRequestModelForAttributes(oActiveRule);

    let oReferencedData = oConfigDetails.referencedAttributes;
    let oNormalization = oAttrVal.normalization;

    return (<LazyMSSView
        context={sContext}
        referencedData={oReferencedData}
        requestResponseInfo={oReqResObj}
        selectedItems={[oNormalization.valueAttributeId]}
        excludedItems={[sAttrId]}
        isMultiSelect={false}
        bShowIcon={false}
        onApply={this.handleAttributeViewTypeChanged.bind(this, sAttrId, oAttrVal, sContext, "ruleEffect")}
    />);
  };

  getValueViews = (aAttrVals, sAttrId, sAttributeType, sContext, sTagType) => {
    var _this = this;
    var __props = _this.props;
    var aMasterRuleList = __props.ruleListDetailData;
    var sSplitter = ViewUtils.getSplitter();
    var sScreenContext = __props.screenContext;
    var aViews = [];

    CS.forEach(aAttrVals, function (oAttrVal, iIndex) {

      var sType = oAttrVal.type;
      var sDefaultUnit = "";

      var oSelectView ="";
      var bIsEmptyNonEmpty = (sType == "empty" || sType == "notempty");
      var oAttributeInputView = "";

      var bIsAttributeSelected = false;
      var bIsCompareWithSystemDate = oAttrVal.shouldCompareWithSystemDate || false;

      if(sContext == "attribute") {
        var oMasterAttribute = _this.getAttributeByAttributeId(sAttrId);
        var iPrecision = oMasterAttribute.precision;
        oSelectView = _this.getTypeDropdownView(sType, sAttrId, oAttrVal.id,sContext, true);
        var sLinkId = oAttrVal.ruleListLinkId;
        if(sScreenContext == "condition") {
          sLinkId = oAttrVal.listId;
        }
        if(CS.isEmpty(sLinkId)){
          if(!CS.isEmpty(oAttrVal.attributeLinkId)) {
            bIsAttributeSelected = true;
            oAttributeInputView = _this.getDropdownViewForAttributeLink(sAttrId, oAttrVal, sContext);
          } else {
            if(!bIsCompareWithSystemDate) {
              oAttributeInputView = bIsEmptyNonEmpty ? null :
                                    _this.getAttributeInputViewByAttributeType(sType, sAttrId, oAttrVal,
                                        sAttributeType, sDefaultUnit, sContext, iPrecision, oMasterAttribute.hideSeparator);
            } else {
              oAttributeInputView = (<div className="compareWithSystemDate">{getTranslation().TODAY}</div> )
            }
          }
        }else {
          var oMasterRuleList = CS.find(aMasterRuleList, {id: sLinkId});
          if(oMasterRuleList) {
            var aBlackListWords = oMasterRuleList.list;
            var sBlackListRuleLabel = CS.getLabelOrCode(oMasterRuleList);
            oAttributeInputView = (<div className="blackListContainer">{_this.getAttributeBlackListViewByAttributeType(sAttrId, oAttrVal, aBlackListWords, sBlackListRuleLabel, true)}</div>);
          }
        }
      }
      else if(sContext == "relationship"){
        oSelectView = _this.getRelationshipTypeView(sType, sAttrId, oAttrVal.id,sContext);
        oAttributeInputView = _this.getRelationshipInputView(sAttrId, oAttrVal,sContext);
      }
      else if(sContext == "role"){
        oSelectView = _this.getRelationshipTypeView(sType, sAttrId, oAttrVal.id,sContext);
        oAttributeInputView = bIsEmptyNonEmpty ? null : _this.getUserValueViews(sAttrId, oAttrVal,sContext);
      }
      else if(sContext == "type"){
        oSelectView = _this.getRelationshipTypeView(sType, sAttrId, oAttrVal.id,sContext);
      }
      else if(sContext == "tag"){
        oSelectView = _this.getRelationshipTypeView(sType, sAttrId, oAttrVal.id,sContext);
        var sContextAppend = "ruleFilterTagsTagValues" + sSplitter + oAttrVal.id + sSplitter + sScreenContext;
        var sTagContext = "contentFilterTagsInner" + sSplitter + sContextAppend;
        var oExtraData = {
          outerContext: "contentFilterTagsInner",
          innerContext: "ruleFilterTagsTagValues",
          attributeValueId: oAttrVal.id,
          context: sTagContext,
          screenContext: __props.screenContext
        };

        oAttributeInputView = bIsEmptyNonEmpty ? null : _this.getTagGroupView(oAttrVal, sAttrId, oExtraData);
      }

      var sSelectedClass = bIsAttributeSelected ? 'selected ': "";
      var sAttributeLinkingClass = bIsEmptyNonEmpty ? 'hideMe ': "";
      if(sType == "range" || sType == "length_range" || sType == "regex") {
        sAttributeLinkingClass += 'hideMe';
      }
      var oAttributeConditionView = sTagType == TagTypeConstants.TAG_TYPE_BOOLEAN ? null : (
          <div className="attributeConditionSelect">{oSelectView}</div>);
      var oAttributeConditionDeleteView = sTagType == TagTypeConstants.TAG_TYPE_BOOLEAN ? null : (
          <div className="attributeConditionDelete"
               title={getTranslation().DELETE}
               onClick={_this.handleAttributeValueDeleteClicked.bind(_this, sAttrId, oAttrVal.id, sContext)}></div>);

      var sTypeForVisual = ViewUtils.getAttributeTypeForVisual(sAttributeType, sAttrId);
      var sCompareWithSystemDateSelectedClass = bIsCompareWithSystemDate ? 'selected ': "";
      aViews.push(<div className="attributeConditionWrapper" key={iIndex}>
        <div className="attributeConditionRow" key="1">
          {oAttributeConditionView}
          <div className="attributeViewWrapper">{oAttributeInputView}</div>
          {(sContext == "attribute") ? (<div
              className={"attributeVisibilityHandlerWrapper " + sAttributeLinkingClass}>
            <div className={"attributeVisibilityHandler " + sSelectedClass}
                 title={getTranslation().TOGGLE_COMPARISON_WITH_ATTRIBUTE}
                 onClick={_this.handleAttributeVisibilityButtonClicked.bind(_this, sAttrId, oAttrVal, "ruleCause")}>{getTranslation().A}</div>
            {sTypeForVisual === "date"?(<div className={"compareWithSystemDateHandler " + sCompareWithSystemDateSelectedClass}
                 title={getTranslation().TODAY}
                 onClick={_this.handleCompareWithSystemDateButtonClicked.bind(_this, sAttrId, oAttrVal)}></div>): null}
          </div>): null}
        </div>
        {aAttrVals.length == 1 ? "" : oAttributeConditionDeleteView}
      </div>)
    });

    return aViews;
  };

  handleConcatenatedFormulaChanged = (oNormalization, aAttributeConcatenatedList, oReferencedData) => {
    EventBus.dispatch(oEvents.RULE_DETAIL_CONCATENATED_FORMULA_CHANGED, oNormalization, aAttributeConcatenatedList, oReferencedData);
  };

  getConcatenatedFormulaView = (oNormalization, oAttribute) => {
    let oActiveRule = this.props.activeRule;
    let oReferencedData = oActiveRule.configDetails;
    let aAllowedEntities = [];
    let oRequestResponseInfoForAttributes = {
      requestType: "configData",
      entityName: ConfigDataEntitiesDictionary.ATTRIBUTES,
      types: ViewUtils.getAllowedAttributeTypesForRuleEffect(oAttribute)
    };
    let bIsLanguageIndependent = !oActiveRule.isLanguageDependent;
    if (bIsLanguageIndependent){
      oRequestResponseInfoForAttributes.customRequestModel = {isLanguageIndependent : true}
    }

    let sAttributeType = oAttribute.type;
    let bIsHTMLAttribute = ViewUtils.isAttributeTypeHtml(sAttributeType);
    let bIsTextAttribute = ViewUtils.isAttributeTypeText(sAttributeType);
    if(bIsHTMLAttribute) {
      aAllowedEntities = [ConcatenatedEntityTypes.html, ConcatenatedEntityTypes.attribute];
    } else if (bIsTextAttribute) {
      aAllowedEntities = [ConcatenatedEntityTypes.text, ConcatenatedEntityTypes.attribute];
    }

    return (
        <ConcatenatedFormulaView
            attributeConcatenatedList={oNormalization.attributeConcatenatedList}
            referencedData={oReferencedData}
            allowedEntities={aAllowedEntities}
            attributesToExclude={[oAttribute.id]}
            requestResponseInfoForAttributes={oRequestResponseInfoForAttributes}
            onChange={this.handleConcatenatedFormulaChanged.bind(this, oNormalization)}/>
    )
  };

  getAttrValueEffectViews =(aAttrVals, sAttrId, sAttributeType, sContext, sTypeOfRule) => {
    var _this = this;
    var aViews = [];
    var oAttributeMap = ViewUtils.getAttributeList();
    let oRuleTypeDictionary = new RuleTypeDictionary();

    CS.forEach(aAttrVals, function (oAttrVal, iIndex) {
      var sType = oAttrVal.type;
      var oRules = oAttrVal.normalization.rules;
      var sRuleType;
      if(oRules){
        sRuleType = oRules.type;
      }else {
        sRuleType = sContext === 'tag' ? 'replacewith' : 'value';
      }

      var isAttributeViewAvailable = (sRuleType == 'uppercase'|| sRuleType == 'trim' || sRuleType == 'propercase' || sRuleType == 'lowercase');
      var iPrecision = 0;
      if(sContext == "attribute") {
        var oMasterAttribute = _this.getAttributeByAttributeId(sAttrId);
        iPrecision = oMasterAttribute.precision;
      }
      var bIsEmptyNonEmpty = (sType == "empty" || sType == "notempty");
      var oRuleViolation = oAttrVal.ruleViolation;
      var sColor = oRuleViolation.color;
      var sDescription = oRuleViolation.description;
      var oAttributeColorView = _this.getAttributeColorView(sColor, sAttrId, oAttrVal, sContext);
      var oAttributeDescriptionView = _this.getAttributeDescriptionView(sDescription, sAttrId, oAttrVal, sContext);
      var oAttributeNormalizedFieldView = isAttributeViewAvailable ? null : _this.getAttributeInputViewByAttributeTypeForEffect(sRuleType, sAttrId, oAttrVal, sAttributeType, "", sContext, iPrecision);

      var oSelectView = _this.getTypeDropdownView(sRuleType, sAttrId, oAttrVal.id,sContext, false, sTypeOfRule);

      let oViewToRender = [];
      switch (sTypeOfRule) {
        case oRuleTypeDictionary.VIOLATION_RULE:
          oViewToRender.push(<div className="attributeConditionLeft" key={iIndex}>
            <div className="ruleViolationColorWrapper">{oAttributeColorView}</div>
            <div className="ruleViolationDescriptionWrapper">{oAttributeDescriptionView}</div>
          </div>);
          break;
        case oRuleTypeDictionary.STANDARDIZATION_AND_NORMALIZATION_RULE:
          let oNormalization = oAttrVal.normalization;
          if(oNormalization.transformationType === "attributeValue") {
            oAttributeNormalizedFieldView = _this.getDropdownViewForAttributeInRuleEffect(sAttrId, oAttrVal, sContext);
          } else if(oNormalization.transformationType === "concat") {
            oAttributeNormalizedFieldView = _this.getConcatenatedFormulaView(oNormalization, oAttributeMap[sAttrId]);
          }
          let oAttributeConditionView = oSelectView !== null? (<div className="attributeConditionSelect" key={"attributeConditionSelect"+iIndex}>{oSelectView}</div>):null;
          oViewToRender.push(oAttributeConditionView);
          oViewToRender.push(<div className="attributeConditionRight" key={iIndex}>
            {oAttributeNormalizedFieldView}
          </div>);
          break;
      }


      aViews.push(<div className="attributeConditionWrapper" key={iIndex}>
        <div className="attributeConditionRow" key="2">
          {oViewToRender}
        </div>
      </div>)
    });

    return aViews;
  };

  handleRemoveBlackListIconClicked = (sDropId) => {
    var sScreenContext = this.props.screenContext;
    EventBus.dispatch(oEvents.REMOVE_BLACKLIST_ICON_CLICKED, this, sDropId, sScreenContext);
  };

  handleAttributeValTypeDropdownChanged = (sAttrId, sValId, sContext, isCause, oEvent) => {
    var sScreenContext = this.props.screenContext;
    var sTypeId = oEvent.target.value;
    EventBus.dispatch(oEvents.RULE_ATTRIBUTE_VALUE_TYPE_CLICKED, this, sAttrId, sValId, sTypeId,sContext, sScreenContext, isCause);
  };

  handleFilterElementDeleteClicked = (sElId, sContext, sHandlerContext) => {
    var sScreenContext = this.props.screenContext;
    EventBus.dispatch(oEvents.RULE_ELEMENT_DELETE_CLICKED, this, sElId, sContext, sScreenContext, sHandlerContext);
  };

  handleFilterElementExpandClicked = (sAttrId, sContext) => {
    var sScreenContext = this.props.screenContext;
    EventBus.dispatch(oEvents.CONTENT_FILTER_ATTRIBUTE_EXPAND_CLICKED, this, sAttrId, sContext, sScreenContext);
  };

  handleAddAttributeValueClicked = (sAttrId, sContext) => {
    var sScreenContext = this.props.screenContext;
    EventBus.dispatch(oEvents.RULE_ADD_ATTRIBUTE_CLICKED, this, sAttrId,sContext, sScreenContext);
  };

  handleAttributeValueChanged = (sAttrId, sValId, sContext, oEvent) => {
    var sScreenContext = this.props.screenContext;
    var sVal = oEvent.target.value;
    EventBus.dispatch(oEvents.RULE_ATTRIBUTE_VALUE_CHANGED, this, sAttrId, sValId, sVal,sContext, sScreenContext);
  };

  handleAttributeValueNumberTypeChanged = (sAttrId, sValId, sContext, sVal) => {
    var sScreenContext = this.props.screenContext;
    if(sVal == null) {
      sVal = "";
    }
    EventBus.dispatch(oEvents.RULE_ATTRIBUTE_VALUE_CHANGED, this, sAttrId, sValId, sVal,sContext, sScreenContext);
  };

  handleAttributeValueChangedForRange = (sAttrId, sValId, sRange, sVal) => {
    var sScreenContext = this.props.screenContext;
    if(sVal == null) {
      sVal = "";
    }
    EventBus.dispatch(oEvents.RULE_ATTRIBUTE_VALUE_CHANGED_FOR_RANGE, this, sAttrId, sValId, sVal, sRange, sScreenContext);
  };

  handleAttributeValueChangedForRangeInNormalization = (sAttrId, sValId, sType, sRange, oEvent) => {
    var sVal = CS.isObject(oEvent) ?  oEvent.target.value : oEvent;
    EventBus.dispatch(oEvents.RULE_ATTRIBUTE_VALUE_CHANGED_FOR_RANGE_IN_NORMALIZATION, this, sAttrId, sValId, sVal, sType, sRange);
  };

  handleDateTypeAttributeValueChanged = (sAttrId, sValId, sContext, sNull, sValue) => {
    var sScreenContext = this.props.screenContext;
    sValue = new Date(sValue).getTime();
    EventBus.dispatch(oEvents.RULE_ATTRIBUTE_VALUE_CHANGED, this, sAttrId, sValId, sValue,sContext, sScreenContext);
  };

  handleDateTypeAttributeValueChangedForRange = (sAttrId, sValId, sRange, sNull, sValue) => {
    var sScreenContext = this.props.screenContext;
    sValue = new Date(sValue).getTime();
    EventBus.dispatch(oEvents.RULE_ATTRIBUTE_VALUE_CHANGED_FOR_RANGE, this, sAttrId, sValId, sValue, sRange, sScreenContext);
  };

  handleAttributeValueDeleteClicked = (sAttrId, sValId, sContext, oEvent) => {
    var sScreenContext = this.props.screenContext;
    EventBus.dispatch(oEvents.RULE_ATTRIBUTE_VALUE_DELETE_CLICKED, this, sAttrId, sValId,sContext, sScreenContext);
  };

  handleMSSLoadMoreClicked = (sContext) => {
    EventBus.dispatch(oEvents.RULE_DETAILS_MSS_LOAD_MORE_CLICKED, sContext);
  };

  handleMSSSearchClicked = (sContext, sSearchText) => {
    EventBus.dispatch(oEvents.RULE_DETAILS_MSS_SEARCH_CLICKED, sContext, sSearchText);
  };

  getAttributeFilterView = () => {
    var _this = this;
    var __props = _this.props;
    var oActiveRule = __props.activeRule;
    var aSelectedAttributes = oActiveRule.attributes;
    let aAttributeViews = [];
    let sSplitter = ViewUtils.getSplitter();
    let  sContext = `ruleDetailView${sSplitter}attributes`;
    let oConfigDetails = oActiveRule.configDetails;
    let requestResponseData = {
      entityName:"attributes",
      requestType:"configData",
      typesToExclude: []
    };

    /** For Language independent rule : To get only language independent attributes in cause section **/
    /** For Language dependent rule : No need to add "isLanguageDependent" key **/
    /** In case of KPI rule don't have isLanguageDependent key **/
    if(CS.has(oActiveRule, "isLanguageDependent") && !oActiveRule.isLanguageDependent) {
      requestResponseData.customRequestModel = {
        isLanguageIndependent: true
      }
    }

    let oReferencedAttributes = oConfigDetails.referencedAttributes;
    let oMSSData = ViewUtils.getLazyMSSViewModel(aSelectedAttributes, oReferencedAttributes, sContext, requestResponseData, false);
    oMSSData.onApply = this.handleAttributeCauseEffectMSSValueChanged.bind(this, "CAUSE");
    var oMSSView = this.getMSSView(oMSSData);

    var sScreenContext = __props.screenContext;
    CS.forEach(aSelectedAttributes, function (oAttribute) {
      var sAttributeId = oAttribute.entityId;
      var aAttributeRules = oAttribute.rules;
      if(sScreenContext == "condition") {
        aAttributeRules = oAttribute.conditions;
      }
      var oMasterAttribute = _this.getAttributeByAttributeId(sAttributeId);
      var aValueViews = _this.getValueViews(aAttributeRules, sAttributeId, oMasterAttribute.type,"attribute");

      var sAttributeBodyClass = "attributeBody ";
      sAttributeBodyClass += "expanded ";

      aAttributeViews.push(<div className="attributeWrapper" key={sAttributeId}>
        <div className="attributeHeader">
          <div className="attributeText" onClick={_this.handleFilterElementExpandClicked.bind(_this, sAttributeId, 'attribute')}>
            {CS.getLabelOrCode(oMasterAttribute)}
          </div>
          <div className="attributeDelete"
               title={getTranslation().DELETE}
               onClick={_this.handleFilterElementDeleteClicked.bind(_this, sAttributeId, 'attribute')}></div>
          <div className="attributeAddCondition"
               title={getTranslation().ADD}
               onClick={_this.handleAddAttributeValueClicked.bind(_this, sAttributeId,"attribute")}></div>
        </div>
        <div className={sAttributeBodyClass}>
          <div className="attributeValueWrapper">
            <div className="attributeValueWrapperBody">{aValueViews}</div>
          </div>
        </div>
      </div>);
    });

    return (
        <div className="attributeFilterViewWrapper innerFilterViewWrapper">
          <div className="attributeFilterViewHeader innerFilterViewHeader">
            <div className="attributeFilterViewHeaderLeft">
              <div className="attributeFilterViewHeaderIcon innerFilterViewHeaderIcon"></div>
              <div className="attributeFilterViewHeaderText innerFilterViewHeaderText">{getTranslation().ATTRIBUTES}</div>
            </div>
            <div className="attributeFilterViewHeaderRight">
              <div className="attributeFilterSearchContainer innerFilterSearchContainer">{oMSSView}</div>
            </div>
          </div>
          <div className="attributeFilterViewBody innerFilterViewBody">
            <div className="attributeFilterSearchContainer innerFilterAttributeViewsContainer">{aAttributeViews}</div>
          </div>
        </div>
    )
  };

  getAttributeFilterEffectView =(sRuleType) =>{
    var _this = this;
    var __props = _this.props;
    var aRuleEffect = this.props.ruleEffect;
    var sMssContext = 'EFFECT';
    var aSelectedAttributes = CS.filter(aRuleEffect, {type: 'attribute'});
    var aAttributeViews = [];
    let oActiveRule = __props.activeRule;
    let sSplitter = ViewUtils.getSplitter();
    let  sContext = `ruleDetailView${sSplitter}attributes`;
    let oConfigDetails = oActiveRule.configDetails;
    let oRuleTypeDictionary = new RuleTypeDictionary();
    let aTypesToExclude = (oRuleTypeDictionary.STANDARDIZATION_AND_NORMALIZATION_RULE === sRuleType) ?
                          NonEditableAttributeTypeDictionary : [];

    let requestResponseData = {
      entityName:"attributes",
      requestType:"configData",
      typesToExclude: aTypesToExclude
    };

    requestResponseData.customRequestModel = this.getCustomRequestModelForAttributes(oActiveRule);
    let oReferencedAttributes = oConfigDetails.referencedAttributes;
    let aExcludedItems = CS.map(aSelectedAttributes, "entityId");
    let oMSSData = ViewUtils.getLazyMSSViewModel(aSelectedAttributes, oReferencedAttributes, sContext, requestResponseData, false, aExcludedItems);

    oMSSData.onApply = this.handleAttributeCauseEffectMSSValueChanged.bind(this, "EFFECT");
    var oMSSView = this.getMSSView(oMSSData);

    CS.forEach(aSelectedAttributes, function (oAttribute) {
      var sAttributeId = oAttribute.entityId;
      var oMasterAttribute = _this.getAttributeByAttributeId(sAttributeId);//oAttributeMap[sAttributeId];
      var aValueViews = _this.getAttrValueEffectViews([oAttribute], sAttributeId, oMasterAttribute.type,"attribute", sRuleType);
      var sAttributeBodyClass = "attributeBody ";
      sAttributeBodyClass += "expanded ";

      aAttributeViews.push(<div className="attributeWrapper" key={sAttributeId}>
        <div className="attributeHeader">
          <div className="attributeText" onClick={_this.handleFilterElementExpandClicked.bind(_this, sAttributeId, 'attribute')}>
            {CS.getLabelOrCode(oMasterAttribute)}
          </div>
          <div className="attributeEffectToggleWrapper">
          </div>
          <div className="attributeDelete"
               title={getTranslation().DELETE}
               onClick={_this.handleFilterElementDeleteClicked.bind(_this, oAttribute.id, 'attribute', sMssContext)}></div>
        </div>
        <div className={sAttributeBodyClass}>
          <div className="attributeValueWrapper">
            <div className="attributeValueWrapperBody">{aValueViews}</div>
          </div>
        </div>
      </div>);
    });

    return (
        <div className="attributeFilterViewWrapper innerFilterViewWrapper">
          <div className="attributeFilterViewHeader innerFilterViewHeader">
            <div className="attributeFilterViewHeaderLeft">
              <div className="attributeFilterViewHeaderIcon innerFilterViewHeaderIcon"></div>
              <div className="attributeFilterViewHeaderText innerFilterViewHeaderText">{getTranslation().ATTRIBUTES}</div>
            </div>
            <div className="attributeFilterViewHeaderRight">
              <div className="attributeFilterSearchContainer innerFilterSearchContainer">{oMSSView}</div>
            </div>
          </div>
          <div className="attributeFilterViewBody innerFilterViewBody">
            <div className="attributeFilterSearchContainer innerFilterAttributeViewsContainer">{aAttributeViews}</div>
          </div>
        </div>
    )
  };

  getRoleFilterView = () => {
    var _this = this;
    var __props = _this.props;
    var oActiveRule = __props.activeRule;
    var aSelectedRoles = oActiveRule.roles;
    var aRoleForMSS = _this.getRoleForMSS();
    var sScreenContext = __props.screenContext;
    var aRoleViews = [];
    let sSearchText = ViewUtils.getEntityVsSearchTextMapping()["roles"];
    var sSplitter = ViewUtils.getSplitter();
    let sContext = `ruleDetailView${sSplitter}roles`;
    var fOnApply = this.handleRoleCauseEffectMSSValueChanged.bind(this, "CAUSE");
    var oMSSData = {
      context: sContext,
      isDisabled: false,
      items: aRoleForMSS,
      selectedItems: [],
      isMultiSelect: false,
      onApply: fOnApply,
      loadMoreHandler: this.handleMSSLoadMoreClicked.bind(this, "roles"),
      searchHandler: this.handleMSSSearchClicked.bind(this, "roles"),
      isLoadMoreEnabled: true,
      searchText: sSearchText
    };

    var oMSSView = this.getMSSView(oMSSData);

    CS.forEach(aSelectedRoles, function (oRole) {
      var sRoleId = oRole.entityId;
      var aRoleRules = oRole.rules;
      if(sScreenContext == "condition") {
        aRoleRules = oRole.conditions;
      }
      var oMasterRelationship = _this.getRoleByRoleId(sRoleId);//oRoleMap[sRoleId];
      var aValueViews = _this.getValueViews(aRoleRules, sRoleId, oMasterRelationship.type,"role");
      var sAttributeBodyClass = "attributeBody ";
      sAttributeBodyClass += "expanded ";

      aRoleViews.push(<div className="attributeWrapper" key={sRoleId}>
        <div className="attributeHeader">
          <div className="attributeText" onClick={_this.handleFilterElementExpandClicked.bind(_this, sRoleId, 'role')}>
            {CS.getLabelOrCode(oMasterRelationship)}
          </div>
          <div className="attributeDelete"
               title={getTranslation().DELETE}
               onClick={_this.handleFilterElementDeleteClicked.bind(_this, sRoleId, 'role')}></div>
          <div className="attributeAddCondition"
               title={getTranslation().DELETE}
               onClick={_this.handleAddAttributeValueClicked.bind(_this, sRoleId,"role")}></div>
        </div>
        <div className={sAttributeBodyClass}>
          <div className="attributeValueWrapper">
            <div className="attributeValueWrapperBody">{aValueViews}</div>
          </div>
        </div>
      </div>);
    });

    return (
        <div className="attributeFilterViewWrapper innerFilterViewWrapper">
          <div className="attributeFilterViewHeader innerFilterViewHeader">
            <div className="attributeFilterViewHeaderLeft">
              <div className="attributeFilterViewHeaderIcon innerFilterViewHeaderIcon"></div>
              <div className="attributeFilterViewHeaderText innerFilterViewHeaderText">{getTranslation().ROLES}</div>
            </div>
            <div className="attributeFilterViewHeaderRight">
              <div className="attributeFilterSearchContainer innerFilterSearchContainer">{oMSSView}</div>
            </div>
          </div>
          <div className="attributeFilterViewBody innerFilterViewBody">
            <div className="attributeFilterSearchContainer innerFilterAttributeViewsContainer">{aRoleViews}</div>
          </div>
        </div>
    )
  };

  getMSSView = (oMSSData) => {
    return (
        <div className="contentFilterMSSContainer">
          <LazyMSSView
              context={oMSSData.context}
              selectedItems={oMSSData.selectedItems}
              isMultiSelect={oMSSData.isMultiSelect}
              requestResponseInfo={oMSSData.requestResponseInfo}
              onApply={oMSSData.onApply}
              referencedData={oMSSData.referencedData}
              excludedItems={oMSSData.excludedItems}
          />
        </div>
    )
  };

  getUserValueViews = (sAttrId, oAttrVal, sContext) => {
    var __props = this.props;
    var sScreenContext = __props.screenContext;
    var aViews = [];
    var aUsers = ViewUtils.getUserList();
    var aUserListForMSS = this.getUserListForMSS(aUsers);
    var aSelectedUsers = oAttrVal.values;
    if(sScreenContext == "condition") {
      aSelectedUsers = oAttrVal.value;
    }
    var oMasterRole = this.getRoleByRoleId(sAttrId);
    var bIsMultiSelect = oMasterRole.isMultiselect;
    var oExtraData = {
      elementId: sAttrId,
      valueId: oAttrVal.id,
      context: sContext
    };

    var fOnApply = this.handleRuleUserValueChanged.bind(this, oExtraData);
    var oMSSData = {
      items: aUserListForMSS,
      selectedItems: aSelectedUsers,
      isMultiSelect: bIsMultiSelect,
      isDisabled: false,
      onApply: fOnApply
    };

    var oMSSView = this.getMSSView(oMSSData);
    aViews.push(oMSSView);

    return aViews;
  };

  getUserValueViewsForNormalization = (sAttrId, oAttrVal) => {
    var aUsers = ViewUtils.getUserList();
    var aUserListForMSS = this.getUserListForMSS(aUsers);
    var aSelectedUsers = oAttrVal.normalization.values;
    var oMasterRole = this.getRoleByRoleId(sAttrId);//oRoleList[sAttrId];
    var bIsMultiSelect = oMasterRole.isMultiselect;

    var fOnApply = this.handleRuleUserValueForNormalizationChanged.bind(this, sAttrId);
    var oMSSData = {
      items: aUserListForMSS,
      selectedItems: aSelectedUsers,
      isMultiSelect: bIsMultiSelect,
      isDisabled: false,
      onApply: fOnApply
    };

    let oMSSView = this.getMSSView(oMSSData);
    return oMSSView;
  };

  getUserListForMSS = (aUserList) => {
    var aUserListForMSS = [];
    CS.forEach(aUserList, function (oUser) {
      aUserListForMSS.push({
        id: oUser.id,
        label: CS.trim(oUser.lastName + " " + oUser.firstName)
      })
    });
    return aUserListForMSS;
  };

  getTagGroupView = (aMandatoryTags, sTagId, oExtraData) => {
    var oMasterTag = this.getTagByTagId(sTagId);

    return (<ContentFilterTagGroupView
        masterTag={oMasterTag}
        filterTags={aMandatoryTags.tagValues}
        extraData={oExtraData}
    />);
  };

  getTagGroupViewForNormalization = (oMandatoryTags, sTagId) => {
    var oNormalization = oMandatoryTags.normalization;
    var aTagValues = oNormalization.tagValues;
    var oMasterTag = this.getTagByTagId(sTagId);

    /**Here entityTag is not available so creating dummy entity tag */
    let oTags = {};
    oTags[oMasterTag["id"]] = CommonUtils.createDummyEntityTagForTagGroupModel(oMasterTag);
    var oTagGroupModel = ViewUtils.getTagGroupModels(oMasterTag, {tags: oTags}, {}, "ruleDetailView", {}, {}, {}, aTagValues);
    var oExtraData = {
      outerContext: "contentFilterTagsInner",
      innerContext: "ruleFilterTagsTagValuesForNormalization",
    };
    let oProperties = oTagGroupModel.tagGroupModel.properties;

    return (<TagGroupView
        tagGroupModel={oTagGroupModel.tagGroupModel}
        tagRanges={oProperties.tagRanges}
        tagValues={oTagGroupModel.tagValues}
        disabled={oTagGroupModel.disabled}
        singleSelect={oProperties.singleSelect}
        extraData={oExtraData}
        masterTagList={[oMasterTag]}
    />);
  };

  getTagFilterView = () => {
    var _this = this;
    var __props = _this.props;
    var oActiveRule = _this.props.activeRule;
    var aSelectedTags = oActiveRule.tags;
    var sScreenContext = __props.screenContext;
    var aTagViews = [];
    let sSplitter = ViewUtils.getSplitter();
    let  sContext = `ruleDetailView${sSplitter}tags`;
    let oConfigDetails = oActiveRule.configDetails;
    let requestResponseData = {
      entityName:"tags",
      requestType:"configData",
      typesToExclude: []
    };
    let oReferencedAttributes = oConfigDetails.referencedTags;
    let oMSSData = ViewUtils.getLazyMSSViewModel(aSelectedTags, oReferencedAttributes, sContext, requestResponseData, false);
    oMSSData.onApply = this.handleTagCauseEffectMSSValueChanged.bind(this, "CAUSE");
    var oMSSView = this.getMSSView(oMSSData);

    CS.forEach(aSelectedTags, function (oRelationship) {
      var sRelationshipId = oRelationship.entityId;
      var aRelationshipRules = oRelationship.rules;
      if(sScreenContext === "condition") {
        aRelationshipRules = oRelationship.conditions;
      }
      var oMasterRelationship = _this.getTagByTagId(sRelationshipId);
      var aValueViews = _this.getValueViews(aRelationshipRules, sRelationshipId, "","tag", oMasterRelationship.tagType);
      var sAttributeBodyClass = "attributeBody ";
      sAttributeBodyClass += "expanded ";

      var oAttributeAddCondition = oMasterRelationship.tagType === TagTypeConstants.TAG_TYPE_BOOLEAN ? null : (
          <div className="attributeAddCondition"
               title={getTranslation().ADD}
               onClick={_this.handleAddAttributeValueClicked.bind(_this, sRelationshipId, "tag")}></div>
      );

      aTagViews.push(<div className="attributeWrapper" key={sRelationshipId}>
        <div className="attributeHeader">
          <div className="attributeText" onClick={_this.handleFilterElementExpandClicked.bind(_this, sRelationshipId, 'tag')}>
            {CS.getLabelOrCode(oMasterRelationship)}
          </div>
          <div className="attributeDelete"
               title={getTranslation().DELETE}
               onClick={_this.handleFilterElementDeleteClicked.bind(_this, sRelationshipId, 'tag')}></div>
          {oAttributeAddCondition}
        </div>
        <div className={sAttributeBodyClass}>
          <div className="attributeValueWrapper">
            <div className="attributeValueWrapperBody">{aValueViews}</div>
          </div>
        </div>
      </div>);
    });

     return (
     <div className="tagFilterViewWrapper innerFilterViewWrapper">
     <div className="tagFilterViewHeader innerFilterViewHeader">
       <div className="attributeFilterViewHeaderLeft">
         <div className="attributeFilterViewHeaderIcon innerFilterViewHeaderIcon"></div>
         <div className="attributeFilterViewHeaderText innerFilterViewHeaderText">{getTranslation().TAGS}</div>
       </div>
       <div className="attributeFilterViewHeaderRight">
         <div className="attributeFilterSearchContainer innerFilterSearchContainer">{oMSSView}</div>
       </div>
     </div>
     <div className="tagFilterViewBody innerFilterViewBody">
     <div className="tagFilterSearchContainer innerFilterAttributeViewsContainer">{aTagViews}</div>
     </div>
     </div>
     )
  };

  getTagFilterEffectView =(sRuleType) =>{
    var _this = this;
    var aRuleEffect = this.props.ruleEffect;
    var aSelectedTags = CS.filter(aRuleEffect, {type: 'tag'});
    var sMssContext = 'EFFECT';
    var aTagViews = [];
    var oActiveRule = _this.props.activeRule;
    let sSplitter = ViewUtils.getSplitter();
    let  sContext = `ruleDetailView${sSplitter}tags`;
    let oConfigDetails = oActiveRule.configDetails;
    let requestResponseData = {
      entityName:"tags",
      requestType:"configData",
      typesToExclude: []
    };
    let oReferencedAttributes = oConfigDetails.referencedTags;
    let aExcludedItemsList = CS.map(aSelectedTags, "entityId");
    let oMSSData = ViewUtils.getLazyMSSViewModel(aSelectedTags, oReferencedAttributes, sContext, requestResponseData, false, aExcludedItemsList);
    oMSSData.onApply = this.handleTagCauseEffectMSSValueChanged.bind(this, "EFFECT");
    var oMSSView = this.getMSSView(oMSSData);

    CS.forEach(aSelectedTags, function (oTag) {
      var sTagId = oTag.entityId;
      var oMasterRelationship = _this.getTagByTagId(sTagId);
      var aValueViews = _this.getAttrValueEffectViews([oTag], sTagId, "", "tag", sRuleType);
      var sAttributeBodyClass = "attributeBody ";
      sAttributeBodyClass += "expanded ";

      aTagViews.push(<div className="attributeWrapper" key={sTagId}>
        <div className="attributeHeader">
          <div className="attributeText" onClick={_this.handleFilterElementExpandClicked.bind(_this, sTagId, 'tag')}>
            {CS.getLabelOrCode(oMasterRelationship)}
          </div>
          <div className="attributeEffectToggleWrapper"></div>
          <div className="attributeDelete"
               title={getTranslation().DELETE}
               onClick={_this.handleFilterElementDeleteClicked.bind(_this, oTag.id, 'tag', sMssContext)}></div>
        </div>
        <div className={sAttributeBodyClass}>
          <div className="attributeValueWrapper">
            <div className="attributeValueWrapperBody">{aValueViews}</div>
          </div>
        </div>
      </div>);
    });

     return (
     <div className="tagFilterViewWrapper innerFilterViewWrapper">
     <div className="tagFilterViewHeader innerFilterViewHeader">
       <div className="attributeFilterViewHeaderLeft">
         <div className="attributeFilterViewHeaderIcon innerFilterViewHeaderIcon"></div>
         <div className="attributeFilterViewHeaderText innerFilterViewHeaderText">{getTranslation().TAGS}</div>
       </div>
       <div className="attributeFilterViewHeaderRight">
         <div className="attributeFilterSearchContainer innerFilterSearchContainer">{oMSSView}</div>
       </div>
     </div>
     <div className="tagFilterViewBody innerFilterViewBody">
     <div className="tagFilterSearchContainer innerFilterAttributeViewsContainer">{aTagViews}</div>
     </div>
     </div>
     )
  };

  getBlackListView = (aWordList, sBlackListRuleLabel, bIsDisabled) => {
    var __props = this.props;
    var sSplitter = ViewUtils.getSplitter();
    var sLabel = CS.isEmpty(sBlackListRuleLabel) ? "Black Listed Words": sBlackListRuleLabel;
    var sContext = "rule" + sSplitter + __props.screenContext;
    return (
        <BlackListEditableView
            list={aWordList}
            label={sLabel}
            context={sContext}
            isDisabled={bIsDisabled}
        />);
  };

  getKlassFilterEffectView = () => {
    let oProps = this.props;
    let oActiveRule = oProps.activeRule;
    let oNormalizationKlass = CS.find(oActiveRule.normalizations, {type: 'type'});
    let aSelectedItems = !CS.isEmpty(oNormalizationKlass) ? oNormalizationKlass.values : [];
    let oReferencedKlasses = !CS.isEmpty(oActiveRule.configDetails) ? oActiveRule.configDetails.referencedKlasses : {};

    return (
        <div className="klassFilterViewWrapper innerFilterViewWrapper">
          <div className="klassFilterViewHeader innerFilterViewHeader">
            <div className="attributeFilterViewHeaderLeft">
              <div className="attributeFilterViewHeaderIcon innerFilterViewHeaderIcon"></div>
              <div className="attributeFilterViewHeaderText innerFilterViewHeaderText">{getTranslation().CLASSES}</div>
            </div>
            <div className="attributeFilterViewHeaderRight">
              <KlassSelectorView
                  context={oActiveRule.id}
                  selectedType={this.props.selectedKlassType}
                  shouldUseGivenType={true}
                  isTypeListHidden={true}
                  isKlassListMultiSelect={true}
                  referencedKlasses={oReferencedKlasses}
                  selectedKlasses={aSelectedItems}
                  isKlassListNatureType={false}
                  isKlassListAbstract={false}
                  onKlassListChanged={this.handleKlassCauseEffectMSSValueChanged.bind(this, "EFFECT")}
                  updateEntityForcefully={!this.props.isRuleDirty}
                  bShowIcon={true}
              />
            </div>
          </div>
        </div>);
  };

  getSmallTaxonomyViewModel = (oMultiTaxonomyData) => {
    var oSelectedMultiTaxonomyList = oMultiTaxonomyData.selected;
    var oMultiTaxonomyListToAdd = oMultiTaxonomyData.notSelected;
    var sRootTaxonomyId = ViewUtils.getTreeRootId();
    var oProperties = {};
    oProperties.headerPermission = {};
    return new SmallTaxonomyViewModel('', oSelectedMultiTaxonomyList, oMultiTaxonomyListToAdd, getTranslation().ADD_TAXONOMY, 'forMultiTaxonomy', sRootTaxonomyId, oProperties)
  };

  getTaxonomyFilterView = () => {
    let oMultiTaxonomyData = this.props.multiTaxonomyData;
    if (CS.isEmpty(oMultiTaxonomyData)) {
      //todo High Priority : Why multiTaxonomyData is Empty on creation?
      return null;
    }
    let oAllowedTaxonomyHierarchyList = oMultiTaxonomyData.allowedTaxonomyHierarchyList;
    var oSmallTaxonomyViewModel = this.getSmallTaxonomyViewModel(oMultiTaxonomyData);

    return (
        <div className="taxonomyFilterViewWrapper innerFilterViewWrapper">
          <div className="taxonomyFilterViewHeader innerFilterViewHeader">
            <div className="attributeFilterViewHeaderLeft">
              <div className="attributeFilterViewHeaderIcon innerFilterViewHeaderIcon"></div>
              <div className="attributeFilterViewHeaderText innerFilterViewHeaderText">{getTranslation().TAXONOMIES}</div>
            </div>
            <div className="attributeFilterViewHeaderRight">
              <div className="smallTaxonomyViewContainer">
                <SmallTaxonomyView model={oSmallTaxonomyViewModel}
                                   allowedTaxonomyHierarchyList={oAllowedTaxonomyHierarchyList}
                                   showCustomIcon={true}
                                   isDisabled={false}
                                   context="dataRules"
                                   showAllComponents={true}
                                   paginationData={this.props.taxonomyPaginationData}/>
              </div>
            </div>
          </div>
        </div>);
  };

  getKlassFilterView = () => {
    let oProps = this.props;
    let oActiveRule = oProps.activeRule;
    let oReferencedKlasses = !CS.isEmpty(oActiveRule.configDetails) ? oActiveRule.configDetails.referencedKlasses : {};

    return (
        <div className="klassFilterViewWrapper innerFilterViewWrapper">
          <div className="klassFilterViewHeader innerFilterViewHeader">
            <div className="attributeFilterViewHeaderLeft">
              <div className="attributeFilterViewHeaderIcon innerFilterViewHeaderIcon"></div>
              <div className="attributeFilterViewHeaderText innerFilterViewHeaderText">{getTranslation().CLASSES}</div>
            </div>
            <div className="attributeFilterViewHeaderRight">
              <KlassSelectorView
                  context={oActiveRule.id}
                  onTypeListChanged={this.handleKlassTypeCauseMSSValueChanged}
                  isKlassListMultiSelect={true}
                  referencedKlasses={oReferencedKlasses}
                  selectedKlasses={oActiveRule.types}
                  isKlassListAbstract={false}
                  onKlassListChanged={this.handleKlassCauseEffectMSSValueChanged.bind(this, "CAUSE")}
                  updateEntityForcefully={!this.props.isRuleDirty}
                  selectedType={this.props.selectedKlassType}
                  shouldUseGivenType={true}
                  bShowIcon={true}
              />
            </div>
          </div>
        </div>);
  };

  // todo: pass different multiTaxonomy from controller
  getTaxonomyFilterEffectView = () => {
    let oMultiTaxonomyDataForRulesEffect = this.props.multiTaxonomyDataForRulesEffect;
    if (CS.isEmpty(oMultiTaxonomyDataForRulesEffect)) {
      //todo High Priority : Why multiTaxonomyData is Empty on creation?
      return null;
    }
    var oSmallTaxonomyViewModel = this.getSmallTaxonomyViewModel(oMultiTaxonomyDataForRulesEffect);
    let oAllowedTaxonomyHierarchyList = oMultiTaxonomyDataForRulesEffect.allowedTaxonomyHierarchyList;

    return (
        <div className="taxonomyFilterViewWrapper innerFilterViewWrapper">
          <div className="taxonomyFilterViewHeader innerFilterViewHeader">
            <div className="attributeFilterViewHeaderLeft">
              <div className="attributeFilterViewHeaderIcon innerFilterViewHeaderIcon"></div>
              <div className="attributeFilterViewHeaderText innerFilterViewHeaderText">{getTranslation().TAXONOMIES}</div>
            </div>
            <div className="attributeFilterViewHeaderRight">
              <div className="smallTaxonomyViewContainer">
                <SmallTaxonomyView model={oSmallTaxonomyViewModel}
                                   showCustomIcon={true}
                                   allowedTaxonomyHierarchyList={oAllowedTaxonomyHierarchyList}
                                   isDisabled={false}
                                   context="dataRulesEffect"
                                   showAllComponents={true}
                                   paginationData={this.props.taxonomyPaginationData}
                                   />
              </div>
            </div>
          </div>
        </div>);
  };

  getFilterView = () => {
    let sScreenContext = this.props.screenContext;
    let aViewsToReturn = [
      (<div className="ruleFilterItem" key="attribute">{this.getAttributeFilterView()}</div>),
      (<div className="ruleFilterItem" key="tag">{this.getTagFilterView()}</div>)
    ];
    if (sScreenContext && !sScreenContext.includes("kpiConfigDetail")) {
      aViewsToReturn.push(<div className="ruleFilterItem" key="role">{this.getRoleFilterView()}</div>);
      aViewsToReturn.push(<div className="ruleFilterItem" key="klass">{this.getKlassFilterView()}</div>);
      aViewsToReturn.push(<div className="ruleFilterItem" key="taxonomy">{this.getTaxonomyFilterView()}</div>);
    }
    return aViewsToReturn;
  };

  handleRuleNameChanged = (oEvent) => {
    var __props = this.props;
    var sScreenContext = this.props.screenContext;
    var sNewValue = oEvent.target.value;
    if(__props.activeRule.label != sNewValue) {
      EventBus.dispatch(oEvents.RULE_NAME_CHANGED, null, sNewValue, sScreenContext);
    }
  };

  handleRuleRightPanelBarIconClicked = (sIconContext) => {
    var sScreenContext = this.props.screenContext;
    EventBus.dispatch(oEvents.RULE_RIGHT_PANEL_BAR_ICON_CLICKED, null, sIconContext, sScreenContext);
  };

  handleRuleConfigMssValueChanged = (sKey, aNewValue ) => {
    EventBus.dispatch(oEvents.RULE_DETAIL_MSS_VALUE_CHANGED, aNewValue, sKey);
  };

  handleRuleDetailEndpointSelectionChanged = (aSelectedItems) =>{
    EventBus.dispatch(oEvents.RULE_DETAIL_ENDPOINTS_MSS_VALUE_CHANGED, aSelectedItems);
  };

  handleGoldenRecordSelectedEntityRemoved = (sContext, sEntityId) => {
    EventBus.dispatch(oEvents.RULE_DETAIL_SELECTED_ENTITY_REMOVED, sContext, sEntityId);
  };

  onDragEnd = (aSelectedSuppliers, sContext, sEntityId, oSource, oDestination) => {
    let iOldIndex = oSource.index;
    let iNewIndex = oDestination.index;
    let aNewSupplierSequence = arrayMove(aSelectedSuppliers, iOldIndex, iNewIndex);
    EventBus.dispatch(oEvents.RULE_DETAIL_SUPPLIER_SEQUENCE_CHANGED, aNewSupplierSequence, sContext, sEntityId);
  };

  handleGoldenRecordSelectedSupplierRemoved = (sSelectedSupplierId, sContext, sSelectedEntityId) => {
    EventBus.dispatch(oEvents.RULE_DETAIL_SELECTED_SUPPLIER_REMOVED, sSelectedSupplierId, sContext, sSelectedEntityId);
  };

  getPhysicalCatalogSelectionView = () => {
    let aPhysicalCatalogIdsData = this.props.physicalCatalogIdsData;
    let oActiveRule = this.props.activeRule;
    let aSelectedItems = oActiveRule.physicalCatalogIds;
    let sContext = this.props.context;

    return {
      selectedItems: aSelectedItems,
      items: aPhysicalCatalogIdsData,
      context: sContext,
      contextKey: "physicalCatalogs"
    }
  };

  getPortalSelectionView = () => {
    let aPortalIdsData = this.props.portalIdsData;
    let oActiveRule = this.props.activeRule;
    let aSelectedItems = oActiveRule.portalIds;
    let sContext = this.props.context;

    return {
      selectedItems: aSelectedItems,
      items: aPortalIdsData,
      context: sContext,
      contextKey: "portals"
    }
  };

  getPartnersSelectionView = () => {
    let oActiveRule = this.props.activeRule;
    let aSelectedItems = oActiveRule.organizations;
    let oConfigDetails = oActiveRule.configDetails;
    let oReferencedOrganizations = oConfigDetails.referencedOrganizations || oConfigDetails.referencedOraganizations;
    let oReqResObj = {
      requestType: "configData",
      entityName: "organizations",
    };

    return{
      isMultiSelect: true,
      context: 'rule',
      disabled: false,
      selectedItems: aSelectedItems,
      cannotRemove: false,
      showSelectedInDropdown: true,
      requestResponseInfo: oReqResObj,
      isLoadMoreEnabled: false,
      referencedData: oReferencedOrganizations,
      onApply: this.handlePartnerApplyClicked
    }
  };

  getEndpointSelectionView = () => {
    let __props = this.props;
    let oReqResObj = {
      requestType: "configData",
      entityName: "endpoints",
    };
    let oActiveRule = __props.activeRule;
    if(oActiveRule.isCreated){
      return null;
    }
    let aSelectedEndpoints = CS.isEmpty(oActiveRule.endpoints) ? [] : oActiveRule.endpoints;
    var oConfigDetails = oActiveRule.configDetails;
    let oReferencedEndpoints = oConfigDetails.referencedEndpoints;
    let oLazyMSSModel = ViewUtils.getLazyMSSViewModel(aSelectedEndpoints, oReferencedEndpoints, "", oReqResObj, true);

    return {
      isMultiSelect: oLazyMSSModel.isMultiSelect,
      context: oLazyMSSModel.context,
      selectedItems: oLazyMSSModel.selectedItems,
      requestResponseInfo: oLazyMSSModel.requestResponseInfo,
      referencedData: oLazyMSSModel.referencedData,
      onApply:  this.handleRuleDetailEndpointSelectionChanged
    }
  };

  getRightPanelRuleListDetailView = () => {
    var __props = this.props;
    var oRightPanelData = this.props.rightPanelData;
    var oMasterRuleList = oRightPanelData.listOfRuleListMap;
    var sSplitter = ViewUtils.getSplitter();
    var sDragDropContext = "ruleList";
    if(__props.screenContext === "condition") {
      sDragDropContext += sSplitter + __props.screenContext;
    }
    var aRuleListView = [];
    CS.forEach(oMasterRuleList, function (oRuleList, iIndex) {
      let sRuleLabel = CS.getLabelOrCode(oRuleList);
      var oDragViewModel = new DragViewModel(oRuleList.id, sRuleLabel, true, sDragDropContext, {
        data: oRuleList.id,
        draggedElementName: sRuleLabel
      });
      aRuleListView.push(
          <DragView model={oDragViewModel}>
            <TooltipView placement="bottom" label={sRuleLabel}>
              <div className="rightPanelRuleList" key={iIndex}>{sRuleLabel}</div>
            </TooltipView>
          </DragView>
      );
    });

    return (
        <div className="rightPanelRuleListWrapper">
          <div className="rightPanelHeader">{getTranslation().WORD_LIST}</div>
          <div className="rightPanelBody">
            {aRuleListView}
          </div>
        </div>
    );
  };

  getRightPanelView = () => {
    var oRightPanelData = this.props.rightPanelData;

    var bRightPanelVisibility = oRightPanelData.isRightPanelActive;
    var sRightPanelDetailClassName =  bRightPanelVisibility ? "rightPanelDetailViewWrapper rightPanelActive" : "rightPanelDetailViewWrapper";

    var oRightBarIconClickMap = oRightPanelData.rightBarSelectedIconMap;
    var sRuleListIconClassName = oRightBarIconClickMap["ruleList"] ? "rightPanelBarIcon ruleListIcon selected" : "rightPanelBarIcon ruleListIcon";

    return (
        <div className="rightPanelWrapper">
          <div className={sRightPanelDetailClassName}>
            {this.getRightPanelRuleListDetailView()}
          </div>
            <div className="rightPanelBarViewWrapper">
                <TooltipView placement="left" label={getTranslation().WORD_LIST}>
                    <div className="rightPanelBarIconWrapper">
                        <div className={sRuleListIconClassName}
                             onClick={this.handleRuleRightPanelBarIconClicked.bind(this, 'ruleList')}></div>
                    </div>
                </TooltipView>
            </div>
        </div>
    );
  };

  getFilterViewNew =(sRuleType) => {
    let oRuleTypeDictionary = new RuleTypeDictionary();
    let aViewsToReturn = [];
    aViewsToReturn.push(<div className="ruleFilterItem" key="klass">{this.getKlassFilterView()}</div>);
    aViewsToReturn.push(<div className="ruleFilterItem" key="taxonomy">{this.getTaxonomyFilterView()}</div>);

    switch (sRuleType) {
      case oRuleTypeDictionary.CLASSIFICATION_RULE:
        break;
      case oRuleTypeDictionary.VIOLATION_RULE:
      case oRuleTypeDictionary.STANDARDIZATION_AND_NORMALIZATION_RULE:
        aViewsToReturn.unshift(<div className="ruleFilterItem" key="tag">{this.getTagFilterView()}</div>);
        aViewsToReturn.unshift(<div className="ruleFilterItem" key="attribute">{this.getAttributeFilterView()}</div>);
        break;
    }

    return aViewsToReturn;
  }

  getFilterEffectViewNew =(sRuleType) => {
    let oRuleTypeDictionary = new RuleTypeDictionary();
    let aViewsToReturn = [];
    let oActiveRule = this.props.activeRule;
    let bIsLanguageDependent = CS.has(oActiveRule, "isLanguageDependent") && oActiveRule.isLanguageDependent;

    switch (sRuleType) {
      case oRuleTypeDictionary.CLASSIFICATION_RULE:
        aViewsToReturn.push(<div className="ruleFilterItem" key="klass">{this.getKlassFilterEffectView()}</div>);
        aViewsToReturn.push(<div className="ruleFilterItem" key="taxonomy">{this.getTaxonomyFilterEffectView()}</div>);
        break;
      case oRuleTypeDictionary.STANDARDIZATION_AND_NORMALIZATION_RULE:
        aViewsToReturn.push(<div className="ruleFilterItem" key="attribute">{this.getAttributeFilterEffectView(sRuleType)}</div>);
        if (!bIsLanguageDependent) {
          aViewsToReturn.push(<div className="ruleFilterItem" key="tag">{this.getTagFilterEffectView(sRuleType)}</div>);
          aViewsToReturn.push(<div className="ruleFilterItem" key="klass">{this.getKlassFilterEffectView()}</div>);
          aViewsToReturn.push(<div className="ruleFilterItem" key="taxonomy">{this.getTaxonomyFilterEffectView()}</div>);
        }
        break;
      case oRuleTypeDictionary.VIOLATION_RULE:
        aViewsToReturn.push(<div className="ruleFilterItem" key="attribute">{this.getAttributeFilterEffectView(sRuleType)}</div>);
        !bIsLanguageDependent && aViewsToReturn.push(<div className="ruleFilterItem" key="tag">{this.getTagFilterEffectView(sRuleType)}</div>);
        break;
    }

    return aViewsToReturn;
  };

  getGoldenRecordLazyMSSModel = (aSelectedItems, oReferencedEntities, sContext, sKey, bIsNature) => {
    let oReqResObj = {
      requestType: "configData",
      entityName: sContext,
      typesToExclude:[AttributesTypeDictionary.CONCATENATED,AttributesTypeDictionary.CALCULATED]
    };
    if (sContext === "attributes") {
      oReqResObj.typesToExclude = [AttributeTypeDictionary.CONCATENATED, AttributeTypeDictionary.CALCULATED];
      oReqResObj.customRequestModel = {
        isLanguageIndependent: true
      }
    }
    let bIsMultiSelecte = true;
    let bCannotRemove = false;
    let bShowIcon = false;

    if (sContext == 'klasses') {
      oReqResObj = {
        requestType: "customType",
        requestURL: "config/klasseslistbybasetype",
        responsePath: ["success", "list"],
        entityName: sContext,
        customRequestModel: {
          isNature: bIsNature,
          types: ["com.cs.core.config.interactor.entity.klass.ProjectKlass"],
          typesToExclude: [NatureTypeDictionary.GTIN, NatureTypeDictionary.EMBEDDED, NatureTypeDictionary.LANGUAGE]
        }
      };

      if (bIsNature) {
        bIsMultiSelecte = false;
        bCannotRemove = true;
      }
      bShowIcon =true;
    }

    let oHandler = this.handleRuleConfigMssValueChanged.bind(this, sKey);

    let oModel = {
      isMultiSelect: bIsMultiSelecte,
      context: 'goldenRecordRule',
      disabled: false,
      selectedItems: aSelectedItems,
      cannotRemove: bCannotRemove,
      showSelectedInDropdown: true,
      requestResponseInfo: oReqResObj,
      isLoadMoreEnabled: false,
      referencedData: oReferencedEntities,
      onApply: oHandler,
      bShowIcon: bShowIcon
    };
    return oModel;
  };

  getSmallTaxonomyView = () => {
    let oMultiTaxonomyData = this.props.multiTaxonomyData;
    let oSmallTaxonomyViewModel = this.getSmallTaxonomyViewModel(oMultiTaxonomyData);
    let oAllowedTaxonomyHierarchyList = oMultiTaxonomyData.allowedTaxonomyHierarchyList;
    return (
        <div className='goldenRecordMatchEntitiesWrapper'>
          <SmallTaxonomyView model={oSmallTaxonomyViewModel}
                             allowedTaxonomyHierarchyList={oAllowedTaxonomyHierarchyList}
                             showCustomIcon={true}
                             isDisabled={false}
                             context="dataRulesEffect"
                             showAllComponents={true}
                             localSearch={true}/>
        </div>)
  };

  getAutoCreateSwitch = (bIsAutoCreate) => {
    return {
      isDisabled: false,
      isSelected: bIsAutoCreate,
      context: ViewContextConstants.GOLDEN_RECORD_RULE,
    }
  };

  getGoldenRecordMatchView = () => {
    let oActiveRule = this.props.activeRule;
    let aSelectedAttributes = oActiveRule.attributes;
    let aSelectedTags = oActiveRule.tags;
    let aReferencedAttributes = oActiveRule.configDetails.referencedAttributes;
    let aReferencedTags = oActiveRule.configDetails.referencedTags;
    let aReferencedKlasses = oActiveRule.configDetails.referencedKlasses;
    let aSelectedNatureKlasses = oActiveRule.natureKlassIds;
    let aSelectedNonNatureKlasses = oActiveRule.nonNatureKlassIds;

    let oModel = {
      tags: this.getGoldenRecordLazyMSSModel(aSelectedTags, aReferencedTags, 'tags', 'tags'),
      attributes: this.getGoldenRecordLazyMSSModel(aSelectedAttributes, aReferencedAttributes, 'attributes', 'attributes'),
      natureKlasses: this.getGoldenRecordLazyMSSModel(aSelectedNatureKlasses, aReferencedKlasses, 'klasses', 'natureKlassIds', true),
      nonNatureKlasses: this.getGoldenRecordLazyMSSModel(aSelectedNonNatureKlasses, aReferencedKlasses, 'klasses', 'nonNatureKlassIds', false),
      taxonomies: this.getSmallTaxonomyView(),
      isAutoCreate: this.getAutoCreateSwitch(oActiveRule.isAutoCreate),
    }

    let oGoldenRecordRuleConfigLayout = new GoldenRecordRuleConfigLayout();


    return (<CommonConfigSectionView context={ViewContextConstants.GOLDEN_RECORD_RULE}
                                     data={oModel}
                                     sectionLayout={oGoldenRecordRuleConfigLayout.goldenRecordsRulesConfigMatchInformation}
    />)
  };

  getLatestSelectionView = (oSelectedItem, sContext) => {
    let oView = null;
    if (sContext != "relationships" && sContext != "natureRelationships") {
      let bValue = oSelectedItem.type == "latest" || false;
      oView = <div className='latestSelectionViewWrapper'>
            <div className='latestSelectionViewContainer'>
              <div className="latestSelectetLabel">Latest</div>
              <GridYesNoPropertyView
                  isDisabled={false}
                  onChange={this.handleLatestEntityValueSelectionToggled.bind(this, sContext, oSelectedItem.entityId)}
                  value={bValue}
              />
            </div>
          </div>;
    }
    return (oView)
  };

  getSupplierSelectionView = (oSelectedItem, sContext) => {
    let aSelectedSuppliers = oSelectedItem.supplierIds;
    let _this = this;
    let sClassName = "goldenRecordMergeViewAddAttributes ";
    let oAnchorOrigin = {horizontal: 'right', vertical: 'top'};
    let oTargetOrigin = {horizontal: 'right', vertical: 'bottom'};
    let oRequestResponseInfo = {
      requestType: "configData",
      entityName: "organizations"
    };

    let sLabel = getTranslation().ADD + ' ' + getTranslation()[sContext];
    let oLazyContextViewForSupplier =
        <div className='goldenRecordMergeViewAddEntitiesWrapper'>
          <div className='goldenRecordMergeViewAddSupplierLabel'>{getTranslation().SUPPLIER}</div>
          <LazyContextMenuView
              isMultiselect={true}
              onApplyHandler={this.addEntitiesFromDropdown.bind(this, 'organizations', sContext, oSelectedItem.entityId)}
              anchorOrigin={oAnchorOrigin}
              targetOrigin={oTargetOrigin}
              menuListHeight={"250px"}
              requestResponseInfo={oRequestResponseInfo}
              selectedItems={oSelectedItem.supplierIds}
              className={sClassName}>
            <TooltipView placement="bottom" label={sLabel}>
              <div className='goldenRecordMergeViewAddEntitiesIcon'
                   onClick={this.handleAddAttributeButtonClicked.bind(this, sContext)}>
              </div>
            </TooltipView>
          </LazyContextMenuView>
        </div>;

    let oActiveRule = this.props.activeRule;
    let aSupplierList = [];
    CS.forEach(aSelectedSuppliers, (sSelectedSupplier) => {
      let oSelectedSupplier = CS.find(oActiveRule.configDetails.referencedOrganizations, {id: sSelectedSupplier});
      let oSupplier = <div className='supplierListItem' key={sSelectedSupplier}>
        <div className='supplierListItemLabel'>{CS.getLabelOrCode(oSelectedSupplier)}</div>
        <div className='supplierListRemoveOption'
             onClick={_this.handleGoldenRecordSelectedSupplierRemoved.bind(_this, sSelectedSupplier, sContext, oSelectedItem.entityId)}></div>
      </div>;
      aSupplierList.push({
        id: sSelectedSupplier,
        component: oSupplier,
        className: "supplierContainer"
      });
    });

    let oListData = {
      droppableId: "supplierList",
      items: aSupplierList
    };

    let oSupplierDetailView = <DragDropContextView
      listData={[oListData]}
      context={"tabSortableList"}
      onDragEnd={this.onDragEnd.bind(this, aSelectedSuppliers, sContext, oSelectedItem.entityId)}
      showDraggableIcon={true}/>;

    let oEntityDetailView = <div className="supplierListContainer">
      <div className='supplierListWrapper'>
        {oLazyContextViewForSupplier}
        {oSupplierDetailView}
      </div>
    </div>;
    return oEntityDetailView;
  };

  getAddedEntityDetailView = (aSelectedItems, sContext) => {
    let aSelectedView = [];
    let _this = this;
    let oActiveRule = this.props.activeRule;
    let sEntity = '';
    switch (sContext) {
      case 'attributes':
        sEntity = 'referencedAttributes';
        break;
      case 'tags':
        sEntity = 'referencedTags';
        break;
      case 'relationships':
        sEntity = 'referencedRelationships';
        break;
      case 'natureRelationships':
        sEntity = 'referencedNatureRelationships';
        break;
    }

    CS.forEach(aSelectedItems, function (oSelectedItem, iIndex) {
      let oLatestSelectionView = _this.getLatestSelectionView(oSelectedItem, sContext);
      let oSupplierSelectionView = _this.getSupplierSelectionView(oSelectedItem, sContext);
      let oReferencedEntity = CS.find(oActiveRule.configDetails[sEntity], {id: oSelectedItem.entityId});
      try{
        let oModel = {
          latest: oLatestSelectionView,
          suppliers: oSupplierSelectionView
        };
        let oGoldenRecordRuleConfigLayout = new GoldenRecordRuleConfigLayout();
        let oView = <CommonConfigSectionView context={ViewContextConstants.GOLDEN_RECORD_RULE}
                                             data={oModel}
                                             sectionLayout={oGoldenRecordRuleConfigLayout.goldenRecordsRulesConfigMergeDetailsInformation}
        />;
        aSelectedView.push(
            <div className="selectedElementContainer" key={iIndex}>
              <div className='selectedElementSection'>
                <div className='selectedEntityHeader'>
                  <div className='selectedEntityLabel'>{CS.getLabelOrCode(oReferencedEntity)} </div>
                  <div className='selectedEntityRemoveButton'
                       onClick={_this.handleGoldenRecordSelectedEntityRemoved.bind(this, sContext, oSelectedItem.entityId)}></div>
                </div>
                <div className='selectedElementsDetailsContainer'>
                  <div className='selectedEntityDetails'>
                    {oView}
                  </div>
                </div>
              </div>
            </div>
        )
      }catch(e) {
        ExceptionLogger.log(e)
      }
    });

    return aSelectedView;
  };

  getAddedEntitiesView = (aSelectedItems, sContext, sEntityLabel) => {
    let bIsMultiselect = true;
    let sClassName = "goldenRecordMergeViewAddAttributes ";
    let oAnchorOrigin = {horizontal: 'right', vertical: 'top'};
    let oTargetOrigin = {horizontal: 'right', vertical: 'bottom'};
    let oRequestResponseInfo = {
      requestType: "configData",
      entityName: sContext,
      typesToExclude:[AttributesTypeDictionary.CONCATENATED,AttributesTypeDictionary.CALCULATED]
    };

    let aSelectedEntities = CS.map(aSelectedItems, 'entityId');

    let sLabel = getTranslation().ADD + ' ' + getTranslation()[sContext];
    let aAddedEntitiesViews = this.getAddedEntityDetailView(aSelectedItems, sContext);
    let oSelectedElementsView = null;
    if (!CS.isEmpty(aAddedEntitiesViews)) {
      oSelectedElementsView = <div className='selectedElementView'>
        <div className='selectedElementWrapper'>
          {aAddedEntitiesViews}
        </div>
      </div>
    }

    return (
        <div className="goldenRecordMergeViewAddEntities">
          <div className='goldenRecordMergeViewAddEntitiesWrapper'>
            <div className='goldenRecordMergeViewAddEntitiesLabel'>{sEntityLabel}</div>
            <LazyContextMenuView
                isMultiselect={bIsMultiselect}
                onApplyHandler={this.addEntitiesFromDropdown.bind(this, sContext, '', '')}
                anchorOrigin={oAnchorOrigin}
                targetOrigin={oTargetOrigin}
                menuListHeight={"250px"}
                requestResponseInfo={oRequestResponseInfo}
                selectedItems={aSelectedEntities}
                className={sClassName}>
              <TooltipView placement="bottom" label={sLabel}>
                <div className='goldenRecordMergeViewAddEntitiesIcon'
                     onClick={this.handleAddAttributeButtonClicked.bind(this, sContext)}>
                </div>
              </TooltipView>
            </LazyContextMenuView>
          </div>
          {oSelectedElementsView}
        </div>
    )
  };

  getGoldenRecordMergeView = () => {
    let oActiveRule = this.props.activeRule;
    let aSelectedAttributes = oActiveRule.mergeEffect.attributes;
    let aSelectedTags = oActiveRule.mergeEffect.tags;
    let aSelectedNatureRelationships = oActiveRule.mergeEffect.natureRelationships;
    let aSelectedRelationships = oActiveRule.mergeEffect.relationships;
    let oAddAttributesView = this.getAddedEntitiesView(aSelectedAttributes, 'attributes', getTranslation().ATTRIBUTES);
    let oAddTagsView = this.getAddedEntitiesView(aSelectedTags, 'tags', getTranslation().TAGS);
    let oAddedRelationshipsView = this.getAddedEntitiesView(aSelectedRelationships, 'relationships', getTranslation().RELATIONSHIPS);
    let oAddedNatureRelationshipsView = this.getAddedEntitiesView(aSelectedNatureRelationships, 'natureRelationships', getTranslation().NATURE_RELATIONSHIPS);

    let oGoldenRecordRuleConfigLayout = new GoldenRecordRuleConfigLayout();

    let oModel = {
      attributes: oAddAttributesView,
      tags: oAddTagsView,
      relationships: oAddedRelationshipsView,
      natureRelationships: oAddedNatureRelationshipsView
    };


    return (<div className="goldenRecordMergeViewWrapper">
          <CommonConfigSectionView context={ViewContextConstants.GOLDEN_RECORD_RULE}
                                   data={oModel}
                                   sectionLayout={oGoldenRecordRuleConfigLayout.goldenRecordsRulesConfigMergeInformation}
          />
        </div>)
  };

  getDataLanguagesMSSModel = (oActiveRule) => {
    let aDataLanguages = this.props.dataLanguages;
    let aSelectedLanguageCodes = oActiveRule.languages || [];
    let aSelectedLanguagesId = ViewUtils.getIdsByCode(aDataLanguages, aSelectedLanguageCodes);
    return {
      items: aDataLanguages,
      selectedItems: aSelectedLanguagesId,
      context: "ruleDataLanguages",
      disabled: false,
      isMultiSelect: true,
    }
  };

  getDataModel = (sRuleTypeLabel) => {
    let oProps = this.props;
    let oActiveRule = oProps.activeRule;
    let oDataModel = {
      id: oActiveRule.id,
      label: oActiveRule.label,
      code: oActiveRule.code,
      partners: this.getPartnersSelectionView(),
      physicalCatalogId : this.getPhysicalCatalogSelectionView(),
      portalId : this.getPortalSelectionView(),
      endpoints: this.getEndpointSelectionView()
    };
    if (this.props.context != "goldenRecordRule") {
      oDataModel.ruleType = sRuleTypeLabel;

      let oRuleTypeDictionary = new RuleTypeDictionary();
      if(oActiveRule.type !== oRuleTypeDictionary.CLASSIFICATION_RULE) {
        oDataModel.isLanguageDependent = {
          isSelected: oActiveRule.isLanguageDependent,
          context: "rule",
        }
      }

      if (oActiveRule.isLanguageDependent) {
        oDataModel.dataLanguages = this.getDataLanguagesMSSModel(oActiveRule);
      }
    }
    return oDataModel;
  };

  getViewNew = () => {
    let oRuleTypeDictionary = new RuleTypeDictionary();
    var oActiveRule= this.props.activeRule;
    let sRuleType = oActiveRule.type;
    var oFilterView = this.getFilterViewNew(sRuleType);
    var oFilterEffectView = this.getFilterEffectViewNew(sRuleType);
    var oRightPanelView = null;

    var oRightPanelData = this.props.rightPanelData;
    var bRightPanelVisibility = oRightPanelData.isRightPanelActive;
    var sRuleDetailLeftViewClassName = bRightPanelVisibility ? "ruleDetailLeftViewWrapper rightPanelActive " : "ruleDetailLeftViewWrapper ";
    let oDataGovernanceRuleConfigLayout = new DataGovernanceRuleConfigLayout();

    let sRuleTypeLabel = "";
    switch (sRuleType) {
      case oRuleTypeDictionary.CLASSIFICATION_RULE:
        sRuleTypeLabel = getTranslation().CLASSIFICATION;
        break;
      case oRuleTypeDictionary.STANDARDIZATION_AND_NORMALIZATION_RULE:
        oRightPanelView = this.getRightPanelView();
        sRuleTypeLabel = getTranslation().STANDARDIZATION_AND_NORMALIZATION;
        break;
      case oRuleTypeDictionary.VIOLATION_RULE:
        oRightPanelView = this.getRightPanelView();
        sRuleTypeLabel = getTranslation().VIOLATION;
        break;
    }

    let oModel = this.getDataModel(sRuleTypeLabel);
    let aDisabledFields = ["code", "ruleType", "isLanguageDependent"];

    let sRuleDetailViewWrapperClassName = "ruleDetailViewWrapper ";
    if (!oRightPanelView) {
      sRuleDetailViewWrapperClassName += "withoutRightSection";
    }

      let oRuleConfigurationSection = null;

      if (this.props.context == 'goldenRecordRule') {
          let oGoldenRecordMatchView = this.getGoldenRecordMatchView();
          let oGoldenRecordMergeView = this.getGoldenRecordMergeView();
          oRuleConfigurationSection = <div className='goldenRecordMatchAndMergeView'>
              <div className='goldenRecordMatchView goldenRecordMatchOrMergeViewWrapper'>
                  <div className='goldenRecordMatchViewHeader goldenRecordMatchOrMergeViewHeader'>{getTranslation().MATCH}</div>
                  {oGoldenRecordMatchView}
              </div>
              <div className='goldenRecordMergeView goldenRecordMatchOrMergeViewWrapper'>
                  <div className='goldenRecordMergeViewHeader goldenRecordMatchOrMergeViewHeader'>{getTranslation().MERGE}</div>
                  {oGoldenRecordMergeView}
              </div>
          </div>
      }

      else {
          oRuleConfigurationSection = <div className="ruleFilterViewContainer">
              <div className="ruleFilterViewCauseWrapper">
                  <div className="ruleFilterViewCauseHeader">{getTranslation().CAUSE}</div>
                  <div className="ruleFilterViewCauseBody">{oFilterView}</div>
              </div>
              <div className="ruleFilterViewEffectWrapper">
                  <div className="ruleFilterViewEffectHeader">{getTranslation().EFFECT}</div>
                  <div className="ruleFilterViewEffectBody">{oFilterEffectView}</div>
              </div>
          </div>;
      }

    return (
        <div className={sRuleDetailViewWrapperClassName}>
          <div className={sRuleDetailLeftViewClassName} ref={this.ruleDetailLeftViewWrapper}>
            <CommonConfigSectionView context={ViewContextConstants.RULE_DETAILS_VIEW_CONFIG}
                                     data={oModel} disabledFields={aDisabledFields}
                                     sectionLayout={oDataGovernanceRuleConfigLayout.dataGovernanceConfigInformation}
            />
              {oRuleConfigurationSection}
          </div>
          {oRightPanelView}
        </div>
    );
  };

  render() {
    if (this.props.showOnlyCause) {
      return (
          <div className="ruleConfigViewContainer">
            <div className="ruleFilterViewCauseBody">
              {this.getFilterView()}
            </div>
          </div>
      )
    } else {
      return this.getViewNew();
    }
  }
}

export const view = RuleDetailView;
export const events = oEvents;
