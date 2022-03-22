package com.cs.config.strategy.plugin.usecase.language;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterEntity;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAllLanguageCodes extends AbstractOrientPlugin {
  
  public GetAllLanguageCodes(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllLanguageCodes/*" };
  }
  
  public static final List<String> fieldsToFetch = Arrays.asList(IConfigMasterEntity.CODE);
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Iterable<Vertex> verticesOfClass = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.LANGUAGE);
    List<String> languageCodes = new ArrayList<>();
    
    for (Vertex language : verticesOfClass) {
      languageCodes.add(language.getProperty(CommonConstants.CODE_PROPERTY));
    }
    
    Map<String, Object> mapToReturn = new HashMap<>();
    mapToReturn.put(IIdsListParameterModel.IDS, languageCodes);
    
    return mapToReturn;
  }
}
