package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.runtime.interactor.model.attribute.AttributeDiffModel;
import com.cs.core.runtime.interactor.model.attribute.IAttributeDiffModel;
import com.cs.core.runtime.interactor.model.configdetails.IStructureDiffModel;
import com.cs.core.runtime.interactor.model.tag.ITagDiffModel;
import com.cs.core.runtime.interactor.model.tag.TagDiffModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class StructureDiffModel implements IStructureDiffModel {
  
  protected String                    id;
  protected String                    structureId;
  protected String                    referenceId;
  protected List<ITagDiffModel>       tags;
  protected List<IAttributeDiffModel> attributes;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getStructureId()
  {
    return structureId;
  }
  
  @Override
  public void setStructureId(String structureId)
  {
    this.structureId = structureId;
  }
  
  @Override
  public String getReferenceId()
  {
    return referenceId;
  }
  
  @Override
  public void setReferenceId(String referenceId)
  {
    this.referenceId = referenceId;
  }
  
  @Override
  public List<ITagDiffModel> getTags()
  {
    if (tags == null) {
      tags = new ArrayList<ITagDiffModel>();
    }
    return tags;
  }
  
  @JsonDeserialize(contentAs = TagDiffModel.class)
  @Override
  public void setTags(List<ITagDiffModel> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public List<IAttributeDiffModel> getAttributes()
  {
    return attributes;
  }
  
  @JsonDeserialize(contentAs = AttributeDiffModel.class)
  @Override
  public void setAttributes(List<IAttributeDiffModel> attributes)
  {
    if (attributes == null) {
      attributes = new ArrayList<IAttributeDiffModel>();
    }
    this.attributes = attributes;
  }
}
