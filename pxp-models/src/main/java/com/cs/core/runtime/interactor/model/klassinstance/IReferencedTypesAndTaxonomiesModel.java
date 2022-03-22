package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.Map;

import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelNatureTypeModel;

public interface IReferencedTypesAndTaxonomiesModel extends IModel {
  
  String REFERENCED_TAXONOMIES = "referencedTaxonomies";
  String REFERENCED_KLASSES    = "referencedKlasses";
  
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies();
  
  public void setReferencedTaxonomies(Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies);
  
  public Map<String, IIdLabelNatureTypeModel> getReferencedKlasses();
  
  public void setReferencedKlasses(Map<String, IIdLabelNatureTypeModel> referencedKlasses);
}
