import RoleIdDictionary from './../../../commonmodule/tack/role-id-dictionary';
import UniqueIdentifierGenerator from '../../../libraries/uniqueidentifiergenerator/unique-identifier-generator';
const generateUUID = UniqueIdentifierGenerator.generateUUID;

export default function (sContext) {
  return [
    {
      "id": generateUUID(),
      "labelKey": "ALL",
      "context": sContext,
      "tasksCount": 0,
      "bShowCount": false,
      "listItemKey": "all"
    },
    {
      "id": generateUUID(),
      "labelKey": "RESPONSIBLE",
      "context": sContext,
      "listItemKey": RoleIdDictionary.ResponsibleRoleId,
      "tasksCount": 0,
      "bShowCount": false,
    },
    {
      "id": generateUUID(),
      "labelKey": "ACCOUNTABLE",
      "context": sContext,
      "listItemKey": RoleIdDictionary.AccountableRoleId,
      "tasksCount": 0,
      "bShowCount": false,
    },
    {
      "id": generateUUID(),
      "labelKey": "CONSULTED",
      "context": sContext,
      "listItemKey": RoleIdDictionary.ConsultedRoleId,
      "tasksCount": 0,
      "bShowCount": false,
    },
    {
      "id": generateUUID(),
      "labelKey": "INFORMED",
      "context": sContext,
      "listItemKey": RoleIdDictionary.InformedRoleId,
      "tasksCount": 0,
      "bShowCount": false,
    },
    {
      "id": generateUUID(),
      "labelKey": "VERIFIED",
      "context": sContext,
      "listItemKey": RoleIdDictionary.VerifyRoleId,
      "tasksCount": 0,
      "bShowCount": false,
    },
    {
      "id": generateUUID(),
      "labelKey": "SIGN_OFF",
      "context": sContext,
      "listItemKey": RoleIdDictionary.SignOffRoleId,
      "tasksCount": 0,
      "bShowCount": false,
    },
    {
      "id": generateUUID(),
      "labelKey": "OTHERS_TASKS",
      "context": sContext,
      "listItemKey": "otherTasks",
      "tasksCount": 0,
      "bShowCount": false,
    }
  ]
};