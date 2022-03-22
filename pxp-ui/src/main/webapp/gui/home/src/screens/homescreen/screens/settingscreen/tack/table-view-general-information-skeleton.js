import {getTranslations} from '../../../../../commonmodule/store/helper/translation-manager.js';
import {oTableRenderTypeConstant} from './table-render-type';

var TableViewGeneralInformationSkeleton = function () {
  return [
    {
      id: "label",
      value: "General Information",
      width: 230,
      renderType: oTableRenderTypeConstant.LABEL,
      labelClassName: ""
    },
    {
      id: "visible",
      label: getTranslations().VISIBLE,
      width: 90,
      renderType: oTableRenderTypeConstant.BOOLEAN,
      iconClass: "eyeIcon",
      labelClassName: "showLabel "
    },
    {
      id: "canEdit",
      label: getTranslations().CAN_EDIT,
      width: 90,
      renderType: oTableRenderTypeConstant.BOOLEAN,
      iconClass: "editIcon",
      labelClassName: "showLabel "
    },
    {
      id: "canAdd",
      label: getTranslations().CAN_ADD,
      width: 90,
      renderType: oTableRenderTypeConstant.BOOLEAN,
      iconClass: "addIcon",
      labelClassName: "showLabel "
    },
    {
      id: "canRemove",
      label: getTranslations().CAN_REMOVE,
      width: 90,
      renderType: oTableRenderTypeConstant.BOOLEAN,
      iconClass: "removeIcon",
      labelClassName: "showLabel "
    }
  ]
};

export default TableViewGeneralInformationSkeleton;