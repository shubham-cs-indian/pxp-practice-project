/**
 * Created by CS97 on 07-03-2017.
 */
import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';

const oPropertyCollectionLayoutData = function () {
  return [{
    id: "1",
    label: oTranslations().BASIC_INFORMATION,
    elements: [
      {
        id: "1",
        label: oTranslations().NAME,
        key: "label",
        type: "singleText",
        width: 20
      },
      {
        id: "6",
        label: oTranslations().TAB,
        key: "tab",
        type: "lazyMSS",
        width: 25
      },
      {
        id: "2",
        label: oTranslations().ICON,
        key: "icon",
        type: "image",
        width: 15
      },
      {
        id: "3",
        label: oTranslations().X_RAY_DEFAULT,
        key: "isDefaultForXRay",
        type: "yesNo",
        width: 15
      },
    ]
  }]
};
export default oPropertyCollectionLayoutData;