package com.ticketing.repository;

import com.ticketing.dto.TaskDTO;
import com.ticketing.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TaskRepository extends JpaRepository<Task,Long> {
    @Query("SELECT count(t) FROM Task t WHERE t.project.projectCode = ?1 AND t.taskStatus <> 'COMPLETE' ")
    int totalNonCompletedTask(String projectCode);


}
