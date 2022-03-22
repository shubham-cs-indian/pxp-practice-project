package com.cs.config.idto;

import com.cs.core.rdbms.config.idto.ITaskDTO.TaskType;

/**
 * Task DTO from the configuration realm
 *
 * @author janak
 */
public interface IConfigTaskDTO extends IConfigJSONDTO {
  
  public String getColor();
  
  public void setColor(String color);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getIcon();
  
  public void setIcon(String icon);
  
  public String getPriorityTag();
  
  public void setPriorityTag(String priorityTag);
  
  public String getStatusTag();
  
  public void setStatusTag(String statusTag);
  
  public String getType();
  
  public void setTaskDTO(String code, TaskType type);
  
  public String getCode();
}
