package backend.service;

import backend.model.MetodoPagamento;
import backend.repository.MetodoPagamentoRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MetodoPagamentoService extends GenericService<MetodoPagamento, UUID> {
    public MetodoPagamentoService(MetodoPagamentoRepository repository) {
        super(repository); // Passa il repository al costruttore della classe base
    }
}
