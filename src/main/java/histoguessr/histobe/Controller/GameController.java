package histoguessr.histobe.Controller;

import histoguessr.histobe.Entity.GameSeed;
import histoguessr.histobe.Service.GameService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/game")
public class GameController {

    Logger logger = LoggerFactory.getLogger(HistoController.class.getName());

    @Autowired
    private GameService service;

    @GetMapping("/{type}")
    public GameSeed startGame(@PathVariable short type) {
        logger.info("Generate Seed with type {}", type);
        return service.generateSeed(type);
    }

    @GetMapping("/{id}")
    public GameSeed getGameBySeedById(@PathVariable String id) {
        logger.info("Get Histo with id {}", id);
        return service.getGameSeed(id);
    }
}
