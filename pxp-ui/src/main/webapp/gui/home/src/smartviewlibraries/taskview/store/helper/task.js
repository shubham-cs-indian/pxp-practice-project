import UniqueIdentifierGenerator from '../../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
import TaskProps from '../model/task-props';

var Task = function (aTypeId, iCreatedOn , iStartDate , iDueDate , iOverDueDate, sElementId, sVariantId, sContentId, iPositionX, iPositionY, sBaseType, sEntityBaseType) {
  var oTaskData = TaskProps.getTaskData();
  var oCurrentUser = oTaskData.currentUser;

  return {
    id: UniqueIdentifierGenerator.generateUUID(),
    name: UniqueIdentifierGenerator.generateUntitledName(),
    icon: null,
    types: aTypeId,
    baseType: sBaseType,
    linkedEntities: [
      {
        id: UniqueIdentifierGenerator.generateUUID(),
        contentId: sContentId,
        elementId: sElementId,
        variantId: sVariantId,
        type: sEntityBaseType || sBaseType,
        position: {
          x: iPositionX,
          y: iPositionY
        }
      }
    ],
    eventSchedule: {
      startTime: iStartDate,
      endTime: null,
      id: UniqueIdentifierGenerator.generateUUID(),
      lastModifiedBy: oCurrentUser.id,
      repeat: {
        repeatType: "NONE",
        daysOfWeek: {
          MON: false,
          TUE: false,
          WED: false,
          THU: true,
          FRI: false,
          SAT: false,
          SUN: false
        },
        repeatEvery: 1,
        endsOn: null,
        endsAfter: 0
      }
    },
    calendarDates: [],
    exclusions: [],
    inclusions: [],
    status: null,
    subTasks: [],
    attachments: [],
    overDueDate: null,
    createdBy: oCurrentUser.id,
    priority: null,
    longDescription: "",
    comments: [],
    location: {},
    timezone: "",
    notifications: [],
    startDate: iStartDate,
    dueDate: null,
    createdOn: iCreatedOn,
    isPrivate: false,
    isFavourite: false,
    globalPermission: {
      canCreate: true,
      canDelete: true,
      canEdit: true,
      canRead: true
    }
  };
};

export default Task;