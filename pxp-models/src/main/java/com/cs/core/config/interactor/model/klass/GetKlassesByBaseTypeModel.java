package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.model.grid.ConfigGetAllRequestModel;

import java.util.ArrayList;
import java.util.List;

public class GetKlassesByBaseTypeModel extends ConfigGetAllRequestModel
    implements IGetKlassesByBaseTypeModel {
  
  private static final long serialVersionUID = 1L;
  
  protected Boolean         isNature;
  protected Boolean         shouldGetAttributionTaxonomies;
  protected Boolean         isAbstract;
  protected List<String>    typesToExlude;
  protected Boolean         isVariantAllowed = true;
  protected Boolean         isNatureAndNonNature;
  
  @Override
  public Boolean getIsAbstract()
  {
    return isAbstract;
  }
  
  @Override
  public void setIsAbstract(Boolean isAbstract)
  {
    this.isAbstract = isAbstract;
  }
  
  @Override
  public Boolean getShouldGetAttributionTaxonomies()
  {
    return shouldGetAttributionTaxonomies;
  }
  
  @Override
  public void setShouldGetAttributionTaxonomies(Boolean shouldGetAttributionTaxonomies)
  {
    this.shouldGetAttributionTaxonomies = shouldGetAttributionTaxonomies;
  }
  
  @Override
  public Boolean getIsNature()
  {
    return isNature;
  }
  
  @Override
  public void setIsNature(Boolean isNature)
  {
    this.isNature = isNature;
  }
  
  @Override
  public List<String> getTypesToExclude()
  {
    if (typesToExlude == null) {
      typesToExlude = new ArrayList<>();
    }
    return typesToExlude;
  }
  
  @Override
  public void setTypesToExclude(List<String> typesToExclude)
  {
    this.typesToExlude = typesToExclude;
  }
  
  @Override
  public Boolean getIsVariantAllowed()
  {
    return isVariantAllowed;
  }
  
  @Override
  public void setIsVariantAllowed(Boolean isVariantAllowed)
  {
    this.isVariantAllowed = isVariantAllowed;
  }
  
  @Override
  public Boolean getIsNatureAndNonNature()
  {
    return isNatureAndNonNature;
  }
  
  @Override
  public void setIsNatureAndNonNature(Boolean isNatureAndNonNature)
  {
    this.isNatureAndNonNature = isNatureAndNonNature;
  }
}
