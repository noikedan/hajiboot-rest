
package com.example.api;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.domain.Customer;
import com.example.service.CustomerService;
import com.example.util.YamlFooProperties;


@RestController
@RequestMapping("api/customers")
public class CustomerRestController {
	@Autowired
	private Environment environment;
    @Autowired
    CustomerService customerService;
    @Autowired
    private YamlFooProperties yamlFooProperties;

    @GetMapping
    List<Customer> getCustomers() {
        List<Customer> customers = customerService.findAll();
        //System.out.println("Region = " + configReader.getRegion());
        //System.out.println("Bucket = " + configReader.getBucket());
        //System.out.println("file = " + configReader.getFile());
        System.out.println("-----------------------------------------------");
        System.out.println(environment.getProperty("spring.profiles.active"));
        System.out.println(yamlFooProperties.getName());

        return customers;
    }

    @GetMapping(path = "{id}")
    Customer getCustomer(@PathVariable Integer id) {
        Customer customer = customerService.findOne(id);
        return customer;
    }

    @PostMapping
    ResponseEntity<Customer> postCustomers(@RequestBody Customer customer, UriComponentsBuilder uriBuilder) {
        Customer created = customerService.create(customer);
        URI location = uriBuilder.path("api/customers/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping(path = "{id}")
    Customer putCustomer(@PathVariable Integer id, @RequestBody Customer customer) {
        customer.setId(id);
        return customerService.update(customer);
    }

    @DeleteMapping(path = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCustomer(@PathVariable Integer id) {
        customerService.delete(id);
    }
}
