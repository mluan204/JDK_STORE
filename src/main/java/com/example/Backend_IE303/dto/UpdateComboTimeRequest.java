package com.example.Backend_IE303.dto;

import java.sql.Timestamp;

public class UpdateComboTimeRequest {
    private Timestamp timeEnd;

    public UpdateComboTimeRequest() {
    }

    public UpdateComboTimeRequest(Timestamp timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Timestamp getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Timestamp timeEnd) {
        this.timeEnd = timeEnd;
    }
}