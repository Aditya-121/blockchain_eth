package com.ethereum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemPoolRepo extends JpaRepository<MemPool, Long> {

}
