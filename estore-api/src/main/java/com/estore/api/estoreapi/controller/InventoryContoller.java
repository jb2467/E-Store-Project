package com.estore.api.estoreapi.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.persistence.InventoryDAO;

 /**
 * Handles the REST API requests for the Product resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 */
@RestController
@RequestMapping("inventory/products")
public class InventoryContoller {
    
    private static final Logger LOG = Logger.getLogger(InventoryContoller.class.getName());
    private InventoryDAO inventoryDAO;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param inventoryDAO The {@link InventoryDAO Inventory Data Access Object} to perform CRUD
     *                operations
     *                <br>
     *                This dependency is injected by the Spring Framework
     */
    public InventoryContoller(InventoryDAO inventoryDAO){
        this.inventoryDAO = inventoryDAO;
    }

    /**
     * Responds to the GET request for a {@linkplain Product product} for the given name
     * 
     * @param name The name used to locate the {@link Product product}
     * 
     * @return ResponseEntity with {@link Product product} object and HTTP status of OK if found<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/find{name}")
    public ResponseEntity<Product> getProduct(@RequestParam String name) {
        LOG.info("GET inventory/products/find?name=" + name);
        try {
            Product product = inventoryDAO.getProduct(name);
            if (product != null)
                return new ResponseEntity<Product>(product,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Product products}
     * 
     * @return ResponseEntity with array of {@link Product products} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<Product[]> getProducts() {
        LOG.info("GET inventory/products");

        try {
            Product[] products = inventoryDAO.getProducts();
            return new ResponseEntity<Product[]>(products,HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Product products} whose name contains
     * the text in name
     * 
     * @param name The name parameter which contains the text used to find the {@link Product products}
     * 
     * @return ResponseEntity with array of {@link Product product} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * <p>
     * Example: Find all product that contain the text "kekW"
     * GET http://localhost:8080/product/?name=kekW
     */
    @GetMapping("/search{name}")
    public ResponseEntity<Product[]> searchProducts(@RequestParam String name) {
        LOG.info("GET inventory/products/search?name="+name);

        try {
            Product[] products = inventoryDAO.findProducts(name);
            return new ResponseEntity<Product[]>(products,HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a {@linkplain Product product} with the provided product object
     * 
     * @param product - The {@link Product product} to create
     * 
     * @return ResponseEntity with created {@link Product product} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link Product product} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        LOG.info("POST inventory/products " + product);

        try {
            Product status = inventoryDAO.createProduct(product);
            if (status != null) {
                return new ResponseEntity<Product>(product,HttpStatus.CREATED);
            } else { 
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain Product product} with the provided {@linkplain Product product} object, if it exists
     * 
     * @param product The {@link Product product} to update
     * 
     * @return ResponseEntity with updated {@link Product product} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product) {
        LOG.info("PUT inventory/products " + product);

        try {
            Product status = inventoryDAO.updateProduct(product);
            if (status != null) {
                return new ResponseEntity<Product>(product,HttpStatus.OK);
            } else { 
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Product product} with the given name
     * 
     * @param name The name of the {@link Product product} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("{name}")
    public ResponseEntity<Product> deleteProduct(@PathVariable String name) {
        LOG.info("DELETE inventory/products/" + name);

        try {
            Product status = inventoryDAO.deleteProduct(name);
            if (status != null) {
                return new ResponseEntity<Product>(status,HttpStatus.OK);
            } else { 
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
}
