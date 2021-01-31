package groovycalamari.podcast

import com.mpatric.mp3agic.Mp3File

final class Mp3FileUtils {

    static Mp3File mp3FileFromUrl(String enclosureUrl) {
        return new Mp3File(fileFromUrl(enclosureUrl))
    }

    private static File fileFromUrl(String urlString) {
        URL url = new URL(urlString)
        URLConnection connection = url.openConnection()
        InputStream inputStream = connection.getInputStream()
        File file  = File.createTempFile('enclosure', '.mp3')
        file.deleteOnExit()
        FileOutputStream fos = new FileOutputStream(file);
        byte[] buf = new byte[512]
        while (true) {
            int len = inputStream.read(buf)
            if (len == -1) {
                break
            }
            fos.write(buf, 0, len)
        }
        inputStream.close()
        fos.flush()
        fos.close()
        file
    }
}
