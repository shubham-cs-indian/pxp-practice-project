package com.cs.core.config.interactor.entity.propertycollection;

import com.cs.core.config.interactor.entity.attributiontaxonomy.AbstractTaxonomy;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class SectionTaxonomy extends AbstractSectionElement implements ISectionTaxonomy {
  
  private static final long serialVersionUID = 1L;
  
  protected ITaxonomy       taxonomy;
  protected Boolean         isMultiselect    = true;
  
  @Override
  public ITaxonomy getTaxonomy()
  {
    return taxonomy;
  }
  
  @Override
  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "baseType", visible = true)
  @JsonDeserialize(as = AbstractTaxonomy.class)
  public void setTaxonomy(ITaxonomy taxonomy)
  {
    this.taxonomy = taxonomy;
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
