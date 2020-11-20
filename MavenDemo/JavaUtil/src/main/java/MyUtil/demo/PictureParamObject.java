package MyUtil.demo;

import org.apache.poi.util.Units;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class PictureParamObject
{
    private BufferedImage image;

    private byte[] byteArray; // 图片文件

    private String fileName; // 图片文件名

    private String fileType; // 图片拓展名

    private int width;

    private int height;

    private File file; // 图片文件

    public BufferedImage getImage()
    {
        return image;
    }

    public void setImage(BufferedImage image)
    {
        this.image = image;
    }

    public String getFileType()
    {
        return fileType;
    }

    public void setFileType(String fileType)
    {
        this.fileType = fileType;
    }

    public byte[] getByteArray()
    {
        return byteArray;
    }

    public void setByteArray(byte[] byteArray)
    {
        this.byteArray = byteArray;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public File getFile()
    {
        return file;
    }

    public void setFile(File file)
    {
        this.file = file;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public boolean readFile(File file) throws IOException
    {
        if (file.exists())
        {
            this.file = file;
            this.image = ImageIO.read(file);
            String fileNameWithEx = file.getName();
            String[] strSplit = fileNameWithEx.split("\\.");
            this.fileType = strSplit.length > 1 ? strSplit[1] : "jpg";
            this.fileName = strSplit.length > 1 ? strSplit[0] : "";
            this.width = this.image.getWidth();
            this.height = this.image.getHeight();
            return true;
        }
        return false;
    }

    public byte[] toByteArray() throws IOException
    {
        if (this.file.exists())
        {
            InputStream inputStream = new FileInputStream(this.file);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) != -1)
            {
                outputStream.write(buf, 0, len);
            }
            return outputStream.toByteArray();
        }
        return null;
    }
}
