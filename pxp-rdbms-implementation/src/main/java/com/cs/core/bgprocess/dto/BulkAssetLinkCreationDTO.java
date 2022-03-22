package com.cs.core.bgprocess.dto;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;

import com.cs.core.bgprocess.idto.IBulkAssetLinkCreationDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;


public class BulkAssetLinkCreationDTO extends SimpleDTO implements IBulkAssetLinkCreationDTO {
  
  /**
   * DTO for BulkAssetLinkCreation service
   */
  private static final long serialVersionUID = 1L;
  private static String MASTER_ASSET_IDS           = "masterAssetIds";
  private static String TECHNICAL_VARIANT_TYPE_IDS = "technicalVariantTypeIds";
  private static String MASTER_ASSET_SHARE         = "masterAssetShare";
  private static String PHYSICAL_CATALOG_ID        = "physicalCatalogId";
  private static String DATA_LANGUAGE              = "dataLanguage";
  private static String ORGANIZATION_ID            = "organizationId";
  
  private List<String>  masterAssetIds             = new ArrayList<>();
  private List<String>  technicalVariantTypeIds    = new ArrayList<>();
  private Boolean       masterAssetShare;
  private String        physicalCatalogId;
  private String        dataLanguage;
  private String        organizationId;

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(
        JSONBuilder.newJSONStringArray(MASTER_ASSET_IDS, masterAssetIds),
        JSONBuilder.newJSONStringArray(TECHNICAL_VARIANT_TYPE_IDS, technicalVariantTypeIds),
        JSONBuilder.newJSONField(MASTER_ASSET_SHARE, masterAssetShare),
        JSONBuilder.newJSONField(PHYSICAL_CATALOG_ID, physicalCatalogId),
        JSONBuilder.newJSONField(DATA_LANGUAGE, dataLanguage),
        JSONBuilder.newJSONField(ORGANIZATION_ID, organizationId)
        );
  }
  

  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    masterAssetIds.clear();
    technicalVariantTypeIds.clear();
    JSONArray masterAssetsJsonArray = json.getJSONArray(MASTER_ASSET_IDS);
    for(Object masterAsset : masterAssetsJsonArray)
    {
      masterAssetIds.add((String)masterAsset);
    }
    JSONArray technicalVariantJsonArray = json.getJSONArray(TECHNICAL_VARIANT_TYPE_IDS);
    for(Object technicalVariant : technicalVariantJsonArray)
    {
      technicalVariantTypeIds.add((String)technicalVariant);
    }
    masterAssetShare = json.getBoolean(MASTER_ASSET_SHARE);
    physicalCatalogId = json.getString(PHYSICAL_CATALOG_ID);
    dataLanguage = json.getString(DATA_LANGUAGE);
    organizationId = json.getString(ORGANIZATION_ID);
    
  }

  @Override
  public List<String> getMasterAssetIds()
  {
    return masterAssetIds;
  }

  @Override
  public void setMasterAssetIds(List<String> masterAssetIds)
  {
    this.masterAssetIds = masterAssetIds;
  }

  @Override
  public List<String> getTechnicalVariantTypeIds()
  {
    return technicalVariantTypeIds;
  }

  @Override
  public void setTechnicalVariantTypeIds(List<String> technicalVariantTypeIds)
  {
    this.technicalVariantTypeIds = technicalVariantTypeIds;
  }

  @Override
  public Boolean getMasterAssetShare()
  {
    return masterAssetShare;
  }

  @Override
  public void setMasterAssetShare(Boolean masterAssetShare)
  {
    this.masterAssetShare = masterAssetShare;
  }
  
  @Override
  public String getPhysicalCatalogId()
  {
    return physicalCatalogId;
  }
  
  @Override
  public void setPhysicalCatalogId(String physicalCatalogId)
  {
    this.physicalCatalogId = physicalCatalogId;
  }
  
  @Override
  public String getDataLanguage()
  {
    return dataLanguage;
  }
  
  @Override
  public void setDataLanguage(String dataLanguage)
  {
    this.dataLanguage = dataLanguage;
  }
  
  @Override
  public String getOrganizationId()
  {
    return organizationId;
  }

  @Override
  public void setOrganizationId(String organizationId)
  {
    this.organizationId = organizationId;
  }

  
}
