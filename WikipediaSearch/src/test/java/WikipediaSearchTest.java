import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.powermock.api.mockito.PowerMockito;

class WikipediaSearchTest {

    @Test
    void urlConnection() throws IOException {
        String query = "java";
        URL url = new URL("https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch=" + query + "&format=json");
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();

        int responseCode = huc.getResponseCode();

        Assert.assertEquals(HttpURLConnection.HTTP_OK, responseCode);
    }
    @Test
    void getBufferedReaderResponse() throws IOException {
        String query = "java";
        URL url = new URL("https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch=" + query + "&format=json");
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(huc.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Assert.assertNotNull(response);
    }
    @Test
    @Disabled
    void querySearch() {

    }

    @Test
    @Disabled
    void articleSearch() {
    }
}


