package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.klassinstance.ISaveKlassInstanceRelationshipTreeStrategyModel;
import com.cs.core.runtime.interactor.model.klassinstance.SaveKlassInstanceRelationshipTreeStrategyModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class BranchRelationshipModel implements IBranchRelationshipModel {
  
  private static final long                                 serialVersionUID = 1L;
  
  protected List<String>                                    ids;
  protected ISaveKlassInstanceRelationshipTreeStrategyModel contentRelationships;
  protected Boolean                                         typeSwitchStatus;
  
  public List<String> getIds()
  {
    if (ids == null) {
      ids = new ArrayList<String>();
    }
    return ids;
  }
  
  public void setIds(List<String> ids)
  {
    this.ids = ids;
  }
  
  @Override
  public ISaveKlassInstanceRelationshipTreeStrategyModel getSaveKlassInstanceRelationship()
  {
    return contentRelationships;
  }
  
  @JsonDeserialize(as = SaveKlassInstanceRelationshipTreeStrategyModel.class)
  @Override
  public void setSaveKlassInstanceRelationship(
      ISaveKlassInstanceRelationshipTreeStrategyModel contentRelationships)
  {
    this.contentRelationships = contentRelationships;
  }
  
  @Override
  public String toString()
  {
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append("{");
    strBuilder.append("\"ids\":");
    
    strBuilder.append("[");
    int listSize = ids.size();
    for (int index = 0; index < listSize; index++) {
      if (index < listSize - 1) {
        strBuilder.append("\"" + ids.get(index) + "\",");
      }
      else {
        strBuilder.append("\"" + ids.get(index) + "\"");
      }
    }
    strBuilder.append("]");
    strBuilder.append("}");
    
    return strBuilder.toString();
  }
  
  @Override
  public Boolean getTypeSwitchStatus()
  {
    return typeSwitchStatus;
  }
  
  @Override
  public void setTypeSwitchStatus(Boolean typeSwitchStatus)
  {
    this.typeSwitchStatus = typeSwitchStatus;
  }
}
