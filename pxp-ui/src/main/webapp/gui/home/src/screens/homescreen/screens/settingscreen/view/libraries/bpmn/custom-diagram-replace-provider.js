import CS from '../../../../../../../libraries/cs';
import EventBus from '../../../../../../../libraries/eventdispatcher/EventDispatcher';
import Replace from 'diagram-js/lib/features/replace/Replace.js';

const oEvents = {
  BPMN_PROCESS_AND_SET_NEW_CUSTOM_ELEMENT: "bpmn_process_and_set_new_custom_element",
  BPMN_PROCESS_AND_SET_NEW_ELEMENT: "bpmn_process_and_set_new_element"
};

function CustomDiagramReplaceProvider () {
  function CustomDiagramReplace (modeling, oBpmnFactory) {
    Replace.call(this, modeling);
    this._bpmnFactory = oBpmnFactory;
  }

  CustomDiagramReplace.$inject = ['modeling', 'bpmnFactory'];

  let fReplaceElement = Replace.prototype.replaceElement;

  let fCustomReplaceElement = function (oldElement, newElementData, options) {
    //whenever an element is replaced this is called. We initialize task with delegate exp need to assign it to the
    // new obj
    let oNewElement = fReplaceElement.call(this, oldElement, newElementData, options);
    //delete previous extension element
    delete oNewElement.businessObject.extensionElements;
    delete oNewElement.businessObject.documentation;

    if (!CS.isEmpty(oldElement.customDefinition)) {
      let oCustomDefinition = oldElement.customDefinition;
      EventBus.dispatch(oEvents.BPMN_PROCESS_AND_SET_NEW_CUSTOM_ELEMENT, oNewElement, oCustomDefinition, this._bpmnFactory, this._modeling);
    } else {
      EventBus.dispatch(oEvents.BPMN_PROCESS_AND_SET_NEW_ELEMENT, oNewElement);
    }
    return oNewElement;
  };

  CustomDiagramReplace.prototype = Replace.prototype;

  Replace.prototype.replaceElement = fCustomReplaceElement;

  return {
    __init__: ['replace'],
    replace: ['type', CustomDiagramReplace]
  };
}


export const customDiagramReplaceProvider = CustomDiagramReplaceProvider;
export const events = oEvents;
