/*
 * (DataRuleUpdateRemoveValueProperty) : This migration script dated 06-09-2016
 * is to update all data rules in Orient configuration to use property "values"
 * instead of value. Previously both properties were being used.
 */

package com.cs.config.strategy.plugin.usecase.repair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.config.standard.IStandardConfig;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class Orient_Migration_Script_06_09_2016 extends AbstractOrientPlugin {
  
  public static final List<String> disabledAttributes = new ArrayList<>(
      Arrays.asList(IStandardConfig.StandardProperty.createdonattribute.toString(),
          "createdbyattribute", "lastmodifiedattribute", "lastmodifiedbyattribute", "exifattribute",
          "iptcattribute", "xmpattribute", "otherattribute"));
  
  public Orient_Migration_Script_06_09_2016(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = null;
    
    // For data rules
    Iterator<Vertex> dataRuleIterator = UtilClass.getGraph()
        .getVertices(VertexLabelConstants.DATA_RULE, new String[] {}, new String[] {})
        .iterator();
    while (dataRuleIterator.hasNext()) {
      Vertex dataRule = dataRuleIterator.next();
      System.out.println(dataRule.getProperty("label") + "");
      
      List<Map<String, Object>> attributes = dataRule.getProperty("attributes");
      for (Map<String, Object> attribute : attributes) {
        List<Map<String, Object>> rules = (List<Map<String, Object>>) attribute.get("rules");
        for (Map<String, Object> rule : rules) {
          List<String> values = (List<String>) rule.get("values");
          String linkedRuleList = (String) rule.get("ruleListLinkId");
          if (linkedRuleList == null || linkedRuleList.equals("")) {
            if (rule.get("value") != null) {
              values.clear();
              values.add((String) rule.get("value"));
            }
          }
          rule.remove("value");
        }
      }
      dataRule.setProperty("attributes", attributes);
      
      List<Map<String, Object>> relationships = dataRule.getProperty("relationships");
      for (Map<String, Object> relationship : relationships) {
        List<Map<String, Object>> rules = (List<Map<String, Object>>) relationship.get("rules");
        for (Map<String, Object> rule : rules) {
          List<String> values = (List<String>) rule.get("values");
          String linkedRuleList = (String) rule.get("ruleListLinkId");
          if (linkedRuleList == null || linkedRuleList.equals("")) {
            if (rule.get("value") != null) {
              values.clear();
              values.add((String) rule.get("value"));
            }
          }
          rule.remove("value");
        }
      }
      dataRule.setProperty("relationships", relationships);
    }
    
    UtilClass.getGraph()
        .commit();
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_06_09_2016/*" };
  }
}
