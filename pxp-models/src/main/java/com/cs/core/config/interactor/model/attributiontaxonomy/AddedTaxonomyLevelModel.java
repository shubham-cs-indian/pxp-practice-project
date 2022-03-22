package com.cs.core.config.interactor.model.attributiontaxonomy;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class AddedTaxonomyLevelModel implements IAddedTaxonomyLevelModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected IAddedTagModel  addedTag;
  
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
  public IAddedTagModel getAddedTag()
  {
    return addedTag;
  }
  
  @Override
  @JsonDeserialize(as = AddedTagModel.class)
  public void setAddedTag(IAddedTagModel addedTag)
  {
    this.addedTag = addedTag;
  }
}
