package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IGetInstanceRequestStrategyModel extends IGetInstanceRequestModel {
  
  public static final String REFRENCED_KLASSES                         = "referencedKlasses";
  public static final String TASK_IDS_FOR_ROLES_HAVING_READ_PERMISSION = "taskIdsForRolesHavingReadPermission";
  public static final String TASK_IDS_HAVING_READ_PERMISSION           = "taskIdsHavingReadPermissions";
  public static final String REFERENCED_TASK_IDS                       = "referencedTaskIds";
  public static final String ENTITIES                                  = "entities";
  public static final String KLASS_IDS_HAVING_RP                       = "klassIdsHavingRP";
  public static final String PERSONAL_TASK_IDS                         = "personalTaskIds";
  public static final String TAXONOMY_IDS_HAVING_RP                    = "taxonomyIdsHavingRP";
  
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses();
  
  public void setReferencedKlasses(Map<String, IReferencedKlassDetailStrategyModel> klasses);
  
  public Map<String, Object> getTaskIdsForRolesHavingReadPermission();
  
  public void setTaskIdsForRolesHavingReadPermission(
      Map<String, Object> taskIdsForRolesHavingReadPermission);
  
  public Set<String> getTaskIdsHavingReadPermissions();
  
  public void setTaskIdsHavingReadPermissions(Set<String> taskIdsHavingReadPermissions);
  
  public Set<String> getReferencedTaskIds();
  
  public void setReferencedTaskIds(Set<String> referencedTaskIds);
  
  public Set<String> getEntities();
  
  public void setEntities(Set<String> entitiesHavingReadPermission);
  
  public Set<String> getKlassIdsHavingRP();
  
  public void setKlassIdsHavingRP(Set<String> klassIdsHavingReadPermission);
  
  public Set<String> getPersonalTaskIds();
  
  public void setPersonalTaskIds(Set<String> personalTaskIds);
  
  public Set<String> getTaxonomyIdsHavingRP();
  
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingReadPermission);
}
