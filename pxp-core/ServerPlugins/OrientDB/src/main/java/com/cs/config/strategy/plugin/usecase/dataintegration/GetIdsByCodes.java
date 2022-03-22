package com.cs.config.strategy.plugin.usecase.dataintegration;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.model.dataintegration.IEntityLabelCodeMapModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetIdsByCodes extends AbstractOrientPlugin {
  
  public GetIdsByCodes(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetIdsByCodes/*" };
  }
  
  @Override
  protected Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, List<String>> entityLabelCodeMap = (Map<String, List<String>>) requestMap
        .get(IEntityLabelCodeMapModel.ENTITYlABELCODEMAP);
    Map<String, Object> entityLabelVsCodeIdMap = new HashMap<>();
    Map<String, Object> mapToReturn = new HashMap<>();
    
    entityLabelCodeMap.forEach((entityLabel, codes) -> {
      Map<String, Object> codeVsidMap = new HashMap<>();
      String query = "Select from " + entityLabel + " where code in " + EntityUtil.quoteIt(codes);
      Iterable<Vertex> resultIterable = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      resultIterable.forEach(entityNode -> {
        codeVsidMap.put(entityNode.getProperty(CommonConstants.CODE_PROPERTY),
            UtilClass.getCodeNew(entityNode));
      });
      entityLabelVsCodeIdMap.put(entityLabel, codeVsidMap);
    });
    
    mapToReturn.put(IEntityLabelCodeMapModel.ENTITYlABELCODEMAP, entityLabelVsCodeIdMap);
    return mapToReturn;
  }
}
