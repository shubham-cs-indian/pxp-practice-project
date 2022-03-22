package com.cs.config.strategy.plugin.usecase.variantcontext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;

import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.config.strategy.plugin.usecase.export.ExportContextList;
import com.cs.config.strategy.plugin.usecase.importPXON.util.ImportUtils;
import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.propertycollection.IAddedTabModel;
import com.cs.core.config.interactor.model.variantcontext.ICreateVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IVariantContextModifiedTagsModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.NotFoundException;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

/**
 * @author tauseef
 */
public class UpsertContexts extends AbstractOrientPlugin {

  private static final String ADDED_TAGS = "addedTags";
  private static final String CLASS_CODE = "classCode";
  private static final String DEFAULT_END_TIME = "defaultEndTime";
  private static final String DEFAULT_START_TIME = "defaultStartTime";
  private static final String IS_CURRENT_TIME = "isCurrentTime";
  private static final String DEFAULT_TIME_RANGE = "defaultTimeRange";
  private static final String TAG_CODES = "tagCodes";
  private static final String FROM = "from";
  private static final String TO = "to";
  private static final String TAB = "tab";

  protected static final List<String> fieldsToExclude    = Arrays.asList(CLASS_CODE, TAB, ADDED_TAGS, DEFAULT_END_TIME, DEFAULT_END_TIME,
      TAG_CODES, ConfigTag.uniqueSelectors.toString());
  
  public UpsertContexts(OServerCommandConfiguration iConfiguration) {
    super(iConfiguration);
  }

  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception {
    List<Map<String, Object>> variantContextsSaveList = (List<Map<String, Object>>) requestMap
        .get(CommonConstants.LIST_PROPERTY);
    IExceptionModel failure = new ExceptionModel();
    List<Map<String, Object>> listOfSuccessSaveVariantContexts = new ArrayList<>();
    List<Map<String, Object>> failedContextList = new ArrayList<>();
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.VARIANT_CONTEXT, CommonConstants.CODE_PROPERTY);
    Vertex contextNode;

