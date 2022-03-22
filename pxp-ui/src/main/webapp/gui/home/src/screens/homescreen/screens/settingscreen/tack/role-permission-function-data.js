import {getTranslations} from '../../../../../commonmodule/store/helper/translation-manager.js';
import RolePermissionFunctionBulkEditLayoutData from './role-permission-function-bulk-edit-data.js';

let aFunctionList = function () {
  return {
    "canClone":
        {
          id: 'canClone',
          label: getTranslations().CLONE,
          className: "cloneIcon",
          permission: {
            type: 'clone'
          }
        },
    "canBulkEdit":
        {
          id: 'canBulkEdit',
          children: new RolePermissionFunctionBulkEditLayoutData,
          label: getTranslations().BULK_EDIT,
          className: "bulkEditIcon",
          permission: {
            type: 'bulkEdit',
          }
        },
    "canGridEdit":
        {
          id: 'canGridEdit',
          label: getTranslations().GRID_EDIT,
          className: "gridEditIcon",
          permission: {
            type: 'gridEdit',
          }
        },
    "canExport":
        {
          id: 'canExport',
          label: getTranslations().EXPORT,
          className: "exportIcon",
          permission: {
            type: 'export',
          }
        },
    "canImport":
        {
          id: 'canImport',
          label: getTranslations().IMPORT,
          className: "importIcon",
          permission: {
            type: 'import',
          }
        },
    "canTransfer":
        {
          id: 'canTransfer',
          label: getTranslations().TRANSFER,
          className: "transferIcon",
          permission: {
            type: 'transfer',
          }
        },
    "canShare":
        {
          id: 'canShare',
          label: getTranslations().SHARE,
          className: "shareIcon",
          permission: {
            type: 'share',
          }
        },
  };
}

export default aFunctionList;