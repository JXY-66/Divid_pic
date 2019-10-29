package test;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * 图像分割测试
 */
public class CutImageDemo extends JFrame {

    private static final long serialVersionUID = 1140239462766935667L;
    private MediaTracker mediaTracker;
    private Image[][] images;

    public CutImageDemo() {
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // 创建媒体追踪器对象
        mediaTracker = new MediaTracker(this);
        // 获取源图像
        Image image = Toolkit.getDefaultToolkit().getImage("images/f.jpg");
        // 分割图像
        images = ImageUtil.cutImage(image, 5, 5, 0, 3, 128, 95, 128, 95, this);
        int index = 0;
        // 将所有分割得到的图像添加到MediaTracker追踪列表中
        for (Image[] images2 : images) {
            for (Image image2 : images2) {
                mediaTracker.addImage(image2, index++);
            }
        }

        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {

        try {
            // 加载所有图像
            mediaTracker.waitForAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (mediaTracker.checkAll()) { // 所有图像加载完毕
            // 循环将每张分割的图像绘制到窗体中
            for (int row = 0, len = images.length; row < len; row++) {
                for (int col = 0, length = images[row].length; col < length; col++) {
                    Image img = images[row][col];
                    int imgWidth = img.getWidth(null);
                    int imgHeight = img.getHeight(null);
                    int x = col * (imgWidth + 10) + 15;
                    int y = row * (imgHeight + 15) + 40;
                    g.drawImage(img, x, y, null);
                }
            }
        }
    }

    public static void main(String[] args) {
        new CutImageDemo();
    }
}
