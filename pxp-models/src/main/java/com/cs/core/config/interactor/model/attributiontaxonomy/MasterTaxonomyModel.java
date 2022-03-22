package com.cs.core.config.interactor.model.attributiontaxonomy;

import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.config.interactor.entity.attributiontaxonomy.MasterTaxonomy;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class MasterTaxonomyModel extends AbstractTagTaxonomyModel implements IMasterTaxonomyModel {
  
  private static final long serialVersionUID = 1L;
  protected Boolean         isVersionable;
  
  public MasterTaxonomyModel()
  {
    this.entity = new MasterTaxonomy();
  }
  
  public MasterTaxonomyModel(IMasterTaxonomy entity)
  {
    this.entity = entity;
  }
  
  @Override
  public IMasterTaxonomy getEntity()
  {
    return (IMasterTaxonomy) entity;
  }
  
  @JsonDeserialize(as = MasterTaxonomy.class)
  @Override
  public void setParent(ITreeEntity parent)
  {
    entity.setParent(parent);
  }
  
  @Override
  @JsonDeserialize(contentAs = MasterTaxonomy.class)
  public void setChildren(List<? extends ITreeEntity> children)
  {
    entity.setChildren(children);
  }
  
  @Override
  public long getPropertyIID()
  {
    return this.entity.getPropertyIID();
  }
  
  @Override
  public void setPropertyIID(long IID)
  {
    this.entity.setPropertyIID(IID);
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
