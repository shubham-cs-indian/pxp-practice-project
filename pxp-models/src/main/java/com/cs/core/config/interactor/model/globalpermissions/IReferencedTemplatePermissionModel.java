package com.cs.core.config.interactor.model.globalpermissions;

import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.template.IHeaderPermission;
import com.cs.core.config.interactor.entity.template.ITabPermission;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;
import java.util.Set;

public interface IReferencedTemplatePermissionModel extends IModel {
  
  public static final String TAB_PERMISSION                            = "tabPermission";
  public static final String HEADER_PERMISSION                         = "headerPermission";
  public static final String VISIBLE_PROPERTY_COLLECTION_IDS           = "visiblePropertyCollectionIds";
  public static final String EDITABLE_PROPERTY_COLLECTION_IDS          = "editablePropertyCollectionIds";
  public static final String EXPANDABLE_PROPERTY_COLLECTION_IDS        = "expandablePropertyCollectionIds";
  public static final String VISIBLE_PROPERTY_IDS                      = "visiblePropertyIds";
  public static final String EDITABLE_PROPERTY_IDS                     = "editablePropertyIds";
  public static final String VISIBLE_RELATIONSHIP_IDS                  = "visibleRelationshipIds";
  public static final String CAN_ADD_RELATIONSHIP_IDS                  = "canAddRelationshipIds";
  public static final String CAN_DELETE_RELATIONSHIP_IDS               = "canDeleteRelationshipIds";
  public static final String CAN_EDIT_CONTEXT_RELATIONSHIP_IDS         = "canEditContextRelationshipIds";
  public static final String GLOBAL_PERMISSION                         = "globalPermission";
  public static final String VISIBLE_TAB_TYPES                         = "visibleTabTypes";
  public static final String TASK_IDS_FOR_ROLES_HAVING_READ_PERMISSION = "taskIdsForRolesHavingReadPermission";
  public static final String TASK_IDS_HAVING_READ_PERMISSION           = "taskIdsHavingReadPermissions";
  public static final String ENTITIES_HAVING_RP                        = "entitiesHavingRP";
  public static final String KLASS_IDS_HAVING_RP                       = "klassIdsHavingRP";
  public static final String TAXONOMY_IDS_HAVING_RP                    = "taxonomyIdsHavingRP";
  public static final String ALL_TAXONOMY_IDS_HAVING_RP                = "allTaxonomyIdsHavingRP";
  public static final String KLASS_ID_VS_TEMPLATE_IDS_MAP              = "klassIdVsTemplateIdsMap";
  public static final String FUNCTION_PERMISSION                       = "functionPermission";
  
  public ITabPermission getTabPermission();
  
  public void setTabPermission(ITabPermission tabPermission);
  
  public IHeaderPermission getHeaderPermission();
  
  public void setHeaderPermission(IHeaderPermission headerPermission);
  
  public Set<String> getVisiblePropertyCollectionIds();
  
  public void setVisiblePropertyCollectionIds(Set<String> visiblePropertyCollectionIds);
  
  public Set<String> getEditablePropertyCollectionIds();
  
  public void setEditablePropertyCollectionIds(Set<String> editablePropertyCollectionIds);
  
  public Set<String> getExpandablePropertyCollectionIds();
  
  public void setExpandablePropertyCollectionIds(Set<String> expandablePropertyCollectionIds);
  
  public Set<String> getVisiblePropertyIds();
  
  public void setVisiblePropertyIds(Set<String> visiblePropertyIds);
  
  public Set<String> getEditablePropertyIds();
  
  public void setEditablePropertyIds(Set<String> editablePropertyIds);
  
  public Set<String> getVisibleRelationshipIds();
  
  public void setVisibleRelationshipIds(Set<String> visibleRelationshipIds);
  
  public Set<String> getCanAddRelationshipIds();
  
  public void setCanAddRelationshipIds(Set<String> canAddRelationshipIds);
  
  public Set<String> getCanDeleteRelationshipIds();
  
  public void setCanDeleteRelationshipIds(Set<String> canDeleteRelationshipIds);
  
  public IGlobalPermission getGlobalPermission();
  
  public void setGlobalPermission(IGlobalPermission globalPermission);
  
  public Set<String> getVisibleTabTypes();
  
  public void setVisibleTabTypes(Set<String> visibleTabTypes);
  
  public Map<String, Object> getTaskIdsForRolesHavingReadPermission();
  
  public void setTaskIdsForRolesHavingReadPermission(
      Map<String, Object> taskIdsForRolesHavingReadPermission);
  
  public Set<String> getTaskIdsHavingReadPermissions();
  
  public void setTaskIdsHavingReadPermissions(Set<String> taskIdsHavingReadPermissions);
  
  public Set<String> getEntitiesHavingRP();
  
  public void setEntitiesHavingRP(Set<String> entitiesHavingReadPermission);
  
  public Set<String> getKlassIdsHavingRP();
  
  public void setKlassIdsHavingRP(Set<String> klassIdsHavingReadPermission);
  
  public Set<String> getTaxonomyIdsHavingRP();
  
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP);
  
  public Set<String> getAllTaxonomyIdsHavingRP();
  
  public void setAllTaxonomyIdsHavingRP(Set<String> allTaxonomyIdsHavingRP);
  
  public Map<String, Set<String>> getKlassIdVsTemplateIdsMap();
  
  public void setKlassIdVsTemplateIdsMap(Map<String, Set<String>> klassIdVsTemplateIdsMap);
  
  public IFunctionPermissionModel getFunctionPermission();
  public void setFunctionPermission(IFunctionPermissionModel functionPermission);
  
  public Set<String> getCanEditContextRelationshipIds();
  public void setCanEditContextRelationshipIds(Set<String> canEditContextRelationshipIds);
}
