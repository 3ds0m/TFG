package com.edson.gonzales.aff.Controller;

import com.edson.gonzales.aff.Entity.Offer;
import com.edson.gonzales.aff.Repository.OfferRepository;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/pago")
public class PagoController {
    @Value("${stripe.api}")
    private String stripeKey;

    @Autowired
    private OfferRepository ofertaRepository;  // Repositorio para acceder a ofertas

    @PostMapping("/crear-sesion")
    public ResponseEntity<?> crearSesionDePago(@RequestBody PaymentRequest paymentRequest) {
        Stripe.apiKey = stripeKey;
        try {
            Offer oferta = ofertaRepository.findById(paymentRequest.getOfferId())
                    .orElseThrow(() -> new RuntimeException("Oferta no encontrada"));
            System.out.println("Oferta encontrada: " + oferta.getTitle());
            if (oferta.getNew_price() == null) {
                throw new RuntimeException("Precio no disponible para la oferta");
            }
            System.out.println("Precio nuevo: " + oferta.getNew_price());


            long amount = Math.round(oferta.getNew_price() * 100);

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("https://google.com")
                    .setCancelUrl("https://youtube.com")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("eur")
                                                    .setUnitAmount(amount)
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName(oferta.getTitle())
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            Session session = Session.create(params);

            return ResponseEntity.ok(Map.of("sessionId", session.getId()));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error al crear la sesión de pago: " + e.getMessage()));
        }
    }


    public static class PaymentRequest {
        private Long offerId;

        public PaymentRequest() {
        }

        public PaymentRequest(Long offerId) {
            this.offerId = offerId;
        }

        public Long getOfferId() {
            return offerId;
        }

        public void setOfferId(Long offerId) {
            this.offerId = offerId;
        }
    }
}

