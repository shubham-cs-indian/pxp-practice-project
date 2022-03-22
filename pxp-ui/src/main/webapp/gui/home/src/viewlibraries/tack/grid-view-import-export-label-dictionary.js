import { getTranslations } from '../../commonmodule/store/helper/translation-manager.js';

const oImportExportEntities = function () {
  return {
    tag: {
      EXPORT: getTranslations().EXPORT,
      IMPORT: getTranslations().IMPORT
    },
    attribute: {
      EXPORT: getTranslations().EXPORT,
      IMPORT: getTranslations().IMPORT
    },
    translations: {
      EXPORT: getTranslations().EXPORT,
      IMPORT: getTranslations().IMPORT
    },
    rule: {
      EXPORT: getTranslations().EXPORT,
      IMPORT: getTranslations().IMPORT
    },
    user: {
      EXPORT: getTranslations().EXPORT,
      IMPORT: getTranslations().IMPORT
    },
    tabs: {
      EXPORT: getTranslations().EXPORT,
      IMPORT: getTranslations().IMPORT
    },
    relationship: {
      EXPORT: getTranslations().EXPORT,
      IMPORT: getTranslations().IMPORT
    },
    context_variant: {
      EXPORT: getTranslations().EXPORT,
      IMPORT: getTranslations().IMPORT
    },
    task: {
      EXPORT: getTranslations().EXPORT,
      IMPORT: getTranslations().IMPORT
    }
  }
};

export default new oImportExportEntities();