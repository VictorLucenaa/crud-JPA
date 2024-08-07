package org.example;


import org.example.services.ProductService;

public class App
{
    public static void main( String[] args ) throws Exception {
        ProductService produtoService = new ProductService();

//        Product product = new Product("queijo", "gouda", 20.00);
//      produtoService.createProduct(product);

        produtoService.getAllProducts();
        produtoService.getProduct(Long.parseLong("2"));
        produtoService.removeProduct(Long.parseLong("1"));
    }
}
