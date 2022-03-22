/**
 * Created by DEV on 16/12/2019.
 */
import alertify from '../../../commonmodule/store/custom-alertify-store';
import CS from '../../../libraries/cs';
import GridViewColumnIdConstants from '../tack/grid-view-column-id-constants'
import {gridViewPropertyTypes as oGridViewPropertyTypes} from "../../tack/view-library-constants";
import ContextualGridViewProps from "./model/contextual-grid-view-props"
import TagTypeConstants from './../../../commonmodule/tack/tag-type-constants';
import MockDataForContextTypes
  from "../../../screens/homescreen/screens/settingscreen/tack/mock/mock-data-for-context-types";
import SettingUtils from "../../../screens/homescreen/screens/settingscreen/store/helper/setting-utils";
import oRulesTypes from "../../../commonmodule/tack/mock-data-for-rule-types";
import TaskTypes from "../../../screens/homescreen/screens/settingscreen/tack/mock/mock-data-for-task-types";
import AttributeUtils from "../../../commonmodule/util/attribute-utils";
import AttributeDictionary from "../../../commonmodule/tack/attribute-type-dictionary-new";
import MockAttributeUnits from "../../../commonmodule/tack/mock-data-for-measurement-metrics-and-imperial";
import MockDataForRTEIcons
  from "../../../screens/homescreen/screens/settingscreen/tack/mock/mock-data-for-rich-text-editor-plugins";
import EntityList from "../../../commonmodule/tack/entities-list";
import VersionableStandardAttributes
  from "../../../screens/homescreen/screens/settingscreen/tack/mock/versionable-standard-attributes";
import SettingScreenProps from "../../../screens/homescreen/screens/settingscreen/store/model/setting-screen-props";
import ConfigPropertyTypeDictionary
  from "../../../screens/homescreen/screens/settingscreen/tack/settinglayouttack/config-module-data-model-property-group-type-dictionary";
import MockColors from "../../../screens/homescreen/screens/settingscreen/tack/mock/mock-data-for-tag-colors";
import RelationshipProps
  from "../../../screens/homescreen/screens/settingscreen/store/model/relationship-config-view-props";
import Constants from "../../../commonmodule/tack/constants";
import UserProps from "../../../screens/homescreen/screens/settingscreen/store/model/user-config-view-props";
import MockDataForMappingTypes from "../../../commonmodule/tack/mock-data-for-mapping-types";
import DictionaryForClassName from '../../../commonmodule/tack/dictionary-for-class-name';
import {getTranslations as getTranslation} from "../../../commonmodule/store/helper/translation-manager";
import RuleTypeDictionary from '../../../commonmodule/tack/rule-type-dictionary';
import SSOSettingConfigViewProps
  from "../../../screens/homescreen/screens/settingscreen/store/model/sso-setting-config-view-props";
import EndpointDictionary from "../../../commonmodule/tack/endpoint-type-dictionary";
import ProfileProps from "../../../screens/homescreen/screens/settingscreen/store/model/profile-config-view-props";
import MockDataForEndPointTypes from "../../../commonmodule/tack/mock-data-for-mdm-endpoint-types";
import oProcessTypeDictionary from "../../../commonmodule/tack/process-type-dictionary";
import ProcessEventTypeDictionary from "../../../commonmodule/tack/process-event-type-dictionary";
import MappingTypeDictionary from "../../../commonmodule/tack/mapping-type-dictionary";
import oPhysicalCatalogDictionary from "../../../commonmodule/tack/physical-catalog-dictionary";
import MockDataForPhysicalCatalogAndPortal
  from '../../../commonmodule/tack/mock-data-physical-catalog-and-portal-types';
import KpiProps from "../../../screens/homescreen/screens/settingscreen/store/model/kpi-config-view-props";
import MockDataForEntityBaseTypesDictionary
  from "../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary";
import NatureTypeDictionary from "../../../commonmodule/tack/nature-type-dictionary";
import ProcessConfigViewProps
  from "../../../screens/homescreen/screens/settingscreen/store/model/process-config-view-props";
import MockDataForEntityTypesDictionary
  from "../../../screens/homescreen/screens/settingscreen/tack/mock/mock-data-for-entity-types-dictionary";
import MockDataForProcessEntityTypes
  from "../../../screens/homescreen/screens/settingscreen/tack/mock/mock-data-for-process-entity-types";
import CommonUtils from "../../../commonmodule/util/common-utils";
import SessionStorageManager from "../../../libraries/sessionstoragemanager/session-storage-manager";
import SessionStorageConstants from "../../../commonmodule/tack/session-storage-constants";
import SessionProps from "../../../commonmodule/props/session-props";
import DateFormatForDataLanguageDictionary from "../../../commonmodule/tack/date-format-for-data-language-dictionary";
import SettingScreenModuleList from "../../../commonmodule/tack/setting-screen-module-dictionary";
import oTranslationsProps
  from "../../../screens/homescreen/screens/settingscreen/store/model/translations-config-view-props";
import TranslationsModuleList from "../../../screens/homescreen/screens/settingscreen/tack/translations-module-list";
import GlobalStore from "../../../screens/homescreen/store/global-store";
import TaskTypeDictionary from "../../../commonmodule/tack/task-type-dictionary";
import RuleProps from "../../../screens/homescreen/screens/settingscreen/store/model/rule-config-view-props";
import EndpointTypeDictionary from '../../../commonmodule/tack/endpoint-type-dictionary';
import MockDataForProcessEventTypes from "../../../commonmodule/tack/mock-data-for-mdm-process-event-types";
import MockDataForProcessTriggeringEvent
  from "../../../screens/homescreen/screens/settingscreen/tack/mock/mock-data-for-process-triggering-event";
import MockDataForTimerDefinitionTypes
  from "../../../screens/homescreen/screens/settingscreen/tack/mock/mock-data-for-process-timer-definition-types";
import ViewUtils from "../../utils/view-library-utils";
import SmallTaxonomyViewModel from "../../small-taxonomy-view/model/small-taxonomy-view-model";
import MockAttributeTypes from "../../../commonmodule/tack/mock-data-for-attribute-types";
import GridViewContexts from "../../../commonmodule/tack/grid-view-contexts";
import MockDataForProcessPhysicalCatalog from '../../../commonmodule/tack/mock-data-physical-catalog-and-portal-types';
import TemplateProps from "../../../screens/homescreen/screens/settingscreen/store/model/template-config-view-props";
import MockDataForProcessWorkflowTypes
  from "../../../screens/homescreen/screens/settingscreen/tack/mock/mock-data-for-process-workflow-types";
import MockDataForWorkflowTypesDictionary
  from "../../../screens/homescreen/screens/settingscreen/tack/mock/mock-data-for-workflow-types-dictionary";
import MockDataForRelationshipTypeList
  from "../../../screens/homescreen/screens/settingscreen/tack/mock/mock-data-for-relationship-type";

const oDateFormatDictionary = new DateFormatForDataLanguageDictionary();
const MockDataForPhysicalCatalog = MockDataForPhysicalCatalogAndPortal.physicalCatalogs;


