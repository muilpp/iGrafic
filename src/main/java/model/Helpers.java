package model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Helpers {
    private static Logger LOGGER = Logger.getLogger(Helpers.class.getName());

    public static void saveImage(String imageUrl, String destinationFile) {
        InputStream is = null;
        OutputStream os = null;

        try {
            URL url = new URL(imageUrl);
            is = url.openStream();
            os = new FileOutputStream(destinationFile);

            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    // Hi ha un objecte intID dins de la resposta del missatge, però si algun dels missatges és només text, tb incrementa la id
    public static int getLastPictureNumber(File folder) {
        int biggestImgNumber = 1;
        int addition = 0;

        for (final File image : folder.listFiles()) {
            try {
                int imgNumber = Integer.valueOf(image.getName().substring(0, image.getName().indexOf(".")));
                if (imgNumber > biggestImgNumber)
                    biggestImgNumber = imgNumber;
            } catch (NumberFormatException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
                addition++;
            }
        }
        //Retorno el numero de la última + 1 + el numero de possibles errors
        return biggestImgNumber+1+addition;
    }
}