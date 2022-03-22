/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates and open the template
 * in the editor.
 */
package com.cs.core.technical.asset;

import java.awt.image.BufferedImage;
import java.util.Map;

public interface IAssetConverter {

  /**
   * This method generated a thumbnail from an image file (jpg, png, ico, psd, ai, eps). If the size of the image is more than 200x200, it
   * scales it down to 200x200 pixels, else it only converts the image to a png file;
   *
   * @param sourcePath: String - source path of the Image file
   * @return BufferedImage: of the generated thumbnail
   * @throws Exception
   */
  public BufferedImage convertImage(String sourcePath) throws Exception;

  /**
   * This method generated a thumbnail from an image file (jpg, png, ico, psd, ai, eps). If the size of the image is more than 200x200, it
   * scales it down to given pixels, else it only converts the image to a png file;
   *
   * @param sourcePath - source path of the Image file
   * @param imageHeight - required height of buffered image.
   * @param imageWidth - required width of buffered image.
   * @return BufferedImage: of the generated thumbnail
   * @throws Exception
   */
  public BufferedImage convertImage(String sourcePath, Integer imageHeight, Integer imageWidth)
          throws Exception;

  /**
   * This method generates a thumbnail of a pdf file.
   *
   * @param sourcePath: String - source path of the doucment file
   * @return BufferedImage: of the generated thumbnail
   * @throws Exception
   */
  public BufferedImage convertPdf(String sourcePath) throws Exception;

  /**
   * This method generates a thumbnail of a pdf file.
   *
   * @param sourcePath: String - source path of the doucment file
   * @return BufferedImage: of the generated thumbnail
   * @throws Exception
   */
  public BufferedImage convertPdf(String sourcePath, Integer imageHeight, Integer imageWidth)
          throws Exception;

  /**
   * This method generates a thumbnail of a pdf file.
   *
   * @param sourcePath: String - source path of the video file
   * @return BufferedImage: of the generated thumbnail
   * @throws Exception
   */
  public String convertDocument(String sourcePath) throws Exception;

  /**
   * This method generates a thumbnail of a jpg file(using imgscalr library).
   *
   * @param sourcePath: String - source path of the video file
   * @return BufferedImage: of the generated thumbnail
   * @throws Exception
   */
  public BufferedImage convertImageImgscalr(String sourcePath) throws Exception;

  /**
   * This method returns a map which contains image dimensions(Height, Width)
   *
   * @param sourcePath - sourcePathOfFile
   * @param imageHeight - required height of buffered image.
   * @param imageWidth - required width of buffered image.
   * @return BufferedImage: of the generated thumbnail
   * @throws Exception
   */
  public Map<String, Integer> getImageDimensions(String sourcePath) throws Exception;

  /**
   * This method generated a new image file (jpg, png, ico, psd, ai, eps) at the given destination path;
   *
   * @param sorucePath - source path of the given resource.
   * @param destinationPath - destination path of the given resource.
   * @return byte array of the target asset that has been created.
   * @throws Exception
   */
  public byte[] changeResolution(String sourcePath, String destinationPath, Integer resolution)
          throws Exception;

}
