package com.cs.core.runtime.interactor.model.configdetails;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.config.interactor.model.xray.XRayConfigDetailsModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ConfigDetailsForInstanceTreeGetModel implements IConfigDetailsForInstanceTreeGetModel {
  
  private static final long         serialVersionUID = 1L;
  
  protected Set<String>             allowedEntities;
  protected Set<String>             klassIdsHavingRP;
  protected Set<String>             dimensionalTagIds;
  protected List<String>            masterKlassIds;
  protected IXRayConfigDetailsModel xRayConfigDetails;
  protected Set<String>             taxonomyIdsHavingRP;
  protected Set<String>             klassIdsHavingCP;
  protected Map<String, Object>     taskIdsForRolesHavingReadPermission;
  protected Set<String>             taskIdsHavingReadPermissions;
  protected Set<String>             personalTaskIds;
  protected List<String>            klassIdsForKPI;
  protected List<String>            taxonomyIdsForKPI;
  protected String                  roleIdOfCurrentUser;
  protected List<String>            side2LinkedVariantKrIds;
  protected List<String>            linkedVariantCodes;
  protected List<String>            majorTaxonomyIds;

  
  public List<String> getMasterKlassIds()
  {
    return masterKlassIds;
  }
  
  public void setMasterKlassIds(List<String> masterKlassIds)
  {
    this.masterKlassIds = masterKlassIds;
  }
  
  @Override
  public Set<String> getAllowedEntities()
  {
    return allowedEntities;
  }
  
  @Override
  public void setAllowedEntities(Set<String> allowedEntities)
  {
    this.allowedEntities = allowedEntities;
  }
  
  @Override
  public Set<String> getKlassIdsHavingRP()
  {
    return klassIdsHavingRP;
  }
  
  @Override
  public void setKlassIdsHavingRP(Set<String> klassIdsHavingRP)
  {
    this.klassIdsHavingRP = klassIdsHavingRP;
  }
  
  @Override
  public Set<String> getDimensionalTagIds()
  {
    return dimensionalTagIds;
  }
  
  @Override
  public void setDimensionalTagIds(Set<String> dimensionalTagIds)
  {
    this.dimensionalTagIds = dimensionalTagIds;
  }
  
  @Override
  public IXRayConfigDetailsModel getXRayConfigDetails()
  {
    return xRayConfigDetails;
  }
  
  @JsonDeserialize(as = XRayConfigDetailsModel.class)
  @Override
  public void setXRayConfigDetails(IXRayConfigDetailsModel xRayConfigDetails)
  {
    this.xRayConfigDetails = xRayConfigDetails;
  }
  
  @Override
  public Set<String> getTaxonomyIdsHavingRP()
  {
    return taxonomyIdsHavingRP;
  }
  
  @Override
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP)
  {
    this.taxonomyIdsHavingRP = taxonomyIdsHavingRP;
  }
  
  @Override
  public Set<String> getKlassIdsHavingCP()
  {
    if (klassIdsHavingCP == null) {
      klassIdsHavingCP = new HashSet<>();
    }
    return klassIdsHavingCP;
  }
  
  @Override
  public void setKlassIdsHavingCP(Set<String> klassIdsHavingCP)
  {
    this.klassIdsHavingCP = klassIdsHavingCP;
  }
  
  @Override
  public Set<String> getTaskIdsHavingReadPermissions()
  {
    return taskIdsHavingReadPermissions;
  }
  
  @Override
  public void setTaskIdsHavingReadPermissions(Set<String> taskIdsHavingReadPermissions)
  {
    this.taskIdsHavingReadPermissions = taskIdsHavingReadPermissions;
  }
  
  @Override
  public Map<String, Object> getTaskIdsForRolesHavingReadPermission()
  {
    return taskIdsForRolesHavingReadPermission;
  }
  
  @Override
  public void setTaskIdsForRolesHavingReadPermission(
      Map<String, Object> taskIdsForRolesHavingReadPermission)
  {
    this.taskIdsForRolesHavingReadPermission = taskIdsForRolesHavingReadPermission;
  }
  
  @Override
  public Set<String> getPersonalTaskIds()
  {
    return personalTaskIds;
  }
  
  @Override
  public void setPersonalTaskIds(Set<String> personalTaskIds)
  {
    this.personalTaskIds = personalTaskIds;
  }
  
  @Override
  public List<String> getKlassIdsForKPI()
  {
    if (klassIdsForKPI == null) {
      klassIdsForKPI = new ArrayList<>();
    }
    return klassIdsForKPI;
  }
  
  @Override
  public void setKlassIdsForKPI(List<String> klassIdsForKPI)
  {
    this.klassIdsForKPI = klassIdsForKPI;
  }
  
  @Override
  public List<String> getTaxonomyIdsForKPI()
  {
    if (taxonomyIdsForKPI == null) {
      taxonomyIdsForKPI = new ArrayList<>();
    }
    return taxonomyIdsForKPI;
  }
  
  @Override
  public void setTaxonomyIdsForKPI(List<String> taxonomyIdsForKPI)
  {
    this.taxonomyIdsForKPI = taxonomyIdsForKPI;
  }
  
  @Override
  public String getRoleIdOfCurrentUser()
  {
    return roleIdOfCurrentUser;
  }
  
  @Override
  public void setRoleIdOfCurrentUser(String roleIdOfCurrentUser)
  {
    this.roleIdOfCurrentUser = roleIdOfCurrentUser;
  }
  
  @Override
  public List<String> getSide2LinkedVariantKrIds()
  {
    if (side2LinkedVariantKrIds == null) {
      side2LinkedVariantKrIds = new ArrayList<String>();
    }
    return side2LinkedVariantKrIds;
  }
  
  @Override
  public void setSide2LinkedVariantKrIds(List<String> side2LinkedVariantKrIds)
  {
    this.side2LinkedVariantKrIds = side2LinkedVariantKrIds;
  }
  
  @Override
  public List<String> getLinkedVariantCodes()
  {
    if (linkedVariantCodes == null) {
    	linkedVariantCodes = new ArrayList<String>();
    }
    return linkedVariantCodes;
  }
  
  @Override
  public void setLinkedVariantCodes(List<String> linkedVariantCodes)
  {
    this.linkedVariantCodes = linkedVariantCodes;
  }
  
  @Override
  public List<String> getMajorTaxonomyIds() {
    if(majorTaxonomyIds == null) {
       majorTaxonomyIds = new ArrayList<String>();
    }
    return majorTaxonomyIds;
  }

  @Override
  public void setMajorTaxonomyIds(List<String> majorTaxonomyIds) {
    this.majorTaxonomyIds = majorTaxonomyIds;
  }
}
