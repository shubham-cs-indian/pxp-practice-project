package com.cs.core.bgprocess.dto;

import com.cs.core.bgprocess.idto.IAssetExpirationDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;

public class AssetExpirationDTO extends SimpleDTO implements IAssetExpirationDTO {
  
  /**
   * 
   */
  private static final long serialVersionUID    = 1L;
  private static String     TIME_STAMP          = "timeStamp";
  private static String     PHYSICAL_CATALOG_ID = "physicalCatalogId";
  private static String     DATA_LANGUAGE       = "dataLanguage";
  private static String     ORGANIZATION_ID     = "organizationId";
  
  private Long              timeStamp;
  private String            physicalCatalogId;
  private String            dataLanguage;
  private String            organizationId;
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(
        JSONBuilder.newJSONField(TIME_STAMP, timeStamp),
        JSONBuilder.newJSONField(PHYSICAL_CATALOG_ID, physicalCatalogId), 
        JSONBuilder.newJSONField(DATA_LANGUAGE, dataLanguage),
        JSONBuilder.newJSONField(ORGANIZATION_ID, organizationId)
        );
  }
  
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    timeStamp = json.getLong(TIME_STAMP);
    physicalCatalogId = json.getString(PHYSICAL_CATALOG_ID);
    dataLanguage = json.getString(DATA_LANGUAGE);
    organizationId = json.getString(ORGANIZATION_ID);
  }
  
  @Override
  public Long getTimeStamp()
  {
    return timeStamp;
  }
  
  @Override
  public void setTimeStamp(Long timeStamp)
  {
    this.timeStamp = timeStamp;
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
