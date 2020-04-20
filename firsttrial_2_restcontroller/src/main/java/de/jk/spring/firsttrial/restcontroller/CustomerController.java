package de.jk.spring.firsttrial.restcontroller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.jk.spring.firsttrial.entity.CustomerEntity;
import de.jk.spring.firsttrial.exception.RecordNotFoundException;
import de.jk.spring.firsttrial.functionalservice.CustomerService;
 
 
@RestController
@RequestMapping("/customers")
public class CustomerController 
{
    @Autowired
    CustomerService service;
 
    @GetMapping()
    public ResponseEntity<List<CustomerEntity>> getAllcustomersWithPagination(
    		@RequestParam(defaultValue = "0") Integer pageNo, 
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy
    		) {
        List<CustomerEntity> list = service.getAllCustomers(pageNo, pageSize, sortBy);
 
        return new ResponseEntity<List<CustomerEntity>>(list, new HttpHeaders(), HttpStatus.OK);
    }
 
    @GetMapping("/{id}")
    public ResponseEntity<CustomerEntity> getcustomerById(@PathVariable("id") Long id) 
                                                    throws RecordNotFoundException {
        CustomerEntity entity = service.getCustomerById(id);
 
        return new ResponseEntity<CustomerEntity>(entity, new HttpHeaders(), HttpStatus.OK);
    }
    
    @GetMapping("/name/{name}")
    public ResponseEntity<List<CustomerEntity>> getCustomersByLastNameLike(@PathVariable("name") Optional<String> name) { 
        List<CustomerEntity> list = service.getCustomersByNameLike(name);
        
        return new ResponseEntity<List<CustomerEntity>>(list, new HttpHeaders(), HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<CustomerEntity> createOrUpdatecustomer(@Valid @RequestBody CustomerEntity customer)
                                                    throws RecordNotFoundException {
        CustomerEntity updated = service.createOrUpdateCustomer(customer);
        return new ResponseEntity<CustomerEntity>(updated, new HttpHeaders(), HttpStatus.OK);
    }
 
    @DeleteMapping("/{id}")
    public ResponseEntity deletecustomerById(@PathVariable("id") Long id) 
                                                    throws RecordNotFoundException {
        service.deleteCustomerById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
 
}