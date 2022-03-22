package com.cs.export.config.strategy.plugin.usecase.icons;

import com.cs.config.strategy.plugin.delivery.response.ResponseCarrier;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.orientechnologies.orient.server.network.protocol.http.OHttpRequest;
import com.orientechnologies.orient.server.network.protocol.http.OHttpResponse;
import com.orientechnologies.orient.server.network.protocol.http.command.OServerCommandAuthenticatedDbAbstract;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetAllIconIds extends OServerCommandAuthenticatedDbAbstract {
  
  public GetAllIconIds(final OServerCommandConfiguration iConfiguration)
  {
    super();
  }
  
  @Override
  public boolean execute(final OHttpRequest iRequest, OHttpResponse iResponse) throws Exception
  {
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    try {
      ODatabaseDocumentTx database = (ODatabaseDocumentTx) getProfiledDatabaseInstance(iRequest);
      OrientGraph graph = new OrientGraph(database);
      UtilClass.setGraph(graph);
      
      Iterable<Vertex> i = graph.command(new OCommandSQL(
          "select @class,icon from V where icon is not NULL and icon <> \"\" and icon.length() < 40"))
          .execute();
      
      List<String> icons = new ArrayList<>();
      for (Vertex icon : i) {
        // System.out.println(icon.getProperty("icon"));
        icons.add(icon.getProperty("icon"));
      }
      
      Iterable<Vertex> previewImages = graph.command(new OCommandSQL(
          "select @class,previewImage from V where icon is not NULL and previewImage <> \"\" and previewImage.length() < 40"))
          .execute();
      
      for (Vertex previewImage : previewImages) {
        icons.add(previewImage.getProperty("previewImage"));
      }
      
      returnMap.put("ids", icons);
      graph.commit();
      ResponseCarrier.successResponse(iResponse, returnMap);
      
    }
    catch (Exception e) {
      ResponseCarrier.failedResponse(iResponse, e);
    }
    return false;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllIconsIds/*" };
  }
}
