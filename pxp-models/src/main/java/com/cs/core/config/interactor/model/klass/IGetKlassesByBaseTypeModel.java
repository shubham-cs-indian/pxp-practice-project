package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;

import java.util.List;

public interface IGetKlassesByBaseTypeModel extends IConfigGetAllRequestModel {
  
  public static final String IS_NATURE                         = "isNature";
  public static final String IS_ABSTRACT                       = "isAbstract";
  public static final String SHOULD_GET_ATTRIBUTION_TAXONOMIES = "shouldGetAttributionTaxonomies";
  public static final String TYPES_TO_EXCLUDE                  = "typesToExclude";
  public static final String IS_VARIANT_ALLOWED                = "isVariantAllowed";
  public static final String IS_NATURE_AND_NON_NATURE          = "isNatureAndNonNature";
  
  public Boolean getIsNature();
  
  public void setIsNature(Boolean isNature);
  
  public Boolean getIsAbstract();
  
  public void setIsAbstract(Boolean isAbstract);
  
  public Boolean getShouldGetAttributionTaxonomies();
  
  public void setShouldGetAttributionTaxonomies(Boolean shouldGetAttributionTaxonomies);
  
  public List<String> getTypesToExclude();
  
  public void setTypesToExclude(List<String> typesToExclude);
  
  public Boolean getIsVariantAllowed();
  
  public void setIsVariantAllowed(Boolean isVariantAllowed);
  
  public Boolean getIsNatureAndNonNature();
  
  public void setIsNatureAndNonNature(Boolean isNatureAndNonNature);
}
