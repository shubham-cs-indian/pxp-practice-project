package com.cs.config.strategy.plugin.usecase.repair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class Config_Update_Node_Translation_Migration extends AbstractOrientPlugin {
  
  public Config_Update_Node_Translation_Migration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Config_Update_Node_Translation_Migration/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> classCodes = Arrays.asList(SystemLevelIds.SUPPLIER, SystemLevelIds.SUPPLIERS);
    Map<String, String> languageVSTranslation = new HashMap<String, String>();
    languageVSTranslation.put("en_US", "Business Partners");
    languageVSTranslation.put("es_ES", "Socias comerciales");
    languageVSTranslation.put("fr_FR", "partenaires d'affaires");
    languageVSTranslation.put("de_DE", "Geschäftspartner");
    
    String getQuery = "select * from " + VertexLabelConstants.ENTITY_TYPE_SUPPLIER + " where "
        + CommonConstants.CODE_PROPERTY + " in " + EntityUtil.quoteIt(classCodes);
    
    // Get Supplier Class Nodes
    Iterable<Vertex> supplierKlassNodes = UtilClass.getGraph()
        .command(new OCommandSQL(getQuery))
        .execute();
    
    for (Vertex supplierKlassNode : supplierKlassNodes) {
      for (Entry<String, String> langTranslation : languageVSTranslation.entrySet()) {
        supplierKlassNode.setProperty("label__" + langTranslation.getKey(),
            langTranslation.getValue());
        
        // Set English translations as default
        if (langTranslation.getKey().equals("en_US")) {
          supplierKlassNode.setProperty(CommonConstants.DEFAULT_LABEL, langTranslation.getValue());
        }
      }
    }
    
    try { // Delete UI Translation key TARGET_TAB
      Vertex targetTranslationNode = UtilClass.getVertexByCode("TARGET_TAB",
          VertexLabelConstants.UI_TRANSLATIONS);
      if (targetTranslationNode != null) {
        targetTranslationNode.remove();
      }
    }
    catch (NotFoundException exception) {
      // Do Nothing
    }
    
    UtilClass.getGraph()
        .commit();
    return null;
  }
}
