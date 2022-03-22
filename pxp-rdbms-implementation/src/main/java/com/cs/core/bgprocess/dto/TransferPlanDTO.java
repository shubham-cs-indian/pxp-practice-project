package com.cs.core.bgprocess.dto;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.bgprocess.idto.ITransferPlanDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;

/**
 * Transfer plan specification
 *
 * @author vallee
 */
public class TransferPlanDTO extends BaseEntityPlanDTO implements ITransferPlanDTO {
  
  private static final String TARGET_CATALOG           = "targetCatalog";
  private static final String SOURCE_CATALOG           = "sourceCatalog";
  private static final String TARGET_ORGANIZATION_CODE = "targetOrgCode";
  private static final String SOURCE_ORGANIZATION_CODE = "sourceOrgCode";
  private static final String TARGET_ENDPOINT_CODE       = "targetEndPointCode";
  private static final String SOURCE_ENDPOINT_CODE       = "sourceEndPointCode";
  public static final String  AUTHORIZATION_MAPPING_ID   = "authorizationMappingId";
  public static final String  IS_REVISIONABLE_TRANSFER = "isRevisionableTransfer";
  
  private String              targetCatalog          = IStandardConfig.StandardCatalog.pim.toString();
  private String              sourceCatalog          = IStandardConfig.StandardCatalog.onboarding.toString();
  private String              targetOrganizationCode = IStandardConfig.STANDARD_ORGANIZATION_CODE;
  private String              sourceOrganizationCode = IStandardConfig.STANDARD_ORGANIZATION_CODE;
  private String              sourceEndPointCode;
  private String              targetEndPointCode;
  private String              authorizationMappingId;
  private Boolean             isRevisionableTransfer;
  
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    super.fromJSON(json);
    this.targetCatalog = json.getString(TARGET_CATALOG);
    this.sourceCatalog = json.getString(SOURCE_CATALOG);
    this.sourceOrganizationCode = json.getString(SOURCE_ORGANIZATION_CODE);
    this.targetOrganizationCode = json.getString(TARGET_ORGANIZATION_CODE);
    this.sourceEndPointCode = json.getString(SOURCE_ENDPOINT_CODE);
    this.targetEndPointCode = json.getString(TARGET_ENDPOINT_CODE).equals("null") ? "" : json.getString(TARGET_ENDPOINT_CODE);
    this.authorizationMappingId = json.getString(AUTHORIZATION_MAPPING_ID);
    this.isRevisionableTransfer = json.getBoolean(IS_REVISIONABLE_TRANSFER);
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(super.toJSONBuffer(),
        JSONBuilder.newJSONField(TARGET_CATALOG, this.targetCatalog),
        JSONBuilder.newJSONField(SOURCE_CATALOG, this.sourceCatalog),
        JSONBuilder.newJSONField(TARGET_ORGANIZATION_CODE, targetOrganizationCode),
        JSONBuilder.newJSONField(SOURCE_ORGANIZATION_CODE, sourceOrganizationCode),
        JSONBuilder.newJSONField(SOURCE_ENDPOINT_CODE, sourceEndPointCode),
        JSONBuilder.newJSONField(TARGET_ENDPOINT_CODE, targetEndPointCode),
        JSONBuilder.newJSONField(AUTHORIZATION_MAPPING_ID, authorizationMappingId),
        JSONBuilder.newJSONField(IS_REVISIONABLE_TRANSFER, isRevisionableTransfer));
  }
  
  @Override
  public String getTargetCatalogCode()
  {
    return targetCatalog;
  }
  
  @Override
  public void setTargetCatalogCode(String catalogCode)
  {
    targetCatalog = catalogCode;
  }

  @Override
  public String getSourceCatalogCode()
  {
    return this.sourceCatalog;
  }

  @Override
  public void setSourceCatalogCode(String catalogCode)
  {
    this.sourceCatalog = catalogCode;
  }
  
  @Override
  public String getSourceOrganizationCode()
  {
    return sourceOrganizationCode;
  }

  @Override
  public void setSourceOrganizationCode(String sourceOrganizationCode)
  {
    this.sourceOrganizationCode = sourceOrganizationCode;
  }
  
  @Override
  public String getTargetOrganizationCode()
  {
    return targetOrganizationCode;
  }

  @Override
  public void setTargetOrganizationCode(String targetOrganizationCode)
  {
    this.targetOrganizationCode = targetOrganizationCode;
  }

  
  public String getSourceEndPointCode()
  {
    return sourceEndPointCode;
  }

  
  public void setSourceEndPointCode(String sourceEndPointId)
  {
    this.sourceEndPointCode = sourceEndPointId;
  }

  
  public String getTargetEndPointCode()
  {
    return targetEndPointCode;
  }

  
  public void setTargetEndPointCode(String targetEndPointId)
  {
    this.targetEndPointCode = targetEndPointId;
  }
  
  @Override
  public String getAuthorizationMappingId()
  {
    return authorizationMappingId;
  }
  
  @Override
  public void setAuthorizationMappingId(String authorizationMappingId)
  {
    this.authorizationMappingId = authorizationMappingId;
  }
 
  @Override
  public Boolean getIsRevisionableTransfer()
  {
    if(isRevisionableTransfer == null) {
      return false;
    }
    return isRevisionableTransfer;
  }

  @Override
  public void setIsRevisionableTransfer(Boolean isRevisionableTransfer)
  {
    this.isRevisionableTransfer = isRevisionableTransfer;
  }

}
