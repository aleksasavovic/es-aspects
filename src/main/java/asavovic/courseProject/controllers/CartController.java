package asavovic.courseProject.controllers;

import asavovic.courseProject.entities.dto.CartDTO;
import asavovic.courseProject.entities.dto.ProductToAdd;
import asavovic.courseProject.services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/addProduct")
    public ResponseEntity<Void> addProductToCart(@RequestHeader Long sessionId,@RequestBody ProductToAdd productToBuy) {
        cartService.addProductToCart(productToBuy, sessionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/cart")
    public CartDTO getCart(@RequestHeader Long sessionId) {
        return cartService.showCart(sessionId);
    }

    @PostMapping("/cart/removeProduct")
    public void removeProduct(@RequestHeader Long sessionId, @RequestBody ProductToAdd productDTO) {
        cartService.removeProductFromCart(sessionId, productDTO.getId());
    }

    @PostMapping("/cart/updateProduct")
    public void updateProduct(@RequestHeader Long sessionId, @RequestBody ProductToAdd productDTO) {
        cartService.updateQuantityOfProductsInCart(productDTO, sessionId);
    }

    @PostMapping("/cart/checkout")
    public void checkout(@RequestHeader Long sessionId) {
        cartService.checkout(sessionId);
    }
}
