import java.awt.*;


public class astroshot
{
   public static final short SMALL_SHOT = 0;
   public static final short LARGE_SHOT = 1;
   
   private final short SMALL_SHOT_FRAMES = 40;
   private final short LARGE_SHOT_FRAMES = 20;
   private final short ERASE_FRAME = 1;
   private final short HYPERSPACE = -500;
   
   private short xMax;
   private short yMax;
   private short xOffset;
   private short yOffset;
   private short OldxOffset;
   private short OldyOffset;
   private float xVelocity;
   private float yVelocity;
   private short Size;
   private short FrameCount;


   public astroshot()
   {
      xMax = 0;
      yMax = 0;
      xOffset = HYPERSPACE;
      yOffset = HYPERSPACE;
      OldxOffset = HYPERSPACE;
      OldyOffset = HYPERSPACE;
      xVelocity = (float)0;
      yVelocity = (float)0;
      Size = SMALL_SHOT;
      FrameCount = 0;
   }


   public void Draw(Graphics hdc)
   {
      int ColourPen;
      int BackgroundPen;

      if (FrameCount != 0)
      {
         if (FrameCount == ERASE_FRAME)
            FrameCount = 0;
         BackgroundPen = 0x000000;
         if (Size == SMALL_SHOT)
         {
            ColourPen = 0x55DD55;
            hdc.setColor(new Color(BackgroundPen));
            hdc.drawLine(OldxOffset, OldyOffset, OldxOffset+1, OldyOffset);
            hdc.drawLine(OldxOffset, OldyOffset+1, OldxOffset+1, OldyOffset+1);
            if (FrameCount != 0)
            {
               hdc.setColor(new Color(ColourPen));
               hdc.drawLine(xOffset, yOffset, xOffset+1, yOffset);
               hdc.drawLine(xOffset, yOffset+1, xOffset+1, yOffset+1);

               OldxOffset = xOffset;
               OldyOffset = yOffset;
            }
         }
         else
         {
            ColourPen = 0x77FF77;
            hdc.setColor(new Color(BackgroundPen));
            hdc.drawLine(OldxOffset, OldyOffset, OldxOffset+2, OldyOffset);
            hdc.drawLine(OldxOffset, OldyOffset+1, OldxOffset+2, OldyOffset+1);
            hdc.drawLine(OldxOffset, OldyOffset+2, OldxOffset+2, OldyOffset+2);
            if (FrameCount != 0)
            {
               hdc.setColor(new Color(ColourPen));
               hdc.drawLine(xOffset, yOffset, xOffset+2, yOffset);
               hdc.drawLine(xOffset, yOffset+1, xOffset+2, yOffset+1);
               hdc.drawLine(xOffset, yOffset+2, xOffset+2, yOffset+2);

               OldxOffset = xOffset;
               OldyOffset = yOffset;
            }
         }
      }
   }


   public void Move()
   {
      if (FrameCount > ERASE_FRAME)
      {
         if (xOffset < 0)
            xOffset = xMax;
         else if (xOffset > xMax)
            xOffset = 0;
         if (yOffset < 0)
            yOffset = yMax;
         else if (yOffset > yMax)
            yOffset = 0;
         xOffset += (short)xVelocity;
         yOffset += (short)yVelocity;
         --FrameCount;
      }
   }


   public void SetArea(short NewxMax, short NewyMax, short NewxOffset, short NewyOffset, float NewxVelocity, float NewyVelocity, short NewSize)
   {
      xMax = NewxMax;
      yMax = NewyMax;
      xOffset = NewxOffset;
      yOffset = NewyOffset;
      xVelocity = NewxVelocity;
      yVelocity = NewyVelocity;
      Size = NewSize;
      if (Size == LARGE_SHOT)
         FrameCount = LARGE_SHOT_FRAMES;
      else
         FrameCount = SMALL_SHOT_FRAMES;
   }


   public short GetXOffset()
   {
      return xOffset;
   }


   public short GetYOffset()
   {
      return yOffset;
   }


   public void Destroy()
   {
      FrameCount = ERASE_FRAME;
      xOffset = HYPERSPACE;
      yOffset = HYPERSPACE;
   }


   public short Active()
   {
      if (FrameCount != 0)
         return -1;
      else
         return 0;
   }
}
