package com.tromza.pokertds.service;

import com.tromza.pokertds.domain.User;
import com.tromza.pokertds.domain.Wallet;
import com.tromza.pokertds.repository.UserRepository;
import com.tromza.pokertds.repository.WalletRepository;
import com.tromza.pokertds.request.UserMoneyAmount;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    public WalletService(WalletRepository walletRepository, UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
    }

    public Optional<Wallet> getWalletByUserId(Integer userId) {
        return walletRepository.findWalletByUserId(userId);
    }

    public Optional<Wallet> getWalletForUser(Principal principal) {
        Optional<User> user = userRepository.findUserByLogin(principal.getName());
        if (user.isEmpty()) {
            return Optional.empty();
        } else {
            return walletRepository.findWalletByUserId(user.get().getId());
        }
    }

    public Wallet createWallet(Integer userId) {
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        return walletRepository.save(wallet);
    }

    public Wallet createWalletForPrincipal(Principal principal) {
        Optional<User> user = userRepository.findUserByLogin(principal.getName());
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User with login " + principal.getName() + " not found!");
        } else if (walletRepository.findWalletByUserId(user.get().getId()).isPresent()) {
            throw new UnsupportedOperationException("Wallet has already been!");
        } else if (user.get().getFirstName() == null || user.get().getLastName() == null || user.get().getCountry() == null || user.get().getTelephone() == null) {
            throw new SecurityException("User data isn't enough");
        } else {
            Wallet wallet = new Wallet();
            wallet.setUserId(user.get().getId());
            wallet.setBalance(BigDecimal.valueOf(0));
            return walletRepository.save(wallet);
        }
    }
@Transactional
    public Wallet updateWallet(UserMoneyAmount userMoney) {
        if (userRepository.findById(userMoney.getUserId()).isPresent()) {
            if (userMoney.getAmount().compareTo(BigDecimal.valueOf(0)) < 0) {
                return withdrawWallet(userMoney);
            } else {
                return refillWallet(userMoney);
            }
        } else {
            throw new NoSuchElementException("User with id " + userMoney.getUserId() + " not found!");
        }
    }
@Transactional
    public Wallet refillWallet(UserMoneyAmount userMoney) {
        Optional<Wallet> optionalWallet = getWalletByUserId(userMoney.getUserId());
        Wallet wallet;
        if (optionalWallet.isPresent()) {
            wallet = optionalWallet.get();
            wallet.setBalance(wallet.getBalance().add(userMoney.getAmount()));
        } else {
            wallet = createWallet(userMoney.getUserId());
            wallet.setBalance(userMoney.getAmount());
        }
        return walletRepository.saveAndFlush(wallet);
    }

    public Wallet withdrawWallet(UserMoneyAmount userMoney) {
        Wallet wallet = getWalletByUserId(userMoney.getUserId()).orElseThrow(() -> new NoSuchElementException("User hasn't wallet!!!"));
        BigDecimal oldBalance = wallet.getBalance();
        if (oldBalance.compareTo(userMoney.getAmount().negate()) < 0) {
            throw new UnsupportedOperationException("Wallet has only " + oldBalance + "$");
        } else {
            wallet.setBalance(oldBalance.add(userMoney.getAmount()));
            return walletRepository.saveAndFlush(wallet);
        }
    }
}
