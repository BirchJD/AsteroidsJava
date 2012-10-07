import java.awt.*;


public class astroship
{
   private final short TRUE = -1;
   private final short FALSE = 0;

   private final short FRAMES = 63;
   private final short ERASE_FRAME = (short)(FRAMES + 1);
   private final short FRAME_POINTS = 5;
   private final short THRUST_POINTS = 8;
   private final short SHIP_WIDTH = 25;
   private final short SHIP_HEIGHT = 25;
   private final short SHIP_START_ANGLE = 31;
   private final short MAX_EXPLODE_FRAME = 40;
   private final short LIFE_XGAP = 30;
   private final short LIFE_XOFFSET = 150;
   private final short LIFE_YOFFSET = 35;
   private final short MAX_LIVES = 3;
   private final short SCORE_XOFFSET = 10;
   private final short SCORE_YOFFSET = 20;
   private final short SCORE_SCALE = 5;
   private final short MAX_SHOTS = 10;
   private final short HYPERSPACE = -500;
   private final short HYPER_FRAMES = 20;

   private double OneDegree;
   private double FrameStep;

   private short Lives;
   private short Crash;
   private short ThrustFlag;
   private short Fade;
   private short xMax;
   private short yMax;
   private short Angle;
   private short xOffset;
   private short yOffset;
   private short ShotIndex;
   private float xVelocity;
   private float yVelocity;
   private short ExplodeFrame;
   private short HyperCount;
   private classnumber PlayerScore;
   private astroshot Shots[];
   private point LifeFrame[];
   private point LifeDisplayFrame[];
   private point Frame[][];
   private point DisplayFrame[];
   private point OldFrame[];
   private point ExplodeDirection[];
   private point ThrustTrail[];

   private sound AppSound;


   public astroship(sound NewAppSound)
   {
      short Count;

      AppSound = NewAppSound;

      Shots = new astroshot[MAX_SHOTS];
      LifeFrame = new point[FRAME_POINTS];
      LifeDisplayFrame = new point[FRAME_POINTS];
      Frame = new point[FRAMES][FRAME_POINTS];
      DisplayFrame = new point[FRAME_POINTS*2];
      OldFrame = new point[FRAME_POINTS*2];
      ExplodeDirection = new point[FRAME_POINTS*2];
      ThrustTrail = new point[THRUST_POINTS];

      PlayerScore = new classnumber();

      OneDegree = (double)6.3 / (double)360;
      FrameStep = (double)0.1;

      xMax = 0;
      yMax = 0;
      xOffset = HYPERSPACE;
      yOffset = HYPERSPACE;
      xVelocity = (short)0;
      yVelocity = (short)0;
      Crash = 0;
      ThrustFlag = 0;
      Fade = 0;
      Lives = MAX_LIVES;
      ExplodeFrame = 0;
      ShotIndex = 0;
      HyperCount = 0;

      for (Count = 0; Count < MAX_SHOTS; ++Count)
      {
         Shots[Count] = new astroshot();
      }

      for (Count = 0; Count < THRUST_POINTS; ++Count)
      {
         ThrustTrail[Count] = new point();
      }

      for (Count = 0; Count < FRAME_POINTS*2; ++Count)
      {
         DisplayFrame[Count] = new point();
         OldFrame[Count] = new point();
         ExplodeDirection[Count] = new point();
      }

      for (Count = 0; Count < FRAME_POINTS; ++Count)
      {
         LifeFrame[Count] = new point();
         LifeDisplayFrame[Count] = new point();
      }
      Angle = SHIP_START_ANGLE;
      LifeFrame[0].x = (short)(19 * Math.sin(FrameStep*Angle + OneDegree*0));
      LifeFrame[0].y = (short)(19 * Math.cos(FrameStep*Angle + OneDegree*0));
      LifeFrame[1].x = (short)(16 * Math.sin(FrameStep*Angle + OneDegree*140));
      LifeFrame[1].y = (short)(16 * Math.cos(FrameStep*Angle + OneDegree*140));
      LifeFrame[2].x = (short)(8 * Math.sin(FrameStep*Angle + OneDegree*180));
      LifeFrame[2].y = (short)(8 * Math.cos(FrameStep*Angle + OneDegree*180));
      LifeFrame[3].x = (short)(16 * Math.sin(FrameStep*Angle + OneDegree*220));
      LifeFrame[3].y = (short)(16 * Math.cos(FrameStep*Angle + OneDegree*220));
      LifeFrame[4].x = (short)(19 * Math.sin(FrameStep*Angle + OneDegree*0));
      LifeFrame[4].y = (short)(19 * Math.cos(FrameStep*Angle + OneDegree*0));

      for (Angle = 0; Angle < FRAMES; ++Angle)
      {
         for (Count = 0; Count < FRAME_POINTS; ++Count)
         {
            Frame[Angle][Count] = new point();
         }
         Frame[Angle][0].x = (short)(16 * Math.sin(FrameStep*Angle + OneDegree*0));
         Frame[Angle][0].y = (short)(16 * Math.cos(FrameStep*Angle + OneDegree*0));
         Frame[Angle][1].x = (short)(13 * Math.sin(FrameStep*Angle + OneDegree*140));
         Frame[Angle][1].y = (short)(13 * Math.cos(FrameStep*Angle + OneDegree*140));
         Frame[Angle][2].x = (short)(5 * Math.sin(FrameStep*Angle + OneDegree*180));
         Frame[Angle][2].y = (short)(5 * Math.cos(FrameStep*Angle + OneDegree*180));
         Frame[Angle][3].x = (short)(13 * Math.sin(FrameStep*Angle + OneDegree*220));
         Frame[Angle][3].y = (short)(13 * Math.cos(FrameStep*Angle + OneDegree*220));
         Frame[Angle][4].x = (short)(16 * Math.sin(FrameStep*Angle + OneDegree*0));
         Frame[Angle][4].y = (short)(16 * Math.cos(FrameStep*Angle + OneDegree*0));
      }

      Angle = SHIP_START_ANGLE;
   }


