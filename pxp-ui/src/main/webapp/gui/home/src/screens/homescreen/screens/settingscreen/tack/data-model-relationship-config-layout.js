import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';

const dataModelRelationshipConfigLayout =function () {
  return {
    dataGovernanceConfigInformation: [
      {
        id: "1",
        label: oTranslations().BASIC_INFORMATION,
        elements: [
          {
            id: "1",
            label: oTranslations().NAME,
            key: "label",
            type: "singleText",
            width: 50
          },
          {
            id: "2",
            label: oTranslations().CODE,
            key: "code",
            type: "singleText",
            width: 50,
            isSortable: true
          },
          {
            id: "3",
            label: oTranslations().TYPE,
            key: "type",
            type: "mssNew",
            width: 33.33,
            isSortable: true
          },
          {
            id: "4",
            label: oTranslations().TAB,
            key: "tab",
            type: "lazyMSS",
            width: 33.33
          },
          {
            id: "5",
            label: oTranslations().ICON,
            key: "icon",
            type: "image",
            width: 33.33
          }
        ]
      },
    ],

    dataModelExtensionLayout: [
      {
        id: "3",
        label: "Ext",
        elements: [
          {
            id: "7",
            label: oTranslations().ENABLE_AFTER_SAVE,
            key: "enableAfterSave",
            type: "yesNo",
            width: 50
          },
          {
            id: "6",
            label: oTranslations().DATA_TRANSFER_PROPERTIES,
            key: "configDetails",
            type: "customView",
            width: 100
          }
        ]
      }



    ],
    dataModelKlassSelectionLayout1: [
      {
        id: "5",
        label: "rww",
        elements: [
          {
            id: "5",
            key: "klassview",
            type: "customView",
            width: 100
          }
        ]
      }



    ]
  }
};

export default dataModelRelationshipConfigLayout;