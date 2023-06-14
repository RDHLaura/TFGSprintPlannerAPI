package com.tfg.sprintplannerapi.controller;

import com.tfg.sprintplannerapi.bo.TaskBO;
import com.tfg.sprintplannerapi.dto.TaskDTO;
import com.tfg.sprintplannerapi.model.states.StateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.data.domain.Sort.Direction.ASC;

@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired private TaskBO taskBO;

    @GetMapping("/sprint/{id}")
    public ResponseEntity<Page<TaskDTO>> getAllTask(
            @PageableDefault(page = 0, size = 10) @SortDefault(sort = "deadline", direction = ASC) Pageable pageable,
            @PathVariable( name = "id") Long id) {
        Page<TaskDTO> tasks = taskBO.findAllTaskPage(pageable, id);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping("/sprint/{id}")
    public ResponseEntity<TaskDTO> newTask(@PathVariable Long id, @RequestBody TaskDTO dto ) throws NoSuchMethodException {
        TaskDTO task = taskBO.createTask(id, dto);
        return (task != null) ?
                ResponseEntity.ok(task) :
                ResponseEntity.badRequest().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @Validated @RequestBody TaskDTO dto) {

        TaskDTO task = taskBO.updateTask(id, dto);
        return (task == null) ?
                ResponseEntity.badRequest().build() :
                ResponseEntity.ok(task);
    }

    @PutMapping("/{id}/state")
    public ResponseEntity<TaskDTO> updateStateTask(@PathVariable Long id, @RequestParam(name = "state") StateTask state) {
        TaskDTO task = taskBO.updateStateTask(id, state);
        return (task == null) ?
                ResponseEntity.badRequest().build() :
                ResponseEntity.ok(task);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOneTask(@PathVariable Long id) {
        taskBO.deleteTask(id);
        return ResponseEntity.ok().build();
    }
}
