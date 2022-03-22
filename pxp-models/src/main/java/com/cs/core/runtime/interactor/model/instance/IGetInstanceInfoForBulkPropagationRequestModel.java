package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.config.interactor.model.klass.ITypesListModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipPropertiesToInheritModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetInstanceInfoForBulkPropagationRequestModel extends IModel {
  
  public static final String TYPES_LIST_MODEL                         = "typesListModel";
  public static final String RELATIONSHIP_PROPERTIES_TO_INHERIT_MODEL = "relationshipPropertiesToInheritModel";
  
  public ITypesListModel getTypesListModel();
  
  public void setTypesListModel(ITypesListModel typeslistModel);
  
  public IRelationshipPropertiesToInheritModel getRelationshipPropertiesToInheritModel();
  
  public void setRelationshipPropertiesToInheritModel(
      IRelationshipPropertiesToInheritModel relationshipPropertiesToInheritModel);
}
