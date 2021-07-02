package com.ticketing.entity;


import com.ticketing.utils.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name ="projects")
@NoArgsConstructor
@Getter
@Setter
@Where(clause = "is_deleted=false")

public class Project extends BaseEntity{

    @Column(unique = true)
    private String projectCode;
    private String projectName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User assignedManager;

    private LocalDate startdate;

    private LocalDate endDate;


    @Enumerated
    private Status projectStatus;
    private String projectDetail;

}
