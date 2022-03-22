package com.cs.core.runtime.interactor.model.klassinstance;


import java.util.HashMap;
import java.util.Map;

import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.ReferencedArticleTaxonomyModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelNatureTypeModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IdLabelNatureTypeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

 

public class ReferencedTypesAndTxonomiesModel implements IReferencedTypesAndTaxonomiesModel {
  

 

  private static final long serialVersionUID = 1L;
  protected Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies;
  protected Map<String, IIdLabelNatureTypeModel>         referencedKlasses;
  
  @Override
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies()
  {
    return referencedTaxonomies;
  }
  
  @JsonDeserialize(contentAs = ReferencedArticleTaxonomyModel.class)
  @Override
  public void setReferencedTaxonomies(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies)
  {
    this.referencedTaxonomies = referencedTaxonomies;
  }
  
  @Override
  public Map<String,IIdLabelNatureTypeModel> getReferencedKlasses()
  {
    if (referencedKlasses == null) {
      return new HashMap<>();
    }
    return referencedKlasses;
  }
  
  @JsonDeserialize(contentAs = IdLabelNatureTypeModel.class)
  @Override
  public void setReferencedKlasses(Map<String, IIdLabelNatureTypeModel> referencedKlasses)
  {
    this.referencedKlasses = referencedKlasses;
  }
}