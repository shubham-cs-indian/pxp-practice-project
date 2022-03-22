package com.cs.core.config.interactor.model.goldenrecord;

import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.ReferencedKlassDetailsModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.ReferencedArticleTaxonomyModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class ConfigDetailsForTypeInfoModel implements IConfigDetailsForTypeInfoModel {
  
  private static final long                                  serialVersionUID = 1L;
  protected Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses;
  protected Map<String, IReferencedArticleTaxonomyModel>     referencedTaxonomies;
  
  @Override
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses()
  {
    if (referencedKlasses == null) {
      referencedKlasses = new HashMap<>();
    }
    return referencedKlasses;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedKlassDetailsModel.class)
  public void setReferencedKlasses(Map<String, IReferencedKlassDetailStrategyModel> klasses)
  {
    this.referencedKlasses = klasses;
  }
  
  @Override
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies()
  {
    if (referencedTaxonomies == null) {
      referencedTaxonomies = new HashMap<>();
    }
    return referencedTaxonomies;
  }
  
  @JsonDeserialize(contentAs = ReferencedArticleTaxonomyModel.class)
  @Override
  public void setReferencedTaxonomies(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies)
  {
    this.referencedTaxonomies = referencedTaxonomies;
  }
}
