package com.cs.config.strategy.plugin.usecase.entity;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.duplicatecode.IBulkCheckForDuplicateCodesModel;
import com.cs.core.config.strategy.usecase.duplicatecode.IBulkCheckForDuplicateCodesReturnModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/** @author Subham.Shaw */
@SuppressWarnings("unchecked")
public class BulkCheckForDuplicateCodes extends AbstractOrientPlugin {
  
  
  public BulkCheckForDuplicateCodes(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  /**
   * This function will check the codes for individual entity and send response
   * as "code":boolean
   */
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    final Map<String, Object> entityTypeVsCodeInfoMap = new HashMap<>();
    final Map<String, Object> entityTypeVsNameInfoMap = new HashMap<>();

    for (Map<String, Object> requestModel : (List<Map<String, Object>>) requestMap
        .get(IListModel.LIST)) {
      List<String> codeValues = (List<String>) requestModel
          .get(IBulkCheckForDuplicateCodesModel.CODES);
      
      List<String> nameValues = (List<String>) requestModel
          .get(IBulkCheckForDuplicateCodesModel.NAMES);
      
      String entityType = (String) requestModel.get(IBulkCheckForDuplicateCodesModel.ENTITY_TYPE);
      
      Map<String, Boolean> codeExistenceMap = new HashMap<>();
      Iterable<Vertex> duplicateVertices = UtilClass.getGraph()
          .command(new OCommandSQL(createQueryForCodeCheck(codeValues, entityType)))
          .execute();
      
      // If duplicate, set as false
      for (Vertex duplicateVertex : duplicateVertices) {
        codeExistenceMap.put(duplicateVertex.getProperty(CommonConstants.CODE_PROPERTY), false);
      }
      
      // If not duplicate, set as true
      for (String codeValue : codeValues) {
        if (!codeExistenceMap.containsKey(codeValue)) {
          codeExistenceMap.put(codeValue, true);
        }
        entityTypeVsCodeInfoMap.put(entityType, codeExistenceMap);
      }
      
      //for name check
      if (nameValues != null && !nameValues.isEmpty()) {
        Map<String, Boolean> nameExistenceMap = new HashMap<>();
        Iterable<Vertex> duplicateNameVertices = UtilClass.getGraph()
            .command(new OCommandSQL(createQueryForNameCheck(nameValues, entityType))).execute();
        
        // If duplicate, set as false
        for (Vertex duplicateVertex : duplicateNameVertices) {
          nameExistenceMap.put(duplicateVertex.getProperty(EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY)), false);
        }
        
        // If not duplicate, set as true
        for (String nameValue : nameValues) {
          if (!nameExistenceMap.containsKey(nameValue)) {
            nameExistenceMap.put(nameValue, true);
          }
          entityTypeVsNameInfoMap.put(entityType, nameExistenceMap);
        }
      }
    }
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IBulkCheckForDuplicateCodesReturnModel.CODE_CHECK, entityTypeVsCodeInfoMap);
    returnMap.put(IBulkCheckForDuplicateCodesReturnModel.NAME_CHECK, entityTypeVsNameInfoMap);

    return returnMap;
  }
  
  /**
   * Query Method
   *
   * @param codeValues
   *          - All the codes of the enity
   * @param entityName
   *          - Entity name
   * @return query
   */
  private String createQueryForCodeCheck(List<String> codeValues, String entityName)
  {
    return "select from " + entityName + " where " + CommonConstants.CODE_PROPERTY + " IN "
        + EntityUtil.quoteIt(codeValues);
  }
  
  /**
   * Query to check duplicate name
   * @param nameValues
   * @param entityName
   * @return
   */
  private String createQueryForNameCheck(List<String> nameValues, String entityName)
  {
    return "select from " + entityName + " where " + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " IN "
        + EntityUtil.quoteIt(nameValues);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|BulkCheckForDuplicateCodes/*" };
  }
}
