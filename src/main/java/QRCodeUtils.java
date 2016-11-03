import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by jack on 2016-11-03.
 */
public class QRCodeUtils {
    public static Image EncodeToQRCode(String input, int width, int height) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>(2);
            hints.put(EncodeHintType.CHARACTER_SET, "ISO-8859-1");
            BitMatrix result = new MultiFormatWriter().encode(input, BarcodeFormat.QR_CODE, width, height, hints);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < result.getHeight(); i++) {
                for (int j = 0; j < result.getWidth(); j++) {
                    image.setRGB(i, j, result.get(i, j) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            return SwingFXUtils.toFXImage(image, null);

        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String DecodeToImage(Image image) throws NotFoundException {
        HashMap<DecodeHintType, String> hint = new HashMap<DecodeHintType, String>();
        hint.put(DecodeHintType.CHARACTER_SET, "ISO-8859-1");
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(SwingFXUtils.fromFXImage(image, null))));
        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, hint);
        return qrCodeResult.getText();
    }
}
