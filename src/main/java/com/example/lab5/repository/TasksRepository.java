package com.example.lab5.repository;

import com.example.lab5.entity.Tasks;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, Long> {

    List<Tasks> findByUserUsernameOrderByPriorityAscDueDateAsc(String username);

    List<Tasks> findByUserUsernameAndStatusOrderByPriorityAscDueDateAsc(String username, String status);

    List<Tasks> findByUserUsernameAndPriorityOrderByPriorityAscDueDateAsc(String username, String priority);

    List<Tasks> findByUserUsernameAndStatusAndPriorityOrderByPriorityAscDueDateAsc(String username, String status, String priority);

}
