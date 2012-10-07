import java.awt.*;


public class msgbox extends java.awt.Canvas
{
   private static final long serialVersionUID = 1;

   public final static int AUTO_SIZE = -1;
   public final static int STYLE_MSGBOX = 0;
   public final static int STYLE_PROGRESS = 1;

   private final int MIN_XLEN = 10;
   private final int MIN_YLEN = 10;
   private final int X_FONT_SIZE = 6;
   private final int Y_FONT_SIZE = 15;
   
   private int Style;
   private int xPos;
   private int yPos;
   private int xLen;
   private int yLen;
   private int ClickXPos;
   private int ClickYPos;
   private int Progress;
   private int ProgressMax;
   private String Title;
   private String MsgText[];
   
   public Dimension MinSize;


   public msgbox()
   {
      setVisible(false);
      xPos = 0;
      yPos = 0;
      xLen = MIN_XLEN;
      yLen = MIN_YLEN;
      Progress = 0;
      ProgressMax = 100;
      Title = "";
      MsgText = null;
      MinSize = new Dimension(MIN_XLEN, MIN_YLEN);
      setBackground(new Color(0x00BBBBBB));
   }


   public  Dimension preferredSize()
   {
      return minimumSize();
   }


   public synchronized Dimension minimumSize()
   {
      return MinSize;
   }


   public void update(Graphics GC)
   {
      paint(GC);
   }


   public void paint(Graphics GC)
   {
      int Count;

      GC.setColor(new Color(0x000000BB));
      GC.fillRect(3, 3, xLen-6, 3 + Y_FONT_SIZE);
      for (Count = 0; Count < 3; ++Count)
      {
         GC.setColor(new Color(0x00999999));
         GC.drawLine(Count, Count, xLen-1-Count, Count);
         GC.drawLine(Count, Count, Count, yLen-1-Count);
         GC.setColor(new Color(0x00555555));
         GC.drawLine(Count, yLen-1-Count, xLen-1-Count, yLen-1-Count);
         GC.drawLine(xLen-1-Count, Count, xLen-1-Count, yLen-1-Count);
      }
      GC.setColor(new Color(0x00FFFFFF));
      GC.drawString(Title, 5, Y_FONT_SIZE+2);
      GC.setColor(new Color(0x00000000));
      for (Count = 0; Count < MsgText.length; ++Count)
         GC.drawString(MsgText[Count], 5, 2*Y_FONT_SIZE+2 + Y_FONT_SIZE*Count);

      if (Style == STYLE_MSGBOX)
      {
         GC.setColor(new Color(0x00999999));
         GC.fillRect(xLen-Y_FONT_SIZE-2, 7, Y_FONT_SIZE-4, Y_FONT_SIZE-4);
         GC.setColor(new Color(0x00000000));
         GC.drawLine(xLen-Y_FONT_SIZE-1, 8, xLen-8, Y_FONT_SIZE+1);
         GC.drawLine(xLen-Y_FONT_SIZE-1, Y_FONT_SIZE+1, xLen-8, 8);
      }
      else if (Style == STYLE_PROGRESS)
      {
         GC.setColor(new Color(0x00BB0000));
         GC.fillRect(6, yLen-11, (Progress*(xLen-13))/ProgressMax, 5);
      }
   }


   public boolean mouseUp(Event Evt, int x, int y)
   {
      if (Style == STYLE_MSGBOX && x > xLen-Y_FONT_SIZE-3 && y < Y_FONT_SIZE+5)
         setVisible(false);
      return true;
   }


   public boolean mouseDown(Event Evt, int x, int y)
   {
      ClickXPos = x;
      ClickYPos = y;
      return true;
   }


   public boolean mouseDrag(Event evt, int x, int y)
   {
      xPos -= ClickXPos-x;
      yPos -= ClickYPos-y;
      setLocation(xPos, yPos);
      return true;
   }


   public void Display(int NewStyle, String NewTitle, String NewMsgText[], int NewXPos, int NewYPos, int NewXLen, int NewYLen)
   {
      int Count;

      Style = NewStyle;
      xPos = NewXPos;
      yPos = NewYPos;
      Title = NewTitle;
      MsgText = NewMsgText;
      if (NewXLen == AUTO_SIZE)
      {
         xLen = (X_FONT_SIZE * Title.length() + 1) + Y_FONT_SIZE + 9;
         for (Count = 0; Count < NewMsgText.length; ++Count)
         {
            if ((X_FONT_SIZE * NewMsgText[Count].length() + 1) + 6 > xLen)
               xLen = (X_FONT_SIZE * NewMsgText[Count].length() + 1) + 6;
         }
      }
      else
         xLen = NewXLen;
      if (NewYLen == AUTO_SIZE)
         yLen = Y_FONT_SIZE * (MsgText.length + 1) + 9;
      else
         yLen = NewYLen;
      setVisible(false);
      setLocation(xPos, yPos);
      setSize(xLen, yLen);
      setVisible(true);
   }
      
   
   public void Progress(int NewProgress, int NewProgressMax)
   {
      Progress = NewProgress;
      ProgressMax = NewProgressMax;
   }
}
