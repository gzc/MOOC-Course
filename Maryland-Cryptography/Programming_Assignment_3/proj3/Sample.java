import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Sample {
    public static void main(String[] args) {
      BufferedReader in;
      int[] ctext = new int[48];
      Oracle oracle;
      int rc, i = 0;

      if(args.length < 1) {
        System.out.println("Usage: java Test <filename>");
        System.exit(-1);
      }

      try {
        in = new BufferedReader(new FileReader(args[0]));

        char[] buf = new char[2];
        while( in.read(buf,0,2) != -1) {
          ctext[i++] = (Integer.parseInt(new String(buf),16));
        }

        oracle = new Oracle();

        oracle.connect();

        rc = oracle.send(ctext,3);
        System.out.printf("Oracle returned: %d\n", rc);

        oracle.disconnect();

      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
}
