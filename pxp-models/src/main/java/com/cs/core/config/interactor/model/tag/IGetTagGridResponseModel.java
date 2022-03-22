package com.cs.core.config.interactor.model.tag;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IGetTagGridResponseModel extends IModel {
  
  public static final String TAGS_LIST       = "tagsList";
  public static final String COUNT           = "count";
  public static final String REFERENCED_TAGS = "referencedTags";
  
  public Integer getCount();
  
  public void setCount(Integer count);
  
  public Map<String, IConfigEntityInformationModel> getReferencedTags();
  
  public void setReferencedTags(Map<String, IConfigEntityInformationModel> referencedTags);
  
  public List<ITag> getTagsList();
  
  public void setTagsList(List<ITag> tagsList);
}
