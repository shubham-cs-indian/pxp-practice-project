package com.cs.config.strategy.plugin.usecase.propertycollection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;

import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.config.strategy.plugin.usecase.importPXON.util.ImportUtils;
import com.cs.config.strategy.plugin.usecase.propertycollection.util.CreatePropertyCollectionUtil;
import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollectionElement;
import com.cs.core.config.interactor.model.propertycollection.IAddedTabModel;
import com.cs.core.config.interactor.model.propertycollection.ICreatePropertyCollectionModel;
import com.cs.core.config.interactor.model.propertycollection.IPropertyCollectionModel;
import com.cs.core.config.interactor.model.propertycollection.ISavePropertyCollectionModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

/**
 * @author tauseef
 */
public class UpsertPropertyCollections extends AbstractSavePropertyCollection {
  
  public static final List<String> fieldsToExclude = Arrays.asList(ConfigTag.isStandard.name(), ConfigTag.tab.name(),
      ConfigTag.elements.name());
  
  public UpsertPropertyCollections(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> propertyCollectionList = (List<Map<String, Object>>) requestMap.get(CommonConstants.LIST_PROPERTY);
    List<Map<String, Object>> createdPropertyCollectionList = new ArrayList<>();
    List<Map<String, Object>> failedPCList = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    String entityLabel = VertexLabelConstants.PROPERTY_COLLECTION;
    
    for (Map<String, Object> pcMap : propertyCollectionList) {
      
      try {
        Vertex propertyCollectionNode;
        try {
          String pcCode = (String) pcMap.get(CommonConstants.CODE_PROPERTY);
          propertyCollectionNode = UtilClass.getVertexByCode(pcCode, entityLabel);
          UtilClass.saveNode(pcMap, propertyCollectionNode, fieldsToExclude);
          updateTab(failure, pcMap, propertyCollectionNode, pcCode);
          List<String> propertySequenceList = prepareADMForElements(failure, pcMap, propertyCollectionNode, pcCode);
          manageDeletedElements(pcMap, propertyCollectionNode, new HashMap<>(), propertySequenceList);
          manageAddedElements(pcMap, propertyCollectionNode, new ArrayList<>(), propertySequenceList);
          manageModifiedElements(pcMap, propertyCollectionNode, propertySequenceList);
        }
        catch (NotFoundException e) {
          prepareTabMap(pcMap);
          propertyCollectionNode = createPropertyCollection(pcMap, failure);
        }
        
        ImportUtils.addSuccessImportedInfo(createdPropertyCollectionList, pcMap);
      }
      catch (Exception e) {
        ImportUtils.logExceptionAndFailureIDs(failure, failedPCList, pcMap, e);
      }
    }
    
    Map<String, Object> result = ImportUtils.prepareImportResponseMap(failure, createdPropertyCollectionList, failedPCList);
    return result;
  }

