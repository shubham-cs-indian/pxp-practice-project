package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.role.IRoleModel;
import com.cs.core.runtime.interactor.model.typeswitch.IGetAllKlassInstancesVariantsModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GetAllKlassInstancesVariantsModel implements IGetAllKlassInstancesVariantsModel {
  
  protected Collection<String>   dimensionalTagIds;
  protected String               currentUserId;
  protected List<IRoleModel>     roles;
  protected List<String>         ids;
  protected String               parent;
  protected int                  variantId;
  protected List<IDataRuleModel> dataRulesOfKlass;
  
  @Override
  public Collection<String> getDimensionalTagIds()
  {
    if (dimensionalTagIds == null) {
      dimensionalTagIds = new ArrayList<String>();
    }
    return dimensionalTagIds;
  }
  
  @Override
  public void setDimensionalTagIds(Collection<String> dimensionalTagIds)
  {
    this.dimensionalTagIds = dimensionalTagIds;
  }
  
  @Override
  public String getCurrentUserId()
  {
    return currentUserId;
  }
  
  @Override
  public void setCurrentUserId(String currentUserId)
  {
    this.currentUserId = currentUserId;
  }
  
  @Override
  public List<IRoleModel> getRoles()
  {
    if (roles == null) {
      roles = new ArrayList<IRoleModel>();
    }
    return roles;
  }
  
  @Override
  public void setRoles(List<IRoleModel> roles)
  {
    this.roles = roles;
  }
  
  @Override
  public List<String> getIds()
  {
    if (ids == null) {
      ids = new ArrayList<String>();
    }
    return ids;
  }
  
  @Override
  public void setIds(List<String> ids)
  {
    this.ids = ids;
  }
  
  @Override
  public int getVariantId()
  {
    return variantId;
  }
  
  @Override
  public void setVariantId(int variantId)
  {
    this.variantId = variantId;
  }
  
  @Override
  public String getParent()
  {
    return parent;
  }
  
  @Override
  public void setParent(String parent)
  {
    this.parent = parent;
  }
  
  @Override
  public List<IDataRuleModel> getDataRulesOfKlass()
  {
    if (dataRulesOfKlass == null) {
      dataRulesOfKlass = new ArrayList<>();
    }
    return dataRulesOfKlass;
  }
  
  @Override
  public void setDataRulesOfKlass(List<IDataRuleModel> dataRulesOfKlass)
  {
    this.dataRulesOfKlass = dataRulesOfKlass;
  }
}
