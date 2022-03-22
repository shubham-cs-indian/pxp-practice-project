package com.cs.config.strategy.plugin.usecase.klass;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoForTypeRequestModel;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetSectionInfoForKlass extends AbstractOrientPlugin {
  
  public GetSectionInfoForKlass(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    String klassId = (String) requestMap.get(IGetSectionInfoForTypeRequestModel.TYPE_ID);
    List<String> sectionIds = (List<String>) requestMap
        .get(IGetSectionInfoForTypeRequestModel.SECTION_IDS);
    Vertex klassNode = UtilClass.getVertexById(klassId,
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    Iterable<Vertex> sectionNodes = UtilClass.getVerticesByIds(sectionIds,
        VertexLabelConstants.ENTITY_TYPE_KLASS_SECTION);
    Map<String, Object> klassEntityMap = new HashMap<String, Object>();
    KlassUtils.addSectionsToKlassMap(klassNode, klassEntityMap, sectionNodes.iterator(), true);
    
    Map<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put(IGetSectionInfoModel.LIST, klassEntityMap.get(IKlass.SECTIONS));
    
    KlassGetUtils.fillReferencedConfigDetailsForSection(returnMap);
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetSectionInfoForKlass/*" };
  }
}
