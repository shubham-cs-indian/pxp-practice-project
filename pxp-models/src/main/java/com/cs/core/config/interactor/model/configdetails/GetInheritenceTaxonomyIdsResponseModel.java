package com.cs.core.config.interactor.model.configdetails;

import java.util.Map;

import com.cs.core.config.interactor.model.taxonomy.IReferencedTaxonomyParentModel;
import com.cs.core.config.interactor.model.taxonomy.ReferencedTaxonomyParentModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetInheritenceTaxonomyIdsResponseModel extends GetContentVsTypesTaxonomyResponseModel  implements IGetInheritanceTaxonomyIdsResponseModel {

  private static final long serialVersionUID = 1L;
  
  protected Map<String, IReferencedTaxonomyParentModel> referencedTaxonomies;
  protected String parentContentId;
  
  @Override
  public Map<String, IReferencedTaxonomyParentModel> getReferencedTaxonomies()
  {
      return referencedTaxonomies;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedTaxonomyParentModel.class)
  public void setReferencedTaxonomies(Map<String, IReferencedTaxonomyParentModel> referencedTaxonomies)
  {
    this.referencedTaxonomies = referencedTaxonomies;
  }

  @Override
  public String getParentContentId()
  {
    return parentContentId;
  }

  @Override
  public void setParentContentId(String parentContentId)
  {
    this.parentContentId = parentContentId;
  }
  
}
