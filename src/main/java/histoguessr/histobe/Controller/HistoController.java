package histoguessr.histobe.Controller;

import histoguessr.histobe.Entity.HistoEntity;
import histoguessr.histobe.PointsValidation.ValidationRequest;
import histoguessr.histobe.Service.HistoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class HistoController {

    Logger logger = LoggerFactory.getLogger(HistoController.class.getName());

    @Autowired
    private HistoService service;


    @GetMapping("/histo/{id}")
    public HistoEntity getHisto(@PathVariable int id) {
        logger.info("Get Histo with id {}", id);
        return service.getHisto(id);
    }

    @GetMapping("/histo/any")
    public HistoEntity getHisto() {
        logger.info("Get Random Histo");
        return service.getHisto();
    }

    @PostMapping("/histo/{id}/validation")
    public int validatePoints(@PathVariable int id, @RequestBody ValidationRequest validationRequest) {
        logger.info( "Get Points with id {} mit daten: {} | {}", id, validationRequest.getPlace(), validationRequest.getYear());
        return service.getPoints(id, validationRequest);
    }
}
