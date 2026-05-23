package com.crm.gravity.model.entities;

public record DanceGroup (
        Long groupId,
        String name,
        String direction,
        String level,
        Long choreographerId
)
{}
