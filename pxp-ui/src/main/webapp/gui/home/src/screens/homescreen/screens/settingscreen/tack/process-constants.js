export default {

  /// Custom Element Types
  CUSTOM_ELEMENT_TYPE_SERVICE_TASK: "CustomServiceTask",
  CUSTOM_ELEMENT_TYPE_USER_TASK: "CustomUserTask",
  CUSTOM_ELEMENT_TYPE_START_EVENT: "CustomStartEvent",
  CUSTOM_ELEMENT_TYPE_END_EVENT: "CustomEndEvent",
  CUSTOM_ELEMENT_TYPE_CALL_ACTIVITY: "customCallActivity",

  ///Action Name

  REPLACE_ACTION_SERVICE_TASK: "custom-replace-with-service-task",
  REPLACE_ACTION_USER_TASK: "custom-replace-with-user-task",
  REPLACE_ACTION_START_EVENT: "custom-replace-with-start-event",
  REPLACE_ACTION_END_EVENT: "custom-replace-with-end-event",
  REPLACE_ACTION_CALL_ACTIVITY: "custom-replace-with-call-activity",


  //Icons
  ICON_CLASS_SERVICE_TASK:"bpmn-icon-service",
  ICON_CLASS_USER_TASK:"bpmn-icon-user",
  ICON_CLASS_START_EVENT: "bpmn-icon-start-event-none",
  ICON_CLASS_END_EVENT: "bpmn-icon-end-event-none",
  ICON_CLASS_PROCESS_START_EVENT: "bpmn-icon-process-start-event-none",
  RECEIVE_TASK: "bpmn-icon-receive",
  SERVICE_TASK: "bpmn-icon-service",
  SEND_TASK: "bpmn-icon-send",
  CALL_ACTIVITY: "bpmn-icon-call-activity",
  SCRIPT_TASK: "bpmn-icon-service",

  //Targets to render

  TARGET_SERVICE_TASK:"bpmn:ServiceTask",
  TARGET_USER_TASK:"bpmn:UserTask",
  TARGET_START_EVENT: "bpmn:StartEvent",
  TARGET_END_EVENT: "bpmn:EndEvent",
  TARGET_CALL_ACTIVITY: "bpmn:CallActivity",


  //Business Object Constants
  BO_ELEMENT_INPUT_OUTPUT:"camunda:InputOutput",
  BO_ELEMENT_EXTENSION_ELEMENTS:"bpmn:ExtensionElements",
  BO_ELEMENT_LIST: "camunda:List",
  BO_ELEMENT_MAP: "camunda:Map",
  BO_ELEMENT_IN: "camunda:In",
  BO_ELEMENT_OUT: "camunda:Out",


  //Extentsion Types
  EXTENSION_INPUT_PARAMETER:"camunda:InputParameter",
  EXTENSION_INPUT_ENTRY: "camunda:Entry",
  EXTENSION_TASK_LISTENER:"camunda:TaskListener",
  EXTENSION_EXECUTION_LISTENER: "camunda:ExecutionListener",

  //Tasks to hide from UI
  BGP_TASK : "BGP_TASK",
  EVENT_TASK : "EVENT_TASK",
  DRAFT_TASK : "DRAFT_TASK",
};