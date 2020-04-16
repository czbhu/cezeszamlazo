package szamlazo.pdf;

import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author szekus
 */
public class DrawString
{
    private String str;
    private int startLinePosition;

    public DrawString(String str, int startLine)
    {
        this.str = str;
        this.startLinePosition = startLine;
    }

    private boolean isEmptyString() {
        return (str.length() == 0 || str.equals("") || str == null);
    }

    public void drawTheString(Graphics2D graphics2D, int my)
    {
        if (!isEmptyString())
        {
            ArrayList<String> lines = new ArrayList<>();
            String[] words = str.split("\\s");

            int sumCharacter = 0;
            String line = "";
            
            for (String word : words)
            {
                sumCharacter += word.length() + 1;
                line += word + " ";
                
                if (line.length() >= 110 || str.length() <= sumCharacter)
                {
                    lines.add(line);
                    line = "";
                }
            }
            
            if (lines.size() > 3)
            {
                increaseLineStartPosition(lines.size());
            }
            
            for (String lablecLine : lines)
            {
                startLinePosition -= 10;
                graphics2D.drawString(lablecLine, 5, my - startLinePosition);
            }
        }
    }
    
    private void increaseLineStartPosition(int lineCount)
    {
        startLinePosition = startLinePosition + (startLinePosition - lineCount * 10);
    }
}