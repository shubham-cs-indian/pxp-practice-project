/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates and open the template
 * in the editor.
 */
package com.cs.core.technical.asset;

import java.awt.image.BufferedImage;

public interface IVideoAssetConverter {

  /**
   * This method converts the given video file to mp4 format.
   *
   * @param sourcePath: String source path of the video file
   * @return byte[] of the converted video file
   * @throws Exception
   */
  public byte[] convertVideo(String sourcePath) throws Exception;

  /**
   * This method generates a png thumbnail of the 10th frame of the video.
   *
   * @param sourcePath: String - source path of the video file
   * @return BufferedImage: of the generated thumbnail
   * @throws Exception
   */
  public BufferedImage getVideoThumbnail(String sourcePath) throws Exception;

  /**
   * This method generates a png thumbnail of the video from the frameNo given in the input parameter
   *
   * @param sourcePath: String - source path of the video file
   * @param frameNo: Integer - frame no to be captured
   * @return BufferedImage: of the generated thumbnail
   * @throws Exception
   */
  public BufferedImage getVideoThumbnail(String sourcePath, int frameNo) throws Exception;

}
