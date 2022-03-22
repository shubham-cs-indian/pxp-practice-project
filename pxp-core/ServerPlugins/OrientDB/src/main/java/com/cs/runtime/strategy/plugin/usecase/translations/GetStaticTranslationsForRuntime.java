package com.cs.runtime.strategy.plugin.usecase.translations;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.Seperators;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IGetStaticLabelTranslationsResponseModel;
import com.cs.core.runtime.interactor.model.translations.IGetStaticTranslationsForRuntimeRequestModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetStaticTranslationsForRuntime extends AbstractOrientPlugin {
  
  public GetStaticTranslationsForRuntime(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetStaticTranslationsForRuntime/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> screens = (List<String>) requestMap
        .get(IGetStaticTranslationsForRuntimeRequestModel.SCREENS);
    String language = (String) requestMap
        .get(IGetStaticTranslationsForRuntimeRequestModel.LANGUAGE);
    
    String query = "select code, defaultLabel, label" + Seperators.FIELD_LANG_SEPERATOR + language + " from "
        + VertexLabelConstants.UI_TRANSLATIONS + " where screens in " + EntityUtil.quoteIt(screens);
    Iterable<Vertex> vertices = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
    
    Map<String, Object> returnMap = new HashMap<>();
    for (Vertex staticLabel : vertices) {
      String label = staticLabel.getProperty(CommonConstants.LABEL_PROPERTY + Seperators.FIELD_LANG_SEPERATOR + language);
      String key = staticLabel.getProperty(CommonConstants.CODE_PROPERTY);
      if (label != null && !label.equals("")) {
        returnMap.put(key, label);
      }
      else {
        String defaultLabel = staticLabel.getProperty(CommonConstants.DEFAULT_LABEL);
        if (defaultLabel != null && !defaultLabel.equals("")) {
          returnMap.put(key, defaultLabel);
        }
        else {
          returnMap.put(key, key);
        }
      }
    }
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IGetStaticLabelTranslationsResponseModel.STATIC_LABEL_TRANSLATIONS, returnMap);
    
    return responseMap;
  }
}
