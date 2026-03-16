package histoguessr.histobe.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import histoguessr.histobe.Entity.GameSessionEntity;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
@Service
public class NaraService {

    private final WebClient webClient;
    private final WebClient photonWebClient;
    private final HandlerMapping resourceHandlerMapping;
    private Logger logger = LoggerFactory.getLogger(NaraService.class);
    private int rekursion = 0;

    private final PointsValidation pointsValidation = new PointsValidation();

    @Autowired
    GameSessionService gameSessionService;

    @Autowired
    HistoService histoService;

    @Autowired
    NerLocationService nerLocationService;


    /**
     * Initialisiert den WebClient mit der Basis-URL und dem geheimen API Key im Header.
     *
     * @param builder Der WebClient.Builder (wird von Spring injiziert).
     * @param apiKey  Der API Key, geladen aus application.properties.
     */
    public NaraService(WebClient.Builder builder,
                       @Value("${nara.api.key}") String apiKey, HandlerMapping resourceHandlerMapping) {

        // Setzt die Basis-URL auf den API-Wurzelpfad
        this.webClient = builder
                .baseUrl("https://catalog.archives.gov/api/v2")
                .defaultHeader("x-api-key", apiKey)
                .build();
        this.photonWebClient = builder.baseUrl("https://photon.komoot.io/")
                .defaultHeader("User-Agent", "Mozilla/5.0 (compatible; HistoryGame/1.0)")
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
        int id = random.nextInt(max - min) + min;

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

        mongo.subscribe(value -> System.out.println(),
                Throwable::printStackTrace,
                () -> System.out.println("completed without a value"));

        rawResponse = mongo.block();
        assert rawResponse != null;

        String photoLink = getPhotoUrl(rawResponse);
        int year = getYear(rawResponse);
        String title = getTitle(rawResponse);
        String location = getLocation(title);
        String category;

        if (location.length() >= 2){
            category = "2";
        }
        else {
            category = "3";
        }

        rekursion = 0;

        HistoEntity histo = new HistoEntity()
                .setPicture(photoLink)
                .setDate(LocalDate.of(year, 1, 1))
                .setId((long) id).setTitle(title)
                .setPlace(location)
                .setCategory(category);
        gameSessionService.saveGameSession(histo);
        return histo;
    }

    public int getPoints(long naraId, ValidationRequest validation) {
        HistoEntity histo = getHistoByGameSession(naraId);

        logger.info("Get Pointsvalidation for Nara Histo with id {}", naraId);

        return pointsValidation.validatePoints(histo, validation);
    }

    public HistoEntity getHistoByGameSession(long naraId) {
        HistoEntity histo = new HistoEntity();
        GameSessionEntity gameSessionEntity = gameSessionService.getGameSession(naraId);
        histo.setDate(gameSessionEntity.getDate());
        histo.setPlace(gameSessionEntity.getPlace());
        histo.setPicture(gameSessionEntity.getImageUrl());
        histo.setTitle(gameSessionEntity.getTitle());
        histo.setId(gameSessionEntity.getHistoId());
        histo.setCategory(gameSessionEntity.getCategory());

        return histo;
    }

    public long saveNaraHisto(long naraId) {
        HistoEntity histo = getHistoByGameSession(naraId);
        long id = histoService.saveHisto(histo);

        logger.info("Saved Nara Histo with id {}", id);

        return id;
    }

    public void deleteNaraHisto(long naraId) {
        gameSessionService.deleteGameSession(gameSessionService.getGameSession(naraId).getId());
    }

    private String getPhotoUrl(String response) {
        String beginMarker = "objectUrl\":\"";
        String endMarker = "\"";

        int startIndex = response.indexOf(beginMarker);

        if (startIndex != -1) {
            int urlStartIndex = startIndex + beginMarker.length();

            int urlEndIndex = response.indexOf(endMarker, urlStartIndex);

            if (urlEndIndex != -1) {

                System.out.println(response.substring(urlStartIndex, urlEndIndex));

                return response.substring(urlStartIndex, urlEndIndex);
            }

        }
        logger.error("URL wasnt found");

        if (rekursion <= 4) {
            rekursion++;
            System.out.println("rekursion: " + rekursion);
            searchRecordsWithImages("BildProblem");
        } else {
            System.out.println("Fertig rekursiert " + rekursion);
            throw new EntityNotFoundException("Can't find Nara Picture");
        }

        return "picture not found";
    }

    private String getTitle(String response) {
        String beginMarker = "title\":\"";
        String endMarker = "\",";

        String title = StringUtils.substringBetween(response, beginMarker, endMarker);

        if (title.startsWith("\\\"") && title.endsWith("\\\"")) {
            title = StringUtils.substring(title, 1, title.length() - 2);
        }

        return title;
    }

    private int getYear(String response) {
        String beginMarker = "productionDates\":";
        String endMarker = ",\"variantControlNumbers";

        String prodDate = StringUtils.substringBetween(response, beginMarker, endMarker);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Map<String, Object>>>() {
        }.getType();

        List<Map<String, Object>> yearJson = gson.fromJson(prodDate, listType);

        if (yearJson == null || yearJson.isEmpty()) {

            Pattern pattern = Pattern.compile("\\b1\\d{3}\\b");
            Matcher matcher = pattern.matcher(getTitle(response));

            if (matcher.find()) {
                String yearTitle = matcher.group();
                logger.info("Title Date {}", yearTitle);
                return Integer.parseInt(yearTitle);
            }

        } else {
            String year = yearJson.get(0).get("year").toString();

            logger.info("Production Date {}", year);
            return (int) Double.parseDouble(year);
        }

        if (rekursion <= 4) {
            rekursion++;
            searchRecordsWithImages("JahrProblem");
        } else {
            throw new EntityNotFoundException("Can't find Nara Picture with a year");
        }

        return 0;
    }

    private String getCoordinatesWithPhotonByString(String query) {

        String cleanQuery = query.replaceAll("\\b\\d{4}\\b", "").trim();

        if (!cleanQuery.isEmpty()) {
            Map location = photonWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("api/")
                            .queryParam("q", cleanQuery)
                            .queryParam("limit", 1)
                            .build())
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> Mono.error(new RuntimeException("Photon API Error: " + response.statusCode())))
                    .bodyToMono(Map.class).block();

            List<Map<String, Object>> features = (List<Map<String, Object>>) location.get("features");

            if (features != null && !features.isEmpty()) {
                LinkedHashMap<String, Object> geometry = (LinkedHashMap<String, Object>) features.get(0).get("geometry");

                if (geometry != null && !geometry.isEmpty()) {
                    List<Double> coordinates = (List<Double>) geometry.get("coordinates");
                        String formattedCordsLong = String.format("%.2f", coordinates.get(0));
                        String formattedCordsLat = String.format("%.2f", coordinates.get(1));

                    return (formattedCordsLong + ", " + formattedCordsLat);
                }
            }
        }
        return "";
    }


    private String getLocation(String title) {
        String foundPlaces = nerLocationService.extractLocations(title);

        return getCoordinatesWithPhotonByString(foundPlaces);
    }
}