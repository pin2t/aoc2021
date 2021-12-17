import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scanner {
    private static final Pattern num = Pattern.compile("\\-?\\d+");
    private final BufferedInputStream in;
    private int c;

    public Scanner(InputStream stream) {
       in = new BufferedInputStream(stream);
       try {
          c  = (char)in.read();
       } catch (IOException e) {
          c  = -1;
       }
    }
 
    public boolean hasNext() {
       return c != -1;
    }
 
    public String next() {
       StringBuilder sb = new StringBuilder();
       try {
          while (c <= ' ') {
             c = in.read();
          } 
          while (c > ' ') {
             sb.append((char)c);
             c = in.read();
          }
       } catch (IOException e) {
          c = -1;
          return "";
       }
       return sb.toString();
    }
 
    public String nextLine() {
       StringBuilder sb = new StringBuilder();
       try {
          while (c != '\n' && c != -1) {
             sb.append((char)c);
             c = in.read();
          }
          if (c != -1)
            c = in.read();
       } catch (IOException e) {
          c = -1;
          return "";
       }
       return sb.toString();   
    }
 
    public int nextInt() {
       String s = next();
       try {
          return Integer.parseInt(s);
       } catch (NumberFormatException e) {
          return 0; //throw new Error("Malformed number " + s);
       }
    }
 
    public double nextDouble() {
       return Double.parseDouble(next());
    }
 
    public long nextLong() {
       return Long.parseLong(next());
    } 
 
    public void useLocale(int l) {}

    /**
     * reads integer numbers separated by non-digit at the current line 
     * only at one line
     * @return
     */
    public int[] readInts() {
      Matcher matcher = num.matcher(nextLine());
      List<Integer> result = new ArrayList<>();
      while (matcher.find()) {
          result.add(Integer.valueOf(matcher.group()));
      }
      return result.stream().mapToInt(i->i).toArray();
    }
 }    