  /**
   * Checked new tab code is valid or not 
   * and checked existing tab and new tab are not same then only remove the old tab and new tab
   * @param failure if new tab code is not valid then log the exception
   * @param pcMap
   * @param propertyCollectionNode
   * @param pcCode
   * @throws Exception
   */
  private void updateTab(IExceptionModel failure, Map<String, Object> pcMap, Vertex propertyCollectionNode, String pcCode) throws Exception
  {
    Map<String, Object> referencedTab = TabUtils.getMapFromConnectedTabNode(propertyCollectionNode,
        Collections.singletonList(CommonConstants.CODE_PROPERTY));
    String existingTabCode = (String) referencedTab.get(CommonConstants.CODE_PROPERTY);
    String newTabCode = (String) pcMap.get(ConfigTag.tab.toString());
    if (StringUtils.isNotEmpty(newTabCode) && !existingTabCode.equals(newTabCode)) {
      try {
        UtilClass.getVertexByCode(newTabCode, VertexLabelConstants.TAB);
        Map<String, Object> tabMap = new HashMap<>();
        tabMap.put(IAddedTabModel.IS_NEWLY_CREATED, false);
        tabMap.put(IAddedTabModel.ID, newTabCode);
        TabUtils.manageAddedAndDeletedTab(propertyCollectionNode, tabMap, existingTabCode, CommonConstants.PROPERTY_COLLECTION);
      }
      catch (NotFoundException e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, pcCode, newTabCode);
      }
    }
  }

  /**
   * Prepare added, modified and deleted property element with proper index
   * Also log the info for invalid property
   * Handle duplicate index, invalid index  
   * @param failure
   * @param pcMap
   * @param propertyCollectionNode
   * @param pcCode
   * @return
   */
  private List<String> prepareADMForElements(IExceptionModel failure, Map<String, Object> pcMap, Vertex propertyCollectionNode,
      String pcCode)
  {
    List<String> oldPropertySequence = propertyCollectionNode.getProperty(IPropertyCollection.PROPERTY_SEQUENCE_IDS);
    List<Map<String, Object>> newElements = (List<Map<String, Object>>) pcMap.get(IPropertyCollectionModel.ELEMENTS);
    insertionSortElements(newElements);
    List<Map<String, Object>> addedElements = new ArrayList<>();
    List<Map<String, Object>> modifiedElements = new ArrayList<>();
    int newElementListSize = newElements.size();
    List<Map<String, Object>> newValidElements = new ArrayList<>(newElementListSize);
    List<Map<String, Object>> inValidIndexElements = new ArrayList<>();
    for (Map<String, Object> newElement : newElements) {
      String newElementCode = (String) newElement.get(IPropertyCollectionElement.CODE);
      String type = (String) newElement.get(IPropertyCollectionElement.TYPE);
      String vertextType = EntityUtil.getVertexLabelByEntityType(type);
      try {
        UtilClass.getVertexByCode(newElementCode, vertextType);
        newElement.put(IPropertyCollectionElement.ID, newElementCode);
        if (oldPropertySequence.contains(newElementCode)) {
          modifiedElements.add(newElement);
        }
        else {
          addedElements.add(newElement);
        }
        prepareSequenceList(newElementListSize, newValidElements, inValidIndexElements, newElement);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, pcCode, newElementCode);
      }
    }
    
    handleUnorderPropertySequence(newValidElements, inValidIndexElements);
    List<String> newElementCodes = newValidElements.stream().map(e -> (String) e.get(IPropertyCollectionElement.CODE))
        .collect(Collectors.toList());
    List<Map<String, Object>> deletedPropertyList = prepareDeletedPropertyMap(propertyCollectionNode, oldPropertySequence,
        newElementCodes);
    pcMap.put(ISavePropertyCollectionModel.MODIFIED_ELEMENTS, modifiedElements);
    pcMap.put(ISavePropertyCollectionModel.ADDED_ELEMENTS, addedElements);
    pcMap.put(ISavePropertyCollectionModel.DELETED_ELEMENTS, deletedPropertyList);
    return oldPropertySequence;
  }
  
  /**
   * Sort elements by index 
   * @param newElements
   */
  private void insertionSortElements(List<Map<String, Object>> newElements) {
    for (int j = 1; j < newElements.size(); j++) {
      Map<String, Object> currentElement = newElements.get(j);
      Integer currentIndex = (Integer) currentElement.get(IPropertyCollectionElement.INDEX);
        int i = j-1;
        while ((i > -1) && (Integer) newElements.get(i).get(IPropertyCollectionElement.INDEX) > currentIndex) {
          newElements.set(i+1, newElements.get(i));
            i--;
        }
        newElements.set(i+1, currentElement);
    }
  }

  /**
   * Prepare list of new element according to index
   * Handle invalid index
   * @param newElementListSize
   * @param newValidElements
   * @param inValidIndexElements
   * @param newElement
   */
  private void prepareSequenceList(int newElementListSize, List<Map<String, Object>> newValidElements,
      List<Map<String, Object>> inValidIndexElements, Map<String, Object> newElement)
  {
    Integer index = (Integer) newElement.get(IPropertyCollectionElement.INDEX);
    boolean isDuplicate = IterableUtils.matchesAny(newValidElements, x -> x.get(IPropertyCollectionElement.INDEX).equals(index));
    boolean isIndexInvalid = index < 0 || newElementListSize < index;
    if (isIndexInvalid || isDuplicate) {
      inValidIndexElements.add(newElement);
    }
    else {
      newElement.put(IPropertyCollectionElement.INDEX, newValidElements.size() + 1);
      newValidElements.add(newElement);
    }
  }

  /**
   * If list new Element contain invalid code or index then handle index here
   * @param validElement
   * @param inValidIndexElements
   */
  private void handleUnorderPropertySequence(List<Map<String, Object>> validElement,
      List<Map<String, Object>> inValidIndexElements)
  {
    for(Map<String, Object> element : inValidIndexElements) {
      element.put(IPropertyCollectionElement.INDEX, validElement.size()+1);
      validElement.add(element);
    }
  }

  /**
   * Prepare deleted property List by comparing with current property
   * @param propertyCollectionNode
   * @param propertySequenceList
   * @param newElementCodes
   * @return
   */
  private List<Map<String, Object>> prepareDeletedPropertyMap(Vertex propertyCollectionNode, List<String> propertySequenceList,
      List<String> newElementCodes)
  {
    List<String> deletedPropertyCodes = new ArrayList<>(propertySequenceList);
    deletedPropertyCodes.removeAll(newElementCodes);
    List<Map<String, Object>> deletedPropertyList = new ArrayList<>();
    List<String> attributeIDs = propertyCollectionNode.getProperty(IPropertyCollection.ATTRIBUTE_IDS);
    List<String> tagIDs = propertyCollectionNode.getProperty(IPropertyCollection.TAG_IDS);
    for (String propertyCode : deletedPropertyCodes) {
      Map<String, Object> deletedElement = new HashMap<>();
      if (attributeIDs.contains(propertyCode)) {
        prepareDeletedElementMap(deletedPropertyList, propertyCode, deletedElement, CommonConstants.ATTRIBUTE);
      }
      else if (tagIDs.contains(propertyCode)) {
        prepareDeletedElementMap(deletedPropertyList, propertyCode, deletedElement, CommonConstants.TAG);
      }
    }
    return deletedPropertyList;
  }

  /**
   * prepare delete element map
   * @param deletedPropertyList
   * @param propertyCode
   * @param deletedElement
   * @param propertyType
   */
  private void prepareDeletedElementMap(List<Map<String, Object>> deletedPropertyList, String propertyCode,
      Map<String, Object> deletedElement, String propertyType)
  {
    deletedElement.put(IPropertyCollectionElement.ID, propertyCode);
    deletedElement.put(IPropertyCollectionElement.CODE, propertyCode);
    deletedElement.put(IPropertyCollectionElement.TYPE, propertyType);
    deletedPropertyList.add(deletedElement);
  }

  private void prepareTabMap(Map<String, Object> pcMap)
  {
    String tab = (String) pcMap.get(ConfigTag.tab.name());
    if (StringUtils.isNotEmpty(tab)) {
      Map<String, Object> tabMap = new HashMap<>();
      tabMap.put(IAddedTabModel.IS_NEWLY_CREATED, false);
      tabMap.put(IAddedTabModel.ID, tab);
      pcMap.put(CommonConstants.TAB, tabMap);
    }
    else {
      pcMap.put(CommonConstants.TAB, null);
    }
  }
  
  private Vertex createPropertyCollection(Map<String, Object> propertyCollectionMap, IExceptionModel failure)
      throws Exception
  {
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.PROPERTY_COLLECTION, CommonConstants.CODE_PROPERTY);
    UtilClass.validateIconExistsForImport(propertyCollectionMap);
    Vertex propertyCollectionNode = UtilClass.createNode(propertyCollectionMap, vertexType,
        CreatePropertyCollectionUtil.fieldsToExclude);
    
    List<String> attributeIds = new ArrayList<>();
    List<String> tagIds = new ArrayList<>();
    List<Map<String, Object>> elements = (List<Map<String, Object>>) propertyCollectionMap
        .get(IPropertyCollectionModel.ELEMENTS);
    insertionSortElements(elements);
    int size = elements.size();
    List<String> propertySeqIDs = new ArrayList<>(Collections.nCopies(size,  null));
    List<String> elementsIDs = new ArrayList<>();
    for (Map<String, Object> element : elements) {
      try {
        String newElementCode = (String) element.get(IPropertyCollectionElement.CODE);
        element.put(IPropertyCollectionElement.ID, newElementCode);
        String elementID = CreatePropertyCollectionUtil.addElement(propertyCollectionNode, attributeIds, tagIds, element);
        Integer index = (Integer) element.get(IPropertyCollectionElement.INDEX);
        prepareSequnenceList(size, propertySeqIDs, elementsIDs, elementID, index);
      }catch(NotFoundException e){
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, (String) propertyCollectionMap.get(CommonConstants.CODE_PROPERTY),
            (String) propertyCollectionMap.get(CommonConstants.LABEL_PROPERTY));
      }
    }
    
    propertySeqIDs.addAll(elementsIDs);
    propertySeqIDs.removeAll(Collections.singleton(null));
    propertyCollectionNode.setProperty(IPropertyCollection.PROPERTY_SEQUENCE_IDS, propertySeqIDs);
    propertyCollectionNode.setProperty(IPropertyCollection.ATTRIBUTE_IDS, attributeIds);
    propertyCollectionNode.setProperty(IPropertyCollection.TAG_IDS, tagIds);
    
    // tab
    Map<String, Object> tabMap = (Map<String, Object>) propertyCollectionMap
        .get(ICreatePropertyCollectionModel.TAB);
    try {
      TabUtils.linkAddedOrDefaultTab(propertyCollectionNode, tabMap, CommonConstants.PROPERTY_COLLECTION);
    }
    catch (NotFoundException e) {
      //if tab Code is not valid default tab is attach
      tabMap.put(IAddedTabModel.ID,
          TabUtils.getStandardTabId(CommonConstants.PROPERTY_COLLECTION, propertyCollectionNode.getProperty(IKlass.TYPE)));
      TabUtils.manageAddedTab(propertyCollectionNode, tabMap);
    }
    
    UtilClass.getGraph()
        .commit();
    return propertyCollectionNode;
  }
  
  /**
   * handle sequence for create usecase
   * @param newElementListSize
   * @param newPropertySequenceList
   * @param inProperSequenceList
   * @param code
   * @param index
   */
  private void prepareSequnenceList(int newElementListSize, List<String> newPropertySequenceList, List<String> inProperSequenceList,
      String code, Integer index)
  {
    if (index > 0 && newElementListSize >= index) {
      int i = index - 1;
      String value = newPropertySequenceList.get(i);
      if (value == null) {
        newPropertySequenceList.set(i, code);
      }
      else {
        inProperSequenceList.add(code);
      }
    }
    else {
      inProperSequenceList.add(code);
    }
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|UpsertPropertyCollections/*" };
  }
}
