package com.cs.core.bgprocess.idto;

import java.util.List;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;


public interface IBulkAssetLinkCreationDTO extends ISimpleDTO {
  
  public List<String> getMasterAssetIds();
  public void setMasterAssetIds(List<String> masterAssetIds);
  
  public List<String> getTechnicalVariantTypeIds();
  public void setTechnicalVariantTypeIds(List<String> technicalVariantTypeIds);
  
  public Boolean getMasterAssetShare();
  public void setMasterAssetShare(Boolean masterAssetShare);

  public String getPhysicalCatalogId();
  public void setPhysicalCatalogId(String physicalCatalogId);
  
  public String getDataLanguage();
  public void setDataLanguage(String dataLanguage);
  
  public String getOrganizationId();
  public void setOrganizationId(String organizationId);
  
  
}
