package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.config.interactor.model.role.IRoleModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;
import com.cs.core.runtime.interactor.model.templating.AbstractGetConfigDetailsModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SaveInstanceStrategyListModel implements ISaveInstanceStrategyListModel {
  
  private static final long                  serialVersionUID = 1L;
  protected Map<String, Object>              variantAndBranchListMap;
  protected IKlassInstanceSaveModel          klassInstance;
  protected Boolean                          isRollback;
  protected Collection<? extends IRoleModel> roles;
  protected String                           loginUserId;
  protected IGetConfigDetailsModel           configDetails;
  protected Boolean                          isTypeSwitch;
  protected Boolean                          isCreateTanslatableInstance;
  
  @Override
  public Boolean getIsRollback()
  {
    return isRollback;
  }
  
  @Override
  public void setIsRollback(Boolean isRollback)
  {
    this.isRollback = isRollback;
  }
  
  @Override
  public IKlassInstanceSaveModel getKlassInstance()
  {
    return this.klassInstance;
  }
  
  @Override
  public void setKlassInstance(IKlassInstanceSaveModel klassInstance)
  {
    this.klassInstance = (IKlassInstanceSaveModel) klassInstance;
  }
  
  @Override
  public Map<String, Object> getVariantAndBranchListMap()
  {
    if (variantAndBranchListMap == null) {
      variantAndBranchListMap = new HashMap<>();
    }
    return variantAndBranchListMap;
  }
  
  @Override
  public void setVariantAndBranchListMap(Map<String, Object> variantAndBranchListMap)
  {
    this.variantAndBranchListMap = variantAndBranchListMap;
  }
  
  @Override
  public Collection<? extends IRoleModel> getRoles()
  {
    return roles;
  }
  
  @Override
  public void setRoles(Collection<? extends IRoleModel> roles)
  {
    this.roles = roles;
  }
  
  @Override
  public IGetConfigDetailsModel getConfigDetails()
  {
    return configDetails;
  }
  
  @Override
  @JsonDeserialize(as = AbstractGetConfigDetailsModel.class)
  public void setConfigDetails(IGetConfigDetailsModel configDetails)
  {
    this.configDetails = configDetails;
  }
  
  @Override
  public Boolean getIsTypeSwitch()
  {
    return isTypeSwitch;
  }
  
  @Override
  public void setIsTypeSwitch(Boolean isTypeSwitch)
  {
    this.isTypeSwitch = isTypeSwitch;
  }
  
  @Override
  public Boolean getIsCreateTanslatableInstance()
  {
    if (isCreateTanslatableInstance == null) {
      isCreateTanslatableInstance = false;
    }
    return isCreateTanslatableInstance;
  }
  
  @Override
  public void setIsCreateTanslatableInstance(Boolean isCreateTanslatableInstance)
  {
    this.isCreateTanslatableInstance = isCreateTanslatableInstance;
  }
}
