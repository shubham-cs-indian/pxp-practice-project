package com.cs.config.strategy.plugin.usecase.asset.iconlibrary;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.exception.icon.IconNotFoundException;
import com.cs.core.config.interactor.model.asset.IIconModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

/**
 * This orient plugin edits icon information.
 * @author pranav.huchche
 *
 */
public class SaveOrReplaceIcon extends AbstractOrientPlugin {
  
  public SaveOrReplaceIcon(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveOrReplaceIcon/*" };
  }
  
  public static final List<String> fieldsToExclude = Arrays.asList(IIconModel.CREATED_ON); 
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String iconCode = (String) requestMap.get(IIconModel.CODE);
    String iconName = (String) requestMap.get(IIconModel.LABEL);
    String iconImage = (String) requestMap.get(IIconModel.ICON_KEY);
    Vertex iconVertex = null;
    Map<String, Object> iconMap = null;
    
    // Fetch icon vertex according to code
    try {
      iconVertex = UtilClass.getVertexByCode(iconCode, VertexLabelConstants.ENTITY_TYPE_ICON);
    }
    catch (NotFoundException e) {
      throw new IconNotFoundException(e);
    }
    
    // Fetch map from iconVertex and modify name and assetKey
    if(iconVertex != null) {
      iconMap = UtilClass.getMapFromNode(iconVertex);
      
      if (iconName != null && !iconName.isEmpty()) {
        iconMap.put(IIconModel.LABEL, iconName);
      }
      if (iconImage != null && !iconImage.isEmpty()) {
        iconMap.put(IIconModel.ICON_KEY, iconImage);
      }
      iconMap.put(IIconModel.MODIFIED_ON, System.currentTimeMillis());
    }
    
    // Save modified vertex in Orient
    if(iconMap != null) {
      UtilClass.saveNode(iconMap, iconVertex, fieldsToExclude);
    }
    UtilClass.getGraph().commit();
    
    return iconMap;
  }
  
}
