import java.applet.*;
import java.awt.*;


public class asteroids extends java.awt.Canvas
{
   private static final long serialVersionUID = 1;

   private final short TRUE = -1;
   private final short FALSE = 0;

   private final int MAX_XLEN = 512;
   private final int MAX_YLEN = 400;

   private final short START_ROCKS = 5;
   private final short MAX_ROCKS = 100;
   private final short HISCORE_XOFFSET = 40;
   private final short HISCORE_YOFFSET = 20;
   private final short GAMEOVER_XOFFSET = 90;
   private final short GAMEOVER_YOFFSET = 20;
   private final short INSERTCOIN_XOFFSET = 110;
   private final short INSERTCOIN_YOFFSET = 20;
   private final short HISCORE_SCALE = 3;

   private final String AboutMsg[] = {"Asteriods Application", "Version 1.01  Friday 16th March 2012", "(C) Jason Birch Java Development"};

   private Panel Application;      
   private msgbox ProgressBar = null;
   private msgbox AboutBox = null;
   private String Progress[] = {"Progress"};

   private Image MainImage = null;

   private sound AppSound = null;

   private rect Desktop;
   private astroship Ship;
   private astroufo UFO;
   private astrorock Rock[];
   private short FirstRock;
   private short NextRock;
   private classnumber HiScore;
   private classtext GameOver;
   private classtext InsertCoin;
   
   private boolean ThrustFlag = false;
   private boolean RotateLeftFlag = false;
   private boolean RotateRightFlag = false;


   public asteroids(Applet NewApplication, int BackgroundColour)
   {
      LoadApp((Panel)NewApplication, BackgroundColour);
   }


   public asteroids(Panel NewApplication, int BackgroundColour)
   {
      LoadApp(NewApplication, BackgroundColour);
   }


   private void LoadApp(Panel NewApplication, int BackgroundColour)
   {
      short Count;

      AppSound = new sound();
      AppSound.Load("Belt.au");
      AppSound.Load("Credit.au");
      AppSound.Load("HyperSpace.au");
      AppSound.Load("Rock.au");
      AppSound.Load("Shot.au");
      AppSound.Load("Thrust.au");
      AppSound.Load("UFO.au");
      AppSound.Load("UFOShot.au");

      Application = NewApplication;

      setBackground(new Color(BackgroundColour));

      AboutBox = new msgbox();
      ProgressBar = new msgbox();
      Application.add("Center", AboutBox);
      Application.add("Center", ProgressBar);
      ProgressBar.Display(msgbox.STYLE_PROGRESS, "Loading Please Wait...", Progress, 0, MAX_YLEN-50, MAX_XLEN, 50);

      MainImage = Application.createImage(MAX_XLEN, MAX_YLEN);

      Desktop = new rect();
      HiScore = new classnumber();
      GameOver = new classtext();
      InsertCoin = new classtext();
      Ship = new astroship(AppSound);
      UFO = new astroufo(AppSound);
      Rock = new astrorock[MAX_ROCKS];
      
      Desktop.left = 0;
      Desktop.right = MAX_XLEN;
      Desktop.top = 0;
      Desktop.bottom = MAX_YLEN;
      
      GameOver.SetLocation((short)((Desktop.right-Desktop.left)/2 - GAMEOVER_XOFFSET), (short)((Desktop.bottom-Desktop.top)/2 - GAMEOVER_YOFFSET), (short)5, (short)200, FALSE, "GAME OVER", 0x55DD55);
      InsertCoin.SetLocation((short)((Desktop.right-Desktop.left)/2 - INSERTCOIN_XOFFSET), (short)((Desktop.bottom-Desktop.top)/2 - INSERTCOIN_YOFFSET), (short)5, (short)15, TRUE, "INSERT COIN", 0x55DD55);
      HiScore.SetLocation((short)((Desktop.right-Desktop.left)/2 - HISCORE_XOFFSET), HISCORE_YOFFSET, HISCORE_SCALE);
      Ship.SetArea(Desktop);
      UFO.SetArea(Desktop);
      FirstRock = START_ROCKS;
      NextRock = FirstRock;
      for (Count = 0; Count < MAX_ROCKS; ++Count)
         Rock[Count] = new astrorock(AppSound);
      for (Count = 0; Count < FirstRock; ++Count)
         Rock[Count].SetArea(Desktop, (short)-1, (short)-1, (short)-1);

      ProgressBar.setVisible(false);
   }


   public void update(Graphics GC)
   {
      paint(GC);
   }


   public void paint(Graphics GC)
   {
      short Count;
      
      GC.drawImage(MainImage, 0, 0, this);

  /*******************************/
 /* Draw ship and rock objects. */
/*******************************/
      InsertCoin.Draw(GC);
      GameOver.Draw(GC);
      HiScore.Draw(GC);
      Ship.Draw(GC);
      UFO.Draw(GC);
      for (Count = 0; Count < MAX_ROCKS; ++Count)
         Rock[Count].Draw(GC);
   }


