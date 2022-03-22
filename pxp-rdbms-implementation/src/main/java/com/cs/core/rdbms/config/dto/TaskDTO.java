package com.cs.core.rdbms.config.dto;

import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.csexpress.definition.CSEProperty;
import com.cs.core.rdbms.config.idto.ITaskDTO;
import com.cs.core.rdbms.config.idto.IContextDTO.ContextType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;

import java.io.Serializable;

public class TaskDTO extends RootConfigDTO implements ITaskDTO, Serializable {
  
  private static final long serialVersionUID = 1L;
  private ITaskDTO.TaskType taskType         = TaskType.UNDEFINED;
  
  /**
   * Enabled default constructor
   */
  public TaskDTO()
  {
  }
  
  /**
   * Task DTO
   *
   * @param code
   * @param taskType
   */
  public TaskDTO(String code, ITaskDTO.TaskType taskType)
  {
    super(code);
    this.taskType = taskType;
  }
  
  @Override
  public ICSEElement toCSExpressID() throws CSFormatException
  {
    CSEObject cse = new CSEObject(CSEObjectType.Task);
    return initCSExpression(cse, taskType.toString());
  }
  
  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException
  {
    CSEObject tcse = (CSEObject) cse;
    fromCSExpression(tcse);
    this.taskType = tcse.getSpecification(TaskType.class, ICSEElement.Keyword.$type);
  }
  
  @Override
  public TaskType getTaskType()
  {
    return taskType;
  }
  
  public void setTaskType(TaskType taskType)
  {
    this.taskType = taskType;
  }
  
  @Override
  public int compareTo(Object t)
  {
    int compare = super.compareTo(t);
    return compare != 0 ? compare : this.taskType.ordinal() - ((TaskDTO) t).taskType.ordinal();
  }
  
  @Override
  public boolean equals(Object obj)
  {
    boolean equal = super.equals(obj);
    return equal ? this.taskType == ((TaskDTO) obj).taskType : false;
  }
}
