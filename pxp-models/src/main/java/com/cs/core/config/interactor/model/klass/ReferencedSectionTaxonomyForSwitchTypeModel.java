package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.attributiontaxonomy.AbstractTaxonomy;
import com.cs.core.config.interactor.entity.propertycollection.ISectionTaxonomy;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ReferencedSectionTaxonomyForSwitchTypeModel
    extends AbstractReferencedSectionElementForSwitchTypeModel implements ISectionTaxonomy {
  
  private static final long serialVersionUID = 1L;
  
  protected ITaxonomy       taxonomy;
  protected Boolean         isMultiselect    = true;
  
  @Override
  public ITaxonomy getTaxonomy()
  {
    return taxonomy;
  }
  
  @Override
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
