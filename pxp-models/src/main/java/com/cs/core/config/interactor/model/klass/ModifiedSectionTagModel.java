package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.entity.tag.IdRelevance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ModifiedSectionTagModel extends AbstractModifiedSectionElementModel
    implements IModifiedSectionTagModel {
  
  private static final long    serialVersionUID         = 1L;
  protected String             tagType;
  protected Boolean            isMultiselect;
  protected List<IIdRelevance> addedDefaultValues       = new ArrayList<>();
  protected List<IIdRelevance> modifiedDefaultValues    = new ArrayList<>();
  protected List<String>       deletedDefaultValues     = new ArrayList<>();
  protected List<String>       addedSelectedTagValues   = new ArrayList<>();
  protected List<String>       deletedSelectedTagValues = new ArrayList<>();
  protected Boolean            isVersionable;
  
  @Override
  public String getTagType()
  {
    return tagType;
  }
  
  @Override
  public void setTagType(String tagType)
  {
    this.tagType = tagType;
  }
  
  @Override
  public Boolean getIsMultiselect()
  {
    return isMultiselect;
  }
  
  @Override
  public void setIsMultiselect(Boolean isMultiselect)
  {
    this.isMultiselect = isMultiselect;
  }
  
  @Override
  public List<IIdRelevance> getAddedDefaultValues()
  {
    return addedDefaultValues;
  }
  
  @JsonDeserialize(contentAs = IdRelevance.class)
  @Override
  public void setAddedDefaultValues(List<IIdRelevance> addedDefaultValues)
  {
    this.addedDefaultValues = addedDefaultValues;
  }
  
  @Override
  public List<IIdRelevance> getModifiedDefaultValues()
  {
    return modifiedDefaultValues;
  }
  
  @JsonDeserialize(contentAs = IdRelevance.class)
  @Override
  public void setModifiedDefaultValues(List<IIdRelevance> modifiedDefaultValues)
  {
    this.modifiedDefaultValues = modifiedDefaultValues;
  }
  
  @Override
  public List<String> getDeletedDefaultValues()
  {
    return deletedDefaultValues;
  }
  
  @Override
  public void setDeletedDefaultValues(List<String> deletedDefaultValues)
  {
    this.deletedDefaultValues = deletedDefaultValues;
  }
  
  @Override
  public List<String> getAddedSelectedTagValues()
  {
    return addedSelectedTagValues;
  }
  
  @Override
  public void setAddedSelectedTagValues(List<String> addedSelectedTagValues)
  {
    this.addedSelectedTagValues = addedSelectedTagValues;
  }
  
  @Override
  public List<String> getDeletedSelectedTagValues()
  {
    return deletedSelectedTagValues;
  }
  
  @Override
  public void setDeletedSelectedTagValues(List<String> deletedSelectedTagValues)
  {
    this.deletedSelectedTagValues = deletedSelectedTagValues;
  }
  
  @Override
  public Boolean getIsVersionable()
  {
    return isVersionable;
  }
  
  @Override
  public void setIsVersionable(Boolean isVersionable)
  {
    this.isVersionable = isVersionable;
  }
}
