import { getTranslations } from '../../../../../../commonmodule/store/helper/translation-manager.js';
import oConfigEntityTypeDictionary from '../../../../../../commonmodule/tack/config-entity-type-dictionary';
import oModulesDictionary from "./config-modules-dictionary";

let aConfigModuleMonitoringEntityList = function () {
  return [
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_AUDIT_LOG,
      label: getTranslations().AUDIT_LOG,
      children: [/**No Children*/],
      className: "actionItemAuditLog",
      parentId: oModulesDictionary.MONITORING
    },
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_DOWNLOADTRACKER,
      label: getTranslations().DOWNLOAD_TRACKER,
      children: [/**No Children*/],
      className: "actionItemDownloadTracker",
      parentId: oModulesDictionary.MONITORING
    },
    {
      id: oConfigEntityTypeDictionary.ENTITY_TYPE_ADMINISTRATION_SUMMARY,
      label: getTranslations().ADMINISTRATION_SUMMARY,
      children: [/**No Children*/],
      className: "actionItemAdministrationSummary",
      parentId: oModulesDictionary.MONITORING
    }
  ];
};

export default aConfigModuleMonitoringEntityList;
