package com.cs.repository.language;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Vertex;
import java.util.List;

public class LanguageRepository {
  
  /**
   * @author bhagwat.bade
   * @return
   * @throws Exception
   */
  public static Iterable<Vertex> getRootLanguages() throws Exception
  {
    String query = "SELECT *,format('%s" + (char) 254 + "',ifnull("
        + EntityUtil.getLanguageConvertedField(ILanguage.LABEL) + ", '')) as SORT_COL FROM "
        + VertexLabelConstants.LANGUAGE + " WHERE outE('"
        + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF + "').size() = 0 ORDER BY SORT_COL "
        + "," + ILanguage.CODE + " ASC";
    return UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
  }
  
  /**
   * @author bhagwat.bade
   * @param rid
   * @return
   * @throws Exception
   */
  public static Iterable<Vertex> getLanguageChildren(String rid) throws Exception
  {
    String query = "SELECT *,format('%s" + (char) 254 + "',ifnull("
        + EntityUtil.getLanguageConvertedField(ILanguage.LABEL)
        + ", '')) as SORT_COL FROM (SELECT EXPAND(IN('"
        + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF + "')) FROM " + rid
        + " ) ORDER BY SORT_COL " + "," + ILanguage.CODE + " ASC";
    return UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
  }
  
  /**
   * @author bhagwat.bade
   * @param key
   * @return
   * @throws Exception
   */
  public static Iterable<Vertex> getDataOrUILanguages(String key) throws Exception
  {
    String query = "SELECT *, format('%s" + (char) 254 + "',ifnull("
        + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY)
        + ", '')) as SORT_COL FROM " + VertexLabelConstants.LANGUAGE + " WHERE " + key + " = "
        + true + " ORDER BY SORT_COL " + "," + ILanguage.CODE + " ASC";
    return UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
  }
  
  /**
   * @author bhagwat.bade
   * @param languageCodes
   * @return
   * @throws Exception
   */
  public static Iterable<Vertex> getLanguageInfo(List<String> languageCodes) throws Exception
  {
    String query = "select *,format('%s" + (char) 254 + "',ifnull("
        + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY)
        + ", '')) as SORT_COL from " + VertexLabelConstants.LANGUAGE + "  where code in "
        + EntityUtil.quoteIt(languageCodes) + " ORDER BY SORT_COL " + ","
        + CommonConstants.CODE_PROPERTY + " ASC";
    return UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
  }
  
  public static Iterable<Vertex> getCurrentDefaultLanguage() throws Exception
  {
    String query = "SELECT * from " + VertexLabelConstants.LANGUAGE + " WHERE "
        + ILanguage.IS_DEFAULT_LANGUAGE + " = " + true;
    return UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
  }
  
  public static Iterable<Vertex> getlanguageInheritedVerticesCount(List<String> languageCodes)
  {
    String query = "SELECT count(*) from (traverse both('"
        + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF + "') from (select from "
        + VertexLabelConstants.LANGUAGE + " where code in" + EntityUtil.quoteIt(languageCodes)
        + " )) where $depth > 0 And code in" + EntityUtil.quoteIt(languageCodes);
    
    return UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
  }
  
  public static Iterable<Vertex> getUITranslationLabelByCode(List<String> codes) throws Exception{
    String query = "select *,format('%s" + (char) 254 + "',ifnull("
        + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY)
        + ", '')) as SORT_COL from " + VertexLabelConstants.UI_TRANSLATIONS + "  where code in "
        + EntityUtil.quoteIt(codes) + " ORDER BY SORT_COL " + ","
        + CommonConstants.CODE_PROPERTY + " ASC";
    return UtilClass.getGraph().command(new OCommandSQL(query)).execute();
  }
}
