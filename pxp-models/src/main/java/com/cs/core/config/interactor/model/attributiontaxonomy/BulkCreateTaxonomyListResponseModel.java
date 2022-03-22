package com.cs.core.config.interactor.model.attributiontaxonomy;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkCreateTaxonomyListResponseModel implements IBulkCreateTaxonomyListResponseModel {
  
  public BulkCreateTaxonomyListResponseModel()
  {
    
  }
  
  private static final long                                       serialVersionUID = 1L;
  
  protected List<IGetAttributionTaxonomyModel>                    list;
  protected Map<String, IConfigTaxonomyHierarchyInformationModel> referencedTaxonomies;
  
  public List<IGetAttributionTaxonomyModel> getList()
  {
    return list;
  }
  
  @JsonDeserialize(contentAs = GetAttributionTaxonomyModel.class)
  public void setList(List<IGetAttributionTaxonomyModel> list)
  {
    this.list = list;
  }
  
  public Map<String, IConfigTaxonomyHierarchyInformationModel> getReferencedTaxonomies()
  {
    return referencedTaxonomies;
  }
  
  @JsonDeserialize(contentAs = ConfigTaxonomyHierarchyInformationModel.class)
  public void setReferencedTaxonomies(
      Map<String, IConfigTaxonomyHierarchyInformationModel> referencedTaxonomies)
  {
    this.referencedTaxonomies = referencedTaxonomies;
  }
  
}
