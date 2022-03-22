package com.cs.config.idto;

import java.util.Arrays;
import java.util.Optional;

import com.cs.core.technical.ijosn.IJSONContent;

public interface IConfigTranslationDTO extends IConfigJSONDTO {
  
  public void setType(EntityType entityType);
  
  public EntityType getEntityType();
  
  public void setCode(String entityCode);
  
  public IJSONContent getTranslations();
  
  public void setTranslations(IJSONContent translations);
  
  public enum EntityType
  {
    UNDEFINED("??"), TAG("tag"), TAG_VALUES("tagValues"), ATTRIBUTE("attribute"), CONTEXT("context"), TASK("task"),
    PROPERTY_COLLECTION("propertyCollection"), ARTICLE("article"), ASSET("asset"), TARGET("target"), TEXT_ASSET("textAsset"),
    SUPPLIER("supplier"), VIRTUAL_CATALOG("virtualCatalog"), TEMPLATE("template"),
    HIERARCHY_TAXONOMY("hierarchyTaxonomy"), ROLE("role"), RULE("rule"), USER("user"),
    ATTRIBUTION_TAXONOMY("attributionTaxonomy"), RULE_LIST("ruleList"), RELATIONSHIP("relationship"), ORGANIZATION("organization"),
    SYSTEM("system"), TAB("tab"), DASHBOARD_TAB("dashboardTab"), GOLDEN_RECORDS("goldenRecords"),
    LANGUAGE("language"), REFERENCE("reference"), STATIC_TRANSLATION("staticTranslation"), ENDPOINT("endpoint"), 
    KEYPERFORMANCEINDEX("keyperformanceindex"), MAPPING("mapping"), PROCESS_EVENT("Process_Event"), KLASS("klass");
    
    private final String type;
    
    private EntityType(String type)
    {
      this.type = type;
    }
    
    private static final EntityType[] values = values();
    
    public String getEntityType()
    {
      return type;
    }
    
    public static EntityType valueOfEntityType(String entityTypePath) {
      Optional<EntityType> firstResult = Arrays.stream(values())
          .filter(x -> x.getEntityType()
              .equals(entityTypePath))
          .findFirst();

      return firstResult.orElse(UNDEFINED);
    }
    
    public static EntityType valueOf(int ordinal)
    {
      return values[ordinal];
    }
  };
  
}
