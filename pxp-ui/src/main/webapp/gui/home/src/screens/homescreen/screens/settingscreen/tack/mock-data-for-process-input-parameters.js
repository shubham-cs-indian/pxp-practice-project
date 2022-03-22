import MockDataForEntityBaseTypesDictionary from '../../../../../commonmodule/tack/mock-data-for-entity-base-types-dictionary';
import { getTranslations as oTranslations } from '../../../../../commonmodule/store/helper/translation-manager.js';
import AttributeTypeDictionary from '../../../../../commonmodule/tack/attribute-type-dictionary-new';
import MappingTypeDictionary from "../../../../../commonmodule/tack/mapping-type-dictionary";

/*******
 * Dev Note:
 * - Only make and instance When translation Needed else make instance at require in order to reduce
 * memory usage
 *
 * - If Request Response Info is absent for List Type Normal MSS will be Displayed For that  propertyDataKey should be
 * mentioned and should be passed to the view via controller or values should be mentioned in items key
 *
 * - for dependentInputParameters  input parameters will be updated by selected value
 *
 *******/
export default function (sCustomElementID) {
  let _getMSSData = function (sLabel, sSelectionContext, sReferencedDataKey, oRequestResponseInfo, sRequestSelectedElementKey) {
    return {
      type: "list",
      label: sLabel,
      selectionContext: sSelectionContext,
      referencedDataKey: sReferencedDataKey,
      requestResponseInfo: oRequestResponseInfo,
      requestSelectedElementKey: sRequestSelectedElementKey
    }
  };

  let _getGroupMSSData = function (sLabel,bIsMultiSelect) {
    let oRequestResponseInfoForUsersList = {
      id: "users",
      label: oTranslations().USERS,
      requestInfo: {
        entities: "users",
        requestType: "configData",
        requestURL: "config/rolesconfigdata",
        responsePath: ["success", "users"],
        paginationData: {
          sortBy: 'firstName',
          searchColumn: 'firstName',
        },
        customRequestModel: {},
        customRequestInfoModel: {}
      }
    };

    let oRequestResponseInfoForRoleList = {
      id: "roles",
      label: oTranslations().ROLES,
      requestInfo: {
        entities: "roles",
        requestType: "configData",
        requestURL: "config/rolesconfigdata",
        responsePath: ["success", "roles"],
        customRequestModel: {},
        customRequestInfoModel: {}
      }
    };

    return {
      type: "groupMss",
      id: "customUserTask",
      label: sLabel,
      isMultiSelect: bIsMultiSelect,
      requestResponseInfoForUsersList: oRequestResponseInfoForUsersList,
      requestResponseInfoForRoleList: oRequestResponseInfoForRoleList,

    }
  };

  let _getRoleMSSData = function (sLabel, sSelectionContext) {
    let oRequestResponseInfo = {
      "requestType": "customType",
      "responsePath": ["success", "usersList"],
      "requestURL": "config/users/grid",
      "customRequestModel": {
        searchColumn: "label",
        sortBy: "userName",
        sortOrder: "asc"
      },
    };
    return _getMSSData(sLabel, sSelectionContext, "referencedUsers", oRequestResponseInfo, "Users")
  }
  let _getTextData = function (sLabel,bIsHidden,sDefaultValue,bIsMultiLine,sId) {
    return {
      id: sId,
      label: sLabel,
      type: "text",
      isHidden: bIsHidden,
      defaultValue: sDefaultValue,
      isMultiLine: bIsMultiLine
    }
  };

  return ({

    isAdvancedOptionsEnabled: {
      label: oTranslations().ADVANCED_OPTIONS,
      type: "checkbox"
    },
    SIDE_1_IID:_getTextData(oTranslations().SIDE_1_IID,false),
    RELATIONSHIP_CODE: _getTextData(oTranslations().RELATIONSHIP_ID),
    SIDE_2_IIDS:_getTextData(oTranslations().SIDE_2_IIDS,false),
    CONTEXT:_getTextData(oTranslations().CONTEXT,false,"",true),
    SOURCE_TYPE: {
      type: "list",
      label: oTranslations().SOURCE_TYPE,
      dependentInputParameters: [],
      items: [{id: "product", label: oTranslations().PRODUCT},
        {id: "asset", label: oTranslations().ASSET}],
      defaultValue: "",
      inputParameterToShowIndicesByValueMap: {},
      viewProperties: {
        cannotRemove: true
      }
    },
    INSTANCE_RELATIONSHIPS: _getTextData(oTranslations().INSTANCE_RELATIONSHIPS),
    SORT_ORDER: {
      type: "list",
      label: oTranslations().SORT_ORDER,
      dependentInputParameters: [],
      items: [{id: "asc", label: oTranslations().ASCENDING}, {
        id: "desc",
        label: oTranslations().DESCENDING
      }],
      defaultValue: "asc",
      inputParameterToShowIndicesByValueMap: {},
      viewProperties: {
        cannotRemove: true
      }
    },

    INPUT_VARIABLES_MAP: {
      type: "variableMap",
      label: oTranslations().ADD_VARIABLE_MAP,
    },

    CLASS_PATH_IP: _getTextData(oTranslations().CLASS_PATH_IP),
    QUEUE_NAME: _getTextData(oTranslations().QUEUE_NAME),
    ACKNOWLEDGEMENT_QUEUE_NAME: _getTextData(oTranslations().ACKNOWLEDGEMENT_QUEUE_NAME),
    JMS_DATA: _getTextData(oTranslations().JMS_DATA),
    PORT: _getTextData(oTranslations().PORT),
    ACKNOWLEDGEMENT: {
      label: oTranslations().ACKNOWLEDGEMENT,
      type: "checkbox",
      defaultValue: "false",
      dependentInputParameters: ["ACKNOWLEDGEMENT_QUEUE_NAME"],
      inputParameterToShowIndicesByValueMap: {
        true: [0],
        false: []
      },
      viewProperties: {
        cannotRemove: true
      }
    },
    ALLOW_SUB_FOLDER_ACCESS: {
      label: oTranslations().ALLOW_SUB_FOLDER_ACCESS,
      type: "checkbox",
      defaultValue: "false",
      dependentInputParameters: [],

      inputParameterToShowIndicesByValueMap: {},
      viewProperties: {
        cannotRemove: true
      }
    },
    FOLDER_PATH: _getTextData(oTranslations().FOLDER_PATH),
    ERROR_PATH: _getTextData(oTranslations().ERROR_PATH),
    ARCHIVAL_PATH: _getTextData(oTranslations().ARCHIVAL_PATH),
    MESSAGE_TYPE_LIST: {
      type: "list",
      label: oTranslations().MESSAGE_TYPE_LIST,
      dependentInputParameters: [],
      items: [{id: "JSON", label: oTranslations().JSON}, {
        id: "EXCEL",
        label: oTranslations().EXCEL
      }],
      defaultValue: "",
      inputParameterToShowIndicesByValueMap: {},
      viewProperties: {
        cannotRemove: false,
      },
      selectionContext: "singleSelectList"
    },
    RECEIVER_MESSAGE_TYPE_LIST: {
      type: "list",
      label: oTranslations().MESSAGE_TYPE_LIST,
      dependentInputParameters: [],
      items: [{id: "JSON", label: oTranslations().JSON}, {
        id: "EXCEL",
        label: oTranslations().EXCEL
      }, {id: "ASSET", label: oTranslations().ASSET}],
      defaultValue: "",
      inputParameterToShowIndicesByValueMap: {},
      viewProperties: {
        cannotRemove: false,
      },
      selectionContext: "singleSelectList",
    },
    INPUT_METHOD: {
      type: "list",
      label: oTranslations().INPUT_METHOD,
      dependentInputParameters: ["HOT_FOLDER_PATH", "ALLOW_SUB_FOLDER_ACCESS", "ARCHIVAL_PATH", "ERROR_PATH", "RECEIVER_MESSAGE_TYPE_LIST"],
      items: [{
        id: "hotFolder",
        label: oTranslations().HOTFOLDER
      }],
      defaultValue: "hotFolder",
      inputParameterToShowIndicesByValueMap: {
        "hotFolder": [0, 1, 2, 3, 4]
      },
      viewProperties: {
        cannotRemove: true
      }
    },
    taskId: _getTextData("", true, "task", false, "taskId"),
    RECEIVED_DATA: _getTextData(oTranslations().RECEIVED_DATA),
    FAILED_FILES: _getTextData(oTranslations().FAILED_FILES),
    EXPORTED_TYPE: _getTextData(oTranslations().EXPORTED_TYPE),
    PXON: _getTextData(oTranslations().PXON),
    EXPORT_MAP: _getTextData(oTranslations().EXPORT_MAP),
    PXON_FILE_PATH: _getTextData(oTranslations().PXON_FILE_PATH),
    TRANSFORMED_FORMAT: {
      type: "list",
      label: oTranslations().TRANSFORMED_FORMAT,
      items: [{id: "JSON", label: oTranslations().JSON}],
      defaultValue: "JSON",
      viewProperties: {
        cannotRemove: true
      }
    },
    TRANSFORMED_DATA: _getTextData(oTranslations().TRANSFORMED_DATA),
    DATA_TO_EXPORT: _getTextData(oTranslations().DATA_TO_EXPORT),
    OUTPUT_METHOD: {
      type: "list",
      label: oTranslations().OUTPUT_METHOD,
      dependentInputParameters: ["CLASS_PATH_IP", "PORT", "QUEUE_NAME", "DELIVERY_TASK_ACKNOWLEDGEMENT", "DESTINATION_PATH", "MESSAGE_TYPE_LIST", "DATA_TO_EXPORT","FILE_PREFIX"],
      items: [{id: "jms", label: oTranslations().JMS}, {
        id: "hotFolder",
        label: oTranslations().HOTFOLDER
      }],
      defaultValue: "",
      inputParameterToShowIndicesByValueMap: {
        "jms": [0, 1, 2, 3, 5, 6],
        "hotFolder": [4, 5, 6, 7]
      },
      viewProperties: {
        cannotRemove: true
      }
    },
    FILE_NAME: _getTextData(oTranslations().FILE_NAME),
    JOB_ID: _getTextData(oTranslations().JOB_ID),
    ASSET_FILES_MAP: _getTextData(oTranslations().ASSET_FILES_MAP),
    RETRY_AHEAD: {
      label: oTranslations().RETRY_AHEAD,
      type: "checkbox",
      defaultValue: "false",
      dependentInputParameters: [],

      inputParameterToShowIndicesByValueMap: {},
      viewProperties: {
        cannotRemove: true
      }
    },
    TRANSACTION_ID_OUTPUT: _getTextData(oTranslations().TRANSACTION_ID_OUTPUT, true, "TRANSACTION_ID"),
    DELIVERY_TASK_ACKNOWLEDGEMENT: {
      label: oTranslations().ACKNOWLEDGEMENT,
      type: "checkbox",
      defaultValue: "false",
      dependentInputParameters: ["ACKNOWLEDGEMENT_QUEUE_NAME", "RETRY_AHEAD"],
      inputParameterToShowIndicesByValueMap: {
        true: [0, 1],
        false: []
      },
      viewProperties: {
        cannotRemove: true
      }
    },
    FILES_TO_BE_STORE: _getTextData(oTranslations().FILES_TO_BE_STORE),
    SUCCESS_FILES: _getTextData(oTranslations().SUCCESS_FILES),
    UNSUCCESS_FILES: _getTextData(oTranslations().UNSUCCESS_FILES),
    IS_RECURSIVE: {
      label: oTranslations().IS_RECURSIVE,
      type: "checkbox",
      defaultValue: "false",
      dependentInputParameters: [],
      inputParameterToShowIndicesByValueMap: {},
      viewProperties: {
        cannotRemove: true
      }
    },
    INPUT_FOLDER_PATH: _getTextData(oTranslations().INPUT_FOLDER_PATH),
    OUTPUT_FOLDER_PATH: _getTextData(oTranslations().OUTPUT_FOLDER_PATH),
    PRIORITY: {
      type: "list",
      label: oTranslations().PRIORITY,
      items: [{id: "undefined", label: oTranslations().UNDEFINED},
        {id: "low", label: oTranslations().LOW},
        {id: "medium", label: oTranslations().MEDIUM},
        {id: "high", label: oTranslations().HIGH},],
      defaultValue: "medium",
      viewProperties: {
        cannotRemove: true
      }
    },
    LANGUAGE_CODE: _getTextData(oTranslations().LANGUAGE_CODE),
    BASE_ENTITY_IIDS: _getTextData(oTranslations().BASE_ENTITY_IIDS),
    COLLECTION_ID: _getTextData(oTranslations().COLLECTION_ID),
    BOOKMARK_ID: _getTextData(oTranslations().BOOKMARK_ID),
    FILE__NAME: _getTextData(oTranslations().FILE_NAME),
    NODE_TYPE: _getTextData(oTranslations().CONFIG_TYPE),
    SEARCH_IN_LIST: _getTextData(oTranslations().INPUT_LIST),
    SEARCH_ITEM: _getTextData(oTranslations().INPUT_ITEM),
    SEARCH_RESULT_INDEX: _getTextData(oTranslations().OUTPUT_LIST_SEARCH_INDEX),
    APPENDED_RESULT_LIST: _getTextData(oTranslations().OUTPUT_LIST_APPENED),
    LIST_TOWHICH_APPEND: _getTextData(oTranslations().INPUT_LIST_TO_WHICH_APPEND),
    LIST_TOAPPENDED: _getTextData(oTranslations().INPUT_LIST_TO_APPENDED),
    INSERT_AT_INDEX: _getTextData(oTranslations().INPUT_POSITION),
    INSERT_INTO_LIST: _getTextData(oTranslations().INPUT_LIST),
    INSERT_ITEM: _getTextData(oTranslations().INPUT_ITEM),
    INSERT_RESULT_LIST: _getTextData(oTranslations().OUTPUT_LIST_INSERT_POSITION),
    EXECUTION_STATUS: _getTextData(oTranslations().EXECUTION_STATUS),
    BOOLEAN_CONDITION: _getTextData(oTranslations().BOOLEAN_CONDITION),
    BOOLEAN_RESULT: _getTextData(oTranslations().BOOLEAN_RESULT),
    EXECUTION_STATUS_MAP: _getTextData(oTranslations().EXECUTION_STATUS_MAP),
    MESSAGE_TYPE: {
      type: "list",
      label: oTranslations().MESSAGE_TYPE_LIST,
      items: [{id: "ERROR", label: oTranslations().ERROR},
        {id: "WARNING", label: oTranslations().WARNING},
        {id: "INFORMATION", label: oTranslations().INFORMATION},
        {id: "SUCCESS", label: oTranslations().SUCCESS}],
      defaultValue: "",
      viewProperties: {
        cannotRemove: true
      },
    },
    INBOUND_MAPPING_ID: _getTextData(oTranslations().MAPPING),
    OUTBOUND_MAPPING_ID: _getTextData(oTranslations().MAPPING),
    LIST_TO_REVERSE: _getTextData(oTranslations().INPUT_LIST),
    RESULT_REVERSED_LIST: _getTextData(oTranslations().OUTPUT_LIST_REVERSE_ALL_ITEMS),
    LIST_TO_SORT: _getTextData(oTranslations().INPUT_LIST),
    RESULT_SORTED_LIST: _getTextData(oTranslations().OUTPUT_LIST),
    LIST_TO_CLEAR: _getTextData(oTranslations().INPUT_LIST),
    COUNT_ITEM: _getTextData(oTranslations().INPUT_SEARCH_ITEM),
    LIST_TO_COUNT_ELEMENT: _getTextData(oTranslations().INPUT_LIST),
    RESULT_LISTCOUNTELEMENT: _getTextData(oTranslations().OUTPUT_LIST_COUNT_ELEMENT),
    APPEND_ITEM: _getTextData(oTranslations().INPUT_ITEM),
    LIST_TO_WHICH_APPEND: _getTextData(oTranslations().INPUT_LIST),
    APPEND_RESULT_LIST: _getTextData(oTranslations().OUTPUT_LIST_APPEND),
    REMOVE_FROM_INDEX: _getTextData(oTranslations().INPUT_POSITION),
    REMOVE_FROM_LIST: _getTextData(oTranslations().INPUT_LIST),
    REMOVED_RESULT_LIST: _getTextData(oTranslations().OUTPUT_LIST),
    REMOVE_ITEM: _getTextData(oTranslations().INPUT_ITEM),
    NOTIFICATION_SUBJECT: _getTextData(oTranslations().SUBJECT),
    NOTIFICATION_BODY: _getTextData(oTranslations().BODY, false, "", true),
    NOTIFICATION: {
      label: oTranslations().NOTIFICATION,
      type: "list",
      defaultValue: "",
      isMultiSelect: true,
      selectionContext: "multiSelectList",
      dependentInputParameters: ["TO_DEFAULT_USER", "RECIPIENTS_TO", "RECIPIENTS_CC", "RECIPIENTS_BCC"],
      items: [{id: "email", label: oTranslations().EMAIL_NOTIFICATION},
        {id: "user", label: oTranslations().USER_NOTIFICATION}],
      inputParameterToShowIndicesByValueMap: {
        "email": [0, 1, 2, 3],
        "user": []
      },
      viewProperties: {
        cannotRemove: true
      }
    },
    CONFIG_PROPERTY_TYPE: _getTextData(oTranslations().CONFIG_PROPERTY_TYPE),
    CONFIG_ITEM_IIDS: _getTextData(oTranslations().CONFIG_ENTITY_IIDS),
    ACTION: {
      type: "list",
      label: oTranslations().ACTION,
      dependentInputParameters: ["SIDE_1_IID", "RELATIONSHIP_CODE", "SIDE_2_IIDS", "CONTEXT", "SOURCE_TYPE"],
      items: [{id: "UPSERT", label: oTranslations().UPSERT},
        {id: "READ", label: oTranslations().READ},
        {id: "DELETE", label: oTranslations().DELETE}],
      defaultValue: "",
      inputParameterToShowIndicesByValueMap: {
        "UPSERT": [0, 1, 2, 3, 4],
        "READ": [0, 1, 2, 4],
        "DELETE": [0, 1, 2, 4]
      },
      viewProperties: {
        cannotRemove: true
      }
    },
    CONFIG_ENTITY_CODES: _getTextData(oTranslations().CONFIG_ENTITY_CODES),
    EXPORT_TYPE: {
      type: "list",
      label: oTranslations().EXPORT_TYPE,
      items: [{id: "product", label: oTranslations().PRODUCT},
        {id: "entity", label: oTranslations().ENTITY}],
      defaultValue: "product",
      dependentInputParameters: ["MANUAL_SEARCH_CRITERIA", "CONFIG_PROPERTY_TYPE", "CONFIG_ENTITY_CODES"],
      inputParameterToShowIndicesByValueMap: {
        "product": [0],
        "entity": [1,2]
      },
      viewProperties: {
        cannotRemove: true
      }
    },
    HOT_FOLDER_PATH: _getTextData(oTranslations().HOT_FOLDER_PATH),
    DESTINATION_PATH: _getTextData(oTranslations().DESTINATION_PATH),
    FILE_PREFIX: _getTextData(oTranslations().FILE_PREFIX),
    NODE: {
      type: "list",
      label: oTranslations().NODE,
      dependentInputParameters: [],
      items: [{id: "ATTRIBUTE", label: oTranslations().ATTRIBUTE},
        {id: "TAG", label: oTranslations().TAG},
        {id: "TAB", label: oTranslations().TAB},
        {id: "PROPERTY_COLLECTION", label: oTranslations().PROPERTY_COLLECTION},
        {id: "HIERARCHY", label: oTranslations().HIERARCHY},
        {id: "MASTERTAXONOMY", label: oTranslations().ATTRIBUTIONTAXONOMY},
        {id: "RELATIONSHIP", label: oTranslations().RELATIONSHIP},
        {id: "PARTNER", label: oTranslations().PARTNER},
        {id: "USER", label: oTranslations().USER},
        {id: "LANGUAGE", label: oTranslations().LANGUAGE},
        {id: "TRANSLATION", label: oTranslations().TRANSLATION},
        {id: "CONTEXT", label: oTranslations().CONTEXT},
        {id: "RULE", label: oTranslations().RULE},
        {id: "ARTICLE", label: oTranslations().ARTICLE},
        {id: "ASSET", label: oTranslations().ASSET},
        {id: "TEXTASSET", label: oTranslations().TEXT_ASSET},
        {id: "SUPPLIER", label: oTranslations().SUPPLIER}
      ],
      defaultValue: "",
      inputParameterToShowIndicesByValueMap: {},
      viewProperties: {
        cannotRemove: true,
      }
    },
    TYPES: _getMSSData(oTranslations().TYPES, "singleSelectList", "referencedTasks", {
      requestType: "configData",
      entityName: "tasks",
      typesToExclude : ["personal"]
    }, "Tasks"),
    ACTION_CONFIG: {
      type: "list",
      label: oTranslations().OPERATION,
      dependentInputParameters: [],
      items: [
        {id: "CREATE", label: oTranslations().CREATE},
        {id: "UPDATE", label: oTranslations().UPDATE}
      ],
      defaultValue: "",
      inputParameterToShowIndicesByValueMap: {},
      viewProperties: {
        cannotRemove: true,
      }
    },
    DATA_TO_IMPORT: _getTextData(oTranslations().DATA_TO_IMPORT, false, "", true),
    PARTNER_AUTHORIZATION_ID: _getTextData(oTranslations().AUTHORIZATION_ID, false, "", false),
    responsible: _getGroupMSSData("RESPONSIBLE",true),
    accountable: _getGroupMSSData("ACCOUNTABLE",false),
    consulted: _getGroupMSSData("CONSULTED",true),
    informed: _getGroupMSSData("INFORMED",true),
    verify: _getGroupMSSData("VERIFIED",false),
    signoff: _getGroupMSSData("SIGN_OFF",false),
    ENTITY_TYPE: {
      type: "list",
      label: oTranslations().ENTITY_TYPE,
      dependentInputParameters: [],
      items: [{id: "product", label: oTranslations().ARTICLE}, {
        id: "asset",
        label: oTranslations().ASSET
      }],
      defaultValue: "",
      inputParameterToShowIndicesByValueMap: {},
      viewProperties: {
        cannotRemove: true
      }
    },
    KLASS_INSTANCE_ID: _getTextData(oTranslations().KLASS_INSTANCE_ID, false, "", false),

    tagId: _getMSSData("", "singleSelectText", "referencedTags", {
      requestType: "configData",
      entityName: "tags",
    }, "TagIdsForComponent"),

    tagValueId: _getMSSData("", "singleSelectText", "referencedTags", {
      requestType: "configData",
      entityName: "tagValues",
    }, "TagIdsForComponent"),

    TAG_VALUES_MAP: {
      type: "tagsMap",
      label: oTranslations().ADD_TAGS,
      referencedDataKey: "referencedTags",
      requestSelectedElementKey: "TagIdsForComponent",
    },
    ATTRIBUTES_VALUES_MAP: {
      type: "attributesValueMap",
      label:oTranslations().ADD_ATTRIBUTES
    },
    ATTRIBUTES_TYPES_MAP: {
      type: "attributesTypeMap",
      label: oTranslations().ATTRIBUTE_GROUP,
      referencedDataKey: "referencedAttributes",
      requestSelectedElementKey: "AttributeIdsForComponent",
    },
    attributeId : _getMSSData("", "singleSelectText", "referencedAttributes", {
      requestType: "configData",
      entityName: "attributes",
      typesToExclude : [AttributeTypeDictionary.CALCULATED,AttributeTypeDictionary.CONCATENATED]
    }, "AttributeIdsForComponent"),
    attributeValue: _getTextData(oTranslations().ATTRIBUTE_VALUE),

    IS_TRIGGERED_THROUGH_SCHEDULER: {
      label: oTranslations().IS_TRIGGERED_THROUGH_SCHEDULER,
      type: "checkbox",
      defaultValue: "false",
      dependentInputParameters: ["DATA_LANGUAGE"],
      inputParameterToShowIndicesByValueMap: {
        true: [0],
        false: []
      },
      viewProperties: {
        cannotRemove: true
      }
    },
    DATA_LANGUAGE: {
      type: "dataLanguage",
      id: "dataLanguage",
    },
    ATTRIBUTES_VALUES_TO_FETCH: _getMSSData(oTranslations().ATTRIBUTES_VALUES_TO_FETCH, "multiSelectList", "referencedAttributes", {
      requestType: "configData",
      entityName: "attributes",
    }, "AttributeIdsForComponent"),
    TAGS_VALUES_TO_FETCH: _getMSSData(oTranslations().TAG_VALUES_TO_FETCH, "multiSelectList", "referencedTags", {
      requestType: "configData",
      entityName: "tags",
    }, "TagIdsForComponent"),
    ATTRIBUTES_AND_TAGS : _getTextData(oTranslations().ATTRIBUTES_AND_TAGS),
    SEARCH_CRITERIA: {
      type: "searchCriteria",
      id: "searchCriteria",
      name: oTranslations().SEARCH_CRITERIA,
    },
    SEARCH_CRITERIA_EDIT: {
      type: "searchCriteriaEdit",
      id: "searchCriteriaEdit",
    },
    EXPORT_SUB_TYPE: {
      type: "list",
      label: oTranslations().EXPORT_SUB_TYPE,
      items: [{id: "EXPORT_ALL", label: oTranslations().EXPORT_ALL},
        {id: "EXPORT_SELECTED", label: oTranslations().EXPORT_SELECTED},
        {id: "TAXONOMY_BASED_EXPORT", label: oTranslations().TAXONOMY_BASED_EXPORT},
        {id: "WITHOUT_TAXONOMY_EXPORT", label: oTranslations().WITHOUT_TAXONOMY_EXPORT},
        {id: "COLLECTION_EXPORT_WITH_CHILD", label: oTranslations().EXPORT_ALL_CURRENT_AND_CHILD_COLLECTIONS},
        {id: "COLLECTION_EXPORT_WITHOUT_CHILD", label: oTranslations().EXPORT_ALL_CURRENT_COLLECTION},
        {id: "BOOKMARK_EXPORT", label: oTranslations().EXPORT_ALL_FROM_CURRENT_BOOKMARK}],
      defaultValue: "",
      dependentInputParameters: ["BASE_ENTITY_IIDS", "SEARCH_CRITERIA_EDIT", "COLLECTION_ID", "BOOKMARK_ID"],
      inputParameterToShowIndicesByValueMap: {
        "EXPORT_SELECTED": [0],
        "TAXONOMY_BASED_EXPORT": [1],
        "COLLECTION_EXPORT_WITH_CHILD": [2],
        "COLLECTION_EXPORT_WITHOUT_CHILD": [2],
        "BOOKMARK_EXPORT": [3]
      },
      viewProperties: {
        cannotRemove: true
      }
    },
    MANUAL_SEARCH_CRITERIA:{
      label: oTranslations().MANUAL_SEARCH_CRITERIA,
      type: "checkbox",
      defaultValue: "false",
      dependentInputParameters: ["SELECTED_BASETYPES","EXPORT_SUB_TYPE", "SEARCH_CRITERIA", "LANGUAGE_CODE"],
      inputParameterToShowIndicesByValueMap: {
        true: [0,1,2,3],
        false: [2,3]
      },
      viewProperties: {
        cannotRemove: true
      }
    },
    SELECTED_BASETYPES: {
      type: "list",
      label: oTranslations().BASE_TYPE,
      items: [{id: "ARTICLE", label: oTranslations().ARTICLE},
        {id: "ASSET", label: oTranslations().ASSET},
        {id: "TARGET", label: oTranslations().TARGET},
        {id: "TEXT_ASSET", label: oTranslations().TEXT_ASSET},
        {id: "SUPPLIER", label: oTranslations().SUPPLIERKLASS}],
      defaultValue: "",
      selectionContext: "multiSelectList",
      dependentInputParameters: [],
      inputParameterToShowIndicesByValueMap: {},
      viewProperties: {
        cannotRemove: false
      }
    },
    INPUT_DATA: _getTextData(oTranslations().INPUT_DATA),
    TALEND_EXECUTABLE: _getMSSData(oTranslations().TALENDJOB, "singleSelectText", "referencedTalendJobs", {
      requestType: "customType",
      requestURL: "config/dataintegration/alltalendjobs",
      responsePath: ["success"],
      "customRequestModel": {}
    }, "TalendJobs"),
    OUTPUT_DATA: _getTextData(oTranslations().OUTPUT_DATA),
    DESTINATION_ENDPOINT_ID: _getTextData(oTranslations().DESTINATION_ENDPOINT_ID),
    SUCCESS_IIDS: _getTextData(oTranslations().SUCCESS_IIDS),
    FAILED_IIDS: _getTextData(oTranslations().FAILED_IIDS),
    DESTINATION_ORGANIZATION_ID: _getMSSData(oTranslations().DESTINATION_ORGANIZATION_ID, "singleSelectText", "referencedDestinationOrganizations", {
      requestType: "customType",
      requestURL: "config/organizations/getall",
      responsePath: ["success", "list"]
    }, "DestinationOrganizations"),
    DESTINATION_CATALOG_ID: {
      type: "list",
      label: oTranslations().DESTINATION_CATALOG_ID,
      dependentInputParameters: ["DESTINATION_ENDPOINT_ID"],
      items: [{id: "pim", label: oTranslations().PIM},
        {id: "onboarding", label: oTranslations().ONBOARDING},
        {id: "offboarding", label: oTranslations().OFFBOARDING},
        {id: "dataIntegration", label: oTranslations().DATA_INTEGRATION}],
      defaultValue: "",
      inputParameterToShowIndicesByValueMap: {
        "dataIntegration": [0]
      },
      viewProperties: {
        cannotRemove: false
      }
    },
    IS_REVISIONABLE_TRANSFER: {
      label: oTranslations().CREATE_REVISIONS,
      type: "checkbox",
      defaultValue: "false",
      dependentInputParameters: [],
      inputParameterToShowIndicesByValueMap: {},
      viewProperties: {
        cannotRemove: true
      }
    },
    IS_JMS_SERVICE_AVAILABLE : _getTextData(oTranslations().IS_JMS_SERVICE_AVAILABLE),
    EXPORTED_FILE_PATH : _getTextData(oTranslations().EXPORTED_FILE_PATH),
    IS_CONFIG_TYPE_PERMISSION: {
      label: oTranslations().IS_CONFIG_TYPE +" "+ oTranslations().PERMISSIONS,
      type: "checkbox",
      defaultValue: "false",
      dependentInputParameters: ["PERMISSIONS_TYPE","ROLE_IDS","NODE_TYPE"],
      inputParameterToShowIndicesByValueMap: {
        true: [2,0,1],
        false: [2]
      },
      viewProperties: {
        cannotRemove: true
      }
    },
    PERMISSIONS_TYPE: _getTextData(oTranslations().PERMISSION_TYPE),
    ROLE_IDS: _getTextData(oTranslations().ROLE_IDS),
    TO_DEFAULT_USER: {
      label: oTranslations().TO_DEFAULT_USER,
      type: "checkbox",
      defaultValue: "true",
      dependentInputParameters: [],
      inputParameterToShowIndicesByValueMap: {},
      viewProperties: {
        cannotRemove: true
      }
    },
    RECIPIENTS_TO: {
      type: "rowList",
      selectionContext: "multiSelectList",
      label: oTranslations().RECIPIENTS_TO,
      tooltip : {
        add : oTranslations().ADD_TO,
        remove : oTranslations().REMOVE_TO
      }
    },
    RECIPIENTS_CC: {
      type: "rowList",
      selectionContext: "multiSelectList",
      label: oTranslations().CC,
      tooltip : {
        add : oTranslations().ADD_CC,
        remove : oTranslations().REMOVE_CC
      }
    },
    RECIPIENTS_BCC: {
      type: "rowList",
      selectionContext: "multiSelectList",
      label: oTranslations().BCC,
      tooltip : {
        add : oTranslations().ADD_BCC,
        remove : oTranslations().REMOVE_BCC
      }
    },
    BASE_ENTITY_IDS: _getTextData(oTranslations().BASE_ENTITY_IDS),
    METADATA_VALUES_TO_FETCH: {
      type: "list",
      label: oTranslations().METADATA_VALUES_TO_FETCH,
      dependentInputParameters: [],
      items: [{id: "sourceOrganization", label: oTranslations().SOURCE_ORGANISATION}],
      defaultValue: "",
      selectionContext: "multiSelectList",
      inputParameterToShowIndicesByValueMap: {},
      viewProperties: {
        cannotRemove: false
      }
    },
  })
};
