package com.cs.core.config.interactor.model.transfer;

import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetVariantsForArticleResponseModel implements IGetVariantsForArticleResponseModel {
  
  private static final long                  serialVersionUID = 1L;
  
  private String                             batchId;
  private IGetConfigDetailsForCustomTabModel configDetails;
  private Set<String>                        klassIds;
  private Set<String>                        taxonomyIds;
  private Map<String, String>                sourceDestinationIds;
  private List<String>                       embeddedVariantIds;
  private List<String>                       attributeVariantIds;
  private String                             parentId;
  private String                             klassInstanceId;
  private Set<String>                        path;
  private String                             variantInstanceId;
  
  @Override
  public String getBatchId()
  {
    return batchId;
  }
  
  @Override
  public void setBatchId(String batchId)
  {
    this.batchId = batchId;
  }
  
  @Override
  public IGetConfigDetailsForCustomTabModel getConfigDetails()
  {
    return configDetails;
  }
  
  @Override
  public void setConfigDetails(IGetConfigDetailsForCustomTabModel configDetails)
  {
    this.configDetails = configDetails;
  }
  
  @Override
  public Map<String, String> getSourceDestinationIds()
  {
    return sourceDestinationIds;
  }
  
  @Override
  public void setSourceDestinationIds(Map<String, String> sourceDestinationIds)
  {
    this.sourceDestinationIds = sourceDestinationIds;
  }
  
  @Override
  public List<String> getEmbeddedVariantIds()
  {
    return embeddedVariantIds;
  }
  
  @Override
  public void setEmbeddedVariantIds(List<String> embeddedVariantIds)
  {
    this.embeddedVariantIds = embeddedVariantIds;
  }
  
  @Override
  public Set<String> getKlassIds()
  {
    return klassIds;
  }
  
  @Override
  public void setKlassIds(Set<String> klassIds)
  {
    this.klassIds = klassIds;
  }
  
  @Override
  public Set<String> getTaxonomyIds()
  {
    return taxonomyIds;
  }
  
  @Override
  public void setTaxonomyIds(Set<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
  }
  
  @Override
  public String getParentId()
  {
    return parentId;
  }
  
  @Override
  public void setParentId(String parentId)
  {
    this.parentId = parentId;
  }
  
  @Override
  public String getKlassInstanceId()
  {
    return klassInstanceId;
  }
  
  @Override
  public void setKlassInstanceId(String klassInstanceId)
  {
    this.klassInstanceId = klassInstanceId;
  }
  
  @Override
  public Set<String> getPath()
  {
    return path;
  }
  
  @Override
  public void setPath(Set<String> path)
  {
    this.path = path;
  }
  
  @Override
  public List<String> getAttributeVariantIds()
  {
    return attributeVariantIds;
  }
  
  @Override
  public void setAttributeVariantIds(List<String> attributeVariantIds)
  {
    this.attributeVariantIds = attributeVariantIds;
  }
  
  @Override
  public String getVariantInstanceId()
  {
    return variantInstanceId;
  }
  
  @Override
  public void setVariantInstanceId(String variantInstanceId)
  {
    this.variantInstanceId = variantInstanceId;
  }
}