   public void Draw(Graphics hdc)
   {
      short Count;
      short LifeCount;
      int ColourPen;
      int FadePen;
      int BackgroundPen;

      for (Count = 0; Count < MAX_SHOTS; ++Count)
         Shots[Count].Draw(hdc);
  /***********************/
 /* Create pens to use. */
/***********************/
      ColourPen = 0x55DD55;
      FadePen = 0x55DD55 - (0x01 * Fade + 0x0100 * 2*Fade + 0x010000 * Fade);
      BackgroundPen = 0x000000;

      if (Lives != 0 || ExplodeFrame != MAX_EXPLODE_FRAME)
      {
  /*********************/
 /* Draw intact ship. */
/*********************/
         if (Crash == 0)
         {
  /********************************/
 /* Plot ships current position. */
/********************************/
            for (Count = 0; Count < FRAME_POINTS; ++Count)
            {
               DisplayFrame[Count].x = (short)(Frame[Angle][Count].x + xOffset);
               DisplayFrame[Count].y = (short)(Frame[Angle][Count].y + yOffset);
            }
            hdc.setColor(new Color(BackgroundPen));
            if (ExplodeFrame >= ERASE_FRAME)
            {
               ExplodeFrame = 0;
  /************************************/
 /* Erase previous position of ship. */
/************************************/
               for (Count = 0; Count < FRAME_POINTS*2; Count += 2)
               {
                  hdc.drawLine(OldFrame[Count].x, OldFrame[Count].y, OldFrame[Count+1].x, OldFrame[Count+1].y);
               }
            }
  /************************************/
 /* Erase previous position of ship. */
/************************************/
            win32.Polyline(hdc, OldFrame, FRAME_POINTS);
  /******************************************/
 /* Draw the ship in the current position. */
/******************************************/
            hdc.setColor(new Color(ColourPen));
            win32.Polyline(hdc, DisplayFrame, FRAME_POINTS);
  /******************/
 /* Remove thrust. */
/******************/
            hdc.setColor(new Color(BackgroundPen));
            for (Count = 0; Count < THRUST_POINTS; ++Count)
            {
               hdc.drawLine(ThrustTrail[Count].x, ThrustTrail[Count].y, ThrustTrail[Count].x + 1, ThrustTrail[Count].y + 1);
            }
  /*****************************************/
 /* Add thrust point if currently active. */
/*****************************************/
            if (ThrustFlag == TRUE)
            {
               hdc.setColor(new Color(ColourPen));
               for (Count = 0; Count < THRUST_POINTS; ++Count)
               {
                  ThrustTrail[Count].x = (short)(xOffset + (short)(Math.random() * 7)-3 + (long)(15 * Math.sin(FrameStep*Angle + OneDegree*180)));
                  ThrustTrail[Count].y = (short)(yOffset + (short)(Math.random() * 7)-3 + (long)(15 * Math.cos(FrameStep*Angle + OneDegree*180)));
                  hdc.drawLine(ThrustTrail[Count].x, ThrustTrail[Count].y, ThrustTrail[Count].x + 1, ThrustTrail[Count].y + 1);
               }
               ThrustFlag = FALSE;
            }
   /******************************************/
  /* Plot ships current position,           */
 /* next time it will be the old position. */
/******************************************/
            for (Count = 0; Count < FRAME_POINTS; ++Count)
            {
               OldFrame[Count].x = DisplayFrame[Count].x;
               OldFrame[Count].y = DisplayFrame[Count].y;
            }
         }
         else
         {
            if (ExplodeFrame == 0)
            {
               --Lives;
  /************************************/
 /* Erase previous position of ship. */
/************************************/
               hdc.setColor(new Color(BackgroundPen));
               win32.Polyline(hdc, OldFrame, FRAME_POINTS);
  /******************/
 /* Remove thrust. */
/******************/
               for (Count = 0; Count < THRUST_POINTS; ++Count)
               {
                  hdc.drawLine(ThrustTrail[Count].x, ThrustTrail[Count].y, ThrustTrail[Count].x + 1, ThrustTrail[Count].y + 1);
               }
  /**************************************************/
 /* Set direction of individual lines of the ship. */
/**************************************************/
               for (Count = 0; Count < FRAME_POINTS*2; Count += 2)
               {
                  do
                  {
                     ExplodeDirection[Count].x = (short)((Math.random() * 10) - 5);
                  } while (ExplodeDirection[Count].x == 0);
                  do
                  {
                     ExplodeDirection[Count].y = (short)((Math.random() * 10) - 5);
                  } while (ExplodeDirection[Count].y == 0);
                  ExplodeDirection[Count+1].x = ExplodeDirection[Count].x;
                  ExplodeDirection[Count+1].y = ExplodeDirection[Count].y;
               }
  /********************************************/
 /* Split polygon shape into seperate lines. */
/********************************************/
               DisplayFrame[0].x = (short)(Frame[Angle][FRAME_POINTS-1].x + xOffset);
               DisplayFrame[0].y = (short)(Frame[Angle][FRAME_POINTS-1].y + yOffset);
               OldFrame[0].x = (short)(Frame[Angle][FRAME_POINTS-1].x + xOffset);
               OldFrame[0].y = (short)(Frame[Angle][FRAME_POINTS-1].y + yOffset);
               DisplayFrame[FRAME_POINTS*2-1].x = (short)(Frame[Angle][FRAME_POINTS-1].x + xOffset);
               DisplayFrame[FRAME_POINTS*2-1].y = (short)(Frame[Angle][FRAME_POINTS-1].y + yOffset);
               OldFrame[FRAME_POINTS*2-1].x = (short)(Frame[Angle][FRAME_POINTS-1].x + xOffset);
               OldFrame[FRAME_POINTS*2-1].y = (short)(Frame[Angle][FRAME_POINTS-1].y + yOffset);
               for (Count = 0; Count < FRAME_POINTS-1; ++Count)
               {
                  DisplayFrame[Count*2+1].x = (short)(Frame[Angle][Count].x + xOffset);
                  DisplayFrame[Count*2+1].y = (short)(Frame[Angle][Count].y + yOffset);
                  DisplayFrame[Count*2+2].x = (short)(Frame[Angle][Count].x + xOffset);
                  DisplayFrame[Count*2+2].y = (short)(Frame[Angle][Count].y + yOffset);
                  OldFrame[Count*2+1].x = (short)(Frame[Angle][Count].x + xOffset);
                  OldFrame[Count*2+1].y = (short)(Frame[Angle][Count].y + yOffset);
                  OldFrame[Count*2+2].x = (short)(Frame[Angle][Count].x + xOffset);
                  OldFrame[Count*2+2].y = (short)(Frame[Angle][Count].y + yOffset);
               }
            }
  /************************************/
 /* Erase previous position of ship. */
/************************************/
            hdc.setColor(new Color(BackgroundPen));
            for (Count = 0; Count < FRAME_POINTS*2; Count += 2)
            {
               hdc.drawLine(OldFrame[Count].x, OldFrame[Count].y, OldFrame[Count+1].x, OldFrame[Count+1].y);
            }
  /******************************************/
 /* Draw the ship in the current position. */
/******************************************/
            if (ExplodeFrame < MAX_EXPLODE_FRAME -1)
            {
               Fade += 0x55 / MAX_EXPLODE_FRAME;
               hdc.setColor(new Color(FadePen));
               for (Count = 0; Count < FRAME_POINTS*2; Count += 2)
               {
                  DisplayFrame[Count].x += ExplodeDirection[Count].x;
                  DisplayFrame[Count].y += ExplodeDirection[Count].y;
                  DisplayFrame[Count+1].x += ExplodeDirection[Count+1].x;
                  DisplayFrame[Count+1].y += ExplodeDirection[Count+1].y;
                  hdc.drawLine(DisplayFrame[Count].x, DisplayFrame[Count].y, DisplayFrame[Count+1].x, DisplayFrame[Count+1].y);
               }
   /******************************************/
  /* Plot ships current position,           */
 /* next time it will be the old position. */
/******************************************/
               for (Count = 0; Count < FRAME_POINTS*2; ++Count)
               {
                  OldFrame[Count].x = DisplayFrame[Count].x;
                  OldFrame[Count].y = DisplayFrame[Count].y;
               }
            }
  /************************/
 /* Reset for next life. */
/************************/
            ++ExplodeFrame;
            if (Lives != 0 && ExplodeFrame == MAX_EXPLODE_FRAME)
            {
               ExplodeFrame = 0;
               Crash = 0;
               ThrustFlag = FALSE;
               Fade = 0;
               Angle = SHIP_START_ANGLE;
               xOffset = (short)(xMax/2);
               yOffset = (short)(yMax/2);
               xVelocity = (short)0;
               yVelocity = (short)0;
            }
         }
  /****************************/
 /* Display remaining lives. */
/****************************/
         for (LifeCount = 0; LifeCount < MAX_LIVES; ++LifeCount)
         {
  /********************************/
 /* Plot ships current position. */
/********************************/
            for (Count = 0; Count < FRAME_POINTS; ++Count)
            {
               LifeDisplayFrame[Count].x = (short)(LifeFrame[Count].x + (xMax - LIFE_XOFFSET) + (LifeCount+1)*LIFE_XGAP);
               LifeDisplayFrame[Count].y = (short)(LifeFrame[Count].y + LIFE_YOFFSET);
            }
  /************************************/
 /* Erase previous position of ship. */
/************************************/
            hdc.setColor(new Color(BackgroundPen));
            win32.Polyline(hdc, LifeDisplayFrame, FRAME_POINTS);
  /******************************************/
 /* Draw the ship in the current position. */
/******************************************/
            if (Lives > LifeCount)
            {
               hdc.setColor(new Color(ColourPen));
               win32.Polyline(hdc, LifeDisplayFrame, FRAME_POINTS);
            }
         }
      }
  /******************/
 /* Redraw scores. */
/******************/
      PlayerScore.Draw(hdc);
   }


