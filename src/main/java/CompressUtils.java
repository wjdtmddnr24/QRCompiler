import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.http.util.ByteArrayBuffer;
import org.tukaani.xz.*;

import java.io.*;

/**
 * Created by jack on 2016-11-03.
 */
public class CompressUtils {
    public static byte[] compressText(String text) throws IOException {
//        System.out.println(text.length() + "original : " + text);
        byte[] result = null;
        byte[] compressTarget = text.getBytes("ISO-8859-1");
        LZMA2Options options = new LZMA2Options();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(compressTarget);
        options.setPreset(6);
        XZOutputStream out = new XZOutputStream(buffer, options);
        byte[] buf = new byte[10];
        int size;
        int progress = 0;
        while ((size = input.read(buf)) != -1) {
            out.write(buf, 0, size);
        }
        out.finish();
        byte[] original = buffer.toByteArray();
        String rr = Base64.encode(buffer.toByteArray()); //base64로 인코딩
        rr = new String(Base64.decode(rr));
        byte[] results = rr.getBytes("ISO-8859-1");

        result = buffer.toByteArray();
        return result;
    }

    public static String decompressText(String text) throws IOException {
        ByteArrayInputStream c_input = new ByteArrayInputStream(text.getBytes("ISO-8859-1"));
//                ByteArrayInputStream c_input = new ByteArrayInputStream(results);
        XZInputStream xz_input = new XZInputStream(c_input);
        StringBuffer sb = new StringBuffer();
        int read;
        ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(6);
        byte[] buf = new byte[10];

        while ((read = xz_input.read(buf)) != -1)
            byteArrayBuffer.append(buf, 0, read);
        String asdf = sb.toString();
//        System.out.println("decompressed? : " + new String(byteArrayBuffer.toByteArray(), "EUC-KR"));
        return new String(byteArrayBuffer.toByteArray(), "ISO-8859-1");
    }

    public static String addMarker(byte[] compressed) throws UnsupportedEncodingException {
        String encodeValue = "TCQREncoded:" + (char) 0x04 + new String(compressed, "ISO-8859-1");
        return encodeValue;
    }

    public static String removeMarker(String content) {
        String result = content.substring(("TCQREncoded:" + (char) 0x04).length());
        return result;
    }

    public static void main(String[] args) throws IOException {
       /* String text = "장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가장되는건가";
        System.out.println(text.length() + "original : " + text);
        byte[] result = null;
        byte[] compressTarget = text.getBytes("EUC-KR");
        LZMA2Options options = new LZMA2Options();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(compressTarget);
        options.setPreset(7);
        XZOutputStream out = new XZOutputStream(buffer, options);
        byte[] buf = new byte[10];
        int size;
        int progress = 0;
        while ((size = input.read(buf)) != -1) {
            out.write(buf, 0, size);
        }
        out.finish();

        byte[] original = buffer.toByteArray();
//                String rr = "TCQREncoded:" + (char) 0x04 + new String(buffer.toByteArray(), "ISO-8859-1");
        String rr = Base64.encode(buffer.toByteArray());
        rr = new String(Base64.decode(rr));
        byte[] results = rr.getBytes("ISO-8859-1");

        result = buffer.toByteArray();

        System.out.println(new String(result).length() + "compressed:\n " + new String(result));


        ByteArrayInputStream c_input = new ByteArrayInputStream(buffer.toByteArray());
//                ByteArrayInputStream c_input = new ByteArrayInputStream(results);
        XZInputStream xz_input = new XZInputStream(c_input);
        StringBuffer sb = new StringBuffer();
        int read;
        ByteArrayBuffer byteArrayBuffer = new ByteArrayBuffer(6);

        while ((read = xz_input.read(buf)) != -1)
            byteArrayBuffer.append(buf, 0, read);

//        sb.append(new String(buf, 0, read));

        String asdf = sb.toString();
        System.out.println("decompressed? : " + new String(byteArrayBuffer.toByteArray(), "EUC-KR"));
*/
        byte[] compressed = compressText("HELLOWORLD");

        String decompressed = decompressText(new String(compressed, "ISO-8859-1"));
        System.out.println("TEST:" + decompressed);
    }
}
