package com.cs.core.runtime.interactor.model.context;

import com.cs.core.config.interactor.model.klass.IModifiedContextKlassModel;
import com.cs.core.runtime.interactor.model.configuration.IIdAndCouplingTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IdAndCouplingTypeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ModifiedContextKlassModel implements IModifiedContextKlassModel {
  
  private static final long               serialVersionUID = 1L;
  
  protected String                        contextKlassId;
  protected List<IIdAndCouplingTypeModel> addedAttributes;
  protected List<IIdAndCouplingTypeModel> modifiedAttributes;
  protected List<String>                  deletedAttributes;
  protected List<IIdAndCouplingTypeModel> addedTags;
  protected List<IIdAndCouplingTypeModel> modifiedTags;
  protected List<String>                  deletedTags;
  protected String                        contextId;
  
  @Override
  public String getContextKlassId()
  {
    return contextKlassId;
  }
  
  @Override
  public void setContextKlassId(String contextKlassId)
  {
    this.contextKlassId = contextKlassId;
  }
  
  @Override
  public List<IIdAndCouplingTypeModel> getAddedAttributes()
  {
    if (addedAttributes == null) {
      addedAttributes = new ArrayList<>();
    }
    return addedAttributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdAndCouplingTypeModel.class)
  public void setAddedAttributes(List<IIdAndCouplingTypeModel> addedAttributes)
  {
    this.addedAttributes = addedAttributes;
  }
  
  @Override
  public List<String> getDeletedAttributes()
  {
    if (deletedAttributes == null) {
      deletedAttributes = new ArrayList<>();
    }
    return deletedAttributes;
  }
  
  @Override
  public void setDeletedAttributes(List<String> deletedAttributes)
  {
    this.deletedAttributes = deletedAttributes;
  }
  
  @Override
  public List<IIdAndCouplingTypeModel> getModifiedAttributes()
  {
    if (modifiedAttributes == null) {
      modifiedAttributes = new ArrayList<>();
    }
    return modifiedAttributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdAndCouplingTypeModel.class)
  public void setModifiedAttributes(List<IIdAndCouplingTypeModel> modifiedAttributes)
  {
    this.modifiedAttributes = modifiedAttributes;
  }
  
  @Override
  public List<IIdAndCouplingTypeModel> getAddedTags()
  {
    if (addedTags == null) {
      addedTags = new ArrayList<>();
    }
    return addedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdAndCouplingTypeModel.class)
  public void setAddedTags(List<IIdAndCouplingTypeModel> addedTags)
  {
    this.addedTags = addedTags;
  }
  
  @Override
  public List<String> getDeletedTags()
  {
    if (deletedTags == null) {
      deletedTags = new ArrayList<>();
    }
    return deletedTags;
  }
  
  @Override
  public void setDeletedTags(List<String> deletedTags)
  {
    this.deletedTags = deletedTags;
  }
  
  @Override
  public List<IIdAndCouplingTypeModel> getModifiedTags()
  {
    if (modifiedTags == null) {
      modifiedTags = new ArrayList<>();
    }
    return modifiedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdAndCouplingTypeModel.class)
  public void setModifiedTags(List<IIdAndCouplingTypeModel> modifiedTags)
  {
    this.modifiedTags = modifiedTags;
  }
  
  @Override
  public String getContextId()
  {
    return contextId;
  }
  
  @Override
  public void setContextId(String contextId)
  {
    this.contextId = contextId;
  }
}
