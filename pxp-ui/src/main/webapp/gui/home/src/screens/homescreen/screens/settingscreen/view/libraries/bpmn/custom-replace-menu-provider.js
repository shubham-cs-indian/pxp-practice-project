import CS from '../../../../../../../libraries/cs';
import { is } from 'bpmn-js/lib/util/ModelUtil';
import ViewUtils from './../../utils/view-utils';
import ReplaceMenuProvider from 'bpmn-js/lib/features/popup-menu/ReplaceMenuProvider.js';
import ProcessConstants from './../../../tack/process-constants';
import oReplaceOptions from 'bpmn-js/lib/features/replace/ReplaceOptions';
let fGetEntries = ReplaceMenuProvider.prototype.getEntries;

function CustomReplaceMenuProvider () {

  let _this;

  let CustomReplaceMenu = function (popupMenu, modeling, moddle,
                                    bpmnReplace, rules, translate) {
    ReplaceMenuProvider.call(this, popupMenu, modeling, moddle,
        bpmnReplace, rules, translate);

    _this = this;
  };

  CustomReplaceMenu.prototype = ReplaceMenuProvider.prototype;

  CustomReplaceMenu.prototype._createMenuEntry = function (definition, element, action) {
    var replaceElement = _this._bpmnReplace.replaceElement;

    //overriding _createMenuEntry in order to add delegateExpression to the BO

    var translate = _this._translate;

    var replaceAction = function () {
      return replaceElement(element, definition.target);
    };

    if (!CS.isEmpty(definition.customElementType)) {
      action = function () {
        /*******Custom Action******/
        let oNewElement;
        let oNewDefinition = CS.cloneDeep(definition);
        element.customDefinition = oNewDefinition;
        oNewElement = replaceElement(element, oNewDefinition.target);
        return oNewElement;
      }
    }

    action = action || replaceAction;

    var menuEntry = {
      label: translate(definition.label),
      className: definition.className,
      id: definition.actionName,
      action: action
    };

    return menuEntry;
  };

  let _getKeyForReplaceOptionsFilter = (sElementType) => {
    let sKeyForReplaceOptions;
    switch (sElementType) {
      case ProcessConstants.TARGET_SERVICE_TASK:
      case ProcessConstants.TARGET_USER_TASK:
        sKeyForReplaceOptions = "TASK";
        break;
      case ProcessConstants.TARGET_START_EVENT:
        sKeyForReplaceOptions = "START_EVENT";
        break;
      case ProcessConstants.TARGET_END_EVENT:
        sKeyForReplaceOptions = "END_EVENT";
        break;
    }
    return sKeyForReplaceOptions;
  };

  let _filterExtendedEntries = function (oElement, sKeyToFilterReplaceOptions) {
    let aEntries = fGetEntries.call(this, oElement);
    let aCustomOptions = CS.filter(oReplaceOptions[sKeyToFilterReplaceOptions], function (oReplaceOption) {
      return !CS.isEmpty(oReplaceOption.customElementID);
    });

    let aCustomEntries = this._createEntries(oElement, aCustomOptions);
    !CS.isEmpty(aCustomEntries) && aEntries.push(...aCustomEntries);
    return aEntries;
  };

  CustomReplaceMenu.prototype.getEntries = function (oElement) {
    var oBusinessObject = oElement.businessObject;
    let sCustomElementId = ViewUtils.getBPMNCustomElementIDFromBusinessObject(oBusinessObject);
    let aEntries = [];
    /***Add Extended Target Types Here***/
    let aExtendedTaskTargetTypes = [ProcessConstants.TARGET_SERVICE_TASK, ProcessConstants.TARGET_USER_TASK];
    let aExtendedTargetEventTypes = [ProcessConstants.TARGET_START_EVENT,ProcessConstants.TARGET_END_EVENT];

    let sKeyToFilterReplaceOptions = _getKeyForReplaceOptionsFilter(oElement.type);
    if (is(oBusinessObject, 'bpmn:FlowNode') && !CS.isEmpty(sCustomElementId) && !CS.isEmpty(sKeyToFilterReplaceOptions)) {
      /** For Custom Element
       * filter custom elements based on customElementID Ignore Factory Element**/
      aEntries = CS.filter(oReplaceOptions[sKeyToFilterReplaceOptions], function (oReplaceOption) {
        return oReplaceOption.customElementID != sCustomElementId;
      });
      return this._createEntries(oElement, aEntries);
    } else if (CS.indexOf(aExtendedTaskTargetTypes, oElement.type) !== -1 && !CS.isEmpty(sKeyToFilterReplaceOptions)) {
      /** For Factory Element of which type is extended
       * filter factory options Ignore custom Elements**/
      return _filterExtendedEntries.call(_this, oElement, sKeyToFilterReplaceOptions);
    } else if (CS.indexOf(aExtendedTargetEventTypes, oElement.type) !== -1 && !CS.isEmpty(sKeyToFilterReplaceOptions) && CS.isEmpty(oBusinessObject.eventDefinitions)) {
      return _filterExtendedEntries.call(_this, oElement, sKeyToFilterReplaceOptions);
    } else {
      /***
       For factory non extended Element
       Factory Filter
       ***/
      return fGetEntries.call(_this, oElement)
    }
  };

  CustomReplaceMenu.$inject = [
    'popupMenu',
    'modeling',
    'moddle',
    'bpmnReplace',
    'rules',
    'translate'
  ];

  return {
    __depends__: [
      require('diagram-js/lib/features/popup-menu'),
      require('./custom-diagram-replace-provider.js').customDiagramReplaceProvider
    ],
    __init__: ['replaceMenuProvider'],
    replaceMenuProvider: ['type', CustomReplaceMenu]
  };
}

export default CustomReplaceMenuProvider;






