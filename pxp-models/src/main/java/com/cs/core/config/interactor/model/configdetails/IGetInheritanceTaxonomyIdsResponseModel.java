package com.cs.core.config.interactor.model.configdetails;

import java.util.Map;

import com.cs.core.config.interactor.model.taxonomy.IReferencedTaxonomyParentModel;

public interface IGetInheritanceTaxonomyIdsResponseModel extends IGetContentVsTypesTaxonomyResponseModel {
  
  public static final String REFERENCED_TAXONOMIES = "referencedTaxonomies";
  public static final String PARENT_CONTENT_ID     = "parentContentId";
 
  public Map<String, IReferencedTaxonomyParentModel> getReferencedTaxonomies();
  public void setReferencedTaxonomies(Map<String, IReferencedTaxonomyParentModel> referencedTaxonomies);
  
  public void setParentContentId(String parentContentId);
  public String getParentContentId();
  
}
