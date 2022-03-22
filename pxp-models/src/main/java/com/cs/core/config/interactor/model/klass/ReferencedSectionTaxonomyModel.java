package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ReferencedSectionTaxonomyModel extends AbstractReferencedSectionElementModel
    implements IReferencedSectionTaxonomyModel {
  
  private static final long serialVersionUID = 1L;
  protected Boolean         isMultiselect    = true;
  
  @Override
  @JsonIgnore
  public ITaxonomy getTaxonomy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setTaxonomy(ITaxonomy taxonomy)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Boolean getIsMultiselect()
  {
    return isMultiselect;
  }
  
  @Override
  public void setIsMultiselect(Boolean isMultiselect)
  {
    this.isMultiselect = isMultiselect;
  }
}
