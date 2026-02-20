package histoguessr.histobe.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import histoguessr.histobe.Entity.HistoEntity;
import histoguessr.histobe.PointsValidation.PointsValidation;
import histoguessr.histobe.PointsValidation.ValidationRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.HandlerMapping;
import reactor.core.publisher.Mono;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
@Service
public class NaraService {

    private final WebClient webClient;
    private final HandlerMapping resourceHandlerMapping;
    private Logger logger = LoggerFactory.getLogger(NaraService.class);
    private int rekursion = 0;
    private int year = 0;

    private final PointsValidation pointsValidation = new PointsValidation();


    /**
     * Initialisiert den WebClient mit der Basis-URL und dem geheimen API Key im Header.
     * @param builder Der WebClient.Builder (wird von Spring injiziert).
     * @param apiKey Der API Key, geladen aus application.properties.
     */
    public NaraService(WebClient.Builder builder,
                       @Value("${nara.api.key}") String apiKey, HandlerMapping resourceHandlerMapping) {

        // Setzt die Basis-URL auf den API-Wurzelpfad
        this.webClient = builder
                .baseUrl("https://catalog.archives.gov/api/v2")
                // Fügt den geheimen API Key als Header hinzu (für jede Anfrage)
                .defaultHeader("x-api-key", apiKey)
                .build();
        this.resourceHandlerMapping = resourceHandlerMapping;
    }

    /**
     * Führt eine Suche in der NARA API durch und filtert nach Datensätzen mit Digitalobjekten (Bildern).
     *
     * @param query Der Suchbegriff (z.B. "World War II")
     * @return Den JSON-Body der API-Antwort als Mono<String>
     */
    public HistoEntity searchRecordsWithImages(String query) {

        Random random = new Random();
        int min = 530708;
        int max = 531472;
        int id = random.nextInt(max-min) + min;

        String rawResponse;

        Mono<String> mongo = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/records/search")
                        .queryParam("naId_is", id)
                        .queryParam("includeExtractedText", "true")
                        .queryParam("limit", "1")
                        .build())
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> Mono.error(new RuntimeException("NARA API Error: " + response.statusCode())))
                .bodyToMono(String.class);

        mongo.subscribe(value -> System.out.println(value),
                Throwable::printStackTrace,
                () -> System.out.println("completed without a value"));

        rawResponse = mongo.block();

        assert rawResponse != null;
        String photoLink = getPhotoUrl(rawResponse);
        int year = getYear(rawResponse);
        this.year = year;

        return new HistoEntity().setPicture(photoLink).setDate(LocalDate.of(year, 1, 1));
    }

    private String getPhotoUrl(String response) {
        String beginMarker = "objectUrl\":\"";
        String endMarker = "\"";

        int startIndex = response.indexOf(beginMarker);

        if (startIndex != -1) {
            int urlStartIndex = startIndex + beginMarker.length();

            int urlEndIndex = response.indexOf(endMarker, urlStartIndex);

            if (urlEndIndex != -1) {
                return response.substring(urlStartIndex, urlEndIndex);
            }

    }
        logger.error("URL wasnt found");

        if (rekursion <= 4){
            rekursion++;
            searchRecordsWithImages("BildProblem");
        }
        else {
            throw new EntityNotFoundException("Can't find Nara Picture");
        }

        return "picture not found";
    }

    private int getYear(String response) {
        String beginMarker = "productionDates\":";
        String endMarker = ",\"variantControlNumbers";

        String beginMarkerTitle = "title\":";
        String endMarkerTitle = "\",\"";

        String prodDate = StringUtils.substringBetween(response, beginMarker, endMarker);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Map<String, Object>>>(){}.getType();

        List<Map<String, Object>> yearJson = gson.fromJson(prodDate, listType);

        if (yearJson.isEmpty()) {
            String title = StringUtils.substringBetween(response, beginMarkerTitle, endMarkerTitle);

            Pattern pattern = Pattern.compile("\\b1\\d{3}\\b");
            Matcher matcher = pattern.matcher(title);

            if (matcher.find()) {
                String yearTitle = matcher.group();
                logger.info("Title Date {}", yearTitle);
                return Integer.parseInt(yearTitle);
            }

        }else {
            String year = yearJson.get(0).get("year").toString();

            logger.info("Production Date {}", year);
            return (int) Double.parseDouble(year);
        }

        if (rekursion <= 4){
            rekursion++;
            searchRecordsWithImages("JahrProblem");
        }
        else {
            throw new EntityNotFoundException("Can't find Nara Picture with a year");
        }

        return 0;
    }

    public int getPoints(long id, ValidationRequest validation) {
        HistoEntity histo = new HistoEntity();
        histo.setDate(LocalDate.of(year, 1, 1));

        logger.info("Get Pointsvalidation for Histo with id {}", id);

        return pointsValidation.validatePoints(histo, validation);
    }
}