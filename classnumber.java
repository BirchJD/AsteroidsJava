import java.awt.*;


public class classnumber
{
   private final short DIGITS = 10;
   private final short MAX_POINTS = 10;

   private short xOffset;
   private short yOffset;
   private short Scale;
   private long NumberValue;
   private point Decimal[][];
   private point DisplayFrame[][];
   private point OldFrame[][];


   public classnumber()
   {
      short Count;
      short PointCount;
      
      NumberValue = 0;
      xOffset = 0;
      yOffset = 0;
      Scale = 0;

      Decimal = new point[DIGITS][MAX_POINTS+1];
      DisplayFrame = new point[DIGITS][MAX_POINTS+1];
      OldFrame = new point[DIGITS][MAX_POINTS+1];
      
      for (Count = 0; Count < DIGITS; ++Count)
      {
         for (PointCount = 0; PointCount < MAX_POINTS + 1; ++PointCount)
         {
            Decimal[Count][PointCount] = new point();
            DisplayFrame[Count][PointCount] = new point();
            OldFrame[Count][PointCount] = new point();
         }
      }
   }


   public void Draw(Graphics hdc)
   {
      int ColourPen;
      int BackgroundPen;
      short Digit;
      short Count;
      int Remainder;
      long TempNumber;

  /***********************/
 /* Create pens to use. */
/***********************/
      ColourPen = 0x55DD55;
      BackgroundPen = 0x000000;
      hdc.setColor(new Color(BackgroundPen));
  /*****************************/
 /* Plot score current value. */
/*****************************/
      TempNumber = NumberValue;
      for (Digit = 0; Digit < 10; ++Digit)
      {
         Remainder = (int)(TempNumber - (((long)(TempNumber/10))*10));
//         Remainder = (int)Math.IEEEremainder(TempNumber, 10);
         TempNumber = TempNumber / 10;
         for (Count = 0; Count < Decimal[Remainder][MAX_POINTS].x; ++Count)
         {
            DisplayFrame[Digit][Count].x = (short)(Decimal[Remainder][Count].x + xOffset + (10 - Digit) * 3*Scale);
            DisplayFrame[Digit][Count].y = (short)(Decimal[Remainder][Count].y + yOffset);
         }
         DisplayFrame[Digit][MAX_POINTS].x = Decimal[Remainder][MAX_POINTS].x;
  /*************************/
 /* Erase previous score. */
/*************************/
         hdc.setColor(new Color(BackgroundPen));
         win32.Polyline(hdc, OldFrame[Digit], OldFrame[Digit][MAX_POINTS].x);
  /***************************/
 /* Draw the current score. */
/***************************/
         hdc.setColor(new Color(ColourPen));
         win32.Polyline(hdc, DisplayFrame[Digit], DisplayFrame[Digit][MAX_POINTS].x);
   /***************************************/
  /* Plot current score,                 */
 /* next time it will be the old score. */
/***************************************/
         for (Count = 0; Count < DisplayFrame[Digit][MAX_POINTS].x; ++Count)
         {
            OldFrame[Digit][Count].x = DisplayFrame[Digit][Count].x;
            OldFrame[Digit][Count].y = DisplayFrame[Digit][Count].y;
         }
         OldFrame[Digit][MAX_POINTS].x = DisplayFrame[Digit][MAX_POINTS].x;
      }
   }


