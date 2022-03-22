package com.cs.core.runtime.interactor.entity.relationshipinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import java.util.List;

public interface IRemoveRelationshipConflictsFromSourceModel extends IModel {
  
  public static final String SOURCE_CONTENT_ID = "sourceContentId";
  public static final String REL_SIDE_IDS      = "relSideIds";
  
  public String getSourceContentId();
  
  public void setSourceContentId(String sourceContentId);
  
  public List<String> getRelSideIds();
  
  public void setRelSideIds(List<String> relSideIds);
}
