package customs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import javax.swing.JButton;
import javax.swing.border.Border;

/**
 * @author Tomy
 */

public class CustomButton extends JButton implements Serializable, MouseListener
{
    private Color color = null;
    private String buttonText = "";
    private Border border = super.getBorder();
    
    public CustomButton()
    {
        super();
        super.setText(buttonText);
        
        super.setBackground(new Color(0, 43, 64));
        setContentAreaFilled(false);
        super.setOpaque(true);
        super.setForeground(Color.WHITE);
        
        addMouseListener(this);
    }
    
    public void setColor(Color color)
    {
        super.setBackground(color);
    }
    
    public void setText(String text)
    {
        super.setText(text);
    }
    
    public String getText()
    {
        return super.getText();
    }
    
    protected void paintComponent(Graphics g)
    {
        final Graphics2D g2 = (Graphics2D) g.create();
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();

        super.paintComponent(g);
    }

    @Override
    public void mouseClicked(MouseEvent me)
    {

    }

    @Override
    public void mousePressed(MouseEvent me)
    {

    }

    @Override
    public void mouseReleased(MouseEvent me)
    {

    }

    @Override
    public void mouseEntered(MouseEvent me)
    {
        super.setBackground(new Color(0, 87, 124));
    }

    @Override
    public void mouseExited(MouseEvent me)
    {
        super.setBackground(new Color(0, 43, 64));
    }
}