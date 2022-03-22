package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.attribute.AttributeDiffModel;
import com.cs.core.runtime.interactor.model.attribute.IAttributeDiffModel;
import com.cs.core.runtime.interactor.model.configdetails.IStructureDiffModel;
import com.cs.core.runtime.interactor.model.instance.StructureDiffModel;
import com.cs.core.runtime.interactor.model.tag.ITagDiffModel;
import com.cs.core.runtime.interactor.model.tag.TagDiffModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class KlassInstanceDiffModel implements IKlassInstanceDiffModel {
  
  protected List<IAttributeDiffModel> attributes;
  protected List<ITagDiffModel>       tags;
  protected List<IStructureDiffModel> structures;
  
  @Override
  public List<IAttributeDiffModel> getAttributes()
  {
    return attributes;
  }
  
  @JsonDeserialize(contentAs = AttributeDiffModel.class)
  @Override
  public void setAttributes(List<IAttributeDiffModel> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public List<ITagDiffModel> getTags()
  {
    return tags;
  }
  
  @JsonDeserialize(contentAs = TagDiffModel.class)
  @Override
  public void setTags(List<ITagDiffModel> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public List<IStructureDiffModel> getStructures()
  {
    return structures;
  }
  
  @JsonDeserialize(contentAs = StructureDiffModel.class)
  @Override
  public void setStructures(List<IStructureDiffModel> structures)
  {
    this.structures = structures;
  }
}
