import java.awt.event.*;


public class winlisten extends WindowAdapter implements WindowListener
{ 
   private static final long serialVersionUID = 1;

   private boolean CloseFlag = false;


	public void windowClosing(WindowEvent e)
   {
      CloseFlag = true;
   }

   public boolean getCloseFlag()
   {
      return CloseFlag;
   }
}

