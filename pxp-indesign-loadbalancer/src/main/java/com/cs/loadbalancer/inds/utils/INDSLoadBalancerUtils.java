package com.cs.loadbalancer.inds.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class INDSLoadBalancerUtils {
  
  /**
   * The Windows separator character.
   */
  private static final char WINDOWS_SEPARATOR = '\\';

  /**
   * The system separator character.
   */
  private static final char SYSTEM_SEPARATOR = File.separatorChar;

	/**
	 * Converts the HTTP request content into a string. 
	 */
	public static String getContentInStringFromRequest(HttpServletRequest request) throws IOException {
		BufferedReader bufferedReader = request.getReader();
		Boolean bufferIsNotEmpty = true;
		String content = "";

		while (bufferIsNotEmpty) {
			String line = bufferedReader.readLine();
			if (line == null) {
				bufferIsNotEmpty = false;
				continue;
			}
			content += line;
		}

		return content;
	}
	
	/**
	 * Sends back an error response alongwith the exception message. 
	 * Must always be used when there is a need to send back failure response.
	 */
	public static void sendErrorResponse(AsyncContext asyncContext, Exception ex) {
		HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
		long time = System.currentTimeMillis();
		response.setDateHeader("Date", time);
		response.setDateHeader("Last-Modified", time);
	
		try {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

		ex.printStackTrace();
		asyncContext.complete(); //async context needs to be completed to send back the response.
	}
	
  /**
   * Copied from org.apache.commons.io.FileUtils
   * Writes a byte array to a file creating the file if it does not exist.
   *
   * @param file  the file to write to
   * @param data  the content to write to the file
   * @param append if {@code true}, then bytes will be added to the
   * end of the file rather than overwriting
   * @throws IOException in case of an I/O error
   * @since IO 2.1
   */
  public static void writeByteArrayToFile(File file, byte[] data, boolean append) throws IOException {
      OutputStream out = null;
      try {
          out = openOutputStream(file, append);
          out.write(data);
          out.close(); // don't swallow close Exception if copy completes normally
      } finally {
          closeQuietly(out);
      }
  }
  
  /**
   * Copied from org.apache.commons.io.FileUtils
   * Unconditionally close an <code>OutputStream</code>.
   * <p>
   * Equivalent to {@link OutputStream#close()}, except any exceptions will be ignored.
   * This is typically used in finally blocks.
   * <p>
   * Example code:
   * <pre>
   * byte[] data = "Hello, World".getBytes();
   *
   * OutputStream out = null;
   * try {
   *     out = new FileOutputStream("foo.txt");
   *     out.write(data);
   *     out.close(); //close errors are handled
   * } catch (IOException e) {
   *     // error handling
   * } finally {
   *     IOUtils.closeQuietly(out);
   * }
   * </pre>
   *
   * @param output  the OutputStream to close, may be null or already closed
   */
  public static void closeQuietly(OutputStream output) {
      closeQuietly((Closeable)output);
  }
  
  /**
   * Copied from org.apache.commons.io.FileUtils
   * Unconditionally close a <code>Closeable</code>.
   * <p>
   * Equivalent to {@link Closeable#close()}, except any exceptions will be ignored.
   * This is typically used in finally blocks.
   * <p>
   * Example code:
   * <pre>
   *   Closeable closeable = null;
   *   try {
   *       closeable = new FileReader("foo.txt");
   *       // process closeable
   *       closeable.close();
   *   } catch (Exception e) {
   *       // error handling
   *   } finally {
   *       IOUtils.closeQuietly(closeable);
   *   }
   * </pre>
   *
   * @param closeable the object to close, may be null or already closed
   * @since 2.0
   */
  public static void closeQuietly(Closeable closeable) {
      try {
          if (closeable != null) {
              closeable.close();
          }
      } catch (IOException ioe) {
          // ignore
      }
  }
  
  /**
   * Copied from org.apache.commons.io.FileUtils
   * Opens a {@link FileOutputStream} for the specified file, checking and
   * creating the parent directory if it does not exist.
   * <p>
   * At the end of the method either the stream will be successfully opened,
   * or an exception will have been thrown.
   * <p>
   * The parent directory will be created if it does not exist.
   * The file will be created if it does not exist.
   * An exception is thrown if the file object exists but is a directory.
   * An exception is thrown if the file exists but cannot be written to.
   * An exception is thrown if the parent directory cannot be created.
   * 
   * @param file  the file to open for output, must not be {@code null}
   * @param append if {@code true}, then bytes will be added to the
   * end of the file rather than overwriting
   * @return a new {@link FileOutputStream} for the specified file
   * @throws IOException if the file object is a directory
   * @throws IOException if the file cannot be written to
   * @throws IOException if a parent directory needs creating but that fails
   * @since 2.1
   */
  public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
      if (file.exists()) {
          if (file.isDirectory()) {
              throw new IOException("File '" + file + "' exists but is a directory");
          }
          if (file.canWrite() == false) {
              throw new IOException("File '" + file + "' cannot be written to");
          }
      } else {
          File parent = file.getParentFile();
          if (parent != null) {
              if (!parent.mkdirs() && !parent.isDirectory()) {
                  throw new IOException("Directory '" + parent + "' could not be created");
              }
          }
      }
      return new FileOutputStream(file, append);
  }
  
  /**
   * Copied from org.apache.commons.lang3.StringUtils
   * <p>Checks if the CharSequence contains only Unicode digits.
   * A decimal point is not a Unicode digit and returns false.</p>
   *
   * <p>{@code null} will return {@code false}.
   * An empty CharSequence (length()=0) will return {@code false}.</p>
   *
   * <p>Note that the method does not allow for a leading sign, either positive or negative.
   * Also, if a String passes the numeric test, it may still generate a NumberFormatException
   * when parsed by Integer.parseInt or Long.parseLong, e.g. if the value is outside the range
   * for int or long respectively.</p>
   *
   * <pre>
   * StringUtils.isNumeric(null)   = false
   * StringUtils.isNumeric("")     = false
   * StringUtils.isNumeric("  ")   = false
   * StringUtils.isNumeric("123")  = true
   * StringUtils.isNumeric("\u0967\u0968\u0969")  = true
   * StringUtils.isNumeric("12 3") = false
   * StringUtils.isNumeric("ab2c") = false
   * StringUtils.isNumeric("12-3") = false
   * StringUtils.isNumeric("12.3") = false
   * StringUtils.isNumeric("-123") = false
   * StringUtils.isNumeric("+123") = false
   * </pre>
   *
   * @param cs  the CharSequence to check, may be null
   * @return {@code true} if only contains digits, and is non-null
   * @since 3.0 Changed signature from isNumeric(String) to isNumeric(CharSequence)
   * @since 3.0 Changed "" to return false and not true
   */
  public static boolean isNumeric(final CharSequence cs) {
      if (cs == null || cs.length() == 0) {
          return false;
      }
      final int sz = cs.length();
      for (int i = 0; i < sz; i++) {
          if (!Character.isDigit(cs.charAt(i))) {
              return false;
          }
      }
      return true;
  }
  
  /**
   * Copied from org.apache.commons.io.FileUtils
   * Deletes a file. If file is a directory, delete it and all sub-directories.
   * <p>
   * The difference between File.delete() and this method are:
   * <ul>
   * <li>A directory to be deleted does not have to be empty.</li>
   * <li>You get exceptions when a file or directory cannot be deleted.
   *      (java.io.File methods returns a boolean)</li>
   * </ul>
   *
   * @param file  file or directory to delete, must not be {@code null}
   * @throws NullPointerException if the directory is {@code null}
   * @throws FileNotFoundException if the file was not found
   * @throws IOException in case deletion is unsuccessful
   */
  public static void forceDelete(File file) throws IOException {
      if (file.isDirectory()) {
          deleteDirectory(file);
      } else {
          boolean filePresent = file.exists();
          if (!file.delete()) {
              if (!filePresent){
                  throw new FileNotFoundException("File does not exist: " + file);
              }
              String message =
                  "Unable to delete file: " + file;
              throw new IOException(message);
          }
      }
  }
  
  /**
   * Copied from org.apache.commons.io.FileUtils
   * Deletes a directory recursively. 
   *
   * @param directory  directory to delete
   * @throws IOException in case deletion is unsuccessful
   */
  public static void deleteDirectory(File directory) throws IOException {
      if (!directory.exists()) {
          return;
      }

      if (!isSymlink(directory)) {
          cleanDirectory(directory);
      }

      if (!directory.delete()) {
          String message =
              "Unable to delete directory " + directory + ".";
          throw new IOException(message);
      }
  }
  
  /**
   * Copied from org.apache.commons.io.FileUtils
   * Determines whether the specified file is a Symbolic Link rather than an actual file.
   * <p>
   * Will not return true if there is a Symbolic Link anywhere in the path,
   * only if the specific file is.
   * <p>
   * <b>Note:</b> the current implementation always returns {@code false} if the system
   * is detected as Windows using {@link FilenameUtils#isSystemWindows()}
   * 
   * @param file the file to check
   * @return true if the file is a Symbolic Link
   * @throws IOException if an IO error occurs while checking the file
   * @since 2.0
   */
  public static boolean isSymlink(File file) throws IOException {
      if (file == null) {
          throw new NullPointerException("File must not be null");
      }
      if (isSystemWindows()) {
          return false;
      }
      File fileInCanonicalDir = null;
      if (file.getParent() == null) {
          fileInCanonicalDir = file;
      } else {
          File canonicalDir = file.getParentFile().getCanonicalFile();
          fileInCanonicalDir = new File(canonicalDir, file.getName());
      }
      
      if (fileInCanonicalDir.getCanonicalFile().equals(fileInCanonicalDir.getAbsoluteFile())) {
          return false;
      } else {
          return true;
      }
  }
  
  /**
   * Copied from org.apache.commons.io.FileUtils
   * Determines if Windows file system is in use.
   * 
   * @return true if the system is Windows
   */
  static boolean isSystemWindows() {
      return SYSTEM_SEPARATOR == WINDOWS_SEPARATOR;
  }
  
  /**
   * Copied from org.apache.commons.io.FileUtils
   * Cleans a directory without deleting it.
   *
   * @param directory directory to clean
   * @throws IOException in case cleaning is unsuccessful
   */
  public static void cleanDirectory(File directory) throws IOException {
      if (!directory.exists()) {
          String message = directory + " does not exist";
          throw new IllegalArgumentException(message);
      }

      if (!directory.isDirectory()) {
          String message = directory + " is not a directory";
          throw new IllegalArgumentException(message);
      }

      File[] files = directory.listFiles();
      if (files == null) {  // null if security restricted
          throw new IOException("Failed to list contents of " + directory);
      }

      IOException exception = null;
      for (File file : files) {
          try {
              forceDelete(file);
          } catch (IOException ioe) {
              exception = ioe;
          }
      }

      if (null != exception) {
          throw exception;
      }
  }
  
}
