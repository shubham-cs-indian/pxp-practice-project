package com.cs.core.config.interactor.utils.assetserver;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

package com.cs.config.interactor.usecase.assetserver.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

import javax.imageio.ImageIO;

//ffmpeg-cli-wrapper
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;


public class VideoExportLib {

    *//**
        * This method converts the given video file to mp4 format.
        *
        * @param sourcePath:
        *          String source path of the video file
        * @return byte[] of the converted video file
        * @throws Exception
        *           <p>
        *           This method generates a png thumbnail of the 10th frame of
        *           the video.
        * @param sourcePath:
        *          String - source path of the video file
        * @return BufferedImage: of the generated thumbnail
        * @throws Exception
        *           <p>
        *           This method generates a png thumbnail of the video from the
        *           frameNo given in the input parameter
        * @param sourcePath:
        *          String - source path of the video file
        * @param frameNo:
        *          Integer - frame no to be captured
        * @return BufferedImage: of the generated thumbnail
        * @throws Exception
        *           <p>
        *           This method generates a png thumbnail of the 10th frame of
        *           the video.
        * @param sourcePath:
        *          String - source path of the video file
        * @return BufferedImage: of the generated thumbnail
        * @throws Exception
        *           <p>
        *           This method generates a png thumbnail of the video from the
        *           frameNo given in the input parameter
        * @param sourcePath:
        *          String - source path of the video file
        * @param frameNo:
        *          Integer - frame no to be captured
        * @return BufferedImage: of the generated thumbnail
        * @throws Exception
        */
/*
            public static byte[] convertVideo (String sourcePath) throws Exception {
                FFmpeg ffmpeg = new FFmpeg();
                FFprobe ffprobe = new FFprobe();
                File temp = File.createTempFile("converted", ".mp4");
                String tempFilePath = temp.getAbsolutePath();
                FFmpegBuilder builder1 = new FFmpegBuilder()
                    .setInput(sourcePath)
                    .addOutput(tempFilePath)
                    .addExtraArgs("-crf", "20")
                    .setFormat("mp4").done();

                FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
                executor.createJob(builder1).run();
                File file = new File(tempFilePath);
                FileInputStream fin = new FileInputStream(file);
                byte b[] = new byte[(int)file.length()];
                fin.read(b);
                return b;
            }

            *//**
                * This method generates a png thumbnail of the 10th frame of the
                * video.
                *
                * @param sourcePath:
                *          String - source path of the video file
                * @return BufferedImage: of the generated thumbnail
                * @throws Exception
                */
/*
                                                                public static BufferedImage getVideoThumbnail (String sourcePath)throws Exception {

                                                                    return this.getVideoThumbnail(sourcePath, 10);
                                                                }

                                                                *//**
                                                                    * This
                                                                    * method
                                                                    * generates
                                                                    * a png
                                                                    * thumbnail
                                                                    * of the
                                                                    * video from
                                                                    * the
                                                                    * frameNo
                                                                    * given in
                                                                    * the input
                                                                    * parameter
                                                                    *
                                                                    * @param sourcePath:
                                                                    *          String
                                                                    *          -
                                                                    *          source
                                                                    *          path
                                                                    *          of
                                                                    *          the
                                                                    *          video
                                                                    *          file
                                                                    * @param frameNo:
                                                                    *          Integer
                                                                    *          -
                                                                    *          frame
                                                                    *          no
                                                                    *          to
                                                                    *          be
                                                                    *          captured
                                                                    * @return BufferedImage:
                                                                    *         of
                                                                    *         the
                                                                    *         generated
                                                                    *         thumbnail
                                                                    * @throws Exception
                                                                    *//*
                                                                                                                                                                                                                                                                                                                                                          public static BufferedImage getVideoThumbnail (String sourcePath, int frameNo)throws Exception {
                                                                                                                                                                                                                                                                                                                                                              FFmpeg ffmpeg = new FFmpeg();
                                                                                                                                                                                                                                                                                                                                                              FFprobe ffprobe = new FFprobe();
                                                                                                                                                                                                                                                                                                                                                              File temp = File.createTempFile("converted", ".png");
                                                                                                                                                                                                                                                                                                                                                              String tempFilePath = temp.getAbsolutePath();
                                                                                                                                                                                                                                                                                                                                                              FFmpegBuilder builder = new FFmpegBuilder()
                                                                                                                                                                                                                                                                                                                                                                  .setInput(sourcePath)
                                                                                                                                                                                                                                                                                                                                                                  .addOutput(tempFilePath)
                                                                                                                                                                                                                                                                                                                                                                  .setFrames(1)
                                                                                                                                                                                                                                                                                                                                                                  .setVideoFilter("select='gte(n\\," + frameNo + ")',scale=200:-1")
                                                                                                                                                                                                                                                                                                                                                                  .done();
                                                                      
                                                                                                                                                                                                                                                                                                                                                              FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
                                                                                                                                                                                                                                                                                                                                                              executor.createJob(builder).run();
                                                                                                                                                                                                                                                                                                                                                              return ImageIO.read(new File(tempFilePath));
                                                                      
                                                                                                                                                                                                                                                                                                                                                          }
                                                                      
                                                                      
                                                                      
                                                                      
                                                                                                                                                                                                                                                                                                                                                      }
                                                                                                                                                                                                                                                                                                                                                      */
