package com.cs.core.config.interactor.model.tag;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IBulkCreateTagValuesSuccessModel extends IModel {
  
  public static final String TAGS            = "tags";
  public static final String REFERENCED_TAGS = "referencedTags";
  
  public List<ITagModel> getTags();
  
  public void setTags(List<ITagModel> tags);
  
  public Map<String, IConfigEntityInformationModel> getReferencedTags();
  
  public void setReferencedTags(Map<String, IConfigEntityInformationModel> referencedTags);
}
