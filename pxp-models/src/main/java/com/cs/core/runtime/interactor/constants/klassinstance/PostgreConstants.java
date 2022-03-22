package com.cs.core.runtime.interactor.constants.klassinstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostgreConstants {
  
  public static final String COLUMN_NAME_CLONE_OF                  = "cloneof";
  
  public static final String COLUMN_NAME_SAVE_COMMENT              = "savecomment";
  
  public static final String COLUMN_NAME_IS_EMBEDDED               = "isembedded";
  
  public static final String COLUMN_NAME_NATURE_RELATIONSHIPS      = "naturerelationships";
  
  public static final String COLUMN_NAME_RELATIONSHIPS             = "relationships";
  
  public static final String SET                                   = "set";
  
  public static final String STRING_LIKE_CONSTANT                  = "%";
  
  public static final String COUNT                                 = "count";
  
  public static final String COLUMN_NAME_NAME                      = "name";
  
  public static final String COLUMN_NAME_LANG_ID                   = "langid";
  
  public static final String COLUMN_NAME_TAG_VALUES                = "tagvalues";
  
  public static final String COLUMN_NAME_CONTEXT_INSTANCE_ID       = "contextinstanceid";
  
  public static final String COLUMN_NAME_VARIANT_OF                = "variantof";
  
  public static final String COLUMN_NAME_TAG_ID                    = "tagid";
  
  public static final String COLUMN_NAME_DUPLICATE_STATUS          = "duplicatestatus";
  
  public static final String COLUMN_NAME_IS_MANDATORY_VIOLATED     = "ismandatoryviolated";
  
  public static final String COLUMN_NAME_TAGS                      = "tags";
  
  public static final String COLUMN_NAME_KLASS_INSTANCE_VERSION    = "klassinstanceversion";
  
  public static final String COLUMN_NAME_IS_MATCH_AND_MERGE        = "ismatchandmerge";
  
  public static final String COLUMN_NAME_VALUE_AS_HTML             = "valueashtml";
  
  public static final String COLUMN_NAME_VALUE_AS_NUMBER           = "valueasnumber";
  
  public static final String COLUMN_NAME_VALUE_AS_EXPRESSION       = "valueasexpression";
  
  public static final String COLUMN_NAME_VALUE                     = "value";
  
  public static final String COLUMN_NAME_VARIANT_INSTANCE_ID       = "variantinstanceid";
  
  public static final String COLUMN_NAME_IS_SHOULD_VIOLATED        = "isshouldviolated";
  
  public static final String COLUMN_NAME_NOTIFICATION              = "notification";
  
  public static final String COLUMN_NAME_ATTRIBUTE_ID              = "attributeid";
  
  public static final String COLUMN_NAME_IS_UNIQUE                 = "isunique";
  
  public static final String COLUMN_NAME_IS_CONFLICT_RESOLVED      = "isconflictresolved";
  
  public static final String COLUMN_NAME_CONFLICTING_VALUES        = "conflictingvalues";
  
  public static final String COLUMN_NAME_LANGUAGE                  = "language";
  
  public static final String COLUMN_NAME_CODE                      = "code";
  
  public static final String COLUMN_NAME_PARTNER_SOURCES           = "partnersources";
  
  public static final String COLUMN_NAME_SUMMARY                   = "summary";
  
  public static final String COLUMN_NAME_CONTEXT                   = "context";
  
  public static final String COLUMN_NAME_MESSAGES                  = "messages";
  
  public static final String COLUMN_NAME_RULE_VIOLATION            = "ruleviolation";
  
  public static final String COLUMN_NAME_CREATION_LANGUAGE         = "creationlanguage";
  
  public static final String COLUMN_NAME_LANGUAGE_CODES            = "languagecodes";
  
  public static final String COLUMN_NAME_LANGUAGE_INSTANCES        = "languageinstances";
  
  public static final String COLUMN_NAME_ATTRIBUTE_VARIANTS        = "attributevariants";
  
  public static final String COLUMN_NAME_VARIANTS                  = "variants";
  
  public static final String COLUMN_NAME_PATH                      = "path";
  
  public static final String COLUMN_NAME_IS_FROM_EXTERNAL_SOURCE   = "isfromexternalsource";
  
  public static final String COLUMN_NAME_IS_SKIPPED                = "isskipped";
  
  public static final String COLUMN_NAME_PARENT_ID                 = "parentid";
  
  public static final String COLUMN_NAME_KLASS_INSTANCE_ID         = "klassinstanceid";
  
  public static final String COLUMN_NAME_VERSION_OF                = "versionof";
  
  public static final String COLUMN_NAME_BRANCH_OF                 = "branchof";
  
  public static final String COLUMN_NAME_DEFAULT_ASSET_INSTANCE_ID = "defaultassetinstanceid";
  
  public static final String COLUMN_NAME_ORIGINAL_INSTANCE_ID      = "originalinstanceid";
  
  public static final String COLUMN_NAME_ENDPOINT_ID               = "endpointid";
  
  public static final String COLUMN_NAME_SYSTEM_ID                 = "systemid";
  
  public static final String COLUMN_NAME_LOGICAL_CATALOG_ID        = "logicalcatalogid";
  
  public static final String COLUMN_NAME_PHYSICAL_CATALOG_ID       = "physicalcatalogid";
  
  public static final String COLUMN_NAME_ORGANIZATION_ID           = "organizationid";
  
  public static final String COLUMN_NAME_SELECTED_TAXONOMY_IDS     = "selectedtaxonomyids";
  
  public static final String COLUMN_NAME_TAXONOMY_IDS              = "taxonomyids";
  
  public static final String COLUMN_NAME_TYPES                     = "types";
  
  public static final String COLUMN_NAME_ROLES                     = "roles";
  
  public static final String COLUMN_NAME_COMPONENT_ID              = "componentid";
  
  public static final String COLUMN_NAME_BASE_TYPE                 = "basetype";
  
  public static final String COLUMN_NAME_JOB_ID                    = "jobid";
  
  public static final String COLUMN_NAME_LAST_MODIFIED             = "lastmodified";
  
  public static final String COLUMN_NAME_OWNER                     = "owner";
  
  public static final String COLUMN_NAME_CREATED_ON                = "createdon";
  
  public static final String COLUMN_NAME_CREATED_BY                = "createdby";
  
  public static final String COLUMN_NAME_LAST_MODIFIED_BY          = "lastmodifiedby";
  
  public static final String COLUMN_NAME_VERSION_TIMESTAMP         = "versiontimestamp";
  
  public static final String COLUMN_NAME_VERSION_ID                = "versionid";
  
  public static final String COLUMN_NAME_ID                        = "id";
  
  public enum ItemType
  {
    ARTICLE
  }
  
  public static final String              COLUMN_QUOTE                     = "\"";
  
  public static final String              ID                               = COLUMN_NAME_ID;
  public static final String              KLASS_INSTANCE_ID                = COLUMN_NAME_KLASS_INSTANCE_ID;
  public static final String              VALUE                            = COLUMN_NAME_VALUE;
  public static final String              NAME                             = COLUMN_NAME_NAME;
  
  public static final String              ARTICLE_TABLE_NAME               = "article";
  
  public static final String              ATTRIBUTE_TABLE_NAME             = "attributes";
  public static final String              TAG_TABLE_NAME                   = COLUMN_NAME_TAGS;
  // public static final String CONFLICTING_VALUES_TABLE_NAME = "attributes";
  public static final String              LANGUAGE_TABLE_NAME              = "article_lang";
  
  public static final String              PARAMETER_MARKER                 = "= ?";
  
  public static final List<String>        STANDARD_KLASS_TABLE_COLUMNS     = new ArrayList<>(
      Arrays.asList(COLUMN_NAME_ID, COLUMN_NAME_BASE_TYPE, COLUMN_NAME_TYPES,
          COLUMN_NAME_TAXONOMY_IDS, COLUMN_NAME_SELECTED_TAXONOMY_IDS, COLUMN_NAME_ORGANIZATION_ID,
          COLUMN_NAME_PHYSICAL_CATALOG_ID, COLUMN_NAME_LOGICAL_CATALOG_ID, COLUMN_NAME_SYSTEM_ID,
          COLUMN_NAME_ENDPOINT_ID, COLUMN_NAME_ORIGINAL_INSTANCE_ID,
          COLUMN_NAME_DEFAULT_ASSET_INSTANCE_ID, COLUMN_NAME_CLONE_OF, COLUMN_NAME_VERSION_OF,
          COLUMN_NAME_KLASS_INSTANCE_ID, COLUMN_NAME_PARENT_ID, COLUMN_NAME_PATH,
          COLUMN_NAME_VARIANTS, COLUMN_NAME_ATTRIBUTE_VARIANTS, COLUMN_NAME_LANGUAGE_INSTANCES,
          COLUMN_NAME_LANGUAGE_CODES, COLUMN_NAME_CREATION_LANGUAGE, COLUMN_NAME_RULE_VIOLATION,
          COLUMN_NAME_MESSAGES, COLUMN_NAME_CONTEXT, COLUMN_NAME_SUMMARY,
          COLUMN_NAME_PARTNER_SOURCES, COLUMN_NAME_RELATIONSHIPS, COLUMN_NAME_NATURE_RELATIONSHIPS,
          COLUMN_NAME_IS_EMBEDDED, COLUMN_NAME_SAVE_COMMENT, COLUMN_NAME_VERSION_ID,
          COLUMN_NAME_VERSION_TIMESTAMP, COLUMN_NAME_CREATED_BY, COLUMN_NAME_CREATED_ON,
          COLUMN_NAME_LAST_MODIFIED_BY, COLUMN_NAME_LAST_MODIFIED));
  
  public static final List<String>        STANDARD_ATTRIBUTE_TABLE_COLUMNS = new ArrayList<>(
      Arrays.asList(COLUMN_NAME_ID, COLUMN_NAME_KLASS_INSTANCE_ID, COLUMN_NAME_CODE,
          COLUMN_NAME_LANGUAGE, COLUMN_NAME_CONFLICTING_VALUES, COLUMN_NAME_IS_CONFLICT_RESOLVED,
          COLUMN_NAME_IS_UNIQUE, COLUMN_NAME_BASE_TYPE, COLUMN_NAME_ATTRIBUTE_ID,
          COLUMN_NAME_NOTIFICATION, COLUMN_NAME_IS_SHOULD_VIOLATED, COLUMN_NAME_CONTEXT,
          COLUMN_NAME_VARIANT_INSTANCE_ID, COLUMN_NAME_VALUE, COLUMN_NAME_VALUE_AS_EXPRESSION,
          COLUMN_NAME_VALUE_AS_NUMBER, COLUMN_NAME_VALUE_AS_HTML, COLUMN_NAME_ORIGINAL_INSTANCE_ID,
          COLUMN_NAME_TAGS, COLUMN_NAME_IS_MANDATORY_VIOLATED, COLUMN_NAME_DUPLICATE_STATUS,
          COLUMN_NAME_VERSION_ID, COLUMN_NAME_VERSION_TIMESTAMP, COLUMN_NAME_CREATED_BY,
          COLUMN_NAME_CREATED_ON, COLUMN_NAME_LAST_MODIFIED_BY, COLUMN_NAME_LAST_MODIFIED));
  
  public static final List<String>        STANDARD_TAG_TABLE_COLUMNS       = new ArrayList<>(
      Arrays.asList(COLUMN_NAME_ID, COLUMN_NAME_KLASS_INSTANCE_ID, COLUMN_NAME_CONFLICTING_VALUES,
          COLUMN_NAME_IS_MATCH_AND_MERGE, COLUMN_NAME_TAG_ID, COLUMN_NAME_BASE_TYPE,
          COLUMN_NAME_NOTIFICATION, COLUMN_NAME_IS_CONFLICT_RESOLVED,
          COLUMN_NAME_IS_SHOULD_VIOLATED, COLUMN_NAME_IS_MANDATORY_VIOLATED,
          COLUMN_NAME_VARIANT_INSTANCE_ID, COLUMN_NAME_CONTEXT_INSTANCE_ID, COLUMN_NAME_TAG_VALUES,
          COLUMN_NAME_VERSION_ID, COLUMN_NAME_VERSION_TIMESTAMP, COLUMN_NAME_CREATED_BY,
          COLUMN_NAME_CREATED_ON, COLUMN_NAME_LAST_MODIFIED_BY, COLUMN_NAME_LAST_MODIFIED));
  
  public static final List<String>        STANDARD_LANG_TABLE_COLUMNS      = new ArrayList<>(
      Arrays.asList(COLUMN_NAME_ID, COLUMN_NAME_LANG_ID, COLUMN_NAME_NAME));
  
  public static final Map<String, String> ARTICLE_TABLE_PROPERTIES         = new HashMap<String, String>()
                                                                           {
                                                                             
                                                                             {
                                                                               put(COLUMN_NAME_ID,
                                                                                   "Id");
                                                                               put(COLUMN_NAME_BASE_TYPE,
                                                                                   "BaseType");
                                                                               put(COLUMN_NAME_TYPES,
                                                                                   "Types");
                                                                               put(COLUMN_NAME_TAXONOMY_IDS,
                                                                                   "TaxonomyIds");
                                                                               put(COLUMN_NAME_SELECTED_TAXONOMY_IDS,
                                                                                   "SelectedTaxonomyIds");
                                                                               put(COLUMN_NAME_ORGANIZATION_ID,
                                                                                   "OrganizationId");
                                                                               put(COLUMN_NAME_PHYSICAL_CATALOG_ID,
                                                                                   "PhysicalCatalogId");
                                                                               put(COLUMN_NAME_LOGICAL_CATALOG_ID,
                                                                                   "LogicalCatalogId");
                                                                               put(COLUMN_NAME_SYSTEM_ID,
                                                                                   "SystemId");
                                                                               put(COLUMN_NAME_ENDPOINT_ID,
                                                                                   "EndpointId");
                                                                               put(COLUMN_NAME_ORIGINAL_INSTANCE_ID,
                                                                                   "OriginalInstanceId");
                                                                               put(COLUMN_NAME_DEFAULT_ASSET_INSTANCE_ID,
                                                                                   "DefaultAssetInstanceId");
                                                                               put(COLUMN_NAME_CLONE_OF,
                                                                                   "CloneOf");
                                                                               put(COLUMN_NAME_VERSION_OF,
                                                                                   "VersionOf");
                                                                               put(COLUMN_NAME_KLASS_INSTANCE_ID,
                                                                                   "KlassInstanceId");
                                                                               put(COLUMN_NAME_PARENT_ID,
                                                                                   "ParentId");
                                                                               put(COLUMN_NAME_PATH,
                                                                                   "Path");
                                                                               put(COLUMN_NAME_VARIANTS,
                                                                                   "Variants");
                                                                               put(COLUMN_NAME_ATTRIBUTE_VARIANTS,
                                                                                   "AttributeVariants");
                                                                               put(COLUMN_NAME_LANGUAGE_INSTANCES,
                                                                                   "LanguageInstances");
                                                                               put(COLUMN_NAME_LANGUAGE_CODES,
                                                                                   "LanguageCodes");
                                                                               put(COLUMN_NAME_CREATION_LANGUAGE,
                                                                                   "CreationLanguage");
                                                                               put(COLUMN_NAME_RULE_VIOLATION,
                                                                                   "RuleViolation");
                                                                               put(COLUMN_NAME_MESSAGES,
                                                                                   "Messages");
                                                                               put(COLUMN_NAME_CONTEXT,
                                                                                   "Context");
                                                                               put(COLUMN_NAME_SUMMARY,
                                                                                   "Summary");
                                                                               put(COLUMN_NAME_PARTNER_SOURCES,
                                                                                   "PartnerSources");
                                                                               put(COLUMN_NAME_RELATIONSHIPS,
                                                                                   "Relationships");
                                                                               put(COLUMN_NAME_NATURE_RELATIONSHIPS,
                                                                                   "NatureRelationships");
                                                                               put(COLUMN_NAME_IS_EMBEDDED,
                                                                                   "IsEmbedded");
                                                                               put(COLUMN_NAME_SAVE_COMMENT,
                                                                                   "SaveComment");
                                                                               put(COLUMN_NAME_VERSION_ID,
                                                                                   "VersionId");
                                                                               put(COLUMN_NAME_VERSION_TIMESTAMP,
                                                                                   "VersionTimestamp");
                                                                               put(COLUMN_NAME_CREATED_BY,
                                                                                   "CreatedBy");
                                                                               put(COLUMN_NAME_CREATED_ON,
                                                                                   "CreatedOn");
                                                                               put(COLUMN_NAME_LAST_MODIFIED_BY,
                                                                                   "LastModifiedBy");
                                                                               put(COLUMN_NAME_LAST_MODIFIED,
                                                                                   "LastModified");
                                                                             }
                                                                           };
  
  public static final Map<String, String> ATTRIBUTE_TABLE_PROPERTIES       = new HashMap<String, String>()
                                                                           {
                                                                             
                                                                             {
                                                                               put(COLUMN_NAME_ID,
                                                                                   "Id");
                                                                               put(COLUMN_NAME_KLASS_INSTANCE_ID,
                                                                                   "KlassInstanceId");
                                                                               put(COLUMN_NAME_CODE,
                                                                                   "Code");
                                                                               put(COLUMN_NAME_LANGUAGE,
                                                                                   "Language");
                                                                               put(COLUMN_NAME_CONFLICTING_VALUES,
                                                                                   "ConflictingValues");
                                                                               put(COLUMN_NAME_IS_CONFLICT_RESOLVED,
                                                                                   "IsConflictResolved");
                                                                               put(COLUMN_NAME_IS_UNIQUE,
                                                                                   "IsUnique");
                                                                               put(COLUMN_NAME_BASE_TYPE,
                                                                                   "BaseType");
                                                                               put(COLUMN_NAME_ATTRIBUTE_ID,
                                                                                   "AttributeId");
                                                                               put(COLUMN_NAME_NOTIFICATION,
                                                                                   "Notification");
                                                                               put(COLUMN_NAME_IS_SHOULD_VIOLATED,
                                                                                   "IsShouldViolated");
                                                                               put(COLUMN_NAME_CONTEXT,
                                                                                   "Context");
                                                                               put(COLUMN_NAME_VARIANT_INSTANCE_ID,
                                                                                   "VariantInstanceId");
                                                                               put(COLUMN_NAME_VALUE,
                                                                                   "Value");
                                                                               put(COLUMN_NAME_VALUE_AS_EXPRESSION,
                                                                                   "ValueAsExpression");
                                                                               put(COLUMN_NAME_VALUE_AS_NUMBER,
                                                                                   "ValueAsNumber");
                                                                               put(COLUMN_NAME_VALUE_AS_HTML,
                                                                                   "ValueAsHtml");
                                                                               put(COLUMN_NAME_ORIGINAL_INSTANCE_ID,
                                                                                   "OriginalInstanceId");
                                                                               put(COLUMN_NAME_TAGS,
                                                                                   "Tags");
                                                                               put(COLUMN_NAME_IS_MANDATORY_VIOLATED,
                                                                                   "IsMandatoryViolated");
                                                                               put(COLUMN_NAME_DUPLICATE_STATUS,
                                                                                   "DuplicateStatus");
                                                                               put(COLUMN_NAME_VERSION_ID,
                                                                                   "VersionId");
                                                                               put(COLUMN_NAME_VERSION_TIMESTAMP,
                                                                                   "VersionTimestamp");
                                                                               put(COLUMN_NAME_CREATED_BY,
                                                                                   "CreatedBy");
                                                                               put(COLUMN_NAME_CREATED_ON,
                                                                                   "CreatedOn");
                                                                               put(COLUMN_NAME_LAST_MODIFIED_BY,
                                                                                   "LastModifiedBy");
                                                                               put(COLUMN_NAME_LAST_MODIFIED,
                                                                                   "LastModified");
                                                                             }
                                                                           };
  
  public static final Map<String, String> TAG_TABLE_PROPERTIES             = new HashMap<String, String>()
                                                                           {
                                                                             
                                                                             {
                                                                               put(COLUMN_NAME_ID,
                                                                                   "Id");
                                                                               put(COLUMN_NAME_KLASS_INSTANCE_ID,
                                                                                   "KlassInstanceId");
                                                                               put(COLUMN_NAME_CONFLICTING_VALUES,
                                                                                   "ConflictingValues");
                                                                               put(COLUMN_NAME_IS_MATCH_AND_MERGE,
                                                                                   "IsMatchAndMerge");
                                                                               put(COLUMN_NAME_TAG_ID,
                                                                                   "TagId");
                                                                               put(COLUMN_NAME_BASE_TYPE,
                                                                                   "BaseType");
                                                                               put(COLUMN_NAME_NOTIFICATION,
                                                                                   "Notification");
                                                                               put(COLUMN_NAME_IS_CONFLICT_RESOLVED,
                                                                                   "IsConflictResolved");
                                                                               put(COLUMN_NAME_IS_SHOULD_VIOLATED,
                                                                                   "IsShouldViolated");
                                                                               put(COLUMN_NAME_IS_MANDATORY_VIOLATED,
                                                                                   "IsMandatoryViolated");
                                                                               put(COLUMN_NAME_VARIANT_INSTANCE_ID,
                                                                                   "VariantInstanceId");
                                                                               put(COLUMN_NAME_CONTEXT_INSTANCE_ID,
                                                                                   "ContextInstanceId");
                                                                               put(COLUMN_NAME_TAG_VALUES,
                                                                                   "TagValues");
                                                                               put(COLUMN_NAME_VERSION_ID,
                                                                                   "VersionId");
                                                                               put(COLUMN_NAME_VERSION_TIMESTAMP,
                                                                                   "VersionTimestamp");
                                                                               put(COLUMN_NAME_CREATED_BY,
                                                                                   "CreatedBy");
                                                                               put(COLUMN_NAME_CREATED_ON,
                                                                                   "CreatedOn");
                                                                               put(COLUMN_NAME_LAST_MODIFIED_BY,
                                                                                   "LastModifiedBy");
                                                                               put(COLUMN_NAME_LAST_MODIFIED,
                                                                                   "LastModified");
                                                                             }
                                                                           };
}
