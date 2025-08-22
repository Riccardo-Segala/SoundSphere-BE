package backend.service;

import backend.dto.carrello.UpdateCartItemDTO;
import backend.dto.carrello.ResponseCartDTO;
import backend.mapper.CartMapper;
import backend.model.Carrello;
import backend.model.DatiStatici;
import backend.model.Prodotto;
import backend.model.Utente;
import backend.model.embeddable.UtenteProdottoId;
import backend.repository.CarrelloRepository;
import backend.repository.DatiStaticiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CarrelloService extends GenericService<Carrello, UtenteProdottoId> {
    @Autowired
    private CarrelloRepository carrelloRepository;
    @Autowired
    private DatiStaticiRepository datiStaticiRepository;
    @Autowired
    private VantaggioService vantaggioService;

    private final CartMapper cartMapper;
    @Autowired
    private UtenteService utenteService;
    @Autowired
    private ProdottoService prodottoService;

    public CarrelloService(CarrelloRepository repository, CartMapper cartMapper) {
        super(repository); // Passa il repository al costruttore della classe base
        this.cartMapper = cartMapper;
    }

    // Calcola il totale parziale del carrello per un utente specifico
    public double calcolaTotaleParziale(UUID utenteId) {
        //Recupera tutte le righe del carrello per l'utente dal repository
        List<Carrello> righeCarrello = carrelloRepository.findCartByUserId(utenteId);

        if (righeCarrello.isEmpty()) {
            return 0.0;
        }

        double totaleParziale = 0.0;

        for (Carrello riga : righeCarrello) {
            totaleParziale += riga.getProdotto().getPrezzo() * riga.getQuantita();
        }

        return totaleParziale;
    }

    //Calcola il totale finale del carrello con le spese di spedizione ed eventuali sconti
    public double calcolaTotaleFinale(UUID utenteId) {
        double totParziale = calcolaTotaleParziale(utenteId);

        Optional<DatiStatici> spedizioneDati = datiStaticiRepository.findByNome("spedizione");

        // Se il valore non è presente, assumiamo che sia 0.0
        double speseDiSpedizione = spedizioneDati.map(DatiStatici::getValore).orElse(0.0);

        double sconto = vantaggioService.calcolaSconto(utenteId);

        double totaleFinale = totParziale + speseDiSpedizione - sconto;

        // Totale non negativo
        return Math.max(0.0, totaleFinale);
    }

    @Transactional
    public void deleteAllItems(List<Carrello> carrelli) {
        // Questo metodo lavora con le entità gestite,
        // mantenendo il contesto di persistenza sincronizzato.
        carrelloRepository.deleteAll(carrelli);
    }

    // aggiunta di un elemento al carrello o alla wishlist

    public List<Carrello> getWishlistByUtenteId(UUID utenteId) {
        return carrelloRepository.findWishlistByUserId(utenteId);
    }

    public List<Carrello> getCartByUtenteId(UUID utenteId) {
        return carrelloRepository.findCartByUserId(utenteId);
    }

    @Transactional // È un'operazione di scrittura, quindi deve essere transazionale
    public List<ResponseCartDTO> addItemToCart(UUID userId, UpdateCartItemDTO createDTO) {

        // 1. Controllo se l'articolo esiste già nel carrello
        UtenteProdottoId cartId = new UtenteProdottoId(userId, createDTO.prodottoId());
        Optional<Carrello> existingItem = carrelloRepository.findById(cartId);

        if (existingItem.isPresent()) {
            // L'ARTICOLO ESISTE GIÀ: aggiorno la quantità
            Carrello item = existingItem.get();
            item.setQuantita(item.getQuantita() + createDTO.quantita());
            carrelloRepository.save(item);
        } else {
            // L'ARTICOLO È NUOVO: lo creo usando il mapper

            // Uso il mapper per creare l'entità dal DTO
            Carrello newItem = cartMapper.fromCreateDto(createDTO);

            // Il mapper ha già gestito prodottoId, ma devo impostare l'userId
            // perché non era presente nel DTO
            newItem.getId().setUtenteId(userId);

            carrelloRepository.save(newItem);
        }

        // 3. Recupero e restituisco l'intero carrello aggiornato
        List<Carrello> updatedCart = carrelloRepository.findCartByUserId(userId);

        // Uso il mapper per convertire la lista di entità in una lista di DTO
        return updatedCart.stream()
                .map(cartMapper::toDto)
                .toList();
    }

    @Transactional
    public ResponseCartDTO updateItemInCart(UUID userId, UpdateCartItemDTO dto) {
        UtenteProdottoId cartId = new UtenteProdottoId(userId, dto.prodottoId());

        // 1. Cercoo l'articolo nel carrello, ma senza lanciare un'eccezione se non c'è.
        //    ottengo un Optional che può essere pieno o vuoto.
        Optional<Carrello> optionalItem = carrelloRepository.findById(cartId);

        Carrello itemToSave; // Dichiaro una variabile per l'oggetto che salvo alla fine

        if (optionalItem.isPresent()) {
            // --- CASO 1: L'ARTICOLO ESISTE GIÀ (AGGIORNA) ---

            // Estraggo l'articolo esistente dall'Optional
            Carrello existingItem = optionalItem.get();

            // Controllo la quantità specificata nel DTO
            if (dto.quantita() > 0) {
                // Se la quantità è positiva, aggiorno il valore
                existingItem.setQuantita(dto.quantita());
                itemToSave = existingItem;

            } else {
                existingItem.setQuantita(0);
                ResponseCartDTO responseDto = cartMapper.toDto(existingItem);
                // Se la quantità è 0 o negativa, rimuovo l'articolo
                carrelloRepository.delete(existingItem);
                return responseDto;
            }

        } else {
            // --- CASO 2: L'ARTICOLO NON ESISTE (CREA) ---

            // Uso il mapper per creare una nuova entità Carrello a partire dal DTO
            Carrello newItem = cartMapper.fromCreateDto(dto);
            Utente utente = utenteService.getById(userId);
            Prodotto prodotto = prodottoService.getById(dto.prodottoId());

            // Imposto l'ID completo. Il prodottoId è già stato mappato,
            // ma devo aggiungere l'userId che proviene dal contesto di sicurezza.
            newItem.getId().setUtenteId(userId);
            newItem.setProdotto(prodotto);
            newItem.setUtente(utente);

            newItem.setWishlist(false);

            // Assegno il nuovo articolo alla variabile
            itemToSave = newItem;
        }

        // 2. A questo punto, 'itemToSave' contiene o l'articolo aggiornato o quello nuovo.
        //    Lo salvo nel database con un'unica operazione.

        Carrello savedItem = carrelloRepository.save(itemToSave);

        // 3. Restituisco il DTO dell'articolo appena salvato, come richiesto.
        return cartMapper.toDto(savedItem);
    }

    public List<ResponseCartDTO> getAllCartItemsByUserId(UUID userId) {
        // 1. Recupero tutte le righe del carrello per l'utente specificato
        List<Carrello> cartItems = carrelloRepository.findCartByUserId(userId);

        // 2. Uso il mapper per convertire le entità in DTO
        return cartItems.stream()
                .map(cartMapper::toDto)
                .toList();
    }
}

