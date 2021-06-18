package ru.sfedu.projectpuskaFinalVersion.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import ru.sfedu.projectpuskaFinalVersion.Constants;
import ru.sfedu.projectpuskaFinalVersion.Main;
import ru.sfedu.projectpuskaFinalVersion.utils.ConfigurationUtil;


import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.opencv.core.CvType.CV_8UC3;
import static ru.sfedu.projectpuskaFinalVersion.Constants.CFG_KEY;

public class ImageAPITest {
    private  static final Logger logger = LogManager.getLogger(Main.class);

    public static final String TEST_IMG="/home/hp/test_java/blm.jpg";


    @Before
    public void setUp() throws Exception {
        System.setProperty(CFG_KEY, "src/test/resources/config1.properties");
    }

    @Test
    public void testCreateTempFile() throws Exception {

        try {
            ImageAPI img = new ImageAPI();
//           Assert.fail("Expected IOException");
        } catch (Exception thrown) {
            Assert.assertNotEquals("", thrown.getMessage());
        }

    }
//    public void test() throws  Exception {
//        try {
//            ImageAPI img = new ImageAPI();
//            logger.debug("Instance created successfully");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public Mat getNoise() {
        Mat mat = new Mat(new Size(800, 800), CV_8UC3, new Scalar(0, 0, 0));
        Core.randn(mat, 20, 50);
        Core.add(mat, mat, mat);
        return mat;
    }
    @Test
    public void convertIntoBlackByChannel() throws Exception {
        ImageAPI api = new ImageAPI();

        Mat defaultMat = api.loadImage(ConfigurationUtil.getConfigurationEntry("lab4.defoult.img1"));
        api.showImage(defaultMat);
        Mat rmBlue = api.convertIntoBlackChannel(0,defaultMat);
        api.showImage(rmBlue);
        Mat rmGreen = api.convertIntoBlackChannel(1,rmBlue);
        api.showImage(rmGreen);
        Mat rmRed = api.convertIntoBlackChannel(2,rmGreen);
        api.showImage(rmRed);
        api.saveMatImageOnDisk(rmRed,TEST_IMG);

        List<Mat> blueGreenRed = new ArrayList<>();
        Core.split(rmRed, blueGreenRed);

        for (int i = 0; i <rmRed.elemSize(); ++i){
            assertEquals(Core.countNonZero(blueGreenRed.get(i)),0);
        }

    }
    @Test
    public void sobel() throws Exception {
        ImageAPI api = new ImageAPI();

        Mat defaultMat = api.loadImage(ConfigurationUtil.getConfigurationEntry("lab4.defoult.img1"));

        api.showImage(defaultMat);

        Mat sobelImage = api.convertSobel(defaultMat, 1, 0);
        api.showImage(sobelImage);
    }
    @Test
    public void Laplace() throws Exception {
        ImageAPI api = new ImageAPI();

        Mat defaultMat = api.loadImage(ConfigurationUtil.getConfigurationEntry("lab4.defoult.img2"));

        api.showImage(defaultMat);

        Mat laplace = api.convertLaplace(defaultMat, 0);
        api.showImage(laplace);
    }
    @Test
    public void mirrorImage() throws Exception {
        ImageAPI api = new ImageAPI();

        Mat defaultMat = api.loadImage(ConfigurationUtil.getConfigurationEntry("lab4.defoult.img1"));

        api.showImage(defaultMat);

        Mat mirrorImage = api.mirrorImage(defaultMat,0);
        api.showImage(mirrorImage);
    }
    @Test
    public void unionImage() throws Exception {
        ImageAPI api = new ImageAPI();

//        Mat defaultMat = api.loadImage("C:\\KS.jpg");

        Mat defaultMat1 = api.loadImage(ConfigurationUtil.getConfigurationEntry("lab3.defoult.imgj"));
//        defaultMat1 = api.convertIntoBlackChannel(1, defaultMat1);
        Mat defaultMat2 = api.loadImage(ConfigurationUtil.getConfigurationEntry("lab3.defoult.imgjj"));
//        api.showImage(defaultMat);

//        List<Mat> list = Arrays.asList(defaultMat, defaultMat1);
        Mat mat = api.unionImage(defaultMat1, defaultMat2);
        api.showImage(mat);
    }

    @Test
    public void repeatImage() throws Exception {
        ImageAPI api = new ImageAPI();

        Mat defaultMat = api.loadImage(ConfigurationUtil.getConfigurationEntry("lab4.defoult.img1"));

        api.showImage(defaultMat);
        Mat repeatImage = api.repeatImage(defaultMat, 2, 2);
        api.showImage(repeatImage);
    }

    @Test
    public void resizeImage() throws Exception {
        ImageAPI api = new ImageAPI();

        Mat defaultMat = api.loadImage(ConfigurationUtil.getConfigurationEntry("lab4.defoult.img1"));

        api.showImage(defaultMat);

        Mat resizeImage = api.resizeImage(defaultMat, 1000, 1000);
        api.showImage(resizeImage);
    }

    @Test
    public void geometryChangeImage() throws Exception {
        ImageAPI api = new ImageAPI();

        Mat defaultMat = api.loadImage(ConfigurationUtil.getConfigurationEntry("lab4.defoult.img1"));

        api.showImage(defaultMat);

        Mat geometryChangeImage = api.geometryChangeImage(defaultMat);
        api.showImage(geometryChangeImage);
    }

    @Test
    public void task3ToMorphing() throws Exception {
        ImageAPI api = new ImageAPI();
        double[] sizes = {3, 5, 7, 9, 13, 15};
        final String str = ConfigurationUtil.getConfigurationEntry("lab4.defoult.img1");
        System.out.println(str);
        Mat defaultMat = Imgcodecs.imread(str);

        AtomicInteger integer = new AtomicInteger(0);
        for (double size: sizes){
            Mat morphRect = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(size, size));

            Mat dst1 = defaultMat.clone();

            Imgproc.dilate(defaultMat, dst1, morphRect);
            api.saveMatImageOnDisk(dst1, String.format(ConfigurationUtil.getConfigurationEntry("lab3.defoult.img3"), integer.incrementAndGet()));

            Mat dst1_1 = defaultMat.clone();
            Imgproc.morphologyEx(defaultMat, dst1_1, Imgproc.MORPH_GRADIENT, morphRect);
            api.saveMatImageOnDisk(dst1_1,  String.format(ConfigurationUtil.getConfigurationEntry("lab3.defoult.img3"), integer.incrementAndGet()));

            Mat dst1_2 = defaultMat.clone();
            Imgproc.morphologyEx(defaultMat, dst1_2, Imgproc.MORPH_BLACKHAT, morphRect);
            api.saveMatImageOnDisk(dst1_2,  String.format(ConfigurationUtil.getConfigurationEntry("lab3.defoult.img3"), integer.incrementAndGet()));
        }
    }

    @Test
    public void task4ToWarp() throws Exception {
        ImageAPI api = new ImageAPI();
        Mat mat = api.task4ToWarp();
        api.showImage(mat);
    }

    @Test
    public void task5ToFeel() throws Exception {
        ImageAPI api = new ImageAPI();
        Mat mat = api.task5ToFill(80);
        api.showImage(mat);
    }

    @Test
    public void pyramid_test() throws Exception{
        ImageAPI api = new ImageAPI();
        Mat mat = api.pyramid();
        api.showImage(mat);
    }

    @Test
    public void square() throws Exception{
        ImageAPI api = new ImageAPI();
        api.square();
    }
}