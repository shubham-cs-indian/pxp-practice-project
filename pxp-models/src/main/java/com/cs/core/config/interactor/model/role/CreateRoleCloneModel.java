package com.cs.core.config.interactor.model.role;

public class CreateRoleCloneModel implements ICreateRoleCloneModel {
  
  private static final long serialVersionUID = 1L;
  
  protected Boolean         isExactClone = true;
  protected String          label;
  protected String          code;
  protected String          roleId;
  protected Boolean         clonePhysicalCatalogs = true;
  protected Boolean         cloneTaxonomies = true;
  protected Boolean         cloneTargetClasses = true;
  protected Boolean         cloneEnableDashboard = true;
  protected Boolean         cloneKPI = true;
  protected Boolean         cloneEntities = true;
  
  @Override
  public Boolean getIsExactClone()
  {
    return isExactClone;
  }
  
  @Override
  public void setIsExactClone(Boolean isExactClone)
  {
    this.isExactClone = isExactClone;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
  
  @Override
  public String getRoleId()
  {
    return roleId;
  }
  
  @Override
  public void setRoleId(String roleId)
  {
    this.roleId = roleId;
  }
  
  @Override
  public Boolean getClonePhysicalCatalogs()
  {
    return clonePhysicalCatalogs;
  }
  
  @Override
  public void setClonePhysicalCatalogs(Boolean clonePhysicalCatalogs)
  {
    this.clonePhysicalCatalogs = clonePhysicalCatalogs;
  }
  
  @Override
  public Boolean getCloneTaxonomies()
  {
    return cloneTaxonomies;
  }
  
  @Override
  public void setCloneTaxonomies(Boolean cloneTaxonomies)
  {
    this.cloneTaxonomies = cloneTaxonomies;
  }
  
  @Override
  public Boolean getCloneTargetClasses()
  {
    return cloneTargetClasses;
  }
  
  @Override
  public void setCloneTargetClasses(Boolean cloneTargetClasses)
  {
    this.cloneTargetClasses = cloneTargetClasses;
  }
  
  @Override
  public Boolean getCloneEnableDashboard()
  {
    return cloneEnableDashboard;
  }
  
  @Override
  public void setCloneEnableDashboard(Boolean cloneEnableDashboard)
  {
    this.cloneEnableDashboard = cloneEnableDashboard;
  }
  
  @Override
  public Boolean getCloneKPI()
  {
    return cloneKPI;
  }
  
  @Override
  public void setCloneKPI(Boolean cloneKPI)
  {
    this.cloneKPI = cloneKPI;
  }
  
  @Override
  public Boolean getCloneEntities()
  {
    return cloneEntities;
  }
  
  @Override
  public void setCloneEntities(Boolean cloneEntities)
  {
    this.cloneEntities = cloneEntities;
  }
  
}
