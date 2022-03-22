package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.klass.KlassNatureRelationship;
import com.cs.core.config.interactor.model.propertycollection.AddedTabModel;
import com.cs.core.config.interactor.model.propertycollection.IAddedTabModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class AddedNatureRelationshipModel extends KlassNatureRelationship
    implements IAddedNatureRelationshipModel {
  
  private static final long serialVersionUID = 1L;
  protected String          addedPropertyCollection;
  protected IAddedTabModel  tab;
  
  @Override
  public String getAddedPropertyCollection()
  {
    
    return addedPropertyCollection;
  }
  
  @Override
  public void setAddedPropertyCollection(String addedPropertyCollection)
  {
    this.addedPropertyCollection = addedPropertyCollection;
  }
  
  @Override
  public IAddedTabModel getTab()
  {
    return tab;
  }
  
  @Override
  @JsonDeserialize(as = AddedTabModel.class)
  public void setTab(IAddedTabModel tab)
  {
    this.tab = tab;
  }
  
  @JsonIgnore
  @Override
  public Long getVersionId()
  {
    
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setVersionId(Long versionId)
  {
  }
  
  @JsonIgnore
  @Override
  public Long getVersionTimestamp()
  {
    
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
  }
  
  @JsonIgnore
  @Override
  public String getLastModifiedBy()
  {
    
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
  }
}
