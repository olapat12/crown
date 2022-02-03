package com.crowninteractive.crown.controllers;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crowninteractive.crown.dao.Customerdao;
import com.crowninteractive.crown.models.Customer;


@RestController
@RequestMapping(path="/customer")
@CrossOrigin("*")
public class CustomerController {
	
	@Autowired
	private Customerdao customerdao;
	
	
	// method for firstname and lastname validtion
	public boolean isValid(String name) {
		
	       return ((name != null) && (!name.equals(""))
                && (name.matches("^[a-zA-Z]*$")));
	}
	
	
	// saving new customer
	@PostMapping("/save")
	public ResponseEntity<Object> save(@RequestBody Customer customer) {
		
		String regex = "^[A-Za-z0-9+_.-]+@(.+)$";  
		
		boolean validfirstname = isValid(customer.getFirstname());
		
		boolean validlastname = isValid(customer.getLastname());
		
		// check if firstname does not contain any number or not empty
		if(!validfirstname) {
			return new ResponseEntity<>("Invalid firstname format", HttpStatus.OK);
		}
		
		// check if lastname does not contain any number or not empty
		if(!validlastname) {
			return new ResponseEntity<>("Invalid lastname format", HttpStatus.OK);
		}
		
		String email = customer.getEmail();
		
		Pattern pattern = Pattern.compile(regex); 
		
		Matcher matcher = pattern.matcher(email); 
		
        Optional<Customer> newCustomer = customerdao.findByEmail(email);
		
        // check if email is valid
		if(!matcher.matches()) {
			return new ResponseEntity<>("Invalid email format", HttpStatus.OK);
		}
		
		// check if email already exist
		if(newCustomer.isPresent()) {
			return new ResponseEntity<>("Email already exist", HttpStatus.OK);
		}
		
		// if it passes all validation 
		
	   Customer newcustomer = customerdao.save(customer);
		
		return new ResponseEntity<>(newcustomer, HttpStatus.OK);
		
	}
	
	@GetMapping("/get-all-customers")
	public List<Customer> getAll(){
		
		return customerdao.findAll();
	}
	
	
	// get customer by id
	@GetMapping("/get-customer-by-id/{id}")
	public ResponseEntity<Object> getCustomer(@PathVariable int id){
		
		Optional<Customer> customer = customerdao.findById(id);
		
		if(customer.isEmpty()) {
			return new ResponseEntity<>("No user found", HttpStatus.OK);
		}
		return new ResponseEntity<>(customer, HttpStatus.OK);
	}
	
	
	// get a customer by email address
	@GetMapping("/get-customer-by-email/{email}")
	public ResponseEntity<Object> getCustomer(@PathVariable String email){
		
		Optional<Customer> customer = customerdao.findByEmail(email);
		
		if(customer.isEmpty()) {
			return new ResponseEntity<>("No user found", HttpStatus.OK);
		}
		return new ResponseEntity<>(customer, HttpStatus.OK);
	}
	
	// update customer by id
	@PutMapping("/update/customer-by-id/{id}")
	public ResponseEntity<Object> updateCustomer(@PathVariable int id, @RequestBody Customer customer){

		Optional<Customer> user = customerdao.findById(id);
		
		if(user.isEmpty()) {
			return new ResponseEntity<>("No record found", HttpStatus.OK);
		}
		
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";  
		
		boolean validfirstname = isValid(customer.getFirstname());
		
		boolean validlastname = isValid(customer.getLastname());
		
		// check if firstname does not contain any number or not empty
		if(!validfirstname) {
			return new ResponseEntity<>("Invalid firstname format", HttpStatus.OK);
		}
		
		// check if lastname does not contain any number or not empty
		if(!validlastname) {
			return new ResponseEntity<>("Invalid lastname format", HttpStatus.OK);
		}
		
		String email = customer.getEmail();
		
		Pattern pattern = Pattern.compile(regex); 
		
		Matcher matcher = pattern.matcher(email); 
		
        Optional<Customer> newCustomer = customerdao.findByEmail(email);
		
        // check if email is valid
		if(!matcher.matches()) {
			return new ResponseEntity<>("Invalid email format", HttpStatus.OK);
		}
		
		customer.setId(id);
		
		Customer updatedUser = customerdao.save(customer);
		
		return new ResponseEntity<>(updatedUser, HttpStatus.OK);
		
	}
	
	
	// get all customer by pagination
	@GetMapping("/{offset}/{limit}")
	public List<Customer> pagination(@PathVariable int offset, @PathVariable int limit){
		
		return customerdao.findAll(PageRequest.of(limit, offset)).getContent();
	}
	
	@GetMapping("/all")
	public List<Customer> pagination(){
		
		return customerdao.findAllCustomer();
	}
	
	@GetMapping("/find-by-id/{id}")
	public ResponseEntity<Object> findbyId(@PathVariable int id){
		
		Optional<Customer> customer = customerdao.findByCustomerId(id);
		
		if(customer.isEmpty()) {
			return new ResponseEntity<>("No record found", HttpStatus.OK);
		}
		return new ResponseEntity<>(customer, HttpStatus.OK);
	}

}
