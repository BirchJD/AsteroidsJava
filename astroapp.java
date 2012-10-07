/**********************************************************/
/* Compile:                                               */
/* javac *.java -classpath .                              */
/*                                                        */
/* Execute from the command line:                         */
/* java astroapp                                          */
/*                                                        */
/* Use the following HTML code to display this applet:    */
/*                                                        */
/* <CENTER>                                               */
/* <APPLET CODE="astroapp.class" WIDTH="512" HEIGHT="400">*/
/* </APPLET>                                              */
/* </CENTER>                                              */
/**********************************************************/
import java.awt.*;
import java.awt.event.*;


public class astroapp extends java.applet.Applet implements Runnable
{
   private static final long serialVersionUID = 1;

   private static winlisten AppWindowFlags = null;
   private static asteroids AstroGame = null;
   private static Thread Task = null;
   private static boolean Abort = false;

/************************************************/
/* Start the application from the command line. */
/************************************************/
   public static void main(String args[])
   {
      astroapp ThisApp = new astroapp();
      GraphicsEnvironment GE;
      GraphicsDevice[] GS;
      GraphicsDevice GD;
      GraphicsConfiguration[] GC;
      Rectangle VirtualBounds = new Rectangle();
      Rectangle AppWinBounds;

//      System.out.println("DEBUG");

  /**************************************/
 /* Get information about the display. */
/**************************************/
      GE = GraphicsEnvironment.getLocalGraphicsEnvironment();
      GS = GE.getScreenDevices();
      GD = GS[0];
      GC = GD.getConfigurations();
      VirtualBounds = VirtualBounds.union(GC[0].getBounds());

  /*********************************/
 /* Create an application Window. */
/*********************************/
      Frame AppWindow = new Frame("Asteroids", GC[0]);
      AppWinBounds = GC[0].getBounds();
      AppWindow.setLocation(10 + AppWinBounds.x, 10 + AppWinBounds.y);
      AppWindow.setResizable(false);
      AppWindow.setSize(512, 400);
  
      Panel AppPanel = new Panel();
      AppWindow.add(AppPanel);

  /**************************************************/
 /* Add the application to the application Window. */
/**************************************************/
      AstroGame = new asteroids(AppPanel, 0x000000);

      AppPanel.setLayout(new BorderLayout());
      AppPanel.add("Center", AstroGame);
  
      AppWindowFlags = new winlisten();
      AppWindow.addWindowListener(AppWindowFlags);

      AppWindow.setVisible(true);
  
  /****************************/  
 /* Animate the application. */
/****************************/  
      Task = new Thread(ThisApp);
      Task.start();
   }


   public static void windowStateChanged(WindowEvent e)
   {
      Abort = true;
   }


/**************************************************/
/* Start the application from a web browser page. */
/**************************************************/
   public void init()
   {
      int BackgroundColour;

      BackgroundColour = Integer.valueOf(getParameter("BackgroundColour")).intValue();
      setBackground(new Color(BackgroundColour));

      AstroGame = new asteroids(this, BackgroundColour);
      
      setLayout(new BorderLayout());
      add("Center", AstroGame);

      Task = new Thread(this);
      Task.start();
   }


/**********************************/
/* Tell the application to close. */
/**********************************/
   public void destroy()
   {
      if (Task != null)
         Abort = true;
   }


   public void update(Graphics GC)
   {
      paint(GC);
   }


   public void paint(Graphics GC)
   {     
      showStatus("(C) 2012 - Jason Birch Java Development");
   }


/****************************/
/* Animate the application. */
/****************************/
   public void run()
   {
      try
      {
         while ((AppWindowFlags == null) || (!Abort && AppWindowFlags != null && !AppWindowFlags.getCloseFlag()))
         {
            Thread.sleep(50);
            AstroGame.DoFrame();
         };
      }
      catch (Exception e)
      {
         showStatus("run() failed. " + e);
      }

      System.exit(0);
   }
}
