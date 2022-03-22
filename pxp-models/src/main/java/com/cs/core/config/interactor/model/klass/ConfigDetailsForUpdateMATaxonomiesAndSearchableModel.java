package com.cs.core.config.interactor.model.klass;

import java.util.List;

public class ConfigDetailsForUpdateMATaxonomiesAndSearchableModel
    implements IConfigDetailsForUpdateMATaxonomiesAndSearchableModel {
  
  private static final long serialVersionUID = 1L;
  
  protected List<String>    readOnlyAttributeIds;
  protected List<String>    readOnlyTagIds;
  protected Integer         numberOfVersionToMaintain;
  protected List<String>    taxonomyIdsToTransfer;
  
  @Override
  public List<String> getReadOnlyAttributeIds()
  {
    return readOnlyAttributeIds;
  }
  
  @Override
  public void setReadOnlyAttributeIds(List<String> readOnlyAttributeIds)
  {
    this.readOnlyAttributeIds = readOnlyAttributeIds;
  }
  
  @Override
  public List<String> getReadOnlyTagIds()
  {
    return readOnlyTagIds;
  }
  
  @Override
  public void setReadOnlyTagIds(List<String> readOnlyTagIds)
  {
    this.readOnlyTagIds = readOnlyTagIds;
  }
  
  @Override
  public List<String> getTaxonomyIdsToTransfer()
  {
    return taxonomyIdsToTransfer;
  }
  
  @Override
  public void setTaxonomyIdsToTransfer(List<String> taxonomyIdsToTransfer)
  {
    this.taxonomyIdsToTransfer = taxonomyIdsToTransfer;
  }
  
  @Override
  public Integer getNumberOfVersionToMaintain()
  {
    return numberOfVersionToMaintain;
  }
  
  @Override
  public void setNumberOfVersionToMaintain(Integer numberOfVersionToMaintain)
  {
    this.numberOfVersionToMaintain = numberOfVersionToMaintain;
  }
}
