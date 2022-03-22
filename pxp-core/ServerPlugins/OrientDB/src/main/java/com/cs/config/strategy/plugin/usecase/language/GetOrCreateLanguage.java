package com.cs.config.strategy.plugin.usecase.language;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.config.interactor.model.language.IGetOrCreateLanguageModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetOrCreateLanguage extends AbstractOrientPlugin {
  
  public GetOrCreateLanguage(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> languages = (List<Map<String, Object>>) requestMap
        .get(IGetOrCreateLanguageModel.LANGUAGES);
    
    for (Map<String, Object> language : languages) {
      String languageId = (String) language.get(ILanguage.ID);
      try {
        UtilClass.getVertexById(languageId, VertexLabelConstants.LANGUAGE);
      }
      catch (NotFoundException e) {
        createAttributionTaxonomy(language);
      }
    }
    UtilClass.getGraph()
        .commit();
    return new HashMap<>();
  }
  
  private void createAttributionTaxonomy(Map<String, Object> languageMap) throws Exception
  {
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.LANGUAGE,
        CommonConstants.CODE_PROPERTY);
    UtilClass.createNode(languageMap, vertexType, new ArrayList<String>());
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetOrCreateLanguage/*" };
  }
}
