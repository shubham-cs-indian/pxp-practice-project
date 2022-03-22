package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.model.configuration.ContentIdentifierModel;
import com.cs.core.runtime.interactor.model.configuration.IContentIdentifierModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class BucketKlassInstanceInfoModel implements IBucketKlassInstanceInfoModel {
  
  private static final long         serialVersionUID = 1L;
  protected String                  id;
  protected String                  name;
  protected String                  defaultAssetInstanceId;
  protected List<String>            taxonomyIds;
  protected List<String>            types;
  protected IContentIdentifierModel partnerSource;
  protected String                  branchOfLabel;
  protected String                  variantOfLabel;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getName()
  {
    return name;
  }
  
  @Override
  public void setName(String name)
  {
    this.name = name;
  }
  
  @Override
  public String getDefaultAssetInstanceId()
  {
    return defaultAssetInstanceId;
  }
  
  @Override
  public void setDefaultAssetInstanceId(String defaultAssetInstanceId)
  {
    this.defaultAssetInstanceId = defaultAssetInstanceId;
  }
  
  @Override
  public List<String> getTaxonomyIds()
  {
    return taxonomyIds;
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
  }
  
  @Override
  public List<String> getTypes()
  {
    return types;
  }
  
  @Override
  public void setTypes(List<String> types)
  {
    this.types = types;
  }
  
  @Override
  public IContentIdentifierModel getPartnerSource()
  {
    return partnerSource;
  }
  
  @JsonDeserialize(as = ContentIdentifierModel.class)
  @Override
  public void setPartnerSource(IContentIdentifierModel partnerSource)
  {
    this.partnerSource = partnerSource;
  }
  
  @Override
  public String getBranchOfLabel()
  {
    return branchOfLabel;
  }
  
  @Override
  public void setBranchOfLabel(String branchOfLabel)
  {
    this.branchOfLabel = branchOfLabel;
  }
  
  @Override
  public String getVariantOfLabel(){
    return variantOfLabel;
  }
  
  @Override
  public void setVariantOfLabel(String variantOfLabel){
    this.variantOfLabel = variantOfLabel;
  }
}
