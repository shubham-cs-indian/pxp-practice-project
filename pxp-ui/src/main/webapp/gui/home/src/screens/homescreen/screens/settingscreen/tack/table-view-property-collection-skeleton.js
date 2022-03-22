import { getTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';
import {oTableRenderTypeConstant} from './table-render-type';

var TableViewPropertyCollectionSkeleton = function () {
  return [
      {
        id: "label",
        label: "",
        width: 230,
        renderType: oTableRenderTypeConstant.LABEL,
        iconClass: "",
        isCollapsed: true,
        labelClassName: "showLabel "
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
      }
    ]
};

export default TableViewPropertyCollectionSkeleton;