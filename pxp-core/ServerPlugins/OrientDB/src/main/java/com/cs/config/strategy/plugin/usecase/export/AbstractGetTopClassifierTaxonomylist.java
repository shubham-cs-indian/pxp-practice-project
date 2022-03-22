package com.cs.config.strategy.plugin.usecase.export;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public abstract class AbstractGetTopClassifierTaxonomylist extends AbstractOrientPlugin {

  private static final String BASE_TYPE = "baseType";

  private static final List<String> fieldToFetch = Arrays.asList(IConfigEntityInformationModel.TYPE,
      IConfigEntityInformationModel.LABEL, IConfigEntityInformationModel.CODE, BASE_TYPE,
      ITaxonomy.CLASSIFIER_IID);
  private static final String       PARENT_CODE  = "parentCode";
  
  public AbstractGetTopClassifierTaxonomylist(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  public abstract String getKlassVertexType();
  
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    HashMap<String, Object> responseMap = new HashMap<>();
    List<HashMap<String, Object>> klasslist = new ArrayList<HashMap<String, Object>>();
    
    List<String> itemCodes = (List<String>) requestMap.get("itemCodes");
    boolean includeChildren = (boolean) requestMap.get("includeChildren");
    
    Iterable<Vertex> resultIterable = null;
    StringBuilder codeQuery = UtilClass.getTypeQueryWithoutANDOperator(itemCodes,
        IPropertyCollection.CODE);
    
    StringBuilder condition = EntityUtil.getConditionQuery(codeQuery);
    String query = "select from " + getKlassVertexType() + condition;
    
    resultIterable = graph.command(new OCommandSQL(query))
        .execute();
    
    for (Vertex klassNode : resultIterable) {
      HashMap<String, Object> klassnMap = new HashMap<String, Object>();
      klassnMap = (HashMap<String, Object>) getKlassEntityMap(klassNode);
      // TODO :if user sends the parent and children code then it will fetch it
      // two times.
      if (!itemCodes.isEmpty() && includeChildren)
        getAllChildren(klassNode, klasslist);
      if (!klassnMap.isEmpty()) {
        getType(klassnMap);
        klasslist.add(klassnMap);
      }
    }
    responseMap.put("list", klasslist);
    
    return responseMap;
  }

  private void getType(HashMap<String, Object> klassEntityMap)
  {
    String baseType = (String) klassEntityMap.remove("baseType");
    if (StringUtils.isNotEmpty(baseType)) {
      klassEntityMap.put("type", baseType);
    }
  }

  private void getAllChildren(Vertex klassNode, List<HashMap<String, Object>> klasslist)
      throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    
    String parentId = klassNode.getProperty(CommonConstants.CODE_PROPERTY);
    String query = "SELECT EXPAND( IN('CHILD_OF')) FROM " + getKlassVertexType() + " WHERE code=\""
        + parentId + "\"";
    Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query))
        .execute();
    
    for (Vertex childklassNode : resultIterable) {
      HashMap<String, Object> childEntityMap = null;
      if (childklassNode != null) {
        childEntityMap = new HashMap<>();
        childEntityMap.putAll(UtilClass.getMapFromVertex(fieldToFetch, childklassNode));
        childEntityMap.put(PARENT_CODE, parentId);
        getType(childEntityMap);
        klasslist.add(childEntityMap);
        getAllChildren(childklassNode, klasslist);
      }
    }
  }
  
  private HashMap<String, Object> getKlassEntityMap(Vertex klassNode) throws Exception
  {
    HashMap<String, Object> klassEntityMap = null;
    if (klassNode != null) {
      klassEntityMap = new HashMap<>();
      klassEntityMap.putAll(UtilClass.getMapFromVertex(fieldToFetch, klassNode));
      KlassUtils.getParentInfoToKlassEntityMap(klassNode, klassEntityMap);
    }
    return klassEntityMap;
  }
  
}
