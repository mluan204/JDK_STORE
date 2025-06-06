package com.example.Backend_IE303.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "combo")
public class Combo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "created_at")
    Timestamp createdAt;
    @Column(name = "time_end")
    Timestamp timeEnd;

    @OneToMany(mappedBy = "combo", cascade = CascadeType.ALL)
    List<ComboProduct> comboProducts = new ArrayList<>();
}
