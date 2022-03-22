package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IFilterDataModel extends IModel {
  
  public static final String TAGS       = "tags";
  public static final String ATTRIBUTES = "attributes";
  
  public List<IConfigEntityTreeInformationModel> getTags();
  
  public void setTags(List<IConfigEntityTreeInformationModel> tags);
  
  public List<IConfigEntityTreeInformationModel> getAttributes();
  
  public void setAttributes(List<IConfigEntityTreeInformationModel> attributes);
}