   public boolean keyDown(Event Evt, int Key)
   {
      short Count;
      
      if (Key == 1004)
         Ship.Shoot();
      else if (Key == 1005)
         ThrustFlag = true;
      else if (Key == 1006)
         RotateLeftFlag = true;
      else if (Key == 1007)
         RotateRightFlag = true;
      else if (Key == 32)
         Ship.Hyperspace();
      else if (Key == 1009)
      {
         if (Ship.GetLives() == FALSE)
         {
            if (Ship.GetScore() > HiScore.GetNumber())
               HiScore.SetNumber(Ship.GetScore());
            Ship.Reset();
            UFO.Destroy();
            UFO.GetShot().Destroy();
            FirstRock = START_ROCKS;
            NextRock = FirstRock;
            for (Count = 0; Count < MAX_ROCKS; ++Count)
               Rock[Count].Destroy();
            for (Count = 0; Count < FirstRock; ++Count)
               Rock[Count].SetArea(Desktop, (short)-1, (short)-1, (short)-1);
         }
      }
      return true;
   }


   public boolean keyUp(Event Evt, int Key)
   {
      if (Key == 1005)
         ThrustFlag = false;
      else if (Key == 1006)
         RotateLeftFlag = false;
      else if (Key == 1007)
         RotateRightFlag = false;
      return true;
   }


   public boolean mouseDown(Event Evt, int x, int y)
   {
      if ((Evt.modifiers & Event.META_MASK) != 0)
         AboutBox.Display(msgbox.STYLE_MSGBOX, "About", AboutMsg, 80, 70, msgbox.AUTO_SIZE, msgbox.AUTO_SIZE);
      return true;
   }


   public void DoFrame()
   {
      short RockFound;
      short Count;
      short ShotCount;
   
      if (RotateLeftFlag == true)
         Ship.IncAngle(FALSE);
      else if (RotateRightFlag == true)
         Ship.IncAngle(TRUE);
      if (ThrustFlag == true)
         Ship.Thrust();
      Ship.Move();
      UFO.Move();
      RockFound = FALSE;
      for (Count = 0; Count < MAX_ROCKS; ++Count)
      {
         if (Rock[Count].GetSize() != astrorock.INACTIVE)
            RockFound = TRUE;
  /*****************************/
 /* Check for ship collision. */
/*****************************/
         if (Ship.GetCrash() == FALSE && UFO.Collide(Ship.GetXOffset(), Ship.GetYOffset(), Ship.GetWidth(), Ship.GetHeight()) == TRUE)
            Ship.SetCrash(TRUE);
         if (UFO.GetShot().Active() == TRUE && Ship.Collide(UFO.GetShot().GetXOffset(), UFO.GetShot().GetYOffset(), (short)2, (short)2) == TRUE)
            Ship.SetCrash(TRUE);
         if (Ship.GetCrash() == FALSE && Rock[Count].Collide(Ship.GetXOffset(), Ship.GetYOffset(), Ship.GetWidth(), Ship.GetHeight()) == TRUE)
         {
            Ship.SetCrash(TRUE);
            Rock[++NextRock].SetArea(Desktop, Ship.GetXOffset(), Ship.GetYOffset(), Rock[Count].GetSize());
         }
  /*************************/
 /* Check for shot rocks. */
/*************************/
         for (ShotCount = 0; ShotCount < Ship.GetShotCount(); ++ShotCount)
         {
            if (Ship.GetShot(ShotCount).Active() != FALSE)
            {
               if (UFO.Collide(Ship.GetShot(ShotCount).GetXOffset(), Ship.GetShot(ShotCount).GetYOffset(), (short)2, (short)2) == TRUE)
               {
                  Ship.SetScore(Ship.GetScore() + 100);
                  Ship.GetShot(ShotCount).Destroy();
               }
               if (Rock[Count].Collide(Ship.GetShot(ShotCount).GetXOffset(), Ship.GetShot(ShotCount).GetYOffset(), (short)2, (short)2) == TRUE)
               {
                  Ship.SetScore(Ship.GetScore() + 5*Rock[Count].GetSize());
                  Ship.GetShot(ShotCount).Destroy();
                  if (NextRock+1 < MAX_ROCKS)
                     Rock[++NextRock].SetArea(Desktop, Rock[Count].GetXOffset(), Rock[Count].GetYOffset(), Rock[Count].GetSize());
               }
            }
         }
         Rock[Count].Move();
      }
      if (RockFound == FALSE)
      {
         ++FirstRock;
         NextRock = FirstRock;
         for (Count = 0; Count < FirstRock; ++Count)
            Rock[Count].SetArea(Desktop, (short)-1, (short)-1, (short)-1);
      }
      if (Ship.GetLives() == FALSE)
         GameOver.SetVisible(TRUE);
      else
         GameOver.SetVisible(FALSE);
      if (GameOver.GetVisible() == FALSE && Ship.GetLives() == FALSE)
         InsertCoin.SetVisible(TRUE);
      else
         InsertCoin.SetVisible(FALSE);
  /******************************/
 /* Display the current frame. */
/******************************/
      repaint();
   }
}
