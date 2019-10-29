package com.company;

import Jama.Matrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;

public class tool {

    /**
     * 灰度处理   MATLAB rgb2gray 改编 java
     */
    public  Image grayImage(BufferedImage bufferedImage) throws Exception {

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        BufferedImage grayBufferedImage = new BufferedImage(width, height, bufferedImage.getType());
        for (int i = 0; i < bufferedImage.getWidth(); i++) {
            for (int j = 0; j < bufferedImage.getHeight(); j++) {
                final int color = bufferedImage.getRGB(i, j);
                final int r = (color >> 16) & 0xff;
                final int g = (color >> 8) & 0xff;
                final int b = color & 0xff;
                int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);
                int newPixel = colorToRGB(255, gray, gray, gray);
                grayBufferedImage.setRGB(i, j, newPixel);
            }
        }

        return grayBufferedImage;

    }

    /**
     * 颜色分量转换为RGB值
     *
     * @param alpha
     * @param red
     * @param green
     * @param blue
     * @return
     */
    private static int colorToRGB(int alpha, int red, int green, int blue) {

        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;

        return newPixel;

    }


    /**
     * 二值化　im2bw
     */
    public static BufferedImage binaryImage(BufferedImage image) throws Exception {
        int w = image.getWidth();
        int h = image.getHeight();
        float[] rgb = new float[3];
        double[][] zuobiao = new double[w][h];
        int black = new Color(0, 0, 0).getRGB();
        int white = new Color(255, 255, 255).getRGB();
        BufferedImage bi = new BufferedImage(w, h,
                BufferedImage.TYPE_BYTE_BINARY);
        ;
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int pixel = image.getRGB(x, y);
                rgb[0] = (pixel & 0xff0000) >> 16;
                rgb[1] = (pixel & 0xff00) >> 8;
                rgb[2] = (pixel & 0xff);
                float avg = (rgb[0] + rgb[1] + rgb[2]) / 3;
                zuobiao[x][y] = avg;

            }
        }
        //这里是阈值，白底黑字还是黑底白字,以白底黑字为例
        double SW = 192;
        for(int x = 0;x<w; x++) {
            for (int y = 0; y < h; y++) {
                if (zuobiao[x][y] < SW) {
                    bi.setRGB(x, y, black);
                } else {
                    bi.setRGB(x, y, white);
                }
            }
        }
	    return bi;
}
    // 自己加周围8个灰度值再除以9，算出其相对灰度值
    public static double getGray(double[][] zuobiao, int x, int y, int w, int h) {
        double rs = zuobiao[x][y] + (x == 0 ? 255 : zuobiao[x - 1][y]) + (x == 0 || y == 0 ? 255 : zuobiao[x - 1][y - 1])
                + (x == 0 || y == h - 1 ? 255 : zuobiao[x - 1][y + 1]) + (y == 0 ? 255 : zuobiao[x][y - 1])
                + (y == h - 1 ? 255 : zuobiao[x][y + 1]) + (x == w - 1 ? 255 : zuobiao[x + 1][y])
                + (x == w - 1 || y == 0 ? 255 : zuobiao[x + 1][y - 1])
                + (x == w - 1 || y == h - 1 ? 255 : zuobiao[x + 1][y + 1]);
        return rs / 9;
    }

    /**
     * 降噪，以1个像素点为单位（可以循环降噪，或者把单位可以扩大为多个像素点）
     * @param image
     * @return
     */
    public static BufferedImage denoise(BufferedImage image){
        int w = image.getWidth();
        int h = image.getHeight();
        int white = new Color(255, 255, 255).getRGB();

        if(isWhite(image.getRGB(1, 0)) && isWhite(image.getRGB(0, 1)) && isWhite(image.getRGB(1, 1))){
            image.setRGB(0,0,white);
        }
        if(isWhite(image.getRGB(w-2, 0)) && isWhite(image.getRGB(w-1, 1)) && isWhite(image.getRGB(w-2, 1))){
            image.setRGB(w-1,0,white);
        }
        if(isWhite(image.getRGB(0, h-2)) && isWhite(image.getRGB(1, h-1)) && isWhite(image.getRGB(1, h-2))){
            image.setRGB(0,h-1,white);
        }
        if(isWhite(image.getRGB(w-2, h-1)) && isWhite(image.getRGB(w-1, h-2)) && isWhite(image.getRGB(w-2, h-2))){
            image.setRGB(w-1,h-1,white);
        }

        for(int x = 1; x < w-1; x++){
            int y = 0;
            if(isBlack(image.getRGB(x, y))){
                int size = 0;
                if(isWhite(image.getRGB(x-1, y))){
                    size++;
                }
                if(isWhite(image.getRGB(x+1, y))){
                    size++;
                }
                if(isWhite(image.getRGB(x, y+1))){
                    size++;
                }
                if(isWhite(image.getRGB(x-1, y+1))){
                    size++;
                }
                if(isWhite(image.getRGB(x+1, y+1))){
                    size++;
                }
                if(size>=5){
                    image.setRGB(x,y,white);
                }
            }
        }
        for(int x = 1; x < w-1; x++){
            int y = h-1;
            if(isBlack(image.getRGB(x, y))){
                int size = 0;
                if(isWhite(image.getRGB(x-1, y))){
                    size++;
                }
                if(isWhite(image.getRGB(x+1, y))){
                    size++;
                }
                if(isWhite(image.getRGB(x, y-1))){
                    size++;
                }
                if(isWhite(image.getRGB(x+1, y-1))){
                    size++;
                }
                if(isWhite(image.getRGB(x-1, y-1))){
                    size++;
                }
                if(size>=5){
                    image.setRGB(x,y,white);
                }
            }
        }

        for(int y = 1; y < h-1; y++){
            int x = 0;
            if(isBlack(image.getRGB(x, y))){
                int size = 0;
                if(isWhite(image.getRGB(x+1, y))){
                    size++;
                }
                if(isWhite(image.getRGB(x, y+1))){
                    size++;
                }
                if(isWhite(image.getRGB(x, y-1))){
                    size++;
                }
                if(isWhite(image.getRGB(x+1, y-1))){
                    size++;
                }
                if(isWhite(image.getRGB(x+1, y+1))){
                    size++;
                }
                if(size>=5){
                    image.setRGB(x,y,white);
                }
            }
        }

        for(int y = 1; y < h-1; y++){
            int x = w - 1;
            if(isBlack(image.getRGB(x, y))){
                int size = 0;
                if(isWhite(image.getRGB(x-1, y))){
                    size++;
                }
                if(isWhite(image.getRGB(x, y+1))){
                    size++;
                }
                if(isWhite(image.getRGB(x, y-1))){
                    size++;
                }
                //斜上下为空时，去掉此点
                if(isWhite(image.getRGB(x-1, y+1))){
                    size++;
                }
                if(isWhite(image.getRGB(x-1, y-1))){
                    size++;
                }
                if(size>=5){
                    image.setRGB(x,y,white);
                }
            }
        }
        //降噪，以1个像素点为单位
        for(int y = 1; y < h-1; y++){
            for(int x = 1; x < w-1; x++){
                if(isBlack(image.getRGB(x, y))){
                    int size = 0;
                    //上下左右均为空时，去掉此点
                    if(isWhite(image.getRGB(x-1, y))){
                        size++;
                    }
                    if(isWhite(image.getRGB(x+1, y))){
                        size++;
                    }
                    //上下均为空时，去掉此点
                    if(isWhite(image.getRGB(x, y+1))){
                        size++;
                    }
                    if(isWhite(image.getRGB(x, y-1))){
                        size++;
                    }
                    //斜上下为空时，去掉此点
                    if(isWhite(image.getRGB(x-1, y+1))){
                        size++;
                    }
                    if(isWhite(image.getRGB(x+1, y-1))){
                        size++;
                    }
                    if(isWhite(image.getRGB(x+1, y+1))){
                        size++;
                    }
                    if(isWhite(image.getRGB(x-1, y-1))){
                        size++;
                    }
                    if(size>=8){
                        image.setRGB(x,y,white);
                    }
                }
            }
        }

        return image;
    }

    public static boolean isBlack(int colorInt)
    {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() <= 300)
        {
            return true;
        }
        return false;
    }

    public static boolean isWhite(int colorInt)
    {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() > 300)
        {
            return true;
        }
        return false;
    }

    public static int isBlack(int colorInt, int whiteThreshold) {
        final Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() <= whiteThreshold) {
            return 1;
        }
        return 0;
    }
    /*
   打印矩阵
    */
    public static void print(Matrix A) {
        int a_col = A.getColumnDimension();
        int a_row = A.getRowDimension();
        for(int i = 0; i < a_row; i++) {
            for(int j = 0; j < a_col; j++) {
                System.out.print(A.get(i, j) + " ");
            }
            System.out.println();
        }
    }
    /*
        MATLAB中函数 reshape，它可以将一个矩阵重塑为另一个大小不同的新矩阵，但保留其原始数据。
    给出一个由二维数组表示的矩阵，以及两个正整数r和c，分别表示想要的重构的矩阵的行数和列数。
    重构后的矩阵需要将原始矩阵的所有元素以相同的行遍历顺序填充。
    如果具有给定参数的reshape操作是可行且合理的，则输出新的重塑矩阵；否则，输出原始矩阵。
     */
    public static int[][] matrixReshape(int[][] nums, int r, int c) {
        if(nums == null){
            return nums;
        }
        if(r == 0 || c == 0){
            return nums;
        }
        int row = 0;//行
        int columns = 0;//列
        columns = nums[0].length;
        row = nums.length;
        if(columns > 100 || row > 100 || columns < 1 || row < 1){
            return nums;
        }
        if(columns * row < r * c){
            return nums;
        }
        int [][]result = new int[r][c];
        int rr = 0;
        int cc = 0;
        int index = 0;
        for(int i = 0; i < row; i ++){
            for(int j = 0; j < columns; j ++){
                index = i * columns + j + 1;
                if(index > r * c){
                    break;
                }
                rr = index / c;//行
                cc = index % c;//列
                if(rr > 0 && cc ==0){
                    rr = rr -1;
                    cc = c - 1;
                }else if(rr > 0 && cc > 0){
                    cc = cc -1;
                }else{
                    cc = cc -1;
                }
                result[rr][cc] = nums[i][j];
            }
        }
        return result;
    }
}
