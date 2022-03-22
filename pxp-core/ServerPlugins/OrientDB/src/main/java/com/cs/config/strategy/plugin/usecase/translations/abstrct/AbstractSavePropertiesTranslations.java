package com.cs.config.strategy.plugin.usecase.translations.abstrct;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.translations.ISaveStandardTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsRequestModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsResponseModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public abstract class AbstractSavePropertiesTranslations extends AbstractOrientPlugin {
  
  protected abstract void saveTranslations(String vertexLabel, List<String> languages,
      List<Map<String, Object>> list, IExceptionModel failure,
      List<Map<String, Object>> returnList);
  
  public AbstractSavePropertiesTranslations(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String entityType = (String) requestMap.get(ISaveTranslationsRequestModel.ENTITY_TYPE);
    String vertexLabel = EntityUtil.getVertexLabelByEntityType(entityType);
    List<String> languages = (List<String>) requestMap.get(ISaveTranslationsRequestModel.LANGAUGES);
    
    List<Map<String, Object>> list = (List<Map<String, Object>>) requestMap
        .get(ISaveStandardTranslationsRequestModel.DATA);
    IExceptionModel failure = new ExceptionModel();
    List<Map<String, Object>> returnList = new ArrayList<>();
    
    saveTranslations(vertexLabel, languages, list, failure, returnList);
    UtilClass.getGraph()
        .commit();
    
    Map<String, Object> responseMap = new HashMap<String, Object>();
    responseMap.put(ISaveTranslationsResponseModel.SUCCESS, returnList);
    responseMap.put(ISaveTranslationsResponseModel.FAILURE, failure);
    return responseMap;
  }
}
