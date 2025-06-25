import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherClient {

    public static void main(String[] args) {
        // Example: Latitude and Longitude for New York
        double latitude = 40.7128;
        double longitude = -74.0060;

        // Open-Meteo API URL
        String urlString = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude
                + "&longitude=" + longitude + "&current_weather=true";

        try {
            // Create URL and open connection
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Set method
            conn.setRequestMethod("GET");

            // Get the response code
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                // Read response line by line
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Print structured JSON response
                System.out.println("📦 Weather API Response:");
                System.out.println(prettyPrintJSON(response.toString()));

            } else {
                System.out.println("❌ GET request failed. Response Code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Simple formatter for JSON string (manual for standard Java)
    public static String prettyPrintJSON(String json) {
        StringBuilder pretty = new StringBuilder();
        int indent = 0;
        boolean inQuotes = false;

        for (char c : json.toCharArray()) {
            switch (c) {
                case '"':
                    pretty.append(c);
                    inQuotes = !inQuotes;
                    break;
                case '{':
                case '[':
                    pretty.append(c);
                    if (!inQuotes) {
                        pretty.append("\n");
                        indent++;
                        pretty.append("  ".repeat(indent));
                    }
                    break;
                case '}':
                case ']':
                    if (!inQuotes) {
                        pretty.append("\n");
                        indent--;
                        pretty.append("  ".repeat(indent));
                    }
                    pretty.append(c);
                    break;
                case ',':
                    pretty.append(c);
                    if (!inQuotes) {
                        pretty.append("\n");
                        pretty.append("  ".repeat(indent));
                    }
                    break;
                case ':':
                    pretty.append(c);
                    if (!inQuotes) {
                        pretty.append(" ");
                    }
                    break;
                default:
                    pretty.append(c);
            }
        }

        return pretty.toString();
    }
}