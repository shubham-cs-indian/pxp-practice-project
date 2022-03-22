package com.cs.core.runtime.interactor.model.relationship;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.klassinstance.ISaveKlassInstanceRelationshipTreeStrategyModel;

import java.util.List;

public interface IBranchRelationshipModel extends IModel {
  
  public static final String IDS                              = "ids";
  public static final String SAVE_KLASS_INSTANCE_RELATIONSHIP = "saveKlassInstanceRelationship";
  public static final String TYPE_SWITCH_STATUS               = "typeSwitchStatus";
  
  public List<String> getIds();
  
  public void setIds(List<String> ids);
  
  public ISaveKlassInstanceRelationshipTreeStrategyModel getSaveKlassInstanceRelationship();
  
  public void setSaveKlassInstanceRelationship(
      ISaveKlassInstanceRelationshipTreeStrategyModel contentRelationships);
  
  public Boolean getTypeSwitchStatus();
  
  public void setTypeSwitchStatus(Boolean typeSwitchStatus);
}
