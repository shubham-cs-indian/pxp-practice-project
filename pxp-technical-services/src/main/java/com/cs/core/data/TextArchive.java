package com.cs.core.data;

import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * String serialization/deserialization as used to store some elaborated information like revision contents File archiving and hashing
 * functions
 * <p>
 * source: https://stackoverflow.com/questions/134492/how-to-serialize-an-object-into-a-string
 *
 * @author vallee
 */
public class TextArchive {

  private static final int BUFFER_SIZE = 64;

  /**
   * Serialize an object into a Base64 encoded String
   *
   * @param object
   * @return the serialization String result
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public static String convertToString(final Serializable object) throws RDBMSException {
    try (final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(object);
      return Base64.getEncoder().encodeToString(baos.toByteArray());
    } catch (final IOException e) {
      throw new RDBMSException(1010, "Serialization", e);
    }
  }

  /**
   * Deserialize an object of type T from a String
   *
   * @param <T> the related type of object
   * @param objectAsString the input Stream
   * @return the object
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public static <T extends Serializable> T convertFrom(final String objectAsString) throws RDBMSException {
    final byte[] data = Base64.getDecoder().decode(objectAsString);
    try (final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
      return (T) ois.readObject();
    } catch (final IOException | ClassNotFoundException e) {
      throw new RDBMSException(1010, "Serialization", e);
    }
  }

  /**
   * @param text a regular String
   * @return its encoded Base64 version
   */
  public static String toBase64(String text) {
    return Base64.getEncoder().encodeToString(text.getBytes());
  }

  /**
   * @param encodedString a base64 encoded String
   * @return its decoded content
   */
  public static String fromBase64(String encodedString) {
    return new String(Base64.getDecoder().decode(encodedString));
  }

  /**
   * @param text any String content to be compressed
   * @return the GZIP result of compression
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public static byte[] zip(String text) throws CSFormatException {
    if (text.isEmpty()) {
      return null;
    }
    return zip(text.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * @param content
   * @return the GZIP result of compression
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public static byte[] zip(byte[] content) throws CSFormatException {
    if (content == null || content.length == 0) {
      return (new byte[0]);
    }
    try (ByteArrayOutputStream strStream = new ByteArrayOutputStream(content.length);
            GZIPOutputStream gzip = new GZIPOutputStream(strStream)) {
      gzip.write(content);
      gzip.flush();
      gzip.close();
      return strStream.toByteArray();
    } catch (IOException ex) {
      throw new CSFormatException("ZIP error", ex);
    }
  }

  /**
   * @param zipped any zip compressed content
   * @return the unzipped String content
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public static String unzip(byte[] zipped) throws CSFormatException
  {
    if (zipped == null || zipped.length == 0) {
      return "";
    }
    StringBuilder strBuffer = new StringBuilder();
    try (ByteArrayInputStream strStream = new ByteArrayInputStream(zipped);
        GZIPInputStream gis = new GZIPInputStream(strStream, BUFFER_SIZE);
        InputStreamReader inputStreamReader = new InputStreamReader(gis, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        strBuffer.append(line);
      }
    }
    catch (IOException ex) {
      throw new CSFormatException("UNZIP error", ex);
    }
    return strBuffer.toString();
  }

  /**
   * @param text any String content to be archived
   * @return the GZIP + Base64 archived content
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public static byte[] toArchive(String text) throws CSFormatException {
    return zip(toBase64(text));
  }

  /**
   * @param archive any archived contend
   * @return the Base64 + GZIP decoded result
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public static String fromArchive(byte[] archive) throws CSFormatException {
    return fromBase64(unzip(archive));
  }

}
