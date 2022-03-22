import { getTranslations as oTranslations } from '../../../../../../commonmodule/store/helper/translation-manager';
const dialogLayoutData = function () {
  return [
  {
    id: "11",
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
        label: oTranslations().CREATE_CLASS_AS_NATURE_TYPE,
        key: "isNature",
        type: "yesNo",
        width: 50
      },
      {
        id: "4",
        label: oTranslations().TYPE,
        key: "natureType",
        type: "mssNew",
        width: 50
      },
      {
        id: "6",
        label: oTranslations().TYPE,
        key: "endpointType",
        type: "mssNew",
        width: 50
      },
      {
        id: "7",
        label: oTranslations().CODE,
        key: "code",
        type: "singleText",
        width: 50
      },
      {
        id: "14",
        label: "Domain",
        key: "domain",
        type: "singleText",
        width: 50
      },
      {
        id: "18",
        label: oTranslations().ACCESS,
        key: "eventAccess",
        type: "mssNew",
        width: 50
      },
      {
        id: "3",
        label: oTranslations().TYPE,
        key: "type",
        type: "mssNew",
        width: 50
      },
      {
        id: "8",
        label: oTranslations().RULE_TYPE,
        key: "ruleType",
        type: "mssNew",
        width: 50
      },
      {
        id: "9",
        label: oTranslations().ROLE_TYPE,
        key: "roleType",
        type: "mssNew",
        width: 50
      },
      {
        id: "10",
        label: oTranslations().NATURE_CLASS,
        key: "goldenRecordNatureClass",
        type: "lazyMSS",
        width: 100
      },
      {
        id: "12",
        label: oTranslations().NATURE_CLASS,
        key: "natureRelationship",
        type: "lazyMSS",
        width: 100
      },
      {
        id: "13",
        label: oTranslations().LANGUAGE_DEPENDENT,
        key: "isTranslatable",
        type: "yesNo",
        width: 50
      },
      {
        id: "14",
        label: oTranslations().LANGUAGE_DEPENDENT,
        key: "isLanguageDependent",
        type: "yesNo",
        width: 50
      },
      {
        id: "11",
        label: oTranslations().IMAGE_RESOLUTION,
        key: "imageResolution",
        type: "singleTextNumber",
        width: 100,
        max : 9999
      },
      {
        id: "12",
        label: oTranslations().IMAGE_EXTENSION,
        key: "imageExtension",
        type: "singleText",
        width: 100
      },
      {
        id: "13",
        label: oTranslations().LANGUAGES,
        key: "dataLanguages",
        type: "mssNew",
        width: 50
      },
      {
        id: "15",
        label: "SSO Setting",
        key: "ssoSetting",
        type: "mssNew",
        width: 100
      },
      //DAM SPECIFIC
      {
        id: "14",
        label: oTranslations().EXTENSION,
        key: "extension",
        type: "singleText",
        width: 100
      },
      {
        id: "15",
        label: oTranslations().EXTRACT_METADATA,
        key: "extractMetadata",
        type: "yesNo",
        width: 50
      },
      {
        id: "16",
        label: oTranslations().EXTRACT_RENDITIONS,
        key: "extractRendition",
        type: "yesNo",
        width: 50
      },
      {
        id: "17",
        label: oTranslations().CONTEXT,
        key: "contextId",
        type: "lazyMSS",
        width: 50
      },
        //Prices
      {
        id: "15",
        label: oTranslations().SOURCE_PRICE,
        key: "sourcePriceId",
        type: "lazyMSS",
        width: 50
      },
      {
        id: "16",
        label: oTranslations().TARGET_PRICE,
        key: "targetPriceId",
        type: "lazyMSS",
        width: 50
      },
      {
        id: "18",
        label: oTranslations().WORKFLOW_TYPE,
        key: "workflowType",
        type: "mssNew",
        width: 50
      },
      {
        id: "5",
        label: oTranslations().EVENT_TYPE,
        key: "eventType",
        type: "mssNew",
        width: 50
      },
      {
        id: "19",
        label: oTranslations().ACTION_TYPE,
        key: "triggeringEventType",
        type: "mssNew",
        width: 50
      },
    ]}
]};

export default dialogLayoutData;