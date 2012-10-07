import java.awt.*;


public class win32
{
   public static void Polyline(Graphics hdc, point Point[], short PointCount)
   {
      short Count;
      
      for (Count = 0; Count < PointCount -1; ++Count)
      {
         hdc.drawLine(Point[Count].x, Point[Count].y, Point[Count+1].x, Point[Count+1].y);
      }
   }
}
