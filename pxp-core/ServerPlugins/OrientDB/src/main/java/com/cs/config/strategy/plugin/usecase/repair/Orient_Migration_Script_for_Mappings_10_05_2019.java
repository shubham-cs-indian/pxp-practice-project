/*
 * (Mapped Columns are getting displayed empty in red box if UI language is
 * changed) Replacing the label property with columnName property and first
 * priority for copying the value is en_US.
 */
package com.cs.config.strategy.plugin.usecase.repair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.config.interactor.entity.mapping.IMapping;
import com.cs.core.runtime.interactor.constants.application.Seperators;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.repository.language.LanguageRepository;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class Orient_Migration_Script_for_Mappings_10_05_2019 extends AbstractOrientMigration {
  
  private static final List<String> fieldsToFetch = Arrays.asList(ILanguage.CODE);
  
  public Orient_Migration_Script_for_Mappings_10_05_2019(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_for_Mappings_10_05_2019/*" };
  }
  
  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put("status", "success");
    List<String> languagesToFetch = new ArrayList<>(
        Arrays.asList(ILanguage.IS_USER_INTERFACE_LANGUAGE, ILanguage.IS_DATA_LANGUAGE));
    List<String> uiLanguages = getLanguages(languagesToFetch, "");
    
    List<String> listOfNodes = new ArrayList<>(
        Arrays.asList(VertexLabelConstants.COLUMN_MAPPING, VertexLabelConstants.VALUE));
    for (String node : listOfNodes) {
      Iterator<Vertex> iterator = UtilClass.getGraph()
          .getVerticesOfClass(node)
          .iterator();
      while (iterator.hasNext()) {
        String label = new String();
        Vertex columnMappingVartex = iterator.next();
        
        String languagekey = IMapping.LABEL + Seperators.FIELD_LANG_SEPERATOR + "en_US";
        label = columnMappingVartex.getProperty(languagekey);
        if (label == null) {
          for (String uiLanguage : uiLanguages) {
            languagekey = IMapping.LABEL + Seperators.FIELD_LANG_SEPERATOR + uiLanguage;
            label = columnMappingVartex.getProperty(languagekey);
            if (label != null) {
              break;
            }
          }
        }
        
        if (label == null) {
          continue;
        }
        
        String updateQuery = "UPDATE " + node + " SET columnName = \"" + label + "\" where @rid= "
            + columnMappingVartex.getId();
        
        try {
          UtilClass.getGraph()
              .command(new OCommandSQL(updateQuery))
              .execute();
        }
        catch (Throwable e) {
          e.printStackTrace();
          //RDBMSLogger.instance().info("Error: " + updateQuery);
          returnMap.put("status", "Failed. Check the Orient console.");
        }
        for (String uiLanguage : uiLanguages) {
          String key = IMapping.LABEL + Seperators.FIELD_LANG_SEPERATOR + uiLanguage;
          columnMappingVartex.removeProperty(key);
        }
      }
    }
    UtilClass.getGraph()
        .commit();
    
    return returnMap;
  }
  
  private List<String> getLanguages(List<String> languagesToFetch, String dataLanguage)
      throws Exception
  {
    Map<String, Object> languageMap = new HashMap<>();
    Set<String> languageCodes = new HashSet<>();
    for (String languageTofetch : languagesToFetch) {
      Iterable<Vertex> languageVertices = LanguageRepository.getDataOrUILanguages(languageTofetch);
      for (Vertex language : languageVertices) {
        languageMap = UtilClass.getMapFromVertex(fieldsToFetch, language, dataLanguage);
        languageCodes.add((String) languageMap.get(ILanguage.CODE));
      }
    }
    return new ArrayList<>(languageCodes);
  }
}
