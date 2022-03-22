package com.cs.config.strategy.plugin.usecase.importPXON.util;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ImportUtils {
  
  /**
   * add successfully created or updated imported PXON info into successImportList
   * @param successfullyImportedList
   * @param importMap
   */
  public static void addSuccessImportedInfo(List<Map<String, Object>> successfullyImportedList, Map<String, Object> importMap)
  {
    Map<String, Object> successMap = new HashMap<>();
    successMap.put(CommonConstants.CODE_PROPERTY, importMap.get(CommonConstants.CODE_PROPERTY));
    successMap.put(CommonConstants.LABEL_PROPERTY, importMap.get(CommonConstants.LABEL_PROPERTY));
    successfullyImportedList.add(successMap);
  }
  
  /**
   * add failed code and label into failedIDList
   * @param failedIDList
   * @param code
   * @param label
   */
  public static void addToFailureIds(List<Map<String, Object>> failedIDList, String code, String label)
  {
    Map<String, Object> failedRelationshipMap = new HashMap<>();
    failedRelationshipMap.put(CommonConstants.CODE_PROPERTY, code);
    failedRelationshipMap.put(CommonConstants.LABEL_PROPERTY, label);
    failedIDList.add(failedRelationshipMap);
  }
  
  /**
   * log the exception details occurs at import and add failure code
   * @param failure
   * @param failedList
   * @param importMap
   * @param ex
   */
  public static void logExceptionAndFailureIDs(IExceptionModel failure, List<Map<String, Object>> failedList,
      Map<String, Object> importMap, Exception ex)
  {
    String code = (String) importMap.get(CommonConstants.CODE_PROPERTY);
    String label = (String) importMap.get(CommonConstants.LABEL_PROPERTY);
    ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, code, label);
    addToFailureIds(failedList, code, label);
  }
  
  /**
   * Prepare import response map with exceptions, successfully imported code list  and failed code list 
   * @param failure
   * @param successfullyImportedIDMap
   * @param failedCodeList
   * @return
   */
  public static Map<String, Object> prepareImportResponseMap(IExceptionModel failure, List<Map<String, Object>> successfullyImportedIDMap,
      List<Map<String, Object>> failedCodeList)
  {
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IBulkResponseModel.SUCCESS, successfullyImportedIDMap);
    responseMap.put(IBulkResponseModel.FAILURE, failure);
    responseMap.put(IPluginSummaryModel.FAILED_IDS, failedCodeList);
    return responseMap;
  }
  
  public static List<String> getValidNodeCodes(List<String> codes, String entityLabel, IExceptionModel failure, String entityCode)
  {
    if (codes == null)
      codes = new ArrayList<>();
    
    List<String> validCodes = new ArrayList<>();
    for (String code : codes) {
      try {
        UtilClass.getVertexByCode(code, entityLabel);
        validCodes.add(code);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, entityCode, code);
      }
    }
    return validCodes;
    
  }

  public static Map<Boolean, List<String>> prepareDelta(Vertex entityToImport, Direction direction, String relationLabel,
      List<String> importedCodes)
  {
    Map<Boolean, List<String>> isAdded = new HashMap<>();
    Iterable<Vertex> existingNodes = entityToImport.getVertices(direction, relationLabel);

    List<String> existingEntities = new ArrayList<>();
    for (Vertex existingNode : existingNodes) {
      existingEntities.add(existingNode.getProperty(CommonConstants.CODE_PROPERTY));
    }
    isAdded.put(Boolean.TRUE, ListUtils.subtract(importedCodes, existingEntities));
    isAdded.put(Boolean.FALSE,ListUtils.subtract(existingEntities, importedCodes));
    return isAdded;
  }
  
  public static Map<String, Object> getValidProperty(Map<String, Object> properties, String entityLabel, IExceptionModel failure)
  {
    Map<String, Object> validProperty = new HashMap<>();
    for (Entry<String, Object> property : properties.entrySet()) {
      String key = property.getKey();
      try {
        UtilClass.getVertexByCode(key, entityLabel);
        validProperty.put(key, property.getValue());
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, key, entityLabel);
      }
    }
    return validProperty;
  }
}
