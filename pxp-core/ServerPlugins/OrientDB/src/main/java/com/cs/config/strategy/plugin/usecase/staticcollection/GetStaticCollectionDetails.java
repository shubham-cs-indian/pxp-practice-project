package com.cs.config.strategy.plugin.usecase.staticcollection;

import com.cs.config.strategy.plugin.usecase.staticcollection.util.StaticCollectionUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.exception.staticcollection.CollectionNodeNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.collections.IGetStaticCollectionInfoModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GetStaticCollectionDetails extends AbstractOrientPlugin {
  
  public GetStaticCollectionDetails(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetStaticCollectionDetails/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String id = (String) requestMap.get(IIdParameterModel.ID);
    Vertex collectionNode = null;
    Boolean isCollectionMovable = false;
    try {
      collectionNode = UtilClass.getVertexById(id, VertexLabelConstants.COLLECTION);
      Iterator<Vertex> sectionNodes = collectionNode
          .getVertices(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF)
          .iterator();
      if (!sectionNodes.hasNext()) {
        isCollectionMovable = true;
      }
    }
    catch (NotFoundException e) {
      throw new CollectionNodeNotFoundException();
    }
    
    HashMap<String, Object> returnMap = new HashMap<>();
    StaticCollectionUtils.getStaticCollectionDetailsMap(collectionNode, returnMap);
    returnMap.put(IGetStaticCollectionInfoModel.IS_COLLECTION_MOVABLE, isCollectionMovable);
    return returnMap;
  }
}
