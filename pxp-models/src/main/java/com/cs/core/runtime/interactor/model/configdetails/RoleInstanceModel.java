package com.cs.core.runtime.interactor.model.configdetails;

import com.cs.core.runtime.interactor.entity.propertyinstance.IRoleInstance;
import com.cs.core.runtime.interactor.entity.role.IRoleCandidate;
import com.cs.core.runtime.interactor.entity.role.RoleInstance;
import com.cs.core.runtime.interactor.model.instance.AbstractPropertyInstanceModel;

import java.util.List;

public class RoleInstanceModel extends AbstractPropertyInstanceModel implements IRoleInstanceModel {
  
  private static final long serialVersionUID = 1L;
  protected Long            iid;
  
  public RoleInstanceModel()
  {
    super(new RoleInstance());
  }
  
  public RoleInstanceModel(IRoleInstance roleInstance)
  {
    super(roleInstance);
  }
  
  public List<? extends IRoleCandidate> getCandidates()
  {
    return ((IRoleInstance) entity).getCandidates();
  }
  
  public void setCandidates(List<? extends IRoleCandidate> candidates)
  {
    ((IRoleInstance) entity).setCandidates(candidates);
  }
  
  public String getRoleId()
  {
    return ((IRoleInstance) entity).getRoleId();
  }
  
  public void setRoleId(String roleId)
  {
    ((IRoleInstance) entity).setRoleId(roleId);
  }
  
  /*  @JsonProperty("roleMappingId")
  @Override
  public String getMappingId()
  {
    return (entity).getMappingId();
  }
  
  @JsonProperty("roleMappingId")
  @Override
  public void setMappingId(String mappingId)
  {
    (entity).setMappingId(mappingId);
  }*/
  
  @Override
  public Long getLastModified()
  {
    return ((RoleInstance) entity).getLastModified();
  }
  
  @Override
  public void setLastModified(Long lastModified)
  {
    ((RoleInstance) entity).setLastModified(lastModified);
  }
  
  @Override
  public String getKlassInstanceId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setKlassInstanceId(String klassInstanceId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getVariantInstanceId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVariantInstanceId(String variantInstanceId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getIid()
  {
    return iid;
  }
  
  @Override
  public void setIid(Long iid)
  {
    this.iid = iid;
  }
}
