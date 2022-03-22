import CommonModuleRequestMapping from './../../../../../commonmodule/tack/common-module-request-mapping';

var oUploadRequestMapping = {
  'GetAssetImage': 'asset/<%=type%>/<%=id%>',
  'GetAssetStatus': 'asset/<%=type%>/<%=id%>',
  'UploadImage': 'assets/upload/',
  'GetAllAssetExtensions': "config/getExtensions",
  'UploadIconsToServer': "config/icons/upload",
  'SaveEditedIconElement': "config/icons/save",
};

const oVariantConfigurationRequestMapping = {
  'SaveVariantConfigurations':'config/variantconfigurations',
  'GetVariantConfigurations':'config/variantconfigurations',
};

var oTagRequestMapping = {
  'BulkGetTag': 'config/tags/grid',
  'GetTag': 'config/tags/<%=id%>?mode=<%=mode%>',
  'SaveTag': 'config/tags',
  'CreateTag': 'config/tags',
  'BulkCreateTag': 'config/tags/bulk',
  'BulkDelete': 'config/tags',
  'GetAllTagTypes': 'config/tagTypes',
  'GetTagsByType': 'config/tagListByTagType/<%=type%>',
  'AllowedMasterTags': 'config/allowedmastertags',
  'CheckEntityCode': 'config/checkentitycode',
  'GetMasterTags': 'config/mastertags',
  'AllowedMasterTagsForHierarchyConfig': 'config/allowedmastertags/articletaxonomy',
  'importTag': 'config/import/tag',
  'exportTag': 'config/export/tag'
};

var oKpiRequestMapping = {
  'GetAllKpi': 'config/keyperformanceindex/getall',
  'BulkDelete': 'config/keyperformanceindex',
  'CreateKpi': 'config/keyperformanceindex',
  'GetTaxonomy': 'config/allchildmajortaxonomies',
  'GetKPI': 'config/keyperformanceindex/<%=id%>',
  'SaveKPI':'config/keyperformanceindex',
  'CheckEntityCode': 'config/checkentitycode',
  'SaveBulkKPI':'config/bulksave/kpirules',
};

let oOrganisationConfigRequestMapping = {
  'GetAll':'config/organizations/getall',
  'Get':'config/organizations/<%=id%>',
  'Save':'config/organizations',
  'Delete':'config/organizations',
  'Create':'config/organizations',
  'GetSystems': 'config/systems/get', //POST request
  'CheckEntityCode': 'config/checkentitycode',
  'GetSystemsForOrganization': 'config/selectedsystemsinorganization/<%=id%>',
  'ImportPartner': 'config/import/partner',
  'ExportPartner': 'config/export/partner'
};

let oSSOSettingConfigRequestMapping = {
  "Create": "config/sso",
  "CheckEntityCode": 'config/checkentitycode',
  "BulkDelete": "config/sso",
  "Save": "config/sso",
  "BulkGetSSO": "config/sso/grid",
  "GetIDPConfiguration": "config/idpConfiguration"
};

let oTabsRequestMapping = {
  'GetAll': 'config/tabs',
  'Get':'config/tab/<%=id%>',
  'BulkSave': 'config/tabs/bulksave',
  'Create': 'config/tab',
  'BulkDelete': 'config/tab',
  'SaveShuffledSequence':'config/tab/save',
  'ImportTab': 'config/import/tab',
  'ExportTab': 'config/export/tab'
};

var oContextRequestMapping = {
  'getAll': 'config/variantcontexts/getall',
  'get': 'config/variantcontexts/<%=id%>',
  'save': 'config/variantcontexts',
  'create': 'config/variantcontexts',
  'delete': 'config/variantcontexts',
  'CheckEntityCode': 'config/checkentitycode',
  'BulkSave': 'config/bulksavevariantcontexts',
  'ImportContext': 'config/import/contexts',
  'ExportContext': 'config/export/contexts'
};

