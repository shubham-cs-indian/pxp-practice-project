package com.cs.di.common.test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.di.workflow.constants.MessageType;
import com.cs.di.workflow.model.executionstatus.IExecutionStatus;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.di.workflow.model.WorkflowTaskModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class DiTestUtil {
	/** 
	 * to create dummy task Model
	 * @param taskId
	 * @param keys
	 * @param values
	 * @return
	 */
	public static WorkflowTaskModel createTaskModel(String taskId, String[] keys, Object[] values) {
		WorkflowTaskModel model = new WorkflowTaskModel();
		model.setTaskId(taskId);
		for (int i = 0; i < keys.length; i++) {
			model.getInputParameters().put(keys[i], values[i]);
		}
		return model;
	}
	
	/**
	 * To verify the error messages received
	 * with expected message
	 * @param model
	 * @param messageType
	 * @param code
	 * @param message
	 * @return
	 */
	public static boolean verifyExecutionStatus(WorkflowTaskModel model, MessageType messageType, String message) {
		Map<MessageType, ?> map = model.getExecutionStatusTable().getExecutionStatusTableMap();
		List<?> list = (List<?>) map.get(messageType);
		for (Object statusModelObj : list) {
		  IOutputExecutionStatusModel statusModel = (IOutputExecutionStatusModel) statusModelObj; 
			if (statusModel.toString().equals(message)) {
			  System.out.println(statusModel.toString());
				return true;
			}
		}
		return false;
	}
  
  /**
   * To verify the error messages received with expected message
   * 
   * @param model
   * @param messageType
   * @param code
   * @param message
   * @return
   */
  public static boolean verifyExecutionStatus2(
      IExecutionStatus<? extends IOutputExecutionStatusModel> model, MessageType messageType,
      String message)
  {
    Map<MessageType, ?> map = model.getExecutionStatusTableMap();
    System.out.println(" executionStatusTable " + map);
    List<?> list = (List<?>) map.get(messageType);
    for (Object statusModelObj : list) {
      IOutputExecutionStatusModel statusModel = (IOutputExecutionStatusModel) statusModelObj;
      System.out.println(statusModel.toString());
      if (statusModel.toString()
          .equals(message)) {
        return true;
      }
    }
    return false;
    
  }
  
  public static String filterExecutionStatus(WorkflowTaskModel model, MessageType messageType) {
    Gson gson = new Gson();
    Map<MessageType, ?> map = model.getExecutionStatusTable().getExecutionStatusTableMap();
    Map<MessageType, ?> result = map.entrySet().stream()
        .filter(x -> x.getKey().equals(messageType))
        .collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
    System.out.println("Execution Status JSON "+gson.toJson(result));
    return gson.toJson(result);
  }
  
  /**
   * This would help to get the count 
   * for records for specified MessageType e.g. ERROR
   * @param json ExecutionStatus stored as JSON
   * in DB column/String with Message Type as Key e.g. ERROR
   * @param msgType to filter Data
   * @return count for specified MessageType 
   * @throws IOException
   */
  public static int getMessageTypeCount(String json, MessageType msgType)
  {
    List<?> list = Collections.EMPTY_LIST;
    try {
      ObjectMapper mapper = new ObjectMapper();
      Map<MessageType, ?> map = mapper.readValue(json, new TypeReference<Map<MessageType, ?>>()
      {
      });
      list = (List<?>) map.get(msgType);
      // TO DO: if possibility of Map giving null then
      // (ObjectUtils.equals(map.get(msgType), null)) ? Collections.EMPTY_LIST :(List<?>) map.get(msgType);
    }
    catch (Exception jsonExp) {
      RDBMSLogger.instance().exception(jsonExp);
      return 0;
    }
    return list.isEmpty() ? 0 : list.size();
  } 
}
