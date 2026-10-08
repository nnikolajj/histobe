package histoguessr.histobe.Controller;

import histoguessr.histobe.Entity.GameSeed;
import histoguessr.histobe.Entity.UserSessionEntity;
import histoguessr.histobe.Service.GameService;
import histoguessr.histobe.Service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/game")
public class GameController {

    Logger logger = LoggerFactory.getLogger(HistoController.class.getName());

    @Autowired
    private GameService service;

    @Autowired
    private UserSessionService userSessionService;

    @PostMapping("/seed/{type}")
    public GameSeed startGame(@PathVariable short type) {
        logger.info("Generate Seed with type {}", type);
        return service.generateSeed(type);
    }

    @GetMapping("/{id}")
    public GameSeed getGameBySeedById(@PathVariable String id) {
        logger.info("Get Histo with id {}", id);
        return service.getGameSeed(id);
    }

    @PostMapping("/join")
    public int joinGame(@RequestBody UserSessionEntity userSession) {
        logger.info("Player {} joins Game {}", userSession.getPlayerName(), userSession.getSeedId());

        return userSessionService.joinGame(userSession);
    }

    @PutMapping("/score/{id}/{points}")
    public UserSessionEntity getPlayerScore(@PathVariable int id, @PathVariable int points) {
        logger.info("Update Score for Entity {} to {}", id, points);

        return userSessionService.updatePoints(id, points);
    }

    @GetMapping("/playerseed/{gameseed}")
    public List<UserSessionEntity> getUserSessionsByGameSeed(@PathVariable String gameseed) {
        logger.info("Get UserSessions with GameSeed {}", gameseed);

        return userSessionService.getUserSessionByGameSeed(gameseed);

    }
}