var oAttributeRequestMapping = {
  'getAll': 'config/attributes?mode=<%=mode%>',
  'get': 'config/attributes/<%=id%>',
  'save': 'config/attributes',
  'create': 'config/attributes',
  'bulkDelete': 'config/attributes',
  'bulkGet' : 'config/attributes/grid',
  'getCalculatedAttributesMapping': 'config/mapping/calculatedattribute',
  'getAllowedAttributesForCalculatedAttribute': 'config/allowedattributesforcalculatedattribute',
  'getBasic': 'config/attributelist?mode=all',
  'CheckEntityCode': 'config/checkentitycode',
  'importAttribute': 'config/import/attribute',
  'exportAttribute': 'config/export/attribute'
};

var oTasksRequestMapping = {
  'GetAll': 'config/tasks/getall',
  'CreateTask': 'config/tasks',
  'DeleteTask': 'config/tasks',
  'GetTask': 'config/tasks/<%=id%>',
  'SaveTask': 'config/tasks',
  'CheckEntityCode': 'config/checkentitycode',
  'bulkGet'  : 'config/tasks/grid',
  'ImportTask':'config/import/task',
  'ExportTask':'config/export/task'
};

const oDataGovernanceTasksRequestMapping = {
  'GetAll': 'config/governancetasks/getall',
  'CreateTask': 'config/governancetasks',
  'DeleteTask': 'config/governancetasks',
  'GetTask': 'config/governancetasks/<%=id%>',
  'SaveTask': 'config/governancetasks',
  'CheckEntityCode': 'config/checkentitycode',
  'Grid': 'config/governancetasks/grid'
};

var oRoleRequestMapping = {
  'getAll': 'config/roles',
  'get': 'config/roles/<%=id%>',
  'save': 'config/roles',
  'bulkDelete': 'config/roles',
  'create': 'config/roles',
  'CheckEntityCode': 'config/checkentitycode',
  'GetAllowedRoleEntities': 'config/getallowedtargetsforroles',
  'GetAllowedUsers': 'config/getallowedusers',
  'GetAllowedEndpoints': 'config/organization/endpoint/<%=id%>',
  'cloneRole': 'config/clonerole'
};

var oUserRequestMapping = {
  'getAll': 'config/users',
  'get': 'config/users/<%=id%>',
  'save': 'config/users',
  'bulkDelete': 'config/users',
  'create': 'config/users',
  'ResetPassword': 'config/users/resetpassword',
  'checkUserAvailability': 'config/checkUsers',
  'CheckEntityCode': 'config/checkentitycode',
  'Grid': 'config/users/grid',
  'getUsersByRole': 'config/usersbyrole/<%=id%>',
  'ExportUser': 'config/export/user',
  'ImportUser': 'config/import/user',
};

var oProfileRequestMapping = {
  'GetAllProfiles': 'config/endpoints',
  'GetProfile': 'config/endpoints/<%=id%>',
  'SaveProfile': 'config/endpoints',
  'DeleteProfile': 'config/endpoints',
  'CreateProfile': 'config/endpoints',
  'CheckEntityCode': 'config/checkentitycode',
  'GetSystems': 'config/systems/get', //POST request
  'CreateSystem': 'config/system', //PUT request
  'Grid': 'config/endpoints/grid',
  'SaveBulkEndpoints': 'config/bulk/endpoints',
  'SaveAs': 'config/endpoints/clone',
  'BulkCodeCheck': 'config/bulkcheckentitycode',
};

var oMappingRequestMapping = {
  'GetAllMappings': 'config/mappings/getall',
  'GetMapping': 'config/mappings/<%=id%>',
  'SaveMapping': 'config/mappings',
  'DeleteMapping': 'config/mappings',
  'CreateMapping': 'config/mappings',
  'CheckEntityCode': 'config/checkentitycode',
  'BulkSave': 'config/bulksavemappings',
  'SaveAs': 'config/mappings/clone',
  'BulkCodeCheck': 'config/bulkcheckentitycode',
  'GetConfigData': 'config/configdata',
  'CreateOutBoundMapping': 'config/outBoundMapping',
  'SaveOutBoundMapping': 'config/outBoundMapping',
  'GetOutBoundMapping': 'config/getOutBoundMapping',
  'GetAttributeTagInfoForKlass' : 'config/outboundMapping/getPCInfo',
  'SaveInboundMapping': 'config/getMappings',

};

