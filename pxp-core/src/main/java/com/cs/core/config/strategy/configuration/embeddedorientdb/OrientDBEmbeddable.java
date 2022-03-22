/*package com.cs.core.config.strategy.configuration.embeddedorientdb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.orientechnologies.orient.server.OServer;
import com.orientechnologies.orient.server.OServerMain;

@Component
public class OrientDBEmbeddable {

  @Value("${orientdb.path}")
  protected String orientdbpath;

  public Integer connectOrientDbInEmbedded()
  {
    RDBMSLogger.instance().info("============= OrientDb Embedded Server Configuration Start ================");
    try {
      FileReader fileReader = new FileReader(
          new File(orientdbpath + "config/orientdb-server-config.xml"));
      BufferedReader bufferReader = new BufferedReader(fileReader);
      String inputString;
      StringBuilder strBuilder = new StringBuilder();
      while ((inputString = bufferReader.readLine()) != null) {
        strBuilder.append(inputString);
      }

      String orientdbHome = new File(orientdbpath).getAbsolutePath(); //Set OrientDB home to current directory
      System.setProperty("ORIENTDB_HOME", orientdbHome);

      OServer server = OServerMain.create();
      server.startup(strBuilder.toString());
      server.activate();
      RDBMSLogger.instance().info("============= OrientDb Embedded Server Successfully Connected ================");
    }
    catch (Throwable e) {
      RDBMSLogger.instance().exception(e);
    }
    return 1;
  }

}*/
