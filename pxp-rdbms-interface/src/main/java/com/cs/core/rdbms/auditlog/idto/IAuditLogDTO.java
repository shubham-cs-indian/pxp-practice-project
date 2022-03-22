package com.cs.core.rdbms.auditlog.idto;


import com.cs.core.technical.rdbms.idto.IRootDTO;

/**
 * DTO representation of audit log.
 *
 * @author Sumit Bhingardive
 */
public interface IAuditLogDTO extends IRootDTO {
  
  public static final String ACTIVITY_IID = "activityiid";
  public static final String DATE         = "date";
  public static final String USER_NAME    = "userName";
  public static final String IP_ADDRESS   = "ipAddress";
  public static final String ACTIVITY     = "activity";
  public static final String ENTITY_TYPE  = "entityType";
  public static final String ELEMENT      = "element";
  public static final String ELEMENT_TYPE = "elementType";
  public static final String ELEMENT_CODE = "elementCode";
  public static final String ELEMENT_NAME = "elementName";
  
  /**
   * @return activity id
   */
  public String getActivityIID();
  
  /**
   * @return timestamp in milliseconds format
   */
  public Long getDate();
  
  /**
   * @return username of the user who performed the activity
   */
  public String getUserName();
  
  /**
   * @return ipAddress of the user machine
   */
  public String getIpAddress();
  
  /**
   * @return type of activity (CREATED, DELETED, UPDATED)
   */
  public ServiceType getActivity();
  
  /**
   * @return type of entity 
   */
  public Entities getEntityType();
  
  /**
   * @return element of the entity
   */
  public Elements getElement();
  
  /**
   * @return type of element
   */
  public String getElementType();
  
  /**
   * @return code of the element
   */
  public String getElementCode();
  
  /**
   * @return name of the element
   */
  public String getElementName();
  
  /**
   * type of service for audit
   * @author faizan.siddique
   *
   */
  enum ServiceType
  {

    UNDEFINED, CREATED, DELETED, GET, LOGGING, MIGRATION, TALENDIMPORTEXPORT, UPDATED,IMPORT,EXPORT;
    
    private static final ServiceType[] values = values();
    
    public static ServiceType valueOf(int ordinal)
    {
      return values[ordinal];
    }
  }

  enum Entities
  {

    UNDEFINED, CLASSES, CONTEXT, GOLDEN_RECORD_RULE, KPI, LANGUAGE_TREE, PARTNERS, PROPERTIES, PROPERTY_GROUPS_MENU_ITEM_TITLE,
    RELATIONSHIPS, ROLES, RULELIST, RULES, SSO_SETTING, TAXONOMIES, TEMPLATES, USERS;

    private static final Entities[] values = values();

    public static Entities valueOf(int ordinal)
    {
      return values[ordinal];
    }
  }

  enum Elements
  {

    UNDEFINED, ARTICLE, ASSET, ATTRIBUTE, HIERARCHIES_TITLE, MARKET, MASTER_TAXONOMY_CONFIGURATION_TITLE,
    PROPERTY_COLLECTION, REFERENCES, RELATIONSHIPS, SUPPLIER, TABS_MENU_ITEM_TILE, TAGS,
    TEXT_ASSET, VIRTUAL_CATALOG;

    public static final Elements[] values = values();

    public static Elements valueOf(int ordinal)
    {
      return values[ordinal];
    }
  }

}