var oAuthorizationRequestMapping = {
  'GetAllAuthorizationMappings': 'config/authorizationmappings/getall',
  'DeleteAuthorizationMapping': 'config/authorizationmappings',
  'CreateAuthorizationMapping': 'config/authorizationmappings',
  'CheckEntityCode': 'config/checkentitycode',
  'SaveAuthorizationMapping': 'config/authorizationmappings',
  'GetAuthorizationMapping': 'config/authorizationmappings/<%=id%>',
  'BulkSaveAuthorizationMapping': 'config/bulk/authorizationmappings',
};

var oEndpointRequestMapping = {
  'CreateEndpoint': 'config/endpoints',
  'GetBulkEndpoints': 'config/bulk/endpoints',
  'SaveBulkEndpoints': 'config/bulk/endpoints',
  'CheckEntityCode': 'config/checkentitycode'
};

var oPropertyCollectionRequestMapping = {
  'GetAllPropertyCollections': 'config/propertycollections/getall?isForXRay=<%=isForXRay%>',
  'CreatePropertyCollection': 'config/propertycollections',
  'GetPropertyCollection': 'config/propertycollections/<%=id%>',
  'DeletePropertyCollections': 'config/propertycollections',
  'SavePropertyCollection': 'config/propertycollections',
  'CheckEntityCode': 'config/checkentitycode',
  'ImportPropertyCollection': 'config/import/propertycollection',
  'ExportPropertyCollection': 'config/export/propertycollection'
};

var oClassRequestMapping = {
  'SaveClass': 'config/klasses',
  'SaveImpactedClass': 'config/klasses',
  "GetAllStandardProperties": 'config/attributes/standard',
  'GetClass': 'config/klassWithGlobalPermission/<%=id%>',
  'GetClassWithoutPC': CommonModuleRequestMapping.GetArticleWithoutPC,
  'GetClassPropertyCollection': CommonModuleRequestMapping.GetClassPropertyCollection,
  "DeleteClasses": 'config/klasses',
  'GetAllClass': 'config/klasses',
  'CreateClass': 'config/klasses',
  'GetAllClasses': 'config/klassesForMapping',
  "GetViewSettings": 'home/src/screens/homescreen/screens/settingscreen/tack/mock/mock-data-for-class-view-setting.json',
  'GetAllKlassessByCategory': 'config/klassesByCategory/<%=id%>',
  'GetClassTree': 'runtime/allklassestree',
  'GetKlassListByNatureType' : 'config/klassesListByNatureType/<%=id%>',
  'GetLazyKlassListByNatureType' : 'config/klassesListByNatureType',
  'GetLazyKlassListByType' : 'config/klassesListByTypes',
  'GetKlassesListByBaseType' : 'config/klasseslistbybasetype',
  'GetTag': 'config/tags/<%=id%>?mode=<%=mode%>',
  'CheckEntityCode': 'config/checkentitycode',
  'GetTaxonomy': 'config/immediatechildmajortaxonomies',
  'ExportClass': 'config/export/klass/article',
  'ImportClass': 'config/import/klass/article',
  "getRelationshipInheritance": "config/relationships/inheritance",

  'GetAssetParentId': 'config/assets/getParentId',
  'GetSectionConfigDataForRelationshipExport': 'config/getconfigdataforrelationshipexport',
  'GetSectionInfoForRelationshipExport': 'config/getsectioninfoforrelationshipexport'
};

var oTemplateRequestMapping = {
  'GetAllTemplates': 'config/templates',
  'GetTemplate': 'config/templates/<%=id%>',
  'CreateTemplate': 'config/templates',
  'SaveTemplate': 'config/templates',
  "DeleteTemplate": 'config/templates',
  'CheckEntityCode': 'config/checkentitycode',
  'BulkSaveTemplates': 'config/templates/bulksave',
  'GetAllTemplatesData': 'config/templates/grid'
};

