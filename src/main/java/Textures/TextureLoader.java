package Textures;

import de.matthiasmann.twl.utils.PNGDecoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class TextureLoader {

    public final ByteBuffer buf;
    public final int tWidth;
    public final int tHeight;

    private TextureLoader(ByteBuffer buf, int tWidth, int tHeight) {
        this.buf = buf;
        this.tWidth = tWidth;
        this.tHeight = tHeight;
    }

    public static TextureLoader load(String texPath) {

        ByteBuffer buf = null;
        int tWidth = 0;
        int tHeight = 0;

        // Open the PNG file as an InputStream
        try {// Link the PNG decoder to this stream
            InputStream in = new FileInputStream(texPath);
            PNGDecoder decoder = new PNGDecoder(in);

            // Get the width and height of the textureLoader
            tWidth = decoder.getWidth();
            tHeight = decoder.getHeight();


            // Decode the PNG file in a ByteBuffer
            buf = ByteBuffer.allocateDirect(
                    4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            buf.flip();

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        return new TextureLoader(buf,tWidth,tHeight);
    }
}
