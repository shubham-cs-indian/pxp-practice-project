package com.cs.core.bgprocess.services.dataintegration;

import com.cs.config.standard.IConfigClass;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.icsexpress.ICSEElement;

import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

enum ImportSteps {
  IMPORT_CONTEXT("contexts"), IMPORT_ATTRIBUTE("attributes"), IMPORT_TAG("tags"), IMPORT_RELATIONSHIP("relationships"), IMPORT_CLASSIFIER(
      "classifiers"), IMPORT_MASTER_TAXONOMY("master taxonomies"), IMPORT_HIERARCHY_TAXONOMY(
      "hierarchy taxonomy"), IMPORT_PROPERTY_COLLECTION("property collections"), IMPORT_BASEENTITY("entities"), IMPORT_USER(
      "users"), IMPORT_TASK("tasks"), IMPORT_DATA_RULE("rules"), IMPORT_TAB("tabs"), IMPORT_GOLDEN_RULES("goldenrules"),
      IMPORT_ORGANIZATION("partners"), IMPORT_TRANSLATION("translations"), IMPORT_LANGUAGE("languages"), IMPORT_PERMISSION("permissions") ;

  private final String label;

  private static final ImportSteps[] values = values();

  ImportSteps(String label)
  {
    this.label = label;
  }

  public static List<String> getLabels()
  {
    return Arrays.stream(values()).map(ImportSteps::getImportLabel).collect(Collectors.toList());
  }

  public static ImportSteps valueOf(int idx)
  {
    return values[idx];
  }

  private String getImportLabel()
  {
    return String.format("Import %s",this.label);
  }

  public static ImportSteps getStepFromPXON(JSONObject pxonObject, String specificationType, ICSEElement.CSEObjectType cseObjectType)
      throws CSInitializationException
  {
    ImportSteps currentStep = null;
    switch (cseObjectType) {
      case Entity:
        // identifier = cseObject.getSpecification(ICSEElement.Keyword.$id);
        return ImportSteps.IMPORT_BASEENTITY;

      case Classifier:
        switch (IClassifierDTO.ClassifierType.valueOf(specificationType)) {
          case CLASS:
            return ImportSteps.IMPORT_CLASSIFIER;
          case TAXONOMY:
            return ImportSteps.IMPORT_MASTER_TAXONOMY;
        // TODO: PXPFDEV-21215 : Deprecate - Taxonomy Hierarchies -- Remove it after usage checking
          /*case HIERARCHY:
            return ImportSteps.IMPORT_HIERARCHY_TAXONOMY;*/
          case MINOR_TAXONOMY:
            String subType = (String) pxonObject.get(IPXON.PXONTag.subtype.toPrivateTag());
            if (IConfigClass.ClassifierClass.HIERARCHY_KLASS_TYPE.name().equals(subType))
              return ImportSteps.IMPORT_HIERARCHY_TAXONOMY;
            else
              return ImportSteps.IMPORT_MASTER_TAXONOMY;
        }

      case Property:

        PropertyType propertyType = IPropertyDTO.PropertyType.valueOf(specificationType);
        IPropertyDTO.SuperType propertySuperType = propertyType.getSuperType();
        switch (propertySuperType) {
          case ATTRIBUTE:
            return ImportSteps.IMPORT_ATTRIBUTE;
          case TAGS:
            return ImportSteps.IMPORT_TAG;
          case RELATION_SIDE:
            return ImportSteps.IMPORT_RELATIONSHIP;
        }

      case Context:
        return ImportSteps.IMPORT_CONTEXT;

      case PropertyCollection:
        return ImportSteps.IMPORT_PROPERTY_COLLECTION;

      case User:
        return ImportSteps.IMPORT_USER;

      case Task:
        return ImportSteps.IMPORT_TASK;

      case Rule:
        return ImportSteps.IMPORT_DATA_RULE;

      case Tab:
        return ImportSteps.IMPORT_TAB;
        
      case Golden_Rule :
        return ImportSteps.IMPORT_GOLDEN_RULES;
        
      case Organization:
        return ImportSteps.IMPORT_ORGANIZATION;
        
      case Translation:
        return ImportSteps.IMPORT_TRANSLATION;

      case LanguageConf:
        return ImportSteps.IMPORT_LANGUAGE;
        
      case Permission:
        return ImportSteps.IMPORT_PERMISSION;
        
      default:
        throw new CSInitializationException("Unknown Object Type");
    }
  }

  public static IImportEntity getImporter(ImportSteps step)
  {
    switch (step) {
      case IMPORT_BASEENTITY:
        return new ImportBaseEntity();
      case IMPORT_ATTRIBUTE:
        return new ImportAttributes();
      case IMPORT_TAG:
        return new ImportTags();
      case IMPORT_RELATIONSHIP:
        return new ImportRelationships();
      case IMPORT_CLASSIFIER:
        return new ImportClassifier();
      case IMPORT_CONTEXT:
        return new ImportContexts();
      case IMPORT_HIERARCHY_TAXONOMY:
        return new ImportHierarchyTaxonomy();
      case IMPORT_MASTER_TAXONOMY:
        return new ImportMasterTaxonomy();
      case IMPORT_PROPERTY_COLLECTION:
        return new ImportPropertyCollections();
      case IMPORT_USER:
        return new ImportUsers();
      case IMPORT_TASK:
        return new ImportTasks();
      case IMPORT_DATA_RULE:
        return new ImportDataRule();
      case IMPORT_TAB:
        return new ImportTabs();
      case IMPORT_GOLDEN_RULES :
        return new ImportGoldenRecordRule();
      case IMPORT_ORGANIZATION:
        return new ImportOrganization();
      case IMPORT_TRANSLATION:
        return new ImportTranslations();
      case IMPORT_LANGUAGE:
        return new ImportLanguages();
      case IMPORT_PERMISSION:
        return new ImportPermissions();
      default:
        RDBMSLogger.instance().info("Nothing to export");
        return null;
    }
  }

}