var oTextAssetRequestMapping = {
  "SaveTextAssets": 'config/textassets',
  "SaveImpactedTextAsset": 'config/textassets',
  'GetAllStandardProperties': 'config/attributes/standard',
  "GetTextAsset": 'config/textassetWithGlobalPermission/<%=id%>',
  'GetClassWithoutPC': CommonModuleRequestMapping.GetTextAssetWithoutPC,
  "DeleteTextAssets": 'config/textassets',
  "GetAllTextAssets": 'config/textassets',
  "CreateTextAssets": 'config/textassets',
  'ImportTextAssets': 'config/import/klass/textasset',
  'ExportTextAssets': 'config/export/klass/textasset',
  'CheckEntityCode': 'config/checkentitycode'
};

var oSupplierRequestMapping = {
  "SaveSuppliers": 'config/suppliers',
  "SaveImpactedSupplier": 'config/suppliers',
  'GetAllStandardProperties': 'config/attributes/standard',
  "GetSupplier": 'config/supplierWithGlobalPermission/<%=id%>',
  'GetClassWithoutPC': CommonModuleRequestMapping.GetSupplierWithoutPC,
  "DeleteSuppliers": 'config/suppliers',
  "GetAllSuppliers": 'config/suppliers',
  "CreateSuppliers": 'config/suppliers',
  'ImportSuppliers': 'config/import/klass/supplier',
  'ExportSuppliers': 'config/export/klass/supplier',
  'CheckEntityCode': 'config/checkentitycode'
};

var oAssetRequestMapping = {
  'SaveAsset': 'config/assets',
  'SaveImpactedAsset': 'config/assets',
  'GetAllStandardProperties': 'config/attributes/standard',
  'GetAsset': 'config/assetWithGlobalPermission/<%=id%>',
  'GetClassWithoutPC': CommonModuleRequestMapping.GetAssetWithoutPC,
  'DeleteAssets': 'config/assets',
  'GetAllAssets': 'config/assets',
  'CreateAsset': 'config/assets',
  'ImportAsset': 'config/import/klass/asset',
  'ExportAsset': 'config/export/klass/asset',
  'CheckEntityCode': 'config/checkentitycode'
};

var oTargetRequestMapping = {
  "SaveTarget": 'config/targets',
  "SaveImpactedTarget": 'config/targets',
  'GetAllStandardProperties': 'config/attributes/standard',
  "GetTarget": 'config/targetWithGlobalPermission/<%=id%>',
  'GetClassWithoutPC': CommonModuleRequestMapping.GetTargetWithoutPC,
  "DeleteTarget": 'config/targets',
  "GetAllTarget": 'config/targets',
  "CreateTarget": 'config/targets',
  'ImportTarget': 'config/import/klass/market',
  'ExportTarget': 'config/export/klass/market',
  'CheckEntityCode': 'config/checkentitycode'
};

var oRelationshipRequestMapping = {
  'GetRelationshipList': 'config/relationships/getall',
  'BulkDelete': 'config/relationships',
  'SaveRelationship': 'config/relationships',
  'CreateRelationship': 'config/relationships',
  'GetRelationshipById': 'config/relationships/<%=id%>',
  'GetKlassesForRelationshipSide':'config/klassesForRelationshipSide/<%=id%>',
  'CheckEntityCode': 'config/checkentitycode',
  'BulkSave': 'config/bulksaverelationships',
  'ImportRelationship' : 'config/import/relationships',
  'ExportRelationship' : 'config/export/relationships'
};

var oRuleRequestMapping = {
  'GetAll': 'config/datarules/getall',
  'CreateRule': 'config/datarule',
  'DeleteRule': 'config/datarule',
  'GetRule': 'config/datarule/<%=id%>',
  'SaveRule': 'config/datarule',
  'CheckEntityCode': 'config/checkentitycode',
  'GetTaxonomy': 'config/allchildmajortaxonomies',
  'SaveBulkRule': 'config/bulksave/datarules',
  'ImportRule' : 'config/import/rule',
  'ExportRule' : 'config/export/rule'
};

var oPermissionRequestMapping = {
  'GetAll': 'config/globalpermissions',
  'GetPermission': 'config/globalpermissionForEntities/<%=id%>',
  'GetHierarchyPermission': 'config/hierarchy/permission/get',
  'GetPropertyCollections': 'config/globalpermissions/propertycollections',
  'GetProperties': 'config/globalpermissions/propertycollection/properties',
  'SavePermission': 'config/createOrSaveGlobalPermission',
  'GetTemplatePermission': 'config/template/permission',
  'SaveTemplatePermission': 'config/template/savepermission',
  'GetPermissionTemplateList': 'config/globalpermissionwithallowedtemplates',
  'CheckEntityCode': 'config/checkentitycode',
  'GetPermissionTemplate': 'config/permission/get',
  'GetPermissionFunction': 'config/permission/function/<%=roleId%>'
};

