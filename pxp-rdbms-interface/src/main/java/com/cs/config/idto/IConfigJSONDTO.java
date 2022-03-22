package com.cs.config.idto;

import com.cs.core.technical.rdbms.idto.IRootDTO;

/**
 * @author vallee
 */
public interface IConfigJSONDTO extends IRootDTO {
  
  // Collection of JSON tags used in configuration database
  public enum ConfigTag
  {
    allowedStyles, attributeVariantContextCode, availability, cardinality, children, classCode, classifierIID, code, color, columns,
    contextCode, couplingType, couplings, defaultEndTime, defaultStartTime, defaultValue, defaultUnit, description, elements,
    embeddedClasses, entities, events, isAbstract, isAutoCreate, isCurrentTime, isCutOff, isDefaultChild,
    isDefaultForXRay, isDisabled, isDuplicateVariantAllowed, isFilterable, isForXRay, isGridEditable, isIdentifier, isInherited,
    isLimitedObject, isMandatory, isMultiselect, isNature, isSearchable, isShould, isSkipped, isSortable, isStandard, isTimeEnabled,
    isTranslatable, isVersionable, isVisible, label, linkedMasterTagId, parentCode, placeholder, precision, prefix, productVariantContextCode,
    promotionalVersionContextCode, propertyIID, nature, natureType, numberOfVersionsToMaintain, relationships, relationshipType, rows,
    sections, side1, side2, selectedTagValues, statusTags, subType, suffix, tab, tagCodes, tagType, tagValueSequence, tasks , type, rendererType,
    attribute, uniqueSelectors, valueAsHtml, levelCode, masterParentTag, firstName, lastName, userName, gender, email, contact,
    birthDate, icon, password, userIID, isBackgroundUser, isEmailLog, priorityTag, statusTag, levelLabels, taxonomyType, isLanguageDependent, attributes,
    types, tags, ruleViolations, normalizations, taxonomies, physicalCatalogIds, organizations, languages, entityId, rules,
    from, to, values, ruleListLinkId, attributeLinkId, klassLinkIds, shouldCompareWithSystemDate, tagValues, attributeOperatorList,
    calculatedAttributeUnit, calculatedAttributeUnitAsHTML, transformationType, baseType, valueAttributeId, attributeConcatenatedList,
    findText, replaceText, startIndex, endIndex, propertySequenceList, propertyCollectionCodes, variantContextCodes, relationshipCodes,sequence,
    klassIds, taxonomyIds, mergeEffect, natureRelationships, entityType, supplierIds, valueAsHTML, isEditable, previewImage,
    tooltip, side1Label, side2Label, physicalCatalogs, portals,endpointIds, systems, roleType, endpoints, kpis, targetKlasses,
    targetTaxonomies, isDashboardEnable, roles, users, enableAfterSave, languageTranslation, imageResolution, imageExtension,
    isBackgroundRole, extensionConfiguration, abbreviation, localeId, dateFormat, numberFormat, isDataLanguage, isDefaultLanguage, isUserInterfaceLanguage,
    index, id, attributeId, order, value, tagId, operator, calculatedAttributeType, isCodeVisible, hideSeparator, entityAttributeType, isNewlyCreatedLevel, 
    levelIndex, detectDuplicate, uploadZip, trackDownloads,isReadOnly,isLite, taxonomyInheritanceSetting,
    canEdit, canAdd, canDelete, viewName, canEditName, viewIcon, canChangeIcon, viewPrimaryType, canEditPrimaryType, viewAdditionalClasses, canAddClasses,
    canDeleteClasses, viewTaxonomies, canDeleteTaxonomy, canAddTaxonomy, viewStatusTags, canEditStatusTag, 
    viewCreatedOn, viewLastModifiedBy, canRead, canCreate, PropertyCollection, propertyPermissions, relationshipPermissions, globalPermission,
    headerPermission, roleId, relationshipId, propertyId, canDownload, permissionType, propertyType, preferredUILanguage, preferredDataLanguage

  }
  
}
