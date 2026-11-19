
package com.example.onlineshop.services;

import com.example.onlineshop.entitetes.Order;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOrderConfirmation(
            Order order,
            String customerEmail,
            String customerName) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper =
                new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("ervinsegaqa@gmail.com");
        helper.setTo(customerEmail);
        helper.setSubject(
                "Porosia #" + " u pranua me sukses!"
//                "Porosia #" + order.getId() + " u pranua me sukses!"
        );

        String html = """
            <!DOCTYPE html>
            <html>
            <body style="margin:0;background:#f4f6f8;
                         font-family:Arial,sans-serif;color:#333;">
              <div style="max-width:600px;margin:30px auto;
                          background:#ffffff;border-radius:10px;
                          overflow:hidden;">
                <div style="background:#172554;color:white;
                            padding:28px;text-align:center;">
                  <h1>ONLINE SHOP</h1>
                  <p>Faleminderit që zgjodhët dyqanin tonë!</p>
                </div>

                <div style="padding:30px;">
                  <h2 style="color:#15803d;text-align:center;">
                    Porosia u pranua me sukses!
                  </h2>

                  <p>Përshëndetje %s,</p>
                  <p>
                    Porosia juaj është regjistruar me sukses.
                    Më poshtë mund t'i gjeni detajet e saj.
                  </p>

                  <hr style="border:0;border-top:1px solid #eee;">

                  <h3>Detajet e porosisë</h3>
                  <p>Numri i porosisë: <b>#%s</b></p>
                  <p>Statusi: <b>%s</b></p>
                  <p>Adresa: %s</p>
                  <p>Metoda e pagesës: %s</p>

                  <hr style="border:0;border-top:1px solid #eee;">

                  <h3>Totali i porosisë</h3>
                  <h2 style="color:#15803d;">
                    € %s
                  </h2>

                  <p>
                    Do t'ju njoftojmë për përditësimet e porosisë.
                  </p>

                  <p>Faleminderit për besimin tuaj!</p>
                  <b>Ekipi Online Shop</b>
                </div>

                <div style="background:#f4f6f8;padding:20px;
                            text-align:center;font-size:12px;color:#777;">
                  Ky është një email automatik.
                  Ju lutemi mos iu përgjigjni këtij email-i.
                </div>
              </div>
            </body>
            </html>
            """.formatted(
                "test user",
                "12345",
                "Processing",
                "123 Main St, City, Country",
                "Credit Card",
                "99.99"
        );

        helper.setText(html, true);

        mailSender.send(message);
    }
}