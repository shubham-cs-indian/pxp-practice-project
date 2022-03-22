package com.cs.core.config.interactor.model.attributiontaxonomy;

import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;

public interface ISaveMasterTaxonomyModel
    extends ISaveTagTaxonomyModel, IMasterTaxonomy, IConfigModel {
 
  public static final String IS_BACKGROUND_SAVE_TAXONOMY = "isBackgroundSaveTaxonomy";
  
  public void setIsBackgroundSaveTaxonomy(Boolean isBackgroundSaveTaxonomy);
  
  public Boolean getIsBackgroundSaveTaxonomy();
  
}