var oRuleListRequestMapping = {
  'GetAll': 'config/ruleList/getall',
  'CreateRuleList': 'config/ruleList',
  'DeleteRuleList': 'config/ruleList',
  'GetRuleList': 'config/ruleList/<%=id%>',
  'SaveRuleList': 'config/ruleList',
  'CheckEntityCode': 'config/checkentitycode'
};

var oTaxonomyRequestMapping = {
  'GetTaxonomyHierarchyForSelectedTaxonomy': 'runtime/taxonomyhierarchyforselectedtaxonomy',
};

var oAttributionTaxonomyRequestMapping = {
  'CreateTaxonomy': 'config/attributiontaxonomy',
  'CreateBulkTaxonomies': 'config/attributiontaxonomy/bulk',
  'SaveTaxonomy': 'config/attributiontaxonomy',
  'GetTaxonomy': 'config/attributiontaxonomy/<%=id%>',
  'DeleteTaxonomy': 'config/attributiontaxonomy',
  'GetFilterAndSortList': 'config/klasstaxonomy/filtersortdata',
  'GetTaxonomyTree': 'runtime/alltaxonomiestree',
  'GetAllTaxonomies': 'config/articletaxonomylist',
  'AddDeleteTaxonomyLevel': 'config/adddeletetaxonomylevel',
  'GetAllowedTagValues': 'config/allowedtagvalues/<%=id%>',
  'CheckEntityCode': 'config/checkentitycode',
  'GetTaxonomyPropertyCollection': CommonModuleRequestMapping.GetTaxonomyPropertyCollection,
  'ExportTaxonomy': 'config/export/attributiontaxonomy',
  'importTaxonomy': 'config/import/attributiontaxonomy',
};

const oLanguageTaxonomyRequestMapping = {
  'CreateLanguages': 'config/languages',
  'SaveLanguages': 'config/languages',
  'GetLanguages': 'config/languages/<%=id%>',
  'DeleteLanguages': 'config/languages',
  'CheckEntityCode': 'config/checkentitycode',
  'GetCurrentDefaultLanguage': 'config/currentdefaultlanguage',
  "ExportLanguageTree": "config/export/language",
  "ImportLanguageTree": "config/import/language"
};

var oProcessRequestMapping = {
  'GetProcessList': 'config/processevents/getall',
  'GetProcess': 'config/processevents/<%=id%>',
  'SaveProcess': 'config/processevents',
  'CreateProcess': 'config/processevents',
  'DeleteProcess': 'config/processevents',
  'CheckEntityCode': 'config/checkentitycode',
  'GetAllTalendJobList': 'config/dataintegration/alltalendjobs',
  'GetAllCustomCompnents': 'config/dataintegration/allcustomcomponents',
  'GetProcessListForGrid': 'config/processevents/grid',
  'BulkSave': 'config/processevents/bulksave',
  'GetAttribute': 'config/attributes/<%=id%>',
  'GetTag': 'config/tags/<%=id%>?mode=<%=mode%>',
  'GetConfigData': 'config/configdata',
  'SaveAs': 'config/processevents/clone',
  'BulkCodeCheck': 'config/bulkcheckentitycode',
  'GetComponentDetails': 'config/taskmetadata/<%=id%>',
  'GetAllProcessComponents': 'config/alltasks',
  'GetDataLanguage': 'config/languagesinfo',
  'GetAllOrganisation':'config/organizations/getall',
  'GetTagValues': 'config/configdata/tagValues'
};

var oSettingsRequestMapping = {
  'GetConfigData': 'config/configdata',
  'GetTag': 'config/tags/<%=id%>?mode=<%=mode%>',
  'GetMajorTaxonomy': 'config/immediatechildmajortaxonomies',
  'GetAllTaxonomy': 'config/allchildmajortaxonomies',
  'CheckEntityCode': 'config/checkentitycode',
  'CreateDashboardTabs': 'config/dashboardtab',
  'ImportExcel': 'config/import',
  'SelectiveExport': 'export/excel'
};

