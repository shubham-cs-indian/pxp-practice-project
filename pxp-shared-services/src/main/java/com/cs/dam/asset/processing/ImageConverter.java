package com.cs.dam.asset.processing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.Info;
import org.im4java.core.InfoException;
import org.imgscalr.Scalr;

import com.cs.constants.DAMConstants;
import com.cs.constants.FileExtensionConstants;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.dam.asset.exceptions.ImageMagickException;

public class ImageConverter {
  
  private static final String PNG_EXCLUDE_CHUNK_DATE_TIME = "png:exclude-chunk=date,time";
  public static final String  RGB_COLORSPACE              = "sRGB";
  public static final String  HEIGHT                      = "height";  

  private ImageConverter()
  {
    // Private constructor
  }
  
  /**
   * This method is used to create thumbnails of image.
   * @param sourcePath
   * @return
   * @throws Exception
   */
  public static BufferedImage convertImage(String sourcePath, String thumbnailDirectoryPath) throws Exception
  {
    return ImageConverter.convertImage(sourcePath, DAMConstants.THUMBNAIL_HEIGHT,
        DAMConstants.THUMBNAIL_WIDTH, thumbnailDirectoryPath);
  }
  
  /**
   * This method changes resolution(in dpi) of image file.
   * @param sourcePath:
   *          String - source path of the image file
   * @param destinationPath:
   *          String - path for the converted file(changed resolution of source
   *          file)
   * @param resolution:
   *          Integer - resolution of converted file
   * @return byte[]: of the converted image file
   * @throws ImageMagickException
   * @throws IOException
   * @throws Exception
   */
  public static byte[] changeResolution(String sourcePath, String destinationPath,
      Integer resolution) throws ImageMagickException, IOException
  {
    ConvertCmd cmd = new ConvertCmd();
    IMOperation op = new IMOperation();
    op.define(PNG_EXCLUDE_CHUNK_DATE_TIME);
    op.addImage(sourcePath);
    op.resample(resolution);
    op.colorspace(RGB_COLORSPACE);
    op.flatten();
    op.addImage(destinationPath);
    byte[] bytes;
    try {
      cmd.run(op);
    }
    catch(IM4JavaException e) {
      throw new ImageMagickException(e);
    }
    catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ImageMagickException(e);
    }
    
    File temp = new File(destinationPath);
    FileInputStream targetStream = new FileInputStream(temp);
    bytes = IOUtils.toByteArray(targetStream);
    if (targetStream != null) {
      targetStream.close();
    }
    
