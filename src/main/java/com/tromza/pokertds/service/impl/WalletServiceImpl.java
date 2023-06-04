package com.tromza.pokertds.service.impl;

import com.tromza.pokertds.model.domain.User;
import com.tromza.pokertds.model.domain.Wallet;
import com.tromza.pokertds.repository.WalletRepository;
import com.tromza.pokertds.service.WalletService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;


    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Optional<Wallet> getWalletByUserId(Integer userId) {
        return walletRepository.findWalletByUserId(userId);
    }

    public Optional<Wallet> getWalletForUser(User user) {
        return walletRepository.findWalletByUserId(user.getId());
    }

    public Wallet createWalletForUser(User user) {
        if (walletRepository.findWalletByUserId(user.getId()).isPresent()) {
            throw new UnsupportedOperationException("Wallet has already been!");
        } else if (user.getFirstName() == null || user.getLastName() == null || user.getCountry() == null || user.getTelephone() == null) {
            throw new SecurityException("User data isn't enough");
        } else {
            Wallet wallet = new Wallet();
            wallet.setUserId(user.getId());
            wallet.setBalance(BigDecimal.valueOf(0));
            return walletRepository.save(wallet);
        }
    }

    @Transactional
    public Wallet updateWallet(Wallet wallet, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.valueOf(0)) < 0) {
            return withdrawWallet(wallet, amount);
        } else {
            return refillWallet(wallet, amount);
        }
    }

    public Wallet refillWallet(Wallet wallet, BigDecimal amount) {
        wallet.setBalance(wallet.getBalance().add(amount));
        return walletRepository.saveAndFlush(wallet);
    }

    public Wallet withdrawWallet(Wallet wallet, BigDecimal amount) {
        BigDecimal oldBalance = wallet.getBalance();
        if (oldBalance.compareTo(amount.negate()) < 0) {
            throw new UnsupportedOperationException("Wallet has only " + oldBalance + "$");
        } else {
            wallet.setBalance(oldBalance.add(amount));
            return walletRepository.saveAndFlush(wallet);
        }
    }
}
