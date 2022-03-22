package com.cs.core.config.interactor.entity.standard.role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.globalpermissions.GlobalPermission;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.config.interactor.entity.user.User;
import com.cs.core.runtime.interactor.constants.SystemLabels;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Component()
@Scope("prototype")
public class AdminRole extends AbstractMandatoryRole implements IAdminRole {
  
  private static final long                serialVersionUID = 1L;
  protected String                         id               = SystemLevelIds.ADMIN_ROLE;
  protected String                         label            = SystemLabels.ADMIN_ROLE_LABEL;
  protected String                         description;
  protected String                         tooltip;
  protected Boolean                        isMandatory      = false;
  protected Boolean                        isStandard       = true;
  protected String                         placeholder;
  protected String                         icon;
  protected String                         type             = this.getClass()
      .getName();
  protected Boolean                        isMultiselect    = true;
  protected Map<String, IGlobalPermission> globalPermission;
  protected List<? extends IUser>          users;
  protected Long                           versionId        = 0l;
  protected Long                           versionTimestamp = 0l;
  protected String                         lastModifiedBy;
  protected List<String>                   entities;
  protected String                         roleType         = CommonConstants.ADMIN;
  
  public AdminRole()
  {
  }
  
  public AdminRole(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getRoleType()
  {
    return roleType;
  }
  
  @Override
  public void setRoleType(String roleType)
  {
    this.roleType = roleType;
  }
  
  @Override
  public List<String> getEntities()
  {
    if (entities == null) {
      entities = new ArrayList<>();
    }
    return entities;
  }
  
  @Override
  public void setEntities(List<String> entities)
  {
    this.entities = entities;
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
  }
  
  @Override
  public String getDescription()
  {
    return this.description;
  }
  
  @Override
  public void setDescription(String description)
  {
    this.description = description;
  }
  
  @Override
  public String getTooltip()
  {
    return this.tooltip;
  }
  
  @Override
  public void setTooltip(String tooltip)
  {
    this.tooltip = tooltip;
  }
  
  @Override
  public Boolean getIsMandatory()
  {
    return this.isMandatory;
  }
  
  @Override
  public void setIsMandatory(Boolean isMandatory)
  {
    this.isMandatory = isMandatory;
  }
  
  @Override
  public String getPlaceholder()
  {
    return this.placeholder;
  }
  
  @Override
  public void setPlaceholder(String placeholder)
  {
    this.placeholder = placeholder;
  }
  
  @Override
  public String getLabel()
  {
    return this.label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getIcon()
  {
    return this.icon;
  }
  
  @Override
  public void setIcon(String icon)
  {
    this.icon = icon;
  }
  
  @Override
  public String getId()
  {
    return this.id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getType()
  {
    return this.type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public Boolean getIsMultiselect()
  {
    return this.isMultiselect;
  }
  
  @Override
  public void setIsMultiselect(Boolean isMultiselect)
  {
    this.isMultiselect = isMultiselect;
  }
  
  @Override
  public Map<String, IGlobalPermission> getGlobalPermission()
  {
    if (globalPermission == null) {
      globalPermission = new HashMap<String, IGlobalPermission>();
    }
    return globalPermission;
  }
  
  @JsonDeserialize(contentAs = GlobalPermission.class)
  @Override
  public void setGlobalPermission(Map<String, IGlobalPermission> globalPermission)
  {
    this.globalPermission = globalPermission;
  }
  
  @JsonDeserialize(contentAs = User.class)
  @Override
  public List<? extends IUser> getUsers()
  {
    if (users == null) {
      users = new ArrayList<IUser>();
    }
    return users;
  }
  
  @JsonDeserialize(contentAs = User.class)
  @Override
  public void setUsers(List<? extends IUser> users)
  {
    this.users = users;
  }
  
  @Override
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
  }
  
  @Override
  public Boolean getIsStandard()
  {
    return this.isStandard;
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    this.isStandard = isStandard;
  }
}
