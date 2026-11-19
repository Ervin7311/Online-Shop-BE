//package com.example.onlineshop.services;
//
//import com.example.onlineshop.entitetes.Order;
//import com.example.onlineshop.entitetes.Payment;
//import com.example.onlineshop.repositorys.OrderRepository;
//import com.example.onlineshop.repositorys.PaymentRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//
//@Service
//public class PaymentService {
//
//    @Autowired
//    private PaymentRepository paymentRepository;
//    private OrderRepository orderRepository;
//
//    public Payment getPaymentById(Long id) {
//        return paymentRepository.findById(id).orElseThrow(() -> new RuntimeException("Payment not found"));
//    }
//
//    public Payment createPayment(Payment payment) {
//        return paymentRepository.save(payment);
//    }
//
//    public List<Payment> getPaymentsByOrder(Long orderId) {
//        Optional<Order> orderOptional = orderRepository.findById(orderId);
//
//        if (orderOptional.isPresent()) {
//            Order order = orderOptional.get();
//            return paymentRepository.findByOrder(order);
//        } else {
//
//            return List.of();
//        }
//    }
//
//    public void deletePayment(Long id) {
//        Payment payment = getPaymentById(id);
//        paymentRepository.delete(payment);
//    }
//}
