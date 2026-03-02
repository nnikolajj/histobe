package histoguessr.histobe.Service;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class NerLocationService {

    public String extractLocations(String text) {
        List<String> locations = new ArrayList<>();

        try (InputStream tokenModelIn = getClass().getResourceAsStream("/binFiles/en-token.bin");
             InputStream locationModelIn = getClass().getResourceAsStream("/binFiles/en-ner-location.bin")) {

            TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);
            TokenizerME tokenizer = new TokenizerME(tokenModel);
            String[] tokens = tokenizer.tokenize(text);

            TokenNameFinderModel locModel = new TokenNameFinderModel(locationModelIn);
            NameFinderME nameFinder = new NameFinderME(locModel);

            Span[] nameSpans = nameFinder.find(tokens);

            for (Span span : nameSpans) {
                StringBuilder location = new StringBuilder();
                for (int i = span.getStart(); i < span.getEnd(); i++) {
                    location.append(tokens[i]).append(" ");
                }
                locations.add(location.toString().trim());
            }

            nameFinder.clearAdaptiveData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String places = "";

        if (!locations.isEmpty() && locations.size() >= 2) {
            locations = locations.subList(locations.size() - 2, locations.size());
            places = locations.get(0) + "," + locations.get(1);
        }
        else if (!locations.isEmpty()) {
            places = locations.getFirst();
        }

        return places;
    }
}