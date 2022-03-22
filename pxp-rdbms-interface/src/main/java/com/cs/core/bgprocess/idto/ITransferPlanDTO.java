package com.cs.core.bgprocess.idto;

/**
 * Specification for base entity transfer
 *
 * @author vallee
 */
public interface ITransferPlanDTO extends IBaseEntityPlanDTO {

  /**
   * @return the target catalog of transfer
   */
  public String getTargetCatalogCode();

  /**
   * @param catalogCode overwritten target catalog of transfer
   */
  public void setTargetCatalogCode(String catalogCode);
  
  /**
   * @return the source catalog of transfer
   */
  public String getSourceCatalogCode();

  /**
   * @param catalogCode overwritten source catalog of transfer
   */
  public void setSourceCatalogCode(String catalogCode);
  
  /**
   * @return target organization code
   */
  public String getTargetOrganizationCode();
  
  /**
   * code of the target organization
   * @param organizationCode
   */
  public void setTargetOrganizationCode(String organizationCode);
  
  /**
   * @return source organization code
   */
  public String getSourceOrganizationCode();
  
  /**
   * code of the source organization
   * @param organizationCode
   */
  public void setSourceOrganizationCode(String organizationCode);
  
  public String getSourceEndPointCode();
 
  public void setSourceEndPointCode(String sourceEndPointId);

  public String getTargetEndPointCode();
  
  public void setTargetEndPointCode(String targetEndPointId);

  public String getAuthorizationMappingId();

  public void setAuthorizationMappingId(String authorizationMappingId); 

  public Boolean getIsRevisionableTransfer();  
  public void setIsRevisionableTransfer(Boolean isRevisionableTransfer);

}
