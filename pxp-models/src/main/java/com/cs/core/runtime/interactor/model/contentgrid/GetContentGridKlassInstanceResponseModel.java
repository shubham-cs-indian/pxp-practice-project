package com.cs.core.runtime.interactor.model.contentgrid;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.runtime.interactor.model.gridcontent.GridContentInstanceModel;
import com.cs.core.runtime.interactor.model.gridcontent.IGridContentInstanceModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetContentGridKlassInstanceResponseModel
    implements IGetContentGridKlassInstanceResponseModel {
  
  private static final long                 serialVersionUID = 1L;
  public Map<String, IAttribute>            referencedAttributes;
  public Map<String, ITag>                  referencedTags;
  public List<String>                       propertyList;
  public List<String>                       gridPropertySequenceList;
  protected List<IGridContentInstanceModel> klassInstance;
  
  @Override
  public List<IGridContentInstanceModel> getKlassInstances()
  {
    return this.klassInstance;
  }
  
  @Override
  @JsonDeserialize(contentAs = GridContentInstanceModel.class)
  public void setKlassInstances(List<IGridContentInstanceModel> klassInstance)
  {
    this.klassInstance = klassInstance;
  }
  
  @Override
  public Map<String, IAttribute> getReferencedAttributes()
  {
    return referencedAttributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractAttribute.class)
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes)
  {
    if (referencedAttributes == null) {
      referencedAttributes = new HashMap<>();
    }
    this.referencedAttributes = referencedAttributes;
  }
  
  @Override
  public Map<String, ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = Tag.class)
  public void setReferencedTags(Map<String, ITag> referencedTags)
  {
    if (referencedTags == null) {
      referencedTags = new HashMap<>();
    }
    this.referencedTags = referencedTags;
  }
  
  @Override
  public List<String> getPropertyList()
  {
    return propertyList;
  }
  
  @Override
  public void setPropertyList(List<String> propertyList)
  {
    if (propertyList == null) {
      propertyList = new ArrayList<>();
    }
    this.propertyList = propertyList;
  }
  
  @Override
  public List<String> getGridPropertySequenceList()
  {
    return gridPropertySequenceList;
  }
  
  @Override
  public void setGridPropertySequenceList(List<String> gridPropertySequenceList)
  {
    if (propertyList == null) {
      propertyList = new ArrayList<>();
    }
    this.gridPropertySequenceList = gridPropertySequenceList;
  }
}
