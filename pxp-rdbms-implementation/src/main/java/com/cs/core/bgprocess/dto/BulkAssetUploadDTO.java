package com.cs.core.bgprocess.dto;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;

import com.cs.core.bgprocess.idto.IAssetFileDTO;
import com.cs.core.bgprocess.idto.IBulkAssetUploadDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;

public class BulkAssetUploadDTO extends SimpleDTO implements IBulkAssetUploadDTO {
  
  private final String        ASSETS                      = "assets";
  private final String        LOCALE_ID                   = "localeID";
  private final String        USER_ID                     = "userId";
  private final String        USER_NAME                   = "userName";
  private final String        CATALOG_CODE                = "catalogCode";
  private static final String PORTAL_ID                   = "portalId";
  private static final String ORGANIZATION_CODE           = "organizationCode";
  private static final String ENDPOINT_ID                 = "endpointId";
  private static final String LOGICAL_CATALOG_ID          = "logicalCatalogId";
  private static final String PARENT_TRANSACTION_ID       = "parentTransactionId";
  private static final String SYSTEM_ID                   = "systemId";
  private static final String SHOULD_CHECK_FOR_REDUNDANCY = "shouldCheckForRedundancy";
  private static final String COLLECTION_IDS              = "collectionIds";
  private static final String BASE_TYPE                   = "baseType";
  private static final String DETECT_DUPLICATE            = "detectDuplicate";
  
   /* For relationship create */
  public static final String  SIDE_1_INSTANCE_ID          = "side1InstanceId";
  public static final String  SIDE_1_BASE_TYPE            = "side1BaseType";
  public static final String  TAB_ID                      = "tabId";
  public static final String  TAB_TYPE                    = "tabType";
  public static final String  RELATIONSHIP_ID             = "relationshipId";
  public static final String  RELATIONSHIP_ENTITY_ID      = "relationshipEntityId";
  public static final String  SIDE_ID                     = "sideId";
  public static final String  MODE                        = "mode";

  
  
