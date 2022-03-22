package com.cs.core.runtime.interactor.model.configdetails;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IConfigDetailsForInstanceTreeGetModel extends IModel {
  
  public static final String ALLOWED_ENTITIES                          = "allowedEntities";
  public static final String KLASS_IDS_HAVING_RP                       = "klassIdsHavingRP";
  public static final String DIMENSIONAL_TAG_IDS                       = "dimensionalTagIds";
  public static final String MASTER_KLASS_IDS                          = "masterKlassIds";
  public static final String X_RAY_CONFIG_DETAILS                      = "xrayConfigDetails";
  public static final String TAXONOMY_IDS_HAVING_RP                    = "taxonomyIdsHavingRP";
  public static final String KLASS_IDS_HAVING_CP                       = "klassIdsHavingCP";
  public static final String TASK_IDS_FOR_ROLES_HAVING_READ_PERMISSION = "taskIdsForRolesHavingReadPermission";
  public static final String TASK_IDS_HAVING_READ_PERMISSION           = "taskIdsHavingReadPermissions";
  public static final String PERSONAL_TASK_IDS                         = "personalTaskIds";
  public static final String TAXONOMY_IDS_FOR_KPI                      = "taxonomyIdsForKPI";
  public static final String KLASS_IDS_FOR_KPI                         = "klassIdsForKPI";
  public static final String ROLE_ID_OF_CURRENT_USER                   = "roleIdOfCurrentUser";
  public static final String SIDE2_LINKED_VARIANT_KR_IDS               = "side2LinkedVariantKrIds";
  public static final String LINKED_VARIANT_CODES                      = "linkedVariantCodes";
  public static final String MAJOR_TAXONOMY_IDS                        = "majorTaxonomyIds";
  
  public Set<String> getAllowedEntities();
  
  public void setAllowedEntities(Set<String> allowedEntites);
  
  public Set<String> getKlassIdsHavingRP();
  
  public void setKlassIdsHavingRP(Set<String> klassIdsWithRP);
  
  public Set<String> getDimensionalTagIds();
  
  public void setDimensionalTagIds(Set<String> dimensionalTagIds);
  
  public List<String> getMasterKlassIds();
  
  public void setMasterKlassIds(List<String> masterKlassIds);
  
  public IXRayConfigDetailsModel getXRayConfigDetails();
  
  public void setXRayConfigDetails(IXRayConfigDetailsModel xRayConfigDetails);
  
  public Set<String> getTaxonomyIdsHavingRP();
  
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP);
  
  public Set<String> getKlassIdsHavingCP();
  
  public void setKlassIdsHavingCP(Set<String> klassIdsHavingCP);
  
  public Map<String, Object> getTaskIdsForRolesHavingReadPermission();
  
  public void setTaskIdsForRolesHavingReadPermission(
      Map<String, Object> taskIdsForRolesHavingReadPermission);
  
  public Set<String> getTaskIdsHavingReadPermissions();
  
  public void setTaskIdsHavingReadPermissions(Set<String> taskIdsHavingReadPermissions);
  
  public Set<String> getPersonalTaskIds();
  
  public void setPersonalTaskIds(Set<String> personalTaskIds);
  
  public List<String> getKlassIdsForKPI();
  
  public void setKlassIdsForKPI(List<String> klassIdsForKPI);
  
  public List<String> getTaxonomyIdsForKPI();
  
  public void setTaxonomyIdsForKPI(List<String> taxonomyIdsForKPI);
  
  public String getRoleIdOfCurrentUser();
  
  public void setRoleIdOfCurrentUser(String roleIdsOfCurrentUser);
  
  public List<String> getSide2LinkedVariantKrIds();
  
  public void setSide2LinkedVariantKrIds(List<String> side2LinkedVariantKrIds);
  
  public List<String> getLinkedVariantCodes();
  
  public void setLinkedVariantCodes(List<String> linkedVariantIds);
  
  public List<String> getMajorTaxonomyIds();
  
  public void setMajorTaxonomyIds(List<String> majorTaxonomyIds);
}
