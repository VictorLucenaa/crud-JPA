package org.example.services;

import javassist.NotFoundException;
import org.example.entities.Product;
import org.example.exceptions.ProductAlreadyExistsException;

import javax.persistence.*;
import java.util.List;

public class ProductRepository {

    private  EntityManagerFactory emf;

    public ProductRepository(){

       this.emf = Persistence.createEntityManagerFactory("Product");
    }





    public Product createProduct(Product product) throws ProductAlreadyExistsException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();


        try{
            transaction.begin();

        if(product.getId() != null && findProductById(product.getId()) != null) throw new ProductAlreadyExistsException("Produto já existe");

        em.persist(product);
        transaction.commit();
            return product;
        }catch (ProductAlreadyExistsException ex){
            if(transaction.isActive()){
                transaction.rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    public List<Product> getAllProducts() throws NotFoundException{
        EntityManager em = emf.createEntityManager();

        try{
            TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p", Product.class);

            List<Product> products = query.getResultList();
            if(products.isEmpty()){
                throw new NotFoundException("Não existem produtos cadastrados");
            }

            return products;

//            for (Product product : products) {
//                System.out.println(product);
//            }
        } catch (NotFoundException ex){
            throw ex;
        } finally {
            em.close();
        }
    }

    public Product getProduct(Long productId) throws NotFoundException {
        EntityManager em = emf.createEntityManager();

        try{
            Product product = findProductById(productId);

            if(product != null){
                return product;
//                System.out.println("Produto encontrado: " + product);
            } else {
                throw new NotFoundException("Produto não encontrado.");
            }
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } finally {
            em.close();
        }
    }

    public Product removeProduct(Long productId) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try{

            transaction.begin();
            Product product = em.find(Product.class, productId);

            if(product != null){
                em.remove(product);
                transaction.commit();
//                System.out.println("Produto removido com sucesso!");
                return product;
            } else {
                throw new NotFoundException("Produto não encontrado");
            }
        } catch (NotFoundException e) {
            if(transaction.isActive()){
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public String updateProduct(Long productId, Product updatedProduct) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try{
            transaction.begin();

            Product existingProduct = em.find(Product.class, productId);

            if(existingProduct == null) throw new NotFoundException("Produto não encontrado.");

            existingProduct.setName(updatedProduct.getName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setPrice(updatedProduct.getPrice());
          transaction.commit();
          return "Produto alterado com sucesso: " + existingProduct.toString();
//            System.out.println("produto alterado com sucesso: " + existingProduct);
        } catch (NotFoundException e) {
            if (transaction.isActive()){
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    private Product findProductById(Long productId){
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Product.class, productId);
        }finally {
            em.close();
        }
    }
}
