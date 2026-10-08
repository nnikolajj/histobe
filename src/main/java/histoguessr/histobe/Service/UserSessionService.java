package histoguessr.histobe.Service;

import histoguessr.histobe.Entity.UserSessionEntity;
import histoguessr.histobe.Repository.UserSessionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSessionService {

    Logger logger = LoggerFactory.getLogger(UserSessionService.class);

    @Autowired
    UserSessionRepository repository;

    public int joinGame(UserSessionEntity userSession){
        UserSessionEntity player = new UserSessionEntity();
        player.setPlayerName(userSession.getPlayerName());
        player.setSeedId(userSession.getSeedId());

        repository.save(userSession);

        return userSession.getId();
    }

    public UserSessionEntity updatePoints(int id, int points){

        return repository.updatePoints(id, points);
    }

    public List<UserSessionEntity> getUserSessionByGameSeed(String gameSeed){
        logger.info("Get UserSessions by GameSeed {}", gameSeed);
        List<UserSessionEntity> players = repository.getAllBySeedId(gameSeed);

        if (players == null || players.isEmpty()) {
            logger.info("No UserSessions found for GameSeed {}", gameSeed);
            throw new EntityNotFoundException("No UserSessions found for GameSeed " + gameSeed);
        }

        return players;
    }

}