var oTranslationsRequestMapping = {
  'GetTranslationsForTag': 'config/dynamictranslations/tag/get',
  'GetTranslationsForRelationship': 'config/dynamictranslations/relationship/get',
  'GetStaticTranslations': 'config/statictranslations/get',
  'GetTranslations': 'config/dynamictranslations/get',
  'SaveTranslations': 'config/dynamictranslations/save',
  'SaveRelationshipTranslations': 'config/dynamictranslations/relationship/save',
  'SaveStaticTranslations': 'config/statictranslations/save',
  'ImportTranslation': 'config/import/translation',
  'ExportTranslation': 'config/export/translation',
  'ExportTranslationByEntityType': 'config/export/translationByEntity',
  'GetDataLanguage': 'config/languagesinfo'
};

var oGoldenRecordsMapping = {
  'GetAllGoldenRecordsRules': 'config/goldenrecordrule/getall',
  'CreateRule': 'config/goldenrecordrule',
  'GetRule': 'config/goldenrecordrule/<%=id%>',
  'SaveRule': 'config/goldenrecordrule',
  'DeleteRule': 'config/goldenrecordrule',
  'GetTaxonomy': 'config/allchildmajortaxonomies',
  'CheckEntityCode': 'config/checkentitycode',
  'SaveBulkRule': 'config/bulksave/goldenrecordrules',
  'ImportGoldenRecordRule' : 'config/import/goldenrecordrule',
  'ExportGoldenRecordRule' : 'config/export/goldenrecordrule'
};

const oThemeConfigurationRequestMapping = {
  'SaveThemeConfigurations':'config/themeconfigurations',
  'GetThemeConfigurations':'config/themeconfigurations',
  'ResetThemeConfigurations':'config/savedefaultthemeconfigurations',
};

const oViewConfigurationRequestMapping = {
  'SaveViewConfigurations':'config/viewconfigurations',
  'GetViewConfigurations':'config/viewconfigurations',
  'ResetViewConfigurations':'config/savedefaultviewconfigurations',
};

const oPdfReactorServerRequestMapping = {
  'TestServerConfiguration': 'config/pdfReactorServer',
  'SaveServerConfiguration': 'config/smartdocument',
  'GetServerConfiguration': 'config/smartdocument/<%=id%>',
};

const oSmartDocumentRequestMapping = {
  'GetSmartDocument': 'config/smartdocument/<%=id%>',
  'SaveSmartDocument': 'config/smartdocument',
  'GetSmartDocumentTemplate': 'config/smartDocumentTemplate/<%=id%>',
  'SaveSmartDocumentTemplate': 'config/smartDocumentTemplate',
  'DeleteSmartDocumentTemplate': 'config/smartDocumentTemplate',
  'GetSmartDocumentPreset': 'config/smartdocumentpreset/<%=id%>',
  'SaveSmartDocumentPreset': 'config/smartdocumentpreset',
  'DeleteSmartDocumentPreset': 'config/smartdocumentpreset',
  'UploadSmartDocumentTemplate' : 'config/smartdocument/uploadtemplate?mode=config',
  'CreateSmartDocumentTemplate' : 'config/smartDocumentTemplate/',
  'CreateSmartDocumentPreset' : 'config/smartdocumentpreset/',
  'GetDataLanguage': 'config/languagesinfo',
  'GetTaxonomy': 'config/immediatechildmajortaxonomies',
  'CheckEntityCode': 'config/checkentitycode',
};

const oDownloadTrackerRequestMapping = {
  'GetDownloadLogs': "config/downloadlogs",
  'DownloadLogs':'config/exportlogs'
};

const oGridEditView = {
  "saveGridEditProperty": "config/grideditpropertylist",
  "getGridEditProperty": "config/grideditpropertylists",
  "getGridEditProperties": "config/grideditproperties"
}

