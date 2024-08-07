package org.example.services;

import org.example.entities.Product;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class ProductService {

    private final EntityManagerFactory entityManagerFactory;

    public ProductService(){
        entityManagerFactory = Persistence.createEntityManagerFactory("Product");
    }





    public void createProduct(Product product) throws Exception{
        EntityManager em = entityManagerFactory.createEntityManager();


        try{

        if(product.getId() != null) throw new IllegalArgumentException("Produto já existe");

        em.getTransaction().begin();
        em.persist(product);
        em.getTransaction().commit();
            System.out.println("Produto cadastrado com sucesso! "+ product);
        }catch (Exception ex){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao cadastrar produto. " + ex);
        } finally {
            em.close();
        }
    }

    public void getAllProducts() {
        EntityManager em = entityManagerFactory.createEntityManager();

        try{
            TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p", Product.class);

            List<Product> products = query.getResultList();

            for (Product product : products) {
                System.out.println(product);
            }
        } catch (Exception ex){
            throw new RuntimeException("Não foi possível resgatar a lista de Produtos. " + ex.getMessage());
        } finally {
            em.close();
        }
    }

    public void getProduct(Long productId){
        EntityManager em = entityManagerFactory.createEntityManager();

        try{
            Product product = em.find(Product.class, productId);

            if(product != null){
                System.out.println("Produto encontrado: " + product);
            } else {
                System.out.println("Produto não encontrado");
            }
        }finally {
            em.close();
        }
    }

    public void removeProduct(Long productId){
        EntityManager em = entityManagerFactory.createEntityManager();

        try{
            em.getTransaction().begin();
            Product product = em.find(Product.class, productId);

            if(product != null){
                em.remove(product);
                em.getTransaction().commit();
                System.out.println("Produto removido com sucesso!");
            } else {
                System.out.println("Produto não encontrado");
            }
        }catch (Exception ex){
           if( em.getTransaction().isActive()){
               em.getTransaction().rollback();
           }
            System.out.println(ex.getMessage());
        }finally {
            em.close();
        }
    }
}
