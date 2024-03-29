package ar.com.dailyMarket.services.util;

import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ThumbnailUtil {

	public static void generateThumbnail(String img, String thumbnail, int thumbWidth, int thumbHeight, int quality) throws ImageFormatException, IOException, InterruptedException {
		    // load image from INFILE
		    Image image = Toolkit.getDefaultToolkit().getImage(img);
		    MediaTracker mediaTracker = new MediaTracker(new Container());
		    mediaTracker.addImage(image, 0);
		    mediaTracker.waitForID(0);
		    // determine thumbnail size from WIDTH and HEIGHT
		    double thumbRatio = (double)thumbWidth / (double)thumbHeight;
		    int imageWidth = image.getWidth(null);
		    int imageHeight = image.getHeight(null);
		    double imageRatio = (double)imageWidth / (double)imageHeight;
		    if (thumbRatio < imageRatio) {
		      thumbHeight = (int)(thumbWidth / imageRatio);
		    } else {
		      thumbWidth = (int)(thumbHeight * imageRatio);
		    }
		    
		    // draw original image to thumbnail image object and
		    // scale it to the new size on-the-fly
		    BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight, BufferedImage.TYPE_INT_RGB);
		    Graphics2D graphics2D = thumbImage.createGraphics();
		    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		    graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);
		    // save thumbnail image to OUTFILE
		    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(thumbnail));
		    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		    JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbImage);
		   
		    quality = Math.max(0, Math.min(quality, 100));
		    param.setQuality((float)quality / 100.0f, false);
		    encoder.setJPEGEncodeParam(param);
		    encoder.encode(thumbImage);
		    out.close(); 		    
	}
}
