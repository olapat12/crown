package com.crowninteractive.crown.controllers;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crowninteractive.crown.dao.BillingDetailsdao;
import com.crowninteractive.crown.dao.Customerdao;
import com.crowninteractive.crown.models.BillingDetails;
import com.crowninteractive.crown.models.Customer;

@RestController
@RequestMapping(path="/billing")
@CrossOrigin("*")
public class Billingdetailscontroller {
	
	@Autowired
	private Customerdao customerdao;
	
	@Autowired
	private BillingDetailsdao bdetails;
	
	
	// saving new account details
	@PostMapping("/save/{id}")
	public ResponseEntity<Object> save(@RequestBody BillingDetails bd, @PathVariable int id) {
		
		String end = "-01";
		
		String acctnum = bd.getAccountnumber();
	
		
	    Pattern pattern = Pattern.compile("^\\d{10}$");
	    
		Matcher matcher = pattern.matcher(acctnum);
		
		String acctNumber = acctnum+end;
		
		bd.setAccountnumber(acctNumber);
		
		Optional<BillingDetails> newBd = bdetails.findByAccountnumber(acctNumber);
		
		// check if account number is less or greater than 10
		if(acctnum.length() != 10) {
			return new ResponseEntity<>("account number must be 10 characters long", HttpStatus.OK);
		}
		
		// check if account number already exist
		if(newBd.isPresent()) {
			return new ResponseEntity<>("duplicate account number", HttpStatus.OK);
		}
		
		// check if account number does not contain any letter
		if(!matcher.matches()) {
			return new ResponseEntity<>("Account number can not contain alphabet", HttpStatus.OK);
		}
		
		Optional<Customer> customer = customerdao.findById(id);
		
		if(customer.isEmpty()) {
			return new ResponseEntity<>("no user found", HttpStatus.OK);
		}
		
		// if it passes all validation 
		
		bd.setCustomer(customer.get());
		
		BillingDetails billdetails = bdetails.save(bd);
		
		return new ResponseEntity<>(billdetails, HttpStatus.OK);
		
	}
	
	// get all account number
	@GetMapping("/get-all-account")
	public List<BillingDetails> getAll(){
		
		return bdetails.findAll();
	}
	
	
	// get billing details by id
	@GetMapping("/get-billing-by-id/{id}")
	public ResponseEntity<Object> getCustomer(@PathVariable int id){
		
		Optional<BillingDetails> bd = bdetails.findById(id);
		
		if(bd.isEmpty()) {
			return new ResponseEntity<>("No record found", HttpStatus.OK);
		}
		return new ResponseEntity<>(bd, HttpStatus.OK);
	}
	
	
	// get billing details by account number
	@GetMapping("/get-billing-by-account/{account}")
	public ResponseEntity<Object> getCustomer(@PathVariable String account){
		
		Optional<BillingDetails> bd = bdetails.findByAccountnumber(account);
		
		if(bd.isEmpty()) {
			return new ResponseEntity<>("No record found", HttpStatus.OK);
		}
		return new ResponseEntity<>(bd, HttpStatus.OK);
	}
	

}
