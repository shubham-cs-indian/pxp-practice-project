import { getTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';

const configLayoutData = function () {
  return {
    organisationConfigBasicInformation: [
      {
        id: "1",
        label: getTranslations().BASIC_INFORMATION,
        elements: [
          {
            id: "11",
            label: getTranslations().NAME,
            key: "label",
            type: "singleText",
            width: 50
          },
          {
            id: "l7",
            label: getTranslations().CODE,
            key: "code",
            type: "singleText",
            width: 50,
            isSortable: true
          },
          {
            id: "18",
            label: getTranslations().ICON,
            key: "icon",
            type: "image",
            width: 50
          },
          {
            id: "12",
            label: getTranslations().ORGANISATION_TYPE,
            key: "type",
            type: "mss",
            width: 50
          },
          {
            id: "13",
            label: getTranslations().TAXONOMIES,
            key: "availability",
            type: "customView",
            width: 50
          },
          {
            id: "16",
            label: getTranslations().TARGET_CLASSES,
            key: "targetClasses",
            type: "lazyMSS",
            width: 50
          },
          {
            id: "14",
            label: getTranslations().PHYSICAL_CATALOG,
            key: "physicalCatalogs",
            type: "selectionToggle",
            width: 100
          },
          {
            id: "15",
            label: getTranslations().SYSTEMS,
            key: "endpointIds",
            type: "customView",
            width: 100
          },
          {
            id: "19",
            label: getTranslations().PORTAL,
            key: "portals",
            type: "selectionToggle",
            width: 100
          },
        ]
      }
    ]
  }
};
export default configLayoutData;
