package com.tromza.pokertds.repository;

import com.tromza.pokertds.model.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Integer> {

    Optional<Wallet> findWalletByUserId (Integer userId);
}
