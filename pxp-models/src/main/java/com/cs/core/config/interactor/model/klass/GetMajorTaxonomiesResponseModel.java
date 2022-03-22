package com.cs.core.config.interactor.model.klass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.model.taxonomyhierarchy.ConfigTaxonomyHierarchyInformationModel;
import com.cs.core.config.interactor.model.taxonomyhierarchy.IConfigTaxonomyHierarchyInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetMajorTaxonomiesResponseModel implements IGetMajorTaxonomiesResponseModel {
  
  private static final long                 serialVersionUID = 1L;
  
  protected List<IConfigTaxonomyHierarchyInformationModel>        list;
  protected Map<String, IConfigTaxonomyHierarchyInformationModel> configDetails    = new HashMap<>();
  protected Long                                                  count;
  
  @Override
  public List<IConfigTaxonomyHierarchyInformationModel> getList()
  {
    if (list == null) {
      list = new ArrayList<>();
    }
    return list;
  }
  
  @JsonDeserialize(contentAs = ConfigTaxonomyHierarchyInformationModel.class)
  @Override
  public void setList(List<IConfigTaxonomyHierarchyInformationModel> taskList)
  {
    this.list = taskList;
  }
  
  @Override
  public Map<String, IConfigTaxonomyHierarchyInformationModel> getConfigDetails()
  {
    return configDetails;
  }
  
  @JsonDeserialize(contentAs = ConfigTaxonomyHierarchyInformationModel.class)
  @Override
  public void setConfigDetails(Map<String, IConfigTaxonomyHierarchyInformationModel> configDetails)
  {
    this.configDetails = configDetails;
  }
  
  @Override
  public Long getCount()
  {
    if (count == null) {
      count = new Long(0);
    }
    return count;
  }
  
  @Override
  public void setCount(Long count)
  {
    this.count = count;
  }  
}
