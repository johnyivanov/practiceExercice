import java.io.*;
import java.net.URL;
import java.net.HttpURLConnection;

public class FileDownloader {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java FileDownloader <URL> <outputFile>");
            System.exit(1);
        }

        String fileURL = args[0];
        String outputFile = args[1];

        try {
            downloadFile(fileURL, outputFile);
            System.out.println("File downloaded successfully!");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void downloadFile(String fileURL, String outputFile) throws IOException {
        URL url = new URL(fileURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (InputStream inputStream = connection.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(outputFile)) {

                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        } else {
            throw new IOException("HTTP Error: " + responseCode);
        }
    }
}
