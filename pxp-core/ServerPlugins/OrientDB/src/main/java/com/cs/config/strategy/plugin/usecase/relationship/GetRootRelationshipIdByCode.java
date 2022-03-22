package com.cs.config.strategy.plugin.usecase.relationship;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipCodeIdModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetRootRelationshipIdByCode extends AbstractOrientPlugin {
  
  public GetRootRelationshipIdByCode(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> relationshipCodeList = (List<String>) requestMap.get(IIdsListParameterModel.IDS);
    Map<String, Object> codeIdMap = new HashMap<String, Object>();
    Map<String, Object> responseMap = new HashMap<String, Object>();
    
    Iterable<Vertex> iterable = getVerticesById(relationshipCodeList);
    
    codeIdMap = getIdsOfCodes(iterable);
    responseMap.put(IRelationshipCodeIdModel.CODE_ID_MAP, codeIdMap);
    return responseMap;
  }
  
  private Map<String, Object> getIdsOfCodes(Iterable<Vertex> iterable)
  {
    Map<String, Object> codeIdMap = new HashMap<String, Object>();
    
    for (Vertex vertex : iterable) {
      codeIdMap.put(vertex.getProperty(CommonConstants.CODE_PROPERTY)
          .toString(),
          vertex.getProperty(CommonConstants.CODE_PROPERTY)
              .toString());
    }
    return codeIdMap;
  }
  
  private Iterable<Vertex> getVerticesById(List<String> relationshipCodeList)
  {
    String codes = EntityUtil.quoteIt(relationshipCodeList);
    Iterable<Vertex> iterable = UtilClass.getGraph()
        .command(new OCommandSQL("select  from v where code contains " + codes))
        .execute();
    
    return iterable;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetRootRelationshipIdByCode/*" };
  }
}