const oAuditLogRequestMapping = {
  "getAuditLogListForGridView": "config/auditLog",
  "exportAuditLogList": "config/auditLog/export",
  "getAuditLogExportStatusListForGridView": "config/auditLog/export",
  "deleteAuditLogExportStatus": "config/auditLog/export",
  "downloadAuditLogExportStatus": "asset/Document/<%=id%>"
};

const oIconLibraryRequestMapping = {
  'GetIconLibrary': 'config/getallicons',
  'ValidateIconLibrary': 'config/bulkcheckentitycode'
};

const oIndesignServerRequestMapping = {
  'getIndesignServersConfiguration': 'config/indesignserver/ping/allinstances',
  'saveIndesignConfiguration': 'config/indesignserver/instances/update',
  'checkStatusOfIndesignServer': 'config/indesignserver/instances/checkstatus',
  'getDAMConfiguration': 'config/getdamconfiguration',
  'saveDAMConfiguration': 'config/savedamconfiguration'
};

const oAdministrationSummaryRequestMapping = {
  'getObjectCount': 'config/getObjectCount',
};


export const UploadRequestMapping = oUploadRequestMapping;
export const TagRequestMapping = oTagRequestMapping;
export const KpiRequestMapping = oKpiRequestMapping;
export const AttributeRequestMapping = oAttributeRequestMapping;
export const PropertyCollectionRequestMapping = oPropertyCollectionRequestMapping;
export const ClassRequestMapping = oClassRequestMapping;
export const TasksRequestMapping = oTasksRequestMapping;
export const DataGovernanceTasksRequestMapping = oDataGovernanceTasksRequestMapping;
export const TemplateRequestMapping = oTemplateRequestMapping;
export const AssetRequestMapping = oAssetRequestMapping;
export const TargetRequestMapping = oTargetRequestMapping;
export const RelationshipRequestMapping = oRelationshipRequestMapping;
export const UserRequestMapping = oUserRequestMapping;
export const ProfileRequestMapping = oProfileRequestMapping;
export const MappingRequestMapping = oMappingRequestMapping;
export const EndpointRequestMapping = oEndpointRequestMapping;
export const RoleRequestMapping = oRoleRequestMapping;
export const RuleRequestMapping = oRuleRequestMapping;
export const PermissionRequestMapping = oPermissionRequestMapping;
export const RuleListRequestMapping = oRuleListRequestMapping;
export const TaxonomyRequestMapping = oTaxonomyRequestMapping;
export const AttributionTaxonomyRequestMapping = oAttributionTaxonomyRequestMapping;
export const TranslationsRequestMapping = oTranslationsRequestMapping;
export const ContextRequestMapping = oContextRequestMapping;
export const TextAssetRequestMapping = oTextAssetRequestMapping;
export const SupplierRequestMapping = oSupplierRequestMapping;
export const SettingsRequestMapping = oSettingsRequestMapping;
export const ProcessRequestMapping = oProcessRequestMapping;
export const OrganisationConfigRequestMapping = oOrganisationConfigRequestMapping;
export const TabsRequestMapping = oTabsRequestMapping;
export const GoldenRecordsMapping = oGoldenRecordsMapping;
export const LanguageTaxonomyRequestMapping = oLanguageTaxonomyRequestMapping;
export const ThemeConfigurationRequestMapping = oThemeConfigurationRequestMapping;
export const ViewConfigurationRequestMapping = oViewConfigurationRequestMapping;
export const SmartDocumentRequestMapping = oSmartDocumentRequestMapping;
export const DownloadTrackerRequestMapping = oDownloadTrackerRequestMapping;
export const SSOSettingRequestMapping = oSSOSettingConfigRequestMapping;
export const AuthorizationRequestMapping = oAuthorizationRequestMapping;
export const PdfReactorServerRequestMapping = oPdfReactorServerRequestMapping;
export const GridEditMapping = oGridEditView;
export const AuditLogRequestMapping = oAuditLogRequestMapping;
export const IconLibraryRequestMapping = oIconLibraryRequestMapping;
export const IndesignServerRequestMapping = oIndesignServerRequestMapping;
export const VariantConfigurationRequestMapping = oVariantConfigurationRequestMapping;
export const AdministrationSummaryRequestMapping = oAdministrationSummaryRequestMapping;
