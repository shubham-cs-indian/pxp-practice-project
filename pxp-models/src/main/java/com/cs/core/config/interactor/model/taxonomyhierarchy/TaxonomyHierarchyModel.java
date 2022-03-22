package com.cs.core.config.interactor.model.taxonomyhierarchy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@SuppressWarnings("serial")
public class TaxonomyHierarchyModel implements ITaxonomyHierarchyModel {
  
  protected List<IConfigTaxonomyHierarchyInformationModel>        list;
  protected Map<String, IConfigTaxonomyHierarchyInformationModel> configDetails;
  protected Long                                                  count;
  
  
  public Map<String, IConfigTaxonomyHierarchyInformationModel> getConfigDetails()
  {
    if (configDetails == null) {
      configDetails = new HashMap<>();
    }
    return configDetails;
  }
  
  @JsonDeserialize(contentAs = ConfigTaxonomyHierarchyInformationModel.class)
  public void setConfigDetails(Map<String, IConfigTaxonomyHierarchyInformationModel> configDetails)
  {
    this.configDetails = configDetails;
  }
  
  public List<IConfigTaxonomyHierarchyInformationModel> getList()
  {
    if (list == null) {
      list = new ArrayList<>();
    }
    return list;
  }
  
  @JsonDeserialize(contentAs = ConfigTaxonomyHierarchyInformationModel.class)
  public void setList(List<IConfigTaxonomyHierarchyInformationModel> list)
  {
    this.list = list;
  }

  @Override
  public Long getCount()
  {
    return count;
  }
  
  @Override
  public void setCount(Long count)
  {
    this.count = count;
  }
}
