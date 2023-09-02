package me.nullaqua;

import me.lanzhi.api.util.collection.ByteVector;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;

public class FileToImage
{
    /**
     * 从图片中读取文件.包含文件的图片应是由{@link #write(File, File, int, int)}方法生成的.
     *
     * @param from  图片文件
     * @param toDir 文件保存目录
     * @throws IOException 发生的IO错误.若图片损坏/不是由{@link #write(File, File, int, int)}方法生成的,也会导致此异常.
     */
    public static void read(File from, File toDir) throws IOException
    {
        toDir.mkdirs();
        if (!toDir.isDirectory())
        {
            throw new IOException("Cannot create directory: " + toDir);
        }
        if (!from.isFile())
        {
            throw new IOException("Image not found: " + from);
        }
        if (!from.getName().endsWith(".png"))
        {
            throw new IOException("Image should be a png file: " + from);
        }
        var in = new DataInputStream(new ImageInputStream(ImageIO.read(from)));
        var name = in.readUTF();
        var length = in.readLong();
        File to = new File(toDir, name);
        to.createNewFile();
        if (!to.isFile())
        {
            throw new IOException("Cannot create file: " + to);
        }
        try (var out = Files.newOutputStream(to.toPath()))
        {
            while (length > 0)
            {
                out.write(in.readNBytes((int) Math.min(length, Integer.MAX_VALUE)));
                length -= Integer.MAX_VALUE;
            }
        }
        catch (Throwable e)
        {
            to.delete();
            throw e;
        }
    }

    /**
     * 将文件写入图片.可用于隐藏文件.使用{@link #read(File, File)}方法恢复.
     *
     * @param from 文件
     * @param to   图片文件保存位置
     * @param h    图片高度
     * @param w    图片宽度
     * @throws IOException            发生的IO错误
     * @throws ImageTooSmallException 图片太小,无法保存文件
     */
    public static void write(File from, File to, int h, int w) throws IOException
    {
        if (!from.isFile())
        {
            throw new IOException("File not found: " + from);
        }
        if (!to.getName().endsWith(".png"))
        {
            throw new IOException("Image should be a png file: " + to);
        }
        if (h <= 0 || w <= 0)
        {
            throw new IllegalArgumentException("Image size should be positive");
        }
        if (from.length() > (long) h * w * 4)
        {
            throw new ImageTooSmallException();
        }
        try (var in = Files.newInputStream(from.toPath()))
        {
            var out = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
            ByteVector vector = new ByteVector();
            var dout = new DataOutputStream(vector.toOutputStream());
            dout.writeUTF(from.getName());
            dout.writeLong(from.length());
            dout.close();
            if (vector.size() + from.length() > (long) h * w * 4)
            {
                throw new ImageTooSmallException();
            }
            dout = new DataOutputStream(new ImageOutputStream(out));
            dout.write(vector.toByteArray());
            in.transferTo(dout);
            dout.close();
            ImageIO.write(out, "png", to);
        }
    }

    public static class ImageTooSmallException extends IOException
    {
        public ImageTooSmallException()
        {
            super("Image too small");
        }
    }
}

class ImageOutputStream extends OutputStream
{
    private final BufferedImage image;
    private int x, y;
    private int color;
    private int count;

    public ImageOutputStream(BufferedImage image)
    {
        this.image = image;
    }

    @Override
    public void write(int b) throws IOException
    {
        color |= (b & 0xff) << (8 * count);
        count++;
        if (count == 4)
        {
            if (y == image.getHeight())
            {
                throw new FileToImage.ImageTooSmallException();
            }
            image.setRGB(x, y, color);
            x++;
            if (x == image.getWidth())
            {
                x = 0;
                y++;
            }
            count = 0;
            color = 0;
        }
    }

    @Override
    public void flush()
    {
        if (count != 0)
        {
            image.setRGB(x, y, color);
        }
    }

    @Override
    public void close()
    {
        flush();
        image.flush();
    }
}

class ImageInputStream extends InputStream
{
    private final BufferedImage image;
    private int x, y;
    private int color;
    private int count;

    public ImageInputStream(BufferedImage image)
    {
        this.image = image;
    }

    @Override
    public int read() throws IOException
    {
        if (count == 0)
        {
            if (y == image.getHeight())
            {
                throw new IOException("Image format error");
            }
            color = image.getRGB(x, y);
            x++;
            if (x == image.getWidth())
            {
                x = 0;
                y++;
            }
        }
        int b = color >> (8 * count) & 0xff;
        count++;
        if (count == 4)
        {
            count = 0;
        }
        return b;
    }

    @Override
    public int available()
    {
        return image.getWidth() * image.getHeight() * 4;
    }

    @Override
    public void close()
    {
        image.flush();
    }
}