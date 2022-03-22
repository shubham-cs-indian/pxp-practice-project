package com.cs.core.config.interactor.model.dashboardtabs;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.dashboardtabs.DashboardTab;
import com.cs.core.config.interactor.entity.dashboardtabs.IDashboardTab;

public class DashboardTabResponseModel extends ConfigResponseWithAuditLogModel implements IDashboardTabResponseModel {
  
  private static final long serialVersionUID = 1L;
  protected IDashboardTab            dashboardTab;
  
  public DashboardTabResponseModel()
  {
    dashboardTab = new DashboardTab();
  }
  
  @Override
  public String getLabel()
  {
    return dashboardTab.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    dashboardTab.setLabel(label);
  }
  
  @Override
  public String getCode()
  {
    return dashboardTab.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    dashboardTab.setCode(code);
  }
  
  @Override
  public String getId()
  {
    return dashboardTab.getId();
  }
  
  @Override
  public void setId(String id)
  {
    dashboardTab.setId(id);
  }
  
  @Override
  public Long getVersionId()
  {
    return dashboardTab.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    dashboardTab.getVersionId();
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return dashboardTab.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    dashboardTab.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    dashboardTab.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return dashboardTab.getLastModifiedBy();
  }

  @Override
  public String getIcon()
  {
    return dashboardTab.getIcon();
  }

  @Override
  public void setIcon(String icon)
  {
    dashboardTab.setIcon(icon);
  }
  
  @Override
  public String getIconKey()
  {
    return dashboardTab.getIconKey();
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    dashboardTab.setIconKey(iconKey);
  }

  @Override
  public String getType()
  {
    return dashboardTab.getType();
  }

  @Override
  public void setType(String type)
  {
    dashboardTab.setType(type);
  }
}
