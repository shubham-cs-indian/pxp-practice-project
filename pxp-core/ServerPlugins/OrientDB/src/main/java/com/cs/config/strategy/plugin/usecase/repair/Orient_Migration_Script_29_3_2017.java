package com.cs.config.strategy.plugin.usecase.repair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.cs.config.strategy.plugin.delivery.response.ResponseCarrier;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attribute.IPriceAttribute;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.orientechnologies.orient.server.network.protocol.http.OHttpRequest;
import com.orientechnologies.orient.server.network.protocol.http.OHttpResponse;
import com.orientechnologies.orient.server.network.protocol.http.command.OServerCommandAuthenticatedDbAbstract;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class Orient_Migration_Script_29_3_2017 extends OServerCommandAuthenticatedDbAbstract {
  
  /*
  This script does the following,
  
  1. change type of following attribute from standard types to respective non standard types -
     Address, GTIN, Maximum Price, Minimum Price, List Price, Selling Price, Due Date, Long Description,
     PID, Short Description, SKU
  
  2. Adds defaultUnit & precision properties to Maximum Price, List Price, Selling Price, Minimum Price attributes
  
  Date: 29-03-2017
   */
  
  public Orient_Migration_Script_29_3_2017(final OServerCommandConfiguration iConfiguration)
  {
    super();
  }
  
  @Override
  public boolean execute(final OHttpRequest iRequest, OHttpResponse iResponse) throws Exception
  {
    
    List<String> pricingAttributeIds = Arrays.asList(SystemLevelIds.LIST_PRICE_ATTRIBUTE,
        SystemLevelIds.SELLING_PRICE_ATTRIBUTE, SystemLevelIds.MAXIMUM_PRICE_ATTRIBUTE,
        SystemLevelIds.MINIMUM_PRICE_ATTRIBUTE);
    
    HashMap<String, Object> response = new HashMap<>();
    
    try {
      ODatabaseDocumentTx database = (ODatabaseDocumentTx) getProfiledDatabaseInstance(iRequest);
      OrientGraph graph = new OrientGraph(database);
      UtilClass.setGraph(graph);
      
      // for pricing attributes
      for (String pricingAttributeId : pricingAttributeIds) {
        Vertex attribute = UtilClass.getVertexById(pricingAttributeId,
            VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
        attribute.setProperty(IAttribute.TYPE,
            "com.cs.config.interactor.entity.concrete.attribute.CurrencyAttribute");
        attribute.setProperty(IPriceAttribute.DEFAULT_UNIT, "EUR");
        attribute.setProperty(IPriceAttribute.PRECISION, "2");
      }
      
      // for address attribute
      Vertex addressAttribute = UtilClass.getVertexById(SystemLevelIds.ADDRESS_ATTRIBUTE,
          VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      addressAttribute.setProperty(IAttribute.TYPE,
          "com.cs.config.interactor.entity.concrete.attribute.HTMLAttribute");
      
      // for Due Date attribute
      Vertex dueDateAttribute = UtilClass.getVertexById(SystemLevelIds.DUE_DATE_ATTRIBUTE,
          VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      dueDateAttribute.setProperty(IAttribute.TYPE,
          "com.cs.config.interactor.entity.concrete.attribute.DateAttribute");
      
      // for gtin attribute
      Vertex gtinAttribute = UtilClass.getVertexById(SystemLevelIds.GTIN_ATTRIBUTE,
          VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      gtinAttribute.setProperty(IAttribute.TYPE,
          "com.cs.config.interactor.entity.concrete.attribute.NumberAttribute");
      
      // for Long Description attribute
      Vertex longDescriptionAttribute = UtilClass.getVertexById(
          SystemLevelIds.LONG_DESCRIPTION_ATTRIBUTE, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      longDescriptionAttribute.setProperty(IAttribute.TYPE,
          "com.cs.config.interactor.entity.concrete.attribute.HTMLAttribute");
      
      // for Short Description attribute
      Vertex shortDescriptionAttribute = UtilClass.getVertexById(
          SystemLevelIds.SHORT_DESCRIPTION_ATTRIBUTE, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      shortDescriptionAttribute.setProperty(IAttribute.TYPE,
          "com.cs.config.interactor.entity.concrete.attribute.HTMLAttribute");
      
      // for PID attribute
      Vertex PIDAttribute = UtilClass.getVertexById(SystemLevelIds.PID_ATTRIBUTE,
          VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      PIDAttribute.setProperty(IAttribute.TYPE,
          "com.cs.config.interactor.entity.concrete.attribute.TextAttribute");
      
      // for SKU attribute
      Vertex SKUAttribute = UtilClass.getVertexById(SystemLevelIds.SKU_ATTRIBUTE,
          VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      SKUAttribute.setProperty(IAttribute.TYPE,
          "com.cs.config.interactor.entity.concrete.attribute.TextAttribute");
      
      graph.commit();
      response.put("status", "SUCCESS");
      ResponseCarrier.successResponse(iResponse, response);
    }
    catch (Exception e) {
      ResponseCarrier.failedResponse(iResponse, e);
    }
    
    return false;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_29_3_2017/*" };
  }
}
