package com.tutorial.ecomproj.controller;

import com.tutorial.ecomproj.model.Product;
import com.tutorial.ecomproj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService service;

//    @RequestMapping("/")
//    public String greet(){
//        return "Hello";
//    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(){
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id){
        Product prod = service.getProductById(id);
        if(prod!=null)
            return new ResponseEntity<>(prod, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product,
                                        @RequestPart MultipartFile imageFile){
        try {
            Product prod= service.addProduct(product, imageFile);
            return new ResponseEntity<>(prod, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("product/{productId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId){
        Product prod= service.getProductById(productId);
        byte[] imageFile= prod.getImageDate();

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(prod.getImageType()))
                .body(imageFile);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@RequestPart Product product,
                                                @RequestPart MultipartFile imageFile){
        Product product1= null;
        try {
            product1 = service.updateProduct(product, imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(product1!=null){
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Failed to Update", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){
        Product product= service.getProductById(id);
        if(product!=null){
            service.deleteProduct(id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword){
        System.out.println("Searching with "+keyword);
        List<Product> products= service.searchProducts(keyword);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
