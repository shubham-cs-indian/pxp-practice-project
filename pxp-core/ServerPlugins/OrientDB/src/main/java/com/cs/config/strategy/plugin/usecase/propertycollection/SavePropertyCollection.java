package com.cs.config.strategy.plugin.usecase.propertycollection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.propertycollection.util.PropertyCollectionUtils;
import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.exception.propertycollection.PropertyCollectionNotFoundException;
import com.cs.core.config.interactor.model.propertycollection.IGetPropertyCollectionModel;
import com.cs.core.config.interactor.model.propertycollection.ISavePropertyCollectionModel;
import com.cs.core.config.interactor.model.propertycollection.ISavePropertyCollectionResponseModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class SavePropertyCollection extends AbstractSavePropertyCollection {
  
  public static final List<String> fieldsToExclude = Arrays.asList(
      ISavePropertyCollectionModel.ADDED_ELEMENTS, ISavePropertyCollectionModel.MODIFIED_ELEMENTS,
      ISavePropertyCollectionModel.DELETED_ELEMENTS, ISavePropertyCollectionModel.ADDED_TAB,
      ISavePropertyCollectionModel.DELETED_TAB, ISavePropertyCollectionModel.ATTRIBUTE_IDS,
      ISavePropertyCollectionModel.TAG_IDS);
  
  public SavePropertyCollection(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SavePropertyCollection/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    UtilClass.setNodesForVersionIncrement(new HashSet<>());
    Map<String, Object> propertyCollection = (Map<String, Object>) requestMap
        .get(CommonConstants.PROPERTY_COLLECTION);
    String id = (String) propertyCollection.get(ISavePropertyCollectionModel.ID);
    Vertex propertyCollectionNode = null;
    Map<String, List<String>> deletedPropertyMap = new HashMap<>();
    List<Map<String, Object>> defaultValueChangeList = new ArrayList<>();
    try {
      propertyCollectionNode = UtilClass.getVertexById(id,
          VertexLabelConstants.PROPERTY_COLLECTION);
    }
    catch (NotFoundException e) {
      throw new PropertyCollectionNotFoundException();
    }
    
    checkIfIsDefaultForXrayCollection(propertyCollection, propertyCollectionNode);
    
    UtilClass.saveNode(propertyCollection, propertyCollectionNode, fieldsToExclude);
    
    String tabIdToDelete = (String) propertyCollection
        .get(ISavePropertyCollectionModel.DELETED_TAB);
    Map<String, Object> tabMap = (Map<String, Object>) propertyCollection
        .get(ISavePropertyCollectionModel.ADDED_TAB);
    TabUtils.manageAddedAndDeletedTab(propertyCollectionNode, tabMap, tabIdToDelete,
        CommonConstants.PROPERTY_COLLECTION);
    List<String> propertySequenceIds = propertyCollectionNode.getProperty(CommonConstants.PROPERTY_SEQUENCE_IDS);
    manageDeletedElements(propertyCollection, propertyCollectionNode, deletedPropertyMap, propertySequenceIds);
    manageAddedElements(propertyCollection, propertyCollectionNode, defaultValueChangeList, propertySequenceIds);
    manageModifiedElements(propertyCollection, propertyCollectionNode, propertySequenceIds);
    
    Set<Vertex> verticesToIncrementVersion = UtilClass.getNodesForVersionIncrement();
    for (Vertex vertex : verticesToIncrementVersion) {
      UtilClass.incrementVersionIdOfNode(vertex);
    }
    
    UtilClass.getGraph()
        .commit();
    
    Map<String, Object> getResponse = PropertyCollectionUtils
        .getPropertyCollection(propertyCollectionNode);
    Map<String, Object> responseTabMap = TabUtils.getMapFromConnectedTabNode(propertyCollectionNode,
        Arrays.asList(CommonConstants.CODE_PROPERTY, IIdLabelModel.LABEL));
    getResponse.put(IGetPropertyCollectionModel.TAB, responseTabMap);
    
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(ISavePropertyCollectionResponseModel.PROPERTY_COLLECTION_GET_RESPONSE,
        getResponse);
    returnMap.put(ISavePropertyCollectionResponseModel.DELETED_PROPERTIES_FROM_SOURCE,
        deletedPropertyMap);
    returnMap.put(ISavePropertyCollectionResponseModel.DEFAULT_VALUES_DIFF, defaultValueChangeList);
    AuditLogUtils.fillAuditLoginfo(returnMap, propertyCollectionNode,
        Entities.PROPERTY_GROUPS_MENU_ITEM_TITLE, Elements.PROPERTY_COLLECTION);
    return returnMap;
  }
  
  /**
   * Description : If a property collection is being made an IsDefaultForXray as
   * true then make all other PCs IsDefaultForXray as false..
   *
   * @author Ajit
   * @param propertyCollection
   * @param currentPropertyCollectionNode
   */
  private void checkIfIsDefaultForXrayCollection(Map<String, Object> propertyCollection,
      Vertex currentPropertyCollectionNode)
  {
    boolean isDefaultForXRay = (boolean) propertyCollection
        .get(ISavePropertyCollectionModel.IS_DEFAULT_FOR_X_RAY);
    
    if (isDefaultForXRay) {
      Iterable<Vertex> iterator = UtilClass.getGraph()
          .command(new OCommandSQL("SELECT FROM " + VertexLabelConstants.PROPERTY_COLLECTION
              + " where " + IPropertyCollection.IS_DEFAULT_FOR_X_RAY + "=true and code <> '"
              + UtilClass.getCodeNew(currentPropertyCollectionNode) + "'"))
          .execute();
      
      for (Vertex propertyCollectionNode : iterator) {
        propertyCollectionNode.setProperty(IPropertyCollection.IS_DEFAULT_FOR_X_RAY, false);
      }
    }
  }  
}