let GridViewStore = (function () {

  let _gridPropertyValueChanged = (sContextId, sPropertyId, sNewValue, oExtraData) => {
    let oGridViewProps = _getGridViewPropsByContext(sContextId) || SettingScreenProps.screen;
    let sContentId = oExtraData.contentId;
    let aGridData = _getGridViewData(oExtraData.pathToRoot, sContentId, oGridViewProps);
    let oRow = CS.find(aGridData, {id: sContentId});
    let oColumn = oRow.properties[sPropertyId];
    let sOldValue = oColumn.value;
    CS.assign(oColumn.referencedData, oExtraData.referencedData);

    if (CS.isEqual(sOldValue, sNewValue)) {
      return;
    }

    oColumn.selectedItems = sNewValue;

    if (oColumn.rendererType === oGridViewPropertyTypes.HTML) {
      oColumn.value = sNewValue.htmlValue;
      oColumn.valueAsHtml = sNewValue.textValue;
    } else if (sPropertyId === GridViewColumnIdConstants.ICON) {
      // As per implementation require object in sNewValue but in case of remove icon sNewvalue is empty string.
      if (sNewValue instanceof Object) {
        oColumn.icon = sNewValue.icon;
        oColumn.iconKey = sNewValue.iconKey;
        oColumn.value = sNewValue.iconKey;
      } else {
        oColumn.value = sNewValue;
        oColumn.icon = sNewValue;
      }
    } else {
      oColumn.value = sNewValue;
    }

    oRow.isDirty = true;
    oGridViewProps.setIsGridDataDirty(true);

    _gridPropertyValueChangeDependencies(oRow, sPropertyId, sNewValue, aGridData, oExtraData);

    let oCallback = oExtraData.callback;
    if (CS.isNotEmpty(oExtraData.callback) && CS.isFunction(oCallback.functionToExecute)) {
      oCallback.functionToExecute(oRow);
    }
  };

  let _gridPropertyValueChangeDependencies = (oRow, sPropertyId, sNewValue, aGridData, oExtraData) => {
    switch (sPropertyId) {
      case GridViewColumnIdConstants.MEASUREMENT_ATTRIBUTE_TYPE:
        _measurementAttributeTypeChanged(oRow, sPropertyId, sNewValue);
        break;

      case GridViewColumnIdConstants.PRECISION:
        _precisionChanged(oRow, sNewValue);
        break;

      case GridViewColumnIdConstants.DEFAULT_UNIT:
        _defaultUnitChanged(oRow);
        break;

      case GridViewColumnIdConstants.TAG_TYPE:
        _tagTypeChanged(oRow, sPropertyId, sNewValue, aGridData, oExtraData);
        break;

      case GridViewColumnIdConstants.SEQUENCE:
        _tabSequenceChanged(oRow);
        break;
    }
  };

  let _tabSequenceChanged = (oRow) => {
    let oColumn = oRow.properties[GridViewColumnIdConstants.SEQUENCE];
    let iSequence = oColumn.value;
    if (CS.isEmpty(iSequence)) {
      oColumn.value = oColumn.selectedItems = "0";
    }
  };

  let _tagTypeChanged = (oRow, sPropertyId, aTypes, aGridData, oExtraData) => {
    let oTag = _getTagFromPath(oExtraData.pathToRoot, oRow.id, aGridData);
    if (oRow.metadata && oRow.metadata.isCreated) {
      if (oTag) {
        oTag[sPropertyId] = aTypes;
      }
    }
    oRow.properties[GridViewColumnIdConstants.IS_MULTISELECT] = {}; //disable multiselect for all tags except YN
    oRow.properties[GridViewColumnIdConstants.DEFAULT_VALUE] = {}; //disable default value for all tags except YN
    if (aTypes[0] === TagTypeConstants.YES_NEUTRAL_TAG_TYPE) {
      oTag && _tagTypeChangedToYNTagType(oRow, oTag, oExtraData);
    }
    oRow.actionItemsToShow = ["delete", "create"];
  };

  let _tagTypeChangedToYNTagType = (oRow, oTag, oExtraData) => {
    let aSelectedItems = [];
    oRow.properties[GridViewColumnIdConstants.DEFAULT_VALUE] = _getMSSObject(oTag.children, aSelectedItems, "gridTagDefaultValue", true, false, "", false);
    oRow.properties[GridViewColumnIdConstants.DEFAULT_VALUE].value = aSelectedItems;
    oRow.properties[GridViewColumnIdConstants.IS_MULTISELECT].value = false;

    if (CS.isEmpty(oRow.properties[GridViewColumnIdConstants.LINKED_MASTER_TAG_ID])) {
      let oMasterList = oExtraData.masterTagList;
      let aMasterListItems = CS.values(oMasterList);
      let aSelectedItems = oTag.linkedMasterTagId ? [oTag.linkedMasterTagId] : [];
      oRow.properties[GridViewColumnIdConstants.LINKED_MASTER_TAG_ID] = _getMSSObject(aMasterListItems, aSelectedItems, "gridTagLinkedMasterTag", true, false, "", false);
      oRow.properties[GridViewColumnIdConstants.LINKED_MASTER_TAG_ID].value = aSelectedItems;
    }
  };

  let _getTagFromPath = (sPathToRoot, sContentId, aGridData) => {
    let aPath = CS.split(sPathToRoot, SettingUtils.getSplitter());
    if (aPath[0] != sContentId && aPath[1]) {
      var oParent = CS.find(aGridData, {
        id: aPath[0]
      }) || {};
      aGridData = oParent.children;
    }
    return CS.find(aGridData, {id: sContentId});
  };

  let _defaultUnitChanged = (oRow) => {
    let sAttributeType = (oRow.properties[GridViewColumnIdConstants.MEASUREMENT_ATTRIBUTE_TYPE] &&
        oRow.properties[GridViewColumnIdConstants.MEASUREMENT_ATTRIBUTE_TYPE].value[0]) ||
        (oRow.properties[GridViewColumnIdConstants.DEFAULT_VALUE]
            && oRow.properties[GridViewColumnIdConstants.DEFAULT_VALUE].type);
    if (AttributeUtils.isAttributeTypePrice(sAttributeType)) {
      let sValue = oRow.properties[GridViewColumnIdConstants.DEFAULT_VALUE].value;
      if (CS.isNotEmpty(CS.toString(sValue))) {
        alertify.message(getTranslation().DEFAULT_UNIT_CHANGE_EXCEPTION);
      }
    } else if (!AttributeUtils.isMeasurementAttributeTypeCustom(sAttributeType)) {
      let sType = oRow.properties[GridViewColumnIdConstants.DEFAULT_UNIT].value[0];
      let aAttributeUnits = CS.find(oRow.properties[GridViewColumnIdConstants.DEFAULT_UNIT].items, {id: sType});
      CS.isNotEmpty(oRow.properties[GridViewColumnIdConstants.DEFAULT_VALUE]) &&
      (oRow.properties[GridViewColumnIdConstants.DEFAULT_VALUE].defaultUnit = aAttributeUnits.unit);
    }
  };

  let _precisionChanged = (oRow, sNewValue) => {
    oRow.properties[GridViewColumnIdConstants.DEFAULT_VALUE] && (oRow.properties[GridViewColumnIdConstants.DEFAULT_VALUE].precision = sNewValue);
  };

  let _measurementAttributeTypeChanged = (oRow, sPropertyId, aType) => {
    let oMeasurementMetricAndImperial = new MockAttributeUnits();
    let sType = aType[0];

    if (AttributeUtils.isMeasurementAttributeTypeCustom(sType)) {
      oRow.properties[GridViewColumnIdConstants.DEFAULT_UNIT] = {
        value: "",
        valueAsHtml: "",
        rendererType: oGridViewPropertyTypes.HTML
      };
      oRow.properties[GridViewColumnIdConstants.PRECISION] = {value: 0};
      oRow.properties[GridViewColumnIdConstants.DEFAULT_VALUE] = {
        value: null,
        precision: 0,
        rendererType: oGridViewPropertyTypes.NUMBER
      };
    }
    else {
      let aAttributeUnits = oMeasurementMetricAndImperial[sType];

      if (aAttributeUnits) {
        var aAttributeMSSList = [];
        CS.each(aAttributeUnits, function (oAttributeUnit) {
          var oAttributeMSS = CS.cloneDeep(oAttributeUnit);
          oAttributeMSS.label = oAttributeUnit.label + ' (' + oAttributeUnit.unitToDisplay + ')';
          aAttributeMSSList.push(oAttributeMSS);
        });

        let oAttributeUnit = CS.find(aAttributeUnits, {isBase: true});
        if (oAttributeUnit) {
          oRow.properties[GridViewColumnIdConstants.DEFAULT_UNIT] = {
            value: [oAttributeUnit.id],
            items: aAttributeMSSList,
            singleSelect: true,
            disableCross: true,
            rendererType: oGridViewPropertyTypes.DROP_DOWN
          }; // select a unit by default
        }

        oRow.properties.precision = {value: 0};
        oRow.properties.defaultValue = {
          value: "",
          rendererType: oGridViewPropertyTypes.MEASUREMENT,
          defaultUnit: oAttributeUnit.unit,
          type: sType,
          precision: 0,
          converterVisibility: false
        };

        if (oRow.properties.type && oRow.properties.type.value[0] === AttributeDictionary.PRICE) {
          oRow.properties[GridViewColumnIdConstants.IS_ORDER] = {value: false};
          oRow.properties[GridViewColumnIdConstants.IS_SALES] = {value: true};
        }
      }
    }
  };

  let _getGridViewData = (sPathToRoot, sParentId, oGridViewProps) => {
    let aGridData = oGridViewProps.getGridViewData();
    /** TODO : Check if pathToRoot is required only for tag config i.e. nested grid view **/
    if (sPathToRoot && sPathToRoot != sParentId) {
      let aPath = CS.split(sPathToRoot, SettingUtils.getSplitter());
      aPath.splice(-1);
      CS.forEach(aPath, (sId) => {
        let oGridLevel = CS.find(aGridData, {id: sId});
        if (oGridLevel) {
          aGridData = oGridLevel.children;
          return false;
        }
      });
    }
    return aGridData;
  };

  let _getGridViewPropsByContext = (sContextId) => {
    if (!sContextId) {
      throw "Grid context should not be empty"
    }
    let oGridViewProps = ContextualGridViewProps.getGridViewPropsByContext(sContextId);
    return oGridViewProps ? oGridViewProps.gridViewProps : null;
  };

  let _getColumnOrganizerPropsByContext = (sContextId) => {
    if (!sContextId) {
      throw "Grid context should not be empty inside grid-view-store"
    }
    let oGridViewProps = ContextualGridViewProps.getGridViewPropsByContext(sContextId);
    return oGridViewProps ? oGridViewProps.columnOrganizerProps : null;
  };

  let _getMSSObject = function (aItems, aSelectedItems, sContext, bSingleSelect, bDisabled, sLabel, bDisabledCross,
                                bShowCreateButton, bIsLoadMoreEnabled, sSearchText) {
    return {
      disabled: bDisabled,
      label: sLabel,
      items: aItems,
      selectedItems: aSelectedItems,
      singleSelect: bSingleSelect,
      context: sContext,
      disableCross: bDisabledCross,
      showCreateButton: bShowCreateButton,
      isLoadMoreEnabled: bIsLoadMoreEnabled,
      searchText: sSearchText
    }
  };

  let _getLazyMSSObject = function (sLabel, bIsDisabled, aItems, aSelectedItems, sContext, oRegResponseInfo, sPropertyType,
                                    bIsSingleSelect, bShowCreateButton, bDisableCross, bIsLoadMoreEnable, bShowSelectedInDropdown,
                                    oConfigDetails) {
    let oReferencedData = {};
    switch (sPropertyType) {
      case GridViewColumnIdConstants.DATA_RULES:
        oReferencedData = ProfileProps.getReferencedDataRules();
        break;
      case GridViewColumnIdConstants.MAPPINGS:
        oReferencedData = ProfileProps.getReferencedMappings();
        break;
      case GridViewColumnIdConstants.PROCESSES:
        oReferencedData = ProfileProps.getReferencedProcesses();
        break;
      case GridViewColumnIdConstants.JMS_PROCESSES:
        oReferencedData = ProfileProps.getReferencedJmsProcesses();
        break;
      case GridViewColumnIdConstants.ENDPOINT_TYPE:
        let aMockDataForEndPointTypes = new MockDataForEndPointTypes();
        let oEndpointType = CS.find(aMockDataForEndPointTypes, function (oEndpointType) {
          return aSelectedItems[0] == oEndpointType.id
        });
        oReferencedData[oEndpointType.id] = oEndpointType;
        break;
      case GridViewColumnIdConstants.AUTHORIZATION_MAPPING:
        oReferencedData = ProfileProps.getReferencedAuthorizationMappings();
        break;
      case GridViewColumnIdConstants.MAPPING_TYPE:
        let aMockDataForMappingTypes = new MockDataForMappingTypes();
        let oMappingType = CS.find(aMockDataForMappingTypes, function (oMappingType) {
          return aSelectedItems[0] == oMappingType.id
        });
        oReferencedData[oMappingType.id] = oMappingType;
        break;
      case GridViewColumnIdConstants.DASHBOARD_TAB:
        oReferencedData = oConfigDetails.referenceData;
        break;
      case GridViewColumnIdConstants.NATURE_TYPE:
        oReferencedData = oConfigDetails.referencedKlasses ? oConfigDetails.referencedKlasses : {};
        break;
      case GridViewColumnIdConstants.STATUS_TAG:
      case GridViewColumnIdConstants.PRIORITY_TAG:
        oReferencedData = SettingUtils.getAppData().getTagMap();
        break;
    }

    return {
      label: sLabel,
      disabled: bIsDisabled,
      items: aItems,
      selectedItems: aSelectedItems,
      singleSelect: bIsSingleSelect,
      context: sContext,
      requestResponseInfo: oRegResponseInfo,
      referencedData: oReferencedData,
      showCreateButton: bShowCreateButton,
      disableCross: bDisableCross,
      isLoadMoreEnabled: bIsLoadMoreEnable,
      showSelectedInDropdown: bShowSelectedInDropdown
    };
  };

  let _getProcessLazyMSSObject = function (aSelectedItems, oReferencedEntities, sContext, bIsMultiSelect, bCannotRemove, tagGroupId) {
    let oReqResObj = {
      requestType: "configData",
      entityName: sContext
    };
    if (sContext === 'endpoints') {
      oReqResObj.customRequestModel = {
        types: [EndpointTypeDictionary.INBOUND_ENDPOINT]
      }
    } else if (sContext === 'tagValues') {
      oReqResObj.customRequestModel = {
        tagGroupId: tagGroupId
      }
    }

    let oHandler = CS.noop;
    let sSplitter = ViewUtils.getSplitter();
    //return _getLazyMSSObject("", false, [], aSelectedItems, 'process'+sSplitter+sContext, oReqResObj, )
    return {
      singleSelect: !bIsMultiSelect,
      context: 'process' + sSplitter + sContext,
      disabled: false,
      selectedItems: aSelectedItems,
      disableCross: bCannotRemove,
      showSelectedInDropdown: true,
      requestResponseInfo: oReqResObj,
      isLoadMoreEnabled: false,
      referencedData: oReferencedEntities,
      onApply: oHandler
    };
  };

  let _getMeasurementAttributeTypeMSSObject = (oSelectedAttribute) => {
    let aAttributeTypeList = new MockAttributeTypes();
    let sType = oSelectedAttribute.type;
    let aSelectedList = [];
    let aAttributeMSSList = [];

    CS.forEach(aAttributeTypeList, function (oType) {
      if (oType.isUnitAttribute) {
        if (sType == oType.value) {
          aSelectedList.push(sType);
        }
        aAttributeMSSList.push({
          id: oType.value,
          label: oType.name
        });
      }
    });
    /**Info: Price attribute is removed from measurement type change dropdown,
     * Coz after adminUI revamp Price attributes now have their own grouping*/
    CS.remove(aAttributeMSSList, {id: AttributeDictionary.PRICE});

    return {
      disabled: oSelectedAttribute.isStandard,
      label: "",
      items: aAttributeMSSList,
      selectedItems: aSelectedList,
      singleSelect: true,
      context: "measurementAttributeType",
      disableCross: true
    }
  };

  let _getAttributeUnitMSSObject = (oSelectedAttribute) => {
    let aSelectedList = [];
    let oMeasurementMetricAndImperial = new MockAttributeUnits();
    let aAttributeUnits = oMeasurementMetricAndImperial[oSelectedAttribute.type];
    let aAttributeMSSList = [];
    CS.each(aAttributeUnits, function (oAttributeUnit) {
      let oAttributeMSS = CS.cloneDeep(oAttributeUnit);
      oAttributeMSS.label = oAttributeUnit.label + ' (' + oAttributeUnit.unitToDisplay + ')';
      aAttributeMSSList.push(oAttributeMSS);

      if (oAttributeUnit.unit == oSelectedAttribute.defaultUnit) {
        aSelectedList.push(oAttributeUnit.id);
      }
    });

    return {
      disabled: false,
      label: "",
      items: aAttributeMSSList,
      selectedItems: aSelectedList,
      singleSelect: true,
      context: "defaultUnit",
      disableCross: true
    }
  };

  let _getLazyMSSModelForAttributesAndTags = function (aSelectedItems, oReferencedAttributes, sContext) {
    let oReqResObj = {
      requestType: "configData",
      entityName: sContext
    };
    let bIsMultiSelect = true;
    let bCannotRemove = false;

    if (sContext === 'attributes') {
      oReqResObj.typesToExclude = [AttributeDictionary.CALCULATED, AttributeDictionary.CONCATENATED];
    }

    let oHandler = CS.noop; //this.handleProcessConfigMssValueChanged.bind(this, sKey);

    return {
      isMultiSelect: bIsMultiSelect,
      context: 'process',
      disabled: false,
      selectedItems: aSelectedItems,
      cannotRemove: bCannotRemove,
      showSelectedInDropdown: true,
      requestResponseInfo: oReqResObj,
      isLoadMoreEnabled: false,
      referencedData: oReferencedAttributes,
      onApply: oHandler
    };
  };
  /** To Prepare Lazy MSS model for Organization **/
  let _getLazyMSSModelForOrganization = function (aSelectedItems, oReforganizations, bIsSingleSelect) {
    let oReqResObj = {
      requestType: "customType",
      requestURL: "config/organizations/getall",
      responsePath: ["success", "list"],
      customRequestModel: {},
    };
    let oHandler = CS.noop; //this.handleProcessConfigMssValueChanged.bind(this, sKey);

    return {
      singleSelect: bIsSingleSelect,
      context: 'process',
      disabled: false,
      selectedItems: aSelectedItems,
      cannotRemove: false,
      showSelectedInDropdown: true,
      requestResponseInfo: oReqResObj,
      isLoadMoreEnabled: false,
      referencedData: oReforganizations,
      onApply: oHandler
    };
  }

  var _getTemplateMSSObject = function (oSelectedTemplate, oRegResponseInfo, context) {
    let oTemplateReferencedData = TemplateProps.getTemplateReferencedData();
    let oList = {};
    let aList = [];
    let aSelectedList = [];
    let sContext = '';

    switch(context){
      case 'propertyCollections':
        oList = oTemplateReferencedData.referencedPropertyCollections;
        aList = CS.values(oList);
        aSelectedList = oSelectedTemplate.propertyCollectionIds;
        sContext = "templatePropertyCollection";
        break;

      case 'contexts':
        oList = oTemplateReferencedData.referencedContexts;
        aList = CS.values(oList);
        aSelectedList = oSelectedTemplate.contextIds;
        sContext = "templateContext";
        break;

      case 'relationships':
        oList = oTemplateReferencedData.referencedRelationships;
        aList = CS.values(oList);
        aSelectedList = oSelectedTemplate.relationshipIds;
        sContext = "templateRelationship";
        break;

      case 'natureRelationships':
        oList = oTemplateReferencedData.referencedNatureRelationships;
        aList = CS.values(oList);
        aSelectedList = oSelectedTemplate.natureRelationshipIds;
        sContext = "templateNatureRelationship";
        break;

      default: break;
    }

    return {
      disabled: false,
      label: "",
      items: aList,
      selectedItems: aSelectedList,
      singleSelect: false,
      context: sContext,
      referencedData: oList,
      requestResponseInfo: oRegResponseInfo
    }
  };

  let _getGridDefaultValueData = (sContextId, oProcessedGridProperties, oGridData, oColumn) => {
    if (sContextId === GridViewContexts.ATTRIBUTE) {
      oProcessedGridProperties = {
        value: oGridData[oColumn.id]
      };
      if (AttributeUtils.isAttributeTypeMeasurement(oGridData.type)) {
        oProcessedGridProperties.disableNumberLocaleFormatting = oGridData.hideSeparator;
        if (AttributeUtils.isMeasurementAttributeTypeCustom(oGridData.type)) {
          oProcessedGridProperties.precision = oGridData.precision;
          oProcessedGridProperties.rendererType = oGridViewPropertyTypes.NUMBER;
        }
        else {
          oProcessedGridProperties.precision = oGridData.precision;
          oProcessedGridProperties.defaultUnit = oGridData.defaultUnit;
          oProcessedGridProperties.type = oGridData.type;
          oProcessedGridProperties.converterVisibility = false;
          oProcessedGridProperties.rendererType = oGridViewPropertyTypes.MEASUREMENT;
          if(AttributeUtils.isAttributeTypePrice(oGridData.type)) {
            oProcessedGridProperties.disableValueConversionByDefaultUnit = true;
            oProcessedGridProperties.disableDefaultUnit = true;
          }
        }
      } else if (AttributeUtils.isAttributeTypeHtml(oGridData.type)) {
        oProcessedGridProperties.value = oGridData && oGridData.valueAsHtml || "";
        oProcessedGridProperties.valueAsHtml = oGridData && oGridData.defaultValue || "";
        oProcessedGridProperties.rendererType = oGridViewPropertyTypes.HTML;
        oProcessedGridProperties.toolbarIcons = oGridData.validator.allowedRTEIcons || "";
      } else if (AttributeUtils.isAttributeTypeNumber(oGridData.type)) {
        oProcessedGridProperties.precision = oGridData.precision;
        oProcessedGridProperties.rendererType = oGridViewPropertyTypes.NUMBER;
        oProcessedGridProperties.disableNumberLocaleFormatting = oGridData.hideSeparator;
      } else if (AttributeUtils.isAttributeTypeDate(oGridData.type)) {
        oProcessedGridProperties.rendererType = oGridViewPropertyTypes.DATE;
      } else if (AttributeUtils.isAttributeTypeConcatenated(oGridData.type) || AttributeUtils.isAttributeTypeCalculated(oGridData.type)) {
        /*cannot do oDefaultValueProperty = {} as it changes the reference of oDefaultValueProperty*/
        oProcessedGridProperties = {}; //default value field disabled in case of concatenated & calculated attribute (even though we get the field in data)
      } else {
        oProcessedGridProperties.rendererType = oGridViewPropertyTypes.TEXT;
      }
    }
    else if (sContextId === GridViewContexts.TAG) {
      if (oGridData.tagType === TagTypeConstants.YES_NEUTRAL_TAG_TYPE) {
        let aSelectedItems = oGridData.defaultValue && oGridData.defaultValue.id ? [oGridData.defaultValue.id] : [];
        oProcessedGridProperties = _getMSSObject(oGridData.children, aSelectedItems, "gridTagDefaultValue", true, false, "", false);
        oProcessedGridProperties.value = aSelectedItems;
      } else {
        oProcessedGridProperties = {}; //default value disabled for all tag types except YN
      }
    }
    return oProcessedGridProperties;
  };

  let _getAvailableTypeMSSObject = (oSelectedAttribute) => {
    let aSelectedEntityList = oSelectedAttribute.availability;
    let aAttributeMSSList = [];
    let aSortedEntityList = CS.sortBy(EntityList(), 'label');

    CS.forEach(aSortedEntityList, function (oEntity) {
      aAttributeMSSList.push({
        id: oEntity.id,
        label: oEntity.label
      });
    });

    return {
      label: "",
      items: aAttributeMSSList,
      selectedItems: aSelectedEntityList,
      singleSelect: false,
      context: "EntityType",
      disableCross: false,
    }
  };

  let _getUserGenderMSSModel = function (aSelectedItems) {
    let oGenderObject = {
      label: "",
      disabled: false,
      items: [
        {
          id: "male",
          label: getTranslation().MALE
        },
        {
          id: "female",
          label: getTranslation().FEMALE
        },
        {
          id: "other",
          label: getTranslation().OTHER
        }
      ],
      singleSelect: true,
      context: "users"
    };
    oGenderObject.selectedItems = aSelectedItems || [];
    return oGenderObject;
  };

  let _getSmallTaxonomyViewModel = (oMultiTaxonomyData) => {
    let oSelectedMultiTaxonomyList = oMultiTaxonomyData.selected;
    let oMultiTaxonomyListToAdd = oMultiTaxonomyData.notSelected;
    let sRootTaxonomyId = ViewUtils.getTreeRootId();
    let oProperties = {};
    oProperties.headerPermission = {};
    return new SmallTaxonomyViewModel('', oSelectedMultiTaxonomyList, oMultiTaxonomyListToAdd, getTranslation().ADD_TAXONOMY, 'forMultiTaxonomy', sRootTaxonomyId, oProperties)
  };

  let _getFilterTagTypeListAccordingToGroup = (aTagTypeList) => {
    let sSelectedItem = SettingScreenProps.screen.getSelectedLeftNavigationTreeItem();
    switch (sSelectedItem) {
      case ConfigPropertyTypeDictionary.LOV:
        aTagTypeList = CS.filter(aTagTypeList, (oType) => {
          return oType.id === TagTypeConstants.YES_NEUTRAL_NO_TAG_TYPE ||
              oType.id === TagTypeConstants.YES_NEUTRAL_TAG_TYPE ||
              oType.id === TagTypeConstants.RULER_TAG_TYPE ||
              oType.id === TagTypeConstants.RANGE_TAG_TYPE
        });
        break;

      case ConfigPropertyTypeDictionary.STATUS:
        aTagTypeList = CS.filter(aTagTypeList, (oType) => {
          return oType.id === TagTypeConstants.STATUS_TAG_TYPE ||
              oType.id === TagTypeConstants.LIFECYCLE_STATUS_TAG_TYPE ||
              oType.id === TagTypeConstants.LISTING_STATUS_TAG_TYPE
        });
        break;
    }

    return CS.sortBy(aTagTypeList, 'label');
  };

  let _getAllowedRTEIconsModelForAttributeConfig = (oSelectedAttribute) => {
    if (AttributeUtils.isAttributeTypeHtml(oSelectedAttribute.type)) {
      var aAllowedSiblingFrames = oSelectedAttribute.validator.allowedRTEIcons;
      var aAttributeIconList = CS.sortBy(CS.cloneDeep(MockDataForRTEIcons), 'name');
      var aSelectedList = [];
      var aAttributeMSSList = [];

      CS.forEach(aAttributeIconList, (oIcon) => {
        if (CS.includes(aAllowedSiblingFrames, oIcon.id)) {
          aSelectedList.push(oIcon.id);
        }
        aAttributeMSSList.push({
          id: oIcon.id,
          label: oIcon.name
        })
      });

      return {
        disabled: false,
        label: "",
        items: aAttributeMSSList,
        selectedItems: aSelectedList,
        singleSelect: false,
        context: "attributeRTEIcon"
      }
    }
    return null;
  };

  let _getListForDefaultHTMLStyle = (aRTEIconsList) => {
    let aListToGet = ['bold', 'italic'];
    let aListToReturn = [];
    CS.forEach(aRTEIconsList, function (oIcon) {
      if (CS.includes(aListToGet, oIcon.id)) {
        aListToReturn.push(CS.cloneDeep(oIcon));
      }
    });
    return aListToReturn;
  };

  let _setProcessTypes = function (oActiveProcess, sColumnId) {
    let sSplitter = ViewUtils.getSplitter();
    let oModel = {};
    switch (sColumnId) {
      case GridViewColumnIdConstants.EVENT_TYPE :
        oModel.eventType = {
          items: MockDataForProcessEventTypes(),
          selectedItems: oActiveProcess.eventType,
          cannotRemove: true,
          context: "process" + sSplitter + "eventType",
          disabled: true,
          label: "",
          singleSelect: true
        };
        break;
      case GridViewColumnIdConstants.TRIGGERING_TYPE :
        let aItems = MockDataForProcessTriggeringEvent();
        let aExcludedItems = [];
        if (oActiveProcess.eventType === "fileuploadevent") {
          aExcludedItems = ["afterSwitchType"];
        }
        else if (oActiveProcess.eventType === "customevent") {
          aExcludedItems = ["afterImport", "transfer"];
        }
        let aFilteredItemList = CS.filter(aItems, function (oItem) {
          return !CS.includes(aExcludedItems, oItem.id);
        });
        let sContext = "process" + sSplitter + "triggeringEventType";
        oModel.triggeringType = {
          items: aFilteredItemList,
          selectedItems: oActiveProcess.triggeringType,
          cannotRemove: true,
          context: sContext,
          disabled: false,
          label: "",
          singleSelect: true
        };
        break;
      case "physicalCatalogIds" :
        let aPhysicalCatalogItems = MockDataForProcessPhysicalCatalog.physicalCatalogs();
        let isSingleSelect = false;
        if(oActiveProcess.workflowType === MockDataForWorkflowTypesDictionary.SCHEDULED_WORKFLOW){
          isSingleSelect = true;
          aPhysicalCatalogItems = CS.filter(aPhysicalCatalogItems, function (oItem) {
            return (oItem.id !== oPhysicalCatalogDictionary.DATAINTEGRATION);
          });
        }
        oModel.physicalCatalogIds = {
          items: aPhysicalCatalogItems,
          selectedItems: oActiveProcess.physicalCatalogIds,
          cannotRemove: false,
          context: "process" + sSplitter + "physicalCatalogIds",
          disabled: false,
          label: "",
          singleSelect: isSingleSelect,
        };
        break;
      case "timerDefinitionType" :
        oModel.timerDefinitionType = {
          items: MockDataForTimerDefinitionTypes(),
          selectedItems: oActiveProcess.timerDefinitionType,
          cannotRemove: true,
          context: "process" + sSplitter + "timerDefinitionType",
          disabled: false,
          label: "",
          singleSelect: true
        };
        break;
      case GridViewColumnIdConstants.WORKFLOW_TYPE :
        oModel.workflowType = {
          items: MockDataForProcessWorkflowTypes(),
          selectedItems: oActiveProcess.workflowType,
          cannotRemove: true,
          context: "process" + sSplitter + "workflowType",
          disabled: true,
          label: "",
          singleSelect: true
        };
        break;
    }
    return oModel;
  };

  let _setRelationshipTypes = function (oRelationship) {
    let oConfigDetails = RelationshipProps.getSelectedRelationshipConfigDetails();
    let oReferencedData = oConfigDetails.referencedTabs;
    let sSelectedTabId = oRelationship.tabId;
    let oSelectedTab = oReferencedData[sSelectedTabId];
    let aSelectedTabObject = CS.isEmpty(oSelectedTab) ? [] : [oSelectedTab];
    let aSelectedTabIds = CS.isEmpty(oSelectedTab) ? [] : [oSelectedTab.id];
    var aTabList = [];
    CS.forEach(oReferencedData, function (oElement, sKey) {
      aTabList.push(oElement);
    });

    let oReqResObj = {
      requestType: "configData",
      entityName: "tabs",
      typesToExclude: [Constants.TAB_RENDITION, Constants.TAB_DUPLICATE_ASSETS]
    };

    return {
      isMultiSelect: false,
      items: aTabList,
      selectedItems: aSelectedTabIds,
      selectedObjects: aSelectedTabObject,
      // searchText: ViewLibraryUtils.getEntitySearchText(),
      showCreateButton: true,
      context: `tab`,
      isLoadMoreEnabled: true,
      referencedData: oReferencedData,
      requestResponseInfo: oReqResObj,
      disableCross: true,
    };
  };

  let _setTypesAccordingToContext = (oActiveContext, sContext) => {
    let oModel = {
      label: "",
      type: {},
      code: ""
    };

    let oMockDataForTypes = {};
    let sModelContext = "";
    let sSplitter = ViewUtils.getSplitter();
    let sSelectedType = [oActiveContext.type];
    switch (sContext) {
      case GridViewContexts.CONTEXT_VARIANT:
        oMockDataForTypes = new MockDataForContextTypes();
        sModelContext = "context" + sSplitter + "type"
        break;

      case GridViewContexts.RULE:
        oMockDataForTypes = new oRulesTypes();
        sModelContext = "ruleConfigDetail";
        break;

      case GridViewContexts.TASK:
        oMockDataForTypes = new TaskTypes();
        break;

      case GridViewContexts.RELATIONSHIP:
        oMockDataForTypes = new MockDataForRelationshipTypeList();
        sModelContext = "relationshipType";
        let oSelectedItem = oActiveContext.isLite ? CS.find(oMockDataForTypes, {id: "liteRelationship"})
                                             : CS.find(oMockDataForTypes, {id: "relationship"});
        sSelectedType = [oSelectedItem.id];
    }

    oModel.type = {
      items: oMockDataForTypes,
      selectedItems: sSelectedType,
      cannotRemove: true,
      context: sModelContext,
      disabled: false,
      label: "",
      singleSelect: true,
      disableCross: true,
      hideTooltip: true
    };

    oModel.label = oActiveContext.label;
    oModel.code = oActiveContext.code;

    return oModel;
  };

  let _getLabelGridPropertyData = (oGridData, oColumn) => {
    return {
      value: oGridData[oColumn.id],
      bIsMultiLine: false,
      showTooltip: true,
      placeholder: oGridData["code"]
    };
  };

  let _getCodeGridPropertyData = (oGridData, oColumn) => {
    return {
      value: oGridData[oColumn.id],
      isDisabled: true,
      bIsMultiLine: false
    };
  };

  let _getTranslationGridCodePropertyView = (oGridData, oProcessedGridData, oProcessedGridProperties, oColumn) => {
    let sModuleId = oTranslationsProps.getSelectedModule();
    let sChildModuleId = oTranslationsProps.getSelectedChildModule();
    let oTranslationsModuleList = new TranslationsModuleList();
    if (sModuleId == SettingScreenModuleList.STATIC_TRANSLATION) {
      if (oGridData.hasOwnProperty("id")) {
        oProcessedGridProperties = {
          value: oGridData.id,
          isDisabled: true,
          bIsMultiLine: false,
        };
      }
      oColumn.label = "KEY";
    } else {
      if (oGridData.hasOwnProperty(oColumn.id)) {
        oProcessedGridProperties = {
          value: oGridData[oColumn.id],
          isDisabled: true,
          bIsMultiLine: true,
          isExpandable: oProcessedGridData.isExpandable
        };
      }
      if (sModuleId == SettingScreenModuleList.CLASS) {
        oColumn.label = CS.find(oTranslationsModuleList.ClassModules, {id: "translations" + ViewUtils.getSplitter() + sChildModuleId}).label;
      } else if (sModuleId == SettingScreenModuleList.TABS) {
        oColumn.label = CS.find(oTranslationsModuleList.TabsModules, {id: "translations" + ViewUtils.getSplitter() + sChildModuleId}).label;
      } else {
        oColumn.label = CS.find(oTranslationsModuleList.SettingScreenModules, {id: "translations" + ViewUtils.getSplitter() + sModuleId}).label;
      }
    }
    return oProcessedGridProperties;
  };

  let _fillBasicGridDetails = (sContextId, oGridData, iIndex, iTotalCount, oProcessedGridData, sTagPathToRoot) => {
    let oGridViewPropsByContext = _getGridViewPropsByContext(sContextId);
    let oGridViewVisualData = oGridViewPropsByContext.getGridViewVisualData();
    oProcessedGridData.id = sContextId === GridViewContexts.AUDIT_LOG_EXPORT_STATUS ? oGridData.assetId : oGridData.id;
    oProcessedGridData.properties = {};
    oProcessedGridData.children = [];
    let oAuditLogProps = SettingScreenProps.auditLogProps;


    if (sContextId === GridViewContexts.TAG) {
      oGridViewVisualData[oGridData.id] = {
        isExpanded: false
      }
      oProcessedGridData.children = [];
      let sPathToAdd = sTagPathToRoot;
      if (!sPathToAdd && oGridData.parent) {
        sPathToAdd = oGridData.parent.id;
      }
      oProcessedGridData.pathToRoot = sPathToAdd ? sPathToAdd + ViewUtils.getSplitter() + oGridData.id : oGridData.id;
      oProcessedGridData.pathToAdd = sPathToAdd;
      oProcessedGridData.metadata = {};
      if (oGridData.isCreated) {
        oProcessedGridData.metadata.isCreated = true;
      }
      if (oGridData.tagType && oGridData.tagType === TagTypeConstants.TAG_TYPE_BOOLEAN) {
        oProcessedGridData.actionItemsToShow = ["manageEntity", "delete"];
      } else {
        if (!sPathToAdd) {
          oProcessedGridData.actionItemsToShow = ["manageEntity", "delete"];
          oProcessedGridData.actionItemsToShow.push("create");
        } else {
          oProcessedGridData.actionItemsToShow = ["deleteValues"];
          if (iIndex != 0) {
            oProcessedGridData.actionItemsToShow.push("moveUp");
          } else {
            //To show empty space
            oProcessedGridData.actionItemsToShow.push("dummy");
          }

          if (iIndex < (iTotalCount - 1)) {
            oProcessedGridData.actionItemsToShow.push("moveDown");
          }
        }
      }
    }
    else if (oAuditLogProps.getIsShowExportStatusDialogActive()) {
      oProcessedGridData.assetId = oGridData.assetId;
      oProcessedGridData.isExpanded = false;
      oProcessedGridData.properties = {};
    }
    else if (sContextId === GridViewContexts.TRANSLATIONS) {
      let sSelectedProperty = oTranslationsProps.getSelectedProperty();
      let sModuleId = oTranslationsProps.getSelectedModule();
      if (!oGridViewVisualData[oGridData.id]) {
        oGridViewVisualData[oGridData.id] = {
          isExpanded: false
        }
      }
      oProcessedGridData.disableRow = sSelectedProperty == "placeholder" && (oGridData.type === AttributeDictionary.CALCULATED ||
          oGridData.type === AttributeDictionary.CONCATENATED);
      let sPathToAdd = sTagPathToRoot;
      if (!sPathToAdd && oGridData.parent) {
        sPathToAdd = oGridData.parent.id;
      }
      oProcessedGridData.pathToRoot = sPathToAdd ? sPathToAdd + ViewUtils.getSplitter() + oGridData.id : oGridData.id;

      oProcessedGridData.actionItemsToShow = [];
      oProcessedGridData.isExpandable = (sModuleId == SettingScreenModuleList.TAG_VALUES &&
          oGridData.tagType != TagTypeConstants.TAG_TYPE_BOOLEAN && !sTagPathToRoot);
    }
    else {
      oProcessedGridData.isExpanded = false;
      oProcessedGridData.actionItemsToShow = [];
      /**Standard attribute should not be deleted*/
      let sSelectedTreeNode = SettingScreenProps.screen.getSelectedLeftNavigationTreeItem();
      let bIsStandardAttributeScreen = (sSelectedTreeNode === ConfigPropertyTypeDictionary.STANDARD);
      if (!bIsStandardAttributeScreen) {
        oProcessedGridData.actionItemsToShow.push("delete");
      }

      let aEntitiesToShowEdit = [
        GridViewContexts.RELATIONSHIP,
        GridViewContexts.TABS_CONFIG,
        GridViewContexts.END_POINT,
        GridViewContexts.MAPPING,
        GridViewContexts.CONTEXT_VARIANT,
        GridViewContexts.AUTHORIZATION_MAPPING,
        GridViewContexts.PROCESS,
        GridViewContexts.RULE,
        GridViewContexts.GOLDEN_RECORD_RULE,
        GridViewContexts.KPI,
        GridViewContexts.USER
      ];
      if (CS.includes(aEntitiesToShowEdit, sContextId)) {
        oProcessedGridData.actionItemsToShow.unshift("edit");
      }

      let aEntitiesToDisableManageEntity = [
        GridViewContexts.TEMPLATE,
        GridViewContexts.END_POINT,
        GridViewContexts.AUTHORIZATION_MAPPING,
        GridViewContexts.PROCESS,
        GridViewContexts.RULE,
        GridViewContexts.GOLDEN_RECORD_RULE,
        GridViewContexts.KPI,
        GridViewContexts.SSO_SETTING
      ];
      if (!CS.includes(aEntitiesToDisableManageEntity, sContextId)) {
        oProcessedGridData.actionItemsToShow.unshift("manageEntity");
      }
    }
  };

  let _getSelectedUILanguageDateFormat = () => {
    let sUILanguageId = SessionStorageManager.getPropertyFromSessionStorage(SessionStorageConstants.SELECTED_UI_LANGUAGE_CODE);
    let oLanguageInfoData = SessionProps.getLanguageInfoData();
    let aUILanguages = oLanguageInfoData.userInterfaceLanguages;
    let oSelectedUILanguage = CS.find(aUILanguages, {code: sUILanguageId});
    return oDateFormatDictionary[oSelectedUILanguage.dateFormat];
  };

  let _getDownloadTrackerColumnKey = function (sColumnId) {
    if (sColumnId === "assetInstanceClassName") {
      return "assetInstanceClassId";
    }
    if (sColumnId === "renditionInstanceClassName") {
      return "renditionInstanceClassId";
    }
    if (sColumnId === "downloadedBy") {
      return "userId";
    }
  };

  let _getGridViewDashboardTabView = (sContextId, oProcessedGridProperties, oGridData, oColumn) => {
    let aDashboardTabs = [];
    let aReferenceData= [];
    let oConfigDetails ={};
    switch (sContextId) {
      case GridViewContexts.END_POINT:
        aDashboardTabs = SettingUtils.getAppData().getDashboardTabsList();
        aReferenceData = ProfileProps.getReferencedDashboardTabs();
        break;

      case GridViewContexts.KPI:
        let aTabList = KpiProps.getReferencedDashboardTabs();
        CS.forEach(aTabList, function (oKpi) {
          aDashboardTabs.push({
            id: oKpi.id,
            label: oKpi.label
          })
        });
        aReferenceData = aTabList;
        break;
    }
    let oMSSDashboardTabsRequestResponseObj = {
      requestType: "configData",
      entityName: "dashboardTabs",
    };
    oConfigDetails.referenceData = aReferenceData;
    oProcessedGridProperties = _getLazyMSSObject("", false, aDashboardTabs, [oGridData.dashboardTabId], "dashboardTab",
        oMSSDashboardTabsRequestResponseObj, oColumn.id, true, true, true, false, false, oConfigDetails);
    oProcessedGridProperties.value = oProcessedGridProperties.selectedItems;
    return oProcessedGridProperties;
  };

  let _getTypesBasedOnEntityTypes = function (sEntityType) {
    let aMockDataForProcessEntityTypes = MockDataForProcessEntityTypes();
    let oFoundEntityType = CS.find(aMockDataForProcessEntityTypes, {id: sEntityType});
    if (CS.isNotEmpty(oFoundEntityType)) {
    }
    return (CS.isNotEmpty(oFoundEntityType)) ? [oFoundEntityType.type] : [];
  };

  let _getGridNatureTypeView = (sContextId, oGridData, sSplitter, oProcessedGridProperties, oColumn, oConfigDetails) => {
    let aSelectedNatureTypeList = oGridData.klassIds ? oGridData.klassIds : [];
    let aTypes = [];
    let aTypesToExclude = [];
    let bIsDisabled = false;
    let oReqResObj = {
      requestType: "customType",
      requestURL: "config/klasseslistbybasetype",
      responsePath: ["success", "list"],
      entityName: "klasses",
      customRequestModel: {
        isNature: true
      }
    };

    if (sContextId === GridViewContexts.END_POINT) {
      bIsDisabled = oGridData.endpointType !== "offboardingendpoint";
      aTypes = [MockDataForEntityBaseTypesDictionary.articleKlassBaseType, MockDataForEntityBaseTypesDictionary.assetKlassBaseType];
      aTypesToExclude = [NatureTypeDictionary.GTIN, NatureTypeDictionary.EMBEDDED,
                         NatureTypeDictionary.LANGUAGE, NatureTypeDictionary.TECHNICAL_IMAGE];
    }
    else if (sContextId === GridViewContexts.PROCESS) {
      bIsDisabled = oGridData.eventType === ProcessEventTypeDictionary.INTEGRATION ||
          oGridData.workflowType === MockDataForWorkflowTypesDictionary.SCHEDULED_WORKFLOW ||
          oGridData.workflowType === MockDataForWorkflowTypesDictionary.JMS_WORKFLOW;
      let sEntityType = ProcessConfigViewProps.getEntityType();
      aTypes = _getTypesBasedOnEntityTypes(sEntityType);
      aTypesToExclude = [NatureTypeDictionary.SUPPLIER];
    }

    oReqResObj.customRequestModel.types = aTypes;
    oReqResObj.customRequestModel.typesToExclude = aTypesToExclude;
    let sContext = 'process' + sSplitter + "klasses";
    oProcessedGridProperties = _getLazyMSSObject("", bIsDisabled, [], aSelectedNatureTypeList, sContext, oReqResObj,
        oColumn.id, false, false, false, false, true, oConfigDetails);
    oProcessedGridProperties.value = oProcessedGridProperties.selectedItems;
    oProcessedGridProperties.isDisabled = bIsDisabled;
    return oProcessedGridProperties;
  };

  let _getGridStatusView = (oGridData, oColumn, oProcessedGridData, oProcessedGridProperties) => {
    if (oGridData.hasOwnProperty(oColumn.id)) {
      let sId = "";
      let sLabel = "";
      let sBackgroundColor = "";
      switch (oGridData[oColumn.id]) {
        case 1 :
          sId = oGridData[oColumn.id];
          sLabel = getTranslation().IN_PROGRESS;
          sBackgroundColor = "#FFA500";
          break;
        case 2 :
          sId = oGridData[oColumn.id];
          sLabel = getTranslation().COMPLETED;
          sBackgroundColor = "#58AA4A";
          oProcessedGridData.actionItemsToShow = ["download", "delete"];
          break;
        case 3 :
          sId = oGridData[oColumn.id];
          sLabel = getTranslation().FAILED;
          sBackgroundColor = "#E54545";
          oProcessedGridData.actionItemsToShow = ["delete"];
          break;
      }
      oProcessedGridProperties = {
        id: sId,
        label: sLabel,
        style: {
          backgroundColor: sBackgroundColor,
          color: "#ffffff",
        },
        rendererType: oGridViewPropertyTypes.CHIPS,
        hideIcon: true
      };
      oProcessedGridProperties.isDisabled = true;
      oProcessedGridProperties.isHideDisabledMask = true;
    }
    return oProcessedGridProperties;
  };

  let _setFixedColumns = (sContextId, oGridSkeleton, oGridData, oProcessedGridData) => {
    CS.forEach(oGridSkeleton.fixedColumns, function (oColumn) {
      let oProcessedGridProperties = {};
      switch (oColumn.id) {
        case GridViewColumnIdConstants.ICON:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = {
              value: oGridData.iconKey ? oGridData.iconKey : oGridData.icon,
              isDisabled: false,
              bIsMultiLine: false,
              iconKey: oGridData.iconKey,
              icon: oGridData.icon
            };
            sContextId === GridViewContexts.USER && (oProcessedGridProperties.limitImageSize = false);
          }
          break;

        case GridViewColumnIdConstants.LABEL:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = _getLabelGridPropertyData(oGridData, oColumn);
          }
          break;

        case GridViewColumnIdConstants.CODE:
          if (sContextId === GridViewContexts.TRANSLATIONS) {
            oProcessedGridProperties = _getTranslationGridCodePropertyView(oGridData, oProcessedGridData, oProcessedGridProperties, oColumn);
          }
          else if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = _getCodeGridPropertyData(oGridData, oColumn);
          }
          break;

        case GridViewColumnIdConstants.USER_NAME:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = {
              value: oGridData[oColumn.id],
              bIsMultiLine: false,
              showTooltip: true,
              isDisabled: true,
            }
          }
          break;

        case GridViewColumnIdConstants.SEQUENCE:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = {
              value: oGridData[oColumn.id],
              isDisabled: true
            };
          }
          break;

        default:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = {
              value: oGridData[oColumn.id]
            };
          }
          break;
      }
      oProcessedGridData.properties[oColumn.id] = oProcessedGridProperties;
    });
  };

  /**This function is currently used for tag values**/
  let _shouldPrepareColumnData = (sContextId, sColumnId, sTagPathToRoot) => {
    if (CS.isNotEmpty(sTagPathToRoot) && sContextId === GridViewContexts.TAG) {
      let aColumnIdsToPrepareData = [
        GridViewColumnIdConstants.LABEL,
        GridViewColumnIdConstants.KLASS,
        GridViewColumnIdConstants.LINKED_MASTER_TAG_ID,
        GridViewColumnIdConstants.ICON,
        GridViewColumnIdConstants.COLOR,
        GridViewColumnIdConstants.IMAGE_RESOLUTION,
        GridViewColumnIdConstants.IMAGE_EXTENSION
      ];
      return CS.includes(aColumnIdsToPrepareData, sColumnId);
    }
    return true;
  };

  let _setScrollableColumns = (sContextId, oGridSkeleton, oGridData, oProcessedGridData, oConfigDetails, sTagPathToRoot, oExtraData) => {
    let oReferencedAttributes = oConfigDetails.referencedAttributes;
    let oReferencedTags = oConfigDetails.referencedTags;
    let oReferencedContexts = oConfigDetails.referencedContexts;
    let oRuleTypeDictionary = new RuleTypeDictionary();
    let sSplitter = ViewUtils.getSplitter();


    CS.forEach(oGridSkeleton.scrollableColumns, function (oColumn) {
      if(!_shouldPrepareColumnData(sContextId, oColumn.id, oProcessedGridData.pathToAdd)) {
        return;
      }

      let oProcessedGridProperties = {};
      switch (oColumn.id) {
        case GridViewColumnIdConstants.LABEL:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = _getLabelGridPropertyData(oGridData, oColumn);
          }
          break;

        case GridViewColumnIdConstants.CODE:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = _getCodeGridPropertyData(oGridData, oColumn);
          }
          break;

        case GridViewColumnIdConstants.TYPE:
          let oType = _setTypesAccordingToContext(oGridData, sContextId);
          if (oGridData.hasOwnProperty(oColumn.id)) {
            let oContextType = oType.type;
            oProcessedGridProperties = _getMSSObject(oContextType.items, oContextType.selectedItems, oContextType.context,
                oContextType.singleSelect, oContextType.disabled, oContextType.label, oContextType.disableCross);
            oProcessedGridProperties.value = oContextType.selectedItems;
            oProcessedGridProperties.isDisabled = true;
            oProcessedGridProperties.bIsMultiLine = false;
            oProcessedGridProperties.singleSelect = true;
            oProcessedGridProperties.rendererType = oGridViewPropertyTypes.DROP_DOWN;
          }
          break;

        case GridViewColumnIdConstants.CONTEXT_ID:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = {
              value: oReferencedContexts[oGridData[oColumn.id]].label,
              isDisabled: true,
              bIsMultiLine: false,
              showTooltip: true,
            };
          }
          break;

        case GridViewColumnIdConstants.MEASUREMENT_ATTRIBUTE_TYPE:
          if (oGridData.hasOwnProperty("type") && AttributeUtils.isAttributeTypeMeasurement(oGridData.type)) {
            oProcessedGridProperties = _getMeasurementAttributeTypeMSSObject(oGridData);
            oProcessedGridProperties.value = [oGridData.type];
          }
          break;

        case GridViewColumnIdConstants.DEFAULT_UNIT:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            if (AttributeUtils.isMeasurementAttributeTypeCustom(oGridData.type)) {
              oProcessedGridProperties = {
                value: oGridData.defaultUnitAsHTML || "",
                valueAsHtml: oGridData.defaultUnit || "",
                rendererType: oGridViewPropertyTypes.HTML
              };
            } else { //for other measurement attributes -
              oProcessedGridProperties = _getAttributeUnitMSSObject(oGridData);
              oProcessedGridProperties.value = oProcessedGridProperties.selectedItems;
              oProcessedGridProperties.rendererType = oGridViewPropertyTypes.DROP_DOWN;
            }
          }
          break;

        case GridViewColumnIdConstants.ALLOWED_RTE_ICONS:
          if (oGridData.hasOwnProperty("validator")) {
            oProcessedGridProperties = _getAllowedRTEIconsModelForAttributeConfig(oGridData);
            oProcessedGridProperties.value = CS.cloneDeep(oGridData.validator[oColumn.id]);
          }
          break;

        case GridViewColumnIdConstants.DEFAULT_VALUE:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = _getGridDefaultValueData(sContextId, oProcessedGridProperties, oGridData, oColumn);
          }
          break;

        case GridViewColumnIdConstants.ATTRIBUTE_CONCATENATED_LIST:
          if (oGridData.hasOwnProperty("attributeConcatenatedList")) {
            oProcessedGridProperties = {
              value: CS.cloneDeep(oGridData.attributeConcatenatedList),
              referencedData: {
                referencedAttributes: oReferencedAttributes || {},
                referencedTags: oReferencedTags || {}
              }
            }
          }

          if (!oColumn.extraData.referencedAttributes) {
            oColumn.extraData.referencedAttributes = {};
          }
          CS.assign(oColumn.extraData.referencedAttributes, oReferencedAttributes);

          break;

        case  GridViewColumnIdConstants.CALCULATED_ATTRIBUTE_FORMULA:
          if (oGridData.hasOwnProperty("attributeOperatorList") && oGridData.hasOwnProperty("calculatedAttributeType") && oGridData.hasOwnProperty("calculatedAttributeUnit")) {
            let aAttributeTypeList = new MockAttributeTypes();
            let oMeasurementMetricAndImperial = new MockAttributeUnits();
            let aUnitAttributesList = CS.filter(aAttributeTypeList, {"isUnitAttribute": true});
            let oValue = {
              attributeOperatorList: CS.cloneDeep(oGridData.attributeOperatorList),
              calculatedAttributeType: oGridData.calculatedAttributeType,
              calculatedAttributeUnit: oGridData.calculatedAttributeUnit,
              calculatedAttributeUnitAsHTML: oGridData.calculatedAttributeUnitAsHTML,
              calculatedAttributeId: oGridData.id
            };

            oProcessedGridProperties = {
              value: oValue,
              unitAttributesList: aUnitAttributesList,
              measurementUnitsData: oMeasurementMetricAndImperial,
              bIsMultiLine: true
            }
          }
          if (oColumn.extraData && !oColumn.extraData.referencedAttributes) {
            oColumn.extraData.referencedAttributes = {};
          }
          CS.assign(oColumn.extraData.referencedAttributes, oReferencedAttributes);
          break;

        case GridViewColumnIdConstants.IS_SEARCHABLE:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = {
              value: oGridData[oColumn.id],
              bIsMultiLine: true,
            };
            if (oGridData.id == 'createdonattribute' || oGridData.id == 'nameattribute') {
              oProcessedGridProperties.isDisabled = true;
            }
          }
          break;

        case GridViewColumnIdConstants.IS_ORDER:
        case GridViewColumnIdConstants.IS_SALES:
        case GridViewColumnIdConstants.IS_PROMOTIONAL:
        case GridViewColumnIdConstants.IS_FILTERABLE:
        case GridViewColumnIdConstants.IS_SORTABLE:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            if ((oGridData.hasOwnProperty("calculatedAttributeType") && oGridData.hasOwnProperty("calculatedAttributeUnit")) || oGridData.hasOwnProperty("attributeConcatenatedList")) {
              oProcessedGridProperties = {}
            } else {
              oProcessedGridProperties = {
                value: oGridData[oColumn.id],
              };
            }
          }
          if (oColumn.id === GridViewColumnIdConstants.IS_SORTABLE) {
            if (oGridData.id === 'createdonattribute' || oGridData.id === "lastmodifiedattribute") {
              oProcessedGridProperties.isDisabled = true;
            }
          }
          break;

        case GridViewColumnIdConstants.IS_GRID_EDITABLE:
          let aPropertiesToBeDisabledForGridEditable = ['assetdownloadcountattribute', 'scheduleendattribute', 'schedulestartattribute'];
          if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = {
              value: oGridData[oColumn.id],
            };
            if (CS.includes(aPropertiesToBeDisabledForGridEditable, oGridData.id)) {
              oProcessedGridProperties.isDisabled = true;
            }
          }
          break;

        case GridViewColumnIdConstants.AVAILABILITY:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = _getAvailableTypeMSSObject(oGridData);
            oProcessedGridProperties.value = oProcessedGridProperties.selectedItems;
          }
          break;

        case GridViewColumnIdConstants.IS_DISABLED:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            if (oGridData.isStandard == true) {
              oProcessedGridProperties = {
                value: oGridData[oColumn.id],
                isDisabled: true
              };
            } else {
              oProcessedGridProperties = {
                value: oGridData[oColumn.id],
              };
            }
          }
          break;

        case GridViewColumnIdConstants.IS_VERSIONABLE:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = {
              value: oGridData[oColumn.id],
            };
            if ((oGridData.isStandard && CS.includes(VersionableStandardAttributes, oGridData.type) || oGridData.id === "assetdownloadcountattribute")) {
              oProcessedGridProperties.isDisabled = true;
            }
          }
          break;

        case GridViewColumnIdConstants.TAG_TYPE:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            // if column is 'type' then prepare mss object
            let aTagTypeList = oExtraData.tagTypeList;
            let sType = oGridData[oColumn.id];
            let aSelectedList = sType ? [sType] : [];
            let bIsDisabled = (sType === TagTypeConstants.TAG_TYPE_MASTER || sType === TagTypeConstants.TAG_TYPE_BOOLEAN);
            let aFilteredTypesList = _getFilterTagTypeListAccordingToGroup(aTagTypeList);
            oProcessedGridProperties = _getMSSObject(aFilteredTypesList, aSelectedList, "gridTagType", true, bIsDisabled, "", true);
            oProcessedGridProperties.value = [sType];
          }
          break;

        case GridViewColumnIdConstants.COLOR:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            let oMockColors = new MockColors();
            let aColorItems = CS.map(oMockColors, function (sColor, sKey) {
              return {
                id: sKey,
                label: sColor,
                color: sColor
              }
            });
            let aSelectedItems = oGridData.color || oMockColors[oGridData.color] ? [oGridData.color] : [];
            let sContextForColor = "gridTagColor";
            if (sContextId === GridViewContexts.TASK) {
              sContextForColor = "tasksConfigColor";
            }
            oProcessedGridProperties = _getMSSObject(aColorItems, aSelectedItems, sContextForColor, true, false, "", false);
            oProcessedGridProperties.value = aSelectedItems;
            oProcessedGridProperties.showColor = true;
          }
          break;

        case GridViewColumnIdConstants.LINKED_MASTER_TAG_ID:
          /**TODO: Need to bind this function from TAGSTORE**/
          if (oGridData.hasOwnProperty(oColumn.id)) {
            let oCallback = oExtraData.linkedMasterTagCallback;
            let sPath = sTagPathToRoot || (oGridData.parent && oGridData.parent.id);
            CS.isFunction(oCallback) && oCallback(sPath, oProcessedGridData, oGridData);
            if(CS.isNotEmpty(oProcessedGridData.properties[oColumn.id])) {
              oProcessedGridProperties = oProcessedGridData.properties[oColumn.id];
            } else {
              return;
            }
          }
          break;

        case GridViewColumnIdConstants.IS_MULTISELECT:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            if (oGridData.tagType === TagTypeConstants.YES_NEUTRAL_TAG_TYPE) {
              oProcessedGridProperties = {
                value: oGridData[oColumn.id]
              }
            } else {
              oProcessedGridProperties = {}
            }
            if (oGridData.id === "resolutiontag" || oGridData.id === "imageextensiontag") {
              oProcessedGridProperties.isDisabled = true;
            }
          }
          break;

        case GridViewColumnIdConstants.TAB_ID:
          let oRelationshipType = _setRelationshipTypes(oGridData);
          if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = oRelationshipType;
            oProcessedGridProperties.value = oRelationshipType.selectedItems;
            oProcessedGridProperties.isDisabled = false;
            oProcessedGridProperties.bIsMultiLine = false;
            oProcessedGridProperties.singleSelect = true;
          }
          break;

        case GridViewColumnIdConstants.PROPERTY_COLLECTION_IDS:
        case GridViewColumnIdConstants.RELATIONSHIP_IDS:
        case GridViewColumnIdConstants.CONTEXT_IDS:
        case GridViewColumnIdConstants.NATURE_RELATIONSHIP_IDS:
          let oTemplateIds = {
            propertyCollectionIds: "propertyCollections",
            relationshipIds: "relationships",
            contextIds: "contexts",
            natureRelationshipIds: "natureRelationships"
          };

          if (oGridData.hasOwnProperty(oColumn.id)) {
            let oMSSRequestResponseObj = {
              requestType: "configData",
              entityName: oTemplateIds[oColumn.id],
            };
            oProcessedGridProperties = _getTemplateMSSObject(oGridData, oMSSRequestResponseObj, oTemplateIds[oColumn.id]);
            oProcessedGridProperties.value = oProcessedGridProperties.selectedItems;
          }
          break;

        case GridViewColumnIdConstants.IS_TRANSLATABLE:
        case GridViewColumnIdConstants.KLASS:
        case GridViewColumnIdConstants.IMAGE_RESOLUTION:
        case GridViewColumnIdConstants.IMAGE_EXTENSION:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = {
              value: oGridData[oColumn.id],
              isDisabled: true
            };
          }
          break;

        case GridViewColumnIdConstants.FIRST_NAME:
        case GridViewColumnIdConstants.LAST_NAME:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = {
              value: oGridData[oColumn.id]
            };
            if (oGridData.isBackgroundUser) {
              oProcessedGridProperties.isDisabled = true;
            }
          }
          break;

        case GridViewColumnIdConstants.EMAIL:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = {
              value: oGridData[oColumn.id],
            };
          }
          break;

        case GridViewColumnIdConstants.CONTACT:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = {
              value: oGridData[oColumn.id]
            };
            oProcessedGridProperties.rendererType = oGridViewPropertyTypes.NUMBER;
          }
          break;

        case GridViewColumnIdConstants.GENDER:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            let aSelectedUserGender = oGridData.gender && [oGridData.gender];
            oProcessedGridProperties = _getUserGenderMSSModel(aSelectedUserGender);
            oProcessedGridProperties.rendererType = oGridViewPropertyTypes.DROP_DOWN;
            oProcessedGridProperties.value = oProcessedGridProperties.selectedItems;
            if (oGridData.isBackgroundUser) {
              oProcessedGridProperties.isDisabled = true;
            }
          }
          break;

        case GridViewColumnIdConstants.ROLE_ID:
          let oReferencedRoles = UserProps.getReferencedRoles();
          let sRoleId = oGridData[oColumn.id];
          let oReferencedRole = oReferencedRoles && oReferencedRoles[sRoleId] || null;
          if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = {
              value: CS.isNotEmpty(oReferencedRole) && CS.getLabelOrCode(oReferencedRole) || "",
              isDisabled: true
            };
          }
          break;

        case GridViewColumnIdConstants.ORGANIZATION_NAME:
        case GridViewColumnIdConstants.ORGANIZATION_TYPE:
          oProcessedGridProperties = {
            value: oGridData[oColumn.id],
            isDisabled: true
          };
          break;

        case GridViewColumnIdConstants.IS_EMAIL_LOG:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = {
              value: oGridData.isEmailLog || false,
            };
          }
          break;

        case GridViewColumnIdConstants.MAPPING_TYPE:
          let aMockDataForMappingTypes = new MockDataForMappingTypes();
          let oMappingTypeMSSObject = _getLazyMSSObject("", true, aMockDataForMappingTypes, [oGridData.mappingType], "mappings", {}, "mappingType");
          if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = oMappingTypeMSSObject;
            oProcessedGridProperties.value = oMappingTypeMSSObject.selectedItems;
            oProcessedGridProperties.isDisabled = true
          }
          break;

        case GridViewColumnIdConstants.IS_RUNTIME_MAPPING_ENABLED:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            if (oGridData.endpointType === EndpointDictionary.OUTBOUND_ENDPOINT) {
              oProcessedGridProperties = {};
            } else {
              oProcessedGridProperties = {}
              //value: oGridData[oColumn.id],

            }
          }
          break;

        case GridViewColumnIdConstants.DATA_RULES:
          let oRuleMap = SettingUtils.getAppData().getRuleList();
          let aRuleList = CS.flatMap(oRuleMap);
          let oMSSDataRuleRequestResponseObj = {
            requestType: "configData",
            entityName: "dataRules",
          };
          let aSelectedRuleList = oGridData[oColumn.id] ? oGridData[oColumn.id] : [];
          oProcessedGridProperties = _getLazyMSSObject("", false, aRuleList, aSelectedRuleList, "endpoints", oMSSDataRuleRequestResponseObj, oColumn.id, false);
          oProcessedGridProperties.value = oProcessedGridProperties.selectedItems;
          break;

        case GridViewColumnIdConstants.SYSTEM_ID:
          let aItems = ProfileProps.getSystemsList();
          let sSystemId = oGridData.systemId || "";
          let aSelectedItems = sSystemId ? [sSystemId] : [];
          ProfileProps.setSelectedSystem(oGridData.id, aSelectedItems);
          let sContext = `systems${sSplitter}${oGridData.id}`;
          let sSearchText = ProfileProps.getSystemsListSearchText();
          oProcessedGridProperties = _getMSSObject(aItems, aSelectedItems, sContext, true,
              false, "", true, true, sSearchText);
          oProcessedGridProperties.value = oProcessedGridProperties.selectedItems;
          oProcessedGridProperties.rendererType = oGridViewPropertyTypes.DROP_DOWN;
          break;

        case GridViewColumnIdConstants.PROCESSES:
          let aProcessList = SettingUtils.getAppData().getProcessList();
          aProcessList = CS.flatMap(aProcessList);
          let aTriggeringType = [];
          if (oGridData.endpointType === "offboardingendpoint") {
            aTriggeringType = ["export"];
          } else {
            aTriggeringType = ["import"];
          }
          let oMSSProcessRequestResponseObj = {
            requestType: "configData",
            entityName: "processes",
            types: [oProcessTypeDictionary.ONBOARDING_PROCESS, oProcessTypeDictionary.OFFBOARDING_PROCESS],
            typesToExclude: [],
            customRequestModel: {
              eventType: [ProcessEventTypeDictionary.INTEGRATION],
              triggeringType: aTriggeringType
            }
          };
          let aSelectedProcessList = oGridData[oColumn.id] ? oGridData[oColumn.id] : [];
          oProcessedGridProperties = _getLazyMSSObject("", false, aProcessList, aSelectedProcessList, "endpoints", oMSSProcessRequestResponseObj, "processes", true);
          oProcessedGridProperties.value = oProcessedGridProperties.selectedItems;
          if (CS.isNotEmpty(aSelectedProcessList) && CS.isEmpty(oProcessedGridProperties.referencedData[aSelectedProcessList[0]]) && CS.isNotEmpty(oConfigDetails.referencedProcesses[aSelectedProcessList[0]])) {
            oProcessedGridProperties.referencedData[aSelectedProcessList[0]] = oConfigDetails.referencedProcesses[aSelectedProcessList[0]];
          }
          break;

        case GridViewColumnIdConstants.JMS_PROCESSES:
          let aJmsProcessList = SettingUtils.getAppData().getProcessList();
          aJmsProcessList = CS.flatMap(aJmsProcessList);
          let oMSSJmsProcessRequestResponseObj = {
            requestType: "configData",
            entityName: "processes",
            types: [oProcessTypeDictionary.ONBOARDING_PROCESS, oProcessTypeDictionary.OFFBOARDING_PROCESS],
            typesToExclude: [],
            customRequestModel: {
              workflowTypes: [MockDataForWorkflowTypesDictionary.JMS_WORKFLOW]
            }
          };
          let aSelectedJmsProcessList = oGridData[oColumn.id] ? oGridData[oColumn.id] : [];
          oProcessedGridProperties = _getLazyMSSObject("", false, aJmsProcessList, aSelectedJmsProcessList, "endpoints", oMSSJmsProcessRequestResponseObj, "jmsProcesses", false);
          oProcessedGridProperties.value = oProcessedGridProperties.selectedItems;
          if (CS.isNotEmpty(aSelectedJmsProcessList)) {
            CS.forEach(aSelectedJmsProcessList, function (sId) {
              if (CS.isEmpty(oProcessedGridProperties.referencedData[sId]) && CS.isNotEmpty(oConfigDetails.referencedJmsProcesses[sId])) {
                oProcessedGridProperties.referencedData[sId] = oConfigDetails.referencedJmsProcesses[sId];
              }
            });
          }
          break;

        case GridViewColumnIdConstants.MAPPINGS:
          let aMappingList = SettingUtils.getAppData().getMappingList();
          aMappingList = CS.flatMap(aMappingList);
          let aTypes = [];
          if (oGridData.endpointType === "offboardingendpoint") {
            aTypes = [MappingTypeDictionary.OUTBOUND_MAPPING];
          } else {
            aTypes = [MappingTypeDictionary.INBOUND_MAPPING];
          }
          let oMSSMappingsRequestResponseObj = {
            requestType: "configData",
            entityName: "mappings",
            types: aTypes
          };
          let aSelectedMappingList = oGridData[oColumn.id] ? oGridData[oColumn.id] : [];
          oProcessedGridProperties = _getLazyMSSObject("", false, aMappingList, aSelectedMappingList, "endpoints", oMSSMappingsRequestResponseObj, "mappings", true);
          oProcessedGridProperties.value = oProcessedGridProperties.selectedItems;
          if (CS.isNotEmpty(aSelectedMappingList) && CS.isEmpty(oProcessedGridProperties.referencedData[aSelectedMappingList[0]]) && CS.isNotEmpty(oConfigDetails.referencedMappings[aSelectedMappingList[0]])) {
            oProcessedGridProperties.referencedData[aSelectedMappingList[0]] = oConfigDetails.referencedMappings[aSelectedMappingList[0]];
          }
          break;

        case GridViewColumnIdConstants.AUTHORIZATION_MAPPING:
          let aAuthorizationMappingList = SettingUtils.getAppData().getAuthorizationMappingList();
          aAuthorizationMappingList = CS.flatMap(aAuthorizationMappingList);
          let oMSSAuthorizationMappingsRequestResponseObj = {
            requestType: "configData",
            entityName: "authorizationMappings",
          }
          let aSelectedAuthorizationMappingList = oGridData[oColumn.id] ? oGridData[oColumn.id] : [];
          oProcessedGridProperties = _getLazyMSSObject("", false, aAuthorizationMappingList, aSelectedAuthorizationMappingList, sContextId, oMSSAuthorizationMappingsRequestResponseObj, "authorizationMapping", true);
          oProcessedGridProperties.value = oProcessedGridProperties.selectedItems;
          if (CS.isNotEmpty(aSelectedAuthorizationMappingList) && CS.isEmpty(oProcessedGridProperties.referencedData[aSelectedAuthorizationMappingList[0]]) && CS.isNotEmpty(oConfigDetails.referencedAuthorizationMappings[aSelectedAuthorizationMappingList[0]])) {
            oProcessedGridProperties.referencedData[aSelectedAuthorizationMappingList[0]] = oConfigDetails.referencedAuthorizationMappings[aSelectedAuthorizationMappingList[0]];
          }
          break;

        case GridViewColumnIdConstants.ENDPOINT_TYPE:
          let aMockDataForEndPointTypes = new MockDataForEndPointTypes();
          let oEndpointTypeMSSObject = _getLazyMSSObject("", true, aMockDataForEndPointTypes, [oGridData.endpointType], "endpoints", {}, "endpointType");
          if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = oEndpointTypeMSSObject;
            oProcessedGridProperties.value = oEndpointTypeMSSObject.selectedItems;
            oProcessedGridProperties.isDisabled = true
          }
          break;

        case GridViewColumnIdConstants.DASHBOARD_TAB:
          oProcessedGridProperties = _getGridViewDashboardTabView(sContextId, oProcessedGridProperties, oGridData, oColumn);
          break;

        case GridViewColumnIdConstants.PHYSICAL_CATALOGS:
          let aMockDataForPhysicalCatalogTypes = MockDataForPhysicalCatalog();
          let aSelectedPhysicalCatalog = oGridData[oColumn.id] || [oPhysicalCatalogDictionary.DATAINTEGRATION];
          let aPhysicalCatalogTypes = [];
          if (oGridData.endpointType === "offboardingendpoint") {
            aPhysicalCatalogTypes = CS.filter(aMockDataForPhysicalCatalogTypes, function (oPhysicalCatalogType) {
              return oPhysicalCatalogType.id !== oPhysicalCatalogDictionary.ONBOARDING
            });
          } else {
            aPhysicalCatalogTypes = CS.filter(aMockDataForPhysicalCatalogTypes, function (oPhysicalCatalogType) {
              return oPhysicalCatalogType.id !== oPhysicalCatalogDictionary.OFFBOARDING
            });
          }
          oProcessedGridProperties = _getMSSObject(aPhysicalCatalogTypes, aSelectedPhysicalCatalog, 'physicalCatalogs', true, false, "", true);
          oProcessedGridProperties.value = oProcessedGridProperties.selectedItems;
          break;

        case GridViewColumnIdConstants.INBOUND_DATA_CATALOG_IDS:
          let aMockDataForInboundDataCatalogTypes = MockDataForPhysicalCatalog();
          let aSelectedInboundDataCatalogs = oGridData["dataCatalogIds"] || [oPhysicalCatalogDictionary.DATAINTEGRATION];
          if (oGridData.endpointType === "onboardingendpoint") {
            let aDataCatalogTypesForOnboarding = CS.filter(aMockDataForInboundDataCatalogTypes, function (oDataCatalogType) {
              return oDataCatalogType.id !== oPhysicalCatalogDictionary.OFFBOARDING;
            });
            oProcessedGridProperties = _getMSSObject(aDataCatalogTypesForOnboarding, aSelectedInboundDataCatalogs, 'dataCatalogs', true, false, "", true);
            oProcessedGridProperties.value = oProcessedGridProperties.selectedItems;
          }
          break;

        case GridViewColumnIdConstants.OUTBOUND_DATA_CATALOG_IDS:
          let aMockDataForOutboundDataCatalogTypes = MockDataForPhysicalCatalog();
          let aSelectedOutboundDataCatalogs = oGridData["dataCatalogIds"] || [oPhysicalCatalogDictionary.DATAINTEGRATION];
          if (oGridData.endpointType === "offboardingendpoint") {
            let aDataCatalogTypesForOffboarding = CS.filter(aMockDataForOutboundDataCatalogTypes, function (oDataCatalogType) {
              return oDataCatalogType.id !== oPhysicalCatalogDictionary.ONBOARDING;
            });
            oProcessedGridProperties = _getMSSObject(aDataCatalogTypesForOffboarding, aSelectedOutboundDataCatalogs, 'dataCatalogs', true, false, "", true);
            oProcessedGridProperties.value = oProcessedGridProperties.selectedItems;
          }
          break;

        case GridViewColumnIdConstants.NATURE_TYPE:
          oProcessedGridProperties = _getGridNatureTypeView(sContextId, oGridData, sSplitter, oProcessedGridProperties, oColumn, oConfigDetails);
          break;

        case GridViewColumnIdConstants.TIMER_START_EXPRESSION:
          oProcessedGridProperties = {
            value: oGridData[oColumn.id],
            placeholder: oGridData.code,
            isDisabled: false,
            bIsMultiLine: false
          };
          if (oGridData.workflowType !== MockDataForWorkflowTypesDictionary.SCHEDULED_WORKFLOW) {
            oProcessedGridProperties.isDisabled = true;
          }
          break;

        case GridViewColumnIdConstants.IP:
          oProcessedGridProperties = {
            value: oGridData[oColumn.id] || "",
            isDisabled: false,
            bIsMultiLine: false
          };
          if (oGridData.workflowType !== MockDataForWorkflowTypesDictionary.JMS_WORKFLOW) {
            oProcessedGridProperties.isDisabled = true;
          }
          break;

        case GridViewColumnIdConstants.PORT:
          oProcessedGridProperties = {
            value: oGridData[oColumn.id] || "",
            isDisabled: false,
            bIsMultiLine: false
          };
          if (oGridData.workflowType !== MockDataForWorkflowTypesDictionary.JMS_WORKFLOW) {
            oProcessedGridProperties.isDisabled = true;
          }
          break;

        case GridViewColumnIdConstants.QUEUE:
          oProcessedGridProperties = {
            value: oGridData[oColumn.id] || "",
            isDisabled: false,
            bIsMultiLine: false
          };
          if (oGridData.workflowType !== MockDataForWorkflowTypesDictionary.JMS_WORKFLOW) {
            oProcessedGridProperties.isDisabled = true;
          }
          break;

        case GridViewColumnIdConstants.TIMER_DEFINITION_TYPE:
        case GridViewColumnIdConstants.PHYSICAL_CATALOG_IDS:
        case GridViewColumnIdConstants.TRIGGERING_TYPE:
        case GridViewColumnIdConstants.EVENT_TYPE:
        case GridViewColumnIdConstants.WORKFLOW_TYPE:
          if (sContextId === GridViewContexts.PROCESS) {
            let oProcessType = _setProcessTypes(oGridData, oColumn.id)[oColumn.id];
            oProcessedGridProperties = _getMSSObject(oProcessType.items, oProcessType.selectedItems, oProcessType.context,
                oProcessType.singleSelect, oProcessType.disabled, oProcessType.label, oProcessType.cannotRemove);
            if (oColumn.id !== GridViewColumnIdConstants.PHYSICAL_CATALOG_IDS) {
              oProcessedGridProperties.value = CS.isEmpty(oProcessType.selectedItems) ? [] : [oProcessType.selectedItems];
            }
            if (oColumn.id === GridViewColumnIdConstants.PHYSICAL_CATALOG_IDS) {
              oProcessedGridProperties.value = CS.isEmpty(oProcessType.selectedItems) ? [] : oProcessType.selectedItems;
              oProcessedGridProperties.isDisabled = oGridData.workflowType === MockDataForWorkflowTypesDictionary.JMS_WORKFLOW
                  || oGridData.eventType === ProcessEventTypeDictionary.INTEGRATION;
            } else if (oColumn.id === GridViewColumnIdConstants.TRIGGERING_TYPE) {
              // if ((oGridData.eventType === ProcessEventTypeDictionary.BUSINESS_PROCESS_EVENT || oGridData.eventType === ProcessEventTypeDictionary.INTEGRATION)
              //     && oGridData.workflowType === MockDataForWorkflowTypesDictionary.SCHEDULED_WORKFLOW ) {
              //   oProcessedGridProperties.isDisabled = true;
              // } else if(oGridData.eventType === ProcessEventTypeDictionary.INTEGRATION || oGridData.eventType === ProcessEventTypeDictionary.BUSINESS_PROCESS_EVENT){
              //   oProcessedGridProperties.isDisabled = true;
              // }
              oProcessedGridProperties.isDisabled = true;
            } else if (oColumn.id === GridViewColumnIdConstants.EVENT_TYPE) {
              oProcessedGridProperties.isDisabled = true;
            } else if (oColumn.id === GridViewColumnIdConstants.WORKFLOW_TYPE) {
              oProcessedGridProperties.isDisabled = true;
            } else if (oColumn.id === GridViewColumnIdConstants.TIMER_DEFINITION_TYPE && oGridData.workflowType !== MockDataForWorkflowTypesDictionary.SCHEDULED_WORKFLOW) {
              oProcessedGridProperties.isDisabled = true;
            }
          }
          else if (sContextId === GridViewContexts.RULE &&
              oColumn.id === GridViewColumnIdConstants.PHYSICAL_CATALOG_IDS) {
            let aCatalogsIds = oGridData[oColumn.id];
            let aCatalogsNames = aCatalogsIds.map(cId => getTranslation()[cId.toUpperCase()]);
            let aPrepareCatalogNames = aCatalogsNames.join(",");
            oProcessedGridProperties = {
              value: aPrepareCatalogNames,
              isDisabled: true,
              bIsMultiLine: false
            };
          }
          break;

        case GridViewColumnIdConstants.ATTRIBUTES:
          let aSelectedNatureAttributeList = oGridData.attributeIds ? oGridData.attributeIds : [];
          let oRefAttributes = oConfigDetails.referencedAttributes ? oConfigDetails.referencedAttributes : {};
          oProcessedGridProperties = _getLazyMSSModelForAttributesAndTags(aSelectedNatureAttributeList, oRefAttributes, "attributes");
          oProcessedGridProperties.isDisabled = !(oGridData.eventType === ProcessEventTypeDictionary.BUSINESS_PROCESS_EVENT && oGridData.triggeringType === "afterSave");
          oProcessedGridProperties.value = oProcessedGridProperties.selectedItems;
          break;

        case GridViewColumnIdConstants.TAGS:
          let aSelectedTagList = oGridData.tagIds ? oGridData.tagIds : [];
          let oRefTags = oConfigDetails.referencedTags ? oConfigDetails.referencedTags : {};
          oProcessedGridProperties = _getLazyMSSModelForAttributesAndTags(aSelectedTagList, oRefTags, "tags");
          oProcessedGridProperties.value = oProcessedGridProperties.selectedItems;
          oProcessedGridProperties.isDisabled = !(oGridData.eventType === ProcessEventTypeDictionary.BUSINESS_PROCESS_EVENT && oGridData.triggeringType === "afterSave");
          break;

        case GridViewColumnIdConstants.ORGANIZATIONS:
          let aSelectedOrganizationList = oGridData.organizationsIds ? oGridData.organizationsIds : [];
          let oReforganizations = oConfigDetails.referencedOrganizations ? oConfigDetails.referencedOrganizations : {};

          let bIsSingleSelect = false;
          if(oGridData.workflowType === MockDataForWorkflowTypesDictionary.SCHEDULED_WORKFLOW || oGridData.workflowType === MockDataForWorkflowTypesDictionary.JMS_WORKFLOW){
            bIsSingleSelect = true;
          }
          oProcessedGridProperties = _getLazyMSSModelForOrganization(aSelectedOrganizationList, oReforganizations, bIsSingleSelect);
          oProcessedGridProperties.value = oProcessedGridProperties.selectedItems;
          break;

        case GridViewColumnIdConstants.TAXONOMY:
          let aSelectedTaxonomyList = oGridData.taxonomyIds ? oGridData.taxonomyIds : [];
          let oAllowedTaxonomiesById = {};
          if (sContextId == GridViewContexts.END_POINT) {
            oAllowedTaxonomiesById = ProfileProps.getAllowedTaxonomies();
          }
          let oReferencedTaxonomies = oConfigDetails.referencedTaxonomies ? oConfigDetails.referencedTaxonomies : {};
          let oMultiTaxonomyData = SettingUtils.getMultiTaxonomyData(aSelectedTaxonomyList, oReferencedTaxonomies, oAllowedTaxonomiesById);
          oProcessedGridProperties = {};
          oProcessedGridProperties.model = _getSmallTaxonomyViewModel(oMultiTaxonomyData);
          oProcessedGridProperties.isTaxonomyDisabled = true;
          oProcessedGridProperties.showAllComponents = false;
          oProcessedGridProperties.isDisabled = true;
          break;

        case GridViewColumnIdConstants.ACTIVATION:
          oProcessedGridProperties = {
            value: oGridData.isExecutable,
            isDisabled: false,
          };
          break;

        case GridViewColumnIdConstants.IS_LANGUAGE_DEPENDENT:
          if ((oGridData.type !== oRuleTypeDictionary.CLASSIFICATION_RULE) && oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = {
              value: oGridData[oColumn.id],
              isDisabled: true
            };
          }
          break;

        case GridViewColumnIdConstants.LANGUAGES:
          let oLanguagesInfo = RuleProps.getDataLanguages();
          if ((oGridData.type !== oRuleTypeDictionary.CLASSIFICATION_RULE) && oGridData.hasOwnProperty(oColumn.id)) {
            let aSelectedLanguagesCode = oGridData.languages;
            let aSelectedLanguagesId = CommonUtils.getIdsByCode(oLanguagesInfo, aSelectedLanguagesCode);
            oProcessedGridProperties = _getMSSObject(oLanguagesInfo, aSelectedLanguagesId, "dataLanguages", true, true, "");
            oProcessedGridProperties.value = oProcessedGridProperties.selectedItems;
            oProcessedGridProperties.rendererType = oGridViewPropertyTypes.DROP_DOWN;
            oProcessedGridProperties.isDisabled = true;
          }
          break;

        case GridViewColumnIdConstants.STATUS_TAG:
        case GridViewColumnIdConstants.PRIORITY_TAG:

          let oMSSRequestResponseObj = {
            requestType: "configData",
            entityName: "tags",
          };
          let aTagList = SettingUtils.getAppData().getTagMap();
          let aSelectedList = [];
          let aStatusTagMSSList = [];

          CS.forEach(aTagList, function (oTag) {
            if (oGridData[oColumn.id] === oTag.id) {
              aSelectedList.push(oGridData[oColumn.id]);
            }
            aStatusTagMSSList.push({
              id: oTag.id,
              label: oTag.label
            })
          });

          let sMSSContext = oColumn.id === GridViewColumnIdConstants.STATUS_TAG ? "taskStatusTag" : "taskPriorityTag";
          let bIsDisable = false;
          if (oColumn.id === GridViewColumnIdConstants.STATUS_TAG) {
            bIsDisable = oGridData.type === TaskTypeDictionary().SHARED;
          }
          oProcessedGridProperties = _getLazyMSSObject("", bIsDisable, aStatusTagMSSList, aSelectedList, sMSSContext,
              oMSSRequestResponseObj, oColumn.id, true);
          oProcessedGridProperties.isDisabled = bIsDisable;
          oProcessedGridProperties.value = oProcessedGridProperties.selectedItems;
          break;

        case GridViewColumnIdConstants.FILE_NAME:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = {
              value: oGridData[oColumn.id],
              showToolTip: true,
              bIsMultiLine: false,
              placeholder: oGridData["fileName"],
              rendererType: oGridViewPropertyTypes.TEXT
            };
            oProcessedGridProperties.isDisabled = true;
            oProcessedGridProperties.isHideDisabledMask = true;
          }
          break;


        case GridViewColumnIdConstants.START_TIME:
        case GridViewColumnIdConstants.END_TIME:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            if (!oGridData[oColumn.id] == 0) {
              oProcessedGridProperties = {
                value: oGridData[oColumn.id],
                rendererType: oGridViewPropertyTypes.DATETIME
              }
            } else {
              oProcessedGridProperties = {
                value: "-",
                rendererType: oGridViewPropertyTypes.TEXT
              }
            }
            oProcessedGridProperties.isDisabled = true;
            oProcessedGridProperties.isHideDisabledMask = true;
          }
          break;

        case GridViewColumnIdConstants.STATUS:
          oProcessedGridProperties = _getGridStatusView(oGridData, oColumn, oProcessedGridData, oProcessedGridProperties);
          break;

        case GridViewColumnIdConstants.TIMESTAMP:
          let oDate = CommonUtils.getDateInSpecifiedDateTimeFormat(oGridData[oColumn.id], _getSelectedUILanguageDateFormat());
          oProcessedGridProperties = {
            value: oDate.date + " " + oDate.time,
            isDisabled: true,
            isExpandable: false,
            isHideDisabledMask: true,
          };
          break;

        case GridViewColumnIdConstants.ASSET_INSTANCE_CLASSNAME:
        case GridViewColumnIdConstants.RENDITION_INSTANCE_CLASSNAME:
        case GridViewColumnIdConstants.DOWNLOADED_BY:
          let sValue = oGridData[oColumn.id];
          if (CS.isEmpty(sValue)) {
            let sKey = _getDownloadTrackerColumnKey(oColumn.id);
            oProcessedGridProperties = {
              value: oGridData[sKey],
              isDisabled: true,
              isExpandable: false,
            };
          } else {
            oProcessedGridProperties = {
              value: oGridData[oColumn.id],
              isDisabled: true,
              isExpandable: false,
              isHideDisabledMask: true,
            };
          }
          break;

        case GridViewColumnIdConstants.DOMAIN:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = {
              value: oGridData[oColumn.id],
              bIsMultiLine: false,
              showTooltip: true,
              placeholder: oGridData["domain"]
            };
          }
          break;

        case GridViewColumnIdConstants.IDP:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            let aSSOConfigurationList = SSOSettingConfigViewProps.getSSOSettingsConfigurationList();
            let sType = oGridData[oColumn.id];
            let aSelectedList = sType ? [sType] : [];
            oProcessedGridProperties = _getMSSObject(aSSOConfigurationList, aSelectedList, "gridSsoSetting", true, false, "", true);
            oProcessedGridProperties.value = [sType];
          }
          break;

        case GridViewColumnIdConstants.USER_NAME:
        case GridViewColumnIdConstants.IP_ADDRESS:
        case GridViewColumnIdConstants.ACTIVITY_ID:
        case GridViewColumnIdConstants.ELEMENT_NAME:
        case GridViewColumnIdConstants.ELEMENT_CODE:
        case GridViewColumnIdConstants.DATE:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = {
              isDisabled: true,
              isHideDisabledMask: true,
              value: oGridData[oColumn.id],
            }
          }
          break;

        case GridViewColumnIdConstants.ELEMENT:
        case GridViewColumnIdConstants.ACTIVITY:
        case GridViewColumnIdConstants.ENTITY_TYPE:
        case GridViewColumnIdConstants.ELEMENT_TYPE:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            let sKeyForTranslation = oGridData[oColumn.id] === "UNDEFINED" ? "" : oGridData[oColumn.id];
            oProcessedGridProperties = {
              isDisabled: true,
              isHideDisabledMask: true,
              value: getTranslation()[sKeyForTranslation],
            }
          }
          break;

        case GridViewColumnIdConstants.HIDE_SEPARATOR:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = {
              value: oGridData[oColumn.id],
              isDisabled: false
            }
          };
          break;

        case GridViewColumnIdConstants.ICON:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = {
              value: oGridData.iconKey ? oGridData.iconKey : oGridData.icon,
              isDisabled: false,
              bIsMultiLine: false,
              iconKey: oGridData.iconKey,
              icon: oGridData.icon
            };
          }
          break;

        default:
          if (oGridData.hasOwnProperty(oColumn.id)) {
            oProcessedGridProperties = {
              value: oGridData[oColumn.id],
              bIsMultiLine: true,
              showTooltip: true
            };
          }
      }

      oProcessedGridData.properties[oColumn.id] = oProcessedGridProperties;
    });
  };

  let _sortTagSequence = (aChildren, aSequence) => {
    let aClonedChildren = CS.clone(aChildren);
    let aSequencedTags = [];
    CS.forEach(aSequence, function (sId) {
      var aTags = CS.remove(aClonedChildren, {
        id: sId
      });
      if (!CS.isEmpty(aTags)) {
        aSequencedTags.push(aTags[0]);
      }
    });

    aSequencedTags.push.apply(aSequencedTags, aClonedChildren);
    return aSequencedTags;
  };

  let _setTranslationsColumns = (oGridData, oProcessGridData) => {
    let sDefaultLanguage = GlobalStore.getLanguageInfo().defaultLanguage;
    CS.forEach(oGridData.translations, function (oTranslations, sLanguage) {
      let oGridProperties = oProcessGridData.properties;
      if (sLanguage == sDefaultLanguage) {
        oGridProperties['defaultLanguage'] = {
          value: oTranslations[sSelectedProperty], // eslint-disable-line
          placeholder: oTranslations['placeholder'] || !oTranslations['label'] ? CS.getLabelOrCode(oProcessGridData) : null,
          bIsMultiLine: false,
          showTooltip: true,
          isDisabled: oProcessGridData.disableRow
        };
        if (sSelectedProperty == "description" || sSelectedProperty == "placeholder" || sSelectedProperty == "tooltip") { // eslint-disable-line
          oGridProperties['label'] = {
            value: oTranslations['label'],
            placeholder: oTranslations['placeholder'] || !oTranslations['label'] ? CS.getLabelOrCode(oProcessGridData) : null,
            bIsMultiLine: true,
            isDisabled: oProcessGridData.disableRow
          };
        }
        oGridProperties[sLanguage] = {
          value: oTranslations[sSelectedProperty], // eslint-disable-line
          bIsMultiLine: true,
          isDisabled: oProcessGridData.disableRow
        };
      } else if (oTranslations.hasOwnProperty(sSelectedProperty)) { // eslint-disable-line
        oGridProperties[sLanguage] = {
          value: oTranslations[sSelectedProperty], // eslint-disable-line
          bIsMultiLine: true,
          isDisabled: oProcessGridData.disableRow
        };
      }
    });
  };

  let _getProcessedGridViewData = (sContextId, aGridList, sTagPathToRoot = "", oConfigDetails = {}, oExtraData = {}) => {
    let oGridViewPropsByContext = _getGridViewPropsByContext(sContextId);
    let oGridSkeleton = oGridViewPropsByContext.getGridViewSkeleton();
    let iGridListCount = aGridList.length;
    let aGridViewData = [];

    CS.forEach(aGridList, function (oGridData, iIndex) {
      let oProcessedGridData = {};
      _fillBasicGridDetails(sContextId, oGridData, iIndex, iGridListCount, oProcessedGridData, sTagPathToRoot);
      _setFixedColumns(sContextId, oGridSkeleton, oGridData, oProcessedGridData);
      _setScrollableColumns(sContextId, oGridSkeleton, oGridData, oProcessedGridData, oConfigDetails, sTagPathToRoot, oExtraData);

      _setTranslationsColumns(oGridData, oProcessedGridData);

      if (CS.isNotEmpty(oGridData.children) && oGridData.tagType !== TagTypeConstants.TAG_TYPE_BOOLEAN) {
        if (sContextId !== GridViewContexts.TRANSLATIONS) {
          oGridData.children = _sortTagSequence(oGridData.children, oGridData.tagValuesSequence);
        }
        oProcessedGridData.children = _getProcessedGridViewData(sContextId, oGridData.children, oProcessedGridData.pathToRoot,
            oConfigDetails, oExtraData);
      }
      aGridViewData.push(oProcessedGridData);
    });
    return aGridViewData;
  };

  let _preProcessDataForGridView = (sContextId, aGridList, iTotalCount, oConfigDetails, sTagPathToRoot, oExtraData) => {
    let oGridViewPropsByContext = _getGridViewPropsByContext(sContextId);
    let aGVData = _getProcessedGridViewData(sContextId, aGridList, sTagPathToRoot, oConfigDetails,
        oExtraData, iTotalCount);
    if (sContextId === GridViewContexts.TAG) {
      let iNestedCount = SettingUtils.calculateNestedContentCount(aGridList);
      oGridViewPropsByContext.setGridViewTotalNestedItems(iNestedCount);
    }
    oGridViewPropsByContext.setGridViewData(aGVData);
    oGridViewPropsByContext.setGridViewTotalItems(iTotalCount);
  };

  /***************************************************Ganesh More ***********************************************/

  let _createGridViewPropsByContext = function (sContext, oData = {}) {
    let GridViewProps = ContextualGridViewProps.createGridViewPropsByContext(sContext).gridViewProps;

    GridViewProps.setGridViewSkeleton(oData.skeleton);
    GridViewProps.setGridViewPaginationData(
        {
          from: oData.from || 0,
          pageSize: oData.pageSize || 20
        }
    );
    GridViewProps.setGridViewSortData(
        {
          sortBy: oData.sortBy || "label",
          sortOrder: oData.sortOrder || "asc"
        }
    );
    let sSearchText = oData.searchText || "";
    let sSearchBy = oData.searchBy || "label";
    GridViewProps.setGridViewSearchText(sSearchText);
    GridViewProps.setGridViewSearchBy(sSearchBy)
  };

  let handleCalculatedAttributeFormulaSave = (oItemToSave, oProperty) => {
    oItemToSave.attributeOperatorList = oProperty.value.attributeOperatorList;
    oItemToSave.calculatedAttributeType = oProperty.value.calculatedAttributeType;
    oItemToSave.calculatedAttributeUnit = oProperty.value.calculatedAttributeUnit;
    if (AttributeUtils.isMeasurementAttributeTypeCustom(oItemToSave.calculatedAttributeType)) {
      oItemToSave.calculatedAttributeUnitAsHTML = oProperty.value.calculatedAttributeUnitAsHTML;
    }
  };

  let handleDefaultUnitSave = (oItemToSave, oProperty, oRow, oMeasurementMetricAndImperial, sPropertyId) => {
    if (AttributeUtils.isMeasurementAttributeTypeCustom(oItemToSave.type)) {
      oItemToSave.defaultUnit = oProperty.valueAsHtml;
      oItemToSave.defaultUnitAsHTML = oProperty.value;
    }
    else {
      let oType = oRow.properties['defaultUnit'];
      if (CS.isEmpty(oType) || CS.isEmpty(oType.value)) {
        oItemToSave.defaultUnit = "";
        oItemToSave.defaultUnitAsHTML = "";
      }
      else {
        let aAttributeUnits = CS.find(oType.items, {id: oType.value[0]});
        if (aAttributeUnits) {
          oItemToSave.defaultUnit = aAttributeUnits.unit;
        }
        let sAttributeType = oType.value[0];
        if (AttributeUtils.isAttributeTypeMeasurement(sAttributeType)) {
          aAttributeUnits = oMeasurementMetricAndImperial[sAttributeType];
          let oUnit = CS.find(aAttributeUnits, {id: oProperty.value[0]});
          oItemToSave[sPropertyId] = oUnit.unit;
        }
      }
    }
  };

  let handleDefaultValueSave = (oItemToSave, oProperty, sPropertyId) => {
    if (AttributeUtils.isAttributeTypeHtml(oItemToSave.type)) {
      oItemToSave.defaultValue = oProperty.valueAsHtml;
      oItemToSave.valueAsHtml = oProperty.value;
    } else if (oItemToSave.type === DictionaryForClassName.ENTITY_TAG && CS.isNotEmpty(oProperty)) {
      let sDefaultChildId = oProperty.value[0] || "";
      oItemToSave[sPropertyId] = CS.find(oItemToSave.children, {id: sDefaultChildId});
    }
    else {
      oItemToSave.defaultValue = oProperty.value;
    }
  };

  let handleDashboardTabSave = (sContextId, oItemToSave, oProperty, oOriginalItem) => {
    switch (sContextId) {
      case GridViewContexts.END_POINT:
        oItemToSave.dashboardTabId = oProperty.value[0];
        break;

      case GridViewContexts.KPI:
        if (oProperty.selectedItems[0] != oOriginalItem["dashboardTabId"]) {
          oItemToSave["addedDashboardTabId"] = oProperty.selectedItems[0];
          oItemToSave["deletedDashboardTabId"] = oOriginalItem["dashboardTabId"];
        }
        break;
    }

  };

  let _handleSaveRuleDependency = (oItemToSave) => {
    let aRuleTypes = CS.values(RuleTypeDictionary());
    if (CS.includes(aRuleTypes, oItemToSave.type)) {
      delete oItemToSave.physicalCatalogIds;
    }
  };

  let _getTypeForSSOSetting = (sId) => {
    let aSSOSettingConfigurationList = SSOSettingConfigViewProps.getSSOSettingsConfigurationList();
    let oSSOSetting = CS.find(aSSOSettingConfigurationList, {id: sId});
    return oSSOSetting.type;
  };

  let _recursiveProcessGridDataToSave = (sContextId, aListToSave, aGridData, aOriginalData, oExtraData, sParentId) => {
    let bSafeToSave = true;
    let oMeasurementMetricAndImperial = new MockAttributeUnits();

    CS.forEach(aGridData, function (oRow) {
      let oOriginalItem = CS.find(aOriginalData, {id: oRow.id});
      let oItemToSave = CS.cloneDeep(oOriginalItem);
      if (oRow.isDirty) {
        CS.forOwn(oRow.properties, function (oProperty, sPropertyId) {

          switch (sPropertyId) {
            case GridViewColumnIdConstants.TAB_ID :
              //Required for references
              oItemToSave[sPropertyId] = oProperty.value[0];
              let oReferencedTabs = oExtraData.referencedTabs;
              oItemToSave.tabId !== oOriginalItem.tabId && (oItemToSave.addedTab = oReferencedTabs[oItemToSave.tabId]);
              break;

            case GridViewColumnIdConstants.TYPE:
            case GridViewColumnIdConstants.SYSTEM_ID:
            case GridViewColumnIdConstants.ENDPOINT_TYPE:
            case GridViewColumnIdConstants.MAPPING_TYPE:
            case GridViewColumnIdConstants.EVENT_TYPE:
            case GridViewColumnIdConstants.WORKFLOW_TYPE:

              //Required for context, Rule .
              if (oItemToSave.type !== "com.cs.core.config.interactor.entity.relationship.Relationship") {
                oItemToSave[sPropertyId] = oProperty.value[0];
              }
              break;

            case GridViewColumnIdConstants.MEASUREMENT_ATTRIBUTE_TYPE:
              if (!CS.isEmpty(oProperty) && !CS.isEmpty(oProperty.value)) {
                oItemToSave["type"] = oProperty.value[0];
              }
              break;

            case GridViewColumnIdConstants.DEFAULT_UNIT:
              handleDefaultUnitSave(oItemToSave, oProperty, oRow, oMeasurementMetricAndImperial, sPropertyId);
              break;

            case GridViewColumnIdConstants.DEFAULT_VALUE:
              handleDefaultValueSave(oItemToSave, oProperty, sPropertyId);
              break;

            case GridViewColumnIdConstants.ALLOWED_RTE_ICONS:
              oItemToSave.validator[sPropertyId] = oProperty.value;
              break;

            case GridViewColumnIdConstants.CALCULATED_ATTRIBUTE_FORMULA:
              handleCalculatedAttributeFormulaSave(oItemToSave, oProperty);
              break;

            case GridViewColumnIdConstants.PRECISION:
              if (oProperty.value === "") {
                oProperty.value = '0';
              }
              oItemToSave[sPropertyId] = CS.toNumber(oProperty.value);
              break;

            case GridViewColumnIdConstants.TAG_TYPE:
            case GridViewColumnIdConstants.COLOR:
            case GridViewColumnIdConstants.LINKED_MASTER_TAG_ID:
            case GridViewColumnIdConstants.PROPERTY_COLLECTIONS:
            case GridViewColumnIdConstants.RELATIONSHIPS:
            case GridViewColumnIdConstants.CONTEXTS:
            case GridViewColumnIdConstants.NATURE_RELATIONSHIPS:
            case GridViewColumnIdConstants.STATUS_TAG:
            case GridViewColumnIdConstants.PRIORITY_TAG:
            case GridViewColumnIdConstants.TRIGGERING_TYPE:
            case GridViewColumnIdConstants.TIMER_DEFINITION_TYPE:
              oItemToSave[sPropertyId] = oProperty.value[0] || "";
              break;

            case GridViewColumnIdConstants.IS_MULTISELECT:
              oItemToSave[sPropertyId] = !!oProperty.value;
              break;

            case GridViewColumnIdConstants.SEQUENCE:
              oItemToSave.modifiedTabSequence = oProperty.value;
              break;

            case GridViewColumnIdConstants.GENDER:
              oItemToSave[sPropertyId] = oProperty.value && oProperty.value[0];
              break;

            case GridViewColumnIdConstants.IDP:
              oItemToSave[sPropertyId] = oProperty.value[0];
              oItemToSave.type = _getTypeForSSOSetting(oProperty.value[0]);
              break;

            case GridViewColumnIdConstants.DASHBOARD_TAB:
              handleDashboardTabSave(sContextId, oItemToSave, oProperty, oOriginalItem);
              break;

            case GridViewColumnIdConstants.NATURE_TYPE:
              oItemToSave.klassIds = oProperty.value;
              break;

            case GridViewColumnIdConstants.ATTRIBUTES:
              oItemToSave.attributeIds = oProperty.value;
              break;

            case GridViewColumnIdConstants.TAGS:
              oItemToSave.tagIds = oProperty.value;
              break;

            case GridViewColumnIdConstants.ORGANIZATIONS:
              oItemToSave.organizationsIds = oProperty.value;
              break;

            case GridViewColumnIdConstants.INBOUND_DATA_CATALOG_IDS:
              oItemToSave.dataCatalogIds = oProperty.value;
              break;
            case GridViewColumnIdConstants.OUTBOUND_DATA_CATALOG_IDS:
              if(oItemToSave.endpointType == EndpointTypeDictionary.OUTBOUND_ENDPOINT){
                oItemToSave.dataCatalogIds = oProperty.value;
              }
              break;

            case GridViewColumnIdConstants.PHYSICAL_CATALOGS:
              let aPhysicalCatalogs = oProperty.value;
              if (CS.isEmpty(aPhysicalCatalogs)) {
                aPhysicalCatalogs.push(oPhysicalCatalogDictionary.DATAINTEGRATION);
              }
              oItemToSave[sPropertyId] = aPhysicalCatalogs;
              break;

            case GridViewColumnIdConstants.ACTIVATION:
              oItemToSave.isExecutable = oProperty.value;
              break;

              //Required to keep value as it is of oItemToSave.
              // Do not remove this case.
            case GridViewContexts.CONTEXT_ID:
              break;

            case GridViewColumnIdConstants.ICON:
              oItemToSave[sPropertyId] = oProperty.icon || "";
             /* oItemToSave.hasOwnProperty('iconKey') && (oItemToSave.iconKey = undefined);*/
                delete oItemToSave.iconKey;
              break;

            default:
              oItemToSave[sPropertyId] = oProperty.value;
              break;
          }
        });

        // Executed when tag's children is updated from Config -> properties.
        if (sParentId) {
          oItemToSave.parent = {
            id: sParentId
          };
        }

        if (oItemToSave.hasOwnProperty("label") && CS.trim(oItemToSave.label) === "") {
          alertify.message(getTranslation().ERROR_PLEASE_ENTER_VALID_NAME);
          bSafeToSave = false;
          return false;
        }
        _handleSaveRuleDependency(oItemToSave);

        aListToSave.push(oItemToSave);
      }

      if (!CS.isEmpty(oRow.children)) {
        bSafeToSave =  _recursiveProcessGridDataToSave(sContextId, aListToSave, oRow.children, oItemToSave.children, aOriginalData.id);
        return bSafeToSave;
      }
    });
    return bSafeToSave;
  };

  let _processGridDataToSave = (aListToSave, sContextId, aOriginalData, oExtraData) => {
    let oGridViewProps= _getGridViewPropsByContext(sContextId);
    let aGridData = oGridViewProps.getGridViewData();

    return _recursiveProcessGridDataToSave(sContextId, aListToSave, aGridData, aOriginalData, oExtraData);
  };

  let _handleGridViewSelectButtonClicked = (aSelectedContentIds, bSelectAllClicked, sContext) => {
    let oGridViewProps = _getGridViewPropsByContext(sContext) || SettingScreenProps.screen;

    let oSkeleton = oGridViewProps.getGridViewSkeleton();
    if (bSelectAllClicked) {
      oSkeleton.selectedContentIds = CS.isEmpty(oSkeleton.selectedContentIds) ? aSelectedContentIds : [];
    }
    else {
      let aGridData = oGridViewProps.getGridViewData();
      let sContentId = aSelectedContentIds[0] || "";
      let oContent = CS.find(aGridData, {id: sContentId});
      if (oContent && !CS.isEmpty(oContent.children)) {
        let aChildrenIds = CS.map(oContent.children, "id");
        if (CS.includes(oSkeleton.selectedContentIds, sContentId)) {
          CS.pull(oSkeleton.selectedContentIds, sContentId);
          oSkeleton.selectedContentIds = CS.difference(oSkeleton.selectedContentIds, aChildrenIds);
        } else {
          oSkeleton.selectedContentIds.push(sContentId);
          oSkeleton.selectedContentIds = CS.union(oSkeleton.selectedContentIds, aChildrenIds);
        }
      } else {
        oSkeleton.selectedContentIds = CS.xor(oSkeleton.selectedContentIds, aSelectedContentIds);
      }
    }
  };

  let _getGridDataToFetchList = (sContext) => {
    let oGridViewPropsByContext = _getGridViewPropsByContext(sContext);
    return {
      from: oGridViewPropsByContext.getGridViewPaginationData().from,
      size: oGridViewPropsByContext.getGridViewPaginationData().pageSize,
      sortBy: oGridViewPropsByContext.getGridViewSortData().sortBy,
      sortOrder: oGridViewPropsByContext.getGridViewSortData().sortOrder,
      searchText: oGridViewPropsByContext.getGridViewSearchText(),
      searchColumn: oGridViewPropsByContext.getGridViewSearchBy(),
    }
  };

  //************************************* Public API's **********************************************//
  return {
    getProcessedGridViewData: function (sContextId, aGridList, sPath, oConfigDetails, oExtraData) {
      return _getProcessedGridViewData(sContextId, aGridList, sPath, oConfigDetails, oExtraData);
    },

    preProcessDataForGridView: function (sContextId, aList, iTotalCount, oConfigDetails, sPath, oExtraData) {
      _preProcessDataForGridView(sContextId, aList, iTotalCount, oConfigDetails, sPath, oExtraData);
    },

    gridPropertyValueChanged: function (sContextId, sPropertyId, sValue, oExtraData) {
      _gridPropertyValueChanged(sContextId, sPropertyId, sValue, oExtraData);
    },

    processGridDataToSave: (aListToSave, sContextId, aOriginalData, oExtraData) => {
      return _processGridDataToSave(aListToSave, sContextId, aOriginalData, oExtraData);
    },

    createGridViewPropsByContext: (sContext, oData) => {
      _createGridViewPropsByContext(sContext, oData);
    },

    getGridViewPropsByContext: (sContext) => {
      return _getGridViewPropsByContext(sContext);
    },

    handleGridViewSelectButtonClicked: (aSelectedContentIds, bSelectAllClicked, sContext) => {
      _handleGridViewSelectButtonClicked(aSelectedContentIds, bSelectAllClicked, sContext);
    },

    getPostDataToFetchList: (sContext) => {
      return _getGridDataToFetchList(sContext);
    },

    getColumnOrganizerPropsByContext: (sContext) => {
      return _getColumnOrganizerPropsByContext(sContext);
    }
  }

})();

export default GridViewStore;
