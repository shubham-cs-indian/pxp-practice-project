package problem;

public class Exceptional
{
    public static void main (String[] args)
    {
         method();
     }
     public static int method()
    {
         try
        {
            System.out.println("try");
            int x =33/0;
            System.out.println(11);
            return 11;
       }
        catch(Exception e)
     {
             System.out.println("catch");
             System.out.println(17);
          return 17;
       }
        finally
        {
            System.out.println("finally");
            System.out.println(88);
            return 88;
        }
   }
}