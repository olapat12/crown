package com.crowninteractive.crown.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crowninteractive.crown.models.BillingDetails;

public interface BillingDetailsdao extends JpaRepository<BillingDetails, Integer> {

	Optional<BillingDetails> findByAccountnumber(String acctnum);

}