   public void IncAngle(short Direction)
   {
      if (Crash == 0)
      {
         if (Direction == 0)
         {
            ++Angle;
            if (Angle >= FRAMES)
               Angle = 0;
         }
         else
         {
            --Angle;
            if (Angle < 0)
               Angle = (short)(FRAMES-1);
         }
      }
   }


   public void Thrust()
   {
      ThrustFlag = TRUE;
      xVelocity += (float)Math.sin(FrameStep*Angle + OneDegree*0);
      yVelocity += (float)Math.cos(FrameStep*Angle + OneDegree*0);
      AppSound.Play("Thrust.au");
   }


   public void Shoot()
   {
      if (Crash == 0)
      {
         AppSound.Play("Shot.au");
         Shots[ShotIndex].SetArea(xMax, yMax, xOffset, yOffset, xVelocity + 10*(float)Math.sin(FrameStep*Angle + OneDegree*0), yVelocity + 10*(float)Math.cos(FrameStep*Angle + OneDegree*0), astroshot.SMALL_SHOT);
         if (++ShotIndex == MAX_SHOTS)
            ShotIndex = 0;
      }
   }


   public void Move()
   {
      short Count;

      if (HyperCount != 0)
      {
         --HyperCount;
         if (HyperCount == 0)
         {
            xOffset = (short)((float)Math.random() * (xMax - 2*SHIP_WIDTH));
            yOffset = (short)((float)Math.random() * (yMax - 2*SHIP_HEIGHT));
         }
      }
      for (Count = 0; Count < MAX_SHOTS; ++Count)
         Shots[Count].Move();
      if (Crash == 0)
      {
         if (xOffset < 0 - SHIP_WIDTH)
            xOffset = (short)(xMax + SHIP_WIDTH);
         else if (xOffset > xMax + SHIP_WIDTH)
            xOffset = (short)(0 - SHIP_WIDTH);
         if (yOffset < 0 - SHIP_HEIGHT)
            yOffset = (short)(yMax + SHIP_HEIGHT);
         else if (yOffset > yMax + SHIP_HEIGHT)
            yOffset = (short)(0 - SHIP_HEIGHT);
         xOffset += (short)xVelocity;
         yOffset += (short)yVelocity;
      }
   }


