package com.cs.config.dto;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import com.cs.config.idto.IConfigTaskDTO;
import com.cs.config.standard.IConfigMap;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.dto.TaskDTO;
import com.cs.core.rdbms.config.idto.ITaskDTO.TaskType;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;

public class ConfigTaskDTO extends AbstractConfigJSONDTO implements IConfigTaskDTO{
  
  private static final long                       serialVersionUID  = 1L;
  private static final BidiMap<ConfigTag, String> KEY_MAP           = new DualHashBidiMap<>();
  private static final Set<ConfigTag>             IGNORED_PXON_TAGS = new HashSet<>();
  
  private final TaskDTO                           taskDTO           = new TaskDTO();
 
  /** Initializing the static key map */
  static {
    // Read only Properties
    KEY_MAP.put(ConfigTag.code, IPXON.PXONTag.code.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.type, IPXON.PXONTag.type.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.statusTag, IPXON.PXONTag.statustag.toReadOnlyTag());
    
    // update only Properties
    KEY_MAP.put(ConfigTag.label, IPXON.PXONTag.label.toTag());
    KEY_MAP.put(ConfigTag.color, IPXON.PXONTag.color.toTag());
    KEY_MAP.put(ConfigTag.priorityTag, IPXON.PXONTag.prioritytag.toTag());
    KEY_MAP.put(ConfigTag.icon, IPXON.PXONTag.icon.toTag());
    
    IGNORED_PXON_TAGS.add(ConfigTag.code);
    IGNORED_PXON_TAGS.add(ConfigTag.type);
  }
  
  public ConfigTaskDTO()
  {
    super(KEY_MAP, IGNORED_PXON_TAGS);
  }


  @Override
  public String getColor()
  {
    return getString(ConfigTag.color);
  }

  @Override
  public String getLabel()
  {
    return getString(ConfigTag.label);
  }

  @Override
  public String getIcon()
  {
    return getString(ConfigTag.icon);
  }

  @Override
  public String getPriorityTag()
  {
    return getString(ConfigTag.priorityTag);
  }

  @Override
  public String getStatusTag()
  {
    return getString(ConfigTag.statusTag);
  }

  @Override
  public String getType()
  {
    return getString(ConfigTag.type);
  }

  @Override
  public void fromJSON(String json) throws CSFormatException {
    super.fromJSON(json);
    initContextDTO();
  }

  private void initContextDTO()
  {
    taskDTO.setCode(getString(ConfigTag.code));
    taskDTO.setTaskType(IConfigMap.getTaskType(getString(ConfigTag.type)));
  }

  @Override
  public ICSEElement toCSExpressID() throws CSFormatException {
    return taskDTO.toCSExpressID();
  }

  @Override
  void fromCSExpression (JSONContentParser parser) throws CSFormatException {
    fromCSExpressID( parser.getCSEElement(IPXON.PXONTag.csid.toTag()));
  }

  @Override
  public void setColor(String color)
  {
    setString(ConfigTag.color, color);
  }

  @Override
  public void setLabel(String label)
  {
    setString(ConfigTag.label, label);
  }


  @Override
  public void setIcon(String icon)
  {
    setString(ConfigTag.icon, icon);
  }


  @Override
  public void setPriorityTag(String priorityTag)
  {
    setString(ConfigTag.priorityTag, priorityTag);
  }


  @Override
  public void setStatusTag(String statusTag)
  {
    setString(ConfigTag.statusTag, statusTag);
  }
  
  @Override
  public void setTaskDTO(String code, TaskType type) 
  {
    taskDTO.setCode(code);
    taskDTO.setTaskType(type);
  }
}
