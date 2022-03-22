package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.config.interactor.model.configdetails.BulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteVariantContextReturnModel;

import java.util.ArrayList;
import java.util.List;

public class BulkDeleteVariantContextReturnModel extends BulkDeleteReturnModel
    implements IBulkDeleteVariantContextReturnModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    relationshipIds;
  
  @Override
  public List<String> getRelationshipIds()
  {
    if (relationshipIds == null) {
      relationshipIds = new ArrayList<>();
    }
    return relationshipIds;
  }
  
  @Override
  public void setRelationshipIds(List<String> relationshipIds)
  {
    this.relationshipIds = relationshipIds;
  }
}
