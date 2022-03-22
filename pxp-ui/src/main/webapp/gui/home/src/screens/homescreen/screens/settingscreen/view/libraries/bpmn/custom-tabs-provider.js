
import CS from '../../../../../../../libraries/cs';
import ViewUtils from '../../utils/view-utils';
import EventBus from '../../../../../../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations as oTranslations } from '../../../../../../../commonmodule/store/helper/translation-manager.js';
import inherits from 'inherits';
import PropertiesActivator from 'bpmn-js-properties-panel/lib/PropertiesActivator';
import Camunda from 'bpmn-js-properties-panel/lib/provider/camunda';
import CamundaPropertiesProvider from 'bpmn-js-properties-panel/lib/provider/camunda/CamundaPropertiesProvider.js';
import CustomPropertyGroupProvider from './custom-property-group-provider';

const oEvents = {
  SET_ACTIVE_BPMN_INSTANCES: "set_active_bpmn_element",
  BPMN_CUSTOM_TABS_PROVIDER_UPDATE: "bpmn_custom_tabs_provider_update"
};


var CustomTabsProvider = (function () {

  var oConfigDetails = {};
  let oCustomPropertiesData = {};
  let oActiveProcess = {};
  let oTaxonomyData ={};
  let oValidationInfo = {};


  function createCustomTabGroups (element, bpmnFactory, modeling, bIsAdvancedTabEnabled) {
// note: put custom tabs entries here
    var oConfigGroup = {
      id: 'ConfigGroup',
      label: oTranslations().PROPERTIES,
      entries: []
    };
    //
    CustomPropertyGroupProvider(oConfigGroup, element, bpmnFactory, modeling, oConfigDetails, oCustomPropertiesData, bIsAdvancedTabEnabled, oActiveProcess, oTaxonomyData, oValidationInfo);

    return [
      oConfigGroup
    ];
  }


  function CustomTabPropertiesProvider (eventBus, bpmnFactory, elementRegistry, elementTemplates, translate, modeling) {

    /** To get the tabs and properties from Camunda **/
    CamundaPropertiesProvider.call(this, eventBus, bpmnFactory, elementRegistry, elementTemplates, translate);
    CamundaPropertiesProvider.$inject.push("modeling");


    /** Assign CamundaPropertiesProvider static properties into CustomTabPropertiesProvider **/
    CS.assign(CustomTabPropertiesProvider, CamundaPropertiesProvider);


    /** Preserve Camunda getTabs method **/
    var fun = this.getTabs;

    this.getTabs = function (element) {

      var aTabs = fun.call(this, element);
      var oConfigTab = {
        id: 'config-tab',
        label: oTranslations().CAMUNDA_TAB_CONFIGURATION,
        groups: []
      };

      let sCustomElementId = ViewUtils.getBPMNCustomElementIDFromBusinessObject(element.businessObject);
      if (!CS.isEmpty(sCustomElementId)) {
        /****Show Custom Config Tab when custom Element Id is present ****/
        oConfigTab.groups = createCustomTabGroups(element, bpmnFactory, modeling, oCustomPropertiesData.isAdvancedOptionsEnabled);
        !oCustomPropertiesData.isAdvancedOptionsEnabled && CS.forEach(aTabs, function (oTab) {
          oTab.enabled = function () {
            return false;
          }
        });
        aTabs.unshift(oConfigTab);
        EventBus.dispatch(oEvents.SET_ACTIVE_BPMN_INSTANCES, element, bpmnFactory, modeling);
      } else {
        oConfigTab.enabled = function () {
          return false;
        };
        aTabs.push(oConfigTab)
      }
      return aTabs;
    };
  }

  CustomTabPropertiesProvider.$inject = [
    'eventBus',
    'bpmnFactory',
    'elementRegistry',
    'elementTemplates',
    'translate',
    'modeling'
  ];

  inherits(CustomTabPropertiesProvider, PropertiesActivator);

  /** Export Camunda dependecies **/
  var oExportingObject = CS.assign({}, Camunda);
  oExportingObject.propertiesProvider = [ 'type', CustomTabPropertiesProvider ];

  return {
    setBPMNPropertiesViewData: function (_oConfigDetails, _oCustomPropertiesData, _oActiveProcess, _oTaxonomyData, _oValidationInfo) {
      let bFireUpdateTabs = (oCustomPropertiesData.isAdvancedOptionsEnabled !== _oCustomPropertiesData.isAdvancedOptionsEnabled) || (!CS.isEqual(oTaxonomyData, _oTaxonomyData));
      oConfigDetails = _oConfigDetails;
      oCustomPropertiesData = _oCustomPropertiesData;
      oActiveProcess = _oActiveProcess;
      oTaxonomyData = _oTaxonomyData;
      oValidationInfo = _oValidationInfo;
      /*******Update Properties panel wrt New PropertieBPMNCustomPropertiesStore;*/
      bFireUpdateTabs && EventBus.dispatch(oEvents.BPMN_CUSTOM_TABS_PROVIDER_UPDATE);
    },

    exportingObject: oExportingObject
  }


})();


export const view = CustomTabsProvider;
export const events = oEvents;
