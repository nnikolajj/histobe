package histoguessr.histobe.Controller;

import histoguessr.histobe.Entity.HistoEntity;
import histoguessr.histobe.Service.NaraService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/nara")
public class NaraController {

    private final NaraService naraService;

    public NaraController(NaraService naraApiService) {
        this.naraService = naraApiService;
    }

    /**
     * Frontend-Endpoint: Leitet die Suchanfrage an den NaraApiService weiter.
     * http://localhost:8080/api/nara/search?q=declaration%20of%20independence
     */
    @GetMapping(value = "/search/{q}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HistoEntity search(@PathVariable String q) {
        System.out.println("Drin " + q);

        return naraService.searchRecordsWithImages(q);
    }
}
