package com.cs.dam.asset.processing;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.DAMConstants;
import com.cs.constants.FileExtensionConstants;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.dam.asset.exceptions.FFMPEGException;
import com.cs.utils.dam.AssetUtils;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

/**
 * This is utility class for video asset conversions.
 * @author pranav.huchche
 *
 */
public class VideoAssetConverter {
  
  private VideoAssetConverter () 
  {
    // Private Constructor.
  }

  /**
   * This method converts the given video file to mp4 format.
   *
   * @param sourcePath: String source path of the video file
   * @return byte[] of the converted video file
   * @throws FFMPEGException 
   * @throws IOException 
   * @throws Exception
   */
  public static byte[] convertVideo(String sourcePath) throws FFMPEGException, IOException 
  {
    FFmpeg ffmpeg = new FFmpeg();
    FFprobe ffprobe = new FFprobe();
    FileInputStream fin = null;
    String tempFilePath = null;
    File temp = null;
    try
    {
      temp = new File(AssetUtils.getFilePath() + DAMConstants.TEMP_CONVERTED_FILE_NAME
          + RDBMSAppDriverManager.getDriver()
              .newUniqueID(IStandardConfig.UniquePrefix.RANDOM.getPrefix())
          + FileExtensionConstants.MP4_EXT);
      temp.createNewFile();
      tempFilePath = temp.getAbsolutePath();
      FFmpegBuilder builder1 = new FFmpegBuilder().setInput(sourcePath)
          .addOutput(tempFilePath)
          .addExtraArgs("-crf", "20")
          .setFormat(FileExtensionConstants.MP4)
          .done();
      
      FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
      executor.createJob(builder1)
          .run();
      File file = new File(tempFilePath);
      fin = new FileInputStream(file);
      byte b[] = new byte[(int) file.length()];
      fin.read(b);
      return b;
    }
    catch (Exception e) {
      throw new FFMPEGException(e);
    }
    finally {
      if (fin != null) {
        fin.close();
      }
      if (tempFilePath != null) {
        AssetUtils.deleteFileAndDirectory(temp);
      }
    }
  }

  /**
   * This method generates a png thumbnail of the 10th frame of the video.
   *
   * @param sourcePath: String - source path of the video file
   * @return BufferedImage: of the generated thumbnail
   * @throws Exception
   */
  public static BufferedImage getVideoThumbnail(String sourcePath) throws FFMPEGException 
  {

    return VideoAssetConverter.getVideoThumbnail(sourcePath, 10);
  }

  /**
   * This method generates a png thumbnail of the video from the frameNo given in the input parameter
   *
   * @param sourcePath: String - source path of the video file
   * @param frameNo: Integer - frame no to be captured
   * @return BufferedImage: of the generated thumbnail
   * @throws FFMPEGException 
   * @throws Exception
   */
  public static BufferedImage getVideoThumbnail(String sourcePath, int frameNo) throws FFMPEGException 
  {
    File temp = null;
    String tempFilePath = null;
    try {
      FFmpeg ffmpeg = new FFmpeg();
      FFprobe ffprobe = new FFprobe();
      temp = new File(AssetUtils.getFilePath() + DAMConstants.TEMP_CONVERTED_FILE_NAME
          + RDBMSAppDriverManager.getDriver()
              .newUniqueID(IStandardConfig.UniquePrefix.RANDOM.getPrefix())
          + FileExtensionConstants.PNG_EXT);
      temp.createNewFile();
      tempFilePath = temp.getAbsolutePath();
      FFmpegBuilder builder = new FFmpegBuilder().setInput(sourcePath)
              .addOutput(tempFilePath)
              .setFrames(1)
              .setVideoFilter("select='gte(n\\," + frameNo + ")',scale=200:-1")
              .done();

      FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
      executor.createJob(builder)
              .run();

      BufferedImage image = ImageIO.read(new File(tempFilePath));
      if (image == null) {
        throw new FFMPEGException("FFMPEG Exception");
      }
      return image;
    }
    catch (Exception e) {
      throw new FFMPEGException(e);
    }
    finally {
      if (tempFilePath != null) {
        AssetUtils.deleteFileAndDirectory(temp);
      }
    }
  }
}
