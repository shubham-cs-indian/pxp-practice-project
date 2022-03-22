package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.config.interactor.model.klass.IAddedDeletedTypesModel;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IContentClassificationRuleViolotionModelForBulkPropagation extends IModel {
  
  public static final String ADDED_DELETED_TYPES              = "addedDeletedTypes";
  public static final String CONTENT_ID_AND_BASE_TYPE_DETAILS = "contentIdAndBaseTypeDetails";
  
  public IAddedDeletedTypesModel getAddedDeletedTypes();
  
  public void setAddedDeletedTypes(IAddedDeletedTypesModel addedDeletedTypes);
  
  public IIdAndBaseType getContentIdAndBaseTypeDetails();
  
  public void setContentIdAndBaseTypeDetails(IIdAndBaseType contentIdAndBaseTypeDetails);
}
