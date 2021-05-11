package ru.sfedu.projectpuskaFinalVersion.api;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.*;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import ru.sfedu.projectpuskaFinalVersion.Constants;
import ru.sfedu.projectpuskaFinalVersion.Main;
import static ru.sfedu.projectpuskaFinalVersion.utils.ConfigurationUtil.getConfigurationEntry;

public class ImageAPI {
    private  static final Logger logger = LogManager.getLogger(Main.class);

    public ImageAPI() throws Exception {
        logger.info("Checking OS.....");
        // init the API with curent os..
        Constants.OSType CurrentOSTypeName = getOperatingSystemType();
        logger.info("Current OS type name - " + CurrentOSTypeName);
        switch (CurrentOSTypeName) {
            case LINUX:
                System.load(getConfigurationEntry(Constants.PATH_TO_NATIVE_LIB_LINUX));
                break;
            case WINDOWS:
                System.load(getConfigurationEntry(Constants.PATH_TO_NATIVE_LIB_WINDOWS));
                break;
            case MACOS:
                System.load(getConfigurationEntry(Constants.PATH_TO_NATIVE_LIB_MAC_OS));
            case OTHER:
                throw new Exception("Current OS does not support!!!!!");
            default:
                throw new Exception("Your OS does not support!!!");
        }
        logger.info("OpenCV version" + Core.getVersionString());
    }

    public Constants.OSType getOperatingSystemType() {
        String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        if (OS.contains("mac") || OS.contains("darwin")) {
            return Constants.OSType.MACOS;
        } else if (OS.contains("win")) {
            return Constants.OSType.WINDOWS;
        } else if (OS.contains("nux")) {
            return Constants.OSType.LINUX;
        } else {
            return Constants.OSType.OTHER;
        }
    }

    public Mat loadImage (String absolutePath){ return Imgcodecs.imread(absolutePath);
    }

    public void showImage(Mat mat){
        HighGui.imshow(String.valueOf(System.nanoTime()),mat);
        HighGui.waitKey();
    }

    public Mat convertIntoBlackChannel(int channel, Mat mat){
        int totalBytes = (int) (mat.total() * mat.elemSize());
        byte[] buffer = new byte[totalBytes];
        mat.get(0, 0, buffer);
        for (int i = channel; i < totalBytes; i += mat.elemSize()){
            buffer[i] = 0;
        }
        mat.put(0, 0, buffer);
        return mat;
    }

    public void saveMatImageOnDisk(Mat image, String path){
        Imgcodecs.imwrite(path, image);
    }


    public Mat convertSobel(Mat image, int dx, int dy) {
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
        Mat dstSobel = new Mat();
        Imgproc.Sobel(grayImage, dstSobel, CvType.CV_32F, dx, dy);
        Core.convertScaleAbs(dstSobel, dstSobel);
        return dstSobel;
    }


    public Mat convertLaplace(Mat image, int kSize) {
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
        Mat dstLaplace = new Mat();
        Imgproc.Laplacian(grayImage, dstLaplace, kSize);
        Core.convertScaleAbs(dstLaplace, dstLaplace);
        return dstLaplace;
    }

    public Mat mirrorImage(Mat image, int flipCode) {
        Mat dstV = new Mat();
        Core.flip(image, dstV, flipCode);
        return dstV;
    }


//    public List<Mat> unionImage(List<Mat> matList, Mat dst, boolean isVertical) {
//        if (isVertical) {
//            Core.vconcat(matList, dst);
//        } else {
//            Core.hconcat(matList, dst);
//        }
//        return matList;
//    }


    public Mat repeatImage(Mat image, int ny, int nx) {
        Mat rotationImage = new Mat();
        Core.repeat(image, ny, nx, rotationImage);
        return rotationImage;
    }


    public Mat resizeImage(Mat image, int width, int height) {
        Mat resizeImage = new Mat();
        Imgproc.resize(image, resizeImage, new Size(width, height));
        return resizeImage;
    }


    public Mat geometryChangeImage(Mat image) {
        Point center = new Point(image.width() >> 1, image.height() >> 1);
        Mat rotationMat = Imgproc.getRotationMatrix2D(center, 45, 1);

        Mat dst = new Mat();
        Imgproc.warpAffine(image, dst, rotationMat,new Size(image.width(), image.height()),
                Imgproc.INTER_LINEAR, Core.BORDER_TRANSPARENT,new Scalar(0,0,0,255));

        return dst;
    }

    public Mat unionImage(Mat mat, Mat dst) {
        Core.addWeighted(mat, 0.5, dst, 0.5, 0.0, mat);
        return mat;
    }
//    4
//    public Mat morph() {
//        Mat defaultMat = Imgcodecs.imread(showImage);
//
//    }

}
