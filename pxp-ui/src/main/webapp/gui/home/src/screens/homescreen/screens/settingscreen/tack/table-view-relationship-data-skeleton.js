import {getTranslations} from '../../../../../commonmodule/store/helper/translation-manager.js';
import {oTableRenderTypeConstant} from './table-render-type';

var TableViewRelationshipDataSkeleton = function () {
  return [
    {
      id: "label",
      value: getTranslations().RELATIONSHIP,
      width: 230,
      renderType: oTableRenderTypeConstant.LABEL,
      iconClass: "",
      labelClassName: ""
    },
    {
      id: "visible",
      value: getTranslations().VISIBLE,
      width:90,
      renderType: oTableRenderTypeConstant.LABEL,
      iconClass: "eyeIcon",
      labelClassName: ""
    },
    {
      id: "canAdd",
      value: getTranslations().CAN_ADD,
      width: 90,
      renderType: oTableRenderTypeConstant.LABEL,
      iconClass: "addIcon",
      labelClassName: ""
    },
    {
      id: "canRemove",
      value: getTranslations().CAN_REMOVE,
      width: 90,
      renderType: oTableRenderTypeConstant.LABEL,
      iconClass: "removeIcon",
      labelClassName: ""
    },
    {
      id: "canEdit",
      value: getTranslations().CAN_EDIT,
      width: 90,
      renderType: oTableRenderTypeConstant.LABEL,
      iconClass: "editIcon",
      labelClassName: ""
    }
  ]
};

export default TableViewRelationshipDataSkeleton;