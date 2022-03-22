package com.cs.core.runtime.interactor.model.contextdatatransfer;

import com.cs.core.runtime.strategy.model.couplingtype.IIdCodeCouplingTypeModel;
import com.cs.core.runtime.strategy.model.couplingtype.IdCodeCouplingTypeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ContextInfoForContextualDataTransferModel
    implements IContextInfoForContextualDataTransferModel {
  
  private static final long                serialVersionUID = 1L;
  protected String                         klassId;
  protected String                         klassType;
  protected String                         contextKlassId;
  protected List<IIdCodeCouplingTypeModel> modifiedDependentAttributes;
  protected List<IIdCodeCouplingTypeModel> modifiedInDependentAttributes;
  protected List<IIdCodeCouplingTypeModel> modifiedTags;
  protected List<String>                   deletedDependentAttributes;
  protected List<String>                   deletedInDependentAttributes;
  protected List<String>                   deletedTags;
  
  @Override
  public List<IIdCodeCouplingTypeModel> getModifiedDependentAttributes()
  {
    if (modifiedDependentAttributes == null) {
      modifiedDependentAttributes = new ArrayList<>();
    }
    return modifiedDependentAttributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdCodeCouplingTypeModel.class)
  public void setModifiedDependentAttributes(
      List<IIdCodeCouplingTypeModel> modifiedDependentAttributes)
  {
    this.modifiedDependentAttributes = modifiedDependentAttributes;
  }
  
  @Override
  public List<IIdCodeCouplingTypeModel> getModifiedInDependentAttributes()
  {
    if (modifiedInDependentAttributes == null) {
      modifiedInDependentAttributes = new ArrayList<>();
    }
    return modifiedInDependentAttributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdCodeCouplingTypeModel.class)
  public void setModifiedInDependentAttributes(
      List<IIdCodeCouplingTypeModel> modifiedInDependentAttributes)
  {
    this.modifiedInDependentAttributes = modifiedInDependentAttributes;
  }
  
  @Override
  public List<IIdCodeCouplingTypeModel> getModifiedTags()
  {
    if (modifiedTags == null) {
      modifiedTags = new ArrayList<>();
    }
    return modifiedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdCodeCouplingTypeModel.class)
  public void setModifiedTags(List<IIdCodeCouplingTypeModel> modifiedTags)
  {
    this.modifiedTags = modifiedTags;
  }
  
  @Override
  public List<String> getDeletedDependentAttributes()
  {
    if (deletedDependentAttributes == null) {
      deletedDependentAttributes = new ArrayList<>();
    }
    return deletedDependentAttributes;
  }
  
  @Override
  public void setDeletedDependentAttributes(List<String> deletedDependentAttributes)
  {
    this.deletedDependentAttributes = deletedDependentAttributes;
  }
  
  @Override
  public List<String> getDeletedInDependentAttributes()
  {
    if (deletedInDependentAttributes == null) {
      deletedInDependentAttributes = new ArrayList<>();
    }
    return deletedInDependentAttributes;
  }
  
  @Override
  public void setDeletedInDependentAttributes(List<String> deletedInDependentAttributes)
  {
    this.deletedInDependentAttributes = deletedInDependentAttributes;
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
  public String getKlassId()
  {
    return klassId;
  }
  
  @Override
  public void setKlassId(String klassId)
  {
    this.klassId = klassId;
  }
  
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
  public String getKlassType()
  {
    return klassType;
  }
  
  @Override
  public void setKlassType(String klassType)
  {
    this.klassType = klassType;
  }
}
