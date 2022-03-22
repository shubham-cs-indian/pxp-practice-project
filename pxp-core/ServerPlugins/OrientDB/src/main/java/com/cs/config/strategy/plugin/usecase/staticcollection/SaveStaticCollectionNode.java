package com.cs.config.strategy.plugin.usecase.staticcollection;

import com.cs.config.strategy.plugin.usecase.klass.util.SaveKlassUtil;
import com.cs.config.strategy.plugin.usecase.staticcollection.abstrct.AbstractSaveStaticCollectionNode;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.exception.staticcollection.CollectionNodeNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.entity.collections.IStaticCollection;
import com.cs.core.runtime.interactor.model.collections.ISaveStaticCollectionDetailsModel;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SaveStaticCollectionNode extends AbstractSaveStaticCollectionNode {
  
  public SaveStaticCollectionNode(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveStaticCollectionNode/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    UtilClass.setNodesForVersionIncrement(new HashSet<>());
    Map<String, Object> saveCollectionMap = (Map<String, Object>) requestMap.get("collection");
    
    String collectionId = (String) saveCollectionMap.get(ISaveStaticCollectionDetailsModel.ID);
    Vertex collectionNode = null;
    try {
      collectionNode = UtilClass.getVertexById(collectionId, VertexLabelConstants.COLLECTION);
    }
    catch (NotFoundException e) {
      throw new CollectionNodeNotFoundException();
    }
    
    SaveKlassUtil.updateTemplateLabelIfKlassLabelChanged(saveCollectionMap, collectionNode);
    collectionNode.setProperty(IStaticCollection.LABEL,
        saveCollectionMap.get(IStaticCollection.LABEL));
    graph.commit();
    
    Map<String, Object> returnMap = new HashMap<>();
    return returnMap;
  }
}
