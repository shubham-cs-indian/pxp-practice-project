package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.config.interactor.entity.matchandmerge.AbstractSection;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.model.klass.DefaultValueChangeModel;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetStaticCollectionInfoModel implements IGetStaticCollectionInfoModel {
  
  private static final long                serialVersionUID  = 1L;
  
  protected List<String>                   childrenIds       = new ArrayList<>();
  protected List<? extends ISection>       sections          = new ArrayList<>();
  protected Map<String, List<String>>      deletedPropertiesFromSource;
  protected List<IDefaultValueChangeModel> defaultValuesDiff = new ArrayList<>();
  protected Boolean                        isCollectionMovable;
  
  @Override
  public List<String> getChildrenIds()
  {
    return childrenIds;
  }
  
  @Override
  public void setChildrenIds(List<String> childrenIds)
  {
    this.childrenIds = childrenIds;
  }
  
  @Override
  public List<? extends ISection> getSections()
  {
    return sections;
  }
  
  @JsonDeserialize(contentAs = AbstractSection.class)
  @Override
  public void setSections(List<? extends ISection> sections)
  {
    this.sections = sections;
  }
  
  @Override
  public List<IDefaultValueChangeModel> getDefaultValuesDiff()
  {
    return defaultValuesDiff;
  }
  
  @JsonDeserialize(contentAs = DefaultValueChangeModel.class)
  @Override
  public void setDefaultValuesDiff(List<IDefaultValueChangeModel> defaultValuesDiff)
  {
    this.defaultValuesDiff = defaultValuesDiff;
  }
  
  @Override
  public Map<String, List<String>> getDeletedPropertiesFromSource()
  {
    if (deletedPropertiesFromSource == null) {
      deletedPropertiesFromSource = new HashMap<>();
    }
    return deletedPropertiesFromSource;
  }
  
  @Override
  public void setDeletedPropertiesFromSource(Map<String, List<String>> deletedPropertiesFromSource)
  {
    this.deletedPropertiesFromSource = deletedPropertiesFromSource;
  }
  
  @Override
  public Boolean getIsCollectionMovable()
  {
    if (isCollectionMovable == null) {
      isCollectionMovable = false;
    }
    return isCollectionMovable;
  }
  
  @Override
  public void setIsCollectionMovable(Boolean isCollectionMovable)
  {
    this.isCollectionMovable = isCollectionMovable;
  }
}
