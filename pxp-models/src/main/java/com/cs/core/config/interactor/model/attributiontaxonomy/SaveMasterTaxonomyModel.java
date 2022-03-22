package com.cs.core.config.interactor.model.attributiontaxonomy;

import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.config.interactor.entity.attributiontaxonomy.MasterTaxonomy;

public class SaveMasterTaxonomyModel extends AbstractSaveTagTaxonomyModel
    implements ISaveMasterTaxonomyModel {
  
  private static final long serialVersionUID = 1L;
  protected Boolean         isVersionable;
  protected Boolean         isBackgroundSaveTaxonomy = false;
  
  public SaveMasterTaxonomyModel()
  {
    this.entity = new MasterTaxonomy();
  }
  
  public SaveMasterTaxonomyModel(IMasterTaxonomy entity)
  {
    this.entity = entity;
  }
  
  @Override
  public IMasterTaxonomy getEntity()
  {
    return (IMasterTaxonomy) entity;
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
  
  public Boolean getIsBackgroundSaveTaxonomy()
  {
    return isBackgroundSaveTaxonomy;
  }
  
  public void setIsBackgroundSaveTaxonomy(Boolean isBackgroundSaveTaxonomy)
  {
    this.isBackgroundSaveTaxonomy = isBackgroundSaveTaxonomy;
  }
  
}
