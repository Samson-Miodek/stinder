package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Stata {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String ip;
    private Timestamp date;


    public Stata(String ip, Timestamp date) {
        this.ip = ip;
        this.date = date;
    }
}
