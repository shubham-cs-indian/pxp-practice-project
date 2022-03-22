package com.cs.core.config.interactor.entity.attributiontaxonomy;

import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class MasterTaxonomy extends AbstractTagTaxonomy implements IMasterTaxonomy {
  
  private static final long serialVersionUID = 1L;
  protected Boolean         isVersionable;
  
  @JsonDeserialize(as = MasterTaxonomy.class)
  @Override
  public void setParent(ITreeEntity parent)
  {
    this.parent = parent;
  }
  
  @JsonDeserialize(contentAs = MasterTaxonomy.class)
  @Override
  public void setChildren(List<? extends ITreeEntity> children)
  {
    this.children = children;
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
