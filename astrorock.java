import java.awt.*;


public class astrorock
{
   private final short TRUE = -1;
   private final short FALSE = 0;

   private final short FRAMES = 3;
   private final short FRAME_POINTS = 9;
   private final short ROCK_WIDTH = 36;
   private final short ROCK_HEIGHT = 36;
   private final short ERASE = 3;
   public static final short INACTIVE = 4;
   private final short NEW_POSITION = -1;
   private final short HYPERSPACE = -500;


   private short xMax;
   private short yMax;
   private short Size;
   private short xOffset;
   private short yOffset;
   private short xVelocity;
   private short yVelocity;
   private point Frame[][];
   private point DisplayFrame[];
   private point OldFrame[];

   private sound AppSound;


   public astrorock(sound NewAppSound)
   {
      short Count;
      short FrameCount;
      
      AppSound = NewAppSound;

      Frame = new point[FRAMES][FRAME_POINTS];
      DisplayFrame = new point[FRAME_POINTS];
      OldFrame = new point[FRAME_POINTS];

      xMax = 0;
      yMax = 0;
      xOffset = HYPERSPACE;
      yOffset = HYPERSPACE;
      xVelocity = 0;
      yVelocity = 0;

      for (Count = 0; Count < FRAME_POINTS; ++Count)
      {
         DisplayFrame[Count] = new point();
         OldFrame[Count] = new point();
         for (FrameCount = 0; FrameCount < FRAMES; ++FrameCount)
         {
            Frame[FrameCount][Count] = new point();
         }
      }

      for (Size = 0; Size < FRAMES; ++Size)
      {
         Frame[Size][0].x = (short)(-30 / (Size*Size+1));
         Frame[Size][0].y = (short)(-22 / (Size*Size+1));
         Frame[Size][1].x = (short)(-5 / (Size*Size+1));
         Frame[Size][1].y = (short)(-32 / (Size*Size+1));
         Frame[Size][2].x = (short)(+35 / (Size*Size+1));
         Frame[Size][2].y = (short)(-17 / (Size*Size+1));
         Frame[Size][3].x = (short)(+25 / (Size*Size+1));
         Frame[Size][3].y = (short)(+28 / (Size*Size+1));
         Frame[Size][4].x = (short)(+12 / (Size*Size+1));
         Frame[Size][4].y = (short)(-2 / (Size*Size+1));
         Frame[Size][5].x = (short)(+10 / (Size*Size+1));
         Frame[Size][5].y = (short)(+28 / (Size*Size+1));
         Frame[Size][6].x = (short)(-20 / (Size*Size+1));
         Frame[Size][6].y = (short)(+33 / (Size*Size+1));
         Frame[Size][7].x = (short)(-37 / (Size*Size+1));
         Frame[Size][7].y = (short)(+5 / (Size*Size+1));
         Frame[Size][8].x = (short)(-30 / (Size*Size+1));
         Frame[Size][8].y = (short)(-22 / (Size*Size+1));
      }
      Size = INACTIVE;
   }


   public void Draw(Graphics hdc)
   {
      short Count;
      int ColourPen;
      int BackgroundPen;

      if (Size < INACTIVE)
      {
         ColourPen = 0x55DD55;
         BackgroundPen = 0;

         hdc.setColor(new Color(BackgroundPen));
         win32.Polyline(hdc, OldFrame, FRAME_POINTS);
         if (Size == ERASE)
            Size = INACTIVE;
         if (Size < INACTIVE)
         {
            for (Count = 0; Count < FRAME_POINTS; ++Count)
            {
               DisplayFrame[Count].x = (short)(Frame[Size][Count].x + xOffset);
               DisplayFrame[Count].y = (short)(Frame[Size][Count].y + yOffset);
            }

            hdc.setColor(new Color(ColourPen));
            win32.Polyline(hdc, DisplayFrame, FRAME_POINTS);

            for (Count = 0; Count < FRAME_POINTS; ++Count)
            {
               OldFrame[Count].x = DisplayFrame[Count].x;
               OldFrame[Count].y = DisplayFrame[Count].y;
            }
         }
      }
   }


   public void Move()
   {
      if (Size < INACTIVE)
      {
         if (xOffset < 0 - ROCK_WIDTH)
            xOffset = (short)(xMax + ROCK_WIDTH);
         else if (xOffset > xMax + ROCK_WIDTH)
            xOffset = (short)(0 - ROCK_WIDTH);
         if (yOffset < 0 - ROCK_HEIGHT)
            yOffset = (short)(yMax + ROCK_HEIGHT);
         else if (yOffset > yMax + ROCK_HEIGHT)
            yOffset = (short)(0 - ROCK_HEIGHT);
         xOffset += xVelocity;
         yOffset += yVelocity;
      }
   }


   public void SetArea(rect ClientRect, short NewxOffset, short NewyOffset, short NewSize)
   {
      xMax = (short)(ClientRect.right - ClientRect.left);
      yMax = (short)(ClientRect.bottom - ClientRect.top);
      if (NewxOffset == NEW_POSITION)
      {
         Size = 0;
         if ((short)(Math.random() * 2) == FALSE)
         {
            xOffset = (short)(Math.random() * xMax);
            yOffset = (short)(yMax * (short)(Math.random() * 2));
         }
         else
         {
            yOffset = (short)(Math.random() * yMax);
            xOffset = (short)(xMax * (short)(Math.random() * 2));
         }
      }
      else
      {
         Size = NewSize;
         xOffset = NewxOffset;
         yOffset = NewyOffset;
      }
      do
      {
         xVelocity = (short)(Math.random()*6*(Size+1) - 3*(Size+1));
         yVelocity = (short)(Math.random()*6*(Size+1) - 3*(Size+1));
      } while(xVelocity == 0 || yVelocity == 0);
   }


   public short Collide(short xPos, short yPos, short Width, short Height)
   {
      short Collision = FALSE;

      if (Size < INACTIVE)
      {
         if (xPos + Width/2 > xOffset - ROCK_WIDTH / (Size*Size+1) && xPos - Width/2 < xOffset + ROCK_WIDTH / (Size*Size+1) && yPos + Height/2 > yOffset - ROCK_HEIGHT / (Size*Size+1) && yPos - Height/2 < yOffset + ROCK_HEIGHT / (Size*Size+1))
            Collision = TRUE;
         else
            Collision = FALSE;
         if (Collision == TRUE)
         {
            AppSound.Play("Rock.au");
            ++Size;
            do
            {
               xVelocity = (short)(Math.random()*6*(Size+1) - 3*(Size+1));
               yVelocity = (short)(Math.random()*6*(Size+1) - 3*(Size+1));
            } while(xVelocity == 0 || yVelocity == 0);
         }
      }
      return  Collision;
   }


   public void Destroy()
   {
      Size = ERASE;
      xOffset = HYPERSPACE;
      yOffset = HYPERSPACE;
   }


   public short GetSize()
   {
      return Size;
   }


   public short GetXOffset()
   {
      return xOffset;
   }


   public short GetYOffset()
   {
      return yOffset;
   }
}
