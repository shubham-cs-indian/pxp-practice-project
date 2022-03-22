package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeStrategyModel;

import java.util.List;
import java.util.Set;

public interface IVariantLinkedInstancesQuickListModel extends IGetKlassInstanceTreeStrategyModel {
  
  public static final String VARIANT_INSTANCE_ID = "variantInstanceId";
  public static final String ENTITIES            = "entities";
  public static final String CONTEXT_ID          = "contextId";
  public static final String BASETYPE            = "baseType";
  public static final String ID                  = "id";
  public static final String LINKED_INSTANCES    = "linkedInstances";
  public static final String ENTITY_ID           = "entityId";
  public static final String ALLOWED_ENTITIES    = "allowedEntities";
  public static final String KLASS_IDS_HAVING_RP = "klassIdsHavingRP";
  
  public Set<String> getAllowedEntities();
  
  public void setAllowedEntities(Set<String> allowedEntities);
  
  public Set<String> getKlassIdsHavingRP();
  
  public void setKlassIdsHavingRP(Set<String> klassIdsHavingReadPermission);
  
  public String getVariantInstanceId();
  
  public void setVariantInstanceId(String variantInstanceId);
  
  public List<String> getEntities();
  
  public void setEntities(List<String> entities);
  
  public String getContextId();
  
  public void setContextId(String contextId);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public List<String> getLinkedInstances();
  
  public void setLinkedInstances(List<String> linkedInstances);
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
}
