package histoguessr.histobe.Service;


import histoguessr.histobe.Entity.HistoEntity;
import histoguessr.histobe.PointsValidation.ValidationRequest;
import histoguessr.histobe.PointsValidation.PointsValidation;
import histoguessr.histobe.Repository.HistoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class HistoService {

    final static int ONE = 1;

    @Autowired
    private HistoRepository repository;

    private final PointsValidation pointsValidation = new PointsValidation();

    public HistoEntity getHisto(long id){

        return repository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "HistoEntity not found"));
    }

    public HistoEntity getHisto(){
        long histoDbSize = repository.count();
        long randomId = (long) (Math.random()*histoDbSize + ONE);

        System.out.println(randomId);

        HistoEntity histo = repository.findById(randomId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "HistoEntity not found"));

        return new HistoEntity().setId(histo.getId()).setPicture(histo.getPicture()).setCategory(histo.getCategory()).setCategory(histo.getCategory());}

    public int getPoints(long id, ValidationRequest validation) {
        HistoEntity histo = repository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "HistoEntity not found"));

       return pointsValidation.validatePoints(histo, validation);
    }
}

