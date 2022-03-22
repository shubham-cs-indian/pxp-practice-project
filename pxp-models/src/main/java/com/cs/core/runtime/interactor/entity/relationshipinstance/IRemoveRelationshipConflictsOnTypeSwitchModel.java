package com.cs.core.runtime.interactor.entity.relationshipinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import java.util.List;

public interface IRemoveRelationshipConflictsOnTypeSwitchModel extends IModel {
  
  public static final String REMOVE_RELATIONSHIP_CONFLICTS_FROM_SOURCE = "removeRelationshipConflictsFromSource";
  public static final String NOT_APPLICABLE_REL_SIDE_IDS               = "notApplicableRelSideIds";
  public static final String NOT_APPLICABLE_NATURE_REL_SIDE_IDS        = "notApplicableNatureRelSideIds";
  
  public IRemoveRelationshipConflictsFromSourceModel getRemoveRelationshipConflictsFromSource();
  
  public void setRemoveRelationshipConflictsFromSource(
      IRemoveRelationshipConflictsFromSourceModel removeRelationshipConflictsFromSource);
  
  public List<String> getNotApplicableRelSideIds();
  
  public void setNotApplicableRelSideIds(List<String> notApplicableRelSideIds);
  
  public List<String> getNotApplicableNatureRelSideIds();
  
  public void setNotApplicableNatureRelSideIds(List<String> notApplicableNatureRelSideIds);
}
