package com.company;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class gray extends JPanel{

    public static void grayImage(Graphics g) throws IOException{

        //读文件，图片文件放在项目同级目录下
        File file = new File("images/A1x.jpg");
        BufferedImage image = ImageIO.read(file);

        int width = image.getWidth();
        int height = image.getHeight();

        //new 一个 BufferedImage的缓冲区，，即时空的，，只起到缓冲作用，，将相应的图片转换
        BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        //BufferedImage grayImage = new BufferedImage(width, height,BufferedImage.TYPE_BYTE_BINARY);
        for(int i= 0 ; i < width ; i++){
            for(int j = 0 ; j < height; j++){
                int rgb = image.getRGB(i, j);
                grayImage.setRGB(i, j, rgb);  //将像素存入缓冲区

            }
        }

//       将图片存入相应的路径下：
       File newFile = new File("images/jxy.png");
       ImageIO.write(grayImage, "jpg", newFile);

        //画图
        g.drawImage(image, 0, 0, 380, 400,null);
        g.drawImage(grayImage,400,0, 380,400,null);
    }

    public static void main(String args[]) throws IOException {

        //创建窗口
        JFrame mFrame = new JFrame();
//        compressPic("/home/yingbao/jxy/Divid/images/jxy.png","/home/yingbao/jxy/Divid/images/jxy_1.jpg");
        mFrame.setSize(800, 500);
        mFrame.setVisible(true);
        mFrame.add(new gray());
    }

    //重写paint 方法 画图
    public void paint(Graphics g){

        try {
            grayImage(g);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public  static boolean compressPic(String srcFilePath, String descFilePath) throws IOException {
        File file = null;
        BufferedImage src = null;
        FileOutputStream out = null;
        ImageWriter imgWrier;
        ImageWriteParam imgWriteParams;

        // 指定写图片的方式为 jpg
        imgWrier = ImageIO.getImageWritersByFormatName("jpg").next();
        imgWriteParams = new javax.imageio.plugins.jpeg.JPEGImageWriteParam(
                null);
        // 要使用压缩，必须指定压缩方式为MODE_EXPLICIT
        imgWriteParams.setCompressionMode(imgWriteParams.MODE_EXPLICIT);
        // 这里指定压缩的程度，参数qality是取值0~1范围内，
        imgWriteParams.setCompressionQuality((float)1);
        imgWriteParams.setProgressiveMode(imgWriteParams.MODE_DISABLED);
        File file1 = new File("/home/yingbao/jxy/Divid/images/f.jpg");
        System.out.println(file1.getName());
        BufferedImage read = ImageIO.read(new File("/home/yingbao/jxy/Divid/images/f.jpg"));
        System.out.println(read.getHeight()+"<<<<<"+read.getWidth());
        ColorModel colorModel = read.getColorModel();// ColorModel.getRGBdefault();
        // 指定压缩时使用的色彩模式
        imgWriteParams.setDestinationType(new javax.imageio.ImageTypeSpecifier(
                colorModel, colorModel.createCompatibleSampleModel(0,25)));
//        imgWriteParams.setDestinationType(new javax.imageio.ImageTypeSpecifier(
//                colorModel, colorModel.createCompatibleSampleModel(16, 16)));

        try {
            if (isBlank(srcFilePath)) {
                return false;
            } else {
                file = new File(srcFilePath);
                System.out.println(file.length());
                src = ImageIO.read(file);
                out = new FileOutputStream(descFilePath);

                imgWrier.reset();
                // 必须先指定 out值，才能调用write方法, ImageOutputStream可以通过任何
                // OutputStream构造
                imgWrier.setOutput(ImageIO.createImageOutputStream(out));
                // 调用write方法，就可以向输入流写图片
                imgWrier.write(null, new IIOImage(src, null, null),
                        imgWriteParams);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean isBlank(String string) {
        if (string == null || string.length() == 0 || string.trim().equals("")) {
            return true;
        }
        return false;
    }
}
