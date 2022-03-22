let oEventType = {
  BUSINESS_PROCESS: {
    id: "BUSINESS_PROCESS",
    label: "BUSINESS_PROCESS_WORKFLOW",
  }
  ,
  INTEGRATION: {
    id: "INTEGRATION",
    label: "INTEGRATION",
  }
};

let oWorkflowType = {
  SCHEDULED_WORKFLOW: {
    id: "SCHEDULED_WORKFLOW",
    label: "SCHEDULED"
  },
  STANDARD_WORKFLOW: {
    id: "STANDARD_WORKFLOW",
    label: "STANDARD"
  },
  TASKS_WORKFLOW: {
    id: "TASKS_WORKFLOW",
    label: "TASKS"
  },
  JMS_WORKFLOW: {
    id: "JMS_WORKFLOW",
    label: "JMS_WORKFLOW"
  }
};

let oTriggeringType = {
  afterSave: {
    id: "afterSave",
    label: "AFTER_SAVE"
  },
  afterCreate: {
    id: "afterCreate",
    label: "AFTER_CREATE"
  },
  transfer: {
    id: "transfer",
    label: "TRANSFER"
  }
};

let oTimerDefinitionType = {
  timeDate: {
    id: "timeDate",
    label: "DATE"
  },
  timeDuration: {
    id: "timeDuration",
    label: "DURATION"
  },
  timeCycle: {
    id: "timeCycle",
    label: "CYCLE"
  }
};

let oActivation = {
  activate: {
    id: "activate",
    label: "ACTIVATE"
  },
  deactivate: {
    id: "deactivate",
    label: "DEACTIVATE"
  }
};

let oPhysicalCatalogIds = {
  pim: {
    id: "pim",
    label: "PIM"
  },
  onboarding: {
    id: "onboarding",
    label: "ONBOARDING"
  },
  offboarding: {
    id: "offboarding",
    label: "OFFBOARDING"
  },
  dataIntegration: {
    id: "dataIntegration",
    label: "DATA_INTEGRATION"
  }
};

export default {
  eventType: oEventType,
  workflowType: oWorkflowType,
  triggeringType: oTriggeringType,
  timerDefinitionType: oTimerDefinitionType,
  activation: oActivation,
  physicalCatalogIds: oPhysicalCatalogIds
};