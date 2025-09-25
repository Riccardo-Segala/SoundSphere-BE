package backend.service;

import backend.dto.carrello.UpdateCartItemDTO;
import backend.dto.carrello.ResponseCartDTO;
import backend.mapper.CartMapper;
import backend.model.Carrello;
import backend.model.Prodotto;
import backend.model.Utente;
import backend.model.embeddable.UtenteProdottoId;
import backend.repository.CarrelloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CarrelloService extends GenericService<Carrello, UtenteProdottoId> {
    private final CarrelloRepository carrelloRepository;
    @Autowired
    private VantaggioService vantaggioService;

    private final CartMapper cartMapper;
    @Autowired
    private UtenteService utenteService;
    @Autowired
    private ProdottoService prodottoService;

    public CarrelloService(CarrelloRepository carrelloRepository, CartMapper cartMapper) {
        super(carrelloRepository); // Passa il repository al costruttore della classe base
        this.carrelloRepository = carrelloRepository;
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

    //Calcola il totale finale del carrello con eventuali sconti
    public double calcolaTotaleFinale(UUID utenteId) {
        double totParziale = calcolaTotaleParziale(utenteId);

        double scontoPercentuale = vantaggioService.calcolaSconto(utenteId);

        double sconto = (scontoPercentuale / 100.0) * totParziale;

        double totaleFinale = totParziale - sconto;

        // Totale non negativo
        return Math.max(0.0, totaleFinale);
    }

    @Transactional
    public void deleteAllItems(List<Carrello> carrelli) {
        // Questo metodo lavora con le entità gestite,
        // mantenendo il contesto di persistenza sincronizzato.
        carrelloRepository.deleteAll(carrelli);
    }

    public List<Carrello> getWishlistByUtenteId(UUID utenteId) {
        return carrelloRepository.findWishlistByUserId(utenteId);
    }

    public List<Carrello> getCartByUtenteId(UUID utenteId) {
        return carrelloRepository.findCartByUserId(utenteId);
    }

    @Transactional
    public List<ResponseCartDTO> addItemToCart(UUID userId, UpdateCartItemDTO createDTO) {

        // Controllo se l'articolo esiste già nel carrello
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

        // Recupero e restituisco l'intero carrello aggiornato
        List<Carrello> updatedCart = carrelloRepository.findCartByUserId(userId);

        // Uso il mapper per convertire la lista di entità in una lista di DTO
        return updatedCart.stream()
                .map(cartMapper::toDto)
                .toList();
    }

    @Transactional
    public ResponseCartDTO updateItemInCart(UUID userId, UpdateCartItemDTO dto) {
        // --- Validazione dell'input ---
        if (dto.quantita() <= 0 && !dto.wishlist()) {
            throw new IllegalArgumentException("La quantità deve essere maggiore di zero.");
        }

        // Il prodottoId viene ora estratto dal DTO
        UUID prodottoId = dto.prodottoId();
        UtenteProdottoId cartId = new UtenteProdottoId(userId, prodottoId);

        Carrello item = carrelloRepository.findById(cartId).orElseGet(() -> {
            // Logica di creazione se l'elemento non esiste
            Carrello newItem = cartMapper.fromCreateDto(dto);
            Utente utente = utenteService.getById(userId);
            Prodotto prodotto = prodottoService.getById(prodottoId);

            newItem.setId(cartId);
            newItem.setUtente(utente);
            newItem.setProdotto(prodotto);
            return newItem;
        });

        item.setQuantita(dto.quantita());
        item.setWishlist(dto.wishlist());
        Carrello savedItem = carrelloRepository.save(item);
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

    public List<ResponseCartDTO> getAllWishlistItemsByUserId(UUID userId) {
        // 1. Recupero tutte le righe della wishlist per l'utente specificato
        List<Carrello> wishlistItems = carrelloRepository.findWishlistByUserId(userId);

        // 2. Uso il mapper per convertire le entità in DTO
        return wishlistItems.stream()
                .map(cartMapper::toDto)
                .toList();
    }

    public void removeItemsFromCart(UUID userId, List<UUID> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            // Se la lista è vuota non fare nulla o lancia un'eccezione
            return;
        }

        // 1. Crea la lista degli ID compositi (UtenteProdottoId) da eliminare
        List<UtenteProdottoId> cartIdsToDelete = productIds.stream()
                .map(productId -> new UtenteProdottoId(userId, productId))
                .toList();

        // 2. Usa il metodo deleteAllById di Spring Data JPA per una cancellazione efficiente.
        //    Questo metodo esegue una singola query (o un numero molto limitato)
        //    invece di una per ogni elemento.
        carrelloRepository.deleteAllById(cartIdsToDelete);
    }
}

