package com.cs.core.config.businessapi.task;

import com.cs.config.businessapi.base.Validations;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.exception.*;
import com.cs.core.rdbms.config.idto.ITaskDTO;

import javax.naming.InvalidNameException;

public class TaskValidations extends Validations {

  public static Boolean  isTypeValid(String taskType)
  {
    try {
      ITaskDTO.TaskType type = ITaskDTO.TaskType.valueOf(taskType.toUpperCase());
      return type.equals(ITaskDTO.TaskType.PERSONAL) || type.equals(ITaskDTO.TaskType.SHARED);
    }
    catch (IllegalArgumentException exception) {
      return false;
    }
  }

  public static boolean isColorValid(String color)
  {
    if(isEmpty(color)){
      return true;
    }
    return ITaskDTO.TaskColor.getColors().contains(color);
  }

  public static void validateTask(ITaskModel task, Boolean isCreate) throws Exception
  {
    if (isEmpty(task.getId())) {
      throw new InvalidIdException();
    }
    if (!isCodeValid(task.getCode())) {
      throw new InvalidCodeException();
    }
    if (isEmpty(task.getLabel())) {
      throw new InvalidNameException();
    }
    if (!isTypeValid(task.getType())) {
      throw new InvalidTypeException();
    }
    if (!isCreate) {
      if (!isColorValid(task.getColor())) {
        throw new InvalidColorException();
      }
      if (task.getType().equals(ITaskDTO.TaskType.SHARED.name().toLowerCase()) && task.getStatusTag().isEmpty()) {
        throw new EmptyStatusTagException();
      }
    }
  }

}
