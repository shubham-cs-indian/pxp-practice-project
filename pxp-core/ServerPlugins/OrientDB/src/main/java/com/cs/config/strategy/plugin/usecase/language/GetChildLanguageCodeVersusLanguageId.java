package com.cs.config.strategy.plugin.usecase.language;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.config.interactor.model.language.IDeleteLanguageRequestModel;
import com.cs.core.config.interactor.model.language.IGetChildLanguageCodeAgainstLanguageIdReturnModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetChildLanguageCodeVersusLanguageId extends AbstractOrientPlugin {
  
  public GetChildLanguageCodeVersusLanguageId(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetChildLanguageCodeVersusLanguageId/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, List<String>> languageIdChildLanguageCodeMap = new HashMap<>();
    List<String> idsToDelete = (List<String>) requestMap.get(IDeleteLanguageRequestModel.IDS);
    Iterable<Vertex> vertices = UtilClass.getVerticesByIndexedIds(idsToDelete,
        VertexLabelConstants.LANGUAGE);
    for (Vertex vertex : vertices) {
      String rid = vertex.getId()
          .toString();
      String query = "select from(traverse in('Child_Of') from " + rid + "strategy BREADTH_FIRST)";
      Iterable<Vertex> languageNodes = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      List<String> languageCode = new ArrayList<>();
      for (Vertex languageNode : languageNodes) {
        languageCode.add(languageNode.getProperty(ILanguage.CODE));
      }
      languageIdChildLanguageCodeMap.put(UtilClass.getCodeNew(vertex), languageCode);
    }
    
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(
        IGetChildLanguageCodeAgainstLanguageIdReturnModel.CHILD_LANGUAGE_CODE_AGAINST_LANGUAGE_ID,
        languageIdChildLanguageCodeMap);
    
    return returnMap;
  }
}
