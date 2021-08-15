package com.crypto.dao;

import com.crypto.models.Crypto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CryptoDao extends JpaRepository<Crypto, Integer> {

    @Query(value = "select * from crypto.crypto where curr1 = :curr1 AND lprice = (select MIN(lprice) from crypto where curr1 = :curr1) limit 1", nativeQuery = true)
    Crypto findMinByCurr1(String curr1);

    @Query(value = "select * from crypto.crypto where curr1 = :curr1 AND lprice = (select MAX(lprice) from crypto where curr1 = :curr1) limit 1", nativeQuery = true)
    Crypto findMaxByCurr1(String curr1);

    @Query(value = "select * from crypto.crypto where curr1 = :curr1 order by lprice limit :limit", nativeQuery = true)
    List<Crypto> findAllByCurr1(String curr1, int limit);

    List<Crypto> findAllByCurr1(String curr1, Pageable pageable);

}
