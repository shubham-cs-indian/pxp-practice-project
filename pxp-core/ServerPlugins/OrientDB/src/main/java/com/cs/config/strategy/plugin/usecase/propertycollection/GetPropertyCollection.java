package com.cs.config.strategy.plugin.usecase.propertycollection;

import com.cs.config.strategy.plugin.usecase.propertycollection.util.PropertyCollectionUtils;
import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.exception.propertycollection.PropertyCollectionNotFoundException;
import com.cs.core.config.interactor.model.propertycollection.IGetPropertyCollectionModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GetPropertyCollection extends AbstractOrientPlugin {
  
  public GetPropertyCollection(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    Vertex vertex = null;
    try {
      vertex = UtilClass.getVertexById((String) requestMap.get(IPropertyCollection.ID),
          VertexLabelConstants.PROPERTY_COLLECTION);
    }
    catch (NotFoundException e) {
      throw new PropertyCollectionNotFoundException();
    }
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.putAll(PropertyCollectionUtils.getPropertyCollection(vertex));
    Map<String, Object> responseTabMap = TabUtils.getMapFromConnectedTabNode(vertex, Arrays
        .asList(CommonConstants.CODE_PROPERTY, IIdLabelModel.LABEL, IPropertyCollection.CODE));
    returnMap.put(IGetPropertyCollectionModel.TAB, responseTabMap);
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetPropertyCollection/*" };
  }
}
