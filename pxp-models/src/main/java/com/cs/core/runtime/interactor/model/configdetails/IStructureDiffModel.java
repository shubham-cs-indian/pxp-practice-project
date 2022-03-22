package com.cs.core.runtime.interactor.model.configdetails;

import com.cs.core.runtime.interactor.model.attribute.IAttributeDiffModel;
import com.cs.core.runtime.interactor.model.tag.ITagDiffModel;

import java.util.List;

public interface IStructureDiffModel {
  
  public static final String ID           = "id";
  public static final String REFERENCE_ID = "referenceId";
  public static final String STRUCTURE_ID = "structureId";
  public static final String ATTRIBUTES   = "attributes";
  public static final String TAGS         = "tags";
  
  public String getId();
  
  public void setId(String id);
  
  public String getReferenceId();
  
  public void setReferenceId(String referenceId);
  
  public String getStructureId();
  
  public void setStructureId(String referenceId);
  
  public List<IAttributeDiffModel> getAttributes();
  
  public void setAttributes(List<IAttributeDiffModel> attributes);
  
  public List<ITagDiffModel> getTags();
  
  public void setTags(List<ITagDiffModel> tags);
}
