package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.config.interactor.model.klass.ITypesListModel;
import com.cs.core.config.interactor.model.klass.TypesListModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipPropertiesToInheritModel;
import com.cs.core.runtime.interactor.model.relationship.RelationshipPropertiesToInheritModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetInstanceInfoForBulkPropagationRequestModel
    implements IGetInstanceInfoForBulkPropagationRequestModel {
  
  private static final long                       serialVersionUID = 1L;
  protected ITypesListModel                       typesListModel;
  protected IRelationshipPropertiesToInheritModel relationshipPropertiesToInheritModel;
  
  @Override
  public ITypesListModel getTypesListModel()
  {
    return typesListModel;
  }
  
  @Override
  @JsonDeserialize(as = TypesListModel.class)
  public void setTypesListModel(ITypesListModel typeslistModel)
  {
    this.typesListModel = typeslistModel;
  }
  
  @Override
  public IRelationshipPropertiesToInheritModel getRelationshipPropertiesToInheritModel()
  {
    return relationshipPropertiesToInheritModel;
  }
  
  @Override
  @JsonDeserialize(as = RelationshipPropertiesToInheritModel.class)
  public void setRelationshipPropertiesToInheritModel(
      IRelationshipPropertiesToInheritModel relationshipPropertiesToInheritModel)
  {
    this.relationshipPropertiesToInheritModel = relationshipPropertiesToInheritModel;
  }
}
