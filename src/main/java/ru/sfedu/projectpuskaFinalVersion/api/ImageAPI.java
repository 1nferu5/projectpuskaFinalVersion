package ru.sfedu.projectpuskaFinalVersion.api;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.util.*;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import ru.sfedu.projectpuskaFinalVersion.Constants;
import ru.sfedu.projectpuskaFinalVersion.Main;
import ru.sfedu.projectpuskaFinalVersion.utils.ConfigurationUtil;

import static org.opencv.core.CvType.CV_8UC3;
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

    public Mat task4ToWarp() throws IOException {
        Mat defaultMat = Imgcodecs.imread(ConfigurationUtil.getConfigurationEntry("lab4.defoult.img1"));

        Mat transMat = new Mat(2, 3, CvType.CV_64FC1);
        MatOfPoint2f src = new MatOfPoint2f(
                new Point(0, 0),
                new Point(defaultMat.cols(), 0),
                new Point(0, defaultMat.rows()),
                new Point(defaultMat.cols(), defaultMat.rows())
        );
        int x = 50;
        int y = 50;
        MatOfPoint2f target = new MatOfPoint2f(
                new Point(x, y),
                new Point(defaultMat.cols() - x, 0),
                new Point(0, defaultMat.rows() - y),
                new Point(defaultMat.cols() - x, defaultMat.rows() - y)
        );

        Mat matWarp = Imgproc.getPerspectiveTransform(src, target);
        Mat res = new Mat();
        Imgproc.warpPerspective(defaultMat, res, matWarp, defaultMat.size());

        return  res;
    }

    public Mat task5ToFill(Integer initVal) throws IOException {

        Mat defaultMat = Imgcodecs.imread(ConfigurationUtil.getConfigurationEntry("lab4.defoult.img1"));

        // координаты точки начала анализа параметров цвета заданного изображения
        Point seedPoint = new Point(0,0);
        // цвет заливки (зеленый)
        Scalar newVal = new Scalar(0,255,0);
        Scalar loDiff = new Scalar(initVal,initVal,initVal);
        Scalar upDiff = new Scalar(initVal,initVal,initVal);
        Mat mask = new Mat();

        Imgproc.floodFill(defaultMat, mask, seedPoint, newVal, new Rect(), loDiff, upDiff,
                Imgproc.FLOODFILL_FIXED_RANGE + 8);
        return defaultMat;
    }

    public Mat getNoise(){
        Mat mat = new Mat(new Size(1000, 600), CV_8UC3, new Scalar(0, 0, 0));
        Core.randn(mat, 20, 50);
        Core.add(mat, mat, mat);
        return mat;
    }

    public Mat pyramid() throws IOException{
        Mat defaultMat = getNoise();

        Mat mask = new Mat();

        Imgproc.pyrDown(defaultMat, mask);
        showImage(mask);

        Imgproc.pyrUp(mask, mask);
        showImage(mask);

        Core.subtract(defaultMat, mask, mask);
        showImage(mask);

        return mask;
    }
}


