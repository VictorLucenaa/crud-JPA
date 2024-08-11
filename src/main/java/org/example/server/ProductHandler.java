package org.example.server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import javassist.NotFoundException;
import org.example.entities.Product;
import org.example.services.ProductRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ProductHandler implements HttpHandler {

    private ProductRepository productRepository = new ProductRepository();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        int statusCode = 200;

        switch (exchange.getRequestMethod()){
            case "GET":
                try {
                    List<Product> products = productRepository.getAllProducts();
                    response = new Gson().toJson(products);
                    break;
                } catch (NotFoundException e) {
                    throw new RuntimeException(e);
                }

        case "POST":
            InputStream inputStream = exchange.getRequestBody();
            Product product = new Gson().fromJson(new InputStreamReader(inputStream), Product.class);
            response = new Gson().toJson(productRepository.createProduct(product));
            break;


        case "DELETE":
            Long productId = Long.valueOf(exchange.getRequestURI().getPath().split("/")[2]);

        try {
          response = new Gson().toJson(productRepository.removeProduct(productId));
          break;
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
            default:
                statusCode = 405;
                response = "Method not allowed";
                break;
        }
        exchange.sendResponseHeaders(statusCode, response.getBytes(StandardCharsets.UTF_8).length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();

    }
}
