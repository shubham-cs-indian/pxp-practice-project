import CS from '../../../../../../libraries/cs';
import {getTranslations} from '../../../../../../commonmodule/store/helper/translation-manager.js';
import oModulesDictionary from './config-modules-dictionary';
import aDataModelEntityList from './config-module-data-model-entity-list';
import aPartnerAdminEntityList from './config-module-partner-admin-entity-list';
import aDataGovernanceEntityList from './config-module-data-governance-entity-list';
import aDataCollaborationEntityList from './config-module-data-collaboration-entity-list';
import aDataIntegrationEntityList from './config-module-data-integration-entity-list';
import aTranslationConfigurationList from './config-module-translation-configuration-entity-list';
import aAdminConfigurationEntityList from './config-module-admin-configuration-entity-list';
import aMonitoringEntityList from './config-module-monitoring-entity-list';

let aSettingScreenModulesDictionary = function () {
  return [
    {
      id: oModulesDictionary.DATA_MODEL,
      label: getTranslations().CONFIG_MODULE_DATA_MODEL,
      className: "tabDataModel",
      children: CS.sortBy(new aDataModelEntityList(), "label"),
      parentId: "-1"
    },
    {
      id: oModulesDictionary.PARTNER_ADMIN,
      label: getTranslations().CONFIG_MODULE_PARTNER_ADMINISTRATION,
      className: "tabPartnerAdmin",
      children: CS.sortBy(new aPartnerAdminEntityList(), "label"),
      parentId: "-1"
    },
    {
      id: oModulesDictionary.DATA_INTEGRATION,
      label: getTranslations().DATA_INTEGRATION,
      className: "tabDataIntegration",
      children: CS.sortBy(new aDataIntegrationEntityList(), "label"),
      parentId: "-1"
    },
    {
      id: oModulesDictionary.DATA_GOVERNANCE,
      label: getTranslations().CONFIG_MODULE_DATA_GOVERNANCE,
      className: "tabDataGovernance",
      children: CS.sortBy(new aDataGovernanceEntityList(), "label"),
      parentId: "-1"
    },
    {
      id: oModulesDictionary.COLLABORATION,
      label: getTranslations().CONFIG_MODULE_DATA_COLLABORATION,
      className: "tabCollaboration",
      children: CS.sortBy(new aDataCollaborationEntityList(), "label"),
      parentId: "-1"
    },
    {
      id: oModulesDictionary.TRANSLATION_CONFIGURATION,
      label: getTranslations().LANGUAGE_CONFIGURATION,
      className: "tabTranslationConfiguration",
      children: CS.sortBy(new aTranslationConfigurationList(), "label"),
      parentId: "-1"
    },
    {
      id: oModulesDictionary.ADMIN_CONFIGURATION,
      label: getTranslations().CONFIG_MODULE_ADMIN_CONFIGURATION,
      className: "tabAdminConfiguration",
      children: CS.sortBy(new aAdminConfigurationEntityList(), "label"),
      parentId: "-1"
    },
    {
      id: oModulesDictionary.MONITORING,
      label: getTranslations().MONITORING,
      className: "tabMonitoring",
      children: CS.sortBy(new aMonitoringEntityList(), "label"),
      parentId: "-1"
    }

  ];
};

export default aSettingScreenModulesDictionary;