package histoguessr.histobe.Service;

import histoguessr.histobe.Entity.GameSessionEntity;
import histoguessr.histobe.Entity.HistoEntity;
import histoguessr.histobe.Repository.GameSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class GameSessionService {
    @Autowired
    private GameSessionRepository repository;

    @Async
    public void saveGameSession(HistoEntity histo) {
        GameSessionEntity gameSession = new GameSessionEntity()
                .setDate(histo.getDate())
                .setHistoId(histo.getId())
                .setPlace(histo.getPlace())
                .setImageUrl(histo.getPicture())
                .setTitle(histo.getTitle())
                .setPlace(histo.getPlace())
                .setCategory(histo.getCategory());
        repository.save(gameSession);
    }

    public GameSessionEntity getGameSession(long id) {
        return repository.findGameSessionByHistoId(id);
    }

    public void deleteGameSession(long id) {
        repository.deleteById(id);
    }

}
