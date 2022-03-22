package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.config.interactor.model.klass.AddedDeletedTypesModel;
import com.cs.core.config.interactor.model.klass.IAddedDeletedTypesModel;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.entity.idandtype.IdAndBaseType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ContentClassificationRuleViolotionModelForBulkPropagation
    implements IContentClassificationRuleViolotionModelForBulkPropagation {
  
  private static final long         serialVersionUID = 1L;
  protected IAddedDeletedTypesModel addedDeletedTypes;
  protected IIdAndBaseType          contentIdAndBaseTypeDetails;
  
  @Override
  public IAddedDeletedTypesModel getAddedDeletedTypes()
  {
    return addedDeletedTypes;
  }
  
  @Override
  @JsonDeserialize(as = AddedDeletedTypesModel.class)
  public void setAddedDeletedTypes(IAddedDeletedTypesModel addedDeletedTypes)
  {
    this.addedDeletedTypes = addedDeletedTypes;
  }
  
  @Override
  public IIdAndBaseType getContentIdAndBaseTypeDetails()
  {
    return contentIdAndBaseTypeDetails;
  }
  
  @Override
  @JsonDeserialize(as = IdAndBaseType.class)
  public void setContentIdAndBaseTypeDetails(IIdAndBaseType contentIdAndBaseTypeDetails)
  {
    this.contentIdAndBaseTypeDetails = contentIdAndBaseTypeDetails;
  }
}