    return bytes;
  }
  
  /**
   * This method convert 
   * @param sourcePath
   * @param imageHeight
   * @param imageWidth
   * @return
   * @throws RDBMSException 
   * @throws IOException 
   * @throws ImageMagickException 
   * @throws Exception
   */
  public static BufferedImage convertImage(String sourcePath, Integer imageHeight,
      Integer imageWidth, String thumbnailDirectoryPath) throws RDBMSException, IOException, ImageMagickException
  {
    String ext = null;
    if (sourcePath.contains(".")) {
      ext = sourcePath.substring(sourcePath.lastIndexOf(DAMConstants.DOT_SEPERATOR) + 1)
          .toLowerCase();
    }
    
    File temp = null;
    BufferedImage img = null;
    try {
      ConvertCmd cmd = new ConvertCmd();
      IMOperation op = new IMOperation();
      temp = new File(thumbnailDirectoryPath);
      String tempFilePath = temp.getAbsolutePath();
      if (ext != null) {
        switch (ext) {
          case FileExtensionConstants.JPEG:
          case FileExtensionConstants.JPG:
          case FileExtensionConstants.PNG:
            img = convertFromJpgPng(cmd, op, imageHeight, imageWidth, sourcePath, tempFilePath);
            break;
          
          case FileExtensionConstants.AI:
            img = ImageConverter.convertFromAi(cmd, op, imageHeight, imageWidth, sourcePath, tempFilePath);
            break;
          case FileExtensionConstants.PSD:
            img = ImageConverter.convertFromPsd(cmd, op, imageHeight, imageWidth, sourcePath, tempFilePath);
            break;
          case FileExtensionConstants.EPS:
            img = ImageConverter.convertFromEps(cmd, op, imageHeight, imageWidth, sourcePath, tempFilePath);
            break;
          case FileExtensionConstants.ICO:
            img = ImageConverter.convertFromIco(cmd, op, sourcePath, tempFilePath);
            break;
          
          case FileExtensionConstants.TIF:
          case FileExtensionConstants.TIFF:
          case FileExtensionConstants.GIF:
            img = ImageConverter.convertFromPsd(cmd, op, imageHeight, imageWidth, sourcePath, tempFilePath);
            break;
          
          default:
            img = convertFromJpgPng(cmd, op, imageHeight, imageWidth, sourcePath, tempFilePath);
            break;
        }
      }
    }
    finally {
    }
    return img;
  }
  
  /**
   * This method generates a thumbnail of a jpg file(using imgscalr library).
   *
   * @param sourcePath:
   *          String - source path of the video file
   * @return BufferedImage: of the generated thumbnail
   * @throws IOException
   * @throws Exception
   */
  public static BufferedImage convertImageImgscalr(String sourcePath) throws IOException
  {
    
    BufferedImage img = ImageIO.read(new File(sourcePath));
    if (img.getHeight() < 200 && img.getWidth() < 200) {
      
      return img;
    }
    
    return org.imgscalr.Scalr.resize(img, Scalr.Method.BALANCED, Scalr.Mode.AUTOMATIC, 200, 200,
        Scalr.OP_ANTIALIAS);
  }
  
  /**
   * This method is used to get dimensions of image.
   * @param sourcePath
   * @return
   * @throws InfoException
   * @throws Exception
   */
  public static Map<String, Integer> getImageDimensions(String sourcePath) throws Exception
  {
    Map<String, Integer> dimensionsMap = new HashMap<>();
    try {
      Info info = new Info(sourcePath, true);
      dimensionsMap.put(HEIGHT, info.getImageHeight());
      dimensionsMap.put("width", info.getImageWidth());
    }
    catch (Exception e) {
      throw new ImageMagickException(e);
    }
    return dimensionsMap;
  }
  
  /**
   * This method generates a thumbnail of a jpg file(using imagemagick).
   *
   * @param cmd:
   *          ConvertCmd
   * @param op:
   *          IMOperation
   * @param sourcePath:
   *          String - source path of the video file
   * @param tempFilePath:
   *          String - temporary path of the of the target file
   * @return BufferedImage: of the generated thumbnail
   * @throws Exception
   * @throws ImageMagickException
   * @throws IOException
   */
  private static BufferedImage convertFromJpgPng(ConvertCmd cmd, IMOperation op,
      Integer imageHeight, Integer imageWidth, String sourcePath, String tempFilePath)
      throws ImageMagickException, IOException
  {
    op.addImage(sourcePath);
    op.adaptiveResize(imageHeight, imageWidth, '>');
    op.addImage(tempFilePath);
    try {
      cmd.run(op);
    }
    catch (Exception e) {
      throw new ImageMagickException(e);
    }
    return ImageIO.read(new File(tempFilePath));
  }
  
  /**
   * This method generates a thumbnail of a pdf or ai file.
   *
   * @param cmd:
   *          ConvertCmd
   * @param op:
   *          IMOperation
   * @param sourcePath:
   *          String - source path of the video file
   * @param tempFilePath:
   *          String - temporary path of the of the target file
   * @return BufferedImage: of the generated thumbnail
   * @throws ImageMagickException
   * @throws IOException
   * @throws Exception
   */
  public static BufferedImage convertFromAi(ConvertCmd cmd, IMOperation op, Integer imageHeight,
      Integer imageWidth, String sourcePath, String tempFilePath)
      throws ImageMagickException, IOException
  {
    op.addImage(sourcePath);
    op.adaptiveResize(imageHeight, imageWidth, '>');
    op.addImage(tempFilePath);
    try {
      cmd.run(op);
    }
    catch (Exception e) {
      throw new ImageMagickException(e);
    }
    
    return ImageIO.read(new File(tempFilePath));
  }
  
  /**
   * This method generates a thumbnail of a psd file.
   *
   * @param cmd:
   *          ConvertCmd
   * @param op:
   *          IMOperation
   * @param sourcePath:
   *          String - source path of the video file
   * @param tempFilePath:
   *          String - temporary path of the of the target file
   * @return BufferedImage: of the generated thumbnail
   * @throws ImageMagickException
   * @throws IOException
   * @throws Exception
   */
  public static BufferedImage convertFromPsd(ConvertCmd cmd, IMOperation op, Integer imageHeight,
      Integer imageWidth, String sourcePath, String tempFilePath)
      throws ImageMagickException, IOException
  {
    op.addImage(sourcePath);
    op.flatten();
    op.adaptiveResize(imageHeight, imageWidth, '>');
    op.addImage(tempFilePath);
    try {
      cmd.run(op);
    }
    catch (Exception e) {
      throw new ImageMagickException(e);
    }
    
    return ImageIO.read(new File(tempFilePath));
  }
  
  /**
   * This method generates a thumbnail of a eps file.
   *
   * @param cmd:
   *          ConvertCmd
   * @param op:
   *          IMOperation
   * @param sourcePath:
   *          String - source path of the video file
   * @param tempFilePath:
   *          String - temporary path of the of the target file
   * @return BufferedImage: of the generated thumbnail
   * @throws ImageMagickException
   * @throws IOException
   * @throws Exception
   */
  public static BufferedImage convertFromEps(ConvertCmd cmd, IMOperation op, Integer imageHeight,
      Integer imageWidth, String sourcePath, String tempFilePath)
      throws ImageMagickException, IOException
  {
    op.addImage(sourcePath);
    op.adaptiveResize(imageHeight, imageWidth, '>');
    op.flatten();
    op.addImage(tempFilePath);
    try {
      cmd.run(op);
    }
    catch (Exception e) {
      throw new ImageMagickException(e);
    }
    
    return ImageIO.read(new File(tempFilePath));
  }
  
  /**
   * This method generates a thumbnail of a ico file.
   *
   * @param cmd:
   *          ConvertCmd
   * @param op:
   *          IMOperation
   * @param sourcePath:
   *          String - source path of the video file
   * @param tempFilePath:
   *          String - temporary path of the of the target file
   * @return BufferedImage: of the generated thumbnail
   * @throws ImageMagickException
   * @throws IOException
   * @throws Exception
   */
  public static BufferedImage convertFromIco(ConvertCmd cmd, IMOperation op, String sourcePath,
      String tempFilePath) throws ImageMagickException, IOException
  {
    op.addImage(sourcePath + "[0]");
    op.adaptiveResize(200, 200, '>');
    op.alpha("background");
    op.addImage(tempFilePath);
    try {
      cmd.run(op);
    }
    catch (Exception e) {
      throw new ImageMagickException(e);
    }
    
    return ImageIO.read(new File(tempFilePath));
  }
  
}
