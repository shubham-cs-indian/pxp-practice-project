package com.cs.config.idto;

import java.util.Collection;

import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.rdbms.config.idto.IContextDTO.ContextType;
import com.cs.core.technical.ijosn.IJSONContent;

/**
 * Context DTO from the configuration realm
 *
 * @author vallee
 */
public interface IConfigContextDTO extends IConfigJSONDTO {

  public String getLabel();
  
  public void setLabel(String label);

  public boolean isAutoCreate();
  
  public void setIsAutoCreate(boolean isAutoCreate);

  public boolean isTimeEnabled();
  
  public void setIsTimeEnabled(boolean isTimeEnabled);

  public long getDefaultStartTime();
  
  public void setDefaultStartTime(long defaultStartTime);

  public long getDefaultEndTime();
  
  public void setDefaultEndTime(long defaultEndTime);

  public boolean isCurrentTime();
  
  public void setIsCurrentTime(boolean isCurrentTime);

  public boolean isDuplicateVariantAllowed();
  
  public void setIsDuplicateVariantAllowed(boolean isDuplicateVariantAllowed);

  public String getTab();
  
  public void setTab(String tab);

  public String getType();
  
  public IJSONContent getTagCodes();
  
  public void setTagCodes(IJSONContent tagCodes);

  public Collection<String> getEntities();
  
  public void setEntities(Collection<String> entities);
  
  public IContextDTO getContextDTO();
  
  public void setContextDTO(String contextCode, ContextType type);
  
  public String getIcon();
  
  public void setIcon(String icon);
}