  private List<IAssetFileDTO> assets                      = new ArrayList<>();
  private String              localeID;
  private String              userId;
  private String              userName;
  private String              catalogCode;
  private String              portalId;
  private String              organizationCode;
  private String              endpointId;
  private String              logicalCatalogId;
  private String              parentTransactionId;
  private String              systemId;
  private boolean             shouldCheckForRedundancy;
  protected List<String>      collectionIds               = new ArrayList<>();
  private String              baseType;
  private boolean             detectDuplicate             = false;
  /*Relationship create data*/
  private String              side1InstanceId;
  private String              side1BaseType;
  private String              tabType;
  private String              tabId;
  private String              relationshipId;
  private String              relationshipEntityId;
  private String              sideId;
  private String              mode;

  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(JSONBuilder.newJSONArray(ASSETS, assets),
        JSONBuilder.newJSONField(LOCALE_ID, localeID),
        JSONBuilder.newJSONField(USER_ID, userId),
        JSONBuilder.newJSONField(USER_NAME, userName),
        JSONBuilder.newJSONField(CATALOG_CODE, catalogCode),
        JSONBuilder.newJSONField(PORTAL_ID, portalId),
        JSONBuilder.newJSONField(ORGANIZATION_CODE, organizationCode),
        JSONBuilder.newJSONField(ENDPOINT_ID, endpointId),
        JSONBuilder.newJSONField(LOGICAL_CATALOG_ID, logicalCatalogId),
        JSONBuilder.newJSONField(PARENT_TRANSACTION_ID, parentTransactionId),
        JSONBuilder.newJSONField(SYSTEM_ID, systemId),
        JSONBuilder.newJSONField(SHOULD_CHECK_FOR_REDUNDANCY, shouldCheckForRedundancy),
        JSONBuilder.newJSONStringArray(COLLECTION_IDS, collectionIds),
        JSONBuilder.newJSONField(BASE_TYPE, baseType),
        JSONBuilder.newJSONField(DETECT_DUPLICATE, detectDuplicate),
        JSONBuilder.newJSONField(SIDE_1_INSTANCE_ID, side1InstanceId),
        JSONBuilder.newJSONField(TAB_ID, tabId),
        JSONBuilder.newJSONField(TAB_TYPE, tabType),
        JSONBuilder.newJSONField(SIDE_1_BASE_TYPE, side1BaseType),
        JSONBuilder.newJSONField(RELATIONSHIP_ID, relationshipId),
        JSONBuilder.newJSONField(RELATIONSHIP_ENTITY_ID, relationshipEntityId),
        JSONBuilder.newJSONField(SIDE_ID, sideId),
        JSONBuilder.newJSONField(MODE, mode)
        );
  }
  
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    assets.clear();
    JSONArray jsonArray = json.getJSONArray(ASSETS);
    for (Object jsonV : jsonArray) {
      AssetFileDTO assetFileDTO = new AssetFileDTO();
      assetFileDTO.fromJSON(jsonV.toString());
      assets.add(assetFileDTO);
    }
    localeID = json.getString(LOCALE_ID);
    userId = json.getString(USER_ID);
    userName = json.getString(USER_NAME);
    catalogCode = json.getString(CATALOG_CODE);
    portalId = json.getString(PORTAL_ID);
    organizationCode = json.getString(ORGANIZATION_CODE);
    endpointId = json.getString(ENDPOINT_ID);
    logicalCatalogId = json.getString(LOGICAL_CATALOG_ID);
    parentTransactionId = json.getString(PARENT_TRANSACTION_ID);
    systemId = json.getString(SYSTEM_ID);
    shouldCheckForRedundancy = json.getBoolean(SHOULD_CHECK_FOR_REDUNDANCY);
    collectionIds = json.getJSONArray(COLLECTION_IDS);
    baseType = json.getString(BASE_TYPE);
    detectDuplicate = json.getBoolean(DETECT_DUPLICATE);
    side1InstanceId = json.getString(SIDE_1_INSTANCE_ID);
    side1BaseType = json.getString(SIDE_1_BASE_TYPE);
    tabId = json.getString(TAB_ID);
    tabType = json.getString(TAB_TYPE);
    relationshipId = json.getString(RELATIONSHIP_ID);
    relationshipEntityId = json.getString(RELATIONSHIP_ENTITY_ID);
    sideId = json.getString(SIDE_ID);
    mode = json.getString(MODE);
  }
  
  @Override
  public void setAssets(List<IAssetFileDTO> assets)
  {
    this.assets = assets;
  }
  
  @Override
  public List<IAssetFileDTO> getAssets()
  {
    return assets;
  }
  
  @Override
  public String getLocaleID()
  {
    return localeID;
  }
  
  @Override
  public void setLocaleID(String localeID)
  {
    this.localeID = localeID;
  }
  
  @Override
  public String getUserId()
  {
    return userId;
  }
  
  @Override
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
  
  @Override
  public String getUserName()
  {
    return userName;
  }

  @Override
  public void setUserName(String userName)
  {
    this.userName = userName;
  }
  
  @Override
  public String getCatalogCode()
  {
    return catalogCode;
  }
  
  @Override
  public void setCatalogCode(String physicalCatalogId)
  {
    this.catalogCode = physicalCatalogId;
  }
  
  @Override
  public String getPortalId()
  {
    return portalId;
  }
  
  @Override
  public void setPortalId(String portalId)
  {
    this.portalId = portalId;
  }
  
  @Override
  public String getOrganizationCode()
  {
    return organizationCode;
  }
  
  @Override
  public void setOrganizationCode(String organizationCode)
  {
    this.organizationCode = organizationCode;
  }
  
  @Override
  public String getEndpointId()
  {
    return endpointId;
  }
  
  @Override
  public void setEndpointId(String endpointId)
  {
    this.endpointId = endpointId;
  }
  
  @Override
  public String getLogicalCatalogId()
  {
    return logicalCatalogId;
  }
  
  @Override
  public void setLogicalCatalogId(String logicalCatelogId)
  {
    this.logicalCatalogId = logicalCatelogId;
  }
  
  @Override
  public String getParentTransactionId()
  {
    return parentTransactionId;
  }
  
  @Override
  public void setParentTransactionId(String parentTransactionId)
  {
    this.parentTransactionId = parentTransactionId;
  }
  
  @Override
  public String getSystemId()
  {
    return systemId;
  }
  
  @Override
  public void setSystemId(String systemId)
  {
    this.systemId = systemId;
  }
  
  @Override
  public boolean getShouldCheckForRedundancy()
  {
    return shouldCheckForRedundancy;
  }
  
  @Override
  public void setShouldCheckForRedundancy(boolean shouldCheckForRedundancy)
  {
    this.shouldCheckForRedundancy = shouldCheckForRedundancy;
  }
  
  @Override
  public List<String> getCollectionIds()
  {
    return collectionIds;
  }
  
  @Override
  public void setCollectionIds(List<String> collectionIds)
  {
    this.collectionIds = collectionIds;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public boolean getDetectDuplicate()
  {
    return detectDuplicate;
  }
  
  @Override
  public void setDetectDuplicate(boolean detectDuplicate)
  {
    this.detectDuplicate = detectDuplicate;
  }
  
  @Override
  public String getSide1InstanceId()
  {
    return side1InstanceId;
  }
  
  @Override
  public void setSide1InstanceId(String id)
  {
    this.side1InstanceId = id;
  }
  
  @Override
  public String getSide1BaseType()
  {
    return side1BaseType;
  }
  
  @Override
  public void setSide1BaseType(String side1baseType)
  {
    this.side1BaseType = side1baseType;
  }
  
  @Override
  public String getTabId()
  {
    return tabId;
  }
  
  @Override
  public void setTabId(String tabId)
  {
    this.tabId = tabId;
  }
  
  @Override
  public String getTabType()
  {
    return tabType;
  }
  
  @Override
  public void setTabType(String tabType)
  {
    this.tabType = tabType;
  }

  @Override
  public String getRelationshipId()
  {
    return relationshipId;
  }

  @Override
  public void setRelationshipId(String relationshipId)
  {
    this.relationshipId = relationshipId;
  }

  @Override
  public String getRelationshipEntityId()
  {
    return relationshipEntityId;
  }

  @Override
  public void setRelationshipEntityId(String relationshipEntityId)
  {
    this.relationshipEntityId = relationshipEntityId;
  }

  @Override
  public String getSideId()
  {
    return sideId;
  }

  @Override
  public void setSideId(String sideId)
  {
    this.sideId = sideId;
  }
  
  @Override
  public String getMode()
  {
    return mode;
  }
  
  @Override
  public void setMode(String mode)
  {
    this.mode = mode;
  }

}
