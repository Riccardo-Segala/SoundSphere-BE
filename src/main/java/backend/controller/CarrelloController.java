package backend.controller;

import backend.dto.carrello.DeleteCartDTO;
import backend.dto.carrello.UpdateCartItemDTO;
import backend.dto.carrello.ResponseCartDTO;
import backend.mapper.CartMapper;
import backend.model.Carrello;
import backend.model.embeddable.UtenteProdottoId;
import backend.security.CustomUserDetails;
import backend.service.CarrelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path="/api/carrello", produces = MediaType.APPLICATION_JSON_VALUE)
class CarrelloController extends GenericController <Carrello, UtenteProdottoId, UpdateCartItemDTO, UpdateCartItemDTO, ResponseCartDTO> {
    public CarrelloController(CarrelloService service, CartMapper mapper) {
        super(service, mapper);
    }
  
    @Autowired
    private CarrelloService carrelloService;




    // GET by ID composto
   /* @GetMapping("/{utenteId}/{prodottoId}")
    public ResponseEntity<ResponseCartDTO> getCartById(
            @PathVariable UUID utenteId,
            @PathVariable UUID prodottoId) {

        UtenteProdottoId id = new UtenteProdottoId(utenteId, prodottoId);
        return super.getById(id);
    }*/

    // POST
    @GetMapping
    public ResponseEntity<List<ResponseCartDTO>> getAllCartOfUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UUID userId = userDetails.getId();
        // Chiamata per ottenere tutti gli elementi del carrello
        List<ResponseCartDTO> cartItems = carrelloService.getAllCartItemsByUserId(userId);

        // Restituisce la lista degli elementi del carrello
        return ResponseEntity.ok(cartItems);
    }

    @GetMapping("/wishlist")
    public ResponseEntity<List<ResponseCartDTO>> getAllWishlist(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UUID userId = userDetails.getId();

        // Chiamata per ottenere tutti gli elementi della wishlist
        List<ResponseCartDTO> wishlistItems = carrelloService.getAllWishlistItemsByUserId(userId);

        // Restituisce la lista di DTO con uno status HTTP 200 OK
        return ResponseEntity.ok(wishlistItems);
    }



    @PutMapping
    public ResponseEntity<ResponseCartDTO> updateItemInCart(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                       @RequestBody UpdateCartItemDTO dto)
    {
        UUID userId = userDetails.getId();
        // Chiamata per rimuovere l'elemento dal carrello
        ResponseCartDTO updatedCart = carrelloService.updateItemInCart(userId, dto);

        // Restituisce la lista aggiornata del carrello
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeItemFromCart(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                   @PathVariable UUID productId)
    {
        UUID userId = userDetails.getId();
        UtenteProdottoId cartId = new UtenteProdottoId(userId, productId);
        // Chiamata per rimuovere l'elemento dal carrello
        carrelloService.delete(cartId);

        // Restituisce la lista aggiornata del carrello
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/items")
    public ResponseEntity<Void> removeItemsFromCart(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody DeleteCartDTO dto) {

        UUID userId = userDetails.getId();
        carrelloService.removeItemsFromCart(userId, dto.productIds());

        // La risposta standard per una DELETE a buon fine è 204 No Content
        return ResponseEntity.noContent().build();
    }


    // implementazione del metodo astratto getId
    @Override
    protected UtenteProdottoId getId(Carrello entity) {
        return entity.getId();
    }

}
