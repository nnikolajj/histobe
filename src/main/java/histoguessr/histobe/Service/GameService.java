package histoguessr.histobe.Service;

import histoguessr.histobe.Entity.GameSeed;
import histoguessr.histobe.Repository.GameSeedRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static histoguessr.histobe.Enum.SeedTypeEnum.HISTO;
import static histoguessr.histobe.Enum.SeedTypeEnum.NARA;
import static histoguessr.histobe.Enum.SeedTypeEnum.ARCH;

@Service
public class GameService {

    @Autowired
    HistoService histoService;

    @Autowired
    NaraService naraService;

    @Autowired
    GameSeedRepository repository;

    private final Logger logger = LoggerFactory.getLogger(GameService.class);

    public GameSeed generateSeed(short type) {
        List<String> enities = new ArrayList<>();

        if (HISTO.getType() == type) {
            for (int i = 0; i< 5; i++){
                enities.add(histoService.getHisto().getId().toString());
            }
        }

        if (NARA.getType() == type) {
            for (int i = 0; i< 5; i++){
                enities.add(String.valueOf(naraService.searchRecordsWithImages(String.valueOf(type)).getId()));
            }
        }

        if (ARCH.getType() == type) {
            for (int i = 0; i< 5; i++){
              //  enities.add(histoService.getHisto().getId().toString());
            }
        }

        GameSeed seed = new GameSeed().setHistoId(enities).setType(type).setDate(LocalDateTime.now()).setShortId(generateId());
        GameSeed generatedSeed = repository.save(seed);

        logger.info("Generated seed for type {} with id {}", type, generatedSeed.getId());

        return generatedSeed;
    }

    public GameSeed getGameSeed(String shortId) {

        GameSeed gameSeed = repository.findGameSeedByShortId(shortId);
        logger.info("Game seed for shortId {} found with id {}", shortId, gameSeed.getId());

        return gameSeed;
    }


    public static String generateId() {
        String possibleChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom RANDOM = new SecureRandom();

        return IntStream.range(0, 6)
                .mapToObj(i -> String.valueOf(possibleChar.charAt(RANDOM.nextInt(possibleChar.length()))))
                .collect(Collectors.joining());
    }
}
