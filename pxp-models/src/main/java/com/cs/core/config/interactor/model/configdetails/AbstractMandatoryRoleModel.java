package com.cs.core.config.interactor.model.configdetails;

import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.datarule.IMandatoryRole;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.config.interactor.model.role.IMandatoryRoleModel;

public class AbstractMandatoryRoleModel extends AbstractRoleModel implements IMandatoryRoleModel {
  
  private static final long serialVersionUID = 1L;
  
  public AbstractMandatoryRoleModel(IMandatoryRole role)
  {
    super(role);
  }
  
  @Override
  public String getCode()
  {
    return role.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    role.setCode(code);
  }
  
  @Override
  public List<String> getKlassType()
  {
    return ((IMandatoryRole) role).getKlassType();
  }
  
  @Override
  public void setKlassType(List<String> klassType)
  {
    ((IMandatoryRole) role).setKlassType(klassType);
  }
  
  @Override
  public Boolean getIsMultiselect()
  {
    return role.getIsMultiselect();
  }
  
  @Override
  public void setIsMultiselect(Boolean isMultiselect)
  {
    role.setIsMultiselect(isMultiselect);
  }
  
  @Override
  public Map<String, IGlobalPermission> getGlobalPermission()
  {
    return role.getGlobalPermission();
  }
  
  @Override
  public void setGlobalPermission(Map<String, IGlobalPermission> globalPermission)
  {
    role.setGlobalPermission(globalPermission);
  }
  
  @Override
  public List<? extends IUser> getUsers()
  {
    return role.getUsers();
  }
  
  @Override
  public void setUsers(List<? extends IUser> users)
  {
    role.setUsers(users);
  }
  
  @Override
  public String getLandingScreen()
  {
    return role.getLandingScreen();
  }
  
  @Override
  public void setLandingScreen(String landingScreen)
  {
    role.setLandingScreen(landingScreen);
  }
  
  @Override
  public Boolean getIsDashboardEnable()
  {
    return role.getIsDashboardEnable();
  }
  
  @Override
  public void setIsDashboardEnable(Boolean isDashboardVisible)
  {
    role.setIsDashboardEnable(isDashboardVisible);
  }

  @Override
  public Boolean getIsBackgroundRole()
  {
    return role.getIsBackgroundRole();
  }

  @Override
  public void setIsBackgroundRole(Boolean isBackgroundRole)
  {
    role.setIsBackgroundRole(isBackgroundRole);
    
  }
}
