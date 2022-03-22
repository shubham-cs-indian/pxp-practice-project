/*package com.cs.core.config.strategy.configuration.embeddedorientdb;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.cs.core.runtime.interactor.model.configuration.IModel;

@Configuration
public class OrientDBInitializeConnectionStrategy extends OrientDBBaseStrategy implements IInitializeConnectionStrategy {

  @Autowired
  protected String orientDBPassword;

  @Autowired
  protected String orientDBUser;

  @Autowired
  protected OrientDBEmbeddable orientDBEmbeddable;

  @PostConstruct
  private void init()
  {
    try {
      String uri = "/" + "listDatabases";
      if (executeWithLoadBalancer) {
        executeWithLoadBalancer(uri, null, "GET", null);
      } else {
        orientDBEmbeddable.connectOrientDbInEmbedded();
        // initializeData.execute(new VoidModel());
        // uri = "http://"+orientDBHost+":"+orientDBPort+uri;
        //executeWithoutLoadBalancer(new URI(uri), null, "GET");
      }
     } catch (Exception e) {
       RDBMSLogger.instance().exception(e);
     }

  }

  @Override
  public IModel execute(IModel model) throws Exception {

    return null;
  }


}
*/
