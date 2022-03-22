import { getTranslations as oTranslations } from '../store/helper/translation-manager.js';

const notificationsDictionary = function () {
  return {
    comment: oTranslations().COMMENTED_ON,
    attachmentUploaded: oTranslations().ATTACHMENT_UPLOADED,
    attachmentDeleted: oTranslations().ATTACHMENT_DELETED,
    dueDateChanged: oTranslations().DUE_DATE_CHANGED,
    startDateChanged: oTranslations().START_DATE_CHANGED,
    overDueDateChanged: oTranslations().OVER_DUE_DATE_CHANGED,
    usersAdded: oTranslations().USER_ADDED,
    usersDeleted: oTranslations().USER_REMOVED,
    taskDeleted: oTranslations().DELETED,
    taskModified: oTranslations().TASK_MODIFIED,
    statusChanged: oTranslations().STATUS_CHANGED,
    priorityChanged: oTranslations().PRIORITY_CHANGED,
    subtaskAdded: oTranslations().SUBTASK_ADDED,
    subtaskDeleted: oTranslations().DELETED,
    assignedYou: oTranslations().ASSIGNED_YOU,
    removedYou: oTranslations().REMOVED_YOU,
    commentWithAttachment: oTranslations().COMMENT_WITH_ATTACHMENT,
    privacyChanged: oTranslations().PRIVACY_CHANGED,
    labelChanged: oTranslations().LABEL_CHANGED,
    descriptionChanged: oTranslations().DESCRIPTION_CHANGED,
    dueDateToday : oTranslations().DUE_DATE_TODAY,
    dueDateApproaching : oTranslations().DUE_DATE_APPROACHING,
    dueDateCrossed : oTranslations().DUE_DATE_CROSSED
  };
};
export default notificationsDictionary;