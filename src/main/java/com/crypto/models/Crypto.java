package com.crypto.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Crypto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int ID;
    private double lprice;
    private String curr1;
    private String curr2;
    private LocalDateTime createdAt;

    public Crypto(double lprice, String curr1, String curr2, LocalDateTime createdAt) {
        this.lprice = lprice;
        this.curr1 = curr1;
        this.curr2 = curr2;
        this.createdAt = createdAt;
    }
}