   public void SetLocation(short NewxOffset, short NewyOffset, short NewScale)
   {
      xOffset = NewxOffset;
      yOffset = NewyOffset;
      Scale = NewScale;

      Decimal[0][MAX_POINTS].x = (short)(6);
      Decimal[0][0].x = (short)(2*Scale);
      Decimal[0][0].y = (short)(0*Scale);
      Decimal[0][1].x = (short)(0*Scale);
      Decimal[0][1].y = (short)(0*Scale);
      Decimal[0][2].x = (short)(0*Scale);
      Decimal[0][2].y = (short)(4*Scale);
      Decimal[0][3].x = (short)(2*Scale);
      Decimal[0][3].y = (short)(4*Scale);
      Decimal[0][4].x = (short)(2*Scale);
      Decimal[0][4].y = (short)(0*Scale);
      Decimal[0][5].x = (short)(0*Scale);
      Decimal[0][5].y = (short)(4*Scale);

      Decimal[1][MAX_POINTS].x = (short)(2);
      Decimal[1][0].x = (short)(1*Scale);
      Decimal[1][0].y = (short)(0*Scale);
      Decimal[1][1].x = (short)(1*Scale);
      Decimal[1][1].y = (short)(4*Scale);

      Decimal[2][MAX_POINTS].x = (short)(6);
      Decimal[2][0].x = (short)(0*Scale);
      Decimal[2][0].y = (short)(0*Scale);
      Decimal[2][1].x = (short)(2*Scale);
      Decimal[2][1].y = (short)(0*Scale);
      Decimal[2][2].x = (short)(2*Scale);
      Decimal[2][2].y = (short)(2*Scale);
      Decimal[2][3].x = (short)(0*Scale);
      Decimal[2][3].y = (short)(2*Scale);
      Decimal[2][4].x = (short)(0*Scale);
      Decimal[2][4].y = (short)(4*Scale);
      Decimal[2][5].x = (short)(2*Scale);
      Decimal[2][5].y = (short)(4*Scale);

      Decimal[3][MAX_POINTS].x = (short)(7);
      Decimal[3][0].x = (short)(0*Scale);
      Decimal[3][0].y = (short)(0*Scale);
      Decimal[3][1].x = (short)(2*Scale);
      Decimal[3][1].y = (short)(0*Scale);
      Decimal[3][2].x = (short)(2*Scale);
      Decimal[3][2].y = (short)(2*Scale);
      Decimal[3][3].x = (short)(0*Scale);
      Decimal[3][3].y = (short)(2*Scale);
      Decimal[3][4].x = (short)(2*Scale);
      Decimal[3][4].y = (short)(2*Scale);
      Decimal[3][5].x = (short)(2*Scale);
      Decimal[3][5].y = (short)(4*Scale);
      Decimal[3][6].x = (short)(0*Scale);
      Decimal[3][6].y = (short)(4*Scale);

      Decimal[4][MAX_POINTS].x = (short)(5);
      Decimal[4][0].x = (short)(0*Scale);
      Decimal[4][0].y = (short)(0*Scale);
      Decimal[4][1].x = (short)(0*Scale);
      Decimal[4][1].y = (short)(2*Scale);
      Decimal[4][2].x = (short)(2*Scale);
      Decimal[4][2].y = (short)(2*Scale);
      Decimal[4][3].x = (short)(2*Scale);
      Decimal[4][3].y = (short)(0*Scale);
      Decimal[4][4].x = (short)(2*Scale);
      Decimal[4][4].y = (short)(4*Scale);

      Decimal[5][MAX_POINTS].x = (short)(6);
      Decimal[5][0].x = (short)(2*Scale);
      Decimal[5][0].y = (short)(0*Scale);
      Decimal[5][1].x = (short)(0*Scale);
      Decimal[5][1].y = (short)(0*Scale);
      Decimal[5][2].x = (short)(0*Scale);
      Decimal[5][2].y = (short)(2*Scale);
      Decimal[5][3].x = (short)(2*Scale);
      Decimal[5][3].y = (short)(2*Scale);
      Decimal[5][4].x = (short)(2*Scale);
      Decimal[5][4].y = (short)(4*Scale);
      Decimal[5][5].x = (short)(0*Scale);
      Decimal[5][5].y = (short)(4*Scale);

      Decimal[6][MAX_POINTS].x = (short)(6);
      Decimal[6][0].x = (short)(2*Scale);
      Decimal[6][0].y = (short)(0*Scale);
      Decimal[6][1].x = (short)(0*Scale);
      Decimal[6][1].y = (short)(0*Scale);
      Decimal[6][2].x = (short)(0*Scale);
      Decimal[6][2].y = (short)(4*Scale);
      Decimal[6][3].x = (short)(2*Scale);
      Decimal[6][3].y = (short)(4*Scale);
      Decimal[6][4].x = (short)(2*Scale);
      Decimal[6][4].y = (short)(2*Scale);
      Decimal[6][5].x = (short)(0*Scale);
      Decimal[6][5].y = (short)(2*Scale);

      Decimal[7][MAX_POINTS].x = (short)(3);
      Decimal[7][0].x = (short)(0*Scale);
      Decimal[7][0].y = (short)(0*Scale);
      Decimal[7][1].x = (short)(2*Scale);
      Decimal[7][1].y = (short)(0*Scale);
      Decimal[7][2].x = (short)(2*Scale);
      Decimal[7][2].y = (short)(4*Scale);

      Decimal[8][MAX_POINTS].x = (short)(7);
      Decimal[8][0].x = (short)(0*Scale);
      Decimal[8][0].y = (short)(0*Scale);
      Decimal[8][1].x = (short)(2*Scale);
      Decimal[8][1].y = (short)(0*Scale);
      Decimal[8][2].x = (short)(2*Scale);
      Decimal[8][2].y = (short)(4*Scale);
      Decimal[8][3].x = (short)(0*Scale);
      Decimal[8][3].y = (short)(4*Scale);
      Decimal[8][4].x = (short)(0*Scale);
      Decimal[8][4].y = (short)(0*Scale);
      Decimal[8][5].x = (short)(0*Scale);
      Decimal[8][5].y = (short)(2*Scale);
      Decimal[8][6].x = (short)(2*Scale);
      Decimal[8][6].y = (short)(2*Scale);

      Decimal[9][MAX_POINTS].x = (short)(6);
      Decimal[9][0].x = (short)(0*Scale);
      Decimal[9][0].y = (short)(4*Scale);
      Decimal[9][1].x = (short)(2*Scale);
      Decimal[9][1].y = (short)(4*Scale);
      Decimal[9][2].x = (short)(2*Scale);
      Decimal[9][2].y = (short)(0*Scale);
      Decimal[9][3].x = (short)(0*Scale);
      Decimal[9][3].y = (short)(0*Scale);
      Decimal[9][4].x = (short)(0*Scale);
      Decimal[9][4].y = (short)(2*Scale);
      Decimal[9][5].x = (short)(2*Scale);
      Decimal[9][5].y = (short)(2*Scale);
   }


   public long GetNumber()
   {
      return NumberValue;
   }


   public void SetNumber(long NewNumber)
   {
      NumberValue = NewNumber;
   }
}
