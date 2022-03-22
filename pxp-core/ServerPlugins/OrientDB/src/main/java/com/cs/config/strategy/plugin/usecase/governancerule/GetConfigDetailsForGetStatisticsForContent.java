package com.cs.config.strategy.plugin.usecase.governancerule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeMapModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeMapModel;
import com.cs.core.runtime.interactor.model.statistics.IGetAllStatisticsWithIdsRequestModel;
import com.cs.core.runtime.interactor.model.statistics.IGetStatisticsForKpiResponseModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForGetStatisticsForContent extends AbstractOrientPlugin {
  
  public GetConfigDetailsForGetStatisticsForContent(
      final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    String userId = (String) requestMap.get(IGetAllStatisticsWithIdsRequestModel.USER_ID);
    Vertex roleNode = RoleUtils.getRoleFromUser(userId);
    // Check for added KPI in role.
    Iterable<Vertex> kpiVertexIterator = roleNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_KPI_ROLE);
    List<String> kpiCodes = new ArrayList<String>();
    if (kpiVertexIterator.iterator().hasNext()) {
      for(Vertex kpiVertex: kpiVertexIterator) {
        kpiCodes.add(kpiVertex.getProperty(IIdLabelCodeMapModel.CODE));
      }
    }
    
    List<String> kpiBlockIds = (List<String>) requestMap.get(IGetAllStatisticsWithIdsRequestModel.KPI_BLOCK_IDS);
    String query = "select from " + VertexLabelConstants.GOVERNANCE_RULE_BLOCK + " where "
        + CommonConstants.CODE_PROPERTY + " IN" + EntityUtil.quoteIt(kpiBlockIds);
    
    Iterable<Vertex> kpiBlockVertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    Map<String, IIdLabelCodeMapModel> referencedKpi = new HashMap<>();
    Map<String, String> kpiBlocks = new HashMap<>();
    Map<String, String> KpiChildInfo = new HashMap<>();
    
    
    for(Vertex kpiBlockVertex: kpiBlockVertices) {
      
      Iterable<Vertex> kpiVertices = kpiBlockVertex.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_KPI);
      for(Vertex kpiVertex: kpiVertices) {
        String kpiVertexCode = kpiVertex.getProperty(IIdLabelCodeMapModel.CODE);
        if (!kpiCodes.isEmpty() && !kpiCodes.contains(kpiVertexCode)) {
          continue;
        }        
        if(!referencedKpi.containsKey(kpiVertexCode)) {
          IIdLabelCodeMapModel idLabeCodeMapModel = new IdLabelCodeMapModel();
          
          idLabeCodeMapModel.setCode(kpiVertexCode);
          idLabeCodeMapModel.setId(kpiVertexCode);
          idLabeCodeMapModel.setLabel((String) UtilClass.getValueByLanguage(kpiVertex, CommonConstants.LABEL_PROPERTY));
          referencedKpi.put(kpiVertexCode, idLabeCodeMapModel);
          idLabeCodeMapModel.setBlockId(KpiChildInfo);
        }
        KpiChildInfo.put(kpiBlockVertex.getProperty(IIdLabelCodeMapModel.CODE), kpiBlockVertex.getProperty(CommonConstants.TYPE));
        kpiBlocks.put(kpiBlockVertex.getProperty(IIdLabelCodeMapModel.CODE), kpiVertexCode);
      }
    }
    
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IGetStatisticsForKpiResponseModel.REFERENCED_KPI, referencedKpi);
    returnMap.put(IGetStatisticsForKpiResponseModel.KPI_BLOCKS, kpiBlocks);
    
    /*Map<String, Object> referencedKpi = new HashMap<>();
    
    for (Vertex conf : confVertices) {
      conf.getVertices(Direction.OUT, "");
      conf// get Parent
      Iterator // hasNext .next
      
      //check referencedKpi parent ? fill : nothing
      
      //id : type 
      Map<String, String> idLabel = new HashMap<>();
      String kpiId = UtilClass.getCodeNew(kpi);
      idLabel.put(IIdLabelCodeModel.ID, kpiId);
      idLabel.put(IIdLabelCodeModel.CODE, kpi.getProperty(IIdLabelCodeModel.CODE));
      idLabel.put(IIdLabelCodeModel.LABEL,
          (String) UtilClass.getValueByLanguage(kpi, CommonConstants.LABEL_PROPERTY));
      referencedKpi.put(kpiId, idLabel);
    }
    
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IGetStatisticsForContentResponseModel.REFERENCED_KPI, referencedKpi);*/
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForGetStatisticsForContent/*" };
  }
}
