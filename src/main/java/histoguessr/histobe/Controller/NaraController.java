package histoguessr.histobe.Controller;

import histoguessr.histobe.Entity.HistoEntity;
import histoguessr.histobe.PointsValidation.ValidationRequest;
import histoguessr.histobe.Service.NaraService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nara")
public class NaraController {

    Logger logger = LoggerFactory.getLogger(HistoController.class.getName());

    @Autowired
    private final NaraService naraService;

    public NaraController(NaraService naraApiService) {
        this.naraService = naraApiService;
    }

    @GetMapping(value = "/search/{q}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HistoEntity search(@PathVariable String q) {

        return naraService.searchRecordsWithImages(q);
    }

    @PostMapping("/histo/{id}/validation")
    public int validatePoints(@PathVariable int id, @RequestBody ValidationRequest validationRequest) {
        logger.info("Get Nara Points with Id {} and place|year: {} | {}", id, validationRequest.getPlace(), validationRequest.getYear());
        return naraService.getPoints(id, validationRequest);
    }

    @GetMapping("/histo/{id}")
    public HistoEntity validatePoints(@PathVariable int id) {
        logger.info("Get Nara Entity with Id {}", id);
        return naraService.getHistoByGameSession(id);
    }
}