   public void Reset()
   {
      if (Lives == 0)
      {
         AppSound.Play("Belt.au");
         ExplodeFrame = ERASE_FRAME;
         Crash = 0;
         ThrustFlag = FALSE;
         Fade = 0;
         Angle = SHIP_START_ANGLE;
         xOffset = (short)(xMax/2);
         yOffset = (short)(yMax/2);
         xVelocity = (short)0;
         yVelocity = (short)0;
         Lives = MAX_LIVES;
         PlayerScore.SetNumber(0);
      }
   }


   public void SetArea(rect ClientRect)
   {
      xMax = (short)(ClientRect.right - ClientRect.left);
      yMax = (short)(ClientRect.bottom - ClientRect.top);
      xOffset = (short)(xMax/2);
      yOffset = (short)(yMax/2);

      PlayerScore.SetLocation(SCORE_XOFFSET, SCORE_YOFFSET, SCORE_SCALE);
   }


   public void Hyperspace()
   {
      if (Crash == 0 && HyperCount == 0)
      {
         AppSound.Play("HyperSpace.au");
         HyperCount = HYPER_FRAMES;
         xOffset = HYPERSPACE;
         yOffset = HYPERSPACE;
         xVelocity = (float)0;
         yVelocity = (float)0;
      }
   }


