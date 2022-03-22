package com.cs.config.strategy.plugin.usecase.asset.iconlibrary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.exception.icon.IconNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.exception.configuration.ExceptionUtil;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;


/**
 * This is the plugin for bulk deletion.
 * @author jamil.ahmad
 *
 */
public class BulkDeleteIcons extends AbstractOrientPlugin {
  
  public BulkDeleteIcons(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|BulkDeleteIcons/*" };
  }
  
  @Override
  @SuppressWarnings("unchecked")
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> deletedIds = new ArrayList<String>();
    
    List<String> idsToDelete = (List<String>) requestMap.get(IIdsListParameterModel.IDS);
    IExceptionModel failure = new ExceptionModel();
    // forced deletion
    for (String iconToDelete : idsToDelete) {
      try {
        Vertex iconNode = UtilClass.getVertexById(iconToDelete, VertexLabelConstants.ENTITY_TYPE_ICON);
        deletedIds.add(iconToDelete);
        iconNode.remove();
      }
      catch (NotFoundException ex) {
        // for already deleted icons added in exception.
        ExceptionUtil.addFailureDetailsToFailureObject(failure, new IconNotFoundException(ex), iconToDelete, null);
      }
    }
    
    UtilClass.getGraph().commit();
    
    //Preparing return model
    Map<String, Object> returnMap = new HashedMap<String, Object>();
    returnMap.put(IBulkDeleteReturnModel.SUCCESS, deletedIds);
    returnMap.put(IBulkDeleteReturnModel.FAILURE, failure);
    
    return returnMap;
  }
  
}
