package com.example.Backend_IE303.entity;

import com.example.Backend_IE303.entity.EmbeddedId.EmployeeShiftId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeShift {
    @EmbeddedId
    EmployeeShiftId id;

    @ManyToOne
    @MapsId("EmployeeId")
    @JoinColumn(name = "employee_id")
    Employee employee;

    @ManyToOne
    @MapsId("ShiftId")
    @JoinColumn(name = "shift_id")
    Shift shift;

    Timestamp time_in;
    Timestamp time_out;
    int wage;
}
