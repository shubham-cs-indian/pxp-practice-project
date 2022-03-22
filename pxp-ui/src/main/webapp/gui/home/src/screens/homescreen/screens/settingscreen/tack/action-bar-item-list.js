/**
 * Created by DEV on 27-07-2015.
 */
import oClassCategoryConstants from './class-category-constants-dictionary';

import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';

const actionBarItemList = function () {
  return {
    SettingScreenTools: [
      /*{
        id: 'cancel',
        className: 'actionItemCancel',
        title: 'CANCEL_MENU_ITEM_TITLE'
      },
      {
        id: 'save',
        className: 'actionItemSave',
        title: 'SAVE'
      }*/

    ],

    SettingScreens: [
      {
        id: 'tags_configuration',
        className: 'actionItemTag',
        title: 'TAG_MENU_ITEM_TITLE'
      },
      {
        id: 'context_configuration',
        className: 'actionItemContext',
        title: 'CONTEXTS'
      },
      {
        id: 'tasks_configuration',
        className: 'actionItemTask',
        title: 'TASKS'
      },
      {
        id: 'propertycollections_configuration',
        className: 'actionItemPropertyCollection',
        title: 'PROPERTY_COLLECTIONS'
      },
      /*{
        id: 'classes_configuration',
        className: 'actionItemClass',
        title: 'CLASS_MENU_ITEM_TITLE'
      },*/
      {
        id: 'classesclone_configuration',
        className: 'actionItemClass_clone',
        title: 'CLASSES'
      },
      {
        id: 'template_configuration',
        className: 'actionItemTemplate',
        title: 'TEMPLATES'
      },
      {
        id: 'permissions_configuration',
        className: 'actionItemPermission',
        title: 'PERMISSIONS_MENU_ITEM_TITLE'
      },
      {
        id: 'relationships_configuration',
        className: 'actionItemRelationship',
        title: 'RELATIONSHIPS'
      },
      {
        id: 'users_configuration',
        className: 'actionItemUser',
        title: 'USERS'
      },
      {
        id: 'roles_configuration',
        className: 'actionItemRole',
        title: 'ROLE_MENU_ITEM_TITLE'
      },
      {
        id: 'profile_configuration',
        className: 'actionItemProfile',
        title: 'ENDPOINTS'
      },
      {
        id: 'rules_configuration',
        className: 'actionItemRule',
        title: 'RULES'
      },
      {
        id: 'ruleList_configuration',
        className: 'actionItemRuleList',
        title: 'RULE_LIST_MENU_ITEM_TITLE'
      },
      {
        id: 'process_configuration',
        className: 'actionItemProcess',
        title: 'PROCESS'
      },
      {
        id: 'mapping_configuration',
        className: 'actionItemMapping',
        title: 'MAPPING'
      },
      {
        id: 'attributionTaxonomy_configuration',
        className: 'actionItemAttributionTaxonomy',
        title: 'MASTER_TAXONOMIES'
      },
      {
        id: 'organisation_configuration',
        className: 'actionItemOrganisationConfig',
        title: 'PARTNERS'
      },
      {
        id: 'translations_configuration',
        className: 'actionItemTranslations',
        title: 'TRANSLATIONS'
      },
      {
        id: 'kpi_configuration',
        className: 'actionItemKpi',
        title: 'KPI_MENU_ITEM_TILE'
      },
      {
        id: 'dataGovernanceTasks_configuration',
        className: 'actionItemDataGovernanceTasks',
        title: 'DATA_GOVERNANCE_TASKS_MENU_ITEM_TILE'
      },
      {
        id: 'tabs_configuration',
        className: 'actionItemTabs',
        title: 'TABS_MENU_ITEM_TILE'
      },
    ],

    ContextConfigView: {
      ContextConfigListView: [
        {
          id: 'refresh_context',
          className: 'actionItemRefresh',
          title: 'REFRESH'
        },
        {
          id: 'delete_context',
          className: 'actionItemDelete',
          title: 'DELETE'
        },
        /*{
          id: 'save',
          className: 'actionItemSave',
          title: 'SAVE'
        },*/
        {
          id: 'create_context',
          className: 'actionItemCreate',
          title: 'CREATE'
        }
      ]
    },

    TasksConfigView: {
      TasksConfigListView: [
        {
          id: 'refresh_task',
          className: 'actionItemRefresh',
          title: 'TASKS_REFRESH_MENU_ITEM_TITLE'
        },
        {
          id: 'delete_task',
          className: 'actionItemDelete',
          title: 'TASKS_DELETE_MENU_ITEM_TITLE'
        },
        {
          id: 'save',
          className: 'actionItemSave',
          title: 'SAVE'
        },
        {
          id: 'create_task',
          className: 'actionItemCreate',
          title: 'TASKS_CREATE_MENU_ITEM_TITLE'
        }
      ]
    },

    DataGovernanceTasksConfigView: {
      DataGovernanceTasksConfigListView: [
        {
          id: 'refresh_dataGovernanceTask',
          className: 'actionItemRefresh',
          title: 'DATA_GOVERNANCE_TASKS_REFRESH_MENU_ITEM_TITLE'
        },
        {
          id: 'delete_dataGovernanceTask',
          className: 'actionItemDelete',
          title: 'DATA_GOVERNANCE_TASKS_DELETE_MENU_ITEM_TITLE'
        },
        {
          id: 'save',
          className: 'actionItemSave',
          title: 'SAVE'
        },
        {
          id: 'create_dataGovernanceTask',
          className: 'actionItemCreate',
          title: 'DATA_GOVERNANCE_TASKS_CREATE_MENU_ITEM_TITLE'
        }
      ]
    },


    ClassConfigView: {
      ClassConfigCategoryView: [
        {
          id: oClassCategoryConstants.ARTICLE_CLASS,
          className: 'actionItemContentClasses',
          title: 'CLASS_CATEGORY_CONTENT_TITLE'
        },
        /*{
          id: oClassCategoryConstants.TASK_CLASS,
          className: 'actionItemTaskClasses',
          title: 'TASK'
        },*/
        {
          id: oClassCategoryConstants.ASSET_CLASS,
          className: 'actionItemAssetClasses',
          title: 'ASSET'
        },
        {
          id: oClassCategoryConstants.TARGET_CLASS,
          className: 'actionItemTargetClasses',
          title: 'MARKET'
        },
        {
          id: oClassCategoryConstants.TEXTASSET_CLASS,
          className: 'actionItemTextAssetClasses',
          title: 'TEXT_ASSET'
        },
        {
          id: oClassCategoryConstants.SUPPLIER_CLASS,
          className: 'actionItemSupplierClasses',
          title: 'SUPPLIER'
        }, /*,
      {
        id: oClassCategoryConstants.EDITORIAL_CLASS,
        className: 'actionItemEditorialClasses',
        title: 'CLASS_CATEGORY_EDITORIAL_CONTENT_TITLE'
      }*/

      ],

      ClassConfigListView: [
     /*   {
          id: 'refresh_class',
          className: 'actionItemRefresh',
          title: 'CLASS_REFRESH_MENU_ITEM_TITLE'
        },
        {
          id: 'delete_class',
          className: 'actionItemDelete',
          title: 'CLASS_DELETE_MENU_ITEM_TITLE'
        },
        {
          id: 'save',
          className: 'actionItemSave',
          title: 'SAVE'
        },
        {
          id: 'create_class',
          className: 'actionItemCreate',
          title: 'CLASS_CREATE_MENU_ITEM_TITLE'
        }*/
        {
          id: 'import_class',
          className: 'actionItemImport',
          title: 'IMPORT'
        },
        {
          id: 'export_class',
          className: 'actionItemExport',
          title: 'EXPORT'
        },
        {
          id: "manage_entity_class",
          className: "manageEntity",
          title: "MANAGE_ENTITY_USAGE",
        }
      ],

      ClassConfigDetailedView: [
        /*{
         id: 'cancel_class',
         className: 'actionItemCancel',
         title: 'CLASS_CANCEL_MENU_ITEM_TITLE'
         },
         {
         id: 'save_class',
         className: 'actionItemSave',
         title: 'CLASS_SAVE'
         }*/
      ],
      ClassConfigRelationshipListView: [
        /* {
         id: 'delete_relationship_class',
         className: 'actionItemDelete',
         title: 'CLASS_DELETE_RELATIONSHIP_MENU_ITEM'
         },
         {
         id: 'create_relationship_class',
         className: 'actionItemCreate',
         title: 'CLASS_CREATE_RELATIONSHIP_MENU_ITEM'
         }*/
      ],
      ClassConfigRelationshipDetailView: []
    },

    PropertyCollectionConfigView: {
      PropertyCollectionConfigListView: [
        {
          id: 'import_propertyCollection',
          className: 'actionItemImport',
          title: 'IMPORT'
        },
        {
          id: 'export_propertyCollection',
          className: 'actionItemExport',
          title: 'EXPORT'
        },
        {
          id: 'refresh_propertycollection',
          className: 'actionItemRefresh',
          title: 'REFRESH'
        },
        {
          id: 'delete_propertycollection',
          className: 'actionItemDelete',
          title: 'DELETE'
        },
        /*{
          id: 'save',
          className: 'actionItemSave',
          title: 'SAVE'
        },*/
        {
          id: 'create_xraycollection',
          className: 'actionItemCreateXRay',
          title: 'PROPERTY_COLLECTION_CREATE_XRAY_MENU_ITEM_TITLE'
        },
        {
          id: "manage_entity_propertyCollection",
          className: "manageEntity",
          title: "MANAGE_ENTITY_USAGE",
        },
        {
          id: 'create_propertycollection',
          className: 'actionItemCreate',
          title: 'CREATE'
        }
      ],
    },

    TemplateConfigView: {
      TemplateConfigListView: [
        {
          id: 'refresh_template',
          className: 'actionItemRefresh',
          title: 'REFRESH'
        },
        {
          id: 'delete_template',
          className: 'actionItemDelete',
          title: 'DELETE'
        },
        /*{
          id: 'save',
          className: 'actionItemSave',
          title: 'SAVE'
        },*/
        {
          id: 'create_template',
          className: 'actionItemCreate',
          title: 'CREATE'
        }
      ]
    },

    ProcessConfigView: {
      ProcessConfigListView: [
        {
          id: 'refresh_process',
          className: 'actionItemRefresh',
          title: 'REFRESH'
        },
        {
          id: 'delete_process',
          className: 'actionItemDelete',
          title: 'WORKFLOW_DELETE_MENU_ITEM_TITLE'
        },
        /*{
          id: 'save',
          className: 'actionItemSave',
          title: 'SAVE'
        },*/
        {
          id: 'create_process',
          className: 'actionItemCreate',
          title: 'WORKFLOW_CREATE_MENU_ITEM_TITLE'
        },
        {
          id: 'copy_process',
          className: 'actionItemSaveAs',
          title: 'COPY'
        }
      ],

      ProcessConfigDetailedView: []
    },

    RelationshipConfigView: {
      RelationshipConfigListView: [
        {
          id: 'refresh_relationship',
          className: 'actionItemRefresh',
          title: 'REFRESH'
        },
        {
          id: 'delete_relationship',
          className: 'actionItemDelete',
          title: 'RELATIONSHIP_DELETE_MENU_ITEM_TITLE'
        },
        /*{
          id: 'save',
          className: 'actionItemSave',
          title: 'SAVE'
        },*/
        {
          id: 'create_relationship',
          className: 'actionItemCreate',
          title: 'RELATIONSHIP_CREATE_MENU_ITEM_TITLE'
        }
      ],

      RelationshipConfigDetailedView: [
        /*{
         id: 'cancel_relationship',
         className: 'actionItemCancel',
         title: 'CANCEL_CHANGES'
         },
         {
         id: 'save_relationship',
         className: 'actionItemSave',
         title: 'RELATIONSHIP_SAVE'
         }*/

      ]
    },

    UserConfigView: {
      UserConfigListView: [
        {
          id: 'refresh_user',
          className: 'actionItemRefresh',
          title: 'REFRESH'
        },
        {
          id: 'delete_user',
          className: 'actionItemDelete',
          title: 'USER_DELETE_MENU_ITEM_TITLE'
        },
        {
          id: 'save',
          className: 'actionItemSave',
          title: 'SAVE'
        },
        {
          id: 'create_user',
          className: 'actionItemCreate',
          title: 'USER_CREATE_MENU_ITEM_TITLE'
        }
      ]
    },

    RoleConfigView: {
      RoleConfigListView: [
        {
          id: 'refresh_role',
          className: 'actionItemRefresh',
          title: 'REFRESH'
        },
        {
          id: 'delete_role',
          className: 'actionItemDelete',
          title: 'ROLE_DELETE_MENU_ITEM_TITLE'
        },
        {
          id: 'save',
          className: 'actionItemSave',
          title: 'SAVE'
        },
        {
          id: 'create_role',
          className: 'actionItemCreate',
          title: 'ROLE_CREATE_MENU_ITEM_TITLE'
        }
      ]
    },

    ProfileConfigView: {
      ProfileConfigListView: [
        {
          id: 'refresh_profile',
          className: 'actionItemRefresh',
          title: 'REFRESH'
        },
        {
          id: 'delete_profile',
          className: 'actionItemDelete',
          title: 'DELETE'
        },
        {
          id: 'save',
          className: 'actionItemSave',
          title: 'SAVE'
        },
        {
          id: 'create_profile',
          className: 'actionItemCreate',
          title: 'CREATE'
        }
      ]
    },

    MappingConfigView: {
      MappingConfigListView: [
        {
          id: 'refresh_mapping',
          className: 'actionItemRefresh',
          title: 'REFRESH'
        },
        {
          id: 'delete_mapping',
          className: 'actionItemDelete',
          title: 'DELETE'
        },
        /*{
          id: 'save',
          className: 'actionItemSave',
          title: 'SAVE'
        },*/
        {
          id: 'create_mapping',
          className: 'actionItemCreate',
          title: 'CREATE'
        },
        {
          id: 'copy_process',
          className: 'actionItemSaveAs',
          title: 'COPY'
        }
      ]
    },

    PermissionConfigView: {
      PermissionConfigListView: [
        {
          id: 'refresh_permission',
          className: 'actionItemRefresh',
          title: 'REFRESH'
        },
        {
          id: 'save',
          className: 'actionItemSave',
          title: 'SAVE'
        }
      ]
    },

    RuleConfigView: {
      RuleConfigListView: [
        {
          id: 'refresh_rule',
          className: 'actionItemRefresh',
          title: 'REFRESH'
        },
        {
          id: 'delete_rule',
          className: 'actionItemDelete',
          title: 'DELETE'
        },
        /*{
          id: 'save',
          className: 'actionItemSave',
          title: 'SAVE'
        },*/
        {
          id: 'create_rule',
          className: 'actionItemCreate',
          title: 'CREATE'
        }
      ]
    },

    GoldenRecordsConfigView: {
      GoldenRecordConfigListView: [
        {
          id: 'refresh_goldenRecordRule',
          className: 'actionItemRefresh',
          title: 'REFRESH'
        },
        {
          id: 'delete_goldenRecordRule',
          className: 'actionItemDelete',
          title: 'DELETE'
        },
        /*{
          id: 'save',
          className: 'actionItemSave',
          title: 'SAVE'
        },*/
        {
          id: 'create_goldenRecordRule',
          className: 'actionItemCreate',
          title: 'CREATE'
        }
      ]
    },

    KpiConfigView: {
      KpiConfigListView: [
        {
          id: 'refresh_kpi',
          className: 'actionItemRefresh',
          title: 'REFRESH'
        },
        {
          id: 'delete_kpi',
          className: 'actionItemDelete',
          title: 'DELETE'
        },
        {
          id: 'save',
          className: 'actionItemSave',
          title: 'SAVE'
        },
        {
          id: 'create_kpi',
          className: 'actionItemCreate',
          title: 'CREATE'
        }
      ]
    },

    RuleListConfigView: {
      RuleListConfigListView: [
        {
          id: 'refresh_rule_list',
          className: 'actionItemRefresh',
          title: 'REFRESH'
        },
        {
          id: 'delete_rule_list',
          className: 'actionItemDelete',
          title: 'DELETE'
        },
        {
          id: "manage_entity_rule_list",
          className: "manageEntity",
          title: "MANAGE_ENTITY_USAGE"
        },
        /*{
          id: 'save',
          className: 'actionItemSave',
          title: 'SAVE'
        },*/
        {
          id: 'create_rule_list',
          className: 'actionItemCreate',
          title: 'CREATE'
        }
      ]
    },

    AdminConfigurationConfigView: {
      ThemeConfigurationConfigView: [
        {
          id: 'reset_theme_configuration',
          className: 'actionItemReset',
          title: 'RESTORE_DEFAULT_CONFIGURATION'
        },
      ],
      ViewConfigurationConfigView: [
        {
          id: 'reset_view_configuration',
          className: 'actionItemReset',
          title: 'RESTORE_DEFAULT_CONFIGURATION'
        },
      ],
      IconLibraryConfigView: [
        {
          id: "upload_icon_library",
          className: "actionIconUpload",
          title: "UPLOAD"
        },
        {
          id: 'refresh_icon_library',
          className: 'actionItemRefresh',
          title: 'REFRESH'
        }
      ]
    },

    OrganisationConfigView: {
      OrganisationConfigListView: [
        {
          id: 'import_organisation',
          className: 'actionItemImport',
          title: 'IMPORT'
        },
        {
          id: 'export_organisation',
          className: 'actionItemExport',
          title: 'EXPORT'
        },
        {
          id: 'refresh_organisation_config',
          className: 'actionItemRefresh',
          title: 'REFRESH'
        },
        /*{
          id: 'save',
          className: 'actionItemSave',
          title: 'SAVE'
        },*/
        {
          id: "manage_entity_partners",
          className: "manageEntity",
          title: "MANAGE_ENTITY_USAGE"
        },
        {
          id: 'create_organisation_config',
          className: 'actionItemCreate',
          title: 'CREATE'
        }
      ]
    },

    AttributionTaxonomyConfigView: {
      AttributionTaxonomyConfigListView: [
        {
          id: 'refresh_attribution_taxonomy_config',
          className: 'actionItemRefresh',
          title: 'REFRESH'
        },
        {
          id: 'import_attribution_taxonomy',
          className: 'actionItemImport',
          title: 'IMPORT'
        },
        {
          id: 'export_attribution_taxonomy',
          className: 'actionItemExport',
          title: 'EXPORT'
        },
        {
          id: "manage_entity_taxonomy",
          className: "manageEntity",
          title: "MANAGE_ENTITY_USAGE",
        }
      ]
    },

    LanguageTreeConfigView: {
      LanguageTreeConfigListView: [
        {
          id: 'import_language_tree',
          className: 'actionItemImport',
          title: 'IMPORT'
        },
        {
          id: 'export_language_tree',
          className: 'actionItemExport',
          title: 'EXPORT'
        },
        {
          id: "manage_entity_language",
          className: "manageEntity",
          title: "MANAGE_ENTITY_USAGE"
        }
      ]
    }

  };
};

export default actionBarItemList;
