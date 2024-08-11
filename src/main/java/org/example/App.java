package org.example;

import com.sun.net.httpserver.HttpServer;
import org.example.server.ProductHandler;
import org.example.services.ProductRepository;

import java.net.InetSocketAddress;

public class App
{
    public static void main( String[] args ) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/products", new ProductHandler());
        server.setExecutor(null);
        server.start();


    }
}
