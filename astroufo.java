import java.awt.*;


public class astroufo
{
   private final short TRUE = -1;
   private final short FALSE = 0;

   private final short FRAMES = 2;
   private final short FRAME_POINTS = 12;
   private final short UFO_WIDTH = 16;
   private final short UFO_HEIGHT = 6;
   private final short INACTIVE = 3;
   private final short ERASE = 4;
   private final short HYPERSPACE = -500;

   private short xMax;
   private short yMax;
   private short Size;
   private short xOffset;
   private short yOffset;
   private short xVelocity;
   private short yVelocity;
   private astroshot Shot;
   private point Frame[][];
   private point DisplayFrame[];
   private point OldFrame[];

   private sound AppSound;


   public astroufo(sound NewAppSound)
   {
      short Count;
      short FrameCount;
      
      AppSound = NewAppSound;
      Shot = new astroshot();
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
         Frame[Size][0].x = (short)(-(8 + (Size+1)*3));
         Frame[Size][0].y = (short)(-(0 + (Size+1)));
         Frame[Size][1].x = (short)(-(0 + (Size+1)));
         Frame[Size][1].y = (short)(-(3 + (Size+1)*3));
         Frame[Size][2].x = (short)(+(0 + (Size+1)));
         Frame[Size][2].y = (short)(-(3 + (Size+1)*3));
         Frame[Size][3].x = (short)(+(8 + (Size+1)*3));
         Frame[Size][3].y = (short)(-(0 + (Size+1)));
         Frame[Size][4].x = (short)(-(8 + (Size+1)*3));
         Frame[Size][4].y = (short)(-(0 + (Size+1)));
         Frame[Size][5].x = (short)(-(8 + (Size+1)*3));
         Frame[Size][5].y = (short)(+(0 + (Size+1)));
         Frame[Size][6].x = (short)(+(8 + (Size+1)*3));
         Frame[Size][6].y = (short)(+(0 + (Size+1)));
         Frame[Size][7].x = (short)(+(8 + (Size+1)*3));
         Frame[Size][7].y = (short)(-(0 + (Size+1)));
         Frame[Size][8].x = (short)(+(8 + (Size+1)*3));
         Frame[Size][8].y = (short)(+(0 + (Size+1)));
         Frame[Size][9].x = (short)(+(0 + (Size+1)));
         Frame[Size][9].y = (short)(+(3 + (Size+1)*3));
         Frame[Size][10].x = (short)(-(0 + (Size+1)));
         Frame[Size][10].y = (short)(+(3 + (Size+1)*3));
         Frame[Size][11].x = (short)(-(8 + (Size+1)*3));
         Frame[Size][11].y = (short)(+(0 + (Size+1)));
      }
      Size = INACTIVE;
   }


   public void Draw(Graphics hdc)
   {
      short Count;
      int ColourPen;
      int BackgroundPen;

      Shot.Draw(hdc);
      if (Size != INACTIVE)
      {
         ColourPen = 0x77FF77;
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
      short ShotDirection;

      if (Shot.Active() == TRUE)
         Shot.Move();
      if (Size < INACTIVE)
      {
         AppSound.Play("UFO.au");
         if (Shot.Active() == FALSE)
         {
            AppSound.Play("UFOShot.au");
            ShotDirection = (short)(Math.random() * 4);
            switch (ShotDirection)
            {
               case 0:
               {
                  Shot.SetArea(xMax, yMax, xOffset, yOffset, (float)-8, (float)-8, astroshot.LARGE_SHOT);
                  break;
               }
               case 1:
               {
                  Shot.SetArea(xMax, yMax, xOffset, yOffset, (float)-8, (float)+8, astroshot.LARGE_SHOT);
                  break;
               }
               case 2:
               {
                  Shot.SetArea(xMax, yMax, xOffset, yOffset, (float)+8, (float)-8, astroshot.LARGE_SHOT);
                  break;
               }
               case 3:
               {
                  Shot.SetArea(xMax, yMax, xOffset, yOffset, (float)+8, (float)+8, astroshot.LARGE_SHOT);
                  break;
               }
            }
         }
         if ((short)(Math.random() * 10) == FALSE)
            yVelocity = (short)((Math.random() * ((Size+2)*10)) - ((Size+2)*5));
         if (xOffset < 0 - UFO_WIDTH)
            Size = INACTIVE;
         else if (xOffset > xMax + UFO_WIDTH)
            Size = INACTIVE;
         if (yOffset < 0)
            yOffset = 0;
         else if (yOffset > yMax)
            yOffset = yMax;
         xOffset += xVelocity;
         yOffset += yVelocity;
      }
      else if ((short)(Math.random() * 1000) == FALSE)
      {
         Size = (short)(Math.random() * FRAMES);
         yOffset = (short)(Math.random() * yMax);
         if ((short)(Math.random() * 2) == FALSE)
         {
            xOffset = (short)(0 - UFO_WIDTH);
            xVelocity = (short)(3 * (2 - Size+1));
         }
         else
         {
            xOffset = (short)(xMax + UFO_WIDTH);
            xVelocity = (short)(-3 * (2 - Size+1));
         }
         yVelocity = (short)((Math.random() * 5) - 3);
      }
   }


   public void SetArea(rect ClientRect)
   {
      xMax = (short)(ClientRect.right - ClientRect.left);
      yMax = (short)(ClientRect.bottom - ClientRect.top);
   }


   public short Collide(short xPos, short yPos, short Width, short Height)
   {
      short Collision = FALSE;

      if (Size < INACTIVE)
      {
         if (xPos + Width/2 > xOffset - UFO_WIDTH / (Size*Size+1) && xPos - Width/2 < xOffset + UFO_WIDTH / (Size*Size+1) && yPos + Height/2 > yOffset - UFO_HEIGHT / (Size*Size+1) && yPos - Height/2 < yOffset + UFO_HEIGHT / (Size*Size+1))
            Collision = TRUE;
         else
            Collision = FALSE;
         if (Collision == TRUE)
            Destroy();
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


   public astroshot GetShot()
   {
      return Shot;
   }
}

