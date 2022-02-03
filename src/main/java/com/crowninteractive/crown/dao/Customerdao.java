package com.crowninteractive.crown.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.crowninteractive.crown.models.Customer;


public interface Customerdao extends JpaRepository<Customer, Integer> {

	Optional<Customer> findByEmail(String email);
	
	@Query(value = "Select * from customer c inner join billing_details b on c.id=b.customerid", nativeQuery = true)
	public List<Customer> findAllCustomer();
	
	@Query(value = "Select * from customer c inner join billing_details b on c.id=b.customerid where c.id=:id", nativeQuery = true)
	Optional<Customer> findByCustomerId(int id);

}
