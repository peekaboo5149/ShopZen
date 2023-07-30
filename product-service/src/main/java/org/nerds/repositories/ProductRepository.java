package org.nerds.repositories;

import org.nerds.entities.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product,String> {
    Product findOneByTitle(String title);

    Product findOneById(String id);
}
