package com.cs.core.runtime.interactor.usecase.taskexecutor;

import com.cs.core.config.interactor.entity.action.IParameters;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import java.util.Map;

public interface IExecutionTask {
  
  public static final String USERS_PARAMETER      = "users";
  
  public static final String ATTRIBUTES_PARAMETER = "attributes";
  
  public static final String TAGS_PARAMETER       = "tags";
  
  public static final String ROLES_PARAMETER      = "roles";
  
  public static final String TYPE_PARAMETER       = "type";
  
  public static final String STATES_PARAMETER     = "states";
  
  public void execute(IModel request, IModel response, Map<String, Object> executionContext,
      IParameters parameters) throws Exception;
  
  public String[] getParameters();
  
  public String getLabel();
}
