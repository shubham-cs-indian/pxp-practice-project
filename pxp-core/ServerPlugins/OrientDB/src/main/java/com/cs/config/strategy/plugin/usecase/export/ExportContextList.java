package com.cs.config.strategy.plugin.usecase.export;

import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.variantcontext.IDefaultTimeRange;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.model.variantcontext.IGetAllVariantContextsResponseModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

import java.util.*;

public class ExportContextList extends AbstractOrientPlugin {

  private static final String DEFAULT_START_TIME = "defaultStartTime";
  private static final String DEFAULT_END_TIME   = "defaultEndTime";
  private static final String TAB                = "tab";
  private static final String TAG_CODES          = "tagCodes";
  private static final String CLASS_CODE         = "classCode";
  
 private static final List<String> fieldsToFetch = Arrays.asList(IVariantContext.LABEL, IVariantContext.TYPE,
      IVariantContext.CODE, IVariantContext.IS_STANDARD, IVariantContext.IS_TIME_ENABLED,
      IVariantContext.IS_DUPLICATE_VARIANT_ALLOWED, IVariantContext.ENTITIES,
      IVariantContext.DEFAULT_TIME_RANGE, IVariantContext.IS_AUTO_CREATE, IVariantContext.ICON);

  public ExportContextList(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|ExportContextList/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    List<String> itemCodes = (List<String>) requestMap.get("itemCodes");
    
    StringBuilder codeQuery = UtilClass.getTypeQueryWithoutANDOperator(itemCodes,
        IVariantContext.CODE);
    StringBuilder condition = EntityUtil.getConditionQuery(codeQuery);
    String query = "select from " + VertexLabelConstants.VARIANT_CONTEXT + condition;
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
        .execute();
    List<Map<String, Object>> contextsToReturn = new ArrayList<>();
    for (Vertex contextNode : searchResults) {
      Map<String, Object> taskMap = getContexts(contextNode);
      contextsToReturn.add(taskMap);
    }
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IGetAllVariantContextsResponseModel.LIST, contextsToReturn);
    return responseMap;
  }

  private static Map<String, Object> getContexts(Vertex contextNode) throws Exception {
    Map<String, Object> returnMap = UtilClass.getMapFromVertex(fieldsToFetch, contextNode);

    //Context Time Data Preparation
    if ((boolean) returnMap.get(IVariantContext.IS_TIME_ENABLED)) {
      Map<String, Object> defaultTime = (Map<String, Object>) returnMap.get(
          IVariantContext.DEFAULT_TIME_RANGE);
      boolean isCurrentTime = false;
      Long from = null;
      Long to = null;
      if(defaultTime != null) {
        Object currentTime = defaultTime.get(IDefaultTimeRange.IS_CURRENT_TIME);
        if(currentTime != null) {
          isCurrentTime = (boolean) currentTime;
        }
        from = (Long) defaultTime.get(IDefaultTimeRange.FROM);
        to = (Long) defaultTime.get(IDefaultTimeRange.TO);
      }
      setDefaultTime(returnMap, isCurrentTime, from, to);
    }
    returnMap.remove(IVariantContext.DEFAULT_TIME_RANGE);

    //Context Tab data preparation
    Map<String, Object> responseTabMap = TabUtils.getMapFromConnectedTabNode(contextNode,
        Arrays.asList(CommonConstants.CODE_PROPERTY));
    if (responseTabMap != null) {
      String tabId = (String) responseTabMap.get(IIdLabelModel.ID);
      returnMap.put(TAB, tabId);
    }

    //Context klass data preparation
    returnMap.put(CLASS_CODE, getContextClassCode(contextNode));

    //Context tag data preparation
    Map<String, Object> contextTagCode = getContextTagCode(contextNode);
    returnMap.put(TAG_CODES, contextTagCode);

    //Unique Selector
    VariantContextUtils.fillUniqueSelections(contextNode, returnMap);

    return returnMap;
  }

  private static void setDefaultTime(Map<String, Object> returnMap, boolean isCurrentTime, Long from, Long to)
  {
    returnMap.put(IDefaultTimeRange.IS_CURRENT_TIME, isCurrentTime);
    returnMap.put(DEFAULT_START_TIME, from);
    returnMap.put(DEFAULT_END_TIME, to);
  }

  private static String getContextClassCode(Vertex contextNode) {
    Iterator<Vertex> variantKlass = contextNode.getVertices(Direction.IN,
        RelationshipLabelConstants.VARIANT_CONTEXT_OF).iterator();
    if (variantKlass.hasNext()) {
      return UtilClass.getCodeNew(variantKlass.next());
    }

    return null;
  }

  public static Map<String, Object> getContextTagCode(Vertex contextNode) throws Exception
  {
    Map<String, Object> contextTagMap = new HashMap<String, Object>();
    Iterable<Vertex> contextTags = contextNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_CONTEXT_TAG);
    for (Vertex contextTag : contextTags) {
      Vertex tagNode = null;
      Iterator<Vertex> tagNodes = contextTag
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_CONTEXT_TAG_PROPERTY)
          .iterator();
      if (tagNodes.hasNext()) {
        tagNode = tagNodes.next();
      }
      else {
        continue;
      }
      String tagId = UtilClass.getCodeNew(tagNode);
      
      Iterable<Vertex> contextTagValueNodes = contextTag.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_CONTEXT_TAG_VALUE);
      
      List<String> selectedTagValueIds = new ArrayList<>();
      for (Vertex contextTagValueNode : contextTagValueNodes) {
        selectedTagValueIds.add(UtilClass.getCodeNew(contextTagValueNode));
      }
      contextTagMap.put(tagId, selectedTagValueIds);
    }
    return contextTagMap;
  }
}
