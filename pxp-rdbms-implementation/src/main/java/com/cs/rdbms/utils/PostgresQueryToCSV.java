package com.cs.rdbms.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

public class PostgresQueryToCSV {
  
  
  /**
   * This function takes postgres query as an input and returns result of query
   * (in CSV format) into a byte array.
   * 
   * @param con Connection object
   * @param query a postgresql query
   * @return byte array of csv file
   * @throws SQLException
   * @throws IOException
   */
  public static byte[] getCSVBytesOfQueryResult(Connection con,
      String query) throws SQLException, IOException
  {
    PGConnection pgConnection = con.unwrap(PGConnection.class);
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    CopyManager cm = new CopyManager((BaseConnection) pgConnection);
    cm.copyOut("COPY ("+query+") TO STDOUT "
        + "WITH (FORMAT CSV, HEADER TRUE, DELIMITER ';', QUOTE '\"', FORCE_QUOTE *)", os);
    byte[] osBytes = os.toByteArray();
    return osBytes;
  }
  
}