   public short Collide(short xPos, short yPos, short Width, short Height)
   {
      short Collision = FALSE;

      if (Crash == 0)
      {
         if (xPos + Width/2 > xOffset - SHIP_WIDTH && xPos - Width/2 < xOffset + SHIP_WIDTH && yPos + Height/2 > yOffset - SHIP_HEIGHT && yPos - Height/2 < yOffset + SHIP_HEIGHT == true)
         {
            Collision = TRUE;
            Crash = TRUE;
         }         
      }
      return  Collision;
   }


   public void SetCrash(short NewCrash)
   {
      if (Crash == 0 && NewCrash == TRUE)
         AppSound.Play("Rock.au");

      Crash = NewCrash;
   }


   public short GetCrash()
   {
      return Crash;
   }


   public short GetXOffset()
   {
      return xOffset;
   }


   public short GetYOffset()
   {
      return yOffset;
   }


   public short GetWidth()
   {
      return SHIP_WIDTH;
   }


   public short GetHeight()
   {
      return SHIP_HEIGHT;
   }


   public long GetScore()
   {
      return PlayerScore.GetNumber();
   }


   public void SetScore(long NewScore)
   {
      PlayerScore.SetNumber(NewScore);
   }


   public short GetLives()
   {
      return Lives;
   }


   public astroshot GetShot(short ShotCount)
   {
      return Shots[ShotCount];
   }


   public short GetShotCount()
   {
      return MAX_SHOTS;
   }
}
