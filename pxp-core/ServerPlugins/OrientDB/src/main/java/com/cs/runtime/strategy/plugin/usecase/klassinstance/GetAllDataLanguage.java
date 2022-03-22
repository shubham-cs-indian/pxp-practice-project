package com.cs.runtime.strategy.plugin.usecase.klassinstance;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.language.IGetAllDataLanguagesInfoModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.languageinstance.IGetAllDataLanguagesModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetAllDataLanguage extends AbstractOrientPlugin {
  
  public GetAllDataLanguage(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllDataLanguage/*" };
  }
  
  private static final List<String> fieldsToFetch = Arrays.asList(ILanguage.ID, ILanguage.ICON,
      ILanguage.LABEL, ILanguage.CODE, ILanguage.NUMBER_FORMAT, ILanguage.DATE_FORMAT,
      ILanguage.LOCALE_ID);
  
  @Override
  @SuppressWarnings("unchecked")
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    List<String> languageCodes = (List<String>) requestMap
        .get(IGetAllDataLanguagesModel.LANGUAGE_CODES);
    
    String conditionQueryPart = " where code  in " + EntityUtil.quoteIt(languageCodes)
        + " AND isDataLanguage = " + EntityUtil.quoteIt(true);
    String query = "select from " + VertexLabelConstants.LANGUAGE + conditionQueryPart ;
    ;
    Iterable<Vertex> languageVertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    Map<String, Object> languageInfoMap = new HashMap<>();
    
    for (Vertex vertex : languageVertices) {
      Map<String, Object> languageInfo = UtilClass.getMapFromVertex(fieldsToFetch, vertex, null);
      languageInfoMap.put((String) languageInfo.get(ILanguage.CODE), languageInfo);
    }
    
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IGetAllDataLanguagesInfoModel.REFERENCED_LANGUAGES, languageInfoMap);
    
    return returnMap;
  }
}
