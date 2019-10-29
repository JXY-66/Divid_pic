package com.company;

import Jama.Matrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;

public class diVid {



    public double[] DividLength(double[] x,double[] y, double[] fig, Matrix mn,Matrix bb) {
            return null;
    }

    public void DividWidth() {

    }

    public void MinBoundrect() {
    }

    public void ModulePoint() {
    }

    public void PrePo() {
    }
    /**
     * 图像切割 主程序
     * @param srcImageFile 源图像地址
     */
    public void Sement(String srcImageFile) throws IOException {
        tool tool = new tool();
        try {
            ImageFilter cropFilter;
            String dir = null;
            // 读取源图像
            System.out.println("1111111111111111111111----------111111");
            // 获取源图像
            File file = new File(srcImageFile);
            BufferedImage read = ImageIO.read(file);
            int srcWidth = read.getWidth(); // 源图宽度
            int srcHeight = read.getWidth(); // 源图高度
            tool.grayImage(read); // 获取图片位置　将图片灰度处理
            tool.binaryImage(read); //二值化处理
            //二值化　灰度化处理后图片保存到img路径下　名称为img/后的名字
//            输出图片
//            File newFile = new File("img/jxy2.jpg");
//            ImageIO.write(read, "jpg", newFile);


            System.out.println("srcWidth:" + srcWidth);
            System.out.println("srcHeight:" + srcHeight);
        }
        catch (Exception e)
        {
            e.printStackTrace();//打印错误信息
        }
    }






    public static void main(String[] args) throws IOException {
        try {
            diVid diVid = new diVid();
            diVid.Sement("images/Aw.jpg");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
