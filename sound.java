import java.net.*;
import java.util.*;
import javax.sound.sampled.*;


public class sound
{
   private Hashtable<String, Clip> Clips;


   public sound()
   {
      Clips = new Hashtable<String, Clip>();
   }


   public void Load(String Filename)
   {
      Clip ThisClip;
      URL ThisURL;
      AudioInputStream AudioFile;

      try
      {
         ThisURL = this.getClass().getClassLoader().getResource(Filename);
         AudioFile = AudioSystem.getAudioInputStream(ThisURL);
         ThisClip = AudioSystem.getClip();
         ThisClip.open(AudioFile);
         Clips.put(Filename, ThisClip);
      }
      catch (Exception e)
      {
      }
   }


   public void Play(String Filename)
   {
      Clip ThisClip;

      ThisClip = Clips.get(Filename);
      if (ThisClip != null && !ThisClip.isRunning())
      {
         ThisClip.setFramePosition(0);
         ThisClip.loop(0);
      }
   }
}

