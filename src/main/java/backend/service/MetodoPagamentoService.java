package backend.service;

import backend.dto.metodo_pagamento.CreatePaymentMethodDTO;
import backend.dto.metodo_pagamento.ResponsePaymentMethodDTO;
import backend.dto.metodo_pagamento.UpdatePaymentMethodDTO;
import backend.mapper.PaymentMethodMapper;
import backend.model.MetodoPagamento;
import backend.model.Utente;
import backend.repository.MetodoPagamentoRepository;
import backend.repository.UtenteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class MetodoPagamentoService extends GenericService<MetodoPagamento, UUID> {
    @Autowired
    private MetodoPagamentoRepository metodoPagamentoRepository;
    @Autowired
    private PaymentMethodMapper paymentMethodMapper;
    @Autowired
    private UtenteRepository utenteRepository;

    public MetodoPagamentoService(MetodoPagamentoRepository repository) {
        super(repository); // Passa il repository al costruttore della classe base
    }

    public List<ResponsePaymentMethodDTO> getAllPaymentMethodItemsByUserId(UUID userId) {
        // 1. Recupera tutti i metodi di pagamento per l'utente specificato
        List<MetodoPagamento> methodItems = metodoPagamentoRepository.findByUtente_Id(userId);

        // 2. Uso il mapper per convertire le entità in DTO
        return methodItems.stream()
                .map(paymentMethodMapper::toDto)
                .toList();
    }

    @Transactional
    public MetodoPagamento create(CreatePaymentMethodDTO dto) {
        // 1. Validare e recuperare l'utente
        Utente utente = utenteRepository.findById(dto.utenteId())
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con id: " + dto.utenteId()));

        // 2. Recuperare i metodi esistenti
        List<MetodoPagamento> metodiEsistenti = metodoPagamentoRepository.findByUtente_Id(utente.getId());

        // 3. Creare il nuovo metodo e associarlo all'utente (Perfetto)
        MetodoPagamento nuovoMetodo = paymentMethodMapper.fromCreateDto(dto);
        nuovoMetodo.setUtente(utente);

        // 4. Logica 'main'
        if (metodiEsistenti.isEmpty()) {
            // Se non ci sono metodi, questo DEVE essere il principale
            nuovoMetodo.setMain(true);
        } else if (nuovoMetodo.isMain()) {
            // Se il nuovo è 'main', retrocedi tutti gli altri
            // Non serve salvare: @Transactional si occuperà dell'update (dirty checking)
            metodiEsistenti.forEach(metodo -> metodo.setMain(false));
        }

        // 5. Salvare SOLO la nuova entità
        return metodoPagamentoRepository.save(nuovoMetodo);

    }

    @Transactional
    public MetodoPagamento update(UUID id, UpdatePaymentMethodDTO dto) {
        // 1. Trova il metodo di pagamento esistente
        MetodoPagamento metodoEsistente = metodoPagamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Metodo di pagamento non trovato con id: " + id));

        boolean eraMainInizialmente = metodoEsistente.isMain();
        UUID utenteId = metodoEsistente.getUtente().getId();

        // 2. RECUPERA GLI ALTRI METODI UNA SOLA VOLTA
        List<MetodoPagamento> altriMetodi = metodoPagamentoRepository.findByUtente_Id(utenteId)
                .stream()
                .filter(metodo -> !metodo.getId().equals(id)) // Escludiamo sempre quello attuale
                .toList();

        // 3. Applica le modifiche parziali dal DTO
        paymentMethodMapper.partialUpdateFromUpdate(dto, metodoEsistente);

        // 4. LOGICA DI BUSINESS

        // Caso A: Promozione a 'main'
        if (metodoEsistente.isMain() && !eraMainInizialmente) {
            // Retrocedi tutti gli altri. Non serve salvare, ci pensa @Transactional.
            altriMetodi.forEach(metodo -> metodo.setMain(false));
        }
        // Caso B: Retrocessione da 'main'
        else if (!metodoEsistente.isMain() && eraMainInizialmente) {
            if (altriMetodi.isEmpty()) {
                // Non può essere retrocesso se è l'unico. Forza il ripristino.
                metodoEsistente.setMain(true);
            } else {
                // Promuovi il primo degli altri a nuovo principale
                altriMetodi.get(0).setMain(true);
            }
        }
        // 5. Salviamo l'entità aggiornata
        return metodoPagamentoRepository.save(metodoEsistente);
    }

    @Transactional
    public void delete(UUID id) {
        // 1. Trova il metodo da cancellare. Se non esiste, lancia un'eccezione 404.
        MetodoPagamento metodoDaCancellare = metodoPagamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Metodo di pagamento non trovato con id: " + id));

        // 2. Se il metodo che stiamo cancellando è quello principale, gestisci la successione.
        if (metodoDaCancellare.isMain()) {
            UUID utenteId = metodoDaCancellare.getUtente().getId();

            // Cerca i possibili candidati a diventare il nuovo metodo principale.
            List<MetodoPagamento> altriMetodi = metodoPagamentoRepository.findByUtente_Id(utenteId)
                    .stream()
                    .filter(metodo -> !metodo.getId().equals(id)) // Escludi quello corrente
                    .toList();

            // Se esiste almeno un altro metodo...
            if (!altriMetodi.isEmpty()) {
                // ...promuovi il primo della lista come nuovo principale.
                MetodoPagamento nuovoMain = altriMetodi.get(0);
                nuovoMain.setMain(true);
                // L'update su 'nuovoMain' verrà gestito automaticamente da @Transactional.
            }
            // Se 'altriMetodi' è vuota, non facciamo nulla.
        }

        // 3. Esegui la cancellazione.
        // Questa istruzione viene eseguita in tutti i casi validi:
        // - Se il metodo non era 'main'.
        // - Se era 'main' e un successore è stato promosso.
        // - Se era 'main' ed era l'ultimo rimasto.
        metodoPagamentoRepository.delete(metodoDaCancellare);
    }
}
