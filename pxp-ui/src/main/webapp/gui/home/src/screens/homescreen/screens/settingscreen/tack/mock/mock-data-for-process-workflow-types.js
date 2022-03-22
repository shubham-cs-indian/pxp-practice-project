import { getTranslations as oTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import MockDataForWorkflowTypesDictionary from '../../tack/mock/mock-data-for-workflow-types-dictionary';
export default function () {
  return [
    {
      id: MockDataForWorkflowTypesDictionary.SCHEDULED_WORKFLOW,
      label: oTranslations().SCHEDULED,
    },
    {
      id: MockDataForWorkflowTypesDictionary.STANDARD_WORKFLOW,
      label: oTranslations().STANDARD,
    },
    {
      id: MockDataForWorkflowTypesDictionary.TASKS_WORKFLOW,
      label: oTranslations().TASKS,
    },
    {
      id: MockDataForWorkflowTypesDictionary.JMS_WORKFLOW,
      label: oTranslations().JMS_WORKFLOW,
    }
  ];
};
