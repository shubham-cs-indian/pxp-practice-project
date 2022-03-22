package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.config.interactor.entity.matchandmerge.AbstractSection;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GetStaticCollectionDetailsModel implements IGetStaticCollectionDetailsModel {
  
  private static final long          serialVersionUID = 1L;
  
  protected List<? extends ISection> sections         = new ArrayList<>();
  protected String                   id;
  protected String                   label;
  protected List<ICollectionModel>   children         = new ArrayList<>();
  
  @Override
  public String getId()
  {
    return id;
  }
  
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public List<ICollectionModel> getChildren()
  {
    return children;
  }
  
  @Override
  @JsonDeserialize(contentAs = CollectionModel.class)
  public void setChildren(List<ICollectionModel> children)
  {
    this.children = children;
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
}
