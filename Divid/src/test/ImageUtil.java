package test;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;

/**
 * 图像处理辅助类
 */
public class ImageUtil {

    /**
     * 分割图像
     *
     * @param image
     *            传入的图片对象
     * @param rows
     *            垂直方向上需要裁剪出的图片数量 - 行
     * @param cols
     *            水平方向上需要裁剪出的图片数量 - 列
     * @param x
     *            开始裁剪位置的X坐标
     * @param y
     *            开始裁剪位置的Y坐标
     * @param width
     *            每次裁剪的图片宽度
     * @param height
     *            每次裁剪的图片高度
     * @param changeX
     *            每次需要改变的X坐标数量
     * @param changeY
     *            每次需要改变的Y坐标数量
     * @param component
     *            容器对象，目的是用来创建裁剪后的每个图片对象
     * @return 裁剪完并加载到内存后的二维图片数组
     */
    public static Image[][] cutImage(Image image, int rows, int cols, int x,
                                     int y, int width, int height, int changeX, int changeY,
                                     Component component) {
        Image[][] images = new Image[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                ImageFilter filter = new CropImageFilter(x + j * changeX, y + i
                        * changeY, width, height);
                images[i][j] = component.createImage(new FilteredImageSource(
                        image.getSource(), filter));
            }
        }

        return images;
    }
}