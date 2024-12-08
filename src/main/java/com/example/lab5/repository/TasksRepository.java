package com.example.lab5.repository;

import com.example.lab5.entity.Tasks;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, Long> {

    Page<Tasks> findAll(Pageable pageable);

    // ORDER THE LIST OF TASKS BY DUEDATE AND PRIORITY
    Page<Tasks> findByUserUsernameOrderByPriorityAscDueDateAsc(String username, Pageable pageable);

    // FIND TASKS BY USERNAME AND STATUS, ORDERED BY PRIORITY
    Page<Tasks> findByUserUsernameAndStatusOrderByPriorityAscDueDateAsc(String username, String status, Pageable pageable);

    // FIND TASKS BY USERNAME AND PRIORITY
    Page<Tasks> findByUserUsernameAndPriorityOrderByPriorityAscDueDateAsc(String username, String priority, Pageable pageable);

    // FIND TASKS BY USERNAME, STATUS AND PRIORITY
    Page<Tasks> findByUserUsernameAndStatusAndPriorityOrderByPriorityAscDueDateAsc(String username, String status, String priority, Pageable pageable);

    Page<Tasks> findByStatus(String status, Pageable pageable);
    Page<Tasks> findByPriority(String priority, Pageable pageable);
    Page<Tasks> findByStatusAndPriority(String status, String priority, Pageable pageable);

    @Query("SELECT t FROM Tasks t WHERE t.title LIKE %:keyword% OR t.description LIKE %:keyword%")
    Page<Tasks> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
