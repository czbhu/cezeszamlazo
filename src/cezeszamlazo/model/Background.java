package cezeszamlazo.model;

import java.awt.Image;
import javax.imageio.ImageIO;

/**
 * @author Tomy
 */
public class Background
{
    private Image image;
    
    public Background(String file)
    {
        try
        {
            image = ImageIO.read(ClassLoader.getSystemResource(file));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public Image Resize(int newWidth)
    {
        image = image.getScaledInstance(newWidth, 700, Image.SCALE_SMOOTH);
    
        return image;
    }

    //GETTERS
    public Image getImage()
    {
        return image;
    }
}
