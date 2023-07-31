package com.example.BE_LinkKien.Controller;

import com.example.BE_LinkKien.Models.Brand;
import com.example.BE_LinkKien.Models.Category;
import com.example.BE_LinkKien.Service.BrandService;
import com.example.BE_LinkKien.Service.CategoryService;
import com.example.BE_LinkKien.dto.BrandDTO;
import com.example.BE_LinkKien.payload.response.ResponseObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/brand")
public class BrandController {

    private final BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }
    @Autowired
    private ModelMapper modelMapper;


    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createBrand(@RequestParam String name, @RequestParam String image) {
        return ResponseEntity.ok().body(new ResponseObject("success",200, "Create brand successfully",brandService.createBrandTest(name,image)));
    }

//    @GetMapping("/image/{imageName}")
//    public ResponseEntity<Resource> getImage(@PathVariable String imageName) throws IOException {
//        Path staticPath = Paths.get("static");
//        Path imagePath = Paths.get("images");
//        Path file = staticPath.resolve(imagePath).resolve(imageName);
//
//        Resource resource = new UrlResource(file.toUri());
//
//        if (resource.exists() && resource.isReadable()) {
//            return ResponseEntity.ok()
//                    .contentType(MediaType.IMAGE_PNG) // Thiết lập kiểu dữ liệu là ảnh PNG
//                    .body(resource);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }


    public static String getFileNameFromPath(String fullPath) {
        int lastSeparatorIndex = fullPath.lastIndexOf("\\");
        if (lastSeparatorIndex != -1) {
            return fullPath.substring(lastSeparatorIndex + 1);
        } else {
            // Nếu không tìm thấy ký tự '\' trong chuỗi, trả về chuỗi gốc
            return fullPath;
        }
    }
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllCategory() {
        return ResponseEntity.ok().body(new ResponseObject("success",200, "Get all category successfully",brandService.getAllBrand()));
    }
    @PostMapping("/getBrand/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getBrand(@PathVariable Integer id) {
        return ResponseEntity.ok().body(new ResponseObject("success",200, "Get brand successfully",brandService.getById(id)));
    }
    @PutMapping("/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> editBrand(@RequestBody Brand category1) {
        return ResponseEntity.ok().body(new ResponseObject("success",200, "Edit brand successfully",brandService.editBrand(category1)));
    }


    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteCategory(@RequestParam Integer id) {
        brandService.deleteBrand(id);
        return ResponseEntity.ok().body(new ResponseObject("success",200, "Delete brand successfully",null));
    }
}
