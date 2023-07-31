package com.example.BE_LinkKien.Service;


import com.example.BE_LinkKien.Models.Brand;
import com.example.BE_LinkKien.Models.ImageProduct;
import com.example.BE_LinkKien.Models.Product;
import com.example.BE_LinkKien.Models.Specification;
import com.example.BE_LinkKien.Repository.*;
import com.example.BE_LinkKien.dto.ImageProductDTO;
import com.example.BE_LinkKien.dto.SpecificationDTO;
import com.example.BE_LinkKien.exception.CustomException;
import com.example.BE_LinkKien.payload.response.ProductRespone;
import com.example.BE_LinkKien.payload.resquest.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SpecificationRepository specificationRepository;
    @Autowired
    private ImagePeoductRepository imagePeoductRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;


    public Product createProduct(ProductRequest product)
    {
        if(!productRepository.existsById(product.getId())) {
            Product _product = new Product();

            List<Specification> specificationList = new ArrayList<>();
            List<ImageProduct> imageProductList = new ArrayList<>();

            _product.setId(product.getId());
            _product.setName(product.getName());
            _product.setDescription(product.getDescription());
            _product.setPrice(product.getPrice());
            _product.setStatus(true);
            _product.setIdBrand(product.getIdBrand());
            _product.setIdCategory(product.getIdCategory());
            _product.setIdEvent(null);

            Product product1 = productRepository.save(_product);

            product.getSpecification().forEach((e)->{
                Specification specification = new Specification();
                specification.setSpecification(e);
                specification.setIdProduct(product.getId());
//            specification.setProduct(_product);
                specificationList.add(specification);
            });
            specificationRepository.saveAll(specificationList);

            product.getImageProducts().forEach((e)->{
                ImageProduct imageProduct = new ImageProduct();
                imageProduct.setName(e.getName());
                imageProduct.setImage(e.getImage());
                imageProduct.setIdProduct(product.getId());
//            imageProduct.setProduct(_product);
                imageProductList.add(imageProduct);
            });
            imagePeoductRepository.saveAll(imageProductList);
            return product1;
        }else {
            throw new CustomException("Product Id is exists", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
    public List<ProductRespone> getAllProduct() {
        List<Product> products = productRepository.findAll();
        if(products == null) {
            throw new CustomException("The product list are empty", HttpStatus.NOT_FOUND);
        }

        List<ProductRespone> productResponeList = new ArrayList<>();


        products.stream().forEach((e)->{
            ProductRespone productRespone = new ProductRespone();
            productRespone.setId(e.getId());
            productRespone.setName(e.getName());
            productRespone.setDescription(e.getDescription());



            List<Specification> specificationList = specificationRepository.findAllByIdProduct(e.getId());
            List<SpecificationDTO> specificationDTOS = specificationList.stream()
                    .map(accounts -> modelMapper.map(accounts, SpecificationDTO.class))
                    .collect(Collectors.toList());

            List<ImageProduct> imageProductList = imagePeoductRepository.findAllByIdProduct(e.getId());
            List<ImageProductDTO> imageProductDTOS = imageProductList.stream()
                    .map(accounts -> modelMapper.map(accounts, ImageProductDTO.class))
                    .collect(Collectors.toList());

            productRespone.setSpecification(specificationDTOS);
            productRespone.setImageProducts(imageProductDTOS);
            productRespone.setPrice(e.getPrice());
            productRespone.setBrand(brandRepository.findBrandById(e.getIdBrand()).getName());
            productRespone.setCategory(categoryRepository.findCategoryById(e.getIdCategory()).getName());
            productRespone.setEvent(eventRepository.findEventById(e.getIdEvent()));
            productRespone.setStatus(e.getStatus());
            productRespone.setCreatedBy(userRepository.findUserById(e.getCreatedBy()).getEmail());
            productRespone.setCreatedAt(e.getCreatedAt());
            productRespone.setUpdatedBy(userRepository.findUserById(e.getUpdatedBy()).getEmail());
            productRespone.setUpdatedAt(e.getUpdatedAt());
            productResponeList.add(productRespone);
        });
        return productResponeList;
    }
}
