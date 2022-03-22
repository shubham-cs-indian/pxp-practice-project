package com.cs.core.config.interactor.model.klass;

public class ModifiedSectionTaxonomyModel extends AbstractModifiedSectionElementModel
    implements IModifiedSectionTaxonomyModel {
  
  private static final long serialVersionUID = 1L;
  protected Boolean         isMultiselect    = true;
  
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
