package com.cs.config.strategy.plugin.usecase.tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cs.config.strategy.plugin.usecase.language.util.LanguageUtil;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.tag.ITagValue;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.Seperators;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.exception.language.LanguageNotFoundException;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

/**
 * @author tauseef
 **/
public class GetTagValueByCode extends AbstractOrientPlugin {
  
  public static final List<String> fieldsToFetchForTagValues = Arrays.asList(ITagValue.COLOR,
      ITagValue.LABEL, ITagValue.CODE);
  
  public GetTagValueByCode(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    List<String> tagIds = (List<String>) requestMap.get("ids");
    List<Map<String, Object>> tagsList = new ArrayList<>();
    
    String dataLanguage = UtilClass.getLanguage()
        .getDataLanguage();
    List<String> locales = LanguageUtil.getReverseTreeLocaleCode(dataLanguage);
    
    for (String tagId : tagIds) {
      Vertex tagValueNode = UtilClass.getVertexByIndexedId(tagId, VertexLabelConstants.ENTITY_TAG);
      
      HashMap<String, Object> tagValueMap = new HashMap<String, Object>();
      tagValueMap.putAll(UtilClass.getMapFromVertex(fieldsToFetchForTagValues, tagValueNode));
      
      for (String userLanguage : locales) {
        String newkey = ITagValue.LABEL + Seperators.FIELD_LANG_SEPERATOR + userLanguage;
        String value = (String) tagValueNode.getProperty(newkey);
        if (!StringUtils.isEmpty(value)) {
          tagValueMap.put(ITagValue.LABEL, value);
          break;
        }
      }
      tagsList.add(tagValueMap);
    }
    Map<String, Object> response = new HashMap<>();
    response.put("list", tagsList);
    
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTagValueByCode/*" };
  }
}
