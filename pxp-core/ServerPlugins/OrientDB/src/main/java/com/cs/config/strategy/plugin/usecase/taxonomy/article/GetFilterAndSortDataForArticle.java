package com.cs.config.strategy.plugin.usecase.taxonomy.article;

import com.cs.config.strategy.plugin.usecase.base.taxonomy.AbstractGetFilterAndSortDataForKlass;
import com.cs.core.config.interactor.exception.klass.ArticleKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class GetFilterAndSortDataForArticle extends AbstractGetFilterAndSortDataForKlass {
  
  public GetFilterAndSortDataForArticle(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetFilterAndSortDataForArticle/*" };
  }
  
  @Override
  public Map<String, Object> executeInternal(Map<String, Object> requestMap) throws Exception
  {
    try {
      return super.executeInternal(requestMap);
    }
    catch (KlassNotFoundException e) {
      throw new ArticleKlassNotFoundException(e);
    }
  }
  
  @Override
  public String getKlassVertexType()
  {
    return VertexLabelConstants.ENTITY_TYPE_KLASS;
  }
}