    for (Map<String, Object> contextMap : variantContextsSaveList) {
      try {
        prepareADMForContext(contextMap);
        UtilClass.validateIconExistsForImport(contextMap);
        try {
          String contextCode = (String) contextMap.get(CommonConstants.CODE_PROPERTY);
          contextNode = UtilClass.getVertexByCode(contextCode, VertexLabelConstants.VARIANT_CONTEXT);
          UtilClass.saveNode(contextMap, contextNode, fieldsToExclude);
          updateTab(failure, contextMap, contextNode, contextCode);
          updateTags(contextNode, contextMap, contextCode, failure);
        }catch(NotFoundException e) {
          //Create Context
          UtilClass.validateOnType(Constants.CONTEXTS_TYPES_LIST,
              (String) contextMap.get(ICreateVariantContextModel.TYPE), true);

          contextNode = UtilClass.createNode(contextMap, vertexType, fieldsToExclude);

          //Klass Linking
          String klassCode = (String) contextMap.remove(CLASS_CODE);
          if (StringUtils.isNotEmpty(klassCode)) {
            try {
              Vertex klassNode = UtilClass.getVertexById(klassCode, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
              klassNode.addEdge(RelationshipLabelConstants.VARIANT_CONTEXT_OF, contextNode);
            }
            catch (Exception ex) {
              ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, klassCode, klassCode);
            }
          }

          //Tags Linking
          prepareValidTags(contextMap, failure);
          Map<String, Object> addedTags = (Map<String, Object>) contextMap.get(ADDED_TAGS);
          if(addedTags != null) {
            VariantContextUtils.handleAddedTags(contextNode, addedTags);
          }

          // tab
          Map<String, Object> tabMap = (Map<String, Object>) contextMap.get(
              ICreateVariantContextModel.TAB);
          TabUtils.linkAddedOrDefaultTab(contextNode, tabMap, CommonConstants.CONTEXT);
        }
        ImportUtils.addSuccessImportedInfo(listOfSuccessSaveVariantContexts, contextMap);
      }
      catch (PluginException ex) {
        ImportUtils.logExceptionAndFailureIDs(failure, failedContextList, contextMap, ex);
      }
      catch (Exception ex) {
        ImportUtils.logExceptionAndFailureIDs(failure, failedContextList, contextMap, ex);
      }

    }
    UtilClass.getGraph().commit();
    Map<String, Object> responseMap = ImportUtils.prepareImportResponseMap(failure, listOfSuccessSaveVariantContexts, failedContextList);
    return responseMap;
  }

  private void updateTags(Vertex contextNode, Map<String, Object> contextMap, String contextCode, IExceptionModel failure) throws Exception
  {
    Map<String, Object> oldContextTags = ExportContextList.getContextTagCode(contextNode);
    Map<String, Object> newAddedTags = (Map<String, Object>) contextMap.get(TAG_CODES);
    List<Map<String, Object>> modifiedTags = new ArrayList<>();
    Map<String, Object> AddedTags = new HashMap<>();
    
    prepareModifiedAndAddedTags(oldContextTags, newAddedTags, modifiedTags, AddedTags, contextCode, failure);
    VariantContextUtils.handleAddedTags(contextNode, AddedTags);
    VariantContextUtils.manageModifiedContextTag(contextNode, modifiedTags);
    //delete tagCodes
    Set<String> newAddedCodes = newAddedTags.keySet();
    Set<String> oldTagCodes = oldContextTags.keySet();
    List<String> deletedTagIds = new ArrayList<>(oldTagCodes);
    deletedTagIds.removeAll(newAddedCodes);
    VariantContextUtils.manageDeleteTagIds(contextNode, deletedTagIds);
  }

  /**
   * 
   * @param oldContextTags existing tag value map before update.
   * @param newTags tag with tag value for update.
   * @param modifiedTags prepare modified tag list with added and deleted tag value.
   * @param addedTags prepare added tag map.
   * @param contextCode
   * @param failure log the invalid tag code.
   */
  private void prepareModifiedAndAddedTags(Map<String, Object> oldContextTags, Map<String, Object> newTags,
      List<Map<String, Object>> modifiedTags, Map<String, Object> addedTags, String contextCode, IExceptionModel failure)
  {
    Set<String> tagCodes = newTags.keySet();
    Set<String> validTagCodes = new TreeSet<>();
    List<Vertex> validTagVertices = IterableUtils.toList(UtilClass.getVerticesByIds(tagCodes, VertexLabelConstants.ENTITY_TAG));
    for (Vertex tagNode : validTagVertices) {
      String tagCode = tagNode.getProperty(CommonConstants.CODE_PROPERTY);
      validTagCodes.add(tagCode);
      if (oldContextTags.containsKey(tagCode)) {
        preapreModifiedTags(oldContextTags, newTags, modifiedTags, tagCode, tagNode);
      }
      else {
        prepareAddedTags(newTags, addedTags, tagNode);
      }
    }
    
    //Invalid tag code and Exceptions
    Set<String> inValidTags = new TreeSet<>(tagCodes);
    inValidTags.removeAll(validTagCodes);    
    if(!inValidTags.isEmpty()) {
      ExceptionUtil.addFailureDetailsToFailureObject(failure, new NotFoundException(), contextCode, inValidTags.toString());
    }
  }

  /**
   * Prepare modified tag with added and deleted tag value
   * Add only valid tag values
   * @param oldContextTags
   * @param newTags
   * @param modifiedTags
   * @param tagCode 
   * @param tagNode
   */
  private void preapreModifiedTags(Map<String, Object> oldContextTags, Map<String, Object> newTags, List<Map<String, Object>> modifiedTags,
      String tagCode, Vertex tagNode)
  {
    List<String> oldTagValue = (List<String>) oldContextTags.get(tagCode);
    List<String> tagValue = (List<String>) newTags.get(tagCode);
    
    //Remove all existing tag values
    List<String> addedTagValues = new ArrayList<>(tagValue);
    addedTagValues.removeAll(oldTagValue);
    //Only valid tag values get attached
    List<String> actualTagValues = tagNode.getProperty(ITag.TAG_VALUES_SEQUENCE);
    addedTagValues.retainAll(actualTagValues);
    
    List<String> deletedTagValues = new ArrayList<>(oldTagValue);
    deletedTagValues.removeAll(tagValue);
    
    //prepare modified tag map only when tag value is added or deleted.
    if(!(addedTagValues.isEmpty() && deletedTagValues.isEmpty())) {
      Map<String, Object> modifiedTagMap = new HashMap<>();
      modifiedTagMap.put(IVariantContextModifiedTagsModel.TAG_ID, tagCode);
      modifiedTagMap.put(IVariantContextModifiedTagsModel.ADDED_TAG_VALUE_IDS, addedTagValues);
      modifiedTagMap.put(IVariantContextModifiedTagsModel.DELETED_TAG_VALUE_IDS, deletedTagValues);
      modifiedTags.add(modifiedTagMap);
    }
  }

  /**
   * Prepare list of tagValue for added tags
   * Add only valid tagValue 
   * Handled default case if tagValue is empty then attached all tag values same as UI
   * @param newTags
   * @param addedTags
   * @param tag
   */
  private void prepareAddedTags(Map<String, Object> newTags, Map<String, Object> addedTags, Vertex tag)
  {
    String tagCode = tag.getProperty(ITag.CODE);
    List<String> actualTagValues = tag.getProperty(ITag.TAG_VALUES_SEQUENCE);
    List<String> tagValue = (List<String>) newTags.get(tagCode);
    if (tagValue.isEmpty()) {
      // Default case if nothing is selected all tag value get selected
      tagValue.addAll(actualTagValues);
    }
    else {
      // only valid tag value codes is added
      tagValue.retainAll(actualTagValues);
    }
    addedTags.put(tagCode, tagValue);
  }

  private void prepareADMForContext(Map<String, Object> contextMap) {

    //Default time range Handling
    Map<String, Object> defaultTimeRange = new HashMap<>();
    Long startTime = (Long) contextMap.remove(DEFAULT_START_TIME);
    Long endTime = (Long) contextMap.remove(DEFAULT_END_TIME);
    Boolean isCurrentTime = (Boolean) contextMap.remove(IS_CURRENT_TIME);
    isCurrentTime = isCurrentTime == null ? false : isCurrentTime;
    defaultTimeRange.put(IS_CURRENT_TIME, isCurrentTime);
    if(isCurrentTime) {
      startTime = null;
      endTime = null;
    }
    defaultTimeRange.put(FROM, startTime);
    defaultTimeRange.put(TO, endTime);
    
    contextMap.put(DEFAULT_TIME_RANGE, defaultTimeRange);

    //Tab Handling
    String newTabCode = (String) contextMap.remove(TAB);
    Map<String, Object> tabMap = null;
    if(StringUtils.isNotEmpty(newTabCode)) {
      tabMap = new HashMap<>();
      tabMap.put(CommonConstants.ID_PROPERTY, newTabCode);
      tabMap.put(CommonConstants.CODE_PROPERTY, newTabCode);
      tabMap.put(IAddedTabModel.IS_NEWLY_CREATED, false);
    }
    contextMap.put(TAB, tabMap);
  }

  /**
   * Only valid tag code with valid tag value are attach to context
   * @param contextMap
   * @param failure
   */
  private void prepareValidTags(Map<String, Object> contextMap, IExceptionModel failure)
  {
    Map<String, Object> addedTags = (Map<String, Object>) contextMap.remove(TAG_CODES);
    if (addedTags != null) {
      Map<String, Object> validTags = new HashMap<>();
      Set<String> tagCodes = addedTags.keySet();
      // only valid tag will attach to Context
      List<Vertex> validTagVertices = IterableUtils.toList(UtilClass.getVerticesByIds(tagCodes, VertexLabelConstants.ENTITY_TAG));
      for (Vertex tagVertex : validTagVertices) {
        prepareAddedTags(addedTags, validTags, tagVertex);
      }
      contextMap.put(ADDED_TAGS, validTags);
      
      Set<String> validTagCodes = validTags.keySet();
      tagCodes.removeAll(validTagCodes);
      if (!tagCodes.isEmpty()) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, new NotFoundException(),
            (String) contextMap.get(CommonConstants.CODE_PROPERTY), tagCodes.toString());
      }
    }
  }

  @Override
  public String[] getNames() {
    return new String[] { "POST|UpsertContexts/*" };
  }
  
  /**
   * Update tab for context other than ATTRIBUTE_VARIANT_CONTEXT, 
   * PRODUCT_VARIANT, RELATIONSHIP_VARIANT
   * Checked new tab and old tab is same or not and new tab code is valid then only update the tab.
   * @param failure
   * @param contextMap
   * @param contextNode
   * @param code
   * @throws Exception
   */
  private void updateTab(IExceptionModel failure, Map<String, Object> contextMap, Vertex contextNode, String code) throws Exception
  {
    Map<String, Object> newTab = (Map<String, Object>) contextMap.get(ConfigTag.tab.toString());
    Map<String, Object> referencedTab = TabUtils.getMapFromConnectedTabNode(contextNode,
        Collections.singletonList(CommonConstants.CODE_PROPERTY));
    if (newTab != null && referencedTab != null) {
      String existingTabCode = (String) referencedTab.get(CommonConstants.CODE_PROPERTY);
      String newTabCode = (String) newTab.get(CommonConstants.CODE_PROPERTY);
      if (StringUtils.isNoneEmpty(newTabCode) && !existingTabCode.equals(newTabCode)) {
        try {
          UtilClass.getVertexByCode(newTabCode, VertexLabelConstants.TAB);
          TabUtils.manageAddedAndDeletedTab(contextNode, newTab, existingTabCode, CommonConstants.CONTEXT);
        }
        catch (NotFoundException e) {
          ExceptionUtil.addFailureDetailsToFailureObject(failure, e, code, newTabCode);
        }
      }
    }
  }
}


