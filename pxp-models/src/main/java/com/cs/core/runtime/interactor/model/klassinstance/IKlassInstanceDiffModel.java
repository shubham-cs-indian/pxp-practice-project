package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.attribute.IAttributeDiffModel;
import com.cs.core.runtime.interactor.model.configdetails.IStructureDiffModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.tag.ITagDiffModel;

import java.util.List;

public interface IKlassInstanceDiffModel extends IModel {
  
  public static final String ATTRIBUTES = "attributes";
  public static final String TAGS       = "tags";
  public static final String STRUCTURES = "structures";
  
  public List<IAttributeDiffModel> getAttributes();
  
  public void setAttributes(List<IAttributeDiffModel> attributes);
  
  public List<ITagDiffModel> getTags();
  
  public void setTags(List<ITagDiffModel> tags);
  
  public List<IStructureDiffModel> getStructures();
  
  public void setStructures(List<IStructureDiffModel> structures);
}
