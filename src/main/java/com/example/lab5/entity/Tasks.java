package com.example.lab5.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Tasks {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="MySequenceGenerator")
    @SequenceGenerator(allocationSize=1, schema="public",  name="MySequenceGenerator", sequenceName = "mysequence")
    private Long taskId;

    @NotEmpty(message = "Title is required")
    private String title;

    @Column(name="description")
    private String description;

    @FutureOrPresent(message = "Due date must not be in the past")
    private LocalDate dueDate;

    @Column(name = "priority")
    private String priority;

    @Column(name="status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Categories category;
}
